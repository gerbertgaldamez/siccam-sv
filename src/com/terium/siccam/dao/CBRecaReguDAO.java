package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBRecaReguModel;
import com.terium.siccam.utils.ConsultasSQ;

public class CBRecaReguDAO {
	
	/**
	 * Se consulta 
	 * @param objModel
	 * @param limiteInicial = limite inicial de la consulta (mejora performance)
	 * @param limiteFinal = limite final de la consulta (mejora performance)
	 * */
	public List<CBRecaReguModel> obtenerPagos(CBRecaReguModel objModel /*, int limiteInicial, int limiteFinal*/) {
		List<CBRecaReguModel> lst = new ArrayList<CBRecaReguModel>();
		Statement stmt = null;
		ResultSet rst = null;
		Connection conn = null;
		CBRecaReguModel obj = null;
		String query = null;
		try {
			long startTime = System.nanoTime();
			query = ConsultasSQ.CONSULTA_RECAREGU_PAGOS_SQ;
			String where = "";
			//String limites = "";
			where += "AND FECHA_PAGO >= TO_DATE ('" + objModel.getFechainicio() + "', 'dd/MM/yyyy') "
					+ "AND FECHA_PAGO <= TO_DATE ('" + objModel.getFechafin() + "', 'dd/MM/yyyy') ";
			
			if (!"".equals(objModel.getCodCliente())) {
				where += "AND COD_CLIENTE = " + objModel.getCodCliente() + " ";
			}
			if (!"".equals(objModel.getNomusuarora())) {
				where += "AND TRIM (UPPER (NOM_USUARORA)) = '" + objModel.getNomusuarora() + "' ";
			}
			where += "AND ROWNUM <= 100";
			//limites += "ORDER BY CBPAGOSID) a WHERE ROWNUM <= " + limiteFinal + ") " + "WHERE rnum >= " + limiteInicial
			//		+ "";
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			stmt = conn.createStatement();
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, query + where);
			rst = stmt.executeQuery(query + where);
			while (rst.next()) {
				obj = new CBRecaReguModel();
				
				obj.setCbpagosid(rst.getInt(1));
				obj.setFechaEfectiva(rst.getString(2));
				obj.setFechaPago(rst.getString(3));
				obj.setCodCliente(rst.getString(4));
				obj.setNomCliente(rst.getString(5));
				obj.setNumSecuenci(rst.getString(6));
				obj.setImpPago(rst.getBigDecimal(7));
				obj.setTransaccion(rst.getString(8));
				obj.setTipoTransaccion(rst.getString(9));
				obj.setCodOficina(rst.getString(10));
				obj.setDesOficina(rst.getString(11));
				obj.setTipoMovCaja(rst.getString(12));
				obj.setDesMovCaja(rst.getString(13));
				obj.setTipoValor(rst.getString(14));
				obj.setDesTipoValor(rst.getString(15));
				obj.setNomusuarora(rst.getString(16));
				obj.setCodBanco(rst.getString(17));
				obj.setNombreBanco(rst.getString(18));
				obj.setCodCaja(rst.getString(19));
				obj.setDesCaja(rst.getString(20));
				obj.setObservacion(rst.getString(21));

				lst.add(obj);
			}
			long endTime = System.nanoTime();
			long totalTime = (endTime - startTime) / 1000000000;
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO,
					"Tiempo de performance CBRecaReguDAO = " + totalTime + " s");
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, "List.size = " + lst.size());
		} catch (Exception e) {
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rst != null)
				try {
					rst.close();
				} catch (SQLException e) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lst;
	}
	
	/**
	 * 
	 * */
	public boolean actualizaPagos(int banderaOperacion, String nomusuario, int cbpagosid){
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			
			if(banderaOperacion == 0) { 
				/**
				 * MANEJO DE BANCOS
				 */
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, ConsultasSQ.UPDATE_PAGO_RECAREGU_QR2);	
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, "COD_CAJA = " + nomusuario);
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, "CBPAGOSID = " + cbpagosid);	
				pstmt = conn.prepareStatement(ConsultasSQ.UPDATE_PAGO_RECAREGU_QR2);
			} else { 
				/**
				 * MANEJO DE TIENDAS PROPIAS
				 */
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, ConsultasSQ.UPDATE_PAGO_RECAREGU_QR1);	
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, "USUARIO = " + nomusuario);
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, "CBPAGOSID = " + cbpagosid);	
				pstmt = conn.prepareStatement(ConsultasSQ.UPDATE_PAGO_RECAREGU_QR1);
			}
			pstmt.setString(1, nomusuario);
			pstmt.setInt(2, cbpagosid);
	
			if (pstmt.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (Exception ex) {			
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}finally {
			try {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
	/**
	 * Agregado por CarlosGodinez -> 09/08/2018
	 * */
	public List<CBCatalogoAgenciaModel> obtenerEntidades(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			pstmt = conn.prepareStatement(ConsultasSQ.ENTIDADES_RECAREGU_QRY);
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, ConsultasSQ.ENTIDADES_RECAREGU_QRY);
			rs = pstmt.executeQuery();
			CBCatalogoAgenciaModel objeBean;
			while (rs.next()) {
				objeBean = new CBCatalogoAgenciaModel();
				objeBean.setcBCatalogoAgenciaId(rs.getString(1));
				objeBean.setNombre(rs.getString(2));
				list.add(objeBean);
			}
		}catch (Exception e) {
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	public List<CBRecaReguModel> obtenerUsuarios(int identidad){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CBRecaReguModel> list = new ArrayList<CBRecaReguModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			pstmt = conn.prepareStatement(ConsultasSQ.USUARIOS_RECAREGU_QRY);
			pstmt.setInt(1, identidad); 
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, ConsultasSQ.USUARIOS_RECAREGU_QRY);
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, "Entidad = " + identidad);
			rs = pstmt.executeQuery();
			CBRecaReguModel objeBean;
			while (rs.next()) {
				objeBean = new CBRecaReguModel();
				objeBean.setNomusuarora(rs.getString(1));
				list.add(objeBean);
			}
		}catch (Exception e) {
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	
	/**
	 * Agrega CarlosGodinez -> 21/08/2018
	 * Obtencion de bancos
	 * */
	public List<CBRecaReguModel> obtenerBancos() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CBRecaReguModel> list = new ArrayList<CBRecaReguModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			pstmt = conn.prepareStatement(ConsultasSQ.BANCOS_RECAREGU_QRY);
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.INFO, ConsultasSQ.BANCOS_RECAREGU_QRY);
			rs = pstmt.executeQuery();
			CBRecaReguModel objeBean;
			while (rs.next()) {
				objeBean = new CBRecaReguModel();
				objeBean.setNombreBanco(rs.getString(2));
				objeBean.setCodAgencia(rs.getString(3));
				list.add(objeBean);
			}
		}catch (Exception e) {
			Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBRecaReguDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
}
