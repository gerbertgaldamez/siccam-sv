/**
 * 
 */
package com.terium.siccam.model;

/**
 * @author lab
 * 
 */
public class CBConfiguracionConModel {

	private String idConexionConf;
	private String nombre;
	private String ipConexion;
	private String usuario;
	private String pass;
	private String creadoPor;
	private String fechaCreacion;
	private String modificadoPor;
	private String fechaModificacion;
	private String usuarioConexion;

	public String getUsuarioConexion() {
		return usuarioConexion;
	}

	public void setUsuarioConexion(String usuarioConexion) {
		this.usuarioConexion = usuarioConexion;
	}

	public String getIdConexionConf() {
		return idConexionConf;
	}

	public void setIdConexionConf(String idConexionConf) {
		this.idConexionConf = idConexionConf;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getIpConexion() {
		return ipConexion;
	}

	public void setIpConexion(String ipConexion) {
		this.ipConexion = ipConexion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public String getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}
