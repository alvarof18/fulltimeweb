package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.ProgramaVacacion;

@Name("programaVacacionList")
public class ProgramaVacacionList extends EntityQuery<ProgramaVacacion> {

	private static final long serialVersionUID = 8338538941014213332L;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	@In
	EntityManager entityManager;
	
	@Logger
	Log log;
	
	private Long[] ciudades;
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	private String tipoReporte;
	
	private String extensionArchivo;
	private int accesoEmpleados;
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	private boolean tabulado;
	
	private boolean sinPlanificacion;
	
	private Long[] autorizadoSiNo = {(long) 0,(long) 1};	

	private static final String EJBQL = "select programaVacacion from ProgramaVacacion programaVacacion";

	private static final String[] RESTRICTIONS = {
		"programaVacacion.empleado.emplId = #{programaVacacionList.programaVacacion.empleado.emplId}",
		"programaVacacion.periodo = #{programaVacacionList.programaVacacion.periodo}",
		"programaVacacion.generado = #{programaVacacionList.programaVacacion.generado}",
	};
	
	private static final String ORDER = "programaVacacion.periodo desc";
 
	private ProgramaVacacion programaVacacion = new ProgramaVacacion();

	public ProgramaVacacionList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}
	
	public void asignarCargo(){
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		
		if(this.getAccesoEmpleados() == 0){
			this.setTipoReporte("Empleado");
		}
		
		//************************************NEW
		if(this.getAccesoEmpleados() == 1){
			if(this.getTipoReporte().equals("")){
				this.setTipoReporte("Departamento");
			}			
			if(this.getTipoReporte().equals("Cargo")){
				this.setTipoReporte("Departamento");
			}
			if(this.getTipoReporte().equals("Grupo")){
				this.setTipoReporte("Departamento");
			}
		}
		//***********************************
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}
	}
	

	public ProgramaVacacion getProgramaVacacion() {
		return programaVacacion;
	}
	
	public List<ProgramaVacacion> getListaProgramaVacacion(){
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setProgramaVacacion(ProgramaVacacion programaVacacion) {
		this.programaVacacion = programaVacacion;
	}

	public Long[] getCiudades() {
		return ciudades;
	}

	public void setCiudades(Long[] ciudades) {
		this.ciudades = ciudades;
	}

	public Long[] getCargos() {
		return cargos;
	}

	public void setCargos(Long[] cargos) {
		this.cargos = cargos;
	}

	public Long[] getDetalleGrupoContratados() {
		return detalleGrupoContratados;
	}

	public void setDetalleGrupoContratados(Long[] detalleGrupoContratados) {
		this.detalleGrupoContratados = detalleGrupoContratados;
	}

	public Long[] getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(Long[] departamentos) {
		this.departamentos = departamentos;
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

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public boolean isTabulado() {
		return tabulado;
	}

	public void setTabulado(boolean tabulado) {
		this.tabulado = tabulado;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public Long[] getAutorizadoSiNo() {
		return autorizadoSiNo;
	}

	public void setAutorizadoSiNo(Long[] autorizadoSiNo) {
		this.autorizadoSiNo = autorizadoSiNo;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public boolean isSinPlanificacion() {
		return sinPlanificacion;
	}

	public void setSinPlanificacion(boolean sinPlanificacion) {
		this.sinPlanificacion = sinPlanificacion;
	}

	
	
}
