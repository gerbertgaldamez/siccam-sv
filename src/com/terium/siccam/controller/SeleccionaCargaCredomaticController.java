package com.terium.siccam.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

import com.terium.siccam.composer.ControladorBase;
import com.terium.siccam.implement.SeleccionaCargaCredomaticInfz;

/**
 * @author Juankrlos by 17/03/2017
 * */
public class SeleccionaCargaCredomaticController extends ControladorBase{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7941396657709194341L;
	
	//Variable de session
	HttpSession misession= (HttpSession) Sessions.getCurrent().getNativeSession();
	
	//Componentes ZK
	Window wdTipoCarga;
	Media archivo = null;
	Combobox entidad;
	Label label = null;
	Label lblPregunta;
	Image img = null;
	
	//Variables de uso global
	int id = 0;
	String format = null;
	

	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);	
		archivo = (Media) misession.getAttribute("archivo");
		entidad = (Combobox) misession.getAttribute("cbxEntidad");
		id = (Integer) misession.getAttribute("idarchivo");
		format = (String) misession.getAttribute("formato");
		label = (Label) misession.getAttribute("label");
		img = (Image) misession.getAttribute("imagen");
		lblPregunta.setValue("Que version desea utilizar en la carga del archivo "+archivo.getName()
				+ " para los estados de cuenta de CREDOMATIC?");
		Logger.getLogger(ConciliacionDetalleController.class.getName())
			.log(Level.INFO, "Archivo obtenido: "+archivo.getName());
	}
	
	public void onClick$btnV1(){
		SeleccionaCargaCredomaticInfz intfz = new CBEstadoCuentasController();
		intfz.seleccionaVuno(archivo, entidad, id, format, label, img);
		wdTipoCarga.detach();
	}
	
	public void onClick$btnV2(){
		SeleccionaCargaCredomaticInfz intfz = new CBEstadoCuentasController();
		intfz.seleccionaVdos(archivo, entidad, id, format, label, img);;
		wdTipoCarga.detach();
	}
}
