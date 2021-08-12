package com.terium.siccam.model;

/**
 * @author CarlosGodinez -> 10/10/2018
 * */
public class CBManualUsuarioModel {
	private String host; 
	private String user; 
	private String pass; 
	private String rutaArchivoFTP;
	private String nombreArchivoFTP;
	
	public CBManualUsuarioModel() {
		
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
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
	public String getRutaArchivoFTP() {
		return rutaArchivoFTP;
	}
	public void setRutaArchivoFTP(String rutaArchivoFTP) {
		this.rutaArchivoFTP = rutaArchivoFTP;
	}
	public String getNombreArchivoFTP() {
		return nombreArchivoFTP;
	}
	public void setNombreArchivoFTP(String nombreArchivoFTP) {
		this.nombreArchivoFTP = nombreArchivoFTP;
	}

}
