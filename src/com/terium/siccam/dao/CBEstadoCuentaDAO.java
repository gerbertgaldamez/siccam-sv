package com.terium.siccam.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBEstadoCuentasModel;
import com.terium.siccam.model.CBPagosModel;
import com.terium.siccam.utils.ConsultasSQ;

/**
 * @author Juankrlos - 11/01/2017 -
 */

public class CBEstadoCuentaDAO extends ControladorBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Se eliminan todos los registros de una transaccion realizada para el mismo
	 * dia
	 */
	public static final String DELETE_REGISTROS_ARCHIVO = "DELETE FROM CB_ESTADO_CUENTA_ARCHIVOS WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?";
	public static final String DELETE_REGISTROS_CREDO = "DELETE FROM CB_ESTADO_CUENTA WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  ";
	public static final String DELETE_REGISTROS_CREDODET = "DELETE FROM CB_ESTADO_CUENTA_DET WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  ";
	public static final String DELETE_REGISTROS_BANCO_NACIONAL = "DELETE FROM CB_ESTADO_CUENTA WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  ";
	public static final String DELETE_REGISTROS_BANCO_NACIONAL1 = "DELETE FROM CB_ESTADO_CUENTA_DET WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  ";
	public static final String DELETE_REGISTROS_VISA = "DELETE FROM CB_ESTADO_CUENTA WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ? ";
	public static final String DELETE_REGISTROS_VISADET = "DELETE FROM CB_ESTADO_CUENTA_DET WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  ";
	public static final String DELETE_REGISTROS_SOCIEDAD = "DELETE FROM CB_ESTADO_CUENTA_SOCIEDAD WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?";

	public static final String DELETE_REGISTROS_SYS_COMERCIAL = "DELETE FROM CB_PAGOS WHERE"
			+ " CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ? ";
	// agregado por Gerbert
	public static final String DELETE_REGISTROS_LIQUIDACION = "DELETE FROM CB_CIERRE_CAJA WHERE"
			+ " CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ? ";

	public void eliminarRegistros(int idarchivo, int idConf, String archivo, String query) {
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(query);

			ps.setInt(1, idarchivo);
			ps.setInt(2, idConf);
			// ps.setString(3, archivo);

			System.out.println("entra al metodo para eliminar registros: " + idarchivo + " id conf: " + idConf);
			System.out.println("query: " + query);

			int exec = ps.executeUpdate();
			if (exec > 0) {
				System.out.println("Script ejecutado correctamente para eliminar registros: " + query);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	/**
	 * 
	 * */
	public void actualizaLiquidacionDetalle(int idarchivo) {
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(ConsultasSQ.UPDATE_LIQUIDACION_DETALLE);

			ps.setInt(1, idarchivo);

			System.out.println("entra al metodo para actualizar registros: " + idarchivo);

			int exec = ps.executeUpdate();
			if (exec > 0) {
				System.out.println("Script ejecutado correctamente para actualizar liquidacion detalle: "
						+ ConsultasSQ.UPDATE_LIQUIDACION_DETALLE);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	/**
	 * Validamos si existen registros ya cargados para el archivo seleccionado
	 */

	public CBEstadoCuentasModel validaCarga(int idconf, String archivo, String fecha) {
		CBEstadoCuentasModel result = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.VALIDA_CARGA_ESTADOS_SQ);
			System.out.println("query valida " + ConsultasSQ.VALIDA_CARGA_ESTADOS_SQ + idconf);
			ps.setInt(1, idconf);
			ps.setString(2, archivo);
			rs = ps.executeQuery();

			if (rs.next()) {

				result = new CBEstadoCuentasModel();
				result.setCbestadocuentaarchivosid(rs.getInt(1));
				result.setNombrearchivos(rs.getString(2));
				result.setFecha(rs.getString(3));
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Obtenemos la fecha para validar las transacciones realizadas en el dia
	 */

	public String obtieneFecha() {
		String result = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(ConsultasSQ.MUESTRA_FECHA_SQ);
			rs = ps.executeQuery();

			if (rs.next())
				result = rs.getString(1);

		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Obtener el ID para insertar archivo
	 */

	public int obtieneIdArchivos() {
		int result = 0;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.ESTADO_CUENTA_ARCHIVOS_ID);
			rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getInt(1);
			}

		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return result;
	}

	/**
	 * @author Juankrlos - 11/01/2017 - Obtenemos las cuentas configurados para los
	 *         estados de cuenta
	 */

	public List<CBEstadoCuentasModel> obtenerCuentasConf() {
		List<CBEstadoCuentasModel> listado = new ArrayList<CBEstadoCuentasModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.OBTIENE_ESTADO_CUENTACONF_SQ);
			System.out.println("query combo:" + ConsultasSQ.OBTIENE_ESTADO_CUENTACONF_SQ);
			rs = ps.executeQuery();
			CBEstadoCuentasModel objModel = null;
			while (rs.next()) {
				objModel = new CBEstadoCuentasModel();
				objModel.setCbestadocuentaconfid(rs.getInt(1));
				objModel.setNombrebanco(rs.getString(2));
				listado.add(objModel);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return listado;
	}

	/**
	 * Inserta los registros para banco nacional Agregado por Juankrlos - 12/01/2017
	 * -
	 */
	// MODIFICA OVIDIO SANTOS
	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// banco nacional
	public static final String INSERT_BANCO_NACIONAL_SQ = "INSERT INTO CB_ESTADO_CUENTA(CBESTADOCUENTAID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, FECHA_TRANSACCION,AFILIACION, TIPO,  BALANCE, CODIGO_LOTE, "
			+ " CONSUMO,RETENCION , PROPINA, IVA, COMISION, IMPUESTO_TURIS, LIQUIDO,  IVA_COMISION, "
			+ " DOCUMENTO, DESCRIPCION, DEBITO, REFERENCIA, CREDITO, SWCREATEBY, SWDATECREATED) "
			+ " VALUES(CB_ESTADO_CUENTA_SQ.NEXTVAL,?,?,to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";

	public boolean insertCuentasBancoNacional(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			CBEstadoCuentasModel objModel = null;
			Iterator<CBEstadoCuentasModel> it = list.iterator();
			int contador = 0;
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"entra al metodo para insertar visa pago: " + list.size());
			if (list.size() == 0) {
				return true;
			}
			while (it.hasNext()) {
				objModel = it.next();
				try {
					contador++;
					ps = con.prepareStatement(INSERT_BANCO_NACIONAL_SQ);

					ps.setInt(1, objModel.getCbestadocuentaconfid());
					ps.setInt(2, objModel.getCbestadocuentaarchivosid());
					ps.setString(3, objModel.getFechatransaccion());
					ps.setString(4, objModel.getAfilicacion());
					ps.setString(5, objModel.getTipo());
					ps.setString(6, objModel.getReferencia());
					ps.setString(7, objModel.getCodigo_lote());
					ps.setDouble(8, changeString(objModel.getConsumo()));
					ps.setDouble(9, changeString(objModel.getImpuestoturis()));
					ps.setString(10, (objModel.getPropina()));
					ps.setDouble(11, changeString(objModel.getIva()));
					ps.setDouble(12, changeString(objModel.getComision()));
					ps.setString(13, objModel.getIvacomision());
					ps.setDouble(14, changeString(objModel.getLiquido()));
					ps.setDouble(15, changeString(objModel.getRetencion()));
					ps.setString(16, objModel.getDocumento());
					ps.setString(17, objModel.getDescripcion());
					/// nuevos campos
					ps.setString(18, objModel.getDebito());
					ps.setString(19, objModel.getBalance());
					ps.setString(20, objModel.getCredito());
					ps.setString(21, objModel.getUsuario());
					ps.execute();

					ps.close();
					ps = null;
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if (contador > 0) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
						"Script ejecutado correctamente: " + contador);
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	// agregado por Gerbert
	public static final String INSERT_REPORTE_CIERRE_CAJA_SQ = "INSERT INTO CB_CIERRE_CAJA(CBCIERRECAJAID,CBESTADOCUENTACONFID,CBESTADOCUENTAARCHIVOSID,FECHA_SOLICITUD, "
			+ " CASO, ESTADO, CAP,DICTAMEN_TERSORERIA, SOLICITANTE, TOTAL_GENERAL_COLONES, TOTAL_GENERAL_VALORES , "
			+ " FILA , BOLETA_DEPOSITO, MONEDA, VALOR_TIPO_CAMBIO) "
			+ " VALUES(cb_cierre_caja_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	public boolean insertReporteCierreCaja(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			CBEstadoCuentasModel objModel = null;
			Iterator<CBEstadoCuentasModel> it = list.iterator();
			int contador = 0;
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"entra al metodo para insertar cierre caja: " + list.size());
			if (list.size() == 0) {
				return true;
			}
			while (it.hasNext()) {
				objModel = it.next();
				try {
					contador++;
					ps = con.prepareStatement(INSERT_REPORTE_CIERRE_CAJA_SQ);

					ps.setInt(1, objModel.getCbestadocuentaconfid());
					ps.setInt(2, objModel.getCbestadocuentaarchivosid());
					ps.setString(3, objModel.getFecha_solicitud());
					ps.setInt(4, objModel.getCaso());
					ps.setString(5, objModel.getEstado());
					ps.setString(6, objModel.getCap());
					ps.setString(7, objModel.getDictamen_tersoreria());
					ps.setString(8, objModel.getSolicitante());
					ps.setString(9, objModel.getTotalgeneralcolones());
					ps.setString(10, objModel.getTotalgeneralvalores());
					ps.setInt(11, objModel.getFila());
					ps.setString(12, objModel.getBoleta_deposito());
					ps.setString(13, objModel.getMoneda());
					ps.setString(14, objModel.getValor_tipo_cambio());
					// ps.setString(15, objModel.getUsuario());

					ps.execute();

					ps.close();
					ps = null;
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if (contador > 0) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
						"Script ejecutado correctamente: " + contador);
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	/**
	 * Inserta los registros para VISA Agregado por Juankrlos - 12/01/2017 -
	 */
	public boolean insertCuentasVisa(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			CBEstadoCuentasModel objModel = null;
			Iterator<CBEstadoCuentasModel> it = list.iterator();
			int contador = 0;
			System.out.println("entra al metodo para insertar visa pago: " + list.size());
			if (list.size() == 0) {
				return true;
			}

			System.out.println("Query: " + ConsultasSQ.INSERT_VISA_SQ);
			while (it.hasNext()) {
				objModel = it.next();
				try {
					contador++;
					ps = con.prepareStatement(ConsultasSQ.INSERT_VISA_SQ);

					ps.setInt(1, objModel.getCbestadocuentaconfid());
					ps.setInt(2, objModel.getCbestadocuentaarchivosid());
					ps.setString(3, objModel.getFechatransaccion());
					ps.setString(4, objModel.getAfilicacion());
					ps.setString(5, objModel.getTipo());
					ps.setString(6, objModel.getReferencia());
					ps.setString(7, objModel.getCodigo_lote());
					ps.setDouble(8, changeString(objModel.getConsumo()));
					ps.setDouble(9, changeString(objModel.getImpuestoturis()));
					ps.setDouble(10, changeString(objModel.getPropina()));
					ps.setDouble(11, changeString(objModel.getIva()));
					ps.setDouble(12, changeString(objModel.getComision()));
					ps.setDouble(13, changeString(objModel.getIvacomision()));
					ps.setDouble(14, changeString(objModel.getLiquido()));
					ps.setDouble(15, changeString(objModel.getRetencion()));
					ps.setString(16, objModel.getDocumento());
					ps.setString(17, objModel.getDescpago());
					ps.setString(18, objModel.getUsuario());
					ps.execute();

				} catch (Exception e) {
					ps.close();
					ps = null;
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
							"Error de counstring  " + e.getMessage());
				}
			}
			if (contador > 0) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
						"Script ejecutado correctamente: " + contador);
				System.out.println("contador " + contador);
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	/**
	 * Inserta los registros para VISA DETALLE Agregado por Juankrlos - 12/01/2017 -
	 */
	public boolean insertCuentasVisaDet(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			CBEstadoCuentasModel objModel = null;
			System.out.println("tama�o de lista: " + list.size());
			if (list.size() == 0) {
				return true;
			}
			Iterator<CBEstadoCuentasModel> it = list.iterator();
			int contador = 0;
			while (it.hasNext()) {
				objModel = it.next();
				try {
					contador++;
					ps = con.prepareStatement(ConsultasSQ.INSERT_VISA_DET_SQ);

					ps.setInt(1, objModel.getCbestadocuentaconfid());
					ps.setInt(2, objModel.getCbestadocuentaarchivosid());
					ps.setString(3, objModel.getAfilicacion());
					ps.setString(4, objModel.getFechacierre());
					ps.setString(5, objModel.getTerminal());
					ps.setString(6, objModel.getLote());
					ps.setString(7, objModel.getTarjeta());
					ps.setString(8, objModel.getFechaventa());
					ps.setString(9, objModel.getHora());
					ps.setString(10, objModel.getAutorizacion());
					ps.setDouble(11, changeString(objModel.getConsumo()));
					ps.setString(12, objModel.getTipotrans());
					ps.setDouble(13, changeString(objModel.getImpturismo()));
					ps.setDouble(14, changeString(objModel.getPropina()));
					ps.setDouble(15, changeString(objModel.getComision()));
					ps.setDouble(16, changeString(objModel.getIva()));
					ps.setDouble(17, changeString(objModel.getLiquido()));
					ps.setString(18, objModel.getUsuario());
					ps.execute();
				} catch (Exception e) {
					ps.close();
					ps = null;
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
							"Error de counstring  " + e.getMessage());
				}
			}

			if (contador > 0) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
						"Script ejecutado correctamente insert iva detalle: " + contador);
				result = true;
			}
		} catch (Exception e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	/**
	 * Inserta los registros para VISA DETALLE Agregado por Juankrlos - 12/01/2017 -
	 */

	public boolean insertCuentasCredo(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		// boolean isEnd = true;
		try {
			con = obtenerDtsPromo().getConnection();
			CBEstadoCuentasModel objModel = null;
			System.out.println("tama�o de lista: " + list.size());
			Iterator<CBEstadoCuentasModel> it = list.iterator();
			ps = con.prepareStatement(ConsultasSQ.INSERT_CREDO_SQ);
			ps.setFetchSize(1024);
			while (it.hasNext()) {
				objModel = it.next();
				ps.setInt(1, objModel.getCbestadocuentaconfid());
				ps.setInt(2, objModel.getCbestadocuentaarchivosid());
				ps.setString(3, objModel.getFechatransaccion());
				ps.setString(4, objModel.getReferencia());
				ps.setString(5, objModel.getCodigo_lote());
				ps.setString(6, objModel.getDebito());
				ps.setString(7, objModel.getCredito());
				ps.setString(8, objModel.getBalance());
				ps.setString(9, objModel.getDescpago());
				ps.setString(10, objModel.getUsuario());

				ps.addBatch();

			}
			int[] exec = ps.executeBatch();
			if (exec.length > 0) {
				System.out.println("Script ejecutado correctamente insert credomatic");
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return result;
	}

	/**
	 * Inserta los registros para VISA DETALLE Agregado por Juankrlos - 12/01/2017 -
	 */
	// modificado Ovidio Santos 24/04/2016
	public boolean insertCuentasCredoEncabezado(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;

		int contador = 0;
		CBEstadoCuentasModel objModel = null;
		try {

			con = obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(ConsultasSQ.INSERT_CREDO_SQ2);
			System.out.println("tama�o de lista: " + list.size());
			Iterator<CBEstadoCuentasModel> it = list.iterator();

			System.out.println("entra al metodo para insertar encabezado credo " + list.size());
			while (it.hasNext()) {
				objModel = it.next();

				ps.setInt(1, objModel.getCbestadocuentaconfid());
				ps.setInt(2, objModel.getCbestadocuentaarchivosid());
				ps.setString(3, objModel.getAfilicacion());
				ps.setString(4, objModel.getDescripcion());

				ps.setString(5, objModel.getConsumo());
				ps.setString(6, objModel.getComision());
				ps.setString(7, objModel.getIvacomision());
				ps.setString(8, objModel.getRetencion());
				ps.setString(9, objModel.getLiquido());
				ps.setString(10, objModel.getFechatransaccion());
				ps.setString(11, objModel.getCodigo_lote());
				ps.setString(12, objModel.getDebito());
				ps.setString(13, objModel.getBalance());
				ps.setString(14, objModel.getReferencia());
				ps.setString(15, objModel.getTipo());
				ps.setString(16, objModel.getUsuario());
				ps.addBatch();
				contador++;
			}
			ps.executeBatch();
			if (contador > 0) {
				System.out.println("Insert ejecutado correctamente, registros guardados = : " + contador);

				result = true;
			}

		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Inserta los registros para credomatic encabezado metodo solo para Costa Rica
	 * 12/01/2017 -
	 */
	// Insert para la nueva version de carga de archivos credomatic
	/*
	 * public static final String INSERT_CREDO_CR_SQ2 =
	 * "INSERT INTO CB_ESTADO_CUENTA " +
	 * "   (CBESTADOCUENTAID, CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, AFILIACION, DESCRIPCION, CONSUMO, COMISION, IVA, RETENCION, LIQUIDO, FECHA_TRANSACCION, CODIGO_LOTE, "
	 * + "	  DEBITO, BALANCE, REFERENCIA, " +
	 * "	 TIPO, DOCUMENTO, PROPINA, CREDITO, IMPUESTO_TURIS, IVA_COMISION, SWCREATEBY, SWDATECREATED) "
	 * + "	 VALUES " +
	 * "   (CB_ESTADO_CUENTA_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?,to_date(?,'MM/dd/yy'),?,?,?,?,?,?,?,?,?,?,?,sysdate) "
	 * ; // modificado Ovidio Santos 24/04/2016 public boolean
	 * insertCuentasCredoEncabezadoCR(List<CBEstadoCuentasModel> list) { boolean
	 * result = false; Connection con = null; PreparedStatement ps = null; int
	 * contador = 0; CBEstadoCuentasModel objModel = null; try { con =
	 * obtenerDtsPromo().getConnection();
	 * 
	 * ps = con.prepareStatement(INSERT_CREDO_CR_SQ2);
	 * Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
	 * "tama�o de lista: " + list.size()); Iterator<CBEstadoCuentasModel> it =
	 * list.iterator();
	 * Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
	 * "entra al metodo para insertar encabezado credo " + list.size()); while
	 * (it.hasNext()) { objModel = it.next();
	 * 
	 * ps.setInt(1, objModel.getCbestadocuentaconfid()); ps.setInt(2,
	 * objModel.getCbestadocuentaarchivosid()); ps.setString(3,
	 * objModel.getAfilicacion()); ps.setString(4, objModel.getDescripcion());
	 * ps.setString(5, objModel.getConsumo()); ps.setString(6,
	 * objModel.getComision()); ps.setString(7, objModel.getIva()); ps.setString(8,
	 * objModel.getRetencion()); ps.setString(9, objModel.getLiquido());
	 * ps.setString(10, objModel.getFechatransaccion()); ps.setString(11,
	 * objModel.getCodigo_lote()); ps.setString(12, objModel.getDebito());
	 * ps.setString(13, objModel.getBalance()); ps.setString(14,
	 * objModel.getReferencia()); ps.setString(15, objModel.getTipo()); //nuevos
	 * ps.setString(16, objModel.getDocumento()); ps.setString(17,
	 * objModel.getPropina()); ps.setString(18, objModel.getCredito());
	 * ps.setString(19, objModel.getImpturismo()); ps.setString(20,
	 * objModel.getIvacomision()); ps.setString(21, objModel.getUsuario());
	 * ps.addBatch(); contador++; } ps.executeBatch(); if (contador > 0) {
	 * Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
	 * "Insert ejecutado correctamente, registros guardados = : " + contador);
	 * result = true; }
	 * 
	 * } catch (SQLException e) {
	 * Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null,
	 * e); } finally { try { if(ps != null) ps.close(); if(con != null) con.close();
	 * } catch(SQLException e) {
	 * Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null,
	 * e); } } return result; }
	 * 
	 */

	// Insert para la nueva version de carga de archivos credomatic
	public static final String INSERT_CREDO_SV_SQL = "INSERT INTO CB_ESTADO_CUENTA_N "
			+ "  (CB_ESTADOCUENTA_ID, CB_ESTADOCUENTACONF_ID, CB_ESTADOCUENTAARCHIVOS_ID, ARCHIVO, IVA_CCF,COMISION_CCF, NUMERO_CCF ,NETO,COMISION_IVA,"
			+ " RETENCION, COMISION ,MONTO ,  TRS,LIQUIDACION ,CODIGO_AFILIADO , FECHA , SERIE_DCL ,DCL,NOMBRE_AFILIADO,COMP_COM,COMP_IVA,MONTO_NETO,"
			+ " MONTO_RET,COM_IVA,VALIDACION_TOTAL,DIF_AJUSTE,DCL_CONCAT,AJUSTE,COMISION_AJUSTE,IVA_COM_AJUESTE,SWCREATEBY, SWDATECREATED)"
			+ " VALUES"
			+ " (SQ_CB_ESTADO_CUENTA_N.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,to_date(?,'YYYY/MM/DD'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";

	/**
	 * Inserta los registros para credomatic encabezado metodo solo para Costa Rica
	 * 12/01/2017 -
	 */
	// Insert para la nueva version de carga de archivos credomatic
	public static final String INSERT_CREDO_CR_SQ2 = "INSERT INTO CB_ESTADO_CUENTA "
			+ "   (CBESTADOCUENTAID, CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, AFILIACION, DESCRIPCION,FECHA_TRANSACCION, CODIGO_LOTE ,DOCUMENTO,IMPUESTO_TURIS,"
			+ " CONSUMO, COMISION ,IVA_COMISION ,  RETENCION,LIQUIDO ,REFERENCIA , DEBITO , PROPINA, TIPO , SWCREATEBY, SWDATECREATED) "
			+ "	 VALUES "
			+ "   (CB_ESTADO_CUENTA_SQ.NEXTVAL,?,?,?,?,to_date(?,'YYYY/MM/DD'),?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate) ";

	// modificado Ovidio Santos 24/04/2016
	public boolean insertCuentasCredoEncabezadoCR(List<CBEstadoCuentasModel> list) {
		boolean result = false;
		Connection con = null;
		PreparedStatement ps = null;
		int contador = 0;
		CBEstadoCuentasModel objModel = null;
		try {
			con = obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(INSERT_CREDO_CR_SQ2);
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO, "tama�o de lista: " + list.size());
			System.out.println("query insert " + INSERT_CREDO_CR_SQ2);
			Iterator<CBEstadoCuentasModel> it = list.iterator();
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"entra al metodo para insertar encabezado credo " + list.size());
			while (it.hasNext()) {
				objModel = it.next();

				ps.setInt(1, objModel.getCbestadocuentaconfid());
				ps.setInt(2, objModel.getCbestadocuentaarchivosid());
				ps.setString(3, objModel.getArchivo());
				ps.setDouble(4, objModel.getIvaCCF());
				ps.setDouble(5, objModel.getComisionCCF());
				ps.setString(6, objModel.getNumberCCF());
				ps.setDouble(7, objModel.getNeto());
				ps.setDouble(8, objModel.getComisionIva());
				ps.setDouble(9, objModel.getRetencionD());
				ps.setDouble(10, objModel.getComisionD());
				ps.setDouble(11, objModel.getMonto());
				ps.setInt(12, objModel.getTrs());
				ps.setLong(13, objModel.getLiquidacion());
				ps.setLong(14, objModel.getCodigoAfiliado());
				ps.setString(15, (objModel.getFechaSv()));
				ps.setString(16, objModel.getSerieDCL());
				ps.setLong(17, objModel.getDcl());
				ps.setString(18, objModel.getNombreAfiliado());
				ps.setInt(19, boolToInt(objModel.isCompCom()));
				ps.setInt(20, boolToInt(objModel.isCompIva()));
				ps.setDouble(21, objModel.getMontoNeto());
				ps.setDouble(22, objModel.getMontoRet());
				ps.setDouble(23, objModel.getComIva());
				ps.setDouble(24, objModel.getValidacionTotal());
				ps.setDouble(25, objModel.getDifAjuste());
				ps.setString(26, objModel.getDclConcat());
				ps.setDouble(27, objModel.getAjuste());
				ps.setDouble(28, objModel.getComisionAjuste());
				ps.setDouble(29, objModel.getIvaComAjuste());
				ps.setString(30, objModel.getUsuario());
//				ps.setInt(1, objModel.getCbestadocuentaconfid());
//				ps.setInt(2, objModel.getCbestadocuentaarchivosid());			
//				ps.setString(3, objModel.getAfilicacion());
//				ps.setString(4, objModel.getDescripcion());
//				ps.setString(5, objModel.getFechatransaccion());
//				//ps.setString(6, objModel.getFechavalor());
//				ps.setString(6, objModel.getCodigo_lote());
//				ps.setString(7, objModel.getDocumento());
//				ps.setString(8, objModel.getImpuestoturis());
//				ps.setString(9, objModel.getConsumo());				
//				ps.setString(10, objModel.getComision());
//				ps.setString(11, objModel.getIvacomision());
//				ps.setString(12, objModel.getRetencion());
//				
//				ps.setString(13, objModel.getLiquido());
//				ps.setString(14, objModel.getReferencia());
//			
//				ps.setString(15, objModel.getDebito());				
//				//nuevos
//				ps.setString(16, objModel.getPropina());
//				ps.setString(17, objModel.getTipo());	
//				
//				ps.setString(18, objModel.getUsuario());
				ps.addBatch();
				contador++;
			}
			ps.executeBatch();
			if (contador > 0) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
						"Insert ejecutado correctamente, registros guardados = : " + contador);
				result = true;
			}

		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public static int boolToInt(Boolean b) {
		return b ? 1 : 0;
	}

//metodo solo para GT
	// Insert para la nueva version de carga de archivos credomatic
	public static final String INSERT_CREDO_SQGT = "INSERT INTO CB_ESTADO_CUENTA "
			+ "   (CBESTADOCUENTAID,CBESTADOCUENTACONFID,CBESTADOCUENTAARCHIVOSID,FECHA_TRANSACCION,AFILIACION,TIPO, "
			+ "	   REFERENCIA,CODIGO_LOTE,CONSUMO,IMPUESTO_TURIS,PROPINA,IVA,COMISION,IVA_COMISION,LIQUIDO,RETENCION, "
			+ "	   DOCUMENTO,DESCRIPCION,DEBITO,CREDITO,BALANCE,SWCREATEBY,SWDATECREATED) " + "	 VALUES "
			+ "   (CB_ESTADO_CUENTA_SQ.NEXTVAL, ?, ?, to_date(?,'yyyymmdd'), ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?, sysdate) ";

	public boolean insertCuentasCredoEncabezadoGT(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			CBEstadoCuentasModel objModel = null;
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO, "Tama�o de lista: " + list.size());
			Iterator<CBEstadoCuentasModel> it = list.iterator();

			int contador = 0;
			while (it.hasNext()) {
				objModel = it.next();
				try {
					contador++;
					ps = con.prepareStatement(INSERT_CREDO_SQGT);
					ps.setInt(1, objModel.getCbestadocuentaconfid());
					ps.setInt(2, objModel.getCbestadocuentaarchivosid());
					ps.setString(3, objModel.getFechatransaccion());
					ps.setString(4, objModel.getAfilicacion());
					ps.setString(5, objModel.getTipo());
					ps.setString(6, objModel.getReferencia());
					ps.setString(7, objModel.getCodigo_lote());
					ps.setString(8, objModel.getConsumo());
					ps.setString(9, objModel.getImpuestoturis());
					ps.setString(10, objModel.getPropina());
					ps.setString(11, objModel.getIva());
					ps.setString(12, objModel.getComision());
					ps.setString(13, objModel.getIvacomision());
					ps.setString(14, objModel.getLiquido());
					ps.setString(15, objModel.getRetencion());
					ps.setString(16, objModel.getDocumento());
					ps.setString(17, objModel.getDescpago());
					ps.setString(18, objModel.getDebito());
					ps.setString(19, objModel.getCredito());
					ps.setString(20, objModel.getBalance());
					ps.setString(21, objModel.getUsuario());

					ps.execute();
				} catch (Exception e) {
					ps.close();
					ps = null;
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
							"Error de counstring  " + e.getMessage());
				}
			}

			if (contador > 0) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
						"Script ejecutado correctamente insert credomatic " + contador);
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	/**
	 * Inserta los registros para CREDOMATIC DETALLE Agregado por Juankrlos -
	 * 12/01/2017 -
	 */
	// modificado Ovidio Santos 24/04/2016
	public boolean insertCuentasCredoDet(List<CBEstadoCuentasModel> list) {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		int contador = 0;
		CBEstadoCuentasModel objModel = null;

		System.out.println("entra al metodo para insertar detalle credo " + list.size());
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.INSERT_CREDO_DET_SQ);
			System.out.println("tama�o de lista: " + list.size());
			Iterator<CBEstadoCuentasModel> it = list.iterator();

			while (it.hasNext()) {
				objModel = it.next();

				ps.setInt(1, objModel.getCbestadocuentaconfid());
				ps.setInt(2, objModel.getCbestadocuentaarchivosid());
				ps.setString(3, objModel.getTarjeta());
				ps.setString(4, objModel.getTerminal());
				ps.setString(5, objModel.getLote());
				ps.setString(6, objModel.getConsumo());
				ps.setString(7, objModel.getPropina());

				ps.setString(8, objModel.getComision());
				ps.setString(9, objModel.getLiquido());
				ps.setString(10, objModel.getAutorizacion());
				ps.setString(11, objModel.getFechaventa());
				ps.setString(12, objModel.getFechacierre());
				ps.setString(13, objModel.getDescripcion());
				ps.setString(14, objModel.getAfilicacion());
				ps.setString(15, objModel.getTipotrans());
				ps.setString(16, objModel.getUsuario());
				ps.addBatch();
				contador++;
			}
			ps.executeBatch();
			if (contador > 0) {
				System.out.println("Insert ejecutado correctamente, registros guardados = : " + contador);

				result = true;

			}

		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return result;
	}

	/**
	 * metodo solo para GT
	 */

	/**
	 * Inserta los registros para CREDOMATIC DETALLE Agregado por Juankrlos -
	 * 12/01/2017 -
	 */

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentasVisaDet()
	public static final String INSERT_CREDO_DET_GT_SQ = "INSERT INTO CB_ESTADO_CUENTA_DET(CBESTADOCUENTADETID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, AFILIACION, F_CIERRE,TERMINAL, LOTE, TARJETA, FECHA_VENTA,"
			+ " HORA, AUTORIZACION, CONSUMO, TIPO_TRANS, IMP_TURISMO, PROPINA, COMISION, IVA, LIQUIDO, SWCREATEBY, SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_DET_SQ.NEXTVAL, ?,?,?,to_date(?,'yyyymmdd'),?,?,?,to_date(?,'yyyy-MM-dd'),"
			+ " ?,?,?,?,?,?,?,?,?,?,SYSDATE) ";

	public boolean insertCuentasCredoDetGT(List<CBEstadoCuentasModel> list) {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {

			con = obtenerDtsPromo().getConnection();
			CBEstadoCuentasModel objModel = null;
			System.out.println("tama�o de lista: " + list.size());
			if (list.size() == 0) {
				return true;
			}
			Iterator<CBEstadoCuentasModel> it = list.iterator();

			int contador = 0;
			while (it.hasNext()) {
				objModel = it.next();
				try {
					contador++;
					ps = con.prepareStatement(INSERT_CREDO_DET_GT_SQ);

					ps.setInt(1, objModel.getCbestadocuentaconfid());
					ps.setInt(2, objModel.getCbestadocuentaarchivosid());
					ps.setString(3, objModel.getAfilicacion());
					ps.setString(4, objModel.getFechacierre());
					ps.setString(5, objModel.getTerminal());
					ps.setString(6, objModel.getLote());
					ps.setString(7, objModel.getTarjeta());
					ps.setString(8, objModel.getFechaventa());
					ps.setString(9, objModel.getHora());
					ps.setString(10, objModel.getAutorizacion());
					ps.setString(11, objModel.getConsumo());
					ps.setString(12, objModel.getTipotrans());
					ps.setString(13, objModel.getImpuestoturis());
					ps.setString(14, objModel.getPropina());
					ps.setString(15, objModel.getComision());
					ps.setString(16, objModel.getIva());
					ps.setString(17, objModel.getLiquido());
					ps.setString(18, objModel.getUsuario());

					ps.execute();
				} catch (Exception e) {
					ps.close();
					ps = null;
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
							"Error de counstring  " + e.getMessage());
				}
			}

			if (contador > 0) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
						"Script ejecutado correctamente insert credo detalle " + contador);
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} catch (Exception e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	/**
	 * Insertamos los archivos que se carguen a las tablas
	 * 
	 * @param objModel : objeto que trae todos los parametros a insertar en la tabla
	 */

	public int insertTableArchivos(CBEstadoCuentasModel objModel) {
		int result = 0;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(ConsultasSQ.INSERT_TABLE_ARCHIVOS);
			ps.setInt(1, objModel.getCbestadocuentaarchivosid());
			ps.setInt(2, objModel.getCbestadocuentaconfid());
			ps.setString(3, objModel.getNombrearchivos());
			ps.setString(4, objModel.getDescarchivos());
			ps.setString(5, objModel.getUsuario());
			result = ps.executeUpdate();
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return result;
	}

	/**
	 * Inserta los registros para Extracto Bancario Agregado por Juankrlos -
	 * 12/01/2017 -
	 */
	// modificado Ovidio santos 25/04/2017
	public boolean insertCuentaSociedad(List<CBEstadoCuentasModel> list) {

		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		int contador = 0;
		CBEstadoCuentasModel objModel = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.INSERT_SOCIEDAD_SQ);
			Iterator<CBEstadoCuentasModel> it = list.iterator();
			System.out.println("entra al metodo para insertar sociedad " + list.size());
			System.out.println("INSERT SOCIEDAD = " + ConsultasSQ.INSERT_SOCIEDAD_SQ);
			while (it.hasNext()) {
				objModel = it.next();
				System.out.println("insertando" + objModel.getNumdocumento());
				ps.setInt(1, objModel.getCbestadocuentaconfid());
				ps.setInt(2, objModel.getCbestadocuentaarchivosid());
				ps.setString(3, objModel.getCuenta());
				ps.setString(4, objModel.getReferencia());
				ps.setString(5, objModel.getAsignacion());

				ps.setString(6, objModel.getClase());
				ps.setString(7, objModel.getNumdocumento());

				ps.setString(8, changeFormtatNumber(objModel.getFechavalor()));
				ps.setString(9, changeFormtatNumber(objModel.getFechacontab()));

				ps.setString(10, objModel.getTexto());

				ps.setString(11, objModel.getMon());

				ps.setString(12, changeFormtatNumber(objModel.getImportemd()));

				ps.setString(13, changeFormtatNumber(objModel.getImporteml()));

				ps.setString(14, objModel.getDocucomp());
				ps.setString(15, objModel.getCtacp());

				// ps.setString(16, objModel.getIdentificador());
				String identificador = objModel.getIdentificador();
				if (!identificador.equals("") && identificador != null) {
					ps.setInt(16, Integer.parseInt(identificador));
				} else {
					ps.setInt(16, 0);
				}

				ps.setString(17, objModel.getTextoCabDoc());

				ps.setString(18, objModel.getAnulacion());
				ps.setString(19, objModel.getCt());

				ps.setString(20, changeFormtatNumber(objModel.getEjercicioMes()));
				ps.setString(21, changeFormtatNumber(objModel.getFechaDoc()));
				ps.setString(22, objModel.getLibMayor());
				ps.setString(23, objModel.getPeriodo());
				ps.setString(24, objModel.getImporteMl3());
				ps.setString(25, objModel.getUsuarioSociedad());
				/*
				 * nuevos campos agregados Ovidio Santos
				 */
				ps.setString(26, objModel.getMl());
				ps.setString(27, objModel.getMl3());
				ps.setString(28, objModel.getCompens());
				ps.setString(29, objModel.getAfun());
				ps.setString(30, objModel.getArea_funcional());

				ps.setString(31, objModel.getCe_coste());
				ps.setString(32, objModel.getCodigo_transaccion());

				ps.setString(33, objModel.getClv_ref_cabecera());
				ps.setString(34, objModel.getBco_prp());

				ps.setString(35, objModel.getOrden());

				ps.setString(36, changeFormtatNumber(objModel.getTp_camb_ef()));

				ps.setString(37, objModel.getTpbc());
				// fin
				ps.setString(38, objModel.getUsuario());
				ps.addBatch();
				contador++;
			}
			ps.executeBatch();
			if (contador > 0) {
				System.out.println("Insert ejecutado correctamente, registros guardados = : " + contador);

				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Inserta los registros para Sistema Comercial Agregado - 12/01/2017 -
	 * modificado OVIDIO SANTOS
	 */
	public boolean insertCuentaComercial(List<CBPagosModel> list) {

		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		int exitosas = 0;

		CBPagosModel objModel = null;

		try {
			con = obtenerDtsPromo().getConnection();

			ps = con.prepareStatement(ConsultasSQ.INSERT_SISTEMACOMERCIAL_SQ);
			Iterator<CBPagosModel> it = list.iterator();

			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"INSERT SISTEMA COMERCIAL = " + ConsultasSQ.INSERT_SISTEMACOMERCIAL_SQ);
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
					"Cantidad de registros antes del insert: " + list.size());

			while (it.hasNext()) {
				objModel = it.next();

				if ("0".equals(objModel.getFecEfectividad()) || "0".equals(objModel.getFecha())) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO, "datos ingresados para fecha "
							+ objModel.getFecEfectividad() + "   " + objModel.getFecha());
				}

				ps.setString(1, objModel.getFecEfectividad());
				ps.setString(2, objModel.getNumSecuenci());
				String cod_cliente = objModel.getCodCliente();

				if (cod_cliente != null && !"".equals(cod_cliente)) {
					ps.setInt(3, Integer.parseInt(cod_cliente));
				} else {
					ps.setInt(3, 0);
				}
				ps.setString(4, objModel.getTelefono());
				String imp_pago = objModel.getImpPago();
				if (imp_pago != null && !"".equals(imp_pago)) {
					ps.setDouble(5, Double.parseDouble(imp_pago));
				} else {
					ps.setDouble(5, 0);
				}
				ps.setString(6, objModel.getCodCaja());
				ps.setString(7, objModel.getDesPago());
				ps.setString(8, objModel.getTipo());
				ps.setString(9, objModel.getEstadoConciliado());

				String num_conciliacion = objModel.getNumConciliacion();
				if (num_conciliacion != null && !"".equals(num_conciliacion)) {
					ps.setInt(10, Integer.parseInt(num_conciliacion));
				} else {
					ps.setInt(10, 0);
				}
				ps.setString(11, objModel.getFecha());

				String cbbancoagenciaconfrontaid = objModel.getCbBancoAgenciaConfrontaId();
				if (cbbancoagenciaconfrontaid != null && !"".equals(cbbancoagenciaconfrontaid)) {
					ps.setDouble(12, Double.parseDouble(cbbancoagenciaconfrontaid));
				} else {
					ps.setInt(12, 0);
				}

				// ps.setString(13, objModel.getFechaTransaccional());
				String fec_transaccional = objModel.getFechaTransaccional();
				if (fec_transaccional != null && !"".equals(fec_transaccional)) {
					ps.setInt(13, Integer.parseInt(fec_transaccional));
				} else {
					ps.setInt(13, 0);
				}

				ps.setString(14, objModel.getNonCliente());
				ps.setString(15, objModel.getCodCliclo());
				ps.setString(16, objModel.getTransaccion());
				ps.setString(17, objModel.getTipoTransaccion());

				String tip_movcaja = objModel.getTipoMovCaja();
				if (tip_movcaja == null || "".equals(tip_movcaja)) {
					ps.setInt(18, 0);
				} else {
					ps.setInt(18, Integer.parseInt(tip_movcaja));
				}
				String tip_valor = objModel.getTipoValor();

				if (tip_valor == null || "".equals(tip_valor)) {
					ps.setInt(19, 0);

				} else {
					ps.setInt(19, Integer.parseInt(tip_valor));
				}
				ps.setString(20, objModel.getNomUsuarora());
				ps.setString(21, objModel.getCodBanco());
				ps.setString(22, objModel.getCodOficina());
				ps.setString(23, objModel.getCodSegmento());
				ps.setString(24, objModel.getDesSegmento());
				ps.setString(25, objModel.getCodMoneda());

				ps.setString(26, objModel.getCreadoPor());

				// Agreado por Omar Gomez
				ps.setInt(27, objModel.getCbestadocuentaarchivosid());
				ps.setInt(28, objModel.getCbestadocuentaconfid());

				ps.addBatch();

				exitosas++;
			}

			ps.executeBatch();
			if (exitosas > 0) {
				System.out.println("Insert ejecutado correctamente, registros guardados = : " + exitosas);

				result = true;
			}

		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Ejecutamos el Store proncedure para extracto bancario
	 */
	public boolean ejecutaSPExtracto() {
		boolean result = false;
		CallableStatement cst = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cst = con.prepareCall("{CALL CB_CONCILIACION_CAJAS_PKG.cb_depositos_cuenta_sp()}");
			int exec = cst.executeUpdate();
			if (exec > 0) {
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cst != null)
				try {
					cst.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Ejecutamos el Store proncedure para estados de cuentas BANCO NACIONAL
	 */
	public boolean ejecutaSPBancoNacional() {
		boolean result = false;
		Connection con = null;
		CallableStatement cStmt = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			cStmt = con.prepareCall("{CALL CB_CONCILIACION_CAJAS_PKG.CB_CONCILIA_VISA_SP()}");
			if (cStmt.executeUpdate() > 0) {
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cStmt != null)
					cStmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	/**
	 * Ejecutamos el Store proncedure para estados de cuentas Visa
	 */
	public boolean ejecutaSPVisa() {
		boolean result = false;
		CallableStatement cst = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cst = con.prepareCall("{CALL CB_CONCILIACION_CAJAS_PKG.CB_CONCILIA_VISA_SP()}");
			int exec = cst.executeUpdate();
			if (exec > 0) {
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cst != null)
				try {
					cst.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Ejecutamos el Store proncedure para estado de cuentas credomatic luego de
	 * hacer la carga
	 * 
	 * @return result
	 */
	public boolean ejecutaSPCredomatic() {
		boolean result = false;
		CallableStatement cst = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cst = con.prepareCall("{CALL CB_CONCILIACION_CAJAS_PKG.CB_CONCILIA_CRED_SP()}");
			int exec = cst.executeUpdate();
			if (exec > 0) {
				result = true;
			}
		} catch (SQLException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (cst != null)
				try {
					cst.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}

	/**
	 * Se encarga de cambiar una cadena con comas a tipo double sin comas 11,000.00-
	 * 
	 * @param cadena : numero ingresado como tipo String
	 */
	public double changeString(String cadena) {
		double result = 0.00;
		System.out.println("string: " + cadena);
		try {
			if (cadena != null && !"".equals(cadena.trim())) {
				result = Double.parseDouble(cadena.replace(",", ""));
			}
		} catch (NumberFormatException e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		}
		return result;
	}

	/**
	 * Las cadenas de texto con caracteres especiales se validan que sean numeros,
	 * se eliminan estos caracteres
	 * 
	 * @return result : se retorna la cadena sin los caracteres
	 */
	public String changeFormtatNumber(String cadena) {
		String result = "";
		try {
			String valida = cadena.substring((cadena.length() - 1), cadena.length());
			if (valida.equals("-")) {
				result = cadena.trim().replaceAll("[,|-]", "");
			} else {
				result = cadena.trim().replace(",", "");
			}
		} catch (Exception e) {
			Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.SEVERE, null, e);
		}
		return result;
	}

}
