package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCausasConciliacion;
import com.terium.siccam.utils.ConsultasSQ;


public class CBCausasConciliacionDAO {
	private static Logger log = Logger.getLogger(CBCausasConciliacionDAO.class);
	
	/**
	 * Modify by Juankrlos --> 19/07/2018
	 * 
	 * */
	
	
	public List<CBCausasConciliacion> obtieneListadoAcciones() {
		List<CBCausasConciliacion> listado = new ArrayList<CBCausasConciliacion>();
		
		Connection con = null;
		ResultSet rst = null;
		Statement stmt = null;
		CBCausasConciliacion obj =null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			stmt  = con.createStatement();
			log.debug( "query " + ConsultasSQ.OBTIENE_CAUSAS_CONCILIACION_SQ);
			//Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE,ConsultasSQ.OBTIENE_CAUSAS_CONCILIACION_SQ);
			rst = stmt.executeQuery(ConsultasSQ.OBTIENE_CAUSAS_CONCILIACION_SQ);
			while(rst.next()){
				obj = new CBCausasConciliacion();

				obj.setId(rst.getString(1));
				obj.setCausas(rst.getString(2));
				obj.setCreadoPor(rst.getString(3));
				obj.setFechaCreacion(rst.getString(4));
				obj.setCodigoconciliacion(rst.getString(5));
				obj.setTipo(rst.getInt(6));
				listado.add(obj);
			}
			
		} catch (Exception e) {
			log.error("obtieneListadoAcciones() - Error ", e);
			//Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rst != null)
				try {
					rst.close();
				} catch (SQLException e) {
					log.error("obtieneListadoAcciones() - Error ", e);
					//Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					log.error("obtieneListadoAcciones() - Error ", e);
					//Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					if(con != null)
					con.close();
				} catch (SQLException e) {
					log.error("obtieneListadoAcciones() - Error ", e);
					//Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return listado;
	}


}
