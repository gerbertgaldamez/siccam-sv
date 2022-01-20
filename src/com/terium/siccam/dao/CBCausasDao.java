/**
 * 
 */
package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.zkoss.zul.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCausasModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;

/**
 * @author lab
 * 
 */
@SuppressWarnings("serial")
public class CBCausasDao extends ControladorBase {
	private static Logger log = Logger.getLogger(CBCausasDao.class);

	private String INSERTA_CAUSA = "INSERT " + "INTO cb_causas_conciliacion (    id_causas_conciliacion, "
			+ "    causas, codigo_conciliacion, tipo, sistema, convenio, CBTIPOLOGIASPOLIZAID,  creado_por,   fecha_creacion  )  values  ( "
			+ "    cb_causa_sq.nextval,   ?, ?, ?, ?,?,   ?, ?,    sysdate   ) ";
	//Cambio Gerbert
	private String CONSULTA_CAUSAS = "SELECT id_causas_conciliacion idCausaConciliacion, causas tipoCausa, codigo_conciliacion, DECODE (tipo, 1, 'CONCILIACION VRS CONFRONTA', 2, 'CONCILIACION VRS EXTRACTO'),  DECODE (sistema, 1, 'SCL', 2, 'ALTAMIRA', 3, 'NINGUNO'),convenio  " + 
			"tipo_conciliacion,(SELECT nombre   FROM CB_Tipologias_poliza WHERE  CBTIPOLOGIASPOLIZAID  = c.CBTIPOLOGIASPOLIZAID)  CBTIPOLOGIASPOLIZAID, creado_por creadoPor,TO_CHAR (fecha_creacion, 'dd/MM/yyyy') fechaCreacion,modificado_por modificadoPor,TO_CHAR (fecha_modificacion, 'dd/MM/yyyy') fechaModificacion FROM cb_causas_conciliacion c WHERE 1 = 1 ";
	private String BORRA_CAUSA = "delete cb_causas_conciliacion where id_causas_conciliacion = ? ";
	
	private String ACTUALIZA_CAUSA = "update cb_causas_conciliacion set causas = ?,codigo_conciliacion = ? ,tipo = ?, sistema = ? , convenio =?,  CBTIPOLOGIASPOLIZAID = ?, modificado_por = ?, fecha_modificacion = sysdate where id_causas_conciliacion = ?";

	// inserta
	public int ingresaNuevaCausa(CBCausasModel objModel) {
		int i = 0;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			i = qr.update(con, INSERTA_CAUSA, objModel.getTipoCausa(), objModel.getCodigoTipologia(), objModel.getTipo(), objModel.getSistema(),objModel.getConvenio(),  objModel.getTipologiaasociada(), objModel.getUsuario());
			log.debug( "query insert " + INSERTA_CAUSA + objModel.getTipoCausa());
			log.debug( "el query insertar   " + INSERTA_CAUSA);
			//Logger.getLogger(CBCausasDao.class.getName()).log(Level.INFO,
					//"el query insertar   " + INSERTA_CAUSA);
		} catch (Exception e) {
			log.error("ingresaNuevaCausa() - Error ", e);
			//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("ingresaNuevaCausa() - Error ", e);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return i;
	}

	// consulta llena lisbox

