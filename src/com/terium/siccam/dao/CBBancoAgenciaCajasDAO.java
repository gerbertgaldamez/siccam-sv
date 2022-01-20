package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBBancoAgenciaCajasModel;
import com.terium.siccam.sql.ConsultasSQ;

public class CBBancoAgenciaCajasDAO {
	private static Logger log = Logger.getLogger(CBBancoAgenciaCajasDAO.class);
	
	public int obtenerPKCajas() {
		String idCajasSQ = ConsultasSQ.SEQ_CAJERO;
		int pk = 0;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Query secuencia cajas SQ = " + idCajasSQ);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
					//"Query secuencia cajas SQ = " + idCajasSQ);
			cmd = conn.prepareStatement(idCajasSQ);
			rs = cmd.executeQuery();
			if (rs.next())
				pk = rs.getInt(1);
		} catch (SQLException e) {
			log.error("obtenerPKCajas() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerPKCajas() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("obtenerPKCajas() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("obtenerPKCajas() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return pk;
	}

	public boolean transaccionValida(CBBancoAgenciaCajasModel objModel) {
		String cajeroValidoSQL = ConsultasSQ.CAJERO_VALIDO;
		boolean result = true;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Valor objeto objModel = " + objModel.toString());
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
					//"Valor objeto objModel = " + objModel.toString());
			log.debug( "Query cajero valido = " + cajeroValidoSQL);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
					//"Query cajero valido = " + cajeroValidoSQL);
			cmd = conn.prepareStatement(cajeroValidoSQL);
			cmd.setString(1, objModel.getCajero());
			cmd.setInt(2, objModel.getCbcatalogoagenciaid());
			rs = cmd.executeQuery();
			if (rs.next()) {
				result = false;
			}
		} catch (SQLException e) {
			log.error("transaccionValida() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("transaccionValida() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("transaccionValida() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("transaccionValida() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	public boolean insertarCajero(CBBancoAgenciaCajasModel param, int pk) {
		String insertCajeroSQL = ConsultasSQ.INSERTAR_CAJERO;
		boolean result = false;
		PreparedStatement cmd = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Valor objeto param = " + param.toString());
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
				//	"Valor objeto param = " + param.toString());
			log.debug( "Valor pk = " + pk);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO, "Valor pk = " + pk);
			log.debug( "Insert cajero = " + insertCajeroSQL);
		//	Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
					//"Insert cajero = " + insertCajeroSQL);
			cmd = conn.prepareStatement(insertCajeroSQL);
			cmd.setInt(1, pk);
			cmd.setInt(2, param.getCbcatalogoagenciaid());
			cmd.setString(3, param.getCod_oficina());
			cmd.setString(4, param.getCod_caja());
			cmd.setString(5, param.getCajero());
			cmd.setInt(6, param.getEstadoCaja());
			cmd.setString(7, param.getCreador());
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			log.error("insertarCajero() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("insertarCajero() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("insertarCajero() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	public boolean actualizarCajero(CBBancoAgenciaCajasModel param, int pk) {
		String updateCajeroSQL = ConsultasSQ.ACTUALIZAR_CAJERO;
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Valor objeto param = " + param.toString());
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
				//	"Valor objeto param = " + param.toString());
			log.debug( "Valor pk = " + pk);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO, "Valor pk = " + pk);
			log.debug( "Update cajero = " + updateCajeroSQL);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
					//"Update cajero = " + updateCajeroSQL);
			cmd = conn.prepareStatement(updateCajeroSQL);
			cmd.setString(1, param.getCod_oficina());
			cmd.setString(2, param.getCod_caja());
			cmd.setString(3, param.getCajero());
			cmd.setInt(4, param.getEstadoCaja());
			cmd.setString(5, param.getCreador());
			cmd.setInt(6, pk);
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			log.error("actualizarCajero() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("actualizarCajero() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("actualizarCajero() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	public int eliminarCajero(int pk) {
		String deleteCajeroSQL = ConsultasSQ.ELIMINAR_CAJERO;
		int result = 0;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Valor pk = " + pk);
		//	Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO, "Valor pk = " + pk);
			log.debug( "Delete cajero = " + deleteCajeroSQL);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
					//"Delete cajero = " + deleteCajeroSQL);
			cmd = conn.prepareStatement(deleteCajeroSQL);
			cmd.setInt(1, pk);
			result = cmd.executeUpdate();
		} catch (SQLException e) {
			log.error("eliminarCajero() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("eliminarCajero() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("eliminarCajero() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	public List<CBBancoAgenciaCajasModel> consByIdAgencia(CBBancoAgenciaCajasModel objModel) {
		String consultaCajerosSQL = ConsultasSQ.CONSULTAR_CAJEROS;
		Connection conn = null;
		List<CBBancoAgenciaCajasModel> list = new ArrayList<CBBancoAgenciaCajasModel>();
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Valor objModel = " + objModel.toString());
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO, 
					//"Valor objModel = " + objModel.toString());
			log.debug( "Consulta cajeros = " + consultaCajerosSQL);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
				//	"Consulta cajeros = " + consultaCajerosSQL);
			cmd = conn.prepareStatement(consultaCajerosSQL);
			cmd.setInt(1, objModel.getCbcatalogoagenciaid());
			rs = cmd.executeQuery();
			CBBancoAgenciaCajasModel objeBean;
			while (rs.next()) {
				objeBean = new CBBancoAgenciaCajasModel();
				objeBean.setCbbancoagenciacajasid(rs.getInt(1));
				objeBean.setCbcatalogoagenciaid(rs.getInt(2));
				objeBean.setCod_oficina(rs.getString(3));
				objeBean.setCod_caja(rs.getString(4));
				objeBean.setCajero(rs.getString(5));
				objeBean.setEstadoCaja(rs.getInt(6));
				objeBean.setCreador(rs.getString(7));
				objeBean.setFechacreacion(rs.getString(8));
				list.add(objeBean);
			}
		} catch (Exception e) {
			log.error("consByIdAgencia() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("consByIdAgencia() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("consByIdAgencia() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("consByIdAgencia() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}
}
