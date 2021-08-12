package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.SocketException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.net.ftp.FTPClient;
import org.zkoss.zk.ui.Component;
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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.terium.siccam.dao.CBCatalogoAgenciaDAO;
import com.terium.siccam.dao.CBConciliacionDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBResumenDiarioConciliacionModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.model.ListaCombo;
import com.terium.siccam.utils.UtilidadesReportes;

/**
 * @author Freddy Ayala to terium.com
 */
public class ConciliacionController extends MenuController {

	private static final long serialVersionUID = 1L;

	// Variables Zul
	Listbox lbxConciliacion;
	Combobox cmbAgencia;
	Datebox dtbDia;
	Datebox dtbDia2;
	Combobox cmbEstado;
	Combobox cmbTipo;
	Button btnGeneraReporte;
	Textbox txtCodigoColector; //CarlosGodinez -> 07/08/2018
	
	@SuppressWarnings("unused")
	private CBCatalogoBancoModel bancoCombo = new CBCatalogoBancoModel();
	private List<CBParametrosGeneralesModel> listParamTipo = new ArrayList<CBParametrosGeneralesModel>();

	// Varibales globales
	// Lista variante
	List<CBResumenDiarioConciliacionModel> lst;
	// Lista que contiene todos los registros con los calculos hechos
	List<CBResumenDiarioConciliacionModel> lstToda;
	// Lista que contiene todos los registros sin los calculos
	List<CBResumenDiarioConciliacionModel> listaOriginal;
	Date fecha = null;
	Date fecha2 = null;
	DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
	String dia = null;
	String dia2 = null;

	// Instancias DAO
	CBConciliacionDAO conciliacionDao = new CBConciliacionDAO();	
	
	public void doAfterCompose(Component comp) {
		super.doAfterCompose(comp);
		cmbEstado.setText("Todos");
		cmbAgencia.setText("Todas");
		cmbTipo.setText("Todos");
		
		cargarComboAgencia();
		llenaComboTipo();
		cargarComboEstado();
		
		fecha = new Date();
		DateFormat fechaFormato = new SimpleDateFormat("dd/MM/yyyy");
		dia = fechaFormato.format(fecha);
		dtbDia2.setText(dia);
	}

