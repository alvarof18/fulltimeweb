package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.Subgrupos;

@Name("subgruposList")
public class SubgruposList extends EntityQuery<Subgrupos> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select subgrupos from Subgrupos subgrupos";
	//private static final String ORDER = "subgrupos.orden";

	private static final String[] RESTRICTIONS = {
			"subgrupos.id = #{idSubgrupo}",
			"lower(subgrupos.descripcion) like concat(lower(#{subgruposList.subgrupos.descripcion}),'%')",
			"lower(subgrupos.etiqueta) like concat(lower(#{subgruposList.subgrupos.etiqueta}),'%')",
			"lower(subgrupos.nombre) like concat(lower(#{subgruposList.subgrupos.nombre}),'%')",
			"lower(subgrupos.grupos.nombre) like concat(lower(#{subgruposList.subgrupos.grupos.nombre}),'%')",};

	private String extensionArchivo;
	
	private Subgrupos subgrupos;
	
	@In(create = true)
	ItemsMenuRolesList itemsMenuRolesList;

	public SubgruposList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		//setOrder(ORDER);
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Subgrupos getSubgrupos() {
		if (subgrupos == null){
			subgrupos =  new Subgrupos();
		}
		return subgrupos;
	}
	
	public List<Subgrupos> getListaSubgrupo() {
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
