/**
 * PagosPortSoap11Stub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class PagosPortSoap11Stub extends org.apache.axis.client.Stub implements org.example.www.Pagos.PagosPort {
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
        oper.setName("consultaSaldo");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoRequest"), org.example.www.Pagos.ConsultaSaldoRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoResponse"));
        oper.setReturnClass(org.example.www.Pagos.SaldoDetalle[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoResponse"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "SaldoDetalle"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoFault"),
                      "org.example.www.Pagos.ConsultaSaldoFault",
                      new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoFault"), 
                      true
                     ));
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("ejecutarPago");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoRequest"), org.example.www.Pagos.EjecutarPagoRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoResponse"));
        oper.setReturnClass(org.example.www.Pagos.PagoDetalle[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoResponse"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "PagoDetalle"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoFault"),
                      "org.example.www.Pagos.EjecutarPagoFault",
                      new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoFault"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("reversaPago");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoRequest"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoRequest"), org.example.www.Pagos.ReversaPagoRequest.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoResponse"));
        oper.setReturnClass(org.example.www.Pagos.ReversaPagoDetalle[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoResponse"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("http://www.example.org/Pagos", "ReversaPagoDetalle"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoFault"),
                      "org.example.www.Pagos.ReversaPagoFault",
                      new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoFault"), 
                      true
                     ));
        _operations[2] = oper;

    }

    public PagosPortSoap11Stub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public PagosPortSoap11Stub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public PagosPortSoap11Stub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
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
            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoFault");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.ConsultaSaldoFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoRequest");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.ConsultaSaldoRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "consultaSaldoResponse");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.SaldoDetalle[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "SaldoDetalle");
            qName2 = new javax.xml.namespace.QName("http://www.example.org/Pagos", "SaldoDetalle");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoDetalle");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.EjecutarPagoDetalle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoFault");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.EjecutarPagoFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoRequest");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.EjecutarPagoRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "ejecutarPagoResponse");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.PagoDetalle[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "PagoDetalle");
            qName2 = new javax.xml.namespace.QName("http://www.example.org/Pagos", "PagoDetalle");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "PagoDetalle");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.PagoDetalle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "ReversaPagoDetalle");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.ReversaPagoDetalle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoFault");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.ReversaPagoFault.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoRequest");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.ReversaPagoRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "reversaPagoResponse");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.ReversaPagoDetalle[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "ReversaPagoDetalle");
            qName2 = new javax.xml.namespace.QName("http://www.example.org/Pagos", "ReversaPagoDetalle");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.example.org/Pagos", "SaldoDetalle");
            cachedSerQNames.add(qName);
            cls = org.example.www.Pagos.SaldoDetalle.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

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
                    _call.setEncodingStyle(null);
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

    public org.example.www.Pagos.SaldoDetalle[] consultaSaldo(org.example.www.Pagos.ConsultaSaldoRequest consultaSaldoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.ConsultaSaldoFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "consultaSaldo"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {consultaSaldoRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.example.www.Pagos.SaldoDetalle[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.example.www.Pagos.SaldoDetalle[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.example.www.Pagos.SaldoDetalle[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.example.www.Pagos.ConsultaSaldoFault) {
              throw (org.example.www.Pagos.ConsultaSaldoFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public org.example.www.Pagos.PagoDetalle[] ejecutarPago(org.example.www.Pagos.EjecutarPagoRequest ejecutarPagoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.EjecutarPagoFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "ejecutarPago"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {ejecutarPagoRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.example.www.Pagos.PagoDetalle[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.example.www.Pagos.PagoDetalle[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.example.www.Pagos.PagoDetalle[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.example.www.Pagos.EjecutarPagoFault) {
              throw (org.example.www.Pagos.EjecutarPagoFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public org.example.www.Pagos.ReversaPagoDetalle[] reversaPago(org.example.www.Pagos.ReversaPagoRequest reversaPagoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.ReversaPagoFault {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "reversaPago"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {reversaPagoRequest});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.example.www.Pagos.ReversaPagoDetalle[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.example.www.Pagos.ReversaPagoDetalle[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.example.www.Pagos.ReversaPagoDetalle[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof org.example.www.Pagos.ReversaPagoFault) {
              throw (org.example.www.Pagos.ReversaPagoFault) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

}
