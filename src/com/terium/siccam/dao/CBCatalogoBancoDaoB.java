/**
 * 
 */
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
//import javax.naming.spi.DirStateFactory.Result;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoBancoModel;

/**
 * @author lab
 * 
 */
@SuppressWarnings("serial")
public class CBCatalogoBancoDaoB extends ControladorBase{
	private static Logger log = Logger.getLogger(CBCatalogoBancoDaoB.class);

	private String CONSULTA_BANCO = "SELECT DISTINCT a.CBCATALOGOBANCOID cbcatalogobancoid, "
			+ "a.NOMBRE nombre, CONTACTO contacto, a.TELEFONO telefono, EXTENSION extension,"
			+ "(SELECT nombre FROM cb_catalogo_opcion b "
			+ "WHERE TIPO = 'ESTADO' AND a.ESTADO = b.valor) estado, TIPO_ENTIDAD tipoEstado, "
			+ "a.CREADO_POR creadoPor, a.FECHA_CREACION fechaCreacion, a.MODIFICADO_POR modificadoPor,"
			+ "a.FECHA_MODIFICACION fechaModificacion FROM CB_CATALOGO_BANCO a, "
			+ "cb_catalogo_agencia b, cb_banco_agencia_confronta c "
			+ "WHERE  a.cbcatalogobancoid = b.cbcatalogobancoid "
			+ "AND b.cbcatalogoagenciaid = c.cbcatalogoagenciaid ";
	
	private String CONSULTA_BANCO2 = 
			"SELECT cbcatalogobancoid,nombre,contacto, telefono, extension extension, " + 
			"                (SELECT nombre " + 
			"                   FROM cb_catalogo_opcion b " + 
			"                  WHERE tipo = 'ESTADO' AND c.estado = b.valor) estado, " + 
			"                tipo_entidad tipoestado, creado_por creadopor, " + 
			"                fecha_creacion fechacreacion, " + 
			"                modificado_por modificadopor, " + 
			"                fecha_modificacion fechamodificacion from cb_catalogo_banco c where 1=1 ";
	
	private String INSERTA_CATALOGO_BANCO = "INSERT "
			+ " INTO cb_catalogo_banco "
			+ "   ( "
			+ "     CBCATALOGOBANCOID, "
			+ "     NOMBRE, "
			+ "     TELEFONO, "
			+ "     CONTACTO, "
			+ "     EXTENSION, "
			+ "     ESTADO,TIPO_ENTIDAD ,  "
			+ "     CREADO_POR, "
			+ "     FECHA_CREACION "
			+ "   ) "
			+ "   VALUES "
			+ "   ( "
			+ "    (SELECT NVL(MAX(CBCATALOGOBANCOID), 0)+1 FROM cb_catalogo_banco "
			+ "     ) " + "     , " + "     ?, " + "     ?, " + "     ?, "
			+ "     ?, " + "     ?, " + "     ?, ?,  " + "     SYSDATE "
			+ "   ) ";
	private String ACTUALIZA_VALORES_CATALGO_BANCO = "UPDATE cb_catalogo_banco "
			+ " SET nombre              = ?, "
			+ "   contacto              = ?, "
			+ "   telefono              = ?, "
			+ "   extension             = ?, "
			+ "   estado                = ?, "
			+ "   tipo_entidad          = ?, "
			+ "   modificado_por        = ?, "
			+ "   fecha_modificacion    = sysdate "
			+ " WHERE CBCATALOGOBANCOID = ? ";
	//agrega ovidio
	private String CONSULTA_BANCOS_MODIFICADO = "SELECT CBCATALOGOBANCOID cbcatalogobancoid,NOMBRE nombre, CONTACTO contacto,"
			+ " TELEFONO telefono, EXTENSION extension, (SELECT nombre FROM cb_catalogo_opcion b WHERE TIPO   ='ESTADO'"
			+ " AND a.ESTADO = b.valor ) estado, TIPO_ENTIDAD tipoEstado, CREADO_POR creadoPor, "
			+ " FECHA_CREACION fechaCreacion, MODIFICADO_POR modificadoPor, FECHA_MODIFICACION fechaModificacion "
			+ " FROM CB_CATALOGO_BANCO a ";

