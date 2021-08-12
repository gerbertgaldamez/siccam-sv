package com.terium.siccam.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zul.Filedownload;

import com.terium.siccam.exception.CustomExcepcion;
import com.terium.siccam.utils.Reporte;

public class Configuracion {

	public final static String NOMBRE_APLICACION = "AUTNOTASCREDITO"; // Nombre del proyecto (Este nombre debe coincidir con el asignado en el Portal de Aplicaciones)
	public static final String PAGINA_PRINCIPAL = "/menu.zul";
	public static final String PAGINA_MENSAJE = "/mensaje.zul";
		
	//Variables que contienen los directorios de los jndi
	public final static String JNDI_PRINCIPAL = "java:comp/env/conn_principal";
	public final static String JNDI_SEGURIDAD = "java:comp/env/conn_seguridad"; 
	public final static String JNDI_GAC       = "java:comp/env/conn_gac";
	
	//Nombres de las bases de datos a las que se conecta la aplicaciï¿½n
	public static final String TGT31WEB = "TGT31WEB";
	public static final String PRINCIPAL = "SCLGTEPR";
	public static final String SCLGTEPR = "SCLGTEPR";
	public static final String TGT12WEB = "TGT31WEB";
	
	public static final String KEYCONEXION = "conexionObtenida";
	
	//objetos para formatear fechas
	public final static String TXT_FORMATO_FECHAHORA = "dd/MM/yyyy HH:mm";
	public final static String TXT_FORMATO_FECHA = "dd/MM/yyyy";
	public final static String TXT_FORMATO_HORA = "HH:mm";

	public SimpleDateFormat FORMATO_FECHA_HORA = new SimpleDateFormat(TXT_FORMATO_FECHAHORA);
	public SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat(TXT_FORMATO_FECHA);
	public SimpleDateFormat FORMATO_HORA = new SimpleDateFormat(TXT_FORMATO_HORA);

	//String de fecha que se pone donde un campo de fecha es null
	public final static String FECHAHORANULL = "01/01/1900 00:00";
	public final static String FECHANULL = "01/01/1900";
	
	//Maximo de filas que permite una hoja de excel 
	public final static Integer MAX_CELDAS_EXCEL = 65536;
	
	//mensajes de error al ejecutar la funcion doAfterCompose
	public static final String ERRORDOAFTERCOMPOSE = "Surgio un inconveniente despues de crear los componentes visuales.";
	
	/**Estas constantes definen el modo de apertura de las ventanas modales*/
	public static final int MODO_NUEVO = 1;
	public static final int MODO_MODIFICAR = 2;
	public static final int MODO_CONSULTA = 3;
	
	
	/**Mensaje que se muestra cuando el proceso es muy largo y se bloquea la pantalla*/
	public static final String MENSAJE_BUSSY= "Espere Por Favor...";
	
	/**Constantes que contienen el titulo de las ventanas emergentes de aviso,confirmaciï¿½n o error*/
	public static final String MENSAJE_APP= "Conciliaciones Bancarias"; 
	public static final String MENSAJE_ERROR= "Error";
	public static final String MENSAJE_AVISO= "Aviso";
	public static final String MENSAJE_CONFIRMACION= "Confirmacion";
	
	/**Variables que nos dicen que botones mostrar en las ventanas emergentes de aviso, confirmacion o error*/
	public static final int BOTON_OK = 0;
	public static final int BOTON_CANCEL = 1;
	public static final int BOTON_IGNORE = 2;
	public static final int BOTON_NO = 3;
	public static final int BOTON_ABORT = 4;
	public static final int BOTON_RETRY = 5;
	public static final int BOTON_YES = 6;
	
	/**Variables que nos dicen que icono se debe mostrar en las ventanas emergentes de aviso, confirmacion o error
	 * guardamos la ubicaciï¿½n del icono en estas variables*/
	public static final String ICO_INFO = "/img/globales/32x32/info-btn.png";
	public static final String ICO_QUESTION = "/img/globales/32x32/question-btn.png";
	public static final String ICO_STOP = "/img/globales/32x32/stop-btn.png";
	public static final String ICO_WARNING = "/img/globales/32x32/warning-btn.png";
	
