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

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("controlEnvioMensajeCumpleanio")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlEnvioMensajeCumpleanio implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5822517835585396238L;
	String hora; 
	String minuto;
	String intervaloCron;
	
	 private QuartzTriggerHandle quartzTriggerHandle;
	 
	 @Create
	    public void generarQuartz() {
	    	
			String hora = "00"; 
			String minuto = "01";
			String intervaloCron;
						
			TareaEnvioMensajeCumpleanio tareas = (TareaEnvioMensajeCumpleanio) Component.getInstance("notificacionCumpleanio", true);
			
			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			tareas.ejecutarproceso(new Date(), intervaloCron);	
	    }
}
