package com.casapazmino.fulltime.action;


import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Funciones;

@Name("funcionesList")
public class FuncionesList extends EntityQuery<Funciones> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select funciones from Funciones funciones";

	private static final String[] RESTRICTIONS = { "funciones.paex_id.paexId = #{funcionesList.funciones.paex_id.paexId}" };

	//private String extensionArchivo;
	
	private Funciones funciones = new Funciones();
	
	@In
	private FacesMessages facesMessages;

	public FuncionesList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);	
		
	}

	public Funciones getFunciones() {
		return funciones;
	}

	public List<Funciones> getListaFunciones() {
		
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setFunciones(Funciones funciones) {
		this.funciones = funciones;
	}

	/*public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}*/
}
