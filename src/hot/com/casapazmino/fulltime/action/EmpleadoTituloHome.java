package com.casapazmino.fulltime.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoTitulo;
import com.casapazmino.fulltime.model.Titulo;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("empleadoTituloHome")
public class EmpleadoTituloHome extends EntityHome<EmpleadoTitulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	TituloHome tituloHome;
	@In(create = true)
	DetalleTipoParametroHome detalleTipoParametroHome;
	@In(create = true)
	EmpleadoHome empleadoHome;

	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;	

	public void setEmpleadoTituloEmtiId(Long id) {
		setId(id);
	}

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public Long getEmpleadoTituloEmtiId() {
		return (Long) getId();
	}

	@Override
	protected EmpleadoTitulo createInstance() {
		EmpleadoTitulo empleadoTitulo = new EmpleadoTitulo();
		return empleadoTitulo;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		Titulo titulo = tituloHome.getDefinedInstance();
		if (titulo != null) {
			getInstance().setTitulo(titulo);
		}
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroHome
				.getDefinedInstance();
		if (detalleTipoParametro != null) {
			getInstance().setDetalleTipoParametro(detalleTipoParametro);
		}
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}
	}

	public boolean isWired() {
		if (getInstance().getEmpleado() == null)
			return false;
		return true;
	}

	public EmpleadoTitulo getDefinedInstance() {
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
					auditoriaHome.asignarCampos("EmpleadoTitulo", "Insertar", cadenaActual, cadenaActual);
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
	
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
//			mensajeError = this.validarAtraso();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("EmpleadoTitulo", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("EmpleadoTitulo", "Eliminar", cadenaAnterior, cadenaActual);
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
