/**
 * SaldoDetalle.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class SaldoDetalle  implements java.io.Serializable {
    private java.lang.String referencia;

    private double balance;

    private java.lang.String primerNombre;

    private java.lang.String segundoNombre;

    private java.lang.String status;

    private java.lang.String msg_response;

    public SaldoDetalle() {
    }

    public SaldoDetalle(
           java.lang.String referencia,
           double balance,
           java.lang.String primerNombre,
           java.lang.String segundoNombre,
           java.lang.String status,
           java.lang.String msg_response) {
           this.referencia = referencia;
           this.balance = balance;
           this.primerNombre = primerNombre;
           this.segundoNombre = segundoNombre;
           this.status = status;
           this.msg_response = msg_response;
    }


    /**
     * Gets the referencia value for this SaldoDetalle.
     * 
     * @return referencia
     */
    public java.lang.String getReferencia() {
        return referencia;
    }


    /**
     * Sets the referencia value for this SaldoDetalle.
     * 
     * @param referencia
     */
    public void setReferencia(java.lang.String referencia) {
        this.referencia = referencia;
    }


    /**
     * Gets the balance value for this SaldoDetalle.
     * 
     * @return balance
     */
    public double getBalance() {
        return balance;
    }


    /**
     * Sets the balance value for this SaldoDetalle.
     * 
     * @param balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }


    /**
     * Gets the primerNombre value for this SaldoDetalle.
     * 
     * @return primerNombre
     */
    public java.lang.String getPrimerNombre() {
        return primerNombre;
    }


    /**
     * Sets the primerNombre value for this SaldoDetalle.
     * 
     * @param primerNombre
     */
    public void setPrimerNombre(java.lang.String primerNombre) {
        this.primerNombre = primerNombre;
    }


    /**
     * Gets the segundoNombre value for this SaldoDetalle.
     * 
     * @return segundoNombre
     */
    public java.lang.String getSegundoNombre() {
        return segundoNombre;
    }


    /**
     * Sets the segundoNombre value for this SaldoDetalle.
     * 
     * @param segundoNombre
     */
    public void setSegundoNombre(java.lang.String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }


    /**
     * Gets the status value for this SaldoDetalle.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this SaldoDetalle.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the msg_response value for this SaldoDetalle.
     * 
     * @return msg_response
     */
    public java.lang.String getMsg_response() {
        return msg_response;
    }


    /**
     * Sets the msg_response value for this SaldoDetalle.
     * 
     * @param msg_response
     */
    public void setMsg_response(java.lang.String msg_response) {
        this.msg_response = msg_response;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SaldoDetalle)) return false;
        SaldoDetalle other = (SaldoDetalle) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.referencia==null && other.getReferencia()==null) || 
             (this.referencia!=null &&
              this.referencia.equals(other.getReferencia()))) &&
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
        if (getReferencia() != null) {
            _hashCode += getReferencia().hashCode();
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
        new org.apache.axis.description.TypeDesc(SaldoDetalle.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "SaldoDetalle"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("referencia");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "referencia"));
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
