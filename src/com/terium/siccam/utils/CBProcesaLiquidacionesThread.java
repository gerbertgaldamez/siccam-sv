package com.terium.siccam.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;

/**
 * @author Carlos Godinez - Qitcorp -13/05/2017
 * */
public class CBProcesaLiquidacionesThread extends ControladorBase implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fechaPago;
	private int estado;
	private String creador;
	private Connection conn;
	
	public CBProcesaLiquidacionesThread( String fechaPago, int estado, String creador){
		Connection conn = null;
		 try {
			conn = obtenerDtsPromo().getConnection();
		} catch (SQLException e) {
			Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
		}
		this.conn = conn;
		this.fechaPago = fechaPago;
		this.estado = estado;
		this.creador = creador;
	}

	public void run() {
		System.out.println("Ejecuta SP masiva de liquidaciones");
		if(ejecutaSPMasiva()){
			System.out.println("*** SP Masiva de liquidaciones ejecutado con exito. ***");
			if(ejecutaSPCargaLiquidaciones()){
				System.out.println("*** SP Carga de liquidaciones ejecutado con exito. ***");
			} else {
				System.out.println("Error al ejecutar SP masiva de liquidaciones");
			}
		} else {
			System.out.println("Error al ejecutar SP carga de liquidaciones");
		}
	}
	
	String QRY_FORMATO_FECHA = "SELECT TO_CHAR(TO_DATE(?,'dd/MM/yy'), ?) FROM DUAL";
	
	private String obtenerFormatoFecha(String valor, String formatoDeseado){
		String resultado = null;
		PreparedStatement cmd = null;
		ResultSet rs = null;
		try {
				System.out.println("Consulta para obtener formato fecha = " + QRY_FORMATO_FECHA);
				System.out.println("Valor fecha = " + valor);
				System.out.println("Formato deseado = " + formatoDeseado);
				cmd = conn.prepareStatement(QRY_FORMATO_FECHA);
				cmd.setString(1, valor);
				cmd.setString(2, formatoDeseado);
				rs = cmd.executeQuery();
				while(rs.next()){
					resultado = rs.getString(1);
				}
		} catch (Exception e1) {
			Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e1);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
				}
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}
	
	private String SP_MASIVA = "{CALL CB_CONCILIACION_CAJAS_PKG.CB_LIQUIDACIONES_MASIVA_SP(?,?,?,?)}";
	
	public boolean ejecutaSPMasiva() {
		boolean result = false;
		CallableStatement cmd = null;
		try {
				String fecha = obtenerFormatoFecha(fechaPago, "dd-MM-yyyy");
				System.out.println("Fecha Inicio = " + fecha);
				System.out.println("Fecha Fin = " + fecha);
				System.out.println("Estado = " + estado);
				System.out.println("Creador  = " + creador);
				cmd = conn.prepareCall(SP_MASIVA);
				cmd.setString(1, fecha);
				cmd.setString(2, fecha);
				cmd.setInt(3, estado);
				cmd.setString(4, creador);
				cmd.execute();
				result = true;
			
		} catch (Exception e) {
			Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	
	private String SP_CARGA_LIQUIDACIONES = "{CALL CB_CONCILIACION_CAJAS_PKG.CB_CARGA_LIQUIDACIONES_SP( to_date(?,'dd/MM/yyyy'))}";
	
	public boolean ejecutaSPCargaLiquidaciones(){
		boolean result = false;
		CallableStatement cmd = null;
		try {
				String fecha = obtenerFormatoFecha(fechaPago, "dd/MM/yyyy");
				System.out.println("Fecha enviada para segundo SP = " + fecha);
				cmd = conn.prepareCall(SP_CARGA_LIQUIDACIONES);
				cmd.setString(1, fecha);
				cmd.execute();
				result = true;
			
		}catch (Exception e) {
			Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
				}
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBProcesaLiquidacionesThread.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
}
