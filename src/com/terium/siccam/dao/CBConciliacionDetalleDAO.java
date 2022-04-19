package com.terium.siccam.dao;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;



import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.ConciliacionController;
import com.terium.siccam.model.CBBmfModel;
import com.terium.siccam.model.CBConciliacionDetallada;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBResumenDiarioConciliacionModel;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.ConsultasSQ;
import com.terium.siccam.utils.Tools;

public class CBConciliacionDetalleDAO {

	private static Logger logger = Logger.getLogger(CBConciliacionDetalleDAO.class);

	// revisar sis e ocupa
	public List<CBConciliacionDetallada> obtenerConciliacionDetalladas(String fecha, String num, int tipo) {
		List<CBConciliacionDetallada> lst = new ArrayList<CBConciliacionDetallada>();
		String query = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;
		CBConciliacionDetallada obj = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
					+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
					+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId "
					+ "FROM cb_conciliacion_detail_vw " + "WHERE dia               = to_date('" + fecha
					+ "', 'dd/MM/yyyy') " + "AND cbcatalogoagenciaid = '" + num + "' " + "AND tipo_id             = '"
					+ tipo + "' ";

			logger.debug("obtenerConciliacionDetalladas() - Query : " + query);
			stmt = conn.createStatement();

			rst = stmt.executeQuery(query);
			while (rst.next()) {
				obj = new CBConciliacionDetallada();
				obj.setAgencia(rst.getString(1));
				obj.setDia(rst.getDate(2));
				obj.setTipo(rst.getString(3));
				obj.setCliente(rst.getString(4));
				obj.setNombre(rst.getString(5));
				obj.setDesPago(rst.getString(6));
				obj.setTransTelca(rst.getString(7));
				obj.setTelefono(rst.getString(8));
				obj.setTransBanco(rst.getString(9));
				obj.setImpPago(rst.getBigDecimal(10).setScale(2, RoundingMode.DOWN));
				obj.setMonto(rst.getBigDecimal(11).setScale(2, RoundingMode.DOWN));
				obj.setManual(rst.getBigDecimal(12).setScale(2, RoundingMode.DOWN));
				obj.setPendiente(rst.getString(13));
				obj.setEstado(rst.getString(14));
				obj.setConciliacionId(rst.getString(15));
				lst.add(obj);
			}

		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (rst != null)
				try {
					rst.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error(e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e);
				}
		}
		return lst;
	}

	/**
	 * Modified by Carlos Godinez -> 11/09/2017
	 * 
	 * Se agrega funcion ABS() para validaciones de conciliaciones para que se tomen
	 * en cuenta registros con reversas.
	 */
	public List<CBConciliacionDetallada> obtenerConciliacionDetalladasFiltros(String fecha, String fechaHasta,
			String num, String tipo, String estado, String tel) throws SQLException {
		List<CBConciliacionDetallada> lst = new ArrayList<CBConciliacionDetallada>();
		String query = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		CBConciliacionDetallada obj = null;
		// Boolean otro_parametro = false;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			// vista de acelaracion para pendientes conciliar y conciliado
			// manualmente - cb_conciliacion_detail_p_vw
			query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
					+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
					+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, " + "sucursal, "
					+ " comision, " + " NOMBRE_SUCURSAL, " + " TIPO_SUCURSAL, " + " (monto*comision) monto_comision,"
					+ "tipo_id, estado_accion, "
					+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema, REAL_A "
					+ "FROM cb_conciliacion_detail_vw a " + "WHERE dia  >= to_date('" + fecha
					+ "', 'dd/MM/yyyy') AND dia  <= to_date ( '" + fechaHasta + "', 'dd/MM/yyyy')";

			if (estado.equals("1")) {
				query = query + " AND  monto = imp_pago";
			}
			if (estado.equals("2")) { // Pendiente de Conciliar

				query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
						+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
						+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision, "
						+ "tipo_id, estado_accion, "
						+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema,REAL_A "
						+ "FROM cb_conciliacion_detail_p_vw " + "WHERE dia  >= to_date('" + fecha
						+ "', 'dd/MM/yyyy') AND dia  <= to_date ( '" + fechaHasta + "', 'dd/MM/yyyy')";

				query = query
						+ " and (ABS(imp_pago) - ABS(monto) - ABS(manual)   > 0 OR ABS(monto) - ABS(imp_pago) - ABS(manual) > 0 ) ";
			}
			if (estado.equals("3")) { // Conciliado Manualmente

				query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
						+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
						+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision, "
						+ "tipo_id, estado_accion, "
						+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema, REAL_A "
						+ "FROM cb_conciliacion_detail_p_vw " + "WHERE dia  >= to_date('" + fecha
						+ "', 'dd/MM/yyyy') AND dia  <= to_date ( '" + fechaHasta + "', 'dd/MM/yyyy')";

				query = query + " and ((ABS(imp_pago) - ABS(monto) - ABS(manual) = 0 and ABS(manual) > 0) "
						+ "or (ABS(monto) - ABS(imp_pago) + ABS(manual) > 0 and ABS(manual) > 0 )) "
						+ " AND (accion != 'AJUSTE DEBITO (TRANS TELEFONICA) AUTO') "
						+ " AND (accion != 'AJUSTE CREDITO (TRANS BANCO) AUTO')"
						+ " AND (accion                    != 'DIFERENCIA_FECHAS')"
						+ " AND (accion                    != 'NO_APLICA')"
						+ " AND (accion 				   != 'AJUSTE DEBITO (TRANS TELEFONICA) DIF_FECHAS')";
			}
			/*
			 * Agergo Benjamin Escobar filtro para ajustes y diferencia de fechas en ajustes
			 */
			if (estado.equals("4")) { // Ajustes de Conciliacion

				query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
						+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
						+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision, "
						+ "tipo_id, estado_accion, "
						+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema, REAL_A "
						+ "FROM cb_conciliacion_detail_p_vw " + "WHERE dia  >= to_date('" + fecha
						+ "', 'dd/MM/yyyy') AND dia  <= to_date ( '" + fechaHasta + "', 'dd/MM/yyyy')";

				query = query + " and ((ABS(imp_pago) - ABS(monto) - ABS(manual) = 0 and ABS(manual) > 0) "
						+ "or (ABS(monto) - ABS(imp_pago) + ABS(manual) > 0 and ABS(manual) > 0 )) and (accion = 'AJUSTE DEBITO (TRANS TELEFONICA) AUTO' or accion = 'AJUSTE CREDITO (TRANS BANCO) AUTO') ";
			}

			if (estado.equals("5")) { // Pendientes de ajustes diferncia entre
				// fechas

				query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
						+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
						+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision, "
						+ "tipo_id, estado_accion, "
						+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema, REAL_A "
						+ "FROM cb_conciliacion_detail_p_vw " + "WHERE dia  = to_date('" + fecha
						+ "', 'dd/MM/yyyy')  AND dia  <= to_date ( '" + fechaHasta + "', 'dd/MM/yyyy') "
						+ "AND accion = 'DIFERENCIA_FECHAS' ";
			}

			if (estado.equals("6")) { // Ajuste aplicado diferencia de fechas

				query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
						+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
						+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision, "
						+ "tipo_id, estado_accion, "
						+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema, REAL_A "
						+ "FROM cb_conciliacion_detail_p_vw " + "WHERE dia  = to_date('" + fecha
						+ "', 'dd/MM/yyyy')  AND dia  <= to_date ( '" + fechaHasta
						+ "', 'dd/MM/yyyy') AND accion = 'AJUSTE DEBITO (TRANS TELEFONICA) DIF_FECHAS' ";
			}

			if (estado.equals("7")) { // Ajuste no aplicado diferencia de fechas

				query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
						+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
						+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision, "
						+ "tipo_id, estado_accion, "
						+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema, REAL_A "
						+ "FROM cb_conciliacion_detail_p_vw " + "WHERE dia  = to_date('" + fecha
						+ "', 'dd/MM/yyyy')  AND dia  <= to_date ( '" + fechaHasta
						+ "', 'dd/MM/yyyy') and accion = 'NO_APLICA' ";
			}

			if (estado.equals("8")) { // Error de concilicaion

				query = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, " + "des_pago desPago, "
						+ "trans_telca transTelca, " + "telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
						+ "monto, " + "manual, " + "pendiente, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision, "
						+ "tipo_id, estado_accion, "
						+ "accion, cbcausasconciliacionid, observacion, cbhistorialaccionid, sistema, REAL_A "
						+ "FROM cb_conciliacion_detail_p_vw " + "WHERE dia  = to_date('" + fecha
						+ "', 'dd/MM/yyyy')  AND dia  <= to_date ( '" + fechaHasta + "', 'dd/MM/yyyy') and estado = "
						+ estado;
			}

			if (!Constantes.TODOS.equals(tipo)) {
				query = query + " AND tipo_id = '" + tipo + "' ";
			}
			// Otros parametros
			if (!num.equals("Todas")) {
				query = query + " AND cbcatalogoagenciaid = '" + num + "' ";
			}

			if (!tel.equals("")) {
				if (tel.length() == 8) {
					query = query + " AND telefono = '" + tel + "' ";
				} else {
					query = query + " AND cliente = '" + tel + "' ";
				}
			}

			/*
			 * if (estado.equals("0")) { // TODOS los estados query = query +
			 * " AND  rownum < 3000"; }
			 */
			logger.debug("obtenerConciliacionDetalladasFiltros() " + " - Query detalle conciliacion => : " + query);
			stmt = conn.prepareStatement(query);

			rst = stmt.executeQuery();
			while (rst.next()) {
				obj = new CBConciliacionDetallada();
				obj.setAgencia(rst.getString(1));
				obj.setDia(rst.getDate(2));
				obj.setTipo(rst.getString(3));
				obj.setCliente(rst.getString(4));
				obj.setNombre(rst.getString(5));
				obj.setDesPago(rst.getString(6));
				obj.setTransTelca(rst.getString(7));
				obj.setTelefono(rst.getString(8));
				obj.setTransBanco(rst.getString(9));
				obj.setImpPago(rst.getBigDecimal(10).setScale(2, RoundingMode.DOWN));
				// System.out.println("monto en dao " + obj.getMonto());
				/*
				 * if(obj.getMonto() == null) { obj.setMonto(new BigDecimal(0)); } else {
				 */
				obj.setMonto(rst.getBigDecimal(11).setScale(2, RoundingMode.DOWN));
				// System.out.println("monto en dao 2 " + obj.getMonto());
				// }
				obj.setManual(rst.getBigDecimal(12).setScale(2, RoundingMode.DOWN));
				obj.setPendiente(rst.getString(13));
				obj.setEstado(rst.getString(14));
				obj.setConciliacionId(rst.getString(15));
				obj.setSucursal(rst.getString(16));
				obj.setComision(rst.getBigDecimal(17).setScale(2, RoundingMode.DOWN));
				obj.setNombre_sucursal((rst.getString(18)));
				obj.setTipo_sucursal(rst.getString(19));
				obj.setMonto_comision(rst.getBigDecimal(20));

				// Nuevos campos => 02/07/2021
				obj.setTipoId(rst.getInt(21));
				obj.setRespuestaAccion(rst.getString(22));
				obj.setAccion(rst.getString(23));
				obj.setCbCausasConciliacionId(rst.getInt(24));
				obj.setObservacion(rst.getString(25));
				obj.setCbHistorialAccionId(rst.getInt(26));
				obj.setSistema(rst.getInt(27));
				obj.setAplicadoReal(rst.getString(28));

				lst.add(obj);
			}

		} catch (Exception e) {
			logger.error( e);
		} finally {
			if (rst != null)
				try {
					rst.close();
				} catch (SQLException e) {
					logger.error( e);
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					logger.error( e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error( e);
				}
		}
		return lst;
	}

	public static final String DELETE_REGISTROS_CONCILIACION = "DELETE FROM cb_historial_accion WHERE CBHISTORIALACCIONID = ?  ";

	public void eliminarRegistros(int id) {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();

			ps = conn.prepareStatement(DELETE_REGISTROS_CONCILIACION);

			ps.setInt(1, id);

			int exec = ps.executeUpdate();
			if (exec > 0) {
				conn.commit();

			}
		} catch (Exception e) {
			logger.error( e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					logger.error(e1);
				}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				logger.error( e);
			}
		}
	}
	
