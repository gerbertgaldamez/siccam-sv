/**
 * PagoOnLineWSLocator.java

 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package _1._0._0._127;

import java.util.List;

import com.terium.siccam.dao.CBParametrosGeneralesDAO;
import com.terium.siccam.model.CBParametrosGeneralesModel;
import com.terium.siccam.utils.Constantes;
import com.terium.siccam.utils.Tools;

public class PagoOnLineWSLocator extends org.apache.axis.client.Service implements _1._0._0._127.PagoOnLineWS {

	public PagoOnLineWSLocator() {
    }


    public PagoOnLineWSLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PagoOnLineWSLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PagoOnLineWSPort
    private java.lang.String PagoOnLineWSPort_address = getUrlPagos();

    public java.lang.String getPagoOnLineWSPortAddress() {
        return PagoOnLineWSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PagoOnLineWSPortWSDDServiceName = "PagoOnLineWSPort";

    public java.lang.String getPagoOnLineWSPortWSDDServiceName() {
        return PagoOnLineWSPortWSDDServiceName;
    }

    public void setPagoOnLineWSPortWSDDServiceName(java.lang.String name) {
        PagoOnLineWSPortWSDDServiceName = name;
    }
    
    public String getUrlPagos() {
    	List<CBParametrosGeneralesModel> parametros = CBParametrosGeneralesDAO.obtenerParametrosWS();
    	
    	return Tools.obtenerParametro(Constantes.URLWSPAGOS, parametros);
    }

    public _1._0._0._127.PagoOnLineWSPort getPagoOnLineWSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PagoOnLineWSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPagoOnLineWSPort(endpoint);
    }

    public _1._0._0._127.PagoOnLineWSPort getPagoOnLineWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            _1._0._0._127.PagoOnLineWSPortStub _stub = new _1._0._0._127.PagoOnLineWSPortStub(portAddress, this);
            _stub.setPortName(getPagoOnLineWSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPagoOnLineWSPortEndpointAddress(java.lang.String address) {
        PagoOnLineWSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (_1._0._0._127.PagoOnLineWSPort.class.isAssignableFrom(serviceEndpointInterface)) {
                _1._0._0._127.PagoOnLineWSPortStub _stub = new _1._0._0._127.PagoOnLineWSPortStub(new java.net.URL(PagoOnLineWSPort_address), this);
                _stub.setPortName(getPagoOnLineWSPortWSDDServiceName());
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
        if ("PagoOnLineWSPort".equals(inputPortName)) {
            return getPagoOnLineWSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://127.0.0.1:7001", "PagoOnLineWS");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://127.0.0.1:7001", "PagoOnLineWSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PagoOnLineWSPort".equals(portName)) {
            setPagoOnLineWSPortEndpointAddress(address);
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
