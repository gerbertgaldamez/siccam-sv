package com.terium.siccam.controller;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import javax.naming.NamingException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.dao.CBCatalogoBancoDaoB;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.dao.CBReportesDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBResumenDiarioConciliacionModel;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBConciliacionDetallada;
import com.terium.siccam.model.CBEntidad;
import com.terium.siccam.model.CBLiquidacionDetalleModel;
import com.terium.siccam.model.CBPagosModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.UtilidadesReportes;

/**
 * @author CarlosGodinez - QitCorp - 05/04/2017
 */
public class CBReportesController extends ControladorBase {
	private static Logger log = Logger.getLogger(CBReportesController.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int tipoReporteSeleccionado;

	// Bloque de componentes utilizados
	private Combobox cmbTipoReporte;
	private Textbox txtUsuario;
	private Datebox dtbInicio;
	private Combobox cmbAgrupacion;
	private Datebox dtbFin;
	private Combobox cmbEntidad;
	private Combobox cmbTipo;
	private Combobox cmbTipoEntidad;
	private Combobox cmbEstado;

	// Bloque de agrupaciones del formulario
	private Label lblTipo;
	private Label lblTipoEntidad;
	private Label lblAgrupacion;
	private Label lblEntidad;

	private Row fila1Grid;
	private Row fila2Grid;
	private Row fila3Grid;
	private Row fila4Grid;
	private Row fila5Grid;

	// Bloque de listas para llenado de combobox
	private List<CBCatalogoBancoModel> listaAgrupacion = new ArrayList<CBCatalogoBancoModel>();
	private List<CBCatalogoAgenciaModel> listaEntidad = new ArrayList<CBCatalogoAgenciaModel>();
	private List<CBParametrosGeneralesModel> listaTipoReporte = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> listaTipo = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> listaTipoEntidad = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> listaEstado = new ArrayList<CBParametrosGeneralesModel>();
	DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		log.debug("doAfterCompose()  " + " - **** Entra al modulo de reporteria ****");
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, 
				//"**** Entra al modulo de reporteria ****");
		llenaComboTipoReporte();
		obtenerTipoReportePredefinido();
		llenaComboAgrupacion();

		llenaComboTipoEntidad();
		llenaComboEstado();
		llenaComboTipo();
		
	}

	/**
	 * Combo convenios
	 */

