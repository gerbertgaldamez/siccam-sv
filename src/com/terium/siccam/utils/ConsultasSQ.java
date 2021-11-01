package com.terium.siccam.utils;

/**
 * @author Juankrlos - QitCorp
 **/
public class ConsultasSQ {

	// Este query se manda a llamar desde la clase CBConciliacionBancoDAO por el metodo generaConsultaPrincipal()
	// Se utiliza de forma general el query de GT para que todos los paises
	// realize la consulta a la vista cb_conciliacion_banco_vs_vw

	public static final String CONSULTA_CONCILIACION_BANCO = "select to_char(dia,'dd/MM/yyyy'), cbcatalogoagenciaid, nombre, codigo_colector, "
			+ "estado_cuenta, confronta_banco, dif_postpago, pagosdeldia, pagosotrosdias, pagosotrosmeses, "
			+ "reversasotrosdias, reversasotrosmeses, total_dia, total_general, estado_cuenta_total, conciliado_manual, diferencia_total, "
			+ "porcentaje_comision, comision_confronta, comision_sc, diferencia_comision,"
			+ " comisiontotal, recafinal, totalfinal, cbbancoagenciaconfrontaid, forma_pago "
			+ "from  cb_conciliacion_banco_vs_vw WHERE 1=1 ";

	//
	public static final String OBTIENE_ESTADO_CUENTACONF_SQ = "select CBMODULOCONCILIACIONCONFID, OBJETO  "
			+ "from CB_MODULO_CONCILIACION_CONF where modulo='CARGA_ESTADO_CUENTA' and estado = 'S'";

	//
	public static final String MUESTRA_FECHA_SQ = "SELECT TO_CHAR(SYSDATE,'DD/MM/YYYY') FROM DUAL";

	//
	public static final String ESTADO_CUENTA_ARCHIVOS_ID = "SELECT CB_ESTADO_CUENTA_ARCHIVOS_SQ.NEXTVAL FROM DUAL";

	//
	public static final String VALIDA_CARGA_ESTADOS_SQ = "SELECT CBESTADOCUENTAARCHIVOSID, NOMBRE, SWDATECREATED  "
			+ "FROM CB_ESTADO_CUENTA_ARCHIVOS WHERE CBESTADOCUENTACONFID = ? AND NOMBRE = ? ";
	// + " AND trunc(SWDATECREATED) = to_date(?, 'dd/mm/yyyy')";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentaSociedad(),modificado
	public static final String INSERT_SOCIEDAD_SQ = "INSERT INTO CB_ESTADO_CUENTA_SOCIEDAD(CBESTADOCUENTASOCIEDADID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID,CUENTA,REFERENCIA,ASIGNACION,CLASE,"
			+ " NUMDOCUMENTO,FECHAVALOR,FECHACONTAB,TEXTO,MON,IMPORTEMD, IMPORTEML,DOCUCOMP,CTACP,IDENTIFICADOR, "
			+ "FECHA_INGRESOS,TEXTO_CAB_DOC, ANULACION, CT, EJERCICIO_MES, FECHA_DOC, LIB_MAYOR, PERIODO , IMPORTE_ML3, "
			+ "USUARIO,ml,ml3,compens,afun,area_funcional,ce_coste,codigo_transaccion,clv_ref_cabecera,bco_prp,orden,tp_camb_ef,tpbc,SWCREATEBY,SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_SOCIEDAD_SQ.NEXTVAL, ?,?,?,?,?,?,?,to_date(?,'dd/MM/yyyy'),"
			+ " to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,?,null,?,?,?,to_date(?,'yyyy/MM'),to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,?,?,?,?,SYSDATE)";

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

	// Insert para la nueva version de carga de archivos credomatic
	public static final String INSERT_CREDO_SQ2 = "INSERT INTO CB_ESTADO_CUENTA "
			+ "   (CBESTADOCUENTAID,CBESTADOCUENTACONFID,CBESTADOCUENTAARCHIVOSID,AFILIACION,DESCRIPCION,CONSUMO,COMISION,IVA_COMISION,RETENCION,LIQUIDO,FECHA_TRANSACCION,CODIGO_LOTE, "
			+ "	  DEBITO, BALANCE,REFERENCIA, " + "	 TIPO, SWCREATEBY,SWDATECREATED) " + "	 VALUES "
			+ "   (CB_ESTADO_CUENTA_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?,to_date(?,'yyyyMMdd')-1,?,?,?,?,?,?,sysdate) ";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java carga detalle
	// credomatic modificado para credomatic
	public static final String INSERT_CREDO_DET_SQ = "INSERT INTO CB_ESTADO_CUENTA_DET(CBESTADOCUENTAVISADETID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID,TARJETA, TERMINAL, LOTE, CONSUMO, PROPINA, COMISION, LIQUIDO, AUTORIZACION, FECHA_VENTA, F_CIERRE, DESCRIPCION, AFILIACION, "
			+ " TIPO_TRANS, SWCREATEBY, SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_DET_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,to_date(?,'mm/DD/yy'),to_date(?,'yyyyMMdd'),"
			+ " ?,?,?,?,SYSDATE) ";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentasVisaDet()
	public static final String INSERT_VISA_DET_SQ = "INSERT INTO CB_ESTADO_CUENTA_DET(CBESTADOCUENTADETID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, AFILIACION, F_CIERRE,TERMINAL, LOTE, TARJETA, FECHA_VENTA,"
			+ " HORA, AUTORIZACION, CONSUMO, TIPO_TRANS, IMP_TURISMO, PROPINA, COMISION, IVA, LIQUIDO, SWCREATEBY, SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_DET_SQ.NEXTVAL, ?,?,?,to_date(?,'dd/MM/yyyy'),?,?,?,to_date(?,'dd/MM/yyyy'),"
			+ " ?,?,?,?,?,?,?,?,?,?,SYSDATE)";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentasVisa()
	public static final String INSERT_VISA_SQ = "INSERT INTO CB_ESTADO_CUENTA(CBESTADOCUENTAID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, FECHA_TRANSACCION,AFILIACION, TIPO, REFERENCIA, CODIGO_LOTE, "
			+ " CONSUMO, IMPUESTO_TURIS, PROPINA, IVA, COMISION, IVA_COMISION, LIQUIDO, RETENCION, "
			+ " DOCUMENTO, DESCRIPCION, SWCREATEBY, SWDATECREATED) "
			+ " VALUES(CB_ESTADO_CUENTA_SQ.NEXTVAL,?,?,to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertSistemaComercial() Ovidio Santos////////////////////
	public static final String INSERT_SISTEMACOMERCIAL_SQ = "INSERT INTO CB_PAGOS (CBPAGOSID, "
			+ " FEC_EFECTIVIDAD, NUM_SECUENCI,COD_CLIENTE,TELEFONO,IMP_PAGO,COD_CAJA,"
			+ " DES_PAGO,TIPO,ESTADO_CONCILIADO,NUM_CONCILIACION,FECHA,CBBANCOAGENCIACONFRONTAID,FEC_TRANSACCIONAL,NOM_CLIENTE,"
			+ "COD_CLICLO,TRANSACCION,TIPO_TRANSACCION,TIP_MOVCAJA,TIP_VALOR,NOM_USUARORA,COD_BANCO,COD_OFICINA,COD_SEGMENTO,"
			+ "DES_SEGMENTO,COD_MONEDA,creado_Por,fecha_Creacion,CBESTADOCUENTAARCHIVOSID,CBESTADOCUENTACONFID)"
			+ " VALUES(CB_PAGOS_SQ.NEXTVAL, to_date(?,'dd/MM/yy'),?,?,?,?,?,?,"
			+ " ?,?,?, to_date(?,'dd/MM/yy'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?)";
	//

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

	// Editado última vez por CarlosGodinez - 07/05/2017
	public static final String CONSULTA_QUERY_NUEVA_LIQUIDACION = "SELECT TIP_VALOR, TIPO_PAGO, COD_TIPTARJETA, DES_TIPTARJETA, IMPORTE from CB_LIQUIDACION_DETALLE_VW "
			+ "WHERE  to_char(fec_efectividad,'dd-mm-yyyy') = ? "
			+ "AND nom_usuarora IN (SELECT DISTINCT COD_CAJA FROM CB_BANCO_AGENCIA_CAJAS WHERE CAJERO = ?)";

