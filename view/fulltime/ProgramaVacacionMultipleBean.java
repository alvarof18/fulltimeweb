package com.casapazmino.fulltime.procesos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.persistence.EntityManager;

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

import com.casapazmino.fulltime.action.CiudadFeriadoList;
import com.casapazmino.fulltime.action.DetalleGrupoContratadoList;
import com.casapazmino.fulltime.action.EmpleadoHome;
import com.casapazmino.fulltime.action.EmpleadoPeriodoVacacionList;
import com.casapazmino.fulltime.action.ProgramaVacacionHome;
import com.casapazmino.fulltime.action.ProgramaVacacionList;
import com.casapazmino.fulltime.action.SolicitudVacacionHome;
import com.casapazmino.fulltime.action.SolicitudVacacionList;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.ProgramaVacacion;
import com.casapazmino.fulltime.seguridad.model.PermisosMenu;

@Name("programaVacacionMultiple")
@Scope(ScopeType.SESSION)

public class ProgramaVacacionMultipleBean implements Serializable, ProgramaVacacionMultiple {

	private static final long serialVersionUID = 8750596808938047779L;

	@In
	EntityManager entityManager;

	@Logger
	Log log;

	private Empleado empleado;
	
	@In(create = true)
	ProgramaVacacionList programaVacacionList;
	
	@In(create = true)
	EmpleadoHome empleadoHome;
	
	@In(create = true)
	ProgramaVacacionHome programaVacacionHome;
	
	@In(create = true)
	SolicitudVacacionList solicitudVacacionList;
	
	@In(create = true)
	SolicitudVacacionHome solicitudVacacionHome;
	
	@In(create = true)
	DetalleGrupoContratadoList detalleGrupoContratadoList;
	
	@In(create = true)
	EmpleadoPeriodoVacacionList empleadoPeriodoVacacionList;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	private List<Empleado> empleados;
	private List<DetalleGrupoContratado> detalleGrupoContratado;
	
	private List<EmpleadoPeriodoVacacion> empleadoPeriodoVacacion;
	
	private String tipoReporte;
	private ArrayList<Long> cargos;
	private ArrayList<Long> ciudades;
	private ArrayList<Long> detalleGrupoContratados;
	private ArrayList<Long> departamentos;
	
	
	private Date periodo;

	private ProgramaVacacion programaVacacion;
	
	private List<ProgramaVacacion> listaProgramaVacacion;
	
	
	private int acceso;
	private Empleado empleadoUsuario;
	
	private PermisosMenu permisos;
	
	//Auditoria
	

