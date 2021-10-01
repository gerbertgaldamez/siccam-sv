package com.terium.siccam.model;

public class CBParametrosGeneralesModel {
	
	private int cbmoduloconciliacionconfid;
	private String modulo;
	private String tipoObjeto;
	private String objeto;
	private String valorObjeto1;
	private String valorObjeto2;
	private String valorObjeto3;
	private String descripcion;
	private String estado;
	private String creador;
	private int idCausaConciliacion;
	private String convenio;
	
	private String codAgencia;
	private String cbbancoagenciaconfrontaid;
	
	
	public String getCbbancoagenciaconfrontaid() {
		return cbbancoagenciaconfrontaid;
	}

	public void setCbbancoagenciaconfrontaid(String cbbancoagenciaconfrontaid) {
		this.cbbancoagenciaconfrontaid = cbbancoagenciaconfrontaid;
	}

	public String getcodAgencia() {
		return codAgencia;
	}

	public void setcodAgencia(String codAgencia) {
		this.codAgencia = codAgencia;
	}

	public CBParametrosGeneralesModel(){
	}

	public int getCbmoduloconciliacionconfid() {
		return cbmoduloconciliacionconfid;
	}

	public void setCbmoduloconciliacionconfid(int cbmoduloconciliacionconfid) {
		this.cbmoduloconciliacionconfid = cbmoduloconciliacionconfid;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public String getTipoObjeto() {
		return tipoObjeto;
	}

	public void setTipoObjeto(String tipoObjeto) {
		this.tipoObjeto = tipoObjeto;
	}

	public String getObjeto() {
		return objeto;
	}

	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	public String getValorObjeto1() {
		return valorObjeto1;
	}

	public void setValorObjeto1(String valorObjeto1) {
		this.valorObjeto1 = valorObjeto1;
	}

	public String getValorObjeto2() {
		return valorObjeto2;
	}

	public void setValorObjeto2(String valorObjeto2) {
		this.valorObjeto2 = valorObjeto2;
	}

	public String getValorObjeto3() {
		return valorObjeto3;
	}

	public void setValorObjeto3(String valorObjeto3) {
		this.valorObjeto3 = valorObjeto3;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}
	public int getIdCausaConciliacion() {
		return idCausaConciliacion;
	}

	public void setIdCausaConciliacion(int idCausaConciliacion) {
		this.idCausaConciliacion = idCausaConciliacion;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

}
