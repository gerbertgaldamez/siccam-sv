package com.terium.siccam.sql;

/**
 * @author Juankrlos - QitCorp
 * @category Querys generales del sistema
 **/
public class ConsultasSQ {
	public static final String OBTIENE_ESTADO_CUENTACONF_SQ = "select cbestadocuentaconfid, nombre_banco  "
			+ "from cb_estado_cuenta_conf where estado = 'S'";

	//
	public static final String MUESTRA_FECHA_SQ = "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') FROM DUAL";

	//
	public static final String ESTADO_CUENTA_ARCHIVOS_ID = "SELECT CB_ESTADO_CUENTA_ARCHIVOS_SQ.NEXTVAL FROM DUAL";

	//
	public static final String VALIDA_CARGA_ESTADOS_SQ = "SELECT CBESTADOCUENTAARCHIVOSID, NOMBRE, SWDATECREATED  "
			+ "FROM CB_ESTADO_CUENTA_ARCHIVOS WHERE CBESTADOCUENTACONFID = ? AND NOMBRE = ? ";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertTableArchivos()
	public static final String INSERT_TABLE_ARCHIVOS = "INSERT INTO CB_ESTADO_CUENTA_ARCHIVOS(CBESTADOCUENTAARCHIVOSID,"
			+ " CBESTADOCUENTACONFID, NOMBRE,DESCRIPCION, SWCREATEBY, SWDATECREATED)" + " VALUES(?,?,?,?,?,sysdate)";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentasCredo()
	public static final String INSERT_CREDO_SQ = "INSERT INTO CB_ESTADO_CUENTA(CBESTADOCUENTAID, CBESTADOCUENTACONFID, "
			+ " CBESTADOCUENTAARCHIVOSID, FECHA_TRANSACCION,REFERENCIA, CODIGO_LOTE, "
			+ " DEBITO, CREDITO, BALANCE, DESCRIPCION, SWCREATEBY, SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_SQ.NEXTVAL, ?,?,to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,?,sysdate)";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentasVisaDet()
	public static final String INSERT_VISA_DET_SQ = "INSERT INTO CB_ESTADO_CUENTA_DET(CBESTADOCUENTADETID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, AFILIACION, F_CIERRE,TERMINAL, LOTE, TARJETA, FECHA_VENTA,"
			+ " HORA, AUTORIZACION, CONSUMO, TIPO_TRANS, IMP_TURISMO, PROPINA, COMISION, IVA, LIQUIDO, SWCREATEBY, SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_DET_SQ.NEXTVAL, ?,?,?,to_date(?,'dd/MM/yyyy'),?,?,?,to_date(?,'dd/MM/yyyy'),"
			+ " ?,?,?,?,?,?,?,?,?,?,SYSDATE)";

	public static final String UPDATE_LIQUIDACION_DETALLE = "update cb_liquidacion_detalle "
			+ "set cbestadocuentaid = null "
			+ "where cbestadocuentaid in ( select cbestadocuentasociedadid from cb_estado_cuenta_sociedad "
			+ "where cbestadocuentaarchivosid = ? )";

	// Agregado por CarlosGodinez - 20/03/2017 - QitCorp

	public static final String OBTENER_PK_LIQUIDACION = "SELECT CB_LIQUIDACION_SQ.NextVal FROM DUAL";

	public static final String INSERT_LIQUIDACION = "insert into CB_LIQUIDACION (CBLIQUIDACIONID, NOMBTRANSACCION, FECHATRANSACCION, "
			+ "DESCRIPCION, ESTADO, SWCREATEBY, SWDATECREATED, SWMODIFYBY, SWDATEMODIFIED) values(?,?,to_date(?,'DD-MM-YYYY'),?,?,?,sysdate,null, null)";

	public static final String DELETE_LIQUIDACION = "DELETE FROM CB_LIQUIDACION WHERE CBLIQUIDACIONID = ?";

	public static final String VALIDA_LIQUIDACION = "SELECT CBLIQUIDACIONID FROM CB_LIQUIDACION WHERE nombtransaccion = ? AND fechatransaccion = to_date(?, 'DD-MM-YYYY')";

	public static final String CONSULTAR_LIQUIDACIONES = "SELECT cbliquidacionid, nombtransaccion, to_char(fechatransaccion,'DD-MM-YYYY') Fecha_Transaccion, Efectivo, CuotasVisa, "
			+ "CuotasCredomatic, Visa, Credomatic, Otras, Cheque, Excencion_IVA, Deposito, SWCREATEBY, to_char(SWDATECREATED,'DD-MM-YYYY') FechaCreacion "
			+ "FROM CB_LIQUIDACION_CAJEROS_VW ";

	public static final String OBTENER_PK_LIQUIDACION_DETALLE = "SELECT CB_LIQUIDACION_DETALLE_SQ.NextVal FROM DUAL";

	public static final String INSERT_MASIVO_LIQUIDACION_DETALLE = "insert into CB_LIQUIDACION_DETALLE (CBLIQUIDACIONDETALLEID, CBLIQUIDACIONID, TIPOVALOR, "
			+ "TIPOPAGO, TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL, SWCREATEBY, SWDATECREATED, CBESTADOCUENTAID) "
			+ "values(CB_LIQUIDACION_DETALLE_SQ.NextVal,?,?,?,?,?,?,?,sysdate, null)";

	public static final String INSERT_TIPO_VALOR_X = "insert into CB_LIQUIDACION_DETALLE (CBLIQUIDACIONDETALLEID, CBLIQUIDACIONID, TIPOVALOR, TIPOPAGO, "
			+ "TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL, SWCREATEBY, SWDATECREATED) values(?,?,?,?,?,?,?,?,sysdate)";

	public static final String UPDATE_TIPO_VALOR_X = "UPDATE CB_LIQUIDACION_DETALLE SET DESCTIPOTARJETA = ? WHERE CBLIQUIDACIONDETALLEID = ?";

	public static final String EXEC_CONCILIA_DEPOSITO_PRC = "{CALL CB_CONCILIACION_CAJAS_PKG.CB_DEPOSITOS_DETALLE_SP(?)}";

	public static final String EXEC_CONCILIA_CRED_UNICO_SP = "{CALL CB_CONCILIACION_CAJAS_PKG.CB_CONCILIA_CRED_UNICO_SP(?)}";

	public static final String CONS_DETALLE_LIQUIDACION_BY_ID = "SELECT CBLIQUIDACIONDETALLEID, TIPOVALOR, TIPOPAGO, TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL FROM "
			+ "CB_LIQUIDACION_DETALLE WHERE CBLIQUIDACIONID = ? ORDER BY TIPOVALOR";

	public static final String CONSULTA_REPORTE_LIQUIDACIONES = "SELECT NOMBTRANSACCION, to_char(FECHATRANSACCION,'DD-MM-YYYY') FechaTransaccion, TIPOVALOR, TIPOPAGO, "
			+ "TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL FROM CB_REPORTE_LIQUIDACIONES_VW ";

	public static final String ASOCIACION_MANUAL = "UPDATE CB_ESTADO_CUENTA SET CBCATALOGOAGENCIAID = ?, OBSERVACIONES = ? WHERE CBESTADOCUENTAID = ?";

	public static final String DESASOCIACION_MANUAL = "UPDATE CB_ESTADO_CUENTA SET CBCATALOGOAGENCIAID = null, OBSERVACIONES = null WHERE CBESTADOCUENTAID = ?";

	public static final String DESASOCIACION_TIPOLOGIA = "UPDATE CB_ESTADO_CUENTA_SOCIEDAD SET CBTIPOLOGIASPOLIZAID = null, "
			+ "CBCATALOGOAGENCIAID = null, FECHA_INGRESOS = null, OBSERVACIONES = null, "
			+ "SWMODIFYBY = ?, SWDATEMODIFIED = sysdate WHERE CBESTADOCUENTASOCIEDADID = ?";
	
	public static final String CONSULTA_ESTADO_CUENTA_TARJETA = "SELECT CBESTADOCUENTAID, TIPO_TARJETA, TO_CHAR(FECHA_TRANSACCION, 'DD/MM/YYYY') FECHA_TRANSACCION, "
			+ "AFILIACION, TIPO, REFERENCIA, LIQUIDO, COMISION, IVA_COMISION, RETENCION, CONSUMO, "
			+ "DECODE (NOMBRE, NULL, '(No asociada)', NOMBRE, NOMBRE) NOMBRE, OBSERVACIONES "
			+ "FROM CB_ESTADOS_TARJETA_VW WHERE 1=1 ";

	//Este query se invoca en el método generaConsultaReporte2() de la clase CBReportesDAO
	public static final String CONSULTA_REPORTE_X_ENTIDAD = " SELECT DISTINCT A.DIA fecha,"
			+ " TO_NUMBER(TO_CHAR(A.FECHA,'MM')) mes_1,"
			+ " DECODE(TO_NUMBER(TO_CHAR(TO_DATE(A.FECHA,'dd/MM/yyyy'),'MM')),1,'ENERO',2,'FEBRERO',3,'MARZO',4,'ABRIL',5,'MAYO',6,'JUNIO',7,'JULIO',8,'AGOSTO',9,'SEPTIEMBRE',10,'OCTUBRE',11,'NOVIEMBRE',12,'DICIEMBRE') mes_2,"
			+ " TO_CHAR(A.FECHA,'HH24:mi:ss') hora,"
			+ " NVL(A.MONTO,0) monto,"
			+ " A.TELEFONO telefono,"
			+ " B.NOMBRE banco,"
			+ " DECODE(A.TIPO, '1', 'PRE-PAGO', '2', 'POST-PAGO') tipo_servicio,"
			+ " A. TRANSACCION secuencia,"
			+ " C.NOMBRE agencia,"
			+ " DECODE(D.TIPO,0,'VIRTUAL',1,'PRESENCIAL','PRESENCIAL') forma_de_pago,"
			+ " NVL((A.COMISION*A.MONTO),0) comision,"
			+ " NVL(A.COMISION,0) porcentaje, " + " A.CODIGO sucursal, "
			+ " D.NOMBRE nombre_sucursal, " + " 1 cantidad"
			+ " FROM CB_DATA_BANCO A, " + " CB_CATALOGO_BANCO B, "
			+ " CB_CATALOGO_AGENCIA C, " + " CB_AGENCIA_VIR_FIS D, "
			+ " CB_BANCO_AGENCIA_CONFRONTA E, " + " CB_CONCILIACION F "
			+ " WHERE A.CBCATALOGOBANCOID = B.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOBANCOID   = C.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = C.CBCATALOGOAGENCIAID "
			+ " AND A.CODIGO              = D.CODIGO(+) "
			+ " AND A.CBCATALOGOBANCOID   = E.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = E.CBCATALOGOAGENCIAID "
			+ " AND A.CBDATABANCOID       = F.CBDATABANCOID "
			+ " AND A.DIA                 = F.DIA "
			+ " AND A.CBCATALOGOAGENCIAID = F.CBCATALOGOAGENCIAID "
			+ " AND A.CBCATALOGOAGENCIAID = D.CBCATALOGOAGENCIAID ";
	
	// este query es utilizado para consultar tipologias poliza modal
	public static final String CONSULTA_TIPOLOGIAS_POLIZA_MODAL_SQ = "SELECT CBTIPOLOGIASPOLIZAID,nombre, CENTRO_DE_BENEFICIO,DIVISION, ORDEN_DE_PROYECTO,  TIPO_DE_CAMBIO, FECHA_DE_CONVERSION, INDICADOR_CME,  "
			+ "  CAR_PA_SEGMENTO,CAR_PA_SERVICIO, CAR_PA_TIPO_TRAFICO, CAR_PA_AMBITO, CAR_PA_LICENCIA,CAR_PA_REGION, SUBTIPO_LINEA, CANAL, BUNDLE, PRODUCTO, EMPRESA_GRUPO   "
			+ " FROM CB_TIPOLOGIAS_POLIZA  WHERE 1=1 ";
	
