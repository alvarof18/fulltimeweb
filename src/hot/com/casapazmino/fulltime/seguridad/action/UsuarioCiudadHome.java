package com.casapazmino.fulltime.seguridad.action;

import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.action.CiudadHome;
import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.seguridad.model.UsuarioCiudad;
import com.casapazmino.fulltime.seguridad.model.Usuarios;

@Name("usuarioCiudadHome")
public class UsuarioCiudadHome extends EntityHome<UsuarioCiudad> {

	private static final long serialVersionUID = 8795603211339395361L;
	
	@In(create = true)
	CiudadHome ciudadHome;
	/*	@In(create = true)
	ProvinciaHome provinciaHome;
	@In(create = true)
	RegionHome regionHome;*/
	@In(create = true)
	UsuariosHome usuariosHome;
	
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
		

	public void setUsuarioCiudadUsuaCiudId(Integer id) {
		setId(id);
	}

	public Integer getUsuarioCiudadUsuaCiudId() {
		return (Integer) getId();
	}

	@Override
	protected UsuarioCiudad createInstance() {
		UsuarioCiudad usuarioCiudad = new UsuarioCiudad();
		return usuarioCiudad;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		Ciudad ciudad = ciudadHome.getDefinedInstance();
		if (ciudad != null) {
			getInstance().setCiudad(ciudad);
		}
/*		Provincia provincia = provinciaHome.getDefinedInstance();
		if (provincia != null) {
			getInstance().setProvincia(provincia);
		}
		Region region = regionHome.getDefinedInstance();
		if (region != null) {
			getInstance().setRegion(region);
		}*/
		Usuarios usuarios = usuariosHome.getDefinedInstance();
		if (usuarios != null) {
			getInstance().setUsuarios(usuarios);
		}
	}

	public boolean isWired() {
/*		if (getInstance().getCiudad() == null)
			return false;
		if (getInstance().getUsuarios() == null)
			return false;
*/		return true;
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
		List<UsuarioCiudad> usuarioCiudades = this.buscarEntidad();
		usuarioCiudades.remove(this.getInstance());
		if (usuarioCiudades.size() != 0) {
			return "Seleccion ya existe";
		}
		return "Ninguno";
	}
	
	public List<UsuarioCiudad> buscarEntidad() {
		UsuarioCiudadList usuarioCiudadList = (UsuarioCiudadList) Component.getInstance("usuarioCiudadList",true);
		
		usuarioCiudadList.getUsuarioCiudad().getUsuarios().setId(this.getInstance().getUsuarios().getId());
		usuarioCiudadList.getUsuarioCiudad().getCiudad().setCiudId(this.getInstance().getCiudad().getCiudId());
		
		List<UsuarioCiudad> usuarioCiudades = usuarioCiudadList.getResultList();
		return usuarioCiudades;
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
					auditoriaHome.asignarCampos("UsuarioCiudad", "Insertar", cadenaActual, cadenaActual);
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
					auditoriaHome.asignarCampos("UsuarioCiudad", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("UsuarioCiudad", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public UsuarioCiudad getDefinedInstance() {
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
