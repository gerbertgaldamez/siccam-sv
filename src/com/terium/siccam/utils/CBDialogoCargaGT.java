package com.terium.siccam.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.terium.siccam.controller.CBProcessFileUploadController;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.dao.CBDataSinProcesarDAO;
import com.terium.siccam.implement.ConciliacionMultipleService;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;

public class CBDialogoCargaGT {
	private static final long serialVersionUID = 1L;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	List<CBDataBancoModel> listDataBanco;
	List<CBDataSinProcesarModel> listSinProcesarBanco;

	String formatoFechaConfronta;

	Window dialogCarga;
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
	String entidad;

	@SuppressWarnings("unchecked")
	public void cargaConfrontasGT() throws ParseException {
		entidad = (String) misession.getAttribute("entidad");
		ConciliacionMultipleService multipleService;
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

		// borra el archivo y registros si
		// encuentra
		// uno igual.

		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		if (cbdb.consultaExistenciaArchivoGT(nombreArchivo, idMaestro)) { // es diferente

			cbdb.borrarArchivoGT(nombreArchivo, idMaestro, cbBancoAgenciaConfrontaId, listDataBanco.get(0).getDia());// es
																														// diferente
		}
		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		int countRec = cbdb.insertarMasivoGT(listDataBanco);
		CBDataSinProcesarDAO cbdatasinprocesar = new CBDataSinProcesarDAO();
		int dataSinProcc = cbdatasinprocesar.insertarMasivoGT(listSinProcesarBanco);
		// dBConnection.closeConneccion(conn);

		//
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yy");
			String fechaV = listDataBanco.get(0).getDia().toString();
			
			System.out.println("Llama al proceso para conciliacion");
			cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
					" Archivo: " + nombreArchivo + "_Inicia el proceso de conciliacion.", "");

			if (cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
				Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.INFO,
						"Finaliza el proceso de conciliacion");
				cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
						" Archivo: " + nombreArchivo + "_Finaliza el proceso de conciliacion", "");
				
				
				Logger.getLogger(CBDialogoCargaGT.class.getName())
				.log(Level.INFO, "inicia proceso de ajustes pendientes");
				// realiza la llamada al proceso de ajustes pendientes
				try {
					cbdb.ejecutaProcesoAjustesPendientes(df.format(df.parse(fechaV)),
							agenciaId);
					Logger.getLogger(CBDialogoCargaGT.class.getName())
					.log(Level.INFO, "finaliza proceso de ajustes pendientes");
				} catch (ParseException e1) {
					System.out.println("error al convertir la fecha: "
							+ e1.getMessage());
				}
				
				Logger.getLogger(CBDialogoCargaGT.class.getName())
				.log(Level.INFO, "inicia proceso de ajustes ");
				try {			
					
					cbdb.ejecutaProcesoAjustes(df.format(df.parse(fechaV)),agenciaId);
					Logger.getLogger(CBDialogoCargaGT.class.getName())
					.log(Level.INFO, "finaliza proceso de ajustes");
				} catch (ParseException e1) {
					System.out.println("error al convertir la fecha: "
							+ e1.getMessage());
				}
				
				
				
				
				//sp comisiones
				Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.INFO,
						"inicia el proceso de comisiones confrontas");
				if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
					Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.INFO,
							"Finaliza el proceso de comisiones confrontas");
				}
			} else {
				Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.INFO,
						"No se pudo ejecutar el proceso de conciliacion");
			}
		} catch (Exception e) {
			Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.SEVERE, null, e);
		}

		Messagebox.show("Se a cargado la entidad bancaria " + misession.getAttribute("entidad").toString()
				+ ".\n\nDetalle de carga:\n" + countRec + " registos grabados\n" + dataSinProcc
				+ " registos sin procesar ", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);

		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);

	}

	@SuppressWarnings("unchecked")
	public void cargaConfrontaDelDia() {
		entidad = (String) misession.getAttribute("entidad");
		ConciliacionMultipleService multipleService;
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

		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		if (cbdb.consultaExistenciaArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro()))
			try {
				cbdb.borrarArchivoGT(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro(), cbBancoAgenciaConfrontaId,
						listDataBanco.get(0).getDia());
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.SEVERE, null, e);
			}

		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		int countRec = cbdb.insertarMasivoGT(listDataBanco);
		CBDataSinProcesarDAO cbsp = new CBDataSinProcesarDAO();
		int dataSinProcc = cbsp.insertarMasivoGT(listSinProcesarBanco);
		// dBConnection.closeConneccion(conn);

		//
		try {
			DateFormat df = new SimpleDateFormat("dd/MM/yy");
			String fechaV = listDataBanco.get(0).getDia().toString();
			
			System.out.println("Llama al proceso para conciliacion");
			cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
					" Archivo: " + nombreArchivo + "_Inicia el proceso de conciliacion.", "");

			if (cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
				Logger.getLogger(CBProcessFileUploadController.class.getName()).log(Level.INFO,
						"Finaliza el proceso de conciliacion");
				cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
						" Archivo: " + nombreArchivo + "_Finaliza el proceso de conciliacion", "");
				
				Logger.getLogger(CBDialogoCargaGT.class.getName())
				.log(Level.INFO, "inicia proceso de ajustes pendientes");
				// realiza la llamada al proceso de ajustes pendientes
				try {
					cbdb.ejecutaProcesoAjustesPendientes(df.format(df.parse(fechaV)),
							agenciaId);
					Logger.getLogger(CBDialogoCargaGT.class.getName())
					.log(Level.INFO, "finaliza proceso de ajustes pendientes");
				} catch (ParseException e1) {
					System.out.println("error al convertir la fecha: "
							+ e1.getMessage());
				}
				
				Logger.getLogger(CBDialogoCargaGT.class.getName())
				.log(Level.INFO, "inicia proceso de ajustes ");
				try {			
					
					cbdb.ejecutaProcesoAjustes(df.format(df.parse(fechaV)),agenciaId);
					Logger.getLogger(CBDialogoCargaGT.class.getName())
					.log(Level.INFO, "finaliza proceso de ajustes");
				} catch (ParseException e1) {
					System.out.println("error al convertir la fecha: "
							+ e1.getMessage());
				}
				
				
				//sp comisiones
				Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.INFO,
						"inicia el proceso de comisiones confrontas");
				if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
					Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.INFO,
							"Finaliza el proceso de comisiones confrontas");
				}
			} else {
				Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.INFO,
						"No se pudo ejecutar el proceso de conciliacion");
			}
		} catch (Exception e) {
			Logger.getLogger(CBDialogoCargaGT.class.getName()).log(Level.SEVERE, null, e);
		}

		Messagebox.show(
				"Se a cargado la entidad bancaria " + misession.getAttribute("entidad").toString() + " " + countRec
						+ " Registos Grabados " + dataSinProcc + " Registos Sin Procesar ",
				"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);

		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);
	}

	@SuppressWarnings("unchecked")
	public void cargaDeNuevoPagos() throws ParseException {
		entidad = (String) misession.getAttribute("entidad");
		ConciliacionMultipleService multipleService;
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
		// levantar dialogo de espera...

		// llama al sp CB_PAGO_INDIVIDUAL

		CBDataBancoDAO cdbd = new CBDataBancoDAO();
		cdbd.ejecutaSPPagosIndividualGT(cbBancoAgenciaConfrontaId, listDataBanco.get(0).getDia());
		// cierra el dialogo de espera...

	}

	public String getMensajeDialogo() {
		return mensajeDialogo;
	}

	public void setMensajeDialogo(String mensajeDialogo) {
		this.mensajeDialogo = mensajeDialogo;
	}
}
