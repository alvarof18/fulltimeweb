package com.casapazmino.fulltime.comun;

public class Constantes 
{
	/**
	 * CONSULTAS
	 */
	public final static int MAX_RESULTS = 10;
	public final static String SEPARADOR_LISTAS = ",";
	
	/**
	 * ESTADOS
	 */
	public final static long ESTADO = 3;
	public final static long SEXO = 1;
	public final static long ESTADO_CIVIL = 4;
	public final static long ESTADO_ACTIVO = 5;
	public final static long ESTADO_INACTIVO = 6;
	public final static long DECISION = 2;
	
	public final static long DECISION_NO = 4;
	public final static long DECISION_SI = 3;
	
	public final static long TIPO_PERMISO = 5;	
	public final static long NIVEL_ACADEMICO= 7;
	public final static long TIPO_CONTACTO = 11;
	public final static long AUTENTICACION = 13;
	public final static long PARAMETROS_ACTIVE_DIRECTORY = 14;
	public final static long PERIODO_VACACIONES = 15;
	
	public final static String PERMISO_PROCESADO = "PP";
	
	public final static int ACCESO_INDIVIDUAL = 0;
	public final static int ACCESO_SUBORDINADOS = 1;
	public final static int ACCESO_TODOS = 2;
	public final static long BASE_DATOS = 17;
	public final static long DIAS_CONSULTA_PERMISO = 18;
	
	public final static long RUTA_FUENTES = 19;
	public final static long RUTA_REPORTES = 20;
	public final static long RUTA_LOGO = 21;
	public final static long RUTA_PLANTILLA_NORMAL = 22;

	public final static long LISTA_CORREOS = 23;
	
	public final static long RUTA_CABECERA = 24;
	public final static long RUTA_PIE = 25;
	
	public final static long ENVIAR_CORREO_VACACION = 26;
	public final static long ENVIAR_NOTIFICACION_SISTEMA = 27;
	
	//Argumentos de envio de correo electronico
	
	public final static long CORREO_PRINCIPAL = 28;
	public final static long PASSWORD_CORREO_PRINCIPAL = 29;
	public final static long SERVIDOR_CORREO = 30;
	public final static long TTLS_CORREO = 31;
	public final static long AUTENTICACION_CORREO = 32;
	public final static long PUERTO_CORREO = 33;
	
	
	//Argumentos de rutas de archivos
	
	public final static long RUTA_ARCHIVOS_PERMISO = 34;
	public final static long RUTA_FOTO_EMPLEADOS = 35;
	
	//
	
	//numero de notificaciones mail faltas
	public final static long NUMERO_NOTIFICACIONES_MAIL_FALTAS = 36;
	
	//numero de notificaciones mail faltas
	public final static long ACTIVAR_NOTIFICACION_MAIL_FALTAS = 37;
	
	//numero de notificaciones mail atrasos
	public final static long ACTIVAR_NOTIFICACION_MAIL_ATRASOS= 38;
	//
		
	//hora de notificaciones mail faltas
	public final static long HORAS_NOTIFICACION_FALTAS= 39;
	//
				
	//minuto de notificaciones mail faltas
	public final static long MINUTOS_NOTIFICACION_FALTAS= 40;
	//
				
    //minuto de notificaciones mail faltas
	public final static long MINUTOS_RANGO_ATRASOS_SALIDAS_ANTES= 41;
	
    //Generacion Periodo Vacaciones Automatica  
	public final static long ACTIVAR_GENERACION_PERIODO_VACACIONES_AUTOMATICA= 42;
	
	//Horas Periodo Vacacion
	public final static long HORAS_PERIODO_VACACION= 43;
	
	//Minutos Periodo Vacacion 
	public final static long MINUTOS_PERIODO_VACACION= 44;
	
	//Ruta Archivo Certificados Correos
	public final static long ARCHIVO_CERTIFICADOS_CORREOS= 45;
	//
	
	//Activar Recuperacion de contraseña
	public final static long RECUPERACION_CONTRASEÑA= 46;
	//

	//Nivele Aprobacion Solicitud Vacaciones
	public final static long NIVEL_APROBACION_SOLICITUD_VACACION= 47;
	//

	//Patrametro de Conexion Driver
	public final static long PARAMETRO_CONEXION= 48;
	
	//Patrametro de Conexion Driver
	public final static long PARAMETRO_VALOR_TECLAS_FUNCION= 49;
	
	// Activar Generacion Automatica de Codigo Empleado
	public final static long ACTIVAR_GENERACION_AUTOMATICA_CODIGO_EMPLEADO = 50;	
	
	// Valor Codigo Empleado
	public final static long CODIGO_AUTOMATICO_EMPLEADO = 51;
	
	public final static long PARAMETRO_TIPO_REPORTE= 54;
	
	
	// NOTIFICACIONES Y CORREOS ELECTRONICOS AUTOMATICOS
	
	public final static long ACTIVAR_CORREO_BIENVENIDA = 60;
	
	public final static long MENSAJE_CORREO_BIENVENIDA = 61;
	
	public final static long ACTIVAR_MENSAJE_ANIVERSARIO = 62;
	
	public final static long MENSAJE_ANIVERSARIO = 63;
	
	public final static long ACTIVAR_MENSAJE_CUMPLEANIOS = 64;	
	
	public final static long MENSAJE_CUMPLEANIOS = 65;
	
	public final static long ACTIVAR_REPORTES_INASISTENCIA_DIARIO = 66;
	
	public final static long LISTADO_CORREO_REPORTES_DIARIO = 67;
	
	public final static long HORA_ENVIAR_REPORTE_FALTAS_DIARIO = 68;
	
