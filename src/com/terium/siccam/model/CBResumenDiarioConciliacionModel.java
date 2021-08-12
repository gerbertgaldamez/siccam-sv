package com.terium.siccam.model;

import java.math.BigDecimal;
import java.util.Date;

public class CBResumenDiarioConciliacionModel {
	private Date dia;
	private String nombre;
	private String codigoColector; //CarlosGodinez -> 07/08/2018
	private String tipo;
	private BigDecimal transTelefonica;
	private BigDecimal pagosTelefonica;
	private BigDecimal difTransaccion;
	private BigDecimal transBanco;
	private BigDecimal confrontaBanco;
	private BigDecimal conciliadas;
	private BigDecimal automatica;
	private BigDecimal manualTelefonica;
	private BigDecimal manualBanco;
	private BigDecimal pendiente;
	private String estado;
	private String idAgencia;
	private BigDecimal pendienteBanco;
	private BigDecimal pendienteTelefonica;
	private String fechaInicial; //CarlosGodinez -> 10/08/2018
	private String fechaFinal; //CarlosGodinez -> 10/08/2018
    private int tipoid;
   
	

	private BigDecimal real_b;
    private BigDecimal real_t;
	

	public CBResumenDiarioConciliacionModel() {
		super();
		// TODO Auto-generated constructor stub
	}

/*	public CBResumenDiarioConciliacionModel(Date dia, String nombre, String codigoColector, String tipo,
			BigDecimal transTelefonica, BigDecimal pagosTelefonica,
			BigDecimal difTransaccion, BigDecimal transBanco,
			BigDecimal confrontaBanco, BigDecimal conciliadas,
			BigDecimal automatica, BigDecimal manualTelefonica,
			BigDecimal manualBanco, BigDecimal pendiente, String estado,
			String idAgencia, BigDecimal pendienteBanco,
			BigDecimal pendienteTelefonica, String fechaInicial, String fechaFinal) {
		super();
		this.dia = dia;
		this.nombre = nombre;
		this.codigoColector = codigoColector; //CarlosGodinez -> 07/08/2018
		this.tipo = tipo;
		this.transTelefonica = transTelefonica;
		this.pagosTelefonica = pagosTelefonica;
		this.difTransaccion = difTransaccion;
		this.transBanco = transBanco;
		this.confrontaBanco = confrontaBanco;
		this.conciliadas = conciliadas;
		this.automatica = automatica;
		this.manualTelefonica = manualTelefonica;
		this.manualBanco = manualBanco;
		this.pendiente = pendiente;
		this.estado = estado;
		this.idAgencia = idAgencia;
		this.pendienteBanco = pendienteBanco;
		this.pendienteTelefonica = pendienteTelefonica;
		this.fechaInicial = fechaInicial;
		this.fechaFinal = fechaFinal;
	}*/

	public BigDecimal getConciliadas() {
		return conciliadas;
	}

	public void setConciliadas(BigDecimal conciliadas) {
		this.conciliadas = conciliadas;
	}

	public Date getDia() {
		return dia;
	}

	public void setDia(Date dia) {
		this.dia = dia;
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

	public BigDecimal getTransTelefonica() {
		return transTelefonica;
	}

	public void setTransTelefonica(BigDecimal transTelefonica) {
		this.transTelefonica = transTelefonica;
	}

	public BigDecimal getPagosTelefonica() {
		return pagosTelefonica;
	}

	public void setPagosTelefonica(BigDecimal pagosTelefonica) {
		this.pagosTelefonica = pagosTelefonica;
	}

	public BigDecimal getTransBanco() {
		return transBanco;
	}

	public void setTransBanco(BigDecimal transBanco) {
		this.transBanco = transBanco;
	}

	public BigDecimal getConfrontaBanco() {
		return confrontaBanco;
	}

	public void setConfrontaBanco(BigDecimal confrontaBanco) {
		this.confrontaBanco = confrontaBanco;
	}

	public BigDecimal getAutomatica() {
		return automatica;
	}

	public void setAutomatica(BigDecimal automatica) {
		this.automatica = automatica;
	}

	public BigDecimal getManualTelefonica() {
		return manualTelefonica;
	}

	public void setManualTelefonica(BigDecimal manualTelefonica) {
		this.manualTelefonica = manualTelefonica;
	}

	public BigDecimal getManualBanco() {
		return manualBanco;
	}

	public void setManualBanco(BigDecimal manualBanco) {
		this.manualBanco = manualBanco;
	}

	public BigDecimal getPendiente() {
		return pendiente;
	}

	public void setPendiente(BigDecimal pendiente) {
		this.pendiente = pendiente;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(String idAgencia) {
		this.idAgencia = idAgencia;
	}

	public BigDecimal getPendienteBanco() {
		return pendienteBanco;
	}

	public void setPendienteBanco(BigDecimal pendienteBanco) {
		this.pendienteBanco = pendienteBanco;
	}

	public BigDecimal getPendienteTelefonica() {
		return pendienteTelefonica;
	}

	public void setPendienteTelefonica(BigDecimal pendienteTelefonica) {
		this.pendienteTelefonica = pendienteTelefonica;
	}

	public BigDecimal getDifTransaccion() {
		return difTransaccion;
	}

	public void setDifTransaccion(BigDecimal difTransaccion) {
		this.difTransaccion = difTransaccion;
	}
	
	/**
	 * Agrega CarlosGodinez
	 */
	public String getCodigoColector() {
		return codigoColector;
	}

	public void setCodigoColector(String codigoColector) {
		this.codigoColector = codigoColector;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	public int getTipoid() {
		return tipoid;
	}

	public void setTipoid(int tipoid) {
		this.tipoid = tipoid;
	}
	 public BigDecimal getReal_b() {
			return real_b;
		}

		public void setReal_b(BigDecimal real_b) {
			this.real_b = real_b;
		}

		public BigDecimal getReal_t() {
			return real_t;
		}

		public void setReal_t(BigDecimal real_t) {
			this.real_t = real_t;
		}

}
