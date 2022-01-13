package com.terium.siccam.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;

/**
 * @author Carlos Godinez - 16/10/2017
 * */
public class CBEntidadModalController extends ControladorBase  {
	private static Logger log = Logger.getLogger(CBEntidadModalController.class);
	private static final long serialVersionUID = 9176164927878418930L;
	
	//Componentes
	Combobox cmbAgrupacion;
	Textbox txtNombre;
	Textbox txtTelefono;
	Textbox txtDireccion;
	Textbox txtCuentaContable;
	Combobox cmbEstado;
	Textbox txtMoneda;
	Textbox txtCodigoColector;
	Textbox txtNit;
	
	Button btnGuardar;
	Button btnModificar;
	
	//Arraylist para llenado de combobox y listbox
	private List<CBCatalogoBancoModel> listaAgrupacionesCmb;
	private List<CBParametrosGeneralesModel> listaEstadoCmb;
	
	//Propiedades
	private String usuario;
	Window entidadModal;
	CBCatalogoAgenciaModel objSeleccionado;
	
	public void doAfterCompose(Component param) {
		try{
			super.doAfterCompose(param);
			log.debug(
					"doAfterCompose() - " + "\n*** Entra a modal de entidad ***\n");
			//Logger.getLogger(CBEntidadModalController.class.getName())
				//.log(Level.INFO, "\n*** Entra a modal de entidad ***\n");
			llenaComboAgrupaciones();
			llenaComboEstado();
			if(session.getAttribute("objEntidadSelected") != null) {
				log.debug(
						"doAfterCompose() - " + "** Objeto entidad modificable enviado a controlador");
				//Logger.getLogger(CBEntidadModalController.class.getName())
					//.log(Level.INFO, "** Objeto entidad modificable enviado a controlador");
				btnGuardar.setDisabled(true);
				btnModificar.setDisabled(false);
				objSeleccionado = (CBCatalogoAgenciaModel) session.getAttribute("objEntidadSelected");
				for (Comboitem item : cmbAgrupacion.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getcBCatalogoBancoId()))) {
						cmbAgrupacion.setSelectedItem(item);
					}
				}
				txtNombre.setText(objSeleccionado.getNombre());
				txtTelefono.setText(objSeleccionado.getTelefono());
				txtDireccion.setText(objSeleccionado.getDireccion());
				txtCuentaContable.setText(objSeleccionado.getCuentaContable());
				txtMoneda.setText(objSeleccionado.getMoneda());
				txtCodigoColector.setText(objSeleccionado.getCodigoColector());
				txtNit.setText(objSeleccionado.getNit());
				for (Comboitem item : cmbEstado.getItems()) {
					if (item.getValue().toString().equals(String.valueOf(objSeleccionado.getEstado()))) {
						cmbEstado.setSelectedItem(item);
					}
				}
			} else {
				log.debug(
						"doAfterCompose() - " + "** Ventana lista para registrar entidad");
				//Logger.getLogger(CBEntidadModalController.class.getName())
					//.log(Level.INFO, "** Ventana lista para registrar entidad");
				btnGuardar.setDisabled(false);
				btnModificar.setDisabled(true);
				objSeleccionado = new CBCatalogoAgenciaModel();
				objSeleccionado.setNombre("");
			}
			usuario = obtenerUsuario().getUsuario();
		} catch (Exception e) {
			log.error("doAfterCompose() - Error ", e);
			//Logger.getLogger(CBEntidadModalController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Guardar entidad
	 * */
	public void onClick$btnGuardar() {
		try{
			if(entidadValida()) { 
				CBCatalogoAgenciaDAO objDAO = new CBCatalogoAgenciaDAO();
				CBCatalogoAgenciaModel objEntidad = new CBCatalogoAgenciaModel();
				objEntidad.setcBCatalogoBancoId(cmbAgrupacion.getSelectedItem().getValue().toString());
				objEntidad.setNombre(txtNombre.getValue().toString().trim());
				objEntidad.setTelefono(txtTelefono.getValue().toString().trim());
				objEntidad.setDireccion(txtDireccion.getValue().toString().trim());
				objEntidad.setEstado(cmbEstado.getSelectedItem().getValue().toString());
				objEntidad.setCreadoPor(usuario);
				objEntidad.setCuentaContable(txtCuentaContable.getValue().toString().trim());
				objEntidad.setMoneda(txtMoneda.getValue().toString().trim());
				objEntidad.setCodigoColector(txtCodigoColector.getValue().toString().trim());
				objEntidad.setNit(txtNit.getValue().toString().trim());
				
				int resultado = objDAO.ingresaNuevaAgencia(objEntidad);
				
				if (resultado > 0) {
					Messagebox.show("Entidad registrada con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					CBConsultaEntidadesController instanciaPrincipal = new CBConsultaEntidadesController();
					instanciaPrincipal =(CBConsultaEntidadesController) session.getAttribute("ifcEntidades");
					instanciaPrincipal.recargaListbox();
					entidadModal.onClose();
				} else {
					Messagebox.show("El codigo de colector, no se puede repetir", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			log.error("onClick$btnGuardar() - Error ", e);
			//Logger.getLogger(CBEntidadModalController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Modificar entidad
	 * */
	public void onClick$btnModificar() {
		try{
			if(entidadValida()) { 
				CBCatalogoAgenciaDAO objDAO = new CBCatalogoAgenciaDAO();
				CBCatalogoAgenciaModel objEntidad = new CBCatalogoAgenciaModel();
				objEntidad.setcBCatalogoBancoId(cmbAgrupacion.getSelectedItem().getValue().toString());
				objEntidad.setNombre(txtNombre.getValue().toString().trim());
				objEntidad.setTelefono(txtTelefono.getValue().toString().trim());
				objEntidad.setDireccion(txtDireccion.getValue().toString().trim());
				objEntidad.setCuentaContable(txtCuentaContable.getValue().toString().trim());
				objEntidad.setMoneda(txtMoneda.getValue().toString().trim());
				objEntidad.setCodigoColector(txtCodigoColector.getValue().toString().trim());
				objEntidad.setNit(txtNit.getValue().toString().trim());
				objEntidad.setEstado(cmbEstado.getSelectedItem().getValue().toString());
				objEntidad.setModificadoPor(usuario);
				objEntidad.setcBCatalogoAgenciaId(objSeleccionado.getcBCatalogoAgenciaId());
				int resultado = objDAO.actualizaAgencia(objEntidad);
				if(resultado > 0) {
					if(objDAO.updateAsociaciones(objEntidad)) {
						Messagebox.show("Entidad modificada con exito", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
						CBConsultaEntidadesController instanciaPrincipal = new CBConsultaEntidadesController();
						instanciaPrincipal =(CBConsultaEntidadesController) session.getAttribute("ifcEntidades");
						instanciaPrincipal.recargaListbox();
						entidadModal.onClose();
					} 
				} else {
					Messagebox.show("El codigo de colector, no se puede repetir", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			log.error("onClick$btnGuardar() - Error ", e);
			//Logger.getLogger(CBEntidadModalController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	/**
	 * Metodo para validar 
	 * */
	public boolean entidadValida() {
		boolean resultado = false;
		try {
			CBCatalogoAgenciaDAO objDAO = new CBCatalogoAgenciaDAO();
			if(txtNombre.getValue() == null || txtNombre.getValue().toString().trim().equals("")) {
				Messagebox.show("El nombre de la entidad no puede ir vacio.", "ATENCIÓN", 
						Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbAgrupacion.getSelectedItem() == null || cmbAgrupacion.getSelectedItem().toString().trim().equals("")) {
				Messagebox.show("Se debe seleccionar una agrupacion.", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbEstado.getSelectedItem() == null || cmbEstado.getSelectedItem().toString().trim().equals("")) {
				Messagebox.show("Debe ingresar un estado para la configuracion de confronta.", "ATENCION", 
						Messagebox.OK,Messagebox.EXCLAMATION);
			} else if (txtCodigoColector.getValue() == null || txtCodigoColector.getValue().toString().trim().equals("")) {
				Messagebox.show("El codigo colector no puede ir vacio.", "ATENCIÓN", 
						Messagebox.OK, Messagebox.EXCLAMATION);
			}else if (txtNit.getValue() == null || txtNit.getValue().toString().trim().equals("")) {
				Messagebox.show("El NIT no puede ir vacio.", "ATENCIÓN", 
						Messagebox.OK, Messagebox.EXCLAMATION);
			}else {
				if (objDAO.consultaNombre(txtNombre.getValue().toString().trim().toUpperCase()) && 
						!txtNombre.getValue().toString().trim().toUpperCase()
							.equals(objSeleccionado.getNombre().trim().toUpperCase())) {
					Messagebox.show("El nombre de la entidad ingresada ya existe.", "ATENCIÓN", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else {
					resultado = true; //Registro de entidad valido
				}
			}
		} catch (Exception e) {
			log.error("entidadValida() - Error ", e);
			//Logger.getLogger(CBEntidadModalController.class.getName()).log(Level.SEVERE, null, e);
		}
		return resultado;
	}
	
	/**
	 * Metodos para llenado de Combobox al cargar vista
	 * */
	
	@SuppressWarnings("unchecked")
	public void llenaComboAgrupaciones(){
		try {
			listaAgrupacionesCmb = (ArrayList<CBCatalogoBancoModel>) session.getAttribute("cmbListAgrupaciones");
			for (CBCatalogoBancoModel d : listaAgrupacionesCmb) {
				Comboitem item = new Comboitem();
				item.setParent(cmbAgrupacion);
				item.setValue(d.getCbcatalogobancoid());
				item.setLabel(d.getNombre());
			} 
			log.debug(
					"llenaComboAgrupaciones() - " + "Llena combo de agrupaciones");
			//Logger.getLogger(CBEntidadModalController.class.getName())
				//.log(Level.INFO, "- Llena combo de agrupaciones");
		} catch (Exception e) {
			log.error("llenaComboAgrupaciones() - Error ", e);
			//Logger.getLogger(CBEntidadModalController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void llenaComboEstado(){
		try {
			listaEstadoCmb = (ArrayList<CBParametrosGeneralesModel>) session.getAttribute("cmbListEstado");;
			for (CBParametrosGeneralesModel d : listaEstadoCmb) {
				Comboitem item = new Comboitem();
				item.setParent(cmbEstado);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			} 
			log.debug(
					"llenaComboEstado() - " + "Llena combo de estado");
			//Logger.getLogger(CBEntidadModalController.class.getName())
				//.log(Level.INFO, "- Llena combo de estado");
		} catch (Exception e) {
			log.error("llenaComboEstado() - Error ", e);
			//Logger.getLogger(CBEntidadModalController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}
