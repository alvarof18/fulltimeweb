package com.casapazmino.fulltime.procesos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.CiudadFeriadoList;
import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.action.EmpleadoPeriodoVacacionList;
import com.casapazmino.fulltime.action.PermisoHome;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.Enumeraciones;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.comun.Enumeraciones.tipoDescuentoPermiso;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.TipoPermiso;

@Name("permisosMultiples")
@Scope(ScopeType.SESSION)
public class PermisosMultiplesBean implements PermisosMultiples {
	
	@In
	EntityManager entityManager;

	@Logger
	Log log;
	
	@In(create = true)
	PermisoHome permisoHome;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	//Atributos
	private String tipoGrupo;
	private TipoPermiso tipoPermiso;
	private Date fechaDesde;
	private Date fechaHasta;
	private String observacion;
	private String lugar;
	private int dias;
	private Double NumeroHoras;
	private Empleado jefe;
	private int acceso;
	private Boolean Error = false;

	Fechas fechas = new Fechas();
	
	//Lista
	private ArrayList<Long> cargos;
	private ArrayList<Long> detalleGrupoContratados;
	private ArrayList<Long> departamentos;
	private ArrayList<Long> ciudades;
	private List<Empleado> empleados;
	private List<Empleado> empleadosSinPeriodoVacacion = new ArrayList<Empleado>();
	private Map<Permiso, String> listaPermisos = new  LinkedHashMap<Permiso,String>();
	private Map<Permiso, String> procesados = new  HashMap<Permiso,String>();
	private Boolean dspProcesar;
	private String detalle;

	// Constructor
	public PermisosMultiplesBean() {
		   super();
		   tipoGrupo="Cargo";
		   dspProcesar = false;
		   acceso = 0;
	}
	
	@Override
	public void procesar() {
	   
		procesados.clear();
		for(Iterator<Permiso> it = listaPermisos.keySet().iterator(); it.hasNext();){
			Permiso clave = it.next();
		    Permiso tmpPer = new Permiso();
		    tmpPer = clave;
			if(listaPermisos.get(clave).equals("Ninguno")){	
				permisoHome.setInstance(tmpPer);
				permisoHome.setNivelesRequeridos(clave.getNivelRequerido());
				permisoHome.persist();
				procesados.put(tmpPer, "Procesado");
			}else{
				procesados.put(clave, listaPermisos.get(clave));
			}
		}
		
		listaPermisos.clear();
		listaPermisos.putAll(procesados);
		FacesMessages.instance().clear();
		FacesMessages.instance().add("Permisos Creados");
		dspProcesar = false;
	}
	
	public void cancelar(){
		
		 tipoGrupo="Cargo";
		 dspProcesar = false;
		 listaPermisos.clear();
		 tipoPermiso = null;
		 fechaDesde = null;
		 fechaHasta = null;
	}
	
	public void verificar(){
		listaPermisos.clear();
		this.buscarEmpleados();
		
		log.info("SDFDSFSDFSDF " + empleados);
		this.validarEmpleados(empleados);
		
		if (this.getEmpleadosSinPeriodoVacacion().size() != 0){
			FacesMessages.instance().add(
					"El empleado: " + this.getEmpleadosSinPeriodoVacacion().get(0).getApellido() + " " + 
							this.getEmpleadosSinPeriodoVacacion().get(0).getNombre() + " no tiene asignado vacaciones");
		}else {
			this.diferenciaFechas();
			this.validarPermiso(empleados);
			
			log.info("========DIAS====== " + dias + " HORAS " + NumeroHoras );
			
			if(listaPermisos.isEmpty() || this.getError()){
				dspProcesar = false;
				this.setError(false);
			}else{ //Verifico que existan al menos un registro que se pueda crear
				
				for(Iterator<Permiso> it = listaPermisos.keySet().iterator(); it.hasNext();){
					Permiso clave = it.next();
					
					if(listaPermisos.get(clave).equals("Ninguno")){
						dspProcesar = true;
						break;
					}
					
				}
			}	
		}
	}
	
