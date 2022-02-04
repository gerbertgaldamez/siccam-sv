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

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.CBConsultaContabilizacionController;
import com.terium.siccam.controller.CBDepositosRecController;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBDepositosRecModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.ConsultasSQ;

@SuppressWarnings("serial")
public class CBDepositosRecDAO extends ControladorBase {
	private static Logger log = Logger.getLogger(CBDepositosRecDAO.class.getName());

	/**
	 * Inserta los registros para mantenimiento Depositos Agregado por Ovidio Santos
	 * - 04/05/2017 -
	 */
	public boolean insertDepositosRec(CBDepositosRecModel objModel) {

		boolean result = false;
		PreparedStatement cmd = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();

			cmd = con.prepareStatement(ConsultasSQ.INSERT_DEPOSITOS_REC_SQ);
			log.debug("el query insert " + ConsultasSQ.INSERT_DEPOSITOS_REC_SQ);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"el query insert " + ConsultasSQ.INSERT_DEPOSITOS_REC_SQ);
			log.debug("el param texto " + objModel.getTexto());
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"el param texto " + objModel.getTexto());
			log.debug("el param tipologias" + objModel.getTipologia());
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"el param tipologias" + objModel.getTipologia());
			log.debug("el param entidad " + objModel.getEntidad());
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"el param entidad " + objModel.getEntidad());
			log.debug("el param tipofecha " + objModel.getTipofecha());
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"el param tipofecha " + objModel.getTipofecha());
			// cmd.setInt(1, objModel.getCbtipologiaspolizaid());
			cmd.setInt(1, objModel.getEntidad());
			cmd.setString(2, objModel.getTexto());
			cmd.setInt(3, objModel.getTipologia());

			cmd.setInt(4, objModel.getTipofecha());

			cmd.setString(5, objModel.getCreadoPor());

			if (cmd.executeUpdate() > 0) {
				result = true;
			}

		} catch (Exception e) {
			log.error("insertDepositosRec" + " - Error", e);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("insertDepositosRec" + " - Error", e);
				//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// consulta llena lisbox

	public List<CBDepositosRecModel> obtenerDepositos(CBDepositosRecModel objModel) {
		List<CBDepositosRecModel> list = new ArrayList<CBDepositosRecModel>();

		Connection con = null;
		ResultSet rs = null;
		Statement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			String query = ConsultasSQ.CONSULTA_DEPOSITOS_REC_SQ;
			String where = " ";

			if (objModel.getEntidad() > 0) {

				where += "AND A.CBCATALOGOAGENCIAID = " + objModel.getEntidad();

			}
			if (objModel.getTexto() != null && !objModel.getTexto().equals("")) {

				where += " AND UPPER (A.TEXTO) LIKE '%" + objModel.getTexto().toUpperCase() + "%' ";

			}
			log.debug("dao tipo fecha " +objModel.getNombreTipoFecha());
			
			if (objModel.getNombreTipoFecha() != null && ! objModel.getNombreTipoFecha().equals("") ) {

				where += " AND A.TIPO_FECHA = " + objModel.getNombreTipoFecha();

			}

			if (objModel.getTipologia() > 0) {
				where += " AND A.CBTIPOLOGIASPOLIZAID = " + objModel.getTipologia();
			}

			log.debug("consulta " + query + where);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.INFO, "consulta " + query + where);

			cmd = con.createStatement();
			where = where + " order by A.CBDEPOSITOSRECID asc ";
			rs = cmd.executeQuery(query + where);
			while (rs.next()) {
				objModel = new CBDepositosRecModel();
				objModel.setCbdepositosid(rs.getInt(1));
				objModel.setTexto(rs.getString(2));
				objModel.setNombreEntidad(rs.getString(3));
				objModel.setEntidad(rs.getInt(4));
				objModel.setNombreTipologia(rs.getString(5));
				objModel.setTipologia(rs.getInt(6));
				objModel.setNombreTipoFecha(rs.getString(7));
				objModel.setTipofecha(rs.getInt(8));

				list.add(objModel);
			}
		} catch (Exception e) {
			log.error("obtenerDepositos" + " - Error", e);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerDepositos" + " - Error", e);
					//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("obtenerDepositos" + " - Error", e);
					//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerDepositos" + " - Error", e);
					//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;

	}

	public double changeString(String cadena) {
		double result = 0.00;
		log.debug("string: " + cadena);
		//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.INFO, "string: " + cadena);
		try {
			if (cadena != null && !"".equals(cadena)) {
				result = Double.parseDouble(cadena.replace(",", ""));
			}
		} catch (NumberFormatException e) {
			log.error("changeString" + " - Error", e);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
		}
		return result;
	}

	// metodo eliminar
	public boolean eliminarDepositosRec(int objBean) {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.DELETE_DEPOSITOS_REC_SQ);
			log.debug("query: " + ConsultasSQ.DELETE_DEPOSITOS_REC_SQ);

			ps.setInt(1, objBean);
			if (ps.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("eliminarDepositosRec" + " - Error", e);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("eliminarDepositosRec" + " - Error", e);
				//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	// metodo modificar
	public boolean modificaDepositosRec(CBDepositosRecModel objModel, int idseleccionado) {
		boolean result = false;

		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(ConsultasSQ.MODIFICAR_DEPOSITO_REC_SQ);
			log.debug("update  " + ConsultasSQ.MODIFICAR_DEPOSITO_REC_SQ);
		//	Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.INFO,
				//	"update  " + ConsultasSQ.MODIFICAR_DEPOSITO_REC_SQ);

			cmd.setInt(1, objModel.getEntidad());
			cmd.setString(2, objModel.getTexto());
			cmd.setInt(3, objModel.getTipologia());
			cmd.setInt(4, objModel.getTipofecha());

			cmd.setString(5, objModel.getModificadoPor());
			cmd.setInt(6, idseleccionado);
			log.debug("idseleccionado en el dao" + idseleccionado);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.INFO,
					//"idseleccionado en el dao" + idseleccionado);

			if (cmd.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("modificaDepositosRec" + " - Error", e);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("modificaDepositosRec" + " - Error", e);
				//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public List<CBCatalogoAgenciaModel> obtieneListadoAgencias(String banco, String nombre, String direccion,
			String estado) {
		String consultaAgenciasSQL = ConsultasSQ.CONSULTA_AGENCIAS_DEPOSITOS_REC;
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
		consultaAgenciasSQL += " ORDER BY TO_NUMBER (codigo_colector) ASC  ";
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			log.debug("Query obtiene listado agencias : " + consultaAgenciasSQL);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.INFO,
					//"Query obtiene listado agencias : " + consultaAgenciasSQL);
			QueryRunner qr = new QueryRunner();

			log.debug("query consulta combo agencias id " + banco);
			

			ResultSetHandler<List<CBCatalogoAgenciaModel>> rsh = new BeanListHandler<CBCatalogoAgenciaModel>(
					CBCatalogoAgenciaModel.class);
			listado = qr.query(con, consultaAgenciasSQL, rsh, new Object[] {});
		} catch (Exception e) {
			log.error("obtieneListadoAgencias" + " - Error", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("obtieneListadoAgencias" + " - Error", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return listado;
	}

	public List<CBTipologiasPolizaModel> obtenerTipologias() {
		Connection con = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBTipologiasPolizaModel> list = new ArrayList<CBTipologiasPolizaModel>();
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(
					"Select CBTIPOLOGIASPOLIZAID, (CBTIPOLOGIASPOLIZAID || ' - ' || NOMBRE), DESCRIPCION, CREADO_POR, FECHA_CREACION, TIPO, PIDE_ENTIDAD from cb_tipologias_poliza order by CBTIPOLOGIASPOLIZAID ");
			rs = cmd.executeQuery();
			CBTipologiasPolizaModel objeBean;
			while (rs.next()) {
				objeBean = new CBTipologiasPolizaModel();
				objeBean.setCbtipologiaspolizaid(rs.getInt(1));
				objeBean.setNombre(rs.getString(2));
				objeBean.setDescripcion(rs.getString(3));
				objeBean.setCreador(rs.getString(4));
				objeBean.setFechaCreacion(rs.getString(5));
				objeBean.setTipo(rs.getInt(6));
				objeBean.setPideEntidad(rs.getInt(7));
				list.add(objeBean);
			}
		} catch (Exception e) {
			log.error("obtenerTipologias" + " - Error", e);
			//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					log.error("obtenerTipologias" + " - Error", e2);
				//	Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					log.error("obtenerTipologias" + " - Error", e1);
					//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("obtenerTipologias" + " - Error", e);
				//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier combobox
	 */
	private String QRY_OBTIENE_TIPO_OBJETO_X = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'DEPOSITOS_REC' AND TIPO_OBJETO = ?";

	public List<CBParametrosGeneralesModel> obtenerTipoObjetoX(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(QRY_OBTIENE_TIPO_OBJETO_X);
			log.debug("query tipo fecha " + QRY_OBTIENE_TIPO_OBJETO_X);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo fecha " + QRY_OBTIENE_TIPO_OBJETO_X);
			ps.setString(1, tipoObjeto);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setObjeto(rs.getString(1));
				obj.setValorObjeto1(rs.getString(2));
				lista.add(obj);
			}
		} catch (Exception e) {
			log.error("obtenerTipoObjetoX" + " - Error", e);
			//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerTipoObjetoX" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerTipoObjetoX" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerTipoObjetoX" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}

}