	public static final String CONS_DETALLE_LIQUIDACION_BY_ID = "SELECT CBLIQUIDACIONDETALLEID, TIPOVALOR, TIPOPAGO, TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL FROM "
			+ "CB_LIQUIDACION_DETALLE WHERE CBLIQUIDACIONID = ? ORDER BY TIPOVALOR";

	public static final String CONSULTA_REPORTE_LIQUIDACIONES = "SELECT NOMBTRANSACCION, to_char(FECHATRANSACCION,'DD-MM-YYYY') FechaTransaccion, TIPOVALOR, TIPOPAGO, "
			+ "TIP_CODTARJETA, DESCTIPOTARJETA, TOTAL FROM CB_REPORTE_LIQUIDACIONES_VW ";

	public static final String ASOCIACION_MANUAL = "UPDATE CB_ESTADO_CUENTA SET CBCATALOGOAGENCIAID = ?, OBSERVACIONES = ? WHERE CBESTADOCUENTAID = ?";

	public static final String DESASOCIACION_MANUAL = "UPDATE CB_ESTADO_CUENTA SET CBCATALOGOAGENCIAID = null, OBSERVACIONES = null WHERE CBESTADOCUENTAID = ?";

	public static final String DESASOCIACION_TIPOLOGIA_MASIVA = "UPDATE CB_ESTADO_CUENTA_SOCIEDAD SET CBTIPOLOGIASPOLIZAID = null, "
			+ "CBCATALOGOAGENCIAID = null, FECHA_INGRESOS = null, "
			+ "SWMODIFYBY = ?,  OBSERVACIONES = ? , SWDATEMODIFIED = sysdate WHERE CBESTADOCUENTASOCIEDADID = ?";

	public static final String DESASOCIACION_TIPOLOGIA = "UPDATE CB_ESTADO_CUENTA_SOCIEDAD SET CBTIPOLOGIASPOLIZAID = null, "
			+ "CBCATALOGOAGENCIAID = null, FECHA_INGRESOS = null, "
			+ "SWMODIFYBY = ?, OBSERVACIONES = ? ,  SWDATEMODIFIED = sysdate WHERE CBESTADOCUENTASOCIEDADID = ?";

	public static final String CONSULTA_ESTADO_CUENTA_TARJETA = "SELECT CBESTADOCUENTAID, TIPO_TARJETA, TO_CHAR(FECHA_TRANSACCION, 'DD/MM/YYYY') FECHA_TRANSACCION, "
			+ "AFILIACION, TIPO, REFERENCIA, LIQUIDO, COMISION, IVA_COMISION, RETENCION, CONSUMO, "
			+ "DECODE (NOMBRE, NULL, '(No asociada)', NOMBRE, NOMBRE) NOMBRE, OBSERVACIONES "
			+ "FROM CB_ESTADOS_TARJETA_VW WHERE 1=1 ";
	/*
	 * Agregado por CarlosGodinez - 29/03/2017 - QitCorp
	 * 
	 */

	// Este query se invoca en el m�todo generaConsultaReporte2() de la clase
	// CBReportesDAO
	public static final String CONSULTA_REPORTE_X_ENTIDAD = " SELECT DISTINCT A.DIA fecha,"
			+ " TO_NUMBER(TO_CHAR(A.FECHA,'MM')) mes_1,"
			+ " DECODE(TO_NUMBER(TO_CHAR(TO_DATE(A.FECHA,'dd/MM/yyyy'),'MM')),1,'ENERO',2,'FEBRERO',3,'MARZO',4,'ABRIL',5,'MAYO',6,'JUNIO',7,'JULIO',8,'AGOSTO',9,'SEPTIEMBRE',10,'OCTUBRE',11,'NOVIEMBRE',12,'DICIEMBRE') mes_2,"
			+ " TO_CHAR(A.FECHA,'HH24:mi:ss') hora," + " NVL(A.MONTO,0) monto," + " A.TELEFONO telefono,"
			+ " B.NOMBRE banco," + " DECODE(A.TIPO, '1', 'PRE-PAGO', '2', 'POST-PAGO') tipo_servicio,"
			+ " A. TRANSACCION secuencia," + " C.NOMBRE agencia,"
			+ " DECODE(D.TIPO,0,'VIRTUAL',1,'PRESENCIAL','PRESENCIAL') forma_de_pago,"
			+ " NVL((A.COMISION*A.MONTO),0) comision," + " NVL(A.COMISION,0) porcentaje, " + " A.CODIGO sucursal, "
			+ " D.NOMBRE nombre_sucursal, " + " 1 cantidad" + " FROM CB_DATA_BANCO A, " + " CB_CATALOGO_BANCO B, "
			+ " CB_CATALOGO_AGENCIA C, " + " CB_AGENCIA_VIR_FIS D, " + " CB_BANCO_AGENCIA_CONFRONTA E, "
			+ " CB_CONCILIACION F " + " WHERE A.CBCATALOGOBANCOID = B.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOBANCOID   = C.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = C.CBCATALOGOAGENCIAID " + " AND A.CODIGO              = D.CODIGO(+) "
			+ " AND A.CBCATALOGOBANCOID   = E.CBCATALOGOBANCOID "
			+ " AND A.CBCATALOGOAGENCIAID = E.CBCATALOGOAGENCIAID " + " AND A.CBDATABANCOID       = F.CBDATABANCOID "
			+ " AND A.DIA                 = F.DIA " + " AND A.CBCATALOGOAGENCIAID = F.CBCATALOGOAGENCIAID "
			+ " AND A.CBCATALOGOAGENCIAID = D.CBCATALOGOAGENCIAID ";

	/**
	 * Agrega Ovidio Santos 26/05/2017
	 */

	// este query es utilizado para insertar tipologias poliza
	public static final String INSERT_TIPOLOGIAS_POLIZA_SQ = "INSERT INTO CB_TIPOLOGIAS_POLIZA(CBTIPOLOGIASPOLIZAID,CENTRO_COSTO, "
			+ "  CUENTA_CONTRAPARTIDA, CLAVE_CONTABILIZACION, INDICADOR_IVA, TERMINACION, ACTIVIDAD, DESCRIPCION, NOMBRE, CENTRO_COSTO_CP,CLAVE_CONTABILIZACION_CP, DESCRIPCION_CP, TERMINACION_CP,CUENTA_INGRESO, CENTRO_COSTO_DF, CLAVE_CONTABILIZACION_DF, DESCRIPCION_DF, TERMINACION_DF, CUENTA_INGRESO_DF, CLAVE_CONTABILIZACION_NEG,INDICADOR_IVA_CP, ACTIVIDAD_CP, "
			+ "  CREADO_POR,TIPO ,PIDE_ENTIDAD, SECUENCIA , TIPO_DOCUMENTO, identificacion, TIPO_IMPUESTO, FECHA_CREACION) "
			+ " VALUES(CB_TIPOLOGIAS_POLIZA_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, sysdate) ";

	// este query es utilizado para modificar tipologias poliza
	public static final String MODIFICAR_POLIZA_SQ = "UPDATE CB_TIPOLOGIAS_POLIZA SET CENTRO_COSTO = ?, "
			+ "  CUENTA_CONTRAPARTIDA = ?, CLAVE_CONTABILIZACION = ?,INDICADOR_IVA = ?, TERMINACION =?, ACTIVIDAD=?,DESCRIPCION = ?,NOMBRE = ?,CENTRO_COSTO_CP=? ,CLAVE_CONTABILIZACION_CP =? , DESCRIPCION_CP= ?, TERMINACION_CP=? ,CUENTA_INGRESO =?, CENTRO_COSTO_DF = ?, CLAVE_CONTABILIZACION_DF =? , DESCRIPCION_DF = ?, TERMINACION_DF = ?, CUENTA_INGRESO_DF = ?,CLAVE_CONTABILIZACION_NEG = ?, INDICADOR_IVA_CP = ?,ACTIVIDAD_CP = ?, TIPO = ?, PIDE_ENTIDAD = ?, SECUENCIA= ?, TIPO_DOCUMENTO= ?, identificacion=? , TIPO_IMPUESTO = ? , MODIFICADO_POR = ?, FECHA_MODIFICACION = SYSDATE WHERE CBTIPOLOGIASPOLIZAID = ?";

