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

@Name("controlHorasExtras")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlHorasExtras implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2224634658231742909L;
	String hora; 
	String minuto;
	String intervaloCron;
	
	  private QuartzTriggerHandle quartzTriggerHandle;
	    
	    @Create
	    public void generarQuartz() {
	    	
			String hora; 
			String minuto;
			
			String intervaloCron;
			
			DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
			DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
			
			detalleTipoParametros = detalleTipoParametroList.getNotificacionHorasExtras();
			
			String parametro = detalleTipoParametros.getDescripcion();
			
			String[] cadena = parametro.split(":");
			
			hora = cadena[0].trim();
			minuto = cadena[1].trim();
						
			
			TareaHorasExtras tareas = (TareaHorasExtras) Component.getInstance("tareaHorasExtras", true);
			/*
			//minuto = "05";
			//hora = "10";
			//intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			
			System.out.println("Sin split " + intervaloCron);
			
			
						
			
			minuto = "";
			hora = "";
			intervaloCron = "";
			
			hora = cadena[0].trim();
			minuto = cadena[1].trim();
			*/
			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			
			System.out.println("Con split " + intervaloCron);
			
			tareas.ejecutarproceso(new Date(), intervaloCron);	
			
			
	    }

}
