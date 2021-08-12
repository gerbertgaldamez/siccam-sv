package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBMantenimientoTipologiasPolizaDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBMantenimientoPolizaModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;

/**
 * @author ovidio santos
 */
public class CBTipologiasPolizaController extends ControladorBase {

	private static final long serialVersionUID = 1L;
	private Textbox tbxCosto;
	private Textbox tbxContrapartida;
	private Textbox tbxTerminacion;
	private Textbox tbxContabilizacion;
	private Textbox tbxIva;
	private Textbox tbxActividad;
	private Textbox tbxDescripcion;
	private Textbox tbxNombre;

	private Textbox tbxCostocp;
	private Textbox tbxTerminacioncp;
	private Textbox tbxDescripcioncp;
	private Textbox tbxContabilizacioncp;
	private Textbox tbxCuenta_Ingreso;

	private Textbox tbxCostodf;
	private Textbox tbxTerminaciondf;
	private Textbox tbxDescripciondf;
	private Textbox tbxContabilizaciondf;
	private Textbox tbxCuenta_Ingresodf;
	private Textbox tbxClaveDiferenciaNegativa;
	private Textbox tbxIndicadorIvacp;
	private Textbox tbxActividadcp;
	private Textbox tbxSecuencia;
	
	private Textbox tbxTipoDocumento;
	private Textbox tbxIdentificacion;

	// private Intbox ibxTipo;
	// private Intbox ibxEntidad;
	Button btnConsutar;
	Button btnNuevo;
	Button btnDelete;
	Button btnModificar;
	Button btnAsignarEntidades; //CarlosGodinez -> 13/08/2018
	Button btnLimpiar;
	Button btnExcel;
	Listbox lbxConsulta;
	int idseleccionado = 0;
	Combobox cmbConvenio;
	Combobox cmbTipo;
	Combobox cmbPideEntidad;
	
	@SuppressWarnings("unused")
	private boolean banderaEntidadSelected;
	private String usuario;
	private CBMantenimientoTipologiasPolizaDAO cbaDao = new CBMantenimientoTipologiasPolizaDAO();
	private CBMantenimientoPolizaModel objModel = new CBMantenimientoPolizaModel();
	private List<CBParametrosGeneralesModel> lstTipologiasPolizas = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBParametrosGeneralesModel> lstPideEntidad = new ArrayList<CBParametrosGeneralesModel>();

