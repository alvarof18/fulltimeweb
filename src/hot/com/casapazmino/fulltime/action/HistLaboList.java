package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.HistLabo;

@Name("histLaboList")
public class HistLaboList extends EntityQuery<HistLabo> {

	private static final long serialVersionUID = 6416548774859778405L;

	private static final String EJBQL = "select histLabo from HistLabo histLabo";

	private static final String[] RESTRICTIONS = {
		"histLabo.estado = #{histLaboList.histLabo.estado}",
		"histLabo.empleado.emplId = #{histLaboList.histLabo.empleado.emplId}",
	};

	private HistLabo histLabo = new HistLabo();
	
	private Empleado empleado = new Empleado();	

	public HistLaboList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	public List<HistLabo> getListaHistoriaLaboral() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public HistLabo getHistLabo() {
		return histLabo;
	}
	
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
}