	/**Listado de Meses
	 * */
	public static final String mes[] = {"enero", "febrero", "marzo", "abril", "mayo","junio", "julio", "agosto", "septiembre", "octubre", 
			"noviembre", "diciembre"};
	
	//Rango maximo entre fechas para consultas
	public static final int RANGO_MAX_FECHAS = 30;
	public static final BigDecimal TIPO_RECHAZO = new BigDecimal(2);
	public static final BigDecimal RANGO_MIN_INICIO = new BigDecimal(0);
	public static final BigDecimal RANGO_MAX_FIN = new BigDecimal(100000);
	
	/*Estados configurados*/
	public static final BigDecimal NODO_INICIAL = new BigDecimal(1);
	public static final BigDecimal NODO_FINAL = new BigDecimal(2);
	public static final BigDecimal NODO_FISCAL = new BigDecimal(3);
	public static final BigDecimal NODO_FINAL_RECHAZO = new BigDecimal(4);
	
	/*Codigos de estados de documento*/
	public static final BigDecimal COD_ESTADOC_PENDIENTE = new BigDecimal(201);
	public static final BigDecimal COD_ESTADOC_RECHAZO = new BigDecimal(901);
	public static final BigDecimal COD_ESTADOC_AUTORIZACION = new BigDecimal(200);
	
	public static final int parametro_pierde_iva = 60;
	
	public static final String MENSAJE_PIERDE_IVA = "Pierde IVA";
	public static final BigDecimal PIERDE_IVA = new BigDecimal(1);
	/*Constantes para selecciï¿½n de ASUME_IVA*/
	public static final BigDecimal ASUME_IVA_0 = new BigDecimal(0);
	public static final BigDecimal ASUME_IVA_1 = new BigDecimal(1);
	public static final String ASUME_IVA[] = {"Cliente","Telefï¿½nica"};
	
