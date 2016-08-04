package com.casapazmino.fulltime.tareas;

import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.procesos.EmailAutomatico;

/**
 * @author Drosan
 * 
 * Esta tarea ejecuta el proceso generarPeriodosVacacion() 
 * heredado de la clase GenerarEmpleadoPeriodoVacacionBean
 * 
 * Para ejecutar esta tarea se necesita un Parametro:
 * Fecha Actual
 *
 */

@Name("tareaEnvioReporteDiario")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class TareaEnvioReporteFaltasDiario extends EmailAutomatico {

	private static final long serialVersionUID = -6258017437615619135L;

	@Asynchronous
	@Transactional
	public QuartzTriggerHandle ejecutarTarea(@Expiration Date when,
			@IntervalCron String interval) {
		
		// Tarea que se ejecuta
		 this.NotificacionFaltasDiarias();
		
		return null;
	}
}