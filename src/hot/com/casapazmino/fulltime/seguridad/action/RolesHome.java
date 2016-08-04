package com.casapazmino.fulltime.seguridad.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.seguridad.model.ItemsMenuRoles;
import com.casapazmino.fulltime.seguridad.model.PermisosMenu;
import com.casapazmino.fulltime.seguridad.model.Roles;
import com.casapazmino.fulltime.seguridad.model.UsuariosRoles;

@Name("rolesHome")
public class RolesHome extends EntityHome<Roles> {

	private static final long serialVersionUID = 1L;

	@Logger
	Log log;
	
	@In
	private FacesMessages facesMessages;
	
	@In
	EntityManager entityManager;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;	
	
	private PermisosMenuHome permisosHome;
	private Boolean crear;
	private Boolean editar;
	private Boolean eliminar;
	private Boolean ver;
	private Boolean imprimir;
	private Boolean permisos;
	private Boolean vacaciones;
	private Boolean global;
	private Boolean eliminarPerm;
	private Boolean autorizarPerm;
	private Boolean legalizarPerm;
	private Boolean modificar_planificacion;
	private int accesoEmpleados;
	
	private Boolean actualizarPendiente;
	private Boolean actualizarAutorizado;
	private Boolean eliminarPendiente;
	private Boolean eliminarAutorizado;	
	private Boolean autorizarVacacion;
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setRolesId(Long id) {
		setId(id);
	}

	public Long getRolesId() {
		return (Long) getId();
	}

	@Override
	protected Roles createInstance() {
		Roles roles = new Roles();
		return roles;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior("");
		
		if(this.getInstance().getId()!=null)
		{
			this.getChecksPermisos();
		}
	}

	public boolean isWired() {
		return true;
	}
	
	public String guardar()
	{
		log.info("ENTRO EN GUARDAR EN ROLES");
		try
		{
			String estado = this.persist();
			if (estado.equals("persisted")){
				return this.guardarPermisos(this);	
			} else return "";
			
		}
		catch (Exception e) {
			log.info("ERROR - ROLES HOME: ",e);
		}
		return null;
	}
	
	private String guardarPermisos(RolesHome rh)
	{
		try
		{
			permisosHome = new PermisosMenuHome();
			permisosHome.getInstance().setCrear(rh.crear);
			permisosHome.getInstance().setModificar(rh.editar);
			permisosHome.getInstance().setEliminar(rh.eliminar);
			permisosHome.getInstance().setVer(rh.ver);
			permisosHome.getInstance().setImprimir(rh.imprimir);
			permisosHome.getInstance().setPermisos(rh.permisos);
			permisosHome.getInstance().setVacaciones(rh.vacaciones);
			permisosHome.getInstance().setGlobal(rh.global);
			permisosHome.getInstance().setEliminarPerm(rh.eliminarPerm);
			permisosHome.getInstance().setAutorizarPerm(rh.autorizarPerm);
			permisosHome.getInstance().setLegalizarPerm(rh.legalizarPerm);
			permisosHome.getInstance().setAccesoEmpleados(rh.accesoEmpleados);
			permisosHome.getInstance().setModificar_planificacion(rh.modificar_planificacion);
			
			permisosHome.getInstance().setActualizarPendiente(this.actualizarPendiente);
			permisosHome.getInstance().setActualizarAutorizado(this.actualizarAutorizado);
			permisosHome.getInstance().setEliminarPendiente(this.eliminarPendiente);
			permisosHome.getInstance().setEliminarAutorizado(this.eliminarAutorizado);
			permisosHome.getInstance().setAutorizarVacacion(this.autorizarVacacion);
			
			permisosHome.getInstance().setRoles(rh.getInstance());
			
			return permisosHome.persist();
		}
		catch (Exception e) {
			log.info("ERROR - ROLES HOME: ",e);
		}
		return null;
	}
	
	public String actualizar()
	{
		log.info("ENTRO EN ACTUALIZAR");
		String estado = this.update();
		if (estado.equals("updated")){
			return this.actualizarPermisos();	
		} else return "";
		
	}
	
