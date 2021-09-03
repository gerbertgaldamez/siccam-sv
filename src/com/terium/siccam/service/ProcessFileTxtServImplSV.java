/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;
import com.terium.siccam.dao.CBConfiguracionConfrontaDaoB;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.implement.ProcessFileTxtService;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.CustomDate;
import com.terium.siccam.utils.Orden;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 * 
 * @author rSianB to terium.com
 */
public class ProcessFileTxtServImplSV extends ControladorBase implements ProcessFileTxtService {

	private static Logger logger = Logger.getLogger(ProcessFileTxtServImplSV.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String delimitador1;

	// variables para archivos sin delimitadores

	private String posiciones;
	private String tamDeCadena;

	private String delimitador2;
	private int cantidadAgrupacion;
	private String nomenclatura;
	private String formatoFecha;
	private List<Orden> orden;
	private List<CBDataSinProcesarModel> listDataSinProcesar = new ListModelList<CBDataSinProcesarModel>();

	// Agregado por Carlos Godinez - 29/05/2017
	private int splitDataLenght = 0;

	List<CBDataBancoModel> dataBancoModels = new ListModelList<CBDataBancoModel>();

	public List<CBDataBancoModel> leerArchivo(BufferedReader bufferedReader, int idBanco, int idAgencia,
			int idConfronta, String user, String nombreArchivo, String tipo, BigDecimal comisionConfronta,
			int idAgeConfro) {
		String methodName = "leerArchivo()";
		logger.debug(methodName + " -  Inicia leer Archivo ");
		// String nombreBi = nombreArchivo.substring(0, 8).trim();
		Date fechaCreacion = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		String strLine = "";
		String strLine1 = "";
		String idMaestro;
		// String linea = "";
		CBArchivosInsertadosDAO cvaidao = new CBArchivosInsertadosDAO();
		idMaestro = cvaidao.idMaestroCarga();
		if (cvaidao.insertarArchivos(idMaestro, nombreArchivo, idBanco, idAgencia, user)) {
			logger.debug(methodName + " - INSERT DE ARCHIVO REALIZADO CON EXITO");

			CBDataBancoDAO objeDataBancoDAO = new CBDataBancoDAO();
			int contLinea = 0;
			int lineaEmpiezaLeer = objeDataBancoDAO.obtenerLineaLectura(idConfronta);
			String formatoFechaConfronta = objeDataBancoDAO.obtenerFormatoFechaConfronta(idConfronta); // CarlosGodinez
																										// -> 28/08/2017
			/**
			 * 
			 * */
			try {
				// conn = obtenerDtsPromo().getConnection();
				getConfrontaConf(idConfronta);
				// Lee el archivo linea por linea
				while ((strLine = bufferedReader.readLine()) != null) {
					if (contLinea >= lineaEmpiezaLeer) {
						// strLine1 = strLine.replaceAll(" ", "").trim();

						strLine1 = strLine.trim();
						logger.debug(methodName + " - strLine1 = " + strLine1);
						// ************************************************************************************
						strLine1 = strLine1.replaceAll("\"", ""); // Agrega Carlos Godinez -> 27/06/2017
						logger.debug(methodName + " - strLine1 = " + strLine1);
						if (strLine1.trim().length() != 0) {

							// *********************************************************************************
							String delimitador = this.delimitador1;
//							logger.debug(methodName + "\nLinea: " + strLine1);
//							logger.debug(methodName + " - Longitud: " + strLine1.length());
//							logger.debug(methodName + " - VALOR DE LA VARIABLE 'delimitador' = " + delimitador);
							if (delimitador.compareTo("n/a") == 0) {
								char aChar = strLine1.charAt(0);
								String cambio = "" + aChar;
								logger.debug(methodName + " - QUE TIENE EL CHAR: " + cambio);
								// int numero = Integer.parseInt(cambio);

								// ********************************************************************************
								// se quita temporalmente para validar que
								// agregue datos a la lista
								// && Character.isDigit(aChar) == true
								// los archivos sv siempre vienen con formato
								// D00001
								if (strLine1.length() > 8) {
									String[] pos = this.posiciones.split(",");
									String longit = this.tamDeCadena;
									String[] splitNomCl = this.nomenclatura.split(",");
									logger.debug(methodName + " - variable longit = " + longit);
									logger.debug(methodName + " - LONGITUD DE VARIABLE longit = " + strLine1.length()
											+ "\n");
									if (strLine1.length() == Integer.parseInt(longit)) {
										logger.debug(methodName + " - Longitud: " + longit);

										CBDataBancoModel bancoModel = new CBDataBancoModel();
										// CBDataBancoDAO cbd = new
										// CBDataBancoDAO();
										bancoModel.setcBCatalogoBancoId(Integer.toString(idBanco));
										bancoModel.setcBCatalogoAgenciaId(Integer.toString(idAgencia));
										bancoModel.setCBBancoAgenciaConfrontaId(Integer.toString(idAgeConfro));
										bancoModel.setCreadoPor(user);
										bancoModel.setTipo(tipo);
										bancoModel.setFechaCreacion(formato.format(fechaCreacion));
										bancoModel.setIdCargaMaestro(idMaestro);
										bancoModel.setFecha(getFormatoFecha());
										// se cambia getFormatoFecha() por
										// fechaCreacion temporal para pruebas
										// temporalmente esta columna se mando a
										// la validacion del campo fecha
										// bancoModel.setDia(getFormatoFecha());
										bancoModel.setFormatofecha(getFormatoFecha());
										bancoModel.setComision(comisionConfronta);

										// obtiene las posiciones

										// ******************************************

										for (int countRec = 0; countRec < splitNomCl.length; countRec++) {
											int contador = 0;
											String posicion1;
											String posicion2;
											String ambasPosiciones = pos[countRec];

											String ambasPosReplace = ambasPosiciones.replace(" ", ",");
											String[] splitAmbasPosString = ambasPosReplace.split(",");

											posicion1 = splitAmbasPosString[contador];

											contador += 1;

											posicion2 = splitAmbasPosString[contador];

											String nomenCla = splitNomCl[countRec];

											// Telefono
											if ("T".equals(nomenCla)) {

												String telefono = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2)).trim();

												try {
													int entero = Integer.parseInt(telefono.trim());
													String telefono1 = ("" + entero).trim();
													logger.debug(methodName + " - TAMANO DEL TELEFONO: "
															+ telefono1.length() + " Telefono: " + telefono);
													if (telefono1.length() == 8) {
														bancoModel.setTelefono(telefono1.trim());
													} else {
														logger.debug(methodName + " - Codigo Cliente: " + telefono1);
														bancoModel.setCodCliente(telefono1.trim());
													}
												} catch (Exception e) {
													// TODO: handle exception
													logger.debug(methodName
															+ " - Formato de telefono/codigo cliente erroneo.");
													bancoModel.setTelefono("");
													bancoModel.setCodCliente("");
												}

											}

											if ("C".equals(nomenCla)) {

												String telefono = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2)).trim();

												int entero = Integer.parseInt(telefono.trim());
												String telefono1 = ("" + entero).trim();
												logger.debug(methodName + " - TAMAÑO DEL TELEFONO: "
														+ telefono1.length() + " Telefono: " + telefono);
												if (telefono1.length() == 8) {
													// System.out.println("Telefono:
													// "
													// + telefono1);
													bancoModel.setTelefono(telefono1.trim());
												} else {
													logger.debug(methodName + " - Codigo Cliente: " + telefono1);
													bancoModel.setCodCliente(telefono1.trim());
												}
											}

											// Monto
											if ("M".equals(nomenCla)) {
												/**
												 * Editado ultima vez por Carlos Godinez - Qitcorp - 28/08/2017
												 */
												String strMonto = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												strMonto = strMonto.replace("$", "");
												strMonto = strMonto.trim();
												logger.debug(methodName + " - ** Monto antes de parsear: " + strMonto);
												if (strMonto.substring(0, 1).equals(".")
														|| strMonto.substring(0, 1).equals(",")) {
													strMonto = "0".concat(strMonto);
													logger.debug(methodName + " - Primer elemento: "
															+ strMonto.substring(0, 1) + " Nueva cadena: " + strMonto);
												}

												String cadena = currencyToBigDecimalFormat(strMonto);
												if (isBigDecimal(cadena)) {
													BigDecimal monto;
													if (cadena.indexOf(".") != -1) {
														logger.debug(methodName
																+ " - viene con punto decimal con delimitador");
														monto = new BigDecimal(cadena);
													} else {
														logger.debug(methodName
																+ " - viene sin punto decimal con delimitador");
														monto = new BigDecimal(cadena).divide(new BigDecimal(100));
													}
													logger.debug(methodName + " - Monto parseado: " + monto);
													bancoModel.setMonto(monto);
												} else {
													throw new Exception("Monto no valido");
												}
											}

											// Texto1
											if ("N".equals(nomenCla)) {

												String texto1 = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												logger.debug(methodName + " - Texto1: " + texto1);

												bancoModel.setTexto1(texto1.trim());

											}

											// Texto2
											if ("A".equals(nomenCla)) {

												String texto2 = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												logger.debug(methodName + " - Texto1: " + texto2);
												bancoModel.setTexto2(texto2.trim());

											}
											// Transaccion

											if ("O".equals(nomenCla)) {

												String transaccion = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												logger.debug(methodName + " - Transaccion: " + transaccion);

												bancoModel.setTransaccion(transaccion.trim());
											}
											if ("R".equals(nomenCla)) {
												String agencia = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												logger.debug(methodName + "Agencia: " + agencia);
												bancoModel.setCbAgenciaVirfisCodigo(agencia.trim());
											}

											if ("D".equals(nomenCla)) {
												String fecha = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												logger.debug(methodName + " - Fecha: " + fecha);
												bancoModel.setFecha(fecha);
												bancoModel.setDia(fecha);
											}

										}
										if (isDate(bancoModel.getFecha(), formatoFechaConfronta)
												&& ((bancoModel.getTelefono() != null
														&& !bancoModel.getTelefono().equals(""))
														|| (bancoModel.getCodCliente() != null
																&& !bancoModel.getCodCliente().equals("")))) {
											dataBancoModels.add(bancoModel);
										} else {
											enviarDataSinProcesar(strLine1, nombreArchivo, user, idMaestro,
													"Cantidad erronea de campos consulte la configuracion de la confronta");
										}

									} else {
										// System.out
										// .println("No tiene los suficientes
										// caracteres: "
										// + strLine1);
										enviarDataSinProcesar(strLine1, nombreArchivo, user, idMaestro,
												"Cantidad erronea de campos consulte la configuracion de la confronta");
									}

								} else if (Character.isDigit(aChar) == true) {
									logger.debug(methodName + " - valida si es diguito: " + aChar);
									String cambioFecha = strLine1.toString();
									CustomDate customDate = new CustomDate();
									setFormatoFecha(customDate.getFormatFecha(cambioFecha.trim(), this.formatoFecha));
									// bancoModel.setFecha(fechaArchivo);
									// bancoModel.setDia(fechaArchivo);
									// Fecha

									// System.out.println("FECHA DEL ARCHIVO: "
									// + fechaArchivo);
									// dataBancoModels.add(bancoModel);
								} else {
									logger.debug(methodName + "No agrega la info a la lista");
								}

							} else {
								boolean isDataColumn = false;
								strLine1 = strLine.trim();
								if (strLine1.length() != 0) {
									int cantAgrup = 0;
									logger.debug(methodName + " - Delimitador para lectura1: " + this.delimitador1);
									logger.debug(methodName + " - Numero de columnas configuradas: "
											+ this.cantidadAgrupacion);
									logger.debug(methodName + " - nomenclatura = " + nomenclatura);

									String[] splitData = null;
									String[] splitDataValidos = null;
									if (nomenclatura.contains(Constantes.COLUMN)) {
										logger.debug(methodName + " - nomenclatura config columna");
										splitData = getData(strLine1, this.delimitador1, 5);
										logger.debug(methodName + " Numero de columnas Archivo = " + splitData.length);
										cantAgrup = this.cantidadAgrupacion - 2;
										splitDataValidos = new String[this.cantidadAgrupacion - 2];
										isDataColumn = true;

									} else if (nomenclatura.contains(Constantes.NOM_FECHA_TIPO5)) {
										logger.debug(methodName + " - nomenclatura es fecha tipo5");
										splitData = getDataFechaTipo5(strLine1, this.delimitador1);
										logger.debug(methodName + " - Se ha realizado depuracion de la cadena");
										cantAgrup = this.cantidadAgrupacion;
										splitDataValidos = new String[cantAgrup];

									} else {
										splitData = strLine1.split("\\" + this.delimitador1);
										splitDataLenght = splitData.length;
										logger.debug(
												methodName + " - Numero de columnas del archivo: " + splitDataLenght);
										cantAgrup = this.cantidadAgrupacion;
										splitDataValidos = new String[cantAgrup];
									}

									// Examina que el numero de columnas de la linea que se esta leyendo sea
									// mayor o igual a la cantidad de agrupacion configurada
									if (splitData.length >= cantAgrup) {
										for (int fila = 0; fila < cantAgrup; fila++) {
											splitDataValidos[fila] = splitData[fila];
										}
										if (splitDataValidos.length == cantAgrup) {
											logger.debug(methodName + " - es valido la condicion splitDataValidos");
											// Si la linea es valida, se pasan los
											// parametros a getBancoModel()
											getBancoModel(strLine1, idBanco, idAgencia, idConfronta, user,
													nombreArchivo, idMaestro, tipo, comisionConfronta,
													getFormatoFecha(), idAgeConfro, formatoFechaConfronta, cantAgrup,
													splitData, isDataColumn);

										} else {
											logger.debug(methodName + " - NO Cumple condicion: splitDataValidos ");
											enviarDataSinProcesar(strLine1, nombreArchivo, user, idMaestro,
													"La cantidad de agrupacion de la linea leida es menor a la configurada");
										}
									} else {
										logger.debug(methodName + " -  NO Cumple condicion: splitData ");
										enviarDataSinProcesar(strLine1, nombreArchivo, user, idMaestro,
												"Cantidad erronea de campos consulte la configuracion de la confronta");
									}

								}
							}
						}
					}
					contLinea++;
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				Messagebox.show("El numero de columnas del archivo es menor a la cantidad de agrupacion configurada. "
						+ "\n\nNumero de columnas configuradas: " + this.cantidadAgrupacion
						+ "\nNumero de columnas del archivo: " + splitDataLenght
						+ "\n\nCambie la cantidad de agrupacion de la confronta seleccionada en la pantalla de configuracion de confrontas",
						"ERROR", Messagebox.OK, Messagebox.EXCLAMATION);
				logger.debug(methodName + " - Error * : ", ex);
			} catch (IOException ex) {
				logger.debug(methodName + " - Error **  : ", ex);
			} catch (Exception e) {
				Messagebox.show("Ha ocurrido un error al intentar procesar el archivo de confronta", "ATENCION",
						Messagebox.OK, Messagebox.ERROR);
				logger.error(methodName + " - " + "ha ocurrido un error: " + e.getMessage());
			}
		} else {
			// System.out.println("viendo si sale");
			enviarDataSinProcesar(strLine1, nombreArchivo, user, idMaestro,
					"Error al intentar cargar archivo de confronta");
		}
		return dataBancoModels;
	}

	private String[] getDataFechaTipo5(String cadena, String delimitador) {
		String methodName = "getDataFechaTipo5()";
		logger.debug(methodName + " - Inicia obtener Data para nomenclatura fecha tipo5");
		String[] firstData = cadena.split("\\" + delimitador);
		String[] lastData = new String[firstData.length];
		if (firstData.length > 0) {
			int posicion = 0;
			for (String s : firstData) {
				if (StringUtils.isNumeric(s)) {
					logger.debug(methodName + " - Depuracion data : " + s);
					if ((posicion == 1 || posicion == 2)) {
						s = s.replaceAll("^0+", "");
						if (s.isEmpty())
							continue;
					}
					if (posicion == firstData.length - 3) {
						if (Integer.parseInt(s.substring(s.length() - 1)) == 0)
							s = s.substring(0, s.length() - 1);

						logger.debug(methodName + " - Depuracion data fecha : " + s);
					}
				}
				lastData[posicion] = s;
				posicion = posicion + 1;
			}

			lastData = ArrayUtils.remove(lastData, lastData.length - 1);

		}

		return lastData;
	}

	private static boolean isDecimal(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private String[] getData(String cadena, String delimitador, int posicion) {
		logger.debug("getData() -  inicia obtener Data ");
		String[] getFistData = cadena.split("\\" + delimitador, posicion);
		String[] getLastData = null;
		String[] dataFinal = null;
		String dataLast = null;
		if (getFistData.length > 0) {
			dataLast = getEndData(getFistData[posicion - 1]);
			getLastData = dataLast.split(",");
			dataFinal = oderData(
					ArrayUtils.addAll(ArrayUtils.remove(getFistData, getFistData.length - 1), getLastData));
		}

		return dataFinal;
	}

	private String[] oderData(String[] allData) {
		String fecha = "";
		fecha = fecha.concat(allData[0]).concat(" ").concat(allData[1]).trim();
		String[] depurateData = ArrayUtils.removeAll(allData, 0, 1);
		depurateData = ArrayUtils.addFirst(depurateData, fecha);
		return depurateData;
	}

	private String getEndData(String data) {
		// TODO Auto-generated method stub
		logger.debug("getEndData() - inicia obtener endData");
		String datosEnd = "";
		int beginIndex = 0;
		beginIndex = searchDigit(data);
		int endIndex = 0;
		char[] cadena = data.toCharArray();
		String str1 = "";
		String str2 = "";
		String str3 = "";
		for (int i = 0; i < cadena.length; i++) {
			if (Character.isDigit(cadena[i])) {
				str2 += cadena[i];
				endIndex = endIndex + 1;
			}
		}
		str1 = data.substring(0, beginIndex - 1).trim();
		str3 = data.substring(beginIndex + endIndex).trim();
		datosEnd = str1.concat(",").concat(str2).concat(",").concat(str3);
		logger.debug("getEndData() - finaliza");
		return datosEnd;
	}

	private int searchDigit(String s) {
		// Function to check if is digit
		// is found or not
		for (int i = 0; i < s.length(); i++) {
			if (Character.isDigit(s.charAt(i)) == true) {
				// return position of digit
				return i + 1;
			}
		}
		// return 0 if digit not present
		return 0;
	}

	/**
	 * Editado ultima vez por Carlos Godinez -> 28/08/2017
	 * 
	 * Se agrega parametro de formato fecha de la confronta
	 * 
	 * @param cantAgrup
	 * @param data
	 * @param isDataColumn
	 */
	private void getBancoModel(String strLine, int idBanco, int idAgencia, int idConfronta, String user,
			String nombreArchivo, String idMaestro, String tipo, BigDecimal comisionConfronta, String formatFecha,
			int idAgeConfro, String formatoFechaConfronta, int cantAgrup, String[] data, boolean isDataColumn) {
		// boolean registroValido = false;
		String methodName = "getBancoModel()";
		Date fechaCreacion = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

//		CustomDate customDate = new CustomDate();
		String[] splitData = null;
		String[] splitNomCl = null;
		if (ArrayUtils.isNotEmpty(data) && isDataColumn) {
			splitData = data;
			splitNomCl = ArrayUtils.removeAll(this.nomenclatura.split(","), 0, 1);

		} else {
			if (this.nomenclatura.contains(Constantes.NOM_FECHA_TIPO5))
				splitData = data;
			else
				splitData = strLine.split("\\" + delimitador1);

			splitNomCl = this.nomenclatura.split(",");
		}

		// System.out.println("SPLITDATA: " + splitData.length);
		logger.debug(methodName + " - Nomenclatura: " + nomenclatura);
		logger.debug(methodName + " - splitData = " + splitData[0] + ".");
		logger.debug(methodName + " - splitData.length = " + splitData.length);
		logger.debug(methodName + " - cantidadAgrupacion = " + cantAgrup);
		String[] splitDataValidos = new String[cantAgrup];
		for (int fila = 0; fila < cantAgrup; fila++) {
			splitDataValidos[fila] = splitData[fila];
		}
		if (splitDataValidos.length == cantAgrup) {
			CBDataBancoModel bancoModel = new CBDataBancoModel();
			CBDataBancoDAO cbd = new CBDataBancoDAO();
			bancoModel.setcBCatalogoBancoId(Integer.toString(idBanco));
			bancoModel.setcBCatalogoAgenciaId(Integer.toString(idAgencia));
			bancoModel.setCBBancoAgenciaConfrontaId(Integer.toString(idAgeConfro));
			bancoModel.setCreadoPor(user);
			bancoModel.setTipo(tipo);
			bancoModel.setFechaCreacion(formato.format(fechaCreacion));
			bancoModel.setIdCargaMaestro(idMaestro);
			bancoModel.setComision(comisionConfronta);
			bancoModel.setFormatofecha(getFormatoFecha());
			logger.debug(
					methodName + "\nbanco agencia confronta usuario tipo fechaCreacion idMaestro comisionConfronta");
			logger.debug(methodName + idBanco + " " + idAgencia + " " + idConfronta + " " + user + " " + tipo + " "
					+ fechaCreacion + " " + idMaestro + " " + comisionConfronta + "\n");
			for (int countRec = 0; countRec < splitDataValidos.length; countRec++) {
				String valueToSave = splitNomCl[countRec];
				String strData = splitDataValidos[countRec];

				strData = strData.replaceAll("\"", ""); // Agrega Carlos Godinez -> 27/06/2017

				// Telefono Tipo 1
				if ("T".equals(valueToSave)) {
					logger.debug(methodName + "\n##Valor que lleva en la nomeclatura T = " + strData.trim());
					String numeros = strData.trim();
					String numReplace = numeros.replace(".", ",");
					Object[] split = numReplace.split(",");
					int telefono;
					String telstr = "";
					try {
						telefono = Integer.parseInt((String) split[0]);
						telstr = "" + telefono;
					} catch (Exception e) {
						logger.error(methodName + " - Numero vacio: " + e.getMessage());
						telstr = "";
					}

					if (telstr.length() == 8) {
						// System.out.println("Telefono: "
						// + telefono1);
						bancoModel.setTelefono(telstr.trim());
					} else {
						// Agrega Carlos Godinez - 12/09/2017
						if (telstr != null && !telstr.equals("") && !telstr.equals("0") && telstr.length() < 8) {
							logger.debug(methodName + " - Codigo Cliente asignado: " + telstr);
							bancoModel.setCodCliente(telstr.trim());
						}
						// FIN Carlos Godinez - 12/09/2017
					}

					// String telefonoString = "" + telstr;
					// bancoModel.setTelefono(telstr.trim());
					// bancoModel.setTelefono(strData.trim());
				}

				// Codigo - Transaccion
				if ("CT".equals(valueToSave)) {
					String numeros = strData.trim();

					int telefono;
					int transaccion;
					String telstr = "";
					String strtran = "";
					try {
						strtran = numeros.substring(0, numeros.length() - 10);
						telstr = numeros.substring(numeros.length() - 10, numeros.length());
						telefono = Integer.parseInt(strtran);
						transaccion = Integer.parseInt(telstr);
						telstr = String.valueOf(telefono);
						strtran = String.valueOf(transaccion);
					} catch (Exception e) {
						logger.error(methodName + " - Numero vacio: " + e.getMessage());
						telstr = "";
					}

					if (telstr.length() == 8) {
						logger.debug(methodName + " - Telefono: " + telstr);
						bancoModel.setTelefono(telstr.trim());
					} else {
						logger.debug(methodName + " - Codigo Cliente: " + telstr);
						bancoModel.setCodCliente(telstr.trim());
					}
					logger.debug(methodName + " - Transaccion: " + strtran);
					bancoModel.setTransaccion(strtran);

					// Agrega Carlos Godinez - 12/09/2017
					if (bancoModel.getCodCliente() == null || bancoModel.getCodCliente().equals("")) {
						logger.debug(methodName + " - Codigo de cliente va nulo, vacio o posee un valor de 0");
						logger.debug(methodName + " - Se asigna primera cadena string obtenida a codigo de cliente");
						bancoModel.setCodCliente(numeros);
					}
					// FIN Carlos Godinez - 12/09/2017

					// String telefonoString = "" + telstr;
					// bancoModel.setTelefono(telstr.trim());
					// bancoModel.setTelefono(strData.trim());
				}

				if ("C".equals(valueToSave)) {
					logger.debug(methodName + " - " + "\n## Valor que lleva en la nomeclatura C = " + strData.trim());
					String numeros = strData.trim();
					String numReplace = numeros.replace(".", ",");
					Object[] split = numReplace.split(",");
					int telefono;
					String telstr = "";
					try {
						telefono = Integer.parseInt((String) split[0]);
						telstr = "" + telefono;
					} catch (Exception e) {
						logger.error(methodName + " - Numero vacio: " + e.getMessage());
						telstr = "";
					}

					if (telstr.length() == 8) {
						// System.out.println("Telefono: "
						// + telefono1);
						bancoModel.setTelefono(telstr.trim());
					} else {
						// Agrega Carlos Godinez - 12/09/2017
						if (telstr != null && !telstr.equals("") && !telstr.equals("0")) {
							logger.debug(methodName + " - Codigo Cliente asignado: " + telstr);
							bancoModel.setCodCliente(telstr.trim());
						}
						// FIN Carlos Godinez - 12/09/2017
					}

				}
				// monto
				if ("M".equals(valueToSave)) {
					// Editado por Carlos Godinez - Qitcorp - 24/08/2017
					strData = strData.replace("$", "");
					strData = strData.trim();
					logger.debug(methodName + " - Monto antes de parsear = " + strData);
					if (strData.substring(0, 1).equals(".") || strData.substring(0, 1).equals(",")) {
						strData = "0".concat(strData);
						logger.debug(methodName + " - Primer elemento: " + strData.substring(0, 1) + " Nueva cadena: "
								+ strData);
					}

					String cadena = currencyToBigDecimalFormat(strData);
					logger.debug(methodName + " - Monto parseado = " + cadena);
					if (isBigDecimal(cadena)) {
						BigDecimal montoF = new BigDecimal(cadena);
						logger.debug(methodName + " -  Monto antes de guardar = " + montoF);
						bancoModel.setMonto(montoF);
					}
				}

				if ("MD".equals(valueToSave)) {
					// Editado por Carlos Godinez - Qitcorp - 24/08/2017
					strData = strData.replace("$", "");
					strData = strData.trim();
					logger.debug(methodName + " -  Monto antes de parsear = " + strData);
					String cadena = currencyToBigDecimalFormat(strData);
					logger.debug(methodName + " -  Monto parseado = " + cadena);
					if (isBigDecimal(cadena)) {
						BigDecimal montoF = new BigDecimal(cadena).divide(new BigDecimal(100));
						logger.debug(methodName + " - Monto antes de guardar = " + montoF);
						bancoModel.setMonto(montoF);
					}
				}

				// FECHA TIPO1
				if ("D".equals(valueToSave)) {
					// Se quita este parseo y se deja el que tiene el insert con to_date
					// String fechaB = customDate.getFormatDate(strData.trim(),
					// this.formatoFecha);
					bancoModel.setFecha(strData);
					bancoModel.setDia(strData);
					// System.out.println("Formato Fecha tipo 1: "
					// + bancoModel.getFecha());
				}
				// transaccion
				if ("O".equals(valueToSave)) {
					bancoModel.setTransaccion(strData.trim());
				}

				if ("H".equals(valueToSave)) {

					// se concatena la fecha obtenida anteriormente para cuando viene la hora
					// separada
					bancoModel.setFecha(bancoModel.getFecha() + " " + strData);
				}
				// FECHA TIPO3
				if ("P".equals(valueToSave)) {

					// System.out.println("nueva fecha: " + fecha);
					bancoModel.setFecha(bancoModel.getFecha() + " " + strData);
				}

				// texto uno
				if ("N".equals(valueToSave)) {

					bancoModel.setTexto1(strData.trim());

				}
				// texto dos
				if ("A".equals(valueToSave)) {

					bancoModel.setTexto2(strData.trim());

				}
				// Agencia Virtual/Fisica
				if ("R".equals(valueToSave)) {
					bancoModel.setCbAgenciaVirfisCodigo(strData.trim());
				}
				if (Constantes.NOM_FECHA_TIPO5.equals(valueToSave)) {
					bancoModel.setFecha(strData);
					bancoModel.setDia(strData);
				}

			}

			if (isDate(bancoModel.getFecha(), formatoFechaConfronta)
					&& ((bancoModel.getTelefono() != null && !bancoModel.getTelefono().equals(""))
							|| (bancoModel.getCodCliente() != null && !bancoModel.getCodCliente().equals("")))) {
				dataBancoModels.add(bancoModel);
			} else {
				enviarDataSinProcesar(strLine, nombreArchivo, user, idMaestro,
						"Cantidad erronea de campos consulte la configuracion de la confronta");
			}
		} else {
			enviarDataSinProcesar(strLine, nombreArchivo, user, idMaestro,
					"Cantidad erronea de campos consulte la configuracion de la confronta");
		}
	}

	private void getConfrontaConf(int idConfronta) {
		CBConfiguracionConfrontaDaoB cccb = new CBConfiguracionConfrontaDaoB();
		// System.out.println("id lista: " + idConfronta);
		List<CBConfiguracionConfrontaModel> dataList = cccb.obtieneListaConfConfronta(idConfronta);
		int sizeList = dataList.size();
		// System.out.println("tama;o lista: " + sizeList);
		for (int countRec = 0; sizeList > countRec; countRec++) {
			CBConfiguracionConfrontaModel confrontaModel = (CBConfiguracionConfrontaModel) dataList.get(countRec);
			this.delimitador1 = confrontaModel.getDelimitador1();
			this.delimitador2 = confrontaModel.getDelimitador2();
			logger.debug("getConfrontaConf()" + " - cantidad agrupacion: " + confrontaModel.getCantidadAgrupacion());
			this.cantidadAgrupacion = confrontaModel.getCantidadAgrupacion();
			this.nomenclatura = confrontaModel.getNomenclatura();
			this.formatoFecha = confrontaModel.getFormatoFecha();
			// valores para cuando la cadena no trae delimitadores
			this.posiciones = confrontaModel.getPosiciones();
			this.tamDeCadena = confrontaModel.getLongitudCadena();

		}
	}

	/**
	 * Agregado por Carlos Godinez - Qitcorp - 25/05/2017
	 * 
	 */

	public void enviarDataSinProcesar(String strLine, String nombreArchivo, String user, String idMaestro,
			String causa) {
		logger.debug("enviarDataSinProcesar()" + " - ENTRA A VALIDACION DE REGISTROS SIN PROCESAR");
		logger.debug("enviarDataSinProcesar()" + " - DATO QUE SE AGREGA A SIN PROCESAR = " + strLine);
		CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
		dataSinProcesarModel.setNombreArchivo(nombreArchivo);
		dataSinProcesarModel.setDataArchivo(strLine);
		dataSinProcesarModel.setCausa(causa);
		dataSinProcesarModel.setEstado(1);
		dataSinProcesarModel.setCreadoPor(user);
		dataSinProcesarModel.setIdCargaMaestro(idMaestro);
		// dataSinProcesarModel.setFechaCreacion(customDate.getMySQLDate());
		this.listDataSinProcesar.add(dataSinProcesarModel);
	}

	public boolean isBigDecimal(String cadena) {
		try {
			BigDecimal number = new BigDecimal(cadena);
			logger.debug("isBigDecimal()" + " -  Monto parseado = " + number);
		} catch (Exception nfe) {
			return false;
		}
		return true;
	}

	public static String currencyToBigDecimalFormat(String currency) {
		try {
			// Reemplazar todos los puntos decimales por comas
			currency = currency.replaceAll("\\.", ",");

			// Si el monto lleva decimales, el separador debe ser .
			if (currency.length() >= 3) {
				char[] chars = currency.toCharArray();
				if (chars[chars.length - 2] == ',') {
					chars[chars.length - 2] = '.';
				} else if (chars[chars.length - 3] == ',') {
					chars[chars.length - 3] = '.';
				}
				currency = new String(chars);
			}

			// Remover todas las comas
			return currency.replaceAll(",", "");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Validamos si el string enviado es fecha
	 */
	public boolean isDate(String fecha, String formatoDeseado) {
		logger.debug("isDate()" + " - ENTRA A PARSEO DE FECHA ENVIADA");
		Date fec = null;
		try {
			logger.debug("isDate()" + " - Formato fecha para Oracle = " + formatoDeseado);
			logger.debug("isDate()" + " - Fecha enviada = " + fecha);
			formatoDeseado = formatoDeseado.replace("hh24", "HH");
			formatoDeseado = formatoDeseado.replace("HH24", "HH");
			formatoDeseado = formatoDeseado.replace("mi", "mm");
			formatoDeseado = formatoDeseado.replace("MI", "mm");
			formatoDeseado = formatoDeseado.replace("am", "a"); // CarlosGodinez -> 30/08/2017
			formatoDeseado = formatoDeseado.replace("am", "a"); // CarlosGodinez -> 30/08/2017
			formatoDeseado = formatoDeseado.replace("AM", "a"); // CarlosGodinez -> 30/08/2017
			formatoDeseado = formatoDeseado.replace("a.m.", "a"); // CarlosGodinez -> 30/08/2017

			logger.debug("isDate()" + " - Formato fecha para Java = " + formatoDeseado);
			SimpleDateFormat format = new SimpleDateFormat(formatoDeseado, Locale.US);
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
			format.setLenient(false);

			DateFormatSymbols symbols = format.getDateFormatSymbols();
			symbols = (DateFormatSymbols) symbols.clone();
			symbols.setAmPmStrings(new String[] { "a.m.", "p.m.", "AM", "PM" });
			format.setDateFormatSymbols(symbols);

			fec = format.parse(fecha);
			logger.debug("isDate()" + " - Fecha parseada con exito = " + fec);
			logger.debug("isDate()" + " - Fin de parseo fecha");
			return true;
		} catch (ParseException e) {
			logger.debug("isDate" + " - fecha con error: " + fecha);
			logger.error("isDate() - Error : ", e);
			return false;
		} catch (NullPointerException e) {
			logger.error("isDate() - Error : ", e);
			return false;
		}
	}

	/**
	 * FIN Agregado por Carlos Godinez
	 */

	public String guardarInfArchivo(List<CBDataBancoModel> dataArchivo, int idBanco, int idAgencia, int idConfronta) {
		throw new UnsupportedOperationException("Not supported yet."); // To
																		// change
																		// body
																		// of
																		// generated
																		// methods,
																		// choose
																		// Tools
																		// |
																		// Templates.
	}

	public List<CBDataSinProcesarModel> getDataSinProcesar() {
		return this.listDataSinProcesar;
	}

	public String getFormatoFecha() {
		return formatoFecha;
	}

	public void setFormatoFecha(String formatoFecha) {
		this.formatoFecha = formatoFecha;
	}

}
