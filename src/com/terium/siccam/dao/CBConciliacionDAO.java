package com.terium.siccam.dao;

import java.math.RoundingMode;
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
import com.terium.siccam.model.CBResumenDiarioConciliacionModel;
import com.terium.siccam.model.CBConciliacionDetallada;
import com.terium.siccam.model.CBConciliacionModel;
import com.terium.siccam.model.ListaCombo;
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
@SuppressWarnings("serial")
public class CBConciliacionDAO extends ControladorBase{

	private int totalRegistros;

	/**
	 * This method should insert a new record in the DB
	 * 
	 * @return an int which depends on the result of the insert
	 */
	public int insertar(CBConciliacionModel registro){

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  "
					+ CBConciliacionModel.TABLE
					+ " ("
					+ ObtieneCampos.obtieneSQL(CBConciliacionModel.class, null,
							true, null) + ") values " + " ("
					+ ObtieneCampos.obtieneInsert(CBConciliacionModel.class)
					+ ")";
			
			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getCBConciliacionId(),
					registro.getCodCliente(), registro.getTelefono(),
					registro.getFecha(), registro.getMonto(), registro.getEstado(),
					registro.getMes(), registro.getCreadoPor(),
					registro.getModificadoPor(), registro.getFechaCreacion(),
					registro.getFechaModificacion() };

