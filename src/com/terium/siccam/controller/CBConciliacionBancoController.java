package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.dao.CBConciliacionBancoDAO;
import com.terium.siccam.dao.CBMantenimientoTipologiasPolizaDAO;
import com.terium.siccam.model.CBConciliacionBancoModel;
import com.terium.siccam.model.CBMantenimientoPolizaModel;
import com.terium.siccam.utils.Constantes;

public class CBConciliacionBancoController extends MenuController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5206238021662364973L;

	// Componentes para manejo de vista MVC
	Combobox cmbBanco;
	Combobox cmbAgencia;
	Combobox cmbEntidad;
	Listbox lbxConsulta;
	Datebox dtbDesde;
	Datebox dtbHasta;
	Textbox txtCodigoColector; // CarlosGodinez -> 07/08/2018
	Button btnReporte;

	Button btnEjecutarComisiones;

	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		llenaComboEntidad();
		llenaComboAgencia();
		// llenaComboBanco();
	}

	public void llenaComboEntidad() {
		try {
			CBConciliacionBancoDAO objDao = new CBConciliacionBancoDAO();
			List<CBConciliacionBancoModel> list = objDao.generaConsultaEntidad();
			Iterator<CBConciliacionBancoModel> it = list.iterator();
			CBConciliacionBancoModel obj = null;
			while (it.hasNext()) {
				obj = it.next();

				Comboitem item = new Comboitem();
				item.setLabel(obj.getNombre());
				item.setValue(obj.getIdcombo());
				item.setParent(cmbEntidad);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void llenaComboAgencia() {
		try {
			CBConciliacionBancoDAO objDao = new CBConciliacionBancoDAO();
			List<CBConciliacionBancoModel> list = objDao.generaConsultaAgencia();
			Iterator<CBConciliacionBancoModel> it = list.iterator();
			CBConciliacionBancoModel obj = null;
			while (it.hasNext()) {
				obj = it.next();

				Comboitem item = new Comboitem();
				item.setLabel(obj.getNombre());
				item.setValue(obj.getIdcombo());
				item.setParent(cmbAgencia);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void llenaComboBanco() {
		try {
			CBConciliacionBancoDAO objDao = new CBConciliacionBancoDAO();
			List<CBConciliacionBancoModel> list = objDao.generaConsultaBanco();
			Iterator<CBConciliacionBancoModel> it = list.iterator();
			CBConciliacionBancoModel obj = null;
			while (it.hasNext()) {
				obj = it.next();

				Comboitem item = new Comboitem();
				item.setLabel(obj.getNombre());
				item.setValue(obj.getIdcombo());
				item.setParent(cmbBanco);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onClick$btnBuscar() {
		try {
			limpiarListbox(lbxConsulta);
			CBConciliacionBancoDAO objDao = new CBConciliacionBancoDAO();
			int agencia = 0;
			String fechaini = null;
			String fechafin = null;
			String codigoColector = "";
			DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");

			if (cmbAgencia.getSelectedItem() != null
					&& !"Todas".equals(cmbAgencia.getSelectedItem().getLabel().trim())) {
				agencia = cmbAgencia.getSelectedItem().getValue();
			}
			if (dtbDesde.getValue() == null) {
				Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			} else if (dtbHasta.getValue() == null) {
				Messagebox.show("Debe ingresar la fecha fin antes de consultar datos.", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			} else if (dtbDesde.getValue().after(dtbHasta.getValue())) {
				Messagebox.show("La fecha desde debe ser menor a la fecha hasta.", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			} else {
				fechaini = fechaFormato.format(dtbDesde.getValue());
				fechafin = fechaFormato.format(dtbHasta.getValue());
			}

			// Modifica CarlosGodinez -> 07/08/2018
			// Se agrega codigo de colector como filtro

			if (txtCodigoColector.getText() == null || "".equals(txtCodigoColector.getText().trim())) {
				codigoColector = "";
			} else {
				codigoColector = txtCodigoColector.getText().trim();
			}

			CBConciliacionBancoModel objModel = new CBConciliacionBancoModel();
			objModel.setCbcatalogoagenciaid(agencia);
			objModel.setFechaInicioFiltro(fechaini);
			objModel.setFechaFinFiltro(fechafin);
			objModel.setCodigoColector(codigoColector);
			llenaListbox(objDao.generaConsultaPrincipal(objModel));
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * Llenamos el listbox con el resultado de consultar la conciliacion bancos
	 * 
	 * cambio de query para el 09/03/2017 --> Juankrlos
	 */
	public void llenaListbox(List<CBConciliacionBancoModel> list) {
		limpiarListbox(lbxConsulta);
		if (list != null && list.size() > 0) {
			Iterator<CBConciliacionBancoModel> it = list.iterator();
			CBConciliacionBancoModel obj = null;
			Listcell cell = null;
			Listitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Listitem();

				/**
				 * Agrupacion general
				 */

				cell = new Listcell();
				cell.setLabel(obj.getFecha()); // Fecha
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(obj.getNombre()); // Nombre entidad
				cell.setParent(item);

				// Agrega CarlosGodinez -> 07/08/2018

				cell = new Listcell();
				cell.setLabel(obj.getCodigoColector()); // Codigo colector
				cell.setParent(item);

				// FIN CarlosGodinez -> 07/08/2018

				/**
				 * Confronta vs Estado de cuenta
				 */
				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getEstadopostpago()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getConfrontapostpago()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getDifpostpago()));
				if (obj.getDifpostpago().compareTo(BigDecimal.ZERO) > 0)
					cell.setStyle("color:red");
				cell.setParent(item);

				/**
				 * Sistema Comercial vs Estado de cuenta
				 */
				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getPagosdeldia()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getPagosotrosdias()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getPagosotrosmeses()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getReversasotrosdias()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getReversasotrosmeses()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getTotaldia()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getEstadocuenta()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getConciliadomanual()));
				cell.setParent(item);

				cell = new Listcell();
				// BigDecimal conciliado_manual= obj.getConciliadomanual();

					if (obj.getDiferenciatotal().compareTo(BigDecimal.ZERO) > 0) {
						cell.setLabel(convertirADecimal(obj.getDiferenciatotal().subtract(obj.getConciliadomanual()))
								);
						cell.setStyle("color:red");
					} else if (obj.getConciliadomanual().compareTo(BigDecimal.ZERO) < 0)  {
						cell.setLabel(convertirADecimal(obj.getDiferenciatotal().subtract(obj.getConciliadomanual()))
								);
						cell.setStyle("color:red");
					} else {
						cell.setLabel(convertirADecimal(obj.getDiferenciatotal().add(obj.getConciliadomanual()))
								);
						cell.setStyle("color:red");
					}

				
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getTotalgeneral()));
				cell.setParent(item);

				/**
				 * Comisiones y recaudaciones
				 */

				cell = new Listcell();
				cell.setLabel(convertirADecimalComision(obj.getPorcentajepostpago()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimalComision(obj.getComisionconfrontapostpago()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimalComision(obj.getComisionpostpago()));
				cell.setParent(item);

				cell = new Listcell();
				Button btnDelete = new Button();
				btnDelete.setImage("/img/globales/16x16/consulta.png");

				cell.setParent(item);
				btnDelete.setParent(cell);
				// btnDelete.setTooltip("popEliminar");

				btnDelete.setAttribute("objModelModal", obj);
				btnDelete.addEventListener(Events.ON_CLICK, eventBtnDetalleComisionModal);

				cell = new Listcell();
				cell.setLabel(convertirADecimalComision(obj.getDiferenciacomisionpospago()));
				if (obj.getDiferenciacomisionpospago().compareTo(BigDecimal.ZERO) > 0)
					cell.setStyle("color:red");
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimalComision(obj.getComisiontotal()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getRecafinalpost()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(obj.getTotalfinal()));
				cell.setParent(item);

				// Agrega Listener --> Juankrlos by 19/07/2018
				item.setValue(obj);
				item.setAttribute("cbbancoagenciaconfrontaid", obj.getCbbancoagenciaconfrontaid());
				item.setAttribute("fecha", obj.getFecha());
				item.setAttribute("monto", obj.getTotalfinal());
				item.setAttribute("montoestadocuenta", obj.getEstadocuenta());

				item.setAttribute("diferenciaTotal", obj.getDiferenciatotal());

				item.setAttribute("montosistemacomercial", obj.getTotaldia());
				item.addEventListener("onDoubleClick", eventTipificacion);
				item.setParent(lbxConsulta);

			}
			btnReporte.setDisabled(false);
		} else {
			btnReporte.setDisabled(true);
			Messagebox.show("No se encontraron registros para ese rango de fecha!", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
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

	// agre metodo Ovidio
	public String convertirADecimalComision(BigDecimal num) {
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

	EventListener<Event> eventBtnDetalleComisionModal = new EventListener<Event>() {
		public void onEvent(Event event) throws Exception {
			try {
				CBConciliacionBancoModel objModelModal = (CBConciliacionBancoModel) event.getTarget()
						.getAttribute("objModelModal");
				Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
						"\n**** Tipologia de poliza seleccionada ****\n");
				session.setAttribute("objModelModal", objModelModal);
				Executions.createComponents("/cbdetallecomsionesmodal.zul", null, null);

			} catch (Exception e) {
				Logger.getLogger(CBTipologiasPolizaController.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	};

	public void onClick$btnReporte() {

		Date fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FORMATO_FECHA2);

		File archivo = new File(Constantes.REPORTE_CONCILIACION_BANCOS + sdf.format(fecha) + Constantes.CSV);
		BufferedWriter bw = null;
		try {
			if (lbxConsulta != null) {
				bw = new BufferedWriter(new FileWriter(archivo));
				bw.write(Constantes.ENCABEZADO_CONCILIACION_BANCOS);
				for (Listitem iterator : this.lbxConsulta.getItems()) {
					CBConciliacionBancoModel obj = (CBConciliacionBancoModel) iterator.getValue();

					bw.write(obj.getFecha() + "|" + obj.getNombre() + "|" + obj.getCodigoColector() + "|"
							+ obj.getEstadopostpago() + "|" + obj.getConfrontapostpago() + "|" + obj.getDifpostpago()
							+ "|" + obj.getPagosdeldia() + "|" + obj.getPagosotrosdias() + "|"
							+ obj.getPagosotrosmeses() + "|" + obj.getReversasotrosdias() + "|"
							+ obj.getReversasotrosmeses() + "|" + obj.getTotaldia() + "|" + obj.getTotalgeneral() + "|"
							+ obj.getEstadocuenta() + "|" + obj.getDiferenciatotal() + "|" + obj.getPorcentajepostpago()
							+ "|" + obj.getComisionconfrontapostpago() + "|" + obj.getComisionpostpago() + "|"
							+ obj.getDiferenciacomisionpospago() + "|" + obj.getComisiontotal() + "|"
							+ obj.getRecafinalpost() + "|" + obj.getTotalfinal() + "\n");
				}

			}

			Logger.getLogger(CBCatalogoBancoController.class.getName()).log(Level.INFO, "Inicia descarga del archivo");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
					Messagebox.OK, Messagebox.INFORMATION);

			Clients.clearBusy();
		} catch (Exception e) {
			Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
				}
		}
	}

	/**
	 * @author Juankrlos by 18/07/2018
	 */

	EventListener<Event> eventTipificacion = new EventListener<Event>() {
		public void onEvent(Event arg0) throws Exception {
			int cbbancoagenciaconfrontaid = (Integer) arg0.getTarget().getAttribute("cbbancoagenciaconfrontaid");
			;
			String fecha = (String) arg0.getTarget().getAttribute("fecha");
			BigDecimal monto = (BigDecimal) arg0.getTarget().getAttribute("monto");
			BigDecimal montoestadocuenta = (BigDecimal) arg0.getTarget().getAttribute("montoestadocuenta");
			BigDecimal montosistemacomercial = (BigDecimal) arg0.getTarget().getAttribute("montosistemacomercial");

			BigDecimal diferenciaTotal = (BigDecimal) arg0.getTarget().getAttribute("diferenciaTotal");

			misession.setAttribute("fecha", fecha);
			misession.setAttribute("monto", monto);
			misession.setAttribute("montoestadocuenta", montoestadocuenta);
			misession.setAttribute("montosistemacomercial", montosistemacomercial);

			misession.setAttribute("diferenciaTotal", diferenciaTotal);

			misession.setAttribute("cbbancoagenciaconfrontaid", cbbancoagenciaconfrontaid);
			session.setAttribute("interfaceTarjeta", CBConciliacionBancoController.this);
			session.setAttribute("filtrosprincipal", true);

			Executions.createComponents("/cbhistorialscecmodal.zul", null, null);
			/*
			 * Window window = (Window)Executions.createComponents(
			 * "cbhistorialscecmodal.zul", null, null); window.doModal();
			 */

		}
	};

	public void onClick$btnEjecutarComisiones() {

		session.setAttribute("interfaceTarjeta", CBConciliacionBancoController.this);

		if (dtbDesde.getValue() != null && dtbHasta.getValue() != null) {
			session.setAttribute("filtrosprincipal", true);
		} else {
			session.setAttribute("filtrosprincipal", false);
		}
		Executions.createComponents("/cbejecutacomisionesmodal.zul", null, null);
	}

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

}
