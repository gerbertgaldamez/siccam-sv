/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.terium.siccam.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.terium.siccam.implement.ManageConnection;

/**
 *
 * @author rSianB to terium.com
 */
public class OracleConnection implements ManageConnection{

    public Connection getConneccion() {
        Connection connection = null;
 
	try {
                Class.forName("oracle.jdbc.OracleDriver");
		connection = DriverManager.getConnection("jdbc:oracle:thin:@//192.168.1.3:1521/XE","system", "rsian123");
 
	} catch (SQLException e) {
		Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, e);
		
	} catch (ClassNotFoundException ex) {
            Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public void closeConneccion(Connection connection) {
        try {
             if((connection != null)&&(!connection.isClosed())){
                connection.close();
                connection = null;
             }
        } catch (SQLException ex) {
            try {
                if((connection != null)&&(!connection.isClosed())){
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex1) {
                Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(OracleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
