package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.Stateless;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jfree.util.Log;

import com.casapazmino.fulltime.comun.Comunes;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.reportes.ReporteBean;

/**
 * @author SndVll
 */
@Stateless
@Name("reportesFulltime")
public class ReportesFulltimeBean extends ReporteBean implements ReportesFulltime {

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	public ReportesFulltimeBean() {
	}
	
	// **************************************
	// LISTADOS
	// **************************************

	@Override
	public void listadoRegiones() {
		
		// FUNCIONA IGUAL QUE @In
		RegionList regionList = (RegionList) Component.getInstance("regionList",true);
		
		String descripcion = regionList.getRegion().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoRegiones", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoRegiones", regionList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoAtrasos() {
		AtrasoList atrasoList = (AtrasoList) Component.getInstance("atrasoList",true);

		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			this.crearArchivo("listadoAtrasos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoAtrasos", atrasoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void listadoEmpleados() {
		EmpleadoList empleadoList = (EmpleadoList) Component.getInstance("empleadoList",true);
		String codigo = empleadoList.getEmpleado().getCodigoEmpleado()+"%";
		String cedula = empleadoList.getEmpleado().getCedula()+"%";
		String apellido = empleadoList.getEmpleado().getApellido()+"%";
		String nombre = empleadoList.getEmpleado().getNombre()+"%";
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		try {				
			parametros.put("codigo", codigo);
			parametros.put("cedula", cedula);
			parametros.put("apellido", apellido);
			parametros.put("nombre", nombre);
			this.crearArchivo("listadoEmpleados", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
			"listadoEmpleados", empleadoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void listadoCargos() {
		CargoList cargoList = (CargoList) Component.getInstance("cargoList",true);
		
		String descripcion = cargoList.getCargo().getDescripcion() + "%";		
		
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoCargos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoCargos", cargoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoCiudades() {
		CiudadList ciudadList = (CiudadList) Component.getInstance("ciudadList",true);
		
		String descripcion = ciudadList.getCiudad().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoCiudades", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoCiudades", ciudadList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoCiudadFeriados() {
		CiudadFeriadoList ciudadFeriadoList = (CiudadFeriadoList) Component.getInstance("ciudadFeriadoList",true);
		
		String descripcion = ciudadFeriadoList.getCiudadFeriado().getCiudad().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoCiudadFeriados", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoCiudadFeriados", ciudadFeriadoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoDepartamentos() {
		DepartamentoList departamentoList = (DepartamentoList) Component.getInstance("departamentoList",true);
		
		String descripcion = departamentoList.getDepartamento().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoDepartamentos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoDepartamentos", departamentoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoDetalleGrupoContratados() {
		DetalleGrupoContratadoList detalleGrupoContratadoList = (DetalleGrupoContratadoList) Component.getInstance("detalleGrupoContratadoList",true);
		
		String descripcion = detalleGrupoContratadoList.getDetalleGrupoContratado().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoDetalleGrupoContratados", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoDetalleGrupoContratados", detalleGrupoContratadoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoDetalleHorarios() {
		DetalleHorarioList detalleHorarioList = (DetalleHorarioList) Component.getInstance("detalleHorarioList",true);
		// descripcion de horario para listar detalle horario
		//String descripcion = detalleHorarioList.getDetalleHorario().getHorario().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			//parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoDetalleHorarios", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoDetalleHorarios", detalleHorarioList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoDetalleTipoParametros() {
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList",true);

		String descripcion = detalleTipoParametroList.getDetalleTipoParametro().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoDetalleTipoParametros", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoDetalleTipoParametros", detalleTipoParametroList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoTipoPermisos() {
		TipoPermisoList tipoPermisoList = (TipoPermisoList) Component.getInstance("tipoPermisoList",true);

		String descripcion = tipoPermisoList.getTipoPermiso().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoTipoPermisos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoTipoPermisos", tipoPermisoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoDiscapacidades() {
		DiscapacidadList discapacidadList = (DiscapacidadList) Component.getInstance("discapacidadList",true);

		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			this.crearArchivo("listadoDiscapacidades", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoDiscapacidades", discapacidadList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void listadoEmpleadoDeclaraBienes() {
	}

	@Override
	public void listadoFeriados() {
		FeriadoList feriadoList = (FeriadoList) Component.getInstance("feriadoList",true);

		String descripcion = feriadoList.getFeriado().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoFeriados", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoFeriados", feriadoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoGrupoContratados() {
		GrupoContratadoList grupoContratadoList = (GrupoContratadoList) Component.getInstance("grupoContratadoList",true);

		String descripcion = grupoContratadoList.getGrupoContratado().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoGrupoContratados", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoGrupoContratados", grupoContratadoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoHorasExtras() {
		HoraExtraList horaExtraList = (HoraExtraList) Component.getInstance("horaExtraList",true);

		String descripcion="";
		try{
		descripcion=""+horaExtraList.getHoraExtra().getDescripcion().ordinal();		
		}catch(Exception e){
			descripcion="";
		}
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoHorasExtras", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoHorasExtras", horaExtraList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void listadoHorarios() {
		HorarioList horarioList = (HorarioList) Component.getInstance("horarioList",true);

		String descripcion = horarioList.getHorario().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoHorarios", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoHorarios", horarioList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoPermisos() {
		PermisoList permisoList = (PermisoList) Component.getInstance("permisoList",true);

		String descripcion = permisoList.getPermiso().getFechaDesde() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoPermisos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoPermisos", permisoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoProgramacionVacaciones() {
		ProgramaVacacionList prograVacacionList = (ProgramaVacacionList) Component.getInstance("prograVacacionList",true);

		//String descripcion = prograVacacionList.getProgramaVacacion().getPeriodo() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			//parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoProgramaVacaciones", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoProgramaVacaciones", prograVacacionList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoProvincias() {
		ProvinciaList provinciaList = (ProvinciaList) Component.getInstance("provinciaList",true);

		String descripcion = provinciaList.getProvincia().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoProvincias", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoProvincias", provinciaList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoRelojes() {
		RelojList relojList = (RelojList) Component.getInstance("relojList",true);

		String descripcion = relojList.getReloj().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoRelojes", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoRelojes", relojList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoSolicitudVacaciones() {
		SolicitudVacacionList solicitudVacacionList = (SolicitudVacacionList) Component.getInstance("solicitudVacacionList",true);

		String descripcion = solicitudVacacionList.getSolicitudVacacion().getFechaDesde() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoSolicitudVacaciones", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoSolicitudVacaciones", solicitudVacacionList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoTipoCargos() {
		TipoCargoList tipoCargoList = (TipoCargoList) Component.getInstance("tipoCargoList",true);
		String descripcion = tipoCargoList.getTipoCargo().getDescripcion() + "%";				
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoTipoCargos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoTipoCargos", tipoCargoList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void listadoTipoParametros() {
		TipoParametroList tipoParametroList = (TipoParametroList) Component.getInstance("tipoParametroList",true);

		String descripcion = tipoParametroList.getTipoParametro().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoTipoParametros", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoTipoParametros", tipoParametroList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoTitulos() {
		TituloList tituloList = (TituloList) Component.getInstance("tituloList",true);

		String descripcion = tituloList.getTitulo().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoTitulos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"listadoTitulos", tituloList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// **************************************
	// REPORTES ASISTENCIA
	// **************************************
	
	@Override
	public void reporteAtrasos() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();
		String justificacion = "";

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			switch (Integer.valueOf(asistenciaList.getJustificacion())){
			
			case 0:
				justificacion = "'P'"; // no esta justificacions			
			break;
			
			case 1:
				justificacion = "'R'"; // solo justificados
				break;
			case 2:
				justificacion = "' '"; //  TODOS
				break;			
			}
			
			parametros.put("justificacion", justificacion);
					
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAtrasosTabuladoCargo";
				}else{
					nombre_reporte="reporteAtrasosCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAtrasosTabuladoDepartamento";
				}else{
					nombre_reporte="reporteAtrasosDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAtrasosTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteAtrasosDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAtrasosTabuladoEmpleado";
				}else{
					nombre_reporte="reporteAtrasosEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteAsistenciaDetalle() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("verDetalle",asistenciaList.isVerDetalle());
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAsistenciaDetalleTabuladoCargo";
				}else{
					nombre_reporte="reporteAsistenciaDetalleCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
				
				
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAsistenciaDetalleTabuladoDepartamento";
				}else{
					nombre_reporte="reporteAsistenciaDetalleDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAsistenciaDetalleTabuladoGrupo";
				}else{
					nombre_reporte="reporteAsistenciaDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteAsistenciaDetalleTabuladoEmpleado";
				}else{
					nombre_reporte="reporteAsistenciaDetalleEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			}		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteAsistenciaMultiple() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPCargo";	
				}else{
					nombre_reporte="reporteAsistenciaMultipleCargo";
				}				
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAsistenciaMultipleCargo", asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
				
				
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPDepartamento";	
				}else{
					nombre_reporte="reporteAsistenciaMultipleDepartamento";
				}			
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAsistenciaMultipleDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPDetalleGrupo";	
				}else{
					nombre_reporte="reporteAsistenciaMultipleGrupo";
				}	
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAsistenciaMultipleGrupo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPEmpleado";	
				}else{
					nombre_reporte="reporteAsistenciaMultipleEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAsistenciaMultipleEmpleado", asistenciaList.getExtensionArchivo());
			}		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void reporteEntradasSalidas() {		
			
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
				
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPCargo";										
				}else{
					if(asistenciaList.isTabulado()){
						nombre_reporte="reporteEntradasSalidasTabuladoCargo";
					}else{
						nombre_reporte="reporteEntradasSalidasCargo";
					}	
				}								
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPDepartamento";										
				}else{
					if(asistenciaList.isTabulado()){
						nombre_reporte="reporteEntradasSalidasTabuladoDepartamento";
					}else{
						nombre_reporte="reporteEntradasSalidasDepartamento";
					}
				}			
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPDetalleGrupo";									
				}else{
					if(asistenciaList.isTabulado()){
						nombre_reporte="reporteEntradasSalidasTabuladoDetalleGrupo";
					}else{
						nombre_reporte="reporteEntradasSalidasDetalleGrupo";
					}
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isSinAsistencia()){
					nombre_reporte="reporteEntradasSalidasSPEmpleado";					
				}else{
					if(asistenciaList.isTabulado()){
						nombre_reporte="reporteEntradasSalidasTabuladoEmpleado";
					}else{
						nombre_reporte="reporteEntradasSalidasEmpleado";
					}
				}			

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteFaltas() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteFaltasTabuladoCargo";
				}else{
					nombre_reporte="reporteFaltasCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteFaltasTabuladoDepartamento";
				}else{
					nombre_reporte="reporteFaltasDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteFaltasTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteFaltasDetalleGrupo";
				}				
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteFaltasTabuladoEmpleado";
				}else{
					nombre_reporte="reporteFaltasEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reporteHorasExtras() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		
		Map<String, Object> parametros = new HashMap<String, Object>();		
		
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		boolean valorado=asistenciaList.isValorado();
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("valorado",valorado);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasExtrasTabuladoCargo";
				}else{
					nombre_reporte="reporteHorasExtrasCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasExtrasTabuladoDepartamento";
				}else{
					nombre_reporte="reporteHorasExtrasDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasExtrasTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteHorasExtrasDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasExtrasTabuladoEmpleado";
				}else{
					nombre_reporte="reporteHorasExtrasEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteHorasTrabajadas() {
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		
		Map<String, Object> parametros = new HashMap<String, Object>();		
		
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasTrabajadasTabuladoCargo";
				}else{
					nombre_reporte="reporteHorasTrabajadasCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasTrabajadasTabuladoDepartamento";
				}else{
					nombre_reporte="reporteHorasTrabajadasDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasTrabajadasTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteHorasTrabajadasDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteHorasTrabajadasTabuladoEmpleado";
				}else{
					nombre_reporte="reporteHorasTrabajadasEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteSalidasAntes() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteSalidasAntesTabuladoCargo";
				}else{
					nombre_reporte="reporteSalidasAntesCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteSalidasAntesTabuladoDepartamento";
				}else{
					nombre_reporte="reporteSalidasAntesDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteSalidasAntesTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteSalidasAntesDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteSalidasAntesTabuladoEmpleado";
				}else{
					nombre_reporte="reporteSalidasAntesEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public void reporteTimbres() {
		TimbreList timbreList = (TimbreList) Component.getInstance("timbreList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = timbreList.getFechaDesde();
		Date fechaFinal = timbreList.getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = timbreList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (timbreList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = timbreList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(timbreList.isTabulado()){
					nombre_reporte="reporteTimbresTabuladoCargo";
				}else{
					nombre_reporte="reporteTimbresCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, timbreList.getExtensionArchivo());
			} else if(timbreList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = timbreList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(timbreList.isTabulado()){
					nombre_reporte="reporteTimbresTabuladoDepartamento";
				}else{
					nombre_reporte="reporteTimbresDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, timbreList.getExtensionArchivo());
				
			} else if(timbreList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = timbreList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(timbreList.isTabulado()){
					nombre_reporte="reporteTimbresTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteTimbresDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, timbreList.getExtensionArchivo());
				
			} else if(timbreList.getTipoReporte().equals("Empleado")) {
				Long emplId = timbreList.getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(timbreList.isTabulado()){
					nombre_reporte="reporteTimbresTabuladoEmpleado";
				}else{
					nombre_reporte="reporteTimbresEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, timbreList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void reporteTimbresIncompletos() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTimbresIncompletosTabuladoCargo";
				}else{
					nombre_reporte="reporteTimbresIncompletosCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTimbresIncompletosTabuladoDepartamento";
				}else{
					nombre_reporte="reporteTimbresIncompletosDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTimbresIncompletosTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteTimbresIncompletosDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTimbresIncompletosTabuladoEmpleado";
				}else{
					nombre_reporte="reporteTimbresIncompletosEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteTimbresMRL() {
		TimbreList timbreList = (TimbreList) Component.getInstance("timbreList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = timbreList.getFechaDesde();
		Date fechaFinal = timbreList.getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = timbreList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (timbreList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = timbreList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteTimbresMRLCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTimbresMRLCargo", "Txt");
				
			} else if(timbreList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = timbreList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteTimbresMRLDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTimbresMRLDepartamento", "Txt");
				
			} else if(timbreList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = timbreList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteTimbresMRLDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTimbresMRLDetalleGrupo","Txt");
				
			} else if(timbreList.getTipoReporte().equals("Empleado")) {
				Long emplId = timbreList.getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteTimbresMRLEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTimbresMRLEmpleado", "Txt");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	// **************************************
	// OTROS REPORTES
	// **************************************
	@Override
	public void reporteKardexVacaciones() {
		SolicitudVacacionList solicitudVacacionList = (SolicitudVacacionList) Component.getInstance("solicitudVacacionList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();
				
		Date fechaInicial = solicitudVacacionList.getFechaDesde();
		Date fechaFinal = solicitudVacacionList.getFechaHasta();
//		String estado = solicitudVacacionList.getEstado();
		String nombre_reporte="";

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = solicitudVacacionList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Long[] arregloActivoInactivo =  solicitudVacacionList.getActivoInactivo();
		String activoInactivo = Comunes.arrayToString(arregloActivoInactivo, Constantes.SEPARADOR_LISTAS);
		
		Long[] arregloHistLaboActivoInactivo =  solicitudVacacionList.getHistLaboActivoInactivo();
		String histLaboActivoInactivo = Comunes.arrayToString(arregloHistLaboActivoInactivo, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
//			parametros.put("estado",estado);
			parametros.put("verDetalle",solicitudVacacionList.isVerDetalleKardexVacacion());
			parametros.put("activoInactivo",activoInactivo);
			parametros.put("histLaboActivoInactivo",histLaboActivoInactivo);
			
			if (solicitudVacacionList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = solicitudVacacionList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteKardexVacacionesTabuladoCargo";
				}else{
					nombre_reporte="reporteKardexVacacionesCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteKardexVacacionesCargo", solicitudVacacionList.getExtensionArchivo());
			} else if(solicitudVacacionList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = solicitudVacacionList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);

				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteKardexVacacionesTabuladoDepartamento";
				}else{
					nombre_reporte="reporteKardexVacacionesDepartamento";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteKardexVacacionesDepartamento", solicitudVacacionList.getExtensionArchivo());
				
			} else if(solicitudVacacionList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = solicitudVacacionList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteKardexVacacionesTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteKardexVacacionesDetalleGrupo";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteKardexVacacionesDetalleGrupo", solicitudVacacionList.getExtensionArchivo());
				
			} else if(solicitudVacacionList.getTipoReporte().equals("Empleado")) {
				Long emplId = solicitudVacacionList.getSolicitudVacacion().getEmpleadoByEmplId().getEmplId();

				parametros.put("emplId",emplId);

				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteKardexVacacionesTabuladoEmpleado";
				}else{
					nombre_reporte="reporteKardexVacacionesEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteKardexVacacionesEmpleado", solicitudVacacionList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void reportePermisos() {
		PermisoList permisoList = (PermisoList) Component.getInstance("permisoList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = permisoList.getFechaDesde();
		Date fechaFinal = permisoList.getFechaHasta();
		
		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = permisoList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Long[] arregloTipoPermisos = permisoList.getTipoPermisos();
		String tipoPermisos = Comunes.arrayToString(arregloTipoPermisos, Constantes.SEPARADOR_LISTAS);
				
		Long[] arregloAutorizadoSiNo =  permisoList.getAutorizadoSiNo();
		String autorizadoSiNo = Comunes.arrayToString(arregloAutorizadoSiNo, Constantes.SEPARADOR_LISTAS);
		
		String[] listaLegalizado =  permisoList.getListaLegalizado();
		String legalizado = Comunes.arrayToStringCadena(listaLegalizado, Constantes.SEPARADOR_LISTAS);
		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("tipoPermisos",tipoPermisos);
			parametros.put("autorizadoSiNo",autorizadoSiNo);
			parametros.put("legalizado",legalizado);
			parametros.put("verDetalle",permisoList.isVerDetalle());
			
			if (permisoList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = permisoList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				String nombre_reporte="";
				
				if(permisoList.isTabulado()){
					nombre_reporte="reportePermisosTabuladoCargo";
				}else{
					nombre_reporte="reportePermisosCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, permisoList.getExtensionArchivo());
			} else if(permisoList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = permisoList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(permisoList.isTabulado()){
					nombre_reporte="reportePermisosTabuladoDepartamento";
				}else{
					nombre_reporte="reportePermisosDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, permisoList.getExtensionArchivo());
				
			} else if(permisoList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = permisoList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(permisoList.isTabulado()){
					nombre_reporte="reportePermisosTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reportePermisosDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, permisoList.getExtensionArchivo());
				
			} else if(permisoList.getTipoReporte().equals("Empleado")) {
				Long emplId = permisoList.getPermiso().getEmpleadoByEmplId().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(permisoList.isTabulado()){
					nombre_reporte="reportePermisosTabuladoEmpleado";
				}else{
					nombre_reporte="reportePermisosEmpleado";
				}
				

				for(Iterator<String> it = parametros.keySet().iterator(); it.hasNext();){
					String claves = it.next();
					System.out.print("---Clave: " + claves + "  "+ " Valor: " + parametros.get(claves));
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, permisoList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void reporteSolicitudVacaciones() {
		SolicitudVacacionList solicitudVacacionList = (SolicitudVacacionList) Component.getInstance("solicitudVacacionList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = solicitudVacacionList.getFechaDesde();
		Date fechaFinal = solicitudVacacionList.getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = solicitudVacacionList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Long[] arregloAutorizadoSiNo =  solicitudVacacionList.getAutorizadoSiNo();
		String autorizadoSiNo = Comunes.arrayToString(arregloAutorizadoSiNo, Constantes.SEPARADOR_LISTAS);
		 		
		try {
			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("autorizadoSiNo",autorizadoSiNo);
			parametros.put("verDetalle",solicitudVacacionList.isVerDetalle());
			
			if (solicitudVacacionList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = solicitudVacacionList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteSolicitudVacacionesTabuladoCargo";
				}else{
					nombre_reporte="reporteSolicitudVacacionesCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, solicitudVacacionList.getExtensionArchivo());
			} else if(solicitudVacacionList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = solicitudVacacionList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteSolicitudVacacionesTabuladoDepartamento";
				}else{
					nombre_reporte="reporteSolicitudVacacionesDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, solicitudVacacionList.getExtensionArchivo());
				
			} else if(solicitudVacacionList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = solicitudVacacionList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteSolicitudVacacionesTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteSolicitudVacacionesDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, solicitudVacacionList.getExtensionArchivo());
				
			} else if(solicitudVacacionList.getTipoReporte().equals("Empleado")) {
				Long emplId = solicitudVacacionList.getSolicitudVacacion().getEmpleadoByEmplId().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(solicitudVacacionList.isTabulado()){
					nombre_reporte="reporteSolicitudVacacionesTabuladoEmpleado";
				}else{
					nombre_reporte="reporteSolicitudVacacionesEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, solicitudVacacionList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// IMPRESION FORMATOS INDIVIDUALES
	
	@Override
	public void reporteSolicitudPermisoIndividual(){
		PermisoHome permisoHome = (PermisoHome) Component.getInstance("permisoHome",true);			
		Map<String, Object> parametros = new HashMap<String, Object>();	
		Long permId = permisoHome.getPermisoPermId();
		
		parametros.put("permId",permId);

		this.crearArchivo("formatoPermiso1", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"formatoPermiso1", "Pdf");			
	}
	
	@Override
	public void reporteSolicitudVacacionIndividual(){
		SolicitudVacacionHome solicitudVacacion = (SolicitudVacacionHome) Component.getInstance("solicitudVacacionHome",true);			
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		Long sovaId = solicitudVacacion.getInstance().getSova();
			
		parametros.put("sovaId",sovaId);	
		this.crearArchivo("formatoSolicitudVacacion1", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"formatoSolicitudVacacion1", "Pdf");			

	}
	
	//new method
	
//	@Override
//	public void reportePlanHorasExtras(){
//		
//		PlanExtrasHome planExtrasHome = (PlanExtrasHome) Component.getInstance("planExtrasHome",true);			
//		Map<String, Object> parametros = new HashMap<String, Object>();	
//		
//		try {
//				Long emplId = planExtrasHome.getInstance().getEmpleado().getEmplId();
//				
//				int m1=planExtrasHome.getInstance().getMes();
//				int m2=m1;				
//				int y1=planExtrasHome.getInstance().getAnio();
//				int y2=y1;		
//				
//				CiudadList ciudadList = (CiudadList) Component.getInstance("ciudadList",true);	
//				List <Ciudad>  ciudad=new ArrayList <Ciudad>();
//				ciudad=ciudadList.getResultList();	
//				
//				Vector arregloCiudades = new Vector();
//				
//				for(Ciudad e : ciudad){
//					arregloCiudades.addElement(e.getCiudId());					
//				}	
//				
//				Long arregloCiudades2[]=new Long[arregloCiudades.size()];
//				
//				for(int i=0;i<arregloCiudades.size();i++){
//					arregloCiudades2[i]=(Long)arregloCiudades.get(i);
//					//System.out.println("(y): " +arregloCiudades2[i]);
//				}			
//				
//				String ciudades = Comunes.arrayToString(arregloCiudades2, Constantes.SEPARADOR_LISTAS);
//				
//				parametros.put("emplId",emplId);	
//				parametros.put("ciudades",ciudades);
//				parametros.put("m1",m1);
//				parametros.put("m2",m2);
//				parametros.put("y1",y1);
//				parametros.put("y2",y2);
//				
//				String prs1=planExtrasHome.getInstance().getEmpleado().getNombre()+" "+planExtrasHome.getInstance().getEmpleado().getApellido();
//				String crg1=planExtrasHome.getInstance().getEmpleado().getCargo().getDescripcion();
//				
//				String prs2=planExtrasHome.getInstance().getEmpleado().getEmpleado().getNombre()+" "+planExtrasHome.getInstance().getEmpleado().getEmpleado().getApellido();
//				String crg2=planExtrasHome.getInstance().getEmpleado().getEmpleado().getCargo().getDescripcion();
//				
//				parametros.put("prs1",prs1);
//				parametros.put("crg1",crg1);
//				parametros.put("prs2",prs2);
//				parametros.put("crg2",crg2);
//
//				this.crearArchivo("reportePlanificacionHorasExtrasEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
//						"PlanificacionHorasExtras", "Pdf");			
//			} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
	
	
	
	@Override
	public void reportePuntualidad() {

		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reportePuntualidadCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePuntualidadCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reportePuntualidadDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePuntualidadDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reportePuntualidadDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePuntualidadDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reportePuntualidadEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePuntualidadDetalleGrupo", asistenciaList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void reporteEmpleadosActivos() {
			
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadosActivosCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadosActivosCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadosActivosDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadosActivosDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadosActivosDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadosActivosDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			} /*else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteAtrasosEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Atrasos", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteEmpleadosInactivos() {
			
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		/*Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();*/

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			//parametros.put("fechaInicial",fechaInicial);
			//parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadosInactivosCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadosInactivosCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadosInactivosDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadosInactivosDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadosInactivosDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadosInactivosDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			} /*else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteAtrasosEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Atrasos", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteEmpleadoPuntuales() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Integer rango1=asistenciaList.getRango_desde();
		Integer rango2=asistenciaList.getRango_hasta();	
		
		boolean ch1=asistenciaList.isCheck1();
		boolean ch2=asistenciaList.isCheck2();
		boolean ch3=asistenciaList.isCheck3();
		
		Integer desde1=0;
		Integer hasta1=0;
		Integer desde2=0;
		Integer hasta2=0;
		
		//String adicional="";	
		
		if(ch1&&ch2&&ch3){
			desde1=0;hasta1=999999999;
			desde2=0;hasta2=999999999;
			//adicional=" and PUNTUALES>=0 ";	
		}
		else{
			if(ch1&&ch2&&!ch3){
				desde1=0;hasta1=rango2-1;
				desde2=0;hasta2=rango2-1;
				//adicional=" and PUNTUALES<"+rango2+" ";
			}
			else{
				if(ch1&&!ch2&&ch3){
					desde1=0;hasta1=rango1;
					desde2=rango2;hasta2=999999999;
					//adicional=" and PUNTUALES<="+rango1+" and PUNTUALES >="+rango2+" ";
				}
				else{
					if(ch1&&!ch2&&!ch3){
						desde1=0;hasta1=rango1;
						desde2=0;hasta2=rango1;
						//adicional=" and PUNTUALES<="+rango1+" ";
					}
					else{
						if(!ch1&&ch2&&ch3){
							desde1=rango1+1;hasta1=999999999;
							desde2=rango1+1;hasta2=999999999;
							//adicional=" and PUNTUALES>"+rango1+" ";
						}
						else{
							if(!ch1&&ch2&&!ch3){
								desde1=rango1+1;hasta1=rango2-1;
								desde2=rango1+1;hasta2=rango2-1;
								//adicional=" and PUNTUALES>"+rango1+" and PUNTUALES <"+rango2+" ";
							}
							else{
								if(!ch1&&!ch2&&ch3){
									desde1=rango2;hasta1=999999999;
									desde2=rango2;hasta2=999999999;
									//adicional=" and PUNTUALES>="+rango2+" ";
								}
								else{
									desde1=-1;hasta1=-1;
									desde2=-1;hasta2=-1;
									//adicional= " and PUNTUALES<0 ";
								}
							}								
						}							
					}						
				}					
			}				 		
		}
			
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("rango1",rango1);
			parametros.put("rango2",rango2);
			
			parametros.put("desde1",desde1);
			parametros.put("hasta1",hasta1);
			parametros.put("desde2",desde2);
			parametros.put("hasta2",hasta2);
			
			parametros.put("ch1",ch1);
			parametros.put("ch2",ch2);
			parametros.put("ch3",ch3);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadoPuntualesCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoPuntualesCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadoPuntualesDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoPuntualesDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadoPuntualesDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoPuntualesDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			}/* else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reportePuntualidadEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Puntualidad", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void reporteEmpleadosAtrasos() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Integer rango1=asistenciaList.getRango_desde();
		Integer rango2=asistenciaList.getRango_hasta();	
		
		boolean ch1=asistenciaList.isCheck1();
		boolean ch2=asistenciaList.isCheck2();
		boolean ch3=asistenciaList.isCheck3();
		
		Integer desde1=0;
		Integer hasta1=0;
		Integer desde2=0;
		Integer hasta2=0;
		
		//String adicional="";	
		
		if(ch1&&ch2&&ch3){
			desde1=0;hasta1=999999999;
			desde2=0;hasta2=999999999;
			//adicional=" and PUNTUALES>=0 ";	
		}
		else{
			if(ch1&&ch2&&!ch3){
				desde1=0;hasta1=rango2-1;
				desde2=0;hasta2=rango2-1;
				//adicional=" and PUNTUALES<"+rango2+" ";
			}
			else{
				if(ch1&&!ch2&&ch3){
					desde1=0;hasta1=rango1;
					desde2=rango2;hasta2=999999999;
					//adicional=" and PUNTUALES<="+rango1+" and PUNTUALES >="+rango2+" ";
				}
				else{
					if(ch1&&!ch2&&!ch3){
						desde1=0;hasta1=rango1;
						desde2=0;hasta2=rango1;
						//adicional=" and PUNTUALES<="+rango1+" ";
					}
					else{
						if(!ch1&&ch2&&ch3){
							desde1=rango1+1;hasta1=999999999;
							desde2=rango1+1;hasta2=999999999;
							//adicional=" and PUNTUALES>"+rango1+" ";
						}
						else{
							if(!ch1&&ch2&&!ch3){
								desde1=rango1+1;hasta1=rango2-1;
								desde2=rango1+1;hasta2=rango2-1;
								//adicional=" and PUNTUALES>"+rango1+" and PUNTUALES <"+rango2+" ";
							}
							else{
								if(!ch1&&!ch2&&ch3){
									desde1=rango2;hasta1=999999999;
									desde2=rango2;hasta2=999999999;
									//adicional=" and PUNTUALES>="+rango2+" ";
								}
								else{
									desde1=-1;hasta1=-1;
									desde2=-1;hasta2=-1;
									//adicional= " and PUNTUALES<0 ";
								}
							}								
						}							
					}						
				}					
			}				 		
		}
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			parametros.put("rango1",rango1);
			parametros.put("rango2",rango2);
			
			parametros.put("desde1",desde1);
			parametros.put("hasta1",hasta1);
			parametros.put("desde2",desde2);
			parametros.put("hasta2",hasta2);
			
			parametros.put("ch1",ch1);
			parametros.put("ch2",ch2);
			parametros.put("ch3",ch3);
			
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadoAtrasosCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoAtrasosCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadoAtrasosDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoAtrasosDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadoAtrasosDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoAtrasosDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			}/* else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reportePuntualidadEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Puntualidad", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	

	@Override
	public void reporteEmpleadosSalidaAntes() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Integer rango1=asistenciaList.getRango_desde();
		Integer rango2=asistenciaList.getRango_hasta();	
		
		boolean ch1=asistenciaList.isCheck1();
		boolean ch2=asistenciaList.isCheck2();
		boolean ch3=asistenciaList.isCheck3();
		
		Integer desde1=0;
		Integer hasta1=0;
		Integer desde2=0;
		Integer hasta2=0;
		
		if(ch1&&ch2&&ch3){
			desde1=0;hasta1=999999999;
			desde2=0;hasta2=999999999;
			//adicional=" and PUNTUALES>=0 ";	
		}
		else{
			if(ch1&&ch2&&!ch3){
				desde1=0;hasta1=rango2-1;
				desde2=0;hasta2=rango2-1;
				//adicional=" and PUNTUALES<"+rango2+" ";
			}
			else{
				if(ch1&&!ch2&&ch3){
					desde1=0;hasta1=rango1;
					desde2=rango2;hasta2=999999999;
					//adicional=" and PUNTUALES<="+rango1+" and PUNTUALES >="+rango2+" ";
				}
				else{
					if(ch1&&!ch2&&!ch3){
						desde1=0;hasta1=rango1;
						desde2=0;hasta2=rango1;
						//adicional=" and PUNTUALES<="+rango1+" ";
					}
					else{
						if(!ch1&&ch2&&ch3){
							desde1=rango1+1;hasta1=999999999;
							desde2=rango1+1;hasta2=999999999;
							//adicional=" and PUNTUALES>"+rango1+" ";
						}
						else{
							if(!ch1&&ch2&&!ch3){
								desde1=rango1+1;hasta1=rango2-1;
								desde2=rango1+1;hasta2=rango2-1;
								//adicional=" and PUNTUALES>"+rango1+" and PUNTUALES <"+rango2+" ";
							}
							else{
								if(!ch1&&!ch2&&ch3){
									desde1=rango2;hasta1=999999999;
									desde2=rango2;hasta2=999999999;
									//adicional=" and PUNTUALES>="+rango2+" ";
								}
								else{
									desde1=-1;hasta1=-1;
									desde2=-1;hasta2=-1;
									//adicional= " and PUNTUALES<0 ";
								}
							}								
						}							
					}						
				}					
			}				 		
		}
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			parametros.put("rango1",rango1);
			parametros.put("rango2",rango2);
			
			parametros.put("desde1",desde1);
			parametros.put("hasta1",hasta1);
			parametros.put("desde2",desde2);
			parametros.put("hasta2",hasta2);
			
			parametros.put("ch1",ch1);
			parametros.put("ch2",ch2);
			parametros.put("ch3",ch3);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadoSalidaAntesCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoSalidaAntesCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadoSalidaAntesDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoSalidaAntesDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadoSalidaAntesDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoSalidaAntesDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			}/* else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reportePuntualidadEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Puntualidad", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void reporteEmpleadosFaltas() {

		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Integer rango1=asistenciaList.getRango_desde();
		Integer rango2=asistenciaList.getRango_hasta();	
		
		boolean ch1=asistenciaList.isCheck1();
		boolean ch2=asistenciaList.isCheck2();
		boolean ch3=asistenciaList.isCheck3();
		
		Integer desde1=0;
		Integer hasta1=0;
		Integer desde2=0;
		Integer hasta2=0;
		
		if(ch1&&ch2&&ch3){
			desde1=0;hasta1=999999999;
			desde2=0;hasta2=999999999;
			//adicional=" and PUNTUALES>=0 ";	
		}
		else{
			if(ch1&&ch2&&!ch3){
				desde1=0;hasta1=rango2-1;
				desde2=0;hasta2=rango2-1;
				//adicional=" and PUNTUALES<"+rango2+" ";
			}
			else{
				if(ch1&&!ch2&&ch3){
					desde1=0;hasta1=rango1;
					desde2=rango2;hasta2=999999999;
					//adicional=" and PUNTUALES<="+rango1+" and PUNTUALES >="+rango2+" ";
				}
				else{
					if(ch1&&!ch2&&!ch3){
						desde1=0;hasta1=rango1;
						desde2=0;hasta2=rango1;
						//adicional=" and PUNTUALES<="+rango1+" ";
					}
					else{
						if(!ch1&&ch2&&ch3){
							desde1=rango1+1;hasta1=999999999;
							desde2=rango1+1;hasta2=999999999;
							//adicional=" and PUNTUALES>"+rango1+" ";
						}
						else{
							if(!ch1&&ch2&&!ch3){
								desde1=rango1+1;hasta1=rango2-1;
								desde2=rango1+1;hasta2=rango2-1;
								//adicional=" and PUNTUALES>"+rango1+" and PUNTUALES <"+rango2+" ";
							}
							else{
								if(!ch1&&!ch2&&ch3){
									desde1=rango2;hasta1=999999999;
									desde2=rango2;hasta2=999999999;
									//adicional=" and PUNTUALES>="+rango2+" ";
								}
								else{
									desde1=-1;hasta1=-1;
									desde2=-1;hasta2=-1;
									//adicional= " and PUNTUALES<0 ";
								}
							}								
						}							
					}						
				}					
			}				 		
		}
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);	
			
			parametros.put("rango1",rango1);
			parametros.put("rango2",rango2);
			
			parametros.put("desde1",desde1);
			parametros.put("hasta1",hasta1);
			parametros.put("desde2",desde2);
			parametros.put("hasta2",hasta2);
			
			parametros.put("ch1",ch1);
			parametros.put("ch2",ch2);
			parametros.put("ch3",ch3);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadoFaltasCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoFaltasCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadoFaltasDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoFaltasDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadoFaltasDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoFaltasDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			}/* else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reportePuntualidadEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Puntualidad", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void reporteEmpleadosPermisos() {

		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Integer rango1=asistenciaList.getRango_desde();
		Integer rango2=asistenciaList.getRango_hasta();	
		
		boolean ch1=asistenciaList.isCheck1();
		boolean ch2=asistenciaList.isCheck2();
		boolean ch3=asistenciaList.isCheck3();
		
		Integer desde1=0;
		Integer hasta1=0;
		Integer desde2=0;
		Integer hasta2=0;
		
		//String adicional="";	
		
		if(ch1&&ch2&&ch3){
			desde1=0;hasta1=999999999;
			desde2=0;hasta2=999999999;
			//adicional=" and PUNTUALES>=0 ";	
		}
		else{
			if(ch1&&ch2&&!ch3){
				desde1=0;hasta1=rango2-1;
				desde2=0;hasta2=rango2-1;
				//adicional=" and PUNTUALES<"+rango2+" ";
			}
			else{
				if(ch1&&!ch2&&ch3){
					desde1=0;hasta1=rango1;
					desde2=rango2;hasta2=999999999;
					//adicional=" and PUNTUALES<="+rango1+" and PUNTUALES >="+rango2+" ";
				}
				else{
					if(ch1&&!ch2&&!ch3){
						desde1=0;hasta1=rango1;
						desde2=0;hasta2=rango1;
						//adicional=" and PUNTUALES<="+rango1+" ";
					}
					else{
						if(!ch1&&ch2&&ch3){
							desde1=rango1+1;hasta1=999999999;
							desde2=rango1+1;hasta2=999999999;
							//adicional=" and PUNTUALES>"+rango1+" ";
						}
						else{
							if(!ch1&&ch2&&!ch3){
								desde1=rango1+1;hasta1=rango2-1;
								desde2=rango1+1;hasta2=rango2-1;
								//adicional=" and PUNTUALES>"+rango1+" and PUNTUALES <"+rango2+" ";
							}
							else{
								if(!ch1&&!ch2&&ch3){
									desde1=rango2;hasta1=999999999;
									desde2=rango2;hasta2=999999999;
									//adicional=" and PUNTUALES>="+rango2+" ";
								}
								else{
									desde1=-1;hasta1=-1;
									desde2=-1;hasta2=-1;
									//adicional= " and PUNTUALES<0 ";
								}
							}								
						}							
					}						
				}					
			}				 		
		}
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("rango1",rango1);
			parametros.put("rango2",rango2);
			
			parametros.put("desde1",desde1);
			parametros.put("hasta1",hasta1);
			parametros.put("desde2",desde2);
			parametros.put("hasta2",hasta2);
			
			parametros.put("ch1",ch1);
			parametros.put("ch2",ch2);
			parametros.put("ch3",ch3);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadoPermisosCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoPermisosCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadoPermisosDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoPermisosDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadoPermisosDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoPermisosDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			}/* else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reportePuntualidadEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Puntualidad", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}	
	
	@Override
	public void reporteEmpleadosVacaciones() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Integer rango1=asistenciaList.getRango_desde();
		Integer rango2=asistenciaList.getRango_hasta();	
		
		boolean ch1=asistenciaList.isCheck1();
		boolean ch2=asistenciaList.isCheck2();
		boolean ch3=asistenciaList.isCheck3();
		
		Integer desde1=0;
		Integer hasta1=0;
		Integer desde2=0;
		Integer hasta2=0;
		
		//String adicional="";	
		
		if(ch1&&ch2&&ch3){
			desde1=0;hasta1=999999999;
			desde2=0;hasta2=999999999;
			//adicional=" and PUNTUALES>=0 ";	
		}
		else{
			if(ch1&&ch2&&!ch3){
				desde1=0;hasta1=rango2-1;
				desde2=0;hasta2=rango2-1;
				//adicional=" and PUNTUALES<"+rango2+" ";
			}
			else{
				if(ch1&&!ch2&&ch3){
					desde1=0;hasta1=rango1;
					desde2=rango2;hasta2=999999999;
					//adicional=" and PUNTUALES<="+rango1+" and PUNTUALES >="+rango2+" ";
				}
				else{
					if(ch1&&!ch2&&!ch3){
						desde1=0;hasta1=rango1;
						desde2=0;hasta2=rango1;
						//adicional=" and PUNTUALES<="+rango1+" ";
					}
					else{
						if(!ch1&&ch2&&ch3){
							desde1=rango1+1;hasta1=999999999;
							desde2=rango1+1;hasta2=999999999;
							//adicional=" and PUNTUALES>"+rango1+" ";
						}
						else{
							if(!ch1&&ch2&&!ch3){
								desde1=rango1+1;hasta1=rango2-1;
								desde2=rango1+1;hasta2=rango2-1;
								//adicional=" and PUNTUALES>"+rango1+" and PUNTUALES <"+rango2+" ";
							}
							else{
								if(!ch1&&!ch2&&ch3){
									desde1=rango2;hasta1=999999999;
									desde2=rango2;hasta2=999999999;
									//adicional=" and PUNTUALES>="+rango2+" ";
								}
								else{
									desde1=-1;hasta1=-1;
									desde2=-1;hasta2=-1;
									//adicional= " and PUNTUALES<0 ";
								}
							}								
						}							
					}						
				}					
			}				 		
		}
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("rango1",rango1);
			parametros.put("rango2",rango2);
			
			parametros.put("desde1",desde1);
			parametros.put("hasta1",hasta1);
			parametros.put("desde2",desde2);
			parametros.put("hasta2",hasta2);
			
			parametros.put("ch1",ch1);
			parametros.put("ch2",ch2);
			parametros.put("ch3",ch3);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteEmpleadoVacacionesCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoVacacionesCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteEmpleadoVacacionesDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoVacacionesDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteEmpleadoVacacionesDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteEmpleadoVacacionesDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			}/* else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reportePuntualidadEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"Puntualidad", asistenciaList.getExtensionArchivo());
			} */
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void reporteTiempoAlmuerzo() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTiempoAlmuerzoTabuladoCargo";
				}else{
					nombre_reporte="reporteTiempoAlmuerzoCargo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTiempoAlmuerzoTabuladoDepartamento";
				}else{
					nombre_reporte="reporteTiempoAlmuerzoDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTiempoAlmuerzoTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteTiempoAlmuerzoDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(asistenciaList.isTabulado()){
					nombre_reporte="reporteTiempoAlmuerzoTabuladoEmpleado";
				}else{
					nombre_reporte="reporteTiempoAlmuerzoEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, asistenciaList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteAtrasosEstadisticos() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteAtrasosCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAtrasosCargoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteAtrasosDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAtrasosDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteAtrasosDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAtrasosDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteFaltasEstadistico() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);				
				
				this.crearArchivo("reporteFaltasCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteFaltasCargoEstadisticos", asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteFaltasDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteFaltasDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteFaltasDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteFaltasDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} 
			} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reportePuntualidadEstadistico() {

		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reportePuntualidadCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePuntualidadCargoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reportePuntualidadDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePuntualidadDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reportePuntualidadDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePuntualidadDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public void reporteSalidasAntesEstadistico() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteSalidasAntesCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSalidasAntesCargoEstadisticos", asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteSalidasAntesDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSalidasAntesDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteSalidasAntesDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSalidasAntesDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public void reporteTiempoAlmuerzoEstadistico() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteTiempoAlmuerzoCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoAlmuerzoCargoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteTiempoAlmuerzoDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoAlmuerzoDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteTiempoAlmuerzoDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoAlmuerzoDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reportePermisosEstadistico() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {
			
			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				
				String cargos = null;
				
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
					
				} else {
					
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);				
				this.crearArchivo("reportePermisosCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,						
						"reportePermisosCargoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				
				String departamentos = null;				
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();						
				if (arregloDepartamentos.length != 0) {
					
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
					
				} else {
					
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reportePermisosDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePermisosDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				
				String detalleGrupos = null;
				
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					
					// crear lista cargos en Comunes
				}
				
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reportePermisosDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reportePermisosDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());
							
			} 			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void reporteSolicitudVacacionesEstadistico() {
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteSolicitudVacacionesCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSolicitudVacacionesCargoEstadisticos", asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteSolicitudVacacionesDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSolicitudVacacionesDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteSolicitudVacacionesDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSolicitudVacacionesDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteHorasExtrasEstadisticos() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		
		Map<String, Object> parametros = new HashMap<String, Object>();		
		
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteHorasExtrasCargoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteHorasExtrasCargoEstadisticos", asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteHorasExtrasDepartamentoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteHorasExtrasDepartamentoEstadisticos", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteHorasExtrasDetalleGrupoEstadisticos", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteHorasExtrasDetalleGrupoEstadisticos", asistenciaList.getExtensionArchivo());				
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void reporteMultiplePlanExtras() {		

		
		PlanExtraList planExtrasList = (PlanExtraList) Component.getInstance("planExtrasList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = planExtrasList.getFechaDesde();
		Date fechaFinal = planExtrasList.getFechaHasta();
		
		Calendar cal=Calendar.getInstance();	
		
		int m1=(fechaInicial.getMonth()+1);
		int m2=(fechaFinal.getMonth()+1);
		cal.setTime(fechaInicial);
		int y1=cal.get(Calendar.YEAR);
		cal.setTime(fechaFinal);
		int y2=cal.get(Calendar.YEAR);		

		Long[] arregloCiudades = planExtrasList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);		
		 		
		try {

			parametros.put("ciudades",ciudades);			
			parametros.put("m1",m1);
			parametros.put("m2",m2);
			parametros.put("y1",y1);
			parametros.put("y2",y2);	
			
			/*String prs2=planExtrasList.getPlanExtras().getEmpleado().getEmpleado().getNombre()+" "+planExtrasList.getPlanExtras().getEmpleado().getEmpleado().getApellido();
			String crg2=planExtrasList.getPlanExtras().getEmpleado().getEmpleado().getCargo().getDescripcion();*/
			
			/*String prs2="-";
			String crg2="Director de Recursos Humanos";*/
			
			/*parametros.put("prs1",prs1);
			parametros.put("crg1",crg1);*/
			/*parametros.put("prs2",prs2);
			parametros.put("crg2",crg2);*/
			
			if (planExtrasList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = planExtrasList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reportePlanificacionHorasExtrasCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"PlanificacionHorasExtras", planExtrasList.getExtensionArchivo());
				
			} else if(planExtrasList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = planExtrasList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reportePlanificacionHorasExtrasDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"PlanificacionHorasExtras", planExtrasList.getExtensionArchivo());
				
			} else if(planExtrasList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = planExtrasList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reportePlanificacionHorasExtrasDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"PlanificacionHorasExtras", planExtrasList.getExtensionArchivo());
				
			} else if(planExtrasList.getTipoReporte().equals("Empleado")) {
				Long emplId = planExtrasList.getPlanExtras().getEmpleado().getEmplId();
				//System.out.println(".........................................................emplId: "+emplId);
				parametros.put("emplId",emplId);
				String prs1=planExtrasList.getPlanExtras().getEmpleado().getNombre()+" "+planExtrasList.getPlanExtras().getEmpleado().getApellido();
				String crg1=planExtrasList.getPlanExtras().getEmpleado().getCargo().getDescripcion();
				String prs2=planExtrasList.getPlanExtras().getEmpleado().getEmpleado().getNombre()+" "+planExtrasList.getPlanExtras().getEmpleado().getEmpleado().getApellido();
				String crg2=planExtrasList.getPlanExtras().getEmpleado().getEmpleado().getCargo().getDescripcion();
				parametros.put("prs1",prs1);
				parametros.put("crg1",crg1);
				parametros.put("prs2",prs2);
				parametros.put("crg2",crg2);
				this.crearArchivo("reportePlanificacionHorasExtrasEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"PlanificacionHorasExtras", planExtrasList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reporteAccionPermisoIndividual() {
		PermisoHome permisoHome = (PermisoHome) Component.getInstance("permisoHome",true);			
		Map<String, Object> parametros = new HashMap<String, Object>();	

		try {
			Long emplId = permisoHome.getInstance().getPermId();
			
			Date fechaDesde=permisoHome.getInstance().getFechaDesde();
			Date fechaHasta=permisoHome.getInstance().getFechaHasta();	
			
			long diference=fechaHasta.getTime()-fechaDesde.getTime();			
		    Double DiasImputables =(double) ((int)((((diference / 1000) / 60) / 60) / 24) +1);
			
			CiudadList ciudadList = (CiudadList) Component.getInstance("ciudadList",true);	
			List<Ciudad>  ciudad = new ArrayList<Ciudad>();
			ciudad=ciudadList.getResultList();	
			
			Vector<Long> arregloCiudades = new Vector<Long>();
			
			for(Ciudad e : ciudad){
				arregloCiudades.addElement(e.getCiudId());					
			}	
			
			Long arregloCiudades2[]=new Long[arregloCiudades.size()];
			
			for(int i=0;i<arregloCiudades.size();i++){
				arregloCiudades2[i]=(Long)arregloCiudades.get(i);
			}			
			
			String ciudades = Comunes.arrayToString(arregloCiudades2, Constantes.SEPARADOR_LISTAS);
			
			parametros.put("emplId",emplId);	
			parametros.put("ciudades",ciudades);
						
			String grupo_contratado=permisoHome.getInstance().getEmpleadoByEmplId().getDetalleGrupoContratado().getDescripcion();
			String emp=permisoHome.getInstance().getEmpleadoByEmplId().getApellido()+" "+permisoHome.getInstance().getEmpleadoByEmplId().getNombre();
			String explicacion="";
			int dias=permisoHome.getInstance().getDias();  
			
			EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
			empleadoPeriodoVacacion = gestionPermisoVacacion.seleccionarEmpleadoPeriodoVacacion(permisoHome.getInstance().getEmpleadoByEmplId());
			
			Double DiasPendientes=(gestionPermisoVacacion.buscarSaldoVacaciones_solicitudes_aprobadas(empleadoPeriodoVacacion));
			
			Double DiasPermisos=(gestionPermisoVacacion.buscarSaldoPermisos(empleadoPeriodoVacacion));
			
			String tipo_perm=permisoHome.getInstance().getTipoPermiso().getDescripcion();
			String departamento=permisoHome.getInstance().getEmpleadoByEmplId().getDepartamento().getDescripcion();
			
			if(grupo_contratado.equals("LEY DE EMPRESAS PUBLICAS")){
				explicacion="De conformidad con el Art. 34 de la LOSEP, y su Art. 32 se conceden "+dias+" días de permisos imputables a "+ tipo_perm+", a partir del "
						+fechaDesde+" al "+fechaHasta+", al Sr/ta. "+emp+", Servidor Público de "+departamento+", de esta Cartera del Estado.";
			}else{
				if(grupo_contratado.equals("CODIGO DE TRABAJO")){
					explicacion="De conformidad con el Art.69 del CÓDIGO DE TRABAJO, se conceden "+dias+" días de permisos imputables a "+ tipo_perm+", a partir del "
							+fechaDesde+" al "+fechaHasta+", al Sr/ta. "+emp+", Servidor Público de "+departamento+", de esta Cartera del Estado.";
				}else{
					explicacion="Se conceden "+dias+" días de permisos imputables a "+ tipo_perm+", a partir del "
							+fechaDesde+" al "+fechaHasta+", al Sr/ta. "+emp+", Servidor Público de "+departamento+".";
				}
			}

			Calendar calendar=Calendar.getInstance();
			calendar.setTime(empleadoPeriodoVacacion.getFechaDesde());
						
			parametros.put("explicacion",explicacion);
			parametros.put("DiasPendientes",DiasPendientes +" días correspondientes al período "+calendar.get(Calendar.YEAR));
			parametros.put("DiasImputables",DiasImputables+" días imputables de permisos del período "+calendar.get(Calendar.YEAR));
			parametros.put("DiasPermisos",DiasPermisos +" días de permisos ocacionales con cargo a vacación");
			
			this.crearArchivo("reporteAccionPermisosEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Accion del personal", "Pdf");			
		} catch (Exception e) {
		e.printStackTrace();
		}

	}

	@Override
	public void reporteAccionVacacionIndividual() {
		SolicitudVacacionHome solicitudVacacion = (SolicitudVacacionHome) Component.getInstance("solicitudVacacionHome",true);			
		Map<String, Object> parametros = new HashMap<String, Object>();	

		try {
			
			Long emplId = solicitudVacacion.getInstance().getSova();
			Date fechaDesde=solicitudVacacion.getInstance().getFechaDesde();
			Date fechaHasta=solicitudVacacion.getInstance().getFechaHasta();	
			
			long diference=fechaHasta.getTime()-fechaDesde.getTime();
			
		    Double DiasImputables =(double) ((int)((((diference / 1000) / 60) / 60) / 24) +1);
			
			CiudadList ciudadList = (CiudadList) Component.getInstance("ciudadList",true);	
			List <Ciudad>  ciudad=new ArrayList <Ciudad>();
			ciudad=ciudadList.getResultList();	
			
			Vector<Long> arregloCiudades = new Vector<Long>();
			
			for(Ciudad e : ciudad){
				arregloCiudades.addElement(e.getCiudId());					
			}	
			
			Long arregloCiudades2[]=new Long[arregloCiudades.size()];
			
			for(int i=0;i<arregloCiudades.size();i++){
				arregloCiudades2[i]=(Long)arregloCiudades.get(i);
			}			
			
			String ciudades = Comunes.arrayToString(arregloCiudades2, Constantes.SEPARADOR_LISTAS);
			
			parametros.put("emplId",emplId);	
			parametros.put("ciudades",ciudades);
						
			String grupo_contratado=solicitudVacacion.getInstance().getEmpleadoByEmplId().getDetalleGrupoContratado().getDescripcion();
			
			String emp=solicitudVacacion.getInstance().getEmpleadoByEmplId().getApellido()+" "+solicitudVacacion.getInstance().getEmpleadoByEmplId().getNombre();
			
			String explicacion="";
			
			final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día 
			long dias = (Math.abs((fechaDesde.getTime() - fechaHasta.getTime())/MILLSECS_PER_DAY))+1;
					
			EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
			empleadoPeriodoVacacion = gestionPermisoVacacion.seleccionarEmpleadoPeriodoVacacion(solicitudVacacion.getInstance().getEmpleadoByEmplId());
			
			Double DiasPendientes=(gestionPermisoVacacion.buscarSaldoVacaciones_solicitudes_aprobadas(empleadoPeriodoVacacion));
					
			Double DiasPermisos=(gestionPermisoVacacion.buscarSaldoPermisos(empleadoPeriodoVacacion));
			
			String tipo_perm="VACACIONES";
			String departamento=solicitudVacacion.getInstance().getEmpleadoByEmplId().getDepartamento().getDescripcion();
			
			if(grupo_contratado.equals("LEY DE EMPRESAS PUBLICAS")){
				explicacion="De conformidad con el Art. 34 de la LOSEP, y su Art. 32 se conceden "+dias+" días de permisos imputables a "+ tipo_perm+", a partir del "
						+fechaDesde+" al "+fechaHasta+", para el/la Sr/ta. "+emp+", Servidor Público de "+departamento+", de esta Cartera del Estado.";
			}else{
				if(grupo_contratado.equals("CODIGO DE TRABAJO")){
					explicacion="De conformidad con el Art.69 del CÓDIGO DE TRABAJO, se conceden "+dias+" días de permisos imputables a "+ tipo_perm+", a partir del "
							+fechaDesde+" al "+fechaHasta+", para el/la Sr/ta. "+emp+", Servidor Público de "+departamento+", de esta Cartera del Estado.";
				}else{
					explicacion="Se conceden "+dias+" días de permisos imputables a "+ tipo_perm+", a partir del "
							+fechaDesde+" al "+fechaHasta+", para el/la Sr/ta. "+emp+", Servidor de "+departamento+".";
				}
			}	
			
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(empleadoPeriodoVacacion.getFechaDesde());
			
			parametros.put("explicacion",explicacion);
			parametros.put("DiasPendientes",DiasPendientes +" días correspondientes al período "+calendar.get(Calendar.YEAR));
			parametros.put("DiasImputables",DiasImputables+" días imputables de vacaciones del período "+calendar.get(Calendar.YEAR));
			parametros.put("DiasPermisos",DiasPermisos +" días de permisos ocacionales con cargo a vacación");
			
			this.crearArchivo("reporteAccionVacacionEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Accion del personal", "Pdf");			
		} catch (Exception e) {
		e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void reporteAusentismo() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();
		
		Integer rango1=asistenciaList.getRango_desde();
		Integer rango2=asistenciaList.getRango_hasta();		
	
		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("rango1",rango1);
			parametros.put("rango2",rango2);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteAusentismoCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAusentismoCargo", asistenciaList.getExtensionArchivo());
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteAusentismoDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAusentismoDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteAusentismoDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAusentismoDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteAusentismoEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteAusentismoEmpleado", asistenciaList.getExtensionArchivo());
			}
			} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void reporteTiempoAdicional() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();	

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("verDetalle",asistenciaList.isVerDetalle());		
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteTiempoAdicionalCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoAdicionalCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteTiempoAdicionalDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoAdicionalDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteTiempoAdicionalDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoAdicionalDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteTiempoAdicionalEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoAdicionalEmpleado", asistenciaList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void reporteTiempoLaborado() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();	

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("verDetalle",asistenciaList.isVerDetalle());
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteTiempoLaboradoCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoLaboradoCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteTiempoLaboradoDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoLaboradoDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteTiempoLaboradoDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoLaboradoDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteTiempoLaboradoEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteTiempoLaboradoEmpleado", asistenciaList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		
	@Override
	public void reporteSaldoVacacion() {
		
		SolicitudVacacionList solicitudVacacionList = (SolicitudVacacionList) Component.getInstance("solicitudVacacionList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = solicitudVacacionList.getFechaDesde();
		Date fechaFinal = solicitudVacacionList.getFechaHasta();
		String estado = solicitudVacacionList.getEstado();

		Long[] arregloCiudades = solicitudVacacionList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			parametros.put("estado",estado);
			
			if (solicitudVacacionList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = solicitudVacacionList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteSaldoVacacionCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSaldoVacacionCargo", solicitudVacacionList.getExtensionArchivo());
				
			} else if(solicitudVacacionList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = solicitudVacacionList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteSaldoVacacionDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSaldoVacacionDepartamento", solicitudVacacionList.getExtensionArchivo());
				
			} else if(solicitudVacacionList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = solicitudVacacionList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteSaldoVacacionDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSaldoVacacionDetalleGrupo", solicitudVacacionList.getExtensionArchivo());
				
			} else if(solicitudVacacionList.getTipoReporte().equals("Empleado")) {
				Long emplId = solicitudVacacionList.getSolicitudVacacion().getEmpleadoByEmplId().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteSaldoVacacionEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteSaldoVacacionEmpleado", solicitudVacacionList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reporteHorasExtraValorado() {
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = asistenciaList.getAsistencia().getFechaDesde();
		Date fechaFinal = asistenciaList.getAsistencia().getFechaHasta();	

		Long[] arregloCiudades = asistenciaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (asistenciaList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = asistenciaList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteHorasExtraValoradoCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteHorasExtraValoradoCargo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = asistenciaList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteHorasExtraValoradoDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteHorasExtraValoradoDepartamento", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = asistenciaList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteHorasExtraValoradoDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteHorasExtraValoradoDetalleGrupo", asistenciaList.getExtensionArchivo());
				
			} else if(asistenciaList.getTipoReporte().equals("Empleado")) {
				Long emplId = asistenciaList.getAsistencia().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteHorasExtraValoradoEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteHorasExtraValoradoEmpleado", asistenciaList.getExtensionArchivo());
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void reporteProgramaVacacion(){
		ProgramaVacacionList programaVacacionList = (ProgramaVacacionList) Component.getInstance("programaVacacionList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = programaVacacionList.getFechaDesde();
		
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(fechaInicial);		
		int periodo=calendar.get(Calendar.YEAR);	

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = programaVacacionList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);		

		Long[] arregloAutorizadoSiNo =  programaVacacionList.getAutorizadoSiNo();
		String autorizadoSiNo = Comunes.arrayToString(arregloAutorizadoSiNo, Constantes.SEPARADOR_LISTAS);
		
		String planificacion = null;
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("periodo",periodo);
			parametros.put("autorizadoSiNo",autorizadoSiNo);
			
			// nuevo//
			if(programaVacacionList.isSinPlanificacion()){
				planificacion = " null "; 
				parametros.put("planificacion", planificacion);
			}else{
				planificacion = " not null"; 
				parametros.put("planificacion", planificacion);		
			}
			
			///////////////////////////////////////
			
			if (programaVacacionList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = programaVacacionList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				String nombre_reporte="";
				
				if(programaVacacionList.isTabulado()){
					nombre_reporte="reporteProgramaVacacionesTabuladoCargo";
				}else{
					nombre_reporte="reporteProgramaVacacionesCargo";
				}
						
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, programaVacacionList.getExtensionArchivo());
			} else if(programaVacacionList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = programaVacacionList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				String nombre_reporte="";
				
				if(programaVacacionList.isTabulado()){
					nombre_reporte="reporteProgramaVacacionesTabuladoDepartamento";
				}else{
					nombre_reporte="reporteProgramaVacacionesDepartamento";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, programaVacacionList.getExtensionArchivo());
				
			} else if(programaVacacionList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = programaVacacionList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				String nombre_reporte="";
				
				if(programaVacacionList.isTabulado()){
					nombre_reporte="reporteProgramaVacacionesTabuladoDetalleGrupo";
				}else{
					nombre_reporte="reporteProgramaVacacionesDetalleGrupo";
				}
				
				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, programaVacacionList.getExtensionArchivo());
				
			} else if(programaVacacionList.getTipoReporte().equals("Empleado")) {
				Long emplId = programaVacacionList.getProgramaVacacion().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);
				
				String nombre_reporte="";
				
				if(programaVacacionList.isTabulado()){
					nombre_reporte="reporteProgramaVacacionesTabuladoEmpleado";
				}else{
					nombre_reporte="reporteProgramaVacacionesEmpleado";
				}

				this.crearArchivo(nombre_reporte, parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						nombre_reporte, programaVacacionList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void reportePlanHorasExtras() {
		
		PlanExtrasHome planExtrasHome = (PlanExtrasHome) Component.getInstance("planExtrasHome",true);			
		Map<String, Object> parametros = new HashMap<String, Object>();	
		
		try {
				Long emplId = planExtrasHome.getInstance().getEmpleado().getEmplId();
				
				int m1=planExtrasHome.getInstance().getMes();
				int m2=m1;				
				int y1=planExtrasHome.getInstance().getAnio();
				int y2=y1;		
				
//				CiudadList ciudadList = (CiudadList) Component.getInstance("ciudadList",true);
				
//				List<Ciudad> ciudad=new ArrayList<Ciudad>();
//				ciudad=ciudadList.getResultList();	
//				
//				Vector arregloCiudades = new Vector();
//				
//				for(Ciudad e : ciudad){
//					arregloCiudades.addElement(e.getCiudId());					
//				}	
//				
//				Long arregloCiudades2[]=new Long[arregloCiudades.size()];
//				
//				for(int i=0;i<arregloCiudades.size();i++){
//					arregloCiudades2[i]=(Long)arregloCiudades.get(i);
//					//System.out.println("(y): " +arregloCiudades2[i]);
//				}			

				AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList",true);				
				Long[] arregloCiudades = asistenciaList.getCiudades();
				String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
				
				parametros.put("emplId",emplId);	
				parametros.put("ciudades",ciudades);
				parametros.put("m1",m1);
				parametros.put("m2",m2);
				parametros.put("y1",y1);
				parametros.put("y2",y2);
				
				String prs1=planExtrasHome.getInstance().getEmpleado().getNombre()+" "+planExtrasHome.getInstance().getEmpleado().getApellido();
				String crg1=planExtrasHome.getInstance().getEmpleado().getCargo().getDescripcion();
				
				String prs2=planExtrasHome.getInstance().getEmpleado().getEmpleado().getNombre()+" "+planExtrasHome.getInstance().getEmpleado().getEmpleado().getApellido();
				String crg2=planExtrasHome.getInstance().getEmpleado().getEmpleado().getCargo().getDescripcion();
				
				parametros.put("prs1",prs1);
				parametros.put("crg1",crg1);
				parametros.put("prs2",prs2);
				parametros.put("crg2",crg2);
				
				this.crearArchivo("reportePlanificacionHorasExtrasEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"PlanificacionHorasExtras", "Pdf");			
			} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void reporteProgramaVacaciones() {
		ProgramaVacacionList programaVacacionList = (ProgramaVacacionList) Component.getInstance("programaVacacionList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = programaVacacionList.getFechaDesde();
		Date fechaFinal = programaVacacionList.getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = programaVacacionList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {

			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			if (programaVacacionList.getTipoReporte().equals("Cargo")){
				String cargos = null;
				Long[] arregloCargos = programaVacacionList.getCargos();
				
				if (arregloCargos.length != 0) {
					cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("cargos",cargos);
				
				this.crearArchivo("reporteProgramaVacacionesCargo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteProgramaVacacionesCargo", programaVacacionList.getExtensionArchivo());
			} else if(programaVacacionList.getTipoReporte().equals("Departamento")){
				String departamentos = null;
				Long[] arregloDepartamentos = programaVacacionList.getDepartamentos();
								
				if (arregloDepartamentos.length != 0) {
					departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("departamentos",departamentos);
				
				this.crearArchivo("reporteProgramaVacacionesDepartamento", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteProgramaVacacionesDepartamento", programaVacacionList.getExtensionArchivo());
				
			} else if(programaVacacionList.getTipoReporte().equals("Grupo")){
				String detalleGrupos = null;
				Long[] arregloDetalleGrupo = programaVacacionList.getDetalleGrupoContratados();
				
				if (arregloDetalleGrupo.length != 0) {
					detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				} else {
					// crear lista cargos en Comunes
				}
				parametros.put("detalleGrupos",detalleGrupos);
				
				this.crearArchivo("reporteProgramaVacacionesDetalleGrupo", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteProgramaVacacionesDetalleGrupo", programaVacacionList.getExtensionArchivo());
				
			} else if(programaVacacionList.getTipoReporte().equals("Empleado")) {
				Long emplId = programaVacacionList.getProgramaVacacion().getEmpleado().getEmplId();

				parametros.put("emplId",emplId);

				this.crearArchivo("reporteProgramaVacacionesEmpleado", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
						"reporteProgramaVacacionesEmpleado", programaVacacionList.getExtensionArchivo());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}	
	
	@Override
	public void reporteEmpleados() {
		
		EmpleadoList empleadoList = (EmpleadoList) Component.getInstance("empleadoList",true);
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		Long[] arregloCiudades = empleadoList.getCiudad();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		
		Long[] arregloControlaVacacion =  empleadoList.getControlVacacion();
		String controlaVacacion = Comunes.arrayToString(arregloControlaVacacion, Constantes.SEPARADOR_LISTAS);
		
		Long[] arregloActivoInactivo =  empleadoList.getActivoInactivo();
		String activoInactivo = Comunes.arrayToString(arregloActivoInactivo, Constantes.SEPARADOR_LISTAS);
		
		String select = "";
		String where = "";
		
		select = "select empleado.empl_id as empleado_EMPL_ID, empleado.cedula as empleado_CEDULA, empleado.estado as empleado_estado, "
				+ "empleado.codigo_empleado as empleado_CODIGO_EMPLEADO, empleado.apellido as empleado_APELLIDO, empleado.nombre as empleado_NOMBRE, "
				+ "empleado.controla_vacacion as empleado_controla_vacacion, "
				+ "empleado.fecha_ingreso as empleado_fecha_ingreso, empleado.horas_trabaja as empleado_HORAS_TRABAJA, ciudad.descripcion as ciudad_DESCRIPCION, "
				+ "cargo.descripcion as cargo_DESCRIPCION, departamento.descripcion as departamento_DESCRIPCION, detalle_grupo_contratado.descripcion as detalle_grupo_descripcion, "
				+ "genero.descripcion as genero_descripcion, estado_civil.descripcion as estado_civil_descripcion "
				+ "from empleado empleado inner join departamento departamento on empleado.depa_id = departamento.depa_id "
				+ "inner join cargo cargo on empleado.carg_id = cargo.carg_id "
				+ "inner join detalle_grupo_contratado detalle_grupo_contratado on detalle_grupo_contratado.dgco_id = empleado.dgco_id "
				+ "inner join ciudad ciudad on empleado.ciud_id = ciudad.ciud_id "
				+ "inner join detalle_tipo_parametro genero on genero.dtpa_id = empleado.sexo "
				+ "inner join detalle_tipo_parametro estado_civil on estado_civil.dtpa_id = empleado.estado_civil ";
		
		if (empleadoList.getTipoReporte().equals("Cargo")){
			String cargos = null;
			Long[] arregloCargos = empleadoList.getCargos();
			
			if (arregloCargos.length != 0) {
				cargos = Comunes.arrayToString(arregloCargos, Constantes.SEPARADOR_LISTAS);
				where = "where empleado.ciud_id in (" + ciudades + ") and "
						+ "empleado.controla_vacacion in (" + controlaVacacion + ") and "						
						+ "empleado.estado in (" + activoInactivo + ") and "
						+ "empleado.carg_id in (" + cargos + ") "
						+ "order by ciudad.descripcion, cargo.descripcion, empleado.apellido";
			}

		} else if(empleadoList.getTipoReporte().equals("Departamento")){
			String departamentos = null;
			Long[] arregloDepartamentos = empleadoList.getDepartamentos();
							
			if (arregloDepartamentos.length != 0) {
				departamentos = Comunes.arrayToString(arregloDepartamentos, Constantes.SEPARADOR_LISTAS);
				where = "where empleado.ciud_id in (" + ciudades + ") and "
						+ "empleado.controla_vacacion in (" + controlaVacacion + ") and "
						+ "empleado.estado in (" + activoInactivo + ") and "
						+ "empleado.depa_id in (" + departamentos + ") "
						+ "order by ciudad.descripcion, departamento.descripcion, empleado.apellido";
			} 
			
		} else if(empleadoList.getTipoReporte().equals("Grupo")) {
			String detalleGrupos = null;
			Long[] arregloDetalleGrupo = empleadoList.getDetalleGrupoContratados();
			
			if (arregloDetalleGrupo.length != 0) {
				detalleGrupos = Comunes.arrayToString(arregloDetalleGrupo, Constantes.SEPARADOR_LISTAS);
				where = "where empleado.ciud_id in (" + ciudades + ") and "
						+ "empleado.controla_vacacion in (" + controlaVacacion + ") and "						
						+ "empleado.estado in (" + activoInactivo + ") and "
						+ "empleado.dgco_id in (" + detalleGrupos + ") "
						+ "order by ciudad.descripcion, detalle_grupo_contratado.descripcion, empleado.apellido";
			} 
						
		} else if(empleadoList.getTipoReporte().equals("Empleado")) {
			Long emplId = empleadoList.getEmpleado().getEmplId();
			where = "where empleado.empl_id = "  + emplId ;
		}
		
		parametros.put("select",select + where);
				
		this.crearArchivo("reporteEmpleados", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
				"reporteEmpleados", empleadoList.getExtensionArchivo());
	}
	
	
}	
