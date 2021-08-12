package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zul.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBConciliacionCajasModel;

@SuppressWarnings("serial")
public class CBConciliacionCajasDAO extends ControladorBase{
	
	//Editado ultima vez por Carlos Godinez - Qitcorp - 22/05/2017
	/*
	String CONSULTA_CAJEROS = "SELECT CBCATALOGOBANCOID,ENTIDAD,CBCATALOGOAGENCIAID,AGENCIA, to_char(FECHA, 'dd/MM/yyyy'),CAJA_EFECTIVO,CAJA_CHEQUE, "
			+ "CAJA_EXENSIONES, CAJA_CUOTAS_VISA, CAJA_CUOTAS_CREDOMATIC,CAJA_TARJETA_CREDOMATIC,CAJA_TARJETA_OTRAS,CAJA_TARJETA_VISA, "
			+ "SC_EFECTIVO, SC_PAGOSOD,SC_PAGOSOM,SC_REVERSASOD,SC_REVERSASOM, "
			+ "NVL(CAJA_TOTAL,0)+NVL(SC_PAGOSOD,0)+NVL(SC_REVERSASOD,0) TOTAL_DIA, "
			+ "CAJA_TOTAL, CREDOMATIC_DEP CONSUMO_CREDOMATIC,CREDOMATIC_RET RETENCION_CREDOMATIC,ESTADO_CRED,CONSUMO_VISA, "
			+ "IVA_VISA,ESTADO_VISA,DEPOSITO, "
			+ "NVL(CREDOMATIC_DEP,0)+NVL(CONSUMO_VISA,0)+NVL(IVA_VISA,0)+NVL(DEPOSITO,0) TOTAL_EC, "
			+ "NVL(TOTAL_SC,0)-(NVL(CREDOMATIC_DEP,0)+NVL(CONSUMO_VISA,0)+NVL(IVA_VISA,0)+NVL(DEPOSITO,0)) DIFERENCIA "
			+ "FROM CB_CONCILIACION_CAJAS_VW "
			+ "WHERE 1 = 1  ";
			*/
	String CONSULTA_CAJEROS = "select CBCATALOGOBANCOID,ENTIDAD, CBCATALOGOAGENCIAID, AGENCIA,  TO_CHAR (FECHA, 'dd/MM/yyyy'), CAJA_EFECTIVO, CAJA_CHEQUE, CAJA_EXENSIONES, CAJA_CUOTAS_VISA, CAJA_CUOTAS_CREDOMATIC, CAJA_TARJETA_CREDOMATIC, CAJA_TARJETA_OTRAS," + 
			"CAJA_TARJETA_VISA, SC_EFECTIVO, SC_PAGOSOD, SC_PAGOSOM, SC_REVERSASOD, SC_REVERSASOM, TOTAL_DIA,CAJA_TOTAL, CONSUMO_CREDOMATIC,  RETENCION_CREDOMATIC, ESTADO_CRED, CONSUMO_VISA,IVA_VISA, ESTADO_VISA, DEPOSITO, TOTAL_EC, " + 
			"DIFERENCIA from CB_CONCILIACION_CAJAS_VS_VW    WHERE 1 = 1 ";
	/**
	 * Obtenemos el listado de la vista
	 * @param entidad agencia fechaini fechafin idbanco
	 * Modifica Juankrlos 001/11/2017
	 * se agrega campo SCefectivo
	 * */
	public List<CBConciliacionCajasModel> generaConsultaPrincipal(int entidad, int agencia,
			String fechaini, String fechafin){
		List<CBConciliacionCajasModel> list = new ArrayList<CBConciliacionCajasModel>();
		String where = "";
		where += " AND fecha  >= to_date('"+fechaini+"', 'dd/MM/yyyy') " + 
				" AND fecha  <= to_date('"+fechafin+"', 'dd/MM/yyyy') ";
		System.out.println("fecha:" + where);
		if(entidad != 0) {
			where += "AND CBCATALOGOBANCOID = '"+entidad+"'";
		}
		if(agencia != 0) {
			where += "AND CBCATALOGOAGENCIAID = '"+agencia+"'";
		}	
		
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try{
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			//Messagebox.show("query." + CONSULTA_CAJEROS+where, "ATENCION", Messagebox.OK,
			//		Messagebox.INFORMATION);
			System.out.println("consulta: "+CONSULTA_CAJEROS+where);
			rs = stm.executeQuery(CONSULTA_CAJEROS+where+" ORDER BY 1, 2, 3, 4, 5 ");
			//Messagebox.show("query despues." + CONSULTA_CAJEROS+where, "ATENCION", Messagebox.OK,
			//		Messagebox.INFORMATION);
			CBConciliacionCajasModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionCajasModel();
				 obj.setCbcatalogobancoid(rs.getInt(1));
				 obj.setEntidad(rs.getString(2));
				 obj.setCbcatalogoagenciaid(rs.getInt(3));
				 obj.setAgencia(rs.getString(4));
				 obj.setFecha(rs.getString(5));
				 obj.setCajaefectivo(rs.getDouble(6));
				 obj.setCajacheque(rs.getDouble(7));
				 obj.setCajaexenciones(rs.getDouble(8));
				 obj.setCajacuotasvisa(rs.getDouble(9));
				 obj.setCajacuotascredomatic(rs.getDouble(10));
				 obj.setCajatarjetacredomatic(rs.getDouble(11));
				 obj.setCajatarjetaotras(rs.getDouble(12));
				 obj.setCajatarjetavisa(rs.getDouble(13));
				 obj.setScefectivo(rs.getDouble(14));
				 obj.setScpagosod(rs.getDouble(15));
				 obj.setScpagosom(rs.getDouble(16));
				 obj.setScreversasod(rs.getDouble(17));
				 obj.setScreversasom(rs.getDouble(18));
				 obj.setTotaldia(rs.getDouble(19));
				 obj.setCajatotal(rs.getDouble(20));
				 obj.setCredomaticdep(rs.getDouble(21));
				 obj.setCredomaticRet(rs.getDouble(22));
				 obj.setEstadoCredo(rs.getDouble(23));
				 obj.setConsumovisa(rs.getDouble(24));
				 obj.setIvavisa(rs.getDouble(25));
				 obj.setEstadoVisa(rs.getDouble(26));
				 obj.setDeposito(rs.getDouble(27));
				 obj.setTotalec(rs.getDouble(28));
				 obj.setDiferencia(rs.getDouble(29));
	
				 list.add(obj);
			}
		}catch(SQLException e){
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		}catch(Exception e){
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		
		return list;
	}
	
