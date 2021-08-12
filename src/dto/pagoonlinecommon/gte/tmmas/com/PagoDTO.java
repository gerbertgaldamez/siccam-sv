/**
 * PagoDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dto.pagoonlinecommon.gte.tmmas.com;

public class PagoDTO  implements java.io.Serializable {
    private java.lang.String agencia;

    private java.lang.String bancoTarjetaDebito;

    private java.lang.String cajero;

    private java.lang.String codBanco;

    private java.lang.String fecha;

    private java.lang.String hora;

    private double montoChequeBanco;

    private double montoChequeOtroBanco;

    private double montoEfectivo;

    private double montoTarjetaCredito;

    private double montoTarjetaDebito;

    private double montoTotalOperacion;

    private java.lang.String numAutorizacion;

    private int numBoleta;

    private int numCheque;

    private int numFactura;

    private java.lang.String numTarjeta;

    private java.lang.String numTelefonoCliente;

    private long telefono;

    private java.lang.String tipTarjeta;

    private java.lang.String tipoOperacion;

    public PagoDTO() {
    }

    public PagoDTO(
           java.lang.String agencia,
           java.lang.String bancoTarjetaDebito,
           java.lang.String cajero,
           java.lang.String codBanco,
           java.lang.String fecha,
           java.lang.String hora,
           double montoChequeBanco,
           double montoChequeOtroBanco,
           double montoEfectivo,
           double montoTarjetaCredito,
           double montoTarjetaDebito,
           double montoTotalOperacion,
           java.lang.String numAutorizacion,
           int numBoleta,
           int numCheque,
           int numFactura,
           java.lang.String numTarjeta,
           java.lang.String numTelefonoCliente,
           long telefono,
           java.lang.String tipTarjeta,
           java.lang.String tipoOperacion) {
           this.agencia = agencia;
           this.bancoTarjetaDebito = bancoTarjetaDebito;
           this.cajero = cajero;
           this.codBanco = codBanco;
           this.fecha = fecha;
           this.hora = hora;
           this.montoChequeBanco = montoChequeBanco;
           this.montoChequeOtroBanco = montoChequeOtroBanco;
           this.montoEfectivo = montoEfectivo;
           this.montoTarjetaCredito = montoTarjetaCredito;
           this.montoTarjetaDebito = montoTarjetaDebito;
           this.montoTotalOperacion = montoTotalOperacion;
           this.numAutorizacion = numAutorizacion;
           this.numBoleta = numBoleta;
           this.numCheque = numCheque;
           this.numFactura = numFactura;
           this.numTarjeta = numTarjeta;
           this.numTelefonoCliente = numTelefonoCliente;
           this.telefono = telefono;
           this.tipTarjeta = tipTarjeta;
           this.tipoOperacion = tipoOperacion;
    }


    /**
     * Gets the agencia value for this PagoDTO.
     * 
     * @return agencia
     */
    public java.lang.String getAgencia() {
        return agencia;
    }


    /**
     * Sets the agencia value for this PagoDTO.
     * 
     * @param agencia
     */
    public void setAgencia(java.lang.String agencia) {
        this.agencia = agencia;
    }


    /**
     * Gets the bancoTarjetaDebito value for this PagoDTO.
     * 
     * @return bancoTarjetaDebito
     */
    public java.lang.String getBancoTarjetaDebito() {
        return bancoTarjetaDebito;
    }


    /**
     * Sets the bancoTarjetaDebito value for this PagoDTO.
     * 
     * @param bancoTarjetaDebito
     */
    public void setBancoTarjetaDebito(java.lang.String bancoTarjetaDebito) {
        this.bancoTarjetaDebito = bancoTarjetaDebito;
    }


    /**
     * Gets the cajero value for this PagoDTO.
     * 
     * @return cajero
     */
    public java.lang.String getCajero() {
        return cajero;
    }


    /**
     * Sets the cajero value for this PagoDTO.
     * 
     * @param cajero
     */
    public void setCajero(java.lang.String cajero) {
        this.cajero = cajero;
    }


    /**
     * Gets the codBanco value for this PagoDTO.
     * 
     * @return codBanco
     */
    public java.lang.String getCodBanco() {
        return codBanco;
    }


    /**
     * Sets the codBanco value for this PagoDTO.
     * 
     * @param codBanco
     */
    public void setCodBanco(java.lang.String codBanco) {
        this.codBanco = codBanco;
    }


    /**
     * Gets the fecha value for this PagoDTO.
     * 
     * @return fecha
     */
    public java.lang.String getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this PagoDTO.
     * 
     * @param fecha
     */
    public void setFecha(java.lang.String fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the hora value for this PagoDTO.
     * 
     * @return hora
     */
    public java.lang.String getHora() {
        return hora;
    }


    /**
     * Sets the hora value for this PagoDTO.
     * 
     * @param hora
     */
    public void setHora(java.lang.String hora) {
        this.hora = hora;
    }


    /**
     * Gets the montoChequeBanco value for this PagoDTO.
     * 
     * @return montoChequeBanco
     */
    public double getMontoChequeBanco() {
        return montoChequeBanco;
    }


    /**
     * Sets the montoChequeBanco value for this PagoDTO.
     * 
     * @param montoChequeBanco
     */
    public void setMontoChequeBanco(double montoChequeBanco) {
        this.montoChequeBanco = montoChequeBanco;
    }


    /**
     * Gets the montoChequeOtroBanco value for this PagoDTO.
     * 
     * @return montoChequeOtroBanco
     */
    public double getMontoChequeOtroBanco() {
        return montoChequeOtroBanco;
    }


    /**
     * Sets the montoChequeOtroBanco value for this PagoDTO.
     * 
     * @param montoChequeOtroBanco
     */
    public void setMontoChequeOtroBanco(double montoChequeOtroBanco) {
        this.montoChequeOtroBanco = montoChequeOtroBanco;
    }


    /**
     * Gets the montoEfectivo value for this PagoDTO.
     * 
     * @return montoEfectivo
     */
    public double getMontoEfectivo() {
        return montoEfectivo;
    }


    /**
     * Sets the montoEfectivo value for this PagoDTO.
     * 
     * @param montoEfectivo
     */
    public void setMontoEfectivo(double montoEfectivo) {
        this.montoEfectivo = montoEfectivo;
    }


    /**
     * Gets the montoTarjetaCredito value for this PagoDTO.
     * 
     * @return montoTarjetaCredito
     */
    public double getMontoTarjetaCredito() {
        return montoTarjetaCredito;
    }


    /**
     * Sets the montoTarjetaCredito value for this PagoDTO.
     * 
     * @param montoTarjetaCredito
     */
    public void setMontoTarjetaCredito(double montoTarjetaCredito) {
        this.montoTarjetaCredito = montoTarjetaCredito;
    }


    /**
     * Gets the montoTarjetaDebito value for this PagoDTO.
     * 
     * @return montoTarjetaDebito
     */
    public double getMontoTarjetaDebito() {
        return montoTarjetaDebito;
    }


    /**
     * Sets the montoTarjetaDebito value for this PagoDTO.
     * 
     * @param montoTarjetaDebito
     */
    public void setMontoTarjetaDebito(double montoTarjetaDebito) {
        this.montoTarjetaDebito = montoTarjetaDebito;
    }


    /**
     * Gets the montoTotalOperacion value for this PagoDTO.
     * 
     * @return montoTotalOperacion
     */
    public double getMontoTotalOperacion() {
        return montoTotalOperacion;
    }


    /**
     * Sets the montoTotalOperacion value for this PagoDTO.
     * 
     * @param montoTotalOperacion
     */
    public void setMontoTotalOperacion(double montoTotalOperacion) {
        this.montoTotalOperacion = montoTotalOperacion;
    }


    /**
     * Gets the numAutorizacion value for this PagoDTO.
     * 
     * @return numAutorizacion
     */
    public java.lang.String getNumAutorizacion() {
        return numAutorizacion;
    }


    /**
     * Sets the numAutorizacion value for this PagoDTO.
     * 
     * @param numAutorizacion
     */
    public void setNumAutorizacion(java.lang.String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }


    /**
     * Gets the numBoleta value for this PagoDTO.
     * 
     * @return numBoleta
     */
    public int getNumBoleta() {
        return numBoleta;
    }


    /**
     * Sets the numBoleta value for this PagoDTO.
     * 
     * @param numBoleta
     */
    public void setNumBoleta(int numBoleta) {
        this.numBoleta = numBoleta;
    }


    /**
     * Gets the numCheque value for this PagoDTO.
     * 
     * @return numCheque
     */
    public int getNumCheque() {
        return numCheque;
    }


    /**
     * Sets the numCheque value for this PagoDTO.
     * 
     * @param numCheque
     */
    public void setNumCheque(int numCheque) {
        this.numCheque = numCheque;
    }


    /**
     * Gets the numFactura value for this PagoDTO.
     * 
     * @return numFactura
     */
    public int getNumFactura() {
        return numFactura;
    }


    /**
     * Sets the numFactura value for this PagoDTO.
     * 
     * @param numFactura
     */
    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }


    /**
     * Gets the numTarjeta value for this PagoDTO.
     * 
     * @return numTarjeta
     */
    public java.lang.String getNumTarjeta() {
        return numTarjeta;
    }


    /**
     * Sets the numTarjeta value for this PagoDTO.
     * 
     * @param numTarjeta
     */
    public void setNumTarjeta(java.lang.String numTarjeta) {
        this.numTarjeta = numTarjeta;
    }


    /**
     * Gets the numTelefonoCliente value for this PagoDTO.
     * 
     * @return numTelefonoCliente
     */
    public java.lang.String getNumTelefonoCliente() {
        return numTelefonoCliente;
    }


    /**
     * Sets the numTelefonoCliente value for this PagoDTO.
     * 
     * @param numTelefonoCliente
     */
    public void setNumTelefonoCliente(java.lang.String numTelefonoCliente) {
        this.numTelefonoCliente = numTelefonoCliente;
    }


    /**
     * Gets the telefono value for this PagoDTO.
     * 
     * @return telefono
     */
    public long getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this PagoDTO.
     * 
     * @param telefono
     */
    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the tipTarjeta value for this PagoDTO.
     * 
     * @return tipTarjeta
     */
    public java.lang.String getTipTarjeta() {
        return tipTarjeta;
    }


    /**
     * Sets the tipTarjeta value for this PagoDTO.
     * 
     * @param tipTarjeta
     */
    public void setTipTarjeta(java.lang.String tipTarjeta) {
        this.tipTarjeta = tipTarjeta;
    }


    /**
     * Gets the tipoOperacion value for this PagoDTO.
     * 
     * @return tipoOperacion
     */
    public java.lang.String getTipoOperacion() {
        return tipoOperacion;
    }


    /**
     * Sets the tipoOperacion value for this PagoDTO.
     * 
     * @param tipoOperacion
     */
    public void setTipoOperacion(java.lang.String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PagoDTO)) return false;
        PagoDTO other = (PagoDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.agencia==null && other.getAgencia()==null) || 
             (this.agencia!=null &&
              this.agencia.equals(other.getAgencia()))) &&
            ((this.bancoTarjetaDebito==null && other.getBancoTarjetaDebito()==null) || 
             (this.bancoTarjetaDebito!=null &&
              this.bancoTarjetaDebito.equals(other.getBancoTarjetaDebito()))) &&
            ((this.cajero==null && other.getCajero()==null) || 
             (this.cajero!=null &&
              this.cajero.equals(other.getCajero()))) &&
            ((this.codBanco==null && other.getCodBanco()==null) || 
             (this.codBanco!=null &&
              this.codBanco.equals(other.getCodBanco()))) &&
            ((this.fecha==null && other.getFecha()==null) || 
             (this.fecha!=null &&
              this.fecha.equals(other.getFecha()))) &&
            ((this.hora==null && other.getHora()==null) || 
             (this.hora!=null &&
              this.hora.equals(other.getHora()))) &&
            this.montoChequeBanco == other.getMontoChequeBanco() &&
            this.montoChequeOtroBanco == other.getMontoChequeOtroBanco() &&
            this.montoEfectivo == other.getMontoEfectivo() &&
            this.montoTarjetaCredito == other.getMontoTarjetaCredito() &&
            this.montoTarjetaDebito == other.getMontoTarjetaDebito() &&
            this.montoTotalOperacion == other.getMontoTotalOperacion() &&
            ((this.numAutorizacion==null && other.getNumAutorizacion()==null) || 
             (this.numAutorizacion!=null &&
              this.numAutorizacion.equals(other.getNumAutorizacion()))) &&
            this.numBoleta == other.getNumBoleta() &&
            this.numCheque == other.getNumCheque() &&
            this.numFactura == other.getNumFactura() &&
            ((this.numTarjeta==null && other.getNumTarjeta()==null) || 
             (this.numTarjeta!=null &&
              this.numTarjeta.equals(other.getNumTarjeta()))) &&
            ((this.numTelefonoCliente==null && other.getNumTelefonoCliente()==null) || 
             (this.numTelefonoCliente!=null &&
              this.numTelefonoCliente.equals(other.getNumTelefonoCliente()))) &&
            this.telefono == other.getTelefono() &&
            ((this.tipTarjeta==null && other.getTipTarjeta()==null) || 
             (this.tipTarjeta!=null &&
              this.tipTarjeta.equals(other.getTipTarjeta()))) &&
            ((this.tipoOperacion==null && other.getTipoOperacion()==null) || 
             (this.tipoOperacion!=null &&
              this.tipoOperacion.equals(other.getTipoOperacion())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAgencia() != null) {
            _hashCode += getAgencia().hashCode();
        }
        if (getBancoTarjetaDebito() != null) {
            _hashCode += getBancoTarjetaDebito().hashCode();
        }
        if (getCajero() != null) {
            _hashCode += getCajero().hashCode();
        }
        if (getCodBanco() != null) {
            _hashCode += getCodBanco().hashCode();
        }
        if (getFecha() != null) {
            _hashCode += getFecha().hashCode();
        }
        if (getHora() != null) {
            _hashCode += getHora().hashCode();
        }
        _hashCode += new Double(getMontoChequeBanco()).hashCode();
        _hashCode += new Double(getMontoChequeOtroBanco()).hashCode();
        _hashCode += new Double(getMontoEfectivo()).hashCode();
        _hashCode += new Double(getMontoTarjetaCredito()).hashCode();
        _hashCode += new Double(getMontoTarjetaDebito()).hashCode();
        _hashCode += new Double(getMontoTotalOperacion()).hashCode();
        if (getNumAutorizacion() != null) {
            _hashCode += getNumAutorizacion().hashCode();
        }
        _hashCode += getNumBoleta();
        _hashCode += getNumCheque();
        _hashCode += getNumFactura();
        if (getNumTarjeta() != null) {
            _hashCode += getNumTarjeta().hashCode();
        }
        if (getNumTelefonoCliente() != null) {
            _hashCode += getNumTelefonoCliente().hashCode();
        }
        _hashCode += new Long(getTelefono()).hashCode();
        if (getTipTarjeta() != null) {
            _hashCode += getTipTarjeta().hashCode();
        }
        if (getTipoOperacion() != null) {
            _hashCode += getTipoOperacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PagoDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "PagoDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("agencia");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "agencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bancoTarjetaDebito");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "bancoTarjetaDebito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cajero");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "cajero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codBanco");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "codBanco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "fecha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hora");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "hora"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("montoChequeBanco");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "montoChequeBanco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("montoChequeOtroBanco");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "montoChequeOtroBanco"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("montoEfectivo");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "montoEfectivo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("montoTarjetaCredito");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "montoTarjetaCredito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("montoTarjetaDebito");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "montoTarjetaDebito"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("montoTotalOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "montoTotalOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numAutorizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numAutorizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numBoleta");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numBoleta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numCheque");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numCheque"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numFactura");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numFactura"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTarjeta");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numTarjeta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTelefonoCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numTelefonoCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipTarjeta");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "tipTarjeta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipoOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "tipoOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
