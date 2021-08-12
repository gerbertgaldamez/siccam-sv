/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import java.sql.Connection;
import java.util.List;

import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.Orden;

/**
 *
 * @author Ramiro Antonio Sian Buenafe
 */
public abstract class ConciliacionServiceC {
        public abstract int insertar(Connection conn, Object registro);
        public abstract int actualiza(Connection conn, Object registro);
        public abstract List<Object> Listado(Connection conn, List<Filtro> filtro, List<Orden> orden);
}
