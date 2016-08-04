package com.casapazmino.fulltime.tareas;

import java.util.Date;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.procesos.EmailAutomatico;

@Name("tareaHorasExtras") 
public class TareaHorasExtras extends  EmailAutomatico {
	
	@Asynchronous
	@Transactional
	public QuartzTriggerHandle ejecutarproceso(@Expiration Date when,
			@IntervalCron String interval) {
		
		// Tarea que se ejecuta
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
		detalleTipoParametros = detalleTipoParametroList.getBaseDatos();
		
		String parametro = detalleTipoParametros.getDescripcion();
		this.notificacionHorasExtras(parametro);
		return null;
		
	}

}
