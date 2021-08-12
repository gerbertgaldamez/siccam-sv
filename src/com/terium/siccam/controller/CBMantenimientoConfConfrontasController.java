package com.terium.siccam.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.apache.commons.lang.StringUtils;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConfiguracionConfrontaDaoB;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBParametrosNomenclaturaModel;

/**
 * @author Carlos Godinez - Terium 
 * Controlador creado el 18/08/2017
 * */
public class CBMantenimientoConfConfrontasController extends ControladorBase{
	private static final long serialVersionUID = 9176164927878418930L;
	
	//Componentes
	private Button btnGuardar;
	private Button btnModificar;
	private Button btnAddNomen;
	private Button btnNuevaNomen;
	
	private Textbox txtNombreConf;
	private Textbox txtFormatoFecha;
	private Intbox txtLineaLectura;
	private Intbox txtLongitudFila;
	private Intbox txtInicia;
	private Intbox txtTermina;
	
	private Combobox cmbEstado;
	private Combobox cmbDelimitador;
	private Combobox cmbNomenclatura;
	
	private Listbox lbxNomenDelim;
	private Listbox lbxNomenNA;
	private Listbox lbxConsulta;
	
	private Groupbox gpbDelim;
	private Groupbox gpbNA;
	
	private Label lblTotalNomen;
	
	private Row rowLongitudFila;
	private Row rowInicioFin;
	
	//Arraylist para llenado de combobox y listbox
	private List<CBParametrosGeneralesModel> listaEstadoCmb = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> listaDelimitadoresCmb = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> listaNomenclaturasCmb = new ArrayList<CBParametrosGeneralesModel>();
	
	private List<CBConfiguracionConfrontaModel> listaConsulta= new ArrayList<CBConfiguracionConfrontaModel>();
	private List<CBParametrosNomenclaturaModel> listaNomenNA = new ArrayList<CBParametrosNomenclaturaModel>();
	private List<CBParametrosNomenclaturaModel> listaNomenDelim = new ArrayList<CBParametrosNomenclaturaModel>();
	
	//Propiedades
	private String usuario;
	private CBParametrosNomenclaturaModel objNomenSeleccionada;
	private Listitem itemNomenSeleccionado;
	
	private int idSeleccionado;
	private String confConfrontaSeleccionada;
	private String delimSeleccionado;
	private int banderaListboxNomen;
	
	public void doAfterCompose(Component param) throws Exception {
		try{
			super.doAfterCompose(param);
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "\n*** Entra a pantalla de configuracion de confrontas ***\n");
			llenaComboEstado();
			llenaComboDelimitador();
			llenaComboNomenclatura();
			txtLineaLectura.setValue(0);
			txtLongitudFila.setValue(0);
			txtInicia.setValue(0);
			txtTermina.setValue(0);
			rowLongitudFila.setVisible(false);
			rowInicioFin.setVisible(false);
			recargarTotalNomen(listaNomenDelim);
			gpbDelim.setVisible(true);
			gpbNA.setVisible(false);
			usuario = obtenerUsuario().getUsuario();
			btnModificar.setDisabled(true);
			btnNuevaNomen.setDisabled(true);
			idSeleccionado = 0;
			confConfrontaSeleccionada = "";
			objNomenSeleccionada = null;
			itemNomenSeleccionado = null;
			banderaListboxNomen = 2;
			onClick$btnConsultar();
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * Metodo que se dispara cuando se selecciona un valor del combobox delimitador
	 * */
	public void onSelect$cmbDelimitador(){
		try {
			delimSeleccionado = cmbDelimitador.getSelectedItem().getValue().toString();
			delimSeleccionado = delimSeleccionado.toLowerCase();
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "Delimitador seleccionado = " + delimSeleccionado);
			if(delimSeleccionado.equals("n/a")){
				gpbDelim.setVisible(false);
				gpbNA.setVisible(true);
				rowLongitudFila.setVisible(true);
				rowInicioFin.setVisible(true);
				limpiarListbox(lbxNomenDelim);
				if(listaNomenDelim != null && !listaNomenDelim.isEmpty()){
					listaNomenDelim.clear();
				}
				recargarTotalNomen(listaNomenNA);
			} else {
				gpbDelim.setVisible(true);
				gpbNA.setVisible(false);
				txtLongitudFila.setValue(0);
				txtInicia.setValue(0);
				txtTermina.setValue(0);
				rowLongitudFila.setVisible(false);
				rowInicioFin.setVisible(false);
				limpiarListbox(lbxNomenNA);
				if(listaNomenNA != null && !listaNomenNA.isEmpty()){
					listaNomenNA.clear();
				}
				recargarTotalNomen(listaNomenDelim);
			}
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	
	/**
	 * EventListener para EDITAR item de nomenclatura
	 * */
	EventListener<Event> evtSelectedItemNomen = new EventListener<Event>() {
		public void onEvent(Event event) {
			try {
				Listitem litem = (Listitem)event.getTarget().getAttribute("itemNomenclatura");
				CBParametrosNomenclaturaModel obj = (CBParametrosNomenclaturaModel)event.getTarget().getAttribute("objSelectedNomen");
				int listboxUsada = Integer.parseInt(event.getTarget().getAttribute("listboxUsada").toString());

				Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
					.log(Level.INFO, "Listbox usada = " + listboxUsada);
				for (Comboitem citem : cmbNomenclatura.getItems()) {
					if (citem.getValue().toString().equals(obj.getIdentificador())) {
						cmbNomenclatura.setSelectedItem(citem);
					}
				}
				if(listboxUsada == 1) { //1- N/A / 2- Con delimitador
					txtInicia.setValue(obj.getInicia());
					txtTermina.setValue(obj.getFinaliza());
				} 
				btnNuevaNomen.setDisabled(false);
				objNomenSeleccionada = obj;
				itemNomenSeleccionado = litem;
				banderaListboxNomen = listboxUsada;
				btnAddNomen.setImage("img/globales/16x16/guardar.png");
				btnAddNomen.setLabel("Guardar");
			} catch (Exception e) {
				Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			}
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "OBJ NOMENCLATURA SELECCIONADO: " + objNomenSeleccionada);
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "LISTITEM SELECCIONADO: " + itemNomenSeleccionado);
		}
	};
	