	private void validarPermiso(List<Empleado> empleados) {
		
		int nivelRequerido = 0;
		String mensaje;
		long indice = 1;
		int i=0;
		PermisoHome permisoHome2 = (PermisoHome) Component.getInstance("permisoHome", true);
		
		for(Empleado empleado: empleados){
		log.info("XAMP " + i);
		
		i++;
			Permiso permiso = new Permiso();
			permiso.setTipoPermiso(tipoPermiso);
			permiso.setEmpleadoByEmplId(empleado);
			
			if(empleado.getEmpleado() == null){
				permiso.setEmpleadoByEmpEmplId(empleado);
			}else{
				permiso.setEmpleadoByEmpEmplId(empleado.getEmpleado());
			}
			permiso.setDetalleTipoParametro(this.cambiarEstadoAutorizacion(Constantes.DECISION_NO));
			permiso.setEmpleadoPeriodoVacacion(this.asignarEmpleadoPeriodoVacacion(empleado));
			permiso.setFecha(new Date());
			permiso.setObservacion(this.getObservacion());
			permiso.setTimbra(true);
			permiso.setTrabajadas(false);
			permiso.setFechaDesde(this.getFechaDesde());
			permiso.setFechaHasta(this.getFechaHasta());
			permiso.setNumeroHoras(Math.abs(this.NumeroHoras));
			permiso.setAprobacion(1);
			permiso.setPermId(indice);
					
			try {
				Integer nivelCiudad = empleado.getCiudad().getNivel();
				Integer nivelTipoPermiso = tipoPermiso.getNivel();

				if (nivelCiudad > 0)
					nivelRequerido = nivelCiudad;
				else
					nivelRequerido = nivelTipoPermiso;
			} catch (Exception ex) {
					nivelRequerido = tipoPermiso.getNivel();
			}
			permiso.setNivelRequerido(nivelRequerido);
			permiso.setDias(dias);
			permiso.setNumeroHoras(NumeroHoras);
			permiso.setEstadoActual("0");
			
			permisoHome.setInstance(permiso);
			permisoHome.getInstance().setNivelRequerido(nivelRequerido);
			
			//Controlar dias laborables y libres
			try{
			
				if(permiso.getDias()>0) {
					log.info("Entre2 " + permiso.getDias());
					permiso = this.ValidacionAdicionalLibreFeriados(permiso);
					//permiso.setEmpleadoByEmplId(empleado);
					
					}
		 	}catch(Exception ex){
				InvalidValue iv = new InvalidValue(
						"Hubo un problema en la verificacion de dias laborables!",
						null, null, null, null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
				break;
			}
			
			//Controlar niveles por detalle de tipo de permiso
			try{
				try {
					Integer nivelCiudad = permiso.getEmpleadoByEmplId()
							.getCiudad().getNivel();					

					if (nivelCiudad > 0){
						permisoHome.setNivelesRequeridos(nivelCiudad);
					}
					else{
						permisoHome.nivelesAprobacionDetalleTipoPermiso();
					}						
				} catch (Exception ex) {
					log.info("............................................excepcion en tipo permiso por ciudad");
					permisoHome.setNivelesRequeridos(0);
				}
			}catch(Exception ex){
				InvalidValue iv = new InvalidValue(
						"Hubo un problema en la verificacion de niveles por tipo de permiso!",
						null, null, null, null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
				break;
			}
			
			mensaje = permisoHome.validarPermiso();	
			
			if(permisoHome.getInstance().getObservacion().equals("Error - Verifique Periodo Vacaciones o Tipo de permiso")){
				InvalidValue iv = new InvalidValue(
						"A ocurrido un error con el empleado " + permiso.getEmpleadoByEmplId().getApellido() + " " + permiso.getEmpleadoByEmplId().getNombre() + 
							" Verifique Periodo Vacaciones o Tipo de permiso " ,
						null, null, null, null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
				this.setError(true);	
				permisoHome.getInstance().setObservacion("");
				break;
				
			}
			
			log.info("holamundo " + permisoHome.getInstance().getObservacion());
			
			Hibernate.initialize(permiso.getEmpleadoByEmplId());
			Hibernate.initialize(permiso.getEmpleadoByEmpEmplId());
			Hibernate.initialize(permiso.getEmpleadoByEmplId().getCargo());
			Hibernate.initialize(permiso.getEmpleadoByEmplId().getDepartamento());
			Hibernate.initialize(permiso.getEmpleadoByEmpEmplId().getDepartamento());
			Hibernate.initialize(permiso.getEmpleadoByEmplId().getDepartamento().getDepartamento());
			Hibernate.initialize(permiso.getDetalleTipoParametro());
			Hibernate.initialize(permiso.getDetalleTipoParametro().getDescripcion());
			
			
			// permisoHome.setInstance(permiso);
			// permisoHome.getInstance().setNivelRequerido(nivelRequerido);
		/*
			
			/*
					*/

			
		    log.info("diassss " + permiso.getDiasLibres());
			listaPermisos.put(permiso, mensaje);
			mensaje ="";
			//log.info("++++++++++++++++++++++++ Mensaje ++++++++++ " + mensaje);
			indice++;
		}
		indice = 0;
	}
	
	public Permiso ValidacionAdicionalLibreFeriados(Permiso permiso){
		
		
		log.info("------------------------------------ENTRO A INFORMACION ADICIONAL");
		
		String mensaje="Ninguno";
		
		Empleado empleado= permiso.getEmpleadoByEmplId();
		int countdiasLaborables=0;
		int countdiasLibres=0;
		int countdiasFeriados=0;
		int countdiasRecuperables=0;
		
		int totaldiasLaborables=0;
		int totaldiasLibres=0;
		
		DetalleGrupoContratado detalleGrupo=empleado.getDetalleGrupoContratado();
				
		if(detalleGrupo.getActivarRegla()){
			
			tipoDescuentoPermiso enumeracionTipoDescuentoPermiso = Enumeraciones.tipoDescuentoPermiso.valueOf("Vacaciones");
				
			if( (permiso.getTipoPermiso().getDescuento() == enumeracionTipoDescuentoPermiso.Vacaciones && detalleGrupo.getPermisoCargo()) ){

			
				
				ArrayList<Long> listaCiudades=new ArrayList<Long>();
				listaCiudades.add(empleado.getCiudad().getCiudId());
				
				Date inicio=permiso.getFechaDesde();
				Date fin=permiso.getFechaHasta();
				
				while(inicio.compareTo(fin)==0 || inicio.before(fin)){
					Calendar index=Calendar.getInstance();
					index.setTime(inicio);
						
					int diaSemana=index.get(Calendar.DAY_OF_WEEK);
						
					if(diaSemana>=2 && diaSemana<=6){
						countdiasLaborables++;	
					}else{
						countdiasLibres++;
					}
						
					index.add(Calendar.DATE, 1);					
					inicio=index.getTime();
				}
				
				
				//Verificar Feriados
				if(detalleGrupo.getActivarFeriados()){
					List<CiudadFeriado> ciudades=RetornarCiudadFeriado(permiso.getFechaDesde(),permiso.getFechaHasta(),listaCiudades);					
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
				if(detalleGrupo.getActivarRecuperable()){
					List<CiudadFeriado> ciudades=RetornarCiudadRecuperable(permiso.getFechaDesde(),permiso.getFechaHasta(),listaCiudades);
					
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
				
				
					
			}	
			
			
		}
		
		totaldiasLaborables=countdiasLaborables+countdiasRecuperables-countdiasFeriados;
		totaldiasLibres=countdiasLibres+countdiasFeriados-countdiasRecuperables;
		
		permiso.setDias(totaldiasLaborables);
		permiso.setDiasLibres(totaldiasLibres);
		
		return permiso;
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

	public List<Empleado> validarEmpleados(List<Empleado> empleados){
		empleadosSinPeriodoVacacion.clear();
		for (Empleado empleado : empleados) {
			if (empleado.getEmpleadoPeriodoVacacions().size() == 0){
				this.getEmpleadosSinPeriodoVacacion().add(empleado);
			}  
		}
		return empleados;	
	}	
	
	public void asignarAcceso(){
		
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		acceso = gestionPermisoVacacion.buscarAccesoEmpleados(empleado);
		log.info("hola" + acceso);
		if(acceso == Constantes.ACCESO_SUBORDINADOS){
			this.setJefe(empleado);
			detalle = "empleados";
		}else if (acceso == Constantes.ACCESO_TODOS){
			
		}else if(acceso == Constantes.ACCESO_INDIVIDUAL){
			this.setJefe(empleado);
			detalle = "empleados";
		}
				
	}

	private void buscarEmpleados() {
		try {
			
			if (acceso == Constantes.ACCESO_SUBORDINADOS){
				getEmpleados().clear();
				getEmpleados().addAll(EmpleadosSub());
				//Añade al usuario a la lista 
				getEmpleados().add(gestionPermisoVacacion.buscarEmpleado());			
			}else{
				 if (tipoGrupo.equals("Cargo")){
					
					getEmpleados().clear();
					getEmpleados().addAll(EmpleadosCargo());	
					
				} else if (tipoGrupo.equals("Departamento")){
					
					getEmpleados().clear();
					getEmpleados().addAll (EmpleadosDepartamento());
					
				} else if (tipoGrupo.equals("Grupo")) {
					
					getEmpleados().clear();
					getEmpleados().addAll (EmpleadosGrupo());
				} 
		 }
		}catch (Exception e) {
			log.info(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	private List<Empleado> EmpleadosSub() {
		
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.detalleTipoParametroByEstado.dtpaId = (:estado) and "
						+ " e.empleado.emplId = :jefe  order by e.apellido")
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.setParameter("jefe", getJefe().getEmplId())
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Empleado> EmpleadosCargo() {
		
		log.info("cargo " + getCargos());
		log.info("ciudades " + getCiudades());
		
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.cargo.cargId in (:cargos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) order by e.apellido")
				.setParameter("cargos", getCargos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Empleado> EmpleadosDepartamento() {
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.departamento.depaId in (:departamentos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) order by e.apellido")
				.setParameter("departamentos", getDepartamentos())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}	
	
	@SuppressWarnings("unchecked")
	private List<Empleado> EmpleadosGrupo() {
				
		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.detalleGrupoContratado.dgcoId in (:grupos) and e.ciudad.ciudId in (:ciudades) and e.detalleTipoParametroByEstado.dtpaId = (:estado) order by e.apellido")
				.setParameter("grupos", getDetalleGrupoContratados())
				.setParameter("ciudades", getCiudades())
				.setParameter("estado", Constantes.ESTADO_ACTIVO)
				.getResultList();
	}
	
	public DetalleTipoParametro cambiarEstadoAutorizacion(Long decision){
		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component.getInstance("detalleTipoParametroHome", true); 
		detalleTipoParametroHome.setId(decision);
		DetalleTipoParametro  detalleTipoParametro = detalleTipoParametroHome.find();
		return detalleTipoParametro;
	}
	
	public EmpleadoPeriodoVacacion asignarEmpleadoPeriodoVacacion (Empleado empleado){
		EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
		for (EmpleadoPeriodoVacacion empleadoPeriodoVacacion2 : empleado.getEmpleadoPeriodoVacacions()) {
			empleadoPeriodoVacacion = empleadoPeriodoVacacion2;
		}
		
		return empleadoPeriodoVacacion;
	}
	
@SuppressWarnings("static-access")
public void diferenciaFechas() {
		
	
	log.info("====================Desde: " + this.getFechaDesde() + "============== hasta: " + this.getFechaHasta());
	
		if (this.getFechaDesde() != null
				&& this.getFechaHasta() != null) {
			
			int dias = (int) fechas.numeroDias(this.getFechaDesde(),this.getFechaHasta());

			Double horas = fechas.numeroHoras(this.getFechaDesde(), this.getFechaHasta());

			if (dias > 1) {
				horas = (double) 0;
			}

			if (dias == 1 && horas != 0) {
				dias = 0;				
			}
			
			this.setDias(dias);
			this.setNumeroHoras(horas);
		}
		
		
	}

	
	// GET- SET
	
	public ArrayList<Long> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(ArrayList<Long> departamentos) {
		this.departamentos = departamentos;
	}

	public ArrayList<Long> getCiudades() {
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}

	public List<Empleado> getEmpleados() {
		if(empleados==null)
			empleados=new ArrayList<Empleado>();
		return empleados;
	}

	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}
	
	public String getTipoGrupo() {
		return tipoGrupo;
	}


	public void setTipoGrupo(String tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
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

	public TipoPermiso getTipoPermiso() {
		return tipoPermiso;
	}

	public void setTipoPermiso(TipoPermiso tipoPermiso) {
		this.tipoPermiso = tipoPermiso;
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
		

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public List<Empleado> getEmpleadosSinPeriodoVacacion() {
		return empleadosSinPeriodoVacacion;
	}

	public void setEmpleadosSinPeriodoVacacion(List<Empleado> empleadosSinPeriodoVacacion) {
		this.empleadosSinPeriodoVacacion = empleadosSinPeriodoVacacion;
	}
	
	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

	public double getNumeroHoras() {
		return NumeroHoras;
	}

	public void setNumeroHoras(double numeroHoras) {
		NumeroHoras = numeroHoras;
	}

	public Map<Permiso, String> getListaPermisos() {
		return listaPermisos;
	}

	public void setListaPermisos(Map<Permiso, String> listaPermisos) {
		this.listaPermisos = listaPermisos;
	}
	
	public Boolean getDspProcesar() {
		return dspProcesar;
	}

	public void setDspProcesar(Boolean dspProcesar) {
		this.dspProcesar = dspProcesar;
	}

	public Map<Permiso, String> getProcesados() {
		return procesados;
	}

	public void setProcesados(Map<Permiso, String> procesados) {
		this.procesados = procesados;
	}

	public Empleado getJefe() {
		return jefe;
	}

	public void setJefe(Empleado jefe) {
		this.jefe = jefe;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public Boolean getError() {
		return Error;
	}

	public void setError(Boolean error) {
		Error = error;
	}

}
