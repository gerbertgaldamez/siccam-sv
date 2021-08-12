/**
 * 
 */
package com.terium.siccam.controller;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBAgenciaComercialDAO;
import com.terium.siccam.model.CBAgenciaComercialModel;
import com.terium.siccam.utils.Constantes;

/**
 * @author aaron4431
 * 
 */
public class CBAgenciaComercialController extends ControladorBase {

	/**
	 * modifica ovidio santos 24042018
	 */

	Listbox lbxlistadoAgenciaComercial;
	String idseleccionado = null;
	Button btnModificar;
	Button btnRegistrar;
	Button btnLimpiar;
	private Textbox txtUserFiltro;
	private static final long serialVersionUID = 1L;

	private String idUnionConfron;



	
	CBAgenciaComercialDAO objeDAO = new CBAgenciaComercialDAO();

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		idUnionConfron = session.getAttribute("idUnionConfronta").toString();
		Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.CONFIG,
				"ID banco agencia confronta enviado para configuracion age comercial = ", idUnionConfron);
		llenaListbox(objeDAO.listadoAgenComPosPre(idUnionConfron));
		btnRegistrar.setDisabled(false);
		btnModificar.setDisabled(true);

	}


	public void onClick$btnRegistrar() {
		CBAgenciaComercialModel objModel = new CBAgenciaComercialModel();
		try {
			if (txtUserFiltro.getText().trim() != null && !txtUserFiltro.getText().trim().equals("")) {

				objModel.setNombreAgenciaComercial(txtUserFiltro.getText().trim());
				
				boolean valido = false;
				Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
						"id de la confronta asociada " + idUnionConfron);
				String codAgenciaDevuelto = objeDAO.validarExistencia(idUnionConfron,
						objModel.getNombreAgenciaComercial());
				if (codAgenciaDevuelto == null || codAgenciaDevuelto.equals("")) {
					valido = true;
				} else {
					valido = false;
				}

				objModel.setNombreAgenciaComercial(txtUserFiltro.getText().trim());

				if (valido) {

					if (objeDAO.insertaData(idUnionConfron, objModel.getNombreAgenciaComercial())) {
						Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
								"Registro insertado de agencia comercial.");

						Messagebox.show("Agencia comercial registrada con exito.", Constantes.ATENCION, Messagebox.OK,
								Messagebox.INFORMATION);
						onClick$btnConsultar();
					} else {
						Messagebox.show("No se pudo completar la operacion", Constantes.ATENCION, Messagebox.OK,
								Messagebox.EXCLAMATION);
					}
				} else {
					Messagebox.show(
							"La agencia comercial ingresada ya se encuentra registrada para la confronta seleccionada.",
							Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
				}

			} else {
				Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", Constantes.ADVERTENCIA,
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	public void onClick$btnConsultar() throws SQLException, NamingException {
		CBAgenciaComercialModel objModel = new CBAgenciaComercialModel();
		objModel.setNombreAgenciaComercial(txtUserFiltro.getText().trim());
		llenaListbox(objeDAO.listadoAgenComPosPre(idUnionConfron));
		limpiarCampos();

	}

	public void llenaListbox(List<CBAgenciaComercialModel> list) {
		limpiarListbox(lbxlistadoAgenciaComercial);
		CBAgenciaComercialModel objModel = null;

		Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
				"cantidad de registros: ", list.size());
		if (list.isEmpty()) {
			Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
					"--> La lista con la informacion de agencias comerciales viene vacia.");
		} else {
			Iterator<CBAgenciaComercialModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getIdAgenciaComercial());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombreAgenciaComercial());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getIdAgenciaComercial());
				btnDelete.addEventListener(Events.ON_CLICK, eventBtnEliminar);

				cell.setParent(item);
				item.setAttribute("objmodificar", objModel);
				item.setAttribute("idseleccionado", objModel.getIdAgenciaComercial());
				item.addEventListener(Events.ON_CLICK, eventBtnModificar);

				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxlistadoAgenciaComercial);

				if (this.lbxlistadoAgenciaComercial.getItemCount() != 0) {
					this.btnRegistrar.setDisabled(false);
					this.btnModificar.setDisabled(true);
					limpiarCampos();
				} else {
					this.btnRegistrar.setDisabled(false);
					this.btnModificar.setDisabled(true);

				}
		
			}
		}
	}

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id

	///////
	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final String id = (String) (event.getTarget().getAttribute("idEliminar"));
			Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
					"ID  a eliminar = ", id);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBAgenciaComercialDAO objDAO = new CBAgenciaComercialDAO();
								objDAO.eliminarAgeCom(id);

								/// ACTUALIZA DESPUES DE ELIMINAR
								List<CBAgenciaComercialModel> list = objDAO.listadoAgenComPosPre(idUnionConfron);
								if (list.isEmpty()) {
									Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
											"creo el registro pero no recarga consulta ");
									limpiarListbox(lbxlistadoAgenciaComercial);
									Messagebox.show("Registros eliminado con exito.", Constantes.ATENCION, Messagebox.OK,
											Messagebox.INFORMATION);
									limpiarCampos();

								} else {
									llenaListbox(objDAO.listadoAgenComPosPre(idUnionConfron));
									limpiarCampos();
									Messagebox.show("Registros eliminado con exito.", Constantes.ATENCION, Messagebox.OK,
											Messagebox.INFORMATION);

								} 							
							}
						}
					});
		}
	};

	public void onClick$btnLimpiar() {
		limpiarCampos();
	}

	public void limpiarCampos() {

		txtUserFiltro.setText("");
		btnModificar.setDisabled(true);
		btnRegistrar.setDisabled(false);
	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}
	
	CBAgenciaComercialModel objModel1 = new CBAgenciaComercialModel();
	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			btnModificar.setDisabled(false);
			btnRegistrar.setDisabled(true);
			
			// se crea una variable personalizada
			CBAgenciaComercialModel objmodificar = (CBAgenciaComercialModel) arg0.getTarget()
					.getAttribute("objmodificar");
			Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
					"obj a modificar: ", objmodificar);
			idseleccionado = (String) arg0.getTarget().getAttribute("idseleccionado");

			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
					"id seleccioando " + idseleccionado);

			txtUserFiltro.setText(objmodificar.getNombreAgenciaComercial());
			objModel1.setNombreAgenciaComercial(objmodificar.getNombreAgenciaComercial());
			

		}
	};

	public void onClick$btnModificar() {
		CBAgenciaComercialDAO objeDAO = new CBAgenciaComercialDAO();
		CBAgenciaComercialModel objModel = new CBAgenciaComercialModel();
		boolean valido = false;
		objModel.setNombreAgenciaComercial(objModel1.getNombreAgenciaComercial());
		Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
				"cbcatalogoagenciaid en control guar ", objModel.getNombreAgenciaComercial());
		System.out.println("id:" + idseleccionado);
		System.out.println("nombreagenciacomercial:" + objModel.getNombreAgenciaComercial());
		
		try {
			if (idseleccionado != null) {
				
				if (txtUserFiltro.getText() != null && !txtUserFiltro.getText().trim().equals("")) {

					if (objModel.getNombreAgenciaComercial().trim().equals(txtUserFiltro.getText().trim())) {
						valido = true;
					} else {
						objModel.setNombreAgenciaComercial(txtUserFiltro.getText().trim());
						String	codAgenciaDevuelto = objeDAO.validarExistencia(idUnionConfron,
									objModel.getNombreAgenciaComercial());
						if (codAgenciaDevuelto == null || codAgenciaDevuelto.equals("")) {
							valido = true;							
						} else {							
							valido = false;								
						}

					}

					if (valido) {
						objModel.setNombreAgenciaComercial(txtUserFiltro.getText().trim());
						Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
								"PREPAGO ID UNION: ", idUnionConfron);
						Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
								"PREPAGO VALOR CODIGO AGENCIA: ", objModel.getNombreAgenciaComercial());
						if (objeDAO.modificaData(idUnionConfron, objModel.getNombreAgenciaComercial(),
								idseleccionado)) {
							Logger.getLogger(CBAgenciaComercialController.class.getName()).log(Level.INFO,
									"Registro de agencia comercial modificado con exito.");

							Messagebox.show("Agencia comercial modificada con exito.", Constantes.ATENCION, Messagebox.OK,
									Messagebox.INFORMATION);
							onClick$btnConsultar();
						} else {
							Messagebox.show("No se pudo completar la operacion", Constantes.ATENCION, Messagebox.OK,
									Messagebox.EXCLAMATION);
						}
					} else {
						Messagebox.show("¡La agencia comercial ya existe para esta confronta!", Constantes.ATENCION,
								Messagebox.OK, Messagebox.EXCLAMATION);
					}

				} else {
					Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", Constantes.ADVERTENCIA,
							Messagebox.OK, Messagebox.EXCLAMATION);
				}

			} else {
				Messagebox.show("¡Seleccione una agencia comercial!", Constantes.ADVERTENCIA, Messagebox.OK,
						Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.ERROR);

		}

	}

	// cierra ventana
	@SuppressWarnings("static-access")
	public void cerrarVentanaAgeCom() {
		CBConsultaEntidadesController ac = new CBConsultaEntidadesController();
		ac.nuevaAgenciaComercial.detach();
	}

	public String getIdUnionConfron() {
		return idUnionConfron;
	}

	public void setIdUnionConfron(String idUnionConfron) {
		this.idUnionConfron = idUnionConfron;
	}

	

}
