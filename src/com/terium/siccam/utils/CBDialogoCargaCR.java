package com.terium.siccam.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.CBProcessFileUploadController;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.dao.CBDataSinProcesarDAO;
import com.terium.siccam.implement.ConciliacionMultipleService;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;

public class CBDialogoCargaCR extends ControladorBase {

	private static final long serialVersionUID = 1L;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	List<CBDataBancoModel> listDataBanco;
	List<CBDataSinProcesarModel> listSinProcesarBanco;

	String formatoFechaConfronta;

	Window dialogCarga;
	
	@SuppressWarnings("unused")
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
	public void cargaConfrontas() throws SQLException, Exception {

		entidad = (String) misession.getAttribute("entidad");
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

		// borra el archivo y registros si encuentra uno igual.

		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		if (cbdb.consultaExistenciaArchivo(nombreArchivo, idMaestro)) {
			// borra archivo
			System.out.println("borra el archivo.");
			System.out.println("Nombre archivo: " + nombreArchivo);
			System.out.println("Id Maestro: " + idMaestro);
			System.out.println("Id bancoagencia: " + cbBancoAgenciaConfrontaId);
			System.out.println("Fecha: " + listDataBanco.get(0).getFecha());
			System.out.println("Formato Fecha: " + listDataBanco.get(0).getDia());
			cbdb.borrarArchivo(nombreArchivo, idMaestro, cbBancoAgenciaConfrontaId,
					listDataBanco.get(0).getFechaCreacion(), false);
		}

		/* Existencia de archivo multiple */
		if (cbdb.consultaExistenciaArchivoMultiple(nombreArchivo, idMaestro)) {
			// borra archivo
			Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
					"borra el archivo.");
			Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
					"Nombre archivo: " + nombreArchivo);
			Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
					"Id Maestro: " + idMaestro);
			Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
					"Id bancoagencia: " + cbBancoAgenciaConfrontaId);
			Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
					"Fecha: " + listDataBanco.get(0).getFecha());
			Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
					"Formato Fecha: " + listDataBanco.get(0).getDia());
			cbdb.borrarArchivo(nombreArchivo, idMaestro, cbBancoAgenciaConfrontaId,
					listDataBanco.get(0).getFechaCreacion(), true);
		}
	//	conn = dBConnection.getConneccion();
		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		int countRec = cbdb.insertarMasivo(listDataBanco, listDataBanco.get(0).getFormatofecha());
		CBDataSinProcesarDAO cbsp = new CBDataSinProcesarDAO();
		int dataSinProcc = cbsp.insertarMasivo(listSinProcesarBanco);
		// dBConnection.closeConneccion(conn);

		try {

			// Obteniendo total de fechas diferentes en la confronta
			int dateConfrontas = cbdb.getDateConfronta(listDataBanco.get(0).getIdCargaMaestro());
			// Obteniendo total de convenios diferentes en la confronta
			int conveniosConfronta = cbdb.getCantidadConvenios(listDataBanco.get(0).getIdCargaMaestro());
			
			if (conveniosConfronta > 1) {
				System.out.println("Llama al proceso para separar confrontas por convenio");
				if (cbdb.ejecutaProcesoCargaConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
					Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
							"Finaliza el proceso para separar confrontas por convenio");
					cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
							"Finaliza el proceso para separar confrontas por convenio", "");
				} else {
					Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
							"Ocurrio un error al ejecutar el proceso para separar confrontas por convenio");
				}
			} else {
				if (dateConfrontas > 1) {
					Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
							"Llama al proceso para separar las fechas de la confronta");
					cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
							" Archivo: " + nombreArchivo + "_Inicia el proceso para separar confrontas por fecha.", "");
					if (cbdb.ejecutaProcesoSeparacionFechasConfronta(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
						Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
								"Separacion de fechas de confrontas realizada exitosamente!");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(), " Archivo: " + nombreArchivo
								+ "_Separacion de fechas de confrontas realizada exitosamente", "");
					} else {
						Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
								"Ocurrio un error al ejecutar la separacion de fechas");
					}
				} else {
					Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO, null,
							"Llama al proceso para conciliacion");
					cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
							" Archivo: " + nombreArchivo + "_Inicia el proceso de conciliacion.", "");
					if (cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
						Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
								"Finaliza el proceso de conciliacion");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								" Archivo: " + nombreArchivo + "_Finaliza el proceso de conciliacion", "");
						//sp comisiones
						Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
								"inicia el proceso de comisiones confrontas");
						if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
							Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
									"Finaliza el proceso de comisiones confrontas");
						}
					} else {
						Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.INFO,
								"No se pudo ejecutar el proceso de conciliacion");
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.SEVERE, null, e);
		}

		Messagebox.show("Se a cargado la entidad bancaria " + entidad
				+ ".\n\nDetalle de carga:\n" + countRec + " registos grabados\n" + dataSinProcc
				+ " registos sin procesar ", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);

		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void cargaConfrontaDelDia() throws SQLException, Exception {
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
		if (cbdb.consultaExistenciaArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro()))
			try {
				System.out.println("borra el archivo.");
				System.out.println("Nombre archivo: " + nombreArchivo);
				System.out.println("Id Maestro: " + idMaestro);
				System.out.println("Id bancoagencia: " + cbBancoAgenciaConfrontaId);
				System.out.println("Fecha: " + listDataBanco.get(0).getFecha());
				cbdb.borrarArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro(), cbBancoAgenciaConfrontaId,
						listDataBanco.get(0).getFechaCreacion(), false);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaCR.class.getName()).log(Level.SEVERE, null, e);
			}

		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		//conn = dBConnection.getConneccion();
		int countRec = cbdb.insertarMasivo(listDataBanco, listDataBanco.get(0).getFormatofecha());
		CBDataSinProcesarDAO cbsp = new CBDataSinProcesarDAO();
		int dataSinProcc = cbsp.insertarMasivo(listSinProcesarBanco);

		// Llama al sp CB_CONCILIACION_SP ejecutado por hilo
		
		try {
			/*
			con_thread = new ProcesaConciliacionThread(listDataBanco.get(0).getIdCargaMaestro(), agenciaId, conn,
					listDataBanco, listSinProcesarBanco, formatoFechaConfronta);
			con_thread.start();
			*/
			//sp comisiones
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
					"inicia el proceso de comisiones confrontas");
			if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
				Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
						"Finaliza el proceso de comisiones confrontas");
			}
			
		} catch (Exception e) {
			System.out.println("Error al llamar al hilo para el proceso de conciliacion y ajustes: " + e.getMessage());
		}

		Messagebox.show(
				"Se a cargado la entidad bancaria " + misession.getAttribute("entidad").toString() + " " + countRec
						+ " Registos Grabados " + dataSinProcc + " Registos Sin Procesar ",
				"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);

		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);

	}

}
