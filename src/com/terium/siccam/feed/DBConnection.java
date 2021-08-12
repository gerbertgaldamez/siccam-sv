/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.feed;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.implement.ManageConnection;
import com.terium.siccam.service.OracleConnection;

import java.sql.Connection;

/**
 *
 * @author rSianB to terium.com
 */
@SuppressWarnings("serial")
public class DBConnection extends ControladorBase{
    private ManageConnection manageConnection = new OracleConnection(); //new MySQLConnection();
    
    public Connection getConneccion() {
        Connection conn = null; 
        try {
             conn = obtenerDtsPromo().getConnection();
        } catch (Exception e) {
            conn = null; 
        }          
        /*Commentar*/
        if(conn != null){
            return conn;
        }else{
            conn = manageConnection.getConneccion();
        }
        /*Commentar*/
        return conn;
    }
    
    public void closeConneccion(Connection connection) {
        manageConnection.closeConneccion(connection);
    }
}
