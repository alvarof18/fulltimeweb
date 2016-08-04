package com.casapazmino.fulltime.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

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

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.DepartamentoAutorizacion;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.ProgramaVacacion;
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.model.SolicitudVacacionAutorizacion;
import com.casapazmino.fulltime.procesos.SolicitudVacacionNivelesBean;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("solicitudVacacionHome")
public class SolicitudVacacionHome extends EntityHome<SolicitudVacacion> {

	private static final long serialVersionUID = 1L;

	@Logger
	Log log;

	@In
	EntityManager entityManager;

	@In(create = true)
	DetalleTipoParametroHome detalleTipoParametroHome;
	@In(create = true)
	EmpleadoHome empleadoHome;
	@In(create = true)
	EmpleadoPeriodoVacacionHome empleadoPeriodoVacacionHome;
	@In(create = true)
	AsistenciaList asistenciaList;
	@In(create = true)
	AsistenciaHome asistenciaHome;
	@In(create = true)
	HorarioHome horarioHome;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	@In(create = true)
	AuditoriaHome auditoriaHome;

	@In(create = true)
	DepartamentoAutorizacionList departamentoAutorizacionList;

	@In(create = true)
	SolicitudVacacionAutorizacionHome solicitudVacacionAutorizacionHome;

	@In(create = true)
	SolicitudVacacionAutorizacionList solicitudVacacionAutorizacionList;

	@In(create = true)
	DetalleTipoParametroList detalleTipoParametroList;
	
	@In(create = true)
	ProgramaVacacionList programaVacacionList;

	private String cadenaAnterior;
	private String cadenaActual;

	Fechas fechas = new Fechas();

	private boolean gestionaVacacion;
	private boolean autorizaVacacion;
	private boolean eliminaVacacion;

	private Double saldoVacaciones;
	private Long diasSolicitud;

	private int saldoDias;
	private int saldoHoras;
	private int saldoMinutos;

	private boolean periodoUnico;

	private Integer nivelesRequeridos;

	private Boolean autorizar;
	private Boolean preAutorizar;
	private Boolean noAutorizar;
	private Boolean eliminacion;

	private int accesoEmpleados;

	private Empleado empleado;

	private Boolean crearCorreo;
	private Boolean actualizarCorreo;
	private Boolean eliminarCorreo;
	private Boolean preAutorizarCorreo;
	private Boolean autorizarCorreo;
	private Boolean noAutorizarCorreo;

	static String cuerpo = "";

	private boolean ActualizaSolicitud = true;
	
	private List<ProgramaVacacion> listaProgramaVacacion;
	private Boolean restriccion;	

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

	public void setSolicitudVacacionSova(Long id) {
		setId(id);
	}

	public Long getSolicitudVacacionSova() {
		return (Long) getId();
	}

	@Override
	protected SolicitudVacacion createInstance() {
		SolicitudVacacion solicitudVacacion = new SolicitudVacacion();
		return solicitudVacacion;
	}

	public void wire() {
		getInstance();

		this.GestionAutorizacion();
		
		this.EstablecerRestriccion();

		this.EnvioCorreo();
		this.instance.setCorreo(false);

		ActualizaSolicitud = true;

		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
			this.EstablecerProgramacionVacacionesEmpleado(this.instance.getEmpleadoByEmplId());
			
		} else
			setCadenaAnterior("");

		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroHome
				.getDefinedInstance();
		if (detalleTipoParametro != null) {
			getInstance().setDetalleTipoParametro(detalleTipoParametro);
		}
		Empleado empleadoByEmpEmplId = empleadoHome.getDefinedInstance();
		if (empleadoByEmpEmplId != null) {
			getInstance().setEmpleadoByEmpEmplId(empleadoByEmpEmplId);
		}
		Empleado empleadoByEmplId = empleadoHome.getDefinedInstance();
		if (empleadoByEmplId != null) {
			getInstance().setEmpleadoByEmplId(empleadoByEmplId);
		}
		EmpleadoPeriodoVacacion empleadoPeriodoVacacion = empleadoPeriodoVacacionHome
				.getDefinedInstance();
		if (empleadoPeriodoVacacion != null) {
			getInstance().setEmpleadoPeriodoVacacion(empleadoPeriodoVacacion);
		}
		Horario horario = horarioHome.getDefinedInstance();
		if (horario != null) {
			getInstance().setHorario(horario);
		}

		// Determina si se esta usando uno o varios periodos de vacaciones
		this.setPeriodoUnico(gestionPermisoVacacion
				.buscarParametroPeriodoVacaciones());

		if (!isManaged()) {
			this.detalleTipoParametroHome.setId(Constantes.DECISION_NO);
			DetalleTipoParametro detalleTipoParametroNo = this.detalleTipoParametroHome
					.find();
			this.getInstance().setDetalleTipoParametro(detalleTipoParametroNo);

		} else {

			// GRABA EL NUMERO DE DIAS DE LA SOLICITUD ACTUAL
			// SE UTILIZA CUANDO SE ACTUALIZA UNA SOLICITUD CON FECHA ANTERIOR A
			// LA FECHA DE CARGA ACTUAL
			this.setDiasSolicitud(fechas.numeroDias(this.getInstance()
					.getFechaDesde(), this.getInstance().getFechaHasta()));

			this.setSaldoVacaciones(gestionPermisoVacacion
					.buscarSaldoVacaciones(this.getInstance()
							.getEmpleadoPeriodoVacacion()));

			this.setSaldoDias(gestionPermisoVacacion
					.saldoVacacionDias(getSaldoVacaciones()));
			this.setSaldoHoras(gestionPermisoVacacion
					.saldoVacacionHoras(getSaldoVacaciones()));
			this.setSaldoMinutos(gestionPermisoVacacion
					.saldoVacacionMinutos(getSaldoVacaciones()));

			if (!this.periodoUnico) {
				List<EmpleadoPeriodoVacacion> empleadoPeriodoVacaciones = new ArrayList<EmpleadoPeriodoVacacion>();
				empleadoPeriodoVacaciones = gestionPermisoVacacion
						.actualizarComboPeriodoVacaciones(this.getInstance()
								.getEmpleadoByEmplId());
			}

			this.setAutorizaVacacion(gestionPermisoVacacion
					.buscarAutorizaPermiso());
			this.setEliminaVacacion(gestionPermisoVacacion
					.buscarEliminarPermiso());
		}

		this.setGestionaVacacion(gestionPermisoVacacion.buscarVacacion());

		if (!isGestionaVacacion()) {

			if (!this.isManaged()) {
				Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
				this.getInstance().setEmpleadoByEmplId(empleado);
				this.getInstance().setEmpleadoByEmpEmplId(
						empleado.getEmpleado());
			
				EstablecerProgramacionVacacionesEmpleado(this.instance.getEmpleadoByEmplId());
			}

			if (this.periodoUnico) {
				EmpleadoPeriodoVacacion empleadoPeriodoVacacion1 = new EmpleadoPeriodoVacacion();
				empleadoPeriodoVacacion1 = gestionPermisoVacacion
						.seleccionarEmpleadoPeriodoVacacion(this.getInstance()
								.getEmpleadoByEmplId());
				this.getInstance().setEmpleadoPeriodoVacacion(
						empleadoPeriodoVacacion1);

				// gestionPermisoVacacion.buscarPeriodosVacacion(empleado);

				// MUESTRA SALDO DE VACACIONES CUANDO INGRESA COMO EMPLEADO
				this.setSaldoVacaciones(gestionPermisoVacacion
						.buscarSaldoVacaciones(empleadoPeriodoVacacion1));

				this.setSaldoDias(gestionPermisoVacacion
						.saldoVacacionDias(getSaldoVacaciones()));
				this.setSaldoHoras(gestionPermisoVacacion
						.saldoVacacionHoras(getSaldoVacaciones()));
				this.setSaldoMinutos(gestionPermisoVacacion
						.saldoVacacionMinutos(getSaldoVacaciones()));
			} else {

				List<EmpleadoPeriodoVacacion> empleadoPeriodoVacaciones = new ArrayList<EmpleadoPeriodoVacacion>();
				empleadoPeriodoVacaciones = gestionPermisoVacacion
						.actualizarComboPeriodoVacaciones(this.getInstance()
								.getEmpleadoByEmplId());
			}
		}

