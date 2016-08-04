package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.DepartamentoAutorizacion;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("departamentoHome")
public class DepartamentoHome extends EntityHome<Departamento> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Logger
	Log log;
	
	@In(create = true)
	DepartamentoHome departamentoHome;
	
	@In(create = true)
	EmpleadoList empleadoList;
	
	@In(create = true)
	EmpleadoHome empleadoHome;
	
	@In
	EntityManager entityManager;
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	@In(create = true)
	DepartamentoAutorizacionList departamentoAutorizacionList;
	
	@In(create = true)
	DepartamentoAutorizacionHome departamentoAutorizacionHome;
	
	private String cadenaAnterior;
	private String cadenaActual;	

	private Empleado empleadoAux=null;
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setDepartamentoDepaId(Long id) {
		setId(id);
	}

	public Long getDepartamentoDepaId() {
		return (Long) getId();
	}

	@Override
	protected Departamento createInstance() {
		Departamento departamento = new Departamento();
		return departamento;
	}

	public void wire() {
		getInstance();
		
		if(empleadoAux==null){
			empleadoAux=this.getInstance().getEmpleado();
		}
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
	}

	public boolean isWired() {
		return true;
	}

	public Departamento getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validarDepartamento(){
		String mensajeError = "Ninguno";

		mensajeError = this.ValidarDepartamentoDescripcion();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.ValidarCodigo();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String ValidarDepartamentoDescripcion(){
		List<Departamento> departamentos = this.buscarDepartamentoDescripcion();
		departamentos.remove(this.getInstance());
		if (departamentos.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	public String ValidarCodigo(){
		List<Departamento> departamentos = this.buscarCodigo();
		departamentos.remove(this.getInstance());
		if (departamentos.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}
	

	@SuppressWarnings("unchecked")
	public List<Departamento> buscarDepartamentoDescripcion() {
		
		String departamentoDescripcion = this.getInstance().getDescripcion();
		
		return (List<Departamento>) entityManager
				.createQuery(
						"select d from Departamento d where d.descripcion = (:departamentoDescripcion)")
				.setParameter("departamentoDescripcion", departamentoDescripcion)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Departamento> buscarCodigo() {
		
		String codigo = this.getInstance().getCodigo();
		
		return (List<Departamento>) entityManager
				.createQuery(
						"select d from Departamento d where d.codigo = (:codigo)")
				.setParameter("codigo", codigo)
				.getResultList();
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarDepartamento();
			if (mensajeError.equals("Ninguno")) {			
				
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Departamento", "Insertar", cadenaActual, cadenaActual);
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
			mensajeError = this.validarDepartamento();
			if (mensajeError.equals("Ninguno")) {
				
				//
				this.ActualizaJefeEmpleados();
				
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Departamento", "Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					facesMessages.add("#{messages['mensaje.actualizar']}");
				}
			} else {
				facesMessages.add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("#{messages['error.actualizar']}");
		}
		return updated;
	}
	
	private void ActualizaJefeEmpleados(){
		List<Empleado> empleado=new ArrayList<Empleado>();
		empleado=empleadoList.getListaEmpleadosAgrupadoDep(this.instance.getDepaId());
		Empleado jefeNivelActual=this.getInstance().getEmpleado();
		
		for(Empleado e : empleado){
			if(jefeNivelActual!=null){
				if(e.getEmplId()!=jefeNivelActual.getEmplId()){
					empleadoHome.setInstance(e);
					empleadoHome.getInstance().setEmpleado(jefeNivelActual);
					empleadoHome.actualizarEmpleado();
				}	
			}else{
				empleadoHome.setInstance(e);
				empleadoHome.getInstance().setEmpleado(null);
				empleadoHome.actualizarEmpleado();
			}			
		}
		
		Empleado jefeNivelSuperior=null;
		try{
			jefeNivelSuperior=this.instance.getDepartamento().getEmpleado();
		}catch(Exception ex){
		}
		
		if(jefeNivelSuperior!=null){
			empleadoHome.setInstance(jefeNivelActual);
			empleadoHome.getInstance().setEmpleado(jefeNivelSuperior);
			empleadoHome.actualizarEmpleado();
		}else{
			empleadoHome.setInstance(jefeNivelActual);
			empleadoHome.getInstance().setEmpleado(jefeNivelActual);
			empleadoHome.actualizarEmpleado();
		}
		
		if(empleadoAux!=null){
			departamentoAutorizacionList.getDepartamentoAutorizacion().setDepartamento(this.instance);
			departamentoAutorizacionList.getDepartamentoAutorizacion().setEmpleado(empleadoAux);
			List <DepartamentoAutorizacion> listaDepAut = new ArrayList<DepartamentoAutorizacion>();		
			listaDepAut=departamentoAutorizacionList.getListaDepartamentosAutorizacion();
			
			if(listaDepAut.size()>0){
				for (DepartamentoAutorizacion da:listaDepAut){
					if(jefeNivelActual!=null){
						departamentoAutorizacionHome.setInstance(da);
						departamentoAutorizacionHome.getInstance().setEmpleado(jefeNivelActual);
						departamentoAutorizacionHome.ActualizarDepartamentoAutorizacion();
					}else{
						departamentoAutorizacionHome.setInstance(da);
						departamentoAutorizacionHome.EliminarDepartamentoAutorizacion();
					}						
				}
			}else{
				if(jefeNivelActual!=null){
					DepartamentoAutorizacion departamentoAutorizacion=new DepartamentoAutorizacion();
					departamentoAutorizacion.setDepartamento(this.instance);
					departamentoAutorizacion.setEmpleado(jefeNivelActual);
					departamentoAutorizacionHome.setInstance(departamentoAutorizacion);
					departamentoAutorizacionHome.IngresarDepartamentoAutorizacion();
				}
			}		
		}else{
			if(jefeNivelActual!=null){
				DepartamentoAutorizacion departamentoAutorizacion=new DepartamentoAutorizacion();
				departamentoAutorizacion.setDepartamento(this.instance);
				departamentoAutorizacion.setEmpleado(jefeNivelActual);
				departamentoAutorizacionHome.setInstance(departamentoAutorizacion);
				departamentoAutorizacionHome.IngresarDepartamentoAutorizacion();
			}
		}
		
		empleadoAux=null;		
	}

	@Override
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("Departamento", "Eliminar", cadenaAnterior, cadenaActual);
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

	public List<Departamento> getDepartamentos() {
		return getInstance() == null ? null : new ArrayList<Departamento>(
				getInstance().getDepartamentos());
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
	
	public List<DepartamentoAutorizacion> getDepartamentoAutorizacion() {
		return getInstance() == null ? null : new ArrayList<DepartamentoAutorizacion>(
				getInstance().getDepartamentoAutorizacion());
	}

}
