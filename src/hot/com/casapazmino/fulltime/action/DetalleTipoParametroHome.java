package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.model.Cargo;
import com.casapazmino.fulltime.model.Contacto;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoTitulo;
import com.casapazmino.fulltime.model.ProgramaVacacion;
import com.casapazmino.fulltime.model.TipoParametro;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("detalleTipoParametroHome")
public class DetalleTipoParametroHome extends EntityHome<DetalleTipoParametro> {

	private static final long serialVersionUID = 1L;
	
	@In
	EntityManager entityManager;
	@In
	private FacesMessages facesMessages;
	@Logger
	Log log;
	
	@In(create = true)
	TipoParametroHome tipoParametroHome;
	@In(create = true)
	TipoParametroList tipoParametroList;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;		
	
	DetalleTipoParametroList parametrosList =(DetalleTipoParametroList)Component.getInstance("detalleTipoParametroList",true);

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setDetalleTipoParametroDtpaId(Long id) {
		setId(id);
	}

	public Long getDetalleTipoParametroDtpaId() {
		return (Long) getId();
	}

	@Override
	protected DetalleTipoParametro createInstance() {
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();
		return detalleTipoParametro;
	}

	public void wire() {
		getInstance();

		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		TipoParametro tipoParametro = tipoParametroHome.getDefinedInstance();
		if (tipoParametro != null) {
			getInstance().setTipoParametro(tipoParametro);
		}
	}

	public boolean isWired() {
		return true;
	}
	
	public List<DetalleTipoParametro> getListaParametros(Long parametro)
	{
		log.info("ENTRO EN GET LISTA PARAMETROS PARAMETRO:"+parametro);
		try
		{
			this.tipoParametroList.getTipoParametro().setTipaId(parametro);
			TipoParametro tipoParametro=new TipoParametro();
			tipoParametro=tipoParametroList.getSingleResult();
			this.parametrosList.getDetalleTipoParametro().setTipoParametro(tipoParametro);
			return parametrosList.getResultList();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return new ArrayList<DetalleTipoParametro>();
	}
	
	public String validarDetalleTipoParametro(){
		String mensajeError = "Ninguno";

		mensajeError = this.ValidarDetalleTipoParametroDescripcion();
		
		return mensajeError;
	}
	
	public String ValidarDetalleTipoParametroDescripcion(){
		List<DetalleTipoParametro> detalleTipoParametros = this.buscarDetalleTipoParametroDescripcion();
		detalleTipoParametros.remove(this.getInstance());
		if (detalleTipoParametros.size() != 0) {
			return "Detalle Tipo Parametro ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<DetalleTipoParametro> buscarDetalleTipoParametroDescripcion() {
		
		String detalleTipoParametroDescripcion = this.getInstance().getDescripcion();
		
		return (List<DetalleTipoParametro>) entityManager
				.createQuery(
						"select dtp from DetalleTipoParametro dtp where dtp.descripcion = (:detalleTipoParametroDescripcion)")
				.setParameter("detalleTipoParametroDescripcion", detalleTipoParametroDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
//			mensajeError = this.validarDetalleTipoParametro();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("DetalleTipoParametro", "Insertar", cadenaActual, cadenaActual);
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
//			mensajeError = this.validarDetalleTipoParametro();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
//				if (updated.equals("updated")) {
//					this.setCadenaActual(this.getInstance().toString());
//					auditoriaHome.asignarCampos("DetalleTipoParametro", "Modificar", cadenaAnterior, cadenaActual);
//					auditoriaHome.persist();
//					facesMessages.add("#{messages['mensaje.actualizar']}");
//				}
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
				auditoriaHome.asignarCampos("DetalleTipoParametro", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}
	
	/**
	 * GET Y SET
	 * @return
	 */

	public DetalleTipoParametro getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Cargo> getCargos() {
		return getInstance() == null ? null : new ArrayList<Cargo>(
				getInstance().getCargos());
	}
	public List<Contacto> getContactos() {
		return getInstance() == null ? null : new ArrayList<Contacto>(
				getInstance().getContactos());
	}

	public List<EmpleadoTitulo> getEmpleadoTitulos() {
		return getInstance() == null ? null : new ArrayList<EmpleadoTitulo>(
				getInstance().getEmpleadoTitulos());
	}
	public List<Empleado> getEmpleadosForEstado() {
		return getInstance() == null ? null : new ArrayList<Empleado>(
				getInstance().getEmpleadosForEstado());
	}
	public List<Empleado> getEmpleadosForEstadoCivil() {
		return getInstance() == null ? null : new ArrayList<Empleado>(
				getInstance().getEmpleadosForEstadoCivil());
	}
	public List<Empleado> getEmpleadosForSexo() {
		return getInstance() == null ? null : new ArrayList<Empleado>(
				getInstance().getEmpleadosForSexo());
	}
	
//	public List<Permiso> getPermisos() {
//		return getInstance() == null ? null : new ArrayList<Permiso>(
//				getInstance().getPermisos());
//	}
	
	public List<ProgramaVacacion> getProgramaVacacions() {
		return getInstance() == null ? null : new ArrayList<ProgramaVacacion>(
				getInstance().getProgramaVacacions());
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
