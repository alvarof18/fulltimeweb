package com.casapazmino.fulltime.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.model.AsistenciaHoraExtra;

@Name("asistenciaHoraExtraList")
public class AsistenciaHoraExtraList extends EntityQuery<AsistenciaHoraExtra> {

	private static final long serialVersionUID = 473162015812025242L;

	private static final String EJBQL = "select asistenciaHoraExtra from AsistenciaHoraExtra asistenciaHoraExtra";

	private static final String[] RESTRICTIONS = {};

	private AsistenciaHoraExtra asistenciaHoraExtra = new AsistenciaHoraExtra();

	public AsistenciaHoraExtraList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public AsistenciaHoraExtra getAsistenciaHoraExtra() {
		return asistenciaHoraExtra;
	}
}
