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
import com.terium.siccam.model.CBDataBancoModel;

public class CBConsultaDetalleCargadosController extends ControladorBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9176164927878418930L;

	Window detalleCargdos;
	Listbox lbxDetalle;

	String idArchivosCargados;
	String usuario;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		// usuario = obtenerUsuario().getUsuario();
		// usuario = obtenerUsuario().getUsuario();

		idArchivosCargados = (String) misession.getAttribute("sesionabrirDetalleCargados");
		llenalistboxDetalleCargas(idArchivosCargados);

	}

	public void llenalistboxDetalleCargas(String paramIdArchivosCargados) {

		CBArchivosInsertadosDAO cbaidao = new CBArchivosInsertadosDAO();
		List<CBDataBancoModel> list = cbaidao.obtieneListadoGrabados(paramIdArchivosCargados);
		System.out.println("Este es el Id para consultar detalle gravados" +" "+ paramIdArchivosCargados);
		if (list != null && list.size() > 0) {
			System.out.println("Entra a llenar listbox: "+list.size());
			Iterator<CBDataBancoModel> it = list.iterator();
			CBDataBancoModel cbainsertados = null;
			Listitem item = null;
			Listcell cell = null;
			int contador = 0;
			while (it.hasNext()) {
				cbainsertados = it.next();
				
				item = new Listitem();
				contador++;	
				
				cell = new Listcell();
				cell.setLabel(cbainsertados.getCodCliente());
				cell.setParent(item);

				if(contador==1)
				System.out.println("numTelefono: "+cbainsertados.getTelefono());
				
				cell = new Listcell();
				cell.setLabel(cbainsertados.getTelefono());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTipo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getFecha());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getcBCatalogoBancoId());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getcBCatalogoAgenciaId());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(cbainsertados.getMonto()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTransaccion());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(cbainsertados.getEstado()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getMes());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTexto1());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTexto2());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getCreadoPor());
				cell.setParent(item);

				item.setValue(cbainsertados);
				item.setParent(lbxDetalle);
			}
			System.out.println("Esto intento llenar: "+contador);
		} else {
			System.out.println("No hay datos para consulta en consola");
		}

	}

	public void onClick$btnCerrarDetalleCargados() {

		detalleCargdos.detach();
	}

}
