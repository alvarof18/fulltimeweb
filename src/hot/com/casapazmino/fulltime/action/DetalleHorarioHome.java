package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.DetalleHorarioHoraExtra;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("detalleHorarioHome")
public class DetalleHorarioHome extends EntityHome<DetalleHorario> {

	private static final long serialVersionUID = 1L;
	
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	HorarioHome horarioHome;

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

	public void setDetalleHorarioDehoId(Long id) {
		setId(id);
	}

	public Long getDetalleHorarioDehoId() {
		return (Long) getId();
	}

	@Override
	protected DetalleHorario createInstance() {
		DetalleHorario detalleHorario = new DetalleHorario();
		return detalleHorario;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		Horario horario = horarioHome.getDefinedInstance();
		if (horario != null) {
			getInstance().setHorario(horario);
		}
	}

	public boolean isWired() {
		return true;
	}

	public DetalleHorario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
//			mensajeError = this.validarProvincia();
			if (mensajeError.equals("Ninguno")) {
//				this.getInstance().setOrden(this.ordenDetalle());
				persisted = super.persist();
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("DetalleHorario", "Insertar", cadenaActual, cadenaActual);
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
//			mensajeError = this.validarProvincia();
			if (mensajeError.equals("Ninguno")) {
//				this.getInstance().setOrden(this.ordenDetalle());
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("DetalleHorario", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("DetalleHorario", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public byte ordenDetalle(){
		
		byte orden = 0;
		if (this.getInstance().getEntradaSalida().equals("E")) {
			orden = 1;
		} else if (this.getInstance().getEntradaSalida().equals("SA")) {
			orden = 2;
		} else if (this.getInstance().getEntradaSalida().equals("EA")) {
			orden = 3;
		} else if (this.getInstance().getEntradaSalida().equals("S")) {
			orden = 4;
		} 
		
		return orden;
	} 
	
	public List<Asistencia> getAsistencias() {
		return getInstance() == null ? null : new ArrayList<Asistencia>(
				getInstance().getAsistencias());
	}
	
	public List<DetalleHorarioHoraExtra> getDetalleHorarioHoraExtras() {
		return getInstance() == null
				? null
				: new ArrayList<DetalleHorarioHoraExtra>(getInstance()
						.getDetalleHorarioHoraExtras());
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
