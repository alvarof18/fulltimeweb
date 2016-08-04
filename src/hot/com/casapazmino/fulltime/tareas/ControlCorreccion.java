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

@Name("controlCorreccion")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlCorreccion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2224634658231742909L;
	String hora; 
	String minuto;
	String intervaloCron;
	
	    @Create
	    public void generarQuartz() {
	    				String hora = "*"; 
			String minuto = "0/59";
			String intervaloCron;
					
			TareaCorreccion tareas = (TareaCorreccion) Component.getInstance("tareaCorreccion", true);
		
			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			 tareas.ejecutarproceso(new Date(), intervaloCron);	
				
	    }

}
