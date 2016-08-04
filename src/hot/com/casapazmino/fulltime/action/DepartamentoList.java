package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Departamento;

@Name("departamentoList")
public class DepartamentoList extends EntityQuery<Departamento> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select departamento from Departamento departamento";
	
	private static final String[] RESTRICTIONS = {
			"lower(departamento.codigo) like concat(lower(#{departamentoList.departamento.codigo}),'%')",
			"lower(departamento.descripcion) like concat(lower(#{departamentoList.departamento.descripcion}),'%')", };
	private static final String ORDER = "departamento.descripcion";
	
	private String extensionArchivo;
	
	private Departamento departamento = new Departamento();

	public DepartamentoList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Departamento getDepartamento() {
		return departamento;
	}
	
	public List<Departamento> getListaDepartamentos() {
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