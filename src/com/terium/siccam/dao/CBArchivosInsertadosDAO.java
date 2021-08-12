/**
 * 
 */
package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;
import com.terium.siccam.model.CBArchivosInsertadosModel;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.sql.ConsultasSQ;

/**
 * @author lab
 */
public class CBArchivosInsertadosDAO {

	//Inserta en tabla para la consulta de archivos cargados
	public boolean insertarArchivos(String idMaestro, String nombreArchivo, int idBanco, 
			int idAgencia, String usuario) {
		String insertarArchivosSQL = ConsultasSQ.INSERTAR_ARCHIVOS;
		boolean resultado = true;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Parametros: idMaestro = " + idMaestro + " | nombreArchivo = " + nombreArchivo 
					+ " | idBanco = " + idBanco + " | idAgencia = " + idAgencia + " | usuario = " + usuario);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Query insertarArchivosSQL = " + insertarArchivosSQL);

			String where = " ";
			where = " VALUES (" + idMaestro + ",'" + nombreArchivo + "'," + idBanco + "," + idAgencia +  ", sysdate,'" + usuario + "',sysdate )";			
			
			QueryRunner qr = new QueryRunner();
			qr.update(con, insertarArchivosSQL + where );

		} catch (Exception e) {
			resultado = false;
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	// Obtiene ultimo id de la tabla
	public String obtieneIdConArchivo(String nombreArchivo) {
		String maxIdArchivoSQL = ConsultasSQ.CONSULTA_ID_ARCHIVO;
		String resultado = "";
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Valor nombreArchivo = " + nombreArchivo);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Query max id archivos insertados = " + maxIdArchivoSQL);
			QueryRunner qr = new QueryRunner();
			ScalarHandler sh = new ScalarHandler();
			resultado = qr.query(con, maxIdArchivoSQL, sh, new Object[] { nombreArchivo }).toString();
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	// consulta archivos cargados
	public List<CBArchivosInsertadosModel> obtieneListaArchivosCargados(String banco, String agencia, 
			String fechaDesde, String fechaHasta) {
		String archivosCargadosSQL = ConsultasSQ.CONSULTA_ARCHIVOS_CARGADOS2;
		List<CBArchivosInsertadosModel> listado = new ArrayList<CBArchivosInsertadosModel>();
		if (banco != null && !"".equals(banco)) {
			archivosCargadosSQL += "and banco = '" + banco + "' ";
		}
		if (agencia != null && !"".equals(agencia)) {
			archivosCargadosSQL += "and agencia = '" + agencia + "' ";
		}
		// if (fechaDesde != null && !"".equals(fechaDesde)) {
		// CONSULTA_ARCHIVOS_CARGADOS += "and to_char(FECHA, 'dd/MM/yyyy') >= '"
		// + fechaDesde
		// + "' and to_char(FECHA, 'dd/MM/yyyy') <= '"
		// + fechaHasta + "' ";
		// }
		if (fechaDesde != null && !"".equals(fechaDesde)) {
			archivosCargadosSQL += "AND FECHA_CREACION BETWEEN TO_DATE('" + fechaDesde + "', 'dd/MM/yyyy HH24:mi:ss') "
					+ " AND TO_DATE('" + fechaHasta + " 23:59:59" + "', 'dd/MM/yyyy HH24:mi:ss') ";
		}
		archivosCargadosSQL += " order by id_archivos_insertados desc";
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBArchivosInsertadosModel> bhl = new BeanListHandler<CBArchivosInsertadosModel>(
					CBArchivosInsertadosModel.class);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO, 
					"Parametros: banco = " + banco + " | agencia = " + agencia 
					+ " | fechaDesde = " + fechaDesde + " | fechaHasta = " + fechaHasta);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Consulta archivos cargados = " + archivosCargadosSQL);
			listado = qr.query(con, archivosCargadosSQL, bhl, new Object[] {});
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	// Consulta detalle de archivos grabados
	public List<CBDataBancoModel> obtieneListadoGrabados(String idMaestroCarga) {
		String detalleGrabadosSQL = ConsultasSQ.CONSULTA_DETALLE_GRABADOS;
		List<CBDataBancoModel> listado = new ArrayList<CBDataBancoModel>();
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Valor idMaestroCarga = " + idMaestroCarga);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Consulta detalle grabados = " + detalleGrabadosSQL);
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBDataBancoModel> bhl = new BeanListHandler<CBDataBancoModel>(CBDataBancoModel.class);
			listado = qr.query(con, detalleGrabadosSQL, bhl, new Object[] { idMaestroCarga });
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	// Consulta detalle de archivos no grabados
	public List<CBDataSinProcesarModel> obtieneListadoDatosNoProcesados(String idMaestroCarga) {
		String detalleNoGrabadosSQL = ConsultasSQ.CONSULTA_DETALLE_NO_GRABADOS;
		List<CBDataSinProcesarModel> listado = new ArrayList<CBDataSinProcesarModel>();
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Valor idMaestroCarga = " + idMaestroCarga);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Consulta detalle no grabados = " + detalleNoGrabadosSQL);
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBDataSinProcesarModel> bhl = new BeanListHandler<CBDataSinProcesarModel>(
					CBDataSinProcesarModel.class);
			listado = qr.query(con, detalleNoGrabadosSQL, bhl, new Object[] { idMaestroCarga });
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}
	
	// Borra registro archivo maestro
	public void borraFilaGrabadaMaestro(String idMaestroCarga) {
		String deleteArchivoMaestroSQL = ConsultasSQ.DELETE_ARCHIVO_MAESTRO;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Valor idMaestroCarga = " + idMaestroCarga);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Delete archivo maestro = " + deleteArchivoMaestroSQL);
			QueryRunner qr = new QueryRunner();
			qr.update(conn, deleteArchivoMaestroSQL, idMaestroCarga);
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	// Borra registro databanco
	public boolean borraDataBancoMaestro(String idMaestroCarga) {
		String deleteDataBancoSQL = ConsultasSQ.DELETE_DATA_BANCO;
		Connection conn = null;
		boolean resultado = true;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Valor idMaestroCarga = " + idMaestroCarga);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Delete data banco = " + deleteDataBancoSQL);
			QueryRunner qr = new QueryRunner();
			qr.update(conn, deleteDataBancoSQL, idMaestroCarga);

		} catch (Exception e) {
			resultado = false;
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}
		
	// Borra registro conciliacion
	public boolean borraConciliacionMaestro(String idMaestroCarga) {
		String deleteConciliacionMaestro = ConsultasSQ.BORRA_CONCILIACION_MAESTRO;
		Connection conn = null;
		boolean resultado = true;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Valor idMaestroCarga = " + idMaestroCarga);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Delete conciliacion maestro = " + deleteConciliacionMaestro);
			QueryRunner qr = new QueryRunner();
			qr.update(conn, deleteConciliacionMaestro, idMaestroCarga);
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
			resultado = false;
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}

	// Borra registro data sin procesar
	public boolean borraDatasinProcesarMaestro(String idMaestroCarga) {
		String deleteDataSinProcesar = ConsultasSQ.BORRA_DATA_SIN_PROCESAR;
		Connection con = null;
		boolean resultado = true;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Valor idMaestroCarga = " + idMaestroCarga);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Delete data sin procesar = " + deleteDataSinProcesar);
			QueryRunner qr = new QueryRunner();
			qr.update(con, deleteDataSinProcesar, idMaestroCarga);
		} catch (Exception e) {
			resultado = false;
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}

	// Genera el id maestro para el archivo
	public String idMaestroCarga() {
		String idMaestroCargaSQ = ConsultasSQ.ID_MAESTRO_CARGA;
		String res = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Generacion nuevo id de archivos insertados = " + idMaestroCargaSQ);
			ps = con.prepareStatement(idMaestroCargaSQ);
			rs = ps.executeQuery();
			rs.next();
			res = rs.getString(1);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO, "El nuevo id es = " + res);
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return res;
	}
	
	/**
	 * Agregado por Carlos Godinez - Terium - 25/10/2017
	 * 
	 * Se toma en cuenta fecha de archivo cargado en vez de fecha de creacion
	 * */
	public boolean updateFechaArchivo(String idMaestro, String formatoFecha, String fecha) {
		String updateFechaArchivoSQL = ConsultasSQ.UPDATE_FECHA_ARCHIVO;
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO, "Parametros: idMaestro = "
					+ idMaestro + " | formatoFecha = " + formatoFecha + " | fecha = " + fecha);
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.INFO,
					"Update fecha archivo = " + updateFechaArchivoSQL);
			cmd = conn.prepareStatement(updateFechaArchivoSQL);
			cmd.setString(1, fecha);
			cmd.setString(2, formatoFecha);
			cmd.setString(3, idMaestro);
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBArchivosInsertadosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	
	
	private String QRY_VALOR_DEFECTO = "SELECT VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF WHERE MODULO = 'CARGAS' "
			+ "AND TIPO_OBJETO = 'TIPO_CARGA_PREDEFINIDO' AND OBJETO = 'VALOR'";
	
	public String obtenerOpcionPorDefecto() {
		String valor = "";
		Connection conn = null;
	
			
			PreparedStatement cmd = null;
			ResultSet rs = null;
			try {
				conn = ControladorBase.obtenerDtsPromo().getConnection();
				Logger.getLogger(CBReportesDAO.class.getName())
					.log(Level.INFO,"Consulta para obtener tipo de reporte predefinido = " + QRY_VALOR_DEFECTO);
				cmd = conn.prepareStatement(QRY_VALOR_DEFECTO);
				rs = cmd.executeQuery();
				while (rs.next()) {
					valor = rs.getString(1);
				}
		}catch (Exception e) {
			Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(cmd != null)
					try {
						cmd.close();
					} catch (SQLException e) {
						Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		Logger.getLogger(CBReportesDAO.class.getName())
			.log(Level.INFO,"Valor predefinido: " + valor);
		return valor;
	}
	
	
	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier combobox
	 * */
	
	private String QRY_OBTIENE_TIPO_OBJETO_X = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CARGAS' AND TIPO_OBJETO = ?";
	
	public List<CBParametrosGeneralesModel> obtenerTipoObjetoX(String tipoObjeto){
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
			try {
				con = ControladorBase.obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(QRY_OBTIENE_TIPO_OBJETO_X);
				ps.setString(1, tipoObjeto);
				rs = ps.executeQuery();
				CBParametrosGeneralesModel obj = null;
				while(rs.next()){
					obj = new CBParametrosGeneralesModel();
					obj.setObjeto(rs.getString(1));
					obj.setValorObjeto1(rs.getString(2));
					lista.add(obj);
				}
		}catch (Exception e) {
			Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
}