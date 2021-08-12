/**
 * 
 */
package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;
import com.terium.siccam.dao.CBArchivosInsertadosEstadoCuentaDAO;
import com.terium.siccam.dao.CBBitacoraLogDAO;
import com.terium.siccam.dao.CBCatalogoBancoDaoB;
import com.terium.siccam.model.CBArchivosInsertadosEstadoCuentaModel;
import com.terium.siccam.model.CBArchivosInsertadosModel;
import com.terium.siccam.model.CBBitacoraLogModel;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBConsultaContabilizacionModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.Constantes;

/**
 * @author lab
 * 
 */
public class CBConsultaCargasController extends ControladorBase {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	Combobox cmbTipoCarga;

	Combobox combxBanco;
	Combobox combxAgencia;
	Datebox dbxfechaDesde;
	Datebox dbxfechaHasta;
	Datebox dbxfechaDesdeCargaEstadoCuenta;
	Datebox dbxfechaHastaCargaEstadoCuenta;

	String usuario;
	Listbox listadoDetalleCargadosEstadoCuenta;
	Listbox listadoDetalleCargados;
	Listbox listadoDetalleNoCargados;
	Window detalleCargdos;
	private String fechaDesde;
	private String fechaHasta;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	ListModelList<CBArchivosInsertadosModel> listaCargas;
	Boolean fila1;
	private Groupbox gpbDelim;
	private Groupbox gpbDelim1;
	private Groupbox gpbDelimcuenta;
	private Groupbox gpbDelimcuenta1;
	private int tipoCargaSeleccionado;
	
	Button btnExcel;
	Button btnExcel2;

