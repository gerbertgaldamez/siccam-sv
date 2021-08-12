package com.terium.siccam.model;

import java.math.BigDecimal;

/**
 * 
 * @author rSianB for terium.com
 */
public class CBDataBancoModel {

	public final static String TABLE = "CB_DATA_BANCO";
	public final static String SEQUENCE = "cb_data_banco_sq.nextval";
	public final static String PKFIELD_CBDATABANCOID = "cBDataBancoId";
	public final static String FIELD_COD_CLIENTE = "cod_Cliente";
	public final static String FIELD_TELEFONO = "telefono";
	public final static String FIELD_FECHA = "fecha";
	public final static String FIELD_CBCATALOGOBANCOID = "cBCatalogoBancoId";
	public final static String FIELD_CBCATALOGOAGENCIAID = "cBCatalogoAgenciaId";
	public final static String FIELD_CBBANCOAGENCIACONFRONTAID = "cBBancoAgenciaConfrontaId";
	public final static String FIELD_MONTO = "monto";
	public final static String FIELD_TRANSACCION = "transaccion";
	public final static String FIELD_ESTADO = "estado";
	public final static String FIELD_MES = "mes";
	public final static String FIELD_TEXTO1 = "texto1";
	public final static String FIELD_TEXTO2 = "texto2";
	public final static String FIELD_CREADO_POR = "creado_Por";
	public final static String FIELD_MODIFICADO_POR = "modificado_Por";
	public final static String FIELD_FECHA_CREACION = "fecha_Creacion";
	public final static String FIELD_FECHA_MODIFICACION = "fecha_Modificacion";

	private String nombre;
	private String cBDataBancoId;
	private String codCliente;
	private String telefono;
	private String fecha;
	private String cBCatalogoBancoId;
	private String cBCatalogoAgenciaId;
	private String cBBancoAgenciaConfrontaId;
	private BigDecimal monto;
	private String transaccion;
	private int estado;
	private String mes;
	private String dia;
	private String texto1;
	private String texto2;
	private String creadoPor;
	private String modificadoPor;
	private String fechaCreacion;
	private String fechaModificacion;
	private String tipo;
	private String idCargaMaestro;
	private String observacion;
	private String cbAgenciaVirfisCodigo;
	private BigDecimal comision;
	private String formatofecha;
	//Temporal para confrontas con mas de 1 convenio
		private boolean convenioconf;

	public boolean isConvenioconf() {
			return convenioconf;
		}

		public void setConvenioconf(boolean convenioconf) {
			this.convenioconf = convenioconf;
		}

	//Propiedades agregadas por CarlosGodinez - QitCorp - 19/04/2017
	//Estas propiedades se utilizaran para la validacion de carga de confrontas en El Salvador
	private String valorObjeto1;
	private String valorObjeto2;
	private String valorObjeto3;
	
	//Juankrlos --> 13/09/2017
	private String estadocomision;
	
	/**
	 * @return the cBDataBancoId
	 */
	public String getCBDataBancoId() {
		return cBDataBancoId;
	}

	/**
	 * @param cBDataBancoId
	 *            the cBDataBancoId to set
	 */
	public void setCBDataBancoId(String cBDataBancoId) {
		this.cBDataBancoId = cBDataBancoId;
	}

	/**
	 * @return the codCliente
	 */
	public String getCodCliente() {
		return codCliente;
	}

	/**
	 * @param codCliente
	 *            the codCliente to set
	 */
	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
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
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha
	 *            the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getcBCatalogoBancoId() {
		return cBCatalogoBancoId;
	}

	public void setcBCatalogoBancoId(String cBCatalogoBancoId) {
		this.cBCatalogoBancoId = cBCatalogoBancoId;
	}

	public String getcBCatalogoAgenciaId() {
		return cBCatalogoAgenciaId;
	}

	public void setcBCatalogoAgenciaId(String cBCatalogoAgenciaId) {
		this.cBCatalogoAgenciaId = cBCatalogoAgenciaId;
	}

	/**
	 * @return the cBBancoAgenciaConfrontaId
	 */
	public String getCBBancoAgenciaConfrontaId() {
		return cBBancoAgenciaConfrontaId;
	}

	/**
	 * @param cBBancoAgenciaConfrontaId
	 *            the cBBancoAgenciaConfrontaId to set
	 */
	public void setCBBancoAgenciaConfrontaId(String cBBancoAgenciaConfrontaId) {
		this.cBBancoAgenciaConfrontaId = cBBancoAgenciaConfrontaId;
	}

	/**
	 * @return the monto
	 */
	public BigDecimal getMonto() {
		return monto;
	}

	/**
	 * @param monto
	 *            the monto to set
	 */
	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	/**
	 * @return the transaccion
	 */
	public String getTransaccion() {
		return transaccion;
	}

	/**
	 * @param transaccion
	 *            the transaccion to set
	 */
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	/**
	 * @return the estado
	 */
	public int getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}

	/**
	 * @return the mes
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * @param mes
	 *            the mes to set
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getDia() {
		return dia;
	}

	public void setDia(String dia) {
		this.dia = dia;
	}

	/**
	 * @return the texto1
	 */
	public String getTexto1() {
		return texto1;
	}

	/**
	 * @param texto1
	 *            the texto1 to set
	 */
	public void setTexto1(String texto1) {
		this.texto1 = texto1;
	}

	/**
	 * @return the texto2
	 */
	public String getTexto2() {
		return texto2;
	}

	/**
	 * @param texto2
	 *            the texto2 to set
	 */
	public void setTexto2(String texto2) {
		this.texto2 = texto2;
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

	public String getIdCargaMaestro() {
		return idCargaMaestro;
	}

	public void setIdCargaMaestro(String idCargaMaestro) {
		this.idCargaMaestro = idCargaMaestro;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getCbAgenciaVirfisCodigo() {
		return cbAgenciaVirfisCodigo;
	}

	public void setCbAgenciaVirfisCodigo(String cbAgenciaVirfisCodigo) {
		this.cbAgenciaVirfisCodigo = cbAgenciaVirfisCodigo;
	}

	public BigDecimal getComision() {
		return comision;
	}

	public void setComision(BigDecimal comision) {
		this.comision = comision;
	}

	public String getFormatofecha() {
		return formatofecha;
	}

	public void setFormatofecha(String formatofecha) {
		this.formatofecha = formatofecha;
	}

	public String getValorObjeto1() {
		return valorObjeto1;
	}

	public void setValorObjeto1(String valorObjeto1) {
		this.valorObjeto1 = valorObjeto1;
	}

	public String getValorObjeto2() {
		return valorObjeto2;
	}

	public void setValorObjeto2(String valorObjeto2) {
		this.valorObjeto2 = valorObjeto2;
	}

	public String getValorObjeto3() {
		return valorObjeto3;
	}

	public void setValorObjeto3(String valorObjeto3) {
		this.valorObjeto3 = valorObjeto3;
	}

	public String getEstadocomision() {
		return estadocomision;
	}

	public void setEstadocomision(String estadocomision) {
		this.estadocomision = estadocomision;
	}
	
	
}