package com.terium.siccam.sql;

/**
 * @author Carlos Godinez
 * */
public class ConsultasSQGT {
	
	/**
	 * CBAgenciaVirFisDAO
	 * */
	
	//Query -> 1
	private static String AGREGAR_AGENCIA_VIR_FIS = "INSERT INTO " + " CB_AGENCIA_VIR_FIS "
			+ " ( " + " CBAGENCIASVIRFISID, " + " CBCATALOGOAGENCIAID, "
			+ " CODIGO, " + " NOMBRE, " + " TIPO, " + " CREADO_POR, "
			+ " FECHA_CREADO " + " ) " + " VALUES " + " ( "
			+ " CB_AGENCIA_VIR_FIS_SQ.NEXTVAL, " + " ?, " + " ?, " + " ?, "
			+ " ?, " + " ?, " + " SYSDATE " + " )";
	
	//Query -> 2
	private static String LISTADO_AGENCIAS = "SELECT CBAGENCIASVIRFISID cbAgenciaVirfiscid, "
			+ " CBCATALOGOAGENCIAID cbCatalogoAgenciaid, "
			+ " CODIGO codigo, "
			+ " NOMBRE nombre, "
			+ " DECODE(TIPO, '1', 'PRESENCIAL', '0', 'VIRTUAL') tipo, "
			+ " CREADO_POR creadoPor, "
			+ " FECHA_CREADO fechaCreado, "
			+ " MODIFICADO_POR modificadoPor, "
			+ " FECHA_MODIFICADO fechaModificado"
			+ " FROM CB_AGENCIA_VIR_FIS "
			+ " WHERE CBCATALOGOAGENCIAID = ?";
	
	/**
	 * CBBancoAgenciaConfrontaDAO
	 * */
	
	//Query -> 3
	
	
	//Query -> 4
	
	//Query -> 5
	
	
	//Query -> 6
	
	
	/**
	 * CBConciliacionBancoDAO
	 * */
	
	//Query -> 6
	private static String CONSULTA_CONCILIACION_BANCO = "select to_char(dia,'dd/MM/yyyy'), cbcatalogoagenciaid, nombre, estado_prepago, confronta_prepago, dif_prepago, estado_postpago, confronta_postpago, dif_postpago, pagosdeldia, pagosotrosdias, pagosotrosmeses, "
			+ "reversasotrosdias, reversasotrosmeses, total_dia, total_general, estado_cuenta, diferencia_total, porcentaje_postpago, comision_confronta_postpago, comision_postpago, diferencia_comision_postpago, porcentaje_prepago,"
			+ "comision_confronta_prepago, comision_prepago, diferencia_comision_prepago, comisiontotal, recafinalpost, recafinalpre, totalfinal "
			+ "from  cb_conciliacion_banco_vs_vw WHERE 1=1 ";
	
	//Query -> 7
	private static String OBTIENE_AGENCIA_SQ = "select B.cbcatalogoagenciaid, b.NOMBRE " + 
			"from cb_catalogo_banco a, cb_catalogo_agencia b " + 
			"where a.cbcatalogobancoid = b.cbcatalogobancoid " + 
			"and a.estado = 1 " + 
			"and b.estado = 1 " + 
			"and b.cuenta_contable is null "+ 
			"and tipo_entidad = 'FINANCIERA'  order by nombre asc ";
	
	/**
	 * CBConciliacionCajasDAO
	 * */
	
	//Query -> 8
	private static String CONSULTA_CAJEROS = "SELECT CBCATALOGOBANCOID,ENTIDAD, CBCATALOGOAGENCIAID,AGENCIA,TO_CHAR (FECHA, 'dd/MM/yyyy'),CAJA_EFECTIVO,CAJA_CHEQUE, CAJA_EXENSIONES,CAJA_CUOTAS_VISA, " + 
			"CAJA_CUOTAS_CREDOMATIC,CAJA_TARJETA_CREDOMATIC,CAJA_TARJETA_OTRAS,CAJA_TARJETA_VISA,SC_PAGOSOD,SC_PAGOSOM,SC_REVERSASOD,SC_REVERSASOM,NVL (CAJA_TOTAL, 0) + NVL (SC_PAGOSOD, 0) + NVL (SC_REVERSASOD, 0) " + 
			" TOTAL_DIA,CAJA_TOTAL, CREDOMATIC_DEP CONSUMO_CREDOMATIC,CREDOMATIC_RET RETENCION_CREDOMATIC,ESTADO_CRED, CONSUMO_VISA,IVA_VISA,ESTADO_VISA,DEPOSITO,NVL (CREDOMATIC_DEP, 0) + NVL (CONSUMO_VISA, 0) + NVL (IVA_VISA, 0) + NVL (DEPOSITO, 0) " + 
			" TOTAL_EC,NVL (TOTAL_SC, 0) - (NVL (CREDOMATIC_DEP, 0) + NVL (CONSUMO_VISA, 0) + NVL (IVA_VISA, 0) + NVL (DEPOSITO, 0)) DIFERENCIA FROM CB_CONCILIACION_CAJAS_VW WHERE  1 = 1 " ;
	
