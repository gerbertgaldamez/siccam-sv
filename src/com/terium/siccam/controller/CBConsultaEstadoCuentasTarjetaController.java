package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
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

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConsultaEstadoCuentasDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.implement.CBNotifyChangeController;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConsultaEstadoCuentasModel;
import com.terium.siccam.model.CBEstadoCuentasModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.Constantes;

/*
 * @author CarlosGodinez - Qitcorp - 21/03/2017
 * */
public class CBConsultaEstadoCuentasTarjetaController extends ControladorBase implements CBNotifyChangeController{

	private static final long serialVersionUID = 9176164927878418930L;
	
	Combobox cmbTipo;
	Datebox dtbDesde;
	Datebox dtbHasta;
	Combobox cmbEntidad;
	Textbox txtAfiliacion;
	Listbox lstConsulta;
	
	Button btnExcel;
	Button btnAsignarTodos;
	Button btnDesasociarTodos;

	CBEstadoCuentasModel paramsLlenaListbox = new CBEstadoCuentasModel();
	private List<CBParametrosGeneralesModel> listParamTipo = new ArrayList<CBParametrosGeneralesModel>();
	private List<CBCatalogoAgenciaModel> listaAgencias = new ArrayList<CBCatalogoAgenciaModel>();
	private List<Integer> listaID = new ArrayList<Integer>();

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		this.btnExcel.setDisabled(true);
		this.btnAsignarTodos.setDisabled(true);
		this.btnDesasociarTodos.setDisabled(true);
		llenaComboEntidad();
		
