package com.terium.siccam.utils;

import java.util.List;

public class Filtro {
	String campo;//campo evaluado en el filtro
	String operador;//operador que es aplicado 
	String valor;//valor con el cual se compara el campo
	
	public static final String IGUAL      = "=";
	public static final String DIFERENTE  = "!=";
	public static final String MAYOR      = ">";
	public static final String MENOR      = "<";
	public static final String MAYORIGUAL = ">=";
	public static final String MENORIGUAL = "<=";
	public static final String LIKE 	  = "like";
	public static final String ISNULL     = "is null";
	public static final String ISNOTNULL  = "is not null";
	public static final String IN		  = "in";
	public static final String BETWEEN    = "between";
	public static final String ESPECIAL   = "especial";
	
	public Filtro(){
	}
	
	public Filtro(String campo,String operador,String valor){
		setCampo(campo);
		setOperador(operador);
		setValor(valor);
	}
	
	/**retorna un listado de filtros con formato <and> <campo> <operador> <valor>
	 * @param filtros listado de filtros que se concatenaran luego en una sentencias select
	 * @param esPrimerFiltro nos dice si le debemos agregar un WHERE o un AND al inicio de la cadena
	 * @return filtro cadena con los filtros construidos con sintaxis de sql 
	 * */
	
	public static String getStringFiltros(List<Filtro> filtros,boolean esPrimerFiltro){
		String filtro = "";
                if(filtros != null && !filtros.isEmpty()){
		//si la lista de filtros viene vacia retornamos un espacio
                    if(filtros.size()==0){
                            return " ";
                    }
                    //si no es primer filtro entonces iniciamos la cadena con AND
                    if(!esPrimerFiltro&&filtros.size()!=0){
                            filtro =" AND ";
                    }else if(esPrimerFiltro&&filtros.size()>0){//si es primer filtro iniciamos la cadena con WHERE
                            filtro = " WHERE ";
                    }

                    for (int i = 0; i < filtros.size(); i++) {
                            if(i==0){//el primer filtro ya lleva la clausula where o el AND
                                    filtro += filtros.get(i).getCampo()+" "+filtros.get(i).getOperador()+" "+filtros.get(i).getValor() +" ";
                            }else{//El resto de filtros le concatenamos el AND
                                    filtro += " AND "+ filtros.get(i).getCampo()+" "+filtros.get(i).getOperador()+" "+filtros.get(i).getValor() +" ";
                            }
                    }
                }
		return filtro;
	}
	
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getOperador() {
		return operador;
	}
	public void setOperador(String operador) {
		this.operador = operador;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
}
