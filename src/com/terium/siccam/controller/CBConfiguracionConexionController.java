/**
 * 
 */
package com.terium.siccam.controller;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConfiguracionConexionDao;
import com.terium.siccam.model.CBConfiguracionConModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;
import com.terium.siccam.utils.Constantes;

/**
 * @author lab
 * 
 */
public class CBConfiguracionConexionController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBConfiguracionConexionController.class);

	Button btnConsutar;
	Button btnGuardar;
	Button btnLimpiar;
	Listbox lbxlistadoConexiones;

	CBConfiguracionConexionDao cbaDao = new CBConfiguracionConexionDao();
	CBConfiguracionConModel objModel = new CBConfiguracionConModel();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		llenaListbox(cbaDao.obtieneListaConexionex(objModel));

		usuario = obtenerUsuario().getUsuario();

	}

	private Textbox tbxNombreDeConexion;
	private Textbox tbxIpConexion;
	private Textbox tbxUsuarioConexion;
	private Textbox tbxPasswordConexion;

	String idseleccionado = null;
	// usuario de session
	private String usuario;

	// parametros para auditoria
	private Textbox tbxcreadoPor;
	private Textbox tbxfechaCreacion;
	private Textbox tbxmodificadoPor;
	private Textbox tbxfechaModificacion;

	public void onClick$btnGuardar() {

		if (this.tbxNombreDeConexion == null || this.tbxNombreDeConexion.getText().trim().equals("")) {

			Messagebox.show("El campo nombre es obligatorio !", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else if (tbxIpConexion == null || tbxIpConexion.getText().trim().equals("")) {
			Messagebox.show("Debe de ingresar una ip valida para la conexión", "ATENCIÓN", Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (tbxUsuarioConexion == null || tbxUsuarioConexion.getText().trim().equals("")) {
			Messagebox.show("Debe de ingresar el nombre del usuario para la conexión", "ATENCIÓN", Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else if (tbxPasswordConexion == null || tbxPasswordConexion.getText().trim().equals("")) {
			Messagebox.show("Debe de ingresar una contraseña para la conexión", "ATENCIÓN", Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else {

			if (idseleccionado == null || idseleccionado.equals("")) {

				CBConfiguracionConexionDao cbaDao = new CBConfiguracionConexionDao();
				CBConfiguracionConModel objModel = new CBConfiguracionConModel();

				objModel.setNombre(tbxNombreDeConexion.getText().trim());
				objModel.setIpConexion(tbxIpConexion.getText().trim());
				objModel.setUsuarioConexion(tbxUsuarioConexion.getText().trim());
				objModel.setPass(tbxPasswordConexion.getText().trim());
				objModel.setUsuario(obtenerUsuario().getUsuario());
				log.debug(
						"onClick$btnGuardar() - " + "id " + idseleccionado);
				
				cbaDao.insertaConexion(objModel);
				onClick$btnLimpiar();
				CBConfiguracionConModel objModel1 = new CBConfiguracionConModel();
				Messagebox.show("Se creo el registro con exito", "ATTENTION", Messagebox.OK, Messagebox.INFORMATION);

				llenaListbox(cbaDao.obtieneListaConexionex(objModel1));

			} else {
				CBConfiguracionConModel objModel = new CBConfiguracionConModel();

				CBConfiguracionConexionDao cbaDao = new CBConfiguracionConexionDao();
				objModel.setNombre(tbxNombreDeConexion.getText().trim());
				objModel.setIpConexion(tbxIpConexion.getText().trim());
				objModel.setUsuarioConexion(tbxUsuarioConexion.getText().trim());
				objModel.setPass(tbxPasswordConexion.getText().trim());
				objModel.setModificadoPor(obtenerUsuario().getUsuario());
				log.debug(
						"onClick$btnGuardar() - " + "ip btn " + objModel.getIpConexion());
				log.debug(
						"onClick$btnGuardar() - " + "\n*** id seleccionado ***\n" + idseleccionado);
				//Logger.getLogger(CBConfiguracionConexionController.class.getName())
				//.log(Level.INFO, "\n*** id seleccionado ***\n" + idseleccionado);
				objModel.setIdConexionConf(idseleccionado);
				cbaDao.actualizaConexion(objModel);

				onClick$btnLimpiar();
				Messagebox.show("Se actualizo el registro", "ATTENTION", Messagebox.OK, Messagebox.INFORMATION);

				CBConfiguracionConModel objModel2 = new CBConfiguracionConModel();
				llenaListbox(cbaDao.obtieneListaConexionex(objModel2));

			}

		}

	}

	public void onClick$btnLimpiar() {

		idseleccionado = null;
		limpiarTextbox();
	}

	public void limpiarTextbox() {
		tbxIpConexion.setText(Constantes.IP_DEFAULT);
		tbxcreadoPor.setText("");
		tbxUsuarioConexion.setText("");
		tbxfechaCreacion.setText("");
		tbxPasswordConexion.setText("");
		tbxNombreDeConexion.setText("");
		tbxfechaModificacion.setText("");
		tbxmodificadoPor.setText("");
	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	public void onClick$btnConsultar() {

		try {

			CBConfiguracionConexionDao cbaDao = new CBConfiguracionConexionDao();
			CBConfiguracionConModel objModel = new CBConfiguracionConModel();
			log.debug(
					"onClick$btnConsultar() - " + "\n*** Entra a pantalla de consulta de conexiones ***\n");
			//Logger.getLogger(CBConfiguracionConexionController.class.getName())
			//.log(Level.INFO, "\n*** Entra a pantalla de consulta de conexiones ***\n");

			if (tbxNombreDeConexion.getText().trim() != null && !"".equals(tbxNombreDeConexion.getText().trim())) {
				objModel.setNombre(tbxNombreDeConexion.getText().trim());
			}

			if (tbxUsuarioConexion.getText().trim() != null && !"".equals(tbxUsuarioConexion.getText().trim())) {
				objModel.setUsuarioConexion(tbxUsuarioConexion.getText().trim());
			}
			onClick$btnLimpiar();
			llenaListbox(cbaDao.obtieneListaConexionex(objModel));

			if (this.lbxlistadoConexiones.getItemCount() != 0) {

			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnConsultar() - Error ", e);
			//Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBConfiguracionConModel> list) {
		limpiarListbox(lbxlistadoConexiones);

		System.out.println("cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			Iterator<CBConfiguracionConModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;

			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getNombre());
				// System.out.println("causa lista " + objModel.getTipoCausa());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getIpConexion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getUsuario());
				cell.setParent(item);

				
				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete_1.png");
				cell.setParent(item);
				

				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getIdConexionConf());
				btnDelete.addEventListener("onClick", eventBtnEliminar);

				// item para btn modificar
				item.setAttribute("objmodificar", objModel);

				item.setAttribute("idseleccionado", objModel.getIdConexionConf());

				item.addEventListener("onClick", eventBtnModificar);

				item.setValue(objModel);

				item.setTooltip("popAsociacionModal");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxlistadoConexiones);
			}
			list.clear();
		} else {

		}

	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			idseleccionado= null;
			// se crea una variable personalizada
			CBConfiguracionConModel objmodificar = (CBConfiguracionConModel) arg0.getTarget()
					.getAttribute("objmodificar");
			idseleccionado = (String) arg0.getTarget().getAttribute("idseleccionado");

			tbxNombreDeConexion.setText(objmodificar.getNombre());
			tbxUsuarioConexion.setText(objmodificar.getUsuario());
			tbxPasswordConexion.setText(objmodificar.getPass());
			tbxIpConexion.setText(objmodificar.getIpConexion());
			tbxcreadoPor.setText(objmodificar.getCreadoPor());
			tbxfechaCreacion.setText(objmodificar.getFechaCreacion());
			tbxmodificadoPor.setText(objmodificar.getModificadoPor());
			tbxfechaModificacion.setText(objmodificar.getFechaModificacion());

		}
	};

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id

	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final String idseleccionado = (String) (event.getTarget().getAttribute("idEliminar"));

			log.debug("onClick$btnConsultar() -\n*** id seleccionado eliminar ***\n" + idseleccionado);
			//Logger.getLogger(CBConfiguracionConexionController.class.getName())
			//.log(Level.INFO, "\n*** id seleccionado eliminar ***\n" + idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								cbaDao.elimaConexion(idseleccionado);
								onClick$btnLimpiar();
								CBConfiguracionConModel objModel = new CBConfiguracionConModel();

								llenaListbox(cbaDao.obtieneListaConexionex(objModel));
								Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
										Messagebox.INFORMATION);

							}
						}
					});
			onClick$btnLimpiar();
		}
	};

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
