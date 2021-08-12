package com.terium.siccam.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBRecaReguDAO;
import com.terium.siccam.implement.CBRecargaListboxRecaReguImpl;
import com.terium.siccam.model.CBRecaReguModel;

public class CBRecaReguController extends ControladorBase implements CBRecargaListboxRecaReguImpl{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4285297288749865925L;
	
	/**
	 * ZK
	 * */
	private Datebox dbxFechaini;
	private Datebox dbxFechafin;
	private Listbox lbxPagos;
	private Textbox tbxCliente;
	private Textbox tbxUsuario;

	public void doAfterCompose(Component param) {
		try {
			super.doAfterCompose(param);
		} catch (Exception e) {
			Logger.getLogger(CBRecaReguController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * 
	 * */
	public void llenaListbox(CBRecaReguModel objModel, int banderaMensaje) {
		CBRecaReguDAO objDao = new CBRecaReguDAO();
		//int limiteInicial = 1;
		//int limiteFinal = 100;
		List<CBRecaReguModel> lst = objDao.obtenerPagos(objModel /*, limiteInicial, limiteFinal*/);
		if(lst.size() > 0) {
			limpiarListbox(lbxPagos);
			Iterator<CBRecaReguModel> it = lst.iterator();
			CBRecaReguModel obj = null;
			Listcell cell = null;
			Listitem fila = null;
			while(it.hasNext()) {
				obj = it.next();
				
				fila = new Listitem();

				cell = new Listcell();
				cell.setLabel(obj.getFechaEfectiva()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getFechaPago());
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getCodCliente()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNomCliente()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNumSecuenci()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getImpPago().toString()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getTransaccion()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getTipoTransaccion()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getCodOficina()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getDesOficina()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getTipoMovCaja()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getDesMovCaja()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getTipoValor()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getDesTipoValor()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNomusuarora()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getCodBanco()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNombreBanco()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getCodCaja()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getDesCaja()); 
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getObservacion()); 
				cell.setParent(fila);
				
				fila.setValue(obj);
				fila.setParent(lbxPagos);
			}
		} else {
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, 
					"Bandera mensaje = " + banderaMensaje);
			if(banderaMensaje == 0) { //Ejecucion de metodo desde pantalla principal
				Messagebox.show("No existen registros en el sistema comercial para los filtros aplicados", 
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} 
		}
	}
	
	public void onClick$btnBuscar() {
		realizaBusqueda(0);
	}
	
	/**
	 * Agregado por CarlosGodinez -> 09/08/2018
	 * Se crea este metodo para uso de interfaz y refrescar busqueda
	 * despues de modificar usuario en modal
	 * */
	public void realizaBusqueda(int banderaMensaje) {
		try {
			limpiarListbox(lbxPagos);
			DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
			if(((tbxUsuario.getValue() == null && "".equals(tbxUsuario.getText())) || (tbxCliente.getValue() == null && "".equals(tbxCliente.getText())))) {
				Messagebox.show("Debe ingresar un usuario o un codigo de cliente para poder realizar las consulta", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if(dbxFechaini.getValue() == null) {
				Messagebox.show("Debe seleccionar una fecha inicio para consultar", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if(dbxFechafin.getValue() == null) {
				Messagebox.show("Debe seleccionar una fecha fin para consultar", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if (fechaFormato.parse(fechaFormato.format(dbxFechaini.getValue()))
					.after(fechaFormato.parse(fechaFormato.format(dbxFechafin.getValue())))) {
				Messagebox.show("La fecha inicial no puede ser mayor a la final", "ATENCIÓN", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else {
				String codcliente = "";
				if(tbxCliente.getValue() == null || "".equals(tbxCliente.getText())) {
					codcliente = "";
				} else {
					codcliente = tbxCliente.getText().trim();
				}
				
				String usuario = "";
				if(tbxUsuario.getValue() == null || "".equals(tbxUsuario.getText())) {
					usuario = "";
				} else {
					usuario = tbxUsuario.getText().toUpperCase().trim();
				}
				
				CBRecaReguModel objModel = new CBRecaReguModel();
				objModel.setFechainicio(dbxFechaini.getText());
				objModel.setFechafin(dbxFechafin.getText());
				objModel.setNomusuarora(usuario);
				objModel.setCodCliente(codcliente);
				llenaListbox(objModel, banderaMensaje);
			}
		} catch(Exception e) {
			Logger.getLogger(CBRecaReguController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void onClick$btnModificarUsuario() {
		try {
			if(lbxPagos.getSelectedItem() == null) {
				Messagebox.show("Debe seleccionar un registro para poder modificar el USUARIO", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else {
				CBRecaReguModel obj = lbxPagos.getSelectedItem().getValue();
				session.setAttribute("cbpagosid", obj.getCbpagosid());
				session.setAttribute("ifcRecaRegu", CBRecaReguController.this);
				Executions.createComponents("/cbmodalrecaudacionusuario.zul", null, null);
			}
		} catch(Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBRecaReguController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * 
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
