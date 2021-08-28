package com.terium.siccam.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.terium.siccam.implement.ProcessFileTxtService;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.service.CBCargaDataBancoMultServImpl;
import com.terium.siccam.service.CBDataSinProcesarMultServImpl;
import com.terium.siccam.service.ProcessFileTxtServImplSV;

public class CBProcessFileUploadUtils {
	private static Logger log = Logger.getLogger(CBProcessFileUploadUtils.class);

	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	HttpSession session = (HttpSession) Sessions.getCurrent().getNativeSession();
	CBDataBancoDAO bancoDAO = new CBDataBancoDAO();

	Label lblMensaje;
	Image imgEstatus;
	int idBanco;;
	int idAgencia;
	int idConfronta;
	String nombreBanco;
	int cantidadAgrupacionConfronta;
	String nomenclaturaConfrota;
	String formatoFechaConfronta;
	String format;
	private Media media;
	String usuario;
	static String nombreArchivo;
	private InputStream is;
	private boolean estadoVentana = false;
	String mensaje1 = "Esta confronta ya ha sido cargada previamente, esta seguro de volverla a cargar?";
	String mensaje2 = "Ya hay informacion de este dia cargada, desea continuar?";

	private static List<CBDataBancoModel> listDataBanco;
	private static List<CBDataSinProcesarModel> listSinProcesarBanco;
	private ConciliacionMultipleService multipleService;

