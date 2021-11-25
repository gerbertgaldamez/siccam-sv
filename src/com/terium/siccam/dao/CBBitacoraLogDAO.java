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
import com.terium.siccam.model.CBBitacoraLogModel;
import com.terium.siccam.model.CBDetalleComisionesModel;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.ConsultasSQ;

public class CBBitacoraLogDAO extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBBitacoraLogDAO.class);
	/**
	 * Added by CarlosGodinez -> 19/09/2018 
	 * Auditar cualquier accion realizada en SICCAM
	 */
	public boolean insertBitacoraLog(CBBitacoraLogModel objBitaModel) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		
		
		try {
			conn = obtenerDtsPromo().getConnection();
			log.debug( "Parametros: modulo =" + objBitaModel.getModulo()+ " | tipoCarga =" + objBitaModel.getTipoCarga()
					+ " | nombreArchivo =" + objBitaModel.getNombreArchivo() + " | accion = "
							+ objBitaModel.getAccion() + " | usuario = " + objBitaModel.getUsuario());
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
				//	"Parametros: modulo = " + objBitaModel.getModulo() + " | tipoCarga = " + objBitaModel.getTipoCarga()
						//	+ " | nombreArchivo = " + objBitaModel.getNombreArchivo() + " | accion = "
						//+ objBitaModel.getAccion() + " | usuario = " + objBitaModel.getUsuario());
			log.debug( "Insert SQL bitacora log " + ConsultasSQ.INSERT_BITACORA_LOG);
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
				//	"Insert SQL bitacora log = " + ConsultasSQ.INSERT_BITACORA_LOG);

			cmd = conn.prepareStatement(ConsultasSQ.INSERT_BITACORA_LOG);
			cmd.setString(1, objBitaModel.getModulo());
			cmd.setString(2, objBitaModel.getTipoCarga());
			cmd.setString(3, objBitaModel.getNombreArchivo());
			cmd.setString(4, objBitaModel.getAccion());
			cmd.setString(5, objBitaModel.getUsuario());

			if (cmd.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("insertBitacoraLog() - Error ", e);
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("insertBitacoraLog() - Error ", e);
					//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("insertBitacoraLog() - Error ", e);
					//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	
	/*
	 * Permite actualizar el log para los hilos
	 * 
	 * */
	public boolean updateBitacoraThread(String token, String pais, int estado) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		log.debug( "Entra a updateBitacoraThread " );
		//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
				//"Entra a updateBitacoraThread");
		String Query2 = " ";
		Query2 += "UPDATE CB_BITACORA SET TIPO_CARGA = " + estado + " WHERE NOMBRE_ARCHIVO = '" + token + "'";
		try {
			System.out.println("obteniendo conexion en el bitacora dao");
			
			conn = obtenerDtsPromo().getConnection();
			
			System.out.println("si la obtuvo");
			log.debug( "Parametros: nombre archivo(tokenItem) = " + token);
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
				//	"Parametros: nombre archivo(tokenItem) = " + token);
			log.debug( "Update SQL log = " + ConsultasSQ.UPDATE_LOG_THREAD);
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
				//	"Update SQL log = " + ConsultasSQ.UPDATE_LOG_THREAD);
//			cmd = conn1.prepareStatement(ConsultasSQ.UPDATE_LOG_THREAD);

			cmd = conn.prepareStatement(Query2);
//			cmd.setInt(1, estado);
	//		cmd.setString(2, token);

			if (cmd.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("updateBitacoraThread() - Error ", e);
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
			//updateBitacoraThread(token, pais, estado);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("updateBitacoraThread() - Error ", e);
					//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("updateBitacoraThread() - Error ", e);
					//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	public int countBitacoraThread(String token, String pais) {
		int result = 0;
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			
			conn = obtenerDtsPromo().getConnection();
			log.debug( "Parametros: modulo(token) = " + token);
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
				//	"Parametros: modulo(token) = " + token);
			log.debug( "Query = " + ConsultasSQ.COUNT_LOG_THREAD);
			//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
				//	"Query = " + ConsultasSQ.COUNT_LOG_THREAD);

			cmd = conn.prepareStatement(ConsultasSQ.COUNT_LOG_THREAD);
			cmd.setString(1, token);

			rs = cmd.executeQuery();
			while(rs.next()) {
				result = rs.getInt(1);
			}
			log.debug( "Cantidad de hilos completados: " + result);
			
				
		} catch (Exception e) {
			log.error("countBitacoraThread() - Error ", e);
			///Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("countBitacoraThread() - Error ", e);
					//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("countBitacoraThread() - Error ", e);
					//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("countBitacoraThread() - Error ", e);
					//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	
	//metodos modal
	
		/**
		 * Se extrae la informacion de la tipificacion por entidad
		 * @param cbbancoagenciaconfrontaid : Id de la configuracion de confronta asignada a cada agencia
		 * @param fecha : Fecha del pago en el estado de cuenta o sistema comercial
		 * 
		 * */
		public List<CBBitacoraLogModel> obtenerDetalleContabilizacion( String token, String pais, Boolean inicio){
			List<CBBitacoraLogModel> lst = new ArrayList<CBBitacoraLogModel>();
			PreparedStatement pstmt = null;
			ResultSet rst = null;
			Connection conn = null;
			CBBitacoraLogModel obj = null;
			String query = "";
			try {
				conn = obtenerDtsPromo().getConnection();
				query = ConsultasSQ.OBTIENE_DETALLE_CONTABILIZACION_SQ;
				
				if(inicio) {
					query = query.concat("AND CREATEDBY = 'CARGA_CONTABILIZACION' ORDER BY CBBITACORAID ASC");
					pstmt = conn.prepareStatement(query);
					
					log.debug( "query: " + query);
				}else {
					query = query.concat("AND MODULO = ? ORDER BY CBBITACORAID ASC");
					pstmt = conn.prepareStatement(query);
					System.out.println("query:" + query );
					pstmt.setString(1, token);
				}
				
				log.debug( "query detalle contabilizacion: " + ConsultasSQ.OBTIENE_DETALLE_CONTABILIZACION_SQ);
				//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE,ConsultasSQ.OBTIENE_DETALLE_CONTABILIZACION_SQ);
				rst = pstmt.executeQuery();
				while (rst.next()) {
					obj = new CBBitacoraLogModel();
					obj.setTipoCarga (rst.getString(1));
					obj.setAccion(rst.getString(2));
					
					lst.add(obj);
				}

			} catch (Exception e) {
				log.error("obtenerDetalleContabilizacion() - Error ", e);
				//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
			}finally {
					if(rst != null)
						try {
							rst.close();
						} catch (SQLException e) {
							log.error("obtenerDetalleContabilizacion() - Error ", e);
							//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					if(pstmt != null)
						try {
							pstmt.close();
						} catch (SQLException e) {
							log.error("obtenerDetalleContabilizacion() - Error ", e);
							//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					if(conn != null)
						try {
							conn.close();
						} catch (SQLException e) {
							log.error("obtenerDetalleContabilizacion() - Error ", e);
							//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
						}
			}
			return lst;
		}
		
		public boolean deleteBitacoraThread(String user) {
			boolean result = false;
			Connection conn = null;
			PreparedStatement cmd = null;
			try {
				conn = ControladorBase.obtenerDtsPromo().getConnection();
				log.debug( "Parametros: usuario = " + user);
				//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
					//	"Parametros: usuario = " + user);
				log.debug( "Delte SQL log = " + ConsultasSQ.DELETE_LOG_THREAD);
				//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.INFO,
					//	"Delte SQL log = " + ConsultasSQ.DELETE_LOG_THREAD);

				cmd = conn.prepareStatement(ConsultasSQ.DELETE_LOG_THREAD);
				cmd.setString(1, user);

				if (cmd.executeUpdate() > 0) {
					result = true;
				}
			} catch (Exception e) {
				log.error("deleteBitacoraThread() - Error ", e);
				//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
				deleteBitacoraThread(user);
			} finally {
				if (cmd != null)
					try {
						cmd.close();
					} catch (SQLException e) {
						log.error("deleteBitacoraThread() - Error ", e);
						//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						log.error("deleteBitacoraThread() - Error ", e);
						//Logger.getLogger(CBBitacoraLogDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}
			return result;
		}
	
}
