package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.CBDepositosRecController;
import com.terium.siccam.model.CBAsignaImpuestosModel;
import com.terium.siccam.model.CBBancoAgenciaAfiliacionesModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.ConsultasSQ;

@SuppressWarnings("serial")
public class CBAsignaImpuestosTiendasPropiasDAO extends ControladorBase {
	private static Logger log = Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class);

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

			cmd = con.prepareStatement(ConsultasSQ.INSERT_IMPUESTOS_TIENDAS_PROPIAS_SQ);
			log.debug( "el query insert " + ConsultasSQ.INSERT_IMPUESTOS_TIENDAS_PROPIAS_SQ);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"el query insert " + ConsultasSQ.INSERT_IMPUESTOS_TIENDAS_PROPIAS_SQ);
			log.debug( "PARAM tipologias" + objModel.getTipologias() );
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"PARAM tipologias" + objModel.getTipologias() );
			
			cmd.setInt(1, objModel.getBancoagenciaafiliacionesid());
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

		} catch (Exception e) {
			log.error("insertImpuestos() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("insertImpuestos() - Error ", e);
				//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
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
			String query = ConsultasSQ.CONSULTA_IMPUESTOS_TIENDAS_PROPIAS_SQ;
			String where = " ";

			if (objModel.getBancoagenciaafiliacionesid() > 0) {

				where += "AND A.cbbancoagenciaafiliacionesid = " + objModel.getBancoagenciaafiliacionesid();

			}
			

			if (objModel.getImpuestoid() > 0) {

				where += " AND A.CBMODULOCONCILIACIONCONFID = " + objModel.getImpuestoid();

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
			log.debug( "consulta " + query + where);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.INFO, "consulta " + query + where);

			cmd = con.createStatement();
			where = where + " order by A.cbcomisionesconfiguracionid asc ";
			rs = cmd.executeQuery(query + where);
			while (rs.next()) {
				objModel = new CBAsignaImpuestosModel();
				objModel.setCbcomisionesconfrontaid(rs.getInt(1));
				objModel.setBancoagenciaafiliacionesid(rs.getInt(2));

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
			log.error("obtenerImpuestos() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() - Error ", e);
					//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() - Error ", e);
					//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() - Error ", e);
					//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;

	}

	public double changeString(String cadena) {
		double result = 0.00;
		log.debug( "string: " + cadena);
		//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.INFO, "string: " + cadena);
		try {
			if (cadena != null && !"".equals(cadena)) {
				result = Double.parseDouble(cadena.replace(",", ""));
			}
		} catch (NumberFormatException e) {
			log.error("changeString() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
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
			
			ps = con.prepareStatement(ConsultasSQ.DELETE_IMPUESTOS_TIENDAS_PROPIAS_SQ);

			ps.setInt(1, objBean);
			if (ps.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("eliminarImpuestos() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("eliminarImpuestos() - Error ", e);
				//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}
	
	
		
	
			/*
			ps.setInt(1, objDepositos.getCbbancoagenciaafiliacionesid());
			System.out.println("afiliaciondao " + objModel.getBancoagenciaafiliacionesid2());
			ps.setInt(2, objModel.getImpuestoid());
			ps.setInt(3, objModel.getTipo());

			ps.setInt(4, objModel.getMedioPago());

			ps.setInt(5, objModel.getTipologias());
			
			ps.setInt(6, objModel.getFormaPago());
			
			ps.setString(7, objModel.getValor());
			ps.setInt(8, 2);
			ps.setString(9, objModel.getCreadoPor());
			
			
			ps.addBatch();
			contador++;
		}
			}
			ps.executeBatch();
	*/
	public boolean eliminarImpuestosmasivo(List<CBBancoAgenciaAfiliacionesModel> list) {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		int contador = 0;
		CBBancoAgenciaAfiliacionesModel objModel = null;
		try {
			
			
			con = obtenerDtsPromo().getConnection();
			Iterator<CBBancoAgenciaAfiliacionesModel> it = list.iterator();
			ps = con.prepareStatement(ConsultasSQ.DELETE_IMPUESTOS_TIENDAS_PROPIAS_TIENDAS_SQ);
			log.debug( "elimina:" + ConsultasSQ.DELETE_IMPUESTOS_TIENDAS_PROPIAS_TIENDAS_SQ );

			ps.setFetchSize(1024);
			
			while (it.hasNext()) {
				objModel = it.next();
				
			ps.setInt(1, objModel.getCbbancoagenciaafiliacionesid());
			ps.addBatch();
			contador++;
			}
			ps.executeBatch();
			System.out.println("eliminar fuera while:" + ConsultasSQ.DELETE_IMPUESTOS_TIENDAS_PROPIAS_TIENDAS_SQ );
			log.debug( "contador:" + contador);
			
			if (contador > 0) {
				result = true;
			}
			
		} catch (Exception e) {
			log.error("eliminarImpuestosmasivo() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("eliminarImpuestosmasivo() - Error ", e);
				//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
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
			cmd = con.prepareStatement(ConsultasSQ.MODIFICAR_IMPUESTOS_TIENDAS_PROPIAS_SQ);
			log.debug( "update  " + ConsultasSQ.MODIFICAR_IMPUESTOS_SQ);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.INFO,
				//	"update  " + ConsultasSQ.MODIFICAR_IMPUESTOS_SQ);

			cmd.setInt(1, objModel.getImpuestoid());
			cmd.setInt(2, objModel.getTipo());
			cmd.setInt(3, objModel.getMedioPago());
			cmd.setInt(4, objModel.getTipologias());
			cmd.setInt(5, objModel.getFormaPago());
			cmd.setString(6, objModel.getValor());
			cmd.setInt(7, objModel.getComisionUso());

			cmd.setString(8, objModel.getModificadoPor());
			cmd.setInt(9, idseleccionado);
			log.debug( "idseleccionado en el dao" + idseleccionado);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.INFO,
				//	"idseleccionado en el dao" + idseleccionado);

			if (cmd.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("modificaImpuestos() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (con != null)
					con.close();
			} catch (Exception e) {
				log.error("modificaImpuestos() - Error ", e);
				//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
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
					"Select CBTIPOLOGIASPOLIZAID,  (CBTIPOLOGIASPOLIZAID || ' - ' || NOMBRE) , DESCRIPCION, CREADO_POR, FECHA_CREACION, TIPO, PIDE_ENTIDAD from cb_tipologias_poliza order by CBTIPOLOGIASPOLIZAID asc");
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
			log.error("obtenerTipologias() - Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					log.error("obtenerTipologias() - Error ", e2);
					//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e1) {
					log.error("obtenerTipologias() - Error ", e1);
					//Logger.getLogger(CBConsultaEstadoCuentasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("obtenerTipologias() - Error ", e);
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
			log.debug( "query tipo impuesto " + QRY_OBTIENE_TIPO_IMPUESTO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"query tipo impuesto " + QRY_OBTIENE_TIPO_IMPUESTO);
			log.debug( "query tipo impuesto " + tipoObjeto);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
				//	"query tipo impuesto " + tipoObjeto);
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
			log.error("obtenerImpuestos() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerImpuestos() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
	/**
	 * Bloque para obtener datos de un tipo de objeto y llenar cualquier combobox
	 */
	private String QRY_OBTIENE_TIPO = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CALCULO_COMISION' AND TIPO_OBJETO = ?";

	public List<CBParametrosGeneralesModel> obtenertipo(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(QRY_OBTIENE_TIPO);
			log.debug( "query tipo impuesto " + QRY_OBTIENE_TIPO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo impuesto " + QRY_OBTIENE_TIPO);
			log.debug( "query tipo impuesto " + tipoObjeto);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo impuesto " + tipoObjeto);
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
			log.error("obtenertipo() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenertipo() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenertipo() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenertipo() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
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
			log.debug( "query tipo impuesto " + QRY_OBTIENE_MEDIO_PAGO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo impuesto " + QRY_OBTIENE_MEDIO_PAGO);
			log.debug( "query tipo impuesto " + tipoObjeto);
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
			log.error("obtenerMedioDePago() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerMedioDePago() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerMedioDePago() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerMedioDePago() - Error ", e);
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
			log.debug( "query tipo impuesto " + QRY_OBTIENE_FORMA_DE_PAGO);
			//Logger.getLogger(CBDepositosRecController.class.getName()).log(Level.INFO,
					//"query tipo impuesto " + QRY_OBTIENE_FORMA_DE_PAGO);
			log.debug( "query tipo impuesto " + tipoObjeto);
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
			log.error("obtenerformaDePago() - Error ", e);
			//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("obtenerformaDePago() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					log.error("obtenerformaDePago() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtenerformaDePago() - Error ", e);
					//Logger.getLogger(CBReportesDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
	
	/*
	
	@SuppressWarnings("unused")
	public int asignarTipologiaMasivaDepositos2(
			List<CBAsignaImpuestosModel> listDepositos) {

		System.out.println("====== ENTRA A ASIGNACION DE TIPOLOGIA POLIZA ======");
		int resultado = 0;
		Statement ps = null;
		String idagencia;
		int contExitosasDep = 0;
		int contador = 0;
		Connection con = null;
		CBAsignaImpuestosModel objDepositos = null;

		try {
			con = obtenerDtsPromo().getConnection();
			if (listDepositos.size() > 0) {

				Iterator<CBAsignaImpuestosModel> it = listDepositos.iterator();
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
			Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return resultado;
	}
*/
	
	// modificado Ovidio santos 25/04/2017
		public int asignarTiendasmasivos(List<CBAsignaImpuestosModel> list,List<CBBancoAgenciaAfiliacionesModel> listafiliacionid) {
			
			int result = 0;
			PreparedStatement ps = null;
			Connection con = null;
			int contador = 0;
			CBAsignaImpuestosModel objModel = null;
			try {
				con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(ConsultasSQ.INSERT_IMPUESTOS_TIENDAS_PROPIAS_TIENDAS_SQ);
				log.debug( "query " +  ConsultasSQ.INSERT_IMPUESTOS_TIENDAS_PROPIAS_TIENDAS_SQ);
				
				if (listafiliacionid.size() > 0) {
					log.debug( "Valor lista = " +  list.size());
				//	Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
						//	"Valor lista = " +  list.size());
					
					for (CBBancoAgenciaAfiliacionesModel objDepositos:listafiliacionid) {
				
				Iterator<CBAsignaImpuestosModel> it = list.iterator();
			
				while (it.hasNext()) {
					objModel = it.next();
					
					ps.setInt(1, objDepositos.getCbbancoagenciaafiliacionesid());
					ps.setInt(2, objModel.getImpuestoid());
					ps.setInt(3, objModel.getTipo());

					ps.setInt(4, objModel.getMedioPago());

					ps.setInt(5, objModel.getTipologias());
					
					ps.setInt(6, objModel.getFormaPago());
					
					ps.setString(7, objModel.getValor());
					ps.setInt(8, 2);
					ps.setString(9, objModel.getCreadoPor());
					
					
					ps.addBatch();
					contador++;
				}
					}
					ps.executeBatch();
				}
				
				if (contador > 0) {
					log.debug( "Insert ejecutado correctamente, registros guardados = : " + contador);
					

					result = contador;
				}
			}catch (SQLException e) {
				log.error("asignarTiendasmasivos() - Error ", e);
				//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if(ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						log.error("asignarTiendasmasivos() - Error ", e);
						//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.error("asignarTiendasmasivos() - Error ", e);
						//Logger.getLogger(CBAsignaImpuestosTiendasPropiasDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}
			return result;
		}

		
		public List<CBBancoAgenciaAfiliacionesModel> consultaafiliaciontiendas(int idagencia) {
			
		 String CONSULTAR_AFILIACIONES = "SELECT CBBANCOAGENCIAAFILIACIONESID, CBCATALOGOAGENCIAID,  "
					+ "DECODE (TIPO,  'BN', 'BANCO NACIONAL',  'CRED', 'CREDOMATIC', 'VISA', 'VISA') TIPO, "
					+ "AFILIACION, ESTADO, SWCREATEBY, SWDATECREATED FROM CB_BANCO_AGENCIA_AFILIACIONES "
					+ "WHERE CBCATALOGOAGENCIAID = ? ORDER BY CBBANCOAGENCIAAFILIACIONESID";
		 CBBancoAgenciaAfiliacionesModel objeBean = null;
			Connection conn = null;
			PreparedStatement cmd = null;
			ResultSet rs = null;
			List<CBBancoAgenciaAfiliacionesModel> list = new ArrayList<CBBancoAgenciaAfiliacionesModel>();
			try {
				conn = ControladorBase.obtenerDtsPromo().getConnection();
				log.debug( "Valor objeto objeBean = " + idagencia);
				//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					//	"Valor objeto objeBean = " + idagencia);
				log.debug( "Consulta afiliaciones = " + CONSULTAR_AFILIACIONES);
				//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
					//	"Consulta afiliaciones = " + CONSULTAR_AFILIACIONES);
				cmd = conn.prepareStatement(CONSULTAR_AFILIACIONES);
				cmd.setInt(1, idagencia);
				rs = cmd.executeQuery();
				while (rs.next()) {
					objeBean = new CBBancoAgenciaAfiliacionesModel();
					objeBean.setCbbancoagenciaafiliacionesid(rs.getInt(1));
					objeBean.setCbcatalogoagenciaid(rs.getInt(2));
					objeBean.setTipo(rs.getString(3));
					objeBean.setAfiliacion(rs.getString(4));
					objeBean.setEstado(rs.getInt(5));
					objeBean.setCreador(rs.getString(6));
					objeBean.setFechaCreacion(rs.getString(7));
					list.add(objeBean);
				}
			} catch (SQLException e) {
				log.error("consultaafiliaciontiendas() - Error ", e);
				//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						log.error("consultaafiliaciontiendas() - Error ", e);
						//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if (cmd != null)
					try {
						cmd.close();
					} catch (SQLException e) {
						log.error("consultaafiliaciontiendas() - Error ", e);
						//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if (conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						log.error("consultaafiliaciontiendas() - Error ", e);
						//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}
			return list;
		}
		
		//consulta todas afiliacion por agrupacion
		public List<CBBancoAgenciaAfiliacionesModel> consultaafiliaciontiendastodasagrupacion(int idcatalogobanco) {
			
			 String CONSULTAR_AFILIACIONES = "SELECT CBBANCOAGENCIAAFILIACIONESID, CBCATALOGOAGENCIAID,  "
						+ "DECODE (TIPO,  'BN', 'BANCO NACIONAL',  'CRED', 'CREDOMATIC', 'VISA', 'VISA') TIPO, "
						+ "AFILIACION, ESTADO, SWCREATEBY, SWDATECREATED FROM CB_BANCO_AGENCIA_AFILIACIONES "
						+ "WHERE CBCATALOGOAGENCIAID in (SELECT a.CBCATALOGOAGENCIAID FROM CB_CATALOGO_AGENCIA a, CB_CATALOGO_BANCO b " + 
						"   WHERE     b.CBCATALOGOBANCOID = a.CBCATALOGOBANCOID AND b.CBCATALOGOBANCOID = ? )";
			 
			 CBBancoAgenciaAfiliacionesModel objeBean = null;
				Connection conn = null;
				PreparedStatement cmd = null;
				ResultSet rs = null;
				List<CBBancoAgenciaAfiliacionesModel> list = new ArrayList<CBBancoAgenciaAfiliacionesModel>();
				try {
					conn = ControladorBase.obtenerDtsPromo().getConnection();
					log.debug( "Valor objeto objeBean = " + idcatalogobanco);
					//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
							//"Valor objeto objeBean = " + idcatalogobanco);
					log.debug( "Consulta afiliaciones = " + CONSULTAR_AFILIACIONES);
					//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.INFO,
							//"Consulta afiliaciones = " + CONSULTAR_AFILIACIONES);
					cmd = conn.prepareStatement(CONSULTAR_AFILIACIONES);
					cmd.setInt(1, idcatalogobanco);
					rs = cmd.executeQuery();
					while (rs.next()) {
						objeBean = new CBBancoAgenciaAfiliacionesModel();
						objeBean.setCbbancoagenciaafiliacionesid(rs.getInt(1));
						objeBean.setCbcatalogoagenciaid(rs.getInt(2));
						objeBean.setTipo(rs.getString(3));
						objeBean.setAfiliacion(rs.getString(4));
						objeBean.setEstado(rs.getInt(5));
						objeBean.setCreador(rs.getString(6));
						objeBean.setFechaCreacion(rs.getString(7));
						list.add(objeBean);
					}
				} catch (SQLException e) {
					log.error("consultaafiliaciontiendastodasagrupacion() - Error ", e);
					//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
				} finally {
					if (rs != null)
						try {
							rs.close();
						} catch (SQLException e) {
							log.error("consultaafiliaciontiendastodasagrupacion() - Error ", e);
							//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					if (cmd != null)
						try {
							cmd.close();
						} catch (SQLException e) {
							log.error("consultaafiliaciontiendastodasagrupacion() - Error ", e);
							//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					if (conn != null)
						try {
							conn.close();
						} catch (SQLException e) {
							log.error("consultaafiliaciontiendastodasagrupacion() - Error ", e);
							//Logger.getLogger(CBBancoAgenciaAfiliacionesDAO.class.getName()).log(Level.SEVERE, null, e);
						}
				}
				return list;
			}
			
		
}