	public String actualizarPermisos()
	{
		try
		{
			PermisosMenu prm=new PermisosMenu();
			this.permisosHome=new PermisosMenuHome();
			for(PermisosMenu pr:this.getPermisosMenu())
			{
				log.info("ID DE PERMISOS MENU: "+pr.getId());
				
				prm.setId(pr.getId());
				this.permisosHome.setId(pr.getId());
			}
			
			if(permisosHome.getInstance().getRoles()!=null) {
				log.info("ID DE ROL SETEADO EN PERMISOS: "+permisosHome.getInstance().getRoles().getId());
			}
			
			this.permisosHome.find();
			permisosHome.getInstance().setCrear(this.getCrear());
			permisosHome.getInstance().setModificar(this.getEditar());
			permisosHome.getInstance().setEliminar(this.getEliminar());
			permisosHome.getInstance().setVer(this.getVer());
			permisosHome.getInstance().setImprimir(this.getImprimir());
			permisosHome.getInstance().setPermisos(this.getPermisos());
			permisosHome.getInstance().setVacaciones(this.getVacaciones());
			permisosHome.getInstance().setGlobal(this.getGlobal());
			permisosHome.getInstance().setEliminarPerm(this.getEliminarPerm());
			permisosHome.getInstance().setAutorizarPerm(this.getAutorizarPerm());
			permisosHome.getInstance().setLegalizarPerm(this.getLegalizarPerm());
			permisosHome.getInstance().setAccesoEmpleados(this.getAccesoEmpleados());
			permisosHome.getInstance().setModificar_planificacion(this.getModificar_planificacion());
			
			permisosHome.getInstance().setActualizarPendiente(this.actualizarPendiente);
			permisosHome.getInstance().setActualizarAutorizado(this.actualizarAutorizado);
			permisosHome.getInstance().setEliminarPendiente(this.eliminarPendiente);
			permisosHome.getInstance().setEliminarAutorizado(this.eliminarAutorizado);
			permisosHome.getInstance().setAutorizarVacacion(this.autorizarVacacion);
			
			return this.permisosHome.update();
		}
		catch (Exception e) {
			log.info("ERROR - ROLES HOME: ",e);
		}
		return null;
	}
	
	public String eliminar(){
		try {
			this.remove();
			return "removed";
		} catch (Exception e) {
			// REVISAR COMO FUNCIONA ESTO sirve para desplegar mensajes en las paginas
			//StatusMessage.getBundleMessage("mensaje", "mensaje");
			
			// SE PUEDE PONER ASI Y NO HACE FALTA INYECTAR FacesMessages
			//FacesMessages.instance().add("No puede eliminar rol, revise lista de permisos");
			facesMessages.add("No puede eliminar rol, revise lista de permisos");
			return null;
		}
	}
	
	public void getChecksPermisos()
	{
		try
		{
			for(PermisosMenu pr:this.getPermisosMenu())
			{
				log.info("valor de Check crear:"+pr.getCrear());
				this.setCrear(pr.getCrear());
				this.setEditar(pr.getModificar());
				this.setEliminar(pr.getEliminar());
				this.setVer(pr.getVer());
				this.setImprimir(pr.getImprimir());
				this.setPermisos(pr.getPermisos());
				this.setVacaciones(pr.getVacaciones());
				this.setGlobal(pr.getGlobal());
				this.setEliminarPerm(pr.getEliminarPerm());
				this.setAutorizarPerm(pr.getAutorizarPerm());
				this.setLegalizarPerm(pr.getLegalizarPerm());
				this.setAccesoEmpleados(pr.getAccesoEmpleados());
				this.setModificar_planificacion(pr.getModificar_planificacion());
				
				this.setActualizarPendiente(pr.getActualizarPendiente());
				this.setActualizarAutorizado(pr.getActualizarAutorizado());
				this.setEliminarPendiente(pr.getEliminarPendiente());
				this.setEliminarAutorizado(pr.getEliminarAutorizado());
				this.setAutorizarVacacion(pr.getAutorizarVacacion());
				
			}
		}
		catch (Exception e) {
			log.info("ERROR - ROLES HOME: ",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PermisosMenu> getPermisosMenu()
	{
		try
		{
			String consulta="SELECT permisos FROM PermisosMenu permisos WHERE permisos.roles=?1";
			Query query=this.getEntityManager().createQuery(consulta);
			query.setParameter(1, this.getInstance());
	
			return (List<PermisosMenu>)query.getResultList();
		}
		catch (Exception e) {
			log.info("ERROR - ROLES HOME: ",e);
		}
		return new ArrayList<PermisosMenu>();
	}
	
	public String validar(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarDescripcion();
		
		return mensajeError;
	}
	
	public String validarDescripcion(){
		List<Roles> roles = this.buscarDescripcion();
		roles.remove(this.getInstance());
		if (roles.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	

	@SuppressWarnings("unchecked")
	public List<Roles> buscarDescripcion() {
		
		String descripcion = this.getInstance().getDescripcion();
		
		return (List<Roles>) entityManager
				.createQuery(
						"select r from Roles r where r.descripcion = (:descripcion)")
				.setParameter("descripcion", descripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Roles", "Insertar", cadenaActual, cadenaActual);
					auditoriaHome.persist();
					facesMessages.add("#{messages['mensaje.grabar']}");
				}
			} else {
				facesMessages.add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("Error al crear registro");
		}
		return persisted;
	}

	@Transactional
	@Override
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Roles", "Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					facesMessages.add("#{messages['mensaje.actualizar']}");
				}
			} else {
				facesMessages.add(mensajeError);
			}

		} catch (Exception e) {
			facesMessages.add("#{messages['error.actualizar']}");
		}
		return updated;
	}

	
//	@Transactional
//	public String persist()
//	{
//		getEntityManager().persist(getInstance());
//		getEntityManager().flush();
//		assignId(PersistenceProvider.instance().getId(getInstance(),getEntityManager()));
//		raiseAfterTransactionSuccessEvent();
//		return "persisted";
//	}
//	
//	@Transactional
//	public String update()
//	{
//		joinTransaction();
//		getEntityManager().flush();
//		raiseAfterTransactionSuccessEvent();
//		return "updated";
//	}
	
