package com.casapazmino.fulltime.action;

import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.Feriado;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("ciudadFeriadoHome")
public class CiudadFeriadoHome extends EntityHome<CiudadFeriado> {

	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	FeriadoHome feriadoHome;
	@In(create = true)
	CiudadHome ciudadHome;
	
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

	public void setCiudadFeriadoCifeId(Long id) {
		setId(id);
	}

	public Long getCiudadFeriadoCifeId() {
		return (Long) getId();
	}

	@Override
	protected CiudadFeriado createInstance() {
		CiudadFeriado ciudadFeriado = new CiudadFeriado();
		return ciudadFeriado;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 

		
		Feriado feriado = feriadoHome.getDefinedInstance();
		if (feriado != null) {
			getInstance().setFeriado(feriado);
		}
		Ciudad ciudad = ciudadHome.getDefinedInstance();
		if (ciudad != null) {
			getInstance().setCiudad(ciudad);
		}
	}

	public boolean isWired() {
		return true;
	}

	
	public String validar(){
		String mensajeError = "Ninguno";
		
		mensajeError = this.validarEntidad();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarEntidad(){
		List<CiudadFeriado> ciudadFeriados = this.buscarEntidad();
		ciudadFeriados.remove(this.getInstance());
		if (ciudadFeriados.size() != 0) {
			return "Seleccion ya existe";
		}
		return "Ninguno";
	}
	
	public List<CiudadFeriado> buscarEntidad() {
		CiudadFeriadoList ciudadFeriadoList = (CiudadFeriadoList) Component.getInstance("ciudadFeriadoList",true);
		
		ciudadFeriadoList.getCiudadFeriado().getCiudad().setCiudId(this.getInstance().getCiudad().getCiudId());
		ciudadFeriadoList.getCiudadFeriado().getFeriado().setFeriId(this.getInstance().getFeriado().getFeriId());
		
		List<CiudadFeriado> ciudadFeriados = ciudadFeriadoList.getResultList();
		return ciudadFeriados;
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
					auditoriaHome.asignarCampos("CiudadFeriado", "Insertar", cadenaActual, cadenaActual);
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
					auditoriaHome.asignarCampos("CiudadFeriado", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("CiudadFeriado", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	
	public CiudadFeriado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
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
