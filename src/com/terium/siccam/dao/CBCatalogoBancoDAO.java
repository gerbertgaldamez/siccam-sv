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
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.FiltroQuery;
import com.terium.siccam.utils.GeneraFiltroQuery;
import com.terium.siccam.utils.ObtieneCampos;
import com.terium.siccam.utils.Orden; 
/**
 *
 * @author rSianB for terium.com 
 */ 
public class  CBCatalogoBancoDAO {  
	private static Logger log = Logger.getLogger(CBCatalogoBancoDAO.class);
    private int totalRegistros;
	/**
	  * This method should insert a new record in the DB
     * @return an int which depends on the result of the insert
     */
	public int insertar(CBCatalogoBancoModel registro) {

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBCatalogoBancoModel.TABLE
			            + " (" + ObtieneCampos.obtieneSQL(CBCatalogoBancoModel.class, null, true, null) + ") values "
                                               + " (" + ObtieneCampos.obtieneInsert(CBCatalogoBancoModel.class) + ")";
			log.debug( "query: " + queInsert);
			
			QueryRunner qry = new QueryRunner();
            Object[] param  = new Object[]{ 
//				registro.getCBCatalogoBancoId(),
				registro.getNombre(),
				registro.getContacto(),
				registro.getTelefono(),
				registro.getExtension(),
				registro.getEstado(),
				registro.getCreadoPor(),
				registro.getModificadoPor(),
				registro.getFechaCreacion(),
				registro.getFechaModificacion()};

            ret = qry.update(conn, queInsert, param);
		} catch (Exception e) {
			log.error("insertar() - Error ", e);
			//Logger.getLogger(CBCatalogoBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)conn.close();
			}catch (SQLException e) {
				log.error("insertar() - Error ", e);
			//	Logger.getLogger(CBCatalogoBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
               
        return ret;	
		
	}
	/**
	  * This method should update a record within provide filters
     * @return an int which depends on the result of the update
     */
	public int actualiza(CBCatalogoBancoModel registro){
		
		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Update " + CBCatalogoBancoModel.TABLE 
			+ " set " +           CBCatalogoBancoModel.FIELD_NOMBRE + " =?, " + 
           CBCatalogoBancoModel.FIELD_CONTACTO + " =?, " + 
           CBCatalogoBancoModel.FIELD_TELEFONO + " =?, " + 
           CBCatalogoBancoModel.FIELD_EXTENSION + " =?, " + 
           CBCatalogoBancoModel.FIELD_ESTADO + " =?, " + 
           CBCatalogoBancoModel.FIELD_CREADO_POR + " =?, " + 
           CBCatalogoBancoModel.FIELD_MODIFICADO_POR + " =?, " + 
           CBCatalogoBancoModel.FIELD_FECHA_CREACION + " =?, " + 
           CBCatalogoBancoModel.FIELD_FECHA_MODIFICACION + " =?, " + 
           " where  " +CBCatalogoBancoModel.PKFIELD_CBCATALOGOBANCOID+ " = ? ";
			
			QueryRunner qry = new QueryRunner();
		            Object[] param  = new Object[]{
				registro.getNombre(),
				registro.getContacto(),
				registro.getTelefono(),
				registro.getExtension(),
				registro.getEstado(),
				registro.getCreadoPor(),
				registro.getModificadoPor(),
				registro.getFechaCreacion(),
				registro.getFechaModificacion(),
		//		registro.getCBCatalogoBancoId()
			};
		
		ret = qry.update(conn, queInsert, param);

        } catch (Exception e) {
        	log.error("actualiza() - Error ", e);
        	//Logger.getLogger(CBCatalogoBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)conn.close();
			}catch (SQLException e) {
				log.error("actualiza() - Error ", e);
				//Logger.getLogger(CBCatalogoBancoDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
               
              return ret; 	
		
	}
	/**
	  * This method should return a list of objects within given filters
     * @return the list of Objects
     */
@SuppressWarnings({ "rawtypes", "unchecked" })
public List<CBCatalogoBancoModel> Listado(List<Filtro> filtro, List<Orden> orden) {
      List<CBCatalogoBancoModel> ret = null;
      String query;
      Connection conn = null;

		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			query = "select " + ObtieneCampos.obtieneSQL(CBCatalogoBancoModel.class, null, true, null) + " from " + CBCatalogoBancoModel.TABLE;
			
			FiltroQuery filtroQuery = null;
			
			filtroQuery = GeneraFiltroQuery.generaFiltro(null, orden, true);

			String sqlFiltros = Filtro.getStringFiltros(filtro, true);
			QueryRunner qry = new QueryRunner();
			BeanListHandler blh = new BeanListHandler(CBCatalogoBancoModel.class);
			
			 log.debug( "query " + query + sqlFiltros +  filtroQuery.getSql());
			if (filtroQuery.getParams() != null){
				ret =  (List<CBCatalogoBancoModel>) qry.query(conn, query + sqlFiltros +  filtroQuery.getSql(), blh, filtroQuery.getParams());
			}else{
				ret =  (List<CBCatalogoBancoModel>) qry.query(conn,  query + sqlFiltros + filtroQuery.getSql(), blh);		
			}
			this.totalRegistros = ret.size();
			log.debug( "resultado: " + ret.size());
			
			
        } catch (Exception e) {
        	log.error("Listado() - Error ", e);
        	//Logger.getLogger(CBCatalogoBancoDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)conn.close();
			}catch (SQLException e) {
				log.error("Listado() - Error ", e);
				//Logger.getLogger(CBCatalogoBancoDAO.class.getName()).log(Level.SEVERE, null, e);
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