	public ProgramaVacacionMultipleBean() {
		this.tipoReporte = "Cargo";
			
		//Consultar detalle grupo contratado 
		detalleGrupoContratadoList=(DetalleGrupoContratadoList) Component.getInstance("detalleGrupoContratadoList", true);		
		detalleGrupoContratadoList.setMaxResults(null);
		this.getDetalleGrupoContratado();
		detalleGrupoContratado=detalleGrupoContratadoList.getResultList();
		
		//consultar periodo de vacacion
		empleadoPeriodoVacacionList=(EmpleadoPeriodoVacacionList) Component.getInstance("empleadoPeriodoVacacionList", true);		
		empleadoPeriodoVacacionList.setMaxResults(null);
		this.getEmpleadoPeriodoVacacion();
		empleadoPeriodoVacacion=empleadoPeriodoVacacionList.getResultList();		
		
		//****************************		
		GestionPermisoVacacion gestionPermisoVacacion=(GestionPermisoVacacion) Component.getInstance("gestionPermisoVacacion", true);	
		this.acceso=gestionPermisoVacacion.buscarAccesoEmpleados();
		this.empleadoUsuario = gestionPermisoVacacion.buscarEmpleado();
		this.permisos=gestionPermisoVacacion.buscarPermisoMenuUsuario();
		
		this.getListaProgramaVacacion().clear();	
		
	}

	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosCargo() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e, EmpleadoPeriodoVacacion pv where e.cargo.cargId in (:cargos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.emplId=pv.empleado.emplId order by e.apellido")
				.setParameter("cargos", getCargos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}
		
	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosDepartamento() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e, EmpleadoPeriodoVacacion pv where e.departamento.depaId in (:departamentos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.emplId=pv.empleado.emplId order by e.apellido")
				.setParameter("departamentos", getDepartamentos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}
	

	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadosGrupo() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e, EmpleadoPeriodoVacacion pv where e.detalleGrupoContratado.dgcoId in (:grupos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) and e.emplId=pv.empleado.emplId order by e.apellido")
				.setParameter("grupos", getDetalleGrupoContratados())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
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
			InvalidValue iv= new InvalidValue("Seleccione los datos correctamente!",null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}

		return ret;

	}
	
	public String cancelar(){
		borraListas();
		return null;
	}

	
	private void borraListas() {	
		this.setCargos(null);
		this.setCiudades(null);
		this.setDetalleGrupoContratados(null);
		this.setDepartamentos(null);
		
		this.setEmpleado(null);	
		this.setPeriodo(null);
		
		this.tipoReporte = "Cargo";
		this.getListaProgramaVacacion().clear();
	}	
	
	
	public void verificaPlanificacion(){
		
		String msg="";
		try{
			Calendar calendar=Calendar.getInstance();
			
			calendar.setTime(periodo);
			int year=calendar.get(Calendar.YEAR);
			this.buscarDatos();		
			this.listaProgramaVacacion.clear();
			this.listaProgramaVacacion.addAll(BuscarDatos(this.getEmpleados(),year,this.empleadoUsuario, this.acceso));	
			
			FacesMessages.instance().clear();
			FacesMessages.instance().add("Datos verificados!");		
			msg="GOOD";
		}catch(Exception e){}
		
		if(msg.equals("")){
			InvalidValue iv= new InvalidValue("Seleccione los datos correctamente!",null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}
	}
	
	public List<ProgramaVacacion> BuscarDatos(List<Empleado> empleado,int year, Empleado empleadoUsuario, int acceso){		
		
		List<ProgramaVacacion> datos = new ArrayList <ProgramaVacacion>();	
		
		if( (getTipoReporte().equals("Empleado")) ){
			//*******************************
			for(Empleado e: this.empleados){
				programaVacacionList.getProgramaVacacion().setEmpleado(e);
				programaVacacionList.getProgramaVacacion().setPeriodo(year);
				
				programaVacacion=new ProgramaVacacion();
				
				List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
				aux=programaVacacionList.getListaProgramaVacacion();
				
				if(aux.size()==0){
					programaVacacion.setEmpleado(e);
					aux.add(programaVacacion);
				}								
				datos.addAll(aux);	
			}
			//*******************************
		}else{
			
			if(acceso==Constantes.ACCESO_INDIVIDUAL ){
				
				programaVacacionList.getProgramaVacacion().setEmpleado(empleadoUsuario);
				programaVacacionList.getProgramaVacacion().setPeriodo(year);
				
				programaVacacion=new ProgramaVacacion();
				
				List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
				aux=programaVacacionList.getListaProgramaVacacion();
				
				if(aux.size()==0){
					programaVacacion.setEmpleado(empleadoUsuario);
					aux.add(programaVacacion);
				}					
				datos.addAll(aux);
			
			}else if (acceso == Constantes.ACCESO_SUBORDINADOS){
				for(Empleado e : empleado){
					if(e.getEmpleado().getEmplId().equals(empleadoUsuario.getEmplId())){
						programaVacacionList.getProgramaVacacion().setEmpleado(e);
						programaVacacionList.getProgramaVacacion().setPeriodo(year);
						
						programaVacacion=new ProgramaVacacion();
						
						List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
						aux=programaVacacionList.getListaProgramaVacacion();
						
						if(aux.size()==0){
							programaVacacion.setEmpleado(e);
							aux.add(programaVacacion);
						}					
						datos.addAll(aux);
					}
				}
				//*******************************
				programaVacacionList.getProgramaVacacion().setEmpleado(empleadoUsuario);
				programaVacacionList.getProgramaVacacion().setPeriodo(year);
				
				programaVacacion=new ProgramaVacacion();
				
				List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
				aux=programaVacacionList.getListaProgramaVacacion();
				
				if(aux.size()==0){
					programaVacacion.setEmpleado(empleadoUsuario);
					aux.add(programaVacacion);
				}								
				datos.addAll(aux);			
				//*******************************
			}else if (acceso == Constantes.ACCESO_TODOS){
				for(Empleado e : empleado){
					programaVacacionList.getProgramaVacacion().setEmpleado(e);
					programaVacacionList.getProgramaVacacion().setPeriodo(year);
					
					programaVacacion=new ProgramaVacacion();
					
					List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
					aux=programaVacacionList.getListaProgramaVacacion();
					
					if(aux.size()==0){
						programaVacacion.setEmpleado(e);
						aux.add(programaVacacion);
					}
					datos.addAll(aux);
				}
			}		
			
		}
		
			
			
		
		return datos;		
	}
	
//	public int diferenciaFechas(Date fecha_inicio, Date fecha_fin){		
//		
//		if(fecha_inicio==null&&fecha_fin==null){
//			//Si son nulos los valores de las fechas, retornar 0 días
//			return 0;
//		}else{
//			if(fecha_inicio!=null&&fecha_fin!=null){
//				if (!fecha_inicio.after(fecha_fin)){
//					//Si la fecha
//					long diferencia= fecha_fin.getTime()-fecha_inicio.getTime();
//					long diferenciaDiasLong = (diferencia / (1000 * 60 * 60 * 24))+1;
//					int diferenciaDiasInt=(int)diferenciaDiasLong;
//					return diferenciaDiasInt;
//				}else{
//					return -1;
//				}
//			}else{
//				//Si no son nulos ambos valores de las fechas, retornar -1 días que indica un error
//				return -1;
//			}
//		}	
//		
//	}
	
	private int diferenciaFechas(Empleado empleado, Date fecha_inicio, Date fecha_fin){
		
		int totaldiasLaborables=0;
		
		if(fecha_inicio==null&&fecha_fin==null){
			//Si son nulos los valores de las fechas, retornar 0 días
			//return 0;
			totaldiasLaborables=0;
		}else{
			if(fecha_inicio!=null&&fecha_fin!=null){
				
				if (!fecha_inicio.after(fecha_fin)){
					
					DetalleGrupoContratado grupoEmpleado=BuscarGrupoContratado(empleado.getDetalleGrupoContratado().getDgcoId());
					
					ArrayList<Long> ciudad=new ArrayList<Long>();
					
					ciudad.add(empleado.getCiudad().getCiudId());
					
					if(grupoEmpleado.getActivarRegla() && grupoEmpleado.getSolicitudVacacion()){
						
						int countdiasLaborables=0;
						
						int countdiasFeriados=0;
						
						int countdiasRecuperables=0;
						
						ArrayList<Long> listaCiudades=new ArrayList<Long>();
						
						listaCiudades.add(empleado.getCiudad().getCiudId());
						
						
						Date inicio=fecha_inicio;
						
						Date fin=fecha_fin;
						
						
						while(inicio.compareTo(fin)==0 || inicio.before(fin)){
							
							Calendar index=Calendar.getInstance();
							
							index.setTime(inicio);							
								
							int diaSemana=index.get(Calendar.DAY_OF_WEEK);
							
								
							if(diaSemana>=2 && diaSemana<=6){
								
								countdiasLaborables++;	
								
							}							
								
							index.add(Calendar.DATE, 1);
							
							inicio=index.getTime();
							
						}
						
						
						//Verificar Feriados
						if(grupoEmpleado.getActivarFeriados()){
							List<CiudadFeriado> ciudades=RetornarCiudadFeriado(fecha_inicio,fecha_fin,ciudad);					
							for(CiudadFeriado cf: ciudades){
								Calendar index=Calendar.getInstance();
								index.setTime(cf.getFeriado().getFecha());
								
								int diaSemana=index.get(Calendar.DAY_OF_WEEK);
								log.info("FERIADO::::::::::::::::::::::::::::::::"+diaSemana);
								if(diaSemana>=2 && diaSemana<=6){
									countdiasFeriados++;
								}
							}
						}
						//*************************************
						
						
						//Verificar Recuperables
						if(grupoEmpleado.getActivarRecuperable()){
							List<CiudadFeriado> ciudades=RetornarCiudadRecuperable(fecha_inicio,fecha_fin,ciudad);
							
							for(CiudadFeriado cf: ciudades){
								Calendar index=Calendar.getInstance();
								index.setTime(cf.getFeriado().getFechaRecupera());
								
								int diaSemana=index.get(Calendar.DAY_OF_WEEK);
								
								log.info("RECUPERABLE::::::::::::::::::::::::::::::::"+diaSemana);
								
								if(diaSemana==7 || diaSemana==1){
									countdiasRecuperables++;
								}
							}
						}
						//*************************************
						
						totaldiasLaborables=countdiasLaborables+countdiasRecuperables-countdiasFeriados;				
						
					}else{
						//*************************************************************
						//NO TOMA EN CUENTA DÍAS HÁBILES Y LIBRES
						//Si la fecha
						long diferencia= fecha_fin.getTime()-fecha_inicio.getTime();
						long diferenciaDiasLong = (diferencia / (1000 * 60 * 60 * 24))+1;
						int diferenciaDiasInt=(int)diferenciaDiasLong;
						//return diferenciaDiasInt;
						totaldiasLaborables=diferenciaDiasInt;
						//*************************************************************
					}
					
				}else{
					//return -1;
					totaldiasLaborables=-1;
				}				
				
			}else{
				//Si no son nulos ambos valores de las fechas, retornar -1 días que indica un error
				//return -1;
				totaldiasLaborables=-1;
			}
		}		
		
		return totaldiasLaborables;
		
	}
	
	//Retorna feriados
		private List<CiudadFeriado> RetornarCiudadFeriado(Date fechaDesde, Date fechaHasta, ArrayList<Long> ciudades){
			CiudadFeriadoList ciudadFeriadoList=(CiudadFeriadoList) Component.getInstance("ciudadFeriadoList", true);
			
			ciudadFeriadoList.setFeriadoDesde(null);
			ciudadFeriadoList.setFeriadoHasta(null);
			ciudadFeriadoList.setRecuperableDesde(null);
			ciudadFeriadoList.setRecuperableHasta(null);
			
			ciudadFeriadoList.setFeriadoDesde(fechaDesde);
			ciudadFeriadoList.setFeriadoHasta(fechaHasta);
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
		
		private List<CiudadFeriado> RetornarCiudadRecuperable(Date fechaDesde, Date fechaHasta, ArrayList<Long> ciudades){
			CiudadFeriadoList ciudadFeriadoList=(CiudadFeriadoList) Component.getInstance("ciudadFeriadoList", true);
			
			ciudadFeriadoList.setFeriadoDesde(null);
			ciudadFeriadoList.setFeriadoHasta(null);
			ciudadFeriadoList.setRecuperableDesde(null);
			ciudadFeriadoList.setRecuperableHasta(null);
			
			ciudadFeriadoList.setRecuperableDesde(fechaDesde);
			ciudadFeriadoList.setRecuperableHasta(fechaHasta);
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
	
	
	public int fechaInicioValida(Date fecha_inicio,int year){
		if(fecha_inicio==null){
			return year;
		}else{
			Calendar calendar=Calendar.getInstance();		
			calendar.setTime(fecha_inicio);			
			int aux=calendar.get(Calendar.YEAR);	
			return aux;
		}
	}
	
	public int FechaRepetidas(Date fi, Date fo,Date fi1, Date fo1,Date fi2, Date fo2,Date fi3, Date fo3){		
		
		int response=0;
		Vector <Date> fin= new Vector<Date>();
		Vector <Date> fou= new Vector<Date>();
		int counter=0;
		
		if(fi!=null&&fo!=null){
			fin.addElement(fi);
			fou.addElement(fo);
			counter ++;
		}		
		
		if(fi1!=null&&fo1!=null){
			fin.addElement(fi1);
			fou.addElement(fo1);
			counter ++;
		}
			
		if(fi2!=null&&fo2!=null){
			fin.addElement(fi2);
			fou.addElement(fo2);
			counter ++;
		}
			
		if(fi3!=null&&fo3!=null){
			fin.addElement(fi3);
			fou.addElement(fo3);
			counter ++;
		}	
		
		if(counter>1){
			int w=0;
			while(w<counter){
				Date fi_a=fin.elementAt(w);
				Date fo_a=fou.elementAt(w);
				
				for(int i=0;i<counter;i++){			
					if(w!=i){
						Date fi_ne=fin.elementAt(i);
						Date fo_ne=fou.elementAt(i);
						
							if((fi_a.after(fi_ne)&&fi_a.before(fo_ne))||
									(fo_a.after(fi_ne)&&fo_a.before(fo_ne))||
										fi_a.compareTo(fi_ne)==0||fi_a.compareTo(fo_ne)==0||
											fo_a.compareTo(fi_ne)==0||fo_a.compareTo(fo_ne)==0){
								response= -1;
								break;
							}
					}			
				}	
				if(response==-1)
					break;
				w++;
			}			
		}	
		
		return response;
		
	}
	
	//Metodo para registrar y actualizar registros
	public void guardar(){	
		
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(periodo);
		
		int year=calendar.get(Calendar.YEAR);		
		
		List<ProgramaVacacion> datos = new ArrayList <ProgramaVacacion>();
		int datosGuardados=0;
		
		datos=this.getListaProgramaVacacion();	
				
		if(datos.size()!=0){			
		
			for(ProgramaVacacion p : datos){
				programaVacacionList.getProgramaVacacion().setEmpleado(p.getEmpleado());
				programaVacacionList.getProgramaVacacion().setPeriodo(year);
				
				Date fin=p.getFechaInicio();
				Date fou=p.getFechaFin();
				
				Date fin1=p.getFechaInicio1();
				Date fou1=p.getFechaFin1();
				
				Date fin2=p.getFechaInicio2();
				Date fou2=p.getFechaFin2();
				
				Date fin3=p.getFechaInicio3();
				Date fou3=p.getFechaFin3();					
				
				/*Obtener la diferencia de días entre las fechas de cada rango. Si retorna -1 hay un error*/
				int dias=diferenciaFechas(p.getEmpleado(),fin,fou);
				int dias1=diferenciaFechas(p.getEmpleado(),fin1,fou1);
				int dias2=diferenciaFechas(p.getEmpleado(),fin2,fou2);
				int dias3=diferenciaFechas(p.getEmpleado(),fin3,fou3);				
				
				int diferenciaDiasInt=1;
				int diasVacaciones= 1;
				
				int yearFin=fechaInicioValida(fin, year);
				int yearFin1=fechaInicioValida(fin1, year);
				int yearFin2=fechaInicioValida(fin2, year);
				int yearFin3=fechaInicioValida(fin3, year);
				
				log.info("DIAS.................................................:"+dias);
				log.info("DIAS1.................................................:"+dias1);
				log.info("DIAS2.................................................:"+dias2);
				log.info("DIAS3.................................................:"+dias3);
				
				int controlaFechas=0;
				
				if(dias!=-1&&dias1!=-1&&dias2!=-1&&dias3!=-1){
					diferenciaDiasInt=dias+dias1+dias2+dias3;
					
					controlaFechas=FechaRepetidas(fin,fou,fin1,fou1,fin2,fou2,fin3,fou3);
					
					Long emplId=p.getEmpleado().getEmplId();
					
					//int diasVacacionContrato=diasVacacionContrato(p.getEmpleado().getDetalleGrupoContratado().getDgcoId());
					DetalleGrupoContratado grupoEmpleado=BuscarGrupoContratado(p.getEmpleado().getDetalleGrupoContratado().getDgcoId());
					
					diasVacaciones= diasVacacion(emplId,year,grupoEmpleado);
				
				}
				
				if((dias!=-1&&dias1!=-1&&dias2!=-1&&dias3!=-1)&&(yearFin==year&&yearFin1==year&&yearFin2==year&&yearFin3==year)
						&&(diferenciaDiasInt<=diasVacaciones)&&(diferenciaDiasInt>0)&&(p.getFechaInicio()!=null&&p.getFechaFin()!=null)&&controlaFechas==0){
					
					List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
					aux=programaVacacionList.getListaProgramaVacacion();				
										
					if(aux.size()==0){
						//log.info("Insertado!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
						programaVacacionHome.clearInstance();
						programaVacacionHome.setInstance(p);				
						programaVacacionHome.getInstance().setPeriodo(year);
						programaVacacionHome.getInstance().setGenerado(0);
						programaVacacionHome.getInstance().setDias_vacacion(diferenciaDiasInt);						
						datosGuardados++;
						programaVacacionHome.persist();		

						//Auditoria creacion de programacion de vacaciones
					;
						//************************************************
						
					}else{
						for (ProgramaVacacion p1 : aux){
							
							//if(p1.getGenerado()==0||(this.acceso==Constantes.ACCESO_TODOS)){
							
							if( (p1.getGenerado()==0 && this.permisos.getActualizarPendiente()) || (p1.getGenerado()==1 && this.permisos.getActualizarAutorizado()) ){
								
								boolean com1=comparaFecha(p1.getFechaInicio(),p.getFechaInicio());
								boolean com2=comparaFecha(p1.getFechaFin(),p.getFechaFin());
								
								boolean com3=comparaFecha(p1.getFechaInicio1(),p.getFechaInicio1());
								boolean com4=comparaFecha(p1.getFechaFin1(),p.getFechaFin1());
								
								boolean com5=comparaFecha(p1.getFechaInicio2(),p.getFechaInicio2());
								boolean com6=comparaFecha(p1.getFechaFin2(),p.getFechaFin2());
								
								boolean com7=comparaFecha(p1.getFechaInicio3(),p.getFechaInicio3());
								boolean com8=comparaFecha(p1.getFechaFin3(),p.getFechaFin3());
								
								/*log.info("COM1...........................: "+com1);
								log.info("COM2...........................: "+com2);
								log.info("COM3...........................: "+com3);
								log.info("COM4...........................: "+com4);
								log.info("COM5...........................: "+com5);
								log.info("COM6...........................: "+com6);
								log.info("COM7...........................: "+com7);
								log.info("COM8...........................: "+com8);*/						
								
								if(com1||com2||com3||com4||com5||com6||com7||com8){
									//log.info("Actualizo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
								
									//Auditoria										programaVacacionHome.setCadenaAnterior(programaVacacionHome.RetornarCadena(p1));
									//
										
									programaVacacionHome.clearInstance();		
									programaVacacionHome.setInstance(p1);
									
									programaVacacionHome.getInstance().setFechaInicio(p.getFechaInicio());
									programaVacacionHome.getInstance().setFechaFin(p.getFechaFin());									
									programaVacacionHome.getInstance().setFechaInicio1(p.getFechaInicio1());
									programaVacacionHome.getInstance().setFechaFin1(p.getFechaFin1());
									programaVacacionHome.getInstance().setFechaInicio2(p.getFechaInicio2());
									programaVacacionHome.getInstance().setFechaFin2(p.getFechaFin2());
									programaVacacionHome.getInstance().setFechaInicio3(p.getFechaInicio3());
									programaVacacionHome.getInstance().setFechaFin3(p.getFechaFin3());
									
									programaVacacionHome.getInstance().setDias_vacacion(diferenciaDiasInt);		
									
									datosGuardados++;
									programaVacacionHome.update();	
									
									//Auditoria modificación de programacion de vacaciones
								;
									//************************************************
									
								}else{								
									datosGuardados++;
								}							
							}							
						}
					}					
				}
			}
		}
			
		//datos.removeAll(lista_eliminacion);
		
		FacesMessages.instance().clear();
			
		if (datos.size()==datosGuardados){	
			FacesMessages.instance().add("Se guardaron todos los datos correctamente!");			
		}else{
			InvalidValue iv= new InvalidValue("No se guardaron todos los datos, verifique que la información esté correctamente ingresada!",null,null,null,null);			
			FacesMessages.instance().add(iv);
		}		
	}
	
	public void generarDatos(){	
		
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(periodo);
		
		int year=calendar.get(Calendar.YEAR);		
		
		List<ProgramaVacacion> datos = new ArrayList <ProgramaVacacion>();
		
		datos=this.getListaProgramaVacacion();	
				
		if(datos.size()!=0){		
			for(ProgramaVacacion p : datos){			
				programaVacacionList.getProgramaVacacion().setEmpleado(p.getEmpleado());
				programaVacacionList.getProgramaVacacion().setPeriodo(year);			
					
				List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
				aux=programaVacacionList.getListaProgramaVacacion();				
										
				if(aux.size()!=0){
					p.setGenerado(1);
					for (ProgramaVacacion p1 : aux){						
						if(p1.getGenerado()!=1){	
						
							programaVacacionHome.setCadenaAnterior(programaVacacionHome.RetornarCadena(p1));
							//
								
							programaVacacionHome.clearInstance();		
							programaVacacionHome.setInstance(p1);
							programaVacacionHome.getInstance().setGenerado(1);	
							programaVacacionHome.update();
						}						
					}								
				}
			}
		}
		
				
	}
	
	public int eliminaDatos(){	
		
		int counter=0;
		
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(periodo);
		
		int year=calendar.get(Calendar.YEAR);		
		
		List<ProgramaVacacion> datos = new ArrayList <ProgramaVacacion>();
		//List<ProgramaVacacion> lista_eliminacion = new ArrayList <ProgramaVacacion>();
		
		datos=this.getListaProgramaVacacion();	
				
		if(datos.size()!=0){		
			for(ProgramaVacacion p : datos){			
				programaVacacionList.getProgramaVacacion().setEmpleado(p.getEmpleado());
				programaVacacionList.getProgramaVacacion().setPeriodo(year);			
					
				List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
				aux=programaVacacionList.getListaProgramaVacacion();				
										
				if(aux.size()!=0){			
					
					for (ProgramaVacacion p1 : aux){						
						//if(p1.getGenerado()==0||(this.acceso==Constantes.ACCESO_TODOS)){
						if( (p1.getGenerado()==0 && this.permisos.getEliminarPendiente()) || (p1.getGenerado()==1 && this.permisos.getEliminarAutorizado()) ){
													
							programaVacacionHome.clearInstance();	
							programaVacacionHome.setInstance(p1);
							
							programaVacacionHome.remove();
							
							//Autorización de programación de vacaciones							
							
							//************************************************
							
							if(counter!=-1)
								counter ++;
							//lista_eliminacion.add(p);
						}						
					}								
				}else{
					counter=-1;
				}
			}
		}	
		
		//datos.removeAll(lista_eliminacion);
		
		if(counter>0)
			this.listaProgramaVacacion.clear();
		
		
		return counter;
	}
	
	public boolean comparaFecha(Date d1, Date d2){
//		log.info("DATE1.................................................:"+d1);
//		log.info("DATE2.................................................:"+d2);
		if(d1==null&&d2==null){
			return false;
		}else{
			if(d1!=null&&d2!=null){
				if((d1.compareTo(d2))!=0){
					return true;
				}else{
					return false;
				}
			}else{
				return true;
			}
		}
	}
	
//	public boolean desactivarRegistros(int generadoP){
////		if(generadoP!=null){
//		//log.info("generadoP..................!!!!!!!!!!!!!!!!!!!!!!!!!!1");
//			if(generadoP==1){
//				if(acceso==Constantes.ACCESO_INDIVIDUAL||acceso==Constantes.ACCESO_SUBORDINADOS){
//				//if(acceso==Constantes.ACCESO_TODOS){
//					return true;
//				}else{
//					return false;
//				}
//			}else{
//				return false;
//			}
////		}else{
////			return false;
////		}
//	}
	

	@Override
	public String crearPlanificacion() {
		FacesMessages.instance().clear();
		if (habilitar()==true){
			InvalidValue iv= new InvalidValue("Existen datos incorrectos",null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}else{		
			this.generarDatos();
			FacesMessages.instance().clear();
			FacesMessages.instance().add("Se autorizó correctamente!");	
		}
		return "ok";
	}
	
	public String eliminarPlanificacion() {
		FacesMessages.instance().clear();
		int aux=this.eliminaDatos();
		if (aux==-1){
			InvalidValue iv= new InvalidValue("No se eliminaron todos los registros!",null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}else{
			if(aux==0){
				InvalidValue iv= new InvalidValue("No se eliminaron registros!",null,null,null,null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}else{
				FacesMessages.instance().clear();
				FacesMessages.instance().add("Se eliminaron los datos correctamente!");	
			}				
		}
		return "ok";
	}	
	
	public boolean verificar(Date in, Date fi){
		boolean respuesta=false;		
		if(in!=null&&fi!=null)
			if(in.after(fi))
				respuesta=true;
		else
			respuesta=false;
		return respuesta;
	}	
	
	public boolean verificaPeriodo(Date entrada){
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(periodo);		
		int year=calendar.get(Calendar.YEAR);
		
		boolean respuesta=false;		
		if(entrada!=null){	
			calendar.setTime(entrada);			
			int aux=calendar.get(Calendar.YEAR);
			if(aux!=year)
				respuesta=true;
		}
		return respuesta;
	}
	
	public boolean habilitar(){
		boolean respuesta=false;
		if(this.permisos.getAutorizarVacacion()){
			
			if(periodo!=null){
				Calendar calendar=Calendar.getInstance();		
				calendar.setTime(periodo);
				
				int year=calendar.get(Calendar.YEAR);	
				
				List<ProgramaVacacion> datos = new ArrayList <ProgramaVacacion>();		
				datos=this.getListaProgramaVacacion();	
				
				if (datos.size()==0){
					respuesta=true;
				}else{
					for(ProgramaVacacion p:datos){
						Date fin=p.getFechaInicio();
						Date fou=p.getFechaFin();
						
						Date fin1=p.getFechaInicio1();
						Date fou1=p.getFechaFin1();
						
						Date fin2=p.getFechaInicio2();
						Date fou2=p.getFechaFin2();
						
						Date fin3=p.getFechaInicio3();
						Date fou3=p.getFechaFin3();
						
						int dias=diferenciaFechas(p.getEmpleado(),fin,fou);
						int dias1=diferenciaFechas(p.getEmpleado(),fin1,fou1);
						int dias2=diferenciaFechas(p.getEmpleado(),fin2,fou2);
						int dias3=diferenciaFechas(p.getEmpleado(),fin3,fou3);
						
						int yearFin=fechaInicioValida(fin, year);
						int yearFin1=fechaInicioValida(fin1, year);
						int yearFin2=fechaInicioValida(fin2, year);
						int yearFin3=fechaInicioValida(fin3, year);
						
						int diferenciaDiasInt=1;				
						int controlaFechas=0;
						
						if(dias!=-1&&dias1!=-1&&dias2!=-1&&dias3!=-1){
							diferenciaDiasInt=dias+dias1+dias2+dias3;				
							controlaFechas=FechaRepetidas(fin,fou,fin1,fou1,fin2,fou2,fin3,fou3);
						}
						if((dias!=-1&&dias1!=-1&&dias2!=-1&&dias3!=-1)&&(yearFin==year&&yearFin1==year&&yearFin2==year&&yearFin3==year)
								&&(diferenciaDiasInt>0)&&(p.getFechaInicio()!=null&&p.getFechaFin()!=null)&&controlaFechas==0){					
							respuesta=false;
						}else{
							respuesta=true;
							break;
						}
					}
				}	
			}else{
				respuesta=false;
			}
			
		}else{
			respuesta=true;
		}
			
		return respuesta;
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
		if (empleados == null) {
			empleados = new ArrayList<Empleado>();
		}
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
	
	public Date getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Date periodo){
		this.periodo = periodo;
	}
	
	public List<ProgramaVacacion> getListaProgramaVacacion() {
		if (listaProgramaVacacion == null) {
			listaProgramaVacacion = new ArrayList <ProgramaVacacion>();
		}
		return listaProgramaVacacion;
	}

	public void setListaProgramaVacacion(List<ProgramaVacacion> listaProgramaVacacion) {
		this.listaProgramaVacacion = listaProgramaVacacion;
	}

	public List<DetalleGrupoContratado> getDetalleGrupoContratado() {
		if (detalleGrupoContratado == null) {
			detalleGrupoContratado = new ArrayList<DetalleGrupoContratado>();
		}
		return detalleGrupoContratado;
	}

	public void setDetalleGrupoContratado(List<DetalleGrupoContratado> detalleGrupoContratado) {
		this.detalleGrupoContratado = detalleGrupoContratado;
	}
	
	public List<EmpleadoPeriodoVacacion> getEmpleadoPeriodoVacacion() {
		if (empleadoPeriodoVacacion == null) {
			empleadoPeriodoVacacion = new ArrayList<EmpleadoPeriodoVacacion>();
		}		
		return empleadoPeriodoVacacion;
	}

	public void setEmpleadoPeriodoVacacion(List<EmpleadoPeriodoVacacion> empleadoPeriodoVacacion) {
		this.empleadoPeriodoVacacion = empleadoPeriodoVacacion;
	}
	
	public int diasVacacion(Long emplId, int year,DetalleGrupoContratado grupoEmpleado/*int diasVacacionContrato*/){
		double dv=0;		
		for(EmpleadoPeriodoVacacion epv: empleadoPeriodoVacacion){
			if(emplId.equals(epv.getEmpleado().getEmplId())){
				
				Calendar calendar=Calendar.getInstance();		
				calendar.setTime(epv.getFechaDesde());
				
				//log.info("epv.getFechaDesde()..........................:"+epv.getFechaDesde());
				
				int year_curr=calendar.get(Calendar.YEAR);
				
				//log.info("year_curr..........................:"+year_curr);
				//log.info("year..........................:"+year);
				
				if(year==year_curr){
					//dv=epv.getHoras();
					double saldoActual = gestionPermisoVacacion.buscarSaldoVacaciones(epv);
					dv=saldoActual;
				}else{
					if(year_curr==(year-1)){			
						double saldoActual = gestionPermisoVacacion.buscarSaldoVacaciones(epv);
						
						int limite=grupoEmpleado.getMaximoHoras();
						int diasVacacionContrato=grupoEmpleado.getHorasVacacion();
						
						dv=saldoActual+diasVacacionContrato;
						
						log.info("OPTION...................................................saldoActual:"+saldoActual);
						log.info("OPTION...................................................diasVacacionContrato:"+diasVacacionContrato);
						log.info("OPTION...................................................limite:"+limite);
						
						if(dv>limite)
							dv=limite;
					}
				}		
							
				break;
			}
		}		
		int i_dv=(int)dv;
		log.info("i_dv...dias currently..........................:"+i_dv);
		return i_dv;
	}
	
	public  int diasVacacionContrato(Long dgcoId){
		short dv=0;		
		for(DetalleGrupoContratado d: detalleGrupoContratado){
			if(d.getDgcoId().equals(dgcoId)){			
				dv=d.getHorasVacacion();								
				break;
			}
		}		
		int i_dv=(int)dv;
		return i_dv;
	}
	
	public DetalleGrupoContratado BuscarGrupoContratado(Long dgcoId){
		DetalleGrupoContratado grupo=new DetalleGrupoContratado();	
		for(DetalleGrupoContratado d: detalleGrupoContratado){
			if(d.getDgcoId().equals(dgcoId)){			
				grupo=d;								
				break;
			}
		}		
		return grupo;
	}
	
	public boolean habilitarGeneraracion(){
		if(this.acceso==Constantes.ACCESO_SUBORDINADOS||this.acceso==Constantes.ACCESO_TODOS)
			return true;
		else
			return false;
	}
	
	public int VerificaSaldo(Empleado e){
		
		try{
			Calendar calendar=Calendar.getInstance();		
			calendar.setTime(periodo);		
			int year=calendar.get(Calendar.YEAR);	
			
			Long emplId=e.getEmplId();			
			//int diasVacacionContrato=diasVacacionContrato(e.getEmpleado().getDetalleGrupoContratado().getDgcoId());
			DetalleGrupoContratado grupoEmpleado=BuscarGrupoContratado(e.getDetalleGrupoContratado().getDgcoId());
			
			int diasVacaciones= diasVacacion(emplId,year,grupoEmpleado);
			
			return diasVacaciones;
		}catch(Exception ex){
			return 0;
		}
		
	}
	
	public boolean validacionSaldo(Empleado empleado, int saldo, Date fin, Date fou, Date fin1, Date fou1, Date fin2, Date fou2, Date fin3, Date fou3){
				
		int dias=diferenciaFechas(empleado,fin,fou);
		int dias1=diferenciaFechas(empleado,fin1,fou1);
		int dias2=diferenciaFechas(empleado,fin2,fou2);
		int dias3=diferenciaFechas(empleado,fin3,fou3);
		int diferenciaDiasInt=0;
		if(dias!=-1&&dias1!=-1&&dias2!=-1&&dias3!=-1){
			diferenciaDiasInt=dias+dias1+dias2+dias3;
		}
		if(diferenciaDiasInt>saldo)
			return true;
		else
			return false;
	}
	
	public int ObtenerDiasTomados(Empleado empleado, Date fin, Date fou, Date fin1, Date fou1, Date fin2, Date fou2, Date fin3, Date fou3){
		
		int dias=diferenciaFechas(empleado,fin,fou);
		int dias1=diferenciaFechas(empleado,fin1,fou1);
		int dias2=diferenciaFechas(empleado,fin2,fou2);
		int dias3=diferenciaFechas(empleado,fin3,fou3);
		int diferenciaDiasInt=0;
		if(dias!=-1&&dias1!=-1&&dias2!=-1&&dias3!=-1){
			diferenciaDiasInt=dias+dias1+dias2+dias3;
		}
		
		return diferenciaDiasInt;
	}
	
	
	//----------------------------
	
	private Long filaRemove;//=new Empleado();
	
	private String EmpleadoRemove; 
	
	public Long getFilaRemove() {
		return filaRemove;
	}

	public void setFilaRemove(Long filaRemove) {
		this.filaRemove = filaRemove;
		
		log.info("...............................INGRESO A SET_FILA_REMOVE");
		log.info("...............................filaRemove:"+this.filaRemove);
		
		String empleadoString="";
		
		for(ProgramaVacacion pv: listaProgramaVacacion){
			if(pv.getEmpleado().getEmplId().equals(this.filaRemove)){
				empleadoString=pv.getEmpleado().getApellido()+" "+pv.getEmpleado().getNombre();
				break;
			}
		}
		
		
		EmpleadoRemove="¿Desea descartar de la lista a "+empleadoString+"?";
		//**********
	}
	
	//Descartar Empleado de la Lista
	public void DescartarEmpleado(){
		if(listaProgramaVacacion.size()>1){
			for(ProgramaVacacion pv: listaProgramaVacacion){
				if(pv.getEmpleado().getEmplId().equals(this.filaRemove)){
					listaProgramaVacacion.remove(pv);
					break;
				}
			}
		}else{
			listaProgramaVacacion=new ArrayList<ProgramaVacacion>();
			CargarPagina();
		}		
	}
	
	//Cargar Página web	
	private void CargarPagina(){
		try{
			String url="/fulltime/fulltime/ProgramaVacacion.seam"; //url donde se redirige la pantalla
	        FacesContext facesContext=new FacesContext();
	        facesContext.getContext();
			javax.faces.context.FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
		
	
	public String getEmpleadoRemove() {
		return EmpleadoRemove;
	}

	public void setEmpleadoRemove(String empleadoRemove) {
		EmpleadoRemove = empleadoRemove;
	}
		
}