	//Query -> 9
	private static String OBTIENE_AGENCIA_SQ2 = "select b.cbcatalogoagenciaid, b.nombre " + 
			"from cb_catalogo_banco a, cb_catalogo_agencia b " + 
			"where a.cbcatalogobancoid = b.cbcatalogobancoid " + 
			"and a.tipo_entidad = 'NO FINANCIERA' " + 
			"and b.estado = 1 " + 
			"and a.estado = 1 ";
	
	/**
	 * CBConsultaEstadoCuentasDAO
	 * */
	
	//Query -> 10
	private static String AGENCIA_SQ = "select distinct a.cbcatalogoagenciaid, a.agencia, b.cuenta_contable  " + 
    		" from cb_estado_cuenta_vw a, cb_catalogo_agencia b " + 
    		" where a.cbcatalogoagenciaid = b.cbcatalogoagenciaid " + 
    		" and b.cuenta_contable is not null " + 
    		" order by agencia";
	
	/**
	 * CBDataBancoDAO
	 * */
	
	//Query -> 11
	private static String INSERTA_MASIVOS_BANCO = "INSERT " + " INTO CB_DATA_BANCO " + "   ( "
			+ "     cBDataBancoId, " + "     cod_Cliente, " + "     telefono, " + "     tipo, " + "     fecha, "
			+ "     cBCatalogoBancoId, " + "     cBCatalogoAgenciaId, " + "     cBBancoAgenciaConfrontaId, "
			+ "     monto, " + "     transaccion, " + "     estado, " + "     mes, dia,  " + "     texto1, "
			+ "     texto2, " + "     creado_Por, " + "     modificado_Por, " + "     fecha_Creacion, "
			+ "     fecha_Modificacion, id_archivos_insertados, " + " CODIGO, " + " COMISION " + "  ) " + "     VALUES "
			+ " ( " + " cb_data_banco_sq.nextval, " + "     ?, " + "     ?, " + "     ?, "
			+ "     to_date(?, 'dd/MM/yyyy HH24:mi:ss'), " + "     ?, " + "     ?, " + "     ?, " + "     ?, "
			+ "     ?, " + "     ?, " + "     ?, trunc(to_date(?,'dd/MM/yyyy HH24:mi:ss'), 'dd') ," + "     ?, "
			+ "     ?, " + "     ?, " + "     ?, " + "     sysdate, " + "     to_date(?, 'dd/MM/yyyy HH24:mi:ss'), "
			+ " ?, " + " ?, " + " ? " + " )";
	
	//Query -> 12
	private static String CONSULTA_ARCHIVO = "Select id_archivos_insertados idArchivosViejos, nombre_archivo nombreArchivo from cb_archivos_insertados "
			+ " where NOMBRE_ARCHIVO = ? and id_archivos_insertados !=  ?";
	
	//Query -> 13
	private static String BORRAR_CONCILACIONES = "DELETE "
			+ " cb_conciliacion "
			+ " WHERE cbbancoagenciaconfrontaid = ? "
			+ " AND dia = TO_DATE (?, 'dd/MM/yy') ";

	//Query -> 14
	private static String BORRA_CONCILIACIONES_PENDIENTES = "delete "
			+ " FROM cb_conciliacion " + " WHERE tipo = 1 " + 
					"        AND cbbancoagenciaconfrontaid = ?  " + 
					"        AND dia = TO_DATE (?, 'dd/MM/yy')";

	//Query -> 15
	private static String BORRAR_REGISTRO = "Delete cb_data_banco "
			+ " where cbbancoagenciaconfrontaid = ?  " + 
			"        AND dia = TO_DATE (?, 'dd/MM/yy')";
	
