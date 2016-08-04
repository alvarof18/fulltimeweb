package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Cargo;

@Name("cargoList")
public class CargoList extends EntityQuery<Cargo> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select cargo from Cargo cargo";

	private static final String[] RESTRICTIONS = { 
		"lower(cargo.descripcion) like concat(lower(#{cargoList.cargo.descripcion}),'%')",
	};
	private static final String ORDER = "cargo.descripcion";

	private String extensionArchivo;
	
	private Cargo cargo = new Cargo();

	public CargoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Cargo getCargo() {
		return cargo;
	}
	
	public List<Cargo> getListaCargos() {
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
