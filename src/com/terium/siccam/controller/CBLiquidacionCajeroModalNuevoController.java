package com.terium.siccam.controller;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
import com.terium.siccam.dao.CBLiquidacionDetalleDAO;
import com.terium.siccam.model.CBLiquidacionCajeroModel;
import com.terium.siccam.model.CBLiquidacionDetalleModel;

public class CBLiquidacionCajeroModalNuevoController extends ControladorBase {
	private static final long serialVersionUID = 1L;

	Textbox txtUser;
	Datebox dbFechTran;
	Listbox lstQuery;
	Button btnConsultar;
	Button btnRegistrar;
	String usuario;
	CBLiquidacionDetalleModel objModel = new CBLiquidacionDetalleModel();

	public void doAfterCompose(Component param) {
		try {
			super.doAfterCompose(param);

			// setTipoCarga("1");
			// setTipoCargaFiltro("1");
			btnRegistrar.setDisabled(true);

			Logger.getLogger(CBLiquidacionCajeroModalNuevoController.class.getName()).log(Level.INFO,
					"Entra al módulo de Liquidaciones.");
			usuario = obtenerUsuario().getUsuario();
			Logger.getLogger(CBLiquidacionCajeroModalNuevoController.class.getName()).log(Level.INFO,
					"Obtiene usuario que ha iniciado sesión: " + usuario);
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * Metodos para llenado de Listbox consulta general
	 */
	String nombretransaccion;
	String fechaTransaccion;

	public void onClick$btnConsultar() {

		CBLiquidacionDetalleDAO objDAO = new CBLiquidacionDetalleDAO();

		SimpleDateFormat fechaFormato = new SimpleDateFormat("dd-MM-yyyy");

		if (txtUser.getText() == null || txtUser.getText().equals("")) {
			System.out.println("entra " + txtUser.getText());
			Messagebox.show("Debe ingresar un nombre antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		if (dbFechTran.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		System.out.println("no entra " + txtUser.getText());
		objModel.setNombtransaccion(txtUser.getText().trim());
		objModel.setFec_efectividad(fechaFormato.format(dbFechTran.getValue()));
		nombretransaccion = txtUser.getText().trim();
		fechaTransaccion = dbFechTran.getText().trim();
		llenaListbox(objDAO.execQuery(objModel));

	}

	public void onClick$btnLimpiar() {
		limpiartextbox();
	}

	public void limpiartextbox() {

		// dtbDesde.setText("");
		// dtbHasta.setText("");

	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	List<CBLiquidacionDetalleModel> list2 = null;

	// metodo para llenar listbox
	public void llenaListbox(List<CBLiquidacionDetalleModel> list) {
		limpiarListbox(lstQuery);

		System.out.println("cantidad de registros " + list.size());
		//
		list2 = list;
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

				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lstQuery);

				// paramsLlenaListbox);
			}
			if (list.size() > 0) {
				btnRegistrar.setDisabled(false);

			}
			// listConsulta.clear();
		} else {
			Messagebox.show("No se encontraron resultados!", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	/**
	 * Levantar modal para registrar nuevos parametros
	 */
	List<CBLiquidacionDetalleModel> listConsulta1 = null;

	public void onClick$btnRegistrar() {
		try {
			if (txtUser.getText() == null || txtUser.getText().equals("")) {
				System.out.println("entra " + txtUser.getText());
				Messagebox.show("Debe ingresar un nombre antes de consultar datos.", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			if (dbFechTran.getValue() == null) {
				Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}

			System.out.println("nombre " + nombretransaccion);
			if (nombretransaccion.equals(txtUser.getText().trim())) {
				// if (getNombtransaccion().trim().equals(this.userConsulta)) {*
				System.out.println("fecha " + fechaTransaccion);
				if (fechaTransaccion.equals(dbFechTran.getText().trim())) {
					// if (getFechatransaccion().equals(this.fechaConsulta)) {

					SimpleDateFormat fechaFormato = new SimpleDateFormat("MM-dd-yyyy");
					SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

					objModel.setNombtransaccion(txtUser.getText().trim());
					objModel.setFec_efectividad(fechaFormato.format(dbFechTran.getValue()));
					usuario = obtenerUsuario().getUsuario();
					String f = sdf.format(dbFechTran.getValue());
					String diaLiquid = f.substring(3, 5);
					String mesLiquid = f.substring(0, 2);
					String anioLiquid = f.substring(6, 10);
					String nomb = objModel.getNombtransaccion().trim();
					String fechaTrans = diaLiquid + "-" + mesLiquid + "-" + anioLiquid;
					String desc = "Liquidación para el día " + diaLiquid + " del mes " + mesLiquid + " del año "
							+ anioLiquid + " realizada por el usuario " + nomb;
					CBLiquidacionCajeroDAO objeDAO = new CBLiquidacionCajeroDAO();
					if (objeDAO.transaccionValida(nomb, fechaTrans)) {
						// En vez de usuario_prueba, mandar como parámetro el usuario que ha iniciado
						// sesión
						CBLiquidacionCajeroModel obje = new CBLiquidacionCajeroModel(nomb, fechaTrans, desc, 1,
								usuario);
						int pk_liquidacion = objeDAO.obtenerPK();
						if (objeDAO.guar(obje, pk_liquidacion) == 1) {
							CBLiquidacionDetalleDAO objeDetalleDAO = new CBLiquidacionDetalleDAO();

							if (objeDetalleDAO.guar(list2, pk_liquidacion, usuario)) {
								onClick$btnConsultar();
								// BindUtils.postGlobalCommand(null, null, "realizarConsulta", null);
								Logger.getLogger(CBLiquidacionCajeroModalNuevoController.class.getName())
										.log(Level.INFO, "Operación exitosa al registrar una nueva liquidación");
								Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
								onClick$closeBtn();
								// nuevaTransaccion.onClose();
							} else {
								Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
										Messagebox.EXCLAMATION);
							}
						} else {
							Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
									Messagebox.EXCLAMATION);
						}
					} else {
						Messagebox.show("El usuario ingresado ya realizó una transacción para la fecha " + fechaTrans,
								"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
					}
				} else {
					Messagebox.show(
							"La fecha con la que realizó la consulta no coincide con la que desea realizar la transacción.",
							"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			} else {
				Messagebox.show(
						"El usuario con el que realizó la consulta no coincide con el que desea realizar la transacción.",
						"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			Logger.getLogger(CBLiquidacionCajeroModalNuevoController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", "ATENCIÓN", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	Window nuevaTransacciones;

	public void onClick$closeBtn() {

		nuevaTransacciones.detach();
	}

	static Window nuevaTransaccion;

	@Command("nuevaTransaccion")
	public static Window getNuevaTransaccion() {
		nuevaTransaccion = (Window) Executions.createComponents("/cbNuevaLiquidacion.zul", null, null);
		nuevaTransaccion.doModal();
		return nuevaTransaccion;
	}

	public static void setNuevaTransaccion(Window nuevaTransaccion) {
		CBLiquidacionCajeroModalNuevoController.nuevaTransaccion = nuevaTransaccion;
	}

}
