package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.Component;
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

import com.terium.siccam.dao.CBConciliacionCajasDAO;
import com.terium.siccam.dao.CBConsultaEstadoCuentasDAO;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBConsultaEstadoCuentasModel;
import com.terium.siccam.utils.Constantes;

public class CBConciliacionCajasController extends MenuController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3861241777448570392L;

	// Componentes para manejo de vista MVC
	Combobox cmbBanco;
	Combobox cmbAgencia;
	Combobox cmbEntidad;
	Listbox lbxConsulta;
	Datebox dtbDesde;
	Datebox dtbHasta;
	Button btnReporte;
	

	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		llenaComboEntidad();
		// llenaComboAgencia();
		// llenaComboBanco();

	}

	public void llenaComboEntidad() {
		limpiaCombobox(cmbEntidad);
		CBConciliacionCajasDAO objDao = new CBConciliacionCajasDAO();
		List<CBConciliacionCajasModel> list = objDao.generaConsultaEntidad();
		Iterator<CBConciliacionCajasModel> it = list.iterator();
		CBConciliacionCajasModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getIdcombo());
			item.setParent(cmbEntidad);
		}
	}

	public void llenaComboAgencia(int idBanco) {
		limpiaCombobox(cmbAgencia);
		CBConciliacionCajasDAO objDao = new CBConciliacionCajasDAO();
		List<CBConciliacionCajasModel> list = objDao.generaConsultaAgencia(idBanco);
		Iterator<CBConciliacionCajasModel> it = list.iterator();
		CBConciliacionCajasModel obj = null;
		//cmbAgencia.setName(Constantes.TODAS);
		Comboitem item = new Comboitem();
		item = new Comboitem();
		item.setValue("0");
		item.setLabel(Constantes.TODAS);
		item.setParent(cmbAgencia);
		while (it.hasNext()) {
			obj = it.next();
			item = new Comboitem();
		
			item.setLabel(obj.getNombre());
			
			item.setValue(obj.getIdcombo());
			item.setParent(cmbAgencia);
		}
	}
	
	
	
	public void limpiar() {
		
	}

	public void llenaComboBanco() {
		CBConciliacionCajasDAO objDao = new CBConciliacionCajasDAO();
		List<CBConciliacionCajasModel> list = objDao.generaConsultaBanco();
		Iterator<CBConciliacionCajasModel> it = list.iterator();
		CBConciliacionCajasModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getIdcombo());
			item.setParent(cmbBanco);
		}
	}

	public void onSelect$cmbEntidad() {
		llenaComboAgencia((Integer) cmbEntidad.getSelectedItem().getValue());
	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	public void onClick$btnBuscar() {
		limpiarListbox(lbxConsulta);
		CBConciliacionCajasDAO objDao = new CBConciliacionCajasDAO();
		int entidad = 0;
		int agencia = 0;
		String fechaini = null;
		String fechafin = null;
		DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
		if (cmbEntidad.getSelectedItem() != null) {
			if (cmbEntidad.getSelectedItem().getLabel().equals("Todas")) {
				entidad = 0;
			} else {
				entidad = cmbEntidad.getSelectedItem().getValue();
			}
		}
		if (cmbAgencia.getSelectedItem() != null) {
			if (cmbAgencia.getSelectedItem().getLabel().equals("Todas")) {
				agencia = 0;
			} else {
				agencia = cmbAgencia.getSelectedItem().getValue();
			}
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
		llenaListbox(objDao.generaConsultaPrincipal(entidad, agencia, fechaini, fechafin));

	}

	/**
	 * 
	 * 
	 * */
	public void llenaListbox(List<CBConciliacionCajasModel> list) {
		if (list != null && list.size() > 0) {
			Iterator<CBConciliacionCajasModel> it = list.iterator();
			CBConciliacionCajasModel obj = null;
			Listcell cell = null;
			Listitem item = null;
			System.out.println("tama;o de lista: " + list.size());
			while (it.hasNext()) {
				obj = it.next();
				item = new Listitem();

				// Datos para la agrupacion general
				cell = new Listcell();
				cell.setLabel(obj.getEntidad());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(obj.getAgencia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(obj.getFecha());
				cell.setParent(item);

				// Datos para la agrupacion de cajas
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajaefectivo()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajacheque()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajaexenciones()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajacuotascredomatic()));
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajacuotasvisa()));
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajatarjetacredomatic()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajatarjetaotras()));
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajatarjetavisa()));
				cell.setParent(item);
				
				//Datos para la agrupación de sistema comercial
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getScefectivo()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getScpagosod()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getScpagosom()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getScreversasod()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getScreversasom()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getTotaldia()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCajatotal()));
				cell.setParent(item);

				// Datos para la agrupación de estado de cuenta
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getDeposito()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCredomaticdep()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getCredomaticRet()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getEstadoCredo()));
				cell.setParent(item);
				
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getConsumovisa()));
				cell.setParent(item);
					
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getIvavisa()));
				cell.setParent(item);
					
				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getEstadoVisa()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getTotalec()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(String.valueOf(obj.getDiferencia()));
				cell.setParent(item);

				item.setValue(obj);
				item.setParent(lbxConsulta);

			}
			btnReporte.setDisabled(false);
		} else {
			btnReporte.setDisabled(true);
			Messagebox.show("No se encontraron registros para ese rango de fecha!", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}
	
	/**
	 * cambia forma de generar reporte ovidio santos 11-04-2018
	 * 
	 * */
	//*
	public void onClick$btnReporte() {
		
		
		Date fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FORMATO_FECHA2);

		// Creamos el encabezado
		String encabezado = "Agrupacion|Entidad|Fecha|"
				+ "Caja efectivo|Caja cheque|Caja extension|Caja cuotas credomatic|Cuotas Otras|Caja tarjeta credomatic|Tarjeta Otras|"
				+ "Caja tarjeta visa|Pagos Dia|" 
				+ "SC Pagosod|SC Pagosom|SC Reversaod|SC Reversaom|Total dia|Total Gnral|"
				+ "Deposito|Consumo credomatic|Retencion credomatic|Liquido credomatic|Consumo Otros|Retención Otros|Liquido Otros|" + "Total EC|Diferencia\n";
		
		File archivo = new File("reporte_conciliacion_cajas_"+sdf.format(fecha)+Constantes.CSV);
		BufferedWriter bw = null;
		try {
			if(!archivo.exists()) {		
				if(archivo.createNewFile())
					Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.INFO, "Archivo temporal creado");
			}
			
			if(lbxConsulta != null ){
				bw = new BufferedWriter(new FileWriter(archivo));
				bw.write(encabezado);
				for (Listitem iterator : this.lbxConsulta.getItems()) {
					CBConciliacionCajasModel obj = (CBConciliacionCajasModel) iterator.getValue();
										 
					bw.write(obj.getEntidad() + "|" + obj.getAgencia() + "|" + obj.getFecha() + "|"
							+ obj.getCajaefectivo() + "|" + obj.getCajacheque() + "|" + obj.getCajaexenciones() + "|"
							+ obj.getCajacuotascredomatic()  + "|" + obj.getCajacuotasvisa() + "|" + obj.getCajatarjetacredomatic() + "|"
							+ obj.getCajatarjetaotras() + "|" + obj.getCajatarjetavisa() +"|" + obj.getScefectivo()+ "|" + obj.getScpagosod() + "|" + obj.getScpagosom() + "|"
							+ obj.getScreversasod() + "|" + obj.getScreversasom() + "|" + obj.getTotaldia() + "|"
							+ obj.getCajatotal() + "|" + obj.getDeposito() + "|" + obj.getCredomaticdep() + "|"
							+ obj.getCredomaticRet() + "|" + obj.getEstadoCredo() +  "|" + obj.getConsumovisa()+"|"+obj.getIvavisa()+"|"
							+obj.getEstadoVisa() + "|" + obj.getTotalec() + "|"
							+ obj.getDiferencia() + "\n");
				}
				
			}
			System.out.println("Descargamos el archivo");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado",
							"ATENCION", Messagebox.OK,
							Messagebox.INFORMATION);
			
			Clients.clearBusy();
		} catch (Exception e) {
			Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(bw!=null) {
				try {
					bw.close();
				} catch (IOException e) {
					Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}
	
	/*/
		/**
	 * 
	 * 
	 * */
	

	/**viejos
	 * 
	 * */
	/*
	public void onClick$btnReporte() {
		CBConciliacionCajasDAO objDao = new CBConciliacionCajasDAO();
		int entidad = 0;
		int agencia = 0;
		String fechaini = null;
		String fechafin = null;
		DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
		if (cmbEntidad.getSelectedItem() != null && !"Todas".equals(cmbEntidad.getSelectedItem().getLabel())) {
			entidad = cmbEntidad.getSelectedItem().getValue();
		}
		if (cmbAgencia.getSelectedItem() != null && !"Todas".equals(cmbAgencia.getSelectedItem().getLabel())) {
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
		try {
			generaReporte(objDao.generaConsultaPrincipal(entidad, agencia, fechaini, fechafin));
		} catch (IOException e) {

			Logger.getLogger(CBConciliacionBancoController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
*/
	/*
	public void generaReporte(List<CBConciliacionCajasModel> list) throws IOException {
		Iterator<CBConciliacionCajasModel> it = list.iterator();
		CBConciliacionCajasModel obj = null;
		Date fecha = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");

		// Creamos el encabezado
		String encabezado = "Agrupacion|Entidad|Fecha|"
				+ "Caja efectivo|Caja cheque|Caja extension|Caja cuotas credomatic|Caja tarjeta credomatic|"
				+ "Caja tarjeta otras|" + "SC Pagosod|SC Pagosom|SC Reversaod|SC Reversaom|Total dia|Total Gnral|"
				+ "Deposito|Consumo credomatic|Retencion credomatic|Liquido credomatic|" + "Total EC|Diferencia\n";

		File archivo = new File("reporte_conciliacion_cajas_" + sdf.format(fecha) + ".csv");
		BufferedWriter bw = null;
		try {

			if (list != null && list.size() > 0) {
				bw = new BufferedWriter(new FileWriter(archivo));
				bw.write(encabezado);
				while (it.hasNext()) {
					obj = it.next();

					bw.write(obj.getEntidad() + "|" + obj.getAgencia() + "|" + obj.getFecha() + "|"
							+ obj.getCajaefectivo() + "|" + obj.getCajacheque() + "|" + obj.getCajaexenciones() + "|"
							+ obj.getCajacuotascredomatic() + "|" + obj.getCajatarjetacredomatic() + "|"
							+ obj.getCajatarjetaotras() + "|" + obj.getScpagosod() + "|" + obj.getScpagosom() + "|"
							+ obj.getScreversasod() + "|" + obj.getScreversasom() + "|" + obj.getTotaldia() + "|"
							+ obj.getCajatotal() + "|" + obj.getDeposito() + "|" + obj.getCredomaticdep() + "|"
							+ obj.getCredomaticRet() + "|" + obj.getEstadoCredo() + "|" + obj.getTotalec() + "|"
							+ obj.getDiferencia() + "\n");
				}

			}

			Logger.getLogger(CBConciliacionCajasController.class.getName())
				.log(Level.INFO, "Inicia descarga del archivo");
			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", "ATENCION",
					Messagebox.OK, Messagebox.INFORMATION);

			Clients.clearBusy();
		} catch (Exception e) {
			Logger.getLogger(CBConciliacionCajasController.class.getName()).log(Level.SEVERE, null, e);
			System.out.println("Error al intentar crear nuevo archivo...");
		} finally {
			if (bw != null)
				bw.close();
		}
	}
*/
	public void limpiarListbox(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!componente.getMold().equals("paging")) {
				componente.setMold("paging");
				componente.setAutopaging(true);
			}
		}
	}

}
