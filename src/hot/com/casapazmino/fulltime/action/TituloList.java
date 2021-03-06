package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Titulo;

@Name("tituloList")
public class TituloList extends EntityQuery<Titulo> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select titulo from Titulo titulo";

	private static final String[] RESTRICTIONS = { "lower(titulo.descripcion) like concat(lower(#{tituloList.titulo.descripcion}),'%')", };
	private static final String ORDER = "titulo.descripcion";

	private String extensionArchivo;
	
	private Titulo titulo = new Titulo();

	public TituloList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Titulo getTitulo() {
		return titulo;
	}
	
	public List<Titulo> getListaTitulo() {
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
