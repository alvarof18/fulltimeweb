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

@Name("controlEnvioMensajeSalida")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlEnvioMensajeSalida implements Serializable{

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
	    	
			String hora = "00"; 
			String minuto = "00";
			String intervaloCron;
			String[] cadena = new String[10];
			
			DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
			DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

			detalleTipoParametros = detalleTipoParametroList.getHoraFaltas();
			
			String parametro = detalleTipoParametros.getDescripcion();
					
		    cadena = parametro.split(";");	
			
		    try{
		    
				hora = cadena[1].trim();
				cadena[1] = "";
				parametro = "";
				
				detalleTipoParametros = detalleTipoParametroList.getMinutosFaltas();
				parametro = detalleTipoParametros.getDescripcion();
						
				cadena = parametro.split(";");
				minuto = cadena[1].trim();
			
		    } catch(Exception e){
		    	System.out.println("Ha ocurrido un error al momento de obtener la hora de ejecucion del proceso Notificacion de Salida Anticipada");
		    	
		    }	
				
			TareaEnvioMensajeSalida tareas = (TareaEnvioMensajeSalida) Component.getInstance("notificacionSalida", true);
		
			intervaloCron = "0 " + minuto + " " + hora + " * * ?";
			
			tareas.ejecutarproceso(new Date(), intervaloCron);	
			
			
	    }

}
