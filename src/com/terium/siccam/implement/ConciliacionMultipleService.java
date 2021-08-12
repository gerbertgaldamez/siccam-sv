/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.implement;

import java.util.List;

/**
 *
 * @author rSianB to terium.com
 */
public interface ConciliacionMultipleService {
    
    @SuppressWarnings("rawtypes")
	public int insertarMas( List registros, String formatFecha);
    @SuppressWarnings("rawtypes")
	public int insertarMas(List registros);

}
