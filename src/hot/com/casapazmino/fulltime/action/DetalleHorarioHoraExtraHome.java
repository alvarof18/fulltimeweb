package com.casapazmino.fulltime.action;

import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.DetalleHorarioHoraExtra;
import com.casapazmino.fulltime.model.HoraExtra;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("detalleHorarioHoraExtraHome")
public class DetalleHorarioHoraExtraHome extends EntityHome<DetalleHorarioHoraExtra> {

	private static final long serialVersionUID = -5264484514833622608L;
	
	@In(create = true)
	DetalleGrupoContratadoHome detalleGrupoContratadoHome;

	@In(create = true)
	DetalleHorarioHome detalleHorarioHome;
	
	@In(create = true)
	HoraExtraHome horaExtraHome;
	
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
		

	public void setDetalleHorarioHoraExtraDhheId(Integer id) {
		setId(id);
	}

	public Integer getDetalleHorarioHoraExtraDhheId() {
		return (Integer) getId();
	}

	@Override
	protected DetalleHorarioHoraExtra createInstance() {
		DetalleHorarioHoraExtra detalleHorarioHoraExtra = new DetalleHorarioHoraExtra();
		return detalleHorarioHoraExtra;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		DetalleHorario detalleHorario = detalleHorarioHome.getDefinedInstance();
		if (detalleHorario != null) {
			getInstance().setDetalleHorario(detalleHorario);
		}
		HoraExtra horaExtra = horaExtraHome.getDefinedInstance();
		if (horaExtra != null) {
			getInstance().setHoraExtra(horaExtra);
		}
		DetalleGrupoContratado detalleGrupoContratado = detalleGrupoContratadoHome.getDefinedInstance();
		if (detalleGrupoContratado != null) {
			getInstance().setDetalleGrupoContratado(detalleGrupoContratado);
		}
		
	}

	public boolean isWired() {
/*		if (getInstance().getDetalleHorario() == null)
			return false;
		if (getInstance().getHoraExtra() == null)
			return false;*/
		return true;
	}

	public DetalleHorarioHoraExtra getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
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
		List<DetalleHorarioHoraExtra> detalleHorarioHoraExtras = this.buscarEntidad();
		detalleHorarioHoraExtras.remove(this.getInstance());
		if (detalleHorarioHoraExtras.size() != 0) {
			return "Seleccion ya existe";
		}
		return "Ninguno";
	}
	
	public List<DetalleHorarioHoraExtra> buscarEntidad() {
		DetalleHorarioHoraExtraList detalleHorarioHoraExtraList = (DetalleHorarioHoraExtraList) Component.getInstance("detalleHorarioHoraExtraList",true);
		
		detalleHorarioHoraExtraList.getDetalleHorarioHoraExtra().getDetalleHorario().setDehoId(this.getInstance().getDetalleHorario().getDehoId());
		detalleHorarioHoraExtraList.getDetalleHorarioHoraExtra().getHoraExtra().setHoraExtraId(this.getInstance().getHoraExtra().getHoraExtraId());
		detalleHorarioHoraExtraList.getDetalleHorarioHoraExtra().getDetalleGrupoContratado().setDgcoId(this.getInstance().getDetalleGrupoContratado().getDgcoId());
		
		List<DetalleHorarioHoraExtra> detalleHorarioHoraExtras = detalleHorarioHoraExtraList.getResultList();
		return detalleHorarioHoraExtras;
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
					auditoriaHome.asignarCampos("DetalleHorarioHoraExtra", "Insertar", cadenaActual, cadenaActual);
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
					auditoriaHome.asignarCampos("DetalleHorarioHoraExtra", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("DetalleHorarioHoraExtra", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
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
