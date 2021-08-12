/**
 * PagoOnLineWSPortStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package _1._0._0._127;

public class PagoOnLineWSPortStub extends org.apache.axis.client.Stub implements _1._0._0._127.PagoOnLineWSPort {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[3];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("aplicarPagoOnLine");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "pagoDTO"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "PagoDTO"), dto.pagoonlinecommon.gte.tmmas.com.PagoDTO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "RespuestaPagoDTO"));
        oper.setReturnClass(dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://127.0.0.1:7001", "PagoOnLineException"),
                      "exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException",
                      new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.exception", "PagoOnLineException"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("reversarPagoOnLine");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "reversaDTO"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "ReversaDTO"), dto.pagoonlinecommon.gte.tmmas.com.ReversaDTO.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "RespuestaReversaDTO"));
        oper.setReturnClass(dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://127.0.0.1:7001", "PagoOnLineException"),
                      "exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException",
                      new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.exception", "PagoOnLineException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("foo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "string"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "result"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

    }

    public PagoOnLineWSPortStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PagoOnLineWSPortStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PagoOnLineWSPortStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("java:com.tmmas.cl.framework.exception", "GeneralException");
            cachedSerQNames.add(qName);
            cls = exception.framework.cl.tmmas.com.GeneralException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "PagoDTO");
            cachedSerQNames.add(qName);
            cls = dto.pagoonlinecommon.gte.tmmas.com.PagoDTO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "RespuestaDTO");
            cachedSerQNames.add(qName);
            cls = dto.pagoonlinecommon.gte.tmmas.com.RespuestaDTO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "RespuestaPagoDTO");
            cachedSerQNames.add(qName);
            cls = dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "RespuestaReversaDTO");
            cachedSerQNames.add(qName);
            cls = dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.dto", "ReversaDTO");
            cachedSerQNames.add(qName);
            cls = dto.pagoonlinecommon.gte.tmmas.com.ReversaDTO.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("java:com.tmmas.gte.pagoonlinecommon.exception", "PagoOnLineException");
            cachedSerQNames.add(qName);
            cls = exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("java:language_builtins.lang", "ArrayOfObject");
            cachedSerQNames.add(qName);
            cls = java.lang.Object[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO aplicarPagoOnLine(dto.pagoonlinecommon.gte.tmmas.com.PagoDTO pagoDTO) throws java.rmi.RemoteException, exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://127.0.0.1:7001", "aplicarPagoOnLine"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {pagoDTO});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO) _resp;
            } catch (java.lang.Exception _exception) {
                return (dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO) org.apache.axis.utils.JavaUtils.convert(_resp, dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException) {
              throw (exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO reversarPagoOnLine(dto.pagoonlinecommon.gte.tmmas.com.ReversaDTO reversaDTO) throws java.rmi.RemoteException, exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://127.0.0.1:7001", "reversarPagoOnLine"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {reversaDTO});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO) _resp;
            } catch (java.lang.Exception _exception) {
                return (dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO) org.apache.axis.utils.JavaUtils.convert(_resp, dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException) {
              throw (exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public java.lang.String foo(java.lang.String string) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://127.0.0.1:7001", "foo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {string});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
