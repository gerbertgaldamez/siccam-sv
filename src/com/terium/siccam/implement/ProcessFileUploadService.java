/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.implement;

import java.io.File;

/**
 *
 * @author Ramiro Antonio Sian Buenafe
 */
public interface ProcessFileUploadService {
    /**
     * @param fileToProcess
     */
    public void setFileToProcess(File fileToProcess);
    /**
     * @param realPathDown
     */
    public void setRealPathDown(String realPathDown);
    /**
     * @param cBCatalogoBancoId the cBCatalogoBancoId to set
     */
    public void setcBCatalogoBancoId(int cBCatalogoBancoId);            
    /**
     * @param cBCatalogoAgenciaId the cBCatalogoAgenciaId to set
     */
    public void setcBCatalogoAgenciaId(int cBCatalogoAgenciaId);
    /**
     * @param cBConfiguracionConfrontaId the cBConfiguracionConfrontaId to set
     */
    public void setcBConfiguracionConfrontaId(int cBConfiguracionConfrontaId);
    /**
     *
     */
    public void readFile();
    /**
     *
     */
    public void processFile();
    /**
     *
     * @return
     */
    public String responseOfProcessing();
}
