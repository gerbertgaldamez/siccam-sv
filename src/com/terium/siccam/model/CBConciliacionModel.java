package com.terium.siccam.model;
import java.math.BigDecimal; 
 
 
/**
 *
 * @author rSianB for terium.com 
 */ 
public class  CBConciliacionModel {  
 
    public final static String TABLE = "CB_CONCILIACION"; 
    public final static String SEQUENCE = "cb_conciliacion_sq.nextval";
    public final static String PKFIELD_CBCONCILIACIONID = "cBConciliacionId"; 
    public final static String FIELD_COD_CLIENTE = "cod_Cliente"; 
    public final static String FIELD_TELEFONO = "telefono"; 
    public final static String FIELD_FECHA = "fecha"; 
    public final static String FIELD_MONTO = "monto"; 
    public final static String FIELD_ESTADO = "estado"; 
    public final static String FIELD_MES = "mes"; 
    public final static String FIELD_CREADO_POR = "creado_Por"; 
    public final static String FIELD_MODIFICADO_POR = "modificado_Por"; 
    public final static String FIELD_FECHA_CREACION = "fecha_Creacion"; 
    public final static String FIELD_FECHA_MODIFICACION = "fecha_Modificacion"; 


    private int cBConciliacionId;
    private String codCliente;
    private String telefono;
    private String fecha;
    private BigDecimal monto;
    private int estado;
    private String mes;
    private String creadoPor;
    private String modificadoPor;
    private String fechaCreacion;
    private String fechaModificacion;

    /**
     * @return the cBConciliacionId
     */
    public int getCBConciliacionId() {
        return cBConciliacionId;
    }

    /**
     * @param  cBConciliacionId the cBConciliacionId to set
     */
    public void setCBConciliacionId (int cBConciliacionId) {
        this.cBConciliacionId = cBConciliacionId;
    }

    /**
     * @return the codCliente
     */
    public String getCodCliente() {
        return codCliente;
    }

    /**
     * @param  codCliente the codCliente to set
     */
    public void setCodCliente (String codCliente) {
        this.codCliente = codCliente;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * @param  telefono the telefono to set
     */
    public void setTelefono (String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param  fecha the fecha to set
     */
    public void setFecha (String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the monto
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * @param  monto the monto to set
     */
    public void setMonto (BigDecimal monto) {
        this.monto = monto;
    }

    /**
     * @return the estado
     */
    public int getEstado() {
        return estado;
    }

    /**
     * @param  estado the estado to set
     */
    public void setEstado (int estado) {
        this.estado = estado;
    }

    /**
     * @return the mes
     */
    public String getMes() {
        return mes;
    }

    /**
     * @param  mes the mes to set
     */
    public void setMes (String mes) {
        this.mes = mes;
    }

    /**
     * @return the creadoPor
     */
    public String getCreadoPor() {
        return creadoPor;
    }

    /**
     * @param  creadoPor the creadoPor to set
     */
    public void setCreadoPor (String creadoPor) {
        this.creadoPor = creadoPor;
    }

    /**
     * @return the modificadoPor
     */
    public String getModificadoPor() {
        return modificadoPor;
    }

    /**
     * @param  modificadoPor the modificadoPor to set
     */
    public void setModificadoPor (String modificadoPor) {
        this.modificadoPor = modificadoPor;
    }

    /**
     * @return the fechaCreacion
     */
    public String getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param  fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion (String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return the fechaModificacion
     */
    public String getFechaModificacion() {
        return fechaModificacion;
    }

    /**
     * @param  fechaModificacion the fechaModificacion to set
     */
    public void setFechaModificacion (String fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
 } 