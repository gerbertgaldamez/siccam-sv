package com.terium.siccam.utils;

import java.util.Iterator;
import java.util.LinkedList;

/**Orden.java
 * Esta clase sirve para agregarle un orden determinado a una 
 * instrucci�n SELECT
 * 
 * */
public class Orden {
	//constante que le define un orden ascendente a la instrucci�n SELECT
	public final static String ORDEN_ASC = "ASC";
	//constante que le define un orden descendente a la instrucci�n SELECT
	public final static String ORDEN_DESC = "DESC";
	//campo que se utilizara como pivote para ordenar la consulta
	private String campo;
	//tipo de orden que se le aplicara a la consulta
	private String orden;
	
	private LinkedList<Orden> listaOrden;
	
	//Constructor Orden
	/** inicializa los campos con valores que al concatenarlos al query ordenan a partir de la primera columna*/
	public Orden(){
		setCampo("1");
		setOrden(ORDEN_DESC);
		listaOrden = new LinkedList<Orden>();
	}
	
	public Orden(String campo,String orden){
		setCampo(campo);
		setOrden(orden);
		listaOrden = new LinkedList<Orden>();
	}
	
	/**
	 * Constructor sin ordenacion alguno
	 * @param par valor dummy, mandar null
	 */
	public Orden(Object par){
		setCampo("");
		setOrden("");
		listaOrden = new LinkedList<Orden>();
	}
	
	/**retorna la cadena con la instrucci�n que definira el orden a la instrucci�n SELECT
	 * 
	 * */
	public String toString(){
		String retorno = "";
		if (!this.campo.trim().equals("") && !this.orden.trim().equals(""))
			retorno = " order by "+campo+" "+orden;
		return retorno;
	}
	
	/**
	 * Agrega un orden nuevo a la lista
	 * @param nuevoOrden el orden a evaluar
	 */
	public void agregaOrden(Orden nuevoOrden) {
		listaOrden.add(nuevoOrden);
	}
	
	/**
	 * Obtiene la cadena de la lista de 'order' ingresados
	 * @return cadena conteniendo los ordenes ingresados
	 */
	public String obtenerListaOrden(){
		String resp = "";
		
		Iterator<Orden> it = listaOrden.iterator();
		while (it.hasNext()) {
			Orden ord = it.next();
			if (resp.length()>0)
				resp+=", ";
			resp+= ord.getCampo() + " " + ord.getOrden();
		}
		
		if (resp.length()>0)
			resp = " order by " + resp;
		
		return resp;
	}
	
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
}
