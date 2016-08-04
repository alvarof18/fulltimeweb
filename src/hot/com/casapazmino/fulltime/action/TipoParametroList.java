package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.TipoParametro;

@Name("tipoParametroList")
public class TipoParametroList extends EntityQuery<TipoParametro> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select tipoParametro from TipoParametro tipoParametro";

	private static final String[] RESTRICTIONS = {
		"tipoParametro.tipaId = #{tipoParametroList.tipoParametro.tipaId}",
		"lower(tipoParametro.descripcion) like concat(lower(#{tipoParametroList.tipoParametro.descripcion}),'%')",
		};
	private static final String ORDER = "tipoParametro.descripcion";

	private String extensionArchivo;
	
	private TipoParametro tipoParametro = new TipoParametro();

	public TipoParametroList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public TipoParametro getTipoParametro() {
		return tipoParametro;
	}
	
	public List<TipoParametro> getListaTipoParametro() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setTipoParametro(TipoParametro tipoParametro) {
		this.tipoParametro = tipoParametro;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
	
}
