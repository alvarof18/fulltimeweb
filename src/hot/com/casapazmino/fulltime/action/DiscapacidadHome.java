package com.casapazmino.fulltime.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Discapacidad;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("discapacidadHome")
public class DiscapacidadHome extends EntityHome<Discapacidad> {

	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	EmpleadoHome empleadoHome;
	
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

	public void setDiscapacidadDiscId(Long id) {
		setId(id);
	}

	public Long getDiscapacidadDiscId() {
		return (Long) getId();
	}

	@Override
	protected Discapacidad createInstance() {
		Discapacidad discapacidad = new Discapacidad();
		return discapacidad;
	}

	public void wire() {
		getInstance();
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Discapacidad getDefinedInstance() {
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
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Provincia", "Insertar", cadenaActual, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add("#{messages['mensaje.grabar']}");					
				}
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

	@Override
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
//			mensajeError = this.validarAtraso();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Discapacidad", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Descapacidad", "Eliminar", cadenaAnterior, cadenaActual);
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
