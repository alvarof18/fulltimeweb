package com.casapazmino.fulltime.procesos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesContext;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;

import com.casapazmino.fulltime.action.AsistenciaHome;
import com.casapazmino.fulltime.action.DetalleHorarioList;
import com.casapazmino.fulltime.action.DetallePlanificacionMultipleHome;
import com.casapazmino.fulltime.action.DetallePlanificacionMultipleList;
import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.action.EmpleadoList;
import com.casapazmino.fulltime.action.HorarioList;
import com.casapazmino.fulltime.action.PlanificacionMultipleHome;
import com.casapazmino.fulltime.action.PlanificacionMultipleList;
import com.casapazmino.fulltime.action.PlantillaPlanificacion;
import com.casapazmino.fulltime.action.PlantillaPlanificacionMultiple;
import com.casapazmino.fulltime.action.PlantillaPlanificacionMultipleEmpleado;
import com.casapazmino.fulltime.comun.Conexion;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.DetallePlanificacionMultiple;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.model.PlanificacionMultiple;
import com.casapazmino.fulltime.model.SolicitudVacacion;

@Name("planificacionHospitales")
@Scope(ScopeType.SESSION)
public class PlanificacionHospitalesBean{
	
	static final long serialVersionUID = 8750596808938047779L;

	@In
	EntityManager entityManager;	
	
	@Logger
	Log log;
	
	private boolean visualizaPanelHorarios; 
	
	private String valueSelected;
	
	private int numeroFilas;
	
	private int vacia;
	
	private String tipoGrupo;
	
	private List<Empleado> empleados;
	
	private ArrayList<Long> cargos;
	
	private ArrayList<Long> departamentos;
	
	private ArrayList<Long> detalleGrupoContratados;
	
	private ArrayList<Long> ciudades;
	
	private Date fechaDesde;
	
	private Date fechaHasta;
	
	private List<Long> codigosEmpleado;
	
	private List<Horario> listaHorario;
	
	private List<DetalleHorario> listaDetalleHorario;
	
	private List<PlantillaPlanificacionMultiple> listaPlanificacion;
	
	private List<PlantillaPlanificacionMultipleEmpleado> listaPlanificacionEmpleado;
	
	private List<SelectItem> listaCodigosHorario;
	
	private List<SelectItem> listaNumeroFilas;
	
	private Integer filaEdit=1;	
	
	private Integer filaRemove=1;
	
	private String EmpleadoRemove; 
	
	private String dataBase;
	
	private Integer registrosProcesados;
	
	private Integer registrosNoProcesados;
	
	private List<PlantillaPlanificacion> asistenciaInsert;
	
	private String value1;
	private String value2;
	private String value3;
	private String value4;
	private String value5;
	private String value6;
	private String value7;
	private String value8;
	private String value9;
	private String value10;
	private String value11;
	private String value12;
	private String value13;
	private String value14;
	private String value15;
	private String value16;
	private String value17;
	private String value18;
	private String value19;
	private String value20;
	private String value21;
	private String value22;
	private String value23;
	private String value24;
	private String value25;
	private String value26;
	private String value27;
	private String value28;
	private String value29;
	private String value30;
	private String value31;
	
	@In(create = true)
	HorarioList horarioList;
	
	@In(create = true)
	DetalleHorarioList detalleHorarioList;
	
	@In(create = true)
	PlanificacionMultipleList planificacionMultipleList;
	
	@In(create = true)
	DetallePlanificacionMultipleList detallePlanificacionMultipleList;
	
	@In(create = true)
	PlanificacionMultipleHome planificacionMultipleHome;
	
	@In(create = true)
	DetallePlanificacionMultipleHome detallePlanificacionMultipleHome;
	
	@In(create = true)
	AsistenciaHome asistenciaHome;
	
	private Conexion conexion;
	
	GestionarPlanificacion gestionarPlanificacion;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	private Empleado empleadoActual;
	private int acceso;
	
	//nuevas variables
	
	private String mensajeCorrecto;
	private String mensajeError;
	
	Boolean archivoAdjunto;
	
	//Constructor del Bean 
	public PlanificacionHospitalesBean() {	
		super();
		tipoGrupo="Cargo";
		CargarHorarios();
		CargarDetalleHorarios();
		CargarListaNumeroFilas();
		numeroFilas=5;
		valueSelected="5";
		visualizaPanelHorarios=false;		
		
		conexion=new Conexion();	
		dataBase=ControlBaseDatos.baseDatos;
		gestionarPlanificacion=new GestionarPlanificacion(conexion, dataBase);
		
		VaciarRegistros();	
		
		CargarEmpleado();
		archivoAdjunto=false;
		
	}
	
	private void VaciarRegistros(){
		registrosProcesados=0;
		registrosNoProcesados=0;
	}
		
	//Cargar en listas la tabla horarios y los códigos de horario
	private void CargarHorarios(){	
		//**********Cargar lista de Horarios
		listaHorario=new ArrayList<Horario>();		
		horarioList= (HorarioList) Component.getInstance("horarioList", true);	
		horarioList.setOrder("horario.codigo");
		listaHorario=horarioList.getListaHorario();	
		//**********
		
		//**********Cargar lista de códigos de horarios
		listaCodigosHorario=new ArrayList <SelectItem>();	
		SelectItem item = new SelectItem("empty", "-");
		listaCodigosHorario.add(item);
		for(Horario h: listaHorario){
			 item = new SelectItem(""+h.getHoraId(), ""+h.getCodigo());
			listaCodigosHorario.add(item);
		}
		item = new SelectItem("V", "V");
		listaCodigosHorario.add(item);
		//**********		
	}
	
	private List<Empleado> listaEmpleadoBD;
	
	//Cargar lista de Empleados
	private void CargarEmpleado(){
		listaEmpleadoBD=new ArrayList<Empleado>();
		EmpleadoList empleadoList= (EmpleadoList) Component.getInstance("empleadoList", true);			
		listaEmpleadoBD=empleadoList.getListaEmpleado();
	}
	
	private Empleado BuscarEmpleadoCodigo(String codigoEmpleado){
		Empleado empleado=null;
		
		for(Empleado e:listaEmpleadoBD){
				if(e.getCodigoEmpleado().equals(codigoEmpleado)){
				empleado=e;
				break;
			}
			
		}
		
		return empleado;
	}
	
	//Cargar en lista la tabla detalle_horario
	private void CargarDetalleHorarios(){	
		//**********Cargar lista de Horarios
		listaDetalleHorario=new ArrayList<DetalleHorario>();		
		detalleHorarioList= (DetalleHorarioList) Component.getInstance("detalleHorarioList", true);	
		detalleHorarioList.setOrder("detalleHorario.horario.horaId, detalleHorario.orden");
		listaDetalleHorario=detalleHorarioList.getListaDetalleHorario();
		//**********
	}
	
