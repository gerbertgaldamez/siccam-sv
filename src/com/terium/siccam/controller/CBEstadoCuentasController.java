package com.terium.siccam.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zk.ui.event.UploadEvent;

import com.google.common.base.Splitter;
import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBEstadoCuentaDAO;
import com.terium.siccam.implement.SeleccionaCargaCredomaticInfz;
import com.terium.siccam.model.CBEstadoCuentasModel;
import com.terium.siccam.model.CBPagosModel;
import com.terium.siccam.utils.CBEstadoCuentaUtils;
import com.terium.siccam.utils.CBProcesaLiquidacionesThread;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

/**
 * @author Juankrlos
 */
public class CBEstadoCuentasController extends ControladorBase implements SeleccionaCargaCredomaticInfz {

	private static Logger logger = Logger.getLogger(CBEstadoCuentasController.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HttpSession misession = (HttpSession) Sessions.getCurrent().getNativeSession();

	private String usuario;
	Media media;
	boolean isReloadFile = false;
	int idArchivo = 0;
	List<CBEstadoCuentasModel> listCuentasConf;
	CBEstadoCuentasModel cuentasCombo = new CBEstadoCuentasModel();
	List<CBEstadoCuentasModel> listVisa = new ArrayList<CBEstadoCuentasModel>();
	List<CBEstadoCuentasModel> listVisaDet = new ArrayList<CBEstadoCuentasModel>();
	List<CBEstadoCuentasModel> listCredomatic = new ArrayList<CBEstadoCuentasModel>();
	List<CBEstadoCuentasModel> listSociedad = new ArrayList<CBEstadoCuentasModel>();
	List<CBEstadoCuentasModel> listCierreCaja = new ArrayList<CBEstadoCuentasModel>();

	CBEstadoCuentaDAO objDaoDelete = new CBEstadoCuentaDAO();
	CBEstadoCuentasModel objModelDelete = null;

	// Fin Datos agregados por JCDlacruz - 09/01/2017

	// Componentes
	Combobox cmbxEntidad;
	Label lblMensaje;
	Image imgEstatus;

	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		usuario = obtenerUsuario().getUsuario();
		llenaComboEntidades();
	}

	/**
	 * @author Juankrlos -12/01/2017 Obtener listado para combo Cuentas
	 */

	public void onUpload$btnArchivoEstados(UploadEvent event) {
		media = event.getMedia();

		if (Constantes.XLSX.equals(media.getFormat())) {
			lblMensaje.setValue(Constantes.ARCHIVO_SELECCIONADO + media.getName());
			lblMensaje.setStyle(Constantes.COLOR_BLUE);
			imgEstatus.setSrc(Constantes.IMG_BLUE);
		} else if (Constantes.XLS.equals(media.getFormat())) {
			lblMensaje.setValue(Constantes.ARCHIVO_SELECCIONADO + media.getName());
			lblMensaje.setStyle(Constantes.COLOR_BLUE);
			imgEstatus.setSrc(Constantes.IMG_BLUE);
		} else if (Constantes.TXT.equals(media.getFormat())) {
			lblMensaje.setValue(Constantes.ARCHIVO_SELECCIONADO + media.getName());
			lblMensaje.setStyle(Constantes.COLOR_BLUE);
			imgEstatus.setSrc(Constantes.IMG_BLUE);
		} else {
			lblMensaje.setValue("Error en formato -- Archivo seleccionado: " + media.getName());
			lblMensaje.setStyle(Constantes.COLOR_RED);
			imgEstatus.setSrc(Constantes.IMG_RED);
		}
	}

