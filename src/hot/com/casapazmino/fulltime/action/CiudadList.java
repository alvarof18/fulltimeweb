package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Ciudad;

@Name("ciudadList")
public class CiudadList extends EntityQuery<Ciudad> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select ciudad from Ciudad ciudad";
	private static final String ORDER = "ciudad.descripcion";

	private static final String[] RESTRICTIONS = {
			"lower(ciudad.codigo) like concat(lower(#{ciudadList.ciudad.codigo}),'%')",
			"lower(ciudad.descripcion) like concat(lower(#{ciudadList.ciudad.descripcion}),'%')", };

	private String extensionArchivo;
	
	private Ciudad ciudad = new Ciudad();

	public CiudadList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Ciudad getCiudad() {
		return ciudad;
	}
	
	public List<Ciudad> getListaCiudades() {
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
