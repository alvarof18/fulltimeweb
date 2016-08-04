package com.casapazmino.fulltime.procesos;

import javax.ejb.Local;

@Local
public interface PlanificacionRangoFechas {
	public String crearPlanificacionRangoFechas();
	public void eliminarPlanificacion();
	public void cargarTimbres();
	public void planificarHorariosFijos();
}
