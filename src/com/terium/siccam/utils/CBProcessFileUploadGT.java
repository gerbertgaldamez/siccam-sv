package com.terium.siccam.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.terium.siccam.controller.CBProcessFileUploadController;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.implement.ConciliacionMultipleService;
import com.terium.siccam.implement.ProcessFileTxtServiceGT;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.service.CBCargaDataBancoMultServImpl;
import com.terium.siccam.service.CBDataSinProcesarMultServImpl;
import com.terium.siccam.service.ProcessFileTxtServImplGT;

public class CBProcessFileUploadGT {
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	CBDataBancoDAO bancoDAO = new CBDataBancoDAO();

	Label lblMensaje;
	Image imgEstatus;
	int idBanco;;
	int idAgencia;
	int idConfronta;
	String nombreBanco;
	int cantidadAgrupacionConfronta;
	String formatoFechaConfronta;
	String format;
	private Media media;
	String usuario;
	static String nombreArchivo;
	private InputStream is;
	private boolean estadoVentana = false;
	String mensaje1 = "Esta confronta ya ha sido cargada previamente, esta seguro de volverla a cargar?";
	String mensaje2 = "Ya hay informacion de este dia cargada, desea continuar?";

	//DBConnection dBConnection = new DBConnection();
	//private Connection conn;
	private static List<CBDataBancoModel> listDataBanco;
	private static List<CBDataSinProcesarModel> listSinProcesarBanco;
	private ConciliacionMultipleService multipleService;

