package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Planificacion;

@Name("planificacionList")
public class PlanificacionList extends EntityQuery<Planificacion> {

	private static final long serialVersionUID = 8295794995044199310L;

	private static final String EJBQL = "select planificacion from Planificacion planificacion";

	private static final String[] RESTRICTIONS = {
		"planificacion.empleadoByEmplId.emplId = #{planificacionList.planificacion.empleadoByEmplId.emplId}",
		"planificacion.mes = #{planificacionList.planificacion.mes}",
		"planificacion.anio = #{planificacionList.planificacion.anio}",
	};

	private String extensionArchivo;
	
	private Planificacion planificacion = new Planificacion();

	public PlanificacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Planificacion getPlanificacion() {
		return planificacion;
	}
	
	public List<Planificacion> getListaPlanificacion() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
