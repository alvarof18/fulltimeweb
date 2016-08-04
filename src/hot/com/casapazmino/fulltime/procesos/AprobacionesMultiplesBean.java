package com.casapazmino.fulltime.procesos;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
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

import com.casapazmino.fulltime.action.DepartamentoAutorizacionList;
import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.action.PermisoHome;
import com.casapazmino.fulltime.action.PermisoList;
import com.casapazmino.fulltime.action.SolicitudVacacionHome;
import com.casapazmino.fulltime.action.SolicitudVacacionList;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.DepartamentoAutorizacion;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.seguridad.action.UsuariosList;

@Name("aprobacionMultiple")
@Scope(ScopeType.SESSION)
public class AprobacionesMultiplesBean implements AprobacionesMultiples, Serializable  {
	
	private static final long serialVersionUID = -907511933344450909L;

	@In
	EntityManager entityManager;

	@Logger
	Log log;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	@In(create = true)
	DepartamentoAutorizacionList departamentoAutorizacionList;
	
	@In(create = true)
	PermisoList permisoList;
	
	@In(create = true)
	SolicitudVacacionHome solicitudVacacionHome;
	
	@In(create = true)
	DetalleTipoParametroList detalleTipoParametroList;
	
	//@In(create = true)
	//PermisoHome permisoHome;
		
	//Atributos
		private String tipoGrupo;
		private String tipoSolicitud;
		private String autorizacion;
		private String autorizacionSeleccionada;
		private Boolean acciones;
		private String opcion;
		private String autorizar;
		private Integer procesados;
		private Empleado empleado;
		private Boolean botonProcesar;
		private Date fechaDesde;

		//Listas
		private List<Empleado> empleados;
		private ArrayList<Long> cargos;
		private ArrayList<Long> detalleGrupoContratados;
		private ArrayList<Long> departamentos;
		private ArrayList<Long> ciudades;
		private List<Permiso> listaPermiso;
		private Map<Long, String> opciones = new  LinkedHashMap<Long,String>();
		private List<SolicitudVacacion> listaSolicitudVacacion;

		//Metodos
		// Constructor
			public AprobacionesMultiplesBean() {
				super();
				tipoGrupo="Cargo";
				tipoSolicitud = "0";
				autorizacion = "0";
				autorizar = "0";
				procesados = 0;
				acciones = true;
				botonProcesar = false;
				fechaDesde = new Date();
			}
			