	// consulta
	public List<CBCatalogoBancoModel> obtieneListaBanco(String nombre,
			String telefono, String estado, String tipoEstado) {
		List<CBCatalogoBancoModel> listado = new ArrayList<CBCatalogoBancoModel>();

		// filtros
		log.debug( "QUERY CONSULTAR 1");
		
		
		if (nombre != null && !nombre.equals("")) {
			CONSULTA_BANCO += "and (upper(a.NOMBRE) like('%' || upper(trim('"
					+ nombre + "'))) || '%') ";
		}
		if (telefono != null && !telefono.equals("")) {
			CONSULTA_BANCO += "and a.TELEFONO = '" + nombre + "' ";
		}
		if (estado != null && !estado.equals("")) {
			CONSULTA_BANCO += "and a.ESTADO = '" + estado + "' ";
		}
		if (tipoEstado != null && !tipoEstado.equals("")) {
			CONSULTA_BANCO += "and TIPO_ENTIDAD = '" + tipoEstado + "' ";
		}
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBCatalogoBancoModel> blh = new BeanListHandler<CBCatalogoBancoModel>(
						CBCatalogoBancoModel.class);
				listado = qr.query(con, CONSULTA_BANCO
						+ " order by NOMBRE asc ", blh, new Object[] {});
				log.debug( "query consultar "+ CONSULTA_BANCO);
				
			
		} catch (Exception e) {
			log.error("obtieneListaBanco() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				log.error("obtieneListaBanco() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return listado;
	}
	
	// consulta
		public List<CBCatalogoBancoModel> obtieneListaBancoMantenimiento( CBCatalogoBancoModel objModel) {
			List<CBCatalogoBancoModel> listado = new ArrayList<CBCatalogoBancoModel>();

			log.debug( "QUERY CONSULTAR 2");
			
			// filtros
			if (objModel.getNombre() != null && !objModel.getNombre().equals("")) {
				CONSULTA_BANCO2 += "and (upper(NOMBRE) like('%' || upper(trim('"
						+ objModel.getNombre() + "'))) || '%') ";
			}
			if (objModel.getTelefono() != null && ! objModel.getTelefono().equals("")) {
				CONSULTA_BANCO2 += "and TELEFONO = '" + objModel.getNombre() + "' ";
			}
			if (objModel.getEstado() != null && !objModel.getEstado().equals("")) {
				CONSULTA_BANCO2 += "and ESTADO = '" + objModel.getEstado() + "' ";
			}
			if (objModel.getTipoEstado() != null && !objModel.getTipoEstado().equals("")) {
				CONSULTA_BANCO2 += "and TIPO_ENTIDAD = '" + objModel.getTipoEstado() + "' ";
			}
			Connection con = null;
			try {
				con = obtenerDtsPromo().getConnection();
					QueryRunner qr = new QueryRunner();
					BeanListHandler<CBCatalogoBancoModel> blh = new BeanListHandler<CBCatalogoBancoModel>(
							CBCatalogoBancoModel.class);
					listado = qr.query(con, CONSULTA_BANCO2
							+ " order by CBCATALOGOBANCOID asc ", blh, new Object[] {});
					log.debug( "query consultar "+ CONSULTA_BANCO2);
					
			} catch (Exception e) {
				log.error("obtieneListaBancoMantenimiento() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				try {
					if(con != null)
						con.close();
				} catch (SQLException e) {
					log.error("obtieneListaBancoMantenimiento() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}

			return listado;
		}
	
	/**
	 * JC
	 * */
	public List<CBCatalogoBancoModel> obtieneListaBanco2(String nombre,
			String telefono, String estado, String tipoEstado) {
		List<CBCatalogoBancoModel> listado = new ArrayList<CBCatalogoBancoModel>();

		// filtros
		if (nombre != null && !nombre.equals("")) {
			CONSULTA_BANCOS_MODIFICADO += "and (upper(a.NOMBRE) like('%' || upper(trim('"
					+ nombre + "'))) || '%') ";
		}
		if (telefono != null && !telefono.equals("")) {
			CONSULTA_BANCOS_MODIFICADO += "and a.TELEFONO = '" + nombre + "' ";
		}
		if (estado != null && !estado.equals("")) {
			CONSULTA_BANCOS_MODIFICADO += "and ESTADO = '" + estado + "' ";
		}
		if (tipoEstado != null && !tipoEstado.equals("")) {
			CONSULTA_BANCOS_MODIFICADO += "and TIPO_ENTIDAD = '" + tipoEstado + "' ";
		}
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBCatalogoBancoModel> blh = new BeanListHandler<CBCatalogoBancoModel>(
						CBCatalogoBancoModel.class);
				listado = qr.query(con, CONSULTA_BANCOS_MODIFICADO
						+ " order by NOMBRE asc ", blh, new Object[] {});
		} catch (Exception e) {
			log.error("obtieneListaBanco2() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				log.error("obtieneListaBanco2() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return listado;
	}
	
	//Agregado por CarlosGodinez - para el modulo de reporteria - Qitcorp - 14/03/2017
	private static String OBTIENE_ENTIDAD_SQ = "select cbcatalogobancoid, nombre from cb_catalogo_banco "
			+ "where estado = 1 and tipo_entidad = 'NO FINANCIERA' order by nombre";
	
	public List<CBCatalogoBancoModel> generaConsultaEntidad(){
		List<CBCatalogoBancoModel> list = new ArrayList<CBCatalogoBancoModel>();
		Connection con = null;
		Statement stm = null;
		ResultSet rs = null;
		try {
			con = obtenerDtsPromo().getConnection();
			stm = con.createStatement();
			rs = stm.executeQuery(OBTIENE_ENTIDAD_SQ);
			CBCatalogoBancoModel obj = null;
			while(rs.next()) {
				obj = new CBCatalogoBancoModel();
				obj.setCbcatalogobancoid(String.valueOf(rs.getInt(1)));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		}catch (Exception e) {
			log.error("generaConsultaEntidad() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					log.error("generaConsultaEntidad() - Error ", e1);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(stm != null)
				try {
					stm.close();
				} catch (SQLException e1) {
					log.error("generaConsultaEntidad() - Error ", e1);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				log.error("generaConsultaEntidad() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	// nuevo ingreso
	public int ingresaEntidadBancaria(CBCatalogoBancoModel objModel) {
		int res = 0;
		log.debug( "nombre: " + objModel.getNombre());
		log.debug( "usuario: " + objModel.getUsuario());
		
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				res = qr.update(con, INSERTA_CATALOGO_BANCO,objModel.getNombre(), objModel.getTelefono(), objModel.getContacto(),
						objModel.getExtension(), objModel.getEstado(), objModel.getTipoEstado(), objModel.getUsuario());
				log.debug( "query insertar: " + INSERTA_CATALOGO_BANCO);
				log.debug( "se a insertado el nuevo registo...");
				
		} catch (Exception e) {
			log.error("ingresaEntidadBancaria() - Error ", e);
		//	Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				log.error("ingresaEntidadBancaria() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return res;

	}

	// actualiza estado

	public int actualizaBanco(CBCatalogoBancoModel bancoModel) {
		int res = 0;
		log.debug( "estado antes de actualizar: "
				+ bancoModel.getEstado());
		
		log.debug( "nombre: " + bancoModel.getNombre());
		log.debug( "telefono: " + bancoModel.getTelefono());
		log.debug( "contacto: " + bancoModel.getContacto());
		log.debug( "extencion: " + bancoModel.getExtension());
		log.debug( "tipo: " + bancoModel.getTipoEstado());
		log.debug( "id: " + bancoModel.getCbcatalogobancoid());
		log.debug( "usuario modifico: " + bancoModel.getUsuario());
		log.debug( "estado = "+ bancoModel.getEstado());
		
		
	
		if (bancoModel.getEstado().equals("1".trim()))
			bancoModel.setEstado("1");
		else
			bancoModel.setEstado("0");
		log.debug( "estado: " + bancoModel.getEstado());
		
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();

				res = qr.update(con, ACTUALIZA_VALORES_CATALGO_BANCO,
						bancoModel.getNombre(), bancoModel.getContacto(),
						bancoModel.getTelefono(), bancoModel.getExtension(),
						Integer.parseInt(bancoModel.getEstado()),
						bancoModel.getTipoEstado(), bancoModel.getUsuario(),
						bancoModel.getCbcatalogobancoid());
				log.debug( "query modificar "+ ACTUALIZA_VALORES_CATALGO_BANCO + bancoModel.getEstado());
				
		} catch (Exception e) {
			log.error("actualizaBanco() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				log.error("actualizaBanco() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return res;

	}

	// consulta nombre entidad
	private String CONSULTA_NOMBRE_ENTIDAD = "SELECT count(*) valor  FROM CB_CATALOGO_BANCO WHERE trim(upper(NOMBRE)) = trim(upper(?))";

	public boolean consultaNombre(CBCatalogoBancoModel objModel) {
		boolean respuesta = false;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(CONSULTA_NOMBRE_ENTIDAD);
				log.debug( "query  "+ CONSULTA_NOMBRE_ENTIDAD);
				ps.setString(1, objModel.getNombre());
				rs = ps.executeQuery();
				rs.next();
				if (Integer.valueOf(rs.getString("valor")) > 0) {
					respuesta = true;
				}
		}catch (Exception e) {
			log.error("consultaNombre() - Error ", e);
			//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					log.error("consultaNombre() - Error ", e1);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e1) {
					log.error("consultaNombre() - Error ", e1);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				log.error("consultaNombre() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return respuesta;
	}
	
	// consulta se manda a llamar para llenar lista bancos
		public List<CBCatalogoBancoModel> obtieneListaBancoModificado(String nombre,
				String telefono, String estado, String tipoEstado) {
			List<CBCatalogoBancoModel> listado = new ArrayList<CBCatalogoBancoModel>();

			// filtros
			if (nombre != null && !nombre.equals("")) {
				CONSULTA_BANCO += "and (upper(NOMBRE) like('%' || upper(trim('"
						+ nombre + "'))) || '%') ";
			}
			if (telefono != null && !telefono.equals("")) {
				CONSULTA_BANCO += "and TELEFONO = '" + nombre + "' ";
			}
			if (estado != null && !estado.equals("")) {
				CONSULTA_BANCO += "and ESTADO = '" + estado + "' ";
			}
			if (tipoEstado != null && !tipoEstado.equals("")) {
				CONSULTA_BANCO += "and TIPO_ENTIDAD = '" + tipoEstado + "' ";
			}
			Connection con = null;
			try {
				con = obtenerDtsPromo().getConnection();
					QueryRunner qr = new QueryRunner();
					BeanListHandler<CBCatalogoBancoModel> blh = new BeanListHandler<CBCatalogoBancoModel>(
							CBCatalogoBancoModel.class);
					listado = qr.query(con, CONSULTA_BANCOS_MODIFICADO
							+ " order by NOMBRE asc ", blh, new Object[] {});
			} catch (Exception e) {
				log.error("obtieneListaBancoModificado() - Error ", e);
				//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				try {
					if(con != null)
						con.close();
				} catch (SQLException e) {
					log.error("obtieneListaBancoModificado() - Error ", e);
					//Logger.getLogger(CBCatalogoAgenciaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}

			return listado;
		}
		
		
		// elimian registro agregado Ovidio Santos 18042018
		private String BORRA_AGRUPACION = "delete cb_catalogo_banco where CBCATALOGOBANCOID = ? ";
		public int eliminaRegistro(String idFila) {
			int i = 0;
			Connection con = null;
			try {
				con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				i = qr.update(con, BORRA_AGRUPACION, idFila);
			} catch (Exception e) {
				log.error("eliminaRegistro() - Error ", e);
				//Logger.getLogger(CBCatalogoBancoDaoB.class.getName()).log(Level.SEVERE, null, e);
			} finally {
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {
					log.error("eliminaRegistro() - Error ", e);
				//	Logger.getLogger(CBCatalogoBancoDaoB.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			return i;
		}
		
	
}
