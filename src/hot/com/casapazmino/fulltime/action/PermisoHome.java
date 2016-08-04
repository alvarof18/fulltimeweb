package com.casapazmino.fulltime.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;

import org.hibernate.validator.InvalidValue;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesContext;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.event.UploadEvent;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.Enumeraciones;
import com.casapazmino.fulltime.comun.Enumeraciones.tipoDescuentoPermiso;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.DepartamentoAutorizacion;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.DetalleTipoPermiso;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;
import com.casapazmino.fulltime.model.HistLabo;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.PermisoAutorizacion;
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.model.Timbre;
import com.casapazmino.fulltime.model.TipoPermiso;
import com.casapazmino.fulltime.procesos.PermisoNivelesBean;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;

@Name("permisoHome")
public class PermisoHome extends EntityHome<Permiso> {

	private static final long serialVersionUID = 1L;

	@Logger
	Log log;

	@In
	EntityManager entityManager;

	@In(create = true)
	EmpleadoHome empleadoHome;

	@In(create = true)
	DetalleTipoPermisoList detalleTipoPermisoList;

	@In(create = true)
	DepartamentoAutorizacionList departamentoAutorizacionList;

	@In(create = true)
	EmpleadoPeriodoVacacionHome empleadoPeriodoVacacionHome;

	@In(create = true)
	DetalleTipoParametroHome detalleTipoParametroHome;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	@In(create = true)
	TipoPermisoHome tipoPermisoHome;

	@In(create = true)
	AuditoriaHome auditoriaHome;

	@In(create = true)
	PermisoAutorizacionHome permisoAutorizacionHome;

	@In(create = true)
	PermisoAutorizacionList permisoAutorizacionList;

	@In(create = true)
	PermisoNivelesBean permisoNiveles;

	private String cadenaAnterior;
	private String cadenaActual;

	Fechas fechas = new Fechas();

	private Double saldoVacaciones;

	private boolean gestionaPermiso;
	private boolean autorizaPermiso;
	private boolean eliminaPermiso;
	private boolean legalizaPermiso;

	private int saldoDias;
	private int saldoHoras;
	private int saldoMinutos;
	private String horasMinutos;

	private Integer nivel;

	private Double diasHorasSolicitud;

	private boolean periodoUnico;

	// new variables
	private byte[] archivo;
	private String archivoNombre;
	private String archivoNombre2;
	private String url;
	
	private String msgError;

	private Empleado empleado;
	//
	private boolean ActualizaPermiso = true;

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

	public void setPermisoPermId(Long id) {
		setId(id);
	}

	public Long getPermisoPermId() {
		return (Long) getId();
	}

	@Override
	protected Permiso createInstance() {
		Permiso permiso = new Permiso();
		return permiso;
	}

	static String cuerpo = "";

