/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;
import com.terium.siccam.dao.CBConfiguracionConfrontaDaoB;
import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.implement.ConciliacionService;
import com.terium.siccam.implement.ProcessFileTxtService;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

/**
 * 
 * @author rSianB to terium.com
 */
public class ProcessFileTxtServImplNI extends ControladorBase implements ProcessFileTxtService {
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
	private String fechaArchivo;
	private ConciliacionService service;
	private List<Orden> orden;
	private List<CBDataSinProcesarModel> listDataSinProcesar = new ListModelList<CBDataSinProcesarModel>();

	// Agregado por Carlos Godinez - 29/05/2017
	private int splitDataLenght = 0;

	List<CBDataBancoModel> dataBancoModels = new ListModelList<CBDataBancoModel>();

	public List<CBDataBancoModel> leerArchivo(BufferedReader bufferedReader, int idBanco, int idAgencia,
			int idConfronta, String user, String nombreArchivo, String tipo, BigDecimal comisionConfronta,
			int idAgeConfro) {
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

			Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
					"INSERT DE ARCHIVO REALIZADO CON ÉXITO");
			/**
			 * Agregado por Carlos Godinez - Configuracion para leer desde la linea N de
			 * cualquier archivo
			 */
			CBDataBancoDAO objeDataBancoDAO = new CBDataBancoDAO();
			int contLinea = 0;
			int lineaEmpiezaLeer = objeDataBancoDAO.obtenerLineaLectura(idConfronta);
			String formatoFechaConfronta = objeDataBancoDAO.obtenerFormatoFechaConfronta(idConfronta); // CarlosGodinez
																										// -> 28/08/2017
			/**
			 * 
			 * */
			try {
				getConfrontaConf(idConfronta);
				// Lee el archivo linea por linea
				while ((strLine = bufferedReader.readLine()) != null) {
					if (contLinea >= lineaEmpiezaLeer) {
						// strLine1 = strLine.replaceAll(" ", "").trim();
						strLine1 = strLine.trim();

						// ************************************************************************************
						strLine1 = strLine1.replaceAll("\"", ""); // Agrega Carlos Godinez -> 27/06/2017

						if (strLine1.trim().length() != 0) {

							// *********************************************************************************
							String delimitador = this.delimitador1;

							Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
									"\nLinea: " + strLine1);
							Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
									"Longitud: " + strLine1.length());
							Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
									"VALOR DE LA VARIABLE 'delimitador' = " + delimitador);

							if (delimitador.compareTo("n/a") == 0) {

								char aChar = strLine1.charAt(0);
								String cambio = "" + aChar;

								Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
										"QUE TIENE EL CHAR: " + cambio);

								if (strLine1.length() > 8) {
									String[] pos = this.posiciones.split(",");
									String longit = this.tamDeCadena;
									String[] splitNomCl = this.nomenclatura.split(",");

									Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
											"variable longit = " + longit);
									Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
											"LONGITUD DE VARIABLE longit = " + strLine1.length() + "\n");

									if (strLine1.length() == Integer.parseInt(longit)) {

										Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
												"longitud: " + longit);

										CBDataBancoModel bancoModel = new CBDataBancoModel();
										bancoModel.setcBCatalogoBancoId(Integer.toString(idBanco));
										bancoModel.setcBCatalogoAgenciaId(Integer.toString(idAgencia));
										bancoModel.setCBBancoAgenciaConfrontaId(Integer.toString(idAgeConfro));
										bancoModel.setCreadoPor(user);
										bancoModel.setTipo(tipo);
										bancoModel.setFechaCreacion(formato.format(fechaCreacion));
										bancoModel.setIdCargaMaestro(idMaestro);
										bancoModel.setFecha(getFormatoFecha());

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

													Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
															.log(Level.INFO, "TAMAÑO DEL TELEFONO: " + telefono1.length()
																	+ " Telefono: " + telefono);

