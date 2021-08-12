package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.DetalleLogModel;
import com.terium.siccam.utils.ConsultasSQ;

public class DetalleLogDAO  extends ControladorBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5259458298131599027L;
	
	public boolean insertErrorLog(DetalleLogModel obj){
		PreparedStatement pstmt = null;
		Connection conn = null;
		
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(DetalleLogDAO.class.getName()).log(Level.INFO, ConsultasSQ.INSERT_ERROR_LOG_QY);		
			pstmt = conn.prepareStatement(ConsultasSQ.INSERT_ERROR_LOG_QY);
			
			pstmt.setInt(1, obj.getCodigoerror());
			pstmt.setString(2, obj.getMensajeerror());
			pstmt.setString(3, obj.getUsuario());
			pstmt.setString(4, obj.getModulo());
			pstmt.setString(5, obj.getDescripcion());
			pstmt.setString(6, obj.getObjeto());
			
			if (pstmt.executeUpdate() > 0)
				return true;
			else
				return false;
		} catch (Exception e) {			
			Logger.getLogger(DetalleLogDAO.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}finally {
			try {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(DetalleLogDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(DetalleLogDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(DetalleLogDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

	}

}
