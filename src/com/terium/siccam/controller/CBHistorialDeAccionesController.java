package com.terium.siccam.controller;


import java.util.Iterator;

import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBHistorialAccionDAO;
import com.terium.siccam.model.CBConciliacionDetallada;
import com.terium.siccam.model.CBHistorialAccionModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;

public class CBHistorialDeAccionesController extends ControladorBase {
	private static Logger log = Logger.getLogger(CBHistorialDeAccionesController.class);

	/**
	 * @author Freddy Ayala to terium.com
	 */
	private static final long serialVersionUID = 1L;
	
	//Variables ZUL
	Listbox lbxHistorialAcciones;
	Textbox txtAccion;
	Textbox txtObservaciones;
	Textbox txtMonto;
	Button btnAgregar;
	Button btnGuardar;
	Button btnEliminar;
	
	//Instancias
	CBConciliacionDetallada conciliacionDetalle;
	
	//Instancias DAO
	CBHistorialAccionDAO historialDao = new CBHistorialAccionDAO();
	
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		conciliacionDetalle = (CBConciliacionDetallada) session.getAttribute("conciliacionDetallada");
		listarHistorialDeAcciones();
	}
	
	public void listarHistorialDeAcciones() {

		List<CBHistorialAccionModel> lst = null;
		String idConciliacion = null;
		try {
			idConciliacion = conciliacionDetalle.getConciliacionId();
			lst = historialDao.obtenerCBHistorialAcciones(idConciliacion);
			Iterator<CBHistorialAccionModel> ilst = lst.iterator();
			try {
				if (lst != null && lst.size() > 0) {

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
						fila.setParent(lbxHistorialAcciones);

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				log.error("listarHistorialDeAcciones() - Error ", e);
				//Logger.getLogger(CBHistorialDeAccionesController.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("listarHistorialDeAcciones() - Error ", e);
			//Logger.getLogger(CBHistorialDeAccionesController.class.getName()).log(Level.SEVERE, null, e);
		}

	}
	
	public void onClick$lbxHistorialAcciones(){
		if(lbxHistorialAcciones.getSelectedItem() != null){
			CBHistorialAccionModel cbHistorial = lbxHistorialAcciones.getSelectedItem().getValue();
			txtAccion.setText(cbHistorial.getAccion());
			txtMonto.setText(cbHistorial.getMonto());
			txtObservaciones.setText(cbHistorial.getObservaciones());
		}
	}
	
	// MANTENIMIENTOS
		// AGREGAR
		public void onClick$btnAgregar() {
			limpiarCampos();
		}

		// GUARDAR
		public void onClick$btnGuardar() {
			CBHistorialAccionModel cbHistorial = null;
			String idPadre =null;
			try {
				idPadre = conciliacionDetalle.getConciliacionId();
				if (lbxHistorialAcciones.getSelectedItem() != null) {
					// ACTUALIZAR
					cbHistorial = lbxHistorialAcciones.getSelectedItem().getValue();
					cbHistorial.setAccion(txtAccion.getText());
					cbHistorial.setMonto(txtMonto.getText());
					cbHistorial.setObservaciones(txtObservaciones.getText());
					cbHistorial.setModificadoPor(obtenerUsuario().getUsuario());
					historialDao.updateReg(cbHistorial, idPadre);

				} else {
					// INSERTAR
					cbHistorial = new CBHistorialAccionModel();
					cbHistorial.setAccion(txtAccion.getText());
					cbHistorial.setMonto(txtMonto.getText());
					cbHistorial.setObservaciones(txtObservaciones.getText());
					cbHistorial.setCreadoPor(obtenerUsuario().getUsuario());	
					historialDao.insertarReg(cbHistorial,idPadre);

				}
				lbxHistorialAcciones.getItems().clear();
				listarHistorialDeAcciones();
				limpiarCampos();			
			} catch (Exception e) {
				log.error("onClick$btnGuardar() - Error ", e);
				//Logger.getLogger(CBHistorialDeAccionesController.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		// ELIMINAR
		public void onClick$btnEliminar() {
			CBHistorialAccionModel cbHistorial = null;
			String idPadre =null;
			
			try {
				idPadre = conciliacionDetalle.getConciliacionId();
				if (lbxHistorialAcciones.getSelectedItem() != null) {
					// ELIMINAR
					cbHistorial = lbxHistorialAcciones.getSelectedItem().getValue();
					historialDao.deleteReg(cbHistorial, idPadre);
				}
				lbxHistorialAcciones.getItems().clear();
				listarHistorialDeAcciones();
				limpiarCampos();
			} catch (Exception e) {
				log.error("onClick$btnEliminar() - Error ", e);
				//Logger.getLogger(CBHistorialDeAccionesController.class.getName()).log(Level.SEVERE, null, e);
			}

		}

		private void limpiarCampos() {
			txtAccion.setText("");
			txtMonto.setText("");
			txtObservaciones.setText("");
		}

}
