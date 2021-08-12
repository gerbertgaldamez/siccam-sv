package com.terium.siccam.model;

import java.math.BigDecimal;
import java.util.Date;

public class CBAplicaDesaplicaModel {
	
	String agencia;
	Date dia;
	String tipo;
	String cliente;
	String nombre;
	String desPago;
	String transTelca;
	String telefono;
	String transBanco;
	BigDecimal impPago; // Pago Telefonica
	BigDecimal monto; // Pago Banco
	BigDecimal manual;
	String pendiente;
	String pendienteT;
	String pendienteB;
	String estado;
	String conciliacionId;
	BigDecimal pendienteBanco;
	BigDecimal pendienteTelefonica;
	BigDecimal comision;
	BigDecimal monto_comision;
	String sucursal;
	String nombre_sucursal;
	String tipo_sucursal;
	String cbcatalogoagenciaid;
	private String lineaSAP;
	private int idSAP;
	
	public int getIdSAP() {
		return idSAP;
	}

	public void setIdSAP(int idSAP) {
		this.idSAP = idSAP;
	}

	public String getLineaSAP() {
		return lineaSAP;
	}

	public void setLineaSAP(String lineaSAP) {
		this.lineaSAP = lineaSAP;
	}

	public String getCbcatalogoagenciaid() {
		return cbcatalogoagenciaid;
	}

	public void setCbcatalogoagenciaid(String cbcatalogoagenciaid) {
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
	}

	public CBAplicaDesaplicaModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CBAplicaDesaplicaModel(String agencia, Date dia, String tipo,
			String cliente, String nombre, String desPago, String transTelca,
			String telefono, String transBanco, BigDecimal impPago,
			BigDecimal monto, BigDecimal manual, String pendiente,
			String pendienteT, String pendienteB, String estado,
			String conciliacionId, BigDecimal pendienteBanco,
			BigDecimal pendienteTelefonica, BigDecimal comision,
			BigDecimal monto_comision, String sucursal, String nombre_sucursal,
			String tipo_sucursal) {
		super();
		this.agencia = agencia;
		this.dia = dia;
		this.tipo = tipo;
		this.cliente = cliente;
		this.nombre = nombre;
		this.desPago = desPago;
		this.transTelca = transTelca;
		this.telefono = telefono;
		this.transBanco = transBanco;
		this.impPago = impPago;
		this.monto = monto;
		this.manual = manual;
		this.pendiente = pendiente;
		this.pendienteT = pendienteT;
		this.pendienteB = pendienteB;
		this.estado = estado;
		this.conciliacionId = conciliacionId;
		this.pendienteBanco = pendienteBanco;
		this.pendienteTelefonica = pendienteTelefonica;
		this.comision = comision;
		this.sucursal = sucursal;
		this.nombre_sucursal = nombre_sucursal;
		this.tipo_sucursal = tipo_sucursal;
		this.monto_comision = monto_comision;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDesPago() {
		return desPago;
	}

	public void setDesPago(String desPago) {
		this.desPago = desPago;
	}

	public String getTransTelca() {
		return transTelca;
	}

	public void setTransTelca(String transTelca) {
		this.transTelca = transTelca;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTransBanco() {
		return transBanco;
	}

	public void setTransBanco(String transBanco) {
		this.transBanco = transBanco;
	}

	public BigDecimal getImpPago() {
		return impPago;
	}

	public void setImpPago(BigDecimal impPago) {
		this.impPago = impPago;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public BigDecimal getManual() {
		return manual;
	}

	public void setManual(BigDecimal manual) {
		this.manual = manual;
	}

	public String getPendiente() {
		return pendiente;
	}

	public String getPendienteT() {
		return pendienteT;
	}

	public void setPendienteT(String pendienteT) {
		this.pendienteT = pendienteT;
	}

	public String getPendienteB() {
		return pendienteB;
	}

	public void setPendienteB(String pendienteB) {
		this.pendienteB = pendienteB;
	}

	public void setPendiente(String pendiente) {
		this.pendiente = pendiente;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getConciliacionId() {
		return conciliacionId;
	}

	public void setConciliacionId(String conciliacionId) {
		this.conciliacionId = conciliacionId;
	}

	public BigDecimal getPendienteBanco() {
		return pendienteBanco;
	}

	public void setPendienteBanco(BigDecimal pendienteBanco) {
		this.pendienteBanco = pendienteBanco;
	}

	public BigDecimal getPendienteTelefonica() {
		return pendienteTelefonica;
	}

	public void setPendienteTelefonica(BigDecimal pendienteTelefonica) {
		this.pendienteTelefonica = pendienteTelefonica;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}

	public BigDecimal getMonto_comision() {
		return monto_comision;
	}

	public void setMonto_comision(BigDecimal monto_comision) {
		this.monto_comision = monto_comision;
	}

	public String getSucursal() {
		return sucursal;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getNombre_sucursal() {
		return nombre_sucursal;
	}

	public void setNombre_sucursal(String nombre_sucursal) {
		this.nombre_sucursal = nombre_sucursal;
	}

	public String getTipo_sucursal() {
		return tipo_sucursal;
	}

	public void setTipo_sucursal(String tipo_sucursal) {
		this.tipo_sucursal = tipo_sucursal;
	}

}
