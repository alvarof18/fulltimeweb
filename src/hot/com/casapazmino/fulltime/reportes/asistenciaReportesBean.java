package com.casapazmino.fulltime.reportes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.AsistenciaList;
import com.casapazmino.fulltime.action.EmpleadoList;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.Empleado;

@Name("asistenciaReportes")
public class asistenciaReportesBean implements asistenciaReportes {

	@Override
	public String descargarReporte() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Logger
	Log log;
	
	@In
	EntityManager entityManager;	
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	@In(create = true)
	AsistenciaList asistenciaList;
	
	@In(create = true)
	EmpleadoList empleadoList;
	
	private Asistencia asistencia;
	
	private String extensionArchivo;
	private Date fechaAsistencia;
	
//	private Long[] ciudades;
	
	private ArrayList<Long> ciudades;
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	private String tipoReporte;
	
	private int accesoEmpleados;
	
	// Este proceso no va en el construtor se invoca desde el page.xlm
	public void asignarCargo(){
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		
		if(this.getAccesoEmpleados() == 0){
			this.setTipoReporte("Empleado");
		}
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}
	}
	
	public List<Empleado> getListaEmpleado() {

		List<Empleado> empleados = new ArrayList<Empleado>();
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		
		if (getAccesoEmpleados() ==  Constantes.ACCESO_INDIVIDUAL ){
			this.empleadoList.setEmpleado(empleado);
		} else if (getAccesoEmpleados() == Constantes.ACCESO_SUBORDINADOS){
			this.empleadoList.getEmpleado().setEmpleado(empleado);
			empleados.add(empleado);
		}

		this.empleadoList.setMaxResults(null);
		empleados.addAll(this.empleadoList.getResultList());
		return empleados;

	}
	
	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosCiudad() {
		
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.ciudad.ciudId in (:ciudades)")
				.setParameter("ciudades", ciudades)
				.getResultList();
	}
	
	public void listarEmpledos(){
		
		List<Empleado> empleados = new ArrayList<Empleado>();
		empleados= this.buscarEmpleadosCiudad();
		
		for (Empleado empleado : empleados) {
		}
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

	public Asistencia getAsistencia() {
		if (asistencia == null){
			asistencia = new Asistencia();
		}
		return asistencia;
	}

	public void setAsistencia(Asistencia asistencia) {
		this.asistencia = asistencia;
	}

	public ArrayList<Long> getCiudades() {
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}
}
