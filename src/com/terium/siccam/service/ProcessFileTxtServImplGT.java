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
import com.terium.siccam.implement.ProcessFileTxtServiceGT;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.utils.CustomDate;
import com.terium.siccam.utils.Orden;

import java.io.BufferedReader;
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

/**
 * 
 * @author rSianB to terium.com
 */
public class ProcessFileTxtServImplGT extends ControladorBase implements ProcessFileTxtServiceGT {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String delimitador1;

	// variables para archivos sin delimitadores

	private String posiciones;
	private String tamDeCadena;

	@SuppressWarnings("unused")
	private String delimitador2;

	private int cantidadAgrupacion;
	private String nomenclatura;
	private String formatoFecha;

	@SuppressWarnings("unused")
	private String fechaArchivo;

	@SuppressWarnings("unused")
	private ConciliacionService service;

	@SuppressWarnings("unused")
	private List<Orden> orden;

	private List<CBDataSinProcesarModel> listDataSinProcesar = new ListModelList<CBDataSinProcesarModel>();

	// Agregado por Carlos Godinez - 29/05/2017
	@SuppressWarnings("unused")
	private int splitDataLenght = 0;

	List<CBDataBancoModel> dataBancoModels = new ListModelList<CBDataBancoModel>();

	@SuppressWarnings("unused")
	public List<CBDataBancoModel> leerArchivoGT(BufferedReader bufferedReader, int idBanco, int idAgencia,
			int idConfigAgencia, int idConfronta, String user, String nombreArchivo, String tipo,
			BigDecimal comisionConfronta) {
		String nombreBi = nombreArchivo.substring(0, 8).trim();
		Date fechaCreacion = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		String strLine = "";
		String strLine1 = "";
		String idMaestro;
		String linea = "";
		CBArchivosInsertadosDAO cvaidao = new CBArchivosInsertadosDAO();
		idMaestro = cvaidao.idMaestroCarga();
		if (cvaidao.insertarArchivos(idMaestro, nombreArchivo, idBanco, idAgencia, user)) {
			Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.INFO, "ID CONFRONTA: " + idConfronta);

			try {
				//conn = ControladorBase.obtenerDtsPromo().getConnection();
				getConfrontaConfGT( idConfigAgencia);
			
				Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.INFO, "");
				// Lee el archivo linea por linea
				while ((strLine = bufferedReader.readLine()) != null) {
					// strLine1 = strLine.replaceAll(" ", "").trim();
					strLine1 = strLine.trim();

					// ************************************************************************************

					if (strLine1.trim().length() != 0) {

						// *********************************************************************************
						String delimitador = this.delimitador1;
						if (delimitador.compareTo("n/a") == 0) {

							char aChar = strLine1.charAt(0);
							String cambio = "" + aChar;
							int numero = Integer.parseInt(cambio);
							CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();

							// ********************************************************************************

							if (strLine1.length() > 8 && Character.isDigit(aChar) == true) {
								String[] pos = this.posiciones.split(",");
								String longit = this.tamDeCadena;
								String[] splitNomCl = this.nomenclatura.split(",");
								if (strLine1.length() == Integer.parseInt(longit)) {

									CBDataBancoModel bancoModel = new CBDataBancoModel();
									CBDataBancoDAO cbd = new CBDataBancoDAO();
									bancoModel.setcBCatalogoBancoId(Integer.toString(idBanco));
									bancoModel.setcBCatalogoAgenciaId(Integer.toString(idAgencia));
									bancoModel.setCBBancoAgenciaConfrontaId(Integer.toString(idConfronta));
									bancoModel.setCreadoPor(user);
									bancoModel.setTipo(tipo);
									bancoModel.setFechaCreacion(formato.format(fechaCreacion));
									bancoModel.setIdCargaMaestro(idMaestro);
									bancoModel.setFecha(getFormatoFecha());
									bancoModel.setDia(getFormatoFecha());
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

										// ******************************************

										String nomenCla = splitNomCl[countRec];
										// Telefono
										if ("T".equals(nomenCla)) {

											String telefono = strLine1
													.substring(Integer.parseInt(posicion1), Integer.parseInt(posicion2))
													.trim();
											
											try {
												int entero = Integer.parseInt(telefono.trim());
												String telefono1 = ("" + entero).trim();

												if (telefono1.length() == 8) {
													bancoModel.setTelefono(telefono1.trim());
												} else {
													if (telefono1.length() < 8) {
														bancoModel.setCodCliente(telefono1.trim());
													}

												}
											}catch (Exception e) {
												// TODO: handle exception
												Logger.getLogger(ProcessFileTxtServImplGT.class.getName())
												.log(Level.INFO,"Formato de telefono/codigo cliente erroneo.");
												bancoModel.setTelefono("");
												bancoModel.setCodCliente("");
											}
											
										}

										// Monto
										if ("M".equals(nomenCla)) {

											BigDecimal monto = BigDecimal.valueOf(
													Double.parseDouble(strLine1.substring(Integer.parseInt(posicion1),
															Integer.parseInt(posicion2))) / 100);
											// System.out.println("Monto: "
											// + monto);
											bancoModel.setMonto(monto);
										}

										// Texto1
										if ("N".equals(nomenCla)) {

											String texto1 = strLine1.substring(Integer.parseInt(posicion1),
													Integer.parseInt(posicion2));
											// System.out.println("Texto1: "
											// + texto1);
											bancoModel.setTexto1(texto1.trim());

										}

										// Texto2
										if ("A".equals(nomenCla)) {

											String texto2 = strLine1.substring(Integer.parseInt(posicion1),
													Integer.parseInt(posicion2));
											// System.out.println("Texto1: "
											// + texto2);
											bancoModel.setTexto2(texto2.trim());

										}
										// Transaccion

										if ("O".equals(nomenCla)) {

											String transaccion = strLine1.substring(Integer.parseInt(posicion1),
													Integer.parseInt(posicion2));
											// System.out.println("Transaccion: "
											// + transaccion);
											bancoModel.setTransaccion(transaccion.trim());
										}
										if ("R".equals(nomenCla)) {
											String agencia = strLine1.substring(Integer.parseInt(posicion1),
													Integer.parseInt(posicion2));

											bancoModel.setCbAgenciaVirfisCodigo(agencia.trim());
										}

									}
									if ((bancoModel.getTelefono() != null && !bancoModel.getTelefono().equals(""))
											|| (bancoModel.getCodCliente() != null && !bancoModel.getCodCliente().equals(""))) {

									dataBancoModels.add(bancoModel);
									}else {
									//	CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
										dataSinProcesarModel.setNombreArchivo(nombreArchivo);
										dataSinProcesarModel.setDataArchivo(strLine1);
										dataSinProcesarModel.setCausa(
												"Esta linea no lleva un numero de telefono o codigo de cliente valido");
										dataSinProcesarModel.setEstado(1);
										dataSinProcesarModel.setCreadoPor(user);
										dataSinProcesarModel.setIdCargaMaestro(idMaestro);
										// dataSinProcesarModel.setFechaCreacion(customDate.getMySQLDate());
										this.listDataSinProcesar.add(dataSinProcesarModel);
									}
								} else {

									//CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
									dataSinProcesarModel.setNombreArchivo(nombreArchivo);
									dataSinProcesarModel.setDataArchivo(strLine1);
									dataSinProcesarModel.setCausa(
											"Cantidad erronea de campos consulte la configuracion de la confronta");
									dataSinProcesarModel.setEstado(1);
									dataSinProcesarModel.setCreadoPor(user);
									dataSinProcesarModel.setIdCargaMaestro(idMaestro);
									// dataSinProcesarModel.setFechaCreacion(customDate.getMySQLDate());
									this.listDataSinProcesar.add(dataSinProcesarModel);
								}

							} else if (Character.isDigit(aChar) == true) {

								String cambioFecha = strLine1.toString();
								CustomDate customDate = new CustomDate();
								setFormatoFecha(customDate.getFormatFecha(cambioFecha.trim(), this.formatoFecha));

							}

						} else {
							strLine1 = strLine.trim();
							// char aChar = strLine1.charAt(0);
							// linea = strLine1.substring(0);
							if (strLine1.trim().length() != 0) {
								// strLine1 = strLine.trim();
								char aChar = strLine1.charAt(0);
								linea = strLine1.substring(0);
								// System.out.println("Que lleva la linea: "
								// + linea);
								if (Character.isDigit(aChar) == true) {

									// System.out.println("Es digito TRUE: " + Character.isDigit(aChar));
									// strLine.substring(0);
									// System.out.println("QUE LLEVA AQUI: " + strLine1);
									getBancoModelGT(strLine1, idBanco, idAgencia, idConfronta, user, nombreArchivo,
											idMaestro, tipo, comisionConfronta);
								} else {
									// System.out.println("Es digito FALSE: " + Character.isDigit(aChar));
									// strLine.substring(0);
									// System.out.println("QUE LLEVA AQUI: " + strLine1);
									CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
									dataSinProcesarModel.setNombreArchivo(nombreArchivo);
									dataSinProcesarModel.setDataArchivo(strLine1);
									dataSinProcesarModel.setCausa(
											"Cantidad erronea de campos consulte la configuracion de la confronta");
									dataSinProcesarModel.setEstado(1);
									dataSinProcesarModel.setCreadoPor(user);
									dataSinProcesarModel.setIdCargaMaestro(idMaestro);
									// dataSinProcesarModel.setFechaCreacion(customDate.getMySQLDate());
									this.listDataSinProcesar.add(dataSinProcesarModel);
								}
							}
						}
					}

				}
			} catch (Exception ex) {
				Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				
				
			}
		} else {
			// System.out.println("viendo si sale");
			CBDataBancoModel bancoModel = new CBDataBancoModel();
			CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
			bancoModel.setObservacion("Error");
			dataSinProcesarModel.setObservacion("Error");
			dataBancoModels.add(bancoModel);
			this.listDataSinProcesar.add(dataSinProcesarModel);
		}
		return dataBancoModels;
	}

	@SuppressWarnings("unused")
	private void getBancoModelGT(String strLine, int idBanco, int idAgencia, int idConfronta, String user,
			String nombreArchivo, String idMaestro, String tipo, BigDecimal comisionConfronta) {
		// boolean registroValido = false;
		CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
		Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.INFO, "Linea a evaluar ==> " + strLine);

		Date fechaCreacion = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		CustomDate customDate = new CustomDate();

		String[] splitData = strLine.split("\\" + delimitador1);
		String[] splitNomCl = this.nomenclatura.split(",");
		String[] splitDataValidos = new String[this.cantidadAgrupacion];
		if (splitData.length > this.cantidadAgrupacion) {
			for (int fila = 0; fila < this.cantidadAgrupacion; fila++) {
				splitDataValidos[fila] = splitData[fila];
			}
		} else {
			splitDataValidos = splitData;
		}

		if (splitDataValidos.length == this.cantidadAgrupacion) {

			CBDataBancoModel bancoModel = new CBDataBancoModel();
			CBDataBancoDAO cbd = new CBDataBancoDAO();
			bancoModel.setcBCatalogoBancoId(Integer.toString(idBanco));
			bancoModel.setcBCatalogoAgenciaId(Integer.toString(idAgencia));
			bancoModel.setCBBancoAgenciaConfrontaId(Integer.toString(idConfronta));
			bancoModel.setCreadoPor(user);
			bancoModel.setTipo(tipo);
			bancoModel.setFechaCreacion(formato.format(fechaCreacion));
			bancoModel.setIdCargaMaestro(idMaestro);
			bancoModel.setComision(comisionConfronta);

			for (int countRec = 0; countRec < splitDataValidos.length; countRec++) {
				String valueToSave = splitNomCl[countRec];
				String strData = splitDataValidos[countRec];
				// Telefono Tipo 1
				/**
				 * Modified by OvidioSantos -> Cambio de tipo de dato de int a Long Modified by
				 * CarlosGodinez 18/09/2018 -> Validacion de longitud de telefono/codigo cliente
				 */
				if ("T".equals(valueToSave)) {
					Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.INFO,
							"\n##Valor que lleva en la nomeclatura T = " + strData.trim());
					String numeros = strData.trim();
					String numReplace = numeros.replace(".", ",");
					Object[] split = numReplace.split(",");

					Long telefono;
					String telstr = "";
					try {
						telefono = Long.parseLong((String) split[0]);
						telstr = "" + telefono;
					} catch (Exception e) {
						Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.SEVERE,
								"Numero vacio: " + e.getMessage());
						telstr = "";
					}

					if (telstr.length() == 8) {
						bancoModel.setTelefono(telstr.trim());
					} else {
						if (telstr != null && !"".equals(telstr) && !"0".equals(telstr) && telstr.length() < 8) {
							Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.INFO,
									"Codigo Cliente asignado: " + telstr);
							bancoModel.setCodCliente(telstr.trim());
						}
					}
				}

				/**
				 * Added by CarlosGodinez -> 18/09/2018 Evaluacion de nomenclatura de codigo de
				 * cliente
				 */
				if ("C".equals(valueToSave)) {
					System.out.println("\n## Valor que lleva en la nomeclatura C = " + strData.trim());
					String numeros = strData.trim();
					String numReplace = numeros.replace(".", ",");
					Object[] split = numReplace.split(",");

					Long telefono;
					String telstr = "";
					try {
						telefono = Long.parseLong((String) split[0]);
						telstr = "" + telefono;
					} catch (Exception e) {
						Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.SEVERE,
								"Numero vacio: " + e.getMessage());
						telstr = "";
					}

					if (telstr.length() == 8) {
						bancoModel.setTelefono(telstr.trim());
					} else {
						if (telstr != null && !telstr.equals("") && !telstr.equals("0")) {
							Logger.getLogger(ProcessFileTxtServImplGT.class.getName()).log(Level.INFO,
									"Codigo Cliente asignado: " + telstr);
							bancoModel.setCodCliente(telstr.trim());
						}
					}

				}
				/**
				 * FIN CarlosGodinez -> 18/09/2018
				 */

				if ("M".equals(valueToSave)) {
					strData = strData.replace(",", "");
					strData = strData.trim();
					System.out.println("** Monto antes de parsear = " + strData);
					if (isBigDecimal(strData)) {
						BigDecimal montoF = new BigDecimal(strData);
						System.out.println("** Monto antes de guardar = " + montoF);
						bancoModel.setMonto(montoF);
					}
				}
				/**
				 * Agrega Carlos Godinez - 01/11/2017 se agrega para mapeo de monto dividido
				 */
				if ("MD".equals(valueToSave)) {
					strData = strData.trim();
					System.out.println("** Monto antes de dividir = " + strData);
					BigDecimal montoF = new BigDecimal(strData).divide(new BigDecimal(100));
					System.out.println("** Monto dividido = " + montoF);
					bancoModel.setMonto(montoF);
				}
				/**
				 * FIN - Carlos Godinez -> 01/11/2017
				 */
				// FECHA TIPO1
				if ("D".equals(valueToSave)) {
					String fechaB = customDate.getFormatDate(strData.trim(), this.formatoFecha);
					bancoModel.setFecha(fechaB);
					bancoModel.setDia(fechaB);
					// System.out.println("Formato Fecha tipo 1: "
					// + bancoModel.getFecha());
				}
				// transaccion
				if ("O".equals(valueToSave)) {
					bancoModel.setTransaccion(strData.trim());
				}

				// FECHA TIPO2
				if ("H".equals(valueToSave)) {
					// System.out.println("fecha antes de substraer: "
					// + bancoModel.getFecha().substring(0, 10));
					String fecha = customDate.getFormatDate(
							bancoModel.getFecha().substring(0, 10) + " " + strData.trim(), "dd/MM/yyyy HHmmss");
					// System.out.println("nueva fecha: " + fecha);
					bancoModel.setFecha(fecha);
				}
				// FECHA TIPO3
				if ("P".equals(valueToSave)) {
					System.out.println("nueva fecha: " + bancoModel.getFecha());
					String fecha = customDate.getFormatDate(
							bancoModel.getFecha().substring(0, 10) + " " + strData.trim(), "dd/MM/yyyy HH:mm:ss");
					bancoModel.setFecha(fecha);
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
			if ((bancoModel.getTelefono() != null && !bancoModel.getTelefono().equals(""))
					|| (bancoModel.getCodCliente() != null && !bancoModel.getCodCliente().equals(""))) {
				dataBancoModels.add(bancoModel);
			} else {
				// CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
				dataSinProcesarModel.setNombreArchivo(nombreArchivo);
				dataSinProcesarModel.setDataArchivo(strLine);
				dataSinProcesarModel.setCausa("Esta linea no lleva un numero de telefono o codigo de cliente valido");
				dataSinProcesarModel.setEstado(1);
				dataSinProcesarModel.setCreadoPor(user);
				dataSinProcesarModel.setIdCargaMaestro(idMaestro);
				// dataSinProcesarModel.setFechaCreacion(customDate.getMySQLDate());
				this.listDataSinProcesar.add(dataSinProcesarModel);
			}

		} else {
			// System.out.println("DATO QUE SE AGREGA A SIN PROCESAR = " + strLine);
			// CBDataSinProcesarModel dataSinProcesarModel = new CBDataSinProcesarModel();
			dataSinProcesarModel.setNombreArchivo(nombreArchivo);
			dataSinProcesarModel.setDataArchivo(strLine);
			dataSinProcesarModel.setCausa("Cantidad erronea de campos consulte la configuracion de la confronta");
			dataSinProcesarModel.setEstado(1);
			dataSinProcesarModel.setCreadoPor(user);
			dataSinProcesarModel.setIdCargaMaestro(idMaestro);
			// dataSinProcesarModel.setFechaCreacion(customDate.getMySQLDate());
			this.listDataSinProcesar.add(dataSinProcesarModel);
		}
		// return bancoModel;
	}

	private void getConfrontaConfGT( int idConfronta) {
		CBConfiguracionConfrontaDaoB cccb = new CBConfiguracionConfrontaDaoB();
		// System.out.println("id lista: " + idConfronta);
		List<CBConfiguracionConfrontaModel> dataList = cccb.obtieneListaConfConfronta(idConfronta);
		int sizeList = dataList.size();
		// System.out.println("tama;o lista: " + sizeList);
		for (int countRec = 0; sizeList > countRec; countRec++) {
			CBConfiguracionConfrontaModel confrontaModel = (CBConfiguracionConfrontaModel) dataList.get(countRec);
			this.delimitador1 = confrontaModel.getDelimitador1();
			this.delimitador2 = confrontaModel.getDelimitador2();
			System.out.println("cantidad agrupacion: " + confrontaModel.getCantidadAgrupacion());
			this.cantidadAgrupacion = confrontaModel.getCantidadAgrupacion();
			// System.out.println("AGRUPACION DEL ARCHIVO: "
			// + this.cantidadAgrupacion);
			this.nomenclatura = confrontaModel.getNomenclatura();
			this.formatoFecha = confrontaModel.getFormatoFecha();

			// valores para cuando la cadena no trae delimitadores
			this.posiciones = confrontaModel.getPosiciones();
			this.tamDeCadena = confrontaModel.getLongitudCadena();
			// System.out.println("DELIMITADOR: "
			// + confrontaModel.getDelimitador1());

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
