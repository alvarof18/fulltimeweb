package com.casapazmino.fulltime.action;


import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.model.SolicitudVacacionAutorizacion;

@Name("solicitudVacacionAutorizacionList")
public class SolicitudVacacionAutorizacionList extends EntityQuery<SolicitudVacacionAutorizacion> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select solicitud_vacacion_autorizacion from SolicitudVacacionAutorizacion solicitud_vacacion_autorizacion";

	private static final String[] RESTRICTIONS = { 
		"solicitud_vacacion_autorizacion.sova_aut_id = #{solicitudVacacionAutorizacionList.solicitudVacacionAutorizacion.sova_aut_id}",
		"solicitud_vacacion_autorizacion.solicitudVacacion.sova = #{solicitudVacacionAutorizacionList.solicitudVacacion.sova}"};

	//private String extensionArchivo;
	
	private SolicitudVacacionAutorizacion solicitudVacacionAutorizacion = new SolicitudVacacionAutorizacion();
	
	private SolicitudVacacion solicitudVacacion = new SolicitudVacacion();
	
	@In
	private FacesMessages facesMessages;

	public SolicitudVacacionAutorizacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);			
	}

	public List<SolicitudVacacionAutorizacion> getListaSolicitudVacacionAutorizacion() {		
		this.setMaxResults(null);
		return this.getResultList();
	}

	public SolicitudVacacionAutorizacion getSolicitudVacacionAutorizacion() {
		if(solicitudVacacionAutorizacion==null){
			solicitudVacacionAutorizacion=new SolicitudVacacionAutorizacion();
		}
		return solicitudVacacionAutorizacion;
	}

	public void setSolicitudVacacionAutorizacion(SolicitudVacacionAutorizacion solicitudVacacionAutorizacion) {
		this.solicitudVacacionAutorizacion = solicitudVacacionAutorizacion;
	}

	public SolicitudVacacion getSolicitudVacacion() {
		if(this.solicitudVacacion==null)
			this.solicitudVacacion=new SolicitudVacacion();
		return this.solicitudVacacion;
	}

	public void setSolicitudVacacion(SolicitudVacacion solicitudVacacion) {
		this.solicitudVacacion = solicitudVacacion;
	}

	/*public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}*/
}
