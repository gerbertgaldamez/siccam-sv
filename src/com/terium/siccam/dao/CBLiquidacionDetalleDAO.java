package com.terium.siccam.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBLiquidacionDetalleModel;
import com.terium.siccam.utils.ConsultasSQ;

/*
 * @author CarlosGodinez - QitCorp
 * */
public class CBLiquidacionDetalleDAO {
	
	public int obtenerPK(){
		int pk = 0;
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Consulta para obtener PK liquidación detalle: " + ConsultasSQ.OBTENER_PK_LIQUIDACION_DETALLE);
			cmd = conn.prepareStatement(ConsultasSQ.OBTENER_PK_LIQUIDACION_DETALLE);
			rs = cmd.executeQuery();
			while(rs.next()){
				pk = rs.getInt(1);
			}
		}catch (Exception e) {
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(cmd != null)
					try {
						cmd.close();
					} catch (SQLException e) {
						Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
			.log(Level.INFO,"Llave primaria obtenida: " + pk);
		return pk;
	}
	
	public boolean guar(List<CBLiquidacionDetalleModel> list, int fk, String creador){
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try{
			 conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Insert masivo de Liquidaci�n detalle: " + ConsultasSQ.INSERT_MASIVO_LIQUIDACION_DETALLE);
			 cmd = conn.prepareStatement(ConsultasSQ.INSERT_MASIVO_LIQUIDACION_DETALLE);
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Registros antes de guardar en CB_LIQUIDACION_DETALLE: " + list.size());
			int exitosas = 0;
			for (CBLiquidacionDetalleModel d : list) {  
				cmd.setInt(1, fk);
	            cmd.setInt(2, d.getTipo_valo());   
	            System.out.println("tipo valor " + d.getTipo_valo());
	            cmd.setString(3,d.getTipo_pago());
	            System.out.println("tipo pago " + d.getTipo_pago());
	            cmd.setString(4, d.getCod_tipotarjeta());
	            cmd.setString(5, d.getDesc());
	            cmd.setString(6,d.getTotal()); 
	            cmd.setString(7, creador);
	            cmd.addBatch();
	            exitosas++;
	        }  
			if(exitosas > 0){	
		        cmd.executeBatch(); 
		        Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
					.log(Level.INFO,"Registros de detalle liquidaci�n guardados con �xito: " + exitosas);
		        result = true;
			}
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public boolean guarDetalleTipoValor19(List<CBLiquidacionDetalleModel> list, int pk,int fk, String creador) throws NamingException{
		boolean result = false;
		Connection conn =  null;
		PreparedStatement cmd = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			System.out.println("Insert tipo valor 19: " + ConsultasSQ.INSERT_TIPO_VALOR_X);
			cmd = conn.prepareStatement(ConsultasSQ.INSERT_TIPO_VALOR_X);
			int exitosas = 0;
			for (CBLiquidacionDetalleModel d : list) {  
				cmd.setInt(1, pk);
				cmd.setInt(2, fk);
				cmd.setInt(3, d.getTipo_valo());   
				cmd.setString(4,d.getTipo_pago());
				cmd.setString(5, d.getCod_tipotarjeta());
	            cmd.setString(6, d.getDesc());
	            cmd.setString(7,d.getTotal()); 
	            cmd.setString(8, creador);
	            cmd.addBatch();
	            exitosas++;
	        }  
			if(exitosas > 0){	
		        cmd.executeBatch(); 
		        Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
					.log(Level.INFO,"Registro de detalle liquidaci�n de tipo valor 19 guardado con �xito.");
		        cmd.close();
		        result = true;
		    }
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public boolean modiDetalleTipoValorX(CBLiquidacionDetalleModel param, int pk){
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			 Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Update detalle liquidaci�n: " + ConsultasSQ.UPDATE_TIPO_VALOR_X);
			 Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Detalle liquidacion id = " + pk);
			 cmd = conn.prepareStatement(ConsultasSQ.UPDATE_TIPO_VALOR_X);
			cmd.setString(1, param.getDesc());
			cmd.setInt(2, pk);
			if(cmd.executeUpdate() > 0)
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
					.log(Level.INFO,"Registro de detalle liquidaci�n modificado con �xito.");
				result = true;
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}
		}
		return result;
	}
	
	public boolean execConciliaDepositoPrc(int idDetalle){
		boolean result = false;
		Connection conn = null;
		CallableStatement cmd = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Store procedure CB_DEPOSITOS_DETALLE_SP: " + ConsultasSQ.EXEC_CONCILIA_DEPOSITO_PRC);
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Detalle liquidacion id = " + idDetalle);
			 cmd = conn.prepareCall(ConsultasSQ.EXEC_CONCILIA_DEPOSITO_PRC);
			cmd.setInt(1, idDetalle);
			if(cmd.executeUpdate()>0)
				result = true;
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
					.log(Level.INFO,"Store procedure CB_DEPOSITOS_DETALLE_SP ejecutado con �xito.");
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public boolean execConciliaCredUnico(int idDetalle){
		boolean result = false;
		Connection conn = null;
		CallableStatement cmd = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Store procedure CB_CONCILIA_CRED_UNICO_SP: " + ConsultasSQ.EXEC_CONCILIA_CRED_UNICO_SP);
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Detalle liquidacion id = " + idDetalle);
			 cmd = conn.prepareCall(ConsultasSQ.EXEC_CONCILIA_CRED_UNICO_SP);
			cmd.setInt(1, idDetalle);
			if(cmd.executeUpdate()>0)
				result = true;
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
					.log(Level.INFO,"Store procedure CB_CONCILIA_CRED_UNICO_SP para el tipo valor 12 ejecutado con �xito.");
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public List<CBLiquidacionDetalleModel> execQuery(CBLiquidacionDetalleModel objModel){
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBLiquidacionDetalleModel> list = new ArrayList<CBLiquidacionDetalleModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"M�dulo liquidaciones...ejecuci�n de query");
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Usuario consulta: " + objModel.getNombtransaccion());
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Fecha consulta: " + objModel.getFec_efectividad());
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Query consulta previa a registro de liquidaci�n: " + ConsultasSQ.CONSULTA_QUERY_NUEVA_LIQUIDACION);
			
			 cmd = conn.prepareStatement(ConsultasSQ.CONSULTA_QUERY_NUEVA_LIQUIDACION);
			cmd.setString(1, objModel.getFec_efectividad());
			cmd.setString(2, objModel.getNombtransaccion());
			rs = cmd.executeQuery();
			CBLiquidacionDetalleModel objeBean;
			while (rs.next()) {
				objeBean = new CBLiquidacionDetalleModel();
				objeBean.setTipo_valo(rs.getInt(1));
				objeBean.setTipo_pago(rs.getString(2));
				objeBean.setCod_tipotarjeta(rs.getString(3));
				objeBean.setDesc(rs.getString(4));
				objeBean.setTotal(String.format("%.2f",Double.parseDouble(rs.getString(5))).replace(',', '.'));
				list.add(objeBean);
			}
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
	}
		return list;
	}
	
	public List<CBLiquidacionDetalleModel> consByID(int id){
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBLiquidacionDetalleModel> list = new ArrayList<CBLiquidacionDetalleModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Consulta de detalle liquidaci�n por ID = " + ConsultasSQ.CONS_DETALLE_LIQUIDACION_BY_ID);
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"ID de liquidaci�n seleccionado = " + id);
			 cmd = conn.prepareStatement(ConsultasSQ.CONS_DETALLE_LIQUIDACION_BY_ID);
			cmd.setInt(1, id);
			rs = cmd.executeQuery();
			CBLiquidacionDetalleModel objeBean;
			while (rs.next()) {
				objeBean = new CBLiquidacionDetalleModel();
				objeBean.setCbliquidaciondetalleid(rs.getInt(1));
				objeBean.setTipo_valo(rs.getInt(2));
				objeBean.setTipo_pago(rs.getString(3));
				objeBean.setCod_tipotarjeta(rs.getString(4));
				objeBean.setDesc(rs.getString(5));
				objeBean.setTotal(String.format("%.2f",Double.parseDouble(rs.getString(6))).replace(',', '.'));
				list.add(objeBean);
			}
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
	}
		return list;
	}

