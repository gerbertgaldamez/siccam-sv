package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
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
import com.terium.siccam.implement.CBNotifyChangeEstadoCuentaSociedad;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConsultaEstadoCuentasModel;
import com.terium.siccam.model.CBEstadoCuentasModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;
import com.terium.siccam.utils.Constantes;

public class CBConsultaEstadoCuentasController extends ControladorBase implements CBNotifyChangeEstadoCuentaSociedad{
	
	private static Logger log = Logger.getLogger(CBConsultaEstadoCuentasController.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 9176164927878418930L;
	private  HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	Combobox cmbxBanco;
	Combobox cmbxAgencia;
	Textbox tbxTexto;
	Textbox tbxObservaciones;
	Listbox lbxConsulta;
	Datebox dtbDesde;
	Datebox dtbHasta;
	
	//Propiedades agregadas por CarlosGodinez - Qitcorp - 07/03/2017
	Combobox cmbTipologia;
	Combobox cmbAgenciaTipologia;
	Datebox dtbFechaIngresos;
	Button btnExcel;
	
	Button btnAsignarTodos;
	Button btnDesasociarTodos;
	Button btnAsignarDepositosTodos;
	
	Textbox tbxAsignacion;
	Textbox tbxTextoCabDocumento;
	
	//Listas para llenar combobox del módulo de tipologías
	private List<CBTipologiasPolizaModel> listaTipologia = new ArrayList<CBTipologiasPolizaModel>();
	private List<CBCatalogoAgenciaModel> listaAgencias = new ArrayList<CBCatalogoAgenciaModel>();
	
	private List<Integer> listaID = new ArrayList<Integer>();
	
	//Agregado por CarlosGodinez - Qitcorp - 02/03/2017
	CBEstadoCuentasModel paramsLlenaListbox = new CBEstadoCuentasModel();
	
	private String usuario; //CarlosGodinez -> 10/10/2017
	
	//juankrlos --> 29/11/2017
	@SuppressWarnings("unused")
	private int rangodias = 0;

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		usuario = obtenerUsuario().getUsuario();
		llenaComboBanco();
		//llenaComboAgencia(); Commented by CarlosGodinez -> 25/09/2018
		llenaComboTipologia();
		llenaComboAgenciaTipologia();
		this.btnExcel.setDisabled(true);
		this.btnAsignarTodos.setDisabled(true);
		this.btnDesasociarTodos.setDisabled(true);
		obtenerAgrupacionPredefinida();
	}
	
