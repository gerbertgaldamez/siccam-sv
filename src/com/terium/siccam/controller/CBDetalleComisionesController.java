package com.terium.siccam.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.terium.siccam.dao.CBHistorialSCECDAO;
import com.terium.siccam.model.CBAsignaImpuestosModel;
import com.terium.siccam.model.CBConciliacionBancoModel;
import com.terium.siccam.model.CBDetalleComisionesModel;
import com.terium.siccam.model.CBHistorialSCECModel;

public class CBDetalleComisionesController extends ControladorBase {

	/**
	 * 
	 */

	Window wddetallecomisiones;

	private static final long serialVersionUID = -968506960180389882L;

	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	private List<CBDetalleComisionesModel> listaDetalleComision = null;
	CBHistorialSCECDAO objChdao = new CBHistorialSCECDAO();
	private String usuario;
	private String fecha = "";
	private int cbbancoagenciaconfrontaid = 0;

	private Listbox lbxHistorialscec;

	Boolean filtros = false;

	CBConciliacionBancoModel objModelModal = null;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		objModelModal = (CBConciliacionBancoModel) misession.getAttribute("objModelModal");
		cbbancoagenciaconfrontaid = objModelModal.getCbbancoagenciaconfrontaid();
		fecha = objModelModal.getFecha();
		llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);

	}

	// agre metodo Ovidio
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
		return numero;
	}

	/**
	 * 
	 * */
	public void llenaListboxTipificacion(int cbbancoagenciaconfrontaid, String fecha) {
		limpiarListbox(lbxHistorialscec);
		System.out.println("parametros em control: " + cbbancoagenciaconfrontaid);
		System.out.println("parametros em control2: " + fecha);
		listaDetalleComision = objChdao.obtenerDetalleComision(cbbancoagenciaconfrontaid, fecha);
		System.out.println("total lista retornada en llenalistbox:" + listaDetalleComision.size());
		if (listaDetalleComision.size() > 0) {

			Iterator<CBDetalleComisionesModel> it = listaDetalleComision.iterator();
			CBDetalleComisionesModel obj = null;
			Listcell cell = null;
			Listitem fila = null;
			while (it.hasNext()) {
				obj = it.next();

				fila = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(obj.getNombreFormaPago());
				cell.setParent(fila);

				cell = new Listcell();
				cell.setLabel(obj.getNombreTipo());
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNombreTipologia());
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNombreImpuesto());
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNombreMedioPago());
				cell.setParent(fila);

				cell = new Listcell();
				System.out.println("valor monto en list:" + obj.getMonto());
				cell.setLabel(convertirADecimal(obj.getMonto()));
				System.out.println("valor monto en lis2:" + convertirADecimal(obj.getMonto()));
				cell.setParent(fila);
				
				cell = new Listcell();
				cell.setLabel(obj.getNombreMedioPago());
				cell.setParent(fila);

				//
				fila.setValue(obj);
				fila.setParent(lbxHistorialscec);
			}

		} else {

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

	public void onClick$closeBtn() {

		wddetallecomisiones.detach();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
