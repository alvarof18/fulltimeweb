package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.anotaciones.Trackable;
import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Provincia;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

//@Inter // Anotacion para Interceptor
@Trackable
@Name("ciudadHome")
public class CiudadHome extends EntityHome<Ciudad>{

	private static final long serialVersionUID = 1L;
	@In
	private FacesMessages facesMessages;
	@In
	EntityManager entityManager;
	
	@Logger
	Log log;
	
	@In(create = true)
	ProvinciaHome provinciaHome;
	
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

	public void setCiudadCiudId(Long id) {
		setId(id);
	}

	public Long getCiudadCiudId() {
		return (Long) getId();
	}

	@Override
	protected Ciudad createInstance() {
		Ciudad ciudad = new Ciudad();
		return ciudad;
	}

	public void wire() {
		getInstance();
				
		Provincia provincia = provinciaHome.getDefinedInstance();
		if (provincia != null) {
			getInstance().setProvincia(provincia);
		}
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 

	}

	public boolean isWired() {
//		if (getInstance().getProvincia() == null)
//			return false;
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public List<Ciudad> listaCiudadXProvincia(Long prov)
	{
		try
		{
			log.info("ENTRO EN LISTA CIUDAD POR PROVINCIA");
			log.info("VALOR DE PROVINCIA ID: "+prov);
			String consulta="SELECT ciuda FROM Ciudad ciuda " +
					"WHERE ciuda.provincia.provId=?1";
			Query query=this.getEntityManager().createQuery(consulta);
			query.setParameter(1,prov);
			return query.getResultList();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return new ArrayList<Ciudad>();
	}
	
	public String validarCiudad(){
		String mensajeError = "Ninguno";

		
		mensajeError = this.validarCiudadDescripcion();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarCodigo();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarCiudadDescripcion(){
		List<Ciudad> ciudades = this.buscarCiudadDescripcion();
		ciudades.remove(this.getInstance());
		if (ciudades.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	public String validarCodigo(){
		List<Ciudad> ciudades = this.buscarCodigo();
		ciudades.remove(this.getInstance());
		if (ciudades.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Ciudad> buscarCiudadDescripcion() {
		
		String ciudadDescripcion = this.getInstance().getDescripcion();
		
		return (List<Ciudad>) entityManager
				.createQuery(
						"select c from Ciudad c where c.descripcion = (:ciudadDescripcion)")
				.setParameter("ciudadDescripcion", ciudadDescripcion)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ciudad> buscarCodigo() {
		
		String codigo = this.getInstance().getCodigo();
		
		return (List<Ciudad>) entityManager
				.createQuery(
						"select c from Ciudad c where c.codigo = (:codigo)")
				.setParameter("codigo", codigo)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarCiudad();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Ciudad", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarCiudad();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Ciudad", "Modificar", cadenaAnterior, cadenaActual);
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


//	REGISTRO DE AUDITORIA DESACTIVADO
//	SE USA SOLAMANTE PARA LA TABLA TIMBRE
//	@Override
//	public String persist(){
//		super.persist();
//		this.setCadenaActual(this.getInstance().toString());
//		auditoriaHome.asignarCampos("Ciudad", "Persist", cadenaAnterior, cadenaActual);
//		auditoriaHome.persist();
//		return "persisted";
//	}
//	
//	@Override
//	@Transactional
//	public String update(){
//		
//		
//		this.setCadenaActual(this.getInstance().toString());
//		auditoriaHome.asignarCampos("Ciudad", "Update", cadenaAnterior, cadenaActual);
//		auditoriaHome.persist();
//		
//		return super.update();
//	}
//	
//	@Override
//	public String remove(){
//		try {
//			this.setCadenaActual(this.getInstance().toString());
//			auditoriaHome.asignarCampos("Ciudad", "Remove", cadenaAnterior, cadenaActual);
//			auditoriaHome.persist();
//			super.remove();
//		} catch (Exception e) {
//			// Coloca mensajes globalmente
//			// Si falla borrar aparecen elmensaje en la otra pantalla
//			FacesMessages.instance().add("Error al borrar registro");
//			
//			// Coloca mensajes localmente
//			// Si falla borrar aparecen elmensaje en la misma pagina, en la otra no sale nada
//			//facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al borra Registro, Contiene empleados", null));
//		}
//		return "removed";
//	}
	
	@Override
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("Ciudad", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	
	/**
	 * GETERS AND SETERS
	 * @return
	 */
	public Ciudad getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<CiudadFeriado> getCiudadFeriados() {
		return getInstance() == null ? null : new ArrayList<CiudadFeriado>(
				getInstance().getCiudadFeriados());
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
