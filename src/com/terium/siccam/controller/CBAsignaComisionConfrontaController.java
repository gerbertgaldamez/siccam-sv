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
import com.terium.siccam.dao.CBAsignaImpuestosDAO;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.model.CBAsignaImpuestosModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.Constantes;

public class CBAsignaComisionConfrontaController extends ControladorBase {
	private static Logger log = Logger.getLogger(CBEstadoCuentaDAO.class);

	/**
	 * creador ovidio santos
	 */
	private static final long serialVersionUID = 1L;

	Button btnConsutar;
	Button btnNuevo;
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
	CBAsignaImpuestosDAO cbaDao = new CBAsignaImpuestosDAO();
	private int bancoAgenciaConfrontaId;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		bancoAgenciaConfrontaId = (Integer.valueOf(misession.getAttribute("idUnionConfronta").toString()));

		log.debug("doAfterCompose" + " - ID banco agencia confronta enviado para configuracion asigna impuesto = "+ bancoAgenciaConfrontaId);
		//Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
				//"ID banco agencia confronta enviado para configuracion asigna impuesto = ", bancoAgenciaConfrontaId);

		btnModificar.setDisabled(true);
		usuario = obtenerUsuario().getUsuario();
		llenaComboTipoImpuesto();
		llenaComboTipo();
		llenaComboMedioDePago();
		llenaComboTipologias();
		llenaComboFormaDePago();
		//txtValor.setText("0.0");
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
		log.debug("llenaComboTipoImpuesto" + " - Llena combo tipo IMPUESTO");
		//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO, "Llena combo tipo IMPUESTO");
		limpiarCombobox(cmbTipoImpuesto);
		CBAsignaImpuestosDAO objeDAO = new CBAsignaImpuestosDAO();
		this.listaImpuestos = objeDAO.obtenerImpuestos("CARGO");
		for (CBParametrosGeneralesModel d : this.listaImpuestos) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipoImpuesto);
			item.setValue(d.getCbmoduloconciliacionconfid());
			item.setLabel(d.getObjeto());
		}
	}

	private List<CBParametrosGeneralesModel> listaTipo = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboTipo() {
		log.debug("llenaComboTipo" + " - Llena combo tipo");
		//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO, "Llena combo tipo");
		limpiarCombobox(cmbTipo);
		CBAsignaImpuestosDAO objeDAO = new CBAsignaImpuestosDAO();
		this.listaTipo = objeDAO.obtenertipo("TIPO");
		for (CBParametrosGeneralesModel d : this.listaTipo) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipo);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
			item.setTooltiptext(d.getDescripcion()); //CarlosGodinez -> 02/08/2018
		}
	}
	
	private List<CBParametrosGeneralesModel> listaMedioDePago = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboMedioDePago() {
		log.debug("llenaComboMedioDePago" + " - Llena combo medio de pago");
		//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,
			//	"Llena combo medio de pago");
		limpiarCombobox(cmbMedioDePago);
		CBAsignaImpuestosDAO objeDAO = new CBAsignaImpuestosDAO();
		this.listaMedioDePago = objeDAO.obtenerMedioDePago("MEDIO_PAGO");
		for (CBParametrosGeneralesModel d : this.listaMedioDePago) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbMedioDePago);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
		}
	}

	private List<CBTipologiasPolizaModel> listaTipologias = new ArrayList<CBTipologiasPolizaModel>();

	public void llenaComboTipologias() {
		log.debug("llenaComboTipologias" + " - Llena combo tipologia");
		//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO, "Llena combo tipologia");
		limpiarCombobox(cmbTipologia);
		CBAsignaImpuestosDAO objeDAO = new CBAsignaImpuestosDAO();
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
		log.debug("llenaComboFormaDePago" + " - Llena combo forma de pago");
	//	Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,
				//"Llena combo forma de pago");
		limpiarCombobox(cmbFormaDePago);
		CBAsignaImpuestosDAO objeDAO = new CBAsignaImpuestosDAO();
		this.listaFormaDePago = objeDAO.obtenerformaDePago("FORMA_PAGO");
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

	/**
	 * Boton de agregar
	 * */
	public void onClick$btnNuevo() {
		try {
			CBAsignaImpuestosDAO objDAO = new CBAsignaImpuestosDAO();

			CBAsignaImpuestosModel objModel = new CBAsignaImpuestosModel();

			String user = getUsuario();

			objModel.setCreadoPor(user);
			objModel.setComisionUso(1);

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
				/**
				 * Added by CarlosGodinez -> 09/10/2018 Valida si se ha
				 * seleccionado el tipo de impuesto = MONTO FIJO/TOTAL Para
				 * todos los demas tipos de impuesto se aceptan valores menores a 1 
				 * para el tipo de impuesto MONTO FIJO/TOTAL se aceptan todos los valores
				 * 
				 * Se valida con variable porcentajeValido
				 */
				boolean porcentajeValido = false;
				String tipoImpuestoSeleccionado = cmbTipo.getSelectedItem().getValue().toString();
				String porcentajetipo = cmbTipo.getSelectedItem().getLabel().toString();
				log.debug("onClick$btnNuevo" + " - tipoImpuestoSeleccionado " + tipoImpuestoSeleccionado);
				//System.out.println("tipoImpuestoSeleccionado " + tipoImpuestoSeleccionado);
				
				String validoporcentaje = "";
				validoporcentaje = objDAO.obtenerporcentajevalido(tipoImpuestoSeleccionado,porcentajetipo);
				if(validoporcentaje == null) {
					validoporcentaje = "";
				}
				log.debug("onClick$btnNuevo" + " - validoporcentaje " + validoporcentaje);
				
				
				/**
				 * tipoImpuestoSeleccionado = 3 -> MONTO FIJO/TOTAL
				 */
				if (validoporcentaje.toUpperCase().trim().equals("FIJO")) {
					porcentajeValido = true;
				} else {
					log.debug("onClick$btnNuevo" + " - Porcentaje ingresado = " + txtValor.getText().trim());
					//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,
						//	"Porcentaje ingresado = " + txtValor.getText().trim());
					if (Double.parseDouble(txtValor.getText().trim()) < 1) {
						porcentajeValido = true;
					} else {
						porcentajeValido = false;
						Messagebox.show("El % debe estar expresado en decimales. \nEj. 2.5% = 0.025", 
								Constantes.ADVERTENCIA, Messagebox.OK, Messagebox.EXCLAMATION);
					}
				}

				if (porcentajeValido) {
					objModel.setImpuestoid(Integer.parseInt(cmbTipoImpuesto.getSelectedItem().getValue().toString()));
					objModel.setTipo(Integer.parseInt(cmbTipo.getSelectedItem().getValue().toString()));
					objModel.setMedioPago(Integer.parseInt(cmbMedioDePago.getSelectedItem().getValue().toString()));
					objModel.setTipologias(Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString()));
					objModel.setFormaPago(Integer.parseInt(cmbFormaDePago.getSelectedItem().getValue().toString()));
					objModel.setValor(txtValor.getText().trim());

					objModel.setBancoagenciaconfrontaid(bancoAgenciaConfrontaId);

					if (objDAO.insertImpuestos(objModel)) {

						Messagebox.show("Se creo el registro con exito", Constantes.ATENCION, Messagebox.OK,
								Messagebox.INFORMATION);

						CBAsignaImpuestosModel objModel1 = new CBAsignaImpuestosModel();
						onClick$btnLimpiar();
						objModel1.setBancoagenciaconfrontaid(bancoAgenciaConfrontaId);
						llenaListbox(cbaDao.obtenerImpuestos(objModel1));
					}
				}
			}
		} catch (Exception e) {
			log.error("onClick$btnNuevo" + "error", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onClick$btnModificar() {
		try {
			if (idseleccionado > 0) {

				CBAsignaImpuestosDAO objDAO = new CBAsignaImpuestosDAO();
				CBAsignaImpuestosModel objModel = new CBAsignaImpuestosModel();
				String user = getUsuario();

				objModel.setModificadoPor(user);
				objModel.setComisionUso(1);

				if (cmbTipoImpuesto.getSelectedItem() == null) {
					Messagebox.show("Debe seleccionar tipo de Cargo.", Constantes.ATENCION, Messagebox.OK,
							Messagebox.EXCLAMATION);

				} else if (cmbTipo.getSelectedItem() == null) {
					Messagebox.show("Debe seleccionar tipo.", Constantes.ATENCION, Messagebox.OK,
							Messagebox.EXCLAMATION);

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
					Messagebox.show("Debe seleccionar Valor.", Constantes.ATENCION, Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else {
					/**
					 * Added by CarlosGodinez -> 09/10/2018 Valida si se ha
					 * seleccionado el tipo de impuesto = MONTO FIJO/TOTAL Para
					 * todos los demas tipos de impuesto se aceptan valores menores a 1 
					 * para el tipo de impuesto MONTO FIJO/TOTAL se
					 * aceptan todos los valores
					 * 
					 * Se valida con variable porcentajeValido
					 */
					boolean porcentajeValido = false;
					String tipoImpuestoSeleccionado = cmbTipo.getSelectedItem().getValue().toString();
					String porcentajetipo = cmbTipo.getSelectedItem().getLabel().toString();
					log.debug("onClick$btnModificar" + " - tipoImpuestoSeleccionado " + tipoImpuestoSeleccionado);
					
					
					String validoporcentaje = "";
					validoporcentaje = objDAO.obtenerporcentajevalido(tipoImpuestoSeleccionado,porcentajetipo);
					if(validoporcentaje == null) {
						validoporcentaje = "";
					}
					log.debug("onClick$btnModificar" + " - validoporcentaje " + validoporcentaje.toUpperCase().trim());
					
					
					/**
					 * tipoImpuestoSeleccionado = 3 -> MONTO FIJO/TOTAL
					 */
					if ((validoporcentaje.toUpperCase().trim()).equals("PORCENTAJE")) {
						porcentajeValido = true;
					} else {
						log.debug("onClick$btnModificar" + " - Porcentaje ingresado = " + txtValor.getText().trim());
						//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,
								//"Porcentaje ingresado = " + txtValor.getText().trim());
						if (Double.parseDouble(txtValor.getText().trim()) < 1) {
							porcentajeValido = true;
						} else {
							porcentajeValido = false;
							Messagebox.show("El % debe estar expresado en decimales. \nEj. 2.5% = 0.025", 
									Constantes.ADVERTENCIA, Messagebox.OK, Messagebox.EXCLAMATION);
						}
					}

					if (porcentajeValido) {
						objModel.setImpuestoid(Integer.parseInt(cmbTipoImpuesto.getSelectedItem().getValue().toString()));
						objModel.setTipo(Integer.parseInt(cmbTipo.getSelectedItem().getValue().toString()));
						objModel.setMedioPago(Integer.parseInt(cmbMedioDePago.getSelectedItem().getValue().toString()));
						objModel.setTipologias(Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString()));
						objModel.setFormaPago(Integer.parseInt(cmbFormaDePago.getSelectedItem().getValue().toString()));
						objModel.setValor(txtValor.getText().trim());

						objModel.setBancoagenciaconfrontaid(bancoAgenciaConfrontaId);

						log.debug("onClick$btnModificar" + " - el id a modificar es "+ idseleccionado);
						//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,
								//"el id a modificar es ", idseleccionado);
						if (objDAO.modificaImpuestos(objModel, idseleccionado)) {

							Messagebox.show("Se actualizo el registro", Constantes.ATENCION, Messagebox.OK,
									Messagebox.INFORMATION);

							CBAsignaImpuestosModel objModel1 = new CBAsignaImpuestosModel();
							onClick$btnLimpiar();
							objModel1.setBancoagenciaconfrontaid(bancoAgenciaConfrontaId);
							llenaListbox(cbaDao.obtenerImpuestos(objModel1));

						} else {
							Messagebox.show("No se Modifico, verifique los datos ingresados!", Constantes.ATENCION,
									Messagebox.OK, Messagebox.EXCLAMATION);
						}
					}
				}
			} else {
				Messagebox.show("Seleccione un registro!", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			log.error("onClick$btnModificar" + "error", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
		btnModificar.setDisabled(true);
		btnNuevo.setDisabled(false);
	}

	public void limpiarTextbox() {
		//txtValor.setText("0.0");
		txtValor.setText("");
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

			if (cmbTipoImpuesto.getSelectedItem() != null) {
				objModel.setImpuestoid(Integer.parseInt(cmbTipoImpuesto.getSelectedItem().getValue().toString()));
			}

			objModel.setBancoagenciaconfrontaid(bancoAgenciaConfrontaId);

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

			if (txtValor.getText().trim() != null
					&& !"".equals(txtValor.getText().trim())) {
				objModel.setValor(txtValor.getText().trim());
				
			}
			llenaListbox(cbaDao.obtenerImpuestos(objModel));
			
			if (this.lbxConsultaImpuesto.getItemCount() != 0) {
				btnNuevo.setDisabled(false);
				btnModificar.setDisabled(true);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnConsultar" + "error", e);
			//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBAsignaImpuestosModel> list) {
		limpiarListbox(lbxConsultaImpuesto);

		
		if (list.isEmpty()) {
			log.debug("llenaListbox" + " - La lista viene vacia!");
			//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,"La lista viene vacia!");
		} else {
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
			}
			list.clear();
		}

	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			btnModificar.setDisabled(false);
			btnNuevo.setDisabled(true);
			limpiarTextbox();
			// se crea una variable personalizada
			CBAsignaImpuestosModel objmodificar = (CBAsignaImpuestosModel) arg0.getTarget()
					.getAttribute("objmodificar");
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");

			log.debug("eventBtnModificar" + " - id seleccioando "+ idseleccionado);
			//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,"id seleccioando ", idseleccionado);

			for (Comboitem item : cmbTipoImpuesto.getItems()) {
				if (item.getValue().equals(objmodificar.getImpuestoid())) {
					cmbTipoImpuesto.setSelectedItem(item);
				}
			}
			
			if (objmodificar.getValor() != null) {
				txtValor.setText(objmodificar.getValor());
			} else {
				//txtValor.setText("0.0");
				txtValor.setText("");
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
			log.debug("eventBtnEliminar" + " - ID  a eliminar = "+ idseleccionado);
			//Logger.getLogger(CBAsignaComisionConfrontaController.class.getName()).log(Level.INFO,"ID  a eliminar = ", idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBAsignaImpuestosDAO objDAO = new CBAsignaImpuestosDAO();
								if (objDAO.eliminarImpuestos(idseleccionado)) {
									onClick$btnLimpiar();
									CBAsignaImpuestosModel objModel = new CBAsignaImpuestosModel();
									objModel.setBancoagenciaconfrontaid(bancoAgenciaConfrontaId);
									limpiarTextbox();
									llenaListbox(cbaDao.obtenerImpuestos(objModel));
									Messagebox.show("Registro eliminado con exito.", Constantes.ATENCION, Messagebox.OK,
											Messagebox.INFORMATION);
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
