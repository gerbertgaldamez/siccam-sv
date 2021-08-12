package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.apache.axis.AxisFault;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.icon.gac.ServicioCasos;
import com.icon.gac.ServicioCasosService;
import com.icon.gac.ServicioCasosServiceLocator;
import com.icon.gac.ServicioCasosSoapBindingStub;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.dao.CBConciliacionDetalleDAO;
import com.terium.siccam.dao.CBHistorialAccionDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCausasConciliacion;
import com.terium.siccam.model.CBResumenDiarioConciliacionModel;
import com.terium.siccam.model.CBConciliacionDetallada;
import com.terium.siccam.model.CBHistorialAccionModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;
import com.terium.siccam.utils.UtilidadesReportes;

import _1._0._0._127.PagoOnLineWS;
import _1._0._0._127.PagoOnLineWSLocator;
import _1._0._0._127.PagoOnLineWSPort;
import _1._0._0._127.PagoOnLineWSPortStub;
import dto.pagoonlinecommon.gte.tmmas.com.PagoDTO;
import dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO;

public class ConciliacionDetalleController extends ControladorBase {

	/**
	 * @author Freddy Ayala to terium.com
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = Logger.getLogger(ConciliacionDetalleController.class.getName());

	// Variables ZUL
	Listbox lbxConciliacionDetalle;
	Listhead lstheadDetalle;
	Combobox cmbTipo;
	Combobox cmbEstado;
	Combobox cmbAgencia;
	Datebox dtbDia;
	Datebox dtbHasta;
	Window wdwHistorial;
	Button btnGeneraReporte;
	Label lblAgencia;
	Label lblDia;
	Button btnAcciones;
	Button btnReenviarManual;
	Button btnEliminarAccion;
	Checkbox ckbDeleteAll;
	Intbox txtNumYCuenta;
	// Variables ZUL wdwHistorial;
	Listbox lbxHistorialAcciones;
	Combobox cmbAccion;
	Textbox txtObservaciones;
	Textbox txtMonto;
	Button btnAgregar;
	Button btnGuardar;
	Button btnEliminar;
	Label lblPendiente;
	Textbox txtPendiente;
	Toolbarbutton btnCerrar;
	String fechaInicio;
	String fechaHasta;

	// Variables ZUL wdwHistorial2
	Window wdwHistorial2;
	Combobox cmbAccion2;
	Textbox txtObservaciones2;
	Button btnGuardar2;
	Toolbarbutton btnCerrar2;
	Toolbarbutton btnTestWS;

	private boolean realizoCambios = false;
	DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");

	// estado de ingreso manual
	boolean actualizado = false;

	// Instancias wdwHistorial
	CBConciliacionDetallada conciliacionDetalle;

	// Instancias DAO wdwHistorial
	CBHistorialAccionDAO historialDao = new CBHistorialAccionDAO();

	// InstanciasGlobales wdwHistorial
	CBResumenDiarioConciliacionModel conciliacion = null;
	Boolean resumenDiario = false;
	// Varibales globales
	Connection conexion = null;
	Set<Listitem> lstSeleccionados = null;
	List<CBConciliacionDetallada> detallesSeleccionados = null;
	// Lista variante
	List<CBConciliacionDetallada> lstConciliacion;
	// Lista que contiene todos los registros con los calculos hechos
	List<CBConciliacionDetallada> lstToda;
	// Lista que contiene todos los registros sin los calculos
	List<CBConciliacionDetallada> listaOriginal;

	// Instancias DAO
	CBConciliacionDetalleDAO conDetalleDao = new CBConciliacionDetalleDAO();

	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);

			crearEncabezadoForCheck();

			if ((Boolean) session.getAttribute("ResumenDiarioConciliacion") != null) {
				resumenDiario = (Boolean) session.getAttribute("ResumenDiarioConciliacion");
				conciliacion = (CBResumenDiarioConciliacionModel) session.getAttribute("conciliacion");
			}
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO,
					"Entra a pantalla desde resumen diario = " + resumenDiario);
			cargarComboAgencia();
			cargarComboEstado();
			llenaComboTipo();

			if (resumenDiario) {

				cmbEstado.setText("Pendiente Conciliar");
				// cmbAgencia.setText("Todas");
				cmbAgencia.setText(conciliacion.getNombre());
				cmbTipo.setText(conciliacion.getTipo());
				System.out.println("Tipo es igual :" + conciliacion.getTipo());
				// lblAgencia.setValue(conciliacion.getNombre());
				// lblDia.setValue(fechaFormato.format(conciliacion.getDia()));

				dtbDia.setText(fechaFormato.format(conciliacion.getDia()));
				dtbHasta.setText(fechaFormato.format(conciliacion.getDia()));
				onClick$btnBuscar();

			} else {
				conciliacion = null;
				cmbEstado.setText("Pendiente Conciliar");
				cmbAgencia.setText("Todas");
				cmbTipo.setText("TODOS");
				conciliacion = null;

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @author Omar Gomez -QIT 18/07/2017
	 */
	public void cargarComboEstado() {

		CBParametrosGeneralesDAO dao = new CBParametrosGeneralesDAO();

		List<CBParametrosGeneralesModel> list = dao
				.obtenerListaTipoAgrupacion(CBParametrosGeneralesDAO.S_OBTENER_ESTADO_CONCILIACION_DET);

		Iterator<CBParametrosGeneralesModel> iterator = list.iterator();

		CBParametrosGeneralesModel model = null;
		Comboitem item = null;

		while (iterator.hasNext()) {
			model = iterator.next();

			item = new Comboitem();

			item.setLabel(model.getObjeto());
			item.setValue(model.getValorObjeto1());
			item.setParent(cmbEstado);
		}
	}

	// cargar combobox
	public void cargarComboAgencia() {

		CBCatalogoAgenciaDAO cba = new CBCatalogoAgenciaDAO();

		List<CBCatalogoAgenciaModel> lst = cba.obtieneListadoAgencias(null, null, null, null);

		Iterator<CBCatalogoAgenciaModel> iLst = lst.iterator();

		CBCatalogoAgenciaModel obj = null;
		Comboitem item = null;

		// para todas
		obj = new CBCatalogoAgenciaModel();
		obj.setNombre("Todas");
		obj.setcBCatalogoAgenciaId("Todas");

		item = new Comboitem();
		item.setLabel(obj.getNombre());
		item.setValue(obj);
		item.setParent(cmbAgencia);
		obj = null;
		while (iLst.hasNext()) {
			obj = iLst.next();

			item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj);
			item.setParent(cmbAgencia);

		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CBConciliacionDetallada> listarTodasConciliacionesFiltradas(String estado, String tel,
			String fechaDesde, String fechaHasta) {
		List lista = null;
		Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO,
				"listarTodasConciliacionesFiltradas()");
		CBCatalogoAgenciaModel agencia = null;
		if (cmbAgencia.getSelectedItem() != null) {
			agencia = cmbAgencia.getSelectedItem().getValue();
		}
		String agenciaId = null;
		if (agencia != null) {
			agenciaId = agencia.getcBCatalogoAgenciaId();
		} else {
			agenciaId = conciliacion.getIdAgencia();
		}

		String tipo = null;

		if (!Constantes.TODOS.equals(cmbTipo.getText())) {
			tipo = cmbTipo.getSelectedItem().getValue().toString().trim();
		} else {
			tipo = "TODOS";
		}

		try {
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO,
					"parametros enviados a consultar fecha desde: " + fechaDesde + "fecha hasta: " + fechaHasta
							+ "agencia " + agenciaId + "estado " + estado + "numero :" + tel + "tipo: " + tipo);
			lista = conDetalleDao.obtenerConciliacionDetalladasFiltros(fechaDesde, fechaHasta, agenciaId, tipo, estado,
					tel);
			if (lista.size() <= 0) {

				Messagebox.show("No se encontraron registros, para los filtros ingresados.", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}

			// System.out.println("termino la ejecucion del query y listado");
		} catch (Exception e) {
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
		}
		return lista;

	}

