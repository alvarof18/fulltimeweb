package com.casapazmino.fulltime.procesos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.AsistenciaHome;
import com.casapazmino.fulltime.action.HorarioHome;
import com.casapazmino.fulltime.action.PlanificacionHome;
import com.casapazmino.fulltime.action.PlanificacionList;
import com.casapazmino.fulltime.action.TimbreHome;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.model.Planificacion;

@Name("CambioTurno")
@Scope(ScopeType.SESSION)
public class CambioTurnoBean implements Serializable {

	private static final long serialVersionUID = 8750596808938047779L;
												
	@In
	EntityManager entityManager;

	@In(create = true)
	AsistenciaHome asistenciaHome;
	
	@In(create = true)
	PlanificacionHome planificacionHome;
	
	@In(create = true)
	TimbreHome timbreHome;
	
	@In(create = true)
	HorarioHome horarioHome;

	@Logger
	Log log;

	private Empleado empleado;
	private Empleado empleadoselect;
	private Horario horario1;
	private Horario horario2;
	private Horario Horarioseleccionado;
	private List<DetalleHorario> detalleHorarios,detallehorarioselect;

	private List<Empleado> empleados;
	
	private List<Asistencia> asistencias,asistenciaselect,asistenciaAdmin;
	private List<Horario> horarios;
	private List<com.casapazmino.fulltime.model.Planificacion> listPlanificacion;

	private String tipoFecha;
	private String tipoReporte;
	private Integer year;
	private Integer mes;
	
	private Date fechaSeleccionada;
	
	private TreeSet<Date> listaFechasLaborables;
	private TreeSet<Date> listaFechasLibres;
	private TreeSet<Date> listaFechasFeriados;
	
	
	
	// SE UTILIZAN PARA MOSTRAR LAS LISTAS EN LA PAGINAS
	private List dataListFechasLaborables;
	private List dataListFechasLibres;
	private List dataListFechasFeriados;

	private ArrayList<Long> cargos;
	private ArrayList<Long> ciudades;
	private ArrayList<Long> detalleGrupoContratados;
	private ArrayList<Long> departamentos;
	
	private int fila;

