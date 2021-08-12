package com.terium.siccam.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBRecaReguDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBRecaReguModel;
import com.terium.siccam.utils.Constantes;

/**
 * @author CarlosGodinez -> 09/08/2018
 * */
public class CBRecaudacionUsuarioController extends ControladorBase{
	
	private static final long serialVersionUID = 4285297288749865925L;
	
	public void doAfterCompose(Component param) {
		try{
			super.doAfterCompose(param);
			llenaComboEntidad();
			cbpagosid = Integer.parseInt(session.getAttribute("cbpagosid").toString());
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * ZK
	 */
	Window usuarioModal;
	private Combobox cmbEntidad;
	private Combobox cmbUsuario;
	
	/**
	 * 
	 * */
	private int cbpagosid;
	
	/**
	 * Listas para llenar combobox
	 */
	private List<CBCatalogoAgenciaModel> listaEntidad = new ArrayList<CBCatalogoAgenciaModel>();
	private List<CBRecaReguModel> listaUsuarios = new ArrayList<CBRecaReguModel>();
	
	public void llenaComboEntidad() {
		try {
			CBRecaReguDAO objeDAO = new CBRecaReguDAO();
			listaEntidad = objeDAO.obtenerEntidades();
			for (CBCatalogoAgenciaModel d : listaEntidad) {
				Comboitem item = new Comboitem();
				item.setParent(cmbEntidad);
				item.setValue(d.getcBCatalogoAgenciaId());
				item.setLabel(d.getNombre());
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// Metodo que se invoca al seleccionar una entidad
	public void onSelect$cmbEntidad()  {
		try {
			int entidadSeleccionada = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.INFO, 
					"Entidad seleccionada= " + entidadSeleccionada);
			cleanCombo(cmbUsuario);
			llenaComboUsuarios(entidadSeleccionada);
			if(listaUsuarios.isEmpty()) {
				cmbUsuario.setText("");
			} else {
				cmbUsuario.setSelectedIndex(0);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void llenaComboUsuarios(int entidadSeleccionada) {
		try {
			CBRecaReguDAO objeDAO = new CBRecaReguDAO();
			if(entidadSeleccionada == 0) { 
				Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.INFO, 
						"Manejo de BANCOS en combo de ENTIDADES");
				listaUsuarios = objeDAO.obtenerBancos();
				for (CBRecaReguModel d : listaUsuarios) {
					Comboitem item = new Comboitem();
					item.setParent(cmbUsuario);
					item.setValue(d.getCodAgencia());
					item.setLabel(d.getNombreBanco());
				}
			} else {
				Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.INFO, 
						"Manejo de TIENDAS PROPIAS en combo de ENTIDADES");
				listaUsuarios = objeDAO.obtenerUsuarios(entidadSeleccionada);
				for (CBRecaReguModel d : listaUsuarios) {
					Comboitem item = new Comboitem();
					item.setParent(cmbUsuario);
					item.setValue(d.getNomusuarora());
					item.setLabel(d.getNomusuarora());
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Modificacion de usuario
	 * */
	public void onClick$btnModificarUsuario() {
		int operacion = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
		if(operacion == 0) { 
			/**
			 * MANEJO DE BANCOS
			 */
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.INFO, 
					"Se modificara COD_CAJA y COD_OFICINA en tabla CB_PAGOS");
			modificacionCajero(0);
		} else { 
			/**
			 * MANEJO DE TIENDAS PROPIAS
			 */
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.INFO, 
					"Se modificara NOM_USUARORA en tabla CB_PAGOS");
			modificacionCajero(1);
		}
	}
	
	public void modificacionCajero(int banderaOperacion) {
		try {
			if (cmbUsuario.getSelectedItem() == null || "".equals(cmbUsuario.getSelectedItem().getValue().toString())) {
				Messagebox.show("No se ha seleccionado un cajero para modificar.", Constantes.ADVERTENCIA,
						Messagebox.OK, Messagebox.EXCLAMATION);
			} else {
				CBRecaReguDAO objeDAO = new CBRecaReguDAO();
				if (objeDAO.actualizaPagos(banderaOperacion, cmbUsuario.getSelectedItem().getValue().toString(), cbpagosid)) {
					Messagebox.show("Usuario modificado con éxito, id de registro de pago modificado = " + cbpagosid,
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.INFO,
							"Actualiza el pago de forma correcta id: " + cbpagosid);
					CBRecaReguController instanciaPrincipal = new CBRecaReguController();
					instanciaPrincipal = (CBRecaReguController) session.getAttribute("ifcRecaRegu");
					instanciaPrincipal.realizaBusqueda(1);
					usuarioModal.onClose();
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void cleanCombo(Combobox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}
}
