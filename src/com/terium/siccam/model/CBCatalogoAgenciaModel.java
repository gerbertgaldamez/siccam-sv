package com.terium.siccam.model;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBCatalogoAgenciaModel {
	
	private String cBCatalogoAgenciaId;
	private String cBCatalogoBancoId;
	private String nombreBanco;
	private String nombre;
	private String telefono;
	private String direccion;
	private String estado;
	private String creadoPor;
	private String modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private String estadoTxt; 
	private String cuentaContable;
	private String moneda;
	private String codigoColector;
	private String fechaDeposito;
	private String deposito; 
	private String tipologia;
	private String entidadDeposito;
	
	private String nit;

	public String getNit() {
		return nit;
	}

	public void setNit(String nit) {
		this.nit = nit;
	}

	public String getCodigoColector() {
		return codigoColector;
	}

	public void setCodigoColector(String codigoColector) {
		this.codigoColector = codigoColector;
	}
	public String getFechaDeposito() {
		return fechaDeposito;
	}

	public void setFechaDeposito(String fechaDeposito) {
		this.fechaDeposito = fechaDeposito;
	}

	public String getDeposito() {
		return deposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public String getTipologia() {
		return tipologia;
	}

	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}

	public String getEntidadDeposito() {
		return entidadDeposito;
	}

	public void setEntidadDeposito(String entidadDeposito) {
		this.entidadDeposito = entidadDeposito;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public String getcBCatalogoAgenciaId() {
		return cBCatalogoAgenciaId;
	}

	public void setcBCatalogoAgenciaId(String cBCatalogoAgenciaId) {
		this.cBCatalogoAgenciaId = cBCatalogoAgenciaId;
	}

	public String getcBCatalogoBancoId() {
		return cBCatalogoBancoId;
	}

	public void setcBCatalogoBancoId(String cBCatalogoBancoId) {
		this.cBCatalogoBancoId = cBCatalogoBancoId;
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
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
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

	public String getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(String cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getEstadoTxt() {
		return estadoTxt;
	}

	public void setEstadoTxt(String estadoTxt) {
		this.estadoTxt = estadoTxt;
	}
}