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

import com.casapazmino.fulltime.model.DetalleTipoPermiso;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.TipoPermiso;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("tipoPermisoHome")
public class TipoPermisoHome extends EntityHome<TipoPermiso> {

	private static final long serialVersionUID = -8530207336733848086L;
	
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

	public void setTipoPermisoTipeId(Long id) {
		setId(id);
	}

	public Long getTipoPermisoTipeId() {
		return (Long) getId();
	}

	@Override
	protected TipoPermiso createInstance() {
		TipoPermiso tipoPermiso = new TipoPermiso();
		return tipoPermiso;
	}
	
	public List<DetalleTipoPermiso> getDetalleTipoPermiso() {
		return getInstance() == null ? null : new ArrayList<DetalleTipoPermiso>(
				getInstance().getDetalleTipoPermiso());
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

	public TipoPermiso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarTipoPermiso(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarTipoPermisoDescripcion();
		
		return mensajeError;
	}
	
	public String validarTipoPermisoDescripcion(){
		List<TipoPermiso> tipoPermisos = this.buscarTipoPermisoDescripcion();
		tipoPermisos.remove(this.getInstance());
		if (tipoPermisos.size() != 0) {
			return "Tipo Permiso ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<TipoPermiso> buscarTipoPermisoDescripcion() {
		
		String tipoPermisoDescripcion = this.getInstance().getDescripcion();
		
		return (List<TipoPermiso>) entityManager
				.createQuery(
						"select tp from TipoPermiso tp where tp.descripcion = (:tipoPermisoDescripcion)")
				.setParameter("tipoPermisoDescripcion", tipoPermisoDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarTipoPermiso();
			if (mensajeError.equals("Ninguno")) {
				
				this.getInstance().setTimbra(true);
				this.getInstance().setCorreo(false);
				this.getInstance().setFactorHoras(new Double(1));
				
				IngresarDatosCorreos();
				
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("TipoPermiso", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarTipoPermiso();
			if (mensajeError.equals("Ninguno")) {
				IngresarDatosCorreos();
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("TipoPermiso", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("TipoPermiso", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");									
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public List<Permiso> getPermisos() {
		return getInstance() == null ? null : new ArrayList<Permiso>(
				getInstance().getPermisos());
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

	private List<GestionCorreo> listaGestionCorreo;
	
	public List<GestionCorreo> getListaGestionaCorreo(){	
		
		if(listaGestionCorreo==null)
			listaGestionCorreo=new ArrayList<GestionCorreo>();
		
		listaGestionCorreo.clear();
		
		GestionCorreo gestionCorreo = new GestionCorreo();
		
		gestionCorreo.setCrear(this.instance.getCrear());
		gestionCorreo.setActualizar(this.instance.getActualizar());
		gestionCorreo.setEliminar(this.instance.getEliminar());
		gestionCorreo.setPreAutorizar(this.instance.getPreautorizar());
		gestionCorreo.setNoAutorizar(this.instance.getNoautorizar());
		gestionCorreo.setAutorizar(this.instance.getAutorizar());
		gestionCorreo.setLegalizar(this.instance.getLegalizar());
		
		listaGestionCorreo.add(gestionCorreo);
		
		return listaGestionCorreo;
	}
	
	private void IngresarDatosCorreos(){
		for(GestionCorreo g:listaGestionCorreo){
			this.instance.setCrear(g.getCrear());
			this.instance.setActualizar(g.getActualizar());
			this.instance.setEliminar(g.getEliminar());
			this.instance.setPreautorizar(g.getPreAutorizar());
			this.instance.setAutorizar(g.getAutorizar());
			this.instance.setNoautorizar(g.getNoAutorizar());
			this.instance.setLegalizar(g.getLegalizar());
		}
	}
	
}
