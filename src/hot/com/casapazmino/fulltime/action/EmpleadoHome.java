package com.casapazmino.fulltime.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.validator.InvalidStateException;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;

import com.casapazmino.fulltime.comun.Cifrado;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.Cargo;
import com.casapazmino.fulltime.model.Ciudad;
import com.casapazmino.fulltime.model.Contacto;
import com.casapazmino.fulltime.model.DeclaraBienes;
import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Discapacidad;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoGrupo;
import com.casapazmino.fulltime.model.EmpleadoHorario;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.EmpleadoTitulo;
import com.casapazmino.fulltime.model.HistLabo;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.ProgramaVacacion;
import com.casapazmino.fulltime.model.Reloj;
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.model.Titulo;
import com.casapazmino.fulltime.procesos.EmailAutomatico;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;
import com.casapazmino.fulltime.seguridad.action.UsuariosHome;
import com.casapazmino.fulltime.seguridad.model.Usuarios;

@Name("empleadoHome")
public class EmpleadoHome extends EntityHome<Empleado> {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	AuditoriaHome auditoriaHome;

	@Logger
	Log log;

	@In
	EntityManager entityManager;

	@In(create = true)
	EmpleadoHome empleadoHome;
	@In(create = true)
	EmpleadoGrupoHome empleadoGrupoHome;	
	@In(create = true)
	CargoHome cargoHome;
	@In(create = true)
	RelojHome relojHome;
	@In(create = true)
	DepartamentoHome departamentoHome;
	@In(create = true)
	DepartamentoList departamentoList;
	@In(create = true)
	DetalleTipoParametroHome detalleTipoParametroHome;
	@In(create = true)
	DetalleGrupoContratadoHome detalleGrupoContratadoHome;
	@In(create = true)
	CiudadHome ciudadHome;
	@In(create = true)
	UsuariosHome usuariosHome;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	@In(create = true)
	EmailAutomatico emailAutomatico;

	private String cadenaAnterior;
	private String cadenaActual;

	private boolean periodoUnico;

	// @In
	// private FacesMessages facesMessages;

	private List<Empleado> empleados = new ArrayList<Empleado>();
	private Long provincia;
	private List<SelectItem> listaProvincia;

	Titulo titulo;
	DetalleTipoParametro niveles;

	// new variables
	private String archivoNombre;
	private int numero_archivos = 1;

	private int accesoEmpleados;
	private boolean modificaRol;
	
