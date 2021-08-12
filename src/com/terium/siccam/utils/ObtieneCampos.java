package com.terium.siccam.utils;

import java.lang.reflect.Field;
import java.util.List;

public class ObtieneCampos {

	public static String obtieneSQL2(Class clase, String sinonimo, boolean insert, List<String> camposLlave) throws IllegalArgumentException, IllegalAccessException {
		Field[] campos = clase.getFields();
		String retorno = "";
		if (sinonimo != null)
			sinonimo += ".";
		else 
			sinonimo = "";

		boolean esCampoLlave = false;

		if (campos != null) {
			for (int i=0; i<campos.length; i++) {
				if (campos[i].getName().startsWith("FIELD_")) {
					if (insert) {
						retorno += " " + sinonimo + campos[i].get(new String());
						retorno += ",";
					}
					else { 
						esCampoLlave = false;
						if (camposLlave != null && camposLlave.size() > 0) {
							for (int j=0; j<camposLlave.size(); j++) 
								if (camposLlave.get(j).equals((String) campos[i].get(new String()) )) {
									esCampoLlave = true;
									break;
								}
						}
						if (!esCampoLlave) {
							retorno += " " + sinonimo + campos[i].get(new String());
							retorno += " = ?,";
						}
					}
				}
			}

			if (retorno != null && retorno.endsWith(",")) 
				retorno = retorno.substring(0, retorno.length()-1);

			if (camposLlave != null && camposLlave.size() > 0) {
				for (int j=0; j<camposLlave.size(); j++)  {
					if (j == 0)
						retorno += " where " + camposLlave.get(j) + " = ? ";
					else
						retorno += " and " + camposLlave.get(j) + " = ? ";
				}
			}
		}
		return retorno;
	}

	public static String obtieneSQL(Class clase, String sinonimo, boolean insert, String nombreLlave) throws IllegalArgumentException, IllegalAccessException {
		Field[] campos = clase.getFields();
		String retorno = "";
		if (sinonimo != null)
			sinonimo += ".";
		else 
			sinonimo = "";
		if (nombreLlave == null)
			nombreLlave = "";

		if (campos != null) {
			for (int i=0; i<campos.length; i++) {
				if (campos[i].getName().startsWith("FIELD_") || campos[i].getName().startsWith("PKFIELD_")) {
					if (insert) {
						retorno += " " + sinonimo + campos[i].get(new String());
						retorno += ",";
					}
					else if (!((String)campos[i].get(new String())).equals(nombreLlave)) {
						retorno += " " + sinonimo + campos[i].get(new String());
						retorno += " = ?,";
					}
				}
			}
			if (retorno != null && retorno.endsWith(",")) 
				retorno = retorno.substring(0, retorno.length()-1);

			if (retorno != null && !insert && nombreLlave != null && !nombreLlave.trim().equals("")) {
				retorno += " where " + nombreLlave + " = ?";
			}
		}
		return retorno;
	}
	public static String obtieneInsert(Class clase) throws IllegalArgumentException, IllegalAccessException {
		Field[] campos = clase.getFields();
		String retorno = "";
		if (campos != null) {
			for (int i=0; i<campos.length; i++) 
				if (campos[i].getName().startsWith("FIELD_"))				
					retorno += " ?,";					
				else if (campos[i].getName().equals("SEQUENCE"))	
//                                    retorno += " ?,";
					retorno += campos[i].get(new String())+" ,";
                                        
                        

			if (retorno != null && retorno.endsWith(",")) 
				retorno = retorno.substring(0, retorno.length()-1);
		}
		return retorno;
	}
}
