/**
 * ConsultaSaldoRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class ConsultaSaldoRequest  implements java.io.Serializable {
    private int telefono;

    private java.lang.String bank_id;

    public ConsultaSaldoRequest() {
    }

    public ConsultaSaldoRequest(
           int telefono,
           java.lang.String bank_id) {
           this.telefono = telefono;
           this.bank_id = bank_id;
    }


    /**
     * Gets the telefono value for this ConsultaSaldoRequest.
     * 
     * @return telefono
     */
    public int getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this ConsultaSaldoRequest.
     * 
     * @param telefono
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the bank_id value for this ConsultaSaldoRequest.
     * 
     * @return bank_id
     */
    public java.lang.String getBank_id() {
        return bank_id;
    }


    /**
     * Sets the bank_id value for this ConsultaSaldoRequest.
     * 
     * @param bank_id
     */
    public void setBank_id(java.lang.String bank_id) {
        this.bank_id = bank_id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConsultaSaldoRequest)) return false;
        ConsultaSaldoRequest other = (ConsultaSaldoRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.telefono == other.getTelefono() &&
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
        if (getBank_id() != null) {
            _hashCode += getBank_id().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ConsultaSaldoRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("telefono");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "telefono"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
