package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
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

import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.utils.UtilidadesReportes;

public class CBConsultaDetalleCargadosController extends ControladorBase {
	private static Logger log = Logger.getLogger(CBConsultaDetalleCargadosController.class.getName());

	/**
	 * 
	 */
	private static final long serialVersionUID = 9176164927878418930L;

	Window detalleCargdos;
	Listbox lbxDetalle;
	List<CBDataBancoModel> listDetalleAsignado;
	String idArchivosCargados;
	String usuario;
	DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
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
		listDetalleAsignado = cbaidao.obtieneListadoGrabados(paramIdArchivosCargados);
		System.out.println("Este es el Id para consultar detalle gravados" +" "+ paramIdArchivosCargados);
		
		if (listDetalleAsignado != null && listDetalleAsignado.size() > 0) {
			System.out.println("Entra a llenar listbox: "+listDetalleAsignado.size());
			Iterator<CBDataBancoModel> it = listDetalleAsignado.iterator();
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
	
	public void onClick$btnGeneraReporte() throws Exception {
		
		log.debug("onClick$btnGeneraReporte() - " + "Comienza a generar el reporte de detalle asignacion...");
		if (listDetalleAsignado != null)
			
		try {
			generarReporteDetalleAsignacion();
		} catch (Exception e) {
			log.error("onClick$btnGeneraReporte() - Error : ", e);
		}
	}
	
	public void generarReporteDetalleAsignacion() throws Exception {
		
		
		if (listDetalleAsignado != null && listDetalleAsignado.size() > 0) {
			
			Date fecha = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
			
			Iterator<CBDataBancoModel> iterator = listDetalleAsignado.iterator();
			
			// Encabezado del archivo
	String encabezado = "CODIGO CLIENTE|TELEFONO|TIPO|FECHA|CATALOGO BANCO ID|CATALOGO AGENCIA ID|MONTO|TRANSACCION|"
	+ "ESTADO|MES|TEXTO 1|TEXTO 2|CREADO POR\n";
	
	// Creando archivo
				File file = new File("reporte_detalle_grabados" + sdf.format(fecha) + ".csv");
				
				BufferedWriter bw = null;
				
				
				try {
					log.debug("try - " + "entra al try ");
					bw = new BufferedWriter(new FileWriter(file));
					
					bw.write(encabezado);
			
					CBDataBancoModel c = null;
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

					while (iterator.hasNext()) {
						c = iterator.next();
						
						bw.write( UtilidadesReportes.changeNull(c.getCodCliente()) + "|"
								+ UtilidadesReportes.changeNull(c.getTelefono()) + "|"
								+ UtilidadesReportes.changeNull(c.getTipo()) + "|"
								+ format.format(c.getFecha()) + "|"
								+ UtilidadesReportes.changeNull(c.getcBCatalogoBancoId()) + "|"
								+ UtilidadesReportes.changeNull(c.getcBCatalogoAgenciaId()) + "|"
								+ UtilidadesReportes.changeNull(String.valueOf(c.getMonto())) + "|"
								+ UtilidadesReportes.changeNull(c.getTransaccion()) + "|"
								+ UtilidadesReportes.changeNull(String.valueOf(c.getEstado())) + "|"
								+ UtilidadesReportes.changeNull(String.valueOf(c.getMes())) + "|"
								//+ format.format(c.getMes())+ "|"
								+ UtilidadesReportes.changeNull(c.getTexto1()) + "|"
								+ UtilidadesReportes.changeNull(c.getTexto2()) + "|"
								+ UtilidadesReportes.changeNull(c.getCreadoPor()) + "\n");
					} 

					Filedownload.save(file, null);

					Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
							Messagebox.OK, Messagebox.INFORMATION);

				} catch (Exception e) {
					
					
					log.error("generarReporteDetalleAsignacion() - Error : ", e);
					
				} finally {
					if (bw != null)
						bw.close();
				}

			} else
				Messagebox.show("No hay resultados en la búsqueda para generar reportes", "ATENCIÓN", Messagebox.OK,
						Messagebox.EXCLAMATION);
		}
			
		}
		

