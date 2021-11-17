package com.terium.siccam.model;

import java.math.BigDecimal;

public class CBHistorialSCECModel {
	
	private int cbhistorialscecid;
	private int cbestadocuentasociedadid;
	private int cbpagosid;
	private int cbbancoagenciaconfrontaid;
	private int cbcausasconciliacionid;
	private int estado;
	private BigDecimal monto;
	private String fecha;
	private String observacion;
	private String creadopor;
	private String modificadopor;
	private String fechacreacion;
	private String fechamodificacion;
	private BigDecimal comisionReal;
	private int cbcomisionid;
	
	//Causas conciliacion
	private int tipo;
	private String causas;
	public String getNombretipo() {
		return nombretipo;
	}

	public void setNombretipo(String nombretipo) {
		this.nombretipo = nombretipo;
	}

	private String nombretipo;
	private String codigoconciliacion;
	
	
	public CBHistorialSCECModel() {}
	
	public int getCbhistorialscecid() {
		return cbhistorialscecid;
	}
	public void setCbhistorialscecid(int cbhistorialscecid) {
		this.cbhistorialscecid = cbhistorialscecid;
	}
	public int getCbestadocuentasociedadid() {
		return cbestadocuentasociedadid;
	}
	public void setCbestadocuentasociedadid(int cbestadocuentasociedadid) {
		this.cbestadocuentasociedadid = cbestadocuentasociedadid;
	}
	public int getCbpagosid() {
		return cbpagosid;
	}
	public void setCbpagosid(int cbpagosid) {
		this.cbpagosid = cbpagosid;
	}
	public int getCbbancoagenciaconfrontaid() {
		return cbbancoagenciaconfrontaid;
	}
	public void setCbbancoagenciaconfrontaid(int cbbancoagenciaconfrontaid) {
		this.cbbancoagenciaconfrontaid = cbbancoagenciaconfrontaid;
	}
	public int getCbcausasconciliacionid() {
		return cbcausasconciliacionid;
	}
	public void setCbcausasconciliacionid(int cbcausasconciliacionid) {
		this.cbcausasconciliacionid = cbcausasconciliacionid;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public BigDecimal getMonto() {
		return monto;
	}
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public String getCreadopor() {
		return creadopor;
	}
	public void setCreadopor(String creadopor) {
		this.creadopor = creadopor;
	}
	public String getModificadopor() {
		return modificadopor;
	}
	public void setModificadopor(String modificadopor) {
		this.modificadopor = modificadopor;
	}
	public String getFechacreacion() {
		return fechacreacion;
	}
	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}
	public String getFechamodificacion() {
		return fechamodificacion;
	}
	public void setFechamodificacion(String fechamodificacion) {
		this.fechamodificacion = fechamodificacion;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getCausas() {
		return causas;
	}

	public void setCausas(String causas) {
		this.causas = causas;
	}

	public String getCodigoconciliacion() {
		return codigoconciliacion;
	}

	public void setCodigoconciliacion(String codigoconciliacion) {
		this.codigoconciliacion = codigoconciliacion;
	}

	public BigDecimal getComisionReal() {
		return comisionReal;
	}

	public void setComisionReal(BigDecimal comisionReal) {
		this.comisionReal = comisionReal;
	}

	public int getCbcomisionid() {
		return cbcomisionid;
	}

	public void setCbcomisionid(int cbcomisionid) {
		this.cbcomisionid = cbcomisionid;
	}
	

}
