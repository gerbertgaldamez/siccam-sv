package com.terium.siccam.model;

public class CBBancoAgenciaCajasModel {
	private int cbbancoagenciacajasid;
	private int cbcatalogoagenciaid;
	private String cod_oficina;
	private String cod_caja;
	private String cajero;
	private int estadoCaja;
	private String creador;
	private String fechacreacion;
	private String usuario;
	
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	private String nombreBanco;
	private String nombreAgencia;
	
	public CBBancoAgenciaCajasModel(){
	}
	
	public CBBancoAgenciaCajasModel(int cbcatalogoagenciaid, String cod_oficina, String cod_caja, String cajero, int estadoCaja, String creador){
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
		this.cod_oficina = cod_oficina;
		this.cod_caja = cod_caja;
		this.cajero = cajero;
		this.estadoCaja = estadoCaja;
		this.creador = creador;
	}

	public int getCbbancoagenciacajasid() {
		return cbbancoagenciacajasid;
	}

	public void setCbbancoagenciacajasid(int cbbancoagenciacajasid) {
		this.cbbancoagenciacajasid = cbbancoagenciacajasid;
	}

	public int getCbcatalogoagenciaid() {
		return cbcatalogoagenciaid;
	}

	public void setCbcatalogoagenciaid(int cbcatalogoagenciaid) {
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
	}

	public String getCod_oficina() {
		return cod_oficina;
	}

	public void setCod_oficina(String cod_oficina) {
		this.cod_oficina = cod_oficina;
	}

	public String getCod_caja() {
		return cod_caja;
	}

	public void setCod_caja(String cod_caja) {
		this.cod_caja = cod_caja;
	}

	public String getCajero() {
		return cajero;
	}

	public void setCajero(String cajero) {
		this.cajero = cajero;
	}

	public int getEstadoCaja() {
		return estadoCaja;
	}

	public void setEstadoCaja(int estadoCaja) {
		this.estadoCaja = estadoCaja;
	}

	public String getCreador() {
		return creador;
	}

	public void setCreador(String creador) {
		this.creador = creador;
	}

	public String getFechacreacion() {
		return fechacreacion;
	}

	public void setFechacreacion(String fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

	public String getNombreBanco() {
		return nombreBanco;
	}

	public void setNombreBanco(String nombreBanco) {
		this.nombreBanco = nombreBanco;
	}

	public String getNombreAgencia() {
		return nombreAgencia;
	}

	public void setNombreAgencia(String nombreAgencia) {
		this.nombreAgencia = nombreAgencia;
	}
}
