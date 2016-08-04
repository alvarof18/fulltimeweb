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

import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.model.Provincia;
import com.casapazmino.fulltime.model.Region;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("provinciaHome")
public class ProvinciaHome extends EntityHome<Provincia> {

	private static final long serialVersionUID = 1L;
		
	@In(create = true)
	RegionHome regionHome;
	
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

	public void setProvinciaProvId(Long id) {
		setId(id);
	}

	public Long getProvinciaProvId() {
		return (Long) getId();
	}

	@Override
	protected Provincia createInstance() {
		Provincia provincia = new Provincia();
		return provincia;
	}

	public void wire() {
		
		getInstance();
				
		Region region = regionHome.getDefinedInstance();
		if (region != null) {
			getInstance().setRegion(region);
		}
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 

	}

	public boolean isWired() {
//		if (getInstance().getRegion() == null)
//			return false;
		return true;
	}
	
	public Provincia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarProvincia(){
		String mensajeError = "Ninguno";
		
		mensajeError = this.validarDescripcionProvincia();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarCodigo();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarDescripcionProvincia(){
		List<Provincia> provincias = this.buscarDescripcionProvincia();
		provincias.remove(this.getInstance());
		if (provincias.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	public String validarCodigo(){
		List<Provincia> provincias = this.buscarCodigo();
		provincias.remove(this.getInstance());
		if (provincias.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Provincia> buscarDescripcionProvincia() {
		
		String provinciaDescripcion = this.getInstance().getDescripcion();
		
		return (List<Provincia>) entityManager
				.createQuery(
						"select p from Provincia p where p.descripcion = (:provinciaDescripcion)")
				.setParameter("provinciaDescripcion", provinciaDescripcion)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Provincia> buscarCodigo() {
		
		String codigo = this.getInstance().getCodigo();
		
		return (List<Provincia>) entityManager
				.createQuery(
						"select p from Provincia p where p.codigo = (:codigo)")
				.setParameter("codigo", codigo)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarProvincia();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Provincia", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarProvincia();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Provincia", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Provincia", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");					
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public List<Ciudad> getCiudads() {
		return getInstance() == null ? null : new ArrayList<Ciudad>(
				getInstance().getCiudads());
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
