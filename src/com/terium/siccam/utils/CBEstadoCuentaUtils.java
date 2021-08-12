package com.terium.siccam.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.CBEstadoCuentasController;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.model.CBEstadoCuentasModel;

public class CBEstadoCuentaUtils extends ControladorBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nombreArchivoCredomatic;
	String user;
	int idArchivo;
	int idBAC;
	boolean isReloadFile;
	Media media;
	List<CBEstadoCuentasModel> listCredomatic;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	// fpu.cerrarDetalleCarga(true, idMaestro);
	// carga estado cuenta credomatic sv
	@SuppressWarnings("unchecked")
	public void leerArchivoCredo(String format) {
		CBEstadoCuentasController fpu = new CBEstadoCuentasController();
		user = (String) misession.getAttribute("user");
		nombreArchivoCredomatic = (String) misession.getAttribute("nombreArchivoCredomatic");
		idArchivo = (Integer) misession.getAttribute("idArchivo");
		media = (Media) misession.getAttribute("media");
		idBAC = (Integer) misession.getAttribute("idBAC");
		isReloadFile = (Boolean) misession.getAttribute("isReloadFile");
		listCredomatic = (List<CBEstadoCuentasModel>) misession.getAttribute("listCredomatic");

		CBEstadoCuentasModel objCredo = null;

		Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
				"id idBAC: " + idBAC + " formato: " + media.getFormat());

		if ("xlsx".equals(format)) {
			// int fila = 0;
			String registro = "";
			boolean isEnd = false;
			try {
				XSSFWorkbook libro = new XSSFWorkbook(OPCPackage.open(media.getStreamData()));

				XSSFSheet hoja = libro.getSheetAt(0);

				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					// validamos si el sector del archivo pertenece a detalle

					if (row.getRowNum() > 4) {

						objCredo = new CBEstadoCuentasModel();
						registro = row.getCell(0).getStringCellValue();
						objCredo.setCbestadocuentaconfid(idBAC);
						objCredo.setCbestadocuentaarchivosid(idArchivo);
						// System.out.println(" registro: "+registro);
						if (registro.trim().toUpperCase().equals("Resumen de Estado Bancario".toUpperCase())) {
							isEnd = true;
							// System.out.println("cambia estado a true");
						}
						StringTokenizer str = new StringTokenizer(registro, ",");
						int contaCredi = 0;
						while (str.hasMoreTokens() && !isEnd) {
							contaCredi++;
							String value = str.nextToken();
							// System.out.println("conta: "+contaCredi+" value:
							// "+value);

							switch (contaCredi) {
							case 1:
								objCredo.setFechatransaccion(value);
								break;
							case 2:
								objCredo.setReferencia(value);
								break;
							case 3:
								objCredo.setCodigo_lote(value);
								break;
							case 4:
								objCredo.setDescpago(value);
								break;
							case 5:
								objCredo.setDebito(value);
								break;
							case 6:
								objCredo.setCredito(value);
								break;
							case 7:
								objCredo.setBalance(value);
								break;
							}
							objCredo.setUsuario(user);

						}
						if (!isEnd && isDate(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}

					}

				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredo(listCredomatic)) {
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					System.out.println("inserta credomatic ==>: " + objDao.ejecutaSPCredomatic());
				} else {
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isEnd = false;
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("El archivo cargado contiene errores o esta danado, favor validarlo", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);

			}
		} else if ("csv".equals(format)) {
			try {
				// Reader read = media.getReaderData();
				InputStreamReader ir = new InputStreamReader(media.getStreamData());
				BufferedReader read = new BufferedReader(ir);
				String line;
				int fila = 0;
				int celda = 0;
				String registro = null;
				while ((line = read.readLine()) != null) {
					fila++;
					objCredo = new CBEstadoCuentasModel();
					StringTokenizer token = new StringTokenizer(line, ",");
					objCredo.setCbestadocuentaconfid(idBAC);
					objCredo.setCbestadocuentaarchivosid(idArchivo);
					// System.out.println("fila: "+fila+" - numero de celda
					// detalle: "+celda+" - registro: "+registro);
					if (fila > 5) {
						while (token.hasMoreTokens()) {
							registro = token.nextToken();
							// System.out.println("registro: "+registro);
							celda++;
							switch (celda) {
							case 1:
								objCredo.setFechatransaccion(registro);
								break;
							case 2:
								objCredo.setReferencia(registro);
								break;
							case 3:
								objCredo.setCodigo_lote(registro);
								break;
							case 4:
								objCredo.setDescpago(registro);
								break;
							case 5:
								objCredo.setDebito(registro);
								break;
							case 6:
								objCredo.setCredito(registro);
								break;
							case 7:
								objCredo.setBalance(registro);
								break;
							}
							objCredo.setUsuario(user);
						}
						celda = 0;
						objCredo.setUsuario(user);
						if (isDate(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}
					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredo(listCredomatic)) {
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
							"inserta credomatic ==>: " + objDao.ejecutaSPCredomatic());
				} else {
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isReloadFile = false;
				fila = 0;
				read.close();
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if ("xls".equals(format)) {
			// int fila = 0;
			String registro = "";
			boolean isEnd = false;
			try {
				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {

					HSSFRow row = (HSSFRow) rows.next();
					// if (fila != 0) {
					if (row.getRowNum() > 4) {

						objCredo = new CBEstadoCuentasModel();
						registro = row.getCell(0).getStringCellValue();
						objCredo.setCbestadocuentaconfid(idBAC);
						objCredo.setCbestadocuentaarchivosid(idArchivo);
						// System.out.println(" registro: "+registro);
						if (registro.trim().toUpperCase().equals("Resumen de Estado Bancario".toUpperCase())) {
							isEnd = true;
							// System.out.println("cambia estado a true");
						}
						StringTokenizer str = new StringTokenizer(registro, ",");
						int contaCredi = 0;
						while (str.hasMoreTokens() && !isEnd) {
							contaCredi++;
							String value = str.nextToken();
							// System.out.println("conta: "+contaCredi+" value:
							// "+value);

							switch (contaCredi) {
							case 1:
								objCredo.setFechatransaccion(value);
								break;
							case 2:
								objCredo.setReferencia(value);
								break;
							case 3:
								objCredo.setCodigo_lote(value);
								break;
							case 4:
								objCredo.setDescpago(value);
								break;
							case 5:
								objCredo.setDebito(value);
								break;
							case 6:
								objCredo.setCredito(value);
								break;
							case 7:
								objCredo.setBalance(value);
								break;
							}
							objCredo.setUsuario(user);

						}
						if (!isEnd && isDate(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}
					}
				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredo(listCredomatic)) {
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
							"inserta credomatic ==>: " + objDao.ejecutaSPCredomatic());
				} else {
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isEnd = false;
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} else {
			Messagebox.show("No se ha cargado un archivo excel... ", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
	}

	/**
	 * Obtenemos los archivos de tipo Liquidacion (Encabezado)
	 * 
	 * @param format
	 */
	// modificado Ovidio Santos 21042017
	@SuppressWarnings({ "unchecked", "unchecked" })
	public void leerCredomaticEncabezado(String format) {
		CBEstadoCuentasController fpu = new CBEstadoCuentasController();

		user = (String) misession.getAttribute("user");

		System.out.println("datos recibidos de la sesion user utils " + user);
		nombreArchivoCredomatic = (String) misession.getAttribute("nombreArchivoCredomatic");
		idArchivo = (Integer) misession.getAttribute("idArchivo");
		media = (Media) misession.getAttribute("media");
		idBAC = (Integer) misession.getAttribute("idBAC");
		isReloadFile = (Boolean) misession.getAttribute("isReloadFile");
		listCredomatic = (List<CBEstadoCuentasModel>) misession.getAttribute("listCredomatic");
		CBEstadoCuentasModel objCredo = null;

		System.out.println("datos recibidos de la sesion nombreArchivoCredomatic " + nombreArchivoCredomatic);
		System.out.println("datos recibidos de la sesion idArchivo " + idArchivo);
		System.out.println("datos recibidos de la sesion media " + media);
		System.out.println("datos recibidos de la sesion isReloadFile " + isReloadFile);
		System.out.println("datos recibidos de la sesion listCredomatic " + listCredomatic.size());
		Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
				"id idBAC: " + idBAC + " formato: " + media.getFormat());
		if ("xls".equals(media.getFormat())) {
			String registro = "";
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			DataFormatter formatter = new DataFormatter();

			try {
				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();

				// while ((line = read.readLine()) != null)
				while (rows.hasNext()) {

					HSSFRow row = (HSSFRow) rows.next();
					if (row.getRowNum() > 0) {

						objCredo = new CBEstadoCuentasModel();
						for (int celda = 0; celda <= 14; celda++) {

							if (row.getCell(celda) == null) {
								// System.out.println("Celda null");
								registro = "";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}

								objCredo.setCbestadocuentaconfid(idBAC);
								objCredo.setCbestadocuentaarchivosid(idArchivo);
								// System.out.println("numero de celda detalle:
								// " + celda + " registro: " + registro);
								switch (celda) {

								// Mapeo apegando el contenido a los archivos
								// Credomatic
								case 0:
									// para el campo CODIGO
									objCredo.setAfilicacion(registro);
									// System.out.println("afiliado insertado "
									// + (registro.trim()));
									break;

								case 1:
									// para el campo AFILIADO
									objCredo.setDescripcion(registro.trim());
									// System.out.println("codigo insertado " +
									// (registro.trim()));
									break;

								case 2:
									// para el campo monto
									objCredo.setConsumo(registro.trim().replace("$", ""));
									// System.out.println("monto insertado " +
									// (registro.trim()));

									break;
								case 3:
									// para el campo comision
									objCredo.setComision(registro.trim().replace("$", ""));
									// System.out.println("comision insertado "
									// + (registro.trim()));
									break;

								case 4:
									// para el campo iva
									objCredo.setIvacomision(registro.trim().replace("$", ""));
									// System.out.println("iva insertado " +
									// (registro.trim()));

									break;
								case 5:
									// para el campo retencion
									objCredo.setRetencion(registro.trim().replace("$", ""));
									break;
								case 6:
									// para el campo neto_p
									objCredo.setLiquido(registro.trim().replace("$", ""));
									break;

								case 7:
									// para el campo fecha
									objCredo.setFechatransaccion(registro.trim());
									// System.out.println("fecha para seteo " +
									// registro.trim());
									break;
								case 8:
									// para el campo ppl
									objCredo.setCodigo_lote(registro.trim());
									break;
								case 9:
									// para el campo no_dcl
									objCredo.setDebito(registro.trim());
									break;
								case 10:
									// para el campo no_ccf
									objCredo.setBalance(registro.trim());
									break;

								case 11:
									// para el campo liq
									objCredo.setReferencia(registro.trim());
									// System.out.println("liquido insertado
									// excel " + (registro.trim()));
									break;

								}
								objCredo.setUsuario(user);
								objCredo.setTipo("CREDOMATIC");
								// System.out.println("fecha liq " + objCredo.getFechatransaccion());
							}

							// celda = 0;
							// objCredo.setUsuario(user);
							// if (isDateCredo(objCredo.getFechatransaccion()))
							// {

						}
						if (isDateCredoEncabezado(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}

					}

				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredoEncabezado(listCredomatic)) {

					// if (objDao.insertCuentasCredoEncabezado(listCredomatic,
					// listCredomatic.size() - 8)) {
					System.out.println("datos prueba valida en if " + media.getName());
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					// System.out.println("inserta credomatic ==>: " +
					// objDao.ejecutaSPCredomatic());
					boolean ex = objDao.ejecutaSPCredomatic();
					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
							"inserta credomatic encabezado ==> " + ex);
				} else {
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
				fpu.cambiaEstadoMensaje(true, media.getName());
				isReloadFile = false;
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);

			} catch (InvalidFormatException e) {

				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			}

		} else {
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
					"el formato no es valido: " + format);
		}
	}

	/**
	 * Obtenemos los archivos de tipo Liquidacion (Encabezado)
	 * 
	 * @param format
	 */
	// modificado Ovidio Santos 24/04/2017
	@SuppressWarnings("unchecked")
	public void leerCredomaticDetalle(String format) {
		CBEstadoCuentasModel objCredo = null;
		CBEstadoCuentasController fpu = new CBEstadoCuentasController();

		user = (String) misession.getAttribute("user");
		nombreArchivoCredomatic = (String) misession.getAttribute("nombreArchivoCredomatic");
		idArchivo = (Integer) misession.getAttribute("idArchivo");
		media = (Media) misession.getAttribute("media");
		idBAC = (Integer) misession.getAttribute("idBAC");
		isReloadFile = (Boolean) misession.getAttribute("isReloadFile");
		listCredomatic = (List<CBEstadoCuentasModel>) misession.getAttribute("listCredomatic");

		Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
				"id idBAC: " + idBAC + " formato: " + media.getFormat());
		if ("xls".equals(media.getFormat())) {
			String registro = "";
			DateFormat df = new SimpleDateFormat("dd/MM/yy");
			DataFormatter formatter = new DataFormatter();

			try {
				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {

					HSSFRow row = (HSSFRow) rows.next();
					if (row.getRowNum() > 0) {

						objCredo = new CBEstadoCuentasModel();
						for (int celda = 0; celda <= 14; celda++) {

							if (row.getCell(celda) == null) {
								// System.out.println("Celda null");
								registro = "";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));

									break;
								}

								objCredo.setCbestadocuentaconfid(idBAC);
								objCredo.setCbestadocuentaarchivosid(idArchivo);
								switch (celda) {

								// Mapeo apegando el contenido a los archivos
								// VISA
								// detalle

								case 0:
									// para el campo TARJETA3
									objCredo.setTarjeta(registro.trim());
									break;
								case 1:
									// para el campo DE350TER
									objCredo.setTerminal(registro.trim());
									break;
								case 2:
									// para el campo DE350AFL
									objCredo.setLote(registro.trim());
									break;
								case 3:
									// para el campo monto transaccion
									objCredo.setConsumo(registro.trim().replace("$", ""));
									break;
								case 4:
									// para el campo PROPINA
									objCredo.setPropina(registro.trim());
									break;
								case 5:
									// para el campo comision
									objCredo.setComision(registro.trim().replace("$", ""));
									break;

								case 6:
									// para el campo neto
									objCredo.setLiquido(registro.trim().replace("$", ""));
									break;
								case 7:
									// para el campo CODIGO_AUTORIZACION
									objCredo.setAutorizacion(registro.trim());
									break;

								case 8:
									// para el campo FECHA_TRANSACCION
									objCredo.setFechaventa(registro.trim());
									// System.out.println("la fecha en el controlador es " + registro.trim());
									break;
								case 9:
									// para el campo DE350FRE
									objCredo.setFechacierre(registro.trim());
									break;

								case 10:
									// para el campo MON007
									objCredo.setDescripcion(registro.trim());
									break;

								case 11:
									// para el campo liquido
									objCredo.setAfilicacion(registro.trim());
									// System.out.println("codigo insertado " +
									// (registro.trim()));
									break;

								}
								objCredo.setTipotrans("CREDOMATIC");
								objCredo.setUsuario(user);
							}

						}
						if (isDateCredo(objCredo.getFechaventa())) {
							listCredomatic.add(objCredo);
						}

					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredoDet(listCredomatic)) {

					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					boolean ex = objDao.ejecutaSPCredomatic();
					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
							"inserta credomatic detalle ==>: " + ex);
				} else {

					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isReloadFile = false;

				fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
				fpu.cambiaEstadoMensaje(true, media.getName());

			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);

			} catch (InvalidFormatException e) {

				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			}

		} else {
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
					"el formato no es valido: " + format);
		}
	}
	
	/**
	 * Credomatic
	 * Validamos si el string enviado es fecha
	 * */
	public boolean isDateCredoGT(String fecha){
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date fec;
		try {
			fec = format.parse(fecha);
			//System.out.println("fecha parseada: "+fec);
			return true;
		} catch (ParseException e) {
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			//System.out.println("fecha con error: "+fecha);
			return false;
		} catch (NullPointerException e){
			//System.out.println(e.getMessage());
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
	}
	

	/**
	 * Credomatic Validamos si el string enviado es fecha
	 */
	public boolean isDateCredo(String fecha) {
		DateFormat format = new SimpleDateFormat("mm/DD/yy");
		@SuppressWarnings("unused")
		Date fec;
		try {
			fec = format.parse(fecha);
			// System.out.println("fecha parseada: " + fec);
			return true;
		} catch (ParseException e) {
			// System.out.println(e.getMessage());
			System.out.println("fecha con error: " + fecha);
			return false;
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			System.out.println("fecha null: " + fecha);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void leerCredomaticEncabezadoCR(String format) {
		CBEstadoCuentasController fpu = new CBEstadoCuentasController();
		CBEstadoCuentasModel objCredo = null;

		user = (String) misession.getAttribute("user");
		nombreArchivoCredomatic = (String) misession.getAttribute("nombreArchivoCredomatic");
		idArchivo = (Integer) misession.getAttribute("idArchivo");
		media = (Media) misession.getAttribute("media");
		idBAC = (Integer) misession.getAttribute("idBAC");
		isReloadFile = (Boolean) misession.getAttribute("isReloadFile");
		listCredomatic = (List<CBEstadoCuentasModel>) misession.getAttribute("listCredomatic");

		Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
				"id idBAC: " + idBAC + " formato: " + media.getFormat());

		System.out.println("id idBAC: " + idBAC + " formato: " + media.getFormat());
		if ("xlsx".equals(format)) {
			// obtiene el id de configuracion
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			DataFormatter formatter = new DataFormatter();

			String registro = "";
			try {
				XSSFWorkbook libro = new XSSFWorkbook(OPCPackage.open(media.getStreamData()));
				XSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					// validamos si el sector del archivo pertenece a detalle
					if (row.getRowNum() > 0) {
						objCredo = new CBEstadoCuentasModel();

						for (int celda = 0; celda <= 24; celda++) {

							if (row.getCell(celda) == null) {
								registro = "  ";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}

								objCredo.setCbestadocuentaconfid(idBAC);
								objCredo.setCbestadocuentaarchivosid(idArchivo);

								switch (celda) {
								// Mapeo apegando el contenido a los archivos
								// Credomatic
								case 0:
									// para el campo Cod afiliacion
									objCredo.setAfilicacion(registro.trim());
									break;

								case 1:
									// para el campo ID_TERMINAL
									objCredo.setDescripcion(registro.trim());
									break;
								case 2:
									// para el campo DE350FRE
									objCredo.setIva(registro.trim());
									break;
								case 3:
									// para el campo DE350SUC
									objCredo.setCodigo_lote(registro.trim());
									break;
								case 4:
									// para el campo DE350LDE
									objCredo.setCredito(registro.trim());
									break;
								case 5:
									// para el campo NUMERO_TARJETA
									objCredo.setDocumento(registro.trim());
									break;

								case 6:
									// para el campo MONTO_TRANSACCION
									objCredo.setConsumo(registro.trim());
									break;
								case 7:
									// para el campo FECHA_TRANSACCION
									objCredo.setFechatransaccion(registro.trim());
									break;
								case 8:
									// para el campo HORA_TRANSACCION
									objCredo.setImpturismo(registro.trim());
									break;
								case 9:
									// para el campo CODIGO_AUTORIZACION
									objCredo.setPropina(registro.trim());
									break;
								case 10:
									// para el campo DE350CVE //campo no se toma en cuenta
									objCredo.setCtacp(registro.trim());
									break;
								case 11:
									// para el campo LIQUIDACION
									objCredo.setReferencia(registro.trim());
									break;
								case 12:
									// para el campo VENTAS
									objCredo.setDebito(registro.trim());
									break;
								case 13:
									// para el campo comision
									objCredo.setComision(registro.trim());
									break;
								case 14:
									// para el campo IMP VENTA
									objCredo.setIvacomision(registro.trim());
									break;
								case 15:
									// para el campo IMP RENTA
									objCredo.setRetencion(registro.trim());
									break;
								case 16:
									// para el campo AJUSTES
									objCredo.setBalance(registro.trim());
									break;
								case 17:
									// para el campo NETO PAGO
									objCredo.setLiquido(registro.trim());
									break;
								}
								objCredo.setUsuario(user);
								objCredo.setTipo("CREDOMATIC");
							}
						}
						if (isDateCredoEncabezado(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}

					}

				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredoEncabezadoCR(listCredomatic)) {
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					boolean ex = objDao.ejecutaSPCredomatic();
					System.out.println("inserta credomatic encabezado ==> " + ex);
				} else {
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();

				fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
				fpu.cambiaEstadoMensaje(true, media.getName());
				isReloadFile = false;

			} catch (IOException e) {
				System.out.println("Error invalidformatException: " + e.getMessage());
			} catch (InvalidFormatException e) {
				System.err.println(e.getMessage());
			}

		} else if ("xls".equals(format)) {
			String registro = "";
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			DataFormatter formatter = new DataFormatter();
			try {
				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					HSSFRow row = (HSSFRow) rows.next();
					if (row.getRowNum() > 0) {
						objCredo = new CBEstadoCuentasModel();
						for (int celda = 0; celda <= 24; celda++) {
							if (row.getCell(celda) == null) {
								registro = "";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}
								objCredo.setCbestadocuentaconfid(idBAC);
								objCredo.setCbestadocuentaarchivosid(idArchivo);
								switch (celda) {
								// Mapeo apegando el contenido a los archivos
								// Credomatic
								case 0:
									// para el campo Cod afiliacion
									objCredo.setAfilicacion(registro.trim());
									break;
								case 1:
									// para el campo ID_TERMINAL
									objCredo.setDescripcion(registro.trim());
									break;
								case 2:
									// para el campo DE350FRE
									objCredo.setIva(registro.trim());
									break;
								case 3:
									// para el campo DE350SUC
									objCredo.setCodigo_lote(registro.trim());
									break;
								case 4:
									// para el campo DE350LDE
									objCredo.setCredito(registro.trim());
									break;
								case 5:
									// para el campo NUMERO_TARJETA
									objCredo.setDocumento(registro.trim());
									break;

								case 6:
									// para el campo MONTO_TRANSACCION
									objCredo.setConsumo(registro.trim());
									break;
								case 7:
									// para el campo FECHA_TRANSACCION
									objCredo.setFechatransaccion(registro.trim());
									break;
								case 8:
									// para el campo HORA_TRANSACCION
									objCredo.setImpturismo(registro.trim());
									break;
								case 9:
									// para el campo CODIGO_AUTORIZACION
									objCredo.setPropina(registro.trim());
									break;
								case 10:
									// para el campo DE350CVE //campo no se toma en cuenta
									objCredo.setCtacp(registro.trim());
									break;
								case 11:
									// para el campo LIQUIDACION
									objCredo.setReferencia(registro.trim());
									break;
								case 12:
									// para el campo VENTAS
									objCredo.setDebito(registro.trim());
									break;
								case 13:
									// para el campo comision
									objCredo.setComision(registro.trim());
									break;
								case 14:
									// para el campo IMP VENTA
									objCredo.setIvacomision(registro.trim());
									break;
								case 15:
									// para el campo IMP RENTA
									objCredo.setRetencion(registro.trim());
									break;
								case 16:
									// para el campo AJUSTES
									objCredo.setBalance(registro.trim());
									break;
								case 17:
									// para el campo NETO PAGO
									objCredo.setLiquido(registro.trim());
									break;
								}
								objCredo.setUsuario(user);
								objCredo.setTipo("CREDOMATIC");
							}
						}
						if (isDateCredoEncabezado(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}
					}

				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredoEncabezado(listCredomatic)) {

					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					boolean ex = objDao.ejecutaSPCredomatic();
					System.out.println("inserta credomatic encabezado ==> " + ex);
				} else {

					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();

				fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
				fpu.cambiaEstadoMensaje(true, media.getName());
				isReloadFile = false;
			} catch (IOException e) {
				System.out.println("Error invalidformatException: " + e.getMessage());
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}

		} else if ("csv".equals(format)) {
			System.out.println("id idBAC: " + idBAC + " formato: " + media.getFormat());
			InputStreamReader ir = null;
			BufferedReader read = null;
			try {
				ir = new InputStreamReader(media.getStreamData());
				read = new BufferedReader(ir);
				String line;
				int fila = 0;
				int celda = 0;
				String registro = null;
				while ((line = read.readLine()) != null) {
					fila++;
					objCredo = new CBEstadoCuentasModel();
					System.out.println("linea:" + line);
					StringTokenizer token = new StringTokenizer(line, "|");
					System.out.println("linea despues:" + line);
					objCredo.setCbestadocuentaconfid(idBAC);
					objCredo.setCbestadocuentaarchivosid(idArchivo);
					if (fila > 1) {
					//	System.out.println("fila:" + fila);
						while (token.hasMoreTokens()) {
							registro = token.nextToken();
							celda++;
							switch (celda) {
							// Mapeo apegando el contenido a los archivos VISA
							case 1:
								// para el campo c
								objCredo.setAfilicacion(registro.replace("\"", "").trim());
								break;
							case 2:
								// para el campo nombre
								objCredo.setDescripcion(registro.replace("\"", "").trim());
								break;
							case 3:
								// para el campo fecha
								objCredo.setFechatransaccion(registro.trim());
								break;
							case 4:
								// para el campo fechapago
								objCredo.setFechavalor(registro.replace("\"", "").trim());
								break;
							case 5:
								// para el campo entrada
								objCredo.setCodigo_lote(registro.replace("\"", "").trim()); //setDebito
								break;
							case 6:
								// para el campo cheque
								objCredo.setDocumento(registro.replace("\"", "").trim()); //setBalance
								break;
							case 7:
								// para el campo cantdoc
								objCredo.setImpuestoturis(registro.replace("\"", "").trim()); //setIva
								break;
							case 8:
								// para el campo lote
								System.out.println("campo consumo antes:" + registro);
								objCredo.setConsumo((registro.replace(",", ".")).replace("\"", "").trim());//setCodigo_lote
							//	System.out.println("campo consumo:" +objCredo.getConsumo());
								break;
							case 9:
								// para el campo venta
								System.out.println("campo setComision antes:" + registro);
								objCredo.setComision((registro.replace(",", ".")).replace("\"", "").trim());
								break;
							case 10:
								// para el campo comision
								objCredo.setIvacomision((registro.replace(",", ".")).replace("\"", "").trim());
								break;
							case 11:
								// para el campo ivacomision
								objCredo.setRetencion((registro.replace(",", ".")).replace("\"", "").trim());
								break;
							case 12:
								// para el campo impprop
								objCredo.setLiquido((registro.replace(",", ".")).replace("\"", "").trim());
								break;
							case 13:
								// para el campo ajuste
								objCredo.setReferencia(registro.replace("\"", "").trim());
								break;
							case 14:
								// para el campo retiva
								objCredo.setDebito(registro.replace("\"", "").trim());
								break;
							case 15:
								// para el campo pago
							//	System.out.println("registro:" + registro);
								objCredo.setPropina(registro.replace("\"", "").trim());
								break;
								/*
							case 16:
								// para el campo docrev
								objCredo.setDocumento(registro);
								break;
							case 17:
								// para el campo montorev
								objCredo.setCredito(registro);
								break;
							case 18:
								// para el campo liquidacion
								objCredo.setReferencia(registro);
								break;
								*/
							}
							objCredo.setUsuario(user);
							objCredo.setTipo("CREDOMATIC");
						}
						celda = 0;
						objCredo.setUsuario(user);
						if (isDateCredoGT(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}
					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredoEncabezadoCR(listCredomatic)) {
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					boolean ex = objDao.ejecutaSPCredomatic();
					System.out.println("inserta credomatic encabezado ==>: " + ex);
				} else {
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isReloadFile = false;
				fila = 0;
				read.close();
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				try {
					if (ir != null)
						ir.close();
					if (read != null)
						read.close();
				} catch (Exception e) {
					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		} 
		
		else {
			Messagebox.show("No se ha cargado un archivo excel... ", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
	}

	/**
	 * Obtenemos los archivos de tipo Liquidacion (Encabezado)
	 * 
	 * @param format
	 */
	@SuppressWarnings("unchecked")
	public void leerCredomaticEncabezadoGT(String format) {
		CBEstadoCuentasController fpu = new CBEstadoCuentasController();
		CBEstadoCuentasModel objCredo = null;

		user = (String) misession.getAttribute("user");
		nombreArchivoCredomatic = (String) misession.getAttribute("nombreArchivoCredomatic");
		idArchivo = (Integer) misession.getAttribute("idArchivo");
		media = (Media) misession.getAttribute("media");
		idBAC = (Integer) misession.getAttribute("idBAC");
		isReloadFile = (Boolean) misession.getAttribute("isReloadFile");
		listCredomatic = (List<CBEstadoCuentasModel>) misession.getAttribute("listCredomatic");

		Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
				"id idBAC: " + idBAC + " formato: " + media.getFormat());

		if ("csv".equals(format)) {
			System.out.println("id idBAC: " + idBAC + " formato: " + media.getFormat());
			InputStreamReader ir = null;
			BufferedReader read = null;
			try {
				ir = new InputStreamReader(media.getStreamData());
				read = new BufferedReader(ir);
				String line;
				int fila = 0;
				int celda = 0;
				String registro = null;
				while ((line = read.readLine()) != null) {
					fila++;
					objCredo = new CBEstadoCuentasModel();
					StringTokenizer token = new StringTokenizer(line, ",");
					objCredo.setCbestadocuentaconfid(idBAC);
					objCredo.setCbestadocuentaarchivosid(idArchivo);
					if (fila > 1) {
						while (token.hasMoreTokens()) {
							registro = token.nextToken();
							celda++;
							switch (celda) {
							// Mapeo apegando el contenido a los archivos VISA
							case 1:
								// para el campo codigo
								objCredo.setAfilicacion(registro);
								break;
							case 2:
								// para el campo nombre
								objCredo.setDescpago(registro);
								break;
							case 3:
								// para el campo direccion
								objCredo.setTipo(registro);
								break;
							case 4:
								// para el campo fechapago
								objCredo.setDebito(registro);
								break;
							case 5:
								// para el campo entrada
								objCredo.setFechatransaccion(registro);
								break;
							case 6:
								// para el campo cheque
								objCredo.setBalance(registro);
								break;
							case 7:
								// para el campo cantdoc
								objCredo.setIva(registro);
								break;
							case 8:
								// para el campo lote
								objCredo.setCodigo_lote(registro);
								;
								break;
							case 9:
								// para el campo venta
								objCredo.setConsumo(registro);
								break;
							case 10:
								// para el campo comision
								objCredo.setComision(registro);
								break;
							case 11:
								// para el campo ivacomision
								objCredo.setIvacomision(registro);
								break;
							case 12:
								// para el campo impprop
								objCredo.setImpturismo(registro);
								break;
							case 13:
								// para el campo ajuste
								objCredo.setPropina(registro);
								break;
							case 14:
								// para el campo retiva
								objCredo.setRetencion(registro);
								break;
							case 15:
								// para el campo pago
								objCredo.setLiquido(registro);
								break;
							case 16:
								// para el campo docrev
								objCredo.setDocumento(registro);
								break;
							case 17:
								// para el campo montorev
								objCredo.setCredito(registro);
								break;
							case 18:
								// para el campo liquidacion
								objCredo.setReferencia(registro);
								break;
							}
							objCredo.setUsuario(user);
						}
						celda = 0;
						objCredo.setUsuario(user);
						if (isDateCredoGT(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}
					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredoEncabezadoGT(listCredomatic)) {
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					boolean ex = objDao.ejecutaSPCredomatic();
					System.out.println("inserta credomatic encabezado ==>: " + ex);
				} else {
					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isReloadFile = false;
				fila = 0;
				read.close();
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				try {
					if (ir != null)
						ir.close();
					if (read != null)
						read.close();
				} catch (Exception e) {
					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		} else {
			System.out.println("el formato no es valido: " + format);
		}
	}

	/**
	 * Obtenemos los archivos de tipo Liquidacion (Encabezado)
	 * 
	 * @param format
	 */
	@SuppressWarnings("unchecked")
	public void leerCredomaticDetalleGT(String format) {
		CBEstadoCuentasModel objCredo = null;
		CBEstadoCuentasController fpu = new CBEstadoCuentasController();

		user = (String) misession.getAttribute("user");
		nombreArchivoCredomatic = (String) misession.getAttribute("nombreArchivoCredomatic");
		idArchivo = (Integer) misession.getAttribute("idArchivo");
		media = (Media) misession.getAttribute("media");
		idBAC = (Integer) misession.getAttribute("idBAC");
		isReloadFile = (Boolean) misession.getAttribute("isReloadFile");
		listCredomatic = (List<CBEstadoCuentasModel>) misession.getAttribute("listCredomatic");

		Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
				"id idBAC: " + idBAC + " formato: " + media.getFormat());

		if ("csv".equals(format)) {
			System.out.println("id idBAC: " + idBAC + " formato: " + media.getFormat());
			InputStreamReader ir = null;
			BufferedReader read = null;
			try {
				ir = new InputStreamReader(media.getStreamData());
				read = new BufferedReader(ir);
				String line;
				int fila = 0;
				int celda = 0;
				String registro = null;
				while ((line = read.readLine()) != null) {
					fila++;
					objCredo = new CBEstadoCuentasModel();
					StringTokenizer token = new StringTokenizer(line, ",");
					objCredo.setCbestadocuentaconfid(idBAC);
					objCredo.setCbestadocuentaarchivosid(idArchivo);
					if (fila > 1) {
						while (token.hasMoreTokens()) {
							registro = token.nextToken();
							celda++;
							switch (celda) {
							// Mapeo apegando el contenido a los archivos VISA detalle
							case 1:
								// para el campo liquidacion
								objCredo.setAfilicacion(registro);
								break;
							case 2:
								// para el campo candoc
								objCredo.setTipotrans(registro);
								break;
							case 3:
								// para el campo venta
								objCredo.setLiquido(registro);
								break;
							case 4:
								// para el campo comision
								objCredo.setComision(registro);
								break;
							case 5:
								// para el campo pago
								objCredo.setConsumo(registro);
								break;
							case 6:
								// para el campo fecha
								objCredo.setFechacierre(registro);
								break;
							case 7:
								// para el campo codigo
								objCredo.setImpuestoturis(registro);
								break;
							case 8:
								// para el campo lote
								objCredo.setLote(registro);
								break;
							case 9:
								// para el campo tarjeta
								objCredo.setTarjeta(registro);
								break;
							case 10:
								// para el campo montotrans
								objCredo.setIva(registro);
								break;
							case 11:
								// para el campo fecha trans
								objCredo.setFechaventa(registro);
								break;
							case 12:
								// para el campo hora trans
								objCredo.setHora(registro);
								break;
							case 13:
								// para el campo autorizacion
								objCredo.setAutorizacion(registro);
								break;
							case 14:
								// para el campo terminal
								objCredo.setTerminal(registro);
								break;
							case 15:
								// para el campo nocierre
								objCredo.setPropina(registro);
								break;
							}
							objCredo.setUsuario(user);
						}
						celda = 0;
						objCredo.setUsuario(user);
						if (isDateCredoGT(objCredo.getFechacierre())) {
							listCredomatic.add(objCredo);
						}
					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentasCredoDetGT(listCredomatic)) {
					System.out.println("lista " + listCredomatic.size());

					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					boolean ex = objDao.ejecutaSPCredomatic();
					System.out.println("inserta credomatic detalle ==>: " + ex);
				} else {

					fpu = (CBEstadoCuentasController) misession.getAttribute("interfaceEstadoCuenta");
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isReloadFile = false;
				fila = 0;
				read.close();
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				try {
					if (ir != null)
						ir.close();
					if (read != null)
						read.close();
				} catch (Exception e) {
					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		} else {
			System.out.println("el formato es: " + format);
		}
	}

	/**
	 * Credomatic Validamos si el string enviado es fecha
	 */
	public boolean isDateCredoEncabezado(String fecha) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		@SuppressWarnings("unused")
		Date fec;
		try {
			fec = format.parse(fecha);
			// System.out.println("fecha parseada: " + fec);
			return true;
		} catch (ParseException e) {
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			// System.out.println("fecha con error: "+fecha);
			return false;
		} catch (NullPointerException e) {
			// System.out.println(e.getMessage());
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
	}

	/**
	 * Validamos si el string enviado es fecha
	 */
	public boolean isDate(String fecha) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unused")
		Date fec;
		try {
			fec = format.parse(fecha);
			// System.out.println("fecha: " + fec.getDate());
			return true;
		} catch (ParseException e) {
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		} catch (NullPointerException e) {
			Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
	}
	
	//creado por Gerbert
	public void leerArchivoReporteCierreCaja(String format) {
		CBEstadoCuentasController fpu = new CBEstadoCuentasController();
		user = (String) misession.getAttribute("user");
		nombreArchivoCredomatic = (String) misession.getAttribute("nombreArchivoCredomatic");
		idArchivo = (Integer) misession.getAttribute("idArchivo");
		media = (Media) misession.getAttribute("media");
		idBAC = (Integer) misession.getAttribute("idBAC");
		isReloadFile = (Boolean) misession.getAttribute("isReloadFile");
		listCredomatic = (List<CBEstadoCuentasModel>) misession.getAttribute("listCredomatic");

		CBEstadoCuentasModel objCredo = null;

		Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
				"id idBAC: " + idBAC + " formato: " + media.getFormat());

		if ("xlsx".equals(format)) {
			// int fila = 0;
			String registro = "";
			boolean isEnd = false;
			try {
				XSSFWorkbook libro = new XSSFWorkbook(OPCPackage.open(media.getStreamData()));

				XSSFSheet hoja = libro.getSheetAt(0);

				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					// validamos si el sector del archivo pertenece a detalle

					if (row.getRowNum() > 4) {

						objCredo = new CBEstadoCuentasModel();
						registro = row.getCell(0).getStringCellValue();
						objCredo.setCbestadocuentaconfid(idBAC);
						objCredo.setCbestadocuentaarchivosid(idArchivo);
						// System.out.println(" registro: "+registro);
						if (registro.trim().toUpperCase().equals("Resumen de Estado Bancario".toUpperCase())) {
							isEnd = true;
							// System.out.println("cambia estado a true");
						}
						StringTokenizer str = new StringTokenizer(registro, ",");
						int contaCredi = 0;
						while (str.hasMoreTokens() && !isEnd) {
							contaCredi++;
							String value = str.nextToken();
							// System.out.println("conta: "+contaCredi+" value:
							// "+value);

							switch (contaCredi) {
							case 1:
								objCredo.setFecha_solicitud(registro);
								
								break;
							
							/*case 2:
								objVisa.setCaso(registro);
								break;*/
							
							case 2:
								objCredo.setEstado(registro);
								System.out.println("el estado es: " +objCredo);
								break;
							
							case 3:
								objCredo.setCap(registro);
								break;
							
							case 4:
								objCredo.setDictamen_tersoreria(registro);
								break;
							
							case 5:
								objCredo.setSolicitante(registro);
								break;
							
							case 6:
								objCredo.setTotalgeneralcolones(registro);
								break;
							
							case 7:
								objCredo.setTotalgeneralvalores(registro);
								break;
							
							/*case 9:
								objVisa.setFila(registro);
								break;*/
							
							case 8:
								objCredo.setBoleta_deposito(registro);
								break;
							
							case 9:
								objCredo.setMoneda(registro);
								break;
							
							case 10:
								objCredo.setValor_tipo_cambio(registro);
								break;
					
							}
							objCredo.setUsuario(user);

						}
						if (!isEnd && isDate(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}

					}

				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertReporteCierreCaja(listCredomatic)) {
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					
				} else {
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isEnd = false;
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("El archivo cargado contiene errores o esta danado, favor validarlo", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);

			}
		} else if ("csv".equals(format)) {
			try {
				// Reader read = media.getReaderData();
				InputStreamReader ir = new InputStreamReader(media.getStreamData());
				BufferedReader read = new BufferedReader(ir);
				String line;
				int fila = 0;
				int celda = 0;
				String registro = null;
				while ((line = read.readLine()) != null) {
					fila++;
					objCredo = new CBEstadoCuentasModel();
					StringTokenizer token = new StringTokenizer(line, ",");
					objCredo.setCbestadocuentaconfid(idBAC);
					objCredo.setCbestadocuentaarchivosid(idArchivo);
					// System.out.println("fila: "+fila+" - numero de celda
					// detalle: "+celda+" - registro: "+registro);
					if (fila > 5) {
						while (token.hasMoreTokens()) {
							registro = token.nextToken();
							// System.out.println("registro: "+registro);
							celda++;
							switch (celda) {
							case 1:
								objCredo.setFecha_solicitud(registro);
								break;
							
							/*case 2:
								objVisa.setCaso(registro);
								break;*/
							
							case 2:
								objCredo.setEstado(registro);
								break;
							
							case 3:
								objCredo.setCap(registro);
								break;
							
							case 4:
								objCredo.setDictamen_tersoreria(registro);
								break;
							
							case 5:
								objCredo.setSolicitante(registro);
								break;
							
							case 6:
								objCredo.setTotalgeneralcolones(registro);
								break;
							
							case 7:
								objCredo.setTotalgeneralvalores(registro);
								break;
							
							/*case 9:
								objVisa.setFila(registro);
								break;*/
							
							case 8:
								objCredo.setBoleta_deposito(registro);
								break;
							
							case 9:
								objCredo.setMoneda(registro);
								break;
							
							case 10:
								objCredo.setValor_tipo_cambio(registro);
								break;
					
							}
							objCredo.setUsuario(user);
						}
						celda = 0;
						objCredo.setUsuario(user);
						if (isDate(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}
					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertReporteCierreCaja(listCredomatic)) {
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					
				} else {
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isReloadFile = false;
				fila = 0;
				read.close();
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			}
		} else if ("xls".equals(format)) {
			// int fila = 0;
			String registro = "";
			boolean isEnd = false;
			try {
				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {

					HSSFRow row = (HSSFRow) rows.next();
					// if (fila != 0) {
					if (row.getRowNum() > 4) {

						objCredo = new CBEstadoCuentasModel();
						registro = row.getCell(0).getStringCellValue();
						objCredo.setCbestadocuentaconfid(idBAC);
						objCredo.setCbestadocuentaarchivosid(idArchivo);
						// System.out.println(" registro: "+registro);
						if (registro.trim().toUpperCase().equals("Resumen de Estado Bancario".toUpperCase())) {
							isEnd = true;
							// System.out.println("cambia estado a true");
						}
						StringTokenizer str = new StringTokenizer(registro, ",");
						int contaCredi = 0;
						while (str.hasMoreTokens() && !isEnd) {
							contaCredi++;
							String value = str.nextToken();
							// System.out.println("conta: "+contaCredi+" value:
							// "+value);

							switch (contaCredi) {
							case 1:
								objCredo.setFecha_solicitud(registro);
								break;
							
							/*case 2:
								objVisa.setCaso(registro);
								break;*/
							
							case 2:
								objCredo.setEstado(registro);
								break;
							
							case 3:
								objCredo.setCap(registro);
								break;
							
							case 4:
								objCredo.setDictamen_tersoreria(registro);
								break;
							
							case 5:
								objCredo.setSolicitante(registro);
								break;
							
							case 6:
								objCredo.setTotalgeneralcolones(registro);
								break;
							
							case 7:
								objCredo.setTotalgeneralvalores(registro);
								break;
							
							/*case 9:
								objVisa.setFila(registro);
								break;*/
							
							case 8:
								objCredo.setBoleta_deposito(registro);
								break;
							
							case 9:
								objCredo.setMoneda(registro);
								break;
							
							case 10:
								objCredo.setValor_tipo_cambio(registro);
								break;
					
							}
							objCredo.setUsuario(user);

						}
						if (!isEnd && isDate(objCredo.getFechatransaccion())) {
							listCredomatic.add(objCredo);
						}
					}
				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertReporteCierreCaja(listCredomatic)) {
					fpu.cambiaEstadoMensaje(true, media.getName());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					
				} else {
					fpu.cambiaEstadoMensaje(false, media.getName());
					Messagebox.show("Ocurrio un error en la carga del archivo, favor intentarlo nuevamente...",
							"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				listCredomatic.clear();
				isEnd = false;
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IOException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} else {
			Messagebox.show("No se ha cargado un archivo excel... ", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		}
	}

}