	// este query es utilizado para modificar tipologias poliza modal
	public static final String MODIFICAR_POLIZA_MODAL_SQ = "UPDATE CB_TIPOLOGIAS_POLIZA SET CENTRO_DE_BENEFICIO = ?, "
			+ "  DIVISION = ?, ORDEN_DE_PROYECTO = ?,TIPO_DE_CAMBIO = ?, FECHA_DE_CONVERSION =?, INDICADOR_CME=?,CAR_PA_SEGMENTO = ?,CAR_PA_SERVICIO = ?,CAR_PA_TIPO_TRAFICO=? ,CAR_PA_AMBITO =? , CAR_PA_LICENCIA= ?, CAR_PA_REGION=? ,SUBTIPO_LINEA =?, CANAL = ?, BUNDLE =? , PRODUCTO = ?, EMPRESA_GRUPO = ?,  MODIFICADO_POR = ?, FECHA_MODIFICACION = SYSDATE WHERE CBTIPOLOGIASPOLIZAID = ?";

	// metodo para eliminar tipologia poliza
	public static final String DELETE_TIPOLOGIA_POLIZA_SQ = "DELETE FROM CB_TIPOLOGIAS_POLIZA WHERE CBTIPOLOGIASPOLIZAID = ?";

	// consulta vista contabilizacion
	public static final String CONSULTA_CONTABILIZACION_SQ = "SELECT CBCONTABILIZACIONID, CENTRO_COSTO,CLAVE_CONTABILIZACION, CUENTA,  REFERENCIA, TEXTO, TEXTO2,  "
			+ "  OBSERVACIONES, to_char(FECHA,'DD-MM-YYYY') FECHA, BANCO, AGENCIA, TERMINACION, NOMBRE,DECODE (TIPO,  '1', 'Ingreso',  '2', 'Contrapartida', '3', 'Diferencia') TIPO, DEBE / 100 , HABER, FECHA_CONTABILIZACION, TO_CHAR(FECHA_INGRESOS, 'DD-MM-YYYY') FECHA_INGRESOS , ESTADO, MODIFICADO_POR, cbcatalogoagenciaid "
			+ " FROM cb_poliza_contable_vw  WHERE 1=1  ";

	// este query es utilizado para modificar contabilizacion
	public static final String MODIFICAR_CONTABILIZACION_SQ = "UPDATE CB_CONTABILIZACION SET CENTRO_COSTO = ?, "
			+ "  CLAVE_CONTABILIZACION = ?, CUENTA = ?,REFERENCIA = ?, TEXTO =?, TEXTO2=?, OBSERVACIONES = ?  WHERE CBCONTABILIZACIONID = ?  ";

	// Se obtienen los datos que seran ingresados al archivo SAP para subir por
	// FTP
	public static final String OBTIENE_DATOS_SAP = "SELECT (SELECT valor_objeto1 "
			+ "          FROM cb_modulo_conciliacion_conf " + "         WHERE modulo = 'CONSULTA_CONTABILIZACION' "
			+ "           AND cbmoduloconciliacionconfid = 38) sociedad, "
			+ "       '000006' secuencia, '001' numero_de_asiento, "
			+ "       TO_CHAR (fecha_contabilizacion, 'YYYYMMDD') fecha_contabilizacion, "
			+ "       TO_CHAR (fecha, 'YYYYMMDD') fecha, texto, referencia, "
			+ "       '40' clave_contabilizacion, cuenta, centro_costo, ' ' centro_beneficio, "
			+ "       ' ' division, ' ' orden_de_proyecto, " + "       (SELECT valor_objeto1 "
			+ "          FROM cb_modulo_conciliacion_conf " + "         WHERE modulo = 'CONSULTA_CONTABILIZACION' "
			+ "           AND cbmoduloconciliacionconfid = 39) moneda, "
			+ "       ' ' tipo_de_cambio, ' ' fecha_conversion, debe importe, "
			+ "       texto2 texto_posicion, 'No' indicador_iva, ' ' indicador_cmd, "
			+ "       ' ' car_pa_segmento, ' ' car_pa_servicio, ' ' car_pa_tipo_trafico, "
			+ "       ' ' car_pa_ambito, ' ' car_pa_licencia, ' ' car_pa_region, " + "       (SELECT valor_objeto1 "
			+ "          FROM cb_modulo_conciliacion_conf " + "         WHERE modulo = 'CONSULTA_CONTABILIZACION' "
			+ "           AND cbmoduloconciliacionconfid = 40) clase_docto, "
			+ "       '96200040' actividad, ' ' subtipo_linea, ' ' canal, ' ' bundle, "
			+ "       ' ' producto, ' ' empresa_grupo " + "  FROM cb_poliza_contable_vw " + " WHERE 1 = 1 "
			+ "   AND fecha >= TO_DATE (?, 'dd/MM/yyyy') " + "   AND fecha <= TO_DATE (?, 'dd/MM/yyyy') ";

	//
	public static final String OBTIENE_DATOS_SAP2 = "SELECT CBCONTABILIZACIONID, (SELECT valor_objeto1 "
			+ "             FROM cb_modulo_conciliacion_conf "
			+ "            WHERE modulo = 'CONSULTA_CONTABILIZACION' "
			+ "              AND cbmoduloconciliacionconfid = 38) " + "       || RPAD (NVL (secuencia, ' '), 6, ' ')    "
			+ "       || LPAD (ROWNUM, 3, 0) " + "       || RPAD (NVL (TO_CHAR (fecha, 'YYYYMMDD'), ' '), 8, ' ') "
			+ "       || RPAD (NVL (TO_CHAR (fecha_ingresos, 'YYYYMMDD'), ' '), 8, ' ') "
			+ "       || RPAD (NVL (texto, ' '), 25, ' ') " + "       || RPAD (NVL (referencia, ' '), 16, ' ') "
			+ "       || RPAD (nvl(clave_contabilizacion, ' '), 2, ' ') " + "       || RPAD (nvl(cuenta,' '), 10, ' ') "
			+ "       || RPAD (nvl(centro_costo,' '), 10, ' ') " + "       || RPAD (NVL (CENTRO_DE_BENEFICIO, ' '), 10, ' ')    "
			+ "       || RPAD (NVL (division, ' '), 4, ' ') " 
			+ "       || RPAD (NVL (orden_de_proyecto, ' '), 12, ' ') "
			+ "       || (SELECT RPAD (NVL(moneda,' '),5,' ') FROM CB_CATALOGO_AGENCIA "
			+ "            WHERE CBCATALOGOAGENCIAID =cb_poliza_contable_vw.CBCATALOGOAGENCIAID ) " 
			+ "       || RPAD (NVL (tipo_de_cambio, ' '), 9, ' ')    "
			+ "       || RPAD (NVL (fecha_de_conversion, ' '), 8, ' ') " 
			+ " 	|| RPAD (NVL (TO_CHAR (haber), ' '), 12, ' ') "
			+ "       || RPAD (NVL (texto2, ' '), 50, ' ') " + "       || RPAD (nvl(indicador_iva, ' '), 2, ' ') "
			+ "       || RPAD (NVL (indicador_cme, ' '), 2, ' ')   " 
			+ "       || RPAD (NVL (car_pa_segmento, ' '), 2, ' ')   " 
			+ "       || RPAD (NVL (car_pa_servicio, ' '), 3, ' ')    "
			+ "       || RPAD (NVL (car_pa_tipo_trafico, ' '), 2, ' ')    " 
			+ "       || RPAD (NVL (car_pa_ambito, ' '), 2, ' ')   " 
			+ "       || RPAD (NVL (car_pa_licencia, ' '), 2, ' ')  "
			+ "       || RPAD (NVL (car_pa_region, ' '), 2, ' ')   " 
			+ "       || (SELECT RPAD (valor_objeto1, 2, ' ') "
			+ "             FROM cb_modulo_conciliacion_conf "
			+ "            WHERE modulo = 'CONSULTA_CONTABILIZACION' "
			+ "              AND cbmoduloconciliacionconfid = 40) " + "       || (RPAD (nvl(actividad, ' '), 10, ' ')) "
			+ "       || RPAD (NVL (subtipo_linea, ' '), 2, ' ')    " 
			+ "       || RPAD (NVL (canal, ' '), 2, ' ')    " 
			+ "       || RPAD (NVL (bundle, ' '), 2, ' ')   "
			+ "       || RPAD (NVL (producto, ' '), 4, ' ')    " 
			+ "       || RPAD (NVL (empresa_grupo, ' '), 4, ' ')sap " + "  FROM cb_poliza_contable_vw "
			+ " WHERE 1 = 1 ";
	
	public static final String OBTIENE_DATOS_SAP3 = "SELECT CBCONCILIACIONID, (SELECT RPAD (valor_objeto1, 21, '0') FROM cb_modulo_conciliacion_conf\r\n"
			+ " WHERE modulo = 'APLICA_DESAPLICA_PAGOS' AND TIPO_OBJETO = 'PARAMETRO_APLICA') "
			+ "       || RPAD (NVL (REPLACE (monto,'.',''), '0'), 9, '0')  "
			+ "       ||(SELECT RPAD (valor_objeto1, 4, '0') FROM cb_modulo_conciliacion_conf"
			+ "          WHERE modulo = 'APLICA_DESAPLICA_PAGOS' AND TIPO_OBJETO = 'PARAMETRO_APLICA2') "
			+ "       ||  LPAD (NVL (CLIENTE, '0'), 10, '0')    "
			+ "       ||(SELECT RPAD (valor_objeto1, 1, ' ') FROM cb_modulo_conciliacion_conf "
			+ "          WHERE modulo = 'APLICA_DESAPLICA_PAGOS' AND TIPO_OBJETO = 'PARAMETRO_APLICA3') "
			+ "       || RPAD (NVL (trans_telca, '0'), 9, '0')sap " + "  FROM cb_conciliacion_detail_vw "
			+ " 		WHERE 1 = 1 ";
	
	/**
	 * CBAgenciaComercialDAO
	 * */
	
	public static final String CONSULTA_LISTA_AGECOMERCIALPRE = "select COD_BANCO valorCodigo, DES_BANCO valorCaracter from "
			+ " ppga_bancos@LINK_PPGAGUA.SCLNUCLEOGT " + " order by des_banco ";
	
	public static final String CONSULTA_LISTA_AGECOMERCIALPOS = "select cod_caja valorCodigo, des_caja valorCaracter "
			+ " from co_cajas " + " where cod_oficina = 'NT' ";
	
	public static final String CONSULTA_POS_PRE = "SELECT tipo FROM cb_banco_agencia_confronta where cbbancoagenciaconfrontaid = ? ";
	
	public static final String INSERTA_DATA = "insert into CB_AGENCIAS_CONFRONTA "
			+ " (CBAGENCIASCONFRONTAID, CBBANCOAGENCIACONFRONTAID, COD_AGENCIA) "
			+ " values (CB_AGENCIAS_CONFRONTA_SQ.nextval,?,?)";

	public static final String VALIDAR_EXISTENCIA = "select cod_agencia "
			+ " from cb_agencias_confronta "
			+ " where cbbancoagenciaconfrontaid = ? " + " and cod_agencia = ?";
	
	public static final String ACTUALIZAR_AGENCIA_COMERCIAL = "UPDATE CB_AGENCIAS_CONFRONTA SET COD_AGENCIA = ? "
			+ "WHERE CBBANCOAGENCIACONFRONTAID = ? AND CBAGENCIASCONFRONTAID = ?";
	
	public static final String LISTADO_AGE_COM_PRE_POS = "SELECT CBAGENCIASCONFRONTAID idAgenciaComercial, COD_AGENCIA nombreAgenciaComercial "
			+ " FROM CB_AGENCIAS_CONFRONTA "
			+ " WHERE cbbancoagenciaconfrontaid = ? ";
	
	public static final String ELIMINAR_AGENCIA_COMERCIAL = " DELETE CB_AGENCIAS_CONFRONTA "
			+ " WHERE CBAGENCIASCONFRONTAID = ? ";
	
	public static final String ELIMINAR_TODAS_AGENCIA_COMERCIAL = " DELETE CB_AGENCIAS_CONFRONTA "
			+ " WHERE CBBANCOAGENCIACONFRONTAID = ? ";
	
	/**
	 * CBAgenciaVirFisDAO
	 * */
	
	public static final String MODIFICAR_AGENCIA = "UPDATE CB_AGENCIA_VIR_FIS " + " SET CODIGO = ?," + " NOMBRE = ?, "
			+ " TIPO = ?, " + " MODIFICADO_POR = ?, " + " FECHA_MODIFICADO  = SYSDATE "
			+ " WHERE CBAGENCIASVIRFISID = ?";
	
	public static final String ELIMINAR_AGENCIA = "DELETE CB_AGENCIA_VIR_FIS WHERE CBAGENCIASVIRFISID = ?";

