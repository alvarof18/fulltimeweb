package com.casapazmino.fulltime.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.PlanificacionMultiple;

@Name("planificacionMultipleHome")
public class PlanificacionMultipleHome extends EntityHome<PlanificacionMultiple> {

	private static final long serialVersionUID = 1L;

	@In
	private FacesMessages facesMessages;
	
	@Override
	protected PlanificacionMultiple createInstance() {
		PlanificacionMultiple planificacionMultiple = new PlanificacionMultiple();
		return planificacionMultiple;
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public PlanificacionMultiple getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}	
	
	@Transactional
	@Override
	public String persist(){	
		return super.persist();
	}

	@Transactional
	@Override
	public String update(){
		return super.update();
	}
	
	@Override
	public String remove(){
		return super.remove();
	}
}
