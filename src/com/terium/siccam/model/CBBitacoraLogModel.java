package com.terium.siccam.model;

/**
 * @author CarlosGodinez
 * Auditar cualquier accion realizada en SICCAM
 * */
public class CBBitacoraLogModel {
	private String modulo;
	private String tipoCarga; 
	private String nombreArchivo;
	private String accion; 
	private String usuario;
	
	public CBBitacoraLogModel() {
		
	}
	
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public String getTipoCarga() {
		return tipoCarga;
	}
	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

}
