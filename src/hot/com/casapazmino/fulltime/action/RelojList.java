package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Reloj;

@Name("relojList")
public class RelojList extends EntityQuery<Reloj> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select reloj from Reloj reloj";

	private static final String[] RESTRICTIONS = { "lower(reloj.descripcion) like concat(lower(#{relojList.reloj.descripcion}),'%')", };
	private static final String ORDER = "reloj.descripcion";

	private String extensionArchivo;
	
	private Reloj reloj = new Reloj();

	public RelojList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Reloj getReloj() {
		return reloj;
	}
	
	public List<Reloj> getListaReloj() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setReloj(Reloj reloj) {
		this.reloj = reloj;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}	
}
