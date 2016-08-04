package com.casapazmino.fulltime.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.AsistenciaHoraExtra;
import com.casapazmino.fulltime.model.HoraExtra;

@Name("asistenciaHoraExtraHome")
public class AsistenciaHoraExtraHome extends EntityHome<AsistenciaHoraExtra> {

	private static final long serialVersionUID = -5609552014296981428L;
	
	@In(create = true)
	AsistenciaHome asistenciaHome;
	@In(create = true)
	HoraExtraHome horaExtraHome;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}
	
	public void setAsistenciaHoraExtraAsisHoraExtrId(Integer id) {
		setId(id);
	}

	public Integer getAsistenciaHoraExtraAsisHoraExtrId() {
		return (Integer) getId();
	}

	@Override
	protected AsistenciaHoraExtra createInstance() {
		AsistenciaHoraExtra asistenciaHoraExtra = new AsistenciaHoraExtra();
		return asistenciaHoraExtra;
	}

	public void wire() {
		getInstance();
		Asistencia asistencia = asistenciaHome.getDefinedInstance();
		if (asistencia != null) {
			getInstance().setAsistencia(asistencia);
		}
		HoraExtra horaExtra = horaExtraHome.getDefinedInstance();
		if (horaExtra != null) {
			getInstance().setHoraExtra(horaExtra);
		}
	}

	public boolean isWired() {
		if (getInstance().getAsistencia() == null)
			return false;
		if (getInstance().getHoraExtra() == null)
			return false;
		return true;
	}

	public AsistenciaHoraExtra getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
