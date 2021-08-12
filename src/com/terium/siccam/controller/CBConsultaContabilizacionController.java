package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpSession;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Thead;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.DesktopUnavailableException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBBitacoraLogDAO;
import com.terium.siccam.dao.CBConsultaContabilizacionDAO;
import com.terium.siccam.model.CBBitacoraLogModel;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBConsultaContabilizacionModel;
import com.terium.siccam.model.CBParametrosSAPModel;
import com.terium.siccam.utils.CBConsultaContabilizacionThread;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

/**
 * @author Ovidio Santos modify by Juankrlos
 * 
 */
public class CBConsultaContabilizacionController extends ControladorBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9176164927878418930L;

	Textbox tbxTexto;
	Textbox tbxObservaciones;
	Textbox tbxTexto2;
	Textbox tbxCentroCosto;
	Textbox tbxCuenta;
	Textbox tbxClave;
	Textbox tbxReferencia;
	Listbox lbxConsulta;
	Datebox dtbDesde;
	Datebox dtbHasta;
	Datebox dtbDia;
	Checkbox ckbMarcarAll;

	int idseleccionado = 0;
	Button btnLimpiar;

	Button btnConsultar;
	Button btnExcel;
	Button btnModificar;
	Button btnSAP;
	Button btnGenerarInfo;
	Button btnDescargarSAP; //CarlosGodinez -> 03/11/2017
	Button btnFTPSAP; //CarlosGodinez -> 03/11/2017
	
	Button btnDescargarSAP2;

	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	HttpSession misession1 = (HttpSession) Sessions.getCurrent().getNativeSession();
	HttpSession misession2 = (HttpSession) Sessions.getCurrent().getNativeSession();

	List<CBConsultaContabilizacionModel> detallesSeleccionados = null;
	CBConsultaContabilizacionModel objModel = new CBConsultaContabilizacionModel();
	List<CBConsultaContabilizacionModel> listConsulta = null;

	CBConsultaContabilizacionDAO objDaoDelete = new CBConsultaContabilizacionDAO();

	CBConsultaContabilizacionModel objModelDelete = null;

	String fechaini;
	String fechafin;

	//String fechainfo;

	Media media;
	boolean isReloadFile = false;
	int idArchivo = 0;

	/**
	 * Propiedades agregadas por Carlos Godinez - Qitcorp - 02/06/2017
	 * 
	 * Estas propiedades se ocupan para la generacion del archivo SAP
	 */
	private String nombreArchivo;
	private String nombreArchivo2;
	private String valorIP;
	private String user;
	private String pass;
	private String rutaArchivoSAP;
	// Agrega Carlos Godinez - 07-06/2017
	private String encabezado1;
	private String encabezado2;
	private String encabezado3;
	// Agrega Carlos Godinez - 09/06/2017
	private String extensionArchivo;
	Combobox cmbAgencia;
	Combobox cmbBanco;
	Combobox cmbAgenciaIngreso;
	
	File archivoGZ; //CarlosGodinez -> 03/11/2017
	File archivoTxt; //CarlosGodinez -> 28/12/2017
	
	File archivoTxt2 = null;
	File archivoGZ2 = null;
	
	String token = "";

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);
		this.btnExcel.setDisabled(true);
		obtienefechaactual();
		llenaComboBanco();
		llenacmbAgenciaIngreso();
		this.btnSAP.setDisabled(true);
		btnDescargarSAP.setDisabled(true); //CarlosGodinez -> 03/11/2017
		btnFTPSAP.setDisabled(true); //CarlosGodinez -> 03/11/2017
		
		btnDescargarSAP2.setDisabled(true);
		
		try {
			CBConsultaContabilizacionDAO paramConfSAP = new CBConsultaContabilizacionDAO();
			CBParametrosSAPModel objConfSAP = paramConfSAP.obtenerValoresConfSAP();
			
			llenarVariableSAP(objConfSAP);
			System.out.println("Valores obtenidos");
			System.out.println("NOMBRE ARCHIVO = " + this.nombreArchivo);
			System.out.println("IP = " + this.valorIP);
			System.out.println("USER = " + this.user);
			System.out.println("RUTA ARCHIVO = " + this.rutaArchivoSAP);
		} catch (Exception e) {
			System.out.println("Ha ocurrido un error: " + e.getMessage());
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
		
		
	}
	
	public void llenarVariableSAP(CBParametrosSAPModel objConfSAP) {
		this.nombreArchivo = objConfSAP.getNombreArchivo();
		this.nombreArchivo2 = objConfSAP.getNombreArchivo2();
		this.valorIP = objConfSAP.getValorIP();
		this.user = objConfSAP.getUser();
		this.pass = objConfSAP.getPass();
		this.rutaArchivoSAP = objConfSAP.getRutaArchivoSAP();
		this.encabezado1 = objConfSAP.getEncabezado1();
		this.encabezado2 = objConfSAP.getEncabezado2();
		this.encabezado3 = objConfSAP.getEncabezado3();
		this.extensionArchivo = objConfSAP.getExtensionArchivo();
	}

	public void onClick$btnLimpiar() {
		limpiartextbox();
	}

	public void limpiartextbox() {

		tbxCentroCosto.setText("");
		tbxClave.setText("");
		tbxObservaciones.setText("");
		tbxTexto.setText("");
		tbxTexto2.setText("");
		tbxReferencia.setText("");
		tbxCuenta.setText("");
		cmbAgencia.setSelectedIndex(-1);
		cmbBanco.setSelectedIndex(-1);
		cmbAgenciaIngreso.setSelectedIndex(-1);
		limpiaCombobox(cmbAgenciaIngreso);
		
		
		// dtbDesde.setText("");
		// dtbHasta.setText("");

	}

	public void limpiarListbox(Listbox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	// metodo consulta
	public void onClick$btnConsultar() {

		int idAgencia = 0;
		int idAgenciaIngreso = 0;
		CBConsultaContabilizacionDAO cbaDao = new CBConsultaContabilizacionDAO();
		CBConsultaContabilizacionModel objModel = new CBConsultaContabilizacionModel();
		DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
		
		if (cmbAgencia.getSelectedItem() != null) {
			idAgencia = Integer.parseInt(cmbAgencia.getSelectedItem().getValue().toString());
		}
		if (cmbAgenciaIngreso.getSelectedItem() != null) {
			idAgenciaIngreso = Integer.parseInt(cmbAgenciaIngreso.getSelectedItem().getValue().toString());
		}

		System.out.println("Consulta estados contabilizacion");
		if (tbxCentroCosto.getText().trim() != null 
				&&  !"".equals(tbxCentroCosto.getText().trim())) {
			objModel.setCentroCosto(tbxCentroCosto.getText().trim());
		}
		if (tbxClave.getText() != null 
				&& !"".equals(tbxClave.getText().trim())) {
			objModel.setClaveContabilizacion(tbxClave.getText().trim());
		}
		if (tbxCuenta.getText().trim() != null 
				&& !"".equals(tbxCuenta.getText().trim())) {
			objModel.setCuenta(tbxCuenta.getText().trim());
		}
		if (tbxReferencia.getText().trim() != null 
				&& !"".equals(tbxReferencia.getText().trim())) {
			objModel.setReferencia(tbxReferencia.getText().trim());
		}
		if (tbxTexto.getText().trim() != null 
				&& !"".equals(tbxTexto.getText().trim())) {
			objModel.setTexto(tbxTexto.getText().trim());
		}
		if (tbxTexto2.getText().trim() != null 
				&& !"".equals(tbxTexto2.getText().trim())) {
			objModel.setTexto2(tbxTexto2.getText().trim());
		}
		if (tbxObservaciones.getText().trim() != null 
				&& !"".equals(tbxObservaciones.getText().trim())) {
			objModel.setObservaciones(tbxObservaciones.getText().trim());
		}

		if (dtbDesde.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dtbHasta.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha fin antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dtbDesde.getValue().after(dtbHasta.getValue())) {
			Messagebox.show("La fecha desde debe ser menor a la fecha hasta.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else {
			objModel.setFechaini(fechaFormato.format(dtbDesde.getValue()));
			objModel.setFechafin(fechaFormato.format(dtbHasta.getValue()));
			fechaini = (fechaFormato.format((dtbDesde.getValue())));
			fechafin = (fechaFormato.format((dtbHasta.getValue())));
		}

		System.out.println("fecha desde " + objModel.getFechaini());
		System.out.println("fecha desde " + objModel.getFechafin());

		objModel.setBanco(objModel.getBanco());
		objModel.setAgencia(objModel.getAgencia());
		objModel.setTerminacion(objModel.getTerminacion());
		objModel.setNombre(objModel.getNombre());
		objModel.setTipo(objModel.getTipo());
		objModel.setDebe(objModel.getDebe());
		objModel.setHaber(objModel.getHaber());
		objModel.setFecha_contabilizacion(objModel.getFecha_contabilizacion());

		objModel.setFecha_ingreso(objModel.getFecha_ingreso());

		objModel.setEstado(objModel.getEstado());
		objModel.setModificado_por(objModel.getModificado_por());

		// objModel.setUsuario(user);
		llenaListbox(cbaDao.obtenerContabilizacion(objModel, idAgencia, idAgenciaIngreso));
		if (this.lbxConsulta.getItemCount() != 0) {
			this.btnExcel.setDisabled(false);
			this.btnSAP.setDisabled(false);
		} else {
			this.btnExcel.setDisabled(true);
			this.btnSAP.setDisabled(true);
		}
		btnDescargarSAP.setDisabled(true); //CarlosGodinez -> 03/11/2017
		btnFTPSAP.setDisabled(true); //CarlosGodinez -> 03/11/2017
		
		btnDescargarSAP2.setDisabled(true);
	}

	// metodo para llenar listbox
	public void llenaListbox(List<CBConsultaContabilizacionModel> list) {
		limpiarListbox(lbxConsulta);

		System.out.println("cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			Iterator<CBConsultaContabilizacionModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getFecha());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getFecha_ingreso());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getBanco());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getAgencia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getReferencia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTexto());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTexto2());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getObservaciones());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getClaveContabilizacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTerminacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCentroCosto());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombre());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCuenta());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTipo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getDebe());
				cell.setParent(item);

				/*
				 * cell = new Listcell(); cell.setLabel(objModel.getHaber());
				 * cell.setParent(item);
				 */
				cell = new Listcell();
				cell.setLabel(objModel.getFecha_contabilizacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getEstado());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getModificado_por());
				cell.setParent(item);

				cell = new Listcell();
				Button btnModificar = new Button();
				btnModificar.setLabel("Modificar");
				cell.setParent(item);
				// item para btn modificar
				item.setAttribute("objmodificar", objModel);
				item.setAttribute("fechaini", fechaini);
				
				item.setAttribute("fechafin", fechafin);
				// item.setAttribute("idseleccionado",
				// objModel.getCbcontabilizacionid());
				item.addEventListener("onDoubleClick", eventBtnModificar);

				cell = new Listcell();
				Checkbox ckbMarcarAll = new Checkbox();
				ckbMarcarAll.setTooltiptext("MarcarTodos");
				// item.setAttribute("objmodificar", cbainsertados);
				// System.out.println("item seleccionado");
				ckbMarcarAll.setParent(cell);

				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxConsulta);

				// paramsLlenaListbox);
			}
			// listConsulta.clear();
		} else {
			Messagebox.show("No se encontraron resultados!", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	public void llenaListbox(List<CBConsultaContabilizacionModel> list, Listbox lbxConsulta) {
		limpiarListbox(lbxConsulta);

		System.out.println("cantidad de registros " + list.size());
		//

		if (list != null && list.size() > 0) {
			Iterator<CBConsultaContabilizacionModel> it = list.iterator();

			Listitem item = null;
			// creo variable de celda
			Listcell cell = null;
			while (it.hasNext()) {
				objModel = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(objModel.getFecha());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getFecha_ingreso());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getBanco());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getAgencia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getReferencia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTexto());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTexto2());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getObservaciones());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getClaveContabilizacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTerminacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCentroCosto());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getNombre());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getCuenta());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getTipo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getDebe());
				cell.setParent(item);

				/*
				 * cell = new Listcell(); cell.setLabel(objModel.getHaber());
				 * cell.setParent(item);
				 */
				cell = new Listcell();
				cell.setLabel(objModel.getFecha_contabilizacion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getEstado());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(objModel.getModificado_por());
				cell.setParent(item);

				cell = new Listcell();
				Button btnModificar = new Button();
				btnModificar.setLabel("Modificar");
				cell.setParent(item);
				// item para btn modificar
				item.setAttribute("objmodificar", objModel);
				item.setAttribute("fechaini", fechaini);
				
				item.setAttribute("fechafin", fechafin);
				// item.setAttribute("idseleccionado",
				// objModel.getCbcontabilizacionid());
				item.addEventListener("onDoubleClick", eventBtnModificar);

				cell = new Listcell();
				Checkbox ckbMarcarAll = new Checkbox();
				ckbMarcarAll.setTooltiptext("MarcarTodos");
				// item.setAttribute("objmodificar", cbainsertados);
				// System.out.println("item seleccionado");
				ckbMarcarAll.setParent(cell);

				item.setValue(objModel);
				item.setTooltip("popAsociacion");
				item.setAttribute("objSeleccionado", objModel);
				item.setParent(lbxConsulta);

				// paramsLlenaListbox);
			}
			// listConsulta.clear();
		} else {
			Messagebox.show("No se encontraron resultados!", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	public void onClick$btnSAP() {
		try {
			if (nombreArchivo == null ||  "".equals(nombreArchivo)) {
				Messagebox.show("No se ha especificado el nombre del archivo SAP a generar.\n "
						+ "Contacte a su administrador.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (nombreArchivo2 == null ||  "".equals(nombreArchivo2)) {
				Messagebox.show("No se ha especificado el nombre del archivo SAP 2 a generar.\n "
						+ "Contacte a su administrador.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (valorIP == null || "".equals(valorIP)) {
				Messagebox.show("No se ha especificado la IP del servidor.\n" + "Contacte a su administrador.",
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (user == null || "" .equals(user)) {
				Messagebox.show("No se ha especificado el usuario FTP del servidor.\n" + "Contacte a su administrador.",
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (pass == null || "".equals(pass)) {
				Messagebox.show(
						"No se ha especificado una contraseña FTP del servidor.\n" + "Contacte a su administrador.",
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (rutaArchivoSAP == null || "".equals(rutaArchivoSAP)) {
				Messagebox.show("No se ha especificado una ruta FTP donde se guardara el archivo SAP.\n"
						+ "Contacte a su administrador.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (encabezado1 == null || "".equals(encabezado1)) {
				Messagebox.show("No se ha especificado segmento para el encabezado para archivo SAP.\n"
						+ "Contacte a su administrador.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (encabezado2 == null || "".equals(encabezado2)) {
				Messagebox.show("No se ha especificado segmento para el encabezado para archivo SAP.\n"
						+ "Contacte a su administrador.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (encabezado3 == null || "".equals(encabezado3)) {
				Messagebox.show("No se ha especificado segmento para el encabezado para archivo SAP.\n"
						+ "Contacte a su administrador.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else {
				System.out.println("\n==== ENTRA A GENERACION DE ARCHIVO SAP ====\n");
				CBConsultaContabilizacionModel objModel = new CBConsultaContabilizacionModel();
				DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
				if (dtbDesde.getValue() != null && dtbHasta.getValue() != null) {
					objModel.setFechaini(fechaFormato.format(dtbDesde.getValue()));
					objModel.setFechafin(fechaFormato.format(dtbHasta.getValue()));

					
					int idAgencia = 0;
					if (cmbAgencia.getSelectedItem() != null) {
						idAgencia = Integer.parseInt(cmbAgencia.getSelectedItem().getValue().toString());
					}
					int idAgenciaIngreso = 0;
					if (cmbAgenciaIngreso.getSelectedItem() != null) {
						idAgenciaIngreso = Integer.parseInt(cmbAgenciaIngreso.getSelectedItem().getValue().toString());
					}
					
					System.out.println("Consulta estados contabilizacion");
					if (tbxCentroCosto.getText().trim() != null && !tbxCentroCosto.getText().trim().equals("")) {
						objModel.setCentroCosto(tbxCentroCosto.getText().trim());
					}
					if (tbxClave.getText() != null && !tbxClave.getText().trim().equals("")) {
						objModel.setClaveContabilizacion(tbxClave.getText().trim());
					}
					if (tbxCuenta.getText().trim() != null && !tbxCuenta.getText().trim().equals("")) {
						objModel.setCuenta(tbxCuenta.getText().trim());
						System.out.println("campo cuenta en el dao " + objModel.getCuenta().trim());
					}
					if (tbxReferencia.getText().trim() != null && !tbxReferencia.getText().trim().equals("")) {
						objModel.setReferencia(tbxReferencia.getText().trim());
					}
					if (tbxTexto.getText().trim() != null && !tbxTexto.getText().trim().equals("")) {
						objModel.setTexto(tbxTexto.getText().trim());
						System.out.println("texto controlador " + objModel.getTexto().trim());
					}
					if (tbxTexto2.getText().trim() != null && !tbxTexto2.getText().trim().equals("")) {
						objModel.setTexto2(tbxTexto2.getText().trim());
					}
					if (tbxObservaciones.getText().trim() != null && !tbxObservaciones.getText().trim().equals("")) {
						objModel.setObservaciones(tbxObservaciones.getText().trim());
					}

					/// fin filtros nuevos
					CBConsultaContabilizacionDAO objdao = new CBConsultaContabilizacionDAO();
				//	List<CBParametrosSAPModel> listaSapModel = objdao.obtieneDatosSAP(objModel, idAgencia, idAgenciaIngreso,1);
					List<CBParametrosSAPModel> listaSapModel = objdao.obtieneDatosSAP(objModel, idAgencia, idAgenciaIngreso,2);
					
					/*Generar archivo formato 1*/
					//obtenerListadoSAP(listaSapModel, 1);
					
					/*Generar archivo formato 2*/
					obtenerListadoSAP(listaSapModel, 2);
					
				} else {
					Messagebox.show("Primero debe consultar informacion para generar el archivo", "ATENCION",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			System.out.println("Ha ocurrido un error al generar archivo SAP: " + e.getMessage());
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/*
	 * @author Nicolas Bermudez
	 * 
	 * Obtener listado SAP para generar archivo 
	 * 
	 * @param
	 * @listaSapModel: Listado obtenido de la consulta
	 * @tipo:			Tipo de archivo a generar
	 * */
	
	public void obtenerListadoSAP(List<CBParametrosSAPModel> listaSapModel, int tipo) {
		List<String> listaSapArchivo = new ArrayList<String>();
		if (listaSapModel != null && listaSapModel.size() > 0) {
			BigDecimal totalMontoSAP = new BigDecimal(0);
			int contCheck = 0; // contador de items seleccionados
			for (Listitem item : lbxConsulta.getItems()) {
				CBConsultaContabilizacionModel objListboxModel = (CBConsultaContabilizacionModel) item.getValue();
				if (item.isSelected()) {
					contCheck++;
					int idSeleccionado = objListboxModel.getCbcontabilizacionid();
					for(CBParametrosSAPModel objSAP : listaSapModel) {
						if(idSeleccionado == objSAP.getIdSAP()){
							listaSapArchivo.add(objSAP.getLineaSAP());
						}
					}
					// Calculo de monto para total y promedio items chequeados
					String montoStr = (objListboxModel.getHaber() == null
							|| objListboxModel.getHaber().equals("")) ? "0" : objListboxModel.getHaber();
					totalMontoSAP = totalMontoSAP.add(new BigDecimal(montoStr));
				}
			}
			if (contCheck == 0) {
				// Calculo de monto para total y promedio todos los items
				for (Listitem item : lbxConsulta.getItems()) {
					CBConsultaContabilizacionModel objListboxModel = (CBConsultaContabilizacionModel)item.getValue();  
					String montoStr = (objListboxModel.getHaber() == null
							|| objListboxModel.getHaber().equals("")) ? "0" : objListboxModel.getHaber();
					totalMontoSAP = totalMontoSAP.add(new BigDecimal(montoStr));
				}
				
				int v_secuencia = 0;
				int v_asiento = 1;
				int v_estadocuentaid = 0;
				
				for(CBParametrosSAPModel objSAP : listaSapModel) { 
					/*Formato de archivo 1*/
					if(tipo == 1) {
						if(v_estadocuentaid != objSAP.getIdEstadocuenta()) {
							v_estadocuentaid = objSAP.getIdEstadocuenta();
							v_asiento = 1;
							v_secuencia++;
						}else {
							v_asiento++;
						}
						
						String secuencia = String.format("%06d", v_secuencia);
						String asiento = String.format("%03d", v_asiento);
						
						String lineaSAP = objSAP.getLineaSAP();
						
						String inicioSAP = lineaSAP.substring(0, 4);
						String finalSAP = lineaSAP.substring(13);
						
						String nuevaLineaSAP = inicioSAP + secuencia + asiento + finalSAP;
						
						listaSapArchivo.add(nuevaLineaSAP);
					}
					/*Formato de archivo 2*/
					if(tipo == 2) {
						String asiento = "";
						String secuencia = "";
						
						if(v_estadocuentaid != objSAP.getIdEstadocuenta()) {
							v_estadocuentaid = objSAP.getIdEstadocuenta();
							v_asiento = 1;
							v_secuencia++;
							
							//secuencia = String.format("%06d", v_secuencia);
							secuencia = Integer.toString(v_secuencia);
							
							String lineaEncabezado = objSAP.getLineaEncabezadoPartida();
							lineaEncabezado = lineaEncabezado.replace("secuencia", secuencia);
							
							String lineaEncabezadoPartida = "1	" + lineaEncabezado;
							listaSapArchivo.add(lineaEncabezadoPartida);
							
						}else {
							v_asiento++;
						}
						
						 //secuencia = String.format("%06d", v_secuencia);
						asiento = String.format("%03d", v_asiento);
						
						String lineaSAP = objSAP.getLineaSAP();
						
						String nuevaLineaSAP = "2	" + lineaSAP + asiento;
						
							
						listaSapArchivo.add(nuevaLineaSAP);
					}
					
				}
			}
			generaArchivoSAP(listaSapArchivo, totalMontoSAP, tipo);
		} else {
			System.out.println("Lista SAP esta vacia.");
		}
	}
	

	/**
	 * @author Juankrlos
	 * 
	 * Se suben los archivos generados con los datos de la consulta por ftp
	 * 
	 * Modificado ultima vez por Carlos Godinez - 14/09/2017
	 * @throws IOException 
	 * 
	 */
	public void generaArchivoSAP(List<String> list, BigDecimal bdTotal, int tipo) {
		BufferedWriter bw = null;
		FileInputStream fis = null;
		GZIPOutputStream gzos = null;
		try {
			if (list.size() > 0) {
				Iterator<String> it = list.iterator();
				System.out.println(StringUtils.rightPad("change", 20, " "));
				Date fecha = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");

				
				/************************** Si es tipo 1 *****************************/
				if(tipo == 1) {
					/**
					 * Generacion de archivo .txt
					 */
					archivoTxt = new File(nombreArchivo + sdf.format(fecha) + ".txt");
					String encabezado = encabezado1 + "  " + sdf2.format(fecha) + encabezado2 + sdf.format(fecha)
							+ encabezado3;
					
					System.out.println("archivo1" );
					
					System.out.println(nombreArchivo + sdf.format(fecha) + ".txt");

					if (!archivoTxt.exists()) {
						if(archivoTxt.createNewFile())
							Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,"Archivo temporal creado");
					}

					//bw = new BufferedWriter(new FileWriter(archivoTxt));
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoTxt,true),StandardCharsets.ISO_8859_1));
					bw.write(encabezado + "\n");

					while (it.hasNext(  )) {
						String bean = it.next() + "\n";
						bw.write(bean);
					}
					BigDecimal bdPromedio = bdTotal.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);  
					
					//Se redondea cada dato para que no queden decimales
					BigDecimal totalRounded = bdTotal.setScale(0, BigDecimal.ROUND_HALF_EVEN);
					BigDecimal promedioRounded = bdPromedio.setScale(0, BigDecimal.ROUND_HALF_EVEN);
					
					String totalItemsStr = StringUtils.leftPad(String.valueOf(list.size()), 10, " ");
					String totalMontoStr = StringUtils.leftPad(String.valueOf(totalRounded), 20, " ");
					String promedioStr = StringUtils.leftPad(String.valueOf(promedioRounded), 20, " ");

					String detalleSAP = "T" + totalItemsStr + totalMontoStr + promedioStr;
					Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO," **** Longitud de linea de detalle = ", detalleSAP.length());
					bw.write(detalleSAP);
					
					System.out.println("ARCHIVO TIPO1 .txt GENERADO CON EXITO");
									
					/**
					 * Generacion de archivo .gz
					 * 
					 * variable archivoGZ pasa a ser variable publica
					 **/
					byte[] buffer = new byte[1024];
					archivoGZ = new File(nombreArchivo + sdf.format(fecha) + extensionArchivo);
					gzos = new GZIPOutputStream(new FileOutputStream(archivoGZ));
					fis = new FileInputStream(archivoTxt);
					int lineaGZ;
					while ((lineaGZ = fis.read(buffer)) > 0) {
						gzos.write(buffer, 0, lineaGZ);
					}
		
					/**
					 * Se genera con exito archivo SAP
					 * */
				}
				
				/************************ Si es tipo 2 **********************************/
				if(tipo == 2) {
					/**
					 * Generacion de archivo .txt
					 */
					archivoTxt2 = new File(nombreArchivo2 + sdf.format(fecha) + ".txt");
					//String encabezado = "0 " + encabezado1 + "  " + sdf2.format(fecha) + encabezado2 + sdf.format(fecha)
					//		+ encabezado3;
					String encabezado = "0	500	" + Tools.getCookie("userName") + "	1";
					
					System.out.println(nombreArchivo2 + sdf.format(fecha) + ".txt");
					
					
					System.out.println("archivo2" );

					if (!archivoTxt2.exists()) {
						if(archivoTxt2.createNewFile())
							Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,"Archivo temporal creado");
					}

					//bw = new BufferedWriter(new FileWriter(archivoTxt2));
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivoTxt2,true),StandardCharsets.ISO_8859_1));
					bw.write(encabezado + "\n");

					while (it.hasNext()) {
						String bean = it.next() + "\n";
						bw.write(bean);
					}
					BigDecimal bdPromedio = bdTotal.divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);  
					
					//Se redondea cada dato para que no queden decimales
					BigDecimal totalRounded = bdTotal.setScale(0, BigDecimal.ROUND_HALF_EVEN);
					BigDecimal promedioRounded = bdPromedio.setScale(0, BigDecimal.ROUND_HALF_EVEN);
					
					
					String totalItemsStr = StringUtils.leftPad(String.valueOf(list.size()), 10, " ");
					String totalMontoStr = StringUtils.leftPad(String.valueOf(totalRounded), 20, " ");
					String promedioStr = StringUtils.leftPad(String.valueOf(promedioRounded), 20, " ");

					String detalleSAP = "T" + totalItemsStr + totalMontoStr + promedioStr;
					Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO," **** Longitud de linea de detalle = ", detalleSAP.length());
					//bw.write(detalleSAP);
					
					System.out.println("ARCHIVO TIPO 2 .txt GENERADO CON EXITO");
									
					/**
					 * Generacion de archivo .gz
					 * 
					 * variable archivoGZ pasa a ser variable publica
					 **/
					byte[] buffer = new byte[1024];
					archivoGZ2 = new File(nombreArchivo2 + sdf.format(fecha) + extensionArchivo);
					gzos = new GZIPOutputStream(new FileOutputStream(archivoGZ2));
					fis = new FileInputStream(archivoTxt2);
					int lineaGZ;
					while ((lineaGZ = fis.read(buffer)) > 0) {
						gzos.write(buffer, 0, lineaGZ);
					}
		
					/**
					 * Se genera con exito archivo SAP
					 * */
				}
				
				
				btnDescargarSAP.setDisabled(false); //CarlosGodinez -> 03/11/2017
				btnFTPSAP.setDisabled(false); //CarlosGodinez -> 03/11/2017
				
				btnDescargarSAP2.setDisabled(false);
				
				/**
				 * CarlosGodinez -> 03/11/2017
				 * 
				 * Separacion de funciones para descargar archivo SAP o enviar por ruta FTP
				 * */
				Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,"\n** Archivo SAP generado con exito\n");
				
				if(tipo == 1) {
					Messagebox.show("Archivo SAP generado con exito\n\nAhora puede:\n- Descargar archivo\n- Subir SAP por FTP", 
							Constantes.ATENCION, Messagebox.OK,Messagebox.INFORMATION);
				}
				
			} else {
				Messagebox.show("Debe consultar informacion antes de subir un archivo a SAP", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {		
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			
			if(bw != null) {
				try {
					bw.close();
				}catch (Exception e) {
					Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if(fis != null) {
				try {
					fis.close();
				}catch (Exception e) {
					Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if(gzos != null){
				try {
					gzos.finish();
					gzos.close();
				}catch (Exception e) {
					Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
				}
			}			
		} 
		Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,
				"\n==== FIN GENERACION DE ARCHIVO SAP ====\n");
	}
	
	/**
	 * Descarga de archivo SAP
	 * 
	 * Editado ultima vez por Carlos Godinez -> 28/12/2017
	 **/
	public void onClick$btnDescargarSAP() {
		try {
			Filedownload.save(archivoTxt, null); //CarlosGodinez -> 28/12/2017
			Messagebox.show("Archivo SAP descargado con exito", "ATENCION", Messagebox.OK,
					Messagebox.INFORMATION);
		} catch (Exception e) {			
			Messagebox.show("Ha ocurrido un error al intentar subir archivo SAP por FTP", "ATENCION", 
					Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void onClick$btnDescargarSAP2() {
		try {
			Filedownload.save(archivoTxt2, null); //CarlosGodinez -> 28/12/2017
			Messagebox.show("Archivo SAP descargado con exito", "ATENCION", Messagebox.OK,
					Messagebox.INFORMATION);
		} catch (Exception e) {			
			Messagebox.show("Ha ocurrido un error al intentar subir archivo SAP por FTP", "ATENCION", 
					Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * CarlosGodinez -> 03/11/2017 
	 * 
	 * Se agrega metodo para separacion de funciones
	 * Subir archivo a servidor FTP
	 * @throws IOException 
	 **/
	public void onClick$btnFTPSAP() {
		FileInputStream input = null;
		try {
			FTPClient ftpClient = new FTPClient();
			ftpClient.connect(InetAddress.getByName(valorIP));
			ftpClient.login(user, pass);

			int reply = ftpClient.getReplyCode();

			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,"Respuesta recibida de conexión FTP:", reply);

			if (FTPReply.isPositiveCompletion(reply)) {
				Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,"Conectado Satisfactoriamente");
			} else {
				Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,"Imposible conectarse al servidor");
			}

			// Verificar si se cambia de directorio de trabajo

			boolean change = ftpClient.changeWorkingDirectory(rutaArchivoSAP);// Cambiar directorio de trabajo
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.INFO,"Se cambió satisfactoriamente el directorio " + change);

			// Activar que se envie cualquier tipo de archivo

			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			input = new FileInputStream(archivoGZ);// Ruta del archivo para enviar
			ftpClient.enterLocalPassiveMode();
			ftpClient.storeFile(archivoGZ.getName(), input);// Ruta completa de alojamiento en el FTP
			System.out.println("ARCHIVO .gz SUBIDO A SERVIDOR FTP CON EXITO");
			
			ftpClient.logout(); // Cerrar sesión
			ftpClient.disconnect();// Desconectarse del servidor
			
			Messagebox.show("Archivo SAP subido a FTP con exito.", "ATENCION", Messagebox.OK,
					Messagebox.INFORMATION);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			Messagebox.show("Ha ocurrido un error al intentar subir archivo SAP por FTP", "ATENCION", 
					Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(input != null) {
				try {
					input.close();
				} catch (IOException e) {
					Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE,null,e);
				} // Cerrar envio de arcivos al FTP
			}
		}
	}

	// se crea el evento modificar
	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {


			// se crea una variable personalizada
			CBConsultaContabilizacionModel objmodificar = (CBConsultaContabilizacionModel) arg0.getTarget()
					.getAttribute("objmodificar");

			fechaini = (String) arg0.getTarget().getAttribute("fechaini");
			fechafin = (String) arg0.getTarget().getAttribute("fechafin");

			System.out.println("obj a modificar: " + objmodificar);
			
			misession1.setAttribute("sesionfecha1", fechaini);
			misession2.setAttribute("sesionfecha2", fechafin);
			System.out.println("fecha ini " + fechaini);
			misession.setAttribute("sesioncontabilizacionModal", objmodificar);
			session.setAttribute("interfaceTarjeta", CBConsultaContabilizacionController.this);
			Window contabilizacionModal = (Window) Executions.createComponents("/cbconsultacontabilizacionmodal.zul",
					null, null);
			contabilizacionModal.doModal();

		}
	};

	/////////////////////

	public void onCheck$ckbMarcarAll() {
		if (ckbMarcarAll.isChecked()) {

			List<Listitem> list = lbxConsulta.getItems();

			System.out.println("listado tama;o " + list.size());

			if (list.size() > 0) {
				detallesSeleccionados = new ArrayList<CBConsultaContabilizacionModel>();
				for (Listitem lista : list) {

					lista.setSelected(true);

				}
				onClick$btnLimpiar();

			}
		} else {
			List<Listitem> list = lbxConsulta.getItems();
			if (list.size() > 0) {
				detallesSeleccionados = new ArrayList<CBConsultaContabilizacionModel>();
				for (Listitem lista : list) {
					lista.setSelected(false);

					detallesSeleccionados = new ArrayList<CBConsultaContabilizacionModel>();
				}
				onClick$btnLimpiar();

			}
		}

	}

	//////////////////// reporte
	public String changeNull(String cadena) {
		if (cadena == null) {
			return " ";
		} else {
			return cadena;
		}
	}

	// CBConsultaContabilizacionModel objModel = new
	// CBConsultaContabilizacionModel();
	public void onClick$btnExcel() {
		int contador = 0;
		List<CBConsultaContabilizacionModel> list = new ArrayList<CBConsultaContabilizacionModel>();

		for (Listitem item : lbxConsulta.getItems()) {

			if (item.isSelected()) {
				contador++;
				CBConsultaContabilizacionModel objModel = (CBConsultaContabilizacionModel) item.getValue();
				list.add(objModel);
			}
		}
		if (contador == 0) {
			for (Listitem item : lbxConsulta.getItems()) {
				CBConsultaContabilizacionModel objModel = (CBConsultaContabilizacionModel) item.getValue();
				list.add(objModel);
			}
		}
		try {
			generarReporte(list);
		} catch (IOException e) {
			
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void generarReporte(List<CBConsultaContabilizacionModel> list) throws IOException {
		BufferedWriter bw = null; 
		System.out.println("Generando reporte ...");

		try {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

			// Creamos el encabezado
			String encabezado = "Fecha a contabilizar|Fecha ingreso|Banco|Agencia|Referencia|Texto|Texto2|"
					+ "Observaciones|Clave_contabilizacion|Terminacion|Centro_costo|Nombre|"
					+ "Cuenta|Tipo|Monto|Fecha_contabilizacion|" + "Estado|Modificado_por\n";
			File archivo = new File("reporte_contabilizacion_" + sdf.format(fecha) + ".csv");
			
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(encabezado);

			CBConsultaContabilizacionModel objModelReporte = new CBConsultaContabilizacionModel();
			Iterator<CBConsultaContabilizacionModel> it = list.iterator();
			while (it.hasNext()) {
				objModelReporte = it.next();
				bw.write(changeNull(objModelReporte.getFecha()).trim() + "|"
						+ changeNull(objModelReporte.getFecha_ingreso()) + "|" + changeNull(objModelReporte.getBanco())
						+ "|" + changeNull(objModelReporte.getAgencia()).trim() + "|"
						+ changeNull(objModelReporte.getReferencia()).trim() + "|"
						+ changeNull(objModelReporte.getTexto()).trim() + "|"
						+ changeNull(objModelReporte.getTexto2()).trim() + "|"
						+ changeNull(objModelReporte.getObservaciones()).trim() + "|"
						+ changeNull(objModelReporte.getClaveContabilizacion()).trim() + "|"
						+ changeNull(objModelReporte.getTerminacion()).trim() + "|"
						+ changeNull(objModelReporte.getCentroCosto()) + "|" + changeNull(objModelReporte.getNombre())
						+ "|" + changeNull(objModelReporte.getCuenta()) + "|" + changeNull(objModelReporte.getTipo())
						+ "|" + changeNull(objModelReporte.getDebe()) + "|"
						+ changeNull(objModelReporte.getFecha_contabilizacion()).trim() + "|"
						+ changeNull(objModelReporte.getEstado()).trim() + "|"
						+ changeNull(objModelReporte.getModificado_por()) + "\n");
			}
			bw.close();
			System.out.println("Descarga exitosa del archivo generado...");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCIÓN",
					Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();

		} catch (IOException e) {
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(bw != null)
				bw.close();
		}
	}

	public void recargaConsultaConta(String fechaini, String fechafin) {
		System.out.println("Entra a recargar consulta...");
		int idAgencia = 0;
		int idAgenciaIngreso = 0;
		CBConsultaContabilizacionModel objModel = new CBConsultaContabilizacionModel();
		CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
		

		objModel.setFechaini(fechaini);
		objModel.setFechafin(fechafin);

		llenaListbox(objDao.obtenerContabilizacion(objModel, idAgencia, idAgenciaIngreso));

	}

	public void GenerarInfo() {
		CBConsultaContabilizacionModel objModel = new CBConsultaContabilizacionModel();
		CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
		int idAgencia = 0;
		int idAgenciaIngreso = 0;
				String fechaDesde = null;
				String fechaHasta = null;
				
				if (dtbDesde.getValue() == null) {
					Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				} else if (dtbHasta.getValue() == null) {
					Messagebox.show("Debe ingresar la fecha fin antes de consultar datos.", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				} else if (dtbDesde.getValue().after(dtbHasta.getValue())) {
					Messagebox.show("La fecha desde debe ser menor a la fecha hasta.", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				} else {
				
					fechaDesde = (dtbDesde.getText());
					fechaHasta = (dtbHasta.getText());
				
					objModel.setFechaini(fechaDesde);
					objModel.setFechafin(fechaHasta);
//
		}
		if (cmbAgencia.getSelectedItem() != null) {
			idAgencia = Integer.parseInt(cmbAgencia.getSelectedItem().getValue().toString());
		}
		if (cmbAgenciaIngreso.getSelectedItem() != null) {
			idAgenciaIngreso = Integer.parseInt(cmbAgenciaIngreso.getSelectedItem().getValue().toString());
		}
		System.out.println("Consulta estados contabilizacion");
		if (tbxCentroCosto.getText().trim() != null 
				&& !"".equals(tbxCentroCosto.getText().trim())) {
			objModel.setCentroCosto(tbxCentroCosto.getText().trim());
		}
		if (tbxClave.getText() != null 
				&& !"".equals(tbxClave.getText().trim())) {
			objModel.setClaveContabilizacion(tbxClave.getText().trim());
		}
		if (tbxCuenta.getText().trim() != null 
				&& !"".equals(tbxCuenta.getText().trim())) {
			objModel.setCuenta(tbxCuenta.getText().trim());
			System.out.println("campo cuenta en el dao " + objModel.getCuenta().trim());
		}
		if (tbxReferencia.getText().trim() != null 
				&& !"".equals(tbxReferencia.getText().trim())) {
			objModel.setReferencia(tbxReferencia.getText().trim());
		}
		if (tbxTexto.getText().trim() != null 
				&& !"".equals(tbxTexto.getText().trim())) {
			objModel.setTexto(tbxTexto.getText().trim());
			System.out.println("texto controlador " + objModel.getTexto().trim());
		}
		if (tbxTexto2.getText().trim() != null 
				&& !"".equals(tbxTexto2.getText().trim())) {
			objModel.setTexto2(tbxTexto2.getText().trim());
		}
		if (tbxObservaciones.getText().trim() != null 
				&& !"".equals(tbxObservaciones.getText().trim())) {
			objModel.setObservaciones(tbxObservaciones.getText().trim());
		}
		System.out.println("fecha antes del insert" + fechaDesde);
		System.out.println("fecha antes del insert" + fechaHasta);
		
		/* ********************* INICIA HILOS ************************* */
		int dias = 0;
		Tools.setCookie("diasCarga", String.valueOf(dias));
		
		token = UUID.randomUUID().toString();
		token = token.replace("-", "");
		
		final String pais = "CR";
		
		CBBitacoraLogDAO dao = new CBBitacoraLogDAO();
		
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
			Date fecha1 = formatoFecha.parse(fechaDesde);
			Date fecha2 = formatoFecha.parse(fechaHasta);

			System.out.println("Iniciando proceso");			
			dias = Tools.diferenciasDeFechas(fecha1, fecha2);
			
			Logger.getLogger(CBConsultaContabilizacionController.class.getName())
			.log(Level.INFO,"Cantidad de dias a consultar: " + (dias + 1));
		
			Tools.setCookie("diasCarga", String.valueOf((dias + 1)));

			System.out.println("ya seteo una cookie");
		
			dao.deleteBitacoraThread("CARGA_CONTABILIZACION");

			System.out.println("se ejecuto el delete");
			
			for(int i = 0; i <= dias; i++) {
				Date fechaActual = Tools.sumarDias(fecha1, i);
				long tokenItem = System.currentTimeMillis();
				System.out.println("Fecha actual: " + i +"  "+ fechaActual);
				System.out.println("Fecha actual: " + tokenItem);
				//CBConsultaContabilizacionThread hilo = new CBConsultaContabilizacionThread(formatoFecha.format(fechaActual));
				Thread hilo = new Thread(new CBConsultaContabilizacionThread(formatoFecha.format(fechaActual), pais, String.valueOf(tokenItem)));
				System.out.println("se lanza el hilo");
				hilo.start();
				System.out.println("hilo lanzado");				
				CBBitacoraLogModel log = new CBBitacoraLogModel();
				log.setTipoCarga("0");
				log.setNombreArchivo(String.valueOf(tokenItem));
				log.setModulo(token);
				log.setAccion( formatoFecha.format(fechaActual));
				log.setUsuario("CARGA_CONTABILIZACION");
				System.out.println("llama a la bitacora");				
				dao.insertBitacoraLog(log);
				System.out.println("finaliza llamada a la bitacora");	
				
				
				Thread.sleep(500);
			}

		}catch (Exception e) {
			// TODO: handle exception
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
		
		/* ************************ FINALIZA HILOS ******************************* */
		
		/* Levantando modal */
		//Executions.createComponents("/cbdetallecargacontabilizacionmodal.zul", null, null);
		
		int diasItem = (dias + 1);
		CBDetalleContabilizacionController daoDetalle = (CBDetalleContabilizacionController) misession2.getAttribute("instanciaModalDetalle");

		//daoDetalle.llenaListboxTipificacion(token, pais, false);
		
		/* Verificando cada 30s si ya terminaron de ejecutarse todos los hilos con un retraso inicial de 3m*/
		try {
			
			Boolean verifica = true;
			Boolean retraso = true;
			
			while(verifica) {
				
				if(retraso) {
					System.out.println("Retraso de 3 min");
					Thread.sleep(3*60*1000);
					retraso = false;
				}
				
				Boolean result =  verificaCargaSP(pais, token, diasItem);
				
				if(result) {
					verifica = false;
					
					daoDetalle.llenaListboxTipificacion(token, pais, false);
					 
					 limpiarListbox(lbxConsulta);
						
						disabledSAPAll();
						
						List<CBConsultaContabilizacionModel> list = objDao.obtenerContabilizacion(objModel, idAgencia, idAgenciaIngreso, pais);
						System.out.println("refrescar lista!" + list.size());
						if (list.size() > 0) {
							llenaListbox(list);
							
							btnExcel.setDisabled(false);
							btnSAP.setDisabled(false);
							btnDescargarSAP.setDisabled(true);
							btnDescargarSAP2.setDisabled(true);
							btnFTPSAP.setDisabled(true);
						}
					
				}else {
					//daoDetalle.llenaListboxTipificacion(token, pais, false);
				}
				Thread.sleep(30000);
			}
			
			
		}catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}

	}
	
	
	
	public boolean verificaCargaSP(String pais, String token, int dias) {
		CBBitacoraLogDAO dao = new CBBitacoraLogDAO();

		Boolean result = false;
		
		System.out.println("Dias recibidos: " + dias);
		if(dao.countBitacoraThread(token, pais) == dias) {
			System.out.println("Proceso terminado!");
			result = true;


		}else {
			System.out.println("Todavia no!!");
		}
		
		return result;
	}

	// METODO PARA VER SI YA EXISTE EL INSERT

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onClick$btnGenerarInfo() {
		//cambia fechas 
		String fechaDesde = null;
		String fechaHasta = null;
		if (dtbDesde.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dtbHasta.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha fin antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dtbDesde.getValue().after(dtbHasta.getValue())) {
			Messagebox.show("La fecha desde debe ser menor a la fecha hasta.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else {
		
			fechaDesde = (dtbDesde.getText());
			fechaHasta = (dtbHasta.getText());
	
		btnGenerarInfo.setTooltip("popAgregar");
		

			//objModelDelete = objDaoDelete.validaCarga(fechaDesde, fechaHasta);
		}
		
		token = "";
		
		misession2.setAttribute("fechaDesdeConta", fechaDesde);
		misession2.setAttribute("fechaHastaConta", fechaHasta);
		misession2.setAttribute("paisActual", Tools.getCookie("conexion"));
		misession2.setAttribute("sesiontoken", token);
		misession2.setAttribute("instanciaConta", CBConsultaContabilizacionController.this);
		
		Executions.createComponents("/cbdetallecargacontabilizacionmodal.zul", null, null);
		
		
		
		/*if (objModelDelete != null) {
			Messagebox.show("Ya hay registros para " + fechaDesde +" y " + fechaHasta + " (Desea reemplazar dicha carga?)", "ATENCION",
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() != Messagebox.YES) {
								Messagebox.show("Accion Cancelada", "CANCELADO", Messagebox.OK, Messagebox.EXCLAMATION);

								// GenerarInfo();
							} else {
								// eliminaRegistros(fecha);
								System.out.println("Elimina info y vuelve a  cargar informacion");

								GenerarInfo();
							}
						}
					});
		} else {
			// isReloadFile = true;
			System.out.println("cargar informacion nueva ");
			GenerarInfo();
		}*/

	}

	public void obtienefechaactual() {
		String fecha = null;
		CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
		try {
		fecha = objDao.validafecha();
		System.out.println("fecha obtenida al entrar " + fecha);
		}catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
		
	}
	// bancos
	public void llenaComboBanco() {
		limpiaCombobox(cmbBanco);
		CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
		List<CBConciliacionCajasModel> list = objDao.generaConsultaBanco();
		Iterator<CBConciliacionCajasModel> it = list.iterator();
		CBConciliacionCajasModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getIdcombo());
			item.setParent(cmbBanco);
		}
	}

	// se obtiene el listado del banco
	public void llenacmbAgenciaIngreso() {
		limpiaCombobox(cmbAgencia);
		CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
		List<CBCatalogoAgenciaModel> list = objDao.generaConsultaAgenciaIngreso();
	System.out.println("tama;o de la lista:" + list.size());
		Iterator<CBCatalogoAgenciaModel> it = list.iterator();
		CBCatalogoAgenciaModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getcBCatalogoAgenciaId());
			item.setParent(cmbAgencia);
		}
	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}
	// se obtiene el listado del agencia ingreso
	public void onSelect$cmbBanco() {
		limpiaCombobox(cmbAgenciaIngreso);
		cmbAgenciaIngreso.setSelectedIndex(-1);
		CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
		//int idBanco = Integer.parseInt(cmbBanco.getSelectedItem().getValue().toString());
		int idBanco = Integer.parseInt(cmbBanco.getSelectedItem().getValue().toString());
		List<CBCatalogoAgenciaModel> list = objDao.generaConsultaAgencia(idBanco);
		Iterator<CBCatalogoAgenciaModel> it = list.iterator();
		CBCatalogoAgenciaModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getcBCatalogoAgenciaId());
			item.setParent(cmbAgenciaIngreso);
		}
	}

	public void disabledSAPAll() {
		btnExcel.setDisabled(true);
		btnSAP.setDisabled(true);
		btnDescargarSAP.setDisabled(true);
		btnDescargarSAP2.setDisabled(true);
		btnFTPSAP.setDisabled(true);
	}
	
}
