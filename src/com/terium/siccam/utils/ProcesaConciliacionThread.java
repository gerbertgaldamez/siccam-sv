/**
 * 
 */
package com.terium.siccam.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zul.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.dao.CBDataSinProcesarDAO;
import com.terium.siccam.exception.CustomExcepcion;
import com.terium.siccam.implement.ConciliacionMultipleService;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.model.ListaCombo;
import com.terium.siccam.service.CBCargaDataBancoMultServImpl;

/**
 * @author benjamin
 * 
 */
public class ProcesaConciliacionThread extends Thread {

	String idCargaMaestro;
	Connection conexion;
	int agencia;
	List<CBDataBancoModel> listDataBanco;
	String formatoFechaConfronta; //CarlosGodinez -> 25/10/2017

	// List<CBDataSinProcesarModel> listSinProcesarBanco;

	/**
	 * Modifica CarlosGodinez -> 25/10/2017
	 * Se agrega formatoFechaConfronta 
	 * */
	public ProcesaConciliacionThread(String idCargaMaestro, int agencia,
			Connection conexion, List<CBDataBancoModel> listDataBanco,
			List<CBDataSinProcesarModel> listSinProcesarBanco, String formatoFechaConfronta) {
		super();
		this.idCargaMaestro = idCargaMaestro;
		this.conexion = conexion;
		this.listDataBanco = listDataBanco;
		this.agencia = agencia;
		this.formatoFechaConfronta = formatoFechaConfronta; //CarlosGodinez -> 25/10/2017
		// this.listSinProcesarBanco = listSinProcesarBanco;
	}

	public void run() {
		ejecutaProceso();
	}

	public void ejecutaProceso() {
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		String fechaV = listDataBanco.get(0).getDia().toString();

		System.out.println("llama al proceso de conciliaciones.");
		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		
		System.out.println("Llama al proceso para conciliacion");
		cbdb.ejecutaProcesoConciliacion(idCargaMaestro);
		/**
		 * Agrega Carlos Godinez -> Actualizar fecha de archivo insertado
		 * 
		 * Solo aplica para archivos que no contienen mas de un convenio y mas de una fecha
		 * 
		CBArchivosInsertadosDAO cvaidao = new CBArchivosInsertadosDAO();
		System.out.println("Formato fecha enviado = " + formatoFechaConfronta);
		System.out.println("Fecha que contiene archivo enviada = " + fechaV);
		if(cvaidao.updateFechaArchivo(this.idCargaMaestro, formatoFechaConfronta, fechaV)) {
			System.out.println("\n*** Se ha modificado la fecha que contiene el archivo con exito ***\n");
		} */
		
		if (conexion != null) {
			try {
				conexion.close();
			} catch (SQLException e) {
				Logger.getLogger(ProcesaConciliacionThread.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
}
