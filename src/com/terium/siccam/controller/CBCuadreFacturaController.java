package com.terium.siccam.controller;



import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBCuadreSidraDAO;
import com.terium.siccam.model.CBCuadreSidraModel;



public class CBCuadreFacturaController extends ControladorBase{
	private static final long serialVersionUID = 1L;
	Listbox lbxCuadreSidra;
	
	CBCuadreSidraDAO cuadreSidra = new CBCuadreSidraDAO();
	List<CBCuadreSidraModel> lst;
	
	public void doAfterCompose(Component comp) {
		

}
	public void listarConciliaciones() {
		CBCuadreSidraModel objModel = new CBCuadreSidraModel();
		lst = cuadreSidra.obtenerCuadreSidra(objModel);

		try {
			limpiarListboxYPaginas(lbxCuadreSidra);

			// lstToda = new ArrayList<CBResumenDiarioConciliacionModel>();
			try {
				if (lst.size() > 0) {
					Iterator<CBCuadreSidraModel> ilst = lst.iterator();
					CBCuadreSidraModel adr = null;
					Listcell cell = null;
					Listitem fila = null;
					while (ilst.hasNext()) {
						adr = ilst.next();
						fila = new Listitem();

						/** Agrupacion principal **/
						// Serie
						cell = new Listcell();
						cell.setLabel(adr.getSerie());
						cell.setParent(fila);

						// NOMBRE
						cell = new Listcell();
						cell.setLabel(adr.getNombreCliente());
						cell.setParent(fila);

						// CODIGO COLECTOR
						// CarlosGodinez -> 07/08/2018
						cell = new Listcell();
						cell.setLabel(adr.getNombreClienteFinal());
						cell.setParent(fila);

						// TIPO
						cell = new Listcell();
						cell.setLabel(adr.getBillRefNo());
						cell.setParent(fila);

						/** Agrupacion BANCO **/
						// TRANAS BANCO
						cell = new Listcell();
						
						cell.setLabel(adr.getFechaPago());
						cell.setParent(fila);

						// PAGOS BANCO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getMontoPago()).toString());
						cell.setParent(fila);

						// MANUAL BANCO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getEstadoFactura());
						cell.setParent(fila);

						// REAL BANCO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getFechaSincronizacion());
						cell.setParent(fila);

						// Pendiente Banco
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getNoBoleta());
						cell.setParent(fila);

						/** Agrupacion SISTEMA COMERCIAL **/
						// TRANS TELEFONICA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getFechaBoleta());
						cell.setParent(fila);

						// PAGOS TELEFONICA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getMontoBoleta()));
						cell.setParent(fila);

						// MANUAL TELEFONICA
						cell = new Listcell();
						
						cell.setLabel(adr.getJornada());
						cell.setParent(fila);

						// REAL TELEFONICA
						cell = new Listcell();
						
						cell.setLabel(adr.getFechaInicioJ());
						cell.setParent(fila);

						
						cell.setLabel(adr.getFechaLiquidacionJ());
						cell.setParent(fila);

						/** Agrupacion RESULTADOS **/

						// estaba comentariada
						// Transacciones Telca
						cell = new Listcell();
						
						cell.setLabel(adr.getEstadoJornada());
						cell.setParent(fila);

						// CONCILIADAS
						cell = new Listcell();
						
						cell.setLabel(adr.getTipoRutaPanel());
						cell.setParent(fila);

						// DIFERENCIA TRANSACCION
						cell = new Listcell();
						
						cell.setLabel(adr.getNombrerutaP());
						cell.setParent(fila);

						// AUTOMATICA
						cell = new Listcell();
						
						cell.setLabel(adr.getNombreVendedor());
						cell.setParent(fila);

						

						cell.setParent(fila);

						fila.setValue(adr);
						// lstToda.add(adr);

					

						fila.setParent(lbxCuadreSidra);

					}
				}
			} catch (Exception e) {
				//log.error("listarConciliaciones() - Error: ", e);
			}
		} catch (Exception e) {
			//log.error("listarConciliaciones() - Error limpiar lista : ", e);
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
			// si es numero negativo lo pasamos a positivo
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

}
