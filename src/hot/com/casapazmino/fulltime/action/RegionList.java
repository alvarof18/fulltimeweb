package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Region;

@Name("regionList")
public class RegionList extends EntityQuery<Region> {
	
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select region from Region region";

	private static final String[] RESTRICTIONS = {
			"lower(region.codigo) like concat(lower(#{regionList.region.codigo}),'%')",
			"lower(region.descripcion) like concat(lower(#{regionList.region.descripcion}),'%')", };
	
	private static final String ORDER = "region.descripcion";
	
	private String extensionArchivo;
	
	private Region region = new Region();
	
	public RegionList() {
//		System.out.println("=========================== Variable base de batos " + System.getProperty("bdd"));
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Region getRegion() {
		return region;
	}
	
	public List<Region> getListaRegiones() {
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