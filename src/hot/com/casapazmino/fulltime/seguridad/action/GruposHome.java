package com.casapazmino.fulltime.seguridad.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.seguridad.model.Grupos;
import com.casapazmino.fulltime.seguridad.model.ItemMenu;
import com.casapazmino.fulltime.seguridad.model.ItemsMenuRoles;
import com.casapazmino.fulltime.seguridad.model.Subgrupos;

@Name("gruposHome")
public class GruposHome extends EntityHome<Grupos> {

	private static final long serialVersionUID = 1L;
	
	@In
	private FacesMessages facesMessages;
	@In
	EntityManager entityManager;
	
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
	
	public void setGruposId(Long id) {
		setId(id);
	}

	public Long getGruposId() {
		return (Long) getId();
	}

	@Override
	protected Grupos createInstance() {
		return new Grupos();
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

	public Grupos getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validar(){
		String mensajeError = "Ninguno";
		
		mensajeError = this.validarDescripcion();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarNombre();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarEtiqueta();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarDescripcion(){
		List<Grupos> grupos = this.buscarDescripcion();
		grupos.remove(this.getInstance());
		if (grupos.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	public String validarNombre(){
		List<Grupos> grupos = this.buscarNombre();
		grupos.remove(this.getInstance());
		if (grupos.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}

	public String validarEtiqueta(){
		List<Grupos> grupos = this.buscarEtiqueta();
		grupos.remove(this.getInstance());
		if (grupos.size() != 0) {
			return "Etiqueta ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Grupos> buscarDescripcion() {
		
		String descripcion = this.getInstance().getDescripcion();
		
		return (List<Grupos>) entityManager
				.createQuery(
						"select g from Grupos g where g.descripcion = (:descripcion)")
				.setParameter("descripcion", descripcion)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Grupos> buscarNombre() {
		
		String nombre = this.getInstance().getNombre();
		
		return (List<Grupos>) entityManager
				.createQuery(
						"select g from Grupos g where g.nombre = (:nombre)")
				.setParameter("nombre", nombre)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Grupos> buscarEtiqueta() {
		
		String etiqueta = this.getInstance().getEtiqueta();
		
		return (List<Grupos>) entityManager
				.createQuery(
						"select g from Grupos g where g.etiqueta = (:etiqueta)")
				.setParameter("etiqueta", etiqueta)
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
					auditoriaHome.asignarCampos("Grupos", "Insertar", cadenaActual, cadenaActual);
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
					auditoriaHome.asignarCampos("Grupos", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Grupos", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");									
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	
	public List<ItemMenu> getItemMenus() {
		return getInstance() == null ? null : new ArrayList<ItemMenu>(
				getInstance().getItemMenus());
	}
	public List<ItemsMenuRoles> getItemsMenuRoleses() {
		return getInstance() == null ? null : new ArrayList<ItemsMenuRoles>(
				getInstance().getItemsMenuRoleses());
	}
	public List<Subgrupos> getSubgruposes() {
		return getInstance() == null ? null : new ArrayList<Subgrupos>(
				getInstance().getSubgruposes());
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