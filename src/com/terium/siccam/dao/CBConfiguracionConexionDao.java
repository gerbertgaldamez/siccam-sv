/**
 * 
 */
package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBConfiguracionConModel;

/**
 * @author lab
 * 
 */
@SuppressWarnings("serial")
public class CBConfiguracionConexionDao extends ControladorBase{
	private static Logger log = Logger.getLogger(CBConfiguracionConexionDao.class);

	private String CONSULATA_CONF_CONEXION = "SELECT id_conexion_conf idConexionConf, "
			+ "nombre_conexion nombre, "
			+ "ip_conexion ipConexion, "
			+ "usuario usuario, "
			+ "pass pass, "
			+ "creado_por creadoPor, "
			+ "to_char(fecha_creacion, 'dd/MM/yyyy') fechaCreacion, "
			+ "modificado_por modificadoPor, "
			+ "to_char(fecha_modificacion, 'dd/MM/yyyy') fechaModificacion "
			+ "FROM cb_conexion_conf where 1 = 1 ";
	private String INSERTA_NUEVA_CONF_CONEXION = "INSERT INTO cb_conexion_conf "
			+ "( "
			+ "  id_conexion_conf, "
			+ "  ip_conexion, "
			+ "  nombre_conexion, "
			+ "  usuario, "
			+ "  pass, "
			+ "  creado_por, "
			+ "  fecha_creacion "
			+ ") "
			+ "VALUES "
			+ "( "
			+ "  (SELECT NVL(MAX(id_conexion_conf), 0)+1 FROM cb_conexion_conf "
			+ "  ) "
			+ "  , "
			+ "  ?, "
			+ "  ?, "
			+ "  ?, "
			+ "  ?, "
			+ "  ?, "
			+ "  sysdate " + ") ";

	private String ACTUALIZA_CONEXION = "UPDATE cb_conexion_conf "
			+ "SET nombre_conexion    = ?, " + "  ip_conexion          = ?, "
			+ "  usuario              = ?, " + "  pass                 = ?, "
			+ "  modificado_por       = ?, "
			+ "  fecha_modificacion   = sysdate "
			+ "WHERE id_conexion_conf = ? ";

	// consulta listado de conexiones
	public List<CBConfiguracionConModel> obtieneListaConexionex(CBConfiguracionConModel objModel) {
		List<CBConfiguracionConModel> listado = new ArrayList<CBConfiguracionConModel>();

		if (objModel.getNombre() != null && !objModel.getNombre().equals("")) {
			CONSULATA_CONF_CONEXION += "and (upper(nombre_conexion) like('%' || upper(trim('"
					+ objModel.getNombre() + "'))) || '%') ";
		}
		if (objModel.getIpConexion()!= null && !objModel.getIpConexion().equals("")) {
			CONSULATA_CONF_CONEXION += "and (upper(ip_conexion) like('%' || upper(trim('"
					+ objModel.getIpConexion() + "'))) || '%') ";
		}
		if (objModel.getUsuarioConexion() != null && !objModel.getUsuarioConexion().equals("")) {
			CONSULATA_CONF_CONEXION += "and (upper(usuario) like('%' || upper(trim('"
					+ objModel.getUsuarioConexion() + "'))) || '%') ";
		}
		if (objModel.getPass() != null && !objModel.getPass().equals("")) {
			CONSULATA_CONF_CONEXION += "and (upper(pass) like('%' || upper(trim('"
					+ objModel.getPass() + "'))) || '%') ";
		}

		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBConfiguracionConModel> bhl = new BeanListHandler<CBConfiguracionConModel>(
						CBConfiguracionConModel.class);
				listado = qr
						.query(con, CONSULATA_CONF_CONEXION
								+ " order by fecha_creacion desc", bhl,
								new Object[] {});
				log.debug( "\n*** query consulta ***\n" + CONSULATA_CONF_CONEXION);
			//	Logger.getLogger(CBConfiguracionConexionDao.class.getName())
				//.log(Level.INFO, "\n*** query consulta ***\n" + CONSULATA_CONF_CONEXION);
		} catch (Exception e) {
			log.error("obtieneListaConexionex() - Error ", e);
			//Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("obtieneListaConexionex() - Error ", e);
					//Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	// inserta nueva conexion
	public void insertaConexion(CBConfiguracionConModel objModel) {
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				qr.update(con, INSERTA_NUEVA_CONF_CONEXION, objModel.getIpConexion(), objModel.getNombre(),
						objModel.getUsuarioConexion(), objModel.getPass(), objModel.getUsuario());
				log.debug( "\n*** query insert ***\n" + INSERTA_NUEVA_CONF_CONEXION);
			//	Logger.getLogger(CBConfiguracionConexionDao.class.getName())
				//.log(Level.INFO, "\n*** query insert ***\n" + INSERTA_NUEVA_CONF_CONEXION);
		} catch (Exception e) {
			log.error("insertaConexion() - Error ", e);
			//Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("insertaConexion() - Error ", e);
					//Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
				}
		}

	}

	// actualiza conexion
	public void actualizaConexion(CBConfiguracionConModel objModel) {
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();

				QueryRunner qr = new QueryRunner();
				qr.update(con, ACTUALIZA_CONEXION,  objModel.getNombre(), objModel.getIpConexion(),
						objModel.getUsuarioConexion(), objModel.getPass(), objModel.getModificadoPor(), objModel.getIdConexionConf());

				log.debug( "\n*** query actualiza ***\n" + ACTUALIZA_CONEXION);
				//Logger.getLogger(CBConfiguracionConexionDao.class.getName())
				//.log(Level.INFO, "\n*** query actualiza ***\n" + ACTUALIZA_CONEXION);
		}catch (Exception e) {
			log.error("actualizaConexion() - Error ", e);
			//Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("actualizaConexion() - Error ", e);
					//Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	// elimina conexion
	public void elimaConexion(String idConexion) {
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				qr.update(con,
						"delete cb_conexion_conf where id_conexion_conf = ? ",
						idConexion);
		}catch (Exception e) {
			log.error("elimaConexion() - Error ", e);
			//Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					log.error("elimaConexion() - Error ", e);
				//	Logger.getLogger(CBConfiguracionConexionDao.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}
}
