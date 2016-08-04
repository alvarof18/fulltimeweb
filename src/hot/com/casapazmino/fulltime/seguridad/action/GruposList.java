package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.Grupos;

@Name("gruposList")
public class GruposList extends EntityQuery<Grupos> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select grupos from Grupos grupos";
	private static final String ORDER = "grupos.orden";

	private static final String[] RESTRICTIONS = {
			"grupos.id = #{idGrupo}",
			"lower(grupos.descripcion) like concat(lower(#{gruposList.grupos.descripcion}),'%')",
			"lower(grupos.etiqueta) like concat(lower(#{gruposList.grupos.etiqueta}),'%')",
			"lower(grupos.nombre) like concat(lower(#{gruposList.grupos.nombre}),'%')",};

	private String extensionArchivo;
	
	private Grupos grupos = new Grupos();

	public GruposList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder(ORDER);
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Grupos getGrupos() {
		return grupos;
	}
	
	public List<Grupos> getListaGrupos()
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