	public CambioTurnoBean() {
		this.tipoReporte = "Cargo";
		this.tipoFecha = "Laborable";
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
	public List<DetalleHorario> buscarDetalleHorario(Horario ho) {
		log.info("horario seleccionado====="+ho.getDescripcion());
		return (List<DetalleHorario>) entityManager
				.createQuery(
						"select dh from DetalleHorario dh inner join dh.horario h where h.descripcion ='"+ho.getDescripcion()+"'")
				.getResultList();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<DetalleHorario> buscarDetalleHorarioiddetalle(Long ho) {
		//log.info("horario seleccionado====="+ho.getDescripcion());
		return (List<DetalleHorario>) entityManager
				.createQuery(
						"select dh from DetalleHorario dh inner join dh.horario h where dh.dehoId ='"+ho+"'")
				.getResultList();
		
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> buscarAsistencia(Date fechaHorario) {

		return (List<Asistencia>) entityManager
				.createQuery(
						"select a from Asistencia a where  " + ControlBaseDatos.modificadorConvertirFecha + "a.fechaHoraHorario)=:fecha and a.estado = :estado")
				.setParameter("fecha", fechaHorario)
				.setParameter("estado",Constantes.ASISTENCIA_FALTA)
				.getResultList();

	}
	
	public Query crearSentenciaModificarAsistencia(Long detalle, String Entrada, Long modificado){
		Query query =  entityManager.createQuery("update Asistencia set dehoIdNuevo = (:detalle), modificado=(:Modificado) where empleado.emplId=(:empleado) and fechaHorario=(:fechaHoraHorario) and entradaSalida=(:Entrada)")								
				.setParameter("empleado",empleadoselect.getEmplId())
				.setParameter("fechaHoraHorario",fechaSeleccionada)
				.setParameter("Entrada",Entrada)
				.setParameter("Modificado",modificado)
				.setParameter("detalle",detalle);
		return query;
	}
	
	public Query crearSentenciaModificarAsistenciaDetalle(Long empleado,Long detalle, Date fecha, String Entrada){
		Query query =  entityManager.createQuery("update Asistencia set detalleHorario.dehoId = (:detalle), fechaHoraHorario=(:fechaHoraHorario) where empleado.emplId=(:empleado) and fechaHorario=(:fechaHora) and entradaSalida=(:Entrada)")								
				.setParameter("empleado",empleado)
				.setParameter("fechaHoraHorario",fecha)
				.setParameter("fechaHora",fechaSeleccionada)
				.setParameter("Entrada",Entrada)
				.setParameter("detalle",detalle);
		return query;
	}
	
	public boolean valida(int num){
		
		Calendar calendario=Calendar.getInstance();
		Calendar calendario2=Calendar.getInstance();
		
				
		
		int diaDesde=calendario.get(Calendar.DAY_OF_MONTH);
		int diaHasta=calendario2.get(Calendar.DAY_OF_MONTH);
		
		
		
		if(num>=diaDesde&&num<=diaHasta){
			//log.info("numero dentro del if"+num);
			return true;
		}
		else
			return false;
	}
	

	public String buscarDatos() {
		String ret = null;
		
		
		try {
			if (getTipoReporte().equals("Cargo")){
				
				this.getEmpleados().addAll(buscarEmpleadosCargo());
				ret = "ok";
			} else if (getTipoReporte().equals("Departamento")){
				empleados = buscarEmpleadosDepartamento();
				ret = "ok";
			} else if (getTipoReporte().equals("Grupo")) {
				empleados = buscarEmpleadosGrupo();
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
		
		asignarListaFechas();

	}
	
	public void eliminarAsistencia() {
		
		getAsistencias().remove(fila);
//		getListaFechasLaborables().clear();
//		getListaFechasLaborables().addAll(getDataListFechasLaborables());
//		
//		List<Asistencia> asistenciaselect=new ArrayList<Asistencia>();
//		Asistencia asistenciaselec = new Asistencia();
//		asistenciaselec=asistencias.get(fila);
//		Empleado emple=asistenciaselec.getEmpleado();
//		//Date fechasel=asistenciaselec.getFechaHoraHorario();
//		String est=asistenciaselec.getEstado();
//		
//		for(Asistencia as: asistencias){
//			//log.info("asistencias==="+as.getEmpleado().getNombre()+"-"+as.getDetalleHorario().getEntradaSalida());
//			if(as.getEmpleado()==emple&& as.getEstado()==est){
//				asistenciaselect.add(as);
//			}
//			
//			log.info("asistenciaseleccionada==="+asistenciaselec.getEmpleado().getNombre()+"-"+asistenciaselec.getDetalleHorario().getEntradaSalida());
//		}
//		log.info("fila==="+getFila());
//		getAsistencias().removeAll(asistenciaselect);
//		//getAsistencias().clear();
//		for(Asistencia as: asistencias){
//			log.info("asistencias==="+as.getEmpleado().getNombre()+"-"+as.getDetalleHorario().getEntradaSalida());
//			
//		}
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
			
			if (getListaFechasLaborables().size() != 0) {
				getListaFechasLaborables().clear();
				getDataListFechasLaborables().clear();
			} 
			
			if (getListaFechasLibres().size() != 0) {
				getListaFechasLibres().clear();
				getDataListFechasLibres().clear();
			} 

			if (getListaFechasFeriados().size() != 0) {
				getListaFechasFeriados().clear();
				getDataListFechasFeriados().clear();
			}
			
						
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
	}
	
	
	

	
	
	public Date transformaFecha(String fecha, String formato){
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat(formato);
		Date fechad = null;
		try {

		fechad = formatoDelTexto.parse(fecha);

		} catch (Exception e) {
			FacesMessages.instance().add("Error al autorizar");
			e.printStackTrace();
		}
		return fechad;
	}
	
	
	public void guardar(){
		
		//String sentenciaNativa = null;
		//String fecha=fechaSeleccionada.toString();
		//log.info("Entro a buscar detalle horario "+empleadoselect+fechaSeleccionada+horario1.getDescripcion());
		detalleHorarios=buscarDetalleHorario(horario1);
		
		//asistenciaHome.guardarAsistencia(asistencias,empleadoselect, fechaSeleccionada, detalleHorarios);	
		
		
		for (DetalleHorario detallehorarios : detalleHorarios) {
			if(detallehorarios.getEntradaSalida().equalsIgnoreCase("E")){
				Query query = this.crearSentenciaModificarAsistencia(detallehorarios.getDehoId(),"E",(long)1);
				query.executeUpdate();
				FacesMessages.instance().add("Proceso realizado...Guardar");
			}
			else{
				Query query = this.crearSentenciaModificarAsistencia(detallehorarios.getDehoId(),"S",(long)1);
				query.executeUpdate();
				FacesMessages.instance().add("Proceso realizado...Guardar");
			}
			//String sentenciaNativa = "UPDATE asistencia a set a.dehoIdNuevo= (:detalle) where a.empleado.emplId =(:empleado) and" + ControlBaseDatos.modificadorConvertirFecha + "a.fechaHoraHorario)=(:fechaHoraHorario);";
			
		}
		//log.info("Se ejecuto el codigo ");
		
	}
	
	
	// TODO: REVISAR	
	public List<Asistencia> buscarAsistencias(){
		
		Calendar cal=Calendar.getInstance();
		
		cal.setTime(fechaSeleccionada);
		
//		asistencias=asistenciaHome.buscarAsistencia(empleadoselect, cal);;
	
		return asistencias;
	}
	
// TODO: REVISAR
	public List<Asistencia> buscarAsistenciasAdmin(){
		
//		asistenciaAdmin=asistenciaHome.buscarAsistenciaAdmin();
		
		return asistenciaAdmin;
	}
	
	public List<Planificacion> buscarPlanificacion(List<Empleado> empleados,Integer mes, Integer anio){
		
		
		List<Planificacion> planificaciones =  new ArrayList<Planificacion>();
		
		PlanificacionList planificacionList = (PlanificacionList) Component.getInstance("planificacionList", true);
		
		for (Empleado empleado : empleados) {
			planificacionList.getPlanificacion().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
			planificacionList.getPlanificacion().setMes(mes);
			planificacionList.getPlanificacion().setAnio(anio);
			planificaciones.addAll(planificacionList.getListaPlanificacion());
		}
		 
		return planificaciones;
	}
	
	public void procesar(){
		
		log.info("===fila=="+fila);
		Asistencia asistencia=new Asistencia();
		asistencia=asistenciaAdmin.get(fila);
		log.info("===asistencia"+asistencia.getCodigoEmpleado());
		
		Calendar cal=Calendar.getInstance();
		Long aux=null;
		Date fecha=asistencia.getFechaHorario();
		cal.setTime(fecha);
		log.info("===fecha"+fecha);
		Date fechaasistencia;
		
		//asistenciaselect=asistenciaHome.buscarAsistencia(empleadoselect, cal);
		
		//for (Asistencia asistencia : asistenciaselect) {
		aux=asistencia.getDehoIdNuevo();
		detallehorarioselect=buscarDetalleHorarioiddetalle(aux);
		for (DetalleHorario detalle : detallehorarioselect) {
					
					fechaasistencia=asistenciaHome.crearFechasAsistencia(cal, detalle
							.getHora().getHours(), detalle.getHora()
							.getMinutes());
					if(detalle.getEntradaSalida().equalsIgnoreCase("E")){
					Query query = this.crearSentenciaModificarAsistenciaDetalle(asistencia.getEmpleado().getEmplId(),aux,fechaasistencia,"E");
					query.executeUpdate();
					FacesMessages.instance().add("Proceso realizado...Modificado asistencia");
									
				}
					else{
						Query query = this.crearSentenciaModificarAsistenciaDetalle(asistencia.getEmpleado().getEmplId(),aux,fechaasistencia,"S");
						query.executeUpdate();
						FacesMessages.instance().add("Proceso realizado...Modificado asistencia");
					}
				}
				
			
			
		
	}
	
	public void cambiarAsistencia(Long iddetalle){
		
		
	}
		
	

	public String eliminarPlanificacion() {

		String estado = null;
		estado = verificar();

		if (estado != null) {
			List<Calendar> fechas = new ArrayList<Calendar>();		
			
			if (getListaFechasLaborables().size() != 0){
				fechas.clear();
				fechas = this.recorrerFechas(listaFechasLaborables);
				
				asistenciaHome.crearListaEliminarAsistencia(empleados, fechas,
						detalleHorarios, Constantes.ASISTENCIA_FALTA);
				
				asistenciaHome.crearListaEliminarAsistencia(empleados, fechas,
						detalleHorarios, Constantes.ASISTENCIA_LIBRE);
				
				asistenciaHome.crearListaEliminarAsistencia(empleados, fechas,
						detalleHorarios, Constantes.ASISTENCIA_FERIADO);
			}

			this.borraListas();
			FacesMessages.instance().add("Proceso realizado satisfactoriamente");
		} else  {
			FacesMessages.instance().add(
					"Proceso no realizado, verifique selección de datos");
		}
		
		return estado;
	}
	

	// Lanzar este proceso luego de planificar
	public String cargarTimbres(){
		
		String estado = null;
		estado = verificar();

		if (estado != null) {
			List<Calendar> fechas = new ArrayList<Calendar>();
			
			if (getListaFechasLaborables().size() != 0){
				fechas.clear();
				fechas = this.recorrerFechas(listaFechasLaborables);
				
				timbreHome.crearListaCargarTimbre(empleados, fechas,
						detalleHorarios, Constantes.ASISTENCIA_FALTA);
			}

			if (getListaFechasLibres().size() != 0){
				fechas.clear();
				fechas = this.recorrerFechas(listaFechasLibres);
				
				timbreHome.crearListaCargarTimbre(empleados, fechas,
						detalleHorarios, Constantes.ASISTENCIA_LIBRE);
			}
			
			if(getListaFechasFeriados().size() != 0){
				fechas.clear();
				fechas = this.recorrerFechas(listaFechasFeriados);
				
				timbreHome.crearListaCargarTimbre(empleados, fechas,
						detalleHorarios, Constantes.ASISTENCIA_FERIADO);
			}

			this.borraListas();
			FacesMessages.instance().add("Proceso realizado satisfactoriamente");
			
		} else {
			FacesMessages.instance().add("Proceso no realizado, verifique selección de datos");
		}
		
		return estado;
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
	
	public Horario getHorario1() {
		if (horario1 == null) {
			horario1 = new Horario();
		}
		return horario1;
	}

	public void setHorario1(Horario horario1) {
		this.horario1 = horario1;
	}
	
	public Horario getHorario2() {
		if (horario2 == null) {
			horario2 = new Horario();
		}
		return horario2;
	}

	public void setHorario2(Horario horario2) {
		this.horario2 = horario2;
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

	public List<Horario> getHorarios() {
			
		if (horarios == null) {
			horarios = new ArrayList<Horario>();
		}
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public Horario getHorarioseleccionado() {
		if (Horarioseleccionado == null) {
			Horarioseleccionado = new Horario();
		}
		return Horarioseleccionado;
	}

	public void setHorarioseleccionado(Horario horarioseleccionado) {
		Horarioseleccionado = horarioseleccionado;
	}

//	public List<Empleado> getlisempleaselect() {
//		if (lisempleaselect == null) {
//			lisempleaselect = new ArrayList<Empleado>();
//		}
//		return lisempleaselect;
//	}
//
//	public void setlisempleaselect(List<Empleado> lisempleaselect) {
//		this.lisempleaselect = lisempleaselect;
//	}
	
	public Empleado getempleadoselect() {
		if (empleadoselect == null) {
			empleadoselect = new Empleado();
		}
		return empleadoselect;
	}

	public void setempleadoselect(Empleado empleadoselect) {
		this.empleadoselect = empleadoselect;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public List<com.casapazmino.fulltime.model.Planificacion> getListPlanificacion() {
		return listPlanificacion;
	}

	public void setListPlanificacion(List<com.casapazmino.fulltime.model.Planificacion> listPlanificacion) {
		this.listPlanificacion = listPlanificacion;
	}

	public List<Asistencia> getAsistenciaselect() {
		return asistenciaselect;
	}

	public void setAsistenciaselect(List<Asistencia> asistenciaselect) {
		this.asistenciaselect = asistenciaselect;
	}

	public List<Asistencia> getAsistenciaAdmin() {
		return asistenciaAdmin;
	}

	public void setAsistenciaAdmin(List<Asistencia> asistenciaAdmin) {
		this.asistenciaAdmin = asistenciaAdmin;
	}

	
	
	

	
	
}