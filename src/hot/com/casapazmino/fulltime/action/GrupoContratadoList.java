package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.GrupoContratado;

@Name("grupoContratadoList")
public class GrupoContratadoList extends EntityQuery<GrupoContratado> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select grupoContratado from GrupoContratado grupoContratado";

	private static final String[] RESTRICTIONS = { "lower(grupoContratado.descripcion) like concat(lower(#{grupoContratadoList.grupoContratado.descripcion}),'%')", };
	private static final String ORDER = "grupoContratado.descripcion";
	
	private String extensionArchivo;
	
	private GrupoContratado grupoContratado = new GrupoContratado();

	public GrupoContratadoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public GrupoContratado getGrupoContratado() {
		return grupoContratado;
	}
	
	public List<GrupoContratado> getListaGrupoContratado() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setGrupoContratado(GrupoContratado grupoContratado) {
		this.grupoContratado = grupoContratado;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
	
}