	@Override	
	public String procesar() {
		
		PermisoHome permisoHome = (PermisoHome) Component.getInstance("permisoHome",true);
		String mensaje;
			if(this.getTipoSolicitud().equals("0")) { // Permisos
		
		//Recorro la lista de Permisos y las acciones a realizar
		
			for(Permiso p:listaPermiso){
				mensaje = "";
				String accion = opciones.get(p.getPermId());
				if(accion.equals("0")){ //Autorizar
					
					mensaje = this.Estado(p);
					
					log.info("ESTADO++++++++++++++++++++++ " + mensaje);
				
					if(mensaje.equals("pre-autoriza")){
						
						permisoHome.setInstance(p);
						mensaje = permisoHome.PreAutorizarPermiso();
						if (mensaje == null){
							mensaje = "";							
						}
						log.info("RESULTADOOOOOOOOOOOOO " + mensaje);
							if(mensaje.equals("updated")){
								this.entityManager.merge(p);
								this.desactivarFunciones();
								FacesMessages.instance().clear();
								FacesMessages.instance().add("Solicitudes Pre-Autorizadas");
					
							}else{
								
								//FacesMessages.instance().clear();
								//FacesMessages.instance().add("Error al momento de Pre-Autorizar");
								break;
							}
						
					}else if(mensaje.equals("autoriza")){
						permisoHome.setInstance(p);
						mensaje = permisoHome.AutorizarPermisos(p);
						if (mensaje == null){
							mensaje = "";							
						}	
						log.info("RESULTADOOOOOOOOOOOOO " + mensaje);
							if(mensaje.equals("updated")){
								log.info("aprobacion" + p);
								this.entityManager.merge(p);
								this.desactivarFunciones();
								FacesMessages.instance().clear();
								FacesMessages.instance().add("Solicitudes Autorizadas");							
							}else{
								
								//FacesMessages.instance().clear();
								//FacesMessages.instance().add("Error al momento de Autorizar");
								break;
							}
						
					} else{
						
						InvalidValue iv = new InvalidValue(
								"No es posible autorizar los permisos seleccionados",
								null, null, null, null);
						FacesMessages.instance().clear();
						FacesMessages.instance().add(iv);
						break;
					}
				
				
				
				}else{ //No-Autoriza
					
					permisoHome.setInstance(p);
					mensaje = permisoHome.NoAutorizarPermiso();
					if (mensaje == null){
						mensaje = "";							
					}
					log.info("RESULTADOOOOOOOOOOOOO " + mensaje);
						if(mensaje.equals("updated")){
							this.desactivarFunciones();
							this.entityManager.merge(p);
							FacesMessages.instance().clear();
							FacesMessages.instance().add("Solicitudes No-Autorizadas");	
						}
				}
			}
			
		}else { //Solicitud de Vacaciones
			//Recorro la lista de Vacaciones y las acciones a realizar
			for(SolicitudVacacion sv :listaSolicitudVacacion ){
				String accion = opciones.get(sv.getSova());
				
				if(accion.equals("0")){ //Autorizar
					mensaje = this.EstadoVacacion(sv);
					log.info("ESTADOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO " + mensaje);
						if(mensaje.equals("autorizar")){
							solicitudVacacionHome.setInstance(sv);
							mensaje = solicitudVacacionHome.AutorizarSolicitudes(sv);
							if (mensaje == null){
								mensaje = "";							
							}
							if(mensaje.equals("updated")){
								this.entityManager.merge(sv);
								this.desactivarFunciones();
								FacesMessages.instance().clear();
								FacesMessages.instance().add("Solicitudes Autorizadas");	
							}else{break;}
						}else if(mensaje.equals("pre-autorizar")){
								solicitudVacacionHome.setInstance(sv);
								solicitudVacacionHome.EnvioCorreo();
								
								mensaje = solicitudVacacionHome.PreAutorizarSolicitud();
								if (mensaje == null){
									mensaje = "";							
								}
								if(mensaje.equals("updated")){
									this.entityManager.merge(sv);
									this.desactivarFunciones();
									FacesMessages.instance().clear();
									FacesMessages.instance().add("Solicitudes Pre-Autorizadas");
								}else{break;}	
						}
						}else{
							solicitudVacacionHome.setInstance(sv);
							solicitudVacacionHome.EnvioCorreo();
							mensaje= solicitudVacacionHome.NoAutorizarSolicitud();
							if (mensaje == null){
								mensaje = "";							
							}
							if(mensaje.equals("updated")){
								this.entityManager.merge(sv);
								this.desactivarFunciones();
								FacesMessages.instance().clear();
								FacesMessages.instance().add("Solicitudes No-Autorizadas");	
							}
							
					}
			}	
		}
				
		return "ok";
	}
	
	
	public void desactivarFunciones(){
		this.acciones = false;
		this.botonProcesar = false;
	}
	
	public void Cancelar(){
		
		this.desactivarFunciones();
		tipoGrupo="Cargo";
		tipoSolicitud = "0";
		autorizacion = "0";
		autorizar = "0";
		procesados = 0;
		listaPermiso.clear();
	}
	
		public void buscarEmpleados() {
				try {
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
				}catch (Exception e) {
					log.info(e);
				}
			}
	
	public void SeleccionaOpcion(ValueChangeEvent evento){
		this.setAutorizar(evento.getNewValue().toString());
	}
	
	public void grabarOpcion(Long clave)
	{
		this.opciones.put(clave, this.getAutorizar());
		
		//------------------Mostrar la lista ----------------------
		
		log.info("-------------------------------Nueva Lista------------------------------------------ ");
		
		for(Iterator<Long> it = opciones.keySet().iterator(); it.hasNext();){
			Long claves = it.next();
			log.info("------------Clave--------------- " + claves + "   "+ " +++++++++++++++Valor++++++++++ " + opciones.get(claves));
		}
		this.setAutorizar("");
	}
	
