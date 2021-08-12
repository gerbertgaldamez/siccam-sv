package com.terium.siccam.model;
 
 
/**
 *
 * @author rSianB for terium.com 
 */ 
public class  CBPagosModel {  
 
    public final static String TABLE = "CB_PAGOS"; 
    public final static String SEQUENCE = "cb_pagos_sq.nextval";
    public final static String PKFIELD_CBPAGOSID = "cBPagosId"; 
    public final static String FIELD_FEC_EFECTIVIDAD = "fec_Efectividad"; 
    public final static String FIELD_NUM_SECUENCI = "num_Secuenci"; 
    public final static String FIELD_COD_CLIENTE = "cod_Cliente"; 
    public final static String FIELD_TELEFONO = "telefono"; 
    public final static String FIELD_IMP_PAGO = "imp_Pago"; 
    
    public final static String FIELD_TIPO = "tipo"; 
    public final static String FIELD_COD_CAJA = "cod_Caja"; 
    public final static String FIELD_DES_PAGO = "des_Pago"; 
    public final static String FIELD_ESTADO_CONCILIADO = "estado_Conciliado"; 
    public final static String FIELD_NUM_CONCILIACION = "num_Conciliacion"; 
    public final static String FIELD_fecha = "fecha"; 
    
    public final static String FIELD_NOM_CLIENTE = "NOM_CLIENTE"; 
    public final static String FIELD_COD_CLICLO = "COD_CLICLO"; 
    public final static String FIELD_TRANSACCION = "TRANSACCION"; 
    public final static String FIELD_TIPO_TRANSACCION = "TIPO_TRANSACCION"; 
    public final static String FIELD_TIP_MOVCAJA = "TIP_MOVCAJA"; 
    public final static String FIELD_TIP_VALOR = "TIP_VALOR"; 
    public final static String FIELD_NOM_USUARORA = "NOM_USUARORA"; 
    public final static String FIELD_COD_BANCO = "COD_BANCO"; 
    public final static String FIELD_COD_OFICINA = "COD_OFICINA"; 
    public final static String FIELD_COD_SEGMENTO = "COD_SEGMENTO"; 
    public final static String FIELD_DES_SEGMENTO = "DES_SEGMENTO"; 
    public final static String FIELD_COD_MONEDA = "COD_MONEDA"; 
    
    public final static String FIELD_CBBANCOAGENCIACONFRONTAID = "cbbancoagenciaconfrontaid"; 
    public final static String FIELD_FEC_TRANSACCIONAL = "fec_transaccional"; 
   
    
    
    public final static String FIELD_CREADO_POR = "creado_Por"; 
    public final static String FIELD_FECHA_CREACION = "fecha_Creacion"; 


    private int cBPagosId;
    private String fecEfectividad;
    private String numSecuenci;
    private String codCliente;
    private String telefono;
    private String impPago;
   
	private String codCaja;
    private String desPago;
    private String tipo;
    private String estadoConciliado;
    private String numConciliacion;
    private String fecha;
    private String creadoPor;
    private String fechaCreacion;
    //agregados Ovidio
    private String cbBancoAgenciaConfrontaId; //CBBANCOAGENCIACONFRONTAID
    private String fechaTransaccional;  //  FEC_TRANSACCIONAL
    private String nonCliente;
    private String codCliclo;
    private String transaccion;
    private String tipoTransaccion;
    private String tipoMovCaja;
    private String tipoValor;
    private String nomUsuarora;
    private String codBanco;
    private String codOficina;
    private String codSegmento;
    private String desSegmento;
    
    /**
     * Agrega CarlosGodinez -> 12/09/2018
     * Propiedades adicionales para reporte de recaudacion
     */
    private String desOficina;
    private String desMovCaja;
    private String desTipoValor;
    private String desBanco;
    private String desCaja;
    private String observacion;
    
    public int getCbestadocuentaconfid() {
		return cbestadocuentaconfid;
	}

	public void setCbestadocuentaconfid(int cbestadocuentaconfid) {
		this.cbestadocuentaconfid = cbestadocuentaconfid;
	}

	public int getCbestadocuentaarchivosid() {
		return cbestadocuentaarchivosid;
	}

	public void setCbestadocuentaarchivosid(int cbestadocuentaarchivosid) {
		this.cbestadocuentaarchivosid = cbestadocuentaarchivosid;
	}

	private String codMoneda;
    
    // agregados por Omar Gomez
    private int cbestadocuentaconfid;
    private int cbestadocuentaarchivosid;
    
    public String getCbBancoAgenciaConfrontaId() {
		return cbBancoAgenciaConfrontaId;
	}

	public void setCbBancoAgenciaConfrontaId(String cbBancoAgenciaConfrontaId) {
		this.cbBancoAgenciaConfrontaId = cbBancoAgenciaConfrontaId;
	}

	public String getFechaTransaccional() {
		return fechaTransaccional;
	}

	public void setFechaTransaccional(String fechaTransaccional) {
		this.fechaTransaccional = fechaTransaccional;
	}


    /**
     * @return the cBPagosId
     */
    public int getCBPagosId() {
        return cBPagosId;
    }