	@Init
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		gpbDelim.setVisible(false);
		gpbDelim1.setVisible(false);
		gpbDelimcuenta.setVisible(false);
		gpbDelimcuenta1.setVisible(false);
		llenaComboTipoCarga();
		// llenacomboTipo();
		obtenerTipoCargaPredefinido();
		usuario = obtenerUsuario().getUsuario();
		obtieneListaBanco();
	}


	public void limpiarCombobox(Combobox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
		}
	}

	// pruebas
	public void obtenerTipoCargaPredefinido() {
		CBArchivosInsertadosDAO objeDAO = new CBArchivosInsertadosDAO();
		String opcionPredefinida = objeDAO.obtenerOpcionPorDefecto();
		for (Comboitem item : cmbTipoCarga.getItems()) {
			if (item.getValue().toString().equals(opcionPredefinida)) {
				cmbTipoCarga.setSelectedItem(item);
			}
		}
		onSelect$cmbTipoCarga();
	}

	public void onSelect$cmbTipoCarga() {

		this.tipoCargaSeleccionado = Integer.parseInt(this.cmbTipoCarga.getSelectedItem().getValue().toString());
		switch (this.tipoCargaSeleccionado) {
		case 1:
			//ESTADO DE CUENTA
			gpbDelimcuenta.setVisible(true);
			gpbDelimcuenta1.setVisible(true);
			System.out.println("caso 1 ");
			gpbDelim.setVisible(false);
			gpbDelim1.setVisible(false);
			break;
		case 2:
			//CONFRONTAS
			gpbDelim.setVisible(true);
			gpbDelim1.setVisible(true);
			System.out.println("caso 2 ");
			gpbDelimcuenta.setVisible(false);
			gpbDelimcuenta1.setVisible(false);
			break;
		
		}
		Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO,
				"Tipo de carga seleccionado = " + this.cmbTipoCarga.getSelectedItem().getLabel());

	}

	private List<CBParametrosGeneralesModel> listaTipoCarga = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboTipoCarga() {
		Logger.getLogger(CBReportesController.class.getName()).log(Level.INFO, "Llena combo tipo carga");
		this.cmbTipoCarga.setSelectedIndex(-1);
		// limpiarCombobox(cmbTipoReporte);
		CBArchivosInsertadosDAO objeDAO = new CBArchivosInsertadosDAO();
		this.listaTipoCarga = objeDAO.obtenerTipoObjetoX("TIPO_CARGA");
		for (CBParametrosGeneralesModel d : this.listaTipoCarga) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipoCarga);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
		}
	}

	// boton consulta
	public void onClick$btnConsulta() {
		// se le da formato a la fecha
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String idBanco = null;
		String idAgencia = null;

		if (combxBanco.getSelectedItem() != null) {
			idBanco = combxBanco.getSelectedItem().getValue();
		}
		if (combxAgencia.getSelectedItem() != null) {
			idAgencia = combxAgencia.getSelectedItem().getValue();
		}
		if (dbxfechaDesde.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha DESDE antes de consultar datos.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dbxfechaHasta.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha HASTA antes de consultar datos.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dbxfechaDesde.getValue().after(dbxfechaHasta.getValue())) {
			Messagebox.show("La fecha DESDE debe ser menor a la fecha HASTA.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;

		} else {
			fechaDesde = (formato.format((dbxfechaDesde.getValue())));
			fechaHasta = (formato.format((dbxfechaHasta.getValue())));
			lbxAbrirDetalleCargados(idBanco, idAgencia, fechaDesde, fechaHasta);
		}

	}

	// bancos

	// se obtiene el listado del banco
	public void obtieneListaBanco() {
		limpiaCombobox(combxBanco);
		CBCatalogoBancoDaoB cbb = new CBCatalogoBancoDaoB();
		List<CBCatalogoBancoModel> list = cbb.obtieneListaBanco(null, null, null, null);
		Iterator<CBCatalogoBancoModel> it = list.iterator();

		CBCatalogoBancoModel objcb;

		while (it.hasNext()) {
			objcb = it.next();

			Comboitem item = new Comboitem();
			item.setValue(objcb.getCbcatalogobancoid());
			item.setLabel(objcb.getNombre());
			item.setParent((Component) combxBanco);
		}

	}

	// obtiene el listado agencia de cbcatalogoagenciamodel
	public void obtieneListaAgencia(int idBanco) {
		this.combxAgencia.setSelectedIndex(-1);
	   limpiaCombobox(combxAgencia);
		CBCatalogoAgenciaDAO cbcbd = new CBCatalogoAgenciaDAO();
		List<CBCatalogoAgenciaModel> lista = cbcbd.obtieneListadoAgencias(String.valueOf(idBanco), null, null, null);
		Iterator<CBCatalogoAgenciaModel> ite = lista.iterator();

		CBCatalogoAgenciaModel objca;
		while (ite.hasNext()) {
			objca = ite.next();
			// combobox agencia
			Comboitem item = new Comboitem();
			item.setValue(objca.getcBCatalogoAgenciaId());
			item.setLabel(objca.getNombre());
			item.setParent((Component) combxAgencia);
		}
	}
	// seleccion cmbxBanco

	public void onSelect$combxBanco() {
		System.out.println("activa onselect");
		
		
		obtieneListaAgencia(Integer.parseInt(combxBanco.getSelectedItem().getValue().toString()));

	}

	// limpia campos

	public void limpiaCampos(Listbox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}
	// OV
	private List<CBArchivosInsertadosModel> listconfrontas = new ArrayList<CBArchivosInsertadosModel>();
	// llenado de lbx
	public void lbxAbrirDetalleCargados(String idBanco, String idAgencia, String fechaDesde, String fechaHasta) {
		limpiaCampos(listadoDetalleCargados);
		CBArchivosInsertadosDAO cbaidao = new CBArchivosInsertadosDAO();
		 listconfrontas = cbaidao.obtieneListaArchivosCargados(idBanco, idAgencia, fechaDesde,
				fechaHasta);
		if (listconfrontas != null && listconfrontas.size() > 0) {
			Iterator<CBArchivosInsertadosModel> it = listconfrontas.iterator();
			CBArchivosInsertadosModel cbainsertados = null;
			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				cbainsertados = it.next();
				item = new Listitem();
				/*
				 * cell = new Listcell();
				 * cell.setLabel(cbainsertados.getIdArchivosInsertados()); cell.setParent(item);
				 */
				// creando la celda y le asigno a la variable cell
				cell = new Listcell();
				cell.setLabel(cbainsertados.getNombreArchivo());
				// le indico quien es el padre de cell en este caso es item
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getFecha());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getBanco());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getAgencia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getCreadoPor());
				cell.setParent(item);

				cell = new Listcell();
				Button btnabrirDetalleCargados = new Button();
				btnabrirDetalleCargados.setImage("/img/globales/16x16/consulta.png");
				cell.setParent(item);
				btnabrirDetalleCargados.setParent(cell);
				btnabrirDetalleCargados.setAttribute("archivosInserta", cbainsertados.getIdArchivosInsertados());
				btnabrirDetalleCargados.addEventListener(Events.ON_CLICK, eventBtnabrirDetalleCargados);

				cell = new Listcell();
				Button btnabrirDetalleNoCargados = new Button();
				btnabrirDetalleNoCargados.setImage("/img/globales/16x16/consulta.png");
				cell.setParent(item);
				btnabrirDetalleNoCargados.setParent(cell);
				btnabrirDetalleNoCargados.setAttribute("abrirDetalleNoCargados",
						cbainsertados.getIdArchivosInsertados());
				btnabrirDetalleNoCargados.addEventListener(Events.ON_CLICK, eventBtnabrirDetalleNoCargados);

				cell = new Listcell();
				Button btnEliminarCargas = new Button();
				btnEliminarCargas.setImage("/img/globales/16x16/delete.png");
				cell.setParent(item);
				btnEliminarCargas.setParent(cell);
				btnEliminarCargas.setAttribute("eliminarCargasArchivos", cbainsertados);
				btnEliminarCargas.addEventListener(Events.ON_CLICK, eventBtnEliminarCargas);

				item.setValue(cbaidao);
				item.setAttribute("objSeleccionado", cbaidao);
				item.setParent(listadoDetalleCargados);
			}
		}else {
				
				Messagebox.show("No se encontraron registros, para los filtros ingresados.", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
			
		}

	}

	EventListener<Event> eventBtnabrirDetalleNoCargados = new EventListener<Event>() {
		public void onEvent(Event arg0) throws Exception {
			try {
				String archivosInserta = (String) arg0.getTarget().getAttribute("abrirDetalleNoCargados");
				System.out.println("abrirDetalleNoCargados: " + eventBtnabrirDetalleNoCargados);

				misession.setAttribute("sesionabrirDetalleNoCargados", archivosInserta);
				Window winDetalleCargados = (Window) Executions.createComponents("/cbDetalleNoCargados.zul", null,
						null);
				winDetalleCargados.doModal();
			} catch (Exception e) {
				Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.SEVERE, null, e);
			}

		}
	};

	EventListener<Event> eventBtnabrirDetalleCargados = new EventListener<Event>() {
		public void onEvent(Event arg0) {
			try {
				String archivosInserta = (String) arg0.getTarget().getAttribute("archivosInserta");
				System.out.println("abrirDetalleCargados: " + eventBtnabrirDetalleCargados);

				misession.setAttribute("sesionabrirDetalleCargados", archivosInserta);
				detalleCargdos = (Window) Executions.createComponents("/cbDetalleCargados.zul", null, null);
				detalleCargdos.doModal();
			} catch (Exception e) {
				Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.SEVERE, null, e);

			}
		}
	};

	EventListener<Event> eventBtnEliminarCargas = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked"})
		public void onEvent(Event arg0) throws Exception {
			final CBArchivosInsertadosModel objModel = (CBArchivosInsertadosModel) arg0.getTarget().getAttribute("eliminarCargasArchivos");
			final String eliminarCargasArchivos = objModel.getIdArchivosInsertados();
			System.out.println("abrirDetalleCargados: " + eventBtnabrirDetalleCargados);
			Messagebox.show("¿Desea eliminar los registros de carga del archivo seleccionado?", Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBArchivosInsertadosDAO objDAO = new CBArchivosInsertadosDAO();
								CBBitacoraLogDAO bitacoraDAO = new CBBitacoraLogDAO();
										
								objDAO.borraFilaGrabadaMaestro(eliminarCargasArchivos);
								Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO,
										"elimina archivo de tabla cb_archivos_insertados ");
								if (objDAO.borraConciliacionMaestro(eliminarCargasArchivos)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO,
											"elimina carga de info conciliacion ");
								}

								if (objDAO.borraDataBancoMaestro(eliminarCargasArchivos)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO, 
											"elimina carga de info de data banco ");
								}

								if (objDAO.borraDatasinProcesarMaestro(eliminarCargasArchivos)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO, 
											"elimina carga de info data sin procesar");
								}
								
								/**
								 * Added by CarlosGodinez -> 19/09/2018
								 * Inserta en tabla de log de carga de archivos para auditar
								 * quien elimino la informacion de la carga
								 * */
								CBBitacoraLogModel objBitaModel = new CBBitacoraLogModel();
								objBitaModel.setModulo("CONSULTA CARGA ARCHIVOS");
								objBitaModel.setTipoCarga(cmbTipoCarga.getText());
								objBitaModel.setNombreArchivo(objModel.getNombreArchivo());
								objBitaModel.setAccion("Se elimino carga de archivo con ID = " + objModel.getIdArchivosInsertados());
								objBitaModel.setUsuario(usuario);
								
								if (bitacoraDAO.insertBitacoraLog(objBitaModel)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO, 
											"Inserta accion en log para carga de archivos");	
								}
								/**
								 * FIN CarlosGodinez -> 19/09/2018
								 * */
								Messagebox.show("Registros eliminado con exito.", Constantes.ATENCION, Messagebox.OK,
										Messagebox.INFORMATION);
								onClick$btnConsulta();

							}
						}
					});
		}
	};

	@Command
	// @NotifyChange("listaCargas")
	public void eliminarDataBanco(@BindingParam("idFila") String idFila) {
		try {
			Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO, null, "id: " + idFila);
			CBArchivosInsertadosDAO cbaidao = new CBArchivosInsertadosDAO();
			cbaidao.borraDataBancoMaestro(idFila);

		} catch (Exception e) {
			Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	///

	// nuevos eliminar conciliacion
	// No cargados
	@Command
	// @NotifyChange("listaCargas")
	public void eliminarConciliacion(@BindingParam("idFila") String idFila) {
		try {
			Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO, null, "id: " + idFila);
			CBArchivosInsertadosDAO cbaidao = new CBArchivosInsertadosDAO();
			cbaidao.borraConciliacionMaestro(idFila);

		} catch (Exception e) {
			Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	///
	public String getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public String getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	/*
	 * metodos para Estado Cuentas
	 */

	public void onClick$btnConsultaCargaEstadoCuenta() {

		// se le da formato a la fecha
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		if (dbxfechaDesdeCargaEstadoCuenta.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha DESDE antes de consultar datos.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dbxfechaHastaCargaEstadoCuenta.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha HASTA antes de consultar datos.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dbxfechaDesdeCargaEstadoCuenta.getValue().after(dbxfechaHastaCargaEstadoCuenta.getValue())) {
			Messagebox.show("La fecha DESDE debe ser menor a la fecha HASTA.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;

		} else {
			fechaDesde = (formato.format((dbxfechaDesdeCargaEstadoCuenta.getValue())));
			fechaHasta = (formato.format((dbxfechaHastaCargaEstadoCuenta.getValue())));
			llenalistboxEstadoCuenta(fechaDesde, fechaHasta);
		}

	}

	private List<CBArchivosInsertadosEstadoCuentaModel> listestadocuenta = new ArrayList<CBArchivosInsertadosEstadoCuentaModel>();
	// llenado de lbx
	public void llenalistboxEstadoCuenta(String fechaDesde, String fechaHasta) {

		limpiaCampos(listadoDetalleCargadosEstadoCuenta);
		CBArchivosInsertadosEstadoCuentaDAO cbaidao = new CBArchivosInsertadosEstadoCuentaDAO();
	 listestadocuenta = cbaidao.obtieneListaArchivosCargados(fechaDesde, fechaHasta);
		if (listestadocuenta != null && listestadocuenta.size() > 0) {
			Iterator<CBArchivosInsertadosEstadoCuentaModel> it = listestadocuenta.iterator();
			CBArchivosInsertadosEstadoCuentaModel cbainsertados = null;
			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				cbainsertados = it.next();
				item = new Listitem();
				// creando la celda y le asigno a la variable cell
				cell = new Listcell();
				cell.setLabel(cbainsertados.getNombreArchivo());
				// le indico quien es el padre de cell en este caso es item
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(cbainsertados.getTipoCarga());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getCreadoPor());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getFecha());
				cell.setParent(item);

				cell = new Listcell();
				Button btnEliminarCargas = new Button();				
				btnEliminarCargas.setImage("/img/globales/16x16/delete.png");
				cell.setParent(item);
				btnEliminarCargas.setParent(cell);
				btnEliminarCargas.setAttribute("eliminarCargasArchivos", cbainsertados);
				btnEliminarCargas.addEventListener(Events.ON_CLICK, eventBtnEliminarCargasEstadoCuenta);

				item.setValue(cbaidao);
				item.setAttribute("objSeleccionado", cbaidao);
				item.setParent(listadoDetalleCargadosEstadoCuenta);
			}
		}else {
				
				Messagebox.show("No se encontraron registros, para los filtros ingresados.", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
			
		}

	}

	EventListener<Event> eventBtnEliminarCargasEstadoCuenta = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event arg0) throws Exception {
			final CBArchivosInsertadosEstadoCuentaModel objModel = (CBArchivosInsertadosEstadoCuentaModel) arg0.getTarget().getAttribute("eliminarCargasArchivos");
			final String eliminarCargasArchivos = objModel.getIdArchivosInsertados();
			Messagebox.show("¿Desea eliminar los registros de carga del archivo seleccionado?", Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBArchivosInsertadosEstadoCuentaDAO objDAO = new CBArchivosInsertadosEstadoCuentaDAO();
								CBBitacoraLogDAO bitacoraDAO = new CBBitacoraLogDAO();
								
								if (objDAO.borraFilaGrabadaMaestro(eliminarCargasArchivos)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO,
											"elimina archivo maestro..");
								}
								if (objDAO.borraRegistrosSociedad(eliminarCargasArchivos)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO,
											"elimina registros de tabla cb_estado_cuenta_sociedad..");
								}
								if (objDAO.borraRegistrosCredomatic(eliminarCargasArchivos)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO,
											"elimina registros de credomatic..");
								}
								if (objDAO.borraRegistrosOtras(eliminarCargasArchivos)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO,
											"elimina registros otros de extracto..");
								}
								/**
								 * Added by CarlosGodinez -> 19/09/2018
								 * Inserta en tabla de log de carga de archivos para auditar
								 * quien elimino la informacion de la carga
								 * */
								CBBitacoraLogModel objBitaModel = new CBBitacoraLogModel();
								objBitaModel.setModulo("CONSULTA CARGA ARCHIVOS");
								objBitaModel.setTipoCarga(objModel.getTipoCarga());
								objBitaModel.setNombreArchivo(objModel.getNombreArchivo());
								objBitaModel.setAccion("Se elimino carga de archivo con ID = " + objModel.getIdArchivosInsertados());
								objBitaModel.setUsuario(usuario);
								
								if (bitacoraDAO.insertBitacoraLog(objBitaModel)) {
									Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO, 
											"Inserta accion en log para carga de archivos");	
								}
								/**
								 * FIN CarlosGodinez -> 19/09/2018
								 * */
								
								Messagebox.show("Registros eliminado con exito.", Constantes.ATENCION, Messagebox.OK,
										Messagebox.INFORMATION);
								onClick$btnConsultaCargaEstadoCuenta();

							}
						}
					});
		}
	};

		
		public void onClick$btnExcel() throws IOException {
		BufferedWriter bw = null; 
		System.out.println("Generando reporte ...");

		try {
			if(listconfrontas.size()>0) {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

			// Creamos el encabezado
			String encabezado = "Nombre Archivo|Fecha|Banco|Agencia|Creado por \n";
			
			File archivo = new File("reporte_confrontas_cargadas_" + sdf.format(fecha) + ".csv");
			System.out.println("Generando reporte lista ..." + listconfrontas.size());
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(encabezado);

			CBArchivosInsertadosModel objModelReporte = new CBArchivosInsertadosModel();
			Iterator<CBArchivosInsertadosModel> it = listconfrontas.iterator();
			System.out.println("Generando reporte lista2 ..." + listconfrontas.size());
			while (it.hasNext()) {
				objModelReporte = it.next();
				bw.write((objModelReporte.getNombreArchivo()).trim() + "|"
						+ (objModelReporte.getFecha()) + "|" + (objModelReporte.getBanco())
						+ "|" + (objModelReporte.getAgencia()).trim() + "|"
						+ (objModelReporte.getCreadoPor()) + "\n");
			}
			bw.close();
			System.out.println("Descarga exitosa del archivo generado...");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCIÓN",
					Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();
			}else {
				Messagebox.show("Debe consultar antes de generar el reporte", "ATENCIÓN",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (IOException e) {
			Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(bw != null)
				bw.close();
		}
	}

		
		public void onClick$btnExcel2() throws IOException {
		BufferedWriter bw = null; 
		System.out.println("Generando reporte ...");

		try {
			if (listestadocuenta.size()>0) {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

			// Creamos el encabezado
			String encabezado = "Nombre Archivo|Tipo de Carga|Cargado por|Fecha Carga \n";
			
			File archivo = new File("reporte_estados_cuentas_cargadas_" + sdf.format(fecha) + ".csv");
			System.out.println("Generando reporte lista ..." + listestadocuenta.size());
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(encabezado);

			CBArchivosInsertadosEstadoCuentaModel objModelReporte = new CBArchivosInsertadosEstadoCuentaModel();
			Iterator<CBArchivosInsertadosEstadoCuentaModel> it = listestadocuenta.iterator();
			System.out.println("Generando reporte lista2 ..." + listestadocuenta.size());
			while (it.hasNext()) {
				objModelReporte = it.next();
				bw.write((objModelReporte.getNombreArchivo()).trim() + "|"
						+ (objModelReporte.getTipoCarga()) + "|" + (objModelReporte.getCreadoPor())+"|"
						+ (objModelReporte.getFecha()) + "\n");
			}
			bw.close();
			System.out.println("Descarga exitosa del archivo generado...");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCIÓN",
					Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();
			}else {
				Messagebox.show("Debe consultar antes de generar el reporte", "ATENCIÓN",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (IOException e) {
			Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(bw != null)
				bw.close();
		}
	}

		
}
