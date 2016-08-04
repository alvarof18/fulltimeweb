package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.HoraExtra;

@Name("horaExtraList")
public class HoraExtraList extends EntityQuery<HoraExtra> {

	private static final long serialVersionUID = -2102096286145360136L;

	private static final String EJBQL = "select horaExtra from HoraExtra horaExtra";

	private static final String[] RESTRICTIONS = {"horaExtra.descripcion = #{horaExtraList.horaExtra.descripcion}",};
	private static final String ORDER = "horaExtra.descripcion";
	private String extensionArchivo;
	
	private HoraExtra horaExtra = new HoraExtra();

	
	public HoraExtraList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public HoraExtra getHoraExtra() {
		return horaExtra;
	}

	public List<HoraExtra> getListaHoraExtra() {
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