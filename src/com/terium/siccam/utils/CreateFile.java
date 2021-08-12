/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.terium.siccam.utils;

import java.io.File;

/**
 *
 * @author Ramiro Antonio Sian Buenafe
 */
public class CreateFile {

    private String uploadPath;
    private String fileName;
    private String contentFile;
    private String processResp;
    
    

    /**
     * @param uploadPath the uploadPath to set
     */
    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    /**
     * @param contentFile the contentFile to set
     */
    public void setContentFile(String contentFile) {
        this.contentFile = contentFile;
    }

    /**
     * @return the processResp
     */
    public String getProcessResp() {
        return processResp;
    }
    
    public void writeCDir(){
        File uploadDir = new File(this.uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
    }
    
    public void writeCFile(){
        String filePath = uploadPath + File.separator + fileName;
        File storeFile = new File(filePath); 
        // saves the file on disk
       
        
    }
    
    public void writeCCFile(){
        String filePath = uploadPath + File.separator + fileName;
        File storeFile = new File(filePath); 
        // saves the file on disk
        
        
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
