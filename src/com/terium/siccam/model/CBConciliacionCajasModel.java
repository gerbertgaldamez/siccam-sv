package com.terium.siccam.model;

/**
 * @author Juankrlos
 * */
public class CBConciliacionCajasModel {
	
	//Datos editados por Carlos Godinez - Qitcorp - 12/05/2017
	
	private int cbcatalogobancoid;
	private String entidad;
	private int cbcatalogoagenciaid;
	private String agencia;
	private String fecha;
	private double cajacuotasvisa;
	private double cajacuotascredomatic;
	private double cajatarjetacredomatic;
	private double cajatarjetaotras;
	private double cajatarjetavisa;
	private double cajacheque;
	private double cajaexenciones;
	private double cajaefectivo;
	//private double cajadeposito;
	private double cajatotal;
	//private double sctarjeta;
	private double scefectivo;
	//private double sccheque;
	//private double scexension;
	//private double sccuotas;
	private double scpagosod;
	private double scpagosom;
	private double screversasod;
	private double screversasom;
	private double totaldia; //Nueva propiedad para 12/05/2017
	//private double totalsc;
	private double credomaticdep;
	private double consumovisa;
	private double ivavisa;
	private double deposito;
	private double totalec;
	private double diferencia;
	
	//Datos para los combos filtros
	private int idcombo;
	private String nombre;
	
	//Campos agregados por Carlos Godínez - Qitcorp - 23/03/2017
	private double credomaticRet;
	private double estadoCredo;
	private double estadoVisa;
	
	public int getIdcombo() {
		return idcombo;
	}
	public void setIdcombo(int idcombo) {
		this.idcombo = idcombo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCbcatalogobancoid() {
		return cbcatalogobancoid;
	}
	public void setCbcatalogobancoid(int cbcatalogobancoid) {
		this.cbcatalogobancoid = cbcatalogobancoid;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public int getCbcatalogoagenciaid() {
		return cbcatalogoagenciaid;
	}
	public void setCbcatalogoagenciaid(int cbcatalogoagenciaid) {
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public double getCajacuotasvisa() {
		return cajacuotasvisa;
	}
	public void setCajacuotasvisa(double cajacuotasvisa) {
		this.cajacuotasvisa = cajacuotasvisa;
	}
	public double getCajacuotascredomatic() {
		return cajacuotascredomatic;
	}
	public void setCajacuotascredomatic(double cajacuotascredomatic) {
		this.cajacuotascredomatic = cajacuotascredomatic;
	}
	public double getCajatarjetacredomatic() {
		return cajatarjetacredomatic;
	}
	public void setCajatarjetacredomatic(double cajatarjetacredomatic) {
		this.cajatarjetacredomatic = cajatarjetacredomatic;
	}
	public double getCajatarjetaotras() {
		return cajatarjetaotras;
	}
	public void setCajatarjetaotras(double cajatarjetaotras) {
		this.cajatarjetaotras = cajatarjetaotras;
	}
	public double getCajatarjetavisa() {
		return cajatarjetavisa;
	}
	public void setCajatarjetavisa(double cajatarjetavisa) {
		this.cajatarjetavisa = cajatarjetavisa;
	}
	public double getCajacheque() {
		return cajacheque;
	}
	public void setCajacheque(double cajacheque) {
		this.cajacheque = cajacheque;
	}
	public double getCajaexenciones() {
		return cajaexenciones;
	}
	public void setCajaexenciones(double cajaextenciones) {
		this.cajaexenciones = cajaextenciones;
	}
	public double getCajaefectivo() {
		return cajaefectivo;
	}
	public void setCajaefectivo(double cajaefectivo) {
		this.cajaefectivo = cajaefectivo;
	}
	/*
	public double getCajadeposito() {
		return cajadeposito;
	}
	public void setCajadeposito(double cajadeposito) {
		this.cajadeposito = cajadeposito;
	}
	*/
	public double getCajatotal() {
		return cajatotal;
	}
	public void setCajatotal(double cajatotal) {
		this.cajatotal = cajatotal;
	}
	/*
	public double getSctarjeta() {
		return sctarjeta;
	}
	public void setSctarjeta(double sctarjeta) {
		this.sctarjeta = sctarjeta;
	}
	*/
	
	public double getScefectivo() {
		return scefectivo;
	}
	public void setScefectivo(double scefectivo) {
		this.scefectivo = scefectivo;
	}
	
	/*
	public double getSccheque() {
		return sccheque;
	}
	public void setSccheque(double sccheque) {
		this.sccheque = sccheque;
	}
	*/
	/*
	public double getScexension() {
		return scexension;
	}
	public void setScexension(double scexension) {
		this.scexension = scexension;
	}
	*/
	/*
	public double getSccuotas() {
		return sccuotas;
	}
	public void setSccuotas(double sccuotas) {
		this.sccuotas = sccuotas;
	}
	*/
	public double getScpagosod() {
		return scpagosod;
	}
	public void setScpagosod(double scpagosod) {
		this.scpagosod = scpagosod;
	}
	public double getScpagosom() {
		return scpagosom;
	}
	public void setScpagosom(double scpagosom) {
		this.scpagosom = scpagosom;
	}
	public double getScreversasod() {
		return screversasod;
	}
	public void setScreversasod(double screversasod) {
		this.screversasod = screversasod;
	}
	public double getScreversasom() {
		return screversasom;
	}
	public void setScreversasom(double screversasom) {
		this.screversasom = screversasom;
	}
	/*
	public double getTotalsc() {
		return totalsc;
	}
	public void setTotalsc(double totalsc) {
		this.totalsc = totalsc;
	}
	*/
	
	//Nueva propiedad para el 12/05/2017
	public double getTotaldia() {
		return totaldia;
	}
	public void setTotaldia(double totaldia) {
		this.totaldia = totaldia;
	}
	public double getCredomaticdep() {
		return credomaticdep;
	}
	public void setCredomaticdep(double credomaticdep) {
		this.credomaticdep = credomaticdep;
	}
	public double getConsumovisa() {
		return consumovisa;
	}
	public void setConsumovisa(double consumovisa) {
		this.consumovisa = consumovisa;
	}
	public double getIvavisa() {
		return ivavisa;
	}
	public void setIvavisa(double ivavisa) {
		this.ivavisa = ivavisa;
	}
	public double getDeposito() {
		return deposito;
	}
	public void setDeposito(double deposito) {
		this.deposito = deposito;
	}
	public double getTotalec() {
		return totalec;
	}
	public void setTotalec(double totalec) {
		this.totalec = totalec;
	}
	public double getDiferencia() {
		return diferencia;
	}
	public void setDiferencia(double diferencia) {
		this.diferencia = diferencia;
	}
	public double getCredomaticRet() {
		return credomaticRet;
	}
	public void setCredomaticRet(double credomaticRet) {
		this.credomaticRet = credomaticRet;
	}
	public double getEstadoCredo() {
		return estadoCredo;
	}
	public void setEstadoCredo(double estadoCredo) {
		this.estadoCredo = estadoCredo;
	}
	public double getEstadoVisa() {
		return estadoVisa;
	}
	public void setEstadoVisa(double estadoVisa) {
		this.estadoVisa = estadoVisa;
	}
}
