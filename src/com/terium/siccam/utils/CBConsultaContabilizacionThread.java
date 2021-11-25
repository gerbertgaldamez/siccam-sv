package com.terium.siccam.utils;

import org.apache.log4j.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBBitacoraLogDAO;
import com.terium.siccam.dao.CBConsultaContabilizacionDAO;

public class CBConsultaContabilizacionThread extends ControladorBase implements Runnable {
	private static Logger log = Logger.getLogger(CBConsultaContabilizacionThread.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fecha;
	private String pais;
	private String token;
	CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
	
	public CBConsultaContabilizacionThread(String fecha, String pais, String token) {
		//super(fecha);
		this.fecha = fecha;
		this.pais = pais;
		this.token = token;
	}
	
	public void run() {
		log.debug( "Fecha en el hilo: " + fecha);
		log.debug( "Pais en el hilo: " + pais);
		log.debug( "Token en el hilo: " + token);	
		
		CBConsultaContabilizacionDAO.ejecutaSPContabilizacion(fecha, fecha, pais, token);//cambio
	}
}
