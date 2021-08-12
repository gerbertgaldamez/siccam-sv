package com.terium.siccam.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.sql.ConsultasSQGT;
import com.terium.siccam.sql.ConsultasSQSV;
import com.terium.siccam.sql.ConsultasSQNI;
import com.terium.siccam.sql.ConsultasSQCR;
import com.terium.siccam.sql.ConsultasSQPA;

/**
 * @author Carlos Godinez -> 29/05/2018
 * */
public class ObtenerSQLPais {
	
	/**
	 * @param paisConexion = JNDI de conexion para un respectivo pais
	 * @param numeroVariable = numero de query en las utilidades respectivas de cada pais 
	 * @return query = valor SQL del pais indicado en el JNDI de conexion
	 * */
	public static String getValorSQL(String paisConexion, int numeroVariable) {
		String query = "";
		try {
			Logger.getLogger(ObtenerSQLPais.class.getName()).log(Level.INFO,
					"Pais de conexion = " + paisConexion);
			Logger.getLogger(ObtenerSQLPais.class.getName()).log(Level.INFO,
					"Utilidad a conectar = ConsultasSQ" + paisConexion);
			Logger.getLogger(ObtenerSQLPais.class.getName()).log(Level.INFO,
					"Numero de query a obtener en metodo getVariableSQL = " + numeroVariable);
			if(paisConexion.equals(Tools.SESSION_GT)) { 
				query = ConsultasSQGT.getVariableSQL(numeroVariable);
			} else if(paisConexion.equals(Tools.SESSION_SV)) {
				query = ConsultasSQSV.getVariableSQL(numeroVariable);
			} else if(paisConexion.equals(Tools.SESSION_NI)) {
				query = ConsultasSQNI.getVariableSQL(numeroVariable);
			} else if(paisConexion.equals(Tools.SESSION_CR)) {
				query = ConsultasSQCR.getVariableSQL(numeroVariable);
			} else if(paisConexion.equals(Tools.SESSION_PA)) {
				query = ConsultasSQPA.getVariableSQL(numeroVariable);
			} else {
				query = null;
			}
		} catch (Exception e) {
			Logger.getLogger(ObtenerSQLPais.class.getName()).log(Level.SEVERE, null, e);
		}
		return query;
	}
}
