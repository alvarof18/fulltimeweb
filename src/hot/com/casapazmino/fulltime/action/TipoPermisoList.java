package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.TipoPermiso;

@Name("tipoPermisoList")
public class TipoPermisoList extends EntityQuery<TipoPermiso> {

	private static final long serialVersionUID = 1028305977624721793L;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	private static final String EJBQL = "select tipoPermiso from TipoPermiso tipoPermiso";

	private static final String[] RESTRICTIONS = {
			"lower(tipoPermiso.descripcion) like concat(lower(#{tipoPermisoList.tipoPermiso.descripcion}),'%')",
			"lower(tipoPermiso.descuento) like concat(lower(#{tipoPermisoList.tipoPermiso.descuento}),'%')",
			"tipoPermiso.accesoEmpleados = #{tipoPermisoList.tipoPermiso.accesoEmpleados}",		
	};
	private static final String ORDER = "tipoPermiso.descripcion";
	

	private String extensionArchivo;
	private int accesoEmpleados;
	
	private TipoPermiso tipoPermiso = new TipoPermiso();

	public TipoPermisoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public TipoPermiso getTipoPermiso() {
		return tipoPermiso;
	}
	
	public List<TipoPermiso> getListaTipoPermiso() {
		
		try {
			this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
			
			if (getAccesoEmpleados() ==  Constantes.ACCESO_INDIVIDUAL){
				this.getTipoPermiso().setAccesoEmpleados(1);
				this.setMaxResults(null);
				return this.getResultList();		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setMaxResults(null);
		return this.getResultList();

	}
	
	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}
}
