/*
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.terium.siccam.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.zkoss.zk.ui.util.Clients;

import com.terium.siccam.utils.Configuracion;

/**
 * @author Julio Cecilio
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Reporte {
	private FileInputStream instream;
	private FileOutputStream outstream ;
	private POIFSFileSystem fsin;
	private HSSFWorkbook    workbook;   
	private HSSFRichTextString hrts;        
	private HSSFSheet sheet;
	private HSSFRow row;
	private HSSFCell cell; 
	private String i_usuario;
	private String i_fecha;

	private static Connection getConexion(String nombre) {
		try{
			Context ctx = new InitialContext();
			return ((DataSource) ctx.lookup("java:comp/env/" + nombre))
			.getConnection();
		} catch (Exception ex) {
			Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	} 

	private HSSFRow getRow(HSSFSheet sheet, int index){
		HSSFRow row = null;
		try{			
			row = sheet.getRow(index);
			return row;
		}catch(Exception _fila){        	
			row = sheet.createRow(index);
			return row;
		}  
	}

	private HSSFRichTextString getData(String dato){
		return new HSSFRichTextString(dato);

	}	

	private Double getData(String dato, double type){
		return Double.parseDouble(dato);
	}	

	
	private boolean isDobule(String dato) {
		boolean result = false;
		try{
			Double.parseDouble(dato);
			result = true;
		}catch(Exception e){
			result = false;
		}
		
		return result;
	}
	
	private HSSFCell getCell(HSSFRow row, int index){
		HSSFCell cell = null;
		try{			
			cell = row.getCell(index);
			return cell;
		}catch(Exception _celda){	    		
			cell = row.createCell(index);
			return cell;
		}
	}

	public void setUsuario(String a){		
		i_usuario = a;
	}

	public String getUsuario(){
		return i_usuario;
	}

	public void setFecha(String a){		
		i_fecha = a;
	}

	public String getFecha(){
		return i_fecha;
	}

	public String getGenerarReporte(int columnaInicio,String archivoBase,String [][] header, String archivoSalida, ResultSet rs){
		String respuesta = "OK";
		try {
			instream = new FileInputStream(archivoBase);
			fsin     = new POIFSFileSystem(instream);
			workbook = new HSSFWorkbook(fsin, true);   
			hrts     = new HSSFRichTextString();        
			sheet    = workbook.getSheetAt(0);
			row	 	 = null;
			cell	 = null;

			/*Creamos un estilo*/
			HSSFCellStyle styleBold = workbook.createCellStyle();
			HSSFFont fontBold = workbook.createFont();
			fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			styleBold.setFont(fontBold); 

			/*Creamos una fila*/
			row  = sheet.createRow(3);
			/*Filtro USUARIO*/
			cell = row.createCell(0);
			cell.setCellValue(getData(getUsuario()));
			/*Filtro FECHA*/
			cell = row.createCell(1);
			cell.setCellValue(getData(getFecha()));
			/*Filtro 1*/
			/*cell = row.createCell(0);
			cell.setCellValue(getData(header[0][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(1);
			cell.setCellValue(getData(header[0][1]));*/
			/*Filtro 2*/
			cell = row.createCell(2);
			cell.setCellValue(getData(header[1][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(3);
			cell.setCellValue(getData(header[1][1]));
			

			/*Creamos una fila*/
			row  = sheet.createRow(4);
			/*Filtro 3*/
			cell = row.createCell(0);
			cell.setCellValue(getData(header[2][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(1);
			cell.setCellValue(getData(header[2][1]));
			/*Filtro 4*/
			cell = row.createCell(2);
			cell.setCellValue(getData(header[3][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(3);
			cell.setCellValue( getData(header[3][1]) );

			/*Creamos una fila*/
			row  = sheet.createRow(5);
			/*Filtro 5*/
			cell = row.createCell(0);
			cell.setCellValue(getData(header[4][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(1);
			cell.setCellValue(getData(header[4][1]));
			/*Filtro 6*/
			cell = row.createCell(2);
			cell.setCellValue(getData(header[5][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(3);
			cell.setCellValue(getData(header[5][1]));

			outstream = new FileOutputStream(archivoSalida);
			int columna = 0;
			int fila = 8;
			boolean encabezado = true;
			Configuracion conf = new Configuracion();
			while (rs.next()){
				if (!encabezado){
					encabezado = true;
					row	 = sheet.createRow(fila);

					row	 = sheet.createRow(fila);
					for (int i=columnaInicio;i<rs.getMetaData().getColumnCount();i++){       		
						cell = row.createCell(columna+i-columnaInicio);
						String valor = "";
//						System.out.println("Tipo de dato: " +rs.getMetaData().getColumnType(i+1));
						if(rs.getMetaData().getColumnType(i+1)==java.sql.Types.DATE){
							Object value = rs.getObject(i+1);
							//System.out.println("Valor fecha: " + value);
							
							if(value!=null){
								valor = conf.FORMATO_FECHA_HORA.format(value);
								cell.setCellValue(valor);
							}
						}else if(rs.getMetaData().getColumnType(i+1)==2){
							BigDecimal value = (BigDecimal)rs.getObject(i+1);
							if(value!=null){
								cell.setCellValue(value.doubleValue());
								
							}
						}else{
							valor = rs.getString(i+1);
							cell.setCellValue(getData(valor));
						}
					}
					fila++;
				}else{
					row	 = sheet.createRow(fila);
					for (int i=columnaInicio;i<rs.getMetaData().getColumnCount();i++){    
						//System.out.println(rs.getString(i+1));
						cell = row.createCell(columna+i-columnaInicio);
						String valor = "";
						if(rs.getMetaData().getColumnType(i+1)==java.sql.Types.DATE){
							Timestamp value = rs.getTimestamp(i+1);
							if(value!=null){
								valor = conf.FORMATO_FECHA_HORA.format(value);
								cell.setCellValue(valor);
							}
						
						}else if(rs.getMetaData().getColumnType(i+1)==2){
							BigDecimal value1 = (BigDecimal)rs.getObject(i+1);
							
							if(value1!=null){
								cell.setCellValue(value1.doubleValue());
							}							
						}else{
							valor = rs.getString(i+1);
							cell.setCellValue(getData(valor));
						}
						 
					}
					fila++;
				}	
			}
			workbook.write(outstream);
			instream.close();
			outstream.close();
		} catch (Exception e) {
			respuesta = e.getMessage();
		}
		return respuesta;
	}
	
	/**
	 * Funci�n para generar un reporte con filtros
	 * @param int columnaInicio
	 * @param int filaFiltro
	 * @param int filaResultado
	 * @param String archivoBase
	 * @param String[][] header
	 * @param String archivoSalida
	 * @param ResultSet rs
	 * @param HaxhMap<String><String> filtros
	 * @return
	 */
	public String getGenerarReporte(int columnaInicio,
									int filaFiltro,
									int filaResultado,
									String archivoBase,
									String [][] header,
									String archivoSalida, 
									ResultSet rs,
									HashMap<String, String> filtros){
		String respuesta = "OK";
		try {
			instream = new FileInputStream(archivoBase);
			fsin     = new POIFSFileSystem(instream);
			workbook = new HSSFWorkbook(fsin, true);   
			hrts     = new HSSFRichTextString();        
			sheet    = workbook.getSheetAt(0);			
			row	 	 = null;
			cell	 = null;

			/*Creamos un estilo*/
			HSSFCellStyle styleBold = workbook.createCellStyle();
			HSSFFont fontBold = workbook.createFont();
			fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			styleBold.setFont(fontBold); 

			/*Creamos una fila*/
			row  = sheet.createRow(5);
			/*Filtro USUARIO*/
			cell = row.createCell(0);
			cell.setCellValue(getData(getUsuario()));
			/*Filtro FECHA*/
			cell = row.createCell(1);
			cell.setCellValue(getData(getFecha()));
			cell = row.createCell(2);
			cell.setCellValue(getData(header[1][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(3);
			cell.setCellValue(getData(header[1][1]));			
			
            Iterator itr = filtros.entrySet().iterator();
            while (itr.hasNext()) {
                  Map.Entry e = (Map.Entry)itr.next();
                  row  = sheet.createRow(filaFiltro);
                  cell = row.createCell(0);
                  HSSFCellStyle visible = cell.getCellStyle();
                  visible.setHidden(false);
                  cell.setCellValue(getData(e.getKey().toString()));
                  cell = row.createCell(1);
                  cell.setCellValue(getData(e.getValue().toString()));
                  filaFiltro++;
            }
			
			outstream = new FileOutputStream(archivoSalida);
			int columna = 0;
			int fila = filaResultado;
			boolean encabezado = true;
			Configuracion conf = new Configuracion();
			while (rs.next()){
				if (!encabezado){
					encabezado = true;
					row	 = sheet.createRow(fila);
					row	 = sheet.createRow(fila);
					for (int i=columnaInicio;i<rs.getMetaData().getColumnCount();i++){       		
						cell = row.createCell(columna+i-columnaInicio);
						String valor = "";
						//System.out.println("Tipo de dato: " +rs.getMetaData().getColumnType(i+1));						
						if(rs.getMetaData().getColumnType(i+1)==java.sql.Types.DATE){
							Object value = rs.getObject(i+1);
							if(value!=null){
								//System.out.println("Fecha:"+value);
								valor = conf.FORMATO_FECHA_HORA.format(value);
								cell.setCellValue(valor);
								//System.out.println("Fecha:"+valor);
							}
						}else if(rs.getMetaData().getColumnType(i+1)==2){
							BigDecimal value = (BigDecimal)rs.getObject(i+1);
							if(value!=null){
								cell.setCellValue(value.doubleValue());
								//System.out.println("Num:"+value);
							}
						}else{
							valor = rs.getString(i+1);
							cell.setCellValue(getData(valor));
							//System.out.println("Otro:"+valor);
						}
					}
					fila++;
				}else{
					row	 = sheet.createRow(fila);
					for (int i=columnaInicio;i<rs.getMetaData().getColumnCount();i++){       		
						cell = row.createCell(columna+i-columnaInicio);
						String valor = "";
						if(rs.getMetaData().getColumnType(i+1)==java.sql.Types.DATE){
							Timestamp value = rs.getTimestamp(i+1);
							if(value!=null){
								valor = conf.FORMATO_FECHA_HORA.format(value);
								cell.setCellValue(valor);
							}
						
						}else if(rs.getMetaData().getColumnType(i+1)==2){
							BigDecimal value1 = (BigDecimal)rs.getObject(i+1);
							
							if(value1!=null){
								cell.setCellValue(value1.doubleValue());
							}							
						}else{
							valor = rs.getString(i+1);
							cell.setCellValue(getData(valor));
						}
						 
					}
					fila++;
				}	
			}					
			workbook.write(outstream);					
			instream.close();
			outstream.close();
		} catch (Exception e) {
			respuesta = e.getMessage();
		}
		return respuesta;
	}

	public String getGenerarReporteFormato(int columnaInicio,
			int filaFiltro,
			int filaResultado,
			String archivoBase,
			String [][] header,
			String archivoSalida, 
			ResultSet rs,
			HashMap<String, String> filtros){
		String respuesta = "OK";
		try {
			int columna = 0;
			int fila = filaResultado;
			boolean encabezado = true;
			int arrIndex = 1;

			String procesoInfo[][] = new String[1250][8]; 
			String formatoInfo[] = new String[]{"ESTADO", "AUTORIZADO POR", "FECHA INICIO", "FECHA FIN", "TIEMPO TRANSCURRIDO"};
			String notacredioInfo[][] = new String[1250][6];
			int total = -1;
			String fechaI = new String();
			String fechaF = new String();
			long num_procesoAct = 0;
			long num_procesoAnt = 0;
			int posicionCabecera = 0;
			long tiempoPromedio = 0;
			long promedio = 0;
			
			while (rs.next()){
				
				total++;
				Date fechaInicio = new Date() ;
				Date fechaFin = new Date() ;
				for (int i=columnaInicio;i<rs.getMetaData().getColumnCount();i++){
				
					//System.out.println(i + " : " + rs.getString(i+1));
					//informacion del proceso
					switch(i) {
						//NUM_PROCESO
						case 0:
							num_procesoAct = Long.parseLong(rs.getString(i+1));
							//System.out.println(num_procesoAct + " != " + num_procesoAnt + " : " + (num_procesoAct != num_procesoAnt));
							if( num_procesoAct != num_procesoAnt) {

								procesoInfo[total][0] = new String("NUMERO PROCESO");
								procesoInfo[total][1] = new String(rs.getString(i+1));
								//System.out.println("Proceso: " + rs.getString(i+1));
								procesoInfo[total][6] = new String("TIEMPO TOTAL");
								procesoInfo[total][7] = new String("0");
								//System.out.println("Total registros: " + arrIndex);
								if( arrIndex - 1 > 0) {
									long tiempoActual = 0;
									if( procesoInfo[total-arrIndex+1][7] != null ) {
										
										tiempoActual = Long.parseLong(procesoInfo[total-arrIndex+1][7]);
									}
									String tiempoTotal = new String( obtenerTiempo(tiempoActual / (arrIndex-1)) );
									tiempoPromedio += (tiempoActual / (arrIndex-1));
									//System.out.println("Promedio para " + tiempoActual + ": " + obtenerTiempo(tiempoActual / (arrIndex-1)));
									procesoInfo[total-arrIndex+1][7] = tiempoTotal;
									
								}
								posicionCabecera = total;								
								arrIndex = 1;
								num_procesoAnt = num_procesoAct;
							}							
							break;
							//CLIENTE
						case 1:
							procesoInfo[total][2] = new String("CLIENTE: ");
							procesoInfo[total][3] = new String( rs.getString(i+1) + "    -    " );
							break;
							//MONTO DE LA NC
						case 2:
							procesoInfo[total][4] = new String("MONTO: ");
							procesoInfo[total][5] = new String( rs.getString(i+1) );
							break;
						case 3:
							//NOMBRE NODO
							notacredioInfo[total][0] = new String(rs.getString(i+1));
							break;
						case 4:
							//AUTORIZADO POR
							notacredioInfo[total][1] = new String(rs.getString(i+1));
							break;
						case 5:
							//FECHA INICIO
							//Fecha creacion nota de credito
							if(arrIndex == 1) {
								//System.out.println("se toma la fecha creacion");
								notacredioInfo[total][2] = new String(rs.getString(i+1));
							}
							//Fecha en que paso de un estado a otro la nota de credito
							else {
								//System.out.println("se toma la anterior fecha de autorizacion: " + notacredioInfo[total-1][3]);
								notacredioInfo[total][2] = new String(notacredioInfo[total-1][3]);
							}
							
							if( notacredioInfo[total][2].length() > 20 ) {
								notacredioInfo[total][2] = notacredioInfo[total][2].substring(0, notacredioInfo[total][2].length()-2);
							}
							
  						    try{
  						    	
  						    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
  						    	fechaInicio = format.parse(notacredioInfo[total][2]);
  						    	format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
								notacredioInfo[total][2] = format.format(fechaInicio);
  						    	fechaI = notacredioInfo[total][2];
  						    }catch(ParseException e) {
  						    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");  
  						    	fechaInicio = format.parse(notacredioInfo[total][2]);
								notacredioInfo[total][2] = format.format(fechaInicio);
								fechaI = notacredioInfo[total][2];
  						    }
							//System.out.println( notacredioInfo[i][2] );
							break;
						case 6:
							//FECHA FIN
							notacredioInfo[total][3] = new String(rs.getString(i+1));
							try{
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
								if( notacredioInfo[total][3].length() > 20 ) {
									notacredioInfo[total][3] = notacredioInfo[total][3].substring(0, notacredioInfo[total][3].length()-2);
								}
								fechaFin = format.parse(notacredioInfo[total][3]);
								format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
								notacredioInfo[total][3] = format.format(fechaFin);
								fechaF = notacredioInfo[total][3];
							}catch(ParseException e) {
								SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm:ss");
								fechaFin = format.parse(notacredioInfo[total][3]);
								notacredioInfo[total][3] = format.format(fechaFin);
								fechaF = notacredioInfo[total][3];
							}
							
//							System.out.println("fecha fin: " + notacredioInfo[total][3]);
							//notacredioInfo[total][4] = new String( obtenerTiempo(fechaFin.getTime() - fechaInicio.getTime()) );
							long tiempoActual = 0;
							if( procesoInfo[total-arrIndex+1][7] != null ){
								
								if( !procesoInfo[total-arrIndex+1][7].equals("") ) {
								
									tiempoActual = Long.parseLong(procesoInfo[total-arrIndex+1][7]);
								}
							}
							//System.out.println("Tiempo promedio = " + tiempoActual + " sumarle " + (fechaFin.getTime() - fechaInicio.getTime()));
							String tiempoTotal = new String( "" + ( (fechaFin.getTime() - fechaInicio.getTime()) + tiempoActual ) );
							procesoInfo[total-arrIndex+1][7] = tiempoTotal;
							promedio += (fechaFin.getTime() - fechaInicio.getTime()) + tiempoActual;
							
							notacredioInfo[total][4] = new String( obtenerTiempo(fechaFin.getTime() - fechaInicio.getTime()) );
							notacredioInfo[total][5] = new String( String.valueOf(num_procesoAnt) );
							
							break;
					}
				}
				arrIndex++;
			}
			if( arrIndex - 1 > 0) {
				long tiempoActual = 0;
				if( procesoInfo[posicionCabecera][7] != null ) {
					
					tiempoActual = Long.parseLong(procesoInfo[posicionCabecera][7]);
				}
				String tiempoTotal = new String( obtenerTiempo(tiempoActual / (arrIndex-1)) );
				tiempoPromedio += (tiempoActual / (arrIndex-1));
				//System.out.println("Promedio para " + tiempoActual + ": " + obtenerTiempo(tiempoActual / (arrIndex-1)));
				procesoInfo[posicionCabecera][7] = tiempoTotal;
				promedio += (tiempoActual / (arrIndex-1));
				
				
			}
			
			
			
			
			instream = new FileInputStream(archivoBase);
			fsin     = new POIFSFileSystem(instream);
			workbook = new HSSFWorkbook(fsin, true);   
			hrts     = new HSSFRichTextString();        
			sheet    = workbook.getSheetAt(0);			
			row	 	 = null;
			cell	 = null;

			/*Creamos un estilo*/
			HSSFCellStyle styleBold = workbook.createCellStyle();
			HSSFFont fontBold = workbook.createFont();
			fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			styleBold.setFont(fontBold); 

			/*Creamos una fila*/
			row  = sheet.createRow(5);
			/*Filtro USUARIO*/
			cell = row.createCell(0);
			cell.setCellValue(getData(getUsuario()));
			/*Filtro FECHA*/
			cell = row.createCell(1);
			cell.setCellValue(getData(getFecha()));
			cell = row.createCell(2);
			cell.setCellValue(getData(header[1][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(3);
			cell.setCellValue(getData(header[1][1]));			

			Iterator itr = filtros.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				row  = sheet.createRow(filaFiltro);
				cell = row.createCell(0);
				HSSFCellStyle visible = cell.getCellStyle();
				visible.setHidden(false);
				cell.setCellValue(getData(e.getKey().toString()));
				cell = row.createCell(1);
				cell.setCellValue(getData(e.getValue().toString()));
				filaFiltro++;
			}

			outstream = new FileOutputStream(archivoSalida);
			String ncNumProceso = "";
			System.out.println("IMPRIMIENDO EN LA HOJA DE EXCEL");
			HSSFCellStyle cellStyle; 
			HSSFFont font ;
			fila = filaFiltro;
			row	 = sheet.createRow(fila);
			fila++;

			cellStyle = workbook.createCellStyle();
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
			cellStyle.setFillForegroundColor( HSSFColor.GREY_25_PERCENT.index );

			font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFColor.WHITE.index);
			
			HSSFCell cellPromedio = row.createCell(0);
			cellStyle.setFont(font);
			cellPromedio.setCellStyle(cellStyle);	
			tiempoPromedio = 0;
			
			//System.out.println("Total registros: " + total);
			
			for(int i = 0; i < total + 1; i++) {

				//ENCABEZADO DE LA NOTA DE CREDITO
				if( procesoInfo[i][1] != null ){
					ncNumProceso = procesoInfo[i][1];
				}else{
					continue;
				}
				
				//System.out.println("ENCABEZADO PARA PROCESO " + ncNumProceso);
				row	 = sheet.createRow(fila);
				fila++;

				cellStyle = workbook.createCellStyle();
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
				cellStyle.setFillForegroundColor( HSSFColor.LIGHT_BLUE.index );
				

				font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setColor(HSSFColor.WHITE.index);
				
				cellStyle.setFont(font);
				cell = row.createCell(0);
				cell.setCellStyle(cellStyle);

//				System.out.println(procesoInfo[i][1]);
				cell.setCellValue( new HSSFRichTextString(procesoInfo[i][0]) );

				cell = row.createCell(1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString(procesoInfo[i][1]) );

				cell = row.createCell(2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString(procesoInfo[i][2] + "" + procesoInfo[i][3] + " " + procesoInfo[i][4] + "Q." + procesoInfo[i][5]) );
				
				cell = row.createCell(3);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString() );
				
				cell = row.createCell(4);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString(procesoInfo[i][6]) );
				
				cell = row.createCell(5);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString(procesoInfo[i][7]) );
				
				//ENCABEZADO DE DETALLE NOTA DE CREDITO
				row	 = sheet.createRow(fila);
				fila++;

				cellStyle = workbook.createCellStyle();
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
				cellStyle.setFillForegroundColor( HSSFColor.GREY_25_PERCENT.index );

				font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setColor(HSSFColor.WHITE.index);
				
				cell = row.createCell(1);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("ESTADO") );
				
				cell = row.createCell(2);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("AUTORIZADO POR") );
				
				cell = row.createCell(3);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("FECHA INICIO") );
				
				cell = row.createCell(4);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("FECHA FIN") );
				
				cell = row.createCell(5);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("TIEMPO TRANSCURRIDO") );
				
				//DETALLE DE WORKFLOW DE LA NOTA DE CREDITO
				for(int detalleIndex = 0; detalleIndex < notacredioInfo.length; detalleIndex++) {
					
					//System.out.println(ncNumProceso + " info para nota de credito: " + notacredioInfo[detalleIndex][5]);
					//System.out.println("POSICION: " + detalleIndex);
					if( notacredioInfo[detalleIndex][5] == null ) {
						break;
					}
					if( notacredioInfo[detalleIndex][5].equals(ncNumProceso) ) {
						row	 = sheet.createRow(fila);
						fila++;

						cellStyle = workbook.createCellStyle();
						cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
						cellStyle.setFillForegroundColor( HSSFColor.WHITE.index );

						font = workbook.createFont();
						font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
						font.setColor(HSSFColor.BLACK.index);
						
						
						//System.out.println(notacredioInfo[detalleIndex][0]);
						cell = row.createCell(1);
						cellStyle.setFont(font);
						cell.setCellStyle(cellStyle);							
						cell.setCellValue( new HSSFRichTextString(notacredioInfo[detalleIndex][0]) );
						
						cell = row.createCell(2);
						cellStyle.setFont(font);
						cell.setCellStyle(cellStyle);							
						cell.setCellValue( new HSSFRichTextString(notacredioInfo[detalleIndex][1]) );
						
						cell = row.createCell(3);
						cellStyle.setFont(font);
						cell.setCellStyle(cellStyle);							
						cell.setCellValue( new HSSFRichTextString(notacredioInfo[detalleIndex][2]) );
						//System.out.print(notacredioInfo[detalleIndex][2] + " : ");
						
						cell = row.createCell(4);
						cellStyle.setFont(font);
						cell.setCellStyle(cellStyle);							
						cell.setCellValue( new HSSFRichTextString(notacredioInfo[detalleIndex][3]) );
						//System.out.println(notacredioInfo[detalleIndex][3]);

						cell = row.createCell(5);
						cellStyle.setFont(font);
						cell.setCellStyle(cellStyle);							
						cell.setCellValue( new HSSFRichTextString(notacredioInfo[detalleIndex][4]) );
						
						
						notacredioInfo[detalleIndex][5] = "";
					}
				}//FIN FOR
				
			}
//			System.out.println("Tiempo promedio: " + tiempoPromedio );
//			System.out.println("total: " + total);
			if( total <= 0 ) {
				
				total = 1;
			}
			cellPromedio.setCellValue( new HSSFRichTextString("WORKFLOW DE AUTORIZACIONES - TIEMPO PROMEDIO " + obtenerTiempo( promedio / total ) ) );
			
			
			workbook.write(outstream);					
			instream.close();
			outstream.close();
		} catch (Exception e) {
			respuesta = e.getMessage();
			Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, e);
		}
		return respuesta;
	}
	
	@SuppressWarnings("unchecked")
	public String getGenerarReporteFormatoUsuario(int columnaInicio,
		int filaFiltro,
		int filaResultado,
		String archivoBase,
		String [][] header,
		String archivoSalida, 
		ResultSet rs,
		HashMap<String, String> filtros){
		String respuesta = "OK";
		try {
			int columna = 0;
			int fila = filaResultado;
			boolean encabezado = true;
			int arrIndex = 1;

			String procesoInfo[][] = new String[250][5]; 
			String formatoInfo[] = new String[]{"ESTADO", "AUTORIZADO POR", "FECHA INICIO", "FECHA FIN", "TIEMPO TRANSCURRIDO"};
			String notacredioInfo[][] = new String[250][6];
			int total = -1;
			String fechaI = new String();
			String fechaF = new String();
			long num_procesoAct = 0;
			long num_procesoAnt = 0;
			int posicionCabecera = 0;
			
			String arrInfoEstados[][] = new String[50][5];
			Properties infoEstados = new Properties();
			String estadosKey = "";
			String arrInfoNC[] = null;
			Date fechaInicio = null;
			Date fechaFin = null;
			String userName = "";
			
			while ( rs.next() ){
				
				total++;
				fechaInicio = new Date() ;
				fechaFin = new Date() ;
				arrInfoNC = new String[7];
				for (int i=columnaInicio;i<rs.getMetaData().getColumnCount();i++){
				
					//System.out.println(i + " : " + rs.getString(i+1));
					switch (i) {
					//NUMERO DE PROCESO
					case 0:
						arrInfoNC[0] = new String(rs.getString(i+1));
						break;
					//COD CLIENTE
					case 1:
						arrInfoNC[1] = new String(rs.getString(i+1));
						break;
					//FECHA INICIO Y EL ESTADO DONDE SE ENCONTRABA LA NC
					case 2:
						String data = new String(rs.getString(i+1));
						int posMatch = data.indexOf("@$");
//						System.out.println(data.indexOf("@$"));
						arrInfoNC[2] = data.substring(data.length() - 19, data.length());
						SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
						fechaInicio = format.parse(arrInfoNC[2]);
						estadosKey = data.substring(0, posMatch);
						break;
					case 3:
						estadosKey += "$$" + new String(rs.getString(i+1)); 
						break;
					case 4:
						userName = rs.getString(i+1);
						break;
					case 5:
						arrInfoNC[5] = new String(rs.getString(i+1));
						//System.out.println(arrInfoNC[5]);
						if( !arrInfoNC[5].equals(" ") ) {
							format = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
							fechaFin = format.parse(arrInfoNC[5]);
							arrInfoNC[6] = new String( String.valueOf(fechaFin.getTime() - fechaInicio.getTime()) );
//							System.out.println("tiempo: " + (fechaFin.getTime() - fechaInicio.getTime()));
						}else {
							arrInfoNC[6] = "";
							int chrPos = estadosKey.indexOf("$$");
							estadosKey = estadosKey.substring(0, chrPos );
							
						}
						if( infoEstados.containsKey(estadosKey) ) {
							
							Collection<String[]> list = (LinkedList<String[]>) infoEstados.get(estadosKey);
							list.add(arrInfoNC);
							infoEstados.put(estadosKey, list);
							
						}
						else {
							
							Collection<String[]> list = new LinkedList<String[]>();
							list.add(arrInfoNC);
							infoEstados.put(estadosKey, list);
						}
						break;
					}
				}
				arrIndex++;
				//if( arrIndex > 2 ) break;
			}

			instream = new FileInputStream(archivoBase);
			fsin     = new POIFSFileSystem(instream);
			workbook = new HSSFWorkbook(fsin, true);   
			hrts     = new HSSFRichTextString();        
			sheet    = workbook.getSheetAt(0);			
			row	 	 = null;
			cell	 = null;

			/*Creamos un estilo*/
			HSSFCellStyle styleBold = workbook.createCellStyle();
			HSSFFont fontBold = workbook.createFont();
			fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			styleBold.setFont(fontBold); 

			/*Creamos una fila*/
			row  = sheet.createRow(5);
			/*Filtro USUARIO*/
			cell = row.createCell(0);
			cell.setCellValue(getData(getUsuario()));
			/*Filtro FECHA*/
			cell = row.createCell(1);
			cell.setCellValue(getData(getFecha()));
			cell = row.createCell(2);
			cell.setCellValue(getData(header[1][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(3);
			cell.setCellValue(getData(header[1][1]));			

			Iterator itr = filtros.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				row  = sheet.createRow(filaFiltro);
				cell = row.createCell(0);
				HSSFCellStyle visible = cell.getCellStyle();
				visible.setHidden(false);
				cell.setCellValue(getData(e.getKey().toString()));
				cell = row.createCell(1);
				cell.setCellValue(getData(e.getValue().toString()));
				filaFiltro++;
			}

			outstream = new FileOutputStream(archivoSalida);

			System.out.println("IMPRIMIENDO EN LA HOJA DE EXCEL");
			HSSFCellStyle cellStyle; 
			HSSFFont font ;
			fila = filaFiltro + 1;


			String estados = "";
			Enumeration keys = infoEstados.keys();
			long tiempoInicial = 0;
			long tiempoFinal = 0;
			long tiempoReal = 0;
			int totalTiempos = 0;
			int cantidadClientes = 0;
			HSSFCell AutperCliente = null;

			row	 = sheet.createRow(fila);
			fila++;

			cellStyle = workbook.createCellStyle();
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
			cellStyle.setFillForegroundColor( HSSFColor.GREY_25_PERCENT.index );

			font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFColor.WHITE.index);
			
			columna = 0;
			cell = row.createCell(columna);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString(userName) );
			
			cell = row.createCell(columna + 1);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("") );
			
			cell = row.createCell(columna + 2);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("---") );
			
			cell = row.createCell(columna + 3);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("") );
			
			AutperCliente = row.createCell(columna + 4);
			cellStyle.setFont(font);
			AutperCliente.setCellStyle(cellStyle);
			String clienteAnt = "";
			String clienteAct = "";
			List<String> clientesHistorico = new ArrayList<String>();

			cell = row.createCell(columna + 5);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("") );
			
			String estadoActual = "";

			while( keys.hasMoreElements() ) {
				//ENCABEZADO DE LA NOTA DE CREDITO
				
				row	 = sheet.createRow(fila);
				fila++;

				cellStyle = workbook.createCellStyle();
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
				cellStyle.setFillForegroundColor( HSSFColor.LIGHT_BLUE.index );
				
				font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setColor(HSSFColor.WHITE.index);
				
				cellStyle.setFont(font);
				columna = 1;
				cell = row.createCell(columna);
				cell.setCellStyle(cellStyle);

				
				estados = (String)keys.nextElement();
				int chrPos = estados.indexOf("$$");
				if( chrPos <= 0 ) {
					chrPos = estados.length();
					estadoActual = "";
				}else {
					estadoActual = estados.substring(chrPos + 2, estados.length());
				}
				
				cell.setCellValue( new HSSFRichTextString( estados.substring(0, chrPos) ) );

				cell = row.createCell(columna + 1);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString( estadoActual ) );

				cell = row.createCell(columna + 2);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString( "" ) );

				cell = row.createCell(columna + 3);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString("TIEMPO PROMEDIO") );
				
				tiempoInicial = 0;
				tiempoFinal = 0;
				tiempoReal = 0;
				totalTiempos = 0;
				HSSFCell cellPromedio = row.createCell(columna + 4);
				cellPromedio.setCellStyle(cellStyle);
				
				//ENCABEZADO DE DETALLE NOTA DE CREDITO
				row	 = sheet.createRow(fila);
				fila++;

				cellStyle = workbook.createCellStyle();
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
				cellStyle.setFillForegroundColor( HSSFColor.GREY_25_PERCENT.index );

				font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setColor(HSSFColor.WHITE.index);
				
				cell = row.createCell(columna);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("NUMERO PROCESO") );
				
				cell = row.createCell(columna + 1);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("CLIENTE") );
				
				cell = row.createCell(columna + 2);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("FECHA INICIO") );
				
				cell = row.createCell(columna + 3);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("FECHA FIN") );
				
				cell = row.createCell(columna + 4);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new HSSFRichTextString("TIEMPO TRANSCURRIDO") );
				
