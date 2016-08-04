package com.casapazmino.fulltime.procesos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.hibernate.validator.InvalidValue;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.faces.FacesContext;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.AsistenciaHome;
import com.casapazmino.fulltime.action.PlantillaPlanificacion;
import com.casapazmino.fulltime.action.TimbreHome;
import com.casapazmino.fulltime.comun.Conexion;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Horario;

@Name("planificacion")
@Scope(ScopeType.SESSION)
@Synchronized(timeout=1800000)

public class PlanificacionBean implements Serializable, Planificacion {

	private static final long serialVersionUID = 8750596808938047779L;

	@In
	EntityManager entityManager;

	@In(create = true)
	AsistenciaHome asistenciaHome;
	
	@In(create = true)
	TimbreHome timbreHome;

	@Logger
	Log log;

	private Empleado empleado;
	private Horario horario;
	private List<DetalleHorario> detalleHorarios;

	private List<Empleado> empleados;
	private List<Asistencia> asistencias;

	private String tipoFecha;
	private String tipoReporte;
	
	private Date fechaSeleccionada;
	private TreeSet<Date> listaFechasLaborables;
	private TreeSet<Date> listaFechasLibres;
	private TreeSet<Date> listaFechasFeriados;
	
	// SE UTILIZAN PARA MOSTRAR LAS LISTAS EN LA PAGINA
	private List dataListFechasLaborables;
	private List dataListFechasLibres;
	private List dataListFechasFeriados;

	private ArrayList<Long> cargos;
	private ArrayList<Long> ciudades;
	private ArrayList<Long> detalleGrupoContratados;
	private ArrayList<Long> departamentos;
	
	private int fila;
	
	private List<DetalleHorario> listaDetalleHorario;
	
	private Conexion conexion;
	
	private String dataBase;
	
	GestionarPlanificacion gestionarPlanificacion;

