/**
 * EjecutarPagoRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class EjecutarPagoRequest  implements java.io.Serializable {
    private int telefono;

    private java.lang.String fecha_pago;

    private java.lang.String bank_id;

    private java.lang.String bill_ref_no;

    private org.example.www.Pagos.EjecutarPagoDetalle[] ejecutarPagoDetalle;

    public EjecutarPagoRequest() {
    }

    public EjecutarPagoRequest(
           int telefono,
           java.lang.String fecha_pago,
           java.lang.String bank_id,
           java.lang.String bill_ref_no,
           org.example.www.Pagos.EjecutarPagoDetalle[] ejecutarPagoDetalle) {
           this.telefono = telefono;
           this.fecha_pago = fecha_pago;
           this.bank_id = bank_id;
           this.bill_ref_no = bill_ref_no;
           this.ejecutarPagoDetalle = ejecutarPagoDetalle;
    }


    /**
     * Gets the telefono value for this EjecutarPagoRequest.
     * 
     * @return telefono
     */
    public int getTelefono() {
        return telefono;
    }


    /**
     * Sets the telefono value for this EjecutarPagoRequest.
     * 
     * @param telefono
     */
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }


    /**
     * Gets the fecha_pago value for this EjecutarPagoRequest.
     * 
     * @return fecha_pago
     */
    public java.lang.String getFecha_pago() {
        return fecha_pago;
    }


    /**
     * Sets the fecha_pago value for this EjecutarPagoRequest.
     * 
     * @param fecha_pago
     */
    public void setFecha_pago(java.lang.String fecha_pago) {
        this.fecha_pago = fecha_pago;
    }


    /**
     * Gets the bank_id value for this EjecutarPagoRequest.
     * 
     * @return bank_id
     */
    public java.lang.String getBank_id() {
        return bank_id;
    }


    /**
     * Sets the bank_id value for this EjecutarPagoRequest.
     * 
     * @param bank_id
     */
    public void setBank_id(java.lang.String bank_id) {
        this.bank_id = bank_id;
    }


    /**
     * Gets the bill_ref_no value for this EjecutarPagoRequest.
     * 
     * @return bill_ref_no
     */
    public java.lang.String getBill_ref_no() {
        return bill_ref_no;
    }


    /**
     * Sets the bill_ref_no value for this EjecutarPagoRequest.
     * 
     * @param bill_ref_no
     */
    public void setBill_ref_no(java.lang.String bill_ref_no) {
        this.bill_ref_no = bill_ref_no;
    }


    /**
     * Gets the ejecutarPagoDetalle value for this EjecutarPagoRequest.
     * 
     * @return ejecutarPagoDetalle
     */
    public org.example.www.Pagos.EjecutarPagoDetalle[] getEjecutarPagoDetalle() {
        return ejecutarPagoDetalle;
    }


    /**
     * Sets the ejecutarPagoDetalle value for this EjecutarPagoRequest.
     * 
     * @param ejecutarPagoDetalle
     */
    public void setEjecutarPagoDetalle(org.example.www.Pagos.EjecutarPagoDetalle[] ejecutarPagoDetalle) {
        this.ejecutarPagoDetalle = ejecutarPagoDetalle;
    }

    public org.example.www.Pagos.EjecutarPagoDetalle getEjecutarPagoDetalle(int i) {
        return this.ejecutarPagoDetalle[i];
    }

    public void setEjecutarPagoDetalle(int i, org.example.www.Pagos.EjecutarPagoDetalle _value) {
        this.ejecutarPagoDetalle[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EjecutarPagoRequest)) return false;
        EjecutarPagoRequest other = (EjecutarPagoRequest) obj;
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
            ((this.bank_id==null && other.getBank_id()==null) || 
             (this.bank_id!=null &&
              this.bank_id.equals(other.getBank_id()))) &&
            ((this.bill_ref_no==null && other.getBill_ref_no()==null) || 
             (this.bill_ref_no!=null &&
              this.bill_ref_no.equals(other.getBill_ref_no()))) &&
            ((this.ejecutarPagoDetalle==null && other.getEjecutarPagoDetalle()==null) || 
             (this.ejecutarPagoDetalle!=null &&
              java.util.Arrays.equals(this.ejecutarPagoDetalle, other.getEjecutarPagoDetalle())));
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
        if (getBank_id() != null) {
            _hashCode += getBank_id().hashCode();
        }
        if (getBill_ref_no() != null) {
            _hashCode += getBill_ref_no().hashCode();
        }
        if (getEjecutarPagoDetalle() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getEjecutarPagoDetalle());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getEjecutarPagoDetalle(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EjecutarPagoRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoRequest"));
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
        elemField.setFieldName("bank_id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "bank_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bill_ref_no");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "bill_ref_no"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ejecutarPagoDetalle");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoDetalle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoDetalle"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
