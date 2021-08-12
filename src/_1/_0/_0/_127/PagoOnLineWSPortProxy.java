package _1._0._0._127;

public class PagoOnLineWSPortProxy implements _1._0._0._127.PagoOnLineWSPort {
  private String _endpoint = null;
  private _1._0._0._127.PagoOnLineWSPort pagoOnLineWSPort = null;
  
  public PagoOnLineWSPortProxy() {
    _initPagoOnLineWSPortProxy();
  }
  
  public PagoOnLineWSPortProxy(String endpoint) {
    _endpoint = endpoint;
    _initPagoOnLineWSPortProxy();
  }
  
  private void _initPagoOnLineWSPortProxy() {
    try {
      pagoOnLineWSPort = (new _1._0._0._127.PagoOnLineWSLocator()).getPagoOnLineWSPort();
      if (pagoOnLineWSPort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pagoOnLineWSPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pagoOnLineWSPort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pagoOnLineWSPort != null)
      ((javax.xml.rpc.Stub)pagoOnLineWSPort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public _1._0._0._127.PagoOnLineWSPort getPagoOnLineWSPort() {
    if (pagoOnLineWSPort == null)
      _initPagoOnLineWSPortProxy();
    return pagoOnLineWSPort;
  }
  
  public dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO aplicarPagoOnLine(dto.pagoonlinecommon.gte.tmmas.com.PagoDTO pagoDTO) throws java.rmi.RemoteException, exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException{
    if (pagoOnLineWSPort == null)
      _initPagoOnLineWSPortProxy();
    return pagoOnLineWSPort.aplicarPagoOnLine(pagoDTO);
  }
  
  public dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO reversarPagoOnLine(dto.pagoonlinecommon.gte.tmmas.com.ReversaDTO reversaDTO) throws java.rmi.RemoteException, exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException{
    if (pagoOnLineWSPort == null)
      _initPagoOnLineWSPortProxy();
    return pagoOnLineWSPort.reversarPagoOnLine(reversaDTO);
  }
  
  public java.lang.String foo(java.lang.String string) throws java.rmi.RemoteException{
    if (pagoOnLineWSPort == null)
      _initPagoOnLineWSPortProxy();
    return pagoOnLineWSPort.foo(string);
  }
  
  
}