	// este query es utilizado para consultar tipologias poliza
	public static final String CONSULTA_TIPOLOGIAS_POLIZA_SQ = "SELECT CBTIPOLOGIASPOLIZAID, CENTRO_COSTO,CLAVE_CONTABILIZACION, DESCRIPCION,  TERMINACION, CUENTA_INGRESO, NOMBRE,  "
			+ "  CENTRO_COSTO_CP,CLAVE_CONTABILIZACION_CP, DESCRIPCION_CP, TERMINACION_CP, CUENTA_CONTRAPARTIDA,INDICADOR_IVA, ACTIVIDAD, CENTRO_COSTO_DF, CLAVE_CONTABILIZACION_DF, DESCRIPCION_DF, TERMINACION_DF, CUENTA_INGRESO_DF, CLAVE_CONTABILIZACION_NEG,INDICADOR_IVA_CP, ACTIVIDAD_CP, (SELECT objeto FROM cb_modulo_conciliacion_conf  WHERE     UPPER (modulo) = 'CONFIGURACION_TIPO_CLIENTE' AND UPPER (tipo_objeto) = 'CONVENIOS' " + 
			" AND UPPER (estado) = 'S' AND valor_objeto1 = a.tipo) tipo, DECODE (PIDE_ENTIDAD ,  '1', 'SI',  '0', 'NO') PIDE_ENTIDAD,  "
			+ " CENTRO_DE_BENEFICIO,DIVISION, ORDEN_DE_PROYECTO,  TIPO_DE_CAMBIO, FECHA_DE_CONVERSION, INDICADOR_CME,  "
			+ "  CAR_PA_SEGMENTO,CAR_PA_SERVICIO, CAR_PA_TIPO_TRAFICO, CAR_PA_AMBITO, CAR_PA_LICENCIA,CAR_PA_REGION, SUBTIPO_LINEA, CANAL, BUNDLE, PRODUCTO, EMPRESA_GRUPO, SECUENCIA , TIPO_DOCUMENTO, identificacion , (SELECT objeto FROM cb_modulo_conciliacion_conf WHERE UPPER (modulo) = 'TIPOLOGIAS_POLIZA_TIPO_IMPUESTO' AND UPPER (tipo_objeto) = 'TIPO_IMPUESTO' AND valor_objeto1 = a.tipo_impuesto) tipo_impuesto , proyecto, sociedad_asociada, grafo1, grafo2, subsegmento, ref1, ref2, tcode, proc, llave, calc_auto_iva, ref3, sociedad, fecha_valor  "
			+ " FROM CB_TIPOLOGIAS_POLIZA a WHERE 1=1 ";

	// este query es utilizado para consultar tipologias poliza modal
	public static final String CONSULTA_TIPOLOGIAS_POLIZA_MODAL_SQ = "SELECT CBTIPOLOGIASPOLIZAID,nombre, CENTRO_DE_BENEFICIO,DIVISION, ORDEN_DE_PROYECTO,  TIPO_DE_CAMBIO, FECHA_DE_CONVERSION, INDICADOR_CME,  "
			+ "  CAR_PA_SEGMENTO,CAR_PA_SERVICIO, CAR_PA_TIPO_TRAFICO, CAR_PA_AMBITO, CAR_PA_LICENCIA,CAR_PA_REGION, SUBTIPO_LINEA, CANAL, BUNDLE, PRODUCTO, EMPRESA_GRUPO   "
			+ " FROM CB_TIPOLOGIAS_POLIZA  WHERE 1=1 ";

	// este query es utilizado para modificar tipologias poliza modal
	public static final String MODIFICAR_POLIZA_MODAL_SQ = "UPDATE CB_TIPOLOGIAS_POLIZA SET CENTRO_DE_BENEFICIO = ?, "
			+ "  DIVISION = ?, ORDEN_DE_PROYECTO = ?,TIPO_DE_CAMBIO = ?, FECHA_DE_CONVERSION =?, INDICADOR_CME=?,CAR_PA_SEGMENTO = ?,CAR_PA_SERVICIO = ?,CAR_PA_TIPO_TRAFICO=? ,CAR_PA_AMBITO =? , CAR_PA_LICENCIA= ?, CAR_PA_REGION=? ,SUBTIPO_LINEA =?, CANAL = ?, BUNDLE =? , PRODUCTO = ?, EMPRESA_GRUPO = ?, proyecto = ?, sociedad_asociada = ?, grafo1 = ?, grafo2 = ?, subsegmento = ?, ref1 = ?, ref2 = ?, tcode = ?, proc = ?, llave = ?, calc_auto_iva = ?, ref3 = ? ,sociedad=?,fecha_valor=?,  MODIFICADO_POR = ?, FECHA_MODIFICACION = SYSDATE WHERE CBTIPOLOGIASPOLIZAID = ?";

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
	/*public static final String OBTIENE_DATOS_SAP = "SELECT (SELECT valor_objeto1 "
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
			+ "   AND fecha >= TO_DATE (?, 'dd/MM/yyyy') " + "   AND fecha <= TO_DATE (?, 'dd/MM/yyyy') ";*/

