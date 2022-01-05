package com.terium.siccam.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
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
import com.terium.siccam.dao.CBAsignaImpuestosTiendasPropiasDAO;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.model.CBAsignaImpuestosModel;
import com.terium.siccam.model.CBBancoAgenciaAfiliacionesModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.Constantes;

public class CBAsignaComisionTiendasPropiasController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBAsignaComisionTiendasPropiasController.class);

	/**
	 * creador ovidio santos
	 */
	private static final long serialVersionUID = 1L;

	Button btnConsutar;
	Button btnNuevo;
	Button btnTienda;
	Button btnTiendaTodos;

	Button btnDelete;
	Button btnModificar;
	Button btnLimpiar;
	Listbox lbxConsultaImpuesto;
	int idseleccionado = 0;
	Combobox cmbTipoImpuesto;
	Combobox cmbTipo;
	Combobox cmbMedioDePago;
	Combobox cmbTipologia;
	Combobox cmbFormaDePago;
	Textbox txtValor = null;
	int comisionUso = 1;

	private String usuario;
	CBAsignaImpuestosTiendasPropiasDAO cbaDao = new CBAsignaImpuestosTiendasPropiasDAO();
	
	private int bancoAgenciaAfiliacionesId;

	private int idagencia;
	
	private int idcatalogobanco;

	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		bancoAgenciaAfiliacionesId = (Integer.valueOf(misession.getAttribute("idUnionConfronta").toString()));

		idagencia = (Integer.valueOf(misession.getAttribute("idagencia").toString()));
		
		idcatalogobanco = (Integer.valueOf(misession.getAttribute("idcatalogobanco").toString()));

		log.debug("doAfterCompose"+ " - ID banco agencia confronta enviado para configuracion asigna impuesto = "+ bancoAgenciaAfiliacionesId);
		//Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
			//	"ID banco agencia confronta enviado para configuracion asigna impuesto = ", bancoAgenciaAfiliacionesId);

		btnModificar.setDisabled(true);
		btnTienda.setDisabled(true);
		btnTiendaTodos.setDisabled(true);
		usuario = obtenerUsuario().getUsuario();
		
		llenaComboTipoImpuesto();
		llenaComboTipo();
		llenaComboMedioDePago();
		llenaComboTipologias();
		llenaComboFormaDePago();
		
		txtValor.setText("0.0");
		onClick$btnConsultar();
	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	////////////////////////////////////// llenado de combos
	private List<CBParametrosGeneralesModel> listaImpuestos = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboTipoImpuesto() {
		log.debug("llenaComboTipoImpuesto"+ " - Llena combo tipo IMPUESTO");
		//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO, "Llena combo tipo IMPUESTO");
		limpiarCombobox(cmbTipoImpuesto);
		CBAsignaImpuestosTiendasPropiasDAO objeDAO = new CBAsignaImpuestosTiendasPropiasDAO();
		this.listaImpuestos = objeDAO.obtenerImpuestos("CARGO_TIENDA");
		for (CBParametrosGeneralesModel d : this.listaImpuestos) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipoImpuesto);
			item.setValue(d.getCbmoduloconciliacionconfid());
			item.setLabel(d.getObjeto());
		}
	}

	private List<CBParametrosGeneralesModel> listaTipo = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboTipo() {
		log.debug("llenaComboTipo"+ " - Llena combo tipo");
		//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO, "Llena combo tipo");
		limpiarCombobox(cmbTipo);
		CBAsignaImpuestosTiendasPropiasDAO objeDAO = new CBAsignaImpuestosTiendasPropiasDAO();
		this.listaTipo = objeDAO.obtenertipo("TIPO_TIENDA");
		for (CBParametrosGeneralesModel d : this.listaTipo) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipo);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
		}
	}

	private List<CBParametrosGeneralesModel> listaMedioDePago = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboMedioDePago() {
		log.debug("llenaComboMedioDePago"+ " -Llena combo medio de pago");
		//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
			//	"Llena combo medio de pago");
		limpiarCombobox(cmbMedioDePago);
		CBAsignaImpuestosTiendasPropiasDAO objeDAO = new CBAsignaImpuestosTiendasPropiasDAO();
		this.listaMedioDePago = objeDAO.obtenerMedioDePago("MEDIO_PAGO_TIENDA");
		for (CBParametrosGeneralesModel d : this.listaMedioDePago) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbMedioDePago);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
		}
	}

	private List<CBTipologiasPolizaModel> listaTipologias = new ArrayList<CBTipologiasPolizaModel>();

	public void llenaComboTipologias() {
		log.debug("llenaComboTipologias"+ " -Llena combo tipologia");
		//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
				//"Llena combo tipologia");
		limpiarCombobox(cmbTipologia);
		CBAsignaImpuestosTiendasPropiasDAO objeDAO = new CBAsignaImpuestosTiendasPropiasDAO();
		this.listaTipologias = objeDAO.obtenerTipologias();
		for (CBTipologiasPolizaModel d : this.listaTipologias) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipologia);
			item.setValue(d.getCbtipologiaspolizaid());
			item.setLabel(d.getNombre());
		}
	}

	private List<CBParametrosGeneralesModel> listaFormaDePago = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboFormaDePago() {
		log.debug("llenaComboFormaDePago"+ " -Llena combo forma de pago");
		//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
			//	"Llena combo forma de pago");
		limpiarCombobox(cmbFormaDePago);
		CBAsignaImpuestosTiendasPropiasDAO objeDAO = new CBAsignaImpuestosTiendasPropiasDAO();
		this.listaFormaDePago = objeDAO.obtenerformaDePago("FORMA_PAGO_TIENDA");
		for (CBParametrosGeneralesModel d : this.listaFormaDePago) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbFormaDePago);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
		}
	}

	public void limpiarCombobox(Combobox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
		}
	}

	// boton agregar

	public void onClick$btnNuevo() {
		CBAsignaImpuestosTiendasPropiasDAO objDAO = new CBAsignaImpuestosTiendasPropiasDAO();
		CBAsignaImpuestosModel objModel = new CBAsignaImpuestosModel();
		String user = getUsuario();
		objModel.setCreadoPor(user);
		objModel.setComisionUso(2);

		if (cmbTipoImpuesto.getSelectedItem() == null) {
			Messagebox.show("Debe seleccionar tipo de Cargo.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (cmbTipo.getSelectedItem() == null) {
			Messagebox.show("Debe seleccionar tipo.", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (cmbMedioDePago.getSelectedItem() == null) {
			Messagebox.show("Debe seleccionar Medio de Pago.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (cmbTipologia.getSelectedItem() == null) {
			Messagebox.show("Debe seleccionar Tipologia.", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (cmbFormaDePago.getSelectedItem() == null) {
			Messagebox.show("Debe seleccionar Forma de Pago.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (txtValor.getText().trim() == null || txtValor.getText().equals("")) {
			Messagebox.show("Debe seleccionar Valor.", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			objModel.setImpuestoid(Integer.parseInt(cmbTipoImpuesto.getSelectedItem().getValue().toString()));
			objModel.setTipo(Integer.parseInt(cmbTipo.getSelectedItem().getValue().toString()));
			objModel.setMedioPago(Integer.parseInt(cmbMedioDePago.getSelectedItem().getValue().toString()));
			objModel.setTipologias(Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString()));
			objModel.setFormaPago(Integer.parseInt(cmbFormaDePago.getSelectedItem().getValue().toString()));
			objModel.setValor(txtValor.getText().trim());
			objModel.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);

			log.debug("onClick$btnNuevo"+ " -PARAM tipologias"+ objModel.getTipologias());
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
					//"PARAM tipologias", objModel.getTipologias());
			if (objDAO.insertImpuestos(objModel)) {
				Messagebox.show("Se creo el registro con exito", Constantes.ATENCION, Messagebox.OK,
						Messagebox.INFORMATION);
				CBAsignaImpuestosModel objModel1 = new CBAsignaImpuestosModel();
				onClick$btnLimpiar();
				objModel1.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);
				objModel1.setComisionUso(2);
				llenaListbox(cbaDao.obtenerImpuestos(objModel1));
			//	btnTienda.setDisabled(false);
			//	btnTiendaTodos.setDisabled(false);
			}
		}
	}

	public void onClick$btnModificar() {

		if (idseleccionado > 0) {

			CBAsignaImpuestosTiendasPropiasDAO objDAO = new CBAsignaImpuestosTiendasPropiasDAO();
			CBAsignaImpuestosModel objModel = new CBAsignaImpuestosModel();

			objModel.setModificadoPor(getUsuario());
			objModel.setComisionUso(2);

			if (cmbTipoImpuesto.getSelectedItem() == null) {
				Messagebox.show("Debe seleccionar tipo de Cargo.", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if (cmbTipo.getSelectedItem() == null) {
				Messagebox.show("Debe seleccionar tipo.", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbMedioDePago.getSelectedItem() == null) {
				Messagebox.show("Debe seleccionar Medio de Pago.", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if (cmbTipologia.getSelectedItem() == null) {
				Messagebox.show("Debe seleccionar Tipologia.", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if (cmbFormaDePago.getSelectedItem() == null) {
				Messagebox.show("Debe seleccionar Forma de Pago.", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if (txtValor.getText().trim() == null || txtValor.getText().equals("")) {
				Messagebox.show("Debe seleccionar Valor.", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			} else {

				objModel.setImpuestoid(Integer.parseInt(cmbTipoImpuesto.getSelectedItem().getValue().toString()));
				objModel.setTipo(Integer.parseInt(cmbTipo.getSelectedItem().getValue().toString()));
				objModel.setMedioPago(Integer.parseInt(cmbMedioDePago.getSelectedItem().getValue().toString()));
				objModel.setTipologias(Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString()));
				objModel.setFormaPago(Integer.parseInt(cmbFormaDePago.getSelectedItem().getValue().toString()));
				objModel.setValor(txtValor.getText().trim());

				objModel.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);

				log.debug("onClick$btnModificar"+ " -el id a modificar es "+ idseleccionado);
				//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
						//"el id a modificar es ", idseleccionado);
				if (objDAO.modificaImpuestos(objModel, idseleccionado)) {

					Messagebox.show("Se actualizo el registro", Constantes.ATENCION, Messagebox.OK,
							Messagebox.INFORMATION);
					CBAsignaImpuestosModel objModel1 = new CBAsignaImpuestosModel();
					onClick$btnLimpiar();
					objModel1.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);
					objModel1.setComisionUso(2);
					llenaListbox(cbaDao.obtenerImpuestos(objModel1));
					//btnTienda.setDisabled(false);
					//btnTiendaTodos.setDisabled(false);

				} else {
					Messagebox.show("No se Modifico, verifique los datos ingresados!", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);
				}

			}
		} else {
			Messagebox.show("Seleccione un registro!", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
		btnModificar.setDisabled(true);
		btnTienda.setDisabled(false);
		btnTiendaTodos.setDisabled(false);
		btnNuevo.setDisabled(false);
	}

	public void limpiarTextbox() {
		txtValor.setText("0.0");
		cmbTipoImpuesto.setSelectedIndex(-1);
		cmbMedioDePago.setSelectedIndex(-1);
		cmbTipo.setSelectedIndex(-1);
		cmbTipologia.setSelectedIndex(-1);
		cmbFormaDePago.setSelectedIndex(-1);

	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	// metodo consulta
	public void onClick$btnConsultar() {

		try {
			CBAsignaImpuestosModel objModel = new CBAsignaImpuestosModel();
			objModel.setComisionUso(2);

			if (cmbTipoImpuesto.getSelectedItem() != null) {
				objModel.setImpuestoid(Integer.parseInt(cmbTipoImpuesto.getSelectedItem().getValue().toString()));
			}
			objModel.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);
			if (cmbTipo.getSelectedItem() != null) {
				objModel.setTipo(Integer.parseInt(cmbTipo.getSelectedItem().getValue().toString()));
			}
			if (cmbMedioDePago.getSelectedItem() != null) {
				objModel.setMedioPago(Integer.parseInt(cmbMedioDePago.getSelectedItem().getValue().toString()));
			}
			if (cmbTipologia.getSelectedItem() != null) {
				objModel.setTipologias(Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString()));
			}
			if (cmbFormaDePago.getSelectedItem() != null) {
				objModel.setFormaPago(Integer.parseInt(cmbFormaDePago.getSelectedItem().getValue().toString()));
			}
			if (txtValor.getText().trim() != null && !"".equals(txtValor.getText().trim())) {
				objModel.setValor(txtValor.getText().trim());
			}
			llenaListbox(cbaDao.obtenerImpuestos(objModel));

			if (this.lbxConsultaImpuesto.getItemCount() != 0) {
				btnNuevo.setDisabled(false);
				btnTienda.setDisabled(false);
				btnTiendaTodos.setDisabled(false);
				btnModificar.setDisabled(true);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnConsultar" + "error", e);
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private List<CBBancoAgenciaAfiliacionesModel> list3 = new ArrayList<CBBancoAgenciaAfiliacionesModel>();
	private List<CBAsignaImpuestosModel> list2 = new ArrayList<CBAsignaImpuestosModel>();
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnTienda() {
		list2.clear();
		list3.clear();
		try {
			list3 = cbaDao.consultaafiliaciontiendas(idagencia);
			if (list3.size() > 0 && this.lbxConsultaImpuesto.getItemCount() != 0) {
				CBAsignaImpuestosModel objModel = null;
				
					for (Listitem item : lbxConsultaImpuesto.getItems()) {
						objModel = (CBAsignaImpuestosModel) item.getValue();;
						list2.add(objModel);
					}
				
					log.debug("onClick$btnTienda" + " - **** lista 2: ****" + list2.size());
				//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
						//"**** lista 2: ****" + list2.size());
					log.debug("onClick$btnTienda" + " - **** lista 3: ****" + list3.size());
				//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
						//"**** lista 3: ****" + list3.size());
				
			
					log.debug("onClick$btnTienda" + " - **** Se asigna tiendas de la misma entidad****");
		
				//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
						//"**** Se asigna tiendas de la misma entidad****");

				Messagebox.show(
						"Se aplicara estos registros para todas los tiendas de esta entidad, ¿desea continuar con la operación?",
						"CONFIRMACION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.YES) {
									if (cbaDao.eliminarImpuestosmasivo(list3)) {
										log.debug("onClick$btnTienda" + " - **** Se eliminan las configuraciones de tiendas antiguas ****");
										//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
											//	"**** Se eliminan las configuraciones de tiendas antiguas ****");
									}
									
									int resultado = cbaDao.asignarTiendasmasivos(list2,list3);
									if (resultado > 0) {
										Messagebox.show("Los datos han sido ingresados de forma correcta", Constantes.ATENCION,
												Messagebox.OK, Messagebox.INFORMATION);
										CBAsignaImpuestosModel objModel1 = new CBAsignaImpuestosModel();
										onClick$btnLimpiar();
										objModel1.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);
										objModel1.setComisionUso(2);
										llenaListbox(cbaDao.obtenerImpuestos(objModel1));
									//	btnTienda.setDisabled(false);
									//	btnTiendaTodos.setDisabled(false);

									}
								}
							}
						});
		
			} else {
				Messagebox.show("No se encontraron registros para esta tienda , vuelva  a intentarlo", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			log.error("onClick$btnTienda" + "error", e);
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnTiendaTodos() {
		list2.clear();
		list3.clear();
		try {
			list3 = cbaDao.consultaafiliaciontiendastodasagrupacion(idcatalogobanco);
			if (list3.size() > 0 && this.lbxConsultaImpuesto.getItemCount() != 0) {
				CBAsignaImpuestosModel objModel = null;
				
					for (Listitem item : lbxConsultaImpuesto.getItems()) {
						objModel = (CBAsignaImpuestosModel) item.getValue();;
						list2.add(objModel);
					}
				
					log.debug("onClick$btnTiendaTodos" + " - **** lista 2: ****" + list2.size());
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
					//"**** lista 2: ****" + list2.size());
					log.debug("onClick$btnTiendaTodos" + " - **** lista 3: ****" + list3.size());
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
					//"**** lista 3: ****" + list3.size());
	
					log.debug("onClick$btnTiendaTodos" + " - **** Se asigna tiendas masiva para toda la agrupacion ****");
				//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
					//	"**** Se asigna tiendas masiva para toda la agrupacion ****");
				Messagebox.show(
						"Se aplicara estos registros para todas las tiendas de esta agrupacion, ¿desea continuar con la operación?",
						"CONFIRMACION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.YES) {
									if (cbaDao.eliminarImpuestosmasivo(list3)) {
										log.debug("onClick$btnTiendaTodos" + " - **** Seeliminan las configuraciones de tiendas antiguas ****");
										//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
												//"**** Seeliminan las configuraciones de tiendas antiguas ****");
									}
									int resultado = cbaDao.asignarTiendasmasivos(list2,list3);
									if (resultado > 0) {
										Messagebox.show("Los datos han sido ingresados de forma correcta", Constantes.ATENCION,
												Messagebox.OK, Messagebox.INFORMATION);
										CBAsignaImpuestosModel objModel1 = new CBAsignaImpuestosModel();
										onClick$btnLimpiar();
										objModel1.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);
										objModel1.setComisionUso(2);
										llenaListbox(cbaDao.obtenerImpuestos(objModel1));
									//	btnTienda.setDisabled(false);
									//	btnTiendaTodos.setDisabled(false);

									}
								}
							}
						});
			
			} else {
				Messagebox.show("No se encontraron registros para esta tienda, segun la agrupacion seleccionada", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			log.error("onClick$btnTiendaTodos" + " error", e);
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}

	

	}
	
	/*
	 
	 @SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnTienda() {
		list2.clear();
		list3.clear();
		try {
		
			list3 = cbaDao.consultaafiliaciontiendas(idagencia);
			
			if (list3.size() > 0) {
				Iterator<CBBancoAgenciaAfiliacionesModel> it = list3.iterator();
				CBAsignaImpuestosModel objModel = null;
			int afiliacionid =0;
				while (it.hasNext()) {
					CBBancoAgenciaAfiliacionesModel objDepositos = null;
					objDepositos = it.next();
					
					objDepositos.getCbbancoagenciaafiliacionesid();
					afiliacionid = objDepositos.getCbbancoagenciaafiliacionesid();
					System.out.println("afiliacionid_objDepositos:" + objDepositos.getCbbancoagenciaafiliacionesid());
					for (Listitem item : lbxConsultaImpuesto.getItems()) {
						
						
						objModel = (CBAsignaImpuestosModel) item.getValue();
						objModel.setBancoagenciaafiliacionesid2(afiliacionid);
						System.out.println("afiliacionidwhile:" + afiliacionid);
						objModel.setTipo(objModel.getTipo());
						objModel.setImpuestoid(objModel.getImpuestoid());
						objModel.setTipologias(objModel.getTipologias());
						objModel.setFormaPago(objModel.getFormaPago());
						System.out.println("afiliacionid:" + objDepositos.getCbbancoagenciaafiliacionesid());
						list4.add(objModel);
					}
					
				}
				list2.addAll(list4);
				
				System.out.println("afiliacionid contador:" + objModel.getBancoagenciaafiliacionesid2());
				
				
				System.out.println("lista3:" + list3.size());
				System.out.println("lista2:" + list2.size());
				
			}
			
			
			
			if (list2.size() != 0) {
				System.out.println("lista3-:" + list3.size());
				System.out.println("lista2-:" + list2.size());
				System.out.println("idagencia:" + idagencia);
				Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
						"**** Se leera un archivo de depositos ****");

				// final String observaciones = txtObservaciones.getText().trim();

				Messagebox.show(
						"Se ha cargado un archivo de depósitos, por lo cual se asignará la tipología poliza ingresada a los registros "
								+ "que contengan los depósitos leídos del archivo cargado, ¿desea continuar con la operación?",
						"CONFIRMACION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.YES) {

									int resultado = cbaDao.asignarTiendasmasivos(list2,list3);
									if (resultado > 0) {

									}
								}
							}
						});
			} else {
				Messagebox.show("No se pudo actualizar ningun registro, revise el archivo de depositos", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}

	}
	 
	 
	 */
	

	// metodo para llenar listbox
	public void llenaListbox(List<CBAsignaImpuestosModel> list) {
		limpiarListbox(lbxConsultaImpuesto);

		log.debug("llenaListbox" + " cantidad de registros "+ list.size());
		//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
				//"cantidad de registros ", list.size());

		if (list.isEmpty()) {
			//btnTienda.setDisabled(false);
			//btnTiendaTodos.setDisabled(false);
			log.debug("llenaListbox" + " La lista viene vacia!");
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
					//"La lista viene vacia!");
		} else {

			//btnTienda.setDisabled(true);
		//	btnTiendaTodos.setDisabled(true);
			Iterator<CBAsignaImpuestosModel> it = list.iterator();

			Listitem item = null;
			Listcell cell = null;
			CBAsignaImpuestosModel objModel = null;

			while (it.hasNext()) {
				objModel = it.next();

				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getNombreImpuesto());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombreTipo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombreMedioPago());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombreTipologia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombreFormaPago());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getValor());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete_1.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbcomisionesconfrontaid());
				btnDelete.addEventListener("onClick", eventBtnEliminar);

				// item para btn modificar
				item.setAttribute("objmodificar", objModel);

				item.setAttribute("idseleccionado", objModel.getCbcomisionesconfrontaid());
				item.addEventListener(Events.ON_CLICK, eventBtnModificar);

				item.setAttribute("objModelModal", objModel);

				item.setValue(objModel);

				item.setTooltip("popAsociacionModal");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxConsultaImpuesto);
				
				if (this.lbxConsultaImpuesto.getItemCount() != 0) {
					btnTienda.setDisabled(false);
					btnTiendaTodos.setDisabled(false);
					
				} else {
					btnTienda.setDisabled(true);
					btnTiendaTodos.setDisabled(true);

				}
				
			}
			list.clear();
		}

	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			btnModificar.setDisabled(false);
			btnNuevo.setDisabled(true);
			btnTienda.setDisabled(true);
			btnTiendaTodos.setDisabled(true);
			limpiarTextbox();
			// se crea una variable personalizada
			CBAsignaImpuestosModel objmodificar = (CBAsignaImpuestosModel) arg0.getTarget()
					.getAttribute("objmodificar");
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");

			log.debug("eventBtnModificar" + " id seleccioando "+ idseleccionado);
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
					//"id seleccioando ", idseleccionado);

			for (Comboitem item : cmbTipoImpuesto.getItems()) {
				if (item.getValue().equals(objmodificar.getImpuestoid())) {
					cmbTipoImpuesto.setSelectedItem(item);
				}
			}

			if (objmodificar.getValor() != null) {
				txtValor.setText(objmodificar.getValor());
			} else {
				txtValor.setText("0.0");
			}
			for (Comboitem item : cmbTipo.getItems()) {
				if (item.getValue().equals(String.valueOf(objmodificar.getTipo()))) {
					cmbTipo.setSelectedItem(item);
				}
			}
			for (Comboitem item : cmbMedioDePago.getItems()) {
				if (item.getValue().equals(String.valueOf(objmodificar.getMedioPago()))) {
					cmbMedioDePago.setSelectedItem(item);
				}
			}
			for (Comboitem item : cmbTipologia.getItems()) {
				if (item.getValue().equals(objmodificar.getTipologias())) {
					cmbTipologia.setSelectedItem(item);
				}
			}
			for (Comboitem item : cmbFormaDePago.getItems()) {
				if (item.getValue().equals(String.valueOf(objmodificar.getFormaPago()))) {
					cmbFormaDePago.setSelectedItem(item);
				}
			}

		}
	};

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id

	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int idseleccionado = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
			log.debug("eventBtnEliminar" + " ID  a eliminar = "+ idseleccionado);
			//Logger.getLogger(CBAsignaComisionTiendasPropiasController.class.getName()).log(Level.INFO,
					//"ID  a eliminar = ", idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", Constantes.CONFIRMACION,
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBAsignaImpuestosTiendasPropiasDAO objDAO = new CBAsignaImpuestosTiendasPropiasDAO();
								if (objDAO.eliminarImpuestos(idseleccionado)) {
									onClick$btnLimpiar();
									CBAsignaImpuestosModel objModel = new CBAsignaImpuestosModel();
									objModel.setBancoagenciaafiliacionesid(bancoAgenciaAfiliacionesId);
									objModel.setComisionUso(2);
									limpiarTextbox();
									llenaListbox(cbaDao.obtenerImpuestos(objModel));
									Messagebox.show("Registros eliminado con exito.", Constantes.ATENCION,
											Messagebox.OK, Messagebox.INFORMATION);
								}
							}
						}
					});
			onClick$btnLimpiar();
		}
	};

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
