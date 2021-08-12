package com.terium.siccam.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.utils.Constantes;

/**
 * @author CarlosGodinez -> 10/10/2018
 * */
public class CBManualUsuarioController extends ControladorBase{
	
	private static final long serialVersionUID = 4285297288749865925L;
	
	Window manualUsuario;
	
	public void doAfterCompose(Component param) {
		try{
			super.doAfterCompose(param);
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBManualUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	/**
	 * Boton de cancelar
	 * */
	public void onClick$btnCancelar() {
		try {
			manualUsuario.detach();
		} catch(Exception e) {
			Messagebox.show("Ha ocurrido un error.", "ATENCION", Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBManualUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		}
	} 
	
	/**
	 * Descargar archivo PDF desde server FTP configurado
	 * */
	public void onClick$btnDescargarArchivo() {
		FTPClient ftpClient = new FTPClient();
		boolean success;
		try {
			String server = "10.50.15.4";
			int port = 21;
			String user = "interfaz";
			String pass = "Interfaz";
			String rutaArchivoFTP = "/home/users/interfaz/terium/manualusuariosiccam/";
			String nombreArchivo = "Manual de Usuario - SICCAM.pdf";
			
			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			String remoteFile1 = rutaArchivoFTP + nombreArchivo;
			File downloadFile1 = new File("Manual de Usuario - SICCAM.pdf");
			OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile1);
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                outputStream2.write(bytesArray, 0, bytesRead);
            }
 
            success = ftpClient.completePendingCommand();
            if (success) {
                System.out.println("File #2 has been downloaded successfully.");
            }
            outputStream2.close();
            inputStream.close();

			Filedownload.save(downloadFile1, null); 
			Messagebox.show("Archivo  descargado con exito", "ATENCION", Messagebox.OK,
					Messagebox.INFORMATION);
		} catch (IOException ex) {
			Messagebox.show("Ha ocurrido un error al intentar descargar archivo PDF desde server FTP configurado",
					Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBManualUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception e) {
			Messagebox.show("Ha ocurrido un error.", Constantes.ATENCION, Messagebox.OK, Messagebox.ERROR);
			Logger.getLogger(CBManualUsuarioController.class.getName()).log(Level.SEVERE, null, e);
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				Logger.getLogger(CBManualUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
