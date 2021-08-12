package com.terium.siccam.model;

public class DetalleLogModel {
	
	private int cberrorlogid;
	private int codigoerror;
	private String mensajeerror;
	private String usuario;
	private String fecha;
	private String modulo;
	private String descripcion;
	private String objeto;
	
	
	public int getCberrorlogid() {
		return cberrorlogid;
	}
	public void setCberrorlogid(int cberrorlogid) {
		this.cberrorlogid = cberrorlogid;
	}
	public int getCodigoerror() {
		return codigoerror;
	}
	public void setCodigoerror(int codigoerror) {
		this.codigoerror = codigoerror;
	}
	public String getMensajeerror() {
		return mensajeerror;
	}
	public void setMensajeerror(String mensajeerror) {
		this.mensajeerror = mensajeerror;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getModulo() {
		return modulo;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObjeto() {
		return objeto;
	}
	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

}
