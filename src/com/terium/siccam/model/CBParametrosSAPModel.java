package com.terium.siccam.model;

/**
 * @author Carlos Godinez
 * */
public class CBParametrosSAPModel {
	private int idContabilizacion;
	private String nombreArchivo;
	private String valorIP; 
	private String user; 
	private String pass; 
	private String rutaArchivoSAP;
	private String lineaSAP;
	//Agregados por Carlos Godinez - 07/06/2017
	private String encabezado1;
	private String encabezado2;
	private String encabezado3;
	
	//Agregado por Carlos Godinez - 09/06/2017
	private String extensionArchivo;
	
	//Agregado por Carlos Godinez - 14/09/2017
	private int idSAP;
	
	/*  */
	private int idEstadocuenta;
	
	/*Campos para formato 2 de archivo*/
	private String nombreArchivo2;
	private String lineaEncabezadoPartida;
	
	public int getIdEstadocuenta() {
		return idEstadocuenta;
	}

	public void setIdEstadocuenta(int idEstadocuenta) {
		this.idEstadocuenta = idEstadocuenta;
	}

	public CBParametrosSAPModel(){
	}
	
	public int getIdContabilizacion() {
		return idContabilizacion;
	}
	public void setIdContabilizacion(int idContabilizacion) {
		this.idContabilizacion = idContabilizacion;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getValorIP() {
		return valorIP;
	}
	public void setValorIP(String valorIP) {
		this.valorIP = valorIP;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getRutaArchivoSAP() {
		return rutaArchivoSAP;
	}
	public void setRutaArchivoSAP(String rutaArchivoSAP) {
		this.rutaArchivoSAP = rutaArchivoSAP;
	}
	public String getLineaSAP() {
		return lineaSAP;
	}
	public void setLineaSAP(String lineaSAP) {
		this.lineaSAP = lineaSAP;
	}
	public String getEncabezado1() {
		return encabezado1;
	}
	public void setEncabezado1(String encabezado1) {
		this.encabezado1 = encabezado1;
	}
	public String getEncabezado2() {
		return encabezado2;
	}
	public void setEncabezado2(String encabezado2) {
		this.encabezado2 = encabezado2;
	}
	public String getEncabezado3() {
		return encabezado3;
	}
	public void setEncabezado3(String encabezado3) {
		this.encabezado3 = encabezado3;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public int getIdSAP() {
		return idSAP;
	}

	public void setIdSAP(int idSAP) {
		this.idSAP = idSAP;
	}

	public String getNombreArchivo2() {
		return nombreArchivo2;
	}

	public void setNombreArchivo2(String nombreArchivo2) {
		this.nombreArchivo2 = nombreArchivo2;
	}

	public String getLineaEncabezadoPartida() {
		return lineaEncabezadoPartida;
	}

	public void setLineaEncabezadoPartida(String lineaEncabezadoPartida) {
		this.lineaEncabezadoPartida = lineaEncabezadoPartida;
	}
	
	
}
