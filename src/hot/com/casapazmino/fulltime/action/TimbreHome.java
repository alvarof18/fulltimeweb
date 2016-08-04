package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Timbre;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("timbreHome")
public class TimbreHome extends EntityHome<Timbre> {

	private static final long serialVersionUID = 728087942896758344L;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	@In(create = true)
	TimbreList timbreList;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	private String cadenaAnterior;
	private String cadenaActual;
	
	@Logger
	Log log;
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setTimbreTimbId(Integer id) {
		setId(id);
	}

	public Integer getTimbreTimbId() {
		return (Integer) getId();
	}

	@Override
	protected Timbre createInstance() {
		Timbre timbre = new Timbre();
		return timbre;
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

	public Timbre getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public void crearListaCargarTimbre(List<Empleado> empleados, List<Calendar> fechas, List<DetalleHorario> detalleHorarios, String estado) {
		
		Calendar fechaCalendar = Calendar.getInstance();
		
		// Recorrer lista de empleados
		for (Empleado e : empleados) {
		
			// Recorrer lista de Fechas Seleccionadas
			Iterator it = fechas.iterator();
		
			while (it.hasNext()) {
		
				fechaCalendar = (Calendar) it.next();
		
//				MODIFICADO PARA MSSQL
//				COLOCA HORAS, MINUTOS Y SEGUNDOS EN CERO
//				SINO NO REALIZA LA BUSQUEDA
				Date fechaTimbre = null;
		
				fechaTimbre = Fechas.cambiarFormatoFecha(fechaCalendar.getTime(), "yyyy-MM-dd");
		
//				FIN MODIFICADO PARA MSSQL				
				
				timbreList.getTimbre().setCodigoEmpleado(e.getCodigoEmpleado());
		
				timbreList.getTimbre().setFechaHoraTimbre(fechaTimbre);
		

				List<Timbre> timbres = new ArrayList<Timbre>();
				
				timbres = timbreList.getListaTimbres();
				
				
				this.insertarTimbres(timbres);
				
				this.eliminarTimbres(timbres);				
				
			}
		}
	}
	
	public String getPicture(){
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
		detalleTipoParametros = detalleTipoParametroList.getTeclasFuncion();				
		String parametro=detalleTipoParametros.getDescripcion();
		parametro=parametro.trim();	
		
		int n=Integer.parseInt(parametro);
		
		if(n==6){
			return "/img/seis.jpg";
		}else{
			if(n==3){
				return "/img/tres.jpg";
			}else{
				if(n==0){
					return "/img/cero.jpg";
				}else{
					return "/img/seis.jpg";
				}
			}
		}
		
	}
	
	
//	public String buscarCodigoEmpleado () {
//		
//		this.getInstance().setCodigoEmpleado(this.getInstance().getEmpleado().getCodigoEmpleado());
////		return this.getInstance().getCodigoEmpleado();
//		
//	}
	
	public void RegistrarTimbreEmpleado(){
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
//			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {				
				
				// Graba el codigo del empleado al ingresar un timbre
				this.getInstance().setCodigoEmpleado(empleado.getCodigoEmpleado());
				Date now=new Date();
				this.getInstance().setFechaHoraTimbre(now);
				this.getInstance().setCodigoReloj("999");
				
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Timbre", "Insertar", cadenaActual, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add("#{messages['mensaje.grabar']}");					
				}
				
				Timbre timbre = new Timbre();
				this.setInstance(timbre);
				
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessages.instance().add("Error al crear registro");
		}

	}	

	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
//			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				
				// Graba el codigo del empleado al ingresar un timbre
				this.getInstance().setCodigoEmpleado(this.getInstance().getEmpleado().getCodigoEmpleado());
				
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Timbre", "Insertar", cadenaActual, cadenaActual);
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
	@Transactional
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
//			mensajeError = this.validar;
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Timbre", "Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add("#{messages['mensaje.actualizar']}");
				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
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
				auditoriaHome.asignarCampos("Timbre", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			// Coloca mensajes globalmente
			// Si falla borrar aparecen elmensaje en la otra pantalla
			FacesMessages.instance().add("Error al borrar registro");
			// Coloca mensajes localmente
			// Si falla borrar aparecen elmensaje en la misma pagina, en la otra no sale nada
			//facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error al borra Registro, Contiene empleados", null));
		}
		return removed;
	}
	
	public String eliminarTimbres(List<Timbre> timbres){
		try {
			for (Timbre timbre : timbres) {
				this.setInstance(timbre);

				super.remove();
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error En Proceso de carga");
			e.printStackTrace();
		}
		FacesMessages.instance().clear();
		return "removed";
	}
	
	public String insertarTimbres(List<Timbre> timbres){
		
		try {
			
			for (Timbre timbre : timbres) {
				
				Timbre timbreInsertar = new Timbre();
				
				timbreInsertar.setCodigoEmpleado(timbre.getCodigoEmpleado());
				
				timbreInsertar.setCodigoReloj(timbre.getCodigoReloj());
				
				timbreInsertar.setFechaHoraTimbre(timbre.getFechaHoraTimbre());
				
				timbreInsertar.setAccion(timbre.getAccion());
				
				timbreInsertar.setTeclaFuncion(timbre.getTeclaFuncion());
				
				this.setInstance(timbreInsertar);
				
				super.persist();
				
			}
			
		} catch (Exception e) {
			FacesMessages.instance().add("Error En Proceso de carga");
			e.printStackTrace();
		}

		FacesMessages.instance().clear();
		return "persisted";
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
