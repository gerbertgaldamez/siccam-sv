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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBParametrosGeneralesModel;

public class CBParametrosGeneralesController extends ControladorBase{
	private static Logger log = Logger.getLogger(CBParametrosGeneralesController.class);

	private static final long serialVersionUID = 9176164927878418930L;
	
	//Lista de componentes
	private Textbox txtModulo;
	private Textbox txtTipoObjeto;
	private Textbox txtObjeto;
	private Textbox txtValor1;
	private Textbox txtValor2;
	private Textbox txtValor3;
	private Textbox txtDescripcion;
	private Combobox cmbEstado;
	
	private Button btnRegistrar;
	private Button btnModificar;
	private Listbox lstConsulta;
	
	//Propiedades
	private String usuario;
	private int idSeleccionado;
	private List<CBParametrosGeneralesModel> listaEstado = new ArrayList<CBParametrosGeneralesModel>();
	
	

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		System.out.println("\n*** Entra a pantalla de parametros generales ***\n");
		btnModificar.setDisabled(true);
		llenaComboEstado();
		usuario = obtenerUsuario().getUsuario();
		idSeleccionado = 0;
	}

	public void llenaComboEstado(){
		CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
		listaEstado = objeDAO.obtenerEstadoCmb();
		if(listaEstado.size() > 0) {
			for(CBParametrosGeneralesModel d : listaEstado)
		    {
				Comboitem item = new Comboitem();
			    item.setParent(cmbEstado);
			    item.setValue(d.getValorObjeto1());
			    item.setLabel(d.getObjeto());
		    }
		}
		
	}
	/* Opcion para mostrar los parametros*/
	public void onClick$btnConsultar() throws SQLException, NamingException {
		String moduloConsulta = "";
		String tipoObjetoConsulta = "";
		String objetoConsulta = "";
		String valor1Consulta = "";
		String valor2Consulta = "";
		String valor3Consulta = "";
		String descripcionConsulta = "";
		String estadoConsulta = "";
		CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
		log.debug("onClick$btnConsultar()  " + " - * Realiza consulta de parametros generales");
		//Logger.getLogger(CBParametrosGeneralesController.class.getName())
			//.log(Level.INFO, "* Realiza consulta de parametros generales");
		if(txtModulo.getText().trim() != null && !"".equals(txtModulo.getText().trim())) {
			moduloConsulta = txtModulo.getText().trim();
		}
		if(txtTipoObjeto.getText().trim() != null && !"".equals(txtTipoObjeto.getText().trim())) {
			tipoObjetoConsulta = txtTipoObjeto.getText().trim();
		}
		if(txtObjeto.getText().trim() != null && !"".equals(txtObjeto.getText().trim())) {
			objetoConsulta = txtObjeto.getText().trim();
		}
		/**
		 * Comentariado por Carlos Godinez - 07/06/2017
		 * 
		 * Se ha comentado para anular el .trim() ya que hay valores que van con espacios
		 * *
		if(txtValor1.getText().trim() != null && !txtValor1.getText().trim().equals("")) {
			valor1Consulta = txtValor1.getText().trim();
		}*/
		if(txtValor1.getText().trim() != null && !"".equals(txtValor1.getText().trim())) {
			valor1Consulta = txtValor1.getText();
		}
		if(txtValor2.getText().trim() != null && !"".equals(txtValor2.getText().trim())) {
			valor2Consulta = txtValor2.getText();
		}
		if(txtValor3.getText().trim() != null && !"".equals(txtValor3.getText().trim())) {
			valor3Consulta = txtValor3.getText();
		}
		if(txtDescripcion.getText().trim() != null && !"".equals(txtDescripcion.getText().trim())) {
			descripcionConsulta = txtDescripcion.getText().trim();
		}
		if(cmbEstado.getSelectedItem() != null) {
			estadoConsulta = cmbEstado.getSelectedItem().getValue().toString();
		}
		llenaListbox(objDao.consultaGeneral(moduloConsulta, tipoObjetoConsulta, objetoConsulta, valor1Consulta, valor2Consulta , valor3Consulta, descripcionConsulta, estadoConsulta));
		if(lstConsulta.getItemCount() != 0){
			btnRegistrar.setDisabled(true);
			btnModificar.setDisabled(false);
		} else {
			btnRegistrar.setDisabled(false);
			btnModificar.setDisabled(true);
		}
	}
	
	public void llenaListbox(List<CBParametrosGeneralesModel> list){
		limpiarListbox(lstConsulta);
		if(list.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = list.iterator();
			CBParametrosGeneralesModel obj = null;
			Listitem item = null;
			Listcell cell = null;
			while(it.hasNext()) {
				obj = it.next();
				
				item = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(obj.getModulo());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getTipoObjeto());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getObjeto());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getValorObjeto1());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getValorObjeto2());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getValorObjeto3());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getDescripcion());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getEstado().equals("S") ? "ACTIVO" : "INACTIVO");
				cell.setParent(item);
				
				cell = new Listcell();
			    Button btnDelete = new Button();
			    btnDelete.setImage("/img/globales/16x16/delete.png");
			    cell.setParent(item);
			    btnDelete.setParent(cell);
			    btnDelete.setTooltip("popEliminar");
			    btnDelete.setAttribute("idEliminar", obj.getCbmoduloconciliacionconfid());
			    btnDelete.addEventListener(Events.ON_CLICK, evtEliminar);
				
				item.setValue(obj);
				item.setParent(lstConsulta);
				item.setAttribute("objSelected", obj);
				item.setTooltip("popSelected");
				item.addEventListener(Events.ON_CLICK, evtSelectedItem);
			}
			list.clear();
		}else {
			Messagebox.show("¡No se encontraron resultados!","ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	EventListener<Event> evtSelectedItem = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			CBParametrosGeneralesModel objSeleccionado = (CBParametrosGeneralesModel) event.getTarget().getAttribute("objSelected");
			idSeleccionado = objSeleccionado.getCbmoduloconciliacionconfid();
			txtModulo.setText(objSeleccionado.getModulo());
			txtTipoObjeto.setText(objSeleccionado.getTipoObjeto());
			txtObjeto.setText(objSeleccionado.getObjeto());
			txtValor1.setText(objSeleccionado.getValorObjeto1());
			txtValor2.setText(objSeleccionado.getValorObjeto2());
			txtValor3.setText(objSeleccionado.getValorObjeto3());
			txtDescripcion.setText(objSeleccionado.getDescripcion());
			for (Comboitem item : cmbEstado.getItems()) {
				if (item.getValue().toString().equals(objSeleccionado.getEstado())) {
					cmbEstado.setSelectedItem(item);
				}
			}
			btnRegistrar.setDisabled(true);
			btnModificar.setDisabled(false);
			log.debug("evtSelectedItem()  " + " - ID parametro general seleccionado = " + idSeleccionado);
			//Logger.getLogger(CBParametrosGeneralesController.class.getName())
				//.log(Level.INFO, "ID parametro general seleccionado = " + idSeleccionado);
		}
	};

	public void onClick$btnLimpiar(){
		idSeleccionado = 0;
		txtModulo.setText("");
		txtTipoObjeto.setText("");
		txtObjeto.setText("");
		txtValor1.setText("");
		txtValor2.setText("");
		txtValor3.setText("");
		txtDescripcion.setText("");
		cmbEstado.setSelectedIndex(-1);
		btnRegistrar.setDisabled(false);
		btnModificar.setDisabled(true);
	}
	
	public void onClick$btnRegistrar() {
		try {
			if (txtModulo.getText() == null || "".equals(txtModulo.getText().trim())) {
				Messagebox.show("Debe ingresar un nombre de modulo.", "ATENCION", Messagebox.OK,Messagebox.EXCLAMATION);
			} else if (txtTipoObjeto.getText() == null || "".equals(txtTipoObjeto.getText().trim())) {
				Messagebox.show("Debe ingresar un tipo de objeto.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (txtObjeto.getText() == null || "".equals(txtObjeto.getText().trim())) {
				Messagebox.show("Debe ingresar un objeto.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (txtValor1.getText() == null || "".equals(txtValor1.getText().trim())) {
				Messagebox.show("Debe ingresar el valor 1, si el objeto requiere mas de un valor los valores 2 y 3 son opcionales.","ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbEstado.getSelectedItem() == null) {
				Messagebox.show("Debe ingresar un estado para el parametro ingresado.", "ATENCION", Messagebox.OK,Messagebox.EXCLAMATION);
			} else {
				CBParametrosGeneralesModel objeModel = new CBParametrosGeneralesModel();
				objeModel.setModulo(txtModulo.getText().trim().toUpperCase().replace(" ", "_"));
				objeModel.setTipoObjeto(txtTipoObjeto.getText().trim().toUpperCase().replace(" ", "_"));
				objeModel.setObjeto(txtObjeto.getText().trim());
				objeModel.setValorObjeto1(txtValor1.getText());
				objeModel.setValorObjeto2((txtValor2.getText() != null && !"".equals(txtValor2.getText().trim()))
						? txtValor2.getText() : "");
				objeModel.setValorObjeto3((txtValor3.getText() != null && !"".equals(txtValor3.getText().trim()))
						? txtValor3.getText() : "");
				objeModel.setDescripcion((txtDescripcion.getText() != null && !"".equals(txtDescripcion.getText().trim()))
						? txtDescripcion.getText().trim() : "");
				objeModel.setEstado(cmbEstado.getSelectedItem().getValue().toString());
				objeModel.setCreador(usuario);
				CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
				if (objDao.insertar(objeModel)) {
					onClick$btnLimpiar();
					llenaListbox(objDao.consultaGeneral("", "", "", "", "", "", "", ""));
					Messagebox.show("Datos registrados con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
				} else {
					Messagebox.show("No se pudo completar la operacion", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnRegistrar() -  Error ", e);
			//Logger.getLogger(CBParametrosGeneralesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnModificar() {
		try {
			if(idSeleccionado == 0){
				Messagebox.show("Debe seleccionar un registro para ejecutar la operacion.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (txtModulo.getText() == null || "".equals(txtModulo.getText().trim())) {
				Messagebox.show("Debe ingresar un nombre de modulo.", "ATENCION", Messagebox.OK,Messagebox.EXCLAMATION);
			} else if (txtTipoObjeto.getText() == null || "".equals(txtTipoObjeto.getText().trim())) {
				Messagebox.show("Debe ingresar un tipo de objeto.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (txtObjeto.getText() == null || "".equals(txtObjeto.getText().trim())) {
				Messagebox.show("Debe ingresar un objeto.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (txtValor1.getText() == null || "".equals(txtValor1.getText().trim())) {
				Messagebox.show("Debe ingresar el valor 1, si el objeto requiere mas de un valor los valores 2 y 3 son opcionales.","ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbEstado.getSelectedItem() == null) {
				Messagebox.show("Debe ingresar un estado para el parametro ingresado.", "ATENCION", Messagebox.OK,Messagebox.EXCLAMATION);
			} else {
				CBParametrosGeneralesModel objeModel = new CBParametrosGeneralesModel();
				objeModel.setModulo(txtModulo.getText().trim().toUpperCase().replace(" ", "_"));
				objeModel.setTipoObjeto(txtTipoObjeto.getText().trim().toUpperCase().replace(" ", "_"));
				objeModel.setObjeto(txtObjeto.getText().trim());
				objeModel.setValorObjeto1(txtValor1.getText());
				objeModel.setValorObjeto2((txtValor2.getText() != null && !"".equals(txtValor2.getText().trim()))
						? txtValor2.getText() : "");
				objeModel.setValorObjeto3((txtValor3.getText() != null && !"".equals(txtValor3.getText().trim()))
						? txtValor3.getText() : "");
				objeModel.setDescripcion((txtDescripcion.getText() != null && !"".equals(txtDescripcion.getText().trim()))
						? txtDescripcion.getText().trim() : "");
				objeModel.setEstado(cmbEstado.getSelectedItem().getValue().toString());
				objeModel.setCbmoduloconciliacionconfid(idSeleccionado);
				CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
				if (objDao.modificar(objeModel)) {
					onClick$btnLimpiar();
					llenaListbox(objDao.consultaGeneral("", "", "", "", "", "", "", ""));
					Messagebox.show("Datos modificados con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
				} else {
					Messagebox.show("No se pudo completar la operacion", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnModificar() -  Error ", e);
			//Logger.getLogger(CBParametrosGeneralesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	EventListener<Event> evtEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int idFila = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
			log.debug("evtEliminar()  " + " - ID parametro general a eliminar = " + idFila);
			//Logger.getLogger(CBParametrosGeneralesController.class.getName())
				//.log(Level.INFO, "ID parametro general a eliminar = " + idFila);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
								if(objDao.eliminar(idFila)){
									onClick$btnLimpiar();
									llenaListbox(objDao.consultaGeneral("", "", "", "", "", "", "", ""));
									Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,Messagebox.INFORMATION);
								}
							}
						}
					});
		}
	};
	
	public void onChanging$txtModulo(InputEvent evt){
		String value = evt.getValue().trim(); 
		txtModulo.setText(value.trim().toUpperCase().replace(" ", "_"));
	}
	
	public void onChanging$txtTipoObjeto(InputEvent evt){
		String value = evt.getValue().trim(); 
		txtTipoObjeto.setText(value.trim().toUpperCase().replace(" ", "_"));
	}
	
	
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
