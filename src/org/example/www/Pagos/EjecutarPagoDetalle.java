/**
 * EjecutarPagoDetalle.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class EjecutarPagoDetalle  implements java.io.Serializable {
    private int tipo;

    private double monto;

    private java.lang.String num_tarjeta;

    private java.lang.String num_cheque;

    private java.lang.String autorizacion;

    private java.lang.String banco;

    public EjecutarPagoDetalle() {
    }

    public EjecutarPagoDetalle(
           int tipo,
           double monto,
           java.lang.String num_tarjeta,
           java.lang.String num_cheque,
           java.lang.String autorizacion,
           java.lang.String banco) {
           this.tipo = tipo;
           this.monto = monto;
           this.num_tarjeta = num_tarjeta;
           this.num_cheque = num_cheque;
           this.autorizacion = autorizacion;
           this.banco = banco;
    }


    /**
     * Gets the tipo value for this EjecutarPagoDetalle.
     * 
     * @return tipo
     */
    public int getTipo() {
        return tipo;
    }


    /**
     * Sets the tipo value for this EjecutarPagoDetalle.
     * 
     * @param tipo
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }


    /**
     * Gets the monto value for this EjecutarPagoDetalle.
     * 
     * @return monto
     */
    public double getMonto() {
        return monto;
    }


    /**
     * Sets the monto value for this EjecutarPagoDetalle.
     * 
     * @param monto
     */
    public void setMonto(double monto) {
        this.monto = monto;
    }


    /**
     * Gets the num_tarjeta value for this EjecutarPagoDetalle.
     * 
     * @return num_tarjeta
     */
    public java.lang.String getNum_tarjeta() {
        return num_tarjeta;
    }


    /**
     * Sets the num_tarjeta value for this EjecutarPagoDetalle.
     * 
     * @param num_tarjeta
     */
    public void setNum_tarjeta(java.lang.String num_tarjeta) {
        this.num_tarjeta = num_tarjeta;
    }


    /**
     * Gets the num_cheque value for this EjecutarPagoDetalle.
     * 
     * @return num_cheque
     */
    public java.lang.String getNum_cheque() {
        return num_cheque;
    }


    /**
     * Sets the num_cheque value for this EjecutarPagoDetalle.
     * 
     * @param num_cheque
     */
    public void setNum_cheque(java.lang.String num_cheque) {
        this.num_cheque = num_cheque;
    }


    /**
     * Gets the autorizacion value for this EjecutarPagoDetalle.
     * 
     * @return autorizacion
     */
    public java.lang.String getAutorizacion() {
        return autorizacion;
    }


    /**
     * Sets the autorizacion value for this EjecutarPagoDetalle.
     * 
     * @param autorizacion
     */
    public void setAutorizacion(java.lang.String autorizacion) {
        this.autorizacion = autorizacion;
    }


    /**
     * Gets the banco value for this EjecutarPagoDetalle.
     * 
     * @return banco
     */
    public java.lang.String getBanco() {
        return banco;
    }


    /**
     * Sets the banco value for this EjecutarPagoDetalle.
     * 
     * @param banco
     */
    public void setBanco(java.lang.String banco) {
        this.banco = banco;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EjecutarPagoDetalle)) return false;
        EjecutarPagoDetalle other = (EjecutarPagoDetalle) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.tipo == other.getTipo() &&
            this.monto == other.getMonto() &&
            ((this.num_tarjeta==null && other.getNum_tarjeta()==null) || 
             (this.num_tarjeta!=null &&
              this.num_tarjeta.equals(other.getNum_tarjeta()))) &&
            ((this.num_cheque==null && other.getNum_cheque()==null) || 
             (this.num_cheque!=null &&
              this.num_cheque.equals(other.getNum_cheque()))) &&
            ((this.autorizacion==null && other.getAutorizacion()==null) || 
             (this.autorizacion!=null &&
              this.autorizacion.equals(other.getAutorizacion()))) &&
            ((this.banco==null && other.getBanco()==null) || 
             (this.banco!=null &&
              this.banco.equals(other.getBanco())));
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
        _hashCode += getTipo();
        _hashCode += new Double(getMonto()).hashCode();
        if (getNum_tarjeta() != null) {
            _hashCode += getNum_tarjeta().hashCode();
        }
        if (getNum_cheque() != null) {
            _hashCode += getNum_cheque().hashCode();
        }
        if (getAutorizacion() != null) {
            _hashCode += getAutorizacion().hashCode();
        }
        if (getBanco() != null) {
            _hashCode += getBanco().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EjecutarPagoDetalle.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoDetalle"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tipo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "tipo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monto");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "monto"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_tarjeta");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "num_tarjeta"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("num_cheque");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "num_cheque"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("autorizacion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "autorizacion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("banco");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "banco"));
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
