package com.terium.siccam.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBDepositosRecDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBDepositosRecModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;

public class CBDepositosRecController extends ControladorBase {

	/**
	 * creador ovidio santos
	 */
	private static final long serialVersionUID = 1L;
	Combobox cmbEntidad;
	private Textbox tbxTexto;
	Button btnConsutar;
	Button btnNuevo;
	Button btnDelete;
	Button btnModificar;
	Button btnLimpiar;
	Listbox lbxConsultaDepositos;
	int idseleccionado = 0;
	Combobox cmbTipologias;
	Combobox cmbTipoFechas;
	CBDepositosRecDAO cbaDao = new CBDepositosRecDAO();
	CBDepositosRecModel objModel = new CBDepositosRecModel();

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		// llenaListbox();
		// llenaListbox(cbaDao.obtenerTipologias(objModel));
		btnModificar.setDisabled(true);
		usuario = obtenerUsuario().getUsuario();
		// llenaComboTipo();
		cargarComboAgencia();
		llenaComboTipologia();
		llenaComboTipoFechas();
		// llenaComboPideEntidad();
		onClick$btnConsultar();
	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	//////////////////////////////////////
	// cargar combobox
	public void cargarComboAgencia() {
		try {
			CBDepositosRecDAO cba = new CBDepositosRecDAO();
			List<CBCatalogoAgenciaModel> lst = cba.obtieneListadoAgencias(null, null, null, null);
			Iterator<CBCatalogoAgenciaModel> iLst = lst.iterator();
			CBCatalogoAgenciaModel obj = null;
			Comboitem item = null;
			while (iLst.hasNext()) {
				obj = iLst.next();
				item = new Comboitem();
				item.setLabel(obj.getNombre());
				item.setValue(obj.getcBCatalogoAgenciaId());
				item.setParent(cmbEntidad);
			}
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			System.out.println("Se ha producido un error = " + e.getMessage());
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void llenaComboTipologia() {
		try {
			CBDepositosRecDAO cba = new CBDepositosRecDAO();
			List<CBTipologiasPolizaModel> lst = cba.obtenerTipologias();
			Iterator<CBTipologiasPolizaModel> iLst = lst.iterator();
			CBTipologiasPolizaModel obj = null;
			Comboitem item = null;
			while (iLst.hasNext()) {
				obj = iLst.next();
				item = new Comboitem();
				item.setLabel(obj.getNombre());
				item.setValue(obj.getCbtipologiaspolizaid());
				item.setParent(cmbTipologias);
			}
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			System.out.println("Se ha producido un error = " + e.getMessage());
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private List<CBParametrosGeneralesModel> listaTipoFecha = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboTipoFechas() {
		Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO, "Llena combo tipo fecha");
		limpiarCombobox(cmbTipoFechas);
		CBDepositosRecDAO objeDAO = new CBDepositosRecDAO();
		this.listaTipoFecha = objeDAO.obtenerTipoObjetoX("TIPO_FECHA");
		for (CBParametrosGeneralesModel d : this.listaTipoFecha) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipoFechas);
			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
		}
	}

	public void limpiarCombobox(Combobox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
		}
	}

	private String usuario;

	// boton agregar

