package com.terium.siccam.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;



import java.util.Locale;

//import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;

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
import org.zkoss.zul.Decimalbox;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class CBDetalleComisionesController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBDetalleComisionesController.class.getName());

	/**
	 * 
	 */

	Window wddetallecomisiones;
	Window wdComisionReal;
	String idseleccionado;
	int comisionesid;
	private static final long serialVersionUID = -968506960180389882L;

	private HttpSession misession = (HttpSession) Sessions.getCurrent()
			.getNativeSession();

	private List<CBDetalleComisionesModel> listaDetalleComision = null;
	CBHistorialSCECDAO objChdao = new CBHistorialSCECDAO();
	private String usuario;
	private String fecha = "";
	private int cbbancoagenciaconfrontaid = 0;
	// BigDecimal comisionReal;
	private Textbox tbxComision;
	private Button btnAgregar2;
	private Listbox lbxHistorialscec;
	// private BigDecimal validaComision = new BigDecimal(0.00);
	
	DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
	//simbolos.setDecimalSeparator('.');
	
	

	Boolean filtros = false;

	CBConciliacionBancoModel objModelModal = null;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);

		objModelModal = (CBConciliacionBancoModel) misession
				.getAttribute("objModelModal");
		cbbancoagenciaconfrontaid = objModelModal
				.getCbbancoagenciaconfrontaid();

		fecha = objModelModal.getFecha();
		usuario = obtenerUsuario().getUsuario();
		 tbxComision.setValue("0.00");
		llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
		// btnAgregar.setDisabled(false);
		limpiarCampos();

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
	public void llenaListboxTipificacion(int cbbancoagenciaconfrontaid,
			String fecha) {
		limpiarListbox(lbxHistorialscec);
		log.debug("llenaListboxTipificacion - " + "parametros em control: " + cbbancoagenciaconfrontaid);
		log.debug("llenaListboxTipificacion - " + "parametros em control2: " + fecha);
		
		listaDetalleComision = objChdao.obtenerDetalleComision(
				cbbancoagenciaconfrontaid, fecha);
		log.debug("total lista retornada en llenalistbox: " + listaDetalleComision.size());
		
		if (listaDetalleComision.size() > 0) {

			Iterator<CBDetalleComisionesModel> it = listaDetalleComision
					.iterator();
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
				log.debug("valor monto en list: " + obj.getMonto());
				
				cell.setLabel(convertirADecimal(obj.getMonto()));
				System.out.println("valor monto en lis2:"
						+ convertirADecimal(obj.getMonto()));
				cell.setParent(fila);

				cell = new Listcell();
				//cell.setLabel(convertirADecimal(obj.getComisionReal()));
				cell.setLabel(String.valueOf(obj.getComisionReal().doubleValue()));
              
				cell.setParent(fila);

				fila.setAttribute("objModel2", obj);

				
				fila.setAttribute("idseleccionado", obj.getCbcomisionid());
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

		tbxComision.setValue("0.00");
	}

	EventListener<Event> eventBtnComisionReal = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				CBConciliacionBancoModel ModelModal = (CBConciliacionBancoModel) event
						.getTarget().getAttribute("objModelModal");
				log.debug("\n**** Tipologia de poliza seleccionada ****\n");
				//Logger.getLogger(
						//CBMantenimientoTipologiasPolizaDAO.class.getName())
						//.log(Level.INFO,
							//	"\n**** Tipologia de poliza seleccionada ****\n");
				session.setAttribute("ModelModal", ModelModal);
				Executions.createComponents("/cbcomisionrealmodal.zul", null,
						null);

			} catch (Exception e) {
				log.debug("eventBtnComisionReal - Error ", e);
				//Logger.getLogger(CBTipologiasPolizaController.class.getName())
						//.log(Level.SEVERE, null, e);
			}
		}
	};

	public void onClick$btnAgregar2() {

		if (tbxComision.getValue() != null
				&& lbxHistorialscec.getSelectedItem() != null) {
			CBDetalleComisionesModel obj = lbxHistorialscec.getSelectedItem()
					.getValue();
			// validaComision = validaComision.add(new
			// BigDecimal(dmbxComision.getValue()));
			int comisionesid = obj.getCbcomisionid();

			if (tbxComision.getValue() == null) {
				Messagebox.show("Se debe ingresar una comision mayor a cero",
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);

			} else {
				try {
					

					boolean resul = objChdao.actualizaComisionReal(
							tbxComision.getValue(), comisionesid);
					

					Messagebox.show("Se creo el registro con exito",
							Constantes.ATENCION, Messagebox.OK,
							Messagebox.INFORMATION);
					
					llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
					limpiarCampos();
					// btnActualizar.setDisabled(true);
					btnAgregar2.setDisabled(false);
					// refrescarModulo();
				} catch (Exception e) {
					log.debug("eventBtnComisionReal - Error ", e);
				}
			}
			// }
		} else {
			System.out.println("La comision real es requerido");
		}

	}

	EventListener<Event> eventBtnModificar = new EventListener<Event>() {
		
		
		
		
		public void onEvent(Event arg0) throws Exception {
			
			//NumberFormat.getNumberInstance(Locale.ENGLISH).parse("265.858");
			
			 // NumberFormat spanishFormat = NumberFormat.getInstance(new
					//  Locale("en","US"));
			
			CBDetalleComisionesModel objModel2 = (CBDetalleComisionesModel) arg0
					.getTarget().getAttribute("objModel2");
			idseleccionado = String.valueOf(arg0.getTarget().getAttribute(
					"idseleccionado"));
			
			//String comision_real = formateador.format(objModel2.getComisionReal().doubleValue());
		//	String comision_real = spanishFormat.format(objModel2.getComisionReal().doubleValue());
			
			//BigDecimal valor = BigDecimal.valueOf(Double.valueOf(comision_real));
			log.debug(
					"valor de comision real en el update "  + objModel2.getComisionReal().doubleValue());
			
			tbxComision.setValue(String.valueOf(objModel2.getComisionReal().doubleValue()));
			
			

			

		}
	};

}
