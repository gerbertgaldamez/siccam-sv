package com.terium.siccam.utils;

/**
 * @author Juankrlos
 */
public class Constantes {

	// CONSTANTES DE USO GENERAL

	public static final String ATENCION = "ATENCION";
	public static final String CONFIRMACION = "CONFIRMACION";
	public static final String ADVERTENCIA = "ADVERTENCIA";
	public static final String TODOS = "TODOS";
	public static final String TODAS = "Todas";

	public static final String STR_CONEX = "java:comp/env/";
	public static final String CONEXION = "conexion";

	public static final String TEXT_RIGHT = "text-align: right";
	public static final String TEXT_CENTER = "text-align: center";

	public static final String IP_DEFAULT = "0.0.0.0";

	// Parametros para consumo de WS Pagos
	public static final String AGENCIA = "AGENCIA";
	public static final String CAJERO = "CAJERO";
	public static final String COD_BANCO = "COD_BANCO";
	public static final String TIPO_OPERACION = "TIPO_OPERACION";
	public static final String TIPOLOGIAGACID = "TIPOLOGIAGACID";
	public static final String UNIDADID = "UNIDADID";
	public static final String SOLUCION = "SOLUCION";
	public static final String TIPO_CIERRE = "TIPO_CIERRE";
	public static final String URLWSPAGOS = "URLWSPAGOS";
	public static final String COD_AGENCIA = "COD_AGENCIA";

	/**
	 * Formato de fecha
	 */
	public static final String FORMATO_FECHA1 = "dd/MM/yyyy";
	public static final String FORMATO_FECHA2 = "yyyyMMdd_HH_mm_ss";
	public static final String FORMATO_FECHA3 = "yyyyMMdd";
	public static final String FORMATO_FECHA4 = "dd-MM-yyyy";

	/**
	 * Extencion de archivos ======> uso general
	 * 
	 */
	public static final String CSV = ".csv";
	public static final String CSV2 = "csv";
	public static final String XLSX = "xlsx";
	public static final String XLS = "xls";
	public static final String TXT = "txt";

	/**
	 * Para uso en clase CBConciliacionBancosController.java
	 * 
	 */
	public static final String REPORTE_CONCILIACION_BANCOS = "reporte_conciliacion_bancos_";

	public static final String ENCABEZADO_CONCILIACION_BANCOS = "Fecha|Entidad|Codigo Colector|"
			+ "Estado confronta|Confronta|Diferencia confronta|" + "Pagos del dia|Pagos otros dias|Pagos otros meses|"
			+ "Reversas otros dias|Reversas otros meses|Total dia|Total general|"
			+ "Estado cuenta|Diferencia total|Porcentaje|Comision confronta|" + "Comision|Diferencia comision|"
			+ "Comision total|Recaudacion final|" + "Total final\n";

	/**
	 * Para uso en clase CBAplicaDesaplicaPagosController.java
	 */
	public static final String REPORTE_APLICADESAPLICA = "reporte_aplicadesaplica_";

	public static final String ENCABEZADO_APLICADESAPLICA = "Entidad|Dia|Tipo|Cliente|Nombre|Des Pago|"
			+ "Trans Telca|Telefono|Trans Banco|Pago Telefonica|Pago Banco|"
			+ "Manual|Pendiente Banco|Pendiente Telefonica|Sucursal|Nombre Sucursal|"
			+ "Tipo Sucursal|Comision|Monto Comision|Estado\n";

	/**
	 * Para uso en clase CBConsultaEstadoCuentasController.java
	 */
	public static final String REPORTE_ESTADO_CUENTA = "reporte_estados_cuenta_";

	public static final String ENCABEZADO_ESTADO_CUENTA = "BANCO|AGENCIA|CUENTA|ASIGNACION|FECHA|TEXTO|DEBE|"
			+ "HABER|IDENTIFICADOR|ID TIPOLOGIA|TIPOLOGIA|"
			+ " CODIGO COLECTOR|ENTIDAD TIPOLOGIA|TEXTO CAB.DOCUMENTO|OBSERVACIONES|FECHA INGRESOS|NUMERO DE DOCUMENTO\n";

	/**
	 * Para uso en clase CBConsultaEstadoCuentasTarjetaController.java
	 */
	public static final String REPORTE_ESTADO_CUENTA_TARJETA = "reporte_estados_cuenta_tarjeta_";

	public static final String ENCABEZADO_ESTADO_CUENTA_TARJETA = "TIPO TARJETA|FECHA TRANSACCION|AFILIACION|TIPO|REFERENCIA|LIQUIDO|"
			+ "COMISION|IVA COMISION|RETENCION|CONSUMO|ENTIDAD\n";
	
	public static final String OBTENER_COD_AGENCIA ="select cod_agencia from cb_agencias_confronta where cbbancoagenciaconfrontaid = ?";

	/**
	 * Iconos
	 */
	public static final String ARCHIVO_SELECCIONADO = "Archivo seleccionado: ";
	public static final String COLOR_BLUE = "color:blue;";
	public static final String COLOR_RED = "color:red;";
	public static final String IMG_BLUE = "img/azul.png";
	public static final String IMG_RED = "img/rojo.png";
	public static final String IMG_CHECK_16X16 = "img/globales/16x16/check.png";
	public static final String IMG_DELETE_16X16 = "/img/globales/16x16/delete.png";

	public static final String EXTRACTO_BANCARIO = "EXTRACTO BANCARIO";
	public static final CharSequence COLUMN = "COLM";
	public static final String SICCAMSV = "SICCAMSV";

	// Parametros para WS SV Ejecutar Pago
	public static final String BILL_REF_NO = "0";
	public static final int EJ_PAGO_WS_TIPO = 3;
	public static final String EJ_PAGO_WS_NUM_CHEQUE = " ";
	public static final String EJ_PAGO_WS_NUM_TARJETA = "00000";
	public static final String EJ_PAGO_WS_AUTORIZACION = "123123123";
	public static final String STATUS_CODE = "statusCode";
	public static final String MESSAGE_ERROR = "message";
	public static final String STATUS = "00";
	public static final String REVERSA_PAGO_WS_REFERENCIA ="0000";
	public static final String NOM_FECHA_TIPO5 = "0";

}
