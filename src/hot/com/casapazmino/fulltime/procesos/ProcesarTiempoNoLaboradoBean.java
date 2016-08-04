package com.casapazmino.fulltime.procesos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.AsistenciaHome;
import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.action.PermisoHome;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.TipoPermiso;

@Name("procesarTiempoNoLaborado")
@Synchronized(timeout = 1800000 )
public class ProcesarTiempoNoLaboradoBean implements ProcesarTiempoNoLaborado {

	@Logger
	Log log;
	
	@In
	EntityManager entityManager;
	
	@In(create = true)
	PermisoHome permisoHome;
	
	@In(create = true)
	AsistenciaHome asistenciaHome;

	private Date fechaDesde;
	private Date fechaHasta;
	
	private ArrayList<Long> ciudades;
	private TipoPermiso tipoPermiso = new TipoPermiso();
	
	private List<Empleado> empleadosSinPeriodoVacacion = new ArrayList<Empleado>();
	
	Fechas fechas = new Fechas();

//	public int validarTipoPermiso(){
//		List<TipoPermiso> tipoPermisos = new ArrayList<TipoPermiso>();
//		tipoPermisos = this.buscarPermisoGeneraJustificacion();
//		
//		if (tipoPermisos.size() != 1) {
//			return 0;
//		}
//		
//		return 1;
//		
//	}
	
	public List<Empleado> validarEmpleados(List<Empleado> empleados){
		for (Empleado empleado : empleados) {
			if (empleado.getEmpleadoPeriodoVacacions().size() == 0){
				this.getEmpleadosSinPeriodoVacacion().add(empleado);
			}  
		}
		return empleados;	
	}	
	
	@Override
	public String procesar() {
		
		List<TipoPermiso> tipoPermisos = new ArrayList<TipoPermiso>();
		tipoPermisos = this.buscarPermisoGeneraJustificacion();
		
		if (tipoPermisos.size() != 1) {
			FacesMessages.instance().add(
					"Verifique Tipo de Permiso - No existe o existe mas de un registro marcado con Generación Automatica");
			return "";
		}
		
		for (TipoPermiso tipoPermiso : tipoPermisos) {
			this.tipoPermiso = tipoPermiso;
		}

		List<Empleado> empleados = this.buscarEmpleadosCiudad();
				
		this.validarEmpleados(empleados);
		if (this.getEmpleadosSinPeriodoVacacion().size() != 0){
			FacesMessages.instance().add(
					"Existe Empleados que no tienen asignado vacaciones");
			return "";
		}
		
		for (Empleado empleado2 : empleados) {
			
			List<Asistencia> asistencias = new ArrayList<Asistencia>();
			asistencias = this.buscarAtrasos(empleado2);
			
			for (Asistencia asistencia : asistencias) {
				
				Calendar calendarioHorario = Fechas.calendarioActual();
				calendarioHorario.setTime(asistencia.getFechaHoraHorario());
				calendarioHorario.add(Calendar.MINUTE, asistencia.getAsisMaxMinuto());

//				REALIZA PERMISOS TOMANDO EL CUENTA EL TIEMPO DE TOLERANCIA
				if ( asistencia.getFechaHoraTimbre().after(calendarioHorario.getTime()) ) {	
					
					Permiso permiso = this.crearPermiso(empleado2, asistencia);
					
					this.crearPermiso(permiso);
					
					this.cambiarEstadoAsistencia(asistencia);					
					
				}
			}
			
			asistencias.clear();
			asistencias = this.buscarSalidasAnticipadas(empleado2);
						
			for (Asistencia asistencia : asistencias) {
				Permiso permiso = this.crearPermiso(empleado2, asistencia);
				this.crearPermiso(permiso);
   				this.cambiarEstadoAsistencia(asistencia);				
			}
			
			List<Permiso> permisos = new ArrayList<Permiso>();
			permisos = this.buscarPermisos(empleado2);
			for (Permiso permiso : permisos) {
				Permiso permisoModificado = this.modificarPermiso(permiso);
				this.grabarPermiso(permisoModificado);
			}
		}
		return null;
	}
	
