package com.casapazmino.fulltime.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.model.DetalleHorarioHoraExtra;

@Name("detalleHorarioHoraExtraList")
public class DetalleHorarioHoraExtraList extends EntityQuery<DetalleHorarioHoraExtra> {

	private static final long serialVersionUID = 7284104049247392673L;

	private static final String EJBQL = "select detalleHorarioHoraExtra from DetalleHorarioHoraExtra detalleHorarioHoraExtra";

	private static final String[] RESTRICTIONS = {
		"detalleHorarioHoraExtra.detalleHorario.dehoId = #{detalleHorarioHoraExtraList.detalleHorarioHoraExtra.detalleHorario.dehoId}",
		"detalleHorarioHoraExtra.horaExtra.horaExtraId = #{detalleHorarioHoraExtraList.detalleHorarioHoraExtra.horaExtra.horaExtraId}",
		"detalleHorarioHoraExtra.detalleGrupoContratado.dgcoId = #{detalleHorarioHoraExtraList.detalleHorarioHoraExtra.detalleGrupoContratado.dgcoId}",
	};
	private static final String ORDER = "detalleHorarioHoraExtra.detalleGrupoContratado.descripcion,detalleHorarioHoraExtra.horaExtra.descripcion,detalleHorarioHoraExtra.detalleHorario.hora";
	
	private DetalleHorarioHoraExtra detalleHorarioHoraExtra = new DetalleHorarioHoraExtra();

	public DetalleHorarioHoraExtraList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public DetalleHorarioHoraExtra getDetalleHorarioHoraExtra() {
		return detalleHorarioHoraExtra;
	}
}
