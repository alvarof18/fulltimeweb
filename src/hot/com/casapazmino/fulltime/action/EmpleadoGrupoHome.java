package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoGrupo;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("empleadoGrupoHome")
public class EmpleadoGrupoHome extends EntityHome<EmpleadoGrupo> {

	private static final long serialVersionUID = -8821532613133368944L;
	
	@In
	EntityManager entityManager;
	
	@In
	private FacesMessages facesMessages;

	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;
	
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) {
			setCreatedMessage(expressions
					.createValueExpression("#{messages['mensaje.grabar']}"));
		}
		if (getUpdatedMessage() == null) {
			setUpdatedMessage(expressions
					.createValueExpression("#{messages['mensaje.actualizar']}"));
		}
		if (getDeletedMessage() == null) {
			setDeletedMessage(expressions
					.createValueExpression("#{messages['mensaje.eliminar']}"));
		}
	}

	public void setEmpleadoGrupoEmgrId(Long id) {
		setId(id);
	}

	public Long getEmpleadoGrupoEmgrId() {
		return (Long) getId();
	}

	@Override
	protected EmpleadoGrupo createInstance() {
		EmpleadoGrupo empleadoGrupo = new EmpleadoGrupo();
		return empleadoGrupo;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 

	}

	public boolean isWired() {
		return true;
	}

	public EmpleadoGrupo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Empleado> getEmpleados() {
		return getInstance() == null ? null : new ArrayList<Empleado>(
				getInstance().getEmpleados());
	}

	public String validar(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarDescripcion();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
			
		return mensajeError;
	}
	
	public String validarDescripcion(){
		List<EmpleadoGrupo> empleadoGrupos = this.buscarDescripcion();
		empleadoGrupos.remove(this.getInstance());
		if (empleadoGrupos.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	@SuppressWarnings("unchecked")
	public List<EmpleadoGrupo> buscarDescripcion() {
		
		String descripcion = this.getInstance().getDescripcion();
		
		return (List<EmpleadoGrupo>) entityManager
				.createQuery(
						"select eg from EmpleadoGrupo eg where eg.descripcion = (:descripcion)")
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
					auditoriaHome.asignarCampos("EmpleadoGrupo", "Insertar", cadenaActual, cadenaActual);
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
					auditoriaHome.asignarCampos("EmpleadoGrupo", "Modificar", cadenaAnterior, cadenaActual);
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

	
	@Override
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("EmpleadoGrupo", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
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