	public Permiso crearPermiso(Empleado empleado, Asistencia asistencia){
		double totalAtrasos = 0;
		
		Calendar fechaHoraHorario = Calendar.getInstance();
		Calendar fechaHoraTimbre = Calendar.getInstance();
		
		fechaHoraHorario.setTime(asistencia.getFechaHoraHorario());
		fechaHoraTimbre.setTime(asistencia.getFechaHoraTimbre());
		
		totalAtrasos = totalAtrasos + (fechaHoraTimbre.getTimeInMillis() - fechaHoraHorario.getTimeInMillis());

		double horasDescuento = totalAtrasos / 3600000;
		
		Permiso permiso = new Permiso();
		permiso.setTipoPermiso(this.tipoPermiso);
		permiso.setDetalleTipoParametro(this.cambiarEstadoAutorizacion(Constantes.DECISION_SI));
		permiso.setEmpleadoPeriodoVacacion(this.asignarEmpleadoPeriodoVacacion(empleado));
		permiso.setFecha(new Date());
		permiso.setEmpleadoByEmplId(empleado);
		permiso.setEmpleadoByEmpEmplId(empleado.getEmpleado());
		permiso.setObservacion("Descuento mensual permiso injustificados");
		permiso.setTimbra(true);
		permiso.setFactor(new Double(1));
		permiso.setTrabajadas(true);
		
		if (horasDescuento > 0) {
			permiso.setFechaDesde(asistencia.getFechaHoraHorario());
			permiso.setFechaHasta(asistencia.getFechaHoraTimbre());
			permiso.setHoraSalida(asistencia.getFechaHoraHorario());
			permiso.setHoraRetorno(asistencia.getFechaHoraTimbre());
		} else {
			permiso.setFechaDesde(asistencia.getFechaHoraTimbre());
			permiso.setFechaHasta(asistencia.getFechaHoraHorario());
			permiso.setHoraSalida(asistencia.getFechaHoraTimbre());
			permiso.setHoraRetorno(asistencia.getFechaHoraHorario());
		}
		
		permiso.setDias(0);
		permiso.setNumeroHoras(Math.abs(horasDescuento));
		permiso.setEstado(Constantes.PERMISO_PROCESADO);
		permiso.setAprobacion(1);
		permiso.setNivel(1);
		permiso.setNivelRequerido(1);
		permiso.setEstadoActual("2");
		
		permiso.setDiasLibres(0);
		
		return permiso;
	}
	
	public Permiso modificarPermiso(Permiso permiso){
		
		if (permiso.getHoraSalida() != null && permiso.getHoraRetorno() != null) {
			log.info("==================== Hora Salida y retorno es diferente nulo " );
			Date fechaDesde = new Date();
			Date fechaHasta = new Date();
			
			fechaDesde = permiso.getFechaDesde();
			fechaHasta = permiso.getFechaHasta();
			
			permiso.setFechaDesde(permiso.getHoraSalida());
			permiso.setFechaHasta(permiso.getHoraRetorno());
			permiso.setHoraSalida(fechaDesde);
			permiso.setHoraRetorno(fechaHasta);
			permiso.setNumeroHoras(fechas.numeroHoras(permiso.getFechaDesde(), permiso.getFechaHasta()));
		}
		
		permiso.setEstado(Constantes.PERMISO_PROCESADO);
		return permiso;
	}

	public String grabarPermiso(Permiso permiso){
		permisoHome.setInstance(permiso);
		return permisoHome.modificarPermiso();
	}
	
	public String crearPermiso(Permiso permiso){
		permisoHome.setInstance(permiso);
		return permisoHome.crearPermiso();
	}
	