	public void VerificarSolicitudes(){
		this.opciones.clear();
		this.buscarEmpleados();
		
		if (this.getTipoSolicitud().equals("0")) //Permisos
		{
			listaPermiso = buscarPermisos();
			
			log.info("lista Permiso " + listaPermiso);
			//preparo la lista
			String mensaje = "";
			List<Permiso> listaTmp = new ArrayList<Permiso>();
			
			for(Permiso p : listaPermiso){
				mensaje = this.Estado(p);
				if(mensaje.equals("") && !this.getAutorizacion().equals("3") && !this.getAutorizacion().equals("2")){
					listaTmp.add(p);					
				}
				
			}
			
			listaPermiso.removeAll(listaTmp);
			
			if(this.getAutorizacion().equals("0") || this.getAutorizacion().equals("1")){
				this.botonProcesar = true;
				this.acciones = true;
						
			}else{
				this.desactivarFunciones();
			}
			// Cargo lista de opciones
			
			for(Permiso permiso:listaPermiso){
				opciones.put(permiso.getPermId(), "0");
			}
			
			if(listaPermiso.size() == 0){
				this.botonProcesar = false;
			}
			
			// mostrar la opciones
			
			for(Iterator<Long> it = opciones.keySet().iterator(); it.hasNext();){
				Long clave = it.next();
				log.info("------------Clave--------------- " + clave + "   "+ " +++++++++++++++Valor++++++++++ " + opciones.get(clave));
			}
		}else{ // Solicitudes de Vacaciones
			
			listaSolicitudVacacion = buscarVacaciones();
			
			//preparo la lista
			String mensaje = "";
			List<SolicitudVacacion> listaTmp = new ArrayList<SolicitudVacacion>();
			
			log.info("Lista de Vacacionessssssssssssss " + this.getListaSolicitudVacacion());
			
			for(SolicitudVacacion sv : listaSolicitudVacacion){
				mensaje = this.EstadoVacacion(sv);
				if(mensaje.equals("") && !this.getAutorizacion().equals("3") && !this.getAutorizacion().equals("2")){
					listaTmp.add(sv);					
				}
				
			}
			
			listaSolicitudVacacion.removeAll(listaTmp);
			
			if(this.getAutorizacion().equals("0") || this.getAutorizacion().equals("1")){
				this.botonProcesar = true;
				this.acciones = true;			
			}else{
				this.desactivarFunciones();
			}
			//cargar Opciones
			for(SolicitudVacacion solicitudVacacion:listaSolicitudVacacion){
				opciones.put(solicitudVacacion.getSova(), "0");
			}
			
			for(Iterator<Long> it = opciones.keySet().iterator(); it.hasNext();){
				Long clave = it.next();
				log.info("------------Clave--------------- " + clave + "   "+ " +++++++++++++++Valor++++++++++ " + opciones.get(clave));
			}
			
			if(listaSolicitudVacacion.size() == 0){
				this.botonProcesar = false;
			}
		
		} 
	}
	
	public List<SolicitudVacacion> buscarVacaciones(){
		
		log.info("Estado de Solicitudddddddddddddd " + this.autorizacion);
		List <SolicitudVacacion> lista=new ArrayList<SolicitudVacacion>();
		List <SolicitudVacacion> result=new ArrayList<SolicitudVacacion>();
		Boolean Acceso;
		Acceso = this.verificarUsuario();
		
		if(Acceso){
		
			SolicitudVacacionList modelo = (SolicitudVacacionList) Component.getInstance("solicitudVacacionList", true);
		
		
			for(Empleado e :empleados){
			
				modelo.setAutorizado(null);
				modelo.setEstado(this.autorizacion);
				modelo.getSolicitudVacacion().setEmpleadoByEmplId(e);
				modelo.getSolicitudVacacion().setFechaDesde(fechaDesde);
				result=modelo.getListaSolicitudVacacionNiveles();
			
			
				for(SolicitudVacacion v: result){
					Hibernate.initialize(v.getEmpleadoByEmplId());
					Hibernate.initialize(v.getEmpleadoByEmpEmplId());
					Hibernate.initialize(v.getEmpleadoByEmplId().getCargo());
					Hibernate.initialize(v.getEmpleadoByEmplId().getDepartamento());
					Hibernate.initialize(v.getEmpleadoByEmpEmplId().getDepartamento());
					Hibernate.initialize(v.getEmpleadoByEmplId().getDepartamento().getDepartamento());
					Hibernate.initialize(v.getEmpleadoPeriodoVacacion());		
					Hibernate.initialize(v.getDetalleTipoParametro());
					Hibernate.initialize(v.getDetalleTipoParametro().getDescripcion());
					Hibernate.initialize(v.getEmpleadoByEmplId().getDetalleGrupoContratado());
					Hibernate.initialize(v.getHorario().getDetalleHorarios());
					lista.add(v);
				
				}
			}
		}
		
		return lista;
		
	}
	
