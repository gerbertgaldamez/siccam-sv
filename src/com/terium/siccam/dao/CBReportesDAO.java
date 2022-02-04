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

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.zkoss.zk.ui.Sessions;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.CBConsultaContabilizacionController;
import com.terium.siccam.model.CBResumenDiarioConciliacionModel;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBConciliacionDetallada;
import com.terium.siccam.model.CBEntidad;
import com.terium.siccam.model.CBLiquidacionDetalleModel;
import com.terium.siccam.model.CBPagosModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;

public class CBReportesDAO extends ControladorBase {
	private static Logger log = Logger.getLogger(CBReportesDAO.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String QRY_VALOR_DEFECTO = "SELECT VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF WHERE MODULO = 'REPORTES' "
			+ "AND TIPO_OBJETO = 'TIPO_REPORTE_PREDEFINIDO' AND OBJETO = 'VALOR'";

	public HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	public String obtenerOpcionPorDefecto() {
		String valor = "";
		Connection conn = null;

		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug("Consulta para obtener tipo de reporte predefinido = " + QRY_VALOR_DEFECTO);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
				//	"Consulta para obtener tipo de reporte predefinido = " + QRY_VALOR_DEFECTO);
			cmd = conn.prepareStatement(QRY_VALOR_DEFECTO);
			rs = cmd.executeQuery();
			while (rs.next()) {
				valor = rs.getString(1);
			}
		} catch (Exception e) {
			log.error("obtenerOpcionPorDefecto" + "Error", e);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerOpcionPorDefecto" + "Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("obtenerOpcionPorDefecto" + "Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("obtenerOpcionPorDefecto" + "Error", e);
				//	Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		log.debug("Valor predefinido: " + valor);
		//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Valor predefinido: " + valor);
		return valor;
	}

	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier
	 * combobox
	 */
	private String QRY_OBTIENE_TIPO_OBJETO_X = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'REPORTES' AND TIPO_OBJETO = ? ORDER BY VALOR_OBJETO1";

	public List<CBParametrosGeneralesModel> obtenerTipoObjetoX(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			log.debug("Query combo tipo reporte = " + QRY_OBTIENE_TIPO_OBJETO_X);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
					//"Query combo tipo reporte = " + QRY_OBTIENE_TIPO_OBJETO_X);
			ps = con.prepareStatement(QRY_OBTIENE_TIPO_OBJETO_X);
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
			log.error("obtenerTipoObjetoX" + "Error", e);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerTipoObjetoX" + "Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerTipoObjetoX" + "Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerTipoObjetoX" + "Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}

	// Este query se invoca en el método generaConsultaReporte2() de la clase
	// CBReportesDAO
	public static final String CONSULTA_REPORTE_X_ENTIDAD = " SELECT DISTINCT A.DIA fecha,"
			+ " TO_NUMBER(TO_CHAR(A.FECHA,'MM')) mes_1,"
			+ " DECODE(TO_NUMBER(TO_CHAR(TO_DATE(A.FECHA,'dd/MM/yyyy'),'MM')),1,'ENERO',2,'FEBRERO',3,'MARZO',4,'ABRIL',5,'MAYO',6,'JUNIO',7,'JULIO',8,'AGOSTO',9,'SEPTIEMBRE',10,'OCTUBRE',11,'NOVIEMBRE',12,'DICIEMBRE') mes_2,"
			+ " TO_CHAR(A.FECHA,'HH24:mi:ss') hora," + " NVL(A.MONTO,0) monto," + " A.TELEFONO telefono,"
			+ " B.NOMBRE banco,"
			+ " (SELECT objeto FROM cb_modulo_conciliacion_conf  WHERE     UPPER (modulo) = 'CONFIGURACION_TIPO_CLIENTE' AND UPPER (tipo_objeto) = 'CONVENIOS' AND UPPER (estado) = 'S' AND valor_objeto1 = A.tipo) tipo_servicio,"
			+ " A. TRANSACCION secuencia," + " C.NOMBRE agencia,"
			+ " DECODE(D.TIPO,0,'VIRTUAL',1,'PRESENCIAL','PRESENCIAL') forma_de_pago,"
			+ " NVL((A.COMISION*A.MONTO),0) comision," + " NVL(A.COMISION,0) porcentaje, " + " A.CODIGO sucursal, "
			+ " D.NOMBRE nombre_sucursal, " + " 1 cantidad" + " FROM CB_DATA_BANCO A, " + " CB_CATALOGO_BANCO B, "
			+ " CB_CATALOGO_AGENCIA C, " + " CB_AGENCIA_VIR_FIS D, " + " CB_BANCO_AGENCIA_CONFRONTA E, "
			+ " CB_CONCILIACION F " + " WHERE A.CBCATALOGOBANCOID = B.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOBANCOID   = C.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = C.CBCATALOGOAGENCIAID " + " AND A.CODIGO              = D.CODIGO(+) "
			+ " AND A.CBCATALOGOBANCOID   = E.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = E.CBCATALOGOAGENCIAID " + " AND A.CBDATABANCOID       = F.CBDATABANCOID "
			+ " AND A.DIA                 = F.DIA " + " AND A.CBCATALOGOAGENCIAID = F.CBCATALOGOAGENCIAID "
			+ " AND A.CBCATALOGOAGENCIAID = D.CBCATALOGOAGENCIAID (+) ";

	public List<CBEntidad> generaConsultaReporte2(String fechaDesde, String fechaHasta, int idBanco, int idAgencia,
			String tipoRecarga, String tipoEntidad) throws SQLException, NamingException {
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		List<CBEntidad> listado = new ArrayList<CBEntidad>();
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			long t1 = System.currentTimeMillis();
			String Where = "";
			Where += " and a.dia >= to_date('" + fechaDesde + "', 'dd/MM/yyyy')" + " and a.dia <= to_date('"
					+ fechaHasta + "', 'dd/MM/yyyy') ";

			if (idBanco == 0) {
				log.debug("Null banco");
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Null banco");
			} else {
				log.debug("ID banco" + idBanco);
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "ID banco" + idBanco);
				Where += " and b.cbcatalogobancoid = " + idBanco + "";
			}
			if (idAgencia == 0) {
				log.debug("Null AGENCIA");
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Null AGENCIA");
			} else {
				log.debug("IDAGENCIA" + idAgencia);
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "IDAGENCIA" + idAgencia);
				Where += " and a.cbcatalogoagenciaid = " + idAgencia + "";
			}
			if ("".equals(tipoRecarga)) {
				log.debug("Null TipoRecarga");
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Null tipoRecarga");
			} else {
				log.debug("tipoRecarga" + tipoRecarga);
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "tipoRecarga" + tipoRecarga);
				Where += " and a.tipo = '" + tipoRecarga + "'";
			}
			if (tipoEntidad.equals("")) {
				log.debug("Null tipo entidad");
				
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "NULL tipO ENTIDAD");
			} else {
				log.debug("TIPO ENTIDAD" + tipoEntidad);
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "TIPO ENTIDAD" + tipoEntidad);
				Where += " and d.tipo = '" + tipoEntidad + "'";
			}
			// con.setAutoCommit(false);

			stm = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
			log.debug("QUERY REPORTE POR ENTIDAD = " + CONSULTA_REPORTE_X_ENTIDAD + Where);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
				//	"QUERY REPORTE POR ENTIDAD = " + CONSULTA_REPORTE_X_ENTIDAD + Where);
			rs = stm.executeQuery(CONSULTA_REPORTE_X_ENTIDAD + Where);
			rs.setFetchSize(1024);
			CBEntidad obj = null;
			long t11 = System.currentTimeMillis();
			while (rs.next()) {
				obj = new CBEntidad();

				obj.setFecha(rs.getDate(1));
				obj.setMes_1(rs.getInt(2));
				obj.setMes_2(rs.getString(3));
				obj.setHora(rs.getString(4));
				obj.setMonto(rs.getBigDecimal(5));
				obj.setTelefono(rs.getInt(6));
				obj.setBanco(rs.getString(7));
				obj.setTipo_servicio(rs.getString(8));
				obj.setSecuencia(rs.getString(9));
				obj.setAgencia(rs.getString(10));
				obj.setForma_de_pago(rs.getString(11));
				obj.setComision(rs.getBigDecimal(12));
				obj.setPorcentaje(rs.getString(13));
				obj.setSucursal(rs.getString(14));
				obj.setNombre_sucursal(rs.getString(15));
				obj.setCantidad(rs.getInt(16));

				listado.add(obj);
			}

			log.debug("pasa de obtener el listado: " + listado.size());
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
					//"pasa de obtener el listado: " + listado.size());
			long t22 = System.currentTimeMillis();
			log.debug("Round  completed " + (t22 - t11) / 1000 + " seconds");
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
					//"Round  completed " + (t22 - t11) / 1000 + " seconds");
			long t2 = System.currentTimeMillis();
			log.debug("Took " + (t2 - t1) / 1000 + " seconds");
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, " Took " + (t2 - t1) / 1000 + " seconds");
		} catch (Exception e) {
			log.error("generaConsultaReporte2" + "Error", e);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("generaConsultaReporte2" + "Error", e);
				//	Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					log.error("generaConsultaReporte2" + "Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("generaConsultaReporte2" + "Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	/////////////////

	private String CONSULTA_REPORTE_X_ESTADO = "SELECT dia, " + "nombre, " + "tipo, "
			+ "trans_telefonica transTelefonica, " + "trans_banco transBanco, " + "conciliadas, "
			+ "dif_trans difTransaccion, " + "pagos_telefonica pagosTelefonica, " + "confronta_banco confrontaBanco, "
			+ "automatica, " + "manual_t manualTelefonica, " + "manual_b manualBanco, "
			+ "pagos_telefonica - automatica - manual_t pendienteTelefonica, "
			+ "confronta_banco  - automatica - manual_b pendienteBanco  " + "FROM cb_conciliacion_vw where 1 = 1 ";

	public List<CBResumenDiarioConciliacionModel> generaConsultaReporte(String fechaDesde, String fechaHasta,
			int idAgencia, String tipoRecarga, String tipoEstado) throws SQLException, NamingException {
		Connection con = null;

		List<CBResumenDiarioConciliacionModel> listado = new ArrayList<CBResumenDiarioConciliacionModel>();
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			// long t1 = System.currentTimeMillis();
			String Where = "";
			Where += " and dia >= to_date('" + fechaDesde + "', 'dd/MM/yyyy')" + " and dia <= to_date('" + fechaHasta
					+ "', 'dd/MM/yyyy') ";

			if (idAgencia == 0) {

			} else {
				log.debug("ID AGENCIA = " + idAgencia);
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "ID AGENCIA = " + idAgencia);
				Where += " and cbcatalogoagenciaid  = " + idAgencia + "";
			}
			if (tipoRecarga == null || tipoRecarga.equals("")) {
			} else {
				Where += " and valor_tipo  = " + tipoRecarga + "";

			}
			if (tipoEstado == null || tipoEstado.equals("")) {
			} else {

				if (tipoEstado.equals("1")) {
					Where += " and pagos_telefonica = confronta_banco ";
				}
				if (tipoEstado.equals("2")) {
					Where += " AND pagos_telefonica >0 and confronta_banco > 0  "
							+ " and ( confronta_banco-automatica-manual_b > 0 OR pagos_telefonica-automatica-manual_t > 0 )";
				}
				if (tipoEstado.equals("3")) {
					Where += " and confronta_banco = 0 ";
				}
				if (tipoEstado.equals("4")) {
					Where += " and pagos_telefonica = 0 ";
				}
				if (tipoEstado.equals("5")) {
					Where += " and ((confronta_banco-automatica-manual_b = 0 "
							+ " and manual_t > 0) or (pagos_telefonica-automatica-manual_t=0 and manual_b > 0)) ";
				}

			}

			QueryRunner qr = new QueryRunner();

			BeanListHandler<CBResumenDiarioConciliacionModel> bhl = new BeanListHandler<CBResumenDiarioConciliacionModel>(
					CBResumenDiarioConciliacionModel.class);
			listado = qr.query(con, CONSULTA_REPORTE_X_ESTADO + Where, bhl, new Object[] {});
			log.debug("QUERY REPORTE POR ESTADO = " + CONSULTA_REPORTE_X_ESTADO + Where);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
				//	"QUERY REPORTE POR ESTADO = " + CONSULTA_REPORTE_X_ESTADO + Where);
		} catch (Exception e) {
			log.error("generaConsultaReporte" + " - Error", e);
			
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("generaConsultaReporte" + " - Error", e);
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
			}

		}
		return listado;

	}
	////////////////////////////////

	public String changeNull(String cadena) {
		if (cadena == null) {
			return " ";
		} else {
			return cadena;
		}
	}

	private String QRY3 = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "a.nombre, " + "des_pago desPago, "
			+ "trans_telca transTelca, " + "a.telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
			+ "monto, " + "manual, " + "pendiente, " + " comision, " + " (monto * comision) monto_comision, "
			+ " sucursal, " + " nombre_sucursal, " + " tipo_sucursal "
			+ "FROM cb_conciliacion_detail2_vw a,cb_catalogo_agencia b " + "WHERE 1                 = 1  "
			+ "and a.cbcatalogoagenciaid = b.cbcatalogoagenciaid ";

	public List<CBConciliacionDetallada> generaConsultaReporte3(String fechaDesde, String fechaHasta, int idAgencia,
			int idBanco, String tipoRecarga, String tipoEstado) {

		List<CBConciliacionDetallada> listado = new ArrayList<CBConciliacionDetallada>();
		Connection con = null;

		Statement st = null;
		ResultSet rs = null;
		try {

			String Where = "";
			Where += " and dia >= to_date('" + fechaDesde + "', 'dd/MM/yyyy')" + " and dia <= to_date('" + fechaHasta
					+ "', 'dd/MM/yyyy') ";

			if (idAgencia == 0) {
				log.debug("Null agencia");
				
			} else {
				log.debug("idAgencia: " + idAgencia);
				
				Where += " and b.cbcatalogoagenciaid  = " + idAgencia + "";
			}
			if (idBanco == 0) {
				log.debug("Null Banco");
				
			} else {
				log.debug("idBanco: " + idBanco);
				
				Where += " and b.cbcatalogobancoid  = " + idBanco + "";
			}
			if (tipoRecarga == null || tipoRecarga.equals("")) {
			} else {
				Where += " and valor_tipo  = " + tipoRecarga + "";

			}
			if (tipoEstado == null || tipoEstado.equals("")) {
			} else {

				if (tipoEstado.equals("1")) {
					Where += " AND  monto = imp_pago";
				}
				if (tipoEstado.equals("2")) {
					Where += " and (imp_pago - monto - manual > 0 or monto-imp_pago - manual > 0 )  ";
				}
				if (tipoEstado.equals("3")) {
					Where += " and ((imp_pago - monto - manual = 0 and manual > 0) "
							+ "or (monto-imp_pago + manual > 0 and manual > 0 )) "
							+ " AND (accion != 'AJUSTE DEBITO (TRANS TELEFONICA) AUTO') "
							+ " AND (accion != 'AJUSTE CREDITO (TRANS BANCO) AUTO')"
							+ " AND (accion                    != 'DIFERENCIA_FECHAS')"
							+ " AND (accion                    != 'NO_APLICA')"
							+ " AND (accion 				   != 'AJUSTE DEBITO (TRANS TELEFONICA) DIF_FECHAS')";
				}
				if (tipoEstado.equals("4")) {
					Where += " and ((imp_pago - monto - manual = 0 and manual > 0) "
							+ "or (monto-imp_pago + manual > 0 and manual > 0 )) and (accion = 'AJUSTE DEBITO (TRANS TELEFONICA) AUTO' or accion = 'AJUSTE CREDITO (TRANS BANCO) AUTO') ";
				}
				if (tipoEstado.equals("5")) {
					Where += " and accion = 'DIFERENCIA_FECHAS' ";
				}
				if (tipoEstado.equals("6")) {
					Where += " and accion = 'AJUSTE DEBITO (TRANS TELEFONICA) DIF_FECHAS' ";
				}
				if (tipoEstado.equals("7")) {
					Where += "and accion = 'NO_APLICA'";
				}

			}

			log.debug("QUERY REPORTE POR DETALLE = " + QRY3 + Where);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
					//"QUERY REPORTE POR detalle = " + QRY3 + Where);

			CBConciliacionDetallada obj = null;

			con = ControladorBase.obtenerDtsPromo().getConnection();

			con.setAutoCommit(false);
			st = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
			st.setFetchSize(1024);
			QRY3 = QRY3 + Where;
			rs = st.executeQuery(QRY3);
			while (rs.next()) {
				obj = new CBConciliacionDetallada();
				obj.setAgencia((rs.getString(1)));
				obj.setDia(rs.getDate(2));
				obj.setTipo((rs.getString(3)));
				obj.setCliente(changeNull(rs.getString(4)));
				obj.setNombre(changeNull(rs.getString(5)));
				obj.setDesPago(changeNull(rs.getString(6)));
				obj.setTransTelca(changeNull(rs.getString(7)));
				obj.setTelefono(changeNull(rs.getString(8)));
				obj.setTransBanco(changeNull(rs.getString(9)));
				obj.setImpPago(rs.getBigDecimal(10));
				obj.setMonto(rs.getBigDecimal(11));
				obj.setManual(rs.getBigDecimal(12));
				obj.setPendiente(changeNull(rs.getString(13)));
				obj.setComision(rs.getBigDecimal(14));
				obj.setMonto_comision(rs.getBigDecimal(15));
				obj.setSucursal(changeNull(rs.getString(16)));
				obj.setNombre_sucursal(changeNull(rs.getString(17)));
				obj.setTipo_sucursal(changeNull(rs.getString(18)));
				listado.add(obj);
			}
		} catch (Exception e) {
			log.error("generaConsultaReporte3" + " - Error", e);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("generaConsultaReporte3" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}

			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					log.error("generaConsultaReporte3" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}

			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("generaConsultaReporte3" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return listado;
	}

	String CONSULTA_CAJEROS = "SELECT CBCATALOGOBANCOID,ENTIDAD,CBCATALOGOAGENCIAID,AGENCIA, to_char(FECHA, 'dd/MM/yyyy'),CAJA_EFECTIVO,CAJA_CHEQUE, "
			+ "CAJA_EXENSIONES, CAJA_CUOTAS_VISA, CAJA_CUOTAS_CREDOMATIC,CAJA_TARJETA_CREDOMATIC,CAJA_TARJETA_OTRAS,CAJA_TARJETA_VISA, "
			+ "SC_PAGOSOD,SC_PAGOSOM,SC_REVERSASOD,SC_REVERSASOM, "
			+ "NVL(CAJA_TOTAL,0)+NVL(SC_PAGOSOD,0)+NVL(SC_REVERSASOD,0) TOTAL_DIA, "
			+ "CAJA_TOTAL, CREDOMATIC_DEP CONSUMO_CREDOMATIC,CREDOMATIC_RET RETENCION_CREDOMATIC,ESTADO_CRED,CONSUMO_VISA, "
			+ "IVA_VISA,ESTADO_VISA,DEPOSITO, "
			+ "NVL(CREDOMATIC_DEP,0)+NVL(CONSUMO_VISA,0)+NVL(IVA_VISA,0)+NVL(DEPOSITO,0) TOTAL_EC, "
			+ "NVL(TOTAL_SC,0)-(NVL(CREDOMATIC_DEP,0)+NVL(CONSUMO_VISA,0)+NVL(IVA_VISA,0)+NVL(DEPOSITO,0)) DIFERENCIA "
			+ "FROM CB_CONCILIACION_CAJAS_VW " + "WHERE 1 = 1  ";

	/**
	 * 
	 * @param entidad
	 *            agencia fechaini fechafin idbanco
	 */
	public List<CBConciliacionCajasModel> generaConsultaPrincipal(String fechaDesde, String fechaHasta, int idAgencia,
			int idBanco) {
		List<CBConciliacionCajasModel> list = new ArrayList<CBConciliacionCajasModel>();

		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			String Where = "";
			Where += " and fecha >= to_date('" + fechaDesde + "', 'dd/MM/yyyy')" + " and fecha <= to_date('"
					+ fechaHasta + "', 'dd/MM/yyyy') ";

			if (idAgencia == 0) {
				log.debug("Null agencia");
				
			} else {
				log.debug("idAgencia: " + idAgencia);
				
				Where += " and cbcatalogoagenciaid  = " + idAgencia + "";
			}
			if (idBanco == 0) {
				log.debug("Null banco");
				
			} else {
				log.debug("idBanco: " + idBanco);
				
				Where += " and CBCATALOGOBANCOID  = " + idBanco + "";
			}

			con = ControladorBase.obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			log.debug("consulta: " + CONSULTA_CAJEROS + Where + " ORDER BY 1, 2, 3, 4, 5 ");
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
					//"consulta: " + CONSULTA_CAJEROS + Where + " ORDER BY 1, 2, 3, 4, 5 ");
			CONSULTA_CAJEROS = CONSULTA_CAJEROS + Where + " ORDER BY 1, 2, 3, 4, 5 ";
			rs = stm.executeQuery(CONSULTA_CAJEROS);
			rs.setFetchSize(1024);
			CBConciliacionCajasModel obj = null;
			while (rs.next()) {
				obj = new CBConciliacionCajasModel();
				obj.setCbcatalogobancoid(rs.getInt(1));
				obj.setEntidad(rs.getString(2));
				obj.setCbcatalogoagenciaid(rs.getInt(3));
				obj.setAgencia(rs.getString(4));
				obj.setFecha(rs.getString(5));
				obj.setCajaefectivo(rs.getDouble(6));
				obj.setCajacheque(rs.getDouble(7));
				obj.setCajaexenciones(rs.getDouble(8));
				obj.setCajacuotasvisa(rs.getDouble(9));
				obj.setCajacuotascredomatic(rs.getDouble(10));
				obj.setCajatarjetacredomatic(rs.getDouble(11));
				obj.setCajatarjetaotras(rs.getDouble(12));
				obj.setCajatarjetavisa(rs.getDouble(13));
				obj.setScpagosod(rs.getDouble(14));
				obj.setScpagosom(rs.getDouble(15));
				obj.setScreversasod(rs.getDouble(16));
				obj.setScreversasom(rs.getDouble(17));
				obj.setTotaldia(rs.getDouble(18));
				obj.setCajatotal(rs.getDouble(19));
				obj.setCredomaticdep(rs.getDouble(20));
				obj.setCredomaticRet(rs.getDouble(21));
				obj.setEstadoCredo(rs.getDouble(22));
				obj.setConsumovisa(rs.getDouble(23));
				obj.setIvavisa(rs.getDouble(24));
				obj.setEstadoVisa(rs.getDouble(25));
				obj.setDeposito(rs.getDouble(26));
				obj.setTotalec(rs.getDouble(27));
				obj.setDiferencia(rs.getDouble(28));

				list.add(obj);
			}

		} catch (SQLException e) {
			log.error("generaConsultaPrincipal" + " - Error", e);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("generaConsultaPrincipal" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					log.error("generaConsultaPrincipal" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();

				} catch (SQLException e) {
					log.error("generaConsultaPrincipal" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);

				}
		}
		return list;
	}

	///////////////////////////
	public static final String CONSULTA_REPORTE_LIQUIDACIONES = "SELECT NOMBTRANSACCION, to_char(FECHATRANSACCION,'DD-MM-YYYY') FechaTransaccion, TIPOVALOR, TIPOPAGO, "
			+ "TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL FROM CB_REPORTE_LIQUIDACIONES_VW WHERE 1 = 1  ";

	////////////////////
	public List<CBLiquidacionDetalleModel> consultaReporte(String user, String fechaInicio, String fechaFin) {

		List<CBLiquidacionDetalleModel> list = new ArrayList<CBLiquidacionDetalleModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			// long t1 = System.currentTimeMillis();
			String Where = "";
			Where += " and FECHATRANSACCION >= to_date('" + fechaInicio + "', 'dd/MM/yyyy')"
					+ " and FECHATRANSACCION <= to_date('" + fechaFin + "', 'dd/MM/yyyy') ";
			if (user != null && !user.equals("")) {

				Where += "AND UPPER (NOMBTRANSACCION) LIKE '%" + user.toUpperCase() + "%' ";

			}

			String query = CONSULTA_REPORTE_LIQUIDACIONES;
			log.debug("Consulta de reportes de liquidaciones = " + query);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO,
					//"Consulta de reportes de liquidaciones = " + query);

			log.debug("Parametros enviados");
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Parámetros envíados: ");

			log.debug("User = " + user);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "User = " + user);

			log.debug("Fecha inicio" + fechaInicio);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Fecha inicio = " + fechaInicio);

			log.debug("fecha fin = " + fechaFin );
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Fecha fin = " + fechaFin);

			con = ControladorBase.obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			log.debug("consulta: " + query + Where);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "consulta: " + query + Where);
			query = query + Where;
			rs = stm.executeQuery(query);
			rs.setFetchSize(1024);
			CBLiquidacionDetalleModel objeBean = null;

			while (rs.next()) {
				objeBean = new CBLiquidacionDetalleModel();
				objeBean.setNombtransaccion(rs.getString(1));
				objeBean.setFec_efectividad(rs.getString(2));
				objeBean.setTipo_valo(rs.getInt(3));
				objeBean.setTipo_pago(rs.getString(4));
				objeBean.setCod_tipotarjeta(rs.getString(5));
				objeBean.setDesc(rs.getString(6));
				String efectivo = rs.getString(7);
				if (efectivo != null) {
					objeBean.setTotal(String.format("%.2f", Double.parseDouble(efectivo)).replace(',', '.'));
				} else {
					objeBean.setTotal("N/A");
				}
				list.add(objeBean);
			}

		} catch (SQLException e) {
			log.error("consultaReporte" + " - Error", e);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("consultaReporte" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					log.error("consultaReporte" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("consultaReporte" + " - Error", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}
	
	/**
	 * Agrega CarlosGodinez -> 12/08/2018
	 * Reporte de recaudacion
	 * */
	
	public static final String CONSULTA_REPORTE_RECAUDACION = "SELECT to_char(fecha_efectiva,'dd/MM/yyyy'), "
			+ "to_char(fecha_pago,'dd/MM/yyyy'), cod_cliente, "
			+ "nom_cliente, num_secuenci, imp_pago, transaccion, tipo_transaccion, cod_oficina, des_oficina, "
			+ "tip_movcaja, des_movcaja, tip_valor, des_tipvalor, nom_usuarora, cod_banco, banco, cod_caja, "
			+ "caja, replace(replace(observacion,chr(13),''),chr(10),'') observaciones FROM cb_recaudacion_reporte_vw "
			+ "WHERE fecha_pago >= TO_DATE (?, 'dd/MM/yyyy') "
			+ "AND fecha_pago <= TO_DATE (?, 'dd/MM/yyyy')";
	
	public List<CBPagosModel> consultaReporteRecaudacion(String fechaInicio, String fechaFin){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CBPagosModel> list = new ArrayList<CBPagosModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			
			log.debug("Query reporte recaudacion = " + CONSULTA_REPORTE_RECAUDACION);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, 
					//"Query reporte recaudacion = " + CONSULTA_REPORTE_RECAUDACION);
			log.debug("fecha inicio = " + fechaInicio);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Fecha inicio = " + fechaInicio);
			log.debug("fecha fin = " + fechaFin);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.INFO, "Fecha fin = " + fechaFin);
			pstmt = conn.prepareStatement(CONSULTA_REPORTE_RECAUDACION);
			pstmt.setString(1, fechaInicio); 
			pstmt.setString(2, fechaFin); 
			rs = pstmt.executeQuery();
			CBPagosModel objeBean;
			while (rs.next()) {
				objeBean = new CBPagosModel();
				objeBean.setFecEfectividad(rs.getString(1));   
				objeBean.setFecha(rs.getString(2));  
				objeBean.setCodCliente(rs.getString(3));   
				objeBean.setNonCliente(rs.getString(4));  
				objeBean.setNumSecuenci(rs.getString(5));   
				objeBean.setImpPago(rs.getString(6));  
				objeBean.setTransaccion(rs.getString(7));   
				objeBean.setTipoTransaccion(rs.getString(8));
				objeBean.setCodOficina(rs.getString(9));   
				objeBean.setDesOficina(rs.getString(10));
				objeBean.setTipoMovCaja(rs.getString(11));   
				objeBean.setDesMovCaja(rs.getString(12));
				objeBean.setTipoValor(rs.getString(13));   
				objeBean.setDesTipoValor(rs.getString(14));
				objeBean.setNomUsuarora(rs.getString(15));   
				objeBean.setCodBanco(rs.getString(16));  
				objeBean.setDesBanco(rs.getString(17));   
				objeBean.setCodCaja(rs.getString(18));  
				objeBean.setDesCaja(rs.getString(19));   
				objeBean.setObservacion(rs.getString(20));
				list.add(objeBean);
			}
		}catch (Exception e) {
			log.error("consultaReporteRecaudacion" + " - Error", e);
			//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					log.error("consultaReporteRecaudacion" + " - Error", e2);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					log.error("consultaReporteRecaudacion" + " - Error", e1);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("consultaReporteRecaudacion" + " - Error", e);
				//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
}
