package com.casapazmino.fulltime.comun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.action.EmpleadoPeriodoVacacionHome;
import com.casapazmino.fulltime.action.EmpleadoPeriodoVacacionList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.HistLabo;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.seguridad.action.UsuariosList;
import com.casapazmino.fulltime.seguridad.model.PermisosMenu;
import com.casapazmino.fulltime.seguridad.model.Usuarios;
import com.casapazmino.fulltime.seguridad.model.UsuariosRoles;

@Name("gestionPermisoVacacion")
public class GestionPermisoVacacion {

	@Logger
	Log log;
	
	@In(create = true)
	Credentials credentials;
	
	@In
	EntityManager entityManager;
	
	@In(create = true)
	EmpleadoPeriodoVacacionHome empleadoPeriodoVacacionHome;
	
	Fechas fechas = new Fechas();
	
	public GestionPermisoVacacion() {
	}

	public boolean buscarVacacion(){
		boolean gestionVacacion = false; 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				gestionVacacion = permisosMenu.getVacaciones();
			}
		}
		return gestionVacacion;
	}
	
	public boolean buscarPermiso(){
		boolean gestionPermiso = false; 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				gestionPermiso = permisosMenu.getPermisos();
			}
		}
		
		return gestionPermiso;
	}

	public boolean buscarGlobal(){
		boolean gestionPermiso = false; 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				gestionPermiso = permisosMenu.getGlobal();
			}
		}
		
		return gestionPermiso;
	}
	
	// FILTRA LISTA DE EMPLEADOS EN EL SISTEMA REPORTES - LISTAS
	public int buscarAccesoEmpleados(){
		int accesoEmpleados = 0;
		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				accesoEmpleados = permisosMenu.getAccesoEmpleados();
			}
		}
		
		return accesoEmpleados;
	}
	
	public int buscarAccesoEmpleados(Empleado empleado){
		int accesoEmpleados = 0;

		Usuarios usuarios = empleado.getUsuarios();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				accesoEmpleados = permisosMenu.getAccesoEmpleados();
			}
		}
		return accesoEmpleados;
	}
	
	
	public boolean buscarAutorizaPermiso(){
		boolean autorizaPermiso = false; 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				autorizaPermiso = permisosMenu.getAutorizarPerm();
			}
		}
		
		return autorizaPermiso;
	}
	
	public boolean buscarLegalizarPermiso(){
		boolean autorizaPermiso = false; 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				autorizaPermiso = permisosMenu.getLegalizarPerm();
			}
		}
		
		return autorizaPermiso;
	}
	
	public boolean buscarModificarPlanificacion(){
		boolean modificar_planificacion = false; 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				modificar_planificacion = permisosMenu.getModificar_planificacion();
			}
		}		
		if (modificar_planificacion==true){
			return true;
		}else{
			return false;
		}
		//return modificar_planificacion;		
	}	

	public boolean buscarEliminarPermiso(){
		boolean autorizaPermiso = false; 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				autorizaPermiso = permisosMenu.getEliminarPerm();
			}
		}
		
		return autorizaPermiso;
	}

	
	public Empleado buscarEmpleado(){
		//List<Empleado> empleados = new ArrayList<Empleado>();
		Usuarios usuarios = this.buscarUsuario();
		// Inyectando la clase solo aparece el usuario que se encontro
		// EmpleadoList empleadoList = (EmpleadoList) Component.getInstance("empleadoList",true);
		// Con el new aparecen todos los empleados

		return (Empleado) entityManager
				.createQuery(
						"select e from Empleado e where e.usuarios.id = :usuarioId")
				.setParameter("usuarioId", usuarios.getId())
				.getSingleResult();
		
	}

	public Usuarios buscarUsuario(){
		String u = credentials.getUsername();
		String p = credentials.getPassword();
		
//		Buscar un usuario en la tabla parametro para lo procesos automaticos
//		Ya que como nadie entra al sistema no existe un usario en la sesion
//		
//		WARN: Existe una incoherencia con el constructor de timbreList
//		al momento de eliminar la parte de codigo que llama a estos procesos 
//		este codigo fuente no hace falta
		
//		log.info("================================= Usuario " + u);
//		log.info("================================= clave " + p);
		
		if (u == null) {
			
			DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();		
			
			detalleTipoParametro = entityManager.find(DetalleTipoParametro.class, new Long(Constantes.USUARIO));
			u = detalleTipoParametro.getDescripcion().trim();
			detalleTipoParametro = entityManager.find(DetalleTipoParametro.class, new Long(Constantes.CLAVE));
			p = detalleTipoParametro.getDescripcion().trim(); 

		}
//		fin
		
		UsuariosList usuariosList = (UsuariosList) Component.getInstance("usuariosList",true);
		return usuariosList.obtenerUsuario(u,p,null);
	}

	public Date buscarFechaMaximaPeriodoVacacion(Empleado empleado) {
		return (Date) entityManager
				.createQuery(
						"select max(empleadoPeriodoVacacion.fechaDesde) from EmpleadoPeriodoVacacion empleadoPeriodoVacacion where empleadoPeriodoVacacion.empleado.emplId = (:empleado)")
				.setParameter("empleado", empleado.getEmplId())
				.getSingleResult();
	}	

	public void buscarPeriodosVacacion(Empleado empleado){

		// DETERMINA FACTOR CUANDO LOS MESES DE VACACIONES SON DIFERENTES DE 12
		// AFECTA AL MOMENTO DE CREAR UN NUEVO PERIODO DE VACACIONES
		int factorMesesPeriodo;
		Date fechaMaxima = this.buscarFechaMaximaPeriodoVacacion(empleado);		
		Calendar calendarioMaximo = Fechas.calendarioActual();
		
		if (fechaMaxima == null){
			HistLabo histLabo = this.buscarHistoriaLaboralActivo(empleado);
//			calendarioMaximo.setTime(empleado.getFechaIngreso());
			calendarioMaximo.setTime(histLabo.getFechaIngreso());
			factorMesesPeriodo = 0;
		} else {
			// CREAR UN FACTOR CUANDO LAS VACACIONES SE TOMAN EN MESES DIFERENTES DE 12
			factorMesesPeriodo = 12 - empleado.getDetalleGrupoContratado().getMesesPeriodo();
			calendarioMaximo.setTime(fechaMaxima);
		}
		
//		PARA PERMITIR SALDOS NEGATIVOS DE VACACIONES QUITAR PRIMER COMENTARIO QUE SE ENCUENTRE HACIA ABAJO 
//		YA QUE RESTA EL VALOR EN MESES QUE CONTIENE EL CAMPO MESES_PERIODO DE LA TABLA DETALLE_GRUPO_CONTRATADOS
//		ENTONCES NO GENERA PERIODOS DE VACACIONES DENTRO DEL MISMO AÑO EJ:
//		SI LA FECHA ACTUAL ES 09-08-2012 Y EL EMPLEADO TIENE UNA FECHA DE INGRESO 01-01-2012
//		CON EL COMENTARIO NO LE TOMA EN CUENTA Y NO GENERA PERIODO VACACION
//		SI LA FECHA ACTUAL ES 09-08-2012 Y EL EMPLEADO TIENE UNA FECHA DE INGRESO 01-01-2012
//		SIN EL COMENTARIO GENERA UN PERIODO DE VACACIONES PARA EL AÑO 2013
		
		Calendar calendarioActual = Fechas.calendarioActual();
		//calendarioActual.add(Calendar.MONTH, -empleado.getDetalleGrupoContratado().getMesesPeriodo());
		calendarioActual.set(Calendar.HOUR, 0);
		calendarioActual.set(Calendar.MINUTE,0);
		calendarioActual.set(Calendar.SECOND,0);
		
		List<EmpleadoPeriodoVacacion> empleadoPeriodoVacaciones = this.actualizarComboPeriodoVacaciones(empleado);
		double saldoVacaciones = 0;
		for (EmpleadoPeriodoVacacion empleadoPeriodoVacacion : empleadoPeriodoVacaciones) {
			saldoVacaciones =  saldoVacaciones + this.buscarSaldoVacaciones(empleadoPeriodoVacacion);
		}
		
		double saldoTotalVacaciones = saldoVacaciones + empleado.getDetalleGrupoContratado().getHorasVacacion();
		
		if (calendarioActual.after(calendarioMaximo) && saldoTotalVacaciones <= empleado.getDetalleGrupoContratado().getMaximoHoras()){
			this.crearPeriodoVacacion(calendarioMaximo, empleado, factorMesesPeriodo);
			this.actualizarComboPeriodoVacaciones(empleado);			
		}
	}
	
