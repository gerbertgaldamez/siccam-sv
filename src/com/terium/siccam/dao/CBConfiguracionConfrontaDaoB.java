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

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBConciliacionDetallada;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;
import com.terium.siccam.model.CBConsultaEstadoCuentasModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.ConsultasSQ;

@SuppressWarnings("serial")
public class CBConfiguracionConfrontaDaoB extends ControladorBase{

	private String CONSULTA_LISTA_CONFRONTA = "SELECT cbconfiguracionconfrontaid cBConfiguracionConfrontaId, "
			+ " nombre nombre, "
			+ " delimitador1 delimitador1, "
			+ " delimitador2 delimitador2, "
			+ " cantidad_agrupacion cantidadAgrupacion, "
			+ " nomenclatura nomenclatura, "
			+ " estado estado, "
			+ " tipo tipo, "
			+ " formato_fecha formatoFecha, "
			+ " longitud_cadena longitudCadena, "
			+ " posiciones posiciones, "
			/*+ " cantidad_ajustes cantidadAjustes, "
			+ " descartar_transaccion descartarTransaccion, "*/
			+ " creado_por creadoPor, "
			+ " fecha_creacion fechaCreacion "
			+ " FROM CB_CONFIGURACION_CONFRONTA "
			+ " where estado = 1 "; //CarlosGodinez -> 05/01/2018
	private String CONSULTA_CONFIG_CONFRONTA = "SELECT cBConfiguracionConfrontaId cBConfiguracionConfrontaId, "
			+ " nombre nombre, "
			+ " delimitador1 delimitador1, "
			+ " delimitador2 delimitador2, "
			+ " cantidad_Agrupacion cantidadAgrupacion, "
			+ " nomenclatura nomenclatura, "
			+ " estado estado, "
			+ " tipo tipo, "
			+ " formato_Fecha formatoFecha,"
			+ " posiciones posiciones, "
			+ " longitud_cadena longitudCadena "
			/*+ " cantidad_ajustes cantidadAjustes, "
			+ " descartar_transaccion descartarTransaccion "*/
			+ " FROM CB_CONFIGURACION_CONFRONTA "
			+ " WHERE cBConfiguracionConfrontaId = ? ";

	
	// consulta lista confronta
	public List<CBConfiguracionConfrontaModel> obtieneListaConfronta() {
		List<CBConfiguracionConfrontaModel> listado = new ArrayList<CBConfiguracionConfrontaModel>();
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBConfiguracionConfrontaModel> bhl = new BeanListHandler<CBConfiguracionConfrontaModel>(
						CBConfiguracionConfrontaModel.class);
				listado = qr.query(con, CONSULTA_LISTA_CONFRONTA
						+ " order by nombre asc ", bhl, new Object[] {});
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	// consulta lista configuracion confronta
	public List<CBConfiguracionConfrontaModel> obtieneListaConfConfronta(
			int idConfronta) {
		List<CBConfiguracionConfrontaModel> listado = new ArrayList<CBConfiguracionConfrontaModel>();
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBConfiguracionConfrontaModel> bhl = new BeanListHandler<CBConfiguracionConfrontaModel>(
						CBConfiguracionConfrontaModel.class);
				listado = qr.query(con, CONSULTA_CONFIG_CONFRONTA, bhl,
						new Object[] { idConfronta });
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	/**
	 * Agregado por Carlos Godinez - Llena combobox de confrontas asociadas
	 * 
	 * Modificado ultima vez por Carlos Godinez -> 02/10/2017
	 * 
	 * */
	private String CONSULTA_CONFRONTAS_ASOCIADAS = "SELECT c.CBBANCOAGENCIACONFRONTAID, b.NOMBRE "
			+ "FROM CB_CATALOGO_AGENCIA a, CB_CONFIGURACION_CONFRONTA b, CB_BANCO_AGENCIA_CONFRONTA c "
			+ "WHERE b.CBCONFIGURACIONCONFRONTAID = c.CBCONFIGURACIONCONFRONTAID "
			+ "AND a.CBCATALOGOAGENCIAID = c.CBCATALOGOAGENCIAID "
			+ "AND b.ESTADO = 1 AND c.CBCATALOGOAGENCIAID = ? ";
		
	public List<CBConfiguracionConfrontaModel> obtieneListaConfrontasAsociadas(int idAgencia, int idbancoagenciaconfronta){
		List<CBConfiguracionConfrontaModel> lista = new ArrayList<CBConfiguracionConfrontaModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			if(idbancoagenciaconfronta > 0 ) {
				CONSULTA_CONFRONTAS_ASOCIADAS = CONSULTA_CONFRONTAS_ASOCIADAS + " and not c.cbbancoagenciaconfrontaid  = " + idbancoagenciaconfronta  ;
			}
				System.out.println("** Id agencia enviado = " + idAgencia);
				System.out.println("** Query consulta confrontas asociadas = " + CONSULTA_CONFRONTAS_ASOCIADAS);
				ps = con.prepareStatement(CONSULTA_CONFRONTAS_ASOCIADAS);
				ps.setInt(1, idAgencia);
				/*
				if(idbancoagenciaconfronta > 0 ) {
					ps.setInt(2, idbancoagenciaconfronta);
				}
					*/
				rs = ps.executeQuery();
				CBConfiguracionConfrontaModel obj = null;
				while(rs.next()){
					obj = new CBConfiguracionConfrontaModel();
					obj.setcBAgenciasConfrontaId(rs.getString(1));
					obj.setConfrontaPadre(rs.getString(2));
					lista.add(obj);
				}
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	} 
	
	

	
	private String AGENCIA_SQ = "select distinct cbcatalogoagenciaid, (codigo_colector || ' - ' || nombre) , codigo_colector  from cb_catalogo_agencia  " + 
			"			 where cuenta_contable is  null ORDER BY TO_NUMBER(codigo_colector) ASC ";
	
	public List<CBConfiguracionConfrontaModel> obtieneListaConfrontasAsociadass(int idAgencia, int idbancoagenciaconfronta){
			List<CBConfiguracionConfrontaModel> lista = new ArrayList<CBConfiguracionConfrontaModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.INFO,
					"Query llenado de combo de Entidades = " + AGENCIA_SQ);
			ps = con.prepareStatement(AGENCIA_SQ);
			//ps.setInt(1, agrupacionSeleccionada);
			rs = ps.executeQuery();
			CBConfiguracionConfrontaModel obj = null;
			while (rs.next()) {
				obj = new CBConfiguracionConfrontaModel();
				obj.setcBAgenciasConfrontaId(rs.getString(1));
				obj.setConfrontaPadre(rs.getString(2));
				lista.add(obj);
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

		return lista;
	}

	
	
	/**
	 * Modificado por -> Carlos Godinez - Terium - 21/08/2017
	 * */
	private static final String QRY_CONFRONTA_EXISTENTE = "SELECT CBCONFIGURACIONCONFRONTAID FROM "
			+ "CB_CONFIGURACION_CONFRONTA WHERE UPPER(NOMBRE) = ?";
	
	public boolean confrontaExistente(String nomb){
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try{
			conn = obtenerDtsPromo().getConnection();
			cmd = conn.prepareStatement(QRY_CONFRONTA_EXISTENTE);
			cmd.setString(1, nomb);
			rs = cmd.executeQuery();
			if(rs.next()){
				result = true;
			}
			cmd.close();
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	
	// metodo para insertar una nueva configuracion de confrontas
	/**
	 * Editado ultima vez por Carlos Godinez - 29/05/2017
	 * 
	 * Agregando campo de linea_lectura
	 * */
	public int insertaNuevaConfrontaB(String nombre, String delimitador,
			String formatoFecha, String nomenclatura, String estado,
			String cantAgrupacion, String usuario, String nomenclaturaString,
			String longitudCadena, int lineaLectura/*, int cantidadAjus, String palabraDesc*/) {
		int resultado = 0;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				resultado = qr
						.update(con,
								"INSERT "
										+ "INTO CB_CONFIGURACION_CONFRONTA "
										+ "  ( "
										+ "    cbconfiguracionconfrontaid, "
										+ "    nombre, "
										+ "    delimitador1, "
										+ "    cantidad_agrupacion, "
										+ "    nomenclatura, "
										+ "    estado, "
										+ "    formato_fecha, "
										+ "    creado_por, "
										+ "    POSICIONES, "
										+ "    LONGITUD_CADENA, "
										/*+ "    CANTIDAD_AJUSTES, "
										+ "    DESCARTAR_TRANSACCION, "*/
										+ "    linea_lectura, "
										+ "    fecha_creacion "
										+ "  ) "
										+ "  VALUES "
										+ "  ( "
										+ "    (SELECT NVL(MAX(cbconfiguracionconfrontaid), 0)+1 FROM CB_CONFIGURACION_CONFRONTA), "
										+ "    '"
										+ nombre
										+ "', "
										+ "    '"
										+ delimitador
										+ "', "
										+ "    "
										+ cantAgrupacion
										+ ", "
										+ "    '"
										+ nomenclatura
										+ "', "
										+ "    "
										+ estado
										+ ", "
										+ "    '"
										+ formatoFecha
										+ "', "
										+ "    '"
										+ usuario
										+ "', "
										+ "    '"
										+ nomenclaturaString
										+ "', "
										+ "    '"
										+ longitudCadena
										+ "', "
										+ "    '"
										+ lineaLectura
										+ "', "
										/*+ "    '"
										+ cantidadAjus
										+ "', "
										+ "    '"
										+ palabraDesc
										+ "', "*/
										+ "    sysdate "
										+ "  )", new Object[] {});
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	public int insertaNuevaConfronta(String nombre, String delimitador,
			String formatoFecha, int lineaLectura, String nomenclatura, String estado,
			String cantAgrupacion, String usuario/*, int cantidadAjus,
			String palabraDesc*/) {
		int resultado = 0;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				resultado = qr
						.update(con,
								"INSERT "
										+ "INTO CB_CONFIGURACION_CONFRONTA "
										+ "  ( "
										+ "    cbconfiguracionconfrontaid, "
										+ "    nombre, "
										+ "    delimitador1, "
										+ "    cantidad_agrupacion, "
										+ "    nomenclatura, "
										+ "    estado, "
										+ "    formato_fecha, "
										+ "    linea_lectura, "
										/*+ "    CANTIDAD_AJUSTES, "
										+ "    DESCARTAR_TRANSACCION, "*/
										+ "    creado_por, "
										+ "    fecha_creacion "
										+ "  ) "
										+ "  VALUES "
										+ "  ( "
										+ "    CB_CONFIGURACION_CONFRONTA_SQ.nextval, "//modifica ovidio
										+ "    '"
										+ nombre
										+ "', "
										+ "    '"
										+ delimitador
										+ "', "
										+ "    "
										+ cantAgrupacion
										+ ", "
										+ "    '"
										+ nomenclatura
										+ "', "
										+ "    "
										+ estado
										+ ", "
										+ "    '"
										+ formatoFecha
										+ "', "
										+ "    '"
										+ lineaLectura
										+ "', "
										+ "    '"
										/*+ cantidadAjus
										+ "', "
										+ "    '"
										+ palabraDesc
										+ "', "
										+ "    '"*/
										+ usuario
										+ "', "
										+ "    sysdate "
										+ "  )", new Object[] {});
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	// elimina confronta

	private String ELIMINA_CONF_CONFRONTA = "delete CB_CONFIGURACION_CONFRONTA where cbconfiguracionconfrontaid = ?";

	public int eliminaConfConfronta(String idFila) {
		int respuesta = 0;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				respuesta = qr.update(con, ELIMINA_CONF_CONFRONTA, idFila);
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return respuesta;
	}

	
	// actualiza confronta
	/**
	 * Editado ultima vez por Carlos Godinez - 29/05/2017
	 * 
	 * Agregando campo de linea_lectura
	 * */
		private String MODIFICA_CONF_CONFRONTA_B = "UPDATE cb_configuracion_confronta "
				+ "SET nombre                       = ?, "
				+ "  delimitador1                   = ?, "
				+ "  cantidad_agrupacion            = ?, "
				+ "  nomenclatura                   = ?, "
				+ "  estado                         = ?, "
				+ "  formato_fecha                  = ?, "
				+ "  linea_lectura                  = ?, "
				+ "  POSICIONES                     = ?, "
				+ "  LONGITUD_CADENA                = ?, "
				/*+ "  CANTIDAD_AJUSTES				= ?, "
				+ "  DESCARTAR_TRANSACCION			= ?, "*/
				+ "  modificado_por                 = ?, "
				+ "  fecha_modificacion             = sysdate "
				+ "WHERE cbconfiguracionconfrontaid = ?";

	public int modificarConfConfrontaB(String nombre, String delimitador,
			String formatoFecha, String nomenclatura, String estado,
			String cantAgrupacion, int lineaLectura, String usuario, String idConfronta,
			String nomenclaturaString, String longitudCadena/*, int cantidadAjus,
			String palabraDesc*/) {
		int respuesta = 0;

		if (estado.compareTo("ACTIVO") == 0) {
			estado = "1";
		} else if (estado.compareTo("BAJA") == 0) {
			estado = "2";
		}

		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				respuesta = qr.update(con, MODIFICA_CONF_CONFRONTA_B, nombre,
						delimitador, cantAgrupacion, nomenclatura, estado,
						formatoFecha, lineaLectura, nomenclaturaString, longitudCadena,
						/*cantidadAjus, palabraDesc,*/ usuario, idConfronta);
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return respuesta;
	}
	
	private String MODIFICA_CONF_CONFRONTA = "UPDATE cb_configuracion_confronta "
			+ "SET nombre                       = ?, "
			+ "  delimitador1                   = ?, "
			+ "  cantidad_agrupacion            = ?, "
			+ "  nomenclatura                   = ?, "
			+ "  estado                         = ?, "
			+ "  formato_fecha                  = ?, "
			+ "  linea_lectura                  = ?, "
			/*+ "  CANTIDAD_AJUSTES				= ?, "
			+ "  DESCARTAR_TRANSACCION			= ?, "*/
			+ "  modificado_por                 = ?, "
			+ "  fecha_modificacion             = sysdate "
			+ "WHERE cbconfiguracionconfrontaid = ?";

	public int modificarConfConfronta(String nombre, String delimitador,
			String formatoFecha, int lineaLectura, String nomenclatura, String estado,
			String cantAgrupacion, String usuario, String idConfronta/*,
			int cantidadAjus, String palabraDesc*/) {
		int respuesta = 0;

		if (estado.compareTo("ACTIVO") == 0) {
			estado = "1";
		} else if (estado.compareTo("BAJA") == 0) {
			estado = "2";
		}

		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				respuesta = qr.update(con, MODIFICA_CONF_CONFRONTA, nombre,
						delimitador, cantAgrupacion, nomenclatura, estado,
						formatoFecha, lineaLectura, /*cantidadAjus, palabraDesc,*/ usuario,
						idConfronta);
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return respuesta;
	}
	
	/**
	 * @author Carlos Godinez 12/07/2017
	 * 
	 * Obtenemos el listado de delimitadores para configuraciones de confronta
	 * Dichos datos se obtienen de la tabla de configuraciones generales
	 * */
	private static final String OBTIENE_DELIMITADORES_QRY = "SELECT OBJETO, VALOR_OBJETO1, DESCRIPCION " + 
			"  FROM CB_MODULO_CONCILIACION_CONF " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_CONFRONTA') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('DELIMITADOR') " +
			"   AND UPPER (estado) = UPPER ('S')";
	
	public List<CBParametrosGeneralesModel> obtenerParamDelimitadores(){
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(OBTIENE_DELIMITADORES_QRY);
				
				rs = ps.executeQuery();
				CBParametrosGeneralesModel obj = null;
				while(rs.next()){
					obj = new CBParametrosGeneralesModel();
					obj.setObjeto(rs.getString(1));
					obj.setValorObjeto1(rs.getString(2));
					obj.setDescripcion(rs.getString(3));
					lista.add(obj);
				}
		} catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return lista;
	}
	
	/**
	 * @author Carlos Godinez 12/07/2017
	 * 
	 * Obtenemos el listado de delimitadores para configuraciones de confronta
	 * Dichos datos se obtienen de la tabla de configuraciones generales
	 * */
	private static final String OBTIENE_NOMENCLATURAS_QRY = "SELECT OBJETO, VALOR_OBJETO1, DESCRIPCION " + 
			"  FROM CB_MODULO_CONCILIACION_CONF " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_CONFRONTA') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('NOMENCLATURA') " +
			"   AND UPPER (estado) = UPPER ('S') ORDER BY OBJETO";
	
	public List<CBParametrosGeneralesModel> obtenerParamNomenclaturas(){
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(OBTIENE_NOMENCLATURAS_QRY);
				
				rs = ps.executeQuery();
				CBParametrosGeneralesModel obj = null;
				while(rs.next()){
					obj = new CBParametrosGeneralesModel();
					obj.setObjeto(rs.getString(1));
					obj.setValorObjeto1(rs.getString(2));
					obj.setDescripcion(rs.getString(3));
					lista.add(obj);
				}
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return lista;
	}
	
	/**
	 * Agrega Carlos Godinez - QitCorp - 14/08/2017
	 * Validacion de formato de fecha aceptado por Oracle
	 * */
	
	private static final String VALIDA_FORMATO_FECHA = "select to_char(sysdate, ?) resultado from dual";
	
	public boolean formatoFechaValido(String formatoStr){
		System.out.println("===== Entra al metodo de validacion de formato de fecha =====");
		boolean resultado = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(VALIDA_FORMATO_FECHA);
				ps.setString(1, formatoStr);
				rs = ps.executeQuery();
				while(rs.next()){
					System.out.println("Formato de fecha obtenido por Oracle = " + rs.getString(1));
					resultado = true;
				}
		} catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}
	
	private String QRY_OBTIENE_ESTADOS = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE UPPER(MODULO) = UPPER('CONFIGURACION_CONFRONTA') "
			+ "AND UPPER(TIPO_OBJETO) =  UPPER('ESTADO') " +
			"   AND UPPER(ESTADO) = UPPER ('S')";
	
	public List<CBParametrosGeneralesModel> obtenerEstadoCmb(){
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(QRY_OBTIENE_ESTADOS);
				rs = ps.executeQuery();
				CBParametrosGeneralesModel obj = null;
				while(rs.next()){
					obj = new CBParametrosGeneralesModel();
					obj.setObjeto(rs.getString(1));
					obj.setValorObjeto1(rs.getString(2));
					lista.add(obj);
				}
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return lista;
	} 
	
	/**
	 * @author Carlos Godinez 12/07/2017
	 * 
	 * Obtenemos el listado de delimitadores de configuraciones de confronta registradas
	 * */
	private static final String QRY_GENERAL_CONF_CONFRONTAS = "SELECT CBCONFIGURACIONCONFRONTAID, NOMBRE, DELIMITADOR1, CANTIDAD_AGRUPACION, "
			+ "NOMENCLATURA, ESTADO, CASE ESTADO WHEN 1 THEN 'ACTIVA' WHEN 0 THEN 'BAJA' END ESTADO_TXT, FORMATO_FECHA, POSICIONES, LONGITUD_CADENA, "
			+ "LINEA_LECTURA FROM CB_CONFIGURACION_CONFRONTA ORDER BY NOMBRE";
	
	public List<CBConfiguracionConfrontaModel> consultaGeneral(){
		List<CBConfiguracionConfrontaModel> lista = new ArrayList<CBConfiguracionConfrontaModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(QRY_GENERAL_CONF_CONFRONTAS);
				
				rs = ps.executeQuery();
				CBConfiguracionConfrontaModel obj = null;
				while(rs.next()){
					obj = new CBConfiguracionConfrontaModel();
					obj.setcBConfiguracionConfrontaId(rs.getString(1));
					obj.setNombre(rs.getString(2));
					obj.setDelimitador1(rs.getString(3));
					obj.setCantidadAgrupacion(rs.getInt(4));
					obj.setNomenclatura(rs.getString(5));
					obj.setEstado(rs.getInt(6));
					obj.setEstadoTxt(rs.getString(7));
					obj.setFormatoFecha(rs.getString(8));
					obj.setPosiciones(rs.getString(9));
					obj.setLongitudCadena(rs.getString(10));
					obj.setLineaLectura(rs.getInt(11));
					lista.add(obj);
				}
		} catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return lista;
	}
	
	/**
	 * Agregado por Carlos Godinez - Terium - 21/08/2017
	 * */
	private static final String QRY_INSERT = "INSERT INTO CB_CONFIGURACION_CONFRONTA "
			+ "(CBCONFIGURACIONCONFRONTAID, NOMBRE, DELIMITADOR1, CANTIDAD_AGRUPACION, NOMENCLATURA, "
			+ "ESTADO, FORMATO_FECHA, CREADO_POR, FECHA_CREACION, "
			+ "POSICIONES, LONGITUD_CADENA, LINEA_LECTURA) VALUES (CB_CONFIGURACION_CONFRONTA_SQ.nextval,?,?,?,?,?,?,?, sysdate, ?,?,?)";
	
	public boolean insertar(CBConfiguracionConfrontaModel obj) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(QRY_INSERT);
				cmd.setString(1, obj.getNombre());
				cmd.setString(2, obj.getDelimitador1());
				cmd.setInt(3, obj.getCantidadAgrupacion());
				cmd.setString(4, obj.getNomenclatura());
				cmd.setInt(5, obj.getEstado());
				cmd.setString(6, obj.getFormatoFecha());
				cmd.setString(7, obj.getCreadoPor());
				cmd.setString(8, obj.getPosiciones());
				cmd.setString(9, obj.getLongitudCadena());
				cmd.setInt(10, obj.getLineaLectura());
				if(cmd.executeUpdate() > 0)
					result = true;
				System.out.println("\n*** Registro insertado con exito ***\n");
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	/**
	 * Agregado por Carlos Godinez - Terium - 21/08/2017
	 * */
	private static final String QRY_UPDATE = "UPDATE CB_CONFIGURACION_CONFRONTA SET NOMBRE = ?, DELIMITADOR1 = ?, ESTADO = ?, "
			+ "FORMATO_FECHA = ?, LINEA_LECTURA = ?, MODIFICADO_POR = ?, FECHA_MODIFICACION = sysdate, "
			+ "CANTIDAD_AGRUPACION = ?, NOMENCLATURA = ?, POSICIONES = ?, LONGITUD_CADENA = ? "
			+ "WHERE CBCONFIGURACIONCONFRONTAID = ?";
	
	public boolean modificar(CBConfiguracionConfrontaModel obj) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(QRY_UPDATE);
				cmd.setString(1, obj.getNombre());
				cmd.setString(2, obj.getDelimitador1());
				cmd.setInt(3, obj.getEstado());
				cmd.setString(4, obj.getFormatoFecha());
				cmd.setInt(5, obj.getLineaLectura());
				cmd.setString(6, obj.getModificadoPor());
				cmd.setInt(7, obj.getCantidadAgrupacion());
				cmd.setString(8, obj.getNomenclatura());
				cmd.setString(9, obj.getPosiciones());
				cmd.setString(10, obj.getLongitudCadena());
				cmd.setString(11, obj.getcBConfiguracionConfrontaId());
				cmd.executeUpdate();
				result = true;
				System.out.println("\n*** Registro modificado con exito ***\n");
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	/**
	 * Agregado por Carlos Godinez - Terium - 21/08/2017
	 * */
	private static final String QRY_DELETE = "DELETE FROM CB_CONFIGURACION_CONFRONTA WHERE CBCONFIGURACIONCONFRONTAID = ?";
	
	public boolean eliminar(int pk) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(QRY_DELETE);
				cmd.setInt(1, pk);
				cmd.executeUpdate();
				result = true;
				System.out.println("\n*** Registro eliminado con exito ***\n");
		}catch (Exception e) {
			Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConfiguracionConfrontaDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	public static String obtenerETH ( ){
		//CBConciliacionDetallada detalle = new CBConciliacionDetallada();
		PreparedStatement ptmt = null;
		ResultSet rst = null;
		Connection con = null;
		try{
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ptmt = con.prepareStatement(ConsultasSQ.OBTENER_ETH);
			
			rst = ptmt.executeQuery();
			
			if(rst.next()){
				return rst.getString(1);
			}
			
		}catch(Exception e){
			//logger.error( e);
		}
		finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				//logger.error( e);
			}
		}
		//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
		return null;
		
	}
	
	public static String obtenerII ( ){
		//CBConciliacionDetallada detalle = new CBConciliacionDetallada();
		PreparedStatement ptmt = null;
		ResultSet rst = null;
		Connection con = null;
		try{
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ptmt = con.prepareStatement(ConsultasSQ.OBTENER_II);
			
			rst = ptmt.executeQuery();
			
			if(rst.next()){
				return rst.getString(1);
			}
			
		}catch(Exception e){
			//logger.error( e);
		}
		finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				//logger.error( e);
			}
		}
		//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
		return null;
		
	}
	public static String obtenerTAPFI ( ){
		//CBConciliacionDetallada detalle = new CBConciliacionDetallada();
		PreparedStatement ptmt = null;
		ResultSet rst = null;
		Connection con = null;
		try{
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ptmt = con.prepareStatement(ConsultasSQ.OBTENER_TAPFI );
			
			rst = ptmt.executeQuery();
			
			if(rst.next()){
				return rst.getString(1);
			}
			
		}catch(Exception e){
			//logger.error( e);
		}
		finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				//logger.error( e);
			}
		}
		//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
		return null;
		
	}
	public static String obtenerWG ( ){
		//CBConciliacionDetallada detalle = new CBConciliacionDetallada();
		PreparedStatement ptmt = null;
		ResultSet rst = null;
		Connection con = null;
		try{
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ptmt = con.prepareStatement(ConsultasSQ.OBTENER_WG );
			
			rst = ptmt.executeQuery();
			
			if(rst.next()){
				return rst.getString(1);
			}
			
		}catch(Exception e){
			//logger.error( e);
		}
		finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				//logger.error( e);
			}
		}
		//logger.debug("obtenerCodAgenciaReversa ->" + " el cod agencia es null " );
		return null;
		
	}
}