	/**
	 * Combo Banco
	 * */
	public void llenaComboBanco(){
		try {
			CBConsultaEstadoCuentasDAO objDao = new CBConsultaEstadoCuentasDAO();	
			Iterator<CBConsultaEstadoCuentasModel> it = objDao.obtenerBanco().iterator();
			CBConsultaEstadoCuentasModel param = null;
			Comboitem item = null;
			while(it.hasNext()){
				param = it.next();
				item = new Comboitem();
				item.setValue(param.getIdbanco());
				item.setLabel(param.getBanco());
				item.setParent(cmbxBanco);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
			log.error("llenaComboBanco() - Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
	  }
	}
	
	/**
	 * Obtener opcion de agrupacion seleccionada por defecto
	 * */
	public void obtenerAgrupacionPredefinida() {
		CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
		int opcionPredefinida = objeDAO.obtenerOpcionPorDefecto();
		for (Comboitem item : cmbxBanco.getItems()) {
			if (Integer.parseInt(item.getValue().toString()) == opcionPredefinida) {
				cmbxBanco.setSelectedItem(item);
			}
		}
		seleccionComboAgrupacion();
	}
	
	/**
	 * Metodo que se invoca al seleccionar una agrupacion
	 */
	public void onSelect$cmbxBanco()  {
		seleccionComboAgrupacion();
	}
	
	public void seleccionComboAgrupacion()  {
		int agrupacionSeleccionada = Integer.parseInt(cmbxBanco.getSelectedItem().getValue().toString());
		log.debug(
				"seleccionComboAgrupacion() - " + "Agrupacion seleccionada= " + agrupacionSeleccionada);
		//Logger.getLogger(CBRecaudacionUsuarioController.class.getName()).log(Level.INFO, 
				//"Agrupacion seleccionada= " + agrupacionSeleccionada);
		cleanCombo(cmbxAgencia);
		llenaComboAgencia(agrupacionSeleccionada);
	}
	
	/**
	 * Combo Agencias
	 * Modified by CarlosGodinez -> 25/09/2018
	 * Se agrega parametro de agrupacion seleccionada
	 * */
	public void llenaComboAgencia(int agrupacionSeleccionada){
		try {
			CBConsultaEstadoCuentasDAO objDao = new CBConsultaEstadoCuentasDAO();	
			Iterator<CBConsultaEstadoCuentasModel> it = objDao.obtenerAgencia(agrupacionSeleccionada).iterator();
			CBConsultaEstadoCuentasModel param = null;
			Comboitem item = null;
			/**
			 * CarlosGodinez -> 25/08/2018
			 * */
			item = new Comboitem();
			item.setValue("0");
			item.setLabel(Constantes.TODAS);
			item.setParent(cmbxAgencia);
			/**
			 * FIN CarlosGodinez -> 25/08/2018
			 * */
			while(it.hasNext()){
				param = it.next();
				
				item = new Comboitem();
				item.setValue(param.getIdagencia());
				item.setLabel(param.getAgencia());
				item.setParent(cmbxAgencia);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
			log.error("llenaComboAgencia() - Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
	  }
	}
	
	/*
	 * Agregado por Carlos Godínez - Qitcorp - 01/03/2017
	 */
	static Window asignarTipologia;
	
	EventListener<Event> evtDoModalTipologia = new EventListener<Event>(){
		  public void onEvent(Event event) {
			  try {
				  log.debug(
							"evtDoModalTipologia() - " + "Bandera asignar tipología, onDobleclick  = en modal " );
				 // Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO, 
						 // "Bandera asignar tipología, onDobleclick  = en modal " );
				  misession.setAttribute("desasociarTipologiaMasiva", false);
				  misession.setAttribute("tipologiaMasiva", false);
				  misession.setAttribute("idEstadoCuenta", event.getTarget().getAttribute("idRegistro"));
				  misession.setAttribute("paramsListbox", paramsLlenaListbox);
				  misession.setAttribute("objEditarTipologia", event.getTarget().getAttribute("objSeleccionado"));
				  misession.setAttribute("interface", CBConsultaEstadoCuentasController.this);
				  asignarTipologia = (Window) Executions.createComponents("/cbAsignarTipologiaEstadoCuenta.zul", null,null);
				  asignarTipologia.doModal();
			  } catch (Exception e) {
				  log.error("evtDoModalTipologia() - Error ", e);
					//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			  }
		  }
	};
	
	public void llenaComboTipologia() throws SQLException, NamingException{
		try {
			CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
			this.listaTipologia = objeDAO.obtenerTipologias();
			if(listaTipologia.size() > 0) {
				for(CBTipologiasPolizaModel d : this.listaTipologia)
				{
					Comboitem item = new Comboitem();
				    item.setParent(this.cmbTipologia);
				    item.setValue(d.getCbtipologiaspolizaid());
				    item.setAttribute("pideEntidad", d.getPideEntidad());
				    item.setLabel(d.getNombre());
				}
			}	
		} catch (Exception e) {
			 log.error("llenaComboTipologia() - Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
	  }
	}
	
	public void llenaComboAgenciaTipologia() throws SQLException, NamingException{
		try {
			CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
			this.listaAgencias = objeDAO.obtenerAgenciasCmb();
			for(CBCatalogoAgenciaModel d : this.listaAgencias)
			{
				Comboitem item = new Comboitem();
			    item.setParent(this.cmbAgenciaTipologia);
			    item.setValue(d.getcBCatalogoAgenciaId());
			    item.setLabel(d.getNombre());
			}
		} catch (Exception e) {
			 log.error("llenaComboTipologia() - Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
	  }
	}
	/*
	 * 	  
	 */
	
	public void onClick$btnConsuta() {
		try { 
			limpiarListbox(lbxConsulta);
			CBConsultaEstadoCuentasDAO objDao = new CBConsultaEstadoCuentasDAO();
			log.debug(
					"onClick$btnConsuta() - " + "Consulta estados");
		//	Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO, "Consulta estados");
			//CBConsultaEstadoCuentasModel objAgencia = null;
			int banco = 0;
			int agencia = 0;
			String fechaini = null;
			String fechafin = null;
			String tipologia = "";
			String agenciaTipologia = "";
			String fechaIngresos = "";
			DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
			if(cmbxBanco.getSelectedItem() != null) {
				if(cmbxBanco.getSelectedItem().getLabel().equals(Constantes.TODAS)){
					banco = 0;
				}else{
					banco = cmbxBanco.getSelectedItem().getValue();
				}
			}
			if(cmbxAgencia.getSelectedItem() != null) {
				if(cmbxAgencia.getSelectedItem().getLabel().equals(Constantes.TODAS)){
					agencia = 0;
				}else{
					agencia = cmbxAgencia.getSelectedItem().getValue();
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
				fechaini = fechaFormato.format(dtbDesde.getValue());
				fechafin = fechaFormato.format(dtbHasta.getValue());
			}
			
			//Agregado por Carlos Godínez - Qitcorp - 07/03/2017
			
			if(cmbTipologia.getSelectedItem() != null 
					&& !Constantes.TODAS.equals(cmbTipologia.getSelectedItem().getLabel().trim())){
				tipologia = cmbTipologia.getSelectedItem().getValue().toString();			
			}
			if(cmbAgenciaTipologia.getSelectedItem() != null
					&& !Constantes.TODAS.equals(cmbAgenciaTipologia.getSelectedItem().getLabel().trim())){
				agenciaTipologia = cmbAgenciaTipologia.getSelectedItem().getValue().toString();	
				
			}
			if(dtbFechaIngresos.getValue() != null){
				fechaIngresos = fechaFormato.format(dtbFechaIngresos.getValue());
			}
			
			/// ovidio
			/**
			 * 
			if(misession.getAttribute(Constantes.CONEXION).equals(Tools.SESSION_GT)) {
				if (cmbxAgencia.getSelectedItem() != null 
						&& !Constantes.TODAS.equals(cmbxAgencia.getSelectedItem().getLabel().trim())) {	
						objAgencia = cmbxAgencia.getSelectedItem().getValue();
						paramsLlenaListbox.setCuenta(objAgencia.getCuenta());
						Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO, 
								"Cuenta contable enviada: ",objAgencia.getCuenta());
					
				}
		
			}
			*/
					
			paramsLlenaListbox.setCbcatalogobancoid(banco);
			paramsLlenaListbox.setCbcatalogoagenciaid(agencia);
			paramsLlenaListbox.setTexto(tbxTexto.getText());
			paramsLlenaListbox.setFechaInicio(fechaini);
			paramsLlenaListbox.setFechaFin(fechafin);
			paramsLlenaListbox.setObservaciones(tbxObservaciones.getText().trim());
			paramsLlenaListbox.setTipologia(tipologia);
			paramsLlenaListbox.setAgenciaTipologia(agenciaTipologia);
			paramsLlenaListbox.setFechaIngresos(fechaIngresos);
			
			llenaListbox(objDao.consultaEstadosCuenta(paramsLlenaListbox) , 1);
			
			if(this.lbxConsulta.getItemCount() != 0){
				this.btnExcel.setDisabled(false);
				this.btnAsignarTodos.setDisabled(false);
				this.btnDesasociarTodos.setDisabled(false);
			} else {
				this.btnExcel.setDisabled(true);
				this.btnAsignarTodos.setDisabled(true);
				this.btnDesasociarTodos.setDisabled(true);
			}
		} catch (Exception e) {
			log.error("onClick$btnConsuta() - Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void llenaListbox(List<CBConsultaEstadoCuentasModel> list, int banderaMensaje){
		limpiarListbox(lbxConsulta);
		if(this.listaID != null && !this.listaID.isEmpty()){
			this.listaID.clear();
		}
		if(list.isEmpty()) {
			if(banderaMensaje == 1) { 
				Messagebox.show("¡No se encontraron resultados!",
					Constantes.ADVERTENCIA, Messagebox.OK, Messagebox.EXCLAMATION);
			}		
		} else {
			Iterator<CBConsultaEstadoCuentasModel> it = list.iterator();
			CBConsultaEstadoCuentasModel obj = null;
			Listitem item = null;
			Listcell cell = null;
			while(it.hasNext()) {
				obj = it.next();
				
				item = new Listitem();
				
				cell = new Listcell();
				cell.setLabel(obj.getBanco());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getAgencia());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getCuenta());
				cell.setParent(item);
				
				//Agrega Carlos Godinez - 07/07/2017
				cell = new Listcell();
				cell.setLabel(obj.getAsignacion());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getFecha());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getTexto());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getDebe());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getHaber());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getIdentificador());
				cell.setParent(item);
				
				//Agregado por Carlos Godínez - Qitcorp - 01/03/2017
				
				cell = new Listcell();
				cell.setLabel(obj.getTipologia());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getAgenciaTipologia());
				cell.setParent(item);
				
				//Agrega Carlos Godinez - 07/07/2017
				cell = new Listcell();
				cell.setLabel(obj.getTextoCabDoc());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getObservaciones());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getFechaIngresos());
				cell.setParent(item);
				
				//add by Omar Gomez -QIT- 07/08/2017
				cell = new Listcell();
				cell.setLabel(obj.getNumDocumento());
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(obj.getModificadoPor());
				cell.setParent(item);
				
				
				cell = new Listcell();
				cell.setLabel(obj.getFechaModificacion());
				cell.setParent(item);
				
				
				//Agregado por Carlos Godínez - Qitcorp - 01/03/2017 (para Asignar tipologías de forma masiva) 
				if(this.listaID != null){
					this.listaID.add(obj.getCbestadocuentasociedad());
				}
	
				item.setValue(obj);
				item.setParent(lbxConsulta);
				item.setAttribute("idRegistro", obj.getCbestadocuentasociedad());
				item.setAttribute("objSeleccionado", obj);
				item.setTooltip("popTipologia");
				item.addEventListener(Events.ON_DOUBLE_CLICK, evtDoModalTipologia);
			}
			list.clear();
		}
	}


