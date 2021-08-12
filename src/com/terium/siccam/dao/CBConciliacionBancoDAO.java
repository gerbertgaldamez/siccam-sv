package com.terium.siccam.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBConciliacionBancoModel;
import com.terium.siccam.utils.ConsultasSQ;
import com.terium.siccam.utils.Tools;;

public class CBConciliacionBancoDAO extends ControladorBase {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Obtenemos el listado de la vista para conciliacion de bancos
	 * @param objModel
	 * */
	public List<CBConciliacionBancoModel> generaConsultaPrincipal(CBConciliacionBancoModel objModel){
		List<CBConciliacionBancoModel> list = new ArrayList<CBConciliacionBancoModel>();
		String where = "";
		where += "and dia >= to_date('"+ objModel.getFechaInicioFiltro() +"','dd/MM/yyyy') " + 
				"and dia <= to_date('"+ objModel.getFechaFinFiltro() +"','dd/MM/yyyy') ";
		
		if(objModel.getCbcatalogoagenciaid() != 0) {
			where += "and cbcatalogoagenciaid = " + objModel.getCbcatalogoagenciaid();
		}	
		//CarlosGodinez -> 07/08/2018 
		//Se agrega filtro de codigo de colector (filtro opcional)
		if(!"".equals(objModel.getCodigoColector())) {
			where += " and trim(codigo_colector) = '" + objModel.getCodigoColector() + "' ";
		}
		//FIN CarlosGodinez -> 07/08/2018
		
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try{
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			System.out.println("consulta: "+ConsultasSQ.CONSULTA_CONCILIACION_BANCO+where+
																" order by 1, 2");
			rs = stm.executeQuery(ConsultasSQ.CONSULTA_CONCILIACION_BANCO+where+ 
																" order by 1, 2");
			CBConciliacionBancoModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionBancoModel();
				 obj.setFecha(rs.getString(1));
				 obj.setCbcatalogoagenciaid(rs.getInt(2));
				 obj.setNombre(rs.getString(3));		
				 obj.setCodigoColector(rs.getString(4)); //CarlosGodinez -> 07/08/2018
				 obj.setEstadopostpago(rs.getBigDecimal(5));
				 obj.setConfrontapostpago(rs.getBigDecimal(6));
				 obj.setDifpostpago(rs.getBigDecimal(7));
				 obj.setPagosdeldia(rs.getBigDecimal(8));
				 obj.setPagosotrosdias(rs.getBigDecimal(9));
				 obj.setPagosotrosmeses(rs.getBigDecimal(10));
				 obj.setReversasotrosdias(rs.getBigDecimal(11));
				 obj.setReversasotrosmeses(rs.getBigDecimal(12));
				 obj.setTotaldia(rs.getBigDecimal(13));
				 obj.setTotalgeneral(rs.getBigDecimal(14));
				 obj.setEstadocuenta(rs.getBigDecimal(15));
				 obj.setConciliadomanual(rs.getBigDecimal(16));
				 obj.setDiferenciatotal(rs.getBigDecimal(17));
				 obj.setPorcentajepostpago(rs.getBigDecimal(18));
				 obj.setComisionconfrontapostpago(rs.getBigDecimal(19));
				 obj.setComisionpostpago(rs.getBigDecimal(20));
				 obj.setDiferenciacomisionpospago(rs.getBigDecimal(21));		
				 obj.setComisiontotal(rs.getBigDecimal(22));
				 obj.setRecafinalpost(rs.getBigDecimal(23));
				 obj.setTotalfinal(rs.getBigDecimal(24));		
				 obj.setCbbancoagenciaconfrontaid(rs.getInt(25));
				 obj.setFormapago(rs.getString(26));
				 
				 list.add(obj);
			}
			
		}catch(SQLException e){		
			Tools.ingresaLog(e, "Modulo consulta conciliacion bancos", 
					"Error al consultar la conciliacion de bancos", "CBConciliacionBancoDAO.java");
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		
		return list;
	}
	
