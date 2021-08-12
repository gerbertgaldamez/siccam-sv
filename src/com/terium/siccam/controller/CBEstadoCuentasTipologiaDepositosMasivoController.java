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
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.terium.siccam.controller.CBEstadoCuentasTipologiaDepositosMasivoController;
import com.google.common.base.Splitter;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBConsultaEstadoCuentasDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBEstadoCuentasModel;

public class CBEstadoCuentasTipologiaDepositosMasivoController extends ControladorBase {

	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();
	private static final long serialVersionUID = 9176164927878418930L;
	// Propiedades agregadas por Carlos Godínez - Qitcorp 02/03/2017

	private Textbox txtObservaciones;

	private int idEstadoCuenta;
	private boolean tipologiaMasiva;
	private Button btnDesasociar;
	Window asignaTipologia;
	// Juankrlos --> 14/07/2017

	// Lista que trae los ID de estados de cuenta consultados para asignar tipologia
	// masivamente
	private List<Integer> listaID = new ArrayList<Integer>();
	// Listas para llenar combobox

	// Agregado por Carlos Godinez - 13/06/2017 - Subida de archivo de depositos
	private Label lblMensaje;
	private Image imgEstatus;
	private Media media;
	// Lista que trae los ID de estados de cuenta consultados para asignar tipologia
	// masivamente
	// Comment by Juankrlos 13/07/2017
	private List<CBCatalogoAgenciaModel> listaDepositos = new ArrayList<CBCatalogoAgenciaModel>();
	private String usuario; // CarlosGodinez -> 10/10/2017
	private CBEstadoCuentasModel paramsLlenaListbox = null;
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		usuario = obtenerUsuario().getUsuario();
		btnDesasociar.setVisible(false);

		try {
		Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
				"Bandera asociar tipología de manera masiva archivo depositos ");
		paramsLlenaListbox = (CBEstadoCuentasModel) misession.getAttribute("paramsListbox");
		}catch (Exception e) {
			//Messagebox.show("Ha ocurrido un error al obtener la session.", "ERROR", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.SEVERE, null,
					e);
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					"error al obtener la session en metodo principal");
		}
		lblMensaje.setValue("No se ha seleccionado ningun archivo de depósitos");
		imgEstatus.setSrc("img/negro.png");
		Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
				"Bandera asignar tipología de manera masiva = " + tipologiaMasiva);

	}

	/**
	 * @
	 */

	public void onUpload$btnSubirDepositos(UploadEvent event) {
		try {
			if (listaDepositos != null && listaDepositos.size() > 0) {
				listaDepositos.clear();
			}
			// CBEstadoCuentasModel objSociedad = null;
			media = event.getMedia();

			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					"-----File Information-----");
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					"name: " + media.getName());
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					"contentType: " + media.getContentType());
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
					"format: " + media.getFormat());

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

						if (isNumeric(objDepositos.getTipologia())) {
							if(objDepositos.getDeposito().length() >5) {
							listaDepositos.add(objDepositos);
							}

						}
					}
					fila++;
				}
				if (listaDepositos.size() > 0) {
					misession.setAttribute("sesionDepositosMasivos", objDepositos);
					lblMensaje.setValue("Archivo seleccionado: " + media.getName());
					lblMensaje.setStyle("color:blue;");
					imgEstatus.setSrc("img/azul.png");

				} else {
					lblMensaje.setValue("El archivo que se ha cargado viene vacío o datos invalidos, favor revisar.");
					lblMensaje.setStyle("color:red;");
					imgEstatus.setSrc("img/rojo.png");

				}
			} else {
				lblMensaje.setValue("Error en formato -- Archivo seleccionado: " + media.getName());
				lblMensaje.setStyle("color:red;");
				imgEstatus.setSrc("img/rojo.png");
			}
		} catch (IOException e) {
			Messagebox.show("Ha ocurrido un error al cargar el archivo.", "ERROR", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.SEVERE, null,
					e);

		}
	}

	public void refrescarModulo(String valorAsociacion, String valorAgenciaTipologia, int exitosas) {
		CBConsultaEstadoCuentasController instanciaPrincipal = new CBConsultaEstadoCuentasController();
		instanciaPrincipal = (CBConsultaEstadoCuentasController) misession.getAttribute("interface");
		System.out.println("param " + valorAsociacion + valorAgenciaTipologia + exitosas);
		instanciaPrincipal.recargarConsulta(valorAsociacion, valorAgenciaTipologia);
		
		Messagebox.show(
				"Operación exitosa.\n\nRegistros actualizados en estado de cuenta: " + exitosas,
				"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
		asignaTipologia.onClose();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnAsignar() {

		try {

			if (listaDepositos.size() != 0) {
				Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.INFO,
						"**** Se leera un archivo de depositos ****");

				final String observaciones = txtObservaciones.getText().trim();

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
									int resultado = objeDAO.asignarTipologiaMasivaDepositos2(observaciones,
											idEstadoCuenta, listaID, listaDepositos,
											usuario /* , fechaRegularizacion */);
									if (resultado > 0) {
										if(paramsLlenaListbox.getTipologia() == null || paramsLlenaListbox.getTipologia().equals("")) {
											paramsLlenaListbox.setTipologia("Todas");
										}
										refrescarModulo(paramsLlenaListbox.getTipologia(),"Todas", resultado);
									} else if (resultado == 0) {
										Messagebox.show(
												"No se pudo actualizar ningun registro, revise el archivo de depositos",
												"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
									}
								}
							}
						});
			} else {
				Messagebox.show("No se pudo actualizar ningun registro, revise el archivo de depositos", "ATENCION",
						Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} catch (Exception e) {
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.SEVERE, null,
					e);
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
		}

	}

	/**
	 * Validamos si la cadena ingresada es numerica o no
	 */
	public boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena.trim());
			// System.out.println("numero valido " + cadena);
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
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.SEVERE, null,
					e);
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
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.SEVERE, null,
					e);
			return false;
		} catch (NullPointerException e) {
			Logger.getLogger(CBEstadoCuentasTipologiaDepositosMasivoController.class.getName()).log(Level.SEVERE, null,
					e);
			return false;
		}
	}

	public void cleanCombo(Combobox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}
}