	//Cargar Página web	
	private void CargarPagina(){
		try{
			String url="/fulltime/fulltime/PlanificacionMultiple.seam"; //url donde se redirige la pantalla
	        FacesContext facesContext=new FacesContext();
	        facesContext.getContext();
			javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//Cargar Lista de número de filas
	private void CargarListaNumeroFilas(){
		listaNumeroFilas=new ArrayList<SelectItem>();
		
		SelectItem opcion1=new SelectItem("5","5");
		SelectItem opcion2=new SelectItem("10","10");
		SelectItem opcion3=new SelectItem("15","15");
		SelectItem opcion4=new SelectItem("20","20");
		SelectItem opcion5=new SelectItem("30","30");
		SelectItem opcion6=new SelectItem("40","40");
		SelectItem opcion7=new SelectItem("50","50");
		
		listaNumeroFilas.add(opcion1);
		listaNumeroFilas.add(opcion2);
		listaNumeroFilas.add(opcion3);
		listaNumeroFilas.add(opcion4);
		listaNumeroFilas.add(opcion5);
		listaNumeroFilas.add(opcion6);
		listaNumeroFilas.add(opcion7);
	}
	
	//Cambiar cantidad de filas a mostrar lista Front-End
	public void cambiarNumeroFilas(ValueChangeEvent event) {
		
		 String value=""+event.getNewValue();
		 
		 if(value.equals("5"))
			 setNumeroFilas(5);
		 else
			 if(value.equals("10"))
				 setNumeroFilas(10);
			 else
				 if(value.equals("15"))
					 setNumeroFilas(15);
				 else
					 if(value.equals("20"))
						 setNumeroFilas(20);
					 else
						 if(value.equals("30"))
							 setNumeroFilas(30);	
						 else
							 if(value.equals("40"))
								 setNumeroFilas(40);
							 else
								 if(value.equals("50"))
									 setNumeroFilas(50);	
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
	private List<Empleado> buscarEmpleadosCargoIndividual(Empleado empleado) {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.cargo.cargId in (:cargos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.emplId =(:empleado) order by e.apellido")
				.setParameter("cargos", getCargos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.setParameter("empleado", empleado.getEmplId())
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Empleado> buscarEmpleadosCargoSubordinado(Empleado empleado) {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.cargo.cargId in (:cargos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.empleado.emplId =(:empleado) order by e.apellido")
				.setParameter("cargos", getCargos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.setParameter("empleado", empleado.getEmplId())
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
	private List<Empleado> buscarEmpleadosDepartamentoIndividual(Empleado empleado) {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.departamento.depaId in (:departamentos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.emplId =(:empleado) order by e.apellido")
				.setParameter("departamentos", getDepartamentos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.setParameter("empleado", empleado.getEmplId())
				.getResultList();
	}	
	
	@SuppressWarnings({ "unchecked", "unused" })
	private List<Empleado> buscarEmpleadosDepartamentoSubordinado(Empleado empleado) {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.departamento.depaId in (:departamentos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.empleado.emplId =(:empleado) order by e.apellido")
				.setParameter("departamentos", getDepartamentos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.setParameter("empleado", empleado.getEmplId())
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
	private List<Empleado> buscarEmpleadosGrupoIndividual(Empleado empleado) {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.detalleGrupoContratado.dgcoId in (:grupos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.emplId =(:empleado) order by e.apellido")
				.setParameter("grupos", getDetalleGrupoContratados())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.setParameter("empleado", empleado.getEmplId())
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Empleado> buscarEmpleadosGrupoSubordinado(Empleado empleado) {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.detalleGrupoContratado.dgcoId in (:grupos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.empleado.emplId =(:empleado) order by e.apellido")
				.setParameter("grupos", getDetalleGrupoContratados())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.setParameter("empleado", empleado.getEmplId())
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Asistencia> buscarAsistencia(ArrayList<Long> listaEmpleado, Date fechaInicial, Date fechaFinal) {
		return (List<Asistencia>) entityManager
				.createQuery(
						"select a from Asistencia a where a.empleado.emplId in (:empleado) and a.fechaHorario >= (:fechaInicial) and a.fechaHorario <= (:fechaFinal)  order by a.fechaHorario")
				.setParameter("empleado", listaEmpleado)
				.setParameter("fechaInicial", fechaInicial)
				.setParameter("fechaFinal", fechaFinal)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<SolicitudVacacion> buscarVacaciones(ArrayList<Long> listaEmpleado, Date fechaInicial, Date fechaFinal ,String autorizado) {
		return (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select s from SolicitudVacacion s where s.empleadoByEmplId.emplId in (:empleado) and "
						+ "( s.fechaDesde between (:fechaInicial) and (:fechaFinal) ) or ( s.fechaHasta between (:fechaInicial) and (:fechaFinal) ) or "
						+ "( (:fechaInicial) between s.fechaDesde and s.fechaHasta ) or ( (:fechaFinal) between s.fechaDesde and s.fechaHasta ) "
						+ "and lower(s.detalleTipoParametro.descripcion) = (:autorizado)")
				.setParameter("empleado", listaEmpleado)
				.setParameter("fechaInicial", fechaInicial)
				.setParameter("fechaFinal", fechaFinal)
				.setParameter("autorizado", autorizado)
				.getResultList();
	}
	
	//**********Búsqueda de empleados por selección de grupo **********
	public void buscarDatos() {	
		
		empleadoActual=gestionPermisoVacacion.buscarEmpleado();		
		acceso=gestionPermisoVacacion.buscarAccesoEmpleados();
		
		try {
			
			if(acceso==Constantes.ACCESO_SUBORDINADOS){
				
				if (tipoGrupo.equals("Cargo")){
					
					getEmpleados().clear();
					getEmpleados().addAll(buscarEmpleadosCargoIndividual(this.empleadoActual));
					getEmpleados().addAll(buscarEmpleadosCargoSubordinado(this.empleadoActual));	
					
				} else if (tipoGrupo.equals("Departamento")){
					
//					getEmpleados().clear();
//					getEmpleados().addAll ( buscarEmpleadosDepartamento());
					
					getEmpleados().addAll(buscarEmpleadosDepartamentoIndividual(this.empleadoActual));
					getEmpleados().addAll(buscarEmpleadosDepartamentoSubordinado(this.empleadoActual));
					
				} else if (tipoGrupo.equals("Grupo")) {
					
					getEmpleados().clear();
					getEmpleados().addAll(buscarEmpleadosGrupoIndividual(this.empleadoActual));
					getEmpleados().addAll ( buscarEmpleadosGrupoSubordinado(this.empleadoActual));
				} 
				
			}
			else{
				if(acceso==Constantes.ACCESO_INDIVIDUAL){
					
					if (tipoGrupo.equals("Cargo")){
						
						getEmpleados().clear();
						getEmpleados().addAll(buscarEmpleadosCargoIndividual(this.empleadoActual));	
						
					} else if (tipoGrupo.equals("Departamento")){
						
						getEmpleados().clear();
						getEmpleados().addAll ( buscarEmpleadosDepartamentoIndividual(this.empleadoActual));
						
					} else if (tipoGrupo.equals("Grupo")) {
						
						getEmpleados().clear();
						getEmpleados().addAll ( buscarEmpleadosGrupoIndividual(this.empleadoActual));
					} 
					
				}else{
					
					if (tipoGrupo.equals("Cargo")){
						
						getEmpleados().clear();
						getEmpleados().addAll(buscarEmpleadosCargo());	
						
					} else if (tipoGrupo.equals("Departamento")){
						
						getEmpleados().clear();
						getEmpleados().addAll ( buscarEmpleadosDepartamento());
						
					} else if (tipoGrupo.equals("Grupo")) {
						
						getEmpleados().clear();
						getEmpleados().addAll ( buscarEmpleadosGrupo());
					} 
					
				}
			}	
			
		}catch (Exception e) {
			log.info(e);
		}
	}
	
	//Restaurar pagina
	public void Cancelar(){
		tipoGrupo="Cargo";
		CargarHorarios();
		CargarDetalleHorarios();
		CargarListaNumeroFilas();
		numeroFilas=5;
		valueSelected="5";
		visualizaPanelHorarios=false;
		listaPlanificacion=null;
		listaPlanificacionEmpleado=null;
		empleados=null;
		cargos=null;
		departamentos=null;
		detalleGrupoContratados=null;
		ciudades=null;
		fechaDesde=null;
		fechaHasta=null;
		dataBase=ControlBaseDatos.baseDatos;
		VaciarRegistros();
	}
	
	//Eliminar planificación
	public void EliminarRegistrosAlmacenados(){
		
		VaciarRegistros();
		
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
				
			int longitudListaPlanificacion=getListaPlanificacion().size();
			
			if(longitudListaPlanificacion>0){
				
				EliminarRegistrosIndividualesPlanificacion();
				listaPlanificacion=null;				
				
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Eliminados!");
			}else{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("No se Encontraron Registros!");
			}
				
		}else{
			listaPlanificacion=null;
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		CargarPagina();
		
	}
	
	//Eliminar Asistencia
	public void EliminarRegistrosProcesados(){
		
		VaciarRegistros();
		
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
				
			int longitudListaPlanificacion=getListaPlanificacion().size();
			
			if(longitudListaPlanificacion>0){
				
				EliminarRegistrosIndividualesAsistencia();
				//listaPlanificacion=null;				
				
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Eliminados!");
			}else{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("No se Encontraron Registros!");
			}
				
		}else{
			listaPlanificacion=null;
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		CargarPagina();
		
	}
	
	//Eliminar Asistencia y Procesados
	public void EliminarRegistrosPlanificacionAsistencia(){
		
		VaciarRegistros();
		
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
				
			int longitudListaPlanificacion=getListaPlanificacion().size();
			
			if(longitudListaPlanificacion>0){
				
				EliminarRegistrosIndividualesPlanificacionAsistencia();
				listaPlanificacion=null;				
				
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Eliminados!");
			}else{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("No se Encontraron Registros!");
			}
				
		}else{
			listaPlanificacion=null;
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
		
		CargarPagina();
		
	}
	
	//Eliminar Registros de Planificacion	
	private void EliminarRegistrosIndividualesPlanificacion(){
		
		List <Empleado> listaEmpleado=new ArrayList<Empleado>();
		for(PlantillaPlanificacionMultiple p : listaPlanificacion){
			listaEmpleado.add(p.getEmpleado());
		}
		
		ArrayList <Long> listaIdEmpleado=getListaIdEmpleados(listaEmpleado);
		Integer mes=getMes(fechaDesde)+1;
		Integer año=getAño(fechaDesde);
		
		List<PlanificacionMultiple> listaPlanificacionMultiple=getListaPlanificacionMultipleBD(listaIdEmpleado,mes,año);
		
		if(listaPlanificacionMultiple.size()>0){
			
			List <DetallePlanificacionMultiple>listaDetallePlanificacionMultiple=getListaDetallePlanificacionMultipleBD(getListaIdPlanificacion(listaPlanificacionMultiple));
		
			if(listaDetallePlanificacionMultiple.size()>0){
				//Eliminar Detalle de Planificacion				
				ArrayList<Long> listaIdDetallePlanificacion=new ArrayList<Long>();
				
				for(DetallePlanificacionMultiple dp: listaDetallePlanificacionMultiple){
					//GestionarDetallePlanificacionMultiple(dp,"delete");
					listaIdDetallePlanificacion.add(dp.getIdDetallePlanificacionMultiple());				
				}	
				EliminarRegistrosDetallePlanificacionMultiple(listaIdDetallePlanificacion);
			}
			//Eliminar Planificacion
			ArrayList<Long> listaIdPlanificacion=new ArrayList<Long>();			
			for(PlanificacionMultiple pm: listaPlanificacionMultiple){
				listaIdPlanificacion.add(pm.getIdPlanificacionMultiple());			
			}			
			EliminarRegistrosPlanificacionMultiple(listaIdPlanificacion);
		}
		
//		//Eliminar Asistencia
//		List<Asistencia> listaAsistencia=getListaAsistenciaEmpleados(listaIdEmpleado,this.fechaDesde,this.fechaHasta);
//		if(listaAsistencia.size()>0)
//			GestionarListaAsistencia(listaAsistencia,"delete");
		
	}
	
	//Eliminar todos los Registros
	private void EliminarRegistrosIndividualesPlanificacionAsistencia(){
		
		List <Empleado> listaEmpleado=new ArrayList<Empleado>();
		for(PlantillaPlanificacionMultiple p : listaPlanificacion){
			listaEmpleado.add(p.getEmpleado());
		}
		
		ArrayList <Long> listaIdEmpleado=getListaIdEmpleados(listaEmpleado);
		Integer mes=getMes(fechaDesde)+1;
		Integer año=getAño(fechaDesde);
		
		List<PlanificacionMultiple> listaPlanificacionMultiple=getListaPlanificacionMultipleBD(listaIdEmpleado,mes,año);
		
		if(listaPlanificacionMultiple.size()>0){
			
			List <DetallePlanificacionMultiple>listaDetallePlanificacionMultiple=getListaDetallePlanificacionMultipleBD(getListaIdPlanificacion(listaPlanificacionMultiple));
		
			if(listaDetallePlanificacionMultiple.size()>0){
				//Eliminar Detalle de Planificacion				
				ArrayList<Long> listaIdDetallePlanificacion=new ArrayList<Long>();
				
				for(DetallePlanificacionMultiple dp: listaDetallePlanificacionMultiple){
					//GestionarDetallePlanificacionMultiple(dp,"delete");
					listaIdDetallePlanificacion.add(dp.getIdDetallePlanificacionMultiple());				
				}	
				EliminarRegistrosDetallePlanificacionMultiple(listaIdDetallePlanificacion);
			}
			//Eliminar Planificacion
			ArrayList<Long> listaIdPlanificacion=new ArrayList<Long>();			
			for(PlanificacionMultiple pm: listaPlanificacionMultiple){
				listaIdPlanificacion.add(pm.getIdPlanificacionMultiple());			
			}			
			EliminarRegistrosPlanificacionMultiple(listaIdPlanificacion);
		}
		
//		//Eliminar Asistencia
		List<Asistencia> listaAsistencia=getListaAsistenciaEmpleados(listaIdEmpleado,this.fechaDesde,this.fechaHasta);
		if(listaAsistencia.size()>0)
			GestionarListaAsistencia(listaAsistencia,"delete");
		
	}
	
	//Eliminar Registros de Asistencia	
	private void EliminarRegistrosIndividualesAsistencia(){
		
		List <Empleado> listaEmpleado=new ArrayList<Empleado>();
		for(PlantillaPlanificacionMultiple p : listaPlanificacion){
			listaEmpleado.add(p.getEmpleado());
		}
		
		ArrayList <Long> listaIdEmpleado=getListaIdEmpleados(listaEmpleado);
		
		//Eliminar Asistencia
		List<Asistencia> listaAsistencia=getListaAsistenciaEmpleados(listaIdEmpleado,this.fechaDesde,this.fechaHasta);
		if(listaAsistencia.size()>0)
			GestionarListaAsistencia(listaAsistencia,"delete");
		
	}
		
	//Procesar Registros
	public void ProcesarRegistros(){
		
		VaciarRegistros();
				
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
				
			int longitudListaPlanificacion=getListaPlanificacion().size();
			
			if(longitudListaPlanificacion>0){
				
				if(archivoAdjunto){
					GuardarRegistrosMatriz();
					archivoAdjunto=false;
				}
				
				ProcesarListaPlanificacion();
				
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Procesados!");
			}else{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("No se Encontraron Registros!");
			}
				
		}else{
			listaPlanificacion=null;
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
	}
	
	//Procesar Registros de Recarga de Timbres
	public void CargarTimbres(){
		
		VaciarRegistros();
				
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
				
			int longitudListaPlanificacion=getListaPlanificacion().size();
			
			if(longitudListaPlanificacion>0){
				
				RecargarTimbresEmpleados(empleados);
				
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Registros Cargados Correctamente!");
			}else{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("No se Encontraron Registros!");
			}
				
		}else{
			listaPlanificacion=null;
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
	}
	
	//Recargar timbres de empleados
	private void RecargarTimbresEmpleados(List<Empleado> listaEmpleado){
		
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
	
	private void ProcesarListaPlanificacion(){
		
		getAsistenciaInsert().clear();
		
		ArrayList<Long> listaIdEmpleado=new ArrayList<Long>();		
		for(PlantillaPlanificacionMultiple p: listaPlanificacion){
			listaIdEmpleado.add(p.getEmpleado().getEmplId());
		}
		
		List<Asistencia> listaAsistencia=getListaAsistenciaEmpleados(listaIdEmpleado,this.fechaDesde,this.fechaHasta);		
			
		for(PlantillaPlanificacionMultiple p: listaPlanificacion){
			
			List<Asistencia> listaAsistenciaEmpleado=ObtenerListaAsistenciaEmpleado(listaAsistencia,p.getEmpleado().getEmplId());
			
			List<SelectItem> diasHorariosEmpleado=ComprobarRegistrosAsistencia(listaAsistenciaEmpleado, p);
			CompararRegistrosAsistencia(diasHorariosEmpleado, listaAsistenciaEmpleado, p);
		}
		
		gestionarPlanificacion.InsertarRegistros(asistenciaInsert);
	}
	

	//**********Verificar Registros de Empleados**********	
	public void VerificarRegistros(){
		
		VaciarRegistros();
		
		String validacionFechas=ValidarFecha();
		
		if(validacionFechas.equals("ok")){
			
			try{
				buscarDatos();		 
				int longitudListaEmpleados=empleados.size();
				if(longitudListaEmpleados>0){
					log.info("...........................................longitudListaEmpleados: "+longitudListaEmpleados);
					
					CargarListaPlanificacion(empleados);
					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("Registros Verificados!");
				}
				else{					
					FacesMessages.instance().clear();
					FacesMessages.instance().add("No se Encontraron Registros!");
				}
			}catch(Exception e){
				e.printStackTrace();
				listaPlanificacion=null;
				InvalidValue iv= new InvalidValue("Seleccione los parámetros de búsqueda correctamente!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}	
			
		}else{
			listaPlanificacion=null;
			InvalidValue iv= new InvalidValue(validacionFechas,null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}	
	}
	
	//Validar fechas de ingreso	
	private String ValidarFecha(){
		
		String msg="";
				
		int mesDesde=getMes(fechaDesde)+1;				
		int mesHasta=getMes(fechaHasta)+1;
		int AñoDesde=getAño(fechaDesde);
		int añoHasta=getAño(fechaHasta);
						
		if(mesDesde==mesHasta&&AñoDesde==añoHasta){
			if(getDia(fechaDesde)<=getDia(fechaHasta)){
				msg="ok";
			}else{
				msg="La Fecha Inicial no debe ser mayor que la Fecha Final!";						
			}
		}
		else{
			msg="Las Fechas deben ser del mismo mes y año!";
		}
		
		return msg;
	}	
	
	//cargar Lista para mostrar en la pagina
	private void CargarListaPlanificacion(List <Empleado> listaEmpleado){
				
		getListaPlanificacion().clear();
		
		ArrayList <Long> listaIdEmpleado=getListaIdEmpleados(listaEmpleado);
		Integer mes=getMes(fechaDesde)+1;
		Integer año=getAño(fechaDesde);
		
		List<PlanificacionMultiple> listaPlanificacionMultiple=getListaPlanificacionMultipleBD(listaIdEmpleado,mes,año);
		
		List<SolicitudVacacion> listaSolicitudVacacion=buscarVacaciones(listaIdEmpleado,getLimiteInferior(),getLimiteSuperior(),"si");
		
		if(listaPlanificacionMultiple.size()>0){
			CargarListaPlanificacionBD(listaEmpleado, listaPlanificacionMultiple, listaSolicitudVacacion);
		}else{
			CargarListaPlanificacionVacia(listaEmpleado, listaIdEmpleado, listaSolicitudVacacion);
		}		
			
	}
	
	//Obtener Lista Id de Empleados
	private ArrayList<Long> getListaIdEmpleados(List <Empleado> listaEmpleado){
		ArrayList <Long> listaIdEmpleado = new ArrayList <Long> ();
		
		for(Empleado e : listaEmpleado){	
			listaIdEmpleado.add(e.getEmplId());
		}
		
		return listaIdEmpleado;
	}
	
	//Obtener Lista Id de Planificacion Múltiple
	private ArrayList<Long> getListaIdPlanificacion(List<PlanificacionMultiple> listaPlanificacionMultiple){
		ArrayList <Long> listaIdPlanificacionMultiple = new ArrayList <Long> ();
		
		for(PlanificacionMultiple p : listaPlanificacionMultiple){	
			listaIdPlanificacionMultiple.add(p.getIdPlanificacionMultiple());
		}
		
		return listaIdPlanificacionMultiple;
	}
	
	//Buscar en BD Planificación Múltiple de empleados
	private List<PlanificacionMultiple> getListaPlanificacionMultipleBD(ArrayList <Long> listaIdEmpleado, Integer mes, Integer año){
		List<PlanificacionMultiple> listaPlanificacionMultiple = new ArrayList <PlanificacionMultiple> ();
		
		planificacionMultipleList.setListaIdEmpleado(listaIdEmpleado);
		planificacionMultipleList.getPlanificacionMultiple().setMes(mes);
		planificacionMultipleList.getPlanificacionMultiple().setAnio(año);
		
		listaPlanificacionMultiple=planificacionMultipleList.getListaPlanificacionMultiple();
		
		return listaPlanificacionMultiple;
	}
	
	//Buscar en BD Detalle de Planificación Múltiple de empleados
	private List<DetallePlanificacionMultiple> getListaDetallePlanificacionMultipleBD(ArrayList <Long> listaIdPlanificacionMultiple){
		List<DetallePlanificacionMultiple> listaDetallePlanificacionMultiple = new ArrayList <DetallePlanificacionMultiple> ();
		
		detallePlanificacionMultipleList.setListaIdPlanificacionMultiple(listaIdPlanificacionMultiple);
		detallePlanificacionMultipleList.setDiaDesde(getDia(fechaDesde));
		detallePlanificacionMultipleList.setDiaHasta(getDia(fechaHasta));
		
		listaDetallePlanificacionMultiple=detallePlanificacionMultipleList.getListaDetallePlanificacionMultiple();
		
		return listaDetallePlanificacionMultiple;
	}
	
	//Buscar en BD Asistencia de empleados
	private List<Asistencia> getListaAsistenciaEmpleados(ArrayList <Long> listaIdEmpleado, Date fechaDesde, Date fechaHasta){
		
		List<Asistencia> listaAsistencia = new ArrayList <Asistencia> ();
		
		listaAsistencia=buscarAsistencia(listaIdEmpleado, fechaDesde, fechaHasta);
		
		return listaAsistencia;
	}
	
	//Obtener Solicitudes Vacaciones por Empleado
	private List<SolicitudVacacion> getListaSolicitudVacacionEmpleado(List<SolicitudVacacion> listaSolicitudVacacion, Empleado empleado){
		List<SolicitudVacacion> listaSolicitudVacacionEmpleado=new ArrayList <SolicitudVacacion>();
		
		Long idEmpleado=empleado.getEmplId();
		
		for(SolicitudVacacion sv: listaSolicitudVacacion){
			if(sv.getEmpleadoByEmplId().getEmplId().equals(idEmpleado))
				listaSolicitudVacacionEmpleado.add(sv);
		}
		
		return listaSolicitudVacacionEmpleado;
	}
	
	//Validar Dias de Vacaciones por Empleado
	private List<Integer> getDiasVacacionesEmpleado(List<SolicitudVacacion> solicitudesEmpleado){
		
		List<Integer> diasTotales =new ArrayList<Integer>();
		
		for(SolicitudVacacion sv: solicitudesEmpleado){
			List<Integer> dias=getDiasVacaciones(sv.getFechaDesde(), sv.getFechaHasta());
			for(int i=0;i<dias.size();i++){
				diasTotales.add(dias.get(i));
			}
		}
				
		return diasTotales;
	}
	
	//Gerarar dias de vacaciones de acuerdo a las fechas de ingreso por solicitud de vacaciones
	private List<Integer> getDiasVacaciones(Date fechaInicial, Date fechaFinal){
		List<Integer> dias =new ArrayList<Integer>();
		
		int añoDesde=getAño(fechaDesde);
		int añoHasta=getAño(fechaHasta);
		
		int mesDesde=getMes(fechaDesde);
		int mesHasta=getMes(fechaHasta);
		
		int diaDesde=getDia(fechaDesde);
		int diaHasta=getDia(fechaHasta);
				
		int añoInicial=getAño(fechaInicial);
		int añoFinal=getAño(fechaFinal);
		
		int mesInicial=getMes(fechaInicial);
		int mesFinal=getMes(fechaFinal);
		
		int diaInicial=getDia(fechaInicial);
		int diaFinal=getDia(fechaFinal);
		
		int inicio=0;
		int fin=0;
		
		if( añoInicial==añoDesde && añoFinal==añoHasta ){
			
			if( mesInicial<=mesDesde && mesFinal>=mesHasta){
					
				if(mesInicial==mesDesde)
					inicio=diaInicial;
				else
					inicio=diaDesde;
					
				if(mesFinal==mesHasta)
					fin=diaFinal;
				else
					fin=diaHasta;
				
			}	
			
		}else{
			
			if(añoInicial < añoFinal){
				
				if(añoInicial==añoDesde){
					
					if(mesInicial<=mesDesde){
						
						if(mesInicial==mesDesde)
							inicio=diaInicial;
						else
							inicio=diaDesde;
						
						fin=diaHasta;
					}
					
				}else{
					if(añoFinal==añoDesde){
						
						if(mesFinal>=mesHasta){
							
							if(mesFinal==mesHasta)
								fin=diaFinal;
							else
								fin=diaHasta;
							
							inicio=diaDesde;
						}
						
					}
				}
				
			}
						
		}
		
		
		if( fin>=inicio && inicio!=0 && fin!=0 ){
			for(int i=inicio;i<=fin;i++){
				dias.add(i);
			}
		}
		
		return dias;
	}
	
	//Resolver dias de vacaciones en lista Front-End
	private PlantillaPlanificacionMultiple ResolverVacaciones(int dia, PlantillaPlanificacionMultiple plantilla){
		
		SelectItem item = new SelectItem("V","V");
		
		switch(dia){
		
			case 1:plantilla.setHorario1(item);break;
			case 2:plantilla.setHorario2(item);break;
			case 3:plantilla.setHorario3(item);break;
			case 4:plantilla.setHorario4(item);break;
			case 5:plantilla.setHorario5(item);break;
			case 6:plantilla.setHorario6(item);break;
			case 7:plantilla.setHorario7(item);break;
			case 8:plantilla.setHorario8(item);break;
			case 9:plantilla.setHorario9(item);break;
			case 10:plantilla.setHorario10(item);break;
			case 11:plantilla.setHorario11(item);break;
			case 12:plantilla.setHorario12(item);break;
			case 13:plantilla.setHorario13(item);break;
			case 14:plantilla.setHorario14(item);break;
			case 15:plantilla.setHorario15(item);break;
			case 16:plantilla.setHorario16(item);break;
			case 17:plantilla.setHorario17(item);break;
			case 18:plantilla.setHorario18(item);break;
			case 19:plantilla.setHorario19(item);break;
			case 20:plantilla.setHorario20(item);break;
			case 21:plantilla.setHorario21(item);break;
			case 22:plantilla.setHorario22(item);break;
			case 23:plantilla.setHorario23(item);break;
			case 24:plantilla.setHorario24(item);break;
			case 25:plantilla.setHorario25(item);break;
			case 26:plantilla.setHorario26(item);break;
			case 27:plantilla.setHorario27(item);break;
			case 28:plantilla.setHorario28(item);break;
			case 29:plantilla.setHorario29(item);break;
			case 30:plantilla.setHorario30(item);break;
			case 31:plantilla.setHorario31(item);break;
			
		}
		
		return plantilla;		
	}
	
	//Cargar Lista de Front-End Vacía
	private void CargarListaPlanificacionVacia(List <Empleado> listaEmpleado, ArrayList <Long> listaIdEmpleado, List<SolicitudVacacion> listaSolicitudVacacion){
		Integer i=1;		
		for(Empleado e : listaEmpleado){			
			
			PlantillaPlanificacionMultiple plantilla=new PlantillaPlanificacionMultiple();	
			
			plantilla.setNumero(i);
			plantilla.setEmpleado(e);
			plantilla.setEstado("0");	
			plantilla.setTotalHoras(0);
			
			//
			List<SolicitudVacacion> solicitudesEmpleado=getListaSolicitudVacacionEmpleado(listaSolicitudVacacion,e);			
			List<Integer> diaVacacionesEmpleado = getDiasVacacionesEmpleado(solicitudesEmpleado);
			
			for(int j=0;j<diaVacacionesEmpleado.size();j++){
				int dia=diaVacacionesEmpleado.get(j);
				plantilla=ResolverVacaciones(dia, plantilla);
			}			
			//
			
			listaPlanificacion.add(plantilla);
			
			i++;
		}
	}
	
	//Cargar Lista de Front-End en Base a registros de BD
	private void CargarListaPlanificacionBD(List <Empleado> listaEmpleado,List<PlanificacionMultiple> listaPlanificacionMultiple, List<SolicitudVacacion> listaSolicitudVacacion){
		
		List <DetallePlanificacionMultiple>listaDetallePlanificacionMultiple=getListaDetallePlanificacionMultipleBD(getListaIdPlanificacion(listaPlanificacionMultiple));
				
		Integer i=1;		
		for(Empleado e : listaEmpleado){			
			PlantillaPlanificacionMultiple plantilla=new PlantillaPlanificacionMultiple();	
			
			plantilla.setNumero(i);
			plantilla.setEmpleado(e);
			plantilla.setEstado("0");
			
			for(PlanificacionMultiple p: listaPlanificacionMultiple){
				if(p.getIdEmpleado().equals(e.getEmplId())){
					
					for(DetallePlanificacionMultiple d: listaDetallePlanificacionMultiple){
						if(d.getIdPlanificacionMultiple().equals(p.getIdPlanificacionMultiple())){
							plantilla=getRegistrosDetallePlanificacionMultiple(plantilla,d);
						}
					}	
					plantilla.setEstado("1");
					break;
				}
			}
			
			//*****************************************************************
			List<SolicitudVacacion> solicitudesEmpleado=getListaSolicitudVacacionEmpleado(listaSolicitudVacacion,e);			
			List<Integer> diaVacacionesEmpleado = getDiasVacacionesEmpleado(solicitudesEmpleado);
			
			for(int j=0;j<diaVacacionesEmpleado.size();j++){
				int dia=diaVacacionesEmpleado.get(j);
				plantilla=ResolverVacaciones(dia, plantilla);
			}			
			//*****************************************************************
			
			plantilla.setTotalHoras(getTotalHorasPlanificadas(plantilla));
			
			listaPlanificacion.add(plantilla);
			
			i++;
		}
	}
	
	//Obtener total de horas planificadas por empleado
	private double getTotalHorasPlanificadas(PlantillaPlanificacionMultiple plantilla){
		double total=0;
		
		total+=BuscarDetalleHorarioDias(plantilla.getHorario1());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario2());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario3());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario4());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario5());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario6());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario7());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario8());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario9());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario10());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario11());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario12());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario13());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario14());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario15());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario16());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario17());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario18());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario19());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario20());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario21());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario22());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario23());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario24());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario25());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario26());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario27());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario28());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario29());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario30());
		total+=BuscarDetalleHorarioDias(plantilla.getHorario31());
				
		return total;
	}
	
	//Obtener Lista detalle horario
	private double BuscarDetalleHorarioDias(SelectItem item){
		double horas=0;
		if(!(item.getValue()==null)){
			if( !(item.getValue().equals("V")) ){
				Long horaId=Long.parseLong(""+item.getValue());				
				List<DetalleHorario> lista =BuscarListaDetallehorario(horaId);	
				horas=getHorasListaDetalleHorario(lista,horaId);	
			}					
		}		
		return horas;
	}
	
	//Calcular horas lista detalle horario
	private double getHorasListaDetalleHorario(List<DetalleHorario> lista, Long horaId){
		
		double totalDetalle=0;
		double totalAuxiliar=0;
		
		Date fechaInicial=null;
		Date fechaFinal=null;
		
		for(DetalleHorario dh:lista){
			String estadoHorario=dh.getHorario().getEstado();
			estadoHorario=estadoHorario.trim();
			if(estadoHorario.equals("Laborable")){				
				//log.info("-------------------dh.order: "+dh.getOrden()+" -------------- EntradaSalida: "+dh.getEntradaSalida()+" --------- dg.hora: "+dh.getHora());
				String tipo=dh.getEntradaSalida();
				tipo=tipo.trim();
				
				if(tipo.equals("E")/*||tipo.equals("EA")*/){
					fechaInicial=dh.getHora();
				}if(tipo.equals("S")/*||tipo.equals("SA")*/){
						fechaFinal=dh.getHora();
						
						totalAuxiliar=DiferenciaFechasHoras(fechaInicial, fechaFinal,dh.isNocturno());
						totalDetalle+=totalAuxiliar;
						
						if(totalDetalle>0){
							double minutosAlmuerzo=BuscarMinutosAlmuerzoHorario(horaId);
							minutosAlmuerzo=minutosAlmuerzo/60;
							//log.info("--------------------------------------minutosAlmuerzo: "+minutosAlmuerzo);
							totalDetalle=totalDetalle-minutosAlmuerzo;
						}							
						
						break;
						/*fechaInicial=null;
						fechaFinal=null;*/
				}
				
			}	
		}
		
		totalDetalle=redondearDecimales(totalDetalle,2);
		
		return totalDetalle;
	}
	
	//redondearDecimales
	private double redondearDecimales( double numero, int decimales ) {
	    return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
	}
	
	//Diferencia de dos fechas en horas
	private double DiferenciaFechasHoras(Date fechaInicial, Date fechaFinal, boolean nocturno) {
		
		double diferenciaHoras=0;
		
		try{
			double minutoInicial=getMinuto(fechaInicial);
			minutoInicial=minutoInicial/60;
			double horasInicial=getHora(fechaInicial);
			horasInicial+=minutoInicial;
			
			double minutoFinal=getMinuto(fechaFinal);
			minutoFinal=minutoFinal/60;
			double horasFinal=getHora(fechaFinal);
			horasFinal+=minutoFinal;
			
			diferenciaHoras=0;
			
			if(nocturno)
				diferenciaHoras = (24 - horasInicial) + horasFinal;
			else
				diferenciaHoras = horasFinal-horasInicial;
			//log.info("------------------fechaInicial: "+fechaInicial+" fechaFinal:"+fechaFinal+" diferenciaHoras: "+diferenciaHoras +"   is nocturno: "+nocturno+ " horasInicial: "+horasInicial+ " horasFinal: "+horasFinal);
		}catch(Exception ex){
			log.info("********************Error en el cálculo de diferencia de fechas********************");
			ex.printStackTrace();			
		}		

		return diferenciaHoras;
	}
	
	//Buscar en lista detalle horario por hora_id
	private List<DetalleHorario> BuscarListaDetallehorario(Long horaId){
		List<DetalleHorario>  lista=new ArrayList<DetalleHorario>();
		
		for(DetalleHorario dh:this.listaDetalleHorario){
			if(dh.getHorario().getHoraId().equals(horaId)){
				lista.add(dh);
			}
		}
		
		return lista;
	}
	
	//Buscar minutosAlmuerzo de Horario
	private int BuscarMinutosAlmuerzoHorario(Long horaId){
		int minutosAlmuerzo=0;
		
		for(Horario h:this.listaHorario){
			if(h.getHoraId().equals(horaId)){
				minutosAlmuerzo=h.getAlmuerzoMinutos();
				break;
			}
		}
		
		return minutosAlmuerzo;
	}
	
	//Obtener registros Detalle Planificación Múltiple
	private PlantillaPlanificacionMultiple getRegistrosDetallePlanificacionMultiple(PlantillaPlanificacionMultiple plantilla, DetallePlanificacionMultiple detallePlanificacionMultiple){
		Integer dia=detallePlanificacionMultiple.getDia();
		Integer idHorario=detallePlanificacionMultiple.getIdHorario();
		String codigoHorario=detallePlanificacionMultiple.getCodigoHorario();
		SelectItem item = new SelectItem(""+idHorario,codigoHorario);
		
		switch(dia){		
			case(1):plantilla.setHorario1(item);
					break;
			case(2):plantilla.setHorario2(item);
					break;
			case(3):plantilla.setHorario3(item);
					break;
			case(4):plantilla.setHorario4(item);
					break;
			case(5):plantilla.setHorario5(item);
					break;
			case(6):plantilla.setHorario6(item);
					break;
			case(7):plantilla.setHorario7(item);
					break;
			case(8):plantilla.setHorario8(item);
					break;
			case(9):plantilla.setHorario9(item);
					break;
			case(10):plantilla.setHorario10(item);
					break;
			case(11):plantilla.setHorario11(item);
					break;
			case(12):plantilla.setHorario12(item);
					break;
			case(13):plantilla.setHorario13(item);
					break;
			case(14):plantilla.setHorario14(item);
					break;
			case(15):plantilla.setHorario15(item);
					break;
			case(16):plantilla.setHorario16(item);
					break;
			case(17):plantilla.setHorario17(item);
					break;
			case(18):plantilla.setHorario18(item);
					break;
			case(19):plantilla.setHorario19(item);
					break;
			case(20):plantilla.setHorario20(item);
					break;
			case(21):plantilla.setHorario21(item);
					break;
			case(22):plantilla.setHorario22(item);
					break;
			case(23):plantilla.setHorario23(item);
					break;
			case(24):plantilla.setHorario24(item);
					break;
			case(25):plantilla.setHorario25(item);
					break;
			case(26):plantilla.setHorario26(item);
					break;
			case(27):plantilla.setHorario27(item);
					break;
			case(28):plantilla.setHorario28(item);
					break;
			case(29):plantilla.setHorario29(item);
					break;
			case(30):plantilla.setHorario30(item);
					break;
			case(31):plantilla.setHorario31(item);
					break;		
		}
		
		return plantilla;
	}
	
	//Asignar un nombre a columnas de dias en la matriz	
	public String getDiaMatriz(int n){
		String diaMatriz="";		
		int dm[]={31,28,31,30,31,30,31,31,30,31,30,31};
		try{			
			if(fechaDesde==null){
				diaMatriz="";
			}else{
								
				int AÑO=getAño(fechaDesde);
				int MES =getMes(fechaDesde);
				int DIA=n;
				
				if(MES==1){
					if((AÑO%4)==0&&(AÑO%100)!=0){
						dm[1]=dm[1]+1;
					}else if((AÑO%100)==0&&(AÑO%400)==0){
						dm[1]=dm[1]+1;
					}
				}				
				
				int limite1=getDia(fechaDesde);
				int limite2=getDia(fechaHasta);
				
				if(DIA<=dm[MES]&&DIA>=limite1&&DIA<=limite2){
									
					Calendar calendar=getCalendar();
					calendar.set(AÑO,MES,DIA);
					
					diaMatriz=""+calendar.getTime();
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
	
	//Habilitar columnas de dias en la matriz	
	public boolean getHabilitarDiaMatriz(int n){
		boolean habilitar;		
		int dm[]={31,28,31,30,31,30,31,31,30,31,30,31};
		try{			
			if(fechaDesde==null){
				habilitar=false;
			}else{
				
				int AÑO=getAño(fechaDesde);
				int MES =getMes(fechaDesde);
				int DIA=n;
				
				if(MES==1){
					if((AÑO%4)==0&&(AÑO%100)!=0){
						dm[1]=dm[1]+1;
					}else if((AÑO%100)==0&&(AÑO%400)==0){
						dm[1]=dm[1]+1;
					}
				}				
				
				int limite1=getDia(fechaDesde);
				int limite2=getDia(fechaHasta);
				
				if(DIA<=dm[MES]&&DIA>=limite1&&DIA<=limite2){
					habilitar=true;
				}else{
					habilitar=false;
				}				
			}
		}catch(Exception e){
			habilitar=false;
		}		
		return habilitar;
	}
	
	//Obtener variable Calendar en base a una fecha
	public Calendar getCalendar(Date date){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}
	
	//Obtener variable Calendar vacía
	public Calendar getCalendar(){
		Calendar calendar=Calendar.getInstance();
		calendar.clear();
		return calendar;
	}
	
	//Obtener año de una fecha
	private int getAño(Date fecha){		
		int año=getCalendar(fecha).get(Calendar.YEAR);
		return año;
	}
	
	//Obtener mes de una fecha
	@SuppressWarnings("deprecation")
	private int getMes(Date fecha){				
		int mes=Integer.parseInt(""+fecha.getMonth());
		return mes;
	}
	
	//Obtener dia de una fecha
	@SuppressWarnings("deprecation")
	private int getDia(Date fecha){
		int dia=fecha.getDate();
		return dia;
	}
	
	//Obtener hora de una fecha
	@SuppressWarnings("deprecation")
	private int getHora(Date fecha){
		int dia=fecha.getHours();
		return dia;
	}
	
	//Obtener minuto de una fecha
	@SuppressWarnings("deprecation")
	private int getMinuto(Date fecha){
		int dia=fecha.getMinutes();
		return dia;
	}
	
	//Obtener límite inferior del mes
	private Date getLimiteInferior(){
		Date fecha=new Date();
		
		int año=getAño(fechaDesde);
		int mes=getMes(fechaDesde);
		int dia=1;
		
		Calendar calendar=getCalendar();
		calendar.set(Calendar.YEAR,año);
		calendar.set(Calendar.MONTH,mes);
		calendar.set(Calendar.DATE,dia);
		
		fecha=calendar.getTime();
		
		return fecha;
	}
	
	//Obtener límite superior del mes
	private Date getLimiteSuperior(){
		Date fecha=new Date();
		Calendar auxiliar=getCalendar(fechaHasta);
		
		int año=getAño(fechaHasta);
		int mes=getMes(fechaHasta);
		int dia=auxiliar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		Calendar calendar=getCalendar();
		calendar.set(Calendar.YEAR,año);
		calendar.set(Calendar.MONTH,mes);
		calendar.set(Calendar.DATE,dia);
		
		fecha=calendar.getTime();
		
		return fecha;
	}	
	
	//Guardar registros por Empleado y actualizar lista Front-End
	public void RegistrarEmpleado(){
		//Actualizar registros lista Front-End
		GuardarListaEmpleado();		
		//Actualizar Registros BD
		GuardarRegistrosEmpleado();
		log.info(".............................GOOD 3!!!!!!!!!!!!!!!");
	}
	
	//Guardar registros por Empleado y actualizar lista Front-End
	public void ProcesarEmpleado(){
		VaciarRegistros();
		//Actualizar registros lista Front-End
		GuardarListaEmpleado();
		//Actualizar Registros BD
		GuardarRegistrosEmpleado();	
		//Actualizar Registros de Asistencia BD
		GuardarRegistrosAsistencia();
	}
	
	//*****//Actualizar Registros de Asistencia BD
	private void GuardarRegistrosAsistencia(){
		
		//Cargar Lista id Empleados
		ArrayList<Long> listaIdEmpleado=new ArrayList<Long>();		
		for(PlantillaPlanificacionMultipleEmpleado pm: listaPlanificacionEmpleado){
			listaIdEmpleado.add(pm.getEmpleado().getEmplId());
		}
		
		//Consultar Asistencia BD en base a id Empleados y rango de fechas
		List<Asistencia> listaAsistencia=getListaAsistenciaEmpleados(listaIdEmpleado,this.fechaDesde,this.fechaHasta);		
		
		//Obtener Empleado a modificar registros Asistencia
		PlantillaPlanificacionMultiple plantilla;		
		int i=filaEdit-1;
		plantilla=listaPlanificacion.get(i);
		
		//Obtener lista de dias y horarios de asistencia de Empleado
		List<SelectItem> diasHorariosEmpleado=ComprobarRegistrosAsistencia(listaAsistencia, plantilla);
		
		getAsistenciaInsert().clear();
		
		CompararRegistrosAsistencia(diasHorariosEmpleado, listaAsistencia, plantilla);
		
		gestionarPlanificacion.InsertarRegistros(asistenciaInsert);		
		
	}
	
	//Comparar Registro Asistencia vs Lista de Empleado
	private void CompararRegistrosAsistencia(List<SelectItem> selectItem, List<Asistencia> listaAsistencia, PlantillaPlanificacionMultiple plantilla){
		
		List<Asistencia> listaAsistenciaDia=new ArrayList<Asistencia>();
		
		Empleado empleado=plantilla.getEmpleado();
		String valueEmpleado="";
		int dia=1;
		
		for(SelectItem s: selectItem){
			listaAsistenciaDia.clear();
			String valueAsistencia=""+s.getLabel();
			
			switch (dia){
			
				case 1:	
					valueEmpleado=""+plantilla.getHorario1().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(1,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(1, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 2:	
					valueEmpleado=""+plantilla.getHorario2().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(2,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(2, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 3:	
					valueEmpleado=""+plantilla.getHorario3().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(3,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(3, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 4:	
					valueEmpleado=""+plantilla.getHorario4().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(4,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(4, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 5:	
					valueEmpleado=""+plantilla.getHorario5().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(5,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(5, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 6:	
					valueEmpleado=""+plantilla.getHorario6().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(6,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(6, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 7:	
					valueEmpleado=""+plantilla.getHorario7().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(7,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(7, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 8:	
					valueEmpleado=""+plantilla.getHorario8().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(8,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(8, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 9:	
					valueEmpleado=""+plantilla.getHorario9().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(9,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(9, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 10:	
					valueEmpleado=""+plantilla.getHorario10().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(10,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(10, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 11:	
					valueEmpleado=""+plantilla.getHorario11().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(11,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(11, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 12:	
					valueEmpleado=""+plantilla.getHorario12().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(12,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(12, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 13:	
					valueEmpleado=""+plantilla.getHorario13().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(13,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(13, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 14:	
					valueEmpleado=""+plantilla.getHorario14().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(14,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(14, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 15:	
					valueEmpleado=""+plantilla.getHorario15().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(15,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(15, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 16:	
					valueEmpleado=""+plantilla.getHorario16().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(16,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(16, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 17:	
					valueEmpleado=""+plantilla.getHorario17().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(17,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(17, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 18:	
					valueEmpleado=""+plantilla.getHorario18().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(18,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(18, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 19:	
					valueEmpleado=""+plantilla.getHorario19().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(19,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(19, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 20:	
					valueEmpleado=""+plantilla.getHorario20().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(20,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(20, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 21:	
					valueEmpleado=""+plantilla.getHorario21().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(21,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(21, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 22:	
					valueEmpleado=""+plantilla.getHorario22().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(22,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(22, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 23:	
					valueEmpleado=""+plantilla.getHorario23().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(23,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(23, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 24:	
					valueEmpleado=""+plantilla.getHorario24().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(24,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(24, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 25:	
					valueEmpleado=""+plantilla.getHorario25().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(25,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(25, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 26:	
					valueEmpleado=""+plantilla.getHorario26().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(26,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(26, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 27:	
					valueEmpleado=""+plantilla.getHorario27().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(27,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(27, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 28:	
					valueEmpleado=""+plantilla.getHorario28().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(28,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(28, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 29:	
					valueEmpleado=""+plantilla.getHorario29().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(29,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(29, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 30:	
					valueEmpleado=""+plantilla.getHorario30().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(30,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(30, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
				
				case 31:	
					valueEmpleado=""+plantilla.getHorario31().getValue();
					listaAsistenciaDia=getListaAsistenciaDias(31,listaAsistencia);
					CompararRegistrosIndividualesAsistencia(31, valueEmpleado, valueAsistencia, listaAsistenciaDia, empleado);
				break;
			
			}
			
			dia++;
		}
		
	}
	
	private List<Asistencia> getListaAsistenciaDias(int dia, List<Asistencia> listaAsistencia){
		
		List<Asistencia> lista=new ArrayList<Asistencia>();
		
		for(Asistencia a: listaAsistencia){
			int diaAsistencia=getDia(a.getFechaHorario());
			if(diaAsistencia==dia){
				lista.add(a);
			}
		}
		
		return lista;
	}
	
	//Comparar Registro a Registro de Asistencia vs Lista Empleado
	private void CompararRegistrosIndividualesAsistencia(int dia, String value1, String value2, List<Asistencia> listaAsistencia,Empleado empleado){
		
		value1=value1.trim();
		value2=value2.trim();
		
		if(value1!=null){
			if( (!(value1.equals("null"))) && (!(value1.equals("empty")))  && (!(value1.equals("V"))) ){				
				
				if(value2.equals("NA")){
					//crear
					Long idHorario=Long.parseLong(value1);
					List<DetalleHorario> listaDetalleHorario= BuscarListaDetallehorario(idHorario);					
					CrearAsistenciaAlmacenada(dia, listaDetalleHorario, empleado, getFechaActual());
					
				}else{
					if(!(value1.equals(value2))){
						//Eliminar
						GestionarListaAsistencia(listaAsistencia,"delete");
						//Crear
						Long idHorario=Long.parseLong(value1);
						List<DetalleHorario> listaDetalleHorario= BuscarListaDetallehorario(idHorario);						
						CrearAsistenciaAlmacenada(dia, listaDetalleHorario, empleado, getFechaActual());					
					}else{
						registrosNoProcesados+=listaAsistencia.size();
					}
				}
				
			}else{
				if(!(value2.equals("NA"))){
					//Eliminar
					GestionarListaAsistencia(listaAsistencia,"delete");
				}
			}
		}else{
			if(!(value2.equals("NA"))){
				//Eliminar
				GestionarListaAsistencia(listaAsistencia,"delete");
			}
		}
		
	}
	
	//Obtener fecha del mes
	private Date getFechaActual(){
		Date fecha=new Date();
		
		int año=getAño(fechaDesde);
		int mes=getMes(fechaDesde);
		
		Calendar calendar=getCalendar();
		calendar.set(Calendar.YEAR,año);
		calendar.set(Calendar.MONTH,mes);
		
		fecha=calendar.getTime();
		
		return fecha;
	}
	
	//Buscar detallehorario por id Horario
	@SuppressWarnings("deprecation")
	private void CrearAsistenciaAlmacenada(int dia, List<DetalleHorario> listaDetalleHorario,Empleado empleado, Date fecha){
		
		Long spEmpleadoId=empleado.getEmplId();
		String spCodigoEmpleado=empleado.getCodigoEmpleado();
		
		for(DetalleHorario dh: listaDetalleHorario){	
			
			PlantillaPlanificacion plantilla= new PlantillaPlanificacion();			
					
			String tipo=dh.getHorario().getEstado();
			
			//*******Generar Fecha y Hora del Horario
			Date date=fecha;
			date.setDate(dia);
			
			Calendar calendar=getCalendar(date);		
			
			if (dh.isNocturno()) {
				calendar = Fechas.sumarRestarDias(calendar, 1);
			}	
			
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fechaCadena = Fechas.cambiarFormato(asistenciaHome.crearFechasAsistencia(calendar, getHora(dh.getHora()), getMinuto(dh.getHora())), "yyyy-MM-dd HH:mm:ss");

			Date fechaHoraHorario = null;
			Calendar dateAndTime=null;
			
			try {
				fechaHoraHorario =  formato.parse(fechaCadena);		
				dateAndTime=getCalendar(fechaHoraHorario);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//*******
			
			Long spDhId=dh.getDehoId();
			//Date spFechaHoraHorario=fechaHoraHorario;
			String spEntradaSalida=dh.getEntradaSalida();
			spEntradaSalida=spEntradaSalida.trim();
			int spOrden= dh.getOrden();
			int spMaxMinuto=dh.getMaxMinuto();
			String spEstado=getEstado(tipo);
			
			Date spFechaHorario=date;		
			Date spHoraHorario=dateAndTime.getTime();	
			
			String cadenaFechaHoraHorario=fechaCadena;
			String cadenaFechaHorario=""+getAño(spFechaHorario)+"-"+(getMes(spFechaHorario)+1)+"-"+getDia(spFechaHorario);
			String candenaHoraHorario=""+getHora(spHoraHorario)+":"+getMinuto(spHoraHorario)+":00";
				
			if( !(spEstado.equals("V")) ){

				plantilla.setEmpId(spEmpleadoId);
				plantilla.setDhId(spDhId);
				plantilla.setFechaHoraHorario(cadenaFechaHoraHorario);
				plantilla.setEntradaSalida(spEntradaSalida);
				plantilla.setCodigoEmpleado(spCodigoEmpleado);
				plantilla.setOrden(spOrden);
				plantilla.setMaxMinuto(spMaxMinuto);
				plantilla.setEstado(spEstado);
				plantilla.setFechaHorario(cadenaFechaHorario);
				plantilla.setHoraHorario(candenaHoraHorario);
				
				asistenciaInsert.add(plantilla);
				
				/*InsertarRegistrosAsistencia(spEmpleadoId, spDhId, cadenaFechaHoraHorario, spEntradaSalida, spCodigoEmpleado, spOrden, spMaxMinuto, spEstado, cadenaFechaHorario, candenaHoraHorario);
				*/				
				registrosProcesados+=1;
			}
			
		}
	}
	
	/*private void InsertarRegistrosAsistencia(Long empId, Long dhId, String fechaHoraHorario, String entradaSalida, String codigoEmpleado, 
			int orden, int maxMinuto, String estado, String fechaHorario, String horaHorario){
		
		String ejecucion=getTipoEjecucion();
		String sentenciaNativa = ejecucion+"spAsistenciaEmpleado(:empleado, :detalle, :fechaHoraHorario, :entradaSalida, :codigoEmpleado, :orden , :maxMinuto , :estado, :fechaHorario, :horaHorario)";
		
		Query query =  entityManager.createNativeQuery(sentenciaNativa)		
				.setParameter("empleado",empId)
				.setParameter("detalle",dhId)
				.setParameter("fechaHoraHorario",fechaHoraHorario)
				.setParameter("entradaSalida",entradaSalida)
				.setParameter("codigoEmpleado",codigoEmpleado)
				.setParameter("orden",orden)
				.setParameter("maxMinuto",maxMinuto)
				.setParameter("estado",estado)
				.setParameter("fechaHorario",fechaHorario)
				.setParameter("horaHorario",horaHorario);
	
		EjecutarConsulta(query);
		
	}*/
	
	private void EliminarRegistrosAsistencia(ArrayList<Long> asisId){
		
		String sentenciaNativa = "delete from asistencia where asis_id in (:asisId) and estado not like 'V%'";
		
		Query query =  entityManager.createNativeQuery(sentenciaNativa)	
				.setParameter("asisId",asisId);
	
		query.executeUpdate();
		
	}
	
	private void EliminarRegistrosPlanificacionMultiple(ArrayList<Long> listaidPlanificacion){
		
		String sentenciaNativa = "delete from planificacion_multiple where id_pla_mul in (:idPlanificacion)";
		
		Query query =  entityManager.createNativeQuery(sentenciaNativa)	
				.setParameter("idPlanificacion",listaidPlanificacion);
	
		query.executeUpdate();		
	}	
	
	private void EliminarRegistrosDetallePlanificacionMultiple(ArrayList<Long> listaidDetalle){
		
		String sentenciaNativa = "delete from detalle_planificacion_multiple where id_det_pla_mul in (:idDetalle)";
		
		Query query =  entityManager.createNativeQuery(sentenciaNativa)	
				.setParameter("idDetalle",listaidDetalle);
	
		query.executeUpdate();		
	}
	
	/*private String getTipoEjecucion(){
		String llamada="";
		
		if(dataBase.equals("MYSQL"))
			llamada="CALL ";
		else
			if(dataBase.equals("SQLSERVER"))
				llamada="EXEC ";
			else
				if(dataBase.equals("ORACLE"))
					llamada="CALL ";
				else
					if(dataBase.equals("POSTGRES"))
						llamada="SELECT ";
		
		return llamada;
	}*/
	
	/*private void EjecutarConsulta(Query query ){
		if(dataBase.equals("MYSQL"))
			query.executeUpdate();
			
		else
			if(dataBase.equals("SQLSERVER"))
				query.executeUpdate();
			else
				if(dataBase.equals("ORACLE"))
					query.executeUpdate();
				else
					if(dataBase.equals("POSTGRES"))
						query.getSingleResult();
	}*/
	
	//Obtener estado del tipo de horario
	private String getEstado(String tipo){
		String estado="";
		
		tipo=tipo.trim();
		
		if(tipo.equals("Laborable"))
			estado="FT";
		else
			if(tipo.equals("Libre"))
				estado="L";
			else
				if(tipo.equals("Feriado"))
					estado="FD";
				else
					if(tipo.equals("Permiso"))
						estado="P";
					else
						if(tipo.equals("Vacaciones"))
							estado="V";
		
		return estado;
	}
	
	//Comprobar Registros de Asistencia con registros de lista Front-End
	private List<SelectItem> ComprobarRegistrosAsistencia(List<Asistencia> listaAsistencia, PlantillaPlanificacionMultiple plantilla){		
		
		Long idEmpPla=plantilla.getEmpleado().getEmplId();
		List<SelectItem> diasHorarios=getListaDiasHorarios();
		
		for(Asistencia a: listaAsistencia){
			Long idEmpAsi=a.getEmpleado().getEmplId();
			if(idEmpPla.equals(idEmpAsi)){
				
				int diaAsistencia=getDia(a.getFechaHorario());
				diasHorarios=ComprobarAsistenciaDias(diaAsistencia,a,diasHorarios);	
				
			}
		}
		
		return diasHorarios;
		
	}
	
	//Obtener Lista de Asistencia por Empleados
	private List<Asistencia> ObtenerListaAsistenciaEmpleado(List<Asistencia> listaAsistencia, Long idEmpl){		
		
		List<Asistencia> listaAsistenciaEmpleado=new ArrayList<Asistencia>();
		
		for(Asistencia a: listaAsistencia){
			Long idEmpAsi=a.getEmpleado().getEmplId();
			if(idEmpl.equals(idEmpAsi)){
				listaAsistenciaEmpleado.add(a);
			}
		}
		
		return listaAsistenciaEmpleado;
		
	}
	
	//Generacion de Lista de Dias y Horarios de asistencia por empleado	
	private List<SelectItem> getListaDiasHorarios(){
		List<SelectItem> listaSelectItem =new ArrayList<SelectItem>();
		SelectItem selectItem;
		for(int i=1;i<=31;i++){
			selectItem=new SelectItem(""+i,"NA");
			listaSelectItem.add(selectItem);
		}
		return listaSelectItem;
	}
	
	//Comprobar Asistencia por dia
	private List<SelectItem> ComprobarAsistenciaDias(int dia, Asistencia asistencia, List<SelectItem> listaSelectItem){
		Long idHorarioAsis=asistencia.getDetalleHorario().getHorario().getHoraId();
		
		switch(dia){
		
			case 1:	listaSelectItem=setListaDiasHorarios(listaSelectItem,0,idHorarioAsis);
					break;	
			case 2:	listaSelectItem=setListaDiasHorarios(listaSelectItem,1,idHorarioAsis);
					break;	
			case 3:	listaSelectItem=setListaDiasHorarios(listaSelectItem,2,idHorarioAsis);
					break;	
			case 4:	listaSelectItem=setListaDiasHorarios(listaSelectItem,3,idHorarioAsis);
					break;	
			case 5:	listaSelectItem=setListaDiasHorarios(listaSelectItem,4,idHorarioAsis);
					break;	
			case 6:	listaSelectItem=setListaDiasHorarios(listaSelectItem,5,idHorarioAsis);
					break;	
			case 7:	listaSelectItem=setListaDiasHorarios(listaSelectItem,6,idHorarioAsis);
					break;	
			case 8:	listaSelectItem=setListaDiasHorarios(listaSelectItem,7,idHorarioAsis);
					break;	
			case 9:	listaSelectItem=setListaDiasHorarios(listaSelectItem,8,idHorarioAsis);
					break;	
			case 10:listaSelectItem=setListaDiasHorarios(listaSelectItem,9,idHorarioAsis);
					break;	
			case 11:listaSelectItem=setListaDiasHorarios(listaSelectItem,10,idHorarioAsis);
					break;	
			case 12:listaSelectItem=setListaDiasHorarios(listaSelectItem,11,idHorarioAsis);
					break;	
			case 13:listaSelectItem=setListaDiasHorarios(listaSelectItem,12,idHorarioAsis);
					break;	
			case 14:listaSelectItem=setListaDiasHorarios(listaSelectItem,13,idHorarioAsis);
					break;	
			case 15:listaSelectItem=setListaDiasHorarios(listaSelectItem,14,idHorarioAsis);
					break;	
			case 16:listaSelectItem=setListaDiasHorarios(listaSelectItem,15,idHorarioAsis);
					break;	
			case 17:listaSelectItem=setListaDiasHorarios(listaSelectItem,16,idHorarioAsis);
					break;	
			case 18:listaSelectItem=setListaDiasHorarios(listaSelectItem,17,idHorarioAsis);
					break;	
			case 19:listaSelectItem=setListaDiasHorarios(listaSelectItem,18,idHorarioAsis);
					break;	
			case 20:listaSelectItem=setListaDiasHorarios(listaSelectItem,19,idHorarioAsis);
					break;				
			case 21:listaSelectItem=setListaDiasHorarios(listaSelectItem,20,idHorarioAsis);
					break;
			case 22:listaSelectItem=setListaDiasHorarios(listaSelectItem,21,idHorarioAsis);
					break;	
			case 23:listaSelectItem=setListaDiasHorarios(listaSelectItem,22,idHorarioAsis);
					break;	
			case 24:listaSelectItem=setListaDiasHorarios(listaSelectItem,23,idHorarioAsis);
					break;	
			case 25:listaSelectItem=setListaDiasHorarios(listaSelectItem,24,idHorarioAsis);
					break;	
			case 26:listaSelectItem=setListaDiasHorarios(listaSelectItem,25,idHorarioAsis);
					break;	
			case 27:listaSelectItem=setListaDiasHorarios(listaSelectItem,26,idHorarioAsis);
					break;	
			case 28:listaSelectItem=setListaDiasHorarios(listaSelectItem,27,idHorarioAsis);
					break;	
			case 29:listaSelectItem=setListaDiasHorarios(listaSelectItem,28,idHorarioAsis);
					break;	
			case 30:listaSelectItem=setListaDiasHorarios(listaSelectItem,29,idHorarioAsis);
					break;				
			case 31:listaSelectItem=setListaDiasHorarios(listaSelectItem,30,idHorarioAsis);
					break;	
					
		}
		
		return listaSelectItem;
	}
	
	//Actualizar lista de dias y Horarios por Empleado
	private List<SelectItem> setListaDiasHorarios(List<SelectItem> listaSelectItem, int i, Long idHorario){
		String label=listaSelectItem.get(i).getLabel();		
		if(label.equals("NA")){
			listaSelectItem.get(i).setLabel(""+idHorario);
		}		
		return listaSelectItem;
	}
	
	//*****Actualizar Lista Front-End modificada
	private void GuardarListaEmpleado(){
	
		for(PlantillaPlanificacionMultipleEmpleado pm: listaPlanificacionEmpleado){
			
			int i=filaEdit-1;		
			
			PlantillaPlanificacionMultiple p=new PlantillaPlanificacionMultiple();
			PlantillaPlanificacionMultiple plantilla=listaPlanificacion.get(i);
			
			p.setEmpleado(pm.getEmpleado());
			p.setNumero(pm.getNumero());
			p.setEstado(pm.getEstado());
			
			p.setHorario1(VerificarLabel(value1,""+plantilla.getHorario1().getValue()));
			p.setHorario2(VerificarLabel(value2,""+plantilla.getHorario2().getValue()));
			p.setHorario3(VerificarLabel(value3,""+plantilla.getHorario3().getValue()));
			p.setHorario4(VerificarLabel(value4,""+plantilla.getHorario4().getValue()));
			p.setHorario5(VerificarLabel(value5,""+plantilla.getHorario5().getValue()));
			p.setHorario6(VerificarLabel(value6,""+plantilla.getHorario6().getValue()));
			p.setHorario7(VerificarLabel(value7,""+plantilla.getHorario7().getValue()));
			p.setHorario8(VerificarLabel(value8,""+plantilla.getHorario8().getValue()));
			p.setHorario9(VerificarLabel(value9,""+plantilla.getHorario9().getValue()));
			p.setHorario10(VerificarLabel(value10,""+plantilla.getHorario10().getValue()));
			p.setHorario11(VerificarLabel(value11,""+plantilla.getHorario11().getValue()));
			p.setHorario12(VerificarLabel(value12,""+plantilla.getHorario12().getValue()));
			p.setHorario13(VerificarLabel(value13,""+plantilla.getHorario13().getValue()));
			p.setHorario14(VerificarLabel(value14,""+plantilla.getHorario14().getValue()));
			p.setHorario15(VerificarLabel(value15,""+plantilla.getHorario15().getValue()));
			p.setHorario16(VerificarLabel(value16,""+plantilla.getHorario16().getValue()));
			p.setHorario17(VerificarLabel(value17,""+plantilla.getHorario17().getValue()));
			p.setHorario18(VerificarLabel(value18,""+plantilla.getHorario18().getValue()));
			p.setHorario19(VerificarLabel(value19,""+plantilla.getHorario19().getValue()));
			p.setHorario20(VerificarLabel(value20,""+plantilla.getHorario20().getValue()));
			p.setHorario21(VerificarLabel(value21,""+plantilla.getHorario21().getValue()));
			p.setHorario22(VerificarLabel(value22,""+plantilla.getHorario22().getValue()));
			p.setHorario23(VerificarLabel(value23,""+plantilla.getHorario23().getValue()));
			p.setHorario24(VerificarLabel(value24,""+plantilla.getHorario24().getValue()));
			p.setHorario25(VerificarLabel(value25,""+plantilla.getHorario25().getValue()));
			p.setHorario26(VerificarLabel(value26,""+plantilla.getHorario26().getValue()));
			p.setHorario27(VerificarLabel(value27,""+plantilla.getHorario27().getValue()));
			p.setHorario28(VerificarLabel(value28,""+plantilla.getHorario28().getValue()));
			p.setHorario29(VerificarLabel(value29,""+plantilla.getHorario29().getValue()));
			p.setHorario30(VerificarLabel(value30,""+plantilla.getHorario30().getValue()));
			p.setHorario31(VerificarLabel(value31,""+plantilla.getHorario31().getValue()));
			
			p.setTotalHoras(getTotalHorasPlanificadas(p));
		
			listaPlanificacion.set(i, p);
			
			break;
		}				
	}
	
	//Verificar Label para refrescar la Lista de Empleados
	public SelectItem VerificarLabel(String value, String valueFrontEnd){
		
		SelectItem selectItem=new SelectItem();	
		String valueFrontEndAux="N/A";
		boolean isVacacion=false;
		
		if(valueFrontEnd!=null){
			if(valueFrontEnd.equals("V")){
				selectItem=new SelectItem("V","V");
				isVacacion=true;
			}else{
				valueFrontEndAux=valueFrontEnd;
			}
		}
		
		if(!isVacacion){
			if(value!=null){
				if( (!(value.equals("null"))) && (!(value.equals("empty"))) && (!(value.equals("V"))) ){		
					String label=getLabel(value);
					selectItem.setValue(value);
					selectItem.setLabel(label);		
				}else{
					if( value.equals("V") ){
						if( !(valueFrontEndAux.equals("N/A")) && !(valueFrontEndAux.equals("null")) && !(valueFrontEndAux.equals("empty")) ){
							String label=getLabel(valueFrontEndAux);
							selectItem.setValue(valueFrontEndAux);
							selectItem.setLabel(label);		
						}
					}					
				}
			}
		}			
		
		return selectItem;
	}
	
	//Guardar Registros en BD
	private void GuardarRegistrosEmpleado(){
				
		int i=filaEdit-1;
		PlantillaPlanificacionMultiple plantilla=new PlantillaPlanificacionMultiple();
		plantilla=listaPlanificacion.get(i);
		
		if(plantilla.getEstado().equals("0")){
			//Crear Cabecera de Planificación
			PlanificacionMultiple planficacionMultiple=new PlanificacionMultiple();
			planficacionMultiple.setIdEmpleado(plantilla.getEmpleado().getEmplId());
			planficacionMultiple.setMes((getMes(fechaDesde)+1));
			planficacionMultiple.setAnio(getAño(fechaDesde));
			planficacionMultiple.setEstado(1);	
		
			planificacionMultipleHome.setInstance(planficacionMultiple);
			planificacionMultipleHome.persist();
			
			listaPlanificacion.get(i).setEstado("1");
			//*****			
		}
				
		//Comprobar Detalle de Planificacion
		ArrayList <Long> listaIdEmpleado=new ArrayList <Long>();
		listaIdEmpleado.add(plantilla.getEmpleado().getEmplId());
		
		Integer mes=getMes(fechaDesde)+1;
		Integer año=getAño(fechaDesde);
		
		List <PlanificacionMultiple> listaPlanificacionMultiple=getListaPlanificacionMultipleBD(listaIdEmpleado,mes,año);
		List <DetallePlanificacionMultiple>listaDetallePlanificacionMultiple=getListaDetallePlanificacionMultipleBD(getListaIdPlanificacion(listaPlanificacionMultiple));
		
		List<Boolean> listaDias=getListaDiasBoolean();
		Long idPlanificacionMultiple = null;
		
		for(PlanificacionMultiple p: listaPlanificacionMultiple){			
			idPlanificacionMultiple=p.getIdPlanificacionMultiple();			
			for(DetallePlanificacionMultiple d: listaDetallePlanificacionMultiple){				
				if(d.getIdPlanificacionMultiple().equals(p.getIdPlanificacionMultiple())){					
					ComprobarRegistrosDetallePlanificacionMultiple(d,plantilla);					
					listaDias.set((d.getDia()-1), true);					
				}				
			}						
		}		
		
		//Ingresar nuevos registros		
		for(int index=0;index<listaDias.size();index++){			
			int diaRecorrdio=index+1;						
			if((diaRecorrdio>=getDia(fechaDesde))&&(diaRecorrdio<=getDia(fechaHasta))){				
				Boolean dia=listaDias.get(index);				
				if(!dia){					
					GuardarRegistrosDetallePlanificacionMultiple((index+1),plantilla,idPlanificacionMultiple);					
				}				
			}			
		}		
		
	}
	
	//Guardar Registros en BD de matriz principal
		private void GuardarRegistrosMatriz(){
			
			int i=0;
			
			for(PlantillaPlanificacionMultiple pl: listaPlanificacion){				
				
				PlantillaPlanificacionMultiple plantilla=new PlantillaPlanificacionMultiple();
				plantilla=pl;
				
				if(plantilla.getEstado().equals("0")){
					//Crear Cabecera de Planificación
					PlanificacionMultiple planficacionMultiple=new PlanificacionMultiple();
					planficacionMultiple.setIdEmpleado(plantilla.getEmpleado().getEmplId());
					planficacionMultiple.setMes((getMes(fechaDesde)+1));
					planficacionMultiple.setAnio(getAño(fechaDesde));
					planficacionMultiple.setEstado(1);	
				
					planificacionMultipleHome.setInstance(planficacionMultiple);
					planificacionMultipleHome.persist();
					
					listaPlanificacion.get(i).setEstado("1");
					//*****			
				}
						
				//Comprobar Detalle de Planificacion
				ArrayList <Long> listaIdEmpleado=new ArrayList <Long>();
				listaIdEmpleado.add(plantilla.getEmpleado().getEmplId());
				
				Integer mes=getMes(fechaDesde)+1;
				Integer año=getAño(fechaDesde);
				
				List <PlanificacionMultiple> listaPlanificacionMultiple=getListaPlanificacionMultipleBD(listaIdEmpleado,mes,año);
				List <DetallePlanificacionMultiple>listaDetallePlanificacionMultiple=getListaDetallePlanificacionMultipleBD(getListaIdPlanificacion(listaPlanificacionMultiple));
				
				List<Boolean> listaDias=getListaDiasBoolean();
				Long idPlanificacionMultiple = null;
				
				for(PlanificacionMultiple p: listaPlanificacionMultiple){			
					idPlanificacionMultiple=p.getIdPlanificacionMultiple();			
					for(DetallePlanificacionMultiple d: listaDetallePlanificacionMultiple){				
						if(d.getIdPlanificacionMultiple().equals(p.getIdPlanificacionMultiple())){					
							ComprobarRegistrosDetallePlanificacionMultiple(d,plantilla);					
							listaDias.set((d.getDia()-1), true);					
						}				
					}						
				}		
				
				//Ingresar nuevos registros		
				for(int index=0;index<listaDias.size();index++){			
					int diaRecorrdio=index+1;						
					if((diaRecorrdio>=getDia(fechaDesde))&&(diaRecorrdio<=getDia(fechaHasta))){				
						Boolean dia=listaDias.get(index);				
						if(!dia){					
							GuardarRegistrosDetallePlanificacionMultiple((index+1),plantilla,idPlanificacionMultiple);					
						}				
					}			
				}		
				
				i++;
			}	
			
		}
	
	private List<Boolean> getListaDiasBoolean(){
		List<Boolean> diasActualizados = new ArrayList <Boolean>();
		for(int i=0;i<31;i++){
			diasActualizados.add(false);
		}
		return diasActualizados;
	}
	
	//comprobar por dias en BD Detalle de Planificación Múltiple
		private void GuardarRegistrosDetallePlanificacionMultiple(int dia, PlantillaPlanificacionMultiple plantilla, Long idPlanificacionMultiple){
			
			DetallePlanificacionMultiple detallePlanificacionMultiple= new DetallePlanificacionMultiple();
			detallePlanificacionMultiple.setIdPlanificacionMultiple(idPlanificacionMultiple);
			detallePlanificacionMultiple.setDia(dia);
			detallePlanificacionMultiple.setEstado(0);
			
			String idHorario;
			String codigoHorario;
			
			switch(dia){		
				case(1):
						idHorario=""+plantilla.getHorario1().getValue();
						codigoHorario=plantilla.getHorario1().getLabel();						
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);
						
						break;
				case(2):
						idHorario=""+plantilla.getHorario2().getValue();
						codigoHorario=plantilla.getHorario2().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(3):
						idHorario=""+plantilla.getHorario3().getValue();
						codigoHorario=plantilla.getHorario3().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(4):
						idHorario=""+plantilla.getHorario4().getValue();
						codigoHorario=plantilla.getHorario4().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(5):
						idHorario=""+plantilla.getHorario5().getValue();
						codigoHorario=plantilla.getHorario5().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(6):
						idHorario=""+plantilla.getHorario6().getValue();
						codigoHorario=plantilla.getHorario6().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(7):
						idHorario=""+plantilla.getHorario7().getValue();
						codigoHorario=plantilla.getHorario7().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(8):
						idHorario=""+plantilla.getHorario8().getValue();
						codigoHorario=plantilla.getHorario8().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(9):
						idHorario=""+plantilla.getHorario9().getValue();
						codigoHorario=plantilla.getHorario9().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(10):
						idHorario=""+plantilla.getHorario10().getValue();
						codigoHorario=plantilla.getHorario10().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(11):
						idHorario=""+plantilla.getHorario11().getValue();
						codigoHorario=plantilla.getHorario11().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(12):
						idHorario=""+plantilla.getHorario12().getValue();
						codigoHorario=plantilla.getHorario12().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(13):
						idHorario=""+plantilla.getHorario13().getValue();
						codigoHorario=plantilla.getHorario13().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(14):
						idHorario=""+plantilla.getHorario14().getValue();
						codigoHorario=plantilla.getHorario14().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(15):
						idHorario=""+plantilla.getHorario15().getValue();
						codigoHorario=plantilla.getHorario15().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(16):
						idHorario=""+plantilla.getHorario16().getValue();
						codigoHorario=plantilla.getHorario16().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(17):
						idHorario=""+plantilla.getHorario17().getValue();
						codigoHorario=plantilla.getHorario17().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(18):
						idHorario=""+plantilla.getHorario18().getValue();
						codigoHorario=plantilla.getHorario18().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(19):
						idHorario=""+plantilla.getHorario19().getValue();
						codigoHorario=plantilla.getHorario19().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(20):
						idHorario=""+plantilla.getHorario20().getValue();
						codigoHorario=plantilla.getHorario20().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;				
				case(21):
						idHorario=""+plantilla.getHorario21().getValue();
						codigoHorario=plantilla.getHorario21().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(22):
						idHorario=""+plantilla.getHorario22().getValue();
						codigoHorario=plantilla.getHorario22().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(23):
						idHorario=""+plantilla.getHorario23().getValue();
						codigoHorario=plantilla.getHorario23().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(24):
						idHorario=""+plantilla.getHorario24().getValue();
						codigoHorario=plantilla.getHorario24().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(25):
						idHorario=""+plantilla.getHorario25().getValue();
						codigoHorario=plantilla.getHorario25().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(26):
						idHorario=""+plantilla.getHorario26().getValue();
						codigoHorario=plantilla.getHorario26().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(27):
						idHorario=""+plantilla.getHorario27().getValue();
						codigoHorario=plantilla.getHorario27().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(28):
						idHorario=""+plantilla.getHorario28().getValue();
						codigoHorario=plantilla.getHorario28().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(29):
						idHorario=""+plantilla.getHorario29().getValue();
						codigoHorario=plantilla.getHorario29().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(30):
						idHorario=""+plantilla.getHorario30().getValue();
						codigoHorario=plantilla.getHorario30().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
				case(31):
						idHorario=""+plantilla.getHorario31().getValue();
						codigoHorario=plantilla.getHorario31().getLabel();																		
						InsertarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,codigoHorario);						
						break;
			}
	}

	
	//comprobar por dias en BD Detalle de Planificación Múltiple
	private void ComprobarRegistrosDetallePlanificacionMultiple(DetallePlanificacionMultiple detallePlanificacionMultiple, PlantillaPlanificacionMultiple plantilla){
		
		Integer dia=detallePlanificacionMultiple.getDia();
		String idHorario=""+detallePlanificacionMultiple.getIdHorario();
		
		String idHorarioPlantilla;
		String codigoHorarioPlantilla;
		
		switch(dia){		
			case(1):
					idHorarioPlantilla=""+plantilla.getHorario1().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario1().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(2):
					idHorarioPlantilla=""+plantilla.getHorario2().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario2().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);				
					break;
			case(3):
					idHorarioPlantilla=""+plantilla.getHorario3().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario3().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(4):
					idHorarioPlantilla=""+plantilla.getHorario4().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario4().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(5):
					idHorarioPlantilla=""+plantilla.getHorario5().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario5().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(6):
					idHorarioPlantilla=""+plantilla.getHorario6().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario6().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(7):
					idHorarioPlantilla=""+plantilla.getHorario7().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario7().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(8):
					idHorarioPlantilla=""+plantilla.getHorario8().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario8().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(9):
					idHorarioPlantilla=""+plantilla.getHorario9().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario9().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(10):
					idHorarioPlantilla=""+plantilla.getHorario10().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario10().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(11):
					idHorarioPlantilla=""+plantilla.getHorario11().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario11().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(12):
					idHorarioPlantilla=""+plantilla.getHorario12().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario12().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);					
					break;
			case(13):
					idHorarioPlantilla=""+plantilla.getHorario13().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario13().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(14):
					idHorarioPlantilla=""+plantilla.getHorario14().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario14().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(15):
					idHorarioPlantilla=""+plantilla.getHorario15().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario15().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(16):
					idHorarioPlantilla=""+plantilla.getHorario16().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario16().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(17):
					idHorarioPlantilla=""+plantilla.getHorario17().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario17().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(18):
					idHorarioPlantilla=""+plantilla.getHorario18().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario18().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(19):
					idHorarioPlantilla=""+plantilla.getHorario19().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario19().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(20):
					idHorarioPlantilla=""+plantilla.getHorario20().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario20().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);	
					break;
			case(21):
					idHorarioPlantilla=""+plantilla.getHorario21().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario21().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(22):
					idHorarioPlantilla=""+plantilla.getHorario22().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario22().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(23):
					idHorarioPlantilla=""+plantilla.getHorario23().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario23().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(24):
					idHorarioPlantilla=""+plantilla.getHorario24().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario24().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(25):			
					idHorarioPlantilla=""+plantilla.getHorario25().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario25().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(26):
					idHorarioPlantilla=""+plantilla.getHorario26().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario26().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(27):
					idHorarioPlantilla=""+plantilla.getHorario27().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario27().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(28):
					idHorarioPlantilla=""+plantilla.getHorario28().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario28().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(29):
					idHorarioPlantilla=""+plantilla.getHorario29().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario29().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(30):
					idHorarioPlantilla=""+plantilla.getHorario30().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario30().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;
			case(31):
					idHorarioPlantilla=""+plantilla.getHorario31().getValue();
					codigoHorarioPlantilla=""+plantilla.getHorario31().getLabel();
					ActualizarRegistrosDetallePlanificacion(detallePlanificacionMultiple,idHorario,idHorarioPlantilla,codigoHorarioPlantilla);
					break;		
		}
	}
	
	//Actualizar Detalle Planificacion Multiple
	private void ActualizarRegistrosDetallePlanificacion(DetallePlanificacionMultiple detallePlanificacionMultiple, String idHorario, String idHorarioPlantilla,String codigoHorarioPlantilla){
	
		if(!(idHorarioPlantilla.equals(idHorario))){
			if(!(idHorarioPlantilla.equals("null")) && !(idHorarioPlantilla.equals("V"))){
				
				detallePlanificacionMultiple.setIdHorario(Integer.parseInt(idHorarioPlantilla));
				detallePlanificacionMultiple.setCodigoHorario(codigoHorarioPlantilla);
				
				GestionarDetallePlanificacionMultiple(detallePlanificacionMultiple,"update");
			}else{
				if( !(idHorarioPlantilla.equals("V")) )
					GestionarDetallePlanificacionMultiple(detallePlanificacionMultiple,"delete");
			}						
		}
		
	}
	
	//insertar Detalle Planificacion Multiple
	private void InsertarRegistrosDetallePlanificacion(DetallePlanificacionMultiple detallePlanificacionMultiple, String idHorario, String codigoHorario){
		
		if(!(idHorario.equals("null")) && !(idHorario.equals("V"))){
			detallePlanificacionMultiple.setIdHorario(Integer.parseInt(idHorario));
			detallePlanificacionMultiple.setCodigoHorario(codigoHorario);
			GestionarDetallePlanificacionMultiple(detallePlanificacionMultiple,"insert");			
		}
		
	}
	
	//Gestionar Detalle Planificacion Multiple
	private void GestionarDetallePlanificacionMultiple(DetallePlanificacionMultiple detallePlanificacionMultiple,String transaccion){
		
		detallePlanificacionMultipleHome.setInstance(detallePlanificacionMultiple);
		
		if(transaccion.equals("insert"))
			detallePlanificacionMultipleHome.persist();
		else
			if(transaccion.equals("update"))
				detallePlanificacionMultipleHome.update();
			else
				if(transaccion.equals("delete"))
					detallePlanificacionMultipleHome.remove();
		
	}
	
	//Gestionar Lista de Asistencia
	private void GestionarListaAsistencia(List<Asistencia> listaAsistencia, String transaccion){
		
		if(transaccion.equals("delete")){
			
			ArrayList <Long> listaAsisId=new ArrayList<Long>();
			
			int max=1000;
			int i=0;
			
			for(Asistencia a: listaAsistencia){
				i++;
				listaAsisId.add(a.getAsisId());
				
				if(i==max){
					EliminarRegistrosAsistencia(listaAsisId);
					i=0;
					listaAsisId=new ArrayList<Long>();
				}
			}
			
			if(listaAsisId.size()>0){
				EliminarRegistrosAsistencia(listaAsisId);
			}
			
		}
		
	}
	
	//Obtener el código de horario en base al id de la tabla
	private String getLabel(String value){
		String label="";
		for(int i=0;i<listaCodigosHorario.size();i++){			
			SelectItem item=listaCodigosHorario.get(i);
			if(item.getValue().equals(value)){
				label=item.getLabel();
				break;
			}
		}			
		return label;
	}
	
	//Obtener el id de horario en base al codigo de horario
	private Integer getLabelCodigo(String value){
		Integer idHorario=null;
		for(int i=0;i<listaCodigosHorario.size();i++){			
			SelectItem item=listaCodigosHorario.get(i);
			if(item.getLabel().equals(value)){
				idHorario=Integer.parseInt(""+item.getValue());
				break;
			}
		}			
		return idHorario;
	}
	
	//Descartar Empleado de la Lista
	public void DescartarEmpleado(){
		if(listaPlanificacion.size()>1){
			int i=filaRemove-1;
			listaPlanificacion.remove(i);
			i=1;
			for(PlantillaPlanificacionMultiple pm: listaPlanificacion){
				pm.setNumero(i);
				i++;
			}
		}else{
			listaPlanificacion=new ArrayList<PlantillaPlanificacionMultiple>();
			CargarPagina();
		}		
	}
	
	public String getTime(Date date){
		
		Calendar calendar=getCalendar(date);
		
		int hour=calendar.get(Calendar.HOUR_OF_DAY);
		int minute=calendar.get(Calendar.MINUTE);
		
		String hourCadena=""+hour;
		String minuteCadena=""+minute;
		String time;
		
		if(hour<10)
			hourCadena="0"+hour;
		
		if(minute<10)
			minuteCadena="0"+minute;
		
		time=hourCadena+":"+minuteCadena;
		
		return time;
	}

	public String getTipoGrupo() {
		if(tipoGrupo == null){
			tipoGrupo = new String();
		}
		
		return tipoGrupo;
	}

	public void setTipoGrupo(String tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	public List<Empleado> getEmpleados() {
		if(empleados==null)
			empleados=new ArrayList<Empleado>();
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
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

	public List<Long> getCodigosEmpleado() {
		return codigosEmpleado;
	}

	public void setCodigosEmpleado(List<Long> codigosEmpleado) {
		this.codigosEmpleado = codigosEmpleado;
	}

	public List<PlantillaPlanificacionMultiple> getListaPlanificacion() {
		if(listaPlanificacion==null)
			listaPlanificacion=new ArrayList<PlantillaPlanificacionMultiple>();
		return listaPlanificacion;
	}

	public void setListaPlanificacion(List<PlantillaPlanificacionMultiple> listaPlanificacion) {
		this.listaPlanificacion = listaPlanificacion;
	}
	
	public List<Horario> getListaHorario() {		
		return listaHorario;
	}

	public void setListaHorario(List<Horario> listaHorario) {
		this.listaHorario = listaHorario;
	}

	public Integer getFilaEdit() {
		return filaEdit;
	}

	public void setFilaEdit(Integer fila) {	
		this.filaEdit = fila;
		
		//**********Cargar Lista a Editar por Empleado
		PlantillaPlanificacionMultiple plantilla=new PlantillaPlanificacionMultiple();
		PlantillaPlanificacionMultipleEmpleado p=new PlantillaPlanificacionMultipleEmpleado();	
		
		int i=this.filaEdit-1;					
		plantilla=listaPlanificacion.get(i);
		
		getListaPlanificacionEmpleado().clear();
		
		//
		p.setEmpleado(plantilla.getEmpleado());
		p.setNumero(plantilla.getNumero());
		p.setEstado(plantilla.getEstado());
		p.setTotalHoras(plantilla.getTotalHoras());
		
		SelectItem selectItem;
		
		selectItem=plantilla.getHorario1(); value1=""+selectItem.getValue();
		selectItem=plantilla.getHorario2(); value2=""+selectItem.getValue();
		selectItem=plantilla.getHorario3(); value3=""+selectItem.getValue();
		selectItem=plantilla.getHorario4(); value4=""+selectItem.getValue();
		selectItem=plantilla.getHorario5(); value5=""+selectItem.getValue();
		selectItem=plantilla.getHorario6(); value6=""+selectItem.getValue();
		selectItem=plantilla.getHorario7(); value7=""+selectItem.getValue();
		selectItem=plantilla.getHorario8(); value8=""+selectItem.getValue();
		selectItem=plantilla.getHorario9(); value9=""+selectItem.getValue();
		selectItem=plantilla.getHorario10();value10=""+selectItem.getValue();
		selectItem=plantilla.getHorario11(); value11=""+selectItem.getValue();
		selectItem=plantilla.getHorario12(); value12=""+selectItem.getValue();
		selectItem=plantilla.getHorario13(); value13=""+selectItem.getValue();
		selectItem=plantilla.getHorario14(); value14=""+selectItem.getValue();
		selectItem=plantilla.getHorario15(); value15=""+selectItem.getValue();
		selectItem=plantilla.getHorario16(); value16=""+selectItem.getValue();
		selectItem=plantilla.getHorario17(); value17=""+selectItem.getValue();
		selectItem=plantilla.getHorario18(); value18=""+selectItem.getValue();
		selectItem=plantilla.getHorario19(); value19=""+selectItem.getValue();
		selectItem=plantilla.getHorario20(); value20=""+selectItem.getValue();
		selectItem=plantilla.getHorario21(); value21=""+selectItem.getValue();
		selectItem=plantilla.getHorario22(); value22=""+selectItem.getValue();
		selectItem=plantilla.getHorario23(); value23=""+selectItem.getValue();
		selectItem=plantilla.getHorario24(); value24=""+selectItem.getValue();
		selectItem=plantilla.getHorario25(); value25=""+selectItem.getValue();
		selectItem=plantilla.getHorario26(); value26=""+selectItem.getValue();
		selectItem=plantilla.getHorario27(); value27=""+selectItem.getValue();
		selectItem=plantilla.getHorario28(); value28=""+selectItem.getValue();
		selectItem=plantilla.getHorario29(); value29=""+selectItem.getValue();
		selectItem=plantilla.getHorario30(); value30=""+selectItem.getValue();
		selectItem=plantilla.getHorario31(); value31=""+selectItem.getValue();		
			
		listaPlanificacionEmpleado.add(p);
		
		//**********
	}	
	
	public List<PlantillaPlanificacionMultipleEmpleado> getListaPlanificacionEmpleado() {
		if(listaPlanificacionEmpleado==null)
			listaPlanificacionEmpleado=new ArrayList<PlantillaPlanificacionMultipleEmpleado>();		
		
		return listaPlanificacionEmpleado;
	}

	public void setListaPlanificacionEmpleado(
			List<PlantillaPlanificacionMultipleEmpleado> listaPlanificacionEmpleado) {	
		this.listaPlanificacionEmpleado = listaPlanificacionEmpleado;
	}

	public List<SelectItem> getListaCodigosHorario() {
		if(listaCodigosHorario==null)
			listaCodigosHorario=new ArrayList<SelectItem>();
		
		return listaCodigosHorario;
	}

	public void setListaCodigosHorario(List<SelectItem> listaCodigosHorario) {
		this.listaCodigosHorario = listaCodigosHorario;
	}

	public Integer getFilaRemove() {
		return filaRemove;
	}

	public void setFilaRemove(Integer filaRemove) {
		this.filaRemove = filaRemove;
		
		//**********Cargar mensaje de empleado a remover
		PlantillaPlanificacionMultiple plantilla=new PlantillaPlanificacionMultiple();	
		Empleado empleado=new Empleado();
		
		int i=this.filaRemove-1;					
		plantilla=listaPlanificacion.get(i);	
		empleado=plantilla.getEmpleado();
		
		String empleadoString=empleado.getApellido()+" "+empleado.getNombre();
		EmpleadoRemove="¿Desea descartar de la lista a "+empleadoString+"?";
		//**********
	}

	public String getEmpleadoRemove() {
		return EmpleadoRemove;
	}

	public void setEmpleadoRemove(String empleadoRemove) {
		EmpleadoRemove = empleadoRemove;
	}

	public List<DetalleHorario> getListaDetalleHorario() {
		if(listaDetalleHorario==null)
			listaDetalleHorario=new ArrayList<DetalleHorario>();
		return listaDetalleHorario;
	}

	public void setListaDetalleHorario(List<DetalleHorario> listaDetalleHorario) {
		this.listaDetalleHorario = listaDetalleHorario;
	}

	public String getValueSelected() {
		return valueSelected;
	}

	public void setValueSelected(String valueSelected) {
		this.valueSelected = valueSelected;
	}

	public int getVacia() {
		return vacia;
	}

	public void setVacia(int vacia) {
		this.vacia = vacia;		
		//Vaciar Lista de Empleado
		if(vacia==1){
			value1=null;
			value2=null;
			value3=null;
			value4=null;
			value5=null;
			value6=null;
			value7=null;
			value8=null;
			value9=null;
			value10=null;
			value11=null;
			value12=null;
			value13=null;
			value14=null;
			value15=null;
			value16=null;
			value17=null;
			value18=null;
			value19=null;
			value20=null;
			value21=null;
			value22=null;
			value23=null;
			value24=null;
			value25=null;
			value26=null;
			value27=null;
			value28=null;
			value29=null;
			value30=null;
			value31=null;			
		}
		
	}
	
	//Nuevos metodos
	
	//limpiar mensajes de alertas
	public void IniciarMensajesCargaArchivo(){
		mensajeCorrecto="";
		mensajeError="";		
		CargarPagina();
	}
	
	//Adjuntar Archivo de planificacion multiple
	@SuppressWarnings({ "deprecation", "resource" })
	public void subirArchivo(UploadEvent event) throws Exception {	
		
		String[] divcadena = null;
		
		archivoAdjunto=false;
		
		//iniciar valores
		this.getListaPlanificacion().clear();
		mensajeCorrecto="";
		mensajeError="";
		
		//parametros de Base de datos
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		detalleTipoParametros = detalleTipoParametroList.getRutaArchivoPermiso();
		String path = detalleTipoParametros.getDescripcion();
		
		//Path de archivo de subida y de escritura
		String sourceFile = "" + event.getUploadItem().getFile();
		String destFile = path + "plantilla.xls";		
		
		//Copiar archivo al Server
		FileChannel origen = new FileInputStream(sourceFile).getChannel();
		FileChannel destino = new FileOutputStream(destFile).getChannel();

		origen = new FileInputStream(sourceFile).getChannel();
		destino = new FileOutputStream(destFile).getChannel();		

		long count = 0;
		long size = origen.size();
		while ((count += destino.transferFrom(origen, count, size - count)) < size);
		origen.close();
		destino.close();
		
		//Leer archivo de Excel
		FileInputStream file = new FileInputStream(new File(destFile));
		
		HSSFWorkbook  libro = new HSSFWorkbook(file);
		HSSFSheet hoja = libro.getSheetAt(0);
		
		Iterator<Row> rowIterator = hoja.iterator();
		
		Row row;
		
		//iniciadores de filas y columnas del archivo
		int i=0,j=0;
		
		//mes y año definidos en el archivo
		int mes=0;
		int año=0;
		
		//inicio y fin del mes definido en el archivo
		int inicio=0;
		int fin=0;		
		
		//recorredor de dias
		int k=0;
		
		//numerador de empleado
		int numero=0;
		
		//definir plantila de planificacion		
		PlantillaPlanificacionMultiple plantilla=null;
		//definir plantilla de vacaciones de empleado
		List<SolicitudVacacion> listaSolicitudVacacion=null;
		//definir empleado
		Empleado em=null;
		
		while (rowIterator.hasNext()){
			
			row = rowIterator.next();			
			Iterator<Cell> cellIterator = row.cellIterator();			
			Cell celda;
			
			j=0;
			
			while (cellIterator.hasNext()){
				
				celda = cellIterator.next();				
				System.out.print("i="+i+", j="+j+" celda:"+celda.toString());
				
				//DEFINIR MES Y AÑO
				if(i==1 && j==1){
					try{
						año=(int) celda.getNumericCellValue();
						System.out.print(" A año:"+año);
						if(año<2000){
							mensajeError="Año no definido correctamente";							
						}
					}catch(Exception e){
						mensajeError="Año no definido como número";
					}
				}				
				if(i==1 && j==3){
					try{
						mes=(int) celda.getNumericCellValue();
						mes--;
						System.out.print(" A mes:"+mes);
	  					if(mes<1 && mes>12){
							mensajeError="Mes no definido correctamente";							
						}
					}catch(Exception e){
						mensajeError="Mes no definido como número";
					}
				}				
				//********************
				//Definir fecha inicio y fecha fin y limite
				if(i==2 && j==0){
				//System.out.print(" año:"+año);
				//	System.out.print(" mes:"+mes);
					
					//System.out.print(" getFechaInicio(año,mes):"+getFechaInicio(año,mes));
					//System.out.print(" getFechaFin(año,mes):"+getFechaFin(año,mes));
					
					this.fechaDesde=getFechaInicio(año,mes);
					this.fechaHasta=getFechaFin(año,mes);
					
					//System.out.print(" fechaDesde:"+fechaDesde);
					//System.out.print(" fechaHasta:"+fechaHasta);
					
					inicio=fechaDesde.getDate();
					fin=fechaHasta.getDate();
					
					System.out.print(" inicio:"+inicio);
					System.out.print(" fin:"+fin);
				}
				//***********
				//Buscar empleado por codigo
				if(i>3 && j==0){
					String codigoEmpleado=celda.toString();
					try{
						divcadena = codigoEmpleado.split("\\.");
						codigoEmpleado = divcadena[0];
					System.out.print(" codigoEmpleado:"+codigoEmpleado);
					}catch(Exception ex){
						ex.printStackTrace();
						System.out.print(" error empleado:"+codigoEmpleado);
					}
					
					em=BuscarEmpleadoCodigo(codigoEmpleado);//Buscar Empleado
					
					if(em==null){
						mensajeError="Empleado no definido correctamente";
						plantilla=null;
						k=0;
					}else{
						numero++;
						plantilla=new PlantillaPlanificacionMultiple();
						plantilla.setEmpleado(em);
						plantilla.setNumero(numero);
						plantilla.setEstado(EstadoPlanificacionMultiple(em));
						k=1;
					}
				}
				//*************
				//Recorrer filas de dias planficados
				if(i>3 && j>1){
					if(plantilla!=null && k>0){
						if(k>=inicio && k<=fin){
							
							String codigoHorario=celda.toString();
							Integer idHorario=getLabelCodigo(codigoHorario);//Buscar id de horario
							
							if(idHorario!=null){
								DetallePlanificacionMultiple detallePlanificacionMultiple=new DetallePlanificacionMultiple();
								detallePlanificacionMultiple.setDia(k);
								detallePlanificacionMultiple.setIdHorario(idHorario);
								detallePlanificacionMultiple.setCodigoHorario(codigoHorario);
								
								plantilla=getRegistrosDetallePlanificacionMultiple(plantilla, detallePlanificacionMultiple);
							}
							
							if(k==fin){
								//vacaciones
								//*****************************************************************
								List<Empleado> listaEmpleado=new ArrayList<Empleado>();
								listaEmpleado.add(em);
								ArrayList <Long> listaIdEmpleado=getListaIdEmpleados(listaEmpleado);
								listaSolicitudVacacion=buscarVacaciones(listaIdEmpleado,getLimiteInferior(),getLimiteSuperior(),"si");
								
								List<SolicitudVacacion> solicitudesEmpleado=getListaSolicitudVacacionEmpleado(listaSolicitudVacacion,em);			
								List<Integer> diaVacacionesEmpleado = getDiasVacacionesEmpleado(solicitudesEmpleado);								
								for(int index=0;index<diaVacacionesEmpleado.size();index++){
									int dia=diaVacacionesEmpleado.get(index);
									plantilla=ResolverVacaciones(dia, plantilla);
								}			
								//*****************************************************************								
								
								this.listaPlanificacion.add(plantilla);
								plantilla=null;
								listaSolicitudVacacion=null;
								em=null;
								k=0;
							}else{
								k++;
							}
							
						}else{							
							//vacaciones
							//*****************************************************************
							List<Empleado> listaEmpleado=new ArrayList<Empleado>();
							listaEmpleado.add(em);
							ArrayList <Long> listaIdEmpleado=getListaIdEmpleados(listaEmpleado);
							listaSolicitudVacacion=buscarVacaciones(listaIdEmpleado,getLimiteInferior(),getLimiteSuperior(),"si");
							
							List<SolicitudVacacion> solicitudesEmpleado=getListaSolicitudVacacionEmpleado(listaSolicitudVacacion,em);			
							List<Integer> diaVacacionesEmpleado = getDiasVacacionesEmpleado(solicitudesEmpleado);								
							for(int index=0;index<diaVacacionesEmpleado.size();index++){
								int dia=diaVacacionesEmpleado.get(index);
								plantilla=ResolverVacaciones(dia, plantilla);
							}			
							//*****************************************************************	
							
							this.listaPlanificacion.add(plantilla);
							plantilla=null;
							listaSolicitudVacacion=null;
							em=null;
							k=0;
						}
					}			
				}
				
				//*******************
				
				
				j++;				
			}			
			
			System.out.println();
			i++;
			
			if(mensajeError.length()>0){
				this.listaPlanificacion.clear();
				this.fechaDesde=null;
				this.fechaHasta=null;
				break;
			}
			
		}
		
		if(mensajeError.equals("")){
			setMensajeCorrecto("Archivo subido correctamente");	
			archivoAdjunto=true;
		}	
		
		System.out.print(" size lista final:: "+this.listaPlanificacion.size());
		//mensajeError="Problema con la carga del archivo";	
			
		File fichero = new File(destFile);
		if (fichero.delete())
			System.out.println("El fichero ha sido borrado satisfactoriamente");
		else
			System.out.println("El fichero no puede ser borrado");
		
	}	
	
	private String EstadoPlanificacionMultiple(Empleado e){
		List<Empleado> lista=new ArrayList<Empleado>();
		lista.add(e);
		
		ArrayList <Long> listaIdEmpleado=getListaIdEmpleados(lista);
		Integer mes=getMes(fechaDesde)+1;
		Integer año=getAño(fechaDesde);
		
		List<PlanificacionMultiple> listaPlanificacionMultiple=getListaPlanificacionMultipleBD(listaIdEmpleado,mes,año);
		
		if(listaPlanificacionMultiple.size()>0){
			return "1";
		}else{
			return "0";
		}
	}
	
	//fecha limite inferior de mes y año de archivo de carga
	private Date getFechaInicio(int año, int mes) {
		 Calendar cal = Calendar.getInstance();
		 cal.set(Calendar.YEAR, año);
		 cal.set(Calendar.MONTH,mes);
		 cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH)); 
		// cal.set(año, mes, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		 return cal.getTime();
	}
	
	//fecha limite superior de mes y año de archivo de carga
	public static Date getFechaFin(int año, int mes) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, año);
		cal.set(Calendar.MONTH,mes);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		//cal.set(año, mes, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = value1;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = value3;
	}

	public String getValue4() {
		return value4;
	}

	public void setValue4(String value4) {
		this.value4 = value4;
	}

	public String getValue5() {
		return value5;
	}

	public void setValue5(String value5) {
		this.value5 = value5;
	}

	public String getValue6() {
		return value6;
	}

	public void setValue6(String value6) {
		this.value6 = value6;
	}

	public String getValue7() {
		return value7;
	}

	public void setValue7(String value7) {
		this.value7 = value7;
	}

	public String getValue8() {
		return value8;
	}

	public void setValue8(String value8) {
		this.value8 = value8;
	}

	public String getValue9() {
		return value9;
	}

	public void setValue9(String value9) {
		this.value9 = value9;
	}

	public String getValue10() {
		return value10;
	}

	public void setValue10(String value10) {
		this.value10 = value10;
	}

	public String getValue11() {
		return value11;
	}

	public void setValue11(String value11) {
		this.value11 = value11;
	}

	public String getValue12() {
		return value12;
	}

	public void setValue12(String value12) {
		this.value12 = value12;
	}

	public String getValue13() {
		return value13;
	}

	public void setValue13(String value13) {
		this.value13 = value13;
	}

	public String getValue14() {
		return value14;
	}

	public void setValue14(String value14) {
		this.value14 = value14;
	}

	public String getValue15() {
		return value15;
	}

	public void setValue15(String value15) {
		this.value15 = value15;
	}

	public String getValue16() {
		return value16;
	}

	public void setValue16(String value16) {
		this.value16 = value16;
	}

	public String getValue17() {
		return value17;
	}

	public void setValue17(String value17) {
		this.value17 = value17;
	}

	public String getValue18() {
		return value18;
	}

	public void setValue18(String value18) {
		this.value18 = value18;
	}

	public String getValue19() {
		return value19;
	}

	public void setValue19(String value19) {
		this.value19 = value19;
	}

	public String getValue20() {
		return value20;
	}

	public void setValue20(String value20) {
		this.value20 = value20;
	}

	public String getValue21() {
		return value21;
	}

	public void setValue21(String value21) {
		this.value21 = value21;
	}

	public String getValue22() {
		return value22;
	}

	public void setValue22(String value22) {
		this.value22 = value22;
	}

	public String getValue23() {
		return value23;
	}

	public void setValue23(String value23) {
		this.value23 = value23;
	}

	public String getValue24() {
		return value24;
	}

	public void setValue24(String value24) {
		this.value24 = value24;
	}

	public String getValue25() {
		return value25;
	}

	public void setValue25(String value25) {
		this.value25 = value25;
	}

	public String getValue26() {
		return value26;
	}

	public void setValue26(String value26) {
		this.value26 = value26;
	}

	public String getValue27() {
		return value27;
	}

	public void setValue27(String value27) {
		this.value27 = value27;
	}

	public String getValue28() {
		return value28;
	}

	public void setValue28(String value28) {
		this.value28 = value28;
	}

	public String getValue29() {
		return value29;
	}

	public void setValue29(String value29) {
		this.value29 = value29;
	}

	public String getValue30() {
		return value30;
	}

	public void setValue30(String value30) {
		this.value30 = value30;
	}

	public String getValue31() {
		return value31;
	}

	public void setValue31(String value31) {
		this.value31 = value31;
	}

	public int getNumeroFilas() {
		return numeroFilas;
	}

	public void setNumeroFilas(int numeroFilas) {
		this.numeroFilas = numeroFilas;
	}

	public List<SelectItem> getListaNumeroFilas() {
		return listaNumeroFilas;
	}

	public void setListaNumeroFilas(List<SelectItem> listaNumeroFilas) {
		this.listaNumeroFilas = listaNumeroFilas;
	}

	public boolean isVisualizaPanelHorarios() {
		return visualizaPanelHorarios;
	}

	public void setVisualizaPanelHorarios(boolean visualizaPanelHorarios) {
		this.visualizaPanelHorarios = visualizaPanelHorarios;
	}

	public Integer getRegistrosProcesados() {
		return registrosProcesados;
	}

	public void setRegistrosProcesados(Integer registrosProcesados) {
		this.registrosProcesados = registrosProcesados;
	}

	public Integer getRegistrosNoProcesados() {
		return registrosNoProcesados;
	}

	public void setRegistrosNoProcesados(Integer registrosNoProcesados) {
		this.registrosNoProcesados = registrosNoProcesados;
	}

	public List<PlantillaPlanificacion> getAsistenciaInsert() {
		if(asistenciaInsert==null)
			asistenciaInsert=new ArrayList<PlantillaPlanificacion>();
		return asistenciaInsert;
	}

	public void setAsistenciaInsert(List<PlantillaPlanificacion> asistenciaInsert) {
		this.asistenciaInsert = asistenciaInsert;
	}

	public Empleado getEmpleadoActual() {
		return empleadoActual;
	}

	public void setEmpleadoActual(Empleado empleadoActual) {
		this.empleadoActual = empleadoActual;
	}

	public String getMensajeCorrecto() {
		return mensajeCorrecto;
	}

	public void setMensajeCorrecto(String mensajeCorrecto) {
		this.mensajeCorrecto = mensajeCorrecto;
	}
	
	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
		
}