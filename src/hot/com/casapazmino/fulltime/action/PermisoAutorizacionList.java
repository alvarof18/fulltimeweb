package com.casapazmino.fulltime.action;


import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.PermisoAutorizacion;

@Name("permisoAutorizacionList")
public class PermisoAutorizacionList extends EntityQuery<PermisoAutorizacion> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select permiso_autorizacion from PermisoAutorizacion permiso_autorizacion";

	private static final String[] RESTRICTIONS = { 
		"permiso_autorizacion.perm_aut_id = #{permisoAutorizacionList.permisoAutorizacion.perm_aut_id}",
		"permiso_autorizacion.permiso.permId = #{permisoAutorizacionList.permiso.permId}"};

	//private String extensionArchivo;
	
	private PermisoAutorizacion permisoAutorizacion = new PermisoAutorizacion();
	
	private Permiso permiso = new Permiso();
	
	@In
	private FacesMessages facesMessages;

	public PermisoAutorizacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);	
		
	}

	public List<PermisoAutorizacion> getListaPermisoAutorizacion() {		
		this.setMaxResults(null);
		return this.getResultList();
	}

	public PermisoAutorizacion getPermisoAutorizacion() {
		if(permisoAutorizacion==null){
			permisoAutorizacion=new PermisoAutorizacion();
		}
		return permisoAutorizacion;
	}

	public void setPermisoAutorizacion(PermisoAutorizacion permisoAutorizacion) {
		this.permisoAutorizacion = permisoAutorizacion;
	}

	public Permiso getPermiso() {
		if(this.permiso==null)
			this.permiso=new Permiso();
		return this.permiso;
	}

	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
	}

	/*public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}*/
}
