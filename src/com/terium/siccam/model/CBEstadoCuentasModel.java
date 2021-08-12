package com.terium.siccam.model;

/**
 * @author Juankrlos - 11/01/2017 -
 * */

public class CBEstadoCuentasModel {
	
	private int cbestadocuentaconfid;
	private int codigo;
	private String nombrebanco;
	private int cbestadocuentaid;
	
	private String afilicacion;
	private String tipo;
	private String referencia;
	private String lote;
	private String consumo;
	private String impuestoturis;
	private String propina;
	private String iva;
	private String comision;
	private String ivacomision;
	private String liquido;
	private String retencion;
	private String documento;
	private String descpago;
	private String usuario;
	private String fecha;
	
	private String codigo_lote;
	private String debito;
	private String credito;
	private String balance;
	
	private int cbestadocuentavisadetid;
	private String fechacierre;
	private String terminal; 
	private String tarjeta;
	private String fechaventa;
	private String hora;	                    
	private String autorizacion;
	private String tipotrans;
	private String impturismo;
	
	private int cbestadocuentaarchivosid;
	private String nombrearchivos;
	private String descarchivos;
	
	private int cbestadocuentasociedadid;  
    private String cuenta;
    private String asignacion;
    private String clase;
    private String numdocumento;
    private String fechavalor;
    private String fechacontab;
    private String texto;
    private String mon;
    private String importemd;
    private String importeml;
    private String docucomp;
    private String ctacp;
    
    //Agregado por Carlos Godinez - Qitcorp - 02/03/2017
    private int cbcatalogobancoid;
    private int cbcatalogoagenciaid;
    private String fechaInicio;
    private String fechaFin;
    private String observaciones;
    
    private String tipologia;
    private String agenciaTipologia;
    private String fechaIngresos;
    
    //Agregado por Carlos God�nez - Qitcorp - 21/03/2017
    private String tipoTarjeta;
    private String fechatransaccion;
    private String nombreAgencia;
    private String afiliacion;
    
    //Agregado por Ovidio Santos - Qitcorp - 25/04/2017
    
    private String identificador;
    private String textoCabDoc;
    private String anulacion;
    private String ct;
    private String ejercicioMes;
    private String fechaDoc;
    private String libMayor;
    private String periodo;
    private String importeMl3;
    private String usuarioSociedad;
    private String descripcion;
    
    
    /*
     * nuevos campos para extracto agregados Ovidio Santos
     *
     */
    
    private String ml;
    private String ml3;
    private String compens;
    private String afun;
    private String area_funcional;
    private String ce_coste;
    private String codigo_transaccion;
    private String clv_ref_cabecera;
    private String bco_prp;
    private String orden;
    private String tp_camb_ef;
    private String tpbc;
    //fin campos
    
    //agregados por Gerbert
    
    private String fecha_solicitud;
   
	private int caso;
    private String estado;
    private String cap;
    private String dictamen_tersoreria;
    private String solicitante;
    private String totalgeneralcolones;
    private String totalgeneralvalores;
    private int fila;
    private String boleta_deposito;
    private String moneda;
    private String valor_tipo_cambio;
    
