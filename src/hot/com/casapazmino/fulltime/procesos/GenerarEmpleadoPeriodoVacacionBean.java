package com.casapazmino.fulltime.procesos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.EmpleadoPeriodoVacacionHome;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.HistLabo;

@Name("generarEmpleadoPeriodoVacacion")
@Synchronized(timeout = 1800000 )
public class GenerarEmpleadoPeriodoVacacionBean implements GenerarEmpleadoPeriodoVacacion,
		Serializable {

	private static final long serialVersionUID = -678069592069970930L;
	
	@Logger
	Log log;
	
	@In
	EntityManager entityManager;

	@In(create = true)
	EmpleadoPeriodoVacacionHome empleadoPeriodoVacacionHome;
		
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	private ArrayList<Long> ciudades;
	private List<Empleado> empleadosNuevos = new ArrayList<Empleado>();
	private List<Empleado> empleadosAntiguos = new ArrayList<Empleado>();
	
	private Date fechaVacacion;
	private boolean periodoUnico;
	
	private Date fechaInicio;
	private Date fechaFin;
		
	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosCiudad() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado)")
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}
	
	public void separarEmpleadosNuevosAntiguos(){
		List<Empleado> empleados = new ArrayList<Empleado>(); 
		empleados = this.buscarEmpleadosCiudad();
		
		for (Empleado empleado : empleados) {
			if (empleado.getEmpleadoPeriodoVacacions().size() == 0){
				this.getEmpleadosNuevos().add(empleado);
			} else if (empleado.getEmpleadoPeriodoVacacions().size() >= 1){
				this.getEmpleadosAntiguos().add(empleado);				
			}
		}
	}
	
	public void crearPeriodoVacaciones(){
//		this.generarListaEmpleadosNuevos();
		this.generarListaEmpleadosAntiguos();
	}

	public void generarListaEmpleadosNuevos(){
		
		for (Empleado empleado : getEmpleadosNuevos()) {
//			// CREAR UN FACTOR CUANDO LAS VACACIONES SE TOMAN EN MESES DIFERENTES DE 12
			int factorMesesPeriodo;
			factorMesesPeriodo = 12 - empleado.getDetalleGrupoContratado().getMesesPeriodo();
			
 			Calendar calendarioVacacion = Fechas.calendarioActual();
			
			HistLabo histLabo = gestionPermisoVacacion.buscarHistoriaLaboralActivo(empleado);
			//calendarioVacacion.setTime(empleado.getFechaIngreso());
			calendarioVacacion.setTime(histLabo.getFechaIngreso());
			
			calendarioVacacion.add(Calendar.MONTH, (12 - factorMesesPeriodo));
			Date fechaVacacion = calendarioVacacion.getTime();
			
			if (fechaVacacion.equals(this.getFechaVacacion())){
				generarEmpleadoPeriodosVacacion(empleado,fechaVacacion);
			}
		}
	}
	
	public void generarListaEmpleadosAntiguos(){

		for (Empleado empleado : getEmpleadosAntiguos()) {
					
			Date fechaVacacion = gestionPermisoVacacion.buscarFechaMaximaPeriodoVacacion(empleado);
			fechaVacacion = this.fechaVacacion(empleado, fechaVacacion);
			
			if (fechaVacacion.equals(this.getFechaVacacion())){
				generarEmpleadoPeriodosVacacion(empleado,fechaVacacion);
			}
		}
	}	
	
	public String generarEmpleadoPeriodosVacacion(Empleado empleado, Date fechaVacacion){
		
		String fechaDescripcion = Fechas.cambiarFormato(fechaVacacion, "yyyy-MM-dd");
		Calendar calendarioVacacion = Fechas.calendarioActual();
		calendarioVacacion.setTime(fechaVacacion);
				
		// DETEMINA A QUE REGIMEN PERTENECE LOSEP O CODIGO DE TRABAJO
		// RESTA UN MES EN EL CASO QUE SE CARGUE VACACIONES A LOS 11 MESES
		// PARA IGUALAR EL NUEVO PERIODO CON LA FECHA DEL PERIODO ANTERIOR
		// YA QUE EL EL METODO ANTERIOR SE SUMO UN AÑO
		if (empleado.getDetalleGrupoContratado().getMesesPeriodo() == 11) {
			calendarioVacacion.add(Calendar.MONTH, -1);
			fechaVacacion = calendarioVacacion.getTime();
		} 
		
		int diasAdicionales = this.diasAdicionales(empleado);

		if (diasAdicionales < 0) {
			diasAdicionales = 0;
		}
		
		EmpleadoPeriodoVacacion empleadoPeriodoVacacionNuevo = new EmpleadoPeriodoVacacion();
		empleadoPeriodoVacacionNuevo.setEmpleado(empleado);
		empleadoPeriodoVacacionNuevo.setFechaDesde(fechaVacacion);
		empleadoPeriodoVacacionNuevo.setHoras(empleado.getDetalleGrupoContratado().getHorasVacacion() + diasAdicionales);
		empleadoPeriodoVacacionNuevo.setDescripcion("Periodo " + fechaDescripcion);
		
		empleadoPeriodoVacacionHome.persistEmpleadoPeriodo(empleadoPeriodoVacacionNuevo);
		
		return "persisted";
	}
	
	public void actualizarListaEmpleadosAntiguos(){
		
		for (Empleado empleado : getEmpleadosAntiguos()) {
			
			for (EmpleadoPeriodoVacacion empleadoPeriodoVacacion : empleado.getEmpleadoPeriodoVacacions()) {

				Date empleadoPeriodoVacacionfecha = this.fechaVacacion(empleadoPeriodoVacacion.getEmpleado(), empleadoPeriodoVacacion.getFechaDesde());
			
				if (this.getFechaVacacion().equals(empleadoPeriodoVacacionfecha)) {
					actualizarEmpleadoPeriodosVacacion(empleadoPeriodoVacacion,empleadoPeriodoVacacionfecha);
				}
			}
		}
	}
			
	public Date fechaVacacion(Empleado empleado, Date fechaDesde){
		Calendar calendarioVacacion = Fechas.calendarioActual();
		calendarioVacacion.setTime(fechaDesde);
		
		calendarioVacacion.add(Calendar.MONTH, 12);
		
		// DETEMINA A QUE REGIMEN PERTENECE LOSEP O CODIGO DE TRABAJO 
		// ESTA VALIDACION NO ES NECESARIA YA QUE TODAS LAS VACACIONES SE TOMAN ANUALMENTE INDEPENDIENTE DEL REGIMEN
		if (empleado.getDetalleGrupoContratado().getMesesPeriodo() == 11) {
			//calendarioVacacion.add(Calendar.MONTH, 1); 
		} 
//		else if (empleadoPeriodoVacacion.getEmpleado().getDetalleGrupoContratado().getMesesPeriodo() == 12) {
//			calendarioVacacion.add(Calendar.DATE, 15); 
//		}
		
		return calendarioVacacion.getTime();
	}

	public void actualizarEmpleadoPeriodosVacacion(EmpleadoPeriodoVacacion empleadoPeriodoVacacion, Date fechaVacacion){
		
		double diasVacacion = empleadoPeriodoVacacion.getEmpleado().getDetalleGrupoContratado().getHorasVacacion();
		double diasAcumulados = empleadoPeriodoVacacion.getHoras() + diasVacacion;
				
		double diasPerdidos = empleadoPeriodoVacacion.getDiasPerdidos();
		double saldoActual = gestionPermisoVacacion.buscarSaldoVacaciones(empleadoPeriodoVacacion) + diasVacacion;
						
		Calendar calendarioVacacion = Fechas.calendarioActual();
		calendarioVacacion.setTime(empleadoPeriodoVacacion.getFechaDesde());				
		calendarioVacacion.add(Calendar.MONTH, 12);
		Date empleadoPeriodoVacacionfecha = calendarioVacacion.getTime();
				
		int diasAdicionales = this.diasAdicionales(empleadoPeriodoVacacion.getEmpleado());
					
//		Incrementar dias en vacaciones despues de un periodo determinado en detalle grupo contratado
//		Realiza el calculo transformando los años en dias		
		if (diasAdicionales > 0) {
			diasAcumulados = diasAcumulados + diasAdicionales;
		}
				
		empleadoPeriodoVacacion.setHoras(diasAcumulados);
		empleadoPeriodoVacacion.setFechaDesde(empleadoPeriodoVacacionfecha);
		
		log.info("========================= diasAdicionales " + diasAdicionales);		
		log.info("========================= diasVacacion " + diasVacacion);
		log.info("========================= diasAcumulados " + diasAcumulados);
		log.info("========================= diasPerdidos " + diasPerdidos);
		log.info("========================= saldoActual " + saldoActual);
		log.info("========================= empleadoPeriodoVacacion.getEmpleado().getDetalleGrupoContratado().getMaximoHoras() " + empleadoPeriodoVacacion.getEmpleado().getDetalleGrupoContratado().getMaximoHoras());
		log.info("========================= empleadoPeriodoVacacionfecha " + empleadoPeriodoVacacionfecha);
// 
		if (saldoActual > empleadoPeriodoVacacion.getEmpleado().getDetalleGrupoContratado().getMaximoHoras()){
			diasPerdidos = diasPerdidos + (saldoActual - empleadoPeriodoVacacion.getEmpleado().getDetalleGrupoContratado().getMaximoHoras());
			empleadoPeriodoVacacion.setDiasPerdidos(diasPerdidos);
		}

		if (saldoActual > 0) {
			empleadoPeriodoVacacion.setEstado(1);
		}
		
		empleadoPeriodoVacacionHome.update();
	}
	
	public int diasAdicionales (Empleado empleado){
		HistLabo histLabo = gestionPermisoVacacion.buscarHistoriaLaboralActivo(empleado);
		
		//Long numeroDias = Fechas.numeroDias(empleado.getFechaIngreso(), this.getFechaVacacion());
		Long numeroDias = Fechas.numeroDias(histLabo.getFechaIngreso(), this.getFechaVacacion());
		int anios = (int) (numeroDias / 365);
		return (anios - empleado.getDetalleGrupoContratado().getAnios());
	}
		
	@Override
	public void generarPeriodosVacacion() {
		
//		this.separarEmpleadosNuevosAntiguos();
//
//		this.setPeriodoUnico(gestionPermisoVacacion.buscarParametroPeriodoVacaciones());
//		if (periodoUnico){
//			this.actualizarListaEmpleadosAntiguos();
//		} else {
//			this.crearPeriodoVacaciones();	
//		}
//
//		this.getEmpleadosAntiguos().clear();
//		this.getEmpleadosNuevos().clear();
		
		Date now=new Date();
		
		if(this.fechaFin.after(now)){
			FacesMessages.instance().clear();
			FacesMessages.instance().add("Fecha Fin no debe ser mayor que Fecha Actual!");
		}else{
			
			if(this.fechaInicio.after(this.fechaFin)){
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Fecha Desde no debe ser mayor que Fecha Hasta!");
			}else{
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(this.fechaInicio);
				
				Date index=calendar.getTime();
				Date top=this.fechaFin;
				
				while(index.before(top) ||index.compareTo(top)==0){		
					
					this.fechaVacacion=index;
					
					log.info("index: "+index);
					log.info("top: "+top);
					log.info("fechaVacacion: "+fechaVacacion);
				
					this.separarEmpleadosNuevosAntiguos();

					this.setPeriodoUnico(gestionPermisoVacacion.buscarParametroPeriodoVacaciones());
					if (periodoUnico){
						this.actualizarListaEmpleadosAntiguos();
					} else {
						this.crearPeriodoVacaciones();	
					}

					this.getEmpleadosAntiguos().clear();
					this.getEmpleadosNuevos().clear();
					
					calendar=Calendar.getInstance();
					calendar.setTime(index);
					calendar.add(Calendar.DATE, 1);
					
					index=calendar.getTime();
				}
			}
			
		}
		
	}	

	
	public void generarPeriodosVacacionAutomatico() {
		
		this.separarEmpleadosNuevosAntiguos();

		this.setPeriodoUnico(gestionPermisoVacacion.buscarParametroPeriodoVacaciones());
		if (periodoUnico){
			this.actualizarListaEmpleadosAntiguos();
		} else {
			this.crearPeriodoVacaciones();	
		}

		this.getEmpleadosAntiguos().clear();
		this.getEmpleadosNuevos().clear();
	}
		
	
	public ArrayList<Long> getCiudades() {
		if (ciudades == null){
			ciudades = new ArrayList<Long>();
		}
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}

	public List<Empleado> getEmpleadosNuevos() {
		return empleadosNuevos;
	}

	public void setEmpleadosNuevos(List<Empleado> empleadosNuevos) {
		this.empleadosNuevos = empleadosNuevos;
	}

	public List<Empleado> getEmpleadosAntiguos() {
		return empleadosAntiguos;
	}

	public void setEmpleadosAntiguos(List<Empleado> empleadosAntiguos) {
		this.empleadosAntiguos = empleadosAntiguos;
	}

	public Date getFechaVacacion() {
		return fechaVacacion;
	}

	public void setFechaVacacion(Date fechaVacacion) {
		this.fechaVacacion = fechaVacacion;
	}

	public boolean isPeriodoUnico() {
		return periodoUnico;
	}

	public void setPeriodoUnico(boolean periodoUnico) {
		this.periodoUnico = periodoUnico;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}