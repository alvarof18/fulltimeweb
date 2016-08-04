package com.casapazmino.fulltime.procesos;

import java.util.List;

import javax.ejb.Local;

import com.casapazmino.fulltime.model.Asistencia;

@Local
public interface CambioTurno {
	//public String crearPlanificacion();
//	public void anadir();
	public List<Asistencia> buscarAsistencias();

	void guardar();
}
