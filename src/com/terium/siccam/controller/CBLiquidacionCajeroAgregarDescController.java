package com.terium.siccam.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBLiquidacionDetalleDAO;
import com.terium.siccam.model.CBLiquidacionCajeroModel;
import com.terium.siccam.model.CBLiquidacionDetalleModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;

public class CBLiquidacionCajeroAgregarDescController extends ControladorBase {
	private static Logger log = Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class);

	Window contabilizacionModa;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	HttpSession misession1 = (HttpSession) Sessions.getCurrent().getNativeSession();
	HttpSession misession2 = (HttpSession) Sessions.getCurrent().getNativeSession();
	List<CBLiquidacionCajeroModel> detallesSeleccionados = null;
	CBLiquidacionCajeroModel objModel = new CBLiquidacionCajeroModel();
	List<CBLiquidacionCajeroModel> listConsulta = null;

	CBLiquidacionCajeroModel objb = null;
	String fechaini = null;

	private static final long serialVersionUID = 1L;

	Textbox txtDescTipoTarjeta = null;
	Datebox dbFechFiltro;
	Listbox lstDetalle;
	Button btnConsultar;
	Button btnModificar;
	private int tipoValorSeleccionado;
	Button btnRegistrarDesc;
	String fechaInicial;
	private Label lblNombreUser;
	private Label lblfecha;
	int idRegistro;
	int idseleccionado = 0;
	String nombretran;
	String usuario;

	public void doAfterCompose(Component param) {
		try {
			super.doAfterCompose(param);
			objb = (CBLiquidacionCajeroModel) misession.getAttribute("sesioncontabilizacionModal");
			fechaInicial = (String) misession1.getAttribute("sesionfecha1");
			lblNombreUser.setValue(objb.getNombtransaccion());
			lblfecha.setValue(fechaInicial);
			idRegistro = objb.getCbliquidacionid();
			onClick$btnConsultar();
			nombretran = objb.getNombtransaccion();
			System.out.println("id que recibe " + objb.getCbliquidacionid());

			log.debug(
					"doAfterCompose() - " + "Entra al módulo de Liquidaciones.");
			//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(Level.INFO,
					//"Entra al módulo de Liquidaciones.");
			usuario = obtenerUsuario().getUsuario();
			log.debug(
					"doAfterCompose() - " + "Obtiene usuario que ha iniciado sesión: " + usuario);
			//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(Level.INFO,
					//"Obtiene usuario que ha iniciado sesión: " + usuario);
		} catch (Exception e) {
			log.error("doAfterCompose() - Error ", e);
			//Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * Metodos para llenado de Listbox consulta general
	 */
	public void onClick$btnConsultar() {
		try {
			CBLiquidacionDetalleDAO obje = new CBLiquidacionDetalleDAO();
			llenaListbox(obje.consByID(idRegistro));

			log.debug(
					"onClick$btnConsultar() - " + "Módulo de liquidaciones - datos seleccionados de liquidación detalle:");
			//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(Level.INFO,
					//"Módulo de liquidaciones - datos seleccionados de liquidación detalle:");

		} catch (Exception e) {
			log.error("onClick$btnConsultar() - Error ", e);
			//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. ", "ERROR", Messagebox.OK, Messagebox.ERROR);
		}

	}

	public void onClick$btnLimpiar() {
		btnRegistrarDesc.setDisabled(false);
		btnModificar.setDisabled(true);
		limpiartextbox();
	}

	public void limpiartextbox() {

		txtDescTipoTarjeta.setText("");
		idseleccionado = 0;

	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBLiquidacionDetalleModel> list) {
		limpiarListbox(lstDetalle);
		CBLiquidacionDetalleModel objModel = new CBLiquidacionDetalleModel();
		System.out.println("cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			Iterator<CBLiquidacionDetalleModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(String.valueOf(objModel.getTipo_valo()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTipo_pago());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCod_tipotarjeta());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getDesc());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTotal());
				cell.setParent(item);

				// item para btn modificar
				item.setAttribute("objmodificar", objModel);

				item.setAttribute("idseleccionado", objModel.getCbliquidaciondetalleid());

				item.setAttribute("tipoValor", objModel.getTipo_valo());

				item.addEventListener("onClick", eventBtnModificar);

				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lstDetalle);

				// paramsLlenaListbox);
			}
			// listConsulta.clear();
		} else {
			Messagebox.show("No se encontraron resultados!", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	/**
	 * Levantar modal para registrar nuevos parametros
	 */

	public void onClick$btnNueva() {
		try {
			Executions.createComponents("/cbNuevaLiquidacion.zul", null, null);
		} catch (Exception e) {
			log.error("onClick$btnNueva() - Error ", e);
			//Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			
			// limpiarTextbox();
			// se crea una variable personalizada
			CBLiquidacionDetalleModel objmodificar = (CBLiquidacionDetalleModel) arg0.getTarget()
					.getAttribute("objmodificar");
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");
			tipoValorSeleccionado = (Integer) arg0.getTarget().getAttribute("tipoValor");

			System.out.println("id seleccioando " + idseleccionado);

			txtDescTipoTarjeta.setText(objmodificar.getDesc());
			if (tipoValorSeleccionado ==12 || tipoValorSeleccionado==19) {
			btnModificar.setDisabled(false);
			btnRegistrarDesc.setDisabled(true);
			}else {
				btnModificar.setDisabled(true);
				btnRegistrarDesc.setDisabled(true);
			}
		}
	};

	public void onClick$btnRegistrarDesc() throws SQLException, NamingException {

		try {
			if (txtDescTipoTarjeta.getText() == null || txtDescTipoTarjeta.getText().equals("")) {
				log.debug(
						"onClick$btnRegistrarDesc() - " + "entra " + txtDescTipoTarjeta.getText());
				
				Messagebox.show("¡Debe ingresar una descripción de tipo tarjeta!", "ADVERTENCIA", Messagebox.OK,
						Messagebox.EXCLAMATION);

				return;
			}

			CBLiquidacionDetalleModel obje = new CBLiquidacionDetalleModel();
			CBLiquidacionDetalleDAO objeDAO = new CBLiquidacionDetalleDAO();
			List<CBLiquidacionDetalleModel> list = new ArrayList<CBLiquidacionDetalleModel>();
			obje.setTipo_valo(19);
			obje.setTipo_pago("DEPOSITO EFECTIVO CAMBIO");
			obje.setCod_tipotarjeta("Déposito");
			obje.setDesc(txtDescTipoTarjeta.getText().trim());
			obje.setTotal("0");
			list.add(obje);
			int pk = objeDAO.obtenerPK();
			log.debug(
					"onClick$btnRegistrarDesc() - " + "parametros " + pk + idRegistro + usuario);
			
			if (objeDAO.guarDetalleTipoValor19(list, pk, idRegistro, usuario)) {

				if (objeDAO.execConciliaDepositoPrc(pk)) {
					log.debug(
							"onClick$btnRegistrarDesc() - " + "Operación exitosa al registrar detalle liquidación con tipo valor 19.");
					//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(Level.INFO,
							//"Operación exitosa al registrar detalle liquidación con tipo valor 19.");
					Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
					final HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("idRegistro", idRegistro);
					map.put("name", nombretran);
					map.put("fech", fechaInicial);
					onClick$btnConsultar();
					onClick$btnLimpiar();
				} else {
					Messagebox.show("No se pudo completar la operación  ", "ATENCIÓN", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}
			} else {
				Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			log.error("onClick$btnRegistrarDesc() - Error ", e);
			//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", "ATENCIÓN", Messagebox.OK,
					Messagebox.ERROR);
		}

	}

	public void onClick$btnModificar() throws SQLException, NamingException {
		{
			if (idseleccionado > 0) {
				if (txtDescTipoTarjeta.getText() != null && !"".equals(txtDescTipoTarjeta.getText().trim())) {
					CBLiquidacionDetalleModel obje = new CBLiquidacionDetalleModel();
					CBLiquidacionDetalleDAO objeDAO = new CBLiquidacionDetalleDAO();
					obje.setDesc(txtDescTipoTarjeta.getText().trim());
					if (objeDAO.modiDetalleTipoValorX(obje, idseleccionado)) {
						log.debug(
								"onClick$btnModificar() - " + "tipo seleccionado " + tipoValorSeleccionado);
						
						if (tipoValorSeleccionado == 19) {
							if (objeDAO.execConciliaDepositoPrc(idseleccionado)) {
								log.debug(
										"onClick$btnModificar() - " + "Operación exitosa al modificar detalle liquidación con tipo valor 19.");
								//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(
										//Level.INFO,
										//"Operación exitosa al modificar detalle liquidación con tipo valor 19.");
								Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
								final HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("idRegistro", idRegistro);
								map.put("name", nombretran);
								map.put("fech", fechaInicial);
								onClick$btnConsultar();
								onClick$btnLimpiar();

							} else {
								Messagebox.show("No se pudo completar la operación. ", "ATENCIÓN", Messagebox.OK,
										Messagebox.EXCLAMATION);
							}
						} else if (tipoValorSeleccionado == 12) {
							if (objeDAO.execConciliaCredUnico(idseleccionado)) {
								log.debug(
										"onClick$btnModificar() - " + "Operación exitosa al modificar detalle liquidación con tipo valor 12.");
								//Logger.getLogger(CBLiquidacionCajeroAgregarDescController.class.getName()).log(
										//Level.INFO,
										//"Operación exitosa al modificar detalle liquidación con tipo valor 12.");
								Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
								final HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("idRegistro", idRegistro);
								map.put("name", nombretran);
								map.put("fech", fechaInicial);
								;
								onClick$btnConsultar();
								onClick$btnLimpiar();

							} else {
								Messagebox.show("No se pudo completar la operación..", "ATENCIÓN", Messagebox.OK,
										Messagebox.EXCLAMATION);
							}
						} else {
							Messagebox.show("No se pudo completar la operación... ", "ATENCIÓN", Messagebox.OK,
									Messagebox.EXCLAMATION);
						}
					} else {
						Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
								Messagebox.EXCLAMATION);
					}

				} else {
					Messagebox.show("¡Debe ingresar una descripción de tipo tarjeta!", "ADVERTENCIA", Messagebox.OK,
							Messagebox.EXCLAMATION);
				}

			} else {
				Messagebox.show("Seleccione una caja a modificar!", "ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		}
	}

}