//	ACTUALIZA LA LISTA DE empleadoPeriodoVacaciones EN LA PAGINA
//	DESPLIEGA SOLO LOS PERIODOS QUE TIENE EL EMPLEADO
//	CASO CONTRARIO EN EL COMBO SE DESPLIEGAN TODOS
	
//	Modificado 21/05/2015
	public List<EmpleadoPeriodoVacacion> actualizarComboPeriodoVacaciones(Empleado empleado){
		
		HistLabo histLabo = this.buscarHistoriaLaboralActivo(empleado);
		
		EmpleadoPeriodoVacacionList empleadoPeriodoVacacionList = (EmpleadoPeriodoVacacionList) Component.getInstance("empleadoPeriodoVacacionList", true);
		
		empleadoPeriodoVacacionList.getEmpleadoPeriodoVacacion().getEmpleado().setEmplId(empleado.getEmplId());
		empleadoPeriodoVacacionList.getEmpleadoPeriodoVacacion().getHistLabo().setHilaId(histLabo.getHilaId());
		
		List<EmpleadoPeriodoVacacion> empleadoPeriodoVacaciones = new ArrayList<EmpleadoPeriodoVacacion>();
		empleadoPeriodoVacaciones = empleadoPeriodoVacacionList.getListaEmpleadoPeriodoVacacion();
		return empleadoPeriodoVacaciones;
		
	} 

	public String crearPeriodoVacacion(Calendar calendarioPeriodoVacacion, Empleado empleado, int factorMesesPeriodo){

		calendarioPeriodoVacacion.add(Calendar.MONTH, empleado.getDetalleGrupoContratado().getMesesPeriodo() + factorMesesPeriodo);
		String fechaDescripcion = Fechas.cambiarFormato(calendarioPeriodoVacacion.getTime(), "yyyy-MM-dd");
		
		EmpleadoPeriodoVacacion empleadoPeriodoVacacionNuevo = new EmpleadoPeriodoVacacion();
		empleadoPeriodoVacacionNuevo.setEmpleado(empleado);
		empleadoPeriodoVacacionNuevo.setFechaDesde(calendarioPeriodoVacacion.getTime());
		empleadoPeriodoVacacionNuevo.setHoras(empleado.getDetalleGrupoContratado().getHorasVacacion());
		empleadoPeriodoVacacionNuevo.setDescripcion("Periodo " + fechaDescripcion);
		
		empleadoPeriodoVacacionHome.persistEmpleadoPeriodo(empleadoPeriodoVacacionNuevo);
		
		return "persisted";
	}
	
	public double buscarSaldoVacaciones(EmpleadoPeriodoVacacion empleadoPeriodoVacacion){
		double totalPermisoVacaciones = 0;
//		double saldoVacaciones;
//		SE TRASNFORMA TODOS LOS REGISTROS A HORAS Y LUEGO NUEVAMENTE A DIAS
//		PARA CALCULOS MAS EXACTOS EN LA VALIDACION
//		TOTAL PERMISOS CON CARGO A VACACIONES EN UN PERIODO
		List<Permiso> permisos =  new ArrayList<Permiso>();
		permisos = this.buscarPermisosVacaciones(empleadoPeriodoVacacion);
		for (Permiso permiso : permisos) {
//			totalPermisoVacaciones = totalPermisoVacaciones + ((permiso.getDias() * 8) + permiso.getNumeroHoras());
			if (permiso.getDetalleTipoParametro().getDtpaId() == 3) {
				totalPermisoVacaciones = totalPermisoVacaciones
						+ ((permiso.getDias() * 8) + permiso.getNumeroHoras());
			}
		}
		
//		TOTAL VACACIONES EN UN PERIODO
		long dias = 0;
		List<SolicitudVacacion> solicitudVacaciones = new ArrayList<SolicitudVacacion>();
		solicitudVacaciones = this.buscarVacaciones(empleadoPeriodoVacacion);
		
		for (SolicitudVacacion solicitudVacacion : solicitudVacaciones) {
			
//			dias=fechas.numeroDias(solicitudVacacion.getFechaDesde(), solicitudVacacion.getFechaHasta());
//			dias = solicitudVacacion.getDiasLaborables();
//			totalPermisoVacaciones = totalPermisoVacaciones + (dias * 8);
			if (solicitudVacacion.getDetalleTipoParametro().getDtpaId() == 3) {
				dias = solicitudVacacion.getDiasLaborables();
				totalPermisoVacaciones = totalPermisoVacaciones + (dias * 8);
			}
			
		}
		
		double totalDias = empleadoPeriodoVacacion.getHoras() * 8;
		double diasPerdidos = empleadoPeriodoVacacion.getDiasPerdidos() * 8; 
		
		// log.info("totalDias: " + totalDias + "diasPerdidos: " + diasPerdidos + "totalPermisoVacaciones" + totalPermisoVacaciones );
		// log.info("empleado " + empleadoPeriodoVacacion);

		return (totalDias - diasPerdidos - totalPermisoVacaciones) / 8;
		
//		RETORNA EL SALDO DE VACACIONES
//		return (((empleadoPeriodoVacacion.getHoras() * 8) - totalPermisoVacaciones) / 8);
	}
	
	public double buscarSaldoVacacionesPeriodo(EmpleadoPeriodoVacacion empleadoPeriodoVacacion){
		double totalPermisoVacaciones = 0;
//		double saldoVacaciones;
//		SE TRASNFORMA TODOS LOS REGISTROS A HORAS Y LUEGO NUEVAMENTE A DIAS
//		PARA CALCULOS MAS EXACTOS EN LA VALIDACION
//		TOTAL PERMISOS CON CARGO A VACACIONES EN UN PERIODO
		List<Permiso> permisos =  new ArrayList<Permiso>();
		permisos = this.buscarPermisosVacaciones(empleadoPeriodoVacacion);
		for (Permiso permiso : permisos) {
			totalPermisoVacaciones = totalPermisoVacaciones + ((permiso.getDias() * 8) + permiso.getNumeroHoras());
		}
		
//		TOTAL VACACIONES EN UN PERIODO
		long dias = 0;
		List<SolicitudVacacion> solicitudVacaciones = new ArrayList<SolicitudVacacion>();
		solicitudVacaciones = this.buscarVacaciones(empleadoPeriodoVacacion);
		for (SolicitudVacacion solicitudVacacion : solicitudVacaciones) {
			dias = fechas.numeroDias(solicitudVacacion.getFechaDesde(), solicitudVacacion.getFechaHasta());
			totalPermisoVacaciones = totalPermisoVacaciones + (dias * 8);
		}
		
		double totalDias = empleadoPeriodoVacacion.getHoras() * 8;
		double diasPerdidos = empleadoPeriodoVacacion.getDiasPerdidos() * 8; 

		log.info("========================== buscarSaldoVacacionesPeriodo " +  empleadoPeriodoVacacion.getEpvaId());
		log.info("============================ GestionPermisoVacacion - Saldo Vacaciones " + (totalDias - diasPerdidos - totalPermisoVacaciones) / 8);
		
		return (totalDias - diasPerdidos - totalPermisoVacaciones) / 8;
		
//		RETORNA EL SALDO DE VACACIONES
//		return (((empleadoPeriodoVacacion.getHoras() * 8) - totalPermisoVacaciones) / 8);
	}
	

	public HistLabo buscarHistoriaLaboralActivo(Empleado empleado) {
		HistLabo histLabo = new HistLabo();
		
		for (HistLabo histLabo1 : empleado.getHistLabos()) {
			if (histLabo1.getEstado() == true) {
				histLabo = histLabo1;
			}
		}
		
		return histLabo;
	}
	
