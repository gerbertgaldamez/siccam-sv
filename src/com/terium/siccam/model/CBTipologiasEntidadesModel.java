package com.terium.siccam.model;

/**
 * @author CarlosGodinez -> 13/08/2018
 * */
public class CBTipologiasEntidadesModel {
	/**
	 * Consulta principal de pantalla
	 * */
	private int cbCatalogoBancoId;
	private String nombreBanco;
	private int cbCatalogoAgenciaId; 
	private String nombreAgencia;
	private int countTipologias;
	
	/**
	 * Propiedades restantes
	 * */
	private int cbTipologiasAgenciaId; 
	private int cbTipologiasPolizaId; 
	private String usuario; 
	
	public CBTipologiasEntidadesModel () {
	}

	public int getCbCatalogoBancoId() {
		return cbCatalogoBancoId;
	}

	public void setCbCatalogoBancoId(int cbCatalogoBancoId) {
		this.cbCatalogoBancoId = cbCatalogoBancoId;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public int getCbCatalogoAgenciaId() {
		return cbCatalogoAgenciaId;
	}

	public void setCbCatalogoAgenciaId(int cbCatalogoAgenciaId) {
		this.cbCatalogoAgenciaId = cbCatalogoAgenciaId;
	}

	public String getNombreAgencia() {
		return nombreAgencia;
	}

	public void setNombreAgencia(String nombreAgencia) {
		this.nombreAgencia = nombreAgencia;
	}

	public int getCountTipologias() {
		return countTipologias;
	}

	public void setCountTipologias(int countTipologias) {
		this.countTipologias = countTipologias;
	}

	public int getCbTipologiasAgenciaId() {
		return cbTipologiasAgenciaId;
	}

	public void setCbTipologiasAgenciaId(int cbTipologiasAgenciaId) {
		this.cbTipologiasAgenciaId = cbTipologiasAgenciaId;
	}

	public int getCbTipologiasPolizaId() {
		return cbTipologiasPolizaId;
	}

	public void setCbTipologiasPolizaId(int cbTipologiasPolizaId) {
		this.cbTipologiasPolizaId = cbTipologiasPolizaId;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
}
