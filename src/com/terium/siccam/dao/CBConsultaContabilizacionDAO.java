package com.terium.siccam.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBConsultaContabilizacionModel;
import com.terium.siccam.model.CBParametrosSAPModel;
import com.terium.siccam.utils.ConsultasSQ;

/**
 * creado Ovidio Santos
 * 
 */

@SuppressWarnings("serial")
public class CBConsultaContabilizacionDAO extends ControladorBase{
	/**
	 * Agrega Carlos Godinez - Qitcorp - 02/06/2017
	 * 
	 * Valores configurables para generacion de archivo SAP
	 */

	private String QRY_VALOR_CONF = "SELECT VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CONSULTA_CONTABILIZACION' AND TIPO_OBJETO = 'CONTABILIZACION_SAP' ORDER BY CBMODULOCONCILIACIONCONFID";

	public CBParametrosSAPModel obtenerValoresConfSAP() {
		ResultSet rs = null;
		Statement ps = null;
		Connection conn = null;
		CBParametrosSAPModel objModel = new CBParametrosSAPModel();
		try {
			conn = obtenerDtsPromo().getConnection();
				System.out.println("==== Obtiene valor configurable ===");
				ps = conn.createStatement();
				System.out.println("Consulta para obtener valores configurables SAP = " + QRY_VALOR_CONF);
				rs = ps.executeQuery(QRY_VALOR_CONF);
				int contObj = 0;
				while (rs.next()) {
					contObj++;
					switch (contObj) {
					case 1:
						objModel.setValorIP(rs.getString(1));
						break;
					case 2:
						objModel.setUser(rs.getString(1));
						break;
					case 3:
						objModel.setPass(rs.getString(1));
						break;
					case 4:
						objModel.setNombreArchivo(rs.getString(1));
						break;
					case 5:
						objModel.setRutaArchivoSAP(rs.getString(1));
						break;
					case 6:
						objModel.setEncabezado1(rs.getString(1));
						break;
					case 7:
						objModel.setEncabezado2(rs.getString(1));
						break;
					case 8:
						objModel.setEncabezado3(rs.getString(1));
						break;
					case 9:
						objModel.setExtensionArchivo(rs.getString(1));
						break;
					case 10:
						objModel.setNombreArchivo2(rs.getString(1));
						break;
					}
				}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return objModel;
	}

	// consulta llena lisbox

	public List<CBConsultaContabilizacionModel> obtenerContabilizacion(CBConsultaContabilizacionModel objModel,
			int idAgencia, int idAgenciaIngreso) {

		List<CBConsultaContabilizacionModel> list = new ArrayList<CBConsultaContabilizacionModel>();
		Statement cmd = null;
		Connection conn = null; 
		ResultSet rs = null;
		try {

			conn= obtenerDtsPromo().getConnection();
			String query = ConsultasSQ.CONSULTA_CONTABILIZACION_SQ;

			String where = " ";
			System.out.println("fecha inicio " + objModel.getFechaini());
			System.out.println("fecha inicio " + objModel.getFechafin());

			where += " and fecha >= to_date('" + objModel.getFechaini() + "','dd/MM/yyyy') " + " and fecha <= to_date('"
					+ objModel.getFechafin() + "','dd/MM/yyyy') ";

			if (idAgencia != 0) {
				where += "and   cbcatalogoagenciaid = '" + idAgencia + "' ";
			}
			
			if (idAgenciaIngreso != 0) {
				where += "and  idagencia_ingreso  = '" + idAgenciaIngreso + "' ";
			}

			if (objModel.getCentroCosto() != null && !objModel.getCentroCosto().equals("")) {

				where += "AND UPPER (CENTRO_COSTO) LIKE '%" + objModel.getCentroCosto().toUpperCase() + "%' ";

			}
			if (objModel.getClaveContabilizacion() != null && !objModel.getClaveContabilizacion().equals("")) {
				where += "AND UPPER (CLAVE_CONTABILIZACION) LIKE '%" + objModel.getClaveContabilizacion().toUpperCase()
						+ "%' ";
			}

			if (objModel.getCuenta() != null && !objModel.getCuenta().equals("")) {
				where += "AND UPPER (CUENTA) LIKE '%" + objModel.getCuenta().toUpperCase() + "%' ";
			}

			if (objModel.getReferencia() != null && !objModel.getReferencia().equals("")) {
				where += "AND UPPER (REFERENCIA) LIKE '%" + objModel.getReferencia().toUpperCase() + "%' ";
			}

			if (objModel.getTexto() != null && !objModel.getTexto().equals("")) {
				where += "AND UPPER (TEXTO) LIKE '%" + objModel.getTexto().toUpperCase() + "%' ";
			}

			if (objModel.getTexto2() != null && !objModel.getTexto2().equals("")) {
				where += "AND UPPER (TEXTO2) LIKE '%" + objModel.getTexto2().toUpperCase() + "%' ";
			}

			if (objModel.getObservaciones() != null && !objModel.getObservaciones().equals("")) {
				where += "AND UPPER (OBSERVACIONES) LIKE '%" + objModel.getObservaciones().toUpperCase() + "%' ";
			}

			System.out.println("consulta contabilizacion " + ConsultasSQ.CONSULTA_CONTABILIZACION_SQ + where);

			cmd = conn.createStatement();
			cmd.setFetchSize(1024);
			 rs = cmd.executeQuery(query + where);
			// CBConsultaContabilizacionModel objModell = null;
			while (rs.next()) {

				// System.out.println("entra al dao while" );
				objModel = new CBConsultaContabilizacionModel();

				objModel.setCbcontabilizacionid(rs.getInt(1));

				objModel.setCentroCosto(rs.getString(2));
				objModel.setClaveContabilizacion(rs.getString(3));

				objModel.setCuenta(rs.getString(4));
				objModel.setReferencia(rs.getString(5));

				objModel.setTexto(rs.getString(6));
				objModel.setTexto2(rs.getString(7));

				objModel.setObservaciones(rs.getString(8));

				objModel.setFecha(rs.getString(9));
				objModel.setBanco(rs.getString(10));
				objModel.setAgencia(rs.getString(11));
				objModel.setTerminacion(rs.getString(12));
				objModel.setNombre(rs.getString(13));
				objModel.setTipo(rs.getString(14));
				objModel.setDebe(rs.getString(15));
				objModel.setHaber(rs.getString(16));
				objModel.setFecha_contabilizacion(rs.getString(17));
				objModel.setFecha_ingreso(rs.getString(18));

				objModel.setEstado(rs.getString(19));
				objModel.setModificado_por(rs.getString(20));

				objModel.setCbcatalogoagenciaid(rs.getInt(21));

				list.add(objModel);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	
	public List<CBConsultaContabilizacionModel> obtenerContabilizacion(CBConsultaContabilizacionModel objModel,
			int idAgencia, int idAgenciaIngreso, String pais) {

/*		
			String idCajasSQ = ConsultasSQ.SEQ_CAJERO;
		int pk = 0;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBBancoAgenciaCajasDAO.class.getName()).log(Level.INFO,
					"Query secuencia cajas SQ = " + idCajasSQ);
			cmd = conn.prepareStatement(idCajasSQ);
			rs = cmd.executeQuery();
			if (rs.next())
				pk = rs.getInt(1);	
		
		
*/		
		List<CBConsultaContabilizacionModel> list = new ArrayList<CBConsultaContabilizacionModel>();
		Statement cmd = null;
		Connection conn = null;
		ResultSet rs = null;
		try {

			conn= obtenerDtsPromos().getConnection();
			String query = ConsultasSQ.CONSULTA_CONTABILIZACION_SQ;

			String where = " ";
			System.out.println("fecha inicio " + objModel.getFechaini());
			System.out.println("fecha inicio " + objModel.getFechafin());

			where += " and fecha >= to_date('" + objModel.getFechaini() + "','dd/MM/yyyy') " + " and fecha <= to_date('"
					+ objModel.getFechafin() + "','dd/MM/yyyy') ";

			if (idAgencia != 0) {
				where += "and   cbcatalogoagenciaid = '" + idAgencia + "' ";
			}
			
			if (idAgenciaIngreso != 0) {
				where += "and  idagencia_ingreso  = '" + idAgenciaIngreso + "' ";
			}

			if (objModel.getCentroCosto() != null && !objModel.getCentroCosto().equals("")) {

				where += "AND UPPER (CENTRO_COSTO) LIKE '%" + objModel.getCentroCosto().toUpperCase() + "%' ";

			}
			if (objModel.getClaveContabilizacion() != null && !objModel.getClaveContabilizacion().equals("")) {
				where += "AND UPPER (CLAVE_CONTABILIZACION) LIKE '%" + objModel.getClaveContabilizacion().toUpperCase()
						+ "%' ";
			}

			if (objModel.getCuenta() != null && !objModel.getCuenta().equals("")) {
				where += "AND UPPER (CUENTA) LIKE '%" + objModel.getCuenta().toUpperCase() + "%' ";
			}

			if (objModel.getReferencia() != null && !objModel.getReferencia().equals("")) {
				where += "AND UPPER (REFERENCIA) LIKE '%" + objModel.getReferencia().toUpperCase() + "%' ";
			}

			if (objModel.getTexto() != null && !objModel.getTexto().equals("")) {
				where += "AND UPPER (TEXTO) LIKE '%" + objModel.getTexto().toUpperCase() + "%' ";
			}

			if (objModel.getTexto2() != null && !objModel.getTexto2().equals("")) {
				where += "AND UPPER (TEXTO2) LIKE '%" + objModel.getTexto2().toUpperCase() + "%' ";
			}

			if (objModel.getObservaciones() != null && !objModel.getObservaciones().equals("")) {
				where += "AND UPPER (OBSERVACIONES) LIKE '%" + objModel.getObservaciones().toUpperCase() + "%' ";
			}

			System.out.println("consulta contabilizacion " + ConsultasSQ.CONSULTA_CONTABILIZACION_SQ + where);

			cmd = conn.createStatement();
			cmd.setFetchSize(1024);
			 rs = cmd.executeQuery(query + where);
			// CBConsultaContabilizacionModel objModell = null;
			while (rs.next()) {

				// System.out.println("entra al dao while" );
				objModel = new CBConsultaContabilizacionModel();

				objModel.setCbcontabilizacionid(rs.getInt(1));

				objModel.setCentroCosto(rs.getString(2));
				objModel.setClaveContabilizacion(rs.getString(3));

				objModel.setCuenta(rs.getString(4));
				objModel.setReferencia(rs.getString(5));

				objModel.setTexto(rs.getString(6));
				objModel.setTexto2(rs.getString(7));

				objModel.setObservaciones(rs.getString(8));

				objModel.setFecha(rs.getString(9));
				objModel.setBanco(rs.getString(10));
				objModel.setAgencia(rs.getString(11));
				objModel.setTerminacion(rs.getString(12));
				objModel.setNombre(rs.getString(13));
				objModel.setTipo(rs.getString(14));
				objModel.setDebe(rs.getString(15));
				objModel.setHaber(rs.getString(16));
				objModel.setFecha_contabilizacion(rs.getString(17));
				objModel.setFecha_ingreso(rs.getString(18));

				objModel.setEstado(rs.getString(19));
				objModel.setModificado_por(rs.getString(20));

				objModel.setCbcatalogoagenciaid(rs.getInt(21));

				list.add(objModel);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	

	// metodo modificar
	public boolean update(CBConsultaContabilizacionModel objModel, int idseleccionado) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		
		try {
			conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(ConsultasSQ.MODIFICAR_CONTABILIZACION_SQ);
				System.out.println("update  " + ConsultasSQ.MODIFICAR_CONTABILIZACION_SQ);

				cmd.setString(1, objModel.getCentroCosto());
				cmd.setString(2, objModel.getClaveContabilizacion());
				cmd.setString(3, objModel.getCuenta());
				// System.out.println("clave contabilizacion " +
				// objModel.getClaveContabilizacion());
				cmd.setString(4, objModel.getReferencia());
				cmd.setString(5, objModel.getTexto());
				cmd.setString(6, objModel.getTexto2());
				cmd.setString(7, objModel.getObservaciones());

				/*
				 * cmd.setString(8,objModel.getFecha());
				 * cmd.setString(9,objModel.getBanco()); cmd.setString(18,
				 * objModel.getModificado_por());
				 */
				cmd.setInt(8, idseleccionado);
				// System.out.println("idseleccionado en el dao" +
				// idseleccionado);

				if (cmd.executeUpdate() > 0) {
					result = true;
				}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	/**
	 * @author Juankrlos
	 * 
	 * Modificado ultima vez por Carlos Godinez - 14/09/2017
	 */
	public List<CBParametrosSAPModel> obtieneDatosSAP(CBConsultaContabilizacionModel objModel, int idAgencia, int idAgenciaIngreso, int tipo) {
		List<CBParametrosSAPModel> list = new ArrayList<CBParametrosSAPModel>();
		System.out.println("Prueba");
		String where = " ";
		Statement cmd = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();

			where += " and fecha >= to_date('" + objModel.getFechaini() + "','dd/MM/yyyy') " + " and fecha <= to_date('"
					+ objModel.getFechafin() + "','dd/MM/yyyy') ";
			if (idAgencia != 0) {
				where += "and cbcatalogoagenciaid = '" + idAgencia + "' ";
			}
			if (idAgenciaIngreso != 0) {
				where += "and  idagencia_ingreso  = '" + idAgenciaIngreso + "' ";
			}
			if (objModel.getCentroCosto() != null && !objModel.getCentroCosto().equals("")) {

				where += "AND UPPER (CENTRO_COSTO) LIKE '%" + objModel.getCentroCosto().toUpperCase() + "%' ";

			}
			if (objModel.getClaveContabilizacion() != null && !objModel.getClaveContabilizacion().equals("")) {
				where += "AND UPPER (CLAVE_CONTABILIZACION) LIKE '%" + objModel.getClaveContabilizacion().toUpperCase()
						+ "%' ";
			}

			if (objModel.getCuenta() != null && !objModel.getCuenta().equals("")) {
				where += "AND UPPER (CUENTA) LIKE '%" + objModel.getCuenta().toUpperCase() + "%' ";
			}

			if (objModel.getReferencia() != null && !objModel.getReferencia().equals("")) {
				where += "AND UPPER (REFERENCIA) LIKE '%" + objModel.getReferencia().toUpperCase() + "%' ";
			}

			if (objModel.getTexto() != null && !objModel.getTexto().equals("")) {
				where += "AND UPPER (TEXTO) LIKE '%" + objModel.getTexto().toUpperCase() + "%' ";
			}

			if (objModel.getTexto2() != null && !objModel.getTexto2().equals("")) {
				where += "AND UPPER (TEXTO2) LIKE '%" + objModel.getTexto2().toUpperCase() + "%' ";
			}

			if (objModel.getObservaciones() != null && !objModel.getObservaciones().equals("")) {
				where += "AND UPPER (OBSERVACIONES) LIKE '%" + objModel.getObservaciones().toUpperCase() + "%' ";
			}

			String query = "";
			
			if(tipo == 1) {
				query = ConsultasSQ.OBTIENE_DATOS_SAP;
				Logger.getLogger("Sap ");
			}
			if(tipo == 2) {
				query = ConsultasSQ.OBTIENE_DATOS_SAP2;
				Logger.getLogger("Sap2 "+ ConsultasSQ.OBTIENE_DATOS_SAP2);
			}

			System.out.println(query);

			cmd = conn.createStatement();
			cmd.setFetchSize(1024);
			rs = cmd.executeQuery(query + where + "ORDER BY cbestadocuentaid asc");

			System.out.println("Parametros enviados para consulta:");
			System.out.println("fecha inicio " + objModel.getFechaini());
			System.out.println("fecha inicio " + objModel.getFechafin());
			System.out.println("Query generacion SAP = " + query + where + "ORDER BY cbestadocuentaid asc");
			
			
			CBParametrosSAPModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosSAPModel();
				obj.setIdSAP(rs.getInt(1));
				obj.setIdEstadocuenta(rs.getInt(2));
				obj.setLineaSAP(rs.getString(3));
				
				if(tipo == 2) {
					obj.setLineaEncabezadoPartida(rs.getString(4));
				}
				
				list.add(obj);
			}
			System.out.println("Tamaño de lista obtiene datos SAP = " + list.size());
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	
	/**
	 * Inserta los registros para contabilizacion Agregado por Ovidio
	 * Santos - 04/05/2017 -
	 */
	
	public static final String INSERT_CONTABILIZACION_SQ = "INSERT INTO cb_contabilizacion SELECT cb_contabilizacion_sq.NEXTVAL,"
			+ " fecha, cbcatalogoagenciaid,cbcatalogobancoid, SUBSTR (agencia, 1, 16), b.nombre, texto,NULL,"
			+ "a.clave_contabilizacion_cp,a.terminacion_cp, a.centro_costo, a.cbtipologiaspolizaid, a.cuenta_contrapartida,"
			+ " cbestadocuentaid, DECODE (a.tipo, 'Ingreso', 1,  'Diferencia', 3,  'Contrapartida', 2), DECODE (a.tipo,"
			+ " 'Ingreso', monto_banco * 100,'Contrapartida', sc * 100, (monto_banco - sc) * 100),  1, NULL,  NULL,0,"
			+ " 'TERIUM ADMIN',NULL,SYSDATE,  NULL, NULL,NULL FROM cb_contabilizacion_vw a, cb_tipologias_poliza b"
			+ " WHERE     a.cbtipologiaspolizaid = b.cbtipologiaspolizaid ";
	
	
	public boolean insertContabilizacion(String fechaini)  {

		boolean result = false;
		PreparedStatement cmd = null;
		Connection conn  = null;
		try {
		
			conn = obtenerDtsPromo().getConnection();
			String where = "";
			where += " and fecha = '" + fechaini+"'";

				cmd = conn.prepareStatement(INSERT_CONTABILIZACION_SQ + where);
System.out.println("query" + INSERT_CONTABILIZACION_SQ+where);
				if (cmd.executeUpdate() > 0) {
					
					
					result = true;
				}
 
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	/**
	 * Validamos si existen registros ya cargados para el archivo seleccionado
	 * */
	public static final String VALIDA_CARGA_CONTABILIZACION_SQ = "SELECT FECHA_CREACION  "
			+ "FROM cb_contabilizacion WHERE  trunc(FECHA) >= to_date(?, 'dd/MM/yyyy') and trunc(FECHA) <= to_date(?, 'dd/MM/yyyy')";

	public CBConsultaContabilizacionModel validaCarga(String fechadesde, String fechaHasta) {
		CBConsultaContabilizacionModel result = null;
		System.out.println("fecha en el dao" + fechadesde + fechaHasta);
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = obtenerDtsPromo().getConnection();
				ps = conn.prepareStatement(VALIDA_CARGA_CONTABILIZACION_SQ);
				System.out.println("query validacion fecha " + VALIDA_CARGA_CONTABILIZACION_SQ);
				ps.setString(1, fechadesde);
				ps.setString(2, fechaHasta);
				rs = ps.executeQuery();
				
				if(rs.next()){
					result = new CBConsultaContabilizacionModel();
					result.setFechaini(rs.getString(1));
				}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public static final String DELETE_REGISTROS_CREDO = "DELETE FROM cb_contabilizacion WHERE " + "FECHA = ?  ";
	
	public void eliminarRegistros( String fecha){	
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			
				ps = conn.prepareStatement(DELETE_REGISTROS_CREDO);

				//System.out.println("fecha a eliminar dao "+ fecha);
				ps.setString(1, fecha);
				System.out.println("query: "+DELETE_REGISTROS_CREDO);
				System.out.println("fecha en eliminar "+fecha);
				int exec = ps.executeUpdate();
				if(exec > 0){
					conn.commit();
					System.out.println("Script ejecutado correctamente para eliminar registros: "+DELETE_REGISTROS_CREDO);
				}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
	/**
	 * Ejecutamos el Store procedure para contabilizacion
	 * */
	public static final String CARGA_CONTABILIZACION_SP = "{CALL CB_CARGA_CONTABILIZACION_SP(?,?)}";
	public static boolean ejecutaSPContabilizacion(String fecha, String fechaHasta, String pais, String token) {
		boolean result = false;
		Connection conn = null;
		CallableStatement cmd = null;
		int ret = 0;
		// PreparedStatement cst = null;

		try {
			conn = obtenerDtsPromo().getConnection();
			cmd = conn.prepareCall(CARGA_CONTABILIZACION_SP);

			cmd.setString(1, fecha);
			cmd.setString(2, fechaHasta);
			//cmd.setString(3, pais);
			//cmd.setString(4, token);
			result = cmd.executeUpdate() > 0;

			Logger.getLogger("Entra a ejecutaSPContabilizacion"+ " [CARGA_CONTABILIZACION_SP]");
			Logger.getLogger("fecha inicio en el dao sp " + fecha);
			Logger.getLogger("fecha inicio en el dao sp " + fechaHasta);
			String Query1 = " ";
			Query1 += "BEGIN CB_CARGA_CONTABILIZACION2_SP('" + fecha + "','" + fechaHasta + "'); END;";
			Logger.getLogger("SP EN CBConsultaContabilizacionDAO " + Query1);

			// try {
			Logger.getLogger("obtiene conexion");
			//conn = obtenerDtsPromo().getConnection();
			//QueryRunner qry = new QueryRunner();

			Logger.getLogger("SP EN EL CBConsultaContabilizacionDAO" + Query1);
			Logger.getLogger("CARGA_CONTABILIZACION_SP  token:" + token);
			// cmd = conn.prepareCall(Query1);
			// con = obtenerDtsPromo().getConnection();
			// ps = con.prepareStatement(VERIFICA_CARGA_DATA_BANCO);

			// cst.setString(1, fecha);
//			cst.setString(2, fechaHasta);
			Logger.getLogger("fecha inicio en el dao sp " + fecha);
			Logger.getLogger("fecha inicio en el dao sp " + fechaHasta);

			// ResultSet rs = null;
			// Statement cmd = null;

			// rs = cmd.executeQuery(Query1);

			// ret= qry.update(conn, Query1);

			// int exec = cst.executeUpdate();
			// if(exec > 0){
			//result = true;
			Logger.getLogger("se detuvo el proceso ");
			// System.out.println(exec);

			CBBitacoraLogDAO dao = new CBBitacoraLogDAO();
			boolean bitacora = dao.updateBitacoraThread(token, pais, 2);
			// }
			Logger.getLogger("Carga exitosa para el dia: " + fecha);

		} catch (Exception e) {
			CBBitacoraLogDAO dao = new CBBitacoraLogDAO();
			dao.updateBitacoraThread(token, pais, 1);
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			e.printStackTrace();
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	//agrega ovidio
	private static String OBTIENE_BANCO_SQ = "select cbcatalogobancoid, nombre " + "from cb_catalogo_banco "
			+ "where estado = 1 ";
	//private String fecha;

	public List<CBConciliacionCajasModel> generaConsultaBanco() {
		List<CBConciliacionCajasModel> list = new ArrayList<CBConciliacionCajasModel>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			stm = conn.createStatement();
			rs = stm.executeQuery(OBTIENE_BANCO_SQ);
			CBConciliacionCajasModel obj = null;
			while (rs.next()) {
				obj = new CBConciliacionCajasModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(stm != null)
				try {
					stm.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	
/*
			private static String OBTIENE_AGENCIA_SQ = "select b.cbcatalogoagenciaid, b.nombre "
					+ "from cb_catalogo_banco a, cb_catalogo_agencia b " + "where a.cbcatalogobancoid = b.cbcatalogobancoid "
					+ "and b.estado = 1 " + "and a.estado = 1  ";
			*/
	private static String OBTIENE_AGENCIA_SQ = "select distinct cbcatalogoagenciaid, (codigo_colector || ' - ' || nombre) nombre, codigo_colector from cb_catalogo_agencia where estado = 1 and cuenta_contable is null  ";

	public List<CBCatalogoAgenciaModel> generaConsultaAgencia(int idBanco) {
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			String where = "and cbcatalogobancoid = " + idBanco + "";
			 where = where +" ORDER BY TO_NUMBER(codigo_colector) ASC  "  ;
			stm = conn.createStatement();
			rs = stm.executeQuery(OBTIENE_AGENCIA_SQ + where);
			System.out.println("combo agencia ingreso 1" + OBTIENE_AGENCIA_SQ + where);
			CBCatalogoAgenciaModel obj = null;
			while (rs.next()) {
				obj = new CBCatalogoAgenciaModel();
				obj.setcBCatalogoAgenciaId(rs.getString(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(stm != null)
				try {
					stm.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	
	//private static String OBTIENE_AGENCIA_INGRESO_SQ = "select distinct b.IDAGENCIA_INGRESO, a.nombre from cb_catalogo_agencia a, cb_contabilizacion b where  b.IDAGENCIA_INGRESO = a.CBCATALOGOAGENCIAID order by nombre  ";

	//private static String OBTIENE_AGENCIA_INGRESO_SQ = "select distinct a.cbcatalogoagenciaid, a.nombre from cb_catalogo_agencia a where a.cuenta_contable is  null order by nombre ";
	//private static String OBTIENE_AGENCIA_INGRESO_SQ = "select distinct b.IDAGENCIA_INGRESO, B.REFERENCIA from cb_catalogo_agencia a, cb_contabilizacion b where  b.CBCATALOGOAGENCIAID = a.CBCATALOGOAGENCIAID AND b.IDAGENCIA_INGRESO IS NOT NULL order by REFERENCIA ";

	
	private static String OBTIENE_AGENCIA_INGRESO_SQ = "select b.cbcatalogoagenciaid, (b.codigo_colector || ' - ' || b.nombre) nombre, b.codigo_colector  from cb_catalogo_banco a, cb_catalogo_agencia b where a.cbcatalogobancoid = b.cbcatalogobancoid  and b.estado = 1 and a.estado = 1 AND B.CUENTA_CONTABLE IS NOT NULL  ORDER BY TO_NUMBER(b.codigo_colector) ASC  ";

	
	public List<CBCatalogoAgenciaModel> generaConsultaAgenciaIngreso() {
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		Connection conn = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			//String where = "and b.cbcatalogobancoid = " + idBanco + "";

			stm = conn.createStatement();
			System.out.println("query combo agencia  2 " + OBTIENE_AGENCIA_INGRESO_SQ);
			rs = stm.executeQuery(OBTIENE_AGENCIA_INGRESO_SQ );
			CBCatalogoAgenciaModel obj = null;
			while (rs.next()) {
				obj = new CBCatalogoAgenciaModel();
				obj.setcBCatalogoAgenciaId(rs.getString(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(stm != null)
				try {
					stm.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	
	
	//metodo para validar fecha 
	/**
	 * Validamos si existen registros ya cargados para el archivo seleccionado
	 * */
	public static final String VALIDA_FECHA_CONTABILIZACION_SQ = "SELECT sysdate from dual";

	public String validafecha() {
		String result = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = obtenerDtsPromo().getConnection();// se cambio a conn
				ps = conn.prepareStatement(VALIDA_FECHA_CONTABILIZACION_SQ);// se cambio a conn
				System.out.println("query validacion fecha " + VALIDA_FECHA_CONTABILIZACION_SQ);
				
				rs = ps.executeQuery();
				
				if(rs.next()){
					result=rs.getString(1);
				}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaContabilizacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
}
