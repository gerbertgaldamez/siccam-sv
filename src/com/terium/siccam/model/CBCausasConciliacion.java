package com.terium.siccam.model;

public class CBCausasConciliacion {
	
	private String id;
	private String causas;
	private String creadoPor;
	private String fechaCreacion;
	private String modificadoPor;
	private String fechaModificacion;
	private String codigoconciliacion;
	private int tipo;
	private int sistema;
	
	public CBCausasConciliacion() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CBCausasConciliacion(String id, String causas, String creadoPor,
			String fechaCreacion, String modificadoPor, String fechaModificacion) {
		super();
		this.id = id;
		this.causas = causas;
		this.creadoPor = creadoPor;
		this.fechaCreacion = fechaCreacion;
		this.modificadoPor = modificadoPor;
		this.fechaModificacion = fechaModificacion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCausas() {
		return causas;
	}
	public void setCausas(String causas) {
		this.causas = causas;
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

	public String getCodigoconciliacion() {
		return codigoconciliacion;
	}

	public void setCodigoconciliacion(String codigoconciliacion) {
		this.codigoconciliacion = codigoconciliacion;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}
	
	

}
