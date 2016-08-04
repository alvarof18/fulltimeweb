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

import com.casapazmino.fulltime.model.Funciones;
import com.casapazmino.fulltime.model.PlanExtras;

@Name("funcionesHome")
public class FuncionesHome extends EntityHome<Funciones>{
	
	
	private static final long serialVersionUID = 1L;

	@Logger
	Log log;
	
	@In
	EntityManager entityManager;	
	
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	PlanExtrasHome planExtrasHome;
	
	private Funciones funciones;
	
	private Integer saldo_laborales;
	private Integer salado_suplementarias;
	private Integer saldo_extraordinarias;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}
	
	public void setFuncionesFuncId(Long id) {
		setId(id);
	}

	public Long getFuncionesFuncId() {
		return (Long) getId();
	}

	@Override
	protected Funciones createInstance() {
		Funciones funciones = new Funciones();
		return funciones;
	}

	public void wire() {
		getInstance();			
		PlanExtras planextras = planExtrasHome.getDefinedInstance();
		if (planextras != null) {
			getInstance().setPaex_id(planextras);
		}
		init_saldos();			
	}

	public boolean isWired() {
		return true;
	}

	public Funciones getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}	
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		try {
			mensajeError=validar_saldos();
			if (mensajeError.equals("Ninguno")) {				
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
		set_saldos();
		String mensajeError = "Ninguno";
		try {		
			mensajeError=validar_saldos();
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
	
	
	public Integer getSaldo_laborales() {
		return saldo_laborales;
	}

	public void setSaldo_laborales(Integer saldo_laborales) {
		this.saldo_laborales = saldo_laborales;
	}

	public Integer getSalado_suplementarias() {
		return salado_suplementarias;
	}

	public void setSalado_suplementarias(Integer salado_suplementarias) {
		this.salado_suplementarias = salado_suplementarias;
	}

	public Integer getSaldo_extraordinarias() {
		return saldo_extraordinarias;
	}

	public void setSaldo_extraordinarias(Integer saldo_extraordinarias) {
		this.saldo_extraordinarias = saldo_extraordinarias;
	}
	
	public void init_saldos(){
		FuncionesList funcioneslist = (FuncionesList) Component.getInstance("funcionesList", true);
		List<Funciones> funciones = new ArrayList<Funciones>();
		Integer a1=0,b1=0,c1=0,a2=0,b2=0,c2=0;	
		funcioneslist.getFunciones().setPaex_id(this.getInstance().getPaex_id());
		funciones=funcioneslist.getResultList();		
		for(Funciones f : funciones){
			//if(f.getPaex_id().getPaexId()==this.getInstance().getPaex_id().getPaexId()){
				a2=a2+f.getHoras_laborales();
				b2=b2+f.getHoras_suplementarias();
				c2=c2+f.getHoras_extraordinarias();
			//}			
		}		
		a1=this.getInstance().getPaex_id().getTotal_horas_laborales();
		b1=this.getInstance().getPaex_id().getTotal_horas_suplementarias();
		c1=this.getInstance().getPaex_id().getTotal_horas_extraordinarias();		
		this.saldo_laborales=a1-a2;
		this.salado_suplementarias=b1-b2;
		this.saldo_extraordinarias=c1-c2;		
	}	
	
	public String validar_saldos(){
		String mensaje;	
		if(this.getInstance().getHoras_laborales()==0&&this.getInstance().getHoras_suplementarias()==0&&this.getInstance().getHoras_extraordinarias()==0){
			mensaje="En las horas ingresadas debe ser al menos una mayor que cero!";
		}else{
			if(this.saldo_laborales==0&&this.salado_suplementarias==0&&this.saldo_extraordinarias==0){
				mensaje="Saldo de horas insuficiente!";
			}else{
				if((this.getInstance().getHoras_laborales()<=this.saldo_laborales)&&(this.getInstance().getHoras_suplementarias()<=this.salado_suplementarias)
						&&(this.getInstance().getHoras_extraordinarias()<=this.saldo_extraordinarias)){
					mensaje="Ninguno";
				}else{
					mensaje="Las horas no deben exceder a sus saldos respectivos!";
				}		
			}	
		}			
		return mensaje;
	}
	

	public String init_saldos_laborables(Long a, Integer b){
		
		PlanExtras pe=new PlanExtras();
		pe.setPaexId(a);
		pe.setTotal_horas_laborales(b);
		
		String msg;		
		FuncionesList funcioneslist = (FuncionesList) Component.getInstance("funcionesList", true);
		List<Funciones> funciones = new ArrayList<Funciones>();
		Integer a2=0;
		funcioneslist.getFunciones().setPaex_id(pe);
		funciones=funcioneslist.getResultList();
		for(Funciones f : funciones){
//			System.out.println("FUNCIONES ID......................................................................."+f.getFunc_id());
			//if(f.getPaex_id().getPaexId().toString().equals(a.toString())){				
				a2=a2+f.getHoras_laborales();					
			//}			
		}				
		if(b.toString().equals(a2.toString())){
			msg="";
		}else{
			msg="*Debe ingresar todas las actividades necesarias para completar "+b.toString()+" horas laborables para su debida autorización!";
		}		
		return msg;
	}	
	
	void set_saldos(){
		FuncionesList funcioneslist = (FuncionesList) Component.getInstance("funcionesList", true);
		List<Funciones> funciones = new ArrayList<Funciones>();
		Integer a1=0,b1=0,c1=0,a2=0,b2=0,c2=0;	
		funcioneslist.getFunciones().setPaex_id(this.getInstance().getPaex_id());
		funciones=funcioneslist.getResultList();
		for(Funciones f : funciones){
			if(f.getFunc_id()!=this.getInstance().getFunc_id()){
				//if(f.getPaex_id().getPaexId().toString().equals(this.getInstance().getPaex_id().getPaexId().toString())){	
					a2=a2+f.getHoras_laborales();
					b2=b2+f.getHoras_suplementarias();
					c2=c2+f.getHoras_extraordinarias();
				//}				
			}
		}		
		a1=this.getInstance().getPaex_id().getTotal_horas_laborales();
		b1=this.getInstance().getPaex_id().getTotal_horas_suplementarias();
		c1=this.getInstance().getPaex_id().getTotal_horas_extraordinarias();		
		this.saldo_laborales=a1-a2;
		this.salado_suplementarias=b1-b2;
		this.saldo_extraordinarias=c1-c2;	
	}

}