			ret = qry.update(conn, queInsert, param);
		} catch (Exception e) {
			Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		
		return ret;

	}

	/**
	 * This method should update a record within provide filters
	 * 
	 * @return an int which depends on the result of the update
	 */
	public int actualiza(CBConciliacionModel registro) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			queInsert = " Update " + CBConciliacionModel.TABLE + " set "
					+ CBConciliacionModel.FIELD_COD_CLIENTE + " =?, "
					+ CBConciliacionModel.FIELD_TELEFONO + " =?, "
					+ CBConciliacionModel.FIELD_FECHA + " =?, "
					+ CBConciliacionModel.FIELD_MONTO + " =?, "
					+ CBConciliacionModel.FIELD_ESTADO + " =?, "
					+ CBConciliacionModel.FIELD_MES + " =?, "
					+ CBConciliacionModel.FIELD_CREADO_POR + " =?, "
					+ CBConciliacionModel.FIELD_MODIFICADO_POR + " =?, "
					+ CBConciliacionModel.FIELD_FECHA_CREACION + " =?, "
					+ CBConciliacionModel.FIELD_FECHA_MODIFICACION + " =?, "
					+ " where  " + CBConciliacionModel.PKFIELD_CBCONCILIACIONID
					+ " = ? ";
			
			QueryRunner qry = new QueryRunner();
			Object[] param = new Object[] { registro.getCodCliente(),
					registro.getTelefono(), registro.getFecha(),
					registro.getMonto(), registro.getEstado(), registro.getMes(),
					registro.getCreadoPor(), registro.getModificadoPor(),
					registro.getFechaCreacion(), registro.getFechaModificacion() };

			ret = qry.update(conn, queInsert, param);

		}catch (Exception e) {
			Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		
		return ret;

	}

	/**
	 * This method should return a list of objects within given filters
	 * 
	 * @return the list of Objects
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<CBConciliacionModel> Listado(
			List<Filtro> filtro, List<Orden> orden){
		List<CBConciliacionModel> ret = null;
		String query;
		Connection conn = null;

		try {
			conn = obtenerDtsPromo().getConnection();
			query = "select "
					+ ObtieneCampos.obtieneSQL(CBConciliacionModel.class, null,
							true, null) + " from " + CBConciliacionModel.TABLE;
			
			FiltroQuery filtroQuery = null;

			filtroQuery = GeneraFiltroQuery.generaFiltro(null, orden, true);

			String sqlFiltros = Filtro.getStringFiltros(filtro, true);
			QueryRunner qry = new QueryRunner();
			BeanListHandler blh = new BeanListHandler(CBConciliacionModel.class);
			if (filtroQuery.getParams() != null) {
				ret = (List<CBConciliacionModel>) qry.query(conn, query
						+ sqlFiltros + filtroQuery.getSql(), blh,
						filtroQuery.getParams());
			} else {
				ret = (List<CBConciliacionModel>) qry.query(conn, query
						+ sqlFiltros + filtroQuery.getSql(), blh);
			}
			this.totalRegistros = ret.size();
			System.out.println("resultado: " + ret.size());
		}catch (Exception e) {
			Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
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

	/**
	 * Consulta de resumen diario de conciliaciones
	 * */
	public List<CBResumenDiarioConciliacionModel> obtenerCBConciliaciones(CBResumenDiarioConciliacionModel objModel)  {
		List<CBResumenDiarioConciliacionModel> lst = new ArrayList<CBResumenDiarioConciliacionModel>();
		String query = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		
		try {
			conn = obtenerDtsPromo().getConnection();
			/**
			 * Modified by CarlosGodinez -> 10/08/2018
			 * Se construye query de manera dinamica y mas limpia
			 * */
			System.out.println("llega hasta este metodo");
			query = ConsultasSQ.RESUMEN_DIARIO_CONCILIACION_QRY;
			
			query += " and dia >= to_date('" + objModel.getFechaInicial() + "', 'dd/MM/yyyy')"
					+ " and dia <= to_date('" + objModel.getFechaFinal() + "', 'dd/MM/yyyy') ";
			
			if (!"".equals(objModel.getIdAgencia())) {
				query += " and cbcatalogoagenciaid = '" + objModel.getIdAgencia() + "' ";
			}
			if (!"".equals(objModel.getTipo())) {
				query += " and valor_tipo = '" + objModel.getTipo() + "' "; 
			}
			if(!"".equals(objModel.getCodigoColector())) {
				query += " and trim(codigo_colector) = '" + objModel.getCodigoColector() + "' ";
			}
			
			//Valiacion de estados
			if("1".equals(objModel.getEstado())) {
				query += " and ((pagos_telefonica - automatica - manual_t) = 0 and (confronta_banco - automatica - manual_b) = 0) and (manual_t <= 0 and manual_b <= 0) ";
			} else if("2".equals(objModel.getEstado())) {
				query += " and ((pagos_telefonica - automatica - manual_t) > 0 or (confronta_banco - automatica - manual_b) > 0) and (pagos_telefonica != 0 and confronta_banco !=0) ";
			} else if("3".equals(objModel.getEstado())) {
				query += " and ((manual_t > 0 and (pagos_telefonica - automatica - manual_t) = 0 and (confronta_banco - automatica - manual_b) = 0) or (manual_b > 0 and (pagos_telefonica - automatica - manual_t) = 0 and (confronta_banco - automatica - manual_b) = 0)) ";
			} else if("4".equals(objModel.getEstado())) {
				query += " and (pagos_telefonica = 0 and confronta_banco != 0) ";
			} else if("5".equals(objModel.getEstado())) {
				query += " and (pagos_telefonica != 0 and confronta_banco = 0) ";
			}
			
			Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.INFO, 
					"Query resumen diario de conciliaciones: " + query);

			/**
			 * FIN CarlosGodinez -> 10/08/2018
			 * */
			/*QueryRunner qr = new QueryRunner();
			BeanListHandler<CBResumenDiarioConciliacionModel> bhl = new BeanListHandler<CBResumenDiarioConciliacionModel>(
					CBResumenDiarioConciliacionModel.class);
			lst = qr.query(conn, query, bhl, new Object[] {});

			Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.INFO, 
					"Tamaño ArrayList: " + lst.size());*/
			stmt = conn.prepareStatement(query);

			rst = stmt.executeQuery();
			while (rst.next()) {
				objModel = new CBResumenDiarioConciliacionModel();
				objModel.setDia(rst.getDate(1));
				objModel.setNombre(rst.getString(2));
				objModel.setCodigoColector(rst.getString(3));
				objModel.setTipo(rst.getString(4));
				objModel.setTransTelefonica(rst.getBigDecimal(5));
				objModel.setTransBanco(rst.getBigDecimal(6));
				objModel.setConciliadas(rst.getBigDecimal(7));
				objModel.setDifTransaccion(rst.getBigDecimal(8));
				objModel.setPagosTelefonica(rst.getBigDecimal(9));
				objModel.setConfrontaBanco(rst.getBigDecimal(10));
				
				objModel.setAutomatica(rst.getBigDecimal(11));
				// System.out.println("monto en dao 2 " + obj.getMonto());
				// }
				objModel.setManualTelefonica(rst.getBigDecimal(12));
				objModel.setManualBanco(rst.getBigDecimal(13));
				objModel.setPendiente(rst.getBigDecimal(14));
				objModel.setIdAgencia(rst.getString(15));
				objModel.setReal_t(rst.getBigDecimal(16));
				objModel.setReal_b(rst.getBigDecimal(17));
				

				lst.add(objModel);
			}


		}catch (Exception e) {
			Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return lst;
	}

	// consulta pr_parametros
	private String CONSULTA_PARAMETROS = "SELECT descripcion_parametro valorCaracter, "
			+ " codigo_parametro valorCodigo " + " FROM CB_PARAMETROS";

	public List<ListaCombo> listadoParametros() {
		List<ListaCombo> listado = new ArrayList<ListaCombo>();
		Connection con = null;
		try {
			con =  obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<ListaCombo> bhl = new BeanListHandler<ListaCombo>(
						ListaCombo.class);
				listado = qr.query(con, CONSULTA_PARAMETROS, bhl,
						new Object[] {});
		}catch (Exception e) {
			Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBConciliacionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return listado;
	}
}