	@SuppressWarnings("unchecked")
	public void crearEncabezadoForCheck() {

		Listheader lh;
		// Delete marker Checkbox
		lh = new Listheader();
		lh.setWidth("80px");
		lh.setParent(lstheadDetalle);
		Hbox hbox = new Hbox();
		hbox.setParent(lh);
		Label lbl = new Label("Todos");
		lbl.setParent(hbox);
		ckbDeleteAll = new Checkbox();
		ckbDeleteAll.setParent(hbox);
		ckbDeleteAll.setChecked(false);
		ckbDeleteAll.setStyle("padding-left: 2px; padding-right: 3px;");
		ckbDeleteAll.setTooltiptext("Seleccionar Todos ");
		ckbDeleteAll.addEventListener("onCheck", evlOnClick_checkAll);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void listarConciliacionesDetalle() {
		limpiarListboxYPaginas(lbxConciliacionDetalle);
		DateFormat fechaHora = new SimpleDateFormat("dd/MM/yyyy");
		try {

			Iterator<CBConciliacionDetallada> ilst = lstConciliacion.iterator();

			try {
				if (lstConciliacion != null && lstConciliacion.size() > 0) {

					CBConciliacionDetallada adr = null;
					Listcell cell = null;
					Listitem fila = null;
					while (ilst.hasNext()) {
						adr = ilst.next();
						fila = new Listitem();

						// Entidad
						cell = new Listcell();
						cell.setLabel(adr.getAgencia());
						cell.setParent(fila);

						// Dia
						cell = new Listcell();
						cell.setLabel(fechaHora.format(adr.getDia()));
						cell.setParent(fila);

						// Tipo
						cell = new Listcell();
						cell.setLabel(adr.getTipo());
						cell.setParent(fila);

						// Cliente
						cell = new Listcell();
						cell.setLabel(adr.getCliente());
						cell.setParent(fila);

						// Nombre
						cell = new Listcell();
						cell.setLabel(adr.getNombre());
						cell.setParent(fila);

						// Des Pago
						cell = new Listcell();
						cell.setLabel(adr.getDesPago());
						cell.setParent(fila);

						// Trans Telca
						cell = new Listcell();
						cell.setLabel(adr.getTransTelca());
						cell.setParent(fila);

						// Telefono
						cell = new Listcell();
						cell.setLabel(adr.getTelefono());
						cell.setParent(fila);

						// Trans Banco
						cell = new Listcell();
						cell.setLabel(adr.getTransBanco());
						cell.setParent(fila);

						// Pago telefonica
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getImpPago()));
						cell.setParent(fila);

						// Pago Banco
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getMonto()));
						cell.setParent(fila);

						// Manual
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getManual()));
						cell.setParent(fila);

						// Si pago telefonica == 0 && pago telefonica != pago
						// Banco ; calcular pendiente Telefonica , pendiente
						// banco = 0
						if ((adr.getImpPago().compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getImpPago().compareTo(adr.getMonto()) != 0)) {
							// El pago esta en Telefonica entonces solo se
							// calculara pendiente Telefonica y el otro sera 0

							// Pendiente Banco
							cell = new Listcell();
							cell.setStyle("text-align: right");
							adr.setPendienteBanco(BigDecimal.ZERO);
							cell.setLabel("0.00");
							cell.setParent(fila);

							// Pendiente Telefonica
							adr = calcularPendienteConciliarTelefonica(adr);
							cell = new Listcell();
							cell.setStyle("text-align: right");
							cell.setLabel(convertirADecimal(adr.getPendienteTelefonica()));
							cell.setParent(fila);
						}
						// Si pago banco == 0 && pago banco != pago telefonica;
						// calcular pendiente Banco , pendiente banco = 0
						if (adr.getMonto().compareTo(BigDecimal.ZERO) == 0
								&& (adr.getMonto().compareTo(adr.getImpPago()) != 0)) {
							// El pago es Banco entonces solo se calcula
							// pendiente Banco y el otro es 0

							// Pendiente Banco
							adr = calcularPendienteConciliarBanco(adr);
							cell = new Listcell();
							cell.setStyle("text-align: right");
							cell.setLabel(convertirADecimal(adr.getPendienteBanco()));
							cell.setParent(fila);

							// Pendiente Telefonica
							cell = new Listcell();
							cell.setStyle("text-align: right");
							adr.setPendienteTelefonica(BigDecimal.ZERO);
							cell.setLabel("0.00");
							cell.setParent(fila);

						}
						// Si pago banco == pago telefonica; pendiente banco =
						// pendiente telefonica = 0
						if ((adr.getMonto().compareTo(adr.getImpPago()) == 0)) {

							// Pendiente Banco
							cell = new Listcell();
							cell.setStyle("text-align: right");
							adr.setPendienteBanco(BigDecimal.ZERO);
							cell.setLabel("0.00");
							cell.setParent(fila);

							// Pendiente Telefonica
							cell = new Listcell();
							cell.setStyle("text-align: right");
							adr.setPendienteTelefonica(BigDecimal.ZERO);
							cell.setLabel("0.00");
							cell.setParent(fila);
						}

						// Sucursal
						cell = new Listcell();
						cell.setLabel(adr.getSucursal());
						cell.setParent(fila);

						// Nombre sucursal
						cell = new Listcell();
						cell.setLabel(adr.getNombre_sucursal());
						cell.setParent(fila);

						// Tipo sucursal
						cell = new Listcell();
						cell.setLabel(adr.getTipo_sucursal());
						cell.setParent(fila);

						// Comision
						if (adr.getComision().compareTo(BigDecimal.ZERO) == 0) {
							cell = new Listcell();
							cell.setStyle("text-align: center");
							adr.setComision(BigDecimal.ZERO);
							cell.setLabel("0.00");
							cell.setParent(fila);
						} else {
							cell = new Listcell();
							cell.setStyle("text-align: center");
							cell.setLabel(convertirADecimal(adr.getComision()));
							cell.setParent(fila);
						}

						// Monto comision
						if (adr.getMonto_comision() == null
								|| adr.getMonto_comision().compareTo(BigDecimal.ZERO) == 0) {
							cell = new Listcell();
							cell.setStyle("text-align: center");
							adr.setMonto_comision(BigDecimal.ZERO);
							cell.setLabel("0.00");
							cell.setParent(fila);
						} else {
							cell = new Listcell();
							cell.setStyle("text-align: center");
							cell.setLabel(convertirADecimal(adr.getMonto_comision()));
							cell.setParent(fila);
						}
						// agregado por Gerbert
						cell = new Listcell();
						cell.setLabel(adr.getAplicadoReal());
						cell.setParent(fila);
						
						// agregado por Gerbert
						cell = new Listcell();
						cell.setLabel(adr.getRespuestaAccion());
						cell.setParent(fila);

