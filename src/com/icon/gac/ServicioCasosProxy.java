package com.icon.gac;

public class ServicioCasosProxy implements com.icon.gac.ServicioCasos {
  private String _endpoint = null;
  private com.icon.gac.ServicioCasos servicioCasos = null;
  
  public ServicioCasosProxy() {
    _initServicioCasosProxy();
  }
  
  public ServicioCasosProxy(String endpoint) {
    _endpoint = endpoint;
    _initServicioCasosProxy();
  }
  
  private void _initServicioCasosProxy() {
    try {
      servicioCasos = (new com.icon.gac.ServicioCasosServiceLocator()).getServicioCasos();
      if (servicioCasos != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)servicioCasos)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)servicioCasos)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (servicioCasos != null)
      ((javax.xml.rpc.Stub)servicioCasos)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.icon.gac.ServicioCasos getServicioCasos() {
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos;
  }
  
  public java.lang.String asignarCaso(java.lang.String casoId, java.lang.String usuario) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.asignarCaso(casoId, usuario);
  }
  
  public java.lang.String adjuntarArchivo(java.lang.String usuario, java.lang.String casoId, java.lang.String comentario, java.lang.String tipoArchivo, javax.activation.DataHandler contenidoArchivo, java.lang.String nombreArchivo) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.adjuntarArchivo(usuario, casoId, comentario, tipoArchivo, contenidoArchivo, nombreArchivo);
  }
  
  public java.lang.String obtenerTipoCierre() throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.obtenerTipoCierre();
  }
  
  public java.lang.String crearCaso(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.crearCaso(tipologiaid, usuario, comentarioEjecutivo, numCelular, unidadId, descripcion);
  }
  
  public java.lang.String reabrirCaso(java.lang.String casoId, java.lang.String usuario) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.reabrirCaso(casoId, usuario);
  }
  
  public java.lang.String cerrarCaso(java.lang.String casoId, java.lang.String usuario, java.lang.String comentario, java.lang.String solucion, java.lang.String clasificacion, java.lang.String monto, java.lang.String notaCredito, java.lang.String tipoCierre, java.lang.String origenId, java.lang.String enviarSMS, java.lang.String mensajeSMS) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.cerrarCaso(casoId, usuario, comentario, solucion, clasificacion, monto, notaCredito, tipoCierre, origenId, enviarSMS, mensajeSMS);
  }
  
  public java.lang.String crearCasoCerrado(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion, java.lang.String solucion, java.lang.String tipoCierre, java.lang.String clasificacionTipologia, java.lang.String notaCredito, java.lang.String monto, java.lang.String origenId) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.crearCasoCerrado(tipologiaid, usuario, comentarioEjecutivo, numCelular, unidadId, descripcion, solucion, tipoCierre, clasificacionTipologia, notaCredito, monto, origenId);
  }
  
  public java.lang.String devolverCaso(java.lang.String casoId, java.lang.String usuario, java.lang.String comentario) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.devolverCaso(casoId, usuario, comentario);
  }
  
  public java.lang.String escalarCaso(java.lang.String casoId, java.lang.String usuario, java.lang.String buzon, java.lang.String comentario) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.escalarCaso(casoId, usuario, buzon, comentario);
  }
  
  public java.lang.String liberarCaso(java.lang.String casoId, java.lang.String usuario) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.liberarCaso(casoId, usuario);
  }
  
  public java.lang.String crearProspecto(java.lang.String usuario, java.lang.String nombre, java.lang.String telefono, java.lang.String correoElectronico, java.lang.String comentarioProspecto, java.lang.String comentarioEjecutivo) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.crearProspecto(usuario, nombre, telefono, correoElectronico, comentarioProspecto, comentarioEjecutivo);
  }
  
  public java.lang.String crearCasoProspecto(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.crearCasoProspecto(tipologiaid, usuario, comentarioEjecutivo, numCelular, unidadId, descripcion);
  }
  
  public java.lang.String crearCasoEscalado(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion, java.lang.String buzonId) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.crearCasoEscalado(tipologiaid, usuario, comentarioEjecutivo, numCelular, unidadId, descripcion, buzonId);
  }
  
  public java.lang.String actualizaUnidadUsuario(java.lang.String usuarioActualizar, long unidadId, java.lang.String usuarioModifica) throws java.rmi.RemoteException{
    if (servicioCasos == null)
      _initServicioCasosProxy();
    return servicioCasos.actualizaUnidadUsuario(usuarioActualizar, unidadId, usuarioModifica);
  }
  
  
}