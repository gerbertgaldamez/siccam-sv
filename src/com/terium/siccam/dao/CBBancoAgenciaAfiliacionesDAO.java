package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBBancoAgenciaAfiliacionesModel;
import com.terium.siccam.sql.ConsultasSQ;

public class CBBancoAgenciaAfiliacionesDAO  {
	
	public int obtenerPKAfiliacion() {
		String idAfiliacionSQ = ConsultasSQ.SEQ_AFILIACION;
		int pk = 0;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Consulta secuencia afiliacion SQ = " + idAfiliacionSQ);
			cmd = conn.prepareStatement(idAfiliacionSQ);
			rs = cmd.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
		} catch (Exception e) {
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return pk;
	}

	public boolean transaccionValida(CBBancoAgenciaAfiliacionesModel objModel) {
		String afiliacionValidaSQL = ConsultasSQ.AFILIACION_VALIDA;
		boolean result = true;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Valor objeto objModel = " + objModel.toString());
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Consulta afiliacion valida = " + afiliacionValidaSQL);
			cmd = conn.prepareStatement(afiliacionValidaSQL);
			cmd.setString(1, objModel.getAfiliacion());
			cmd.setInt(2, objModel.getCbcatalogoagenciaid());
			rs = cmd.executeQuery();
			if (rs.next())
				result = false;
		} catch (Exception e) {
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	public boolean insertarAfiliacion(CBBancoAgenciaAfiliacionesModel param, int pk) {
		String insertAfiliacionSQL = ConsultasSQ.INSERTAR_AFILIACION;
		boolean result = false;
		PreparedStatement cmd = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Valor objeto param = " + param.toString());
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO, "Valor pk = " + pk);
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Insert afiliacion = " + insertAfiliacionSQL);
			cmd = conn.prepareStatement(insertAfiliacionSQL);
			cmd.setInt(1, pk);
			cmd.setInt(2, param.getCbcatalogoagenciaid());
			cmd.setString(3, param.getTipo());
			cmd.setString(4, param.getAfiliacion());
			cmd.setInt(5, param.getEstado());
			cmd.setString(6, param.getUsuario());
			if (cmd.executeUpdate() > 0)
				result = true;

		} catch (SQLException e) {
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	public boolean actualizarAfiliacion(CBBancoAgenciaAfiliacionesModel param, int idSeleccionado) {
		String updateAfiliacionSQL = ConsultasSQ.ACTUALIZAR_AFILIACION;
		boolean result = false;
		PreparedStatement cmd = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Valor objeto param = " + param.toString());
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Valor idSeleccionado = " + idSeleccionado);
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Update afiliacion = " + updateAfiliacionSQL);
			cmd = conn.prepareStatement(updateAfiliacionSQL);
			cmd.setString(1, param.getTipo());
			cmd.setString(2, param.getAfiliacion());
			cmd.setInt(3, param.getEstado());
			cmd.setString(4, param.getCreador());
			cmd.setInt(5, idSeleccionado);
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (SQLException e) {
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	public boolean eliminarAfiliacion(int pk) {
		String deleteAfiliacionSQL = ConsultasSQ.ELIMINAR_AFILIACION;
		boolean result = false;
		PreparedStatement cmd = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO, "Valor pk = " + pk);
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Delete afiliacion = " + deleteAfiliacionSQL);
			cmd = conn.prepareStatement(deleteAfiliacionSQL);
			cmd.setInt(1, pk);
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (SQLException e) {
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	public List<CBBancoAgenciaAfiliacionesModel> consByIdAgencia(CBBancoAgenciaAfiliacionesModel objeBean) {
		String consultaAfiliacionesSQL = ConsultasSQ.CONSULTAR_AFILIACIONES;
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBBancoAgenciaAfiliacionesModel> list = new ArrayList<CBBancoAgenciaAfiliacionesModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Valor objeto objeBean = " + objeBean.toString());
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					"Consulta afiliaciones = " + consultaAfiliacionesSQL);
			cmd = conn.prepareStatement(consultaAfiliacionesSQL);
			cmd.setInt(1, objeBean.getCbcatalogoagenciaid());
			rs = cmd.executeQuery();
			while (rs.next()) {
				objeBean = new CBBancoAgenciaAfiliacionesModel();
				objeBean.setCbbancoagenciaafiliacionesid(rs.getInt(1));
				objeBean.setCbcatalogoagenciaid(rs.getInt(2));
				objeBean.setTipo(rs.getString(3));
				objeBean.setAfiliacion(rs.getString(4));
				objeBean.setEstado(rs.getInt(5));
				objeBean.setCreador(rs.getString(6));
				objeBean.setFechaCreacion(rs.getString(7));
				list.add(objeBean);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}
}
