package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.DetalleTipoPermiso;
import com.casapazmino.fulltime.model.TipoPermiso;

@Name("detalleTipoPermisoList")
public class DetalleTipoPermisoList extends EntityQuery<DetalleTipoPermiso> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select detalleTipoPermiso from DetalleTipoPermiso detalleTipoPermiso";

	private static final String[] RESTRICTIONS = { "detalleTipoPermiso.tipo = #{detalleTipoPermisoList.tipo}", 
		"detalleTipoPermiso.tipoPermiso.tipeId = #{detalleTipoPermisoList.tipoPermiso.tipeId}", };
	private static final String ORDER = "detalleTipoPermiso.dtpeId";
	
	private String tipo;
	
	private TipoPermiso tipoPermiso = new TipoPermiso();

	public DetalleTipoPermisoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
	}

	public List<DetalleTipoPermiso> getListaDetalleTipoPermiso() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public TipoPermiso getTipoPermiso() {
		return tipoPermiso;
	}

	public void setTipoPermiso(TipoPermiso tipoPermiso) {
		this.tipoPermiso = tipoPermiso;
	}

}
