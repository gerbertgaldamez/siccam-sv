package com.terium.siccam.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

//import java.util.logging.Logger;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.dao.CBMantenimientoTipologiasPolizaDAO;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBMantenimientoPolizaModel;
import com.terium.siccam.model.CBTipologiasEntidadesModel;
import com.terium.siccam.utils.Constantes;

/**
 * @author CarlosGodinez -> 13/08/2018
 */
public class CBTipologiasEntidadesController extends ControladorBase{
	private static Logger log = Logger.getLogger(CBTipologiasEntidadesController.class);
	
	private static final long serialVersionUID = 1L;
	
	public void doAfterCompose(Component param) {
		try{
			super.doAfterCompose(param);
			objSeleccionado = (CBMantenimientoPolizaModel) session.getAttribute("tipologiaSeleccionada");
			llenaComboAgrupaciones();
			cmbAgrupacion.setText(Constantes.TODAS);
			cmbEntidad.setText(Constantes.TODAS);
			lblEntidadSeleccionada.setValue("Tipología de póliza seleccionada: " 
					+ objSeleccionado.getNombre());	
			usuario = obtenerUsuario().getUsuario();
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("doAfterCompose() -  Error ", e);
			//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * ZK
	 */
	Window tipologiaEntidadModal;
	
	private Listbox lbxConsulta;
	
	private Combobox cmbAgrupacion;
	private Combobox cmbEntidad;
	
	private Checkbox chkFiltroTipolSelected;
	private Checkbox chkMarcarTodas;
	
	private Label lblEntidadSeleccionada;

	/**
	 * Objeto enviado desde pantalla principal
	 * */
	private CBMantenimientoPolizaModel objSeleccionado = new CBMantenimientoPolizaModel();
	private String usuario;
	
	/**
	 * Listas para llenar combobox
	 */
	private List<CBCatalogoBancoModel> listaAgrupacion = new ArrayList<CBCatalogoBancoModel>();
	private List<CBCatalogoAgenciaModel> listaEntidad = new ArrayList<CBCatalogoAgenciaModel>();
	
	/**
	 * Lista de entidades a asociar
	 * */
	private List<CBTipologiasEntidadesModel> listaEntidadesAsociar = new ArrayList<CBTipologiasEntidadesModel>();
	
	public void llenaComboAgrupaciones() {
		try {
			CBMantenimientoTipologiasPolizaDAO objeDAO = new CBMantenimientoTipologiasPolizaDAO();
			listaAgrupacion = objeDAO.obtenerAgrupaciones();
			for (CBCatalogoBancoModel d : listaAgrupacion) {
				Comboitem item = new Comboitem();
				item.setParent(cmbAgrupacion);
				item.setValue(d.getCbcatalogobancoid());
				item.setLabel(d.getNombre());
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("llenaComboAgrupaciones() -  Error ", e);
			//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Metodo que se invoca al seleccionar una agrupacion
	 */
	public void onSelect$cmbAgrupacion() {
		try {
			if (cmbAgrupacion.getSelectedItem().getValue() != null && !Constantes.TODAS.equals(cmbAgrupacion.getText())) {
				int agrupacionSeleccionada = Integer.parseInt(cmbAgrupacion.getSelectedItem().getValue().toString());
				log.debug("onSelect$cmbAgrupacion()  " + " - Agrupacion seleccionada= " + agrupacionSeleccionada);
				//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.INFO,
						//"Agrupacion seleccionada= " + agrupacionSeleccionada);
				cleanCombo(cmbEntidad);
				llenaComboEntidades(agrupacionSeleccionada);
				if (listaEntidad.isEmpty()) {
					cmbEntidad.setText(Constantes.TODAS);
				} else {
					cmbEntidad.setSelectedIndex(0);
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onSelect$cmbAgrupacion() -  Error ", e);
			//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void llenaComboEntidades(int agrupacionSeleccionada) {
		try {
			CBMantenimientoTipologiasPolizaDAO objeDAO = new CBMantenimientoTipologiasPolizaDAO();
			listaEntidad = objeDAO.obtenerEntidades(agrupacionSeleccionada);
			int cont = 0; //Variable cotroladora para asignar el primer valor como 'Todas'
			for (CBCatalogoAgenciaModel d : listaEntidad) {
				Comboitem item = null;
				if (cont == 0) {
					item = new Comboitem();
					item.setParent(cmbEntidad);
					item.setValue(Constantes.TODAS);
					item.setLabel(Constantes.TODAS);
					item = new Comboitem();
					item.setParent(cmbEntidad);
					item.setValue(d.getcBCatalogoAgenciaId());
					item.setLabel(d.getNombre());
					cont = 1;
				} else {
					item = new Comboitem();
					item.setParent(cmbEntidad);
					item.setValue(d.getcBCatalogoAgenciaId());
					item.setLabel(d.getNombre());
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("llenaComboEntidades() -  Error ", e);
			//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void onClick$btnConsultar() {
		realizaBusqueda(1);
	}
	
	
	/**
	 * Aplica filtros para la consulta
	 */
	public void realizaBusqueda(int banderaMensaje) {
		try {
			limpiarListbox(lbxConsulta);
			int filtroAgrupacion = 0;
			int filtroEntidad = 0;
			int filtroTipologia = 0;
			if(cmbAgrupacion.getSelectedItem().getValue() != null && !Constantes.TODAS.equals(cmbAgrupacion.getText())) {
				filtroAgrupacion = Integer.parseInt(cmbAgrupacion.getSelectedItem().getValue().toString());
			} 
			if(cmbEntidad.getSelectedItem().getValue() != null && !Constantes.TODAS.equals(cmbEntidad.getText())) {
				filtroEntidad = Integer.parseInt(cmbEntidad.getSelectedItem().getValue().toString());
			} 
			if(chkFiltroTipolSelected.isChecked()) {
				filtroTipologia = objSeleccionado.getCbtipologiaspolizaid();
			}
			CBTipologiasEntidadesModel objModel = new CBTipologiasEntidadesModel();
			objModel.setCbCatalogoBancoId(filtroAgrupacion);
			objModel.setCbCatalogoAgenciaId(filtroEntidad);
			objModel.setCbTipologiasPolizaId(filtroTipologia);
			llenaListbox(objModel, objSeleccionado.getCbtipologiaspolizaid(), banderaMensaje);
		} catch(Exception e) {
			log.error("realizaBusqueda() -  Error ", e);
			//Logger.getLogger(CBRecaReguController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Realiza consulta general
	 * @param objModel = objeto con filtros aplicados
	 * @param tipologiaRecuperada = tipologia seleccionada desde pantalla principal
	 */
	public void llenaListbox(CBTipologiasEntidadesModel objModel, int tipologiaRecuperada, int banderaMensaje) {
		try {
			CBMantenimientoTipologiasPolizaDAO objDAO = new CBMantenimientoTipologiasPolizaDAO();
			List<CBTipologiasEntidadesModel> lst = objDAO.consultaEntidadesAsociadas(objModel, tipologiaRecuperada);
			if (lst.size() > 0) {
				limpiarListbox(lbxConsulta);
				Iterator<CBTipologiasEntidadesModel> it = lst.iterator();
				CBTipologiasEntidadesModel obj = null;
				Listcell cell = null;
				Listitem fila = null;
				while (it.hasNext()) {
					obj = it.next();

					fila = new Listitem();
					
					cell = new Listcell();
					cell.setParent(fila);
					
					cell = new Listcell();
					cell.setLabel(obj.getNombreBanco()); // Agrupacion
					cell.setParent(fila);

					cell = new Listcell();
					cell.setLabel(obj.getNombreAgencia()); // Entidad
					cell.setParent(fila);
					
					cell = new Listcell();
					if(obj.getCountTipologias() > 0) {  //Verifica si entidad esta asociada
						cell.setImage(Constantes.IMG_CHECK_16X16);
					} else {
						cell.setLabel(""); 
					}
					cell.setParent(fila);

					cell = new Listcell();
					if(obj.getCountTipologias() > 0) {  //Verifica si entidad esta asociada
						Button btnDelete = new Button();
						btnDelete.setImage(Constantes.IMG_DELETE_16X16);
						btnDelete.setParent(cell);
						btnDelete.setTooltip("popEliminar");
						btnDelete.setAttribute("objEntidad", obj);
						btnDelete.addEventListener(Events.ON_CLICK, evtEliminar);
					} else {
						cell.setLabel(""); 
					} 
					cell.setParent(fila);
					
					fila.setValue(obj);
					if(obj.getCountTipologias() > 0) {  //Verifica si entidad esta asociada
						fila.setCheckable(false); //Deshabilita el check para Listitem
					} else {
						fila.setCheckable(true); //Habilita el check para el Listitem
					}
					fila.setParent(lbxConsulta);
				}
			} else {
				if(banderaMensaje == 1) {
					Messagebox.show("No existen registros de entidades para los filtros aplicados", "ATENCION",
							Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("llenaListbox() -  Error ", e);
			//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Metodo para marcar todas las entidades (excepto las que ya estan asociadas)
	 */
	public void onCheck$chkMarcarTodas() {
		if (chkMarcarTodas.isChecked()) {
			List<Listitem> list = lbxConsulta.getItems();
			if (list.size() > 0) {
				int cont = 0;
				for (Listitem item : list) {
					if(item.isCheckable()) { //Verifica si item contiene check para asociar
						item.setSelected(true);
						cont++;
					}
				}
				log.debug("onCheck$chkMarcarTodas()  " + " - (Marcar Todas) cant. de items marcados = "+ cont);
				//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.INFO, 
						//"(Marcar Todas) cant. de items marcados = "+ cont);
			}
		} else {
			List<Listitem> list = lbxConsulta.getItems();
			log.debug("onCheck$chkMarcarTodas()  " + " - Se desmarcan todos los items del Grid principal");
			//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.INFO, 
					//"Se desmarcan todos los items del Grid principal");
			if (list.size() > 0) {
				for (Listitem item : list) {
					item.setSelected(false);
				}
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onClick$btnGuardar() {
		try {
			if(listaEntidadesAsociar != null && listaEntidadesAsociar.size() > 0) {
				listaEntidadesAsociar.clear();
			}
			List<Listitem> gridItems = lbxConsulta.getItems();
			for (Listitem item : gridItems) {
				if (item.isSelected()) { // Verifica entidades seleccionadas y se agregan a ArrayList
					listaEntidadesAsociar.add((CBTipologiasEntidadesModel) item.getValue());
				}
			}
			if (listaEntidadesAsociar != null && listaEntidadesAsociar.size() > 0) {
				Messagebox.show(
						"Se asociarán " + listaEntidadesAsociar.size() + " entidades a la tipología de póliza "
								+ objSeleccionado.getNombre() + " ¿Desea continuar con la operación?",
						Constantes.CONFIRMACION, Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
						new EventListener() {
							public void onEvent(Event event) throws Exception {
								if (((Integer) event.getData()).intValue() == Messagebox.YES) {
									asociacionMasivaEntidades(1);
								} else {
									asociacionMasivaEntidades(0);
								}
							}
						});
			} else {
				Messagebox.show("No se ha seleccionado ninguna entidad para asociar a " + objSeleccionado.getNombre(),
						"ATENCION", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			log.error("onClick$btnGuardar() -  Error ", e);
			//Logger.getLogger(CBTipologiasEntidadesController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	public void asociacionMasivaEntidades(int confirmacion) {
		if (confirmacion == 1) {
			CBMantenimientoTipologiasPolizaDAO objDAO = new CBMantenimientoTipologiasPolizaDAO();
			if (objDAO.asociacionEntidadesTipologia(listaEntidadesAsociar, 
					objSeleccionado.getCbtipologiaspolizaid(), usuario)) {
				Messagebox.show("Se han asociado " + listaEntidadesAsociar.size()
						+ " entidades para la tipologia de póliza " + objSeleccionado.getNombre(), "ATENCION",
						Messagebox.OK, Messagebox.INFORMATION);
				if(listaEntidadesAsociar != null && listaEntidadesAsociar.size() > 0) {
					listaEntidadesAsociar.clear();
				}
				realizaBusqueda(0);
			}
		} else {
			if(listaEntidadesAsociar != null && listaEntidadesAsociar.size() > 0) {
				listaEntidadesAsociar.clear();
			}
		}
	}
	
	/**
	 * Metodo para eliminar entidad asociada
	 * */
	EventListener<Event> evtEliminar = new EventListener<Event>() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void onEvent(Event event) throws Exception {
			CBTipologiasEntidadesModel objModel = (CBTipologiasEntidadesModel) event.getTarget()
					.getAttribute("objEntidad");
			final int idFila = objModel.getCbCatalogoAgenciaId();
			log.debug("evtEliminar()  " + " - ID entidad asociada a eliminar = " + idFila);
			//Logger.getLogger(CBMantenimientoConfConfrontasController.class.getName()).log(Level.INFO,
					//"ID entidad asociada a eliminar = " + idFila);
			Messagebox.show("¿Desea eliminar la asociación de esta entidad?", Constantes.CONFIRMACION,
					Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
						public void onEvent(Event event) throws Exception {
							if (((Integer) event.getData()).intValue() == Messagebox.YES) {
								CBMantenimientoTipologiasPolizaDAO objeDAO = new CBMantenimientoTipologiasPolizaDAO();
								if (objeDAO.eliminaAsociacion(objSeleccionado.getCbtipologiaspolizaid(), idFila)) {
									realizaBusqueda(0);
									Messagebox.show("Asociación de entidad eliminada con éxito.", Constantes.ATENCION,
											Messagebox.OK, Messagebox.INFORMATION);
								}
							}
						}
					});
		}
	};
	
	/**
	 * 
	 * */
	public void cleanCombo(Combobox component) {
		if (component != null) {
			component.getItems().removeAll(component.getItems());
		}
	}
	
	public void limpiarListbox(Listbox componente) {
		if (componente != null) {
			componente.getItems().removeAll(componente.getItems());
			if (!"paging".equals(componente.getMold())) {
				componente.setMold("paging");
				componente.setAutopaging(true);
			}
		}
	}
}
