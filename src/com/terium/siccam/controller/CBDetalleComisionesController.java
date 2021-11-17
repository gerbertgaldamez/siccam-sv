package com.terium.siccam.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBHistorialSCECDAO;
import com.terium.siccam.dao.CBMantenimientoTipologiasPolizaDAO;
import com.terium.siccam.model.CBAsignaImpuestosModel;
import com.terium.siccam.model.CBCausasModel;
import com.terium.siccam.model.CBConciliacionBancoModel;
import com.terium.siccam.model.CBDetalleComisionesModel;
import com.terium.siccam.model.CBHistorialSCECModel;
import com.terium.siccam.utils.Constantes;

public class CBDetalleComisionesController extends ControladorBase {

	/**
	 * 
	 */

	Window wddetallecomisiones;
	Window wdComisionReal;
	String idseleccionado;
	private static final long serialVersionUID = -968506960180389882L;

	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	private List<CBDetalleComisionesModel> listaDetalleComision = null;
	CBHistorialSCECDAO objChdao = new CBHistorialSCECDAO();
	private String usuario;
	private String fecha = "";
	private int cbbancoagenciaconfrontaid = 0;
	BigDecimal comisionReal;
	private Textbox dmbxComision;
	private Button btnAgregar2;
	private Listbox lbxHistorialscec;
	private BigDecimal validaComision = new BigDecimal(0.00);

	Boolean filtros = false;

	CBConciliacionBancoModel objModelModal = null;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		objModelModal = (CBConciliacionBancoModel) misession.getAttribute("objModelModal");
		cbbancoagenciaconfrontaid = objModelModal.getCbbancoagenciaconfrontaid();
		
		
		fecha = objModelModal.getFecha();
		usuario = obtenerUsuario().getUsuario();
		
		llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
		//btnAgregar.setDisabled(false);
		

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
				
				/*cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/consulta.png");

				cell.setParent(fila);
				btnDelete.setParent(cell);
				// btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("ModelModal", obj);
				btnDelete.addEventListener(Events.ON_CLICK, eventBtnComisionReal);*/
				cell = new Listcell();
				//cell.setLabel(convertirADecimal(obj.getComisionReal()));
				cell.setLabel(convertirADecimal(validaComision));
				//fila.setAttribute("objModel2", obj);
				cell.setParent(fila);
				fila.setAttribute("idseleccionado", obj.getCbcomisionesconfrontaid());
				fila.addEventListener("onClick", eventBtnModificar);
				
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
		wdComisionReal.detach();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	public void onClick$btnLimpiar() {
		
		btnAgregar2.setDisabled(false);
	}
	public void limpiarCampos() {
		
		btnAgregar2.setDisabled(false);
		
		dmbxComision.setValue("0.00");
	}
	EventListener<Event> eventBtnComisionReal = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				CBConciliacionBancoModel ModelModal = (CBConciliacionBancoModel) event.getTarget()
						.getAttribute("objModelModal");
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
						"\n**** Tipologia de poliza seleccionada ****\n");
				session.setAttribute("ModelModal", ModelModal);
				Executions.createComponents("/cbcomisionrealmodal.zul", null, null);

			} catch (Exception e) {
				Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};
   public void onClick$btnAgregar2() {
		
		
		if (dmbxComision.getValue() != null) {
			 validaComision = validaComision.add(new BigDecimal(dmbxComision.getValue()));
			System.out.println("Comision al guardar para validar validaComision: " + validaComision);
			
				 if (new BigDecimal(dmbxComision.getValue()).compareTo(BigDecimal.ZERO) == 0) {
					Messagebox.show("Se debe ingresar una comision mayor a cero", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
					/*try {
						CBHistorialSCECModel objModel = new CBHistorialSCECModel();
						objModel.setCbbancoagenciaconfrontaid(cbbancoagenciaconfrontaid);
						objModel.setFecha(fecha);
						objModel.setCreadopor(usuario);
						objModel.setComisionReal(new BigDecimal(dmbxComision.getValue()));
			
						objChdao.ingresaComision(objModel);
						
						Messagebox.show("Se creo el registro con exito", Constantes.ATENCION, Messagebox.OK,
								Messagebox.INFORMATION);
						System.out.println("registro guardado: " + objModel.getMonto());
						llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
						limpiarCampos();
					//	btnActualizar.setDisabled(true);
						btnAgregar.setDisabled(false);
						//refrescarModulo();
					} catch (Exception e) {
						Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
					}*/
				} else {
					try {
						CBHistorialSCECModel objModel = new CBHistorialSCECModel();
						CBDetalleComisionesModel obj = new CBDetalleComisionesModel();
						int comisionesid = obj.getCbcomisionid();
						BigDecimal comisionReal = validaComision;
						System.out.println("comision id es : " + comisionesid);
						System.out.println("la comision real es : " + comisionReal);
						
						//objModel.setCbbancoagenciaconfrontaid(cbbancoagenciaconfrontaid);
						//objModel.setFecha(fecha);
						//objModel.setCreadopor(usuario);
						System.out.println("el usuario es : " + usuario);
						objModel.setComisionReal(new BigDecimal(dmbxComision.getValue()));
						objModel.setCbcomisionid(comisionesid);
						//objModel.setCbpagosid(cbpagosid);
			           // objModel.setTipo(tipo);
						
						boolean resul = objChdao.actualizaComisionReal(comisionReal,comisionesid);
						System.out.println("Actualiza comision Real: " +  resul);
						
						Messagebox.show("Se creo el registro con exito", Constantes.ATENCION, Messagebox.OK,
								Messagebox.INFORMATION);
						System.out.println("registro guardado: " + objModel.getMonto());
						llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
						limpiarCampos();
					//	btnActualizar.setDisabled(true);
						btnAgregar2.setDisabled(false);
						//refrescarModulo();
					} catch (Exception e) {
						Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
					}
				}
			//}
		} else {
			System.out.println("La comision real es requerido");
		}
		
		
	}
   EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {
			
			CBDetalleComisionesModel objModel2 = (CBDetalleComisionesModel) arg0.getTarget().getAttribute("objModel2");
			idseleccionado = String.valueOf( arg0.getTarget().getAttribute("idseleccionado"));
			
			System.out.println("el idseleccionado es " + idseleccionado);

			dmbxComision.setText(String.valueOf(convertirADecimal(objModel2.getComisionReal())));
			

		
		}
	};
	
}