	public void onClick$btnNuevo() throws SQLException, NamingException {

		CBDepositosRecDAO objDAO = new CBDepositosRecDAO();

		CBDepositosRecModel objModel = new CBDepositosRecModel();

		String user = getUsuario();

		objModel.setTexto(tbxTexto.getText().trim());
		objModel.setCreadoPor(user);
		if (cmbEntidad.getSelectedItem() == null) {

			Messagebox.show("Debe seleccionar una entidad .", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (cmbTipologias.getSelectedItem() == null) {

			Messagebox.show("Debe seleccionar una tipologia .", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else if (cmbTipoFechas.getSelectedItem() == null) {

			Messagebox.show("Debe seleccionar tipo de fecha.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else {

			objModel.setEntidad(Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString()));
			objModel.setTipologia(Integer.parseInt(cmbTipologias.getSelectedItem().getValue().toString()));
			objModel.setTipofecha(Integer.parseInt(cmbTipoFechas.getSelectedItem().getValue().toString()));

			System.out.println("COMBO TIPOLOGIA " + objModel.getTipologia());
			if (objDAO.insertDepositosRec(objModel)) {

				Messagebox.show("Se creo el registro con exito", "ATTENTION", Messagebox.OK, Messagebox.INFORMATION);

				CBDepositosRecModel objModel1 = new CBDepositosRecModel();
				onClick$btnLimpiar();
				llenaListbox(cbaDao.obtenerDepositos(objModel1));
			}
		}
	}

	public void onClick$btnModificar() throws SQLException, NamingException {

		if (idseleccionado > 0) {

			CBDepositosRecDAO objDAO = new CBDepositosRecDAO();
			CBDepositosRecModel objModel = new CBDepositosRecModel();
			String user = getUsuario();

			objModel.setTexto(tbxTexto.getText().trim());

			objModel.setModificadoPor(user);

			if (cmbEntidad.getSelectedItem() == null) {

				Messagebox.show("Debe seleccionar una entidad .", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (cmbTipologias.getSelectedItem() == null) {

				Messagebox.show("Debe seleccionar una tipologia .", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);

			} else if (cmbTipoFechas.getSelectedItem() == null) {

				Messagebox.show("Debe seleccionar tipo de fecha.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);

			} else {

				objModel.setEntidad(Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString()));
				objModel.setTipologia(Integer.parseInt(cmbTipologias.getSelectedItem().getValue().toString()));
				objModel.setTipofecha(Integer.parseInt(cmbTipoFechas.getSelectedItem().getValue().toString()));

				Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
						"el id a modificar es " + idseleccionado);
				if (objDAO.modificaDepositosRec(objModel, idseleccionado)) {
					// llenaListbox();

					Messagebox.show("Se actualizo el registro", "ATTENTION", Messagebox.OK, Messagebox.INFORMATION);

					CBDepositosRecModel objModel1 = new CBDepositosRecModel();
					onClick$btnLimpiar();
					llenaListbox(cbaDao.obtenerDepositos(objModel1));

				} else {
					Messagebox.show("No se Modifico, verifique los datos ingresados!", "ATTENTION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}

			}
		} else {
			Messagebox.show("Seleccione un deposito!", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
		btnModificar.setDisabled(true);
		btnNuevo.setDisabled(false);
	}

	public void limpiarTextbox() {

		tbxTexto.setText("");
		cmbEntidad.setSelectedIndex(-1);
		cmbTipologias.setSelectedIndex(-1);
		cmbTipoFechas.setSelectedIndex(-1);

	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	// metodo consulta
	public void onClick$btnConsultar() {

		try {
			CBDepositosRecDAO cbaDao = new CBDepositosRecDAO();
			CBDepositosRecModel objModel = new CBDepositosRecModel();

			if (cmbEntidad.getSelectedItem() != null) {

				objModel.setEntidad(Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString()));

			}
			if (cmbTipoFechas.getSelectedItem() != null ) {

				objModel.setNombreTipoFecha(cmbTipoFechas.getSelectedItem().getValue().toString());
				System.out.println("set tipo fecha " +objModel.getTipofecha());
			}
			if (cmbTipologias.getSelectedItem() != null) {

				objModel.setTipologia(Integer.parseInt(cmbTipologias.getSelectedItem().getValue().toString()));
			}

			if (tbxTexto.getText().trim() != null && !"".equals(tbxTexto.getText().trim())) {
				objModel.setTexto(tbxTexto.getText().trim());
			}
			llenaListbox(cbaDao.obtenerDepositos(objModel));
			if (this.lbxConsultaDepositos.getItemCount() != 0) {
				btnNuevo.setDisabled(false);
				btnModificar.setDisabled(true);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBDepositosRecModel> list) {
		limpiarListbox(lbxConsultaDepositos);

		System.out.println("cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			Iterator<CBDepositosRecModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;

			// metodo para llenar listbox

			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(objModel.getNombreEntidad());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombreTipologia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTexto());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombreTipoFecha());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete_1.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbdepositosid());
				btnDelete.addEventListener("onClick", eventBtnEliminar);

				// item para btn modificar
				item.setAttribute("objmodificar", objModel);

				item.setAttribute("idseleccionado", objModel.getCbdepositosid());
				item.addEventListener("onClick", eventBtnModificar);

				item.setAttribute("objModelModal", objModel);

				item.setValue(objModel);

				item.setTooltip("popAsociacionModal");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxConsultaDepositos);
			}
			list.clear();
		} else {

		}

	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			btnModificar.setDisabled(false);
			btnNuevo.setDisabled(true);
			limpiarTextbox();
			// se crea una variable personalizada
			CBDepositosRecModel objmodificar = (CBDepositosRecModel) arg0.getTarget().getAttribute("objmodificar");
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");

			System.out.println("id seleccioando " + idseleccionado);

			tbxTexto.setText(objmodificar.getTexto());

			System.out.println("comboEntidad abtes de if " + objmodificar.getEntidad());

			for (Comboitem item : cmbEntidad.getItems()) {
				// System.out.println("en for en modificar evaluar " + item.getValue());
				if (item.getValue().equals(String.valueOf(objmodificar.getEntidad()))) {
					System.out.println("en if en modificar evaluar " + item.getValue());
					cmbEntidad.setSelectedItem(item);
				}

			}

			System.out.println("combotipologias abtes de if " + objmodificar.getTipologia());
			for (Comboitem item : cmbTipologias.getItems()) {
				// System.out.println("for en modificar evaluar. " + item.getValue());
				// System.out.println("combotipologias en validacion " +
				// objmodificar.getTipologia());
				if (item.getValue().equals(objmodificar.getTipologia())) {
					System.out.println("en if en modificar evaluar " + item.getValue());
					cmbTipologias.setSelectedItem(item);
				}
			}

			System.out.println("combotipo fechas abtes de if " + objmodificar.getTipofecha());
			for (Comboitem item : cmbTipoFechas.getItems()) {
				//System.out.println("for en modificar " + item.getValue());
				// System.out.println("comboESTADO abtes de if " +
				// objmodificar.getPide_Entidad());
				if (item.getValue().equals(String.valueOf(objmodificar.getTipofecha()))) {
					// System.out.println("combo ESTADO en if " + objmodificar.getPide_Entidad());
					cmbTipoFechas.setSelectedItem(item);
				}
			}

		}
	};

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id

	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int idseleccionado = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
			System.out.println("ID  a eliminar = " + idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBDepositosRecDAO objDAO = new CBDepositosRecDAO();
								if (objDAO.eliminarDepositosRec(idseleccionado)) {
									onClick$btnLimpiar();
									CBDepositosRecModel objModel = new CBDepositosRecModel();
									limpiarTextbox();
									llenaListbox(cbaDao.obtenerDepositos(objModel));
									Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
											Messagebox.INFORMATION);
								}
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
