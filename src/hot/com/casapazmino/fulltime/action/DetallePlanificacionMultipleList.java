package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.DetallePlanificacionMultiple;

@Name("detallePlanificacionMultipleList")
public class DetallePlanificacionMultipleList extends EntityQuery<DetallePlanificacionMultiple> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select detallePlanificacionMultiple from DetallePlanificacionMultiple detallePlanificacionMultiple";

	private static final String[] RESTRICTIONS = {
			"detallePlanificacionMultiple.idPlanificacionMultiple = #{detallePlanificacionMultipleList.detallePlanificacionMultiple.idPlanificacionMultiple}",
			"detallePlanificacionMultiple.idPlanificacionMultiple in (#{detallePlanificacionMultipleList.listaIdPlanificacionMultiple})",
			"detallePlanificacionMultiple.idHorario = #{detallePlanificacionMultipleList.detallePlanificacionMultiple.idHorario}",
			"detallePlanificacionMultiple.dia = #{detallePlanificacionMultipleList.detallePlanificacionMultiple.dia}",
			"detallePlanificacionMultiple.estado = #{detallePlanificacionMultipleList.detallePlanificacionMultiple.estado}",
			"detallePlanificacionMultiple.dia  >= #{detallePlanificacionMultipleList.diaDesde}",
			"detallePlanificacionMultiple.dia  <= #{detallePlanificacionMultipleList.diaHasta}"};
	
	private Integer diaDesde;
	
	private Integer diaHasta;
	
	private DetallePlanificacionMultiple detallePlanificacionMultiple = new DetallePlanificacionMultiple();
	
	private ArrayList<Long> listaIdPlanificacionMultiple=new ArrayList<Long>();	

	public DetallePlanificacionMultipleList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
	}

	public DetallePlanificacionMultiple getDetallePlanificacionMultiple() {
		return detallePlanificacionMultiple;
	}
	
	public List<DetallePlanificacionMultiple> getListaDetallePlanificacionMultiple() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public ArrayList<Long> getListaIdPlanificacionMultiple() {
		return listaIdPlanificacionMultiple;
	}

	public void setListaIdPlanificacionMultiple(
			ArrayList<Long> listaIdPlanificacionMultiple) {
		this.listaIdPlanificacionMultiple = listaIdPlanificacionMultiple;
	}

	public Integer getDiaDesde() {
		return diaDesde;
	}

	public void setDiaDesde(Integer diaDesde) {
		this.diaDesde = diaDesde;
	}

	public Integer getDiaHasta() {
		return diaHasta;
	}

	public void setDiaHasta(Integer diaHasta) {
		this.diaHasta = diaHasta;
	}

	
}
