package com.terium.siccam.model;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBCatalogoBancoModel {

	public final static String TABLE = "CB_CATALOGO_BANCO";
	public final static String SEQUENCE = "cb_catalogo_banco_sq.nextval";
	public final static String PKFIELD_CBCATALOGOBANCOID = "cBCatalogoBancoId";
	public final static String FIELD_NOMBRE = "nombre";
	public final static String FIELD_CONTACTO = "contacto";
	public final static String FIELD_TELEFONO = "telefono";
	public final static String FIELD_EXTENSION = "extension";
	public final static String FIELD_ESTADO = "estado";
	public final static String FIELD_CREADO_POR = "creado_Por";
	public final static String FIELD_MODIFICADO_POR = "modificado_Por";
	public final static String FIELD_FECHA_CREACION = "fecha_Creacion";
	public final static String FIELD_FECHA_MODIFICACION = "fecha_Modificacion";

	private String cbcatalogobancoid;
	private String nombre;
	private String contacto;
	private String telefono;
	private String extension;
	private String estado;
	private String tipoEstado;
	private String creadoPor;
	private String modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private String usuario;
	

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCbcatalogobancoid() {
		return cbcatalogobancoid;
	}

	public void setCbcatalogobancoid(String cbcatalogobancoid) {
		this.cbcatalogobancoid = cbcatalogobancoid;
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
	 * @return the contacto
	 */
	public String getContacto() {
		return contacto;
	}

	/**
	 * @param contacto
	 *            the contacto to set
	 */
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
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

	public String getTipoEstado() {
		return tipoEstado;
	}

	public void setTipoEstado(String tipoEstado) {
		this.tipoEstado = tipoEstado;
	}

}