package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.model.Feriado;

@Name("feriadoList")
public class FeriadoList extends EntityQuery<Feriado> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select feriado from Feriado feriado";

	private static final String[] RESTRICTIONS = {
		"lower(feriado.descripcion) like concat(lower(#{feriadoList.feriado.descripcion}),'%')",
		ControlBaseDatos.modificadorConvertirFecha + "feriado.fecha) >= #{feriadoList.feriado.fecha}",
	};
	private static final String ORDER = "feriado.fecha";

	private String extensionArchivo;
	
	private Feriado feriado = new Feriado();

	public FeriadoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Feriado getFeriado() {
		return feriado;
	}

	public List<Feriado> getListaFeriado() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setFeriado(Feriado feriado) {
		this.feriado = feriado;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
