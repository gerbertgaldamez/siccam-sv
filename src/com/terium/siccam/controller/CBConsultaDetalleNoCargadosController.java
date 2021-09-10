package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import java.util.List;

import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBArchivosInsertadosDAO;

import com.terium.siccam.model.CBDataSinProcesarModel;
import com.terium.siccam.utils.UtilidadesReportes;

public class CBConsultaDetalleNoCargadosController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBConsultaDetalleNoCargadosController.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 9176164927878418930L;

	Window detalleNoCargados;
	Listbox lbxDetalleNoCargados;
	List<CBDataSinProcesarModel> listDetalleAsignado;
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
		//List<CBDataSinProcesarModel> list = cbaidao.obtieneListadoDatosNoProcesados(paramIdArchivosCargados);
		listDetalleAsignado = cbaidao.obtieneListadoDatosNoProcesados(paramIdArchivosCargados);
		System.out.println("Este es el Id para consultar detalle gravados" +" "+ paramIdArchivosCargados);
		if (listDetalleAsignado != null && listDetalleAsignado.size() > 0) {
			System.out.println("Entra a llenar listbox: "+listDetalleAsignado.size());
			Iterator<CBDataSinProcesarModel> it = listDetalleAsignado.iterator();
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
public void onClick$btnGeneraReporte() throws Exception {
		
		log.debug("onClick$btnGeneraReporte() - " + "Comienza a generar el reporte de detalle no grabado...");
		if (listDetalleAsignado != null)
			
		try {
			generarReporteDetalleNoCargado();
		} catch (Exception e) {
			log.error(e);
		}
	}
public void generarReporteDetalleNoCargado() throws Exception {
	
	
	if (listDetalleAsignado != null && listDetalleAsignado.size() > 0) {
		
		Date fecha = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		
		Iterator<CBDataSinProcesarModel> iterator = listDetalleAsignado.iterator();
		
		// Encabezado del archivo
String encabezado = "NOMBRE ARCHIVO|DATA ARCHIVO|CAUSA|ESTADO|CREADO POR\n";

// Creando archivo
			File file = new File("reporte_detalle_no_grabados" + sdf.format(fecha) + ".csv");
			
			BufferedWriter bw = null;
			
			
			try {
				
				bw = new BufferedWriter(new FileWriter(file));
				
				bw.write(encabezado);
		
				CBDataSinProcesarModel c = null;
				

				while (iterator.hasNext()) {
					c = iterator.next();
					
					bw.write( UtilidadesReportes.changeNull(c.getNombreArchivo()) + "|"
							+ UtilidadesReportes.changeNull(c.getDataArchivo()) + "|"
							+ UtilidadesReportes.changeNull(c.getCausa()) + "|"
							+ UtilidadesReportes.changeNumber(String.valueOf(c.getEstado()) + "|"
							+ UtilidadesReportes.changeNumber(String.valueOf(c.getCreadoPor()))) + "\n");
				} 

				Filedownload.save(file, null);

				Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
						Messagebox.OK, Messagebox.INFORMATION);

			} catch (Exception e) {
				
				log.error(e);
			} finally {
				if (bw != null)
					bw.close();
			}

		} else
			Messagebox.show("No hay resultados en la búsqueda para generar reportes", "ATENCIÓN", Messagebox.OK,
					Messagebox.EXCLAMATION);
	}

}
