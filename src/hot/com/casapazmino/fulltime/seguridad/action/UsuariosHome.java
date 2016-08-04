package com.casapazmino.fulltime.seguridad.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.action.EmpleadoHome;
import com.casapazmino.fulltime.comun.Cifrado;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.seguridad.model.Auditoria;
import com.casapazmino.fulltime.seguridad.model.Roles;
import com.casapazmino.fulltime.seguridad.model.UsuarioCiudad;
import com.casapazmino.fulltime.seguridad.model.Usuarios;
import com.casapazmino.fulltime.seguridad.model.UsuariosRoles;

@Name("usuariosHome")
public class UsuariosHome extends EntityHome<Usuarios> {

	private static final long serialVersionUID = 1L;
	
	@Logger
	Log log;
	
	@In
	EntityManager entityManager;

	@In(create = true)
	DetalleTipoParametroHome detalleTipoParametroHome;

	@In(create = true)
	EmpleadoHome empleadoHome;

	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;

	private String cadenaAnterior;
	private String cadenaActual;	

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}
	
	
	private Long idRol = Long.valueOf(0);
	private List<SelectItem> listRol;
	private RolesList rolesLista;
	private UsuariosRolesHome usuarioRolesHome;
	private Roles rol;
	private RolesHome rolHome;
	private Usuarios user;
	private UsuariosHome userHome;
	private Boolean checkEstado;
	private Long idEstado;
	private Long idUserRol;
	
	

	public void setUsuariosId(Long id) {
		setId(id);
	}

	public Long getUsuariosId() {
		return (Long) getId();
	}

	@Override
	protected Usuarios createInstance() {
		Usuarios usuarios = new Usuarios();
		return usuarios;
	}

	public void wire() {
		
		getInstance();
		//*************************
		try{
			if(this.instance.getContrasena().charAt(0)=='$'){
				Cifrado cidfrado=new Cifrado();
				String password=this.instance.getContrasena();
				password=password.substring(1,password.length());
				byte[] bx=cidfrado.hexStringToByteArray(password);
				try {
					String desifrado=cidfrado.descifrar(bx);
					this.instance.setContrasena(desifrado);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}catch(Exception ex){
			
		}		
		//*************************		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		this.llenarListaRoles();
		if (isManaged()) {
			this.obtenerIdRol();
			this.obtenetActivo();
			this.setIdUserRol(this.getUsuarioRol().getId());
		}
		
		DetalleTipoParametro estado = detalleTipoParametroHome
				.getDefinedInstance();
		if (estado != null) {
			getInstance().setEstado(estado);
		}
		
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null){
			getInstance().setEmpleado(empleado);
		}
		
		this.instance.setFechaCreacion(Fechas.fechaActual());
	}

	public boolean isWired() {
		return true;
	}

	public Usuarios getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Auditoria> getAuditorias() {
		return getInstance() == null ? null : new ArrayList<Auditoria>(
				getInstance().getAuditorias());
	}

	/*
	 * public List<Empleado> getEmpleados() { return getInstance() == null ?
	 * null : new ArrayList<Empleado>( getInstance().getEmpleados()); }
	 */

	public List<UsuarioCiudad> getUsuarioCiudads() {
		return getInstance() == null ? null : new ArrayList<UsuarioCiudad>(
				getInstance().getUsuarioCiudads());
	}

	public List<UsuariosRoles> getUsuariosRoleses() {
		return getInstance() == null ? null : new ArrayList<UsuariosRoles>(
				getInstance().getUsuariosRoleses());
	}

	@Transactional
	public String guardar() {
		//**************
		Cifrado cifrado=new Cifrado();
		try {
			String password="$"+cifrado.toHexadecimal(cifrado.cifrar(this.instance.getContrasena()));
			this.instance.setContrasena(password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//**************		
		
		this.rol = new Roles();
		this.rolHome = new RolesHome();
		this.user = new Usuarios();
		this.userHome = new UsuariosHome();
		UsuariosRoles usersRoles = new UsuariosRoles();
		try {
			this.rol.setId(this.getIdRol());
			this.rolHome.setRolesId(rol.getId());
			this.rolHome.find();
			DetalleTipoParametro estado = new DetalleTipoParametro();
			estado.setDtpaId(this.asignarActivo());
			this.getInstance().setEstado(estado);
			String est = this.persist();
						
			if (est.equals("persisted")){
				
				this.userHome.setUsuariosId(this.getInstance().getId());
				this.userHome.find();

				usersRoles.setRoles(rol);
				usersRoles.setUsuarios(this.userHome.getInstance());
				return this.guardarUserRoles(usersRoles);				
			} return "";

		} catch (Exception e) {
			// TODO: handle exception
			// log.info("ERROR EN GUARDAR: ", e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String validarUsuario(){
		String mensajeError = "Ninguno";

		mensajeError = this.validarCodigoUsuario();
		
		return mensajeError;
	}
	
	public String validarCodigoUsuario(){
		List<Usuarios> usuarios = this.buscarUsuarioCodigo();
		usuarios.remove(this.getInstance());
		if (usuarios.size() != 0) {
			return "Codigo Usuarios ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Usuarios> buscarUsuarioCodigo() {
		
		String codigoUsuario = this.getInstance().getUsuario();
		
		return (List<Usuarios>) entityManager
				.createQuery(
						"select u from Usuarios u where u.usuario = (:codigoUsuario)")
				.setParameter("codigoUsuario", codigoUsuario)
				.getResultList();
	}
	
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validarUsuario();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
//				if (persisted.equals("persisted")) {
//					this.setCadenaActual(this.getInstance().toString());
//					auditoriaHome.asignarCampos("Usuario", "Insertar", cadenaActual, cadenaActual);
//					auditoriaHome.persist();
//					facesMessages.add("#{messages['mensaje.grabar']}");					
//				}
			} else {
				facesMessages.add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("Error al crear registro");
		}
		return persisted;
	}
	
	@Override
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		try {
			mensajeError = this.validarUsuario();
			if (mensajeError.equals("Ninguno")) {
				facesMessages.clear();
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Usuarios", "Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					facesMessages.add("#{messages['mensaje.actualizar']}");
				}
			} else {
				facesMessages.add(mensajeError);
			}
		} catch (Exception e) {
			facesMessages.add("#{messages['error.actualizar']}");
		}
		return updated;
	}

	public String actualizar() {
		//**************
		Cifrado cifrado=new Cifrado();
		try {
			String password="$"+cifrado.toHexadecimal(cifrado.cifrar(this.instance.getContrasena()));
			this.instance.setContrasena(password);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//**************
		UsuariosRoles usersRoles = new UsuariosRoles();
		try {
			DetalleTipoParametro estado = new DetalleTipoParametro();
			estado.setDtpaId(this.asignarActivo());
			
			this.getInstance().setEstado(estado);
			
			String grabo = this.update();
			
			if (grabo.equals("updated")){
				usersRoles.getRoles().setId(this.getIdRol());
				usersRoles.getUsuarios().setId(this.getInstance().getId());
				usersRoles.setId(this.getIdUserRol());
				return this.actualizarUsuarioRoles(usersRoles);				
			} else return "";

		} catch (Exception e) {
			facesMessages.add("Error al actualizar usuario");
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String remove(){
		String removed = null;
		try {
			removed =  super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("Usuarios", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			facesMessages.add("#{messages['error.eliminar']}");
		}
		return removed;
	}

	private String guardarUserRoles(UsuariosRoles usersRoles) {
		this.usuarioRolesHome = new UsuariosRolesHome();
		this.usuarioRolesHome.setInstance(usersRoles);
		return this.usuarioRolesHome.persist();
	}

	private String actualizarUsuarioRoles(UsuariosRoles usersRoles) {
		this.usuarioRolesHome = new UsuariosRolesHome();
		this.usuarioRolesHome.setId(usersRoles.getId());
		this.usuarioRolesHome.find();

		this.usuarioRolesHome.getInstance().setRoles(usersRoles.getRoles());
		this.usuarioRolesHome.getInstance().setUsuarios(
				usersRoles.getUsuarios());
		return this.usuarioRolesHome.update();
	}

	public void llenarListaRoles() {
		this.listRol = new ArrayList<SelectItem>();
		this.rolesLista = new RolesList();
		try {
			if (listRol.isEmpty()) {
				for (Roles rol : rolesLista.getlistadoRoles()) {
					this.listRol.add(new SelectItem(rol.getId(), rol
							.getDescripcion()));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("ERROR EN LLENAR LISTA ROLES: ", e);
		}
	}

	public void obtenerIdRol() {
		try {
			String consulta = "SELECT userRol FROM UsuariosRoles userRol "
					+ "WHERE userRol.usuarios.id=?1";
			Query q = this.getEntityManager().createQuery(consulta);
			q.setParameter(1, this.getUsuariosId());
			UsuariosRoles usrol = new UsuariosRoles();
			if (!q.getResultList().isEmpty()) {
				for (UsuariosRoles ur : (List<UsuariosRoles>) q.getResultList()) {
					usrol = ur;
					this.setIdRol(usrol.getRoles().getId());
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.info("ERROR EN OBTENER ID ROL: ", e);
		}
	}

	public void obtenetActivo() {
		if (this.getInstance().getEstado().getDtpaId()
				.equals(Long.valueOf(Constantes.ESTADO_ACTIVO))) {
			this.setCheckEstado(true);
		} else {
			this.setCheckEstado(false);
		}
	}

	public Long asignarActivo() {

		if (this.getCheckEstado()) {
			this.setIdEstado(Long.valueOf(Constantes.ESTADO_ACTIVO));
		} else {
			this.setIdEstado(Long.valueOf(Constantes.ESTADO_INACTIVO));
		}
		return this.getIdEstado();
	}

	public UsuariosRoles getUsuarioRol() {
		String consulta = "SELECT usuariosRoles FROM UsuariosRoles usuariosRoles"
				+ " WHERE usuariosRoles.usuarios.id=?1";

		if (this.getIdRol().equals(null) || this.getIdRol().equals(0)) {
			consulta += " AND usuariosRoles.roles.id=?2";
		}
		Query query = this.getEntityManager().createQuery(consulta);
		query.setParameter(1, this.getInstance().getId());
		if (this.getIdRol().equals(null) || this.getIdRol().equals(0)) {
			query.setParameter(2, this.getIdRol());
		}
		for (UsuariosRoles userRol : (List<UsuariosRoles>) query
				.getResultList()) {
			return userRol;
		}
		this.usuarioRolesHome = new UsuariosRolesHome();

		return null;
	}

	public String getRoles() {

		for (UsuariosRoles ur : this.getUsuariosRoleses()) {
			RolesHome rolesHome = new RolesHome();
			rolesHome.setId(ur.getRoles().getId());
			rolesHome.find();
			return rolesHome.getInstance().getDescripcion();
		}
		return null;

	}

	public String getEstados() {
		DetalleTipoParametroHome estado = new DetalleTipoParametroHome();
		estado.setId(this.getInstance().getEstado().getDtpaId());
		estado.find();
		return estado.getInstance().getDescripcion();
	}

	public void verificarFechaCaducidad(ValueChangeEvent e)
			throws ValidatorException {

		try {
			Date fecha = (Date) e.getNewValue();
			if (fecha.before(Fechas.fechaActual())) {
				facesMessages.add("No puede eliminar", null);
			}
		} catch (Exception e2) {
			log.info(e2);
		}
	}

	/*
	 * GETERS AND SETERS
	 */

	public Long getIdRol() {
		return idRol;
	}

	public void setIdRol(Long idRol) {
		this.idRol = idRol;
	}

	public List<SelectItem> getListRol() {
		return listRol;
	}

	public void setListRol(List<SelectItem> listRol) {
		this.listRol = listRol;
	}

	public Boolean getCheckEstado() {
		return checkEstado;
	}

	public void setCheckEstado(Boolean checkEstado) {
		this.checkEstado = checkEstado;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public Long getIdUserRol() {
		return idUserRol;
	}

	public void setIdUserRol(Long idUserRol) {
		this.idUserRol = idUserRol;
	}

	public String getCadenaActual() {
		return cadenaActual;
	}

	public void setCadenaActual(String cadenaActual) {
		this.cadenaActual = cadenaActual;
	}

	public String getCadenaAnterior() {
		return cadenaAnterior;
	}

	public void setCadenaAnterior(String cadenaAnterior) {
		this.cadenaAnterior = cadenaAnterior;
	}
	
	public void actualizarBean(){
		super.update();
	}

}
