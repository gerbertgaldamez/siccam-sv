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

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.CBDepositosRecController;
import com.terium.siccam.controller.ConciliacionController;
import com.terium.siccam.model.CBAsignaImpuestosModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.ConsultasSQ;
import com.terium.siccam.utils.Tools;;

@SuppressWarnings("serial")
public class CBAsignaImpuestosDAO extends ControladorBase {
	private static Logger log = Logger.getLogger(CBAsignaImpuestosDAO.class);

	/**
	 * Inserta los registros para mantenimiento impuestos Agregado por Ovidio Santos
	 * - 04/05/2017 -
	 */
	public boolean insertImpuestos(CBAsignaImpuestosModel objModel) {

		boolean result = false;
		PreparedStatement cmd = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();

			cmd = con.prepareStatement(ConsultasSQ.INSERT_IMPUESTOS_SQ);
			log.debug("insertImpuestos()  " + " - el query insert :" + ConsultasSQ.INSERT_IMPUESTOS_SQ);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"el query insert :" + ConsultasSQ.INSERT_IMPUESTOS_SQ);
			log.debug("insertImpuestos()  " + " - PARAM tipologias" + objModel.getTipologias() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"PARAM tipologias" + objModel.getTipologias() );
			log.debug("insertImpuestos()  " + " - PARAM getTipo" + objModel.getTipo());
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
			//		"PARAM getTipo" + objModel.getTipo());
			log.debug("insertImpuestos()  " + " - PARAM getFormaPago" + objModel.getFormaPago() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
			//		"PARAM getFormaPago" + objModel.getFormaPago() );
			log.debug("insertImpuestos()  " + " - PARAM getMedioPago" + objModel.getMedioPago() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"PARAM getMedioPago" + objModel.getMedioPago() );
			log.debug("insertImpuestos()  " + " - PARAM getValor" + objModel.getValor() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
			//		"PARAM getValor" + objModel.getValor() );
			log.debug("insertImpuestos()  " + " - PARAM getComisionUso" + objModel.getComisionUso() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"PARAM getComisionUso" + objModel.getComisionUso() );
			log.debug("insertImpuestos()  " + " - PARAM getImpuestoid" + objModel.getImpuestoid() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"PARAM getImpuestoid" + objModel.getImpuestoid() );
			log.debug("insertImpuestos()  " + " - PARAM getCreadoPor" + objModel.getCreadoPor() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"PARAM getCreadoPor" + objModel.getCreadoPor() );
			
			cmd.setInt(1, objModel.getBancoagenciaconfrontaid());
			cmd.setInt(2, objModel.getImpuestoid());
			cmd.setInt(3, objModel.getTipo());

			cmd.setInt(4, objModel.getMedioPago());

			cmd.setInt(5, objModel.getTipologias());
			
			cmd.setInt(6, objModel.getFormaPago());
			
			cmd.setString(7, objModel.getValor());
			cmd.setInt(8, objModel.getComisionUso());
			cmd.setString(9, objModel.getCreadoPor());

			if (cmd.executeUpdate() > 0) {
				result = true;
			}

		} catch (SQLException e) {
			log.error("insertImpuestos() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
			Tools.ingresaLog(e, "Modulo Asigna impuesto confronta", 
					"Error al ingresar", "CBAsignaImpuestosDAO.java");
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("insertImpuestos() -  Error ", e);
				//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// consulta llena lisbox

	public List<CBAsignaImpuestosModel> obtenerImpuestos(CBAsignaImpuestosModel objModel) {
		List<CBAsignaImpuestosModel> list = new ArrayList<CBAsignaImpuestosModel>();

		Connection con = null;
		ResultSet rs = null;
		Statement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			String query = ConsultasSQ.CONSULTA_IMPUESTOS_SQ;
			String where = " ";

			if (objModel.getBancoagenciaconfrontaid() > 0) {
				where += "AND A.CBBANCOAGENCIACONFRONTAID = " + objModel.getBancoagenciaconfrontaid();
			}

			if (objModel.getImpuestoid() > 0) {
				where += " AND A.cbcomisionesconfiguracionid = " + objModel.getImpuestoid();
			}
			
			if (objModel.getMedioPago() > 0) {
				where += " AND A.MEDIO_PAGO = " + objModel.getMedioPago();
			}

			if (objModel.getTipo() > 0) {
				where += " AND A.TIPO = " + objModel.getTipo();
			}

			if (objModel.getTipologias() > 0) {
				where += " AND A.TIPOLOGIA = " + objModel.getTipologias();
			}

			if (objModel.getFormaPago() > 0) {
				where += " AND A.FORMA_PAGO = " + objModel.getFormaPago();
			}

			if (objModel.getValor() != null && !objModel.getValor().equals("0.0")) {
				where += "AND A.valor = " + objModel.getValor();
			}
			log.debug("obtenerImpuestos()  " + " - consulta " + query + where);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.INFO, "consulta " + query + where);

			cmd = con.createStatement();
			where = where + " order by A.cbcomisionesconfiguracionid asc ";
			rs = cmd.executeQuery(query + where);
			while (rs.next()) {
				objModel = new CBAsignaImpuestosModel();
				objModel.setCbcomisionesconfrontaid(rs.getInt(1));
				objModel.setBancoagenciaconfrontaid(rs.getInt(2));
				objModel.setImpuestoid(rs.getInt(3));
				objModel.setNombreImpuesto(rs.getString(4));
				objModel.setTipo(rs.getInt(5));
				objModel.setNombreTipo(rs.getString(6));
				objModel.setMedioPago(rs.getInt(7));
				objModel.setNombreMedioPago(rs.getString(8));
				objModel.setNombreTipologia(rs.getString(9));
				objModel.setTipologias(rs.getInt(10));
				objModel.setFormaPago(rs.getInt(11));
				objModel.setNombreFormaPago(rs.getString(12));
				objModel.setValor(rs.getString(13));

				list.add(objModel);
			}
		} catch (Exception e) {
			log.error("obtenerImpuestos() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() -  Error ", e);
					//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() -  Error ", e);
					//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() -  Error ", e);
					//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;

	}

	public double changeString(String cadena) {
		double result = 0.00;
		log.debug("changeString()  " + " - string: " + cadena);
		//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.INFO, "string: " + cadena);
		try {
			if (cadena != null && !"".equals(cadena)) {
				result = Double.parseDouble(cadena.replace(",", ""));
			}
		} catch (NumberFormatException e) {
			log.error("changeString() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		}
		return result;
	}

	// metodo eliminar
	public boolean eliminarImpuestos(int objBean) {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.DELETE_IMPUESTOS_SQ);

			ps.setInt(1, objBean);
			if (ps.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("eliminarImpuestos() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("eliminarImpuestos() -  Error ", e);
				//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	// metodo modificar
	public boolean modificaImpuestos(CBAsignaImpuestosModel objModel, int idseleccionado) {
		boolean result = false;

		Connection con = null;
		PreparedStatement cmd = null;
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(ConsultasSQ.MODIFICAR_IMPUESTOS_SQ);
			log.debug("modificaImpuestos()  " + " - update  " + ConsultasSQ.MODIFICAR_IMPUESTOS_SQ);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.INFO,
					//"update  " + ConsultasSQ.MODIFICAR_IMPUESTOS_SQ);

			cmd.setInt(1, objModel.getImpuestoid());
			cmd.setInt(2, objModel.getTipo());
			cmd.setInt(3, objModel.getMedioPago());
			cmd.setInt(4, objModel.getTipologias());
			cmd.setInt(5, objModel.getFormaPago());
			cmd.setString(6, objModel.getValor());
			cmd.setInt(7, objModel.getComisionUso());

			cmd.setString(8, objModel.getModificadoPor());
			cmd.setInt(9, idseleccionado);
			log.debug("modificaImpuestos()  " + " - idseleccionado en el dao" + idseleccionado);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.INFO,
				//	"idseleccionado en el dao" + idseleccionado);

			if (cmd.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("modificaImpuestos() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("modificaImpuestos() -  Error ", e);
				//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	
	public List<CBTipologiasPolizaModel> obtenerTipologias() {
		Connection con = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		List<CBTipologiasPolizaModel> list = new ArrayList<CBTipologiasPolizaModel>();
		try {
			con = obtenerDtsPromo().getConnection();
			cmd = con.prepareStatement(
					"Select CBTIPOLOGIASPOLIZAID,(CBTIPOLOGIASPOLIZAID || ' - ' || NOMBRE), DESCRIPCION, CREADO_POR, FECHA_CREACION, TIPO, PIDE_ENTIDAD from cb_tipologias_poliza where tipo_impuesto = 2 order by CBTIPOLOGIASPOLIZAID asc ");
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
			log.error("obtenerTipologias() -  Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					log.error("obtenerTipologias() -  Error ", e2);
					//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					log.error("obtenerTipologias() -  Error ", e1);
					//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("obtenerTipologias() -  Error ", e);
				//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier combobox
	 */
	private String QRY_OBTIENE_TIPO_IMPUESTO = "SELECT OBJETO, CBMODULOCONCILIACIONCONFID  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CALCULO_COMISION' AND TIPO_OBJETO = ?";

	public List<CBParametrosGeneralesModel> obtenerImpuestos(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(QRY_OBTIENE_TIPO_IMPUESTO);
			log.debug("obtenerImpuestos()  " + " - query tipo impuesto " + QRY_OBTIENE_TIPO_IMPUESTO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo impuesto " + QRY_OBTIENE_TIPO_IMPUESTO);
			log.debug("obtenerImpuestos()  " + " - query tipo impuesto " + tipoObjeto);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo impuesto " + tipoObjeto);
			ps.setString(1, tipoObjeto);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setObjeto(rs.getString(1));
				obj.setCbmoduloconciliacionconfid(rs.getInt(2));
				lista.add(obj);
			}
		} catch (Exception e) {
			log.error("obtenerImpuestos() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier combobox
	 */
	private String QRY_OBTIENE_TIPO = "SELECT OBJETO, VALOR_OBJETO1, DESCRIPCION FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CALCULO_COMISION' AND TIPO_OBJETO = ?";

	public List<CBParametrosGeneralesModel> obtenertipo(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(QRY_OBTIENE_TIPO);
			log.debug("obtenertipo()  " + " - query tipo impuesto " + QRY_OBTIENE_TIPO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"query tipo impuesto " + QRY_OBTIENE_TIPO);
			log.debug("obtenertipo()  " + " - query tipo impuesto " + tipoObjeto);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"query tipo impuesto " + tipoObjeto);
			ps.setString(1, tipoObjeto);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setObjeto(rs.getString(1));
				obj.setValorObjeto1(rs.getString(2));
				obj.setDescripcion(rs.getString(3));
				lista.add(obj);
			}
		} catch (Exception e) {
			log.error("obtenertipo() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {

					log.error("obtenertipo() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenertipo() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenertipo() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
	
	
	// valida si los datos ya fueron insertados en cb_agencias_confronta
		public String obtenerporcentajevalido(String porcentaje, String porcentajetipo) {
			String qryValidarExistencia = "SELECT VALOR_OBJETO3 FROM CB_MODULO_CONCILIACION_CONF "
					+ "WHERE MODULO = 'CALCULO_COMISION' AND VALOR_OBJETO1 = to_char(?)  and objeto = ? ";
			String existe = "";
			Connection con = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				con = ControladorBase.obtenerDtsPromo().getConnection();
				log.debug("obtenerporcentajevalido()  " + " - Valor porcentaje = " + porcentaje);
				//Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
						//"Valor porcentaje = " + porcentaje);
				log.debug("obtenerporcentajevalido()  " + " - Query validar porcentaje valido = " + qryValidarExistencia);
				//Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					//	"Query validar porcentaje valido = " + qryValidarExistencia);
				ps = con.prepareStatement(qryValidarExistencia);
				ps.setString(1, porcentaje);
				ps.setString(2, porcentajetipo);
				rs = ps.executeQuery();
				if (rs.next()) 
					existe = rs.getString(1);				
			} catch (Exception e) {
				log.error("obtenerporcentajevalido() -  Error ", e);
				//Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						log.error("obtenerporcentajevalido() -  Error ", e);
						//Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						log.error("obtenerporcentajevalido() -  Error ", e);
						//Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.error("obtenerporcentajevalido() -  Error ", e);
						//Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}
			return existe;
		}
	
	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier combobox
	 */
	private String QRY_OBTIENE_MEDIO_PAGO = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CALCULO_COMISION' AND TIPO_OBJETO = ?";

	public List<CBParametrosGeneralesModel> obtenerMedioDePago(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(QRY_OBTIENE_MEDIO_PAGO);
			log.debug("obtenerMedioDePago()  " + " - query tipo impuesto " + QRY_OBTIENE_MEDIO_PAGO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"query tipo impuesto " + QRY_OBTIENE_MEDIO_PAGO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo impuesto " + tipoObjeto);
			log.debug("obtenerMedioDePago()  " + " - query tipo impuesto " + tipoObjeto);
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
			log.error("obtenerMedioDePago() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerMedioDePago() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerMedioDePago() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerMedioDePago() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier combobox
	 */
	private String QRY_OBTIENE_FORMA_DE_PAGO = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CALCULO_COMISION' AND TIPO_OBJETO = ?";

	public List<CBParametrosGeneralesModel> obtenerformaDePago(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(QRY_OBTIENE_FORMA_DE_PAGO);
			log.debug("obtenerformaDePago()  " + " - query tipo impuesto " + QRY_OBTIENE_FORMA_DE_PAGO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"query tipo impuesto " + QRY_OBTIENE_FORMA_DE_PAGO);
			log.debug("obtenerformaDePago()  " + " - query tipo impuesto " + tipoObjeto);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"query tipo impuesto " + tipoObjeto);
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
			log.error("obtenerformaDePago() -  Error ", e);
			//Logger.getLogger(CBAsignaImpuestosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerformaDePago() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerformaDePago() -  Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerformaDePago() -  Error ", e);
				//	Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}

}
