package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCuadreSidraModel;
import com.terium.siccam.utils.ConsultasSQ;




public class CBCuadreSidraDAO extends ControladorBase {
	private static Logger log = Logger.getLogger(CBCuadreSidraDAO.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<CBCuadreSidraModel> obtenerCuadreSidra(CBCuadreSidraModel objModel) {
		List<CBCuadreSidraModel> lst = new ArrayList<CBCuadreSidraModel>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		String query = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			query = ConsultasSQ.CONSULTA_CUADRE_SIDRA_QR;
			String where = "";
			/*where += "AND FECHA_FACTURA >= TO_CHAR(TO_DATE ('" + objModel.getFechaInicio() + "', 'dd/MM/yyyy')) "
					+ "AND FECHA_FACTURA <= TO_CHAR(TO_DATE ('" + objModel.getFechaFin() + "', 'dd/MM/yyyy')) ";*/
			where += "AND FECHA_FACTURA >= '" + objModel.getFechaInicio() + "' " + 
					 "AND FECHA_FACTURA <= '" + objModel.getFechaFin() + "' " ;
			
			log.debug(
					"obtenerCuadreSidra() - " + "valor del get existe en la dao " + objModel.getExiste());
			if (Integer.parseInt(objModel.getExiste()) != 2) {
				where += "AND EXISTE = "   + objModel.getExiste().toUpperCase() + " ";
				
			}
			if(Integer.parseInt(objModel.getCuadre())!= 2){
				
				if(Integer.parseInt(objModel.getCuadre()) == 0){
					where += "AND CUADRE <> 0"  ;
				}
				else {
					where += "AND CUADRE = 0  ";
				}
				
			}
			if (!"".equals(objModel.getNombreCliente())) {
				where += "AND NOMBRE_CLIENTE_PDV = '" + objModel.getNombreCliente() + "' ";
			}
			if (!"".equals(objModel.getNumFactura())) {
				
				where += "AND NUMERO_FACTURA = " + objModel.getNumFactura() + " ";
				log.debug(
						"numero de factura en la dao - " + objModel.getNumFactura());
			}
			
			stmt = conn.prepareStatement(query + where);
			
			log.debug(
					"obtenerCuadreSidra() - " + "QUERY " + query + where);
			
			rst = stmt.executeQuery();
			while (rst.next()) {
				objModel = new CBCuadreSidraModel();
				objModel.setNumFactura(rst.getString(1));
				//objModel.setCodFactura(rst.getString(3));
				objModel.setSerie(rst.getString(2));
				 objModel.setFechaFactura(rst.getString(3));
				objModel.setNombreCliente(rst.getString(4));
				objModel.setNombreClienteFinal(rst.getString(5));
				objModel.setBillRefNo(rst.getString(6));
				objModel.setFechaPago(rst.getString(7));
				objModel.setMontoPago(rst.getString(8));
				objModel.setEstadoFactura(rst.getString(9));
				objModel.setFechaSincronizacion(rst.getString(10));
				objModel.setNoBoleta(rst.getString(11));
				objModel.setFechaBoleta(rst.getString(12));
                objModel.setMontoBoleta(rst.getBigDecimal(13));
				objModel.setJornada(rst.getString(14));
				objModel.setFechaInicioJ(rst.getString(15));
				objModel.setFechaLiquidacionJ(rst.getString(16));
				objModel.setEstadoJornada(rst.getString(17));
				objModel.setTipoRutaPanel(rst.getString(18));
				objModel.setNombrerutaP(rst.getString(19));
				objModel.setNombreVendedor(rst.getString(20));
				objModel.setExiste(rst.getString(21));
				objModel.setTotalArbor(rst.getBigDecimal(22));
				objModel.setTotalPagado(rst.getBigDecimal(23));
				objModel.setMontoPagadoBmf(rst.getString(24));
				
				
				lst.add(objModel);
			}
	}catch(Exception e){
		log.error("obtenerCuadreSidra() - Error ", e);	
	}finally {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			log.error("obtenerCuadreSidra() - Error ", e);	
		}
	}
		return lst;
	}
}