	//Query -> 16
	private static String VERIFICA_CARGA_DATA_BANCO = "SELECT * FROM CB_DATA_BANCO "
			+ " WHERE TO_CHAR(DIA, 'dd/MM/yyyy') = ? "
			+ " AND CBCATALOGOBANCOID = ? " + " AND CBCATALOGOAGENCIAID = ? "
			+ " AND CBBANCOAGENCIACONFRONTAID = ?";
	
	//Query -> 17
	private static String CONSULTA_IDBAC = "SELECT cbbancoagenciaconfrontaid "
			+ "FROM cb_banco_agencia_confronta "
			+ "WHERE cbcatalogobancoid        = ? "
			+ "AND cbcatalogoagenciaid        = ? "
			+ "AND cbconfiguracionconfrontaid = ? ";
	
	//Query -> 18
	private static String BORRAR_ARCHIVO = "Delete cb_archivos_insertados "
			+ " where id_archivos_insertados != ? and nombre_archivo = ? ";
			
	/**
	 * CBEstadoCuentaDAO
	 * */
	
	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentasVisaDet()
	//Query -> 19
	private static String INSERT_CREDO_DET_SQ = "INSERT INTO CB_ESTADO_CUENTA_DET(CBESTADOCUENTADETID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, AFILIACION, F_CIERRE,TERMINAL, LOTE, TARJETA, FECHA_VENTA,"
			+ " HORA, AUTORIZACION, CONSUMO, TIPO_TRANS, IMP_TURISMO, PROPINA, COMISION, IVA, LIQUIDO, SWCREATEBY, SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_DET_SQ.NEXTVAL, ?,?,?,to_date(?,'yyyymmdd'),?,?,?,to_date(?,'yyyy-MM-dd'),"
			+ " ?,?,?,?,?,?,?,?,?,?,SYSDATE) ";
	
	// Insert para la nueva version de carga de archivos credomatic
	//Query -> 20
	private static String INSERT_CREDO_SQ2 = "INSERT INTO CB_ESTADO_CUENTA "
			+ "   (CBESTADOCUENTAID,CBESTADOCUENTACONFID,CBESTADOCUENTAARCHIVOSID,FECHA_TRANSACCION,AFILIACION,TIPO, "
			+ "	   REFERENCIA,CODIGO_LOTE,CONSUMO,IMPUESTO_TURIS,PROPINA,IVA,COMISION,IVA_COMISION,LIQUIDO,RETENCION, "
			+ "	   DOCUMENTO,DESCRIPCION,DEBITO,CREDITO,BALANCE,SWCREATEBY,SWDATECREATED) " + "	 VALUES "
			+ "   (CB_ESTADO_CUENTA_SQ.NEXTVAL, ?, ?, to_date(?,'yyyymmdd'), ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?, sysdate) ";

	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentaSociedad()
	//Query -> 21
	private static String INSERT_SOCIEDAD_SQ = "INSERT INTO CB_ESTADO_CUENTA_SOCIEDAD(CBESTADOCUENTASOCIEDADID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID,CUENTA,REFERENCIA,ASIGNACION,CLASE,"
			+ " NUMDOCUMENTO,FECHAVALOR,FECHACONTAB,TEXTO,MON,IMPORTEMD, IMPORTEML,DOCUCOMP,CTACP,TEXTO_CAB_DOC,ANULACION,CT,SWCREATEBY,SWDATECREATED)"
			+ " VALUES(CB_ESTADO_CUENTA_SOCIEDAD_SQ.NEXTVAL, ?,?,?,?,?,?,?,to_date(?,'dd/MM/yyyy'),"
			+ " to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,?,?,?,?,SYSDATE)";
	
	// Este query es llamado desde la clase CBEstadoCuentaDAO.java por el metodo
	// insertCuentasVisa()
	//Query -> 22
	private static String INSERT_VISA_SQ = "INSERT INTO CB_ESTADO_CUENTA(CBESTADOCUENTAID, "
			+ " CBESTADOCUENTACONFID, CBESTADOCUENTAARCHIVOSID, FECHA_TRANSACCION,AFILIACION, TIPO, REFERENCIA, CODIGO_LOTE, "
			+ " CONSUMO, IMPUESTO_TURIS, PROPINA, IVA, COMISION, IVA_COMISION, LIQUIDO, RETENCION, "
			+ " DOCUMENTO, DESCRIPCION, SWCREATEBY, SWDATECREATED) "
			+ " VALUES(CB_ESTADO_CUENTA_SQ.NEXTVAL,?,?,to_date(?,'dd/MM/yyyy'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";

