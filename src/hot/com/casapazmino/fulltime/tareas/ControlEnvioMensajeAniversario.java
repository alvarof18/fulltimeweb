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

@Name("controlEnvioMensajeAniversario")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlEnvioMensajeAniversario implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2224634658231742909L;
	String hora; 
	String minuto;
	String intervaloCron;
	
	    @Create
	    public void generarQuartz() {
	    	
			String hora = "01"; 
			String minuto = "10";
			String intervaloCron;
					
			TareaEnvioMensajeAniversario tareas = (TareaEnvioMensajeAniversario) Component.getInstance("notificacionAniversario", true);
		
			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			tareas.ejecutarproceso(new Date(), intervaloCron);				
	    }

}
