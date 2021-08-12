package com.terium.siccam.model;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBDataSinProcesarModel {

	public final static String TABLE = "CB_DATA_SIN_PROCESAR";
	public final static String SEQUENCE = "cb_data_sin_procesar_sq.nextval";
	public final static String PKFIELD_CBDATASINPROCESARID = "cBDataSinProcesarId";
	public final static String FIELD_NOMBRE_ARCHIVO = "nombre_Archivo";
	public final static String FIELD_DATA_ARCHIVO = "data_Archivo";
	public final static String FIELD_ESTADO = "estado";
	public final static String FIELD_CREADO_POR = "creado_Por";
	public final static String FIELD_MODIFICADO_POR = "modificado_Por";
	public final static String FIELD_FECHA_CREACION = "fecha_Creacion";
	public final static String FIELD_FECHA_MODIFICACION = "fecha_Modificacion";

	private int cBDataSinProcesarId;
	private String nombreArchivo;
	private String dataArchivo;
	private String causa;
	private int estado;
	private String creadoPor;
	private String modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private String idCargaMaestro;
	private String observacion;

	public String getCausa() {
		return causa;
	}

	public void setCausa(String causa) {
		this.causa = causa;
	}

	/**
	 * @return the cBDataSinProcesarId
	 */
	public int getCBDataSinProcesarId() {
		return cBDataSinProcesarId;
	}

	/**
	 * @param cBDataSinProcesarId
	 *            the cBDataSinProcesarId to set
	 */
	public void setCBDataSinProcesarId(int cBDataSinProcesarId) {
		this.cBDataSinProcesarId = cBDataSinProcesarId;
	}

	/**
	 * @return the nombreArchivo
	 */
	public String getNombreArchivo() {
		return nombreArchivo;
	}

	/**
	 * @param nombreArchivo
	 *            the nombreArchivo to set
	 */
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	/**
	 * @return the dataArchivo
	 */
	public String getDataArchivo() {
		return dataArchivo;
	}

	/**
	 * @param dataArchivo
	 *            the dataArchivo to set
	 */
	public void setDataArchivo(String dataArchivo) {
		this.dataArchivo = dataArchivo;
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

	public String getIdCargaMaestro() {
		return idCargaMaestro;
	}

	public void setIdCargaMaestro(String idCargaMaestro) {
		this.idCargaMaestro = idCargaMaestro;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

}