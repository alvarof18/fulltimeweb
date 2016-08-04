package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.EmpleadoHorario;

@Name("empleadoHorarioList")
public class EmpleadoHorarioList extends EntityQuery<EmpleadoHorario> {

	private static final long serialVersionUID = -8941592671891011846L;
	
	private static final String EJBQL = "select empleadoHorario from EmpleadoHorario empleadoHorario";

	private static final String[] RESTRICTIONS = {
		"lower(empleadoHorario.empleado.detalleTipoParametroByEstado.descripcion) = lower((#{empleadoHorarioList.empleadoHorario.empleado.detalleTipoParametroByEstado.descripcion}))",
	};

	private static final String ORDER = "empleadoHorario.fechaIni";	
	
	private EmpleadoHorario empleadoHorario = new EmpleadoHorario();

	public EmpleadoHorarioList() {
		setOrder(ORDER);
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
	}

	public EmpleadoHorario getEmpleadoHorario() {
		return empleadoHorario;
	}
	
	public List<EmpleadoHorario> getListaEmpleadoHorarios() {
		this.setMaxResults(null);
		return this.getResultList();
	} 
	
	
}