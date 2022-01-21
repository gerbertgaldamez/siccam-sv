package com.terium.siccam.controller;



import java.math.BigDecimal;

import org.apache.log4j.Logger;




import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;




import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCuadreSidraDAO;
import com.terium.siccam.model.CBCuadreSidraModel;
import com.terium.siccam.utils.Constantes;



public class CBCuadreFacturaController extends ControladorBase{
	private static Logger log = Logger.getLogger(CBCuadreFacturaController.class);
	private static final long serialVersionUID = 1L;
	Listbox lbxCuadreSidra;
	private Datebox dtbDesde;
	private Datebox dtbHasta;
	private Combobox cmbxExiste;
	Datebox dtbDia;
	DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
	
	CBCuadreSidraDAO cuadreSidra = new CBCuadreSidraDAO();
	List<CBCuadreSidraModel> lst;
	
	public void doAfterCompose(Component comp) {
		
		try {
			super.doAfterCompose(comp);
		} catch (Exception e) {
			log.error("doAfterCompose() - Error: ", e);
			Messagebox.show("Ha ocurrido un error", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
}
	public void listarConciliaciones(CBCuadreSidraModel objModel, int banderaMensaje) {
		//CBCuadreSidraDAO objDao = new CBCuadreSidraDAO();
		lst = cuadreSidra.obtenerCuadreSidra(objModel);

		try {
			limpiarListboxYPaginas(lbxCuadreSidra);

			
			
				if (lst.size() > 0) {
					Iterator<CBCuadreSidraModel> ilst = lst.iterator();
					CBCuadreSidraModel adr = null;
					Listcell cell = null;
					Listitem fila = null;
					while (ilst.hasNext()) {
						adr = ilst.next();
						fila = new Listitem();

					/*	cell = new Listcell();
						cell.setLabel(adr.getNumFactura());
						cell.setParent(fila);
						
						cell = new Listcell();
						cell.setLabel(adr.getFechaFactura());
						cell.setParent(fila);*/
						// Serie
						cell = new Listcell();
						cell.setLabel(adr.getSerie());
						cell.setParent(fila);
						
						/*cell = new Listcell();
						cell.setLabel(adr.getCodFactura());
						cell.setParent(fila);*/
						

						// NOMBRE CLIENTE
						cell = new Listcell();
						cell.setLabel(adr.getNombreCliente());
						cell.setParent(fila);
						
						// NOMBRE CLIENTE FINAL
						
						cell = new Listcell();
						cell.setLabel(adr.getNombreClienteFinal());
						cell.setParent(fila);
						
						// BILL REF NO
						cell = new Listcell();
						cell.setLabel(adr.getBillRefNo());
						cell.setParent(fila);
						
						
						// FECHA PAGO
						cell = new Listcell();
						cell.setLabel(adr.getFechaPago());
						cell.setParent(fila);
						
						// MONTO PAGO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getMontoPago()).toString());
						cell.setParent(fila);
						
						// ESTADO FACTURA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getEstadoFactura());
						cell.setParent(fila);
						
						// FECHA SINCRONIZACION
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getFechaSincronizacion());
						cell.setParent(fila);
					
						// NO BOLETA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getNoBoleta());
						cell.setParent(fila);

						
						// FECHA BOLETA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getFechaBoleta());
						cell.setParent(fila);
						
						// NOMTO BOLETA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getMontoBoleta()));
						cell.setParent(fila);
						
						// JORNADA
						cell = new Listcell();
						cell.setLabel(adr.getJornada());
						cell.setParent(fila);
						
						// FECHA INICIO JORNADA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						//cell.setLabel(fechaFormato.format(adr.getFechaInicioJ()));
						cell.setLabel(adr.getFechaInicioJ());
						cell.setParent(fila);
					
						//FECHA LIQUIDACION JORNADA
						cell = new Listcell();
						cell.setLabel(adr.getFechaLiquidacionJ());
						cell.setParent(fila);

						
						
						// ESTADO JORNADA
						cell = new Listcell();
						cell.setLabel(adr.getEstadoJornada());
						cell.setParent(fila);
						
						// TIPO RUTA PANEL
						cell = new Listcell();
						cell.setLabel(adr.getTipoRutaPanel());
						cell.setParent(fila);
						
						// NOMBRE RUTA PANEL
						cell = new Listcell();
						cell.setLabel(adr.getNombrerutaP());
						cell.setParent(fila);
						
						// NOMBRE VENDEDOR
						cell = new Listcell();
						cell.setLabel(adr.getNombreVendedor());
						cell.setParent(fila);
						
						cell = new Listcell();
						cell.setLabel(adr.getExiste());
						cell.setParent(fila);
						
						
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getTotalArbor()!=null?convertirADecimal(adr.getTotalArbor()).toString():"0");
						cell.setParent(fila);
						
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getTotalPagado()!=null?convertirADecimal(adr.getTotalPagado()).toString():"0");
						cell.setParent(fila);
						
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getMontoPagadoBmf());
						cell.setParent(fila);
						
						
						cell.setParent(fila);

						fila.setValue(adr);
						

					fila.setParent(lbxCuadreSidra);

					}
				}else {
					
					if(banderaMensaje == 0) { 
						Messagebox.show("No existen registros para los filtros aplicados", 
								"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
					} 
				}
				
			} catch (Exception e) {
				log.error("listarConciliaciones() - Error: ", e);
			}
		

	}
	public String convertirADecimal(BigDecimal num) {
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(2);
		df.setMaximumFractionDigits(2);
		String numero = null;
		BigDecimal numConv = num;
		if (num.compareTo(BigDecimal.ZERO) == 0) {
			numConv = new BigDecimal("0.00");
		}
		numero = df.format(numConv);
		if (numConv.compareTo(BigDecimal.ZERO) == -1) {
			
			numConv = numConv.negate();
		}
		return numero;
	}
	public void limpiarListboxYPaginas(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);

			}
		}
	}
	public void onClick$btnConsulta() {
		log.debug("onClick$btnConsulta() - entra al metodo de consulta : ");
		realizaBusqueda(0);
		log.debug("onClick$btnConsulta() - entra al metodo de consulta : ");
	}
	public void realizaBusqueda(int banderaMensaje) {
		log.debug("realizaBusqueda() - entra al metodo realiza busqueda : ");
		try { 
			CBCuadreSidraModel objModel = new CBCuadreSidraModel();
			
			limpiarListbox(lbxCuadreSidra);
			DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yy");
			
			if(dtbDesde.getValue() == null) {
				Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			}else if(dtbHasta.getValue() == null){
				Messagebox.show("Debe ingresar la fecha fin antes de consultar datos.",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			} else if(dtbDesde.getValue().after(dtbHasta.getValue())){
				Messagebox.show("La fecha desde debe ser menor a la fecha hasta.",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
				return;
			}else {
				String existe = "2";
				//objModel = lbxCuadreSidra.getSelectedItem().getValue();
				
				/*if(cmbxExiste.getValue() != null) {
					existe = String.valueOf(cmbxExiste.getItems());
					
					
				} else {
					existe = "";
					
					
				}*/
				log.debug("realizaBusqueda() - nombre : " + existe);
				if(cmbxExiste.getSelectedItem()!= null){
				existe = cmbxExiste.getSelectedItem().getValue();
				}
				objModel.setExiste(existe);
				objModel.setFechaInicio(dtbDesde.getText());
				objModel.setFechaFin(dtbHasta.getText());
				listarConciliaciones( objModel,  banderaMensaje);
				
			}
			
		
		
		
		
		} catch (Exception e) {
			log.error("realizaBusqueda() - Error ", e);
			
			Messagebox.show("Ha ocurrido un error", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
		}
		

}
	public void limpiarListbox(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);
			}
		}
	}
}
