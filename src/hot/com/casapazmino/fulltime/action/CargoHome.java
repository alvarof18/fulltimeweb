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

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Cargo;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.TipoCargo;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("cargoHome")
public class CargoHome extends EntityHome<Cargo> {

	private static final long serialVersionUID = 1L;
		
	@In
	EntityManager entityManager;
	@In
	private FacesMessages facesMessages;
	@In(create = true)
	TipoCargoHome tipoCargoHome;
	@In(create = true)
	DetalleTipoParametroHome detalleTipoParametroHome;
	
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
	
	public void setCargoCargId(Long id) {
		setId(id);
	}

	public Long getCargoCargId() {
		return (Long) getId();
	}

	@Override
	protected Cargo createInstance() {
		Cargo cargo = new Cargo();
		return cargo;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		
		TipoCargo tipoCargo = tipoCargoHome.getDefinedInstance();
		if (tipoCargo != null) {
			getInstance().setTipoCargo(tipoCargo);
		}
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroHome
				.getDefinedInstance();
		if (detalleTipoParametro != null) {
			getInstance().setDetalleTipoParametro(detalleTipoParametro);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Cargo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarCargo(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarCargoDescripcion();
		
		return mensajeError;
	}
	
	public String validarCargoDescripcion(){
		List<Cargo> cargos = this.buscarCargoDescripcion();
		cargos.remove(this.getInstance());
		if (cargos.size() != 0) {
			return "Cargo ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Cargo> buscarCargoDescripcion() {
		
		String cargoDescripcion = this.getInstance().getDescripcion();
		
		return (List<Cargo>) entityManager
				.createQuery(
						"select c from Cargo c where c.descripcion = (:cargoDescripcion)")
				.setParameter("cargoDescripcion", cargoDescripcion)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarCargo();
			if (mensajeError.equals("Ninguno")) {
				
				// Coloca siempre no en campo Horas_Extras
				this.detalleTipoParametroHome.setId(Constantes.DECISION_NO);
				DetalleTipoParametro  detalleTipoParametro = this.detalleTipoParametroHome.find();
				this.getInstance().setDetalleTipoParametro(detalleTipoParametro);
//				this.getInstance().setSueldo(new BigDecimal(0));
				
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Cargo", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarCargo();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Cargo", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("Cargo", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
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
