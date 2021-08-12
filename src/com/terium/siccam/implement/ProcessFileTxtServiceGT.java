/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.implement;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.List;

import com.terium.siccam.model.CBDataBancoModel;
import com.terium.siccam.model.CBDataSinProcesarModel;

/**
 *
 * @author rSianB to terium.com
 */
public interface ProcessFileTxtServiceGT {
	 public List<CBDataBancoModel> leerArchivoGT(BufferedReader br,int idBanco,int idAgencia,int idConfigAgencia,int idConfronta, String user, String nombreArchivo, String tipo, BigDecimal comisionConfronta);
	    public String guardarInfArchivo(List<CBDataBancoModel> dataArchivo,int idBanco,int idAgencia,int idConfronta);
	    public List<CBDataSinProcesarModel> getDataSinProcesar();


}
