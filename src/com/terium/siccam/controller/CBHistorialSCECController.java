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
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBBitacoraLogDAO;
import com.terium.siccam.dao.CBHistorialSCECDAO;
import com.terium.siccam.model.CBBitacoraLogModel;
import com.terium.siccam.model.CBHistorialSCECModel;
import com.terium.siccam.utils.Constantes;

public class CBHistorialSCECController extends ControladorBase  {
	
	/**
	 * 
	 */
	
	
	Window wdHistorialScec;
	
	private static final long serialVersionUID = -968506960180389882L;

	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	private List<CBHistorialSCECModel> listaTipificacion = null;
	CBHistorialSCECDAO objChdao = new CBHistorialSCECDAO();
	private String usuario;
	private String fecha = "";
	private int cbbancoagenciaconfrontaid = 0;
	private BigDecimal monto;
	private BigDecimal DiferenciaTotal;
	private BigDecimal montoestadocuenta;
	private BigDecimal montosistemacomercial;
	private BigDecimal acumulamonto = new BigDecimal(0.00);
	BigDecimal montoseleccionado;

	private BigDecimal calculaDiferencia;

	// Componentes ZK
	private Combobox cmbxCusas;
	private Textbox dmbxMontoTotal;
	private Textbox dmbxMontoAcumulado;
	private Textbox dmbxMontoSC;
	private Textbox dmbxMontoEC;
	private Textbox dmbxMonto;
	private Textbox dmbxDiferenciaTotal;
	private Textbox tbxObservacion;
	private Listbox lbxHistorialscec;
	private Button btnActualizar;
	private Button btnAgregar;
	Button btnLimpiar;
	Boolean filtros = false;

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);
		
		cbbancoagenciaconfrontaid = (Integer) misession.getAttribute("cbbancoagenciaconfrontaid");
		monto = (BigDecimal) misession.getAttribute("monto");
		fecha = (String) misession.getAttribute("fecha");
		montoestadocuenta = (BigDecimal) misession.getAttribute("montoestadocuenta");
		montosistemacomercial = (BigDecimal) misession.getAttribute("montosistemacomercial");

		
			DiferenciaTotal = montoestadocuenta.subtract(montosistemacomercial);
		

		dmbxDiferenciaTotal.setValue(DiferenciaTotal.toString());
		usuario = obtenerUsuario().getUsuario();
		dmbxMontoTotal.setValue(monto.toString());
		dmbxMontoSC.setValue(montosistemacomercial.toString());
		dmbxMontoEC.setValue(montoestadocuenta.toString());
		dmbxMonto.setValue("0.00");
		filtros = (Boolean) misession.getAttribute("filtrosprincipal");
		System.out.println("parametro en pantalla modal por sesion monto: " + monto);
		System.out.println("parametro en pantalla modal por sesion montosistemacomercial: " + montosistemacomercial);
		System.out.println("parametro en pantalla modal por sesion montoestadocuenta: " + montoestadocuenta);
		System.out.println(
				"parametro en pantalla modal por sesion cbbancoagenciaconfrontaid: " + cbbancoagenciaconfrontaid);

		llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
		cargarComboAcciones();
		btnActualizar.setDisabled(true);
		btnAgregar.setDisabled(false);
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
		listaTipificacion = objChdao.obtenerCBHistorialTipificacion(cbbancoagenciaconfrontaid, fecha);
		System.out.println("total lista retornada en llenalistbox:" + listaTipificacion.size());
		if (listaTipificacion.size() > 0) {

			acumulamonto = new BigDecimal(0.00);
			calculaDiferencia = new BigDecimal(0.00);
			Iterator<CBHistorialSCECModel> it = listaTipificacion.iterator();
			CBHistorialSCECModel obj = null;
			Listcell cell = null;
			Listitem fila = null;
			while (it.hasNext()) {
				obj = it.next();

				acumulamonto = acumulamonto.add(obj.getMonto());
				fila = new Listitem();

				cell = new Listcell();
				cell.setLabel(obj.getCausas());
				cell.setParent(fila);

				cell = new Listcell();
				System.out.println("valor monto en list:" + obj.getMonto());
				cell.setLabel(convertirADecimal(obj.getMonto()));
				System.out.println("valor monto en lis2:" + convertirADecimal(obj.getMonto()));
				cell.setParent(fila);

				cell = new Listcell();
				cell.setLabel(obj.getObservacion());
				cell.setParent(fila);

				cell = new Listcell();
				cell.setLabel(obj.getCreadopor());
				cell.setParent(fila);

				cell = new Listcell();
				cell.setLabel(obj.getFechacreacion());
				cell.setParent(fila);

				cell = new Listcell();
				cell.setLabel(obj.getModificadopor());
				cell.setParent(fila);

				cell = new Listcell();
				cell.setLabel(obj.getFechamodificacion());
				cell.setParent(fila);

				cell = new Listcell();
				Button btnEliminar = new Button();
				btnEliminar.setImage("/img/eliminar_16.png");
				btnEliminar.setAttribute("cbhistorialscecid", obj.getCbhistorialscecid());
				btnEliminar.setAttribute("monto", obj.getMonto());
				btnEliminar.setAttribute("tipologia", obj.getCbcausasconciliacionid());
				btnEliminar.addEventListener("onClick", eventBtnEliminarHistorial);
				btnEliminar.setParent(cell);
				cell.setParent(fila);
				
				
				fila.setAttribute("idseleccionado", obj.getCbhistorialscecid());
				fila.addEventListener(Events.ON_CLICK, eventBtnModificar);

				//
				fila.setValue(obj);
				fila.setParent(lbxHistorialscec);
			}
			System.out.println("Monto total del historial: " + acumulamonto);
			 //misession.setAttribute("montoacumulado", acumulamonto);//linea  a probar
			dmbxMontoAcumulado.setValue(acumulamonto.toString());
		
			calculaDiferencia = DiferenciaTotal.subtract(acumulamonto);
			dmbxDiferenciaTotal.setValue(calculaDiferencia.toString());
			System.out.println("diferencia en if " + calculaDiferencia);
		} else {

			dmbxMontoAcumulado.setValue(BigDecimal.valueOf(0).toString());

			dmbxDiferenciaTotal.setValue(DiferenciaTotal.toString());

		}
	}

	// se crea el evento modificar

	EventListener<Event> eventBtnModificar = new EventListener<Event>() {

		public void onEvent(Event arg0) throws Exception {

			btnActualizar.setDisabled(false);
			btnAgregar.setDisabled(true);
			// Obtenemos el objeto seleccionado
			CBHistorialSCECModel objModel = lbxHistorialscec.getSelectedItem().getValue();

			dmbxMonto.setValue(objModel.getMonto().toString());

			montoseleccionado = objModel.getMonto();
			tbxObservacion.setValue(objModel.getObservacion());
			int idcombo = 0;
			for (Comboitem item : cmbxCusas.getItems()) {
				idcombo = item.getValue();
				if (idcombo == objModel.getCbcausasconciliacionid()) {
					cmbxCusas.setSelectedItem(item);
				}
			}
		}
	};

	public void cargarComboAcciones() {
		List<CBHistorialSCECModel> lst = objChdao.obtieneCausasConciliacion();

		Iterator<CBHistorialSCECModel> iLst = lst.iterator();
		CBHistorialSCECModel obj = null;
		Comboitem item = null;
		while (iLst.hasNext()) {
			obj = iLst.next();

			item = new Comboitem();
			item.setLabel(obj.getCausas());
			item.setValue(obj.getCbcausasconciliacionid());
			item.setParent(cmbxCusas);

		}
	}

	/**
	 * Evento click para el boton agregar nuevo registro de tipificacion\
	 * 
	 */

	@SuppressWarnings("unused")
	public void onClick$btnAgregar() {

		BigDecimal validaDiferencia = DiferenciaTotal;

		System.out.println("valor DiferenciaTotal:" + DiferenciaTotal);

		BigDecimal validamonto = acumulamonto;
		System.out.println("valida monto: " + validamonto);

		System.out.println("valida dmbxMonto: " + dmbxMonto.getValue());
		if (dmbxMonto.getValue() != null) {
			validamonto = validamonto.add(new BigDecimal(dmbxMonto.getValue()));
			System.out.println("Monto al guardar para validar validamonto: " + validamonto);
			System.out.println("Monto al guardar para validar monto: " + monto.compareTo(BigDecimal.ZERO));
			System.out.println("Monto al guardar para validar dmbxMonto: " + dmbxMonto.getValue());
		/*	if (dmbxMonto.getValue().compareTo(dmbxDiferenciaTotal.getValue()) > 0) {
				Messagebox.show("No es posible agregar ese monto ya que sobrepasa el valor de "
						+ dmbxDiferenciaTotal.getValue(), "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else {
				*/
				if (cmbxCusas.getSelectedItem() == null && cmbxCusas.getText().equals("")) {
					Messagebox.show("Debe seleccionar una causa de conciliacion!", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else if (tbxObservacion.getText() == null && tbxObservacion.getText().equals("")) {
					Messagebox.show("Debe ingresar datos en el campo Observacion!", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				//} else if (dmbxMonto.getValue().compareTo(BigDecimal.ZERO) == 0) {
				} else if (new BigDecimal(dmbxMonto.getValue()).compareTo(BigDecimal.ZERO) == 0) {
					Messagebox.show("Se debe ingresar un monto mayor a cero", "ATENCION", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} else {
					try {
						CBHistorialSCECModel objModel = new CBHistorialSCECModel();
						objModel.setCbbancoagenciaconfrontaid(cbbancoagenciaconfrontaid);
						objModel.setFecha(fecha);
						objModel.setObservacion(tbxObservacion.getText());
						System.out.println("cmpo monto listo para agregar: " + (dmbxMonto.getText().trim()));
						objModel.setMonto(new BigDecimal(dmbxMonto.getValue()));
						objModel.setCbcausasconciliacionid((Integer) cmbxCusas.getSelectedItem().getValue());
						objModel.setCreadopor(usuario);
						objChdao.ingresaTipificacion(objModel);
						Messagebox.show("Se creo el registro con exito", Constantes.ATENCION, Messagebox.OK,
								Messagebox.INFORMATION);
						System.out.println("registro guardado: " + objModel.getMonto());
						llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
						limpiarCampos();
						btnActualizar.setDisabled(true);
						btnAgregar.setDisabled(false);
						refrescarModulo();
					} catch (Exception e) {
						Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
					}
				}
			//}
		} else {
			System.out.println("EL monto es requerido");
		}

	}

	public void onClick$btnActualizar() {
		BigDecimal validamonto = acumulamonto;

		BigDecimal validaDiferencia = DiferenciaTotal;
		System.out.println("valor DiferenciaTotal:" + DiferenciaTotal);

		validamonto = validamonto.add(new BigDecimal(dmbxMonto.getValue()));

		validamonto = validamonto.subtract(montoseleccionado);
		System.out.println("Monto al guardar para validar: " + validamonto);
		if (lbxHistorialscec.getSelectedItem() == null) {
			Messagebox.show("Debe seleccionar el registro que desea actualizar", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		//} else if (dmbxMonto.getValue().compareTo(validaDiferencia) > 0) {
		} else if (new BigDecimal(dmbxMonto.getValue()).compareTo(validaDiferencia) > 0) {
			Messagebox.show("No es posible agregar ese monto ya que sobrepasa el valor de " + validaDiferencia,
					"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (validamonto.compareTo(validaDiferencia) > 0) {
			Messagebox.show("No es posible agregar ese monto ya que sobrepasa el valor de " + validamonto, "ATENCION",
					Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			if (cmbxCusas.getSelectedItem() == null && cmbxCusas.getText().equals("")) {
				Messagebox.show("Debe seleccionar una causa de conciliacion!", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else if (tbxObservacion.getText() == null && tbxObservacion.getText().equals("")) {
				Messagebox.show("Debe ingresar datos en el campo Observacion!", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			//} else if (dmbxMonto.getValue().doubleValue() == 0) {
			} else if (new BigDecimal(dmbxMonto.getValue()).compareTo(BigDecimal.ZERO) == 0) {
				Messagebox.show("Se debe ingresar un monto mayor a cero", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} else {
				try {
					CBHistorialSCECModel objModel = new CBHistorialSCECModel();
					CBHistorialSCECModel objListb = lbxHistorialscec.getSelectedItem().getValue();
					objModel.setCbhistorialscecid(objListb.getCbhistorialscecid());
					objModel.setObservacion(tbxObservacion.getText());
					objModel.setMonto(new BigDecimal(dmbxMonto.getValue()));
					objModel.setCbcausasconciliacionid((Integer) cmbxCusas.getSelectedItem().getValue());
					objModel.setModificadopor(usuario);
					objChdao.actualizaTipificacion(objModel);
					llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
					Messagebox.show("Se actualizo el registro con exito", Constantes.ATENCION, Messagebox.OK,
							Messagebox.INFORMATION);
					limpiarCampos();
					refrescarModulo();
					btnActualizar.setDisabled(true);
					btnAgregar.setDisabled(false);
				} catch (Exception e) {
					Logger.getLogger(ConciliacionDetalleController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}

	public void onClick$btnLimpiar() {
		limpiarCampos();
		btnActualizar.setDisabled(true);
		btnAgregar.setDisabled(false);
	}

	/*
	 * public void onSelect$lbxHistorialscec() { btnActualizar.setDisabled(false);
	 * btnAgregar.setDisabled(true); //Obtenemos el objeto seleccionado
	 * CBHistorialSCECModel objModel =
	 * lbxHistorialscec.getSelectedItem().getValue();
	 * 
	 * dmbxMonto.setValue(objModel.getMonto());
	 * tbxObservacion.setValue(objModel.getObservacion()); int idcombo = 0;
	 * for(Comboitem item : cmbxCusas.getItems()) { idcombo = item.getValue();
	 * if(idcombo == objModel.getCbcausasconciliacionid()) {
	 * cmbxCusas.setSelectedItem(item); } } }
	 */
	public void limpiarCampos() {
		dmbxMonto.setValue("0.00");
		cmbxCusas.setSelectedItem(null);
		cmbxCusas.setText("");
		tbxObservacion.setText("");
		btnAgregar.setDisabled(false);
		btnActualizar.setDisabled(true);
	}

	EventListener<Event> eventBtnEliminarHistorial = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event arg0) throws Exception {

			final int id = (Integer) arg0.getTarget().getAttribute("cbhistorialscecid");
			final int tipologia = (Integer) arg0.getTarget().getAttribute("tipologia");
			final BigDecimal monto = (BigDecimal) arg0.getTarget().getAttribute("monto");
			Messagebox.show("¿Desea eliminar el registro seleccionado?", "CONFIRMACION", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {

						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {

								if (objChdao.eliminaHistorial(id)) {
									llenaListboxTipificacion(cbbancoagenciaconfrontaid, fecha);
									System.out.println("Elimina historial id: " + id);

									CBBitacoraLogDAO bitacoraDAO = new CBBitacoraLogDAO();

									CBBitacoraLogModel objBitaModel = new CBBitacoraLogModel();
									objBitaModel.setModulo("CONCILIACION MANUAL - TIPIFICACION CONC. BANCOS");
									objBitaModel.setTipoCarga("");
									objBitaModel.setNombreArchivo("");
									objBitaModel.setAccion("Se elimino registro de conciliacion manual con ID = " + id
											+ ", con tipologia: " + tipologia + "  y Monto: " + monto);
									objBitaModel.setUsuario(usuario);
									if (bitacoraDAO.insertBitacoraLog(objBitaModel)) {
										Logger.getLogger(CBConsultaCargasController.class.getName()).log(Level.INFO,
												"Inserta accion en log para tipificacion en conciliacion de bancos");
									}

									Messagebox.show("Se elimino el registro con exito", Constantes.ATENCION,
											Messagebox.OK, Messagebox.INFORMATION);
									limpiarCampos();
									refrescarModulo();
								}
							}
						}
					});
			limpiarCampos();
		}
	};

	/**
	 * 
	 * 
	 * */
	public void limpiarListbox(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);
			}
		}
	}

	public void refrescarModulo() {
		CBConciliacionBancoController instanciaPrincipal = new CBConciliacionBancoController();
		instanciaPrincipal = (CBConciliacionBancoController) session.getAttribute("interfaceTarjeta");

		if (filtros) {
			instanciaPrincipal.onClick$btnBuscar();
		}
		// onClick$closeBtn();

	}
	
	 public void onClick$closeBtn() {
	
	 wdHistorialScec.detach(); 
	 }
	 
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
