package com.terium.siccam.model;

import java.math.BigDecimal;

public class CBConciliacionBancoModel {

	private int idcombo;
	private String fecha;
	private String nombre;
	private int cbcatalogoagenciaid;

	private BigDecimal estadopostpago;
	private BigDecimal comisionpostpago;
	private BigDecimal confrontapostpago;
	private BigDecimal difpostpago;
	private BigDecimal pagosdeldia;
	private BigDecimal pagosotrosdias;
	private BigDecimal pagosotrosmeses;
	private BigDecimal reversasotrosdias;
	private BigDecimal reversasotrosmeses;
	private BigDecimal totaldia;
	private BigDecimal totalgeneral;
	private BigDecimal estadocuenta;
	private BigDecimal diferenciatotal;
	private BigDecimal porcentajepostpago;
	private BigDecimal comisionconfrontapostpago;
	private BigDecimal recafinalpost;
	private BigDecimal totalfinal;
	private BigDecimal comisiontotal;
	private BigDecimal diferenciacomisionpospago;
	private int cbbancoagenciaconfrontaid;
	
	//CarlosGodinez -> 07/08/2018
	
	private String codigoColector; 
	private String fechaInicioFiltro;
	private String fechaFinFiltro;
	private BigDecimal conciliadomanual;
	
	//FIN CarlosGodinez -> 07/08/2018
	private String formapago;
	
	public String getFormapago() {
		return formapago;
	}
	public void setFormapago(String formapago) {
		this.formapago = formapago;
	}
	public BigDecimal getConciliadomanual() {
		return conciliadomanual;
	}
	public void setConciliadomanual(BigDecimal conciliadomanual) {
		this.conciliadomanual = conciliadomanual;
	}
	public int getIdcombo() {
		return idcombo;
	}
	public void setIdcombo(int idcombo) {
		this.idcombo = idcombo;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCbcatalogoagenciaid() {
		return cbcatalogoagenciaid;
	}
	public void setCbcatalogoagenciaid(int cbcatalogoagenciaid) {
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
	}

	public BigDecimal getEstadopostpago() {
		return estadopostpago;
	}
	public void setEstadopostpago(BigDecimal estadopostpago) {
		this.estadopostpago = estadopostpago;
	}
	public BigDecimal getConfrontapostpago() {
		return confrontapostpago;
	}
	public void setConfrontapostpago(BigDecimal confrontapostpago) {
		this.confrontapostpago = confrontapostpago;
	}
	public BigDecimal getDifpostpago() {
		return difpostpago;
	}
	public void setDifpostpago(BigDecimal difpostpago) {
		this.difpostpago = difpostpago;
	}
	public BigDecimal getPagosdeldia() {
		return pagosdeldia;
	}
	public void setPagosdeldia(BigDecimal pagosdeldia) {
		this.pagosdeldia = pagosdeldia;
	}
	public BigDecimal getPagosotrosdias() {
		return pagosotrosdias;
	}
	public void setPagosotrosdias(BigDecimal pagosotrosdias) {
		this.pagosotrosdias = pagosotrosdias;
	}
	public BigDecimal getPagosotrosmeses() {
		return pagosotrosmeses;
	}
	public void setPagosotrosmeses(BigDecimal pagosotrosmeses) {
		this.pagosotrosmeses = pagosotrosmeses;
	}
	public BigDecimal getReversasotrosdias() {
		return reversasotrosdias;
	}
	public void setReversasotrosdias(BigDecimal reversasotrosdias) {
		this.reversasotrosdias = reversasotrosdias;
	}
	public BigDecimal getReversasotrosmeses() {
		return reversasotrosmeses;
	}
	public void setReversasotrosmeses(BigDecimal reversasotrosmeses) {
		this.reversasotrosmeses = reversasotrosmeses;
	}
	public BigDecimal getTotaldia() {
		return totaldia;
	}
	public void setTotaldia(BigDecimal totaldia) {
		this.totaldia = totaldia;
	}
	public BigDecimal getTotalgeneral() {
		return totalgeneral;
	}
	public void setTotalgeneral(BigDecimal totalgeneral) {
		this.totalgeneral = totalgeneral;
	}
	public BigDecimal getEstadocuenta() {
		return estadocuenta;
	}
	public void setEstadocuenta(BigDecimal estadocuenta) {
		this.estadocuenta = estadocuenta;
	}
	public BigDecimal getDiferenciatotal() {
		return diferenciatotal;
	}
	public void setDiferenciatotal(BigDecimal diferenciatotal) {
		this.diferenciatotal = diferenciatotal;
	}
	public BigDecimal getPorcentajepostpago() {
		return porcentajepostpago;
	}
	public void setPorcentajepostpago(BigDecimal porcentajepostpago) {
		this.porcentajepostpago = porcentajepostpago;
	}
	public BigDecimal getComisionpostpago() {
		return comisionpostpago;
	}
	public void setComisionpostpago(BigDecimal comisionpostpago) {
		this.comisionpostpago = comisionpostpago;
	}

	public BigDecimal getRecafinalpost() {
		return recafinalpost;
	}
	public void setRecafinalpost(BigDecimal recafinalpost) {
		this.recafinalpost = recafinalpost;
	}

	public BigDecimal getTotalfinal() {
		return totalfinal;
	}
	public void setTotalfinal(BigDecimal totalfinal) {
		this.totalfinal = totalfinal;
	}
	public BigDecimal getComisiontotal() {
		return comisiontotal;
	}
	public void setComisiontotal(BigDecimal comisiontotal) {
		this.comisiontotal = comisiontotal;
	}

	public BigDecimal getComisionconfrontapostpago() {
		return comisionconfrontapostpago;
	}
	public void setComisionconfrontapostpago(BigDecimal comisionconfrontapostpago) {
		this.comisionconfrontapostpago = comisionconfrontapostpago;
	}

	public BigDecimal getDiferenciacomisionpospago() {
		return diferenciacomisionpospago;
	}
	public void setDiferenciacomisionpospago(BigDecimal diferenciacomisionpospago) {
		this.diferenciacomisionpospago = diferenciacomisionpospago;
	}

	public int getCbbancoagenciaconfrontaid() {
		return cbbancoagenciaconfrontaid;
	}
	public void setCbbancoagenciaconfrontaid(int cbbancoagenciaconfrontaid) {
		this.cbbancoagenciaconfrontaid = cbbancoagenciaconfrontaid;
	}
	
	//CarlosGodinez -> 07/08/2018
	
	public String getCodigoColector() {
		return codigoColector;
	}
	public void setCodigoColector(String codigoColector) {
		this.codigoColector = codigoColector;
	}
	public String getFechaInicioFiltro() {
		return fechaInicioFiltro;
	}
	public void setFechaInicioFiltro(String fechaInicioFiltro) {
		this.fechaInicioFiltro = fechaInicioFiltro;
	}
	public String getFechaFinFiltro() {
		return fechaFinFiltro;
	}
	public void setFechaFinFiltro(String fechaFinFiltro) {
		this.fechaFinFiltro = fechaFinFiltro;
	}
	
	//FIN CarlosGodinez -> 07/08/2018
}
