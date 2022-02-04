package com.terium.siccam.dao;
 
import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.controller.CBConsultaContabilizacionController;
import com.terium.siccam.model.CBPagosModel;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.FiltroQuery;
import com.terium.siccam.utils.GeneraFiltroQuery;
import com.terium.siccam.utils.ObtieneCampos;
import com.terium.siccam.utils.Orden; 
/**
 *
 * @author rSianB for terium.com 
 */ 
public class  CBPagosDAO { 
	private static Logger log = Logger.getLogger(CBPagosDAO.class.getName());
    private int totalRegistros;
	/**
	  * This method should insert a new record in the DB
     * @return an int which depends on the result of the insert
     */
	public int insertar(CBPagosModel registro){

		String queInsert;
		int ret = 0;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			queInsert = " Insert into  " + CBPagosModel.TABLE
			            + " (" + ObtieneCampos.obtieneSQL(CBPagosModel.class, null, true, null) + ") values "
                                               + " (" + ObtieneCampos.obtieneInsert(CBPagosModel.class) + ")";
			
               QueryRunner qry = new QueryRunner();
               Object[] param  = new Object[]{ 
				registro.getCBPagosId(),
				registro.getFecEfectividad(),
				registro.getNumSecuenci(),
				registro.getCodCliente(),
				registro.getTelefono(),
				registro.getImpPago(),
				registro.getCodCaja(),
				registro.getDesPago(),
				registro.getEstadoConciliado(),
				registro.getNumConciliacion(),
				registro.getCreadoPor(),
				registro.getFechaCreacion()};

            ret= qry.update(conn, queInsert, param);
		
		} catch (Exception e) {
			log.error("insertar" + " - Error", e);
			//Logger.getLogger(CBPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)conn.close();
			}catch (Exception e) {
				log.error("insertar" + " - Error", e);
				//Logger.getLogger(CBPagosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
               
        return ret;	
		
	}
	/**
	  * This method should update a record within provide filters
     * @return an int which depends on the result of the update
     */
	public int actualiza(Connection conn, CBPagosModel registro){
		
		String queInsert;
		int ret = 0;
		try {
			queInsert = " Update " + CBPagosModel.TABLE 
			+ " set " +           CBPagosModel.FIELD_FEC_EFECTIVIDAD + " =?, " + 
           CBPagosModel.FIELD_NUM_SECUENCI + " =?, " + 
           CBPagosModel.FIELD_COD_CLIENTE + " =?, " + 
           CBPagosModel.FIELD_TELEFONO + " =?, " + 
           CBPagosModel.FIELD_IMP_PAGO + " =?, " + 
           CBPagosModel.FIELD_COD_CAJA + " =?, " + 
           CBPagosModel.FIELD_DES_PAGO + " =?, " + 
           CBPagosModel.FIELD_ESTADO_CONCILIADO + " =?, " + 
           CBPagosModel.FIELD_NUM_CONCILIACION + " =?, " + 
           CBPagosModel.FIELD_CREADO_POR + " =?, " + 
           CBPagosModel.FIELD_FECHA_CREACION + " =?, " + 
           " where  " +CBPagosModel.PKFIELD_CBPAGOSID+ " = ? ";
			
               QueryRunner qry = new QueryRunner();
                       Object[] param  = new Object[]{
				registro.getFecEfectividad(),
				registro.getNumSecuenci(),
				registro.getCodCliente(),
				registro.getTelefono(),
				registro.getImpPago(),
				registro.getCodCaja(),
				registro.getDesPago(),
				registro.getEstadoConciliado(),
				registro.getNumConciliacion(),
				registro.getCreadoPor(),
				registro.getFechaCreacion()};

            ret = qry.update(conn, queInsert, param);


        } catch (Exception e) {
        	log.error("actualiza" + " - Error", e);
			//Logger.getLogger(CBPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)conn.close();
			}catch (Exception e) {
				log.error("actualiza" + " - Error", e);
				//Logger.getLogger(CBPagosDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
               
              return ret; 	
		
	}
	/**
	  * This method should return a list of objects within given filters
     * @return the list of Objects
     */
@SuppressWarnings({ "unchecked", "rawtypes" })
public List<CBPagosModel> Listado(Connection conn, List<Filtro> filtro, List<Orden> orden) {
      List<CBPagosModel> ret = null;
      String query;

		try {
			query = "select " + ObtieneCampos.obtieneSQL(CBPagosModel.class, null, true, null) + " from " + CBPagosModel.TABLE;
		FiltroQuery filtroQuery = null;
		
		filtroQuery = GeneraFiltroQuery.generaFiltro(null, orden, true);

		String sqlFiltros = Filtro.getStringFiltros(filtro, true);
		QueryRunner qry = new QueryRunner();
		BeanListHandler blh = new BeanListHandler(CBPagosModel.class);
		if (filtroQuery.getParams() != null){
			ret =  (List<CBPagosModel>) qry.query(conn, query + sqlFiltros +  filtroQuery.getSql(), blh, filtroQuery.getParams());
		}else{
			ret =  (List<CBPagosModel>) qry.query(conn,  query + sqlFiltros + filtroQuery.getSql(), blh);		
}
		this.totalRegistros = ret.size();
		log.debug("resultado: " + ret.size());
			//Logger.getLogger(CBPagosDAO.class.getName())
			//	.log(Level.INFO,"resultado: " + ret.size());
			
        }  catch (Exception e) {
        	log.error("Listado" + " - Error", e);
			//Logger.getLogger(CBPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(conn != null)conn.close();
			}catch (Exception e) {
				log.error("Listado" + " - Error", e);
				//Logger.getLogger(CBPagosDAO.class.getName()).log(Level.SEVERE, null, e);
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