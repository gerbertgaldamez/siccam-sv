/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import com.terium.siccam.dao.CBCatalogoBancoDAO;
import com.terium.siccam.implement.ConciliacionService;
import com.terium.siccam.model.CBCatalogoBancoModel;
import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.Orden;

import java.sql.Connection;
import java.util.List;
import org.zkoss.zul.ListModelList;

/**
 *
 * @author rSianB to terium.com
 */
public class CBBancoServImpl implements ConciliacionService {

    public int insertar(Connection conn, Object registro) {
        CBCatalogoBancoDAO bancoDAO0 = new CBCatalogoBancoDAO();
        int resp = 0;
        resp = bancoDAO0.insertar((CBCatalogoBancoModel) registro);
        return resp;
    }

    public int actualiza(Connection conn, Object registro) {
        CBCatalogoBancoDAO bancoDAO0 = new CBCatalogoBancoDAO();
        int resp = 0;
        resp = bancoDAO0.actualiza( (CBCatalogoBancoModel) registro);
        return resp;
    }

    public List<CBCatalogoBancoModel> Listado(Connection conn, List<Filtro> filtro, List<Orden> orden) {        
        CBCatalogoBancoDAO bancoDAO0 = new CBCatalogoBancoDAO();
        List<CBCatalogoBancoModel> customList = new ListModelList<CBCatalogoBancoModel>();
        customList = bancoDAO0.Listado(filtro, orden);        
       return customList;
    }

}
