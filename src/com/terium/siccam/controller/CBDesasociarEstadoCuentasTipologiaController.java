package com.terium.siccam.controller;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.controller.CBDesasociarEstadoCuentasTipologiaController;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConsultaEstadoCuentasDAO;
import com.terium.siccam.utils.CBEstadoCuentaUtils;

public class CBDesasociarEstadoCuentasTipologiaController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBDesasociarEstadoCuentasTipologiaController.class);
	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	private static final long serialVersionUID = 9176164927878418930L;
	private Textbox txtObservaciones;
	private boolean desasociarTipologiaMasiva;
	Window asignaTipologia;

	// Lista que trae los ID de estados de cuenta consultados para asignar tipologia
	// masivamente
	private List<Integer> listaID = new ArrayList<Integer>();

	String usuarioDesasociar;
	private String usuario; // CarlosGodinez -> 10/10/2017

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		usuario = obtenerUsuario().getUsuario();

		// inicia variables de sesion para desasociar masivo
		desasociarTipologiaMasiva = Boolean
				.parseBoolean(misession.getAttribute("desasociarTipologiaMasiva").toString());
		usuarioDesasociar = (String) (misession.getAttribute("usuarioDesasociar"));
		// finalizan
		listaID = (List<Integer>) misession.getAttribute("listaIDEstadoCuenta");
		log.debug(
				"doAfterCompose() - " + "Bandera desasociar tipología de manera masiva = " + desasociarTipologiaMasiva);
		//Logger.getLogger(CBDesasociarEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
				//"Bandera desasociar tipología de manera masiva = " + desasociarTipologiaMasiva);

	}

	public void refrescarModulo(String valorAsociacion, String valorAgenciaTipologia, int exitosas) {
		CBConsultaEstadoCuentasController instanciaPrincipal = new CBConsultaEstadoCuentasController();
		instanciaPrincipal = (CBConsultaEstadoCuentasController) session.getAttribute("interface");
		instanciaPrincipal.recargarConsulta(valorAsociacion, valorAgenciaTipologia);
		Messagebox.show(
				"Operación exitosa.\n\nRegistros actualizados: " + exitosas
						+ "\nEl/Los estado(s) de cuenta que acaba de "
						+ "modificar se ha(n) agregado a los registros con: \n\nTipología = " + valorAsociacion + ".",
				"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
		asignaTipologia.onClose();
	}

	String observaciones;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnDesasociar() {
		if (txtObservaciones.getText().trim() == null || txtObservaciones.getText().trim().equals("")) {
			Messagebox.show("El campo es necesario para desasociar tipologias, ingrese el motivo del cambio.",
					"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);

		} else {
			log.debug(
					"onClick$btnDesasociar() - " + "Bandera desasociar tipologia " + txtObservaciones);
			//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName()).log(Level.INFO,
					//"Bandera desasociar tipologia " + txtObservaciones);
			System.out.println("observaciones " + txtObservaciones.getText());
			try {
				Messagebox.show("¿Desea desasociar esta tipologia poliza al estado de cuenta seleccionado?",
						"CONFIRMACION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.YES) {
									CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
									observaciones = txtObservaciones.getText().trim();

									if (objeDAO.desasociarTipologiaMasiva(listaID, usuario, observaciones)) {
										log.debug(
												"onClick$btnDesasociar() - " + "Entra a desasociar tipologias masivas ");
										//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
											//	.log(Level.INFO, "Entra a desasociar tipologias masivas ");
										int exitosas = listaID.size();

										refrescarModulo("(No asignada)", "(No asignada)", exitosas);
										// Messagebox.show("Registros desasociados con éxito.", "ATENCIÓN",
										// Messagebox.OK,Messagebox.INFORMATION);
									}

								}
							}
						});
			} catch (Exception e) {
				log.error(
						"onClick$btnDesasociar() - " + "-Error ", e);
				//Logger.getLogger(CBDesasociarEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null,
					//	e);
				Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			}

		}

	}

	/**
	 * Cambiamos el formato de la fecha
	 */
	public String changeDate(String fecha) {
		String result = "";
		try {
			// System.out.println("fecha: "+fecha);
			result = fecha.replace(".", "/");
			// System.out.println("result: "+result);
		} catch (Exception e) {
			log.error(
					"changeDate() - " + "-Error ", e);
			//Logger.getLogger(CBDesasociarEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
		}
		return result;
	}

	/**
	 * Validamos si el string enviado es fecha
	 */
	public boolean isDate(String fecha) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		@SuppressWarnings("unused")
		Date fec;
		try {
			fec = format.parse(fecha);
			// System.out.println("fecha: " + fec.getDate());
			return true;
		} catch (ParseException e) {
			log.error(
					"isDate() - " + "-Error ", e);
			//Logger.getLogger(CBDesasociarEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		} catch (NullPointerException e) {
			log.error(
					"isDate() - " + "-Error ", e);
			//Logger.getLogger(CBDesasociarEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
	}

}
