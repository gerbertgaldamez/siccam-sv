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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBBancoAgenciaCajasDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBBancoAgenciaCajasModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;

/*
 * Desarrollado por Carlos Godínez / QitCorp 21-02-2017
 * */

public class CBCatalogoAgenciaCajasController extends ControladorBase {
	private static final long serialVersionUID = 1L;
	private List<CBParametrosGeneralesModel> listParamEstado = new ArrayList<CBParametrosGeneralesModel>();
	CBBancoAgenciaCajasModel objModel = new CBBancoAgenciaCajasModel();

	Listbox lbxConsulta;
	int idseleccionado = 0;
	private Combobox cmbEstado;
	Button btnModificar;
	Button btnRegistrar;
	Button btnLimpiar;
	private Textbox txtCodOficina;
	private Textbox txtCodCaja;
	private Textbox txtCajero;
	private Label lblAgrupacion;
	private Label lblNombreAgencia;
	// Extra
	private String nombreBanco;
	private String nombreAgencia;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		setNombreBanco((String) session.getAttribute("nombreBanco"));
		setNombreAgencia((String) session.getAttribute("nombreAgencia"));
		objModel.setCbcatalogoagenciaid(Integer.parseInt(session.getAttribute("idAgencia").toString()));
		btnRegistrar.setDisabled(false);
		btnModificar.setDisabled(true);

		// this.limpiarCampos();

		lblAgrupacion.setValue(getNombreBanco());
		lblNombreAgencia.setValue(getNombreAgencia());

		llenaComboEstado();
		usuario = obtenerUsuario().getUsuario();
		onClick$btnConsultar();

	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public String getNombreAgencia() {
		return nombreAgencia;
	}

