package com.terium.siccam.controller;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import javax.naming.NamingException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBBancoAgenciaAfiliacionesDAO;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBBancoAgenciaAfiliacionesModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;

public class CBBancoAgenciaAfiliacionesController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBEstadoCuentaDAO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CBParametrosGeneralesModel> listParamTipo = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> listParamEstado = new ArrayList<CBParametrosGeneralesModel>();
	CBBancoAgenciaAfiliacionesModel objModel = new CBBancoAgenciaAfiliacionesModel();

	Listbox lbxConsulta;
	int idseleccionado = 0;
	private Combobox cmbTipo;
	private Combobox cmbEstado;
	Button btnModificar;
	Button btnRegistrar;
	Button btnLimpiar;
	int idagencia= 0;
	int idcatalogobanco= 0;
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		setNombreBanco((String) session.getAttribute("nombreBanco"));
		setNombreAgencia((String) session.getAttribute("nombreAgencia"));
		objModel.setCbcatalogoagenciaid(Integer.parseInt(session.getAttribute("idAgencia").toString()));
		idagencia = (Integer.parseInt(session.getAttribute("idAgencia").toString()));
		idcatalogobanco = (Integer.parseInt(session.getAttribute("idBanco").toString()));
		
		btnRegistrar.setDisabled(false);
		btnModificar.setDisabled(true);

		// this.limpiarCampos();

		lblAgrupacion.setValue(getNombreBanco());
		lblNombreAgencia.setValue(getNombreAgencia());

		llenaComboTipo();
		llenaComboEstado();
		usuario = obtenerUsuario().getUsuario();
		onClick$btnConsultar();

	}

	// Usuario que inicia sesión
	private String usuario;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	// Propiedades de la tabla CB_BANCO_AGENCIA_AFILIACIONES

	private Textbox txtAfiliacion;
	private Label lblAgrupacion;
	private Label lblNombreAgencia;
	// Extra
	private String nombreBanco;
	private String nombreAgencia;

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public String getNombreAgencia() {
		return nombreAgencia;
	}

	public void setNombreAgencia(String nombreAgencia) {
		this.nombreAgencia = nombreAgencia;
	}
	/// cambio en buscar mvc

	/**
	 * Combo tipoo
	 */
	public void llenaComboTipo() {
		CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
		listParamTipo = objDao.obtenerParamCuentasTipo();
		if (listParamTipo != null && listParamTipo.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = listParamTipo.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto1());
				item.setParent(cmbTipo);
			}
		} else {
			Messagebox.show("Error al cargar la configuracion de cuentas", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	/**
	 * Combo estado
	 */
	public void llenaComboEstado() {
		CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
		listParamEstado = objDao.obtenerParamCuentasEstado();
		System.out.println("tama;o de la lista " + listParamEstado.size());
		if (listParamEstado != null && listParamEstado.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = listParamEstado.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto1());
				item.setParent(cmbEstado);
			}
		} else {
			Messagebox.show("Error al cargar la configuracion de cuentas", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	public void onClick$btnConsultar() throws SQLException, NamingException {

		// limpiarListbox(lbxConsulta);

		CBBancoAgenciaAfiliacionesDAO objeDAO = new CBBancoAgenciaAfiliacionesDAO();
		// CBBancoAgenciaAfiliacionesModel objModel = new
		// CBBancoAgenciaAfiliacionesModel();
		objModel.setCbcatalogoagenciaid(objModel.getCbcatalogoagenciaid());
		log.debug("onClick$btnConsultar" + " - Entra a consulta estados de afiliaciones");
		//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
				//"Entra a consulta estados de afiliaciones");

		// objModel.setUsuario(user);

		llenaListbox(objeDAO.consByIdAgencia(objModel));

		limpiarCampos();

	}

	public void llenaListbox(List<CBBancoAgenciaAfiliacionesModel> list) {
		limpiarListbox(lbxConsulta);
		CBBancoAgenciaAfiliacionesModel objModel = null;

		log.debug("llenaListbox" + " - cantidad de registros " + list.size());
		//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
				//"cantidad de registros " + list.size());
		if (list != null && list.size() > 0) {
			Iterator<CBBancoAgenciaAfiliacionesModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getTipo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getAfiliacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(objModel.getEstado() == 1 ? "ACTIVA" : "INACTIVA"));
				cell.setParent(item);

				// agrega boton para los impuestos Ovidio Santos
				cell = new Listcell();
				Button btnAsignacionImpuestosTiendasPropias = new Button();
				btnAsignacionImpuestosTiendasPropias.setImage("/img/globales/16x16/add.png"); // impuestos
				cell.setParent(item);
				btnAsignacionImpuestosTiendasPropias.setParent(cell);
				btnAsignacionImpuestosTiendasPropias.setTooltip("popAsignacionImpuestos");
				btnAsignacionImpuestosTiendasPropias.setAttribute("idBancoAgenciaAfiliaciones",
						objModel.getCbbancoagenciaafiliacionesid());
				btnAsignacionImpuestosTiendasPropias.addEventListener(Events.ON_CLICK,
						evtAsignacionImpuestosTiendasPropias);

				cell = new Listcell();
				Button btnDelete = new Button();
				// btnDelete.set("/img/globales/16x16/info.png");
				btnDelete.setImage("/img/globales/16x16/delete.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbbancoagenciaafiliacionesid());
				btnDelete.addEventListener("onClick", eventBtnEliminar);
				/////////////////
				/*
				 * cell = new Listcell(); Button btnModificar = new Button();
				 * btnModificar.setLabel("Modificar"); cell.setParent(item);
				 * 
				 * // item para btn modificar
				 */
				cell.setParent(item);
				item.setAttribute("objmodificar", objModel);
				item.setAttribute("idseleccionado", objModel.getCbbancoagenciaafiliacionesid());
				item.addEventListener("onClick", eventBtnModificar);

				///////////
				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxConsulta);

				if (this.lbxConsulta.getItemCount() != 0) {
					this.btnRegistrar.setDisabled(false);
					this.btnModificar.setDisabled(true);
					limpiarCampos();
				} else {
					this.btnRegistrar.setDisabled(false);
					this.btnModificar.setDisabled(true);

				}
				// paramsLlenaListbox);
			}
			// listConsulta.clear();
		} else {
			//Messagebox.show("No se encontraron resultados!", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id
	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int idseleccionado = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
			log.debug("eventBtnEliminar" + " - ID  a eliminar = " + idseleccionado);
			//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
					//"ID  a eliminar = " + idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBBancoAgenciaAfiliacionesDAO objDAO = new CBBancoAgenciaAfiliacionesDAO();
								// objeDAO.elim(Integer.parseInt(idFila));
								if (objDAO.eliminarAfiliacion(idseleccionado)) {
									// onClick$btnLimpiar();

									/// ACTUALIZA DESPUES DE ELIMINAR
									List<CBBancoAgenciaAfiliacionesModel> list = objDAO.consByIdAgencia(objModel);
									if (list.size() > 0) {
										llenaListbox(list);
										limpiarCampos();
										Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
												Messagebox.INFORMATION);

										// onClick$btnConsultar();
									} else {
										log.debug("eventBtnEliminar" + " - creo el registro pero no recarga consulta ");
										//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName())
												//.log(Level.INFO, "creo el registro pero no recarga consulta ");
										limpiarListbox(lbxConsulta);
										Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
												Messagebox.INFORMATION);
										limpiarCampos();
									}

								}
							}
						}
					});
		}
	};

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {
		public void onEvent(Event arg0) throws Exception {
			btnModificar.setDisabled(false);
			btnRegistrar.setDisabled(true);
			// se crea una variable personalizada
			CBBancoAgenciaAfiliacionesModel objmodificar = (CBBancoAgenciaAfiliacionesModel) arg0.getTarget()
					.getAttribute("objmodificar");
			log.debug("eventBtnModificar" + " - obj a modificar: " + objmodificar);
			//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
					//"obj a modificar: " + objmodificar);
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");

			log.debug("eventBtnModificar" + " - id seleccioando " + idseleccionado);
			//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
					//"id seleccioando " + idseleccionado);

			txtAfiliacion.setText(objmodificar.getAfiliacion());
			objModel.setAfiliacion(objmodificar.getAfiliacion());
			for (Comboitem item : cmbTipo.getItems()) {				
				if (item.getLabel().toString().trim().toUpperCase()
						.equals(objmodificar.getTipo().trim().toUpperCase())) {					
					cmbTipo.setSelectedItem(item);
				}
			}
			for (Comboitem item : cmbEstado.getItems()) {
				log.debug("eventBtnModificar" + " - for en modificar " + item.getValue());
				//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
						//"for en modificar " + item.getValue());
				log.debug("eventBtnModificar" + " - comboESTADO abtes de if " + objmodificar.getEstado());
				//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
						//"comboESTADO abtes de if " + objmodificar.getEstado());
				if (item.getValue().toString().equals(String.valueOf(objmodificar.getEstado()))) {
					log.debug("eventBtnModificar" + " - combo ESTADO en if " + objmodificar.getEstado());
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//"combo ESTADO en if " + objmodificar.getEstado());
					cmbEstado.setSelectedItem(item);
				}
			}
		}
	};

	/**
	 * Evento que se dispara al dar clic al boton de asignacion de impuestos Seteo
	 * de valores en componentes de modal
	 */
	EventListener<Event> evtAsignacionImpuestosTiendasPropias = new EventListener<Event>() {
		public void onEvent(Event event) {
			try {
				session.setAttribute("idUnionConfronta", event.getTarget().getAttribute("idBancoAgenciaAfiliaciones"));
				session.setAttribute("idagencia", idagencia);
				session.setAttribute("idcatalogobanco", idcatalogobanco);
				
				Executions.createComponents("/cbAsignaImpuestosTiendasPropias.zul", null, null);
			} catch (Exception e) {
				log.error("evtAsignacionImpuestosTiendasPropias" + "error", e);
				//Logger.getLogger(CBAsociacionConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};

	public void onClick$btnLimpiar() {
		limpiarCampos();
	}

	public void limpiarCampos() {
		objModel.setCbbancoagenciaafiliacionesid(0);
		objModel.setAfiliacion("");
		txtAfiliacion.setText("");
		cmbEstado.setSelectedIndex(-1);
		cmbTipo.setSelectedIndex(-1);
		btnModificar.setDisabled(true);
		btnRegistrar.setDisabled(false);
	}

	public void onClick$btnRegistrar() {	
		usuario = obtenerUsuario().getUsuario();
		objModel.setUsuario(obtenerUsuario().getUsuario());
		try {
			if (txtAfiliacion.getText().trim() != null && !txtAfiliacion.getText().trim().equals("")) {

				if (cmbTipo.getSelectedItem() != null && cmbEstado.getSelectedItem() != null) {

					CBBancoAgenciaAfiliacionesDAO objeDAO = new CBBancoAgenciaAfiliacionesDAO();
					
					objModel.setCbcatalogoagenciaid(objModel.getCbcatalogoagenciaid());
					log.debug("onClick$btnRegistrar" + " - cbcatalogoagenciaid en control guar " + objModel.getCbcatalogoagenciaid());
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
						//	"cbcatalogoagenciaid en control guar " + objModel.getCbcatalogoagenciaid());

					objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString());

					objModel.setAfiliacion(txtAfiliacion.getText().trim());
					log.debug("onClick$btnRegistrar" + " - txtafilicaion en control " + txtAfiliacion);
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
						//	"txtafilicaion en control " + txtAfiliacion);
					log.debug("onClick$btnRegistrar" + " - objmodelgetafiliacion en control " + objModel.getAfiliacion());
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//"objmodelgetafiliacion en control " + objModel.getAfiliacion());

					objModel.setEstado(Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString()));
					objModel.setUsuario(objModel.getUsuario());

					log.debug("onClick$btnRegistrar" + " - usuario en el control " + objModel.getUsuario());
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//"usuario en el control " + objModel.getUsuario());
					log.debug("onClick$btnRegistrar" + " - tipo en el control " + objModel.getTipo());
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//"tipo en el control " + objModel.getTipo());
					log.debug("onClick$btnRegistrar" + " - estado en el control " + objModel.getEstado());
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//"estado en el control " + objModel.getEstado());
					log.debug("onClick$btnRegistrar" + " - datos en tran valida " + objModel.getAfiliacion() + objModel.getCbcatalogoagenciaid());
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//"datos en tran valida " + objModel.getAfiliacion() + objModel.getCbcatalogoagenciaid());

					int pk_afiliacion = objeDAO.obtenerPKAfiliacion();
					log.debug("onClick$btnRegistrar" + " - pk afiliacion en guard " + pk_afiliacion);
					//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//"pk afiliacion en guard " + pk_afiliacion);
					if (objeDAO.transaccionValida(objModel)) {

						if (objeDAO.insertarAfiliacion(objModel, pk_afiliacion)) {
							onClick$btnConsultar();
							limpiarCampos();
							Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
						} else {
							Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
									Messagebox.EXCLAMATION);
						}
					} else {
						Messagebox.show(
								"La afiliación ingresada ya se encuentra registrada para la agencia seleccionada.",
								"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
					}
				} else {
					Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}
			} else {
				Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			log.error("onClick$btnRegistrar" + "error", e);
			//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", "ATENCIÓN", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	///

	public void onClick$btnModificar() {
		usuario = obtenerUsuario().getUsuario();
		objModel.setUsuario(obtenerUsuario().getUsuario());
		try {
			System.out.println("id seleccionado en condicion " + idseleccionado);
			if (idseleccionado > 0) {
				if (txtAfiliacion.getText() != null && !txtAfiliacion.getText().trim().equals("")) {

					if (cmbTipo.getSelectedItem() != null && cmbEstado.getSelectedItem() != null) {

						// setEstado(Integer.parseInt(getEstadoCombo()));
						CBBancoAgenciaAfiliacionesDAO objeDAO = new CBBancoAgenciaAfiliacionesDAO();
						// CBBancoAgenciaAfiliacionesModel objModel = new
						// CBBancoAgenciaAfiliacionesModel();

						objModel.setCbcatalogoagenciaid(objModel.getCbcatalogoagenciaid());
						log.debug("onClick$btnModificar" + " - cbcatalogoagenciaid en control guar " + objModel.getCbcatalogoagenciaid());
						//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
								//"cbcatalogoagenciaid en control guar " + objModel.getCbcatalogoagenciaid());
						objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString());

						log.debug("onClick$btnModificar" + " - txtafilicaion en control " + txtAfiliacion + objModel.getAfiliacion());
						//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
							//	"txtafilicaion en control " + txtAfiliacion + objModel.getAfiliacion());
						objModel.setEstado(Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString()));
						// objModel.setUsuario(objModel.getUsuario());

						log.debug("onClick$btnModificar" + " - usuario en el control " + objModel.getUsuario());
						//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
								//"usuario en el control " + objModel.getUsuario());
						log.debug("onClick$btnModificar" + " - tipo en el control modif" + objModel.getTipo());
						//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
								//"tipo en el control modif" + objModel.getTipo());
						log.debug("onClick$btnModificar" + " - estado en el control modif" + objModel.getEstado());
						//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
								//"estado en el control modif" + objModel.getEstado());

						objModel.setCbbancoagenciaafiliacionesid(idseleccionado);
						log.debug("onClick$btnModificar" + " - id seleccion modificar cbbancoagenciaafiliacion "
						//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
								//"id seleccion modificar cbbancoagenciaafiliacion "
										+ objModel.getCbbancoagenciaafiliacionesid());
						objModel.setCreador(usuario);
						if (txtAfiliacion.getText().equals(objModel.getAfiliacion().trim())) {
							objModel.setAfiliacion(txtAfiliacion.getText().trim());
							System.out.println("txtafiliacion  modi " + txtAfiliacion.getText());
							System.out.println("getafiliacion modi  " + objModel.getAfiliacion());
							if (objeDAO.actualizarAfiliacion(objModel, idseleccionado)) {
								onClick$btnConsultar();
								limpiarCampos();
								Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
							} else {
								Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
										Messagebox.EXCLAMATION);
							}
						} else {
							objModel.setAfiliacion(txtAfiliacion.getText().trim());
							System.out.println("entra a comprobar afiliacion");
							if (objeDAO.transaccionValida(objModel)) {
								if (objeDAO.actualizarAfiliacion(objModel, idseleccionado)) {
									onClick$btnConsultar();
									limpiarCampos();
									Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK,
											Messagebox.INFORMATION);
								} else {
									Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
											Messagebox.EXCLAMATION);
								}
							} else {
								Messagebox.show(
										"La afiliación ingresada ya se encuentra registrada para la agencia seleccionada.",
										"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
							}
						}
					} else {
						Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
								Messagebox.OK, Messagebox.EXCLAMATION);
					}
				} else {
					Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}

			} else {
				Messagebox.show("¡Seleccione una afiliacion!", "ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			log.error("onClick$btnModificar" + "error", e);
			//Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", "ATENCIÓN", Messagebox.OK,
					Messagebox.ERROR);

		}

	}
}
