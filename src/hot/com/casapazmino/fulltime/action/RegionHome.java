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

import com.casapazmino.fulltime.anotaciones.Trackable;
import com.casapazmino.fulltime.model.Provincia;
import com.casapazmino.fulltime.model.Region;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Trackable
@Name("regionHome")
public class RegionHome extends EntityHome<Region> {

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
	

	public void setRegionRegiId(Long id) {
		setId(id);
	}

	public Long getRegionRegiId() {
		return (Long) getId();
	}

	@Override
	protected Region createInstance() {
		Region region = new Region();
		return region;
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

	public Region getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}


	public List<Provincia> getProvincias() {
		return getInstance() == null ? null : new ArrayList<Provincia>(
				getInstance().getProvincias());
	}

	public String validarRegion(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarRegionDescripcion();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarCodigo();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarRegionDescripcion(){
		List<Region> regiones = this.buscarRegionDescripcion();
		regiones.remove(this.getInstance());
		if (regiones.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	public String validarCodigo(){
		List<Region> regiones = this.buscarCodigo();
		regiones.remove(this.getInstance());
		if (regiones.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Region> buscarRegionDescripcion() {
		
		String regionDescripcion = this.getInstance().getDescripcion();
		
		return (List<Region>) entityManager
				.createQuery(
						"select r from Region r where r.descripcion = (:regionDescripcion)")
				.setParameter("regionDescripcion", regionDescripcion)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Region> buscarCodigo() {
		
		String codigo = this.getInstance().getCodigo();
		
		return (List<Region>) entityManager
				.createQuery(
						"select r from Region r where r.codigo = (:codigo)")
				.setParameter("codigo", codigo)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarRegion();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Region", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarRegion();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Region", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Region", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	
//	@Override
//	public String persist(){
//		try {
//			super.persist();
//		} catch (Exception e) {
//			FacesMessages.instance().add("Error al crear registro");
//		}
//		return "persisted";
//	}
//	
//	@Transactional
//    @Tracked(values = {
//            @TrackedValue(parameter = "name", expr = "#{regionHome.cadenaAnterior}"),
//            @TrackedValue(parameter = "id", expr = "#{regionHome.cadenaActual}")
//                })
//	@Override
//	public String update(){
//		try {
//			super.update();
//			
//			this.setCadenaActual(this.getInstance().toString());
//
//		} catch (Exception e) {
//			FacesMessages.instance().add("Error al modificar registro");
//		}
//		return "updated";
//	}

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
	
	
//	@WebServiceRef(CalculadoraWS.class)
//	private CalculadoraWS calculadora;
//	
//	int resultado = calculadora.add(10,10);
//
//	public CalculadoraWS getCalculadora() {
//		return calculadora;
//	}
//	
//	public void setCalculadora(CalculadoraWS calculadora) {
//		this.calculadora = calculadora;
//	}
//
//
//	public int getResultado() {
//		return resultado;
//	}
//
//	public void setResultado(int resultado) {
//		this.resultado = resultado;
//	}
	
}