package com.terium.siccam.controller;

import java.util.Date;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Window;

import com.terium.siccam.dao.CBConciliacionBancoDAO;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.dao.CBHistorialSCECDAO;
import com.terium.siccam.utils.CBEstadoCuentaUtils;

public class CBEjecutaExtractoController extends GenericForwardComposer<Window> {
	private static Logger log = Logger.getLogger(CBEjecutaExtractoController.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -968506960180389882L;

	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	


	Boolean filtros = false;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		filtros = (Boolean) misession.getAttribute("filtrosprincipal");
		System.out.println("filtros es: " + filtros);

	}

	/**
	 * Agregando ejecucion de sp de extracto
	 * 
	 */

	public void onClick$btnEjecutarProcesoExtracto() {
		//DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
		CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
		
		log.debug(
				"onClick$btnEjecutarProcesoExtracto() - " + "Entra a ejecutar  sp" );
			//Logger.getLogger(CBEstadoCuentaDAO.class.getName()).log(Level.INFO,
				//	"Entra a ejecutar  sp" );

			if (objDao.ejecutaSPExtracto()) {
				Messagebox.show("Proceso Ejecutado con exito.", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
				refrescarModulo();
				log.debug(
						"onClick$btnEjecutarProcesoExtracto() - " + "****************** EJECUTA SP  exito===> ");
				//Logger.getLogger(CBEjecutaExtractoController.class.getName()).log(Level.INFO,
					//	"****************** EJECUTA SP  exito===> ");
			}
		}

	

	public void refrescarModulo() {
		CBConsultaEstadoCuentasController instanciaPrincipal = new CBConsultaEstadoCuentasController();
		instanciaPrincipal = (CBConsultaEstadoCuentasController) session.getAttribute("interfaceTarjeta");

		if (filtros) {
			instanciaPrincipal.onClick$btnConsuta();
		}
		onClick$closeBtn();

	}

	Window ejecutaextracto;

	public void onClick$closeBtn() {

		ejecutaextracto.detach();
	}

	public void limpiarCampos() {
	}

	/**
	 * 
	 * 
	 * */

}
