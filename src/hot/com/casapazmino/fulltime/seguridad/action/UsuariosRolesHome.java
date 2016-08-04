package com.casapazmino.fulltime.seguridad.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.persistence.PersistenceProvider;

import com.casapazmino.fulltime.seguridad.model.Roles;
import com.casapazmino.fulltime.seguridad.model.Usuarios;
import com.casapazmino.fulltime.seguridad.model.UsuariosRoles;

@Name("usuariosRolesHome")
public class UsuariosRolesHome extends EntityHome<UsuariosRoles> {

	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	RolesHome rolesHome;
	@In(create = true)
	UsuariosHome usuariosHome;

	@In(create = true)
	AuditoriaHome auditoriaHome;

	private String cadenaAnterior;
	private String cadenaActual;
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setUsuariosRolesId(Long id) {
		setId(id);
	}

	public Long getUsuariosRolesId() {
		return (Long) getId();
	}

	@Override
	protected UsuariosRoles createInstance() {
		UsuariosRoles usuariosRoles = new UsuariosRoles();
		return usuariosRoles;
	}

	public void wire() {
		
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		Roles roles = rolesHome.getDefinedInstance();
		if (roles != null) {
			getInstance().setRoles(roles);
		}
		Usuarios usuarios = usuariosHome.getDefinedInstance();
		if (usuarios != null) {
			getInstance().setUsuarios(usuarios);
		}
	}

	public boolean isWired() {
		if (getInstance().getRoles() == null)
			return false;
		if (getInstance().getUsuarios() == null)
			return false;
		return true;
	}

	public UsuariosRoles getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
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
	
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
//			mensajeError = this.validarAtraso();
			if (mensajeError.equals("Ninguno")) {
				getEntityManager().persist(getInstance());
				getEntityManager().flush();
				assignId(PersistenceProvider.instance().getId(getInstance(),getEntityManager()));
				raiseAfterTransactionSuccessEvent();
				persisted = "persisted";

//				if (persisted.equals("persisted")) {
//					this.setCadenaActual(this.getInstance().toString());
//					auditoriaHome.asignarCampos("UsuariosRoles", "Insertar", cadenaActual, cadenaActual);
//					auditoriaHome.persist();
//					FacesMessages.instance().add("#{messages['mensaje.grabar']}");					
//				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessages.instance().add("Error al crear registro");
		}
		return persisted;
	}

	
//	@Transactional
//	public String update()
//	{
//		joinTransaction();
//		getEntityManager().flush();
//		raiseAfterTransactionSuccessEvent();
//		return "updated";
//	}
	
	@Transactional
	@Override
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
//			mensajeError = this.validarAtraso();
			if (mensajeError.equals("Ninguno")) {
				
				joinTransaction();
				getEntityManager().flush();
				raiseAfterTransactionSuccessEvent();
				updated = "updated";

				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("UsuariosRoles", "Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add("#{messages['mensaje.actualizar']}");					
				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			FacesMessages.instance().add("#{messages['error.actualizar']}");
		}
		return updated;
	}
	
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("UsuariosRoles", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
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
}