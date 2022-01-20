package com.terium.siccam.dao;

import java.sql.Connection;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		//PreparedStatement stmt = null;
		Statement stmt = null;
		ResultSet rst = null;
		CBCuadreSidraModel obj = null;
		String query = null;
		try {
			query = ConsultasSQ.CONSULTA_CUADRE_SIDRA_QR;
			String where = "";
			where += "AND FECHA_PAGO >= TO_CHAR(TO_DATE ('" + objModel.getFechaInicio() + "', 'dd/MM/yyyy')) "
					+ "AND FECHA_PAGO <= TO_CHAR(TO_DATE ('" + objModel.getFechaFin() + "', 'dd/MM/yyyy')) ";
			if (!"".equals(objModel.getNombreCliente())) {
				where += "AND UPPER (NOMBRE_CLIENTE_PDV) = '"   + objModel.getNombreCliente().toUpperCase() + "' ";
				//where += "AND NOMBRE_CLIENTE_PDV = '"   + objModel.getNombreCliente() + "' ";
			}
			conn = obtenerDtsPromo().getConnection();
			//stmt = conn.prepareStatement(ConsultasSQ.CONSULTA_CUADRE_SIDRA_QR);
			stmt = conn.createStatement();
			log.debug(
					"obtenerCuadreSidra() - " + "QUERY " + query + where);
			
			rst = stmt.executeQuery(query + where);
			while (rst.next()) {
				objModel = new CBCuadreSidraModel();
				objModel.setFechaFactura(rst.getString(1));
				objModel.setSerie(rst.getString(2));
				objModel.setNombreCliente(rst.getString(3));
				objModel.setNombreClienteFinal(rst.getString(4));
				objModel.setBillRefNo(rst.getString(5));
				objModel.setFechaPago(rst.getString(6));
				objModel.setMontoPago(rst.getBigDecimal(7));
				objModel.setEstadoFactura(rst.getString(8));
				objModel.setFechaSincronizacion(rst.getString(9));
				objModel.setNoBoleta(rst.getString(10));
				objModel.setFechaBoleta(rst.getString(11));
                objModel.setMontoBoleta(rst.getBigDecimal(12));
				objModel.setJornada(rst.getString(13));
				objModel.setFechaInicioJ(rst.getString(14));
				objModel.setFechaLiquidacionJ(rst.getString(15));
				objModel.setEstadoJornada(rst.getString(16));
				objModel.setTipoRutaPanel(rst.getString(17));
				objModel.setNombrerutaP(rst.getString(18));
				objModel.setNombreVendedor(rst.getString(19));
				objModel.setExiste(rst.getString(20));
				//objModel.setTotalArbor(rst.getBigDecimal(21));
				//objModel.setTotalPagado(rst.getBigDecimal(22));
				//objModel.setMontoPagadoBmf(rst.getBigDecimal(23));
				objModel.setTotalArbor(rst.getString(21));
				objModel.setTotalPagado(rst.getString(22));
				objModel.setMontoPagadoBmf(rst.getString(23));
				

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


