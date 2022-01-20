package com.terium.siccam.dao;
 
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoOpcionModel;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.FiltroQuery;
import com.terium.siccam.utils.GeneraFiltroQuery;
import com.terium.siccam.utils.ObtieneCampos;
import com.terium.siccam.utils.Orden; 
/**
 *
 * @author rSianB for terium.com 
 */ 
public class  CBCatalogoOpcionDAO {  
	private static Logger log = Logger.getLogger(CBCatalogoOpcionDAO.class);
 



    private int totalRegistros;
	/**
	  * This method should insert a new record in the DB
     * @return an int which depends on the result of the insert
     */
	public int insertar(CBCatalogoOpcionModel registro) {

		String queInsert;
		Connection conn = null;
		int ret = 0;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBCatalogoOpcionModel.TABLE
			            + " (" + ObtieneCampos.obtieneSQL(CBCatalogoOpcionModel.class, null, true, null) + ") values "
                                               + " (" + ObtieneCampos.obtieneInsert(CBCatalogoOpcionModel.class) + ")";
			
			QueryRunner qry = new QueryRunner();
            Object[] param  = new Object[]{ 
				registro.getCBCatalogoOpcionId(),
				registro.getNombre(),
				registro.getValor(),
				registro.getTipo(),
				registro.getEstado(),
				registro.getOrden(),
				registro.getCreadoPor(),
				registro.getModificadoPor(),
				registro.getFechaCreacion(),
				registro.getFechaModificacion()};

		ret = qry.update(conn, queInsert, param);
		
		}catch (Exception e) {
			log.error("insertar() - Error ", e);
			//Logger.getLogger(CBCatalogoOpcionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("insertar() - Error ", e);
				//Logger.getLogger(CBCatalogoOpcionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
               
        return ret;	
		
	}
	/**
	  * This method should update a record within provide filters
     * @return an int which depends on the result of the update
     */
	public int actualiza(CBCatalogoOpcionModel registro) {
		
		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Update " + CBCatalogoOpcionModel.TABLE 
			+ " set " +           CBCatalogoOpcionModel.FIELD_NOMBRE + " =?, " + 
           CBCatalogoOpcionModel.FIELD_VALOR + " =?, " + 
           CBCatalogoOpcionModel.FIELD_TIPO + " =?, " + 
           CBCatalogoOpcionModel.FIELD_ESTADO + " =?, " + 
           CBCatalogoOpcionModel.FIELD_ORDEN + " =?, " + 
           CBCatalogoOpcionModel.FIELD_CREADO_POR + " =?, " + 
           CBCatalogoOpcionModel.FIELD_MODIFICADO_POR + " =?, " + 
           CBCatalogoOpcionModel.FIELD_FECHA_CREACION + " =?, " + 
           CBCatalogoOpcionModel.FIELD_FECHA_MODIFICACION + " =?, " + 
           " where  " +CBCatalogoOpcionModel.PKFIELD_CBCATALOGOOPCIONID+ " = ? ";
			
			QueryRunner qry = new QueryRunner();
		            Object[] param  = new Object[]{
				registro.getNombre(),
				registro.getValor(),
				registro.getTipo(),
				registro.getEstado(),
				registro.getOrden(),
				registro.getCreadoPor(),
				registro.getModificadoPor(),
				registro.getFechaCreacion(),
				registro.getFechaModificacion(),
				registro.getCBCatalogoOpcionId()
			};
		
		ret = qry.update(conn, queInsert, param);

        } catch (Exception e) {
        	log.error("actualiza() - Error ", e);
			//Logger.getLogger(CBCatalogoOpcionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("actualiza() - Error ", e);
				//Logger.getLogger(CBCatalogoOpcionDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
               
              return ret; 	
		
	}
	/**
	  * This method should return a list of objects within given filters
     * @return the list of Objects
     */
@SuppressWarnings({ "unchecked", "rawtypes" })
public List<CBCatalogoOpcionModel> Listado(List<Filtro> filtro, List<Orden> orden) {
      List<CBCatalogoOpcionModel> ret = null;
      String query;
      Connection conn = null;

		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection(); 
			query = "select " + ObtieneCampos.obtieneSQL(CBCatalogoOpcionModel.class, null, true, null) + " from " + CBCatalogoOpcionModel.TABLE;
			FiltroQuery filtroQuery = null;
			
			filtroQuery = GeneraFiltroQuery.generaFiltro(null, orden, true);

			String sqlFiltros = Filtro.getStringFiltros(filtro, true);
			QueryRunner qry = new QueryRunner();
			BeanListHandler blh = new BeanListHandler(CBCatalogoOpcionModel.class);
			if (filtroQuery.getParams() != null){
				ret =  (List<CBCatalogoOpcionModel>) qry.query(conn, query + sqlFiltros +  filtroQuery.getSql(), blh, filtroQuery.getParams());
			}else{
				ret =  (List<CBCatalogoOpcionModel>) qry.query(conn,  query + sqlFiltros + filtroQuery.getSql(), blh);		
	}
			this.totalRegistros = ret.size();
			System.out.println("resultado: " + ret.size());
			log.debug("resultado: " + ret.size());
			
        }catch (Exception e) {
        	log.error("Listado() - Error ", e);
			//Logger.getLogger(CBCatalogoOpcionDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				log.error("Listado() - Error ", e);
				//Logger.getLogger(CBCatalogoOpcionDAO.class.getName()).log(Level.SEVERE, null, e);
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
 } 