/**
 * PagosPortServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public class PagosPortServiceLocator extends org.apache.axis.client.Service implements org.example.www.Pagos.PagosPortService {

    public PagosPortServiceLocator() {
    }


    public PagosPortServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PagosPortServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for pagosPortSoap11
    private java.lang.String pagosPortSoap11_address = "http://10.231.128.139:7003/wspagos/";

    public java.lang.String getpagosPortSoap11Address() {
        return pagosPortSoap11_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String pagosPortSoap11WSDDServiceName = "pagosPortSoap11";

    public java.lang.String getpagosPortSoap11WSDDServiceName() {
        return pagosPortSoap11WSDDServiceName;
    }

    public void setpagosPortSoap11WSDDServiceName(java.lang.String name) {
        pagosPortSoap11WSDDServiceName = name;
    }

    public org.example.www.Pagos.PagosPort getpagosPortSoap11() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(pagosPortSoap11_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getpagosPortSoap11(endpoint);
    }

    public org.example.www.Pagos.PagosPort getpagosPortSoap11(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.example.www.Pagos.PagosPortSoap11Stub _stub = new org.example.www.Pagos.PagosPortSoap11Stub(portAddress, this);
            _stub.setPortName(getpagosPortSoap11WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setpagosPortSoap11EndpointAddress(java.lang.String address) {
        pagosPortSoap11_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.example.www.Pagos.PagosPort.class.isAssignableFrom(serviceEndpointInterface)) {
                org.example.www.Pagos.PagosPortSoap11Stub _stub = new org.example.www.Pagos.PagosPortSoap11Stub(new java.net.URL(pagosPortSoap11_address), this);
                _stub.setPortName(getpagosPortSoap11WSDDServiceName());
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
        if ("pagosPortSoap11".equals(inputPortName)) {
            return getpagosPortSoap11();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.example.org/Pagos", "pagosPortService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.example.org/Pagos", "pagosPortSoap11"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("pagosPortSoap11".equals(portName)) {
            setpagosPortSoap11EndpointAddress(address);
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
