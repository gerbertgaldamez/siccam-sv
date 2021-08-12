/**
 * 
 */
package com.terium.siccam.model;

/**
 * @author lab
 * 
 */
public class CBArchivosInsertadosModel {
	private String idArchivosInsertados;
	private String nombreArchivo;
	private String banco;
	private String agencia;
	private String fecha;
	private String creadoPor;

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

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
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