	/**
	 * Llenado de combo tipo
	 */
	public void llenaComboTipo() {
		CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
		listParamTipo = objeDAO.obtenerParamConvenios();
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
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"- Llena combo tipo");
		} else {
			Messagebox.show("Error al cargar la configuracion de tipos de conciliacion", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	/**
	 * Llenado de combo de entidades
	 * */
	public void cargarComboAgencia() {
		try {
			CBCatalogoAgenciaDAO cba = new CBCatalogoAgenciaDAO();
			List<CBCatalogoAgenciaModel> lst = cba.obtieneListadoAgencias(null, null, null, null);
			Iterator<CBCatalogoAgenciaModel> iLst = lst.iterator();
			CBCatalogoAgenciaModel obj = null;
			Comboitem item = null;
			while (iLst.hasNext()) {
				obj = iLst.next();
				item = new Comboitem();
				item.setLabel(obj.getNombre());
				item.setValue(obj.getcBCatalogoAgenciaId());
				item.setParent(cmbAgencia);
			}
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"- Llena combo de entidades");
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * @author Omar Gomez -QIT 18/07/2017 cargar combobox de estado
	 */
	public void cargarComboEstado() {
		try {
			CBParametrosGeneralesDAO dao = new CBParametrosGeneralesDAO();
			List<CBParametrosGeneralesModel> list = dao
					.obtenerListaTipoAgrupacion(CBParametrosGeneralesDAO.S_OBTENER_ESTADO_CONCILIACION);
			Iterator<CBParametrosGeneralesModel> iterator = list.iterator();
			CBParametrosGeneralesModel model = null;
			Comboitem item = null;
			while (iterator.hasNext()) {
				model = iterator.next();
				item = new Comboitem();
				item.setLabel(model.getObjeto());
				item.setValue(model.getValorObjeto1());
				item.setParent(cmbEstado);
			}
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"- Llena combo estado");
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void listarTodasConciliaciones(String agencia, String tipo, String estado, String codigoColector) {
		try {
			dia = fechaFormato.format(dtbDia.getValue());
			dia2 = fechaFormato.format(dtbDia2.getValue());
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"Fecha inicial para consulta " + dia);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"Fecha final para consulta " + dia2);
			CBResumenDiarioConciliacionModel objModel = new CBResumenDiarioConciliacionModel();
			objModel.setFechaInicial(dia);
			objModel.setFechaFinal(dia2);
			objModel.setIdAgencia(agencia);
			objModel.setTipo(tipo);
			objModel.setEstado(estado);
			objModel.setCodigoColector(codigoColector);
			lst = conciliacionDao.obtenerCBConciliaciones(objModel);
	if(lst.size() <= 0 ) {
				
				Messagebox.show("No se encontraron registros, para los filtros ingresados.", "ATENCION", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}
			// clonamos la lista
			if (lst != null) {
				listaOriginal = new ArrayList<CBResumenDiarioConciliacionModel>();
				listaOriginal = (List<CBResumenDiarioConciliacionModel>) ((ArrayList) lst).clone();
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void listarConciliaciones() {

		try {
			limpiarListboxYPaginas(lbxConciliacion);
			
			// lstToda = new ArrayList<CBResumenDiarioConciliacionModel>();
			try {
				if (lst.size() > 0) {
					Iterator<CBResumenDiarioConciliacionModel> ilst = lst.iterator();
					CBResumenDiarioConciliacionModel adr = null;
					Listcell cell = null;
					Listitem fila = null;
					while (ilst.hasNext()) {
						adr = ilst.next();
						fila = new Listitem();

						/** Agrupacion principal **/
						// DIA
						cell = new Listcell();
						cell.setLabel(fechaFormato.format(adr.getDia()));
						cell.setParent(fila);

						// NOMBRE
						cell = new Listcell();
						cell.setLabel(adr.getNombre());
						cell.setParent(fila);

						//CODIGO COLECTOR 
						//CarlosGodinez -> 07/08/2018
						cell = new Listcell();
						cell.setLabel(adr.getCodigoColector());
						cell.setParent(fila);
						
						// TIPO
						cell = new Listcell();
						cell.setLabel(adr.getTipo());
						cell.setParent(fila);
						
						/** Agrupacion BANCO **/
						// TRANAS BANCO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getTransBanco().toString());
						cell.setParent(fila);
						
						// PAGOS BANCO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getConfrontaBanco()).toString());
						cell.setParent(fila);
						
						// MANUAL BANCO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getManualBanco()));
						cell.setParent(fila);
						
						// REAL BANCO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getReal_b()));
						cell.setParent(fila);
						
						// Pendiente Banco
						adr = calcularPendienteConciliarBanco(adr);
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getPendienteBanco()));
						cell.setParent(fila);
						
						/** Agrupacion SISTEMA COMERCIAL **/
						// TRANS TELEFONICA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getTransTelefonica().toString());
						cell.setParent(fila);

						// PAGOS TELEFONICA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getPagosTelefonica()));
						cell.setParent(fila);
						
						// MANUAL TELEFONICA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getManualTelefonica()));
						cell.setParent(fila);
						
						// REAL TELEFONICA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getReal_t()));
						cell.setParent(fila);

						// Pendiente Telefonica
						adr = calcularPendienteConciliarTelefonica(adr);
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getPendienteTelefonica()));
						cell.setParent(fila);
						
						/** Agrupacion RESULTADOS **/
						
						 //estaba comentariada
						//Transacciones Telca
						 cell = new Listcell();
						 cell.setStyle("text-align: right");
						 cell.setLabel(convertirADecimal(adr.getConciliadas()));
						 cell.setParent(fila);
						
						// CONCILIADAS
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getConciliadas().toString());
						cell.setParent(fila);

						// DIFERENCIA TRANSACCION
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(adr.getDifTransaccion().toString());
						cell.setParent(fila);
                       

						// AUTOMATICA
						cell = new Listcell();
						cell.setStyle("text-align: right");
						cell.setLabel(convertirADecimal(adr.getAutomatica()));
						cell.setParent(fila);

						/**
						 * Agrega Carlos Godinez -> 07/07/2017
						 * */
						// EFECTO NETO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						//BigDecimal noConciliadoBanco = new BigDecimal(convertirADecimal(adr.getPendienteBanco()));
						//BigDecimal noConciliadoTelca = new BigDecimal(convertirADecimal(adr.getPendienteTelefonica()));
						BigDecimal result = null;
						result = adr.getPendienteTelefonica().subtract(adr.getPendienteBanco());
						cell.setLabel(convertirADecimal(result));
						cell.setParent(fila);
						
						/**
						 * FIN Agrega Carlos Godinez -> 07/07/2017
						 * */
						
						// ESTADO
						cell = new Listcell();
						cell.setStyle("text-align: right");
						// Si pendiente banco == 0 && pendiente telefonica == 0
						// ; verde
						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) == 0)) {
							cell.setImage("/img/cerrado.png");
							adr.setEstado("1");
						}
						// Si pendiente banco > 0 || pendiente telefonica > 0;
						// rojo
						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) > 0)
								|| (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) > 0)) {
							cell.setImage("/img/rojo.png");
							adr.setEstado("2");
						}
						
						//esta validacion esta para sv, ni y pa, para GT y CR no esta se realizan pruebas para ver si se debe tomar en cuenta.
						//Agregado por Carlos Godinez -> 05/09/2017
						if (adr.getConfrontaBanco().compareTo(adr.getPagosTelefonica()) > 0){
							cell.setImage("/img/rojo.png");
							adr.setEstado("2");
						}
						//FIN Agregado por Carlos Godinez -> 05/09/2017
						
						// Si manual telefonica > 0 && pendiente banco == 0 &&
						// pendiente telefonica == 0 ; amarillo
						if ((adr.getManualTelefonica().compareTo(
								BigDecimal.ZERO) > 0)
								&& (adr.getPendienteBanco().compareTo(
										BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) == 0)) {
							cell.setImage("/img/amarillo.png");
							adr.setEstado("3");
						}
						// Si manual banco > 0 && pendiente banco == 0 &&
						// pendiente telefonica == 0 ; amarillo
						if ((adr.getManualBanco().compareTo(BigDecimal.ZERO) > 0)
								&& (adr.getPendienteBanco().compareTo(
										BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) == 0)) {
							cell.setImage("/img/amarillo.png");
							adr.setEstado("3");
						}
						// Si pagos telefonica = 0 y != pendiente banco ; azul
						if ((adr.getPagosTelefonica()
								.compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getConfrontaBanco().compareTo(
										BigDecimal.ZERO) != 0)) {
							cell.setImage("/img/azul.png");
							adr.setEstado("4");
						}
						// Si pagos banco = 0 y != pendiente telefonica ; negro
						if ((adr.getPagosTelefonica()
								.compareTo(BigDecimal.ZERO) != 0)
								&& (adr.getConfrontaBanco().compareTo(
										BigDecimal.ZERO) == 0)) {
							cell.setImage("/img/negro.png");
							adr.setEstado("5");
						}

						cell.setParent(fila);
						

						fila.setValue(adr);
						// lstToda.add(adr);

						fila.addEventListener(Events.ON_DOUBLE_CLICK, 
								new EventListener() {
									public void onEvent(Event event) {
										abrirConciliacionDetalle();
									}
								});

						fila.setParent(lbxConciliacion);

					}
				}
			} catch (Exception e) {
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	public void listarConciliacionesAux(List<CBResumenDiarioConciliacionModel> lista) {

		lstToda = new ArrayList<CBResumenDiarioConciliacionModel>();
		try {
			dia = fechaFormato.format(fecha);

			Iterator<CBResumenDiarioConciliacionModel> ilst = lista.iterator();
			try {
				if (lista != null && lista.size() > 0) {

					CBResumenDiarioConciliacionModel adr = null;

					while (ilst.hasNext()) {
						adr = ilst.next();

						adr = calcularPendienteConciliarBanco(adr);

						adr = calcularPendienteConciliarTelefonica(adr);

						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) == 0)) {

							adr.setEstado("1");
						}
						if ((adr.getPendienteBanco().compareTo(BigDecimal.ZERO) > 0)
								|| (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) > 0)) {

							adr.setEstado("2");
						}
						if ((adr.getManualTelefonica().compareTo(
								BigDecimal.ZERO) > 0)
								&& (adr.getPendienteBanco().compareTo(
										BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) == 0)) {

							adr.setEstado("3");
						}
						if ((adr.getManualBanco().compareTo(BigDecimal.ZERO) > 0)
								&& (adr.getPendienteBanco().compareTo(
										BigDecimal.ZERO) == 0)
								&& (adr.getPendienteTelefonica().compareTo(
										BigDecimal.ZERO) == 0)) {
							adr.setEstado("3");
						}

						if ((adr.getPagosTelefonica()
								.compareTo(BigDecimal.ZERO) == 0)
								&& (adr.getConfrontaBanco().compareTo(
										BigDecimal.ZERO) != 0)) {

							adr.setEstado("4");
						}

						if ((adr.getPagosTelefonica()
								.compareTo(BigDecimal.ZERO) != 0)
								&& (adr.getConfrontaBanco().compareTo(
										BigDecimal.ZERO) == 0)) {

							adr.setEstado("5");
						}

						lstToda.add(adr);

					}
				}
			} catch (Exception e) {
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	@SuppressWarnings("deprecation")
	public void abrirConciliacionDetalle() {
		CBResumenDiarioConciliacionModel con = null;
		String num = null;
		try {

			if (lbxConciliacion.getSelectedItem() != null) {
				con = lbxConciliacion.getSelectedItem().getValue();
				session.setAttribute("conciliacion", con);
				session.setAttribute("ResumenDiarioConciliacion", true);
				num = con.getDia().getDay() + "-" + con.getDia().getMonth()
						+ "- Agen: " + con.getIdAgencia() + " - "
						+ con.getTipo();
				super.llamarConciliacionDetalle(num);
			}

		} catch (Exception e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private CBResumenDiarioConciliacionModel calcularPendienteConciliarBanco(CBResumenDiarioConciliacionModel obj) {
		// String label = null;
		BigDecimal pendienteConciliarBanco = null;
		BigDecimal pagoBanco = obj.getConfrontaBanco();
		BigDecimal manualBanco = obj.getManualBanco();
		BigDecimal automatica = obj.getAutomatica();

		// pendienteBanco =
		pendienteConciliarBanco = pagoBanco.subtract(automatica);
		pendienteConciliarBanco = pendienteConciliarBanco.subtract(manualBanco);

		// pendienteConciliarBanco = confrontaBanco.subtract(automatica);

		obj.setPendienteBanco(pendienteConciliarBanco);
		// label = pendienteConciliarBanco.toBigInteger().toString();

		return obj;

	}

	private CBResumenDiarioConciliacionModel calcularPendienteConciliarTelefonica(
			CBResumenDiarioConciliacionModel obj) {
		// String label = null;
		BigDecimal conciliarTelefonica;
		BigDecimal automatica = obj.getAutomatica();
		BigDecimal manualTelefonica = obj.getManualTelefonica();
		BigDecimal pagoTelefonica = obj.getPagosTelefonica();

		conciliarTelefonica = pagoTelefonica.subtract(automatica);
		conciliarTelefonica = conciliarTelefonica.subtract(manualTelefonica);
		// conciliarTelefonica = pagosTelefonica.subtract(automatica);

		obj.setPendienteTelefonica(conciliarTelefonica);
		// label = conciliarTelefonica.toBigInteger().toString();
		//System.out.println("Pendiente Telefonica: " + obj.getPendienteTelefonica());
		
		return obj;
	}

	public void iteradorConciliacionEstado(String estado) {
			// listarConciliacionesAux();
			
			Iterator<CBResumenDiarioConciliacionModel> ilst = lstToda.iterator();
		if (lstToda.size() > 0) {
				CBResumenDiarioConciliacionModel adr = null;
				while (ilst.hasNext()) {
					adr = ilst.next();
					if (adr.getEstado().equals(estado)) {
						lst.add(adr);
					}
			}
		}

	}
	
	public void iteradorConciliacionAgencia(CBCatalogoAgenciaModel agen) {

			Iterator<CBResumenDiarioConciliacionModel> ilst = lstToda.iterator();
		if (lstToda.size() > 0) {
				CBResumenDiarioConciliacionModel adr = null;
				while (ilst.hasNext()) {
					adr = ilst.next();
					if (adr.getIdAgencia().equals(agen.getcBCatalogoAgenciaId())) {
						lst.add(adr);
				}
			}
		}

	}

	public void iteradorConciliacionTipo(String tip) {

		Iterator<CBResumenDiarioConciliacionModel> ilst = lstToda.iterator();
		if (lstToda.size() > 0) {
			CBResumenDiarioConciliacionModel adr = null;
			while (ilst.hasNext()) {
				adr = ilst.next();
				if (adr.getTipo().equals(tip)) {
					lst.add(adr);
				}
			}
		}

	}

	public void iteradorConciliacionDia(String dia) {
		// System.out.println("entra al conciliaiconDia");
			Iterator<CBResumenDiarioConciliacionModel> ilst = lstToda.iterator();
		if (lstToda.size() > 0) {
				CBResumenDiarioConciliacionModel adr = null;
				while (ilst.hasNext()) {
					adr = ilst.next();
					// System.out.println(adr.getDia().toString()+"=="+dia);
					if (adr.getDia().toString().equals(dia)) {
						lst.add(adr);
				}
			}
		}

	}
	
	/**
	 * Filtros de busqueda para conciliaciones
	 * */
	public void onClick$btnBuscar() {
		Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, "Combo agencia: " + cmbAgencia.getText()
				+ " estado: " + cmbEstado.getText() + " cmbTipo: " + cmbTipo.getText());
		if (dtbDia.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dtbDia2.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha fin antes de consultar datos.", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		
		/**
		 *  Agregado por Nicolas Bermudez - Qitcorp 19/09/2017 
		 *  Parseo de fechas
		 *  */
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = format.parse(dtbDia.getText());
			date2 = format.parse(dtbDia2.getText());
		} catch (ParseException e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}
		/**
		 * Validacion de fechas
		 * */
		if (date1.compareTo(date2) > 0) {
			Messagebox.show("El valor de fecha inicial no puede ser mayor al valor de fecha final.", "ATENCION",
					Messagebox.OK, Messagebox.EXCLAMATION);
			dtbDia.focus();
			return;
		}

		/**
		 * Evaluacion de parametros
		 * */
		
		// Limpiamos listas anteriores
		if (lst != null) {
			lst.clear();
		}

		String agencia = "";
		String tipo = "";
		String estado = "";
		
		if (!"Todas".equals(cmbAgencia.getText())) {
			agencia = cmbAgencia.getSelectedItem().getValue().toString().trim(); 
		}
		if (!"Todos".equals(cmbTipo.getText())) {
			tipo = cmbTipo.getSelectedItem().getValue().toString().trim(); 
		}
		
		if ("Conciliado Automaticamente".equals(cmbEstado.getText())) {
			estado = "1";
		}
		if ("Pendiente Conciliar".equals(cmbEstado.getText())) {
			estado = "2";
		}
		if ("Conciliado Manualmente".equals(cmbEstado.getText())) {
			estado = "3";
		}
		if ("Pendiente Pagos".equals(cmbEstado.getText())) {
			estado = "4";
		}
		if ("Pendiente Confronta".equals(cmbEstado.getText())) {
			estado = "5";
		}

		/**
		 * Agrega CarlosGodinez -> 07/08/2018
		 * Se agregago codigo de colector como parametro
		 * 
		 * */
		String codigoColector = "";
		if (txtCodigoColector.getText() == null || "".equals(txtCodigoColector.getText().trim())) {
			codigoColector = "";
		} else {
			codigoColector = txtCodigoColector.getText().trim();
		}
		/**
		 * FIN CarlosGodinez -> 10/08/2018
		 * */
		listarTodasConciliaciones(agencia, tipo, estado, codigoColector);
		listarConciliaciones();
		// listarConciliacionesAux(listaOriginal);
		Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO,
				"FILTROS: agencia = " + agencia + " tipo = " + tipo + " estado = " + estado);

	}

	public void limpiarListboxYPaginas(Listbox componente) {
			if (componente != null) {
				componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
					componente.setMold("paging");
					componente.setAutopaging(true);

			}
		}
	}

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
		if (numConv.compareTo(BigDecimal.ZERO) == -1) {
			// si es numero negativo lo pasamos a positivo
			numConv = numConv.negate();
		}
		return numero;
	}

	// ********** AGREGO BENJAMIN ************
	// ********** 30/09/2013
	
	
	/**
	 * Modificado Omar Gomez -QIT- 25/07/2017
	 */
	public void onClick$btnGeneraReporte() {
		Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
				"Comienza a generar el reporte...");
			
			if (dtbDia.getValue() == null) {	
				Messagebox.show(
						"Debe de seleccionar un rango de fecha para el reporte.",
						"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
				
			} else if (dtbDia.getValue().after(dtbDia2.getValue())) {
				Messagebox.show("La fecha desde debe ser menor a la fecha hasta.",
						"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
				
			} else {
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
						"fecha: " + fechaFormato.format(dtbDia.getValue()));
				
				generarTxtConciliaciones();
				
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
						"finaliza de generar el reporte...");
			}

	}

	// generaciond reporte

	private String QRY = "SELECT dia, " + "nombre, " + "tipo, "
			+ "trans_telefonica transTelefonica, " + "trans_banco transBanco, "
			+ "conciliadas, " + "dif_trans difTransaccion, "
			+ "pagos_telefonica pagosTelefonica, "
			+ "confronta_banco confrontaBanco, " + "automatica, "
			+ "manual_t manualTelefonica, " + "manual_b manualBanco, "
			+ "pagos_telefonica - automatica - manual_t pendienteTelefonica, "
			+ "confronta_banco  - automatica - manual_b pendienteBanco, real_b, real_t  "
			+ "FROM cb_conciliacion_vw where 1 = 1 ";

	// generar la consulta con sus filtros
	// ya no es usado por generarTxtConciliaciones
	/*
	public List<CBResumenDiarioConciliacionModel> generaConsultaReporte() {
		
		List<CBResumenDiarioConciliacionModel> listado = new ArrayList<CBResumenDiarioConciliacionModel>();

		String fechaDesde = fechaFormato.format(dtbDia.getValue());
		String fechaHasta = fechaFormato.format(dtbDia2.getValue());
		
		String Where = "";

		Where += " and dia >= to_date('" + fechaDesde + "', 'dd/MM/yyyy')"
				+ " and dia <= to_date('" + fechaHasta + "', 'dd/MM/yyyy') ";

		if (!"Todas".equals(cmbAgencia.getText().trim())) {
			CBCatalogoAgenciaModel agen = cmbAgencia.getSelectedItem()
					.getValue();

			String idAgencia = agen.getcBCatalogoAgenciaId();
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"agencia: " + idAgencia);
			Where += " and cbcatalogoagenciaid = '" + idAgencia + "' ";

		}

		if (!"Todos".equals(cmbTipo.getText().trim())) {
			String tipo = cmbTipo.getSelectedItem().getLabel();
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"tipo: " + tipo);
			Where += " and tipo = '" + tipo + "' ";
		}

		if (!"Todos".equals(cmbEstado.getText().trim())) {
			String op = cmbEstado.getText();

			if ("Conciliado Automaticamente".equals(op)) {
				Where += " and pagos_telefonica = confronta_banco ";
			}
			if ("Pendiente Conciliar".equals(op)) {
				Where += " AND pagos_telefonica >0 and confronta_banco > 0  "
						+ " and ( confronta_banco-automatica-manual_b > 0 OR pagos_telefonica-automatica-manual_t > 0 )";
			}
			if ("Conciliado Manualmente".equals(op)) {
				Where += " and (confronta_banco-automatica-manual_b = 0 and manual_t > 0) or (pagos_telefonica-automatica-manual_t=0 and manual_b > 0) ";
			}
			if ("Pendiente Pagos".equals(op)) {
				Where += " and pagos_telefonica = 0 ";
			}
			if ("Pendiente Confronta".equals(op)) {
				Where += " and confronta_banco = 0 ";
			}

		}


		Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
				"Query reporte conciliaciones: " + QRY + Where);
		try {
			Connection con = obtenerDtsPromo().getConnection();
			try {
				QueryRunner qr = new QueryRunner();
				BeanListHandler<CBResumenDiarioConciliacionModel> bhl = new BeanListHandler<CBResumenDiarioConciliacionModel>(
						CBResumenDiarioConciliacionModel.class);
				listado = qr.query(con, QRY + Where, bhl, new Object[] {});
			} finally {
				if(con != null)
				con.close();
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}

		return listado;
	}
	*/
	
	/**
	 * Modificado Omar Gomez - QIT - 25/07/2017
	 */
	
	public void generarTxtConciliaciones() {
		BufferedWriter bw = null;
		try {
			if(lst != null)
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
						"lst size "+lst.size());
			
			if(lst != null && lst.size() > 0){
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO,
						"Generando reporte obtenido en la consulta...");
				
				//String fechaDesde = fechaFormato.format(dtbDia.getValue());
				//String fechaHasta = fechaFormato.format(dtbDia2.getValue());
				
				Date fecha = new Date();
				//String fechaDos = fechaFormato.format(fecha);
				
				//HttpSession session = (HttpSession) Sessions.getCurrent()
					//	.getNativeSession();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
				
				// preparando lst como Iterator
				Iterator<CBResumenDiarioConciliacionModel> iterator = lst.iterator();
				
				//creando reportes .csv
			//encabezado
			String encabezado = "DIA|AGENCIA|CODIGO COLECTOR|TIPO|TRANSACCIONES BANCO|PAGOS BANCO (CANTIDAD A FACTURAR)|CONCILIACION MANUAL BANCO|"
					+"NO CONCILIADO BANCO|TRANSACCIONES TELEFONICA|PAGOS TELEFONICA|"
					+"CONCILIACION MANUAL TELCA|NO CONCILIADO TELEFONICA|TRANSACCIONES CONCILIADAS|DIFERENCIA TRANSACCIONES|"
					+"AUTOMATICA|EFECTO NETO|REAL BANCO|REAL TELEFONICA\n";
				
				File file = new File("reporte_conciliaciones"+sdf.format(fecha)+".csv");
				
				
					/*if(!file.exists())
						file.createNewFile();*/
					
					bw = new BufferedWriter(new FileWriter(file));
					bw.write(encabezado);
				
					CBResumenDiarioConciliacionModel c = null;
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					
					while (iterator.hasNext()) {
						c = iterator.next();
						
					bw.write(format.format(c.getDia()) + "|" + c.getNombre() + "|" + c.getCodigoColector() + "|" +
					UtilidadesReportes.changeNull(c.getTipo()) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getTransBanco())) + "|" + 
					UtilidadesReportes.changeNumber(String.valueOf(c.getConfrontaBanco())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getManualBanco())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getPendienteBanco())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getTransTelefonica())) + "|"+ 
					UtilidadesReportes.changeNumber(String.valueOf(c.getPagosTelefonica())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getManualTelefonica())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getPendienteTelefonica())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getConciliadas())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getDifTransaccion())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getAutomatica()))+ "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getPendienteTelefonica().subtract(c.getPendienteBanco()))) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getReal_b())) + "|" +
					UtilidadesReportes.changeNumber(String.valueOf(c.getReal_t()))+"\n");
					}
					
					Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
							"Descargando el archivo...");
					
					Filedownload.save(file,null);
					
					Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado",
							"ATENCION", Messagebox.OK, Messagebox.INFORMATION);
					
					Clients.clearBusy();
				
			}else {
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
						"No hay datos generados en la busqueda...");
				
				Messagebox.show(
						"Debe consultar datos antes de generar reportes.",
						"ATENCIÓN", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
				}
		}
		
	}
	
	// ******************************************************************************************************
	// ******************************************************************************************************

	// ********** AGREGO BENJAMIN ************
	// ********** 30/09/2013
