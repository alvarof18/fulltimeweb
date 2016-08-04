package com.casapazmino.fulltime.action;

import javax.ejb.Local;

@Local
public interface ReportesFulltime {
	void listadoAtrasos();
	void listadoCargos();
	void listadoCiudadFeriados();
	void listadoCiudades();
	void listadoDepartamentos();
	void listadoDetalleGrupoContratados();
	void listadoDetalleHorarios();
	void listadoDetalleTipoParametros();
	void listadoDiscapacidades();
	void listadoEmpleadoDeclaraBienes();
	void listadoEmpleados();
	void listadoFeriados();
	void listadoGrupoContratados();
	void listadoHorasExtras();
	void listadoHorarios();
	void listadoPermisos();
	void listadoProvincias();
	void listadoRegiones();
	void listadoRelojes();
	void listadoSolicitudVacaciones();
	void listadoTipoCargos();
	void listadoTipoParametros();
	void listadoTipoPermisos();
	void listadoTitulos();
	void listadoProgramacionVacaciones();
	
	void reporteAtrasos();
	void reporteAsistenciaDetalle();
	void reporteAsistenciaMultiple();
	void reporteEntradasSalidas();
	void reporteFaltas();
	void reporteHorasExtras();
	void reporteHorasTrabajadas();
	void reporteSalidasAntes();
	void reporteTimbres();
	void reporteTimbresIncompletos();
	void reporteTimbresMRL();
	
	void reporteKardexVacaciones();
	void reportePermisos();
	void reporteSolicitudVacaciones();
	
	//new
	void reportePlanHorasExtras();
	void reporteMultiplePlanExtras();
	void reporteSolicitudVacacionIndividual();
	void reporteSolicitudPermisoIndividual();
	void reporteAccionPermisoIndividual();
	void reporteAccionVacacionIndividual();
	//end new
	//new new 
	void reportePuntualidad();
	void reporteEmpleadosActivos();
	void reporteEmpleadosInactivos();
	void reporteEmpleadoPuntuales();
	void reporteEmpleadosAtrasos();
	void reporteEmpleadosSalidaAntes();
	void reporteEmpleadosFaltas();
	void reporteEmpleadosPermisos();
	void reporteEmpleadosVacaciones();
	void reporteTiempoAlmuerzo();
	void reporteAtrasosEstadisticos();
	void reporteFaltasEstadistico();
	void reportePuntualidadEstadistico();
	void reporteSalidasAntesEstadistico();
	void reporteTiempoAlmuerzoEstadistico();
	void reportePermisosEstadistico();
	void reporteSolicitudVacacionesEstadistico();
	void reporteHorasExtrasEstadisticos();
	void reporteAusentismo();
	
	void reporteTiempoAdicional();
	void reporteTiempoLaborado();
	void reporteSaldoVacacion();
	void reporteHorasExtraValorado();
	void reporteProgramaVacacion();
	void reporteProgramaVacaciones();
	
	void reporteEmpleados();
	
}