	private List<CBParametrosGeneralesModel> listParamConvenio = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboConvenios() {
		CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
		listParamConvenio = objDao.obtenerParamConvenios();
		if (listParamConvenio.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = listParamConvenio.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Comboitem();
				item.setLabel(obj.getValorObjeto1());
				item.setValue(obj.getValorObjeto2());
				item.setParent(cmbTipo);
			}
		} else {
			Messagebox.show("Error al cargar la configuracion de cuentas", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	public void obtenerTipoReportePredefinido() {
		CBReportesDAO objeDAO = new CBReportesDAO();
		String opcionPredefinida = objeDAO.obtenerOpcionPorDefecto();
		for (Comboitem item : cmbTipoReporte.getItems()) {
			if (item.getValue().toString().equals(opcionPredefinida)) {
				cmbTipoReporte.setSelectedItem(item);
			}
		}
		onSelect$cmbTipoReporte();
	}

	public void onSelect$cmbTipoReporte() {
		tipoReporteSeleccionado = Integer.parseInt(cmbTipoReporte.getSelectedItem().getValue().toString());
		switch (tipoReporteSeleccionado) {
		case 1:
			// Reporte por entidad
			fila1Grid.setVisible(false);
			fila2Grid.setVisible(true);
			fila3Grid.setVisible(true);
			fila4Grid.setVisible(true);
			fila5Grid.setVisible(false);

			lblAgrupacion.setVisible(true);
			lblEntidad.setVisible(true);
			cmbAgrupacion.setVisible(true);
			cmbEntidad.setVisible(true);
			lblTipoEntidad.setVisible(true);
			cmbTipoEntidad.setVisible(true);
			break;
		case 2:
			// Conciliación resumido
			fila1Grid.setVisible(false);
			fila2Grid.setVisible(true);
			fila3Grid.setVisible(true);

			lblTipo.setVisible(true);
			cmbTipo.setVisible(true);
			lblTipoEntidad.setVisible(false);
			cmbTipoEntidad.setVisible(false);

			fila5Grid.setVisible(true);

			lblAgrupacion.setVisible(true);
			lblEntidad.setVisible(true);
			cmbAgrupacion.setVisible(true);
			cmbEntidad.setVisible(true);
			break;
		case 3:
			// Conciliación detallado
			fila1Grid.setVisible(false);
			fila2Grid.setVisible(true);
			fila3Grid.setVisible(true);

			lblTipo.setVisible(true);
			cmbTipo.setVisible(true);
			lblTipoEntidad.setVisible(false);
			cmbTipoEntidad.setVisible(false);

			fila5Grid.setVisible(true);

			lblAgrupacion.setVisible(true);
			lblEntidad.setVisible(true);
			cmbAgrupacion.setVisible(true);
			cmbEntidad.setVisible(true);
			break;
		case 4:
			// Conciliación cajas
			fila1Grid.setVisible(false);
			fila2Grid.setVisible(true);
			fila3Grid.setVisible(true);
			fila4Grid.setVisible(false);
			fila5Grid.setVisible(false);

			lblAgrupacion.setVisible(true);
			lblEntidad.setVisible(true);
			cmbAgrupacion.setVisible(true);
			cmbEntidad.setVisible(true);
			break;
		case 5:
			// Liquidacion de cajeros
			fila1Grid.setVisible(true);
			fila2Grid.setVisible(true);
			fila3Grid.setVisible(true);
			fila4Grid.setVisible(false);
			fila5Grid.setVisible(false);

			lblAgrupacion.setVisible(false);
			lblEntidad.setVisible(false);
			cmbAgrupacion.setVisible(false);
			cmbEntidad.setVisible(false);

			lblAgrupacion.setVisible(false);
			lblEntidad.setVisible(false);
			cmbAgrupacion.setVisible(false);
			cmbEntidad.setVisible(false);
			break;
		case 6:
			// Reporte de recaudacion
			fila1Grid.setVisible(false);
			fila2Grid.setVisible(true);
			fila3Grid.setVisible(true);
			fila4Grid.setVisible(false);
			fila5Grid.setVisible(false);

			lblAgrupacion.setVisible(false);
			lblEntidad.setVisible(false);
			cmbAgrupacion.setVisible(false);
			cmbEntidad.setVisible(false);

			lblAgrupacion.setVisible(false);
			lblEntidad.setVisible(false);
			cmbAgrupacion.setVisible(false);
			cmbEntidad.setVisible(false);
			break;
		}
		log.debug("onSelect$cmbTipoReporte()  " + " - Tipo de reporte seleccionado = " + cmbTipoReporte.getSelectedItem().getLabel());
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO,
			//	"Tipo de reporte seleccionado = " + cmbTipoReporte.getSelectedItem().getLabel());
		log.debug("onSelect$cmbTipoReporte()  " + " - Identificador reporte seleccionado = " + tipoReporteSeleccionado);
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO,
				//"Identificador reporte seleccionado = " + tipoReporteSeleccionado);
		cmbAgrupacion.setSelectedIndex(-1);
		cmbEntidad.setSelectedIndex(-1);
		limpiarCombobox(cmbAgrupacion);
		limpiarCombobox(cmbEntidad);
		llenaComboAgrupacion();
		llenaComboEstado();
	}

	/**
	 * Bloque de llenado de combobox
	 */

	public void llenaComboAgrupacion() {
		log.debug("llenaComboAgrupacion()  " + " - Llena combo agrupación");
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Llena combo agrupación");
		limpiarCombobox(cmbAgrupacion);
		CBCatalogoBancoDaoB objeDAO = new CBCatalogoBancoDaoB();
		if (tipoReporteSeleccionado == 4) {
			listaAgrupacion = objeDAO.generaConsultaEntidad();
		} else {
			listaAgrupacion = objeDAO.obtieneListaBanco(null, null, null, null);
		}
		for (CBCatalogoBancoModel d : listaAgrupacion) {
			Comboitem item = new Comboitem();
			item.setParent(cmbAgrupacion);
			item.setValue(d.getCbcatalogobancoid());
			item.setLabel(d.getNombre());
		}
	}

	public void onSelect$cmbAgrupacion() {
		log.debug("onSelect$cmbAgrupacion()  " + " - Llena combo entidades");
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Llena combo entidades");
		limpiarCombobox(cmbEntidad);
		String bancoId = cmbAgrupacion.getSelectedItem().getValue().toString();
		log.debug("onSelect$cmbAgrupacion()  " + " - ID Agrupación seleccionada = " + bancoId);
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO,
				//"ID Agrupación seleccionada = " + bancoId);
		CBCatalogoAgenciaDAO objeDAO = new CBCatalogoAgenciaDAO();
		if (tipoReporteSeleccionado == 4) {
			listaEntidad = objeDAO.generaConsultaAgencia();
		} else {
			listaEntidad = objeDAO.obtieneListadoAgencias(bancoId, null, null, null);
		}
		for (CBCatalogoAgenciaModel d : listaEntidad) {
			Comboitem item = new Comboitem();
			item.setParent(cmbEntidad);
			item.setValue(d.getcBCatalogoAgenciaId());
			item.setLabel(d.getNombre());
		}
		cmbEntidad.setSelectedIndex(-1);
	}

	public void llenaComboTipoReporte() {
		log.debug("llenaComboTipoReporte()  " + " - Llena combo tipo reporte");
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Llena combo tipo reporte");
		limpiarCombobox(cmbTipoReporte);
		CBReportesDAO objeDAO = new CBReportesDAO();
		listaTipoReporte = objeDAO.obtenerTipoObjetoX("TIPO_REPORTE");
		for (CBParametrosGeneralesModel d : listaTipoReporte) {
			Comboitem item = new Comboitem();
			item.setParent(cmbTipoReporte);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
		}
	}

	/**
	 * Llenado de combo tipo
	 */
	public void llenaComboTipo() {
		CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
		listaTipo = objeDAO.obtenerParamConvenios();
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
			log.debug("llenaComboTipo()  " + " - Llena combo tipo");
			//Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					//"- Llena combo tipo");
		} else {
			Messagebox.show("Error al cargar la configuracion de tipos de conciliacion", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}
	
	public void llenaComboTipoEntidad() {
		log.debug("llenaComboTipoEntidad()  " + " - Llena combo tipo entidad");
		//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Llena combo tipo entidad");
		limpiarCombobox(cmbTipoEntidad);
		CBReportesDAO objeDAO = new CBReportesDAO();
		listaTipoEntidad = objeDAO.obtenerTipoObjetoX("TIPO_ENTIDAD");
		if (listaTipoEntidad.size() > 0) {
			for (CBParametrosGeneralesModel d : listaTipoEntidad) {
				Comboitem item = new Comboitem();
				item.setParent(cmbTipoEntidad);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			}
		}

	}

	public void llenaComboEstado() {
		cmbEstado.setSelectedIndex(-1);
		limpiarCombobox(cmbEstado);
		if (tipoReporteSeleccionado == 2 || tipoReporteSeleccionado == 3) {
			CBReportesDAO objeDAO = new CBReportesDAO();
			String tipoEstado = "";
			if (tipoReporteSeleccionado == 2) {
				tipoEstado = "ESTADO_CONCILIACION_RESUMIDO";
			} else if (tipoReporteSeleccionado == 3) {
				tipoEstado = "ESTADO_CONCILIACION_DETALLADO";
			}
			listaEstado = objeDAO.obtenerTipoObjetoX(tipoEstado);
			if (listaEstado.size() > 0) {
				for (CBParametrosGeneralesModel d : listaEstado) {
					Comboitem item = new Comboitem();
					item.setParent(cmbEstado);
					item.setValue(d.getValorObjeto1());
					item.setLabel(d.getObjeto());
				}
			}
		}
	}

	/**
	 * 
	 * */

	public void onClick$btnLimpiar() {
		txtUsuario.setText("");
		dtbInicio.setText("");
		cmbAgrupacion.setSelectedIndex(-1);
		dtbFin.setText("");
		cmbEntidad.setSelectedIndex(-1);
		cmbTipo.setSelectedIndex(-1);
		cmbTipoEntidad.setSelectedIndex(-1);
		cmbEstado.setSelectedIndex(-1);
		// obtenerTipoReportePredefinido();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnExcel() {
		if (dtbInicio.getValue() == null || "".equals(dtbInicio.getText().trim())) {
			Messagebox.show("Debe de ingresar una fecha de inicio", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (dtbFin.getValue() == null || "".equals(dtbFin.getText().trim())) {
			Messagebox.show("Debe de ingresar una fecha de fin", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
		} else
			try {
				if (fechaFormato.parse(fechaFormato.format(dtbInicio.getValue()))
						.after(fechaFormato.parse(fechaFormato.format(dtbFin.getValue())))) {
					Messagebox.show("La fecha inicial no puede ser mayor a la final", "ATENCIÓN", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else if (UtilidadesReportes.diferenciasDeFechas(dtbInicio.getValue(), dtbFin.getValue()) > 31) {
					// nueva validacion
					Messagebox.show("Generar registros mayores a 31 dias puede tomar tiempo, desea continuar?",
							"ATENCION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
								public void onEvent(Event event) throws Exception {
									if (((Integer) event.getData()).intValue() != Messagebox.YES) {
										Messagebox.show("Accion Cancelada", "CANCELADO", Messagebox.OK,
												Messagebox.EXCLAMATION);
										return;
									} else {
										llamadaGenerarTipoReporte();
									}
								}
							});
					// finaliza

				} else {
					llamadaGenerarTipoReporte();

				}
			} catch (WrongValueException e) {
				log.error("onClick$btnExcel() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
			} catch (NumberFormatException e) {
				log.error("onClick$btnExcel() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
			} catch (ParseException e) {
				log.error("onClick$btnExcel() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
			}
	}

	public void llamadaGenerarTipoReporte() {
		if (tipoReporteSeleccionado == 1) {
			log.debug("llamadaGenerarTipoReporte()  " + " - \n*** Genera reporte por entidad ***\n");
			
			try {
				generaReporteXEntidad();
			} catch (IOException e) {
				log.error("llamadaGenerarTipoReporte() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if (tipoReporteSeleccionado == 2) {
			log.debug("llamadaGenerarTipoReporte()  " + " - \n*** Genera conciliacion resumido ***\n");
			
			try {
				generaArchivoExcel();
			} catch (IOException e) {
				log.error("llamadaGenerarTipoReporte() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if (tipoReporteSeleccionado == 3) {
			log.debug("llamadaGenerarTipoReporte()  " + " - \n*** Genera reporte conciliacion detallado ***\n");
			
			try {
				generaReporteConciliacionDetalle();
			} catch (IOException e) {
				log.error("llamadaGenerarTipoReporte() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if (tipoReporteSeleccionado == 4) {
			log.debug("llamadaGenerarTipoReporte()  " + " - \n*** Genera reporte conciliacion cajas ***\n");
			
			try {
				generaReporteCajas();
			} catch (IOException e) {
				log.error("llamadaGenerarTipoReporte() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if (tipoReporteSeleccionado == 5) {
			log.debug("llamadaGenerarTipoReporte()  " + " - \n*** Genera reporte liquidaciones ***\n");
			
			try {
				generaReporteLiquidaciones();
			} catch (IOException e) {
				log.error("llamadaGenerarTipoReporte() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if (tipoReporteSeleccionado == 6) {
			log.debug("llamadaGenerarTipoReporte()  " + " - \n*** Genera reporte de recaudacion ***\n");
			
			generaReporteRecaudacion();
		}

	}

	/**
	 * @author Juankrlos by 02/02/2017 Generamos el reporte en formato CSV el cual
	 *         luego es descargado
	 * 
	 *         Modificado ultima vez por CarlosGodinez - 10/04/2017
	 */
	public void generaReporteXEntidad() throws IOException {
		BufferedWriter bw = null;
		try {
			CBReportesDAO objeDAO = new CBReportesDAO();
			String fechaDesde = fechaFormato.format(dtbInicio.getValue());
			String fechaHasta = fechaFormato.format(dtbFin.getValue());
			int idBanco = 0;
			int idAgencia = 0;
			String tipoRecarga = "";
			String tipoEntidad = "";
			if (cmbAgrupacion.getSelectedItem() != null) {
				idBanco = Integer.parseInt(cmbAgrupacion.getSelectedItem().getValue().toString());
			}
			if (cmbEntidad.getSelectedItem() != null) {
				idAgencia = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
			}
			if (cmbTipo.getSelectedItem() != null) {
				tipoRecarga = cmbTipo.getSelectedItem().getValue().toString();
				log.debug("generaReporteXEntidad()  " + " - tipoRecarga tipo" + tipoRecarga);
				
			}
			if (cmbTipoEntidad.getSelectedItem() != null) {
				tipoEntidad = cmbTipoEntidad.getSelectedItem().getValue().toString();
			}
			List<CBEntidad> listCSV = objeDAO.generaConsultaReporte2(fechaDesde, fechaHasta, idBanco, idAgencia,
					tipoRecarga, tipoEntidad);
			if (listCSV.size() > 0) {
				Iterator<CBEntidad> it = listCSV.iterator();
				CBEntidad obj = null;
				Date fecha = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

				// Creamos el encabezado
				String encabezado = "FECHA|MES 1|MES 2|HORA|MONTO|TELEFONO|BANCO|TIPO SERVICIO|"
						+ "SECUENCIA|AGENCIA|FORMA DE PAGO|COMISION|PORCENTAJE|SUCURSAL|NOMBRE SUCURSAL|CANTIDAD\n";
				File archivo = new File("reporte_entidad" + sdf.format(fecha) + ".csv");

				if (listCSV.size() > 0) {
					bw = new BufferedWriter(new FileWriter(archivo));
					bw.write(encabezado);
					while (it.hasNext()) {
						obj = it.next();
						bw.write(obj.getFecha() + "|" + obj.getMes_1() + "|"
								+ UtilidadesReportes.changeNull(obj.getMes_2()) + "|"
								+ UtilidadesReportes.changeNull(obj.getHora()) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(obj.getMonto())) + "|"
								+ obj.getTelefono() + "|" + UtilidadesReportes.changeNull(obj.getBanco()) + "|"
								+ UtilidadesReportes.changeNull(obj.getTipo_servicio()) + "|"
								+ UtilidadesReportes.changeNull(obj.getSecuencia()) + "|"
								+ UtilidadesReportes.changeNull(obj.getAgencia()) + "|"
								+ UtilidadesReportes.changeNull(obj.getForma_de_pago()) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(obj.getComision())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(obj.getPorcentaje())) + "|"
								+ UtilidadesReportes.changeNull(obj.getSucursal()) + "|"
								+ UtilidadesReportes.changeNull(obj.getNombre_sucursal()) + "|" + obj.getCantidad()
								+ "\n");
					}
				}
				log.debug("generaReporteXEntidad()  " + " - Descargamos el archivo");
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Descargamos el archivo");
				Filedownload.save(archivo, null);
				Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
						Messagebox.OK, Messagebox.INFORMATION);
				Clients.clearBusy();
			} else {
				Messagebox.show("No se han encontrado resultados para la consulta, el reporte no se ha podido generar.",
						"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (SQLException e1) {
			log.error("generaReporteXEntidad() -  Error ", e1);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e1);
			Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
		} catch (NamingException e1) {
			log.error("generaReporteXEntidad() -  Error ", e1);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e1);
			Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
		} catch (IOException e) {
			log.error("generaReporteXEntidad() -  Error ", e);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
		} finally {
			if (bw != null)
				bw.close();
		}
	}

	/**
	 * @author Ovidio Santos
	 */
	public void generaArchivoExcel() throws IOException {
		BufferedWriter bw = null;
		try {
			CBReportesDAO objeDAO = new CBReportesDAO();
			String fechaDesde = fechaFormato.format(dtbInicio.getValue());
			String fechaHasta = fechaFormato.format(dtbFin.getValue());

			int idAgencia = 0;
			String tipoRecarga = "";
			String tipoEstado = "";

			if (cmbEntidad.getSelectedItem() != null) {
				idAgencia = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
			}
			
			if (cmbTipo.getSelectedItem() != null) {
				tipoRecarga = cmbTipo.getSelectedItem().getValue().toString();
				log.debug("generaArchivoExcel()  " + " - tipoRecarga tipo combo " + tipoRecarga);
				
			}
			if (cmbEstado.getSelectedItem() != null) {
				tipoEstado = cmbEstado.getSelectedItem().getValue().toString();
			}
			Date fecha = new Date();
			// *
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
			List<CBResumenDiarioConciliacionModel> listCSV = objeDAO.generaConsultaReporte(fechaDesde, fechaHasta, idAgencia, tipoRecarga,
					tipoEstado);
			if (listCSV.size() > 0) {
				Iterator<CBResumenDiarioConciliacionModel> it = listCSV.iterator();
				CBResumenDiarioConciliacionModel c = null;

				String encabezado = "DIA|AGENCIA|TIPO|TRANSACCIONES BANCO|PAGOS BANCO (CANTIDAD A FACTURAR)|CONCILIACION MANUAL BANCO|"
						+ "NO CONCILIADO BANCO|TRANSACCIONES TELEFONICA|PAGOS TELEFONICA|"
						+ "CONCILIACION MANUAL TELCA|NO CONCILIADO TELEFONICA|TRANSACCIONES CONCILIADAS|DIFERENCIA TRANSACCIONES|"
						+ "AUTOMATICA|EFECTO NETO\n";

				File archivo = new File("reporte_conciliacion_resumido" + sdf.format(fecha) + ".csv");

				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

				if (listCSV != null && listCSV.size() > 0) {
					bw = new BufferedWriter(new FileWriter(archivo));
					bw.write(encabezado);
					while (it.hasNext()) {
						c = it.next();
						bw.write(format.format(c.getDia()) + "|" + c.getNombre() + "|" + c.getTipo() + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getTransBanco())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getConfrontaBanco())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getManualBanco())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getPendienteBanco())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getTransTelefonica())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getPagosTelefonica())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getManualTelefonica())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getPendienteTelefonica())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getConciliadas())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getDifTransaccion())) + "|"
								+ UtilidadesReportes.changeNumber(String.valueOf(c.getAutomatica())) + "|"
								+ UtilidadesReportes.changeNumber(
										String.valueOf(c.getPendienteTelefonica().subtract(c.getPendienteBanco())))
								+ "\n");
					}

				}
				log.debug("generaArchivoExcel()  " + " - Descargamos el archivo");
				
				Filedownload.save(archivo, null);
				Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
						Messagebox.OK, Messagebox.INFORMATION);
				Clients.clearBusy();
			} else {
				Messagebox.show("No se han encontrado resultados para la consulta, el reporte no se ha podido generar.",
						"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (SQLException e1) {
			log.error("generaArchivoExcel() -  Error ", e1);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e1);
			Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
		} catch (NamingException e1) {
			log.error("generaArchivoExcel() -  Error ", e1);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e1);
			Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
		} catch (IOException e) {
			log.error("generaArchivoExcel() -  Error ", e);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ADVERTENCIA", Messagebox.OK, Messagebox.ERROR);
		} finally {
			if (bw != null)
				bw.close();
		}
	}

	// modificado por Ovidio Santos
	public String changeNull(String cadena) {
		if (cadena == null) {
			return " ";
		} else {
			return cadena;
		}
	}

	public void generaReporteConciliacionDetalle() throws IOException {

		CBReportesDAO objeDAO = new CBReportesDAO();
		String fechaDesde = fechaFormato.format(dtbInicio.getValue());
		String fechaHasta = fechaFormato.format(dtbFin.getValue());

		int idAgencia = 0;
		int idBanco = 0;
		String tipoRecarga = "";
		String tipoEstado = "";

		if (cmbEntidad.getSelectedItem() != null) {
			idAgencia = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
		}
		if (cmbAgrupacion.getSelectedItem() != null) {
			idBanco = Integer.parseInt(cmbAgrupacion.getSelectedItem().getValue().toString());
		}
		if (cmbTipo.getSelectedItem() != null) {
			tipoRecarga = cmbTipo.getSelectedItem().getValue().toString();
		}
		if (cmbEstado.getSelectedItem() != null) {
			tipoEstado = cmbEstado.getSelectedItem().getValue().toString();
		}

		List<CBConciliacionDetallada> listCSV = objeDAO.generaConsultaReporte3(fechaDesde, fechaHasta, idAgencia,
				idBanco, tipoRecarga, tipoEstado);
		if (listCSV.size() > 0) {
			Iterator<CBConciliacionDetallada> it = listCSV.iterator();
			CBConciliacionDetallada obj = null;
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

			// Creamos el encabezado
			String encabezado = "AGENCIA|DIA|TIPO|CLIENTE|NOMBRE|DESCRIPCION PAGO|TRANS TELCA|TELEFONO|TRANS BANCO|"
					+ "PAGO TELEFONICA|PAGO BANCO (CANTIDAD A FACTURAR)|MANUAL|PENDIENTE|"
					+ "COMISION|MONTO COMISION|CODIGO|NOMBRE SUCUSAL|TIPO\n";
			File archivo = new File("reporte_conciliaciones_detalle_" + sdf.format(fecha) + ".csv");
			BufferedWriter bw = null;
			try {

				if (listCSV != null && listCSV.size() > 0) {
					bw = new BufferedWriter(new FileWriter(archivo));
					bw.write(encabezado);
					while (it.hasNext()) {
						obj = it.next();
						bw.write(changeNull(obj.getAgencia()) + "|" + obj.getDia() + "|" + changeNull(obj.getTipo())
								+ "|" + changeNull(obj.getCliente()) + "|" + changeNull(obj.getNombre()) + "|"
								+ changeNull(obj.getDesPago()) + "|" + changeNull(obj.getTransTelca()) + "|"
								+ changeNull(obj.getTelefono()) + "|" + changeNull(obj.getTransBanco()) + "|"
								+ obj.getImpPago() + "|" + obj.getMonto() + "|" + obj.getManual() + "|"
								+ changeNull(obj.getPendiente()) + "|" + obj.getComision() + "|"
								+ obj.getMonto_comision() + "|" + changeNull(obj.getSucursal()) + "|"
								+ changeNull(obj.getNombre_sucursal()) + "|" + changeNull(obj.getTipo_sucursal())
								+ "\n");
					}
				}
				log.debug("generaReporteConciliacionDetalle()  " + " - Descargamos el archivo");
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Descargamos el archivo");
				Filedownload.save(archivo, null);
				Messagebox.show(
						"Reporte " + archivo.getName() + " generado de manera exitosa, el archivo ha sido descargado",
						"ATENCION", Messagebox.OK, Messagebox.INFORMATION);

				Clients.clearBusy();
			} catch (Exception e) {
				log.error("generaReporteConciliacionDetalle() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if (bw != null)
					bw.close();
			}
		} else {
			Messagebox.show("No se han encontrado resultados para la consulta, el reporte no se ha podido generar.",
					"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
		}

	}

	
	public void generaReporteCajas() throws IOException {
		BufferedWriter bw = null;
		CBReportesDAO objeDAO = new CBReportesDAO();
		String fechaDesde = fechaFormato.format(dtbInicio.getValue());
		String fechaHasta = fechaFormato.format(dtbFin.getValue());

		int idAgencia = 0;
		int idBanco = 0;

		if (cmbEntidad.getSelectedItem() != null) {
			idAgencia = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
		}
		if (cmbAgrupacion.getSelectedItem() != null) {
			idBanco = Integer.parseInt(cmbAgrupacion.getSelectedItem().getValue().toString());
		}

		List<CBConciliacionCajasModel> list = objeDAO.generaConsultaPrincipal(fechaDesde, fechaHasta, idAgencia,
				idBanco);
		if (list.size() > 0) {
			Iterator<CBConciliacionCajasModel> it = list.iterator();
			CBConciliacionCajasModel obj = null;
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

			// Creamos el encabezado
			String encabezado = "Agrupacion|Entidad|Fecha|"
					+ "Caja efectivo|Caja cheque|Caja extension|Caja cuotas credomatic|Caja cuotas visa|Caja tarjeta credomatic|"
					+ "Caja tarjeta otras|Caja tarjeta visa|"
					+ "SC Pagosod|SC Pagosom|SC Reversaod|SC Reversaom|Total dia|Total Gnral|"
					+ "Deposito|Consumo credomatic|Retencion credomatic|Liquido credomatic|Consumo Visa|Retencion Visa|Liquido Visa|"
					+ "Total EC|Diferencia\n";

			File archivo = new File("reporte_conciliacion_cajas_" + sdf.format(fecha) + ".csv");

			try {

				if (list != null && list.size() > 0) {
					bw = new BufferedWriter(new FileWriter(archivo));
					bw.write(encabezado);
					while (it.hasNext()) {
						obj = it.next();

						bw.write(obj.getEntidad() + "|" + obj.getAgencia() + "|" + obj.getFecha() + "|"
								+ obj.getCajaefectivo() + "|" + obj.getCajacheque() + "|" + obj.getCajaexenciones()
								+ "|" + obj.getCajacuotascredomatic() + "|" + obj.getCajacuotasvisa() + "|"
								+ obj.getCajatarjetacredomatic() + "|" + obj.getCajatarjetaotras() + "|"
								+ obj.getCajatarjetavisa() + "|" + obj.getScpagosod() + "|" + obj.getScpagosom() + "|"
								+ obj.getScreversasod() + "|" + obj.getScreversasom() + "|" + obj.getTotaldia() + "|"
								+ obj.getCajatotal() + "|" + obj.getDeposito() + "|" + obj.getCredomaticdep() + "|"
								+ obj.getCredomaticRet() + "|" + obj.getEstadoCredo() + "|" + obj.getConsumovisa() + "|"
								+ obj.getIvavisa() + "|" + obj.getEstadoVisa() + "|" + obj.getTotalec() + "|"
								+ obj.getDiferencia() + "\n");
					}

				}
				log.debug("generaReporteCajas()  " + " - Descargamos el archivo");
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Descargamos el archivo");
				Filedownload.save(archivo, null);
				Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
						Messagebox.OK, Messagebox.INFORMATION);

				Clients.clearBusy();

			} catch (Exception e) {
				log.error("generaReporteCajas() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
				
			} finally {
				if (bw != null)
					bw.close();
			}
		} else {
			Messagebox.show("No se han encontrado resultados para la consulta, el reporte no se ha podido generar.",
					"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	public void generaReporteLiquidaciones() throws IOException {
		CBReportesDAO objeDAO = new CBReportesDAO();
		String fechaDesde = fechaFormato.format(dtbInicio.getValue());
		String fechaHasta = fechaFormato.format(dtbFin.getValue());
		CBLiquidacionDetalleModel obj = null;
		String usuarioliquidacion = null;
		if (txtUsuario.getText().trim() != null && !"".equals(txtUsuario.getText().trim())) {
			usuarioliquidacion = txtUsuario.getText().trim();
		}
		List<CBLiquidacionDetalleModel> list = objeDAO.consultaReporte(usuarioliquidacion, fechaDesde, fechaHasta);
		if (list.size() > 0) {
			Iterator<CBLiquidacionDetalleModel> it = list.iterator();

			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

			// Creamos el encabezado
			String encabezado = "Usuario|FechaTransaccion|TipoValor|TipoPago|TipoCodigoTarjeta|Descripcion|Total\n";

			File archivo = new File("reporte_liquidacion_cajeros_" + sdf.format(fecha) + ".csv");
			BufferedWriter bw = null;
			try {
				if (!archivo.exists()) {
					if(archivo.createNewFile())
						log.debug("generaReporteLiquidaciones()  " + " - Archivo temporal creado");
						//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Archivo temporal creado");
				}

				if (list.size() > 0) {
					bw = new BufferedWriter(new FileWriter(archivo));
					bw.write(encabezado);
					while (it.hasNext()) {
						obj = it.next();
						bw.write(changeNull(obj.getNombtransaccion()) + "|" + changeNull(obj.getFec_efectividad()) + "|"
								+ obj.getTipo_valo() + "|" + changeNull(obj.getTipo_pago()) + "|"
								+ changeNull(obj.getCod_tipotarjeta()) + "|" + changeNull(obj.getDesc()) + "|"
								+ changeNull(obj.getTotal()) + "\n");
					}
				}
				log.debug("generaReporteLiquidaciones()  " + " - Descargamos el archivo");
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Descargamos el archivo");
				Filedownload.save(archivo, null);
				Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
						Messagebox.OK, Messagebox.INFORMATION);

				Clients.clearBusy();
			} catch (Exception e) {
				log.error("generaReporteLiquidaciones() -  Error ", e);
				//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if (bw != null)
					bw.close();
			}
		} else {
			Messagebox.show("No se han encontrado resultados para la consulta, el reporte no se ha podido generar.",
					"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	public void generaReporteRecaudacion() {
		try {
			CBReportesDAO objeDAO = new CBReportesDAO();
			String fechaDesde = fechaFormato.format(dtbInicio.getValue());
			String fechaHasta = fechaFormato.format(dtbFin.getValue());
			CBPagosModel obj = null;

			List<CBPagosModel> list = objeDAO.consultaReporteRecaudacion(fechaDesde, fechaHasta);
			
			if (list.size() > 0) {
				Iterator<CBPagosModel> it = list.iterator();

				Date fecha = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

				// Creamos el encabezado
				String encabezado = "FECHA_EFECTIVA|FECHA_PAGO|COD_CLIENTE|NOM_CLIENTE|NUM_SECUENCI|"
						+ "IMP_PAGO|TRANSACCION|TIPO_TRANSACCION|COD_OFICINA|DES_OFICINA|TIP_MOVCAJA|"
						+ "DES_MOVCAJA|TIP_VALOR|DES_TIPVALOR|NOM_USUARORA|COD_BANCO|"
						+ "BANCO|COD_CAJA|CAJA|OBSERVACION \n";

				File archivo = new File("reporte_recaudacion_" + sdf.format(fecha) + ".csv");
				BufferedWriter bw = null;
				try {
					if (!archivo.exists()) {
						if (archivo.createNewFile())
							log.debug("generaReporteRecaudacion()  " + " - Archivo temporal creado");
							//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO,
									//"Archivo temporal creado");
					}

					if (list.size() > 0) {
						bw = new BufferedWriter(new FileWriter(archivo));
						bw.write(encabezado);
						while (it.hasNext()) {
							obj = it.next();
							bw.write(obj.getFecEfectividad() + "|" + obj.getFecha() + "|"
									+ changeNull(obj.getCodCliente()) + "|" + changeNull(obj.getNonCliente()) + "|"
									+ changeNull(obj.getNumSecuenci()) + "|" + changeNull(obj.getImpPago()) + "|"
									+ changeNull(obj.getTransaccion()) + "|" + changeNull(obj.getTipoTransaccion())
									+ "|" + changeNull(obj.getCodOficina()) + "|" + changeNull(obj.getDesOficina())
									+ "|" + changeNull(obj.getTipoMovCaja()) + "|" + changeNull(obj.getDesMovCaja())
									+ "|" + changeNull(obj.getTipoValor()) + "|" + changeNull(obj.getDesTipoValor())
									+ "|" + changeNull(obj.getNomUsuarora()) + "|" + changeNull(obj.getCodBanco()) + "|"
									+ changeNull(obj.getDesBanco()) + "|" + changeNull(obj.getCodCaja()) + "|"
									+ changeNull(obj.getDesCaja()) + "|" + changeNull(obj.getObservacion()) + "\n");

						}
					}
					log.debug("generaReporteRecaudacion()  " + " - Descargamos el archivo");
					//Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Descargamos el archivo");
					Filedownload.save(archivo, null);
					Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
							Messagebox.OK, Messagebox.INFORMATION);

					Clients.clearBusy();
				} catch (Exception e) {
					log.error("generaReporteRecaudacion() -  Error ", e);
					//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
				} finally {
					if (bw != null)
						bw.close();
				}
			} else {
				Messagebox.show("No se han encontrado resultados para la consulta, el reporte no se ha podido generar.",
						"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (IOException ioe) {
			log.error("generaReporteRecaudacion() -  Error ", ioe);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, ioe);
		} catch (Exception e) {
			log.error("generaReporteRecaudacion() -  Error ", e);
			//Logger.getLogger(CBReportesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void limpiarCombobox(Combobox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
		}
	}

}