//	Modificado 21/05/2015
	public EmpleadoPeriodoVacacion seleccionarEmpleadoPeriodoVacacion(Empleado empleado){
		
		HistLabo histLabo = this.buscarHistoriaLaboralActivo(empleado);

		EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
		for (EmpleadoPeriodoVacacion empleadoPeriodoVacacion1 : empleado.getEmpleadoPeriodoVacacions()) {
			if (empleadoPeriodoVacacion1.getHistLabo().getHilaId() == histLabo.getHilaId()){
				empleadoPeriodoVacacion = empleadoPeriodoVacacion1;
			}
		}
		
		return empleadoPeriodoVacacion;				
	}

//	BUSCA PERMISOS CON CARGO A VACACIONES EN UN PERIODO DETERMINADO
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisosVacaciones(EmpleadoPeriodoVacacion empleadoPeriodoVacacion) {				
		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p inner join p.tipoPermiso tp where p.empleadoByEmplId.emplId = (:empleado) And p.tipoPermiso.descuento = 'Vacaciones' And p.empleadoPeriodoVacacion.epvaId = (:empleadoPeriodoVacacion)")
				.setParameter("empleado", empleadoPeriodoVacacion.getEmpleado().getEmplId())
				.setParameter("empleadoPeriodoVacacion", empleadoPeriodoVacacion.getEpvaId())
				.getResultList();
	}

