/**
 * 
 */
package com.terium.siccam.model;

/**
 * @author lab
 * 
 */
public class CBArchivosInsertadosEstadoCuentaModel {
	private String idArchivosInsertados;
	private String nombreArchivo;
	private String creadoPor;
	private String fecha;
	private String tipoCarga;

	public String getTipoCarga() {
		return tipoCarga;
	}

	public void setTipoCarga(String tipoCarga) {
		this.tipoCarga = tipoCarga;
	}

	public String getIdArchivosInsertados() {
		return idArchivosInsertados;
	}

	public void setIdArchivosInsertados(String idArchivosInsertados) {
		this.idArchivosInsertados = idArchivosInsertados;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	
	

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

}
