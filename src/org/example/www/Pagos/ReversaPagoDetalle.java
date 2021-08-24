/**
 * ReversaPagoDetalle.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class ReversaPagoDetalle  implements java.io.Serializable {
    private java.lang.String cod_transaccion;

    private java.lang.String num_referencia;

    private java.lang.String status;

    private java.lang.String msg_response;

    public ReversaPagoDetalle() {
    }

    public ReversaPagoDetalle(
           java.lang.String cod_transaccion,
           java.lang.String num_referencia,
           java.lang.String status,
           java.lang.String msg_response) {
           this.cod_transaccion = cod_transaccion;
           this.num_referencia = num_referencia;
           this.status = status;
           this.msg_response = msg_response;
    }


    /**
     * Gets the cod_transaccion value for this ReversaPagoDetalle.
     * 
     * @return cod_transaccion
     */
    public java.lang.String getCod_transaccion() {
        return cod_transaccion;
    }


    /**
     * Sets the cod_transaccion value for this ReversaPagoDetalle.
     * 
     * @param cod_transaccion
     */
    public void setCod_transaccion(java.lang.String cod_transaccion) {
        this.cod_transaccion = cod_transaccion;
    }


    /**
     * Gets the num_referencia value for this ReversaPagoDetalle.
     * 
     * @return num_referencia
     */
    public java.lang.String getNum_referencia() {
        return num_referencia;
    }


    /**
     * Sets the num_referencia value for this ReversaPagoDetalle.
     * 
     * @param num_referencia
     */
    public void setNum_referencia(java.lang.String num_referencia) {
        this.num_referencia = num_referencia;
    }


    /**
     * Gets the status value for this ReversaPagoDetalle.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ReversaPagoDetalle.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the msg_response value for this ReversaPagoDetalle.
     * 
     * @return msg_response
     */
    public java.lang.String getMsg_response() {
        return msg_response;
    }


    /**
     * Sets the msg_response value for this ReversaPagoDetalle.
     * 
     * @param msg_response
     */
    public void setMsg_response(java.lang.String msg_response) {
        this.msg_response = msg_response;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReversaPagoDetalle)) return false;
        ReversaPagoDetalle other = (ReversaPagoDetalle) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cod_transaccion==null && other.getCod_transaccion()==null) || 
             (this.cod_transaccion!=null &&
              this.cod_transaccion.equals(other.getCod_transaccion()))) &&
            ((this.num_referencia==null && other.getNum_referencia()==null) || 
             (this.num_referencia!=null &&
              this.num_referencia.equals(other.getNum_referencia()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.msg_response==null && other.getMsg_response()==null) || 
             (this.msg_response!=null &&
              this.msg_response.equals(other.getMsg_response())));
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
        if (getCod_transaccion() != null) {
            _hashCode += getCod_transaccion().hashCode();
        }
        if (getNum_referencia() != null) {
            _hashCode += getNum_referencia().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getMsg_response() != null) {
            _hashCode += getMsg_response().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReversaPagoDetalle.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ReversaPagoDetalle"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cod_transaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "cod_transaccion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_referencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "num_referencia"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msg_response");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "msg_response"));
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
