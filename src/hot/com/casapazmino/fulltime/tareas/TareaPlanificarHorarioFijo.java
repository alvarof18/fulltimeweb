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

import com.casapazmino.fulltime.procesos.PlanificacionRangoFechasBean;

@Name("tareaPlanificaHorarioFijo")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class TareaPlanificarHorarioFijo extends PlanificacionRangoFechasBean {

	@Asynchronous
	@Transactional
	public QuartzTriggerHandle ejecutarTarea(@Expiration Date when,
			@IntervalCron String interval) {

		// Tarea que se ejecuta
		this.planificarHorariosFijos();

		return null;
	}
}
