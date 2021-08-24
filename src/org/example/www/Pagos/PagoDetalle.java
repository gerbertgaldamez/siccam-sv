/**
 * PagoDetalle.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class PagoDetalle  implements java.io.Serializable {
    private java.lang.String num_transaccion;

    private java.lang.String num_referencia;

    private double balance;

    private java.lang.String primerNombre;

    private java.lang.String segundoNombre;

    private java.lang.String status;

    private java.lang.String msg_response;

    public PagoDetalle() {
    }

    public PagoDetalle(
           java.lang.String num_transaccion,
           java.lang.String num_referencia,
           double balance,
           java.lang.String primerNombre,
           java.lang.String segundoNombre,
           java.lang.String status,
           java.lang.String msg_response) {
           this.num_transaccion = num_transaccion;
           this.num_referencia = num_referencia;
           this.balance = balance;
           this.primerNombre = primerNombre;
           this.segundoNombre = segundoNombre;
           this.status = status;
           this.msg_response = msg_response;
    }


    /**
     * Gets the num_transaccion value for this PagoDetalle.
     * 
     * @return num_transaccion
     */
    public java.lang.String getNum_transaccion() {
        return num_transaccion;
    }


    /**
     * Sets the num_transaccion value for this PagoDetalle.
     * 
     * @param num_transaccion
     */
    public void setNum_transaccion(java.lang.String num_transaccion) {
        this.num_transaccion = num_transaccion;
    }


    /**
     * Gets the num_referencia value for this PagoDetalle.
     * 
     * @return num_referencia
     */
    public java.lang.String getNum_referencia() {
        return num_referencia;
    }


    /**
     * Sets the num_referencia value for this PagoDetalle.
     * 
     * @param num_referencia
     */
    public void setNum_referencia(java.lang.String num_referencia) {
        this.num_referencia = num_referencia;
    }


    /**
     * Gets the balance value for this PagoDetalle.
     * 
     * @return balance
     */
    public double getBalance() {
        return balance;
    }


    /**
     * Sets the balance value for this PagoDetalle.
     * 
     * @param balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }


    /**
     * Gets the primerNombre value for this PagoDetalle.
     * 
     * @return primerNombre
     */
    public java.lang.String getPrimerNombre() {
        return primerNombre;
    }


    /**
     * Sets the primerNombre value for this PagoDetalle.
     * 
     * @param primerNombre
     */
    public void setPrimerNombre(java.lang.String primerNombre) {
        this.primerNombre = primerNombre;
    }


    /**
     * Gets the segundoNombre value for this PagoDetalle.
     * 
     * @return segundoNombre
     */
    public java.lang.String getSegundoNombre() {
        return segundoNombre;
    }


    /**
     * Sets the segundoNombre value for this PagoDetalle.
     * 
     * @param segundoNombre
     */
    public void setSegundoNombre(java.lang.String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }


    /**
     * Gets the status value for this PagoDetalle.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this PagoDetalle.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the msg_response value for this PagoDetalle.
     * 
     * @return msg_response
     */
    public java.lang.String getMsg_response() {
        return msg_response;
    }


    /**
     * Sets the msg_response value for this PagoDetalle.
     * 
     * @param msg_response
     */
    public void setMsg_response(java.lang.String msg_response) {
        this.msg_response = msg_response;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PagoDetalle)) return false;
        PagoDetalle other = (PagoDetalle) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.num_transaccion==null && other.getNum_transaccion()==null) || 
             (this.num_transaccion!=null &&
              this.num_transaccion.equals(other.getNum_transaccion()))) &&
            ((this.num_referencia==null && other.getNum_referencia()==null) || 
             (this.num_referencia!=null &&
              this.num_referencia.equals(other.getNum_referencia()))) &&
            this.balance == other.getBalance() &&
            ((this.primerNombre==null && other.getPrimerNombre()==null) || 
             (this.primerNombre!=null &&
              this.primerNombre.equals(other.getPrimerNombre()))) &&
            ((this.segundoNombre==null && other.getSegundoNombre()==null) || 
             (this.segundoNombre!=null &&
              this.segundoNombre.equals(other.getSegundoNombre()))) &&
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
        if (getNum_transaccion() != null) {
            _hashCode += getNum_transaccion().hashCode();
        }
        if (getNum_referencia() != null) {
            _hashCode += getNum_referencia().hashCode();
        }
        _hashCode += new Double(getBalance()).hashCode();
        if (getPrimerNombre() != null) {
            _hashCode += getPrimerNombre().hashCode();
        }
        if (getSegundoNombre() != null) {
            _hashCode += getSegundoNombre().hashCode();
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
        new org.apache.axis.description.TypeDesc(PagoDetalle.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "PagoDetalle"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_transaccion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "num_transaccion"));
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
        elemField.setFieldName("balance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "balance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("primerNombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "primerNombre"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("segundoNombre");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "segundoNombre"));
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
