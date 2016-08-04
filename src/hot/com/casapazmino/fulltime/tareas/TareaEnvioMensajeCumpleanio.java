package com.casapazmino.fulltime.tareas;

import java.util.Date;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.procesos.EmailAutomatico;

@Name("notificacionCumpleanio") 
public class TareaEnvioMensajeCumpleanio extends EmailAutomatico {

	@Asynchronous
	@Transactional
	public QuartzTriggerHandle ejecutarproceso(@Expiration Date when,
			@IntervalCron String interval) {
		
		// Tarea que se ejecuta
		this.notificacionCumpleanos();
		
		System.out.println("**************************************");
		System.out.println("*********PROCESO AUTOMATICO***********");
		System.out.println("************CUMPLEAÑOS****************");
		System.out.println("************EJECUTADO*****************");
		System.out.println("**************************************");
		
		return null;
		
	}
	
	
	
	
}
