package com.terium.siccam.model;

public class CBLiquidacionDetalleModel {
	private int cbliquidaciondetalleid;
	private int liquidacionid;
	private int tipo_valo;
	private String tipo_pago;
	private String cod_tipotarjeta;
	private String desc;
	private String total;
	
	//Campos agregados para reporte (.csv) de liquidaciones
	private String nombtransaccion;
	private String fec_efectividad;
	
	public CBLiquidacionDetalleModel(){
	}
	
	public int getCbliquidaciondetalleid() {
		return cbliquidaciondetalleid;
	}

	public void setCbliquidaciondetalleid(int cbliquidaciondetalleid) {
		this.cbliquidaciondetalleid = cbliquidaciondetalleid;
	}

	public int getLiquidacionid() {
		return liquidacionid;
	}

	public void setLiquidacionid(int liquidacionid) {
		this.liquidacionid = liquidacionid;
	}

	public int getTipo_valo() {
		return tipo_valo;
	}

	public void setTipo_valo(int tipo_valo) {
		this.tipo_valo = tipo_valo;
	}

	public String getTipo_pago() {
		return tipo_pago;
	}

	public void setTipo_pago(String tipo_pago) {
		this.tipo_pago = tipo_pago;
	}
	
	public String getCod_tipotarjeta() {
		return cod_tipotarjeta;
	}

	public void setCod_tipotarjeta(String cod_tipotarjeta) {
		this.cod_tipotarjeta = cod_tipotarjeta;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
	
	public String getNombtransaccion() {
		return nombtransaccion;
	}

	public void setNombtransaccion(String nombtransaccion) {
		this.nombtransaccion = nombtransaccion;
	}

	public String getFec_efectividad() {
		return fec_efectividad;
	}

	public void setFec_efectividad(String fec_efectividad) {
		this.fec_efectividad = fec_efectividad;
	}
}