		// System.out.println("AUTTO......................................................VACA:"+this.isAutorizaVacacion());
		// System.out.println("DTP........................................................VACA:"+this.instance.getDetalleTipoParametro().getDtpaId());
	}

	// if (isGestionaVacacion() && isManaged()){
	// EmpleadoPeriodoVacacion empleadoPeriodoVacacion1 = new
	// EmpleadoPeriodoVacacion();
	// empleadoPeriodoVacacion1 =
	// gestionPermisoVacacion.seleccionarEmpleadoPeriodoVacacion(this.getInstance().getEmpleadoByEmplId());
	// this.getInstance().setEmpleadoPeriodoVacacion(empleadoPeriodoVacacion1);

	// this.setSaldoVacaciones(gestionPermisoVacacion.buscarSaldoVacaciones(this.getInstance().getEmpleadoPeriodoVacacion()));
	//
	// this.setSaldoDias(gestionPermisoVacacion.saldoVacacionDias(getSaldoVacaciones()));
	// this.setSaldoHoras(gestionPermisoVacacion.saldoVacacionHoras(getSaldoVacaciones()));
	// this.setSaldoMinutos(gestionPermisoVacacion.saldoVacacionMinutos(getSaldoVacaciones()));
	// }
	// }

	public boolean isWired() {
		// if (getInstance().getDetalleTipoParametro() == null)
		// return false;
		// if (getInstance().getEmpleado() == null)
		// return false;
		return true;
	}

	public SolicitudVacacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Asistencia> consultarAsistencia() {
		// PARAMETROS PARA LA CONSULTA EN LA TABLA ASISTENCIA
		asistenciaList
				.getAsistencia()
				.getEmpleado()
				.setCodigoEmpleado(
						this.getInstance().getEmpleadoByEmplId()
								.getCodigoEmpleado());
		asistenciaList.getAsistencia().setFechaDesde(
				this.getInstance().getFechaDesde());
		asistenciaList.getAsistencia().setFechaHasta(
				this.getInstance().getFechaHasta());

		AsistenciaList asistencias = new AsistenciaList();
		return asistencias.getListaAsistencias();
	}

	public int validarFechas() {
		List<Asistencia> listaAsistencia = consultarAsistencia();
		log.info("========================== listaAsistencia "
				+ listaAsistencia.size());
		if (listaAsistencia.size() != 0) {
			List<Asistencia> listaRegistrada = verificarRegistros(listaAsistencia);
			// SI LA LISTA ES IGUAL A 1
			// QUIERE DECIR QUE CONTIENE REGISTROS TIMBRADOS, VACACIONES O
			// PERMISOS
			// ENTONCES RETORNA 0
			if (listaRegistrada.size() == 0)
				return 1;
			else
				return 0;
		} else {
			return 2;
		}
	}

	public String validarSolicitudVacacion() {

		String mensajeError = "Ninguno";

		try {
			if (this.getInstance().getFechaDesde()
					.after(this.getInstance().getFechaHasta())) {
				mensajeError = "Fecha Desde menor que Fecha Hasta";
			}

			// double meses =
			// fechas.numeroDias(this.getInstance().getEmpleadoByEmplId().getFechaIngreso(),
			// this.getInstance().getFechaDesde()) / 30;
			double meses = fechas.numeroDias(this.getInstance()
					.getEmpleadoPeriodoVacacion().getFechaDesde(), this
					.getInstance().getFechaDesde()) / 30;

			// RESTAR CALENDARIO ACTUAL DE CALENDARIO INGRESO
			double diasProporcionalVacacion = meses
					* this.getInstance().getEmpleadoByEmplId()
							.getDetalleGrupoContratado().getTotalHoras();
			//
			// if (meses > 12){
			// diasProporcionalVacacion = diasProporcionalVacacion - (((int)
			// (diasProporcionalVacacion / 10)) * 10);
			// if (diasProporcionalVacacion == 0){
			// diasProporcionalVacacion = 10;
			// }
			// }
			//
			// // Si los meses son multiplos de 12 asignar 0 a
			// diasProporcionalVacacion
			// if ((int) (meses % 12) == 0){
			// diasProporcionalVacacion = 0;
			// }

			long diasVacacion = fechas.numeroDias(this.getInstance()
					.getFechaDesde(), this.getInstance().getFechaHasta());

			if (isManaged()) {
				this.setSaldoVacaciones(gestionPermisoVacacion
						.buscarSaldoVacaciones(this.getInstance()
								.getEmpleadoPeriodoVacacion())
						+ diasVacacion);
			} else {
				this.setSaldoVacaciones(gestionPermisoVacacion
						.buscarSaldoVacaciones(this.getInstance()
								.getEmpleadoPeriodoVacacion()));
			}

			double controlDiasVacacion = diasProporcionalVacacion
					+ this.getSaldoVacaciones();

			log.info("================================= this.getSaldoVacaciones() "
					+ this.getSaldoVacaciones());
			log.info("================================= diasVacacion "
					+ diasVacacion);
			log.info("================================= Meses " + meses);
			log.info("================================= diasProporcionalVacacion "
					+ diasProporcionalVacacion);
			log.info("================================= controlDiasVacacion  "
					+ controlDiasVacacion);

			if (controlDiasVacacion > this.getInstance().getEmpleadoByEmplId()
					.getDetalleGrupoContratado().getMaximoHoras()) {
				controlDiasVacacion = this.getInstance().getEmpleadoByEmplId()
						.getDetalleGrupoContratado().getMaximoHoras();
			}

			if (diasVacacion > controlDiasVacacion) {
				mensajeError = "Número días supera saldo vacaciones";
			}

			List<Permiso> permisos = new ArrayList<Permiso>();
			permisos = gestionPermisoVacacion.buscarPermisoDia(this
					.getInstance());
			if (permisos.size() != 0) {
				mensajeError = "Existen permisos con esta fecha";
				return mensajeError;
			}

			permisos = gestionPermisoVacacion.buscarPermisoFechaDesde(this
					.getInstance());
			if (permisos.size() != 0) {
				mensajeError = "Existen permisos con estas fechas";
				return mensajeError;
			}

			permisos = gestionPermisoVacacion.buscarPermisoFechaHasta(this
					.getInstance());
			if (permisos.size() != 0) {
				mensajeError = "Existen permisos con estas fechas";
				return mensajeError;
			}

			permisos = gestionPermisoVacacion.buscarPermisosEmpleado(this
					.getInstance());
			for (Permiso permiso : permisos) {
				// if (this.getInstance().getDias() == 0 &&
				// this.getInstance().getNumeroHoras() != 0){
				// log.info("============= lista de permisos horas " +
				// permiso.getFechaDesde() + "  " + permiso.getFechaHasta() +
				// "  " + this.getInstance().getFechaDesde() + "  " +
				// this.getInstance().getFechaHasta());
				// if(permiso.getFechaDesde().before(this.getInstance().getFechaDesde())
				// &&
				// permiso.getFechaDesde().after(this.getInstance().getFechaHasta())
				// ||
				// permiso.getFechaHasta().before(this.getInstance().getFechaDesde())
				// &&
				// permiso.getFechaHasta().after(this.getInstance().getFechaHasta())){
				// mensajeError = "Existen permisos con estas fechas";
				// return mensajeError;
				// }
				// } else if (this.getInstance().getDias() != 0 &&
				// this.getInstance().getNumeroHoras() == 0){
				if (this.getInstance().getFechaDesde()
						.after(permiso.getFechaDesde())
						&& this.getInstance().getFechaDesde()
								.before(permiso.getFechaHasta())
						|| this.getInstance().getFechaHasta()
								.after(permiso.getFechaDesde())
						&& this.getInstance().getFechaHasta()
								.before(permiso.getFechaHasta())) {
					mensajeError = "Existen permisos con estas fechas";
					return mensajeError;
				}
				// }
			}

			List<SolicitudVacacion> solicitudVacaciones = new ArrayList<SolicitudVacacion>();
			solicitudVacaciones = gestionPermisoVacacion
					.buscarVacacionesFechaDesde(this.getInstance());
			solicitudVacaciones.remove(this.getInstance());
			if (solicitudVacaciones.size() != 0) {
				mensajeError = "Existen vacaciones con estas fechas";
				return mensajeError;
			}

			solicitudVacaciones = gestionPermisoVacacion
					.buscarVacacionesFechaHasta(this.getInstance());
			solicitudVacaciones.remove(this.getInstance());
			if (solicitudVacaciones.size() != 0) {
				mensajeError = "Existen vacaciones con estas fechas";
				return mensajeError;
			}

			solicitudVacaciones = gestionPermisoVacacion
					.buscarVacacionesEmpleado(this.getInstance());
			solicitudVacaciones.remove(this.getInstance());
			for (SolicitudVacacion solicitudVacacion : solicitudVacaciones) {
				if (this.getInstance().getFechaDesde()
						.after(solicitudVacacion.getFechaDesde())
						&& this.getInstance().getFechaDesde()
								.before(solicitudVacacion.getFechaHasta())
						|| this.getInstance().getFechaHasta()
								.after(solicitudVacacion.getFechaDesde())
						&& this.getInstance().getFechaHasta()
								.before(solicitudVacacion.getFechaHasta())) {
					mensajeError = "Existen vacaciones con estas fechas";
					return mensajeError;
				}
			}
			
			mensajeError = ValidarSolicitudProgramacionVacacion();
			
			return mensajeError;
		} catch (Exception e) {
			FacesMessages.instance()
					.add("Error - Verifique Periodo Vacaciones");
			e.printStackTrace();
		}
		return mensajeError;
	}

	@Override
	public String persist() {
		String persisted = null;
		String mensajeError = this.validarSolicitudVacacion();

		// **********************************controla permisos de jefes
		Empleado empleado = this.instance.getEmpleadoByEmplId();
		Empleado jefe = this.instance.getEmpleadoByEmpEmplId();

		if (jefe != null) {
			if (empleado.getEmplId() == jefe.getEmplId()) {
				this.getInstance().setNivel(
						empleado.getDepartamento().getNivel());
				// log.info("..........................................SI ES JEFE");
			} else {
				this.getInstance().setNivel(jefe.getDepartamento().getNivel());
				// log.info("..........................................NO ES JEFE");
			}
		} else {
			this.getInstance().setNivel(empleado.getDepartamento().getNivel());
			this.getInstance().setEmpleadoByEmpEmplId(empleado);
		}
		// ***********************************fin controla permisos de jefes

		this.getInstance().setEstado("0");
		this.instance.setAprobacion(0);
		this.instance.setNivelRequerido(this.nivelesRequeridos);

		if (mensajeError.equals("Ninguno")) {
			// SOLICITUD VACACIONES NO TIENE UNA COLUMNA PARA EL FACTOR DE
			// DESCUENTO
			// SE DEBERIA AUMENTAR ESTA COLUMNA EN LA BASE DE DATOS
			// this.getInstance().setFactor(this.getInstance().getTipoPermiso().getFactorHoras());
			try {

				// ***
				this.getInstance().setEstado("0");
				this.instance.setAprobacion(0);
				this.instance.setNivelRequerido(this.nivelesRequeridos);
				// ***

				persisted = super.persist();

				if (persisted.equals("persisted")) {

					double numeroDias = fechas.numeroDias(this.getInstance()
							.getFechaDesde(), this.getInstance()
							.getFechaHasta());
					Date empleadoPeriodoVacacionfecha = this.gestionPermisoVacacion
							.fechaCargaVacacion(this.getInstance()
									.getEmpleadoByEmplId(), this.getInstance()
									.getEmpleadoPeriodoVacacion()
									.getFechaDesde());
					Double diasPerdidos = this.getInstance()
							.getEmpleadoPeriodoVacacion().getDiasPerdidos();

					// RESTA DIAS PERDIDOS CUANDO LA FECHA DEL PERMISO ES MENOR
					// QUE LA FECHA DE LA ULTIMA CARGA
//					if (this.getInstance().getFechaDesde()
//							.before(empleadoPeriodoVacacionfecha)) {
//						this.getInstance().getEmpleadoPeriodoVacacion()
//								.setDiasPerdidos(diasPerdidos - numeroDias);
//						empleadoPeriodoVacacionHome.update();
//					}

					// Envia Correo Electronico
					if (this.crearCorreo) {
						EnviarMensajeSolCreado();
					}

					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("SolicitudVacacion",
							"Insertar", cadenaActual, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add(
							"#{messages['mensaje.grabar']}");
				}
			} catch (Exception e) {
				e.printStackTrace();
				FacesMessages.instance().add(
						"Error - Verifique Periodo Vacaciones");
			}
		} else {
			FacesMessages.instance().add(mensajeError);
		}
		return persisted;
	}

	@Override
	public String update() {
		String updated = null;
		String mensajeError = this.validarSolicitudVacacion();

		// **********************************controla permisos de jefes
		Empleado empleado = this.instance.getEmpleadoByEmplId();
		Empleado jefe = this.instance.getEmpleadoByEmpEmplId();

		if (this.instance.getEstado().trim().equals("0")) {
			if (jefe != null) {
				if (empleado.getEmplId() == jefe.getEmplId()) {
					this.instance.setNivel(empleado.getDepartamento()
							.getNivel());
					// log.info("..........................................SI ES JEFE");
				} else {
					this.instance.setNivel(jefe.getDepartamento().getNivel());
					// log.info("..........................................NO ES JEFE");
				}
			} else {
				this.instance.setNivel(empleado.getDepartamento().getNivel());
				this.instance.setEmpleadoByEmpEmplId(empleado);
			}
		}
		// ***********************************fin controla permisos de jefes

		this.instance.setNivelRequerido(this.nivelesRequeridos);

		try {
			if (mensajeError.equals("Ninguno")) {

				updated = super.update();
				if (updated.equals("updated")) {
					Double diasPerdidos = this.getInstance()
							.getEmpleadoPeriodoVacacion().getDiasPerdidos();
					double numeroDias = fechas.numeroDias(this.getInstance()
							.getFechaDesde(), this.getInstance()
							.getFechaHasta());
					Date empleadoPeriodoVacacionfecha = this.gestionPermisoVacacion
							.fechaCargaVacacion(this.getInstance()
									.getEmpleadoByEmplId(), this.getInstance()
									.getEmpleadoPeriodoVacacion()
									.getFechaDesde());

					// RESTA DIAS PERDIDOS CUANDO LA FECHA DEL PERMISO ES MENOR
					// QUE LA FECHA DE LA ULTIMA CARGA					
//					if (this.getInstance().getFechaDesde()
//							.before(empleadoPeriodoVacacionfecha)) {
//						this.getInstance()
//								.getEmpleadoPeriodoVacacion()
//								.setDiasPerdidos(
//										(diasPerdidos + (this
//												.getDiasSolicitud() - numeroDias)));
//						empleadoPeriodoVacacionHome.update();
//					}

					if (this.actualizarCorreo) {
						// Envia Correo Electronico
						if (ActualizaSolicitud)
							EnviarMensajeSolActualizacion();
						// Fin Envia Correo Electronico
					}

					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("SolicitudVacacion",
							"Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add(
							"#{messages['mensaje.actualizar']}");
				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al actualizar vacaciones");
			e.printStackTrace();
		}
		return updated;
	}

	public String grabar() {
		return this.update();
	}

	@Transactional
	@Override
	public String remove() {
		String removed = null;
		double numeroDias = fechas.numeroDias(this.getInstance()
				.getFechaDesde(), this.getInstance().getFechaHasta());

		if (this.eliminarCorreo) {
			// Envia Correo Electronico
			EnviarMensajeSolEliminacion();
			// Fin Envia Correo Electronico
		}

		// Eliminar autorizaciones//
		List<SolicitudVacacionAutorizacion> solicitudVacacionAutorizacion = new ArrayList<SolicitudVacacionAutorizacion>();

		solicitudVacacionAutorizacionList.setSolicitudVacacion(this
				.getInstance());
		solicitudVacacionAutorizacion = solicitudVacacionAutorizacionList
				.getListaSolicitudVacacionAutorizacion();
		for (SolicitudVacacionAutorizacion s : solicitudVacacionAutorizacion) {
			solicitudVacacionAutorizacionHome.setInstance(s);
			solicitudVacacionAutorizacionHome.remove();
		}
		// Eliminar autorizaciones//

		if (this.getInstance().getDetalleTipoParametro().getDtpaId() == Constantes.DECISION_NO) {
			try {
				removed = super.remove();
				if (removed.equals("removed")) {
					Date empleadoPeriodoVacacionfecha = this.gestionPermisoVacacion
							.fechaCargaVacacion(this.getInstance()
									.getEmpleadoByEmplId(), this.getInstance()
									.getEmpleadoPeriodoVacacion()
									.getFechaDesde());
					Double diasPerdidos = this.getInstance()
							.getEmpleadoPeriodoVacacion().getDiasPerdidos();

					// ACTUALIZAR DIAS PERDIDOS CUANDO LA FECHA DEL PERMISO ES
					// MENOR QUE LA FECHA DE LA ULTIMA CARGA
					// CONTROLAR DIFERENCIA ENTRE AUTORIZADO Y NO AUTORIZADO
//					if (this.getInstance().getFechaDesde()
//							.before(empleadoPeriodoVacacionfecha)) {
//						this.getInstance().getEmpleadoPeriodoVacacion()
//								.setDiasPerdidos(diasPerdidos + numeroDias);
//						empleadoPeriodoVacacionHome.update();
//					}

					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("SolicitudVacacion",
							"Eliminar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add(
							"#{messages['mensaje.eliminar']}");

					SolicitudVacacionNivelesBean solicitudVacacionNiveles = (SolicitudVacacionNivelesBean) Component
							.getInstance("solicitudVacacionNiveles", true);
					solicitudVacacionNiveles.ConsultarSolicitudVacacion();
					return removed;
				}
			} catch (Exception e) {
				FacesMessages.instance().add("Error al eliminar solicitud");
				e.printStackTrace();
			}
		}

		try {
			List<Asistencia> asistencias = new ArrayList<Asistencia>();

			asistencias.clear();
			asistencias = this.buscarAsistencias();

			for (Asistencia asistencia : asistencias) {
				asistencia.setEstado(Constantes.ASISTENCIA_FALTA);
			}

			this.actualizarAsistenciaVacacion(asistencias);

			// RESTAURA EL ESTADO DEL SALDO DE VACACIONES A POSITIVO O NEGATIVO
			if (gestionPermisoVacacion.buscarSaldoVacaciones(this.getInstance()
					.getEmpleadoPeriodoVacacion()) + numeroDias < 0) {
				this.actualizarEstadoNegativo(new Long(-1));
			} else
				this.actualizarEstadoNegativo(new Long(0));

			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("SolicitudVacacion", "Eliminar",
						cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al eliminar solicitud");
			e.printStackTrace();
		}

		SolicitudVacacionNivelesBean solicitudVacacionNiveles = (SolicitudVacacionNivelesBean) Component
				.getInstance("solicitudVacacionNiveles", true);
		solicitudVacacionNiveles.ConsultarSolicitudVacacion();
		return removed;
	}

	// Metodo duplicado en permiso
	@SuppressWarnings("unchecked")
	public List<Asistencia> buscarAsistencias() {

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFechaDesde = Fechas.cambiarFormato(this.getInstance()
				.getFechaDesde(), "yyyy-MM-dd");
		String stringFechaHasta = Fechas.cambiarFormato(this.getInstance()
				.getFechaHasta(), "yyyy-MM-dd");

		Date fechaDesde = null;
		Date fechaHasta = null;
		try {
			fechaDesde = formato.parse(stringFechaDesde);
			fechaHasta = formato.parse(stringFechaHasta);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return (List<Asistencia>) entityManager
				.createQuery(
						"select a from Asistencia a where a.empleado.emplId = (:empleado) And "
								+ ControlBaseDatos.modificadorConvertirFecha
								+ "a.fechaHoraHorario) Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado",
						this.getInstance().getEmpleadoByEmplId().getEmplId())
				.setParameter("permisoDesde", fechaDesde)
				.setParameter("permisoHasta", fechaHasta).getResultList();
	}

	// Metodo duplicado en permiso
	@Transactional
	public String actualizarAsistenciaVacacion(List<Asistencia> asistencias) {
		AsistenciaHome asistenciaHome = (AsistenciaHome) Component.getInstance(
				"asistenciaHome", true);
		asistenciaHome.updateAsistencia(asistencias);
		return "persisted";
	}

	// Metodo duplicado en permiso
	public void actualizarEstadoNegativo(Long estadoSaldoVacaciones) {
		// VALIDAR PORQUE LA CONSULTA PUEDE TRAER MAS DE UN REGISTRO O NO PUEDE
		// TRAER NINGUNO
		List<EmpleadoPeriodoVacacion> empleadoPeriodoVacacions = this
				.buscarUltimoPeriodoVacacion();
		if (empleadoPeriodoVacacions.size() == 1) {
			EmpleadoPeriodoVacacion empleadoPeriodoVacacion = empleadoPeriodoVacacions
					.get(0);

			empleadoPeriodoVacacion.setEstado(estadoSaldoVacaciones);
			empleadoPeriodoVacacionHome.update();
		}
	}

	// Metodo duplicado en permiso
	// BUSCA LA ULTIMA FECHA EN PERIODO DE VACACIONES
	// PARA PODER RECORRER LA FECHA EN CASO DE QUE PERMISO TENGA ACTIVADA LA
	// CASILLA AFECTA_FECHA_VACACION
	public List<EmpleadoPeriodoVacacion> buscarUltimoPeriodoVacacion() {
		Date fechaMaxima = gestionPermisoVacacion
				.buscarFechaMaximaPeriodoVacacion(this.getInstance()
						.getEmpleadoByEmplId());

		EmpleadoPeriodoVacacionList empleadoPeriodoVacacionList = (EmpleadoPeriodoVacacionList) Component
				.getInstance("empleadoPeriodoVacacionList", true);
		empleadoPeriodoVacacionList
				.getEmpleadoPeriodoVacacion()
				.getEmpleado()
				.setEmplId(this.getInstance().getEmpleadoByEmplId().getEmplId());
		empleadoPeriodoVacacionList.getEmpleadoPeriodoVacacion().setFechaDesde(
				fechaMaxima);
		return empleadoPeriodoVacacionList.getListaEmpleadoPeriodoVacacion();
	}

	@Transactional
	public String NoAutorizarSolicitud() {
		// this.remove();
		// this.cambiarEstadoAutorizacion((long) 5);
		this.instance.setEstado("3");

		this.detalleTipoParametroHome.setId(Constantes.DECISION_NO);
		DetalleTipoParametro detalleTipoParametroNo = this.detalleTipoParametroHome
				.find();
		this.getInstance().setDetalleTipoParametro(detalleTipoParametroNo);
				
		String grabar = super.update();

		if (this.noAutorizarCorreo) {
			// Envia Correo Electronico
			EnviarMensajeSolNoAuto();
			// Fin Envia Correo Electronico
		}

		FacesMessages.instance().clear();
		FacesMessages.instance().add("Solicitud No Autorizada!");

		return grabar;
	}

	@Transactional
	public String AutorizarSolicitud() {

		ActualizaSolicitud = false;
		// TODO: validar porque sale jboss colocar excepcion		
		//String estado = this.grabar();
		
		String estado=this.validarSolicitudVacacion();
				
		ActualizaSolicitud = true;

		/*if (estado.equals("")) {
			return null;
		}*/
		
		if (!estado.equals("Ninguno")) {
			FacesMessages.instance().clear();
			FacesMessages.instance().add(estado);

			return null;
		}

		int ret = validarFechas();

		if (ret == 0) {
			FacesMessages.instance().add(
					"Fechas seleccionadas contienen registros de asistencia");
			return null;
		}

		switch (ret) {
		case 1:
			eliminarAsistencia();
			crearAsistencia();
			break;
		case 2:
			crearAsistencia();
			break;
		default:
			// no hace nada;
			// FacesMessages.instance().add("Fechas seleccionadas contienen registros de asistencia");
			break;
		}

		this.cambiarEstadoAutorizacion(Constantes.DECISION_SI);

		// *********new
		this.instance.setAprobacion(this.instance.getNivelRequerido());
		this.instance.setEstado("2");
		this.empleado = gestionPermisoVacacion.buscarEmpleado();

		SolicitudVacacionAutorizacion solicitudVacacionAutorizacion = new SolicitudVacacionAutorizacion();

		solicitudVacacionAutorizacion.setSolicitudVacacion(this.getInstance());
		solicitudVacacionAutorizacion.setEmpleado(empleado);
		solicitudVacacionAutorizacion.setNivel(this.instance.getNivel());

		solicitudVacacionAutorizacion.setEstado("0");

		solicitudVacacionAutorizacionHome
				.setInstance(solicitudVacacionAutorizacion);
		solicitudVacacionAutorizacionHome.persist();
		// *********new

		if (this.autorizarCorreo) {
			// Envia Correo Electronico
			EnviarMensajeSolAuto();
			// Fin Envia Correo Electronico
		}

		ActualizaSolicitud = false;
		estado = this.grabar();
		ActualizaSolicitud = true;

		FacesMessages.instance().clear();
		FacesMessages.instance().add("Solicitud Autorizada!");

		return estado;
	}

	@Transactional
	public String PreAutorizarSolicitud() {
		// this.cambiarEstadoAutorizacion((long) 5);
		// this.remove();
		// **Registrar personal que autoriza

		this.empleado = gestionPermisoVacacion.buscarEmpleado();

		SolicitudVacacionAutorizacion solicitudVacacionAutorizacion = new SolicitudVacacionAutorizacion();

		solicitudVacacionAutorizacion.setSolicitudVacacion(this.getInstance());
		solicitudVacacionAutorizacion.setEmpleado(empleado);
		solicitudVacacionAutorizacion.setNivel(this.instance.getNivel());
		solicitudVacacionAutorizacion.setEstado("0");

		solicitudVacacionAutorizacionHome
				.setInstance(solicitudVacacionAutorizacion);
		solicitudVacacionAutorizacionHome.persist();
		// **Registrar personal que autoriza

		// this.instance.setNivel((this.instance.getNivel())+1);
		this.instance.setAprobacion(this.instance.getNivel());
		Integer nivelAux = this.instance.getNivel();
		nivelAux = nivelAux + 1;
		this.instance.setNivel(nivelAux);
		this.instance.setEstado("1");
		
		this.detalleTipoParametroHome.setId(Constantes.DECISION_NO);
		DetalleTipoParametro detalleTipoParametroNo = this.detalleTipoParametroHome
				.find();
		this.getInstance().setDetalleTipoParametro(detalleTipoParametroNo);

		String grabar = super.update();

		if (this.preAutorizarCorreo) {
			// Envia Correo Electronico
			EnviarMensajeSolPreAuto();
			// Fin Envia Correo Electronico
		}

		FacesMessages.instance().clear();
		FacesMessages.instance().add("Solicitud Pre-Autorizada!");

		return grabar;
	}

	//Autorizacion por Bloques
	@SuppressWarnings("static-access")
	public String AutorizarSolicitudes(SolicitudVacacion sv) {
		
		String estado = "";
		ActualizaSolicitud = false;
		/////////////////Nuevo///////////////////////////////////////
		this.setDiasSolicitud(fechas.numeroDias(sv.getFechaDesde(), sv.getFechaHasta()));
		this.setCadenaAnterior(this.getInstance().toString());
		this.EnvioCorreo();
		this.instance.setCorreo(false);
		
		// obtener el nivel Requerido
		Integer nivelCiudad = this.getInstance().getEmpleadoByEmplId()
				.getCiudad().getNivel();
				DetalleTipoParametro detalleTipoParametros = detalleTipoParametroList
				.getNivelAprobacionSolicitudesVacaciones();
		Integer nivelContrato = Integer.parseInt(detalleTipoParametros
				.getDescripcion());

		if (nivelCiudad > 0)
			this.nivelesRequeridos = nivelCiudad;
		else
			this.nivelesRequeridos = nivelContrato;
		
		estado = this.AutorizarSolicitud();
		
		return estado;
	}

	
	public void cambiarEstadoAutorizacion(Long decision) {
		this.detalleTipoParametroHome.setId(decision);
		DetalleTipoParametro detalleTipoParametro = this.detalleTipoParametroHome
				.find();
		this.getInstance().setDetalleTipoParametro(detalleTipoParametro);
	}

	public void crearAsistencia() {
		List<Calendar> fechas = new ArrayList<Calendar>();
		List<Empleado> empleados = new ArrayList<Empleado>();
		List<DetalleHorario> detalleHorarios = new ArrayList<DetalleHorario>();
		// RECUPERA DETALLE HORARIO DE ACUERDO AL HORARIO SELECCIONADO

		detalleHorarios.addAll(this.getInstance().getHorario()
				.getDetalleHorarios());
		fechas = crearListFechas();
		empleados.add(this.getInstance().getEmpleadoByEmplId());

		asistenciaHome.crearListaInsertarAsistencia(empleados, fechas,
				detalleHorarios, Constantes.ASISTENCIA_VACACIONES);
		fechas.clear();
		detalleHorarios.clear();
		empleados.clear();
	}

	public void eliminarAsistencia() {
		List<Asistencia> listaAsistencia = consultarAsistencia();
		asistenciaHome.removeAsistencias(listaAsistencia);
	}

	public List<Calendar> crearListFechas() {
		List<Calendar> listaFechas = new ArrayList<Calendar>();

		Calendar fechaDesde = Calendar.getInstance();
		fechaDesde.setTime(this.getInstance().getFechaDesde());

		Calendar fechaHasta = Calendar.getInstance();
		fechaHasta.setTime(this.getInstance().getFechaHasta());

		for (Calendar c = fechaDesde; c.compareTo(fechaHasta) <= 0; c.add(
				Calendar.DATE, 1)) {
			Calendar fechaSiguiente = Calendar.getInstance();
			fechaSiguiente.setTime(fechaDesde.getTime());
			listaFechas.add(fechaSiguiente);
		}

		return listaFechas;
	}

	public List<Asistencia> verificarRegistros(List<Asistencia> listaAsistencia) {
		List<Asistencia> listaRegistrados = new ArrayList<Asistencia>();

		for (Asistencia asistencia : listaAsistencia) {
			if (asistencia.getEstado().equals(Constantes.ASISTENCIA_REGISTRADO)) {
				listaRegistrados.add(asistencia);
			}
		}
		return listaRegistrados;
	}

	public void valueChangedEventEmpleado(ValueChangeEvent event) {

		Empleado empleado = ((Empleado) event.getNewValue());
		
		this.EstablecerProgramacionVacacionesEmpleado(empleado);

		if (this.periodoUnico) {
			// gestionPermisoVacacion.buscarPeriodosVacacion(empleado);
			EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
			empleadoPeriodoVacacion = gestionPermisoVacacion
					.seleccionarEmpleadoPeriodoVacacion(empleado);
			this.getInstance().setEmpleadoPeriodoVacacion(
					empleadoPeriodoVacacion);
			this.setSaldoVacaciones(gestionPermisoVacacion
					.buscarSaldoVacaciones(empleadoPeriodoVacacion));

			this.setSaldoDias(gestionPermisoVacacion
					.saldoVacacionDias(getSaldoVacaciones()));
			this.setSaldoHoras(gestionPermisoVacacion
					.saldoVacacionHoras(getSaldoVacaciones()));
			this.setSaldoMinutos(gestionPermisoVacacion
					.saldoVacacionMinutos(getSaldoVacaciones()));
		} else {

			List<EmpleadoPeriodoVacacion> empleadoPeriodoVacaciones = new ArrayList<EmpleadoPeriodoVacacion>();
			empleadoPeriodoVacaciones = gestionPermisoVacacion
					.actualizarComboPeriodoVacaciones(empleado);

		}
		this.getInstance().setEmpleadoByEmplId(empleado);
		// this.getInstance().setEmpleadoByEmpEmplId(empleado.getEmpleado());
		Empleado jefe = empleado.getEmpleado();
		if (jefe != null)
			this.getInstance().setEmpleadoByEmpEmplId(jefe);
		else
			this.getInstance().setEmpleadoByEmpEmplId(empleado);
		
		this.diferenciaFechas();
	}

	public void valueChangedEventPeriodo(ValueChangeEvent event) {
		EmpleadoPeriodoVacacion empleadoPeriodoVacacion = ((EmpleadoPeriodoVacacion) event
				.getNewValue());
		this.setSaldoVacaciones(gestionPermisoVacacion
				.buscarSaldoVacaciones(empleadoPeriodoVacacion));

		this.setSaldoDias(gestionPermisoVacacion
				.saldoVacacionDias(getSaldoVacaciones()));
		this.setSaldoHoras(gestionPermisoVacacion
				.saldoVacacionHoras(getSaldoVacaciones()));
		this.setSaldoMinutos(gestionPermisoVacacion
				.saldoVacacionMinutos(getSaldoVacaciones()));
	}

	public boolean isGestionaVacacion() {
		return gestionaVacacion;
	}

	public void setGestionaVacacion(boolean gestionaVacacion) {
		this.gestionaVacacion = gestionaVacacion;
	}

	public Double getSaldoVacaciones() {
		return saldoVacaciones;
	}

	public void setSaldoVacaciones(Double saldoVacaciones) {
		this.saldoVacaciones = saldoVacaciones;
	}

	public int getSaldoDias() {
		return saldoDias;
	}

	public void setSaldoDias(int saldoDias) {
		this.saldoDias = saldoDias;
	}

	public int getSaldoHoras() {
		return saldoHoras;
	}

	public void setSaldoHoras(int saldoHoras) {
		this.saldoHoras = saldoHoras;
	}

	public int getSaldoMinutos() {
		return saldoMinutos;
	}

	public void setSaldoMinutos(int saldoMinutos) {
		this.saldoMinutos = saldoMinutos;
	}

	public boolean isPeriodoUnico() {
		return periodoUnico;
	}

	public void setPeriodoUnico(boolean periodoUnico) {
		this.periodoUnico = periodoUnico;
	}

	public boolean isAutorizaVacacion() {
		return autorizaVacacion;
	}

	public void setAutorizaVacacion(boolean autorizaVacacion) {
		this.autorizaVacacion = autorizaVacacion;
	}

	public boolean isEliminaVacacion() {
		return eliminaVacacion;
	}

	public void setEliminaVacacion(boolean eliminaVacacion) {
		this.eliminaVacacion = eliminaVacacion;
	}

	// enviar mail de vacacion

	private String RetornarFecha(Date date, String a) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(date);

		String year = "" + calendario.get(Calendar.YEAR);
		String month = "" + (calendario.get(Calendar.MONTH) + 1);
		String day = "" + calendario.get(Calendar.DATE);
		String nameDay = getDiaSemana(calendario.get(Calendar.DAY_OF_WEEK));

		if ((calendario.get(Calendar.MONTH)) < 10)
			month = "0" + month;

		if ((calendario.get(Calendar.DATE)) < 10)
			day = "0" + day;

		String fecha = "";

		if (a.equals("all")) {
			String hour = "" + calendario.get(Calendar.HOUR_OF_DAY);
			String minute = "" + calendario.get(Calendar.MINUTE);

			if (calendario.get(Calendar.HOUR_OF_DAY) < 10)
				hour = "0" + hour;

			if (calendario.get(Calendar.MINUTE) < 10)
				minute = "0" + minute;

			fecha = nameDay + " " + day + "/" + month + "/" + year + " " + hour
					+ ":" + minute;
		} else {
			fecha = nameDay + " " + day + "/" + month + "/" + year;
		}

		// System.out.println(fecha);
		return fecha;
	}

	private String getDiaSemana(int a) {
		String dia = "";

		switch (a) {
		case 0:
			dia = "Sábado";
			break;
		case 1:
			dia = "Domingo";
			break;
		case 2:
			dia = "Lunes";
			break;
		case 3:
			dia = "Martes";
			break;
		case 4:
			dia = "Miércoles";
			break;
		case 5:
			dia = "Jueves";
			break;
		case 6:
			dia = "Viernes";
			break;
		}

		return dia;
	}

	private String Cuerpo() {
		String empleado = this.instance.getEmpleadoByEmplId().getApellido()
				+ " " + this.instance.getEmpleadoByEmplId().getNombre();
		String cargo = this.instance.getEmpleadoByEmplId().getCargo()
				.getDescripcion();
		String departamento = this.instance.getEmpleadoByEmplId()
				.getDepartamento().getDescripcion();
		String contrato = this.instance.getEmpleadoByEmplId()
				.getDetalleGrupoContratado().getDescripcion();
		String motivo = "Vacaciones";
		String fechaSolicitud = "" + RetornarFecha(new Date(), "all");
		String fechaDesde = "";
		String fechaHasta = "";
		String nivelRequerido = "" + this.instance.getNivelRequerido();
		String aprobacion = "" + this.instance.getAprobacion();
		String estadoAux = this.instance.getEstado().trim();
		String estado = "";

		fechaDesde = RetornarFecha(this.instance.getFechaDesde(), "");
		fechaHasta = RetornarFecha(this.instance.getFechaHasta(), "");

		if (estadoAux.equals("0"))
			estado = "Pendiente";
		else if (estadoAux.equals("1"))
			estado = "Pre-Autorizado";
		else if (estadoAux.equals("2"))
			estado = "Autorizado";
		else if (estadoAux.equals("3"))
			estado = "No-Autorizado";
		else
			estado = "-";

		String cuerpo = "<center><h2><b>DATOS DEL SOLICITANTE</b></h2></center><br><br>"
				+

				"<b>Empleado:</b>&#32;"
				+ empleado
				+ "<br>"
				+ "<b>Cargo:</b>&#32;"
				+ cargo
				+ "<br>"
				+ "<b>Departamento:</b>&#32;"
				+ departamento
				+ "<br>"
				+ "<b>Contrato:</b>&#32;"
				+ contrato
				+ "<br><br>"
				+

				"<center><h2><b>INFORMACIÓN DE LA SOLICITUD</b></h2></center><br><br>"
				+

				"<b>Motivo:</b>&#32;"
				+ motivo
				+ "<br>"
				+ "<b>Fecha&#32;de&#32;Solicitud:</b>&#32;"
				+ fechaSolicitud
				+ "<br>"
				+ "<b>Desde:</b>&#32;"
				+ fechaDesde
				+ "<br>"
				+ "<b>Hasta:</b>&#32;"
				+ fechaHasta
				+ "<br>"
				+ "<b>Niveles&#32;requeridos&#32;de&#32;aprobaci&#243;n:</b>&#32;"
				+ nivelRequerido
				+ "<br>"
				+ "<b>Niveles&#32;aprobados:</b>&#32;"
				+ aprobacion
				+ "<br>"
				+ "<b>Estado:</b>&#32;" + estado + "";
		return cuerpo;
	}

	private void EnviarMailSimple(String cuerpo, String asunto) {
		EnviarMail enviarMail = new EnviarMail();
		enviarMail.enviar(cuerpo, instance.getEmpleadoByEmplId().getCorreo(),
				asunto, "E");
		/*******************ENVIAR CORREO ALETERNATIVO EMPLEADO********************
		String alternativo=instance.getEmpleadoByEmplId().getCorreoAlternativo();
		if(alternativo!=null){
			if(alternativo.length()>0){
				enviarMail.enviar(cuerpo, alternativo, asunto, "E");
			}
		}
		//*************************************************************************/
		enviarMail.enviar(cuerpo,
				instance.getEmpleadoByEmpEmplId().getCorreo(), asunto, "J");
		
		/*******************ENVIAR CORREO ALETERNATIVO JEFE********************
		alternativo=instance.getEmpleadoByEmpEmplId().getCorreoAlternativo();
		if(alternativo!=null){
			if(alternativo.length()>0){
				enviarMail.enviar(cuerpo, alternativo, asunto, "E");
			}
		}
		//**********************************************************************/
		
	}

	private void EnviarMailMultiple(String cuerpo, String asunto) {
		EnviarMail enviarMail = new EnviarMail();
		enviarMail.enviar(cuerpo, instance.getEmpleadoByEmplId().getCorreo(),
				asunto, "E");
		List<SolicitudVacacionAutorizacion> autorizaList = getListaPermisoAutoriza();
		int i = autorizaList.size();
		int j = 1;
		for (SolicitudVacacionAutorizacion pa : autorizaList) {
			if (j == i){
				enviarMail.enviar(cuerpo, pa.getEmpleado().getCorreo(), asunto,"J");
				/*******************ENVIAR CORREO ALETERNATIVO JEFE********************
				String alternativo=pa.getEmpleado().getCorreoAlternativo();
				if(alternativo!=null){
					if(alternativo.length()>0){
						enviarMail.enviar(cuerpo, alternativo, asunto, "E");
					}
				}
				//**********************************************************************/
			}
			else{
				enviarMail.enviar(cuerpo, pa.getEmpleado().getCorreo(), asunto,"E");
				/*******************ENVIAR CORREO ALETERNATIVO EMPLEADO********************
				String alternativo=pa.getEmpleado().getCorreoAlternativo();
				if(alternativo!=null){
					if(alternativo.length()>0){
						enviarMail.enviar(cuerpo, alternativo, asunto, "E");
					}
				}
				//*************************************************************************/
			}
			j++;
		}
	}

	public void EnviarMensajeSolCreado() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha creado la siguiente solicitud de vacaciones:<br><br>"
				+ Cuerpo() + "<br><br><b>Solicitado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Vacaciones";
		EnviarMailSimple(CuerpoMensaje, Asunto);
	}

	public void EnviarMensajeSolActualizacion() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar cambios de la siguiente solicitud de vacaciones:<br><br>"
				+ "<center><h1>SOLICITUD ANTERIOR</h1></center><br><br>"
				+ cuerpo
				+ "<br><br>"
				+ "<center><h1>SOLICITUD ACTUALIZADA</h1></center><br><br>"
				+ Cuerpo()
				+ "<br><br><b>Actualizado por: </b>"
				+ e
				+ "<br><br>";
		String Asunto = "Solicitud de Vacaciones Actualizada";

		if (this.instance.getEstado().trim().equals("0")) {
			EnviarMailSimple(CuerpoMensaje, Asunto);
		} else {
			EnviarMailMultiple(CuerpoMensaje, Asunto);
		}
		cuerpo = "";
	}

	public void EnviarMensajeSolEliminacion() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha eliminado la siguiente solicitud de vacaciones:<br><br>"
				+ Cuerpo() + "<br><br><b>Eliminado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Vacaciones Eliminada";

		if (this.instance.getEstado().equals("0")) {
			EnviarMailSimple(CuerpoMensaje, Asunto);
		} else {
			EnviarMailMultiple(CuerpoMensaje, Asunto);
		}
	}

	public void EnviarMensajeSolPreAuto() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha autorizado previamente la siguiente solicitud de vacaciones:<br><br>"
				+ Cuerpo()
				+ "<br><br><b>Pre-Autorizado por: </b>"
				+ e
				+ "<br><br>";
		String Asunto = "Solicitud de Vacaciones Pre-Autorizada";

		EnviarMailMultiple(CuerpoMensaje, Asunto);
	}

	public void EnviarMensajeSolAuto() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha autorizado la siguiente solicitud de vacaciones:<br><br>"
				+ Cuerpo() + "<br><br><b>Autorizado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Vacaciones Autorizada";

		EnviarMailMultiple(CuerpoMensaje, Asunto);
	}

	public void EnviarMensajeSolNoAuto() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que no se ha autorizado la siguiente solicitud de vacaciones:<br><br>"
				+ Cuerpo() + "<br><br><b>Negado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Vacaciones No-Autorizada";

		EnviarMailMultiple(CuerpoMensaje, Asunto);
	}

	//

	public void EnvioCorreo() {

		log.info("entro en correo solicitud");

		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		detalleTipoParametros = detalleTipoParametroList
				.getEnviarCorreoVacacion();

		String envio = detalleTipoParametros.getDescripcion();

		// String p="CR=F AC=F EL=T PR=F NO=F AU=T";
		Vector<Boolean> vector = new Vector<Boolean>();
		for (int i = 0; i < 6; i++) {
			vector.add(false);
		}

		boolean begin = false;
		String value = "";
		int j = 0;
		for (int i = 0; i < envio.length(); i++) {
			if (begin) {
				value = "" + envio.charAt(i);
				if (value.equals("T")) {
					vector.set(j, true);
				} else {
					if (value.equals("F")) {
						vector.set(j, false);
					}
				}
				begin = false;
				j++;
			}

			if (envio.charAt(i) == '=') {
				begin = true;
			}
		}
		this.crearCorreo = vector.get(0);
		this.actualizarCorreo = vector.get(1);
		this.eliminarCorreo = vector.get(2);
		this.preAutorizarCorreo = vector.get(3);
		this.noAutorizarCorreo = vector.get(4);
		this.autorizarCorreo = vector.get(5);
	}

	private List<GestionCorreo> listaGestionCorreo;

	public List<GestionCorreo> getListaGestionaCorreo() {

		if (listaGestionCorreo == null)
			listaGestionCorreo = new ArrayList<GestionCorreo>();

		listaGestionCorreo.clear();

		GestionCorreo gestionCorreo = new GestionCorreo();

		gestionCorreo.setCrear(this.crearCorreo);
		gestionCorreo.setActualizar(this.actualizarCorreo);
		gestionCorreo.setEliminar(this.eliminarCorreo);
		gestionCorreo.setPreAutorizar(this.preAutorizarCorreo);
		gestionCorreo.setNoAutorizar(this.noAutorizarCorreo);
		gestionCorreo.setAutorizar(this.autorizarCorreo);
		gestionCorreo.setLegalizar(false);

		listaGestionCorreo.add(gestionCorreo);

		return listaGestionCorreo;
	}

	// public boolean verAccion(){
	// String user = credentials.getUsername();
	//
	// UsuariosRolesList usuariosRolesList = (UsuariosRolesList)
	// Component.getInstance("usuariosRolesList", true);
	// UsuariosRoles usuariosRoles = new UsuariosRoles();
	//
	// String[] RESTRICTIONS =
	// {"usuariosRoles.usuarios.usuario = #{usuariosRolesList.usuario}",};
	//
	// usuariosRolesList.setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
	//
	//
	// //usuariosRolesList.usuariosRoles.usuarios.usuario}
	//
	// usuariosRolesList.setUsuario(user);
	//
	// usuariosRoles=usuariosRolesList.getSingleResult();
	//
	// if(usuariosRoles.getRoles().getDescripcion().equals("Administrador")){
	// return false;
	// }else{
	// return true;
	// }
	// }
	//
	// public boolean ReporteAccion(){
	// if(this.getInstance().getDetalleTipoParametro().getDtpaId()==3){
	// //log.info("..................................................VALIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: "+this.getInstance().getDetalleTipoParametro().getDtpaId());
	// if(this.getInstance().getCodigo()!=null){
	// if(this.getInstance().getCodigo().equals("")){
	// return false;
	// }else{
	// return true;
	// }
	// }else{
	// return false;
	// }
	//
	// }else{
	// return false;
	// }
	// }

	public void GestionAutorizacion() {

		if (this.isManaged()) {
			if (cuerpo.equals(""))
				cuerpo = Cuerpo();
			// *********visualizar nivel requerido
			Integer nivelCiudad = this.getInstance().getEmpleadoByEmplId()
					.getCiudad().getNivel();
			// Integer
			// nivelContrato=this.getInstance().getEmpleadoByEmplId().getDetalleGrupoContratado().getNivel();+
			DetalleTipoParametro detalleTipoParametros = detalleTipoParametroList
					.getNivelAprobacionSolicitudesVacaciones();
			Integer nivelContrato = Integer.parseInt(detalleTipoParametros
					.getDescripcion());

			if (nivelCiudad > 0)
				this.nivelesRequeridos = nivelCiudad;
			else
				this.nivelesRequeridos = nivelContrato;
			// ***********************************

			this.empleado = gestionPermisoVacacion.buscarEmpleado();

			departamentoAutorizacionList.getDepartamentoAutorizacion()
					.setEmpleado(this.empleado);
			List<DepartamentoAutorizacion> departamentoAutorizacion = new ArrayList<DepartamentoAutorizacion>();
			departamentoAutorizacion = departamentoAutorizacionList
					.getResultList();

			log.info("EMPL_ID!!!!!!!!!!!!!!!!!!!!!!!!!!!!:.................................:"
					+ empleado.getEmplId());
			log.info("VAMOS!!!!!!!!!!!!!!!!!!!!!!!!!!!!:.................................:"
					+ departamentoAutorizacion.size());

			this.setAccesoEmpleados(gestionPermisoVacacion
					.buscarAccesoEmpleados());

			if (departamentoAutorizacion.size() > 0) {

				Integer nivelDepartamento = empleado.getDepartamento()
						.getNivel();
				// Integer
				// nivelTipoPermiso=this.getInstance().getTipoPermiso().getNivel();
				Integer nivelPermiso = this.getInstance().getNivel();
				// Integer
				// nivelCiudad=this.getInstance().getEmpleadoByEmplId().getCiudad().getNivel();
				Integer nivelAprobacion = this.getInstance().getAprobacion();

				String est = this.getInstance().getEstado().trim();

				if (nivelCiudad > 0)
					nivelContrato = nivelCiudad;

				if (est.equals("0") || est.equals("1")) {

					// ********************************************recorrer
					// listado
					Departamento departamentoPermisoEmpleado = RetornoDepartamento(
							this.instance.getEmpleadoByEmplId()
									.getDepartamento(), nivelPermiso);
					if ((departamentoPermisoEmpleado == null))
						log.info("================================================== departamentoPermisoEmpleado null");
					else
						log.info("================================================== departamentoPermisoEmpleado not null");
					if (departamentoPermisoEmpleado != null) {
						log.info("================================================== departamentoPermisoEmpleado: "
								+ departamentoPermisoEmpleado.getDepaId());
						log.info("================================================== nivelPermiso: "
								+ nivelPermiso);
						log.info("================================================== ");
						for (DepartamentoAutorizacion da : departamentoAutorizacion) {
							Departamento departamento = da.getDepartamento();
							log.info("................................ departamento.getDepaId(): "
									+ departamento.getDepaId());
							log.info("................................ departamento.getNivel(): "
									+ departamento.getNivel());

							if (departamento.getDepaId().equals(
									departamentoPermisoEmpleado.getDepaId())
									&& (departamento.getNivel()
											.equals(nivelPermiso))) {
								nivelDepartamento = nivelPermiso;
								break;
							}
						}
						log.info("================================================== ");
					}
					// **************************************************fin
					// recorrer listado

					Integer auxNivelAprobacion = nivelAprobacion + 1;

					if (est.equals("0") && nivelDepartamento > 1
							&& nivelContrato > 1) {
						Integer nivelRequerido = this.getInstance()
								.getNivelRequerido();
						if (nivelPermiso > nivelRequerido)
							auxNivelAprobacion = nivelContrato;
						else
							auxNivelAprobacion = nivelDepartamento;
					}

					// Departamento
					// depSup=empleado.getDepartamento().getDepartamento();

					Departamento depSup = null;
					if (departamentoPermisoEmpleado != null) {
						depSup = departamentoPermisoEmpleado.getDepartamento();
					}

					if (((auxNivelAprobacion.equals(nivelContrato)) || (depSup == null))
							&& (nivelPermiso.equals(nivelDepartamento))) {
						this.autorizar = true;
						this.preAutorizar = false;
						this.noAutorizar = true;
					} else {
						if ((auxNivelAprobacion.equals(nivelPermiso))
								&& (depSup != null)
								&& (nivelPermiso.equals(nivelDepartamento))) {
							this.autorizar = false;
							this.preAutorizar = true;
							this.noAutorizar = true;
						} else {
							this.autorizar = false;
							this.preAutorizar = false;
							this.noAutorizar = false;
						}
					}

				} else {
					this.autorizar = false;
					this.preAutorizar = false;
					this.noAutorizar = false;
				}

				if (nivelDepartamento.equals(nivelPermiso) || est.equals("0")) {
					this.eliminacion = true;
					// this.actualizacion=true;
				} else {
					this.eliminacion = false;
					// this.actualizacion=false;
				}

			} else {
				this.autorizar = false;
				this.preAutorizar = false;
				this.noAutorizar = false;

				String est = this.getInstance().getEstado().trim();

				if (est.equals("0")) {
					this.eliminacion = true;
					// this.actualizacion=true;
				} else {
					this.eliminacion = false;
					// this.actualizacion=false;
				}

			}

			if (getAccesoEmpleados() != Constantes.ACCESO_INDIVIDUAL
					&& getAccesoEmpleados() != Constantes.ACCESO_SUBORDINADOS) {
				this.eliminacion = true;
				// this.actualizacion=true;
			}

		} else {
			this.autorizar = false;
			this.preAutorizar = false;
			this.noAutorizar = false;

			this.eliminacion = false;
			// this.actualizacion=false;
		}
	}

	public Boolean getAutorizar() {
		return autorizar;
	}

	public void setAutorizar(Boolean autorizar) {
		this.autorizar = autorizar;
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

	public Boolean getEliminacion() {
		return eliminacion;
	}

	public void setEliminacion(Boolean eliminacion) {
		this.eliminacion = eliminacion;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
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

	public Long getDiasSolicitud() {
		return diasSolicitud;
	}

	public void setDiasSolicitud(Long diasSolicitud) {
		this.diasSolicitud = diasSolicitud;
	}

	public Integer getNivelesRequeridos() {
		// ***

		Empleado empleado = this.instance.getEmpleadoByEmplId();
			try {
				Integer nivelCiudad = empleado.getCiudad().getNivel();
				// Integer
				// nivelContrato=empleado.getDetalleGrupoContratado().getNivel();
				DetalleTipoParametro detalleTipoParametros = detalleTipoParametroList
						.getNivelAprobacionSolicitudesVacaciones();
				Integer nivelContrato = Integer.parseInt(detalleTipoParametros
						.getDescripcion());
	
				if (nivelCiudad > 0)
					this.nivelesRequeridos = nivelCiudad;
				else
					this.nivelesRequeridos = nivelContrato;
			} catch (Exception ex) {
				ex.printStackTrace();
			
		// ***
		}
		return nivelesRequeridos;
	}

	public List<SolicitudVacacionAutorizacion> getListaPermisoAutoriza() {

		List<SolicitudVacacionAutorizacion> lista = new ArrayList<SolicitudVacacionAutorizacion>();

		solicitudVacacionAutorizacionList.setSolicitudVacacion(this
				.getInstance());
		lista = solicitudVacacionAutorizacionList
				.getListaSolicitudVacacionAutorizacion();

		return lista;
	}

	public void setNivelesRequeridos(Integer nivelesRequeridos) {
		this.nivelesRequeridos = nivelesRequeridos;
	}

	public Boolean getCrearCorreo() {
		return crearCorreo;
	}

	public void setCrearCorreo(Boolean crearCorreo) {
		this.crearCorreo = crearCorreo;
	}

	public Boolean getActualizarCorreo() {
		return actualizarCorreo;
	}

	public void setActualizarCorreo(Boolean actualizarCorreo) {
		this.actualizarCorreo = actualizarCorreo;
	}

	public Boolean getEliminarCorreo() {
		return eliminarCorreo;
	}

	public void setEliminarCorreo(Boolean eliminarCorreo) {
		this.eliminarCorreo = eliminarCorreo;
	}

	public Boolean getPreAutorizarCorreo() {
		return preAutorizarCorreo;
	}

	public void setPreAutorizarCorreo(Boolean preAutorizarCorreo) {
		this.preAutorizarCorreo = preAutorizarCorreo;
	}

	public Boolean getAutorizarCorreo() {
		return autorizarCorreo;
	}

	public void setAutorizarCorreo(Boolean autorizarCorreo) {
		this.autorizarCorreo = autorizarCorreo;
	}

	public Boolean getNoAutorizarCorreo() {
		return noAutorizarCorreo;
	}

	public void setNoAutorizarCorreo(Boolean noAutorizarCorreo) {
		this.noAutorizarCorreo = noAutorizarCorreo;
	}
	
	public List<ProgramaVacacion> getListaProgramaVacacion() {
		if (listaProgramaVacacion == null) {
			listaProgramaVacacion = new ArrayList <ProgramaVacacion>();
		}
		return listaProgramaVacacion;
	}

	public void setListaProgramaVacacion(List<ProgramaVacacion> listaProgramaVacacion) {
		this.listaProgramaVacacion = listaProgramaVacacion;
	}

	public Boolean getRestriccion() {
		return restriccion;
	}

	public void setRestriccion(Boolean restriccion) {
		this.restriccion = restriccion;
	}
	

	private Departamento RetornoDepartamento(Departamento departamento,
			int nivel) {
		Departamento departamentoAux = departamento;
		int i = (departamento.getNivel());
		if (i >= nivel) {
			i = nivel;
		}
		while (i < nivel) {
			if (departamentoAux != null) {
				departamentoAux = departamentoAux.getDepartamento();
			} else {
				departamentoAux = null;
				break;
			}
			i++;
		}
		return departamentoAux;
	}
	
	public void diferenciaFechas() {
		
		if (this.getInstance().getFechaDesde() != null
				&& this.getInstance().getFechaHasta() != null) {
			
			int dias = (int) fechas.numeroDias(this.instance.getFechaDesde(),
					this.instance.getFechaHasta());					
			

			this.instance.setDiasLaborables(dias);
			this.instance.setDiasLibres(0);			
			
			
			//Controlar dias laborables y libres
			try{
				
				ValidacionAdicionalLibreFeriados();
				
			}catch(Exception ex){
				InvalidValue iv = new InvalidValue(
						"Hubo un problema en la verificacion de dias laborables!",
						null, null, null, null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}				
		}	
	}
	
	private void ValidacionAdicionalLibreFeriados(){
		
		log.info("------------------------------------ENTRO A INFORMACION ADICIONAL");
		
		String mensaje="Ninguno";
		
		Empleado empleado=this.instance.getEmpleadoByEmplId();
		
		DetalleGrupoContratado detalleGrupo=empleado.getDetalleGrupoContratado();
				
		if(detalleGrupo.getActivarRegla()){
				
			if( detalleGrupo.getSolicitudVacacion() ){

				int countdiasLaborables=0;
				int countdiasLibres=0;
				int countdiasFeriados=0;
				int countdiasRecuperables=0;
				
				int totaldiasLaborables=0;
				int totaldiasLibres=0;
				
				ArrayList<Long> listaCiudades=new ArrayList<Long>();
				listaCiudades.add(empleado.getCiudad().getCiudId());
				
				Date inicio=this.getInstance().getFechaDesde();
				Date fin=this.getInstance().getFechaHasta();
				
				while(inicio.compareTo(fin)==0 || inicio.before(fin)){
					Calendar index=Calendar.getInstance();
					index.setTime(inicio);
						
					int diaSemana=index.get(Calendar.DAY_OF_WEEK);
						
					if(diaSemana>=2 && diaSemana<=6){
						countdiasLaborables++;	
					}else{
						countdiasLibres++;
					}
						
					index.add(Calendar.DATE, 1);					
					inicio=index.getTime();
				}
				
				
				//Verificar Feriados
				if(detalleGrupo.getActivarFeriados()){
					List<CiudadFeriado> ciudades=RetornarCiudadFeriado(this.getInstance().getFechaDesde(),this.getInstance().getFechaHasta(),listaCiudades);					
					for(CiudadFeriado cf: ciudades){
						Calendar index=Calendar.getInstance();
						index.setTime(cf.getFeriado().getFecha());
						
						int diaSemana=index.get(Calendar.DAY_OF_WEEK);
						log.info("FERIADO::::::::::::::::::::::::::::::::"+diaSemana);
						if(diaSemana>=2 && diaSemana<=6){
							countdiasFeriados++;
						}
					}
				}
				//*************************************
				
				//Verificar Recuperables
				if(detalleGrupo.getActivarRecuperable()){
					List<CiudadFeriado> ciudades=RetornarCiudadRecuperable(this.getInstance().getFechaDesde(),this.getInstance().getFechaHasta(),listaCiudades);
					
					for(CiudadFeriado cf: ciudades){
						Calendar index=Calendar.getInstance();
						index.setTime(cf.getFeriado().getFechaRecupera());
						
						int diaSemana=index.get(Calendar.DAY_OF_WEEK);
						
						log.info("RECUPERABLE::::::::::::::::::::::::::::::::"+diaSemana);
						
						if(diaSemana==7 || diaSemana==1){
							countdiasRecuperables++;
						}
					}
				}
				//*************************************
				
				totaldiasLaborables=countdiasLaborables+countdiasRecuperables-countdiasFeriados;
				totaldiasLibres=countdiasLibres+countdiasFeriados-countdiasRecuperables;
				
				this.getInstance().setDiasLaborables(totaldiasLaborables);
				this.getInstance().setDiasLibres(totaldiasLibres);
					
			}		
			
		}
	}
		
	//Retorna feriados
	private List<CiudadFeriado> RetornarCiudadFeriado(Date fechaDesde, Date fechaHasta, ArrayList<Long> ciudades){
		CiudadFeriadoList ciudadFeriadoList=(CiudadFeriadoList) Component.getInstance("ciudadFeriadoList", true);
		
		ciudadFeriadoList.setFeriadoDesde(null);
		ciudadFeriadoList.setFeriadoHasta(null);
		ciudadFeriadoList.setRecuperableDesde(null);
		ciudadFeriadoList.setRecuperableHasta(null);
		
		ciudadFeriadoList.setFeriadoDesde(fechaDesde);
		ciudadFeriadoList.setFeriadoHasta(fechaHasta);
		ciudadFeriadoList.setListaCiudades(ciudades);
			
		List<CiudadFeriado> listaCiudadFeriado=new ArrayList<CiudadFeriado>();
			
		try{
			listaCiudadFeriado=ciudadFeriadoList.getListaCiudadFeriados();
		}catch(Exception e){
			log.info("**********************PROBLEMA EN BÚSQUEDA DE FERIADOS**************");
			e.printStackTrace();
		}
		return listaCiudadFeriado;
	}
	
	private List<CiudadFeriado> RetornarCiudadRecuperable(Date fechaDesde, Date fechaHasta, ArrayList<Long> ciudades){
		CiudadFeriadoList ciudadFeriadoList=(CiudadFeriadoList) Component.getInstance("ciudadFeriadoList", true);
		
		ciudadFeriadoList.setFeriadoDesde(null);
		ciudadFeriadoList.setFeriadoHasta(null);
		ciudadFeriadoList.setRecuperableDesde(null);
		ciudadFeriadoList.setRecuperableHasta(null);
		
		ciudadFeriadoList.setRecuperableDesde(fechaDesde);
		ciudadFeriadoList.setRecuperableHasta(fechaHasta);
		ciudadFeriadoList.setListaCiudades(ciudades);
			
		List<CiudadFeriado> listaCiudadFeriado=new ArrayList<CiudadFeriado>();
			
		try{
			listaCiudadFeriado=ciudadFeriadoList.getListaCiudadFeriados();
		}catch(Exception e){
			log.info("**********************PROBLEMA EN BÚSQUEDA DE FERIADOS**************");
			e.printStackTrace();
		}
		return listaCiudadFeriado;
	}
	
	private void EstablecerRestriccion(){
		//Nuevo parámetro
		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

		detalleTipoParametroHome.setId(new Long(100));
		detalleTipoParametro = detalleTipoParametroHome.find();
		String notificar = detalleTipoParametro.getDescripcion();
		
		if (notificar.toLowerCase().equals("activar")) {
			this.restriccion=true;
		}else{
			this.restriccion=false;
		}
		
	}
	
	
	private void EstablecerProgramacionVacacionesEmpleado(Empleado empleado){
		
		if(restriccion){
			if(listaProgramaVacacion != null){
			  this.listaProgramaVacacion.clear();
			}
			ProgramaVacacion programaVacacion=new ProgramaVacacion();
			programaVacacion.setEmpleado(empleado);
			programaVacacion.setGenerado(1);
			
			programaVacacionList.setProgramaVacacion(programaVacacion);
			
			this.listaProgramaVacacion=programaVacacionList.getListaProgramaVacacion();
		}		
		
	}
	
	private String ValidarSolicitudProgramacionVacacion(){
		
		String mensaje="Ninguno";		
		
		
		if(restriccion){	
			
			int contadorValidos=0;
			
			if(listaProgramaVacacion.size()>0){			
				
				for(ProgramaVacacion pv: listaProgramaVacacion){
					
					ProgramaVacacion programaVacacion=new ProgramaVacacion();
					programaVacacion=pv;
					
					log.info(".........................................programaVacacion.getPrvaId:"+programaVacacion.getPrvaId());
					
					Date desde=this.instance.getFechaDesde();
					Date hasta=this.instance.getFechaHasta();
									
					String com1=ComparacionFechas(desde,hasta,programaVacacion.getFechaInicio(),programaVacacion.getFechaFin());
					String com2=ComparacionFechas(desde,hasta,programaVacacion.getFechaInicio1(),programaVacacion.getFechaFin1());
					String com3=ComparacionFechas(desde,hasta,programaVacacion.getFechaInicio2(),programaVacacion.getFechaFin2());
					String com4=ComparacionFechas(desde,hasta,programaVacacion.getFechaInicio3(),programaVacacion.getFechaFin3());
					
					log.info("......................................COM 1:"+com1);
					log.info("......................................COM 2:"+com2);
					log.info("......................................COM 3:"+com3);
					log.info("......................................COM 4:"+com4);
					
					if(com1.equals("Dentro") || com2.equals("Dentro") || com3.equals("Dentro") || com4.equals("Dentro")){
						contadorValidos++;
						break;
					}
				}
			}else{
				mensaje="No existen programaciones de vacaciones autorizadas";
				return mensaje;
			}
			
			log.info("......................................contadorValidos:"+contadorValidos);
			if( contadorValidos>0 ){
				mensaje="Ninguno";
			}else{
				mensaje="La fecha desde y fecha hasta deben estar dentro de un rango de la programación de vacaciones";
			}
			
		}	
		
		return mensaje;
	}
	
	private String ComparacionFechas(Date desde, Date hasta, Date inicio, Date fin){
		String validacion="Dentro";
		
		if(inicio!=null && fin!=null){
			if(desde.before(inicio) || hasta.after(fin)){
				validacion="Fuera";
			}
		}else{
			validacion="Nulo";
		}
		
		log.info(".................................................desde:"+desde);
		log.info(".................................................hasta:"+hasta);
		log.info(".................................................inicio:"+inicio);
		log.info(".................................................fin:"+fin);
		log.info(".................................................validacion:"+validacion);
		
		return validacion;
	}
	
	
}