	/**
	 * Metodo para reestablecer ventana de asignacion de nomenclaturas para poder agregar nueva
	 * Vuelve a su estado original
	 * */
	public void onClick$btnNuevaNomen(){
		try{
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "Listbox usada restablecer = " + banderaListboxNomen);
			objNomenSeleccionada = null;
			btnAddNomen.setImage("img/globales/16x16/agregar.png");
			btnAddNomen.setLabel("Agregar");
			btnNuevaNomen.setDisabled(true);
			cmbNomenclatura.setSelectedIndex(-1);
			if(banderaListboxNomen == 1) {
				txtInicia.setValue(0);
				txtTermina.setValue(0);
			} 
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * EventListener para ELIMINAR item de nomenclatura
	 * */
	EventListener<Event> evtEliminarNomen = new EventListener<Event>() {
		public void onEvent(Event event) {
			try {
				Listitem item = (Listitem)event.getTarget().getAttribute("itemNomenclatura");
				CBParametrosNomenclaturaModel obj = (CBParametrosNomenclaturaModel)event.getTarget().getAttribute("objNomenclatura");
				int listboxUsada = Integer.parseInt(event.getTarget().getAttribute("listboxUsada").toString());
				Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
					.log(Level.INFO, "Listbox usada = " + listboxUsada);
				onClick$btnNuevaNomen();
				if(listboxUsada == 1) {
					//Listbox con N/A
					lbxNomenNA.removeChild(item);
					listaNomenNA.remove(obj);
					recargarTotalNomen(listaNomenNA);
				} else if(listboxUsada == 2) {
					//Listbox con delimitador
					lbxNomenDelim.removeChild(item);
					listaNomenDelim.remove(obj);
					recargarTotalNomen(listaNomenDelim);
				}
			} catch (Exception e) {
				Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			}
		}
	};
	
	public void onClick$btnAddNomen() {
		try {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "Objeto nomenclatura seleccionado = " + objNomenSeleccionada);
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "Operacion a realizar = " + (objNomenSeleccionada == null ? "Agregar" : "Modificar"));
			if(objNomenSeleccionada == null) {
				/**
				 * Se realizara la operacion de AGREGAR nomenclatura
				 * */
				Listitem item = null;
				Listcell cell = null;
				CBParametrosNomenclaturaModel objModel = null;
				if (delimSeleccionado == null || delimSeleccionado.equals("")) {
					Messagebox.show("Debe seleccionar un delimitador", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				} else if (cmbNomenclatura.getSelectedItem() == null
						|| cmbNomenclatura.getSelectedItem().getLabel().toString().equals("")) {
					Messagebox.show("Debe seleccionar una nomenclatura", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				} else {
					if ("n/a".equals(delimSeleccionado)) {
						/**
						 * SE HA SELECCIONADO N/A
						 */
						
						if(txtTermina.getValue() >= txtInicia.getValue()) {
							//Se agrega primero al ArrayList de nomenclaturas de delimitador
							objModel = new CBParametrosNomenclaturaModel();
							objModel.setNombre(cmbNomenclatura.getSelectedItem().getLabel().toString());
							objModel.setIdentificador(cmbNomenclatura.getSelectedItem().getValue().toString());
							objModel.setInicia(txtInicia.getValue());
							objModel.setFinaliza(txtTermina.getValue());
							listaNomenNA.add(objModel);
							
							//Se agrega luego a la Listbox de delimitador
							item = new Listitem();
		
							cell = new Listcell();
							cell.setLabel(objModel.getNombre());
							cell.setParent(item);
		
							cell = new Listcell();
							cell.setLabel(objModel.getIdentificador());
							cell.setParent(item);
							
							cell = new Listcell();
							cell.setLabel(String.valueOf(objModel.getInicia()));
							cell.setParent(item);
							
							cell = new Listcell();
							cell.setLabel(String.valueOf(objModel.getFinaliza()));
							cell.setParent(item);
		
							cell = new Listcell();
							Button btnDelete = new Button();
							btnDelete.setImage("/img/globales/16x16/delete.png");
							cell.setParent(item);
							btnDelete.setParent(cell);
							btnDelete.setTooltip("popEliminarNomen");
							btnDelete.setAttribute("itemNomenclatura", item);
							btnDelete.setAttribute("objNomenclatura", objModel);
							btnDelete.setAttribute("listboxUsada", 1);
							btnDelete.addEventListener(Events.ON_CLICK, evtEliminarNomen);
		
							item.setParent(lbxNomenNA);
							item.setAttribute("itemNomenclatura", item);
							item.setAttribute("objSelectedNomen", objModel);
							item.setAttribute("listboxUsada", 1);
							item.setTooltip("popSelected");
							item.addEventListener(Events.ON_CLICK, evtSelectedItemNomen);
							
							banderaListboxNomen = 1;
							recargarTotalNomen(listaNomenNA);
							onClick$btnNuevaNomen();
						} else {
							Messagebox.show("El numero de columna donde inicia el parámetro de INICIO no debe ser mayor al de FIN.", 
									"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
						}
					} else {
						/**
						 * SE HA SELECCIONADO UN DELIMITADOR
						 */
						
						//Se agrega primero al ArrayList de nomenclaturas de delimitador
						objModel = new CBParametrosNomenclaturaModel();
						objModel.setNombre(cmbNomenclatura.getSelectedItem().getLabel().toString());
						objModel.setIdentificador(cmbNomenclatura.getSelectedItem().getValue().toString());
						listaNomenDelim.add(objModel);
						
						//Se agrega luego a la Listbox de delimitador
						item = new Listitem();
	
						cell = new Listcell();
						cell.setLabel(objModel.getNombre());
						cell.setParent(item);
	
						cell = new Listcell();
						cell.setLabel(objModel.getIdentificador());
						cell.setParent(item);
	
						cell = new Listcell();
						Button btnDelete = new Button();
						btnDelete.setImage("/img/globales/16x16/delete.png");
						cell.setParent(item);
						btnDelete.setParent(cell);
						btnDelete.setTooltip("popEliminarNomen");
						btnDelete.setAttribute("itemNomenclatura", item);
						btnDelete.setAttribute("objNomenclatura", objModel);
						btnDelete.setAttribute("listboxUsada", 2);
						btnDelete.addEventListener(Events.ON_CLICK, evtEliminarNomen);
	
						item.setParent(lbxNomenDelim);
						item.setAttribute("itemNomenclatura", item);
						item.setAttribute("objSelectedNomen", objModel);
						item.setAttribute("listboxUsada", 2);
						item.setTooltip("popSelected");
						item.addEventListener(Events.ON_CLICK, evtSelectedItemNomen);
						
						banderaListboxNomen = 2;
						recargarTotalNomen(listaNomenDelim);
						onClick$btnNuevaNomen();
					}
				}
			} else {
				/**
				 * Se realizara la operacion de EDITAR nomenclatura
				 * */
				if (delimSeleccionado == null || delimSeleccionado.equals("")) {
					Messagebox.show("Debe seleccionar un delimitador", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				} else if (cmbNomenclatura.getSelectedItem() == null
						|| cmbNomenclatura.getSelectedItem().getLabel().toString().equals("")) {
					Messagebox.show("Debe seleccionar una nomenclatura", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				} else {
					if (delimSeleccionado.equals("n/a")) {
						/**
						 * Se modificara nomenclatura SIN delimitador (N/A)
						 * */
						if(txtTermina.getValue() >= txtInicia.getValue()) {
							CBParametrosNomenclaturaModel objModificado = new CBParametrosNomenclaturaModel();
							Listitem item = null;
							Listcell cell = null;
							objModificado = new CBParametrosNomenclaturaModel();
							objModificado.setNombre(cmbNomenclatura.getSelectedItem().getLabel().toString());
							objModificado.setIdentificador(cmbNomenclatura.getSelectedItem().getValue().toString());
							objModificado.setInicia(txtInicia.getValue());
							objModificado.setFinaliza(txtTermina.getValue());
							//Modificamos el valor en el ArrayList
							for(int i = 0; i < listaNomenNA.size(); i++) {
								CBParametrosNomenclaturaModel objEditar = listaNomenNA.get(i);
								if(objEditar.equals(objNomenSeleccionada)){
									listaNomenNA.set(i, objModificado);
								}
							}
							//Iteramos el ArrayList y generamos Listbox
							limpiarListbox(lbxNomenNA);
							for(CBParametrosNomenclaturaModel objLista : listaNomenNA){
								item = new Listitem();
								
								cell = new Listcell();
								cell.setLabel(objLista.getNombre());
								cell.setParent(item);
			
								cell = new Listcell();
								cell.setLabel(objLista.getIdentificador());
								cell.setParent(item);
								
								cell = new Listcell();
								cell.setLabel(String.valueOf(objLista.getInicia()));
								cell.setParent(item);
								
								cell = new Listcell();
								cell.setLabel(String.valueOf(objLista.getFinaliza()));
								cell.setParent(item);
			
								cell = new Listcell();
								Button btnDelete = new Button();
								btnDelete.setImage("/img/globales/16x16/delete.png");
								cell.setParent(item);
								btnDelete.setParent(cell);
								btnDelete.setTooltip("popEliminarNomen");
								btnDelete.setAttribute("itemNomenclatura", item);
								btnDelete.setAttribute("objNomenclatura", objLista);
								btnDelete.setAttribute("listboxUsada", 1);
								btnDelete.addEventListener(Events.ON_CLICK, evtEliminarNomen);
			
								item.setParent(lbxNomenNA);
								item.setAttribute("itemNomenclatura", item);
								item.setAttribute("objSelectedNomen", objLista);
								item.setAttribute("listboxUsada", 1);
								item.setTooltip("popSelected");
								item.addEventListener(Events.ON_CLICK, evtSelectedItemNomen);
								
								banderaListboxNomen = 1;
								recargarTotalNomen(listaNomenNA);
							}
							onClick$btnNuevaNomen();
						} else {
							Messagebox.show("El numero de columna donde inicia el parámetro de INICIO no debe ser mayor al de FIN.", 
									"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
						}
					} else {
						/**
						 * Se modificara nomenclatura CON delimitador
						 * */
						CBParametrosNomenclaturaModel objModificado = new CBParametrosNomenclaturaModel();
						Listitem item = null;
						Listcell cell = null;
						objModificado = new CBParametrosNomenclaturaModel();
						objModificado.setNombre(cmbNomenclatura.getSelectedItem().getLabel().toString());
						objModificado.setIdentificador(cmbNomenclatura.getSelectedItem().getValue().toString());
						//Modificamos el valor en el ArrayList
						for(int i = 0; i < listaNomenDelim.size(); i++) {
							CBParametrosNomenclaturaModel objEditar = listaNomenDelim.get(i);
							if(objEditar.equals(objNomenSeleccionada)){
								listaNomenDelim.set(i, objModificado);
							}
						}
						//Iteramos el ArrayList y generamos Listbox
						limpiarListbox(lbxNomenDelim);
						for(CBParametrosNomenclaturaModel objLista : listaNomenDelim){
							item = new Listitem();
							
							cell = new Listcell();
							cell.setLabel(objLista.getNombre());
							cell.setParent(item);
		
							cell = new Listcell();
							cell.setLabel(objLista.getIdentificador());
							cell.setParent(item);
							
							cell = new Listcell();
							Button btnDelete = new Button();
							btnDelete.setImage("/img/globales/16x16/delete.png");
							cell.setParent(item);
							btnDelete.setParent(cell);
							btnDelete.setTooltip("popEliminarNomen");
							btnDelete.setAttribute("itemNomenclatura", item);
							btnDelete.setAttribute("objNomenclatura", objLista);
							btnDelete.setAttribute("listboxUsada", 2);
							btnDelete.addEventListener(Events.ON_CLICK, evtEliminarNomen);
		
							item.setParent(lbxNomenDelim);
							item.setAttribute("itemNomenclatura", item);
							item.setAttribute("objSelectedNomen", objLista);
							item.setAttribute("listboxUsada", 2);
							item.setTooltip("popSelected");
							item.addEventListener(Events.ON_CLICK, evtSelectedItemNomen);
							
							banderaListboxNomen = 2;
							recargarTotalNomen(listaNomenDelim);
						}
						onClick$btnNuevaNomen();
					}
				}
			}
		} catch(WrongValueException wve){
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, wve);
			Messagebox.show("Error de validacion: " + wve.getMessage(), "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Metodo para realizar la insercion de registros de configuracion de confronta
	 * */
	public void onClick$btnGuardar() {
		try{
			if(configuracionConfrontaValida()){
				CBConfiguracionConfrontaModel objModel = new CBConfiguracionConfrontaModel();
				CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
				objModel.setNombre(txtNombreConf.getValue().toString().trim());
				objModel.setDelimitador1(cmbDelimitador.getSelectedItem().getValue().toString());
				objModel.setEstado(Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString()));
				objModel.setFormatoFecha(txtFormatoFecha.getValue().toString().trim());
				objModel.setLineaLectura(txtLineaLectura.getValue());
				objModel.setCreadoPor(usuario);
				if("n/a".equals(delimSeleccionado)) {
					objModel.setCantidadAgrupacion(listaNomenNA.size());
					objModel.setNomenclatura(obtenerNomenclatura(listaNomenNA));
					objModel.setPosiciones(obtenerPosiciones(listaNomenNA)); 
					objModel.setLongitudCadena(txtLongitudFila.getValue().toString());
				} else {
					objModel.setCantidadAgrupacion(listaNomenDelim.size());
					objModel.setNomenclatura(obtenerNomenclatura(listaNomenDelim));
					objModel.setPosiciones(null);
					objModel.setLongitudCadena(null);
				}
				if(objeDAO.insertar(objModel)){
					//Llena listbox principal
					onClick$btnLimpiar();
					onClick$btnConsultar();
					Messagebox.show("Datos registrados con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
				} else {
					Messagebox.show("No se pudo completar la operacion", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}
		} catch(WrongValueException wve){
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, wve);
			Messagebox.show("Error de validacion: " + wve.getMessage(), "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Metodo para realizar la modificacion de registros de configuracion de confronta
	 * */
	public void onClick$btnModificar() {
		try{
			if(idSeleccionado != 0) {
				if(configuracionConfrontaValida()){
					CBConfiguracionConfrontaModel objModel = new CBConfiguracionConfrontaModel();
					CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
					objModel.setNombre(txtNombreConf.getValue().toString().trim());
					objModel.setDelimitador1(cmbDelimitador.getSelectedItem().getValue().toString());
					objModel.setEstado(Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString()));
					objModel.setFormatoFecha(txtFormatoFecha.getValue().toString().trim());
					objModel.setLineaLectura(txtLineaLectura.getValue());
					objModel.setModificadoPor(usuario);
					objModel.setcBConfiguracionConfrontaId(String.valueOf(idSeleccionado));
					if("n/a".equals(delimSeleccionado)) {
						objModel.setCantidadAgrupacion(listaNomenNA.size());
						objModel.setNomenclatura(obtenerNomenclatura(listaNomenNA));
						objModel.setPosiciones(obtenerPosiciones(listaNomenNA)); 
						objModel.setLongitudCadena(txtLongitudFila.getValue().toString());
					} else {
						objModel.setCantidadAgrupacion(listaNomenDelim.size());
						objModel.setNomenclatura(obtenerNomenclatura(listaNomenDelim));
						objModel.setPosiciones(null);
						objModel.setLongitudCadena(null);
					}
					if(objeDAO.modificar(objModel)){
						//Llena listbox principal
						onClick$btnLimpiar();
						onClick$btnConsultar();
						Messagebox.show("Datos modificados con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					} else {
						Messagebox.show("No se pudo completar la operacion", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
					}
				}
			} else {
				Messagebox.show("No se ha seleccionado ningun registro de configuracion de confronta", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch(WrongValueException wve){
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, wve);
			Messagebox.show("Error de validacion: " + wve.getMessage(), "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Evento que se dispara al seleccionar un registro de configuracion de confronta de la Listbox principal
	 * Seteo de valores en componentes de ventanas
	 * */
	EventListener<Event> evtSelectedItem = new EventListener<Event>() {
		public void onEvent(Event event) {
			try {
				onClick$btnLimpiar();
				btnGuardar.setDisabled(true);
				btnModificar.setDisabled(false);
				CBConfiguracionConfrontaModel objSeleccionado = (CBConfiguracionConfrontaModel) event.getTarget().getAttribute("objSelected");
				idSeleccionado = Integer.parseInt(objSeleccionado.getcBConfiguracionConfrontaId());
				confConfrontaSeleccionada =  objSeleccionado.getNombre();
				txtNombreConf.setText(objSeleccionado.getNombre());
				txtFormatoFecha.setText(objSeleccionado.getFormatoFecha());
				txtLineaLectura.setValue(objSeleccionado.getLineaLectura());
				for (Comboitem item : cmbEstado.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getEstado()))) {
						cmbEstado.setSelectedItem(item);
					}
				}
				delimSeleccionado = objSeleccionado.getDelimitador1();
				for (Comboitem item : cmbDelimitador.getItems()) {
					if (item.getValue().toString().equals(objSeleccionado.getDelimitador1())) {
						cmbDelimitador.setSelectedItem(item);
					}
				}
				String delimitador = objSeleccionado.getDelimitador1();
				Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
					.log(Level.INFO, "Delimitador seleccionado = " + delimitador);
				if( "n/a".equals(delimitador)) {
					/**
					 * N/A
					 * */
					
					gpbDelim.setVisible(false);
					gpbNA.setVisible(true);
					rowLongitudFila.setVisible(true);
					rowInicioFin.setVisible(true);
					txtLongitudFila.setValue(Integer.parseInt(objSeleccionado.getLongitudCadena()));
					
					//Llenar ArrayList de nomenclaturas
					String[] splitNomenclaturas = objSeleccionado.getNomenclatura().split(",");
					String[] splitPosiciones = objSeleccionado.getPosiciones().split(",");
					CBParametrosNomenclaturaModel obj = null;
					for(int i = 0; i < splitNomenclaturas.length; i++){
						obj = new CBParametrosNomenclaturaModel();
						for (CBParametrosGeneralesModel objComboNomen : listaNomenclaturasCmb) {
							if(objComboNomen.getValorObjeto1().equals(splitNomenclaturas[i])) {
								obj.setNombre(objComboNomen.getObjeto());
								obj.setIdentificador(objComboNomen.getValorObjeto1());
								String[] splitSubPosiciones = splitPosiciones[i].split(" ");
								obj.setInicia(Integer.parseInt(splitSubPosiciones[0]));
								obj.setFinaliza(Integer.parseInt(splitSubPosiciones[1]));
								listaNomenNA.add(obj);
							}
						}
					}
					
					//Pintar tabla de nomenclaturas iterando ArrayList
					Listitem item = null;
					Listcell cell = null;
					for(CBParametrosNomenclaturaModel objModel : listaNomenNA) {
						//Se agrega luego a la Listbox de delimitador
						item = new Listitem();

						cell = new Listcell();
						cell.setLabel(objModel.getNombre());
						cell.setParent(item);

						cell = new Listcell();
						cell.setLabel(objModel.getIdentificador());
						cell.setParent(item);
						
						cell = new Listcell();
						cell.setLabel(String.valueOf(objModel.getInicia()));
						cell.setParent(item);
						
						cell = new Listcell();
						cell.setLabel(String.valueOf(objModel.getFinaliza()));
						cell.setParent(item);

						cell = new Listcell();
						Button btnDelete = new Button();
						btnDelete.setImage("/img/globales/16x16/delete.png");
						cell.setParent(item);
						btnDelete.setParent(cell);
						btnDelete.setTooltip("popEliminarNomen");
						btnDelete.setAttribute("itemNomenclatura", item);
						btnDelete.setAttribute("objNomenclatura", objModel);
						btnDelete.setAttribute("listboxUsada", 1);
						btnDelete.addEventListener(Events.ON_CLICK, evtEliminarNomen);

						item.setParent(lbxNomenNA);
						item.setAttribute("itemNomenclatura", item);
						item.setAttribute("objSelectedNomen", objModel);
						item.setAttribute("listboxUsada", 1);
						item.setTooltip("popSelected");
						item.addEventListener(Events.ON_CLICK, evtSelectedItemNomen);
						
						banderaListboxNomen = 1;
						recargarTotalNomen(listaNomenNA);
						//onClick$btnNuevaNomen();
					}
					recargarTotalNomen(listaNomenNA);
				} else {
					/**
					 *  LLEVA DELIMITADOR
					 * */
					
					gpbDelim.setVisible(true);
					gpbNA.setVisible(false);
					rowLongitudFila.setVisible(false);
					rowInicioFin.setVisible(false);
					txtLongitudFila.setValue(0);
					
					//Llenar ArrayList de nomenclaturas
					String[] splitNomenclaturas = objSeleccionado.getNomenclatura().split(",");
					CBParametrosNomenclaturaModel obj = null;
					for(int i = 0; i < splitNomenclaturas.length; i++){
						obj = new CBParametrosNomenclaturaModel();
						for (CBParametrosGeneralesModel objComboNomen : listaNomenclaturasCmb) {
							if(objComboNomen.getValorObjeto1().equals(splitNomenclaturas[i])) {
								obj.setNombre(objComboNomen.getObjeto());
								obj.setIdentificador(objComboNomen.getValorObjeto1());
								listaNomenDelim.add(obj);
							}
						}
					}
					
					//Pintar tabla de nomenclaturas iterando ArrayList
					Listitem item = null;
					Listcell cell = null;
					for(CBParametrosNomenclaturaModel objModel : listaNomenDelim) {
						//Se agrega luego a la Listbox de delimitador
						item = new Listitem();

						cell = new Listcell();
						cell.setLabel(objModel.getNombre());
						cell.setParent(item);

						cell = new Listcell();
						cell.setLabel(objModel.getIdentificador());
						cell.setParent(item);

						cell = new Listcell();
						Button btnDelete = new Button();
						btnDelete.setImage("/img/globales/16x16/delete.png");
						cell.setParent(item);
						btnDelete.setParent(cell);
						btnDelete.setTooltip("popEliminarNomen");
						btnDelete.setAttribute("itemNomenclatura", item);
						btnDelete.setAttribute("objNomenclatura", objModel);
						btnDelete.setAttribute("listboxUsada", 2);
						btnDelete.addEventListener(Events.ON_CLICK, evtEliminarNomen);

						item.setParent(lbxNomenDelim);
						item.setAttribute("itemNomenclatura", item);
						item.setAttribute("objSelectedNomen", objModel);
						item.setAttribute("listboxUsada", 2);
						item.setTooltip("popSelected");
						item.addEventListener(Events.ON_CLICK, evtSelectedItemNomen);
						
						banderaListboxNomen = 2;
						recargarTotalNomen(listaNomenDelim);
						//onClick$btnNuevaNomen();
					}
					recargarTotalNomen(listaNomenDelim);
				}
				btnGuardar.setDisabled(true);
				btnModificar.setDisabled(false);
				Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
					.log(Level.INFO, "ID parametro general seleccionado = " + idSeleccionado);
			} catch (Exception e) {
				Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			}
		}
	};
	
	/**
	 * Metodo para eliminar configuracion de confronta seleccionada
	 * */
	EventListener<Event> evtEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int idFila = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "ID configuracion confronta a eliminar = " + idFila);
			Messagebox.show("¿Desea eliminar la configuracion de confronta seleccionada?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
								if(objeDAO.eliminar(idFila)){
									onClick$btnLimpiar();
									onClick$btnConsultar();
									Messagebox.show("Registro de configuracion de confronta eliminado con exito.", "ATENCION", 
											Messagebox.OK,Messagebox.INFORMATION);
								}
							}
						}
					});
		}
	};
	
	/**
	 * Metodo para llenar Listbox de consulta general
	 * */
	public void onClick$btnConsultar(){
		try {
			limpiarListbox(lbxConsulta);
			CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
			listaConsulta = objeDAO.consultaGeneral();
			Listitem item = null;
			Listcell cell = null;
			for (CBConfiguracionConfrontaModel data : listaConsulta) {
				item = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(data.getNombre());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getDelimitador1());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getNomenclatura());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(String.valueOf(data.getCantidadAgrupacion()));
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getFormatoFecha());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(String.valueOf(data.getLineaLectura()));
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getLongitudCadena());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getEstadoTxt());
				cell.setParent(item);
				
				cell = new Listcell();
			    Button btnDelete = new Button();
			    btnDelete.setImage("/img/globales/16x16/delete.png");
			    cell.setParent(item);
			    btnDelete.setParent(cell);
			    btnDelete.setTooltip("popEliminar");
			    btnDelete.setAttribute("idEliminar", data.getcBConfiguracionConfrontaId());
			    btnDelete.addEventListener(Events.ON_CLICK, evtEliminar);
				
				item.setValue(data);
				item.setParent(lbxConsulta);
				item.setAttribute("objSelected", data);
				item.setTooltip("popSelected");
				item.addEventListener(Events.ON_CLICK, evtSelectedItem);
			} 
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "- Llena listbox de consulta general");
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Metodo para limpiar campos de formulario y reestablecer
	 * */
	public void onClick$btnLimpiar(){
		try{
			txtNombreConf.setText("");
			txtFormatoFecha.setText("");
			cmbEstado.setSelectedIndex(-1);
			cmbDelimitador.setSelectedIndex(-1);
			cmbNomenclatura.setSelectedIndex(-1);
			txtLineaLectura.setValue(0);
			txtLongitudFila.setValue(0);
			txtInicia.setValue(0);
			txtTermina.setValue(0);
			rowLongitudFila.setVisible(false);
			rowInicioFin.setVisible(false);
			limpiarListbox(lbxNomenNA);
			limpiarListbox(lbxNomenDelim);
			if(listaNomenNA != null && listaNomenNA.size() > 0){
				listaNomenNA.clear();
			}
			if(listaNomenDelim != null && listaNomenDelim.size() > 0){
				listaNomenDelim.clear();
			}
			recargarTotalNomen(listaNomenDelim);
			gpbDelim.setVisible(true);
			gpbNA.setVisible(false);
			btnGuardar.setDisabled(false);
			btnModificar.setDisabled(true);
			btnNuevaNomen.setDisabled(true);
			idSeleccionado = 0;
			confConfrontaSeleccionada = "";
			objNomenSeleccionada = null;
			itemNomenSeleccionado = null;
			banderaListboxNomen = 2;			
			btnAddNomen.setImage("img/globales/16x16/agregar.png");
			btnAddNomen.setLabel("Agregar");
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Metodo para validar registro de configuracion de confronta antes de insertar o modificar
	 * */
	public boolean configuracionConfrontaValida(){
		Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
			.log(Level.INFO, "\n***************************************");
		Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
			.log(Level.INFO, "*** VALIDACION DE CONF DE CONFRONTA ***");
		Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
			.log(Level.INFO, "***************************************");
		boolean resultado = false;
		try {
			CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
			if(txtNombreConf.getValue() == null || txtNombreConf.getValue().toString().trim().equals("")) {
				Messagebox.show("El nombre de la configuracion no puede ir vacio.", "ATENCIÓN", 
						Messagebox.OK, Messagebox.EXCLAMATION);
			} else if(txtFormatoFecha.getValue() == null || txtFormatoFecha.getValue().toString().trim().equals("")) { 
				Messagebox.show("El formato de la fecha no puede ir vacio.", "ATENCIÓN", 
						Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbEstado.getSelectedItem() == null || cmbEstado.getSelectedItem().toString().trim().equals("")) {
				Messagebox.show("Debe ingresar un estado para la configuracion de confronta.", "ATENCION", 
						Messagebox.OK,Messagebox.EXCLAMATION);
			} else if (cmbDelimitador.getSelectedItem() == null || cmbDelimitador.getSelectedItem().toString().trim().equals("")) {
				Messagebox.show("El delimitador no puede ir vacio.", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if(txtLineaLectura.getValue() == null) { 
				Messagebox.show("Se debe especificar desde que número de línea se empezará a leer el archivo "
						+ "de confronta.", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			}  else {
				if(delimSeleccionado.equals("n/a")) {
					if(listaNomenNA.size() == 0) {
						Messagebox.show("Se deben añadir nomenclaturas antes de completar la operación.", "ATENCION", 
								Messagebox.OK,Messagebox.EXCLAMATION);
					} else if(txtLongitudFila.getValue() == null) { 
						Messagebox.show("Se debe especificar la longitud de cada fila en el archivo de confronta.", 
								"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
					} else if(!objeDAO.formatoFechaValido(txtFormatoFecha.getValue().toString().trim())) { 
						Messagebox.show("El formato de la fecha no es válido.", "ATENCIÓN", Messagebox.OK,
								Messagebox.EXCLAMATION);
					} else if(objeDAO.confrontaExistente(txtNombreConf.getValue().toString().trim().toUpperCase()) 
							&& !txtNombreConf.getValue().toString().trim().toUpperCase().equals(confConfrontaSeleccionada.trim().toUpperCase())) { 
						Messagebox.show("La configuración de confronta ingresada ya esta existe.", "ATENCIÓN",
								Messagebox.OK, Messagebox.EXCLAMATION);
					} else {
						//Registro de configuracion de confronta valido
						resultado = true;
					}
				} else {
					if(listaNomenDelim.size() == 0) {
						Messagebox.show("Se deben añadir nomenclaturas antes de completar la operación.", "ATENCION", 
								Messagebox.OK,Messagebox.EXCLAMATION);
					} else if(!objeDAO.formatoFechaValido(txtFormatoFecha.getValue().toString().trim())) { 
						Messagebox.show("El formato de la fecha no es válido.", "ATENCIÓN", Messagebox.OK,
								Messagebox.EXCLAMATION);
					} else if(objeDAO.confrontaExistente(txtNombreConf.getValue().toString().trim().toUpperCase()) 
							&& !txtNombreConf.getValue().toString().trim().toUpperCase().equals(confConfrontaSeleccionada.trim().toUpperCase())) { 
						Messagebox.show("La configuración de confronta ingresada ya esta existe.", "ATENCIÓN",
								Messagebox.OK, Messagebox.EXCLAMATION);
					} else {
						//Registro de configuracion de confronta valido
						resultado = true;
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			return false;
		}
		Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
			.log(Level.INFO, "Configuracion de confronta valida = " + resultado);		
		return resultado;
	}

	/**
	 * Metodo para obtener nomenclatura separada por comas
	 * */
	public String obtenerNomenclatura(List<CBParametrosNomenclaturaModel> lista) {
		String nomenStr = "";
		List<String> nomenclatura = new ArrayList<String>();
		for(CBParametrosNomenclaturaModel obj : lista){
			nomenclatura.add(obj.getIdentificador());
		}
		nomenStr = StringUtils.join(nomenclatura, ",");
		//Impresion de valores
		Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
			.log(Level.INFO, "Valores nomenclatura: " + nomenStr);
		return nomenStr;
	}
	
	/**
	 * Metodo para obtener valor de posiciones separadas por comas
	 * */
	public String obtenerPosiciones(List<CBParametrosNomenclaturaModel> lista) {
		String posStr = "";
		List<String> posiciones = new ArrayList<String>();
		for(CBParametrosNomenclaturaModel obj : lista){
			posiciones.add(obj.getInicia() + " " + obj.getFinaliza());
		}
		posStr = StringUtils.join(posiciones, ",");
		 //Impresion de valores
		Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
			.log(Level.INFO, "Valores posiciones: " + posStr);
		return posStr;
	}
	
	/**
	 * Metodos para llenado de Combobox al cargar vista
	 * */
	
	public void llenaComboEstado(){
		try {
			CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
			listaEstadoCmb = objeDAO.obtenerEstadoCmb();
			for (CBParametrosGeneralesModel d : listaEstadoCmb) {
				Comboitem item = new Comboitem();
				item.setParent(cmbEstado);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			} 
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "- Llena combo de estado");
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void llenaComboDelimitador(){
		try {
			CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
			listaDelimitadoresCmb = objeDAO.obtenerParamDelimitadores();
			for (CBParametrosGeneralesModel d : listaDelimitadoresCmb) {
				Comboitem item = new Comboitem();
				item.setParent(cmbDelimitador);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
				item.setTooltiptext(d.getDescripcion());
			} 
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
				.log(Level.INFO, "- Llena combo de delimitadores");
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Carlos Godinez -> 07/11/2017
	 * 
	 * Se cambia de Hashmap a Arraylist para poder asignar un tooltip text
	 * */
	public void llenaComboNomenclatura(){
		try {
			CBConfiguracionConfrontaDaoB objeDAO = new CBConfiguracionConfrontaDaoB();
			listaNomenclaturasCmb = objeDAO.obtenerParamNomenclaturas();
			for (CBParametrosGeneralesModel d : listaNomenclaturasCmb) {
				Comboitem item = new Comboitem();
				item.setParent(cmbNomenclatura);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
				item.setTooltiptext(d.getDescripcion());
			}
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName())
			.log(Level.INFO, "- Llena combo de nomenclaturas");
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Metodo para recargar Total de nomenclaturas asignadas
	 * */
	public void recargarTotalNomen(List<CBParametrosNomenclaturaModel> lista){
		try {
			lblTotalNomen.setValue(String.valueOf(lista.size()));
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
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
}
