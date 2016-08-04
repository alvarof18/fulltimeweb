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

import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.GrupoContratado;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("detalleGrupoContratadoHome")
public class DetalleGrupoContratadoHome extends
		EntityHome<DetalleGrupoContratado> {

	private static final long serialVersionUID = 1L;
	
	@In
	EntityManager entityManager;
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	GrupoContratadoHome grupoContratadoHome;
	
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

	public void setDetalleGrupoContratadoDgcoId(Long id) {
		setId(id);
	}

	public Long getDetalleGrupoContratadoDgcoId() {
		return (Long) getId();
	}

	@Override
	protected DetalleGrupoContratado createInstance() {
		DetalleGrupoContratado detalleGrupoContratado = new DetalleGrupoContratado();
		return detalleGrupoContratado;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		GrupoContratado grupoContratado = grupoContratadoHome
				.getDefinedInstance();
		if (grupoContratado != null) {
			getInstance().setGrupoContratado(grupoContratado);
		}
	}

	public boolean isWired() {
		return true;
	}
	
	public String validarDetalleGrupoContratado(){
		String mensajeError = "Ninguno";

		mensajeError = this.ValidarDetalleGrupoContratadoDescripcion();
		
		return mensajeError;
	}
	
	public String ValidarDetalleGrupoContratadoDescripcion(){
		List<DetalleGrupoContratado> detalleGrupoContratados = this.buscarDetalleGrupoContratadosDescripcion();
		detalleGrupoContratados.remove(this.getInstance());
		if (detalleGrupoContratados.size() != 0) {
			return "Detalle Grupo Contratado ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<DetalleGrupoContratado> buscarDetalleGrupoContratadosDescripcion() {
		
		String detalleGrupoContratadosDescripcion = this.getInstance().getDescripcion();
		
		return (List<DetalleGrupoContratado>) entityManager
				.createQuery(
						"select dgc from DetalleGrupoContratado dgc where dgc.descripcion = (:detalleGrupoContratadosDescripcion)")
				.setParameter("detalleGrupoContratadosDescripcion", detalleGrupoContratadosDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarDetalleGrupoContratado();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("DetalleGrupoContratado", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarDetalleGrupoContratado();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("DetalleGrupoContratado", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("DetalleGrupoContratado", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	
	public DetalleGrupoContratado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<Empleado> getEmpleados() {
		return getInstance() == null ? null : new ArrayList<Empleado>(
				getInstance().getEmpleados());
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
