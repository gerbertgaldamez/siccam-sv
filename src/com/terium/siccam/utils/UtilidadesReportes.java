package com.terium.siccam.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilidadesReportes {
	public static int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechaInicioString = df.format(fechaInicial);
		try {
			fechaInicial = df.parse(fechaInicioString);
		} catch (ParseException ex) {
		}
		String fechaFinalString = df.format(fechaFinal);
		try {
			fechaFinal = df.parse(fechaFinalString);
		} catch (ParseException ex) {
		}
		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ((int) dias);
	}
	
	/**
	 * @author Juankrlos
	 * */
	public static String changeNull(String cadena){
			if(cadena == null){
				return " ";
			}else{
				return cadena;
			}
	}
	
	public static String changeNumber(String cadena){
		String result = "0.00";
		try{
			//result = String.format("%3.2f", cadena).replace(".00","");		
			//System.out.println("cadena: "+cadena);
			int resultado = cadena.indexOf(".");
	        //System.out.println("resultado: "+resultado);
	        if(resultado != -1) { 	
	        	result = cadena;
	        }else{
	        	result = cadena+".00";
	        }
		}catch(NumberFormatException e){
			Logger.getLogger(UtilidadesReportes.class.getName()).log(Level.SEVERE, null, e);
			System.out.println("error: "+e.getMessage());
			result = "0.00";
		} 
		return result;
	}
}
