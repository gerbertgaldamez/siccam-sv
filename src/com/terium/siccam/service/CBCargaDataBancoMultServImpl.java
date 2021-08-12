/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import com.terium.siccam.dao.CBDataBancoDAO;
import com.terium.siccam.exception.CustomExcepcion;
import com.terium.siccam.implement.ConciliacionMultipleService;
import com.terium.siccam.model.CBDataBancoModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rSianB to terium.com
 */
public class CBCargaDataBancoMultServImpl implements ConciliacionMultipleService{
    
    public int insertarMas( List registros, String formatFecha) {
        CBDataBancoDAO dataBancoDAO = new CBDataBancoDAO();
        int recIns = 0;
        recIns = dataBancoDAO.insertarMasivo( registros, formatFecha);
        return recIns;
    }

    public int insertarMas(List registros) {
        CBDataBancoDAO dataBancoDAO = new CBDataBancoDAO();
        int recIns = 0;
        recIns = dataBancoDAO.insertarMasivoGT( registros);
        return recIns;
    }

	

}
