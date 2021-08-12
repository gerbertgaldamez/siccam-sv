package com.terium.siccam.model;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBDepositosRecModel {


	private int cbdepositosid;
	private String texto;
	private int tipologia;
	private int tipofecha;
	private int entidad;
	private String NombreEntidad;
	private String NombreTipologia;
	private String nombreTipoFecha;
	public String getNombreTipoFecha() {
		return nombreTipoFecha;
	}
	public void setNombreTipoFecha(String nombreTipoFecha) {
		this.nombreTipoFecha = nombreTipoFecha;
	}
	public String getNombreTipologia() {
		return NombreTipologia;
	}
	public void setNombreTipologia(String nombreTipologia) {
		NombreTipologia = nombreTipologia;
	}
	public String getNombreEntidad() {
		return NombreEntidad;
	}
	public void setNombreEntidad(String nombreEntidad) {
		NombreEntidad = nombreEntidad;
	}
	private String creadoPor;
	private String modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	public int getCbdepositosid() {
		return cbdepositosid;
	}
	public void setCbdepositosid(int cbdepositosid) {
		this.cbdepositosid = cbdepositosid;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getTipologia() {
		return tipologia;
	}
	public void setTipologia(int tipologia) {
		this.tipologia = tipologia;
	}
	public int getTipofecha() {
		return tipofecha;
	}
	public void setTipofecha(int tipofecha) {
		this.tipofecha = tipofecha;
	}
	public int getEntidad() {
		return entidad;
	}
	public void setEntidad(int entidad) {
		this.entidad = entidad;
	}
	public String getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}
	public String getModificadoPor() {
		return modificadoPor;
	}
	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}