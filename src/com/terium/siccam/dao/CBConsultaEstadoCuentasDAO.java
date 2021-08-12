package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Sessions;

import com.terium.siccam.dao.CBConsultaEstadoCuentasDAO;
import com.terium.siccam.model.CBEstadoCuentasModel;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConsultaEstadoCuentasModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.ConsultasSQ;

@SuppressWarnings("serial")
public class CBConsultaEstadoCuentasDAO extends ControladorBase {

	/**
	 * @author Juankrlos - 11/01/2017 - Obtenemos los bancos configurados para los
	 *         estados de cuenta
	 * 
	 * Modified and Commented by CarlosGodinez -> 25/09/2018
	 * Se sustituye query para el llenado de combo de Agrupacion por lentitud reportada        
	 */
	
	/**
	* private String BANCO_SQ = "select distinct cbcatalogobancoid, banco from cb_estado_cuenta_vw";
	* 
	* */
	
	private String BANCO_SQ = "select distinct cbcatalogobancoid, nombre from cb_catalogo_banco "
			+ "where cbcatalogobancoid in (select valor_objeto1 from cb_modulo_conciliacion_conf "
			+ "where modulo = 'CONSULTA_ESTADO_CUENTA' and tipo_objeto = 'VALORES_POR_DEFECTO' "
			+ "and objeto = 'VALOR_ID_BANCOS') order by nombre";