						// Estado
						cell = new Listcell();
						cell.setStyle("text-align: center");
						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(BigDecimal.ZERO) == 0)) {
							cell.setImage("/img/cerrado.png");
							adr.setEstado("1");

						}
						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) > 0)
								|| (adr.getPendienteBanco().compareTo(BigDecimal.ZERO) < 0)
								|| (adr.getPendienteTelefonica().compareTo(BigDecimal.ZERO) > 0)) {
							cell.setImage("/img/rojo.png");
							adr.setEstado("2");
						}

						if ((adr.getManual().compareTo(BigDecimal.ZERO) > 0)
								&& (adr.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(BigDecimal.ZERO) == 0)) {
							cell.setImage("/img/amarillo.png");
							adr.setEstado("3");

						}

						// Agregar nuevo campo respuesta accion

						cell.setParent(fila);
						// cambio Gerbert
						cell = new Listcell();
						cell.setLabel("");
						cell.setParent(fila);

						fila.setValue(adr);
						// Modificado por Freddy Ayala - Eliminando Evento Doble
						// Click 28/10/2013
						fila.addEventListener("onDoubleClick", new EventListener() {
							public void onEvent(Event event) throws SQLException, NamingException {
								abrirHistorialDeAcciones();
							}
						});
						fila.setParent(lbxConciliacionDetalle);

					}
				}
			} catch (Exception e) {
				Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	@SuppressWarnings("unchecked")
	public void abrirHistorialDeAcciones() throws SQLException, NamingException {

		if (lbxConciliacionDetalle.getSelectedItem() != null) {
			conciliacionDetalle = lbxConciliacionDetalle.getSelectedItem().getValue();

			// Instanciamos cada componente de la ventanilla
			cmbAccion = (Combobox) wdwHistorial.getFellow("cmbAccion");
			txtObservaciones = (Textbox) wdwHistorial.getFellow("txtObservaciones");
			txtMonto = (Textbox) wdwHistorial.getFellow("txtMonto");
			txtPendiente = (Textbox) wdwHistorial.getFellow("txtPendiente");
			lblPendiente = (Label) wdwHistorial.getFellow("lblPendiente");
			lbxHistorialAcciones = (Listbox) wdwHistorial.getFellow("lbxHistorialAcciones");
			Toolbarbutton btnAgregar = (Toolbarbutton) wdwHistorial.getFellow("btnAgregar");
			Toolbarbutton btnGuardar = (Toolbarbutton) wdwHistorial.getFellow("btnGuardar");
			Toolbarbutton btnEliminar = (Toolbarbutton) wdwHistorial.getFellow("btnEliminar");
			btnCerrar = (Toolbarbutton) wdwHistorial.getFellow("btnCerrar");

			// eventos de botones
			btnAgregar.removeEventListener("onClick", evlOnClick_btnAgregarHistorico);
			btnAgregar.addEventListener("onClick", evlOnClick_btnAgregarHistorico);

			btnGuardar.removeEventListener("onClick", evlOnClick_btnGuardarHistorico);
			btnGuardar.addEventListener("onClick", evlOnClick_btnGuardarHistorico);

			btnEliminar.removeEventListener("onClick", evlOnClick_btnEliminarHistorico);
			btnEliminar.addEventListener("onClick", evlOnClick_btnEliminarHistorico);
			btnCerrar.addEventListener("onClick", evlOnClick_btnCancelar);
			lbxHistorialAcciones.addEventListener("onClick", evlOnClick_lbxHistorico);
			// Llenamos los componentes
			cargarComboAcciones(cmbAccion);
			txtMonto.setText("");
			txtMonto.setDisabled(false);
			cmbAccion.setText("");
			txtObservaciones.setText("");
			txtPendiente.setText(obtenerPendienteLabel(conciliacionDetalle, lblPendiente));
			txtPendiente.setReadonly(true);

			listarHistorialDeAcciones(conciliacionDetalle, lbxHistorialAcciones);
			wdwHistorial.doModal();
		}

	}

	@SuppressWarnings("unchecked")
	public void abrirHistorialDeAccionesMasivas() throws SQLException, NamingException {

		// if (lbxConciliacionDetalle.getSelectedItem() != null) {

		// Instanciamos cada componente de la ventanilla
		cmbAccion2 = (Combobox) wdwHistorial2.getFellow("cmbAccion2");
		txtObservaciones2 = (Textbox) wdwHistorial2.getFellow("txtObservaciones2");

		Toolbarbutton btnGuardar2 = (Toolbarbutton) wdwHistorial2.getFellow("btnGuardar2");
		btnCerrar2 = (Toolbarbutton) wdwHistorial2.getFellow("btnCerrar2");

		// eventos de botones

		btnGuardar2.removeEventListener("onClick", evlOnClick_btnGuardarHistorico2);
		btnGuardar2.addEventListener("onClick", evlOnClick_btnGuardarHistorico2);
		btnCerrar2.addEventListener("onClick", evlOnClick_btnCancelar2);

		// temp JC
		// System.out.println("Carga boton de test");
		// Toolbarbutton btnTestWS = (Toolbarbutton)
		// wdwHistorial2.getFellow("btnTestWS");
		// btnTestWS.removeEventListener("onClick", evlOnClick_btnTestWS);
		// btnTestWS.addEventListener("onClick", evlOnClick_btnTestWS);

		// Llenamos los componentes
		cargarComboAcciones(cmbAccion2);
		cmbAccion2.setText("");
		txtObservaciones2.setText("");
		wdwHistorial2.doModal();
		// }

	}

	public void listarConciliacionesAux(List<CBConciliacionDetallada> lista) {
		lstToda = new ArrayList<CBConciliacionDetallada>();
		try {

			Iterator<CBConciliacionDetallada> ilst = lista.iterator();

			try {
				if (lista != null && lista.size() > 0) {

					CBConciliacionDetallada adr = null;

					while (ilst.hasNext()) {
						adr = ilst.next();

						adr = calcularPendienteConciliarBanco(adr);

						adr = calcularPendienteConciliarTelefonica(adr);

						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(BigDecimal.ZERO) == 0)) {

							adr.setEstado("1");
						}
						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) > 0)
								|| (adr.getPendienteTelefonica().compareTo(BigDecimal.ZERO) > 0)) {

							adr.setEstado("2");
						}
						if ((adr.getManual().compareTo(BigDecimal.ZERO) > 0)
								&& (adr.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(BigDecimal.ZERO) == 0)) {

							adr.setEstado("3");
						}

						lstToda.add(adr);

					}
				}
			} catch (Exception e) {
				Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	private CBConciliacionDetallada calcularPendienteConciliarBanco(CBConciliacionDetallada obj) {
		// String label = null;
		BigDecimal pendienteConciliarBanco;

		BigDecimal impPago = obj.getImpPago();
		BigDecimal monto = obj.getMonto();
		BigDecimal manual = obj.getManual();
		// monto-impago+manual
		pendienteConciliarBanco = impPago.subtract(monto);
		pendienteConciliarBanco = pendienteConciliarBanco.subtract(manual);
		obj.setPendienteBanco(pendienteConciliarBanco);

		return obj;

	}

	private CBConciliacionDetallada calcularPendienteConciliarTelefonica(CBConciliacionDetallada obj) {
		// String label = null;
		BigDecimal conciliarTelefonica;
		BigDecimal impPago = obj.getImpPago();
		BigDecimal monto = obj.getMonto();
		BigDecimal manual = obj.getManual();
		// impPago-monto+manual
		conciliarTelefonica = monto.subtract(impPago);
		conciliarTelefonica = conciliarTelefonica.subtract(manual);

		obj.setPendienteTelefonica(conciliarTelefonica);
		// label = conciliarTelefonica.toBigInteger().toString();

		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onChange$cmbEstadoSinQuery() {

		// String op = cmbEstado.getText();

		if (lstConciliacion != null) {
			lstConciliacion.clear();
		}

		if (!cmbEstado.getSelectedItem().getLabel().equals("Todos")) {

			String estado = cmbEstado.getSelectedItem().getValue();

			System.out.println("estado combo " + estado);

			iteradorConciliacion(estado);

		} else {

			System.out.println("estado combo  todos");

			lstConciliacion = (List<CBConciliacionDetallada>) ((ArrayList) lstToda).clone();
		}

		listarConciliacionesDetalle();
	}

	// ---------- FILTROS -------------------------

	public void onClick$btnBuscar() {

		// buscarFecha();

		if (dtbDia.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		if (dtbHasta.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha Hasta antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		fechaInicio = fechaFormato.format(dtbDia.getValue());
		fechaHasta = fechaFormato.format(dtbHasta.getValue());

		filtrar(fechaInicio, fechaHasta);
	}

	// ---------- FILTROS FIN -------------------------
	public void onSelectAll$lbxConciliacionDetalle() {
		System.out.println("Entra al evento OnSelectAll");
	}

	public void iteradorConciliacion(String estado) {
		Iterator<CBConciliacionDetallada> ilst = lstToda.iterator();
		if (lstToda.size() > 0) {
			CBConciliacionDetallada adr = null;
			while (ilst.hasNext()) {
				adr = ilst.next();
				if (adr.getEstado().equals(estado)) {
					lstConciliacion.add(adr);
				}
			}
		}

	}

	public void iteradorConciliacionAgencia(CBCatalogoAgenciaModel agen) {

		Iterator<CBConciliacionDetallada> ilst = lstToda.iterator();
		if (lstToda.size() > 0) {
			CBConciliacionDetallada adr = null;
			while (ilst.hasNext()) {
				adr = ilst.next();
				if (adr.getAgencia().equals(agen.getNombre())) {
					lstConciliacion.add(adr);
				}
			}
		}

	}

	public void iteradorConciliacionDia(String dia) {
		Iterator<CBConciliacionDetallada> ilst = lstToda.iterator();
		if (lstToda != null && lstToda.size() > 0) {
			CBConciliacionDetallada adr = null;
			while (ilst.hasNext()) {
				adr = ilst.next();
				if (adr.getDia().toString().equals(dia)) {
					lstConciliacion.add(adr);
				}
			}
		}

	}

	public void limpiarListboxYPaginas(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);

			}
		}
	}

	public String convertirADecimal(BigDecimal num) {
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		String numero = null;
		BigDecimal numConv = num;
		if (num.compareTo(BigDecimal.ZERO) == 0) {
			numConv = new BigDecimal("0.00");
		}
		numero = df.format(numConv);
		return numero;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnAcciones() throws SQLException, NamingException {
		System.out.println("Click al conciliar manual");

		if (ckbDeleteAll.isChecked() || lbxConciliacionDetalle.getSelectedItem() != null) {
			if (ckbDeleteAll.isChecked()) {
				Messagebox.show(
						"Ha seleccionado Conciliar Todo, este proceso podria tardar varios minutos , esta seguro que desea realizarlo?",
						"Advertencia", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {

							public void onEvent(Event event) throws Exception {

								if (((Integer) event.getData()).intValue() != Messagebox.YES) {
									return;
								} else {
									abrirHistorialDeAccionesMasivas();
									return;
								}

							}
						});

			} else {
				// SOLO LOS SELECCIONADOS
				lstSeleccionados = lbxConciliacionDetalle.getSelectedItems();
				Iterator<Listitem> iSeleccionados = lstSeleccionados.iterator();
				Listitem item = null;
				CBConciliacionDetallada obj = null;
				detallesSeleccionados = new ArrayList<CBConciliacionDetallada>();
				while (iSeleccionados.hasNext()) {
					item = iSeleccionados.next();
					obj = item.getValue();
					// Pasamos los listitem a objetos detalle y los guardamos en
					// lista
					detallesSeleccionados.add(obj);
					
				}
				abrirHistorialDeAccionesMasivas();
			}

		} else {

			Messagebox.show("Debe seleccionar por lo menos un detalle", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
	}

	/**
	 * Metodo para reenviar peticiones a WS
	 */
	public void onClick$btnReenviarManual() throws SQLException, NamingException {

		if (lbxConciliacionDetalle.getSelectedItem() != null) {
			List<CBParametrosGeneralesModel> parametros = CBParametrosGeneralesDAO.obtenerParametrosWS();
			
			lstSeleccionados = lbxConciliacionDetalle.getSelectedItems();
			Iterator<Listitem> iSeleccionados = lstSeleccionados.iterator();
			Listitem item = null;
			CBConciliacionDetallada obj = null;

			while (iSeleccionados.hasNext()) {
				item = iSeleccionados.next();

				obj = item.getValue();

				System.out.println("Respuesta accion: " + obj.getRespuestaAccion());
				System.out.println("CBHistorialAccionId: " + obj.getCbHistorialAccionId());
				System.out.println("Sistema: " + obj.getSistema());
				
				CBHistorialAccionModel cbHistorial = new CBHistorialAccionModel();
				
				// Se obtiene la secuencia y se setea a objeto
				cbHistorial.setcBHistorialAccionId(obj.getCbHistorialAccionId());
				cbHistorial.setCbCausasConciliacionId(obj.getCbCausasConciliacionId());
				cbHistorial.setObservaciones(obj.getObservacion());
				
				// Request WS Pagos - Reenvio
				procesaWSPagosCreaCasos(parametros, obj, cbHistorial, true);
			}
			
			Messagebox.show("El reenvio de las acciones fue ejecutado de forma correcta!", "ATENCION", Messagebox.OK,
					Messagebox.INFORMATION);

		} else {

			Messagebox.show("Debe seleccionar por lo menos un detalle a reenviar", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
	}
	public void onClick$btnEliminarAccion() throws SQLException, NamingException {
        
		if (lbxConciliacionDetalle.getSelectedItem() != null) {
			//List<CBParametrosGeneralesModel> parametros = CBParametrosGeneralesDAO.obtenerParametrosWS();
			
			lstSeleccionados = lbxConciliacionDetalle.getSelectedItems();
			Iterator<Listitem> iSeleccionados = lstSeleccionados.iterator();
			Listitem item = null;
			CBConciliacionDetallada obj = null;
			

			while (iSeleccionados.hasNext()) {
				item = iSeleccionados.next();

				obj = item.getValue();
                               
				CBConciliacionDetalleDAO detalle = new CBConciliacionDetalleDAO();
				
				// Request WS Pagos - Reenvio
				detalle.eliminarRegistros(obj.getCbHistorialAccionId());
				System.out.println("Llega anates del metodo llenar lista");
				
				listarConciliacionesDetalle();
				onClick$btnBuscar();
				
				
				
				
				
				
				System.out.println("Llega despues del metodo llenar lista");
			}
			listarConciliacionesDetalle();
			Messagebox.show("El Registro fue Eliminado de forma correcta!", "ATENCION", Messagebox.OK,
					Messagebox.INFORMATION);
			
			
		} else {
			
			Messagebox.show("Debe seleccionar por lo menos un detalle a Eliminar", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			
			return;
			
		}
		
	}
	
	

	public void seleccionarTodosLosRegistros() {

		List<Listitem> list = lbxConciliacionDetalle.getItems();

		if (list.size() > 0) {
			detallesSeleccionados = new ArrayList<CBConciliacionDetallada>();
			for (Listitem li : list) {
				li.setSelected(true);
				CBConciliacionDetallada entity = (CBConciliacionDetallada) li.getValue();
				detallesSeleccionados.add(entity);

			}
		}
	}
	
	
	public void deseleccionarTodosLosRegistros() {
		List<Listitem> list = lbxConciliacionDetalle.getItems();
		if (list.size() > 0) {
			detallesSeleccionados = new ArrayList<CBConciliacionDetallada>();
			for (Listitem li : list) {
				li.setSelected(false);
				detallesSeleccionados = new ArrayList<CBConciliacionDetallada>();
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	// Ventana HISTORIAL ACCIONES

	public void listarHistorialDeAcciones(CBConciliacionDetallada conDet, Listbox lbx) {
		limpiarListboxYPaginas(lbxHistorialAcciones);
		List<CBHistorialAccionModel> lst = null;
		String idConciliacion = null;
		try {
			idConciliacion = conDet.getConciliacionId();
			System.out.println("ID Conciliacion : " + conDet.getConciliacionId().toString());
			lst = historialDao.obtenerCBHistorialAcciones(idConciliacion);
			Iterator<CBHistorialAccionModel> ilst = lst.iterator();
			try {
				if (lst.size() > 0) {

					CBHistorialAccionModel adr = null;
					Listcell cell = null;
					Listitem fila = null;
					while (ilst.hasNext()) {
						adr = ilst.next();
						fila = new Listitem();

						cell = new Listcell();
						cell.setLabel(adr.getAccion());
						cell.setParent(fila);

						cell = new Listcell();
						cell.setLabel(adr.getMonto());
						cell.setParent(fila);

						cell = new Listcell();
						cell.setLabel(adr.getObservaciones());
						cell.setParent(fila);

						cell = new Listcell();
						cell.setLabel(adr.getCreadoPor());
						cell.setParent(fila);

						cell = new Listcell();
						cell.setLabel(adr.getFechaCreacion());
						cell.setParent(fila);

						cell = new Listcell();
						cell.setLabel(adr.getModificadoPor());
						cell.setParent(fila);

						cell = new Listcell();
						cell.setLabel(adr.getFechaModificacion());
						cell.setParent(fila);

						fila.setValue(adr);
						fila.setParent(lbx);

					}
				}
			} catch (Exception e) {
				Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	public void onClick$lbxHistorialAcciones() {
		if (lbxHistorialAcciones.getSelectedItem() != null) {
			CBHistorialAccionModel cbHistorial = lbxHistorialAcciones.getSelectedItem().getValue();
			cmbAccion.setText(cbHistorial.getAccion());
			txtMonto.setText(cbHistorial.getMonto());
			txtObservaciones.setText(cbHistorial.getObservaciones());
		}
	}

	// MANTENIMIENTOS
	// AGREGAR
	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_btnAgregarHistorico = new EventListener() {
		public void onEvent(Event event) {
			lbxHistorialAcciones.setSelectedItem(null);
			txtMonto.setDisabled(false);
			setRealizoCambios(true);
			limpiarCampos();

		}
	};

	// GUARDAR
	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_btnGuardarHistorico2 = new EventListener() {
		public void onEvent(Event event) {

			String idPadre = null;
			setRealizoCambios(true);
			if (!cmbAccion2.getText().equals("") && !txtObservaciones2.getText().equals("")) {
				// juankrlos -> 19/06/2021
				List<CBParametrosGeneralesModel> parametros = CBParametrosGeneralesDAO.obtenerParametrosWS();

				try {
					Iterator<CBConciliacionDetallada> iDetalles = detallesSeleccionados.iterator();

					while (iDetalles.hasNext()) {
						CBConciliacionDetallada det = iDetalles.next();
						CBCausasConciliacion objCausas = cmbAccion2.getSelectedItem().getValue();
						idPadre = det.getConciliacionId();
						CBHistorialAccionModel cbHistorial = new CBHistorialAccionModel();
						
						// Se obtiene la secuencia y se setea a objeto
						cbHistorial.setcBHistorialAccionId(CBHistorialAccionDAO.obtieneSecuenciaHistorial());
						cbHistorial.setAccion(cmbAccion2.getText());
						cbHistorial.setCbCausasConciliacionId(Integer.parseInt(objCausas.getId()));
						cbHistorial.setObservaciones(txtObservaciones2.getText());
						cbHistorial.setMonto(obtenerPendiente(det));
						cbHistorial.setCreadoPor(obtenerUsuario().getUsuario());

						// INSERTAR
						historialDao.insertarReg(cbHistorial, idPadre);

						// Request WS Pagos
						procesaWSPagosCreaCasos(parametros, det, cbHistorial, false);
					}

					actualizado = true;
					//Clients.showBusy("Procesando...");
					Events.echoEvent("onClick", btnCerrar2, null);

				} catch (Exception e) {
					logger.log(Level.SEVERE, "Error click btnGuardarHistorico2", e);
				}
			} else {
				Messagebox.show("Se debe elegir una acción y observación", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		}
	};

	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_btnTestWS = new EventListener() {
		public void onEvent(Event event) {
			List<CBParametrosGeneralesModel> parametros = CBParametrosGeneralesDAO.obtenerParametrosWS();
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO, "Se ejecuta test");
			try {
				Iterator<CBConciliacionDetallada> iDetalles = detallesSeleccionados.iterator();

				while (iDetalles.hasNext()) {
					CBConciliacionDetallada detalle = iDetalles.next();

					CBHistorialAccionModel cbHistorial = new CBHistorialAccionModel();
					cbHistorial.setAccion(cmbAccion2.getText());
					cbHistorial.setObservaciones(txtObservaciones2.getText());
					cbHistorial.setMonto(obtenerPendiente(detalle));
					cbHistorial.setCreadoPor(obtenerUsuario().getUsuario());

					procesaWSPagosCreaCasos(parametros, detalle, cbHistorial, false);
				}

			} catch (Exception e) {
				Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
			}

		}
	};

	/**
	 * @author juankrlos
	 * @param parametros
	 * @param detalle
	 * @date 19/06/2021
	 * @description: Metodo para consumo de WS crearCasoCerrado
	 */
	public void procesaWSPagosCreaCasos(List<CBParametrosGeneralesModel> parametros, CBConciliacionDetallada detalle,
			CBHistorialAccionModel historial, boolean reenvio) {
		CBCausasConciliacion obj = null;
		if(reenvio) {
			obj = new CBCausasConciliacion();
			obj.setSistema(detalle.getSistema());
		} else {
			obj = cmbAccion2.getSelectedItem().getValue();
		}
		logger.log(Level.INFO, "Valor para pendiente Telefonica: "+detalle.getPendienteTelefonica());
		if (obj.getSistema() == 2 && detalle.getPendienteTelefonica().intValue() > 0) {
			// ejecuta request Pago
			RespuestaPagoDTO response = requestWsAplicarPago(parametros, detalle);

			if (response != null && response.getRespuesta().getCodigoError() == 0) {
				historial.setEstado(1);// Estado 1 = pendiente para procesar por GAC
				historial.setNombreCliente(response.getNombreCliente());
				historial.setRespuestascl("Pago Ejecutado Correctamente");
			} else {
				historial.setEstado(4);// Estado 2 = error por parte de WS Pagos
				historial.setRespuestascl(response.getRespuesta().getMensajeError());
			}
			logger.log(Level.INFO, "Ingresa a ejecutar WS Pagos");
			// ejecuta request crearCasoCerrado
			// requestWsCrearCasoCerrado(parametros, detalle);
		} else if (obj.getSistema() == 2 && detalle.getPendienteBanco().intValue() > 0) {

			// Se ejecuta SP
			boolean result = CBHistorialAccionDAO.ejecutaApldesRecargaSP(historial.getcBHistorialAccionId());
			logger.log(Level.INFO, "[CB_HISTORIAL_ACCION] Ejecuta SP para id => "
					+ historial.getcBHistorialAccionId() + " Resultado => " + result);
			// ejecuta request crearCasoCerrado
			// requestWsCrearCasoCerrado(parametros, detalle);
		}else if(obj.getSistema() == 1) {
			// Se ejecuta SP
						boolean result = CBHistorialAccionDAO.ejecutaApldesRecargaSP(historial.getcBHistorialAccionId());
						logger.log(Level.INFO, "[CB_HISTORIAL_ACCION] Ejecuta SP para id => "
								+ historial.getcBHistorialAccionId() + " Resultado => " + result);
			
		}else if(obj.getSistema() == 3) {
			// Se ejecuta SP
						boolean result = CBHistorialAccionDAO.ejecutaApldesRecargaSP(historial.getcBHistorialAccionId());
						logger.log(Level.INFO, "[CB_HISTORIAL_ACCION] Ejecuta SP para id => "
								+ historial.getcBHistorialAccionId() + " Resultado => " + result);
		}

		historial.setTipologiaGacId(Tools.obtenerParametro(Constantes.TIPOLOGIAGACID, parametros));
		historial.setUnidadId(Tools.obtenerParametro(Constantes.UNIDADID, parametros));
		historial.setSolucion(Tools.obtenerParametro(Constantes.SOLUCION, parametros));
		historial.setTipoCierre(Tools.obtenerParametro(Constantes.TIPO_CIERRE, parametros));

		boolean resultado = historialDao.actualizaHistorialAcciones(historial);
		logger.log(Level.INFO, "[CB_HISTORIAL_ACCION] Finaliza update para registro => "
				+ historial.getcBHistorialAccionId() + " Resultado => " + resultado);
	}

	/**
	 * @author juankrlos
	 * @param parametros
	 * @param detalle
	 * @date 19/06/2021
	 * @description: Metodo para consumo de WS crearCasoCerrado
	 */
	public void requestWsCrearCasoCerrado(List<CBParametrosGeneralesModel> parametros,
			CBConciliacionDetallada detalle) {
		ServicioCasosService servicio = new ServicioCasosServiceLocator();
		ServicioCasos wsExec;

		try {
			wsExec = new ServicioCasosSoapBindingStub(new URL(servicio.getServicioCasosAddress()), servicio);

			System.out.println("Telefono en request: " + detalle.getTelefono());
			String descripcion = cmbAccion2.getText() + " por conciliación del día " + detalle.getDia() + " de "
					+ detalle.getAgencia();
			String response = wsExec.crearCasoCerrado(Tools.obtenerParametro("TIPOLOGIAGACID", parametros),
					Tools.obtenerParametro("USUARIOGAC", parametros), Tools.obtenerParametro("COMENTARIO", parametros), // comentarioEjecutivo
					detalle.getTelefono(), Tools.obtenerParametro("UNIDADID", parametros), descripcion,
					Tools.obtenerParametro("SOLUCION", parametros), Tools.obtenerParametro("TIPO_CIERRE", parametros), // tipoCierre
					null, null, null, null);
			System.out.println("Respuesta de WS CrearCasoCerrado: " + response);
		} catch (AxisFault e) {
			logger.log(Level.SEVERE, "error Axis: ", e);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "error MalFormed: ", e);
		} catch (RemoteException e) {
			logger.log(Level.SEVERE, "error Remote:", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "error General: ", e);
		}
	}

	/**
	 * @author juankrlos
	 * @param parametros
	 * @param detalle
	 * @date 19/06/2021
	 * @description: Metodo para consumo de WS aplicarPagoOnLine
	 */
	public RespuestaPagoDTO requestWsAplicarPago(List<CBParametrosGeneralesModel> parametros,
			CBConciliacionDetallada detalle) {
		PagoOnLineWS servicio = new PagoOnLineWSLocator();
		RespuestaPagoDTO response = null;
		PagoOnLineWSPort ws;

		try {
			ws = new PagoOnLineWSPortStub(new URL(servicio.getPagoOnLineWSPortAddress()), servicio);

			PagoDTO obj = setParamsRequestPagos(parametros, detalle);

			response = ws.aplicarPagoOnLine(obj);
			logger.log(Level.INFO, "Respuesta WS Pagos: " + response.getNombreCliente());
			logger.log(Level.INFO, "Respuesta WS Pagos: " + response.getStatus());

			return response;
		} catch (AxisFault e) {
			logger.log(Level.SEVERE, "error Axis: ", e);
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "error MalFormed: ", e);
		} catch (RemoteException e) {
			logger.log(Level.SEVERE, "error Remote:", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "error General: ", e);
		}

		return response;
	}

	public PagoDTO setParamsRequestPagos(List<CBParametrosGeneralesModel> parametros, CBConciliacionDetallada detalle) {
		DateFormat fechaFormato = new SimpleDateFormat("yyyyMMdd");
		DateFormat horaFormato = new SimpleDateFormat("HHmmss");

		Date objFecha = new Date();
		String fecha = fechaFormato.format(objFecha);
		String hora = horaFormato.format(objFecha);
		String cliente = detalle.getCliente() != null ? detalle.getCliente() : "0";

		long telefono = detalle.getTelefono() != null ? Long.parseLong(detalle.getTelefono()) : 0;

		PagoDTO obj = new PagoDTO();

		obj.setAgencia(Tools.obtenerParametro(Constantes.AGENCIA, parametros));
		obj.setBancoTarjetaDebito("");
		obj.setCajero(Tools.obtenerParametro(Constantes.CAJERO, parametros));
		obj.setCodBanco(Tools.obtenerParametro(Constantes.COD_BANCO, parametros));
		obj.setFecha(fecha);
		obj.setHora(hora);
		obj.setMontoChequeBanco(0);
		obj.setMontoChequeOtroBanco(0);
		obj.setMontoEfectivo(detalle.getMonto().doubleValue());
		obj.setMontoTarjetaCredito(0);
		obj.setMontoTarjetaDebito(0);
		obj.setMontoTotalOperacion(detalle.getMonto().doubleValue());
		obj.setNumAutorizacion("");
		obj.setNumBoleta(0);
		obj.setNumCheque(0);
		obj.setNumFactura(1105134882);// Valor temporal
		obj.setNumTarjeta("");
		obj.setNumTelefonoCliente(cliente);
		obj.setTelefono(telefono);
		obj.setTipTarjeta("");
		obj.setTipoOperacion(Tools.obtenerParametro(Constantes.TIPO_OPERACION, parametros));

		return obj;
	}

	// METODO GUARDAR CON LISTBOX
	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_btnGuardarHistorico = new EventListener() {
		public void onEvent(Event event) {
			CBHistorialAccionModel cbHistorial = null;
			String idPadre = null;
			setRealizoCambios(true);
			if (!cmbAccion.getText().equals("") && !txtObservaciones.getText().equals("")) {
				try {
					idPadre = conciliacionDetalle.getConciliacionId();
					if (lbxHistorialAcciones.getSelectedItem() != null) {
						// ACTUALIZAR
						cbHistorial = lbxHistorialAcciones.getSelectedItem().getValue();
						cbHistorial.setAccion(cmbAccion.getText());
						cbHistorial.setMonto(txtMonto.getText());

						Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO,
								"montoen el if getselecteitem " + cbHistorial.getMonto());
						cbHistorial.setObservaciones(txtObservaciones.getText());
						cbHistorial.setModificadoPor(obtenerUsuario().getUsuario());
						historialDao.updateReg(cbHistorial, idPadre);
						

					} else {
						// INSERTAR

						cbHistorial = new CBHistorialAccionModel();
						cbHistorial.setAccion(cmbAccion.getText());
						if (Double.parseDouble(txtMonto.getText()) <= Double.parseDouble(txtPendiente.getText())) {
							cbHistorial.setMonto(txtMonto.getText());
						} else {
							// throw new WrongValueException(txtMonto,
							// Labels.getLabel("El monto no puede sobrepasar lo pendiente"));
							Messagebox.show("El  valor del monto no puede ser mayor al valor pendiente", "ERROR",
									Messagebox.OK, Messagebox.EXCLAMATION);
							return;
						}
						cbHistorial.setObservaciones(txtObservaciones.getText());
						cbHistorial.setCreadoPor(obtenerUsuario().getUsuario());
						historialDao.insertarReg(cbHistorial, idPadre);

					}
					lbxHistorialAcciones.getItems().clear();
					listarHistorialDeAcciones(conciliacionDetalle, lbxHistorialAcciones);
					limpiarCampos();
					actualizado = true;
					//Clients.showBusy("Procesando...");
					Events.echoEvent("onClick", btnCerrar, null);

				} catch (Exception e) {
					Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
				}
			} else {
				Messagebox.show("Se debe elegir una acción y observación", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		}
	};

	// ELIMINAR

	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_btnEliminarHistorico = new EventListener() {
		public void onEvent(Event event) {

			CBConciliacionDetallada conDet = lbxConciliacionDetalle.getSelectedItem().getValue();
			CBHistorialAccionModel cbHistorial = null;
			String idPadre = null;
			setRealizoCambios(true);

			try {
				idPadre = conDet.getConciliacionId();
				if (lbxHistorialAcciones.getSelectedItem() != null) {
					// ELIMINAR
					cbHistorial = lbxHistorialAcciones.getSelectedItem().getValue();
					historialDao.deleteReg(cbHistorial, idPadre);
				} else {
					Messagebox.show("Deber seleccionar un registro", "ERROR", Messagebox.OK, Messagebox.EXCLAMATION);
					return;

				}
				lbxHistorialAcciones.getItems().clear();
				listarHistorialDeAcciones(conciliacionDetalle, lbxHistorialAcciones);
				limpiarCampos();
				actualizado = true;
				Events.echoEvent("onClick", btnCerrar, null);
			} catch (Exception e) {
				Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};

	// CANCELAR
	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_btnCancelar = new EventListener() {
		public void onEvent(Event event) {

			if (actualizado) {
				actualizado = false;
				limpiarCampos();
				wdwHistorial.setVisible(false);
				cmbEstado.setText("Pendiente Conciliar");
				// cmbAgencia.setText("Todas");

				onClick$btnBuscar();
			} else {
				wdwHistorial.setVisible(false);
			}
			Clients.clearBusy();
		}
	};

	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_btnCancelar2 = new EventListener() {
		public void onEvent(Event event) {

			if (actualizado) {
				actualizado = false;
				limpiarCampos2();
				wdwHistorial2.setVisible(false);
				cmbEstado.setText("Pendiente Conciliar");

				onClick$btnBuscar();
				Messagebox.show("Se ha conciliado con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
				
			} else {
				wdwHistorial2.setVisible(false);
			}
			System.out.println("Click en cerrar ventana");
			Clients.clearBusy();
		}
	};

	// CLICK ENN GRID

	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_lbxHistorico = new EventListener() {
		public void onEvent(Event event) {
			if (lbxHistorialAcciones.getSelectedItem() != null) {
				CBHistorialAccionModel ht = lbxHistorialAcciones.getSelectedItem().getValue();
				txtMonto.setDisabled(true);
				cmbAccion.setText(ht.getAccion());
				txtObservaciones.setText(ht.getObservaciones());
			}
		}
	};

	/**
	 * Condicion de pendiente Banco > 0 comentariado por Carlos Godinez
	 * 
	 * Se deben aceptar montos negativos para la conciliacion
	 */
	private String obtenerPendienteLabel(CBConciliacionDetallada cd, Label lb) {
		String pendiente = null;
		// pendiente banco > 0
		// if (cd.getPendienteBanco().compareTo(BigDecimal.ZERO) > 0) {
		pendiente = cd.getPendienteBanco().toString();
		lb.setValue("Pendiente Banco ");
		// }
		// pendiente telefonica > 0
		if (cd.getPendienteTelefonica().compareTo(BigDecimal.ZERO) > 0) {
			pendiente = cd.getPendienteTelefonica().toString();
			lb.setValue("Pendiente Telefonica ");
		}
		Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO,
				"\n**Valor que retorna de la variable pendiente = " + pendiente + "\n");
		return pendiente;
	}

	/**
	 * Condicion de pendiente Banco > 0 comentariado por Carlos Godinez
	 * 
	 * Se deben aceptar montos negativos para la conciliacion
	 */
	private String obtenerPendiente(CBConciliacionDetallada cd) {
		String pendiente = null;
		// pendiente banco > 0
		// if (cd.getPendienteBanco().compareTo(BigDecimal.ZERO) > 0) {
		pendiente = cd.getPendienteBanco().toString();
		// }
		// pendiente telefonica > 0
		if (cd.getPendienteTelefonica().compareTo(BigDecimal.ZERO) > 0) {
			pendiente = cd.getPendienteTelefonica().toString();

		}

		logger.log(Level.INFO,"**Valor que retorna de la variable pendiente = " + pendiente);
		return pendiente;
	}

	// cargar combobox
	public void cargarComboAcciones(Combobox cmb) throws SQLException, NamingException {
		cmb.getItems().clear();
		CBHistorialAccionDAO cba = new CBHistorialAccionDAO();
		List<CBCausasConciliacion> lst = cba.obtieneListadoAcciones(obtieneTipoIdConvenio());

		Iterator<CBCausasConciliacion> iLst = lst.iterator();
		CBCausasConciliacion obj = null;
		Comboitem item = null;
		while (iLst.hasNext()) {
			obj = iLst.next();

			item = new Comboitem();
			item.setLabel(obj.getCausas());
			item.setValue(obj);
			item.setParent(cmb);

		}
	}

	// Obtener tipoId en detalle conciliacion
	public int obtieneTipoIdConvenio() {
		try {
			Iterator<CBConciliacionDetallada> iDetalles = detallesSeleccionados.iterator();
			if (iDetalles.hasNext()) {
				CBConciliacionDetallada det = iDetalles.next();
				return det.getTipoId();
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
		}
		return 0;
	}

	private void limpiarCampos() {
		cmbAccion.setText("");
		txtMonto.setText("");
		txtObservaciones.setText("");
	}

	private void limpiarCampos2() {
		cmbAccion2.setText("");
		txtObservaciones2.setText("");
	}

	public boolean isRealizoCambios() {
		return realizoCambios;
	}

	public void setRealizoCambios(boolean realizoCambios) {
		this.realizoCambios = realizoCambios;
	}

	// CHECKBOX ALL

	@SuppressWarnings("rawtypes")
	EventListener evlOnClick_checkAll = new EventListener() {
		public void onEvent(Event event) {

			if (ckbDeleteAll.isChecked()) {
				seleccionarTodosLosRegistros();
			} else {
				deseleccionarTodosLosRegistros();
			}

		}
	};

	// ******* AGREGO BENJAMIN******
	// *****************************

	/**
	 * Modificado Omar Gomez - QIT - 25/07/2017
	 */

	public void onClick$btnGeneraReporte() {

		Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO,
				"Comienza a generar el reporte de conciliaciones detalle...");

		if (lstConciliacion != null)
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.INFO,
					"lst size: " + lstConciliacion.size());

		// generar reporte detalle conciliacion
		try {
			generarReporteConciliacionesDet();
		} catch (Exception e) {
			Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	/**
	 * Modificado Omar Gomez -QIT- 07/25/2017
	 */
	public void generarReporteConciliacionesDet() throws Exception {

		if (lstConciliacion != null && lstConciliacion.size() > 0) {

			Date fecha = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

			// Obtener la lista para iterar
			Iterator<CBConciliacionDetallada> iterator = lstConciliacion.iterator();

			// Encabezado del archivo
			String encabezado = "AGENCIA|DIA|TIPO|CLIENTE|NOMBRE|DES PAGO|TRANS TELCA|TELEFONO|"
					+ "TRANS BANCO|PAGO TELEFONICA|PAGO BANCO (CANTIDAD A FACTURAR)|MANUAL|PENDIENTE TELEFONICA|"
					+ "PENDIENTE BANCO|COMISION|MONTO COMISION|CODIGO|NOMBRE SUCURSAL|TIPO|APLICADO REAL|RESPUESTA ACCION\n";

			// Creando archivo
			File file = new File("reporte_conciliaciones_detalle" + sdf.format(fecha) + ".csv");
			BufferedWriter bw = null;

			/// *
			try {

				bw = new BufferedWriter(new FileWriter(file));
				bw.write(encabezado);

				CBConciliacionDetallada c = null;
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

				while (iterator.hasNext()) {
					c = iterator.next();

					bw.write(c.getAgencia() + "|" + format.format(c.getDia()) + "|"
							+ UtilidadesReportes.changeNull(c.getTipo()) + "|"
							+ UtilidadesReportes.changeNull(c.getCliente()) + "|"
							+ UtilidadesReportes.changeNull(c.getNombre()) + "|"
							+ UtilidadesReportes.changeNull(c.getDesPago()) + "|"
							+ UtilidadesReportes.changeNull(c.getTransTelca()) + "|"
							+ UtilidadesReportes.changeNull(c.getTelefono()) + "|"
							+ UtilidadesReportes.changeNull(c.getTransBanco()) + "|"
							+ UtilidadesReportes.changeNull(String.valueOf(c.getImpPago())) + "|"
							+ UtilidadesReportes.changeNull(String.valueOf(c.getMonto())) + "|"
							+ UtilidadesReportes.changeNull(String.valueOf(c.getManual())) + "|"
							+ UtilidadesReportes.changeNull(String.valueOf(c.getPendienteTelefonica())) + "|"
							+ UtilidadesReportes.changeNull(String.valueOf(c.getPendienteBanco())) + "|"
							+ UtilidadesReportes.changeNumber(String.valueOf(c.getComision())) + "|"
							+ UtilidadesReportes.changeNumber(String.valueOf(c.getMonto_comision())) + "|"
							+ UtilidadesReportes.changeNull(c.getSucursal()) + "|"
							+ UtilidadesReportes.changeNull(c.getNombre_sucursal()) + "|"
							+ UtilidadesReportes.changeNull(c.getTipo_sucursal()) + "|"
							+ UtilidadesReportes.changeNull(c.getAplicadoReal()) + "|"
							+ UtilidadesReportes.changeNull(c.getRespuestaAccion()) + "\n");// agregado por Gerbert
				} // */

				Filedownload.save(file, null);

				Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
						Messagebox.OK, Messagebox.INFORMATION);

			} catch (Exception e) {
				Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if (bw != null)
					bw.close();
			}

		} else
			Messagebox.show("No hay resultados en la búsqueda para generar reportes", "ATENCIÓN", Messagebox.OK,
					Messagebox.EXCLAMATION);
	}

	// filtro de busqueda
	public void filtrar(String fechaDesde, String fechaHasta) {

		String op = cmbEstado.getText();
		String estado = getStatus(op);
		String valor = txtNumYCuenta.getText();
		
		logger.log(Level.INFO, "Combo Estado " + op);
		
		if (lstConciliacion != null) {
			lstConciliacion.clear();
		}		

		lstConciliacion = listarTodasConciliacionesFiltradas(estado, valor, fechaDesde, fechaHasta);
		listarConciliacionesDetalle();
	}
	
	//Se separa metodo para uso estricto de retornar estado a filtrar
	public String getStatus(String op) {
		String result = "";
		
		if (op.equals("Todos")) {		
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "0";
		}

		if (op.equals("Conciliado Automaticamente")) {			
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "1";

		}
		if (op.equals("Pendiente Conciliar")) {
			btnAcciones.setDisabled(false);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "2";

		}
		if (op.equals("Conciliado Manualmente")) {
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "3";
		}

		/*
		 * agregado para los cambios de ajustes Agrego: Benjamin Escobar 01/09/2014
		 */
		if (op.equals("Ajustes Automaticos")) {
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "4";
		}
		
		if (op.equals("Pendiente Dif Fechas")) {		
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "5";
		}
		
		if (op.equals("Ajuste Aplicado Dif Fechas")) {
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "6";
		}

		if (op.equals("Ajuste No Aplicado Dif Fechas")) {		
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(true);
			btnEliminarAccion.setDisabled(true);
			return "7";
		}
		
		if (op.equals("Error en Conciliacion")) {	
			btnAcciones.setDisabled(true);
			btnReenviarManual.setDisabled(false);
			btnEliminarAccion.setDisabled(false);
			return "8";
		}
		
		return result;
	}

	/**
	 * Llenado de combo tipo
	 */
	public void llenaComboTipo() {
		CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
		List<CBParametrosGeneralesModel> listaTipo = objeDAO.obtenerParamConvenios();
		if (listaTipo != null && listaTipo.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = listaTipo.iterator();
			CBParametrosGeneralesModel obj = null;

			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();
				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto1());
				item.setParent(cmbTipo);
			}
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, "- Llena combo tipo");
		} else {
			Messagebox.show("Error al cargar la configuracion de tipos de conciliacion", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

}
