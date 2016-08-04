package com.casapazmino.fulltime.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Atraso;

@Name("atrasoList")
public class AtrasoList extends EntityQuery<Atraso> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select atraso from Atraso atraso";

	private static final String[] RESTRICTIONS = {};

	private String extensionArchivo;
	
	private Atraso atraso = new Atraso();

	public AtrasoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Atraso getAtraso() {
		return atraso;
	}
	
	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
