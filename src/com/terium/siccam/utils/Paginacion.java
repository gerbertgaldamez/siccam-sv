package com.terium.siccam.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import com.terium.siccam.exception.CustomExcepcion;


/**
 * Clase que se encarga de hacer el pagineo de un listado
 * @author Alex Godinez
 */
public class Paginacion {

	private final static String OBJ_CONNECTION = "java.sql.Connection";
	private final static String OBJ_BIGDECIMAL = "java.math.BigDecimal";
	private final static String OBJ_LIST = "java.util.List";
	private final static String OBJ_ORDEN = "com.consystec.puntosmovistar.util.Orden";
	
	private Connection conexionDB;
	private Paging paginador;
	private Listbox listadoLbx;
	
	private List<Filtro> listaFiltros;
	private Orden ordenLista;
	
	private int pageSize = 13;
	//indica el total de registros a paginear
	private int totalRegistros = 0;
	private int posIni=0;
	private int posFin=0;
	
	private String claseDAO;
	private String metodoTotalRegistros;
	private String metodoListadoRegistros;
	
	private List listaRegistros;

	public List getListaRegistros() {
		return listaRegistros;
	}
	
	/**
	 * Obtiene el listado del "Listbox" asociado a la paginacion
	 * @return Objeto Listbox asociado al paginador
	 */
	public Listbox getListadoLbx() {
		return listadoLbx;
	}

	/**
	 * Constructor de la clasde Paginacion, donde se fijan todos los componentes y valores que se usaran
	 * para el pagineo del listado asociado.
	 * @param listbox Objeto tipo Listbox al cual se aplicara el pagineo
	 * @param paging Objeto tipo Paging que efectuara la paginacion
	 * @param tamanoPagina Tamano de la pagina del listado
	 * @param claseDAO Nombre de la clase "DAO" donde se contendran los metodos basicos que se usan en
	 * 			el pagineo, que son encargados de dar informacion de la base de datos
	 * @param metodoTotRegs Nombre del metodo de la clase DAO que retorna la totalidad de registros de la base de datos
	 * @param metodoListadoRegs Nombre del metodo de la clase DAO que retorna el listado de registros
	 */
	public Paginacion(Listbox listbox, Paging paging, int tamanoPagina, String claseDAO, String metodoTotRegs,
					String metodoListadoRegs) {
		this.listadoLbx = listbox;
		this.paginador = paging;
		this.pageSize = tamanoPagina;
		this.claseDAO = claseDAO;
		this.metodoTotalRegistros = metodoTotRegs;
		this.metodoListadoRegistros = metodoListadoRegs;
	
		paginador.addEventListener("onPaging", onPagingListado);
	}


	private EventListener onPagingListado = new EventListener(){
		public void onEvent(Event evt)throws InterruptedException{
			PagingEvent pe = (PagingEvent) evt;
			//numero de pagina actual
			int pgno = pe.getActivePage();
			//numero de registro donde empieza el listado
			posIni = pgno * paginador.getPageSize();
			posFin = paginador.getPageSize();
			//ejecutamos el evento que pinta el listado en base a los indices
			repintarListadoRegistros(conexionDB);
		}
	};
	
	
	/**
	 * Metodo que muestra los registros del listado asociado a la paginacion
	 * @param conexion Conexion a la base de datos
	 * @param filtros Filtros que se usaran para mostrar el resultado del listado
	 * @param orden Ordenacion que se efectua sobre el listado
	 * @throws InterruptedException
	 */
	public void mostrarRegistros(Connection conexion, List<Filtro> filtros, Orden orden) throws InterruptedException, CustomExcepcion{
		try{
			conexionDB = conexion;
			listaFiltros = filtros;
			ordenLista = orden;
			//obtenemos el total de registros
			BigDecimal totalRegs = getTotalRegistros(listaFiltros, conexionDB); 
			if(totalRegs!=null){
				this.totalRegistros = totalRegs.intValue();
				if(this.totalRegistros != 0){
					//iniciamos el pagineo en la pagina 0
					paginador.setActivePage(0);
					//asignamos el tama�o de la pagina
					paginador.setPageSize(pageSize);
					//asignamos el total de registros al objeto paging
					paginador.setTotalSize(this.totalRegistros);
					//cargamos en sesion los filtros y el orden del query
					posIni = 0;
					posFin = paginador.getPageSize();
					repintarListadoRegistros(conexionDB);
				} else {
					//limpiamos el listado
					listadoLbx.getItems().clear();
				}
			} else {
				throw new CustomExcepcion("Surgio un imprevisto al obtener el total de registros.");
			}
		} catch (SQLException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
//			showMensaje(e.getMessage(), showModal, e);
		} finally {
			if(conexionDB!=null){
				try {
					conexionDB.close();
				} catch (Exception e) {
					Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
				}
				conexionDB = null;
			}

		}
	}
	