	public List<Permiso> buscarPermisos(){
		
		List <Permiso> lista = new ArrayList<Permiso>();
		List <Permiso> result=new ArrayList<Permiso>();
		Boolean Acceso;
		
		Acceso = this.verificarUsuario();
		log.info("Acceso " + Acceso);
		if(Acceso){
		
			PermisoList modelo = (PermisoList) Component.getInstance("permisoList", true);
		
			for(Empleado e: empleados){
				
				permisoList.setAutorizado(null);
				permisoList.getPermiso().setEmpleadoByEmplId(e);
				permisoList.setEstado(this.autorizacion);	
				permisoList.getPermiso().setFechaDesde(fechaDesde);
							
			result = modelo.getListaPermisoNiveles();				
			for(Permiso p: result){
				Hibernate.initialize(p.getEmpleadoByEmplId());
				Hibernate.initialize(p.getEmpleadoByEmplId().getCargo());
				Hibernate.initialize(p.getEmpleadoByEmplId().getDepartamento());
				Hibernate.initialize(p.getEmpleadoByEmplId().getDepartamento().getDepartamento());
				Hibernate.initialize(p.getEmpleadoByEmpEmplId());
				Hibernate.initialize(p.getEmpleadoByEmpEmplId().getDepartamento());
				Hibernate.initialize(p.getEmpleadoByEmplId().getHistLabos());
				Hibernate.initialize(p.getTipoPermiso());		
				Hibernate.initialize(p.getDetalleTipoParametro());
				Hibernate.initialize(p.getEmpleadoPeriodoVacacion());
				Hibernate.initialize(p.getEmpleadoByEmplId().getDetalleGrupoContratado());
				log.info("NIVELES REQUERIDO " + p.getNivelRequerido());
				lista.add(p);
			}
			}
		}
		return lista;		
	}
	