	/**
	 * CBAplicaDesaplicaPagosDAO
	 * */
	
	public static final String OBTIENE_BANCO_SQ = "select cbcatalogobancoid, nombre " + "from cb_catalogo_banco "
			+ "where estado = 1 ";
	
	public static final String OBTIENE_AGENCIA_SQ = "select b.cbcatalogoagenciaid, b.nombre "
			+ "from cb_catalogo_banco a, cb_catalogo_agencia b " + "where a.cbcatalogobancoid = b.cbcatalogobancoid "
			+ "and b.estado = 1 " + "and a.estado = 1 ";
	
	public static final String OBTIENE_TIPO_SQ = "select b.cbcatalogoagenciaid, b.nombre "
			+ "from cb_catalogo_banco a, cb_catalogo_agencia b " + "where a.cbcatalogobancoid = b.cbcatalogobancoid "
			+ "and b.estado = 1 " + "and a.estado = 1 ";
	
	public static final String CONSULTA_ARCHIVOS_CARGADOS = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, " + "nombre, "
			+ "des_pago , " + "trans_telca, " + "telefono, " + "trans_banco , " + "imp_pago, " + "monto, " + "manual, "
			+ "pendiente, estado, cbconciliacionid, " + "sucursal, " + " comision, " + " NOMBRE_SUCURSAL, "
			+ " TIPO_SUCURSAL, " + " (monto*comision) monto_comision, cbcatalogoagenciaid "
			+ "FROM cb_conciliacion_detail_vw " + "WHERE 1 =1  ";
	
	public static final String QRY_OBTIENE_ESTADOS = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'APLICA_DESAPLICA_PAGO' AND TIPO_OBJETO = ? ";
	
	public static final String QRY_OBTIENE_TIPO = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'APLICA_DESAPLICA_PAGO' AND TIPO_OBJETO = ? ";
	
	/**
	 * CBArchivosInsertadosDAO
	 * */
	
	public static final String CONSULTA_ID_ARCHIVO = "SELECT NVL(MAX(ID_ARCHIVOS_INSERTADOS), 0) FROM cb_archivos_insertados  where NOMBRE_ARCHIVO = ? ";
	
	public static final String INSERTAR_ARCHIVOS = "INSERT "
			+ "INTO CB_ARCHIVOS_INSERTADOS (  ID_ARCHIVOS_INSERTADOS,  NOMBRE_ARCHIVO, "
			+ "    BANCO,   AGENCIA,  FECHA, CREADO_POR, FECHA_CREACION ) ";
	
	public static final String CONSULTA_ARCHIVOS_CARGADOS2 = "SELECT id_archivos_insertados idArchivosInsertados, nombre_archivo nombreArchivo, "
			+ " to_char(fecha, 'dd/MM/yyyy') fecha, "
			+ " (SELECT nombre FROM cb_catalogo_banco WHERE cbcatalogobancoid = banco "
			+ " ) banco, "
			+ " (SELECT nombre FROM cb_catalogo_agencia WHERE cbcatalogoagenciaid = agencia "
			+ " ) agencia, "
			+ " creado_por creadoPor "
			+ " FROM CB_ARCHIVOS_INSERTADOS where 1=1 ";
	
	public static final String CONSULTA_ARCHIVOS_CARGADOS_ESTADO_CUENTA ="SELECT CBESTADOCUENTAARCHIVOSID idArchivosInsertados, nombre nombreArchivo, B.OBJETO  TIPO,SWCREATEBY creadoPor, TO_CHAR (SWDATECREATED, 'dd/MM/yyyy') fecha" + 
			"  FROM CB_ESTADO_CUENTA_ARCHIVOS A, CB_MODULO_CONCILIACION_CONF B where 1=1 AND  A.CBESTADOCUENTACONFID = B.CBMODULOCONCILIACIONCONFID   ";
	
	public static final String CONSULTA_DETALLE_GRABADOS = "SELECT cod_cliente codCliente, "
			+ "telefono telefono, "
			+ "tipo tipo, "
			+ "to_char(fecha, 'dd/MM/yyyy') fecha, "
			+ "(SELECT b.nombre "
			+ "FROM cb_catalogo_banco b "
			+ "WHERE b.cbcatalogobancoid = d.cbcatalogobancoid "
			+ ") cBCatalogoBancoId, "
			+ "(SELECT a.nombre "
			+ "FROM cb_catalogo_agencia a "
			+ "WHERE a.cbcatalogoagenciaid = d.cbcatalogoagenciaid "
			+ ") cBCatalogoAgenciaId , "
			+ "monto monto, "
			+ "transaccion transaccion, "
			+ "estado estado, "
			+ "mes mes, "
			+ "texto1 texto1, "
			+ "texto2 texto2, "
			+ "creado_por creadoPor "
			+ "FROM cb_data_banco d " + "WHERE id_archivos_insertados = ?";
	
	public static final String CONSULTA_DETALLE_NO_GRABADOS = "SELECT nombre_archivo nombreArchivo, "
			+ "data_archivo dataArchivo, "
			+ "causa causa, "
			+ "estado estado, "
			+ "creado_por creadoPor "
			+ "FROM cb_data_sin_procesar " + "WHERE id_archivos_insertados = ?";

	public static final String DELETE_ARCHIVO_MAESTRO = "delete cb_archivos_insertados where id_archivos_insertados = ?";
	
	
	public static final String DELETE_ARCHIVO_MAESTRO_ESTADO_CUENTA = "delete CB_ESTADO_CUENTA_ARCHIVOS where CBESTADOCUENTAARCHIVOSID = ?";
	
	public static final String DELETE_REGISTROS_SOCIEDAD_ESTADO_CUENTA = "delete CB_ESTADO_CUENTA_SOCIEDAD where CBESTADOCUENTAARCHIVOSID = ?";

	public static final String DELETE_REGISTROS_CREDOMATIC_ESTADO_CUENTA = "delete CB_ESTADO_CUENTA where CBESTADOCUENTAARCHIVOSID = ?";

	public static final String DELETE_REGISTROS_OTRAS_ESTADO_CUENTA = "delete CB_ESTADO_CUENTA_DET where CBESTADOCUENTAARCHIVOSID = ?";

	
	public static final String DELETE_DATA_BANCO = "delete cb_data_banco where id_archivos_insertados = ?";
	
	public static final String BORRA_CONCILIACION_MAESTRO = "delete from cb_conciliacion where cbdatabancoid "
			+ "in(select cbdatabancoid from cb_data_banco where id_archivos_insertados = ?)";
	
	public static final String BORRA_DATA_SIN_PROCESAR = "delete from cb_data_sin_procesar where id_archivos_insertados = ?";
	
	public static final String ID_MAESTRO_CARGA = "SELECT CB_ARCHIVOS_INSERTADOS_SQ.NEXTVAL FROM DUAL";

	public static final String UPDATE_FECHA_ARCHIVO = "UPDATE CB_ARCHIVOS_INSERTADOS "
			+ " SET FECHA = to_date(?, ?) "
			+ " WHERE ID_ARCHIVOS_INSERTADOS = ?";
	
	/**
	 * CBBancoAgenciaAfiliacionesDAO
	 * */
	
	public static final String SEQ_AFILIACION = "SELECT CB_BANCO_AGENCIA_AF_SQ.NextVal FROM DUAL";
	
	public static final String AFILIACION_VALIDA = "SELECT CBBANCOAGENCIAAFILIACIONESID FROM CB_BANCO_AGENCIA_AFILIACIONES "
			+ "WHERE AFILIACION = ? AND CBCATALOGOAGENCIAID = ?";
	
	public static final String INSERTAR_AFILIACION = "insert into CB_BANCO_AGENCIA_AFILIACIONES (CBBANCOAGENCIAAFILIACIONESID, CBCATALOGOAGENCIAID, TIPO, "
			+ "AFILIACION, ESTADO, SWCREATEBY, SWDATECREATED, SWMODIFYBY, SWDATEMODIFIED) "
			+ "values(?,?,?,?,?,?,sysdate,null,null)";
	
	public static final String ACTUALIZAR_AFILIACION = "UPDATE CB_BANCO_AGENCIA_AFILIACIONES SET TIPO = ?, "
			+ "AFILIACION = ?, ESTADO = ?, SWMODIFYBY = ?, SWDATEMODIFIED = sysdate "
			+ "WHERE CBBANCOAGENCIAAFILIACIONESID = ?";
	
	public static final String ELIMINAR_AFILIACION = "DELETE FROM CB_BANCO_AGENCIA_AFILIACIONES WHERE CBBANCOAGENCIAAFILIACIONESID = ?";
	
	public static final String CONSULTAR_AFILIACIONES = "SELECT CBBANCOAGENCIAAFILIACIONESID, CBCATALOGOAGENCIAID,  "
			+ "DECODE (TIPO,  'BN', 'BANCO NACIONAL',  'CRED', 'CREDOMATIC', 'VISA', 'VISA') TIPO, "
			+ "AFILIACION, ESTADO, SWCREATEBY, SWDATECREATED FROM CB_BANCO_AGENCIA_AFILIACIONES "
			+ "WHERE CBCATALOGOAGENCIAID = ? ORDER BY CBBANCOAGENCIAAFILIACIONESID";
	
	
	
	/**
	 * CBBancoAgenciaCajasDAO
	 * */
	
	public static final String SEQ_CAJERO = "SELECT CB_BANCO_AGENCIA_CAJAS_SQ.NextVal FROM DUAL";
	
	public static final String CAJERO_VALIDO = "SELECT CBBANCOAGENCIACAJASID FROM CB_BANCO_AGENCIA_CAJAS "
			+ "WHERE CAJERO = ? AND CBCATALOGOAGENCIAID = ?";
	
	public static final String INSERTAR_CAJERO = "insert into CB_BANCO_AGENCIA_CAJAS (CBBANCOAGENCIACAJASID, CBCATALOGOAGENCIAID, "
			+ "COD_OFICINA, COD_CAJA, CAJERO, ESTADO, SWCREATEBY, SWDATECREATED, SWMODIFYBY, SWDATEMODIFIED) values(?,?,?,?,?,?,?,sysdate,null,null)";
	
	
	public static final String ACTUALIZAR_CAJERO = "UPDATE CB_BANCO_AGENCIA_CAJAS SET COD_OFICINA = ?, COD_CAJA = ?, "
			+ "CAJERO = ?, ESTADO = ?, SWMODIFYBY = ?, SWDATEMODIFIED = sysdate WHERE CBBANCOAGENCIACAJASID = ?";

	public static final String ELIMINAR_CAJERO = "DELETE FROM CB_BANCO_AGENCIA_CAJAS WHERE CBBANCOAGENCIACAJASID = ?";
	
	public static final String CONSULTAR_CAJEROS = "SELECT CBBANCOAGENCIACAJASID, CBCATALOGOAGENCIAID, COD_OFICINA, COD_CAJA, "
			+ "CAJERO, ESTADO, SWCREATEBY, SWDATECREATED FROM CB_BANCO_AGENCIA_CAJAS "
			+ "WHERE CBCATALOGOAGENCIAID = ? ORDER BY CBBANCOAGENCIACAJASID";

	/**
	 * CBBancoAgenciaConfrontaDAO
	 * */
	//el campo comision se ocupa para el campo aproximacion
	public static final String INSERTAR_BANCO_AGE_CONF = "INSERT INTO CB_BANCO_AGENCIA_CONFRONTA"
			+ "(CBBANCOAGENCIACONFRONTAID, CBCATALOGOBANCOID, CBCATALOGOAGENCIAID, CBCONFIGURACIONCONFRONTAID, "
			+ "TIPO, PATHFTP, ESTADO, ID_CONEXION_CONF, CREADO_POR, FECHA_CREACION, PALABRA_ARCHIVO, "
			+ "CANTIDAD_AJUSTES, DESCARTAR_TRANSACCION,  COMISION, AFILIACION, CBAGENCIASCONFRONTAID , CONFRONTA_SOCIA) "
			+ "VALUES(CB_BANCO_AGENCIA_CONFRONTA_SQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, 0, ?, ? )";

