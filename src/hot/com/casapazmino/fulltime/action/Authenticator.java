package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import com.casapazmino.fulltime.comun.Cifrado;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.menu.action.ConstruirMenu;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.seguridad.action.ConeccionLdap;
import com.casapazmino.fulltime.seguridad.action.UsuariosList;
import com.casapazmino.fulltime.seguridad.model.Usuarios;

@Name("authenticator")
public class Authenticator {
	@Logger
	private Log log;

	@In
	Identity identity;
	@In
	Credentials credentials;
	@In
	private FacesContext facesContext;

	@In(create = true)
	UsuariosList usuariosList;

	@In(create = true)
	DetalleTipoParametroList detalleTipoParametroList;

	ConstruirMenu construirMenu = new ConstruirMenu();

	private Usuarios usuarios;

	public boolean authenticate() {
		String autenticacion = "BDD"; // "AD"

		if (credentials.getUsername().length() != 0
				&& credentials.getPassword().length() != 0) {

			usuarios = usuariosList.obtenerUsuario(credentials.getUsername()
					.toString(), credentials.getPassword().toString(), null);

			if (usuarios == null) {
				facesContext.addMessage(null, new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						"Usuario no se encuentra en el sistema.", null));
				return false;
			}

			List<DetalleTipoParametro> detalleTipoParametros = new ArrayList<DetalleTipoParametro>();
			detalleTipoParametros = detalleTipoParametroList
					.getListaAutenticacion();

			for (DetalleTipoParametro detalleTipoParametro : detalleTipoParametros) {
				autenticacion = detalleTipoParametro.getDescripcion();
			}

			if (autenticacion.equals("AD")) {
				
				return this.autenticacionActiveDirectory();
			} else {
				return this.autenticacionBaseDatos();
			}
		}
		
		facesContext.addMessage(null, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, "Ingrese Usuario y Clave", null));
		return false;
	}

	public void adminMenus() {
		this.construirMenu.cargarMenu(this.credentials.getUsername());
		this.construirMenu.eliminarVarSessionMenu();
	}
	
	public void buscarNombreEmpresa(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getListaEmpresa();
		
		Constantes.NombreEmpresa = detalleTipoParametro.getDescripcion();
	}
	
	public void buscarRutaFuentes(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getRutaFuentes();
		
		Constantes.RUTA_LISTADOS_FUENTES = detalleTipoParametro.getDescripcion();
	}

	public void buscarRutaReportes(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getRutaReportes();
		
		Constantes.RUTA_LISTADOS_SERVIDOR = detalleTipoParametro.getDescripcion();
	}

	public void buscarRutaLogo(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getRutaLogo();
		
		Constantes.LOGO = detalleTipoParametro.getDescripcion();
	}

	public void buscarRutaPlantillaNormal(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getRutaPlantillaNormal();
		
		Constantes.RUTA_PLANTILLA = detalleTipoParametro.getDescripcion();
	}
	
	public boolean autenticacionActiveDirectory() {
		
		List<DetalleTipoParametro> detalleTipoParametros = detalleTipoParametroList.getListaParametrosActiveDirectory();
		
		String dominio = detalleTipoParametros.get(0).getDescripcion();
		String host = detalleTipoParametros.get(1).getDescripcion();
		
//		log.info("======================= dominio " + dominio);
//		log.info("======================= host " + host);
		
		ConeccionLdap coneccionLdap = new ConeccionLdap();
		String usuarioAd = coneccionLdap
				.authenticate(host, dominio,
						credentials.getUsername().toString(), credentials
								.getPassword().toString());
		if (usuarioAd.equals(null) || usuarioAd == null || usuarioAd == "") {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Clave o Usuario incorrecto",
					null));
			return false;
		} else {
			// DETERMINAR LA BASE DE DATOS QUE SE ESTA USANDO
			// PARA PODER CONTROLAR LAS SENTENCIAS SQL
			// DE LAS DIFERENTES BASES DE DATOS
			ControlBaseDatos.colocarBaseDatos();
			// FIN
			
			this.adminMenus();
			this.buscarNombreEmpresa();
			this.asignarRutas();
			return true;
		}
	}
	
	public void asignarRutas(){
		this.buscarRutaFuentes();
		this.buscarRutaReportes();
		this.buscarRutaLogo();
		this.buscarRutaPlantillaNormal();
	}

	public boolean autenticacionBaseDatos() {
		
		if (usuarios != null
				&& usuarios.getEstado() != null
				&& usuarios.getEstado().equals(
						Constantes.ESTADO_INACTIVO)) {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Usuario no se encuentra activado", null));
			return false;
		}
		if (usuarios != null
				&& usuarios.getFechaVencimiento().before(
						Fechas.fechaActual())) {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,
					"Fecha de Acceso Finalizada", null));
			return false;
		}
		
		String password=usuarios.getContrasena();
		String desifrado="";
		try{
			if(password.charAt(0)=='$'){
				password=password.substring(1,password.length());
				Cifrado cidfrado=new Cifrado();
				byte[] bx=cidfrado.hexStringToByteArray(password);
				desifrado=cidfrado.descifrar(bx);
			}else{
				desifrado=password;
			}
		}catch(Exception ex){
			desifrado=password;
		}				
		
		if (credentials.getPassword().equals(desifrado)) {
			// DETERMINAR LA BASE DE DATOS QUE SE ESTA USANDO
			// PARA PODER CONTROLAR LAS SENTENCIAS SQL
			// DE LAS DIFERENTES BASES DE DATOS
			ControlBaseDatos.colocarBaseDatos();
			// FIN

			this.adminMenus();
			this.buscarNombreEmpresa();
			this.asignarRutas();
			return true;
		} else {
			facesContext.addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR, "Clave incorrecta.", null));
			return false;
		}
	}
	
	public void eliminarVariableSesion() {
		this.construirMenu.eliminarVarSessionMenu();
		this.eliminarVariableSessionPanelMenu();
	}

	public void eliminarVariableSessionPanelMenu() {
		Contexts.getSessionContext().remove("panelMenuSesion");
	}

	public void logout() {
		this.eliminarVariableSesion();
		this.identity.logout();
	}

	public Usuarios getUsuario() {
		return usuarios;
	}

	public void setUsuario(Usuarios usuario) {
		usuarios = usuario;
	}
}