    public String getMl() {
		return ml;
	}
	public void setMl(String ml) {
		this.ml = ml;
	}
	public String getMl3() {
		return ml3;
	}
	public void setMl3(String ml3) {
		this.ml3 = ml3;
	}
	public String getCompens() {
		return compens;
	}
	public void setCompens(String compens) {
		this.compens = compens;
	}
	public String getAfun() {
		return afun;
	}
	public void setAfun(String afun) {
		this.afun = afun;
	}
	public String getArea_funcional() {
		return area_funcional;
	}
	public void setArea_funcional(String area_funcional) {
		this.area_funcional = area_funcional;
	}
	public String getCe_coste() {
		return ce_coste;
	}
	public void setCe_coste(String ce_coste) {
		this.ce_coste = ce_coste;
	}
	public String getCodigo_transaccion() {
		return codigo_transaccion;
	}
	public void setCodigo_transaccion(String codigo_transaccion) {
		this.codigo_transaccion = codigo_transaccion;
	}
	public String getClv_ref_cabecera() {
		return clv_ref_cabecera;
	}
	public void setClv_ref_cabecera(String clv_ref_cabecera) {
		this.clv_ref_cabecera = clv_ref_cabecera;
	}
	public String getBco_prp() {
		return bco_prp;
	}
	public void setBco_prp(String bco_prp) {
		this.bco_prp = bco_prp;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
	}
	public String getTp_camb_ef() {
		return tp_camb_ef;
	}
	public void setTp_camb_ef(String tp_camb_ef) {
		this.tp_camb_ef = tp_camb_ef;
	}
	public String getTpbc() {
		return tpbc;
	}
	public void setTpbc(String tpbc) {
		this.tpbc = tpbc;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTextoCabDoc() {
		return textoCabDoc;
	}
	public void setTextoCabDoc(String textoCabDoc) {
		this.textoCabDoc = textoCabDoc;
	}
	public String getAnulacion() {
		return anulacion;
	}
	public void setAnulacion(String anulacion) {
		this.anulacion = anulacion;
	}
	public String getCt() {
		return ct;
	}
	public void setCt(String ct) {
		this.ct = ct;
	}
	public String getEjercicioMes() {
		return ejercicioMes;
	}
	public void setEjercicioMes(String ejercicioMes) {
		this.ejercicioMes = ejercicioMes;
	}
	public String getFechaDoc() {
		return fechaDoc;
	}
	public void setFechaDoc(String fechaDoc) {
		this.fechaDoc = fechaDoc;
	}
	public String getLibMayor() {
		return libMayor;
	}
	public void setLibMayor(String libMayor) {
		this.libMayor = libMayor;
	}
	public String getPeriodo() {
		return periodo;
	}
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public String getImporteMl3() {
		return importeMl3;
	}
	public void setImporteMl3(String importeMl3) {
		this.importeMl3 = importeMl3;
	}
	public String getUsuarioSociedad() {
		return usuarioSociedad;
	}
	public void setUsuarioSociedad(String usuarioSociedad) {
		this.usuarioSociedad = usuarioSociedad;
	}
	
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	public int getCbestadocuentaarchivosid() {
		return cbestadocuentaarchivosid;
	}
	public void setCbestadocuentaarchivosid(int cbestadocuentaarchivosid) {
		this.cbestadocuentaarchivosid = cbestadocuentaarchivosid;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public int getCbestadocuentaconfid() {
		return cbestadocuentaconfid;
	}
	public void setCbestadocuentaconfid(int cbestadocuentaconfid) {
		this.cbestadocuentaconfid = cbestadocuentaconfid;
	}
	public String getNombrebanco() {
		return nombrebanco;
	}
	public void setNombrebanco(String nombrebanco) {
		this.nombrebanco = nombrebanco;
	}
	public int getCbestadocuentaid() {
		return cbestadocuentaid;
	}
	public void setCbestadocuentaid(int cbestadocuentaid) {
		this.cbestadocuentaid = cbestadocuentaid;
	}
	public String getFechatransaccion() {
		return fechatransaccion;
	}
	public void setFechatransaccion(String fechatransaccion) {
		this.fechatransaccion = fechatransaccion;
	}
	public String getAfilicacion() {
		return afilicacion;
	}
	public void setAfilicacion(String afilicacion) {
		this.afilicacion = afilicacion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public String getLote() {
		return lote;
	}
	public void setLote(String lote) {
		this.lote = lote;
	}
	public String getConsumo() {
		return consumo;
	}
	public void setConsumo(String consumo) {
		this.consumo = consumo;
	}
	public String getImpuestoturis() {
		return impuestoturis;
	}
	public void setImpuestoturis(String impuestoturis) {
		this.impuestoturis = impuestoturis;
	}
	public String getPropina() {
		return propina;
	}
	public void setPropina(String propina) {
		this.propina = propina;
	}
	public String getIva() {
		return iva;
	}
	public void setIva(String iva) {
		this.iva = iva;
	}
	public String getComision() {
		return comision;
	}
	public void setComision(String comision) {
		this.comision = comision;
	}
	public String getIvacomision() {
		return ivacomision;
	}
	public void setIvacomision(String ivacomision) {
		this.ivacomision = ivacomision;
	}
	public String getLiquido() {
		return liquido;
	}
	public void setLiquido(String liquido) {
		this.liquido = liquido;
	}
	public String getRetencion() {
		return retencion;
	}
	public void setRetencion(String retencion) {
		this.retencion = retencion;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getDescpago() {
		return descpago;
	}
	public void setDescpago(String descpago) {
		this.descpago = descpago;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getCodigo_lote() {
		return codigo_lote;
	}
	public void setCodigo_lote(String codigo_lote) {
		this.codigo_lote = codigo_lote;
	}
	public String getDebito() {
		return debito;
	}
	public void setDebito(String debito) {
		this.debito = debito;
	}
	public String getCredito() {
		return credito;
	}
	public void setCredito(String credito) {
		this.credito = credito;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public int getCbestadocuentavisadetid() {
		return cbestadocuentavisadetid;
	}
	public void setCbestadocuentavisadetid(int cbestadocuentavisadetid) {
		this.cbestadocuentavisadetid = cbestadocuentavisadetid;
	}
	public String getFechacierre() {
		return fechacierre;
	}
	public void setFechacierre(String fechacierre) {
		this.fechacierre = fechacierre;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getTarjeta() {
		return tarjeta;
	}
	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}
	public String getFechaventa() {
		return fechaventa;
	}
	public void setFechaventa(String fechaventa) {
		this.fechaventa = fechaventa;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getAutorizacion() {
		return autorizacion;
	}
	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}
	public String getTipotrans() {
		return tipotrans;
	}
	public void setTipotrans(String tipotrans) {
		this.tipotrans = tipotrans;
	}
	public String getImpturismo() {
		return impturismo;
	}
	public void setImpturismo(String impturismo) {
		this.impturismo = impturismo;
	}
	public String getNombrearchivos() {
		return nombrearchivos;
	}
	public void setNombrearchivos(String nombrearchivos) {
		this.nombrearchivos = nombrearchivos;
	}
	public String getDescarchivos() {
		return descarchivos;
	}
	public void setDescarchivos(String descarchivos) {
		this.descarchivos = descarchivos;
	}
	public int getCbestadocuentasociedadid() {
		return cbestadocuentasociedadid;
	}
	public void setCbestadocuentasociedadid(int cbestadocuentasociedadid) {
		this.cbestadocuentasociedadid = cbestadocuentasociedadid;
	}
	public String getCuenta() {
		return cuenta;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	public String getAsignacion() {
		return asignacion;
	}
	public void setAsignacion(String asignacion) {
		this.asignacion = asignacion;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public String getNumdocumento() {
		return numdocumento;
	}
	public void setNumdocumento(String numdocumento) {
		this.numdocumento = numdocumento;
	}
	public String getFechavalor() {
		return fechavalor;
	}
	public void setFechavalor(String fechavalor) {
		this.fechavalor = fechavalor;
	}
	public String getFechacontab() {
		return fechacontab;
	}
	public void setFechacontab(String fechacontab) {
		this.fechacontab = fechacontab;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getMon() {
		return mon;
	}
	public void setMon(String mon) {
		this.mon = mon;
	}
	public String getImportemd() {
		return importemd;
	}
	public void setImportemd(String importemd) {
		this.importemd = importemd;
	}
	public String getImporteml() {
		return importeml;
	}
	public void setImporteml(String importeml) {
		this.importeml = importeml;
	}
	public String getDocucomp() {
		return docucomp;
	}
	public void setDocucomp(String docucomp) {
		this.docucomp = docucomp;
	}
	public String getCtacp() {
		return ctacp;
	}
	public void setCtacp(String ctacp) {
		this.ctacp = ctacp;
	}
	public int getCbcatalogobancoid() {
		return cbcatalogobancoid;
	}
	public void setCbcatalogobancoid(int cbcatalogobancoid) {
		this.cbcatalogobancoid = cbcatalogobancoid;
	}
	public int getCbcatalogoagenciaid() {
		return cbcatalogoagenciaid;
	}
	public void setCbcatalogoagenciaid(int cbcatalogoagenciaid) {
		this.cbcatalogoagenciaid = cbcatalogoagenciaid;
	}
	public String getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public String getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public String getAgenciaTipologia() {
		return agenciaTipologia;
	}
	public void setAgenciaTipologia(String agenciaTipologia) {
		this.agenciaTipologia = agenciaTipologia;
	}
	public String getFechaIngresos() {
		return fechaIngresos;
	}
	public void setFechaIngresos(String fechaIngresos) {
		this.fechaIngresos = fechaIngresos;
	}
	public String getTipoTarjeta() {
		return tipoTarjeta;
	}
	public void setTipoTarjeta(String tipoTarjeta) {
		this.tipoTarjeta = tipoTarjeta;
	}
	public String getNombreAgencia() {
		return nombreAgencia;
	}
	public void setNombreAgencia(String nombreAgencia) {
		this.nombreAgencia = nombreAgencia;
	}
	public String getAfiliacion() {
		return afiliacion;
	}
	public void setAfiliacion(String afiliacion) {
		this.afiliacion = afiliacion;
	}	
	
	 public String getFecha_solicitud() {
			return fecha_solicitud;
		}
		public void setFecha_solicitud(String fecha_solicitud) {
			this.fecha_solicitud = fecha_solicitud;
		}
		public int getCaso() {
			return caso;
		}
		public void setCaso(int caso) {
			this.caso = caso;
		}
		public String getEstado() {
			return estado;
		}
		public void setEstado(String estado) {
			this.estado = estado;
		}
		public String getCap() {
			return cap;
		}
		public void setCap(String cap) {
			this.cap = cap;
		}
		public String getDictamen_tersoreria() {
			return dictamen_tersoreria;
		}
		public void setDictamen_tersoreria(String dictamen_tersoreria) {
			this.dictamen_tersoreria = dictamen_tersoreria;
		}
		public String getSolicitante() {
			return solicitante;
		}
		public void setSolicitante(String solicitante) {
			this.solicitante = solicitante;
		}
		public String getTotalgeneralcolones() {
			return totalgeneralcolones;
		}
		public void setTotalgeneralcolones(String totalgeneralcolones) {
			this.totalgeneralcolones = totalgeneralcolones;
		}
		public String getTotalgeneralvalores() {
			return totalgeneralvalores;
		}
		public void setTotalgeneralvalores(String totalgeneralvalores) {
			this.totalgeneralvalores = totalgeneralvalores;
		}
		public int getFila() {
			return fila;
		}
		public void setFila(int fila) {
			this.fila = fila;
		}
		public String getBoleta_deposito() {
			return boleta_deposito;
		}
		public void setBoleta_deposito(String boleta_deposito) {
			this.boleta_deposito = boleta_deposito;
		}
		public String getMoneda() {
			return moneda;
		}
		public void setMoneda(String moneda) {
			this.moneda = moneda;
		}
		public String getValor_tipo_cambio() {
			return valor_tipo_cambio;
		}
		public void setValor_tipo_cambio(String valor_tipo_cambio) {
			this.valor_tipo_cambio = valor_tipo_cambio;
		}
}