		public List<CBCausasModel> consultaCausas(CBCausasModel objModel) {
			List<CBCausasModel> list = new ArrayList<CBCausasModel>();

			Connection con = null;
			ResultSet rs = null;
			Statement cmd = null;
			try {
				con = obtenerDtsPromo().getConnection();
				String query = CONSULTA_CAUSAS;
				String where = " ";

				if (objModel.getTipoCausa() != null && !objModel.getTipoCausa().equals("")) {
					where += "and (upper(causas) like('%' || upper(trim('" + objModel.getTipoCausa() + "'))) || '%') ";
				}
				if (objModel.getCodigoTipologia() != null && !objModel.getCodigoTipologia().equals("")) {
					where += "and (upper(codigo_conciliacion) like('%' || upper(trim('" + objModel.getCodigoTipologia() + "'))) || '%') ";
				}
				if (objModel.getTipo() != null && !objModel.getTipo().equals("")) {
					where += "and (upper(tipo) like('%' || upper(trim('" + objModel.getTipo() + "'))) || '%') ";
				}
				if (objModel.getSistema() != null && !objModel.getSistema().equals("")) {
					where += "and (upper(sistema) like('%' || upper(trim('" + objModel.getSistema() + "'))) || '%') ";
				}
				
				if (objModel.getConvenio() != null && !objModel.getConvenio().equals("")) {
					where += "and (upper(convenio) like('%' || upper(trim('" + objModel.getConvenio() + "'))) || '%') ";
				}
				
				if (objModel.getTipologiaasociada() != null && !objModel.getTipologiaasociada().equals("")) {
					where += "and (upper(CBTIPOLOGIASPOLIZAID) like('%' || upper(trim('" + objModel.getTipologiaasociada() + "'))) || '%') ";
				}

				log.debug( "consulta " + query + where);
				//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.INFO, "consulta " + query + where);

				cmd = con.createStatement();
				where = where + " order by fecha_creacion asc ";
				rs = cmd.executeQuery(query + where);
				while (rs.next()) {
					objModel = new CBCausasModel();
					objModel.setIdCausaConciliacion(rs.getString(1));
					objModel.setTipoCausa(rs.getString(2));
					objModel.setCodigoTipologia(rs.getString(3));
					objModel.setTipo(rs.getString(4));
					objModel.setSistema(rs.getString(5));
					objModel.setConvenio(rs.getString(6));
					objModel.setTipologiaasociada(rs.getString(7));
					objModel.setCreadoPor(rs.getString(8));
					objModel.setFechaCreacion(rs.getString(9));
					objModel.setModificadoPor(rs.getString(10));
					objModel.setFechaModificacion(rs.getString(11));


					list.add(objModel);
				}
			} catch (Exception e) {
				log.error("consultaCausas() - Error ", e);
				//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				if (rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						log.error("consultaCausas() - Error ", e);
					//	Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if (cmd != null)
					try {
						cmd.close();
					} catch (SQLException e) {
						log.error("consultaCausas() - Error ", e);
						//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if (con != null)
					try {
						con.close();
					} catch (SQLException e) {
						log.error("consultaCausas() - Error ", e);
						//Logger.getLogger(CBDepositosRecDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}
			return list;

		}
	/*
	// consulta
	public List<CBCausasModel> consultaCausas(CBCausasModel objModel) {
		List<CBCausasModel> listado = new ArrayList<CBCausasModel>();

		if (objModel.getTipoCausa() != null && !objModel.getTipoCausa().equals("")) {
			CONSULTA_CAUSAS += "and (upper(causas) like('%' || upper(trim('" + objModel.getTipoCausa() + "'))) || '%') ";
		}
		if (objModel.getCodigoTipologia() != null && !objModel.getCodigoTipologia().equals("")) {
			CONSULTA_CAUSAS += "and (upper(codigo_conciliacion) like('%' || upper(trim('" + objModel.getCodigoTipologia() + "'))) || '%') ";
		}
		if (objModel.getTipo() != null && !objModel.getTipo().equals("")) {
			CONSULTA_CAUSAS += "and (upper(tipo_conciliacion) like('%' || upper(trim('" + objModel.getTipo() + "'))) || '%') ";
		}
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBCausasModel> bhl = new BeanListHandler<CBCausasModel>(CBCausasModel.class);
			listado = qr.query(con, CONSULTA_CAUSAS + " order by fecha_creacion desc", bhl, new Object[] {});
			Logger.getLogger(CBCausasDao.class.getName()).log(Level.INFO,
					"el query consultar  " + CONSULTA_CAUSAS);
			
		} catch (Exception e) {
			Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return listado;
	}
*/
	// elimian registro
	public int eliminaRegistro(String idFila) {
		int i = 0;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			i = qr.update(con, BORRA_CAUSA, idFila);
			log.debug( "el id eliminar  " + idFila);
			//Logger.getLogger(CBCausasDao.class.getName()).log(Level.INFO,
					//"el id eliminar  " + idFila);
		} catch (Exception e) {
			log.error("eliminaRegistro() - Error ", e);
			//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("eliminaRegistro() - Error ", e);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return i;
	}

	// actualiza registro
	public int actualizaRegistroCausa(CBCausasModel objModel) {
		int i = 0;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			try {
			i = qr.update(con, ACTUALIZA_CAUSA, objModel.getTipoCausa(), objModel.getCodigoTipologia(), objModel.getTipo(),objModel.getSistema(), objModel.getConvenio(), objModel.getTipologiaasociada(), objModel.getUsuario(),objModel.getIdCausaConciliacion());
			}catch (SQLException e) {
				
				log.error("actualizaRegistroCausa() - Error ", e);
				///Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
			log.debug( "el actualizar  " + ACTUALIZA_CAUSA);
			//Logger.getLogger(CBCausasDao.class.getName()).log(Level.INFO,
					//"el actualizar  " + ACTUALIZA_CAUSA);
		} catch (Exception e) {
			log.error("actualizaRegistroCausa() - Error ", e);
			//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				log.error("actualizaRegistroCausa() - Error ", e);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return i;
	}
	
////////////
private String QRY_OBTIENE_TIPO_CONCILIACION = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
		+ "WHERE MODULO = 'TIPOLOGIAS_CONCILIACION_TIPO' AND TIPO_OBJETO = ? ";

public List<CBParametrosGeneralesModel> obtenerTipoConciliacion(String tipoObjeto) {
	List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();

	
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(QRY_OBTIENE_TIPO_CONCILIACION);
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
		log.error("obtenerTipoConciliacion() - Error ", e);
	//	Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
	}finally {
		if(rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("obtenerTipoConciliacion() - Error ", e);
				//Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		if(ps != null)
			try {
				ps.close();
			} catch (SQLException e) {
				log.error("obtenerTipoConciliacion() - Error ", e);
				//Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		if(con != null)
			try {
				con.close();
			} catch (SQLException e) {
				log.error("obtenerTipoConciliacion() - Error ", e);
				//Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
	}
	return lista;
}
//////////Cambio Gerbert
private static final String OBTIENE_SISTEMAS_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
		"  FROM cb_modulo_conciliacion_conf " + 
		" WHERE modulo = 'CONCILIACION' and tipo_objeto = 'SISTEMA'";

public List<CBParametrosGeneralesModel> obtenerSistemas() {
            ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();

		try {
			con = obtenerDtsPromo().getConnection();
			//ps = con.prepareStatement("select CBMODULOCONCILIACIONCONFID,  MODULO , TIPO_OBJETO, OBJETO,VALOR_OBJETO1, VALOR_OBJETO2, VALOR_OBJETO3, SACREATEDY, DESCRIPCION, ESTADO from CB_MODULO_CONCILIACION_CONF where modulo = 'CONCILIACION' and tipo_objeto = 'SISTEMA'");
			ps = con.prepareStatement(OBTIENE_SISTEMAS_QRY);
			log.debug("query - " + OBTIENE_SISTEMAS_QRY);
			rs = ps.executeQuery();
			
			CBParametrosGeneralesModel obj;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setCbmoduloconciliacionconfid(rs.getInt(1));
				//obj.setModulo(rs.getString(2));
				//obj.setTipoObjeto(rs.getString(3));
				obj.setObjeto(rs.getString(2));
				obj.setValorObjeto1(rs.getString(3));
				//obj.setValorObjeto2(rs.getString(6));
				//obj.setValorObjeto3(rs.getString(7));
				//obj.setCreador(rs.getString(8));
				//obj.setDescripcion(rs.getString(9));
				//obj.setEstado(rs.getString(10));
				
				
				
				
				
				
				lista.add(obj);
			}
	} catch (Exception e) {
		log.error("obtenerSistemas() - Error ", e);
		//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
	}finally {
		if(rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				log.error("obtenerSistemas() - Error ", e);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
		if(ps != null)
			try {
				ps.close();
			} catch (SQLException e) {
				log.error("obtenerSistemas() - Error ", e);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
		if(con != null)
			try {
				con.close();
			} catch (SQLException e) {
				log.error("obtenerSistemas() - Error ", e);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
			}
	}
	return lista;
}
//Termina Cambio de Gerbert
//////////
//////////Cambio Gerbert
private static final String OBTIENE_CONVENIO_QRY = "SELECT ID_CAUSAS_CONCILIACION,CONVENIO " + 
"  FROM cb_causas_conciliacion " + 
" WHERE TIPO = 1 or TIPO = 2";

public List<CBParametrosGeneralesModel> obtenerConvenio() {
  ResultSet rs = null;
PreparedStatement ps = null;
Connection con = null;
List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();

try {
	con = obtenerDtsPromo().getConnection();
	
	ps = con.prepareStatement(OBTIENE_CONVENIO_QRY);
	log.debug("query - " + OBTIENE_CONVENIO_QRY);
	rs = ps.executeQuery();
	
	CBParametrosGeneralesModel obj;
	while (rs.next()) {
		obj = new CBParametrosGeneralesModel();
		obj.setIdCausaConciliacion(rs.getInt(1));
		
		obj.setConvenio(rs.getString(2));
		
		
		
		
		
		
		
		lista.add(obj);
	}
} catch (Exception e) {
	log.error("obtenerConvenio() - Error ", e);
//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
}finally {
if(rs != null)
	try {
		rs.close();
	} catch (SQLException e) {
		log.error("obtenerConvenio() - Error ", e);
		//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
	}
if(ps != null)
	try {
		ps.close();
	} catch (SQLException e) {
		log.error("obtenerConvenio() - Error ", e);
		//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
	}
if(con != null)
	try {
		con.close();
	} catch (SQLException e) {
		log.error("obtenerConvenio() - Error ", e);
		//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
	}
}
return lista;
}
//Termina Cambio de Gerbert
//////////
public List<CBTipologiasPolizaModel> obtenerTipologias() {
	Connection con = null;
	PreparedStatement cmd = null;
	ResultSet rs = null;
	List<CBTipologiasPolizaModel> list = new ArrayList<CBTipologiasPolizaModel>();
	try {
		con = obtenerDtsPromo().getConnection();
		cmd = con.prepareStatement(
				"Select CBTIPOLOGIASPOLIZAID, (CBTIPOLOGIASPOLIZAID || ' - ' || NOMBRE), DESCRIPCION, CREADO_POR, FECHA_CREACION, TIPO, PIDE_ENTIDAD from cb_tipologias_poliza where TIPO_IMPUESTO = 3 order by CBTIPOLOGIASPOLIZAID asc  ");
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
		//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
	} finally {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e2) {
				log.error("obtenerTipologias() - Error ", e2);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e2);
			}
		if (cmd != null)
			try {
				cmd.close();
			} catch (SQLException e1) {
				log.error("obtenerTipologias() - Error ", e1);
				//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e1);
			}
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			log.error("obtenerTipologias() - Error ", e);
			//Logger.getLogger(CBCausasDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	return list;
}

}
