package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.HistLabo;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("histLaboHome")
public class HistLaboHome extends EntityHome<HistLabo> {

	private static final long serialVersionUID = 4546523394052680366L;
	
	@In(create = true)
	EmpleadoHome empleadoHome;
	
	@In(create = true)
	HistLaboList histLaboList;

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

	public void setHistLaboHilaId(Integer id) {
		setId(id);
	}

	public Integer getHistLaboHilaId() {
		return (Integer) getId();
	}

	@Override
	protected HistLabo createInstance() {
		HistLabo histLabo = new HistLabo();
		return histLabo;
	}

	public void wire() {
		getInstance();
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}
		
		if (!this.isManaged()){
			setCadenaAnterior(this.getInstance().toString());
			this.instance.setEstado(true);
		} else {
			setCadenaAnterior("");
		}
	}

	public boolean isWired() {
		if (getInstance().getEmpleado() == null)
			return false;
		return true;
	}

	public HistLabo getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<EmpleadoPeriodoVacacion> getEmpleadoPeriodoVacacions() {
		return getInstance() == null
				? null
				: new ArrayList<EmpleadoPeriodoVacacion>(getInstance()
						.getEmpleadoPeriodoVacacions());
	}
	
	public String validarHistLabo(){
		
		String mensajeError = "Ninguno";
		
		int contadorActivos = 0;
		
		if (this.getInstance().getEstado() == true){
			contadorActivos++;
		}
		
		if (this.getInstance().getFechaSalida() != null){
			if (this.getInstance().getFechaIngreso().after(this.getInstance().getFechaSalida())) {
				mensajeError = "Fecha Ingreso mayor que fecha de salida";
				return mensajeError;
			}
		}
		
		if (this.getInstance().getEstado() == false && this.getInstance().getFechaSalida() == null) {
			mensajeError = "Ingrese fecha de salida";
			return mensajeError;			
		}


		//Validacion suspendida temporalmente
//		Set<HistLabo> histLabos = new HashSet<HistLabo>(0);
//		histLabos = this.getInstance().getEmpleado().getHistLabos();
//		
//		// INICIO
//		// SE BORRA LA INSTANCIA ACTUAL PARA PODER REALIZAR LAS VALIDACIONES AL ACTUALIZAR UN REGISTRO
//		// CASO CONTRARIO VALIDA CONTRA LA MISMA INSTANCIA POR LO TANTO NO ACTUALIZA
//		if (this.isManaged()){
//			histLabos.remove(this.getInstance());
//		}
//		// FIN
//
//		for (HistLabo histLabo : histLabos) {
//												
//			if (histLabo.getEstado() == false){
//								
//				if ( this.getInstance().getFechaIngreso().after(histLabo.getFechaIngreso()) && 
//						this.getInstance().getFechaIngreso().before(histLabo.getFechaSalida())  || 
//						this.getInstance().getFechaIngreso().equals(histLabo.getFechaIngreso()) || 
//								this.getInstance().getFechaIngreso().equals(histLabo.getFechaSalida()) ) {
//								mensajeError = "Fecha Ingreso registrada en otro periodo";
//								return mensajeError;
//						}
//				
//				if ( this.getInstance().getFechaSalida().after(histLabo.getFechaIngreso()) && 
//						this.getInstance().getFechaSalida().before(histLabo.getFechaSalida()) ||
//						this.getInstance().getFechaSalida().equals(histLabo.getFechaIngreso()) ||
//						this.getInstance().getFechaSalida().equals(histLabo.getFechaSalida()) ) {
//					mensajeError = "Fecha salida registrada en otro periodo";
//					return mensajeError;
//				}
//				
//				if (histLabo.getFechaIngreso().after(this.getInstance().getFechaIngreso()) && histLabo.getFechaIngreso().before(this.getInstance().getFechaSalida()) ){
//					mensajeError = "Fecha Ingreso o fecha salida registrada en otro periodo";
//					return mensajeError;
//
//				}
//				
//				if (histLabo.getFechaSalida().after(this.getInstance().getFechaIngreso()) && histLabo.getFechaSalida().before(this.getInstance().getFechaSalida()) ){
//					mensajeError = "Fecha Ingreso o fecha salida registrada en otro periodo";
//					return mensajeError;
//				}
//				
//			} else {
//				
//				// ESTE ES EL PERIODO ACTIVO
//				contadorActivos++;
//				
//				if ( this.getInstance().getFechaIngreso().after(histLabo.getFechaIngreso()) || this.getInstance().getFechaSalida().after(histLabo.getFechaIngreso())) {
//					mensajeError = "Fecha Ingreso o fecha salida supera periodo activo";
//					return mensajeError;
//				}					 
//			} 
//		}
				
		if (contadorActivos > 1) {
			mensajeError = "Ya existe un periodo activo";
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validar(){
		String mensajeError = "Ninguno";
		
		mensajeError = this.validarHistLabo();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
				
		return mensajeError;
	}
	
	
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
					auditoriaHome.asignarCampos("HistoriaLaboral", "Insertar", cadenaActual, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add("#{messages['mensaje.grabar']}");					
				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessages.instance().add("Error al crear registro");
		}
		return persisted;
	}

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
					auditoriaHome.asignarCampos("HistoriaLaboral", "Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add("#{messages['mensaje.actualizar']}");					
				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessages.instance().add("#{messages['error.actualizar']}");
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
				auditoriaHome.asignarCampos("HistoriaLaboral", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			e.printStackTrace();
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
