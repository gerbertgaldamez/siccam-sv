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
import com.terium.siccam.dao.CBHistorialSCECDAO;
import com.terium.siccam.utils.CBEstadoCuentaUtils;

public class CBEjecutaComisionesController extends GenericForwardComposer<Window> {
	
	private static Logger log = Logger.getLogger(CBEjecutaComisionesController.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = -968506960180389882L;

	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	CBHistorialSCECDAO objChdao = new CBHistorialSCECDAO();

	// Componentes ZK
	Datebox dtbDesde;
	Datebox dtbHasta;
	Boolean filtros = false;

	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		filtros = (Boolean) misession.getAttribute("filtrosprincipal");
		System.out.println("filtros es: " + filtros);

	}

	/**
	 * Agregando ejecucion de sp de comisiones\
	 * 
	 */

	public void onClick$btnEjecutarProcesoComisiones() {
		//DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
		CBConciliacionBancoDAO objDao = new CBConciliacionBancoDAO();
		Date fechainicio;
		Date fechafin;
		if (dtbDesde.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha de inicio.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		} else if (dtbHasta.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha hasta.", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		} else if (dtbDesde.getValue().after(dtbHasta.getValue())) {
			Messagebox.show("La fecha desde debe ser menor a la fecha hasta.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else {
			/*
			 * fechainicio = (fechaFormato.format((dtbDesde.getValue()))); fechafin =
			 * (fechaFormato.format((dtbHasta.getValue())));
			 */
			fechainicio = dtbDesde.getValue();
			fechafin = dtbHasta.getValue();
			log.debug(
					"onClick$btnEjecutarProcesoComisiones() - " + "fecha inicio envio sp" + fechainicio);
			//Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.INFO,
				//	"fecha inicio envio sp" + fechainicio);
			log.debug(
					"onClick$btnEjecutarProcesoComisiones() - " + "fecha fin envio sp" + fechafin);
		//	Logger.getLogger(CBConciliacionBancoDAO.class.getName()).log(Level.INFO, "fecha fin envio sp" + fechafin);

			if (objDao.ejecutaSPComisiones(fechainicio, fechafin)) {
				Messagebox.show("Proceso Ejecutado con exito.", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
				refrescarModulo();
				log.debug(
						"onClick$btnEjecutarProcesoComisiones() - " + "****************** EJECUTA SP  exito===> ");
				//Logger.getLogger(CBEjecutaComisionesController.class.getName()).log(Level.INFO,
					//	"****************** EJECUTA SP  exito===> ");
			}
		}

	}

	public void refrescarModulo() {
		CBConciliacionBancoController instanciaPrincipal = new CBConciliacionBancoController();
		instanciaPrincipal = (CBConciliacionBancoController) session.getAttribute("interfaceTarjeta");

		if (filtros) {
			instanciaPrincipal.onClick$btnBuscar();
		}
		onClick$closeBtn();

	}

	Window ejecutacomisiones;

	public void onClick$closeBtn() {

		ejecutacomisiones.detach();
	}

	public void limpiarCampos() {
	}

	/**
	 * 
	 * 
	 * */

}
