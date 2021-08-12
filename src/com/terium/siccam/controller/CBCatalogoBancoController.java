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
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

/**
 * @author lab
 * 
 */
public class CBCatalogoBancoController extends ControladorBase {

	/**
	 * modificado ovidio santos mvc 17042018
	 */
	
	boolean banderaEntidadSelected ;
	Combobox cmbEstado;
	Combobox cmbTipo;
	private Textbox tbxNombre;
	Button btnConsultar;
	Button btnNuevo;
	Listbox lbxListaBanco;
	private static final long serialVersionUID = 1L;

	static Window detalleBanco;
	static Window nuevoBanco;
	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);
	
		llenaComboTipo();
		llenaComboEstado();
		onClick$btnConsultar();

	}

	public void onClick$btnNuevo() {

		Window agrupacionNuevaModal = (Window) Executions.createComponents("/cbBanco.zul", null, null);
		agrupacionNuevaModal.doModal();

		session.setAttribute("interfaceTarjeta", CBCatalogoBancoController.this);

	}
	
	public void onClick$btnConsultar() {

		try {
			CBCatalogoBancoDaoB cbaDao = new CBCatalogoBancoDaoB();
			CBCatalogoBancoModel objModel = new CBCatalogoBancoModel();
			System.out.println("Consulta estados tipologias");

			if (tbxNombre.getText().trim() != null && !"".equals(tbxNombre.getText().trim())) {
				objModel.setNombre(tbxNombre.getText().trim());
			}

			// System.out.println("combo estado " + cmbEstado.getSelectedItem());
			if (cmbEstado.getSelectedItem() != null) {
				objModel.setEstado(cmbEstado.getSelectedItem().getValue().toString());

			}
			if (cmbTipo.getSelectedItem() != null) {
				objModel.setTipoEstado(cmbTipo.getSelectedItem().getValue().toString());

			}
			// limpiarTextbox();
			llenaListbox(cbaDao.obtieneListaBancoMantenimiento(objModel));
			if (this.lbxListaBanco.getItemCount() != 0) {

			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	CBCatalogoBancoModel objModel = new CBCatalogoBancoModel();

	// metodo para llenar listbox
	public void llenaListbox(List<CBCatalogoBancoModel> list) {
		limpiarListbox(lbxListaBanco);

		System.out.println("cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			Iterator<CBCatalogoBancoModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;

			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getCbcatalogobancoid());
				// System.out.println("causa lista " + objModel.getTipoCausa());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombre());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getEstado());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTipoEstado());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete_1.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbcatalogobancoid());
				btnDelete.addEventListener("onClick", eventBtnEliminar);

				// item para btn modificar
				item.setAttribute("objmodificar", objModel);

				item.setAttribute("idseleccionado", objModel.getCbcatalogobancoid());
				// System.out.println("en el llena listbox " +
				// objModel.getIdCausaConciliacion());
				item.addEventListener("onDoubleClick", eventBtnModificar);

				item.setValue(objModel);

				item.setTooltip("popAsociacionModal");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxListaBanco);
			}
			list.clear();
		} else {

		}

	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
	}

	public void limpiarTextbox() {
		// idseleccionado = null;
		tbxNombre.setText("");

	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id

	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final String idseleccionado = (String) (event.getTarget().getAttribute("idEliminar"));
			System.out.println("ID  a eliminar = " + idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBCatalogoBancoDaoB objDAO = new CBCatalogoBancoDaoB();
								objDAO.eliminaRegistro(idseleccionado);
								onClick$btnLimpiar();
								CBCatalogoBancoModel objModel = new CBCatalogoBancoModel();
								limpiarTextbox();
								llenaListbox(objDAO.obtieneListaBancoMantenimiento(objModel));
								Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
										Messagebox.INFORMATION);

							}
						}
					});
			onClick$btnLimpiar();
		}
	};

	/**
	 * Evento que se dispara al seleccionar un registro de entidad de la Listbox
	 * principal Seteo de valores en componentes de modal
	 */
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				banderaEntidadSelected = true;
				CBCatalogoBancoModel objModelModal = (CBCatalogoBancoModel) event.getTarget()
						.getAttribute("objmodificar");
				System.out.println("\n**** Entidad seleccionada ****\n");
				session.setAttribute("objModelModal", objModelModal);
				session.setAttribute("interfaceTarjeta", CBCatalogoBancoController.this);

				Window agrupacionModal = (Window) Executions.createComponents("/cbActualizaBanco.zul", null, null);
				agrupacionModal.doModal();

			} catch (Exception e) {
				Logger.getLogger(CBCatalogoBancoController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};

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

	public void recargaConsultaConta(CBCatalogoBancoModel objModel) {
		System.out.println("Entra a recargar consulta...");

		CBCatalogoBancoDaoB objDao = new CBCatalogoBancoDaoB();

		llenaListbox(objDao.obtieneListaBancoMantenimiento(objModel));
		onClick$btnLimpiar();

	}

}