	@Override
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("Roles", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	
	/**
	 * METODOS GET Y SET
	 * 
	 */

	public Roles getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<ItemsMenuRoles> getItemsMenuRoleses() {
		return getInstance() == null ? null : new ArrayList<ItemsMenuRoles>(
				getInstance().getItemsMenuRoleses());
	}
	public List<PermisosMenu> getPermisosMenus() {
		return getInstance() == null ? null : new ArrayList<PermisosMenu>(
				getInstance().getPermisosMenus());
	}
	public List<UsuariosRoles> getUsuariosRoleses() {
		return getInstance() == null ? null : new ArrayList<UsuariosRoles>(
				getInstance().getUsuariosRoleses());
	}
	
	public Boolean getCrear() {
		return crear;
	}

	public void setCrear(Boolean crear) {
		this.crear = crear;
	}
	
	public Boolean getEditar() {
		return editar;
	}

	public void setEditar(Boolean editar) {
		this.editar = editar;
	}

	public Boolean getEliminar() {
		return eliminar;
	}

	public void setEliminar(Boolean eliminar) {
		this.eliminar = eliminar;
	}

	public Boolean getVer() {
		return ver;
	}

	public void setVer(Boolean ver) {
		this.ver = ver;
	}

	public Boolean getImprimir() {
		return imprimir;
	}

	public void setImprimir(Boolean imprimir) {
		this.imprimir = imprimir;
	}

	public Boolean getVacaciones() {
		return vacaciones;
	}

	public void setVacaciones(Boolean vacaciones) {
		this.vacaciones = vacaciones;
	}

	public Boolean getPermisos() {
		return permisos;
	}

	public void setPermisos(Boolean permisos) {
		this.permisos = permisos;
	}

	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	public Boolean getEliminarPerm() {
		return eliminarPerm;
	}

	public void setEliminarPerm(Boolean eliminarPerm) {
		this.eliminarPerm = eliminarPerm;
	}

	public Boolean getAutorizarPerm() {
		return autorizarPerm;
	}

	public void setAutorizarPerm(Boolean autorizarPerm) {
		this.autorizarPerm = autorizarPerm;
	}

	public Boolean getLegalizarPerm() {
		return legalizarPerm;
	}

	public void setLegalizarPerm(Boolean legalizarPerm) {
		this.legalizarPerm = legalizarPerm;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	public Boolean getModificar_planificacion() {
		return modificar_planificacion;
	}

	public void setModificar_planificacion(Boolean modificar_planificacion) {
		this.modificar_planificacion = modificar_planificacion;
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

	public Boolean getActualizarPendiente() {
		return actualizarPendiente;
	}

	public void setActualizarPendiente(Boolean actualizarPendiente) {
		this.actualizarPendiente = actualizarPendiente;
	}

	public Boolean getActualizarAutorizado() {
		return actualizarAutorizado;
	}

	public void setActualizarAutorizado(Boolean actualizarAutorizado) {
		this.actualizarAutorizado = actualizarAutorizado;
	}

	public Boolean getEliminarPendiente() {
		return eliminarPendiente;
	}

	public void setEliminarPendiente(Boolean eliminarPendiente) {
		this.eliminarPendiente = eliminarPendiente;
	}

	public Boolean getEliminarAutorizado() {
		return eliminarAutorizado;
	}

	public void setEliminarAutorizado(Boolean eliminarAutorizado) {
		this.eliminarAutorizado = eliminarAutorizado;
	}

	public Boolean getAutorizarVacacion() {
		return autorizarVacacion;
	}

	public void setAutorizarVacacion(Boolean autorizarVacacion) {
		this.autorizarVacacion = autorizarVacacion;
	}
}