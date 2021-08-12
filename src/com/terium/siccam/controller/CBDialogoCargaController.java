package com.terium.siccam.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

import com.terium.siccam.controller.CBProcessFileUploadController;
import com.terium.siccam.controller.CBDialogoCargaController;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.implement.ConciliacionMultipleService;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.utils.CBDialogoCargaCR;
import com.terium.siccam.utils.CBDialogoCargaGT;
import com.terium.siccam.utils.CBDialogoCargaNi;
import com.terium.siccam.utils.CBDialogoCargaPa;
import com.terium.siccam.utils.CBDialogoCargaUtils;
import com.terium.siccam.utils.Tools;

public class CBDialogoCargaController extends ControladorBase {

	private static final long serialVersionUID = 1L;

	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	// variables
	private String mensajeDialogo;
	String tipoDeCarga;
	String nombreArchivo;
	String idMaestro;
	Integer agenciaId;
	int cbBancoAgenciaConfrontaId;
	boolean estadoVentana;
	int estado;
	Button btnCargar;
	Button btnCargarTodo;
	Button btnCancelar;

	List<CBDataBancoModel> listDataBanco;
	List<CBDataSinProcesarModel> listSinProcesarBanco;

	String formatoFechaConfronta;

	Window dialogCarga;

	

	ConciliacionMultipleService multipleService;

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// carga las variables de session
		agenciaId = (Integer) misession.getAttribute("agencia");
		mensajeDialogo = (String) misession.getAttribute("mensaje");
		nombreArchivo = (String) misession.getAttribute("archivo");
		idMaestro = (String) misession.getAttribute("idMaestro");
		tipoDeCarga = (String) misession.getAttribute("tipoMensaje");
		listDataBanco = (List<CBDataBancoModel>) misession.getAttribute("listaBanco");
		cbBancoAgenciaConfrontaId = (Integer) misession.getAttribute("cbBancoAgenciaConfrontaId");
		listSinProcesarBanco = (List<CBDataSinProcesarModel>) misession.getAttribute("listaSinProcesar");
		estadoVentana = (Boolean) misession.getAttribute("estadoVentana");
		formatoFechaConfronta = (String) misession.getAttribute("formatoFechaConfronta");
		
		/*
		if (tipoDeCarga.equals("1")) {
			btnCargarTodo.setVisible(false);
			btnCancelar.setVisible(true);
			btnCargar.setVisible(true);
		} else {
			btnCargarTodo.setVisible(true);
			btnCancelar.setVisible(true);
			btnCargar.setVisible(false);
		}
		*/
		if(misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
			
			btnCargarTodo.setVisible(true);
			btnCancelar.setVisible(true);
			btnCargar.setVisible(true);
		}else {
			btnCargarTodo.setVisible(false);
			btnCancelar.setVisible(true);
			btnCargar.setVisible(true);
		}
		
	}

	/**
	 * Cancela el proceso de la carga de confronta
	 */

	public void onClick$btnCancelar() {
		// elimina el id creado anteriormente
		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		cbdb.borraArchivoPrecargado(nombreArchivo, idMaestro);

		// llama el metodo que cierra la ventana de dialogo
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(true, idMaestro);

		dialogCarga.detach();
	}

	public void onClick$btnCargar(){
		// verifica el tipo de carga
		System.out.println("tipo de carga: " + tipoDeCarga);
		if (tipoDeCarga.equals("1")) {
			// tipo de carga normal

			cargaConfronta();

			dialogCarga.detach();

		} else {
			cargaConfrontaDelDia();
			dialogCarga.detach();
		}

	}

	public void onClick$btnCargarTodo()  {
		// elimina y carga de nuevo los pagos
		
		
		try {
			cargaDeNuevoPagos();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
		}

		System.out.println("tipo de carga: " + tipoDeCarga);
		// verifica el tipo de carga
		if (tipoDeCarga.equals("1")) {
			cargaConfronta();

		} else {
			cargaConfrontaDelDia();
		}
		dialogCarga.detach();
	}

	CBDialogoCargaUtils cbdialogoSV = new CBDialogoCargaUtils();
	CBDialogoCargaGT cbdialogoGT = new CBDialogoCargaGT();
	CBDialogoCargaNi cbdialogoNI = new CBDialogoCargaNi();
	CBDialogoCargaPa cbdialogoPA = new CBDialogoCargaPa();
	CBDialogoCargaCR cbdialogoCR = new CBDialogoCargaCR();
	public void cargaConfronta(){

		if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
			try {
				cbdialogoGT.cargaConfrontasGT();
			} catch (ParseException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}

		} else if (misession.getAttribute("conexion").equals(Tools.SESSION_NI)) {
			try {
				cbdialogoNI.cargaConfrontas();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		}else if (misession.getAttribute("conexion").equals(Tools.SESSION_PA)) {
			try {
				cbdialogoPA.cargaConfrontas();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		}else if (misession.getAttribute("conexion").equals(Tools.SESSION_CR)) {
			try {
				cbdialogoCR.cargaConfrontas();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		}else {
			try {
				cbdialogoSV.cargaConfrontas();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		}

	}

	/**
	 * Metodo para cuando la confronta tiene info del dia y se desea cargar de nuevo
	 * 
	 * @throws CustomExcepcion
	 * @throws SQLException
	 * @throws ParseException
	 */
	public void cargaConfrontaDelDia() {
		if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
			cbdialogoGT.cargaConfrontaDelDia();
		} else if (misession.getAttribute("conexion").equals(Tools.SESSION_NI)) {
			try {
				cbdialogoNI.cargaConfrontaDelDia();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		} else if (misession.getAttribute("conexion").equals(Tools.SESSION_PA)) {
			try {
				cbdialogoPA.cargaConfrontas();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		} else if (misession.getAttribute("conexion").equals(Tools.SESSION_CR)) {
			try {
				cbdialogoCR.cargaConfrontas();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		}else {
			try {
				cbdialogoSV.cargaConfrontaDelDia();
			} catch (SQLException e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaController.class.getName()).log(Level.INFO, null, e);
			}
		}
	}

	/**
	 * Metodo que llama la eliminacion y carga de los pagos
	 * 
	 * @throws ParseException
	 */
	public void cargaDeNuevoPagos() throws ParseException {
		if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
			cbdialogoGT.cargaDeNuevoPagos();
		} 
		else {
			cbdialogoSV.cargaDeNuevoPagos();
		}
	}

	// GET AND SETS
	public String getMensajeDialogo() {
		return mensajeDialogo;
	}

	public void setMensajeDialogo(String mensajeDialogo) {
		this.mensajeDialogo = mensajeDialogo;
	}
}
