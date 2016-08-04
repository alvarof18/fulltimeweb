package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.DepartamentoAutorizacion;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("departamentoAutorizacionHome")
public class DepartamentoAutorizacionHome extends EntityHome<DepartamentoAutorizacion> {

	/**
	 * 
	 */
	
	@In(create = true)
	DepartamentoHome departamentoHome;
	
	@In(create = true)
	DepartamentoAutorizacionList departamentoAutorizacionList;
	
	private static final long serialVersionUID = 1L;
	
	@In
	EntityManager entityManager;
	
	@In
	private FacesMessages facesMessages;
	
	@Logger
	Log log;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;	
	
	private String cadenaAnterior;
	private String cadenaActual;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setDepartamentoAutorizacionDepAutId(Long id) {
		setId(id);
	}

	public Long getDepartamentoAutorizacionDepAutId() {
		return (Long) getId();
	}

	@Override
	protected DepartamentoAutorizacion createInstance() {
		DepartamentoAutorizacion departamento = new DepartamentoAutorizacion();
		return departamento;
	}

	public void wire() {
		getInstance();
		
		Departamento departamento = departamentoHome.getDefinedInstance();
		if (departamento != null) {
			getInstance().setDepartamento(departamento);			
			
		}
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 	
	}

	public boolean isWired() {
		return true;
	}

	public DepartamentoAutorizacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	private String validadorPersonalAutoriza(){
		//departamentoAutorizacionList = (DepartamentoAutorizacionList) Component.getInstance("departamentoAutorizacionList", true);
		
		int acceso=(gestionPermisoVacacion.buscarAccesoEmpleados(this.getInstance().getEmpleado()));
		
		if(acceso==2)
			return "Ninguno";
		else{
			Long id=this.getInstance().getEmpleado().getEmplId();
			
			//departamentoAutorizacionList.SetDepartamentoAutorizacion(this.instance);
			departamentoAutorizacionList.getDepartamentoAutorizacion().getEmpleado().setEmplId(id);
			departamentoAutorizacionList.getDepartamentoAutorizacion().setDepaId(null);
			
			List<DepartamentoAutorizacion> departamentoAutorizacion = new ArrayList<DepartamentoAutorizacion>();
			departamentoAutorizacion=departamentoAutorizacionList.getResultList();
			
			if(departamentoAutorizacion.size()>0)
				return "El empleado ya se encuentra registrado!";
			else
				return "Ninguno";
		}		
		
	}	

	private String validadorPersonalAutorizaUpdate(){
		
		//departamentoAutorizacionList = (DepartamentoAutorizacionList) Component.getInstance("departamentoAutorizacionList", true);
		
		int acceso=(gestionPermisoVacacion.buscarAccesoEmpleados(this.getInstance().getEmpleado()));
		
		if(acceso==2)
			return "Ninguno";
		else{
			List<DepartamentoAutorizacion> departamentoAutorizacion = new ArrayList<DepartamentoAutorizacion>();	
			
			Long id=this.getInstance().getEmpleado().getEmplId();
			Long id_dep_aut=this.getInstance().getDepaId();
			//log.info("IIIIIIIIIIIIIIIIIDDDDDDDDDDDDDDDDDDDDDDDDDDD:                   :"+id);
			
			departamentoAutorizacionList.getDepartamentoAutorizacion().getEmpleado().setEmplId(id);
			departamentoAutorizacionList.getDepartamentoAutorizacion().setDepaId(id_dep_aut);
			departamentoAutorizacion=departamentoAutorizacionList.getResultList();
			
			log.info("departamentoAutorizacion.size():                   :"+departamentoAutorizacion.size());
			
			if(departamentoAutorizacion.size()>0){
				return "Ninguno";
			}else{
				//departamentoAutorizacionList = (DepartamentoAutorizacionList) Component.getInstance("departamentoAutorizacionList", true);
				
				departamentoAutorizacionList.getDepartamentoAutorizacion().getEmpleado().setEmplId(id);		
				departamentoAutorizacionList.getDepartamentoAutorizacion().setDepaId(null);
				
				departamentoAutorizacion=departamentoAutorizacionList.getResultList();
				
				log.info("departamentoAutorizacion.size(2):                   :"+departamentoAutorizacion.size());
				
				if(departamentoAutorizacion.size()>0)
					return "El empleado ya se encuentra registrado!";
				else
					return "Ninguno";
			}		
		}	
	}

	@Transactional
	@Override
	public String persist(){
		String mensajeError = validadorPersonalAutoriza();
		String persisted = null;
		try {			
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Departamento_Autorización", "Insertar", cadenaActual, cadenaActual);
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
	
	public void IngresarDepartamentoAutorizacion(){
		super.persist();
	}

	public void ActualizarDepartamentoAutorizacion(){
		super.update();
	}
	
	public void EliminarDepartamentoAutorizacion(){
		super.remove();
	}
		
	@Transactional
	@Override
	public String update(){
		String mensajeError = validadorPersonalAutorizaUpdate();
		String updated = null;		
		try {
			
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Departamento_Autorización", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Departamento_Autorización", "Eliminar", cadenaAnterior, cadenaActual);
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
