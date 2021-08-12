package com.terium.siccam.model;
 
 
/**
 *
 * @author rSianB for terium.com 
 */ 
public class  CBHistorialAccionModel {  
 
    public final static String TABLE = "CB_HISTORIAL_ACCION"; 
    public final static String SEQUENCE = "cb_historial_accion_sq.nextval";
    public final static String PKFIELD_CBHISTORIALACCIONID = "cBHistorialAccionId"; 
    public final static String FIELD_CBDATATELEFONICAID = "cBDataTelefonicaId"; 
    public final static String FIELD_CBDATABANCOID = "cBDataBancoId"; 
    public final static String FIELD_FECHA = "fecha"; 
    public final static String FIELD_TIPO = "tipo"; 
    public final static String FIELD_ESTADO = "estado"; 
    public final static String FIELD_ACCION = "accion"; 
    public final static String FIELD_OBSERVACIONES = "observaciones"; 
    public final static String FIELD_CREADO_POR = "creado_Por"; 
    public final static String FIELD_MODIFICADO_POR = "modificado_Por"; 
    public final static String FIELD_FECHA_CREACION = "fecha_Creacion"; 
    public final static String FIELD_FECHA_MODIFICACION = "fecha_Modificacion"; 


    private int cBHistorialAccionId;
    private String cbPagosId;
    private int cBDataBancoId;
    private int cBConciliacionId;
    private String fecha;
    private int tipo;
    private int estado;
    private String accion;
    private String monto;
    private String observaciones;
    private String creadoPor;
    private String modificadoPor;
    private String fechaCreacion;
    private String fechaModificacion;
    
    /**
     * @author juankrlos
     * @date 19/06/2021
     * Campos agregados para consumo de WS Pagos y Servicio
     * */
    private String idAgencia;
    private String telefono;
    private String tipologiaGacId;
    private String unidadId;
    private String solucion;
    private String tipoCierre;
    private String clasifTipologia;
    private String origenId;
    private String agencia;
    private String tipoOperacion;
    private String responseGac;
    private String nombreCliente;
    private String respuestascl;
    private int cbCausasConciliacionId;
    
	
	public CBHistorialAccionModel(int cBHistorialAccionId, String cbPagosId,
			int cBDataBancoId, int cBConciliacionId, String fecha, int tipo,
			int estado, String accion, String monto, String observaciones,
			String creadoPor, String modificadoPor, String fechaCreacion,
			String fechaModificacion) {
		super();
		this.cBHistorialAccionId = cBHistorialAccionId;
		this.cbPagosId = cbPagosId;
		this.cBDataBancoId = cBDataBancoId;
		this.cBConciliacionId = cBConciliacionId;
		this.fecha = fecha;
		this.tipo = tipo;
		this.estado = estado;
		this.accion = accion;
		this.monto = monto;
		this.observaciones = observaciones;
		this.creadoPor = creadoPor;
		this.modificadoPor = modificadoPor;
		this.fechaCreacion = fechaCreacion;
		this.fechaModificacion = fechaModificacion;
		
	}
	public CBHistorialAccionModel() {
		// TODO Auto-generated constructor stub
	}
	public int getcBHistorialAccionId() {
		return cBHistorialAccionId;
	}
	public void setcBHistorialAccionId(int cBHistorialAccionId) {
		this.cBHistorialAccionId = cBHistorialAccionId;
	}
	public String getCbPagosId() {
		return cbPagosId;
	}
	public void setCbPagosId(String cbPagosId) {
		this.cbPagosId = cbPagosId;
	}
	public int getcBDataBancoId() {
		return cBDataBancoId;
	}
	public void setcBDataBancoId(int cBDataBancoId) {
		this.cBDataBancoId = cBDataBancoId;
	}
	public int getcBConciliacionId() {
		return cBConciliacionId;
	}
	public void setcBConciliacionId(int cBConciliacionId) {
		this.cBConciliacionId = cBConciliacionId;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getAccion() {
		return accion;
	}
	public void setAccion(String accion) {
		this.accion = accion;
	}
	public String getMonto() {
		return monto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getCreadoPor() {
		return creadoPor;
	}
	public void setCreadoPor(String creadoPor) {
		this.creadoPor = creadoPor;
	}
	public String getModificadoPor() {
		return modificadoPor;
	}
	public void setModificadoPor(String modificadoPor) {
		this.modificadoPor = modificadoPor;
	}
	public String getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	
	//Nuevos campos consumo WS
	public String getIdAgencia() {
		return idAgencia;
	}
	public void setIdAgencia(String idAgencia) {
		this.idAgencia = idAgencia;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getTipologiaGacId() {
		return tipologiaGacId;
	}
	public void setTipologiaGacId(String tipologiaGacId) {
		this.tipologiaGacId = tipologiaGacId;
	}
	public String getUnidadId() {
		return unidadId;
	}
	public void setUnidadId(String unidadId) {
		this.unidadId = unidadId;
	}
	public String getSolucion() {
		return solucion;
	}
	public void setSolucion(String solucion) {
		this.solucion = solucion;
	}
	public String getTipoCierre() {
		return tipoCierre;
	}
	public void setTipoCierre(String tipoCierre) {
		this.tipoCierre = tipoCierre;
	}
	public String getClasifTipologia() {
		return clasifTipologia;
	}
	public void setClasifTipologia(String clasifTipologia) {
		this.clasifTipologia = clasifTipologia;
	}
	public String getOrigenId() {
		return origenId;
	}
	public void setOrigenId(String origenId) {
		this.origenId = origenId;
	}
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public String getTipoOperacion() {
		return tipoOperacion;
	}
	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}
	public String getResponseGac() {
		return responseGac;
	}
	public void setResponseGac(String responseGac) {
		this.responseGac = responseGac;
	}
	public String getNombreCliente() {
		return nombreCliente;
	}
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}
	public String getRespuestascl() {
		return respuestascl;
	}
	public void setRespuestascl(String respuestascl) {
		this.respuestascl = respuestascl;
	}
	public int getCbCausasConciliacionId() {
		return cbCausasConciliacionId;
	}
	public void setCbCausasConciliacionId(int cbCausasConciliacionId) {
		this.cbCausasConciliacionId = cbCausasConciliacionId;
	}
	
	

 } 