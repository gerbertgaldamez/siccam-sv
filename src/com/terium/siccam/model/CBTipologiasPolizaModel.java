package com.terium.siccam.model;

public class CBTipologiasPolizaModel {
	private int cbtipologiaspolizaid;
	private String nombre;
	private String descripcion;
	private String creador;
	private String fechaCreacion;
	private String modificado_por;
	private String fecha_modificacion;

	private int tipo;
	private int pideEntidad;
	
	

	public String getModificado_por() {
		return modificado_por;
	}

	public void setModificado_por(String modificado_por) {
		this.modificado_por = modificado_por;
	}

	public String getFecha_modificacion() {
		return fecha_modificacion;
	}

	public void setFecha_modificacion(String fecha_modificacion) {
		this.fecha_modificacion = fecha_modificacion;
	}



	public CBTipologiasPolizaModel() {
	}
	
	public int getCbtipologiaspolizaid() {
		return cbtipologiaspolizaid;
	}
	public void setCbtipologiaspolizaid(int cbtipologiaspolizaid) {
		this.cbtipologiaspolizaid = cbtipologiaspolizaid;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCreador() {
		return creador;
	}
	public void setCreador(String creador) {
		this.creador = creador;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getPideEntidad() {
		return pideEntidad;
	}
	public void setPideEntidad(int pideEntidad) {
		this.pideEntidad = pideEntidad;
	}
}
