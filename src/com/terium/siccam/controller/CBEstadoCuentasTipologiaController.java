package com.terium.siccam.controller;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.Reader;
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

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.controller.CBEstadoCuentasTipologiaController;
import com.google.common.base.Splitter;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConsultaEstadoCuentasDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBConsultaEstadoCuentasModel;
import com.terium.siccam.model.CBEstadoCuentasModel;
import com.terium.siccam.model.CBTipologiasPolizaModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;
import com.terium.siccam.utils.Tools;

public class CBEstadoCuentasTipologiaController extends ControladorBase {
	
	private static Logger log = Logger.getLogger(CBEstadoCuentasTipologiaController.class);
	
	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	private static final long serialVersionUID = 9176164927878418930L;
	// Propiedades agregadas por Carlos Godínez - Qitcorp 02/03/2017
	private Combobox cmbTipologia;
	private Combobox cmbEntidad;
	private Datebox dbFecha;
	private Textbox txtObservaciones;
	private CBConsultaEstadoCuentasModel objSeleccionado;
	private int idEstadoCuenta;
	private boolean tipologiaMasiva;
	private boolean desasociarTipologiaMasiva;
	private Button btnDesasociar;
	private Button btnSubirDepositos;
	Window asignaTipologia;
	// Juankrlos --> 14/07/2017
	private CBEstadoCuentasModel paramsLlenaListbox = null;

	// Lista que trae los ID de estados de cuenta consultados para asignar tipologia
	// masivamente
	private List<Integer> listaID = new ArrayList<Integer>();
	// Listas para llenar combobox
	private List<CBTipologiasPolizaModel> listaTipologia = new ArrayList<CBTipologiasPolizaModel>();
	private List<CBCatalogoAgenciaModel> listaAgencias = new ArrayList<CBCatalogoAgenciaModel>();

	// Agregado por Carlos Godinez - 13/06/2017 - Subida de archivo de depositos
	private Label lblMensaje;
	private Image imgEstatus;
	private Media media;
	private Checkbox chkFecha;
	private Checkbox chkFechaRegularizacion; // CarlosGodinez -> 30/10/2017
	private Checkbox chktipologia; // CarlosGodinez -> 30/10/2017
	// Lista que trae los ID de estados de cuenta consultados para asignar tipologia
	// masivamente
	// Comment by Juankrlos 13/07/2017
	private List<CBCatalogoAgenciaModel> listaDepositos = new ArrayList<CBCatalogoAgenciaModel>();
	private String usuario; // CarlosGodinez -> 10/10/2017

	@SuppressWarnings("unchecked")
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		usuario = obtenerUsuario().getUsuario();

		// finalizan
		listaID = (List<Integer>) misession.getAttribute("listaIDEstadoCuenta");
		log.debug(
				"doAfterCompose() - " + "Bandera desasociar tipología de manera masiva = " + desasociarTipologiaMasiva);
		//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
				//"Bandera desasociar tipología de manera masiva = " + desasociarTipologiaMasiva);

		// btnSubirDepositos.setVisible(true);
		tipologiaMasiva = Boolean.parseBoolean(misession.getAttribute("tipologiaMasiva").toString());
		objSeleccionado = (CBConsultaEstadoCuentasModel) misession.getAttribute("objEditarTipologia");

		llenaComboTipologia();
		chktipologia.setDisabled(true);
		chkFechaRegularizacion.setDisabled(true);

