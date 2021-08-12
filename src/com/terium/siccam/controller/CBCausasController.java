/**
 * 
 */
package com.terium.siccam.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import com.terium.siccam.dao.CBAsignaImpuestosDAO;
import com.terium.siccam.dao.CBCausasDao;
import com.terium.siccam.model.CBCausasModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;

public class CBCausasController extends ControladorBase {

	private Textbox tbxCausa;
	private Textbox tbxCodigo;
	Combobox cmbTipoConciliacion;
	Combobox cmbTipologiaasociada;
	Combobox cmbSistema;
	Combobox cmbConvenio;
	String idseleccionado;
	Button btnConsutar;
	Button btnGuardar;
	Button btnLimpiar;
	Listbox lbxlistadoCausas;
	private String usuario;
	CBCausasDao cbaDao = new CBCausasDao();
	CBCausasModel objModel = new CBCausasModel();
	/**
	 * modificado de mvvc a mvc Ovidio Santos
	 */
	private static final long serialVersionUID = 1L;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		llenaListbox(cbaDao.consultaCausas(objModel));

		usuario = obtenerUsuario().getUsuario();

		llenaComboTipo();
		llenaComboTipologias();
		llenaComboSistema();
	}

	private Textbox tbxfechaCreacion;
	private Textbox tbxusuarioCreador;
	private Textbox tbxfechaModificacion;
	private Textbox tbxmodificadoPor;

	private List<CBParametrosGeneralesModel> lstTipologiasConciliacion = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboTipo() {
		System.out.println("Llena combo tipo estado");
		limpiaCombobox(cmbTipoConciliacion);

		CBCausasDao objeDAO = new CBCausasDao();
		this.lstTipologiasConciliacion = objeDAO.obtenerTipoConciliacion("TIPO_CONCILIACION");
		if (lstTipologiasConciliacion.size() > 0) {
			for (CBParametrosGeneralesModel d : this.lstTipologiasConciliacion) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbTipoConciliacion);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());

			}
		}

	}
	//cambio de Gerbert
	private List<CBParametrosGeneralesModel> lstSistema = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboSistema() {
		System.out.println("Llena combo tipo estado");
		limpiaCombobox(cmbSistema);

		CBCausasDao objeDAO = new CBCausasDao();
		this.lstSistema = objeDAO.obtenerSistemas();
		if (lstSistema.size() > 0) {
			for (CBParametrosGeneralesModel d : this.lstSistema) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbSistema);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());

			}
		}

	}

	private List<CBParametrosGeneralesModel> lstConvenio = new ArrayList<CBParametrosGeneralesModel>();

	public void llenaComboConvenio() {
		System.out.println("Llena combo tipo estado");
		limpiaCombobox(cmbConvenio);

		CBCausasDao objeDAO = new CBCausasDao();
		this.lstConvenio = objeDAO.obtenerConvenio();
		if (lstConvenio.size() > 0) {
			for (CBParametrosGeneralesModel d : this.lstConvenio) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbConvenio);
				item.setValue(d.getIdCausaConciliacion());
				item.setLabel(d.getConvenio());

			}
		}

	}


	private List<CBTipologiasPolizaModel> listaTipologias = new ArrayList<CBTipologiasPolizaModel>();

	public void llenaComboTipologias() {
		Logger.getLogger(CBCausasController.class.getName()).log(Level.INFO, "Llena combo tipologia");
		limpiaCombobox(cmbTipologiaasociada);
		CBCausasDao objeDAO = new CBCausasDao();
		this.listaTipologias = objeDAO.obtenerTipologias();
		for (CBTipologiasPolizaModel d : this.listaTipologias) {
			Comboitem item = new Comboitem();
			item.setParent(this.cmbTipologiaasociada);
			item.setValue(d.getCbtipologiaspolizaid());
			item.setLabel(d.getNombre());
		}
	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	public void onClick$btnGuardar() {

		if (this.tbxCausa == null || this.tbxCausa.getText().trim().equals("")) {

			Messagebox.show("El campo nombre es obligatorio !", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else if (this.tbxCodigo == null || this.tbxCodigo.getText().trim().equals("")) {

			Messagebox.show("El campo codigo es obligatorio !", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else if (cmbTipoConciliacion.getValue() == null || cmbTipoConciliacion.getValue().equals("")) {

			Messagebox.show("El campo tipo es obligatorio !", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);
			//Agregado Por Gerbert
		} else if (cmbSistema.getValue() == null || cmbSistema.getValue().equals("")) {

			Messagebox.show("El campo Sistema es obligatorio !", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);
			//Agregado Por Gerbert
		} else if (cmbConvenio.getValue() == null || cmbConvenio.getValue().equals("")) {

			Messagebox.show("El campo Convenio es obligatorio !", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else if (cmbTipologiaasociada.getValue() == null || cmbTipologiaasociada.getValue().equals("")) {

			Messagebox.show("El campo tipologia poliza asociada es obligatorio !", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else {

			if (idseleccionado == null || idseleccionado.equals("")) {
				CBCausasDao cbaDao = new CBCausasDao();
				CBCausasModel objModel = new CBCausasModel();

				objModel.setTipoCausa(tbxCausa.getText().trim());

				objModel.setCodigoTipologia(tbxCodigo.getText().trim());
				objModel.setTipo(cmbTipoConciliacion.getSelectedItem().getValue().toString());
				objModel.setTipologiaasociada(cmbTipologiaasociada.getSelectedItem().getValue().toString());
				objModel.setSistema(cmbSistema.getSelectedItem().getValue().toString());//Agregado por Gerbert
				objModel.setConvenio(cmbConvenio.getSelectedItem().getValue().toString());//Agregado por Gerbert
				objModel.setUsuario(getUsuario());

				int resultado1 = cbaDao.ingresaNuevaCausa(objModel);
				// llenaListbox(objModel);
				if (resultado1 > 0) {

					onClick$btnLimpiar();
					CBCausasModel objModel1 = new CBCausasModel();
					Messagebox.show("Se creo el registro con exito", "ATTENTION", Messagebox.OK,
							Messagebox.INFORMATION);
					// onClick$btnLimpiar();
					llenaListbox(cbaDao.consultaCausas(objModel1));
				} else {
					Messagebox.show("El codigo de tipologia, no se puede repetir", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			} else {
				CBCausasDao cbcd = new CBCausasDao();
				objModel.setTipoCausa(tbxCausa.getText().trim());
				objModel.setCodigoTipologia(tbxCodigo.getText().trim());
				objModel.setTipo(cmbTipoConciliacion.getSelectedItem().getValue().toString());
				objModel.setTipologiaasociada(cmbTipologiaasociada.getSelectedItem().getValue().toString());
				objModel.setSistema(cmbSistema.getSelectedItem().getValue().toString());//Agregado Por Gerbert
				objModel.setConvenio(cmbConvenio.getSelectedItem().getValue().toString());//Agregado Por Gerbert
				objModel.setUsuario(getUsuario());
				objModel.setIdCausaConciliacion(idseleccionado);

				System.out.println("en modificar     " + objModel.getTipoCausa() + objModel.getIdCausaConciliacion());
				int resultado = cbcd.actualizaRegistroCausa(objModel);
				if (resultado > 0) {
					onClick$btnLimpiar();
					CBCausasModel objModel1 = new CBCausasModel();
					llenaListbox(cbaDao.consultaCausas(objModel1));
					Messagebox.show("Se actualizo el registro", "ATTENTION", Messagebox.OK, Messagebox.INFORMATION);
					// consultaCausas();
				} else {
					Messagebox.show("El codigo de tipologia, no se puede repetir", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			}

		}

	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
	}

	public void limpiarTextbox() {
		idseleccionado = null;
		tbxCausa.setText("");
		tbxCodigo.setText("");
		cmbTipoConciliacion.setSelectedIndex(-1);
		cmbTipologiaasociada.setSelectedIndex(-1);
		tbxfechaCreacion.setText("");
		tbxfechaModificacion.setText("");
		tbxusuarioCreador.setText("");
		tbxmodificadoPor.setText("");
		cmbSistema.setSelectedIndex(-1);//Cambio Gerbert
		cmbConvenio.setSelectedIndex(-1);//Cambio Gerbert

	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	public void onClick$btnConsultar() {

		try {
			CBCausasDao cbaDao = new CBCausasDao();
			CBCausasModel objModel = new CBCausasModel();
			System.out.println("Consulta estados tipologias");

			if (tbxCausa.getText().trim() != null && !"".equals(tbxCausa.getText().trim())) {
				objModel.setTipoCausa(tbxCausa.getText().trim());
			}
			if (tbxCodigo.getText().trim() != null && !"".equals(tbxCodigo.getText().trim())) {
				objModel.setCodigoTipologia(tbxCodigo.getText().trim());
			}
			if (cmbTipoConciliacion.getValue() != null && !cmbTipoConciliacion.getValue().equals("")) {
				objModel.setTipo(cmbTipoConciliacion.getSelectedItem().getValue().toString());
			}
			if (cmbTipologiaasociada.getValue() != null && !cmbTipologiaasociada.getValue().equals("")) {
				objModel.setTipologiaasociada(cmbTipologiaasociada.getSelectedItem().getValue().toString());
			}
			//Cambio Gerbert
			if (cmbSistema.getValue() != null && !cmbSistema.getValue().equals("")) {
				objModel.setSistema(cmbSistema.getSelectedItem().getValue().toString());
			}

			//Cambio Gerbert
			if (cmbConvenio.getValue() != null && !cmbConvenio.getValue().equals("")) {
				objModel.setConvenio(cmbConvenio.getSelectedItem().getValue().toString());
			}

			//limpiarTextbox();
			llenaListbox(cbaDao.consultaCausas(objModel));
			if (this.lbxlistadoCausas.getItemCount() != 0) {

			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBCausasModel> list) {
		limpiarListbox(lbxlistadoCausas);

		System.out.println("cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			Iterator<CBCausasModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;

			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getCodigoTipologia());
				// System.out.println("codigo lista " + objModel.getCodigoTipologia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTipoCausa());
				// System.out.println("causa lista " + objModel.getTipoCausa());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTipo());
				// System.out.println("tipo lista " + objModel.getTipo());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(objModel.getTipologiaasociada());
				// System.out.println("tipo lista " + objModel.getTipo());
				cell.setParent(item);
				

				cell = new Listcell();
				cell.setLabel(objModel.getCreadoPor());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getFechaCreacion());
				cell.setParent(item);
				//cambio Gerbert
				cell = new Listcell();
				cell.setLabel(objModel.getSistema());
				cell.setParent(item);
				//cambio Gerbert
				cell = new Listcell();
				cell.setLabel(objModel.getConvenio());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete_1.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getIdCausaConciliacion());
				btnDelete.addEventListener("onClick", eventBtnEliminar);

				// item para btn modificar
				item.setAttribute("objmodificar", objModel);

				item.setAttribute("idseleccionado", objModel.getIdCausaConciliacion());
				// System.out.println("en el llena listbox " +
				// objModel.getIdCausaConciliacion());
				item.addEventListener("onClick", eventBtnModificar);

				item.setValue(objModel);

				item.setTooltip("popAsociacionModal");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxlistadoCausas);
			}
			list.clear();
		} else {

		}

	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			limpiarTextbox();
			// se crea una variable personalizada
			CBCausasModel objmodificar = (CBCausasModel) arg0.getTarget().getAttribute("objmodificar");
			idseleccionado = (String) arg0.getTarget().getAttribute("idseleccionado");

			tbxCausa.setText(objmodificar.getTipoCausa());
			tbxfechaCreacion.setText(objmodificar.getFechaCreacion());
			tbxfechaModificacion.setText(objmodificar.getFechaModificacion());
			tbxmodificadoPor.setText(objmodificar.getModificadoPor());
			tbxusuarioCreador.setText(objmodificar.getCreadoPor());
			tbxCodigo.setText(objmodificar.getCodigoTipologia());

			for (Comboitem item : cmbTipoConciliacion.getItems()) {
				// System.out.println("en for en modificar evaluar " + item.getValue());
				if (item.getLabel().equals(String.valueOf(objmodificar.getTipo()))) {
					System.out.println("en if en modificar evaluar " + item.getValue());
					cmbTipoConciliacion.setSelectedItem(item);
				}

			}

			for (Comboitem item : cmbTipologiaasociada.getItems()) {
				// System.out.println("en for en modificar evaluar aso " + item.getValue());
				// System.out.println("en for en modificar evaluar " + item.getLabel());
				// System.out.println("en if en modificar evaluar asociada " + objmodificar.getTipologiaasociada());
				if (item.getLabel().equals(String.valueOf(objmodificar.getTipologiaasociada()))) {
				//	System.out.println("en if en modificar evaluar asociada " + item.getValue());
					//System.out.println("en if en modificar evaluar asociada " + objmodificar.getTipologiaasociada());
					cmbTipologiaasociada.setSelectedItem(item);
				}

			}
			for (Comboitem item : cmbSistema.getItems()) {
				// System.out.println("en for en modificar evaluar aso " + item.getValue());
				// System.out.println("en for en modificar evaluar " + item.getLabel());
				// System.out.println("en if en modificar evaluar asociada " + objmodificar.getTipologiaasociada());
				if (item.getLabel().equals(String.valueOf(objmodificar.getSistema()))) {
				//	System.out.println("en if en modificar evaluar asociada " + item.getValue());
					//System.out.println("en if en modificar evaluar asociada " + objmodificar.getTipologiaasociada());
					cmbSistema.setSelectedItem(item);
				}

			}
			for (Comboitem item : cmbConvenio.getItems()) {
				
				if (item.getLabel().equals(String.valueOf(objmodificar.getConvenio()))) {
			
					cmbConvenio.setSelectedItem(item);
				}

			}
			
		}
	};

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
								CBCausasDao objDAO = new CBCausasDao();
								objDAO.eliminaRegistro(idseleccionado);
								onClick$btnLimpiar();
								CBCausasModel objModel = new CBCausasModel();
								limpiarTextbox();
								llenaListbox(cbaDao.consultaCausas(objModel));
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
