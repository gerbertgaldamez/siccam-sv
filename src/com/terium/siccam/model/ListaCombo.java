package com.terium.siccam.model;

import java.math.BigDecimal;

public class ListaCombo {
	private BigDecimal valorEntero;
	private String valorCaracter;
	private String valorCodigo;

	public ListaCombo(String valorCaracter, String valorCodigo) {
		this.valorCaracter = valorCaracter;
		this.valorCodigo = valorCodigo;
	}

	public ListaCombo() {

	}

	public String getValorCodigo() {
		return valorCodigo;
	}

	public void setValorCodigo(String valorCodigo) {
		this.valorCodigo = valorCodigo;
	}

	public BigDecimal getValorEntero() {
		return valorEntero;
	}

	public void setValorEntero(BigDecimal valorEntero) {
		this.valorEntero = valorEntero;
	}

	public String getValorCaracter() {
		return valorCaracter;
	}

	public void setValorCaracter(String valorCaracter) {
		this.valorCaracter = valorCaracter;
	}

}