	public final static long MINUTO_ENVIAR_REPORTE_FALTAS_DIARIO = 69;
	
	public final static long ACTIVAR_REPORTES_INASISTENCIA_SEMANAL = 70;
	
	public final static long LISTADO_CORREO_REPORTES_SEMANAL = 71;
	
	public final static long HORA_ENVIAR_REPORTE_FALTAS_SEMANAL = 72;
	
	public final static long MINUTO_ENVIAR_REPORTE_FALTAS_SEMANAL = 73;
	
	public final static long DIA_ENVIAR_REPORTE_FALTAS_SEMANAL = 74;
	
	public final static long AVISO_HORAS_EXTRAS = 79;
	
	public final static long HORA_NOTIFICACION_HORAS_EXTRAS = 80;
		
	// FIN NOTIFICACIONES Y CORREOS ELECTRONICOS AUTOMATICOS
	
	// USUARIO Y CLAVE PARA AUDITORIA DE PROCESOS AUTOMATICOS
	public final static long USUARIO = 97;
	
	public final static long CLAVE = 98;
	
	// FIN USUARIO Y CLAVE PARA AUDITORIA DE PROCESOS AUTOMATICOS	
	
	public final static long TECLAS_FUNCION= 16;
	
	// BLOQUEO DE CREACION DE PERMISOS PARA FECHAS ANTERIORES
	
	public final static long BLOQUE_PERMISOS = 81;

		
	/**
	 * ESTADOS ASISTENCIA
	 */	
	
	public final static String ASISTENCIA_FALTA = "FT";
	public final static String ASISTENCIA_REGISTRADO = "R";
	public final static String ASISTENCIA_VACACIONES = "V";
	public final static String ASISTENCIA_PERMISO = "P";
	public final static String ASISTENCIA_MANUAL = "RM";
	public final static String ASISTENCIA_LIBRE = "L";
	public final static String ASISTENCIA_FERIADO = "FD";
	
	public final static String ENTRADA = "E";
	public final static String SALIDA = "S";

	
	
	/***
	 * REPORTES
	 */
	public final static String DATA_SOURCE = "java:/fulltimeDatasource";

	
	/**
	 * RUTAS PARA REPORTES
	 */
	//public final static String LOGO = ".img/logo.png";
	public static String LOGO =  ""; // 				 "C:\\PROYECTO\\fulltime\\view\\reportes\\logo.png";
	public static String RUTA_LISTADOS_FUENTES = ""; //  "C:\\PROYECTO\\fulltime\\view\\reportes\\";
	public static String RUTA_LISTADOS_SERVIDOR = ""; // "\\reportes\\";
	public static String RUTA_PLANTILLA = ""; //  "C:\\PROYECTO\\fulltime\\view\\reportes\\estiloFulltimeNormal.jrtx";

//	VARIABLES UTILIZADAS PARA SUBIR Y LEER ARCHIVOS AL Y DEL SERVIDOR
	public final static String RUTA_ARCHIVOS_TIMBRES = "C:\\PROYECTO\\fulltime\\view\\archivosTimbres\\";
	public final static String RUTA_ARCHIVOS_IMAGENES = "C:\\PROYECTO\\fulltime\\view\\archivosImagenes\\";
	public final static String ARCHIVO_TIMBRES_NO_REGISTRADOS = "timbresNoRegistrados.txt";
	public final static String ARCHIVO_TIMBRES_REGISTRADOS = "timbresRegistrados.txt";
	
	/**
	 * EXTENCION ARCHIVOS
	 */
	public final static String EXT_JASPER = ".jasper";
	public final static String EXT_JRXML = ".jrxml";
	public final static String EXT_PDF = ".pdf";
	public final static String EXT_EXCEL = ".xls";
	public final static String EXT_CSV = ".csv";
	public final static String EXT_TXT = ".txt";
	public final static String EXT_HTML = ".html";
	public final static String EXT_XML = ".xml";
	
	public final static String PDF = "Pdf";
	public final static String EXCEL = "Xls";
	public final static String CSV = "Csv";
	public final static String TXT = "Txt";
	public final static String HTML = "Html";
	public final static String XML = "Xml";	
	public final static String MSG_ARCHIVO_NS = "Extension de archivo no soportada";

	public static String NombreEmpresa = "CASA LUIS PAZMINO";
	public final static long ID_EMPRESA  = 12;
	
	/**
	 * MENSAJES
	 */
	public final static String MENSAJE_TITULO="Seleccione un titulo para agregar.";
	public final static String MENSAJE_NIVEL_TITULO="Seleccione un nivel del titulo.";
	public final static String MENSAJE_ACTUALIZAR_TITULO="Seleccione el dato que desea actualizar.";
	public final static String MENSAJE_CARNET_CONADIS="Ingrese el número del carnet del CONADIS.";
	public final static String MENSAJE_DISCAPACIDAD="Ingrese la discapacidad.";
	public final static String MENSAJE_PORCENTAJE_DISCAPACIDAD="Ingrese el nivel de discapacidad.";
	public final static String MENSAJE_ACTUALIZAR_DISCAPACIDAD="Seleccione el dato que desea actualizar.";
	public final static String MENSAJE_TIPO_TITULO="Seleccione el tipo de contacto.";
	public final static String MENSAJE_CONTACTO="Ingrese el contacto.";
	public final static String MENSAJE_ACTUALIZAR_CONTACTO="Seleccione el dato que desea actualizar.";
	
	
	/**
	 * BASES DE DATOS
	 */
	public final static String MYSQL="MYSQL";
	public final static String SQLSERVER="SQLSERVER";
	public final static String ORACLE="ORACLE";
	public final static String POSTGRES="POSTGRES";
}