//	BUSCA VACACIONES EN UN PERIODO DETERMINADO
	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacaciones(EmpleadoPeriodoVacacion empleadoPeriodoVacacion){
		return  (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select sv from SolicitudVacacion sv where sv.empleadoByEmplId.emplId = (:empleado) And sv.empleadoPeriodoVacacion.epvaId = (:empleadoPeriodoVacacion)")
				.setParameter("empleado", empleadoPeriodoVacacion.getEmpleado().getEmplId())
				.setParameter("empleadoPeriodoVacacion", empleadoPeriodoVacacion.getEpvaId())
				.getResultList();
	}
	
//	=========================================================================
//	VALIDACIONES PARA CONTROLAR REGISTROS DUPLICADOS EN PERMISOS Y VACACIONES	
//	=========================================================================
	
//	BUSCA VACACIONES DONDE LA FECHA DESDE ESTE ENTRE EL RANGO DE LA NUEVA SOLICITUD  INGRESADA
//	SI ENCUENTRA ALGUN REGISTRO QUIERE DECIR QUE NO PUEDE REALIZAR UNA NUEVA SOLICITUD CON ESTAS FECHAS
	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacacionesFechaDesde(Permiso permiso){
		
		Date permisoDesde = permiso.getFechaDesde();
		Date permisoHasta = permiso.getFechaHasta();

		return  (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select sv from SolicitudVacacion sv where sv.empleadoByEmplId.emplId = (:empleado) And sv.fechaDesde Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado", permiso.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.getResultList();
	}

//	BUSCA VACACIONES DONDE LA FECHA HASTA ESTE ENTRE EL RANGO DE LA NUEVA SOLICITUD  INGRESADA
//	SI ENCUENTRA ALGUN REGISTRO QUIERE DECIR QUE NO PUEDE REALIZAR UNA NUEVA SOLICITUD CON ESTAS FECHAS
	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacacionesFechaHasta(Permiso permiso){
		
		Date permisoDesde = permiso.getFechaDesde();
		Date permisoHasta = permiso.getFechaHasta();

		return  (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select sv from SolicitudVacacion sv where sv.empleadoByEmplId.emplId = (:empleado) And sv.fechaHasta Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado", permiso.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.getResultList();
	}

//	BUSCA TODAS VACACIONES DE UN EMPLEADO PARA RECORRER Y COMPARAR FECHAS
	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacacionesEmpleado(Permiso permiso){
		return  (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select sv from SolicitudVacacion sv where sv.empleadoByEmplId.emplId = (:empleado)")
				.setParameter("empleado", permiso.getEmpleadoByEmplId().getEmplId())
				.getResultList();
	}

//	BUSCA PERMISOS DONDE LA FECHA DESDE ESTE ENTRE EL RANGO DEL NUEVO PERMISO INGRESADO
//	SI ENCUENTRA ALGUN REGISTRO QUIERE DECIR QUE NO PUEDE REALIZAR UN PERMISO CON ESTAS FECHAS
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisoFechaDesde(Permiso permiso){
		
		boolean validaFechas = true;

		Date permisoDesde = permiso.getFechaDesde();
		Date permisoHasta = permiso.getFechaHasta();
		
		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado) And p.fechaDesde Between (:permisoDesde) and (:permisoHasta) And p.tipoPermiso.validaFechas = (:validaFechas)")
				.setParameter("empleado", permiso.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.setParameter("validaFechas", validaFechas)				
				.getResultList();
	}
	
//	BUSCA PERMISOS DONDE LA FECHA HASTA ESTE ENTRE EL RANGO DEL NUEVO PERMISO INGRESADO
//	SI ENCUENTRA ALGUN REGISTRO QUIERE DECIR QUE NO PUEDE REALIZAR UN PERMISO CON ESTAS FECHAS
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisoFechaHasta(Permiso permiso){
		
		boolean validaFechas = true;

		Date permisoDesde = permiso.getFechaDesde();
		Date permisoHasta = permiso.getFechaHasta();
						
		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado) And p.fechaHasta Between (:permisoDesde) and (:permisoHasta) And p.tipoPermiso.validaFechas = (:validaFechas)")
				.setParameter("empleado", permiso.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.setParameter("validaFechas", validaFechas)				
				.getResultList();
	}
	
//	BUSCA TODOS LOS PERMISOS DE UN EMPLEADO PARA RECORRER Y COMPARAR FECHAS
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisosEmpleado(Permiso permiso){
		
		boolean validaFechas = true;

		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado) And p.tipoPermiso.validaFechas = (:validaFechas)")
				.setParameter("empleado", permiso.getEmpleadoByEmplId().getEmplId())
				.setParameter("validaFechas", validaFechas)
				.getResultList();
	}
	
//	BUSCA TODOS LOS PERMISOS DE UN EMPLEADO EN UN SOLO DIA
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisoDia(Permiso permiso){
		
		boolean validaFechas = true;
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFecha = Fechas.cambiarFormato(permiso.getFechaDesde(), "yyyy-MM-dd");

		Date permisoDesde = null;
		try {
			permisoDesde =  formato.parse(stringFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado) And " + ControlBaseDatos.modificadorConvertirFecha + "p.fechaDesde) = (:permisoDesde) And p.tipoPermiso.validaFechas = (:validaFechas)")
				.setParameter("empleado", permiso.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("validaFechas", validaFechas)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacacionesFechaDesde(SolicitudVacacion solicitudVacacion){
		Date permisoDesde = solicitudVacacion.getFechaDesde();
		Date permisoHasta = solicitudVacacion.getFechaHasta();

		return  (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select sv from SolicitudVacacion sv where sv.empleadoByEmplId.emplId = (:empleado) And sv.fechaDesde Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado", solicitudVacacion.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacacionesFechaHasta(SolicitudVacacion solicitudVacacion){
		
		Date permisoDesde = solicitudVacacion.getFechaDesde();
		Date permisoHasta = solicitudVacacion.getFechaHasta();

		return  (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select sv from SolicitudVacacion sv where sv.empleadoByEmplId.emplId = (:empleado) And sv.fechaHasta Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado", solicitudVacacion.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacacionesEmpleado(SolicitudVacacion solicitudVacacion){
		return  (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select sv from SolicitudVacacion sv where sv.empleadoByEmplId.emplId = (:empleado)")
				.setParameter("empleado", solicitudVacacion.getEmpleadoByEmplId().getEmplId())
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisoFechaDesde(SolicitudVacacion solicitudVacacion){

		Date permisoDesde = solicitudVacacion.getFechaDesde();
		Date permisoHasta = solicitudVacacion.getFechaHasta();
		
		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado) And p.fechaDesde Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado", solicitudVacacion.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisoFechaHasta(SolicitudVacacion solicitudVacacion){

		Date permisoDesde = solicitudVacacion.getFechaDesde();
		Date permisoHasta = solicitudVacacion.getFechaHasta();
						
		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado) And p.fechaHasta Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado", solicitudVacacion.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisosEmpleado(SolicitudVacacion solicitudVacacion){

		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado)")
				.setParameter("empleado", solicitudVacacion.getEmpleadoByEmplId().getEmplId())
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisoDia(SolicitudVacacion solicitudVacacion){
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFecha = Fechas.cambiarFormato(solicitudVacacion.getFechaDesde(), "yyyy-MM-dd");

		Date solicitudDesde = null;
		try {
			solicitudDesde =  formato.parse(stringFecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return  (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.empleadoByEmplId.emplId = (:empleado) And " + ControlBaseDatos.modificadorConvertirFecha + "p.fechaDesde) = (:permisoDesde)")
				.setParameter("empleado", solicitudVacacion.getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", solicitudDesde)
				.getResultList();
	}
	
	public int saldoVacacionDias(Double saldo){
		int dias = saldo.intValue();
		return dias;
	}
	
	public int saldoVacacionHoras(Double saldo){
		int dias = saldo.intValue();
		float horas = (float) (saldo - dias);
		return (int) (horas * 8);
	}

	public int saldoVacacionMinutos(Double saldo){
		int dias = saldo.intValue();
		float horas = (float) (saldo - dias);
		horas = (horas * 8);
		int minutos = (Math.round( (horas - (int) horas) * 60) );
		return minutos;
	}
	
	public boolean buscarParametroPeriodoVacaciones() {
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		List<DetalleTipoParametro> detalleTipoParametros = detalleTipoParametroList.getListaPeriodoVacaciones();
		
		String parametro = detalleTipoParametros.get(0).getDescripcion();
		if (parametro.toLowerCase().equals("unico")){
			return true;
		} else return false;
	}	

	// DETEMINA A QUE REGIMEN PERTENECE LOSEP O CODIGO DE TRABAJO
	// RETORNA LA FECHA DE CARGA DE VACACIONES SEGUN REGIMEN
	public Date fechaCargaVacacion(Empleado empleado, Date fecha) {
		
		Calendar calendario = Fechas.calendarioActual();
		calendario.setTime(fecha);
 
		if (empleado.getDetalleGrupoContratado().getMesesPeriodo() == 11) {
			calendario.add(Calendar.MONTH, 1); 
		} else if (empleado.getDetalleGrupoContratado().getMesesPeriodo() == 12) {
			calendario.add(Calendar.DATE, 15); 
		}		
		
		return calendario.getTime();
	}
	
	// CALCULA LOS DIAS ADICIONALES EN EL CASO QUE PERTENEZCA AL CODIGO DE TRABAJO
	// EN EL CASO DE QUE PASE LOS CINCO AÑOS LABORABLES
	public int diasAdicionales (Empleado empleado, Date fechaVacacion){
		
		HistLabo histLabo = this.buscarHistoriaLaboralActivo(empleado);
		
//		Long numeroDias = Fechas.numeroDias(empleado.getFechaIngreso(), fechaVacacion);
		
		Long numeroDias = Fechas.numeroDias(histLabo.getFechaIngreso(), fechaVacacion);
		int anios = (int) (numeroDias / 365);		
		return (anios - empleado.getDetalleGrupoContratado().getAnios());
	}	

	public double buscarSaldoVacaciones_solicitudes_aprobadas(EmpleadoPeriodoVacacion empleadoPeriodoVacacion){
		double totalPermisoVacaciones = 0;
//		double saldoVacaciones;
//		SE TRASNFORMA TODOS LOS REGISTROS A HORAS Y LUEGO NUEVAMENTE A DIAS
//		PARA CALCULOS MAS EXACTOS EN LA VALIDACION
//		TOTAL PERMISOS CON CARGO A VACACIONES EN UN PERIODO
		List<Permiso> permisos =  new ArrayList<Permiso>();
		permisos = this.buscarPermisosVacaciones(empleadoPeriodoVacacion);
		for (Permiso permiso : permisos) {
			if(permiso.getDetalleTipoParametro().getDtpaId()==3)
			totalPermisoVacaciones = totalPermisoVacaciones + ((permiso.getDias() * 8) + permiso.getNumeroHoras());
		}
		
//		TOTAL VACACIONES EN UN PERIODO
		long dias = 0;
		List<SolicitudVacacion> solicitudVacaciones = new ArrayList<SolicitudVacacion>();
		solicitudVacaciones = this.buscarVacaciones(empleadoPeriodoVacacion);
		for (SolicitudVacacion solicitudVacacion : solicitudVacaciones) {
			if(solicitudVacacion.getDetalleTipoParametro().getDtpaId()==3){
				dias = fechas.numeroDias(solicitudVacacion.getFechaDesde(), solicitudVacacion.getFechaHasta());
				totalPermisoVacaciones = totalPermisoVacaciones + (dias * 8);
			}			
		}
		
		double totalDias = empleadoPeriodoVacacion.getHoras() * 8;
		double diasPerdidos = empleadoPeriodoVacacion.getDiasPerdidos() * 8; 

		return (totalDias - diasPerdidos - totalPermisoVacaciones) / 8;
	}	
	
	public double buscarSaldoPermisos(EmpleadoPeriodoVacacion empleadoPeriodoVacacion){
		double totalPermisoVacaciones = 0;
		List<Permiso> permisos =  new ArrayList<Permiso>();
		permisos = this.buscarPermisosVacaciones(empleadoPeriodoVacacion);
		Date fecha_emp_per_vaca=empleadoPeriodoVacacion.getFechaDesde();
		for (Permiso permiso : permisos) {
			if(permiso.getFechaDesde().after(fecha_emp_per_vaca)&&permiso.getDetalleTipoParametro().getDtpaId()==3){
				totalPermisoVacaciones = totalPermisoVacaciones + ((permiso.getDias() * 8) + permiso.getNumeroHoras());
			}		
		}

		return (totalPermisoVacaciones) / 8;
	
	}

	public PermisosMenu buscarPermisoMenuUsuario(){
		PermisosMenu autorizaPermiso =new PermisosMenu(); 

		Usuarios usuarios = this.buscarUsuario();
		
		Set<UsuariosRoles> usuariosRoles = usuarios.getUsuariosRoleses();
		for (UsuariosRoles usuariosRoles2 : usuariosRoles) {
			for (PermisosMenu permisosMenu: usuariosRoles2.getRoles().getPermisosMenus()) {
				autorizaPermiso = permisosMenu;
			}
		}
		
		return autorizaPermiso;
	}

	
}
