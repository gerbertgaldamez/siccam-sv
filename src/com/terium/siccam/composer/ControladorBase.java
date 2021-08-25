package com.terium.siccam.composer;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import com.consystec.ms.seguridad.corecliente.SeguridadWeb;
import com.consystec.ms.seguridad.corecliente.SeguridadZK;
import com.consystec.ms.seguridad.excepciones.ExcepcionSeguridad;
import com.consystec.ms.seguridad.orm.Aplicacion;
import com.consystec.ms.seguridad.orm.Elemento;
import com.consystec.ms.seguridad.orm.PermisosGenerales;
import com.consystec.ms.seguridad.orm.Usuario;

import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

public class ControladorBase extends GenericForwardComposer<Component> {

	private static Logger log = Logger.getLogger(ControladorBase.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String nombreAplicacion = "SICCAMSV";

	static HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	static Desktop desktop = Executions.getCurrent().getDesktop();

	// public ControladorBase() {}
	Connection conn = null;

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		String methodName = "doAfterCompose()";
		// obtenerDtsPromo();
		// obtenerDtsModsec();//

		try {
			conn = obtenerDtsModsec().getConnection();
			SeguridadWeb segWeb = new SeguridadWeb();
			Usuario usuario = null;

			SeguridadZK segZK = new SeguridadZK();
			List<PermisosGenerales> permisos;
			List<Elemento> elementos;
			Aplicacion aplicacion = null;

			if (Executions.getCurrent().getParameter("autoriza") != null) {
				String codArea = null;
				Tools.eraseCookie("userName");
				Tools.eraseCookie("userId");
				Tools.eraseCookie("conexion");
				Tools.eraseCookie("pais");

				/** Obtener el valor del parametro de url autoriza. */
				String param = (String) Executions.getCurrent().getParameter("autoriza");

				/** Obtener valor del parametro de url pais */
				if (Executions.getCurrent().getParameter("pais") == null) { // cambios para pruebas
					codArea = (String) Executions.getCurrent().getParameter("pais");
					desktop.getSession().setAttribute("paisAPP_" + nombreAplicacion, codArea);
				}

				/**
				 * Asignar un valor nulo a los atributos de sesión, que contendrán los permisos
				 * y elementos de la aplicación
				 */
				desktop.getSession().setAttribute("permisosUsr_" + nombreAplicacion, null);
				desktop.getSession().setAttribute("elementosApp_" + nombreAplicacion, null);
				HttpServletRequest hsr = (HttpServletRequest) execution.getNativeRequest();

				/**
				 * .invocar el método @validaAutorización, por medio del cuál se verificará que
				 * la seseión del usuario se encuentre activa, y retornará el objeto de tipo
				 * Usuario.
				 */
				usuario = segWeb.validaAutorizacion(conn, param, nombreAplicacion, hsr);

				/** Almacenar en sesión los datos de usuario. */
				desktop.getSession().setAttribute("usuarioFirmado", usuario);
				desktop.getSession().setAttribute("usuario", usuario.getUsuario());
				Tools.setCookie("userName", usuario.getUsuario());
				Tools.setCookie("userId", String.valueOf(usuario.getSecusuarioid()));

			}

			if (Tools.getCookie("userName") != null && Tools.getCookie("userName") != "") {

				if (Tools.getCookie("pais") != null && Tools.getCookie("pais") != "") {

					usuario = new Usuario();
					usuario.setUsuario(Tools.getCookie("userName"));
					usuario.setSecusuarioid(new BigDecimal(Tools.getCookie("userId")));

					/** Obtener información de aplicación */
					aplicacion = segWeb.obtenerAplicacion(conn, nombreAplicacion);
					desktop.getSession().setAttribute("SecAplicacion_" + nombreAplicacion, aplicacion);

					/** Obtener Permisos configurados para el usuario */

					int pais = Integer.parseInt(Tools.getCookie("pais"));

					log.debug(methodName + " - SICCAM pais:" + pais);
					permisos = segWeb.obtenerPermisos(conn, usuario, aplicacion.getNombre(), new BigDecimal(pais));

					desktop.getSession().setAttribute("permisosUsr_" + nombreAplicacion, permisos);

					if (desktop.getSession().getAttribute("elementosApp_" + nombreAplicacion) == null) {
						/** Obtener listado de elementos de aplicación */
						elementos = segWeb.obtenerElementosApp(conn, aplicacion.getSecaplicacionid());
						desktop.getSession().setAttribute("elementosApp_" + nombreAplicacion, elementos);
					} else
						elementos = (List<Elemento>) desktop.getSession()
								.getAttribute("elementosApp_" + nombreAplicacion);

					if (permisos != null && elementos != null && aplicacion != null) {
						// permisos = (List<PermisosGenerales>)
						// desktop.getSession().getAttribute("permisosUsr_" + nombreAplicacion);
						// elementos = (List<Elemento>)
						// desktop.getSession().getAttribute("elementosApp_" + nombreAplicacion);
						// aplicacion = (Aplicacion) desktop.getSession().getAttribute("SecAplicacion_"
						// + nombreAplicacion);
						/** Habilitar o Deshabilitar componentes de Zk, automáticamente */
						segZK.generacionComponentesSeguridad(null, permisos, elementos, comp, usuario,
								aplicacion.getSecaplicacionid(), aplicacion);

						/*
						 * Messagebox.show("Genera componentes de seguridad", Constantes.ATENCION,
						 * Messagebox.OK, Messagebox.INFORMATION);
						 */
						log.debug(methodName + " - SICCAM Genera Componentes de Seguridad");
					}
				}

			} else {
				String url = new SeguridadWeb().obtenerUrlPortal(conn, nombreAplicacion);

				Tools.eraseCookie("userName");
				Tools.eraseCookie("userId");
				Tools.eraseCookie("conexion");
				Tools.eraseCookie("pais");
				/** Redirigir a AVI */

				execution.sendRedirect(url);
			}

		} catch (ExcepcionSeguridad e) {
			Log.error(e.getMessage(), e);
		} catch (SQLException e) {
			Log.error(e.getMessage(), e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

	public static boolean validarEmail(String email) {
		String regex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public BigDecimal obtenerEmpresa() {
		return new BigDecimal(1);
	}

	public Usuario obtenerUsuario() {
		Usuario usr = null;
		if (Tools.getCookie("userName") != null && Tools.getCookie("userName") != "") {
			usr = new Usuario();
			usr.setUsuario(Tools.getCookie("userName"));
		} else {
			/* seccion usuario no encontrado */
			Usuario ret = new Usuario();
			usr = new Usuario();
			usr.setUsuario("Administrador");
//			usr.setSecusuarioid(new BigDecimal(-1));
//			return ret;
			try {
				String url = new SeguridadWeb().obtenerUrlPortal(conn, nombreAplicacion);

				Tools.eraseCookie("userName");
				Tools.eraseCookie("conexion");
				Tools.eraseCookie("pais");

				/** Redirigir a AVI */
				execution.sendRedirect(url);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("obtenerUsuario() - Error ", e);
			}

		}
		return usr;

	}

	public static DataSource obtenerDtsPromo() {
		DataSource dts = null;
		String methodName = "obtenerDtsPromo()";
		log.debug(methodName + " - inicio controlador base");
		try {
			Context contexto = new InitialContext();
			log.debug(methodName + " Conexion getCookie " + Tools.getCookie("conexion"));
			if (Tools.getCookie("conexion") == null || Tools.getCookie("conexion") == "conexion") {
				log.debug(methodName + "la conexion es nula: " + misession.getAttribute(Constantes.CONEXION));
			} else if (Tools.getCookie("conexion").equals(Constantes.CONEXION)) {// Tools.SESSION_SV
				dts = (DataSource) contexto.lookup(Constantes.STR_CONEX + Tools.CONEXION_SV);// CONEXION_CR
				log.debug(methodName + " JNDI conexion : " + Constantes.STR_CONEX + Tools.CONEXION_SV);
			} // CONEXION_CR
			else {
				dts = (DataSource) contexto.lookup(Constantes.STR_CONEX + Tools.CONEXION_SV);// CONEXION_CR
				log.debug(methodName + " JNDI contexto : " + Constantes.STR_CONEX + Tools.CONEXION_SV);
			}

		} catch (NamingException e) {
			log.error(methodName + " - No se ha podido obtener la conexion ", e);
			/*
			 * Messagebox .show(
			 * "Ocurrió un error al tratar de obtener la conexión a DB, comúníquese con su administrador."
			 * , "Error", Messagebox.OK, Messagebox.ERROR);
			 */
		}
		return dts;
	}

	/*
	 * version ovidio public static DataSource obtenerDtsPromo() { DataSource dts =
	 * null; System.out.println("entrando a dtspromo " );
	 * Logger.getLogger(ControladorBase.class.getName()).log(Level.INFO, null,
	 * "entrando a controlador base" ); try {
	 * 
	 * Context contexto = new InitialContext();
	 * System.out.println("entrando a dtspromo 1" ); //
	 * System.out.println(Tools.getCookie("conexion") );
	 * Logger.getLogger(ControladorBase.class.getName()).log(Level.INFO, null,
	 * "la conexion es nula: " + misession.getAttribute(Constantes.CONEXION));
	 * 
	 * 
	 * dts = (DataSource) contexto.lookup(Constantes.STR_CONEX + Tools.CONEXION_CR);
	 * System.out.println("entrando a dtspromo 5" );
	 * 
	 * 
	 * 
	 * } catch (NamingException e) {
	 * Logger.getLogger(ControladorBase.class.getName()).log(Level.INFO, null,
	 * "No logro la conexion");
	 * Logger.getLogger(ControladorBase.class.getName()).log(Level.SEVERE, null, e);
	 * /* Messagebox .show(
	 * "Ocurrió un error al tratar de obtener la conexión a DB, comúníquese con su administrador."
	 * , "Error", Messagebox.OK, Messagebox.ERROR);
	 */
	/*
	 * } return dts; }
	 */

//	public static DataSource obtenerDtsPromos(String pais) {
	public static DataSource obtenerDtsPromos() {
		String methodName = "obtenerDtsPromos()";
		String pais;
		DataSource dts = null;
		pais = "SV";// CR
		log.debug(methodName + " - inicio ");
		try {
			log.debug(methodName + " - obteniendo conexion SV");
			Context contexto = new InitialContext();
			if (pais == null || pais == "") {
				log.debug(methodName + " - la conexion es nula: " + misession.getAttribute(Constantes.CONEXION));
				dts = (DataSource) contexto.lookup(Constantes.STR_CONEX + Tools.CONEXION_SV);// CONEXION_CR
			}

		} catch (NamingException e) {
			log.error(methodName + " - Error al obtener la conexion ", e);
			/*
			 * Messagebox .show(
			 * "Ocurrió un error al tratar de obtener la conexión a DB, comúníquese con su administrador."
			 * , "Error", Messagebox.OK, Messagebox.ERROR);
			 */
		}
		return dts;
	}

	public String obtenerMensajeExcepcion(Exception e) {
		String ret = "";
		int errorCode = 0;

		if (e instanceof SQLException) {
			errorCode = ((SQLException) e).getErrorCode();
		}
		if (e.getCause() instanceof SQLException) {
			errorCode = ((SQLException) e.getCause()).getErrorCode();
		}
		if (errorCode != 0) {
			switch (errorCode) {
			case 1:
				ret = "El bono(s) que desea ingresar ya existe, selecciones otro bono(s)";
				break;
			case 904:
				ret = "Se realizo una consulta con un indice de columna no valida, consulte a su administrador";
				log.error("obtenerMensajeExcepcion() - Error : " + e);
				break;
			case 936:
				ret = "La consulta no tiene una sintaxis valida, consulte a su administrador";
				log.error("obtenerMensajeExcepcion() - Error : " + e);
				break;
			case 942:
				ret = "La tabla que se esta intentando consultar no existe, consulte a su administrador";
				log.error("obtenerMensajeExcepcion() - Error : " + e);
				break;
			case 1017:
				ret = "Usuario o clave de base de datos invalido, consulte a su administrador";
				log.error("obtenerMensajeExcepcion() - Error : " + e);
				break;
			case 1034:
				ret = "La base de datos no ha sido inicializada, consulte a su administrador";
				log.error("obtenerMensajeExcepcion() - Error : " + e);
				break;
			case 1400:
				ret = "No se puede insertar un valor nulo en este registro";
				break;
			case 1401:
				ret = "El valor insertado excede el maximo permitido para esta columna";
				break;
			case 1722:
				ret = "Se trato de convertir una cadena de carateres a un numero y no fue exitoso, consulte a su administrador";
				break;
			case 1403:
				ret = "No se encontraron datos";
				break;
			case 2292: {
				if (e.getMessage().startsWith("com.consystec.seguridad.dao.PerfilesDAO"))
					ret = "Este perfil no se puede eliminar porque existe herencia a sus permisos desde otro perfil";
				else
					ret = "Este registro no puede ser eliminado porque existen dependencias";
			}
				break;
			case 3113:
				ret = "La conexión hacia la base de datos se cerro anticipadamente";
				break;

			case 20001:
				ret = "El numero ya tiene una promocion asignada anteriormente";
				break;

			case 20002:
				if (e.getMessage().indexOf("ORA-20001:") > -1) {
					ret = e.getMessage().substring(e.getMessage().indexOf("ORA-20001:") + 11);
					ret = ret.substring(0, ret.indexOf("ORA-"));
				} else
					ret = e.getMessage();

				break;
			default:
				if (errorCode < 0)
					ret = e.getMessage();
				else {
					ret = "Ocurrió un error inesperado, comuníquese con su administrador de sistema.";
					log.error("obtenerMensajeExcepcion() - Error : " + e);
				}
				break;
			}
		} else
			ret = e.getMessage();

		return ret;
	}

	public DataSource obtenerDtsModsec() {
		DataSource dts = null;
		try {
			if (desktop.getSession().getAttribute("dtsModsec") == null) {
				Context contexto = new InitialContext();

				dts = (DataSource) contexto.lookup("java:comp/env/jdbc/modsec");
				log.debug("obtenerDtsModsec() - " + "dts:" + dts);
				desktop.getSession().setAttribute("dtsModsec", dts);
			} else
				dts = (DataSource) desktop.getSession().getAttribute("dtsModsec");
		} catch (NamingException e) {
			log.error("obtenerDtsModsec() - Error : ", e);
		}
		return dts;
	}

	/**
	 * obtiene la ruta real de la carpeta archivos, que es donde estan los reportes
	 * 
	 * @return ruta completa
	 */
	@SuppressWarnings("unused")
	public String ObtenerRuta() {
		String separador = "";
		String raiz = application.getRealPath("/Archivos/");

		if (raiz.indexOf("/") >= 0 && !raiz.endsWith("/")) {
			separador = "/";
		} else if (raiz.indexOf("\\") >= 0 && !raiz.endsWith("\\")) {
			separador = "\\";
		}

		if (raiz.indexOf("/") >= 0 && !raiz.endsWith("/")) {
			raiz = raiz + "/";
		} else if (raiz.indexOf("\\") >= 0 && !raiz.endsWith("\\")) {
			raiz = raiz + "\\";
		}

		return raiz;
	}

	// SALIR AL PORTAL DE APLICACIONES
	public void onClick$mniSalir() throws InterruptedException {

	}

	public String obtenerLink() {
		String ret = "@link_rppgaslv";
		return ret;
	}
}