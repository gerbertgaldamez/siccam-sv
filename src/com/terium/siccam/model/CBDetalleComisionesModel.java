package com.terium.siccam.model;

import java.math.BigDecimal;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBDetalleComisionesModel {

	
	
	private int cbcomisionesconfrontaid;
	private int bancoagenciaconfrontaid;
	private int bancoagenciaafiliacionesid;
	private int impuestoid;
	private int tipo;
	private int medioPago;
	private int tipologias;
	private int formaPago;
	
	private String creadoPor;
	private String fechaCreacion;
	
	private String modificadoPor;
	private String fechaModificado;

	
	private String NombreTipologia;
	private String nombreImpuesto;
	private String nombreTipo;
	private String nombreMedioPago;
	private String nombreFormaPago;
	private String valor;
	private BigDecimal comisionReal;
	private int cbcomisionid;
	
	public BigDecimal getComisionReal() {
		return comisionReal;
	}
	public void setComisionReal(BigDecimal comisionReal) {
		this.comisionReal = comisionReal;
	}
	private int comisionUso;
	private BigDecimal monto;
	
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public int getBancoagenciaafiliacionesid() {
		return bancoagenciaafiliacionesid;
	}
	public void setBancoagenciaafiliacionesid(int bancoagenciaafiliacionesid) {
		this.bancoagenciaafiliacionesid = bancoagenciaafiliacionesid;
	}
	public int getComisionUso() {
		return comisionUso;
	}
	public void setComisionUso(int comisionUso) {
		this.comisionUso = comisionUso;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public int getBancoagenciaconfrontaid() {
		return bancoagenciaconfrontaid;
	}
	public void setBancoagenciaconfrontaid(int bancoagenciaconfrontaid) {
		this.bancoagenciaconfrontaid = bancoagenciaconfrontaid;
	}
	public String getNombreImpuesto() {
		return nombreImpuesto;
	}
	public void setNombreImpuesto(String nombreImpuesto) {
		this.nombreImpuesto = nombreImpuesto;
	}
	public String getNombreTipo() {
		return nombreTipo;
	}
	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}
	public String getNombreMedioPago() {
		return nombreMedioPago;
	}
	public void setNombreMedioPago(String nombreMedioPago) {
		this.nombreMedioPago = nombreMedioPago;
	}
	public String getNombreFormaPago() {
		return nombreFormaPago;
	}
	public void setNombreFormaPago(String nombreFormaPago) {
		this.nombreFormaPago = nombreFormaPago;
	}
	public int getCbcomisionesconfrontaid() {
		return cbcomisionesconfrontaid;
	}
	public void setCbcomisionesconfrontaid(int cbcomisionesconfrontaid) {
		this.cbcomisionesconfrontaid = cbcomisionesconfrontaid;
	}

	public int getImpuestoid() {
		return impuestoid;
	}
	public void setImpuestoid(int impuestoid) {
		this.impuestoid = impuestoid;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getMedioPago() {
		return medioPago;
	}
	public void setMedioPago(int medioPago) {
		this.medioPago = medioPago;
	}
	public int getTipologias() {
		return tipologias;
	}
	public void setTipologias(int tipologias) {
		this.tipologias = tipologias;
	}
	public int getFormaPago() {
		return formaPago;
	}
	public void setFormaPago(int formaPago) {
		this.formaPago = formaPago;
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
	public String getFechaModificado() {
		return fechaModificado;
	}
	public void setFechaModificado(String fechaModificado) {
		this.fechaModificado = fechaModificado;
	}
	
	public String getNombreTipologia() {
		return NombreTipologia;
	}
	public void setNombreTipologia(String nombreTipologia) {
		NombreTipologia = nombreTipologia;
	}
	public int getCbcomisionid() {
		return cbcomisionid;
	}
	public void setCbcomisionid(int cbcomisionid) {
		this.cbcomisionid = cbcomisionid;
	}
		
		
		
		
	    
	

}