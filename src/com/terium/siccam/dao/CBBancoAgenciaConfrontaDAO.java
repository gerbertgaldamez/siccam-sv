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
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.sql.ConsultasSQ;

/**
 * Modifica Juankrlos --> 11/12/2017
 * */
public class CBBancoAgenciaConfrontaDAO extends ControladorBase {
	private static Logger log = Logger.getLogger(CBBancoAgenciaConfrontaDAO.class);
	
	// Consulta listado banco agencia confronta
	public List<CBConfiguracionConfrontaModel> obtieneListadoBancoAgeConfronta(String idBanco, String idAgencia) {
		String consultaBACSQL = ConsultasSQ.CONSULTA_BANC_AGE_CONFRONTA;
		List<CBConfiguracionConfrontaModel> listado = new ArrayList<CBConfiguracionConfrontaModel>();
		String where = " ";
		where += " where cbcatalogobancoid = " + idBanco + " and cbcatalogoagenciaid = " + idAgencia;

		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Valor idBanco = " + idBanco);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO, "Valor idBanco = " + idBanco);
			log.debug( "Valor idAgencia = " + idAgencia);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO, "Valor idAgencia = " + idAgencia);
			log.debug( "Consulta banco agencia confronta prueba = " + consultaBACSQL + where);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
					//"Consulta banco agencia confronta prueba = " + consultaBACSQL + where);
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBConfiguracionConfrontaModel> bhl = new BeanListHandler<CBConfiguracionConfrontaModel>(
					CBConfiguracionConfrontaModel.class);
			listado = qr.query(con, consultaBACSQL + where, bhl, new Object[] {});
		} catch (Exception e) {
			log.error("obtieneListadoBancoAgeConfronta() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtieneListadoBancoAgeConfronta() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}
	
	// Consulta listado banco agencia confronta
		public List<CBConfiguracionConfrontaModel> obtieneListadoBancoAgeConfrontaAsociacionConfrontas(String idBanco, String idAgencia) {
			String consultaBACSQL = ConsultasSQ.CONSULTA_BANC_AGE_CONFRONTA_ASOCIACION;
			log.debug( "query :" + ConsultasSQ.CONSULTA_BANC_AGE_CONFRONTA_ASOCIACION);
			log.debug( "query 2:" + consultaBACSQL);
			
			List<CBConfiguracionConfrontaModel> listado = new ArrayList<CBConfiguracionConfrontaModel>();
			Connection con = null;
			try {
				con = ControladorBase.obtenerDtsPromo().getConnection();
				String where = " ";
				where += " where cbcatalogobancoid = " + idBanco + " and cbcatalogoagenciaid = " + idAgencia;
				log.debug( "Valor idBanco = " + idBanco);
				//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO, "Valor idBanco = " + idBanco);
				log.debug( "Valor idAgencia = " + idAgencia);
				//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO, "Valor idAgencia = " + idAgencia);
				log.debug( "Consulta banco agencia confronta = " + consultaBACSQL + where);
				//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
						//"Consulta banco agencia confronta = " + consultaBACSQL + where);
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBConfiguracionConfrontaModel> bhl = new BeanListHandler<CBConfiguracionConfrontaModel>(
						CBConfiguracionConfrontaModel.class);
				listado = qr.query(con, consultaBACSQL + where,  bhl, new Object[] {});
			} catch (Exception e) {
				log.error("obtieneListadoBancoAgeConfrontaAsociacionConfrontas() - Error ", e);
				//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if (con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.error("obtieneListadoBancoAgeConfrontaAsociacionConfrontas() - Error ", e);
						//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}
			return listado;
		}
		
	
	// Inserta la asociacion de confronta
	public int insertaAsociacionConfronta(CBConfiguracionConfrontaModel objModel) {
		String insertAsociacionConfronta = ConsultasSQ.INSERTAR_BANCO_AGE_CONF;

		log.debug( "query  " + insertAsociacionConfronta);
		
		int resultado = 0;
		PreparedStatement cmd = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Valor objeto objModel = " + objModel.toString());
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
				//	"Valor objeto objModel = " + objModel.toString());
			log.debug( "Insert banco agencia confronta = " + insertAsociacionConfronta);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
					//"Insert banco agencia confronta = " + insertAsociacionConfronta);
			cmd = con.prepareStatement(insertAsociacionConfronta);
			cmd.setString(1, objModel.getCbCatalogoBancoId()); // bancoId
			cmd.setString(2, objModel.getCbCatalogoAgenciaId()); // agenciaId
			cmd.setString(3, objModel.getcBConfiguracionConfrontaId()); // confrontaId
			cmd.setString(4, objModel.getTipo()); // tipo
			cmd.setString(5, objModel.getPathFtp()); // pathArchivo
			cmd.setInt(6, objModel.getEstado()); // estadoPrePos
			cmd.setString(7, objModel.getIdConexionConf()); // idConexion
			cmd.setString(8, objModel.getCreadoPor()); // usuario
			cmd.setString(9, objModel.getPalabraArchivo()); // paBusqueda
			cmd.setInt(10, objModel.getCantidadAjustes()); // cantidadAjus
			cmd.setString(11, objModel.getDescartarTransaccion()); // palabraDesc
			//cmd.setDouble(12, objModel.getComision()); // comision
			cmd.setDouble(12, objModel.getAproximacion()); // comision
			System.out.println("campo aproximacion dao " + objModel.getAproximacion());
			cmd.setString(13, objModel.getConfrontaPadre()); // idConfrontaPadre
			//cmd.setInt(15, objModel.getEstadoComision()); // estadoComision
			cmd.setInt(14, objModel.getConfrontasocia()); // confronta socia
			resultado = cmd.executeUpdate();
			log.debug( "Se a insertado un nuevo registro en banco agencia confronta...");
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
					//"Se a insertado un nuevo registro en banco agencia confronta...");
		} catch (Exception e) {
			log.error("insertaAsociacionConfronta() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("insertaAsociacionConfronta() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("insertaAsociacionConfronta() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	// Actualiza asociacion de confronta
	public int actualizaConfronta(CBConfiguracionConfrontaModel objModel) {
		String updateAsociacionSQL = ConsultasSQ.ACTUALIZA_BANCO_AGE_CONF;
		int i = 0;
		log.debug( "id conexion: " + objModel.getIdConexionConf());
		//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
				//"id conexion: " + objModel.getIdConexionConf());
		PreparedStatement cmd = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Valor objModel = " + objModel.toString());
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
				//	"Valor objModel = " + objModel.toString());
			log.debug( "Update banco agencia confronta = " + updateAsociacionSQL);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
					//"Update banco agencia confronta = " + updateAsociacionSQL);
			cmd = con.prepareStatement(updateAsociacionSQL);
			cmd.setString(1, objModel.getcBConfiguracionConfrontaId()); // confronta
			cmd.setInt(2, objModel.getEstado()); // estado
			cmd.setString(3, objModel.getTipo()); // tipo
			cmd.setString(4, objModel.getPathFtp()); // path
			cmd.setString(5, objModel.getModificadoPor()); // usuario
			cmd.setString(6, objModel.getIdConexionConf()); // idConexion
			cmd.setString(7, objModel.getPalabraArchivo()); // paBusqueda
			cmd.setInt(8, objModel.getCantidadAjustes()); // cantidadAjus
			cmd.setString(9, objModel.getDescartarTransaccion()); // palabraDesc
			//cmd.setDouble(10, objModel.getComision()); // comision
			cmd.setDouble(10, objModel.getAproximacion()); // APROXIMACION
			cmd.setString(11, objModel.getConfrontaPadre()); // idConfrontaPadre
			cmd.setInt(12, objModel.getConfrontasocia()); // confronta socia
			//cmd.setInt(12, objModel.getEstadoComision()); // estadoComision
			cmd.setString(13, objModel.getCbBancoAgenciaConfrontaId()); // idConfronta
			i = cmd.executeUpdate();
		} catch (SQLException e) {
			log.error("actualizaConfronta() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					log.error("actualizaConfronta() - Error ", e1);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("actualizaConfronta() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return i;
	}
	
	// Elimina asociacion de confronta
	public int eliminaAsociacionConfronta(String idFila) {
		String deleteAsociacion = ConsultasSQ.ELIMINA_BANCO_AGE_CONF;
		int i = 0;
		Connection con = null;
		try {
			log.debug( "Valor idFila = " + idFila);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO, "Valor idFila = " + idFila);
			log.debug( "Delete banco agencia confronta = " + deleteAsociacion);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
					//"Delete banco agencia confronta = " + deleteAsociacion);
			con = ControladorBase.obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			i = qr.update(con, deleteAsociacion, idFila);
		} catch (Exception e) {
			log.error("eliminaAsociacionConfronta() - Error ", e);
		//	Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("eliminaAsociacionConfronta() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return i;
	}
	
	/**
	 * Agrega Carlos Godinez -> 22/09/2017
	 * 
	 * Se agrega campo de estado_comision para verificar si se haran calculos
	 * ya se por porcentaje o por monto.
	 * */
	public List<CBParametrosGeneralesModel> obtenerEstadoComisionCmb(){
		String estadosComisionSQL = ConsultasSQ.QRY_OBTIENE_ESTADOS_COMISION;
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "Consulta estados comision = " + estadosComisionSQL);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
					//"Consulta estados comision = " + estadosComisionSQL);
			ps = con.prepareStatement(estadosComisionSQL);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while(rs.next()){
				obj = new CBParametrosGeneralesModel();
				obj.setObjeto(rs.getString(1));
				obj.setValorObjeto1(rs.getString(2));
				lista.add(obj);
			}
		} catch (Exception e) {
			log.error("obtenerEstadoComisionCmb() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerEstadoComisionCmb() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerEstadoComisionCmb() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerEstadoComisionCmb() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		} 
		return lista;
	} 
	
	/**
	 * FIN Agrega Carlos Godinez -> 22/09/2017
	 * */
	
	/**
	 * Agrega CarlosGodinez -> 14/09/2018
	 * Verificacion de registros de conciliacion para 
	 * asociacion de confronta previo a eliminar
	 * */
	public int cantidadRegistrosConciliacion(int cbBancoAgenciaConfrontaId) {
		String countConciliacionesSQL = ConsultasSQ.CANT_REGISTROS_CONCILIACION;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		int cantRegistros = 0;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug( "cbbancoagenciaconfrontaid = : " + cbBancoAgenciaConfrontaId);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
					//"cbbancoagenciaconfrontaid = : " + cbBancoAgenciaConfrontaId);
			log.debug( "Count SQL registros de conciliacion = " + countConciliacionesSQL);
		//	Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
				//	"Count SQL registros de conciliacion = " + countConciliacionesSQL);
			ps = con.prepareStatement(countConciliacionesSQL);
			ps.setInt(1, cbBancoAgenciaConfrontaId); 
			rs = ps.executeQuery();
			if(rs.next()){
				cantRegistros = rs.getInt(1);
				log.debug( "Registros de conciliacion encontrados = " + cantRegistros);
				//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.INFO,
						//"Registros de conciliacion encontrados = " + cantRegistros);
			}
		} catch (Exception e) {
			log.error("cantidadRegistrosConciliacion() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("cantidadRegistrosConciliacion() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("cantidadRegistrosConciliacion() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("cantidadRegistrosConciliacion() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		} 
		return cantRegistros;
	} 
}
