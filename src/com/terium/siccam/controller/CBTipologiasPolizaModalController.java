package com.terium.siccam.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBMantenimientoTipologiasPolizaDAO;
import com.terium.siccam.model.CBMantenimientoPolizaModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;

public class CBTipologiasPolizaModalController extends ControladorBase {
	private static Logger log = Logger.getLogger(CBTipologiasPolizaModalController.class);


	/**
	 * creador ovidio santos
	 */
	private static final long serialVersionUID = 1L;
	private Textbox tbxCentroBeneficio;
	private Textbox tbxDivision;
	private Textbox tbxOrdenProyecto;
	private Textbox tbxTipoCambio;
	private Textbox tbxFechaConversion;
	private Textbox tbxIndicadorcme;
	private Textbox tbxCarPaSegmento;
	private Textbox tbxCarPaServicio;

	private Textbox tbxCarPaTipoTrafico;
	private Textbox tbxCarPaAmbito;
	private Textbox tbxCarPaLicencia;
	private Textbox tbxCarPaRegion;
	private Textbox tbxSubTipoLinea;
	private Textbox tbxCanal;
	private Textbox tbxBundle;
	private Textbox tbxProducto;
	private Textbox tbxEmpresaGrupo;
	

	private Textbox tbxProyecto;
	private Textbox tbxSociedadAsociada;
	private Textbox tbxGrafo1;
	private Textbox tbxGrafo2;
	private Textbox tbxSubSegmento;
	private Textbox tbxRef1;
	private Textbox tbxRef2;
	
	private Textbox tbxTcode;
	private Textbox tbxProc;
	private Textbox tbxLlave;
	private Textbox tbxCalcAutoIva;
	private Textbox tbxRef3;
	
	private Textbox tbxFechaValor;
	private Textbox tbxSociedad;
	

	// private Intbox ibxTipo;
	// private Intbox ibxEntidad;
	Button btnConsutar;
	Button btnNuevo;
	Button btnDelete;
	Button btnModificar;
	Button btnLimpiar;
	Listbox lbxConsulta;
	int idseleccionado = 0;
	Window contabilizacionModa;
	private Label lblNombre;
	HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	//CBMantenimientoPolizaModel objModelModal = new CBMantenimientoPolizaModel();	
	static Window nuevaAgenciaComercial;
	CBMantenimientoPolizaModel objModelModal = null;
	
	public void doAfterCompose(Component param) throws Exception {
		super.doAfterCompose(param);
		
		// llenaListbox();
		//llenaListbox(objModel, 0);
		btnModificar.setDisabled(false);
		usuario = obtenerUsuario().getUsuario();
		objModelModal =  (CBMantenimientoPolizaModel) misession.getAttribute("objModelModal");
		log.debug("doAfterCompose()  " + " - idseleccionado  en doafter" + objModelModal);
		
		//objModel.setCbtipologiaspolizaid(Integer.parseInt(session.getAttribute("idseleccionado").toString()));
		llenaComboPideEntidad();
		llenarcasillas();
	
		
	}



	private String usuario;
	// boton agregar
	
	