	//
	public static final String OBTIENE_DATOS_SAP = "SELECT CBCONTABILIZACIONID, cbestadocuentaid, (SELECT valor_objeto1 "
			+ "             FROM cb_modulo_conciliacion_conf "
			+ "            WHERE modulo = 'CONSULTA_CONTABILIZACION' "
			+ "              AND cbmoduloconciliacionconfid = 38) "
			+ "       || RPAD (NVL (secuencia, ' '), 6, ' ')    " + "       || '000' "
			+ "       || RPAD (NVL (TO_CHAR (fecha, 'YYYYMMDD'), ' '), 8, ' ') "
			+ "       || RPAD (NVL (TO_CHAR (fecha_ingresos, 'YYYYMMDD'), ' '), 8, ' ') "
			+ "       || RPAD (NVL (texto, ' '), 25, ' ') " + "       || RPAD (NVL (referencia, ' '), 16, ' ') "
			+ "       || RPAD (nvl(clave_contabilizacion, ' '), 2, ' ') " + "       || RPAD (nvl(cuenta,' '), 10, ' ') "
			+ "       || RPAD (nvl(centro_costo,' '), 10, ' ') "
			+ "       || RPAD (NVL (CENTRO_DE_BENEFICIO, ' '), 10, ' ')    "
			+ "       || RPAD (NVL (division, ' '), 4, ' ') "
			+ "       || RPAD (NVL (orden_de_proyecto, ' '), 12, ' ') "
			+ "       || (SELECT RPAD (NVL(moneda,' '),5,' ') FROM CB_CATALOGO_AGENCIA "
			+ "            WHERE CBCATALOGOAGENCIAID =cb_poliza_contable_vw.CBCATALOGOAGENCIAID ) "
			+ "       || RPAD (NVL (tipo_de_cambio, ' '), 9, ' ')    "
			+ "       || RPAD (NVL (fecha_de_conversion, ' '), 8, ' ') "
			+ " 	|| RPAD (NVL (TO_CHAR (haber), ' '), 12, ' ') " + "       || RPAD (NVL (texto2, ' '), 50, ' ') "
			+ "       || RPAD (nvl(indicador_iva, ' '), 2, ' ') "
			+ "       || RPAD (NVL (indicador_cme, ' '), 2, ' ')   "
			+ "       || RPAD (NVL (car_pa_segmento, ' '), 2, ' ')   "
			+ "       || RPAD (NVL (car_pa_servicio, ' '), 3, ' ')    "
			+ "       || RPAD (NVL (car_pa_tipo_trafico, ' '), 2, ' ')    "
			+ "       || RPAD (NVL (car_pa_ambito, ' '), 2, ' ')   "
			+ "       || RPAD (NVL (car_pa_licencia, ' '), 2, ' ')  "
			+ "       || RPAD (NVL (car_pa_region, ' '), 2, ' ')   " + "       || RPAD (NVL (TIPO_DOCUMENTO, ' '), 2, ' ') " + "       || (RPAD (nvl(actividad, ' '), 10, ' ')) "
			+ "       || RPAD (NVL (subtipo_linea, ' '), 2, ' ')    " + "       || RPAD (NVL (canal, ' '), 2, ' ')    "
			+ "       || RPAD (NVL (bundle, ' '), 2, ' ')   " + "       || RPAD (NVL (producto, ' '), 4, ' ')    "
			+ "       || RPAD (NVL (empresa_grupo, ' '), 4, ' ')sap " + "  FROM cb_poliza_contable_vw "
			+ " WHERE 1 = 1 ";
	//para prueba
	public static final String OBTIENE_DATOS_SAP2 = "SELECT CBCONTABILIZACIONID, \r\n" + 
			"		cbestadocuentaid,\r\n" + 
			"		nvl(clave_contabilizacion, '/') || chr(9)\r\n" + 
			"		|| nvl(indicador_cme,'/') || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(TO_CHAR (haber/100, 'fm99999999990.00'),'/'),0,16) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(TO_CHAR (haber/100, 'fm99999999990.00'),'/'),0,16) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(indicador_iva,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(centro_costo,'/'),0,10) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(orden_de_proyecto, '/'),0,12) || chr(9)\r\n" + 
			"		|| '/' || chr(9) \r\n" + 
			"		|| SUBSTR(nvl(asignacion,'/'),0,18) ||chr(9) \r\n" + 
			"		|| SUBSTR(nvl(texto2,'/'),0,50) || chr(9)\r\n" + 
			"		|| '/' || chr(9) \r\n" + 
			"		|| '/' || chr(9) \r\n" + 
			"		|| '/' || chr(9) \r\n" + 
			"		|| SUBSTR(nvl(banco_propio,'/'),0,5) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(cuenta,'/'),0,17) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(ref1,'/'),0,12) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(referenciasap,'/'),0,15) || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| nvl((sociedad),'/') || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(car_pa_region,'/'),0,3) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(car_pa_licencia,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(car_pa_tipo_trafico,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(car_pa_ambito,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(producto,'/'),0,4) || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(empresa_grupo,'/'),0,4) || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(actividad,'/'),0,16) || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(to_char(fecha_valor,'DDMMYYYY'),''),0,8) || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(proyecto,'/'),0,24) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(sociedad_asociada,'/'),0,6) || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| '/' || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(grafo1,'/'),0,12) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(grafo2,'/'),0,12) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(subsegmento,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(bundle,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(subtipo_linea,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(canal,'/'),0,2) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(car_pa_servicio,'/'),0,3) || chr(9)\r\n" + 
			"		|| SUBSTR(nvl(car_pa_segmento,'/'),0,2) || chr(9)\r\n" + 
			"		|| '/' || chr(9) sap,\r\n" + 
			"		(nvl(tcode,'FB01') || chr(9)\r\n" + 
			"			||SUBSTR(nvl(tipo_documento,'/'),0,2)  || chr(9)\r\n" + 
			"			|| nvl((SELECT valor_objeto1 \r\n" + 
			"	             FROM cb_modulo_conciliacion_conf \r\n" + 
			"	            WHERE modulo = 'CONSULTA_CONTABILIZACION' \r\n" + 
			"	              AND cbmoduloconciliacionconfid = 38),'/') || chr(9)\r\n" + 
			"	        || nvl((SELECT SUBSTR(moneda,0,3) FROM CB_CATALOGO_AGENCIA \r\n" + 
			"	            WHERE CBCATALOGOAGENCIAID =cb_poliza_contable_vw.CBCATALOGOAGENCIAID ),'/') || chr(9)\r\n" + 
			"	        || SUBSTR(nvl(tipo_de_cambio,'/'),0,9) || chr(9)\r\n" + 
			"	        || SUBSTR(nvl(to_char(fecha_ingresos,'DDMMYYYY'),''),0,8) || chr(9)\r\n" + 
			"	        || SUBSTR(nvl(to_char(fecha,'DDMMYYYY'),''),0,8) || chr(9)\r\n" + 
			"	        || '/' || chr(9)\r\n" + 
			"	        || SUBSTR(nvl(texto,'/'),0,25) || chr(9)\r\n" + 
			"	        || '/' || chr(9)\r\n" + 
			"	        || '/' || chr(9)\r\n" + 
			"	        || SUBSTR(nvl(referencia,'/'),0,16) || chr(9)\r\n" + 
			"	        || SUBSTR(nvl(agencia,'/'),0,16) || chr(9)\r\n" + 
			"	        || nvl(proc,'31') || chr(9)\r\n" + 
			"	        || 'secuencia' || chr(9)\r\n" + 
			"	        || nvl(llave,'/') || chr(9)\r\n" + 
			"	        || nvl(calc_auto_iva,'/') || chr(9)\r\n" + 
			"		) lineaEncabezado, CBCATALOGOAGENCIAID\r\n" + 
			"		FROM cb_poliza_contable_vw \r\n" + 
			" 		WHERE 1 = 1 ";
	/*public static final String OBTIENE_DATOS_SAP2 = "SELECT CBCONTABILIZACIONID, cbestadocuentaid, (SELECT valor_objeto1 "
			+ "             FROM cb_modulo_conciliacion_conf "
			+ "            WHERE modulo = 'CONSULTA_CONTABILIZACION' "
			+ "              AND cbmoduloconciliacionconfid = 38) "
			+ "       || RPAD (NVL (secuencia, ' '), 6, ' ')    " + "       || '000' "
			+ "       || RPAD (NVL (TO_CHAR (fecha, 'YYYYMMDD'), ' '), 8, ' ') "
			+ "       || RPAD (NVL (TO_CHAR (fecha_ingresos, 'YYYYMMDD'), ' '), 8, ' ') "
			+ "       || RPAD (NVL (texto, ' '), 25, ' ') " + "       || RPAD (NVL (referencia, ' '), 16, ' ') "
			+ "       || RPAD (nvl(clave_contabilizacion, ' '), 2, ' ') " + "       || RPAD (nvl(cuenta,' '), 20, ' ') "
			+ "       || RPAD (nvl(centro_costo,' '), 10, ' ') "
			+ "       || RPAD (NVL (CENTRO_DE_BENEFICIO, ' '), 10, ' ')    "
			+ "       || RPAD (NVL (division, ' '), 4, ' ') "
			+ "       || RPAD (NVL (orden_de_proyecto, ' '), 12, ' ') "
			+ "       || (SELECT RPAD (NVL(moneda,' '),5,' ') FROM CB_CATALOGO_AGENCIA "
			+ "            WHERE CBCATALOGOAGENCIAID =cb_poliza_contable_vw.CBCATALOGOAGENCIAID ) "
			+ "       || RPAD (NVL (tipo_de_cambio, ' '), 9, ' ')    "
			+ "       || RPAD (NVL (TO_CHAR(fecha_de_conversion,'DD/MM/YYYY'), ' '), 8, ' ') "
			+ " 	|| RPAD (NVL (TO_CHAR (haber), ' '), 12, ' ') " + "       || RPAD (NVL (texto2, ' '), 50, ' ') "
			+ "       || RPAD (nvl(indicador_iva, ' '), 2, ' ') "
			+ "       || RPAD (NVL (indicador_cme, ' '), 2, ' ')   "
			+ "       || RPAD (NVL (car_pa_segmento, ' '), 2, ' ')   "
			+ "       || RPAD (NVL (car_pa_servicio, ' '), 3, ' ')    "
			+ "       || RPAD (NVL (car_pa_tipo_trafico, ' '), 2, ' ')    "
			+ "       || RPAD (NVL (car_pa_ambito, ' '), 2, ' ')   "
			+ "       || RPAD (NVL (car_pa_licencia, ' '), 2, ' ')  "
			+ "       || RPAD (NVL (car_pa_region, ' '), 2, ' ')   " + "       || RPAD (NVL (TIPO_DOCUMENTO, ' '), 2, ' ') " + "       || (RPAD (nvl(actividad, ' '), 10, ' ')) "
			+ "       || RPAD (NVL (subtipo_linea, ' '), 2, ' ')    " + "       || RPAD (NVL (canal, ' '), 2, ' ')    "
			+ "       || RPAD (NVL (bundle, ' '), 2, ' ')   " + "       || RPAD (NVL (producto, ' '), 4, ' ')    "
			+ "       || RPAD (NVL (empresa_grupo, ' '), 4, ' ')sap, CBCATALOGOAGENCIAID " + "   FROM cb_poliza_contable_vw "
			+ " WHERE 1 = 1 ";*/