	public String Estado(Permiso permiso){
		String mensaje = "";
		
		this.empleado = gestionPermisoVacacion.buscarEmpleado();
		
		Integer nivelCiudad = permiso.getEmpleadoByEmplId()
				.getCiudad().getNivel();
		Integer nivelTipoPermiso = permiso.getTipoPermiso()
				.getNivel();
	
		departamentoAutorizacionList.getDepartamentoAutorizacion().setEmpleado(this.empleado);
		List<DepartamentoAutorizacion> departamentoAutorizacion = new ArrayList<DepartamentoAutorizacion>();
		departamentoAutorizacion = departamentoAutorizacionList.getResultList();
			
		log.info("departamento Autorizacion " + departamentoAutorizacion.size());

		if (departamentoAutorizacion.size() > 0) {
			
			Integer nivelDepartamento = empleado.getDepartamento()
					.getNivel();
			Integer nivelPermiso = permiso.getNivel();
			Integer nivelAprobacion = permiso.getAprobacion();
			
			String est = permiso.getEstadoActual().trim();
			
			log.info("================================================== est : "
					+ est);
			log.info("================================================== this.getInstance().getEstadoActual() : "
					+ permiso.getEstadoActual());
			
			if (nivelCiudad > 0)
				nivelTipoPermiso = nivelCiudad;
			log.info("nivelDepartamento " + nivelDepartamento +" nivel Permiso " + nivelPermiso + "  nivelAprobacion " 
					  + nivelAprobacion + " nivelCiudad " +  nivelCiudad);
			
			if (est.equals("0") || est.equals("1")) {

				Departamento departamentoPermisoEmpleado = RetornoDepartamento(
						permiso.getEmpleadoByEmplId()
								.getDepartamento(), nivelPermiso);
				if ((departamentoPermisoEmpleado == null))
					log.info("================================================== departamentoPermisoEmpleado null");
				else
					log.info("================================================== departamentoPermisoEmpleado not null");
				if (departamentoPermisoEmpleado != null) {
					log.info("================================================== departamentoPermisoEmpleado: "
							+ departamentoPermisoEmpleado.getDepaId());
					log.info("================================================== nivelPermiso: "
							+ nivelPermiso);
					log.info("================================================== "); 
					
					for (DepartamentoAutorizacion da : departamentoAutorizacion) {
						Departamento departamento = da.getDepartamento();
						log.info("................................ departamento.getDepaId(): "
								+ departamento.getDepaId());
						log.info("................................ departamento.getNivel(): "
								+ departamento.getNivel());

						if (departamento.getDepaId().equals(
								departamentoPermisoEmpleado.getDepaId())
								&& (departamento.getNivel()
										.equals(nivelPermiso))) {
							nivelDepartamento = nivelPermiso;
							break;
						}
					}
					log.info("================================================== ");
				}
								
				Integer auxNivelAprobacion = nivelAprobacion + 1;
				
				if (est.equals("0") && nivelDepartamento > 1
						&& nivelTipoPermiso > 1) {
					Integer nivelRequerido = permiso
							.getNivelRequerido();
					if (nivelPermiso > nivelRequerido)
						auxNivelAprobacion = nivelTipoPermiso;
					else
						auxNivelAprobacion = nivelDepartamento;
				}
				
				Departamento depSup = null;
				//log.info("LAZYYYYYYYYYYYYYYYYYYYYYYYYYY " + departamentoPermisoEmpleado);
				if (departamentoPermisoEmpleado != null) {
					depSup = departamentoPermisoEmpleado.getDepartamento();
				}
				

				log.info("================================================== nivelDepartamento:"
						+ nivelDepartamento);
				log.info("================================================== nivelPermiso:"
						+ nivelPermiso);
				log.info("================================================== nivelAprobacion:"
						+ nivelAprobacion);
				log.info("================================================== auxNivelAprobacion:"
						+ auxNivelAprobacion);
				log.info("================================================== nivelTipoPermiso:"
						+ nivelTipoPermiso);
				
				if ((depSup == null))
					log.info("================================================== depSup null");
				else
					log.info("================================================== depSup not null");
			
				if (((auxNivelAprobacion.equals(nivelTipoPermiso)) || (depSup == null))
						&& (nivelPermiso.equals(nivelDepartamento))) {
					mensaje = "autoriza";
						log.info("AUTORIZAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAR");
				} else {
					if ((auxNivelAprobacion.equals(nivelPermiso))
							&& (depSup != null)
							&& (nivelPermiso.equals(nivelDepartamento))) {
						mensaje = "pre-autoriza";
						log.info("PRE-AUTORIZAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAR");
					} 
				}	
				
			}
		}
		
		return mensaje;
	}
	
		
	public String EstadoVacacion(SolicitudVacacion sv){
		String mensaje = "";

		Integer nivelCiudad = sv.getEmpleadoByEmplId()
							.getCiudad().getNivel();
		
		DetalleTipoParametro detalleTipoParametros = detalleTipoParametroList
				.getNivelAprobacionSolicitudesVacaciones();
		Integer nivelContrato = Integer.parseInt(detalleTipoParametros
				.getDescripcion());
		
		this.empleado = gestionPermisoVacacion.buscarEmpleado();
		
		departamentoAutorizacionList.getDepartamentoAutorizacion()
				.setEmpleado(this.empleado);
		List<DepartamentoAutorizacion> departamentoAutorizacion = new ArrayList<DepartamentoAutorizacion>();
		departamentoAutorizacion = departamentoAutorizacionList
				.getResultList();

		if (departamentoAutorizacion.size() > 0) {

			Integer nivelDepartamento = empleado.getDepartamento()
					.getNivel();
			
			Integer nivelPermiso = sv.getNivel();
			Integer nivelAprobacion = sv.getAprobacion();

			String est = sv.getEstado().trim();
		
			
			if (nivelCiudad > 0)
				nivelContrato = nivelCiudad;

			if (est.equals("0") || est.equals("1")) {

				// ********************************************recorrer
				// listado
				Departamento departamentoPermisoEmpleado = RetornoDepartamento(
						sv.getEmpleadoByEmplId()
								.getDepartamento(), nivelPermiso);
				if ((departamentoPermisoEmpleado == null))
					log.info("================================================== departamentoPermisoEmpleado null");
				else
					log.info("================================================== departamentoPermisoEmpleado not null");
				if (departamentoPermisoEmpleado != null) {
					log.info("================================================== departamentoPermisoEmpleado: "
							+ departamentoPermisoEmpleado.getDepaId());
					log.info("================================================== nivelPermiso: "
							+ nivelPermiso);
					log.info("================================================== ");
					for (DepartamentoAutorizacion da : departamentoAutorizacion) {
						Departamento departamento = da.getDepartamento();
						log.info("................................ departamento.getDepaId(): "
								+ departamento.getDepaId());
						log.info("................................ departamento.getNivel(): "
								+ departamento.getNivel());

						if (departamento.getDepaId().equals(
								departamentoPermisoEmpleado.getDepaId())
								&& (departamento.getNivel()
										.equals(nivelPermiso))) {
							nivelDepartamento = nivelPermiso;
							break;
						}
					}
								
				// **************************************************fin
				// recorrer listado

				Integer auxNivelAprobacion = nivelAprobacion + 1;

				if (est.equals("0") && nivelDepartamento > 1
						&& nivelContrato > 1) {
					Integer nivelRequerido = sv
							.getNivelRequerido();
					if (nivelPermiso > nivelRequerido)
						auxNivelAprobacion = nivelContrato;
					else
						auxNivelAprobacion = nivelDepartamento;
				}

				Departamento depSup = null;
				if (departamentoPermisoEmpleado != null) {
					depSup = departamentoPermisoEmpleado.getDepartamento();
				}

				if (((auxNivelAprobacion.equals(nivelContrato)) || (depSup == null))
						&& (nivelPermiso.equals(nivelDepartamento))) {
							mensaje = "autorizar";		
							log.info("Autorizarrrrrrrrrrrrrrrrrrrrrrrrrrr");
				} else {
					if ((auxNivelAprobacion.equals(nivelPermiso))
							&& (depSup != null)
							&& (nivelPermiso.equals(nivelDepartamento))) {
							mensaje = "pre-autorizar";
							log.info("PREEEEEEEEEEEEEEEEEEEEEE - Autorizarrrrrrrrrrrrrrrrrrrrrrrrrrr");
					} 
					}

				} 
			} 
		}
		return mensaje;

	}
	
