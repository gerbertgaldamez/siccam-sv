package com.terium.siccam.controller;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConsultaContabilizacionDAO;
import com.terium.siccam.model.CBConsultaContabilizacionModel;



public class CBConsultaContabilizacionModalController extends ControladorBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Textbox tbxTexto;
	Textbox tbxObservaciones;
	Textbox tbxTexto2;
	Textbox tbxCentroCosto;
	Textbox tbxCuenta;
	Textbox tbxClave;
	Textbox tbxReferencia;
	Listbox lbxConsulta;
	Datebox dtbDesde;
	Datebox dtbHasta;
	Checkbox ckbMarcarAll;

	int idseleccionado = 0;
	Button btnLimpiar;

	String ArchivosCargados;
	String usuario;
	
	
	Button btnConsultar;
	Button btnExcel;
	Button btnModificar;
	Button btnSAP;
	Window contabilizacionModa;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	HttpSession misession1 = (HttpSession) Sessions.getCurrent().getNativeSession();
	HttpSession misession2 = (HttpSession) Sessions.getCurrent().getNativeSession();
	List<CBConsultaContabilizacionModel> detallesSeleccionados = null;
	CBConsultaContabilizacionModel objModel = new CBConsultaContabilizacionModel();
	List<CBConsultaContabilizacionModel> listConsulta = null;

	CBConsultaContabilizacionModel objb = null;
	String fechaini = null;
	String fechafin = null;
	

	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);
		objb = (CBConsultaContabilizacionModel) misession.getAttribute("sesioncontabilizacionModal");
		fechaini =  (String) misession1.getAttribute("sesionfecha1");
		fechafin =  (String) misession2.getAttribute("sesionfecha2");
		llenarcasillas();
	}

	String asociacion;

	public void onClick$btnModificar() throws SQLException {
		
		CBConsultaContabilizacionDAO objDAO = new CBConsultaContabilizacionDAO();
		objModel.setCentroCosto(tbxCentroCosto.getText().trim());
		objModel.setClaveContabilizacion(tbxClave.getText().trim());
		objModel.setReferencia(tbxReferencia.getText().trim());
		objModel.setCuenta(tbxCuenta.getText().trim());
		objModel.setTexto(tbxTexto.getText().trim());
		objModel.setTexto2(tbxTexto2.getText().trim());

		objModel.setObservaciones(tbxObservaciones.getText().trim());

		// objb.setCbcontabilizacionid(idseleccionado);

		Logger.getLogger(CBConsultaContabilizacionController.class.getName())
			.log(Level.INFO, "en el controlador de modificar " + objModel);
		if (objDAO.update(objModel, objb.getCbcontabilizacionid())) {
			refrescarModulo(fechaini, fechafin);
			Messagebox.show("Se actualizo el registro", "ATENCION", Messagebox.OK, Messagebox.INFORMATION);
			// limpiartextbox();
			
			
			
		} else {
			Messagebox.show("No se Modifico !", "ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}

	public void llenarcasillas() {
		Logger.getLogger(CBConsultaContabilizacionController.class.getName())
			.log(Level.INFO, "id seleccioando " + idseleccionado);

		tbxCentroCosto.setText(objb.getCentroCosto());
		tbxClave.setText(objb.getClaveContabilizacion());
		tbxCuenta.setText(objb.getCuenta());
		tbxReferencia.setText(objb.getReferencia());
		tbxTexto.setText(objb.getTexto());
		tbxTexto2.setText(objb.getTexto2());
		tbxObservaciones.setText(objb.getObservaciones());

	}

	public void onClick$btnLimpiar() {

		limpiartextbox();
	}

	public void limpiartextbox() {

		tbxCentroCosto.setText("");
		tbxClave.setText("");
		tbxObservaciones.setText("");
		tbxTexto.setText("");
		tbxTexto2.setText("");
		tbxReferencia.setText("");
		tbxCuenta.setText("");

	}

	public void onClick$closeBtn() {

		contabilizacionModa.detach();
	}

	DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");

	public void refrescarModulo(String fechaini, String fechafin) {
		CBConsultaContabilizacionController instanciaPrincipal = new CBConsultaContabilizacionController();
		instanciaPrincipal = (CBConsultaContabilizacionController) session.getAttribute("interfaceTarjeta");
		onClick$btnLimpiar();
		System.out.println("fechaini " + fechaini);
		System.out.println("fechafin " + fechafin);
		instanciaPrincipal.recargaConsultaConta(fechaini, fechafin);
	
		onClick$closeBtn();

	}

}
