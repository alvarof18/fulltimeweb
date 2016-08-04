package com.casapazmino.fulltime.tareas;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Create;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("controlEnvioReporteFaltasSemanal")
@Scope(ScopeType.APPLICATION)
@AutoCreate
@Startup
public class ControlEnvioReporteFaltasSemanal implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 417014989065390801L;

	private QuartzTriggerHandle quartzTriggerHandle;

	@Create
	public void generarQuartz() {

		String hora;
		String minuto;
		String intervaloCron;
		String semanal;
		String enviarCorreo;

		Map<String, String> dia = new HashMap<String, String>();

		dia.put("lunes", "MON");
		dia.put("martes", "TUE");
		dia.put("miercoles", "WED");
		dia.put("jueves", "THU");
		dia.put("viernes", "FRI");
		dia.put("sabado", "SAT");
		dia.put("domingo", "SUN");

		TareaEnvioReporteFaltasSemanal tareas = (TareaEnvioReporteFaltasSemanal) Component
				.getInstance("tareas", true);

		// Intervalo cada minuto
		// String cronGeneraPeriodoVacaciones = "0 * * * * ?";

		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

		detalleTipoParametroHome.setId(new Long(92));
		detalleTipoParametro = detalleTipoParametroHome.find();
		enviarCorreo = detalleTipoParametro.getDescripcion();

		if (enviarCorreo.toLowerCase().equals("activar")) {
			
			// dia de la semana
			detalleTipoParametros = detalleTipoParametroList.getDiaReporteFaltasSemanal();
			String parametro = detalleTipoParametros.getDescripcion().toLowerCase();

			semanal = dia.get(parametro);
			

			detalleTipoParametroHome.setId(new Long(94));
			detalleTipoParametro = detalleTipoParametroHome.find();
			hora = detalleTipoParametro.getDescripcion();

			detalleTipoParametroHome.setId(new Long(95));
			detalleTipoParametro = detalleTipoParametroHome.find();
			minuto = detalleTipoParametro.getDescripcion();

			intervaloCron = "0 " + minuto + " " + hora + " ? * " + semanal;

			tareas.ejecutarproceso(new Date(), intervaloCron);
		}

	}

}
