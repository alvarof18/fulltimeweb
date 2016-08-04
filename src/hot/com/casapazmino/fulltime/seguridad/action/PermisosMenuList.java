package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.PermisosMenu;

@Name("permisosMenuList")
public class PermisosMenuList extends EntityQuery<PermisosMenu> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select permisosMenu from PermisosMenu permisosMenu";

	private static final String[] RESTRICTIONS = {
		"PermisosMenu.roles.id = #{permisoRol.id})",
	};

	private String extensionArchivo;
	
	private PermisosMenu permisosMenu = new PermisosMenu();

	public PermisosMenuList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public PermisosMenu getPermisosMenu() {
		return permisosMenu;
	}
	
	public List<PermisosMenu> listaPermisosMenu()
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