	public void mapeoCargaConfrontasGT() throws ParseException {

		nombreArchivo = (String) misession.getAttribute("nombreArchivo");
		usuario = (String) misession.getAttribute("usuario");
		media = (Media) misession.getAttribute("media");
		format = (String) misession.getAttribute("format");
		formatoFechaConfronta = (String) misession.getAttribute("formatoFechaConfronta");
		cantidadAgrupacionConfronta = (Integer) misession.getAttribute("cantidadAgrupacionConfronta");
		nombreBanco = (String) misession.getAttribute("entidad");
		idBanco = (Integer) misession.getAttribute("banco");
		idAgencia = (Integer) misession.getAttribute("agencia");
		idConfronta = (Integer) misession.getAttribute("confronta");
		int idBAC = bancoDAO.obtieneIdBancoAgeConfro(idBanco, idAgencia, idConfronta);
		String tipos = bancoDAO.obtieneTipo(idBanco, idAgencia, idConfronta);

		if ("txt".equals(media.getFormat()) | format.toUpperCase().equals("log".toUpperCase())
				| format.toUpperCase().equals("dat".toUpperCase())) {

			System.out.println("id idBAC configuracion: " + idBAC);
			BigDecimal comision = bancoDAO.recuperaComision(idBAC);
			misession.setAttribute("cbBancoAgenciaConfrontaId", idBAC);
			misession.setAttribute("comisionConfronta", comision);

			// *****************************************************
			leerArchivoTxt(idBanco, idAgencia, idConfronta, idBAC, usuario, media.getName(), tipos, comision);

		} else if ("xlsx".equals(media.getFormat())) {
			Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
					"nombre de archivo antes de guardar: " + nombreArchivo);

			// asigna el valor de la entidad para su uso previo
			misession.setAttribute("entidad", nombreBanco);
			misession.setAttribute("banco", idBanco);
			misession.setAttribute("agencia", idAgencia);
			misession.setAttribute("confronta", idConfronta);

			// obtiene cbbancoagenciaconfrontaid
			CBDataBancoDAO bancoDAO = new CBDataBancoDAO();

			System.out.println("id idBAC: " + idBAC);
			BigDecimal comision = bancoDAO.recuperaComision(idBAC);
			misession.setAttribute("comisionConfronta", comision);
			misession.setAttribute("cbBancoAgenciaConfrontaId", idBAC);
			// int fila = 0;
			String registro = "";
			String linea = "";
			String lineaCompleta = "";
			// CarlosGodinez -> 31/08/2017
			// DateFormat df = new SimpleDateFormat(formatoFechaConfronta);
			SimpleDateFormat df = new SimpleDateFormat(formatoFechaConfronta);
			//df.setTimeZone(TimeZone.getTimeZone("UTC"));

			DateFormatSymbols symbols = df.getDateFormatSymbols();
			symbols = (DateFormatSymbols) symbols.clone();
			symbols.setAmPmStrings(new String[] { "a.m.", "p.m.", "AM", "PM" });
			df.setDateFormatSymbols(symbols);
			// FIN CarlosGodinez -> 31/08/2017
			System.out.println("Formato de fecha: " + formatoFechaConfronta);
			DataFormatter formatter = new DataFormatter();
			try {
				XSSFWorkbook libro = (XSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				XSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					linea = "";
					XSSFRow row = (XSSFRow) rows.next();
					Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
							"**** LEE LINEAS DE ARCHIVO EXCEL ****");
					/**
					 * Modify and commented by CarlosGodinez -> 30/08/2017 se lee cantidad de
					 * agrupacion para la iteracion de filas de archivo Excel
					 */
					for (int celda = 0; celda < cantidadAgrupacionConfronta; celda++) {
						if (row.getCell(celda) != null) {
							switch (row.getCell(celda).getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								/**
								 * Ha comentariado Juankrlos --:> 01/11/2017 if
								 * (DateUtil.isCellDateFormatted(row.getCell(celda))) { registro =
								 * df.format(row.getCell(celda).getDateCellValue()); } else { registro =
								 * formatter.formatCellValue(row.getCell(celda)); }
								 */
								//registro = formatter.formatCellValue(row.getCell(celda));comentario ovidio Santos
								if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
									registro = df.format(row.getCell(celda).getDateCellValue());
								} else {
									registro = formatter.formatCellValue(row.getCell(celda));
								}
								break;
							case Cell.CELL_TYPE_STRING:
								registro = formatter.formatCellValue(row.getCell(celda));
								break;
							case Cell.CELL_TYPE_BLANK:
								registro = "0";
								break;
							}
						}
						if (registro == null || registro.equals("")) {
							registro = "0";
						}
						Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
								"Valor de celda = " + registro);
						if ("".equals(linea)) {
							linea = registro;
						} else {
							linea = linea + "\t" + registro;
						}
						registro = ""; //CarlosGodinez -> 17/08/2018
					}
					Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
							"**** FIN LEE LINEAS DE ARCHIVO EXCEL ****");
					linea = linea.replace("\"", "");
					lineaCompleta = lineaCompleta + linea + "\n";
				}
				is = new ByteArrayInputStream(lineaCompleta.getBytes());
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
						"Todas las lineas: \n" + lineaCompleta);
			} catch (InvalidFormatException e) {
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
				is = media.getStreamData();
			} catch (Exception e) {
				System.out.println("Ha ocurrido un error: " + e.getMessage());
				Messagebox.show("Ha ocurrido un error al intentar cargar el archivo de confronta", "ATENCION",
						Messagebox.OK, Messagebox.ERROR);
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
			}
			leerArchivoTxt(idBanco, idAgencia, idConfronta, idBAC, usuario, media.getName(), tipos, comision);

		} else if ("xls".equals(media.getFormat())) {
			Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
					"nombre de archivo antes de guardar: " + nombreArchivo);

			// asigna el valor de la entidad para su uso previo
			misession.setAttribute("entidad", nombreBanco);
			misession.setAttribute("banco", idBanco);
			misession.setAttribute("agencia", idAgencia);
			misession.setAttribute("confronta", idConfronta);

			// obtiene cbbancoagenciaconfrontaid

			System.out.println("id idBAC: " + idBAC);
			BigDecimal comision = bancoDAO.recuperaComision(idBAC);
			misession.setAttribute("comisionConfronta", comision);
			misession.setAttribute("cbBancoAgenciaConfrontaId", idBAC);
			// int fila = 0;
			String registro = "";
			String linea = "";
			String lineaCompleta = "";
			// CarlosGodinez -> 31/08/2017
			// DateFormat df = new SimpleDateFormat(formatoFechaConfronta);
			SimpleDateFormat df = new SimpleDateFormat(formatoFechaConfronta);
			//df.setTimeZone(TimeZone.getTimeZone("UTC"));

			DateFormatSymbols symbols = df.getDateFormatSymbols();
			symbols = (DateFormatSymbols) symbols.clone();
			symbols.setAmPmStrings(new String[] { "a.m.", "p.m.", "AM", "PM" });
			df.setDateFormatSymbols(symbols);
			// FIN CarlosGodinez -> 31/08/2017
			DataFormatter formatter = new DataFormatter();
			try {
				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();

				while (rows.hasNext()) {
					linea = "";
					HSSFRow row = (HSSFRow) rows.next();
					Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO, null,
							"**** LEE LINEAS DE ARCHIVO EXCEL ****");
					/**
					 * Modify and commented by CarlosGodinez -> 30/08/2017 se lee cantidad de
					 * agrupacion para la iteracion de filas de archivo Excel
					 */
					for (int celda = 0; celda < cantidadAgrupacionConfronta; celda++) {
						if (row.getCell(celda) != null) {
							switch (row.getCell(celda).getCellType()) {
							case Cell.CELL_TYPE_NUMERIC:
								/**
								 * Ha comentariado Juankrlos --:> 01/11/2017 if
								 * (DateUtil.isCellDateFormatted(row.getCell(celda))) { registro =
								 * df.format(row.getCell(celda).getDateCellValue()); } else { registro =
								 * formatter.formatCellValue(row.getCell(celda)); }
								 */
								//registro = formatter.formatCellValue(row.getCell(celda));
								if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
									registro = df.format(row.getCell(celda).getDateCellValue());
								} else {
									registro = formatter.formatCellValue(row.getCell(celda));
								}
								break;
							case Cell.CELL_TYPE_STRING:
								registro = formatter.formatCellValue(row.getCell(celda));
								break;
							case Cell.CELL_TYPE_BLANK:
								registro = "0";
								break;
							}
						}
						if (registro == null || registro.equals("")) {
							registro = "0";
						}
						Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO, null,
								"Valor de celda = " + registro);
						if ("".equals(linea)) {
							linea = registro;
						} else {
							linea = linea + "\t" + registro;
						}
						registro = ""; //CarlosGodinez -> 17/08/2018
					}
					Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
							"**** FIN LEE LINEAS DE ARCHIVO EXCEL ****");
					linea = linea.replace("\"", "");
					lineaCompleta = lineaCompleta + linea + "\n";
				}

				is = new ByteArrayInputStream(lineaCompleta.getBytes());
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
						"Todas las lineas: \n" + lineaCompleta);
			} catch (InvalidFormatException e) {
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
				is = media.getStreamData();
			} catch (Exception e) {
				System.out.println("Ha ocurrido un error: " + e.getMessage());
				Messagebox.show("Ha ocurrido un error al intentar cargar el archivo de confronta", "ATENCION",
						Messagebox.OK, Messagebox.ERROR);
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
			}

			leerArchivoTxt(idBanco, idAgencia, idConfronta, idBAC, usuario, media.getName(), tipos, comision);

		} else {
			Messagebox.show("El formato del archivo seleccionado no es legible para el sistema!", "ATENCIÓN",
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

	}

	private void leerArchivoTxt(int idBanco, int idAgencia, int idConfigAgencia, int idConfronta, String user,
			String nombreArchivo, String tipo, BigDecimal comision) throws ParseException {
		ProcessFileTxtServiceGT fileTxtService = new ProcessFileTxtServImplGT();
		BufferedReader bufferedReader = null;
		String format = media.getName().substring(media.getName().length() - 3, media.getName().length());
		System.out.println("Formato con substring leyendo: " + format);
		if ("txt".equals(media.getFormat())) {
			bufferedReader = new BufferedReader(media.getReaderData());
		} else if (format.toUpperCase().equals("log".toUpperCase())) {
			bufferedReader = new BufferedReader(new InputStreamReader(media.getStreamData()));
		} else if (format.toUpperCase().equals("dat".toUpperCase())) { // CarlosGodinez->12/09/2017
			bufferedReader = new BufferedReader(new InputStreamReader(media.getStreamData()));
		} else if ("xls".equals(media.getFormat())) {
			bufferedReader = new BufferedReader(new InputStreamReader(is));
			// bufferedReader = new BufferedReader(new
			// InputStreamReader(media.getStreamData()));
		} else if ("xlsx".equals(media.getFormat())) {
			bufferedReader = new BufferedReader(new InputStreamReader(is));
		}

		listDataBanco = new ListModelList<CBDataBancoModel>(fileTxtService.leerArchivoGT(bufferedReader, idBanco,
				idAgencia, idConfigAgencia, idConfronta, user, nombreArchivo, tipo, comision));
		System.out.println("datos procesados: " + listDataBanco.size());
		listSinProcesarBanco = new ListModelList<CBDataSinProcesarModel>(fileTxtService.getDataSinProcesar());
		System.out.println("datos sin procesar: " + listSinProcesarBanco.size());
		// dBConnection.closeConneccion(conn);
		// System.out.println("\nIMPRESION DE REGISTROS SIN PROCESAR:\n");

		if (listDataBanco.size() == 0 && listSinProcesarBanco.size() == 0) {
			Messagebox.show("El archivo se encuentra en blanco... ", "ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			BindUtils.postGlobalCommand(null, null, "refrescar", null);
		} else if (listSinProcesarBanco.size() > 0 && listDataBanco.size() <= 0) {
			Messagebox.show(
					"El archivo es incorrecto favor de verificar si se a seleccionado bien la configuración... ",
					"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			// setOcupado(false);
			nombreArchivo = "";
			// BindUtils.postGlobalCommand(null, null, "refrescar", null);

		} else {
			if (listDataBanco.get(0).getObservacion() == null) {
				try {
					grabarData();
				} catch (SQLException e) {
					Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
				} catch (NamingException e) {
					Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
				}
				// mostrarDetalle();
			} else {
				Messagebox.show("A ocurrido un problema por favor intente cargar de nuevo el archivo", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		}

	}

	@SuppressWarnings("unused")
	public void grabarData() throws ParseException, SQLException, NamingException {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		CBDataBancoDAO cbdb = new CBDataBancoDAO();

		String fechaV = listDataBanco.get(0).getDia().toString();

		if (cbdb.consultaExistenciaArchivo(nombreArchivo, listDataBanco.get(0).getIdCargaMaestro())) {
			Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
					"*** ENTRA A CONSULTA EXISTENCIA ARCHIVO ***");
			// carga las variables
			misession.setAttribute("mensaje", mensaje1);
			misession.setAttribute("tipoMensaje", "1");
			misession.setAttribute("archivo", nombreArchivo);
			misession.setAttribute("idMaestro", listDataBanco.get(0).getIdCargaMaestro());
			misession.setAttribute("listaBanco", listDataBanco);
			misession.setAttribute("listaSinProcesar", listSinProcesarBanco);
			misession.setAttribute("estadoVentana", estadoVentana);

			// carga la nueva ventana y asigna las nuevas propiedades
			Window dialogCarga = (Window) Executions.createComponents("/dialogoCargaArchivo.zul", null, null);
			dialogCarga.doModal();

			System.out.println("fecha " + fechaV);
		}
		/* else if (cbdb.verificaCargaDataBancoGT(format.format(format.parse(fechaV)),
				misession.getAttribute("banco").toString(), misession.getAttribute("agencia").toString(),
				misession.getAttribute("confronta").toString())) {
			System.out.println("fecha " + fechaV);

			// carga las variables
			misession.setAttribute("mensaje", mensaje2);
			misession.setAttribute("tipoMensaje", "2");
			misession.setAttribute("archivo", nombreArchivo);
			misession.setAttribute("idMaestro", listDataBanco.get(0).getIdCargaMaestro());
			misession.setAttribute("listaBanco", listDataBanco);
			misession.setAttribute("listaSinProcesar", listSinProcesarBanco);
			misession.setAttribute("estadoVentana", estadoVentana);
			// carga la nueva ventana y asigna las nuevas propiedades
			Window dialogCarga = (Window) Executions.createComponents("/dialogoCargaArchivo.zul", null, null);
			dialogCarga.doModal();

		}
		*/ else {

			//conn = dBConnection.getConneccion();
			multipleService = new CBCargaDataBancoMultServImpl();
			int countRec = multipleService.insertarMas(listDataBanco);
			multipleService = new CBDataSinProcesarMultServImpl();
			int dataSinProcc = multipleService.insertarMas(listSinProcesarBanco);
			// dBConnection.closeConneccion(conn);

			// llama al sp CB_CONCILIACION_SP
			/*
			ProcesaConciliacionThread con_thread;
			if (conn.isClosed() || conn == null) {
				conn = dBConnection.getConneccion();
			}
*/
			///

			try {
				Integer agencia_ID = (Integer) misession.getAttribute("agencia");

				// Obteniendo total de fechas diferentes en la confronta
				int dateConfrontas = cbdb.getDateConfronta(listDataBanco.get(0).getIdCargaMaestro());
				// Obteniendo total de convenios diferentes en la confronta
				int conveniosConfronta = cbdb.getCantidadConvenios(listDataBanco.get(0).getIdCargaMaestro());
				
				if (conveniosConfronta > 1) {
					System.out.println("Llama al proceso para separar confrontas por convenio");
					if (cbdb.ejecutaProcesoCargaConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
						Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
								"Finaliza el proceso para separar confrontas por convenio");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								"Finaliza el proceso para separar confrontas por convenio", "");
					} else {
						Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
								"Ocurrio un error al ejecutar el proceso para separar confrontas por convenio");
					}
				} else {

					if (dateConfrontas > 1) {
						Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO, null,
								"Llama al proceso para separar las fechas de la confronta");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								" Archivo: " + nombreArchivo + "_Inicia el proceso para separar confrontas por fecha.",
								"");

						if (cbdb.ejecutaProcesoSeparacionFechasConfronta(
								listDataBanco.get(0).getIdCargaMaestro()) > 0) {
							Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
									"Separacion de fechas de confrontas realizada exitosamente!");
							cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
									"Separacion de fechas de confrontas realizada exitosamente", "");
						} else {
							Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
									"Ocurrio un error al ejecutar la separacion de fechas");
						}
					} else {
						Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO, null,
								"Llama al proceso para conciliacion");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								" Archivo: " + nombreArchivo + "_Inicia el proceso de conciliacion.", "");

						if (cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
							Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
									"Finaliza el proceso de conciliacion");
							cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
									" Archivo: " + nombreArchivo + "_Finaliza el proceso de conciliacion.", "");
							DateFormat df = new SimpleDateFormat("dd/MM/yy");
							Logger.getLogger(CBDialogoCargaGT.class.getName())
							.log(Level.INFO, "inicia proceso de ajustes pendientes");
							// realiza la llamada al proceso de ajustes pendientes
							try {
								cbdb.ejecutaProcesoAjustesPendientes(df.format(df.parse(fechaV)),
										agencia_ID);
								Logger.getLogger(CBDialogoCargaGT.class.getName())
								.log(Level.INFO, "finaliza proceso de ajustes pendientes");
							} catch (ParseException e1) {
								System.out.println("error al convertir la fecha: "
										+ e1.getMessage());
							}
							
							Logger.getLogger(CBDialogoCargaGT.class.getName())
							.log(Level.INFO, "inicia proceso de ajustes ");
							try {			
								
								cbdb.ejecutaProcesoAjustes(df.format(df.parse(fechaV)),agencia_ID);
								Logger.getLogger(CBDialogoCargaGT.class.getName())
								.log(Level.INFO, "finaliza proceso de ajustes");
							} catch (ParseException e1) {
								System.out.println("error al convertir la fecha: "
										+ e1.getMessage());
							}
							
							
							//sp comisiones
							Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
									"inicia el proceso de comisiones confrontas");
							if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
								Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
										"Finaliza el proceso de comisiones confrontas");
							}
						} else {
							Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.INFO,
									"No se pudo ejecutar el proceso de conciliacion");
						}
					}
				}
			} catch (Exception e) {
				Logger.getLogger(CBProcessFileUploadGT.class.getName()).log(Level.SEVERE, null, e);
			}

			Messagebox.show("Se a cargado la entidad bancaria " + misession.getAttribute("entidad").toString()
					+ ".\n\nDetalle de carga:\n " + countRec + " registos grabados\n" + dataSinProcc
					+ " registos sin procesar ", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
			CBProcessFileUploadController cbproces = new CBProcessFileUploadController();
			cbproces.cerrarDetalleCarga(false, listDataBanco.get(0).getIdCargaMaestro());
		}

	}

}
