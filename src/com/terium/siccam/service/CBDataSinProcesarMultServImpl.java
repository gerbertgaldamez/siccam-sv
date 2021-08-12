/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import com.terium.siccam.dao.CBDataSinProcesarDAO;
import com.terium.siccam.exception.CustomExcepcion;
import com.terium.siccam.implement.ConciliacionMultipleService;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rSianB to terium.com
 */
public class CBDataSinProcesarMultServImpl implements ConciliacionMultipleService{

    public int insertarMas(List registros, String formatFecha) {
    	
        CBDataSinProcesarDAO dataSinProcesarDAO = new CBDataSinProcesarDAO();
        int recIns = 0;
        try {
            recIns = dataSinProcesarDAO.insertarMasivo( registros);
        } catch (SQLException ex) {
           
        } catch (CustomExcepcion ex) {
            Logger.getLogger(CBDataSinProcesarMultServImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return recIns;
    }

    public int insertarMas( List registros) {
        CBDataSinProcesarDAO dataSinProcesarDAO = new CBDataSinProcesarDAO();
        int recIns = 0;
        System.out.println("lista sin procesar en dao :" + registros.size());
        recIns = dataSinProcesarDAO.insertarMasivoGT(registros);
       return recIns;
    }



}
