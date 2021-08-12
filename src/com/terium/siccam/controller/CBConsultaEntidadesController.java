package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.implement.CBRecargaListboxEntidadImpl;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;

/**
 * @author Carlos Godinez - 16/10/2017
 */
public class CBConsultaEntidadesController extends ControladorBase implements CBRecargaListboxEntidadImpl {
	private static final long serialVersionUID = 9176164927878418930L;

	// Componentes
	Combobox cmbAgrupacion;
	Textbox txtNombre;
	Combobox cmbEstado;
	Textbox txtCuentaContable;
	Textbox txtMoneda;
	Textbox txtCodigoColector;
	Textbox txtNit;
	Listbox lbxConsulta;

	// Arraylist para llenado de combobox y listbox
	private List<CBCatalogoBancoModel> listaAgrupacionesCmb = new ArrayList<CBCatalogoBancoModel>();
	private List<CBParametrosGeneralesModel> listaEstadoCmb = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBCatalogoAgenciaModel> listaConsulta = new ArrayList<CBCatalogoAgenciaModel>();
	// Propiedades
	private boolean banderaEntidadSelected;
	static Window nuevaAgenciaComercial;
	String agrupacion = "";
	String entidad = "";
	String codigoColector = "";
	String nit = "";
	String cuentaContable = "";
	String estado = "";

	public void doAfterCompose(Component param) {
		try {
			super.doAfterCompose(param);
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.INFO,
					"\n*** Entra a pantalla de consulta de entidades ***\n");
			llenaComboAgrupaciones();
			llenaComboEstado();
			banderaEntidadSelected = false;
			this.btnExcel.setDisabled(true);
			llenaListbox(agrupacion, entidad, cuentaContable, estado, codigoColector, nit);
			
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * Metodos para llenado de Combobox al cargar vista
	 */

	public void llenaComboAgrupaciones() {

		CBCatalogoAgenciaDAO objeDAO = new CBCatalogoAgenciaDAO();
		listaAgrupacionesCmb = objeDAO.obtieneListaAgrupaciones();
		if (listaAgrupacionesCmb.size() > 0) {
			for (CBCatalogoBancoModel d : listaAgrupacionesCmb) {
				Comboitem item = new Comboitem();
				item.setParent(cmbAgrupacion);
				item.setValue(d.getCbcatalogobancoid());
				item.setLabel(d.getNombre());
			}
			System.out.println("- Llena combo de agrupaciones");
		}
	}

	public void llenaComboEstado() {
		CBCatalogoAgenciaDAO objeDAO = new CBCatalogoAgenciaDAO();
		listaEstadoCmb = objeDAO.obtenerEstadoCmb();
		if (listaEstadoCmb.size() > 0) {
			for (CBParametrosGeneralesModel d : listaEstadoCmb) {
				Comboitem item = new Comboitem();
				item.setParent(cmbEstado);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());
			}
			System.out.println("- Llena combo de estado");
		}
	}