	//Esta funcion tiene mal la cadena de match (Verificar)
	/***
	 * Valida la direcciï¿½n de correo electronico,
	 * @param correo direcciï¿½n de correo electronico
	 * @return true correo valido, false correo invalido
	 */
	public static boolean isEmailValido(String correo) {  
		//creamos una copia de la cadena para que no nos afecte despues de las conversiones
		String copiaCorreo = correo;
		int indice = copiaCorreo.indexOf("@"); 
		if(indice==-1){//si no encuentra la arroba en la cadena retornamos false
			return false;
		}
		copiaCorreo = copiaCorreo.substring(indice+1, correo.length());
		indice = copiaCorreo.indexOf("@");
		if(indice!=-1){//si encuentra una segunda arroba retornamos false
			return false;
		}
		Pattern pat = null;        
		Matcher mat = null;
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z]{1,9}.)+[a-zA-Z]{2,3})$");        
		mat = pat.matcher(correo);        
		if (mat.find()) {     
			return true;        
		}else{            
			return false;        
		}            
	}
	
	public static String obtenerMensajeSQLExcepcion(SQLException e){
		String ret = "";
		try{
			int errorCode = 0;
			if(e instanceof SQLException){
				errorCode = ((SQLException)e).getErrorCode();
			}
			if(e.getCause() instanceof SQLException){
				errorCode = ((SQLException)e.getCause()).getErrorCode();
			}
			if(errorCode !=0){
				switch (errorCode){
				case 1: ret = "El registro que desea ingresar ya existe, ingrese uno nuevo."; break;
				case 904: ret = "Se realizo una consulta con un indice de columna no valida, consulte a su administrador."; break;
				case 936: ret = "La consulta no tiene una sintaxis valida, consulte a su administrador.";  break;
				case 942: ret = "La tabla que se esta intentando consultar no existe, consulte a su administrador.";  break;
				case 1017: ret = "Usuario o clave de base de datos invalido, consulte a su administrador.";break; 
				case 1034: ret = "La base de datos no ha sido inicializada, consulte a su administrador.";  break;
				case 1400: ret = "No se puede insertar un valor nulo en este registro."; break;
				case 1401: ret = "El valor insertado excede el maximo permitido para esta columna."; break;
				case 1722: ret = "Se trato de convertir una cadena de carateres a un numero y no fue exitoso, consulte a su administrador."; break;
				case 1403: ret = "No se encontraron datos."; break;
				case 2292: ret = "Este registro no puede ser eliminado porque existen dependencias."; break;
				case 3113: ret = "La conexion hacia la base de datos se cerro anticipadamente."; break;
				case 17002: ret = "No se pudo realizar la conexiï¿½n."; break; 
				default: 
					ret = "Ocurrio un error inesperado, comuniquese con su administrador de sistema. ("+errorCode+")";
					break;
				case 20001: ret = "No se puede eliminar ningun registro de configuracion del sistema."; break;
				}
			}
			else {
				ret = "Ocurrio un error inesperado, comuniquese con su administrador de sistema.";
			}
		}catch(Exception e1){
			Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e1);
			ret = "Ocurrio un error inesperado, comuniquese con su administrador de sistema. ";
		}
		return ret;
	}
	
	/**Funciï¿½n que realiza la descarga de un archivo
	 *  con el path , nombre y mime especificados como parametros de entrada
	 * @throws CustomExcepcion */
	public static void descargarReporte(String path,String nombreArchivo,String tipoMime){
		File file = new File(path);
		FileInputStream stream = null;
		ByteArrayInputStream bstream = null;
		try {
			if(file.exists()){
				stream = new FileInputStream(file);
				byte bytes [] = new byte[(int)file.length()];
				int b = stream.read();
				int i = 0;
				while(b!=-1){
					bytes[i]=(byte)b;
					b = stream.read();
					i++;
				}
				bstream = new ByteArrayInputStream(bytes);
				Filedownload.save(bstream, tipoMime, nombreArchivo);
			}else{
				throw new CustomExcepcion("El archivo no existe en el Servidor." );
			}
		} catch (Exception e) {
			Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
		}finally{
			try {
				if(bstream!=null){
					bstream.close();
				}
			} catch (IOException e) {
				Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
			}
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if(file != null)
				file.delete();
		}
	}
	
	public static void generarReporte(String query,String firmado,String pathArchivoEntrada,String pathArchivoSalida, String nombreReporte,String [][]header,Connection conexion){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
		SimpleDateFormat sdf_p = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		String archivo = nombreReporte+".xls";
		String archivoSalida = firmado+archivo;
		
		PreparedStatement prepare = null;
		ResultSet result = null;
		try {
			archivo = nombreReporte;
			archivoSalida = archivo+sdf.format(new Date())+".xls";
			prepare= conexion.prepareStatement(query);
			result = prepare.executeQuery(query);
			Reporte rpt = new Reporte();
			rpt.setUsuario(firmado);
			rpt.setFecha(sdf_p.format(new Date()));
			String a = rpt.getGenerarReporte(0,pathArchivoEntrada,header,
					pathArchivoSalida+archivoSalida,
					result);
			if(a.equals("OK")){
				Configuracion.descargarReporte(pathArchivoSalida+archivoSalida,archivoSalida,"application/vnd.ms-excel");
			}
		} catch (Exception e) {
			Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
		}finally{
			if(result!=null){
				try {
					result.close();
				} catch (SQLException e) {
					Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
				}
			}
			if(prepare!=null){
				try {
					prepare.close();
				} catch (SQLException e) {
					Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		}
	}
	
	
	/**
	 * Función que realiza la descarga de un archivo con el path , nombre y mime
	 * especificados como parametros de entrada
	 * 
	 * 
	 */
	public static void descargaReporte(String path, String nombreArchivo,
			String tipoMime) {
		File file = new File(path);
		FileInputStream stream = null;
		ByteArrayInputStream bstream = null;
		try {
			if (file.exists()) {
				stream = new FileInputStream(file);
				byte bytes[] = new byte[(int) file.length()];
				int b = stream.read();
				int i = 0;
				while (b != -1) {
					bytes[i] = (byte) b;
					b = stream.read();
					i++;
				}
				bstream = new ByteArrayInputStream(bytes);
				Filedownload.save(bstream, tipoMime, nombreArchivo);
			} else {
			}
		} catch (FileNotFoundException e) {
			Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
		} catch (IOException e) {
			Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
		}finally {
			if(stream != null)
				try {
					stream.close();
				} catch (IOException e) {
					Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
				}
			if(bstream != null)
				try {
					bstream.close();
				} catch (IOException e) {
					Logger.getLogger(Configuracion.class.getName()).log(Level.SEVERE, null, e);
				}
		}

	}
	
}
