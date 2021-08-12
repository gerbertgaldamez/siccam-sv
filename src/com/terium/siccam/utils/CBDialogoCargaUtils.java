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
import com.terium.siccam.exception.CustomExcepcion;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;

public class CBDialogoCargaUtils extends ControladorBase {
	/**
	 * 
	 */
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
	public void cargaConfrontas() throws SQLException, CustomExcepcion {

		entidad = (String) misession.getAttribute("entidad");
		//ConciliacionMultipleService multipleService;
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
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"borra el archivo.");
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Nombre archivo: " + nombreArchivo);
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Id Maestro: " + idMaestro);
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Id bancoagencia: " + cbBancoAgenciaConfrontaId);
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Fecha: " + listDataBanco.get(0).getFecha());
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Formato Fecha: " + listDataBanco.get(0).getDia());
			cbdb.borrarArchivo(nombreArchivo, idMaestro, cbBancoAgenciaConfrontaId,
					listDataBanco.get(0).getFechaCreacion(), true);
		}
		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		// Llama al sp CB_CONCILIACION_SP ejecutado por hilo
		//ProcesaConciliacionThread con_thread;

		

		int countRec = cbdb.insertarMasivo(listDataBanco, listDataBanco.get(0).getFormatofecha());
		CBDataSinProcesarDAO cbsp = new CBDataSinProcesarDAO();
		int dataSinProcc = cbsp.insertarMasivo(listSinProcesarBanco);
		try {

			// Obteniendo total de fechas diferentes en la confronta
			int dateConfrontas = cbdb.getDateConfronta(listDataBanco.get(0).getIdCargaMaestro());
			// Obteniendo total de convenios diferentes en la confronta
			int conveniosConfronta = cbdb.getCantidadConvenios(listDataBanco.get(0).getIdCargaMaestro());
			
			if (conveniosConfronta > 1) {
				System.out.println("Llama al proceso para separar confrontas por convenio");
				if (cbdb.ejecutaProcesoCargaConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
					Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
							"Finaliza el proceso para separar confrontas por convenio");
					cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
							"Finaliza el proceso para separar confrontas por convenio", "");
				} else {
					Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
							"Ocurrio un error al ejecutar el proceso para separar confrontas por convenio");
				}
			} else {
				if (dateConfrontas > 1) {
					Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
							"Llama al proceso para separar las fechas de la confronta");
					cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
							" Archivo: " + nombreArchivo + "_Inicia el proceso para separar confrontas por fecha.", "");
					if (cbdb.ejecutaProcesoSeparacionFechasConfronta(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
						Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
								"Separacion de fechas de confrontas realizada exitosamente!");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(), " Archivo: " + nombreArchivo
								+ "_Separacion de fechas de confrontas realizada exitosamente", "");
					} else {
						Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
								"Ocurrio un error al ejecutar la separacion de fechas");
					}
				} else {
					Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
							"Llama al proceso para conciliacion");
					cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
							" Archivo: " + nombreArchivo + "_Inicia el proceso de conciliacion.", "");
					if (cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
						Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
								"Finaliza el proceso de conciliacion");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								" Archivo: " + nombreArchivo + "_Finaliza el proceso de conciliacion", "");
						//sp comisiones
						Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
								"inicia el proceso de comisiones confrontas");
						if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
							Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
									"Finaliza el proceso de comisiones confrontas");
						}
					} else {
						Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
								"No se pudo ejecutar el proceso de conciliacion");
					}
				}
				// dBConnection.closeConneccion(conn);

			}

		} catch (Exception e) {
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.SEVERE, null, e);
		}

		Messagebox.show("Se a cargado la entidad bancaria " + entidad
				+ ".\n\nDetalle de carga:\n" + countRec + " registos grabados\n" + dataSinProcc
				+ " registos sin procesar ", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);

		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);

	}

	@SuppressWarnings("unchecked")
	public void cargaConfrontaDelDia() throws SQLException, CustomExcepcion {
		entidad = (String) misession.getAttribute("entidad");
		//ConciliacionMultipleService multipleService;
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
		if (cbdb.consultaExistenciaArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro())) {
			try {
				System.out.println("borra el archivo.");
				System.out.println("Nombre archivo: " + nombreArchivo);
				System.out.println("Id Maestro: " + idMaestro);
				System.out.println("Id bancoagencia: " + cbBancoAgenciaConfrontaId);
				System.out.println("Fecha: " + listDataBanco.get(0).getFecha());
				cbdb.borrarArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro(), cbBancoAgenciaConfrontaId,
						listDataBanco.get(0).getFechaCreacion(), false);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		if (cbdb.consultaExistenciaArchivoMultiple(nombreArchivo, idMaestro)) {
			// borra archivo
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"borra el archivo.");
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Nombre archivo: " + nombreArchivo);
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Id Maestro: " + idMaestro);
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Id bancoagencia: " + cbBancoAgenciaConfrontaId);
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Fecha: " + listDataBanco.get(0).getFecha());
			Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO, null,
					"Formato Fecha: " + listDataBanco.get(0).getDia());
			cbdb.borrarArchivo(nombreArchivo, idMaestro, cbBancoAgenciaConfrontaId,
					listDataBanco.get(0).getFechaCreacion(), true);
		}

		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		System.out.println("tama;o de lista en dialogo ) " + listDataBanco.size());
		System.out.println("tama;o de lista en dialogo ) " + listDataBanco.get(0).getFormatofecha());
		int countRec = cbdb.insertarMasivo(listDataBanco, listDataBanco.get(0).getFormatofecha());
		CBDataSinProcesarDAO cbsp = new CBDataSinProcesarDAO();
		int dataSinProcc = cbsp.insertarMasivo(listSinProcesarBanco);
		// dBConnection.closeConneccion(conn);

		// Obteniendo total de fechas diferentes en la confronta
		int dateConfrontas = cbdb.getDateConfronta(listDataBanco.get(0).getIdCargaMaestro());

		System.out.println("valida por convenios " + listDataBanco.get(0).isConvenioconf());
		if (listDataBanco.get(0).isConvenioconf()) {
			System.out.println("Llama al proceso para separar confrontas por convenio");
			if (cbdb.ejecutaProcesoCargaConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
				Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
						"Finaliza el proceso para separar confrontas por convenio");
				cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
						"Finaliza el proceso para separar confrontas por convenio", "");
			} else {
				Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
						"Ocurrio un error al ejecutar el proceso para separar confrontas por convenio");
			}
		} else {

			System.out.println("Llama al proceso para conciliacion");

			if (dateConfrontas > 1) {
				System.out.println("Llama al proceso para seprarar las fechas de la confronta");
				if (cbdb.ejecutaProcesoSeparacionFechasConfronta(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
					Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
							"Separacion de fechas de confrontas realizada exitosamente!");
					cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
							" Archivo: " + nombreArchivo + "_Separacion de fechas de confrontas realizada exitosamente",
							"");
				} else {
					Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
							"Ocurrio un error al ejecutar la separacion de fechas");
				}

				System.out.println("Sepracion de fechas de confrontas realizada exitosamente!");
			} else {
				System.out.println("Llama al proceso para conciliacion");
				cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro());
				//sp comisiones
				Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
						"inicia el proceso de comisiones confrontas");
				if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
					Logger.getLogger(CBDialogoCargaUtils.class.getName()).log(Level.INFO,
							"Finaliza el proceso de comisiones confrontas");
				}
				
			}
		}
		Messagebox.show(
				"Se a cargado la entidad bancaria " + misession.getAttribute("entidad").toString() + " " + countRec
						+ " Registos Grabados " + dataSinProcc + " Registos Sin Procesar ",
				"ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);

		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);

	}

	public void cargaDeNuevoPagos() {
		// levantar dialogo de espera...

		// llama al sp CB_PAGO_INDIVIDUAL

		//CBDataBancoDAO cdbd = new CBDataBancoDAO();
		/*
		 * if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
		 * cdbd.ejecutaSPPagosIndividual(cbBancoAgenciaConfrontaId,
		 * listDataBanco.get(0).getDia(), formatoFechaConfronta); }
		 */
		// cdbd.ejecutaSPPagosIndividual(cbBancoAgenciaConfrontaId,
		// listDataBanco.get(0).getDia());
		// cierra el dialogo de espera...
	}

	public String getMensajeDialogo() {
		return mensajeDialogo;
	}

	public void setMensajeDialogo(String mensajeDialogo) {
		this.mensajeDialogo = mensajeDialogo;
	}
}
