/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.consystec.db.StatementException;
import com.consystec.ms.seguridad.corecliente.SeguridadWeb;
import com.consystec.ms.seguridad.excepciones.ExcepcionSeguridad;
import com.consystec.ms.seguridad.orm.Usuario;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConsultaContabilizacionDAO;
import com.terium.siccam.utils.Configuracion;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

public class MenuController extends ControladorBase {

	private static final long serialVersionUID = 1L;
	Window cMenuPrincipal;
	Label lblUsuario;
	Label lblPais;
	Label lblFecha;
	Label lblCambiarPais;
	Div divPrincipal;
	Div divTitulo;
	Tabbox tbxPrincipal;
	Tab tabPrincipal;
	Toolbarbutton btnInicio;
	Toolbarbutton btnMaxSize;
	Toolbarbutton btnAyuda;
	Button btnFocusTab;
	Button btnDummy;

	Label lblAnio; // Agregado por Carlos Godinez - 04/07/2017
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	Desktop desktop = Executions.getCurrent().getDesktop();

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			
			misession.setAttribute("conexion", Tools.SESSION_SV);
			misession.setAttribute("pais", Tools.CODE_SV);
			
			Tools.setCookie("conexion", Tools.SESSION_SV);
			Tools.setCookie("pais", String.valueOf(Tools.CODE_SV));			