public static String obtenerCodAgencia(String cbBancoAgenciaConfrontaID){
		
		PreparedStatement ptmt = null;
		ResultSet rst = null;
		Connection con = null;
		//CBParametrosGeneralesModel parametros = null;
		try{
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ptmt = con.prepareStatement(Constantes.OBTENER_COD_AGENCIA);
			logger.debug("obtenerCod Agencia ->" + Constantes.OBTENER_COD_AGENCIA );
			ptmt.setString(1, cbBancoAgenciaConfrontaID);
			
			//logger.debug("obtenerCodAgencia() " + " - Query obtener cod agencia en la dao => : " + Constantes.OBTENER_COD_AGENCIA);
			rst = ptmt.executeQuery();
			if(rst.next()){
				//return rst.getString(1);
				return rst.getString(Constantes.FIELD_COD_AGENCIA );
				
			}
			
		}catch(Exception e){
			logger.error( e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error( e);
			}
		}
		return null;
	}
	
public static String obtenerCodAgenciaReversa(String conciliacionid){
		
		PreparedStatement ptmt = null;
		ResultSet rst = null;
		Connection con = null;
		//CBParametrosGeneralesModel parametros = null;
		CBResumenDiarioConciliacionModel resumen = null;
		
		
		
		try{
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ptmt = con.prepareStatement(Constantes.OBTENER_COD_AGENCIA_REVERSA);
			logger.debug("obtenerCodAgenciaReversa ->" + Constantes.OBTENER_COD_AGENCIA_REVERSA );
			ptmt.setString(1, conciliacionid);
			
			rst = ptmt.executeQuery();
			
			if(rst.next()){
				//return rst.getString(1);
				//logger.debug("obtenerCodAgenciaReversa ->" + "se obtiene el cod agencia " + resumen.getIdAgencia());
				return rst.getString(Constantes.FIELD_COD_AGENCIA);
				
			}
			
		}catch(Exception e){
			logger.error( e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				logger.error( e);
			}
		}
		logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
		return null;
		
	}

