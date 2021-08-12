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

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Component;

/**
 * @author lab
 * 
 */
public class CBCatalogoBancoNuevoController extends ControladorBase {

	/**
	 * modificado ovidio santos mvc 17042018
	 */
	Combobox cmbEstado;
	Combobox cmbTipo;
	private Textbox tbxNombre;
	Button btnConsultar;
	Button btnNuevo;
	Listbox lbxListaBanco;
	private static final long serialVersionUID = 1L;

	private String usuario;

	Window nBanco;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);
		usuario = obtenerUsuario().getUsuario();
		llenaComboTipo();
		llenaComboEstado();

	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
	}

	public void limpiarTextbox() {
		cmbEstado.setSelectedIndex(-1);
		cmbTipo.setSelectedIndex(-1);

	}

	public void onClick$btnGuardar() {
		CBCatalogoBancoModel objModel = new CBCatalogoBancoModel();
		CBCatalogoBancoDaoB bancoDao = new CBCatalogoBancoDaoB();

		objModel.setNombre(tbxNombre.getText().trim());
		objModel.setUsuario(obtenerUsuario().getUsuario());

		if (tbxNombre == null || "".equals(tbxNombre)) {

			Messagebox.show("El campo Nombre no puede ir vacío", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);

		} else if (bancoDao.consultaNombre(objModel)) {

			Messagebox.show("Ya a ingresado una entidad con el mismo nombre", "ATENCIÓN", Messagebox.OK,
					Messagebox.EXCLAMATION);

		} else if (cmbEstado.getSelectedItem() == null) {
			Messagebox.show("Debe seleccionar un estado", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);

		} else if (cmbTipo.getSelectedItem() == null) {

			Messagebox.show("Debe seleccionar un tipo de entidad", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);

		} else {

			objModel.setEstado(cmbEstado.getSelectedItem().getValue().toString());
			objModel.setTipoEstado(cmbTipo.getSelectedItem().getValue().toString());

			int res = 0;

			res = bancoDao.ingresaEntidadBancaria(objModel);

			/*
			 * res = bancoDao.ingresaEntidadBancaria(objModel);
			 */

			if (res != 0) {

				onClick$btnLimpiar();

				Messagebox.show("Agrupacion agregada con exito", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
				CBCatalogoBancoModel objModel1 = new CBCatalogoBancoModel();
				refrescarModulo(objModel1);
			} else {

				Messagebox.show("No se pudo completar la operacion", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			}

		}

	}

	public void onClick$closeBtn() {

		nBanco.detach();
	}

	public void refrescarModulo(CBCatalogoBancoModel objModel) {
		CBCatalogoBancoController instanciaPrincipal = new CBCatalogoBancoController();
		instanciaPrincipal = (CBCatalogoBancoController) session.getAttribute("interfaceTarjeta");

		instanciaPrincipal.recargaConsultaConta(objModel);

		onClick$closeBtn();

	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	private List<CBCatalogoOpcionModel> lstEstadoAgrupacion = new ArrayList<CBCatalogoOpcionModel>();

	public void llenaComboEstado() {
		System.out.println("Llena combo tipo estado");
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
		System.out.println("Llena combo tipo estado");
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
