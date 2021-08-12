package com.terium.siccam.model;

/**
 * creado Ovidio Santos
 * 
 */
public class CBConsultaContabilizacionModel {

	private String centroCosto;
	private String referencia;
	private String claveContabilizacion;
	private String texto;
	private String Texto2;
	private String observaciones;
	private String cuenta;
	private String fechaini;
	private String fechafin;
	private int cbcontabilizacionid;
	private String fecha;
	private String banco;
	private String agencia;
	private String terminacion;
	private String nombre;
	private String tipo;

	private String debe;
	private String haber;
	private String fecha_contabilizacion;
	private String estado;
	private String modificado_por;

	private String usuario;
	private int cbcatalogoagenciaid ;
	private int cbcatalogobancoid;
	private int cbtipologiaspolizaid;
	private int cbestadocuentaid;
	private String monto;
	private String cargo_abono;
	private String creado_por;
	private String indicador_iva;
	private String actividad ;
	
	private String fecha_ingreso;
	
	public String getFecha_ingreso() {
		return fecha_ingreso;
	}

	public void setFecha_ingreso(String fecha_ingreso) {
		this.fecha_ingreso = fecha_ingreso;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public String getIndicador_iva() {
		return indicador_iva;
	}

	public void setIndicador_iva(String indicador_iva) {
		this.indicador_iva = indicador_iva;
	}

	public String getCreado_por() {
		return creado_por;
	}

	public void setCreado_por(String creado_por) {
		this.creado_por = creado_por;
	}

	public String getCargo_abono() {
		return cargo_abono;
	}

	public void setCargo_abono(String cargo_abono) {
		this.cargo_abono = cargo_abono;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public int getCbestadocuentaid() {
		return cbestadocuentaid;
	}

	public void setCbestadocuentaid(int cbestadocuentaid) {
		this.cbestadocuentaid = cbestadocuentaid;
	}

	public int getCbtipologiaspolizaid() {
		return cbtipologiaspolizaid;
	}

	public void setCbtipologiaspolizaid(int cbtipologiaspolizaid) {
		this.cbtipologiaspolizaid = cbtipologiaspolizaid;
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

	private int cbestadocuentaarchivosid;
	public int getCbestadocuentaarchivosid() {
		return cbestadocuentaarchivosid;
	}

	public void setCbestadocuentaarchivosid(int cbestadocuentaarchivosid) {
		this.cbestadocuentaarchivosid = cbestadocuentaarchivosid;
	}

	public String getNombrearchivos() {
		return nombrearchivos;
	}

	public void setNombrearchivos(String nombrearchivos) {
		this.nombrearchivos = nombrearchivos;
	}

	private String nombrearchivos;

	public int getCbcontabilizacionid() {
		return cbcontabilizacionid;
	}

	public void setCbcontabilizacionid(int cbcontabilizacionid) {
		this.cbcontabilizacionid = cbcontabilizacionid;
	}

	public String getFechaini() {
		return fechaini;
	}

	public void setFechaini(String fechaini) {
		this.fechaini = fechaini;
	}

	public String getFechafin() {
		return fechafin;
	}

	public void setFechafin(String fechafin) {
		this.fechafin = fechafin;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getClaveContabilizacion() {
		return claveContabilizacion;
	}

	public void setClaveContabilizacion(String claveContabilizacion) {
		this.claveContabilizacion = claveContabilizacion;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTexto2() {
		return Texto2;
	}

	public void setTexto2(String texto2) {
		Texto2 = texto2;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getTerminacion() {
		return terminacion;
	}

	public void setTerminacion(String terminacion) {
		this.terminacion = terminacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDebe() {
		return debe;
	}

	public void setDebe(String debe) {
		this.debe = debe;
	}

	public String getHaber() {
		return haber;
	}

	public void setHaber(String haber) {
		this.haber = haber;
	}

	public String getFecha_contabilizacion() {
		return fecha_contabilizacion;
	}

	public void setFecha_contabilizacion(String fecha_contabilizacion) {
		this.fecha_contabilizacion = fecha_contabilizacion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getModificado_por() {
		return modificado_por;
	}

	public void setModificado_por(String modificado_por) {
		this.modificado_por = modificado_por;
	}

}
