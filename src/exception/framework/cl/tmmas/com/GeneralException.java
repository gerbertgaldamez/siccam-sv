/**
 * GeneralException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package exception.framework.cl.tmmas.com;

public class GeneralException  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private java.lang.String codigo;

    private long codigoEvento;

    private java.lang.String descripcionEvento;

    private java.lang.String message1;

    private java.lang.String messageUser;

    private java.lang.String trace;

    private java.lang.Object[] traces;

    public GeneralException() {
    }

    public GeneralException(
           java.lang.String codigo,
           long codigoEvento,
           java.lang.String descripcionEvento,
           java.lang.String message1,
           java.lang.String messageUser,
           java.lang.String trace,
           java.lang.Object[] traces) {
        this.codigo = codigo;
        this.codigoEvento = codigoEvento;
        this.descripcionEvento = descripcionEvento;
        this.message1 = message1;
        this.messageUser = messageUser;
        this.trace = trace;
        this.traces = traces;
    }


    /**
     * Gets the codigo value for this GeneralException.
     * 
     * @return codigo
     */
    public java.lang.String getCodigo() {
        return codigo;
    }


    /**
     * Sets the codigo value for this GeneralException.
     * 
     * @param codigo
     */
    public void setCodigo(java.lang.String codigo) {
        this.codigo = codigo;
    }


    /**
     * Gets the codigoEvento value for this GeneralException.
     * 
     * @return codigoEvento
     */
    public long getCodigoEvento() {
        return codigoEvento;
    }


    /**
     * Sets the codigoEvento value for this GeneralException.
     * 
     * @param codigoEvento
     */
    public void setCodigoEvento(long codigoEvento) {
        this.codigoEvento = codigoEvento;
    }


    /**
     * Gets the descripcionEvento value for this GeneralException.
     * 
     * @return descripcionEvento
     */
    public java.lang.String getDescripcionEvento() {
        return descripcionEvento;
    }


    /**
     * Sets the descripcionEvento value for this GeneralException.
     * 
     * @param descripcionEvento
     */
    public void setDescripcionEvento(java.lang.String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }


    /**
     * Gets the message1 value for this GeneralException.
     * 
     * @return message1
     */
    public java.lang.String getMessage1() {
        return message1;
    }


    /**
     * Sets the message1 value for this GeneralException.
     * 
     * @param message1
     */
    public void setMessage1(java.lang.String message1) {
        this.message1 = message1;
    }


    /**
     * Gets the messageUser value for this GeneralException.
     * 
     * @return messageUser
     */
    public java.lang.String getMessageUser() {
        return messageUser;
    }


    /**
     * Sets the messageUser value for this GeneralException.
     * 
     * @param messageUser
     */
    public void setMessageUser(java.lang.String messageUser) {
        this.messageUser = messageUser;
    }


    /**
     * Gets the trace value for this GeneralException.
     * 
     * @return trace
     */
    public java.lang.String getTrace() {
        return trace;
    }


    /**
     * Sets the trace value for this GeneralException.
     * 
     * @param trace
     */
    public void setTrace(java.lang.String trace) {
        this.trace = trace;
    }


    /**
     * Gets the traces value for this GeneralException.
     * 
     * @return traces
     */
    public java.lang.Object[] getTraces() {
        return traces;
    }


    /**
     * Sets the traces value for this GeneralException.
     * 
     * @param traces
     */
    public void setTraces(java.lang.Object[] traces) {
        this.traces = traces;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GeneralException)) return false;
        GeneralException other = (GeneralException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.codigo==null && other.getCodigo()==null) || 
             (this.codigo!=null &&
              this.codigo.equals(other.getCodigo()))) &&
            this.codigoEvento == other.getCodigoEvento() &&
            ((this.descripcionEvento==null && other.getDescripcionEvento()==null) || 
             (this.descripcionEvento!=null &&
              this.descripcionEvento.equals(other.getDescripcionEvento()))) &&
            ((this.message1==null && other.getMessage1()==null) || 
             (this.message1!=null &&
              this.message1.equals(other.getMessage1()))) &&
            ((this.messageUser==null && other.getMessageUser()==null) || 
             (this.messageUser!=null &&
              this.messageUser.equals(other.getMessageUser()))) &&
            ((this.trace==null && other.getTrace()==null) || 
             (this.trace!=null &&
              this.trace.equals(other.getTrace()))) &&
            ((this.traces==null && other.getTraces()==null) || 
             (this.traces!=null &&
              java.util.Arrays.equals(this.traces, other.getTraces())));
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
        if (getCodigo() != null) {
            _hashCode += getCodigo().hashCode();
        }
        _hashCode += new Long(getCodigoEvento()).hashCode();
        if (getDescripcionEvento() != null) {
            _hashCode += getDescripcionEvento().hashCode();
        }
        if (getMessage1() != null) {
            _hashCode += getMessage1().hashCode();
        }
        if (getMessageUser() != null) {
            _hashCode += getMessageUser().hashCode();
        }
        if (getTrace() != null) {
            _hashCode += getTrace().hashCode();
        }
        if (getTraces() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTraces());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTraces(), i);
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
        new org.apache.axis.description.TypeDesc(GeneralException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "GeneralException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigo");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "codigo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codigoEvento");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "codigoEvento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("descripcionEvento");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "descripcionEvento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message1");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageUser");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "messageUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trace");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "trace"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("traces");
        elemField.setXmlName(new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "traces"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
