package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.Roles;

@Name("rolesList")
public class RolesList extends EntityQuery<Roles> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select roles from Roles roles";

	private static final String[] RESTRICTIONS = {"lower(roles.descripcion) like concat(lower(#{rolesList.roles.descripcion}),'%')",};

	private String extensionArchivo;
	
	private Roles roles = new Roles();

	public RolesList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Roles getRoles() {
		return roles;
	}
	
	public List<Roles> getlistadoRoles()
	{
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
