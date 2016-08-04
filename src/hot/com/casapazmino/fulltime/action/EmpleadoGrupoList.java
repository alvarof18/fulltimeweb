package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.EmpleadoGrupo;

@Name("empleadoGrupoList")
public class EmpleadoGrupoList extends EntityQuery<EmpleadoGrupo> {

	private static final long serialVersionUID = -6648581959669954329L;

	private static final String EJBQL = "select empleadoGrupo from EmpleadoGrupo empleadoGrupo";

	private static final String[] RESTRICTIONS = {"lower(empleadoGrupo.descripcion) like concat(lower(#{empleadoGrupoList.empleadoGrupo.descripcion}),'%')",};

	private EmpleadoGrupo empleadoGrupo = new EmpleadoGrupo();

	public EmpleadoGrupoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
	}

	public EmpleadoGrupo getEmpleadoGrupo() {
		return empleadoGrupo;
	}
	
	public List<EmpleadoGrupo> getListaEmpleadoGrupos() {
		this.setMaxResults(null);
		return this.getResultList();
	}
}