			if (Tools.getCookie("conexion") == null || Tools.getCookie("conexion") == "") {
				System.out.println("sesion null");
				execution.sendRedirect(Tools.REDIRECT_INDEX);
			}else {
				obtienefechaactual();
				// Seteamos Fecha en pantalla
				Date fecha = new Date();
				lblAnio.setValue("®" + String.valueOf(fecha.getYear() + 1900));
				lblFecha.setValue(
						fecha.getDate() + " de " + Configuracion.mes[fecha.getMonth()] + " de " + (fecha.getYear() + 1900));
				// Setear Usuario logueado en pantalla
				lblUsuario.setValue(obtenerUsuario().getUsuario());
//				lblCambiarPais.setValue("(Cambiar Pais)");
				
//				if (Tools.getCookie("conexion").equals(Tools.SESSION_SV)) {
	//				lblPais.setValue("El Salvador");
		//		} else if (Tools.getCookie("conexion").equals(Tools.SESSION_GT)) {
			//		lblPais.setValue("Guatemala");
		//		} else if (Tools.getCookie("conexion").equals(Tools.SESSION_CR)) {
			//		lblPais.setValue("Costa Rica");
		//		} else if (Tools.getCookie("conexion").equals(Tools.SESSION_PA)) {
			//		lblPais.setValue("Panama");
	//			} else if (Tools.getCookie("conexion").equals(Tools.SESSION_NI)) {
					lblPais.setValue("El Salvador");
		//		}
		
				// System.out.println("usuario: " + lblUsuario.getValue());
				/* Seteo de variable de Documento Ayuda de Menu */
				desktop.getSession().setAttribute("documentoAyuda", "/docs_ayuda/ayuda_menu.zul");
		
				btnMaxSize.addEventListener("onClick", maximizar);
				tabPrincipal.addEventListener("onDoubleClick", maximizar);
				tabPrincipal.addEventListener("onClick", onClick$tabDinamico);
			}
		} catch (Exception e) {
			Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, e);			
		}
		
		
	}

	public void onClick$btnInicio(Event ev) {
		tabPrincipal.setSelected(true);
		onClick$btnFocusTab(ev);
	}

	@SuppressWarnings("rawtypes")
	// Evento para realizar maximizacion desde Boton Maximizar,
	// y doble click en pestannias
	EventListener maximizar = new EventListener() {
		public void onEvent(Event arg0) throws Exception {
			if (divPrincipal.getWidth() == null) {
				reSize("1000px", "530px");
				width = "1000px";
				height = "530px";
				divTitulo.setVisible(true);
			} else {
				if (divPrincipal.getWidth().equals("100%")) {
					reSize("1000px", "530px");
					width = "1000px";
					height = "700px";
					divTitulo.setVisible(true);
				} else {
					reSize("100%", "100%");
					width = "100%";
					height = "100%";
					divTitulo.setVisible(false);
				}
			}
			Events.echoEvent("onClick", btnDummy, null);
		}
	};

	public void obtienefechaactual() {
		String fecha = null;
		CBConsultaContabilizacionDAO objDao = new CBConsultaContabilizacionDAO();
		try {
		fecha = objDao.validafecha();
		System.out.println("fecha obtenida al entrar " + fecha);
		}catch (Exception e) {
			Logger.getLogger(CBConsultaContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}
		
	}
	
	public void reSize(String width, String height) {
		Clients.evalJavaScript("$('.divPrincipal').stop().animate({width:'" + width + "', height:'" + height
				+ "'},{queue:true,duration:300});");
	}

	String width = "";
	String height = "";

	public void onClick$btnDummy() throws InterruptedException {
		Thread.sleep(100);
		divPrincipal.setWidth(width);
		divPrincipal.setHeight(height);
		Clients.resize(divPrincipal);

		// divPrincipal.invalidate();
	}

	// Evento para detectar el cambio de pestanias y cambiar la configuracion
	// de ventanas de ayuda.
	@SuppressWarnings("rawtypes")
	EventListener onClick$tabDinamico = new EventListener() {
		public void onEvent(Event event) throws Exception {
			desktop.removeAttribute("documentoAyuda");

			desktop.setAttribute("documentoAyuda", "/docs_ayuda/ayuda.zul");
			/*
			 * desktop.setAttribute("documentoAyuda", "/docs_ayuda/ayuda_" +
			 * tbxPrincipal.getSelectedTab().getLabel().replaceAll(" ", "_")
			 * .replaceAll("ÃƒÂ¡", "a").replaceAll("ÃƒÂ©", "e").replaceAll("o", "o") +
			 * ".zul");
			 */
		}

	};
	// Evento que permite simular el click sobre otra pestania y cambiar
	// configuracion
	// cuando una pestania es cerrada.
	@SuppressWarnings("rawtypes")
	EventListener evlOnCloseTabDinamico = new EventListener() {
		public void onEvent(Event event) throws Exception {
			Events.echoEvent("onClick", btnFocusTab, null);
		}
	};

	// Simulacion de click sobre pestania seleccionada
	public void onClick$btnFocusTab(Event evt) {
		for (int i = 0; i < tbxPrincipal.getTabs().getChildren().size(); i++) {
			Tab t1 = (Tab) tbxPrincipal.getTabs().getChildren().get(i);
			if (t1.isSelected()) {
				Events.echoEvent("onClick", t1, null);
			}
		}
	}

	// Crea un nuevo Tab con las propiedades recibidas de parametro.
	// Si el tab ya existe solo se le enfoca.
	@SuppressWarnings("unchecked")
	public void muestraOpcion(String titulo, String url, String id, String ico) {
		int index = getIndexTab(id);
		Include tempInc = null;
		if (index == -1) {
			Tab tab = new Tab();
			tab.addEventListener("onDoubleClick", maximizar);
			tab.addEventListener("onClick", onClick$tabDinamico);
			tab.addEventListener("onClose", evlOnCloseTabDinamico);
			tab.setId("tab" + id);
			tab.setLabel(titulo);
			tab.setClosable(true);
			tab.setImage(ico);
			tab.setParent(tbxPrincipal.getTabs());

			tempInc = new Include(url);
			tempInc.setId(id);
			tempInc.setWidth("100%");
			tempInc.setHeight("100%");

			Tabpanel tabpanel = new Tabpanel();
			tabpanel.setId("pnltab" + id);
			tabpanel.setParent(tbxPrincipal.getTabpanels());
			tempInc.setParent(tabpanel);
			desktop.removeAttribute("documentoAyuda");
			desktop.setAttribute("documentoAyuda", "/docs_ayuda/ayuda.zul");

			/*
			 * comentariado Ovidio Santos 08082017 se hace general la ayuda para todas las
			 * pantallas desktop.setAttribute("documentoAyuda", "/docs_ayuda/ayuda_" +
			 * titulo.toLowerCase().replaceAll(" ", "_").replaceAll("ÃƒÂ¡", "a")
			 * .replaceAll("ÃƒÂ©", "e").replaceAll("ÃƒÂ­", "i").replaceAll("o", "o") +
			 * ".zul");
			 */
			tbxPrincipal.setSelectedIndex(tbxPrincipal.getTabs().getChildren().size() - 1);
		} else {
			tbxPrincipal.setSelectedIndex(index);
		}
	}

	// Validar si el tab ya existe.
	public int getIndexTab(String id) {
		int ret = -1;
		for (int i = 0; i < tbxPrincipal.getTabs().getChildren().size(); i++) {
			if (((Tab) tbxPrincipal.getTabs().getChildren().get(i)).getId().equals("tab" + id)) {
				return i;
			}
		}
		return ret;
	}

	public void llamarConciliacionDetalle(String num) {
		String titulo = "C.Detalle: " + num;
		String url = "/conciliacion_detalle.zul";
		String id = "inclitNumero" + num;
		String ico = "/img/globales/16x16/edit.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Mantenimiento Prioridades
	public void onDoubleClick$cMantenimiento(Event ev) {
		String titulo = "Conciliaciones";
		// String url = "/pendiente.zul";
		String url = "/conciliacion.zul";
		String id = "inclit" + "Conciliacion";
		String ico = "/img/globales/16x16/consultas.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Quickitem Mantenimiento Prioridades
	public void onClick$qkiConsulta(Event ev) throws InterruptedException {
		onDoubleClick$cMantenimiento(ev);
	}

	// Bitacora Cambio Prioridades
	public void onDoubleClick$bcambioPrioridades(Event ev) {
		String titulo = "Bitacora de cambios de prioridades";
		// String url = "/pendiente.zul";
		String url = "/bitacoracambiosdeprioridad.zul";
		String id = "inclit" + "Mantenimiento";
		String ico = "/img/globales/16x16/consultas.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Quickitem Bitacora Cambio Prioridades
	public void onClick$qkibcambioPrioridades(Event ev) throws InterruptedException {
		onDoubleClick$cMantenimiento(ev);
	}

	// Carga de Confrontas
	public void onDoubleClick$cConfronta(Event ev) {
		String titulo = "Carga de Confrontas";
		String url = "/fileTxtUpload.zul";
		String id = "inclit" + "Confrontas";
		String ico = "/img/globales/16x16/autorizaciones.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Carga de Estado de cuentas - by Juankrlos -
	public void onDoubleClick$cargaEstadoCuentas(Event ev) {
		String titulo = "Carga de Estados de cuenta";
		String url = "/cbloadestadocuentas.zul";
		String id = "inclit" + "EstadoCuentas";
		String ico = "/img/globales/16x16/autorizaciones.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onDoubleClick$consultaConciliacionCajeros(Event ev) {
		String titulo = "Consulta Conciliacion Cajeros";
		String url = "/cbconciliacioncajas.zul";
		String id = "inclit" + "ConciliacionCajas";
		String ico = "/img/globales/16x16/autorizaciones.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onDoubleClick$consultaEstadoCuentas(Event ev) {
		String titulo = "Consulta Estados de Cuentas";
		String url = "/cbconsultaestadoscuenta.zul";
		String id = "inclit" + "consultaCuentas";
		String ico = "/img/globales/16x16/consultas.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Agregado por Carlos Godínez - QitCorp - 21/03/2017
	public void onDoubleClick$consultaEstadoCuentasTarjeta(Event ev) {
		String titulo = "Consulta Estados de Cuentas de Tarjeta";
		String url = "/cbconsultaestadocuentatarjeta.zul";
		String id = "inclit" + "consultaTarjetas";
		String ico = "/img/globales/16x16/consultas.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onDoubleClick$consultaConciliaBanco(Event ev) {
		String titulo = "Consulta conciliacion de bancos";
		String url = "/cbconciliacionbanco.zul";
		String id = "inclit" + "consultaConciliaBanco";
		String ico = "/img/globales/16x16/consultas.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Agregado por Ovidio Santos - QitCorp - 21/03/2017
	public void onDoubleClick$consultacontabilizacion(Event ev) {
		String titulo = "Consulta Contabilizacion";
		String url = "/cbConsultaContabilizacion.zul";
		String id = "inclit" + "consultacontabilizacion";
		String ico = "/img/globales/16x16/consultas.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Quickitem Mantenimiento de Confronta
	public void onClick$qkiConfronta(Event ev) throws InterruptedException {
		onDoubleClick$cConfronta(ev);
	}

	// Mantenimiento de bitcaroa
	public void onDoubleClick$qkiBitacora(Event ev) {
		String titulo = "Bitacora de Cambios de Prioridad";
		String url = "/bitacoracambiosdeprioridad.zul";
		String id = "inclit" + "Bitacora";
		String ico = "/img/globales/16x16/autorizaciones.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Quickitem Mantenimiento de bitacora
	public void onClick$qkiBitacora(Event ev) throws InterruptedException {
		onDoubleClick$qkiBitacora(ev);
	}

	// Mantenimiento tipologias poliza - Ovidio Santos - Qitcorp
	public void onDoubleClick$Tipologia(Event ev) {
		String titulo = "Mantenimiento de Tipologias Poliza";
		String url = "/tipologias.zul";
		String id = "inclit" + "Tipologia";
		String ico = "/img/globales/16x16/tipologiascl_1.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onClick$qkiTipologias(Event ev) throws InterruptedException {
		onDoubleClick$Tipologia(ev);
	}

	// Mantenimiento Courier
	public void onDoubleClick$entidadBancaria(Event ev) {	
		String titulo = "Agrupación de entidades";
		String url = "/cbConsultaBanco.zul";
		String id = "inclit" + "Banco";
		String ico = "/img/globales/16x16/usuarios.png";
		muestraOpcion(titulo, url, id, ico);
	}

	// Quickitem Mantenimiento de Entidades Bancarias
	public void onClick$qkientidadBancaria(Event ev) throws InterruptedException {
		onDoubleClick$entidadBancaria(ev);
	}

	public void onDoubleClick$agenciaBancaria(Event ev) {
		String titulo = "Catálogo de entidades";
		// String url = "/cbConsultaAgencia.zul"; //Carlos Godinez -> 16/10/2017
		String url = "/cbconsultaentidades.zul";
		String id = "inclit" + "Agencias";
		String ico = "/img/globales/16x16/usuarios.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onClick$qkiagenciaBancaria(Event ev) throws InterruptedException {
		onDoubleClick$agenciaBancaria(ev);
	}

	// Quickitem Consulta Carga por archivo
	public void onClick$qkiconsultaCargasMas(Event ev) throws InterruptedException {
		onDoubleClick$consultaCargasMas(ev);
	}

	public void onDoubleClick$consultaCargasMas(Event ev) {
		String titulo = "Consulta de Cargas";
		String url = "/consultaCargas.zul";
		String id = "inclit" + "Cargas";
		String ico = "/img/globales/16x16/usuarios.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onDoubleClick$opLiquidacionCajeros(Event ev) {
		String titulo = "Liquidación de cajeros";
		String url = "/cbliquidacioncajeros.zul";
		String id = "inclit" + "Liquidaciones";
		String ico = "/img/globales/16x16/usuarios.png";
		muestraOpcion(titulo, url, id, ico);
	}

	/**
	 * Agrega Carlos Godinez - Qitcorp - 18/05/2017
	 */
	public void onClick$qkiConfParamGenerales(Event ev) {
		String titulo = "Configuración de parametros generales";
		String url = "/cbparametrosgenerales.zul";
		String id = "inclit" + "ParamGenerales";
		String ico = "/img/globales/16x16/estados.png";
		muestraOpcion(titulo, url, id, ico);
	}
	
	/**
	 * Agrega Ovidio Santos- Qitcorp - 18/05/2017
	 */
	public void onClick$qkiConfDepositosRec(Event ev) {
		String titulo = "Configuración de Depositos";
		String url = "/cbDepositosRec.zul";
		String id = "inclit" + "Depositos";
		String ico = "/img/globales/16x16/estados.png";
		muestraOpcion(titulo, url, id, ico);
	}


	/**
	 * Agrega Ovidio- Qitcorp - 18/05/2017
	 */
	public void onClick$qkiConfAplicaDesaplica(Event ev) {
		String titulo = "Aplica Desaplica Pagos";
		String url = "/cbAplicaDesaplicaPagos.zul";
		String id = "inclit" + "AplicaDesaplica";
		String ico = "/img/globales/16x16/estados.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onClick$qkiConsultaCargas(Event ev) throws InterruptedException {
		onDoubleClick$consultaCargasMas(ev);
	}

	// Quickitem configuracion de conexiones
	public void onClick$qkiconfConexion(Event ev) throws InterruptedException {
		onDoubleClick$confConexion(ev);
	}

	public void onDoubleClick$confConexion(Event ev) {
		String titulo = "Configuración de Conexiones";
		String url = "/cbConfiguracionConexiones.zul";
		String id = "inclit" + "Configuracion";
		String ico = "/img/globales/16x16/estados.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onClick$qkiConfConexion(Event ev) throws InterruptedException {
		onDoubleClick$confConexion(ev);
	}

	// Quickitem Tipologias causas de conciliacion
	public void onClick$qkiCausas(Event ev) throws InterruptedException {
		onDoubleClick$Causas(ev);
	}

	public void onDoubleClick$Causas(Event ev) {
		String titulo = "Mantenimiento de Tipologias";
		String url = "/cbMantenimientoCausas.zul";
		String id = "inclit" + "Causas";
		String ico = "/img/globales/16x16/tipologiascl_1.png";
		muestraOpcion(titulo, url, id, ico);
	}
	
	//Agrega Juankrlos 26/07/2018
	public void onClick$qkiRecaRegu(Event ev) {
		String titulo = "Manejo Regularizacion y Recaudacion";
		String url = "/cbregularizacionrecaudacion.zul";
		String id = "in" + "RecaRegu";
		String ico = "/img/globales/16x16/tipologiascl_1.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onClick$qkiCausasDos(Event ev) throws InterruptedException {
		onDoubleClick$Causas(ev);
	}

	// Quickitem Configuracion de confrontas
	public void onClick$qkiconfConfrontas(Event ev) throws InterruptedException {
		onDoubleClick$ConfConfrontas(ev);
	}

	public void onDoubleClick$ConfConfrontas(Event ev) {
		String titulo = "Configuración de Confrontas";
		// String url = "/cbMatenimientoConfronta.zul"; //CarlosGodinez -> 22/08/2017
		String url = "/cbmantenimientoconfronta.zul";
		String id = "inclit" + "ConfiguracionConf";
		String ico = "/img/globales/16x16/tipologiascl_1.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onClick$qkiConfConfrontasDos(Event ev) throws InterruptedException {
		onDoubleClick$ConfConfrontas(ev);
	}

	public void onDoubleClick$mncUsuariosOficinas(Event ev) {
		String titulo = "Reglas de Asignacion";
		String url = "/mantenimientosdeasignaciones.zul";
		String id = "inclit" + "UsuariosOficinas";
		String ico = "/img/globales/16x16/usuarios.png";
		muestraOpcion(titulo, url, id, ico);
	}

	//

	public void onClick$qkiEntidadBanc(Event ev) throws InterruptedException {
		onDoubleClick$entidadBancaria(ev);
	}

	public void onDoubleClick$qkiEmpresaCour(Event ev) {
		String titulo = "Catologo de Couriers";
		String url = "/catalogocourier.zul";
		String id = "inclit" + "UsuariosOficinas";
		String ico = "/img/globales/16x16/usuarios.png";
		muestraOpcion(titulo, url, id, ico);
	}

	//
	// Quickitem Agencia Bancarias
	public void onClick$qkiAgenciasBanck(Event ev) throws InterruptedException {
		onDoubleClick$agenciaBancaria(ev);
	}

	public void onDoubleClick$qkiReportes(Event ev) {
		String titulo = "Reportes";
		String url = "/reportes.zul";
		// String url = "/reportes.zul";
		String id = "inclit" + "Reportes";
		String ico = "/img/globales/16x16/xlsx.png";
		muestraOpcion(titulo, url, id, ico);
	}

	public void onClick$qkiConciliaciondet(Event ev) {
		String titulo = "Conciliacion Detallada";
		String url = "/conciliacion_detalle.zul";
		String id = "inclitNumero" + "";
		String ico = "/img/globales/16x16/edit.png";
		muestraOpcion(titulo, url, id, ico);
	}
	public void onDoubleClick$qkiCuadreSidra(Event ev) {
		String titulo = "Cuadre Sidra";
		String url = "/cbCuadreSidra.zul";
		String id = "inclit" + "cuadreSidra";
		String ico = "/img/globales/16x16/edit.png";
		muestraOpcion(titulo, url, id, ico);
	}

	/**
	 * Added by CarlosGodinez -> 10/10/2018
	 * Manual de Usuario
	 * */
	public void onClick$qkiManualUsuario(Event ev) {
		Executions.createComponents("/cbmanualusuario.zul", null, null);
	}
	
	//
	// Quickitem Reportes
	public void onClick$qkiReportesCon(Event ev) throws InterruptedException {
		onDoubleClick$qkiReportes(ev);
	}

	// Menucard Ayuda
	public void onDoubleClick$cAyuda(Event ev) throws InterruptedException {
		Window cAyuda = null;
		try {
			cAyuda = (Window) tbxPrincipal.getSelectedTab()
					.getFellow("wdwAyuda" + tbxPrincipal.getSelectedTab().getId());
		} catch (Exception e) {
		}

		if (desktop.getAttribute("documentoAyuda") != null) {
			if (cAyuda == null) {
				cAyuda = new Window();
				cAyuda.setId("wdwAyuda" + tbxPrincipal.getSelectedTab().getId());
				String archivo = desktop.getAttribute("documentoAyuda").toString();
				cAyuda.setWidth("680px");
				cAyuda.setHeight("580px");
				cAyuda.setPosition("right,top");
				cAyuda.setClosable(true);
				cAyuda.setSizable(true);
				cAyuda.setMaximizable(false);
				cAyuda.setBorder("normal");
				Tabpanel tabpanel = (Tabpanel) tbxPrincipal.getFellow("pnl" + tbxPrincipal.getSelectedTab().getId());

				Iframe ifr = new Iframe();

				if (tbxPrincipal.getSelectedTab().getLabel().indexOf("Inicio") == -1) {
					ifr.setSrc(archivo.toLowerCase());
				} else {
					ifr.setSrc("/docs_ayuda/ayuda_menu.zul");
				}
				ifr.setWidth("100%");
				ifr.setHeight("100%");
				ifr.setParent(cAyuda);
				cAyuda.setParent(tabpanel);
				cAyuda.setTitle("Ayuda " + tbxPrincipal.getSelectedTab().getLabel());
				Caption cpt = new Caption();
				cpt.setImage("img/globales/16x16/ayuda16.png");
				cpt.setParent(cAyuda);
				cAyuda.doOverlapped();
			} else {
				cAyuda.setPosition("right,top");
				cAyuda.doOverlapped();
			}
		} else {
			// showMensaje("No se tiene archivo de ayuda configurado.", null);
		}
	}

	// Quickitem Ayuda
	public void onClick$qkiAyuda(Event ev) throws InterruptedException {
		onDoubleClick$cAyuda(ev);
	}

	// Acceso directo Ayuda
	public void onClick$btnAyuda(Event ev) throws InterruptedException {
		onDoubleClick$cAyuda(ev);
	}

	// //Menucard Salir
	public void onDoubleClick$mncSalir(Event ev) throws InterruptedException {
		
	}

	// //Quickitem Salir
	public void onClick$qkiSalir(Event ev) throws InterruptedException {
		Messagebox.show("Desea salir de esta aplicación y regresar al Portal de Aplicaciones", Constantes.CONFIRMACION,
				Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
					public void onEvent(Event event) throws Exception {
						if (((Integer) event.getData()).intValue() != Messagebox.YES) {
							System.out.println("Messagebox.CANCEL selected!");
							return;
						} else {

							String url = "";
							try {
									url = new SeguridadWeb().obtenerUrlPortal(obtenerDtsModsec().getConnection(),
											nombreAplicacion);
									
									Tools.eraseCookie("userName");
									Tools.eraseCookie("userId");
									Tools.eraseCookie("conexion");
									Tools.eraseCookie("pais");
									
									/*Redirigir a AVI*/
									execution.sendRedirect(url);
								
							} catch (Exception ex) {
								Logger.getLogger(ControladorBase.class.getName()).log(Level.SEVERE, null, ex);
								//Messagebox.show(obtenerMensajeExcepcion(ex), "ERROR", Messagebox.OK, Messagebox.ERROR);
							}

						}

					}
				});
	}

	
	
	
}
