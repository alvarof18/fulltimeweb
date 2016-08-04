package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.seguridad.model.UsuarioCiudad;
import com.casapazmino.fulltime.seguridad.model.Usuarios;

@Name("usuarioCiudadList")
public class UsuarioCiudadList extends EntityQuery<UsuarioCiudad> {

	private static final long serialVersionUID = 2600886480730006714L;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;	

	private static final String EJBQL = "select usuarioCiudad from UsuarioCiudad usuarioCiudad";

	private static final String[] RESTRICTIONS = {
		"lower(usuarioCiudad.usuarios.usuario) like concat(lower(#{usuarioCiudadList.usuario}),'')",
		"usuarioCiudad.usuarios.id = #{usuarioCiudadList.usuarioCiudad.usuarios.id}",
		"usuarioCiudad.ciudad.ciudId = #{usuarioCiudadList.usuarioCiudad.ciudad.ciudId}",
	};

	private String usuario;
	private UsuarioCiudad usuarioCiudad = new UsuarioCiudad();

	public UsuarioCiudadList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(50);
	}

	public UsuarioCiudad getUsuarioCiudad() {
		return usuarioCiudad;
	}
	
	public List<UsuarioCiudad> getListaCiudadesUsuarios() {
		Usuarios usuarios = gestionPermisoVacacion.buscarUsuario();
		
		this.setUsuario(usuarios.getUsuario());
		this.setMaxResults(null);
		return this.getResultList();
	}
	
	public List<UsuarioCiudad> getListaUsuariosCiudades() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
}