	public List<CBConsultaEstadoCuentasModel> obtenerBanco() {
		List<CBConsultaEstadoCuentasModel> banco = new ArrayList<CBConsultaEstadoCuentasModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO, 
					"Query llenado combo Agrupacion = " + BANCO_SQ);
			ps = con.prepareStatement(BANCO_SQ);
			rs = ps.executeQuery();
			CBConsultaEstadoCuentasModel obj = null;
			while (rs.next()) {
				obj = new CBConsultaEstadoCuentasModel();
				obj.setIdbanco(rs.getInt(1));
				obj.setBanco(rs.getString(2));
				banco.add(obj);
			}

		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return banco;
	}
	
	/**
	 * Obtener opcion por defecto
	 * */
	private String QRY_VALOR_DEFECTO = "SELECT valor_objeto1 FROM cb_modulo_conciliacion_conf "
			+ "WHERE modulo = 'CONSULTA_ESTADO_CUENTA' AND tipo_objeto = 'VALORES_POR_DEFECTO' "
			+ "AND objeto = 'VALOR_ID_BANCOS' AND ROWNUM <= 1 ORDER BY CBMODULOCONCILIACIONCONFID";
	
	public int obtenerOpcionPorDefecto() {
		int valor = 0;
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO,
					"Consulta para obtener agrupacion predefinida = " + QRY_VALOR_DEFECTO);
			cmd = conn.prepareStatement(QRY_VALOR_DEFECTO);
			rs = cmd.executeQuery();
			while (rs.next()) {
				valor = Integer.parseInt(rs.getString(1));
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO, 
				"Valor predefinido: " + valor);
		return valor;
	}

	/**
	 * @author Juankrlos - 11/01/2017 - Obtenemos los bancos configurados para los
	 *         estados de cuenta
	 *         
	 * Modified and Commented by CarlosGodinez -> 25/09/2018
	 * Se sustituye query para el llenado de combo de Entidades por lentitud reportada       
	 */
	
	/**
	* private String AGENCIA_SQ = "select distinct a.cbcatalogoagenciaid, a.agencia "
	*		+ " from cb_estado_cuenta_vw a, cb_catalogo_agencia b "
	*		+ " where a.cbcatalogoagenciaid = b.cbcatalogoagenciaid " + " and b.cuenta_contable is not null "
	*		+ " order by agencia";
	*/		

	private String AGENCIA_SQ = "select distinct cbcatalogoagenciaid, (codigo_colector || ' - ' || nombre) , codigo_colector  from cb_catalogo_agencia  " + 
			"			 where cbcatalogobancoid = ? and cuenta_contable is not null ORDER BY TO_NUMBER(codigo_colector) ASC ";
	
	public List<CBConsultaEstadoCuentasModel> obtenerAgencia(int agrupacionSeleccionada) {
		List<CBConsultaEstadoCuentasModel> banco = new ArrayList<CBConsultaEstadoCuentasModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO,
					"Query llenado de combo de Entidades = " + AGENCIA_SQ);
			ps = con.prepareStatement(AGENCIA_SQ);
			ps.setInt(1, agrupacionSeleccionada);
			rs = ps.executeQuery();
			CBConsultaEstadoCuentasModel obj = null;
			while (rs.next()) {
				obj = new CBConsultaEstadoCuentasModel();
				obj.setIdagencia(rs.getInt(1));
				obj.setAgencia(rs.getString(2));
				banco.add(obj);
			}

		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return banco;
	}

	/**
	 * Consulta general de estado de cuenta
	 * 
	 */
	public List<CBConsultaEstadoCuentasModel> consultaEstadosCuenta(CBEstadoCuentasModel objModel) {
		List<CBConsultaEstadoCuentasModel> result = new ArrayList<CBConsultaEstadoCuentasModel>();
		ResultSet rs = null;
		Statement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			String where = "";
			where += " and fecha >= to_date('" + objModel.getFechaInicio() + "','dd/MM/yyyy') " 
					+ " and fecha <= to_date('" + objModel.getFechaFin() + "','dd/MM/yyyy') ";
			if (objModel.getCbcatalogobancoid() != 0) {
				where += " and cbcatalogobancoid = " + objModel.getCbcatalogobancoid()  + " ";
			}
			if (objModel.getCbcatalogoagenciaid() != 0) {
				where += " and cbcatalogoagenciaid = " + objModel.getCbcatalogoagenciaid() + " ";
			}
			if (objModel.getTexto() != null && !objModel.getTexto().equals("")) {
				where += "AND (trim(texto) like'%" + objModel.getTexto().trim() + "%' " 
						+ "OR trim(asignacion) like'%" + objModel.getTexto().trim()
						+ "%' " + "OR trim(texto_cab_doc) like'%" + objModel.getTexto().trim() + "%' ) ";
			}
			if (objModel.getObservaciones() != null && !objModel.getObservaciones().equals("")) {
				where += " AND trim(observaciones) = '" + objModel.getObservaciones().trim() + "'";
			}
			if (objModel.getTipologia() != null && !objModel.getTipologia().equals("")) {
				if (!objModel.getTipologia().equals("Todas")) {
					if (objModel.getTipologia().equals("(No asignada)")) {
						where += " AND tipologia IS NULL ";
					} else {
						where += " AND cbtipologiaspolizaid = " + Integer.parseInt(objModel.getTipologia()) + " ";
					}
				}
			}
			if (objModel.getAgenciaTipologia() != null && !objModel.getAgenciaTipologia().equals("")) {
				if (!objModel.getAgenciaTipologia().equals("Todas")) {
					if (objModel.getAgenciaTipologia().equals("(No asignada)")) {
						where += " AND agencia_ingreso IS NULL ";
					} else {
						where += " AND IDAGENCIA_INGRESO = " + Integer.parseInt(objModel.getAgenciaTipologia()) + " ";
					}
				}
			}
			if (objModel.getFechaIngresos() != null && !objModel.getFechaIngresos().equals("")) {
				where += " AND to_char(fecha_ingresos, 'DD/MM/YYYY') = '" + objModel.getFechaIngresos() + "' ";
			}

			ps = con.createStatement();
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO, 
					"Query consulta estados de cuenta: " + ConsultasSQ.CONSULTA_ESTADO_CUENTAS_SQ + where);
			rs = ps.executeQuery(ConsultasSQ.CONSULTA_ESTADO_CUENTAS_SQ  + where);
			CBConsultaEstadoCuentasModel obj = null;
			while (rs.next()) {
				obj = new CBConsultaEstadoCuentasModel();
				obj.setCbestadocuentasociedad(rs.getInt(1)); // Agregado por Carlos Godínez - Qitcorp - 01/03/2017
				obj.setBanco(rs.getString(2));
				obj.setAgencia(rs.getString(3));
				obj.setCuenta(rs.getString(4));
				obj.setAsignacion(rs.getString(5)); // Agregado por Carlos Godínez - Qitcorp - 07/07/2017
				obj.setFecha(rs.getString(6));
				obj.setTexto(rs.getString(7));
				obj.setDebe(rs.getString(8));
				obj.setHaber(rs.getString(9));
				obj.setIdentificador(rs.getString(10));
				// Agregado por Carlos Godínez - Qitcorp - 01/03/2017
				obj.setTipologia(rs.getString(11));
				obj.setAgenciaTipologia(rs.getString(12));
				obj.setTextoCabDoc(rs.getString(13)); // Agregado por Carlos Godínez - Qitcorp - 07/07/2017
				obj.setObservaciones(rs.getString(14));
				obj.setFechaIngresos(rs.getString(15));
				obj.setNumDocumento(rs.getString(16));

				obj.setModificadoPor(rs.getString(17));
				obj.setFechaModificacion(rs.getString(18));
				obj.setIdtipologia(rs.getInt(19));
				obj.setCodigoColector(rs.getString(20));
				obj.setIdAgenciaTipologia(rs.getInt(21)); //CarlosGodinez -> 20/08/2018
				result.add(obj);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// Agregado por Carlos Godínez - Qitcorp - 01/03/2017
	public List<CBTipologiasPolizaModel> obtenerTipologias() {
		Connection con = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBTipologiasPolizaModel> list = new ArrayList<CBTipologiasPolizaModel>();
		try {
			con = obtenerDtsPromo().getConnection();
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO,
					"Query llenado de combo de Tipologias = " + ConsultasSQ.QRY_CMB_TIPOLOGIA_ENTIDADES);
			cmd = con.prepareStatement(ConsultasSQ.QRY_CMB_TIPOLOGIA_ENTIDADES);
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
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	// Modificado 20/06/2017 -> Carlos Godinez
	private String QRY_AGENCIA_TIPOLOGIAS = "select distinct cbcatalogoagenciaid, (codigo_colector || ' - ' || nombre) nombre , codigo_colector "
			+ "from cb_catalogo_agencia "
			+ "where estado = 1 and cuenta_contable is null ORDER BY TO_NUMBER(codigo_colector) ASC ";

	public List<CBCatalogoAgenciaModel> obtenerAgenciasCmb() {
		Connection con = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		try {
			con = obtenerDtsPromo().getConnection();
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO,
					"Query llenado de combo de Entidad Tipologia = " + QRY_AGENCIA_TIPOLOGIAS);
			cmd = con.prepareStatement(QRY_AGENCIA_TIPOLOGIAS);
			rs = cmd.executeQuery();
			CBCatalogoAgenciaModel objeBean;
			while (rs.next()) {
				objeBean = new CBCatalogoAgenciaModel();
				objeBean.setcBCatalogoAgenciaId(rs.getString(1));
				objeBean.setNombre(rs.getString(2));
				list.add(objeBean);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	/**
	 * Agregado por CarlosGodinez
	 * Llenado de combo para entidades asociadas a tipologia seleccionada
	 * Ventana modal de tipificacion de estados de cuenta
	 * */
	public List<CBCatalogoAgenciaModel> obtenerEntidadesAsociadasCmb(int tipologiaRecuperada) {
		Connection con = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(ConsultasSQ.QRY_CMB_ENTIDADES_TIPOLOGIA);
			cmd.setInt(1, tipologiaRecuperada); //CarlosGodinez -> 17/08/2018
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO, 
					ConsultasSQ.QRY_CMB_ENTIDADES_TIPOLOGIA );
			rs = cmd.executeQuery();
			CBCatalogoAgenciaModel objeBean;
			while (rs.next()) {
				objeBean = new CBCatalogoAgenciaModel();
				objeBean.setcBCatalogoAgenciaId(rs.getString(1));
				objeBean.setNombre(rs.getString(2));
				list.add(objeBean);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	
	// Modificado 20/06/2017 -> Carlos Godinez
	private String QRY_ENTIDADES_ESTADOS_TARJETA = "SELECT cbcatalogoagenciaid, (codigo_colector || ' - ' || nombre) nombre FROM CB_CATALOGO_AGENCIA "
			+ "WHERE ESTADO = 1 ORDER BY TO_NUMBER(codigo_colector) ASC ";

	public List<CBCatalogoAgenciaModel> obtenerAgenciasEstCuentTarjCmb() {
		Connection con = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(QRY_ENTIDADES_ESTADOS_TARJETA);
			rs = cmd.executeQuery();
			CBCatalogoAgenciaModel objeBean;
			while (rs.next()) {
				objeBean = new CBCatalogoAgenciaModel();
				objeBean.setcBCatalogoAgenciaId(rs.getString(1));
				objeBean.setNombre(rs.getString(2));
				list.add(objeBean);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	/**
	 * Modificado ultima vez por Carlos Godinez - Qitcorp - 30/10/2017
	 * 
	 * Se ocupara el siguiente método para TODAS las operaciones de asignación de
	 * tipologias en estado de cuenta Se modifico para la version unificada para los
	 * paises SV, NI, PA, CR.
	 */
	public int asignarTipologia(int tipologia, int entidad, String fechaIngresos, String observaciones,
			int idEstadoCuenta, List<Integer> listID, List<CBCatalogoAgenciaModel> listDepositos,
			String usuario, int check_tipologia /* , int fechaRegularizacion */) {
		System.out.println("====== ENTRA A ASIGNACION DE TIPOLOGIA POLIZA ======");
		int resultado = 0;
		Statement ps = null;
		Connection con = null;
		String query = "UPDATE CB_ESTADO_CUENTA_SOCIEDAD SET ";
		try {
			con = obtenerDtsPromo().getConnection();
			query += "CBTIPOLOGIASPOLIZAID = " + tipologia + ", " + "OBSERVACIONES = '" + observaciones + "' ";
			if (entidad != 0) {
				query += ", CBCATALOGOAGENCIAID = " + entidad;
			} else {
				query += ", CBCATALOGOAGENCIAID = null";
			}
			// if(fechaRegularizacion == 0) { //CarlosGodinez -> 30/10/2017
			if (fechaIngresos.equals("")) {
				query += ", FECHA_INGRESOS = FECHAVALOR ";
			} else {
				query += ", FECHA_INGRESOS = to_date('" + fechaIngresos + "', 'DD/MM/YYYY') ";
			}
			// }
			/**
			 * Agrega Carlos Godinez -> 10/10/2017 Se agregan campos de usuario y fecha de
			 * modificacion
			 */
			query += ", SWMODIFYBY = '" + usuario + "', SWDATEMODIFIED = sysdate ";
			if (listDepositos.size() == 0) {
				if (idEstadoCuenta != 0) {
					// Asignar tipologia individual
					query += " WHERE CBESTADOCUENTASOCIEDADID = " + idEstadoCuenta;
					
					System.out.println("Query asignar tipologia poliza (individual) = " + query);
					ps = con.createStatement();
					resultado = ps.executeUpdate(query);
				} else {
					// Asignar tipologia masiva
					String where = "";
					System.out.println("Query asignar tipologia poliza (masiva) = " + query
							+ " WHERE CBESTADOCUENTASOCIEDADID = ?; -- Total de valores enviados = " + listID.size());
					ps = con.createStatement();
					for (int pk : listID) {
						where = " WHERE CBESTADOCUENTASOCIEDADID = " + pk;
						System.out.println("check_tipologia: " + check_tipologia);
						if (check_tipologia == 1) {
							where = where + " AND CBTIPOLOGIASPOLIZAID IS NULL ";						
						}

						System.out.println("query masivo : " + query + where);
						ps.addBatch(query + where);
					}
					int contExitosasMasiva = 0;
					int resultadosMasiva[] = ps.executeBatch();
					contExitosasMasiva = resultadosMasiva.length;
					if (contExitosasMasiva > 0) {
						System.out.println("Tipología asignada con éxito.");
						System.out.println("Registros modificados = " + contExitosasMasiva);
					} else if (contExitosasMasiva == 0) {
						System.out.println("No se pudo modificar ningun registro.");
						System.out.println("Registros modificados = " + contExitosasMasiva);
					}
					resultado = contExitosasMasiva;
				}
			} else {
				// String fecIngr = null; //CarlosGodinez -> 30/10/2017
				// String queryFecha = "";
				System.out.println("Asignacion de tipologia se hara por depositos");
				String where = "";
				System.out.println("Update asignar tipologia poliza (masiva) = " + query
						+ "-- Total de valores enviados = " + listDepositos.size());
				ps = con.createStatement();

				for (CBCatalogoAgenciaModel objDepositos : listDepositos) {
					where = "";

					where = " WHERE TRIM(ASIGNACION) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO_CAB_DOC) LIKE '%" + objDepositos.getDeposito() + "%' ";
					
					
					if (check_tipologia == 1) {
						where = where + "AND CBTIPOLOGIASPOLIZAID IS NULL ";						
					}

					// System.out.println("query getTipologia " + objDepositos.getTipologia());
					// System.out.println("query getEntidadDeposito " +
					// objDepositos.getEntidadDeposito());

					// ps.addBatch(query + queryFecha + where);
					ps.addBatch(query + where);
					// System.out.println("Query update archivo depositos = " + query + queryFecha +
					// where);
					System.out.println(query + where);
				}
				int contExitosasDep = 0;
				int resultadosDep[] = ps.executeBatch();
				contExitosasDep = resultadosDep.length;
				if (contExitosasDep > 0) {
					System.out.println("Tipología asignada con éxito.");
					System.out.println("Registros modificados = " + contExitosasDep);
				} else if (contExitosasDep == 0) {
					System.out.println("No se pudo modificar ningun registro.");
					System.out.println("n/** Registros modificados = " + contExitosasDep);
				}
				resultado = contExitosasDep;
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}

	/**
	 * Modificado para guatemala version unificada
	 * 
	 * Se ocupara el siguiente método para TODAS las operaciones de asignación de
	 * tipologias en estado de cuenta Se agrega fechaRegularizacion
	 */
	public int asignarTipologiaGT(int tipologia, int entidad, String fechaIngresos, String observaciones,
			int idEstadoCuenta, List<Integer> listID, List<CBCatalogoAgenciaModel> listDepositos, String usuario,
			int fechaRegularizacion, int check_tipologia, CBEstadoCuentasModel objModel) {
		System.out.println("====== ENTRA A ASIGNACION DE TIPOLOGIA POLIZA ======");
		int resultado = 0;
		Statement ps = null;
		Connection con = null;
		String query = "UPDATE CB_ESTADO_CUENTA_SOCIEDAD SET ";
		try {
			con = obtenerDtsPromo().getConnection();
			query += "CBTIPOLOGIASPOLIZAID = " + tipologia + ", " + "OBSERVACIONES = '" + observaciones + "' ";
			if (entidad != 0) {
				query += ", CBCATALOGOAGENCIAID = " + entidad;
			} else {
				query += ", CBCATALOGOAGENCIAID = null";
			}
			if (fechaRegularizacion == 0) { // CarlosGodinez -> 30/10/2017
				if (fechaIngresos.equals("")) {
					query += ", FECHA_INGRESOS = FECHAVALOR ";
				} else {
					query += ", FECHA_INGRESOS = to_date('" + fechaIngresos + "', 'DD/MM/YYYY') ";
				}
			}
			/**
			 * Agrega Carlos Godinez -> 10/10/2017 Se agregan campos de usuario y fecha de
			 * modificacion
			 */
			query += ", SWMODIFYBY = '" + usuario + "', SWDATEMODIFIED = sysdate ";
			if (listDepositos.size() == 0) {
				if (idEstadoCuenta != 0) {
					// Asignar tipologia individual
					query += " WHERE CBESTADOCUENTASOCIEDADID = " + idEstadoCuenta;
					System.out.println("Query asignar tipologia poliza (individual) = " + query);
					ps = con.createStatement();
					resultado = ps.executeUpdate(query);
					;
				} else {
					// Asignar tipologia masiva
					String where = "";
					System.out.println("Query asignar tipologia poliza (masiva) = " + query
							+ " WHERE CBESTADOCUENTASOCIEDADID = ?; -- Total de valores enviados = " + listID.size());
					ps = con.createStatement();
					for (int pk : listID) {
						where = " WHERE CBESTADOCUENTASOCIEDADID = " + pk;
						
						System.out.println("check_tipologia: " + check_tipologia);
						if (check_tipologia == 1) {
							where = where + " AND CBTIPOLOGIASPOLIZAID IS NULL ";						
						}

						System.out.println("query masivo : " + query + where);

						ps.addBatch(query + where);
					}
					int contExitosasMasiva = 0;
					int resultadosMasiva[] = ps.executeBatch();
					contExitosasMasiva = resultadosMasiva.length;
					if (contExitosasMasiva > 0) {
						System.out.println("Tipología asignada con éxito.");
						System.out.println("Registros modificados = " + contExitosasMasiva);
					} else if (contExitosasMasiva == 0) {
						System.out.println("No se pudo modificar ningun registro.");
						System.out.println("Registros modificados = " + contExitosasMasiva);
					}
					resultado = contExitosasMasiva;
				}
			} else {
				String fecIngr = null; // CarlosGodinez -> 30/10/2017
				String queryFecha = "";
				System.out.println("Asignacion de tipologia se hara por depositos");
				String where = "";
				System.out.println("Update asignar tipologia poliza (masiva) = " + query
						+ "-- Total de valores enviados = " + listDepositos.size());
				ps = con.createStatement();
				if (fechaIngresos.equals("")) {
					fecIngr = "FECHAVALOR";
				} else {
					fecIngr = "to_date('" + fechaIngresos + "', 'DD/MM/YYYY')";
				}
				for (CBCatalogoAgenciaModel objDepositos : listDepositos) {
					// Comment by CarlosGodinez 01/09/2017 se agrega espacio a texto
					/**
					 * CarlosGodinez -> 30/10/2017 Agrega fecha de regularizacion para validar
					 */
					queryFecha = "";
					where = "";
					if (fechaRegularizacion == 1) {
						queryFecha += " , FECHA_INGRESOS = NVL((select min(trunc(fec_regulariza)) "
								+ "from co_hist_regulariza " + "where TRIM(des_observa)LIKE '"
								+ objDepositos.getDeposito() + "%' " + "and fec_regulariza >= sysdate -60), " + fecIngr
								+ ") ";
					}
					/**
					 * CarlosGodinez -> 30/10/2017 Cambia Juankrlos --> 23/11/2017
					 */
System.out.println("deposito GT:" + objDepositos.getDeposito());

					where = " WHERE (TRIM(ASIGNACION) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO_CAB_DOC) LIKE '%" + objDepositos.getDeposito() + "%' ) "
							+ " AND TRUNC(FECHAVALOR) >= TO_DATE('" + objModel.getFechaInicio() + "', 'dd/MM/yyyy') "
							+ " AND TRUNC(FECHAVALOR) <= TO_DATE('" + objModel.getFechaFin() + "', 'dd/MM/yyyy')"
							+ " AND ROWNUM<3 "
							+ " AND FECHACONTAB >= SYSDATE - (SELECT TO_NUMBER (valor_objeto1) FROM cb_modulo_conciliacion_conf  WHERE     modulo = 'DIAS_REVISION' AND OBJETO = 'DIAS') ";

					// System.out.println("query getTipologia " + objDepositos.getTipologia());
					// System.out.println("query getEntidadDeposito " +
					// objDepositos.getEntidadDeposito());
					// campos mapeados del archivo de depositos
					/*
					 * if(objDepositos.getTipologia().trim() != null) {
					 * 
					 * where = where + " AND CBTIPOLOGIASPOLIZAID  = " +
					 * objDepositos.getTipologia(); }
					 */
					
					if (check_tipologia == 1) {
						where = where + "AND CBTIPOLOGIASPOLIZAID IS NULL ";						
					}

					if (objModel != null && objModel.getCuenta() != null) {
						where = where + " AND TRIM(CUENTA) = '" + objModel.getCuenta() + "'";

					}

					ps.addBatch(query + queryFecha + where);
					System.out.println("Query update archivo depositos 1 = " + query + queryFecha + where);
				}
				int contExitosasDep = 0;
				int resultadosDep[] = ps.executeBatch();
				contExitosasDep = resultadosDep.length;
				if (contExitosasDep > 0) {
					System.out.println("Tipología asignada con éxito.");
					System.out.println("Registros modificados = " + contExitosasDep);
				} else if (contExitosasDep == 0) {
					System.out.println("No se pudo modificar ningun registro.");
					System.out.println("n/** Registros modificados = " + contExitosasDep);
				}
				resultado = contExitosasDep;
			}

		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}

	public boolean asignarManual(int entidad, String observaciones, int pk) {
		boolean result = false;
		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(ConsultasSQ.ASOCIACION_MANUAL);
			cmd.setInt(1, entidad);
			cmd.setString(2, observaciones);
			cmd.setInt(3, pk);
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public boolean desasociarTipologia(int pk, String usuario, String observaciones) {
		boolean result = false;
		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(ConsultasSQ.DESASOCIACION_TIPOLOGIA);
			cmd.setString(1, usuario); // CarlosGodinez -> 10/10/2017
			cmd.setString(2, observaciones); // CarlosGodinez -> 10/10/2017
			cmd.setInt(3, pk);
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public boolean desasociarTipologiaMasiva(List<Integer> pkList, String usuario, String observaciones) {
		boolean result = false;
		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			System.out
					.println("Update masivo para desasociar tipologias: " + ConsultasSQ.DESASOCIACION_TIPOLOGIA_MASIVA);
			cmd = con.prepareStatement(ConsultasSQ.DESASOCIACION_TIPOLOGIA_MASIVA);
			System.out.println("Registros antes de actualizar en CB_ESTADO_CUENTA_SOCIEDAD: " + pkList.size());
			int exitosas = 0;
			for (int pk : pkList) {
				cmd.setString(1, usuario); // CarlosGodinez -> 10/10/2017
				cmd.setString(2, observaciones); // ovidio santos -> 10/10/2017
				cmd.setInt(3, pk);
				cmd.addBatch();
				exitosas++;
			}
			if (exitosas > 0) {
				cmd.executeBatch();
				System.out.println("Registros modificados con éxito: " + exitosas);
				result = true;
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public boolean desasociarManual(int pk) {
		boolean result = false;
		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(ConsultasSQ.DESASOCIACION_MANUAL);
			cmd.setInt(1, pk);
			if (cmd.executeUpdate() > 0)
				result = true;
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public boolean desasociarAsociacionManualMasiva(List<Integer> pkList) {
		boolean result = false;
		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			System.out.println("Update masivo para desasociar entidades: " + ConsultasSQ.DESASOCIACION_MANUAL);
			cmd = con.prepareStatement(ConsultasSQ.DESASOCIACION_MANUAL);
			System.out.println("Registros antes de actualizar en CB_ESTADO_CUENTA: " + pkList.size());
			int exitosas = 0;
			for (int pk : pkList) {
				cmd.setInt(1, pk);
				cmd.addBatch();
				exitosas++;
			}
			if (exitosas > 0) {
				cmd.executeBatch();
				cmd.close();
				System.out.println("Registros modificados con éxito: " + exitosas);
				result = true;
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public boolean asignarManualMasiva(int entidad, String observaciones, List<Integer> pkList) {
		boolean result = false;
		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			System.out.println("Update masivo de asociacion manual: " + ConsultasSQ.ASOCIACION_MANUAL);
			cmd = con.prepareStatement(ConsultasSQ.ASOCIACION_MANUAL);
			System.out.println("Registros antes de actualizar en CB_ESTADO_CUENTA: " + pkList.size());
			int exitosas = 0;
			for (int pk : pkList) {
				cmd.setInt(1, entidad);
				cmd.setString(2, observaciones);
				cmd.setInt(3, pk);
				cmd.addBatch();
				exitosas++;
			}
			if (exitosas > 0) {
				cmd.executeBatch();
				cmd.close();
				System.out.println("Registros modificados con éxito: " + exitosas);
				result = true;
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public List<CBConsultaEstadoCuentasModel> consultaEstadoCuentasTarjeta(String tipo, String fechaIni,
			String fechaFin, String agencia, String afiliacion) {
		Connection con = null;
		Statement cmd = null;
		ResultSet rs = null;
		List<CBConsultaEstadoCuentasModel> list = new ArrayList<CBConsultaEstadoCuentasModel>();
		try {
			con = obtenerDtsPromo().getConnection();
			String query = ConsultasSQ.CONSULTA_ESTADO_CUENTA_TARJETA;
			String where = "AND FECHA_TRANSACCION BETWEEN to_date('" + fechaIni + "', 'DD/MM/YYYY') " + "AND to_date('"
					+ fechaFin + "', 'DD/MM/YYYY') ";

			if (tipo != null && !tipo.equals("")) {
				if (!tipo.equals("Todos")) {
					where += "AND TIPO_TARJETA = '" + tipo + "' ";
				}
			}
			if (agencia != null && !agencia.equals("")) {
				if (!agencia.equals("Todas")) {
					if (agencia.equals("(No asociada)")) {
						where += "AND NOMBRE IS NULL ";
					} else {
						where += " AND CBCATALOGOAGENCIAID = '" + agencia + "' ";
					}
				}
			}
			if (!afiliacion.equals("")) {
				where += "AND AFILIACION LIKE '%" + afiliacion + "%' ";
			}
			cmd = con.createStatement();
			System.out.println("Query consulta estado cuenta tarjetas: " + query + where);
			rs = cmd.executeQuery(query + where);
			CBConsultaEstadoCuentasModel objeBean;
			while (rs.next()) {
				objeBean = new CBConsultaEstadoCuentasModel();
				objeBean.setCbestadocuentaid(rs.getInt(1));
				objeBean.setTipoTarjeta(rs.getString(2));
				objeBean.setFechaTransaccion(rs.getString(3));
				objeBean.setAfiliacion(rs.getString(4));
				objeBean.setTipo(rs.getString(5));
				objeBean.setReferencia(rs.getString(6));
				objeBean.setLiquido(rs.getString(7));
				objeBean.setComision(rs.getString(8));
				objeBean.setIvaComision(rs.getString(9));
				objeBean.setRetencion(rs.getString(10));
				objeBean.setConsumo(rs.getString(11));
				objeBean.setNombreEntidad(rs.getString(12));
				objeBean.setObservaciones(rs.getString(13));
				list.add(objeBean);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	/*
	 * metodos para la carga masiva desde el archivo de depositos
	 */

	/**
	 * 
	 * Se ocupara el siguiente método para TODAS las operaciones de asignación de
	 * tipologias masivas depositos en estado de cuenta Se modifico para la version
	 * unificada para los paises SV, NI, PA, CR.
	 */
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	CBCatalogoAgenciaModel objSesionDepositos = null;

	public int asignarTipologiaMasivaDepositos(String fechaIngresos, String observaciones, int idEstadoCuenta,
			List<Integer> listID, List<CBCatalogoAgenciaModel> listDepositos,
			String usuario) {
		objSesionDepositos = (CBCatalogoAgenciaModel) misession.getAttribute("sesionDepositosMasivos");

		System.out.println("====== ENTRA A ASIGNACION DE TIPOLOGIA POLIZA ======");
		int resultado = 0;
		Statement ps = null;
		Connection con = null;
		String query = "UPDATE CB_ESTADO_CUENTA_SOCIEDAD SET ";
		try {
			con = obtenerDtsPromo().getConnection();
			if (listDepositos.size() > 0) {
				ps = con.createStatement();
				for (CBCatalogoAgenciaModel objDepositos : listDepositos) {
					if (objSesionDepositos.getTipologia() != null) {
						query += "CBTIPOLOGIASPOLIZAID = " + objSesionDepositos.getTipologia() + ", "
								+ "OBSERVACIONES = '" + observaciones + "' ";
					} else {
						query += "CBTIPOLOGIASPOLIZAID = null, " + "OBSERVACIONES = '" + observaciones + "' ";
					}

					if (objSesionDepositos.getEntidadDeposito() != null) {
						query += ", CBCATALOGOAGENCIAID = " + objSesionDepositos.getEntidadDeposito();
					} else {
						query += ", CBCATALOGOAGENCIAID = null";
					}

					if (objSesionDepositos.getFechaDeposito() != null) {
						query += ", FECHA_INGRESOS = to_date('" + objSesionDepositos.getFechaDeposito()
								+ "', 'DD/MM/YYYY') ";
					} else {
						query += ", FECHA_INGRESOS = FECHAVALOR ";
					}
					/**
					 * Agrega Carlos Godinez -> 10/10/2017 Se agregan campos de usuario y fecha de
					 * modificacion
					 */
					query += ", SWMODIFYBY = '" + usuario + "', SWDATEMODIFIED = sysdate ";

					System.out.println("query 1 " + query);
					
					System.out.println("Asignacion de tipologia se hara por depositos");
					String where = "";
					System.out.println("Update asignar tipologia poliza (masiva) = " + query
							+ "-- Total de valores enviados = " + listDepositos.size());
					
					where = "";

					where = " WHERE TRIM(ASIGNACION) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO_CAB_DOC) LIKE '%" + objDepositos.getDeposito() + "%' ";

					
					ps.addBatch(query + where);										
				}
				int contExitosasDep = 0;
				int resultadosDep[] = ps.executeBatch();
				contExitosasDep = resultadosDep.length;
				if (contExitosasDep > 0) {
					System.out.println("Tipología asignada con éxito.");
					System.out.println("Registros modificados = " + contExitosasDep);
				} else if (contExitosasDep == 0) {
					System.out.println("No se pudo modificar ningun registro.");
					System.out.println("n/** Registros modificados = " + contExitosasDep);
				}
				resultado = contExitosasDep;
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}

	// prueba 3
	@SuppressWarnings("unused")
	public int asignarTipologiaMasivaDepositos2(String observaciones, int idEstadoCuenta, List<Integer> listID,
			List<CBCatalogoAgenciaModel> listDepositos, String usuario /* , int fechaRegularizacion */) {

		System.out.println("====== ENTRA A ASIGNACION DE TIPOLOGIA POLIZA ======");
		int resultado = 0;
		Statement ps = null;
		String idagencia;
		int contExitosasDep = 0;
		int contador = 0;
		Connection con = null;
		CBCatalogoAgenciaModel objDepositos = null;

		try {
			con = obtenerDtsPromo().getConnection();
			if (listDepositos.size() > 0) {

				Iterator<CBCatalogoAgenciaModel> it = listDepositos.iterator();
				ps = con.createStatement();
				while (it.hasNext()) {
					objDepositos = it.next();
					String query = "";
					query = "UPDATE CB_ESTADO_CUENTA_SOCIEDAD SET ";
					if (objDepositos.getTipologia() != null && !objDepositos.getTipologia().equals("")) {
						query += "CBTIPOLOGIASPOLIZAID = " + objDepositos.getTipologia() + " , " + "OBSERVACIONES = '"
								+ observaciones + "' ";
					} else {
						query += "CBTIPOLOGIASPOLIZAID = null, " + "OBSERVACIONES = '" + observaciones + "' ";
					}

					if (objDepositos.getEntidadDeposito() != null && !objDepositos.getEntidadDeposito().equals("")) {
						// idagencia = obtenerIdAgencia(objDepositos.getEntidadDeposito());

						query += ", CBCATALOGOAGENCIAID = "
								+ "(SELECT CBCATALOGOAGENCIAID FROM CB_CATALOGO_AGENCIA WHERE CODIGO_COLECTOR = '"
								+ objDepositos.getEntidadDeposito() + "' and rownum < 2)";
					} else {
						query += ", CBCATALOGOAGENCIAID = null";
					}

					if (isDate(objDepositos.getFechaDeposito())) {
						query += ", FECHA_INGRESOS = to_date('" + objDepositos.getFechaDeposito() + "', 'DD/MM/YYYY') ";
					} else {
						query += ", FECHA_INGRESOS = FECHAVALOR ";
					}

					/**
					 * Agrega Carlos Godinez -> 10/10/2017 Se agregan campos de usuario y fecha de
					 * modificacion
					 */
					query += ", SWMODIFYBY = '" + usuario + "', SWDATEMODIFIED = sysdate ";

					// System.out.println("query 1 " + query);
					String where = "";

					where = " WHERE TRIM(ASIGNACION) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO) LIKE '%" + objDepositos.getDeposito() + "%' "
							+ "OR TRIM(TEXTO_CAB_DOC) LIKE '%" + objDepositos.getDeposito() + "%' "
					        + "OR TRIM(NUMDOCUMENTO) LIKE '%" + objDepositos.getDeposito() + "%' "
					        + " AND FECHACONTAB >= SYSDATE - (SELECT TO_NUMBER (valor_objeto1) FROM cb_modulo_conciliacion_conf  WHERE     modulo = 'DIAS_REVISION' AND OBJETO = 'DIAS') " ;
					query = query + where;	
					ps.addBatch(query);
					System.out.println(query);
					contador++;
					int resultadosDep[] = ps.executeBatch();
					contExitosasDep = resultadosDep.length;

				}
				contExitosasDep = contador;
				if (contExitosasDep > 0) {
					System.out.println("Tipología asignada con éxito.");
					System.out.println("Registros modificados = " + contExitosasDep);
				} else if (contExitosasDep == 0) {
					System.out.println("No se pudo modificar ningun registro.");
					System.out.println("n/** Registros modificados = " + contExitosasDep);
				}

				resultado = contExitosasDep;
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}

	private String QRY_OBTIENE_ID_AGENCIA = "SELECT  cbcatalogoagenciaid  "
			+ "FROM CB_catalogo_agencia WHERE codigo_colector = ? ";

	public String obtenerIdAgencia(String entidad) {
		String valor = null;
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			System.out.println("Consulta para obtener IDAGENCIA= " + QRY_OBTIENE_ID_AGENCIA);
			cmd = conn.prepareStatement(QRY_OBTIENE_ID_AGENCIA);
			cmd.setString(1, entidad);
			rs = cmd.executeQuery();
			while (rs.next()) {
				valor = rs.getString(1);
			}
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		System.out.println("Valor devuelto idagencia: " + valor);
		return valor;
	}

	/**
	 * Validamos si el string enviado es fecha
	 */
	public boolean isDate(String fecha) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unused")
		Date fec;
		try {
			fec = format.parse(fecha);
			return true;
		} catch (ParseException e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			return false;
		} catch (NullPointerException e) {
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
	}

}