		if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {

			chkFechaRegularizacion.setVisible(true); // CarlosGodinez -> 30/10/2017
			chktipologia.setVisible(true); // CarlosGodinez -> 30/10/2017
			btnSubirDepositos.setVisible(true);

		} else {
			chkFechaRegularizacion.setVisible(false); // CarlosGodinez -> 30/10/2017
			chktipologia.setVisible(true); // CarlosGodinez -> 30/10/2017
			btnSubirDepositos.setVisible(false);
		}
		// chkFechaRegularizacion.setDisabled(true); //CarlosGodinez -> 30/10/2017
		if (tipologiaMasiva) {
			idEstadoCuenta = 0;
			btnDesasociar.setDisabled(true);
			chktipologia.setDisabled(false);
			if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
				paramsLlenaListbox = (CBEstadoCuentasModel) session.getAttribute("paramsListbox");
			}

		} else {
			chktipologia.setDisabled(true);
			idEstadoCuenta = Integer.parseInt(session.getAttribute("idEstadoCuenta").toString());
			obtenerValoresSeleccionados();
			btnDesasociar.setDisabled(false);
			paramsLlenaListbox = (CBEstadoCuentasModel) session.getAttribute("paramsListbox");
		}
		// lblMensaje.setValue("No se ha seleccionado ningun archivo de depósitos");
		// imgEstatus.setSrc("img/negro.png");
		log.debug(
				"doAfterCompose() - " + "Bandera asignar tipología de manera masiva = " + tipologiaMasiva);
		//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
				//"Bandera asignar tipología de manera masiva = " + tipologiaMasiva);

	}

	public void obtenerValoresSeleccionados() {
		try {
			int tipologia = objSeleccionado.getIdtipologia();
			int agencia = objSeleccionado.getIdAgenciaTipologia();
			String fecha = objSeleccionado.getFechaIngresos();
			String observaciones = objSeleccionado.getObservaciones();

			log.debug(
					"obtenerValoresSeleccionados() - " + "ID tipologia = " + objSeleccionado.getIdtipologia());
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
					//"ID tipologia = " + objSeleccionado.getIdtipologia());
			log.debug(
					"obtenerValoresSeleccionados() - " + "ID agencia tipologia = " + objSeleccionado.getIdAgenciaTipologia());
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
				//	"ID agencia tipologia = " + objSeleccionado.getIdAgenciaTipologia());
			log.debug(
					"obtenerValoresSeleccionados() - " + "fecha = " + objSeleccionado.getFechaIngresos());
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
					//"fecha = " + objSeleccionado.getFechaIngresos());
			log.debug(
					"obtenerValoresSeleccionados() - " + "observaciones = " + objSeleccionado.getObservaciones());
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
					//"observaciones = " + objSeleccionado.getObservaciones());

			if (!"(No asignada)".equals(objSeleccionado.getTipologia())) {
				for (Comboitem citem : cmbTipologia.getItems()) {
					if (citem.getValue().toString().equals(String.valueOf(tipologia))) {
						cmbTipologia.setSelectedItem(citem);
					}
				}
				obtieneEntidadesAsociadas();
				if (!"(No asignada)".equals(objSeleccionado.getAgenciaTipologia())) {
					for (Comboitem citem : cmbEntidad.getItems()) {
						if (citem.getValue().toString().equals(String.valueOf(agencia))) {
							cmbEntidad.setSelectedItem(citem);
						}
					}
				}
				dbFecha.setText(fecha);
				txtObservaciones.setText(observaciones);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("obtenerValoresSeleccionados() - Error ", e);
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void llenaComboTipologia() {
		try {
			CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
			listaTipologia = objeDAO.obtenerTipologias();
			for (CBTipologiasPolizaModel d : listaTipologia) {
				Comboitem item = new Comboitem();
				item.setParent(cmbTipologia);
				item.setValue(d.getCbtipologiaspolizaid());
				item.setAttribute("pideEntidad", d.getPideEntidad());
				item.setLabel(d.getNombre());
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("llenaComboTipologia() - Error ", e);
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	/**
	 * Modified by CarlosGodinez -> 17/08/2018 Realiza consultas de entidades
	 * asociadas a tipologia seleccionada
	 */
	public void llenaComboEntidad(int tipologiaRecuperada) {
		try {
			CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
			listaAgencias = objeDAO.obtenerEntidadesAsociadasCmb(tipologiaRecuperada);
			for (CBCatalogoAgenciaModel d : listaAgencias) {
				Comboitem item = new Comboitem();
				item.setParent(cmbEntidad);
				item.setValue(d.getcBCatalogoAgenciaId());
				item.setLabel(d.getNombre());
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("llenaComboEntidad() - Error ", e);
		//	Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);

		}
	}

	// Método que se invoca al seleccionar una tipología...llenar combobox de
	// agencias
	public void onSelect$cmbTipologia() {
		obtieneEntidadesAsociadas();
	}

	public void obtieneEntidadesAsociadas() {
		try {
			int opcion = Integer.parseInt(cmbTipologia.getSelectedItem().getAttribute("pideEntidad").toString());
			if (opcion == 1) { // Si la tipologia pide entidad...
				cleanCombo(cmbEntidad);
				/**
				 * Modified by CarlosGodinez -> 17/08/2018 Obtiene entidades asociadas a
				 * tipologia seleccionada
				 */
				int tipologiaRecuperada = Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString());
				llenaComboEntidad(tipologiaRecuperada);
				if (listaAgencias.isEmpty()) {
					cmbEntidad.setText("");
				} else {
					cmbEntidad.setSelectedIndex(0);
				}
			} else {
				cleanCombo(cmbEntidad);
				Comboitem item = new Comboitem();
				item.setParent(cmbEntidad);
				item.setValue(0);
				item.setLabel("N/A");
				cmbEntidad.setSelectedIndex(0);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("obtieneEntidadesAsociadas() - Error ", e);
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	public void onUpload$btnSubirDepositos(UploadEvent event) {
		try {
			if (listaDepositos != null && listaDepositos.size() > 0) {
				listaDepositos.clear();
			}
			// CBEstadoCuentasModel objSociedad = null;
			media = event.getMedia();

			log.debug(
					"onUpload$btnSubirDepositos() - " + "-----File Information-----");
			//Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					//"-----File Information-----");
			log.debug(
					"onUpload$btnSubirDepositos() - " + "name: " + media.getName());
			//Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					//"name: " + media.getName());
			log.debug(
					"onUpload$btnSubirDepositos() - " + "contentType: " + media.getContentType());
			//Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					//"contentType: " + media.getContentType());
			log.debug(
					"onUpload$btnSubirDepositos() - " + "format: " + media.getFormat());
			//Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					//"format: " + media.getFormat());

			if (media.getFormat().equals("txt")) {
				Reader read = media.getReaderData();
				BufferedReader reader = new BufferedReader(read);
				String line;
				int fila = 0;
				CBCatalogoAgenciaModel objDepositos = null;

				//
				while ((line = reader.readLine()) != null) {
					if (fila >= 0) {

						// nueva forma de leer el archivo
						objDepositos = new CBCatalogoAgenciaModel();

						int contador = 0;
						@SuppressWarnings("unused")
						int colNum = 0;
						Iterable<String> parts = Splitter.on('\t').split(line);

						for (@SuppressWarnings("unused")
						String column : parts) {
							colNum++;
						}

						for (String token : parts) {
							contador++;

							switch (contador) {

							case 1:
								objDepositos.setDeposito(token.trim());
								break;
							// para el campo numero de cuenta
							case 2:
								objDepositos.setTipologia(token.trim());
								break;
							case 3:
								objDepositos.setEntidadDeposito(token.trim());
								break;

							case 4:
								objDepositos.setFechaDeposito(token.trim());
								break;

							}
						}

						if (objDepositos.getTipologia() == null || objDepositos.getTipologia().equals("")) {
							if (objDepositos.getDeposito().length() > 5) {
								listaDepositos.add(objDepositos);
							}

						}
					}
					fila++;
				}
				if (listaDepositos.size() > 0) {
					System.out.println("lista: " + listaDepositos.size());
					lblMensaje.setValue("Archivo seleccionado: " + media.getName());
					lblMensaje.setStyle("color:blue;");
					imgEstatus.setSrc("img/azul.png");
					if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
						chkFechaRegularizacion.setDisabled(false); // CarlosGodinez -> 30/10/2017
						chktipologia.setDisabled(false); // CarlosGodinez -> 30/10/2017
					}
				} else {
					lblMensaje.setValue("El archivo que se ha cargado viene vacío.");
					lblMensaje.setStyle("color:red;");
					imgEstatus.setSrc("img/rojo.png");
					if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
						chkFechaRegularizacion.setDisabled(true); // CarlosGodinez -> 30/10/2017
						chktipologia.setDisabled(true); // CarlosGodinez -> 30/10/2017
					}
				}
			} else {
				lblMensaje.setValue("Error en formato -- Archivo seleccionado: " + media.getName());
				lblMensaje.setStyle("color:red;");
				imgEstatus.setSrc("img/rojo.png");
			}
		} catch (IOException e) {
			Messagebox.show("Ha ocurrido un error al cargar el archivo.", "ERROR", Messagebox.OK, Messagebox.ERROR);
			log.error("onUpload$btnSubirDepositos() - Error ", e);
			//Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.SEVERE, null,
					//e);

		}
	}

	/**
	 * Agregado por Carlos Godinez - 13/06/2017
	 * 
	 * Metodo que identifica el archivo de depositos que se ha seleccionado
	 * 
	 * @throws IOException
	 */
	/*
	 * public void onUpload$btnSubirDepositos1(UploadEvent event) { try { if
	 * (listaDepositos != null && listaDepositos.size() > 0) {
	 * listaDepositos.clear(); } // CBEstadoCuentasModel objSociedad = null; media =
	 * event.getMedia();
	 * 
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.INFO, "-----File Information-----");
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.INFO, "name: " + media.getName());
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.INFO, "contentType: " + media.getContentType());
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.INFO, "format: " + media.getFormat());
	 * 
	 * if (media.getFormat().equals("txt")) { Reader read = media.getReaderData();
	 * BufferedReader reader = new BufferedReader(read); String line; int fila = 0;
	 * CBCatalogoAgenciaModel objDepositos = null;
	 * 
	 * @SuppressWarnings("unused") String registro = null;
	 * 
	 * @SuppressWarnings("unused") int celda = 0; // while ((line =
	 * reader.readLine()) != null) { if (fila > 0) {
	 * 
	 * 
	 * //nueva forma de leer el archivo objDepositos = new CBCatalogoAgenciaModel();
	 * 
	 * int contador = 0;
	 * 
	 * @SuppressWarnings("unused") int colNum = 0; //
	 * System.out.println("VALOR LINEA = " + line);
	 * 
	 * //Iterable<String> parts = Splitter.on('|').split(line); Iterable<String>
	 * parts = Splitter.on('\t').split(line);
	 * 
	 * for (@SuppressWarnings("unused") String column : parts) { colNum++; }
	 * 
	 * for (String token : parts) {
	 * 
	 * contador++;
	 * 
	 * switch (contador) {
	 * 
	 * case 1: objDepositos.setDeposito(token.trim()); break;
	 * 
	 * // para el campo numero de cuenta case 2:
	 * objDepositos.setEstado(token.trim()); break;
	 * 
	 * } }
	 * 
	 * if(objDepositos.getEstado() != null ) { lblMensaje.
	 * setValue("El archivo que se ha cargado es incorrecto, favor revisar.");
	 * lblMensaje.setStyle("color:red;"); imgEstatus.setSrc("img/rojo.png"); return;
	 * } if(!line.trim().equals("")) { if(objDepositos.getDeposito().length()>5) {
	 * System.out.println("datos deposito en if:" + objDepositos);
	 * listaDepositos.add(objDepositos);
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.INFO, "tamano de la lista depositos en if " +listaDepositos.size()); }
	 * }
	 * 
	 * } fila++; }
	 * 
	 * 
	 * //
	 * 
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.INFO, "tamano de la lista depositos " +listaDepositos.size()); if
	 * (listaDepositos.size() > 0) { System.out.println("lista: " +
	 * listaDepositos.size()); lblMensaje.setValue("Archivo seleccionado: " +
	 * media.getName()); lblMensaje.setStyle("color:blue;");
	 * imgEstatus.setSrc("img/azul.png"); if
	 * (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
	 * chkFechaRegularizacion.setDisabled(false); // CarlosGodinez -> 30/10/2017
	 * chktipologia.setDisabled(false); // CarlosGodinez -> 30/10/2017 } } else {
	 * lblMensaje.setValue("El archivo que se ha cargado viene vacío.");
	 * lblMensaje.setStyle("color:red;"); imgEstatus.setSrc("img/rojo.png"); if
	 * (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
	 * chkFechaRegularizacion.setDisabled(true); // CarlosGodinez -> 30/10/2017
	 * chktipologia.setDisabled(true); // CarlosGodinez -> 30/10/2017 } } } else {
	 * lblMensaje.setValue("Error en formato -- Archivo seleccionado: " +
	 * media.getName()); lblMensaje.setStyle("color:red;");
	 * imgEstatus.setSrc("img/rojo.png"); } } catch (IOException e) {
	 * Messagebox.show("Ha ocurrido un error al cargar el archivo.", "ERROR",
	 * Messagebox.OK, Messagebox.ERROR);
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.SEVERE, null, e);
	 * 
	 * } catch (Exception e) {
	 * Messagebox.show("Ha ocurrido un error al cargar el archivo.", "ERROR",
	 * Messagebox.OK, Messagebox.ERROR);
	 * Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(
	 * Level.SEVERE, null, e);
	 * 
	 * } }
	 */
	public void refrescarModulo(String asociacion, String valorAsociacion, String valorAgenciaTipologia, int exitosas) {
		CBConsultaEstadoCuentasController instanciaPrincipal = new CBConsultaEstadoCuentasController();
		instanciaPrincipal = (CBConsultaEstadoCuentasController) session.getAttribute("interface");
		instanciaPrincipal.recargarConsulta(valorAsociacion, valorAgenciaTipologia);
		Messagebox.show(
				"Operación exitosa.\n\nRegistros actualizados: " + exitosas
						+ "\nEl/Los estado(s) de cuenta que acaba de "
						+ "modificar se ha(n) agregado a los registros con: \n\nTipología = " + asociacion + ".",
				"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
		asignaTipologia.onClose();
	}

	public void onClick$btnAsignar() {

		if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
			// se utiliza solo para Guatemala ya que tiene el campo fecha regularizacion
			asignarGT();
		} else {
			// se utiliza para los paises SV, NI , PA y CR.
			asignar();
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void asignar() {

		try {
			if (cmbTipologia.getSelectedItem() != null && cmbEntidad.getSelectedItem() != null
					&& (dbFecha.getValue() != null || chkFecha.isChecked() || chktipologia.isChecked())) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
				final int tipologia = Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString());
				final int entidad = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
				String banderaFecIngresos = (dbFecha.getValue() == null ? "" : sdf.format(dbFecha.getValue())); // CarlosGodinez
																												// ->
																												// 30/10/2017
				final String fechaIngresos = (chkFecha.isChecked() ? "" : banderaFecIngresos);
				final String observaciones = txtObservaciones.getText().trim();
				// final int fechaRegularizacion = (chkFechaRegularizacion.isChecked() ? 1 : 0);
				// //CarlosGodinez->30/10/2017
				final int check_tipologia = (chktipologia.isChecked() ? 1 : 0); // CarlosGodinez->30/10/2017
				if (listaDepositos.size() != 0) {
					log.debug(
							"asignar() - " + "**** Se leera un archivo de depositos ****");
					//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
							//"**** Se leera un archivo de depositos ****");

					Messagebox.show(
							"Se ha cargado un archivo de depósitos, por lo cual se asignará la tipología poliza ingresada a los registros "
									+ "que contengan los depósitos leídos del archivo cargado, ¿desea continuar con la operación?",
							"CONFIRMACION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
								public void onEvent(Event event) throws Exception {
									if (((Integer) event.getData()).intValue() == Messagebox.YES) {
										CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
										/**
										 * Se agrega fecha regularizacion -> 30/10/2017
										 */
										int resultado = objeDAO.asignarTipologia(tipologia, entidad, fechaIngresos,
												observaciones, idEstadoCuenta, listaID, listaDepositos, usuario,
												check_tipologia /* , fechaRegularizacion */);
										if (resultado > 0) {
											refrescarModulo(cmbTipologia.getSelectedItem().getLabel().trim(),
													cmbTipologia.getSelectedItem().getValue().toString(),
													cmbEntidad.getSelectedItem().getValue().toString(), resultado);
										} else if (resultado == 0) {
											Messagebox.show(
													"No se pudo actualizar ningun registro, revise el archivo de depositos",
													"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
										}
									}
								}
							});
				} else {
					log.debug(
							"asignar() - " + "**** Actualizacion normal de tipoligia sin depositos ****");
					//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
							//"**** Actualizacion normal de tipoligia sin depositos ****");

					int resultado = objeDAO.asignarTipologia(tipologia, entidad, fechaIngresos, observaciones,
							idEstadoCuenta, listaID, listaDepositos, usuario, check_tipologia /* , 0 */);
					if (resultado > 0) {
						refrescarModulo(cmbTipologia.getSelectedItem().getLabel().trim(),
								cmbTipologia.getSelectedItem().getValue().toString(),
								cmbEntidad.getSelectedItem().getValue().toString(), resultado);
					} else if (resultado == 0) {
						Messagebox.show("No se pudo completar la operación.", "ATENCION", Messagebox.OK,
								Messagebox.EXCLAMATION);
					}
				}
			} else {
				Messagebox.show("Debe completar todos los campos antes de realizar la operacion.", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			log.error("asignar() - Error ", e);
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void asignarGT() {

		try {
			if (cmbTipologia.getSelectedItem() != null && cmbEntidad.getSelectedItem() != null
					&& (dbFecha.getValue() != null || chkFecha.isChecked() || chkFechaRegularizacion.isChecked()
							|| chktipologia.isChecked())) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
				final int tipologia = Integer.parseInt(cmbTipologia.getSelectedItem().getValue().toString());
				final int entidad = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
				String banderaFecIngresos = (dbFecha.getValue() == null ? "" : sdf.format(dbFecha.getValue())); // CarlosGodinez
																												// ->
																												// 30/10/2017
				final String fechaIngresos = (chkFecha.isChecked() ? "" : banderaFecIngresos);
				final String observaciones = txtObservaciones.getText().trim();
				final int fechaRegularizacion = (chkFechaRegularizacion.isChecked() ? 1 : 0); // CarlosGodinez->30/10/2017
				final int check_tipologia = (chktipologia.isChecked() ? 1 : 0); // CarlosGodinez->30/10/2017,
																				// check_tipologia ||
																				// chktipologia.isChecked()))
				if (listaDepositos.size() != 0) {
					log.debug(
							"asignarGT() - " + "**** Se leera un archivo de depositos ****");
					//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
						//	"**** Se leera un archivo de depositos ****");
					Messagebox.show(
							"Se ha cargado un archivo de depósitos, por lo cual se asignará la tipología poliza ingresada a los registros "
									+ "que contengan los depósitos leídos del archivo cargado, ¿desea continuar con la operación?",
							"CONFIRMACION", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
								public void onEvent(Event event) throws Exception {
									if (((Integer) event.getData()).intValue() == Messagebox.YES) {
										CBConsultaEstadoCuentasDAO objeDAO = new CBConsultaEstadoCuentasDAO();
										/**
										 * Se agrega fecha regularizacion -> 30/10/2017
										 */
										int resultado = objeDAO.asignarTipologiaGT(tipologia, entidad, fechaIngresos,
												observaciones, idEstadoCuenta, listaID, listaDepositos, usuario,
												fechaRegularizacion, check_tipologia, paramsLlenaListbox);
										if (resultado > 0) {
											refrescarModulo(cmbTipologia.getSelectedItem().getLabel().trim(),
													cmbTipologia.getSelectedItem().getValue().toString(),
													cmbEntidad.getSelectedItem().getValue().toString(), resultado);
										} else if (resultado == 0) {
											Messagebox.show(
													"No se pudo actualizar ningun registro, revise el archivo de depositos",
													"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
										}
									}
								}
							});
				} else {
					log.debug(
							"asignarGT() - " + "**** Actualizacion normal de tipoligia sin depositos ****");
					//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.INFO,
							//"**** Actualizacion normal de tipoligia sin depositos ****");
					int resultado = objeDAO.asignarTipologiaGT(tipologia, entidad, fechaIngresos, observaciones,
							idEstadoCuenta, listaID, listaDepositos, usuario, 0, check_tipologia, null);
					if (resultado > 0) {
						refrescarModulo(cmbTipologia.getSelectedItem().getLabel().trim(),
								cmbTipologia.getSelectedItem().getValue().toString(),
								cmbEntidad.getSelectedItem().getValue().toString(), resultado);
					} else if (resultado == 0) {
						Messagebox.show("No se pudo completar la operación.", "ATENCION", Messagebox.OK,
								Messagebox.EXCLAMATION);
					}
				}
			} else {
				Messagebox.show("Debe completar todos los campos antes de realizar la operacion.", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			log.error("asignarGT() - Error ", e);
		//	Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}

	}

	String observaciones;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnDesasociar() {
		if (txtObservaciones.getText().trim() == null || txtObservaciones.getText().trim().equals("")) {
			Messagebox.show(
					"El campo observaciones es necesario para desasociar tipologia, ingrese el motivo del cambio.",
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

									if (objeDAO.desasociarTipologia(idEstadoCuenta, usuario, observaciones)) {
										log.debug(
												"onClick$btnDesasociar() - " + "Entra a desasociar tipologias individual ");
										//Logger.getLogger(CBConsultaEstadoCuentasController.class.getName())
												//.log(Level.INFO, "Entra a desasociar tipologias individual ");
										refrescarModulo("(No asignada)", "(No asignada)", "(No asignada)", 1);

									}

								}
							}
						});
			} catch (Exception e) {
				//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
				log.error("onClick$btnDesasociar() - Error ", e);
				Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			}

		}

	}

	/**
	 * Validamos si la cadena ingresada es numerica o no
	 */
	public boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena.trim());
			System.out.println("numero valido " + cadena);
			return true;

		} catch (NumberFormatException nfe) {
			System.out.println("numero invalido " + cadena);
			return false;
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
			log.error("changeDate() - Error ", e);
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
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
			log.error("isDate() - Error ", e);
			//Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		} catch (NullPointerException e) {
			log.error("isDate() - Error ", e);
		//	Logger.getLogger(CBEstadoCuentasTipologiaController.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
	}

	public void cleanCombo(Combobox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}
}
