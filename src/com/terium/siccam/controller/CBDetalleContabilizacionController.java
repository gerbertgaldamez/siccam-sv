package com.terium.siccam.controller;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBBitacoraLogDAO;
import com.terium.siccam.model.CBBitacoraLogModel;
import com.terium.siccam.model.CBConciliacionBancoModel;

public class CBDetalleContabilizacionController extends ControladorBase {

	/**
	 * 
	 */

	Window wddetallecontabilizacion;

	private static final long serialVersionUID = -968506960180389882L;

	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	private List<CBBitacoraLogModel> listaDetalleContabilizacion = null;
	CBBitacoraLogDAO objChdao = new CBBitacoraLogDAO();
	private String usuario;

	private String fechaDesde = "";
	private String fechaHasta = "";
	private String pais = "";
	private String token = "";
	
	
	private Listbox lbxHistorialscec;

	Boolean filtros = false;

	CBConciliacionBancoModel objModelModal = null;
	
	private Label lblFechas;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);


		pais = (String) misession.getAttribute("paisActual");
		token = (String) misession.getAttribute("sesiontoken");
		
		fechaDesde = (String) misession.getAttribute("fechaDesdeConta");
		fechaHasta = (String) misession.getAttribute("fechaHastaConta");
		
		llenaListboxTipificacion(token, pais, true );
		
		misession.setAttribute("instanciaModalDetalle", CBDetalleContabilizacionController.this);
		

	}



	/**
	 * 
	 * */
	public void llenaListboxTipificacion( String token, String pais, Boolean inicio) {
		limpiarListbox(lbxHistorialscec);
		System.out.println("Token: " + token);
		listaDetalleContabilizacion = objChdao.obtenerDetalleContabilizacion( token, pais, inicio);
		System.out.println("total lista retornada en llenalistbox:" + listaDetalleContabilizacion.size());
		if (listaDetalleContabilizacion.size() > 0) {

			Iterator<CBBitacoraLogModel> it = listaDetalleContabilizacion.iterator();
			CBBitacoraLogModel obj = null;
			Listcell cell = null;
			Listitem fila = null;
			
			String estado = "";
			
			while (it.hasNext()) {
				obj = it.next();
				
				if("0".equals(obj.getTipoCarga()))
					estado = "En proceso.";
				else if("1".equals(obj.getTipoCarga()))
					estado = "No se encontro informacion.";
				else
					estado = "Informacion cargada exitosamente.";

				fila = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(obj.getAccion());
				cell.setParent(fila);

				cell = new Listcell();
				cell.setLabel(estado);
				cell.setParent(fila);
				
				
				//
				fila.setValue(obj);
				fila.setParent(lbxHistorialscec);
			}

		} else {

		}
		
		if(inicio) {
			lblFechas.setValue("Fechas seleccionadas: " + fechaDesde + " - " + fechaHasta);
		}
	}

	public boolean limpiarListbox(Listbox componente) {
		Boolean result = false;
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);
			}
			result = true;
		}
		
		return result;
	}

	public void onClick$closeBtn() {

		wddetallecontabilizacion.detach();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public void onClick$btnCargarInfo() {
		limpiarListbox(lbxHistorialscec);
		
		CBConsultaContabilizacionController obj = (CBConsultaContabilizacionController) misession.getAttribute("instanciaConta");
		obj.GenerarInfo();
		
		/*try {
			if(limpiarListbox(lbxHistorialscec)) {
				Thread.sleep(10000);
				CBConsultaContabilizacionController obj = (CBConsultaContabilizacionController) misession.getAttribute("instanciaConta");
				obj.GenerarInfo();
			//}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(CBDetalleContabilizacionController.class.getName()).log(Level.SEVERE, null, e);
		}*/
	}
	
	public void onClick$btnActualizar() {
		llenaListboxTipificacion(token, pais, true );
	}
}
