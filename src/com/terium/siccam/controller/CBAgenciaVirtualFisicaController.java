package com.terium.siccam.controller;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBAgenciaVirFiscDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBAgenciaVirtualFisicaModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.Constantes;

public class CBAgenciaVirtualFisicaController extends ControladorBase {

	/**
	 * modifica ovidio santos 20042018
	 */
	private static final long serialVersionUID = 1L;

	Listbox lbxagenciasListado;
	int idseleccionado = 0;
	private Combobox cmbTipo;
	Button btnModificar;
	Button btnRegistrar;
	Button btnLimpiar;
	Button btnConsultar;
	
	Textbox tbxCodigoAgencia;
	Textbox tbxNombreAgencia;
	int idagencia;
	String nombreBanco;
	private Label lblAgrupacion;
	private Label lblNombreAgencia;

	CBAgenciaVirFiscDAO objeDAO = new CBAgenciaVirFiscDAO();
	Execution exec = Executions.getCurrent();
	CBCatalogoBancoController mc = (CBCatalogoBancoController) exec.getArg().get("MenuController");

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		nombreBanco = ((String) session.getAttribute("nombreBanco"));
		
		idagencia = (Integer.parseInt(session.getAttribute("idAgencia").toString()));
		Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,"nombre banco ", nombreBanco);
		Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,"nombre agencia ", session.getAttribute("nombreAgencia"));	

		lblAgrupacion.setValue(nombreBanco);
		lblNombreAgencia.setValue((String) session.getAttribute("nombreAgencia"));
		llenaComboTipo();
		onClick$btnConsultar();

	}

	/**
	 * Combo tipoo
	 */
	public void llenaComboTipo() {
		CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
		List<CBParametrosGeneralesModel> listParamTipo = objDao.obtenerListaTipoAgrupacion(CBParametrosGeneralesDAO.S_OBTENER_AGENCIA_VF);
		if (listParamTipo.isEmpty()) {
			Messagebox.show("Error al cargar la configuracion de cuentas", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else {			
			Iterator<CBParametrosGeneralesModel> it = listParamTipo.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto2());
				item.setParent(cmbTipo);
			}
		}
	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	public void onClick$btnConsultar() throws SQLException, NamingException {
		CBAgenciaVirtualFisicaModel objModel = new CBAgenciaVirtualFisicaModel();
		objModel.setIdAgencia(objModel.getIdAgencia());
		Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
				"Entra a consulta estados de afiliaciones");

		llenaListbox(objeDAO.listadoAgencias(idagencia));
		limpiarCampos();

	}

	public void limpiarCampos() {
		tbxCodigoAgencia.setText("");
		cmbTipo.setSelectedIndex(-1);
		tbxNombreAgencia.setText("");
	}

	public void llenaListbox(List<CBAgenciaVirtualFisicaModel> list) {
		limpiarListbox(lbxagenciasListado);
		CBAgenciaVirtualFisicaModel objModel = null;

		Logger.getLogger(CBAgenciaVirtualFisicaController.class.getName()).log(Level.INFO,
				"cantidad de registros ", list.size());
		if (list.isEmpty()) {
			//Messagebox.show("No se encontraron resultados!", Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
		} else {		
			Iterator<CBAgenciaVirtualFisicaModel> it = list.iterator();
			Listitem item = null;
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getCodigo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombre());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTipo());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbAgenciaVirfiscid());
				btnDelete.addEventListener("onClick", eventBtnEliminar);

				cell.setParent(item);
				item.setAttribute("objmodificar", objModel);
				item.setAttribute("idseleccionado", objModel.getCbAgenciaVirfiscid());
				item.addEventListener("onClick", eventBtnModificar);

				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxagenciasListado);			
			}
		}
	}

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id
	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int id = (Integer) (event.getTarget().getAttribute("idEliminar"));
			Logger.getLogger(CBAgenciaVirtualFisicaController.class.getName()).log(Level.INFO,
					"*** id seleccionado eliminar ***", idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								objeDAO.eliminarAgencia(id);
								onClick$btnLimpiar();
								llenaListbox(objeDAO.listadoAgencias(idagencia));
								Messagebox.show("Registros eliminado con exito.", Constantes.ATENCION, Messagebox.OK,
										Messagebox.INFORMATION);

							}
						}
					});
			onClick$btnLimpiar();
		}
	};

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			// se crea una variable personalizada
			CBAgenciaVirtualFisicaModel objModel = new CBAgenciaVirtualFisicaModel();
			objModel.setCbAgenciaVirfiscid(1);
			CBAgenciaVirtualFisicaModel objmodificar = (CBAgenciaVirtualFisicaModel) arg0.getTarget()
					.getAttribute("objmodificar");
			Logger.getLogger(CBAgenciaVirtualFisicaController.class.getName()).log(Level.INFO,
					"obj a modificar: " + objmodificar);
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");
			Logger.getLogger(CBAgenciaVirtualFisicaController.class.getName()).log(Level.INFO,
					"id seleccioando " + idseleccionado);

			tbxCodigoAgencia.setText(objmodificar.getCodigo());
			tbxNombreAgencia.setText(objmodificar.getNombre());
			for (Comboitem item : cmbTipo.getItems()) {

				if (item.getLabel().toString().trim().equalsIgnoreCase(objmodificar.getTipo().trim())) {			
					cmbTipo.setSelectedItem(item);
				}
			}

		}
	};

	public void onClick$btnLimpiar() {
		limpiarCampos();
	}
	/// fin

	public void onClick$btnRegistrar() {
		CBAgenciaVirFiscDAO objDAO = new CBAgenciaVirFiscDAO();
		CBAgenciaVirtualFisicaModel objModel = new CBAgenciaVirtualFisicaModel();
		if (tbxCodigoAgencia.getText() != null && !tbxCodigoAgencia.getText().equals("")) {
			if (tbxNombreAgencia.getText() != null && !tbxNombreAgencia.getText().equals("")) {

				if (cmbTipo.getSelectedItem() != null) {

					try {
						if (objModel.getCbAgenciaVirfiscid() == 0) { // agregar agencia virtual fisica

							objModel.setCodigo(tbxCodigoAgencia.getText().trim());
							objModel.setNombre(tbxNombreAgencia.getText().trim());
							objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString());
							objModel.setIdAgencia(idagencia);

							objModel.setUsuario(obtenerUsuario().getUsuario());
							objDAO.agregarAgenciaVirFis(objModel);
							llenaListbox(objeDAO.listadoAgencias(idagencia));
							Messagebox.show("Registros ingresado con exito.", Constantes.ATENCION, Messagebox.OK,
									Messagebox.INFORMATION);
							onClick$btnLimpiar();
						} else { // modificar agencia virtual fisica

							objModel.setCodigo(tbxCodigoAgencia.getText().trim());
							objModel.setNombre(tbxNombreAgencia.getText().trim());
							objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString());
							objModel.setCbAgenciaVirfiscid(idseleccionado);

							objModel.setUsuario(obtenerUsuario().getUsuario());
							objDAO.modificarAgencia(objModel);
							Messagebox.show("Registros actualizado con exito.", Constantes.ATENCION, Messagebox.OK,
									Messagebox.INFORMATION);
							llenaListbox(objeDAO.listadoAgencias(idagencia));
							onClick$btnLimpiar();
						}
					} catch (Exception e) {
						Logger.getLogger(CBAgenciaVirtualFisicaController.class.getName()).log(Level.SEVERE, null, e);
						Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", Constantes.ATENCION, Messagebox.OK,
								Messagebox.ERROR);
					}

				} else {
					Messagebox.show("¡El campo tipo es obligatorio para ejecutar la operación!", Constantes.ADVERTENCIA,
							Messagebox.OK, Messagebox.EXCLAMATION);
				}
			} else {
				Messagebox.show("¡El campo nombre es obligatorio para ejecutar la operación!", Constantes.ADVERTENCIA,
						Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} else {
			Messagebox.show("¡El campo codigo es obligatorio para ejecutar la operación!",Constantes.ADVERTENCIA, Messagebox.OK,
					Messagebox.EXCLAMATION);
		}

	}

}
