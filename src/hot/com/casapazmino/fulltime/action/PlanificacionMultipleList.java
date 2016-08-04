package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.PlanificacionMultiple;

@Name("planificacionMultipleList")
public class PlanificacionMultipleList extends EntityQuery<PlanificacionMultiple> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select planificacionMultiple from PlanificacionMultiple planificacionMultiple";

	private static final String[] RESTRICTIONS = {
			"planificacionMultiple.idEmpleado = #{planificacionMultipleList.planificacionMultiple.idEmpleado}",
			"planificacionMultiple.idEmpleado in (#{planificacionMultipleList.listaIdEmpleado})",
			"planificacionMultiple.mes = #{planificacionMultipleList.planificacionMultiple.mes}",
			"planificacionMultiple.anio = #{planificacionMultipleList.planificacionMultiple.anio}",
			"planificacionMultiple.estado = #{planificacionMultipleList.planificacionMultiple.estado}"};
	
	private PlanificacionMultiple planificacionMultiple = new PlanificacionMultiple();
	
	private ArrayList<Long> listaIdEmpleado=new ArrayList<Long>();	

	public PlanificacionMultipleList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
	}

	public PlanificacionMultiple getPlanificacionMultiple() {
		return planificacionMultiple;
	}
	
	public List<PlanificacionMultiple> getListaPlanificacionMultiple() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public ArrayList<Long> getListaIdEmpleado() {
		return listaIdEmpleado;
	}

	public void setListaIdEmpleado(ArrayList<Long> listaIdEmpleado) {
		this.listaIdEmpleado = listaIdEmpleado;
	}	
}
