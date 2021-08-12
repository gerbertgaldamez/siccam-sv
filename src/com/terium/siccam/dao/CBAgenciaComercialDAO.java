package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBAgenciaComercialModel;
import com.terium.siccam.model.ListaCombo;
import com.terium.siccam.sql.ConsultasSQ;

/**
 * @author aaron4431
 * 
 */
public class CBAgenciaComercialDAO {

	// Consulta lista prepago
	public List<ListaCombo> listaComboPre() {
		String listaAgeComercialPre = ConsultasSQ.CONSULTA_LISTA_AGECOMERCIALPRE;
		List<ListaCombo> listado = new ArrayList<ListaCombo>();
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			QueryRunner qr = new QueryRunner();
			BeanListHandler<ListaCombo> bhl = new BeanListHandler<ListaCombo>(ListaCombo.class);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Query listaComboPre() = " + listaAgeComercialPre);
			listado = qr.query(con, listaAgeComercialPre, bhl, new Object[] {});
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return listado;
	}

	//Consulta lista postpago
	public List<ListaCombo> listaComboPos() {
		String listaAgeComercialPos = ConsultasSQ.CONSULTA_LISTA_AGECOMERCIALPOS;
		List<ListaCombo> listado = new ArrayList<ListaCombo>();
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
				QueryRunner qr = new QueryRunner();
				BeanListHandler<ListaCombo> bhl = new BeanListHandler<ListaCombo>(ListaCombo.class);
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
						"Query listaComboPos() = " + listaAgeComercialPos);
				listado = qr.query(con, listaAgeComercialPos , bhl, new Object[] {});
		} catch (Exception e) {			
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return listado;
	}

	//Obtiene valores para prepago y pospago
	public int consultaSiEsPreOPos(String idUnionConfrontas) {
		String consultaPosPre = ConsultasSQ.CONSULTA_POS_PRE;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int resultado = 0;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor idUnionConfrontas = " + idUnionConfrontas);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Query consultaSiEsPreOPos([...]) = " + consultaPosPre);
			ps = con.prepareStatement(consultaPosPre);
			ps.setString(1, idUnionConfrontas);
			rs = ps.executeQuery();
			if (rs.next())
				resultado = rs.getInt("tipo");
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return resultado;
	}

	/**
	 * Editado ultima vez por Carlos Godinez - QitCorp - 21/04/2017
	 * @category insertar data en CB_AGENCIAS_CONFRONTA
	 * @throws SQLException 
	 * */
	public boolean insertaData(String idUnionConfrontas, String valorCombo)  {
		String insertSQL = ConsultasSQ.INSERTA_DATA;
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor idUnionConfrontas = " + idUnionConfrontas);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor valorCombo = " + valorCombo);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Insert agencia comercial = " + insertSQL);
			cmd = conn.prepareStatement(insertSQL);
			cmd.setString(1, idUnionConfrontas);
			cmd.setString(2, valorCombo);
			if(cmd.executeUpdate() > 0)
				result = true;
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"\n*** Se ha agregado registro con exito ***\n");
		} catch (SQLException e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)
					cmd.close();
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// valida si los datos ya fueron insertados en cb_agencias_confronta
	public String validarExistencia(String idUnionConfrontas, String valorCombo) {
		String qryValidarExistencia = ConsultasSQ.VALIDAR_EXISTENCIA;
		String existe = "";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor idUnionConfrontas = " + idUnionConfrontas);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor valorCombo = " + valorCombo);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Query validar existencia agencia comercial = " + qryValidarExistencia);
			ps = con.prepareStatement(qryValidarExistencia);
			ps.setString(1, idUnionConfrontas);
			ps.setString(2, valorCombo);
			rs = ps.executeQuery();
			if (rs.next()) 
				existe = rs.getString(1);				
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return existe;
	}
	
	public boolean modificaData(String idUnionConfrontas, String valorCombo, String pk) {
		String updateSQL = ConsultasSQ.ACTUALIZAR_AGENCIA_COMERCIAL;
		boolean result = false;
		Connection conn = null;
		PreparedStatement cmd = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor idUnionConfrontas = " + idUnionConfrontas);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor valorCombo = " + valorCombo);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Valor pk = " + pk);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Query modificar agencia comercial = " + updateSQL);
			cmd = conn.prepareStatement(updateSQL);
			cmd.setString(1, valorCombo);
			cmd.setString(2, idUnionConfrontas);
			cmd.setString(3, pk);
			if(cmd.executeUpdate() > 0)
				result = true;
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"\n*** Registro modificado con exito ***\n");
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if(cmd != null)
					cmd.close();
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// Editado por Carlos Godinez - Qitcorp - 28/04/2017
	public List<CBAgenciaComercialModel> listadoAgenComPosPre(String idUnionConfrontas) {
		String listadoAgeComPrePos = ConsultasSQ.LISTADO_AGE_COM_PRE_POS;
		List<CBAgenciaComercialModel> listado = new ArrayList<CBAgenciaComercialModel>();
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Id agencia enviado = " + idUnionConfrontas);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Query consulta agencias comerciales = " + listadoAgeComPrePos);
			QueryRunner qr = new QueryRunner();
			BeanListHandler<CBAgenciaComercialModel> blh = new BeanListHandler<CBAgenciaComercialModel>(
					CBAgenciaComercialModel.class);
			listado = qr.query(con, listadoAgeComPrePos, blh, new Object[] { idUnionConfrontas });
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return listado;
	}

	// Eliminar Agencia comercial de CB_AGENCIAS_CONFRONTA
	public boolean eliminarAgeCom(String idFila) {
		String deleteSQL = ConsultasSQ.ELIMINAR_AGENCIA_COMERCIAL;
		boolean result = false;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO, "Valor idFila = " + idFila);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Query eliminar agencia comercial = " + deleteSQL);
			QueryRunner qr = new QueryRunner();
			qr.update(con, deleteSQL, idFila);
			result = true;
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// Eliminar Agencia comercial de CB_AGENCIAS_CONFRONTA
	public void eliminarTodasAgeCom(String idFila) {
		String deleteSQL = ConsultasSQ.ELIMINAR_TODAS_AGENCIA_COMERCIAL;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO, "Valor idFila = " + idFila);
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.INFO,
					"Query eliminar todas agencias comerciales = " + deleteSQL);
			QueryRunner qr = new QueryRunner();
			qr.update(con, deleteSQL, idFila);
		} catch (Exception e) {
			Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				Logger.getLogger(CBAgenciaComercialDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}

}
