package com.terium.siccam.controller;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBLiquidacionCajeroDAO;
import com.terium.siccam.model.CBLiquidacionCajeroModel;
import com.terium.siccam.utils.Constantes;

public class CBLiquidacionCajeroController extends ControladorBase {
	private static final long serialVersionUID = 1L;

	Textbox txtUserFiltro = null;
	Datebox dbFechFiltro;
	Listbox lstLiquidaciones;
	Button btnConsultar;
	Button btnNueva;
	String fechaInicial;
	String usuario;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	public void doAfterCompose(Component param) {
		try {
			super.doAfterCompose(param);
			
			Logger.getLogger(CBLiquidacionCajeroController.class.getName()).log(Level.INFO,
					"Entra al módulo de Liquidaciones.");
			usuario = obtenerUsuario().getUsuario();
			Logger.getLogger(CBLiquidacionCajeroController.class.getName()).log(Level.INFO,
					"Obtiene usuario que ha iniciado sesión: " + usuario);
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * Metodos para llenado de Listbox consulta general
	 */
	public void onClick$btnConsultar() {
		CBLiquidacionCajeroModel objModel = new CBLiquidacionCajeroModel();
		CBLiquidacionCajeroDAO objDAO = new CBLiquidacionCajeroDAO();

		SimpleDateFormat fechaFormato = new SimpleDateFormat(Constantes.FORMATO_FECHA4);

		if (txtUserFiltro.getText() != null || !txtUserFiltro.getText().trim().equals("")) {
			System.out.println("no entra " + txtUserFiltro.getText());
			objModel.setNombtransaccion(txtUserFiltro.getText().trim());

		}

		if (dbFechFiltro.getValue() != null) {
			objModel.setFechatransaccion(fechaFormato.format(dbFechFiltro.getValue()));
		}
		llenaListbox(objDAO.consTodo(objModel));
	}

	

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBLiquidacionCajeroModel> list) {
		limpiarListbox(lstLiquidaciones);
		CBLiquidacionCajeroModel objModel = new CBLiquidacionCajeroModel();
		System.out.println("cantidad de registros " + list.size());

		if (list != null && list.size() > 0) {
			Iterator<CBLiquidacionCajeroModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getNombtransaccion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getFechatransaccion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getEfectivo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCuotasvisa());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCuotascredomatic());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getVisa());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCredomatic());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getOtras());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCheque());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getExcencioniva());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getDeposito());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCreador());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getFechacreacion());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete_1.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbliquidacionid());
				btnDelete.addEventListener(Events.ON_CLICK, eventBtnEliminar);

				item.setAttribute("objmodificar", objModel);
				item.setAttribute("fechaini", objModel.getFechatransaccion());
				btnDelete.setAttribute("idconsultar", objModel.getCbliquidacionid());

				item.addEventListener(Events.ON_DOUBLE_CLICK, eventBtnModificar);

				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lstLiquidaciones);
			
			}
		} else {
			Messagebox.show("No se encontraron resultados!", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id

	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int idseleccionado = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
			System.out.println("ID  a eliminar = " + idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (((Integer) event.getData()).intValue() == Messagebox.YES) {
							CBLiquidacionCajeroDAO objDAO = new CBLiquidacionCajeroDAO();
							objDAO.elim(idseleccionado);
							CBLiquidacionCajeroModel objModel = new CBLiquidacionCajeroModel();
							llenaListbox(objDAO.consTodo(objModel));
							Messagebox.show("Registros eliminado con exito.", Constantes.ATENCION, Messagebox.OK,
									Messagebox.INFORMATION);
						}
					}
				});
		}
	};

	/**
	 * Levantar modal para registrar nuevos parametros
	 */

	public void onClick$btnNueva() {
		try {
			Executions.createComponents("/cbNuevaLiquidacion.zul", null, null);
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
		}
	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {

			// llenaListbox(objModel);

			// se crea una variable personalizada
			CBLiquidacionCajeroModel objmodificar = (CBLiquidacionCajeroModel) arg0.getTarget()
					.getAttribute("objmodificar");

			fechaInicial = (String) arg0.getTarget().getAttribute("fechaini");

			System.out.println("obj a modificar: " + objmodificar);
			

			misession.setAttribute("sesionfecha1", fechaInicial);
			misession.setAttribute("sesioncontabilizacionModal", objmodificar);
			session.setAttribute("interfaceTarjeta", CBLiquidacionCajeroController.this);
			Window detalle = (Window) Executions.createComponents("/cbDetalleLiquidacion.zul", null, null);
			detalle.doModal();

		}
	};

}
