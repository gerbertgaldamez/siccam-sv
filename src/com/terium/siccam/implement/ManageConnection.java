/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.terium.siccam.implement;

import java.sql.Connection;

/**
 *
 * @author rSianB to terium.com
 */
public interface ManageConnection {
    public Connection getConneccion();
    public void closeConneccion(Connection connection);
    
}
