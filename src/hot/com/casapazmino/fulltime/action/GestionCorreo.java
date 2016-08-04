package com.casapazmino.fulltime.action;


import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Permiso;

@Name("gestionCorreoList")
public class GestionCorreo extends EntityHome<Permiso>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean crear;
	private Boolean actualizar;
	private Boolean eliminar;
	private Boolean preAutorizar;
	private Boolean noAutorizar;
	private Boolean Autorizar;
	private Boolean Legalizar;
	
	public Boolean getCrear() {
		return crear;
	}
	public void setCrear(Boolean crear) {
		this.crear = crear;
	}
	public Boolean getActualizar() {
		return actualizar;
	}
	public void setActualizar(Boolean actualizar) {
		this.actualizar = actualizar;
	}
	public Boolean getEliminar() {
		return eliminar;
	}
	public void setEliminar(Boolean eliminar) {
		this.eliminar = eliminar;
	}
	public Boolean getPreAutorizar() {
		return preAutorizar;
	}
	public void setPreAutorizar(Boolean preAutorizar) {
		this.preAutorizar = preAutorizar;
	}
	public Boolean getNoAutorizar() {
		return noAutorizar;
	}
	public void setNoAutorizar(Boolean noAutorizar) {
		this.noAutorizar = noAutorizar;
	}
	public Boolean getAutorizar() {
		return Autorizar;
	}
	public void setAutorizar(Boolean autorizar) {
		Autorizar = autorizar;
	}
	public Boolean getLegalizar() {
		return Legalizar;
	}
	public void setLegalizar(Boolean legalizar) {
		Legalizar = legalizar;
	}
	
	
}
