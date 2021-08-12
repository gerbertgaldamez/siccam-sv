/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.terium.siccam.service;

import java.io.File;

import com.terium.siccam.implement.ProcessFileUploadService;

/**
 *
 * @author Ramiro Antonio Sian Buenafe
 */
public class ProcessFileTxtUploadServiceImpl implements ProcessFileUploadService {

    private File fileToProcess;
    private int cBCatalogoBancoId;
    private int cBCatalogoAgenciaId;
    private int cBConfiguracionConfrontaId;
    private String realPathDown;
    private String responseOfProcessing;

    
    public void setFileToProcess(File fileToProcess) {
        this.fileToProcess = fileToProcess;
    }

    
    public void setRealPathDown(String realPathDown) {
        this.realPathDown = realPathDown;
    }

    
    public void setcBCatalogoBancoId(int cBCatalogoBancoId) {
        this.cBCatalogoBancoId = cBCatalogoBancoId;
    }

    
    public void setcBCatalogoAgenciaId(int cBCatalogoAgenciaId) {
        this.cBCatalogoAgenciaId = cBCatalogoAgenciaId;
    }

    
    public void setcBConfiguracionConfrontaId(int cBConfiguracionConfrontaId) {
        this.cBConfiguracionConfrontaId = cBConfiguracionConfrontaId;
    }

    
    public void readFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public void processFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    public String responseOfProcessing() {
        return responseOfProcessing;
    }
}
