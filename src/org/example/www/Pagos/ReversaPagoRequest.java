/**
 * ReversaPagoRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class ReversaPagoRequest  implements java.io.Serializable {
    private int telefono;

    private java.lang.String fecha_pago;

    private double monto;

    private java.lang.String referencia;

    private java.lang.String bank_id;

    public ReversaPagoRequest() {
    }

    public ReversaPagoRequest(
           int telefono,
           java.lang.String fecha_pago,
           double monto,
           java.lang.String referencia,
           java.lang.String bank_id) {
           this.telefono = telefono;
           this.fecha_pago = fecha_pago;
           this.monto = monto;
           this.referencia = referencia;
           this.bank_id = bank_id;
    }


    /**
     * Gets the telefono value for this ReversaPagoRequest.
     * 
     * @return telefono
     */
    public int getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this ReversaPagoRequest.
     * 
     * @param telefono
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the fecha_pago value for this ReversaPagoRequest.
     * 
     * @return fecha_pago
     */
    public java.lang.String getFecha_pago() {
        return fecha_pago;
    }


    /**
     * Sets the fecha_pago value for this ReversaPagoRequest.
     * 
     * @param fecha_pago
     */
    public void setFecha_pago(java.lang.String fecha_pago) {
        this.fecha_pago = fecha_pago;
    }


    /**
     * Gets the monto value for this ReversaPagoRequest.
     * 
     * @return monto
     */
    public double getMonto() {
        return monto;
    }


    /**
     * Sets the monto value for this ReversaPagoRequest.
     * 
     * @param monto
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }


    /**
     * Gets the referencia value for this ReversaPagoRequest.
     * 
     * @return referencia
     */
    public java.lang.String getReferencia() {
        return referencia;
    }


    /**
     * Sets the referencia value for this ReversaPagoRequest.
     * 
     * @param referencia
     */
    public void setReferencia(java.lang.String referencia) {
        this.referencia = referencia;
    }


    /**
     * Gets the bank_id value for this ReversaPagoRequest.
     * 
     * @return bank_id
     */
    public java.lang.String getBank_id() {
        return bank_id;
    }


    /**
     * Sets the bank_id value for this ReversaPagoRequest.
     * 
     * @param bank_id
     */
    public void setBank_id(java.lang.String bank_id) {
        this.bank_id = bank_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReversaPagoRequest)) return false;
        ReversaPagoRequest other = (ReversaPagoRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.telefono == other.getTelefono() &&
            ((this.fecha_pago==null && other.getFecha_pago()==null) || 
             (this.fecha_pago!=null &&
              this.fecha_pago.equals(other.getFecha_pago()))) &&
            this.monto == other.getMonto() &&
            ((this.referencia==null && other.getReferencia()==null) || 
             (this.referencia!=null &&
              this.referencia.equals(other.getReferencia()))) &&
            ((this.bank_id==null && other.getBank_id()==null) || 
             (this.bank_id!=null &&
              this.bank_id.equals(other.getBank_id())));
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
        _hashCode += getTelefono();
        if (getFecha_pago() != null) {
            _hashCode += getFecha_pago().hashCode();
        }
        _hashCode += new Double(getMonto()).hashCode();
        if (getReferencia() != null) {
            _hashCode += getReferencia().hashCode();
        }
        if (getBank_id() != null) {
            _hashCode += getBank_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReversaPagoRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fecha_pago");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "fecha_pago"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "monto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "referencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bank_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "bank_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
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
