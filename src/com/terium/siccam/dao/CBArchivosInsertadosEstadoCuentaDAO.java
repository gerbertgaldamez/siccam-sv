/**
 * 
 */
package com.terium.siccam.dao;

import java.sql.Connection;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;


import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosEstadoCuentaDAO;
import com.terium.siccam.model.CBArchivosInsertadosEstadoCuentaModel;
import com.terium.siccam.sql.ConsultasSQ;

/**
 * @author lab
 */
public class CBArchivosInsertadosEstadoCuentaDAO extends ControladorBase{
	
	private static Logger log = Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class);


	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public List<CBArchivosInsertadosEstadoCuentaModel> obtieneListaArchivosCargados(String fechaDesde, String fechaHasta){
		List<CBArchivosInsertadosEstadoCuentaModel> lista = new ArrayList<CBArchivosInsertadosEstadoCuentaModel>();
		ResultSet rs = null;
		Statement ps = null;
		Connection con = null;
		String archivosCargadosSQL = ConsultasSQ.CONSULTA_ARCHIVOS_CARGADOS_ESTADO_CUENTA;
		
			
			try {
				con = obtenerDtsPromo().getConnection();
				String where = "";
				if (fechaDesde != null && !"".equals(fechaDesde)) {
					where += "AND SWDATECREATED BETWEEN TO_DATE('" + fechaDesde + "', 'dd/MM/yyyy HH24:mi:ss') "
							+ " AND TO_DATE('" + fechaHasta + " 23:59:59" + "', 'dd/MM/yyyy HH24:mi:ss') ";
				}
				
				log.debug( "Query general consulta cargas estado cuentas = " + archivosCargadosSQL + where + " ORDER BY CBESTADOCUENTAARCHIVOSID");
				//Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName())
				//	.log(Level.INFO,"Query general consulta cargas estado cuentas = " + archivosCargadosSQL + where + " ORDER BY CBESTADOCUENTAARCHIVOSID");
				
				ps = con.createStatement();
				rs = ps.executeQuery(archivosCargadosSQL + where + " ORDER BY CBESTADOCUENTAARCHIVOSID");
				CBArchivosInsertadosEstadoCuentaModel obj = null;
				while(rs.next()){
					obj = new CBArchivosInsertadosEstadoCuentaModel();
					obj.setIdArchivosInsertados(rs.getString(1));
					obj.setNombreArchivo(rs.getString(2));
					obj.setTipoCarga(rs.getString(3));
					obj.setCreadoPor(rs.getString(4));
					obj.setFecha(rs.getString(5));
					lista.add(obj);
				}
		}catch (Exception e) {
			log.error("obtieneListaArchivosCargados() - Error ", e);
			//Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtieneListaArchivosCargados() - Error ", e);
					//Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtieneListaArchivosCargados() - Error ", e);
					//Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtieneListaArchivosCargados() - Error ", e);
					//Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
	}
		return lista;
	} 
	
	
