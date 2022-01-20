package com.terium.siccam.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.consystec.ms.seguridad.orm.Usuario;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.utils.Configuracion;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

public class InicioController extends ControladorBase{
	private static Logger log = Logger.getLogger(InicioController.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -7264612439441042285L;
	Label lblUsuario;
	Label lblFecha;
	Label lblAnio;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();	
	Desktop desktop = Executions.getCurrent().getDesktop();
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	
	public void doAfterCompose(Component comp) {
		try {
			super.doAfterCompose(comp);
			
			misession.setAttribute("conexion", null);
			misession.setAttribute("pais", null);
			
			
			misession.setAttribute("conexion", Tools.SESSION_SV);
			misession.setAttribute("pais", Tools.CODE_SV);
			
			Tools.setCookie("conexion", Tools.SESSION_SV);
			Tools.setCookie("pais", String.valueOf(Tools.CODE_SV));

				execution.sendRedirect(Tools.REDIRECT_MENU);
			
			
			// Seteamos Fecha en pantalla
			Date fecha = new Date();
			lblAnio.setValue("®" + String.valueOf(fecha.getYear() + 1900));
			lblFecha.setValue(
					fecha.getDate() + " de " + Configuracion.mes[fecha.getMonth()] + " de " + (fecha.getYear() + 1900));
			
			// Setear Usuario logueado en pantalla
			lblUsuario.setValue(obtenerUsuario().getUsuario());
			//Messagebox.show("Carga componentes de inicio:", Constantes.ATENCION, Messagebox.OK,
				//	Messagebox.INFORMATION);
		} catch (Exception e) {
			//Messagebox.show("entra a excepcion en inicio:", Constantes.ATENCION, Messagebox.OK,
				//	Messagebox.INFORMATION);
			log.error("doAfterCompose() -  Error ", e);
			//Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, e);
		}	
		
	}
	
	

}
