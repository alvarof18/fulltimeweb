package com.casapazmino.fulltime.seguridad.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.persistence.PersistenceProvider;

import com.casapazmino.fulltime.seguridad.model.PermisosMenu;
import com.casapazmino.fulltime.seguridad.model.Roles;

@Name("permisosMenuHome")
public class PermisosMenuHome extends EntityHome<PermisosMenu> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*@In(create = true)
	ItemsMenuRolesHome itemsMenuRolesHome;*/
	@In(create = true)
	RolesHome rolesHome;
	
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

	public void setPermisosMenuId(Integer id) {
		setId(id);
	}

	public Integer getPermisosMenuId() {
		return (Integer) getId();
	}

	@Override
	protected PermisosMenu createInstance() {
		PermisosMenu permisosMenu = new PermisosMenu();
		return permisosMenu;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		/*ItemsMenuRoles itemsMenuRoles = itemsMenuRolesHome.getDefinedInstance();
		if (itemsMenuRoles != null) {
			getInstance().setItemsMenuRoles(itemsMenuRoles);
		}*/
		Roles roles = rolesHome.getDefinedInstance();
		if (roles != null) {
			getInstance().setRoles(roles);
		}
	}

	public boolean isWired() {
		return true;
	}

	public PermisosMenu getDefinedInstance() {
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
//			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				//persisted = super.persist();
				
				getEntityManager().persist(getInstance());
				getEntityManager().flush();
				assignId(PersistenceProvider.instance().getId(getInstance(),getEntityManager()));
				raiseAfterTransactionSuccessEvent();
				persisted =  "persisted";				
				
//				if (persisted.equals("persisted")) {
//					this.setCadenaActual(this.getInstance().toString());
//					auditoriaHome.asignarCampos("PermisosMenu", "Insertar", "hola", "hola2");
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
	
	@Transactional
	@Override
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
//			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
								
				joinTransaction();
				getEntityManager().flush();
				raiseAfterTransactionSuccessEvent();
				updated = "updated";

//				if (updated.equals("updated")) {
//					this.setCadenaActual(this.getInstance().toString());
//					auditoriaHome.asignarCampos("PermisosMenu", "Modificar", cadenaAnterior, cadenaActual);
//					auditoriaHome.persist();
//					FacesMessages.instance().add("#{messages['mensaje.actualizar']}");
//				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessages.instance().add("#{messages['error.actualizar']}");
		}
		return updated;
	}
	
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
//			if (removed.equals("removed")) {
//				this.setCadenaActual(this.getInstance().toString());
//				auditoriaHome.asignarCampos("PermisoMenu", "Eliminar", cadenaAnterior, cadenaActual);
//				auditoriaHome.persist();
//				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");				
//			}
		} catch (Exception e) {
			e.printStackTrace();
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