	public static final String OBTIENE_DATOS_SAP3 = "SELECT CBCONCILIACIONID, (SELECT RPAD (valor_objeto1, 21, '0') FROM cb_modulo_conciliacion_conf\r\n"
			+ "WHERE modulo = 'APLICA_DESAPLICA_PAGOS' AND TIPO_OBJETO = 'PARAMETRO_APLICA') "
			+ "       || RPAD (NVL (REPLACE (monto,'.',''), '0'), 9, '0')  "
			+ "       ||(SELECT RPAD (valor_objeto1, 4, '0') FROM cb_modulo_conciliacion_conf"
			+ "          WHERE modulo = 'APLICA_DESAPLICA_PAGOS' AND TIPO_OBJETO = 'PARAMETRO_APLICA2') "
			+ "       ||  LPAD (NVL (CLIENTE, '0'), 10, '0')    "
			+ "       ||(SELECT RPAD (valor_objeto1, 1, ' ') FROM cb_modulo_conciliacion_conf "
			+ "          WHERE modulo = 'APLICA_DESAPLICA_PAGOS' AND TIPO_OBJETO = 'PARAMETRO_APLICA3') "
			+ "       || RPAD (NVL (trans_telca, '0'), 9, '0')sap " + "  FROM cb_conciliacion_detail_vw "
			+ " 		WHERE 1 = 1 ";

	// este query es utilizado para depositos rec
	public static final String CONSULTA_DEPOSITOS_REC_SQ = "SELECT a.CBDEPOSITOSRECID, TEXTO,b.nombre nombre_entidad, a.CBCATALOGOAGENCIAID,c.nombre nombre_tipologia ,  a.CBTIPOLOGIASPOLIZAID,DECODE(a.TIPO_FECHA,0,'DEPOSITO DIA ANTERIOR',1,'FECHA DEL DEPOSITO',2, 'FECHA DEL SIGUIENTE DIA', 4,'SIN FECHA DEPOSITO')nombreTipoFecha, "
			+ " a.TIPO_FECHA FROM cb_depositos_rec a , cb_catalogo_agencia b , cb_tipologias_poliza c   WHERE  b.cbcatalogoagenciaid = a.CBCATALOGOAGENCIAID   AND a.CBTIPOLOGIASPOLIZAID= c.CBTIPOLOGIASPOLIZAID  ";

	// este query es utilizado para insertar depositos rec
	public static final String INSERT_DEPOSITOS_REC_SQ = "INSERT INTO cb_depositos_rec(CBDEPOSITOSRECID,CBCATALOGOAGENCIAID, "
			+ "  TEXTO, CBTIPOLOGIASPOLIZAID,TIPO_FECHA,CREADO_POR, FECHA_CREACION) "
			+ " VALUES(CB_DEPOSITOS_REC_SQ.NEXTVAL,?,?,?,?,?, sysdate) ";

	// este query es utilizado para modificar tipologias poliza
	public static final String MODIFICAR_DEPOSITO_REC_SQ = "UPDATE cb_depositos_rec SET CBCATALOGOAGENCIAID = ?, "
			+ "  TEXTO = ?, CBTIPOLOGIASPOLIZAID = ?,TIPO_FECHA = ?, MODIFICADO_POR =?, FECHA_MODIFICACION = SYSDATE WHERE CBDEPOSITOSRECID = ?";

	// Query para la carga de confrontas
	public static final String CONSULTA_AGENCIAS_DEPOSITOS_REC = "SELECT DISTINCT a.cbcatalogoagenciaid cbcatalogoagenciaid,  a.cbcatalogobancoid cbcatalogobancoid, b.nombre nombrebanco,  (a.codigo_colector || ' - ' || a.nombre) nombre, a.telefono telefono, a.direccion direccion,    "
			+ "(SELECT nombre FROM cb_catalogo_opcion b       WHERE tipo = 'ESTADO' AND a.estado = b.valor) estado,      a.creado_por creadopor, a.fecha_creacion fechacreacion , a.codigo_colector   FROM cb_catalogo_agencia a,     cb_catalogo_banco b    WHERE a.cbcatalogobancoid = b.cbcatalogobancoid "
			+ "AND a.estado = 1 AND a.cuenta_contable IS NULL  ";

	// metodo para eliminar tipologia poliza
	public static final String DELETE_DEPOSITOS_REC_SQ = "DELETE FROM cb_depositos_rec WHERE CBDEPOSITOSRECID = ?";

	// este query es utilizado para impuestos
	public static final String CONSULTA_IMPUESTOS_SQ = "SELECT A.cbcomisionesconfiguracionid, A.CBBANCOAGENCIACONFRONTAID, A.CBMODULOCONCILIACIONCONFID, "
			+ "c.OBJETO, A.TIPO, (SELECT OBJETO FROM CB_MODULO_CONCILIACION_CONF WHERE MODULO = 'CALCULO_COMISION' "
			+ "AND TIPO_OBJETO = 'TIPO' AND VALOR_OBJETO1 = A.TIPO) TIPO_DESC, A.MEDIO_PAGO, (SELECT OBJETO "
			+ "FROM CB_MODULO_CONCILIACION_CONF WHERE MODULO = 'CALCULO_COMISION' AND TIPO_OBJETO = 'MEDIO_PAGO' "
			+ "AND VALOR_OBJETO1 = A.MEDIO_PAGO) MEDIO_PAGO_DESC, B.NOMBRE NOMBRE_TIPOLOGIA, A.TIPOLOGIA, "
			+ "A.FORMA_PAGO, (SELECT OBJETO FROM CB_MODULO_CONCILIACION_CONF WHERE MODULO = 'CALCULO_COMISION' "
			+ "AND TIPO_OBJETO = 'FORMA_PAGO' AND VALOR_OBJETO1 = A.FORMA_PAGO) FORMA_PAGO_DESC, A.VALOR "
			+ "FROM cb_comisiones_configuracion A, CB_TIPOLOGIAS_POLIZA B, CB_MODULO_CONCILIACION_CONF c "
			+ "WHERE A.TIPOLOGIA = B.CBTIPOLOGIASPOLIZAID "
			+ "AND a.CBMODULOCONCILIACIONCONFID = c.CBMODULOCONCILIACIONCONFID AND COMISION_USO = 1 ";

	// este query es utilizado para insertar impuestos
	public static final String INSERT_IMPUESTOS_SQ = "INSERT INTO cb_comisiones_configuracion(cbcomisionesconfiguracionid,CBBANCOAGENCIACONFRONTAID, "
			+ "  CBMODULOCONCILIACIONCONFID, TIPO,MEDIO_PAGO, TIPOLOGIA,FORMA_PAGO, VALOR , COMISION_USO ,CREATEDBY, DATECREATED) "
			+ " VALUES(CB_COMISIONES_CONFIGURACION_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?, sysdate) ";

	// este query es utilizado para modificar impuesto
	public static final String MODIFICAR_IMPUESTOS_SQ = "UPDATE cb_comisiones_configuracion SET "
			+ "  CBMODULOCONCILIACIONCONFID = ?, TIPO = ?,MEDIO_PAGO = ?,TIPOLOGIA=?, FORMA_PAGO=?, VALOR=? , COMISION_USO =? ,  CREATEDBY =?, DATECREATED = SYSDATE WHERE cbcomisionesconfiguracionid = ?";

	// metodo para eliminar impuesto
	public static final String DELETE_IMPUESTOS_SQ = "DELETE FROM cb_comisiones_configuracion WHERE cbcomisionesconfiguracionid = ?";