	public static final String CONSULTA_BANC_AGE_CONFRONTA_ASOCIACION = "SELECT DISTINCT cbbancoagenciaconfrontaid cbBancoAgenciaConfrontaId, pathftp pathFtp, "
			+ "estado estado, tipo tipo, cbconfiguracionconfrontaid cBConfiguracionConfrontaId, id_conexion_conf idConexionConf, "
			+ "palabra_archivo palabraArchivo, cbagenciasconfrontaid cBAgenciasConfrontaId, "
			+ "(SELECT a.nombre FROM cb_configuracion_confronta a, cb_banco_agencia_confronta b WHERE "
			+ "a.cbconfiguracionconfrontaid = b.cbconfiguracionconfrontaid  AND b.cbbancoagenciaconfrontaid = a.cbagenciasconfrontaid) confrontaPadre, "
			+ "(SELECT COUNT (*) FROM CB_BANCO_AGENCIA_CONFRONTA x WHERE x.CBAGENCIASCONFRONTAID = a.cbbancoagenciaconfrontaid) confrontasDependientes, "
			+ "(SELECT CBCONFIGURACIONCONFRONTAID FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) cBConfiguracionConfrontaId, "
			+ "(SELECT nombre FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) nombre, (SELECT delimitador1 FROM cb_configuracion_confronta "
			+ "WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) delimitador1, "
			+ "(SELECT delimitador2 FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) delimitador2, "
			+ "(SELECT nomenclatura FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) nomenclatura, "
			+ "(SELECT nombre_conexion FROM cb_conexion_conf WHERE id_conexion_conf = A.id_conexion_conf) nombreConexion, cantidad_ajustes cantidadAjustes, "
			+ "descartar_transaccion descartarTransaccion, CONFRONTA_SOCIA confrontasocia, comision aproximacion, afiliacion afiliacion FROM CB_BANCO_AGENCIA_CONFRONTA A ";
	
	public static final String CONSULTA_BANC_AGE_CONFRONTA = "SELECT DISTINCT cbbancoagenciaconfrontaid cbBancoAgenciaConfrontaId, pathftp pathFtp, "
			+ "estado estado, tipo tipo, cbconfiguracionconfrontaid cBConfiguracionConfrontaId, id_conexion_conf idConexionConf, "
			+ "palabra_archivo palabraArchivo, cbagenciasconfrontaid cBAgenciasConfrontaId, "
			+ "(SELECT a.nombre FROM cb_configuracion_confronta a, cb_banco_agencia_confronta b WHERE "
			+ "a.cbconfiguracionconfrontaid = b.cbconfiguracionconfrontaid  AND b.cbbancoagenciaconfrontaid = a.cbagenciasconfrontaid) confrontaPadre, "
			+ "(SELECT COUNT (*) FROM CB_BANCO_AGENCIA_CONFRONTA x WHERE x.CBAGENCIASCONFRONTAID = a.cbbancoagenciaconfrontaid) confrontasDependientes, "
			+ "(SELECT CBCONFIGURACIONCONFRONTAID FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) cBConfiguracionConfrontaId, "
			+ "(SELECT nombre FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) nombre, (SELECT delimitador1 FROM cb_configuracion_confronta "
			+ "WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) delimitador1, "
			+ "(SELECT delimitador2 FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) delimitador2, "
			+ "(SELECT nomenclatura FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) nomenclatura, "
			+ "(SELECT nombre_conexion FROM cb_conexion_conf WHERE id_conexion_conf = A.id_conexion_conf) nombreConexion, cantidad_ajustes cantidadAjustes, "
			+ "descartar_transaccion descartarTransaccion, comision comision, afiliacion afiliacion FROM CB_BANCO_AGENCIA_CONFRONTA A ";
	
	public static final String CONSULTA_BANC_AGE_CONFRONTA_BKP = "SELECT DISTINCT cbbancoagenciaconfrontaid cbBancoAgenciaConfrontaId, " 
			+ "(SELECT nombre FROM cb_configuracion_confronta WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) nombre, (SELECT delimitador1 FROM cb_configuracion_confronta "
					+ " WHERE CBCONFIGURACIONCONFRONTAID = A.CBCONFIGURACIONCONFRONTAID) delimitador1 " 
			+" FROM CB_BANCO_AGENCIA_CONFRONTA A "
			+ "WHERE cbcatalogobancoid = ? AND cbcatalogoagenciaid = ?";
	
	public static final String ACTUALIZA_BANCO_AGE_CONF = "UPDATE cb_banco_agencia_confronta SET cbconfiguracionconfrontaid  = ?, "
			+ "estado = ?,  tipo = ?, pathftp = ?, modificado_por = ?, fecha_modificacion = sysdate, id_conexion_conf = ? , "
			+ "palabra_archivo = ?, cantidad_ajustes = ?, descartar_transaccion = ?, comision = ?,  "
			+ "cbagenciasconfrontaid = ? , CONFRONTA_SOCIA = ? WHERE cbbancoagenciaconfrontaid = ? ";
	
	public static final String ELIMINA_BANCO_AGE_CONF = "delete cb_banco_agencia_confronta where cbbancoagenciaconfrontaid = ?";
	
	public static final String QRY_OBTIENE_ESTADOS_COMISION = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE UPPER(MODULO) = UPPER('CONFIGURACION_CONFRONTA') "
			+ "AND UPPER(TIPO_OBJETO) =  UPPER('ESTADO_COMISION') " +
			"   AND UPPER(ESTADO) = UPPER ('S')";

	/**
	 * CBCatalogoAgenciaDAO
	 * */
	
	//Query para el catalogo de entidades
	public static final String CONSULTA_AGENCIAS = "SELECT a.CBCATALOGOAGENCIAID cBCatalogoAgenciaId, a.CBCATALOGOBANCOID cBCatalogoBancoId, "
			+ "b.NOMBRE nombreBanco, a.NOMBRE nombre, a.TELEFONO telefono, a.DIRECCION direccion, "
			+ "(SELECT c.OBJETO FROM CB_MODULO_CONCILIACION_CONF c WHERE UPPER (c.MODULO) = UPPER ('CONFIGURACION_ENTIDADES') "
			+ "AND UPPER (c.TIPO_OBJETO) = UPPER ('ESTADO') AND UPPER (c.ESTADO) = UPPER ('S') "
			+ "AND TO_CHAR (a.ESTADO) = c.VALOR_OBJETO1) estadoTxt, a.CREADO_POR creadoPor, a.FECHA_CREACION fechaCreacion, "
			+ "NVL(a.cuenta_contable, '') cuentaContable, a.estado estado, a.moneda MONEDA , a.codigo_colector CODIGO_COLECTOR, a.nit NIT FROM CB_CATALOGO_AGENCIA a, CB_CATALOGO_BANCO b "
			+ "WHERE b.CBCATALOGOBANCOID = a.CBCATALOGOBANCOID ";
	
	// Query para la carga de confrontas
	public static final String CONSULTA_AGENCIAS2 = "SELECT DISTINCT a.cbcatalogoagenciaid cbcatalogoagenciaid, " + 
			"                a.cbcatalogobancoid cbcatalogobancoid, b.nombre nombrebanco, " + 
			"                a.nombre nombre, a.telefono telefono, a.direccion direccion, " + 
			"                (SELECT nombre " + 
			"                   FROM cb_catalogo_opcion b " + 
			"                  WHERE tipo = 'ESTADO' AND a.estado = b.valor) estado, " + 
			"                a.creado_por creadopor, a.fecha_creacion fechacreacion " + 
			"           FROM cb_catalogo_agencia a, " + 
			"                cb_catalogo_banco b, " + 
			"                cb_banco_agencia_confronta c " + 
			"          WHERE a.cbcatalogobancoid = b.cbcatalogobancoid " + 
			"            AND a.cbcatalogoagenciaid = c.cbcatalogoagenciaid AND a.estado = 1 ";
	
	public static final String INSERTA_AGENCIA = "INSERT INTO cb_catalogo_agencia (cbcatalogoagenciaid, cbcatalogobancoid, "
			+ "nombre, telefono, direccion, estado, creado_por, cuenta_contable, fecha_creacion, moneda, codigo_colector, nit ) "
			+ "VALUES (CB_CATALOGO_AGENCIA_SQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?,  SYSDATE, ? , ?, ? )";

	public static final String ACTUALIZA_AGENCIA = "UPDATE CB_CATALOGO_AGENCIA "
			+ " SET cbcatalogobancoid     = ?, "
			+ "   nombre                  = ?, "
			+ "   telefono                = ?, "
			+ "   direccion               = ?, "
			+ "   estado                  = ?, "
			+ "   modificado_por          = ?, "
			+ "   fecha_modificacion      = sysdate, "
			+ "   cuenta_contable         = ? ,"
			+ "   moneda                  = ? ,"
			+ "   codigo_colector         = ? , "
			+ "   nit         = ? "
			+ " WHERE cbcatalogoagenciaid = ?";
	
	public static final String QRY_UPDATE_ASOCIACIONES_ENTIDAD = "UPDATE CB_BANCO_AGENCIA_CONFRONTA SET CBCATALOGOBANCOID = ? "
			+ "WHERE CBCATALOGOAGENCIAID = ?";
	
	public static final String CONSULTA_NOMBRE_AGENCIA = "SELECT count(*) valor  FROM CB_CATALOGO_AGENCIA WHERE trim(upper(NOMBRE)) = trim(upper(?))";

	//Agregado por CarlosGodinez - para el modulo de reporteria - Qitcorp - 14/03/2017
	public static final String OBTIENE_AGENCIA_SQ_R = "select b.cbcatalogoagenciaid, b.nombre " + 
			"from cb_catalogo_banco a, cb_catalogo_agencia b " + 
			"where a.cbcatalogobancoid = b.cbcatalogobancoid " + 
			"and a.tipo_entidad = 'NO FINANCIERA' " + 
			"and b.estado = 1 " + 
			"and a.estado = 1 ";
	
	public static final String CONSULTA_AGRUPACIONES = "SELECT CBCATALOGOBANCOID, NOMBRE FROM CB_CATALOGO_BANCO";
	
	public static final String QRY_OBTIENE_ESTADOS2 = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE UPPER(MODULO) = UPPER('CONFIGURACION_ENTIDADES') "
			+ "AND UPPER(TIPO_OBJETO) =  UPPER('ESTADO') " +
			"   AND UPPER(ESTADO) = UPPER ('S')";
	
	/**
	 * CBCatalogoBancoDAO
	 * */
	
	public static final String CONSULTA_BANCO = "SELECT DISTINCT a.CBCATALOGOBANCOID cbcatalogobancoid, "
			+ "a.NOMBRE nombre, CONTACTO contacto, a.TELEFONO telefono, EXTENSION extension,"
			+ "(SELECT nombre FROM cb_catalogo_opcion b "
			+ "WHERE TIPO = 'ESTADO' AND a.ESTADO = b.valor) estado, TIPO_ENTIDAD tipoEstado, "
			+ "a.CREADO_POR creadoPor, a.FECHA_CREACION fechaCreacion, a.MODIFICADO_POR modificadoPor,"
			+ "a.FECHA_MODIFICACION fechaModificacion FROM CB_CATALOGO_BANCO a, "
			+ "cb_catalogo_agencia b, cb_banco_agencia_confronta c "
			+ "WHERE  a.cbcatalogobancoid = b.cbcatalogobancoid "
			+ "AND b.cbcatalogoagenciaid = c.cbcatalogoagenciaid ";
	
	public static final String CONSULTA_BANCO2 = 
			"SELECT cbcatalogobancoid,nombre,contacto, telefono, extension extension, " + 
			"                (SELECT nombre " + 
			"                   FROM cb_catalogo_opcion b " + 
			"                  WHERE tipo = 'ESTADO' AND c.estado = b.valor) estado, " + 
			"                tipo_entidad tipoestado, creado_por creadopor, " + 
			"                fecha_creacion fechacreacion, " + 
			"                modificado_por modificadopor, " + 
			"                fecha_modificacion fechamodificacion from cb_catalogo_banco c where 1=1 ";
	
	public static final String INSERTA_CATALOGO_BANCO = "INSERT "
			+ " INTO cb_catalogo_banco "
			+ "   ( "
			+ "     CBCATALOGOBANCOID, "
			+ "     NOMBRE, "
			+ "     TELEFONO, "
			+ "     CONTACTO, "
			+ "     EXTENSION, "
			+ "     ESTADO,TIPO_ENTIDAD ,  "
			+ "     CREADO_POR, "
			+ "     FECHA_CREACION "
			+ "   ) "
			+ "   VALUES "
			+ "   ( "
			+ "    (SELECT NVL(MAX(CBCATALOGOBANCOID), 0)+1 FROM cb_catalogo_banco "
			+ "     ) " + "     , " + "     ?, " + "     ?, " + "     ?, "
			+ "     ?, " + "     ?, " + "     ?, ?,  " + "     SYSDATE "
			+ "   ) ";
	
