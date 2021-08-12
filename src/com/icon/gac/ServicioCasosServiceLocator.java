/**
 * ServicioCasosServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.icon.gac;

public class ServicioCasosServiceLocator extends org.apache.axis.client.Service implements com.icon.gac.ServicioCasosService {

    public ServicioCasosServiceLocator() {
    }


    public ServicioCasosServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ServicioCasosServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ServicioCasos
    private java.lang.String ServicioCasos_address = "http://10.225.162.107:9080/gac/services/ServicioCasos";

    public java.lang.String getServicioCasosAddress() {
        return ServicioCasos_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ServicioCasosWSDDServiceName = "ServicioCasos";

    public java.lang.String getServicioCasosWSDDServiceName() {
        return ServicioCasosWSDDServiceName;
    }

    public void setServicioCasosWSDDServiceName(java.lang.String name) {
        ServicioCasosWSDDServiceName = name;
    }

    public com.icon.gac.ServicioCasos getServicioCasos() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ServicioCasos_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getServicioCasos(endpoint);
    }

    public com.icon.gac.ServicioCasos getServicioCasos(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.icon.gac.ServicioCasosSoapBindingStub _stub = new com.icon.gac.ServicioCasosSoapBindingStub(portAddress, this);
            _stub.setPortName(getServicioCasosWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setServicioCasosEndpointAddress(java.lang.String address) {
        ServicioCasos_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.icon.gac.ServicioCasos.class.isAssignableFrom(serviceEndpointInterface)) {
                com.icon.gac.ServicioCasosSoapBindingStub _stub = new com.icon.gac.ServicioCasosSoapBindingStub(new java.net.URL(ServicioCasos_address), this);
                _stub.setPortName(getServicioCasosWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("ServicioCasos".equals(inputPortName)) {
            return getServicioCasos();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gac.icon.com", "ServicioCasosService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gac.icon.com", "ServicioCasos"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ServicioCasos".equals(portName)) {
            setServicioCasosEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
