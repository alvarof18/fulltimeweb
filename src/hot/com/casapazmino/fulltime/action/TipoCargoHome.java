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

import com.casapazmino.fulltime.model.Cargo;
import com.casapazmino.fulltime.model.TipoCargo;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("tipoCargoHome")
public class TipoCargoHome extends EntityHome<TipoCargo> {

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
	
	public void setTipoCargoTicaId(Long id) {
		setId(id);
	}

	public Long getTipoCargoTicaId() {
		return (Long) getId();
	}

	@Override
	protected TipoCargo createInstance() {
		TipoCargo tipoCargo = new TipoCargo();
		return tipoCargo;
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

	public TipoCargo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarTipoCargo(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarTipoCargoDescripcion();
		
		return mensajeError;
	}
	
	public String validarTipoCargoDescripcion(){
		List<TipoCargo> tipoCargos = this.buscarTipoCargoDescripcion();
		tipoCargos.remove(this.getInstance());
		if (tipoCargos.size() != 0) {
			return "Tipo Cargo ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<TipoCargo> buscarTipoCargoDescripcion() {
		
		String tipoCargoDescripcion = this.getInstance().getDescripcion();
		
		return (List<TipoCargo>) entityManager
				.createQuery( 
						"select tipoCargo from TipoCargo tipoCargo where tipoCargo.descripcion = (:tipoCargoDescripcion)")
				.setParameter("tipoCargoDescripcion", tipoCargoDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarTipoCargo();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("TipoCargo", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarTipoCargo();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("TipoCargo", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("TipoCargo", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	public List<Cargo> getCargos() {
		return getInstance() == null ? null : new ArrayList<Cargo>(
				getInstance().getCargos());
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
