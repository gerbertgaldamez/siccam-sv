package com.terium.siccam.model;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBCatalogoOpcionModel {

	public final static String TABLE = "CB_CATALOGO_OPCION";
	public final static String SEQUENCE = "cb_catalogo_opcion_sq.nextval";
	public final static String PKFIELD_CBCATALOGOOPCIONID = "cBCatalogoOpcionId";
	public final static String FIELD_NOMBRE = "nombre";
	public final static String FIELD_VALOR = "valor";
	public final static String FIELD_TIPO = "tipo";
	public final static String FIELD_ESTADO = "estado";
	public final static String FIELD_ORDEN = "orden";
	public final static String FIELD_CREADO_POR = "creado_Por";
	public final static String FIELD_MODIFICADO_POR = "modificado_Por";
	public final static String FIELD_FECHA_CREACION = "fecha_Creacion";
	public final static String FIELD_FECHA_MODIFICACION = "fecha_Modificacion";

	private int cBCatalogoOpcionId;
	private String nombre;
	private String valor;
	private String tipo;
	private String estado;
	private int orden;
	private String creadoPor;
	private String modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;

	/**
	 * @return the cBCatalogoOpcionId
	 */
	public int getCBCatalogoOpcionId() {
		return cBCatalogoOpcionId;
	}

	/**
	 * @param cBCatalogoOpcionId
	 *            the cBCatalogoOpcionId to set
	 */
	public void setCBCatalogoOpcionId(int cBCatalogoOpcionId) {
		this.cBCatalogoOpcionId = cBCatalogoOpcionId;
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
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden
	 *            the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
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
}