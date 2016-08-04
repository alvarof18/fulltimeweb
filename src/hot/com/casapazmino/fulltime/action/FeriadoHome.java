package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.Feriado;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("feriadoHome")
public class FeriadoHome extends EntityHome<Feriado> {

	private static final long serialVersionUID = 1L;
	
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

	public void setFeriadoFeriId(Long id) {
		setId(id);
	}

	public Long getFeriadoFeriId() {
		return (Long) getId();
	}

	@Override
	protected Feriado createInstance() {
		Feriado feriado = new Feriado();
		return feriado;
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
					auditoriaHome.asignarCampos("Feriado", "Insertar", cadenaActual, cadenaActual);
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
	

	public Feriado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
//			mensajeError = this.validarAtraso();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Feriado", "Modificar", cadenaAnterior, cadenaActual);
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
	

	@Override
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("Feriado", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public List<CiudadFeriado> getCiudadFeriados() {
		return getInstance() == null ? null : new ArrayList<CiudadFeriado>(
				getInstance().getCiudadFeriados());
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
