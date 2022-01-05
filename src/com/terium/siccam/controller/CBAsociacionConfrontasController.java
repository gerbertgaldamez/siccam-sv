package com.terium.siccam.controller;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBAgenciaComercialDAO;
import com.terium.siccam.dao.CBBancoAgenciaConfrontaDAO;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.dao.CBConciliacionCajasDAO;
import com.terium.siccam.dao.CBConfiguracionConexionDao;
import com.terium.siccam.dao.CBConfiguracionConfrontaDaoB;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBConfiguracionConModel;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.Constantes;

/**
 * @author Carlos Godinez - 16/10/2017
 * */
public class CBAsociacionConfrontasController extends ControladorBase  {
	
	private static Logger log = Logger.getLogger(CBAsociacionConfrontasController.class);
	private static final long serialVersionUID = 9176164927878418930L;
	
	Combobox cmbConfConfronta;
	Combobox cmbConfrontaPadre;
	Combobox cmbconfrontasocia;
	Combobox cmbTipo;
	Combobox cmbEstadoComision;
	Combobox cmbEstado;
	Combobox cmbConexion;
	
	Textbox txtNombreBusqueda;
	Textbox txtComision;
	Textbox txtAproximacion;
	Textbox txtCantidadAjustes;
	Textbox txtDescartarTrans;
	Textbox txtPathArchivo;
	
	
	Listbox lbxConsulta;
	
	Button btnGuardar;
	Button btnModificar;
	
	private String usuario;
	private String idAgrupacion;
	private String idEntidad;
	private int idbancoagenciaconfronta;
	private String idSeleccionado;
	//private Row fila1Grid;
	//private Row fila2Grid;
	//Textbox txtAfiliacion; 
	
	private List<CBConfiguracionConfrontaModel> listaConfConfrontas = new ArrayList<CBConfiguracionConfrontaModel>();
	private List<CBConfiguracionConfrontaModel> listaConfrontasAsociadas = new ArrayList<CBConfiguracionConfrontaModel>();
	private List<CBConfiguracionConfrontaModel> listaConfrontasSocias = new ArrayList<CBConfiguracionConfrontaModel>();
	private List<CBParametrosGeneralesModel> listaTipo = new ArrayList<CBParametrosGeneralesModel>();
	//private List<CBParametrosGeneralesModel> listaEstadoComision= new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> listaEstado= new ArrayList<CBParametrosGeneralesModel>();
	private List<CBConfiguracionConModel> listaConexiones= new ArrayList<CBConfiguracionConModel>();
	
	private List<CBConfiguracionConfrontaModel> listaConsulta = new ArrayList<CBConfiguracionConfrontaModel>();
	
	public void doAfterCompose(Component param) {
		try{
			super.doAfterCompose(param);
			log.debug("doAfterCompose" + " - \n*** Entra a modal de asociacion de confrontas ***\n");
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.INFO,
					//"\n*** Entra a modal de asociacion de confrontas ***\n");
			idAgrupacion = (String)session.getAttribute("idBanco");
			idEntidad = (String)session.getAttribute("idAgencia");
			idbancoagenciaconfronta = 0;
			log.debug("doAfterCompose" + " - ID Banco = " + idAgrupacion);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.INFO,"ID Banco = " + idAgrupacion);
			log.debug("doAfterCompose" + " - ID Agencia seleccionado = " + idEntidad);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.INFO,"ID Agencia seleccionado = " + idEntidad);
			metodosDeInicio();
			btnGuardar.setDisabled(false);
			btnModificar.setDisabled(true);
			usuario = obtenerUsuario().getUsuario();
			idSeleccionado = "0";
			cmbConfrontaPadre.setValue("SIN ASOCIAR");
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("doAfterCompose" + "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	public void metodosDeInicio() {
		llenaListbox();
		llenaComboConfrontas();
		llenaComboConfrontasAsociadas(idEntidad,idbancoagenciaconfronta);
		llenaComboTipo();
		//llenaComboEstadoComision();
		llenaComboEstado();
		llenaComboConexiones();
		llenaComboConfrontasSocias(idEntidad, idbancoagenciaconfronta);
	}
	
