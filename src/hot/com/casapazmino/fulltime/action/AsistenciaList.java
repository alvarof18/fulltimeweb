package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;

@Name("asistenciaList")
public class AsistenciaList extends EntityQuery<Asistencia> {

	@Logger
	Log log;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select asistencia from Asistencia asistencia";

	private static final String[] RESTRICTIONS = {
		"lower(asistencia.empleado.codigoEmpleado) = #{asistenciaList.asistencia.empleado.codigoEmpleado}",
		"lower(asistencia.empleado.cedula) like concat(lower(#{asistenciaList.asistencia.empleado.cedula}),'%')",
		"lower(asistencia.empleado.nombre) like concat(lower(#{asistenciaList.asistencia.empleado.nombre}),'%')",
		"lower(asistencia.empleado.apellido) like concat(lower(#{asistenciaList.asistencia.empleado.apellido}),'%')",
		"lower(asistencia.empleado.departamento.descripcion) = lower(#{asistenciaList.asistencia.empleado.departamento.descripcion})",
		"lower(asistencia.empleado.cargo.descripcion) = lower(#{asistenciaList.asistencia.empleado.cargo.descripcion})",
		"lower(asistencia.entradaSalida) = lower(#{asistenciaList.asistencia.entradaSalida})",
		ControlBaseDatos.modificadorConvertirFecha + "asistencia.fechaHoraHorario) >= #{asistenciaList.asistencia.fechaDesde}",
		ControlBaseDatos.modificadorConvertirFecha + "asistencia.fechaHoraHorario) <= #{asistenciaList.asistencia.fechaHasta}",
		ControlBaseDatos.modificadorConvertirFecha + "asistencia.fechaHoraHorario) = #{asistenciaList.fechaAsistencia}",
		ControlBaseDatos.modificadorConvertirFecha + "asistencia.fechaHorario) = #{asistenciaList.asistencia.fechaHorario}",
		"asistencia.asisOrden = #{asistenciaList.asistencia.asisOrden}",
		"asistencia.estado = #{asistenciaList.asistencia.estado}",
		ControlBaseDatos.modificadorConvertirFecha + "asistencia.fechaHorario) = #{asistenciaList.fechaAsistenciaHorario}",
		};

	private String extensionArchivo;
	private Date fechaAsistencia;
//	private Date fechaAsistenciaNocturno;
	private Date fechaAsistenciaHorario;
	
	private Long[] ciudades;
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	private String tipoReporte;
	
	private int accesoEmpleados;
	private boolean verDetalle;
	
	private boolean tabulado;
	
	private boolean check1;
	private boolean check2;
	private boolean check3;
	
	private boolean sinAsistencia;
	private boolean valorado;
	
	private String justificacion;
	
	

	private Asistencia asistencia = new Asistencia();
	
	//new variable for ausentismo
	private Integer rango_desde=40;
	private Integer rango_hasta=60;
	//
	
	public AsistenciaList() {
		setExtensionArchivo("Pdf");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));	
		setMaxResults(Constantes.MAX_RESULTS);
	}
	
	public Asistencia getAsistencia() {
		return asistencia;
	}
	
	// Este proceso no va en el construtor se invoca desde el page.xlm
	public void asignarCargo(){
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		
//		this.setGestionaPermiso(gestionPermisoVacacion.buscarPermiso());
//		if (!isGestionaPermiso()) {
//			this.setTipoReporte("Empleado");
//		}
		
		if(this.getAccesoEmpleados() == 0){
			this.setTipoReporte("Empleado");
		}
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}
		
		this.setVerDetalle(true);
		
		this.setCheck1(true);
		this.setCheck2(true);
		this.setCheck3(true);
		
		if(this.getJustificacion().equals("")){
			this.setJustificacion("2");
		}
	}

	public List<Asistencia> getListaAsistencias(){
		setMaxResults(null);
		return this.getResultList();
	}
		
	public Date getFechaAsistencia() {
		return fechaAsistencia;
	}

	public void setFechaAsistencia(Date fechaAsistencia) {
		this.fechaAsistencia = fechaAsistencia;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public Long[] getCargos() {
		return cargos;
	}

	public void setCargos(Long[] cargos) {
		this.cargos = cargos;
	}

	public String getTipoReporte() {
		if (tipoReporte == null){
			tipoReporte = new String();	
		}
		
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public Long[] getDetalleGrupoContratados() {
		return detalleGrupoContratados;
	}

	public void setDetalleGrupoContratados(Long[] detalleGrupoContratados) {
		this.detalleGrupoContratados = detalleGrupoContratados;
	}

	public Long[] getCiudades() {
		return ciudades;
	}

	public void setCiudades(Long[] ciudades) {
		this.ciudades = ciudades;
	}

	public Long[] getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(Long[] departamentos) {
		this.departamentos = departamentos;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	public boolean isVerDetalle() {
		return verDetalle;
	}

	public void setVerDetalle(boolean verDetalle) {
		this.verDetalle = verDetalle;
	}

	public Integer getRango_desde() {
		return rango_desde;
	}

	public void setRango_desde(Integer rango_desde) {
		this.rango_desde = rango_desde;
	}

	public Integer getRango_hasta() {
		return rango_hasta;
	}

	public void setRango_hasta(Integer rango_hasta) {
		this.rango_hasta = rango_hasta;
	}

	public boolean isTabulado() {
		return tabulado;
	}

	public void setTabulado(boolean tabulado) {
		this.tabulado = tabulado;
	}

	public boolean isCheck1() {
		return check1;
	}

	public void setCheck1(boolean check1) {
		this.check1 = check1;
	}

	public boolean isCheck2() {
		return check2;
	}

	public void setCheck2(boolean check2) {
		this.check2 = check2;
	}

	public boolean isCheck3() {
		return check3;
	}

	public void setCheck3(boolean check3) {
		this.check3 = check3;
	}

	public boolean isSinAsistencia() {
		return sinAsistencia;
	}

	public void setSinAsistencia(boolean sinAsistencia) {
		this.sinAsistencia = sinAsistencia;
	}

	public boolean isValorado() {
		return valorado;
	}

	public void setValorado(boolean valorado) {
		this.valorado = valorado;
	}

	public Date getFechaAsistenciaHorario() {
		return fechaAsistenciaHorario;
	}

	public void setFechaAsistenciaHorario(Date fechaAsistenciaHorario) {
		this.fechaAsistenciaHorario = fechaAsistenciaHorario;
	}
	
	public String getJustificacion() {
		if(justificacion == null){
			justificacion = new String();
		}
		return justificacion;
	}

	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

//	public Date getFechaAsistenciaNocturno() {
//		return fechaAsistenciaNocturno;
//	}
//
//	public void setFechaAsistenciaNocturno(Date fechaAsistenciaNocturno) {
//		this.fechaAsistenciaNocturno = fechaAsistenciaNocturno;
//	}

}
