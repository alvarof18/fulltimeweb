package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.TipoCargo;

@Name("tipoCargoList")
public class TipoCargoList extends EntityQuery<TipoCargo> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select tipoCargo from TipoCargo tipoCargo";

	private static final String[] RESTRICTIONS = { "lower(tipoCargo.descripcion) like concat(lower(#{tipoCargoList.tipoCargo.descripcion}),'%')", };
	private static final String ORDER = "tipoCargo.descripcion";
	
	private String extensionArchivo;
	
	private TipoCargo tipoCargo = new TipoCargo();

	public TipoCargoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public TipoCargo getTipoCargo() {
		return tipoCargo;
	}

	public List<TipoCargo> getListaTipoCargo() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setTipoCargo(TipoCargo tipoCargo) {
		this.tipoCargo = tipoCargo;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}	
}
