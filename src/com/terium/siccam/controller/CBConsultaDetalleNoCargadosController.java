package com.terium.siccam.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;
import com.terium.siccam.model.CBDataSinProcesarModel;

public class CBConsultaDetalleNoCargadosController extends ControladorBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9176164927878418930L;

	Window detalleNoCargados;
	Listbox lbxDetalleNoCargados;

	String idArchivosCargados;
	String usuario;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		usuario = obtenerUsuario().getUsuario();
		

		idArchivosCargados = (String) misession.getAttribute("sesionabrirDetalleNoCargados");
		llenalistboxDetalleCargas(idArchivosCargados);

	}

	public void llenalistboxDetalleCargas(String paramIdArchivosCargados) {

		CBArchivosInsertadosDAO cbaidao = new CBArchivosInsertadosDAO();
		List<CBDataSinProcesarModel> list = cbaidao.obtieneListadoDatosNoProcesados(paramIdArchivosCargados);
		System.out.println("Este es el Id para consultar detalle gravados" +" "+ paramIdArchivosCargados);
		if (list != null && list.size() > 0) {
			System.out.println("Entra a llenar listbox: "+list.size());
			Iterator<CBDataSinProcesarModel> it = list.iterator();
			CBDataSinProcesarModel cbainsertados = null;
			Listitem item = null;
			Listcell cell = null;
			int contador = 0;
			while (it.hasNext()) {
				cbainsertados = it.next();
				
				item = new Listitem();
				contador++;	
				
				cell = new Listcell();
				cell.setLabel(cbainsertados.getNombreArchivo());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(cbainsertados.getDataArchivo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getCausa());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(cbainsertados.getEstado()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getCreadoPor());
				cell.setParent(item);


				item.setValue(cbainsertados);
				item.setParent(lbxDetalleNoCargados);
			}
			System.out.println("Esto intento llenar: "+contador);
		} else {
			System.out.println("No hay datos para consulta en consola");
		}

	}

	public void onClick$btnDetalleNoCargdos() {

		detalleNoCargados.detach();
	}

}
