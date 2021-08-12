/**
 * 
 */
package com.terium.siccam.utils;

/**
 * @author lab
 *
 */
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.*;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.beans.PropertyDescriptor;

import org.apache.poi.hssf.usermodel.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class GeneraExcel {

	public GeneraExcel() {
	}

	public static HSSFWorkbook leeWB(String nombre) throws IOException {
		HSSFWorkbook wb = null;
		FileInputStream s = new FileInputStream(nombre);
		try {
			wb = new HSSFWorkbook(s);
		} finally {
			s.close();
			s = null;
		}
		return wb;
	}

	// public static HSSFWorkbook leeWB(String nombre) {
	// HSSFWorkbook wb = null;
	// try {
	// FileInputStream s = new FileInputStream(nombre);
	// try{
	// wb = new HSSFWorkbook(s);
	// return wb;
	// }finally{
	// s.close();
	// s = null;
	// }
	//
	// } catch (IOException e) {
	// System.out.println("erro en archivo: " + e.getLocalizedMessage());
	// // TODO: handle exception
	// }
	// return wb;
	// }

	public static String escribeWB(HSSFWorkbook wb, String nombre)
			throws IOException {
		String respuesta = "OK";
		FileOutputStream s = new FileOutputStream(nombre);
		try {
			wb.write(s);
			return respuesta;
		} finally {
			s.close();
			s = null;
		}
	}

	public static HSSFCell celda(HSSFSheet hoja, int fila, short columna) {
		fila = Math.max(fila, 0);
		columna = (short) Math.max(columna, 0);
		HSSFRow hssf_fila = hoja.getRow(fila);
		if (hssf_fila == null) {
			hssf_fila = hoja.createRow(fila);
		}
		HSSFCell hssf_celda = hssf_fila.getCell(columna);
		if (hssf_celda == null) {
			hssf_celda = hssf_fila.createCell(columna);
		}
		return hssf_celda;
	}

	public static void valorCelda(HSSFSheet hoja, int fila, short columna,
			Object valor, HSSFCellStyle estilo) {
		HSSFCell hssf_celda = celda(hoja, fila, columna);

		if (estilo != null) {
			hssf_celda.setCellStyle(estilo);
		}
		if (valor == null) {
			return;
		}
		if (valor instanceof Number) {
			hssf_celda.setCellValue(((Number) valor).doubleValue());
		} else if (valor instanceof Boolean) {
			hssf_celda.setCellValue(((Boolean) valor).booleanValue());
		} else if (valor instanceof Date) {
			hssf_celda.setCellValue((Date) valor);
		} else {
			hssf_celda.setCellValue(valor.toString());
		}

	}

	public static void valorCelda(HSSFSheet hoja, int fila, short columna,
			Object valor) {
		valorCelda(hoja, fila, columna, valor, null);
	}

	@SuppressWarnings("unused")
	public HSSFWorkbook generaHoja(ResultSet fuente, String columnas[],
			HSSFWorkbook wbBase, String titulos[], int filasTitulos[],
			short colsTitulos[], boolean cabeceras, int filaInicial,
			short colInicial, String Encabezado) throws Exception {
		HSSFWorkbook wb = wbBase;
		HSSFDataFormat df = wb.createDataFormat();
		System.out.println("1");
		if (wb == null) {
			wb = new HSSFWorkbook();
		}
		HSSFSheet hssf_hoja = null;

		if (hssf_hoja == null) {
			System.out.println("2");
			hssf_hoja = wb.getSheetAt(0);
		}
		ResultSetMetaData md = fuente.getMetaData();
		System.out.println("md:" + md.getColumnLabel(1));
		int cantCols = md.getColumnCount();
		System.out.println("3");
		if (columnas == null) {
			columnas = new String[cantCols];
			for (int i = 0; i < cantCols; i++) {
				columnas[i] = md.getColumnName(i + 1);
			}
		} else {
			cantCols = Math.min(cantCols, columnas.length);
		}
		int cantTitulos = 0;
		if (titulos != null && filasTitulos != null && colsTitulos != null) {
			cantTitulos = Math.min(titulos.length,
					Math.min(filasTitulos.length, colsTitulos.length));
		}
		filaInicial = Math.max(filaInicial, 0);
		colInicial = (short) Math.max(colInicial, 0);
		HSSFCellStyle estilosCeldas[] = new HSSFCellStyle[cantCols];

		for (short columna = 0; columna < cantCols; columna++) {
			estilosCeldas[columna] = celda(hssf_hoja, filaInicial,
					(short) (colInicial + columna)).getCellStyle();
			System.out.println("5");
		}
		if (cantCols == 0) {
			return wb;
		}

		int fila = filaInicial;
		if (cabeceras) {
			HSSFFont f = wb.createFont();
			HSSFCellStyle e = wb.createCellStyle();

			for (short columna = 0; columna < cantCols; columna++) {
				valorCelda(hssf_hoja, fila, (short) (colInicial + columna),
						columnas[columna], e);
			}

			fila++;
		}

		while (fuente.next()) { // while 1

			short columna = colInicial;

			for (int i = 0; i < cantCols; i++) { // for 1
				Object valor;

				try { // try 1
					valor = fuente.getObject(columnas[i]);
				} catch (Exception ex) { // try1
					valor = null;
				} // try 1
				HSSFCellStyle estilo = estilosCeldas[i];

				if (estilo.getDataFormat() == 0) {
					estilo = wb.createCellStyle();
					if (valor instanceof Date) {
						estilo.setDataFormat(df.getFormat("dd/mm/yyyy"));
					}

					// System.out.println("34");
					estilosCeldas[i] = estilo;

				}

				valorCelda(hssf_hoja, fila, columna, valor, estilo);

				// -----------
				columna++;
			} // for 1
			fila++;
		} // while 1

		return wb;
	}

	@SuppressWarnings("rawtypes")
	public static HSSFWorkbook generaArchivo(List listaObjetos,
			String propiedades[], String cabeceras[], HSSFWorkbook archivoBase,
			Object titulos[], int filasTitulos[], short colsTitulos[],
			int filaInicial, short colInicial) throws Exception {

		HSSFWorkbook wb = archivoBase;
		// System.out.println("1");
		if (wb == null)
			wb = new HSSFWorkbook();
		// System.out.println("2");
		HSSFDataFormat df = wb.createDataFormat();

		HSSFSheet hssf_hoja = null;
		// System.out.println("3");
		try {
			// System.out.println("4");
			hssf_hoja = wb.getSheetAt(0);
		} catch (Exception ex) {
			// System.out.println("Errror"+ ex.getMessage());
		}
		// System.out.println("5");
		if (hssf_hoja == null)
			hssf_hoja = wb.createSheet("Hoja 1");

		// System.out.println("6");
		int cantCols = propiedades.length;

		// System.out.println("7");
		int cantTitulos = 0;

		// System.out.println("8");
		if (titulos != null && filasTitulos != null && colsTitulos != null)
			cantTitulos = Math.min(titulos.length,
					Math.min(filasTitulos.length, colsTitulos.length));
		filaInicial = Math.max(filaInicial, 0);

		// System.out.println("9");
		colInicial = (short) Math.max(colInicial, 0);

		// System.out.println("10");
		HSSFCellStyle estilosCeldas[] = new HSSFCellStyle[cantCols];

		// System.out.println("12");
		for (short columna = 0; columna < cantCols; columna++)
			estilosCeldas[columna] = celda(hssf_hoja, filaInicial,
					(short) (colInicial + columna)).getCellStyle();

		// System.out.println("13");
		for (int i = 0; i < cantTitulos; i++)
			valorCelda(hssf_hoja, filasTitulos[i], colsTitulos[i], titulos[i]);

		// System.out.println("14");
		if (cantCols == 0)
			return wb;

		// System.out.println("15");
		int fila = filaInicial;

		// System.out.println("16");
		if (cabeceras != null && cabeceras.length > 0) {
			int cantCabe = cabeceras.length;
			HSSFFont f = wb.createFont();
			HSSFCellStyle e = wb.createCellStyle();
			f.setBoldweight((short) 700);
			e.setFont(f);

			// System.out.println("17");
			for (short columna = 0; columna < cantCabe; columna++)
				valorCelda(hssf_hoja, fila, (short) (colInicial + columna),
						cabeceras[columna], e);
			// System.out.println("18");
			fila++;
		}

		// System.out.println("19");
		int numRow = listaObjetos.size();
		short formatoFecha = 164;
		short formatoGeneral = 0;
		// System.out.println("20");
		HSSFCellStyle estilo = null;
		for (int j = 0; j < numRow; j++) {
			// System.out.println("21");
			short columna = colInicial;
			// System.out.println("22");
			List fuente = getParametrosBean(listaObjetos.get(j), propiedades);
			// System.out.println("30");

			for (int i = 0; i < cantCols; i++) {
				Object valor;
				// System.out.println("31");
				try {
					// System.out.println("32");
					valor = fuente.get(i);
				} catch (Exception ex) {
					// System.out.println("Error 2: "+ ex.getMessage());
					valor = null;
				}
				// System.out.println("33");
				estilo = estilosCeldas[i];
//				System.out.println("DATA FORMAT "
//						+ estilo.getDataFormat());
			
				if (estilo.getDataFormat() == 0) {
					// estilo = wb.createCellStyle();
					//System.out.println("INGRESO ACA: " + i);
					if (valor instanceof String) {
						//System.out.println("ES String");
						estilo.setDataFormat(formatoGeneral);
					} /*else if (valor instanceof Date) {
						System.out.println("ES FECHA");
						estilo.setDataFormat(df.getFormat("dd/mm/yyyy"));
					}*/

					// System.out.println("34");
					estilosCeldas[i] = estilo;

				}else if(estilo.getDataFormat() == 164){
					if (valor instanceof Date)
					estilo.setDataFormat(df.getFormat("dd/mm/yyyy"));
					
					
					estilosCeldas[i] = estilo;
				}
				// System.out.println("35");
				estilo.setWrapText(true);
				valorCelda(hssf_hoja, fila, columna, valor, estilo);
				columna++;
				// System.out.println("36");
			}

			fila++;
		}
		// System.out.println("37");
		return wb;

	}

	public static List getParametrosBean(Object bean,
			PropertyDescriptor[] properties) throws Exception {
		List params = new ArrayList();
		for (int i = 0; i < properties.length; i++) {
			PropertyDescriptor property = properties[i];
			Object value = null;
			Method method = property.getReadMethod();
			if (method == null) {
				throw new Exception("No se puede leer el metodo del bean "
						+ bean.getClass() + " " + property.getName());
			}
			try {
				value = method.invoke(bean, new Object[0]);
			} catch (Exception e) {
				throw new RuntimeException("No se puede invoke el metodo: "
						+ method, e);
			}
			params.add(value);

		}
		return params;

	}

	public static List getParametrosBean(Object bean, String[] propertyNames)
			throws Exception {
		PropertyDescriptor[] descriptors;
		// System.out.println("23");
		try {
			descriptors = Introspector.getBeanInfo(bean.getClass())
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
			throw new Exception("No se puede Introspectar el bean:"
					+ bean.getClass().toString(), e);
		}
		// System.out.println("24");

		PropertyDescriptor[] sorted = new PropertyDescriptor[propertyNames.length];
		// System.out.println("25");
		for (int i = 0; i < propertyNames.length; i++) {
			String propertyName = propertyNames[i];
			if (propertyName == null) {
				throw new NullPointerException(
						"propertyName no puede ser null: " + i);
			}
			// System.out.println("26");
			boolean found = false;
			for (int j = 0; j < descriptors.length; j++) {
				PropertyDescriptor descriptor = descriptors[j];
				// System.out.println("27");
				if (propertyName.equals(descriptor.getName())) {
					sorted[i] = descriptor;
					found = true;
					break;
				}
			}
			// System.out.println("28");
			if (!found) {
				throw new RuntimeException(
						"No se puede encontrar la propiedad del bean: "
								+ bean.getClass() + " " + propertyName);
			}
		}
		// System.out.println("29");
		return getParametrosBean(bean, sorted);

	}

}