package com.terium.siccam.composer;

import java.sql.Connection;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import com.consystec.ms.seguridad.corecliente.SeguridadWeb;
import com.consystec.ms.seguridad.orm.Aplicacion;
import com.consystec.ms.seguridad.orm.Usuario;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

public class Index extends ControladorBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3819722154936882178L;
	

	public void doAfterCompose(Component win) throws InterruptedException {
		try {
			super.doAfterCompose(win);
			
			String url;
			SeguridadWeb seg = new SeguridadWeb();
			Usuario usr = null;
			Connection conn = null;
			
			//desktop.getSession().invalidate();
			try {
				try {
					conn = obtenerDtsModsec().getConnection();
					Aplicacion app = seg.obtenerAplicacion(conn, nombreAplicacion);
					url = new SeguridadWeb().obtenerUrlPortal(conn, nombreAplicacion);

					if (desktop.getSession().getAttribute("usuarioFirmado") == null
							&& (Executions.getCurrent().getParameter("autoriza") == null)) {
						
						/*Messagebox.show("Ingresa en primera condicion", Constantes.ATENCION, Messagebox.OK,
								Messagebox.INFORMATION);*/
						
						Logger.getLogger(ControladorBase.class.getName()).log(Level.INFO,
								"Verificando seguridad APP:" );

						if (app.getNueva_ventana().intValue() == 0) {
							execution.sendRedirect(url);
						} else
							execution.sendRedirect("timeout.zul");
					} else {
						/*Messagebox.show("Ingresa en segunda condicion", Constantes.ATENCION, Messagebox.OK,
								Messagebox.INFORMATION);*/
						String param = (String) Executions.getCurrent().getParameter("autoriza");
						if (desktop.getSession().getAttribute("usuarioFirmado") == null) {
							usr = seg.validaAutorizacion(conn, param, nombreAplicacion);
							desktop.getSession().setAttribute("url", seg.obtenerUrlPortal(conn, nombreAplicacion));
							desktop.getSession().setAttribute("usuarioFirmado", usr);
							desktop.getSession().setAttribute("usuario", usr.getUsuario());
							
							Tools.setCookie("userName",  usr.getUsuario());
							
							execution.sendRedirect("menu.zul");

						} else {
							execution.sendRedirect("menu.zul");
						}

					}
				} finally {
					if (conn != null)
						conn.close();
				}
			} catch (Exception e) {
				Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, e);
			}
			
		} catch (Exception e1) {
			Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, e1);
		}	
	}
}