	/**
	 * Combo entidades
	 */
	public void llenaComboEntidades() {
		CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
		listCuentasConf = objDao.obtenerCuentasConf();
		if (listCuentasConf.isEmpty()) {
			Messagebox.show("Error al cargar la configuracion de cuentas", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
		} else {
			Iterator<CBEstadoCuentasModel> it = listCuentasConf.iterator();
			CBEstadoCuentasModel obj = null;
			Comboitem item = null;
			while (it.hasNext()) {
				obj = it.next();

				item = new Comboitem();
				item.setLabel(obj.getNombrebanco());
				item.setValue(obj.getCbestadocuentaconfid());
				item.setParent(cmbxEntidad);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnCargaEstados() {
		try {
			if (cmbxEntidad.getSelectedItem() == null) {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			} else if (media == null) {
				Messagebox.show("Primero debe seleccionar un archivo para cargar ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			} else {
				// validar formato del archivo
				logger.debug("onClick$btnCargaEstados() - " + "Formato del archivo " + media.getFormat());
//				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//						"Formato del archivo " + media.getFormat());

				if (media.getFormat().equals(Constantes.XLSX) || media.getFormat().equals(Constantes.XLS)
						|| media.getFormat().equals(Constantes.TXT) || media.getFormat().equals(Constantes.CSV2)) {

					int id = cmbxEntidad.getSelectedItem().getValue();
					logger.debug("onClick$btnCargaEstados() - " + "id que valida banco: " + id + " archivo: "
							+ media.getName() + " Fecha: " + objDaoDelete.obtieneFecha());
//					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO, "id que valida banco: ",
//							id + " archivo: " + media.getName() + " Fecha: " + objDaoDelete.obtieneFecha());
					objModelDelete = objDaoDelete.validaCarga(id, media.getName(), objDaoDelete.obtieneFecha());
					logger.debug(
							"onClick$btnCargaEstados() - " + "id que valida banco: " + "Objeto: " + objModelDelete);
//					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO, "Objeto: ",
//							objModelDelete);
					if (objModelDelete != null) {
						Messagebox.show(
								"Ya hay registros para " + cmbxEntidad.getSelectedItem().getLabel()
										+ " - Archivo cargado: " + objModelDelete.getNombrearchivos() + " - Fecha:"
										+ objModelDelete.getFecha() + " (Desea reemplazar dicha carga?)",
								Constantes.ATENCION, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
								new EventListener() {
									public void onEvent(Event event) throws Exception {
										if (((Integer) event.getData()).intValue() != Messagebox.YES) {
											Messagebox.show("Accion Cancelada", Constantes.ATENCION, Messagebox.OK,
													Messagebox.EXCLAMATION);
											isReloadFile = false;
											ingresaRegistro();
										} else {
											isReloadFile = true;
											ingresaRegistro();
										}
									}
								});

					} else {
						isReloadFile = true;
						ingresaRegistro();
					}
				} else {
					Messagebox.show("Error en el formato! Seleccione un archivo valido", "ERROR", Messagebox.OK,
							Messagebox.ERROR);
				}

			}
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error al cargar el archivo.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.ERROR);
			if (media != null)
				cambiaEstadoMensaje(false, media.getName());
			else
				cambiaEstadoMensaje(false, "Archivo desconocido o no cargado");

			logger.debug("onClick$btnCargaEstados() - Error ", e);
			// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE,
			// null, e);
		}

	}

	/**
	 * Validamos que tipo de entidad se a seleccionado para la carga de archivo
	 */

	/**
	 * @author Omar Gomez 10/July/2017 Modify "SISTEMA COMERCIAL" agrega a version
	 *         unificada ovidio santos
	 */
	public void ingresaRegistro() {
		try {
			if ("BANCO NACIONAL".equals(cmbxEntidad.getSelectedItem().getLabel())) {
				int totalname = media.getName().length();
				logger.debug(
						"ingresaRegistro() - " + "extension: " + media.getName().substring((totalname - 3), totalname));
//				Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//						"extension: " + media.getName().substring((totalname - 3), totalname));
				try {
					if (media == null) {
						Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
								Messagebox.EXCLAMATION);
						return;
					} else {
						logger.debug("ingresaRegistro() - " + "pasa al siguiente nivel en banco nacional");

//						Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//								"pasa al siguiente nivel en banco nacional");
						if (isReloadFile) {
							if (objModelDelete != null) {
								eliminaRegistros();
							} else {
								logger.debug("ingresaRegistro() - " + "No hay datos ingresados");
//								Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//										"No hay datos ingresados");
							}
							ingresaArchivo();
							leerArchivoBancoNacional();
						} else {
							logger.debug("ingresaRegistro() - " + "Usuario cancela accion");
//							Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//									"Usuario cancela accion");
						}
					}
				} catch (NumberFormatException e) {
					logger.debug("ingresaRegistro() - Error ", e);
					// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE,
					// null, e);
				} catch (ParseException e) {
					logger.debug("ingresaRegistro() - Error ", e);
					// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE,
					// null, e);
				}

			} else if (cmbxEntidad.getSelectedItem().getLabel().equals("VISA")) {

				try {

					if (media == null) {
						Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
								Messagebox.EXCLAMATION);
						return;

					} else {

						logger.debug("ingresaRegistro() - pasa al siguiente nivel");
//						Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//								"pasa al siguiente nivel");

						if (isReloadFile) {
							if (objModelDelete != null) {
								eliminaRegistros();

							} else {
								logger.debug("ingresaRegistro() - " + "No hay datos ingresados");
//								Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//										"No hay datos ingresados");
							}

							ingresaArchivo();
							leerArchivoVisa();

						} else {
							logger.debug("ingresaRegistro() - Usuario cancela accion");
//							Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//									"Usuario cancela accion");
						}
					}
				} catch (NumberFormatException e) {
					logger.debug("ingresaRegistro() - Error ", e);
					// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE,
					// null, e);
				} catch (ParseException e) {
					logger.debug("ingresaRegistro() - Error ", e);
					// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.SEVERE,
					// null, e);
				}

			} else if ("CREDOMATIC".equals(cmbxEntidad.getSelectedItem().getLabel().trim())) {
				if (media == null) {
					Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				} else {
					logger.debug("ingresaRegistro() - pasa al siguiente nivel");
//					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//							"pasa al siguiente nivel");

					int totalname = media.getName().length();

					logger.debug("ingresaRegistro() - " + "extension: "
							+ media.getName().substring((totalname - 3), totalname));
//					Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO, "extension: ",
//							media.getName().substring((totalname - 3), totalname));

					String format = null;

					if (media.getFormat().equals(Constantes.XLSX)) {
						format = media.getFormat();
					} else if (media.getName().substring((totalname - 3), totalname).toUpperCase()
							.equals(Constantes.CSV2.toUpperCase())) {
						format = Constantes.CSV2;
					} else if (media.getName().substring((totalname - 3), totalname).toUpperCase()
							.equals(Constantes.XLS.toUpperCase())) {
						format = Constantes.XLS;
					} else {
						format = media.getFormat();
					}

					System.out.println("formato" + format);
					if (isReloadFile) {
						if (objModelDelete != null) {
							eliminaRegistros();
						} else {
							logger.debug("ingresaRegistro() - " + "No hay datos ingresados");
//							Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//									"No hay datos ingresados");
						}
						ingresaArchivo();
						if (media.getName().toUpperCase().contains("LIQ")) {
							logger.debug("ingresaRegistro() - " + "Encuentra que el archivo es liquidacion");
//							Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//									"Encuentra que el archivo es liquidacion");
							leerCredomaticEncabezado(format);
						} else if (media.getName().toUpperCase().contains("TRX")
								|| media.getName().toUpperCase().contains("DET")) {
							logger.debug("ingresaRegistro() - " + "Encuentra que el archivo es detalle");
//							Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//									"Encuentra que el archivo es detalle");
							leerCredomaticDetalle(format);
						} else if (misession.getAttribute(Constantes.CONEXION).equals(Tools.SESSION_SV)) {
							leerCredomaticEncabezado(format);
							logger.debug("ingresaRegistro() - " + "carga para CR " + format);
						} else {
							logger.debug("ingresaRegistro() - " + "El nombre del archivo no es correcto: "
									+ media.getName());
//							Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//									"El nombre del archivo no es correcto: " + media.getName());
						}
					} else {
						logger.debug("ingresaRegistro() - " + "Cancela accion...");
//						Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,
//								"Cancela accion...");
					}
				}
			} else if (Constantes.EXTRACTO_BANCARIO.equals(cmbxEntidad.getSelectedItem().getLabel().toUpperCase())) {
				if (media == null) {
					Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				} else {
					logger.debug("ingresaRegistro() - " + "pasa al siguiente nivel");
					if (isReloadFile) {
						if (objModelDelete != null) {
							eliminaRegistros();

						} else {
							logger.debug("ingresaRegistro() - " + "No hay datos ingresados");
						}
						ingresaArchivo();
						leerArchivoSociedad();
					} else {
						logger.debug("ingresaRegistro() - " + "Cancela accion");
					}
				}

			} else if ("LIQUIDACIONES".equals(cmbxEntidad.getSelectedItem().getLabel())) {
				int totalname = media.getName().length();
				logger.debug(
						"ingresaRegistro() - " + "extension: " + media.getName().substring((totalname - 3), totalname));
				try {
					if (media == null) {
						Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
								Messagebox.EXCLAMATION);
						logger.debug("ingresaRegistro() - " + " media is null ");
						return;
					} else {
						logger.debug("ingresaRegistro() - " + "pasa al siguiente nivel en cierre caja");
						if (isReloadFile) {
							if (objModelDelete != null) {
								eliminaRegistros();
							} else {
								logger.debug("ingresaRegistro() - " + "No hay datos ingresados");
							}
							ingresaArchivo();
							leerArchivoReporteCierreCaja();
						} else {
							logger.debug("ingresaRegistro() - " + "Usuario cancela accion");
						}
					}
				} catch (NumberFormatException e) {
					logger.error("ingresaRegistro() - Error ", e);
				} catch (ParseException e) {
					logger.error("ingresaRegistro() - Error ", e);
				}
			} else if (cmbxEntidad.getSelectedItem().getLabel().toUpperCase()
					.equals("SISTEMA COMERCIAL".toUpperCase())) {
				logger.debug("ingresaRegistro() - SISTEMA COMERCIAL");
				if (media == null) {
					Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
							Messagebox.EXCLAMATION);
					return;
				} else {
					logger.debug("ingresaRegistro() - pasa al siguiente nivel");
					if (isReloadFile) {
						if (objModelDelete != null) {
							eliminaRegistros(); // eliminar registros previamente cargados
						} else {
							logger.debug("ingresaRegistro() - No hay datos ingresados");
						}
						ingresaArchivo();
						leerArchivoSistemaComercial();
					} else {
						logger.debug("ingresaRegistro() - Cancela accion");
					}

				}

			}
		} catch (Exception e) {
			Messagebox.show("Se ha producido un error al cargar el archivo.", Constantes.ATENCION, Messagebox.OK,
					Messagebox.ERROR);
			if (media != null)
				cambiaEstadoMensaje(false, media.getName());
			else
				cambiaEstadoMensaje(false, "Archivo desconocido o no cargado");

			logger.error("ingresaRegistro() - error ", e);

		}
	}

	/**
	 * Eliminamos los registros segun la entidad seleccionada
	 */