	public static final String ACTUALIZA_CATALOGO_BANCO = "UPDATE cb_catalogo_banco "
			+ " SET nombre              = ?, "
			+ "   contacto              = ?, "
			+ "   telefono              = ?, "
			+ "   extension             = ?, "
			+ "   estado                = ?, "
			+ "   tipo_entidad          = ?, "
			+ "   modificado_por        = ?, "
			+ "   fecha_modificacion    = sysdate "
			+ " WHERE CBCATALOGOBANCOID = ? ";
	
	//agrega ovidio
	public static final String CONSULTA_BANCOS_MODIFICADO = "SELECT CBCATALOGOBANCOID cbcatalogobancoid,NOMBRE nombre, CONTACTO contacto,"
			+ " TELEFONO telefono, EXTENSION extension, (SELECT nombre FROM cb_catalogo_opcion b WHERE TIPO   ='ESTADO'"
			+ " AND a.ESTADO = b.valor ) estado, TIPO_ENTIDAD tipoEstado, CREADO_POR creadoPor, "
			+ " FECHA_CREACION fechaCreacion, MODIFICADO_POR modificadoPor, FECHA_MODIFICACION fechaModificacion "
			+ " FROM CB_CATALOGO_BANCO a ";
	
	//Agregado por CarlosGodinez - para el modulo de reporteria - Qitcorp - 14/03/2017
	public static final String OBTIENE_ENTIDAD_SQ = "select cbcatalogobancoid, nombre from cb_catalogo_banco "
			+ "where estado = 1 and tipo_entidad = 'NO FINANCIERA' order by nombre";
	
	public static final String CONSULTA_NOMBRE_ENTIDAD = "SELECT count(*) valor  FROM CB_CATALOGO_BANCO WHERE trim(upper(NOMBRE)) = trim(upper(?))";

	/**
	 * CBCatalogoOpcionDAO
	 * */
	
	public static final String CONSULTA_OPCIONES = "select nombre, valor valor, estado estado from CB_CATALOGO_OPCION where tipo = 'ESTADO' ";

	/**
	 * CBCausasConciliacionDAO
	 * 
	 * Estandarizar a una DAO con CBCausasDao
	 * */
	
	public static final String LISTADO_ACCIONES = "select * from cb_causas_conciliacion";
	
	/**
	 * CBCausasDao
	 * */
	
	public static final String INSERTA_CAUSA = "INSERT " + "INTO cb_causas_conciliacion " + "  ( " + "    id_causas_conciliacion, "
			+ "    causas, " + "    creado_por, " + "    fecha_creacion " + "  ) " + "  values " + "  ( "
			+ "    cb_causa_sq.nextval, " + "    ?, " + "    ?, " + "    sysdate " + "  ) ";
	
	public static final String CONSULTA_CAUSAS = "SELECT id_causas_conciliacion idCausaConciliacion, " + "causas tipoCausa, "
			+ "creado_por creadoPor, " + "to_char(fecha_creacion, 'dd/MM/yyyy') fechaCreacion, "
			+ "modificado_por modificadoPor, " + "to_char(fecha_modificacion, 'dd/MM/yyyy') fechaModificacion "
			+ "FROM cb_causas_conciliacion where 1 = 1 ";
	
	public static final String ACTUALIZA_CAUSA = "update cb_causas_conciliacion set causas = ?, modificado_por = ?, fecha_modificacion = sysdate where id_causas_conciliacion = ?";

	public static final String BORRA_CAUSA = "delete cb_causas_conciliacion where id_causas_conciliacion = ? ";
	
	/**
	 * CBConciliacionBancoDAO
	 * */
	
	public static final String OBTIENE_ENTIDAD_SQ2 = "select cbcatalogobancoid, nombre from cb_catalogo_banco "
			+ "where estado = 1 and tipo_entidad = 'NO FINANCIERA' ";
	
	public static final String OBTIENE_BANCO_SQ2 = "select cbcatalogobancoid, nombre " + 
			"from cb_catalogo_banco " + 
			"where estado = 1 " + 
			"and tipo_entidad = 'NO FINANCIERA'";
	
	/**
	 * CBConciliacionCajasDAO
	 * */
	
	public static final String OBTIENE_ENTIDAD_SQ3 = "select cbcatalogobancoid, nombre from cb_catalogo_banco "
			+ "where estado = 1 and tipo_entidad = 'NO FINANCIERA'";

	public static final String OBTIENE_BANCO_SQ3 = "select cbcatalogobancoid, nombre " + 
			"from cb_catalogo_banco " + 
			"where estado = 1 " + 
			"and tipo_entidad = 'NO FINANCIERA'";
	
	/**
	 * CBConciliacionDAO
	 * */
	
	public static final String CONSULTA_PARAMETROS = "SELECT descripcion_parametro valorCaracter, "
			+ " codigo_parametro valorCodigo " + " FROM CB_PARAMETROS";
	
	/**
	 * CBConciliacionDetalleDAO
	 * */
	
	/**
	 * TRABAJAR QUERY DE MANERA PARAMETRIZABLE
	 * obtenerConciliacionDetalladas()
	 * 
	public static final String CONCILIACION_DETAIL = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, "
			+ "nombre, " + "des_pago desPago, "
			+ "trans_telca transTelca, " + "telefono, "
			+ "trans_banco transBanco, " + "imp_pago impPago, "
			+ "monto, " + "manual, "
			+ "pendiente, estado, cbconciliacionid conciliacionId "
			+ "FROM cb_conciliacion_detail_vw "
			+ "WHERE dia               = to_date('" + fecha
			+ "', 'dd/MM/yyyy') " + "AND cbcatalogoagenciaid = '" + num
			+ "' " + "AND tipo_id             = '" + tipo + "' ";
	*/
	
	/**
	 * TRABAJAR QUERY DE MANERA PARAMETRIZABLE
	 * obtenerConciliacionDetalladasFiltros
	 * 
	 
	public static final String QRY_BASE_CONCILIACION = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, "
					+ "nombre, " + "des_pago desPago, "
					+ "trans_telca transTelca, " + "telefono, "
					+ "trans_banco transBanco, " + "imp_pago impPago, "
					+ "monto, " + "manual, "
					+ "pendiente, estado, cbconciliacionid conciliacionId, "
					+ "sucursal, " 
					+ " comision, "
					+ " NOMBRE_SUCURSAL, " 
					+ " TIPO_SUCURSAL, "
					+ " (monto*comision) monto_comision "
					+ "FROM cb_conciliacion_detail_vw a "
					+ "WHERE dia  = to_date('" + fecha
					+ "', 'dd/MM/yyyy')  AND tipo_id =  ?";
	 
	public static final String QRY_BASE_CONCILIACION2 = "SELECT agencia, "
			+ "dia, "
			+ "tipo, "
			+ "cliente, "
			+ "nombre, "
			+ "des_pago desPago, "
			+ "trans_telca transTelca, "
			+ "telefono, "
			+ "trans_banco transBanco, "
			+ "imp_pago impPago, "
			+ "monto, "
			+ "manual, "
			+ "pendiente, estado, cbconciliacionid conciliacionId, "
			+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision "
			+ "FROM cb_conciliacion_detail_p_vw "
			+ "WHERE dia  = to_date('" + fecha
			+ "', 'dd/MM/yyyy')  AND tipo_id =  ?";
	*/
	
