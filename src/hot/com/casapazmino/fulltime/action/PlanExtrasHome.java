package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.security.Credentials;

import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Funciones;
import com.casapazmino.fulltime.model.PlanExtras;

@Name("planExtrasHome")
public class PlanExtrasHome extends EntityHome<PlanExtras> {

	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	private String tipoReporte;
	
	@In
	EntityManager entityManager;
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	Credentials credentials;
	
	private boolean autorizaPermiso;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}
	
	public void setPlanExtraPaexId(Long id) {
		setId(id);
	}

	public Long getPlanExtraPaexId() {
		return (Long) getId();
	}

	@Override
	protected PlanExtras createInstance() {
		PlanExtras planExtra = new PlanExtras();
		return planExtra;
	}

	public void wire() {
		getInstance();
		this.tipoReporte="PDF";
		this.setAutorizaPermiso(gestionPermisoVacacion.buscarAutorizaPermiso());		
		/*String u = credentials.getUsername();
		//System.out.println("USER......................................"+u);
		EmpleadoList empleadolist = (EmpleadoList) Component.getInstance("empleadoList", true);
		List<Empleado> empleado = new ArrayList<Empleado>();
		empleado=empleadolist.getResultList();
		for(Empleado em : empleado){
			//System.out.println("EMPL_USER......................................"+em.getUsuarios().getUsuario());
			if(em.getUsuarios().getUsuario().equals(u)){				
				this.getInstance().setEmpleado(em);
				break;
			}
		}		*/
		//this.getInstance().setTotal_horas_laborales(160);			
	}

	public boolean isWired() {
		return true;
	}

	public PlanExtras getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}	

	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		try {
			mensajeError=validaMes();
			//System.out.println("MESSAGE-------"+mensajeError);
			if (mensajeError.equals("Ninguno")) {
				this.instance.setAutorizado(4);
				return super.persist();				
			} else {				
				facesMessages.add(mensajeError);
				return null;
			}
			
		} catch (Exception e) {			
			e.printStackTrace();
			facesMessages.add("Error al crear registro");
		}
		return "persisted";
	}

	@Transactional
	@Override
	public String update(){
		String mensajeError = "Ninguno";
		try {		
			mensajeError=validaMesupadte();
			if (mensajeError.equals("Ninguno")) {
				return super.update();				
			} else {
				facesMessages.add(mensajeError);
				return null;
			}

		} catch (Exception e) {
			facesMessages.add("#{messages['error.actualizar']}");
		}
		return "updated";
	}


	@Override
	public String remove(){
		try {
			super.remove();
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return "removed";
	}	
	
	/*public void valueChangedEventPeriodo(ValueChangeEvent event){	
		DetalleGrupoContratado h = ( (DetalleGrupoContratado) event.getNewValue());	
		String au=""+h.getDescripcion();
		//System.out.println("EVENT-----------------------------------------------------"+h.getDescripcion());
		if(au.equals("LEY DE EMPRESAS PUBLICAS")){		
			this.getInstance().setTotal_horas_extraordinarias(60);
			this.getInstance().setTotal_horas_suplementarias(60);		
		}else if (au.equals("Codigo del trabajo")){				
			this.getInstance().setTotal_horas_extraordinarias(48);
			this.getInstance().setTotal_horas_suplementarias(48);
			
		}
	}*/
	
	public List<Funciones> getFunciones() {
		return getInstance() == null ? null : new ArrayList<Funciones>(
				getInstance().getFunciones());
	}
	
	public String validaMes(){
		String mensaje="Ninguno";
		Integer m=this.getInstance().getMes();
		Integer y=this.getInstance().getAnio();
		if(m>=1&&m<=12){
			PlanExtraList planextrasList = (PlanExtraList) Component.getInstance("planExtrasList", true);
			List<PlanExtras> planextra = new ArrayList<PlanExtras>();
			planextrasList.getPlanExtras().setEmpleado(this.getInstance().getEmpleado());
			planextrasList.setAutorizado(null);
			planextra=planextrasList.getResultList();
			/*System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX m2 :"+m.toString()+":");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX y2 :"+y.toString()+":");*/
			for(PlanExtras p:planextra){
				/*System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX "+p.getEmpleado().getApellido());
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX m1 :"+p.getMes().toString()+":");
				System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX y1 :"+p.getAnio().toString()+":");*/
				
				if(p.getMes().toString().equals(m.toString())&& p.getAnio().toString().equals(y.toString())){
					mensaje="El mes ingresado ya se encuentra registrado en el año "+y;
					break;
				}				
			}
		}else{
			mensaje="Ingrese un valor para mes entre 1 y 12";
		}
		return mensaje;
	}
	
	public String validaMesupadte(){
		String mensaje="Ninguno";
		Integer m=this.getInstance().getMes();
		Integer y=this.getInstance().getAnio();
		Long c=this.getInstance().getPaexId();
		if(m>=1&&m<=12){
			PlanExtraList planextrasList = (PlanExtraList) Component.getInstance("planExtrasList", true);
			List<PlanExtras> planextra = new ArrayList<PlanExtras>();
			planextrasList.getPlanExtras().setEmpleado(this.getInstance().getEmpleado());
			planextra=planextrasList.getResultList();			
			for(PlanExtras p:planextra){
				if(c!=p.getPaexId()){
					if(p.getMes().toString().equals(m.toString())&& p.getAnio().toString().equals(y.toString())){
						mensaje="El mes ingresado ya se encuentra registrado en el año "+y;
						break;
					}	
				}
							
			}
		}else{
			mensaje="Ingrese un valor para mes entre 1 y 12";
		}
		return mensaje;
	}
	
	public String validaAño(){
		String mensaje="";
		return mensaje;
	}
	
	
public void valueChangedEventEmpleado(ValueChangeEvent event){		
		Empleado empleado = ( (Empleado) event.getNewValue());			
		this.getInstance().setEmpleado(empleado);	
		this.getInstance().setTotal_horas_laborales(this.getInstance().getEmpleado().getDetalleGrupoContratado().getHoras_laborables());
		this.getInstance().setTotal_horas_suplementarias(this.getInstance().getEmpleado().getDetalleGrupoContratado().getHoras_suplementarias());
		this.getInstance().setTotal_horas_extraordinarias(this.getInstance().getEmpleado().getDetalleGrupoContratado().getHoras_extraordinarias());
	}


public String getTipoReporte() {
	if (tipoReporte == null){
		tipoReporte = new String();	
	}
	
	return tipoReporte;
}

public void setTipoReporte(String tipoReporte) {
	this.tipoReporte = tipoReporte;
}

public boolean isAutorizaPermiso() {
	return autorizaPermiso;
}

public void setAutorizaPermiso(boolean autorizaPermiso) {
	this.autorizaPermiso = autorizaPermiso;
}

public boolean visualizar_autorizacion(){	

	if(gestionPermisoVacacion.buscarAutorizaPermiso()){
	
		if(this.getInstance().getAutorizado()==4){
			
			Long a=this.getInstance().getPaexId();
			Integer b=this.getInstance().getTotal_horas_laborales();
			
			FuncionesList funcioneslist = (FuncionesList) Component.getInstance("funcionesList", true);
			List<Funciones> funciones = new ArrayList<Funciones>();
			Integer a2=0;
			funcioneslist.getFunciones().setPaex_id(this.getInstance());
			funciones=funcioneslist.getResultList();
			for(Funciones f : funciones){
				//if(f.getPaex_id().getPaexId().toString().equals(a.toString())){				
					a2=a2+f.getHoras_laborales();					
				//}			
			}				
			if(b.toString().equals(a2.toString())){
				return true;
			}else{
				return false;
			}				
		
	}else{
		return false;
	}
	}else{
		return false;
	}
	
		
}

public void autorizar(){
	this.getInstance().setAutorizado(3);
	this.update();
	facesMessages.clear();
	facesMessages.add("Se ha autorizado la planificación de horas extras");	
}

public String no_autorizar(Long b){	
	PlanExtras pe=new PlanExtras();
	pe.setPaexId(b);
	FuncionesList funcioneslist = (FuncionesList) Component.getInstance("funcionesList", true);
	List<Funciones> funciones = new ArrayList<Funciones>();	
	funcioneslist.getFunciones().setPaex_id(pe);
	funciones=funcioneslist.getResultList();
	for(Funciones f : funciones){
		FuncionesHome aux=new FuncionesHome();
		aux.setInstance(f);
		aux.remove();
		facesMessages.clear();
	}	
	this.remove();
	facesMessages.clear();
	facesMessages.add("Se ha descartado la planificación de horas extras");
	return "/fulltime/PlanExtrasList.xhtml" ;
}

}
