package com.casapazmino.fulltime.action;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.PermisoAutorizacion;

@Name("permisoAutorizacionHome")
public class PermisoAutorizacionHome extends EntityHome<PermisoAutorizacion>{
	
	
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;
	
	@In
	EntityManager entityManager;	
	
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	PermisoHome permisoHome;
	
	@In(create = true)
	EmpleadoHome empleadoHome;
	
	private PermisoAutorizacion permisoAutorizacion;
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}
	
	public void setPermisoAutorizacionId(Long id) {
		setId(id);
	}

	public Long getPermisoAutorizacionId() {
		return (Long) getId();
	}

	@Override
	protected PermisoAutorizacion createInstance() {
		PermisoAutorizacion permisoAutorizacion = new PermisoAutorizacion();
		return permisoAutorizacion;
	}

	public void wire() {
		getInstance();			
		Permiso permiso = permisoHome.getDefinedInstance();
		if (permiso != null) {
			getInstance().setPermiso(permiso);
		}
		
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}		
	}

	public boolean isWired() {
		return true;
	}

	public PermisoAutorizacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}	
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		try {
			mensajeError="Ninguno";
			if (mensajeError.equals("Ninguno")) {				
				return super.persist();				
			} else {
				facesMessages.add(mensajeError);
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("Error al crear registro");
		}
		return "persisted";
	}

	@Transactional
	@Override
	public String update(){
		String mensajeError = "Ninguno";
		try {		
			mensajeError="Ninguno";
			if (mensajeError.equals("Ninguno")) {
				return super.update();				
			} else {
				facesMessages.add(mensajeError);
				return null;
			}

		} catch (Exception e) {
			facesMessages.add("#{messages['error.actualizar']}");
		}
		return "updated";
	}

	@Override
	public String remove(){
		try {
			super.remove();
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return "removed";
	}		
}