	/**
	 * TRABAJAR QUERYS DE MANERA PARAMETRIZABLE
	 * obtenerConciliacionDetalladasFiltrosReportes
	 * 
	public static final String QRY_CONCILIA_REPORTE1 = "SELECT agencia, "
			+ "dia, "
			+ "tipo, "
			+ "cliente, "
			+ "nombre, "
			+ "des_pago desPago, "
			+ "trans_telca transTelca, "
			+ "telefono, "
			+ "trans_banco transBanco, "
			+ "imp_pago impPago, "
			+ "monto, "
			+ "manual, "
			+ "NVL(CASE WHEN monto > 0 AND DES_PAGO IS NULL THEN monto - manual WHEN monto = 0 AND DES_PAGO IS NULL THEN 0 WHEN monto = imp_pago THEN 0 END, 0) pendienteT, NVL(CASE WHEN imp_pago > 0 AND TRANS_BANCO IS NULL THEN imp_pago - manual WHEN imp_pago = 0 AND TRANS_BANCO IS NULL THEN 0 WHEN imp_pago = monto THEN 0 END, 0) pendienteB, estado, cbconciliacionid conciliacionId, sucursal, comision, nombre_sucursal, tipo_sucursal, nvl(monto*comision, 0) "
			+ "FROM cb_conciliacion_detail_vw "
			+ "WHERE dia  = to_date('" + fecha
			+ "', 'dd/MM/yyyy')  AND tipo_id =  ?";
	
	public static final String QRY_CONCILIA_REPORTE2 = "SELECT agencia, "
						+ "dia, "
						+ "tipo, "
						+ "cliente, "
						+ "nombre, "
						+ "des_pago desPago, "
						+ "trans_telca transTelca, "
						+ "telefono, "
						+ "trans_banco transBanco, "
						+ "imp_pago impPago, "
						+ "monto, "
						+ "manual, "
						+ "NVL(CASE WHEN monto > 0 AND DES_PAGO IS NULL THEN monto - manual WHEN monto = 0 AND DES_PAGO IS NULL THEN 0 WHEN monto = imp_pago THEN 0 END, 0) pendienteT, NVL(CASE WHEN imp_pago > 0 AND TRANS_BANCO IS NULL THEN imp_pago - manual WHEN imp_pago = 0 AND TRANS_BANCO IS NULL THEN 0 WHEN imp_pago = monto THEN 0 END, 0) pendienteB, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision "
						+ "FROM cb_conciliacion_detail_p_vw "
						+ "WHERE dia  = to_date('" + fecha
						+ "', 'dd/MM/yyyy')  AND tipo_id =  ?";
						
	public static final String QRY_CONCILIA_REPORTE3 = "SELECT agencia, "
						+ "dia, "
						+ "tipo, "
						+ "cliente, "
						+ "nombre, "
						+ "des_pago desPago, "
						+ "trans_telca transTelca, "
						+ "telefono, "
						+ "trans_banco transBanco, "
						+ "imp_pago impPago, "
						+ "monto, "
						+ "manual, "
						+ "NVL(CASE WHEN monto > 0 AND DES_PAGO IS NULL THEN monto - manual WHEN monto = 0 AND DES_PAGO IS NULL THEN 0 WHEN monto = imp_pago THEN 0 END, 0) pendienteT, NVL(CASE WHEN imp_pago > 0 AND TRANS_BANCO IS NULL THEN imp_pago - manual WHEN imp_pago = 0 AND TRANS_BANCO IS NULL THEN 0 WHEN imp_pago = monto THEN 0 END, 0) pendienteB, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision "
						+ "FROM cb_conciliacion_detail_p_vw "
						+ "WHERE dia  = to_date('" + fecha
						+ "', 'dd/MM/yyyy')  AND tipo_id =  ?"
						+ " and ((ABS(imp_pago) - ABS(monto) - ABS(manual) = 0 and ABS(manual) > 0) "
						+ "or (ABS(monto) - ABS(imp_pago) + ABS(manual) > 0 and ABS(manual) > 0 )) "
						+ " AND (accion != 'AJUSTE DEBITO (TRANS TELEFONICA) AUTO') "
						+ " AND (accion != 'AJUSTE CREDITO (TRANS BANCO) AUTO')"
						+ " AND (accion                    != 'DIFERENCIA_FECHAS')"
						+ " AND (accion                    != 'NO_APLICA')"
						+ " AND (accion 				   != 'AJUSTE DEBITO (TRANS TELEFONICA) DIF_FECHAS')";
		
	public static final String QRY_CONCILIA_REPORTE4 = "SELECT agencia, "
						+ "dia, "
						+ "tipo, "
						+ "cliente, "
						+ "nombre, "
						+ "des_pago desPago, "
						+ "trans_telca transTelca, "
						+ "telefono, "
						+ "trans_banco transBanco, "
						+ "imp_pago impPago, "
						+ "monto, "
						+ "manual, "
						+ "NVL(CASE WHEN monto > 0 AND DES_PAGO IS NULL THEN monto - manual WHEN monto = 0 AND DES_PAGO IS NULL THEN 0 WHEN monto = imp_pago THEN 0 END, 0) pendienteT, NVL(CASE WHEN imp_pago > 0 AND TRANS_BANCO IS NULL THEN imp_pago - manual WHEN imp_pago = 0 AND TRANS_BANCO IS NULL THEN 0 WHEN imp_pago = monto THEN 0 END, 0) pendienteB, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision "
						+ "FROM cb_conciliacion_detail_p_vw "
						+ "WHERE dia  = to_date('" + fecha
						+ "', 'dd/MM/yyyy')  AND tipo_id =  ?"
						+ " and ((ABS(imp_pago) - ABS(monto) - ABS(manual) = 0 and ABS(manual) > 0) "
						+ "or (ABS(monto) - ABS(imp_pago) + ABS(manual) > 0 and ABS(manual) > 0 )) and (accion = 'AJUSTE DEBITO (TRANS TELEFONICA) AUTO' or accion = 'AJUSTE CREDITO (TRANS BANCO) AUTO') ";
	
	public static final String QRY_CONCILIA_REPORTE5 = "SELECT agencia, "
						+ "dia, "
						+ "tipo, "
						+ "cliente, "
						+ "nombre, "
						+ "des_pago desPago, "
						+ "trans_telca transTelca, "
						+ "telefono, "
						+ "trans_banco transBanco, "
						+ "imp_pago impPago, "
						+ "monto, "
						+ "manual, "
						+ "NVL(CASE WHEN monto > 0 AND DES_PAGO IS NULL THEN monto - manual WHEN monto = 0 AND DES_PAGO IS NULL THEN 0 WHEN monto = imp_pago THEN 0 END, 0) pendienteT, NVL(CASE WHEN imp_pago > 0 AND TRANS_BANCO IS NULL THEN imp_pago - manual WHEN imp_pago = 0 AND TRANS_BANCO IS NULL THEN 0 WHEN imp_pago = monto THEN 0 END, 0) pendienteB, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision "
						+ "FROM cb_conciliacion_detail_p_vw "
						+ "WHERE dia  = to_date('"
						+ fecha
						+ "', 'dd/MM/yyyy')  AND tipo_id =  ? and accion = 'DIFERENCIA_FECHAS' ";
						
	public static final String QRY_CONCILIA_REPORTE6 = "SELECT agencia, "
						+ "dia, "
						+ "tipo, "
						+ "cliente, "
						+ "nombre, "
						+ "des_pago desPago, "
						+ "trans_telca transTelca, "
						+ "telefono, "
						+ "trans_banco transBanco, "
						+ "imp_pago impPago, "
						+ "monto, "
						+ "manual, "
						+ "NVL(CASE WHEN monto > 0 AND DES_PAGO IS NULL THEN monto - manual WHEN monto = 0 AND DES_PAGO IS NULL THEN 0 WHEN monto = imp_pago THEN 0 END, 0) pendienteT, NVL(CASE WHEN imp_pago > 0 AND TRANS_BANCO IS NULL THEN imp_pago - manual WHEN imp_pago = 0 AND TRANS_BANCO IS NULL THEN 0 WHEN imp_pago = monto THEN 0 END, 0) pendienteB, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision "
						+ "FROM cb_conciliacion_detail_p_vw "
						+ "WHERE dia  = to_date('"
						+ fecha
						+ "', 'dd/MM/yyyy')  AND tipo_id =  ? and accion = 'AJUSTE DEBITO (TRANS TELEFONICA) DIF_FECHAS' ";
	
	public static final String QRY_CONCILIA_REPORTE7 = "SELECT agencia, "
						+ "dia, "
						+ "tipo, "
						+ "cliente, "
						+ "nombre, "
						+ "des_pago desPago, "
						+ "trans_telca transTelca, "
						+ "telefono, "
						+ "trans_banco transBanco, "
						+ "imp_pago impPago, "
						+ "monto, "
						+ "manual, "
						+ "NVL(CASE WHEN monto > 0 AND DES_PAGO IS NULL THEN monto - manual WHEN monto = 0 AND DES_PAGO IS NULL THEN 0 WHEN monto = imp_pago THEN 0 END, 0) pendienteT, NVL(CASE WHEN imp_pago > 0 AND TRANS_BANCO IS NULL THEN imp_pago - manual WHEN imp_pago = 0 AND TRANS_BANCO IS NULL THEN 0 WHEN imp_pago = monto THEN 0 END, 0) pendienteB, estado, cbconciliacionid conciliacionId, "
						+ "sucursal, comision, nombre_sucursal, tipo_sucursal, (monto*comision) monto_comision "
						+ "FROM cb_conciliacion_detail_p_vw "
						+ "WHERE dia  = to_date('"
						+ fecha
						+ "', 'dd/MM/yyyy')  AND tipo_id =  ? and accion = 'NO_APLICA' ";
			
			
	*/
	
	/**
	 * CBConfiguracionConexionDAO
	 * */
	
	public static final String CONSULTA_CONF_CONEXION = "SELECT id_conexion_conf idConexionConf, "
			+ "nombre_conexion nombre, "
			+ "ip_conexion ipConexion, "
			+ "usuario usuario, "
			+ "pass pass, "
			+ "creado_por creadoPor, "
			+ "to_char(fecha_creacion, 'dd/MM/yyyy') fechaCreacion, "
			+ "modificado_por modificadoPor, "
			+ "to_char(fecha_modificacion, 'dd/MM/yyyy') fechaModificacion "
			+ "FROM cb_conexion_conf where 1 = 1 ";
	
	public static final String INSERTA_NUEVA_CONF_CONEXION = "INSERT INTO cb_conexion_conf "
			+ "( "
			+ "  id_conexion_conf, "
			+ "  ip_conexion, "
			+ "  nombre_conexion, "
			+ "  usuario, "
			+ "  pass, "
			+ "  creado_por, "
			+ "  fecha_creacion "
			+ ") "
			+ "VALUES "
			+ "( "
			+ "  (SELECT NVL(MAX(id_conexion_conf), 0)+1 FROM cb_conexion_conf "
			+ "  ) "
			+ "  , "
			+ "  ?, "
			+ "  ?, "
			+ "  ?, "
			+ "  ?, "
			+ "  ?, "
			+ "  sysdate " + ") ";

	public static final String ACTUALIZA_CONEXION = "UPDATE cb_conexion_conf "
			+ "SET nombre_conexion    = ?, " + "  ip_conexion          = ?, "
			+ "  usuario              = ?, " + "  pass                 = ?, "
			+ "  modificado_por       = ?, "
			+ "  fecha_modificacion   = sysdate "
			+ "WHERE id_conexion_conf = ? ";
			
	public static final String ELIMINA_CONEXION = "delete cb_conexion_conf where id_conexion_conf = ? ";

	/**
	 * CBConfiguracionConfrontaDAO
	 * */
	
	public static final String CONSULTA_LISTA_CONFRONTA = "SELECT cbconfiguracionconfrontaid cBConfiguracionConfrontaId, "
			+ " nombre nombre, "
			+ " delimitador1 delimitador1, "
			+ " delimitador2 delimitador2, "
			+ " cantidad_agrupacion cantidadAgrupacion, "
			+ " nomenclatura nomenclatura, "
			+ " estado estado, "
			+ " tipo tipo, "
			+ " formato_fecha formatoFecha, "
			+ " longitud_cadena longitudCadena, "
			+ " posiciones posiciones, "
			/*+ " cantidad_ajustes cantidadAjustes, "
			+ " descartar_transaccion descartarTransaccion, "*/
			+ " creado_por creadoPor, "
			+ " fecha_creacion fechaCreacion "
			+ " FROM CB_CONFIGURACION_CONFRONTA "
			+ " where estado = 1 "; 
	
	public static final String CONSULTA_CONFIG_CONFRONTA = "SELECT cBConfiguracionConfrontaId cBConfiguracionConfrontaId, "
			+ " nombre nombre, "
			+ " delimitador1 delimitador1, "
			+ " delimitador2 delimitador2, "
			+ " cantidad_Agrupacion cantidadAgrupacion, "
			+ " nomenclatura nomenclatura, "
			+ " estado estado, "
			+ " tipo tipo, "
			+ " formato_Fecha formatoFecha,"
			+ " posiciones posiciones, "
			+ " longitud_cadena longitudCadena "
			/*+ " cantidad_ajustes cantidadAjustes, "
			+ " descartar_transaccion descartarTransaccion "*/
			+ " FROM CB_CONFIGURACION_CONFRONTA "
			+ " WHERE cBConfiguracionConfrontaId = ? ";
	
	public static final String CONSULTA_CONFRONTAS_ASOCIADAS = "SELECT c.CBBANCOAGENCIACONFRONTAID, b.NOMBRE "
			+ "FROM CB_CATALOGO_AGENCIA a, CB_CONFIGURACION_CONFRONTA b, CB_BANCO_AGENCIA_CONFRONTA c "
			+ "WHERE b.CBCONFIGURACIONCONFRONTAID = c.CBCONFIGURACIONCONFRONTAID "
			+ "AND a.CBCATALOGOAGENCIAID = c.CBCATALOGOAGENCIAID "
			+ "AND b.ESTADO = 1 AND c.CBCATALOGOAGENCIAID = ?";
	
	public static final String QRY_CONFRONTA_EXISTENTE = "SELECT CBCONFIGURACIONCONFRONTAID FROM "
			+ "CB_CONFIGURACION_CONFRONTA WHERE UPPER(NOMBRE) = ?";
	
	public static final String OBTIENE_DELIMITADORES_QRY = "SELECT OBJETO, VALOR_OBJETO1, DESCRIPCION " + 
			"  FROM CB_MODULO_CONCILIACION_CONF " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_CONFRONTA') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('DELIMITADOR') " +
			"   AND UPPER (estado) = UPPER ('S')";
	
	public static final String OBTIENE_NOMENCLATURAS_QRY = "SELECT OBJETO, VALOR_OBJETO1, DESCRIPCION " + 
			"  FROM CB_MODULO_CONCILIACION_CONF " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_CONFRONTA') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('NOMENCLATURA') " +
			"   AND UPPER (estado) = UPPER ('S') ORDER BY CBMODULOCONCILIACIONCONFID";
	
	public static final String VALIDA_FORMATO_FECHA = "select to_char(sysdate, ?) resultado from dual";
	
	public static final String QRY_OBTIENE_ESTADOS3 = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE UPPER(MODULO) = UPPER('CONFIGURACION_CONFRONTA') "
			+ "AND UPPER(TIPO_OBJETO) =  UPPER('ESTADO') " +
			"   AND UPPER(ESTADO) = UPPER ('S')";
	
	public static final String QRY_GENERAL_CONF_CONFRONTAS = "SELECT CBCONFIGURACIONCONFRONTAID, NOMBRE, DELIMITADOR1, CANTIDAD_AGRUPACION, "
			+ "NOMENCLATURA, ESTADO, CASE ESTADO WHEN 1 THEN 'ACTIVA' WHEN 0 THEN 'BAJA' END ESTADO_TXT, FORMATO_FECHA, POSICIONES, LONGITUD_CADENA, "
			+ "LINEA_LECTURA FROM CB_CONFIGURACION_CONFRONTA ORDER BY NOMBRE";
	
	public static final String INSERT_CONF_CONFRONTA = "INSERT INTO CB_CONFIGURACION_CONFRONTA "
			+ "(CBCONFIGURACIONCONFRONTAID, NOMBRE, DELIMITADOR1, CANTIDAD_AGRUPACION, NOMENCLATURA, "
			+ "ESTADO, FORMATO_FECHA, CREADO_POR, FECHA_CREACION, "
			+ "POSICIONES, LONGITUD_CADENA, LINEA_LECTURA) VALUES (CB_CONFIGURACION_CONFRONTA_SQ.nextval,?,?,?,?,?,?,?, sysdate, ?,?,?)";
	
