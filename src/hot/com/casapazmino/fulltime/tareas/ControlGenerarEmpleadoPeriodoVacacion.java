package com.casapazmino.fulltime.tareas;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.action.CiudadList;
import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("controlGenerarEmpleadoPeriodoVacacion")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlGenerarEmpleadoPeriodoVacacion implements Serializable {
 
	private static final long serialVersionUID = -5667344199930118798L;
		
	@In(create = true)
	CiudadList ciudadList;

    @In
    TareaGenerarEmpleadoPeriodoVacacion tareaGenerarEmpleadoPeriodoVacacion;
 
    private QuartzTriggerHandle quartzTriggerHandle;
    
    @Create
    public void generarQuartz() {
    	
		String generaPeriodoVacacion;
		String hora;
		String minuto;
		String intervaloCron;
		
//		Intervalo cada minuto
//		String cronGeneraPeriodoVacaciones = "0 * * * * ?";
    	
		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

		detalleTipoParametroHome.setId(new Long(64));
		detalleTipoParametro = detalleTipoParametroHome.find();
		generaPeriodoVacacion = detalleTipoParametro.getDescripcion();
		
		if (generaPeriodoVacacion.toLowerCase().equals("activar")) {
			
			detalleTipoParametroHome.setId(new Long(65));
			detalleTipoParametro = detalleTipoParametroHome.find();
			hora = detalleTipoParametro.getDescripcion();

			detalleTipoParametroHome.setId(new Long(66));
			detalleTipoParametro = detalleTipoParametroHome.find();
			minuto = detalleTipoParametro.getDescripcion();
			
			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			
			quartzTriggerHandle =
	        		tareaGenerarEmpleadoPeriodoVacacion.ejecutarTarea(new Date(), intervaloCron);
		}
    }

//	@In
//    ScheduleProcessor processor;
//
//    private QuartzTriggerHandle quartzTriggerHandleDoJob;
//
//    public void scheduleTimer() {
//
//    quartzTriggerHandleDoJob = processor.doJob(new Date(), "0 0/1 * * * ?");
//
//    }
}