	/**
	 * 
	 * */
	private static String OBTIENE_ENTIDAD_SQ = "select cbcatalogobancoid, nombre from cb_catalogo_banco "
			+ "where estado = 1 and tipo_entidad = 'NO FINANCIERA'";
	public List<CBConciliacionCajasModel> generaConsultaEntidad(){
		List<CBConciliacionCajasModel> list = new ArrayList<CBConciliacionCajasModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			rs = stm.executeQuery(OBTIENE_ENTIDAD_SQ);
			System.out.println("query combo " + OBTIENE_ENTIDAD_SQ);
			CBConciliacionCajasModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionCajasModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch(SQLException e) {
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		}catch(Exception e){
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return list;
	}
	
	/**
	 * 
	 * */
	private static String OBTIENE_AGENCIA_SQ = "select b.cbcatalogoagenciaid, (b.codigo_colector || ' - ' || b.nombre) nombre  " + 
			"from cb_catalogo_banco a, cb_catalogo_agencia b " + 
			"where a.cbcatalogobancoid = b.cbcatalogobancoid " + 
			"and a.tipo_entidad = 'NO FINANCIERA' " + 
			"and b.estado = 1 " + 
			"and a.estado = 1 ";
	public List<CBConciliacionCajasModel> generaConsultaAgencia(int idBanco){
		List<CBConciliacionCajasModel> list = new ArrayList<CBConciliacionCajasModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		
		try {
			
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			String query = OBTIENE_AGENCIA_SQ;
			query += " and b.cbcatalogobancoid = "+idBanco;
			query += " ORDER BY TO_NUMBER(b.codigo_colector) ASC ";
			System.out.println("query combo entidades " + query);
			rs = stm.executeQuery(query);
			CBConciliacionCajasModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionCajasModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch(SQLException e) {
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		}catch(Exception e){
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
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
	public List<CBConciliacionCajasModel> generaConsultaBanco(){
		List<CBConciliacionCajasModel> list = new ArrayList<CBConciliacionCajasModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			rs = stm.executeQuery(OBTIENE_BANCO_SQ);
			CBConciliacionCajasModel obj = null;
			while(rs.next()) {
				obj = new CBConciliacionCajasModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch(SQLException e) {
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		}catch(Exception e){
			Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stm != null)
					try {
						stm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBConciliacionCajasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return list;
	}

}