	public void onClick$btnModificar() throws SQLException, NamingException {
		{
			
					CBMantenimientoTipologiasPolizaDAO objDAO = new CBMantenimientoTipologiasPolizaDAO();
					CBMantenimientoPolizaModel objModel = new CBMantenimientoPolizaModel();
					String user = getUsuario();
					objModel.setCentrodebeneficio(tbxCentroBeneficio.getText().trim());
					objModel.setDivision(tbxDivision.getText().trim());
					objModel.setOrdendeproyecto(tbxOrdenProyecto.getText().trim());
					objModel.setTipodecambio(tbxTipoCambio.getText().trim());
					objModel.setFechadecomversion(tbxFechaConversion.getText().trim());
					objModel.setIndicadorcme(tbxIndicadorcme.getText().trim());

					objModel.setCarpasegmento(tbxCarPaSegmento.getText().trim());
					objModel.setCarpaservicio(tbxCarPaServicio.getText().trim());
					objModel.setCarpatipotrafico(tbxCarPaTipoTrafico.getText().trim());
					objModel.setCarpaambito(tbxCarPaAmbito.getText().trim());
					objModel.setCarpalicencia(tbxCarPaLicencia.getText().trim());
					objModel.setCarparegion(tbxCarPaRegion.getText().trim());
					objModel.setSubtipolinea(tbxSubTipoLinea.getText().trim());
					objModel.setCanal(tbxCanal.getText().trim());
					objModel.setBundle(tbxBundle.getText().trim());
					objModel.setProducto(tbxProducto.getText().trim());
					objModel.setEmpresagrupo(tbxEmpresaGrupo.getText().trim());
					

				
					objModel.setProyecto(tbxProyecto.getText().trim());
					objModel.setSociedadasociada(tbxSociedadAsociada.getText().trim());
					objModel.setGrafo1(tbxGrafo1.getText().trim());
					objModel.setGrafo2(tbxGrafo2.getText().trim());
					objModel.setSubsegmento(tbxSubSegmento.getText().trim());
					objModel.setRef1(tbxRef1.getText().trim());
					objModel.setRef2(tbxRef2.getText().trim());
					
					objModel.setTcode(tbxTcode.getText().trim());
					objModel.setProc(tbxProc.getText().trim());
					objModel.setLlave(tbxLlave.getText().trim());
					objModel.setCalc_auto_iva(tbxCalcAutoIva.getText().trim());
					objModel.setRef3(tbxRef3.getText().trim());
					
					objModel.setSociedad(tbxSociedad.getText().trim());
					//objModel.setFecha_valor(tbxFechaValor.getText().trim());

				
					

					if (cmbFechaValor.getSelectedItem() != null) {
						objModel.setFecha_valor(cmbFechaValor.getSelectedItem().getValue().toString());
					} else {
						objModel.setFecha_valor(null);
					}
					
					objModel.setCbtipologiaspolizaid(objModelModal.getCbtipologiaspolizaid());
					objModel.setUsuario(user);
					

					log.debug("onClick$btnModificar()  " + " - id en el controlador de modificar " + objModelModal.getCbtipologiaspolizaid());
					
					if (objDAO.modificaTipologiasModal(objModel)) {
						// llenaListbox();
						log.debug("onClick$btnModificar()  " + " - fecha en el controlador de modificar " + objModel.getFecha_valor());
						

						Messagebox.show("Se actualizo el registro", "ATTENTION", Messagebox.OK, Messagebox.INFORMATION);
						
						refrescarModulo(objModel);

					} else {
						Messagebox.show("No se Modifico Tipologia!", "ATTENTION", Messagebox.OK,
								Messagebox.EXCLAMATION);
					}

					// } else {
					// Messagebox.show("Es necesario llenar todos los campos!",
					// "ATTENTION", Messagebox.OK,
					// Messagebox.EXCLAMATION);
					// }
				
			
		}
	}
	Combobox cmbFechaValor;
	private List<CBParametrosGeneralesModel> lstPideEntidad = new ArrayList<CBParametrosGeneralesModel>();
	public void llenaComboPideEntidad() {
		log.debug("lstPideEntidad()  " + " - Llena combo pide entidad" );
		//Logger.getLogger(CBMantenimientoTipologiasPolizaDAO.class.getName()).log(Level.INFO,
			//	"Llena combo tipo estado" );
		limpiaCombobox(cmbFechaValor);

		CBMantenimientoTipologiasPolizaDAO objeDAO = new CBMantenimientoTipologiasPolizaDAO();
		this.lstPideEntidad = objeDAO.obtenerFechaValor("FECHA_VALOR");
		if (lstPideEntidad.size() > 0) {
			for (CBParametrosGeneralesModel d : this.lstPideEntidad) {
				Comboitem item = new Comboitem();
				item.setParent(this.cmbFechaValor);
				item.setValue(d.getValorObjeto1());
				item.setLabel(d.getObjeto());

			}
		}

	}
	
	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}
	
	
	public void refrescarModulo(CBMantenimientoPolizaModel objModel) {
		CBTipologiasPolizaController instanciaPrincipal = new CBTipologiasPolizaController();
		instanciaPrincipal = (CBTipologiasPolizaController) session.getAttribute("interfaceTarjeta");

		
		instanciaPrincipal.recargaConsultaConta(objModel);
	
		onClick$closeBtn();

	}
	
	public void llenarcasillas() {
		log.debug("llenarcasillas()  " + " - id seleccioando " + idseleccionado);
		
		//CBMantenimientoPolizaModel objModel =  new CBMantenimientoPolizaModel();
		//idseleccionado = (Integer) arg0.getTarget().getAttribute("idseleccionado");
		log.debug("llenarcasillas()  " + " - id seleccioando " + idseleccionado);
		
		//String nombrepoliza = objmodificar.getNombre();
		lblNombre.setValue(objModelModal.getNombre());
		tbxCentroBeneficio.setText(objModelModal.getCentrodebeneficio());
		tbxDivision.setText(objModelModal.getDivision());
		tbxOrdenProyecto.setText(objModelModal.getOrdendeproyecto());
		tbxTipoCambio.setText(objModelModal.getTipodecambio());
		tbxFechaConversion.setText(objModelModal.getFechadecomversion());
		tbxIndicadorcme.setText(objModelModal.getIndicadorcme());
		tbxCarPaSegmento.setText(objModelModal.getCarpasegmento());
		System.out.println("car pa segmento "+ tbxCarPaSegmento);
		tbxCarPaServicio.setText(objModelModal.getCarpaservicio());
		tbxCarPaTipoTrafico.setText(objModelModal.getCarpatipotrafico());
		tbxCarPaAmbito.setText(objModelModal.getCarpaambito());
		tbxCarPaRegion.setText(objModelModal.getCarparegion());
		tbxCarPaLicencia.setText(objModelModal.getCarpalicencia());
		tbxSubTipoLinea.setText(objModelModal.getSubtipolinea());
		tbxCanal.setText(objModelModal.getCanal());
		tbxBundle.setText(objModelModal.getBundle());
		tbxProducto.setText(objModelModal.getProducto());
		tbxEmpresaGrupo.setText(objModelModal.getEmpresagrupo());
	
		
		tbxProyecto.setText(objModelModal.getProyecto());
		tbxSociedadAsociada.setText(objModelModal.getSociedadasociada());
		tbxGrafo1.setText(objModelModal.getGrafo1());
		tbxGrafo2.setText(objModelModal.getGrafo2());
		tbxSubSegmento.setText(objModelModal.getSubsegmento());
		tbxRef1.setText(objModelModal.getRef1());
		tbxRef2.setText(objModelModal.getRef2());
		
		tbxTcode.setText(objModelModal.getTcode());
		tbxProc.setText(objModelModal.getProc());
		tbxCalcAutoIva.setText(objModelModal.getCalc_auto_iva());
		tbxLlave.setText(objModelModal.getLlave());
		tbxRef3.setText(objModelModal.getRef3());
		tbxSociedad.setText(objModelModal.getSociedad());
		for (Comboitem item : cmbFechaValor.getItems()) {
			if (item.getValue().equals(String.valueOf(objModelModal.getFecha_valor()))) {
				log.debug("llenarcasillas()  " + " - combo fecha valor en if " + objModelModal.getFecha_valor());
				
				cmbFechaValor.setSelectedItem(item);
			}
		}
		
	


		
	}
	
	
	
	

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	public void onClick$closeBtn() {

		contabilizacionModa.detach();
	}


}
