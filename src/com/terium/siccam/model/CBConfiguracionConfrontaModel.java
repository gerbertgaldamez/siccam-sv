package com.terium.siccam.model;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBConfiguracionConfrontaModel {
	private String cbBancoAgenciaConfrontaId;
	private String cBConfiguracionConfrontaId;
	private String nombre;
	private String delimitador1;
	private String delimitador2;
	private int cantidadAgrupacion;
	private String nomenclatura;
	private String estadoTxt;
	private int estado;
	private String pathFtp;
	private String tipo;
	private String nombreConexion;
	private String formatoFecha;
	private String creadoPor;
	private String modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private String idConexionConf;
	private String posiciones;
	private String longitudCadena;
	private String palabraArchivo;
	private int cantidadAjustes;
	private String descartarTransaccion;
	private int confrontasocia;
	private double comision;
	private double aproximacion;
	
	
	
	public int getConfrontasocia() {
		return confrontasocia;
	}

	public void setConfrontasocia(int confrontasocia) {
		this.confrontasocia = confrontasocia;
	}

	public double getAproximacion() {
		return aproximacion;
	}

	public void setAproximacion(double aproximacion) {
		this.aproximacion = aproximacion;
	}

	private int afiliacion;
	private int lineaLectura;
	
	//Agrega Carlos Godinez - Propiedades para asignar confronta padre
	private String cBAgenciasConfrontaId;
	private int confrontasDependientes;
	private int estadoComision; //CarlosGodinez -> 22/09/2017
	private String confrontaPadre; //CarlosGodinez -> 02/10/2017
	
	/**
	 * Agrega Carlos Godinez -> 06/06/2018
	 * */
	private String cbCatalogoBancoId;
	private String cbCatalogoAgenciaId;

	public String getEstadoTxt() {
		return estadoTxt;
	}

	public void setEstadoTxt(String estadoTxt) {
		this.estadoTxt = estadoTxt;
	}

	public String getPathFtp() {
		return pathFtp;
	}

	public void setPathFtp(String pathFtp) {
		this.pathFtp = pathFtp;
	}

	public String getNombreConexion() {
		return nombreConexion;
	}

	public void setNombreConexion(String nombreConexion) {
		this.nombreConexion = nombreConexion;
	}

	public String getCbBancoAgenciaConfrontaId() {
		return cbBancoAgenciaConfrontaId;
	}

	public void setCbBancoAgenciaConfrontaId(String cbBancoAgenciaConfrontaId) {
		this.cbBancoAgenciaConfrontaId = cbBancoAgenciaConfrontaId;
	}

	public String getcBConfiguracionConfrontaId() {
		return cBConfiguracionConfrontaId;
	}

	public void setcBConfiguracionConfrontaId(String cBConfiguracionConfrontaId) {
		this.cBConfiguracionConfrontaId = cBConfiguracionConfrontaId;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the delimitador1
	 */
	public String getDelimitador1() {
		return delimitador1;
	}

	/**
	 * @param delimitador1
	 *            the delimitador1 to set
	 */
	public void setDelimitador1(String delimitador1) {
		this.delimitador1 = delimitador1;
	}

	/**
	 * @return the delimitador2
	 */
	public String getDelimitador2() {
		return delimitador2;
	}

	/**
	 * @param delimitador2
	 *            the delimitador2 to set
	 */
	public void setDelimitador2(String delimitador2) {
		this.delimitador2 = delimitador2;
	}

	/**
	 * @return the cantidadAgrupacion
	 */
	public int getCantidadAgrupacion() {
		return cantidadAgrupacion;
	}

	/**
	 * @param cantidadAgrupacion
	 *            the cantidadAgrupacion to set
	 */
	public void setCantidadAgrupacion(int cantidadAgrupacion) {
		this.cantidadAgrupacion = cantidadAgrupacion;
	}

	/**
	 * @return the nomenclatura
	 */
	public String getNomenclatura() {
		return nomenclatura;
	}

	/**
	 * @param nomenclatura
	 *            the nomenclatura to set
	 */
	public void setNomenclatura(String nomenclatura) {
		this.nomenclatura = nomenclatura;
	}

	/**
	 * @return the estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the formatoFecha
	 */
	public String getFormatoFecha() {
		return formatoFecha;
	}

	/**
	 * @param formatoFecha
	 *            the formatoFecha to set
	 */
	public void setFormatoFecha(String formatoFecha) {
		this.formatoFecha = formatoFecha;
	}

	/**
	 * @return the creadoPor
	 */
	public String getCreadoPor() {
		return creadoPor;
	}

	/**
	 * @param creadoPor
	 *            the creadoPor to set
	 */
	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	/**
	 * @return the modificadoPor
	 */
	public String getModificadoPor() {
		return modificadoPor;
	}

	/**
	 * @param modificadoPor
	 *            the modificadoPor to set
	 */
	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	/**
	 * @return the fechaCreacion
	 */
	public String getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            the fechaCreacion to set
	 */
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion
	 *            the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	public String getIdConexionConf() {
		return idConexionConf;
	}

	public void setIdConexionConf(String idConexionConf) {
		this.idConexionConf = idConexionConf;
	}

	public String getPosiciones() {
		return posiciones;
	}

	public void setPosiciones(String posiciones) {
		this.posiciones = posiciones;
	}

	public String getLongitudCadena() {
		return longitudCadena;
	}

	public void setLongitudCadena(String longitudCadena) {
		this.longitudCadena = longitudCadena;
	}

	public String getPalabraArchivo() {
		return palabraArchivo;
	}

	public void setPalabraArchivo(String palabraArchivo) {
		this.palabraArchivo = palabraArchivo;
	}

	public int getCantidadAjustes() {
		return cantidadAjustes;
	}

	public void setCantidadAjustes(int cantidadAjustes) {
		this.cantidadAjustes = cantidadAjustes;
	}

	public String getDescartarTransaccion() {
		return descartarTransaccion;
	}

	public void setDescartarTransaccion(String descartarTransaccion) {
		this.descartarTransaccion = descartarTransaccion;
	}

	public double getComision() {
		return comision;
	}

	public void setComision(double comision) {
		this.comision = comision;
	}

	public int getAfiliacion() {
		return afiliacion;
	}

	public void setAfiliacion(int afiliacion) {
		this.afiliacion = afiliacion;
	}

	public int getLineaLectura() {
		return lineaLectura;
	}

	public void setLineaLectura(int lineaLectura) {
		this.lineaLectura = lineaLectura;
	}

	public String getcBAgenciasConfrontaId() {
		return cBAgenciasConfrontaId;
	}

	public void setcBAgenciasConfrontaId(String cBAgenciasConfrontaId) {
		this.cBAgenciasConfrontaId = cBAgenciasConfrontaId;
	}

	public int getConfrontasDependientes() {
		return confrontasDependientes;
	}

	public void setConfrontasDependientes(int confrontasDependientes) {
		this.confrontasDependientes = confrontasDependientes;
	}
	
	/**
	 * CarlosGodinez -> 22/09/2017
	 * */
	
	public int getEstadoComision() {
		return estadoComision;
	}

	public void setEstadoComision(int estadoComision) {
		this.estadoComision = estadoComision;
	}
	
	/**
	 * FIN CarlosGodinez -> 22/09/2017
	 * */
	
	/**
	 * CarlosGodinez -> 22/09/2017
	 * */
	public String getConfrontaPadre() {
		return confrontaPadre;
	}

	public void setConfrontaPadre(String confrontaPadre) {
		this.confrontaPadre = confrontaPadre;
	}

	/**
	 * FIN CarlosGodinez -> 22/09/2017
	 * */
	
	/**
	 * CarlosGodinez -> 06/06/2018
	 * Propiedades de agrupacion y entidad
	 * */
	public String getCbCatalogoBancoId() {
		return cbCatalogoBancoId;
	}

	public void setCbCatalogoBancoId(String cbCatalogoBancoId) {
		this.cbCatalogoBancoId = cbCatalogoBancoId;
	}

	public String getCbCatalogoAgenciaId() {
		return cbCatalogoAgenciaId;
	}

	public void setCbCatalogoAgenciaId(String cbCatalogoAgenciaId) {
		this.cbCatalogoAgenciaId = cbCatalogoAgenciaId;
	}
	/**
	 * FIN CarlosGodinez -> 06/06/2018
	 * */
	
}