	public List<CBLiquidacionDetalleModel> consultaReporte(String user, String fechaInicio, String fechaFin){
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBLiquidacionDetalleModel> list = new ArrayList<CBLiquidacionDetalleModel>();
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			String query = ConsultasSQ.CONSULTA_REPORTE_LIQUIDACIONES;
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Consulta de reportes de liquidaciones = " + query);
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Par�metros env�ados: ");
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"User = " + user);
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Fecha inicio = " + fechaInicio);
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName())
				.log(Level.INFO,"Fecha fin = " + fechaFin);
			String where = "";
			
			if(!user.equals("") && !fechaInicio.equals("") && !fechaFin.equals("")){
				where = "WHERE NOMBTRANSACCION LIKE ? AND FECHATRANSACCION BETWEEN to_date(?, 'DD-MM-YYYY') AND to_date(?, 'DD-MM-YYYY') ";
				cmd = conn.prepareStatement(query + where);
				cmd.setString(1, "%" + user + "%");
				cmd.setString(2, fechaInicio);
				cmd.setString(3, fechaFin);
				rs = cmd.executeQuery();
			} else if(!user.equals("") && fechaInicio.equals("") && fechaFin.equals("")){
				where = "WHERE NOMBTRANSACCION LIKE ? ";
				cmd = conn.prepareStatement(query + where);
				cmd.setString(1, "%" + user + "%");
				rs = cmd.executeQuery();
			} else if(user.equals("") && !fechaInicio.equals("") && !fechaFin.equals("")){
				where = "WHERE FECHATRANSACCION BETWEEN to_date(?, 'DD-MM-YYYY') AND to_date(?, 'DD-MM-YYYY') ";
				cmd = conn.prepareStatement(query + where);
				cmd.setString(1, fechaInicio);
				cmd.setString(2, fechaFin);
				rs = cmd.executeQuery();
			} else if(user.equals("") && fechaInicio.equals("") && fechaFin.equals("")){
				where = "";
				cmd = conn.prepareStatement(query + where);
				rs = cmd.executeQuery();
			}
			System.out.println("Where consulta = " + where);
			CBLiquidacionDetalleModel objeBean;
			while(rs.next()){
				objeBean = new CBLiquidacionDetalleModel();
				objeBean.setNombtransaccion(rs.getString(1));
				objeBean.setFec_efectividad(rs.getString(2));
				objeBean.setTipo_valo(rs.getInt(3));
				objeBean.setTipo_pago(rs.getString(4));
				objeBean.setCod_tipotarjeta(rs.getString(5));
				objeBean.setDesc(rs.getString(6));
				String efectivo = rs.getString(7);
				if(efectivo != null){
					objeBean.setTotal(String.format("%.2f",Double.parseDouble(efectivo)).replace(',', '.'));
				} else {
					objeBean.setTotal("N/A");
				}
				list.add(objeBean);
			}
		}catch(Exception e){
			Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionDetalleDAO.class.getName()).log(Level.SEVERE, null, e);
				}
	}
		return list;
	}
}
