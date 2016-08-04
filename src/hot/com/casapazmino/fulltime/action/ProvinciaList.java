package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Provincia;

@Name("provinciaList")
public class ProvinciaList extends EntityQuery<Provincia> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select provincia from Provincia provincia";

	private static final String[] RESTRICTIONS = {
			"lower(provincia.codigo) like concat(lower(#{provinciaList.provincia.codigo}),'%')",
			"lower(provincia.descripcion) like concat(lower(#{provinciaList.provincia.descripcion}),'%')", };
	private static final String ORDER = "provincia.descripcion";
	
	private String extensionArchivo;
	
	private Provincia provincia = new Provincia();

	public ProvinciaList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Provincia getProvincia() {
		return provincia;
	}
	
	public List<Provincia> getListaProvincias() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}	
}
