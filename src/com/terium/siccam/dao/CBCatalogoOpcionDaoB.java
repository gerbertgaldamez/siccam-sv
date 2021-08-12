package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoOpcionModel;

@SuppressWarnings("serial")
public class CBCatalogoOpcionDaoB extends ControladorBase{

	private String CONSULTA_OPCIONES = "select nombre, valor valor, estado estado from CB_CATALOGO_OPCION where tipo = 'ESTADO' ";

	// consulta
	public List<CBCatalogoOpcionModel> obtieneListaOpcion() {
		List<CBCatalogoOpcionModel> listado = new ArrayList<CBCatalogoOpcionModel>();
		Connection con = null;
		try {
			con = obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBCatalogoOpcionModel> blh = new BeanListHandler<CBCatalogoOpcionModel>(
						CBCatalogoOpcionModel.class);
				listado = qr
						.query(con, CONSULTA_OPCIONES, blh, new Object[] {});
				
				if(listado!= null && listado.size() > 0){
					for(CBCatalogoOpcionModel m : listado){
						System.out.println("nombre: "+m.getNombre() +" valor: "+m.getValor());
					}
				}
				
		} catch (Exception e) {
			Logger.getLogger(CBCatalogoOpcionDaoB.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBCatalogoOpcionDaoB.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return listado;
	}
}
