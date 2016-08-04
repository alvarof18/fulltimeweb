package com.casapazmino.fulltime.procesos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
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
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.ProgramaVacacion;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;
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
	
	private EmpleadoPeriodoVacacion empleadoPeriodoVacacionEdit;
	
	private Date periodo;

	private ProgramaVacacion programaVacacion;
	
	private List<ProgramaVacacion> listaProgramaVacacion;
	private int acceso;
	private Empleado empleadoUsuario;
	
	private PermisosMenu permisos;
	
	private String valueSelected;
	
	private int numeroFilas;
	
	private List<SelectItem> listaNumeroFilas;
	
	private Date inicio;
	private Date inicio1;
	private Date inicio2;
	private Date inicio3;
	
	private Date fin;
	private Date fin1;
	private Date fin2;
	private Date fin3;
	
	private Integer saldo;
	private Integer saldo1;
	private Integer saldo2;
	private Integer saldo3;
	
	private Integer dias;
	private Integer dias1;
	private Integer dias2;
	private Integer dias3;
	
	private Integer saldoPendiente;
	private Integer saldoPendiente1;
	private Integer saldoPendiente2;
	private Integer saldoPendiente3;
	
	private Integer diasTomados;
	
	private Boolean habilitarGuardar;
	
	private Integer diasMaximoContrato;
	
	private Integer periodoContrato;
	

	public ProgramaVacacionMultipleBean() {
		this.tipoReporte = "Cargo";
		
		numeroFilas=5;
		valueSelected="5";
		CargarListaNumeroFilas();
		habilitarGuardar = true;
		
	}
	
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
		habilitarGuardar = true;
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
		habilitarGuardar = true;
		
	    //empleadoPeriodoVacacionEdit = null;
	    //empleadoPeriodoVacacion.clear();
	    
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
	    
		
		this.listaProgramaVacacion.clear();

		try{
			Calendar calendar=Calendar.getInstance();
			
			calendar.setTime(periodo);
			int year=calendar.get(Calendar.YEAR);
			this.buscarDatos();		
			this.listaProgramaVacacion.addAll(BuscarDatos(this.getEmpleados(),year,this.empleadoUsuario, this.acceso));	
			ordenarLista();
			//new obtener vector de entero de numero de filas
			
			
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
	
	private void ordenarLista(){
		int count=1;
		for(ProgramaVacacion pv: listaProgramaVacacion){
			pv.setNumero(count);;
			count++;
		}
	}
	
	public int VerificaSaldo(Empleado emple){
		
		// Date desde, double diasVacacionContrato, EmpleadoPeriodoVacacion empleadoPeriodoVacacion
		
		int diasVacaciones= 0;
		Date hoy = new Date();
				
			DetalleGrupoContratado grupoEmpleado=BuscarGrupoContratado(emple.getDetalleGrupoContratado().getDgcoId());
			
			for(EmpleadoPeriodoVacacion epv:empleadoPeriodoVacacion){
				if(epv.getEmpleado().getEmplId() == emple.getEmplId()){
					log.info("Entre ");
					log.info("..................... DATOS " + hoy + grupoEmpleado.getTotalHoras() + epv);
					periodoContrato = grupoEmpleado.getMesesPeriodo();
					diasMaximoContrato = (int) grupoEmpleado.getHorasVacacion();
					diasVacaciones = diasVacacion(hoy,grupoEmpleado.getTotalHoras(),epv);
					periodoContrato = 0;
					diasMaximoContrato =0;
					break;
				}
				
			}
			
			return diasVacaciones;		
	}
	
//	public int ObtenerNumero(Long emplId){
//		int numero=1;
//		
//		for(ProgramaVacacion pv:listaProgramaVacacion){
//			
//			if(pv.getEmpleado().getEmplId().equals(emplId)){
//				break;
//			}else{
//				numero++;
//			}
//				
//		}
//		
//		return numero;
//		
//	}
	
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
					totaldiasLaborables=-2;
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
	
	public String RetornarMensaje(){
		
		String mensaje="javascript:Richfaces.hideModalPanel('popUpEdit');";		
		// String mensaje="";
		return mensaje;
	}
	
	public void GuardarRegistrosEmpleado(){
		
		diasTomados = 0;
		
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(periodo);
		
		int year=calendar.get(Calendar.YEAR);	
				
		for(int i = 0 ; i< listaProgramaVacacion.size(); i++){ //Ejecuto el for de esta forma porque estoy actualizando la lista y al mismo tiempo haciendo la persistencia
			
			if(listaProgramaVacacion.get(i).getNumero().equals(this.filaEdit)){
			
				listaProgramaVacacion.get(i).setPeriodo(year);
				
				listaProgramaVacacion.get(i).setFechaInicio(this.inicio);
				listaProgramaVacacion.get(i).setFechaFin(this.fin);	
				
				listaProgramaVacacion.get(i).setFechaInicio1(this.inicio1);
				listaProgramaVacacion.get(i).setFechaFin1(this.fin1);	
					
				listaProgramaVacacion.get(i).setFechaInicio2(this.inicio2);
				listaProgramaVacacion.get(i).setFechaFin2(this.fin2);	
				
				listaProgramaVacacion.get(i).setFechaInicio3(this.inicio3);
				listaProgramaVacacion.get(i).setFechaFin3(this.fin3);
								
				this.setDiasTomados(this.ObtenerDiasTomados(listaProgramaVacacion.get(i).getEmpleado(), inicio, fin, inicio1, fin1, inicio2, fin2, inicio3, fin3));
				
				listaProgramaVacacion.get(i).setDias_vacacion(diasTomados);
				
				programaVacacionList.getProgramaVacacion().setEmpleado(listaProgramaVacacion.get(i).getEmpleado());
				programaVacacionList.getProgramaVacacion().setPeriodo(year);
				
				List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
				aux=programaVacacionList.getListaProgramaVacacion();	
				
				programaVacacionHome.clearInstance();
				programaVacacionHome.setInstance(listaProgramaVacacion.get(i));	
				
				if(aux.size()==0){
								
					programaVacacionHome.getInstance().setGenerado(0);
					programaVacacionHome.persist();		
				
				}else{
					programaVacacionHome.setCadenaAnterior(programaVacacionHome.RetornarCadena(listaProgramaVacacion.get(i)));
					programaVacacionHome.update();
					entityManager.merge(listaProgramaVacacion.get(i)); 
				}
				
	/*			
				//Auditoria	
				programaVacacionHome.setCadenaAnterior(programaVacacionHome.RetornarCadena(listaProgramaVacacion.get(i)));
				//
					
				programaVacacionHome.clearInstance();		
				programaVacacionHome.setInstance(listaProgramaVacacion.get(i));
				
				programaVacacionHome.getInstance().setFechaInicio(listaProgramaVacacion.get(i).getFechaInicio());
				programaVacacionHome.getInstance().setFechaFin(listaProgramaVacacion.get(i).getFechaFin());									
				programaVacacionHome.getInstance().setFechaInicio1(listaProgramaVacacion.get(i).getFechaInicio1());
				programaVacacionHome.getInstance().setFechaFin1(listaProgramaVacacion.get(i).getFechaFin1());
				programaVacacionHome.getInstance().setFechaInicio2(listaProgramaVacacion.get(i).getFechaInicio2());
				programaVacacionHome.getInstance().setFechaFin2(listaProgramaVacacion.get(i).getFechaFin2());
				programaVacacionHome.getInstance().setFechaInicio3(listaProgramaVacacion.get(i).getFechaInicio3());
				programaVacacionHome.getInstance().setFechaFin3(listaProgramaVacacion.get(i).getFechaFin3());
				
				programaVacacionHome.getInstance().setDias_vacacion(listaProgramaVacacion.get(i).getdias);		
				
				datosGuardados++;
				programaVacacionHome.update();	
				
				*/
				
				break;
			}
	
	}
		
	}	
	
	public void generarDatos(){	
		
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(periodo);
		
		int year=calendar.get(Calendar.YEAR);		
		
		List<ProgramaVacacion> datos = new ArrayList <ProgramaVacacion>();
		
		datos=this.getListaProgramaVacacion();
		
		log.info("datos " + datos.size());
				
		if(datos.size()!=0){		
			for(ProgramaVacacion p : datos){			
				programaVacacionList.getProgramaVacacion().setEmpleado(p.getEmpleado());
				programaVacacionList.getProgramaVacacion().setPeriodo(year);			
					
				List<ProgramaVacacion> aux = new ArrayList <ProgramaVacacion>();
				aux=programaVacacionList.getListaProgramaVacacion();				
				
				log.info("AUXX " + aux );
				
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
				counter=0;
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
					//counter=-1;
				}
				
				
			}
		}	
		
		//datos.removeAll(lista_eliminacion);
		
		if(counter>=0){
			this.listaProgramaVacacion.clear();
			counter ++;
		}
		
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
	
	Fechas fechas = new Fechas();
	
	public int diasVacacion(Date desde, double diasVacacionContrato, EmpleadoPeriodoVacacion empleadoPeriodoVacacion){
		
		double saldoDoble=0;		
		
		log.info("......................DIASSSS " + desde + " " + empleadoPeriodoVacacion );
		
		if(desde!=null && empleadoPeriodoVacacion!=null){
			
			log.info(".---------------------- ENTREAA ");
			Date ultimaFechaCarga=empleadoPeriodoVacacion.getFechaDesde();
					
			if(ultimaFechaCarga.after(desde)){
				
				Calendar calendarPeriodo=Calendar.getInstance();		
				calendarPeriodo.setTime(ultimaFechaCarga);
				
				Calendar calendarDesde=Calendar.getInstance();		
				calendarDesde.setTime(desde);		
				int anioDesde=calendarDesde.get(Calendar.YEAR);
				
				if(anioDesde < calendarPeriodo.get(Calendar.YEAR)){
					calendarPeriodo.set(Calendar.YEAR,(anioDesde));
				}else{
					calendarPeriodo.set(Calendar.YEAR,(anioDesde-1));
				}

				ultimaFechaCarga=calendarPeriodo.getTime();				
			}	
			
			if(this.periodoContrato == 11){
				
				int month = 0;
				Calendar calendarPeriodo=Calendar.getInstance();		
				calendarPeriodo.setTime(ultimaFechaCarga);
				month = calendarPeriodo.get(Calendar.MONTH);
				calendarPeriodo.set(Calendar.MONTH, (month+1));
				
				ultimaFechaCarga = calendarPeriodo.getTime();
				
			}
			
			
			//Esta condicion busca "Corregir" el desface de dias que hay entre el reporte de Kardex
			 // Ya que en el Karde se asume que todos los meses tienen 30 dias en caso de correccion del Kardex Eliminar esta condicion
			if(fechas.numeroDias(ultimaFechaCarga,desde) >= 330 ){
				
				int days = 0;
				Calendar calendarPeriodo=Calendar.getInstance();		
				calendarPeriodo.setTime(desde);
				days = calendarPeriodo.get(Calendar.DAY_OF_YEAR);
				calendarPeriodo.set(Calendar.DAY_OF_YEAR, (days-6));
				
				desde = calendarPeriodo.getTime();	
			}
			
			double saldoProrrateado = (fechas.numeroDias(ultimaFechaCarga,desde) * diasVacacionContrato)  / 30; // asi esta en el reporte
			//double meses = this.calcularmeses(ultimaFechaCarga,desde);
			// double saldoProrrateado = meses* diasVacacionContrato;			
			double saldoActual = gestionPermisoVacacion.buscarSaldoVacaciones(empleadoPeriodoVacacion);
			saldoDoble=saldoActual+saldoProrrateado;
			log.info("......SaldoDoble: " + saldoDoble + " saldoProrrateado: " + saldoProrrateado + " saldo Actual " + saldoActual + " diasVacacionContrato " + diasVacacionContrato );
			log.info("ultimaFechaCarga " + ultimaFechaCarga + " desde: "+ desde );
			 log.info("optroro " + fechas.numeroDias(ultimaFechaCarga,desde));
		}	
		
		int saldoEntero=(int)saldoDoble;
		
		if (saldoEntero > diasMaximoContrato){
			 saldoEntero = diasMaximoContrato;
		}
		
		log.info("...................................saldoEntero: "+saldoEntero);
		return saldoEntero;
		
	}

	public DetalleGrupoContratado BuscarGrupoContratado(Long dgcoId){
		DetalleGrupoContratado grupo=new DetalleGrupoContratado();	
		
		if(detalleGrupoContratado == null){
			
			 //Consultar detalle grupo contratado 
	  		detalleGrupoContratadoList=(DetalleGrupoContratadoList) Component.getInstance("detalleGrupoContratadoList", true);		
	  		detalleGrupoContratadoList.setMaxResults(null);
	  		this.getDetalleGrupoContratado();
	  		detalleGrupoContratado=detalleGrupoContratadoList.getResultList();
			
		}
		
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
	
	private Integer filaRemove;//=new Empleado();
	private Integer filaEdit;
	
	private String EmpleadoRemove;	
	private String EmpleadoEdit; 
	
	private Empleado empleadoEditar;
	private double diasVacacionContrato;
	
	
	Date fechaCargaEmpleado;
	
	private String mensajeError;
	private String mensajeError1;
	private String mensajeError2;
	private String mensajeError3;
	private String mensajeErrorGeneral;
	
	public void diferencia() {
		dias=0;
		mensajeError="";
		habilitarGuardar = true;
		int total = 0;
		saldoPendiente=0;
		
		if(inicio!=null && fin!=null){
			if(!inicio.after(fin)){
				dias=diferenciaFechas(empleadoEditar,inicio,fin);
				mensajeError=InterpretarMensaje(dias,0);
			}else{
				mensajeError="Fecha inicial es mayor que fecha final";
			}
		}else{
			if(inicio==null && fin==null){
				mensajeError="Debe ingresar un rango inicial";
			}else{
				mensajeError="Debe ingresar un rango completo";
			}
		}
		
		if(empleadoPeriodoVacacionEdit == null){
				mensajeError = "El empleado no posee periodo de Vacacion asignado";	
		}
		
		mensajeErrorGeneral=InterpretarMensajeGeneral(FechaRepetidas(inicio,fin,inicio1,fin1,inicio2,fin2,inicio3,fin3));
		
		saldo=0;
		if(inicio!=null){			
			saldo=diasVacacion(inicio,diasVacacionContrato,empleadoPeriodoVacacionEdit);
		}
		
		if(saldo < 1 && fin!=null && inicio!=null){ // no tiene saldo de vacaciones
			mensajeError = "El empleado no posee saldo de vacaciones";
		}
		
		if(saldo > 0){
			total = (saldo - dias); // Valido que la sumatoria de la planificacion no exceda el saldo
		}
		
		if(total < 0){		
			mensajeErrorGeneral = "La planificacion excede el saldo de vacaciones";
		}
		
		if(mensajeError.equals("") && mensajeErrorGeneral.equals("")){
			habilitarGuardar = false;
		}
		
		if(inicio != null && saldo>0){
			saldoPendiente = total;
		}
				
		log.info("....................... SALDO " + saldo);
	
	}
	
	public void diferencia1() {
		dias1=0;
		habilitarGuardar = true;
		int total1 = 0;
		saldoPendiente1= 0;
		
		if(inicio1!=null && fin1!=null){
			if(!inicio1.after(fin1)){
				dias1=diferenciaFechas(empleadoEditar,inicio1,fin1);
				mensajeError1=InterpretarMensaje(dias1,1);
			}else{
				mensajeError1="Fecha inicial es mayor que fecha final";
			}
		}else{
			if(inicio1==null && fin1==null){
				mensajeError1="";
			}else{
				mensajeError1="Debe ingresar un rango completo";
			}
		}
		
		mensajeErrorGeneral=InterpretarMensajeGeneral(FechaRepetidas(inicio,fin,inicio1,fin1,inicio2,fin2,inicio3,fin3));
		
		saldo1=0;
		if(inicio1!=null){			
			saldo1=diasVacacion(inicio1,diasVacacionContrato,empleadoPeriodoVacacionEdit);
		}
		

		if(saldo1<1 && fin1!=null && inicio1!=null){ // no tiene saldo de vacaciones
			mensajeError1 = "El empleado no posee saldo de vacaciones";
		}
		
		if(saldo1 > 0){
			total1 = saldo1 - dias1; // Valido que la sumatoria de la planificacion no exceda el saldo
		}
		
		if(total1 < 0){		
			mensajeErrorGeneral = "La planificacion excede el saldo de vacaciones";
		}
		
		if(mensajeError1.equals("") && mensajeErrorGeneral.equals("")){
			habilitarGuardar = false;
		}
		
		if(inicio1 != null && saldo1>0){
			saldoPendiente1 = total1;
		}
				
	}
	
	public void diferencia2() {
		dias2=0;
		habilitarGuardar = true;
		int total2 = 0;
		
		saldoPendiente2 = 0;
		
		if(inicio2!=null && fin2!=null){
			if(!inicio2.after(fin2)){
				dias2=diferenciaFechas(empleadoEditar,inicio2,fin2);
				mensajeError2=InterpretarMensaje(dias2,2);
			}else{
				mensajeError2="Fecha inicial es mayor que fecha final";
			}
		}else{
			if(inicio2==null && fin2==null){
				mensajeError2="";
			}else{
				mensajeError2="Debe ingresar un rango completo";
			}
		}
		
		mensajeErrorGeneral=InterpretarMensajeGeneral(FechaRepetidas(inicio,fin,inicio1,fin1,inicio2,fin2,inicio3,fin3));
		
		saldo2=0;
		if(inicio2!=null){			
			saldo2=diasVacacion(inicio2,diasVacacionContrato,empleadoPeriodoVacacionEdit);
		}
		
		if(saldo2 < 1 && fin2!=null && inicio2!=null){ // no tiene saldo de vacaciones
			mensajeError2= "El empleado no posee saldo de vacaciones";
		}
		
		if(saldo2 > 0){
			total2 = saldo2 - dias2; // Valido que la sumatoria de la planificacion no exceda el saldo
		}
		
		if(total2 < 0){		
			mensajeErrorGeneral = "La planificacion excede el saldo de vacaciones";
		}
		
		if(mensajeError2.equals("") && mensajeErrorGeneral.equals("")){
			habilitarGuardar = false;
		}
		
		if(inicio2 != null && saldo2>0){
			saldoPendiente2 = total2;
		}		
	}
	
	public void diferencia3() {
		dias3=0;
		mensajeError3="";
		habilitarGuardar = true;
		saldoPendiente3 = 0;
		int total3 = 0;
		
		if(inicio3!=null && fin3!=null){
			if(!inicio3.after(fin3)){
				dias3=diferenciaFechas(empleadoEditar,inicio3,fin3);
				mensajeError3=InterpretarMensaje(dias3,3);
			}else{
				mensajeError3="Fecha inicial es mayor que fecha final";
			}
		}else{
			if(inicio3==null && fin3==null){
				mensajeError3="";
			}else{
				mensajeError3="Debe ingresar un rango completo";
			}
		}
		
		mensajeErrorGeneral=InterpretarMensajeGeneral(FechaRepetidas(inicio,fin,inicio1,fin1,inicio2,fin2,inicio3,fin3));
		
		saldo3=0;
		if(inicio3!=null){			
			saldo3=diasVacacion(inicio3,diasVacacionContrato,empleadoPeriodoVacacionEdit);
		}
		
		if(saldo3 < 1 && fin3!=null && inicio3!=null){ // no tiene saldo de vacaciones
			mensajeError3 = "El empleado no posee saldo de vacaciones";
		}
		
		if(saldo3 > 0){
			total3 = (saldo3 - dias3); // Valido que la sumatoria de la planificacion no exceda el saldo
		}
		
		if(total3 < 0){		
			mensajeErrorGeneral = "La planificacion excede el saldo de vacaciones";
		}
		
		if(mensajeError3.equals("") && mensajeErrorGeneral.equals("")){
			habilitarGuardar = false;
		}
		
		if(inicio3 != null && saldo3>0){
			saldoPendiente3 = total3;
		}			
	}
	
	public Integer getFilaEdit() {
		return filaEdit;
	}

	public void setFilaEdit(Integer filaEdit) {
		// fgdg
		mensajeError="";
		mensajeError1="";
		mensajeError2="";
		mensajeError3="";
		mensajeErrorGeneral="";
		
		habilitarGuardar = true;
		
		EmpleadoEdit="";
		
		inicio=null;
		inicio1=null;
		inicio2=null;
		inicio3=null;	
		
		fin=null;
		fin1=null;
		fin2=null;
		fin3=null;
		
		saldo=0;
		saldo1=0;
		saldo2=0;
		saldo3=0;
		
		dias=0;
		dias1=0;
		dias2=0;
		dias3=0;
		
		saldoPendiente = 0;
		saldoPendiente1 = 0;
		saldoPendiente2 = 0;
		saldoPendiente3 = 0;
		
		this.filaEdit = filaEdit; 
		
		log.info("...............................INGRESO A EDIT");
		log.info("...............................filaEdit:"+this.filaEdit);
		
		String empleadoString="";		
		
		for(ProgramaVacacion pv: listaProgramaVacacion){
			if(pv.getNumero().equals(this.filaEdit)){
												
				empleadoEditar=pv.getEmpleado();
				
				empleadoString=empleadoEditar.getApellido()+" "+empleadoEditar.getNombre();
				
				inicio=pv.getFechaInicio();
				inicio1=pv.getFechaInicio1();
				inicio2=pv.getFechaInicio2();
				inicio3=pv.getFechaInicio3();
				
				fin=pv.getFechaFin();
				fin1=pv.getFechaFin1();
				fin2=pv.getFechaFin2();
				fin3=pv.getFechaFin3();
				
				dias=diferenciaFechas(empleadoEditar,inicio,fin);
				dias1=diferenciaFechas(empleadoEditar,inicio1,fin1);
				dias2=diferenciaFechas(empleadoEditar,inicio2,fin2);
				dias3=diferenciaFechas(empleadoEditar,inicio3,fin3);
				
				mensajeError=InterpretarMensaje(dias,0);
				mensajeError1=InterpretarMensaje(dias1,1);
				mensajeError2=InterpretarMensaje(dias2,2);
				mensajeError3=InterpretarMensaje(dias3,3);
				
				mensajeErrorGeneral=InterpretarMensajeGeneral(FechaRepetidas(inicio,fin,inicio1,fin1,inicio2,fin2,inicio3,fin3));
				
				EstablecerPeriodoVacacion(empleadoEditar);
				
				saldo=diasVacacion(inicio,diasVacacionContrato,empleadoPeriodoVacacionEdit);
				if (saldo != 0){
					saldoPendiente = saldo - dias;}
				saldo1=diasVacacion(inicio1,diasVacacionContrato,empleadoPeriodoVacacionEdit);
				if(saldo1!=0){
					saldoPendiente1 = saldo1 - dias1;}	
				saldo2=diasVacacion(inicio2,diasVacacionContrato,empleadoPeriodoVacacionEdit);
				if(saldo2!=0){
					saldoPendiente2= saldo2 - dias2;}
				saldo3=diasVacacion(inicio3,diasVacacionContrato,empleadoPeriodoVacacionEdit);
				if(saldo3 != 0){
					saldoPendiente3 = saldo3 - dias3;}
				break;
			}
		}
		
		EmpleadoEdit=empleadoString;
	}
	
	private String InterpretarMensaje(int dias, int orden){
		String mensaje="";
		
		//log.info("......................dias:"+dias+"    ......orden:"+orden);
		
		if(dias==-1){
			mensaje="Debe ingresar completamente los rangos";	
		}else{
			if(dias==-2){
				mensaje="Fecha inicial es mayor que fecha final";	
			}else{
				if(dias==0 && orden ==0){
					mensaje="Debe ingresar un rango inicial";
				}
			}
		}
		
		return mensaje;
	}
	
	private String InterpretarMensajeGeneral(int respuesta){
		String mensaje="";
		
		if(respuesta!=0){
			mensaje="Los rangos de fechas no deben anteponerce";
		}
		
		return mensaje;
	}
	
	private void EstablecerPeriodoVacacion(Empleado empleado){	
		
		empleadoPeriodoVacacionEdit = null;	
		diasMaximoContrato = 0;
		periodoContrato = 0;
		
		for(EmpleadoPeriodoVacacion epv: empleadoPeriodoVacacion){			
			if(empleado.getEmplId().equals(epv.getEmpleado().getEmplId())){		
				this.empleadoPeriodoVacacionEdit=epv;
				this.fechaCargaEmpleado=epv.getFechaDesde();
				break;
			}
		}	
		
		
		for(DetalleGrupoContratado dg:detalleGrupoContratado){
			if(dg.getDgcoId().equals(empleado.getDetalleGrupoContratado().getDgcoId())){
				diasVacacionContrato=dg.getTotalHoras();
				setDiasMaximoContrato((int) dg.getMaximoHoras());
				periodoContrato = dg.getMesesPeriodo();
				break;
			}
		}
		
	}
	
	public Integer getFilaRemove() {
		return filaRemove;
	}

	public void setFilaRemove(Integer filaRemove) {
		
		EmpleadoRemove="";
		
		this.filaRemove = filaRemove;
		
		log.info("...............................INGRESO A SET_FILA_REMOVE");
		log.info("...............................filaRemove:"+this.filaRemove);
		
		String empleadoString="";
		
		for(ProgramaVacacion pv: listaProgramaVacacion){
			if(pv.getNumero().equals(this.filaRemove)){
				empleadoString=pv.getEmpleado().getApellido()+" "+pv.getEmpleado().getNombre();
	
 			    break;
			}
		}
		
		ordenarLista();
		
		EmpleadoRemove="¿Desea descartar de la lista a "+empleadoString+"?";
		
				
		//**********
	}
	
	//Descartar Empleado de la Lista
	public void DescartarEmpleado(){
		if(listaProgramaVacacion.size()>1){
			for(ProgramaVacacion pv: listaProgramaVacacion){
				if(pv.getNumero().equals(this.filaRemove)){
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
		
	
	public String getEmpleadoRemove() {
		return EmpleadoRemove;
	}

	public void setEmpleadoRemove(String empleadoRemove) {
		EmpleadoRemove = empleadoRemove;
	}

	public String getValueSelected() {
		return valueSelected;
	}

	public void setValueSelected(String valueSelected) {
		this.valueSelected = valueSelected;
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

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getInicio1() {
		return inicio1;
	}

	public void setInicio1(Date inicio1) {
		this.inicio1 = inicio1;
	}

	public Date getInicio2() {
		return inicio2;
	}

	public void setInicio2(Date inicio2) {
		this.inicio2 = inicio2;
	}

	public Date getInicio3() {
		return inicio3;
	}

	public void setInicio3(Date inicio3) {
		this.inicio3 = inicio3;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public Date getFin1() {
		return fin1;
	}

	public void setFin1(Date fin1) {
		this.fin1 = fin1;
	}

	public Date getFin2() {
		return fin2;
	}

	public void setFin2(Date fin2) {
		this.fin2 = fin2;
	}

	public Date getFin3() {
		return fin3;
	}

	public void setFin3(Date fin3) {
		this.fin3 = fin3;
	}

	public Integer getSaldo() {
		return saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	public Integer getSaldo1() {
		return saldo1;
	}

	public void setSaldo1(Integer saldo1) {
		this.saldo1 = saldo1;
	}

	public Integer getSaldo2() {
		return saldo2;
	}

	public void setSaldo2(Integer saldo2) {
		this.saldo2 = saldo2;
	}

	public Integer getSaldo3() {
		return saldo3;
	}

	public void setSaldo3(Integer saldo3) {
		this.saldo3 = saldo3;
	}

	public String getEmpleadoEdit() {
		return EmpleadoEdit;
	}

	public void setEmpleadoEdit(String empleadoEdit) {
		EmpleadoEdit = empleadoEdit;
	}

	public Integer getDias() {
		return dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public Integer getDias1() {
		return dias1;
	}

	public void setDias1(Integer dias1) {
		this.dias1 = dias1;
	}

	public Integer getDias3() {
		return dias3;
	}

	public void setDias3(Integer dias3) {
		this.dias3 = dias3;
	}

	public Integer getDias2() {
		return dias2;
	}

	public void setDias2(Integer dias2) {
		this.dias2 = dias2;
	}

	public Empleado getEmpleadoEditar() {
		return empleadoEditar;
	}

	public void setEmpleadoEditar(Empleado empleadoEditar) {
		this.empleadoEditar = empleadoEditar;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getMensajeError1() {
		return mensajeError1;
	}

	public void setMensajeError1(String mensajeError1) {
		this.mensajeError1 = mensajeError1;
	}

	public String getMensajeError2() {
		return mensajeError2;
	}

	public void setMensajeError2(String mensajeError2) {
		this.mensajeError2 = mensajeError2;
	}

	public String getMensajeError3() {
		return mensajeError3;
	}

	public void setMensajeError3(String mensajeError3) {
		this.mensajeError3 = mensajeError3;
	}

	public String getMensajeErrorGeneral() {
		return mensajeErrorGeneral;
	}

	public void setMensajeErrorGeneral(String mensajeErrorGeneral) {
		this.mensajeErrorGeneral = mensajeErrorGeneral;
	}
	
	public Integer getDiasTomados() {
		return diasTomados;
	}

	public void setDiasTomados(Integer diasTomados) {
		this.diasTomados = diasTomados;
	}

	public Boolean getHabilitarGuardar() {
		return habilitarGuardar;
	}

	public void setHabilitarGuardar(Boolean habilitarGuardar) {
		this.habilitarGuardar = habilitarGuardar;
	}

	public Integer getDiasMaximoContrato() {
		return diasMaximoContrato;
	}

	public void setDiasMaximoContrato(Integer diasMaximoContrato) {
		this.diasMaximoContrato = diasMaximoContrato;
	}

	public Integer getPeriodoContrato() {
		return periodoContrato;
	}

	public void setPeriodoContrato(Integer periodoContrato) {
		this.periodoContrato = periodoContrato;
	}
	
	public void setSaldoPendiente(Integer saldoPendiente) {
		this.saldoPendiente = saldoPendiente;
	}

	public Integer getSaldoPendiente1() {
		return saldoPendiente1;
	}

	public void setSaldoPendiente1(Integer saldoPendiente1) {
		this.saldoPendiente1 = saldoPendiente1;
	}

	public Integer getSaldoPendiente2() {
		return saldoPendiente2;
	}

	public void setSaldoPendiente2(Integer saldoPendiente2) {
		this.saldoPendiente2 = saldoPendiente2;
	}

	public Integer getSaldoPendiente3() {
		return saldoPendiente3;
	}

	public void setSaldoPendiente3(Integer saldoPendiente3) {
		this.saldoPendiente3 = saldoPendiente3;
	}
	public Integer getSaldoPendiente() {
		return saldoPendiente;
	}
}