		llenaComboTipo();	
	}
	
	public void llenaComboEntidad() throws SQLException, NamingException{
		CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
		this.listaAgencias = objeDAO.obtenerAgenciasEstCuentTarjCmb();
		for(CBCatalogoAgenciaModel d : this.listaAgencias)
	    {
			Comboitem item = new Comboitem();
		    item.setParent(this.cmbEntidad);
		    item.setValue(d.getcBCatalogoAgenciaId());
		    item.setLabel(d.getNombre());
	    }
	}
	
	static Window asociacionManual;
	
	EventListener<Event> evtDoModalAsociacion = new EventListener<Event>(){
		  public void onEvent(Event event) throws Exception {
			  session.setAttribute("asociacionMasiva", false);
			  session.setAttribute("paramsListboxTarjeta", paramsLlenaListbox);
			  session.setAttribute("idEstadoCuentaTarjeta", event.getTarget().getAttribute("idRegistroTarjeta"));
			  session.setAttribute("paramsLlenaListboxTarjeta", event.getTarget().getAttribute("objParamsListboxTarjeta"));
			  session.setAttribute("objAsociacionManual", event.getTarget().getAttribute("objSeleccionadoTarjeta"));
			  session.setAttribute("interfaceTarjeta", CBConsultaEstadoCuentasTarjetaController.this);
			  asociacionManual = (Window) Executions.createComponents("/cbAsociacionManual.zul", null,null);
			  asociacionManual.doModal();
		  }
	};
	

	
	public void onClick$btnConsulta() throws SQLException, NamingException {
		limpiarListbox(lstConsulta);
		CBConsultaEstadoCuentasDAO objDao = new CBConsultaEstadoCuentasDAO();	
		Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
			.log(Level.INFO, "Consulta estados tarjeta");
		String tipo = "";
		String fechaIni = "";
		String fechaFin = ""; 
		String agencia = "";
		String afiliacion = "";
		
		if (cmbTipo.getSelectedItem() != null) {
			if (Constantes.TODOS.equals(cmbTipo.getSelectedItem().getLabel())) {
				tipo = "";
			} else {
				tipo = cmbTipo.getSelectedItem().getValue();
			}
		}
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
			fechaIni = dtbDesde.getText().trim();
			fechaFin = dtbHasta.getText().trim();
		}
		if(cmbEntidad.getSelectedItem() != null){
			agencia = cmbEntidad.getSelectedItem().getValue();
		}
		if(txtAfiliacion.getText().trim() != null){
			afiliacion = txtAfiliacion.getText().trim();
		}
		paramsLlenaListbox.setTipo(tipo);
		paramsLlenaListbox.setFechaInicio(fechaIni);
		paramsLlenaListbox.setFechaFin(fechaFin);
		paramsLlenaListbox.setNombreAgencia(agencia);
		paramsLlenaListbox.setAfiliacion(afiliacion);
		llenaListbox(objDao.consultaEstadoCuentasTarjeta(tipo, fechaIni, fechaFin, agencia, afiliacion));
		if(this.lstConsulta.getItemCount() != 0){
			this.btnExcel.setDisabled(false);
			this.btnAsignarTodos.setDisabled(false);
			this.btnDesasociarTodos.setDisabled(false);
		} else {
			this.btnExcel.setDisabled(true);
			this.btnAsignarTodos.setDisabled(true);
			this.btnDesasociarTodos.setDisabled(true);
		}
	}
	
	
	public void llenaListbox(List<CBConsultaEstadoCuentasModel> list){
		limpiarListbox(lstConsulta);
		if(listaID != null && !listaID.isEmpty()){
			listaID.clear();
		}
		if(list.size() > 0) {
			Iterator<CBConsultaEstadoCuentasModel> it = list.iterator();
			CBConsultaEstadoCuentasModel obj = null;
			Listitem item = null;
			Listcell cell = null;
			while(it.hasNext()) {
				obj = it.next();
				
				item = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(obj.getTipoTarjeta());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getFechaTransaccion());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getAfiliacion());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getTipo());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getReferencia());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getLiquido());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getComision());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getIvaComision());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getRetencion());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getConsumo());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getNombreEntidad());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getObservaciones());
				cell.setParent(item);
				
				if(listaID != null) {
					listaID.add(obj.getCbestadocuentaid());
				}
				item.setValue(obj);
				item.setParent(lstConsulta);
				item.setAttribute("idRegistroTarjeta", obj.getCbestadocuentaid());
				item.setAttribute("objParamsListboxTarjeta", paramsLlenaListbox);
				item.setAttribute("objSeleccionadoTarjeta", obj);
				item.setTooltip("popAsociacion");
				item.addEventListener(Events.ON_DOUBLE_CLICK, evtDoModalAsociacion);
			}
			list.clear();
		}else {
			Messagebox.show("¡No se encontraron resultados!",
					Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	public void onClick$btnAsignarTodos(){
		session.setAttribute("interfaceTarjeta", CBConsultaEstadoCuentasTarjetaController.this);
		session.setAttribute("paramsListboxTarjeta", paramsLlenaListbox);
		session.setAttribute("asociacionMasiva", true);
		session.setAttribute("listaIDEstadoCuentaTarjeta", listaID);
		asociacionManual = (Window) Executions.createComponents("/cbAsociacionManual.zul", null,null);
		asociacionManual.doModal();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnDesasociarTodos(){
		Messagebox.show("¿Desea desasociar las entidades que están asignadas a los registros consultados?",Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO,
				Messagebox.QUESTION, new EventListener() {
					public void onEvent(Event event) throws Exception {
						if (((Integer) event.getData()).intValue() == Messagebox.YES) {
							CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
							if(objeDAO.desasociarAsociacionManualMasiva(listaID)){
								session.setAttribute("paramsListboxTarjeta", paramsLlenaListbox);
								recargarConsulta("Todas");
								Messagebox.show("Registros desasociados con éxito.", Constantes.ATENCION, Messagebox.OK,Messagebox.INFORMATION);
							}
						}
					}
				});
	}
	
	/**
	 * @author Juankrlos
	 * */
	public String changeNull(String cadena){
			if(cadena == null){
				return " ";
			}else{
				return cadena;
			}
	}
	
	//Agregado por Carlos Godínez - QitCorp - 21/03/2017
	public void onClick$btnExcel() {
		Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO, 
				"Generando reporte de estados de cuenta de tarjetas...");
		BufferedWriter bw = null;
		try {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FORMATO_FECHA2);
			// Creamos el encabezado
			
			File archivo = new File(Constantes.REPORTE_ESTADO_CUENTA_TARJETA + sdf.format(fecha) + Constantes.CSV);
			
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(Constantes.ENCABEZADO_ESTADO_CUENTA_TARJETA);
			for (Listitem iterator : this.lstConsulta.getItems()) {
				CBConsultaEstadoCuentasModel registro = (CBConsultaEstadoCuentasModel) iterator.getValue();
				bw.write(changeNull(registro.getTipoTarjeta()).trim() + "|"
						+ changeNull(registro.getFechaTransaccion()).trim() + "|"
						+ changeNull(registro.getAfiliacion()).trim() + "|" + changeNull(registro.getTipo()).trim()
						+ "|" + changeNull(registro.getReferencia()).trim() + "|"
						+ changeNull(registro.getLiquido()).trim() + "|" + changeNull(registro.getComision()).trim()
						+ "|" + changeNull(registro.getIvaComision()).trim() + "|"
						+ changeNull(registro.getRetencion()).trim() + "|" + changeNull(registro.getConsumo()).trim()
						+ "|" + changeNull(registro.getNombreEntidad()).trim() + "\n");
			}
			
			Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO, 
					"Descarga exitosa del archivo generado...");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", Constantes.ATENCION,Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();
		} catch (IOException e) {
			Logger.getLogger(CBConsultaEstadoCuentasTarjetaController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					Logger.getLogger(CBConsultaEstadoCuentasTarjetaController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
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

	public void recargarConsulta(String asociacion) {
		Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
			.log(Level.INFO, "Entra a recargar consulta...");
		CBEstadoCuentasModel param = new CBEstadoCuentasModel();
		param = (CBEstadoCuentasModel)session.getAttribute("paramsListboxTarjeta");
		CBConsultaEstadoCuentasDAO objDao = new CBConsultaEstadoCuentasDAO();
		try {
			this.cmbEntidad.setText(asociacion);
			if(asociacion.equals("Todas")){
				asociacion = "";
			}
			llenaListbox(objDao.consultaEstadoCuentasTarjeta(param.getTipo(), param.getFechaInicio(), param.getFechaFin(), asociacion, param.getAfiliacion()));
		} catch (Exception e) {
			Logger.getLogger(CBConsultaEstadoCuentasTarjetaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	
	/**
	 * Combo tipoo
	 */
	public void llenaComboTipo() {
		CBParametrosGeneralesDAO objDao = new CBParametrosGeneralesDAO();
		listParamTipo = objDao.obtenerTipoTarjeta();
		if (listParamTipo != null && listParamTipo.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = listParamTipo.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto1());
				item.setParent(cmbTipo);
			}
		} else {
			Messagebox.show("Error al cargar la configuracion de cuentas", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}
}
