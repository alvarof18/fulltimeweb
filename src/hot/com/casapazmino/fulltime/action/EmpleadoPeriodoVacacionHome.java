package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("empleadoPeriodoVacacionHome")
public class EmpleadoPeriodoVacacionHome extends EntityHome<EmpleadoPeriodoVacacion> {

	private static final long serialVersionUID = 3843897807753672979L;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	@In(create = true)
	DetalleTipoParametroHome detalleTipoParametroHome;
	@In(create = true)
	EmpleadoHome empleadoHome;
	
	private String cadenaAnterior;
	private String cadenaActual;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}
		
	public void setEmpleadoPeriodoVacacionEpvaId(Integer id) {
		setId(id);
	}

	public Integer getEmpleadoPeriodoVacacionEpvaId() {
		return (Integer) getId();
	}

	@Override
	protected EmpleadoPeriodoVacacion createInstance() {
		EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
		return empleadoPeriodoVacacion;
	}

	public void wire() {
		
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}
	}

	public boolean isWired() {
/*		if (getInstance().getDetalleTipoParametro() == null)
			return false;*/
		if (getInstance().getEmpleado() == null)
			return false;
		return true;
	}

	public EmpleadoPeriodoVacacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	
	public String persistEmpleadoPeriodo(EmpleadoPeriodoVacacion empleadoPeriodoVacacion){
		this.setInstance(empleadoPeriodoVacacion);
		return this.persist();
	}
	
	
	public List<Permiso> getPermisos() {
		return getInstance() == null ? null : new ArrayList<Permiso>(
				getInstance().getPermisos());
	}
	public List<SolicitudVacacion> getSolicitudVacacions() {
		return getInstance() == null ? null : new ArrayList<SolicitudVacacion>(
				getInstance().getSolicitudVacacions());
	}
	
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
//			mensajeError = this.validar;
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("EmpleadoPeriodoVacacion", "Insertar", cadenaActual, cadenaActual);
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

	@Override
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		try {
//			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
//					this.setCadenaActual(this.getInstance().toString());
//					auditoriaHome.asignarCampos("EmpleadoPeriodoVacacion", "Modificar", cadenaAnterior, cadenaActual);
//					auditoriaHome.persist();
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

	
	@Override
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("EmpleadoPeriodoVacacion", "Eliminar", cadenaAnterior, cadenaActual);
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