	public void setNombreAgencia(String nombreAgencia) {
		this.nombreAgencia = nombreAgencia;
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

	/**
	 * Combo estado
	 */
	public void llenaComboEstado() {
		CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
		listParamEstado = objDao.obtenerListaTipoAgrupacion(CBParametrosGeneralesDAO.S_OBTENER_TIPO_AGRUPACIONES_2);
		System.out.println("tama;o de la lista " + listParamEstado.size());
		if (listParamEstado != null && listParamEstado.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = listParamEstado.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto1());
				item.setParent(cmbEstado);
			}
		} else {
			Messagebox.show("Error al cargar la configuracion de cuentas", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	public void onClick$btnConsultar() throws SQLException, NamingException {

		// limpiarListbox(lbxConsulta);

		CBBancoAgenciaCajasDAO objeDAO = new CBBancoAgenciaCajasDAO();
		// CBBancoAgenciaAfiliacionesModel objModel = new
		// CBBancoAgenciaAfiliacionesModel();
		objModel.setCbcatalogoagenciaid(objModel.getCbcatalogoagenciaid());
		objModel.setCajero(txtCajero.getText().trim());
		objModel.setCod_caja(txtCodCaja.getText().trim());
		objModel.setCod_oficina(txtCodOficina.getText().trim());

		Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
				"Entra a consulta estados de afiliaciones");

		// objModel.setUsuario(user);

		llenaListbox(objeDAO.consByIdAgencia(objModel));

		limpiarCampos();

	}

	public void llenaListbox(List<CBBancoAgenciaCajasModel> list) {
		limpiarListbox(lbxConsulta);
		CBBancoAgenciaCajasModel objModel = null;

		Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
				"cantidad de registros " + list.size());
		if (list != null && list.size() > 0) {
			Iterator<CBBancoAgenciaCajasModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getCod_oficina());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCod_caja());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCajero());
				cell.setParent(item);
				
				/*
				 * cell = new Listcell();
				 * cell.setLabel(String.valueOf(objModel.getEstadoCaja()));
				 * cell.setParent(item);
				*/
				
				cell = new Listcell();
				cell.setLabel(String.valueOf(objModel.getEstadoCaja()==1?"ACTIVA":"INACTIVA"));
				cell.setParent(item);
				
				cell = new Listcell();
				Button btnDelete = new Button();
				// btnDelete.set("/img/globales/16x16/info.png");
				btnDelete.setImage("/img/globales/16x16/delete.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbbancoagenciacajasid());
				btnDelete.addEventListener("onClick", eventBtnEliminar);
				/////////////////
				/*
				 * cell = new Listcell(); Button btnModificar = new Button();
				 * btnModificar.setLabel("Modificar"); cell.setParent(item);
				 * 
				 * // item para btn modificar
				 */
				cell.setParent(item);
				item.setAttribute("objmodificar", objModel);
				item.setAttribute("idseleccionado", objModel.getCbbancoagenciacajasid());
				item.addEventListener("onClick", eventBtnModificar);

				///////////
				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxConsulta);

				if (this.lbxConsulta.getItemCount() != 0) {
					this.btnRegistrar.setDisabled(false);
					this.btnModificar.setDisabled(true);
					limpiarCampos();
				} else {
					this.btnRegistrar.setDisabled(false);
					this.btnModificar.setDisabled(true);

				}
				// paramsLlenaListbox);
			}
			// listConsulta.clear();
		} else {
			//Messagebox.show("No se encontraron resultados!", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	// se crea el evento eliminar se crea una variable de atributo personalizado
	// se setea el id donde se obtiene el id

	///////
	EventListener<Event> eventBtnEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			final int idseleccionado = Integer.parseInt(event.getTarget().getAttribute("idEliminar").toString());
			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
					"ID  a eliminar = " + idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBBancoAgenciaCajasDAO objDAO = new CBBancoAgenciaCajasDAO();
								// objeDAO.elim(Integer.parseInt(idFila));
								objDAO.eliminarCajero(idseleccionado);
								// onClick$btnLimpiar();

								/// ACTUALIZA DESPUES DE ELIMINAR
								List<CBBancoAgenciaCajasModel> list = objDAO.consByIdAgencia(objModel);
								if (list.size() > 0) {
									llenaListbox(list);
									limpiarCampos();
									Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
											Messagebox.INFORMATION);

									// onClick$btnConsultar();
								} else {
									Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
											"creo el registro pero no recarga consulta ");
									limpiarListbox(lbxConsulta);
									Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
											Messagebox.INFORMATION);
									limpiarCampos();
								}

							}
						}
					});
		}
	};

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			btnModificar.setDisabled(false);
			btnRegistrar.setDisabled(true);
			// se crea una variable personalizada
			CBBancoAgenciaCajasModel objmodificar = (CBBancoAgenciaCajasModel) arg0.getTarget()
					.getAttribute("objmodificar");
			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
					"obj a modificar: " + objmodificar);
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");

			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
					"id seleccioando " + idseleccionado);

			txtCajero.setText(objmodificar.getCajero());
			txtCodCaja.setText(objmodificar.getCod_caja());
			txtCodOficina.setText(objmodificar.getCod_oficina());
			objModel.setCajero(objmodificar.getCajero());

			for (Comboitem item : cmbEstado.getItems()) {
				//Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
				//		"for en modificar " + item.getValue());
				//Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
				//		"comboESTADO abtes de if " + objmodificar.getEstadoCaja());
				if (item.getValue().toString().equals(String.valueOf(objmodificar.getEstadoCaja()))) {
					Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
							"combo ESTADO en if " + objmodificar.getEstadoCaja());
					cmbEstado.setSelectedItem(item);
				}
			}
		}
	};

	public void onClick$btnLimpiar() {
		limpiarCampos();
	}
	/// fin

	public void limpiarCampos() {

		objModel.setCbbancoagenciacajasid(0);
		// objModel.setCajero("");
		txtCajero.setText("");
		txtCodCaja.setText("");
		txtCodOficina.setText("");
		cmbEstado.setSelectedIndex(-1);
		btnModificar.setDisabled(true);
		btnRegistrar.setDisabled(false);
	}

	public void onClick$btnRegistrar() {
		String usuario;
		usuario = obtenerUsuario().getUsuario();
		try {
			if (txtCajero.getText().trim() != null && !txtCajero.getText().trim().equals("")) {
				if (txtCodCaja.getText().trim() != null && !txtCodCaja.getText().trim().equals("")) {
				//	if (txtCodOficina.getText().trim() != null && !txtCodOficina.getText().trim().equals("")) {

						if (cmbEstado.getSelectedItem() != null) {

							CBBancoAgenciaCajasDAO objeDAO = new CBBancoAgenciaCajasDAO();

							objModel.setCbcatalogoagenciaid(objModel.getCbcatalogoagenciaid());
							Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
									"cbcatalogoagenciaid en control guar " + objModel.getCbcatalogoagenciaid());

							objModel.setCajero(txtCajero.getText().trim());
							objModel.setCod_caja(txtCodCaja.getText().trim());
							objModel.setCod_oficina(txtCodOficina.getText().trim());

							objModel.setEstadoCaja(Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString()));
							objModel.setCreador(usuario);

							int PKCajas = objeDAO.obtenerPKCajas();
							Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
									"pk afiliacion en guard " + PKCajas);
							if (objeDAO.transaccionValida(objModel)) {

								if (objeDAO.insertarCajero(objModel, PKCajas)) {
									onClick$btnConsultar();
									limpiarCampos();
									Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK,
											Messagebox.INFORMATION);
								} else {
									Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
											Messagebox.EXCLAMATION);
								}
							} else {
								Messagebox.show(
										"La caja ingresada ya se encuentra registrada para la agencia seleccionada.",
										"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
							}
						} else {
							Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
									Messagebox.OK, Messagebox.EXCLAMATION);
						}
					/*} else {
						Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
								Messagebox.OK, Messagebox.EXCLAMATION);
					}*/
				} else {
					Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}
			} else {
				Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", "ATENCIÓN", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	public void onClick$btnModificar() {

		CBBancoAgenciaCajasDAO objeDAO = new CBBancoAgenciaCajasDAO();
		usuario = obtenerUsuario().getUsuario();
		try {
			System.out.println("id seleccionado en condicion " + idseleccionado);
			if (idseleccionado > 0) {
				if (txtCajero.getText() != null && !txtCajero.getText().trim().equals("")) {
					if (txtCodCaja.getText() != null && !txtCodCaja.getText().trim().equals("")) {
					//	if (txtCodOficina.getText() != null && !txtCodOficina.getText().trim().equals("")) {

							if (cmbEstado.getSelectedItem() != null) {

								objModel.setCbcatalogoagenciaid(objModel.getCbcatalogoagenciaid());
								Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.INFO,
										"cbcatalogoagenciaid en control guar " + objModel.getCbcatalogoagenciaid());

								objModel.setEstadoCaja(
										Integer.parseInt(cmbEstado.getSelectedItem().getValue().toString()));
								// objModel.setUsuario(objModel.getUsuario());

								objModel.setCbbancoagenciacajasid(idseleccionado);
								Logger.getLogger(CBBancoAgenciaAfiliacionesController.class.getName()).log(Level.INFO,
										"id seleccion modificar cbbancoagenciaafiliacion "
												+ objModel.getCbbancoagenciacajasid());

								if (txtCajero.getText().equals(objModel.getCajero().trim())) {
									objModel.setCajero(txtCajero.getText().trim());
									objModel.setCod_caja(txtCodCaja.getText().trim());
									objModel.setCod_oficina(txtCodOficina.getText().trim());
									objModel.setCreador(usuario);
									System.out.println("txtcajero  modi " + txtCajero.getText());
									System.out.println("getcajero modi  " + objModel.getCajero());
									if (objeDAO.actualizarCajero(objModel, idseleccionado)) {
										onClick$btnConsultar();
										limpiarCampos();
										Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK,
												Messagebox.INFORMATION);
									} else {
										Messagebox.show("No se pudo completar la operación", "ATENCIÓN", Messagebox.OK,
												Messagebox.EXCLAMATION);
									}
								} else {
									objModel.setCajero(txtCajero.getText().trim());
									objModel.setCod_caja(txtCodCaja.getText().trim());
									objModel.setCod_oficina(txtCodOficina.getText().trim());
									objModel.setCreador(usuario);
									System.out.println("entra a comprobar caja");
									if (objeDAO.transaccionValida(objModel)) {
										if (objeDAO.actualizarCajero(objModel, idseleccionado)) {
											onClick$btnConsultar();
											limpiarCampos();
											Messagebox.show("Operación exitosa", "ATENCIÓN", Messagebox.OK,
													Messagebox.INFORMATION);
										} else {
											Messagebox.show("No se pudo completar la operación", "ATENCIÓN",
													Messagebox.OK, Messagebox.EXCLAMATION);
										}
									} else {
										Messagebox.show(
												"La caja ingresada ya se encuentra registrada para la agencia seleccionada.",
												"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
									}
								}
							} else {
								Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!",
										"ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
							}
					/*	} else {
							Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
									Messagebox.OK, Messagebox.EXCLAMATION);
						}*/
					} else {
						Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
								Messagebox.OK, Messagebox.EXCLAMATION);
					}
				} else {
					Messagebox.show("¡Complete todos los campos antes de ejecutar la operación!", "ADVERTENCIA",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}

			} else {
				Messagebox.show("¡Seleccione una caja!", "ADVERTENCIA", Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			Logger.getLogger(CBCatalogoAgenciaCajasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error. Revise los datos ingresados.", "ATENCIÓN", Messagebox.OK,
					Messagebox.ERROR);

		}

	}

	// Usuario que inicia sesión
	private String usuario;

}
