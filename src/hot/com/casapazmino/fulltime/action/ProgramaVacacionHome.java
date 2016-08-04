package com.casapazmino.fulltime.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.ProgramaVacacion;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("programaVacacionHome")
public class ProgramaVacacionHome extends EntityHome<ProgramaVacacion> {

	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = 1L;
	@In(create = true)
	EmpleadoHome empleadoHome;

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

	public void setProgramaVacacionPrvaId(Long id) {
		setId(id);
	}

	public Long getProgramaVacacionPrvaId() {
		return (Long) getId();
	}

	@Override
	protected ProgramaVacacion createInstance() {
		ProgramaVacacion programaVacacion = new ProgramaVacacion();
		return programaVacacion;
	}

	public void wire() {
		getInstance();
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ProgramaVacacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String RetornarCadena(ProgramaVacacion programaVacacion){
		String cadena="";
		
		ProgramaVacacion p = programaVacacion;
		
		Empleado e=p.getEmpleado();
		int a=p.getGenerado();
		
		String estado="";
		
		if(a==0){
			estado="NO";
		}else{
			estado="SI";
		}
		
		cadena="PrvaId="+p.getPrvaId()+", codigoEmpleado="+e.getCodigoEmpleado()+
				", periodo="+p.getPeriodo()+", autorizado="+estado+
				", fechaInicio="+p.getFechaInicio()	+", fechaFin="+p.getFechaFin()+
				", fechaInicio1="+p.getFechaInicio1()+", fechaFin1="+p.getFechaFin1()+
				", fechaInicio2="+p.getFechaInicio2()+", fechaFin2="+p.getFechaFin2()+
				", fechaInicio3="+p.getFechaInicio3()+", fechaFin3="+p.getFechaFin3();
		
		return cadena;
	}
	
	@Override
	public String persist() {
		
		String persisted= super.persist();
		
		this.setCadenaAnterior("");
		this.setCadenaActual(RetornarCadena(this.getInstance()));
		
		auditoriaHome.clearInstance();
		auditoriaHome.asignarCampos("Programación de Vacaciones", "Insertar", cadenaAnterior, cadenaActual);
		auditoriaHome.persist();
			
		return persisted;
	}
	
	@Override
	public String update() {
		
		String updated=super.update();
		System.out.println("update " + updated);
		
		this.setCadenaActual(RetornarCadena(this.getInstance()));
		
		auditoriaHome.clearInstance();
		auditoriaHome.asignarCampos("Programación Vacaciones", "Modificar", cadenaAnterior, cadenaActual);
		auditoriaHome.persist();
		
		return updated;
	}
	
	@Override
	public String remove() {
		
		String removed=super.remove();
		
		this.setCadenaAnterior(RetornarCadena(this.getInstance()));
		this.setCadenaActual(RetornarCadena(this.getInstance()));
		
		auditoriaHome.clearInstance();
		auditoriaHome.asignarCampos("Programación Vacaciones", "Eliminar", cadenaAnterior, cadenaActual);
		auditoriaHome.persist();
		
		return removed;
	}
	
	/*public void ObtenerDatosCadenaAnterior(){
		this.setCadenaAnterior(RetornarCadena(this.getInstance()));
	}*/

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
