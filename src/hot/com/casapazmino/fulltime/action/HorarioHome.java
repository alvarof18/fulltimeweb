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

import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("horarioHome")
public class HorarioHome extends EntityHome<Horario> {

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

	public void setHorarioHoraId(Long id) {
		setId(id);
	}

	public Long getHorarioHoraId() {
		return (Long) getId();
	}

	@Override
	protected Horario createInstance() {
		Horario horario = new Horario();
		return horario;
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

	public Horario getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarHorario(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarDescripcionHorario();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarCodigoHorario();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarDescripcionHorario(){
		List<Horario> horarios = this.buscarDescripcionHorario();
		horarios.remove(this.getInstance());
		if (horarios.size() != 0) {
			return "Horario ya existe";
		}
		return "Ninguno";
	}
	
	public String validarCodigoHorario(){
		List<Horario> horarios = this.buscarCodigoHorario();
		horarios.remove(this.getInstance());
		if (horarios.size() != 0) {
			return "Código ya existe";
		}
		return "Ninguno";
	}
	
	@SuppressWarnings("unchecked")
	public List<Horario> buscarCodigoHorario() {
		
		String horarioCodigo = this.getInstance().getCodigo();
		
		return (List<Horario>) entityManager
				.createQuery(
						"select h from Horario h where h.codigo = (:horarioCodigo)")
				.setParameter("horarioCodigo", horarioCodigo)
				.getResultList();
	}	

	@SuppressWarnings("unchecked")
	public List<Horario> buscarDescripcionHorario() {
		
		String horarioDescripcion = this.getInstance().getDescripcion();
		
		return (List<Horario>) entityManager
				.createQuery(
						"select h from Horario h where h.descripcion = (:horarioDescripcion)")
				.setParameter("horarioDescripcion", horarioDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarHorario();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Horario", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarHorario();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Horario", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Horario", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	
	@SuppressWarnings("unchecked")
	public void buscarDetalleHorario (){
		List<DetalleHorario> detalleHorario = new ArrayList<DetalleHorario>();
		detalleHorario = entityManager.createQuery(
		 "SELECT dh FROM DetalleHorario dh JOIN dh.horario h where h.descripcion=#{planificacion}")
		.getResultList(); 		
	}
	
	public List<DetalleHorario> getDetalleHorarios() {
		return getInstance() == null ? null : new ArrayList<DetalleHorario>(
				getInstance().getDetalleHorarios());
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
