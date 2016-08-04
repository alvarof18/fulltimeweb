package com.casapazmino.fulltime.tareas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.action.CiudadList;
import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.action.EnviarMail;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.procesos.GenerarEmpleadoPeriodoVacacionBean;

/**
 * @author Drosan
 * 
 * Esta tarea ejecuta el proceso generarPeriodosVacacion() 
 * heredado de la clase GenerarEmpleadoPeriodoVacacionBean
 * 
 * Para ejecutar esta tarea se necesita dos Parametros:
 * Fecha Actual y lista de ciudades
 *
 */

@Name("tareaGenerarEmpleadoPeriodoVacacion")
@AutoCreate
@Scope(ScopeType.APPLICATION)
public class TareaGenerarEmpleadoPeriodoVacacion extends GenerarEmpleadoPeriodoVacacionBean {

	private static final long serialVersionUID = -6258017437615619135L;

	@Asynchronous
	@Transactional
	public QuartzTriggerHandle ejecutarTarea(@Expiration Date when,
			@IntervalCron String interval) {

		Date fechaActual = new Date();

		CiudadList ciudadList = (CiudadList) Component.getInstance(
				"ciudadList", true);

//		Buscar y Colocar ciudades para generacion de proceso
		ArrayList<Ciudad> ciudadesList = new ArrayList<Ciudad>();
		ciudadesList = (ArrayList<Ciudad>) ciudadList.getListaCiudades();

		for (Ciudad ciudad : ciudadesList) {
			this.getCiudades().add(ciudad.getCiudId());
		}

//		CAMBIA EL FORMATO A LA FECHA ACTUAL SINO NO REALIZA EL PROCESO
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFecha = Fechas.cambiarFormato(fechaActual, "yyyy-MM-dd");
		
//		Colocar fecha actual para generacion de proceso
		try {
			fechaActual = formato.parse(stringFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.setFechaVacacion(fechaActual);
		
		// Tarea que se ejecuta
		this.generarPeriodosVacacionAutomatico();
		
		this.EnviarNotificacionGeneracionPeriodoVacacionAutomatico();
		System.out.println("**************************************************");
		System.out.println("**************************************************");
		System.out.println("*********PROCESO PERIODICO EJECTUTADO*************");
		System.out.println("**************************************************");
		System.out.println("**************************************************");

		// Regresa Parametros a nulo 
		// caso contrario los acumula para el siguiente proceso
		this.setFechaVacacion(null);
		this.getCiudades().clear();

		return null;
	}
	
	private void EnviarNotificacionGeneracionPeriodoVacacionAutomatico(){
		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

		detalleTipoParametroHome.setId(new Long(77));
		detalleTipoParametro = detalleTipoParametroHome.find();
		String notificar = detalleTipoParametro.getDescripcion();
		
		if (notificar.toLowerCase().equals("activar")) {
			List<String> lista=getListaCorreos();
			for(int i=0;i<lista.size();i++){
				
				Calendar calendar = Calendar.getInstance();
				
				String CuerpoMensaje = "Este correo es para informar que se ha generado el proceso de carga de vacaciones con fecha "+calendar.getTime()+".";
				String Asunto = "GENERACIÓN DE PERIODO DE VACACION";
				
				EnviarMail enviarMail = new EnviarMail();
				enviarMail.enviar(CuerpoMensaje, lista.get(i), Asunto, "E");
				
			}
		}
	}
	
	private List<String> getListaCorreos(){
		List<String> lista = new ArrayList<String>();
		
		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

		detalleTipoParametroHome.setId(new Long(78));
		detalleTipoParametro = detalleTipoParametroHome.find();
		String correos = detalleTipoParametro.getDescripcion();
		
		if(correos.length()>0){
			String aux="";
			for(int i=0;i<correos.length();i++){
				if(correos.charAt(i)==','||correos.charAt(i)==';'||correos.charAt(i)=='|'){
					lista.add(aux);
					aux="";
				}else{
					aux=aux+correos.charAt(i);
				}
			}
			
			if(aux.length()>0){
				lista.add(aux);
				aux="";
			}
		}
		
		return lista;
	}
	
}