	public void onClick$btnGuardar() {
		try {
			if(asociacionConfrontaValida()) {
				CBBancoAgenciaConfrontaDAO objDAO = new CBBancoAgenciaConfrontaDAO();
				String confrontaPadre = null; 
				//int estadoComision = 0; 
				String conexion = null; 
				String nombreBusqueda = null; 
				//Double comision = 0.0; 
				Double aproximacion = 0.0; 
				int cantidadAjustes = 0; 
				int confrontasocia = 0;
				//int afiliacion = 0; 
				int resultado;
				String descartarTrans = null;
				if (cmbConfrontaPadre.getSelectedItem() != null && !cmbConfrontaPadre.getSelectedItem().getValue().toString().trim().equals("0")) {
					confrontaPadre = cmbConfrontaPadre.getSelectedItem().getValue().toString();
					System.out.println("entra");
				}
				System.out.println("entra" + confrontaPadre);
				/*
				if (cmbConfrontaPadre.getSelectedItem() != null) {
					if (cmbConfrontaPadre.getSelectedItem().getLabel().equals("Todas")) {
						confrontaPadre = null;
					} else {
						confrontaPadre = cmbConfrontaPadre.getSelectedItem().getValue();
					}
				}
				*/
				/*
				if (Double.parseDouble(txtComision.getText()) > 0) {
					comision = Double.parseDouble(txtComision.getText());
					//estadoComision = Integer.parseInt(cmbEstadoComision.getSelectedItem().getValue().toString().trim());
				}
				*/
				if (Double.parseDouble(txtAproximacion.getText()) > 0) {
					aproximacion = Double.parseDouble(txtAproximacion.getText());
					//estadoComision = Integer.parseInt(cmbEstadoComision.getSelectedItem().getValue().toString().trim());
				}
				if (cmbConexion.getSelectedItem() != null && !cmbConexion.getSelectedItem().toString().trim().equals("")) {
					conexion = cmbConexion.getSelectedItem().getValue().toString();
				}
				if(txtNombreBusqueda.getValue() != null && !txtNombreBusqueda.getValue().toString().trim().equals("")) {
					nombreBusqueda = txtNombreBusqueda.getValue().trim();
				} 
				if(txtCantidadAjustes.getValue() != null && !txtCantidadAjustes.getValue().toString().trim().equals("")) {
					cantidadAjustes = Integer.parseInt(txtCantidadAjustes.getValue().toString().trim());
				}
				
				
				if(txtDescartarTrans.getValue() != null && !txtDescartarTrans.getValue().toString().trim().equals("")) {
					descartarTrans = txtDescartarTrans.getValue().toString().trim();
				}
				/*
				if (cmbEstadoComision.getSelectedItem() != null && !cmbEstadoComision.getSelectedItem().toString().trim().equals("")) {
					estadoComision = Integer.parseInt(cmbEstadoComision.getSelectedItem().getValue().toString().trim());
				}
				*/
				
				
				
				if (cmbconfrontasocia.getSelectedItem() != null && !cmbconfrontasocia.getSelectedItem().getValue().toString().trim().equals("0")) {
					confrontasocia = Integer.parseInt(cmbconfrontasocia.getSelectedItem().getValue().toString());
					System.out.println("entra");
				}
				
				CBConfiguracionConfrontaModel objModel = new CBConfiguracionConfrontaModel();
				objModel.setCbCatalogoBancoId(idAgrupacion);
				objModel.setCbCatalogoAgenciaId(idEntidad);
				objModel.setcBConfiguracionConfrontaId(cmbConfConfronta.getSelectedItem().getValue().toString().trim());
				objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString().trim());
				objModel.setPathFtp(txtPathArchivo.getValue().toString().trim());
				objModel.setEstado(Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString().trim()));
				objModel.setIdConexionConf(conexion);
				objModel.setCreadoPor(usuario);
				objModel.setPalabraArchivo(nombreBusqueda);
				objModel.setCantidadAjustes(cantidadAjustes);
				objModel.setDescartarTransaccion(descartarTrans);
				//objModel.setComision(comision);
				objModel.setAproximacion(aproximacion);
				System.out.println("campo aproximacion " + aproximacion);
				objModel.setConfrontaPadre(confrontaPadre);
				objModel.setConfrontasocia(confrontasocia);
				//objModel.setEstadoComision(estadoComision);
				resultado = objDAO.insertaAsociacionConfronta(objModel);
				if (resultado > 0) {
					Messagebox.show("Asociacion de confronta registrada con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					onClick$btnLimpiar();
					llenaListbox();
					cleanCombo(cmbConfrontaPadre);
					llenaComboConfrontasAsociadas(idEntidad,idbancoagenciaconfronta);
				} else {
					Messagebox.show("No se pudo completar la operacion", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnGuardar"+ "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	public void onClick$btnModificar() {
		try {
			if(asociacionConfrontaValida()) {
				CBBancoAgenciaConfrontaDAO objDAO = new CBBancoAgenciaConfrontaDAO();
				String confrontaPadre = null; 
				//int estadoComision = 0; 
				String conexion = null; 
				String nombreBusqueda = null; 
			//	Double comision = 0.0; 
				Double aproximacion = 0.0; 
				int cantidadAjustes = 0; 
				int confrontasocia = 0;
				String descartarTrans = null;
				//int afiliacion = 0; 
				int resultado;
				/*
				if (cmbConfrontaPadre.getSelectedItem() != null && !cmbConfrontaPadre.getSelectedItem().toString().trim().equals("")) {
					confrontaPadre = cmbConfrontaPadre.getSelectedItem().getValue().toString();
				}
				*/
				if (cmbConfrontaPadre.getSelectedItem() != null && !cmbConfrontaPadre.getSelectedItem().getValue().toString().trim().equals("0")) {
					confrontaPadre = cmbConfrontaPadre.getSelectedItem().getValue().toString();
					System.out.println("entra");
				}
				/*
				if (Double.parseDouble(txtComision.getText()) > 0) {
					comision = Double.parseDouble(txtComision.getText());
					//estadoComision = Integer.parseInt(cmbEstadoComision.getSelectedItem().getValue().toString());
				}
				*/
				if (Double.parseDouble(txtAproximacion.getText()) > 0) {
					aproximacion = Double.parseDouble(txtAproximacion.getText());
					//estadoComision = Integer.parseInt(cmbEstadoComision.getSelectedItem().getValue().toString());
				}
				if (cmbConexion.getSelectedItem() != null && !cmbConexion.getSelectedItem().getValue().toString().trim().equals("")) {
					conexion = cmbConexion.getSelectedItem().getValue().toString();
				}
				if(txtNombreBusqueda.getValue() != null && !txtNombreBusqueda.getValue().toString().trim().equals("")) {
					nombreBusqueda = txtNombreBusqueda.getValue().trim();
				} 
				if(txtCantidadAjustes.getValue() != null && !txtCantidadAjustes.getValue().toString().trim().equals("")) {
					cantidadAjustes = Integer.parseInt(txtCantidadAjustes.getValue().toString().trim());
				}
				if(txtDescartarTrans.getValue() != null && !txtDescartarTrans.getValue().toString().trim().equals("")) {
					descartarTrans = txtDescartarTrans.getValue().toString().trim();
				}
				
				
				
				if (cmbconfrontasocia.getSelectedItem() != null && !cmbconfrontasocia.getSelectedItem().getValue().toString().trim().equals("0")) {
					confrontasocia = Integer.parseInt(cmbconfrontasocia.getSelectedItem().getValue().toString());
					System.out.println("entra");
				}
				
				/*
				if (cmbEstadoComision.getSelectedItem() != null && !cmbEstadoComision.getSelectedItem().toString().trim().equals("")) {
					estadoComision = Integer.parseInt(cmbEstadoComision.getSelectedItem().getValue().toString().trim());
				}
				*/
				CBConfiguracionConfrontaModel objModel = new CBConfiguracionConfrontaModel();
				objModel.setcBConfiguracionConfrontaId(cmbConfConfronta.getSelectedItem().getValue().toString().trim());
				objModel.setEstado(Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString().trim()));
				objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString().trim());
				objModel.setPathFtp(txtPathArchivo.getValue().toString().trim());
				objModel.setIdConexionConf(conexion);
				objModel.setPalabraArchivo(nombreBusqueda);
				objModel.setCantidadAjustes(cantidadAjustes);
				objModel.setDescartarTransaccion(descartarTrans);
				//objModel.setComision(comision);
				objModel.setAproximacion(aproximacion);
				objModel.setConfrontaPadre(confrontaPadre);
				objModel.setConfrontasocia(confrontasocia);
				//objModel.setEstadoComision(estadoComision);
				objModel.setModificadoPor(usuario);
				objModel.setCbBancoAgenciaConfrontaId(idSeleccionado);
				resultado = objDAO.actualizaConfronta(objModel);
				if (resultado > 0) {
					Messagebox.show("Asociacion de modificada con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					onClick$btnLimpiar();
					llenaListbox();
					cleanCombo(cmbConfrontaPadre);
					llenaComboConfrontasAsociadas(idEntidad,idbancoagenciaconfronta);
				} else {
					Messagebox.show("No se pudo completar la operacion", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnModificar"+ "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}
	
	public void onClick$btnLimpiar() {
		try {
			idSeleccionado = "0";
			idbancoagenciaconfronta=0;
			cmbConfConfronta.setSelectedIndex(-1);
		
			
			
			cmbConfrontaPadre.setValue("SIN ASOCIAR");
			//cmbConfrontaPadre.setText("SIN ASOCIAR");
			cmbTipo.setSelectedIndex(-1);
			/*
			if(cmbEstadoComision.getSelectedItem() != null && !cmbEstadoComision.getSelectedItem().getValue().toString().trim().equals("")){
				cmbEstadoComision.setSelectedIndex(-1);
			}
			*/
			cmbEstado.setSelectedIndex(-1);
			if (cmbConexion.getSelectedItem() != null && !cmbConexion.getSelectedItem().getValue().toString().trim().equals("")) {
				cmbConexion.setSelectedIndex(-1);
			}
			txtNombreBusqueda.setText("");
			//txtComision.setText("0.0");
			txtAproximacion.setText("0.0");
			txtCantidadAjustes.setText("0");
			cmbconfrontasocia.setValue("SIN ASOCIAR");
			txtDescartarTrans.setText("");
			txtPathArchivo.setText("");
			btnGuardar.setDisabled(false);;
			btnModificar.setDisabled(true);
			//txtAfiliacion.setText("0");
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnLimpiar"+ "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	/**
	 * Validar asociacion de confronta
	 * */
	public boolean asociacionConfrontaValida() {
		boolean resultado = false;
		try {
			if (cmbConfConfronta.getSelectedItem() == null || cmbConfConfronta.getSelectedItem().getValue().toString().trim().equals("")) {
				Messagebox.show("Se debe seleccionar una configuracion de confronta.", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbTipo.getSelectedItem() == null || cmbTipo.getSelectedItem().getValue().toString().trim().equals("")) {
				Messagebox.show("Debe seleccionar un tipo para asociar a las confrontas.", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbEstado.getSelectedItem() == null || cmbEstado.getSelectedItem().getValue().toString().trim().equals("")) {
				Messagebox.show("Debe ingresar un estado para la asociacion de confronta.", "ATENCION", 
						Messagebox.OK,Messagebox.EXCLAMATION);
			} else if(txtPathArchivo.getValue() == null || txtPathArchivo.getValue().toString().trim().equals("")) {
				Messagebox.show("Debe de ingresar la direccion del path del archivo.", "ATENCIÓN", 
						Messagebox.OK, Messagebox.EXCLAMATION);
			}  else if (!"".equals(txtAproximacion.getText())) {
				resultado = true; // Registro de asociacion valido
			}
				/*
				if (!"".equals(txtComision.getText())) {
					if (Double.parseDouble(txtComision.getText()) > 0) {
						if (cmbEstadoComision.getSelectedItem() == null
								|| cmbEstadoComision.getSelectedItem().getValue().toString().trim().equals("")) {
							Messagebox.show(
									"Si el valor de la comision es mayor que cero, se debe seleccionar un estado de comision",
									"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
						} else {
							resultado = true; // Registro de asociacion valido
						}
					} else if (Double.parseDouble(txtComision.getText()) == 0) {
						resultado = true; // Registro de asociacion valido
					}
				}
				*/
			
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("asociacionConfrontaValida"+ "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
		return resultado;
	}
	
	public void llenaListbox(){
		try {
			limpiarListbox(lbxConsulta);
			idSeleccionado = "0";
			CBBancoAgenciaConfrontaDAO objeDAO = new CBBancoAgenciaConfrontaDAO();
			listaConsulta = objeDAO.obtieneListadoBancoAgeConfrontaAsociacionConfrontas(idAgrupacion, idEntidad);
			Listitem item = null;
			Listcell cell = null;
			for (CBConfiguracionConfrontaModel data : listaConsulta) {
				item = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(data.getNombre()); //Nombre
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getDelimitador1()); //Delimitador
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getNomenclatura()); //Nomenclatura
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getPathFtp()); //Ruta del archivo
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getNombreConexion()); //Nombre de la conexion
				cell.setParent(item);
				
				cell = new Listcell();
				if(data.getConfrontasDependientes() > 0) { //Confronta principal
					cell.setImage("img/globales/16x16/check.png");
				} else {
					cell.setLabel(""); 
				}
				cell.setParent(item);
				
				cell = new Listcell();
				Button btnAgeComercial = new Button();
				btnAgeComercial.setImage("/img/globales/16x16/add.png"); //Agencia Comercial
				cell.setParent(item);
				btnAgeComercial.setParent(cell);
				btnAgeComercial.setTooltip("popAgenciaComercial");
				btnAgeComercial.setAttribute("idBancoAgenciaConfronta", data.getCbBancoAgenciaConfrontaId());
				btnAgeComercial.addEventListener(Events.ON_CLICK, evtAgenciaComercial);
				
				//agrega boton para los impuestos Ovidio Santos
				cell = new Listcell();
				Button btnAsignacionImpuestosConfrontas = new Button();
				btnAsignacionImpuestosConfrontas.setImage("/img/globales/16x16/add.png"); //impuestos
				cell.setParent(item);
				btnAsignacionImpuestosConfrontas.setParent(cell);
				btnAsignacionImpuestosConfrontas.setTooltip("popAsignacionImpuestos");
				btnAsignacionImpuestosConfrontas.setAttribute("idBancoAgenciaConfronta", data.getCbBancoAgenciaConfrontaId());
				btnAsignacionImpuestosConfrontas.addEventListener(Events.ON_CLICK, evtAsignacionImpuestosConfrontas);
				
				cell = new Listcell();
			    Button btnDelete = new Button();
			    btnDelete.setImage("/img/globales/16x16/delete.png"); //Eliminar asignacion
			    cell.setParent(item);
			    btnDelete.setParent(cell);
			    btnDelete.setTooltip("popEliminar");
			    btnDelete.setAttribute("idEliminar", data.getCbBancoAgenciaConfrontaId());
			    btnDelete.addEventListener(Events.ON_CLICK, evtEliminar);
			    
			    item.setValue(data);
				item.setParent(lbxConsulta);
				item.setAttribute("objSelected", data);
				item.addEventListener(Events.ON_CLICK, evtSelectedItem);
			} 
			log.debug("llenaListbox"+ "Llena de listbox de confrontas asignadas");
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName())
				//.log(Level.INFO,"- Llena de listbox de confrontas asignadas");
		} catch (Exception e) {
		//	Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
			log.error("llenaListbox"+ "error"+ e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Evento que se dispara al dar clic al boton de agencia comercial
	 * Seteo de valores en componentes de modal
	 * */
	EventListener<Event> evtAgenciaComercial = new EventListener<Event>() {
		public void onEvent(Event event) {
			try {
				session.setAttribute("idUnionConfronta", event.getTarget().getAttribute("idBancoAgenciaConfronta"));
				Executions.createComponents("/agenciaComercial.zul",null,null);
			} catch (Exception e) {
				log.error("evtAgenciaComercial"+ "error"+ e);
				//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
			}
		}
	};
	
	
	/**
	 * Evento que se dispara al dar clic al boton de asignacion de impuestos
	 * Seteo de valores en componentes de modal
	 * */
	EventListener<Event> evtAsignacionImpuestosConfrontas = new EventListener<Event>() {
		public void onEvent(Event event) {
			try {
				session.setAttribute("idUnionConfronta", event.getTarget().getAttribute("idBancoAgenciaConfronta"));
				Executions.createComponents("/cbAsignaImpuestosConfronta.zul",null,null);
			} catch (Exception e) {
				log.error("evtAsignacionImpuestosConfrontas"+ "error"+ e);
				//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
			}
		}
	};
	
	
	/**
	 * EventListener para eliminar configuracion de confronta seleccionada
	 * Modified by CarlosGodinez -> 14/09/2018
	 * Verificar si asociacion de confrontas posee registros de conciliacion
	 * antes de eliminar
	 * */
	EventListener<Event> evtEliminar = new EventListener<Event>() {
		public void onEvent(Event event) {
			try {
				int idFila = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
				log.debug("evtEliminar"+ "Id de asociacion de confronta a verificar antes de eliminar = " + idFila);
				//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.INFO,
						//"Id de asociacion de confronta a verificar antes de eliminar = " + idFila);
				CBBancoAgenciaConfrontaDAO objDAO = new CBBancoAgenciaConfrontaDAO();
				if (objDAO.cantidadRegistrosConciliacion(idFila) > 0) {
					Messagebox.show("No se puede eliminar una asociación de confronta que posea registros de conciliación.",
							"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
				} else {
					eliminarAsociacionConfronta(idFila);
				}
			} catch (Exception e) {
				log.error("evtEliminar"+ "error"+ e);
			//	Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			}
		}
	};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void eliminarAsociacionConfronta(final int idFila) {
		log.debug("eliminarAsociacionConfronta"+ "ID configuracion confronta a eliminar = " + idFila);
		//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.INFO,
				//"ID configuracion confronta a eliminar = " + idFila);
		Messagebox.show(
				"¿Desea eliminar la asociación de confronta seleccionada y todas sus agencias comerciales configuradas?",
				"CONFIRMACION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (((Integer) event.getData()).intValue() == Messagebox.YES) {
							CBBancoAgenciaConfrontaDAO cbbac = new CBBancoAgenciaConfrontaDAO();
							CBAgenciaComercialDAO cbac = new CBAgenciaComercialDAO();
							cbbac.eliminaAsociacionConfronta(String.valueOf(idFila));
							cbac.eliminarTodasAgeCom(String.valueOf(idFila));
							onClick$btnLimpiar();
							llenaListbox();
							cleanCombo(cmbConfrontaPadre);
							llenaComboConfrontasAsociadas(idEntidad,idbancoagenciaconfronta);
							Messagebox.show("Registro de asociación de confronta eliminado con exito.", "ATENCION",
									Messagebox.OK, Messagebox.INFORMATION);
						}
					}
				});
	}
	
	/**
	 * Evento que se dispara al seleccionar una asignacion de confronta
	 * Seteo de valores en componentes de modal
	 * */
	EventListener<Event> evtSelectedItem = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				onClick$btnLimpiar();
				btnGuardar.setDisabled(true);
				btnModificar.setDisabled(false);
				CBConfiguracionConfrontaModel objSeleccionado = (CBConfiguracionConfrontaModel) event.getTarget().getAttribute("objSelected");
				idSeleccionado = objSeleccionado.getCbBancoAgenciaConfrontaId();
				idbancoagenciaconfronta = Integer.parseInt(idSeleccionado);
				System.out.println("id seleccionado " + idSeleccionado);
				llenaComboConfrontasAsociadas(idEntidad,idbancoagenciaconfronta);
				for (Comboitem item : cmbConfConfronta.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getcBConfiguracionConfrontaId()))) {
						cmbConfConfronta.setSelectedItem(item);
					}
				}
				for (Comboitem item : cmbConfrontaPadre.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getcBAgenciasConfrontaId()))) {
						cmbConfrontaPadre.setSelectedItem(item);
					}
				}
				//txtComision.setText(String.valueOf(objSeleccionado.getComision()));
				txtAproximacion.setText(String.valueOf(objSeleccionado.getAproximacion()));
				for (Comboitem item : cmbTipo.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getTipo()))) {
						cmbTipo.setSelectedItem(item);
					}
				}
				txtCantidadAjustes.setText(String.valueOf(objSeleccionado.getCantidadAjustes()));
				
				
				


				for (Comboitem item : cmbEstado.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getEstado()))) {
						cmbEstado.setSelectedItem(item);
					}
				}
				
				System.out.println("confrontasocia:" + objSeleccionado.getConfrontasocia());
				for (Comboitem item : cmbconfrontasocia.getItems()) {
					
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getConfrontasocia()))) {
						System.out.println("confrontasocia:" + item.getValue());
						cmbconfrontasocia.setSelectedItem(item);
					}
					
					
				}
				
				txtDescartarTrans.setText(objSeleccionado.getDescartarTransaccion());
				for (Comboitem item : cmbConexion.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getIdConexionConf()))) {
						cmbConexion.setSelectedItem(item);
					}
				}
				txtNombreBusqueda.setText(objSeleccionado.getPalabraArchivo());
				txtPathArchivo.setText(objSeleccionado.getPathFtp());
			} catch (Exception e) {
				log.error("evtSelectedItem" + "error", e);
			//	Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
			}
		}
	};
	
	/**
	 * Metodos para llenado de Combobox al cargar vista
	 * */
	
	public void llenaComboConfrontas(){
		try {
			CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
			listaConfConfrontas = objeDAO.obtieneListaConfronta();
			for (CBConfiguracionConfrontaModel d : listaConfConfrontas) {
				Comboitem item = new Comboitem();
				item.setParent(cmbConfConfronta);
				item.setValue(d.getcBConfiguracionConfrontaId());
				item.setLabel(d.getNombre());
			} 
			log.debug("llenaComboConfrontas" + "Llena combo de confrontas");
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName())
				//.log(Level.INFO,"- Llena combo de confrontas");
		} catch (Exception e) {
			log.error("llenaComboConfrontas" + "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	public void llenaComboConfrontasAsociadas(String idEntidad, int idbancoagenciaconfronta) {
		//limpiaCombobox(cmbConfrontaPadre);
		//cmbConfrontaPadre.setSelectedIndex(-1);
		limpiaCombobox(cmbConfrontaPadre);
		CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
		listaConfrontasAsociadas = objeDAO.obtieneListaConfrontasAsociadas(Integer.parseInt(idEntidad),idbancoagenciaconfronta);
		Iterator<CBConfiguracionConfrontaModel> it = listaConfrontasAsociadas.iterator();
		CBConfiguracionConfrontaModel obj = null;
		//cmbAgencia.setName(Constantes.TODAS);
		Comboitem item = new Comboitem();
		item = new Comboitem();
		item.setValue("0");
		item.setLabel("SIN ASOCIAR");
		item.setParent(cmbConfrontaPadre);
		for (CBConfiguracionConfrontaModel d : listaConfrontasAsociadas) {
			item = new Comboitem();
			item.setParent(cmbConfrontaPadre);
			item.setValue(d.getcBAgenciasConfrontaId());
			item.setLabel(d.getConfrontaPadre());
		}
	}
	
	public void llenaComboConfrontasSocias(String idEntidad, int idbancoagenciaconfronta) {
		//limpiaCombobox(cmbConfrontaPadre);
		//cmbConfrontaPadre.setSelectedIndex(-1);
		limpiaCombobox(cmbconfrontasocia);
		CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
		listaConfrontasSocias = objeDAO.obtieneListaConfrontasAsociadass(Integer.parseInt(idEntidad),idbancoagenciaconfronta);
		Iterator<CBConfiguracionConfrontaModel> it = listaConfrontasSocias.iterator();
		CBConfiguracionConfrontaModel obj = null;
		//cmbAgencia.setName(Constantes.TODAS);
		Comboitem item = new Comboitem();
		item = new Comboitem();
		item.setValue("0");
		item.setLabel("SIN ASOCIAR");
		item.setParent(cmbconfrontasocia);
		for (CBConfiguracionConfrontaModel d : listaConfrontasSocias) {
			item = new Comboitem();
			item.setParent(cmbconfrontasocia);
			item.setValue(d.getcBAgenciasConfrontaId());
			item.setLabel(d.getConfrontaPadre());
		}
	}
	
	
	
	/*
	public void llenaComboConfrontasAsociadas(String idEntidad){
		try {
			CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
			listaConfrontasAsociadas = objeDAO.obtieneListaConfrontasAsociadas(Integer.parseInt(idEntidad));
			for (CBConfiguracionConfrontaModel d : listaConfrontasAsociadas) {
				Comboitem item = new Comboitem();
				item.setParent(cmbConfrontaPadre);
				item.setValue(d.getcBAgenciasConfrontaId());
				item.setLabel(d.getConfrontaPadre());
			} 
			Logger.getLogger(CBAsociacionConfrontasController.class.getName())
				.log(Level.INFO,"- Llena combo de confrontas asociadas");
		} catch (Exception e) {
			Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	*/
	public void llenaComboTipo(){
		try {
			CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
			listaTipo = objeDAO.obtenerParamConvenios();
			for (CBParametrosGeneralesModel d : listaTipo) {
				Comboitem item = new Comboitem();
				item.setParent(cmbTipo);
				/*
				 * Commented by CarlosGodinez -> 08/08/2018
				 * item.setValue(d.getValorObjeto2());
				 * item.setLabel(d.getValorObjeto1());
				 */
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			} 
			log.debug("llenaComboTipo" + "Llena combo tipo");
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName())
				//.log(Level.INFO,"- Llena combo tipo");
		} catch (Exception e) {
			log.error("llenaComboTipo" + "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	/*
	public void llenaComboEstadoComision(){
		try {
			CBBancoAgenciaConfrontaDAO objeDAO = new CBBancoAgenciaConfrontaDAO();
			listaEstadoComision = objeDAO.obtenerEstadoComisionCmb();
			for (CBParametrosGeneralesModel d : listaEstadoComision) {
				Comboitem item = new Comboitem();
				item.setParent(cmbEstadoComision);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			} 
			Logger.getLogger(CBAsociacionConfrontasController.class.getName())
				.log(Level.INFO,"- Llena combo estado comision");
		} catch (Exception e) {
			Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	*/
	
	public void llenaComboEstado(){
		try {
			CBCatalogoAgenciaDAO objeDAO = new CBCatalogoAgenciaDAO();
			listaEstado = objeDAO.obtenerEstadoCmb();
			for (CBParametrosGeneralesModel d : listaEstado) {
				Comboitem item = new Comboitem();
				item.setParent(cmbEstado);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			} 
			log.debug("llenaComboEstado" + "Llena combo de estado ");
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName())
				//.log(Level.INFO,"- Llena combo de estado ");
		} catch (Exception e) {
			log.error("llenaComboEstado" + "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	public void llenaComboConexiones(){
		try {
			CBConfiguracionConexionDao objeDAO = new CBConfiguracionConexionDao();
			CBConfiguracionConModel objModel = new CBConfiguracionConModel();
			listaConexiones = objeDAO.obtieneListaConexionex(objModel);
			for (CBConfiguracionConModel d : listaConexiones) {
				Comboitem item = new Comboitem();
				item.setParent(cmbConexion);
				item.setValue(d.getIdConexionConf());
				item.setLabel(d.getNombre());
			} 
			log.debug("llenaComboConexiones" + "Llena combo de conexiones");
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName())
				//.log(Level.INFO,"- Llena combo de conexiones");
		} catch (Exception e) {
			log.error("llenaComboConexiones" + "error", e);
			//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE,null,e);
		}
	}
	
	/**
	 * Metodo para limpiar cualquier Listbox y para poder agregar nuevos Listitems
	 * */
	public void limpiarListbox(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);
			}
		}
	}
	
	public void cleanCombo(Combobox component) {
		if (component != null) {
			if(listaConfrontasAsociadas != null && listaConfrontasAsociadas.size() > 0) {
				component.getItems().removeAll(component.getItems());
				listaConfrontasAsociadas.clear();
			}
		}
	}
}
