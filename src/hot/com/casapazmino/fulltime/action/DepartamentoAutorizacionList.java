package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.DepartamentoAutorizacion;

@Name("departamentoAutorizacionList")
public class DepartamentoAutorizacionList extends EntityQuery<DepartamentoAutorizacion> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select departamento_autorizacion from DepartamentoAutorizacion departamento_autorizacion";
	
	private static final String[] RESTRICTIONS = {
		    "departamento_autorizacion.depaId = #{departamentoAutorizacionList.departamentoAutorizacion.depaId} ",
			"departamento_autorizacion.departamento.depaId = #{departamentoAutorizacionList.departamentoAutorizacion.departamento.depaId} ",
			"departamento_autorizacion.empleado.emplId = #{departamentoAutorizacionList.departamentoAutorizacion.empleado.emplId} ", };
	private static final String ORDER = "departamento_autorizacion.depaId";
	
	private String extensionArchivo;
	
	private DepartamentoAutorizacion departamento_autorizacion = new DepartamentoAutorizacion();

	public DepartamentoAutorizacionList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public DepartamentoAutorizacion getDepartamentoAutorizacion() {
		if (this.departamento_autorizacion == null){
			this.departamento_autorizacion = new DepartamentoAutorizacion();	
		}
			return this.departamento_autorizacion;
	}
	
	public void SetDepartamentoAutorizacion(DepartamentoAutorizacion departamento_autorizacion ) {
		 this.departamento_autorizacion=departamento_autorizacion ;
	}
	
	
	public List<DepartamentoAutorizacion> getListaDepartamentosAutorizacion() {
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
