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

import com.casapazmino.fulltime.model.AsistenciaHoraExtra;
import com.casapazmino.fulltime.model.HoraExtra;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("horaExtraHome")
public class HoraExtraHome extends EntityHome<HoraExtra> {

	private static final long serialVersionUID = 2984084997468165467L;

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
	
	public void setHoraExtraHoraExtraId(Integer id) {
		setId(id);
	}

	public Integer getHoraExtraHoraExtraId() {
		return (Integer) getId();
	}

	@Override
	protected HoraExtra createInstance() {
		HoraExtra horaExtra = new HoraExtra();
		return horaExtra;
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

	public HoraExtra getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarHoraExtra(){
		String mensajeError = "Ninguno";
	
		mensajeError = this.validarHoraExtraDescripcion();
		
		return mensajeError;
	}
	
	public String validarHoraExtraDescripcion(){
//		List<HoraExtra> horaExtras = this.buscarHoraExtraDescripcion();
//		horaExtras.remove(this.getInstance());
//		if (horaExtras.size() != 0) {
//			return "Hora Extra ya existe";
//		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
//	public List<HoraExtra> buscarHoraExtraDescripcion() {
		
//		String horaExtraDescripcion = this.getInstance().getDescripcion();
		
//		return (List<HoraExtra>) entityManager
//				.createQuery(
//						"select he from HoraExtra he where he.descripcion = (:horaExtraDescripcion)")
//				.setParameter("horaExtraDescripcion", horaExtraDescripcion)
//				.getResultList();
//	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarHoraExtra();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("HoraExtra", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarHoraExtra();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("HoraExtra", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("HoraExtra", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

//	Implementacion automatica para el codigo del dia de la semana
//	public int codigoDia() {
//		
//		int dia = 0;
//		if (ControlBaseDatos.baseDatos.equals(Constantes.MYSQL)){
//
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.SQLSERVER)){
//		
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.ORACLE)){
//			if (this.getInstance().getDescripcion() == Enumeraciones.diasSemana.valueOf("Domingo")) {
//				this.getInstance().setCodigoDia("7");
//			} else if (this.getInstance().getDescripcion() == Enumeraciones.diasSemana.valueOf("Lunes")) {
//				this.getInstance().setCodigoDia("7");
//				}
//			
//		} else if (ControlBaseDatos.baseDatos.equals(Constantes.POSTGRES)){
//		
//		}
//		
//		return dia;
//	}

	public List<AsistenciaHoraExtra> getAsistenciaHoraExtras() {
		return getInstance() == null
				? null
				: new ArrayList<AsistenciaHoraExtra>(getInstance()
						.getAsistenciaHoraExtras());
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
