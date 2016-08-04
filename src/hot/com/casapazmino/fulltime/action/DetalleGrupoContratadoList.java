package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;

@Name("detalleGrupoContratadoList")
public class DetalleGrupoContratadoList extends
		EntityQuery<DetalleGrupoContratado> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select detalleGrupoContratado from DetalleGrupoContratado detalleGrupoContratado";

	private static final String[] RESTRICTIONS = { "lower(detalleGrupoContratado.descripcion) like concat(lower(#{detalleGrupoContratadoList.detalleGrupoContratado.descripcion}),'%')", };
	private static final String ORDER = "detalleGrupoContratado.descripcion";
	
	private String extensionArchivo;
	
	private DetalleGrupoContratado detalleGrupoContratado = new DetalleGrupoContratado();

	public DetalleGrupoContratadoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public DetalleGrupoContratado getDetalleGrupoContratado() {
		return detalleGrupoContratado;
	}
	
	public List<DetalleGrupoContratado> getListaDetalleGrupoContratado() {
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
