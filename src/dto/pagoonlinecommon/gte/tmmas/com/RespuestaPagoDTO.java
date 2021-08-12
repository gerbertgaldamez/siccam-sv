/**
 * RespuestaPagoDTO.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package dto.pagoonlinecommon.gte.tmmas.com;

public class RespuestaPagoDTO  implements java.io.Serializable {
    private java.lang.String apellidosCliente;

    private double mtoSaldo;

    private java.lang.String nombreCliente;

    private long numTelefonoCliente;

    private int numTransaccion;

    private dto.pagoonlinecommon.gte.tmmas.com.RespuestaDTO respuesta;

    private java.lang.String status;

    private java.lang.String tipoOperacion;

    public RespuestaPagoDTO() {
    }

    public RespuestaPagoDTO(
           java.lang.String apellidosCliente,
           double mtoSaldo,
           java.lang.String nombreCliente,
           long numTelefonoCliente,
           int numTransaccion,
           dto.pagoonlinecommon.gte.tmmas.com.RespuestaDTO respuesta,
           java.lang.String status,
           java.lang.String tipoOperacion) {
           this.apellidosCliente = apellidosCliente;
           this.mtoSaldo = mtoSaldo;
           this.nombreCliente = nombreCliente;
           this.numTelefonoCliente = numTelefonoCliente;
           this.numTransaccion = numTransaccion;
           this.respuesta = respuesta;
           this.status = status;
           this.tipoOperacion = tipoOperacion;
    }


    /**
     * Gets the apellidosCliente value for this RespuestaPagoDTO.
     * 
     * @return apellidosCliente
     */
    public java.lang.String getApellidosCliente() {
        return apellidosCliente;
    }


    /**
     * Sets the apellidosCliente value for this RespuestaPagoDTO.
     * 
     * @param apellidosCliente
     */
    public void setApellidosCliente(java.lang.String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
    }


    /**
     * Gets the mtoSaldo value for this RespuestaPagoDTO.
     * 
     * @return mtoSaldo
     */
    public double getMtoSaldo() {
        return mtoSaldo;
    }


    /**
     * Sets the mtoSaldo value for this RespuestaPagoDTO.
     * 
     * @param mtoSaldo
     */
    public void setMtoSaldo(double mtoSaldo) {
        this.mtoSaldo = mtoSaldo;
    }


    /**
     * Gets the nombreCliente value for this RespuestaPagoDTO.
     * 
     * @return nombreCliente
     */
    public java.lang.String getNombreCliente() {
        return nombreCliente;
    }


    /**
     * Sets the nombreCliente value for this RespuestaPagoDTO.
     * 
     * @param nombreCliente
     */
    public void setNombreCliente(java.lang.String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }


    /**
     * Gets the numTelefonoCliente value for this RespuestaPagoDTO.
     * 
     * @return numTelefonoCliente
     */
    public long getNumTelefonoCliente() {
        return numTelefonoCliente;
    }


    /**
     * Sets the numTelefonoCliente value for this RespuestaPagoDTO.
     * 
     * @param numTelefonoCliente
     */
    public void setNumTelefonoCliente(long numTelefonoCliente) {
        this.numTelefonoCliente = numTelefonoCliente;
    }


    /**
     * Gets the numTransaccion value for this RespuestaPagoDTO.
     * 
     * @return numTransaccion
     */
    public int getNumTransaccion() {
        return numTransaccion;
    }


    /**
     * Sets the numTransaccion value for this RespuestaPagoDTO.
     * 
     * @param numTransaccion
     */
    public void setNumTransaccion(int numTransaccion) {
        this.numTransaccion = numTransaccion;
    }


    /**
     * Gets the respuesta value for this RespuestaPagoDTO.
     * 
     * @return respuesta
     */
    public dto.pagoonlinecommon.gte.tmmas.com.RespuestaDTO getRespuesta() {
        return respuesta;
    }


    /**
     * Sets the respuesta value for this RespuestaPagoDTO.
     * 
     * @param respuesta
     */
    public void setRespuesta(dto.pagoonlinecommon.gte.tmmas.com.RespuestaDTO respuesta) {
        this.respuesta = respuesta;
    }


    /**
     * Gets the status value for this RespuestaPagoDTO.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this RespuestaPagoDTO.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the tipoOperacion value for this RespuestaPagoDTO.
     * 
     * @return tipoOperacion
     */
    public java.lang.String getTipoOperacion() {
        return tipoOperacion;
    }


    /**
     * Sets the tipoOperacion value for this RespuestaPagoDTO.
     * 
     * @param tipoOperacion
     */
    public void setTipoOperacion(java.lang.String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RespuestaPagoDTO)) return false;
        RespuestaPagoDTO other = (RespuestaPagoDTO) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.apellidosCliente==null && other.getApellidosCliente()==null) || 
             (this.apellidosCliente!=null &&
              this.apellidosCliente.equals(other.getApellidosCliente()))) &&
            this.mtoSaldo == other.getMtoSaldo() &&
            ((this.nombreCliente==null && other.getNombreCliente()==null) || 
             (this.nombreCliente!=null &&
              this.nombreCliente.equals(other.getNombreCliente()))) &&
            this.numTelefonoCliente == other.getNumTelefonoCliente() &&
            this.numTransaccion == other.getNumTransaccion() &&
            ((this.respuesta==null && other.getRespuesta()==null) || 
             (this.respuesta!=null &&
              this.respuesta.equals(other.getRespuesta()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
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
        if (getApellidosCliente() != null) {
            _hashCode += getApellidosCliente().hashCode();
        }
        _hashCode += new Double(getMtoSaldo()).hashCode();
        if (getNombreCliente() != null) {
            _hashCode += getNombreCliente().hashCode();
        }
        _hashCode += new Long(getNumTelefonoCliente()).hashCode();
        _hashCode += getNumTransaccion();
        if (getRespuesta() != null) {
            _hashCode += getRespuesta().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getTipoOperacion() != null) {
            _hashCode += getTipoOperacion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RespuestaPagoDTO.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "RespuestaPagoDTO"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("apellidosCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "apellidosCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mtoSaldo");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "mtoSaldo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nombreCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "nombreCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTelefonoCliente");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numTelefonoCliente"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numTransaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "numTransaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("respuesta");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "respuesta"));
        elemField.setXmlType(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "RespuestaDTO"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "status"));
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
