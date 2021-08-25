package com.terium.siccam.utils;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.lang.model.type.*;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Combobox;

import com.terium.siccam.dao.DetalleLogDAO;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.DetalleLogModel;

/**
 * @author Juankrlos by 29/11/2017
 */
public class Tools {
	
	/**
	 * Constantes
	 * */
	
	public static final String SESSION_SV = "SV";
	public static final String SESSION_GT = "GT";
	public static final String SESSION_CR = "CR";
	public static final String SESSION_PA = "PA";
	public static final String SESSION_NI = "NI";
	public static final String REDIRECT_MENU = "menu.zul";
	public static final String REDIRECT_INICIO = "inicio.zul";
	public static final String REDIRECT_INDEX = "index.zul";
	public static final String CONEXION_SV = "jdbc/corrmas";
	public static final String CONEXION_GT = "jdbc/sclgtepr";
	public static final String CONEXION_CR = "jdbc/corrmas";
	public static final String CONEXION_PA = "jdbc/corrmas";
	public static final String CONEXION_NI = "jdbc/corrmas";
	
	public static final int CODE_SV = 503;
	public static final int CODE_GT = 502;
	public static final int CODE_CR = 506;
	public static final int CODE_PA = 507;
	public static final int CODE_NI = 505;

	/**
	 * Herramienta para retornar el numero de dias en un rango de fechas dadas.
	 * 
	 * @param fechaInicial
	 * @param fechaFinal
	 */
	public static int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
		String fechaInicioString = df.format(fechaInicial);
		try {
			fechaInicial = df.parse(fechaInicioString);
		} catch (ParseException ex) {
			Logger.getLogger(Tools.class.getName()).log(Level.INFO, null,"error en pareseo de fecha" + ex.getMessage());
		}
		String fechaFinalString = df.format(fechaFinal);
		try {
			fechaFinal = df.parse(fechaFinalString);
		} catch (ParseException ex) {
			Logger.getLogger(Tools.class.getName()).log(Level.INFO, null, ex.getMessage());
		}
		long fechaInicialMs = fechaInicial.getTime();
		long fechaFinalMs = fechaFinal.getTime();
		long diferencia = fechaFinalMs - fechaInicialMs;
		double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
		return ((int) dias);
	}

	/**
	 * 
	 * */
	public static boolean isValidaCombo(Combobox param) {
		boolean result = false;
		if (param.getSelectedItem() != null && !param.getSelectedItem().
				getValue().toString().trim().equals("")) {
			Logger.getLogger(Tools.class.getName()).log(Level.INFO, null,
					"El combo trae el valor de: "+param.getSelectedItem().getValue());
			result = true;
		}
		return result;
	}
	
	/**
	 *
	 **/
	public static void ingresaLog(SQLException e, String clase, String descripcion, String objeto) {
		DetalleLogDAO objDao = new DetalleLogDAO();
		DetalleLogModel obj = new DetalleLogModel();
		obj.setCodigoerror(e.getErrorCode());
		obj.setMensajeerror(e.getMessage());
		obj.setModulo(clase);
		obj.setDescripcion(descripcion);
		obj.setUsuario("SICCAM-ERROR-LOG");
		obj.setObjeto(objeto);
		
		Logger.getLogger(Tools.class.getName()).log(Level.INFO, null,
				"Se ingresa log de error: "+objDao.insertErrorLog(obj));
	}
	
	/* Permite agregar una cookie */
	public static void setCookie(String name, String value) {
		/*((HttpServletResponse) Executions.getCurrent().getNativeResponse()).addCookie(new Cookie(
				name, value));*/
		
		HttpServletResponse response = (HttpServletResponse)Executions.getCurrent().getNativeResponse();
        Cookie userCookie = new Cookie(name, value);
        //userCookie.setMaxAge(60*2);
        response.addCookie(userCookie);
	}

	/* Permite obtener una cookie por medio del nombre */
	public static String getCookie(String name) {
		System.out.println("GetCookie");
		
	//	java.lang.Object getNativeRequest()
		
		try {

		//	HttpServletRequest hsr = (HttpServletRequest) Executions.getCurrent();  //   .getNativeRequest();
		//	if (hsr != null) {
			
			//		HttpServletRequest hsr = (HttpServletRequest)execution.getNativeRequest();			
			
			Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent().getNativeRequest())
					.getCookies();
			System.out.println("es la validacion");
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(name)) {
						System.out.println(cookie.getValue());
						return cookie.getValue();
					}
				}
			} 
			//}
			
		//	HttpServletResponse response = (HttpServletResponse)Executions.getCurrent().getNativeResponse();
	    //    Cookie userCookie = new Cookie(name, "1000");
			
			}
			catch ( Exception e ) {	
				Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, "error", e);
		  //      Cookie userCookie = new Cookie(name, "1000");
		        //userCookie.setMaxAge(60*2);
		   //     response.addCookie(userCookie);
			//	HttpServletResponse response = (HttpServletResponse)Executions.getCurrent().getNativeResponse();
		     //   Cookie userCookie = new Cookie(name, "1000");
				return "";
			}
		return "";
	}
	
	/* Borrar cookie */
	public static void eraseCookie(String name) {
		Cookie[] cookies = ((HttpServletRequest) Executions.getCurrent().getNativeRequest())
				.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					cookie.setValue("");
					cookie.setMaxAge(0);
				}
			}
		}
	}
	
	/* 
	 * Permite sumar dias a una fecha
	 *  
	 *  
	 *  */
	public static Date sumarDias(Date fecha, int dias){
	      if (dias==0) return fecha;
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      calendar.add(Calendar.DAY_OF_YEAR, dias);  
	      return calendar.getTime(); 
	}
	
	//Obtener parametros para WS
	public static String obtenerParametro(String parametro, List<CBParametrosGeneralesModel> parametros) {

		if (parametros != null && parametros.size() > 0) {
			for (CBParametrosGeneralesModel item : parametros) {
				if (item.getObjeto().equals(parametro)) {
					System.out.println("OBJETO => "+item.getObjeto()+" - VALOR OBJETO1 => "+item.getValorObjeto1());
					return item.getValorObjeto1();
				}
			}
		} else {
			return "";
		}

		return "";
	}

}
