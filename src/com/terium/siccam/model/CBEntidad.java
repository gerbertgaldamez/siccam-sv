package com.terium.siccam.model;

import java.math.BigDecimal;
import java.util.Date;

public class CBEntidad {
	Date fecha;
	int mes_1;
	String mes_2;
	String hora;
	BigDecimal monto;
	int telefono;
	String banco;
	String tipo_servicio;
	String secuencia;
	String agencia;
	String forma_de_pago;
	BigDecimal comision;
	String porcentaje;
	String sucursal;
	String nombre_sucursal;
	int cantidad;

	public CBEntidad() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CBEntidad(Date fecha, int mes_1, String mes_2, String hora,
			BigDecimal monto, int telefono, String banco, String tipo_servicio,
			String secuencia, String agencia, String forma_de_pago,
			BigDecimal comision, String porcentaje, String sucursal,
			String nombre_sucursal, int cantidad) {
		super();
		this.fecha = fecha;
		this.mes_1 = mes_1;
		this.mes_2 = mes_2;
		this.hora = hora;
		this.monto = monto;
		this.telefono = telefono;
		this.banco = banco;
		this.tipo_servicio = tipo_servicio;
		this.secuencia = secuencia;
		this.agencia = agencia;
		this.forma_de_pago = forma_de_pago;
		this.comision = comision;
		this.porcentaje = porcentaje;
		this.sucursal = sucursal;
		this.nombre_sucursal = nombre_sucursal;
		this.cantidad = cantidad;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public int getMes_1() {
		return mes_1;
	}

	public void setMes_1(int mes_1) {
		this.mes_1 = mes_1;
	}

	public String getMes_2() {
		return mes_2;
	}

	public void setMes_2(String mes_2) {
		this.mes_2 = mes_2;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getTipo_servicio() {
		return tipo_servicio;
	}

	public void setTipo_servicio(String tipo_servicio) {
		this.tipo_servicio = tipo_servicio;
	}

	public String getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(String secuencia) {
		this.secuencia = secuencia;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getForma_de_pago() {
		return forma_de_pago;
	}

	public void setForma_de_pago(String forma_de_pago) {
		this.forma_de_pago = forma_de_pago;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}

	public String getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(String porcentaje) {
		this.porcentaje = porcentaje;
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

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
