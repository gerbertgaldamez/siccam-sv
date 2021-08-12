package com.terium.siccam.utils;

import java.math.BigDecimal;
import java.util.List;


public class GeneraFiltroQuery {
	public static FiltroQuery generaFiltro(List<Filtro> filtros, List<Orden> orden, boolean addWhere) {
		FiltroQuery ret = null;
		Filtro f = null;
		int cont = 0;
		Object[] params = null;
		String   campo  = "", queryFiltro = ""; 
		
		ret = new FiltroQuery();
		ret.setParams(null);
		ret.setSql("");
		
		if (filtros != null && filtros.size() > 0) {
			for (int i = 0; i < filtros.size(); i++) {
				f = filtros.get(i);
				if(!f.getOperador().equals(Filtro.ISNULL)&&!f.getOperador().equals(Filtro.ISNOTNULL)&&!f.getOperador().equals(Filtro.IN)
						&&!f.getOperador().equals(Filtro.BETWEEN))
					cont++;
			}
			params = new Object[cont];
			int contParams = 0;
			for (int i = 0; i < filtros.size(); i++) {
				f = filtros.get(i);
				campo = f.getCampo();
				if(f.getOperador().equals(Filtro.LIKE))
					campo = "upper("+f.getCampo()+")";
				
				if(!f.getOperador().equals(Filtro.ISNULL)&&!f.getOperador().equals(Filtro.ISNOTNULL)&&!f.getOperador().equals(Filtro.IN)
						&&!f.getOperador().equals(Filtro.BETWEEN)) {
					if (i == 0 && addWhere) 
						queryFiltro += " where " + campo + " " + f.getOperador() + " ?";
					else 
						queryFiltro += " and " + campo + " " + f.getOperador() + " ?";
					params[contParams] = filtros.get(i).getValor();
					contParams++;
				}else if(f.getOperador().equals(Filtro.BETWEEN)){
					if (i == 0 && addWhere) 
						queryFiltro += " where " + campo + " " + f.getOperador() + " "+f.getValor();
					else 
						queryFiltro += " and " + campo + " " + f.getOperador() +  " "+f.getValor();
				}else {
					if (!f.getOperador().equals(Filtro.IN)) {
						if (i == 0 && addWhere) 
							queryFiltro += " where " + campo + " " + f.getOperador();
						else 
							queryFiltro += " and " + campo + " " + f.getOperador();
					} else {
						if (i == 0 && addWhere) 
							queryFiltro += " where " + campo + " " + f.getOperador() + " " + f.getValor();
						else 
							queryFiltro += " and " + campo + " " + f.getOperador() + " " + f.getValor();
					}
				}
			}
			ret.setParams(params);
			ret.setSql(queryFiltro);
		}
		

		if (orden != null) {
			Orden o = null;
			String queryOrden = "";
			for (int i = 0; i < orden.size(); i++) {
				o = orden.get(i);
				if (i == 0)
					queryOrden += " order by " + o.getCampo() + " "+ o.getOrden();						
				else
					queryOrden += " , " + o.getCampo() + " "+ o.getOrden();
			}
			if (ret.getSql() != null && !ret.getSql().trim().equals(""))
				ret.setSql(ret.getSql() + " " + queryOrden);
			else
				ret.setSql(queryOrden);
		}
		
		return ret;
	}
	
	public static FiltroQuery generaFiltroString(List<Filtro> filtros, List<Orden> orden, boolean addWhere) {
		FiltroQuery ret = null;
		Filtro f = null;
		int cont = 0;
		//Object[] params = null;
		String   campo  = "", queryFiltro = ""; 
		
		ret = new FiltroQuery();
		ret.setParams(null);
		ret.setSql("");
		
		if (filtros != null && filtros.size() > 0) {
			for (int i = 0; i < filtros.size(); i++) {
				f = filtros.get(i);
				campo = f.getCampo();
				if(f.getOperador().equals(Filtro.LIKE))
					campo = "upper("+f.getCampo()+")";
				
				if(!f.getOperador().equals(Filtro.ISNULL)&&!f.getOperador().equals(Filtro.ISNOTNULL)&&!f.getOperador().equals(Filtro.IN)
						&&!f.getOperador().equals(Filtro.BETWEEN)) {
					String valor = getValor(f.getValor());
					if (i == 0 && addWhere) {
						queryFiltro += " where " + campo + " " + f.getOperador() + valor;
					}else{ 
						queryFiltro += " and " + campo + " " + f.getOperador() + valor;
					}
				}else if(f.getOperador().equals(Filtro.BETWEEN)){
					if (i == 0 && addWhere) 
						queryFiltro += " where " + campo + " " + f.getOperador() + " "+f.getValor();
					else 
						queryFiltro += " and " + campo + " " + f.getOperador() +  " "+f.getValor();
				}else {
					if (!f.getOperador().equals(Filtro.IN)) {
						if (i == 0 && addWhere) 
							queryFiltro += " where " + campo + " " + f.getOperador();
						else 
							queryFiltro += " and " + campo + " " + f.getOperador();
					} else {
						if (i == 0 && addWhere) 
							queryFiltro += " where " + campo + " " + f.getOperador() + " " + f.getValor();
						else 
							queryFiltro += " and " + campo + " " + f.getOperador() + " " + f.getValor();
					}
				}
						
			}
			ret.setSql(queryFiltro);
		}

		if (orden != null) {
			Orden o = null;
			String queryOrden = "";
			for (int i = 0; i < orden.size(); i++) {
				o = orden.get(i);
				if (i == 0)
					queryOrden += " order by " + o.getCampo() + " "+ o.getOrden();						
				else
					queryOrden += " , " + o.getCampo() + " "+ o.getOrden();
			}
			if (ret.getSql() != null && !ret.getSql().trim().equals(""))
				ret.setSql(ret.getSql() + " " + queryOrden);
			else
				ret.setSql(queryOrden);
		}
		
		return ret;
	}
	
	/**Esta funci�n obtiene el valor que se necesita compara con el campo
	 * en el filtro del query*/
	private static String getValor(Object valor){
		if(valor instanceof BigDecimal){
			return valor.toString();
		}else if(valor instanceof Integer){
			return valor.toString();
		}else if(valor instanceof Double){
			return valor.toString();
		}else if(valor instanceof Long){
			return valor.toString();
		}else if(valor instanceof String){
			return "'"+valor+"'";
		}
		return "";
	}
	
}