	// este query es utilizado para impuestos
	public static final String CONSULTA_IMPUESTOS_TIENDAS_PROPIAS_SQ = "SELECT A.cbcomisionesconfiguracionid,A.cbbancoagenciaafiliacionesid,A.CBMODULOCONCILIACIONCONFID,c.OBJETO ,"
			+ "                   A.TIPO,   decode (A.TIPO, 1,'PORCENTAJE/TOTAL', 2, 'PORCENTAJE/COMISION', 3, 'MONTO FIJO/TOTAL' ) TIPO, A.MEDIO_PAGO, decode (A.MEDIO_PAGO, 1,'VIRTUAL', 2, 'PRESENCIAL', 3, 'AMBAS' ) MEDIO_PAGO,B.NOMBRE NOMBRE_TIPOLOGIA, A.TIPOLOGIA, A.FORMA_PAGO, decode (A.FORMA_PAGO, 1,'DESCONTADA', 2, 'DESCONTADA' ) FORMA_PAGO , a.valor "
			+ "                  FROM cb_comisiones_configuracion A, CB_TIPOLOGIAS_POLIZA B , CB_MODULO_CONCILIACION_CONF c "
			+ "                 WHERE 1 = 1 AND A.TIPOLOGIA = B.CBTIPOLOGIASPOLIZAID  and a.CBMODULOCONCILIACIONCONFID = c.CBMODULOCONCILIACIONCONFID  AND COMISION_USO = 2 ";

	// este query es utilizado para insertar impuestos
	public static final String INSERT_IMPUESTOS_TIENDAS_PROPIAS_SQ = "INSERT INTO cb_comisiones_configuracion(cbcomisionesconfiguracionid,cbbancoagenciaafiliacionesid, "
			+ "  CBMODULOCONCILIACIONCONFID, TIPO,MEDIO_PAGO, TIPOLOGIA,FORMA_PAGO, VALOR , COMISION_USO ,CREATEDBY, DATECREATED) "
			+ " VALUES(CB_COMISIONES_CONFIGURACION_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?, sysdate) ";

	// este query es utilizado para insertar impuestos
		public static final String INSERT_IMPUESTOS_TIENDAS_PROPIAS_TIENDAS_SQ = "INSERT INTO cb_comisiones_configuracion(cbcomisionesconfiguracionid,cbbancoagenciaafiliacionesid, "
				+ "  CBMODULOCONCILIACIONCONFID, TIPO,MEDIO_PAGO, TIPOLOGIA,FORMA_PAGO, VALOR , COMISION_USO ,CREATEDBY, DATECREATED) "
				+ " VALUES(CB_COMISIONES_CONFIGURACION_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?, sysdate) ";

	
	// este query es utilizado para modificar impuesto
	public static final String MODIFICAR_IMPUESTOS_TIENDAS_PROPIAS_SQ = "UPDATE cb_comisiones_configuracion SET "
			+ "  CBMODULOCONCILIACIONCONFID = ?, TIPO = ?,MEDIO_PAGO = ?,TIPOLOGIA=?, FORMA_PAGO=?, VALOR=? , COMISION_USO =? ,  CREATEDBY =?, DATECREATED = SYSDATE WHERE cbcomisionesconfiguracionid = ?";

	// metodo para eliminar impuesto
	public static final String DELETE_IMPUESTOS_TIENDAS_PROPIAS_SQ = "DELETE FROM cb_comisiones_configuracion WHERE cbcomisionesconfiguracionid = ?";

	// metodo para eliminar impuesto
	public static final String DELETE_IMPUESTOS_TIENDAS_PROPIAS_TIENDAS_SQ = "DELETE FROM cb_comisiones_configuracion WHERE cbbancoagenciaafiliacionesid = ?";
	
	// metodo para modificar
	// este query es utilizado para modificar tipologias poliza modal
	public static final String MODIFICAR_depositos_masivos_SQ = "UPDATE CB_TIPOLOGIAS_POLIZA SET CENTRO_DE_BENEFICIO = ?, "
			+ "  DIVISION = ?, ORDEN_DE_PROYECTO = ?,TIPO_DE_CAMBIO = ?, FECHA_DE_CONVERSION =?, INDICADOR_CME=?,CAR_PA_SEGMENTO = ?,CAR_PA_SERVICIO = ?,CAR_PA_TIPO_TRAFICO=? ,CAR_PA_AMBITO =? , CAR_PA_LICENCIA= ?, CAR_PA_REGION=? ,SUBTIPO_LINEA =?, CANAL = ?, BUNDLE =? , PRODUCTO = ?, EMPRESA_GRUPO = ?,  MODIFICADO_POR = ?, FECHA_MODIFICACION = SYSDATE WHERE CBTIPOLOGIASPOLIZAID = ?";

	/**
	 * VARIABLES PARA USO EN CLASE: CBHistorialSCECDAO
	 * 
	 **/
	public static final String OBTIENE_HISTORIAL_TIPIFICACION_SQ = "SELECT "
			+ "	a.CBHISTORIALSCECID, a.CBESTADOCUENTASOCIEDADID, a.CBPAGOSID,"
			+ "	a.CBBANCOAGENCIACONFRONTAID, a.CBCAUSASCONCILIACIONID,"
			+ "	b.CAUSAS, NVL(a.MONTO, 0), a.FECHA, a.OBSERVACION, a.CREADO_POR, "
			+ "	a.MODIFICADO_POR, a.FECHA_CREACION, a.FECHA_MODIFICACION " + "FROM"
			+ "	CB_HISTORIAL_SCEC a, cb_causas_conciliacion b " + "WHERE "
			+ "   a.CBCAUSASCONCILIACIONID = b.ID_CAUSAS_CONCILIACION" + "	AND a.CBBANCOAGENCIACONFRONTAID = ?"
			+ "	AND FECHA = TO_DATE(?,'dd/MM/yyyy')";

	/**
	 * VARIABLES PARA USO EN CLASE: CBHistorialSCECDAO
	 * 
	 **/
	//public static final String OBTIENE_DETALLE_COMISION_SQ = "SELECT TIPO, NVL(SUM(MONTO), 0) FROM cb_comisiones WHERE "
//			+ " CBBANCOAGENCIACONFRONTAID = ? AND FECHA = TO_DATE(?,'dd/MM/yyyy') GROUP BY tipo";
	
	public static final String OBTIENE_DETALLE_COMISION_SQ = "SELECT (SELECT OBJETO " + 
			"            FROM CB_MODULO_CONCILIACION_CONF " + 
			"           WHERE     MODULO = 'CALCULO_COMISION' " + 
			"                 AND TIPO_OBJETO = 'FORMA_PAGO' " + 
			"                 AND VALOR_OBJETO1 = c.FORMA_PAGO) FORMA_PAGO, " + 
			"            (SELECT OBJETO " + 
			"            FROM CB_MODULO_CONCILIACION_CONF " + 
			"           WHERE     MODULO = 'CALCULO_COMISION' " + 
			"                 AND TIPO_OBJETO = 'TIPO' " + 
			"                 AND VALOR_OBJETO1 = a.TIPO)" + 
			"            TIPO," + 
			"            (SELECT nombre " + 
			"            FROM CB_Tipologias_poliza " + 
			"           WHERE    " + 
			"                 CBTIPOLOGIASPOLIZAID  = c.TIPOLOGIA)" + 
			"            TIPOlogia, " + 
			"            (SELECT OBJETO " + 
			"            FROM CB_MODULO_CONCILIACION_CONF " + 
			"           WHERE     MODULO = 'CALCULO_COMISION' " + 
			"                 AND TIPO_OBJETO = 'CARGO' " + 
			"                 AND CBMODULOCONCILIACIONCONFID = A.CBMODULOCONCILIACIONCONFID) CARGO, " + 
			"                 (SELECT OBJETO " + 
			"            FROM CB_MODULO_CONCILIACION_CONF " + 
			"           WHERE     MODULO = 'CALCULO_COMISION' " + 
			"                 AND TIPO_OBJETO = 'MEDIO_PAGO' " + 
			"                 AND VALOR_OBJETO1 = c.MEDIO_PAGO) MEDIO_PAGO, " + 
			"         NVL (SUM (a.MONTO), 0) " + 
			"    FROM cb_comisiones a, CB_MODULO_CONCILIACION_CONF b, CB_COMISIONES_CONFIGURACION c " + 
			"   WHERE     1 = 1 " + 
			"         AND a.CBMODULOCONCILIACIONCONFID = b.CBMODULOCONCILIACIONCONFID" + 
			"         and a.CBMODULOCONCILIACIONCONFID = c.CBMODULOCONCILIACIONCONFID" + 
			"         and c.CBMODULOCONCILIACIONCONFID = b.CBMODULOCONCILIACIONCONFID" + 
			"         and c.CBBANCOAGENCIACONFRONTAID = a.CBBANCOAGENCIACONFRONTAID   AND  A.CBPAGOSID is not null  AND a.TIPO = C.TIPO  " + 
			"          AND  A.CBBANCOAGENCIACONFRONTAID =? " + 
			"          AND A.FECHA = TO_DATE(?,'dd/MM/yyyy') " + 
			"GROUP BY a.tipo, c.FORMA_PAGO, c.TIPOLOGIA, A.CBMODULOCONCILIACIONCONFID, c.MEDIO_PAGO ";
	
