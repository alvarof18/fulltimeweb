package com.casapazmino.fulltime.procesos;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Synchronized;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.AsistenciaHome;
import com.casapazmino.fulltime.action.AsistenciaList;
import com.casapazmino.fulltime.action.HorarioHome;
import com.casapazmino.fulltime.action.HorarioList;
import com.casapazmino.fulltime.action.PermisoList;
import com.casapazmino.fulltime.action.PlanificacionHome;
import com.casapazmino.fulltime.action.PlanificacionList;
import com.casapazmino.fulltime.action.SolicitudVacacionList;
import com.casapazmino.fulltime.action.TimbreHome;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.Planificacion;
import com.casapazmino.fulltime.model.SolicitudVacacion;


@Name("planificacionMultiple")
@Scope(ScopeType.SESSION)
@Synchronized(timeout = 1800000 )
public class PlanificacionMultipleBean implements Serializable, PlanificacionMultiple {

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
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	@In(create = true)
	HorarioList horarioList;

	@Logger
	Log log;
	
	private Empleado empleado;
	private Empleado empleadoselect;
	private Horario horario1;
	private Horario horario2;
	private Horario Horarioseleccionado;
	private List<DetalleHorario> detalleHorarios;

	private List<Empleado> empleados;
	
	private List<Asistencia> asistencias;
	private List<Horario> horarios;
	private List<com.casapazmino.fulltime.model.Planificacion> listPlanificacion;

	private String tipoFecha;
	private String tipoReporte;
	private Integer year;
	private Integer mes;
	
	private Date fechaSeleccionada;
	private Date fechaDesde;
	private Date fechaHasta;
	private TreeSet<Date> listaFechasLaborables;
	private TreeSet<Date> listaFechasLibres;
	private TreeSet<Date> listaFechasFeriados;
	private boolean hora1;
	private boolean hora2;
	private boolean hora3;
	private boolean hora4;
	private boolean hora5;
	private boolean hora6;
	private boolean hora7;
	private boolean hora8;
	private boolean hora9;
	private boolean hora10;
	private boolean hora11;
	private boolean hora12;
	private boolean hora13;
	private boolean hora14;
	private boolean hora15;
	private boolean hora16;
	private boolean hora17;
	private boolean hora18;
	private boolean hora19;
	private boolean hora20;
	private boolean hora21;
	private boolean hora22;
	private boolean hora23;
	private boolean hora24;
	private boolean hora25;
	private boolean hora26;
	private boolean hora27;
	private boolean hora28;
	private boolean hora29;
	private boolean hora30;
	private boolean hora31;
	
	
	
	// SE UTILIZAN PARA MOSTRAR LAS LISTAS EN LA PAGINAS
	private List dataListFechasLaborables;
	private List dataListFechasLibres;
	private List dataListFechasFeriados;

	private ArrayList<Long> cargos;
	private ArrayList<Long> ciudades;
	private ArrayList<Long> detalleGrupoContratados;	
	private ArrayList<Long> departamentos;
	
	private int fila;
	
	//new local variables
	private List<Integer> listaDiasMes;
	private String cadenaAnterior;/*crear en todas*/
	private String cadenaActual;/*crear en todas*/
	private List<com.casapazmino.fulltime.model.Planificacion> listPlanificacionRotativo;

	private Boolean access;
	
	//end local variables
	
