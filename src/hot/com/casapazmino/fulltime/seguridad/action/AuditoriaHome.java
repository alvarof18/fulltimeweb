package com.casapazmino.fulltime.seguridad.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.comun.Comunes;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.seguridad.model.Auditoria;
import com.casapazmino.fulltime.seguridad.model.Usuarios;

@Name("auditoriaHome")
public class AuditoriaHome extends EntityHome<Auditoria> {

	private static final long serialVersionUID = 7058010782937776607L;
	
	@In(create = true)
	UsuariosHome usuariosHome;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setAuditoriaAudiId(Integer id) {
		setId(id);
	}

	public Integer getAuditoriaAudiId() {
		return (Integer) getId();
	}

	@Override
	protected Auditoria createInstance() {
		Auditoria auditoria = new Auditoria();
		return auditoria;
	}

	public void wire() {
		getInstance();
		Usuarios usuarios = usuariosHome.getDefinedInstance();
		if (usuarios != null) {
			getInstance().setUsuarios(usuarios);
		}
	}

	public boolean isWired() {
		if (getInstance().getUsuarios() == null)
			return false;
		return true;
	}

	public Auditoria getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}


	@Override
	public String persist(){
		super.persist();
		FacesMessages.instance().clear();
		return "persisted";
	}
	
	@Override
	public String remove(){
		try {
			super.remove();
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return "removed";
	}
	
	public void asignarCampos(String tabla, String operacion, String cadenaAnterior, String cadenaActual){

		Comunes comunes = new Comunes();
		Usuarios usuario = new Usuarios();
		usuario = gestionPermisoVacacion.buscarUsuario();
		
		this.getInstance().setUsuarios(usuario);		
		
		this.getInstance().setFecha(Fechas.fechaActual());
		this.getInstance().setTabla(tabla);
		this.getInstance().setOperacion(operacion);
		this.getInstance().setCadenaAnterior(cadenaAnterior);
		this.getInstance().setCadenaActual(cadenaActual);
		this.getInstance().setIp(comunes.obtenerIp());
	}
}
