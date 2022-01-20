package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.zkoss.zul.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.sql.ConsultasSQ;

@SuppressWarnings("serial")
public class CBCatalogoAgenciaDAO extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBCatalogoAgenciaDAO.class);
	/**
	 * @author JCDeLaCruz
	 * 
	 *         Consulta desde carga de confrontas
	 */

	public List<CBCatalogoAgenciaModel> obtieneListadoAgencias(String banco, String nombre, String direccion,
			String estado) {
		String consultaAgenciasSQL = ConsultasSQ.CONSULTA_AGENCIAS2;
//		String consultaAgenciasSQL = ConsultasSQ.CONSULTA_BANC_AGE_CONFRONTA;
		
		List<CBCatalogoAgenciaModel> listado = new ArrayList<CBCatalogoAgenciaModel>();
		// Filtros
		if (banco != null && !banco.equals("")) {
			consultaAgenciasSQL += " and b.CBCATALOGOBANCOID = '" + banco + "' ";
		}
		if (nombre != null && !nombre.equals("")) {
			consultaAgenciasSQL += "and (upper(a.NOMBRE) like('%' || upper(trim('" + nombre + "'))) || '%') ";
		}
		if (direccion != null && !direccion.equals("")) {
			consultaAgenciasSQL += "and a.DIRECCION = '" + estado + "' ";
		}
		if (estado != null && !estado.equals("")) {
			consultaAgenciasSQL += "and a.ESTADO = '" + estado + "' ";
		}
		consultaAgenciasSQL += "order by a.NOMBRE asc";
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Query obtiene listado agencias : " + consultaAgenciasSQL);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Query obtiene listado agencias : " + consultaAgenciasSQL);
			QueryRunner qr = new QueryRunner();

			log.debug( "query consulta combo agencias id " + banco);
			
			

			ResultSetHandler<List<CBCatalogoAgenciaModel>> rsh = new BeanListHandler<CBCatalogoAgenciaModel>(
					CBCatalogoAgenciaModel.class);
			listado = qr.query(con, consultaAgenciasSQL, rsh, new Object[] {});
		} catch (Exception e) {
			log.error("obtieneListadoAgencias() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("obtieneListadoAgencias() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		} 
		return listado;
	}

	// inicia nueva forma de consultar
	// Consulta de entidades -- modifico Ovidio Santos
	public List<CBCatalogoAgenciaModel> obtieneListadoAgenciasMnt(String banco, String nombre, String cuentaContable,
			String estado, String codigoColector, String nit) {
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		String query = ConsultasSQ.CONSULTA_AGENCIAS;
		String where = " ";
		try {
			if (banco != null && !"".equals(banco)) {
				where += " and b.CBCATALOGOBANCOID = '" + banco + "' ";
			}
			if (nombre != null && !"".equals(nombre)) {
				where += "and (upper(a.NOMBRE) like('%' || upper(trim('" + nombre + "'))) || '%') ";
			}
			if (cuentaContable != null && !"".equals(cuentaContable)) {
				where += "and a.CUENTA_CONTABLE = '" + cuentaContable + "' ";
			}
			if (estado != null && !"".equals(estado)) {
				where += "and a.ESTADO = '" + estado + "' ";
			}

			if (codigoColector != null && !"".equals(codigoColector)) {
				where += "and a.codigo_colector = '" + codigoColector + "' ";
			}
			if (nit != null && !"".equals(nit)) {
				where += "and a.nit = '" + nit + "' ";
			}
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			where = where + "order by a.NOMBRE asc";
			log.debug( "query consulta entidades = " + query + where);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"query consulta entidades = " + query + where);
			rs = stm.executeQuery(query + where);
			CBCatalogoAgenciaModel obj = null;
			while (rs.next()) {
				obj = new CBCatalogoAgenciaModel();
				obj.setcBCatalogoAgenciaId(rs.getString(1));
				obj.setcBCatalogoBancoId(rs.getString(2));
				obj.setNombreBanco(rs.getString(3));
				obj.setNombre(rs.getString(4));
				obj.setTelefono(rs.getString(5));
				obj.setDireccion(rs.getString(6));
				obj.setEstadoTxt(rs.getString(7));
				obj.setCreadoPor(rs.getString(8));
				obj.setFechaCreacion(rs.getString(9));
				obj.setCuentaContable(rs.getString(10));
				obj.setEstado(rs.getString(11));
				obj.setMoneda(rs.getString(12));
				obj.setCodigoColector(rs.getString(13));
				obj.setNit(rs.getString(14));
				list.add(obj);
			}
		} catch (SQLException e) {
			log.error("obtieneListadoAgenciasMnt() - Error ", e);
			//Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtieneListadoAgenciasMnt() - Error ", e);
					//Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					log.error("obtieneListadoAgenciasMnt() - Error ", e);
					//Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtieneListadoAgenciasMnt() - Error ", e);
					//Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}

	// fin

	// Insertar entidades
	public int ingresaNuevaAgencia(CBCatalogoAgenciaModel objEntidad) {
		String insertAgenciaSQL = ConsultasSQ.INSERTA_AGENCIA;
		int resultado = 0;
		PreparedStatement cmd = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Valor objeto objEntidad = " + objEntidad.toString());
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
				//	"Valor objeto objEntidad = " + objEntidad.toString());
			log.debug( "Insert agencia = " + insertAgenciaSQL);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Insert agencia = " + insertAgenciaSQL);
			try {
			cmd = con.prepareStatement(insertAgenciaSQL);
			cmd.setString(1, objEntidad.getcBCatalogoBancoId());
			cmd.setString(2, objEntidad.getNombre());
			cmd.setString(3, objEntidad.getTelefono());
			cmd.setString(4, objEntidad.getDireccion());
			cmd.setString(5, objEntidad.getEstado());
			cmd.setString(6, objEntidad.getCreadoPor());
			cmd.setString(7, objEntidad.getCuentaContable());
			cmd.setString(8, objEntidad.getMoneda());
			cmd.setString(9, objEntidad.getCodigoColector());
			cmd.setString(10, objEntidad.getNit());
			resultado = cmd.executeUpdate();
			log.debug( "Se ha ingresado una nueva agencia...");
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
				//	"Se ha ingresado una nueva agencia...");
		}catch (SQLException e) {
			log.error("ingresaNuevaAgencia() - Error ", e);
			//Logger.getLogger(CBEstadoCuentaDAO.class.getName())
			//.log(Level.INFO,"Error de counstring  "+ e.getMessage());
			
		}
			
		} catch (Exception e) {
			log.error("ingresaNuevaAgencia() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("ingresaNuevaAgencia() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("ingresaNuevaAgencia() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	// Actualiza entidad
	public int actualizaAgencia(CBCatalogoAgenciaModel objEntidad) {
		String updateAgenciaSQL = ConsultasSQ.ACTUALIZA_AGENCIA;
		int resultado = 0;
		PreparedStatement cmd = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Valor objeto objEntidad = " + objEntidad.toString());
		//	Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Valor objeto objEntidad = " + objEntidad.toString());
			log.debug( "Update agencia = " + updateAgenciaSQL);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Update agencia = " + updateAgenciaSQL);
			try {
			cmd = con.prepareStatement(updateAgenciaSQL);
			cmd.setString(1, objEntidad.getcBCatalogoBancoId());
			cmd.setString(2, objEntidad.getNombre());
			cmd.setString(3, objEntidad.getTelefono());
			cmd.setString(4, objEntidad.getDireccion());
			cmd.setString(5, objEntidad.getEstado());
			cmd.setString(6, objEntidad.getModificadoPor());
			cmd.setString(7, objEntidad.getCuentaContable());
			cmd.setString(8, objEntidad.getMoneda());
			cmd.setString(9, objEntidad.getCodigoColector());
			cmd.setString(10, objEntidad.getNit());
			cmd.setString(11, objEntidad.getcBCatalogoAgenciaId());
			resultado = cmd.executeUpdate();
			log.debug( "Se ha modificado agencia...");
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO, "Se ha modificado agencia...");
		}catch (SQLException e) {
			log.error("actualizaAgencia() - Error ", e);
			//Logger.getLogger(CBEstadoCuentaDAO.class.getName())
			//.log(Level.INFO,"Error de counstring  "+ e.getMessage());
			
		}
		} catch (Exception e) {
			log.error("actualizaAgencia() - Error ", e);
			//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("actualizaAgencia() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("actualizaAgencia() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaConfrontaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	/**
	 * Agregado por Carlos Godinez - Terium - 18/10/2017
	 * 
	 * Modificar asociaciones para tablas relacionadas al cambiar de agrupacion una
	 * entidad
	 */
	public boolean updateAsociaciones(CBCatalogoAgenciaModel obj) {
		String updateAsociacionesEntidad = ConsultasSQ.QRY_UPDATE_ASOCIACIONES_ENTIDAD;
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			log.debug( "Valor objeto obj = " + obj.toString());
		//	Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Valor objeto obj = " + obj.toString());
			log.debug( "Update asociaciones por entidad = " + updateAsociacionesEntidad);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
				//	"Update asociaciones por entidad = " + updateAsociacionesEntidad);
			cmd = conn.prepareStatement(updateAsociacionesEntidad);
			cmd.setString(1, obj.getcBCatalogoBancoId());
			cmd.setString(2, obj.getcBCatalogoAgenciaId());
			cmd.executeUpdate();
			result = true;
			log.debug( "\n*** Asociacion en CB_BANCO_AGENCIA_CONFRONTA modificado ***\n");
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"\n*** Asociacion en CB_BANCO_AGENCIA_CONFRONTA modificado ***\n");
		} catch (Exception e) {
			log.error("updateAsociaciones() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("updateAsociaciones() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// Consulta nombre entidad
	public boolean consultaNombre(String nombre) {
		String nombreAgenciaSQL = ConsultasSQ.CONSULTA_NOMBRE_AGENCIA;
		boolean respuesta = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Valor nombre = " + nombre);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO, "Valor nombre = " + nombre);
			log.debug( "Consulta nombre agencia = " + nombreAgenciaSQL);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Consulta nombre agencia = " + nombreAgenciaSQL);
			ps = con.prepareStatement(nombreAgenciaSQL);
			ps.setString(1, nombre);
			rs = ps.executeQuery();
			rs.next();
			if (Integer.valueOf(rs.getString("valor")) > 0) {
				respuesta = true;
			}
		} catch (Exception e) {
			log.error("consultaNombre() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("consultaNombre() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("consultaNombre() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("consultaNombre() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return respuesta;
	}

	// Agregado por CarlosGodinez - para el modulo de reporteria - Qitcorp -
	// 14/03/2017
	public List<CBCatalogoAgenciaModel> generaConsultaAgencia() {
		String obtieneAgenciaSQ = ConsultasSQ.OBTIENE_AGENCIA_SQ_R;
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Consulta nombre agencia = " + obtieneAgenciaSQ);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Consulta nombre agencia = " + obtieneAgenciaSQ);
			stm = con.createStatement();
			rs = stm.executeQuery(obtieneAgenciaSQ);
			CBCatalogoAgenciaModel obj = null;
			while (rs.next()) {
				obj = new CBCatalogoAgenciaModel();
				obj.setcBCatalogoAgenciaId(String.valueOf(rs.getInt(1)));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		} catch (SQLException e) {
			log.error("generaConsultaAgencia() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("generaConsultaAgencia() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					log.error("generaConsultaAgencia() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("generaConsultaAgencia() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}

	/**
	 * Agregado por Carlos Godinez - 16/10/2017
	 * 
	 * Llena combo de agrupaciones al editar entidades
	 */
	public List<CBCatalogoBancoModel> obtieneListaAgrupaciones() {
		String consultaAgrupacionesSQL = ConsultasSQ.CONSULTA_AGRUPACIONES;
		List<CBCatalogoBancoModel> lista = new ArrayList<CBCatalogoBancoModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Query consulta agrupaciones = " + consultaAgrupacionesSQL);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Query consulta agrupaciones = " + consultaAgrupacionesSQL);
			ps = con.prepareStatement(consultaAgrupacionesSQL);
			rs = ps.executeQuery();
			CBCatalogoBancoModel obj = null;
			while (rs.next()) {
				obj = new CBCatalogoBancoModel();
				obj.setCbcatalogobancoid(rs.getString(1));
				obj.setNombre(rs.getString(2));
				lista.add(obj);
			}
		} catch (Exception e) {
			log.error("obtieneListaAgrupaciones() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtieneListaAgrupaciones() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtieneListaAgrupaciones() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtieneListaAgrupaciones() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}

	public List<CBParametrosGeneralesModel> obtenerEstadoCmb() {
		String obtenerEstadosSQL = ConsultasSQ.QRY_OBTIENE_ESTADOS2;
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug( "Query consulta agrupaciones = " + obtenerEstadosSQL);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Query consulta agrupaciones = " + obtenerEstadosSQL);
			ps = con.prepareStatement(obtenerEstadosSQL);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setObjeto(rs.getString(1));
				obj.setValorObjeto1(rs.getString(2));
				lista.add(obj);
			}
		} catch (Exception e) {
			log.error("obtenerEstadoCmb() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerEstadoCmb() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerEstadoCmb() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerEstadoCmb() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}

}