//				System.out.println("key: " + estados);
				//DETALLE DE NOTA DE CREDITO
				Collection<String[]> detalleNC = (LinkedList<String[]>) infoEstados.get(estados);
				String arrInfo[] = null;
				Iterator<String[]> it = detalleNC.iterator();
				while( it.hasNext() ) {
					
					arrInfo = it.next();
					row	 = sheet.createRow(fila);
					fila++;

					cellStyle = workbook.createCellStyle();
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
					cellStyle.setFillForegroundColor( HSSFColor.WHITE.index );

					font = workbook.createFont();
					font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
					font.setColor(HSSFColor.BLACK.index);
					
					cell = row.createCell(columna);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);							
					cell.setCellValue( new BigDecimal(arrInfo[0]).doubleValue() );
					clienteAct = arrInfo[1];
					
//					System.out.println("clienteAct: " + clienteAct);
//					System.out.println("clienteAnt: " + clienteAnt);
//					System.out.println(!clienteAct.equals(clienteAnt));
					if( !clienteAct.equals(clienteAnt) ) {
						
						if( !clientesHistorico.contains(clienteAct) ) {
							cantidadClientes++;
						}
						clientesHistorico.add(clienteAnt);
						clienteAnt = clienteAct;
					}
					
					
					cell = row.createCell(columna + 1);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);							
					cell.setCellValue( new BigDecimal(arrInfo[1]).doubleValue() );
					
					cell = row.createCell(columna + 2);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);
					cell.setCellValue( new HSSFRichTextString(arrInfo[2]) );
					
					cell = row.createCell(columna + 3);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);						
					cell.setCellValue( new HSSFRichTextString(arrInfo[5]) );

					long infoPos6 = 0;
					try{
						tiempoReal += Long.parseLong(arrInfo[6]);
						//System.out.println(" [" + (tiempoReal / 60000) + "]");
						totalTiempos++;
						infoPos6 = Long.parseLong(arrInfo[6]);
					}catch(NumberFormatException e) {
						Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, e);
					}
					
					cell = row.createCell(columna + 4);
					cellStyle.setFont(font);
					cell.setCellStyle(cellStyle);				
					cell.setCellValue( new HSSFRichTextString( obtenerTiempo( infoPos6 ) ) );
						
				}
				long infoTimeGeneral = 0;
				try {
					infoTimeGeneral = tiempoReal / totalTiempos;
				}catch(ArithmeticException e) {}
				cellPromedio.setCellValue( new HSSFRichTextString( obtenerTiempo( infoTimeGeneral ) ) );

			}
			
			AutperCliente.setCellValue( new HSSFRichTextString( "CANTIDAD DE AUTORIZACIONES POR CLIENTE " + cantidadClientes) );
			workbook.write(outstream);					
			instream.close();
			outstream.close();
		} catch (Exception e) {
			respuesta = e.getMessage();
			Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, e); 	 	
			//Clients.showBusy("",false);
		}
		return respuesta;
	}

	@SuppressWarnings("unchecked")
	public String getGenerarReporteAllFormatoUsuario(int columnaInicio,
		int filaFiltro,
		int filaResultado,
		String archivoBase,
		String [][] header,
		String archivoSalida, 
		ResultSet rs,
		HashMap<String, String> filtros){
		String respuesta = "OK";
		try {
			System.out.println("creando variables");
			int columna = 0;
			int fila = filaResultado;
			boolean encabezado = true;
			int arrIndex = 1;

			String procesoInfo[][] = new String[250][5]; 
			String formatoInfo[] = new String[]{"ESTADO", "AUTORIZADO POR", "FECHA INICIO", "FECHA FIN", "TIEMPO TRANSCURRIDO"};
			String notacredioInfo[][] = new String[250][6];

			
			//CREACION DE LA HOJA DE EXCEL Y ENCABEZADO
			instream = new FileInputStream(archivoBase);
			fsin     = new POIFSFileSystem(instream);
			workbook = new HSSFWorkbook(fsin, true);   
			hrts     = new HSSFRichTextString();        
			sheet    = workbook.getSheetAt(0);			
			row	 	 = null;
			cell	 = null;

			/*Creamos un estilo*/
			HSSFCellStyle styleBold = workbook.createCellStyle();
			HSSFFont fontBold = workbook.createFont();
			fontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			styleBold.setFont(fontBold); 

			/*Creamos una fila*/
			row  = sheet.createRow(5);
			/*Filtro USUARIO*/
			cell = row.createCell(0);
			cell.setCellValue(getData(getUsuario()));
			/*Filtro FECHA*/
			cell = row.createCell(1);
			cell.setCellValue(getData(getFecha()));
			cell = row.createCell(2);
			cell.setCellValue(getData(header[1][0]));
			cell.setCellStyle(styleBold);
			cell = row.createCell(3);
			cell.setCellValue(getData(header[1][1]));
			
			

			Iterator itr = filtros.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry e = (Map.Entry)itr.next();
				row  = sheet.createRow(filaFiltro);
				cell = row.createCell(0);
				HSSFCellStyle visible = cell.getCellStyle();
				visible.setHidden(false);
				cell.setCellValue(getData(e.getKey().toString()));
				cell = row.createCell(1);
				cell.setCellValue(getData(e.getValue().toString()));
				filaFiltro++;
			}

			outstream = new FileOutputStream(archivoSalida);
			
			HSSFCellStyle cellStyle; 
			HSSFFont font ;
			fila = filaFiltro;			
			
			
			int total = -1;
			String fechaI = new String();
			String fechaF = new String();
			long num_procesoAct = 0;
			long num_procesoAnt = 0;
			int posicionCabecera = 0;
			
			String arrInfoEstados[][] = new String[200][5];
			Properties infoEstados = new Properties();
			String estadosKey = "";
			String arrInfoNC[] = null;
			Date fechaInicio = null;
			Date fechaFin = null;
			String userNameAct = "";
			String userNameAnt = "";
			String infoGrupos = "";
			
			while ( rs.next() ) {
				
				total++;
				fechaInicio = new Date() ;
				fechaFin = new Date() ;
				arrInfoNC = new String[7];
				for (int i=columnaInicio;i<rs.getMetaData().getColumnCount();i++){
				
					//System.out.println(i + " : " + rs.getString(i+1));
					switch (i) {
					//NOMBRE DEL USUARIO
					case 0:
						userNameAct = rs.getString(i+1);
						//System.out.println("userNameAct: " + userNameAct + " userNameAnt: " + userNameAnt + "[" +( !userNameAct.equals(userNameAnt) ) + "]");
						if( !userNameAnt.equals("") ) {
							if( !userNameAct.equals(userNameAnt)) {
								//System.out.println("IMPRIMIR INFORMACION DE USUARIO");
								fila = printToExcel(infoEstados, userNameAnt, infoGrupos, fila);
								userNameAnt = userNameAct;
								infoEstados.clear();
							}
						}
						userNameAnt = userNameAct;
						break;
					//COD CLIENTE
					case 1:
						arrInfoNC[0] = new String(rs.getString(i+1));
						break;
					//NUMERO DE PROCESO
					case 2:
						arrInfoNC[1] = new String(rs.getString(i+1));
						break;
					//FECHA INICIO Y EL ESTADO DONDE SE ENCONTRABA LA NC
					case 3:
						String data = new String(rs.getString(i+1));
						int posMatch = data.indexOf("@$");
						//System.out.println(data.indexOf("@$"));
						arrInfoNC[2] = data.substring(data.length() - 19, data.length());
						SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
						fechaInicio = format.parse(arrInfoNC[2]);
						estadosKey = data.substring(0, posMatch);
						break;
						//ESTADO DONDE SE ENCUENTRALA NOTA DE CREDITO
					case 4:
						estadosKey += "$$" + new String(rs.getString(i+1)); 
						break;
						//FECHA FIN DE LA NOTA DE CREDITO Y TIEMPO PROMEDIO
					case 5:
						arrInfoNC[5] = new String(rs.getString(i+1));
						//System.out.println(arrInfoNC[5]);
						format = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
						fechaFin = format.parse(arrInfoNC[5]);
						arrInfoNC[6] = new String( String.valueOf(fechaFin.getTime() - fechaInicio.getTime()) );
						//System.out.println("tiempo: " + (fechaFin.getTime() - fechaInicio.getTime()));
						if( infoEstados.containsKey(estadosKey) ) {
							
							Collection<String[]> list = (LinkedList<String[]>) infoEstados.get(estadosKey);
							list.add(arrInfoNC);
							infoEstados.put(estadosKey, list);
							
						}
						else {
							
							Collection<String[]> list = new LinkedList<String[]>();
							list.add(arrInfoNC);
							infoEstados.put(estadosKey, list);
						}
						break;
					case 6: infoGrupos = rs.getString(i+1);break;
					}
				}
				arrIndex++;
				//if( arrIndex > 2 ) break;
			}

			//System.out.println("IMPRIMIR INFORMACION DE USUARIO");
			fila = printToExcel(infoEstados, userNameAnt, infoGrupos, fila);
			workbook.write(outstream);					
			instream.close();
			outstream.close();
		} catch (Exception e) {
			respuesta = e.getMessage();
			Logger.getLogger(Reporte.class.getName()).log(Level.SEVERE, null, e);
		}
		return respuesta;
	}

	@SuppressWarnings("deprecation")
	public int printToExcel(Properties infoEstados, String userName, String infoGrupos, int fila) {
		//System.out.println("IMPRIMIENDO EN LA HOJA DE EXCEL");
		
		String estados = "";
		Enumeration keys = infoEstados.keys();
		long tiempoInicial = 0;
		long tiempoFinal = 0;
		long tiempoReal = 0;
		int totalTiempos = 0;
		int cantidadClientes = 0;
		HSSFCell AutperCliente = null;

		row	 = sheet.createRow(fila);
		fila++;

		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
		cellStyle.setFillForegroundColor( HSSFColor.GREY_40_PERCENT.index );

		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);
		
		int columna = 0;
		cell = row.createCell(columna);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);							
		cell.setCellValue( new HSSFRichTextString(userName) );
		
		cell = row.createCell(columna + 1);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);							
		cell.setCellValue( new HSSFRichTextString("") );
		
		cell = row.createCell(columna + 2);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);							
		cell.setCellValue( new HSSFRichTextString(infoGrupos) );
		
		cell = row.createCell(columna + 3);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);							
		cell.setCellValue( new HSSFRichTextString("") );
		
		AutperCliente = row.createCell(columna + 4);
		cellStyle.setFont(font);
		AutperCliente.setCellStyle(cellStyle);
		String clienteAnt = "";
		String clienteAct = "";
		List<String> clientesHistorico = new ArrayList<String>();

		cell = row.createCell(columna + 5);
		cellStyle.setFont(font);
		cell.setCellStyle(cellStyle);							
		cell.setCellValue( new HSSFRichTextString("") );

		while( keys.hasMoreElements() ) {
			//ENCABEZADO DE LA NOTA DE CREDITO
			
			row	 = sheet.createRow(fila);
			fila++;

			cellStyle = workbook.createCellStyle();
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
			cellStyle.setFillForegroundColor( HSSFColor.LIGHT_BLUE.index );
			
			font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFColor.WHITE.index);
			
			cellStyle.setFont(font);
			columna = 1;
			cell = row.createCell(columna);
			cell.setCellStyle(cellStyle);

			estados = (String)keys.nextElement();
			int chrPos = estados.indexOf("$$");
			
			cell.setCellValue( new HSSFRichTextString( estados.substring(0, chrPos) ) );

			cell = row.createCell(columna + 1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue( new HSSFRichTextString( estados.substring(chrPos + 2, estados.length()) ) );

			cell = row.createCell(columna + 2);
			cell.setCellStyle(cellStyle);
			cell.setCellValue( new HSSFRichTextString( "" ) );

			cell = row.createCell(columna + 3);
			cell.setCellStyle(cellStyle);
			cell.setCellValue( new HSSFRichTextString("TIEMPO PROMEDIO") );
			
			tiempoInicial = 0;
			tiempoFinal = 0;
			tiempoReal = 0;
			totalTiempos = 1;
			
			HSSFCell cellPromedio = row.createCell(columna + 4);
			cellPromedio.setCellStyle(cellStyle);
			
			//ENCABEZADO DE DETALLE NOTA DE CREDITO
			row	 = sheet.createRow(fila);
			fila++;

			cellStyle = workbook.createCellStyle();
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
			cellStyle.setFillForegroundColor( HSSFColor.GREY_25_PERCENT.index );

			font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setColor(HSSFColor.WHITE.index);
			
			cell = row.createCell(columna);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("NUMERO PROCESO") );			
			
			cell = row.createCell(columna + 1);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("CLIENTE") );
			
			cell = row.createCell(columna + 2);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("FECHA INICIO") );
			
			cell = row.createCell(columna + 3);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("FECHA FIN") );
			
			cell = row.createCell(columna + 4);
			cellStyle.setFont(font);
			cell.setCellStyle(cellStyle);							
			cell.setCellValue( new HSSFRichTextString("TIEMPO TRANSCURRIDO") );
			
			//System.out.println("key: " + estados);
			//DETALLE DE NOTA DE CREDITO
			Collection<String[]> detalleNC = (LinkedList<String[]>) infoEstados.get(estados);
			String arrInfo[] = null;
			Iterator<String[]> it = detalleNC.iterator();
			while( it.hasNext() ) {
				
				arrInfo = it.next();
				row	 = sheet.createRow(fila);
				fila++;

				cellStyle = workbook.createCellStyle();
				cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND );
				cellStyle.setFillForegroundColor( HSSFColor.WHITE.index );

				font = workbook.createFont();
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setColor(HSSFColor.BLACK.index);
				
				cell = row.createCell(columna);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new BigDecimal(arrInfo[1]).doubleValue() );
				clienteAct = arrInfo[1];
