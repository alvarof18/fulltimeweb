package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.DetalleHorario;

@Name("detalleHorarioList")
public class DetalleHorarioList extends EntityQuery<DetalleHorario> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select detalleHorario from DetalleHorario detalleHorario";

	private static final String[] RESTRICTIONS = { "lower(detalleHorario.entradaSalida) like lower(#{detalleHorarioList.detalleHorario.entradaSalida})", };
	private static final String ORDER = "detalleHorario.hora";
	
	private String extensionArchivo;
	
	private DetalleHorario detalleHorario = new DetalleHorario();

	public DetalleHorarioList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public DetalleHorario getDetalleHorario() {
		return detalleHorario;
	}

	public List<DetalleHorario> getListaDetalleHorario() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleHorario> getListaDetalleHorarioSalida() {
		this.getDetalleHorario().setEntradaSalida(Constantes.SALIDA);
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
