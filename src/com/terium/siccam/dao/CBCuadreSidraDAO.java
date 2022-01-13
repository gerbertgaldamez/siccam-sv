package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCuadreSidraModel;
import com.terium.siccam.utils.ConsultasSQ;




public class CBCuadreSidraDAO extends ControladorBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<CBCuadreSidraModel> obtenerCuadreSidra(CBCuadreSidraModel objModel) {
		List<CBCuadreSidraModel> lst = new ArrayList<CBCuadreSidraModel>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;
		try {
			conn = obtenerDtsPromo().getConnection();
			stmt = conn.prepareStatement(ConsultasSQ.CONSULTA_CUADRE_SIDRA_QR);

			rst = stmt.executeQuery();
			while (rst.next()) {
				objModel = new CBCuadreSidraModel();
				objModel.setSerie(rst.getString(1));
				objModel.setNombreCliente(rst.getString(2));
				objModel.setNombreClienteFinal(rst.getString(3));
				objModel.setBillRefNo(rst.getString(4));
				objModel.setFechaPago(rst.getString(5));
				objModel.setMontoPago(rst.getBigDecimal(6));
				objModel.setEstadoFactura(rst.getString(7));
				objModel.setFechaSincronizacion(rst.getString(8));
				objModel.setNoBoleta(rst.getString(9));
				objModel.setFechaBoleta(rst.getString(10));
                objModel.setMontoBoleta(rst.getBigDecimal(11));
				objModel.setJornada(rst.getString(12));
				objModel.setFechaInicioJ(rst.getString(13));
				objModel.setFechaLiquidacionJ(rst.getString(14));
				objModel.setEstadoJornada(rst.getString(15));
				objModel.setTipoRutaPanel(rst.getString(16));
				objModel.setNombrerutaP(rst.getString(17));
				objModel.setNombreVendedor(rst.getString(18));

				lst.add(objModel);
			}
	}catch(Exception e){
		
	}finally {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			
		}
	}
		return lst;
	}
}