/*
	// consulta archivos cargados
	public List<CBArchivosInsertadosEstadoCuentaModel> obtieneListaArchivosCargados(String fechaDesde, String fechaHasta) {
		String archivosCargadosSQL = ConsultasSQ.CONSULTA_ARCHIVOS_CARGADOS_ESTADO_CUENTA;
		List<CBArchivosInsertadosEstadoCuentaModel> listado = new ArrayList<CBArchivosInsertadosEstadoCuentaModel>();
	
		if (fechaDesde != null && !"".equals(fechaDesde)) {
			archivosCargadosSQL += "AND SWDATECREATED BETWEEN TO_DATE('" + fechaDesde + "', 'dd/MM/yyyy HH24:mi:ss') "
					+ " AND TO_DATE('" + fechaHasta + " 23:59:59" + "', 'dd/MM/yyyy HH24:mi:ss') ";
		}
		archivosCargadosSQL += " order by CBESTADOCUENTAARCHIVOSID desc";
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBArchivosInsertadosEstadoCuentaModel> bhl = new BeanListHandler<CBArchivosInsertadosEstadoCuentaModel>(
					CBArchivosInsertadosEstadoCuentaModel.class);
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.INFO, 
					"Parametros: "
					+ " | fechaDesde = " + fechaDesde + " | fechaHasta = " + fechaHasta);
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"Consulta archivos cargados = " + archivosCargadosSQL);
			listado = qr.query(con, archivosCargadosSQL, bhl, new Object[] {});
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	*/
	

	public boolean borraFilaGrabadaMaestro(String idMaestroCarga) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		String deleteArchivoMaestroSQL = ConsultasSQ.DELETE_ARCHIVO_MAESTRO_ESTADO_CUENTA;
		log.debug( "\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL);
	//	Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
		//.log(Level.INFO,"\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL);
			try {
				conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(deleteArchivoMaestroSQL);
				cmd.setString(1, idMaestroCarga);
			if(cmd.executeUpdate() > 0)
				result = true;
			log.debug( "\n*** Registro eliminado con exito ***\n");
			//	Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
					//.log(Level.INFO,"\n*** Registro eliminado con exito ***\n");
		}catch (Exception e) {
			log.error("borraFilaGrabadaMaestro() - Error ", e);
			//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				log.error("borraFilaGrabadaMaestro() - Error ", e);
				//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	
	public boolean borraRegistrosSociedad(String idMaestroCarga) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		String deleteArchivoMaestroSQL = ConsultasSQ.DELETE_REGISTROS_SOCIEDAD_ESTADO_CUENTA;
		log.debug( "\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL );
		//Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
		//.log(Level.INFO,"\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL );
			try {
				conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(deleteArchivoMaestroSQL);
				cmd.setString(1, idMaestroCarga);
			if(cmd.executeUpdate() > 0)
				result = true;
			log.debug( "\n*** Registro eliminado con exito ***\n");
				//Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
					//.log(Level.INFO,"\n*** Registro eliminado con exito ***\n");
		}catch (Exception e) {
			log.error("borraRegistrosSociedad() - Error ", e);
			//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				log.error("borraRegistrosSociedad() - Error ", e);
				//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	
	public boolean borraRegistrosCredomatic(String idMaestroCarga) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		String deleteArchivoMaestroSQL = ConsultasSQ.DELETE_REGISTROS_CREDOMATIC_ESTADO_CUENTA;
		log.debug( "\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL );
		//Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
		//.log(Level.INFO,"\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL );
			try {
				conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(deleteArchivoMaestroSQL);
				cmd.setString(1, idMaestroCarga);
			if(cmd.executeUpdate() > 0)
				result = true;
			log.debug( "\n*** Registro eliminado con exito ***\n");
				//Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
				//	.log(Level.INFO,"\n*** Registro eliminado con exito ***\n");
		}catch (Exception e) {
			log.error("borraRegistrosCredomatic() - Error ", e);
			//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				log.error("borraRegistrosCredomatic() - Error ", e);
				//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public boolean borraRegistrosOtras(String idMaestroCarga) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		String deleteArchivoMaestroSQL = ConsultasSQ.DELETE_REGISTROS_OTRAS_ESTADO_CUENTA;
		log.debug( "\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL );
	//	Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
		//.log(Level.INFO,"\n*** QUERY ELIMINAR: ***\n" + deleteArchivoMaestroSQL );
			try {
				conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(deleteArchivoMaestroSQL);
				cmd.setString(1, idMaestroCarga);
			if(cmd.executeUpdate() > 0)
				result = true;
			log.debug( "\n*** Registro eliminado con exito ***\n");
				//Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
					//.log(Level.INFO,"\n*** Registro eliminado con exito ***\n");
		}catch (Exception e) {
			log.error("borraRegistrosOtras() - Error ", e);
			//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				log.error("borraRegistrosOtras() - Error ", e);
				//Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	/*
	// Borra registro archivo maestro
	public void borraFilaGrabadaMaestro(String idMaestroCarga) {
		String deleteArchivoMaestroSQL = ConsultasSQ.DELETE_ARCHIVO_MAESTRO_ESTADO_CUENTA;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"Valor idMaestroCarga = " + idMaestroCarga);
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"Delete archivo maestro = " + deleteArchivoMaestroSQL);
			QueryRunner qr = new QueryRunner();
			qr.update(conn, deleteArchivoMaestroSQL, idMaestroCarga);
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}
*/
	/*

	// Genera el id maestro para el archivo
	public String idMaestroCarga() {
		String idMaestroCargaSQ = ConsultasSQ.ID_MAESTRO_CARGA;
		String res = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"Generacion nuevo id de archivos insertados = " + idMaestroCargaSQ);
			ps = con.prepareStatement(idMaestroCargaSQ);
			rs = ps.executeQuery();
			rs.next();
			res = rs.getString(1);
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.INFO, "El nuevo id es = " + res);
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return res;
	}
	*/
	
}