/*
	public void onClick$btnGeneraReporteTxt() {
		Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
				"comienza a generar el reporte...");
			if (dtbDia.getValue() == null) {
				Messagebox.show(
						"Debe de seleccionar un rango de fecha para el reporte.",
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else if (dtbDia.getValue().after(dtbDia2.getValue())) {
				Messagebox.show("La fecha desde debe ser menor a la fecha hasta.",
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			} else {
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
						"fecha: " + fechaFormato.format(dtbDia.getValue()));
				generarTxtConciliacionesDos();
				Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
						"finaliza de generar el reporte...");
		}
		Logger.getLogger(ConciliacionController.class.getName())
		.log(Level.INFO, "Finaliza de generar el reporte...");

	}
*/
	// generaciond reporte
	private static String CAMPOS_REPORTE_CONCILIACIONES = "DIA|NOMBRE|TIPO|TRANSACCION TELEFONICA|PAGOS TELEFONICA";
	private static String QRYDOS = "SELECT DISTINCT dia||'|'|| "
			+ "b.nombre||'|'|| "
			+ "DECODE (a.tipo, '1', 'PRE-PAGO', '2', 'POST-PAGO')||'|'|| "
			+ "(SELECT NVL(COUNT(d.imp_pago),0) " + "FROM cb_pagos d "
			+ "WHERE d.tipo   = a.tipo " + "AND d.cod_caja = m.cod_agencia "
			+ "AND a.dia      = d.fecha " + ")||'|'|| "
			+ "(SELECT NVL( SUM (d.imp_pago),0) " + "FROM cb_pagos d "
			+ "WHERE d.tipo   = a.tipo " + "AND d.cod_caja = m.cod_agencia "
			+ "AND a.dia      = d.fecha " + ")  " + "FROM cb_conciliacion a, "
			+ "  cb_catalogo_agencia b, " + "  cb_banco_agencia_confronta m "
			+ "WHERE a.cbcatalogoagenciaid = b.cbcatalogoagenciaid "
			+ "AND a.cbcatalogoagenciaid   = m.cbcatalogoagenciaid "
			+ "AND a.tipo                  = m.tipo ";
