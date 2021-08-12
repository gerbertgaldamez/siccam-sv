package com.terium.siccam.model;

public class CBLiquidacionCajeroModel {
	private int cbliquidacionid;
	private String nombtransaccion;
	private String fechatransaccion;
	private String descripcion;
	private int estado;
	private String creador;
	
	//Campos agregados de la vista CB_LIQUIDACION_CAJEROS
	private String efectivo;
	private String cuotasvisa;
	private String cuotascredomatic;
	private String visa;
	private String credomatic;
	private String otras;
	private String cheque;
	private String excencioniva;
	private String deposito;
	private String fechacreacion;

	public CBLiquidacionCajeroModel(){
	}
	
	public CBLiquidacionCajeroModel(String nombtransaccion, String fechatransaccion, String descripcion, int estado, String creador){
		this.nombtransaccion = nombtransaccion;
		this.fechatransaccion = fechatransaccion;
		this.descripcion = descripcion;
		this.estado = estado;
		this.creador = creador;
	}
	
	public int getCbliquidacionid() {
		return cbliquidacionid;
	}

	public void setCbliquidacionid(int cbliquidacionid) {
		this.cbliquidacionid = cbliquidacionid;
	}

	public String getNombtransaccion() {
		return nombtransaccion;
	}

	public void setNombtransaccion(String nombtransaccion) {
		this.nombtransaccion = nombtransaccion;
	}
	
	public String getFechatransaccion() {
		return fechatransaccion;
	}

	public void setFechatransaccion(String fechatransaccion) {
		this.fechatransaccion = fechatransaccion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}

	
	//Campos agregados de la vista CB_LIQUIDACION_CAJEROS
	
	public String getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(String efectivo) {
		this.efectivo = efectivo;
	}

	public String getCuotasvisa() {
		return cuotasvisa;
	}

	public void setCuotasvisa(String cuotasvisa) {
		this.cuotasvisa = cuotasvisa;
	}

	public String getCuotascredomatic() {
		return cuotascredomatic;
	}

	public void setCuotascredomatic(String cuotascredomatic) {
		this.cuotascredomatic = cuotascredomatic;
	}

	public String getVisa() {
		return visa;
	}

	public void setVisa(String visa) {
		this.visa = visa;
	}

	public String getCredomatic() {
		return credomatic;
	}

	public void setCredomatic(String credomatic) {
		this.credomatic = credomatic;
	}

	public String getOtras() {
		return otras;
	}

	public void setOtras(String otras) {
		this.otras = otras;
	}

	public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}

	public String getExcencioniva() {
		return excencioniva;
	}

	public void setExcencioniva(String excencioniva) {
		this.excencioniva = excencioniva;
	}

	public String getDeposito() {
		return deposito;
	}

	public void setDeposito(String deposito) {
		this.deposito = deposito;
	}

	public String getFechacreacion() {
		return fechacreacion;
	}

	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}
	
}