public static String obtenerTrackingId ( String cod_num){
	CBConciliacionDetallada detalle = new CBConciliacionDetallada();
	PreparedStatement ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(ConsultasSQ.OBTENER_TRACKING_ID_SQ2);
		
		logger.debug("query para obtener el tracking id ->" + ConsultasSQ.OBTENER_TRACKING_ID_SQ2 );
		ptmt.setString(1, cod_num);
		rst = ptmt.executeQuery();
		
		if(rst.next()){
			return rst.getString(1);
		}
		
	}catch(Exception e){
		logger.error( e);
	}
	finally {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			logger.error( e);
		}
	}
	//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
	return null;
	
}

public static String obtenerTrackingIddepagosid ( String pagosid){
	CBConciliacionDetallada detalle = new CBConciliacionDetallada();
	PreparedStatement ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(ConsultasSQ.OBTENER_TRACKING_ID_SQ);
		
		logger.debug("query para obtener el tracking id ->" + ConsultasSQ.OBTENER_TRACKING_ID_SQ );
		ptmt.setString(1, pagosid);
		rst = ptmt.executeQuery();
		
		if(rst.next()){
			return rst.getString(1);
		}
		
	}catch(Exception e){
		logger.error( e);
	}
	finally {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			logger.error( e);
		}
	}
	//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
	return null;
	
}
public static String obtenerTelefono ( String acount_no){
	CBConciliacionDetallada detalle = new CBConciliacionDetallada();
	PreparedStatement ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(ConsultasSQ.OBTENER_TELEFONO_SQ);
		logger.debug("query para obtener el telefono ->" + ConsultasSQ.OBTENER_TELEFONO_SQ );
		ptmt.setString(1, acount_no);
		rst = ptmt.executeQuery();
		
		if(rst.next()){
			return rst.getString(1);
			
		}
		
		
	}catch(Exception e){
		logger.error( e);
	}
	finally {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			logger.error( e);
		}
	}
	//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
	return null;
	
}
public boolean actualizarTransDate( String fecha, int trackingId){

	boolean result = false;
	Connection con = null;
	PreparedStatement ptmt = null;
	
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(ConsultasSQ.ACTUALIZA_TRANS_DATE_SQ);
		logger.debug("query para actualizar la fecha trans_date ->" + ConsultasSQ.ACTUALIZA_TRANS_DATE_SQ );
		//logger.debug("actualizarTrackingId ->" + " la fecha " + fecha);
		//logger.debug("actualizarTrackingId ->" + " el trackingId " + trackingId);
		
		ptmt.setString(1, fecha);
		ptmt.setInt(2, trackingId);
		
		return ptmt.executeUpdate() > 0;
		
	}catch(SQLException e){
		logger.error("error1", e);
	}catch (Exception e) {
		logger.error("error2", e);
	} finally {
		try {
			if (ptmt != null)
				ptmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			logger.error("error3", e);
		}
	}

	return result;
}
public static String obtenerCbPagosid ( String conciliacionid){
	CBConciliacionDetallada detalle = new CBConciliacionDetallada();
	PreparedStatement ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(Tools.OBTENER_CBPAGOSID);
		logger.debug("query para obtener el id del pago ->" + Tools.OBTENER_CBPAGOSID );
		ptmt.setString(1, conciliacionid);
		rst = ptmt.executeQuery();
		
		if(rst.next()){
			//return rst.getString(1);
			return rst.getString(Constantes.CB_PAGOS_ID);
			
		}
		
		
	}catch(Exception e){
		logger.error( e);
	}
	finally {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			logger.error( e);
		}
	}
	return null;
}
public static String obtenerNum_Secuenci ( String cbpagosid){
	CBConciliacionDetallada detalle = new CBConciliacionDetallada();
	PreparedStatement ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(Tools.OBTENER_NUM_SECUENCI);
		logger.debug("query para obtener el num secuenci ->" + Tools.OBTENER_NUM_SECUENCI );
		ptmt.setString(1, cbpagosid);
		rst = ptmt.executeQuery();
		
		if(rst.next()){
			return rst.getString(1);
			
		}
		
		
	}catch(Exception e){
		logger.error( e);
	}
	finally {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			logger.error( e);
		}
	}
	return null;
}
public static String obtenerReferencia(String trackingid){
	PreparedStatement ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(Tools.OBTENER_REFERENCIA);
		logger.debug("query para obtener la referencia " + Tools.OBTENER_REFERENCIA);
		ptmt.setString(1, trackingid);
		rst = ptmt.executeQuery();
		if(rst.next()){
			return rst.getString(1);
		}
		
	}catch(Exception e){
		logger.error(e);
		
	}
	finally{
		try{
			if(con != null)
				con.close();
			}catch(SQLException e){
				
			}
		
	}
	return null;
}
public static String ontenerCTN(int trackingId){
	PreparedStatement  ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	
	try {
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(Tools.OBTENER_CTN);
		logger.debug("query para obtener CTN " + Tools.OBTENER_CTN);
		ptmt.setInt(1, trackingId);
		rst = ptmt.executeQuery();
		
		while(rst.next()){
			return  rst.getString(1);
		}
		
	}catch(Exception e){
		logger.error(e);
	}
	finally{
		try{
			if(con != null)
				con.close();
			}catch(SQLException e){
				
			}
		
	}
	return null;
}
public boolean actualizarTransDateReversa( String fecha, int accountNo, int trackingId){

	boolean result = false;
	Connection con = null;
	PreparedStatement ptmt = null;
	
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(ConsultasSQ.ACTUALIZAR_TRANS_DATE_SQ);
		logger.debug("query para actualizar la fecha trans_date ->" + ConsultasSQ.ACTUALIZAR_TRANS_DATE_SQ );
		//logger.debug("actualizarTrackingId ->" + " la fecha " + fecha);
		//logger.debug("actualizarTrackingId ->" + " el trackingId " + trackingId);
		
		ptmt.setString(1, fecha);
		ptmt.setInt(2, accountNo);
		ptmt.setInt(3, trackingId);
		
		return ptmt.executeUpdate() > 0;
		
	}catch(SQLException e){
		logger.error("error1", e);
	}catch (Exception e) {
		logger.error("error2", e);
	} finally {
		try {
			if (ptmt != null)
				ptmt.close();
			if (con != null)
				con.close();
		} catch (Exception e) {
			logger.error("error3", e);
		}
	}

	return result;
}


public static String obtenerAccountno ( String acount_no){
	CBConciliacionDetallada detalle = new CBConciliacionDetallada();
	PreparedStatement ptmt = null;
	ResultSet rst = null;
	Connection con = null;
	try{
		con = ControladorBase.obtenerDtsPromo().getConnection();
		ptmt = con.prepareStatement(ConsultasSQ.OBTENER_ACCOUNT_NO_SQ);
		logger.debug("query para obtener el telefono ->" + ConsultasSQ.OBTENER_ACCOUNT_NO_SQ );
		ptmt.setString(1, acount_no);
		rst = ptmt.executeQuery();
		
		if(rst.next()){
			return rst.getString(1);
			
		}
		
		
	}catch(Exception e){
		logger.error( e);
	}
	finally {
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			logger.error( e);
		}
	}
	//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
	return null;
	
}
}
