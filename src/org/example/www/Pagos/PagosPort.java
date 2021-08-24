/**
 * PagosPort.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.example.www.Pagos;

public interface PagosPort extends java.rmi.Remote {
    public org.example.www.Pagos.SaldoDetalle[] consultaSaldo(org.example.www.Pagos.ConsultaSaldoRequest consultaSaldoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.ConsultaSaldoFault;
    public org.example.www.Pagos.PagoDetalle[] ejecutarPago(org.example.www.Pagos.EjecutarPagoRequest ejecutarPagoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.EjecutarPagoFault;
    public org.example.www.Pagos.ReversaPagoDetalle[] reversaPago(org.example.www.Pagos.ReversaPagoRequest reversaPagoRequest) throws java.rmi.RemoteException, org.example.www.Pagos.ReversaPagoFault;
}
