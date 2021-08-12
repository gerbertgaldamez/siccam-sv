package com.terium.siccam.model;

public class CBAgenciaVirtualFisicaModel {

	private int cbAgenciaVirfiscid;
	private int idAgencia;
	private String usuario;
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(int idAgencia) {
		this.idAgencia = idAgencia;
	}

	private String codigo;
	private String nombre;
	private String tipo;
	private String creadoPor;
	private String fechaCreado;
	private String modificadoPor;
	private String fechaModificado;
	private String nombreTipo;
	


	public int getCbAgenciaVirfiscid() {
		return cbAgenciaVirfiscid;
	}

	public void setCbAgenciaVirfiscid(int cbAgenciaVirfiscid) {
		this.cbAgenciaVirfiscid = cbAgenciaVirfiscid;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCreadoPor() {
		return creadoPor;
	}

	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}

	public String getFechaCreado() {
		return fechaCreado;
	}

	public void setFechaCreado(String fechaCreado) {
		this.fechaCreado = fechaCreado;
	}

	public String getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public String getFechaModificado() {
		return fechaModificado;
	}

	public void setFechaModificado(String fechaModificado) {
		this.fechaModificado = fechaModificado;
	}

	public String getNombreTipo() {
		if(getTipo().equals("0")){
			setNombreTipo("VIRTUAL");
		}else{
			setNombreTipo("PRESENCIAL");
		}
		return nombreTipo;
	}

	public void setNombreTipo(String nombreTipo) {
		this.nombreTipo = nombreTipo;
	}

}
