/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Ramiro Antonio Sian Buenafe
 */
public class CustomDate {

	public static String getCDate() {
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM.dd.yyyy");
		return dateFormatter.format(now);
	}

	public static String getMySQLDate() {
		Date now = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateFormatter.format(now);
	}

	/**
	 * Este metodo trata de formatear una fecha dada utilizando la configuracion
	 * en la base de datos
	 * 
	 * @param dateWithoutFormat
	 * @param confFormat
	 * @return
	 */
	public String getFormatDate(String dateWithoutFormat, String confFormat) {
		SimpleDateFormat sdfG = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return tryCasesV2(dateWithoutFormat, sdfG, confFormat);
	}
	
	public String getFormatFecha(String dateWithoutFormat, String confFormat) {
		SimpleDateFormat sdfG = new SimpleDateFormat("dd/MM/yyyy");
		return tryCasesV2(dateWithoutFormat, sdfG, confFormat);
	}

	private String tryCasesV2(String dateWithoutFormat, SimpleDateFormat sdfG,
			String confFormat) {
		String strResp = "";
		Date convertedDate = null;
		convertedDate = tryFormat(dateWithoutFormat, confFormat);
		if (convertedDate != null) {
			strResp = sdfG.format(convertedDate);
			if ("".equals(strResp)) {
				tryCases(dateWithoutFormat, sdfG);
			}
		}
		return strResp;
	}

	@SuppressWarnings("unused")
	private String pruebaCasesV2(String dateWithoutFormat,
			SimpleDateFormat sdfG, String confFormat) {
		String strResp = "";
		Date convertedDate = null;
		convertedDate = pruebaFormat(dateWithoutFormat, confFormat);
		if (convertedDate != null) {
			strResp = sdfG.format(convertedDate);
			if ("".equals(strResp)) {
				pruebaCases(dateWithoutFormat, sdfG);
			}
		}
		return strResp;
	}

	private String tryCases(String dateWithoutFormat, SimpleDateFormat sdfG) {
		String strResp = "";
		Date convertedDate = null;
		Iterator<String> itCases = casesToTry().iterator();
		while (itCases.hasNext()) {
			convertedDate = tryFormat(dateWithoutFormat, itCases.next());
			if (convertedDate != null) {
				strResp = sdfG.format(convertedDate);
				break;
			}
		}
		return strResp;
	}

	private String pruebaCases(String dateWithoutFormat, SimpleDateFormat sdfG) {
		String strResp = "";
		Date convertedDate = null;
		Iterator<String> itCases = casesToTry().iterator();
		while (itCases.hasNext()) {
			convertedDate = pruebaFormat(dateWithoutFormat, itCases.next());
			if (convertedDate != null) {
				strResp = sdfG.format(convertedDate);
				break;
			}
		}
		return strResp;
	}

	@SuppressWarnings("unchecked")
	private static List<String> casesToTry() {
		@SuppressWarnings("rawtypes")
		List listC = new ArrayList();
		listC.add("yyyyMMdd HHmmss");
		listC.add("dd/MM/yyyy");
		listC.add("dd/MM/yyyy HH:mm:ss");
		listC.add("yyyyMMdd");
		listC.add("yyyyMMddHHmmss");
		listC.add("yyyy-MM-dd");
		listC.add("yyyy-MM-dd HH:mm:ss");
		listC.add("yyMMdd");
		listC.add("ddMMyy");
		return listC;
	}

	private Date tryFormat(String dateWithoutFormat, String formatUse) {
		DateFormat formatter = null;
		Date convertedDate = null;
		try {
			formatter = new SimpleDateFormat(formatUse);
			convertedDate = (Date) formatter.parse(dateWithoutFormat.trim());
		} catch (ParseException ex) {
			Logger.getLogger(CustomDate.class.getName()).log(Level.INFO, null,
					ex.getMessage());
			return null;
		}
		return convertedDate;
	}

	private Date pruebaFormat(String dateWithoutFormat, String formatUse) {
		DateFormat formatter = null;
		Date convertedDate = null;
		try {
			formatter = new SimpleDateFormat(formatUse);
			convertedDate = (Date) formatter.parse(dateWithoutFormat.trim());
		} catch (ParseException ex) {
			Logger.getLogger(CustomDate.class.getName()).log(Level.INFO, null,
					ex.getMessage());
			return null;
		}
		return convertedDate;
	}

	public Date getParserDate(String strDate) {
		DateFormat formatter = null;
		Date convertedDate = null;
		try {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			convertedDate = (Date) formatter.parse(strDate.trim());
		} catch (ParseException ex) {
			Logger.getLogger(CustomDate.class.getName()).log(Level.INFO, null,
					ex.getMessage());
			return null;
		}
		return convertedDate;
	}
	
	/**
	 * Agregado por Carlos Godinez -> 01/11/2017
	 * 
	 * Se verifica que el formato de fecha en la configuracion de confrontas sea valido
	 * */
	public static boolean isDate(String formatoEnviado){
		System.out.println("======== ENTRA A PARSEO DE FECHA ENVIADA ========");
		System.out.println("Formato fecha enviado = " + formatoEnviado);
		try {
			String fechaStr = null;
			DateFormat dateFormat = new SimpleDateFormat(formatoEnviado);
			Date fechaActual = new Date();
			fechaStr = dateFormat.format(fechaActual);
			System.out.println("Fecha parseada con exito = " + fechaStr);
			System.out.println("======== FIN DE PARSEO DE FECHA =========");
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("Formato de fecha invalido = : " + formatoEnviado);
			System.out.println("======== FIN DE PARSEO DE FECHA =========");
			return false;
		} 
	}
}
