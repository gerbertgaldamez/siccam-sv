/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.controller;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCatalogoBancoDaoB;
import com.terium.siccam.dao.CBCatalogoOpcionDaoB;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBCatalogoOpcionModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;

import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;

/**
 * @author lab
 * 
 */
public class CBCatalogoBancoModalModificarController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBCatalogoBancoModalModificarController.class);

	/**
	 * modificado ovidio santos mvc 17042018
	 */

	private static final long serialVersionUID = 1L;

	private Textbox tbxNombre;
	private Textbox tbxFechaCreacion;
	private Textbox tbxFechaModificacion;
	private Textbox tbxUsuarioCreador;
	private Textbox tbxUsuarioModifica;
	Combobox cmbEstado;
	Combobox cmbTipo;
	Button btnNuevo;
	Button btnGuardar;
	Button btnLimpiar;
	int idseleccionado = 0;
	Window aBanco;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	static Window nuevaAgenciaComercial;
	CBCatalogoBancoModel objModelModal = null;
	String usuario;
	static Window detalleBanco;
	static Window nuevoBanco;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		llenaComboTipo();
		llenaComboEstado();
		usuario = obtenerUsuario().getUsuario();
		objModelModal = (CBCatalogoBancoModel) misession.getAttribute("objModelModal");
		System.out.println("idseleccionado  en doafter" + objModelModal);
		// objModel.setCbtipologiaspolizaid(Integer.parseInt(session.getAttribute("idseleccionado").toString()));
		llenarcasillas();
	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
	}

	public void limpiarTextbox() {
		// idseleccionado = null;
		tbxNombre.setText("");

	}

	public void cerrarDetalle() {
		detalleBanco.detach();
	}

	public void cerrarNuevo() {
		nuevoBanco.detach();
	}

	public void onClick$closeBtn() {

		aBanco.detach();
	}

	public void onClick$btnGuardar() {
		int res = 0;
		{
			try {
				String user = obtenerUsuario().getUsuario();
				CBCatalogoBancoDaoB objDAO = new CBCatalogoBancoDaoB();
				CBCatalogoBancoModel objModel = new CBCatalogoBancoModel();
				objModel.setNombre(tbxNombre.getText().trim());
				objModel.setFechaCreacion(tbxFechaCreacion.getText().trim());
				objModel.setFechaModificacion(tbxFechaModificacion.getText().trim());
				objModel.setCreadoPor(tbxUsuarioCreador.getText().trim());
				objModel.setUsuario(user);
				if (cmbEstado.getSelectedItem() != null) {
					objModel.setEstado(cmbEstado.getSelectedItem().getValue().toString());

				}
				if (cmbTipo.getSelectedItem() != null) {
					objModel.setTipoEstado(cmbTipo.getSelectedItem().getValue().toString());

				}
				objModel.setCbcatalogobancoid(objModelModal.getCbcatalogobancoid());				

				res = objDAO.actualizaBanco(objModel);

				if (res != 0) {
					Messagebox.show("Agrupacion modificada con exito", "ATENCIÓN", Messagebox.OK,
							Messagebox.INFORMATION);

					CBCatalogoBancoModel objModel1 = new CBCatalogoBancoModel();
					refrescarModulo(objModel1);

				} else {
					Messagebox.show("No se pudo completar la operacion", "ATENCIÓN", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}

			} catch (Exception e) {
				log.error(
						"onClick$btnGuardar() - " + "valores null");
			//	Logger.getLogger(CBCatalogoBancoModalModificarController.class.getName()).log(Level.INFO,
						//"valores null ");
			}

		}
	}

	public void refrescarModulo(CBCatalogoBancoModel objModel) {
		CBCatalogoBancoController instanciaPrincipal = new CBCatalogoBancoController();
		instanciaPrincipal = (CBCatalogoBancoController) session.getAttribute("interfaceTarjeta");

		instanciaPrincipal.recargaConsultaConta(objModel);

		onClick$closeBtn();

	}

	public void llenarcasillas() {
		log.debug(
				"llenarcasillas() - " + "id seleccioando " + idseleccionado);
		
		// CBMantenimientoPolizaModel objModel = new CBMantenimientoPolizaModel();
		// idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");

		System.out.println("id seleccioando " + idseleccionado);
		// String nombrepoliza = objmodificar.getNombre();

		tbxNombre.setText(objModelModal.getNombre());
		tbxFechaCreacion.setText(objModelModal.getFechaCreacion());
		tbxFechaModificacion.setText(objModelModal.getFechaModificacion());
		tbxUsuarioCreador.setText(objModelModal.getCreadoPor());
		tbxUsuarioModifica.setText(objModelModal.getModificadoPor());

		System.out.println("combotipo abtes de if " + objModelModal.getTipoEstado());
		for (Comboitem item : cmbTipo.getItems()) {
			log.debug(
					"llenarcasillas() - " + "for en modificar " + item.getLabel());
			
			if (item.getLabel().equals(String.valueOf(objModelModal.getTipoEstado()))) {
				// System.out.println("combo tipo en if " + objmodificar.getTipo());
				System.out.println("for en modificar " + item.getLabel());
				System.out.println("combotipo abtes de if " + objModelModal.getTipoEstado());
				cmbTipo.setSelectedItem(item);
			}

		}

		for (Comboitem item : cmbEstado.getItems()) {
			// System.out.println("for en modificar " + item.getValue());
			// System.out.println("comboESTADO abtes de if " +
			// objmodificar.getPide_Entidad());
			if (item.getLabel().equals(String.valueOf(objModelModal.getEstado()))) {
				// System.out.println("combo ESTADO en if " + objmodificar.getPide_Entidad());
				cmbEstado.setSelectedItem(item);
			}
		}

	}

	private List<CBCatalogoOpcionModel> lstEstadoAgrupacion = new ArrayList<CBCatalogoOpcionModel>();

	public void llenaComboEstado() {
		log.debug(
				"llenaComboEstado() - " + "Llena combo tipo estado");
		
		// limpiaCombobox(cmbTipo);

		CBCatalogoOpcionDaoB objeDAO = new CBCatalogoOpcionDaoB();
		this.lstEstadoAgrupacion = objeDAO.obtieneListaOpcion();
		if (lstEstadoAgrupacion.size() > 0) {
			for (CBCatalogoOpcionModel d : this.lstEstadoAgrupacion) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbEstado);
				item.setLabel(d.getNombre());
				item.setValue(d.getValor());
			}
		}

	}

	private List<CBParametrosGeneralesModel> lstTipo = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboTipo() {
		log.debug(
				"llenaComboTipo() - " + "Llena combo tipo");
		
		// limpiaCombobox(cmbTipo);

		CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
		this.lstTipo = objeDAO.obtenerListaTipoAgrupacion(CBParametrosGeneralesDAO.S_OBTENER_TIPO_AGRUPACIONES);
		if (lstTipo.size() > 0) {
			for (CBParametrosGeneralesModel d : this.lstTipo) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbTipo);
				item.setLabel(d.getValorObjeto1());
				item.setValue(d.getValorObjeto1());
			}
		}

	}

}