/*				System.out.println("clienteAct: " + clienteAct);
				System.out.println("clienteAnt: " + clienteAnt);
				System.out.println(!clienteAct.equals(clienteAnt));
*/				if( !clienteAct.equals(clienteAnt) ) {
					if( !clientesHistorico.contains(clienteAct) ) {
						cantidadClientes++;
					}
					clientesHistorico.add(clienteAnt);
					clienteAnt = clienteAct;
				}
				
				cell = row.createCell(columna + 1);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);							
				cell.setCellValue( new BigDecimal(arrInfo[0]).doubleValue() );
								
				cell = row.createCell(columna + 2);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);
				cell.setCellValue( new HSSFRichTextString(arrInfo[2]) );
				
				cell = row.createCell(columna + 3);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);						
				cell.setCellValue( new HSSFRichTextString(arrInfo[5]) );
				
				tiempoReal += Long.parseLong(arrInfo[6]);
//				System.out.println(" [" + (tiempoReal / 60000) + "]");
				totalTiempos++;
				
				cell = row.createCell(columna + 4);
				cellStyle.setFont(font);
				cell.setCellStyle(cellStyle);				
				cell.setCellValue( new HSSFRichTextString( obtenerTiempo(Long.parseLong(arrInfo[6])) ) );
					
			}
//			System.out.println("SALIENDO DE WHILE it.hasNext");
			cellPromedio.setCellValue( new HSSFRichTextString( obtenerTiempo( tiempoReal / totalTiempos) ) );

		}
		//System.out.println("SALIENDO DE WHILE key.hasMoreElements");
		AutperCliente.setCellValue( new HSSFRichTextString( "CANTIDAD DE AUTORIZACIONES POR CLIENTE " + cantidadClientes) );	
		return fila;
	}

	public String obtenerTiempo(long diferencia){
		String tiempo = "";
		double minutos = diferencia / (1000 * 60);
        long horas = (long) (minutos / 60);
        long minuto = (long) (minutos%60);
        long segundos = (long) (diferencia / 1000) % 60;
        long dias = horas/24;
        horas = horas % 24;
        if(dias>0)
        	tiempo += String.valueOf(dias)+"d�as ";
        if(horas>0)
        	tiempo += String.valueOf(horas)+"hr. ";
        if(minuto>0)
        	tiempo += String.valueOf(minuto)+"min. ";
        if(segundos>0)
        	tiempo += String.valueOf(segundos)+"s.";
		return tiempo;
	}
	
}
