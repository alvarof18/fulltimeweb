package com.casapazmino.fulltime.action;

import java.util.Date;

import org.jboss.seam.annotations.Name;

@Name("plantillaPlanificacion")
public class PlantillaPlanificacion{	
		
	private Long asisId;
	private Long empId;
	private Long timbId;	
	private Long dhId;
	private String fechaHoraHorario;
	private String fechaHoraTimbre;
	private String entradaSalida;
	private String codigoEmpleado;	
	private int orden;
	private int maxMinuto;
	private String estado;
	private String fechaHorario;
	private String horaHorario;
	private Date fecha;
	private Date fechaTimbre;
	private String accion;
	private String teclaFuncion;
	private String cadenaFechaTimbre;
	private String cadenaHoraTimbre;
	
	private Long permId;
	private Date fechaPermiso;
	private String cadenaFechaDesdePermiso;
	private String cadenaFechaHastaPermiso;
	
	private Date fechaPermisoDesde;
	private Date fechaPermisoHasta;
	
	public PlantillaPlanificacion()
	{
		super();
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Long getDhId() {
		return dhId;
	}

	public void setDhId(Long dhId) {
		this.dhId = dhId;
	}

	public String getFechaHoraHorario() {
		return fechaHoraHorario;
	}

	public void setFechaHoraHorario(String fechaHoraHorario) {
		this.fechaHoraHorario = fechaHoraHorario;
	}

	public String getEntradaSalida() {
		return entradaSalida;
	}

	public void setEntradaSalida(String entradaSalida) {
		this.entradaSalida = entradaSalida;
	}

	public String getCodigoEmpleado() {
		return codigoEmpleado;
	}

	public void setCodigoEmpleado(String codigoEmpleado) {
		this.codigoEmpleado = codigoEmpleado;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public int getMaxMinuto() {
		return maxMinuto;
	}

	public void setMaxMinuto(int maxMinuto) {
		this.maxMinuto = maxMinuto;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaHorario() {
		return fechaHorario;
	}

	public void setFechaHorario(String fechaHorario) {
		this.fechaHorario = fechaHorario;
	}

	public String getHoraHorario() {
		return horaHorario;
	}

	public void setHoraHorario(String horaHorario) {
		this.horaHorario = horaHorario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Long getAsisId() {
		return asisId;
	}

	public void setAsisId(Long asisId) {
		this.asisId = asisId;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getTeclaFuncion() {
		return teclaFuncion;
	}

	public void setTeclaFuncion(String teclaFuncion) {
		this.teclaFuncion = teclaFuncion;
	}

	public Long getTimbId() {
		return timbId;
	}

	public void setTimbId(Long timbId) {
		this.timbId = timbId;
	}

	public String getFechaHoraTimbre() {
		return fechaHoraTimbre;
	}

	public void setFechaHoraTimbre(String fechaHoraTimbre) {
		this.fechaHoraTimbre = fechaHoraTimbre;
	}

	public Date getFechaTimbre() {
		return fechaTimbre;
	}

	public void setFechaTimbre(Date fechaTimbre) {
		this.fechaTimbre = fechaTimbre;
	}

	public String getCadenaFechaTimbre() {
		return cadenaFechaTimbre;
	}

	public void setCadenaFechaTimbre(String cadenaFechaTimbre) {
		this.cadenaFechaTimbre = cadenaFechaTimbre;
	}

	public String getCadenaHoraTimbre() {
		return cadenaHoraTimbre;
	}

	public void setCadenaHoraTimbre(String cadenaHoraTimbre) {
		this.cadenaHoraTimbre = cadenaHoraTimbre;
	}

	public Long getPermId() {
		return permId;
	}

	public void setPermId(Long permId) {
		this.permId = permId;
	}
	
	public String getCadenaFechaDesdePermiso() {
		return cadenaFechaDesdePermiso;
	}

	public void setCadenaFechaDesdePermiso(String cadenaFechaDesdePermiso) {
		this.cadenaFechaDesdePermiso = cadenaFechaDesdePermiso;
	}

	public Date getFechaPermiso() {
		return fechaPermiso;
	}

	public void setFechaPermiso(Date fechaPermiso) {
		this.fechaPermiso = fechaPermiso;
	}

	public String getCadenaFechaHastaPermiso() {
		return cadenaFechaHastaPermiso;
	}

	public void setCadenaFechaHastaPermiso(String cadenaFechaHastaPermiso) {
		this.cadenaFechaHastaPermiso = cadenaFechaHastaPermiso;
	}

	public Date getFechaPermisoDesde() {
		return fechaPermisoDesde;
	}

	public void setFechaPermisoDesde(Date fechaPermisoDesde) {
		this.fechaPermisoDesde = fechaPermisoDesde;
	}

	public Date getFechaPermisoHasta() {
		return fechaPermisoHasta;
	}

	public void setFechaPermisoHasta(Date fechaPermisoHasta) {
		this.fechaPermisoHasta = fechaPermisoHasta;
	}

	@Override
	public String toString() {
		return "PlantillaPlanificacion [asisId=" + asisId + ", empId=" + empId + ", timbId=" + timbId + ", dhId=" + dhId
				+ ", fechaHoraHorario=" + fechaHoraHorario + ", fechaHoraTimbre=" + fechaHoraTimbre + ", entradaSalida="
				+ entradaSalida + ", codigoEmpleado=" + codigoEmpleado + ", orden=" + orden + ", maxMinuto=" + maxMinuto
				+ ", estado=" + estado + ", fechaHorario=" + fechaHorario + ", horaHorario=" + horaHorario + ", fecha="
				+ fecha + ", fechaTimbre=" + fechaTimbre + ", accion=" + accion + ", teclaFuncion=" + teclaFuncion
				+ ", cadenaFechaTimbre=" + cadenaFechaTimbre + ", cadenaHoraTimbre=" + cadenaHoraTimbre + ", permId="
				+ permId + ", fechaPermiso=" + fechaPermiso + ", cadenaFechaDesdePermiso=" + cadenaFechaDesdePermiso
				+ ", cadenaFechaHastaPermiso=" + cadenaFechaHastaPermiso + ", fechaPermisoDesde=" + fechaPermisoDesde
				+ ", fechaPermisoHasta=" + fechaPermisoHasta + "]";
	}
	
	
		
}
