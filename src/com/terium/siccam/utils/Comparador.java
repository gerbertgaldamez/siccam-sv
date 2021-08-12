package com.terium.siccam.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.terium.siccam.utils.Configuracion;

@SuppressWarnings("rawtypes")
public class Comparador implements Comparator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3942461150766421025L;
	
	public static final int TIPO_NUMERO = 1;
	public static final int TIPO_FECHA = 2;
	
	public static final int ORD_ASCENDENTE = 3;
	public static final int ORD_DESCENDENTE = 4;
	
	private boolean ordAsc;
	private int tipoObjeto;
	private int posColumna;
	
	public Comparador(int tipoOrdenamiento, int tipoObjeto, int posicionColumna) {
		if (tipoOrdenamiento==ORD_ASCENDENTE)
			this.ordAsc = true;
		else
			this.ordAsc = false;
		this.tipoObjeto = tipoObjeto;
		this.posColumna = posicionColumna;
	}

	public int compare(Object arg0, Object arg1) {
		int resp = 0;
		Listitem li1 = (Listitem)arg0;
		Listitem li2 = (Listitem)arg1;
		
		Listcell celda1 = (Listcell)li1.getChildren().get(this.posColumna-1);
		Listcell celda2 = (Listcell)li2.getChildren().get(this.posColumna-1);
		
		String strValor1 = celda1.getLabel();
		String strValor2 = celda2.getLabel();
		
		if (tipoObjeto == TIPO_NUMERO) {
			BigDecimal num1 = new BigDecimal((String)strValor1);
			BigDecimal num2 = new BigDecimal((String)strValor2);
			resp = (num1.compareTo(num2)*(ordAsc?1:-1));
		} else if (tipoObjeto == TIPO_FECHA) {
			Timestamp fec1 = null;
			Timestamp fec2 = null;
			Configuracion conf = new Configuracion();
			try {
				fec1 = new Timestamp(conf.FORMATO_FECHA_HORA.parse((String)strValor1).getTime());
				fec2 = new Timestamp(conf.FORMATO_FECHA_HORA.parse((String)strValor2).getTime());
			} catch (ParseException e) {
				Logger.getLogger(Comparador.class.getName()).log(Level.SEVERE, null, e);
			}
			resp = (fec1.compareTo(fec2)*(ordAsc?1:-1));
		} 
		
		return resp;
	}

}
