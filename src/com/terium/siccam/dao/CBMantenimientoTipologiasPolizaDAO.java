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
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBMantenimientoPolizaModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.CBTipologiasEntidadesModel;
import com.terium.siccam.utils.ConsultasSQ;

@SuppressWarnings("serial")
public class CBMantenimientoTipologiasPolizaDAO extends ControladorBase {

	/**
	 * Inserta los registros para mantenimiento poliza Agregado por Ovidio Santos -
	 * 04/05/2017 -
	 */
	public boolean insertTipologiasPoliza(CBMantenimientoPolizaModel objModel){

		boolean result = false;
		PreparedStatement cmd = null;
		Connection con = null;
	
			

			try {
				con = obtenerDtsPromo().getConnection();

				cmd = con.prepareStatement(ConsultasSQ.INSERT_TIPOLOGIAS_POLIZA_SQ);

				// cmd.setInt(1, objModel.getCbtipologiaspolizaid());
				cmd.setString(1, objModel.getCentroCosto());
				cmd.setString(2, objModel.getCuentaContrapartida());
				cmd.setString(3, objModel.getClaveContabilizacion());
				cmd.setString(4, objModel.getIndicadorIva());
				cmd.setString(5, objModel.getTerminacion());
				cmd.setString(6, objModel.getActividad());
				cmd.setString(7, objModel.getDescripcion());
				cmd.setString(8, objModel.getNombre());
				cmd.setString(9, objModel.getCentroCostocp());
				cmd.setString(10, objModel.getClaveContabilizacioncp());
				cmd.setString(11, objModel.getDescripcioncp());
				cmd.setString(12, objModel.getTerminacioncp());
				cmd.setString(13, objModel.getCuenta_Ingreso());

				cmd.setString(14, objModel.getCentroCostodf());
				cmd.setString(15, objModel.getClaveContabilizaciondf());
				cmd.setString(16, objModel.getDescripciondf());
				cmd.setString(17, objModel.getTerminaciondf());
				cmd.setString(18, objModel.getCuenta_Ingresodf());

			cmd.setString(19, objModel.getClaveDiferenciaNegativa());
			
			cmd.setString(20, objModel.getIndicadorivacp());
			cmd.setString(21, objModel.getActividadcp());

			cmd.setString(22, objModel.getUsuario());

			cmd.setString(23, objModel.getConvenio());
			cmd.setString(24, objModel.getPide_Entidad());
			cmd.setString(25, objModel.getSecuencia());
			cmd.setString(26, objModel.getTipodocumento());
			cmd.setString(27, objModel.getIdentificacion());
			cmd.setString(28, objModel.getTipo());

			if (cmd.executeUpdate() > 0) {
				result = true;
			}


		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(con != null)con.close();
			}catch (Exception e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	// consulta llena lisbox

	public List<CBMantenimientoPolizaModel> obtenerTipologias(CBMantenimientoPolizaModel objModel){
		List<CBMantenimientoPolizaModel> list = new ArrayList<CBMantenimientoPolizaModel>();
	
		Connection con = null;
			ResultSet rs = null;
			Statement cmd = null;
			try {
				con = obtenerDtsPromo().getConnection();
			String query = ConsultasSQ.CONSULTA_TIPOLOGIAS_POLIZA_SQ;
			String where = " ";

			if (objModel.getCentroCosto() != null && !objModel.getCentroCosto().equals("")) {

				where += "AND UPPER (CENTRO_COSTO) LIKE '%" + objModel.getCentroCosto().toUpperCase() + "%' ";

			}
			if (objModel.getClaveContabilizacion() != null && !objModel.getClaveContabilizacion().equals("")) {

				where += "AND UPPER (CLAVE_CONTABILIZACION) LIKE '%" + objModel.getClaveContabilizacion().toUpperCase()
						+ "%' ";

			}

			if (objModel.getDescripcion() != null && !objModel.getDescripcion().equals("")) {

				where += "AND UPPER (DESCRIPCION) LIKE '%" + objModel.getDescripcion().toUpperCase() + "%' ";

			}

			if (objModel.getTerminacion() != null && !objModel.getTerminacion().equals("")) {
				where += "AND UPPER (TERMINACION) LIKE '%" + objModel.getTerminacion().toUpperCase() + "%' ";
			}

			if (objModel.getCuenta_Ingreso() != null && !objModel.getCuenta_Ingreso().equals("")) {
				where += "AND UPPER (CUENTA_INGRESO) LIKE '%" + objModel.getCuenta_Ingreso().toUpperCase() + "%' ";
			}

			if (objModel.getNombre() != null && !objModel.getNombre().equals("")) {
				where += "AND UPPER (NOMBRE) LIKE '%" + objModel.getNombre().toUpperCase() + "%' ";
			}

			if (objModel.getCentroCostocp() != null && !objModel.getCentroCostocp().equals("")) {
				where += "AND UPPER (CENTRO_COSTO_CP) LIKE '%" + objModel.getCentroCostocp().toUpperCase() + "%' ";
			}

			if (objModel.getClaveContabilizacioncp() != null && !objModel.getClaveContabilizacioncp().equals("")) {
				where += "AND UPPER (CLAVE_CONTABILIZACION_CP) LIKE '%"
						+ objModel.getClaveContabilizacioncp().toUpperCase() + "%' ";
			}

			if (objModel.getDescripcioncp() != null && !objModel.getDescripcioncp().equals("")) {
				where += "AND UPPER (DESCRIPCION_CP) LIKE '%" + objModel.getDescripcioncp().toUpperCase() + "%' ";
			}

			if (objModel.getTerminacioncp() != null && !objModel.getTerminacioncp().equals("")) {
				where += "AND UPPER (TERMINACION_CP) LIKE '%" + objModel.getTerminacioncp().toUpperCase() + "%' ";
			}

			if (objModel.getCuentaContrapartida() != null && !objModel.getCuentaContrapartida().equals("")) {
				where += "AND UPPER (CUENTA_CONTRAPARTIDA) LIKE '%" + objModel.getCuentaContrapartida().toUpperCase()
						+ "%' ";
			}

			if (objModel.getIndicadorIva() != null && !objModel.getIndicadorIva().equals("")) {
				where += "AND UPPER (INDICADOR_IVA) LIKE '%" + objModel.getIndicadorIva().toUpperCase() + "%' ";
			}

			if (objModel.getActividad() != null && !objModel.getActividad().equals("")) {
				where += "AND UPPER (ACTIVIDAD) LIKE '%" + objModel.getActividad().toUpperCase() + "%' ";
			}

			if (objModel.getCentroCostodf() != null && !objModel.getCentroCostodf().equals("")) {
				where += "AND UPPER (CENTRO_COSTO_DF) LIKE '%" + objModel.getCentroCostodf().toUpperCase() + "%' ";
			}

			if (objModel.getClaveContabilizaciondf() != null && !objModel.getClaveContabilizaciondf().equals("")) {
				where += "AND UPPER (CLAVE_CONTABILIZACION_DF) LIKE '%"
						+ objModel.getClaveContabilizaciondf().toUpperCase() + "%' ";
			}

			if (objModel.getDescripciondf() != null && !objModel.getDescripciondf().equals("")) {
				where += "AND UPPER (DESCRIPCION_DF) LIKE '%" + objModel.getDescripciondf().toUpperCase() + "%' ";
			}

			if (objModel.getTerminaciondf() != null && !objModel.getTerminaciondf().equals("")) {
				where += "AND UPPER (TERMINACION_DF) LIKE '%" + objModel.getTerminaciondf().toUpperCase() + "%' ";
			}

			if (objModel.getCuenta_Ingresodf() != null && !objModel.getCuenta_Ingresodf().equals("")) {
				where += "AND UPPER (CUENTA_INGRESO_DF) LIKE '%" + objModel.getCuenta_Ingresodf().toUpperCase() + "%' ";
			}

			if (objModel.getClaveDiferenciaNegativa() != null && !objModel.getClaveDiferenciaNegativa().equals("")) {
				where += "AND UPPER (CLAVE_CONTABILIZACION_NEG) LIKE '%"
						+ objModel.getClaveDiferenciaNegativa().toUpperCase() + "%' ";
			}
			if (objModel.getIndicadorivacp() != null && !objModel.getIndicadorivacp().equals("")) {
				where += "AND UPPER (INDICADOR_IVA_CP) LIKE '%" + objModel.getIndicadorivacp().toUpperCase() + "%' ";
			}

			if (objModel.getActividadcp() != null && !objModel.getActividadcp().equals("")) {
				where += "AND UPPER (ACTIVIDAD_CP) LIKE '%"
						+ objModel.getActividadcp().toUpperCase() + "%' ";
			}

			if (objModel.getConvenio() != null && !objModel.getConvenio().equals("")) {
				where += "AND UPPER (TIPO) LIKE '%" + objModel.getConvenio().toUpperCase() + "%' ";
			}
			
			if (objModel.getTipo() != null && !objModel.getTipo().equals("")) {
				where += "AND UPPER (TIPO_IMPUESTO) LIKE '%" + objModel.getTipo().toUpperCase() + "%' ";
			}

			if (objModel.getPide_Entidad() != null && !objModel.getPide_Entidad().equals("")) {
				where += "AND UPPER (PIDE_ENTIDAD) LIKE '%" + objModel.getPide_Entidad().toUpperCase() + "%' ";
			}
			if (objModel.getSecuencia() != null && !objModel.getSecuencia().equals("")) {
				where += "AND UPPER (SECUENCIA) LIKE '%" + objModel.getSecuencia().toUpperCase() + "%' ";
			}
			if (objModel.getTipodocumento() != null && !objModel.getTipodocumento().equals("")) {
				where += "AND UPPER (tipo_documento) LIKE '%" + objModel.getTipodocumento().toUpperCase() + "%' ";
			}

			if (objModel.getIdentificacion() != null && !objModel.getIdentificacion().equals("")) {
				where += "AND UPPER (identificacion) LIKE '%" + objModel.getIdentificacion().toUpperCase() + "%' ";
			}


			// ps =
			// con.prepareStatement(ConsultasSQ.CONSULTA_TIPOLOGIAS_POLIZA_SQ);
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
				.log(Level.INFO,"consulta " + query + where);

			cmd = con.createStatement();
			where = where + " order by CBTIPOLOGIASPOLIZAID asc";
			rs = cmd.executeQuery(query + where );
			// CBMantenimientoPolizaModel objModel = null;
			while (rs.next()) {
				objModel = new CBMantenimientoPolizaModel();
				objModel.setCbtipologiaspolizaid(rs.getInt(1));
				objModel.setCentroCosto(rs.getString(2));
				objModel.setClaveContabilizacion(rs.getString(3));
				objModel.setDescripcion(rs.getString(4));
				objModel.setTerminacion(rs.getString(5));

				objModel.setCuenta_Ingreso(rs.getString(6));
				objModel.setNombre(rs.getString(7));

				objModel.setCentroCostocp(rs.getString(8));
				objModel.setClaveContabilizacioncp(rs.getString(9));
				objModel.setDescripcioncp(rs.getString(10));
				objModel.setTerminacioncp(rs.getString(11));
				objModel.setCuentaContrapartida(rs.getString(12));
				objModel.setIndicadorIva(rs.getString(13));

				objModel.setActividad(rs.getString(14));

				objModel.setCentroCostodf(rs.getString(15));
				objModel.setClaveContabilizaciondf(rs.getString(16));
				objModel.setDescripciondf(rs.getString(17));
				objModel.setTerminaciondf(rs.getString(18));

				objModel.setCuenta_Ingresodf(rs.getString(19));

				objModel.setClaveDiferenciaNegativa(rs.getString(20));
				objModel.setIndicadorivacp(rs.getString(21));

				objModel.setActividadcp(rs.getString(22));

				objModel.setConvenio(rs.getString(23));
				objModel.setPide_Entidad(rs.getString(24));
				
				
				objModel.setCentrodebeneficio(rs.getString(25));
				objModel.setDivision(rs.getString(26));
				objModel.setOrdendeproyecto(rs.getString(27));
				objModel.setTipodecambio(rs.getString(28));

				objModel.setFechadecomversion(rs.getString(29));
				objModel.setIndicadorcme(rs.getString(30));

				objModel.setCarpasegmento(rs.getString(31));
				objModel.setCarpaservicio(rs.getString(32));
				objModel.setCarpatipotrafico(rs.getString(33));
				objModel.setCarpaambito(rs.getString(34));
				objModel.setCarpalicencia(rs.getString(35));
				objModel.setCarparegion(rs.getString(36));

				objModel.setSubtipolinea(rs.getString(37));

				objModel.setCanal(rs.getString(38));
				objModel.setBundle(rs.getString(39));
				objModel.setProducto(rs.getString(40));
				objModel.setEmpresagrupo(rs.getString(41));
				objModel.setSecuencia(rs.getString(42));
				objModel.setTipodocumento(rs.getString(43));
				objModel.setIdentificacion(rs.getString(44));
				objModel.setTipo(rs.getString(45));
				
				objModel.setProyecto(rs.getString(46));
				objModel.setSociedadasociada(rs.getString(47));
				objModel.setGrafo1(rs.getString(48));
				objModel.setGrafo2(rs.getString(49));
				objModel.setSubsegmento(rs.getString(50));
				objModel.setRef1(rs.getString(51));
				objModel.setRef2(rs.getString(52));
				
				objModel.setTcode(rs.getString(53));
				objModel.setProc(rs.getString(54));
				objModel.setLlave(rs.getString(55));
				objModel.setCalc_auto_iva(rs.getString(56));
				objModel.setRef3(rs.getString(57));
				objModel.setSociedad(rs.getString(58));
				objModel.setFecha_valor(rs.getString(59));
				list.add(objModel);
			}
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rs != null)
					try {
						rs.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(cmd != null)
					try {
						cmd.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(con != null)
					try {
						con.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return list;

	}
	
	
	// consulta llena lisbox

		public List<CBMantenimientoPolizaModel> obtenerTipologiasModal(CBMantenimientoPolizaModel objModel){
			Connection con = null;
			ResultSet rs = null;
			Statement cmd = null;
			List<CBMantenimientoPolizaModel> list = new ArrayList<CBMantenimientoPolizaModel>();
			try {

				con = obtenerDtsPromo().getConnection();
				String query = ConsultasSQ.CONSULTA_TIPOLOGIAS_POLIZA_MODAL_SQ;
				String where = " ";

				if (objModel.getCentrodebeneficio() != null && !objModel.getCentrodebeneficio().equals("")) {

					where += "AND UPPER (CENTRO_DE_BENEFICIO) LIKE '%" + objModel.getCentrodebeneficio().toUpperCase() + "%' ";

				}
				if (objModel.getDivision() != null && !objModel.getDivision().equals("")) {

					where += "AND UPPER (DIVISION) LIKE '%" + objModel.getDivision().toUpperCase()
							+ "%' ";

				}

				if (objModel.getOrdendeproyecto() != null && !objModel.getOrdendeproyecto().equals("")) {

					where += "AND UPPER (ORDEN_DE_PROYECTO) LIKE '%" + objModel.getOrdendeproyecto().toUpperCase() + "%' ";

				}

				if (objModel.getTipodecambio() != null && !objModel.getTipodecambio().equals("")) {
					where += "AND UPPER (TIPO_DE_CAMBIO) LIKE '%" + objModel.getTipodecambio().toUpperCase() + "%' ";
				}

				if (objModel.getFechadecomversion() != null && !objModel.getFechadecomversion().equals("")) {
					where += "AND UPPER (FECHA_DE_CONVERSION) LIKE '%" + objModel.getFechadecomversion().toUpperCase() + "%' ";
				}

				if (objModel.getIndicadorcme() != null && !objModel.getIndicadorcme().equals("")) {
					where += "AND UPPER (INDICADOR_CME) LIKE '%" + objModel.getIndicadorcme().toUpperCase() + "%' ";
				}

				if (objModel.getCarpasegmento() != null && !objModel.getCarpasegmento().equals("")) {
					where += "AND UPPER (CAR_PA_SEGMENTO) LIKE '%" + objModel.getCarpasegmento().toUpperCase() + "%' ";
				}

				if (objModel.getCarpaservicio() != null && !objModel.getCarpaservicio().equals("")) {
					where += "AND UPPER (CAR_PA_SERVICIO) LIKE '%"
							+ objModel.getCarpaservicio().toUpperCase() + "%' ";
				}

				if (objModel.getCarpatipotrafico() != null && !objModel.getCarpatipotrafico().equals("")) {
					where += "AND UPPER (CAR_PA_TIPO_TRAFICO) LIKE '%" + objModel.getCarpatipotrafico().toUpperCase() + "%' ";
				}

				if (objModel.getCarpaambito() != null && !objModel.getCarpaambito().equals("")) {
					where += "AND UPPER (CAR_PA_AMBITO) LIKE '%" + objModel.getCarpaambito().toUpperCase() + "%' ";
				}

				if (objModel.getCarpalicencia() != null && !objModel.getCarpalicencia().equals("")) {
					where += "AND UPPER (CAR_PA_LICENCIA) LIKE '%" + objModel.getCarpalicencia().toUpperCase()
							+ "%' ";
				}

				if (objModel.getCarparegion() != null && !objModel.getCarparegion().equals("")) {
					where += "AND UPPER (CAR_PA_REGION) LIKE '%" + objModel.getCarparegion().toUpperCase() + "%' ";
				}

				if (objModel.getSubtipolinea() != null && !objModel.getSubtipolinea().equals("")) {
					where += "AND UPPER (SUBTIPO_LINEA) LIKE '%" + objModel.getSubtipolinea().toUpperCase() + "%' ";
				}

				if (objModel.getCanal() != null && !objModel.getCanal().equals("")) {
					where += "AND UPPER (CANAL) LIKE '%" + objModel.getCanal().toUpperCase() + "%' ";
				}

				if (objModel.getBundle() != null && !objModel.getBundle().equals("")) {
					where += "AND UPPER (BUNDLE) LIKE '%"
							+ objModel.getBundle().toUpperCase() + "%' ";
				}

				if (objModel.getProducto() != null && !objModel.getProducto().equals("")) {
					where += "AND UPPER (PRODUCTO) LIKE '%" + objModel.getProducto().toUpperCase() + "%' ";
				}

				if (objModel.getEmpresagrupo() != null && !objModel.getEmpresagrupo().equals("")) {
					where += "AND UPPER (EMPRESA_GRUPO) LIKE '%" + objModel.getEmpresagrupo().toUpperCase() + "%' ";
				}

				// ps =
				// con.prepareStatement(ConsultasSQ.CONSULTA_TIPOLOGIAS_POLIZA_SQ);
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
					.log(Level.INFO,"consulta " + query + where);

				cmd = con.createStatement();
				where = where + " order by cbtipologiaspolizaid asc ";
				rs = cmd.executeQuery(query + where);
				// CBMantenimientoPolizaModel objModel = null;
				while (rs.next()) {
					objModel = new CBMantenimientoPolizaModel();
					objModel.setCbtipologiaspolizaid(rs.getInt(1));
					objModel.setNombre(rs.getString(2));
					objModel.setCentrodebeneficio(rs.getString(3));
					objModel.setDivision(rs.getString(4));
					objModel.setOrdendeproyecto(rs.getString(5));
					objModel.setTipodecambio(rs.getString(6));

					objModel.setFechadecomversion(rs.getString(7));
					objModel.setIndicadorcme(rs.getString(8));

					objModel.setCarpasegmento(rs.getString(9));
					objModel.setCarpaservicio(rs.getString(10));
					objModel.setCarpatipotrafico(rs.getString(11));
					objModel.setCarpaambito(rs.getString(12));
					objModel.setCarpalicencia(rs.getString(13));
					objModel.setCarparegion(rs.getString(14));

					objModel.setSubtipolinea(rs.getString(15));

					objModel.setCanal(rs.getString(16));
					objModel.setBundle(rs.getString(17));
					objModel.setProducto(rs.getString(18));
					objModel.setEmpresagrupo(rs.getString(19));

					

					
					list.add(objModel);
				}
			} catch (Exception e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}finally {
					if(rs != null)
						try {
							rs.close();
						} catch (SQLException e) {
							Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					if(cmd != null)
						try {
							cmd.close();
						} catch (SQLException e) {
							Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
						}
					if(con != null)
						try {
							con.close();
						} catch (SQLException e) {
							Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
						}
			}
			return list;

		}

	
	
		// metodo modificar
		public boolean modificaTipologiasModal(CBMantenimientoPolizaModel objModel){
			boolean result = false;
			Connection con = null;
			PreparedStatement cmd = null;
			try {
				con = obtenerDtsPromo().getConnection();
				cmd = con.prepareStatement(ConsultasSQ.MODIFICAR_POLIZA_MODAL_SQ);
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
					.log(Level.INFO,"update  " + ConsultasSQ.MODIFICAR_POLIZA_MODAL_SQ);

				cmd.setString(1, objModel.getCentrodebeneficio());
				cmd.setString(2, objModel.getDivision());
				cmd.setString(3, objModel.getOrdendeproyecto());
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
					.log(Level.INFO,"clave contabilizacion " + objModel.getClaveContabilizacion());
				
				cmd.setString(4, objModel.getTipodecambio());
				cmd.setString(5, objModel.getFechadecomversion());
				cmd.setString(6, objModel.getIndicadorcme());
				cmd.setString(7, objModel.getCarpasegmento());
				cmd.setString(8, objModel.getCarpaservicio());
				cmd.setString(9, objModel.getCarpatipotrafico());
				cmd.setString(10, objModel.getCarpaambito());
				cmd.setString(11, objModel.getCarpalicencia());
				cmd.setString(12, objModel.getCarparegion());
				cmd.setString(13, objModel.getSubtipolinea());
				cmd.setString(14, objModel.getCanal());
				cmd.setString(15, objModel.getBundle());
				cmd.setString(16, objModel.getProducto());
				cmd.setString(17, objModel.getEmpresagrupo());
				
				
				
				
				cmd.setString(18, objModel.getProyecto());
				cmd.setString(19, objModel.getSociedadasociada());
				cmd.setString(20, objModel.getGrafo1());
				cmd.setString(21, objModel.getGrafo2());
				cmd.setString(22, objModel.getSubsegmento());
				cmd.setString(23, objModel.getRef1());
				cmd.setString(24, objModel.getRef2());
				
				cmd.setString(25, objModel.getTcode());
				cmd.setString(26, objModel.getProc());
				cmd.setString(27, objModel.getLlave());
				cmd.setString(28, objModel.getCalc_auto_iva());
				cmd.setString(29, objModel.getRef3());
				cmd.setString(30, objModel.getSociedad());
				cmd.setString(31, objModel.getFecha_valor());
				
				cmd.setString(32, objModel.getUsuario());
				cmd.setInt(33, objModel.getCbtipologiaspolizaid());
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
					.log(Level.INFO,"idseleccionado en el dao" + objModel.getCbtipologiaspolizaid());

				System.out.println("fecha en el controlador de modificar " + objModel.getFecha_valor());
				if (cmd.executeUpdate() > 0) {
					result = true;
				}
			}catch (Exception e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}finally {
				try {
					if(cmd != null)cmd.close();
					if(con != null)con.close();
				}catch (Exception e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			return result;
		}

	public double changeString(String cadena) {
		double result = 0.00;
		Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
			.log(Level.INFO,"string: " + cadena);
		try {
			if (cadena != null && !"".equals(cadena)) {
				result = Double.parseDouble(cadena.replace(",", ""));
			}
		} catch (NumberFormatException e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}
		return result;
	}

	// metodo eliminar
	public boolean delete(int objBean){
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
			try {
				con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(ConsultasSQ.DELETE_TIPOLOGIA_POLIZA_SQ);

				ps.setInt(1, objBean);
				if (ps.executeUpdate() > 0) {
					result = true;
				}
		}catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(ps != null)ps.close();
				if(con != null)con.close();
			}catch (Exception e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}

		return result;
	}

	// metodo modificar
	public boolean update(CBMantenimientoPolizaModel objModel, int idseleccionado){
		boolean result = false;
	
			Connection con = null;
			PreparedStatement cmd = null;
			try {
				con = obtenerDtsPromo().getConnection();
				cmd = con.prepareStatement(ConsultasSQ.MODIFICAR_POLIZA_SQ);
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
				.log(Level.INFO,"update  " + ConsultasSQ.MODIFICAR_POLIZA_SQ);

				cmd.setString(1, objModel.getCentroCosto());
				cmd.setString(2, objModel.getCuentaContrapartida());
				cmd.setString(3, objModel.getClaveContabilizacion());
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
				.log(Level.INFO,"clave contabilizacion " + objModel.getClaveContabilizacion());
				cmd.setString(4, objModel.getIndicadorIva());
				cmd.setString(5, objModel.getTerminacion());
				cmd.setString(6, objModel.getActividad());
				cmd.setString(7, objModel.getDescripcion());
				cmd.setString(8, objModel.getNombre());
				cmd.setString(9, objModel.getCentroCostocp());
				cmd.setString(10, objModel.getClaveContabilizacioncp());
				cmd.setString(11, objModel.getDescripcioncp());
				cmd.setString(12, objModel.getTerminacioncp());
				cmd.setString(13, objModel.getCuenta_Ingreso());

				cmd.setString(14, objModel.getCentroCostodf());
				cmd.setString(15, objModel.getClaveContabilizaciondf());
				cmd.setString(16, objModel.getDescripciondf());
				cmd.setString(17, objModel.getTerminaciondf());
				cmd.setString(18, objModel.getCuenta_Ingresodf());

			cmd.setString(19, objModel.getClaveDiferenciaNegativa());
			cmd.setString(20, objModel.getIndicadorivacp());
			cmd.setString(21, objModel.getActividadcp());

			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
				.log(Level.INFO,"convenio en dao " + objModel.getConvenio());
			
			cmd.setString(22, objModel.getConvenio());
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
				.log(Level.INFO,"pide entidad en dao " + objModel.getPide_Entidad());
			
			cmd.setString(23, objModel.getPide_Entidad());
			cmd.setString(24, objModel.getSecuencia());
			cmd.setString(25, objModel.getTipodocumento());
			cmd.setString(26, objModel.getIdentificacion());
			
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
			.log(Level.INFO,"tipo en dao " + objModel.getTipo());
		
		cmd.setString(27, objModel.getTipo());

			cmd.setString(28, objModel.getUsuario());
			cmd.setInt(29, idseleccionado);
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName())
				.log(Level.INFO,"idseleccionado en el dao" + idseleccionado);

			if (cmd.executeUpdate() > 0) {
				result = true;
			}
		}catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			try {
				if(cmd != null)cmd.close();
				if(con != null)con.close();
			}catch (Exception e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return result;
	}

	////////////
	private String QRY_OBTIENE_TIPOS = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'TIPOLOGIAS_POLIZA_TIPO_IMPUESTO' AND TIPO_OBJETO = ? ";

	public List<CBParametrosGeneralesModel> obtenerTipo(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();

		
			ResultSet rs = null;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = obtenerDtsPromo().getConnection();
				System.out.println("query combo tipo:" + QRY_OBTIENE_TIPOS);
				ps = con.prepareStatement(QRY_OBTIENE_TIPOS);
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
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	//////////

	////////////
	private String QRY_OBTIENE_ENTIDAD = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'TIPOLOGIAS_POLIZA_ENTIDAD' AND TIPO_OBJETO = ? ";

	public List<CBParametrosGeneralesModel> obtenerPideEntidad(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();

			ResultSet rs = null;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				
				con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(QRY_OBTIENE_ENTIDAD);
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
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}
	

	private String QRY_OBTIENE_FECHA = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'TIPOLOGIAS_POLIZA_FECHA_VALOR' AND TIPO_OBJETO = ? ";

	public List<CBParametrosGeneralesModel> obtenerFechaValor(String tipoObjeto) {
		List<CBParametrosGeneralesModel> lista = new ArrayList<CBParametrosGeneralesModel>();
		System.out.println("query fecha valor " + QRY_OBTIENE_FECHA );
			ResultSet rs = null;
			PreparedStatement ps = null;
			Connection con = null;
			try {
				con = obtenerDtsPromo().getConnection();
				ps = con.prepareStatement(QRY_OBTIENE_FECHA);
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
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return lista;
	}

	
	/**
	 * Agregado por CarlosGodinez -> 13/08/2018
	 * Asociacion de entidades a tipologias de poliza
	 * */
	
	//Llena combo de agrupaciones
	public List<CBCatalogoBancoModel> obtenerAgrupaciones(){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CBCatalogoBancoModel> list = new ArrayList<CBCatalogoBancoModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			pstmt = conn.prepareStatement(ConsultasSQ.AGRUPACIONES_TIPOL_ENTIDAD_QRY);
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					ConsultasSQ.AGRUPACIONES_TIPOL_ENTIDAD_QRY);
			rs = pstmt.executeQuery();
			CBCatalogoBancoModel objeBean;
			while (rs.next()) {
				objeBean = new CBCatalogoBancoModel();
				objeBean.setCbcatalogobancoid(rs.getString(1));
				objeBean.setNombre(rs.getString(2));
				list.add(objeBean);
			}
		}catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}

	//Llena combo de entidades
	public List<CBCatalogoAgenciaModel> obtenerEntidades(int idagrupacion){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<CBCatalogoAgenciaModel> list = new ArrayList<CBCatalogoAgenciaModel>();
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			pstmt = conn.prepareStatement(ConsultasSQ.ENTIDADES_TIPOL_ENTIDAD_QRY );
			pstmt.setInt(1, idagrupacion); 
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					ConsultasSQ.ENTIDADES_TIPOL_ENTIDAD_QRY );
			rs = pstmt.executeQuery();
			CBCatalogoAgenciaModel objeBean;
			while (rs.next()) {
				objeBean = new CBCatalogoAgenciaModel();
				objeBean.setcBCatalogoAgenciaId(rs.getString(1));
				objeBean.setNombre(rs.getString(2));
				list.add(objeBean);
			}
		}catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if(rs != null)
				try {
					rs.close();
				} catch (SQLException e2) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e2);
				}
			if(pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e1);
				}
			try {
				if(conn != null)
					conn.close();
			} catch (SQLException e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		return list;
	}
	
	/**
	 * Realiza consulta general
	 * @param objModel
	 * @param tipologiaRecuperada
	 * @return lst
	 */
	public List<CBTipologiasEntidadesModel> consultaEntidadesAsociadas(CBTipologiasEntidadesModel objModel, int tipologiaRecuperada){
		List<CBTipologiasEntidadesModel> lst = new ArrayList<CBTipologiasEntidadesModel>();
		Statement stmt = null;
		ResultSet rst = null;
		Connection conn = null;
		CBTipologiasEntidadesModel obj = null;
		String query1 = ConsultasSQ.CONSULTA_TIPOL_ENTIDAD_QRY1; 
		String query2 = ConsultasSQ.CONSULTA_TIPOL_ENTIDAD_QRY2; 
		try {
			String where_subquery = "AND ta.cbtipologiaspolizaid = " + tipologiaRecuperada + " ";
			
			String where = "";
			if(objModel.getCbCatalogoBancoId() != 0) { 
				where += "and x.cbcatalogobancoid = " + objModel.getCbCatalogoBancoId() + " ";
			}
			if(objModel.getCbCatalogoAgenciaId() != 0) { 
				where += "and x.cbcatalogoagenciaid = " + objModel.getCbCatalogoAgenciaId() + " ";
			} 
			if(objModel.getCbTipologiasPolizaId() != 0) { 
				where += "and x.count_tipologias > 0";
			}
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"Consulta entidades asociadas = " + query1 + where_subquery + query2 + where);
			
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			stmt = conn.createStatement();
			rst = stmt.executeQuery(query1 + where_subquery + query2 + where);
			while (rst.next()) {
				obj = new CBTipologiasEntidadesModel();
				obj.setCbCatalogoBancoId(rst.getInt(1));
				obj.setNombreBanco(rst.getString(2));
				obj.setCbCatalogoAgenciaId(rst.getInt(3));
				obj.setNombreAgencia(rst.getString(4));
				obj.setCountTipologias(rst.getInt(5));
				lst.add(obj);
			}
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		}finally {
				if(rst != null)
					try {
						rst.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(stmt != null)
					try {
						stmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
		}
		return lst;
	}
	
	/***
	 * Asociacion masiva de entidades
	 * 
	 * @param listaEntidades
	 * @param tipologiaRecuperada
	 * @param usuario
	 * @return result
	 */
	public boolean asociacionEntidadesTipologia(List<CBTipologiasEntidadesModel> listaEntidades,
			int tipologiaRecuperada, String usuario) {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"Insert masivo para tabla CB_TIPOLOGIAS_AGENCIA: " + ConsultasSQ.INSERT_MASIVO_ENTIDAD_TIPOLOGIA);
			pstmt = conn.prepareStatement(ConsultasSQ.INSERT_MASIVO_ENTIDAD_TIPOLOGIA);
			int exitosas = 0;
			for(CBTipologiasEntidadesModel obj : listaEntidades) {
				pstmt.setInt(1, obj.getCbCatalogoAgenciaId());
				pstmt.setInt(2, tipologiaRecuperada);
				pstmt.setString(3, usuario);
				pstmt.addBatch();
				exitosas++;
			}
			if(exitosas > 0){	
				pstmt.executeBatch(); 
		        Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
		        		"Registros de CB_TIPOLOGIAS_AGENCIA insertados con exito: " + exitosas);
		        result = true;
			}
		} catch (Exception e) {
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		return result;
	}
	/***
	 * Elimina asociacion de entidad
	 * 
	 * @param objModel
	 * @return result
	 */
	public boolean eliminaAsociacion(int tipologiaRecuperada, int entidadAsociada){
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = ControladorBase.obtenerDtsPromo().getConnection();
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					ConsultasSQ.DELETE_ASOCIACION_ENTIDAD);	
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"Tipologia recuperada = " + tipologiaRecuperada);	
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO, 
					"Entidad asociada = " + entidadAsociada);
			pstmt = conn.prepareStatement(ConsultasSQ.DELETE_ASOCIACION_ENTIDAD);
			
			pstmt.setInt(1, tipologiaRecuperada);
			pstmt.setInt(2, entidadAsociada);
			
			if (pstmt.executeUpdate() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {			
			Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}finally {
			try {
				if(pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
				if(conn != null)
					try {
						conn.close();
					} catch (SQLException e) {
						Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
					}
			}catch (Exception e) {
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
}
