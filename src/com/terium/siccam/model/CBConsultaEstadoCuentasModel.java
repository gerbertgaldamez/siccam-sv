package com.terium.siccam.model;

public class CBConsultaEstadoCuentasModel {

	private int idbanco;
	private int idagencia;
	private int idtipologia;
	private String banco;
    private String agencia;
    private String fecha;
    private String cuenta;
    private String texto;
    private String debe;
    private String haber;
    private String identificador;
    private String observaciones;
    //Propiedades agregadas por CarlosGodinez - Qitcorp - 01/03/2017
    private int cbestadocuentasociedad;
    private String tipologia;
    private String agenciaTipologia;
    private String fechaIngresos;
    private String numDocumento; //agregado por Omar Gomez -QIT - 07/08/2017
    
    //Propiedades agregadas por CarlosGodinez - Qitcorp - 21/03/2017
    private int cbestadocuentaid;
    private int cbestadocuentavisadetid;
    
    //Propiedades agregadas por CarlosGodinez - Qitcorp -07/07/2017
    private String asignacion;
    private String textoCabDoc;
  
	private String tipoTarjeta;
    private String fechaTransaccion;
    private String afiliacion;
    private String tipo;
    private String referencia;
    private String liquido;
    private String comision;
    private String ivaComision;
    private String retencion;
    private String consumo;
    private String nombreEntidad;
    private String codigoColector;
    
    private String modificadoPor;
    private String fechaModificacion;
    
    private int idAgenciaTipologia; //CarlosGodinez -> 20/08/2018
    
    public String getCodigoColector() {
		return codigoColector;
	}
	public void setCodigoColector(String codigoColector) {
		this.codigoColector = codigoColector;
	}
	public int getIdtipologia() {
		return idtipologia;
	}
	public void setIdtipologia(int idtipologia) {
		this.idtipologia = idtipologia;
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
	public int getIdbanco() {
		return idbanco;
	}
	public void setIdbanco(int idbanco) {
		this.idbanco = idbanco;
	}
	public int getIdagencia() {
		return idagencia;
	}
	public void setIdagencia(int idagencia) {
		this.idagencia = idagencia;
	}
	public String getBanco() {
		return banco;
	}
	public void setBanco(String banco) {
		this.banco = banco;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getDebe() {
		return debe;
	}
	public void setDebe(String debe) {
		this.debe = debe;
	}
	public String getHaber() {
		return haber;
	}
	public void setHaber(String haber) {
		this.haber = haber;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public int getCbestadocuentasociedad() {
		return cbestadocuentasociedad;
	}
	public void setCbestadocuentasociedad(int cbestadocuentasociedad) {
		this.cbestadocuentasociedad = cbestadocuentasociedad;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	
	public String getAgenciaTipologia() {
		return agenciaTipologia;
	}
	public void setAgenciaTipologia(String agenciaTipologia) {
		this.agenciaTipologia = agenciaTipologia;
	}
	public String getFechaIngresos() {
		return fechaIngresos;
	}
	public void setFechaIngresos(String fechaIngresos) {
		this.fechaIngresos = fechaIngresos;
	}
	public String getTipoTarjeta() {
		return tipoTarjeta;
	}
	public void setTipoTarjeta(String tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}
	public String getFechaTransaccion() {
		return fechaTransaccion;
	}
	public void setFechaTransaccion(String fechaTransaccion) {
		this.fechaTransaccion = fechaTransaccion;
	}
	public String getAfiliacion() {
		return afiliacion;
	}
	public void setAfiliacion(String afiliacion) {
		this.afiliacion = afiliacion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getLiquido() {
		return liquido;
	}
	public void setLiquido(String liquido) {
		this.liquido = liquido;
	}
	public String getComision() {
		return comision;
	}
	public void setComision(String comision) {
		this.comision = comision;
	}
	public String getIvaComision() {
		return ivaComision;
	}
	public void setIvaComision(String ivaComision) {
		this.ivaComision = ivaComision;
	}
	public String getRetencion() {
		return retencion;
	}
	public void setRetencion(String retencion) {
		this.retencion = retencion;
	}
	public String getConsumo() {
		return consumo;
	}
	public void setConsumo(String consumo) {
		this.consumo = consumo;
	}

	public String getNombreEntidad() {
		return nombreEntidad;
	}
	public void setNombreEntidad(String nombreEntidad) {
		this.nombreEntidad = nombreEntidad;
	}

	public int getCbestadocuentaid() {
		return cbestadocuentaid;
	}

	public void setCbestadocuentaid(int cbestadocuentaid) {
		this.cbestadocuentaid = cbestadocuentaid;
	}

	public int getCbestadocuentavisadetid() {
		return cbestadocuentavisadetid;
	}

	public void setCbestadocuentavisadetid(int cbestadocuentavisadetid) {
		this.cbestadocuentavisadetid = cbestadocuentavisadetid;
	}
	public String getAsignacion() {
		return asignacion;
	}
	public void setAsignacion(String asignacion) {
		this.asignacion = asignacion;
	}
	public String getTextoCabDoc() {
		return textoCabDoc;
	}
	public void setTextoCabDoc(String textoCabDoc) {
		this.textoCabDoc = textoCabDoc;
	}
	public String getNumDocumento() {
		return numDocumento;
	}
	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}
	public int getIdAgenciaTipologia() {
		return idAgenciaTipologia;
	}
	public void setIdAgenciaTipologia(int idAgenciaTipologia) {
		this.idAgenciaTipologia = idAgenciaTipologia;
	}
	
}