	public static final String UPDATE_CONF_CONFRONTA = "UPDATE CB_CONFIGURACION_CONFRONTA SET NOMBRE = ?, DELIMITADOR1 = ?, ESTADO = ?, "
			+ "FORMATO_FECHA = ?, LINEA_LECTURA = ?, MODIFICADO_POR = ?, FECHA_MODIFICACION = sysdate, "
			+ "CANTIDAD_AGRUPACION = ?, NOMENCLATURA = ?, POSICIONES = ?, LONGITUD_CADENA = ? "
			+ "WHERE CBCONFIGURACIONCONFRONTAID = ?";
	
	public static final String DELETE_CONF_CONFRONTA = "DELETE FROM CB_CONFIGURACION_CONFRONTA WHERE CBCONFIGURACIONCONFRONTAID = ?";
	
	/**
	 * CBConsultaContabilizacionDAO
	 * */
	
	public static final String QRY_VALOR_CONF = "SELECT VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'CONSULTA_CONTABILIZACION' AND TIPO_OBJETO = 'CONTABILIZACION_SAP' ";
	
	public static final String INSERT_CONTABILIZACION_SQ = "INSERT INTO cb_contabilizacion SELECT cb_contabilizacion_sq.NEXTVAL,"
			+ " fecha, cbcatalogoagenciaid,cbcatalogobancoid, SUBSTR (agencia, 1, 16), b.nombre, texto,NULL,"
			+ "a.clave_contabilizacion_cp,a.terminacion_cp, a.centro_costo, a.cbtipologiaspolizaid, a.cuenta_contrapartida,"
			+ " cbestadocuentaid, DECODE (a.tipo, 'Ingreso', 1,  'Diferencia', 3,  'Contrapartida', 2), DECODE (a.tipo,"
			+ " 'Ingreso', monto_banco * 100,'Contrapartida', sc * 100, (monto_banco - sc) * 100),  1, NULL,  NULL,0,"
			+ " 'TERIUM ADMIN',NULL,SYSDATE,  NULL, NULL,NULL FROM cb_contabilizacion_vw a, cb_tipologias_poliza b"
			+ " WHERE     a.cbtipologiaspolizaid = b.cbtipologiaspolizaid ";

	public static final String VALIDA_CARGA_CONTABILIZACION_SQ = "SELECT FECHA_CREACION  "
			+ "FROM cb_contabilizacion WHERE  trunc(FECHA) >= to_date(?, 'dd/MM/yyyy') and trunc(FECHA) <= to_date(?, 'dd/MM/yyyy')";

	public static final String DELETE_REGISTROS_CREDO = "DELETE FROM cb_contabilizacion WHERE " + "FECHA = ?  ";

	public static final String CARGA_CONTABILIZACION_SP = "{CALL CB_CARGA_CONTABILIZACION_SP(?,?)}";

	public static final String OBTIENE_BANCO_SQ4 = "select cbcatalogobancoid, nombre " + "from cb_catalogo_banco "
			+ "where estado = 1 ";
	
	public static final String OBTIENE_AGENCIA_SQ2 = "select b.cbcatalogoagenciaid, b.nombre "
			+ "from cb_catalogo_banco a, cb_catalogo_agencia b " + "where a.cbcatalogobancoid = b.cbcatalogobancoid "
			+ "and b.estado = 1 " + "and a.estado = 1 ";
	
	/**
	 * CBConsultaEstadoCuentasDAO
	 * */
	
	public static final String BANCO_SQ= "select distinct cbcatalogobancoid, banco from cb_estado_cuenta_vw";
	
	public static final String CONSULTA_ESTADO_CUENTAS_SQ = " select cbestadocuentasociedadid, banco, agencia, trim(cuenta) cuenta, asignacion, "
			+ "to_char(fecha,'DD/MM/YYYY') fecha, texto, debe, haber, identificador, "
			+ "CASE WHEN tipologia IS NULL THEN '(No asignada)' ELSE tipologia END tipologia, "
			+ "CASE WHEN agencia_ingreso IS NOT NULL THEN agencia_ingreso "
			+ "ELSE (CASE WHEN tipologia IS NOT NULL THEN 'N/A' ELSE '(No asignada)' END) END agencia_ingreso, texto_cab_doc, "
			+ "observaciones, to_char(fecha_ingresos, 'DD/MM/YYYY') fecha_ingresos,numdocumento "
			+ " from cb_estado_cuenta_vw " + " where 1 = 1 ";
	
	public static final String OBTENER_TIPOLOGIAS = "Select CBTIPOLOGIASPOLIZAID, NOMBRE, DESCRIPCION, CREADO_POR, FECHA_CREACION, TIPO, "
			+ "PIDE_ENTIDAD from cb_tipologias_poliza";
	
	public static final String QRY_AGENCIA_TIPOLOGIAS = "select distinct cbcatalogoagenciaid, nombre from cb_catalogo_agencia "
			+ "where estado = 1 and cuenta_contable is null order by nombre";
	
	public static final String QRY_ENTIDADES_ESTADOS_TARJETA = "SELECT CBCATALOGOAGENCIAID, NOMBRE FROM CB_CATALOGO_AGENCIA "
			+ "WHERE ESTADO = 1 ORDER BY NOMBRE";
	
	public static final String OBTIENE_RANGO_DIAS_SQ = "SELECT VALOR_OBJETO1 "+ 
			"FROM CB_MODULO_CONCILIACION_CONF " + 
			"WHERE MODULO = 'CONSULTA_ESTADO_CUENTA' " + 
				"AND TIPO_OBJETO = 'RANGO_DIAS' " + 
				"AND OBJETO = 'DIAS' " + 
				"AND ESTADO = 'S'";
	
	/**
	 * CBDataBancoDAO
	 * */
	
	public static final String EJECUTA_PROCESO_CB_CONCILIACION_SP = "{call CB_CONCILIACION_SP(?)}";

	public static final String EJECUTA_PROCESO_CB_AJUSTES_SP = "{call CB_AJUSTES(?, ?)}";

	public static final String EJECUTA_PROCESO_CB_AJUSTES_PENDIENTES_SP = "{call CB_AJUSTES_PENDIENTES(?, ?)}";
	
	public static final String BORRAR_NO_REGISTRADOS = "Delete cb_data_sin_procesar "
			+ " where id_archivos_insertados = ?";
	
	public static final String CONSULTA_ARCHIVO_DELETE = "Select id_archivos_insertados idArchivosViejos, nombre_archivo nombreArchivo "
			+ "from cb_archivos_insertados where nombre_archivo = ? AND id_archivos_insertados != ?";
	
	public static final String BORRAR_ARCHIVO_PRECARGADO = "Delete cb_archivos_insertados "
			+ " where id_archivos_insertados = ? and nombre_archivo = ? ";

	public static final String LLAMADA_SP_PAGOS_IND = "{call CB_PAGO_INDIVIDUAL(?, to_date(?, 'dd/MM/yy'), to_date(?, 'dd/MM/yy'))}";

	public static final String RECUPERA_COMISION = "SELECT NVL(COMISION,0) FROM CB_BANCO_AGENCIA_CONFRONTA WHERE CBBANCOAGENCIACONFRONTAID = ?";

	public static final String INSERT_LOG = "INSERT INTO CB_PROCESOS_CONTROL VALUES(?,sysdate)"; 
	
	/**
	 * CBDataSinProcesarDAO
	 * */
	
	public static final String INSERTA_DATOS_NO_PROCESADOS = "INSERT " + "INTO cb_data_sin_procesar " + "  ( "
			+ "  cbdatasinprocesarid,  nombre_archivo, " + "    data_archivo, " + "causa,    estado, "
			+ "    creado_por, " + "    fecha_creacion, " + "    id_archivos_insertados " + "  ) " + "  VALUES "
			+ "  ( " + " cb_data_sin_procesar_sq.nextval,   ?, " + "    ?, " + " ?,   ?, " + "    ?, " + "    sysdate, "
			+ "    ? " + "  )";

	/**
	 * CBEstadoCuentaDAO
	 * */
	
	public static final String DELETE_REGISTROS_ARCHIVO = "DELETE FROM CB_ESTADO_CUENTA_ARCHIVOS WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ? ";
	
	public static final String DELETE_REGISTROS_CREDOMATIC = "DELETE FROM CB_ESTADO_CUENTA WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  AND TRUNC(SWDATECREATED) = TRUNC(SYSDATE)";
	
	public static final String DELETE_REGISTROS_CREDODET = "DELETE FROM CB_ESTADO_CUENTA_DET WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  AND TRUNC(SWDATECREATED) = TRUNC(SYSDATE)";
	
	public static final String DELETE_REGISTROS_VISA = "DELETE FROM CB_ESTADO_CUENTA WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  AND TRUNC(SWDATECREATED) = TRUNC(SYSDATE)";
	
	public static final String DELETE_REGISTROS_VISADET = "DELETE FROM CB_ESTADO_CUENTA_DET WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ?  AND TRUNC(SWDATECREATED) = TRUNC(SYSDATE)";
	
	public static final String DELETE_REGISTROS_SOCIEDAD = "DELETE FROM CB_ESTADO_CUENTA_SOCIEDAD WHERE "
			+ "CBESTADOCUENTAARCHIVOSID = ? AND CBESTADOCUENTACONFID = ? ";

	public static final String EJECUTA_SP_EXTRACTO = "{CALL CB_CONCILIACION_CAJAS_PKG.cb_depositos_cuenta_sp()}";

	public static final String EJECUTA_SP_VISA = "{CALL CB_CONCILIACION_CAJAS_PKG.CB_CONCILIA_VISA_SP()}";
	
	public static final String EJECUTA_SP_CREDOMATIC = "{CALL CB_CONCILIACION_CAJAS_PKG.CB_CONCILIA_CRED_SP()}";
	
	/**
	 * CBHistorialAccionDAO
	 * */
	
	/**
	 * Todas las querys estandarizarlas a parametrizables (PreparedStatement
	 * 
	 * getTotalRegistros()
	 * query = "select accion,monto,observaciones,creado_por,fecha_creacion,modificado_por,fecha_modificacion, CBHISTORIALACCIONID from CB_HISTORIAL_ACCION where CBCONCILIACIONID = "
					+ id;
		
		insertarReg()
		String qry = "INSERT INTO CB_HISTORIAL_ACCION(CBHISTORIALACCIONID,ACCION,MONTO,OBSERVACIONES,CREADO_POR,FECHA_CREACION,CBCONCILIACIONID) VALUES"
				+ "( CB_HISTORIAL_ACCION_SQ.NEXTVAL , '"
				+ obj.getAccion()
				+ "','"
				+ obj.getMonto()
				+ "','"
				+ obj.getObservaciones()
				+ "','" + obj.getCreadoPor() + "',sysdate," + idPadre + ")";
	
		updateReg()
		String qry = "update CB_HISTORIAL_ACCION set  accion = '"
					+ evt.getAccion() + "'," + " monto = '" + evt.getMonto()
					+ "'," + " observaciones = '" + evt.getObservaciones()
					+ "'," + " modificado_por = '" + evt.getModificadoPor()
					+ "'," + " fecha_modificacion = sysdate "
					+ " WHERE CBHISTORIALACCIONID = '"
					+ evt.getcBHistorialAccionId()
					+ "' and CBCONCILIACIONID = " + idPadre;
					
		deleteReg()
		String qry = "delete from CB_HISTORIAL_ACCION  WHERE CBHISTORIALACCIONID = '"
					+ evt.getcBHistorialAccionId()
					+ "' and CBCONCILIACIONID = " + idPadre;
	 * */
	
	/**
	 * CBLiquidacionCajeroDAO
	 * */
	
	/**
	 * La invocacion del SP para inserciones masivas no se usa del lado de la aplicacion
	 * 
	 * "CALL CARGA_MASIVA_LIQUIDACIONES_PRC(?,?,?,?,?)"
	 * */
	
	/**
	 * CBMantenimientoTipologiasPolizaDAO
	 * */
	
	public static final String QRY_OBTIENE_TIPOS = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'TIPOLOGIAS_POLIZA_TIPO' AND TIPO_OBJETO = ? ";
	
	public static final String QRY_OBTIENE_ENTIDAD = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'TIPOLOGIAS_POLIZA_ENTIDAD' AND TIPO_OBJETO = ? ";
	
	/**
	 * CBParametrosGeneralesDAO
	 * */
	
	public static final String QRY_OBTIENE_ESTADOS4 = "SELECT OBJETO, VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'PARAMETROS_GENERALES_CONF' AND TIPO_OBJETO =  'ESTADO'";
	
