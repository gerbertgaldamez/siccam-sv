package com.terium.siccam.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;

import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;

import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBAplicaDesaplicaPagosDAO;
import com.terium.siccam.dao.CBDataSinProcesarDAO;
import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBAplicaDesaplicaModel;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBResumenDiarioConciliacionModel;
import com.terium.siccam.model.CBConciliacionCajasModel;
import com.terium.siccam.model.CBConsultaContabilizacionModel;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

public class CBAplicaDesaplicaPagosController extends ControladorBase {
	/**
	 * creador Ovidio Santos
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(CBAplicaDesaplicaPagosController.class);

	Combobox cmbAgencia;
	Combobox cmbBanco;
	Checkbox ckbDeleteAll;
	Datebox dtbDia = null;
	Datebox dtbDia2 = null;
	Listbox lbxlistado;
	Button btnBuscar;
	Button btnExcel;
	Button btnAplicaDesaplica;
	int idseleccionado = 1;
	DateFormat fechaFormato = new SimpleDateFormat(Constantes.FORMATO_FECHA1);
	Combobox cmbTipo;
	Combobox cmbEstado;

	CBAplicaDesaplicaModel cbainsertados = null;
	List<CBResumenDiarioConciliacionModel> lst;
	List<CBResumenDiarioConciliacionModel> lstToda;

	private List<CBParametrosGeneralesModel> lstAplicaDesaplica = new ArrayList<CBParametrosGeneralesModel>();

	List<CBAplicaDesaplicaModel> detallesSeleccionados = null;
	Set<Listitem> lstSeleccionados = null;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		this.btnExcel.setDisabled(true);
		llenaComboBanco();
		llenaComboTipoEstado();
		llenaComboTipo();
	}

	public void onClick$btnBuscar() {
		CBAplicaDesaplicaPagosDAO cbaidao = new CBAplicaDesaplicaPagosDAO();
		SimpleDateFormat formato = new SimpleDateFormat(Constantes.FORMATO_FECHA1);
		String tipo = null;
		int idAgencia = 0;
		String estado = null;

		if (cmbAgencia.getSelectedItem() != null) {
			idAgencia = Integer.parseInt(cmbAgencia.getSelectedItem().getValue().toString());
		}
		if (cmbEstado.getSelectedItem() != null && !Constantes.TODOS.equals(cmbEstado.getSelectedItem().getLabel())) {
			estado = cmbEstado.getSelectedItem().getValue();
		}

		if (cmbTipo.getSelectedItem() != null && !Constantes.TODOS.equals(cmbTipo.getSelectedItem().getLabel())) {
			tipo = cmbTipo.getSelectedItem().getValue();
		}
		if (dtbDia.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha de inicio antes de consultar datos.", Constantes.ATENCION,
					Messagebox.OK, Messagebox.EXCLAMATION);
			return;
		} else if (dtbDia2.getValue() == null) {
			Messagebox.show("Debe ingresar la fecha fin antes de consultar datos.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (dtbDia.getValue().after(dtbDia2.getValue())) {
			Messagebox.show("La fecha desde debe ser menor a la fecha hasta.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (Tools.diferenciasDeFechas(dtbDia.getValue(), dtbDia2.getValue()) >= 7) {
			Messagebox.show("No se puede consultar informacion mas de 7 dias.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else {
			llenaDestalle(cbaidao.obtieneListaArchivosCargados(tipo, idAgencia, estado,
					formato.format(dtbDia.getValue()), formato.format(dtbDia2.getValue())));
		}
	}

	public void llenaDestalle(List<CBAplicaDesaplicaModel> list) {
		limpiaCampos(lbxlistado);
		DateFormat fechaHora = new SimpleDateFormat(Constantes.FORMATO_FECHA1);

		if (list.isEmpty()) {
			logger.debug("llenaDestalle() - " + "La lista se enuentra vacia para cargar detalle");
		} else {
			Iterator<CBAplicaDesaplicaModel> it = list.iterator();

			Listitem item = null;
			Listcell cell = null;
			while (it.hasNext()) {
				cbainsertados = it.next();
				item = new Listitem();

				cell = new Listcell();
				cell.setLabel(cbainsertados.getAgencia());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(fechaHora.format(cbainsertados.getDia()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTipo());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getCliente());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getNombre());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getDesPago());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTransTelca());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTelefono());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTransBanco());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(cbainsertados.getImpPago()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(cbainsertados.getMonto()));
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(convertirADecimal(cbainsertados.getManual()));
				cell.setParent(item);

				if ((cbainsertados.getImpPago().compareTo(BigDecimal.ZERO) == 0)
						&& (cbainsertados.getImpPago().compareTo(cbainsertados.getMonto()) != 0)) {
					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cbainsertados.setPendienteBanco(BigDecimal.ZERO);
					cell.setLabel("0.00");
					cell.setParent(item);

					cbainsertados = calcularPendienteConciliarTelefonica(cbainsertados);
					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cell.setLabel(convertirADecimal(cbainsertados.getPendienteTelefonica()));
					cell.setParent(item);
				}

				if (cbainsertados.getMonto().compareTo(BigDecimal.ZERO) == 0
						&& (cbainsertados.getMonto().compareTo(cbainsertados.getImpPago()) != 0)) {
					cbainsertados = calcularPendienteConciliarBanco(cbainsertados);
					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cell.setLabel(convertirADecimal(cbainsertados.getPendienteBanco()));
					cell.setParent(item);

					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cbainsertados.setPendienteTelefonica(BigDecimal.ZERO);
					cell.setLabel("0.00");
					cell.setParent(item);

				}

				if ((cbainsertados.getMonto().compareTo(cbainsertados.getImpPago()) == 0)) {

					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cbainsertados.setPendienteBanco(BigDecimal.ZERO);
					cell.setLabel("0.00");
					cell.setParent(item);

					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cbainsertados.setPendienteTelefonica(BigDecimal.ZERO);
					cell.setLabel("0.00");
					cell.setParent(item);
				}

				cell = new Listcell();
				cell.setLabel(cbainsertados.getSucursal());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getNombre_sucursal());
				cell.setParent(item);

				cell = new Listcell();
				cell.setLabel(cbainsertados.getTipo_sucursal());
				cell.setParent(item);

				if (cbainsertados.getComision().compareTo(BigDecimal.ZERO) == 0) {
					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cbainsertados.setComision(BigDecimal.ZERO);
					cell.setLabel("0.00");
					cell.setParent(item);
				} else {
					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cell.setLabel(convertirADecimal(cbainsertados.getComision()));
					cell.setParent(item);
				}

				if (cbainsertados.getMonto_comision().compareTo(BigDecimal.ZERO) == 0) {
					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cbainsertados.setMonto_comision(BigDecimal.ZERO);
					cell.setLabel("0.00");
					cell.setParent(item);
				} else {
					cell = new Listcell();
					cell.setStyle(Constantes.TEXT_RIGHT);
					cell.setLabel(convertirADecimal(cbainsertados.getMonto_comision()));
					cell.setParent(item);
				}

				cell = new Listcell();
				if ((cbainsertados.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
						&& (cbainsertados.getPendienteTelefonica().compareTo(BigDecimal.ZERO) == 0)) {

					cell.setStyle(Constantes.TEXT_CENTER);
					cell.setImage("/img/cerrado.png");
					cbainsertados.setEstado("1");
					cell.setParent(item);
					cell.setTooltip("popAsociacion");
				}
				if ((cbainsertados.getPendienteBanco().compareTo(BigDecimal.ZERO) > 0)
						|| (cbainsertados.getPendienteTelefonica().compareTo(BigDecimal.ZERO) > 0)) {
					cell.setImage("/img/rojo.png");
					cbainsertados.setEstado("2");
					cell.setParent(item);
					cell.setTooltip("popAsociacion");
				}
				if ((cbainsertados.getManual().compareTo(BigDecimal.ZERO) > 0)
						&& (cbainsertados.getPendienteBanco().compareTo(BigDecimal.ZERO) == 0)
						&& (cbainsertados.getPendienteTelefonica().compareTo(BigDecimal.ZERO) == 0)) {
					cell.setImage("/img/amarillo.png");
					cbainsertados.setEstado("3");
					cell.setParent(item);
					cell.setTooltip("popAsociacion");
				}

				cell = new Listcell();
				ckbDeleteAll.setTooltiptext("MarcarTodos");
				ckbDeleteAll.setParent(cell);

				item.setValue(cbainsertados);
				item.setAttribute("objSeleccionado", cbainsertados);
				item.setParent(lbxlistado);
				if (lbxlistado.getItemCount() != 0) {
					btnExcel.setDisabled(false);

				} else {
					btnExcel.setDisabled(true);
				}
			}
		}
	}

	public void onCheck$ckbDeleteAll() {
		if (ckbDeleteAll.isChecked()) {

			List<Listitem> list = lbxlistado.getItems();
			logger.debug("onCheck$ckbDeleteAll() - " + "listado tamaño : " + list.size());
			if (!list.isEmpty()) {
				detallesSeleccionados = new ArrayList<CBAplicaDesaplicaModel>();
				for (Listitem lista : list) {
					lista.setSelected(true);
				}
			}
		} else {
			List<Listitem> list = lbxlistado.getItems();
			if (list.size() > 0) {
				detallesSeleccionados = new ArrayList<CBAplicaDesaplicaModel>();
				for (Listitem lista : list) {
					lista.setSelected(false);
					detallesSeleccionados = new ArrayList<CBAplicaDesaplicaModel>();
				}
			}
		}

	}

	public String changeNull(String cadena) {
		if (cadena == null) {
			return " ";
		} else {
			return cadena;
		}
	}

	public void onClick$btnExcel() {
		logger.debug("onClick$btnExcel() - " + "Generando reporte Aplica desaplica pagos...");
		int contador = 0;
		List<CBAplicaDesaplicaModel> list = new ArrayList<CBAplicaDesaplicaModel>();

		for (Listitem item : lbxlistado.getItems()) {

			if (item.isSelected()) {
				contador++;
				CBAplicaDesaplicaModel objModel = (CBAplicaDesaplicaModel) item.getValue();
				list.add(objModel);
			}
		}
		if (contador == 0) {
			for (Listitem item2 : lbxlistado.getItems()) {
				CBAplicaDesaplicaModel objModel = (CBAplicaDesaplicaModel) item2.getValue();
				list.add(objModel);
			}
		}

		generarReporte(list);

	}

	public void generarReporte(List<CBAplicaDesaplicaModel> list) {
		BufferedWriter bw = null;
		try {
			Date fecha = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FORMATO_FECHA2);

			File archivo = new File(Constantes.REPORTE_APLICADESAPLICA + sdf.format(fecha) + Constantes.CSV);

			if (!archivo.exists()) {
				if (archivo.createNewFile())
					logger.debug("onClick$btnExcel() - " + "Archivo creado de forma correcta");

			}
			bw = new BufferedWriter(new FileWriter(archivo));
			bw.write(Constantes.ENCABEZADO_APLICADESAPLICA);

			CBAplicaDesaplicaModel objModelReporte = null;
			Iterator<CBAplicaDesaplicaModel> it = list.iterator();
			while (it.hasNext()) {
				objModelReporte = it.next();
				bw.write(changeNull(objModelReporte.getAgencia()).trim() + "|" + (objModelReporte.getDia()) + "|"
						+ changeNull(objModelReporte.getTipo()).trim() + "|"
						+ changeNull(objModelReporte.getCliente()).trim() + "|"
						+ changeNull(objModelReporte.getNombre()).trim() + "|"
						+ changeNull(objModelReporte.getDesPago()).trim() + "|"
						+ changeNull(objModelReporte.getTransTelca()).trim() + "|"
						+ changeNull(objModelReporte.getTelefono()).trim() + "|"
						+ changeNull(objModelReporte.getTransBanco()).trim() + "|" + (objModelReporte.getImpPago())
						+ "|" + (objModelReporte.getMonto()) + "|" + (objModelReporte.getManual()) + "|"
						+ (objModelReporte.getPendienteBanco()) + "|" + (objModelReporte.getPendienteTelefonica()) + "|"
						+ changeNull(objModelReporte.getSucursal()).trim() + "|"
						+ changeNull(objModelReporte.getNombre_sucursal()).trim() + "|"
						+ changeNull(objModelReporte.getTipo_sucursal()).trim() + "|" + (objModelReporte.getComision())
						+ "|" + (objModelReporte.getMonto_comision()) + "|" + (objModelReporte.getEstado()) + "\n");

			}

			logger.debug("generarReporte() - " + "Archivo creado de forma correcta");

			Filedownload.save(archivo, null);
			Messagebox.show("Reporte generado de manera exitosa, el archivo ha sido descargado", Constantes.ATENCION,
					Messagebox.OK, Messagebox.INFORMATION);
			Clients.clearBusy();

		} catch (Exception e) {
			logger.error("generarReporte() - Error ", e);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					logger.error("generarReporte() - Error ** ", e);
				}
			}
		}
	}

	////////////////////////////////
	private CBAplicaDesaplicaModel calcularPendienteConciliarTelefonica(CBAplicaDesaplicaModel obj) {
		BigDecimal conciliarTelefonica;
		BigDecimal impPago = obj.getImpPago();
		BigDecimal monto = obj.getMonto();
		BigDecimal manual = obj.getManual();

		conciliarTelefonica = monto.subtract(impPago);
		conciliarTelefonica = conciliarTelefonica.subtract(manual);

		obj.setPendienteTelefonica(conciliarTelefonica);

		return obj;
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
		return numero;
	}

	private CBAplicaDesaplicaModel calcularPendienteConciliarBanco(CBAplicaDesaplicaModel obj) {
		BigDecimal pendienteConciliarBanco;

		BigDecimal impPago = obj.getImpPago();
		BigDecimal monto = obj.getMonto();
		BigDecimal manual = obj.getManual();
		pendienteConciliarBanco = impPago.subtract(monto);
		pendienteConciliarBanco = pendienteConciliarBanco.subtract(manual);
		obj.setPendienteBanco(pendienteConciliarBanco);

		return obj;
	}

	public void llenaComboBanco() {
		limpiaCombobox(cmbBanco);
		CBAplicaDesaplicaPagosDAO objDao = new CBAplicaDesaplicaPagosDAO();
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

	public void onSelect$cmbBanco() {
		limpiaCombobox(cmbAgencia);
		CBAplicaDesaplicaPagosDAO objDao = new CBAplicaDesaplicaPagosDAO();
		int idBanco = Integer.parseInt(cmbBanco.getSelectedItem().getValue().toString());
		List<CBCatalogoAgenciaModel> list = objDao.generaConsultaAgencia(idBanco);
		Iterator<CBCatalogoAgenciaModel> it = list.iterator();
		CBCatalogoAgenciaModel obj = null;
		while (it.hasNext()) {
			obj = it.next();

			Comboitem item = new Comboitem();
			item.setLabel(obj.getNombre());
			item.setValue(obj.getcBCatalogoAgenciaId());
			item.setParent(cmbAgencia);
		}
	}

	/**
	 * Llenado de combo tipo
	 */
	public void llenaComboTipo() {
		CBParametrosGeneralesDAO objeDAO = new CBParametrosGeneralesDAO();
		lstAplicaDesaplica = objeDAO.obtenerParamConvenios();
		if (lstAplicaDesaplica != null && lstAplicaDesaplica.size() > 0) {
			Iterator<CBParametrosGeneralesModel> it = lstAplicaDesaplica.iterator();
			CBParametrosGeneralesModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();
				item = new Comboitem();
				item.setLabel(obj.getObjeto());
				item.setValue(obj.getValorObjeto1());
				item.setParent(cmbTipo);
			}
			logger.debug("llenaComboTipo()  " + "- Llena combo tipo");
		} else {
			Messagebox.show("Error al cargar la configuracion de tipos de conciliacion", "ATENCION", Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	public void llenaComboTipoEstado() {
		logger.debug("llenaComboTipoEstado()  " + "Llena combo tipo estado");

		limpiaCombobox(cmbEstado);

		CBAplicaDesaplicaPagosDAO objeDAO = new CBAplicaDesaplicaPagosDAO();
		lstAplicaDesaplica = objeDAO.obtenerEstadoCmb("ESTADO");
		cmbEstado.getText();
		for (CBParametrosGeneralesModel d : lstAplicaDesaplica) {
			Comboitem item = new Comboitem();

			item.setValue(d.getValorObjeto1());
			item.setLabel(d.getObjeto());
			item.setParent(this.cmbEstado);
			if ("2".equals(d.getValorObjeto1())) {
				cmbEstado.setSelectedItem(item);

			}
		}

	}
	// limpia campos

	public void limpiaCampos(Listbox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	public void limpiaCombobox(Combobox component) {

		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}

	}

	public void onClick$btnAplicaDesaplica() {
		CBConsultaContabilizacionModel objModel = new CBConsultaContabilizacionModel();
		SimpleDateFormat formato = new SimpleDateFormat(Constantes.FORMATO_FECHA1);
		String tipo = null;
		int idAgencia = 0;
		String estado = null;
		if (cmbAgencia.getSelectedItem() != null) {
			idAgencia = Integer.parseInt(cmbAgencia.getSelectedItem().getValue().toString());
		}

		if (cmbEstado.getSelectedItem() != null) {
			if (Constantes.TODOS.equals(cmbEstado.getSelectedItem().getLabel())) {
				estado = null;
			} else {
				estado = cmbEstado.getSelectedItem().getValue();
			}
		}

		if (cmbTipo.getSelectedItem() != null) {
			if (Constantes.TODOS.equals(cmbTipo.getSelectedItem().getLabel())) {
				estado = null;
			} else {
				tipo = cmbTipo.getSelectedItem().getValue();
			}
		}
		if (dtbDia.getValue() != null && dtbDia2.getValue() != null) {
			objModel.setFechaini(formato.format(dtbDia.getValue()));
			objModel.setFechafin(formato.format(dtbDia2.getValue()));

			CBAplicaDesaplicaPagosDAO cbaidao = new CBAplicaDesaplicaPagosDAO();
			List<CBAplicaDesaplicaModel> listaSapModel = cbaidao.obtienDatosSAP2(tipo, idAgencia, estado,
					formato.format(dtbDia.getValue()), formato.format(dtbDia2.getValue()));
			List<String> listaSapArchivo = new ArrayList<String>();

			logger.debug("onClick$btnAplicaDesaplica() - " + " tamaño de lista sap " + listaSapModel.size()
					+ listaSapArchivo.size());
			if (listaSapModel != null && listaSapModel.size() > 0) {

				int contCheck = 0;
				for (Listitem item1 : lbxlistado.getItems()) {
					CBAplicaDesaplicaModel objListboxModel = (CBAplicaDesaplicaModel) item1.getValue();
					if (item1.isSelected()) {
						contCheck++;
						int idSeleccionado = Integer.parseInt(objListboxModel.getConciliacionId());
						logger.debug("onClick$btnAplicaDesaplica() - " + "entra al contcheck = 0");

						for (CBAplicaDesaplicaModel objSAP : listaSapModel) {
							if (idSeleccionado == objSAP.getIdSAP()) {
								logger.debug("onClick$btnAplicaDesaplica() - " + "idseleccionado - objsap.getidsap "
										+ idSeleccionado + objSAP.getIdSAP());
								listaSapArchivo.add(objSAP.getLineaSAP());
							}
						}

					}
				}
				if (contCheck == 0) {
					logger.debug("onClick$btnAplicaDesaplica() " + " - entra al contcheck == 0");
					for (CBAplicaDesaplicaModel objSAP : listaSapModel) {
						listaSapArchivo.add(objSAP.getLineaSAP());
					}
				}
				generaArchivoSAP(listaSapArchivo);
			} else {
				logger.debug("onClick$btnAplicaDesaplica() - " + "Lista SAP esta vacia.");

			}
		} else {
			Messagebox.show("Primero debe consultar informacion para generar el archivo", Constantes.ATENCION,
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

	}

	public void generaArchivoSAP(List<String> list) {

		BufferedWriter bw = null;
		try {
			if (list != null && list.size() > 0) {

				Iterator<String> it = list.iterator();
				logger.debug("generaArchivoSAP() - " + StringUtils.rightPad("change", 20, " "));
				Date fecha = new Date();

				SimpleDateFormat sdf2 = new SimpleDateFormat(Constantes.FORMATO_FECHA3);

				/**
				 * Generacion de archivo .txt
				 */
				String nombreArchivo = "ArchivoAplicaDesaplica";
				File archivoTxt = new File(nombreArchivo + sdf2.format(fecha) + ".txt");

				bw = new BufferedWriter(new FileWriter(archivoTxt));

				while (it.hasNext()) {
					String bean = it.next() + "\n";
					bw.write(bean);
				}
				logger.debug("ARCHIVO .txt GENERADO CON EXITO");

				/**
				 * Descarga de archivo SAP
				 */
				Filedownload.save(archivoTxt, null);

				Messagebox.show("El archivo ha sido cargado de manera correcta", "Atencion", Messagebox.OK,
						Messagebox.INFORMATION);
			} else {
				Messagebox.show("Debe consultar informacion antes de subir un archivo", "Atencion", Messagebox.OK,
						Messagebox.EXCLAMATION);
			}

		} catch (IOException e) {
			logger.error("ERROR : ", e);

		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					logger.error("ERROR * : ", e);
				}
		}
		logger.debug("\n==== FIN GENERACION DE ARCHIVO SAP ====\n");
	}

}
