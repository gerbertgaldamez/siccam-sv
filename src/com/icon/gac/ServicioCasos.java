/**
 * ServicioCasos.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.icon.gac;

public interface ServicioCasos extends java.rmi.Remote {
    public java.lang.String asignarCaso(java.lang.String casoId, java.lang.String usuario) throws java.rmi.RemoteException;
    public java.lang.String adjuntarArchivo(java.lang.String usuario, java.lang.String casoId, java.lang.String comentario, java.lang.String tipoArchivo, javax.activation.DataHandler contenidoArchivo, java.lang.String nombreArchivo) throws java.rmi.RemoteException;
    public java.lang.String obtenerTipoCierre() throws java.rmi.RemoteException;
    public java.lang.String crearCaso(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion) throws java.rmi.RemoteException;
    public java.lang.String reabrirCaso(java.lang.String casoId, java.lang.String usuario) throws java.rmi.RemoteException;
    public java.lang.String cerrarCaso(java.lang.String casoId, java.lang.String usuario, java.lang.String comentario, java.lang.String solucion, java.lang.String clasificacion, java.lang.String monto, java.lang.String notaCredito, java.lang.String tipoCierre, java.lang.String origenId, java.lang.String enviarSMS, java.lang.String mensajeSMS) throws java.rmi.RemoteException;
    public java.lang.String crearCasoCerrado(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion, java.lang.String solucion, java.lang.String tipoCierre, java.lang.String clasificacionTipologia, java.lang.String notaCredito, java.lang.String monto, java.lang.String origenId) throws java.rmi.RemoteException;
    public java.lang.String devolverCaso(java.lang.String casoId, java.lang.String usuario, java.lang.String comentario) throws java.rmi.RemoteException;
    public java.lang.String escalarCaso(java.lang.String casoId, java.lang.String usuario, java.lang.String buzon, java.lang.String comentario) throws java.rmi.RemoteException;
    public java.lang.String liberarCaso(java.lang.String casoId, java.lang.String usuario) throws java.rmi.RemoteException;
    public java.lang.String crearProspecto(java.lang.String usuario, java.lang.String nombre, java.lang.String telefono, java.lang.String correoElectronico, java.lang.String comentarioProspecto, java.lang.String comentarioEjecutivo) throws java.rmi.RemoteException;
    public java.lang.String crearCasoProspecto(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion) throws java.rmi.RemoteException;
    public java.lang.String crearCasoEscalado(java.lang.String tipologiaid, java.lang.String usuario, java.lang.String comentarioEjecutivo, java.lang.String numCelular, java.lang.String unidadId, java.lang.String descripcion, java.lang.String buzonId) throws java.rmi.RemoteException;
    public java.lang.String actualizaUnidadUsuario(java.lang.String usuarioActualizar, long unidadId, java.lang.String usuarioModifica) throws java.rmi.RemoteException;
}
