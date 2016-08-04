package com.casapazmino.fulltime.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.DetalleTipoPermiso;
import com.casapazmino.fulltime.model.TipoPermiso;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("detalleTipoPermisoHome")
public class DetalleTipoPermisoHome extends EntityHome<DetalleTipoPermiso> {

	private static final long serialVersionUID = 1L;
	
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	TipoPermisoHome tipoPermisoHome;

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

	public void setDetalleTipoPermisoDehoId(Long id) {
		setId(id);
	}

	public Long getDetalleTipoPermisoDehoId() {
		return (Long) getId();
	}

	@Override
	protected DetalleTipoPermiso createInstance() {
		DetalleTipoPermiso detalleTipoPermiso = new DetalleTipoPermiso();
		return detalleTipoPermiso;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		TipoPermiso tipoPermiso = tipoPermisoHome.getDefinedInstance();
		if (tipoPermiso != null) {
			getInstance().setTipoPermiso(tipoPermiso);
		}
	}

	public boolean isWired() {
		return true;
	}

	private String validarDetalleTipoPermiso(){
		String mensaje="Ninguno";
		
		if(this.instance.getInicio()<=0||this.instance.getFin()<=0||this.instance.getNivel()<=0){
			mensaje="No debe ingresar valores negativos o iguales a 0";
		}
		
		if(this.instance.getInicio()>this.instance.getFin()){
			mensaje="Desde no puede ser mayor que hasta";
		}
		
		return mensaje;
	}
		
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarDetalleTipoPermiso();
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
			mensajeError = this.validarDetalleTipoPermiso();
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
