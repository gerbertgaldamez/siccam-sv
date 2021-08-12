/**
 * ReversaDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dto.pagoonlinecommon.gte.tmmas.com;

public class ReversaDTO  implements java.io.Serializable {
    private java.lang.String cajero;

    private java.lang.String codBanco;

    private java.lang.String fecha;

    private java.lang.String hora;

    private double montoTotalOperacion;

    private java.lang.String numTelefonoCliente;

    private int numTransaccion;

    private java.lang.String tipoOperacion;

    public ReversaDTO() {
    }

    public ReversaDTO(
           java.lang.String cajero,
           java.lang.String codBanco,
           java.lang.String fecha,
           java.lang.String hora,
           double montoTotalOperacion,
           java.lang.String numTelefonoCliente,
           int numTransaccion,
           java.lang.String tipoOperacion) {
           this.cajero = cajero;
           this.codBanco = codBanco;
           this.fecha = fecha;
           this.hora = hora;
           this.montoTotalOperacion = montoTotalOperacion;
           this.numTelefonoCliente = numTelefonoCliente;
           this.numTransaccion = numTransaccion;
           this.tipoOperacion = tipoOperacion;
    }


    /**
     * Gets the cajero value for this ReversaDTO.
     * 
     * @return cajero
     */
    public java.lang.String getCajero() {
        return cajero;
    }


    /**
     * Sets the cajero value for this ReversaDTO.
     * 
     * @param cajero
     */
    public void setCajero(java.lang.String cajero) {
        this.cajero = cajero;
    }


    /**
     * Gets the codBanco value for this ReversaDTO.
     * 
     * @return codBanco
     */
    public java.lang.String getCodBanco() {
        return codBanco;
    }


    /**
     * Sets the codBanco value for this ReversaDTO.
     * 
     * @param codBanco
     */
    public void setCodBanco(java.lang.String codBanco) {
        this.codBanco = codBanco;
    }


    /**
     * Gets the fecha value for this ReversaDTO.
     * 
     * @return fecha
     */
    public java.lang.String getFecha() {
        return fecha;
    }


    /**
     * Sets the fecha value for this ReversaDTO.
     * 
     * @param fecha
     */
    public void setFecha(java.lang.String fecha) {
        this.fecha = fecha;
    }


    /**
     * Gets the hora value for this ReversaDTO.
     * 
     * @return hora
     */
    public java.lang.String getHora() {
        return hora;
    }


    /**
     * Sets the hora value for this ReversaDTO.
     * 
     * @param hora
     */
    public void setHora(java.lang.String hora) {
        this.hora = hora;
    }


    /**
     * Gets the montoTotalOperacion value for this ReversaDTO.
     * 
     * @return montoTotalOperacion
     */
    public double getMontoTotalOperacion() {
        return montoTotalOperacion;
    }


    /**
     * Sets the montoTotalOperacion value for this ReversaDTO.
     * 
     * @param montoTotalOperacion
     */
    public void setMontoTotalOperacion(double montoTotalOperacion) {
        this.montoTotalOperacion = montoTotalOperacion;
    }


    /**
     * Gets the numTelefonoCliente value for this ReversaDTO.
     * 
     * @return numTelefonoCliente
     */
    public java.lang.String getNumTelefonoCliente() {
        return numTelefonoCliente;
    }


    /**
     * Sets the numTelefonoCliente value for this ReversaDTO.
     * 
     * @param numTelefonoCliente
     */
    public void setNumTelefonoCliente(java.lang.String numTelefonoCliente) {
        this.numTelefonoCliente = numTelefonoCliente;
    }


    /**
     * Gets the numTransaccion value for this ReversaDTO.
     * 
     * @return numTransaccion
     */
    public int getNumTransaccion() {
        return numTransaccion;
    }


    /**
     * Sets the numTransaccion value for this ReversaDTO.
     * 
     * @param numTransaccion
     */
    public void setNumTransaccion(int numTransaccion) {
        this.numTransaccion = numTransaccion;
    }


    /**
     * Gets the tipoOperacion value for this ReversaDTO.
     * 
     * @return tipoOperacion
     */
    public java.lang.String getTipoOperacion() {
        return tipoOperacion;
    }


    /**
     * Sets the tipoOperacion value for this ReversaDTO.
     * 
     * @param tipoOperacion
     */
    public void setTipoOperacion(java.lang.String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReversaDTO)) return false;
        ReversaDTO other = (ReversaDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
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
            this.montoTotalOperacion == other.getMontoTotalOperacion() &&
            ((this.numTelefonoCliente==null && other.getNumTelefonoCliente()==null) || 
             (this.numTelefonoCliente!=null &&
              this.numTelefonoCliente.equals(other.getNumTelefonoCliente()))) &&
            this.numTransaccion == other.getNumTransaccion() &&
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
        _hashCode += new Double(getMontoTotalOperacion()).hashCode();
        if (getNumTelefonoCliente() != null) {
            _hashCode += getNumTelefonoCliente().hashCode();
        }
        _hashCode += getNumTransaccion();
        if (getTipoOperacion() != null) {
            _hashCode += getTipoOperacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReversaDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "ReversaDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("montoTotalOperacion");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "montoTotalOperacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTelefonoCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numTelefonoCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTransaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numTransaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
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
