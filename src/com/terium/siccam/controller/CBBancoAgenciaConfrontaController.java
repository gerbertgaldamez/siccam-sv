/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.controller;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.model.CBCatalogoAgenciaModel;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.model.CBConfiguracionConfrontaModel;




/**
 *
 * @author rSianB to terium.com
 */
@SuppressWarnings("serial")
public class CBBancoAgenciaConfrontaController extends ControladorBase{
    
    private CBCatalogoBancoModel banco = new CBCatalogoBancoModel();
    private CBCatalogoAgenciaModel agencia = new CBCatalogoAgenciaModel();
    private CBConfiguracionConfrontaModel confronta = new CBConfiguracionConfrontaModel();



    /**
     * @return the banco
     */
    public CBCatalogoBancoModel getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(CBCatalogoBancoModel banco) {
        this.banco = banco;
    }

    /**
     * @return the agencia
     */
    public CBCatalogoAgenciaModel getAgencia() {
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(CBCatalogoAgenciaModel agencia) {
        this.agencia = agencia;
    }

    /**
     * @return the confronta
     */
    public CBConfiguracionConfrontaModel getConfronta() {
        return confronta;
    }

    /**
     * @param confronta the confronta to set
     */
    public void setConfronta(CBConfiguracionConfrontaModel confronta) {
        this.confronta = confronta;
    }

}
