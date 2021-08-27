/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.terium.siccam.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;
import com.terium.siccam.dao.CBBancoAgenciaConfrontaDAO;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.dao.CBCatalogoBancoDaoB;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.utils.CBProcessFileUploadCR;
import com.terium.siccam.utils.CBProcessFileUploadGT;
import com.terium.siccam.utils.CBProcessFileUploadNi;
import com.terium.siccam.utils.CBProcessFileUploadPa;
import com.terium.siccam.utils.CBProcessFileUploadUtils;
import com.terium.siccam.utils.Tools;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;

/**
 * 
 * @author rSianB to terium.com
 * @modify Juankrlos to qitcorp.com
 */
public class CBProcessFileUploadController extends ControladorBase {

	private static Logger logger = Logger.getLogger(CBProcessFileUploadController.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	Combobox cmbAgencia;
	Combobox cmbBanco;
	Combobox cmbConfronta;
	Label lblMensaje;
	Image imgEstatus;
	Button btnCargaConfrontas;
	// private InputStream is;
	private static String nombreArchivo;
	private Media media;
	String usuario;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		usuario = obtenerUsuario().getUsuario();
		llenaComboBanco();
		btnCargaConfrontas.setVisible(false);
	}

	// bancos
	public void llenaComboBanco() {
		limpiaCombobox(cmbBanco);
		CBCatalogoBancoDaoB objDao = new CBCatalogoBancoDaoB();
		List<CBCatalogoBancoModel> list = objDao.obtieneListaBanco(null, null, null, null);
		Iterator<CBCatalogoBancoModel> it = list.iterator();
		CBCatalogoBancoModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getCbcatalogobancoid());
			item.setParent(cmbBanco);

		}
		this.cmbAgencia.setSelectedIndex(-1);
		this.cmbConfronta.setSelectedIndex(-1);
		cmbAgencia.setText("");

	}

	// se obtiene el listado del banco
	public void onSelect$cmbBanco() {
		limpiaCombobox(cmbAgencia);
		CBCatalogoAgenciaDAO objDao = new CBCatalogoAgenciaDAO();
		String idBanco = (cmbBanco.getSelectedItem().getValue().toString());
		List<CBCatalogoAgenciaModel> list = objDao.obtieneListadoAgencias(idBanco, null, null, null);
		Iterator<CBCatalogoAgenciaModel> it = list.iterator();
		CBCatalogoAgenciaModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getcBCatalogoAgenciaId());
			item.setParent(cmbAgencia);

		}

		cmbConfronta.setText("");

		this.cmbConfronta.setSelectedIndex(-1);

	}

	// se obtiene el listado del banco
	public void onSelect$cmbAgencia() {
		limpiaCombobox(cmbConfronta);
		CBBancoAgenciaConfrontaDAO objDao = new CBBancoAgenciaConfrontaDAO();
		cmbConfronta.setText("");
		String idAgencia = cmbAgencia.getSelectedItem().getValue().toString();
		String idBancos = cmbBanco.getSelectedItem().getValue().toString();
		List<CBConfiguracionConfrontaModel> list = objDao.obtieneListadoBancoAgeConfronta(idBancos, idAgencia);
		Iterator<CBConfiguracionConfrontaModel> it = list.iterator();
		CBConfiguracionConfrontaModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getcBConfiguracionConfrontaId());
			item.setParent(cmbConfronta);
			btnCargaConfrontas.setVisible(true);

		}

	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	/**
	 * @author Juankrlos -12/01/2017 Obtener listado para combo Cuentas
	 */
	// format.toUpperCase().equals("dat".toUpperCase()
	public void onUpload$btnArchivoEstados(UploadEvent event) {
		media = event.getMedia();
		String methodName = "onUpload$btnArchivoEstados()";
		logger.debug(methodName + " - Formato del archivo antes de insertar: " + media.getFormat());
		String format = media.getName().substring(media.getName().length() - 3, media.getName().length());
		logger.debug(methodName + " - Formato con substring: " + format);

		logger.debug(methodName + " - File Information : " + "name: " + media.getName());
		logger.debug(methodName + " - File Information : " + "contentType: " + media.getContentType());
		logger.debug(methodName + " - File Information : " + "format: " + media.getFormat());

		if (media.getFormat().toUpperCase().equals("xlsx".toUpperCase())) {
			lblMensaje.setValue("Archivo seleccionado: " + media.getName());
			lblMensaje.setStyle("color:blue;");
			imgEstatus.setSrc("img/azul.png");
		} else if (media.getFormat().toUpperCase().equals("xls".toUpperCase())) {
			lblMensaje.setValue("Archivo seleccionado: " + media.getName());
			lblMensaje.setStyle("color:blue;");
			imgEstatus.setSrc("img/azul.png");
		} else if (media.getFormat().toUpperCase().equals("txt".toUpperCase())) {
			lblMensaje.setValue("Archivo seleccionado: " + media.getName());
			lblMensaje.setStyle("color:blue;");
			imgEstatus.setSrc("img/azul.png");
		} else if (format.toUpperCase().equals("log".toUpperCase())) {
			lblMensaje.setValue("Archivo seleccionado: " + media.getName());
			lblMensaje.setStyle("color:blue;");
			imgEstatus.setSrc("img/azul.png");
		} else if (format.toUpperCase().equals("dat".toUpperCase())) {
			lblMensaje.setValue("Archivo seleccionado: " + media.getName());
			lblMensaje.setStyle("color:blue;");
			imgEstatus.setSrc("img/azul.png");
		} else {
			lblMensaje.setValue("Error en formato -- Archivo seleccionado: " + media.getName());
			lblMensaje.setStyle("color:red;");
			imgEstatus.setSrc("img/rojo.png");
		}

	}

	int cantidadAgrupacionConfronta;
	CBProcessFileUploadUtils cbprocesSV = new CBProcessFileUploadUtils();
	CBProcessFileUploadGT cbprocesGT = new CBProcessFileUploadGT();
	CBProcessFileUploadNi cbprocesNI = new CBProcessFileUploadNi();
	CBProcessFileUploadPa cbprocesPA = new CBProcessFileUploadPa();
	CBProcessFileUploadCR cbprocesCR = new CBProcessFileUploadCR();

	public void onClick$btnCargaConfrontas() {

		CBDataBancoDAO bancoDAO = new CBDataBancoDAO();

		if (cmbBanco.getSelectedItem() != null && (cmbAgencia.getSelectedItem() != null)
				&& (cmbConfronta.getSelectedItem() != null)) {

			String nombreBanco = cmbBanco.getSelectedItem().getLabel();

			int idBanco = Integer.parseInt(cmbBanco.getSelectedItem().getValue().toString());
			int idAgencia = Integer.parseInt(cmbAgencia.getSelectedItem().getValue().toString());
			int idConfronta = Integer.parseInt(cmbConfronta.getSelectedItem().getValue().toString());

			if (media != null)
				try {
					{
						nombreArchivo = media.getName();
						String format = media.getName().substring(media.getName().length() - 3,
								media.getName().length());
						logger.debug("onClick$btnCargaConfrontas()" + " - Formato con substring: " + format);
						formatoFechaConfronta = bancoDAO.obtenerFormatoFechaConfronta(idConfronta);
						// Cambia Juankrlos --:> 01/11/2017
						formatoFechaConfronta = cambiaFormatoFecha(formatoFechaConfronta);

						cantidadAgrupacionConfronta = bancoDAO.obtenerCantidadAgrupacion(idConfronta);

						session.setAttribute("nombreArchivo", nombreArchivo);
						session.setAttribute("usuario", usuario);
						session.setAttribute("media", media);
						session.setAttribute("format", format);
						session.setAttribute("formatoFechaConfronta", formatoFechaConfronta);
						session.setAttribute("cantidadAgrupacionConfronta", cantidadAgrupacionConfronta);
						session.setAttribute("entidad", nombreBanco);
						session.setAttribute("banco", idBanco);
						session.setAttribute("agencia", idAgencia);
						session.setAttribute("confronta", idConfronta);

						if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
							cbprocesGT.mapeoCargaConfrontasGT();
						} else if (misession.getAttribute("conexion").equals(Tools.SESSION_NI)) {
							cbprocesNI.mapeoCargaConfrontasNI();
						} else if (misession.getAttribute("conexion").equals(Tools.SESSION_PA)) {
							cbprocesPA.mapeoCargaConfrontasPA();
						} else if (misession.getAttribute("conexion").equals(Tools.SESSION_CR)) {
							cbprocesCR.mapeoCargaConfrontasCR();
						} else {
							logger.debug("onClick$btnCargaConfrontas() - ingresa mapeoCargaConfrontasSV");
							cbprocesSV.mapeoCargaConfrontasSV();
						}
					}
				} catch (Exception e) {
					logger.error("onClick$btnCargaConfrontas() - Error : ", e);
				}
			else {
				Messagebox.show("Debe seleccionar un archivo para cargar ", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}

		} else {
			Messagebox.show("Debe seleccionar una Entidad Bancaria, Agencia y Confronta... ", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}

	}

	public void limpiaCampos() {
		cmbAgencia.setSelectedIndex(-1);
		cmbBanco.setSelectedIndex(-1);
		cmbConfronta.setSelectedIndex(-1);
		setNombreArchivo("");

	}

	String formatoFechaConfronta;
	private static List<CBDataBancoModel> listDataBanco;
	private String idCargaMaestra;
	private String tipo = "0";

	/**
	 * @author Juankrlos 01/11/2017
	 */
	public String cambiaFormatoFecha(String formato) {
		// String formato = "";
		try {
			formato = formato.replace("hh24", "HH");
			formato = formato.replace("HH24", "HH");
			formato = formato.replace("mi", "mm");
			formato = formato.replace("MI", "mm");
			formato = formato.replace("am", "a");
			formato = formato.replace("am", "a");
			formato = formato.replace("AM", "a");
			formato = formato.replace("a.m.", "a");
			logger.debug("cambiaFormatoFecha() " + " - formato seleccionado " + formato);
		} catch (Exception e) {
			logger.error("cambiaFormatoFecha() - Error : ", e);

			return "";
		}

		return formato;
	}

	private CBArchivosInsertadosDAO cvaidao = new CBArchivosInsertadosDAO();

	public void cerrarDetalleCarga(boolean estVentana, String idMaestroC) {

		// carga variables
		if (estVentana) {
			System.out.println("borra fila Carga Maestro");
			cvaidao.borraFilaGrabadaMaestro(idMaestroC);
		}

	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		CBProcessFileUploadController.nombreArchivo = nombreArchivo;
	}

	public String getIdCargaMaestra() {
		return idCargaMaestra;
	}

	public void setIdCargaMaestra(String idCargaMaestra) {
		this.idCargaMaestra = idCargaMaestra;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public List<CBDataBancoModel> getListDataBanco() {
		return listDataBanco;
	}

}