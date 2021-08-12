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
import com.terium.siccam.model.CBLiquidacionCajeroModel;
import com.terium.siccam.utils.ConsultasSQ;

/*
 * @author CarlosGodinez - QitCorp
 * */
public class CBLiquidacionCajeroDAO {
	
	public int obtenerPK(){
		int pk = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
			.log(Level.INFO,"Consulta para obtener PK liquidación = " + ConsultasSQ.OBTENER_PK_LIQUIDACION);
			ps = conn.prepareStatement(ConsultasSQ.OBTENER_PK_LIQUIDACION);
			rs = ps.executeQuery();
			while(rs.next()){
				pk = rs.getInt(1);
			}
		}catch (Exception e) {
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
		.log(Level.INFO,"Llave primaria obtenida: " + pk);
		return pk;
	}
	
	public int guar(CBLiquidacionCajeroModel param, int pk){
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
			.log(Level.INFO,"Insert liquidación = " + ConsultasSQ.INSERT_LIQUIDACION);
			ps = conn.prepareStatement(ConsultasSQ.INSERT_LIQUIDACION);
			
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
			.log(Level.INFO,"Llave primaria envíada = " + pk);
			ps.setInt(1, pk);
			ps.setString(2, param.getNombtransaccion());
			ps.setString(3, param.getFechatransaccion());
			ps.setString(4, param.getDescripcion());
			ps.setInt(5, param.getEstado());
			ps.setString(6, param.getCreador());
			result = ps.executeUpdate();
			if(result > 0)
				Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Liquidación registrada con éxito...");
		}catch (Exception e) {
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(ps != null)ps.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public int elim(int pk){
		int result = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
			.log(Level.INFO,"Delete liquidación = " + ConsultasSQ.DELETE_LIQUIDACION);
			ps = conn.prepareStatement(ConsultasSQ.DELETE_LIQUIDACION);
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
			.log(Level.INFO,"Llave primaria envíada = " + pk);
			ps.setInt(1, pk);
			result = ps.executeUpdate();
			if(result > 0)
				Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Liquidación eliminada con éxito...");
		}
		catch(Exception e){
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(ps != null)ps.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	
	public boolean transaccionValida(String nomb, String fecha){
		boolean result = true;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try{
			con = ControladorBase.obtenerDtsPromo().getConnection();

			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Consulta valida liquidación = " + ConsultasSQ.VALIDA_LIQUIDACION);
			
			ps = con.prepareStatement(ConsultasSQ.VALIDA_LIQUIDACION);
			
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Nombre de usuario envíado = " + nomb);
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Fecha de transacción envíada = " + fecha);
			
			ps.setString(1, nomb);
			ps.setString(2, fecha);
			rs = ps.executeQuery();
			if(rs.next()){
				result = false;
			}
			String resultStr = (result ? "Liquidación válida" : "Liquidación inválida");

			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,resultStr);

		}catch (Exception e) {
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	
	public List<CBLiquidacionCajeroModel> consTodo(CBLiquidacionCajeroModel objModel){
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		String fecha = "";
		if(objModel.getFechatransaccion()==null) {
			objModel.setFechatransaccion(fecha);
		}
		List<CBLiquidacionCajeroModel> list = new ArrayList<CBLiquidacionCajeroModel>();
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			String query = ConsultasSQ.CONSULTAR_LIQUIDACIONES;
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Consulta liquidaciones = " + query);
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Filtros enviados...");
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"User = " + objModel.getNombtransaccion());
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Fecha de inicio = " + objModel.getFechatransaccion());
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Fecha fin = " + objModel.getFechatransaccion());
			String where = "";
			
			if(!objModel.getNombtransaccion().equals("") && !objModel.getFechatransaccion().equals("") && !objModel.getFechatransaccion().equals("")){
				where = "WHERE UPPER (nombtransaccion) LIKE ? AND fechatransaccion BETWEEN to_date(?, 'DD-MM-YYYY') AND to_date(?, 'DD-MM-YYYY') ";
				cmd = conn.prepareStatement(query + where + "ORDER BY cbliquidacionid");
				cmd.setString(1, "%" + objModel.getNombtransaccion().toUpperCase() + "%");
				cmd.setString(2, objModel.getFechatransaccion());
				cmd.setString(3, objModel.getFechatransaccion());
				rs = cmd.executeQuery();
			} else if(!objModel.getNombtransaccion().equals("") && objModel.getFechatransaccion().equals("") && objModel.getFechatransaccion().equals("")){
				where = "WHERE UPPER (nombtransaccion) LIKE ? ";
				cmd = conn.prepareStatement(query + where + "ORDER BY cbliquidacionid");
				cmd.setString(1, "%" + objModel.getNombtransaccion().toUpperCase() + "%");
				rs = cmd.executeQuery();
			} else if(objModel.getNombtransaccion().equals("") && !objModel.getFechatransaccion().equals("") && !objModel.getFechatransaccion().equals("")){
				where = "WHERE fechatransaccion BETWEEN to_date(?, 'DD-MM-YYYY') AND to_date(?, 'DD-MM-YYYY') ";
				cmd = conn.prepareStatement(query + where + "ORDER BY cbliquidacionid");
				cmd.setString(1, objModel.getFechatransaccion());
				cmd.setString(2, objModel.getFechatransaccion());
				rs = cmd.executeQuery();
			} else if(objModel.getNombtransaccion().equals("") && objModel.getFechatransaccion().equals("") && objModel.getFechatransaccion().equals("")){
				where = "";
				cmd = conn.prepareStatement(query + where + "ORDER BY cbliquidacionid");
				rs = cmd.executeQuery();
			}
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName())
				.log(Level.INFO,"Where consulta = " + where);
			CBLiquidacionCajeroModel objeBean;
			while(rs.next()){
				objeBean = new CBLiquidacionCajeroModel();
				objeBean.setCbliquidacionid(rs.getInt(1));
				objeBean.setNombtransaccion(rs.getString(2));
				objeBean.setFechatransaccion(rs.getString(3));
				String efectivo = rs.getString(4);
				if(efectivo != null){
					objeBean.setEfectivo(String.format("%.2f",Double.parseDouble(efectivo)).replace(',', '.'));
				} else {
					objeBean.setEfectivo("N/A");
				}
				objeBean.setCuotasvisa(String.format("%.2f",Double.parseDouble(rs.getString(5))).replace(',', '.'));
				objeBean.setCuotascredomatic(String.format("%.2f",Double.parseDouble(rs.getString(6))).replace(',', '.'));
				objeBean.setVisa(String.format("%.2f",Double.parseDouble(rs.getString(7))).replace(',', '.'));
				objeBean.setCredomatic(String.format("%.2f",Double.parseDouble(rs.getString(8))).replace(',', '.'));
				objeBean.setOtras(String.format("%.2f",Double.parseDouble(rs.getString(9))).replace(',', '.'));
				objeBean.setCheque(String.format("%.2f",Double.parseDouble(rs.getString(10))).replace(',', '.'));
				objeBean.setExcencioniva(String.format("%.2f",Double.parseDouble(rs.getString(11))).replace(',', '.'));
				objeBean.setDeposito(String.format("%.2f",Double.parseDouble(rs.getString(12))).replace(',', '.'));
				objeBean.setCreador(rs.getString(13));
				objeBean.setFechacreacion(rs.getString(14));
				list.add(objeBean);
			}
		}catch (Exception e) {
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
				}
	}
		return list;
	}
	
	/*
	 * Este metodo de carga masiva aun no se ocupa en el sistema
	 * 
	 * */
	public int guarMasivo(String usuario, String fec_inicio, String fec_fin, int estado, String creador) throws SQLException, NamingException{
		int result = 0;
		Connection conn = null;
		CallableStatement cmd = null;
		try{
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			 cmd = conn.prepareCall("CALL CARGA_MASIVA_LIQUIDACIONES_PRC(?,?,?,?,?)");
			cmd.setString(1, usuario);
			cmd.setString(2, fec_inicio);
			cmd.setString(3, fec_fin);
			cmd.setInt(4, estado);
			cmd.setString(5, creador);
			if(cmd.execute())
				result = 1;
		}catch (Exception e) {
			Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBLiquidacionCajeroDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
}
