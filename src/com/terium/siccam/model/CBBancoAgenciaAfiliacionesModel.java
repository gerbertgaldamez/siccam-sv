package com.terium.siccam.model;

public class CBBancoAgenciaAfiliacionesModel {
	private int cbbancoagenciaafiliacionesid;
	
	private int cbcatalogoagenciaid;
	private String tipo;
	private String afiliacion;
	private int estado;
	private String creador;
	
	private String fechaCreacion;
	
private String usuario;
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public CBBancoAgenciaAfiliacionesModel(){
	}
	
	public CBBancoAgenciaAfiliacionesModel(int cbcatalogoagenciaid, String tipo, String afiliacion, int estado, String creador){
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
		this.tipo = tipo;
		this.afiliacion = afiliacion;
		this.estado = estado;
		this.creador = creador;
	}

	public int getCbbancoagenciaafiliacionesid() {
		return cbbancoagenciaafiliacionesid;
	}

	public void setCbbancoagenciaafiliacionesid(int cbbancoagenciaafiliacionesid) {
		this.cbbancoagenciaafiliacionesid = cbbancoagenciaafiliacionesid;
	}

	public int getCbcatalogoagenciaid() {
		return cbcatalogoagenciaid;
	}

	public void setCbcatalogoagenciaid(int cbcatalogoagenciaid) {
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getAfiliacion() {
		return afiliacion;
	}

	public void setAfiliacion(String afiliacion) {
		this.afiliacion = afiliacion;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
