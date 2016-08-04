package com.casapazmino.fulltime.action;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoHorario;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("empleadoHorarioHome")
public class EmpleadoHorarioHome extends EntityHome<EmpleadoHorario> {

	private static final long serialVersionUID = 1248072011160291988L;
	
	@In
	EntityManager entityManager;
	
	@In
	private FacesMessages facesMessages;

	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;
		
	@In(create = true)
	EmpleadoHome empleadoHome;
	@In(create = true)
	HorarioHome horarioHome;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) {
			setCreatedMessage(expressions
					.createValueExpression("#{messages['mensaje.grabar']}"));
		}
		if (getUpdatedMessage() == null) {
			setUpdatedMessage(expressions
					.createValueExpression("#{messages['mensaje.actualizar']}"));
		}
		if (getDeletedMessage() == null) {
			setDeletedMessage(expressions
					.createValueExpression("#{messages['mensaje.eliminar']}"));
		}
	}

	public void setEmpleadoHorarioEmhoId(Long id) {
		setId(id);
	}

	public Long getEmpleadoHorarioEmhoId() {
		return (Long) getId();
	}

	@Override
	protected EmpleadoHorario createInstance() {
		EmpleadoHorario empleadoHorario = new EmpleadoHorario();
		return empleadoHorario;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 

		
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}
		Horario horario = horarioHome.getDefinedInstance();
		if (horario != null) {
			getInstance().setHorario(horario);
		}
	}

	public boolean isWired() {
//		if (getInstance().getEmpleado() == null)
//			return false;
//		if (getInstance().getHorario() == null)
//			return false;
		return true;
	}
	
//	public String validar(){
//		String mensajeError = "Ninguno";
//
//		mensajeError = this.validarDescripcion();
//		if (!mensajeError.equals("Ninguno")){
//			return mensajeError;
//		}
//			
//		return mensajeError;
//	}
	
//	public String validarDescripcion(){
//		List<EmpleadoGrupo> empleadoGrupos = this.buscarDescripcion();
//		empleadoGrupos.remove(this.getInstance());
//		if (empleadoGrupos.size() != 0) {
//			return "Descripcion ya existe";
//		}
//		return "Ninguno";
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<EmpleadoHorario> buscarDescripcion() {
//		
//		Date descripcion = this.getInstance().getFechaIni();
//		
//		return (List<EmpleadoHorario>) entityManager
//				.createQuery(
//						"select eg from EmpleadoGrupo eg where eg.descripcion = (:descripcion)")
//				.setParameter("descripcion", descripcion)
//				.getResultList();
//	}
		
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
//			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("EmpleadoHorario", "Insertar", cadenaActual, cadenaActual);
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
//			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("EmpleadoHorario", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("EmpleadoHorario", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	
	public EmpleadoHorario getDefinedInstance() {
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