	public static final String QRY_GENERAL = "SELECT CBMODULOCONCILIACIONCONFID, MODULO, TIPO_OBJETO, OBJETO, VALOR_OBJETO1, VALOR_OBJETO2, VALOR_OBJETO3, "
			+ "DESCRIPCION, ESTADO FROM CB_MODULO_CONCILIACION_CONF WHERE ACCESO_MODULO = 'S' ";
	
	public static final String QRY_INSERT = "Insert into CB_MODULO_CONCILIACION_CONF "
			+ "(CBMODULOCONCILIACIONCONFID, MODULO, TIPO_OBJETO, OBJETO, VALOR_OBJETO1, "
			+ "VALOR_OBJETO2, VALOR_OBJETO3, DESCRIPCION, ESTADO, SACREATEDY, "
			+ "SADATECREATED, ACCESO_MODULO)"
			+ "Values(CB_MODULO_CONCILIACION_CONF_SQ.nextval,?,?,?,?,?,?,?,?,?, sysdate, 'S')";
	
	public static final String QRY_UPDATE = "UPDATE CB_MODULO_CONCILIACION_CONF SET MODULO = ?, TIPO_OBJETO = ?, OBJETO = ?, VALOR_OBJETO1 = ?, "
			+ "VALOR_OBJETO2 = ?, VALOR_OBJETO3 = ?, DESCRIPCION = ?, ESTADO = ? WHERE CBMODULOCONCILIACIONCONFID = ?";
	
	public static final String QRY_DELETE = "DELETE FROM CB_MODULO_CONCILIACION_CONF WHERE CBMODULOCONCILIACIONCONFID = ?";
	
	public static final String OBTIENE_CONVENIOS_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_TIPO_CLIENTE') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('CONVENIOS') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	public static final String OBTIENE_CUENTAS_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_TIPO_AFILIACION') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('TIPO') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	public static final String OBTIENE_CUENTAS_ESTADO_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONFIGURACION_ESTADO_AFILIACION') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('ESTADO') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	public static final String OBTIENE_TIPO_QRY = "SELECT cbmoduloconciliacionconfid, objeto, valor_objeto1 " + 
			"  FROM cb_modulo_conciliacion_conf " + 
			" WHERE UPPER (modulo) = UPPER ('CONSULTA_ESTADO_CUENTA_TARJETA') " + 
			"   AND UPPER (tipo_objeto) = UPPER ('TIPO') " + 
			"   AND UPPER (estado) = UPPER ('S')";
	
	// QUERY : FINANCIERA - NO FINANCIERA
	public static final String S_OBTENER_TIPO_AGRUPACIONES = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY "+
			"FROM CB_MODULO_CONCILIACION_CONF " + 
			"WHERE MODULO = 'CONFIGURACION_AGRUPACIONES' " + 
			"AND TIPO_OBJETO ='TIPO_AGRUPACIONES' ";
	
	// QUERY : ACTIVA - INACTIVA
	public static final String S_OBTENER_TIPO_AGRUPACIONES_2 = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY " + 
			"			FROM CB_MODULO_CONCILIACION_CONF " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES' " + 
			"			AND TIPO_OBJETO ='ESTADO'";
	
	// QUERY : TIPO AGENCIA {PREPAGO , POSTPAGO}
	public static final String S_OBTENER_TIPO_AGENCIA = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY " + 
			"			FROM CB_MODULO_CONCILIACION_CONF " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES' " + 
			"			AND TIPO_OBJETO ='TIPO_AGENCIA' ";
	
	// QUERY : TIPO AGENCIA VF {VIRTUAL ,PRESENCIAL}
	public static final String S_OBTENER_AGENCIA_VF =
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES'  " + 
			"			AND TIPO_OBJETO ='TIPO_AGENCIA_VF' ";
	
	// QUERY : LIQUIDACION TIPO DE CARGA {CARGA UNICA , CARGA MASIVA}
	public static final String S_OBTENER_TIPO_LIQUIDACION_CARGA = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONFIGURACION_AGRUPACIONES'  " + 
			"			AND TIPO_OBJETO ='TIPO_AGRUPACION_LIQUIDACION_CARGA' ";
	
	// QUERY : CONFIGURACION NOMENCLATURA CONFRONTA
	public static final String S_OBTENER_CONF_NOMENCLATURA = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONFIGURACION_CONFRONTA'  " + 
			"			AND TIPO_OBJETO ='DELIMITADOR' ";
	
	// QUERY : TIPO PAGO {PREPAGO , POSTPAGO}
	public static final String S_OBTENER_PAGO_CONCILIACION = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'APLICA_DESAPLICA_PAGO'  " + 
			"			AND TIPO_OBJETO ='TIPO' AND OBJETO != 'TODOS' ";
	
	public static final String S_OBTENER_ESTADO_CONCILIACION = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONCILIACION'  " + 
			"			AND TIPO_OBJETO ='ESTADO' ";
	
	public static final String S_OBTENER_ESTADO_CONCILIACION_DET = 
			"SELECT CBMODULOCONCILIACIONCONFID,MODULO,TIPO_OBJETO,OBJETO,VALOR_OBJETO1,VALOR_OBJETO2, " + 
			"			VALOR_OBJETO3,DESCRIPCION,ESTADO,SACREATEDY  " + 
			"			FROM CB_MODULO_CONCILIACION_CONF  " + 
			"			WHERE MODULO = 'CONCILIACION'  " + 
			"			AND TIPO_OBJETO ='ESTADO_DETALLE' ";
	
	/**
	 * CBReportesDAO
	 * */
	
	public static final String QRY_VALOR_DEFECTO = "SELECT VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF WHERE MODULO = 'REPORTES' "
			+ "AND TIPO_OBJETO = 'TIPO_REPORTE_PREDEFINIDO' AND OBJETO = 'VALOR'";
	
	public static final String QRY_OBTIENE_TIPO_OBJETO_X = "SELECT OBJETO, VALOR_OBJETO1  FROM CB_MODULO_CONCILIACION_CONF "
			+ "WHERE MODULO = 'REPORTES' AND ESTADO = 'S' AND TIPO_OBJETO = ?";
	
	public static final String CONSULTA_REPORTE_X_ENTIDAD2 = " SELECT DISTINCT A.DIA fecha,"
			+ " TO_NUMBER(TO_CHAR(A.FECHA,'MM')) mes_1,"
			+ " DECODE(TO_NUMBER(TO_CHAR(TO_DATE(A.FECHA,'dd/MM/yyyy'),'MM')),1,'ENERO',2,'FEBRERO',3,'MARZO',4,'ABRIL',5,'MAYO',6,'JUNIO',7,'JULIO',8,'AGOSTO',9,'SEPTIEMBRE',10,'OCTUBRE',11,'NOVIEMBRE',12,'DICIEMBRE') mes_2,"
			+ " TO_CHAR(A.FECHA,'HH24:mi:ss') hora,"
			+ " NVL(A.MONTO,0) monto,"
			+ " A.TELEFONO telefono,"
			+ " B.NOMBRE banco,"
			+ " DECODE(A.TIPO, '1', 'PRE-PAGO', '2', 'POST-PAGO') tipo_servicio,"
			+ " A. TRANSACCION secuencia,"
			+ " C.NOMBRE agencia,"
			+ " DECODE(D.TIPO,0,'VIRTUAL',1,'PRESENCIAL','PRESENCIAL') forma_de_pago,"
			+ " NVL((A.COMISION*A.MONTO),0) comision,"
			+ " NVL(A.COMISION,0) porcentaje, " + " A.CODIGO sucursal, "
			+ " D.NOMBRE nombre_sucursal, " + " 1 cantidad"
			+ " FROM CB_DATA_BANCO A, " + " CB_CATALOGO_BANCO B, "
			+ " CB_CATALOGO_AGENCIA C, " + " CB_AGENCIA_VIR_FIS D, "
			+ " CB_BANCO_AGENCIA_CONFRONTA E, " + " CB_CONCILIACION F "
			+ " WHERE A.CBCATALOGOBANCOID = B.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOBANCOID   = C.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = C.CBCATALOGOAGENCIAID "
			+ " AND A.CODIGO              = D.CODIGO(+) "
			+ " AND A.CBCATALOGOBANCOID   = E.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = E.CBCATALOGOAGENCIAID "
			+ " AND A.CBDATABANCOID       = F.CBDATABANCOID "
			+ " AND A.DIA                 = F.DIA "
			+ " AND A.CBCATALOGOAGENCIAID = F.CBCATALOGOAGENCIAID "
			+ " AND A.CBCATALOGOAGENCIAID = D.CBCATALOGOAGENCIAID(+) ";
	
	public static final String CONSULTA_REPORTE_X_ESTADO = "SELECT dia, " + "nombre, " + "tipo, "
			+ "trans_telefonica transTelefonica, " + "trans_banco transBanco, "
			+ "conciliadas, " + "dif_trans difTransaccion, "
			+ "pagos_telefonica pagosTelefonica, "
			+ "confronta_banco confrontaBanco, " + "automatica, "
			+ "manual_t manualTelefonica, " + "manual_b manualBanco, "
			+ "pagos_telefonica - automatica - manual_t pendienteTelefonica, "
			+ "confronta_banco  - automatica - manual_b pendienteBanco  "
			+ "FROM cb_conciliacion_vw where 1 = 1 ";
	
	public static final String QRY_REPORTE3 = "SELECT agencia, " + "dia, " + "tipo, " + "cliente, "
			+ "a.nombre, " + "des_pago desPago, " + "trans_telca transTelca, "
			+ "a.telefono, " + "trans_banco transBanco, " + "imp_pago impPago, "
			+ "monto, " + "manual, " + "pendiente, " + " comision, "
			+ " (monto * comision) monto_comision, " + " sucursal, "
			+ " nombre_sucursal, " + " tipo_sucursal "
			+ "FROM cb_conciliacion_detail2_vw a,cb_catalogo_agencia b "
			+ "WHERE 1                 = 1  "
			+ "and a.cbcatalogoagenciaid = b.cbcatalogoagenciaid ";
	
	public static final String CONSULTA_CAJEROS = "SELECT CBCATALOGOBANCOID,ENTIDAD,CBCATALOGOAGENCIAID,AGENCIA, to_char(FECHA, 'dd/MM/yyyy'),CAJA_EFECTIVO,CAJA_CHEQUE, "
			+ "CAJA_EXENSIONES, CAJA_CUOTAS_VISA, CAJA_CUOTAS_CREDOMATIC,CAJA_TARJETA_CREDOMATIC,CAJA_TARJETA_OTRAS,CAJA_TARJETA_VISA, "
			+ "SC_PAGOSOD,SC_PAGOSOM,SC_REVERSASOD,SC_REVERSASOM, "
			+ "NVL(CAJA_TOTAL,0)+NVL(SC_PAGOSOD,0)+NVL(SC_REVERSASOD,0) TOTAL_DIA, "
			+ "CAJA_TOTAL, CREDOMATIC_DEP CONSUMO_CREDOMATIC,CREDOMATIC_RET RETENCION_CREDOMATIC,ESTADO_CRED,CONSUMO_VISA, "
			+ "IVA_VISA,ESTADO_VISA,DEPOSITO, "
			+ "NVL(CREDOMATIC_DEP,0)+NVL(CONSUMO_VISA,0)+NVL(IVA_VISA,0)+NVL(DEPOSITO,0) TOTAL_EC, "
			+ "NVL(TOTAL_SC,0)-(NVL(CREDOMATIC_DEP,0)+NVL(CONSUMO_VISA,0)+NVL(IVA_VISA,0)+NVL(DEPOSITO,0)) DIFERENCIA "
			+ "FROM CB_CONCILIACION_CAJAS_VW "
			+ "WHERE 1 = 1  ";
	
	public static final String CONSULTA_REPORTE_LIQUIDACIONES2 = "SELECT NOMBTRANSACCION, to_char(FECHATRANSACCION,'DD-MM-YYYY') FechaTransaccion, TIPOVALOR, TIPOPAGO, "
			+ "TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL FROM CB_REPORTE_LIQUIDACIONES_VW WHERE 1 = 1  ";
	
	public static final String CANT_REGISTROS_CONCILIACION = "select count(*) conteo from cb_conciliacion "
			+ "where cbbancoagenciaconfrontaid = ?";
}
