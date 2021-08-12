package com.terium.siccam.model;

public class CBParametrosNomenclaturaModel {
	private String nombre;
	private String identificador;
	private int inicia;
	private int finaliza;
	
	public CBParametrosNomenclaturaModel(){
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public int getInicia() {
		return inicia;
	}

	public void setInicia(int inicia) {
		this.inicia = inicia;
	}

	public int getFinaliza() {
		return finaliza;
	}

	public void setFinaliza(int finaliza) {
		this.finaliza = finaliza;
	}
}