	/**
	 * @author Omar Gomez Sistema Comercial
	 */
	public void eliminaRegistros() {
		int idarchivo = cmbxEntidad.getSelectedItem().getValue();
		if (cmbxEntidad.getSelectedItem().getLabel().toUpperCase().equals("BANCO NACIONAL".toUpperCase())) {
			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_ARCHIVO);
			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_BANCO_NACIONAL);
			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_BANCO_NACIONAL1);
		} else if (cmbxEntidad.getSelectedItem().getLabel().toUpperCase().equals("VISA")) {

			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_ARCHIVO);

			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_VISA);

			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_VISADET);

		} else if (cmbxEntidad.getSelectedItem().getLabel().toUpperCase().equals("CREDOMATIC".toUpperCase())) {
			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_ARCHIVO);

			if (media.getName().toUpperCase().contains("LIQ")) {
				logger.debug("eliminaRegistros() - Elimina liquidacion credo");
				objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
						CBEstadoCuentaDAO.DELETE_REGISTROS_CREDO);

			} else if (media.getName().toUpperCase().contains("TRX") || media.getName().toUpperCase().contains("DET")) {
				logger.debug("eliminaRegistros() - Elimina liquidacion credo");
				objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
						CBEstadoCuentaDAO.DELETE_REGISTROS_CREDODET);

			} else if (misession.getAttribute(Constantes.CONEXION).equals(Tools.SESSION_CR)) {
				logger.debug("eliminaRegistros() - Elimina liquidacion credo");
				objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
						CBEstadoCuentaDAO.DELETE_REGISTROS_CREDO);

			} else {
				logger.debug("eliminaRegistros() - " + "El nombre del archivo no es correcto: " + media.getName());
			}

		} else if (Constantes.EXTRACTO_BANCARIO.toUpperCase()
				.equals(cmbxEntidad.getSelectedItem().getLabel().toUpperCase())) {

			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_ARCHIVO);

			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_SOCIEDAD);

			objDaoDelete.actualizaLiquidacionDetalle(idArchivo);

		} else if (cmbxEntidad.getSelectedItem().getLabel().toUpperCase().equals("SISTEMA COMERCIAL".toUpperCase())) {
			logger.debug("eliminaRegistros() - " + "eliminar registros " + objModelDelete.getCbestadocuentaarchivosid()
					+ " " + idarchivo + " " + media.getName());

			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_ARCHIVO);

			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_SYS_COMERCIAL);
			// agregado por Gerbert
		} else if (cmbxEntidad.getSelectedItem().getLabel().toUpperCase().equals("LIQUIDACIONES".toUpperCase())) {
			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_ARCHIVO);
			objDaoDelete.eliminarRegistros(objModelDelete.getCbestadocuentaarchivosid(), idarchivo, media.getName(),
					CBEstadoCuentaDAO.DELETE_REGISTROS_LIQUIDACION);
		}
	}

	public void ingresaArchivo() {
		CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
		CBEstadoCuentasModel objModel = new CBEstadoCuentasModel();

		// Obtenemos el id de la tabla archivo para el registro a ingresar
		idArchivo = objDao.obtieneIdArchivos();

		int idBanco = cmbxEntidad.getSelectedItem().getValue();

		logger.debug("ingresaArchivo() - " + "Id obtenido: " + idArchivo + " id banco: " + idBanco);

		objModel.setCbestadocuentaarchivosid(idArchivo);
		objModel.setCbestadocuentaconfid(idBanco);
		objModel.setNombrearchivos(media.getName());
		objModel.setDescarchivos(media.getName() + " - Para: " + cmbxEntidad.getSelectedItem().getLabel());
		objModel.setUsuario(getUsuario());

		if (objDao.insertTableArchivos(objModel) > 0)
			logger.debug("ingresaArchivo() - " + "Inserta archivo");
		else
			logger.debug("ingresaArchivo() - " + "No inserta archivo");

	}

	/**
	 * Se leen llos archivos para CREDOMATIC
	 */
	String nombreArchivoCredomatic;

	public void leerArchivoCredo(String format) {
		CBEstadoCuentaUtils cbutil = new CBEstadoCuentaUtils();
		int idBAC = 0;
		if (cmbxEntidad.getSelectedItem() != null) {
			idBAC = cmbxEntidad.getSelectedItem().getValue();
		} else {
			Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else {

			nombreArchivoCredomatic = media.getName();
			misession.setAttribute("nombreArchivoCredomatic", nombreArchivoCredomatic);
			misession.setAttribute("user", obtenerUsuario().getUsuario());
			misession.setAttribute("idBAC", idBAC);
			misession.setAttribute("media", media);
			misession.setAttribute("idArchivo", idArchivo);
			misession.setAttribute("isReloadFile", isReloadFile);
			misession.setAttribute("listCredomatic", listCredomatic);

			misession.setAttribute("interfaceEstadoCuenta", CBEstadoCuentasController.this);

			cbutil.leerArchivoCredo(format);
		}
	}

	public void leerCredomaticEncabezado(String format) {

		CBEstadoCuentaUtils cbutil = new CBEstadoCuentaUtils();
		int idBAC = 0;
		if (cmbxEntidad.getSelectedItem() != null) {
			idBAC = cmbxEntidad.getSelectedItem().getValue();
		} else {
			Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else {

			misession.setAttribute("user", getUsuario());
			nombreArchivoCredomatic = media.getName();
			misession.setAttribute("nombreArchivoCredomatic", nombreArchivoCredomatic);
			misession.setAttribute("idBAC", idBAC);
			misession.setAttribute("media", media);
			misession.setAttribute("idArchivo", idArchivo);
			misession.setAttribute("isReloadFile", isReloadFile);
			misession.setAttribute("listCredomatic", listCredomatic);
			misession.setAttribute("interfaceEstadoCuenta", CBEstadoCuentasController.this);
			logger.debug("leerCredomaticEncabezado - " + "datos recibidos de la sesion user " + getUsuario());
			logger.debug("leerCredomaticEncabezado - " + "datos recibidos de la sesion nombreArchivoCredomatic "
					+ nombreArchivoCredomatic);
			logger.debug("leerCredomaticEncabezado - " + "datos recibidos de la sesion idArchivo " + idArchivo);
			logger.debug("leerCredomaticEncabezado - " + "datos recibidos de la sesion media " + media);
			logger.debug("leerCredomaticEncabezado - " + "datos recibidos de la sesion isReloadFile " + isReloadFile);
			logger.debug("datos recibidos de la sesion listCredomatic " + listCredomatic.size());

			if (misession.getAttribute("conexion").equals(Tools.SESSION_SV)) {
				logger.debug("leerCredomaticEncabezado()" + " - SESSION : " + Tools.SESSION_SV);
				cbutil.leerCredomaticEncabezado(format,Tools.SESSION_SV);

			}
			if (misession.getAttribute(Constantes.CONEXION).equals(Tools.SESSION_NI)) {
				logger.debug("leerCredomaticEncabezado()" + " - SESSION : " + Tools.SESSION_NI);
				cbutil.leerCredomaticEncabezado(format,Tools.SESSION_NI);

			}
			if (misession.getAttribute(Constantes.CONEXION).equals(Tools.SESSION_PA)) {
				logger.debug("leerCredomaticEncabezado()" + " - SESSION : " + Tools.SESSION_PA);
				cbutil.leerCredomaticEncabezado(format,Tools.SESSION_PA);

			}
			if (misession.getAttribute(Constantes.CONEXION).equals(Tools.SESSION_CR)) {
				logger.debug("leerCredomaticEncabezado()" + " - SESSION : " + Tools.SESSION_CR);
				cbutil.leerCredomaticEncabezadoCR(format);

			}
			if (misession.getAttribute(Constantes.CONEXION).equals(Tools.SESSION_GT)) {
				logger.debug("leerCredomaticEncabezado()" + " - SESSION : " + Tools.SESSION_GT);
				cbutil.leerCredomaticEncabezadoGT(format);

			}
		}

	}

	public void leerCredomaticDetalle(String format) {

		CBEstadoCuentaUtils cbutil = new CBEstadoCuentaUtils();

		int idBAC = 0;
		if (cmbxEntidad.getSelectedItem() != null) {
			idBAC = cmbxEntidad.getSelectedItem().getValue();
		} else {
			Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else {

			nombreArchivoCredomatic = media.getName();
			misession.setAttribute("nombreArchivoCredomatic", nombreArchivoCredomatic);
			misession.setAttribute("user", obtenerUsuario().getUsuario());
			misession.setAttribute("idBAC", idBAC);
			misession.setAttribute("media", media);
			misession.setAttribute("idArchivo", idArchivo);
			misession.setAttribute("isReloadFile", isReloadFile);
			misession.setAttribute("listCredomatic", listCredomatic);

			misession.setAttribute("interfaceEstadoCuenta", CBEstadoCuentasController.this);
			if (misession.getAttribute("conexion").equals(Tools.SESSION_SV)) {
				cbutil.leerCredomaticDetalle(format);

			}
			if (misession.getAttribute("conexion").equals(Tools.SESSION_NI)) {
				cbutil.leerCredomaticDetalle(format);

			}
			if (misession.getAttribute("conexion").equals(Tools.SESSION_PA)) {
				cbutil.leerCredomaticDetalle(format);

			}
			if (misession.getAttribute("conexion").equals(Tools.SESSION_CR)) {
				cbutil.leerCredomaticDetalle(format);

			}
			if (misession.getAttribute("conexion").equals(Tools.SESSION_GT)) {
				cbutil.leerCredomaticDetalleGT(format);

			}

		}

	}

	public static final String UTF8_BOM = "\uFEFF";

	// *modificacion para la carga de archivo OVIDIO SANTOS agrega a version
	// unificada solo para CR
	public void leerArchivoBancoNacional() throws NumberFormatException, ParseException {
		// variables de uso local

		CBEstadoCuentasModel objVisa = null;
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (Constantes.TXT.equals(media.getFormat())) {
			String user = getUsuario();
			// obtiene el id de configuracion
			int idBAC = 0;
			if (cmbxEntidad.getSelectedItem() != null) {
				idBAC = cmbxEntidad.getSelectedItem().getValue();
			} else {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			logger.debug("leerArchivoBancoNacional() - " + "id idBAC: " + idBAC);
			try {
				Reader read = media.getReaderData();
				BufferedReader reader = new BufferedReader(read);
				String line;
				int fila = 0;
				int celda = 0;
				String registro = null;
				while ((line = reader.readLine()) != null) {
					fila++;
					objVisa = new CBEstadoCuentasModel();
					logger.debug("leerArchivoBancoNacional() " + "Leyendo fila: " + line);
					if (line.startsWith(UTF8_BOM)) {
						line = line.substring(1);
					}

					StringTokenizer token = new StringTokenizer(line, "\t");
					objVisa.setCbestadocuentaconfid(idBAC);
					objVisa.setCbestadocuentaarchivosid(idArchivo);

					while (token.hasMoreTokens() && fila > 0) {
						registro = token.nextToken();
						celda++;
						switch (celda) {
						// para el campo NombreCuenta
						case 1:
							objVisa.setDocumento(registro);
							break;
						// para el campo numero de cuenta
						case 2:
							objVisa.setReferencia(registro.trim().replace("'", ""));
							break;
						// para el campo moneda
						case 3:
							objVisa.setMon(registro);
							break;
						// para el campo numero afiliacion
						case 4:
							objVisa.setAfilicacion(registro.trim().replace("'", ""));
							break;
						// para el campo nombre afiliado
						case 5:
							objVisa.setDescripcion(registro);
							break;
						// para el campo numerodeposito
						case 6:
							objVisa.setCodigo_lote(registro);
							break;
						// para el campo tipo tarjeta
						case 7:
							objVisa.setTipo(registro);
							break;
						// para el campo transaccion
						case 8:
							objVisa.setTextoCabDoc(registro);
							break;
						// para el campo numero targeta
						case 9:
							objVisa.setDebito(registro);
							break;
						// para el campo comprobante
						case 10:
							objVisa.setBalance(registro.trim().replace("'", ""));
							break;
						// para el campo Autorizacion
						case 11:
							objVisa.setPropina(registro);
							break;
						// para el campo plan
						case 12:
							objVisa.setImportemd(registro);
							break;
						// para el campo plazo
						case 13:
							objVisa.setImporteml(registro);
							break;
						// para el campo numero de cierre
						case 14:
							objVisa.setNumdocumento(registro.trim().replace("'", ""));
							break;
						// para el campo Fecha
						case 15:
							objVisa.setFechatransaccion(registro);
							break;
						// para el campo valor transaccion
						case 16:
							objVisa.setConsumo(registro);
							break;
						// para el campo comisiontransaccion
						case 17:
							objVisa.setComision(registro);
							break;
						// para el campo ValorRetencion
						case 18:
							objVisa.setRetencion(registro);
							break;
						// para el campo valor renta
						case 19:
							objVisa.setImpuestoturis(registro);
							break;
						// para el campo PorcentajeRetencion
						case 20:
							objVisa.setIvacomision(registro);
							break;
						// para el campo TasaDescuento
						case 21:
							objVisa.setIva(registro);
							break;
						// para el campo conglomerado
						case 22:
							objVisa.setCredito(registro);
							break;
						// para el campo deposito
						case 23:
							objVisa.setLiquido(registro);
							break;
						// para el campo EsTasaCero(S/N)
						case 24:
							objVisa.setDescpago(registro);
							break;
						}
						objVisa.setUsuario(user);
					}
					celda = 0;
					logger.debug(
							"leerArchivoBancoNacional() - fecha a validar en isdate " + objVisa.getFechatransaccion());
					if (isDate(objVisa.getFechatransaccion())) {
						listVisa.add(objVisa);
					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (listVisa.size() <= 0) {
					Messagebox.show("El formato del archivo no es valido, porfavor validar ", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);
					return;
				}
				if (objDao.insertCuentasBancoNacional(listVisa)) {
					logger.debug("leerArchivoBancoNacional() - " + "Ejecuta primer insert visa");
					logger.debug("leerArchivoBancoNacional() - " + "lista en el control envia " + listVisa.size());
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);
					logger.debug("leerArchivoBancoNacional() - " + "inserta BANCO NACIONAL ==> "
							+ objDao.ejecutaSPBancoNacional());
					cambiaEstadoMensaje(true, media.getName());
				} else {
					logger.debug("leerArchivoBancoNacional() - " + "No Ejecuta BANCO NACIONAL");
					Messagebox.show("Error al cargar el archivo seleccionado, favor validar", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);
					cambiaEstadoMensaje(false, media.getName());
				}
				listVisa.clear();
				isReloadFile = false;
			} catch (IOException e) {
				logger.error("leerArchivoBancoNacional() - Error ", e);
			} catch (IllegalArgumentException e) {
				logger.error("leerArchivoBancoNacional() - Error ", e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} else {
			Messagebox.show("El formato del archivo no es valido, porfavor validar ", Constantes.ATENCION,
					Messagebox.OK, Messagebox.EXCLAMATION);
			logger.error("leerArchivoBancoNacional() - el formato no es valido");
		}
	}

	/**
	 * Se leen los archivos para VISA
	 */

	public void leerArchivoVisa() throws NumberFormatException, ParseException {
		// variables de uso local
		boolean isDetalle = false;
		int conDet = 0;

		CBEstadoCuentasModel objVisa = null;
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (Constantes.XLSX.equals(media.getFormat())) {
			String user = getUsuario();
			// obtiene el id de configuracion

			int idBAC = 0;
			if (cmbxEntidad.getSelectedItem() != null) {
				idBAC = cuentasCombo.getCbestadocuentaconfid();
			} else {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			logger.debug("leerArchivoVisa() - " + "id idBAC: " + idBAC);

			// int fila = 0;
			String registro = "";

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			DataFormatter formatter = new DataFormatter();
			try {
				XSSFWorkbook libro = new XSSFWorkbook(OPCPackage.open(media.getStreamData()));
				XSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					// validamos si el sector del archivo pertenece a detalle

					if (row.getRowNum() > 12) {

						objVisa = new CBEstadoCuentasModel();

						for (int celda = 0; celda < 16; celda++) {

							if (row.getCell(celda) == null) {
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Celda
								// null");
								registro = "  ";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}

								if (registro.toUpperCase().trim().equals("DETALLE")) {
									// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"registro
									// de
									// incremento: "+registro);
									isDetalle = true;
									conDet++;
									// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Cambia
									// a detalle:
									// "+registro+" conta: "+conDet);
								}
								if (isDetalle) {
									if (conDet > 0) {

										objVisa.setCbestadocuentaconfid(idBAC);
										objVisa.setCbestadocuentaarchivosid(idArchivo);
										// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
										// de celda
										// detalle: "+celda+" registro:
										// "+registro+" cont:"+conDet);
										switch (celda) {

										case 0:
											// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"celda
											// para
											// afilicacion: "+celda);
											objVisa.setFechacierre(registro);
											break;
										case 1:

											objVisa.setAfilicacion(registro);
											System.out.println("detalle prueba terium" + registro);
											break;
										case 2:
											objVisa.setTerminal(registro);
											break;
										case 3:
											objVisa.setLote(registro);
											break;
										case 4:
											objVisa.setTarjeta(registro);
											break;
										case 5:
											objVisa.setFechaventa(registro);
											break;
										case 6:
											objVisa.setHora(registro);
											break;
										case 7:
											objVisa.setAutorizacion(registro);
											break;
										case 9:
											objVisa.setConsumo(registro);
											break;
										case 10:
											objVisa.setTipotrans(registro);
											break;
										case 11:
											objVisa.setImpturismo(registro);
											break;
										case 12:
											objVisa.setPropina(registro);
											break;
										case 13:
											objVisa.setComision(registro);
											break;
										case 14:
											objVisa.setIva(registro);
											break;
										case 15:
											objVisa.setLiquido(registro);
											break;
										}
										objVisa.setUsuario(user);
									}
								} else {
									// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
									// de celda visa:
									// "+celda+" registro: "+registro+"
									// cont:"+conDet);

									objVisa.setCbestadocuentaconfid(idBAC);
									objVisa.setCbestadocuentaarchivosid(idArchivo);
									switch (celda) {
									case 0:
										objVisa.setFechatransaccion(registro);
										System.out.println("pagos prueba terium" + registro);
										break;
									case 1:
										objVisa.setAfilicacion(registro);
										break;
									case 2:
										objVisa.setTipo(registro);
										break;
									case 3:
										objVisa.setReferencia(registro);
										break;
									case 4:
										objVisa.setLote(registro);
										break;
									case 5:
										objVisa.setConsumo(registro);
										break;
									case 6:
										objVisa.setImpuestoturis(registro);
										break;
									case 7:
										objVisa.setPropina(registro);
										break;
									case 9:
										objVisa.setIva(registro);
										break;
									case 10:
										objVisa.setComision(registro);
										break;
									case 11:
										objVisa.setIvacomision(registro);
										break;
									case 12:
										objVisa.setLiquido(registro);
										break;
									case 13:
										objVisa.setRetencion(registro);
										break;
									case 14:
										objVisa.setDocumento(registro);
										break;
									case 15:
										objVisa.setDescpago(registro);
										break;
									}
									objVisa.setUsuario(user);
								}
							}

						}
						if (isDetalle) {

							if (isDate(objVisa.getFechatransaccion())) {
								listVisaDet.add(objVisa);
							} else {
								logger.debug(
										"leerArchivoVisa()() - " + "No agrega detalle: " + objVisa.getAfilicacion());
							}
						} else {
							if (isDate(objVisa.getFechatransaccion())) {
								listVisa.add(objVisa);
							} else {
								logger.debug("leerArchivoVisa() - " + "No agrega visa normal");
							}
						}
					}

				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				boolean insert = objDao.insertCuentasVisa(listVisa);
				if (insert) {
					logger.debug("leerArchivoVisa() - " + "Ejecuta primer insert visa");
				} else {
					logger.debug("leerArchivoVisa() - " + "No Ejecuta primer insert visa");
				}
				if (insert && objDao.insertCuentasVisaDet(listVisaDet)) {
					logger.debug("leerArchivoVisa() - " + "ejecuta segundo insert");
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);
					logger.debug("leerArchivoVisa() - " + "inserta visa ==> " + objDao.ejecutaSPVisa());
					cambiaEstadoMensaje(true, media.getName());
				} else {
					Messagebox.show("Error al cargar el archivo seleccionado, favor validar", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);
					cambiaEstadoMensaje(false, media.getName());
				}
				listVisa.clear();
				listVisaDet.clear();
				isDetalle = false;
				conDet = 0;
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				logger.error("leerArchivoVisa()() - Error ", e);
			} catch (IOException e) {
				logger.error("leerArchivoVisa()() - Error ", e);
			} catch (IllegalArgumentException e) {
				logger.error("leerArchivoVisa()() - Error ", e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} else if (Constantes.XLS.equals(media.getFormat())) {
			String user = getUsuario();

			// obtiene el id de configuracion

			int idBAC = 0;
			if (cmbxEntidad.getSelectedItem() != null) {
				idBAC = cmbxEntidad.getSelectedItem().getValue();
			} else {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			logger.debug("leerArchivoVisa() - " + "id idBAC: " + idBAC);

			// int fila = 0;
			String registro = "";
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			DataFormatter formatter = new DataFormatter();
			try {
				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {

					HSSFRow row = (HSSFRow) rows.next();

					if (row.getRowNum() > 12) {

						objVisa = new CBEstadoCuentasModel();

						for (int celda = 0; celda <= 17; celda++) {

							if (row.getCell(celda) == null) {
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Celda
								// null");
								registro = "  ";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}

								if (registro.toUpperCase().trim().equals("DETALLE")) {
									// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"registro
									// de
									// incremento: "+registro);
									isDetalle = true;
									conDet++;
									// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Cambia
									// a detalle:
									// "+registro+" conta: "+conDet);
								}
								if (isDetalle) {
									if (conDet > 0) {

										objVisa.setCbestadocuentaconfid(idBAC);
										objVisa.setCbestadocuentaarchivosid(idArchivo);
										// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
										// de celda
										// detalle: "+celda+" registro:
										// "+registro+" cont:"+conDet);
										switch (celda) {

										case 0:
											// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"celda
											// para
											// afilicacion: "+celda);
											objVisa.setFechacierre(registro);
											break;
										case 1:

											objVisa.setAfilicacion(registro);
											System.out.println("detalle prueba terium" + registro);
											break;
										case 2:
											objVisa.setTerminal(registro);
											break;
										case 3:
											objVisa.setLote(registro);
											break;
										case 4:
											objVisa.setTarjeta(registro);
											break;
										case 5:
											objVisa.setFechaventa(registro);
											break;
										case 6:
											objVisa.setHora(registro);
											break;
										case 7:
											objVisa.setAutorizacion(registro);
											break;
										case 9:
											objVisa.setConsumo(registro);
											break;
										case 10:
											objVisa.setTipotrans(registro);
											break;
										case 11:
											objVisa.setImpturismo(registro);
											break;
										case 12:
											objVisa.setPropina(registro);
											break;
										case 13:
											objVisa.setComision(registro);
											break;
										case 14:
											objVisa.setIva(registro);
											break;
										case 15:
											objVisa.setLiquido(registro);
											break;
										}
										objVisa.setUsuario(user);
									}
								} else {
									// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
									// de celda visa:
									// "+celda+" registro: "+registro+"
									// cont:"+conDet);

									objVisa.setCbestadocuentaconfid(idBAC);
									objVisa.setCbestadocuentaarchivosid(idArchivo);
									switch (celda) {
									case 0:
										objVisa.setFechatransaccion(registro);
										System.out.println("pagos prueba terium" + registro);
										break;
									case 1:
										objVisa.setAfilicacion(registro);
										break;
									case 2:
										objVisa.setTipo(registro);
										break;
									case 3:
										objVisa.setReferencia(registro);
										break;
									case 4:
										objVisa.setLote(registro);
										break;
									case 5:
										objVisa.setConsumo(registro);
										break;
									case 6:
										objVisa.setImpuestoturis(registro);
										break;
									case 7:
										objVisa.setPropina(registro);
										break;
									case 9:
										objVisa.setIva(registro);
										break;
									case 10:
										objVisa.setComision(registro);
										break;
									case 11:
										objVisa.setIvacomision(registro);
										break;
									case 12:
										objVisa.setLiquido(registro);
										break;
									case 13:
										objVisa.setRetencion(registro);
										break;
									case 14:
										objVisa.setDocumento(registro);
										break;
									case 15:
										objVisa.setDescpago(registro);
										break;
									}
									objVisa.setUsuario(user);
								}
							}

						}
						if (isDetalle) {
							if (isDate(objVisa.getFechacierre())) {
								listVisaDet.add(objVisa);
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Agrega
								// detalle:
								// "+objVisa.getAfilicacion()+" fecha:
								// "+objVisa.getFechacierre());
							} else {
								logger.debug("No agrega detalle");

							}
						} else {
							if (isDate(objVisa.getFechatransaccion())) {
								listVisa.add(objVisa);
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"agrega
								// visa normal");
							} else {
								logger.debug("No agrega visa normal");
							}
						}
					}

				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				boolean insert = objDao.insertCuentasVisa(listVisa);
				if (insert) {
					logger.debug("leerArchivoVisa() - " + "Ejecuta primer insert visa");
				} else {
					logger.debug("leerArchivoVisa() - " + "No Ejecuta primer insert visa");

				}
				if (insert && objDao.insertCuentasVisaDet(listVisaDet)) {
					logger.debug("leerArchivoVisa() - " + "ejecuta segundo insert");
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);
					logger.debug("leerArchivoVisa() - " + "inserta visa ==> " + objDao.ejecutaSPVisa());
					cambiaEstadoMensaje(true, media.getName());
				} else {
					Messagebox.show(
							"Error al cargar el archivo seleccionado, "
									+ "favor validar si no se encuentra vacOo o el formato es correcto",
							Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
					cambiaEstadoMensaje(false, media.getName());
				}
				listVisa.clear();
				;
				listVisaDet.clear();
				;
				isDetalle = false;
				conDet = 0;
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				logger.error("leerArchivoVisa() - Error ", e);
			} catch (IOException e) {
				logger.error("leerArchivoVisa() - Error ", e);
			} catch (IllegalArgumentException e) {
				logger.error("leerArchivoVisa() - Error ", e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			}

		} else {
			Messagebox.show("No se ha cargado un archivo excel... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
		}
	}

	/**
	 * Leemos el archivo para sociedad en formato TXT
	 */
	// modificado ovidio Santos 25/04/2017
	public void leerArchivoSociedad() {
		CBEstadoCuentasModel objSociedad = null;
		int idBAC = 0;
		if (cmbxEntidad.getSelectedItem() != null) {
			idBAC = cmbxEntidad.getSelectedItem().getValue();
		} else {
			Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (Constantes.TXT.equals(media.getFormat())) {
			try {
				String user = getUsuario();
				Reader read = media.getReaderData();
				BufferedReader reader = new BufferedReader(read);
				String line;
				int fila = 0;
				int celda = 0;
				String registro = null;
				while ((line = reader.readLine()) != null) {
					fila++;
					objSociedad = new CBEstadoCuentasModel();
					StringTokenizer token = new StringTokenizer(line, "|");
					objSociedad.setCbestadocuentaconfid(idBAC);
					objSociedad.setCbestadocuentaarchivosid(idArchivo);
					// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fila:
					// "+fila+" - numero de
					// celdadetalle: "+celda+" - registro: "+registro);
					while (token.hasMoreTokens() && fila > 0) {
						registro = token.nextToken();

						celda++;
						switch (celda) {
						// mapeo de datos
						case 1:
							objSociedad.setCuenta(registro.trim());
							break;
						case 2:
							objSociedad.setReferencia(registro.trim());
							;
							break;
						case 3:
							objSociedad.setAsignacion(registro.trim());
							break;
						case 4:
							objSociedad.setClase(registro.trim());
							break;
						case 5:
							objSociedad.setNumdocumento(registro.trim());
							break;
						case 6:
							objSociedad.setFechavalor(changeDate(registro.trim()));
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fecha
							// valor " +
							// registro.trim());
							break;
						case 7:
							objSociedad.setFechacontab(changeDate(registro.trim()));
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fecha
							// contab " +
							// registro.trim());
							break;
						case 8:
							objSociedad.setTexto(registro.trim());
							break;
						case 9:
							objSociedad.setMon(registro.trim());
							break;
						case 10:
							objSociedad.setImportemd(registro.trim());
							break;
						case 11:
							objSociedad.setImporteml(registro.trim());
							break;
						case 12:
							objSociedad.setDocucomp(registro.trim());
							break;
						case 13:
							objSociedad.setCtacp(registro.trim());
							break;

						// nuevos
						case 14:
							objSociedad.setTextoCabDoc(registro.trim());
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"texto
							// cab doc "
							// +registro.trim());
							break;

						case 15:
							objSociedad.setAnulacion(registro.trim());
							break;
						case 16:
							objSociedad.setCt(registro.trim());
							break;
						case 17:
							objSociedad.setEjercicioMes(changeDate(registro.trim()));
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"ejercicio
							// mes " +
							// registro.trim());
							break;
						// para el campo registrados
						case 18:
							objSociedad.setFechaIngresos(changeDate(registro.trim()));
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fecha
							// de ingresos campo
							// registrados " + registro.trim());
							break;
						// para el campo so
						case 19:
							objSociedad.setIdentificador(registro.trim());
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"identificador
							// so " +
							// registro.trim());
							break;
						case 20:
							objSociedad.setFechaDoc(changeDate(registro.trim()));
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fecha
							// doc " +
							// registro.trim());
							break;
						case 21:
							objSociedad.setLibMayor(registro.trim());
							break;
						case 22:
							objSociedad.setPeriodo(registro.trim());
							break;
						case 23:
							objSociedad.setImporteMl3(registro.trim());
							break;
						case 24:
							objSociedad.setUsuarioSociedad(registro.trim());
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"usuario
							// sociedad " +
							// registro.trim());
							break;
						/*
						 * Nuevos campos para el mapeo de extracto
						 */
						case 25:
							objSociedad.setMl(registro.trim());
							break;
						case 26:
							objSociedad.setMl3(registro.trim());
							;
							break;
						case 27:
							objSociedad.setCompens(changeDate(registro.trim()));
							break;
						case 28:
							objSociedad.setAfun(registro.trim());
							break;
						case 29:
							objSociedad.setArea_funcional(registro.trim());
							break;
						case 30:
							objSociedad.setCe_coste(registro.trim());
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fecha
							// valor " +
							// registro.trim());
							break;
						case 31:
							objSociedad.setCodigo_transaccion(registro.trim());
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fecha
							// contab " +
							// registro.trim());
							break;
						case 32:
							objSociedad.setClv_ref_cabecera(registro.trim());
							break;
						case 33:
							objSociedad.setBco_prp(registro.trim());
							break;
						case 34:
							objSociedad.setOrden(registro.trim());
							break;
						case 35:
							objSociedad.setTp_camb_ef(registro.trim());
							// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"leyendo
							// Tp_camb_ef : " + registro.trim());
							break;
						case 36:
							objSociedad.setTpbc(registro.trim());
							break;

						}

					}
					celda = 0;
					objSociedad.setUsuario(user);
					if (isDate(objSociedad.getFechavalor())) {
						listSociedad.add(objSociedad);
						// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"listado
						// " + listSociedad);
					}
				}
				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();

				if (objDao.insertCuentaSociedad(listSociedad)) {
					Messagebox.show("Los datos han sido ingresados de forma correcta", Constantes.ATENCION,
							Messagebox.OK, Messagebox.INFORMATION);
					cambiaEstadoMensaje(true, media.getName());
					// DESCOMENTA OVIDIO SANTOS 13072017
					boolean ex = objDao.ejecutaSPExtracto();

					logger.debug("leerArchivoSociedad() - EJECUTA SP  ===> ");
					logger.debug("leerArchivoSociedad() - inserta Sociedad ===> ");

				} else {
					Messagebox.show("Error al cargar archivo seleccionado, favor validar", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);
					cambiaEstadoMensaje(false, media.getName());
				}
				listSociedad.clear();
				isReloadFile = false;
				fila = 0;
				reader.close();

			} catch (IOException e) {
				Messagebox.show("Error al cargar archivo seleccionado, favor validar", Constantes.ATENCION,
						Messagebox.OK, Messagebox.EXCLAMATION);
				cambiaEstadoMensaje(false, media.getName());
				logger.error("leerArchivoSociedad() - Error  ", e);
			} catch (Exception e) {
				Messagebox.show("Se ha producido un error al cargar el archivo.", Constantes.ATENCION, Messagebox.OK,
						Messagebox.ERROR);
				cambiaEstadoMensaje(false, media.getName());
				logger.error("leerArchivoSociedad() - Error  ", e);
			}
		} else {
			Messagebox.show("El formato del archivo no es correcto, favor seleccionar otro!", Constantes.ATENCION,
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

	}

	/**
	 * Lee el archivo para Sociedad
	 */
	public void leerArchivoSociedad2() {
		CBEstadoCuentasModel objSociedad = null;
		DateFormat df = new SimpleDateFormat(Constantes.FORMATO_FECHA1);
		DataFormatter formatter = new DataFormatter();
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		} else if (Constantes.XLSX.equals(media.getFormat())) {
			// obtiene el id de configuracion

			int idBAC = 0;
			if (cmbxEntidad.getSelectedItem() != null) {
				idBAC = cmbxEntidad.getSelectedItem().getValue();
			} else {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			logger.debug("id idBAC: " + idBAC);

			// int fila = 0;
			String registro = "";
			try {
				// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"String
				// data: "+media.getByteData());
				InputStream data = new ByteArrayInputStream(media.getByteData());

				XSSFWorkbook libro = new XSSFWorkbook(OPCPackage.open(data));

				XSSFSheet hoja = libro.getSheetAt(0);

				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					// validamos si el sector del archivo pertenece a detalle

					if (row.getRowNum() > 5) {

						objSociedad = new CBEstadoCuentasModel();

						for (int celda = 2; celda < 16; celda++) {

							if (row.getCell(celda) == null) {
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Celda
								// null");
								registro = "  ";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}

								objSociedad.setCbestadocuentaconfid(idBAC);
								objSociedad.setCbestadocuentaarchivosid(idArchivo);
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
								// de celda detalle:
								// "+celda+" registro: "+registro);
								switch (celda) {
								case 2:
									objSociedad.setCuenta(registro);
									break;
								case 3:
									objSociedad.setReferencia(registro);
									;
									break;
								case 5:
									objSociedad.setAsignacion(registro);
									break;
								case 6:
									objSociedad.setClase(registro);
									break;
								case 7:
									objSociedad.setNumdocumento(registro);
									break;
								case 8:
									objSociedad.setFechavalor(registro);
									break;
								case 9:
									objSociedad.setFechacontab(registro);
									break;
								case 10:
									objSociedad.setTexto(registro);
									break;
								case 11:
									objSociedad.setMon(registro);
									break;
								case 12:
									objSociedad.setImportemd(registro);
									break;
								case 13:
									objSociedad.setImporteml(registro);
									break;
								case 14:
									objSociedad.setDocucomp(registro);
									break;
								case 15:
									objSociedad.setCtacp(registro);
									break;
								}
								objSociedad.setUsuario(getUsuario());
							}
						}
						listSociedad.add(objSociedad);
					}
				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentaSociedad(listSociedad)) {
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);
					cambiaEstadoMensaje(true, media.getName());
				} else {
					Messagebox.show("Error al cargar archivo seleccionado, favor validar", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);
					cambiaEstadoMensaje(false, media.getName());
				}
				listSociedad.clear();
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				logger.error("leerArchivoSociedad2() - Error ", e);
			} catch (IOException e) {
				logger.error("leerArchivoSociedad2() - Error ", e);
			} catch (IllegalArgumentException e) {
				logger.error("leerArchivoSociedad2() - Error ", e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);

			}
		} else if (Constantes.XLS.equals(media.getFormat())) {
			String user = getUsuario();

			// obtiene el id de configuracion

			int idBAC = 0;
			if (cmbxEntidad.getSelectedItem() != null) {
				idBAC = cmbxEntidad.getSelectedItem().getValue();
			} else {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}

			logger.debug("leerArchivoSociedad2() - " + "id idBAC: " + idBAC);

			String registro = "";
			try {

				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(OPCPackage.open(media.getStreamData()));
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {

					HSSFRow row = (HSSFRow) rows.next();

					if (row.getRowNum() > 12) {

						objSociedad = new CBEstadoCuentasModel();

						for (int celda = 0; celda <= 17; celda++) {

							if (row.getCell(celda) == null) {
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Celda
								// null");
								registro = "  ";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}

								objSociedad.setCbestadocuentaconfid(idBAC);
								objSociedad.setCbestadocuentaarchivosid(idArchivo);
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
								// de celda detalle:
								// "+celda+" registro: "+registro);
								switch (celda) {
								case 2:
									objSociedad.setCuenta(registro);
									break;
								case 3:
									objSociedad.setReferencia(registro);
									;
									break;
								case 5:
									objSociedad.setAsignacion(registro);
									break;
								case 6:
									objSociedad.setClase(registro);
									break;
								case 7:
									objSociedad.setNumdocumento(registro);
									break;
								case 8:
									objSociedad.setFechavalor(registro);
									break;
								case 9:
									objSociedad.setFechacontab(registro);
									break;
								case 10:
									objSociedad.setTexto(registro);
									break;
								case 11:
									objSociedad.setMon(registro);
									break;
								case 12:
									objSociedad.setImportemd(registro);
									break;
								case 13:
									objSociedad.setImporteml(registro);
									break;
								case 14:
									objSociedad.setDocucomp(registro);
									break;
								case 15:
									objSociedad.setCtacp(registro);
									break;
								}
								objSociedad.setUsuario(user);
							}
						}
						listSociedad.add(objSociedad);
					}

				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertCuentaSociedad(listSociedad)) {
					Messagebox.show(
							"Los datos para: " + media.getName() + " han sido ingresados de forma " + "correcta",
							Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);

					logger.debug("inserta Sociedad");

				} else {
					Messagebox.show("No se a cargado la informacion de manera correcta, favor validar el " + "archivo "
							+ media.getName(), Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);
				}
				listSociedad.clear();
				;
				cambiaEstadoMensaje(true, media.getName());
				isReloadFile = false;
			} catch (IOException e) {
				logger.error(" error : ", e);
			} catch (IllegalArgumentException e) {
				logger.error(" error : ", e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InvalidFormatException e) {
				logger.error(" error : ", e);
			}

		} else {
			Messagebox.show("No se ha cargado un archivo excel... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
	}

	public void segmentarCadena(String line) {

	}

	/**
	 * Leemos el archivo para Comercial en formato TXT
	 *
	 */
	// OVIDIO SANTOS 19042017

	/**
	 * Modify 10/ july /2017
	 * 
	 * @author Omar
	 */
	public void leerArchivoSistemaComercial() {

		List<CBPagosModel> listaComercial = new ArrayList<CBPagosModel>();
		CBPagosModel objComercial = null;

		int idBAC = 0;

		if (cmbxEntidad.getSelectedItem() != null) {
			idBAC = cmbxEntidad.getSelectedItem().getValue();

		} else {
			Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;

		} else if (Constantes.TXT.equals(media.getFormat())) {
			try {

				String user = getUsuario();

				Reader read = media.getReaderData();
				BufferedReader reader = new BufferedReader(read);

				String line;
				int fila = 0;

				while ((line = reader.readLine()) != null) {
					if (fila > 0) {

						objComercial = new CBPagosModel();
						objComercial.setCBPagosId(idBAC);

						objComercial.setCbestadocuentaconfid(idBAC);
						objComercial.setCbestadocuentaarchivosid(idArchivo);

						int contador = 0;
						@SuppressWarnings("unused")
						int colNum = 0;

						Iterable<String> parts = Splitter.on('|').split(line);

						for (@SuppressWarnings("unused")
						String column : parts) {
							colNum++;
						}
						for (String token : parts) {
							contador++;

							switch (contador) {

							case 1:
								objComercial.setFecEfectividad(token.trim().replace("//", "/"));
								break;
							case 2:
								objComercial.setNumSecuenci(token.trim());
								break;
							case 3:
								objComercial.setCodCliente(token.trim());
								break;
							case 4:
								objComercial.setTelefono(token.trim());
								break;
							case 5:
								objComercial.setImpPago(token.trim());
								break;
							case 6:
								objComercial.setCodCaja(token.trim());
								break;
							case 7:
								objComercial.setDesPago(token.trim());
								break;
							case 8:
								objComercial.setTipo(token.trim());
								// System.out.println("cod tipo" + objComercial.getTipo());
								break;
							case 9:
								objComercial.setEstadoConciliado(token.trim());
								break;
							case 10:
								objComercial.setNumConciliacion(token.trim());
								break;
							case 11:
								objComercial.setFecha(token.trim().replace("//", "/"));
								break;

							// agregados Ovidio

							case 12:
								objComercial.setCbBancoAgenciaConfrontaId(token.trim());
								break;
							case 13:
								objComercial.setFechaTransaccional(token.trim());
								break;

							case 14:
								objComercial.setNonCliente(token.trim());
								break;
							case 15:
								objComercial.setCodCliclo(token.trim());
								break;

							case 16:
								objComercial.setTransaccion(token.trim());
								break;
							case 17:
								objComercial.setTipoTransaccion(token.trim());
								break;
							case 18:
								objComercial.setTipoMovCaja(token.trim());
								break;
							case 19:
								objComercial.setTipoValor(token.trim());
								break;
							case 20:
								objComercial.setNomUsuarora(token.trim());
								break;
							case 21:
								objComercial.setCodBanco(token.trim());
								break;

							case 22:
								objComercial.setCodOficina(token.trim());
								break;
							case 23:
								objComercial.setCodSegmento(token.trim());
								break;
							case 24:
								objComercial.setDesSegmento(token.trim());
								break;
							case 25:
								objComercial.setCodMoneda(token.trim());
								// System.out.println("cod moneda" + objComercial.getCodMoneda());
								break;

							}
						}
						objComercial.setCreadoPor(user);
						if (isDate(objComercial.getFecEfectividad())) {
							listaComercial.add(objComercial);
						}
					}
					fila++;
				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();

				if (objDao.insertCuentaComercial(listaComercial)) {
					Messagebox.show("Los datos han sido ingresados de forma correcta", Constantes.ATENCION,
							Messagebox.OK, Messagebox.INFORMATION);
					cambiaEstadoMensaje(true, media.getName());

				} else {
					Messagebox.show("Error al cargar archivo seleccionado, favor validar", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);

					cambiaEstadoMensaje(false, media.getName());
					isReloadFile = false;
				}

				/**
				 * Agregado por Carlos Godinez - Qitcorp - 13/05/2017
				 * 
				 * Levantamiento de hilo para carga de liquidaciones luego de carga de archivo
				 * de pagos
				 * 
				 */

				if (objComercial != null) {
					logger.debug("Fecha de pagos de archivo cargado = " + objComercial.getFecEfectividad());
					Runnable hiloLiquidaciones = new CBProcesaLiquidacionesThread(objComercial.getFecEfectividad(), 1,
							usuario);
					Thread hilo = new Thread(hiloLiquidaciones);
					hilo.start();
				}

				/**
				 * Fin Agregado por Carlos Godinez - Qitcorp - 13/05/2017
				 */

				reader.close();
			} catch (IOException e) {
				Messagebox.show("Error al cargar archivo seleccionado, favor validar", Constantes.ATENCION,
						Messagebox.OK, Messagebox.EXCLAMATION);
				cambiaEstadoMensaje(false, media.getName());
				logger.error(" Error ", e);
			}
		} else {
			Messagebox.show("El formato del archivo no es correcto, favor seleccionar otro!", Constantes.ATENCION,
					Messagebox.OK, Messagebox.EXCLAMATION);
		}

	}

	// cambios Gerbert
	public void leerArchivoReporteCierreCaja() throws NumberFormatException, ParseException {

		CBEstadoCuentasModel objReporte = null;
		DateFormat df = new SimpleDateFormat(Constantes.FORMATO_FECHA1);
		DataFormatter formatter = new DataFormatter();
		if (media == null) {
			Messagebox.show("Primero debe seleccionar un archivo... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			System.out.println("entra al primer if de del metodo leer archivo reporte cierre caja");
			return;
		} else if (Constantes.XLSX.equals(media.getFormat())) {
			// obtiene el id de configuracion

			int idBAC = 0;
			if (cmbxEntidad.getSelectedItem() != null) {
				idBAC = cmbxEntidad.getSelectedItem().getValue();
			} else {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			logger.debug("id idBAC: " + idBAC);

			// int fila = 0;
			String registro = "";
			try {
				// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"String
				// data: "+media.getByteData());
				InputStream data = new ByteArrayInputStream(media.getByteData());

				XSSFWorkbook libro = new XSSFWorkbook(OPCPackage.open(data));

				XSSFSheet hoja = libro.getSheetAt(0);

				Iterator<Row> rows = hoja.iterator();
				while (rows.hasNext()) {
					XSSFRow row = (XSSFRow) rows.next();
					// validamos si el sector del archivo pertenece a detalle

					if (row.getRowNum() > 5) {

						objReporte = new CBEstadoCuentasModel();

						for (int celda = 1; celda < 13; celda++) {

							if (row.getCell(celda) == null) {
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Celda
								// null");
								registro = "  ";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								}

								objReporte.setCbestadocuentaconfid(idBAC);
								objReporte.setCbestadocuentaarchivosid(idArchivo);
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
								// de celda detalle:
								// "+celda+" registro: "+registro);
								switch (celda) {
								case 1:
									objReporte.setFecha_solicitud(registro);
									break;
								case 2:
									objReporte.setCaso(Integer.parseInt(registro));
									;
									break;
								case 3:
									objReporte.setEstado(registro);
									break;
								case 4:
									objReporte.setCap(registro);
									break;
								case 5:
									objReporte.setDictamen_tersoreria(registro);
									break;
								case 6:
									objReporte.setSolicitante(registro);
									break;
								case 7:
									objReporte.setTotalgeneralcolones(registro);
									break;
								case 8:
									objReporte.setTotalgeneralvalores(registro);
									break;
								case 9:
									objReporte.setFila(Integer.parseInt(registro));
									break;
								case 10:
									objReporte.setBoleta_deposito(registro);
									break;
								case 11:
									objReporte.setMoneda(registro);
									break;
								case 12:
									objReporte.setValor_tipo_cambio(registro);
									break;
								}
								objReporte.setUsuario(getUsuario());
							}
						}
						listCierreCaja.add(objReporte);
					}
				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertReporteCierreCaja(listCierreCaja)) {
					Messagebox.show(
							"Los datos han sido ingresados de forma correcta para el archivo: " + media.getName(),
							Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);

					cambiaEstadoMensaje(true, media.getName());
				} else {
					Messagebox.show("Error al cargar archivo seleccionado, favor validar", Constantes.ATENCION,
							Messagebox.OK, Messagebox.EXCLAMATION);
					cambiaEstadoMensaje(false, media.getName());
				}
				listCierreCaja.clear();
				isReloadFile = false;
			} catch (InvalidFormatException e) {
				logger.error("Error : ", e);
			} catch (IOException e) {
				logger.error("Error : ", e);
			} catch (IllegalArgumentException e) {
				logger.error("Error : ", e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);

			}
		} else if (Constantes.XLS.equals(media.getFormat())) {
			String user = getUsuario();

			// obtiene el id de configuracion

			int idBAC = 0;
			if (cmbxEntidad.getSelectedItem() != null) {
				idBAC = cmbxEntidad.getSelectedItem().getValue();
				System.out.println("entra a este if donde valida si el archivo es excel");
			} else {
				Messagebox.show("Debe seleccionar una entidad ", Constantes.ATENCION, Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			logger.debug("id idBAC: " + idBAC);

			String registro = "";
			try {

				/*
				 * HSSFWorkbook libro = (HSSFWorkbook)
				 * WorkbookFactory.create(OPCPackage.open(media.getStreamData())); HSSFSheet
				 * hoja = libro.getSheetAt(1); Iterator<Row> rows = hoja.iterator();
				 */

				HSSFWorkbook libro = (HSSFWorkbook) WorkbookFactory.create(media.getStreamData());
				HSSFSheet hoja = libro.getSheetAt(0);
				Iterator<Row> rows = hoja.iterator();

				while (rows.hasNext()) {
					System.out.println("entra a este while");
					HSSFRow row = (HSSFRow) rows.next();

					if (row.getRowNum() >= 1) {

						objReporte = new CBEstadoCuentasModel();

						for (int celda = 0; celda <= 13; celda++) {

							if (row.getCell(celda) == null) {
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"Celda
								// null");
								registro = "  ";
							} else {
								switch (row.getCell(celda).getCellType()) {
								case Cell.CELL_TYPE_NUMERIC:
									if (DateUtil.isCellDateFormatted(row.getCell(celda))) {
										registro = df.format(row.getCell(celda).getDateCellValue());
									} else {
										registro = formatter.formatCellValue(row.getCell(celda));
									}
									break;
								case Cell.CELL_TYPE_STRING:
									registro = formatter.formatCellValue(row.getCell(celda));
									break;
								/*
								 * case Cell.: registro = formatter.formatCellValue(row.getCell(celda)); break;
								 */
								}

								objReporte.setCbestadocuentaconfid(idBAC);
								objReporte.setCbestadocuentaarchivosid(idArchivo);
								// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"numero
								// de celda detalle:
								// "+celda+" registro: "+registro);
								switch (celda) {
								case 0:
									objReporte.setFecha_solicitud(registro);
									System.out.println("valor de la celda 1: " + celda);
									System.out.println("valor del registro 1: " + registro);
									break;
								case 1:
									objReporte.setCaso(Integer.parseInt(registro));
									System.out.println("valor de la celda 2: " + celda);
									System.out.println("valor del registro 2: " + registro);
									;
									break;
								case 2:
									objReporte.setEstado(registro);
									System.out.println("valor de la celda 3: " + celda);
									System.out.println("valor del registro 3: " + registro);
									break;
								case 3:
									objReporte.setCap(registro);
									System.out.println("valor de la celda 4: " + celda);
									System.out.println("valor del registro 4: " + registro);
									break;
								case 4:
									objReporte.setDictamen_tersoreria(registro);
									System.out.println("valor de la celda 5: " + celda);
									System.out.println("valor del registro 5: " + registro);
									break;
								case 5:
									objReporte.setSolicitante(registro);
									System.out.println("valor de la celda 6: " + celda);
									System.out.println("valor del registro 6: " + registro);
									break;
								case 6:
									objReporte.setTotalgeneralcolones(registro);
									System.out.println("valor de la celda 7: " + celda);
									System.out.println("valor del registro 7: " + registro);
									break;
								case 7:
									objReporte.setTotalgeneralvalores(registro);
									System.out.println("valor de la celda 8: " + celda);
									System.out.println("valor del registro 8: " + registro);
									break;
								case 8:
									objReporte.setFila(Integer.parseInt(registro));
									System.out.println("valor de la celda 9: " + celda);
									System.out.println("valor del registro 9: " + registro);
									break;
								case 9:
									objReporte.setBoleta_deposito(registro);
									System.out.println("valor de la celda 10: " + celda);
									System.out.println("valor del registro 10: " + registro);
									break;
								case 10:
									objReporte.setMoneda(registro);
									System.out.println("valor de la celda 11: " + celda);
									System.out.println("valor del registro 11: " + registro);
									break;
								case 11:
									objReporte.setValor_tipo_cambio(registro);
									System.out.println("valor de la celda 12: " + celda);
									System.out.println("valor del registro 12: " + registro);
									break;

								}
								objReporte.setUsuario(user);
							}
						}
						listCierreCaja.add(objReporte);
					}

				}

				CBEstadoCuentaDAO objDao = new CBEstadoCuentaDAO();
				if (objDao.insertReporteCierreCaja(listCierreCaja)) {
					Messagebox.show(
							"Los datos para: " + media.getName() + " han sido ingresados de forma " + "correcta",
							Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);
					logger.debug("inserta Sociedad");

				} else {
					Messagebox.show("No se a cargado la informacion de manera correcta, favor validar el " + "archivo "
							+ media.getName(), Constantes.ATENCION, Messagebox.OK, Messagebox.INFORMATION);
				}
				listCierreCaja.clear();
				;
				cambiaEstadoMensaje(true, media.getName());
				isReloadFile = false;
			} catch (IOException e) {
				logger.error("Error : ", e);
			} catch (IllegalArgumentException e) {
				logger.error("Error : ", e);
				Messagebox.show("El archivo cargado contiene errores o esta daOado, favor validarlo",
						Constantes.ATENCION, Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InvalidFormatException e) {
				logger.error("Error : ", e);
			}

		} else {
			Messagebox.show("No se ha cargado un archivo excel... ", Constantes.ATENCION, Messagebox.OK,
					Messagebox.EXCLAMATION);
			return;
		}
	}

	/**
	 * Validamos si la cadena ingresada es numerica o no
	 */
	public boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena.trim());
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Validamos si el string enviado es fecha
	 */
	@SuppressWarnings("unused")
	public boolean isDate(String fecha) {
		DateFormat format = new SimpleDateFormat(Constantes.FORMATO_FECHA1);

		Date fec;
		try {
			fec = format.parse(fecha);
			return true;
		} catch (ParseException e) {
			logger.error("Error : ", e);
			return false;
		} catch (NullPointerException e) {
			logger.error("Error : ", e);
			return false;
		}
	}

	/**
	 * Credomatic Validamos si el string enviado es fecha
	 */
	public boolean isDateCredo(String fecha) {
		DateFormat format = new SimpleDateFormat("mm/DD/yy");
		@SuppressWarnings("unused")
		Date fec;
		try {
			fec = format.parse(fecha);
			// Logger.getLogger(CBEstadoCuentasController.class.getName()).log(Level.INFO,"fecha
			// parseada: " + fec);
			return true;
		} catch (ParseException e) {
			logger.error("isDateCredo() - Error : ", e);
			return false;
		} catch (NullPointerException e) {
			logger.error("isDateCredo() - Error : ", e);
			return false;
		}
	}

	/**
	 * Credomatic Validamos si el string enviado es fecha
	 */
	public boolean isDateCredoEncabezado(String fecha) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		@SuppressWarnings("unused")
		Date fec;
		try {
			fec = format.parse(fecha);
			return true;
		} catch (ParseException e) {
			logger.error("isDateCredoEncabezado() - Error : ", e);
			return false;
		} catch (NullPointerException e) {
			logger.error("isDateCredoEncabezado() - Error : ", e);
			return false;
		}
	}

	/**
	 * Cambiamos el formato de la fecha
	 */
	public String changeDate(String fecha) {
		String result = "";
		try {
			result = fecha.replace(".", "/");
		} catch (Exception e) {
			logger.error("isDateCredoEncabezado() - Error : ", e);
		}
		return result;
	}

	/**
	 * Llenamos los campos para VISA pago
	 */
	public void cambiaEstadoMensaje(boolean bandera, String mensaje) {
		if (bandera) {
			lblMensaje.setValue("Archivo cargado correctamente: " + mensaje);
			lblMensaje.setStyle("color:green;");
			imgEstatus.setSrc("img/ok.png");
			logger.debug("cambiaEstadoMensaje() - cambia estado");
		} else {
			lblMensaje.setValue("Ocurrio un error: " + mensaje);
			lblMensaje.setStyle("color:red;");
			imgEstatus.setSrc("img/rojo.png");
		}
	}

	/*
	 * Getter and Setter
	 */

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void seleccionaVuno(Media param, Combobox param2, int param3, String param4, Label param5, Image param6) {
		media = param;
		cmbxEntidad = param2;
		idArchivo = param3;
		lblMensaje = param5;
		imgEstatus = param6;
		leerArchivoCredo(param4);
	}

	public void seleccionaVdos(Media param, Combobox param2, int param3, String param4, Label param5, Image param6) {
		media = param;
		cmbxEntidad = param2;
		idArchivo = param3;
		lblMensaje = param5;
		imgEstatus = param6;

		if (media.getName().toUpperCase().contains("LIQ")) {
			logger.debug("seleccionaVdos() - " + "Encuentra que el archivo es liquidacion");
			leerCredomaticEncabezado(param4);
		} else if (media.getName().toUpperCase().contains("TRX") || media.getName().toUpperCase().contains("DET")) {
			logger.debug("seleccionaVdos() - " + "Encuentra que el archivo es detalle");
			leerCredomaticDetalle(param4);
		} else {
			logger.debug("seleccionaVdos() - " + "El nombre del archivo no es correcto: " + media.getName());
		}
	}

}