	public static final String OBTIENE_CAUSAS_CONCILIACION_SQ = "SELECT ID_CAUSAS_CONCILIACION,CAUSAS,CREADO_POR,FECHA_CREACION,"
			+ "	CODIGO_CONCILIACION,TIPO " + "FROM cb_causas_conciliacion " + "ORDER BY CAUSAS ASC";

	
	public static final String OBTIENE_CAUSAS_CONCILIACION_BANCOS_SQ = "SELECT ID_CAUSAS_CONCILIACION,CAUSAS,CREADO_POR,FECHA_CREACION,"
			+ "	CODIGO_CONCILIACION,TIPO " + "FROM cb_causas_conciliacion  WHERE TIPO = 2 " + "ORDER BY CAUSAS ASC";

	
	public static final String OBTIENE_CAUSAS_CONCILIACION_DETALLADA_SQ = "SELECT ID_CAUSAS_CONCILIACION,CAUSAS,CREADO_POR,FECHA_CREACION,"
			+ "	CODIGO_CONCILIACION,TIPO,SISTEMA " + "FROM cb_causas_conciliacion  WHERE CONVENIO = ? AND TIPO = 1 " + "ORDER BY CAUSAS ASC";
	
	public static final String INSERT_CAUSA_TIPIFICACION_QY = "INSERT INTO CB_HISTORIAL_SCEC(CBHISTORIALSCECID,CBBANCOAGENCIACONFRONTAID,CBCAUSASCONCILIACIONID,\n"
			+ "	MONTO,FECHA,OBSERVACION,CREADO_POR,FECHA_CREACION) VALUES\n"
			+ "( CB_HISTORIAL_SCEC_SQ.NEXTVAL,?,?,?,TO_DATE(?,'dd/MM/yyyy'),?,?,sysdate)";

	public static final String UPDATE_CAUSA_TIPIFICACION_QY = "UPDATE CB_HISTORIAL_SCEC SET CBCAUSASCONCILIACIONID = ?, MONTO = ?, OBSERVACION = ?, "
			+ "	MODIFICADO_POR = ?,  FECHA_MODIFICACION = SYSDATE " + "WHERE CBHISTORIALSCECID = ?";

	public static final String ELIMINAR_CAUSAS_CONCILIACION_QY = "DELETE FROM CB_HISTORIAL_SCEC WHERE CBHISTORIALSCECID = ? ";

	/**
	 * VARIABLES PARA USO EN CLASE: CBRecaReguDAO
	 */
	//public static final String CONSULTA_RECAREGU_PAGOS_SQ = " SELECT * FROM (SELECT a.*, ROWNUM rnum "
	//		+ "FROM (  SELECT CBPAGOSID, COD_CLIENTE, FECHA, NOM_CLIENTE, TRANSACCION, NOM_USUARORA, IMP_PAGO "
	//		+ "FROM cb_pagos ";
	
	public static final String CONSULTA_RECAREGU_PAGOS_SQ = "SELECT cbpagosid, to_char(fecha_efectiva,'dd/MM/yyyy'), "
			+ "to_char(fecha_pago,'dd/MM/yyyy'), cod_cliente, "
			+ "nom_cliente, num_secuenci, imp_pago, transaccion, tipo_transaccion, cod_oficina, des_oficina, "
			+ "tip_movcaja, des_movcaja, tip_valor, des_tipvalor, nom_usuarora, cod_banco, banco, cod_caja, "
			+ "caja, observacion FROM cb_recaudacion_reporte_vw WHERE 1=1 ";

	public static final String ENTIDADES_RECAREGU_QRY = "select a. cbcatalogoagenciaid, a.nombre "
			+ "from cb_catalogo_agencia a, cb_catalogo_banco b " + "where upper(b.nombre) like '%TIENDA%' "
			+ "and a.cbcatalogobancoid = b.cbcatalogobancoid";

	public static final String USUARIOS_RECAREGU_QRY = "select cajero from cb_banco_agencia_cajas "
			+ "where cbcatalogoagenciaid = ?";

	public static final String UPDATE_PAGO_RECAREGU_QR1 = "UPDATE CB_PAGOS SET NOM_USUARORA = ? WHERE CBPAGOSID = ?";

	public static final String UPDATE_PAGO_RECAREGU_QR2 = "UPDATE CB_PAGOS SET COD_OFICINA = 'NT', "
			+ "COD_CAJA = ? WHERE CBPAGOSID = ?";

	
	public static final String BANCOS_RECAREGU_QRY = "SELECT b.cbcatalogoagenciaid, b.nombre , MIN(c.cod_agencia) cod_agencia "
			+ "FROM cb_banco_agencia_confronta a, cb_catalogo_agencia b, cb_agencias_confronta c "
			+ "WHERE a.cbbancoagenciaconfrontaid = c.cbbancoagenciaconfrontaid "
			+ "AND a.cbcatalogoagenciaid = b.cbcatalogoagenciaid "
			+ "GROUP BY b.cbcatalogoagenciaid, b.nombre "
			+ "ORDER BY 2";
	
	/**
	 * Consulta resumen diario de conciliaciones 
	 * Agregado -> 10/08/2018
	 */
	public static final String RESUMEN_DIARIO_CONCILIACION_QRY = "SELECT dia, nombre, codigo_colector codigoColector, tipo, "
			+ "trans_telefonica transTelefonica, trans_banco transBanco, conciliadas, "
			+ "dif_trans difTransaccion, pagos_telefonica pagosTelefonica, confronta_banco confrontaBanco, "
			+ "automatica, manual_t manualTelefonica, manual_b manualBanco, "
			+ "pendiente, cbcatalogoagenciaid idAgencia, real_t, real_b FROM cb_conciliacion_vw where 1 = 1 ";
	
	/**
	 * VARIABLES PARA USO EN CLASE: DetalleLogDAO.java
	 */

	public static final String INSERT_ERROR_LOG_QY = "INSERT INTO CB_ERROR_LOG "
			+ " (CBERRORLOGID, CODIGO_ERROR, MENSAJE_ERROR, CREADO_POR, FECHA_CREACION, MODULO, DESCRIPCION, OBJETO) "
			+ " VALUES(cb_error_log_sq.NEXTVAL, ?,?,?,SYSDATE,?,?,?)";
		
	/**
	 * VARIABLES PARA USO EN CLASE CBMantenimientoTipologiasPolizaDAO
	 * */
	public static final String AGRUPACIONES_TIPOL_ENTIDAD_QRY = "select cbcatalogobancoid, nombre from "
			+ "cb_catalogo_banco where estado = 1 order by cbcatalogobancoid";
	
	public static final String ENTIDADES_TIPOL_ENTIDAD_QRY = "select cbcatalogoagenciaid, (codigo_colector || ' - ' || nombre)  from "
			+ "cb_catalogo_agencia where estado = 1 and cbcatalogobancoid = ? order by nombre";
	
