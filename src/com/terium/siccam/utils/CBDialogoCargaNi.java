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
import com.terium.siccam.implement.ConciliacionMultipleService;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;

public class CBDialogoCargaNi extends ControladorBase {

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

		// borra el archivo y registros si encuentra uno igual.
		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		if (cbdb.consultaExistenciaArchivo(nombreArchivo, idMaestro)) {
			// borra archivo
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null, "borra el archivo.");
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Nombre archivo: " + nombreArchivo);
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null, "Id Maestro: " + idMaestro);
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Id bancoagencia: " + cbBancoAgenciaConfrontaId);
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Fecha: " + listDataBanco.get(0).getFecha());
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Formato Fecha: " + listDataBanco.get(0).getDia());
			cbdb.borrarArchivo(nombreArchivo, idMaestro, cbBancoAgenciaConfrontaId,
					listDataBanco.get(0).getFechaCreacion(), false);
		}

		/* Existencia de archivo multiple */
		if (cbdb.consultaExistenciaArchivoMultiple(nombreArchivo, idMaestro)) {
			// borra archivo
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null, "borra el archivo.");
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Nombre archivo: " + nombreArchivo);
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null, "Id Maestro: " + idMaestro);
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Id bancoagencia: " + cbBancoAgenciaConfrontaId);
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Fecha: " + listDataBanco.get(0).getFecha());
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Formato Fecha: " + listDataBanco.get(0).getDia());
			cbdb.borrarArchivo(nombreArchivo, idMaestro, cbBancoAgenciaConfrontaId,
					listDataBanco.get(0).getFechaCreacion(), true);
		}
		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		int countRec = cbdb.insertarMasivo(listDataBanco, listDataBanco.get(0).getFormatofecha());
		CBDataSinProcesarDAO cbsp = new CBDataSinProcesarDAO();
		int dataSinProcc = cbsp.insertarMasivo(listSinProcesarBanco);
		// Llama al sp CB_CONCILIACION_SP ejecutado por hilo

		// Obteniendo total de fechas diferentes en la confronta
		int dateConfrontas = cbdb.getDateConfronta(listDataBanco.get(0).getIdCargaMaestro());
		if (dateConfrontas > 1) {
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Llama al proceso para separar las fechas de la confronta");
			cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
					" Archivo: " + nombreArchivo + "_Inicia el proceso para separar confrontas por fecha.", "");
			if (cbdb.ejecutaProcesoSeparacionFechasConfronta(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
				Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
						"Separacion de fechas de confrontas realizada exitosamente!");
				cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
						" Archivo: " + nombreArchivo + "_Separacion de fechas de confrontas realizada exitosamente",
						"");
			} else {
				Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
						"Ocurrio un error al ejecutar la separacion de fechas");
			}
		} else {
			Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null,
					"Llama al proceso para conciliacion");
			cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
					" Archivo: " + nombreArchivo + "_Inicia el proceso de conciliacion.", "");
			if (cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
				Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
						"Finaliza el proceso de conciliacion");
				cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
						" Archivo: " + nombreArchivo + "_Finaliza el proceso de conciliacion", "");
				//sp comisiones
				Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
						"inicia el proceso de comisiones confrontas");
				if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
					Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
							"Finaliza el proceso de comisiones confrontas");
				}
			} else {
				Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO,
						"No se pudo ejecutar el proceso de conciliacion");
			}
		}
		Messagebox.show(
				"Se a cargado la entidad bancaria " + entidad + ".\n\nDetalle de carga:\n" + countRec
						+ " registos grabados\n" + dataSinProcc + " registos sin procesar ",
				"ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);

	}

	@SuppressWarnings("unchecked")
	public void cargaConfrontaDelDia() throws SQLException, CustomExcepcion {
		entidad = (String) session.getAttribute("entidad");
		ConciliacionMultipleService multipleService;
		agenciaId = (Integer) session.getAttribute("agencia");
		mensajeDialogo = (String) session.getAttribute("mensaje");
		nombreArchivo = (String) session.getAttribute("archivo");
		idMaestro = (String) session.getAttribute("idMaestro");
		tipoDeCarga = (String) session.getAttribute("tipoMensaje");
		listDataBanco = (List<CBDataBancoModel>) session.getAttribute("listaBanco");
		cbBancoAgenciaConfrontaId = (Integer) session.getAttribute("cbBancoAgenciaConfrontaId");
		listSinProcesarBanco = (List<CBDataSinProcesarModel>) session.getAttribute("listaSinProcesar");
		estadoVentana = (Boolean) session.getAttribute("estadoVentana");
		formatoFechaConfronta = (String) session.getAttribute("formatoFechaConfronta");

		// borra el archivo y registros si encuentra uno igual.
		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		if (cbdb.consultaExistenciaArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro()))
			try {
				cbdb.borrarArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro(), cbBancoAgenciaConfrontaId,
						listDataBanco.get(0).getFechaCreacion(), false);
			} catch (Exception e) {
				Logger.getLogger(CBDialogoCargaNi.class.getName()).log(Level.INFO, null, e);
			}

		// INSERTA LAS LISTAS PROCESADAS Y SIN PROCESAR
		int countRec = cbdb.insertarMasivo(listDataBanco, listDataBanco.get(0).getFormatofecha());
		CBDataSinProcesarDAO cbsp = new CBDataSinProcesarDAO();
		int dataSinProcc = cbsp.insertarMasivo(listSinProcesarBanco);

		// Llama al sp CB_CONCILIACION_SP ejecutado por hilo

		Messagebox.show("Se a cargado la entidad bancaria " + entidad + " " + countRec + " Registos Grabados "
				+ dataSinProcc + " Registos Sin Procesar ", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);

		// elimina y limpia los campos
		CBProcessFileUploadController fpu = new CBProcessFileUploadController();
		fpu.cerrarDetalleCarga(false, idMaestro);

	}

}