	/**
	 * 
	 * */
	private static String OBTIENE_ENTIDAD_SQ = "select cbcatalogobancoid, nombre from cb_catalogo_banco "
			+ "where estado = 1 and tipo_entidad = 'NO FINANCIERA'";
	public List<CBConciliacionBancoModel> generaConsultaEntidad(){
		List<CBConciliacionBancoModel> list = new ArrayList<CBConciliacionBancoModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			rs = stm.executeQuery(OBTIENE_ENTIDAD_SQ);
			CBConciliacionBancoModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionBancoModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch(SQLException e){
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return list;
	}
	
	/**
	 * 
	 * */
	private static String OBTIENE_AGENCIA_SQ = "select distinct b.cbcatalogoagenciaid, (b.codigo_colector || ' - ' || b.nombre) nombre, b.codigo_colector " + 
			"from cb_catalogo_banco a, cb_catalogo_agencia b, cb_banco_agencia_confronta c " + 
			"where a.cbcatalogobancoid = b.cbcatalogobancoid " + 
			"and b.cbcatalogoagenciaid = c.cbcatalogoagenciaid " + 
			"and a.estado = 1 " + 
			"and b.estado = 1 " + 
			"and b.cuenta_contable is null "+ 
			" ORDER BY TO_NUMBER(b.codigo_colector) ASC  ";
	public List<CBConciliacionBancoModel> generaConsultaAgencia(){
		List<CBConciliacionBancoModel> list = new ArrayList<CBConciliacionBancoModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			System.out.println("query agencias:" + OBTIENE_AGENCIA_SQ);
			rs = stm.executeQuery(OBTIENE_AGENCIA_SQ);
			CBConciliacionBancoModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionBancoModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch(SQLException e){
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return list;
	}
	
	
	/**
	 * 
	 * */
	private static String OBTIENE_BANCO_SQ = "select cbcatalogobancoid, nombre " + 
			"from cb_catalogo_banco " + 
			"where estado = 1 " + 
			"and tipo_entidad = 'NO FINANCIERA'";
	public List<CBConciliacionBancoModel> generaConsultaBanco(){
		List<CBConciliacionBancoModel> list = new ArrayList<CBConciliacionBancoModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			rs = stm.executeQuery(OBTIENE_BANCO_SQ);
			CBConciliacionBancoModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionBancoModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch(SQLException e){
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return list;
	}
	
	
	
	/**
	 * Ejecutamos el Store procedure para comisiones
	 * */
	public static final String CARGA_COMISIONES_SP = "{CALL cb_comision_pagos_sp(?,?)}";
	public boolean ejecutaSPComisiones(Date fechainicio , Date fechafin) {
		boolean result = false;
		Connection con = null;
		CallableStatement cst = null;
		try{
			//SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			System.out.println("fecha inicio en el dao sp " + fechainicio);
			System.out.println("fecha inicio en el dao sp " + fechafin);
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.INFO,
					"Fecha inicio" + fechainicio);			
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.INFO,
					"Fecha fin" + fechafin);
			con = obtenerDtsPromo().getConnection();
			cst = con.prepareCall(CARGA_COMISIONES_SP);
			System.out.println("SP EN EL DAO CONTA " + CARGA_COMISIONES_SP);
			
			java.sql.Date sqlDate1 = new java.sql.Date(fechainicio.getTime()); 
			java.sql.Date sqlDate2 = new java.sql.Date(fechafin.getTime()); 
			
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.INFO,
					"fecha inicio en el dao sp" + sqlDate1);
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.INFO,
					"fecha fin en el dao sp" + sqlDate2);
			
			
			cst.setDate(1, (java.sql.Date) sqlDate1);
			cst.setDate(2, (java.sql.Date) sqlDate2);

			
		
			
			int exec = cst.executeUpdate();
			if(exec > 0){
				result = true;
			}
		}catch (Exception e) {
			Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(cst != null)
				try {
					cst.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
}
