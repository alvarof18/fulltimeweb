/**
 * 
 */
package com.casapazmino.fulltime.procesos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.InvalidValue;
import org.jboss.seam.Component;
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
import com.casapazmino.fulltime.action.CiudadFeriadoList;
import com.casapazmino.fulltime.action.EmpleadoHorarioList;
import com.casapazmino.fulltime.action.PlantillaPlanificacion;
import com.casapazmino.fulltime.comun.Conexion;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoHorario;
import com.casapazmino.fulltime.model.Horario;

/**
 * @author Fabricio
 *
 */

@Scope(ScopeType.SESSION)
@Name("planificacionRangoFechas")
@Synchronized(timeout=1800000)
public class PlanificacionRangoFechasBean implements PlanificacionRangoFechas {

	@Logger
	Log log;
	
	@In
	EntityManager entityManager;

	private String tipoReporte;
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	private boolean lunes;
	private boolean martes;
	private boolean miercoles;
	private boolean jueves;
	private boolean viernes;
	private boolean sabado;
	private boolean domingo;
 
	private Ciudad ciudad;
	private Horario horario;
	private Empleado empleado;
	private List<Empleado> empleados;
	
	@In(create = true)
	AsistenciaHome asistenciaHome;
	
	@In(create = true)
	CiudadFeriadoList ciudadFeriadoList;
	
	private Conexion conexion;
	
	private String dataBase;
	
	private ArrayList<Long> ciudades;
	private ArrayList<Long> cargos;
	private ArrayList<Long> detalleGrupoContratados;
	private ArrayList<Long> departamentos;
	
	private List<DetalleHorario> listaDetalleHorario;
	
	GestionarPlanificacion gestionarPlanificacion;
	
	public PlanificacionRangoFechasBean() {
		this.setTipoReporte("Cargo");
		conexion = new Conexion();

		ControlBaseDatos.colocarBaseDatos();

		dataBase = ControlBaseDatos.baseDatos;
		gestionarPlanificacion = new GestionarPlanificacion(conexion, dataBase);
	}