	@SuppressWarnings("unchecked")
	public List<Asistencia> buscarAtrasos(Empleado empleado){
				
		return entityManager.createQuery("select a from Asistencia a where " +
				"a.empleado.emplId = (:empleado) And " +
				ControlBaseDatos.modificadorConvertirFecha + "a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
				"a.entradaSalida = (:entradaSalida) And " +
				"a.fechaHoraTimbre >  a.fechaHoraHorario And " +
				"a.estado = (:estado)")
		.setParameter("empleado", empleado.getEmplId())
		.setParameter("fechaDesde", this.getFechaDesde())
		.setParameter("fechaHasta", this.getFechaHasta())
		.setParameter("entradaSalida", Constantes.ENTRADA)
		.setParameter("estado", "R")
		.getResultList();
		
//		List<Asistencia> asistenciaAtrasos = new ArrayList<Asistencia>();
//		if (ControlBaseDatos.baseDatos.equals(Constantes.SQLSERVER)){
//			asistenciaAtrasos = entityManager.createQuery("select a from Asistencia a where " +
//					"a.empleado.emplId = (:empleado) And " +
//					"convert(date, a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//					"a.entradaSalida = (:entradaSalida) And " +
//					"a.fechaHoraTimbre >  a.fechaHoraHorario And " +
//					"a.estado = (:estado)")
//			.setParameter("empleado", empleado.getEmplId())
//			.setParameter("fechaDesde", this.getFechaDesde())
//			.setParameter("fechaHasta", this.getFechaHasta())
//			.setParameter("entradaSalida", Constantes.ENTRADA)
//			.setParameter("estado", "R")
//			.getResultList();			
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.ORACLE)){
//			asistenciaAtrasos = entityManager.createQuery("select a from Asistencia a where " +
//					"a.empleado.emplId = (:empleado) And " +
//					"TRUNC(a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//					"a.entradaSalida = (:entradaSalida) And " +
//					"a.fechaHoraTimbre >  a.fechaHoraHorario And " +
//					"a.estado = (:estado)")
//			.setParameter("empleado", empleado.getEmplId())
//			.setParameter("fechaDesde", this.getFechaDesde())
//			.setParameter("fechaHasta", this.getFechaHasta())
//			.setParameter("entradaSalida", Constantes.ENTRADA)
//			.setParameter("estado", "R")
//			.getResultList();
//
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.POSTGRES)) {
//			asistenciaAtrasos = entityManager.createQuery("select a from Asistencia a where " +
//					"a.empleado.emplId = (:empleado) And " +
//					"DATE(a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//					"a.entradaSalida = (:entradaSalida) And " +
//					"a.fechaHoraTimbre >  a.fechaHoraHorario And " +
//					"a.estado = (:estado)")
//			.setParameter("empleado", empleado.getEmplId())
//			.setParameter("fechaDesde", this.getFechaDesde())
//			.setParameter("fechaHasta", this.getFechaHasta())
//			.setParameter("entradaSalida", Constantes.ENTRADA)
//			.setParameter("estado", "R")
//			.getResultList(); 
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.MYSQL)) {
//			asistenciaAtrasos = entityManager.createQuery("select a from Asistencia a where " +
//				"a.empleado.emplId = (:empleado) And " +
//				"DATE(a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//				"a.entradaSalida = (:entradaSalida) And " +
//				"a.fechaHoraTimbre >  a.fechaHoraHorario And " +
//				"a.estado = (:estado)")
//				.setParameter("empleado", empleado.getEmplId())
//				.setParameter("fechaDesde", this.getFechaDesde())
//				.setParameter("fechaHasta", this.getFechaHasta())
//				.setParameter("entradaSalida", Constantes.ENTRADA)
//				.setParameter("estado", "R")
//				.getResultList();
//		}
//		return asistenciaAtrasos;		
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> buscarSalidasAnticipadas(Empleado empleado){

		return entityManager.createQuery("select a from Asistencia a where " +
				"a.empleado.emplId = (:empleado) And " +
				ControlBaseDatos.modificadorConvertirFecha + "a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
				"a.entradaSalida = (:entradaSalida) And " +
				"a.fechaHoraTimbre <  a.fechaHoraHorario And " +
				"a.estado = (:estado)")
		.setParameter("empleado", empleado.getEmplId())
		.setParameter("fechaDesde", this.getFechaDesde())
		.setParameter("fechaHasta", this.getFechaHasta())
		.setParameter("entradaSalida", Constantes.SALIDA)
		.setParameter("estado", "R")
		.getResultList();
		
//		List<Asistencia> asistenciaSalidaAntes = new ArrayList<Asistencia>();		
//		if (ControlBaseDatos.baseDatos.equals(Constantes.SQLSERVER)){
//			asistenciaSalidaAntes = entityManager.createQuery("select a from Asistencia a where " +
//					"a.empleado.emplId = (:empleado) And " +
//					"convert(DATE,a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//					"a.entradaSalida = (:entradaSalida) And " +
//					"a.fechaHoraTimbre <  a.fechaHoraHorario And " +
//					"a.estado = (:estado)")
//			.setParameter("empleado", empleado.getEmplId())
//			.setParameter("fechaDesde", this.getFechaDesde())
//			.setParameter("fechaHasta", this.getFechaHasta())
//			.setParameter("entradaSalida", Constantes.SALIDA)
//			.setParameter("estado", "R")
//			.getResultList();
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.ORACLE)){
//			asistenciaSalidaAntes = entityManager.createQuery("select a from Asistencia a where " +
//					"a.empleado.emplId = (:empleado) And " +
//					"TRUNC(a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//					"a.entradaSalida = (:entradaSalida) And " +
//					"a.fechaHoraTimbre <  a.fechaHoraHorario And " +
//					"a.estado = (:estado)")
//			.setParameter("empleado", empleado.getEmplId())
//			.setParameter("fechaDesde", this.getFechaDesde())
//			.setParameter("fechaHasta", this.getFechaHasta())
//			.setParameter("entradaSalida", Constantes.SALIDA)
//			.setParameter("estado", "R")
//			.getResultList();
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.POSTGRES)){
//			asistenciaSalidaAntes = entityManager.createQuery("select a from Asistencia a where " +
//					"a.empleado.emplId = (:empleado) And " +
//					"DATE(a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//					"a.entradaSalida = (:entradaSalida) And " +
//					"a.fechaHoraTimbre <  a.fechaHoraHorario And " +
//					"a.estado = (:estado)")
//			.setParameter("empleado", empleado.getEmplId())
//			.setParameter("fechaDesde", this.getFechaDesde())
//			.setParameter("fechaHasta", this.getFechaHasta())
//			.setParameter("entradaSalida", Constantes.SALIDA)
//			.setParameter("estado", "R")
//			.getResultList();
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.MYSQL)){
//			asistenciaSalidaAntes = entityManager.createQuery("select a from Asistencia a where " +
//						"a.empleado.emplId = (:empleado) And " +
//						"DATE(a.fechaHoraHorario) Between (:fechaDesde) and (:fechaHasta) And " +
//						"a.entradaSalida = (:entradaSalida) And " +
//						"a.fechaHoraTimbre <  a.fechaHoraHorario And " +
//						"a.estado = (:estado)")
//				.setParameter("empleado", empleado.getEmplId())
//				.setParameter("fechaDesde", this.getFechaDesde())
//				.setParameter("fechaHasta", this.getFechaHasta())
//				.setParameter("entradaSalida", Constantes.SALIDA)
//				.setParameter("estado", "R")
//				.getResultList();
//		}
//		return asistenciaSalidaAntes;
	}

	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisos (Empleado empleado){
		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p inner join p.tipoPermiso tp where " +
						"p.empleadoByEmplId.emplId = (:empleado) And " +
						"p.tipoPermiso.descuento = 'Vacaciones' And " + 
						ControlBaseDatos.modificadorConvertirFecha + "p.fechaDesde) Between (:permisoDesde) and (:permisoHasta) And " +
						"p.detalleTipoParametro.dtpaId = (:autorizado) And " +
						"p.estado is null ")
				.setParameter("empleado", empleado.getEmplId())
				.setParameter("permisoDesde", this.getFechaDesde())
				.setParameter("permisoHasta", this.getFechaHasta())
				.setParameter("autorizado", Constantes.DECISION_SI)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoPermiso> buscarPermisoGeneraJustificacion(){
		return  (List<TipoPermiso>) entityManager
				.createQuery(
						"select tp from TipoPermiso tp where tp.generaJust = (:generaJust)")
				.setParameter("generaJust", true)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosCiudad() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.ciudad.ciudId in (:ciudades) And e.empleado.emplId != null ")
				.setParameter("ciudades", getCiudades())
				.getResultList();
	}
	
	public String cambiarEstadoAsistencia(Asistencia asistencia){
		
		asistencia.setEstado(Constantes.ASISTENCIA_PERMISO);
		asistenciaHome.setInstance(asistencia);
		return asistenciaHome.persist();
		
	}
	
	public DetalleTipoParametro cambiarEstadoAutorizacion(Long decision){
		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component.getInstance("detalleTipoParametroHome", true); 
		detalleTipoParametroHome.setId(decision);
		DetalleTipoParametro  detalleTipoParametro = detalleTipoParametroHome.find();
		return detalleTipoParametro;
	}
	
	public EmpleadoPeriodoVacacion asignarEmpleadoPeriodoVacacion (Empleado empleado){
		EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
		for (EmpleadoPeriodoVacacion empleadoPeriodoVacacion2 : empleado.getEmpleadoPeriodoVacacions()) {
			empleadoPeriodoVacacion = empleadoPeriodoVacacion2;
		}
		
		return empleadoPeriodoVacacion;
	}


	public ArrayList<Long> getCiudades() {
		if(ciudades==null){
			ciudades=new ArrayList<Long>();
		}
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public TipoPermiso getTipoPermiso() {
		return tipoPermiso;
	}

	public void setTipoPermiso(TipoPermiso tipoPermiso) {
		this.tipoPermiso = tipoPermiso;
	}

	public List<Empleado> getEmpleadosSinPeriodoVacacion() {
		return empleadosSinPeriodoVacacion;
	}

	public void setEmpleadosSinPeriodoVacacion(
			List<Empleado> empleadosSinPeriodoVacacion) {
		this.empleadosSinPeriodoVacacion = empleadosSinPeriodoVacacion;
	}
}