	private Map<Asistencia, Long> atrasos = new  LinkedHashMap<Asistencia,Long>();
	private Map<Asistencia, Long> salidasAnticipadas = new  LinkedHashMap<Asistencia,Long>();
	private Map<Permiso, String> permisos = new  LinkedHashMap<Permiso,String>();
	private Map<SolicitudVacacion, String> vacaciones = new  LinkedHashMap<SolicitudVacacion,String>();
	private List<Asistencia> inasistencia = new ArrayList<Asistencia>();
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) {
			setCreatedMessage(expressions
					.createValueExpression("#{messages['mensaje.grabar']}"));
		}
		if (getUpdatedMessage() == null) {
			setUpdatedMessage(expressions
					.createValueExpression("#{messages['mensaje.actualizar']}"));
		}
		if (getDeletedMessage() == null) {
			setDeletedMessage(expressions
					.createValueExpression("#{messages['mensaje.eliminar']}"));
		}
	}

	public void setEmpleadoEmplId(Long id) {
		setId(id);
	}

	public Long getEmpleadoEmplId() {
		return (Long) getId();
	}

	@Override
	protected Empleado createInstance() {
		Empleado empleado = new Empleado();
		return empleado;
	}

	public void wire() {

		getInstance();

		usuariosHome.wire();

		this.setPeriodoUnico(gestionPermisoVacacion
				.buscarParametroPeriodoVacaciones());

		if (this.isManaged()) {
			setCadenaAnterior(this.getInstance().toString());

			this.usuariosHome.setId(this.getInstance().getUsuarios().getId());
			this.usuariosHome.find();

			this.usuariosHome.obtenerIdRol();
			this.usuariosHome.obtenetActivo();
			this.usuariosHome
					.setIdUserRol(usuariosHome.getUsuarioRol().getId());

			this.setAccesoEmpleados(gestionPermisoVacacion
					.buscarAccesoEmpleados());
			
			//*************************
			try{
				if(this.usuariosHome.getInstance().getContrasena().charAt(0)=='$'){
					Cifrado cidfrado=new Cifrado();
					String password=this.usuariosHome.getInstance().getContrasena();
					password=password.substring(1,password.length());
					byte[] bx=cidfrado.hexStringToByteArray(password);
					try {
						String desifrado=cidfrado.descifrar(bx);
						this.usuariosHome.getInstance().setContrasena(desifrado);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}catch(Exception ex){
				
			}		
			//*************************	

			// PERMITE DESACTIVAR EL PANEL DE USUARIO
			// PARA CONTROLAR QUE UN USUARIO QUE NO TENGA ACCESO A TODOS LOS
			// EMPLEDOS
			// NO PUEDA CAMBIAR EL ROL DEL EMPLEADO
			if (getAccesoEmpleados() != Constantes.ACCESO_TODOS) {
				this.setModificaRol(false);
			} else {
				this.setModificaRol(true);
			}

		} else {
			
			this.usuariosHome.getInstance().setFechaCreacion(Fechas.fechaActual());
			this.getInstance().setControlaVacacion(1);
			
			setCadenaAnterior("");
			this.setModificaRol(true);
		}

		Cargo cargo = cargoHome.getDefinedInstance();
		if (cargo != null) {
			getInstance().setCargo(cargo);
		}
		Reloj reloj = relojHome.getDefinedInstance();
		if (reloj != null) {
			getInstance().setReloj(reloj);
		}
		
		EmpleadoGrupo empleadoGrupo = empleadoGrupoHome.getDefinedInstance();
		if (empleadoGrupo != null) {
			getInstance().setEmpleadoGrupo(empleadoGrupo);
		}		
		Departamento departamento = departamentoHome.getDefinedInstance();
		if (departamento != null) {
			getInstance().setDepartamento(departamento);
		}
		DetalleTipoParametro detalleTipoParametroByEstadoCivil = detalleTipoParametroHome
				.getDefinedInstance();
		if (detalleTipoParametroByEstadoCivil != null) {
			getInstance().setDetalleTipoParametroByEstadoCivil(
					detalleTipoParametroByEstadoCivil);
		}
		DetalleTipoParametro detalleTipoParametroBySexo = detalleTipoParametroHome
				.getDefinedInstance();
		if (detalleTipoParametroBySexo != null) {
			getInstance().setDetalleTipoParametroBySexo(
					detalleTipoParametroBySexo);
		}
		DetalleTipoParametro detalleTipoParametroByEstado = detalleTipoParametroHome
				.getDefinedInstance();
		if (detalleTipoParametroByEstado != null) {
			getInstance().setDetalleTipoParametroByEstado(
					detalleTipoParametroByEstado);
		}

		DetalleGrupoContratado detalleGrupoContratado = detalleGrupoContratadoHome
				.getDefinedInstance();
		if (detalleGrupoContratado != null) {
			getInstance().setDetalleGrupoContratado(detalleGrupoContratado);
		}
		Ciudad ciudad = ciudadHome.getDefinedInstance();
		if (ciudad != null) {
			getInstance().setCiudad(ciudad);
		}

		Usuarios usuarios = usuariosHome.getDefinedInstance();
		if (usuarios != null) {
			getInstance().setUsuarios(usuarios);
		}
	}

	public boolean isWired() {
		// if (getInstance().getCargo() == null)
		// return false;
		// if (getInstance().getReloj() == null)
		// return false;
		// if (getInstance().getDepartamento() == null)
		// return false;
		// if (getInstance().getDetalleTipoParametroByEstadoCivil() == null)
		// return false;
		// if (getInstance().getDetalleTipoParametroBySexo() == null)
		// return false;
		// if (getInstance().getDetalleTipoParametroByEstado() == null)
		// return false;
		// if (getInstance().getHorario() == null)
		// return false;
		// if (getInstance().getDetalleGrupoContratado() == null)
		// return false;
		// if (getInstance().getCiudad() == null)
		// return false;
		// if (getInstance() == null) return false;
		return true;
	}
	
	public List<EmpleadoPeriodoVacacion> getListaEmpleadoPeridoVacacion() {
		EmpleadoPeriodoVacacionList empleadoPeriodoVacacionList = new EmpleadoPeriodoVacacionList();
		empleadoPeriodoVacacionList
				.getEmpleadoPeriodoVacacion()
				.getEmpleado()
				.setCodigoEmpleado(
						this.getInstance().getEmpleado().getCodigoEmpleado());

		return empleadoPeriodoVacacionList.getResultList();
	}

	public List<Empleado> getJefes() {
		this.empleados = new ArrayList<Empleado>();
		if (this.empleados.isEmpty()
				&& this.getInstance().getDepartamento() != null
				&& this.getInstance().getDepartamento().getDepaId() != null) {
			this.empleados.addAll(this.listaEmpleadoXDepartamento(this
					.getInstance().getDepartamento()));
		}

		return this.empleados;
	}

	@SuppressWarnings("unchecked")
	public List<Empleado> listaEmpleadoXDepartamento(Departamento depart) {
		List<Empleado> result = new ArrayList<Empleado>();
		try {
			String consulta = "SELECT empleado FROM Empleado empleado "
					+ "WHERE empleado.departamento.depaId=?1";
			Query query = this.getEntityManager().createQuery(consulta);
			query.setParameter(1, depart.getDepaId());
			result.addAll(query.getResultList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String validarEmpleado() {
		String mensajeError = "Ninguno";

		// if
		// (this.getInstance().getFechaIngreso().before(this.getInstance().getFechaNacimiento())){
		// return mensajeError = "Fecha ingreso Menor que fecha de Nacimiento";
		// }

		mensajeError = this.validarCodigoEmpleado();
		if (!mensajeError.equals("Ninguno")) {
			return mensajeError;
		}
		mensajeError = this.validarCedula();
		if (!mensajeError.equals("Ninguno")) {
			return mensajeError;
		}
		
		return mensajeError;
	}

	public String validarCodigoEmpleado() {
		List<Empleado> empleados = this.buscarEmpleadoCodigo();
		empleados.remove(this.getInstance());
		if (empleados.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}

	public String validarCedula() {
		List<Empleado> empleados = this.buscarEmpleadoCedula();
		empleados.remove(this.getInstance());
		if (empleados.size() != 0) {
			return "Cedula ya existe";
		}
		
		if(!validadorDeCedula(this.instance.getCedula())){
			return "Cedula incorrecta";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadoCodigo() {

		String codigoEmpleado = this.getInstance().getCodigoEmpleado();

		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.codigoEmpleado = (:codigoEmpleado)")
				.setParameter("codigoEmpleado", codigoEmpleado).getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Empleado> buscarEmpleadoCedula() {

		String cedulaEmpleado = this.getInstance().getCedula();

		return (List<Empleado>) entityManager
				.createQuery(
						"select e from Empleado e where e.cedula = (:cedulaEmpleado)")
				.setParameter("cedulaEmpleado", cedulaEmpleado).getResultList();
	}

	// Cambio Alvaro 21/10/2015
	public boolean activarGeneracionAutomatica() {

		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

		detalleTipoParametros = detalleTipoParametroList
				.getActivarGeneracionAutomaticaCodigo();
		String parametro = detalleTipoParametros.getDescripcion();

		if (parametro.toLowerCase().equals("activar")) {
			return true;
		} else {
			return false;
		}
	}

	public Long ultimoCodigoEmpleados() {

		String codigoEmpleado;

		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

		detalleTipoParametros = detalleTipoParametroList
				.getCodigoAutomaticoEmpleado();

		codigoEmpleado = detalleTipoParametros.getDescripcion();

		return Long.parseLong(codigoEmpleado);
	}
	
	public DetalleTipoParametro ultimoCodigoEmpleado() {

		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);

		return detalleTipoParametroList.getCodigoAutomaticoEmpleado();
	}	

	@Transactional
	@Override
	public String persist() {
		String mensajeError = "Ninguno";
		String persisted = null;
		Long codigoEmpleado;

		try {
			mensajeError = this.validarEmpleado();
			if (mensajeError.equals("Ninguno")) {

				String estado = usuariosHome.guardar();
				if (estado.equals("persisted")) {

					this.instance.setUsuarios(usuariosHome.getInstance());
					this.instance.setSueldo(this.instance.getCargo()
							.getSueldo());

					if (this.activarGeneracionAutomatica()) {
						
						DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();
						detalleTipoParametro = this.ultimoCodigoEmpleado();
						
						codigoEmpleado = Long.parseLong(detalleTipoParametro.getDescripcion());
						codigoEmpleado = codigoEmpleado + 1 ;
						
						this.getInstance().setCodigoEmpleado(
								Long.toString(codigoEmpleado));
						
						persisted = super.persist();
						
						// Actualiza el nuevo codigo en parametros para poder seguir incrementando en uno
						if (persisted.equals("persisted")) {
							detalleTipoParametro.setDescripcion(Long.toString(codigoEmpleado));
							detalleTipoParametroHome.update();
						} 
						
					} else {
						persisted = super.persist();
					}

					if (persisted.equals("persisted")) {
						
						this.setCadenaActual(this.getInstance().toString());
						auditoriaHome.asignarCampos("Empleado", "Insertar",
								cadenaActual, cadenaActual);
						auditoriaHome.persist();
						FacesMessages.instance().add(
								"#{messages['mensaje.grabar']}");
					}
					
				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessages.instance().add("Error al crear registro");
		}

		return persisted;
	}

	public void actualizarEmpleado() {
	try{
		
		super.update();
	
	} catch (InvalidStateException e) {
		log.info("++++++++++++++++++ EMPLEADO " + this.getInstance().getCodigoEmpleado());
	    for (InvalidValue invalidValue : e.getInvalidValues()) {
	        log.info("Instance of bean class: " + invalidValue.getBeanClass().getSimpleName() +
	                 " has an invalid property: " + invalidValue.getPropertyName() +
	                 " with message: " + invalidValue.getMessage());
	    }
	}
		
		
	}

	@Transactional
	@Override
	public String update() {
		String mensajeError = "Ninguno";
		String updated = null;
		try {
			mensajeError = this.validarEmpleado();
			if (mensajeError.equals("Ninguno")) {

				String estado = usuariosHome.actualizar();
				if (estado.equals("updated")) {
					// facesMessages.clear();
					FacesMessages.instance().clear();
					updated = super.update();

					if (updated.equals("updated")) {
						this.setCadenaActual(this.getInstance().toString());
						auditoriaHome.asignarCampos("Empleado", "Modificar",
								cadenaAnterior, cadenaActual);
						auditoriaHome.persist();
						FacesMessages.instance().add(
								"#{messages['mensaje.actualizar']}");
					}
				} else
					FacesMessages.instance().add(
							"Error al Actualizar - Usuario");
			} else {
				// facesMessages.add(mensajeError);
				FacesMessages.instance().add(mensajeError);
			}

		} catch (Exception e) {
			FacesMessages.instance().add("#{messages['error.actualizar']}");
			// facesMessages.add("#{messages['error.actualizar']}");
		}
		return updated;
	}

	@Transactional
	@Override
	public String remove() {
		String removed = null;
		try {
			this.eliminar_foto();
			super.remove();
			FacesMessages.instance().clear();
			removed = usuariosHome.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("Empleado", "Eliminar",
						cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessages.instance().add("Error al borrar registro");
		}

		return removed;
	}

	// new methods
	public String getArchivoNombre() {
		return archivoNombre;
	}

	public void setArchivoNombre(String archivoNombre) {
		this.archivoNombre = archivoNombre;
	}

	public void subirArchivo(UploadEvent event) throws Exception {

		this.archivoNombre = getInstance().getEmplId() + ".jpg";

		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

		detalleTipoParametros = detalleTipoParametroList.getRutaFotoEmpleado();
		String path = detalleTipoParametros.getDescripcion();

		String sourceFile = "" + event.getUploadItem().getFile();
		String destFile = path + this.archivoNombre;

		FileChannel origen = null;
		FileChannel destino = null;
		try {
			origen = new FileInputStream(sourceFile).getChannel();
			destino = new FileOutputStream(destFile).getChannel();

			long count = 0;
			long size = origen.size();
			while ((count += destino.transferFrom(origen, count, size - count)) < size)
				;
		} finally {
			if (origen != null) {
				origen.close();
			}
			if (destino != null) {
				destino.close();
			}
		}
		FacesMessages.instance().clear();
		FacesMessages.instance().add("La imagen se guardo correctamente!");

		// facesMessages.clear();
		// facesMessages.add("La imagen se guardo correctamente!");
	}

	public String URLFoto() throws IOException {
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

		detalleTipoParametros = detalleTipoParametroList.getRutaFotoEmpleado();
		String path = detalleTipoParametros.getDescripcion();

		path += getInstance().getEmplId() + ".jpg";

		return path;
	}
		
	private boolean validadorDeCedula(String cedula) {
		boolean cedulaCorrecta = false;
		
		try {
			if (cedula.length() == 10) {// ConstantesApp.LongitudCedula
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					// Coeficientes de validación cédula
					// El decimo digito se lo considera dígito verificador
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9,10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
						}
					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
						}
					else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
						} 
					else{
						cedulaCorrecta = false;
						}
				} else {
					cedulaCorrecta = false;
					}
			} else {
			cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
		cedulaCorrecta = false;
		} catch (Exception err) {
		System.out.println("Una excepcion ocurrio en el proceso de validadcion");
		cedulaCorrecta = false;
		}
		 
		if (!cedulaCorrecta) {
		System.out.println("La Cédula ingresada es Incorrecta");
		}
		return cedulaCorrecta;
	}
	
	
	public int getNumero_archivos() {
		return numero_archivos;
	}

	public void setNumero_archivos(int numero_archivos) {
		this.numero_archivos = numero_archivos;
	}

	// end new methods

	// new new methods
	public void valueChangedEventEmpleado(ValueChangeEvent event) {
		Departamento departamento = ((Departamento) event.getNewValue());
		this.getInstance().setEmpleado(departamento.getEmpleado());
	}
	
	public void historicos(){
		
		// Atrasos
		Long diferencia;
		
		HistLaboList modelo = (HistLaboList) Component.getInstance("histLaboList", true);
		List<Asistencia> tmpList = new ArrayList<Asistencia>();
		Date fechaDesde;
		List<HistLabo> listaHistoria = new ArrayList<HistLabo>();	
		modelo.getHistLabo().setEmpleado(this.getInstance());
		modelo.getHistLabo().setEstado(true);
		
		listaHistoria = modelo.getResultList();
		
		if (!listaHistoria.isEmpty()){	
			fechaDesde = listaHistoria.get(0).getFechaIngreso();	
		}else{
			fechaDesde = new Date();
		}	
			
		this.atrasos.clear();
		
		tmpList = emailAutomatico.RetrasoEntrada(fechaDesde, new Date());
		
		if(!tmpList.isEmpty()){
			for(Asistencia asistencia : tmpList){
				if (asistencia.getEmpleado().getEmplId() == this.getInstance().getEmplId()){
					diferencia = Fechas.calcularTiempo(asistencia.getFechaHoraHorario(), asistencia.getFechaHoraTimbre())/1000/60;
					atrasos.put(asistencia, diferencia);	
				}
			}
		}
		
		// Salidas Anticipadas
		
		tmpList.clear();
		this.salidasAnticipadas.clear();
		tmpList = emailAutomatico.SalidasAnticipadas(fechaDesde, new Date());
		
		if(!tmpList.isEmpty()){
			for(Asistencia asistencia : tmpList){
				if(asistencia.getEmpleado().getEmplId() == this.getInstance().getEmplId()){
					diferencia = Fechas.calcularTiempo(asistencia.getFechaHoraHorario(), asistencia.getFechaHoraTimbre())/1000/60;
					salidasAnticipadas.put(asistencia, diferencia);
				}

			}

		}
		
		// Permisos
		
		PermisoList modeloPermisoList = (PermisoList) Component.getInstance("permisoList", true);
		List<Permiso> listaPermisos = new ArrayList<Permiso>();
		
		modeloPermisoList.setAutorizado(null);
		modeloPermisoList.getPermiso().setEmpleadoByEmplId(this.getInstance());
		modeloPermisoList.getPermiso().setFechaDesde(fechaDesde);
		modeloPermisoList.setMaxResults(null);
		listaPermisos = modeloPermisoList.getResultList();
		
		if(!listaPermisos.isEmpty()){
			permisos.clear();
			for(Permiso permiso : listaPermisos){
				
				if(permiso.getEstadoActual().equals("0")){
				   permisos.put(permiso, "Pendiente");
				}
				
				if(permiso.getEstadoActual().equals("1")){
					   permisos.put(permiso, "Pre-Autorizado");
				}
				
				if(permiso.getEstadoActual().equals("2")){
					   permisos.put(permiso, "Autorizado");
				}
				
				if(permiso.getEstadoActual().equals("3")){
					   permisos.put(permiso, "No-Autorizado");
				}
				
			}		
		}
		
		// Vacaciones
		List<SolicitudVacacion> listaVacacion = new ArrayList<SolicitudVacacion>();
		SolicitudVacacionList VacacionesList = (SolicitudVacacionList) Component.getInstance("solicitudVacacionList", true);
		
		VacacionesList.setAutorizado(null);
		VacacionesList.getSolicitudVacacion().setEmpleadoByEmplId(this.getInstance());
		VacacionesList.getSolicitudVacacion().setFechaDesde(fechaDesde);
		VacacionesList.setMaxResults(null);
		
		listaVacacion=VacacionesList.getResultList();
		
		if(!listaVacacion.isEmpty()){
			vacaciones.clear();
			for(SolicitudVacacion vacacion : listaVacacion){
				
				if(vacacion.getEstado().equals("0")){
				   vacaciones.put(vacacion, "Pendiente");
				}
				
				if(vacacion.getEstado().equals("1")){
					   vacaciones.put(vacacion, "Pre-Autorizado");
				}

				if(vacacion.getEstado().equals("2")){
					   vacaciones.put(vacacion, "Autorizado");
				}
				
				if(vacacion.getEstado().equals("3")){
					   vacaciones.put(vacacion, "No-Autorizado");
				}
				
			}		
		
		}
		
		// Faltas
		AsistenciaList AsistenciaList = (AsistenciaList) Component.getInstance("asistenciaList", true);
		
		List<Asistencia> lista = new ArrayList<Asistencia>();
		
		
		AsistenciaList.getAsistencia().setEmpleado(this.getInstance());
		AsistenciaList.getAsistencia().setEstado("FT");
		AsistenciaList.getAsistencia().setFechaDesde(fechaDesde);
		AsistenciaList.getAsistencia().setFechaHasta(new Date());
		AsistenciaList.setMaxResults(null);
		inasistencia.clear();
		
		inasistencia = AsistenciaList.getResultList();
		
		log.info("nueva lista " + inasistencia + " otra cosa "  + this.getInstance());
		
	}	

	/**
	 * GET AND SET
	 * 
	 * @return
	 */
	public Empleado getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Asistencia> getAsistencias() {
		return getInstance() == null ? null : new ArrayList<Asistencia>(
				getInstance().getAsistencias());
	}

	public List<Contacto> getContactos() {
		return getInstance() == null ? null : new ArrayList<Contacto>(
				getInstance().getContactos());
	}

	public List<DeclaraBienes> getDeclaraBieneses() {
		return getInstance() == null ? null : new ArrayList<DeclaraBienes>(
				getInstance().getDeclaraBieneses());
	}

	public List<Discapacidad> getDiscapacidads() {
		return getInstance() == null ? null : new ArrayList<Discapacidad>(
				getInstance().getDiscapacidads());
	}

	public List<EmpleadoHorario> getEmpleadoHorarios() {
		return getInstance() == null ? null : new ArrayList<EmpleadoHorario>(
				getInstance().getEmpleadoHorarios());
	}

	public List<HistLabo> getHistLabos() {
		return getInstance() == null ? null : new ArrayList<HistLabo>(
				getInstance().getHistLabos());
	}

	public List<EmpleadoPeriodoVacacion> getEmpleadoPeriodoVacacions() {
		return getInstance() == null ? null
				: new ArrayList<EmpleadoPeriodoVacacion>(getInstance()
						.getEmpleadoPeriodoVacacions());
	}

	public List<EmpleadoTitulo> getEmpleadoTitulos() {
		return getInstance() == null ? null : new ArrayList<EmpleadoTitulo>(
				getInstance().getEmpleadoTitulos());
	}

	public List<Empleado> getEmpleados() {
		return getInstance() == null ? null : new ArrayList<Empleado>(
				getInstance().getEmpleados());
	}

	public List<Permiso> getPermisosForEmpEmplId() {
		return getInstance() == null ? null : new ArrayList<Permiso>(
				getInstance().getPermisosForEmpEmplId());
	}

	public List<Permiso> getPermisosForEmplId() {
		return getInstance() == null ? null : new ArrayList<Permiso>(
				getInstance().getPermisosForEmplId());
	}

	public List<ProgramaVacacion> getProgramaVacacions() {
		return getInstance() == null ? null : new ArrayList<ProgramaVacacion>(
				getInstance().getProgramaVacacions());
	}

	public List<SolicitudVacacion> getSolicitudVacacions() {
		return getInstance() == null ? null : new ArrayList<SolicitudVacacion>(
				getInstance().getSolicitudVacacionsForEmplId());
	}

	public Long getProvincia() {
		return provincia;
	}

	public void setProvincia(Long provincia) {
		this.provincia = provincia;
	}

	public List<SelectItem> getListaProvincia() {
		return listaProvincia;
	}

	public void setListaProvincia(List<SelectItem> listaProvincia) {
		this.listaProvincia = listaProvincia;
	}

	// *********************************

	public void eliminar_foto() {
		try {
			File ficheroFile = new File(URLFoto());
			ficheroFile.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCadenaAnterior() {
		return cadenaAnterior;
	}

	public void setCadenaAnterior(String cadenaAnterior) {
		this.cadenaAnterior = cadenaAnterior;
	}

	public String getCadenaActual() {
		return cadenaActual;
	}

	public void setCadenaActual(String cadenaActual) {
		this.cadenaActual = cadenaActual;
	}

	public boolean isPeriodoUnico() {
		return periodoUnico;
	}

	public void setPeriodoUnico(boolean periodoUnico) {
		this.periodoUnico = periodoUnico;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	public boolean isModificaRol() {
		return modificaRol;
	}

	public void setModificaRol(boolean modificaRol) {
		this.modificaRol = modificaRol;
	}

	// ********************************


	public Map<Asistencia, Long> getAtrasos() {
		return atrasos;
	}

	public void setAtrasos(Map<Asistencia, Long> atrasos) {
		this.atrasos = atrasos;
	}

	public Map<Asistencia, Long> getSalidasAnticipadas() {
		return salidasAnticipadas;
	}

	public void setSalidasAnticipadas(Map<Asistencia, Long> salidasAnticipadas) {
		this.salidasAnticipadas = salidasAnticipadas;
	}

	public Map<Permiso, String> getPermisos() {
		return permisos;
	}

	public void setPermisos(Map<Permiso, String> permisos) {
		this.permisos = permisos;
	}

	public Map<SolicitudVacacion, String> getVacaciones() {
		return vacaciones;
	}

	public void setVacaciones(Map<SolicitudVacacion, String> vacaciones) {
		this.vacaciones = vacaciones;
	}

	public List<Asistencia> getInasistencia() {
		return inasistencia;
	}

	public void setInasistencia(List<Asistencia> inasistencia) {
		this.inasistencia = inasistencia;
	}
}