	public void wire() {
		getInstance();

		ActualizaPermiso = true;
		log.info("LOG1 ");
		GestionAutorizacion();
		log.info("LOG2 ");
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

		TipoPermiso tipoPermiso = tipoPermisoHome.getDefinedInstance();
		if (tipoPermiso != null) {
			getInstance().setTipoPermiso(tipoPermiso);
		}

		// Determina si se esta usando uno o varios periodos de vacaciones
		this.setPeriodoUnico(gestionPermisoVacacion
				.buscarParametroPeriodoVacaciones());

		if (!isManaged()) {

			setCadenaAnterior("");

			this.detalleTipoParametroHome.setId(Constantes.DECISION_NO);
			DetalleTipoParametro detalleTipoParametroNo = this.detalleTipoParametroHome
					.find();
			this.getInstance().setDetalleTipoParametro(detalleTipoParametroNo);

			this.getInstance().setFecha(Fechas.calendarioActual().getTime());
			this.getInstance().setTrabajadas(false);
			// this.getInstance().setTimbra(true);

		} else {

			if (this.getDiasHorasSolicitud() == null) {
				this.setDiasHorasSolicitud(this.getInstance().getDias()
						+ (this.getInstance().getNumeroHoras() / 8));
			}

			setCadenaAnterior(this.getInstance().toString());

			this.permisoHorasMinutos();

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
			//
			this.setAutorizaPermiso(gestionPermisoVacacion
					.buscarAutorizaPermiso());
			this.setLegalizaPermiso(gestionPermisoVacacion
					.buscarLegalizarPermiso());
			this.setEliminaPermiso(gestionPermisoVacacion
					.buscarEliminarPermiso());
			//
		}

		this.setGestionaPermiso(gestionPermisoVacacion.buscarPermiso());

		if (!isGestionaPermiso()) {

			if (!this.isManaged()) {
				Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
				this.getInstance().setEmpleadoByEmplId(empleado);
				this.getInstance().setEmpleadoByEmpEmplId(
						empleado.getEmpleado());

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

	}

	public boolean isWired() {
		// if (getInstance().getDetalleTipoParametroByDtpaId() == null)
		// return false;
		// if (getInstance().getDetalleTipoParametroByAutorizado() == null)
		// return false;
		// if (getInstance().getEmpleadoByEmplId() == null)
		// return false;
		return true;
	}

	public Permiso getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void nivelesAprobacionDetalleTipoPermiso(){
		try{
			TipoPermiso tipoPermiso=this.instance.getTipoPermiso();
			
			if(tipoPermiso!=null){
				log.info("...................................................................1");
				Integer nivelTipoPermiso = tipoPermiso.getNivel();
				log.info("...................................................................2");
				detalleTipoPermisoList.getTipoPermiso().setTipeId(null);
				log.info("...................................................................3");
				detalleTipoPermisoList.setTipo(null);
				log.info("...................................................................4");
				detalleTipoPermisoList.getTipoPermiso().setTipeId(tipoPermiso.getTipeId());
				log.info("...................................................................5");
				int dias=this.getInstance().getDias();
				log.info("...................................................................6");
				double horas=this.getInstance().getNumeroHoras();
				log.info("...................................................................7");

				if(dias>0){
					//busqueda por dias
					detalleTipoPermisoList.setTipo("dias");
					List<DetalleTipoPermiso> lista=new ArrayList<DetalleTipoPermiso>();
					lista=detalleTipoPermisoList.getListaDetalleTipoPermiso();
					
					if(lista.size()>0){
						this.nivelesRequeridos = nivelTipoPermiso;
						for(DetalleTipoPermiso d: lista){
							if(dias>=d.getInicio()&&dias<=d.getFin()){
								this.nivelesRequeridos = d.getNivel();
								break;
							}
						}	
						
					}else{
						this.nivelesRequeridos = nivelTipoPermiso;
					}				
					
				}else{
					if(horas>0){
						//busqueda por horas
						detalleTipoPermisoList.setTipo("horas");
						List<DetalleTipoPermiso> lista=new ArrayList<DetalleTipoPermiso>();
						lista=detalleTipoPermisoList.getListaDetalleTipoPermiso();
						
						if(lista.size()>0){
							this.nivelesRequeridos = nivelTipoPermiso;
							for(DetalleTipoPermiso d: lista){
								if(horas>=d.getInicio()&&horas<=d.getFin()){
									this.nivelesRequeridos = d.getNivel();
									break;
								}
							}	
							
						}else{
							this.nivelesRequeridos = nivelTipoPermiso;
						}
					}else{
						this.nivelesRequeridos = nivelTipoPermiso;
					}
				}
				
			}else{
				log.info("............................................tipo permiso nulo");
				this.nivelesRequeridos = 0;
			}								
			
		}catch (Exception ex) {
			log.info("............................................excepcion en tipo permiso");
			this.nivelesRequeridos = 0;
		}
	}
	
	
	public void diferenciaFechas() {
		
		if (this.getInstance().getFechaDesde() != null
				&& this.getInstance().getFechaHasta() != null) {
		
			int dias = (int) fechas.numeroDias(this.instance.getFechaDesde(),
					this.instance.getFechaHasta());

			Double horas = fechas.numeroHoras(this.instance.getFechaDesde(),
					this.instance.getFechaHasta());

			if (dias > 1) {
				horas = (double) 0;
			}

			if (dias == 1 && horas != 0) {
				dias = 0;				
			}
			
			this.instance.setDias(dias);
			this.instance.setNumeroHoras(horas);
			this.instance.setDiasLibres(0);

//			CONTROLA TIEMPO ALMUERZO 
//			Para no tomar el cuenta los minutos para alimentación definidos en el horario
//			en el caso que un permiso este en el horario del almuerzo
			try{
				if(this.getInstance().getTipoPermiso().getIncluirAlmuerzo()==true && this.getInstance().getNumeroHoras()>0){
					//ControlarAlmuerzos
					ejecutarAlmuerzo();
				}
			}catch(Exception ex){
				InvalidValue iv = new InvalidValue(
						"No se ha verificado el tiempo de almuerzo asignado!",
						null, null, null, null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}
			
//			FIN CONTROLA TIEMPO ALMUERZO
			
			//Controlar dias laborables y libres
			try{
				if(this.getInstance().getDias()>0){
					ValidacionAdicionalLibreFeriados();
				}
			}catch(Exception ex){
				InvalidValue iv = new InvalidValue(
						"Hubo un problema en la verificacion de dias laborables!",
						null, null, null, null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}
			
			//Controlar niveles por detalle de tipo de permiso
			try{
				try {
					Integer nivelCiudad = this.getInstance().getEmpleadoByEmplId()
							.getCiudad().getNivel();					

					if (nivelCiudad > 0){
						this.nivelesRequeridos = nivelCiudad;
					}
					else{
						nivelesAprobacionDetalleTipoPermiso();
					}						
				} catch (Exception ex) {
					log.info("............................................excepcion en tipo permiso por ciudad");
					this.nivelesRequeridos = 0;
				}
			}catch(Exception ex){
				InvalidValue iv = new InvalidValue(
						"Hubo un problema en la verificacion de niveles por tipo de permiso!",
						null, null, null, null);
				FacesMessages.instance().clear();
				FacesMessages.instance().add(iv);
			}			
						
			this.permisoHorasMinutos();
		}
	}

	public String validarPermiso() {
		String mensajeError = "Ninguno";
		String parametro = "ninguno";

		try {
			int diasEnUnAnio = 0;

			if (this.getInstance().getFechaDesde()
					.after(this.getInstance().getFechaHasta())) {
				mensajeError = "Fecha Desde menor que Fecha Hasta";
			}

			this.buscarPermisoPorTipo(this.getInstance());

			// if(this.getInstance().getNumeroHoras() >
			// this.getInstance().getTipoPermiso().getMaximoHoras() &&
			// !this.getInstance().getTipoPermiso().isAfectaFechaVacacion()){
			// mensajeError = "Número horas supera el permitido";
			// }

			// if(this.getInstance().getDias() >
			// this.getInstance().getTipoPermiso().getMaximoDias() &&
			// !this.getInstance().getTipoPermiso().isAfectaFechaVacacion()) {
			// mensajeError = "Número días supera el permitido";
			// }

			if (this.getInstance().getNumeroHoras() > this.getInstance()
					.getTipoPermiso().getMaximoHoras()) {
				mensajeError = "Número horas supera el permitido";
			}

			if (this.getInstance().getDias() > this.getInstance()
					.getTipoPermiso().getMaximoDias()) {
				mensajeError = "Número días supera el permitido";
			}

			if (this.getInstance().getTipoPermiso().isAcumulaAnios()) {
				diasEnUnAnio = this.validarPermisoAcumulaAnios();
				if (diasEnUnAnio > this.getInstance().getTipoPermiso()
						.getMaximoDias()) {
					mensajeError = "Número días supera el permitido en un periodo";
				}
			}

			tipoDescuentoPermiso enumeracionTipoDescuentoPermiso = Enumeraciones.tipoDescuentoPermiso
					.valueOf("Vacaciones");

			if (this.getInstance().getTipoPermiso().getDescuento() == enumeracionTipoDescuentoPermiso.Vacaciones) {
				double diasHorasPermiso = ((this.getInstance().getDias() * 8) + this
						.getInstance().getNumeroHoras()) / 8;

				// Si fecha de ingreso es enero dias ProporcionalVacacion se
				// calcula desde febrero
				// double meses =
				// fechas.numeroDias(this.getInstance().getEmpleadoByEmplId().getFechaIngreso(),
				// this.getInstance().getFechaDesde()) / 30;

				double meses = fechas.numeroDias(this.getInstance()
						.getEmpleadoPeriodoVacacion().getFechaDesde(), this
						.getInstance().getFechaDesde()) / 30;

				double diasProporcionalVacacion = meses
						* this.getInstance().getEmpleadoByEmplId()
								.getDetalleGrupoContratado().getTotalHoras();

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

				// CONTROLA EL SALDO DEPENDIENDO SI EL REGISTRO ES NUEVO O NO
				if (isManaged()) {
					this.setSaldoVacaciones(gestionPermisoVacacion
							.buscarSaldoVacaciones(this.getInstance()
									.getEmpleadoPeriodoVacacion())
							+ diasHorasPermiso);
				} else {
					this.setSaldoVacaciones(gestionPermisoVacacion
							.buscarSaldoVacaciones(this.getInstance()
									.getEmpleadoPeriodoVacacion()));
				}

				double controlDiasVacacion = diasProporcionalVacacion
						+ this.getSaldoVacaciones();

				log.info("================================= this.getSaldoVacaciones() "
						+ this.getSaldoVacaciones());
				log.info("================================= diasHorasPermiso "
						+ diasHorasPermiso);
				log.info("================================= Meses " + meses);
				log.info("================================= diasProporcionalVacacion "
						+ diasProporcionalVacacion);
				log.info("================================= controlDiasVacacion  "
						+ controlDiasVacacion);

				log.info("================================= getMaximoHoras()  "
						+ this.getInstance().getEmpleadoByEmplId()
								.getDetalleGrupoContratado().getMaximoHoras());

				if (controlDiasVacacion > this.getInstance()
						.getEmpleadoByEmplId().getDetalleGrupoContratado()
						.getMaximoHoras()) {
					controlDiasVacacion = this.getInstance()
							.getEmpleadoByEmplId().getDetalleGrupoContratado()
							.getMaximoHoras();
				}

				if (diasHorasPermiso > controlDiasVacacion
						&& this.getInstance().getEmpleadoByEmplId()
								.getControlaVacacion() == 1) {
					mensajeError = "Número días supera saldo vacaciones";
				}
			}

			List<Permiso> permisos = new ArrayList<Permiso>();

			// CONTROLA QUE NO DUPLIQUE UN PERMISO POR UN DIA CUANDO SE TIENE
			// UNO POR HORAS
			permisos = gestionPermisoVacacion.buscarPermisoDia(this
					.getInstance());			
			permisos.remove(this.getInstance());
					
			for (Permiso permiso : permisos) {
				if (permiso.getDias() == 1) {
					mensajeError = "Existen permisos con esta fecha";
					return mensajeError;

				}
			}

			// CONTROLA QUE NO DUPLIQUE UN PERMISO POR HORAS CUANDO SE TIENE UNO
			// POR UN DIA
			if (this.getInstance().getDias() == 1) {
				if (permisos.size() != 0) {
					mensajeError = "Existen permisos con esta fecha";
					return mensajeError;
				}
			}

			permisos = gestionPermisoVacacion.buscarPermisoFechaDesde(this
					.getInstance());
			
			
			permisos.remove(this.getInstance());
			
			if (permisos.size() != 0) {
			
				mensajeError = "Existen permisos con estas fechas";
				return mensajeError;
			}

			permisos = gestionPermisoVacacion.buscarPermisoFechaHasta(this
					.getInstance());
			permisos.remove(this.getInstance());
			if (permisos.size() != 0) {
				
				
				mensajeError = "Existen permisos con estas fechas";
				return mensajeError;
			}

			permisos = gestionPermisoVacacion.buscarPermisosEmpleado(this
					.getInstance());
			permisos.remove(this.getInstance());
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
			if (solicitudVacaciones.size() != 0) {
				mensajeError = "Existen vacaciones con estas fechas";
				return mensajeError;
			}

			solicitudVacaciones = gestionPermisoVacacion
					.buscarVacacionesFechaHasta(this.getInstance());
			if (solicitudVacaciones.size() != 0) {
				mensajeError = "Existen vacaciones con estas fechas";
				return mensajeError;
			}

			solicitudVacaciones = gestionPermisoVacacion
					.buscarVacacionesEmpleado(this.getInstance());
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
			
			// Validacion para no crear permisos que superen el tiempo de justificacion
			
			DetalleTipoParametroList detalleTipoParametro = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
			DetalleTipoParametro detalle = detalleTipoParametro.getBloquePermiso();
			parametro = detalle.getDescripcion();
			
			if(parametro.equalsIgnoreCase("activar")){
				// True puedo crear permiso
				if(!this.justificacion()){
					mensajeError = "El tiempo de justificacion del permiso esta excedido";
				}	
			}
		
			return mensajeError;
		} catch (Exception e) {
			FacesMessages.instance().add(
					"Error - Verifique Periodo Vacaciones o Tipo de permiso");
			e.printStackTrace();

		}
		return mensajeError;

	}
	
	//****************************************************************************************
	
	public void ValidacionAdicionalLibreFeriados(){
		
		log.info("------------------------------------ENTRO A INFORMACION ADICIONAL");
		
		String mensaje="Ninguno";
		
		Empleado empleado=this.instance.getEmpleadoByEmplId();
		
		DetalleGrupoContratado detalleGrupo=empleado.getDetalleGrupoContratado();
				
		if(detalleGrupo.getActivarRegla()){
			
			tipoDescuentoPermiso enumeracionTipoDescuentoPermiso = Enumeraciones.tipoDescuentoPermiso.valueOf("Vacaciones");
				
			if( (this.getInstance().getTipoPermiso().getDescuento() == enumeracionTipoDescuentoPermiso.Vacaciones && detalleGrupo.getPermisoCargo()) ){

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
				
				this.getInstance().setDias(totaldiasLaborables);
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
	
	

	// SE UTILIZAR DESDE ProcesarTiempoNoaboradoBean
	public String crearPermiso() {
		return super.persist();
	}

	// SE UTILIZAR DESDE ProcesarTiempoNoaboradoBean
	public String modificarPermiso() {
		return super.update();
	}

	@Override
	public String persist() {
		String persisted = null;
		this.getInstance().setEstadoActual("0");
		String mensajeError = this.validarPermiso();
	
		this.instance.setAprobacion(0);
		this.instance.setNivelRequerido(this.nivelesRequeridos);
		
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

		if (mensajeError.equals("Ninguno")) {

			try {

				// Modificado para que timbre todos los permisos, sino no cambia
				// el estado de la asistencia al autorizar el permiso
				// this.getInstance().setTimbra(this.getInstance().getTipoPermiso().isTimbra());
				this.getInstance().setTimbra(true);
				this.getInstance().setFactor(
						this.getInstance().getTipoPermiso().getFactorHoras());
				
				if(getInstance().getPermId() != null){
					this.getInstance().setPermId(null);
				}
				
				persisted = super.persist();

				double numeroDiasHoras = this.getInstance().getDias()
						+ (this.getInstance().getNumeroHoras() / 8);
				Date empleadoPeriodoVacacionfecha = this.gestionPermisoVacacion
						.fechaCargaVacacion(this.getInstance()
								.getEmpleadoByEmplId(), this.getInstance()
								.getEmpleadoPeriodoVacacion().getFechaDesde());
				Double diasPerdidos = this.getInstance()
						.getEmpleadoPeriodoVacacion().getDiasPerdidos();

				// ACTUALIZAR DIAS PERDIDOS CUANDO LA FECHA DEL PERMISO ES MENOR
				// QUE LA FECHA DE LA ULTIMA CARGA
//				tipoDescuentoPermiso enumeracionTipoDescuentoPermiso = Enumeraciones.tipoDescuentoPermiso
//						.valueOf("Vacaciones");
//				if (this.getInstance().getFechaDesde()
//						.before(empleadoPeriodoVacacionfecha)
//						&& this.getInstance().getTipoPermiso().getDescuento() == enumeracionTipoDescuentoPermiso.Vacaciones) {
//					this.getInstance().getEmpleadoPeriodoVacacion()
//							.setDiasPerdidos(diasPerdidos - numeroDiasHoras);
//					empleadoPeriodoVacacionHome.update();
//				}

				if (this.getInstance().getTipoPermiso().getCrear()) {
					// Envia correo electronico
					EnviarMensajePermisoCreado();
					// Fin Envia correo electronico
				}

				//if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Permiso", "Insertar",
							cadenaActual, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add(
							"#{messages['mensaje.grabar']}");
				//}

			} catch (Exception e) {
				e.printStackTrace();
				FacesMessages
						.instance()
						.add("Error - Verifique Periodo Vacaciones o Tipo de permiso");
			}
		} else {
			FacesMessages.instance().add(mensajeError);
		}
	
		return persisted;
	}

	public String grabar() {

		// this.getInstance().setFactor(this.getInstance().getTipoPermiso().getFactorHoras());
		// return super.persist();
		return this.update();

		// String mensajeError = this.validarPermiso();
		// if (mensajeError.equals("Ninguno")){
		// this.getInstance().setFactor(this.getInstance().getTipoPermiso().getFactorHoras());
		// try {
		// return super.update();
		// } catch (Exception e) {
		// e.printStackTrace();
		// FacesMessages.instance().add("Error - Verifique Periodo Vacaciones o Tipo de permiso");
		// return "";
		// }
		//
		// } else {
		// FacesMessages.instance().add(mensajeError);
		// return "";
		// }
	}

	@Override
	public String update() {
		String updated = null;
		String mensajeError = this.validarPermiso();
				
		// **********************************controla permisos de jefes
		Empleado empleado = this.instance.getEmpleadoByEmplId();
		Empleado jefe = this.instance.getEmpleadoByEmpEmplId();

		if (this.instance.getEstadoActual().trim().equals("0")) {
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
				this.getInstance().setFactor(
						this.getInstance().getTipoPermiso().getFactorHoras());

				updated = super.update();
				if (updated.equals("updated")) {

					Double diasPerdidos = this.getInstance()
							.getEmpleadoPeriodoVacacion().getDiasPerdidos();
					double numeroDiasHoras = this.getInstance().getDias()
							+ (this.getInstance().getNumeroHoras() / 8);
					Date empleadoPeriodoVacacionfecha = this.gestionPermisoVacacion
							.fechaCargaVacacion(this.getInstance()
									.getEmpleadoByEmplId(), this.getInstance()
									.getEmpleadoPeriodoVacacion()
									.getFechaDesde());

					log.info("=========================== dias perdidos "
							+ diasPerdidos);
					log.info("=========================== dias solicitud anterior"
							+ this.getDiasHorasSolicitud());
					log.info("=========================== dias solicitud actual"
							+ numeroDiasHoras);
					log.info("=========================== restar "
							+ (this.getDiasHorasSolicitud() - numeroDiasHoras));

					// ACTUALIZAR DIAS PERDIDOS CUANDO LA FECHA DEL PERMISO ES
					// MENOR QUE LA FECHA DE LA ULTIMA CARGA
//					tipoDescuentoPermiso enumeracionTipoDescuentoPermiso = Enumeraciones.tipoDescuentoPermiso
//							.valueOf("Vacaciones");
//					if (this.getInstance().getFechaDesde()
//							.before(empleadoPeriodoVacacionfecha)
//							&& this.getInstance().getTipoPermiso()
//									.getDescuento() == enumeracionTipoDescuentoPermiso.Vacaciones) {
//						this.getInstance()
//								.getEmpleadoPeriodoVacacion()
//								.setDiasPerdidos(
//										(diasPerdidos + (this
//												.getDiasHorasSolicitud() - numeroDiasHoras)));
//						empleadoPeriodoVacacionHome.update();
//					}

					if (this.getInstance().getTipoPermiso().getActualizar()) {
						// Envia Correo Electronico
						if (ActualizaPermiso)
							EnviarMensajePermisoActualizacion();
						// Fin Envia Correo Electronico
					}

					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Permiso", "Modificar",
							cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add(
							"#{messages['mensaje.actualizar']}");
				}
			} else {
				FacesMessages.instance().add(mensajeError);
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al actualizar permiso");
			e.printStackTrace();
		}
	
		return updated;
	}

	@Transactional
	@Override
	public String remove() {
		String removed = null;
		int numeroDiasPermiso = this.getInstance().getDias();

		this.eliminar_Archivos();

		if (this.getInstance().getTipoPermiso().getEliminar()) {
			// Envia Correo Electronico
			EnviarMensajePermisoEliminacion();
			// Fin Envia Correo Electronico
		}

		// Eliminar autorizaciones//
		List<PermisoAutorizacion> permisoAutorizacion = new ArrayList<PermisoAutorizacion>();

		permisoAutorizacionList.setPermiso(this.getInstance());
		permisoAutorizacion = permisoAutorizacionList
				.getListaPermisoAutorizacion();
		for (PermisoAutorizacion p : permisoAutorizacion) {
			permisoAutorizacionHome.setInstance(p);
			permisoAutorizacionHome.remove();
		}
		// Eliminar autorizaciones//

		if (this.getInstance().getDetalleTipoParametro().getDtpaId() == Constantes.DECISION_NO) {
			try {

				removed = super.remove();
				if (removed.equals("removed")) {
					double numeroDiasHoras = this.getInstance().getDias()
							+ (this.getInstance().getNumeroHoras() / 8);
					Date empleadoPeriodoVacacionfecha = this.gestionPermisoVacacion
							.fechaCargaVacacion(this.getInstance()
									.getEmpleadoByEmplId(), this.getInstance()
									.getEmpleadoPeriodoVacacion()
									.getFechaDesde());
					Double diasPerdidos = this.getInstance()
							.getEmpleadoPeriodoVacacion().getDiasPerdidos();

					// ACTUALIZAR DIAS PERDIDOS CUANDO LA FECHA DEL PERMISO ES
					// MENOR QUE LA FECHA DE LA ULTIMA CARGA
//					tipoDescuentoPermiso enumeracionTipoDescuentoPermiso = Enumeraciones.tipoDescuentoPermiso
//							.valueOf("Vacaciones");
//					if (this.getInstance().getFechaDesde()
//							.before(empleadoPeriodoVacacionfecha)
//							&& this.getInstance().getTipoPermiso()
//									.getDescuento() == enumeracionTipoDescuentoPermiso.Vacaciones) {
//						this.getInstance()
//								.getEmpleadoPeriodoVacacion()
//								.setDiasPerdidos(diasPerdidos + numeroDiasHoras);
//						empleadoPeriodoVacacionHome.update();
//					}

					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("Permiso", "Eliminar",
							cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					FacesMessages.instance().add(
							"#{messages['mensaje.eliminar']}");

					permisoNiveles.ConsultarPermisos();
					return removed;
				}
			} catch (Exception e) {
				FacesMessages.instance().add("Error al eliminar permiso");
				e.printStackTrace();
			}
		}

		// CONTROLAR SI EXISTE DOS O MAS PERMISOS QUE SUMEN 8.48 PARA QUITAR
		// JUSTIFICACION DEL DIA
		// SI EL PERMISO ES POR HORAS NO HACE NADA
		double totalPermisoHoras = this.totalPermisoHoras();
		if (totalPermisoHoras >= 8.48) {
			numeroDiasPermiso = 1;
		} else {
			numeroDiasPermiso = this.getInstance().getDias();
		}

		try {
			if (numeroDiasPermiso >= 0) {
				List<Asistencia> asistencias = new ArrayList<Asistencia>();
				//
				asistencias.clear();
				asistencias = this.buscarAsistencias();

				// Autorizar permiso por días
				for (Asistencia asistencia : asistencias) {
					// if(asistencia.getFechaHoraHorario().equals(asistencia.getFechaHoraTimbre())){
					// asistencia.setFechaHoraTimbre(null);
					// asistencia.setEstado(Constantes.ASISTENCIA_FALTA);
					// } else {
					// asistencia.setEstado(Constantes.ASISTENCIA_REGISTRADO);
					// }
					if (asistencia.getFechaHoraTimbre() == null) {
						// asistencia.setFechaHoraTimbre(null);
						asistencia.setEstado(Constantes.ASISTENCIA_FALTA);
					} else {
						asistencia.setEstado(Constantes.ASISTENCIA_REGISTRADO);
					}
				}
				this.actualizarAsistenciaPermiso(asistencias);
			}

			// AUMENTA FECHA DE ULTIMA VACACION LOS DIAS QUE TENGA EN PERMISO
			if (this.getInstance().getTipoPermiso().isAfectaFechaVacacion()
					&& this.getInstance().getDias() > 0) {
				this.actualizarPeriodoVacacion(-(this.getInstance().getDias()));
			}
			// RESTAURA EL ESTADO DEL SALDO DE VACACIONES A POSITIVO O NEGATIVO
			if (gestionPermisoVacacion.buscarSaldoVacaciones(this.getInstance()
					.getEmpleadoPeriodoVacacion())
					+ this.getInstance().getNumeroHoras()
					+ this.getInstance().getDias() < 0) {
				this.actualizarEstadoNegativo(new Long(-1));
			} else
				this.actualizarEstadoNegativo(new Long(0));

			removed = super.remove();

		//	if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("Permiso", "Eliminar",
						cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				FacesMessages.instance().add("#{messages['mensaje.eliminar']}");
			//}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al eliminar permiso");
			e.printStackTrace();
		}

		permisoNiveles.ConsultarPermisos();

		return removed;
	}

	public String legalizar() {
		if (this.getInstance().isLegalizado()) {
			this.getInstance().setLegalizado(false);
		} else {

			this.getInstance().setLegalizado(true);

			// Envia correo electronico
			if (this.getInstance().getTipoPermiso().getLegalizar()) {
				EnviarMensajePermisoLegali();
			}
		}

		return this.update();
	}

	// CONTROLAR JUSTIFICACION DE FALTA CUANDO SE GENERA MAS DE UN PERMISO
	public double totalPermisoHoras() {

		List<Permiso> permisos = this.gestionPermisoVacacion
				.buscarPermisoDia(this.getInstance());
		double totalHorasDia = 0;
		for (Permiso permiso : permisos) {
			// SUMAR SOLO PERMISO AUTORIZADOS
			if (permiso.getDetalleTipoParametro().getDtpaId() == Constantes.DECISION_SI) {
				totalHorasDia = totalHorasDia + permiso.getNumeroHoras();
			}
		}

		log.info("================================= numeroHorasDia "
				+ totalHorasDia);
		return totalHorasDia;
	};

	@Transactional
	public String PreAutorizarPermiso() {
		// this.cambiarEstadoAutorizacion((long) 5);
		// this.remove();
		// **Registrar personal que autoriza

		this.empleado = gestionPermisoVacacion.buscarEmpleado();

		PermisoAutorizacion permisoAutorizacion = new PermisoAutorizacion();

		permisoAutorizacion.setPermiso(this.getInstance());
		permisoAutorizacion.setEmpleado(empleado);
		permisoAutorizacion.setNivel(this.instance.getNivel());
		permisoAutorizacion.setEstado("0");

		permisoAutorizacionHome.setInstance(permisoAutorizacion);
		permisoAutorizacionHome.persist();
		// **Registrar personal que autoriza

		// this.instance.setNivel((this.instance.getNivel())+1);
		this.instance.setAprobacion(this.instance.getNivel());
		Integer nivelAux = this.instance.getNivel();
		log.info("------------------------------------------------------------------------------nivelAux1: "
				+ nivelAux);
		nivelAux = nivelAux + 1;
		log.info("------------------------------------------------------------------------------nivelAux2: "
				+ nivelAux);
		this.instance.setNivel(nivelAux);
		this.instance.setEstadoActual("1");
		
		this.detalleTipoParametroHome.setId(Constantes.DECISION_NO);
		DetalleTipoParametro detalleTipoParametroNo = this.detalleTipoParametroHome
				.find();
		this.getInstance().setDetalleTipoParametro(detalleTipoParametroNo);
		

		String grabar = super.update();

		if (this.getInstance().getTipoPermiso().getPreautorizar()) {
			// Envia Correo Electronico
			EnviarMensajePermisoPreAuto();
			// Fin Envia Correo Electronico
		}

		FacesMessages.instance().clear();
		FacesMessages.instance().add("Solicitud Pre-Autorizada!");

		return grabar;
	}

	@Transactional
	public String NoAutorizarPermiso() {
		// this.cambiarEstadoAutorizacion((long) 5);
		// this.remove();
		// this.instance.setNivel((this.instance.getNivel())+1);
		this.instance.setEstadoActual("3");

		this.detalleTipoParametroHome.setId(Constantes.DECISION_NO);
		DetalleTipoParametro detalleTipoParametroNo = this.detalleTipoParametroHome
				.find();
		this.getInstance().setDetalleTipoParametro(detalleTipoParametroNo);
		
		String grabar = super.update();

		if (this.getInstance().getTipoPermiso().getNoautorizar()) {
			// Envia Correo Electronico
			EnviarMensajePermisoNoAuto();
			// Fin Envia Correo Electronico
		}

		FacesMessages.instance().clear();
		FacesMessages.instance().add("Solicitud No Autorizada!");

		return grabar;
	}

	@Transactional
	public String AutorizarPermiso() {
		boolean autorizar = true;

		int numeroDiasPermiso = this.getInstance().getDias();
		double numeroHorasPermiso = this.getInstance().getDias();

		List<Asistencia> asistencias = new ArrayList<Asistencia>();

		Asistencia asistenciaEntrada = this.buscarAsistenciaEntradaSalida(this
				.getInstance().getFechaDesde(), "E");
		Asistencia asistenciaSalida = this.buscarAsistenciaEntradaSalida(this
				.getInstance().getFechaHasta(), "S");

		asistencias.add(asistenciaEntrada);
		asistencias.add(asistenciaSalida);

		if (asistenciaEntrada.getFechaHoraHorario() == null
				|| asistenciaSalida.getFechaHoraHorario() == null) {
			autorizar = false;
		}

		String estado = null;
		ActualizaPermiso = false;
		// TODO: validar porque sale jboss colocar excepcion
		//estado = this.grabar();
		estado=this.validarPermiso();
		ActualizaPermiso = true;
		log.info("==================================== estado " + estado);
		/*if (estado.equals("")) {
			return null;
		}*/
		if (!estado.equals("Ninguno")) {
			FacesMessages.instance().clear();
			FacesMessages.instance().add(estado);

			return null;
		}

		this.cambiarEstadoAutorizacion(Constantes.DECISION_SI);

		// CONTROLAR SI EXISTE DOS O MAS PERMISOS QUE SUMEN 8.48 PARA JUSTIFICAR
		// EL DIA COMPLETO
		double totalPermisoHoras = this.totalPermisoHoras();
		if (totalPermisoHoras >= 8.48) {
			numeroDiasPermiso = 1;
			numeroHorasPermiso = 0;
		} else {
			numeroDiasPermiso = this.getInstance().getDias();
			numeroHorasPermiso = this.getInstance().getNumeroHoras();
		}

		// Si permiso es por horas
		if (numeroHorasPermiso != 0) {
			String asistenciaTime = "";
			String permisoTime = "";
			if (autorizar) {
				TimbreList timbreList = (TimbreList) Component.getInstance(
						"timbreList", true);

				// MODIFICADO PARA MSSQL
				// COLOCA HORAS, MINUTOS Y SEGUNDOS EN CERO
				// SINO NO REALIZA LA BUSQUEDA
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
				String stringFecha = Fechas.cambiarFormato(this.getInstance()
						.getFechaDesde(), "yyyy-MM-dd");

				Date fechaDesde = null;
				try {
					fechaDesde = formato.parse(stringFecha);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				timbreList.getTimbre().setFechaHoraTimbre(fechaDesde);
				timbreList.getTimbre().setCodigoEmpleado(
						this.getInstance().getEmpleadoByEmplId()
								.getCodigoEmpleado());

				List<Timbre> timbres = new ArrayList<Timbre>();
				timbres = timbreList.getListaTimbres();
				if (timbres.size() != 0) {
					long diferenciaPermisoDesde, diferenciaPermisoHasta, diferenciaAsistencia;

					/*
					 * OJO FALTA
					 * ************************************************
					 * ****************** Tomar en cuenta el numero de minutos
					 * que se controla para no repetir timbres Tomar en cuenta
					 * cuando Timbra al día siguente Ej. 02:00 AM Implementar
					 * Borrar Permisos al hacer esto cargar timbres nuevamente
					 */
					List<Asistencia> asistenciasUpdate = this
							.revisarAsistenciaSalida(asistencias);
					// Recorre Timbres y asistencias para buscar el valor con
					// menor diferencia de horas
					// for (Timbre timbre : timbres) {
					// Asistencia asistenciaMenor = new Asistencia();
					//
					// long menorAsistencia =
					// Math.abs(asistenciasUpdate.get(0).getFechaHoraHorario().getTime()
					// - timbre.getFechaHoraTimbre().getTime());
					//
					// for (Asistencia asistencia : asistenciasUpdate) {
					// diferenciaAsistencia =
					// Math.abs(timbre.getFechaHoraTimbre().getTime() -
					// asistencia.getFechaHoraHorario().getTime());
					// if (diferenciaAsistencia <= menorAsistencia) {
					// // DE ALGUNA FORMA SUPER EXTRAÑA, QUE NO SE PORQUE
					// !"#$%&, ACTUALIZA LA LISTA asistenciaUpdate???
					// asistenciaMenor = asistencia;
					// menorAsistencia = diferenciaAsistencia;
					// }
					// }
					//
					// diferenciaPermisoDesde =
					// Math.abs(timbre.getFechaHoraTimbre().getTime() -
					// this.getInstance().getFechaDesde().getTime());
					// diferenciaPermisoHasta =
					// Math.abs(timbre.getFechaHoraTimbre().getTime() -
					// this.getInstance().getFechaHasta().getTime());
					//
					// log.info("============================ diferenciaPermisoDesde "
					// + diferenciaPermisoDesde);
					// log.info("============================ diferenciaPermisoHasta "
					// + diferenciaPermisoHasta);
					// log.info("============================ menorAsistencia "
					// + menorAsistencia);
					//
					// // if (menorAsistencia < diferenciaPermisoDesde &&
					// menorAsistencia < diferenciaPermisoHasta ){
					// // // DE ALGUNA FORMA SUPER EXTRAÑA, QUE NO SE PORQUE
					// !"#$%&, ACTUALIZA LA LISTA asistenciaUpdate???
					// //
					// asistenciaMenor.setFechaHoraTimbre(timbre.getFechaHoraTimbre());
					// // if
					// (asistenciaMenor.getEstado().equals(Constantes.ASISTENCIA_FALTA)){
					// //
					// asistenciaMenor.setEstado(Constantes.ASISTENCIA_REGISTRADO);
					// // }
					// // } else
					// if (diferenciaPermisoDesde < diferenciaPermisoHasta &&
					// diferenciaPermisoDesde < menorAsistencia){
					// this.getInstance().setHoraSalida(timbre.getFechaHoraTimbre());
					// } else if (diferenciaPermisoHasta <
					// diferenciaPermisoDesde && diferenciaPermisoHasta <
					// menorAsistencia){
					// this.getInstance().setHoraRetorno(timbre.getFechaHoraTimbre());
					// }
					// // if (this.getInstance().getHoraSalida() == null) {
					// //
					// this.getInstance().setHoraSalida(this.getInstance().getFechaDesde());
					// // }
					// // if (this.getInstance().getHoraRetorno() == null) {
					// //
					// this.getInstance().setHoraRetorno(this.getInstance().getFechaHasta());
					// // }
					// }

					Asistencia asistenciaEntrada1 = this
							.buscarAsistenciaEntradaSalida(this.getInstance()
									.getFechaDesde(), "E");
					Asistencia asistenciaSalida1 = this
							.buscarAsistenciaEntradaSalida(this.getInstance()
									.getFechaHasta(), "S");

					if (asistenciaEntrada1.getFechaHoraHorario().getTime() == this
							.getInstance().getFechaDesde().getTime()) {
						this.getInstance().setHoraSalida(
								this.getInstance().getFechaDesde());
						for (Timbre timbre : timbres) {
							if (timbre.getAccion().equals("0")) {
								this.getInstance().setHoraRetorno(
										timbre.getFechaHoraTimbre());
							}
						}
					}

					if (asistenciaSalida1.getFechaHoraHorario().getTime() == this
							.getInstance().getFechaHasta().getTime()) {
						this.getInstance().setHoraRetorno(
								this.getInstance().getFechaHasta());
						for (Timbre timbre : timbres) {
							if (timbre.getAccion().equals("1")) {
								this.getInstance().setHoraSalida(
										timbre.getFechaHoraTimbre());
							}
						}
					}

					if (asistenciaEntrada1.getFechaHoraHorario().getTime() != this
							.getInstance().getFechaDesde().getTime()
							&& asistenciaSalida1.getFechaHoraHorario()
									.getTime() != this.getInstance()
									.getFechaHasta().getTime()) {

						Timbre timbreMenorEntrada = new Timbre();
						Timbre timbreMenorSalida = new Timbre();
						long diferenciaMenorEntrada = 0;
						long diferenciaMenorSalida = 0;

						for (Timbre timbre : timbres) {
							if (timbre.getAccion().equals("5")) {
								timbreMenorEntrada = timbre;
								diferenciaMenorEntrada = timbreMenorEntrada
										.getFechaHoraTimbre().getTime()
										- this.getInstance().getFechaDesde()
												.getTime();
							}
						}

						for (Timbre timbre : timbres) {
							if (timbre.getAccion().equals("5")) {
								if (diferenciaMenorEntrada > Math.abs(timbre
										.getFechaHoraTimbre().getTime()
										- this.getInstance().getFechaDesde()
												.getTime())) {
									timbreMenorEntrada = timbre;
								}
							}
						}

						this.getInstance().setHoraSalida(
								timbreMenorEntrada.getFechaHoraTimbre());

						for (Timbre timbre : timbres) {
							if (timbre.getAccion().equals("4")) {
								timbreMenorSalida = timbre;
								diferenciaMenorSalida = timbreMenorSalida
										.getFechaHoraTimbre().getTime()
										- this.getInstance().getFechaHasta()
												.getTime();
								log.info("==================== timbreMenorSalida "
										+ timbreMenorSalida
												.getFechaHoraTimbre());
								log.info("==================== diferenciaMenorSalida "
										+ diferenciaMenorSalida);
							}
						}

						for (Timbre timbre : timbres) {
							if (timbre.getAccion().equals("4")) {

								log.info("==================== timbreMenorSalida "
										+ timbreMenorSalida
												.getFechaHoraTimbre());
								log.info("==================== diferenciaMenorSalida "
										+ diferenciaMenorSalida);

								if (diferenciaMenorSalida > Math.abs(timbre
										.getFechaHoraTimbre().getTime()
										- this.getInstance().getFechaHasta()
												.getTime())) {
									timbreMenorSalida = timbre;
								}
							}
						}

						this.getInstance().setHoraRetorno(
								timbreMenorSalida.getFechaHoraTimbre());
						log.info("===================== timbreMenorSalida.getFechaHoraTimbre() "
								+ timbreMenorSalida.getFechaHoraTimbre());
						log.info("===================== timbreMenorEntrada.getFechaHoraTimbre() "
								+ timbreMenorEntrada.getFechaHoraTimbre());

					}

					// Graba asistencias modificadas
					// this.actualizarAsistenciaPermiso(asistenciasUpdate);

					// this.grabar();
					// estado = "persisted";

				}
				// SI NO EXISTE TIMBRES
				// else {
				// List<Asistencia> asistenciasUpdate =
				// this.revisarAsistenciaSalida(asistencias);
				// this.actualizarAsistenciaPermiso(asistenciasUpdate);
				// }
				
			// Modificacion realizada para grabar un permiso por horas o minutos en la asistencia,
			// se requiere hacer una restructuracion completa de este proceso
			// se dejara lo de arriba hasta tanto no se conozca que es lo que realiza
				// Alvaro Figueroa 26/04/2016
				
				
			//========Actualizacion de Asistencia Permisos por Horas===========
				for(Asistencia sa : asistencias){
					
					if(sa.getEntradaSalida().equals("E")){
       				    permisoTime = Fechas.cambiarFormato(this.getInstance().getFechaDesde(), "hh:mm:ss");						
					}
					
					if(sa.getEntradaSalida().equals("S")){
						permisoTime = Fechas.cambiarFormato(this.getInstance().getFechaHasta(), "hh:mm:ss");	
					}
					
					asistenciaTime = Fechas.cambiarFormato(sa.getHoraHorario(), "hh:mm:ss");
					
					log.info("!!!!!!!!!!!!!!!!!!!asistenciaTime " + asistenciaTime);
					log.info("!!!!!!!!!!!!!!!!!!!permisoTime " + permisoTime);
					
					if(asistenciaTime.equals(permisoTime)){
						sa.setEstado(Constantes.ASISTENCIA_PERMISO);	
					}
				}
				
				log.info("!!!!!!!!!!!!!!!!!!! " + asistencias);
				this.actualizarAsistenciaPermiso(asistencias);
			//===============================FIN================================	
			
				ActualizaPermiso = false;
				this.grabar();
				ActualizaPermiso = true;
				estado = "updated";

				// estado = "persisted";
			}
		} else if (numeroDiasPermiso != 0) {

			// Limpia la lista de aistencias para poder seleccionar fechaDesde y
			// fechaHasta
			// sino cambia el estado solo a una asistencia
			asistencias.clear();
			// asistencias =
			// this.consultarAsistencia(this.instance.getFechaDesde(),
			// this.instance.getFechaHasta());
			asistencias = this.buscarAsistencias();

			log.info("========================= asistencias.size() "
					+ asistencias.size());

			if (asistencias.size() == 0)
				autorizar = false;
			else
				autorizar = true;

			if (autorizar) {
				// Autorizar permiso por días
				for (Asistencia asistencia : asistencias) {
					// Coloca la misma hora de asistencia en timbre y cambia
					// todos los estados a permiso
					// asistencia.setFechaHoraTimbre(asistencia.getFechaHoraHorario());
					asistencia.setEstado(Constantes.ASISTENCIA_PERMISO);
				}

				this.actualizarAsistenciaPermiso(asistencias);
				ActualizaPermiso = false;
				this.grabar();
				ActualizaPermiso = true;
				estado = "updated";
			}
		}

		if (autorizar) {



//			AUMENTA FECHA DE ULTIMA VACACION LOS DIAS QUE TENGA EN PERMISO
			if (this.getInstance().getTipoPermiso().isAfectaFechaVacacion()
					&& this.getInstance().getDias() > 0) {
				this.actualizarPeriodoVacacion(this.getInstance().getDias());
			}

			if (gestionPermisoVacacion.buscarSaldoVacaciones(this.getInstance()
					.getEmpleadoPeriodoVacacion()) < 0) {
				this.actualizarEstadoNegativo(new Long(-1));
			}

			// *********new
			if(this.instance.getNivelRequerido() == null){
				this.instance.setNivelRequerido(this.getNivel());
			}
			this.instance.setAprobacion(this.instance.getNivelRequerido());
			this.instance.setEstadoActual("2");
			super.update();

			this.empleado = gestionPermisoVacacion.buscarEmpleado();

			PermisoAutorizacion permisoAutorizacion = new PermisoAutorizacion();

			permisoAutorizacion.setPermiso(this.getInstance());
			permisoAutorizacion.setEmpleado(empleado);
			permisoAutorizacion.setNivel(this.instance.getNivel());

			permisoAutorizacion.setEstado("0");

			permisoAutorizacionHome.setInstance(permisoAutorizacion);
			permisoAutorizacionHome.persist();
			// *********new

			if (this.getInstance().getTipoPermiso().getAutorizar()) {
				// Envia Correo Electronico
				EnviarMensajePermisoAuto();
				// Fin Envia Correo Electronico
			}

			FacesMessages.instance().clear();
			FacesMessages.instance().add("Solicitud Autorizada");
		} else {
			InvalidValue iv = new InvalidValue(
					"No existe una planificación previa!\nRealice el proceso de planificación antes de autorizar un permiso!",
					null, null, null, null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}

		ActualizaPermiso = true;
		return estado;
	}

	// new Aprobacion por bloque
	public String AutorizarPermisos(Permiso permiso) {
		
		String mensaje = "";
		// bloque
		this.setNivel(permiso.getNivelRequerido());

		if (this.getDiasHorasSolicitud() == null) {
			this.setDiasHorasSolicitud(this.getInstance().getDias()
					+ (this.getInstance().getNumeroHoras() / 8));
		}
		this.setCadenaAnterior(this.getInstance().toString());
		return mensaje = this.AutorizarPermiso();
	}

	// SE EJECUTA CUANDO SE AUTORIZA EL PERMISO
	public void actualizarPeriodoVacacion(Integer dias) {
		// VALIDAR PORQUE LA CONSULTA PUEDE TRAER MAS DE UN REGISTRO O NO PUEDE
		// TRAER NINGUNO

		List<EmpleadoPeriodoVacacion> empleadoPeriodoVacacions = this
				.buscarUltimoPeriodoVacacion();
		if (empleadoPeriodoVacacions.size() == 1) {
			EmpleadoPeriodoVacacion empleadoPeriodoVacacion = empleadoPeriodoVacacions
					.get(0);
			Calendar nuevaFechaVacaciones = Fechas.calendarioActual();
			nuevaFechaVacaciones.setTime(empleadoPeriodoVacacion
					.getFechaDesde());

			nuevaFechaVacaciones.add(Calendar.DATE, dias);

			empleadoPeriodoVacacion.setFechaDesde(nuevaFechaVacaciones
					.getTime());
			empleadoPeriodoVacacionHome.update();
		}
	}

	public void actualizarEstadoNegativo(Long estadoSaldoVacaciones) {
		// VALIDAR PORQUE LA CONSULTA PUEDE TRAER MAS DE UN REGISTRO O NO PUEDE
		// TRAER NINGUNO
		List<EmpleadoPeriodoVacacion> empleadoPeriodoVacacions = this
				.buscarUltimoPeriodoVacacion();
		if (empleadoPeriodoVacacions.size() == 1) {
			EmpleadoPeriodoVacacion empleadoPeriodoVacacion = empleadoPeriodoVacacions
					.get(0);

			// empleadoPeriodoVacacionHome.wire();
			// empleadoPeriodoVacacionHome.setInstance(empleadoPeriodoVacacion);
			empleadoPeriodoVacacion.setEstado(estadoSaldoVacaciones);
			empleadoPeriodoVacacionHome.update();
		}
	}

	// MODIFICADO PARA MSSQL
	public List<Asistencia> consultarAsistencia(Date fechaDesde, Date fechaHasta) {

		// Hay que hacer esto sino coloca 00:00 en permiso.fechaDesde
		// Solo para MSSQl en mysql funciona sin esto
		// GRABAR TEMPORALMENTE HORAS Y MINUTOS

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFechaDesde = Fechas.cambiarFormato(fechaDesde,
				"yyyy-MM-dd");
		String stringFechaHasta = Fechas.cambiarFormato(fechaHasta,
				"yyyy-MM-dd");

		Date fechaDesdePermiso = null;
		Date fechaHastaPermiso = null;
		try {
			fechaDesdePermiso = formato.parse(stringFechaDesde);
			fechaHastaPermiso = formato.parse(stringFechaHasta);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance(
				"asistenciaList", true);

		asistenciaList
				.getAsistencia()
				.getEmpleado()
				.setCodigoEmpleado(
						this.getInstance().getEmpleadoByEmplId()
								.getCodigoEmpleado());
		asistenciaList.getAsistencia().setFechaDesde(fechaDesdePermiso);
		asistenciaList.getAsistencia().setFechaHasta(fechaHastaPermiso);

		List<Asistencia> asistencias = asistenciaList.getListaAsistencias();

		return asistencias;
	}

	@SuppressWarnings("unchecked")
	public List<Asistencia> buscarAsistencias() {

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFechaDesde = Fechas.cambiarFormato(this.getInstance()
				.getFechaDesde(), "yyyy-MM-dd");
		String stringFechaHasta = Fechas.cambiarFormato(this.getInstance()
				.getFechaHasta(), "yyyy-MM-dd");

		Date fechaDesdePermiso = null;
		Date fechaHastaPermiso = null;
		try {
			fechaDesdePermiso = formato.parse(stringFechaDesde);
			fechaHastaPermiso = formato.parse(stringFechaHasta);
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
				.setParameter("permisoDesde", fechaDesdePermiso)
				.setParameter("permisoHasta", fechaHastaPermiso)
				.getResultList();
	}

	// Modifica las asistencias cuando se autoriza un permiso
	public List<Asistencia> revisarAsistenciaSalida(List<Asistencia> asistencias) {
		List<Asistencia> asistenciasUpdate = new ArrayList<Asistencia>();
		Calendar permisoHasta = Calendar.getInstance();
		permisoHasta.setTime(this.getInstance().getFechaHasta());

		Calendar permisoDesde = Calendar.getInstance();
		permisoDesde.setTime(this.getInstance().getFechaDesde());

		for (Asistencia asistencia : asistencias) {
			Calendar asistenciaFechaHoraHorario = Calendar.getInstance();
			asistenciaFechaHoraHorario
					.setTime(asistencia.getFechaHoraHorario());

			if (permisoDesde.getTimeInMillis() <= asistenciaFechaHoraHorario
					.getTimeInMillis()
					&& permisoHasta.getTimeInMillis() >= asistenciaFechaHoraHorario
							.getTimeInMillis()) {
				// asistencia.setFechaHoraTimbre(asistencia.getFechaHoraHorario());
				asistencia.setEstado(Constantes.ASISTENCIA_PERMISO);
				asistenciasUpdate.add(asistencia);
			} else {
				// asistencia.setFechaHoraTimbre(null);
				// asistencia.setEstado(Constantes.ASISTENCIA_FALTA);
				asistenciasUpdate.add(asistencia);
			}
		}
		return asistenciasUpdate;
	}

	public Asistencia buscarAsistenciaEntradaSalida(Date fecha,
			String entradaSalida) {

		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFechaDesde = Fechas.cambiarFormato(fecha, "yyyy-MM-dd");

		Date fechaPermiso = null;
		try {
			fechaPermiso = formato.parse(stringFechaDesde);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance(
				"asistenciaList", true);

		asistenciaList
				.getAsistencia()
				.getEmpleado()
				.setCodigoEmpleado(
						this.getInstance().getEmpleadoByEmplId()
								.getCodigoEmpleado());
		asistenciaList.setFechaAsistenciaHorario(fechaPermiso);
		asistenciaList.getAsistencia().setEntradaSalida(entradaSalida);

		List<Asistencia> asistencias = asistenciaList.getListaAsistencias();
		Asistencia asistencia = new Asistencia();
		for (Asistencia asistencia2 : asistencias) {
			asistencia = asistencia2;
		}

		return asistencia;
	}

	public void cambiarEstadoAutorizacion(Long decision) {
		this.detalleTipoParametroHome.setId(decision);
		DetalleTipoParametro detalleTipoParametro = this.detalleTipoParametroHome
				.find();
		this.getInstance().setDetalleTipoParametro(detalleTipoParametro);
	}

	@Transactional
	public String actualizarAsistenciaPermiso(List<Asistencia> asistencias) {
		AsistenciaHome asistenciaHome = (AsistenciaHome) Component.getInstance(
				"asistenciaHome", true);
		asistenciaHome.updateAsistencia(asistencias);
		return "persisted";
	}

	// RETORNA EL NUMERO DE DIAS DE PERMISO QUE TIENE EN UN AÑO
	// PARA CONTROLAR QUE NO SE PASE EL LIMITE PERMITIDO
	public int validarPermisoAcumulaAnios() {

		int dias = this.getInstance().getDias();

		List<Permiso> permisosAfectaVacacion = this.buscarPermisoPorTipo(this
				.getInstance());
		for (Permiso permiso : permisosAfectaVacacion) {
			dias = dias + permiso.getDias();
		}

		return dias;
	}

	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisoPorTipo(Permiso permiso) {

		// SE UTILIZA PARA OBTENER EL AÑO DEL PERMISO
		// LUEGO A LA FECHA DE INGRESO DEL EMPLEADO SE LE CAMBIA POR ESTE AÑO
		// PARA OBTENER LA FECHA DESDE
		// A ESTA FECHA SE LE SUMA 1 AÑO PARA OBTENER LA FECHA HASTA
		// Y SE REALIZA LA BUSQUEDA DE PERMISOS CON ESTOS RANGOS
		Calendar calendarioPermisoDesde = Fechas.calendarioActual();
		calendarioPermisoDesde.setTime(permiso.getFechaDesde());
		
		Calendar calendarioIngresoEmpleadoDesde = Fechas.calendarioActual();
		
//		Fecha de ingreso se movio a HIST_LABO
//		calendarioIngresoEmpleadoDesde.setTime(permiso.getEmpleadoByEmplId().getFechaIngreso());

		HistLabo histLabo = gestionPermisoVacacion.buscarHistoriaLaboralActivo(permiso.getEmpleadoByEmplId());
		calendarioIngresoEmpleadoDesde.setTime(histLabo.getFechaIngreso());
				
		calendarioIngresoEmpleadoDesde.set(Calendar.YEAR,
				calendarioPermisoDesde.get(Calendar.YEAR));

		Calendar calendarioIngresoEmpleadoHasta = Fechas.calendarioActual();
		calendarioIngresoEmpleadoHasta = (Calendar) calendarioIngresoEmpleadoDesde
				.clone();
		calendarioIngresoEmpleadoHasta.add(Calendar.YEAR, 1);

		Date permisoDesde = calendarioIngresoEmpleadoDesde.getTime();
		Date permisoHasta = calendarioIngresoEmpleadoHasta.getTime();

		boolean acumulaAnios = true;

		return (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p inner join p.tipoPermiso tp where p.empleadoByEmplId.emplId = (:empleado) And tp.acumulaAnios = (:acumulaAnios) And "
								+ ControlBaseDatos.modificadorConvertirFecha
								+ "p.fechaDesde) Between (:permisoDesde) and (:permisoHasta)")
				.setParameter("empleado",
						permiso.getEmpleadoByEmplId().getEmplId())
				.setParameter("acumulaAnios", acumulaAnios)
				.setParameter("permisoDesde", permisoDesde)
				.setParameter("permisoHasta", permisoHasta).getResultList();
	}

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

	// ESTE PROCESO SE LLAMA ASI DESDE LA PAGINA
	// valueChangeListener="#{permisoHome.valueChangedEventEmpleado}"
	// immediate="true"
	public void valueChangedEventEmpleado(ValueChangeEvent event) {

		Empleado empleado = ((Empleado) event.getNewValue());

		if (this.periodoUnico) {
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
				.buscarSaldoVacacionesPeriodo(empleadoPeriodoVacacion));

		this.setSaldoDias(gestionPermisoVacacion
				.saldoVacacionDias(getSaldoVacaciones()));
		this.setSaldoHoras(gestionPermisoVacacion
				.saldoVacacionHoras(getSaldoVacaciones()));
		this.setSaldoMinutos(gestionPermisoVacacion
				.saldoVacacionMinutos(getSaldoVacaciones()));
	}

	public void buscarTipoPermiso(ValueChangeEvent event) {

		TipoPermiso tipoPermiso = ((TipoPermiso) event.getNewValue());
		// City city = null;
		// if (!getCities().isEmpty()){
		// city = getCities().get(0);
		// }
		// client.setCity(city);
		// FacesContext.getCurrentInstance().renderResponse();

		// TipoPermiso tipoPermiso = tipoPermisoHome.getDefinedInstance();
		// if (tipoPermiso != null) {
		// getInstance().setTipoPermiso(tipoPermiso);
		// } else {
		// tipoPermiso = new TipoPermiso();
		this.getInstance().setTipoPermiso(tipoPermiso);

		try {
			Integer nivelCiudad = this.getInstance().getEmpleadoByEmplId()
					.getCiudad().getNivel();
			Integer nivelTipoPermiso = tipoPermiso.getNivel();

			if (nivelCiudad > 0)
				this.nivelesRequeridos = nivelCiudad;
			else
				this.nivelesRequeridos = nivelTipoPermiso;
		} catch (Exception ex) {
			this.nivelesRequeridos = tipoPermiso.getNivel();
		}
		
		this.diferenciaFechas();
		
		// }
	}

	public void permisoHorasMinutos() {
		int horas, minutos;
		horas = this.getInstance().getNumeroHoras().intValue();
		minutos = (int) Math
				.round((this.getInstance().getNumeroHoras() - horas) * 60);
		this.setHorasMinutos(horas + ":" + minutos);
	}

	public boolean isGestionaPermiso() {
		return gestionaPermiso;
	}

	public void setGestionaPermiso(boolean gestionaPermiso) {
		this.gestionaPermiso = gestionaPermiso;
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

	public String getHorasMinutos() {
		return horasMinutos;
	}

	public void setHorasMinutos(String horasMinutos) {
		this.horasMinutos = horasMinutos;
	}

	public boolean isPeriodoUnico() {
		return periodoUnico;
	}

	public void setPeriodoUnico(boolean periodoUnico) {
		this.periodoUnico = periodoUnico;
	}

	public boolean isAutorizaPermiso() {
		return autorizaPermiso;
	}

	public void setAutorizaPermiso(boolean autorizaPermiso) {
		this.autorizaPermiso = autorizaPermiso;
	}

	public boolean isLegalizaPermiso() {
		return legalizaPermiso;
	}

	public void setLegalizaPermiso(boolean legalizaPermiso) {
		this.legalizaPermiso = legalizaPermiso;
	}

	public boolean isEliminaPermiso() {
		return eliminaPermiso;
	}

	public void setEliminaPermiso(boolean eliminaPermiso) {
		this.eliminaPermiso = eliminaPermiso;
	}

	/* envia correo */

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
		String motivo = this.instance.getTipoPermiso().getDescripcion();
		String fechaSolicitud = ""
				+ RetornarFecha(this.instance.getFecha(), "all");
		String fechaDesde = "";
		String fechaHasta = "";
		String observacion = this.instance.getObservacion();
		String nivelRequerido = "" + this.instance.getNivelRequerido();
		String aprobacion = "" + this.instance.getAprobacion();
		String estadoAux = this.instance.getEstadoActual().trim();
		String estado = "";

		if (this.instance.getDias() > 0) {
			fechaDesde = RetornarFecha(this.instance.getFechaDesde(), "");
			fechaHasta = RetornarFecha(this.instance.getFechaHasta(), "");
		} else {
			fechaDesde = RetornarFecha(this.instance.getFechaDesde(), "all");
			fechaHasta = RetornarFecha(this.instance.getFechaHasta(), "all");
		}

		if(observacion==null){
			observacion = "Ninguna";
		}else{
			if (observacion.length() == 0){
				observacion = "Ninguna";
			}
		}

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
				+ "<b>Observaci&#243;n:</b>&#32;"
				+ observacion
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
		List<PermisoAutorizacion> autorizaList = getListaPermisoAutoriza();
		int i = autorizaList.size();
		int j = 1;
		for (PermisoAutorizacion pa : autorizaList) {
			if (j == i){
				enviarMail.enviar(cuerpo, pa.getEmpleado().getCorreo(), asunto,"J");
				/*******************ENVIAR CORREO ALETERNATIVO JEFE********************
				String alternativo=pa.getEmpleado().getCorreoAlternativo();
				if(alternativo!=null){
					if(alternativo.length()>0){
						enviarMail.enviar(cuerpo, alternativo, asunto, "E");
					}
				}
				/**********************************************************************/
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

	public void EnviarMensajePermisoCreado() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha creado la siguiente solicitud de permiso:<br><br>"
				+ Cuerpo() + "<br><br><b>Solicitado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Permiso";
		EnviarMailSimple(CuerpoMensaje, Asunto);
	}

	public void EnviarMensajePermisoActualizacion() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar cambios de la siguiente solicitud de permiso:<br><br>"
				+ "<center><h1>SOLICITUD ANTERIOR</h1></center><br><br>"
				+ cuerpo
				+ "<br><br>"
				+ "<center><h1>SOLICITUD ACTUALIZADA</h1></center><br><br>"
				+ Cuerpo()
				+ "<br><br><b>Actualizado por: </b>"
				+ e
				+ "<br><br>";
		String Asunto = "Solicitud de Permiso Actualizada";

		if (this.instance.getEstadoActual().trim().equals("0")) {
			EnviarMailSimple(CuerpoMensaje, Asunto);
		} else {
			EnviarMailMultiple(CuerpoMensaje, Asunto);
		}
		cuerpo = "";
	}

	public void EnviarMensajePermisoEliminacion() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha eliminado la siguiente solicitud de permiso:<br><br>"
				+ Cuerpo() + "<br><br><b>Eliminado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Permiso Eliminada";

		if (this.instance.getEstadoActual().trim().equals("0")) {
			EnviarMailSimple(CuerpoMensaje, Asunto);
		} else {
			EnviarMailMultiple(CuerpoMensaje, Asunto);
		}
	}

	public void EnviarMensajePermisoPreAuto() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha autorizado previamente la siguiente solicitud de permiso:<br><br>"
				+ Cuerpo()
				+ "<br><br><b>Pre-Autorizado por: </b>"
				+ e
				+ "<br><br>";
		String Asunto = "Solicitud de Permiso Pre-Autorizada";

		EnviarMailMultiple(CuerpoMensaje, Asunto);
	}

	public void EnviarMensajePermisoAuto() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que se ha autorizado la siguiente solicitud de permiso:<br><br>"
				+ Cuerpo() + "<br><br><b>Autorizado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Permiso Autorizada";

		EnviarMailMultiple(CuerpoMensaje, Asunto);
	}

	public void EnviarMensajePermisoNoAuto() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que no se ha autorizado la siguiente solicitud de permiso:<br><br>"
				+ Cuerpo() + "<br><br><b>Negado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Permiso No-Autorizada";

		EnviarMailMultiple(CuerpoMensaje, Asunto);
	}

	public void EnviarMensajePermisoLegali() {
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		String e = empleado.getApellido() + " " + empleado.getNombre();

		String CuerpoMensaje = "Este correo es para informar que no se ha legalizado la siguiente solicitud de permiso:<br><br>"
				+ Cuerpo() + "<br><br><b>Legalizado por: </b>" + e + "<br><br>";
		String Asunto = "Solicitud de Permiso Legalizada";

		EnviarMailMultiple(CuerpoMensaje, Asunto);
	}

	// new methods
	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public String getArchivoNombre() {
		return archivoNombre;
	}

	public void setArchivoNombre(String archivoNombre) {
		this.archivoNombre = archivoNombre;
	}

	public String getArchivoNombre2() {
		return archivoNombre2;
	}

	public void setArchivoNombre2(String archivoNombre2) {
		this.archivoNombre2 = archivoNombre2;
	}
	
	
	public Boolean justificacion(){
		Boolean resultado = true;
			
		// True puede justificar el permiso
		// False no puede justificar el permiso
		msgError = "";
		Date fechaHoy = new Date();
		Date fechaPermisoDesde = this.getInstance().getFechaDesde();
		Calendar tiempo = Calendar.getInstance();
		
	   	if(this.getInstance().getEstadoActual().equals("0")){ // Solo activo la regla para permisos pendientes
					
			int justificacion = this.getInstance().getTipoPermiso().getDiasJustificacion();
			
			if(justificacion > 0){		
				
				tiempo.setTime(fechaPermisoDesde);
				tiempo.add(Calendar.DATE, justificacion);
				
				if(fechaHoy.after(tiempo.getTime())){
					resultado = false;
				}
	
			}
		}
		/*
		log.info("!!!!!! Fecha Hoy " + fechaHoy);
		log.info("!!!!!! Fecha Desde " + this.getInstance().getFechaDesde());
		log.info("!!!!!! Fecha Limite " + tiempo.getTime());
		*/
		if(!resultado){
			msgError = "El tiempo de justificacion del permiso esta excedido";
		}
	
		return resultado;
	}
	
	
	public void subirArchivo(UploadEvent event) throws Exception {
		
		 
	    	// log.info("*****************************************************************Q ES: "
	  		// + event.getUploadItem().getContentType());
	  		String type = event.getUploadItem().getContentType();
	  		type = type.substring(type.length() - 3, type.length());
	  		String ext = "";
	  		if (type.equals("pdf")) {
	  			ext = ".pdf";
	  		} else {
	  			ext = ".jpg";
	  		}
	  		// log.info("/*/*/*/*/*/*/*/*/*/*/*/Archivo: " + this.archivo);

	  		this.archivoNombre2 = ""
	  				+ this.getInstance().getEmpleadoByEmplId().getEmplId();

	  		// String path ="D:\\Permisos\\";

	  		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
	  				.getInstance("detalleTipoParametroList", true);
	  		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

	  		detalleTipoParametros = detalleTipoParametroList
	  				.getRutaArchivoPermiso();
	  		String path = detalleTipoParametros.getDescripcion();

	  		String dir = this.getInstance().getEmpleadoByEmplId().getEmplId() + "_"
	  				//+ this.getInstance().getEmpleadoByEmplId().getApellido() + " "
	  				//+ this.getInstance().getEmpleadoByEmplId().getNombre();
	  				+RemoverCaracteresEspeciales(this.getInstance().getEmpleadoByEmplId().getApellido()+" "+this.getInstance().getEmpleadoByEmplId().getNombre());

	  		File folder = new File(path + dir);

	  		if (folder.exists()) {
	  			path = path + dir + "/";
	  		} else {
	  			folder.mkdir();
	  			path = path + dir + "/";
	  		}

	  		String aux = "" + this.getInstance().getFechaDesde();
	  		String fecha = "";
	  		for (int i = 0; i < aux.length(); i++) {
	  			if (aux.charAt(i) != '.' && aux.charAt(i) != ':' && aux.charAt(i) != ' ') {
	  				fecha = fecha + aux.charAt(i);
	  			}
	  		}

	  		folder = new File(path + fecha);

	  		if (folder.exists()) {
	  			path = path + fecha + "/";
	  		} else {
	  			folder.mkdir();
	  			path = path + fecha + "/";
	  		}

	  		int i = 0;
	  		while (i >= 0) {
	  			File fichero = new File(path + this.archivoNombre2 + "-" + i + ext);
	  			if (fichero.exists()) {
	  				i++;
	  			} else {
	  				this.archivoNombre2 = this.archivoNombre2 + "-" + i + ext;
	  				i = -1;
	  			}
	  		}

	  		// File f = new File(path, this.archivoNombre2);

	  		String sourceFile = "" + event.getUploadItem().getFile();
	  		String destFile = path + this.archivoNombre2;

	  		FileChannel origen = null;
	  		FileChannel destino = null;
	  		// try {
	  		origen = new FileInputStream(sourceFile).getChannel();
	  		destino = new FileOutputStream(destFile).getChannel();

	  		long count = 0;
	  		long size = origen.size();
	  		while ((count += destino.transferFrom(origen, count, size - count)) < size)
	  			;
	  		// }
	  		// finally {
	  		// if(origen != null) {
	  		origen.close();
	  		// }
	  		// if(destino != null) {
	  		destino.close();
	  		// }
	  		// }
 
	 
	}


	private Archivo arch;

	public List<Archivo> listaArchivo() {
		List<Archivo> a = new ArrayList<Archivo>();

		// String path ="D:\\Permisos\\";

		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();

		detalleTipoParametros = detalleTipoParametroList
				.getRutaArchivoPermiso();
		String path = detalleTipoParametros.getDescripcion();

		String dir = this.getInstance().getEmpleadoByEmplId().getEmplId() + "_"
//				+ this.getInstance().getEmpleadoByEmplId().getApellido() + " "
//				+ this.getInstance().getEmpleadoByEmplId().getNombre();
				+RemoverCaracteresEspeciales(this.getInstance().getEmpleadoByEmplId().getApellido()+" "+this.getInstance().getEmpleadoByEmplId().getNombre());

		String aux = "" + this.getInstance().getFechaDesde();
		String fecha = "";
		for (int i = 0; i < aux.length(); i++) {
			if (aux.charAt(i) != '.' && aux.charAt(i) != ':' && aux.charAt(i) != ' ') {
				fecha = fecha + aux.charAt(i);
			}
		}

		File folder = new File(path + dir + "/" + fecha);

		if (folder.exists()) {

			this.archivoNombre = ""
					+ this.getInstance().getEmpleadoByEmplId().getEmplId();
			path = path + dir + "/" + fecha + "/";
			String[] arregloArchivos = folder.list();
			int numArchivos = arregloArchivos.length;

			int y = 0;
			int x = 0;
			boolean y1 = false;
			boolean y2 = false;

			File fichero;
			while (y < numArchivos) {
				// validator

				fichero = new File(path + this.archivoNombre + "-" + x + ".jpg");
				if (!y1) {
					y1 = true;
					if (fichero.exists()) {
						arch = new Archivo(this.archivoNombre + "-" + x
								+ ".jpg", path + this.archivoNombre + "-" + x
								+ ".jpg");
						a.add(arch);
						y++;
					}
					// x++;
				} else {
					fichero = new File(path + this.archivoNombre + "-" + x
							+ ".pdf");
					if (!y2) {
						y2 = true;
						if (fichero.exists()) {
							arch = new Archivo(this.archivoNombre + "-" + x
									+ ".pdf", path + this.archivoNombre + "-"
									+ x + ".pdf");
							a.add(arch);
							y++;
						}
						// x++;
					} else {
						x++;
						y1 = false;
						y2 = false;
					}
				}
			}
		}

		return a;
	}

	public void eliminar_Archivo() {
		File ficheroFile = new File(url);
		if (ficheroFile.delete()) {
			FacesMessages.instance().clear();
			FacesMessages.instance().add("Archivo eliminado!");
		} else {
			FacesMessages.instance().clear();
			FacesMessages.instance().add("No se puedo eliminar el archivo!");
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// private int num;
	// boolean ac;
	// public boolean activado(){
	//
	// boolean ac=false;
	//
	// String user=credentials.getUsername();
	// String
	// user_actual=this.getInstance().getEmpleadoByEmplId().getUsuarios().getUsuario();
	//
	// if(user.equals(user_actual)){
	// if(this.num==0){
	//
	// Calendar fecha_actual= Calendar.getInstance();
	// int dias=this.getInstance().getTipoPermiso().getDiasJustificacion();
	// Date limit=new Date();
	// limit=this.getInstance().getFechaDesde();
	//
	// int dias_fecha=this.getInstance().getFechaDesde().getDate();
	// int aux= dias_fecha+dias;
	// limit.setDate(aux);
	//
	// if(fecha_actual.getTime().before(limit)){
	// ac=false;
	// }else{
	// ac=true;
	// }
	// dias_fecha=this.getInstance().getFechaDesde().getDate();
	// aux=dias_fecha-dias;
	// limit=this.getInstance().getFechaDesde();
	// limit.setDate(aux);
	//
	// }
	//
	// }else{
	// ac=false;
	// }
	//
	// this.num=1;
	//
	// return ac;
	// }

	public void eliminar_Archivos() {

		log.info("ENTRO A ELIMINAR!!!");

		List<Archivo> a = new ArrayList<Archivo>();
		// String path ="D:\\Permisos\\";
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		detalleTipoParametros = detalleTipoParametroList
				.getRutaArchivoPermiso();
		String path = detalleTipoParametros.getDescripcion();
		String dir = this.getInstance().getEmpleadoByEmplId().getEmplId() + "_"
				//+ this.getInstance().getEmpleadoByEmplId().getApellido() + " "
				//+ this.getInstance().getEmpleadoByEmplId().getNombre();
				+RemoverCaracteresEspeciales(this.getInstance().getEmpleadoByEmplId().getApellido()+" "+this.getInstance().getEmpleadoByEmplId().getNombre());
		String aux = "" + this.getInstance().getFechaDesde();
		String fecha = "";
		for (int i = 0; i < aux.length(); i++) {
			if (aux.charAt(i) != '.' && aux.charAt(i) != ':' && aux.charAt(i) != ' ') {
				fecha = fecha + aux.charAt(i);
			}
		}
		File folder = new File(path + dir + "/" + fecha);
		if (folder.exists()) {
			this.archivoNombre = ""
					+ this.getInstance().getEmpleadoByEmplId().getEmplId();
			path = path + dir + "/" + fecha + "/";
			String[] arregloArchivos = folder.list();
			int numArchivos = arregloArchivos.length;
			int y = 0;
			int x = 0;
			while (y < numArchivos) {
				File fichero = new File(path + this.archivoNombre + "-" + x
						+ ".jpg");
				if (fichero.exists()) {
					/*
					 * arch=new
					 * Archivo(this.archivoNombre+"-"+x+".jpg",path+this
					 * .archivoNombre+"-"+x+".jpg"); a.add(arch);
					 */
					url = path + this.archivoNombre + "-" + x + ".jpg";
					File ficheroFile = new File(url);
					ficheroFile.delete();
					y++;

				}

				fichero = new File(path + this.archivoNombre + "-" + x + ".pdf");
				if (fichero.exists()) {
					url = path + this.archivoNombre + "-" + x + ".pdf";
					File ficheroFile = new File(url);
					ficheroFile.delete();
					y++;
				}
				x++;
			}

			// folder.delete();
		}
		folder.delete();
	}
	
	//Eliminar Caracteres Especiales
	public static String RemoverCaracteresEspeciales(String input) {
	    // Cadena de caracteres original a sustituir.
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ ";
	    // Cadena de caracteres ASCII que reemplazarán los originales.
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC_";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {
	        // Reemplazamos los caracteres especiales.
	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }//for i
	    return output;
	}
	// REVISAR
	// public boolean verAccion(){
	// String user=credentials.getUsername();
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
	// //usuariosRolesList.usuariosRoles.usuarios.usuario}
	//
	// usuariosRolesList.setUsuario(user);
	//
	// usuariosRoles=usuariosRolesList.getSingleResult();
	//
	// if(usuariosRoles.getRoles().getDescripcion().equals("Administrador")){
	// return false;
	// } else {
	// return true;
	// }
	// }

	// public boolean ReporteAccion(){
	// //System.out.println("OK...........................:"+this.getInstance().getDetalleTipoParametro().getDtpaId());
	// if(this.getInstance().getDetalleTipoParametro().getDtpaId()==3){
	// //System.out.println("OK2...........................:"+this.getInstance().getCodigo());
	// if(this.getInstance().getCodigo()!=null){
	// //
	// System.out.println("OK3...........................:"+this.getInstance().getCodigo());
	// if(this.getInstance().getCodigo().equals("")){
	// return false;
	// }else{
	// return true;
	// }
	//
	// }else{
	// return false;
	//
	// }
	//
	// }else{
	// return false;
	// }
	// }

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

	public Double getDiasHorasSolicitud() {
		return diasHorasSolicitud;
	}

	public void setDiasHorasSolicitud(Double diasHorasSolicitud) {
		this.diasHorasSolicitud = diasHorasSolicitud;
	}

	public void GestionAutorizacion() {
   
		DepartamentoAutorizacion departamentoAutoriza2 = new DepartamentoAutorizacion();
		if (this.isManaged()) {
			
			if (cuerpo.equals(""))
				cuerpo = Cuerpo();
			// *********visualizar nivel requerido
			Integer nivelCiudad = this.getInstance().getEmpleadoByEmplId()
					.getCiudad().getNivel();
			Integer nivelTipoPermiso = this.getInstance().getTipoPermiso()
					.getNivel();

			if (nivelCiudad > 0)
				this.nivelesRequeridos = nivelCiudad;
			else
				this.nivelesRequeridos = nivelTipoPermiso;
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
            log.info("LOG 3");
			if (departamentoAutorizacion.size() > 0) {
				 log.info("LOG 4");
				Integer nivelDepartamento = empleado.getDepartamento()
						.getNivel();
				// Integer
				// nivelTipoPermiso=this.getInstance().getTipoPermiso().getNivel();
				Integer nivelPermiso = this.getInstance().getNivel();
				// Integer
				// nivelCiudad=this.getInstance().getEmpleadoByEmplId().getCiudad().getNivel();
				Integer nivelAprobacion = this.getInstance().getAprobacion();

				String est = this.getInstance().getEstadoActual().trim();

				log.info("================================================== est : "
						+ est);
				log.info("================================================== this.getInstance().getEstadoActual() : "
						+ this.getInstance().getEstadoActual());

				if (nivelCiudad > 0)
					nivelTipoPermiso = nivelCiudad;

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
						
						log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++");
						
						for (DepartamentoAutorizacion da : departamentoAutorizacion) {
							log.info("++++++++++++++");
							Departamento departamento = da.getDepartamento();
							log.info("DEpar " + departamento);
							
							log.info("................................ departamento.getDepaId(): "
									+ departamento.getDepaId());
							log.info("................................ departamento.getNivel(): "
									+ departamento.getNivel());
							 log.info("LOG 5");
							if (departamento.getDepaId().equals(
									departamentoPermisoEmpleado.getDepaId())
									&& (departamento.getNivel()
											.equals(nivelPermiso))) {
								nivelDepartamento = nivelPermiso;
								departamentoAutoriza2 = da;
								break;
							}
						}
						log.info("================================================== ");
					}
					
			
					// **************************************************fin
					// recorrer listado

					Integer auxNivelAprobacion = nivelAprobacion + 1;

					if (est.equals("0") && nivelDepartamento > 1
							&& nivelTipoPermiso > 1) {
						Integer nivelRequerido = this.getInstance()
								.getNivelRequerido();
						if (nivelPermiso > nivelRequerido)
							auxNivelAprobacion = nivelTipoPermiso;
						else
							auxNivelAprobacion = nivelDepartamento;
					}

					// Departamento depSup =
					// empleado.getDepartamento().getDepartamento();
					Departamento depSup = null;
					if (departamentoPermisoEmpleado != null) {
						depSup = departamentoPermisoEmpleado.getDepartamento();
					}

					log.info("================================================== nivelesRequeridos:"
							+ this.nivelesRequeridos);
					log.info("================================================== nivelDepartamento:"
							+ nivelDepartamento);
					log.info("================================================== nivelPermiso:"
							+ nivelPermiso);
					log.info("================================================== nivelAprobacion:"
							+ nivelAprobacion);
					log.info("================================================== auxNivelAprobacion:"
							+ auxNivelAprobacion);
					log.info("================================================== nivelTipoPermiso:"
							+ nivelTipoPermiso);

					if ((depSup == null))
						log.info("================================================== depSup null");
					else
						log.info("================================================== depSup not null");

					if (((auxNivelAprobacion.equals(nivelTipoPermiso)) || (depSup == null))
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
					this.legalizacion = true;
				} else {
					this.eliminacion = false;
					// this.actualizacion=false;
					this.legalizacion = false;
				}

			} else {
				this.autorizar = false;
				this.preAutorizar = false;
				this.noAutorizar = false;

				this.legalizacion = false;

				String est = this.getInstance().getEstadoActual().trim();

				if (est.equals("0")) {
					this.eliminacion = true;
					// this.actualizacion=true;
				} else {
					this.eliminacion = false;
					// this.actualizacion=false;
				}

			}
			log.info("LOG6");
			if (getAccesoEmpleados() != Constantes.ACCESO_INDIVIDUAL
					&& getAccesoEmpleados() != Constantes.ACCESO_SUBORDINADOS) {
				this.eliminacion = true;
				// this.actualizacion=true;
				this.legalizacion = true;
			}
			
		if(getAccesoEmpleados() == Constantes.ACCESO_SUBORDINADOS){
			
			if(this.getInstance().getEmpleadoByEmplId().getEmplId() == this.empleado.getEmplId()){
				this.autorizar = false;
				this.preAutorizar = false;
				this.noAutorizar = false;	
			}
			
		}	


		} else {
			this.autorizar = false;
			this.preAutorizar = false;
			this.noAutorizar = false;

			this.eliminacion = false;
			// this.actualizacion=false;
			this.legalizacion = false;
		}
		
     

	}

	// end new methods

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

	// public Boolean getActualizacion() {
	// return actualizacion;
	// }
	//
	// public void setActualizacion(Boolean actualizacion) {
	// this.actualizacion = actualizacion;
	// }

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	public Boolean getLegalizacion() {
		return legalizacion;
	}

	public void setLegalizacion(Boolean legalizacion) {
		this.legalizacion = legalizacion;
	}

	public Integer getNivelesRequeridos() {
		return nivelesRequeridos;
	}

	public void setNivelesRequeridos(Integer nivelesRequeridos) {
		this.nivelesRequeridos = nivelesRequeridos;
	}
	
	

	public String getMsgError() {
		return msgError;
	}

	public void setMsgError(String msgError) {
		this.msgError = msgError;
	}

	private Boolean autorizar;
	private Boolean preAutorizar;
	private Boolean noAutorizar;

	private Boolean eliminacion;
	// private Boolean actualizacion;
	private Boolean legalizacion;

	private int accesoEmpleados;

	private Integer nivelesRequeridos;

	public List<PermisoAutorizacion> getListaPermisoAutoriza() {

		List<PermisoAutorizacion> lista = new ArrayList<PermisoAutorizacion>();

		permisoAutorizacionList.setPermiso(this.getInstance());
		lista = permisoAutorizacionList.getListaPermisoAutorizacion();

		return lista;
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

	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	private void ejecutarAlmuerzo(){
		
		log.info("..............................ENTRO METODO");
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String stringFechaDesde = Fechas.cambiarFormato(this.getInstance().getFechaDesde(), "yyyy-MM-dd");

		Date fechaPermiso = null;
		try {
			fechaPermiso = formato.parse(stringFechaDesde);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList", true);

		asistenciaList
		.getAsistencia()
		.getEmpleado()
		.setCodigoEmpleado(
				this.getInstance().getEmpleadoByEmplId()
						.getCodigoEmpleado());
		asistenciaList.setFechaAsistenciaHorario(fechaPermiso);
		asistenciaList.getAsistencia().setEntradaSalida(null);

		List<Asistencia> asistencias = asistenciaList.getListaAsistencias();		
		
		Date inicioAlmuerzo=null;
		Date finAlmuerzo=null;
		double minAlmuerzos = 0;
	
		for (Asistencia a : asistencias) {
			if(a.getEntradaSalida().equals("SA"))
				inicioAlmuerzo=a.getFechaHoraHorario();
			
			if(a.getEntradaSalida().equals("EA"))
				finAlmuerzo=a.getFechaHoraHorario();
			
			minAlmuerzos=a.getDetalleHorario().getHorario().getAlmuerzoMinutos();
		}
		
		if(inicioAlmuerzo!=null && finAlmuerzo!=null){
			log.info("..............................CONDICION PRINCIPAL");
			
			Date inicioPermiso=this.instance.getFechaDesde();
			Date finPermiso=this.instance.getFechaHasta();
			double diferencia=(double) (minAlmuerzos/60);
			double actual=this.instance.getNumeroHoras();
			
			if( (inicioPermiso.before(inicioAlmuerzo) || inicioPermiso.compareTo(inicioAlmuerzo)==0) && (finPermiso.after(finAlmuerzo) || finPermiso.compareTo(finAlmuerzo)==0) ){
				log.info("..............................CONDICION 1");
				double resta=actual-diferencia;
				this.getInstance().setNumeroHoras(resta);
				
			}else if(inicioPermiso.before(inicioAlmuerzo) && finPermiso.before(finAlmuerzo) && (finPermiso.after(inicioAlmuerzo))){
				log.info("..............................CONDICION 2");
				
				long dif=Math.abs(finPermiso.getTime()-inicioAlmuerzo.getTime());
				double difMin=(double) (dif/ (60 * 1000));
				
				if(difMin>=minAlmuerzos){
					double resta=actual-diferencia;
					this.getInstance().setNumeroHoras(resta);
				}else{
					diferencia=difMin/60;
					double resta=actual-diferencia;
					this.getInstance().setNumeroHoras(resta);
				}
				
			}else if(inicioPermiso.after(inicioAlmuerzo) && finPermiso.after(finAlmuerzo) && (inicioPermiso.before(finAlmuerzo))){
				log.info("..............................CONDICION 3");
				
				long dif=Math.abs(inicioPermiso.getTime()-finAlmuerzo.getTime());
				Double difMin=(double) (dif/ (60 * 1000));
				
				if(difMin>=minAlmuerzos){
					double resta=actual-diferencia;
					this.getInstance().setNumeroHoras(resta);
				}else{
					diferencia=difMin/60;
					double resta=actual-diferencia;
					this.getInstance().setNumeroHoras(resta);
				}
				
			}			
		}
	}
}