	private BigDecimal getTotalRegistros(List<Filtro> filtros,Connection conexion) throws SQLException {
		BigDecimal total = null;
		
		Object valObj = ejecutaMetodo(claseDAO, metodoTotalRegistros, new String[]{OBJ_CONNECTION, OBJ_LIST, OBJ_ORDEN}, 
								new Object[]{conexion, filtros, ordenLista});
		if (valObj instanceof BigDecimal) {
			total = (BigDecimal)valObj;
		}
		return total;
	}

	/**
	 * Metodo que se encarga de pintar los registros en el listado, aqui se debe de pintar el listado segun los campos
	 * que se quieran, con el metodo getListaRegistros() se obtiene el listado de los registros, y con el metodo
	 * getListadoLbx() se obtiene el Listbox asociado al paginador, con se debe de construir el pintado del listado
	 * @param conexion Conexion a la base de datos
	 */
	protected void repintarListadoRegistros(Connection conexion){
		//limpiamos el listado
		listadoLbx.getItems().clear();
		//componentes que sirven para pintar los campos del registro
		posFin = posIni + posFin;
		//si se cumple la siguiente condici�n estamos en la ultima pagina del listado
		if(posFin > totalRegistros){
			posFin = totalRegistros;
		}
		BigDecimal min = new BigDecimal(posIni);
		BigDecimal max = new BigDecimal(posFin);
		this.listaRegistros = getListadoRegistros(min,max,conexion);
		
		if (conexion!=null) {
			try {
				conexion.close();
			} catch (SQLException e) {
				Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
			}
			conexion=null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private List getListadoRegistros(BigDecimal minimo,BigDecimal maximo,Connection conexion){
		List listaRegs = null;
		
		Object valObj = ejecutaMetodo(claseDAO, metodoListadoRegistros, 
							new String[]{OBJ_CONNECTION, OBJ_BIGDECIMAL, OBJ_BIGDECIMAL, OBJ_LIST, OBJ_ORDEN}, 
							new Object[]{ conexion, minimo, maximo, listaFiltros, ordenLista});
		
		listaRegs = (List)valObj;
		
		return listaRegs;
	}
	
	
	@SuppressWarnings("unchecked")
	private Object ejecutaMetodo (String clase, String metodoLlamado, String[] tiposParametros, Object[] valoresParametros) {
		Object retorno = null;
		Object tempClass;
		try {
			tempClass = Class.forName(clase).newInstance();
			
			Class claseCargada = tempClass.getClass();
			int cantidad = valoresParametros.length;
			//Firma del metodo.
			Class[] argumentos = new Class[cantidad];
			// seteando el tipo de objeto que es cada parametro
			for (int i=0;i<cantidad;i++) {
//				Object tipoClase = Class.forName(tiposParametros[i]).newInstance();
//				argumentos[i] = tipoClase.getClass();
				if (tiposParametros[i].equalsIgnoreCase("String"))
					argumentos[i] = String.class;
				else if (tiposParametros[i].equalsIgnoreCase(OBJ_BIGDECIMAL))
					argumentos[i] = BigDecimal.class;
				else if (tiposParametros[i].equalsIgnoreCase("Integer"))
					argumentos[i] = Integer.class;
				else if (tiposParametros[i].equalsIgnoreCase(OBJ_CONNECTION))
					argumentos[i] = Connection.class;
				else if (tiposParametros[i].equalsIgnoreCase(OBJ_ORDEN))
					argumentos[i] = Orden.class;
				else if (tiposParametros[i].equalsIgnoreCase(OBJ_LIST))
					argumentos[i] = List.class;
			}
			 
			//Busqueda del metodo a ejecutar
			Method metodo = claseCargada.getDeclaredMethod(metodoLlamado, argumentos);
			//Ejecucion del metodo pasandole la clase de este y los parametros.
			retorno = metodo.invoke(tempClass, valoresParametros);
			
		} catch (InstantiationException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
		} catch (IllegalAccessException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
		} catch (ClassNotFoundException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
		} catch (SecurityException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
		} catch (NoSuchMethodException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
		} catch (IllegalArgumentException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
		} catch (InvocationTargetException e) {
			Logger.getLogger(Paginacion.class.getName()).log(Level.SEVERE, null, e);
		}
		 
		return retorno;
	}

}