	public PlanificacionMultipleBean() {
		this.tipoReporte = "Cargo";
		this.tipoFecha = "Laborable";
		//this.access=gestionPermisoVacacion.buscarModificarPlanificacion();
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
	public List<Asistencia> BuscarAsistencia(Date fechaHorario) {

		return (List<Asistencia>) entityManager
				.createQuery(
						"select a from Asistencia a where  " + ControlBaseDatos.modificadorConvertirFecha + "a.fechaHoraHorario)=:fecha and a.estado = :estado")
				.setParameter("fecha", fechaHorario)
				.setParameter("estado",Constantes.ASISTENCIA_FALTA)
				.getResultList();

	}
	
	public boolean valida(int num){
		
		Calendar calendario=Calendar.getInstance();
		Calendar calendario2=Calendar.getInstance();
		
				
		calendario.setTime(fechaDesde);
		calendario2.setTime(fechaHasta);
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
				this.getEmpleados().clear();
				this.getEmpleados().addAll(buscarEmpleadosCargo());				
				ret = "ok";
			} else if (getTipoReporte().equals("Departamento")){
				this.getEmpleados().clear();
				this.getEmpleados().addAll ( buscarEmpleadosDepartamento());
				ret = "ok";
			} else if (getTipoReporte().equals("Grupo")) {
				this.getEmpleados().clear();
				this.getEmpleados().addAll ( buscarEmpleadosGrupo());
				ret = "ok";
			} else if (getTipoReporte().equals("Empleado")) {
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
		listPlanificacionRotativo=null;
		listPlanificacion=null;
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
	
	
	//new method
	public void eliminarAsistenciaMultiple() {
		log.info("//////////////////Eliminar Asistencia Multiple");
	
		String dia="0";
		String formato="yyyy-MM-dd";
		Calendar aux=Calendar.getInstance();
		
		String fecha=null;
	
		for(Planificacion plani: listPlanificacion){
			for(int i=0;i<=31;i++){
				if(i<10){
					dia="0"+i;
				}else{
					dia=""+i;
				}								
				fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
				fechaSeleccionada=transformaFecha(fecha, formato);
				aux.setTime(fechaSeleccionada);
				asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
			}						
		}
	}
	//
	
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
		
		//new changes
		setFechaDesde(null);
		setFechaHasta(null);
		//
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
	
	
	

	
//	public void anadirViejo(){
//		verificar();
//		String estado = null;
//		estado = verificar();
//
//		if (estado != null) {
//			List<Calendar> fechas = new ArrayList<Calendar>();
//			
//			if (getListaFechasLaborables().size() != 0){
//				//fechas.clear();
//				
//				fechas = this.recorrerFechas(listaFechasLaborables);
//				
//				asistencias.addAll(asistenciaHome.crearListaInsertarAsistenciaMultiple(lisempleaselect, fechas,
//						detalleHorarios, Constantes.ASISTENCIA_FALTA));
//				lisempleaselect.clear();
//				fechas.clear();
//				getListaFechasLaborables().clear();
//				getDataListFechasLaborables().clear();
//				detalleHorarios.clear();
//				
//			}
//
//			if (getListaFechasLibres().size() != 0){
//				
//				fechas = this.recorrerFechas(listaFechasLibres);
//				
//				asistencias.addAll(asistenciaHome.crearListaInsertarAsistenciaMultiple(lisempleaselect, fechas,
//						detalleHorarios, Constantes.ASISTENCIA_LIBRE));
//				lisempleaselect.clear();
//				fechas.clear();
//				getListaFechasLibres().clear();
//				getDataListFechasLibres().clear();
//				detalleHorarios.clear();
//			}
//			
//			if(getListaFechasFeriados().size() != 0){
//				
//				fechas = this.recorrerFechas(listaFechasFeriados);
//				
//				asistencias.addAll(asistenciaHome.crearListaInsertarAsistenciaMultiple(lisempleaselect, fechas,
//						detalleHorarios, Constantes.ASISTENCIA_FERIADO));
//				lisempleaselect.clear();
//				fechas.clear();
//				getListaFechasFeriados().clear();
//				getDataListFechasFeriados().clear();
//				detalleHorarios.clear();
//			}
//
//			//this.borraListas();
//			FacesMessages.instance().add("Proceso realizado satisfactoriamente");
//			
//		} else {
//			FacesMessages.instance().add("Proceso no realizado, verifique selección de datos");
//		}		
//	}
	
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
	
	public void construirAsistencia(){
		try{

			log.info("//////////////////Procesar////////////////////////////////////");
			
			this.access=gestionPermisoVacacion.buscarModificarPlanificacion();
			
			//Boolean access=gestionPermisoVacacion.buscarModificarPlanificacion();
			String dia="0";
			String formato="yyyy-MM-dd";
			Calendar aux=Calendar.getInstance();
			
			String fecha=null;
			
			List <Planificacion> p=new ArrayList<Planificacion>();
			
			if(listPlanificacion.isEmpty()){
				FacesMessages.instance().clear();
				FacesMessages.instance().add("No se puedo crear la planificación!");
			}else{
			
				guardar(1);	
				for(Planificacion plani: listPlanificacion){
				
					
					//update state
					//pf.setInstance(plani);
					//pf.getInstance().setEstado(1);
					//pf.update();
					//
					
					if((plani.getEstado()==0) ||(plani.getEstado()==1 && this.access)){
					
					if(plani.getHorarioByHora1Id()!=null){
						
						
						dia="01";				
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
						
						fechaSeleccionada=transformaFecha(fecha, formato);
						
						//this.horarioHome.setId(plani.getHorarioByHora1Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora1Id());
						
						aux.setTime(fechaSeleccionada);		
					
						String estado=plani.getHorarioByHora1Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
							
						}else if (estado.equalsIgnoreCase("Libre")){
							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
							
						}else if(estado.equalsIgnoreCase("Feriado")){
							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);							
						}else if(estado.equalsIgnoreCase("Vacaciones")){
							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){
							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}
					}else{	
						//log.info("HORARIO1-------------------------"+plani.getHorarioByHora1Id().getPlanificacionsForHora1Id());
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						
						if(1>=fechaDesde.getDate()&&1<=fechaHasta.getDate()){
							dia="01";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}
						if(plani.getHorarioByHora2Id()!=null){
						
						dia="02";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora2Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora2Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora2Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}
						else{	
							//eliminar asistencia cuando se selecciona campo nulo en combobox
							if(2>=fechaDesde.getDate()&&2<=fechaHasta.getDate()){
								dia="02";				
								fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
								fechaSeleccionada=transformaFecha(fecha, formato);
								aux.setTime(fechaSeleccionada);
								asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
							}							
							//
						}
					if(plani.getHorarioByHora3Id()!=null){
						dia="03";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora3Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora3Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora3Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(3>=fechaDesde.getDate()&&3<=fechaHasta.getDate()){
							dia="03";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}					
						//
					}if(plani.getHorarioByHora4Id()!=null){
						
						dia="04";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora4Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora4Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora4Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(4>=fechaDesde.getDate()&&4<=fechaHasta.getDate()){
							dia="04";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora5Id()!=null){						
						dia="05";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora5Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora5Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora5Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(5>=fechaDesde.getDate()&&5<=fechaHasta.getDate()){
							dia="05";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}
					if(plani.getHorarioByHora6Id()!=null){
						
						dia="06";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora6Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora6Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora6Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(6>=fechaDesde.getDate()&&6<=fechaHasta.getDate()){
							dia="06";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora7Id()!=null){
						
						dia="07";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora7Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora7Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora7Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(7>=fechaDesde.getDate()&&7<=fechaHasta.getDate()){
							dia="07";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora8Id()!=null){
						
						dia="08";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora8Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora8Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora8Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(8>=fechaDesde.getDate()&&8<=fechaHasta.getDate()){
							dia="08";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora9Id()!=null){
						dia="09";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora9Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora9Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora9Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(9>=fechaDesde.getDate()&&9<=fechaHasta.getDate()){
							dia="09";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora10Id()!=null){
						dia="10";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora10Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora10Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora10Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(10>=fechaDesde.getDate()&&10<=fechaHasta.getDate()){
							dia="10";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora11Id()!=null){
						dia="11";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora11Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora11Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora11Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(11>=fechaDesde.getDate()&&11<=fechaHasta.getDate()){
							dia="11";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora12Id()!=null){
						dia="12";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora12Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora12Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora12Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(12>=fechaDesde.getDate()&&12<=fechaHasta.getDate()){
							dia="12";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);	
						}						
						//
					}if(plani.getHorarioByHora13Id()!=null){
						dia="13";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora13Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora13Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora13Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(13>=fechaDesde.getDate()&&13<=fechaHasta.getDate()){
							dia="13";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora14Id()!=null){
						dia="14";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora14Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora14Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora14Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(14>=fechaDesde.getDate()&&14<=fechaHasta.getDate()){
							dia="14";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora15Id()!=null){
						dia="15";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora15Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora15Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora15Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(15>=fechaDesde.getDate()&&15<=fechaHasta.getDate()){
							dia="15";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora16Id()!=null){
						dia="16";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora16Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora16Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora16Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(16>=fechaDesde.getDate()&&16<=fechaHasta.getDate()){
							dia="16";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);	
						}						
						//
					}if(plani.getHorarioByHora17Id()!=null){
						dia="17";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora17Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora17Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora17Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(17>=fechaDesde.getDate()&&17<=fechaHasta.getDate()){
							dia="17";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora18Id()!=null){
						dia="18";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora18Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora18Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora18Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(18>=fechaDesde.getDate()&&18<=fechaHasta.getDate()){
							dia="18";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora19Id()!=null){
						dia="19";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora19Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora19Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora19Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(19>=fechaDesde.getDate()&&19<=fechaHasta.getDate()){
							dia="19";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora20Id()!=null){
						dia="20";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora20Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora20Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora20Id().getEstado();						
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}
					else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(20>=fechaDesde.getDate()&&20<=fechaHasta.getDate()){
							dia="20";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);	
						}						
						//
					}if(plani.getHorarioByHora21Id()!=null){
						dia="21";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora21Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora21Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora21Id().getEstado();	
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(21>=fechaDesde.getDate()&&21<=fechaHasta.getDate()){
							dia="21";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);	
						}						
						//
					}if(plani.getHorarioByHora22Id()!=null){
						dia="22";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora22Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora22Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora22Id().getEstado();	
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(22>=fechaDesde.getDate()&&22<=fechaHasta.getDate()){
							dia="22";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora23Id()!=null){
						dia="23";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora23Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora23Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora23Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(23>=fechaDesde.getDate()&&23<=fechaHasta.getDate()){
							dia="23";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora24Id()!=null){
						dia="24";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora24Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora24Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora24Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(24>=fechaDesde.getDate()&&24<=fechaHasta.getDate()){
							dia="24";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora25Id()!=null){
						dia="25";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora25Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora25Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora25Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(25>=fechaDesde.getDate()&&25<=fechaHasta.getDate()){
							dia="25";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora26Id()!=null){
						dia="26";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora26Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora26Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora26Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(26>=fechaDesde.getDate()&&26<=fechaHasta.getDate()){
							dia="26";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora27Id()!=null){
						dia="27";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora27Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora27Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora27Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(27>=fechaDesde.getDate()&&27<=fechaHasta.getDate()){
							dia="27";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora28Id()!=null){						
						dia="28";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora28Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora28Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora28Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(28>=fechaDesde.getDate()&&28<=fechaHasta.getDate()){
							dia="28";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora29Id()!=null){
						dia="29";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora29Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora29Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora29Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(29>=fechaDesde.getDate()&&29<=fechaHasta.getDate()){
							dia="29";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora30Id()!=null){
						dia="30";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora30Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora30Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora30Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}	
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(30>=fechaDesde.getDate()&&30<=fechaHasta.getDate()){
							dia="30";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}if(plani.getHorarioByHora31Id()!=null){
						dia="31";
						fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;
						fechaSeleccionada=transformaFecha(fecha, formato);
						//this.horarioHome.setId(plani.getHorarioByHora31Id());
						//Horarioseleccionado=this.horarioHome.find();
						detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora31Id());
						aux.setTime(fechaSeleccionada);
						
						String estado=plani.getHorarioByHora31Id().getEstado();
						
						if(estado.equalsIgnoreCase("Laborable")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FALTA);
						}else if (estado.equalsIgnoreCase("Libre")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_LIBRE);
						}else if(estado.equalsIgnoreCase("Feriado")){
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_FERIADO);
						}else if(estado.equalsIgnoreCase("Vacaciones")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
						}else if(estado.equalsIgnoreCase("Permiso")){							
							asistenciaHome.insertaAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux, detalleHorarios, Constantes.ASISTENCIA_PERMISO);
						}				
					}else{	
						//eliminar asistencia cuando se selecciona campo nulo en combobox
						if(31>=fechaDesde.getDate()&&31<=fechaHasta.getDate()){
							dia="31";				
							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
							fechaSeleccionada=transformaFecha(fecha, formato);
							aux.setTime(fechaSeleccionada);
							asistenciaHome.eliminarAsistenciaMultiple(plani.getEmpleadoByEmplId(), aux);
						}						
						//
					}
								
				}else{
					log.info("...................................................No se proceso por privilegios");
				}
			
				}
			}
			
			//guardar(1);	
			
			//listPlanificacion=this.buscarPlanificacion();
			FacesMessages.instance().clear();
			FacesMessages.instance().add("Datos procesados satisfactoriamente");
			//
		
		}catch(Exception e){
			FacesMessages.instance().clear();
			FacesMessages.instance().add("No se ha creado registros!");
		}
	}	
	
	//new Method
	public void CargarTimbres(){
		log.info("//////////////////CARGAR TIMBRES////////////////////////////////////");		
		this.access=gestionPermisoVacacion.buscarModificarPlanificacion();		

		String dia="0";
		String formato="yyyy-MM-dd";
		Calendar aux=Calendar.getInstance();//		
		String fecha=null;
		
		List<Empleado> empleados=new ArrayList<Empleado>();
		List<Calendar> fechas = new ArrayList<Calendar>();		
		List<DetalleHorario> detalleHorarios=new ArrayList<DetalleHorario>();
		String estado="";
		
		List <Planificacion> p=new ArrayList<Planificacion>();
	
		if(listPlanificacion==null){
			FacesMessages.instance().clear();
			FacesMessages.instance().add("No se ha realizado el proceso!");
		}else{	
		
			for(Planificacion plani: listPlanificacion){

				empleados.clear();
				fechas.clear();
					
				if(plani.getHorarioByHora1Id()!=null){					
					dia="01";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora2Id()!=null){					
					dia="02";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora3Id()!=null){					
					dia="03";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora4Id()!=null){					
					dia="04";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora5Id()!=null){					
					dia="05";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora6Id()!=null){					
					dia="06";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora7Id()!=null){					
					dia="07";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora8Id()!=null){					
					dia="08";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora9Id()!=null){					
					dia="09";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora10Id()!=null){					
					dia="10";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora11Id()!=null){					
					dia="11";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora12Id()!=null){					
					dia="12";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora13Id()!=null){					
					dia="13";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora14Id()!=null){					
					dia="14";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora15Id()!=null){					
					dia="15";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora16Id()!=null){					
					dia="16";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora17Id()!=null){					
					dia="17";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora17Id()!=null){					
					dia="17";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora18Id()!=null){					
					dia="18";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora19Id()!=null){					
					dia="19";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora20Id()!=null){					
					dia="20";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora21Id()!=null){					
					dia="21";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora22Id()!=null){					
					dia="22";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora23Id()!=null){					
					dia="23";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora24Id()!=null){					
					dia="24";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora25Id()!=null){					
					dia="25";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora26Id()!=null){					
					dia="26";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora27Id()!=null){					
					dia="27";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora28Id()!=null){					
					dia="28";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora29Id()!=null){					
					dia="29";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora30Id()!=null){					
					dia="30";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				if(plani.getHorarioByHora31Id()!=null){					
					dia="31";				
					fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+dia;	
					fechaSeleccionada=transformaFecha(fecha, formato);		
					aux.setTime(fechaSeleccionada);	
					fechas.add(aux);
					}
				
				empleados.add(plani.getEmpleadoByEmplId());
				//cargar Timbres
				timbreHome.crearListaCargarTimbre(empleados, fechas,detalleHorarios, estado);		
				//		
				}
			FacesMessages.instance().clear();
			FacesMessages.instance().add("Timbres cargados satisfactoriamente");
			}
				
		//listPlanificacion=this.buscarPlanificacion();
	
		//
	
	}
	
		
	public String crearPlanificacion() {		
		
		listPlanificacion=this.buscarPlanificacion();
		
		if(listPlanificacion!=null){
			if(listPlanificacion.size()==0){
				this.buscarDatos();
				listPlanificacion=planificacionHome.crearListaPlanificacion(empleados, year, mes);
				//FacesMessages.instance().add("Proceso realizado satisfactoriamente");
					if (getTipoReporte().equals("Departamento")||getTipoReporte().equals("Cargo")||getTipoReporte().equals("Grupo")) {						
						this.getEmpleados().clear();	
					}
			}
			else{
				//listPlanificacion=this.buscarPlanificacion();
			}
			
		}else{
			FacesMessages.instance().add("No se ha creado ningun registro");
		}
		
		return "ok";	
			
	}	
	
	
	//new verification of planification

	public List<Planificacion> verificaPlanificacion(){
		
		Calendar cal=Calendar.getInstance();
		Calendar cale=Calendar.getInstance();
				
		cal.setTime(fechaDesde);
		year=cal.get(Calendar.YEAR);
		mes=fechaDesde.getMonth()+1;
		
		cale.setTime(fechaHasta);
		int meshasta=fechaHasta.getMonth()+1;
		int yearHasta=cale.get(Calendar.YEAR);
		
		this.buscarDatos();
				
		if(mes==meshasta&&year==yearHasta){
			if(fechaDesde.getDate()<=fechaHasta.getDate()){
				try{	
					listPlanificacion = this.NuevaBusquedaPlanificacion(empleados, mes, year);
					
					vaca=horarioAlterno("V");
					perm=horarioAlterno("P");
					listPlanificacion=planificacionIndividual(listPlanificacion);
					
					FacesMessages.instance().add("Proceso realizado satisfactoriamente");
					
				}
				catch (Exception e2) {
					FacesMessages.instance().add(
							"Error Busqueda Planificacion - Generando planificación ");
					e2.printStackTrace();
				}
			}else{
				FacesMessages.instance().add(
						"El día de Fecha Desde no debe ser mayor que el día de Fecha Hasta");
						listPlanificacion=null;
			}
		}
		else{
			FacesMessages.instance().add(
					"Las fechas deben ser del mismo mes y año");
					listPlanificacion=null;
		}
		
		return listPlanificacion;
	}
	
	
	//end new

	
	//new method for planification per hour
	public String crearPlanificacionRota() {
		try{
			
			log.info("//////////////////para los rotativos////////////////////////////////////");
			
			listPlanificacionRotativo=this.buscarPlanificacion2();
			
			log.info("TAMAÑO 1...............listPlanificacionRotativo............................................:"+listPlanificacionRotativo.size());
			log.info("TAMAÑO 2...............listPlanificacion............................................:"+listPlanificacion.size());
			
			if(listPlanificacionRotativo.size()==0){
				
				this.buscarDatos();
				
				log.info("entro a crear planificacion");
				
				int mes_valido=mes;
				int year_valido=year;
				
				if(mes==13){
					year_valido=year_valido+1;
					mes_valido=1;
				}
				
				listPlanificacionRotativo = planificacionHome.crearListaPlanificacion(empleados, year_valido, mes_valido);

				
				for(Planificacion planif : listPlanificacionRotativo)
				{
					log.info("*************************************Rotado: " + planif.getEmpleadoByEmplId().getEmplId());
					for(Planificacion planif2 : listPlanificacion)
					{
						log.info("*************************************Anterior: " + planif2.getEmpleadoByEmplId().getEmplId());
						if(planif.getEmpleadoByEmplId().getEmplId().intValue() == planif2.getEmpleadoByEmplId().getEmplId().intValue())
						{
							log.info("**************************************Entro*****************************");
							log.info("**************************************Entro*****************************");
							log.info("**************Empleado: "+planif.getEmpleadoByEmplId().getNombre());
							if(planif.getHorarioByHora1Id() != null)
							log.info("**************Planificaion: "+planif.getHorarioByHora1Id().getDescripcion());
							
							////////////////
							Calendar cal=Calendar.getInstance();
							
							int messig=mes-2;
							
							Vector <Horario>ORDER=new Vector<Horario>();
							int order=1;						
							Vector <Horario> LUNES=new Vector<Horario>();
							int lunes=0;
							Vector <Horario>MARTES=new Vector<Horario>();
							int martes=0;
							Vector <Horario>MIERCOLES=new Vector<Horario>();
							int miercoles=0;
							Vector <Horario>JUEVES=new Vector<Horario>();
							int jueves=0;
							Vector <Horario>VIERNES=new Vector<Horario>();
							int viernes=0;
							Vector <Horario>SABADO=new Vector<Horario>();
							int sabado=0;
							Vector <Horario>DOMINGO=new Vector<Horario>();
							int domingo=0;
							
							for(int i=1;i<=31;i++){
								cal.set(year, messig,i);
								if(cal.getTime().getMonth()==messig){
									String time=""+cal.getTime();
									time=time.substring(0, 3);
									
										if(time.equals("Mon")){
											switch(order){
												case 1: LUNES.add(planif2.getHorarioByHora1Id());
												break;
												case 2: LUNES.add(planif2.getHorarioByHora2Id());
												break;
												case 3: LUNES.add(planif2.getHorarioByHora3Id());
												break;
												case 4: LUNES.add(planif2.getHorarioByHora4Id());
												break;
												case 5: LUNES.add(planif2.getHorarioByHora5Id());
												break;
												case 6: LUNES.add(planif2.getHorarioByHora6Id());
												break;
												case 7: LUNES.add(planif2.getHorarioByHora7Id());
												break;
												case 8: LUNES.add(planif2.getHorarioByHora8Id());
												break;
												case 9: LUNES.add(planif2.getHorarioByHora9Id());
												break;
												case 10: LUNES.add(planif2.getHorarioByHora10Id());
												break;
												case 11: LUNES.add(planif2.getHorarioByHora11Id());
												break;
												case 12: LUNES.add(planif2.getHorarioByHora12Id());
												break;
												case 13: LUNES.add(planif2.getHorarioByHora13Id());
												break;
												case 14: LUNES.add(planif2.getHorarioByHora14Id());
												break;
												case 15: LUNES.add(planif2.getHorarioByHora15Id());
												break;
												case 16: LUNES.add(planif2.getHorarioByHora16Id());
												break;
												case 17: LUNES.add(planif2.getHorarioByHora17Id());
												break;
												case 18: LUNES.add(planif2.getHorarioByHora18Id());
												break;
												case 19: LUNES.add(planif2.getHorarioByHora19Id());
												break;
												case 20: LUNES.add(planif2.getHorarioByHora20Id());
												break;
												case 21: LUNES.add(planif2.getHorarioByHora21Id());
												break;
												case 22: LUNES.add(planif2.getHorarioByHora22Id());
												break;
												case 23: LUNES.add(planif2.getHorarioByHora23Id());
												break;
												case 24: LUNES.add(planif2.getHorarioByHora24Id());
												break;
												case 25: LUNES.add(planif2.getHorarioByHora25Id());
												break;
												case 26: LUNES.add(planif2.getHorarioByHora26Id());
												break;
												case 27: LUNES.add(planif2.getHorarioByHora27Id());
												break;
												case 28: LUNES.add(planif2.getHorarioByHora28Id());
												break;
												case 29: LUNES.add(planif2.getHorarioByHora29Id());
												break;
												case 30: LUNES.add(planif2.getHorarioByHora30Id());
												break;
												case 31: LUNES.add(planif2.getHorarioByHora31Id());
												break;
											}
											order++;
										}else if(time.equals("Tue")){
											switch(order){
												case 1: MARTES.add(planif2.getHorarioByHora1Id());
												break;
												case 2: MARTES.add(planif2.getHorarioByHora2Id());
												break;
												case 3: MARTES.add(planif2.getHorarioByHora3Id());
												break;
												case 4: MARTES.add(planif2.getHorarioByHora4Id());
												break;
												case 5: MARTES.add(planif2.getHorarioByHora5Id());
												break;
												case 6: MARTES.add(planif2.getHorarioByHora6Id());
												break;
												case 7: MARTES.add(planif2.getHorarioByHora7Id());
												break;
												case 8: MARTES.add(planif2.getHorarioByHora8Id());
												break;
												case 9: MARTES.add(planif2.getHorarioByHora9Id());
												break;
												case 10: MARTES.add(planif2.getHorarioByHora10Id());
												break;
												case 11: MARTES.add(planif2.getHorarioByHora11Id());
												break;
												case 12: MARTES.add(planif2.getHorarioByHora12Id());
												break;
												case 13: MARTES.add(planif2.getHorarioByHora13Id());
												break;
												case 14: MARTES.add(planif2.getHorarioByHora14Id());
												break;
												case 15: MARTES.add(planif2.getHorarioByHora15Id());
												break;
												case 16: MARTES.add(planif2.getHorarioByHora16Id());
												break;
												case 17: MARTES.add(planif2.getHorarioByHora17Id());
												break;
												case 18: MARTES.add(planif2.getHorarioByHora18Id());
												break;
												case 19: MARTES.add(planif2.getHorarioByHora19Id());
												break;
												case 20: MARTES.add(planif2.getHorarioByHora20Id());
												break;
												case 21: MARTES.add(planif2.getHorarioByHora21Id());
												break;
												case 22: MARTES.add(planif2.getHorarioByHora22Id());
												break;
												case 23: MARTES.add(planif2.getHorarioByHora23Id());
												break;
												case 24: MARTES.add(planif2.getHorarioByHora24Id());
												break;
												case 25: MARTES.add(planif2.getHorarioByHora25Id());
												break;
												case 26: MARTES.add(planif2.getHorarioByHora26Id());
												break;
												case 27: MARTES.add(planif2.getHorarioByHora27Id());
												break;
												case 28: MARTES.add(planif2.getHorarioByHora28Id());
												break;
												case 29: MARTES.add(planif2.getHorarioByHora29Id());
												break;
												case 30: MARTES.add(planif2.getHorarioByHora30Id());
												break;
												case 31: MARTES.add(planif2.getHorarioByHora31Id());
												break;
											}
											order++;									
										}else if(time.equals("Wed")){
												switch(order){
												case 1: MIERCOLES.add(planif2.getHorarioByHora1Id());
												break;
												case 2: MIERCOLES.add(planif2.getHorarioByHora2Id());
												break;
												case 3: MIERCOLES.add(planif2.getHorarioByHora3Id());
												break;
												case 4: MIERCOLES.add(planif2.getHorarioByHora4Id());
												break;
												case 5: MIERCOLES.add(planif2.getHorarioByHora5Id());
												break;
												case 6: MIERCOLES.add(planif2.getHorarioByHora6Id());
												break;
												case 7: MIERCOLES.add(planif2.getHorarioByHora7Id());
												break;
												case 8: MIERCOLES.add(planif2.getHorarioByHora8Id());
												break;
												case 9: MIERCOLES.add(planif2.getHorarioByHora9Id());
												break;
												case 10: MIERCOLES.add(planif2.getHorarioByHora10Id());
												break;
												case 11: MIERCOLES.add(planif2.getHorarioByHora11Id());
												break;
												case 12: MIERCOLES.add(planif2.getHorarioByHora12Id());
												break;
												case 13: MIERCOLES.add(planif2.getHorarioByHora13Id());
												break;
												case 14: MIERCOLES.add(planif2.getHorarioByHora14Id());
												break;
												case 15: MIERCOLES.add(planif2.getHorarioByHora15Id());
												break;
												case 16: MIERCOLES.add(planif2.getHorarioByHora16Id());
												break;
												case 17: MIERCOLES.add(planif2.getHorarioByHora17Id());
												break;
												case 18: MIERCOLES.add(planif2.getHorarioByHora18Id());
												break;
												case 19: MIERCOLES.add(planif2.getHorarioByHora19Id());
												break;
												case 20: MIERCOLES.add(planif2.getHorarioByHora20Id());
												break;
												case 21: MIERCOLES.add(planif2.getHorarioByHora21Id());
												break;
												case 22: MIERCOLES.add(planif2.getHorarioByHora22Id());
												break;
												case 23: MIERCOLES.add(planif2.getHorarioByHora23Id());
												break;
												case 24: MIERCOLES.add(planif2.getHorarioByHora24Id());
												break;
												case 25: MIERCOLES.add(planif2.getHorarioByHora25Id());
												break;
												case 26: MIERCOLES.add(planif2.getHorarioByHora26Id());
												break;
												case 27: MIERCOLES.add(planif2.getHorarioByHora27Id());
												break;
												case 28: MIERCOLES.add(planif2.getHorarioByHora28Id());
												break;
												case 29: MIERCOLES.add(planif2.getHorarioByHora29Id());
												break;
												case 30: MIERCOLES.add(planif2.getHorarioByHora30Id());
												break;
												case 31: MIERCOLES.add(planif2.getHorarioByHora31Id());
												break;
											}
											order++;															
										}else if(time.equals("Thu")){
												switch(order){
												case 1: JUEVES.add(planif2.getHorarioByHora1Id());
												break;
												case 2: JUEVES.add(planif2.getHorarioByHora2Id());
												break;
												case 3: JUEVES.add(planif2.getHorarioByHora3Id());
												break;
												case 4: JUEVES.add(planif2.getHorarioByHora4Id());
												break;
												case 5: JUEVES.add(planif2.getHorarioByHora5Id());
												break;
												case 6: JUEVES.add(planif2.getHorarioByHora6Id());
												break;
												case 7: JUEVES.add(planif2.getHorarioByHora7Id());
												break;
												case 8: JUEVES.add(planif2.getHorarioByHora8Id());
												break;
												case 9: JUEVES.add(planif2.getHorarioByHora9Id());
												break;
												case 10: JUEVES.add(planif2.getHorarioByHora10Id());
												break;
												case 11: JUEVES.add(planif2.getHorarioByHora11Id());
												break;
												case 12: JUEVES.add(planif2.getHorarioByHora12Id());
												break;
												case 13: JUEVES.add(planif2.getHorarioByHora13Id());
												break;
												case 14: JUEVES.add(planif2.getHorarioByHora14Id());
												break;
												case 15: JUEVES.add(planif2.getHorarioByHora15Id());
												break;
												case 16: JUEVES.add(planif2.getHorarioByHora16Id());
												break;
												case 17: JUEVES.add(planif2.getHorarioByHora17Id());
												break;
												case 18: JUEVES.add(planif2.getHorarioByHora18Id());
												break;
												case 19: JUEVES.add(planif2.getHorarioByHora19Id());
												break;
												case 20: JUEVES.add(planif2.getHorarioByHora20Id());
												break;
												case 21: JUEVES.add(planif2.getHorarioByHora21Id());
												break;
												case 22: JUEVES.add(planif2.getHorarioByHora22Id());
												break;
												case 23: JUEVES.add(planif2.getHorarioByHora23Id());
												break;
												case 24: JUEVES.add(planif2.getHorarioByHora24Id());
												break;
												case 25: JUEVES.add(planif2.getHorarioByHora25Id());
												break;
												case 26: JUEVES.add(planif2.getHorarioByHora26Id());
												break;
												case 27: JUEVES.add(planif2.getHorarioByHora27Id());
												break;
												case 28: JUEVES.add(planif2.getHorarioByHora28Id());
												break;
												case 29: JUEVES.add(planif2.getHorarioByHora29Id());
												break;
												case 30: JUEVES.add(planif2.getHorarioByHora30Id());
												break;
												case 31: JUEVES.add(planif2.getHorarioByHora31Id());
												break;
											}
											order++;					
										}else if(time.equals("Fri")){
												switch(order){
												case 1: VIERNES.add(planif2.getHorarioByHora1Id());
												break;
												case 2: VIERNES.add(planif2.getHorarioByHora2Id());
												break;
												case 3: VIERNES.add(planif2.getHorarioByHora3Id());
												break;
												case 4: VIERNES.add(planif2.getHorarioByHora4Id());
												break;
												case 5: VIERNES.add(planif2.getHorarioByHora5Id());
												break;
												case 6: VIERNES.add(planif2.getHorarioByHora6Id());
												break;
												case 7: VIERNES.add(planif2.getHorarioByHora7Id());
												break;
												case 8: VIERNES.add(planif2.getHorarioByHora8Id());
												break;
												case 9: VIERNES.add(planif2.getHorarioByHora9Id());
												break;
												case 10: VIERNES.add(planif2.getHorarioByHora10Id());
												break;
												case 11: VIERNES.add(planif2.getHorarioByHora11Id());
												break;
												case 12: VIERNES.add(planif2.getHorarioByHora12Id());
												break;
												case 13: VIERNES.add(planif2.getHorarioByHora13Id());
												break;
												case 14: VIERNES.add(planif2.getHorarioByHora14Id());
												break;
												case 15: VIERNES.add(planif2.getHorarioByHora15Id());
												break;
												case 16: VIERNES.add(planif2.getHorarioByHora16Id());
												break;
												case 17: VIERNES.add(planif2.getHorarioByHora17Id());
												break;
												case 18: VIERNES.add(planif2.getHorarioByHora18Id());
												break;
												case 19: VIERNES.add(planif2.getHorarioByHora19Id());
												break;
												case 20: VIERNES.add(planif2.getHorarioByHora20Id());
												break;
												case 21: VIERNES.add(planif2.getHorarioByHora21Id());
												break;
												case 22: VIERNES.add(planif2.getHorarioByHora22Id());
												break;
												case 23: VIERNES.add(planif2.getHorarioByHora23Id());
												break;
												case 24: VIERNES.add(planif2.getHorarioByHora24Id());
												break;
												case 25: VIERNES.add(planif2.getHorarioByHora25Id());
												break;
												case 26: VIERNES.add(planif2.getHorarioByHora26Id());
												break;
												case 27: VIERNES.add(planif2.getHorarioByHora27Id());
												break;
												case 28: VIERNES.add(planif2.getHorarioByHora28Id());
												break;
												case 29: VIERNES.add(planif2.getHorarioByHora29Id());
												break;
												case 30: VIERNES.add(planif2.getHorarioByHora30Id());
												break;
												case 31: VIERNES.add(planif2.getHorarioByHora31Id());
												break;
											}
											order++;					
										}else if(time.equals("Sat")){
												switch(order){
												case 1: SABADO.add(planif2.getHorarioByHora1Id());
												break;
												case 2: SABADO.add(planif2.getHorarioByHora2Id());
												break;
												case 3: SABADO.add(planif2.getHorarioByHora3Id());
												break;
												case 4: SABADO.add(planif2.getHorarioByHora4Id());
												break;
												case 5: SABADO.add(planif2.getHorarioByHora5Id());
												break;
												case 6: SABADO.add(planif2.getHorarioByHora6Id());
												break;
												case 7: SABADO.add(planif2.getHorarioByHora7Id());
												break;
												case 8: SABADO.add(planif2.getHorarioByHora8Id());
												break;
												case 9: SABADO.add(planif2.getHorarioByHora9Id());
												break;
												case 10: SABADO.add(planif2.getHorarioByHora10Id());
												break;
												case 11: SABADO.add(planif2.getHorarioByHora11Id());
												break;
												case 12: SABADO.add(planif2.getHorarioByHora12Id());
												break;
												case 13: SABADO.add(planif2.getHorarioByHora13Id());
												break;
												case 14: SABADO.add(planif2.getHorarioByHora14Id());
												break;
												case 15: SABADO.add(planif2.getHorarioByHora15Id());
												break;
												case 16: SABADO.add(planif2.getHorarioByHora16Id());
												break;
												case 17: SABADO.add(planif2.getHorarioByHora17Id());
												break;
												case 18: SABADO.add(planif2.getHorarioByHora18Id());
												break;
												case 19: SABADO.add(planif2.getHorarioByHora19Id());
												break;
												case 20: SABADO.add(planif2.getHorarioByHora20Id());
												break;
												case 21: SABADO.add(planif2.getHorarioByHora21Id());
												break;
												case 22: SABADO.add(planif2.getHorarioByHora22Id());
												break;
												case 23: SABADO.add(planif2.getHorarioByHora23Id());
												break;
												case 24: SABADO.add(planif2.getHorarioByHora24Id());
												break;
												case 25: SABADO.add(planif2.getHorarioByHora25Id());
												break;
												case 26: SABADO.add(planif2.getHorarioByHora26Id());
												break;
												case 27: SABADO.add(planif2.getHorarioByHora27Id());
												break;
												case 28: SABADO.add(planif2.getHorarioByHora28Id());
												break;
												case 29: SABADO.add(planif2.getHorarioByHora29Id());
												break;
												case 30: SABADO.add(planif2.getHorarioByHora30Id());
												break;
												case 31: SABADO.add(planif2.getHorarioByHora31Id());
												break;
											}
											order++;					
										}else if(time.equals("Sun")){
												switch(order){
												case 1: DOMINGO.add(planif2.getHorarioByHora1Id());
												break;
												case 2: DOMINGO.add(planif2.getHorarioByHora2Id());
												break;
												case 3: DOMINGO.add(planif2.getHorarioByHora3Id());
												break;
												case 4: DOMINGO.add(planif2.getHorarioByHora4Id());
												break;
												case 5: DOMINGO.add(planif2.getHorarioByHora5Id());
												break;
												case 6: DOMINGO.add(planif2.getHorarioByHora6Id());
												break;
												case 7: DOMINGO.add(planif2.getHorarioByHora7Id());
												break;
												case 8: DOMINGO.add(planif2.getHorarioByHora8Id());
												break;
												case 9: DOMINGO.add(planif2.getHorarioByHora9Id());
												break;
												case 10: DOMINGO.add(planif2.getHorarioByHora10Id());
												break;
												case 11: DOMINGO.add(planif2.getHorarioByHora11Id());
												break;
												case 12: DOMINGO.add(planif2.getHorarioByHora12Id());
												break;
												case 13: DOMINGO.add(planif2.getHorarioByHora13Id());
												break;
												case 14: DOMINGO.add(planif2.getHorarioByHora14Id());
												break;
												case 15: DOMINGO.add(planif2.getHorarioByHora15Id());
												break;
												case 16: DOMINGO.add(planif2.getHorarioByHora16Id());
												break;
												case 17: DOMINGO.add(planif2.getHorarioByHora17Id());
												break;
												case 18: DOMINGO.add(planif2.getHorarioByHora18Id());
												break;
												case 19: DOMINGO.add(planif2.getHorarioByHora19Id());
												break;
												case 20: DOMINGO.add(planif2.getHorarioByHora20Id());
												break;
												case 21: DOMINGO.add(planif2.getHorarioByHora21Id());
												break;
												case 22: DOMINGO.add(planif2.getHorarioByHora22Id());
												break;
												case 23: DOMINGO.add(planif2.getHorarioByHora23Id());
												break;
												case 24: DOMINGO.add(planif2.getHorarioByHora24Id());
												break;
												case 25: DOMINGO.add(planif2.getHorarioByHora25Id());
												break;
												case 26: DOMINGO.add(planif2.getHorarioByHora26Id());
												break;
												case 27: DOMINGO.add(planif2.getHorarioByHora27Id());
												break;
												case 28: DOMINGO.add(planif2.getHorarioByHora28Id());
												break;
												case 29: DOMINGO.add(planif2.getHorarioByHora29Id());
												break;
												case 30: DOMINGO.add(planif2.getHorarioByHora30Id());
												break;
												case 31: DOMINGO.add(planif2.getHorarioByHora31Id());
												break;
											}
											order++;					
										}							
										
										log.info(" DIA "+i+": "+cal.getTime());
								}else{
										log.info("NO DIA "+i+": "+cal.getTime());
								}						
							}
							
							log.info("NEXT MONTH!");
							messig=messig+1;
							if(messig==12){
								messig=0;
							}
							
							for(int i=1;i<=31;i++){
								cal.set(year_valido, messig,i);
								if(cal.getTime().getMonth()==messig){
									String time=""+cal.getTime();
									time=time.substring(0, 3);
									
										if(time.equals("Mon")){
											if(LUNES.size()==lunes){
												lunes=0;
											}
											ORDER.add(LUNES.elementAt(lunes));
											lunes++;
										}else if(time.equals("Tue")){
											if(MARTES.size()==martes){
												martes=0;
											}
											ORDER.add(MARTES.elementAt(martes));
											martes++;
										}else if(time.equals("Wed")){
											if(MIERCOLES.size()==miercoles){
												miercoles=0;
											}
											ORDER.add(MIERCOLES.elementAt(miercoles));
											miercoles++;
										}else if(time.equals("Thu")){
											if(JUEVES.size()==jueves){
												jueves=0;
											}
											ORDER.add(JUEVES.elementAt(jueves));
											jueves++;
										}else if(time.equals("Fri")){
											if(VIERNES.size()==viernes){
												viernes=0;
											}
											ORDER.add(VIERNES.elementAt(viernes));
											viernes++;
										}else if(time.equals("Sat")){
											if(SABADO.size()==sabado){
												sabado=0;
											}
											ORDER.add(SABADO.elementAt(sabado));
											sabado++;
										}else if(time.equals("Sun")){
											if(DOMINGO.size()==domingo){
												domingo=0;
											}
											ORDER.add(DOMINGO.elementAt(domingo));
											domingo++;
										}
										log.info(" DIA2 "+i+": "+cal.getTime());
								}else{
									log.info("NO DIA2 "+i+": "+cal.getTime());
								}
							}						
							
							order=ORDER.size();
							if(order<=30){
								for(int i=order;i<=30;i++){
									ORDER.add(null);
								}
							}					
							
							planif.setHorarioByHora1Id(ORDER.get(0));
							planif.setHorarioByHora2Id(ORDER.get(1));
							planif.setHorarioByHora3Id(ORDER.get(2));
							planif.setHorarioByHora4Id(ORDER.get(3));
							planif.setHorarioByHora5Id(ORDER.get(4));
							planif.setHorarioByHora6Id(ORDER.get(5));
							planif.setHorarioByHora7Id(ORDER.get(6));
							planif.setHorarioByHora8Id(ORDER.get(7));
							planif.setHorarioByHora9Id(ORDER.get(8));
							planif.setHorarioByHora10Id(ORDER.get(9));
							planif.setHorarioByHora11Id(ORDER.get(10));
							planif.setHorarioByHora12Id(ORDER.get(11));
							planif.setHorarioByHora13Id(ORDER.get(12));
							planif.setHorarioByHora14Id(ORDER.get(13));
							planif.setHorarioByHora15Id(ORDER.get(14));
							planif.setHorarioByHora16Id(ORDER.get(15));
							planif.setHorarioByHora17Id(ORDER.get(16));
							planif.setHorarioByHora18Id(ORDER.get(17));
							planif.setHorarioByHora19Id(ORDER.get(18));
							planif.setHorarioByHora20Id(ORDER.get(19));
							planif.setHorarioByHora21Id(ORDER.get(20));
							planif.setHorarioByHora22Id(ORDER.get(21));
							planif.setHorarioByHora23Id(ORDER.get(22));
							planif.setHorarioByHora24Id(ORDER.get(23));
							planif.setHorarioByHora25Id(ORDER.get(24));
							planif.setHorarioByHora26Id(ORDER.get(25));
							planif.setHorarioByHora27Id(ORDER.get(26));
							planif.setHorarioByHora28Id(ORDER.get(27));
							planif.setHorarioByHora29Id(ORDER.get(28));
							planif.setHorarioByHora30Id(ORDER.get(29));
							planif.setHorarioByHora31Id(ORDER.get(30));		
							planif.setEstado(0);
							
							//////////////

						}
					}
				}
				
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Se ha replicado la planificación correctamente!");
	}else{
		FacesMessages.instance().clear();
		FacesMessages.instance().add("Ya se encuentra repliacada la planificación!");
	}
				
				//listPlanificacion = listPlanificacionRotativo;
				//this.guardar();			
				
				

				if (getTipoReporte().equals("Departamento")||getTipoReporte().equals("Cargo")||getTipoReporte().equals("Grupo")) {						
					this.getEmpleados().clear();	
				}
			
			/*}
			else{
				listPlanificacion=this.buscarPlanificacion();
			}*/
			return "ok";				
		
		}catch(Exception e){
			listPlanificacion=null;
			FacesMessages.instance().clear();
			FacesMessages.instance().add("No se ha creado registros!");				
			return "no ok";
		}
	}
	//end method
	
	//new method for planification per hour
	
public List<Planificacion> buscarPlanificacion2(){
		
		Calendar cal=Calendar.getInstance();
		Calendar cale=Calendar.getInstance();		
		
		mes=fechaDesde.getMonth()+2;
		int meshasta=fechaHasta.getMonth()+2;
		
		fechaDesde.setYear(fechaDesde.getYear());		
		fechaHasta.setYear(fechaHasta.getYear());		
				
		cal.setTime(fechaDesde);
		year=cal.get(Calendar.YEAR);		
		
		cale.setTime(fechaHasta);		
		int yearHasta=cale.get(Calendar.YEAR);
				
		this.buscarDatos();
	
		log.info("mes fecha desde "+mes);
		log.info("year fecha desde "+year);
		log.info("mes fecha hasta "+meshasta);
		log.info("year fecha hasta "+yearHasta);
		
		
		//Este proceso esta de mas solo se deveria poner un combo con el mes y el anio o crear un proceso para poder crear de diferentes meses
		if(mes==meshasta&&year==yearHasta){
		
		try{		
			listPlanificacionRotativo = this.buscarPlanificacion(empleados, mes, year);
			log.info("Proceso realizado satisfactoriamente");
			//FacesMessages.instance().add("Proceso realizado satisfactoriamente");
			if (getTipoReporte().equals("Departamento")||getTipoReporte().equals("Cargo")||getTipoReporte().equals("Grupo")) {						
				this.getEmpleados().clear();	
			}
			
		}
		catch (Exception e2) {
			log.info("Error Busqueda Planificacion - Generando planificación "+e2);
			//FacesMessages.instance().add(
			//		"Error Busqueda Planificacion - Generando planificación ");
			//e2.printStackTrace();
		}
		}
		else{
			log.info("Las fechas deben ser del mismo mes y año ");
			//FacesMessages.instance().add(
			//		"Las fechas deben ser del mismo mes y año ");
		}
		return listPlanificacionRotativo;
	}
	
	//end method

	
	
	public List<Planificacion> buscarPlanificacion(){
		
		Calendar cal=Calendar.getInstance();
		Calendar cale=Calendar.getInstance();
				
		cal.setTime(fechaDesde);
		year=cal.get(Calendar.YEAR);
		mes=fechaDesde.getMonth()+1;
		
		cale.setTime(fechaHasta);
		int meshasta=fechaHasta.getMonth()+1;
		int yearHasta=cale.get(Calendar.YEAR);
		
		this.buscarDatos();
	
				
		if(mes==meshasta&&year==yearHasta){
			if(fechaDesde.getDate()<=fechaHasta.getDate()){
				try{			
					listPlanificacion = this.buscarPlanificacion(empleados, mes, year);
					//buscarAsistencia();
					FacesMessages.instance().add("Proceso realizado satisfactoriamente");
					
					if (getTipoReporte().equals("Departamento")||getTipoReporte().equals("Cargo")||getTipoReporte().equals("Grupo")) {						
						this.getEmpleados().clear();	
					}
					
				}
				catch (Exception e2) {
					FacesMessages.instance().add(
							"Error Busqueda Planificacion - Generando planificación ");
					e2.printStackTrace();
				}
			}else{
				FacesMessages.instance().add(
						"El día de Fecha Desde no debe ser mayor que el día de Fecha Hasta");
						listPlanificacion=null;
			}
		}
		else{
			FacesMessages.instance().add(
					"Las fechas deben ser del mismo mes y año");
					listPlanificacion=null;
		}
		return listPlanificacion;
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
	
	//new method
	
	public List<Planificacion> NuevaBusquedaPlanificacion(List<Empleado> empleados,Integer mes, Integer anio){		
		
		List<Planificacion> planificaciones =  new ArrayList<Planificacion>();
		
		PlanificacionList planificacionList = (PlanificacionList) Component.getInstance("planificacionList", true);		
		

		for (Empleado empleado : empleados) {						
			
			planificacionList.getPlanificacion().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
			
			planificacionList.getPlanificacion().setMes(mes);
			
			planificacionList.getPlanificacion().setAnio(anio);
			
			List<Planificacion> p = new ArrayList<Planificacion>();
			
			p=planificacionList.getListaPlanificacion();
			
			//log.info("P.SIZE()..."+empleado.getApellido()+"-----------------------------------------------..............................:"+p.size());
			if(p.size()==0){
				List <Empleado> aux=new ArrayList<Empleado>();
				aux.add(empleado);				
				planificaciones.addAll(planificacionHome.VerificarListaPlanificacion(aux, anio, mes));
			}else{
				planificaciones.addAll(p);	
			}			
		}
		 
		return planificaciones;
	}
	
	//end new
	
	public List<Planificacion> buscarPlanificacionCreada(List<Empleado> empleados,Integer mes, Integer anio){		
		
		List<Planificacion> planificaciones =  new ArrayList<Planificacion>();
		
		PlanificacionList planificacionList = (PlanificacionList) Component.getInstance("planificacionList", true);		
		
		List<Planificacion> p =  new ArrayList<Planificacion>();
		List<Planificacion> v =  new ArrayList<Planificacion>();

		for (Empleado empleado : empleados) {						
			
			planificacionList.getPlanificacion().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
			
			planificacionList.getPlanificacion().setMes(mes);
			
			planificacionList.getPlanificacion().setAnio(anio);		
			
			p=planificacionList.getListaPlanificacion();
			
//			log.info("LISTA MODIFICADA..........................................:"+planificacionList.getListaPlanificacion().size());
			if(p.size()==0){
				List <Empleado> aux=new ArrayList<Empleado>();
				aux.add(empleado);
				log.info("AUX..........................................:"+aux.size());
				v=planificacionHome.crearListaPlanificacion(aux, anio, mes);
				//log.info("VUELTA DE AUX..........................................:"+v.size());
				planificaciones.addAll(v);			
			}else{
				planificaciones.addAll(p);
			}
			
			/*planificacionList.getPlanificacion().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
			
			planificacionList.getPlanificacion().setMes(mes);
			
			planificacionList.getPlanificacion().setAnio(anio);	
			
			planificaciones.addAll(planificacionList.getListaPlanificacion());*/
		}
		 
		return planificaciones;
	}
	
	public void guardar(int i){
		
		vaca=horarioAlterno("V");
		perm=horarioAlterno("P");

		//new method///////////////////////////////////////
		//Boolean access=gestionPermisoVacacion.buscarModificarPlanificacion();
		this.access=gestionPermisoVacacion.buscarModificarPlanificacion();
		
		Calendar cal=Calendar.getInstance();
		cal.setTime(fechaDesde);
		this.buscarDatos();
		mes=fechaDesde.getMonth()+1;
		year=cal.get(Calendar.YEAR);
		log.info("MOUNT****************************************"+mes);
		log.info("YEAR*****************************************"+year);
		
		listPlanificacionRotativo = this.buscarPlanificacionCreada(empleados, mes, year);		
		log.info("lista size"+listPlanificacionRotativo.size());		
		log.info("ENTER TO THE METHOD");					
			
			for(Planificacion planif : listPlanificacionRotativo)
			{					
				for(Planificacion planif2 : listPlanificacion)
				{					
					if(planif.getEmpleadoByEmplId().getEmplId().intValue() == planif2.getEmpleadoByEmplId().getEmplId().intValue())
					{
						log.info("**************************************UPDATE DE PLANIFICACIÓN POR HORAS*****************************");				
						
						if(planif.getEstado()==null||planif.getEstado()==0){
							
							planif.setEstado(i);							
							String cod=planif.getEmpleadoByEmplId().getCodigoEmpleado();							
							planif.setHorarioByHora1Id(horarioPlanificacion(planif2.getHorarioByHora1Id(),1,cod,2));
							planif.setHorarioByHora2Id(horarioPlanificacion(planif2.getHorarioByHora2Id(),2,cod,2));
							planif.setHorarioByHora3Id(horarioPlanificacion(planif2.getHorarioByHora3Id(),3,cod,2));
							planif.setHorarioByHora4Id(horarioPlanificacion(planif2.getHorarioByHora4Id(),4,cod,2));
							planif.setHorarioByHora5Id(horarioPlanificacion(planif2.getHorarioByHora5Id(),5,cod,2));
							planif.setHorarioByHora6Id(horarioPlanificacion(planif2.getHorarioByHora6Id(),6,cod,2));
							planif.setHorarioByHora7Id(horarioPlanificacion(planif2.getHorarioByHora7Id(),7,cod,2));
							planif.setHorarioByHora8Id(horarioPlanificacion(planif2.getHorarioByHora8Id(),8,cod,2));
							planif.setHorarioByHora9Id(horarioPlanificacion(planif2.getHorarioByHora9Id(),9,cod,2));
							planif.setHorarioByHora10Id(horarioPlanificacion(planif2.getHorarioByHora10Id(),10,cod,2));
							planif.setHorarioByHora11Id(horarioPlanificacion(planif2.getHorarioByHora11Id(),11,cod,2));
							planif.setHorarioByHora12Id(horarioPlanificacion(planif2.getHorarioByHora12Id(),12,cod,2));
							planif.setHorarioByHora13Id(horarioPlanificacion(planif2.getHorarioByHora13Id(),13,cod,2));
							planif.setHorarioByHora14Id(horarioPlanificacion(planif2.getHorarioByHora14Id(),14,cod,2));
							planif.setHorarioByHora15Id(horarioPlanificacion(planif2.getHorarioByHora15Id(),15,cod,2));
							planif.setHorarioByHora16Id(horarioPlanificacion(planif2.getHorarioByHora16Id(),16,cod,2));
							planif.setHorarioByHora17Id(horarioPlanificacion(planif2.getHorarioByHora17Id(),17,cod,2));
							planif.setHorarioByHora18Id(horarioPlanificacion(planif2.getHorarioByHora18Id(),18,cod,2));
							planif.setHorarioByHora19Id(horarioPlanificacion(planif2.getHorarioByHora19Id(),19,cod,2));
							planif.setHorarioByHora20Id(horarioPlanificacion(planif2.getHorarioByHora20Id(),20,cod,2));
							planif.setHorarioByHora21Id(horarioPlanificacion(planif2.getHorarioByHora21Id(),21,cod,2));
							planif.setHorarioByHora22Id(horarioPlanificacion(planif2.getHorarioByHora22Id(),22,cod,2));
							planif.setHorarioByHora23Id(horarioPlanificacion(planif2.getHorarioByHora23Id(),23,cod,2));
							planif.setHorarioByHora24Id(horarioPlanificacion(planif2.getHorarioByHora24Id(),24,cod,2));
							planif.setHorarioByHora25Id(horarioPlanificacion(planif2.getHorarioByHora25Id(),25,cod,2));
							planif.setHorarioByHora26Id(horarioPlanificacion(planif2.getHorarioByHora26Id(),26,cod,2));
							planif.setHorarioByHora27Id(horarioPlanificacion(planif2.getHorarioByHora27Id(),27,cod,2));
							planif.setHorarioByHora28Id(horarioPlanificacion(planif2.getHorarioByHora28Id(),28,cod,2));
							planif.setHorarioByHora29Id(horarioPlanificacion(planif2.getHorarioByHora29Id(),29,cod,2));
							planif.setHorarioByHora30Id(horarioPlanificacion(planif2.getHorarioByHora30Id(),30,cod,2));
							planif.setHorarioByHora31Id(horarioPlanificacion(planif2.getHorarioByHora31Id(),31,cod,2));							
							break;
						}else{
							if(this.access){
								planif.setEstado(i);
								String cod=planif.getEmpleadoByEmplId().getCodigoEmpleado();							
								planif.setHorarioByHora1Id(horarioPlanificacion(planif2.getHorarioByHora1Id(),1,cod,2));
								planif.setHorarioByHora2Id(horarioPlanificacion(planif2.getHorarioByHora2Id(),2,cod,2));
								planif.setHorarioByHora3Id(horarioPlanificacion(planif2.getHorarioByHora3Id(),3,cod,2));
								planif.setHorarioByHora4Id(horarioPlanificacion(planif2.getHorarioByHora4Id(),4,cod,2));
								planif.setHorarioByHora5Id(horarioPlanificacion(planif2.getHorarioByHora5Id(),5,cod,2));
								planif.setHorarioByHora6Id(horarioPlanificacion(planif2.getHorarioByHora6Id(),6,cod,2));
								planif.setHorarioByHora7Id(horarioPlanificacion(planif2.getHorarioByHora7Id(),7,cod,2));
								planif.setHorarioByHora8Id(horarioPlanificacion(planif2.getHorarioByHora8Id(),8,cod,2));
								planif.setHorarioByHora9Id(horarioPlanificacion(planif2.getHorarioByHora9Id(),9,cod,2));
								planif.setHorarioByHora10Id(horarioPlanificacion(planif2.getHorarioByHora10Id(),10,cod,2));
								planif.setHorarioByHora11Id(horarioPlanificacion(planif2.getHorarioByHora11Id(),11,cod,2));
								planif.setHorarioByHora12Id(horarioPlanificacion(planif2.getHorarioByHora12Id(),12,cod,2));
								planif.setHorarioByHora13Id(horarioPlanificacion(planif2.getHorarioByHora13Id(),13,cod,2));
								planif.setHorarioByHora14Id(horarioPlanificacion(planif2.getHorarioByHora14Id(),14,cod,2));
								planif.setHorarioByHora15Id(horarioPlanificacion(planif2.getHorarioByHora15Id(),15,cod,2));
								planif.setHorarioByHora16Id(horarioPlanificacion(planif2.getHorarioByHora16Id(),16,cod,2));
								planif.setHorarioByHora17Id(horarioPlanificacion(planif2.getHorarioByHora17Id(),17,cod,2));
								planif.setHorarioByHora18Id(horarioPlanificacion(planif2.getHorarioByHora18Id(),18,cod,2));
								planif.setHorarioByHora19Id(horarioPlanificacion(planif2.getHorarioByHora19Id(),19,cod,2));
								planif.setHorarioByHora20Id(horarioPlanificacion(planif2.getHorarioByHora20Id(),20,cod,2));
								planif.setHorarioByHora21Id(horarioPlanificacion(planif2.getHorarioByHora21Id(),21,cod,2));
								planif.setHorarioByHora22Id(horarioPlanificacion(planif2.getHorarioByHora22Id(),22,cod,2));
								planif.setHorarioByHora23Id(horarioPlanificacion(planif2.getHorarioByHora23Id(),23,cod,2));
								planif.setHorarioByHora24Id(horarioPlanificacion(planif2.getHorarioByHora24Id(),24,cod,2));
								planif.setHorarioByHora25Id(horarioPlanificacion(planif2.getHorarioByHora25Id(),25,cod,2));
								planif.setHorarioByHora26Id(horarioPlanificacion(planif2.getHorarioByHora26Id(),26,cod,2));
								planif.setHorarioByHora27Id(horarioPlanificacion(planif2.getHorarioByHora27Id(),27,cod,2));
								planif.setHorarioByHora28Id(horarioPlanificacion(planif2.getHorarioByHora28Id(),28,cod,2));
								planif.setHorarioByHora29Id(horarioPlanificacion(planif2.getHorarioByHora29Id(),29,cod,2));
								planif.setHorarioByHora30Id(horarioPlanificacion(planif2.getHorarioByHora30Id(),30,cod,2));
								planif.setHorarioByHora31Id(horarioPlanificacion(planif2.getHorarioByHora31Id(),31,cod,2));									
								break;
							}else{
								log.info("...................................................No se guardo por privilegios");
								break;
							}
						}					
					}
				}		
				
			}
		
			//////////////////////////////////////////////////
			
			listPlanificacion = listPlanificacionRotativo;
			//planificacionHome.updatePlanificacion(listPlanificacion);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(
					"Se guardaron los cambios satisfactoriamente");	
			
			if (getTipoReporte().equals("Departamento")||getTipoReporte().equals("Cargo")||getTipoReporte().equals("Grupo")) {						
				this.getEmpleados().clear();	
			}	
	}

	
	//new method for planification per hour
	public void ExportarArchivoPl()
	{
//		Archivo archivo = new Archivo("D:\\planificacion.csv");
//		List<String> listaAGrabar = new ArrayList<String>();
//		listaAGrabar.add("EMPL_ID;HORA1_ID;HORA2_ID;HORA3_ID;HORA4_ID;HORA5_ID;HORA6_ID;HORA7_ID;HORA8_ID;HORA9_ID;HORA10_ID;HORA11_ID;HORA12_ID;HORA13_ID;HORA14_ID;HORA15_ID;HORA16_ID;HORA17_ID;HORA18_ID;HORA19_ID;HORA20_ID;HORA21_ID;HORA22_ID;HORA23_ID;HORA24_ID;HORA25_ID;HORA26_ID;HORA27_ID;HORA28_ID;HORA29_ID;HORA30_ID;HORA31_ID;ANIO;MES;ESTADO");
//		
//		for (Planificacion planif : listPlanificacion) {
//			String entrada = "";
//			entrada += String.valueOf(planif.getEmpleadoByEmplId().getEmplId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora1Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora1Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora2Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora2Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora3Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora3Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora4Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora4Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora5Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora5Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora6Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora6Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora7Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora7Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora8Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora8Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora9Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora9Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora10Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora10Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora11Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora11Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora12Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora12Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora13Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora13Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora14Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora14Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora15Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora15Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora16Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora16Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora17Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora17Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora18Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora18Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora19Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora19Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora20Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora20Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora21Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora21Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora22Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora22Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora23Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora23Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora24Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora24Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora25Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora25Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora26Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora26Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora27Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora27Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora28Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora28Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora29Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora29Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora30Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora30Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getHorarioByHora31Id() != null)
//			entrada += String.valueOf(planif.getHorarioByHora31Id().getHoraId()); 
//			entrada += ";";
//			if(planif.getAnio() != null)
//			entrada += String.valueOf(planif.getAnio()); 
//			entrada += ";";
//			if(planif.getMes() != null)
//			entrada += String.valueOf(planif.getMes()); 
//			entrada += ";";
//			if(planif.getEstado() != null)
//			entrada += String.valueOf(planif.getEstado());
//			
//			listaAGrabar.add(entrada);
//		}
//		
//		
//		archivo.EscribirArchivo("D:\\planificacion.csv", listaAGrabar);
	}
	//end method
	
	//new method por planification per hour
	public void ImportarArchivoPl()
	{
//		Archivo archivo = new Archivo("D:\\planificacion.csv");
//		List<String> listaRegistrosLeidos = new ArrayList<String>();
//		listaRegistrosLeidos = archivo.LeerArchivo();
//		
//		for(int i = 1;  i<listaRegistrosLeidos.size() ; i++ )
//		{
//			String[] palabras = listaRegistrosLeidos.get(i).split(";");
//			log.info("/*/*/*/*si entro 1/*/*/*/*");
//			log.info(palabras.length);
//			if(palabras.length == 35)
//			{
//				for (Planificacion planif : listPlanificacion) {
//					log.info("/*/*/*/*si entro 2/*/*/*/*");
//					if(!palabras[32].equals("") && !palabras[33].equals("") && !palabras[0].equals(""))
//					{
//						log.info("/*/*/*/*si entro 3/*/*/*/*");
//						log.info(planif.getAnio());
//						log.info(Integer.valueOf(palabras[32]));
//						log.info(planif.getMes());
//						log.info(Integer.valueOf(palabras[33]));
//						if(planif.getAnio() == Integer.valueOf(palabras[32]).intValue() && planif.getMes() == Integer.valueOf(palabras[33]).intValue())
//						{
//							log.info("/*/*/*/*si entro 4/*/*/*/*");
//							if(planif.getEmpleadoByEmplId().getEmplId().intValue() == Integer.valueOf(palabras[0]))
//							{
//								log.info("/*/*/*/*si entro 5/*/*/*/*");
//								if(!palabras[1].equals(""))
//								{
//									log.info("/*/*/*/*si entro 6/*/*/*/*");
//									entityManager.createNativeQuery("update planificacion set HORA1_ID = " + palabras[1] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//									
//								}if(!palabras[2].equals(""))
//								{
//									log.info("/*/*/*/*si entro 7/*/*/*/*");
//									entityManager.createNativeQuery("update planificacion set HORA2_ID = " + palabras[2] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[3].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA3_ID = " + palabras[3] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();	
//								}if(!palabras[4].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA4_ID = " + palabras[4] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[5].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA5_ID = " + palabras[5] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[6].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA6_ID = " + palabras[6] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[7].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA7_ID = " + palabras[7] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[8].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA8_ID = " + palabras[8] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[9].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA9_ID = " + palabras[9] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[10].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA10_ID = " + palabras[10] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[11].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA11_ID = " + palabras[11] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[12].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA12_ID = " + palabras[12] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[13].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA13_ID = " + palabras[13] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[14].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA14_ID = " + palabras[14] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[15].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA15_ID = " + palabras[15] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[16].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA16_ID = " + palabras[16] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[17].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA17_ID = " + palabras[17] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[18].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA18_ID = " + palabras[18] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[19].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA19_ID = " + palabras[19] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[20].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA20_ID = " + palabras[20] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[21].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA21_ID = " + palabras[21] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[22].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA22_ID = " + palabras[22] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[23].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA23_ID = " + palabras[23] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[24].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA24_ID = " + palabras[24] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[25].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA25_ID = " + palabras[25] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[26].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA26_ID = " + palabras[26] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[27].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA27_ID = " + palabras[27] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[28].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA28_ID = " + palabras[28] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[29].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA29_ID = " + palabras[29] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[30].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA30_ID = " + palabras[30] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}if(!palabras[31].equals(""))
//								{
//
//									entityManager.createNativeQuery("update planificacion set HORA31_ID = " + palabras[31] + " where EMPL_ID = " + Integer.valueOf(palabras[0]) +" AND ANIO = "+ palabras[32] + " AND MES = " +palabras[33]).executeUpdate();
//								}
//							}
//						}
//					}
//				}
//				
//			}
//		}
//		crearPlanificacion();
	}
	//end method
	
	//new method for planificacion per hour
	
	public Horario buscarHorarioLista(int codigo)
	{
		Horario horarioBuscado = new Horario();
		
		//for (Horario horar : ) {
			
		//}
		return horarioBuscado;
	}
	
	//end method

	public String eliminarPlanificacion() {

		listPlanificacion=this.buscarPlanificacion();		
			
			if(listPlanificacion!=null){
				if(listPlanificacion.size()>0){
					eliminarAsistenciaMultiple();
					this.buscarDatos();
					listPlanificacion=planificacionHome.eliminarListaPlanificacion(empleados, year, mes);
					listPlanificacion.clear();
					this.borraListas();
				}
				else{
					FacesMessages.instance().clear();
					FacesMessages.instance().add("No se ha encontrado registros!");
				}
				
			}else{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("No se ha encontrado registros!");
			}
			listPlanificacion=null;
			
		return "ok";
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

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public boolean getHora31() {
		return hora31;
	}

	public void setHora31(boolean hora31) {
		this.hora31 = hora31;
	}

	public boolean getHora30() {
		return hora30;
	}

	public void setHora30(boolean hora30) {
		this.hora30 = hora30;
	}

	public boolean getHora29() {
		return hora29;
	}

	public void setHora29(boolean hora29) {
		this.hora29 = hora29;
	}

	public boolean getHora28() {
		return hora28;
	}

	public void setHora28(boolean hora28) {
		this.hora28 = hora28;
	}

	public boolean getHora27() {
		return hora27;
	}

	public void setHora27(boolean hora27) {
		this.hora27 = hora27;
	}

	public boolean getHora26() {
		return hora26;
	}

	public void setHora26(boolean hora26) {
		this.hora26 = hora26;
	}

	public boolean getHora25() {
		return hora25;
	}

	public void setHora25(boolean hora25) {
		this.hora25 = hora25;
	}

	public boolean getHora24() {
		return hora24;
	}

	public void setHora24(boolean hora24) {
		this.hora24 = hora24;
	}

	public boolean getHora23() {
		return hora23;
	}

	public void setHora23(boolean hora23) {
		this.hora23 = hora23;
	}

	public boolean getHora22() {
		return hora22;
	}

	public void setHora22(boolean hora22) {
		this.hora22 = hora22;
	}

	public boolean getHora21() {
		return hora21;
	}

	public void setHora21(boolean hora21) {
		this.hora21 = hora21;
	}

	public boolean getHora20() {
		return hora20;
	}

	public void setHora20(boolean hora20) {
		this.hora20 = hora20;
	}

	public boolean getHora19() {
		return hora19;
	}

	public void setHora19(boolean hora19) {
		this.hora19 = hora19;
	}

	public boolean getHora18() {
		return hora18;
	}

	public void setHora18(boolean hora18) {
		this.hora18 = hora18;
	}

	public boolean getHora17() {
		return hora17;
	}

	public void setHora17(boolean hora17) {
		this.hora17 = hora17;
	}

	public boolean getHora16() {
		return hora16;
	}

	public void setHora16(boolean hora16) {
		this.hora16 = hora16;
	}

	public boolean getHora15() {
		return hora15;
	}

	public void setHora15(boolean hora15) {
		this.hora15 = hora15;
	}

	public boolean getHora14() {
		return hora14;
	}

	public void setHora14(boolean hora14) {
		this.hora14 = hora14;
	}

	public boolean getHora13() {
		return hora13;
	}

	public void setHora13(boolean hora13) {
		this.hora13 = hora13;
	}

	public boolean getHora12() {
		return hora12;
	}

	public void setHora12(boolean hora12) {
		this.hora12 = hora12;
	}

	public boolean getHora11() {
		return hora11;
	}

	public void setHora11(boolean hora11) {
		this.hora11 = hora11;
	}

	public boolean getHora10() {
		return hora10;
	}

	public void setHora10(boolean hora10) {
		this.hora10 = hora10;
	}

	public boolean getHora9() {
		return hora9;
	}

	public void setHora9(boolean hora9) {
		this.hora9 = hora9;
	}

	public boolean getHora8() {
		return hora8;
	}

	public void setHora8(boolean hora8) {
		this.hora8 = hora8;
	}

	public boolean getHora7() {
		return hora7;
	}

	public void setHora7(boolean hora7) {
		this.hora7 = hora7;
	}

	public boolean getHora6() {
		return hora6;
	}

	public void setHora6(boolean hora6) {
		this.hora6 = hora6;
	}

	public boolean getHora5() {
		return hora5;
	}

	public void setHora5(boolean hora5) {
		this.hora5 = hora5;
	}

	public boolean getHora4() {
		return hora4;
	}

	public void setHora4(boolean hora4) {
		this.hora4 = hora4;
	}

	public boolean getHora3() {
		return hora3;
	}

	public void setHora3(boolean hora3) {
		this.hora3 = hora3;
	}

	public boolean getHora2() {
		return hora2;
	}

	public void setHora2(boolean hora2) {
		this.hora2 = hora2;
	}

	public boolean getHora1() {
		return hora1;
	}

	public void setHora1(boolean hora1) {
		this.hora1 = hora1;
	}

	
/*empieza el nuevo codigo pora planificacion horas extras 04-12-2013*/
	
	
	public List<Integer> obtenerDiasMes(Integer anio, Integer mes) {
		List<Integer> diasMes = new ArrayList<Integer>();
		Boolean esBisiesto = Boolean.FALSE;

		// si es febrero
		if (mes.equals(new Integer(2))) {
			// verificar si es anio bisiesto
			if (anio % 4 == 0) {
				if (anio % 100 == 0) {
					if (anio % 400 == 0) {
						esBisiesto = Boolean.TRUE;
					} else {
						esBisiesto = Boolean.FALSE;
					}
				} else {
					esBisiesto = Boolean.TRUE;
				}
			} else {
				esBisiesto = Boolean.FALSE;
			}
			// fin anio bisiesto

			if (esBisiesto) {
				for (int i = 0; i < 29; i++) {
					diasMes.add(new Integer(i + 1));
				}

			} else {
				for (int i = 0; i < 28; i++) {
					diasMes.add(new Integer(i + 1));
				}
			}

		} else if (anio.equals(new Integer(4)) || anio.equals(new Integer(6))
				|| anio.equals(new Integer(9)) || anio.equals(new Integer(11))) {

			for (int i = 0; i < 30; i++) {
				diasMes.add(new Integer(i + 1));
			}

		} else {
			for (int i = 0; i < 31; i++) {
				diasMes.add(new Integer(i + 1));
			}
		}

		return diasMes;
	}
	
	public List<Integer> getListaDiasMes() {
		return listaDiasMes;
	}

	public void setListaDiasMes(List<Integer> listaDiasMes) {
		this.listaDiasMes = listaDiasMes;
	}
	
	
	
	public void creabackup() {
		log.info("**********entro al backup*********");	
		Runtime aplicacion = Runtime.getRuntime(); 
        try{
        	log.info("**********se creo el backup*********");	
        	aplicacion.exec("cmd.exe /K C:/proyecto/fulltime/view/backup/backup2.bat"); 
        }
        catch(Exception e){
        	
        	log.info("error "+e);
        }
	}

	public String getCadenaAnterior() {
		return cadenaAnterior;
	}

	public void setCadenaAnterior(String cadenaAnterior) {
		this.cadenaAnterior = cadenaAnterior;
	}

	public String getCadenaActual() {
		return cadenaActual;
	}

	public void setCadenaActual(String cadenaActual) {
		this.cadenaActual = cadenaActual;
	}	
	
	///////////////
	
	public String getDiaMatriz(int n){
		log.info("...........................................................ENTRO DIA "+n);
		String diaMatriz="";		
		int dm[]={31,28,31,30,31,30,31,31,30,31,30,31};
		try{			
			if(getFechaDesde()==null){
				diaMatriz="";
			}else{
				Calendar cal=Calendar.getInstance();
									
				cal.setTime(getFechaDesde());
				
				int AÑO=cal.get(Calendar.YEAR);
				int MES =Integer.parseInt(""+getFechaDesde().getMonth());
				int DIA=n;
				
				if(MES==1){
					if((AÑO%4)==0&&(AÑO%100)!=0){
						dm[1]=dm[1]+1;
					}else if((AÑO%100)==0&&(AÑO%400)==0){
						dm[1]=dm[1]+1;
					}
				}				
				
				int limit1=getFechaDesde().getDate();
				int limit2=getFechaHasta().getDate();
				
				if(DIA<=dm[MES]&&DIA>=limit1&&DIA<=limit2){
									
					cal.clear();
					cal.set(AÑO,MES,DIA);
					
					diaMatriz=""+cal.getTime();
					diaMatriz=diaMatriz.substring(0,3);
					
					if(diaMatriz.equals("Mon")){
						diaMatriz="Lun "+DIA;
					}else if(diaMatriz.equals("Tue")){
						diaMatriz="Mar "+DIA;
					}else if(diaMatriz.equals("Wed")){
						diaMatriz="Mier "+DIA;
					}else if(diaMatriz.equals("Thu")){
						diaMatriz="Jue "+DIA;
					}else if(diaMatriz.equals("Fri")){
						diaMatriz="Vie "+DIA;
					}else if(diaMatriz.equals("Sat")){
						diaMatriz="Sab "+DIA;
					}else if(diaMatriz.equals("Sun")){
						diaMatriz="Dom "+DIA;
					}
				}else{
					diaMatriz="";
				}				
			}
		}catch(Exception e){
			diaMatriz="";
		}
		
		
		return diaMatriz;
	}
	
	///////////////
	
	private boolean d2=false;
	//new method
	public boolean comprobarAsistencia(int n,String codigo){
		//log.info("//////////////////Procesar////////////////////////////////////");			
		boolean d1=false;
		
//		String formato="yyyy-MM-dd";
//		Calendar aux=Calendar.getInstance();		
//		String fecha=null;
	
//		for(Planificacion plani: listPlanificacion){	
			
			//log.info("ya esta en la base?:::::::::::::"+asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux));
			
//			if(plani.getEmpleadoByEmplId().getCodigoEmpleado().equals(codigo)){			
//			
//					switch(n){
//					
//					case 1:
//						if(plani.getHorarioByHora1Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora1Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//							log.info("ID1 HORARIO: "+plani.getEmpleadoByEmplId().getCodigoEmpleado()+" FECHA: "+aux.getTime()+" Bool "+ d1);
//						}else{	
//							log.info("ID1 HORARIO: ");
//							d1= false;			
//						}
//						break;
//						
//					case 2:
//						if(plani.getHorarioByHora2Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora2Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 3:
//						if(plani.getHorarioByHora3Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora3Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 4:
//						if(plani.getHorarioByHora4Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora4Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 5:
//						if(plani.getHorarioByHora5Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora5Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 6:
//						if(plani.getHorarioByHora6Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora6Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 7:
//						if(plani.getHorarioByHora7Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora7Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 8:
//						if(plani.getHorarioByHora8Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora8Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 9:
//						if(plani.getHorarioByHora9Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora9Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 10:
//						if(plani.getHorarioByHora10Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora10Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 11:
//						if(plani.getHorarioByHora11Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora11Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 12:
//						if(plani.getHorarioByHora12Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora12Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 13:
//						if(plani.getHorarioByHora13Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora13Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;	
//					case 14:
//						if(plani.getHorarioByHora14Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora14Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 15:
//						if(plani.getHorarioByHora15Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora15Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 16:
//						if(plani.getHorarioByHora16Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora16Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 17:
//						if(plani.getHorarioByHora17Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora17Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 18:
//						if(plani.getHorarioByHora18Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora18Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 19:
//						if(plani.getHorarioByHora19Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora19Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 20:
//						if(plani.getHorarioByHora20Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora20Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 21:
//						if(plani.getHorarioByHora21Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora21Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 22:
//						if(plani.getHorarioByHora22Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora22Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 23:
//						if(plani.getHorarioByHora23Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora23Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 24:
//						if(plani.getHorarioByHora24Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora24Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 25:
//						if(plani.getHorarioByHora25Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora25Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 26:
//						if(plani.getHorarioByHora26Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora26Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 27:
//						if(plani.getHorarioByHora27Id()!=null){								
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora27Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 28:
//						if(plani.getHorarioByHora28Id()!=null){									
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora28Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 29:
//						if(plani.getHorarioByHora29Id()!=null){										
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora29Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 30:
//						if(plani.getHorarioByHora30Id()!=null){										
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora30Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					case 31:
//						if(plani.getHorarioByHora31Id()!=null){										
//							fecha=plani.getAnio().toString()+"-"+plani.getMes().toString()+"-"+n;					
//							fechaSeleccionada=transformaFecha(fecha, formato);
//							detalleHorarios = buscarDetalleHorario(plani.getHorarioByHora31Id());					
//							aux.setTime(fechaSeleccionada);	
//							d1= asistenciaHome.verificar_asistencia(plani.getEmpleadoByEmplId(),aux);
//						}
//						break;
//					
//					
//					}
//			
//			}
//			
//		}
		
		d2=gestionPermisoVacacion.buscarModificarPlanificacion();
		
		//deshabilitar combobox si no tiene permisos y si ya están los registros en Asistencia
		/*if(d1==true){
			if(d2!=true){
				d1=false;
			}
		}	*/
		
		//desplegar combobox solamente si posee privilegios
		d1=d2;
		//
		
		return d1;
	}
	
	public boolean invertirValor(int n,String codigo){
		boolean d1=comprobarAsistencia(n,codigo),d3=false;
			if(d1==true){
				d3=true;
			}
		return d3;		
	}

	public Boolean getAccess() {
		return access;
	}

	public void setAccess(Boolean access) {
		this.access = access;
	}
	
	
	public List<Horario> getListaHorario2(String n) {			
		if(n.equals("")){
			return null;
		}else{
			return this.horarios;
		}
	}	
	
	public String verificaAsistencia(int b,String c){		
		
			Calendar cal=Calendar.getInstance();
			cal.setTime(getFechaDesde());
			
			int año=cal.get(Calendar.YEAR);
			int mes =Integer.parseInt(""+getFechaDesde().getMonth());
			int dia=b;
			
			Calendar aux=Calendar.getInstance();
			
			String formato="yyyy-MM-dd";
			
			mes++;
			String fecha=año+"-"+mes+"-"+dia;	
			
			fechaSeleccionada=transformaFecha(fecha, formato);
			
			aux.setTime(fechaSeleccionada);
			
			AsistenciaList asistenciaList= (AsistenciaList) Component.getInstance("asistenciaList", true);
			
			asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(c);
			asistenciaList.getAsistencia().setFechaHorario(aux.getTime());				
				
			List <Asistencia>list = asistenciaList.getListaAsistencias();
			
			if(list.size()>0){	
				
				boolean v=false;
				boolean p=false;
				
				for(Asistencia a:list){
					if(a.getEstado().equals("V")){
						v=true;
						break;
					}else{
						v=false;
						break;
					}
				}
				
				if(v){
					return "V";				
				}else{
					
					for(Asistencia a:list){
						if(a.getEstado().equals("P")){
							p=true;
						}else{
							p=false;
							break;
						}
					}
					
					if(p){
						return "P";
					}else{
						return "";
					}
				}				
				
			}else{
				return "vacio";
			}			
	
	}	
	
	public String ItemLabel(String n,int b,String c){
	
		if(n.equals("")){
			return "-";
		}else{
			Calendar cal=Calendar.getInstance();
			cal.setTime(getFechaDesde());
			
			int año=cal.get(Calendar.YEAR);
			int mes =Integer.parseInt(""+getFechaDesde().getMonth());
			int dia=b;
			
			Calendar aux=Calendar.getInstance();
			
			String formato="yyyy-MM-dd";
			
			mes++;
			String fecha=año+"-"+mes+"-"+dia;	
			
			fechaSeleccionada=transformaFecha(fecha, formato);
			
			aux.setTime(fechaSeleccionada);
			
			AsistenciaList asistenciaList= (AsistenciaList) Component.getInstance("asistenciaList", true);
			
			asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(c);
			asistenciaList.getAsistencia().setFechaHorario(aux.getTime());				
				
			List <Asistencia>list = asistenciaList.getListaAsistencias();
			
			if(list.size()>0){	
				
				boolean v=false;
				boolean p=false;
				
				for(Asistencia a:list){
					if(a.getEstado().equals("V")){
						v=true;
						break;
					}else{
						v=false;
						break;
					}
				}				
				if(v){
					return null;				
				}else{					
					for(Asistencia a:list){
						if(a.getEstado().equals("P")){
							p=true;
						}else{
							p=false;
							break;
						}
					}					
					if(p){
						return null;
					}else{
						return "-";
					}
				}					
			}else{
				return "-";
			}
		}
	}
	
	
	
	private Horario horarioAlterno(String a){
		Horario alterno=null;
		if(a.equals("V")){
			HorarioList horarioList= (HorarioList) Component.getInstance("horarioList", true);
			horarioList.getHorario().setEstado("Vacaciones");
			List <Horario> lista = new  ArrayList <Horario>();
			lista=horarioList.getResultList();
			
			//log.info("Vacaciones***********************************"+lista.size());
			if(lista.size()>0){
				for(Horario h:lista){
					alterno=h;
					
					break;
				}
				return alterno;
			}else{
				return null;
			}			
		}else{
			if(a.equals("P")){
				HorarioList horarioList= (HorarioList) Component.getInstance("horarioList", true);
				horarioList.getHorario().setEstado("Permiso");
				List <Horario> lista = new  ArrayList <Horario>();
				lista=horarioList.getResultList();
				
				//log.info("Permiso***********************************"+lista.size());
				if(lista.size()>0){
					for(Horario h:lista){
						alterno=h;
						break;
					}
					return alterno;
				}else{
					return null;
				}			
			}else{
				return null;
			}
		}
	}
	
	private Horario vaca=new Horario();
	private Horario perm=new Horario();
	
	private Horario horarioPlanificacion(Horario p,int dia,String codigo_empleado,int tipo){
		Horario retorno;			
		if(dia>=fechaDesde.getDate()&&dia<=fechaHasta.getDate()){
			String asis=verificaAsistencia(dia,codigo_empleado);
			log.info("DIA..............................................:"+dia+" -----------------------asis:"+asis);
			if(asis.equals("vacio")){
				if(tipo==1){
					retorno=p;
				}else{
					retorno=p;
				}				
			}else{
				if(asis.equals("")){																			
					retorno=p;														
				}else{										
					if(asis.equals("V")){
						if(tipo==1){
							retorno=vaca;
						}else{
//							Calendar cal=Calendar.getInstance();
//							cal.setTime(getFechaDesde());
//							
//							int año=cal.get(Calendar.YEAR);
//							int mes =Integer.parseInt(""+getFechaDesde().getMonth());
//							
//							Calendar aux=Calendar.getInstance();
//							
//							String formato="yyyy-MM-dd";
//							
//							mes++;
//							String fecha=año+"-"+mes+"-"+dia;	
							
							Date mydate=fechaSeleccionada(dia,getFechaDesde());
							
							SolicitudVacacionList solicitudVacacionList= (SolicitudVacacionList) Component.getInstance("solicitudVacacionList", true);
							List <SolicitudVacacion> sol=new ArrayList <SolicitudVacacion>();
								
							solicitudVacacionList.setAutorizado("si");
							solicitudVacacionList.getSolicitudVacacion().getEmpleadoByEmplId().setCodigoEmpleado(codigo_empleado);
							solicitudVacacionList.setMaxResults(null);
							
							sol=solicitudVacacionList.getResultList();
							
							boolean d=false;
							if(sol.size()>0){							
								for(SolicitudVacacion s: sol){
									Date init=s.getFechaDesde();
									Date end=s.getFechaHasta();
									if((mydate.compareTo(init)==0||mydate.compareTo(end)==0)||(mydate.after(init)&&mydate.before(end))){
										d=true;
										break;
									}
								}
							}
							
							if(d)
								retorno=vaca;
							else
								retorno=p;
						}		
					}else{
						if(tipo==1){
							retorno=perm;
						}else{
							
							Date mydate=fechaSeleccionada(dia,getFechaDesde());
							
							PermisoList permisoList= (PermisoList)Component.getInstance("permisoList", true);
							List <Permiso> permiso=new ArrayList <Permiso>();
								
							permisoList.setAutorizado("si");
							permisoList.getPermiso().getEmpleadoByEmplId().setCodigoEmpleado(codigo_empleado);
							permisoList.getPermiso().setFechaDesde(null);
							permisoList.setMaxResults(null);
							
							permiso=permisoList.getResultList();							
							
							boolean d=false;
							if(permiso.size()>0){	
								
								for(Permiso per: permiso){
									Date init=per.getFechaDesde();
									Date end=per.getFechaHasta();									
									if((mydate.compareTo(init)==0||mydate.compareTo(end)==0)||(mydate.after(init)&&mydate.before(end))){
										d=true;
										break;
									}
								}
							}
							
							if(d)
								retorno=perm;
							else
								retorno=p;
						}		
					}
				}
			}										
		}else{
			retorno=p;
		}
		return retorno;
	}	
	
	private List <Planificacion> planificacionIndividual(List <Planificacion> plani){
		vaca=horarioAlterno("V");
		perm=horarioAlterno("P");
		for(Planificacion pl: plani){
			String cod=pl.getEmpleadoByEmplId().getCodigoEmpleado();
			pl.setHorarioByHora1Id(horarioPlanificacion(pl.getHorarioByHora1Id(),1,cod,1));
			pl.setHorarioByHora2Id(horarioPlanificacion(pl.getHorarioByHora2Id(),2,cod,1));
			pl.setHorarioByHora3Id(horarioPlanificacion(pl.getHorarioByHora3Id(),3,cod,1));
			pl.setHorarioByHora4Id(horarioPlanificacion(pl.getHorarioByHora4Id(),4,cod,1));
			pl.setHorarioByHora5Id(horarioPlanificacion(pl.getHorarioByHora5Id(),5,cod,1));
			pl.setHorarioByHora6Id(horarioPlanificacion(pl.getHorarioByHora6Id(),6,cod,1));
			pl.setHorarioByHora7Id(horarioPlanificacion(pl.getHorarioByHora7Id(),7,cod,1));
			pl.setHorarioByHora8Id(horarioPlanificacion(pl.getHorarioByHora8Id(),8,cod,1));
			pl.setHorarioByHora9Id(horarioPlanificacion(pl.getHorarioByHora9Id(),9,cod,1));
			pl.setHorarioByHora10Id(horarioPlanificacion(pl.getHorarioByHora10Id(),10,cod,1));
			pl.setHorarioByHora11Id(horarioPlanificacion(pl.getHorarioByHora11Id(),11,cod,1));
			pl.setHorarioByHora12Id(horarioPlanificacion(pl.getHorarioByHora12Id(),12,cod,1));
			pl.setHorarioByHora13Id(horarioPlanificacion(pl.getHorarioByHora13Id(),13,cod,1));
			pl.setHorarioByHora14Id(horarioPlanificacion(pl.getHorarioByHora14Id(),14,cod,1));
			pl.setHorarioByHora15Id(horarioPlanificacion(pl.getHorarioByHora15Id(),15,cod,1));
			pl.setHorarioByHora16Id(horarioPlanificacion(pl.getHorarioByHora16Id(),16,cod,1));
			pl.setHorarioByHora17Id(horarioPlanificacion(pl.getHorarioByHora17Id(),17,cod,1));
			pl.setHorarioByHora18Id(horarioPlanificacion(pl.getHorarioByHora18Id(),18,cod,1));
			pl.setHorarioByHora19Id(horarioPlanificacion(pl.getHorarioByHora19Id(),19,cod,1));
			pl.setHorarioByHora20Id(horarioPlanificacion(pl.getHorarioByHora20Id(),20,cod,1));
			pl.setHorarioByHora21Id(horarioPlanificacion(pl.getHorarioByHora21Id(),21,cod,1));
			pl.setHorarioByHora22Id(horarioPlanificacion(pl.getHorarioByHora22Id(),22,cod,1));
			pl.setHorarioByHora23Id(horarioPlanificacion(pl.getHorarioByHora23Id(),23,cod,1));
			pl.setHorarioByHora24Id(horarioPlanificacion(pl.getHorarioByHora24Id(),24,cod,1));
			pl.setHorarioByHora25Id(horarioPlanificacion(pl.getHorarioByHora25Id(),25,cod,1));
			pl.setHorarioByHora26Id(horarioPlanificacion(pl.getHorarioByHora26Id(),26,cod,1));
			pl.setHorarioByHora27Id(horarioPlanificacion(pl.getHorarioByHora27Id(),27,cod,1));
			pl.setHorarioByHora28Id(horarioPlanificacion(pl.getHorarioByHora28Id(),28,cod,1));
			pl.setHorarioByHora29Id(horarioPlanificacion(pl.getHorarioByHora29Id(),29,cod,1));
			pl.setHorarioByHora30Id(horarioPlanificacion(pl.getHorarioByHora30Id(),30,cod,1));
			pl.setHorarioByHora31Id(horarioPlanificacion(pl.getHorarioByHora31Id(),31,cod,1));
		}	
		return plani;
	}	
	
	private Date fechaSeleccionada(int dia,Date fechaDesde){
		Calendar cal=Calendar.getInstance();
		cal.setTime(fechaDesde);
		
		int año=cal.get(Calendar.YEAR);
		int mes =Integer.parseInt(""+getFechaDesde().getMonth());
		
		Calendar aux=Calendar.getInstance();
		
		String formato="yyyy-MM-dd";
		
		mes++;
		String fecha=año+"-"+mes+"-"+dia;	
		
		Date mydate=transformaFecha(fecha, formato);
		
		return mydate;
	}
}