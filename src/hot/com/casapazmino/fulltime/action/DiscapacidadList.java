package com.casapazmino.fulltime.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Discapacidad;

@Name("discapacidadList")
public class DiscapacidadList extends EntityQuery<Discapacidad> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select discapacidad from Discapacidad discapacidad";

	private static final String[] RESTRICTIONS = { "lower(discapacidad.carnetConadis) like concat(lower(#{discapacidadList.discapacidad.carnetConadis}),'%')", };

	private String extensionArchivo;
	
	private Discapacidad discapacidad = new Discapacidad();

	public DiscapacidadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Discapacidad getDiscapacidad() {
		return discapacidad;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