	//Query -> 23
	private static String CONSULTA_QUERY_NUEVA_LIQUIDACION = "SELECT TIP_VALOR, TIPO_PAGO, COD_TIPTARJETA, DES_TIPTARJETA, IMPORTE from SATELITES.CB_LIQUIDACION_DETALLE_VW "
			+ "WHERE  to_char(fec_efectividad,'dd-mm-yyyy') = ? AND nom_usuarora = ?";

	// Este query es por el metodo insertTipologiasPoliza() agrega ovidio Santos
	// 04052017
	//Query -> 24
	private static String INSERT_TIPOLOGIAS_POLIZA_SQ = "INSERT INTO CB_TIPOLOGIAS_POLIZA(CBTIPOLOGIASPOLIZAID,CENTRO_COSTO, "
			+ "  CUENTA_CONTRAPARTIDA, CLAVE_CONTABILIZACION, INDICADOR_IVA, TERMINACION, ACTIVIDAD, DESCRIPCION, NOMBRE, CENTRO_COSTO_CP,CLAVE_CONTABILIZACION_CP, DESCRIPCION_CP, TERMINACION_CP,CUENTA_INGRESO, CENTRO_COSTO_DF, CLAVE_CONTABILIZACION_DF, DESCRIPCION_DF, TERMINACION_DF, CUENTA_INGRESO_DF, CLAVE_CONTABILIZACION_NEG,INDICADOR_IVA_CP, ACTIVIDAD_CP, "
			+ "  CREADO_POR,TIPO ,PIDE_ENTIDAD, SECUENCIA , FECHA_CREACION) "
			+ " VALUES(CB_TIPOLOGIAS_POLIZA_SQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, sysdate) ";

	// este query es utilizado para modificar tipologias poliza
	//Query -> 25
	private static String MODIFICAR_POLIZA_SQ = "UPDATE CB_TIPOLOGIAS_POLIZA SET CENTRO_COSTO = ?, "
			+ "  CUENTA_CONTRAPARTIDA = ?, CLAVE_CONTABILIZACION = ?,INDICADOR_IVA = ?, TERMINACION =?, ACTIVIDAD=?,DESCRIPCION = ?,NOMBRE = ?,CENTRO_COSTO_CP=? ,CLAVE_CONTABILIZACION_CP =? , DESCRIPCION_CP= ?, TERMINACION_CP=? ,CUENTA_INGRESO =?, CENTRO_COSTO_DF = ?, CLAVE_CONTABILIZACION_DF =? , DESCRIPCION_DF = ?, TERMINACION_DF = ?, CUENTA_INGRESO_DF = ?,CLAVE_CONTABILIZACION_NEG = ?, INDICADOR_IVA_CP = ?,ACTIVIDAD_CP = ?, TIPO = ?, PIDE_ENTIDAD = ?, SECUENCIA= ?,  MODIFICADO_POR = ?, FECHA_MODIFICACION = SYSDATE WHERE CBTIPOLOGIASPOLIZAID = ?";

	// este query es utilizado para consultar tipologias poliza
	//Query -> 26
	private static String CONSULTA_TIPOLOGIAS_POLIZA_SQ = "SELECT CBTIPOLOGIASPOLIZAID, CENTRO_COSTO,CLAVE_CONTABILIZACION, DESCRIPCION,  TERMINACION, CUENTA_INGRESO, NOMBRE,  "
			+ "  CENTRO_COSTO_CP,CLAVE_CONTABILIZACION_CP, DESCRIPCION_CP, TERMINACION_CP, CUENTA_CONTRAPARTIDA,INDICADOR_IVA, ACTIVIDAD, CENTRO_COSTO_DF, CLAVE_CONTABILIZACION_DF, DESCRIPCION_DF, TERMINACION_DF, CUENTA_INGRESO_DF, CLAVE_CONTABILIZACION_NEG,INDICADOR_IVA_CP, ACTIVIDAD_CP, DECODE (TIPO,  '1', 'PREPAGO',  '2', 'POSTPAGO') TIPO , DECODE (PIDE_ENTIDAD ,  '1', 'SI',  '0', 'NO') PIDE_ENTIDAD,  "
			+ " CENTRO_DE_BENEFICIO,DIVISION, ORDEN_DE_PROYECTO,  TIPO_DE_CAMBIO, FECHA_DE_CONVERSION, INDICADOR_CME,  "  
			+ "  CAR_PA_SEGMENTO,CAR_PA_SERVICIO, CAR_PA_TIPO_TRAFICO, CAR_PA_AMBITO, CAR_PA_LICENCIA,CAR_PA_REGION, SUBTIPO_LINEA, CANAL, BUNDLE, PRODUCTO, EMPRESA_GRUPO, SECUENCIA  "
			+ " FROM CB_TIPOLOGIAS_POLIZA  WHERE 1=1 ";
	