	public PlanificacionBean() {
		this.tipoReporte = "Cargo";
		this.tipoFecha = "Laborable";
		
		conexion=new Conexion();
		dataBase=ControlBaseDatos.baseDatos;		
		gestionarPlanificacion=new GestionarPlanificacion(conexion, dataBase);
	}

	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosCargo() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.cargo.cargId in (:cargos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado)")
				.setParameter("cargos", getCargos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}
		
	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosDepartamento() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.departamento.depaId in (:departamentos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado)")
				.setParameter("departamentos", getDepartamentos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosGrupo() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.detalleGrupoContratado.dgcoId in (:grupos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado)")
				.setParameter("grupos", getDetalleGrupoContratados())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DetalleHorario> buscarDetalleHorario() {
		return (List<DetalleHorario>) entityManager
				.createQuery(
						"select dh from DetalleHorario dh inner join dh.horario h where h.descripcion = #{planificacion.horario.descripcion}")
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<DetalleHorario> buscarDetalleHorario(Long horaId) {
		return (List<DetalleHorario>) entityManager
				.createQuery(
						"select dh from DetalleHorario dh where dh.horario.horaId = (:horaId)")
				.setParameter("horaId", horaId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> BuscarAsistencia(Date fechaHorario) {

		return (List<Asistencia>) entityManager
				.createQuery(
						"select a from Asistencia a where  " + ControlBaseDatos.modificadorConvertirFecha + "a.fechaHoraHorario)=:fecha and a.estado = :estado")
				.setParameter("fecha", fechaHorario)
				.setParameter("estado",Constantes.ASISTENCIA_FALTA)
				.getResultList();
		
//		"select a from Asistencia a where " + ControlBaseDatos.modificadorConvertirFecha + "a.fechaHoraHorario)=:fecha and a.estado = :estado")

	}

	public String buscarDatos() {
		String ret = null;
		try {
			
			empleados.clear();
			if (getTipoReporte().equals("Cargo")){
				empleados = buscarEmpleadosCargo();
			} else if (getTipoReporte().equals("Departamento")){
				empleados = buscarEmpleadosDepartamento();
			} else if (getTipoReporte().equals("Grupo")) {
				empleados = buscarEmpleadosGrupo();
			} else if (getTipoReporte().equals("Empleado")) {
				empleados.add(this.empleado);
			}
			
			detalleHorarios = buscarDetalleHorario();

			if (getEmpleados().size() == 0 || getDetalleHorarios().size() == 0) {
				ret = null;
			} else if (getEmpleados().size() != 0
					&& getDetalleHorarios().size() != 0) {
				ret = "ok";
			}

		} catch (Exception e) {
			log.info(e);
			ret = null;
		}

		return ret;

	}
	
	public String cancelar(){
		borraListas();
		return null;
	}

	public void agregarFecha() {

		// AGREGAR LISTA DE FECHAS DE ACUERDO AL TIPO SELECCIONADO
		
		if (this.getTipoFecha().equals("Laborable")){
			getListaFechasLaborables().add(getFechaSeleccionada());
		} else if (this.getTipoFecha().equals("Libre")){
			getListaFechasLibres().add(getFechaSeleccionada());
		} else if (this.getTipoFecha().equals("Feriado")){
			getListaFechasFeriados().add(getFechaSeleccionada());
		}

	}

	public void eliminarFechaLaborable() {
		getDataListFechasLaborables().remove(fila);
		getListaFechasLaborables().clear();
		getListaFechasLaborables().addAll(getDataListFechasLaborables());
	}

	public void eliminarFechaLibre() {
		getDataListFechasLibres().remove(fila);
		getListaFechasLibres().clear();
		getListaFechasLibres().addAll(getDataListFechasLibres());
	}

	public void eliminarFechaFeriado() {
		getDataListFechasFeriados().remove(fila);
		getListaFechasFeriados().clear();
		getListaFechasFeriados().addAll(getDataListFechasFeriados());
	}

	public void borraListas() {
		getListaFechasLaborables().clear();
		getDataListFechasLaborables().clear();
		
		getListaFechasLibres().clear();
		getDataListFechasLibres().clear();

		getListaFechasFeriados().clear();
		getDataListFechasFeriados().clear();

		setAsistencias(null);
		
		setEmpleado(null);
		setEmpleados(null);
		setDetalleHorarios(null);

		setCargos(null);
		setCiudades(null);
		setDetalleGrupoContratados(null);
		setDepartamentos(null);
	}

	public String asignarListaFechas() {
		String ret = null;
		try {
			
			if (getListaFechasLaborables().size() == 0 && getListaFechasLibres().size() == 0 && getListaFechasFeriados().size() == 0 ) {
				ret = null;
			}
			
			if (getListaFechasLaborables().size() != 0) {
				getDataListFechasLaborables().clear();
				getDataListFechasLaborables().addAll(listaFechasLaborables);
			} 
			
			if (getListaFechasLibres().size() != 0) {
				getDataListFechasLibres().clear();
				getDataListFechasLibres().addAll(listaFechasLibres);
			} 

			if (getListaFechasFeriados().size() != 0) {
				getDataListFechasFeriados().clear();
				getDataListFechasFeriados().addAll(listaFechasFeriados);
			}
			
			ret = "ok";
			
		} catch (Exception e) {
			log.info(e);
			ret = null;
		}

		return ret;
	
		/*
		 * for (Iterator iterator = dataListFechas.iterator();
		 * iterator.hasNext();) { Date fecha = (Date) iterator.next(); }
		 */
	}

	public String verificar() {
		String estado = null;

		estado = asignarListaFechas();
		if (estado != null) {
			estado = buscarDatos();
		}

		if (estado == null) {
			FacesMessages.instance().add(
							"No se ha seleccionado ninguna fecha, No Existe empleados o No existe Detalle Horario");
		} else {
			FacesMessages.instance().add(
							"Comprobación finalizada, presione Procesar o Eliminar para terminar proceso");
			estado = "ok";
		}
		return estado;

	}
	
	public List<Calendar> recorrerFechas(TreeSet<Date> listaFechas){
		List<Calendar> fechas = new ArrayList<Calendar>();
		
		for (Date date : listaFechas) {
			Calendar fechaCalendar = Calendar.getInstance();
			Date fecha = date;
			fechaCalendar.setTime(fecha);
			fechas.add(fechaCalendar);
		}
		
		return fechas;
		
/*		List<Calendar> fechas = new ArrayList<Calendar>();

		Iterator it = listaFechasLaborables.iterator();
		while (it.hasNext()) {
			Calendar fechaCalendar = Calendar.getInstance();
			Date fecha = (Date) it.next();
			fechaCalendar.setTime(fecha);
			fechas.add(fechaCalendar);
		}
*/		
	}

	private String verificarInformacion(){
		String validacion="Verifique la información antes de crear la planificación!";
		
		int tamañoEmpleado=getEmpleados().size();
		int tamañoLaborable=getDataListFechasLaborables().size();
		int tamañoLibre=getDataListFechasLibres().size();
		int tamañoFeriado=getDataListFechasFeriados().size();
		
		if( tamañoEmpleado>0 && (tamañoLaborable>0 || tamañoLibre>0 || tamañoFeriado>0) )
			validacion="ok";
		
		return validacion;
		
			
	}
	
	@Override
	public String crearPlanificacion() {
		
		String estado=verificarInformacion();
		
		if(estado.equals("ok")){
			
			try{
				
				CargarListaPlanificacion(empleados);
				
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Creados Correctamente!");
				
			}catch(Exception e){
				e.printStackTrace();
				InvalidValue iv= new InvalidValue("Seleccione los parámetros de búsqueda correctamente!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}	
			
		}else{
			InvalidValue iv= new InvalidValue(estado,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		return null;
	}
	
	
		
//		if(estdo)
//
//		String estado = null;
//		estado = verificar();
//
//		if (estado != null) {
//			List<Calendar> fechas = new ArrayList<Calendar>();
//			
//			if (getListaFechasLaborables().size() != 0){
//				fechas.clear();
//				fechas = this.recorrerFechas(listaFechasLaborables);
//				
//				asistenciaHome.crearListaInsertarAsistencia(empleados, fechas,
//						detalleHorarios, Constantes.ASISTENCIA_FALTA);
//			}
//
//			if (getListaFechasLibres().size() != 0){
//				fechas.clear();
//				fechas = this.recorrerFechas(listaFechasLibres);
//				
//				asistenciaHome.crearListaInsertarAsistencia(empleados, fechas,
//						detalleHorarios, Constantes.ASISTENCIA_LIBRE);
//			}
//			
//			if(getListaFechasFeriados().size() != 0){
//				fechas.clear();
//				fechas = this.recorrerFechas(listaFechasFeriados);
//				
//				asistenciaHome.crearListaInsertarAsistencia(empleados, fechas,
//						detalleHorarios, Constantes.ASISTENCIA_FERIADO);
//			}
//
//			this.borraListas();
//			FacesMessages.instance().add("Proceso realizado satisfactoriamente");
//			
//		} else {
//			FacesMessages.instance().add("Proceso no realizado, verifique selección de datos");
//		}
	
	//cargar Lista para mostrar en la pagina
		private void CargarListaPlanificacion(List <Empleado> listaEmpleado){
				
			ArrayList<Long> listaIdEmpleado=gestionarPlanificacion.getListaCodigoEmpleado(listaEmpleado);
			List<String> cadenaListaIdEmpleado=gestionarPlanificacion.getCadenaArregloLong(listaIdEmpleado);
			
			getListaDetalleHorario().clear();
			listaDetalleHorario= buscarDetalleHorario(horario.getHoraId());		

			List<Date> listaFecha=getListaFecha();
			
			List<String> listaEstado=getListaEstado();
			
			Date fechaDesde=MinDate(listaFecha);
			Date fechaHasta=MaxDate(listaFecha);
				
			List<PlantillaPlanificacion> asistenciaEmpleados=gestionarPlanificacion.buscarAsistenciaEmpleados(cadenaListaIdEmpleado,fechaDesde,fechaHasta);
			List<PlantillaPlanificacion> permisoEmpleados=gestionarPlanificacion.buscarPermisoEmpleados(cadenaListaIdEmpleado,fechaDesde,fechaHasta);
			List<PlantillaPlanificacion> permisoHorasEmpleados=gestionarPlanificacion.buscarPermisoHorasEmpleados(cadenaListaIdEmpleado,fechaDesde,fechaHasta);
			
			List<CiudadFeriado> listaCiudadFeriado=new ArrayList<CiudadFeriado>();
			List<CiudadFeriado> listaCiudadRecuperable=new ArrayList<CiudadFeriado>();
				
			if(asistenciaEmpleados.size()>0){
				//CompararAsistencia
				List <PlantillaPlanificacion> plantilla=gestionarPlanificacion.CompararRegistros(listaEmpleado,listaFecha,asistenciaEmpleados,listaEstado,horario,listaDetalleHorario,permisoEmpleados,permisoHorasEmpleados, listaCiudadFeriado,listaCiudadRecuperable);
				gestionarPlanificacion.InsertarRegistros(plantilla);
			}else{
				//IngresarAsistencia
				List <PlantillaPlanificacion> plantilla=gestionarPlanificacion.CrearPlantillaPlanificacion(listaEmpleado,listaFecha,listaEstado,listaDetalleHorario,permisoEmpleados, permisoHorasEmpleados,listaCiudadFeriado,listaCiudadRecuperable);
				gestionarPlanificacion.InsertarRegistros(plantilla);
					
			}

		}
		
		//Generar Lista de fechas a ingresar
		private List<Date> getListaFecha(){
			
			List<Date> listaFecha=new ArrayList<Date>();
			
			listaFecha.addAll(listaFechasLaborables);
			listaFecha.addAll(listaFechasLibres);
			listaFecha.addAll(listaFechasFeriados);			
			
			return listaFecha;
		
		}
		
		//Obtener Dias Libres y Laborables
		private List<String> getListaEstado(){
			
			List<String> listaEstado=new ArrayList<String>();
			
			for(int i=0;i<listaFechasLaborables.size();i++){
				listaEstado.add("FT");
			}
			
			for(int i=0;i<listaFechasLibres.size();i++){
				listaEstado.add("L");
			}
			
			for(int i=0;i<listaFechasFeriados.size();i++){
				listaEstado.add("FD");
			}
					
			return listaEstado;
			
		}
		
	private Date MaxDate(List<Date> listaFecha){
		Date fechaMaxima=null;
		
		for(int i=0;i<listaFecha.size();i++){
			Date fecha=listaFecha.get(i);
			
			if(i==0)
				fechaMaxima=fecha;
			else
				if(fecha.after(fechaMaxima))
					fechaMaxima=fecha;		
		}
		
		return fechaMaxima;
	}
	
	private Date MinDate(List<Date> listaFecha){
		Date fechaMinima=null;
		
		for(int i=0;i<listaFecha.size();i++){
			Date fecha=listaFecha.get(i);
			
			if(i==0)
				fechaMinima=fecha;
			else
				if(fecha.before(fechaMinima))
					fechaMinima=fecha;		
		}
		
		return fechaMinima;
	}

	public String eliminarPlanificacion() {

		String estado=verificarInformacion();
		
		if(estado.equals("ok")){
			
			try{
				
				EliminarListaPlanificacion(empleados);

				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Eliminados Correctamente!");
				
				

				
			}catch(Exception e){
				e.printStackTrace();
				InvalidValue iv= new InvalidValue("Seleccione los parámetros de búsqueda correctamente!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}	
			
		}else{
			InvalidValue iv= new InvalidValue(estado,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		CargarPagina();
		
		return null;
	}
	
	//Cargar Página web	
	private void CargarPagina(){
		try{
			String url="/fulltime/fulltime/PlanificacionList.seam"; //url donde se redirige la pantalla
	        FacesContext facesContext=new FacesContext();
	        facesContext.getContext();
			javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void EliminarListaPlanificacion(List<Empleado> listaEmpleado){
		
		List<Date> listaFecha=getListaFecha();
		
		Date fechaDesde=MinDate(listaFecha);
		
		Date fechaHasta=MaxDate(listaFecha);
		
		ArrayList<Long> listaIdEmpleado=gestionarPlanificacion.getListaCodigoEmpleado(listaEmpleado);
		
		//String listacodigos=gestionarPlanificacion.getCadenaArregloLong(listaIdEmpleado);
		
		List<String> listacodigos=gestionarPlanificacion.getListaCadenaArregloLong(listaIdEmpleado);
		
		for(int i=0;i<listacodigos.size();i++){
			gestionarPlanificacion.EliminarRegistros(listacodigos.get(i), fechaDesde, fechaHasta);
		}
		
		//gestionarPlanificacion.EliminarRegistros(listacodigos, fechaDesde, fechaHasta);
	}

	// Lanzar este proceso luego de planificar
	public String cargarTimbres(){
		
		String estado=verificarInformacion();
		
		if(estado.equals("ok")){
			
			try{
				
				RecargarTimbresEmpleados(empleados);

				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Cargados Correctamente!");

				
			}catch(Exception e){
				e.printStackTrace();
				InvalidValue iv= new InvalidValue("Seleccione los parámetros de búsqueda correctamente!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}	
			
		}else{
			InvalidValue iv= new InvalidValue(estado,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		return null;
	}
	
	//Recargar Timbres de Empleados
	private void RecargarTimbresEmpleados(List<Empleado> listaEmpleado){
		
		List<Date> listaFecha=getListaFecha();
		
		Date fechaDesde=MinDate(listaFecha);		
		Date fechaHasta=MaxDate(listaFecha);
		
		ArrayList<Long> listaIdEmpleado=gestionarPlanificacion.getListaCodigoEmpleado(listaEmpleado);		
		List<String> listaCodigos=gestionarPlanificacion.getCadenaArregloLong(listaIdEmpleado);
		
		List <PlantillaPlanificacion> plantillaResultadosAsistencia=gestionarPlanificacion.buscarAsistenciaGrupo(listaCodigos, fechaDesde, fechaHasta);		
		
		ArrayList<String> listaCodigoEmpleado=gestionarPlanificacion.getListaCodigoRelojEmpleado(listaEmpleado);		
		List<String> listaCodigosReloj=gestionarPlanificacion.getCadenaArregloString(listaCodigoEmpleado);
		
		List <PlantillaPlanificacion> plantillaResultadosTimbre=gestionarPlanificacion.buscarTimbresGrupo(listaCodigosReloj, fechaDesde, fechaHasta);
		
		List <PlantillaPlanificacion> plantillaResultadosPermisoDesde=gestionarPlanificacion.buscarPermisoDesdeGrupo(listaCodigos, fechaDesde, fechaHasta);
		List <PlantillaPlanificacion> plantillaResultadosPermisoHasta=gestionarPlanificacion.buscarPermisoHastaGrupo(listaCodigos, fechaDesde, fechaHasta);
		
		gestionarPlanificacion.RecargarTimbres(plantillaResultadosAsistencia, plantillaResultadosTimbre, listaEmpleado, plantillaResultadosPermisoDesde, plantillaResultadosPermisoHasta);
	}
		
	/*
	 * GET - SET
	 */

	public Date getFechaSeleccionada() {
		return fechaSeleccionada;
	}

	public void setFechaSeleccionada(Date fechasSeleccionadas) {
		this.fechaSeleccionada = fechasSeleccionadas;
	}

	public Empleado getEmpleado() {
		if (empleado == null) {
			empleado = new Empleado();
		}
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Horario getHorario() {
		if (horario == null) {
			horario = new Horario();
		}
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public List<Empleado> getEmpleados() {
		if (empleados == null) {
			empleados = new ArrayList<Empleado>();
		}
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}

	public List<DetalleHorario> getDetalleHorarios() {
		if (detalleHorarios == null) {
			detalleHorarios = new ArrayList<DetalleHorario>();
		}
		return detalleHorarios;
	}

	public void setDetalleHorarios(List<DetalleHorario> detalleHorarios) {
		this.detalleHorarios = detalleHorarios;
	}

	public List<Asistencia> getAsistencias() {
		if (asistencias == null) {
			asistencias = new ArrayList<Asistencia>();
		}

		return asistencias;
	}

	public void setAsistencias(List<Asistencia> asistencias) {
		this.asistencias = asistencias;
	}

	public TreeSet<Date> getListaFechasLaborables() {
		if (listaFechasLaborables == null) {
			listaFechasLaborables = new TreeSet();
		}
		return listaFechasLaborables;
	}

	public void setListaFechasLaborables(TreeSet<Date> listaFechasLaborables) {
		this.listaFechasLaborables = listaFechasLaborables;
	}

	public TreeSet<Date> getListaFechasLibres() {
		if (listaFechasLibres == null) {
			listaFechasLibres = new TreeSet();
		}
		return listaFechasLibres;
	}

	public void setListaFechasLibres(TreeSet<Date> listaFechasLibres) {
		this.listaFechasLibres = listaFechasLibres;
	}

	public TreeSet<Date> getListaFechasFeriados() {
		if (listaFechasFeriados == null) {
			listaFechasFeriados = new TreeSet();
		}
		return listaFechasFeriados;
	}

	public void setListaFechasFeriados(TreeSet<Date> listaFechasFeriados) {
		this.listaFechasFeriados = listaFechasFeriados;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}


	public ArrayList<Long> getCargos() {
		return cargos;
	}

	public void setCargos(ArrayList<Long> cargos) {
		this.cargos = cargos;
	}

	public ArrayList<Long> getCiudades() {
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public ArrayList<Long> getDetalleGrupoContratados() {
		return detalleGrupoContratados;
	}

	public void setDetalleGrupoContratados(ArrayList<Long> detalleGrupoContratados) {
		this.detalleGrupoContratados = detalleGrupoContratados;
	}

	public ArrayList<Long> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(ArrayList<Long> departamentos) {
		this.departamentos = departamentos;
	}
	
	public String getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(String tipoFecha) {
		this.tipoFecha = tipoFecha;
	}
	
	public List getDataListFechasLaborables() {
		if (dataListFechasLaborables == null) {
			dataListFechasLaborables = new ArrayList();
		}

		return dataListFechasLaborables;
	}

	public void setDataListFechasLaborables(List dataListFechasLaborables) {
		this.dataListFechasLaborables = dataListFechasLaborables;
	}
	
	public List getDataListFechasLibres() {
		if (dataListFechasLibres == null) {
			dataListFechasLibres = new ArrayList();
		}

		return dataListFechasLibres;
	}

	public void setDataListFechasLibres(List dataListFechasLibres) {
		this.dataListFechasLibres = dataListFechasLibres;
	}

	public List getDataListFechasFeriados() {
		if (dataListFechasFeriados == null) {
			dataListFechasFeriados = new ArrayList();
		}

		return dataListFechasFeriados;
	}

	public void setDataListFechasFeriados(List dataListFechasFeriados) {
		this.dataListFechasFeriados = dataListFechasFeriados;
	}
	

//	public void insertarAsistenciaStoreProcedure(List<Empleado> empleados, List<Calendar> fechas, List<DetalleHorario> detalleHorarios, String estado) {
//		
//		Calendar fechaCalendar = Calendar.getInstance();
//		
//		// Recorrer lista de empleados
//		for (Empleado e : empleados) {
////			listPersistAsistencias.clear();
//			// Recorrer lista de Fechas Seleccionadas
//			Iterator it = fechas.iterator();
//			while (it.hasNext()) {
//				fechaCalendar = (Calendar) it.next();
//				// Recorrer lista de horario seleccionado 
//				
//				List<Asistencia> listAsistencias = new ArrayList<Asistencia>();
//				listAsistencias.clear();
//				
//				if (listAsistencias.size() == 0 ){
//					for (DetalleHorario dh : detalleHorarios) {
//					
//						Asistencia asistencia = new Asistencia();
//					
//						asistencia.setDetalleHorario(dh);
//						asistencia.setEmpleado(e); 
//						asistencia.setCodigoEmpleado(e.getCodigoEmpleado());
//						asistencia.setEntradaSalida(dh.getEntradaSalida());
//						asistencia.setFechaHoraHorario(this.crearFechasAsistencia(
//							fechaCalendar, dh.getHora().getHours(), dh
//									.getHora().getMinutes()));
//						asistencia.setFechaHoraTimbre(null);
//						asistencia.setAsisOrden((int) dh.getOrden());
//						asistencia.setAsisMaxMinuto(dh.getMaxMinuto());
//						asistencia.setEstado(estado);
//					} // TERMINO DE RECORRER DETALLE HORARIO
//				} 
//			} // TERMINO DE RECORRER FECHA
//		} // TERMINO DE RECORRER EMPLEADOS
//		
//	}
//	
//	public Date crearFechasAsistencia(Calendar fechaHorario, int hora,
//			int minuto) {
//		Calendar fecha = Calendar.getInstance();
//		fecha.set(fechaHorario.get(Calendar.YEAR), fechaHorario
//				.get(Calendar.MONTH), fechaHorario.get(Calendar.DAY_OF_MONTH),
//				hora, minuto, 00);
//		Date fechaAsistencia = fecha.getTime();
//		return fechaAsistencia;
//	}
//	
	
	public List<DetalleHorario> getListaDetalleHorario() {
		if(listaDetalleHorario==null)
			listaDetalleHorario=new ArrayList<DetalleHorario>();
		return listaDetalleHorario;
	}

	public void setListaDetalleHorario(List<DetalleHorario> listaDetalleHorario) {
		this.listaDetalleHorario = listaDetalleHorario;
	}

}