	/**
	 * Levantar modal para registrar nueva entidad
	 */
	public void onClick$btnNuevaEntidad() {
		try {
			session.setAttribute("objEntidadSelected", null);
			session.setAttribute("cmbListAgrupaciones", listaAgrupacionesCmb);
			session.setAttribute("cmbListEstado", listaEstadoCmb);
			session.setAttribute("ifcEntidades", CBConsultaEntidadesController.this);
			Executions.createComponents("/cbentidadmodal.zul", null, null);
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	/**
	 * Metodos para llenado de Listbox consulta general
	 */
	public void onClick$btnConsultar() {
		banderaEntidadSelected = false;
		String agrupacion = "";
		String entidad = "";
		String codigoColector = "";
		String nit = "";
		String cuentaContable = "";
		String estado = "";
		
		if (cmbAgrupacion.getSelectedItem() != null
				&& !"Todas".equals(cmbAgrupacion.getSelectedItem().getLabel().trim())) {
			agrupacion = cmbAgrupacion.getSelectedItem().getValue().toString();
		}
		if (txtNombre.getValue() != null && !"".equals(txtNombre.getValue().toString().trim())) {
			entidad = txtNombre.getValue().toString().trim();
		}
		if (txtCuentaContable.getValue() != null && !"".equals(txtCuentaContable.getValue().toString().trim())) {
			cuentaContable = txtCuentaContable.getValue().toString().trim();
		}
		if (cmbEstado.getSelectedItem() != null && !"Todos".equals(cmbEstado.getSelectedItem().getLabel().trim())) {
			estado = cmbEstado.getSelectedItem().getValue().toString();
		}
		if (txtCodigoColector.getValue() != null && !"".equals(txtCodigoColector.getValue().toString().trim())) {
			codigoColector = txtCodigoColector.getValue().toString().trim();
		}
		if (txtNit.getValue() != null && !"".equals(txtNit.getValue().toString().trim())) {
			nit = txtNit.getValue().toString().trim();
		}
		llenaListbox(agrupacion, entidad, cuentaContable, estado, codigoColector, nit );
	}

	public void llenaListbox(String agrupacion, String entidad, String cuentaContable, String estado,
			String codigoColector, String nit) {
		try {
			limpiarListbox(lbxConsulta);
			CBCatalogoAgenciaDAO objeDAO = new CBCatalogoAgenciaDAO();
			listaConsulta = objeDAO.obtieneListadoAgenciasMnt(agrupacion, entidad, cuentaContable, estado,
					codigoColector, nit );
			if (listaConsulta.size() > 0) {
				this.btnExcel.setDisabled(false);
			} else {
				this.btnExcel.setDisabled(true);
			}
			Listitem item = null;
			Listcell cell = null;
			int contador = 1;
			for (CBCatalogoAgenciaModel data : listaConsulta) {
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(String.valueOf(contador).toString()); // Agrupacion
				cell.setParent(item);
				/*
				 * cell = new Listcell(); cell.setLabel(data.getcBCatalogoAgenciaId()); //
				 * Codigo cell.setParent(item);
				 */
				cell = new Listcell();
				cell.setLabel(data.getCodigoColector()); // Codigo colector
				// System.out.println("codigo colector " + data.getCodigoColector());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(data.getNombreBanco()); // Agrupacion
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(data.getNombre()); // Entidad
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(data.getTelefono()); // Telefono
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(data.getDireccion()); // Direccion
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(data.getCuentaContable()); // Cuenta contable
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(data.getEstadoTxt()); // Estado
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(data.getMoneda()); // moneda
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(data.getNit()); // nit
				cell.setParent(item);

				item.setValue(data);
				item.setParent(lbxConsulta);
				item.setAttribute("objSelected", data);
				item.setTooltip("popSelected");
				item.addEventListener(Events.ON_DOUBLE_CLICK, evtDoubleClickItem);
				item.addEventListener(Events.ON_CLICK, evtSelectedItem);
				contador++;
			}
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,
					"- Llena listbox de consulta general");
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void limpiar() {
		txtCuentaContable.setText("");
		txtNombre.setText("");
		cmbAgrupacion.setSelectedIndex(-1);
		cmbEstado.setSelectedIndex(-1);
		txtCodigoColector.setText("");
	}

	/**
	 * Evento que se dispara al dar doble clic sobre un registro de entidad de la
	 * Listbox principal Seteo de valores en componentes de modal
	 */
	EventListener<Event> evtDoubleClickItem = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				banderaEntidadSelected = true;
				session.setAttribute("objEntidadSelected", event.getTarget().getAttribute("objSelected"));
				session.setAttribute("cmbListAgrupaciones", listaAgrupacionesCmb);
				session.setAttribute("cmbListEstado", listaEstadoCmb);
				session.setAttribute("ifcEntidades", CBConsultaEntidadesController.this);
				Executions.createComponents("/cbentidadmodal.zul", null, null);
			} catch (Exception e) {
				Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};

	/**
	 * Evento que se dispara al seleccionar un registro de entidad de la Listbox
	 * principal Seteo de valores en componentes de modal
	 */
	EventListener<Event> evtSelectedItem = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				banderaEntidadSelected = true;
				CBCatalogoAgenciaModel obj = (CBCatalogoAgenciaModel) event.getTarget().getAttribute("objSelected");
				Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,
						"\n**** Entidad seleccionada ****\n");
				session.setAttribute("idAgencia", obj.getcBCatalogoAgenciaId());
				session.setAttribute("idBanco", obj.getcBCatalogoBancoId());
				session.setAttribute("nombreBanco", obj.getNombreBanco());
				session.setAttribute("nombreAgencia", obj.getNombre());

			} catch (Exception e) {
				Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};

