package com.terium.siccam.dao;

import java.math.BigDecimal;
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
import com.terium.siccam.model.CBAsignaImpuestosModel;
import com.terium.siccam.model.CBDetalleComisionesModel;
import com.terium.siccam.model.CBHistorialSCECModel;
import com.terium.siccam.utils.ConsultasSQ;

/**
 * @author Juankrlos
 * 
 * */

public class CBHistorialSCECDAO {

	
	/**
	 * Se extrae la informacion de la tipificacion por entidad
	 * @param cbbancoagenciaconfrontaid : Id de la configuracion de confronta asignada a cada agencia
	 * @param fecha : Fecha del pago en el estado de cuenta o sistema comercial
	 * 
	 * */
	public List<CBHistorialSCECModel> obtenerCBHistorialTipificacion(int cbbancoagenciaconfrontaid, String fecha){
		List<CBHistorialSCECModel> lst = new ArrayList<CBHistorialSCECModel>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		Connection conn = null;
		CBHistorialSCECModel obj = null;
		System.out.println("parametros em dao: " + cbbancoagenciaconfrontaid );
		System.out.println("parametros em dao2: " + fecha );
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			
			pstmt = conn.prepareStatement(ConsultasSQ.OBTIENE_HISTORIAL_TIPIFICACION_SQ);
			pstmt.setInt(1, cbbancoagenciaconfrontaid);
			pstmt.setString(2, fecha);
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE,ConsultasSQ.OBTIENE_HISTORIAL_TIPIFICACION_SQ);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				obj = new CBHistorialSCECModel();
				obj.setCbhistorialscecid(rst.getInt(1));
				obj.setCbestadocuentasociedadid(rst.getInt(2));
				System.out.println("valor pagois en dao:" + obj.getCbpagosid());
				obj.setCbpagosid(rst.getInt(3));
				obj.setCbbancoagenciaconfrontaid(rst.getInt(4));
				obj.setCbcausasconciliacionid(rst.getInt(5));
				System.out.println("valor causas en dao:" + obj.getCausas());
				obj.setCausas(rst.getString(6));
				System.out.println("valor monto en dao:" + obj.getMonto());
				obj.setMonto(rst.getBigDecimal(7));
				System.out.println("valor monto en dao:" + obj.getMonto());
				obj.setFecha(rst.getString(8));
				obj.setObservacion(rst.getString(9));
				obj.setCreadopor(rst.getString(10));
				obj.setModificadopor(rst.getString(11));
				obj.setFechacreacion(rst.getString(12));
				obj.setFechamodificacion(rst.getString(13));

