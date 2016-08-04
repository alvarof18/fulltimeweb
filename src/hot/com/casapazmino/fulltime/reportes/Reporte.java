package com.casapazmino.fulltime.reportes;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface Reporte {
	void crearArchivo(String nombreReporte, Map<String, Object> parametros,
			String rutaReporteFuente, String rutaReporteServidor, String descripcionReporte, String formato);
}
