package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.zkoss.zk.ui.Sessions;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBAgenciaVirtualFisicaModel;
import com.terium.siccam.sql.ConsultasSQ;
import com.terium.siccam.utils.ObtenerSQLPais;

/**
 * Modifica Juankrlos 10/12/2017
 */
public class CBAgenciaVirFiscDAO {
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	
	public void agregarAgenciaVirFis(CBAgenciaVirtualFisicaModel objModel) {
		String paisConexion = misession.getAttribute("conexion").toString();
		String agregarAgenciaVirFis = ObtenerSQLPais.getValorSQL(paisConexion, 1);
		Object[] param = new Object[] { objModel.getIdAgencia(), objModel.getCodigo(), objModel.getNombre(),
				objModel.getTipo(), objModel.getUsuario() };
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Valor objeto param = " + param);
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Query agregar agencia vir fis = " + agregarAgenciaVirFis);
			QueryRunner qr = new QueryRunner();
			qr.update(con, agregarAgenciaVirFis, param);
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}
	
	public List<CBAgenciaVirtualFisicaModel> listadoAgencias(int idAgencia) {
		String paisConexion = misession.getAttribute("conexion").toString();
		String listadoAgencias = ObtenerSQLPais.getValorSQL(paisConexion, 2);
		listadoAgencias += " ORDER BY CODIGO ASC";
		List<CBAgenciaVirtualFisicaModel> listado = new ArrayList<CBAgenciaVirtualFisicaModel>();
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBAgenciaVirtualFisicaModel> blh = new BeanListHandler<CBAgenciaVirtualFisicaModel>(
					CBAgenciaVirtualFisicaModel.class);
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Valor id agencia = " + idAgencia);
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Query listado agencias vir fis = " + listadoAgencias);
			listado = qr.query(con, listadoAgencias , blh, new Object[] { idAgencia });
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return listado;
	}

	public void modificarAgencia(CBAgenciaVirtualFisicaModel objModel) {
		String updateSQL = ConsultasSQ.MODIFICAR_AGENCIA;
		Object[] param = new Object[] { objModel.getCodigo(), objModel.getNombre(), objModel.getTipo(),
				objModel.getUsuario(), objModel.getCbAgenciaVirfiscid() };
		/*
		System.out.println("nombre modificar " + objModel.getNombre());
		System.out.println("tipo modificar " + objModel.getTipo());
		System.out.println("codigo modificar " + objModel.getCodigo());
		System.out.println("usuario modificar " + objModel.getUsuario());
		System.out.println("idagencia modificar " + objModel.getIdAgencia());
		*/
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Valores objeto param = " + param);
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Update agencia vir fis = " + updateSQL);
			qr.update(con, updateSQL, param);
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}

	public void eliminarAgencia(int idFila) {
		String deleteSQL = ConsultasSQ.ELIMINAR_AGENCIA;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Valor idFila = " + idFila);
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.INFO,
					"Delete agencia vir fis = " + deleteSQL);
			qr.update(con, deleteSQL, idFila);
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaVirFiscDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}
}