	public void mapeoCargaConfrontasSV() {
		String methodName = "mapeoCargaConfrontasSV()";
		log.debug(methodName + " -  inicia ");
		nombreArchivo = (String) session.getAttribute("nombreArchivo");
		usuario = (String) session.getAttribute("usuario");
		media = (Media) session.getAttribute("media");
		format = (String) session.getAttribute("format");
		formatoFechaConfronta = (String) session.getAttribute("formatoFechaConfronta");
		cantidadAgrupacionConfronta = (Integer) session.getAttribute("cantidadAgrupacionConfronta");

		nombreBanco = (String) session.getAttribute("entidad");
		idBanco = (Integer) session.getAttribute("banco");
		idAgencia = (Integer) session.getAttribute("agencia");
		idConfronta = (Integer) session.getAttribute("confronta");

		log.debug(methodName + " -  valida tipo de formato ");
		if ("txt".equals(media.getFormat()) || format.toUpperCase().equals("log".toUpperCase())
				|| format.toUpperCase().equals("dat".toUpperCase())) {

			// asigna el valor de la entidad para su uso previo

			int idBAC = bancoDAO.obtieneIdBancoAgeConfro(idBanco, idAgencia, idConfronta);
			String tipos = bancoDAO.obtieneTipo(idBanco, idAgencia, idConfronta);
			log.debug(methodName + " - tipos " + tipos);
			log.debug(methodName + " - id idBAC configuracion: " + idBAC);
			BigDecimal comision = bancoDAO.recuperaComision(idBAC);
			misession.setAttribute("cbBancoAgenciaConfrontaId", idBAC);
			misession.setAttribute("comisionConfronta", comision);

			// *****************************************************
			log.debug(methodName + " - inica metodo leerArchioTxt");
			leerArchivoTxt(idBanco, idAgencia, idConfronta, usuario, media.getName(), tipos, comision, idBAC);

		} else if ("xlsx".equals(media.getFormat())) {

			setNombreArchivo(media.getName());
			log.debug(methodName + " - nombre de archivo antes de guardar: " + getNombreArchivo());

			// asigna el valor de la entidad para su uso previo
			misession.setAttribute("entidad", nombreBanco);
			misession.setAttribute("banco", idBanco);
			misession.setAttribute("agencia", idAgencia);
			misession.setAttribute("confronta", idConfronta);

			int idBAC = bancoDAO.obtieneIdBancoAgeConfro(idBanco, idAgencia, idConfronta);
			String tipos = bancoDAO.obtieneTipo(idBanco, idAgencia, idConfronta);

			log.debug(methodName + " - tipos " + tipos);
			log.debug(methodName + " - id idBAC configuracion: " + idBAC);
			BigDecimal comision = bancoDAO.recuperaComision(idBAC);
			misession.setAttribute("comisionConfronta", comision);
			misession.setAttribute("cbBancoAgenciaConfrontaId", idBAC);

			String registro = "";
			String linea = "";
			String lineaCompleta = "";
			// CarlosGodinez -> 31/08/2017
			// DateFormat df = new SimpleDateFormat(formatoFechaConfronta);
			SimpleDateFormat df = new SimpleDateFormat(formatoFechaConfronta);
			// df.setTimeZone(TimeZone.getTimeZone("UTC"));

			DateFormatSymbols symbols = df.getDateFormatSymbols();
			symbols = (DateFormatSymbols) symbols.clone();
			symbols.setAmPmStrings(new String[] { "a.m.", "p.m.", "AM", "PM" });
			df.setDateFormatSymbols(symbols);
			// FIN CarlosGodinez -> 31/08/2017
			log.debug(methodName + " - Formato de fecha: " + formatoFechaConfronta);
			DataFormatter formatter = new DataFormatter();
			try {
				XSSFWorkbook libro = (XSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				XSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					linea = "";
					XSSFRow row = (XSSFRow) rows.next();
					log.debug(methodName + " - LEE LINEAS DE ARCHIVO EXCEL");
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
								// registro = formatter.formatCellValue(row.getCell(celda));comentario ovidio
								// Santos
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
						log.debug(methodName + " - Valor de celda = " + registro);
						if ("".equals(linea)) {
							linea = registro;
						} else {
							linea = linea + "\t" + registro;
						}
						registro = ""; // CarlosGodinez -> 17/08/2018
					}
					log.debug(methodName + " - FIN LEE LINEAS DE ARCHIVO EXCEL ");
					linea = linea.replace("\"", "");
					lineaCompleta = lineaCompleta + linea + "\n";
				}
				// fila++;
				// lineaCompleta = lineaCompleta + linea + "\n";
				// }
				is = new ByteArrayInputStream(lineaCompleta.getBytes());
				log.debug(methodName + " - Todas las lineas: \n" + lineaCompleta);
			} catch (InvalidFormatException e) {
				log.error(methodName + " -  Error * : " + e);
			} catch (IOException e) {
				log.error(methodName + " -  Error ** : " + e);
			} catch (IllegalArgumentException e) {
				log.error(methodName + " -  Error *** : " + e);
				is = media.getStreamData();
			} catch (Exception e) {
				log.error(methodName + " -  Error **** : " + e);
				Messagebox.show("Ha ocurrido un error al intentar cargar el archivo de confronta", "ATENCION",
						Messagebox.OK, Messagebox.ERROR);
			}
			leerArchivoTxt(idBanco, idAgencia, idConfronta, usuario, media.getName(), tipos, comision, idBAC);

		} else if ("xls".equals(media.getFormat())) {
			log.debug(methodName + " - archivo xls");
			setNombreArchivo(media.getName());
			log.debug(methodName + " - nombre de archivo antes de guardar: " + getNombreArchivo());
			// asigna el valor de la entidad para su uso previo
			misession.setAttribute("entidad", nombreBanco);
			misession.setAttribute("banco", idBanco);
			misession.setAttribute("agencia", idAgencia);
			misession.setAttribute("confronta", idConfronta);
			nomenclaturaConfrota = bancoDAO.obtenerNomenclaturaConfronta(idConfronta);

			log.debug(methodName + " Se obtiene nomenclatura confronta : " + nomenclaturaConfrota);
			// obtiene cbbancoagenciaconfrontaid
			int idBAC = bancoDAO.obtieneIdBancoAgeConfro(idBanco, idAgencia, idConfronta);
			String tipos = bancoDAO.obtieneTipo(idBanco, idAgencia, idConfronta);
			log.debug(methodName + "- tipos " + tipos);
			log.debug(methodName + " - id idBAC: " + idBAC);
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
			// df.setTimeZone(TimeZone.getTimeZone("UTC"));

			DateFormatSymbols symbols = df.getDateFormatSymbols();
			symbols = (DateFormatSymbols) symbols.clone();
			symbols.setAmPmStrings(new String[] { "a.m.", "p.m.", "AM", "PM" });
			df.setDateFormatSymbols(symbols);
			// FIN CarlosGodinez -> 31/08/2017
			DataFormatter formatter = new DataFormatter();
			String[] nomConfronta = nomenclaturaConfrota.split(",");
			int numColumn = 0;
			if (getIsColumn(nomConfronta)) {
				log.debug(methodName + " -  Valida nomenclatura Confronta ");
				numColumn = getNumColum(nomConfronta[1].toString());
				log.debug(methodName + " -  numero de columna es : " + numColumn);
			}
			try {
				log.debug(methodName + " - inicia crear workBook");
				Workbook wb = create(media.getStreamData());
				log.debug(methodName + " - crea Workbook");
				if (numColumn > 0) {
					log.debug(methodName + " - numero columna = " + numColumn);
					lineaCompleta = getDataColumn(numColumn, wb, registro, linea);

				} else {
					Sheet hoja = wb.getSheetAt(0);
					Iterator<Row> rows = hoja.iterator();
					log.debug(methodName + " - recorre Excel");
					while (rows.hasNext()) {
						linea = "";
						Row row = rows.next();
						log.debug(methodName + " - LEE LINEAS DE ARCHIVO EXCEL");
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
									// registro = formatter.formatCellValue(row.getCell(celda));
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
							log.debug(methodName + " - Valor de celda = " + registro);
							if ("".equals(linea)) {
								linea = registro;
							} else {
								linea = linea + "\t" + registro;
							}
							registro = ""; // CarlosGodinez -> 17/08/2018
						}
						log.debug(methodName + " - FIN LEE LINEAS DE ARCHIVO EXCEL");
						linea = linea.replace("\"", "");
						lineaCompleta = lineaCompleta + linea + "\n";
					}
				}

				// fila++;
				// lineaCompleta = lineaCompleta + linea + "\n";
				// }
				is = new ByteArrayInputStream(lineaCompleta.getBytes());
				log.debug(methodName + " - " + "Todas las lineas: \n" + lineaCompleta);
			} catch (InvalidFormatException e) {
				log.error(methodName + " - ERROR * : " + e);
			} catch (IOException e) {
				log.error(methodName + " - ERROR ** : " + e);
			} catch (IllegalArgumentException e) {
				log.error(methodName + " - ERROR *** : " + e);
				is = media.getStreamData();
			} catch (Exception e) {
				log.error(methodName + " - ERROR **** : " + e.getMessage());
				Messagebox.show("Ha ocurrido un error al intentar cargar el archivo de confronta", "ATENCION",
						Messagebox.OK, Messagebox.ERROR);
			}
			leerArchivoTxt(idBanco, idAgencia, idConfronta, usuario, media.getName(), tipos, comision, idBAC);

		} else {
			Messagebox.show("El formato del archivo seleccionado no es legible para el sistema!", "ATENCIÓN",
					Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	private String getDataColumn(int numColumn, Workbook wb, String registro, String linea) {
		String methodName = "getDataColumn()";
		String lineaCompleta = " ";
		Sheet hoja = wb.getSheetAt(0);
		Iterator<Row> rows = hoja.iterator();
		log.debug(methodName + " - recorrer columna # : " + numColumn);
		while (rows.hasNext()) {
			linea = "";
			Row row = rows.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			if (row.getRowNum() > 0) {
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (cell != null) {
						registro = getCellTypes(cell, wb);
						if (cell.getColumnIndex() == numColumn - 1) {
							linea = registro;

							log.debug(methodName + " - Registros = " + registro);
							registro = "";
						}

					}

				}
				linea = linea.replace("\"", "");
				lineaCompleta = lineaCompleta + linea + "\n";
			}

		}

		return lineaCompleta;
	}

	private String getCellTypes(Cell cell, Workbook wb) {
		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
		DataFormatter formatter = new DataFormatter();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String registro = null;
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			registro = "" + cell.getBooleanCellValue();
			break;

		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell))
				registro = "" + df.format(cell.getDateCellValue());

			else
				registro = "" + formatter.formatCellValue(cell);

			break;
		case Cell.CELL_TYPE_STRING:
			registro = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			registro = "";
			break;
		case Cell.CELL_TYPE_FORMULA:
			registro = "" + formatter.formatCellValue(cell, evaluator);
			break;
		}
		return registro;
	}

	private static int getNumColum(String nom) {
		log.debug("getNumColum() " + " - nomenclatura = " + nom);
		int columNum = 0;
		String[] numColum = nom.split("_");
		columNum = Integer.parseInt(numColum[1].toString().trim());

		log.debug("getNumColum() " + " - return numero columna  = " + columNum);
		return columNum;
	}

	private static boolean getIsColumn(String[] nomSplit) {
		for (int i = 0; i < nomSplit.length; i++) {
			if (nomSplit[i].toString().contains("COLM"))
				return true;
		}
		return false;
	}

	/**
	 * Creates the appropriate HSSFWorkbook / XSSFWorkbook from the given
	 * InputStream. Your input stream MUST either support mark/reset, or be wrapped
	 * as a {@link PushbackInputStream}!
	 */
	public static Workbook create(InputStream inp) throws IOException, InvalidFormatException {
		// If clearly doesn't do mark/reset, wrap up
		log.debug("create() - valida tipo archivo excel ");
		if (!inp.markSupported()) {
			log.debug("create() - inp.markSupported() " + !inp.markSupported());
			inp = new PushbackInputStream(inp, 8);
		}

		if (POIFSFileSystem.hasPOIFSHeader(inp)) {
			log.debug("create() - return HSSFWorkbook ");
			return new HSSFWorkbook(inp);
		}
		if (POIXMLDocument.hasOOXMLHeader(inp)) {
			log.debug("create() - return XSSFWorkbook ");
			return new XSSFWorkbook(OPCPackage.open(inp));
		}

		log.debug("create() - return IllegalArgumentException ");
		throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
	}

	private void leerArchivoTxt(int idBanco, int idAgencia, int idConfronta, String user, String nombreArchivo,
			String tipo, BigDecimal comision, int idAgeConfro) {
		String methodName = "leerArchivoTxt()";
		BufferedReader bufferedReader = null;
		try {
			ProcessFileTxtService fileTxtService = new ProcessFileTxtServImplSV();

			String format = media.getName().substring(media.getName().length() - 3, media.getName().length());
			log.debug(methodName + "- Formato con substring leyendo: " + format);
			if ("txt".equals(media.getFormat())) {
				bufferedReader = new BufferedReader(media.getReaderData());
			} else if ("log".toUpperCase().equals(format.toUpperCase())) {
				bufferedReader = new BufferedReader(new InputStreamReader(media.getStreamData()));
			} else if ("dat".toUpperCase().equals(format.toUpperCase())) { // CarlosGodinez->12/09/2017
				bufferedReader = new BufferedReader(new InputStreamReader(media.getStreamData()));
			} else if ("xls".equals(media.getFormat())) {
				log.debug(methodName + " - Examinar Data xls ");
				bufferedReader = new BufferedReader(new InputStreamReader(is));
				// bufferedReader = new BufferedReader(new
				// InputStreamReader(media.getStreamData()));
			} else if ("xlsx".equals(media.getFormat())) {
				bufferedReader = new BufferedReader(new InputStreamReader(is));
			}

			listDataBanco = new ListModelList<CBDataBancoModel>(fileTxtService.leerArchivo(bufferedReader, idBanco,
					idAgencia, idConfronta, user, nombreArchivo, tipo, comision, idAgeConfro));
			log.debug(methodName + " - " + "datos procesados ==> " + listDataBanco.size());
			listSinProcesarBanco = new ListModelList<CBDataSinProcesarModel>(fileTxtService.getDataSinProcesar());
			log.debug(methodName + " - " + " datos sin procesar ==> " + listSinProcesarBanco.size());
			log.debug(methodName + " - " + "\nIMPRESION DE REGISTROS SIN PROCESAR:\n");
			int countFallidos = 0;
			for (CBDataSinProcesarModel objeSinProc : listSinProcesarBanco) {
				countFallidos++;
				log.debug(methodName + " - cantidad de datos sin procesar :" + countFallidos);
				log.debug(methodName + " - cantidad de datos sin procesar lista :" + listSinProcesarBanco.size());
				log.debug(methodName + " - dataArchivo = " + objeSinProc.getDataArchivo());
				log.debug(methodName + " - Causa = " + objeSinProc.getCausa());
				log.debug(methodName + " - idCargaMaestro = " + objeSinProc.getIdCargaMaestro());
			}

			if (listDataBanco.size() == 0 && listSinProcesarBanco.size() == 0) {
				Messagebox.show("El archivo se encuentra en blanco... ", "ATENCIÓN", Messagebox.OK,
						Messagebox.EXCLAMATION);
				// BindUtils.postGlobalCommand(null, null, "refrescar", null);
			} else if (listSinProcesarBanco.size() > 0 && listDataBanco.size() <= 0) {
				Messagebox.show(
						"El archivo es incorrecto favor de verificar si se a seleccionado bien la configuración... ",
						"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
				// setOcupado(false);
				setNombreArchivo("");
				// BindUtils.postGlobalCommand(null, null, "refrescar", null);

			} else {
				if (listDataBanco.get(0).getObservacion() == null) {
					try {
						grabarData();
					} catch (SQLException e) {
						log.error(methodName + " - ERROR * : " + e);

					} catch (NamingException e) {
						log.error(methodName + " - ERROR ** : " + e);

					}
				} else {
					Messagebox.show("A ocurrido un problema por favor intente cargar de nuevo el archivo", "ATENCION",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}

			}
		} catch (Exception e) {
			log.error(methodName + " - ERROR - : " + e);
		} finally {

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (Exception e) {
					log.error(methodName + " - ERROR -- : " + e);
				}
			}
		}

	}

	@SuppressWarnings("unused")
	public void grabarData() throws ParseException, SQLException, NamingException {
		idBanco = (Integer) misession.getAttribute("banco");
		idAgencia = (Integer) misession.getAttribute("agencia");
		idConfronta = (Integer) misession.getAttribute("confronta");
		String methodName = "grabarData()";

		log.debug(methodName + " - id idAgencia a validar " + idAgencia);
		log.debug(methodName + " - id confronta a validar " + idConfronta);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		CBDataBancoDAO cbdb = new CBDataBancoDAO();
		log.debug(methodName + " - nombre archivo: " + media.getName());
		log.debug(methodName + " - id archivo: " + listDataBanco.get(0).getIdCargaMaestro());
		String fechaV = listDataBanco.get(0).getDia();

		if (cbdb.consultaExistenciaArchivo(media.getName(), listDataBanco.get(0).getIdCargaMaestro())) {
			log.debug(methodName + " - ENTRA A CONSULTA EXISTENCIA ARCHIVO ");
			// carga las variables
			misession.setAttribute("mensaje", mensaje1);
			misession.setAttribute("tipoMensaje", "1");
			misession.setAttribute("archivo", media.getName());
			misession.setAttribute("idMaestro", listDataBanco.get(0).getIdCargaMaestro());
			misession.setAttribute("listaBanco", listDataBanco);
			misession.setAttribute("listaSinProcesar", listSinProcesarBanco);
			misession.setAttribute("estadoVentana", estadoVentana);

			misession.setAttribute("formatoFechaConfronta", formatoFechaConfronta); // CarlosGodinez -> 25/10/2017

			Window dialogCarga = (Window) Executions.createComponents("/dialogoCargaArchivo.zul", null, null);
			dialogCarga.doModal();

			/*
			 * // carga la nueva ventana y asigna las nuevas propiedades dialogCarga =
			 * (Window) Executions.createComponents("/dialogoCargaArchivo.zul", null, null);
			 * 
			 * //session.setAttribute("winDialogoCarga", dialogCarga);
			 * 
			 * dialogCarga.doModal();
			 */
		} else if (cbdb.consultaExistenciaArchivoMultiple(getNombreArchivo(),
				listDataBanco.get(0).getIdCargaMaestro())) {
			log.debug(methodName + " - ENTRA A CONSULTA EXISTENCIA ARCHIVO MULTIPLE");
			// carga las variables
			misession.setAttribute("mensaje", mensaje1);
			misession.setAttribute("tipoMensaje", "1");
			misession.setAttribute("archivo", getNombreArchivo());
			misession.setAttribute("idMaestro", listDataBanco.get(0).getIdCargaMaestro());
			misession.setAttribute("listaBanco", listDataBanco);
			misession.setAttribute("listaSinProcesar", listSinProcesarBanco);
			misession.setAttribute("estadoVentana", estadoVentana);
			misession.setAttribute("formatoFechaConfronta", formatoFechaConfronta); // CarlosGodinez -> 25/10/2017

			// carga la nueva ventana y asigna las nuevas propiedades
			Window dialogCarga = (Window) Executions.createComponents("/dialogoCargaArchivo.zul", null, null);
			dialogCarga.doModal();

			/*
			 * } else if (cbdb.verificaCargaDataBanco(fechaV,
			 * misession.getAttribute("banco").toString(),
			 * misession.getAttribute("agencia").toString(),
			 * misession.getAttribute("cbBancoAgenciaConfrontaId").toString(),
			 * listDataBanco.get(0).getFormatofecha())) {
			 * System.out.println("*** ENTRA A VERIFICA DATA BANCO ***");
			 * 
			 * // carga las variables misession.setAttribute("mensaje", mensaje2);
			 * misession.setAttribute("tipoMensaje", "2"); misession.setAttribute("archivo",
			 * media.getName()); misession.setAttribute("idMaestro",
			 * listDataBanco.get(0).getIdCargaMaestro());
			 * misession.setAttribute("listaBanco", listDataBanco);
			 * misession.setAttribute("listaSinProcesar", listSinProcesarBanco);
			 * misession.setAttribute("estadoVentana", estadoVentana);
			 * 
			 * misession.setAttribute("formatoFechaConfronta", formatoFechaConfronta); //
			 * CarlosGodinez -> 25/10/2017
			 * 
			 * misession.setAttribute("", listDataBanco.get(0).getDia()); // carga la nueva
			 * ventana y asigna las nuevas propiedades Window dialogCarga = (Window)
			 * Executions.createComponents("/dialogoCargaArchivo.zul", null, null);
			 * dialogCarga.doModal();
			 */
		} else {
			log.debug(methodName + " - Asi manda la fecha formato: " + listDataBanco.get(0).getFormatofecha());
			// llama al sp CB_CONCILIACION_SP

			multipleService = new CBCargaDataBancoMultServImpl();
			int countRec = multipleService.insertarMas(listDataBanco, listDataBanco.get(0).getFormatofecha());
			multipleService = new CBDataSinProcesarMultServImpl();
			int dataSinProcc = multipleService.insertarMas(listSinProcesarBanco, listDataBanco.get(0).getDia());
			try {
				// dBConnection.closeConneccion(conn);
				// Obteniendo total de fechas diferentes en la confronta
				int dateConfrontas = cbdb.getDateConfronta(listDataBanco.get(0).getIdCargaMaestro());
				// Obteniendo total de convenios diferentes en la confronta
				int conveniosConfronta = cbdb.getCantidadConvenios(listDataBanco.get(0).getIdCargaMaestro());

				if (conveniosConfronta > 1) {
					log.debug(methodName + " - Llama al proceso para separar confrontas por convenio");
					if (cbdb.ejecutaProcesoCargaConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
						log.debug(methodName + " - Finaliza el proceso para separar confrontas por convenio");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								"Finaliza el proceso para separar confrontas por convenio", "");
					} else {
						log.debug(methodName
								+ " - Ocurrio un error al ejecutar el proceso para separar confrontas por convenio");
					}
				} else {

					if (dateConfrontas > 1) {
						log.debug(methodName + " - Llama al proceso para separar las fechas de la confronta");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								" Archivo: " + nombreArchivo + "_Inicia el proceso para separar confrontas por fecha.",
								"");

						if (cbdb.ejecutaProcesoSeparacionFechasConfronta(
								listDataBanco.get(0).getIdCargaMaestro()) > 0) {
							log.debug(methodName + " - Separacion de fechas de confrontas realizada exitosamente!");
							cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(), " Archivo: " + nombreArchivo
									+ "_Separacion de fechas de confrontas realizada exitosamente", "");

						} else {
							log.debug(methodName + "- Ocurrio un error al ejecutar la separacion de fechas");
						}
					} else {
						log.debug(methodName + " - Llama al proceso para conciliacion");
						cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
								" Archivo: " + nombreArchivo + "_Inicia el proceso de conciliacion.", "");

						if (cbdb.ejecutaProcesoConciliacion(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
							log.debug(methodName + " - Finaliza el proceso de conciliacion");
							cbdb.insertLog(listDataBanco.get(0).getIdCargaMaestro(),
									" Archivo: " + nombreArchivo + "_Finaliza el proceso de conciliacion.", "");
							// sp comisiones
							log.debug(methodName + " - inicia el proceso de comisiones confrontas");
							if (cbdb.ejecutaProcesoComisionesConfrontas(listDataBanco.get(0).getIdCargaMaestro()) > 0) {
								log.debug(methodName + " - Finaliza el proceso de comisiones confrontas");
							}
						} else {
							log.debug(methodName + " - No se pudo ejecutar el proceso de conciliacion");

						}
					}
				}
			} catch (Exception e) {
				log.error(methodName + " - Error :", e);
			}
			Messagebox.show("Se a cargado la entidad bancaria " + misession.getAttribute("entidad").toString()
					+ ".\n\nDetalle de carga:\n " + countRec + " registos grabados\n" + dataSinProcc
					+ " registos sin procesar ", "ATENCIÓN", Messagebox.OK, Messagebox.INFORMATION);
			CBProcessFileUploadController cvai = new CBProcessFileUploadController();
			cvai.cerrarDetalleCarga(false, listDataBanco.get(0).getIdCargaMaestro());
		}

	}
	/*
	 * private CBArchivosInsertadosDAO cvaidao = new CBArchivosInsertadosDAO();
	 * 
	 * public void cerrarDetalleCarga(boolean estVentana, String idMaestroC) {
	 * 
	 * // carga variables if (estVentana) {
	 * System.out.println("borra fila Carga Maestro");
	 * cvaidao.borraFilaGrabadaMaestro(idMaestroC); }
	 * 
	 * }
	 */

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		CBProcessFileUploadUtils.nombreArchivo = nombreArchivo;
	}

	/**
	 * @return the media
	 */
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public List<CBDataBancoModel> getListDataBanco() {
		return listDataBanco;
	}

}
