package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("detalleTipoParametroList")
public class DetalleTipoParametroList extends EntityQuery<DetalleTipoParametro> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select detalleTipoParametro from DetalleTipoParametro detalleTipoParametro";

	private static final String[] RESTRICTIONS = {
			"detalleTipoParametro.dtpaId = #{detalleTipoParametroList.detalleTipoParametro.dtpaId}",
			"lower(detalleTipoParametro.descripcion) like concat(lower(#{detalleTipoParametroList.detalleTipoParametro.descripcion}),'%')",
			"detalleTipoParametro.tipoParametro.tipaId = #{detalleTipoParametroList.tipaId}",
			"detalleTipoParametro.tipoParametro.tipaId = #{tParam}", };

	private Long tipaId;
	private String extensionArchivo;

	private DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

	public DetalleTipoParametroList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public DetalleTipoParametro getDetalleTipoParametro() {
		return detalleTipoParametro;
	}

	public List<DetalleTipoParametro> getListaDetalleTipoParametro() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaEstadoCivil() {
		setTipaId(Constantes.ESTADO_CIVIL);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaSexo() {
		setTipaId(Constantes.SEXO);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaDecision() {
		setTipaId(Constantes.DECISION);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public DetalleTipoParametro getListaEmpresa() {
		setTipaId(Constantes.ID_EMPRESA);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public List<DetalleTipoParametro> getListaEstado() {
		setTipaId(Constantes.ESTADO);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaNivelAcademico() {
		setTipaId(Constantes.NIVEL_ACADEMICO);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaTipoContacto() {
		setTipaId(Constantes.TIPO_CONTACTO);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaContactos() {
		setTipaId(Constantes.TIPO_CONTACTO);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaAutenticacion() {
		setTipaId(Constantes.AUTENTICACION);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaParametrosActiveDirectory() {
		setTipaId(Constantes.PARAMETROS_ACTIVE_DIRECTORY);
		this.setMaxResults(null);
		this.setOrder("detalleTipoParametro.dtpaId");
		return this.getResultList();
	}

	public List<DetalleTipoParametro> getListaPeriodoVacaciones() {
		setTipaId(Constantes.PERIODO_VACACIONES);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public DetalleTipoParametro getBaseDatos() {
		setTipaId(Constantes.BASE_DATOS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getDiasConsultaPermiso() {
		setTipaId(Constantes.DIAS_CONSULTA_PERMISO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getRutaFuentes() {
		setTipaId(Constantes.RUTA_FUENTES);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getRutaReportes() {
		setTipaId(Constantes.RUTA_REPORTES);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getRutaLogo() {
		setTipaId(Constantes.RUTA_LOGO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getRutaPlantillaNormal() {
		setTipaId(Constantes.RUTA_PLANTILLA_NORMAL);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public List<DetalleTipoParametro> getListaCorreos() {
		setTipaId(Constantes.LISTA_CORREOS);
		this.setMaxResults(null);
		return this.getResultList();
	}

	public DetalleTipoParametro getRutaCabecera() {
		setTipaId(Constantes.RUTA_CABECERA);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getRutaPie() {
		setTipaId(Constantes.RUTA_PIE);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getEnviarCorreoVacacion() {
		setTipaId(Constantes.ENVIAR_CORREO_VACACION);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getNotificacionesSistema() {
		setTipaId(Constantes.ENVIAR_NOTIFICACION_SISTEMA);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Argumentos para el envio de correo electronico

	public DetalleTipoParametro getCorreoPrincipal() {
		setTipaId(Constantes.CORREO_PRINCIPAL);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getPassword_Correo() {
		setTipaId(Constantes.PASSWORD_CORREO_PRINCIPAL);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getServidorCorreo() {
		setTipaId(Constantes.SERVIDOR_CORREO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getTTLSCorreo() {
		setTipaId(Constantes.TTLS_CORREO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getAutenticacionCorreo() {
		setTipaId(Constantes.AUTENTICACION_CORREO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getPuertoCorreo() {
		setTipaId(Constantes.PUERTO_CORREO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	public DetalleTipoParametro getRutaArchivoPermiso() {
		setTipaId(Constantes.RUTA_ARCHIVOS_PERMISO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getRutaFotoEmpleado() {
		setTipaId(Constantes.RUTA_FOTO_EMPLEADOS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Numero de veces de envio de mail por faltas

	public DetalleTipoParametro getTopSendMailAll() {
		setTipaId(Constantes.NUMERO_NOTIFICACIONES_MAIL_FALTAS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Activar notificaciones por mail para faltas

	public DetalleTipoParametro getActivarNotificacionMailFaltas() {
		setTipaId(Constantes.ACTIVAR_NOTIFICACION_MAIL_FALTAS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Activar notificaciones por mail para atrasos

	public DetalleTipoParametro getActivarNotificacionMailAtrasos() {
		setTipaId(Constantes.ACTIVAR_NOTIFICACION_MAIL_ATRASOS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Hora notificaciones por mail para faltas

	public DetalleTipoParametro getHoraFaltas() {
		setTipaId(Constantes.HORAS_NOTIFICACION_FALTAS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Minuto notificaciones por mail para faltas

	public DetalleTipoParametro getMinutosFaltas() {
		setTipaId(Constantes.MINUTOS_NOTIFICACION_FALTAS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Minuto rango notificaciones por mail para atrasos y salidas antes

	public DetalleTipoParametro getMinutosRangoAtrasosSalidasAntes() {
		setTipaId(Constantes.MINUTOS_RANGO_ATRASOS_SALIDAS_ANTES);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Activar generacion de periodo de vacaciones automatica

	public DetalleTipoParametro getActivarPeriodoVacacionesAutomatica() {
		setTipaId(Constantes.ACTIVAR_GENERACION_PERIODO_VACACIONES_AUTOMATICA);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Hora periodo vacacion automatica

	public DetalleTipoParametro getHoraPeriodoVacacion() {
		setTipaId(Constantes.HORAS_PERIODO_VACACION);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Minuto periodo vacacion automatica

	public DetalleTipoParametro getMinutoPeriodoVacacion() {
		setTipaId(Constantes.MINUTOS_PERIODO_VACACION);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Minuto periodo vacacion automatica

	public DetalleTipoParametro getRutaCertifiacosJava() {
		setTipaId(Constantes.ARCHIVO_CERTIFICADOS_CORREOS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Activar Recuperación de Contraseña

	public DetalleTipoParametro getRecuperarContraseña() {
		setTipaId(Constantes.RECUPERACION_CONTRASEÑA);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Niveles Aprobacion Solicitudes de Vacaciones

	public DetalleTipoParametro getNivelAprobacionSolicitudesVacaciones() {
		setTipaId(Constantes.NIVEL_APROBACION_SOLICITUD_VACACION);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Parametro Conexión Driver
	public DetalleTipoParametro getParametroConexionDriver() {
		setTipaId(Constantes.PARAMETRO_CONEXION);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Número de Teclas de función
	public DetalleTipoParametro getValorTeclasFuncion() {
		setTipaId(Constantes.PARAMETRO_VALOR_TECLAS_FUNCION);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	//

	// Activar generacion Automatica codigo Empleado (Alvaro)
	public DetalleTipoParametro getActivarGeneracionAutomaticaCodigo() {
		setTipaId(Constantes.ACTIVAR_GENERACION_AUTOMATICA_CODIGO_EMPLEADO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getCodigoAutomaticoEmpleado() {
		setTipaId(Constantes.CODIGO_AUTOMATICO_EMPLEADO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Tipo de reporte
	public DetalleTipoParametro getTipoReporte() {
		setTipaId(Constantes.PARAMETRO_TIPO_REPORTE);
		this.setMaxResults(null);
		return this.getSingleResult();
	}
	
	public DetalleTipoParametro getTeclasFuncion() {
		setTipaId(Constantes.TECLAS_FUNCION);
		this.setMaxResults(null);
		return this.getSingleResult();
	}		
	
	// Bloque en la creacion de permisos
	
	public DetalleTipoParametro getBloquePermiso() {
		setTipaId(Constantes.BLOQUE_PERMISOS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}	
	

	// =================== NOTIFICACIONES Y CORREOS ELECTRONICOS AUTOMATICOS

	// Activar envio de Correo de Bienvenida (Alvaro)
	public DetalleTipoParametro getCorreoBienvenida() {
		setTipaId(Constantes.ACTIVAR_CORREO_BIENVENIDA);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Mensaje de Correo de Bienvenido (Alvaro)
	public DetalleTipoParametro getMensajeBienvenida() {
		setTipaId(Constantes.MENSAJE_CORREO_BIENVENIDA);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Mensaje de Correo de Cumpleaños (Alvaro)

	public DetalleTipoParametro getMensajeCumpleaños() {
		setTipaId(Constantes.MENSAJE_CUMPLEANIOS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Mensaje de Correo de Aniversario (Alvaro)

	public DetalleTipoParametro getMensajeAniversario() {
		setTipaId(Constantes.MENSAJE_ANIVERSARIO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Activar envio de Correo de Cumpleaños (Alvaro)
	public DetalleTipoParametro getActivarCorreoCumpleaños() {
		setTipaId(Constantes.ACTIVAR_MENSAJE_CUMPLEANIOS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Activar envio de Correo de Cumpleaños (Alvaro)
	public DetalleTipoParametro getActivarCorreoAniversario() {
		setTipaId(Constantes.ACTIVAR_MENSAJE_ANIVERSARIO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Activar envio de reportes diario de inasistencia (Alvaro)
	public DetalleTipoParametro getActivarReportesInasistencia() {
		setTipaId(Constantes.ACTIVAR_REPORTES_INASISTENCIA_DIARIO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	// Listado de correo para reportes diario y semanal de inasistencia (Alvaro)
	public DetalleTipoParametro getListadoCorreoInasistencia() {
		setTipaId(Constantes.LISTADO_CORREO_REPORTES_DIARIO);
		this.setMaxResults(null);
		return this.getSingleResult();
	}
	
	// Activar envio de reportes semanal de inasistencia (Alvaro)
	public DetalleTipoParametro getActivarReportesInasistenciaSemanal() {
		setTipaId(Constantes.ACTIVAR_REPORTES_INASISTENCIA_SEMANAL);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	
	// Listado de correo para reportes diario y semanal de inasistencia (Alvaro)
	public DetalleTipoParametro getDiaReporteFaltasSemanal() {
		setTipaId(Constantes.DIA_ENVIAR_REPORTE_FALTAS_SEMANAL);
		this.setMaxResults(null);
		return this.getSingleResult();
	}

	public DetalleTipoParametro getActivarAvisoHoraExtra() {
		setTipaId(Constantes.AVISO_HORAS_EXTRAS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}
	
	public DetalleTipoParametro getNotificacionHorasExtras() {
		setTipaId(Constantes.HORA_NOTIFICACION_HORAS_EXTRAS);
		this.setMaxResults(null);
		return this.getSingleResult();
	}
	
	// =================== FIN NOTIFICACIONES Y CORREOS ELECTRONICOS AUTOMATICOS
	
	// ///// GET === SET \\\\\\\

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public Long getTipaId() {
		return tipaId;
	}

	public void setTipaId(Long tipaId) {
		this.tipaId = tipaId;
	}
}
