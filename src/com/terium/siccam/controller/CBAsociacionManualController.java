package com.terium.siccam.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import javax.naming.NamingException;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConsultaEstadoCuentasDAO;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConsultaEstadoCuentasModel;

public class CBAsociacionManualController extends ControladorBase{
	
	private static Logger log = Logger.getLogger(CBAsociacionManualController.class);
	
	private static final long serialVersionUID = 9176164927878418930L;
	
	//Propiedades agregadas por Carlos Godínez - Qitcorp 02/03/2017
	private Combobox cmbEntidad;
	private Textbox txtObservaciones;
	private CBConsultaEstadoCuentasModel objSeleccionado;
	private int idEstadoCuenta;
	private boolean asociacionMasiva;
	Window asociaManual;
	
	private Button btnDesasociar;
	
	//Lista que trae los ID de estados de cuenta consultados para asociar masivamente
	private List<Integer> listaID = new ArrayList<Integer>();
	
	//Listas para llenar combobox
	private List<CBCatalogoAgenciaModel> listaAgencias = new ArrayList<CBCatalogoAgenciaModel>();
	
	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		//usuario = obtenerUsuario().getUsuario();
		this.asociacionMasiva = Boolean.parseBoolean(session.getAttribute("asociacionMasiva").toString());
		this.listaID = (List<Integer>)session.getAttribute("listaIDEstadoCuentaTarjeta");
		if(!this.asociacionMasiva){
			this.idEstadoCuenta = Integer.parseInt(session.getAttribute("idEstadoCuentaTarjeta").toString());
		}
		this.objSeleccionado = (CBConsultaEstadoCuentasModel)session.getAttribute("objAsociacionManual");
		llenaComboEntidad();
		if(!this.asociacionMasiva){
			obtenerValoresSeleccionados();
		}
		if(this.asociacionMasiva){
			this.btnDesasociar.setDisabled(true);
		} else {
			this.btnDesasociar.setDisabled(false);
		}
		System.out.println("Bandera asociar de manera masiva = " + this.asociacionMasiva);
	}
	
	public void obtenerValoresSeleccionados() throws SQLException, NamingException{
		String entidad = this.objSeleccionado.getNombreEntidad();
		String observaciones = this.objSeleccionado.getObservaciones();
		if(!entidad.equals("(No asociada)")){
			this.cmbEntidad.setText(entidad);
		} 
		this.txtObservaciones.setText(observaciones);
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
	
	public void cleanCombo(Combobox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}
	
	public void refrescarModulo(String asociacion){
		CBConsultaEstadoCuentasTarjetaController instanciaPrincipal = new CBConsultaEstadoCuentasTarjetaController();
		instanciaPrincipal = (CBConsultaEstadoCuentasTarjetaController)session.getAttribute("interfaceTarjeta");
		instanciaPrincipal.recargarConsulta(asociacion);
		Messagebox.show("Operación exitosa.\n\nEl/Los estado de cuenta de tarjeta que acaba de modificar se ha(n) agregado a los registros con: \n\nEntidad = " + asociacion + ".","ATENCION", Messagebox.OK, Messagebox.INFORMATION);
		asociaManual.onClose();
	}

	public void onClick$btnAsignar(){
		try{
			if(this.cmbEntidad.getSelectedItem() != null){ 
				CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
				int entidad = Integer.parseInt(this.cmbEntidad.getSelectedItem().getValue().toString());
				String observaciones = this.txtObservaciones.getText().trim();
				if(this.asociacionMasiva){ 
					if(objeDAO.asignarManualMasiva(entidad, observaciones, this.listaID)){
						this.refrescarModulo(this.cmbEntidad.getSelectedItem().getLabel().trim());
					}
					else{
						Messagebox.show("No se pudo completar la operacion.","ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
					}
				} else {
					if(objeDAO.asignarManual(entidad, observaciones, this.idEstadoCuenta)){
						this.refrescarModulo(this.cmbEntidad.getSelectedItem().getLabel().trim());
					}
					else{
						Messagebox.show("No se pudo completar la operacion.","ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
					}
				}
			} else{
				Messagebox.show("Debe completar todos los campos antes de realizar la operacion.","ATTENTION", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		}
		catch(Exception e){
			log.error("onClick$btnAsignar" + "error", e);
			//Logger.getLogger(CBAsociacionManualController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error.","ATENCION", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnDesasociar(){
		try {
			Messagebox.show("¿Desea desasociar esta entidad al estado de cuenta de tarjeta seleccionado?", "CONFIRMACIÓN", Messagebox.YES | Messagebox.NO,
					Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
								if(objeDAO.desasociarManual(idEstadoCuenta)){
									refrescarModulo("(No asociada)");
								}
							}
						}
					});
		} catch (Exception e) {
			log.error("onClick$btnDesasociar" + "error", e);
			//Logger.getLogger(CBAsociacionManualController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error.", "ATENCIÓN", Messagebox.OK, Messagebox.ERROR);
		}
	}
}