	@SuppressWarnings("unchecked")
	private List<Empleado> EmpleadosCargo() {
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
	
	private Departamento RetornoDepartamento(Departamento departamento,
			int nivel) {
		Departamento departamentoAux = departamento;
		int i = (departamento.getNivel());
		if (i >= nivel) {
			i = nivel;
		}
		while (i < nivel) {
			if (departamentoAux != null) {
				departamentoAux = departamentoAux.getDepartamento();
				Hibernate.initialize(departamentoAux.getDepartamento());
			} else {
				departamentoAux = null;
				break;
			}
			i++;
		}
		return departamentoAux;
	}

	
	private Boolean verificarUsuario(){
		Empleado empleado = new Empleado();
		List<DepartamentoAutorizacion> departamentoAutorizacion = new ArrayList<DepartamentoAutorizacion>();
		
		 empleado = gestionPermisoVacacion.buscarEmpleado();
		 departamentoAutorizacionList.getDepartamentoAutorizacion().setEmpleado(empleado);
		  departamentoAutorizacion = departamentoAutorizacionList.getResultList();
		  
		  if(departamentoAutorizacion.size() > 0){
			  return true;
		  }else
			  return false;
	}

	// GET - SET
	public String getTipoGrupo() {
		return tipoGrupo;
	}

	public void setTipoGrupo(String tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	public String getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public String getAutorizacion() {
		return autorizacion;
	}

	public void setAutorizacion(String autorizacion) {
		this.autorizacion = autorizacion;
	}

	public String getAutorizacionSeleccionada() {
		return autorizacionSeleccionada;
	}

	public void setAutorizacionSeleccionada(String autorizacionSeleccionada) {
		this.autorizacionSeleccionada = autorizacionSeleccionada;
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

	public ArrayList<Long> getCiudades() {
		return ciudades;
	}

	public void setCiudades(ArrayList<Long> ciudades) {
		this.ciudades = ciudades;
	}

	public List<Permiso> getListaPermiso() {
		if (listaPermiso == null) 
			listaPermiso = new ArrayList <Permiso>();
		
		return listaPermiso;
	}

	public void setListaPermiso(List<Permiso> listaPermiso) {
		this.listaPermiso = listaPermiso;
	}

	public Map<Long, String> getOpciones() {
		return opciones;
	}

	public void setOpciones(Map<Long, String> opciones) {
		this.opciones = opciones;
	}

	public String getOpcion() {
		return opcion;
	}

	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	public String getAutorizar() {
		return autorizar;
	}

	public void setAutorizar(String autorizar) {
		this.autorizar = autorizar;
	}

	public Integer getProcesados() {
		return procesados;
	}

	public void setProcesados(Integer procesados) {
		this.procesados = procesados;
	}

	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	public Boolean getAcciones() {
		return acciones;
	}

	public void setAcciones(Boolean acciones) {
		this.acciones = acciones;
	}

	public Boolean getBotonProcesar() {
		return botonProcesar;
	}

	public void setBotonProcesar(Boolean botonProcesar) {
		this.botonProcesar = botonProcesar;
	}

	public List<SolicitudVacacion> getListaSolicitudVacacion() {
		return listaSolicitudVacacion;
	}

	public void setListaSolicitudVacacion(List<SolicitudVacacion> listaSolicitudVacacion) {
		this.listaSolicitudVacacion = listaSolicitudVacacion;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

}
