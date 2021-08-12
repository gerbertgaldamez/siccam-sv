package com.terium.siccam.model;

public class CBCausasModel {

	private String idCausaConciliacion;
	private int cbmoduloconciliacionconfid;
	public int getCbmoduloconciliacionconfid() {
		return cbmoduloconciliacionconfid;
	}

	public void setCbmoduloconciliacionconfid(int cbmoduloconciliacionconfid) {
		this.cbmoduloconciliacionconfid = cbmoduloconciliacionconfid;
	}

	private String tipoCausa;
	private String creadoPor;
	private String fechaCreacion;
	private String modificadoPor;
	private String fechaModificacion;
	private String sistema;
	private String tipo;
	private String convenio;
	private int idCausasConsiliacion;
	



	private String codigoTipologia;
	private String tipologiaasociada;
	
	public String getTipologiaasociada() {
		return tipologiaasociada;
	}

	public void setTipologiaasociada(String tipologiaasociada) {
		this.tipologiaasociada = tipologiaasociada;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getSistema() {
		return sistema;
	}
//Cambio Gerbert
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getCodigoTipologia() {
		return codigoTipologia;
	}

	public void setCodigoTipologia(String codigoTipologia) {
		this.codigoTipologia = codigoTipologia;
	}

	private String usuario;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	

	

	public String getIdCausaConciliacion() {
		return idCausaConciliacion;
	}

	public void setIdCausaConciliacion(String idCausaConciliacion) {
		this.idCausaConciliacion = idCausaConciliacion;
	}

	public String getTipoCausa() {
		return tipoCausa;
	}

	public void setTipoCausa(String tipoCausa) {
		this.tipoCausa = tipoCausa;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	public int getIdCausasConsiliacion() {
		return idCausasConsiliacion;
	}

	public void setIdCausasConsiliacion(int idCausasConsiliacion) {
		this.idCausasConsiliacion = idCausasConsiliacion;
	}

}
