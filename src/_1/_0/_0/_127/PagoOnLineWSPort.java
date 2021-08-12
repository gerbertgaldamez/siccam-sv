/**
 * PagoOnLineWSPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package _1._0._0._127;

public interface PagoOnLineWSPort extends java.rmi.Remote {
    public dto.pagoonlinecommon.gte.tmmas.com.RespuestaPagoDTO aplicarPagoOnLine(dto.pagoonlinecommon.gte.tmmas.com.PagoDTO pagoDTO) throws java.rmi.RemoteException, exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException;
    public dto.pagoonlinecommon.gte.tmmas.com.RespuestaReversaDTO reversarPagoOnLine(dto.pagoonlinecommon.gte.tmmas.com.ReversaDTO reversaDTO) throws java.rmi.RemoteException, exception.pagoonlinecommon.gte.tmmas.com.PagoOnLineException;
    public java.lang.String foo(java.lang.String string) throws java.rmi.RemoteException;
}
