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

import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.TipoParametro;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("tipoParametroHome")
public class TipoParametroHome extends EntityHome<TipoParametro> {

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
	
	public void setTipoParametroTipaId(Long id) {
		setId(id);
	}

	public Long getTipoParametroTipaId() {
		return (Long) getId();
	}

	@Override
	protected TipoParametro createInstance() {
		TipoParametro tipoParametro = new TipoParametro();
		return tipoParametro;
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

	public TipoParametro getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarTipoParametro(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarTipoParametroDescripcion();
		
		return mensajeError;
	}
	
	public String validarTipoParametroDescripcion(){
		List<TipoParametro> tipoParametros = this.buscarTipoParametroDescripcion();
		tipoParametros.remove(this.getInstance());
		if (tipoParametros.size() != 0) {
			return "Tipo Parametro ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<TipoParametro> buscarTipoParametroDescripcion() {
		
		String tipoParametroDescripcion = this.getInstance().getDescripcion();
		
		return (List<TipoParametro>) entityManager
				.createQuery(
						"select tp from TipoParametro tp where tp.descripcion = (:tipoParametroDescripcion)")
				.setParameter("tipoParametroDescripcion", tipoParametroDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarTipoParametro();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("TipoParametro", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarTipoParametro();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("TipoParametro", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("TipoParametro", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public List<DetalleTipoParametro> getDetalleTipoParametros() {
		return getInstance() == null ? null
				: new ArrayList<DetalleTipoParametro>(getInstance()
						.getDetalleTipoParametros());
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