				lst.add(obj);
			}

		} catch (Exception e) {
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rst != null)
					try {
						rst.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return lst;
	}
	
	
	/**
	 * Se obtienen las tipologias de conciliacion (Causas de conciliacion)
	 * 
	 * */
	public List<CBHistorialSCECModel> obtieneCausasConciliacion() {
		List<CBHistorialSCECModel> listado = new ArrayList<CBHistorialSCECModel>();
		
		Connection con = null;
		ResultSet rst = null;
		Statement stmt = null;
		CBHistorialSCECModel obj =null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			stmt  = con.createStatement();
			Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE,ConsultasSQ.OBTIENE_CAUSAS_CONCILIACION_BANCOS_SQ);
			rst = stmt.executeQuery(ConsultasSQ.OBTIENE_CAUSAS_CONCILIACION_BANCOS_SQ);
			while(rst.next()){
				obj = new CBHistorialSCECModel();

				obj.setCbcausasconciliacionid(rst.getInt(1));
				obj.setCausas(rst.getString(2));
				obj.setCreadopor(rst.getString(3));
				obj.setFechacreacion(rst.getString(4));
				obj.setCodigoconciliacion(rst.getString(5));
				obj.setTipo(rst.getInt(6));
				listado.add(obj);
			}
			
		} catch (Exception e) {
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rst != null)
				try {
					rst.close();
				} catch (SQLException e) {
					Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					if(con != null)
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return listado;
	}
	
	public boolean ingresaTipificacion(CBHistorialSCECModel obj){
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.INFO, ConsultasSQ.INSERT_CAUSA_TIPIFICACION_QY);		
			pstmt = conn.prepareStatement(ConsultasSQ.INSERT_CAUSA_TIPIFICACION_QY);
			pstmt.setInt(1, obj.getCbbancoagenciaconfrontaid());
			pstmt.setInt(2, obj.getCbcausasconciliacionid());
			pstmt.setBigDecimal(3, obj.getMonto());
			pstmt.setString(4, obj.getFecha());
			pstmt.setString(5, obj.getObservacion());
			pstmt.setString(6, obj.getCreadopor());
			
			if (pstmt.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (Exception ex) {			
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}finally {
			try {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}

	public boolean actualizaTipificacion(CBHistorialSCECModel obj){
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.INFO, ConsultasSQ.UPDATE_CAUSA_TIPIFICACION_QY);		
			pstmt = conn.prepareStatement(ConsultasSQ.UPDATE_CAUSA_TIPIFICACION_QY);
			
			pstmt.setInt(1, obj.getCbcausasconciliacionid());
			pstmt.setBigDecimal(2, obj.getMonto());
			pstmt.setString(3, obj.getObservacion());
			pstmt.setString(4, obj.getModificadopor());
			pstmt.setInt(5, obj.getCbhistorialscecid());
			
			if (pstmt.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (Exception ex) {			
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}finally {
			try {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}

	
	/**
	 * @param bean : Manejo de campos para la tabla perfil persona
	 * */
	public boolean eliminaHistorial(int cbhistorialscecid) {
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			pstm = conn.prepareStatement(ConsultasSQ.ELIMINAR_CAUSAS_CONCILIACION_QY);		
			pstm.setInt(1, cbhistorialscecid);
						
			if(pstm.executeUpdate() > 0) {
				return true;
			} else {				
				return false;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}finally {
			try {
				if(pstm != null)
					try {
						pstm.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
	
	//metodos modal
	
	/**
	 * Se extrae la informacion de la tipificacion por entidad
	 * @param cbbancoagenciaconfrontaid : Id de la configuracion de confronta asignada a cada agencia
	 * @param fecha : Fecha del pago en el estado de cuenta o sistema comercial
	 * 
	 * */
	public List<CBDetalleComisionesModel> obtenerDetalleComision(int cbbancoagenciaconfrontaid, String fecha){
		List<CBDetalleComisionesModel> lst = new ArrayList<CBDetalleComisionesModel>();
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		Connection conn = null;
		CBDetalleComisionesModel obj = null;
		System.out.println("parametros em dao: " + cbbancoagenciaconfrontaid );
		System.out.println("parametros em dao2: " + fecha );
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			
			pstmt = conn.prepareStatement(ConsultasSQ.OBTIENE_DETALLE_COMISION_SQ);
			System.out.println("query:" + ConsultasSQ.OBTIENE_DETALLE_COMISION_SQ );
			
			System.out.println("filtros:" + cbbancoagenciaconfrontaid + fecha  );
			
			pstmt.setInt(1, cbbancoagenciaconfrontaid);
			pstmt.setString(2, fecha);
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE,ConsultasSQ.OBTIENE_DETALLE_COMISION_SQ);
			rst = pstmt.executeQuery();
			while (rst.next()) {
				obj = new CBDetalleComisionesModel();
				obj.setCbcomisionid(rst.getInt(1));
				obj.setNombreFormaPago (rst.getString(2));
				obj.setNombreTipo(rst.getString(3));
				obj.setNombreTipologia (rst.getString(4));
				obj.setNombreImpuesto(rst.getString(5));
				obj.setNombreMedioPago(rst.getString(6));
				obj.setMonto(rst.getBigDecimal(7));
				//obj.setComisionReal(rst.getBigDecimal(7));
				System.out.println("valor monto en dao:" + obj.getMonto());
				
				lst.add(obj);
			}

		} catch (Exception e) {
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rst != null)
					try {
						rst.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return lst;
	}
	/*public boolean ingresaComision(CBHistorialSCECModel obj){
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.INFO, ConsultasSQ.INSERT_COMISIONES_QY);		
			pstmt = conn.prepareStatement(ConsultasSQ.INSERT_COMISIONES_QY);
			pstmt.setInt(1, obj.getCbbancoagenciaconfrontaid());
			//pstmt.setBigDecimal(2, obj.getMonto());
			pstmt.setString(2, obj.getFecha());
			pstmt.setString(3, obj.getCreadopor());
			pstmt.setBigDecimal(4, obj.getComisionReal());
			
			if (pstmt.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (Exception ex) {			
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}finally {
			try {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}*/
	public boolean actualizaComisionReal(BigDecimal comisionReal, int comisionid){
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.INFO, ConsultasSQ.ACTUALIZA_COMISION_REAL);		
			pstmt = conn.prepareStatement(ConsultasSQ.ACTUALIZA_COMISION_REAL);
			
			
			pstmt.setBigDecimal(1, comisionReal);
			pstmt.setInt(2, comisionid);
			
			
			if (pstmt.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (Exception ex) {			
			Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}finally {
			try {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(CBHistorialSCECDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}

	
}
