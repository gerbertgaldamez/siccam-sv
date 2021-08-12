package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.exception.CustomExcepcion;
import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.FiltroQuery;
import com.terium.siccam.utils.GeneraFiltroQuery;
import com.terium.siccam.utils.ObtieneCampos;
import com.terium.siccam.utils.Orden;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBDataSinProcesarDAO {

	private int totalRegistros;
	private String INSERTA_DATOS_NO_PROCESADOS = "INSERT " + "INTO cb_data_sin_procesar " + "  ( "
			+ "  cbdatasinprocesarid,  nombre_archivo, " + "    data_archivo, " + "causa,    estado, "
			+ "    creado_por, " + "    fecha_creacion, " + "    id_archivos_insertados " + "  ) " + "  VALUES "
			+ "  ( " + " cb_data_sin_procesar_sq.nextval,   ?, " + "    ?, " + " ?,   ?, " + "    ?, " + "    sysdate, "
			+ "    ? " + "  )";

	/**
	 * This method should insert a new record in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 */
	public int insertar(CBDataSinProcesarModel registro) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBDataSinProcesarModel.TABLE + " ("
					+ ObtieneCampos.obtieneSQL(CBDataSinProcesarModel.class, null, true, null) + ") values " + " ("
					+ ObtieneCampos.obtieneInsert(CBDataSinProcesarModel.class) + ")";

			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getCBDataSinProcesarId(), registro.getNombreArchivo(),
					registro.getDataArchivo(), registro.getEstado(), registro.getCreadoPor(),
					registro.getModificadoPor(), registro.getFechaCreacion(), registro.getFechaModificacion() };

			ret = qry.update(conn, queInsert, param);

		} catch (Exception e) {
			Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return ret;

	}

	/**
	 * This method should insert a new record in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 */
	public int insertarMasivo(List<CBDataSinProcesarModel> registros) throws SQLException, CustomExcepcion {
		Connection conn = null;
		String queInsert;
		int ret = 0;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBDataSinProcesarModel.TABLE + " ("
					+ ObtieneCampos.obtieneSQL(CBDataSinProcesarModel.class, null, true, null) + ") values " + " ("
					+ ObtieneCampos.obtieneInsert(CBDataSinProcesarModel.class) + ")";
			System.out.println("query data sin procesar: " + queInsert);
			QueryRunner qry = new QueryRunner();
			Iterator<CBDataSinProcesarModel> itData = registros.iterator();
			while (itData.hasNext()) {
				CBDataSinProcesarModel registro = (CBDataSinProcesarModel) itData.next();
				Object[] param = new Object[] { registro.getNombreArchivo(), registro.getDataArchivo(),
						registro.getCausa(), registro.getEstado(), registro.getCreadoPor(),
						registro.getIdCargaMaestro() };

				ret += qry.update(conn, INSERTA_DATOS_NO_PROCESADOS, param);
			}
		} catch (Exception e) {
			System.out.println("error al insertar datos no procesados: " + e.getMessage());
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return ret;

	}

	/**
	 * This method should update a record within provide filters
	 * 
	 * @return an int which depends on the result of the update
	 */
	public int actualiza(CBDataSinProcesarModel registro) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Update " + CBDataSinProcesarModel.TABLE + " set "
					+ CBDataSinProcesarModel.FIELD_NOMBRE_ARCHIVO + " =?, " + CBDataSinProcesarModel.FIELD_DATA_ARCHIVO
					+ " =?, " + CBDataSinProcesarModel.FIELD_ESTADO + " =?, " + CBDataSinProcesarModel.FIELD_CREADO_POR
					+ " =?, " + CBDataSinProcesarModel.FIELD_MODIFICADO_POR + " =?, "
					+ CBDataSinProcesarModel.FIELD_FECHA_CREACION + " =?, "
					+ CBDataSinProcesarModel.FIELD_FECHA_MODIFICACION + " =?, " + " where  "
					+ CBDataSinProcesarModel.PKFIELD_CBDATASINPROCESARID + " = ? ";

			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getNombreArchivo(), registro.getDataArchivo(),
					registro.getEstado(), registro.getCreadoPor(), registro.getModificadoPor(),
					registro.getFechaCreacion(), registro.getFechaModificacion(), registro.getCBDataSinProcesarId() };

			ret = qry.update(conn, queInsert, param);

		} catch (Exception e) {
			Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}

		return ret;

	}

	/**
	 * This method should return a list of objects within given filters
	 * 
	 * @return the list of Objects
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CBDataSinProcesarModel> Listado(List<Filtro> filtro, List<Orden> orden) {
		List<CBDataSinProcesarModel> ret = null;
		String query;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			query = "select " + ObtieneCampos.obtieneSQL(CBDataSinProcesarModel.class, null, true, null) + " from "
					+ CBDataSinProcesarModel.TABLE;

			FiltroQuery filtroQuery = null;

			filtroQuery = GeneraFiltroQuery.generaFiltro(null, orden, true);

			String sqlFiltros = Filtro.getStringFiltros(filtro, true);
			QueryRunner qry = new QueryRunner();
			BeanListHandler blh = new BeanListHandler(CBDataSinProcesarModel.class);
			if (filtroQuery.getParams() != null) {
				ret = (List<CBDataSinProcesarModel>) qry.query(conn, query + sqlFiltros + filtroQuery.getSql(), blh,
						filtroQuery.getParams());
			} else {
				ret = (List<CBDataSinProcesarModel>) qry.query(conn, query + sqlFiltros + filtroQuery.getSql(), blh);
			}
			this.totalRegistros = ret.size();
			System.out.println("resultado: " + ret.size());
		} catch (Exception e) {
			Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
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

	/*
	 * Metodo propio para GT version unificada
	 * 
	 */

	/**
	 * This method should insert a new record in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 */
	private String INSERTA_DATOS_NO_PROCESADOSGT = "INSERT " + "INTO cb_data_sin_procesar " + "  ( "
			+ "  cbdatasinprocesarid,  nombre_archivo, " + "    data_archivo, " + "causa,    estado, "
			+ "    creado_por, " + "    fecha_creacion, " + "    id_archivos_insertados " + "  ) " + "  VALUES "
			+ "  ( " + " cb_data_sin_procesar_sq.nextval,   ?, " + "    ?, " + " ?,   ?, " + "    ?, " + "    sysdate, "
			+ "    ? " + "  )";

	public int insertarMasivoGT(List<CBDataSinProcesarModel> registros) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {

			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBDataSinProcesarModel.TABLE + " ("
					+ ObtieneCampos.obtieneSQL(CBDataSinProcesarModel.class, null, true, null) + ") values " + " ("
					+ ObtieneCampos.obtieneInsert(CBDataSinProcesarModel.class) + ")";
			Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.INFO,
					"Query data sin procesar:" + queInsert);
			QueryRunner qry = new QueryRunner();
			Iterator<CBDataSinProcesarModel> itData = registros.iterator();

			while (itData.hasNext()) {
				CBDataSinProcesarModel registro = (CBDataSinProcesarModel) itData.next();
				Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.INFO,
						"Id maestro:" + registro.getIdCargaMaestro());
				Object[] param = new Object[] { registro.getNombreArchivo(), registro.getDataArchivo(),
						registro.getCausa(), registro.getEstado(), registro.getCreadoPor(),
						registro.getIdCargaMaestro() };

				ret += qry.update(conn, INSERTA_DATOS_NO_PROCESADOSGT, param);
				Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.INFO, "Ret:" + ret);
			}
		} catch (Exception e) {
			Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				Logger.getLogger(CBDataSinProcesarDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return ret;

	}

}