    /**
     * @param  cBPagosId the cBPagosId to set
     */
    public void setCBPagosId (int cBPagosId) {
        this.cBPagosId = cBPagosId;
    }

    public String getNonCliente() {
		return nonCliente;
	}

	public void setNonCliente(String nonCliente) {
		this.nonCliente = nonCliente;
	}

	public String getCodCliclo() {
		return codCliclo;
	}

	public void setCodCliclo(String codCliclo) {
		this.codCliclo = codCliclo;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public String getTipoTransaccion() {
		return tipoTransaccion;
	}

	public void setTipoTransaccion(String tipoTransaccion) {
		this.tipoTransaccion = tipoTransaccion;
	}

	public String getTipoMovCaja() {
		return tipoMovCaja;
	}

	public void setTipoMovCaja(String tipoMovCaja) {
		this.tipoMovCaja = tipoMovCaja;
	}

	public String getTipoValor() {
		return tipoValor;
	}

	public void setTipoValor(String tipoValor) {
		this.tipoValor = tipoValor;
	}

	public String getNomUsuarora() {
		return nomUsuarora;
	}

	public void setNomUsuarora(String nomUsuarora) {
		this.nomUsuarora = nomUsuarora;
	}

	public String getCodBanco() {
		return codBanco;
	}

	public void setCodBanco(String codBanco) {
		this.codBanco = codBanco;
	}

	public String getCodOficina() {
		return codOficina;
	}

	public void setCodOficina(String codOficina) {
		this.codOficina = codOficina;
	}

	public String getCodSegmento() {
		return codSegmento;
	}

	public void setCodSegmento(String codSegmento) {
		this.codSegmento = codSegmento;
	}

	public String getDesSegmento() {
		return desSegmento;
	}

	public void setDesSegmento(String desSegmento) {
		this.desSegmento = desSegmento;
	}

	public String getCodMoneda() {
		return codMoneda;
	}

	public void setCodMoneda(String codMoneda) {
		this.codMoneda = codMoneda;
	}

	/**
     * @return the fecEfectividad
     */
    public String getFecEfectividad() {
        return fecEfectividad;
    }

    /**
     * @param  fecEfectividad the fecEfectividad to set
     */
    public void setFecEfectividad (String fecEfectividad) {
        this.fecEfectividad = fecEfectividad;
    }

    /**
     * @return the numSecuenci
     */
    public String getNumSecuenci() {
        return numSecuenci;
    }

    /**
     * @param  numSecuenci the numSecuenci to set
     */
    public void setNumSecuenci (String numSecuenci) {
        this.numSecuenci = numSecuenci;
    }

    /**
     * @return the codCliente
     */
    

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }

    public String getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(String codCliente) {
		this.codCliente = codCliente;
	}

	/**
     * @param  telefono the telefono to set
     */
    public void setTelefono (String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the impPago
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param  impPago the impPago to set
     */
    public void setTipo (String tipo) {
        this.tipo = tipo;
    }
    
    

    public String getImpPago() {
		return impPago;
	}

	public void setImpPago(String impPago) {
		this.impPago = impPago;
	}

	/**
     * @return the codCaja
     */
    public String getCodCaja() {
        return codCaja;
    }

    /**
     * @param  codCaja the codCaja to set
     */
    public void setCodCaja (String codCaja) {
        this.codCaja = codCaja;
    }

    /**
     * @return the desPago
     */
    public String getDesPago() {
        return desPago;
    }

    /**
     * @param  desPago the desPago to set
     */
    public void setDesPago (String desPago) {
        this.desPago = desPago;
    }

    /**
     * @return the estadoConciliado
     */
    public String getEstadoConciliado() {
        return estadoConciliado;
    }

    /**
     * @param  estadoConciliado the estadoConciliado to set
     */
    public void setEstadoConciliado (String estadoConciliado) {
        this.estadoConciliado = estadoConciliado;
    }

    /**
     * @return the numConciliacion
     */
   
/*
    * @return the numConciliacion
    	     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param  numConciliacion the numConciliacion to set
     */
    public void setFecha (String fecha) {
        this.fecha = fecha;
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

	public String getNumConciliacion() {
		return numConciliacion;
	}

	public void setNumConciliacion(String numConciliacion) {
		this.numConciliacion = numConciliacion;
	}

	public String getDesOficina() {
		return desOficina;
	}

	public void setDesOficina(String desOficina) {
		this.desOficina = desOficina;
	}

	public String getDesMovCaja() {
		return desMovCaja;
	}

	public void setDesMovCaja(String desMovCaja) {
		this.desMovCaja = desMovCaja;
	}

	public String getDesTipoValor() {
		return desTipoValor;
	}

	public void setDesTipoValor(String desTipoValor) {
		this.desTipoValor = desTipoValor;
	}

	public String getDesBanco() {
		return desBanco;
	}

	public void setDesBanco(String desBanco) {
		this.desBanco = desBanco;
	}

	public String getDesCaja() {
		return desCaja;
	}

	public void setDesCaja(String desCaja) {
		this.desCaja = desCaja;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
    
	
 } 