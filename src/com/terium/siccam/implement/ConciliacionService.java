/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.implement;

import java.sql.Connection;
import java.util.List;

import com.terium.siccam.utils.Filtro;
import com.terium.siccam.utils.Orden;

/**
 *
 * @author Ramiro Antonio Sian Buenafe
 */
public interface ConciliacionService {

        public int insertar(Connection conn, Object registro);
        public int actualiza(Connection conn, Object registro);
        @SuppressWarnings("rawtypes")
		public List Listado(Connection conn, List<Filtro> filtro, List<Orden> orden);
}
