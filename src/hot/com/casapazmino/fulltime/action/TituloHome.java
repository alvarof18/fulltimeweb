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

import com.casapazmino.fulltime.model.EmpleadoTitulo;
import com.casapazmino.fulltime.model.Titulo;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("tituloHome")
public class TituloHome extends EntityHome<Titulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}
	
	public void setTituloTituId(Long id) {
		setId(id);
	}

	public Long getTituloTituId() {
		return (Long) getId();
	}

	@Override
	protected Titulo createInstance() {
		Titulo titulo = new Titulo();
		return titulo;
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

	public Titulo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarTitulo(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarTituloDescripcion();
		
		return mensajeError;
	}
	
	public String validarTituloDescripcion(){
		List<Titulo> titulos = this.buscarTituloDescripcion();
		titulos.remove(this.getInstance());
		if (titulos.size() != 0) {
			return "Titulo ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Titulo> buscarTituloDescripcion() {
		
		String tituloDescripcion = this.getInstance().getDescripcion();
		
		return (List<Titulo>) entityManager
				.createQuery(
						"select t from Titulo t where t.descripcion = (:tituloDescripcion)")
				.setParameter("tituloDescripcion", tituloDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarTitulo();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Titulo", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarTitulo();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Titulo", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Titulo", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public List<EmpleadoTitulo> getEmpleadoTitulos() {
		return getInstance() == null ? null : new ArrayList<EmpleadoTitulo>(
				getInstance().getEmpleadoTitulos());
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
