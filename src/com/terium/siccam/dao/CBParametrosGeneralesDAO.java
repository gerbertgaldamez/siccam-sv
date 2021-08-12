package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBParametrosGeneralesModel;

@SuppressWarnings("serial")
public class CBParametrosGeneralesDAO extends ControladorBase{
	
	private String QRY_OBTIENE_ESTADOS = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'PARAMETROS_GENERALES_CONF' AND TIPO_OBJETO =  'ESTADO'";
	
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
			
		} catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return lista;
	} 
	
	private String QRY_GENERAL = "SELECT CBMODULOCONCILIACIONCONFID, MODULO, TIPO_OBJETO, OBJETO, VALOR_OBJETO1, VALOR_OBJETO2, VALOR_OBJETO3, "
			+ "DESCRIPCION, ESTADO FROM CB_MODULO_CONCILIACION_CONF WHERE ACCESO_MODULO = 'S' ";

	public List<CBParametrosGeneralesModel> consultaGeneral(String modulo, String tipoObjeto, String objeto, String valor1, String valor2 , String valor3, String descripcion, String estado){
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		Statement ps = null;
		Connection con = null;
		
			
			try {
				con = obtenerDtsPromo().getConnection();
				String where = "";
				if(!modulo.equals("")){
					where += " AND UPPER(MODULO) LIKE '%" + modulo.toUpperCase() + "%' ";
				}
				if(!tipoObjeto.equals("")){
					where += " AND UPPER(TIPO_OBJETO) LIKE '%" + tipoObjeto.toUpperCase() + "%' ";
				}
				if(!objeto.equals("")){
					where += " AND UPPER(OBJETO) LIKE '%" + objeto.toUpperCase() + "%' ";
				}
				if(!valor1.equals("")){
					where += " AND UPPER(VALOR_OBJETO1) LIKE '%" + valor1.toUpperCase() + "%' ";
				}
				if(!valor2.equals("")){
					where += " AND UPPER(VALOR_OBJETO2) LIKE '%" + valor2.toUpperCase() + "%' ";
				}
				if(!valor3.equals("")){
					where += " AND UPPER(VALOR_OBJETO3) LIKE '%" + valor3.toUpperCase() + "%' ";
				}
				if(!descripcion.equals("")){
					where += " AND UPPER(DESCRIPCION) LIKE '%" + descripcion.toUpperCase() + "%' ";
				}
				if(!estado.equals("")){
					where += " AND UPPER(ESTADO) = '" + estado.toUpperCase() + "' ";
				}
				Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
					.log(Level.INFO,"Query general parametros generales = " + QRY_GENERAL + where + " ORDER BY CBMODULOCONCILIACIONCONFID");
				
				ps = con.createStatement();
				rs = ps.executeQuery(QRY_GENERAL + where + " ORDER BY CBMODULOCONCILIACIONCONFID");
				CBParametrosGeneralesModel obj = null;
				while(rs.next()){
					obj = new CBParametrosGeneralesModel();
					obj.setCbmoduloconciliacionconfid(rs.getInt(1));
					obj.setModulo(rs.getString(2));
					obj.setTipoObjeto(rs.getString(3));
					obj.setObjeto(rs.getString(4));
					obj.setValorObjeto1(rs.getString(5));
					obj.setValorObjeto2(rs.getString(6));
					obj.setValorObjeto3(rs.getString(7));
					obj.setDescripcion(rs.getString(8));
					obj.setEstado(rs.getString(9));
					lista.add(obj);
				}
		}catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
	}
		return lista;
	} 
	
	private String QRY_INSERT = "Insert into CB_MODULO_CONCILIACION_CONF "
			+ "(CBMODULOCONCILIACIONCONFID, MODULO, TIPO_OBJETO, OBJETO, VALOR_OBJETO1, "
			+ "VALOR_OBJETO2, VALOR_OBJETO3, DESCRIPCION, ESTADO, SACREATEDY, "
			+ "SADATECREATED, ACCESO_MODULO)"
			+ "Values(CB_MODULO_CONCILIACION_CONF_SQ.nextval,?,?,?,?,?,?,?,?,?, sysdate, 'S')";
	
	public boolean insertar(CBParametrosGeneralesModel obj) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
			
			try {
				conn = obtenerDtsPromo().getConnection();
			    cmd = conn.prepareStatement(QRY_INSERT);
				cmd.setString(1, obj.getModulo());
				cmd.setString(2, obj.getTipoObjeto());
				cmd.setString(3, obj.getObjeto());
				cmd.setString(4, obj.getValorObjeto1());
				cmd.setString(5, obj.getValorObjeto2());
				cmd.setString(6, obj.getValorObjeto3());
				cmd.setString(7, obj.getDescripcion());
				cmd.setString(8, obj.getEstado());
				cmd.setString(9, obj.getCreador());
			if(cmd.executeUpdate() > 0)
				result = true;
				Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
					.log(Level.INFO,"\n*** Registro insertado con exito ***\n");
		}catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	private String QRY_UPDATE = "UPDATE CB_MODULO_CONCILIACION_CONF SET MODULO = ?, TIPO_OBJETO = ?, OBJETO = ?, VALOR_OBJETO1 = ?, "
			+ "VALOR_OBJETO2 = ?, VALOR_OBJETO3 = ?, DESCRIPCION = ?, ESTADO = ? WHERE CBMODULOCONCILIACIONCONFID = ?";
	
	public boolean modificar(CBParametrosGeneralesModel obj) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
			
			try {
				conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(QRY_UPDATE);
				cmd.setString(1, obj.getModulo());
				cmd.setString(2, obj.getTipoObjeto());
				cmd.setString(3, obj.getObjeto());
				cmd.setString(4, obj.getValorObjeto1());
				cmd.setString(5, obj.getValorObjeto2());
				cmd.setString(6, obj.getValorObjeto3());
				cmd.setString(7, obj.getDescripcion());
				cmd.setString(8, obj.getEstado());
				cmd.setInt(9, obj.getCbmoduloconciliacionconfid());
			if(cmd.executeUpdate() > 0)
				result = true;
				Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
					.log(Level.INFO,"\n*** Registro insertado con exito ***\n");
		}catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	private String QRY_DELETE = "DELETE FROM CB_MODULO_CONCILIACION_CONF WHERE CBMODULOCONCILIACIONCONFID = ?";
	
	public boolean eliminar(int pk) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
			
			try {
				conn = obtenerDtsPromo().getConnection();
				cmd = conn.prepareStatement(QRY_DELETE);
				cmd.setInt(1, pk);
			if(cmd.executeUpdate() > 0)
				result = true;
				Logger.getLogger(CBParametrosGeneralesDAO.class.getName())
					.log(Level.INFO,"\n*** Registro eliminado con exito ***\n");
		}catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(conn != null)conn.close();
			}catch (Exception e) {
				Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	/**
	 * @author JCDlacruz by 27/06/2017
	 * Last Modified: Carlos Godinez -> 08/08/2018
	 * Obtenemos el listado de convenios (tipos de clientes o transacciones) desde una configuracion
	 * */
	private static final String OBTIENE_CONVENIOS_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_TIPO_CLIENTE') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('CONVENIOS') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	public List<CBParametrosGeneralesModel> obtenerParamConvenios() {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			try {
				ps = con.prepareStatement(OBTIENE_CONVENIOS_QRY);
				rs = ps.executeQuery();
				CBParametrosGeneralesModel obj = null;
				while (rs.next()) {
					obj = new CBParametrosGeneralesModel();
					obj.setCbmoduloconciliacionconfid(rs.getInt(1));
					obj.setObjeto(rs.getString(2));
					obj.setValorObjeto1(rs.getString(3));
					lista.add(obj);
				}
			} finally {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			}
		} catch (Exception e) {
			System.out.println("Error al obtener lista de convenios: " + e.getMessage());
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
	/**
	 * @author Ovidio by 27/06/2017
	 * 
	 * */
	private static final String OBTIENE_CUENTAS_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_TIPO_AFILIACION') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('TIPO') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	public List<CBParametrosGeneralesModel> obtenerParamCuentasTipo() {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(OBTIENE_CUENTAS_QRY);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setCbmoduloconciliacionconfid(rs.getInt(1));
				obj.setObjeto(rs.getString(2));
				obj.setValorObjeto1(rs.getString(3));
				lista.add(obj);
			}
		} catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}

	/**
	 * @author Ovidio by 27/06/2017
	 * 
	 * Obtenemos el listado de ESTADO desde una configuracion
	 * */
	private static final String OBTIENE_CUENTAS_ESTADO_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_ESTADO_AFILIACION') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('ESTADO') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	public List<CBParametrosGeneralesModel> obtenerParamCuentasEstado(){
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;			
			try {
				con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(OBTIENE_CUENTAS_ESTADO_QRY);
				System.out.println("query combo estado " + OBTIENE_CUENTAS_ESTADO_QRY);
				rs = ps.executeQuery();
				System.out.println("lista dao " + lista.size());
				CBParametrosGeneralesModel obj = null;
				while(rs.next()){
					System.out.println("entra al while en dao");
					obj = new CBParametrosGeneralesModel();
					obj.setCbmoduloconciliacionconfid(rs.getInt(1));
					obj.setObjeto(rs.getString(2));
					obj.setValorObjeto1(rs.getString(3));
					lista.add(obj);
				}
		}catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
	}
		return lista;
	}
	
	
	/**
	 * @author Ovidio by 26/07/2017
	 * 
	 * Obtenemos  (tipos de tarjetas) desde una configuracion
	 * */
	private static final String OBTIENE_TIPO_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONSULTA_ESTADO_CUENTA_TARJETA') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('TIPO') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	public List<CBParametrosGeneralesModel> obtenerTipoTarjeta(){
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			try {
				ps = con.prepareStatement(OBTIENE_TIPO_QRY);
				System.out.println("query tipo de tarjeta " + OBTIENE_TIPO_QRY);
				rs = ps.executeQuery();
				CBParametrosGeneralesModel obj = null;
				while(rs.next()){
					obj = new CBParametrosGeneralesModel();
					obj.setCbmoduloconciliacionconfid(rs.getInt(1));
					obj.setObjeto(rs.getString(2));
					obj.setValorObjeto1(rs.getString(3));
					lista.add(obj);
				}
			} finally {
				if(rs != null)
					rs.close();
				if(ps != null)
					ps.close();
				if(con != null)
					con.close();
			}
		} catch (Exception e) {
			System.out.println("Error al obtener lista de estado en pantalla de parametros generales: "+ e.getMessage());
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		} 
		return lista;
	}
	
	/**
	 * @author Omar Gomez -Qit Corp - 13/07/2017
	 * 
	 * Retorna la lista con los tipos de agrupaciones
	 */
	
	// QUERY : FINANCIERA - NO FINANCIERA
	public static final String S_OBTENER_TIPO_AGRUPACIONES = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY "+
			"FROM CB_MODULO_CONCILIACION_CONF " + 
			"WHERE MODULO = 'CONFIGURACION_AGRUPACIONES' " + 
			"AND TIPO_OBJETO ='TIPO_AGRUPACIONES' ";
	
	// QUERY : ACTIVA - INACTIVA
	public static final String S_OBTENER_TIPO_AGRUPACIONES_2 = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY " + 
			"			FROM CB_MODULO_CONCILIACION_CONF " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES' " + 
			"			AND TIPO_OBJETO ='ESTADO'";
	
	// QUERY : TIPO AGENCIA {PREPAGO , POSTPAGO}
	public static final String S_OBTENER_TIPO_AGENCIA = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY " + 
			"			FROM CB_MODULO_CONCILIACION_CONF " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES' " + 
			"			AND TIPO_OBJETO ='TIPO_AGENCIA' ";
	
	// QUERY : TIPO AGENCIA VF {VIRTUAL ,PRESENCIAL}
	public static final String S_OBTENER_AGENCIA_VF =
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES'  " + 
			"			AND TIPO_OBJETO ='TIPO_AGENCIA_VF' ";
	
	// QUERY : LIQUIDACION TIPO DE CARGA {CARGA UNICA , CARGA MASIVA}
	public static final String S_OBTENER_TIPO_LIQUIDACION_CARGA = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES'  " + 
			"			AND TIPO_OBJETO ='TIPO_AGRUPACION_LIQUIDACION_CARGA' ";
	
	// QUERY : CONFIGURACION NOMENCLATURA CONFRONTA
	public static final String S_OBTENER_CONF_NOMENCLATURA = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONFIGURACION_CONFRONTA'  " + 
			"			AND TIPO_OBJETO ='DELIMITADOR' ";
	
	// QUERY : TIPO PAGO {PREPAGO , POSTPAGO}
	public static final String S_OBTENER_PAGO_CONCILIACION = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'APLICA_DESAPLICA_PAGO'  " + 
			"			AND TIPO_OBJETO ='TIPO' AND OBJETO != 'TODOS' ";
	
	public static final String S_OBTENER_ESTADO_CONCILIACION = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONCILIACION'  " + 
			"			AND TIPO_OBJETO ='ESTADO' ";
	
	public static final String S_OBTENER_ESTADO_CONCILIACION_DET = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONCILIACION'  " + 
			"			AND TIPO_OBJETO ='ESTADO_DETALLE' ";
	
	public List<CBParametrosGeneralesModel> obtenerListaTipoAgrupacion(String query) {
		List<CBParametrosGeneralesModel> listResult = new ArrayList<CBParametrosGeneralesModel>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CBParametrosGeneralesModel model = null;
		try {
			System.out.println("Query : " + query);
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("nombre " + rs.getString(4));
				model = new CBParametrosGeneralesModel();
				model.setCbmoduloconciliacionconfid(rs.getInt(1));
				model.setModulo(rs.getString(2));
				model.setTipoObjeto(rs.getString(3));
				model.setObjeto(rs.getString(4));
				model.setValorObjeto1(rs.getString(5));
				model.setValorObjeto2(rs.getString(6));
				model.setValorObjeto3(rs.getString(7));
				model.setDescripcion(rs.getString(8));
				model.setEstado(rs.getString(9));
				model.setCreador(rs.getString(10));
				listResult.add(model);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		} catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		if (listResult != null)
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.INFO,
					"Lista tipo agrupacion Quantity: " + listResult.size());
		return listResult;
	}
	
	
	//Obtener parametros para consumo de WS Pagos y Servicio
	public static final String OBTENER_PARAM_WS_QRY = "SELECT CBMODULOCONCILIACIONCONFID,"
			+ "       MODULO,TIPO_OBJETO, OBJETO,"
			+ "       VALOR_OBJETO1,"
			+ "       DESCRIPCION, ESTADO FROM CB_MODULO_CONCILIACION_CONF"
			+ " WHERE MODULO = 'CONCILIACION' "
			+ "AND TIPO_OBJETO = 'APLICACION/DESAPLICACION' "
			+ "AND ESTADO = 'S'";
	
	public static List<CBParametrosGeneralesModel> obtenerParametrosWS() {
		List<CBParametrosGeneralesModel> listResult = new ArrayList<CBParametrosGeneralesModel>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CBParametrosGeneralesModel model = null;
		try {

			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(OBTENER_PARAM_WS_QRY);
			rs = ps.executeQuery();
			while (rs.next()) {
				model = new CBParametrosGeneralesModel();
				model.setCbmoduloconciliacionconfid(rs.getInt(1));
				model.setModulo(rs.getString(2));
				model.setTipoObjeto(rs.getString(3));
				model.setObjeto(rs.getString(4));
				model.setValorObjeto1(rs.getString(5));
				model.setDescripcion(rs.getString(6));
				model.setEstado(rs.getString(7));

				listResult.add(model);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		} catch (Exception e) {
			Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBParametrosGeneralesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return listResult;
	}

}
