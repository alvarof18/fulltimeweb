package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityNotFoundException;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.Usuarios;

@Name("usuariosList")
public class UsuariosList extends EntityQuery<Usuarios> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select usuarios from Usuarios usuarios";

	private static final String[] RESTRICTIONS = {
			"lower(usuarios.contrasena) like concat(lower(#{usuariosList.usuarios.contrasena}),'%')",
			"lower(usuarios.estado.descripcion) like concat(lower(#{usuariosList.usuarios.estado.descripcion}),'%')",
			"lower(usuarios.usuario) like concat(lower(#{usuariosList.usuarios.usuario}),'%')",
			};

	private String extensionArchivo;
	
	private Usuarios usuarios = new Usuarios();
	
	@Logger
	private Log log;
	@In
	Identity identity;
	@In
	Credentials credentials;

	public UsuariosList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Usuarios getUsuarios() {
		return usuarios;
	}
	
	public List<Usuarios> getListaUsuarios() {
		this.setMaxResults(null);
		return this.getResultList();
	}
	
	public Usuarios obtenerUsuario(String usuario, String password, String estado)
	{
		log.info("ENTRO EN IF DE OBTENER USUARIO");
		/*String user=credentials.getUsername().toString();
		String pws=credentials.getPassword().toString();*/
		Usuarios usuarios=new Usuarios();
		try {
			StringBuffer sentenciaSql = new StringBuffer();
			sentenciaSql.append(EJBQL);
			sentenciaSql.append(" where Usuarios.usuario = ?1");
/*			if (estado != null) {
				sentenciaSql.append(" and Usuarios.estado= ?2");
			}
			sentenciaSql.append(" and Usuarios.contrasena= ?3");*/
			Query query = this.getEntityManager().createQuery(
					sentenciaSql.toString());
			query.setParameter(1, usuario);
/*			if (estado != null) {
				query.setParameter(2, estado);
			}
			query.setParameter(3,password);*/
			
			usuarios = (Usuarios) query.getSingleResult();
		} catch (EntityNotFoundException e) {
			usuarios = null;
			log.info("No se encontro usuario: " + credentials.getUsername());
		} catch (NoResultException e) {
			usuarios = null;
			log.info("No se encontro usuario: " + credentials.getUsername());
		} catch (Exception e) {
			usuarios = null;
			log.info("Error en metodo obtenerUsuario: ", e);
		}
		return usuarios;		
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