	public static final String CONSULTA_TIPOL_ENTIDAD_QRY1 = "SELECT x.cbcatalogobancoid, x.nombre_banco, x.cbcatalogoagenciaid, "
			+ "x.nombre_agencia, x.count_tipologias FROM (  SELECT b.cbcatalogobancoid, b.nombre nombre_banco, a.cbcatalogoagenciaid, "
			+ "(a.codigo_colector || ' - ' || a.nombre) nombre_agencia, (SELECT COUNT (*) FROM cb_tipologias_agencia ta "
			+ "WHERE ta.cbcatalogoagenciaid = a.cbcatalogoagenciaid ";
	
	public static final String CONSULTA_TIPOL_ENTIDAD_QRY2 = ") count_tipologias FROM cb_catalogo_banco b, cb_catalogo_agencia a "
			+ "WHERE b.cbcatalogobancoid = a.cbcatalogobancoid "
			+ "AND a.estado = 1 AND a.cuenta_contable is null "
			+ "ORDER BY 2) x WHERE 1 = 1 ";
	
	public static final String INSERT_MASIVO_ENTIDAD_TIPOLOGIA = "INSERT INTO CB_TIPOLOGIAS_AGENCIA(CBTIPOLOGIASAGENCIAID, "
			+ "CBCATALOGOAGENCIAID, CBTIPOLOGIASPOLIZAID, CREATEBY, DATECREATED) VALUES(CB_TIPOLOGIAS_AGENCIA_SQ.nextval, ?, ?, ?, sysdate)";
	
	public static final String DELETE_ASOCIACION_ENTIDAD = "DELETE FROM CB_TIPOLOGIAS_AGENCIA WHERE CBTIPOLOGIASPOLIZAID = ? AND CBCATALOGOAGENCIAID = ?";
	
	/**
	 * Query para llenado de combo de TIPOLOGIAS en tipificacion en Estado de Cuenta
	 * */
	public static final String QRY_CMB_TIPOLOGIA_ENTIDADES = "SELECT CBTIPOLOGIASPOLIZAID, (CBTIPOLOGIASPOLIZAID || ' - ' || NOMBRE), "
			+ "DESCRIPCION, CREADO_POR, FECHA_CREACION, "
			+ "TIPO, PIDE_ENTIDAD FROM CB_TIPOLOGIAS_POLIZA ORDER BY CBTIPOLOGIASPOLIZAID";
	
	/**
	 * Query para llenado de combo de ENTIDADES en tipificacion en Estado de Cuenta
	 * */
	public static final String QRY_CMB_ENTIDADES_TIPOLOGIA = "select a.cbcatalogoagenciaid, (a.codigo_colector || ' - ' || a.nombre)  "
			+ "from cb_catalogo_agencia a, cb_tipologias_agencia ta "
			+ "where ta.cbcatalogoagenciaid = a.cbcatalogoagenciaid "
			+ "and ta.cbtipologiaspolizaid = ? "
			+ "and a.estado = 1 and a.cuenta_contable is null ORDER BY TO_NUMBER(a.codigo_colector) ASC ";
	
	/**
	 * Query general para Consultas de Estados de Cuenta
	 * */
	public static final String CONSULTA_ESTADO_CUENTAS_SQ = " select cbestadocuentasociedadid, banco, agencia, trim(cuenta) cuenta, asignacion, "
			+ "to_char(fecha,'DD/MM/YYYY') fecha, texto, debe, haber, identificador, "
			+ "CASE WHEN tipologia IS NULL THEN '(No asignada)' ELSE (CBTIPOLOGIASPOLIZAID || ' - ' || tipologia) END tipologia, "
			+ "CASE WHEN agencia_ingreso IS NOT NULL THEN (codigo_colector || ' - ' || agencia_ingreso) "
			+ "ELSE (CASE WHEN tipologia IS NOT NULL THEN 'N/A' ELSE '(No asignada)' END) END agencia_ingreso, texto_cab_doc, "
			+ "observaciones, to_char(fecha_ingresos, 'DD/MM/YYYY') fecha_ingresos,numdocumento , USUARIO_MODIFICA ,"
			+ " to_char(FECHA_MODIFICA, 'DD/MM/YYYY') FECHA_MODIFICA , CBTIPOLOGIASPOLIZAID , codigo_colector, IDAGENCIA_INGRESO "
			+ " from cb_estado_cuenta_vw " + " where 1 = 1 ";
	
	/**
	 * Added by CarlosGodinez
	 * Auditar cualquier accion realizada en SICCAM
	 * */
	public static final String INSERT_BITACORA_LOG = "INSERT INTO CB_BITACORA (CBBITACORAID, MODULO, TIPO_CARGA, "
			+ "NOMBRE_ARCHIVO, ACCION, CREATEDBY, DATECREATED) VALUES (CB_BITACORA_SQ.nextval, ?, ?, ?, ?, ?, sysdate)";
	
	public static final String UPDATE_LOG_THREAD = "UPDATE CB_BITACORA SET TIPO_CARGA = ? WHERE NOMBRE_ARCHIVO = ?";

	public static final String COUNT_LOG_THREAD = "SELECT COUNT(*) FROM CB_BITACORA WHERE MODULO = ? AND TIPO_CARGA >= '1'";
	
	public static final String DELETE_LOG_THREAD = "DELETE FROM CB_BITACORA WHERE CREATEDBY = ?";
	
	public static final String OBTIENE_DETALLE_CONTABILIZACION_SQ = "SELECT TIPO_CARGA, ACCION FROM CB_BITACORA  WHERE 1=1 ";
	
	/**
	 * @author juankrlos
	 * 
	 * */
	public static final String CB_APLDES_RECARGA_SP = "{CALL CONCILBANCARIA.CB_APLDES_RECARGA_SP(?)}";
	
	public static final String ACTUALIZA_HISTORIAL_ACCION = "UPDATE CB_HISTORIAL_ACCION "
			+ "SET ESTADO_ACCION = ?,"
			+ "    TIPOLOGIAGACID = ?,"
			+ "    RESPONSE_GAC = ?,"
			+ "    UNIDADID = ?,"
			+ "    SOLUCION = ?,"
			+ "    TIPO_CIERRE = ?,"
			+"     NOMBRE_CLIENTE = ?,"
			+ "    MODIFICADO_POR = USER,"
			+ "    FECHA_MODIFICACION = SYSDATE,"
			+"     RESPUESTA_SCL = ?"
			+ "	WHERE CBHISTORIALACCIONID = ?";
	
	public static final String OBTENER_TRACKING_ID_SQ = "SELECT MAX(TRACKING_ID) FROM BMF WHERE ACCOUNT_NO = ? ORDER BY TRACKING_ID, TRACKING_ID_SERV ";
	//public static final String OBTENER_TELEFONO_SQ = "select num_cuentarbor from te_numeros_contrato a, te_numeros b, te_contrato c, te_cuenta d where a.cod_numeroid = b.cod_numeroid and a.tecontratoid = c.tecontratoid and c.tecuentaid = d.tecuentaid and b.cod_num = ? and a.fec_baja is null ";
	public static final String OBTENER_TELEFONO_SQ = " SELECT NUM_CUENTARBOR FROM TE_CUENTA A, TCG_CONTRATO B,TCG_INSTANCIA_CONTRATO C,TCG_INSTANCIA D  WHERE A.TECUENTAID = B.TECUENTAID  AND B.TCGCONTRATOID = C.TCGCONTRATOID AND C.TCGINSTANCIAID = D.TCGINSTANCIAID AND D.CODIGO = ? ";
	
	public static final String ACTUALIZA_TRANS_DATE_SQ = "UPDATE BMF SET TRANS_DATE = to_date(?) WHERE TRACKING_ID = ? AND TRACKING_ID_SERV = 3 ";
	
	public static final String OBTENER_ETH = " select VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF where valor_objeto1 = 'ETH' ";
	
	public static final String OBTENER_II = " select VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF where valor_objeto1 = 'II' ";
	
	public static final String OBTENER_TAPFI = " select VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF where valor_objeto1 = 'TAPFI' ";
	
	public static final String OBTENER_WG = " select VALOR_OBJETO1 FROM CB_MODULO_CONCILIACION_CONF where valor_objeto1 = 'WG' ";
}
