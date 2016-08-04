package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.UsuariosRoles;

@Name("usuariosRolesList")
public class UsuariosRolesList extends EntityQuery<UsuariosRoles> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select usuariosRoles from UsuariosRoles usuariosRoles";
	

	private static final String[] RESTRICTIONS = {
		"usuariosRoles.usuarios.usuario = #{users}",
		"usuariosRoles.id = #{usuariosRolesList.usuariosRoles.id}",
		"usuariosRoles.usuarios.usuario = #{usuariosRolesList.usuariosRoles.usuarios.usuario}",

	};

	private String extensionArchivo;
	
	private UsuariosRoles usuariosRoles;

	public UsuariosRolesList() {
		usuariosRoles=new UsuariosRoles();
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public UsuariosRoles getUsuariosRoles() {
		return usuariosRoles;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