	/**
	 * Metodos para ingresar a pantallas anexas
	 */

	public void onClick$btnAsignarConfronta() {
		try {
			if (banderaEntidadSelected) {
				Executions.createComponents("cbasociacionconfronta.zul", null, null);
			} else {
				Messagebox.show("Debe seleccionar una entidad para ingresar a esta pantalla", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnSucursales() {
		try {
			if (banderaEntidadSelected) {
				Executions.createComponents("cbAgenciaVirtualFisica.zul", null, null);
			} else {
				Messagebox.show("Debe seleccionar una entidad para ingresar a esta pantalla", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnCajas() {
		try {
			if (banderaEntidadSelected) {
				Executions.createComponents("/cbAgenciaCajas.zul", null, null);
			} else {
				Messagebox.show("Debe seleccionar una entidad para ingresar a esta pantalla", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnAfiliaciones() {
		try {
			if (banderaEntidadSelected) {
				Executions.createComponents("/cbAgenciaAfiliaciones.zul", null, null);
			} else {
				Messagebox.show("Debe seleccionar una entidad para ingresar a esta pantalla", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnComisiones() {
		try {
			if (banderaEntidadSelected) {
				Executions.createComponents("/cbAsignaImpuestosTiendasPropias.zul", null, null);
			} else {
				Messagebox.show("Debe seleccionar una entidad para ingresar a esta pantalla", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * Metodo para limpiar cualquier Listbox y para poder agregar nuevos Listitems
	 */
	public void limpiarListbox(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);
			}
		}
	}

	public void recargaListbox() {
		onClick$btnConsultar();
	}

	/*
	 * se agrega generacion reporte Ovidio Santos
	 */

	Button btnExcel;

	// Agregado por Carlos Godínez - QitCorp - 21/03/2017
	public void onClick$btnExcel() throws IOException {
		System.out.println("Generando reporte ...");
		BufferedWriter bw = null;
		try {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
			// Creamos el encabezado
			String encabezado = "CODIGO|CODIGO COLECTOR|AGRUPACION|ENTIDAD|TELEFONO|DIRECCION|NIT|CUENTA CONTABLE\n";
			File archivo = new File("reporte_entidades_" + sdf.format(fecha) + ".csv");

			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(encabezado);
			for (Listitem iterator : this.lbxConsulta.getItems()) {
				CBCatalogoAgenciaModel registro = (CBCatalogoAgenciaModel) iterator.getValue();
				bw.write(changeNull(registro.getcBCatalogoAgenciaId()).trim() + "|"
						+ changeNull(registro.getCodigoColector()).trim() + "|"
						+ changeNull(registro.getNombreBanco()).trim() + "|" + changeNull(registro.getNombre()).trim()
						+ "|'" + changeNull(registro.getTelefono()).trim() + "|"
						+ changeNull(registro.getDireccion()).trim() + "|"
								+ changeNull(registro.getNit()).trim() + "|"
						+ changeNull(registro.getCuentaContable()).trim() + "\n");
			}

			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.INFO,
					"Descarga exitosa del archivo generado...");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCIÓN",
					Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();
		} catch (IOException e) {
			Logger.getLogger(CBConsultaEntidadesController.class.getName()).log(Level.SEVERE, null, e);
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