													if (telefono1.length() == 8) {
														bancoModel.setTelefono(telefono1.trim());
													} else {
														System.out.println("Codigo Cliente: " + telefono1);
														bancoModel.setCodCliente(telefono1.trim());
													}
												}catch (Exception e) {
													// TODO: handle exception
													Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
													.log(Level.INFO,"Formato de telefono/codigo cliente erroneo.");
													bancoModel.setTelefono("");
													bancoModel.setCodCliente("");
												}
												
											}

											if ("C".equals(nomenCla)) {

												String telefono = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2)).trim();

												int entero = Integer.parseInt(telefono.trim());
												String telefono1 = ("" + entero).trim();

												Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
														.log(Level.INFO, "TAMAÑO DEL TELEFONO: " + telefono1.length()
																+ " Telefono: " + telefono);
												if (telefono1.length() == 8) {
													bancoModel.setTelefono(telefono1.trim());
												} else {
													Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
															.log(Level.INFO, "Codigo Cliente: " + telefono1);
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

												Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
														.log(Level.INFO, "** Monto antes de parsear: " + strMonto);
												
												if(strMonto.substring(0,1).equals(".") || strMonto.substring(0,1).equals(",")) {
													strMonto = "0".concat(strMonto);
													System.out.println("Primer elemento: " + strMonto.substring(0,1)  + " Nueva cadena: " + strMonto);
												}
												
												String cadena = currencyToBigDecimalFormat(strMonto);
												if (isBigDecimal(cadena)) {
													BigDecimal monto;
													if (cadena.indexOf(".") != -1) {
														Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(
																Level.INFO, "viene con punto decimal con delimitador");
														monto = new BigDecimal(cadena);
													} else {
														Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(
																Level.INFO, "viene sin punto decimal con delimitador");
														monto = new BigDecimal(cadena).divide(new BigDecimal(100));
													}

													Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
															.log(Level.INFO, "** Monto parseado: " + monto);
													bancoModel.setMonto(monto);
												} else {
													Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
															.log(Level.INFO, "Monto no valido");
												}
											}

											// Texto1
											if ("N".equals(nomenCla)) {

												String texto1 = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));

												Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
														.log(Level.INFO, "Texto1: " + texto1);
												bancoModel.setTexto1(texto1.trim());

											}

											// Texto2
											if ("A".equals(nomenCla)) {

												String texto2 = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
														.log(Level.INFO, "Texto1: " + texto2);
												bancoModel.setTexto2(texto2.trim());

											}
											// Transaccion

											if ("O".equals(nomenCla)) {

												String transaccion = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));

												Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
														.log(Level.INFO, "Transaccion: " + transaccion);
												bancoModel.setTransaccion(transaccion.trim());
											}
											if ("R".equals(nomenCla)) {
												String agencia = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												System.out.println("Agencia: " + agencia);
												bancoModel.setCbAgenciaVirfisCodigo(agencia.trim());
											}

											if ("D".equals(nomenCla)) {
												String fecha = strLine1.substring(Integer.parseInt(posicion1),
														Integer.parseInt(posicion2));
												Logger.getLogger(ProcessFileTxtServImplNI.class.getName())
														.log(Level.INFO, "Fecha: " + fecha);
												System.out.println("Fecha: " + fecha);
												bancoModel.setFecha(fecha);
												bancoModel.setDia(fecha);
											}

										}
										if (isDate(bancoModel.getFecha(), formatoFechaConfronta)
												&& ((bancoModel.getTelefono() != null && !bancoModel.getTelefono().equals(""))
														|| (bancoModel.getCodCliente() != null && !bancoModel.getCodCliente().equals("")))) {
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
									Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
											"valida si es diguito: " + aChar);

									String cambioFecha = strLine1.toString();
									CustomDate customDate = new CustomDate();
									setFormatoFecha(customDate.getFormatFecha(cambioFecha.trim(), this.formatoFecha));
								} else {
									Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
											"====> No agrega la info a la lista: ");
								}

							} else {
								// strLine1 = strLine.trim();

								if (strLine1.trim().length() != 0) {
									char aChar = strLine1.charAt(0);

									Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
											"Delimitador para lectura1: " + this.delimitador1);
									Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
											"Numero de columnas configuradas: " + this.cantidadAgrupacion);

									String[] splitData = strLine1.split("\\" + this.delimitador1);
									splitDataLenght = splitData.length;

									Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
											"Numero de columnas del archivo: " + splitDataLenght);
									// Condicion editada por Carlos Godinez - Qitcorp - 22/05/2017
									String[] splitDataValidos = new String[this.cantidadAgrupacion];

									// Examina que el numero de columnas de la linea que se esta leyendo sea
									// mayor o igual a la cantidad de agrupacion configurada
									if (splitData.length >= this.cantidadAgrupacion) {
										for (int fila = 0; fila < this.cantidadAgrupacion; fila++) {
											splitDataValidos[fila] = splitData[fila];
										}
										if (splitDataValidos.length == this.cantidadAgrupacion) {

											Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
													"\n*** Cumple condición: splitDataValidos.length == this.cantidadAgrupacion ***\n");

											// Si la linea es valida, se pasan los
											// parametros a getBancoModel()
											getBancoModel(strLine1, idBanco, idAgencia, idConfronta, user,
													nombreArchivo, idMaestro, tipo, comisionConfronta,
													getFormatoFecha(), idAgeConfro, formatoFechaConfronta);

										} else {
											Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
													"\n*** NO Cumple condición: splitDataValidos.length == this.cantidadAgrupacion ***\n");

											enviarDataSinProcesar(strLine1, nombreArchivo, user, idMaestro,
													"La cantidad de agrupacion de la linea leida es menor a la configurada");
										}
									} else {
										Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
												"\n*** NO Cumple condición: splitDataValidos.length == this.cantidadAgrupacion ***\n");
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
						+ "\n\nCambie la cantidad de agrupación de la confronta seleccionada en la pantalla de configuración de confrontas",
						"ERROR", Messagebox.OK, Messagebox.EXCLAMATION);
				Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IOException ex) {
				Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.SEVERE, null, ex);
			} catch (Exception e) {
				Messagebox.show("Ha ocurrido un error al intentar procesar el archivo de confronta", "ATENCION",
						Messagebox.OK, Messagebox.ERROR);
				Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.SEVERE, null, e);
			}
		} else {
			// System.out.println("viendo si sale");
			enviarDataSinProcesar(strLine1, nombreArchivo, user, idMaestro,
					"Error al intentar cargar archivo de confronta");
		}
		return dataBancoModels;
	}

	/**
	 * Editado ultima vez por Carlos Godinez -> 28/08/2017
	 * 
	 * Se agrega parametro de formato fecha de la confronta
	 */
	private void getBancoModel(String strLine, int idBanco, int idAgencia, int idConfronta, String user,
			String nombreArchivo, String idMaestro, String tipo, BigDecimal comisionConfronta, String formatFecha,
			int idAgeConfro, String formatoFechaConfronta) {
		boolean registroValido = false;
		Date fechaCreacion = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		CustomDate customDate = new CustomDate();

		String[] splitData = strLine.split("\\" + delimitador1);
		String[] splitNomCl = this.nomenclatura.split(",");

		Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
				"-----------------------------------");
		Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO, "Nomenclatura: " + nomenclatura);
		Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO, "splitData = " + splitData[0] + ".");
		Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
				"splitData.length = " + splitData.length);
		Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
				"cantidadAgrupacion = " + this.cantidadAgrupacion);
		Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
				"-----------------------------------");

		String[] splitDataValidos = new String[this.cantidadAgrupacion];
		for (int fila = 0; fila < this.cantidadAgrupacion; fila++) {
			splitDataValidos[fila] = splitData[fila];
		}
		if (splitDataValidos.length == this.cantidadAgrupacion) {
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

			Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
					"\nbanco agencia confronta usuario tipo fechaCreacion idMaestro comisionConfronta");
			Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
					idBanco + " " + idAgencia + " " + idConfronta + " " + user + " " + tipo + " " + fechaCreacion + " "
							+ idMaestro + " " + comisionConfronta + "\n");

			for (int countRec = 0; countRec < splitDataValidos.length; countRec++) {
				String valueToSave = splitNomCl[countRec];
				String strData = splitDataValidos[countRec];

				strData = strData.replaceAll("\"", ""); // Agrega Carlos Godinez -> 27/06/2017

				// Telefono Tipo 1
				if ("T".equals(valueToSave)) {

					Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
							"\n##Valor que lleva en la nomeclatura T = " + strData.trim());
					String numeros = strData.trim();
					String numReplace = numeros.replace(".", ",");
					Object[] split = numReplace.split(",");
					int telefono;
					String telstr = "";
					try {
						telefono = Integer.parseInt((String) split[0]);
						telstr = "" + telefono;
					} catch (Exception e) {
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.SEVERE, null, e);
						telstr = "";
					}

					if (telstr.length() == 8) {
						bancoModel.setTelefono(telstr.trim());
					} else {
						// Agrega Carlos Godinez - 12/09/2017
						if (telstr != null && !telstr.equals("") && !telstr.equals("0") && telstr.length() < 8) {
							Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
									"Codigo Cliente asignado: " + telstr);
							bancoModel.setCodCliente(telstr.trim());
						}
						// FIN Carlos Godinez - 12/09/2017
					}
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
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.SEVERE, null, e);
						telstr = "";
					}

					if (telstr.length() == 8) {
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
								"Telefono: " + telstr);
						bancoModel.setTelefono(telstr.trim());
					} else {
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
								"Codigo Cliente: " + telstr);
						bancoModel.setCodCliente(telstr.trim());
					}
					Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
							"Transaccion: " + strtran);
					bancoModel.setTransaccion(strtran);

					// Agrega Carlos Godinez - 12/09/2017
					if (bancoModel.getCodCliente() == null || "".equals(bancoModel.getCodCliente())) {
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
								"** Codigo de cliente va nulo, vacio o posee un valor de 0");
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
								"** Se asigna primera cadena string obtenida a codigo de cliente");
						bancoModel.setCodCliente(numeros);
					}
					// FIN Carlos Godinez - 12/09/2017

				}

				if ("C".equals(valueToSave)) {
					Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
							"\n## Valor que lleva en la nomeclatura C = " + strData.trim());
					String numeros = strData.trim();
					String numReplace = numeros.replace(".", ",");
					Object[] split = numReplace.split(",");
					int telefono;
					String telstr = "";
					try {
						telefono = Integer.parseInt((String) split[0]);
						telstr = "" + telefono;
					} catch (Exception e) {
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.SEVERE, null, e);
						telstr = "";
					}

					if (telstr.length() == 8) {
						bancoModel.setTelefono(telstr.trim());
					} else {
						// Agrega Carlos Godinez - 12/09/2017
						if (telstr != null && !"".equals(telstr) && !"0".equals(telstr)) {
							Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
									"Codigo Cliente asignado: " + telstr);
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

					Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
							"** Monto antes de parsear = " + strData);
					
					if(strData.substring(0,1).equals(".") || strData.substring(0,1).equals(",")) {
						strData = "0".concat(strData);
						System.out.println("Primer elemento: " + strData.substring(0,1)  + " Nueva cadena: " + strData);
					}
					
					
					String cadena = currencyToBigDecimalFormat(strData);

					Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
							"** Monto parseado = " + cadena);
					if (isBigDecimal(cadena)) {
						BigDecimal montoF = new BigDecimal(cadena);
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
								"** Monto antes de guardar = " + montoF);
						bancoModel.setMonto(montoF);
					}
				}

				if ("MD".equals(valueToSave)) {
					// Editado por Carlos Godinez - Qitcorp - 24/08/2017
					strData = strData.replace("$", "");
					strData = strData.trim();

					Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
							"** Monto antes de parsear = " + strData);
					String cadena = currencyToBigDecimalFormat(strData);
					Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
							"** Monto parseado = " + cadena);
					if (isBigDecimal(cadena)) {
						BigDecimal montoF = new BigDecimal(cadena).divide(new BigDecimal(100));
						Logger.getLogger(ProcessFileTxtServImplNI.class.getName()).log(Level.INFO,
								"** Monto antes de guardar = " + montoF);
						bancoModel.setMonto(montoF);
					}
				}

				// FECHA TIPO1
				if ("D".equals(valueToSave)) {
					// Se quita este parseo y se deja el que tiene el insert con to_date
					bancoModel.setFecha(strData);
					bancoModel.setDia(strData);
					System.out.println("Fecha_: " + strData);
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

		List<CBConfiguracionConfrontaModel> dataList = cccb.obtieneListaConfConfronta(idConfronta);
		int sizeList = dataList.size();

		for (int countRec = 0; sizeList > countRec; countRec++) {
			CBConfiguracionConfrontaModel confrontaModel = (CBConfiguracionConfrontaModel) dataList.get(countRec);
			this.delimitador1 = confrontaModel.getDelimitador1();
			this.delimitador2 = confrontaModel.getDelimitador2();
			System.out.println("cantidad agrupacion: " + confrontaModel.getCantidadAgrupacion());
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
		System.out.println("\nENTRA A VALIDACION DE REGISTROS SIN PROCESAR\n");
		System.out.println("DATO QUE SE AGREGA A SIN PROCESAR = " + strLine);
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
			System.out.println("** Monto parseado = " + number);
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
		System.out.println("======== ENTRA A PARSEO DE FECHA ENVIADA ========");
		Date fec = null;
		try {
			System.out.println("Formato fecha para Oracle = " + formatoDeseado);
			System.out.println("Fecha enviada = " + fecha);
			formatoDeseado = formatoDeseado.replace("hh24", "HH");
			formatoDeseado = formatoDeseado.replace("HH24", "HH");
			formatoDeseado = formatoDeseado.replace("mi", "mm");
			formatoDeseado = formatoDeseado.replace("MI", "mm");
			formatoDeseado = formatoDeseado.replace("am", "a"); // CarlosGodinez -> 30/08/2017
			formatoDeseado = formatoDeseado.replace("am", "a"); // CarlosGodinez -> 30/08/2017
			formatoDeseado = formatoDeseado.replace("AM", "a"); // CarlosGodinez -> 30/08/2017
			formatoDeseado = formatoDeseado.replace("a.m.", "a"); // CarlosGodinez -> 30/08/2017

			System.out.println("Formato fecha para Java = " + formatoDeseado);
			SimpleDateFormat format = new SimpleDateFormat(formatoDeseado, Locale.US);
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
			format.setLenient(false);

			DateFormatSymbols symbols = format.getDateFormatSymbols();
			symbols = (DateFormatSymbols) symbols.clone();
			symbols.setAmPmStrings(new String[] { "a.m.", "p.m.", "AM", "PM" });
			format.setDateFormatSymbols(symbols);

			fec = format.parse(fecha);
			System.out.println("Fecha parseada con exito = " + fec);
			System.out.println("======== FIN DE PARSEO DE FECHA =========");
			return true;
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.out.println("fecha con error: " + fecha);
			System.out.println("======== FIN DE PARSEO DE FECHA =========");
			return false;
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			System.out.println("fecha null: " + fecha);
			System.out.println("======== FIN DE PARSEO DE FECHA =========");
			return false;
		}
	}

	/**
	 * FIN Agregado por Carlos Godinez
	 */

	public String guardarInfArchivo(List<CBDataBancoModel> dataArchivo,  int idBanco, int idAgencia,
			int idConfronta) {
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