	//**********Métodos de búsqueda de empleado por agrupación (Cargo, Departamento, Contrato y Ciudad)*********
	@SuppressWarnings("unchecked")
	private List<Empleado> buscarEmpleadosCargo() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.cargo.cargId in (:cargos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) order by e.apellido")
				.setParameter("cargos", getCargos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Empleado> buscarEmpleadosDepartamento() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.departamento.depaId in (:departamentos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) order by e.apellido")
				.setParameter("departamentos", getDepartamentos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}	

	@SuppressWarnings("unchecked")
	private List<Empleado> buscarEmpleadosGrupo() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.detalleGrupoContratado.dgcoId in (:grupos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) order by e.apellido")
				.setParameter("grupos", getDetalleGrupoContratados())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
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
	

	
	//**********Búsqueda de empleados por selección de grupo **********
	public void buscarDatos() {
		try {
			if (tipoReporte.equals("Cargo")){
				
				getEmpleados().clear();
				getEmpleados().addAll(buscarEmpleadosCargo());	
				
			} else if (tipoReporte.equals("Departamento")){
				
				getEmpleados().clear();
				getEmpleados().addAll ( buscarEmpleadosDepartamento());
				
			} else if (tipoReporte.equals("Grupo")) {
				
				getEmpleados().clear();
				getEmpleados().addAll ( buscarEmpleadosGrupo());
				
			} else if (tipoReporte.equals("Empleado")){
				
				getEmpleados().clear();
				getEmpleados().add(empleado);
				
			}
		}catch (Exception e) {
			log.info(e);
		}
	}	
	
	@Override
	public String crearPlanificacionRangoFechas() {
				
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
			
			try{
				buscarDatos();		 
				int longitudListaEmpleados=empleados.size();
				if(longitudListaEmpleados>0){				
					
					CargarListaPlanificacion(empleados);					
					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("Registros Creados Correctamente!");
				}
				else{					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("No se Encontraron Registros!");
				}
			}catch(Exception e){
				e.printStackTrace();
				InvalidValue iv= new InvalidValue("Seleccione los parámetros de búsqueda correctamente!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}	
			
		}else{
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		return null;
	}

	@Override
	public void cargarTimbres(){	
		
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
			
			try{
				buscarDatos();		 
				int longitudListaEmpleados=empleados.size();
				if(longitudListaEmpleados>0){
					
					RecargarTimbresEmpleados(empleados);
					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("Registros Cargados Correctamente!");
				}
				else{					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("No se Encontraron Registros!");
				}
			}catch(Exception e){
				e.printStackTrace();
				InvalidValue iv= new InvalidValue("Seleccione los parámetros de búsqueda correctamente!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}	
			
		}else{
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}		
		
	}
	
	//Metodo de eliminacion a llamar en pagina 
	@Override
	public void eliminarPlanificacion(){
		
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
			
			try{
				buscarDatos();		 
				int longitudListaEmpleados=empleados.size();
				if(longitudListaEmpleados>0){
					
					EliminarListaPlanificacion(empleados);
					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("Registros Eliminados Correctamente!");
				}
				else{					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("No se Encontraron Registros!");
				}
			}catch(Exception e){
				e.printStackTrace();
				InvalidValue iv= new InvalidValue("Seleccione los parámetros de búsqueda correctamente!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}	
			
		}else{
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		CargarPagina();
		
	}
	
	//Cargar Página web	
	private void CargarPagina(){
		try{
			String url="/fulltime/fulltime/PlanificacionRangoFechas.seam"; //url donde se redirige la pantalla
	        FacesContext facesContext=new FacesContext();
	        facesContext.getContext();
			javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		}catch(Exception ex){
		}
	}
	
	//Gestion de eliminacion de Datos
	private void EliminarListaPlanificacion(List<Empleado> listaEmpleado){
		
		ArrayList<Long> listaIdEmpleado=gestionarPlanificacion.getListaCodigoEmpleado(listaEmpleado);
		
		//String listacodigos=gestionarPlanificacion.getCadenaArregloLong(listaIdEmpleado);
		
		List<String> listacodigos=gestionarPlanificacion.getListaCadenaArregloLong(listaIdEmpleado);
		
		for(int i=0;i<listacodigos.size();i++){
			gestionarPlanificacion.EliminarRegistros(listacodigos.get(i), fechaDesde, fechaHasta);
		}
		
		//gestionarPlanificacion.EliminarRegistros(listacodigos, fechaDesde, fechaHasta);
	}
	
	//Recargar Timbres de Empleados
	private void RecargarTimbresEmpleados(List<Empleado> listaEmpleado){
		
		ArrayList<Long> listaIdEmpleado=gestionarPlanificacion.getListaCodigoEmpleado(listaEmpleado);		
		List<String> listaCodigos=gestionarPlanificacion.getCadenaArregloLong(listaIdEmpleado);
		
		List <PlantillaPlanificacion> plantillaResultadosAsistencia=gestionarPlanificacion.buscarAsistenciaGrupo(listaCodigos, fechaDesde, fechaHasta);		
		
		ArrayList<String> listaCodigoEmpleado=gestionarPlanificacion.getListaCodigoRelojEmpleado(listaEmpleado);		
		List<String> listaCodigosReloj=gestionarPlanificacion.getCadenaArregloString(listaCodigoEmpleado);
		
		List <PlantillaPlanificacion> plantillaResultadosTimbre=gestionarPlanificacion.buscarTimbresGrupo(listaCodigosReloj, fechaDesde, fechaHasta);
		
		List <PlantillaPlanificacion> plantillaResultadosPermisoDesde=gestionarPlanificacion.buscarPermisoDesdeGrupo(listaCodigos, fechaDesde, fechaHasta);
		List <PlantillaPlanificacion> plantillaResultadosPermisoHasta=gestionarPlanificacion.buscarPermisoHastaGrupo(listaCodigos, fechaDesde, fechaHasta);
		
//		log.info("..........................................................plantillaResultadosAsistencia: "+plantillaResultadosAsistencia.size());
//		log.info("..........................................................plantillaResultadosTimbre: "+plantillaResultadosTimbre.size());
//		log.info("..........................................................plantillaResultadosPermisoDesde: "+plantillaResultadosPermisoDesde.size());
//		log.info("..........................................................plantillaResultadosPermisoHasta: "+plantillaResultadosPermisoHasta.size());
		
		gestionarPlanificacion.RecargarTimbres(plantillaResultadosAsistencia, plantillaResultadosTimbre, listaEmpleado, plantillaResultadosPermisoDesde, plantillaResultadosPermisoHasta);
	}	
	
	//Validar fechas de ingreso	
	private String ValidarFecha(){
		
		String msg="";
			
		if(fechaDesde.before(fechaHasta)||fechaDesde.equals(fechaHasta)){
			msg="ok";
		}else{
			msg="La Fecha Inicial no debe ser mayor que la Fecha Final!";						
		}
		
		return msg;
	}	
	
	//cargar Lista para mostrar en la pagina
	private void CargarListaPlanificacion(List <Empleado> listaEmpleado){
			
		ArrayList<Long> listaIdEmpleado=gestionarPlanificacion.getListaCodigoEmpleado(listaEmpleado);
		List<String> cadenaListaIdEmpleado=gestionarPlanificacion.getCadenaArregloLong(listaIdEmpleado);
		
		getListaDetalleHorario().clear();
		listaDetalleHorario= buscarDetalleHorario(horario.getHoraId());		

		List<Date> listaFecha=getListaFecha();
		List<String> listaEstado=getListaEstado(listaFecha);	
			
		List<PlantillaPlanificacion> asistenciaEmpleados=gestionarPlanificacion.buscarAsistenciaEmpleados(cadenaListaIdEmpleado,this.fechaDesde,this.fechaHasta);
		List<PlantillaPlanificacion> permisoEmpleados=gestionarPlanificacion.buscarPermisoEmpleados(cadenaListaIdEmpleado,fechaDesde,fechaHasta);
		List<PlantillaPlanificacion> permisoHorasEmpleados=gestionarPlanificacion.buscarPermisoHorasEmpleados(cadenaListaIdEmpleado,fechaDesde,fechaHasta);
		
		List<CiudadFeriado> listaCiudadFeriado=RetornarCiudadFeriado();
		List<CiudadFeriado> listaCiudadRecuperable=RetornarCiudadRecuperable();
		
		//log.info("****************************************************listaCiudadFeriado:"+listaCiudadFeriado.size());
		
		if(asistenciaEmpleados.size()>0){
			//CompararAsistencia
			List <PlantillaPlanificacion> plantilla=gestionarPlanificacion.CompararRegistros(listaEmpleado,listaFecha,asistenciaEmpleados,listaEstado,horario,listaDetalleHorario,permisoEmpleados,permisoHorasEmpleados,listaCiudadFeriado,listaCiudadRecuperable);
			gestionarPlanificacion.InsertarRegistros(plantilla);
		}else{
			//IngresarAsistencia			
			List <PlantillaPlanificacion> plantilla=gestionarPlanificacion.CrearPlantillaPlanificacion(listaEmpleado,listaFecha,listaEstado,listaDetalleHorario,permisoEmpleados,permisoHorasEmpleados,listaCiudadFeriado,listaCiudadRecuperable);
			gestionarPlanificacion.InsertarRegistros(plantilla);
				
		}
				
	}
	
	//Retorna feriados
	private List<CiudadFeriado> RetornarCiudadFeriado(){
		ciudadFeriadoList.setFeriadoDesde(null);
		ciudadFeriadoList.setFeriadoHasta(null);
		ciudadFeriadoList.setRecuperableDesde(null);
		ciudadFeriadoList.setRecuperableHasta(null);
		
		ciudadFeriadoList.setFeriadoDesde(this.fechaDesde);
		ciudadFeriadoList.setFeriadoHasta(this.fechaHasta);
		ciudadFeriadoList.setListaCiudades(ciudades);
		
		List<CiudadFeriado> listaCiudadFeriado=new ArrayList<CiudadFeriado>();
		
		try{
			listaCiudadFeriado=ciudadFeriadoList.getListaCiudadFeriados();
		}catch(Exception e){
			log.info("**********************PROBLEMA EN BÚSQUEDA DE FERIADOS**************");
			e.printStackTrace();
		}
		return listaCiudadFeriado;
	}
	
	//Retorna recuperable
	private List<CiudadFeriado> RetornarCiudadRecuperable(){
		
		ciudadFeriadoList.setFeriadoDesde(null);
		ciudadFeriadoList.setFeriadoHasta(null);
		ciudadFeriadoList.setRecuperableDesde(null);
		ciudadFeriadoList.setRecuperableHasta(null);
		
		ciudadFeriadoList.setRecuperableDesde(this.fechaDesde);
		ciudadFeriadoList.setRecuperableHasta(this.fechaHasta);
		ciudadFeriadoList.setListaCiudades(ciudades);
			
		List<CiudadFeriado> listaCiudadFeriado=new ArrayList<CiudadFeriado>();
			
		try{
			listaCiudadFeriado=ciudadFeriadoList.getListaCiudadFeriados();
		}catch(Exception e){
			log.info("**********************PROBLEMA EN BÚSQUEDA DE RECUPERABLES**************");
			e.printStackTrace();
		}
		return listaCiudadFeriado;
	}
	
	
	
	
	//Generar Lista de fechas a ingresar
	private List<Date> getListaFecha(){
		
		List<Date> listaFecha=new ArrayList<Date>();
		
		int año=gestionarPlanificacion.getAño(fechaDesde);
		int mes=gestionarPlanificacion.getMes(fechaDesde);
		int dia=gestionarPlanificacion.getDia(fechaDesde);
				
		Calendar calendar=gestionarPlanificacion.getCalendar();
		calendar.set(año,mes,dia);
		
		Date fecha=calendar.getTime();
		
		while ( fecha.before(fechaHasta)  ||  fecha.equals(fechaHasta) ){
			listaFecha.add(fecha);
			
			dia++;
			calendar=gestionarPlanificacion.getCalendar();
			calendar.set(año,mes,dia);			
			fecha=calendar.getTime();
		}		
		
		return listaFecha;
	
	}
	
	//Obtener Dias Libres y Laborables
	private List<String> getListaEstado(List<Date> listaFecha) {

		List<String> listaEstado = new ArrayList<String>();

		// int i=0;
		String diaMatriz = "";

		// while ( i<7 && i<listaFecha.size() ){
		for (int i = 0; i < listaFecha.size(); i++) {

			diaMatriz = "" + listaFecha.get(i);
			diaMatriz = diaMatriz.substring(0, 3);

			if (diaMatriz.equals("Mon")) {

				if (lunes)
					listaEstado.add("L");
				else
					listaEstado.add("FT");

			} else if (diaMatriz.equals("Tue")) {

				if (martes)
					listaEstado.add("L");
				else
					listaEstado.add("FT");

			} else if (diaMatriz.equals("Wed")) {
				if (miercoles)
					listaEstado.add("L");
				else
					listaEstado.add("FT");

			} else if (diaMatriz.equals("Thu")) {
				if (jueves)
					listaEstado.add("L");
				else
					listaEstado.add("FT");

			} else if (diaMatriz.equals("Fri")) {
				if (viernes)
					listaEstado.add("L");
				else
					listaEstado.add("FT");

			} else if (diaMatriz.equals("Sat")) {
				if (sabado)
					listaEstado.add("L");
				else
					listaEstado.add("FT");

			} else if (diaMatriz.equals("Sun")) {
				if (domingo)
					listaEstado.add("L");
				else
					listaEstado.add("FT");

			}

			// i++;
		}

		return listaEstado;

	}

	public String cancelar(){
		this.borraListas();
		return "null";
	}
	
	public void borraListas() {

		setCargos(null);
		setDepartamentos(null);
		setDetalleGrupoContratados(null);
		setEmpleado(null);
		setCiudades(null);
		setFechaDesde(null);
		setFechaHasta(null);
		setHorario(null);
		
		setLunes(false);
		setMartes(false);
		setMiercoles(false);
		setJueves(false);
		setViernes(false);
		setSabado(false);
		setDomingo(false);
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public boolean isLunes() {
		return lunes;
	}

	public void setLunes(boolean lunes) {
		this.lunes = lunes;
	}

	public boolean isMartes() {
		return martes;
	}

	public void setMartes(boolean martes) {
		this.martes = martes;
	}

	public boolean isMiercoles() {
		return miercoles;
	}

	public void setMiercoles(boolean miercoles) {
		this.miercoles = miercoles;
	}

	public boolean isJueves() {
		return jueves;
	}

	public void setJueves(boolean jueves) {
		this.jueves = jueves;
	}

	public boolean isViernes() {
		return viernes;
	}

	public void setViernes(boolean viernes) {
		this.viernes = viernes;
	}

	public boolean isSabado() {
		return sabado;
	}

	public void setSabado(boolean sabado) {
		this.sabado = sabado;
	}

	public boolean isDomingo() {
		return domingo;
	}

	public void setDomingo(boolean domingo) {
		this.domingo = domingo;
	}

	public Ciudad getCiudad() {
		if(ciudad ==  null) {
			ciudad = new Ciudad();
		}
		return ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		
		this.ciudad = ciudad;
	}

	public Horario getHorario() {
		return horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public ArrayList<Long> getCiudades() {
		if (ciudades == null){
			ciudades = new ArrayList<Long>();
		}
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}

	public ArrayList<Long> getCargos() {
		return cargos;
	}

	public void setCargos(ArrayList<Long> cargos) {
		this.cargos = cargos;
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
	
	public Empleado getEmpleado() {
		if (empleado == null) {
			empleado = new Empleado();
		}
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public List<Empleado> getEmpleados() {
		if(empleados==null)
			empleados=new ArrayList<Empleado>();
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}		

	public List<DetalleHorario> getListaDetalleHorario() {
		if(listaDetalleHorario==null)
			listaDetalleHorario=new ArrayList<DetalleHorario>();
		return listaDetalleHorario;
	}

	public void setListaDetalleHorario(List<DetalleHorario> listaDetalleHorario) {
		this.listaDetalleHorario = listaDetalleHorario;
	}
	
	public Date buscarUltimaFechaAsistencia(Empleado empleado) {
		return (Date) entityManager
				.createQuery(
						"select max(asistencia.fechaHoraHorario) from Asistencia asistencia where asistencia.empleado.emplId = (:empleado)")
				.setParameter("empleado", empleado.getEmplId())
				.getSingleResult();
	}
	
//	SE EJECUTA AUTOMATICAMENTE DE ACUERDO A PARAMETROS
	@Override
	public void planificarHorariosFijos() {
		
		Date fechaActual = new Date();
		Date ultimaFechaAsistencia = null;
		Boolean planificarEmpleado = false;
		String diasRevision = null;
		
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();		
		
		detalleTipoParametro = entityManager.find(DetalleTipoParametro.class, new Long(81));		
		diasRevision = detalleTipoParametro.getDescripcion();
				
		List<EmpleadoHorario> empleadosHorarios = new ArrayList<EmpleadoHorario>();
		
		EmpleadoHorarioList empleadoHorarioList = (EmpleadoHorarioList) Component
				.getInstance("empleadoHorarioList", true);

//		Buscar solo empleados activos
		empleadoHorarioList.getEmpleadoHorario().getEmpleado().getDetalleTipoParametroByEstado().setDescripcion("Activo");
		empleadosHorarios = empleadoHorarioList.getListaEmpleadoHorarios();
		
		for (EmpleadoHorario empleadoHorario : empleadosHorarios) {
			
			planificarEmpleado = false;
			
			ultimaFechaAsistencia = this.buscarUltimaFechaAsistencia(empleadoHorario.getEmpleado());

			if (ultimaFechaAsistencia != null) {
				
				Date fechaRevision = Fechas.SumarRestarDias(fechaActual, Integer.parseInt(diasRevision));
				
				if (fechaRevision.after(ultimaFechaAsistencia)) {
					this.fechaDesde = Fechas.SumarRestarDias(ultimaFechaAsistencia, 1);
					this.fechaHasta = Fechas.SumarRestarDias(ultimaFechaAsistencia, empleadoHorario.getDiasPlanificar());
					planificarEmpleado =  true;
				} 
			} else {
				
				this.fechaDesde = empleadoHorario.getFechaIni();
				this.fechaHasta = Fechas.SumarRestarDias(fechaDesde, empleadoHorario.getDiasPlanificar());
				planificarEmpleado =  true;

			}
			
			if (planificarEmpleado) {
//				Valida fecha final para no planificar mas de lo que indica en el horario del empleado
				if(this.fechaHasta.after(empleadoHorario.getFechaFin())){
					this.fechaHasta = empleadoHorario.getFechaFin();
				}

				this.crearPlanificacionHorarioFijo(empleadoHorario);
			}
						
		}
	}
		
	public void crearPlanificacionHorarioFijo(EmpleadoHorario empleadoHorario){

		List<Empleado> empleadosHorarioFijo = new ArrayList<Empleado>();
		
		this.horario = empleadoHorario.getHorario();
		
		this.lunes = empleadoHorario.isLunes();
		this.martes = empleadoHorario.isMartes();
		this.miercoles = empleadoHorario.isMiercoles();
		this.jueves = empleadoHorario.isJueves();
		this.viernes = empleadoHorario.isViernes();
		this.sabado = empleadoHorario.isSabado();
		this.domingo = empleadoHorario.isDomingo();
		
		this.getCiudades().add(empleadoHorario.getEmpleado().getCiudad().getCiudId());
					
		empleadosHorarioFijo.add(empleadoHorario.getEmpleado());
		this.CargarListaPlanificacion(empleadosHorarioFijo);
		empleadosHorarioFijo.clear();
		this.borraListas();
	}	
}
 