	public void onClick$btnAsignarTodos() {
		try {
			misession.setAttribute("desasociarTipologiaMasiva", false);
			session.setAttribute("interface", CBConsultaEstadoCuentasController.this);
			session.setAttribute("paramsListbox", paramsLlenaListbox);
			session.setAttribute("tipologiaMasiva", true);
			session.setAttribute("listaIDEstadoCuenta", this.listaID);
			asignarTipologia = (Window) Executions.createComponents("/cbAsignarTipologiaEstadoCuenta.zul", null, null);
			asignarTipologia.doModal();
		 } catch (Exception e) {
			 log.error("onClick$btnAsignarTodos() - Error ", e);
				//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
		  }
	}
	
	
	//nueva forma de desasociar
	public void onClick$btnDesasociarTodos(){
		try {
			misession.setAttribute("desasociarTipologiaMasiva", false);
			session.setAttribute("interface", CBConsultaEstadoCuentasController.this);
			session.setAttribute("paramsListbox", paramsLlenaListbox);
			session.setAttribute("tipologiaMasiva", true);
			session.setAttribute("listaIDEstadoCuenta", this.listaID);
			asignarTipologia = (Window) Executions.createComponents("/cbDesasociarTipologiaEstadoCuenta.zul", null, null);
			asignarTipologia.doModal();
		 } catch (Exception e) {
			 log.error("onClick$btnDesasociarTodos() - Error ", e);
			 //Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
		  }
	}
	
	
	//nueva forma de asignar por archivo depositos
	public void onClick$btnAsignarDepositosTodos(){
		try {
			misession.setAttribute("interface", CBConsultaEstadoCuentasController.this);
			misession.setAttribute("paramsListbox", paramsLlenaListbox);
			misession.setAttribute("desasociarTipologiaMasiva", true);
			misession.setAttribute("listaIDEstadoCuenta", this.listaID);
			misession.setAttribute("usuarioDesasociar", usuario);
			asignarTipologia = (Window) Executions.createComponents("/cbAsignarTipologiasDepositosMasivosEstadoCuenta.zul", null, null);
			asignarTipologia.doModal();
		 } catch (Exception e) {
			 log.error("onClick$btnAsignarDepositosTodos() - Error ", e);
			//	Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
		  }
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
		log.debug(
				"onClick$btnExcel() - " + "Generando reporte de estados de cuenta...");
		//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO, 
			//	"Generando reporte de estados de cuenta...");
		BufferedWriter bw = null;
		try {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FORMATO_FECHA2);
			// Creamos el encabezado
			
			File archivo = new File(Constantes.REPORTE_ESTADO_CUENTA + sdf.format(fecha) + Constantes.CSV);
			
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(Constantes.ENCABEZADO_ESTADO_CUENTA);
			for (Listitem iterator : this.lbxConsulta.getItems()) {
				CBConsultaEstadoCuentasModel registro = (CBConsultaEstadoCuentasModel) iterator.getValue();
				bw.write(changeNull(registro.getBanco()).trim() + "|" + changeNull(registro.getAgencia()).trim() + "|"
						+ changeNull(registro.getCuenta()).trim() + "|'" + changeNull(registro.getAsignacion()).trim().replace(",", "") + "|" + changeNull(registro.getFecha()).trim() + "|"
						+ changeNull(registro.getTexto()).trim() + "|" + changeNull(registro.getDebe()).trim() + "|"
						+ changeNull(registro.getHaber()).trim() + "|" + changeNull(registro.getIdentificador()).trim() + "|"
						+ (registro.getIdtipologia()) + "|" + changeNull(registro.getTipologia()).trim() + "|"
						+ changeNull(registro.getCodigoColector()).trim() +  "|" + changeNull(registro.getAgenciaTipologia()).trim() + "|'" + changeNull(registro.getTextoCabDoc()).trim() + "|"
						+ changeNull(registro.getObservaciones()).trim() + "|"
						+ changeNull(registro.getFechaIngresos()).trim() + "|"
						+ changeNull(registro.getNumDocumento()) +"\n");
			}
			
			log.debug(
					"onClick$btnExcel() - " + "Descarga exitosa del archivo generado...");
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO, 
					//"Descarga exitosa del archivo generado...");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", Constantes.ATENCION,
					Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();
		} catch (IOException e) {
			log.error("onClick$btnExcel() - Error ", e);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					log.error("onClick$btnExcel() - Error ", e);
					//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.SEVERE, null, e);
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

	public void recargarConsulta(String valorAsociacion, String valorAgenciaTipologia) {
		log.debug(
				"recargarConsulta() - " + "Entra a recargar consulta...");
		//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
			//.log(Level.INFO, "Entra a recargar consulta...");
		CBEstadoCuentasModel param = new CBEstadoCuentasModel();
		if(dtbDesde.getValue() != null || dtbHasta.getValue() != null) {
		param = (CBEstadoCuentasModel)session.getAttribute("paramsListbox");
		CBConsultaEstadoCuentasDAO objDao = new CBConsultaEstadoCuentasDAO();
		if("".equals(valorAsociacion)){
			valorAsociacion = Constantes.TODAS;
		}
		if("".equals(valorAgenciaTipologia)){
			valorAgenciaTipologia = Constantes.TODAS;
		}
		if(lbxConsulta.getItemCount() > 1) {
			log.debug(
					"recargarConsulta() - " + "Listbox item count es mayor a 1");
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
				//.log(Level.INFO, "Listbox item count es mayor a 1");
			log.debug(
					"recargarConsulta() - " + "VALOR FILTRO TIPOLOGIA = " + param.getTipologia());
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
				//.log(Level.INFO, "VALOR FILTRO TIPOLOGIA = " + param.getTipologia());
			
			for(Comboitem item : cmbTipologia.getItems()){
				if(item.getValue().toString().equals(param.getTipologia())){
					cmbTipologia.setSelectedItem(item);
				}
			}
			
			for (Comboitem item : cmbAgenciaTipologia.getItems()) {
				if (item.getValue().toString().equals(param.getAgenciaTipologia())) {
					cmbAgenciaTipologia.setSelectedItem(item);
				}
			}
		} else if(lbxConsulta.getItemCount() == 1){
			log.debug(
					"recargarConsulta() - " + "Listbox item count es igual a 1");
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
				//.log(Level.INFO, "Listbox item count es igual a 1");
			log.debug(
					"recargarConsulta() - " + "VALOR FILTRO TIPOLOGIA = " + valorAsociacion);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
				//.log(Level.INFO, "VALOR FILTRO TIPOLOGIA = " + valorAsociacion);
		
			for(Comboitem item : cmbTipologia.getItems()){
				if(item.getValue().toString().equals(valorAsociacion)){
					cmbTipologia.setSelectedItem(item);
				}
			}
			
			for (Comboitem item : cmbAgenciaTipologia.getItems()) {
				if (item.getValue().toString().equals(valorAgenciaTipologia)) {
					cmbAgenciaTipologia.setSelectedItem(item);
				}
			}
			
			param.setTipologia(valorAsociacion);
			param.setAgenciaTipologia(valorAgenciaTipologia);
		}
		List<CBConsultaEstadoCuentasModel> list = objDao.consultaEstadosCuenta(param);
		llenaListbox(list, 0);
		}
	}

	public void recargaConsultaConta(String fechaini, String fechafin) {
		
	}
	
	public void cleanCombo(Combobox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}
	//nuevo ejecuta sp extracto
	public void onClick$btnEjecutarComisiones() {

		session.setAttribute("interfaceTarjeta", CBConsultaEstadoCuentasController.this);

		if(dtbDesde.getValue() != null && dtbHasta.getValue() != null) {
			session.setAttribute("filtrosprincipal", true);
		}else {
			session.setAttribute("filtrosprincipal", false);
		}
		
		Executions.createComponents("/cbejecutaspextractomodal.zul", null, null);
	}

}
