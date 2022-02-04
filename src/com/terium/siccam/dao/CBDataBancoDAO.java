package com.terium.siccam.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
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
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.service.ProcessFileTxtServImplCR;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.FiltroQuery;
import com.terium.siccam.utils.GeneraFiltroQuery;
import com.terium.siccam.utils.ObtieneCampos;
import com.terium.siccam.utils.Orden;

/**
 * 
 * @author rSianB for terium.com
 */
@SuppressWarnings("serial")
public class CBDataBancoDAO extends ControladorBase {
	private static Logger logger = Logger.getLogger(CBDataBancoDAO.class);

	private int totalRegistros;

	/**
	 * This method should insert a new record in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 */
	public int insertar(CBDataBancoModel registro) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBDataBancoModel.TABLE + " ("
					+ ObtieneCampos.obtieneSQL(CBDataBancoModel.class, null, true, null) + ") values " + " ("
					+ ObtieneCampos.obtieneInsert(CBDataBancoModel.class) + ")";

			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getCBDataBancoId(), registro.getCodCliente(),
					registro.getTelefono(), registro.getFecha(), registro.getcBCatalogoBancoId(),
					registro.getcBCatalogoAgenciaId(), registro.getCBBancoAgenciaConfrontaId(), registro.getMonto(),
					registro.getTransaccion(), registro.getEstado(), registro.getMes(), registro.getTexto1(),
					registro.getTexto2(), registro.getCreadoPor(), registro.getModificadoPor(),
					registro.getFechaCreacion(), registro.getFechaModificacion() };

			ret = qry.update(conn, queInsert, param);
		} catch (Exception e) {
			logger.error("insertar()"
					+ " - Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("insertar()"
							+ " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return ret;

	}

	/**
	 * This method should insert a list of records in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 * 
	 *         Modified by CarlosGodinez -> 31/08/2017 Se modifica insercion masiva
	 *         en la parte del formateo de fecha para los valores que vayan con
	 *         formato 12 horas si el valor lleva caracteres como a.m. o p.m. se
	 *         modifican a am y pm sin puntos para que sea un formato valido para
	 *         Oracle
	 */
	public int insertarMasivo(List<CBDataBancoModel> registros, String formatFecha) {
		logger.debug("insertarMasivo()" + "- campo fecha recibido param " + formatFecha);
		

		String INSERTA_MASIVOS_BANCO = "INSERT " + " INTO CB_DATA_BANCO " + "   ( " + "     cBDataBancoId, "
				+ "     cod_Cliente, " + "     telefono, " + "     tipo, " + "     fecha, " + "     cBCatalogoBancoId, "
				+ "     cBCatalogoAgenciaId, " + "     cBBancoAgenciaConfrontaId, " + "     monto, "
				+ "     transaccion, " + "     estado, " + "     mes, dia,  " + "     texto1, " + "     texto2, "
				+ "     creado_Por, " + "     modificado_Por, " + "     fecha_Creacion, "
				+ "     fecha_Modificacion, id_archivos_insertados, " + " CODIGO, " + " COMISION " + "  ) "
				+ "     VALUES " + " ( " + " cb_data_banco_sq.nextval, " + "     ?, " + "     ?, " + "     ?, "
				+ " to_date(replace(replace(?, 'a.m.', 'am'), 'p.m.', 'pm'), '" + formatFecha + "'), " + "     ?, "
				+ "     ?, " + "     ?, " + "     ?, " + "     ?, " + "     ?, "
				+ "     ?, trunc(to_date(replace(replace(?, 'a.m.', 'am'), 'p.m.', 'pm'), '" + formatFecha
				+ "'), 'dd') ," + "      SUBSTR (TRIM(?), 1, 70), " + "     SUBSTR (TRIM(?), 1, 70), " + "     ?, "
				+ "     ?, " + "     sysdate, " + " to_date(replace(replace(?, 'a.m.', 'am'), 'p.m.', 'pm'), '"
				+ formatFecha + "'), " + " ?, " + " ?, " + " ? " + " )";

		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			logger.debug("insertarMasivo()" + "- CANTIDAD DE REGISTROS ANTES DEL INSERT: " + registros.size());
			

			QueryRunner qr = new QueryRunner();
			List<Object[]> dataList = new ArrayList<Object[]>(registros.size());
			Object[] param;
			Object[][] dataObj = new Object[registros.size()][18];
			for (CBDataBancoModel registro : registros) {

				param = new Object[] { registro.getCodCliente(),

						registro.getTelefono(), registro.getTipo(), registro.getFecha(),
						Integer.parseInt(registro.getcBCatalogoBancoId()),
						Integer.parseInt(registro.getcBCatalogoAgenciaId()), registro.getCBBancoAgenciaConfrontaId(),
						registro.getMonto(), registro.getTransaccion(), registro.getEstado(), registro.getMes(),
						registro.getDia(), registro.getTexto1(), registro.getTexto2(), registro.getCreadoPor(),
						registro.getModificadoPor(), registro.getFechaModificacion(), registro.getIdCargaMaestro(),
						registro.getCbAgenciaVirfisCodigo(), registro.getComision() };
				dataList.add(param);
			}
			logger.debug("insertarMasivo()" + "- Query para la carga Masiva: " + INSERTA_MASIVOS_BANCO);
			

			dataObj = dataList.toArray(dataObj);
			try {
				int[] objRet = qr.batch(conn, INSERTA_MASIVOS_BANCO, dataObj);
				logger.debug("insertarMasivo()" + "- CANTIDAD DE ARCHIVOS ALMACENADOS: " + objRet.length);
				
				return objRet.length;
			} catch (Exception e) {
				logger.error("insertarMasivo()" + " - Error", e);
				
				e.printStackTrace();
			}

		} catch (Exception e) {
			logger.error("insertarMasivo()" + " - Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("insertarMasivo()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return 0;

	}

	/**
	 * This method should update a record within provide filters
	 * 
	 * @return an int which depends on the result of the update
	 */
	public int actualiza(CBDataBancoModel registro) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Update " + CBDataBancoModel.TABLE + " set " + CBDataBancoModel.FIELD_COD_CLIENTE + " =?, "
					+ CBDataBancoModel.FIELD_TELEFONO + " =?, " + CBDataBancoModel.FIELD_FECHA + " =?, "
					+ CBDataBancoModel.FIELD_CBCATALOGOBANCOID + " =?, " + CBDataBancoModel.FIELD_CBCATALOGOAGENCIAID
					+ " =?, " + CBDataBancoModel.FIELD_CBBANCOAGENCIACONFRONTAID + " =?, "
					+ CBDataBancoModel.FIELD_MONTO + " =?, " + CBDataBancoModel.FIELD_TRANSACCION + " =?, "
					+ CBDataBancoModel.FIELD_ESTADO + " =?, " + CBDataBancoModel.FIELD_MES + " =?, "
					+ CBDataBancoModel.FIELD_TEXTO1 + " =?, " + CBDataBancoModel.FIELD_TEXTO2 + " =?, "
					+ CBDataBancoModel.FIELD_CREADO_POR + " =?, " + CBDataBancoModel.FIELD_MODIFICADO_POR + " =?, "
					+ CBDataBancoModel.FIELD_FECHA_CREACION + " =?, " + CBDataBancoModel.FIELD_FECHA_MODIFICACION
					+ " =?, " + " where  " + CBDataBancoModel.PKFIELD_CBDATABANCOID + " = ? ";

			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getCodCliente(), registro.getTelefono(), registro.getFecha(),
					registro.getcBCatalogoBancoId(), registro.getcBCatalogoAgenciaId(),
					registro.getCBBancoAgenciaConfrontaId(), registro.getMonto(), registro.getTransaccion(),
					registro.getEstado(), registro.getMes(), registro.getTexto1(), registro.getTexto2(),
					registro.getCreadoPor(), registro.getModificadoPor(), registro.getFechaCreacion(),
					registro.getFechaModificacion(), registro.getCBDataBancoId() };

			ret = qry.update(conn, queInsert, param);

		} catch (Exception e) {
			logger.error("actualiza()" + " - Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("actualiza()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return ret;

	}

	/**
	 * This method should return a list of objects within given filters
	 * 
	 * @return the list of Objects
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CBDataBancoModel> Listado(List<Filtro> filtro, List<Orden> orden) {
		List<CBDataBancoModel> ret = null;
		String query;
		Connection conn = null;

		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			query = "select " + ObtieneCampos.obtieneSQL(CBDataBancoModel.class, null, true, null) + " from "
					+ CBDataBancoModel.TABLE;

			FiltroQuery filtroQuery = null;

			filtroQuery = GeneraFiltroQuery.generaFiltro(null, orden, true);

			String sqlFiltros = Filtro.getStringFiltros(filtro, true);
			QueryRunner qry = new QueryRunner();
			BeanListHandler blh = new BeanListHandler(CBDataBancoModel.class);
			if (filtroQuery.getParams() != null) {
				ret = (List<CBDataBancoModel>) qry.query(conn, query + sqlFiltros + filtroQuery.getSql(), blh,
						filtroQuery.getParams());
			} else {
				ret = (List<CBDataBancoModel>) qry.query(conn, query + sqlFiltros + filtroQuery.getSql(), blh);
			}
			this.totalRegistros = ret.size();
			logger.debug("Listado()" + " - resultado: " + ret.size());
			
		} catch (Exception e) {
			logger.error("Listado()" + " - Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("Listado()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return ret;
	}

	/**
	 * @return the totalRegistros
	 */
	public int getTotalRegistros() {
		return totalRegistros;
	}

	// Llama a proceso CB_CONCILIACION_SP
	private static String EJECUTA_PROCESO_CB_CONCILIACION_SP = "{call CB_CONCILIACION_SP(?)}";

	public int ejecutaProcesoConciliacion(String idMaestroCarga) {
		PreparedStatement callableStatement = null;
		Connection con = null;
		logger.debug("ejecutaProcesoConciliacion()" + " - Parametro para el proceso: " + idMaestroCarga);
		logger.debug("llamada de proceso en el java: " + "{call CB_CONCILIACION_SP(" + idMaestroCarga + ")}");
		
		int result = 0;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();

			callableStatement = con.prepareStatement(EJECUTA_PROCESO_CB_CONCILIACION_SP);
			callableStatement.setInt(1, Integer.parseInt(idMaestroCarga));
			result = callableStatement.executeUpdate();
		} catch (Exception e) {
			logger.error("ejecutaProcesoConciliacion()" + " - Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e) {
					logger.error("ejecutaProcesoConciliacion()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ejecutaProcesoConciliacion()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}

		return result;
	}
	/*
	 * // Llama a proceso CB_AJUSTES private static String
	 * EJECUTA_PROCESO_CB_AJUSTES_SP = "{call CB_AJUSTES(?, ?)}";
	 * 
	 * public void ejecutaProcesoAjustes(String fechaArchivo, int agenciaId, String
	 * formato) { CallableStatement callableStatement = null;
	 * 
	 * String lecturafor = formato.replace("mi".toLowerCase(), "mm".toLowerCase());
	 * lecturafor = lecturafor.replace("hh24", "HH");
	 * System.out.println("Formato para fecha: " + lecturafor); DateFormat dfInicio
	 * = new SimpleDateFormat(lecturafor); Date date = null; try { date =
	 * dfInicio.parse(fechaArchivo); } catch (ParseException e2) {
	 * Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
	 * } DateFormat df = new SimpleDateFormat("dd/MM/yy");
	 * System.out.println("Fecha del archivo: " + fechaArchivo); fechaArchivo =
	 * df.format(date);
	 * 
	 * System.out.println("Fecha Archivo: " + fechaArchivo);
	 * System.out.println("id: " + agenciaId); Connection con = null; try { con =
	 * ControladorBase.obtenerDtsPromo().getConnection();
	 * 
	 * callableStatement = con.prepareCall(EJECUTA_PROCESO_CB_AJUSTES_SP);
	 * callableStatement.setString(1, fechaArchivo); callableStatement.setInt(2,
	 * agenciaId); callableStatement.execute(); } catch (Exception e) {
	 * Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
	 * } finally { if (callableStatement != null) try { callableStatement.close(); }
	 * catch (SQLException e1) {
	 * Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
	 * } if (con != null) try { con.close(); } catch (SQLException e) {
	 * Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
	 * } } }
	 * 
	 * private static String EJECUTA_PROCESO_CB_AJUSTES_PENDIENTES_SP =
	 * "{call CB_AJUSTES_PENDIENTES(?, ?)}";
	 * 
	 * public void ejecutaProcesoAjustesPendientes(String fechaArchivo, int
	 * agenciaId, String formato) throws ParseException { CallableStatement
	 * callableStatement = null;
	 * 
	 * String lecturafor = formato.replace("mi".toLowerCase(), "mm".toLowerCase());
	 * lecturafor = lecturafor.replace("hh24", "HH");
	 * System.out.println("Formato para fecha: " + lecturafor); DateFormat dfInicio
	 * = new SimpleDateFormat(lecturafor); Date date = dfInicio.parse(fechaArchivo);
	 * 
	 * DateFormat df = new SimpleDateFormat("dd/MM/yy");
	 * System.out.println("Fecha del archivo: " + fechaArchivo); fechaArchivo =
	 * df.format(date);
	 * 
	 * System.out.println("Fecha Archivo: " + fechaArchivo);
	 * System.out.println("id: " + agenciaId); Connection con = null; try { con =
	 * ControladorBase.obtenerDtsPromo().getConnection(); // try {
	 * 
	 * callableStatement =
	 * con.prepareCall(EJECUTA_PROCESO_CB_AJUSTES_PENDIENTES_SP);
	 * callableStatement.setString(1, fechaArchivo); callableStatement.setInt(2,
	 * agenciaId); callableStatement.execute(); // } finally { // con.close(); // }
	 * } catch (Exception e) {
	 * Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
	 * } finally { if (callableStatement != null) try { callableStatement.close(); }
	 * catch (SQLException e1) {
	 * Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
	 * } if (con != null) try { con.close(); } catch (SQLException e) {
	 * Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
	 * } }
	 * 
	 * }
	 */

	// metodos solo para GT
	// Llama a proceso CB_AJUSTES
	private static String EJECUTA_PROCESO_CB_AJUSTES_SP = "{call CB_AJUSTES(?, ?)}";

	public void ejecutaProcesoAjustes(String fechaArchivo, int agenciaId) {
		CallableStatement callableStatement = null;
		logger.debug("Fecha Archivo: " + fechaArchivo);
		logger.debug("id: " + agenciaId);
		
		Connection conexion = null;
		try {

			conexion = ControladorBase.obtenerDtsPromo().getConnection();
			callableStatement = conexion.prepareCall(EJECUTA_PROCESO_CB_AJUSTES_SP);
			callableStatement.setString(1, fechaArchivo);
			callableStatement.setInt(2, agenciaId);
			callableStatement.execute();
		} catch (Exception e) {
			logger.error("ejecutaProcesoAjustes()" + " - Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conexion != null)
				try {
					conexion.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("ejecutaProcesoAjustes()" + " - Error", e);
					e.printStackTrace();
				}
			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e1) {
					logger.error("ejecutaProcesoAjustes()" + " - Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			}
		}
	}

	private static String EJECUTA_PROCESO_CB_AJUSTES_PENDIENTES_SP = "{call CB_AJUSTES_PENDIENTES(?, ?)}";

	public void ejecutaProcesoAjustesPendientes(String fechaArchivo, int agenciaId) {
		CallableStatement callableStatement = null;
		Connection conexion = null;
		logger.debug("Fecha Archivo: " + fechaArchivo);
		logger.debug("id: " + agenciaId);
		
		try {

			conexion = ControladorBase.obtenerDtsPromo().getConnection();

			callableStatement = conexion.prepareCall(EJECUTA_PROCESO_CB_AJUSTES_PENDIENTES_SP);
			callableStatement.setString(1, fechaArchivo);
			callableStatement.setInt(2, agenciaId);
			callableStatement.execute();

		} catch (Exception e) {
			logger.error("ejecutaProcesoAjustesPendientes()" + " - Error", e);
		//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conexion != null)
				try {
					conexion.close();
				} catch (SQLException e) {
					logger.error("ejecutaProcesoAjustesPendientes()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}

			if (callableStatement != null) {
				try {
					callableStatement.close();

				} catch (SQLException e1) {
					logger.error("ejecutaProcesoAjustesPendientes()" + " - Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			}
		}

	}

	// fin

	// Llama a proceso CB_CONCILIACION_SP
	private static String EJECUTA_PROCESO_CB_CARCA_COONFRONTAS = "{call cb_carga_confrontas_sp(?)}";

	public int ejecutaProcesoCargaConfrontas(String idMaestroCarga) {
		CallableStatement callableStatement = null;
		int result = 0;
		logger.debug("Parametro para el proceso carga confronta: " + idMaestroCarga);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
			//	"Parametro para el proceso carga confronta: " + idMaestroCarga);
		logger.debug("llamada de proceso en el java: " + "{call cb_carga_confrontas_sp(" + idMaestroCarga + ")}");
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//"llamada de proceso en el java: " + "{call cb_carga_confrontas_sp(" + idMaestroCarga + ")}");
		Connection conexion = null;
		try {

			conexion = ControladorBase.obtenerDtsPromo().getConnection();
			callableStatement = conexion.prepareCall(EJECUTA_PROCESO_CB_CARCA_COONFRONTAS);
			callableStatement.setInt(1, Integer.parseInt(idMaestroCarga));
			result = callableStatement.executeUpdate();
		} catch (Exception e) {
			logger.error("ejecutaProcesoCargaConfrontas()" + " - Error", e);
		//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			insertLog(idMaestroCarga, e.getMessage(), "");
		} finally {
			if (conexion != null)
				try {
					conexion.close();
				} catch (SQLException e) {
					logger.error("ejecutaProcesoCargaConfrontas()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}

			try {
				if (callableStatement != null)
					callableStatement.close();

			} catch (SQLException e) {
				logger.error("ejecutaProcesoCargaConfrontas()" + " - Error", e);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	private static String CONSULTA_ARCHIVO = "Select id_archivos_insertados idArchivosViejos, nombre_archivo nombreArchivo "
			+ "from cb_archivos_insertados where 1=1";
	// Borra todos los registros de la tabla concilaciones del archivo
	// selecionado
	private String BORRAR_CONCILACIONES = "DELETE " + " cb_conciliacion " + " WHERE cbdatabancoid IN "
			+ " (SELECT cbdatabancoid FROM cb_data_banco WHERE ID_ARCHIVOS_INSERTADOS = ? " + " ) ";

	// elimina registros pendientes
	private String BORRA_CONCILIACIONES_PENDIENTES = "delete " + " FROM cb_conciliacion "
			+ " WHERE tipo                    = 1 " + " AND cbbancoagenciaconfrontaid = ? "
			+ " AND dia                       = TO_DATE(?, 'dd/MM/yy') " + " AND (cbpagosid               IS  NULL "
			+ " OR cbdatabancoid             IS  NULL)";

	// Borra el archivo con el id maestro
	private static String BORRAR_ARCHIVO = "Delete cb_archivos_insertados " + " where id_archivos_insertados = ?  ";
	// Borra los registros con el id maestro
	private static String BORRAR_REGISTRO = "Delete cb_data_banco " + " where id_archivos_insertados = ?";
	// Borra los registros que no proceso
	private static String BORRAR_NO_REGISTRADOS = "Delete cb_data_sin_procesar " + " where id_archivos_insertados = ?";

	// Borra registros de comisiones
	private static String DELETE_COMISIONES_ARCHIVO = "DELETE FROM CB_COMISIONES "
			+ "WHERE CBDATABANCOID IN (SELECT CBDATABANCOID FROM CB_DATA_BANCO " + "WHERE ID_ARCHIVOS_INSERTADOS = ?)";

	// consulta si el archivo existe
	public boolean consultaExistenciaArchivo(String archivo, String idArchivoNuevo) {
		boolean valor = false;
		Statement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			List<CBDataBancoModel> listadoConfig = new ArrayList<CBDataBancoModel>();
			listadoConfig = obtenerValidacionConf();
			String valor1 = "", valor2 = "", valor3 = "", where = "";
			for (CBDataBancoModel d : listadoConfig) {
				valor1 = d.getValorObjeto1();
				valor2 = d.getValorObjeto2();
				valor3 = d.getValorObjeto3();
			}
			if (valor1.toUpperCase().equals("S")) {
				logger.debug("\n*** VALIDACION ARCHIVO EXISTE LA REALIZA POR NOMBRE DEL ARCHIVO ***\n");
				
				where += " AND id_archivos_insertados !=  '" + idArchivoNuevo + "' AND NOMBRE_ARCHIVO = '" + archivo
						+ "'";
			} else if (valor2.toUpperCase().equals("S")) {
				logger.debug("\n*** VALIDACION ARCHIVO EXISTE LA REALIZA POR FECHA DE CREACION DEL ARCHIVO ***\n");
				
				where += " AND id_archivos_insertados !=  '" + idArchivoNuevo
						+ "'  AND trunc(FECHA_CREACION) = trunc(sysdate)";
			} else if (valor3.toUpperCase().equals("S")) {
				logger.debug("\n*** VALIDACION ARCHIVO EXISTE LA REALIZA POR NOMBRE Y FECHA DE CREACION DEL ARCHIVO ***\n");
				
				where += " AND id_archivos_insertados !=  '" + idArchivoNuevo + "' AND NOMBRE_ARCHIVO = '" + archivo
						+ "' AND trunc(FECHA_CREACION) = trunc(sysdate)";
			}
			logger.debug("archivo = " + archivo);
			logger.debug("idArchivoNuevo = " + idArchivoNuevo);
			logger.debug("Consulta existencia archivo = " + CONSULTA_ARCHIVO + where);
		
			ps = con.createStatement();
			rs = ps.executeQuery(CONSULTA_ARCHIVO + where);
			if (rs.next()) {
				valor = true;
				logger.debug("Archivo existe");
				
			} else {
				logger.debug("Archivo no existe");
				
			}
			// if (rs.getString("nombreArchivo").compareTo(archivo) == 0) {
			// valor = true;
			// }
		} catch (Exception e) {
			logger.error("consultaExistenciaArchivo()" + " - Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("consultaExistenciaArchivo()" + " - Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.error("consultaExistenciaArchivo()" + " - Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("consultaExistenciaArchivo()" + " - Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		System.out.println("******************************\n");
		return valor;

	}

	private static String CONSULTA_ARCHIVO_DELETE = "Select id_archivos_insertados idArchivosViejos, nombre_archivo nombreArchivo "
			+ "from cb_archivos_insertados where nombre_archivo = ? AND id_archivos_insertados != ?";

	// Consulta existencia de archivo de fechas multiples
	private static String CONSULTA_ARCHIVO_MULTIPLE_DELETE = "SELECT id_archivos_insertados idArchivosViejos, nombre_archivo nombreArchivo"
			+ " FROM CB_ARCHIVOS_INSERTADOS WHERE 1=1";

	// borra el archivo y los registros
	public void borrarArchivo(String nombre, String idArchivo, int cbBancoAgenciaConfrontaId, String fechaConciliacion,
			boolean multiple) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement st = null;
		Connection con = null;
		logger.debug("nombre de archivo: " + nombre);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "nombre de archivo: " + nombre);
		logger.debug("ID archivo nuevo carga: " + idArchivo);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "ID archivo nuevo carga: " + idArchivo);
		logger.debug("fecha conciliacion:" + fechaConciliacion);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "fecha conciliacion:" + fechaConciliacion);
		logger.debug("ID BANCO AGENCIA CONFRONTA: " + cbBancoAgenciaConfrontaId);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
			//	"ID BANCO AGENCIA CONFRONTA: " + cbBancoAgenciaConfrontaId);

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		try {
			fechaConciliacion = df.format(df.parse(fechaConciliacion));

			con = obtenerDtsPromo().getConnection();
			logger.debug("***Parametros enviados para DELETE de archivos duplicados ***");
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "");
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//	"***Parametros enviados para DELETE de archivos duplicados ***");
			logger.debug("Nombre archivos para borrar = " + nombre);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "Nombre archivos para borrar = " + nombre);
			logger.debug("ID archivo a ignorar en el delete = " + idArchivo);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//	"ID archivo a ignorar en el delete = " + idArchivo);
			logger.debug("Consulta archivo antes de borrar = " + CONSULTA_ARCHIVO_DELETE);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//	"Consulta archivo antes de borrar = " + CONSULTA_ARCHIVO_DELETE);
		//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "**********\n");

			if (multiple) {
				String where = "";
				logger.debug("**** Entra a eliminar archivo multiple...");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"**** Entra a eliminar archivo multiple...");
				where = " and nombre_archivo LIKE '%" + nombre + "%' and id_archivos_insertados != " + idArchivo;
				st = con.createStatement();
				// CONSULTA_ARCHIVO_MULTIPLE_DELETE =
				// CONSULTA_ARCHIVO_MULTIPLE_DELETE + where;

				rs = st.executeQuery(CONSULTA_ARCHIVO_MULTIPLE_DELETE + where);
				logger.debug("Consulta previa a eliminacion de archivo multiple: "
						+ CONSULTA_ARCHIVO_MULTIPLE_DELETE + where);
				
			} else {
				logger.debug("**** Entra a eliminar archivo simple...");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"**** Entra a eliminar archivo simple...");
				logger.debug("Consulta previa a eliminacion de archivo simple: " + CONSULTA_ARCHIVO_DELETE);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"Consulta previa a eliminacion de archivo simple: " + CONSULTA_ARCHIVO_DELETE);
				ps = con.prepareStatement(CONSULTA_ARCHIVO_DELETE);
				ps.setString(1, nombre);
				ps.setString(2, idArchivo);
				rs = ps.executeQuery();
			}

			while (rs.next()) {
				String resultado = rs.getString("idArchivosViejos");
				logger.debug("ID del archivo a eliminar: " + resultado);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
					//	"ID del archivo a eliminar: " + resultado);
				int ejecuta = 0;

				/**
				 * 1- Borra registros de comisiones
				 */
				logger.debug("1 -> Elimina registros de comisiones...");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
					//	"1 -> Elimina registros de comisiones...");
				ps = con.prepareStatement(DELETE_COMISIONES_ARCHIVO);
				ps.setString(1, resultado);
				logger.debug("ID de archivo a eliminar: " + resultado);
				logger.debug("Delete comisiones: " + DELETE_COMISIONES_ARCHIVO);
				
				ejecuta = ps.executeUpdate();
				if (ps != null)
					ps.close();
				logger.debug("Se han eliminado registros de comisiones...  Valor ejecucion: " + ejecuta);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"Se han eliminado registros de comisiones...  Valor ejecucion: " + ejecuta);

				/**
				 * 2 - Limpia conciliaciones
				 */
				logger.debug("2 -> Elimina conciliaciones...");
				///Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "2 -> Elimina conciliaciones...");
				ps = con.prepareStatement(BORRAR_CONCILACIONES);
				ps.setString(1, resultado);
				ejecuta = ps.executeUpdate();
				if (ps != null)
					ps.close();
				logger.debug("2 -> Elimina conciliaciones...");
				logger.debug("Delete conciliaciones: " + BORRAR_CONCILACIONES);
				logger.debug("Las conciliaciones fueron borradas... Valor ejecucion: " + ejecuta);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"Las conciliaciones fueron borradas... Valor ejecucion: " + ejecuta);

				/**
				 * 3 - Limpia conciliaciones pendientes
				 */
				logger.debug("3-> Elimina conciliaciones pendientes...");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"3-> Elimina conciliaciones pendientes...");
				ps = con.prepareStatement(BORRA_CONCILIACIONES_PENDIENTES);
				ps.setInt(1, cbBancoAgenciaConfrontaId);
				ps.setString(2, fechaConciliacion);
				ejecuta = ps.executeUpdate();
				if (ps != null)
					ps.close();
				logger.debug("ID de archivo a eliminar: " + resultado);
				logger.debug("Delete conciliaciones pendientes: " + BORRA_CONCILIACIONES_PENDIENTES);
				logger.debug("Las conciliaciones pendientes fueron borradas...  Valor ejecucion: " + ejecuta);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"Las conciliaciones pendientes fueron borradas...  Valor ejecucion: " + ejecuta);

				/**
				 * 4 - Limpia cb_data_banco
				 */
				logger.debug("4 -> Elimina data banco...");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "4 -> Elimina data banco...");
				ps = con.prepareStatement(BORRAR_REGISTRO);
				ps.setString(1, resultado);
				ejecuta = ps.executeUpdate();
				if (ps != null)
					ps.close();
				logger.debug("ID de archivo a eliminar: " + resultado);
				logger.debug("Delete data banco: " + BORRAR_REGISTRO);
				logger.debug("Los registros data banco fueron borrados...  Valor ejecucion: " + ejecuta);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"Los registros data banco fueron borrados...  Valor ejecucion: " + ejecuta);
				// *****************************************************************

				/**
				 * 5 - Limpia data no registrada
				 */
				logger.debug("5 -> Elimina data sin procesar...");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "5 -> Elimina data sin procesar...");
				ps = con.prepareStatement(BORRAR_NO_REGISTRADOS);
				ps.setString(1, resultado);
				ejecuta = ps.executeUpdate();
				if (ps != null)
					ps.close();
				logger.debug("ID de archivo a eliminar: " + resultado);
				logger.debug("Delete data no procesada: " + BORRAR_NO_REGISTRADOS);
				logger.debug("Se ha eliminado data sin procesar...  Valor ejecucion: " + ejecuta);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
					//	"Se ha eliminado data sin procesar...  Valor ejecucion: " + ejecuta);
				// ******************************************************************

				/**
				 * 6- Borra archivo(s) en cb_archivos_insertados
				 */
				logger.debug("6 -> Elimina los archivos anteriores en CB_ARCHIVOS_INSERTADOS...");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
					//	"6 -> Elimina los archivos anteriores en CB_ARCHIVOS_INSERTADOS...");
				ps = con.prepareStatement(BORRAR_ARCHIVO);
				ps.setString(1, resultado);
				logger.debug("ID de archivo anterior a eliminar: " + resultado);
				logger.debug("Delete archivos anteriores: " + BORRAR_ARCHIVO);
				
				ejecuta = ps.executeUpdate();
				if (ps != null)
					ps.close();
				logger.debug("El archivo fue borrado de tabla CB_ARCHIVOS_INSERTADOS... Valor ejecucion: " + ejecuta);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"El archivo fue borrado de tabla CB_ARCHIVOS_INSERTADOS... Valor ejecucion: " + ejecuta);

			}
		} catch (Exception e) {
			logger.error("borrarArchivo()" + "- Erorr", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("borrarArchivo()" + "- Erorr", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					logger.error("borrarArchivo()" + "- Erorr", e);
				//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("borrarArchivo()" + "- Erorr", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("borrarArchivo()" + "- Erorr", e);
				//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	// Borra archivo que se iba a cargar
	private static String BORRAR_ARCHIVO_PRECARGADO = "Delete cb_archivos_insertados "
			+ " where id_archivos_insertados = ? and nombre_archivo = ? ";

	public void borraArchivoPrecargado(String nombre, String idArchivo) {
		PreparedStatement ps = null;
		Connection con = null;
		int result = 0;
		logger.debug("nombre de archivo: " + nombre);
		logger.debug("id archivo nuevo carga: " + idArchivo);
		
		try {

			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(BORRAR_ARCHIVO_PRECARGADO);
			ps.setString(1, idArchivo);
			ps.setString(2, nombre);

			result = ps.executeUpdate();
			logger.debug("el archivo pre-cargado fue borrado: " + result);
			
		} catch (Exception e) {
			logger.error("borraArchivoPrecargado()" + "- Erorr", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.error("borraArchivoPrecargado()" + "- Erorr", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("borraArchivoPrecargado()" + "- Erorr", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	// Verifica si la fecha ya fue cargada

	public boolean verificaCargaDataBanco(String fechaVerificar, String banco, String agencia, String confronta,
			String formatoFecha) {

		boolean verifica = false;
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;

		String VERIFICA_CARGA_DATA_BANCO = "SELECT * FROM CB_DATA_BANCO " + " WHERE TRUNC(DIA) = TRUNC(to_date(?,'"
				+ formatoFecha + "')) " + " AND CBCATALOGOBANCOID = ? " + " AND CBCATALOGOAGENCIAID = ? "
				+ " AND CBBANCOAGENCIACONFRONTAID = ?";
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(VERIFICA_CARGA_DATA_BANCO);

			logger.debug("query valida data banco " + VERIFICA_CARGA_DATA_BANCO);
			
			ps.setString(1, fechaVerificar);
			ps.setString(2, banco);
			ps.setString(3, agencia);
			ps.setString(4, confronta);
			rs = ps.executeQuery();
			if (rs.next()) {
				verifica = true;
			}
		} catch (Exception e) {
			logger.error("verificaCargaDataBanco()" + "- Erorr", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("verificaCargaDataBanco()" + "- Erorr", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.error("verificaCargaDataBanco()" + "- Erorr", e1);
				//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("verificaCargaDataBanco()" + "- Erorr", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return verifica;
	}

	// string de llamada
	private String LLAMADA_SP_PAGOS_IND = "{call CB_PAGO_INDIVIDUAL(?, to_date(?, 'dd/MM/yy'), to_date(?, 'dd/MM/yy'))}";

	/**
	 * Metodo para ejecutar el proceso de pagos individual
	 * 
	 * @throws ParseException
	 */
	public void ejecutaSPPagosIndividual(int cbBancoAgenciaConfrontaId, String fechaArchivo, String formato)
			throws ParseException {

		String lecturafor = formato.replace("mi".toLowerCase(), "mm".toLowerCase());
		lecturafor = lecturafor.replace("hh24", "HH");
		logger.debug("Formato para fecha: " + lecturafor);
		
		DateFormat dfInicio = new SimpleDateFormat(lecturafor);
		Date date = dfInicio.parse(fechaArchivo);
		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		logger.debug("Fecha del archivo: " + fechaArchivo);
		
		fechaArchivo = df.format(date);
		logger.debug("id:" + cbBancoAgenciaConfrontaId);
		logger.debug("fecha: " + fechaArchivo);
		

		CallableStatement callableStatement = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			callableStatement = con.prepareCall(LLAMADA_SP_PAGOS_IND);
			callableStatement.setInt(1, cbBancoAgenciaConfrontaId);
			callableStatement.setString(2, fechaArchivo);
			callableStatement.setString(3, fechaArchivo);
			callableStatement.execute();
		} catch (Exception e) {
			logger.error("ejecutaSPPagosIndividual()" + "- Erorr", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (callableStatement != null)
				try {
					callableStatement.close();
				} catch (SQLException e1) {
					logger.error("ejecutaSPPagosIndividual()" + "- Erorr", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ejecutaSPPagosIndividual()" + "- Erorr", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	/**
	 * Modificado ultima vez por Carlos Godinez - Qitcorp - 24/04/2017
	 * 
	 */

	private String CONSULTA_IDBAC = "SELECT cbbancoagenciaconfrontaid, NVL(cbagenciasconfrontaid, 0) ageConfrId "
			+ "FROM cb_banco_agencia_confronta " + "WHERE cbcatalogobancoid        = ? "
			+ "AND cbcatalogoagenciaid        = ? " + "AND cbconfiguracionconfrontaid = ? ";

	public int obtieneIdBancoAgeConfro(int idBanco, int idAgencia, int idConfronta) {
		int id = 0;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			logger.debug("Consulta id banco agencia confronta = " + CONSULTA_IDBAC);
			
			ps = con.prepareStatement(CONSULTA_IDBAC);
			ps.setInt(1, idBanco);
			ps.setInt(2, idAgencia);
			ps.setInt(3, idConfronta);

			rs = ps.executeQuery();
			if (rs.next()) {
				id = rs.getInt(1);
				if (rs.getInt(2) != 0) {
					logger.debug("\n\n ---- CBAGENCIASCONFRONTAID lleva valor ----\n");
				
					id = rs.getInt(2);
				}
			}
		} catch (Exception e) {
			logger.error("obtieneIdBancoAgeConfro()" + "- Erorr", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("obtieneIdBancoAgeConfro()" + "- Erorr", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.error("obtieneIdBancoAgeConfro()" + "- Erorr", e1);
				//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("obtieneIdBancoAgeConfro()" + "- Erorr", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		logger.debug("ID BANCO AGENCIA CONFRONTA DEVUELTO = " + id);
		
		return id;
	}

	/**
	 * Agrega ovidio para obtener el tipo
	 * 
	 */

	private String CONSULTA_TIPO = "SELECT tipo " + "FROM cb_banco_agencia_confronta "
			+ "WHERE cbcatalogobancoid        = ? " + "AND cbcatalogoagenciaid        = ? "
			+ "AND cbconfiguracionconfrontaid = ? ";

	public String obtieneTipo(int idBanco, int idAgencia, int idConfronta) {
		String tipo = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		logger.debug("Tipo en el dao query " + CONSULTA_TIPO);
		logger.debug("Tipo en el dao query " + idBanco + idAgencia + idConfronta);
		
		try {
			con = obtenerDtsPromo().getConnection();
			logger.debug("Consulta id banco agencia confronta = " + CONSULTA_TIPO);
			
			ps = con.prepareStatement(CONSULTA_TIPO);
			ps.setInt(1, idBanco);
			ps.setInt(2, idAgencia);
			ps.setInt(3, idConfronta);

			rs = ps.executeQuery();
			if (rs.next()) {
				tipo = rs.getString(1);

			}
		} catch (Exception e) {
			logger.error("obtieneTipo()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("obtieneTipo()" + "- Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.error("obtieneTipo()" + "- Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("obtieneTipo()" + "- Error", e);
				//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		logger.debug("ID BANCO AGENCIA CONFRONTA DEVUELTO = " + tipo);
		
		return tipo;
	}

	private String RECUPERA_COMISION = "SELECT NVL(COMISION,0) FROM CB_BANCO_AGENCIA_CONFRONTA WHERE CBBANCOAGENCIACONFRONTAID = ?";

	public BigDecimal recuperaComision(int idConfronta) {
		BigDecimal res = new BigDecimal("0.00");
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(RECUPERA_COMISION);
			ps.setInt(1, idConfronta);
			rs = ps.executeQuery();
			rs.next();
			res = rs.getBigDecimal(1);
		} catch (Exception e) {
			logger.error("recuperaComision()" + "- Error", e);
		//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("recuperaComision()" + "- Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.error("recuperaComision()" + "- Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("recuperaComision()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return res;
	}

	/**
	 * Agregado por Carlos Godinez - QitCorp - 19/04/2017
	 * 
	 */

	private String QRY_ARCHIVO_UNICO = "SELECT VALOR_OBJETO1, VALOR_OBJETO2, VALOR_OBJETO3 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CARGA_CONFRONTAS' " + "AND TIPO_OBJETO = 'VALIDACION_ARCHIVOS' " + "AND ESTADO = 'S'";

	public List<CBDataBancoModel> obtenerValidacionConf() throws SQLException {
		List<CBDataBancoModel> list = new ArrayList<CBDataBancoModel>();
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			cmd = conn.prepareStatement(QRY_ARCHIVO_UNICO);
			rs = cmd.executeQuery();
			CBDataBancoModel objeBean;
			while (rs.next()) {
				objeBean = new CBDataBancoModel();
				objeBean.setValorObjeto1(rs.getString(1));
				objeBean.setValorObjeto2(rs.getString(2));
				objeBean.setValorObjeto3(rs.getString(3));
				list.add(objeBean);
			}
		} catch (Exception e) {
			logger.error("obtenerValidacionConf()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("obtenerValidacionConf()" + "- Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					logger.error("obtenerValidacionConf()" + "- Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("obtenerValidacionConf()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}

	/**
	 * Agregado por Carlos Godinez - QitCorp - 26/05/2017
	 * 
	 */

	private String QRY_FORMATO_FECHA = "SELECT FORMATO_FECHA FROM CB_CONFIGURACION_CONFRONTA WHERE CBCONFIGURACIONCONFRONTAID = ?";

	public String obtenerFormatoFechaConfronta(int id) {
		String valor = "";
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			logger.debug("Consulta para obtener formato fecha = " + QRY_FORMATO_FECHA);
			
			cmd = conn.prepareStatement(QRY_FORMATO_FECHA);
			cmd.setInt(1, id);
			rs = cmd.executeQuery();
			while (rs.next()) {
				valor = rs.getString(1);
			}
		} catch (Exception e) {
			logger.error("obtenerFormatoFechaConfronta()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("obtenerFormatoFechaConfronta()" + "- Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					logger.error("obtenerFormatoFechaConfronta()" + "- Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("obtenerFormatoFechaConfronta()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		logger.debug("Valor devuelto de formato fecha: " + valor);
		
		return valor;
	}

	private String QRY_LINEA_LECTURA = "SELECT NVL(LINEA_LECTURA, 0) linea FROM CB_CONFIGURACION_CONFRONTA WHERE CBCONFIGURACIONCONFRONTAID = ?";

	public int obtenerLineaLectura(int id) {
		int valor = 0;
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			logger.debug("\n======== Obtiene linea de inicio de lectura de archivo ========\n");
			logger.debug("Id configuracion confronta enviado = " + id);
			logger.debug("Consulta para obtener linea donde se empezara a leer = " + QRY_LINEA_LECTURA);
			
			cmd = conn.prepareStatement(QRY_LINEA_LECTURA);
			cmd.setInt(1, id);
			rs = cmd.executeQuery();
			while (rs.next()) {
				valor = rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("obtenerLineaLectura()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("obtenerLineaLectura()" + "- Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					logger.error("obtenerLineaLectura()" + "- Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("obtenerLineaLectura()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		logger.debug("** Linea donde se empezara a leer: " + valor);
		logger.debug("\n===============================================================\n");
		
		return valor;
	}

	/**
	 * Agregado por Carlos Godinez - QitCorp - 30/08/2017
	 * 
	 */

	private String QRY_CANTIDAD_AGRUPACION = "SELECT NVL(CANTIDAD_AGRUPACION, 0) CANTIDAD_AGRUPACION "
			+ "FROM CB_CONFIGURACION_CONFRONTA WHERE CBCONFIGURACIONCONFRONTAID = ?";

	public int obtenerCantidadAgrupacion(int id) {
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			logger.debug("Consulta para obtener cantidad de agrupacion = " + QRY_CANTIDAD_AGRUPACION);
			
			cmd = conn.prepareStatement(QRY_CANTIDAD_AGRUPACION);
			cmd.setInt(1, id);
			rs = cmd.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			logger.error("obtenerCantidadAgrupacion()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			return 0;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("obtenerCantidadAgrupacion()" + "- Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			}
			if (cmd != null) {
				try {
					cmd.close();
				} catch (SQLException e1) {
					logger.error("obtenerCantidadAgrupacion()" + "- Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("obtenerCantidadAgrupacion()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return 0;
	}

	// Query para obtener la cantidad de fechas diferentes en la confronta
	private static String GET_DATE_CONFRONTAS = "SELECT count(DISTINCT trunc(fecha)) as total " + "FROM cb_data_banco "
			+ "WHERE id_archivos_insertados = ?";

	/*
	 * Verificacion de confrontas con mas de una fecha en sus registros
	 * 
	 * @Author Nicolas Bermudez
	 */
	public int getDateConfronta(String idArchivo) {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Connection con = null;

		logger.debug("Parametro para el proceso: " + idArchivo);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "Parametro para el proceso: " + idArchivo);
		logger.debug("llamada al query en el java: " + GET_DATE_CONFRONTAS + " " + idArchivo);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//"llamada al query en el java: " + GET_DATE_CONFRONTAS + " " + idArchivo);
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(GET_DATE_CONFRONTAS);
			ps.setString(1, idArchivo);
			rs = ps.executeQuery();

			while (rs.next()) {
				result = rs.getInt("total");
			}
			logger.debug("Cantidad de fechas diferentes en la confronta: " + result);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
					//"Cantidad de fechas diferentes en la confronta: " + result);
		} catch (Exception e) {
			logger.error("getDateConfronta()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("getDateConfronta()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("getDateConfronta()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("getDateConfronta()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Added by CarlosGodinez -> 20/09/2018 Verificacion de cantidad de convenios de
	 * la confronta cargada
	 */
	private static String QRY_CANT_CONVENIOS_CONFRONTA = "SELECT COUNT(DISTINCT tipo) as total "
			+ "FROM cb_data_banco WHERE id_archivos_insertados = ?";

	public int getCantidadConvenios(String idArchivo) {
		int result = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;

		Connection con = null;

		logger.debug("Parametro para el proceso: " + idArchivo);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "Parametro para el proceso: " + idArchivo);
		logger.debug("llamada al query en el java: " + QRY_CANT_CONVENIOS_CONFRONTA);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//"llamada al query en el java: " + QRY_CANT_CONVENIOS_CONFRONTA);
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(QRY_CANT_CONVENIOS_CONFRONTA);
			ps.setString(1, idArchivo);
			rs = ps.executeQuery();

			while (rs.next()) {
				result = rs.getInt("total");
			}
			logger.debug("Cantidad de convenios diferentes en la confronta: " + result);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//	"Cantidad de convenios diferentes en la confronta: " + result);
		} catch (Exception e) {
			logger.error("getCantidadConvenios()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("getCantidadConvenios()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("getCantidadConvenios()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("getCantidadConvenios()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/*
	 * @Author Nicolas Bermudez
	 * 
	 * Proceso de separacion de fechas
	 * 
	 */

	private static String EJECUTA_PROCESO_CB_FECHAS_CONFRONTAS_SP = "{call CB_FECHAS_CONFRONTAS_SP(?)}";

	public int ejecutaProcesoSeparacionFechasConfronta(String idMaestroCarga) {
		PreparedStatement ps = null;
		Connection con = null;

		logger.debug("Parametro para el proceso: " + idMaestroCarga);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//"Parametro para el proceso: " + idMaestroCarga);
		logger.debug("llamada de proceso en el java: " + "{call CB_FECHAS_CONFRONTAS_SP(" + idMaestroCarga + ")}");
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//"llamada de proceso en el java: " + "{call CB_FECHAS_CONFRONTAS_SP(" + idMaestroCarga + ")}");

		int result = 0;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(EJECUTA_PROCESO_CB_FECHAS_CONFRONTAS_SP);
			ps.setInt(1, Integer.parseInt(idMaestroCarga));
			result = ps.executeUpdate();
		} catch (Exception e) {
			logger.error("ejecutaProcesoSeparacionFechasConfronta()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				logger.error("ejecutaProcesoSeparacionFechasConfronta()" + "- Error", e);
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ejecutaProcesoSeparacionFechasConfronta()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return result;
	}

	// Consulta existencia de archivo de fechas multiples
	private static String CONSULTA_ARCHIVO_MULTIPLE = "SELECT COUNT(nombre_archivo) as total FROM CB_ARCHIVOS_INSERTADOS "
			+ "WHERE 1=1";

	/*
	 * @Author Nicolas Bermudez
	 * 
	 * Verifica si existe el nombre del archivo duplicado de fechas multiples
	 * 
	 */
	public boolean consultaExistenciaArchivoMultiple(String archivo, String idArchivoNuevo) {
		boolean result = false;
		Connection con = null;
		Statement ps = null;
		ResultSet rs = null;

		String where = " and nombre_archivo LIKE '%" + archivo + "%' and id_archivos_insertados!=" + idArchivoNuevo;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			logger.debug("archivo = " + archivo);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "archivo = " + archivo);
			logger.debug("idArchivoNuevo = " + idArchivoNuevo);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "idArchivoNuevo = " + idArchivoNuevo);
			logger.debug("Consulta existencia archivo = " + CONSULTA_ARCHIVO_MULTIPLE + where);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
				//	"Consulta existencia archivo = " + CONSULTA_ARCHIVO_MULTIPLE + where);
			ps = con.createStatement();
			rs = ps.executeQuery(CONSULTA_ARCHIVO_MULTIPLE + where);
			while (rs.next()) {
				if (rs.getInt("total") > 0) {
					logger.debug("El archivo " + archivo + " ya esta registrado.");
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
							//"El archivo " + archivo + " ya esta registrado.");
					result = true;
				}
			}
		} catch (Exception e) {
			logger.error("consultaExistenciaArchivoMultiple()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("consultaExistenciaArchivoMultiple()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("consultaExistenciaArchivoMultiple()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("consultaExistenciaArchivoMultiple()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/*
	 * @Author Nicolas Bermudez
	 * 
	 * Permite registrar log
	 */

	private String INSERT_LOG = "INSERT INTO CB_PROCESOS_CONTROL VALUES(?,sysdate)";

	public void insertLog(String element_id, String descripcion, String tipo) {
		PreparedStatement stm = null;
		Connection conexion = null;

		try {
			conexion = ControladorBase.obtenerDtsPromo().getConnection();
			stm = conexion.prepareStatement(INSERT_LOG);
			
			stm.setString(1, "Id archivo insertado: " + element_id + "_" + descripcion);
			stm.executeUpdate();

		} catch (Exception e) {
			logger.error("insertLog()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					logger.error("insertLog()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if (conexion != null) {
				try {
					conexion.close();
				} catch (SQLException e) {
					logger.error("insertLog()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}

	}

	/*
	 * metodos que son propios de Guatemala para version unificada
	 * 
	 */

	// string de llamada
	// private String LLAMADA_SP_PAGOS_INDGT = "{call CB_PAGO_INDIVIDUAL(?,
	// to_date(?, 'dd/MM/yy'), to_date(?, 'dd/MM/yy'))}";

	/**
	 * Metodo para ejecutar el proceso de pagos individual
	 * 
	 * @throws ParseException
	 */
	public void ejecutaSPPagosIndividualGT(int cbBancoAgenciaConfrontaId, String fechaArchivo) throws ParseException {

		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		fechaArchivo = df.format(df.parse(fechaArchivo));

		logger.debug("Id = " + cbBancoAgenciaConfrontaId);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "Id = " + cbBancoAgenciaConfrontaId);
		logger.debug("Fecha = " + fechaArchivo);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "Fecha = " + fechaArchivo);

		CallableStatement callableStatement = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			callableStatement = con.prepareCall(LLAMADA_SP_PAGOS_IND);
			callableStatement.setInt(1, cbBancoAgenciaConfrontaId);
			callableStatement.setString(2, fechaArchivo);
			callableStatement.setString(3, fechaArchivo);
			if (callableStatement.execute())
				logger.debug("Ejecucion exitosa de SP_PAGOS_INDIVIDUAL");
				//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO,
						//"Ejecucion exitosa de SP_PAGOS_INDIVIDUAL");

		} catch (Exception e) {
			logger.error("ejecutaSPPagosIndividualGT()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {

			if (callableStatement != null) {
				try {
					callableStatement.close();
				} catch (SQLException e) {
					logger.error("ejecutaSPPagosIndividualGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ejecutaSPPagosIndividualGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}

	private static String CONSULTA_ARCHIVO_GT = "Select id_archivos_insertados idArchivosViejos, nombre_archivo nombreArchivo from cb_archivos_insertados "
			+ " where NOMBRE_ARCHIVO = ? and id_archivos_insertados !=  ?";

	public boolean consultaExistenciaArchivoGT(String archivo, String idArchivoNuevo) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			logger.debug("Consulta existencia archivo = " + CONSULTA_ARCHIVO_GT);
			
			ps = con.prepareStatement(CONSULTA_ARCHIVO_GT);
			ps.setString(1, archivo);
			ps.setString(2, idArchivoNuevo);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}

		} catch (Exception e) {
			logger.error("consultaExistenciaArchivoGT()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			return false;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("consultaExistenciaArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("consultaExistenciaArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("consultaExistenciaArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}

		}
		return false;
	}

	// se utiliza solo para GT
	// Borra el archivo con el id maestro
	private static String BORRAR_ARCHIVOGT = "Delete cb_archivos_insertados "
			+ " where id_archivos_insertados != ? and nombre_archivo = ? ";
	// Borra los registros con el id maestro
	private static String BORRAR_REGISTROGT = "Delete cb_data_banco " + " where cbbancoagenciaconfrontaid = ?  "
			+ "        AND dia = TO_DATE (?, 'dd/MM/yy')";
	// Borra los registros que no proceso
	private static String BORRAR_NO_REGISTRADOSGT = "Delete cb_data_sin_procesar "
			+ " where id_archivos_insertados = ?";

	// elimina registros pendientes
	private String BORRA_CONCILIACIONES_PENDIENTESGT = "delete " + " FROM cb_conciliacion " + " WHERE tipo = 1 "
			+ "        AND cbbancoagenciaconfrontaid = ?  " + "        AND dia = TO_DATE (?, 'dd/MM/yy')";
	private String BORRAR_CONCILACIONESGT = "DELETE " + " cb_conciliacion " + " WHERE cbbancoagenciaconfrontaid = ? "
			+ " AND dia = TO_DATE (?, 'dd/MM/yy') ";
	private static String CONSULTA_ARCHIVOGT = "Select id_archivos_insertados idArchivosViejos, nombre_archivo nombreArchivo from cb_archivos_insertados "
			+ " where NOMBRE_ARCHIVO = ? and id_archivos_insertados !=  ?";

	// borra el archivo y los registros
	public void borrarArchivoGT(String nombre, String idArchivo, int cbBancoAgenciaConfrontaId,
			String fechaConciliacion) throws ParseException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;

		DateFormat df = new SimpleDateFormat("dd/MM/yy");
		fechaConciliacion = df.format(df.parse(fechaConciliacion));

		logger.debug("nombre de archivo: " + nombre);
		logger.debug("id archivo nuevo carga: " + idArchivo);
		logger.debug("fecha conciliacion:" + fechaConciliacion);
		logger.debug("ID BANCO AGENCIA CONFRONTA: " + cbBancoAgenciaConfrontaId);
		
		try {
			con = obtenerDtsPromo().getConnection();
			logger.debug("Consulta archivo antes de borrar = " + CONSULTA_ARCHIVOGT);
			
			ps = con.prepareStatement(CONSULTA_ARCHIVOGT);
			ps.setString(1, nombre);
			ps.setString(2, idArchivo);

			rs = ps.executeQuery();

			while (rs.next()) {
				// priemero limpia conciliaciones
				String resultado = rs.getString("idArchivosViejos");
				logger.debug("id del archivo a eliminar: " + resultado);
				

				int ejecuta = 0;

				/**
				 * 1- Borra registros de comisiones
				 */
				logger.debug("1 -> Elimina registros de comisiones...");
				
				ps = con.prepareStatement(DELETE_COMISIONES_ARCHIVO);
				ps.setString(1, resultado);
				logger.debug("ID de archivo a eliminar: " + resultado);
				logger.debug("Delete comisiones: " + DELETE_COMISIONES_ARCHIVO);
				
				ejecuta = ps.executeUpdate();
				if (ps != null)
					ps.close();
				logger.debug("Se han eliminado registros de comisiones...  Valor ejecucion: " + ejecuta);
				
				logger.debug("Elimina conciliaciones...");
				
				PreparedStatement ps2 = null;
				try {
					ps2 = con.prepareStatement(BORRAR_CONCILACIONESGT);
					ps2.setInt(1, cbBancoAgenciaConfrontaId);
					ps2.setString(2, fechaConciliacion);
					ejecuta = ps2.executeUpdate();
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				} finally {
					if (ps2 != null) {
						try {
							ps2.close();
						} catch (SQLException e) {
							logger.error("borrarArchivoGT()" + "- Error", e);
							//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					}
				}
				logger.debug("las conciliaciones fueron borradas..." + ejecuta);
				logger.debug("query borra conciliacion: " + BORRAR_CONCILACIONESGT);
				
				// *****************************************************************

				// AGREGADO PARA ELIMINACION DE REGITROS VIEJOS
				logger.debug("Elimina conciliaciones pendientes...");
				
				PreparedStatement ps3 = null;
				try {
					ps3 = con.prepareStatement(BORRA_CONCILIACIONES_PENDIENTESGT);
					logger.debug("query borra pendientes: " + BORRA_CONCILIACIONES_PENDIENTESGT);
					
					ps3.setInt(1, cbBancoAgenciaConfrontaId);
					ps3.setString(2, fechaConciliacion);
					ejecuta = ps3.executeUpdate();
					logger.debug("las conciliaciones pendientes fueron borradas..." + ejecuta);
					
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
				} finally {
					if (ps3 != null) {
						try {
							ps3.close();
						} catch (SQLException e) {
							logger.error("borrarArchivoGT()" + "- Error", e);
							//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					}
				}
				// segundo limpia cb_data_banco
				logger.debug("Elimina data banco... param: " + resultado);
				logger.debug("query borra registrados: " + BORRAR_REGISTROGT);
				
				PreparedStatement ps4 = null;
				try {
					ps4 = con.prepareStatement(BORRAR_REGISTROGT);
					ps4.setInt(1, cbBancoAgenciaConfrontaId);
					ps4.setString(2, fechaConciliacion);
					ejecuta = ps4.executeUpdate();
					logger.debug("los registros data banco furon borrados..." + ejecuta);
					
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				} finally {
					if (ps4 != null) {
						try {
							ps4.close();
						} catch (SQLException e) {
							logger.error("borrarArchivoGT()" + "- Error", e);
							//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					}
				}

				// tercero limpia data no registrada
				logger.debug("Elimina datos no registrados...param: " + resultado);
				logger.debug("query no registrados: " + BORRAR_NO_REGISTRADOSGT);
				
				PreparedStatement ps5 = null;
				try {
					ps5 = con.prepareStatement(BORRAR_NO_REGISTRADOSGT);
					ps5.setString(1, resultado);
					ejecuta = ps5.executeUpdate();
					logger.debug("se han elimnado los datos no registrados..." + ejecuta);
					
					// ******************************************************************
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				} finally {
					if (ps5 != null) {
						try {
							ps5.close();
						} catch (SQLException e) {
							logger.error("borrarArchivoGT()" + "- Error", e);
							//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					}
				}

				// cuarto limpia
				logger.debug("Elimina los archivos anteriores...");
				logger.debug("query borra archivo: " + BORRAR_ARCHIVOGT);
				
				PreparedStatement ps6 = null;
				try {
					ps6 = con.prepareStatement(BORRAR_ARCHIVOGT);
					ps6.setString(1, idArchivo);
					ps6.setString(2, nombre);
					ejecuta = ps6.executeUpdate();
					logger.debug("el archivo fue borrado..." + ejecuta);
					
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				} finally {
					if (ps6 != null) {
						try {
							ps6.close();
						} catch (SQLException e) {
							logger.error("borrarArchivoGT()" + "- Error", e);
							//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					}
				}

			}

		} catch (Exception e) {
			logger.error("borrarArchivoGT()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("borrarArchivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}

		}
	}

	private String INSERTA_MASIVOS_BANCOGT = "INSERT " + " INTO CB_DATA_BANCO " + "   ( " + "     cBDataBancoId, "
			+ "     cod_Cliente, " + "     telefono, " + "     tipo, " + "     fecha, " + "     cBCatalogoBancoId, "
			+ "     cBCatalogoAgenciaId, " + "     cBBancoAgenciaConfrontaId, " + "     monto, " + "     transaccion, "
			+ "     estado, " + "     mes, dia,  " + "     texto1, " + "     texto2, " + "     creado_Por, "
			+ "     modificado_Por, " + "     fecha_Creacion, " + "     fecha_Modificacion, id_archivos_insertados, "
			+ " CODIGO, " + " COMISION " + "  ) " + "     VALUES " + " ( " + " cb_data_banco_sq.nextval, " + "     ?, "
			+ "     ?, " + "     ?, " + "     to_date(?, 'dd/MM/yyyy HH24:mi:ss'), " + "     ?, " + "     ?, "
			+ "     ?, " + "     ?, " + "     ?, " + "     ?, "
			+ "     ?, trunc(to_date(?,'dd/MM/yyyy HH24:mi:ss'), 'dd') ," + "     SUBSTR (TRIM(?), 1, 70), "
			+ "         SUBSTR (TRIM(?), 1, 70),  " + "     ?, " + "     ?, " + "     sysdate, "
			+ "     to_date(?, 'dd/MM/yyyy HH24:mi:ss'), " + " ?, " + " ?, " + " ? " + " )";

	/**
	 * This method should insert a list of records in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 */
	public int insertarMasivoGT(List<CBDataBancoModel> registros) {
		Connection conn = null;

		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			logger.debug("CANTIDAD DE REGISTROS ANTES DEL INSERT: " + registros.size());
			
			QueryRunner qr = new QueryRunner();
			List<Object[]> dataList = new ArrayList<Object[]>(registros.size());
			Object[] param;
			Object[][] dataObj = new Object[registros.size()][18];
			for (CBDataBancoModel registro : registros) {
				param = new Object[] { registro.getCodCliente(), registro.getTelefono(), registro.getTipo(),
						registro.getFecha(), Integer.parseInt(registro.getcBCatalogoBancoId()),
						Integer.parseInt(registro.getcBCatalogoAgenciaId()), registro.getCBBancoAgenciaConfrontaId(),
						registro.getMonto(), registro.getTransaccion(), registro.getEstado(), registro.getMes(),
						registro.getDia(), registro.getTexto1(), registro.getTexto2(), registro.getCreadoPor(),
						registro.getModificadoPor(), registro.getFechaModificacion(), registro.getIdCargaMaestro(),
						registro.getCbAgenciaVirfisCodigo(), registro.getComision() };
				dataList.add(param);
			}

			logger.debug("Query para la carga Masiva: " + INSERTA_MASIVOS_BANCOGT);
		

			dataObj = dataList.toArray(dataObj);
			int[] objRet = qr.batch(conn, INSERTA_MASIVOS_BANCOGT, dataObj);
			logger.debug("CANIDAD DE ARCHIVOS ALMACENADOS: " + objRet.length);
			
			return objRet.length;

		} catch (Exception e) {
			logger.error("insertarMasivoGT()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("insertarMasivoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return 0;

	}

	/*
	 * se probara para gt
	 */

	// Verifica si la fecha ya fue cargada
	private String VERIFICA_CARGA_DATA_BANCOGT = "SELECT * FROM CB_DATA_BANCO "
			+ " WHERE TO_CHAR(DIA, 'dd/MM/yyyy') = ? " + " AND CBCATALOGOBANCOID = ? " + " AND CBCATALOGOAGENCIAID = ? "
			+ " AND CBBANCOAGENCIACONFRONTAID = ?";

	public boolean verificaCargaDataBancoGT(String fechaVerificar, String banco, String agencia, String confronta) {
		boolean verifica = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = null;
		logger.debug("FECHA A VERIFICAR = " + fechaVerificar);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "FECHA A VERIFICAR = " + fechaVerificar);
		logger.debug("BANCO A VERIFICAR = " + banco);
	//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "BANCO A VERIFICAR = " + banco);
		logger.debug("AGENCIA A VERIFICAR = " + agencia);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "AGENCIA A VERIFICAR = " + agencia);
		logger.debug("CONFRONTA A VERIFICAR = " + confronta);
		//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.INFO, "CONFRONTA A VERIFICAR = " + confronta);
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(VERIFICA_CARGA_DATA_BANCOGT);
			ps.setString(1, fechaVerificar);
			ps.setString(2, banco);
			ps.setString(3, agencia);
			ps.setString(4, confronta);
			rs = ps.executeQuery();
			if (rs.next()) {
				verifica = true;
			}
		} catch (Exception e) {
			logger.error("verificaCargaDataBancoGT()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			verifica = false;
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("verificaCargaDataBancoGT()" + "- Error", e);
				//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					logger.error("verificaCargaDataBancoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("verificaCargaDataBancoGT()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}

		}
		return verifica;
	}

	/**
	 * Llama a proceso CB_COMISION_CONFRONTA_SP
	 */

	private static String CALL_CB_COMISIONES_CONFRONTAS_SP = "{call cb_comision_confronta_sp(?)}";

	public int ejecutaProcesoComisionesConfrontas(String idMaestroCarga) {
		PreparedStatement callableStatement = null;
		Connection con = null;

		logger.debug("Parametro para el proceso: " + idMaestroCarga);
		logger.debug("llamada de proceso en el java: " + "{call cb_comision_confronta_sp(" + idMaestroCarga + ")}");
		

		int result = 0;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			// try {
			callableStatement = con.prepareStatement(CALL_CB_COMISIONES_CONFRONTAS_SP);
			callableStatement.setInt(1, Integer.parseInt(idMaestroCarga));
			result = callableStatement.executeUpdate();
			// } finally {
			// con.close();
			// }
		} catch (Exception e) {
			logger.error("ejecutaProcesoComisionesConfrontas()" + "- Error", e);
			
		} finally {

			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					logger.error("ejecutaProcesoComisionesConfrontas()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (callableStatement != null)
				try {
					callableStatement.close();
				} catch (SQLException e) {
					logger.error("ejecutaProcesoComisionesConfrontas()" + "- Error", e);
				//	Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}

		}

		return result;
	}

	private String QRY_NOMENCLATURA = "SELECT NOMENCLATURA  "
			+ "FROM CB_CONFIGURACION_CONFRONTA WHERE CBCONFIGURACIONCONFRONTAID = ?";

	public String obtenerNomenclaturaConfronta(int id) {
		String valor = "";
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			cmd = conn.prepareStatement(QRY_NOMENCLATURA);
			cmd.setInt(1, id);
			rs = cmd.executeQuery();
			while (rs.next()) {
				valor = rs.getString(1);
			}
		} catch (Exception e) {
			logger.error("obtenerNomenclaturaConfronta()" + "- Error", e);
			//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e2) {
					logger.error("obtenerNomenclaturaConfronta()" + "- Error", e2);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			}
			if (cmd != null) {
				try {
					cmd.close();
				} catch (SQLException e1) {
					logger.error("obtenerNomenclaturaConfronta()" + "- Error", e1);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("obtenerNomenclaturaConfronta()" + "- Error", e);
					//Logger.getLogger(CBDataBancoDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
		return valor;
	}

}