	public void doAfterCompose(Component param) {
		try {
			super.doAfterCompose(param);
			llenaListbox(cbaDao.obtenerTipologias(objModel));
			btnModificar.setDisabled(true);
			btnAsignarEntidades.setDisabled(true);
			usuario = obtenerUsuario().getUsuario();
			llenaComboTipo();
			llenaComboConvenio();
			llenaComboPideEntidad();
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * Llenado de combo tipo
	 */
	public void llenaComboConvenio() {
		CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
		lstTipologiasPolizas = objeDAO.obtenerParamConvenios();
		if (lstTipologiasPolizas != null && lstTipologiasPolizas.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = lstTipologiasPolizas.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();
				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto1());
				item.setParent(cmbConvenio);
			}
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"- Llena combo tipo");
		} else {
			Messagebox.show("Error al cargar la configuracion de tipos ", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	

	public void llenaComboTipo() {
		Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
				"Llena combo tipo estado");
		limpiaCombobox(cmbConvenio);
		CBMantenimientoTipologiasPolizaDAO objeDAO = new CBMantenimientoTipologiasPolizaDAO();
		this.lstTipologiasPolizas = objeDAO.obtenerTipo("TIPO_IMPUESTO");
		if (lstTipologiasPolizas.size() > 0) {
			for (CBParametrosGeneralesModel d : this.lstTipologiasPolizas) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbTipo);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			}
		}
	}

	public void llenaComboPideEntidad() {
		Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
				"Llena combo tipo estado");
		limpiaCombobox(cmbPideEntidad);

		CBMantenimientoTipologiasPolizaDAO objeDAO = new CBMantenimientoTipologiasPolizaDAO();
		this.lstPideEntidad = objeDAO.obtenerPideEntidad("ESTADO");
		if (lstPideEntidad.size() > 0) {
			for (CBParametrosGeneralesModel d : this.lstPideEntidad) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbPideEntidad);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());

			}
		}

	}
	

	
	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}
	
	public void onClick$btnNuevo() {
		try {
			if (!this.tbxNombre.getText().trim().equals("")) {
				CBMantenimientoTipologiasPolizaDAO objDAO = new CBMantenimientoTipologiasPolizaDAO();

				CBMantenimientoPolizaModel objModel = new CBMantenimientoPolizaModel();

				String user = usuario;
				objModel.setCentroCosto(tbxCosto.getText().trim());
				objModel.setCuentaContrapartida(tbxContrapartida.getText().trim());
				objModel.setClaveContabilizacion(tbxContabilizacion.getText().trim());
				objModel.setDescripcion(tbxDescripcion.getText().trim());
				objModel.setTerminacion(tbxTerminacion.getText().trim());

				objModel.setCuenta_Ingreso(tbxCuenta_Ingreso.getText().trim());

				objModel.setNombre(tbxNombre.getText().trim());
				objModel.setCentroCostocp(tbxCostocp.getText().trim());
				objModel.setClaveContabilizacioncp(tbxContabilizacioncp.getText().trim());
				objModel.setDescripcioncp(tbxDescripcioncp.getText().trim());
				objModel.setTerminacioncp(tbxTerminacioncp.getText().trim());

				objModel.setIndicadorIva(tbxIva.getText().trim());
				objModel.setActividad(tbxActividad.getText().trim());

				objModel.setCentroCostodf(tbxCostodf.getText().trim());
				objModel.setClaveContabilizaciondf(tbxContabilizaciondf.getText().trim());
				objModel.setDescripciondf(tbxDescripciondf.getText().trim());
				objModel.setTerminaciondf(tbxTerminaciondf.getText().trim());
				objModel.setCuenta_Ingresodf(tbxCuenta_Ingresodf.getText().trim());
				objModel.setClaveDiferenciaNegativa(tbxClaveDiferenciaNegativa.getText().trim());
				objModel.setIndicadorivacp(tbxIndicadorIvacp.getText().trim());
				objModel.setActividadcp(tbxActividadcp.getText().trim());
				objModel.setSecuencia(tbxSecuencia.getText().trim());
				objModel.setTipodocumento(tbxTipoDocumento.getText().trim());
				objModel.setIdentificacion(tbxIdentificacion.getText().trim());
				objModel.setUsuario(user);
				if (cmbConvenio.getSelectedItem() != null) {
					objModel.setConvenio(cmbConvenio.getSelectedItem().getValue().toString());
				} else {
					objModel.setConvenio(null);
				}
				
				if (cmbTipo.getSelectedItem() != null) {
					objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString());
				} else {
					objModel.setTipo(null);
				}
				
				if (cmbPideEntidad.getSelectedItem() != null) {
					objModel.setPide_Entidad(cmbPideEntidad.getSelectedItem().getValue().toString());
				} else {
					objModel.setPide_Entidad(null);
				}

				if (objDAO.insertTipologiasPoliza(objModel)) {
					Messagebox.show("Se creo el registro con exito", "ATENCION", Messagebox.OK,
							Messagebox.INFORMATION);

					CBMantenimientoPolizaModel objModel1 = new CBMantenimientoPolizaModel();
					onClick$btnLimpiar();
					llenaListbox(cbaDao.obtenerTipologias(objModel1));
				}
			} else {
				Messagebox.show("El campo nombre es obligatorio para crear tipologias!", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnModificar() {
		try {
			if (idseleccionado > 0) {
				if (!this.tbxNombre.getText().trim().equals("")) {

					CBMantenimientoTipologiasPolizaDAO objDAO = new CBMantenimientoTipologiasPolizaDAO();
					CBMantenimientoPolizaModel objModel = new CBMantenimientoPolizaModel();
					String user = usuario;
					objModel.setCentroCosto(tbxCosto.getText().trim());
					objModel.setClaveContabilizacion(tbxContabilizacion.getText().trim());
					objModel.setDescripcion(tbxDescripcion.getText().trim());
					objModel.setTerminacion(tbxTerminacion.getText().trim());
					objModel.setCuenta_Ingreso(tbxCuenta_Ingreso.getText().trim());
					objModel.setNombre(tbxNombre.getText().trim());

					objModel.setCentroCostocp(tbxCostocp.getText().trim());
					objModel.setClaveContabilizacioncp(tbxContabilizacioncp.getText().trim());
					objModel.setDescripcioncp(tbxDescripcioncp.getText().trim());
					objModel.setTerminacioncp(tbxTerminacioncp.getText().trim());
					objModel.setCuentaContrapartida(tbxContrapartida.getText().trim());
					objModel.setIndicadorIva(tbxIva.getText().trim());

					objModel.setActividad(tbxActividad.getText().trim());

					objModel.setCentroCostodf(tbxCostodf.getText().trim());
					objModel.setClaveContabilizaciondf(tbxContabilizaciondf.getText().trim());
					objModel.setDescripciondf(tbxDescripciondf.getText().trim());
					objModel.setTerminaciondf(tbxTerminaciondf.getText().trim());
					objModel.setCuenta_Ingresodf(tbxCuenta_Ingresodf.getText().trim());
					objModel.setClaveDiferenciaNegativa(tbxClaveDiferenciaNegativa.getText().trim());
					objModel.setIndicadorivacp(tbxIndicadorIvacp.getText().trim());
					objModel.setActividadcp(tbxActividadcp.getText().trim());
					objModel.setSecuencia(tbxSecuencia.getText().trim());
					objModel.setTipodocumento(tbxTipoDocumento.getText().trim());
					objModel.setIdentificacion(tbxIdentificacion.getText().trim());
					objModel.setCbtipologiaspolizaid(idseleccionado);
					objModel.setUsuario(user);

					if (cmbConvenio.getSelectedItem() != null) {

						objModel.setConvenio(cmbConvenio.getSelectedItem().getValue().toString());

					} else {
						objModel.setConvenio(null);
					}
					
					if (cmbTipo.getSelectedItem() != null) {

						objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString());

					} else {
						objModel.setTipo(null);
					}
					
					if (cmbPideEntidad.getSelectedItem() != null) {

						objModel.setPide_Entidad(cmbPideEntidad.getSelectedItem().getValue().toString());
					} else {
						objModel.setPide_Entidad(null);
					}

					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
							"id en el controlador de modificar " + idseleccionado);
					if (objDAO.update(objModel, idseleccionado)) {
						// llenaListbox();

						Messagebox.show("Se actualizo el registro", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);

						CBMantenimientoPolizaModel objModel1 = new CBMantenimientoPolizaModel();
						onClick$btnLimpiar();
						llenaListbox(cbaDao.obtenerTipologias(objModel1));

					} else {
						Messagebox.show("No se Modifico Tipologia!", "ATENCION", Messagebox.OK,
								Messagebox.EXCLAMATION);
					}

				} else {
					Messagebox.show("El campo nombre es obligatorio para modificar!", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);

				}
			} else {
				Messagebox.show("Seleccione una tipologia a modificar!", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnLimpiar() {
		limpiarTextbox();
		btnModificar.setDisabled(true);
		btnAsignarEntidades.setDisabled(true);
		btnNuevo.setDisabled(false);
	}

	public void limpiarTextbox() {
		if (tbxNombre != null) {

			tbxCosto.setText("");
			tbxContabilizacion.setText("");
			tbxContrapartida.setText("");
			tbxIva.setText("");
			tbxTerminacion.setText("");
			tbxActividad.setText("");
			tbxDescripcion.setText("");
			tbxNombre.setText("");
			tbxCostocp.setText("");
			tbxContabilizacioncp.setText("");
			tbxDescripcioncp.setText("");
			tbxTerminacioncp.setText("");
			tbxCuenta_Ingreso.setText("");

			tbxCostodf.setText("");
			tbxContabilizaciondf.setText("");
			tbxDescripciondf.setText("");
			tbxTerminaciondf.setText("");
			tbxCuenta_Ingresodf.setText("");
			tbxClaveDiferenciaNegativa.setText("");
			tbxIndicadorIvacp.setText("");
			tbxActividadcp.setText("");
			cmbPideEntidad.setSelectedIndex(-1);
			cmbConvenio.setSelectedIndex(-1);
			cmbTipo.setSelectedIndex(-1);
			tbxSecuencia.setText("");
			tbxIdentificacion.setText("");
			tbxTipoDocumento.setText("");
		}
	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}

	// metodo consulta
	public void onClick$btnConsultar() {

		try {
			CBMantenimientoTipologiasPolizaDAO cbaDao = new CBMantenimientoTipologiasPolizaDAO();
			CBMantenimientoPolizaModel objModel = new CBMantenimientoPolizaModel();
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"Consulta estados tipologias");
			if (cmbConvenio.getSelectedItem() != null) {
				objModel.setConvenio(cmbConvenio.getSelectedItem().getValue().toString());
			}
			if (cmbTipo.getSelectedItem() != null) {
				objModel.setTipo(cmbTipo.getSelectedItem().getValue().toString());
			}
			
			if (cmbPideEntidad.getSelectedItem() != null) {
				objModel.setPide_Entidad(cmbPideEntidad.getSelectedItem().getValue().toString());
			}
			if (tbxCosto.getText().trim() != null && !"".equals(tbxCosto.getText().trim())) {
				objModel.setCentroCosto(tbxCosto.getText().trim());
			}
			if (tbxContabilizacion.getText().trim() != null && !"".equals(tbxContabilizacion.getText().trim())) {
				objModel.setClaveContabilizacion(tbxContabilizacion.getText().trim());
			}
			if (tbxDescripcion.getText().trim() != null && !"".equals(tbxDescripcion.getText().trim())) {
				objModel.setDescripcion(tbxDescripcion.getText().trim());
			}
			if (tbxTerminacion.getText().trim() != null && !"".equals(tbxTerminacion.getText().trim())) {
				objModel.setTerminacion(tbxTerminacion.getText().trim());
			}
			if (tbxCuenta_Ingreso.getText().trim() != null && !"".equals(tbxCuenta_Ingreso.getText().trim())) {
				objModel.setCuenta_Ingreso(tbxCuenta_Ingreso.getText().trim());
			}
			if (tbxNombre.getText().trim() != null && !"".equals(tbxNombre.getText().trim())) {
				objModel.setNombre(tbxNombre.getText().trim());
			}
			if (tbxCostocp.getText().trim() != null && !"".equals(tbxCostocp.getText().trim())) {
				objModel.setCentroCostocp(tbxCostocp.getText().trim());
			}
			if (tbxContabilizacioncp.getText().trim() != null && !"".equals(tbxContabilizacioncp.getText().trim())) {
				objModel.setClaveContabilizacioncp(tbxContabilizacioncp.getText().trim());
			}
			if (tbxDescripcioncp.getText().trim() != null && !"".equals(tbxDescripcioncp.getText().trim())) {
				objModel.setDescripcioncp(tbxDescripcioncp.getText().trim());
			}
			if (tbxTerminacioncp.getText().trim() != null && !"".equals(tbxTerminacioncp.getText().trim())) {
				objModel.setTerminacioncp(tbxTerminacioncp.getText().trim());
			}
			if (tbxContrapartida.getText().trim() != null && !"".equals(tbxContrapartida.getText().trim())) {
				objModel.setCuentaContrapartida(tbxContrapartida.getText().trim());
			}
			if (tbxIva.getText().trim() != null && !"".equals(tbxIva.getText().trim())) {
				objModel.setIndicadorIva(tbxIva.getText().trim());
			}
			if (tbxActividad.getText().trim() != null && !"".equals(tbxActividad.getText().trim())) {
				objModel.setActividad(tbxActividad.getText().trim());
			}
			if (tbxCostodf.getText().trim() != null && !"".equals(tbxCostodf.getText().trim())) {
				objModel.setCentroCostodf(tbxCostodf.getText().trim());
			}
			if (tbxContabilizaciondf.getText().trim() != null && !"".equals(tbxContabilizaciondf.getText().trim())) {
				objModel.setClaveContabilizaciondf(tbxContabilizaciondf.getText().trim());
			}
			if (tbxDescripciondf.getText().trim() != null && !"".equals(tbxDescripciondf.getText().trim())) {
				objModel.setDescripciondf(tbxDescripciondf.getText().trim());
			}
			if (tbxTerminaciondf.getText().trim() != null && !"".equals(tbxTerminaciondf.getText().trim())) {
				objModel.setTerminaciondf(tbxTerminaciondf.getText().trim());
			}
			if (tbxCuenta_Ingresodf.getText().trim() != null && !"".equals(tbxCuenta_Ingresodf.getText().trim())) {
				objModel.setCuenta_Ingresodf(tbxCuenta_Ingresodf.getText().trim());
			}
			if (tbxClaveDiferenciaNegativa.getText().trim() != null
					&& !"".equals(tbxClaveDiferenciaNegativa.getText().trim())) {
				objModel.setClaveDiferenciaNegativa(tbxClaveDiferenciaNegativa.getText().trim());
			}
			if (tbxIndicadorIvacp.getText().trim() != null && !"".equals(tbxIndicadorIvacp.getText().trim())) {
				objModel.setIndicadorivacp(tbxIndicadorIvacp.getText().trim());
			}
			if (tbxActividadcp.getText().trim() != null && !"".equals(tbxActividadcp.getText().trim())) {
				objModel.setActividadcp(tbxActividadcp.getText().trim());
			}
			if (tbxSecuencia.getText().trim() != null && !"".equals(tbxSecuencia.getText().trim())) {
				objModel.setSecuencia(tbxSecuencia.getText().trim());
			}
			if (tbxTipoDocumento.getText().trim() != null && !"".equals(tbxTipoDocumento.getText().trim())) {
				objModel.setTipodocumento(tbxTipoDocumento.getText().trim());
			}
			if (tbxIdentificacion.getText().trim() != null && !"".equals(tbxIdentificacion.getText().trim())) {
				objModel.setIdentificacion(tbxIdentificacion.getText().trim());
			}
			//limpiarTextbox();
			llenaListbox(cbaDao.obtenerTipologias(objModel));
			if (this.lbxConsulta.getItemCount() != 0) {
				btnNuevo.setDisabled(false);
				btnModificar.setDisabled(true);
				btnAsignarEntidades.setDisabled(true);
			}
			
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBMantenimientoPolizaModel> list) {
		limpiarListbox(lbxConsulta);
		this.btnExcel.setDisabled(true);
		Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
				"cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			this.btnExcel.setDisabled(false);
			Iterator<CBMantenimientoPolizaModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;

			// metodo para llenar listbox
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(String.valueOf(objModel.getCbtipologiaspolizaid()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCentroCosto());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getClaveContabilizacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getDescripcion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTerminacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCuenta_Ingreso());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombre());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCentroCostocp());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getClaveContabilizacioncp());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getDescripcioncp());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTerminacioncp());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCuentaContrapartida());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getIndicadorIva());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getActividad());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(objModel.getCentroCostodf());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getClaveContabilizaciondf());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getDescripciondf());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTerminaciondf());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCuenta_Ingresodf());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getClaveDiferenciaNegativa());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getConvenio());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getPide_Entidad());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getIndicadorivacp());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getActividadcp());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getSecuencia());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(objModel.getTipodocumento());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(objModel.getIdentificacion());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(objModel.getTipo());
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/delete_1.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("idEliminar", objModel.getCbtipologiaspolizaid());
				btnDelete.addEventListener(Events.ON_CLICK, eventBtnEliminar);

				// item para btn modificar
				item.setAttribute("objmodificar", objModel);

				item.setAttribute("idseleccionado", objModel.getCbtipologiaspolizaid());
				item.addEventListener(Events.ON_CLICK, eventBtnModificar);

				item.setAttribute("objModelModal", objModel);

				item.addEventListener(Events.ON_DOUBLE_CLICK, eventBtnParamAdicionalesModal);

				item.setValue(objModel);

				item.setTooltip("popAsociacionModal");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxConsulta);
			}
			list.clear();
		} else {
		}
	}

	/**
	 * Agregado por CarlosGodinez -> 13/08/2018
	 * Levantar modal para asociacion de entidades a tipologias de poliza
	 * */
	public void onClick$btnAsignarEntidades() {
		try {
			session.setAttribute("tipologiaSeleccionada", lbxConsulta.getSelectedItem().getValue());
			Executions.createComponents("/cbmodaltipologiaentidad.zul", null, null);
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			btnModificar.setDisabled(false);
			btnAsignarEntidades.setDisabled(false); //CarlosGodinez -> 13/08/2018
			btnNuevo.setDisabled(true);
			limpiarTextbox();
			// se crea una variable personalizada
			CBMantenimientoPolizaModel objmodificar = (CBMantenimientoPolizaModel) arg0.getTarget()
					.getAttribute("objmodificar");
			idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");

			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"id seleccioando " + idseleccionado);

			tbxCosto.setText(objmodificar.getCentroCosto());
			tbxContabilizacion.setText(objmodificar.getClaveContabilizacion());
			tbxContrapartida.setText(objmodificar.getCuentaContrapartida());
			tbxIva.setText(objmodificar.getIndicadorIva());
			tbxTerminacion.setText(objmodificar.getTerminacion());
			tbxActividad.setText(objmodificar.getActividad());
			tbxDescripcion.setText(objmodificar.getDescripcion());
			tbxNombre.setText(objmodificar.getNombre());
			tbxCostocp.setText(objmodificar.getCentroCostocp());
			tbxContabilizacioncp.setText(objmodificar.getClaveContabilizacioncp());
			tbxDescripcioncp.setText(objmodificar.getDescripcioncp());
			tbxTerminacioncp.setText(objmodificar.getTerminacioncp());
			tbxCuenta_Ingreso.setText(objmodificar.getCuenta_Ingreso());

			tbxCostodf.setText(objmodificar.getCentroCostodf());
			tbxContabilizaciondf.setText(objmodificar.getClaveContabilizaciondf());
			tbxDescripciondf.setText(objmodificar.getDescripciondf());
			tbxTerminaciondf.setText(objmodificar.getTerminaciondf());
			tbxCuenta_Ingresodf.setText(objmodificar.getCuenta_Ingresodf());
			tbxClaveDiferenciaNegativa.setText(objmodificar.getClaveDiferenciaNegativa());
			tbxIndicadorIvacp.setText(objmodificar.getIndicadorivacp());
			tbxActividadcp.setText(objmodificar.getActividadcp());
			tbxSecuencia.setText(objmodificar.getSecuencia());
			tbxTipoDocumento.setText(objmodificar.getTipodocumento());
			tbxIdentificacion.setText(objmodificar.getIdentificacion());
			// ibxTipo.setValue(objmodificar.getTipo());
			// ibxEntidad.setValue(objmodificar.getPideEntidad());

			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"combotipo abtes de if " + objmodificar.getConvenio());
			for (Comboitem item : cmbConvenio.getItems()) {
				if (item.getLabel().equals(String.valueOf(objmodificar.getConvenio()))) {
					cmbConvenio.setSelectedItem(item);
				}
			}
			for (Comboitem item : cmbTipo.getItems()) {
				if (item.getLabel().equals(String.valueOf(objmodificar.getTipo()))) {
					cmbTipo.setSelectedItem(item);
				}
			}
			
			for (Comboitem item : cmbPideEntidad.getItems()) {
				if (item.getLabel().equals(String.valueOf(objmodificar.getPide_Entidad()))) {
					cmbPideEntidad.setSelectedItem(item);
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
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"ID  a eliminar = " + idseleccionado);
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBMantenimientoTipologiasPolizaDAO objDAO = new CBMantenimientoTipologiasPolizaDAO();
								if (objDAO.delete(idseleccionado)) {
									onClick$btnLimpiar();
									CBMantenimientoPolizaModel objModel = new CBMantenimientoPolizaModel();
									limpiarTextbox();
									llenaListbox(cbaDao.obtenerTipologias(objModel));
									Messagebox.show("Registros eliminado con exito.", "ATENCION", Messagebox.OK,
											Messagebox.INFORMATION);
								}
							}
						}
					});
			onClick$btnLimpiar();
		}
	};


	/**
	 * Levantar modal para registrar nuevos parametros
	 */
	/*
	 * public void onClick$btnNuevosParametrosTipologias () { try {
	 * 
	 * Executions.createComponents("/cbTipologiasPolizaModal.zul",null,null); }
	 * catch (Exception e) {
	 * Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.
	 * SEVERE, null, e); Messagebox.show("Ha ocurrido un error", "ATENCION",
	 * Messagebox.OK, Messagebox.ERROR); } }
	 * 
	 * /* //////////////////////// /** Evento que se dispara al seleccionar un
	 * registro de entidad de la Listbox principal Seteo de valores en componentes
	 * de modal
	 */
	EventListener<Event> eventBtnParamAdicionalesModal = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				banderaEntidadSelected = true;
				CBMantenimientoPolizaModel objModelModal = (CBMantenimientoPolizaModel) event.getTarget()
						.getAttribute("objModelModal");
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
						"\n**** Tipologia de poliza seleccionada ****\n");
				session.setAttribute("objModelModal", objModelModal);
				session.setAttribute("interfaceTarjeta", CBTipologiasPolizaController.this);
				Executions.createComponents("/cbTipologiasPolizaModal.zul", null, null);

			} catch (Exception e) {
				Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};

	public void recargaConsultaConta(CBMantenimientoPolizaModel objModel) {
		Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
				"Entra a recargar consulta...");
		CBMantenimientoTipologiasPolizaDAO objDao = new CBMantenimientoTipologiasPolizaDAO();
		llenaListbox(objDao.obtenerTipologias(objModel));
		onClick$btnLimpiar();

	}

	/*
	 * metodo para generar reporte general Ovidio Santos
	 */

	public void onClick$btnExcel() throws IOException {
		Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
				"Generando reporte ...");
		BufferedWriter bw = null;
		try {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
			// Creamos el encabezado
			String encabezado = "ID TIPOLOGIA|CENTRO COSTO|CLAVE CONTABILIZACION|DESCRIPCION|TERMINACION|CUENTA INGRESO|NOMBRE|CENTRO COSTO CP|CLAVE CONTABILIZACION CP|DESCRIPCION CP|TERMINACION CP|CUENTA CONTRAPARTIDA|"
					+ "INDICADOR IVA|ACTIVIDAD|CENTRO COSTO DF|CLAVE CONTABILIZACION DF|"
					+ " DESCRIPCION DF|TERMINACION DF|CUENTA INGRESO DF|CLAVE DIFERENCIA NEGATIVA|INDICADOR IV CP|ACTIVIDAD CP|CONVENIO|PIDE ENTIDAD|CENTRO BENEFICIO|DIVISION|ORDEN DE PROYECTO|TIPO DE CAMBIO|FECHA DE CONVERSION|INDICADOR CME|CAR PA SEGMENTO|CAR PA SERVICIO|CAR PA TIPO TRAFICO|CAR PA AMBITO|CAR PA LICENCIA|CAR PA REGION|SUN TIPO LINEA|CANAL|BUNDLE|"
					+ " PRODUCTO|EMPRESA GRUPO|SECUENCIA|TIPO_DOCUMENTO|IDENTIFICACION|TIPO_IMPUESTO\n";
			File archivo = new File("reporte_tipologias_poliza_" + sdf.format(fecha) + ".csv");

			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(encabezado);
			for (Listitem iterator : this.lbxConsulta.getItems()) {
				CBMantenimientoPolizaModel registro = (CBMantenimientoPolizaModel) iterator.getValue();

				bw.write((registro.getCbtipologiaspolizaid()) + "|" + changeNull(registro.getCentroCosto()).trim() + "|"
						+ changeNull(registro.getClaveContabilizacion()).trim() + "|"
						+ changeNull(registro.getDescripcion()).trim() + "|'"
						+ changeNull(registro.getTerminacion()).trim().replace(",", "") + "|"
						+ changeNull(registro.getCuenta_Ingreso()).trim() + "|"
						+ changeNull(registro.getNombre()).trim() + "|'"
						+ changeNull(registro.getCentroCostocp()).trim() + "|"
						+ changeNull(registro.getClaveContabilizacioncp()).trim() + "|"
						+ changeNull(registro.getDescripcioncp()).trim() + "|"
						+ changeNull(registro.getTerminacioncp()).trim() + "|"
						+ changeNull(registro.getCuentaContrapartida()).trim() + "|"
						+ changeNull(registro.getIndicadorIva()).trim() + "|"
						+ changeNull(registro.getActividad()).trim() + "|"
						+ changeNull(registro.getCentroCostodf()).trim() + "|"
						+ changeNull(registro.getClaveContabilizaciondf()).trim() + "|"
						+ changeNull(registro.getDescripciondf()).trim() + "|"
						+ changeNull(registro.getTerminaciondf()).trim() + "|"
						+ changeNull(registro.getCuenta_Ingresodf()).trim() + "|"
						+ changeNull(registro.getClaveDiferenciaNegativa()).trim() + "|"
						+ changeNull(registro.getIndicadorivacp()).trim() + "|"
						+ changeNull(registro.getActividadcp()).trim() + "|" + changeNull(registro.getConvenio()).trim()
						+ "|" + changeNull(registro.getPide_Entidad()).trim() + "|"
						+ changeNull(registro.getCentrodebeneficio()).trim() + "|"
						+ changeNull(registro.getDivision()).trim() + "|"
						+ changeNull(registro.getOrdendeproyecto()).trim() + "|"
						+ changeNull(registro.getTipodecambio()).trim() + "|"
						+ changeNull(registro.getFechadecomversion()).trim() + "|"
						+ changeNull(registro.getIndicadorcme()).trim() + "|"
						+ changeNull(registro.getCarpasegmento()).trim() + "|"
						+ changeNull(registro.getCarpaservicio()).trim() + "|"
						+ changeNull(registro.getCarpatipotrafico()).trim() + "|"
						+ changeNull(registro.getCarpaambito()).trim() + "|"
						+ changeNull(registro.getCarpalicencia()).trim() + "|"
						+ changeNull(registro.getCarparegion()).trim() + "|"
						+ changeNull(registro.getSubtipolinea()).trim() + "|" + changeNull(registro.getCanal()).trim()
						+ "|" + changeNull(registro.getBundle()).trim() + "|"
						+ changeNull(registro.getProducto()).trim() + "|"
						+ changeNull(registro.getEmpresagrupo()).trim() + "|"
								+ changeNull(registro.getSecuencia()).trim() + "|"
										+ changeNull(registro.getTipodocumento()).trim() + "|"
												+ changeNull(registro.getIdentificacion()).trim() + "|"
						+ changeNull(registro.getTipo()).trim() + "\n");
			}

			Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.INFO,
					"Descarga exitosa del archivo generado...");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCIÓN",
					Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();
		} catch (IOException e) {
			Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (bw != null)
				bw.close();
		}
	}

	public String changeNull(String cadena) {
		if (cadena == null) {
			return " ";
		} else {
			return cadena;
		}
	}

}
