package org.example.www.Pagos;

public class PagosPortProxy implements org.example.www.Pagos.PagosPort {
  private String _endpoint = null;
  private org.example.www.Pagos.PagosPort pagosPort = null;
  
  public PagosPortProxy() {
    _initPagosPortProxy();
  }
  
  public PagosPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initPagosPortProxy();
  }
  
  private void _initPagosPortProxy() {
    try {
      pagosPort = (new org.example.www.Pagos.PagosPortServiceLocator()).getpagosPortSoap11();
      if (pagosPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pagosPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pagosPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pagosPort != null)
      ((javax.xml.rpc.Stub)pagosPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.example.www.Pagos.PagosPort getPagosPort() {
    if (pagosPort == null)
      _initPagosPortProxy();
    return pagosPort;
  }
  
  public org.example.www.Pagos.SaldoDetalle[] consultaSaldo(org.example.www.Pagos.ConsultaSaldoRequest consultaSaldoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.ConsultaSaldoFault{
    if (pagosPort == null)
      _initPagosPortProxy();
    return pagosPort.consultaSaldo(consultaSaldoRequest);
  }
  
  public org.example.www.Pagos.PagoDetalle[] ejecutarPago(org.example.www.Pagos.EjecutarPagoRequest ejecutarPagoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.EjecutarPagoFault{
    if (pagosPort == null)
      _initPagosPortProxy();
    return pagosPort.ejecutarPago(ejecutarPagoRequest);
  }
  
  public org.example.www.Pagos.ReversaPagoDetalle[] reversaPago(org.example.www.Pagos.ReversaPagoRequest reversaPagoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.ReversaPagoFault{
    if (pagosPort == null)
      _initPagosPortProxy();
    return pagosPort.reversaPago(reversaPagoRequest);
  }
  
  
}