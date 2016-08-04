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

import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("controlEnvioReporteFaltasDiario")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlEnvioReporteFaltasDiario implements Serializable {
 

	private static final long serialVersionUID = -7817264575405821069L;

	@In
    TareaEnvioReporteFaltasDiario tareaEnvioReporteDiario;
 
    private QuartzTriggerHandle quartzTriggerHandle;
    
    @Create
    public void generarQuartz() {
    	
		String hora;
		String minuto;
		String intervaloCron;
		String enviarCorreo;
		  
//		Intervalo cada minuto
//		String cronGeneraPeriodoVacaciones = "0 * * * * ?";
		
		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();
		
		detalleTipoParametroHome.setId(new Long(88));
		detalleTipoParametro = detalleTipoParametroHome.find();
		enviarCorreo = detalleTipoParametro.getDescripcion();
		
		if (enviarCorreo.toLowerCase().equals("activar")) {
			
			detalleTipoParametroHome.setId(new Long(90) );
			detalleTipoParametro = detalleTipoParametroHome.find();
			hora = detalleTipoParametro.getDescripcion();

			detalleTipoParametroHome.setId(new Long(91)); 
			detalleTipoParametro = detalleTipoParametroHome.find();
			minuto = detalleTipoParametro.getDescripcion();
		
			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			quartzTriggerHandle =
					tareaEnvioReporteDiario.ejecutarTarea(new Date(), intervaloCron);
		}
    }

}
