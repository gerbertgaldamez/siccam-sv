/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import com.terium.siccam.dao.CBCatalogoOpcionDAO;
import com.terium.siccam.implement.ConciliacionService;
import com.terium.siccam.model.CBCatalogoOpcionModel;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.Orden;

import java.sql.Connection;
import java.util.List;
import org.zkoss.zul.ListModelList;

/**
 *
 * @author rSianB to terium.com
 */
public class CBCatalogoServImpl implements ConciliacionService{

    public int insertar(Connection conn, Object registro) {
        CBCatalogoOpcionDAO catalogoOpcionDAO = new CBCatalogoOpcionDAO();
        int resp = 0;
        resp = catalogoOpcionDAO.insertar( (CBCatalogoOpcionModel) registro);
        return resp;        
        
    }

    public int actualiza(Connection conn, Object registro) {
        CBCatalogoOpcionDAO catalogoOpcionDAO = new CBCatalogoOpcionDAO();
        int resp = 0;
        resp = catalogoOpcionDAO.actualiza((CBCatalogoOpcionModel) registro);
        return resp; 
    }

    public List Listado(Connection conn, List<Filtro> filtro, List<Orden> orden) {
        CBCatalogoOpcionDAO catalogoOpcionDAO = new CBCatalogoOpcionDAO();
        List<CBCatalogoOpcionModel> customList = new ListModelList<CBCatalogoOpcionModel>();
        customList = catalogoOpcionDAO.Listado(filtro, orden);
        return customList;
    }

}
