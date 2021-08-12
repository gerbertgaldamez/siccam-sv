package com.terium.siccam.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.zkoss.zhtml.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCausasConciliacion;
import com.terium.siccam.model.CBHistorialAccionModel;
import com.terium.siccam.utils.ConsultasSQ;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.FiltroQuery;
import com.terium.siccam.utils.GeneraFiltroQuery;
import com.terium.siccam.utils.ObtieneCampos;
import com.terium.siccam.utils.Orden;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBHistorialAccionDAO {

	private int totalRegistros;

	/**
	 * This method should insert a new record in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 */
	public int insertar(CBHistorialAccionModel registro) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBHistorialAccionModel.TABLE + " ("
					+ ObtieneCampos.obtieneSQL(CBHistorialAccionModel.class, null, true, null) + ") values " + " ("
					+ ObtieneCampos.obtieneInsert(CBHistorialAccionModel.class) + ")";

			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getFecha(), registro.getTipo(), registro.getEstado(),
					registro.getAccion(), registro.getObservaciones(), registro.getCreadoPor(),
					registro.getModificadoPor(), registro.getFechaCreacion(), registro.getFechaModificacion() };

			ret = qry.update(conn, queInsert, param);

		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return ret;

	}

	/**
	 * This method should update a record within provide filters
	 * 
	 * @return an int which depends on the result of the update
	 */
	public int actualiza(CBHistorialAccionModel registro) {

		String queInsert;
		Connection conn = null;
		int ret = 0;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Update " + CBHistorialAccionModel.TABLE + " set "
					+ CBHistorialAccionModel.FIELD_CBDATATELEFONICAID + " =?, "
					+ CBHistorialAccionModel.FIELD_CBDATABANCOID + " =?, " + CBHistorialAccionModel.FIELD_FECHA
					+ " =?, " + CBHistorialAccionModel.FIELD_TIPO + " =?, " + CBHistorialAccionModel.FIELD_ESTADO
					+ " =?, " + CBHistorialAccionModel.FIELD_ACCION + " =?, "
					+ CBHistorialAccionModel.FIELD_OBSERVACIONES + " =?, " + CBHistorialAccionModel.FIELD_CREADO_POR
					+ " =?, " + CBHistorialAccionModel.FIELD_MODIFICADO_POR + " =?, "
					+ CBHistorialAccionModel.FIELD_FECHA_CREACION + " =?, "
					+ CBHistorialAccionModel.FIELD_FECHA_MODIFICACION + " =?, " + " where  "
					+ CBHistorialAccionModel.PKFIELD_CBHISTORIALACCIONID + " = ? ";

			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getFecha(), registro.getTipo(), registro.getEstado(),
					registro.getAccion(), registro.getObservaciones(), registro.getCreadoPor(),
					registro.getModificadoPor(), registro.getFechaCreacion(), registro.getFechaModificacion(), };

			ret = qry.update(conn, queInsert, param);

		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return ret;

	}

	/**
	 * This method should return a list of objects within given filters
	 * 
	 * @return the list of Objects
	 */
	@SuppressWarnings("unchecked")
	public List<CBHistorialAccionModel> Listado(List<Filtro> filtro, List<Orden> orden) {
		List<CBHistorialAccionModel> ret = null;
		String query;
		Connection conn = null;

		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			query = "select " + ObtieneCampos.obtieneSQL(CBHistorialAccionModel.class, null, true, null) + " from "
					+ CBHistorialAccionModel.TABLE;

			FiltroQuery filtroQuery = null;

			filtroQuery = GeneraFiltroQuery.generaFiltro(null, orden, true);

			String sqlFiltros = Filtro.getStringFiltros(filtro, true);
			QueryRunner qry = new QueryRunner();
			@SuppressWarnings({ "rawtypes" })
			BeanListHandler blh = new BeanListHandler(CBHistorialAccionModel.class);
			if (filtroQuery.getParams() != null) {
				ret = (List<CBHistorialAccionModel>) qry.query(conn, query + sqlFiltros + filtroQuery.getSql(), blh,
						filtroQuery.getParams());
			} else {
				ret = (List<CBHistorialAccionModel>) qry.query(conn, query + sqlFiltros + filtroQuery.getSql(), blh);
			}
			this.totalRegistros = ret.size();
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.INFO, "resultado: " + ret.size());

		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return ret;
	}

	/**
	 * @return the totalRegistros
	 */
	public int getTotalRegistros() {
		return totalRegistros;
	}

	public List<CBHistorialAccionModel> obtenerCBHistorialAcciones(String id) {
		List<CBHistorialAccionModel> lst = new ArrayList<CBHistorialAccionModel>();
		String query = null;
		Statement stmt = null;
		ResultSet rst = null;
		Connection conn = null;
		CBHistorialAccionModel obj = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			query = "select accion,monto,observaciones,creado_por,fecha_creacion,modificado_por,fecha_modificacion, CBHISTORIALACCIONID from CB_HISTORIAL_ACCION where CBCONCILIACIONID = "
					+ id;

			stmt = conn.createStatement();
			System.out.println(query);
			rst = stmt.executeQuery(query);
			while (rst.next()) {
				obj = new CBHistorialAccionModel();
				obj.setAccion(rst.getString(1));
				obj.setMonto(rst.getString(2));
				obj.setObservaciones(rst.getString(3));
				obj.setCreadoPor(rst.getString(4));
				obj.setFechaCreacion(rst.getString(5));
				obj.setModificadoPor(rst.getString(6));
				obj.setFechaModificacion(rst.getString(7));
				obj.setcBHistorialAccionId(rst.getInt(8));
				lst.add(obj);
			}

		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);

		} finally {
			if (rst != null)
				try {
					rst.close();
				} catch (SQLException e) {
					Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lst;
	}

	public boolean insertarReg(CBHistorialAccionModel obj, String idPadre) {
		boolean result = false;
		Statement stmt = null;
		Connection conn = null;
		String qry = "INSERT INTO CB_HISTORIAL_ACCION(CBHISTORIALACCIONID,ACCION,MONTO,OBSERVACIONES,CREADO_POR,FECHA_CREACION,CBCONCILIACIONID, CBCAUSASCONCILIACIONID) VALUES"
				+ "( "+obj.getcBHistorialAccionId()+", '" + obj.getAccion() + "'," + obj.getMonto() + ",'"
				+ obj.getObservaciones() + "','" + obj.getCreadoPor() + "',sysdate," + idPadre + ", "+obj.getCbCausasConciliacionId()+")";
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.INFO, qry);
			stmt = conn.createStatement();
			if (stmt.executeUpdate(qry) > 0)
				result = true;
		} catch (Exception ex) {
			Messagebox.show("Ha ocurrido un error, verifique si los datos ingresados son validos", "ERROR",
					Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;

	}

	public boolean updateReg(CBHistorialAccionModel evt, String idPadre) {
		Connection conn = null;
		Statement stmt = null;
		boolean result = false;
		try {
			// get connection
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			stmt = conn.createStatement();

			String qry = "update CB_HISTORIAL_ACCION set  accion = '" + evt.getAccion() + "'," + " monto = '"
					+ evt.getMonto() + "'," + " observaciones = '" + evt.getObservaciones() + "',"
					+ " modificado_por = '" + evt.getModificadoPor() + "'," + " fecha_modificacion = sysdate "
					+ " WHERE CBHISTORIALACCIONID = '" + evt.getcBHistorialAccionId() + "' and CBCONCILIACIONID = "
					+ idPadre;

			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.INFO, "Query a la BDD === > " + qry);
			if (stmt.executeUpdate(qry) > 0)
				;
			result = true;
			Messagebox.show("Datos Modificados Exitosamente", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.INFO, "Datos Modificados Exitosamente");
		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	public boolean deleteReg(CBHistorialAccionModel evt, String idPadre) {
		Connection conn = null;
		Statement stmt = null;
		boolean result = false;
		try {
			// get connection
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			stmt = conn.createStatement();
			String qry = "delete from CB_HISTORIAL_ACCION  WHERE CBHISTORIALACCIONID = '" + evt.getcBHistorialAccionId()
					+ "' and CBCONCILIACIONID = " + idPadre;
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.INFO, "Query a la BDD === > " + qry);
			if (stmt.executeUpdate(qry) > 0)
				;
			result = true;
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.INFO, "Datos Eliminados Exitosamente");
		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	public List<CBCausasConciliacion> obtieneListadoAcciones(int tipoid) {
		List<CBCausasConciliacion> listado = new ArrayList<CBCausasConciliacion>();
System.out.println("Tipo id es:"+tipoid);
		Connection con = null;
		ResultSet rst = null;
		PreparedStatement ps = null;
		CBCausasConciliacion obj = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			ps = con.prepareStatement(ConsultasSQ.OBTIENE_CAUSAS_CONCILIACION_DETALLADA_SQ);
			Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE,
					ConsultasSQ.OBTIENE_CAUSAS_CONCILIACION_DETALLADA_SQ);
			ps.setInt(1, tipoid);
			
			rst = ps.executeQuery();
			while (rst.next()) {
				obj = new CBCausasConciliacion();

				obj.setId(rst.getString(1));
				obj.setCausas(rst.getString(2));
				obj.setCreadoPor(rst.getString(3));
				obj.setFechaCreacion(rst.getString(4));
				obj.setCodigoconciliacion(rst.getString(5));
				obj.setTipo(rst.getInt(6));
				obj.setSistema(rst.getInt(7));
				
				listado.add(obj);
			}

		} catch (Exception e) {
			Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rst != null)
				try {
					rst.close();
				} catch (SQLException e) {
					Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					if (con != null)
						con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return listado;
	}

	/**
	 * @author juankrlos
	 * @date 21/06/2021
	 */
	public static boolean ejecutaApldesRecargaSP(int cbhistorialaccionid) {
		boolean result = false;
		Connection conn = null;
		CallableStatement cmd = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();

			cmd = conn.prepareCall(ConsultasSQ.CB_APLDES_RECARGA_SP);
			cmd.setInt(1, cbhistorialaccionid);
			
			result = cmd.executeUpdate() > 0;
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.INFO, "Ejecuta => "+ConsultasSQ.CB_APLDES_RECARGA_SP+" => resultado: "+result);
		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (cmd != null)
					cmd.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}
	
	public boolean actualizaHistorialAcciones(CBHistorialAccionModel historial) {
		boolean result = false;	
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			ps = conn.prepareStatement(ConsultasSQ.ACTUALIZA_HISTORIAL_ACCION);
			
			ps.setInt(1, historial.getEstado());
			ps.setString(2, historial.getTipologiaGacId());
			ps.setString(3, historial.getResponseGac());
			ps.setString(4, historial.getUnidadId());
			ps.setString(5, historial.getSolucion());
			ps.setString(6, historial.getTipoCierre());
			ps.setString(7, historial.getNombreCliente());
			ps.setString(8, historial.getRespuestascl());
			ps.setInt(9, historial.getcBHistorialAccionId());
			

			return ps.executeUpdate() > 0;
			
		} catch (SQLException e) {
			Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} catch (Exception e) {
			Logger.getLogger(CBCausasConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		
		return result;
	}
	
	public static int obtieneSecuenciaHistorial() {
		int secuencia = 0;
		
		String query = "SELECT CB_HISTORIAL_ACCION_SQ.NEXTVAL FROM DUAL";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;

		try {
			// get connection
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			stmt = conn.createStatement();
			System.out.println(query);
			rst = stmt.executeQuery(query);
			if (rst.next())
				secuencia = rst.getInt(1);
		} catch (Exception e) {
			Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBHistorialAccionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		
		return secuencia;
	}

}