/*
	public void generarTxtConciliacionesDos() {

			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
			String fechaDesde = fechaFormato.format(dtbDia.getValue());
			String fechaHasta = fechaFormato.format(dtbDia2.getValue());
			String nombreArchivo = "reporte_conciliaciones_" + sdf.format(fecha)
					+ ".txt";
			QRYDOS += " and a.dia >= to_date('" + fechaDesde + "', 'dd/MM/yyyy')"
					+ " and a.dia <= to_date('" + fechaHasta + "', 'dd/MM/yyyy') ";

			String usuario = obtenerUsuario().getUsuario();
			String correo = obtenerUsuario().getEmail();

			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, "query: " + QRYDOS);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, "usuario: " + usuario);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, "correo: " + correo);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, "nombre de archivo: " + nombreArchivo);

			CallableStatement callableStatement = null;
			try {
				Connection con = obtenerDtsPromo().getConnection();
				try {
					callableStatement = con
							.prepareCall("{call PR_GENERA_REPORTE  (?, ?, ?, ?, ?)}");
					callableStatement.setString(1, QRY);
					callableStatement.setString(2, CAMPOS_REPORTE_CONCILIACIONES);
					callableStatement.setString(3, usuario);
					callableStatement.setString(4, nombreArchivo);
					callableStatement.setString(5, correo);
					callableStatement.executeUpdate();

					// ejecuta shell para el envio de reporte
					ejecutaSh();

					Messagebox
							.show("Su repote le estara llegando en unos momentos a su correo.",
									"ATENCION", Messagebox.OK,
									Messagebox.EXCLAMATION);
				} finally {
				if(callableStatement != null)
					callableStatement.close();
				if(con != null)
					con.close();
			}
		} catch (Exception e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}

	}
*/
	// metodo para ejecutar sh
	public void ejecutaSh() throws JSchException {
		FTPClient client = new FTPClient();

		// Obtiene valores de la conexion;
		List<ListaCombo> configuraciones = new CBConciliacionDAO()
				.listadoParametros();
		String ipFTP = "";
		String usuarioFTP = "";
		String passFTP = "";
		String directorioFTP = "";
		for (ListaCombo lista : configuraciones) {

			if (lista.getValorCaracter().compareTo("FTPIP") == 0) {
				ipFTP = lista.getValorCodigo();
			}
			if (lista.getValorCaracter().compareTo("FTPUSUARIO") == 0) {
				usuarioFTP = lista.getValorCodigo();
			}
			if (lista.getValorCaracter().compareTo("FTPPASSWORD") == 0) {
				passFTP = lista.getValorCodigo();
			}
			if (lista.getValorCaracter().compareTo("FTPDIRECTORIO") == 0) {
				directorioFTP = lista.getValorCodigo();
			}
		}

		try {
			client.connect(ipFTP);
			boolean login = client.login(usuarioFTP, passFTP);
			Logger.getLogger(ConciliacionController.class.getName())
				.log(Level.INFO, "Conectado: " + login);
			if (login) {
				ejecutaComandoSSH(usuarioFTP, passFTP, ipFTP, "cd "
						+ directorioFTP + "; " + "./genera_reporte.sh");
			} else {
				Logger.getLogger(ConciliacionController.class.getName())
					.log(Level.INFO, "Sin Conexion FTP...");
			}
			client.logout();
			client.disconnect();

		} catch (SocketException e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		} catch (IOException e) {
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.SEVERE, null, e);
		}

	}

	public void ejecutaComandoSSH(String sshuser, String sshpass,
			String sshhost, String command) throws JSchException, IOException {
			List<String> results = new ArrayList<String>();
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"ejecutaComandoSSH start "
					+ Calendar.getInstance().getTime());
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, 
					"\tcommand -> " + command);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			JSch jsch = new JSch();
			Session session;
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, sshuser);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, sshhost);
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, sshpass);

			session = jsch.getSession(sshuser, sshhost, 22);
			session.setPassword(sshpass);
			session.setConfig(config);
			session.setTimeout(10000);
			session.connect();
	
			Logger.getLogger(ConciliacionController.class.getName())
				.log(Level.INFO, "conectado!" + session.isConnected());
	
			Channel channel = session.openChannel("shell");

			// channel.setInputStream(null);
			// channel.setOutputStream(null);
			InputStream in = channel.getExtInputStream();
			OutputStream out = channel.getOutputStream();
			channel.connect();
	
			Logger.getLogger(ConciliacionController.class.getName())
				.log(Level.INFO, "channel conectado! " + channel.isConnected());
	
			byte[] tmp = new byte[1024];
			out.write((command + ";exit").getBytes());
			out.write(("\n").getBytes());
			out.flush();

			while (true) {
				while (in.available() > 0) {
					Logger.getLogger(ConciliacionController.class.getName())
						.log(Level.INFO, "Ingreso");
					int i = in.read(tmp, 0, 1024);
					Logger.getLogger(ConciliacionController.class.getName())
						.log(Level.INFO, "valor de i: " + i);
					if (i < 0) {
						Logger.getLogger(ConciliacionController.class.getName())
							.log(Level.INFO, "i es menor a 0 ");
						break;
					}
					String buffer = new String(tmp, 0, i);
					results.add(buffer);
					Logger.getLogger(ConciliacionController.class.getName())
						.log(Level.INFO, "resultados: " + results.get(0));
					Logger.getLogger(ConciliacionController.class.getName())
						.log(Level.INFO, "buffer contains: " + buffer.intern());
					if (buffer.contains("REMOTE JSH COMMAND FINISHED")) {
						break;
					}

				}
				if (channel.isClosed()) {
					in.close();
					break;
				}
			}
			channel.disconnect();
			session.disconnect();
			Logger.getLogger(ConciliacionController.class.getName()).log(Level.INFO, "\trunCommandViaSSH end "
				+ Calendar.getInstance().getTime());

	}
	

	
	
	
}