	/**
	 * CBConciliacionDAO
	 * */
	
	/**
	 * TRABAJAR QUERY DE MANERA PARAMETRIZABLE
	 * obtenerCBConciliaciones()
	 *
	//Query -> 27
	private static String OBTENER_CONCILIACIONES = "SELECT dia, " + "nombre, " + "tipo, "
			+ "trans_telefonica transTelefonica, "
			+ "trans_banco transBanco, " + "conciliadas, "
			+ "dif_trans difTransaccion, "
			+ "pagos_telefonica pagosTelefonica, "
			+ "confronta_banco confrontaBanco, " + "automatica, "
			+ "manual_t manualTelefonica, " + "manual_b manualBanco, "
			+ "pendiente, " + "cbcatalogoagenciaid idAgencia "
			+ "FROM cb_conciliacion_vw where 1 = 1 "
			+ agencia + tipo + estado
			+ " and dia >= to_date('" + dia + "', 'dd/MM/yyyy')"
			+ " and dia <= to_date('" + dia2 + "', 'dd/MM/yyyy') ";
	*/
	
	/**
	 * @author Carlos Godinez -> 30/05/2018
	 * @throws variableSQL 
	 * Se envia de parametro un numero de variable y se retorna la consulta SQL
	 * correspondiente de esta clase
	 * */
	public static String getVariableSQL(int numeroVariable) {
		String variableSQL = "";
		switch(numeroVariable) {
			case 1:
				variableSQL = AGREGAR_AGENCIA_VIR_FIS;
				break;
			case 2:
				variableSQL = LISTADO_AGENCIAS;
				break;
				/*
			case 3:
				variableSQL = INSERTAR_BANCO_AGE_CONF;
				break;
			case 4:
				variableSQL = CONSULTA_BANC_AGE_CONFRONTA;
				break;
			case 5:
				variableSQL = ACTUALIZA_BANCO_AGE_CONF;
				break;
				*/
			case 6:
				variableSQL = CONSULTA_CONCILIACION_BANCO;
				break;
			case 7:
				variableSQL = OBTIENE_AGENCIA_SQ;
				break;
			case 8:
				variableSQL = CONSULTA_CAJEROS;
				break;
			case 9:
				variableSQL = OBTIENE_AGENCIA_SQ2;
				break;
			case 10:
				variableSQL = AGENCIA_SQ;
				break;
			case 11:
				variableSQL = INSERTA_MASIVOS_BANCO;
				break;
			case 12:
				variableSQL = CONSULTA_ARCHIVO;
				break;
			case 13:
				variableSQL = BORRAR_CONCILACIONES;
				break;
			case 14:
				variableSQL = BORRA_CONCILIACIONES_PENDIENTES;
				break;
			case 15:
				variableSQL = BORRAR_REGISTRO;
				break;
			case 16:
				variableSQL = VERIFICA_CARGA_DATA_BANCO;
				break;
			case 17:
				variableSQL = CONSULTA_IDBAC;
				break;
			case 18:
				variableSQL = BORRAR_ARCHIVO;
				break;
			case 19:
				variableSQL = INSERT_CREDO_DET_SQ;
				break;
			case 20:
				variableSQL = INSERT_CREDO_SQ2;
				break;
			case 21:
				variableSQL = INSERT_SOCIEDAD_SQ;
				break;
			case 22:
				variableSQL = INSERT_VISA_SQ;
				break;
			case 23:
				variableSQL = CONSULTA_QUERY_NUEVA_LIQUIDACION;
				break;
			case 24:
				variableSQL = INSERT_TIPOLOGIAS_POLIZA_SQ;
				break;
			case 25:
				variableSQL = MODIFICAR_POLIZA_SQ;
				break;
			case 26:
				variableSQL = CONSULTA_TIPOLOGIAS_POLIZA_SQ;
				break;
			case 27:
				/**
				 * Pendiente
				 * 
				 * variableSQL = OBTENER_CONCILIACIONES;
				 */
				break;
			default:
				variableSQL = null;
				break;
		}
		return variableSQL;
	}
}
