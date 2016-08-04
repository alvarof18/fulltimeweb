package com.casapazmino.fulltime.tareas;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("controlPlanificarHorarioFijo")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlPlanificarHorarioFijo implements Serializable {

	private static final long serialVersionUID = 9123918385915267515L;
		
	private QuartzTriggerHandle quartzTriggerHandle;
	
	@Create
	public void generarQuartz() {
		
		String planificarHorarioFijo;
		String hora;
		String minuto;
		String intervaloCron;
		
		TareaPlanificarHorarioFijo tareaPlanificarHorarioFijo =  (TareaPlanificarHorarioFijo) Component
				.getInstance("tareaPlanificaHorarioFijo", true);
		
		// Intervalo cada minuto
		// String cronGeneraPeriodoVacaciones = "0 * * * * ?";

		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

		detalleTipoParametroHome.setId(new Long(79));
		detalleTipoParametro = detalleTipoParametroHome.find();
		planificarHorarioFijo = detalleTipoParametro.getDescripcion();

		if (planificarHorarioFijo.toLowerCase().equals("activar")) {

			detalleTipoParametroHome.setId(new Long(80));
			detalleTipoParametro = detalleTipoParametroHome.find();

			// En este caso el parametro viene en una sola variable Ej: 02:30
			// Por esa razon hay que separarlo en hora y minuto
			hora = detalleTipoParametro.getDescripcion().substring(0, 2);
			minuto = detalleTipoParametro.getDescripcion().substring(3, 5);

			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			
			quartzTriggerHandle = 
					tareaPlanificarHorarioFijo.ejecutarTarea(new Date(), intervaloCron);
		}
	}
}
