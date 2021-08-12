package com.terium.siccam.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBAplicaDesaplicaModel;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBPagosModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.sql.ConsultasSQ;

/**
 * @author Ovidio Santos
 */
public class CBAplicaDesaplicaPagosDAO {

	public List<CBConciliacionCajasModel> generaConsultaBanco() {
		String obtieneBancoSQ = ConsultasSQ.OBTIENE_BANCO_SQ;
		List<CBConciliacionCajasModel> list = new ArrayList<CBConciliacionCajasModel>();
		Statement stm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Query obtiene banco SQ = " + obtieneBancoSQ);
			stm = con.createStatement();
			rs = stm.executeQuery(obtieneBancoSQ);
			CBConciliacionCajasModel obj = null;
			while (rs.next()) {
				obj = new CBConciliacionCajasModel();
				obj.setIdcombo(rs.getInt(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}

	public List<CBCatalogoAgenciaModel> generaConsultaAgencia(int idBanco) {
		String obtieneAgenciaSQ = ConsultasSQ.OBTIENE_AGENCIA_SQ;
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		Statement stm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			
			String where = "and b.cbcatalogobancoid = " + idBanco + "";
			obtieneAgenciaSQ += where;
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Valor idBanco = " + idBanco);
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Query obtiene agencia SQ = " + obtieneAgenciaSQ);
				stm = con.createStatement();
				rs = stm.executeQuery(obtieneAgenciaSQ + where);
			CBCatalogoAgenciaModel obj = null;
			while (rs.next()) {
				obj = new CBCatalogoAgenciaModel();
				obj.setcBCatalogoAgenciaId(rs.getString(1));
				obj.setNombre(rs.getString(2));
				list.add(obj);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally{
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}

	public List<CBPagosModel> generaConsultaTipo() {
		String obtieneTipoSQ = ConsultasSQ.OBTIENE_TIPO_SQ;
		List<CBPagosModel> list = new ArrayList<CBPagosModel>();
		Statement stm = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Query obtiene tipo SQ = " + obtieneTipoSQ);
			stm = con.createStatement();
			rs = stm.executeQuery(obtieneTipoSQ);
			CBPagosModel obj = null;
			while (rs.next()) {
				obj = new CBPagosModel();
				obj.setCBPagosId(rs.getInt(1));
				obj.setDesPago(rs.getString(2));
				list.add(obj);
			}
		} catch (SQLException e) {
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (stm != null)
				try {
					stm.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}

	// consulta archivos cargados
	public List<CBAplicaDesaplicaModel> obtieneListaArchivosCargados(String tipo, int agencia, String estado,
			String fechaDesde, String fechaHasta) {
		String archivosCargadosSQL = ConsultasSQ.CONSULTA_ARCHIVOS_CARGADOS;
		List<CBAplicaDesaplicaModel> list = new ArrayList<CBAplicaDesaplicaModel>();
		String where = "";
		if (agencia != 0) {
			where += "and cbcatalogoagenciaid = '" + agencia + "' ";
		}
		if (tipo != null && !"TODOS".equals(tipo)) {
			where += "and VALOR_TIPO = '" + tipo + "' ";
		}
		if (estado != null && !"TODOS".equals(estado)) {
			where += "and estado = '" + estado + "' ";
		}
		where += " and dia >= to_date('" + fechaDesde + "','dd/MM/yyyy') " + " and dia <= to_date('" + fechaHasta
				+ "','dd/MM/yyyy') ";
		archivosCargadosSQL += where;
		Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO, "Datos en combo = " + estado);
		Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
				"Consulta archivos cargados = " + archivosCargadosSQL);
		Statement cmd = null;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			cmd = con.createStatement();
			cmd.setFetchSize(1024);
			rs = cmd.executeQuery(archivosCargadosSQL);
			while (rs.next()) {
				CBAplicaDesaplicaModel objModel = new CBAplicaDesaplicaModel();
				objModel.setAgencia(rs.getString(1));
				objModel.setDia(rs.getDate(2));
				objModel.setTipo(rs.getString(3));
				objModel.setCliente(rs.getString(4));
				objModel.setNombre(rs.getString(5));
				objModel.setDesPago(rs.getString(6));
				objModel.setTransTelca(rs.getString(7));
				objModel.setTelefono(rs.getString(8));
				objModel.setTransBanco(rs.getString(9));
				objModel.setImpPago(rs.getBigDecimal(10));
				objModel.setMonto(rs.getBigDecimal(11));
				// System.out.println("en el dao monto " + objModel.getMonto());
				objModel.setManual(rs.getBigDecimal(12));
				objModel.setPendiente(rs.getString(13));
				objModel.setEstado(rs.getString(14));
				objModel.setConciliacionId(rs.getString(15));
				objModel.setSucursal(rs.getString(16));
				objModel.setComision(rs.getBigDecimal(17));
				objModel.setNombre_sucursal(rs.getString(18));
				objModel.setTipo_sucursal(rs.getString(19));
				objModel.setMonto_comision(rs.getBigDecimal(20));
				objModel.setCbcatalogoagenciaid(rs.getString(21));

				list.add(objModel);
			}
		} catch (Exception e) {
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}

	public List<CBParametrosGeneralesModel> obtenerEstadoCmb(String tipoObjeto) {
		String obtieneEstadosSQL = ConsultasSQ.QRY_OBTIENE_ESTADOS;
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Valor tipoObjeto= " + tipoObjeto);
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Consulta obtiene estados = " + obtieneEstadosSQL);
			ps = con.prepareStatement(obtieneEstadosSQL);
			ps.setString(1, tipoObjeto);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setObjeto(rs.getString(1));
				obj.setValorObjeto1(rs.getString(2));
				lista.add(obj);
			}
		} catch (Exception e) {
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	

	public List<CBParametrosGeneralesModel> obtenerTipoCmb(String tipoObjeto) {
		String obtieneTipoSQL = ConsultasSQ.QRY_OBTIENE_TIPO;
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Valor tipoObjeto= " + tipoObjeto);
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Consulta obtiene tipo = " + obtieneTipoSQL);
			ps = con.prepareStatement(obtieneTipoSQL);
			ps.setString(1, tipoObjeto);
			rs = ps.executeQuery();
			CBParametrosGeneralesModel obj = null;
			while (rs.next()) {
				obj = new CBParametrosGeneralesModel();
				obj.setObjeto(rs.getString(1));
				obj.setValorObjeto1(rs.getString(2));
				lista.add(obj);
			}
		} catch (Exception e) {
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	
	public List<CBAplicaDesaplicaModel> obtienDatosSAP2(String tipo, int agencia, String estado, String fechaDesde,
			String fechaHasta) {
		List<CBAplicaDesaplicaModel> list = new ArrayList<CBAplicaDesaplicaModel>();
		String where = "";
		// if (banco != null && !banco.equals("")) {
		// where += "and banco = '" + banco + "' ";
		// }
		if (agencia != 0) {
			where += "and cbcatalogoagenciaid = '" + agencia + "' ";
		}
		if (tipo != null && !"TODOS".equals(tipo)) {
			where += "and tipo = '" + tipo + "' ";
		}
		if (estado != null && !"TODOS".equals(estado)) {
			where += "and estado = '" + estado + "' ";
		}
		where += " and dia >= to_date('" + fechaDesde + "','dd/MM/yyyy') " + " and dia <= to_date('" + fechaHasta
				+ "','dd/MM/yyyy') ";
		Connection con = null;
		Statement cmd = null;
		ResultSet rs = null;
		try {
			con = ControladorBase.obtenerDtsPromo().getConnection();
			cmd = con.createStatement();
			String obtieneDatosSAP3 = ConsultasSQ.OBTIENE_DATOS_SAP3;
			obtieneDatosSAP3 += where;
			cmd.setFetchSize(1024);
			rs = cmd.executeQuery(obtieneDatosSAP3);
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Valor fechaDesde = " + fechaDesde);
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Valor fechaHasta = " + fechaHasta);
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Query generacion obtienDatosSAP2= " + obtieneDatosSAP3);
			CBAplicaDesaplicaModel obj = null;
			while (rs.next()) {
				obj = new CBAplicaDesaplicaModel();
				obj.setIdSAP(rs.getInt(1));
				obj.setLineaSAP(rs.getString(2));
				list.add(obj);
			}
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.INFO,
					"Tamano de lista obtiene datos SAP = " + list.size());
		} catch (SQLException e) {
			Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (cmd != null)
				try {
					cmd.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBAplicaDesaplicaPagosDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return list;
	}
	
}
