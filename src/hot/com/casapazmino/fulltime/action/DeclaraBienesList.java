package com.casapazmino.fulltime.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.DeclaraBienes;

@Name("declaraBienesList")
public class DeclaraBienesList extends EntityQuery<DeclaraBienes> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select declaraBienes from DeclaraBienes declaraBienes";

	private static final String[] RESTRICTIONS = {
			"lower(declaraBienes.lugarNotaria) like concat(lower(#{declaraBienesList.declaraBienes.lugarNotaria}),'%')",
			"lower(declaraBienes.numeroNotaria) like concat(lower(#{declaraBienesList.declaraBienes.numeroNotaria}),'%')", };

	private String extensionArchivo;
	
	private DeclaraBienes declaraBienes = new DeclaraBienes();

	public DeclaraBienesList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public DeclaraBienes getDeclaraBienes() {
		return declaraBienes;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
