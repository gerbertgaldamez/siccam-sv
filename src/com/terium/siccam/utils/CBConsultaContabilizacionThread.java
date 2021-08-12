package com.terium.siccam.utils;

import com.terium.siccam.composer.ControladorBase;

import com.terium.siccam.dao.CBConsultaContabilizacionDAO;

public class CBConsultaContabilizacionThread extends ControladorBase implements Runnable {

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
		System.out.println("Fecha en el hilo: " + fecha);
		System.out.println("Pais en el hilo: " + pais);		
		System.out.println("Token en el hilo: " + token);
		objDao.ejecutaSPContabilizacion(fecha, fecha, pais, token);
	}
}
