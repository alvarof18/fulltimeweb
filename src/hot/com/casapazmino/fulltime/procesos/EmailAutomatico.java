package com.casapazmino.fulltime.procesos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.mail.Session;
import javax.persistence.EntityManager;

import oracle.jdbc.internal.OracleTypes;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.log.Log;

import org.hibernate.Criteria;

import com.casapazmino.fulltime.action.DetalleGrupoContratadoList;
import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.action.EnviarMail;
import com.casapazmino.fulltime.action.PermisoHome;
import com.casapazmino.fulltime.action.PermisoList;
import com.casapazmino.fulltime.comun.Conexion;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.DetalleGrupoContratado;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Permiso;

@Name("emailAutomatico")
public class EmailAutomatico {
	
	@In
	EntityManager entityManager;
	
	@Logger
	Log log;
	
	private static Date fechaActual = new Date();
	
	private List<Long> idEmpleados = new ArrayList<Long>();
	private Map<Empleado, Integer> horasExtrasByEmpleado = new HashMap<Empleado,Integer>();
	private String dataBase;
	private Conexion conexion;	
	private Connection connection;
	
	public void notificacionJustificacionPermisos(){
		
	   PermisoList PermisosList = (PermisoList) Component.getInstance("permisoList", true);
	   PermisoHome PermisoHome  = (PermisoHome) Component.getInstance("permisoHome", true);
	   
	   List<Permiso> listaPermisos = new ArrayList<Permiso>(); 
	   
	   List<Permiso> justificables = new ArrayList<Permiso>();
	   List<Permiso> Nojustificables = new ArrayList<Permiso>();
		
	   // Filtros
	    PermisosList.setMaxResults(null);
	    PermisosList.setEstado("0");
	    PermisosList.getPermiso().setFechaDesde(null);
		listaPermisos = PermisosList.getResultList();
		
		log.info("!!!!!!!!!!!!!!!!!!!! Cantidad de PErmisos " + listaPermisos.size());
		
		for(Permiso permiso : listaPermisos){
			
			PermisoHome.setInstance(permiso);
			
			if(PermisoHome.justificacion()){ // True: puede Justificar el permiso
				justificables.add(permiso);
			}else {
				
				// verifico si hay algun archivo adjunto, Si existe alguno ya esta justificado el permiso
				
				if(PermisoHome.listaArchivo().size() == 0){
					Nojustificables.add(permiso);
				}
			}
			
			//PermisoHome.clearInstance();
		}
		
		log.info("--------------JUSTIFICABLES-----------------");
		for(Permiso pe:justificables){
			log.info("Codigo " + pe.getPermId() + " Fecha Desde: " + pe.getFechaDesde() + " Fecha Hasta: ");
			
		}
		log.info("--------------FIN-----------------");
		
		log.info("-------------- NO JUSTIFICABLES-----------------");
		for(Permiso pe:Nojustificables){
			log.info("Codigo " + pe.getPermId() + " Fecha Desde: " + pe.getFechaDesde() + " Fecha Hasta: ");
			
		}
		log.info("--------------FIN-----------------");
		
	}
	
	public void notificacionAniversario() {
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
	// flag de activacion de notificacion
		detalleTipoParametros = detalleTipoParametroList.getActivarCorreoAniversario();	
		String parametro=detalleTipoParametros.getDescripcion();
		List<Empleado> lista = new ArrayList<Empleado>();

			if(parametro.toLowerCase().equals("activar")){
					
		          lista = this.listaAniversario(new Date());
		            if(!lista.isEmpty()){
		            	for(Empleado empleado: lista){      		
		            		EnviarMensajeAniversario(empleado);
		            	}
		            }
			}
	  }	
		
	public void notificacionCumpleanos(){
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
	// flag de activacion de notificacion
		detalleTipoParametros = detalleTipoParametroList.getActivarCorreoCumpleaños();	
		String parametro=detalleTipoParametros.getDescripcion();
		List<Empleado> lista = new ArrayList<Empleado>();
		
		if(parametro.toLowerCase().equals("activar")){
		
			lista = this.notificacionCumpleanos(new Date());
			
			if(!lista.isEmpty()){
			
				for(Empleado empleado: lista){
					EnviarMensajeCumpleaNo(empleado);	
				}
			}
	    }
	}
	
	public void NotificacionFaltasDiarias(){
		
		ResultSet rs = null;
		
		String CorreoCuerpo = "";
			
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
		// flag de activacion de notificacion
		detalleTipoParametros = detalleTipoParametroList.getActivarReportesInasistencia();	
		String parametro=detalleTipoParametros.getDescripcion();
		
		if(parametro.toLowerCase().equals("activar")){
		
		try {
			    Date fechaDesde = new Date();
			    fechaDesde = Fechas.SumarRestarDias(fechaDesde, -1);
				conexion = new Conexion(); 
				connection = conexion.getConnection();
				rs = this.ConsultaReportes(fechaDesde,fechaDesde);
				
				if(rs != null){					
				
				String cuerpoReporte = "Este correo es un reporte diario de inasistencia de empleados:<br><br>"
						+ "<center><h1>INFORMACION DEL REPORTE </h1></center><br><br>";
				
				while(rs.next()){
					CorreoCuerpo += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
					
				}
			
				cuerpoReporte += CorreoCuerpo;
				detalleTipoParametros = detalleTipoParametroList.getActivarReportesInasistencia();
				parametro=detalleTipoParametros.getDescripcion();
				 
				if(parametro.toLowerCase().equals("activar")){
					 detalleTipoParametros = detalleTipoParametroList.getListadoCorreoInasistencia();
					 parametro=detalleTipoParametros.getDescripcion();
					 
					 String[] listaCorreos = parametro.split(",");
					 for(int i = 0; i<listaCorreos.length ; i++){
						EnviarMailSimple(cuerpoReporte,"Reporte de Inasistencia del dia " + 
					                     Fechas.cambiarFormato(fechaDesde, "dd-MM-yyyy"),listaCorreos[i]);				 
					 }
					 
				 }
				
			}
			rs.close();
			connection.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
	}
}
	
	
	public void notificacionSemanalFaltas(){
		
		ResultSet rs = null;
		String[] CuerpoCorreo = new String[7]; 
		String cuerpoReporte;
		String fechaAsistencia  = ""; 
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
	// flag de activacion de notificacion
		detalleTipoParametros = detalleTipoParametroList.getActivarReportesInasistencia();	
		String parametro=detalleTipoParametros.getDescripcion();
		
		if(parametro.toLowerCase().equals("activar")){
			
			//Calculo de los rangos del reporte semanal
			 Calendar hoy = Calendar.getInstance();
			 Date fecha_desde = new Date();
			 Date fecha_hasta = new Date();
			 int dias = 7;
			 int dia_semana;
			 log.info("Dia de la semana : " + hoy.get(Calendar.DAY_OF_WEEK));
			
			dia_semana = hoy.get(Calendar.DAY_OF_WEEK);
			dia_semana--;
			
			dias += dia_semana;
			dias *= -1;
			
			fecha_desde= Fechas.SumarRestarDias(fecha_desde , dias);
			log.info("Fecha Desde: " + fecha_desde);
			
			fecha_hasta = Fechas.SumarRestarDias(fecha_desde , +6);
			
			log.info("Fecha Hasta: " + fecha_hasta);
			
			conexion = new Conexion(); 
			connection = conexion.getConnection();
			rs = this.ConsultaReportes(fecha_desde, fecha_hasta);
			
			if(!rs.equals(null)){
				
				cuerpoReporte = "Este correo es un reporte semanal de inasistencias<br><br>"
						+ "<center><h1>INFORMACION DEL REPORTE</h1></center><br>";
				
				//Cabeceras
			 	for(int i =0; i<CuerpoCorreo.length; i++){
			 	
			 		CuerpoCorreo[i] = "<center><h3>FECHA: " + 
			 							Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, +i),"dd-MM-yyyy") +
			 						  "  </h3></center><br>";		 	
			 	}
			 	
			 try{  
				while(rs.next()){
					fechaAsistencia = Fechas.cambiarFormato(rs.getDate(8),"dd-MM-yyyy");
					
					//Domingo
					if(Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, 0),"dd-MM-yyyy").equals(fechaAsistencia)){
					   
						if(CuerpoCorreo[0] == null){
							CuerpoCorreo[0] = " ";
						}
						
						CuerpoCorreo[0] += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
					}
					
					//Lunes
					if(Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, +1),"dd-MM-yyyy").equals(fechaAsistencia)){
						CuerpoCorreo[1] += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				
					}
					
					//Martes
					if(Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, +2),"dd-MM-yyyy").equals(fechaAsistencia)){
						CuerpoCorreo[2] += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				
					}
					
					//Miercoles
					if(Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, +3),"dd-MM-yyyy").equals(fechaAsistencia)){
						CuerpoCorreo[3] += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				
					}
					
					//Jueves
					if(Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, +4),"dd-MM-yyyy").equals(fechaAsistencia)){
						CuerpoCorreo[4] += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				
					}
					
					//Viernes
					if(Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, +5),"dd-MM-yyyy").equals(fechaAsistencia)){
						CuerpoCorreo[5] += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
				
					}
					
					//Sabados
					if(Fechas.cambiarFormato(Fechas.SumarRestarDias(fecha_desde, +6),"dd-MM-yyyy").equals(fechaAsistencia)){
						CuerpoCorreo[6] += EnviarMensajeFaltaSemanal(rs.getString(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
					}
					
				}
				rs.close();
				connection.close();
			 } catch (SQLException e1) {
					e1.printStackTrace();
				} 
				
				cuerpoReporte += CuerpoCorreo[0];
				cuerpoReporte += CuerpoCorreo[1];
				cuerpoReporte += CuerpoCorreo[2];
				cuerpoReporte += CuerpoCorreo[3];
				cuerpoReporte += CuerpoCorreo[4];
				cuerpoReporte += CuerpoCorreo[5];
				cuerpoReporte += CuerpoCorreo[6];
			
				detalleTipoParametros = detalleTipoParametroList.getListadoCorreoInasistencia();
				parametro=detalleTipoParametros.getDescripcion();
					 
				String[] listaCorreos = parametro.split(",");
				
				 for(int i = 0; i<listaCorreos.length ; i++){
						EnviarMailSimple(cuerpoReporte,"Reporte semanal de falta de empleados",listaCorreos[i]);				 
					 }

			}
		}
	}
			
	private String EnviarMensajeFaltaSemanal(String Apellido, String Nombre, String CodigoE, String Cargo, String Departamento, String Grupo, String Ciudad ) {
		
		String cuerpo = "<center><b>DATOS DEL EMPLEADO</b></center>"
						+
				"<b>Empleado:</b>&#32;"
				+ Apellido + " " + Nombre
				+ "<br>"
				+ "<b>Codigo Empleado:</b>&#32;"
				+ CodigoE
				+"<br>"
				+ "<b>Cargo:</b>&#32;"
				+ Cargo
				+ "<br>"
				+ "<b>Departamento:</b>&#32;"
				+ Departamento
				+ "<br>"
				+ "<b>Contrato:</b>&#32;"
				+ Grupo
				+ "<br>"
				+ "<b>Ciudad:</b>&#32;"
				+ Ciudad
				+ "<br><br>";	
						
	return cuerpo;		
	}
	

	
	private void EnviarMensajeAniversario(Empleado empleado) {
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		detalleTipoParametros = detalleTipoParametroList.getMensajeAniversario();
		
		String CuerpoMensaje = "Este correo es una notificacion de Felicitacion por Aniversario <br><br>" +
								detalleTipoParametros.getDescripcion();
				   
		String Asunto = "FELIZ ANIVERSARIO";
		EnviarMailSimple(CuerpoMensaje,Asunto, empleado.getCorreo());
			
	}
	
	private void EnviarMensajeCumpleaNo(Empleado empleado) {
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		detalleTipoParametros = detalleTipoParametroList.getMensajeCumpleaños();
		
		String CuerpoMensaje = "Este correo es una notificacion de Felicitacion por Cumpleaños <br><br>" +
								detalleTipoParametros.getDescripcion();
				   
		String Asunto = "FELIZ CUMPLEAÑOS";
		EnviarMailSimple(CuerpoMensaje,Asunto, empleado.getCorreo());	
		
	}
	
	public void atrasosEntrada(){ // realizar tambien el de retraso a la hora de almuerzo
		
		String motivo = "Atraso en Entrada";
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
	// flag de activacion de notificacion de retrasos y salidas antes
		detalleTipoParametros = detalleTipoParametroList.getActivarNotificacionMailAtrasos();	
		String parametro=detalleTipoParametros.getDescripcion();
		List<Asistencia> listaRetrasoEntrada = new ArrayList<Asistencia>();
		
		if(parametro.toLowerCase().equals("activar")){
			
			listaRetrasoEntrada = this.RetrasoEntrada(new Date(),new Date());
			if(!listaRetrasoEntrada.isEmpty()){
				
				for(Asistencia asistencia : listaRetrasoEntrada){
					
					//Validacion para que tome en cuenta el tiempo de tolerancia
				
					log.info("Tiempo sumado " + this.sumarRestarMinutosFecha(asistencia.getFechaHoraHorario(), +asistencia.getAsisMaxMinuto()));
					
					if (asistencia.getFechaHoraTimbre().after(this.sumarRestarMinutosFecha(asistencia.getFechaHoraHorario(), +asistencia.getAsisMaxMinuto()))){
						EnviarMensajeAtrasoEntrada(asistencia, motivo);
			
					}
				}
				
			}		
			
		}
		
	}
	
	private void EnviarMensajeAtrasoEntrada(Asistencia asistencia, String motivo) {
		
		String Correo = asistencia.getEmpleado().getCorreo();
		String CuerpoMensaje = "Este correo es una notificacion de atraso en la Entrada:<br><br>"
				+ "<center><h1>INFORMACION DE NOTIFICACION </h1></center><br><br>"
				+ Cuerpo(asistencia,motivo,true);
		String Asunto = "Notificacion de Atraso en Entrada ";
		EnviarMailSimple(CuerpoMensaje,Asunto,Correo);
		if(!asistencia.getEmpleado().getEmpleado().equals(null)){
			Correo = asistencia.getEmpleado().getEmpleado().getCorreo(); //Correo al Jefe
			EnviarMailSimple(CuerpoMensaje,Asunto,Correo);
		}
	}
	
	public void atrasosEntradaAlimentacion(){
		
		String motivo = "Atraso en Entrada de Alimentacion";
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
	// flag de activacion de notificacion de retrasos y salidas antes
		detalleTipoParametros = detalleTipoParametroList.getActivarNotificacionMailAtrasos();	
		String parametro=detalleTipoParametros.getDescripcion();
		List<Asistencia> listaRetrasoEntradaAlimentacion = new ArrayList<Asistencia>();
		
		if(parametro.toLowerCase().equals("activar")){
			
			listaRetrasoEntradaAlimentacion = this.RetrasoEntradaAlimentacion(fechaActual);
			if(!listaRetrasoEntradaAlimentacion.isEmpty()){
				
				for(Asistencia asistencia : listaRetrasoEntradaAlimentacion){
					EnviarMensajeAtrasoEntradaAlimientacion(asistencia, motivo);
				
				}
				
			}		
			
		}
		
		log.info("Retrasoooooooooooooooooooo Entrada" + listaRetrasoEntradaAlimentacion);
	
	}
	
	private void EnviarMensajeAtrasoEntradaAlimientacion(Asistencia asistencia, String motivo) {
		String Correo = asistencia.getEmpleado().getCorreo();
	
		String CuerpoMensaje = "Este correo es una notificacion de atraso en la Entrada Alimentacion:<br><br>"
				+ "<center><h1>INFORMACION DE NOTIFICACION </h1></center><br><br>"
				+ Cuerpo(asistencia,motivo,false);
		String Asunto = "Notificacion de Atraso en Entrada Alimentacion ";
		EnviarMailSimple(CuerpoMensaje,Asunto,Correo);
		Correo = asistencia.getEmpleado().getEmpleado().getCorreo(); //Correo al Jefe
		EnviarMailSimple(CuerpoMensaje,Asunto,Correo);
		
	}

	public void salidaAnticipadas(){
		
		String motivo = "Salida Anticipada";
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
	// flag de activacion de notificacion de retrasos y salidas antes
		detalleTipoParametros = detalleTipoParametroList.getActivarNotificacionMailAtrasos();	
		String parametro=detalleTipoParametros.getDescripcion();
		List<Asistencia> listaSalidaAnticipada = new ArrayList<Asistencia>();
		
		if(parametro.toLowerCase().equals("activar")){
		
			listaSalidaAnticipada = this.SalidasAnticipadas(new Date(),new Date());
			
			if(!listaSalidaAnticipada.isEmpty()){
			
				for(Asistencia asistencia: listaSalidaAnticipada){
					EnviarMensajeSalidaAnticipada(asistencia, motivo);
				}
				
			} 
	
		}
	}
	
	
	private void EnviarMensajeSalidaAnticipada(Asistencia Asistencia, String motivo) {
		String Correo = Asistencia.getEmpleado().getCorreo();
		String CuerpoMensaje = "Este correo es para notificar una salida anticipada:<br><br>"
				+ "<center><h1>INFORMACION DE NOTIFICACION </h1></center><br><br>"
				+ Cuerpo(Asistencia,motivo,false);
		String Asunto = "Notificacion de salida anticipada";
		EnviarMailSimple(CuerpoMensaje,Asunto,Correo);
		if(!Asistencia.getEmpleado().getEmpleado().equals(null)){
			Correo = Asistencia.getEmpleado().getEmpleado().getCorreo(); //Correo al Jefe
			EnviarMailSimple(CuerpoMensaje,Asunto,Correo);
		}
	
	}
	
	private void EnviarMensajeHoraExtra(Empleado empleado, int minutosExtras, int maximoHoras, String periodo){
		EnviarMail enviarMail = new EnviarMail();
		
		String motivo = "Proximidad al maximo de horas extras";
		String CuerpoMensaje = "Este correo es para notificar la proximidad al maximo de horas extras:<br><br>"
				+ "<center><h1>INFORMACION DE NOTIFICACION </h1></center><br><br>"
				+ CuerpoExtra(empleado,motivo, minutosExtras, maximoHoras, periodo);
		String Asunto = "Proximidad de Maximo de Horas Extras";
		//EnviarMailSimple(CuerpoMensaje,Asunto,empleado.getCorreo());
		enviarMail.enviar(CuerpoMensaje, empleado.getEmpleado().getCorreo(), Asunto, "J");
		enviarMail.enviar(CuerpoMensaje, empleado.getCorreo(), Asunto, "E");
		//EnviarMailSimple(CuerpoMensaje,Asunto,empleado.getEmpleado().getCorreo());
	}
	

    private void EnviarMailSimple(String cuerpoMensaje, String asunto, String Correo) {
		EnviarMail enviarMail = new EnviarMail();
		enviarMail.enviar(cuerpoMensaje, Correo,asunto, "E"); // Envia Correo al empleado
		
	}

	private String Cuerpo(Asistencia asistencia,String motivo, Boolean entrada) {
		
		String duracion;
		Empleado empleado = new Empleado();
		empleado = asistencia.getEmpleado();
		String empleados = empleado.getApellido()
				+ " " + empleado.getNombre();
		String cargo = empleado.getCargo().getDescripcion();
		String departamento = empleado.getDepartamento().getDescripcion();
		String contrato = empleado.getDetalleGrupoContratado().getDescripcion();
		String fecha = Fechas.cambiarFormato(asistencia.getFechaHorario(), "dd-MM-yyyy");
		String HoraSalida = Fechas.cambiarFormato(asistencia.getFechaHoraHorario(), "dd-MM-yyyy HH:mm:ss");
		String HoraTimbre = Fechas.cambiarFormato(asistencia.getFechaHoraTimbre(), "dd-MM-yyyy HH:mm:ss");
		String tolerancia = String.valueOf(asistencia.getAsisMaxMinuto());
		if(entrada){
			asistencia.setFechaHoraHorario(this.sumarRestarMinutosFecha(asistencia.getFechaHoraHorario(), +asistencia.getAsisMaxMinuto()));	
		}
		
		Long diferencia = Fechas.calcularTiempo(asistencia.getFechaHoraHorario(), asistencia.getFechaHoraTimbre())/1000/60;
		
		if (diferencia < 0){
			diferencia *= -1;
		}		
		
		duracion = String.valueOf(diferencia);
		
		log.info ("Diferencia de tiempoooooooooo " + (Fechas.calcularTiempo(asistencia.getFechaHoraHorario(), asistencia.getFechaHoraTimbre())/1000/60) * -1);		
		
		String cuerpo = "<center><h2><b>DATOS DEL EMPLEADO</b></h2></center><br><br>"
				+
		"<b>Empleado:</b>&#32;"
		+ empleados
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
		
		+ "<center><h2><b>INFORMACIóN DE LA NOTIFICACION</b></h2></center><br><br>"
		
		+"<b>Motivo:</b>&#32;"
		+ motivo
		+ "<br>"
		+ "<b>Fecha: </b>&#32;"
		+ fecha
		+ "<br>"
		+ "<b>Horario:</b>&#32;"
		+ HoraSalida
		+ "<br>"
		+ "<b>Hora Timbre:</b>&#32;"
		+ HoraTimbre
		+ "<br>"
		+ "<b>Cantidad de minutos:</b>&#32;"
		+duracion
		+ "<br>";
		if(entrada){
			cuerpo += "<b>Minutos de Tolerancia:</b>&#32;"
					+ tolerancia;
		 }	
		return cuerpo;
		}
	
	private String CuerpoExtra(Empleado empleado ,String motivo, int minutosExtras, int maximoHoras, String periodo) {
		
		String empleados = empleado.getApellido()
				+ " " + empleado.getNombre();
		String cargo = empleado.getCargo().getDescripcion();
		String departamento = empleado.getDepartamento().getDescripcion();
		String contrato = empleado.getDetalleGrupoContratado().getDescripcion();
		String horasExtrasC = this.convertirMinutosHoras(minutosExtras);
				
		String cuerpo = "<center><h2><b>DATOS DEL EMPLEADO</b></h2></center><br><br>"
				+
		"<b>Empleado:</b>&#32;"
		+ empleados
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
		
		+ "<center><h2><b>INFORMACIóN DE LA NOTIFICACION</b></h2></center><br><br>"
		
		+"<b>Motivo:</b>&#32;"
		+ motivo
		+ "<br>"
		+ "<b>Maximo de Horas Extras:</b>&#32;"
		+ maximoHoras
		+ "<br>"
		+ "<b>Tiempo extra Trabajadas</b>&#32;"
		+ horasExtrasC
		+ "<br>"
		+ "<b>Periodo: </b> &#32;"
		+ periodo;
		;
			
		return cuerpo;
		}
	
	
	public void notificacionHorasExtras(String dataBase){
		
		int diaSemana;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		int limiteInferior = 0;
		int topeHoras = 0;
		String llamada = "";
		Integer horas;
		Calendar cal= Calendar.getInstance();
		Map<Long, Integer> totalHoras = new HashMap<Long,Integer>();
		Date fechaTmp = new Date();
		java.sql.Date fechaDesde = new java.sql.Date(0);
		java.sql.Date fechaHasta = new java.sql.Date(0);
		Boolean resultado;
		
		List<DetalleGrupoContratado> grupoContratado = new ArrayList<DetalleGrupoContratado>();
		DetalleGrupoContratadoList detalleGrupoContratadoList = (DetalleGrupoContratadoList) Component.getInstance("detalleGrupoContratadoList", true);
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		detalleTipoParametros = detalleTipoParametroList.getActivarAvisoHoraExtra();
		String parametro = detalleTipoParametros.getDescripcion();
		
		log.info("base de datos " + dataBase.toString());
		
		if(parametro.toLowerCase().equals("activar")){
		
			grupoContratado = detalleGrupoContratadoList.getResultList();
			Conexion conexion=new Conexion();		
			Connection connection = conexion.getConnection();
			
			if(dataBase.equals("ORACLE")){
				llamada = "{call spHorasExtras(?,?,?,?)}";			
			}else { 
				llamada = "{call spHorasExtras(?,?,?)}";
			}
		//CallableStatement
		
		 for (DetalleGrupoContratado grupo : grupoContratado){
			
			 if (grupo.getMaximo_horas_semanales() > 0){ // busqueda por semanas 
					
					diaSemana = cal.get(Calendar.DAY_OF_WEEK);
					
					if(diaSemana!= 0){
						 
						 diaSemana = ((diaSemana - 1) * - 1);
					 }
					 fechaTmp = Fechas.SumarRestarDias(new Date(), diaSemana);
					 
					 fechaDesde=new java.sql.Date(fechaTmp.getTime());
					 fechaHasta=new java.sql.Date(new Date().getTime());
					 topeHoras = 0;
					 topeHoras = grupo.getMaximo_horas_semanales() * 60;
				}else { // busqueda por mes
					
					diaSemana = cal.get(Calendar.DAY_OF_MONTH);
					diaSemana = ((diaSemana - 1) * - 1);
					topeHoras = 0;
					topeHoras = grupo.getMaximo_hora_extra() * 60;
					fechaDesde = new java.sql.Date (Fechas.SumarRestarDias(new Date(), diaSemana).getTime());
					fechaHasta=new java.sql.Date(new Date().getTime());				
					
				}
			 
			   // Borrar al terminar las pruebas
			   //	fechaDesde = new java.sql.Date (Fechas.SumarRestarDias(new Date(),-26).getTime());
			//	fechaHasta = new  java.sql.Date (Fechas.SumarRestarDias(new Date(),+2).getTime());
				limiteInferior = 0;
				limiteInferior = grupo.getLimite_aviso() * 60;			 
				 
				try {
					cstmt = connection.prepareCall(llamada);
				    
					cstmt.setDate(1,fechaDesde);
					cstmt.setDate(2,fechaHasta);
					cstmt.setLong(3,grupo.getDgcoId());
					
					if(dataBase.equals("ORACLE")){
					   cstmt.registerOutParameter(4, OracleTypes.CURSOR);
					}
					resultado = cstmt.execute();
					rs = cstmt.getResultSet();
					if(dataBase.equals("ORACLE")){
			         rs = (ResultSet) cstmt.getObject(4);
					  resultado = true;
					}
					/*
					log.info("resultado " + resultado);
					log.info("fechadesde " + fechaDesde);
					log.info("fechahasta " + fechaHasta);
					log.info("grupo " + grupo.getDgcoId());
					*/
					//log.info("NEXT " + rs.next());
					
					
					if(resultado){
					
						while(rs.next()){
							horas = 0;
							log.info("Empleado " + rs.getInt("idempleado"));
							log.info("Suplementaria " + rs.getInt("horassuplementarias"));
							log.info("horasextraordinariaslv " + rs.getInt("horasextraordinariaslv"));
							log.info("grupo " + grupo.getDescripcion());
							
							idEmpleados.add((long) rs.getInt("idempleado"));
							if(totalHoras.containsKey((long)rs.getInt("idempleado"))){ // si es true el codigo existe
								horas = totalHoras.get((long)rs.getInt("idempleado"));
								
								horas += rs.getInt("horassuplementarias") + rs.getInt("horasextraordinariaslv")  +
										 rs.getInt("horasextraordinariassd");
										
							}else{ // el codigo no existe
								
								horas += rs.getInt("horassuplementarias") + rs.getInt("horasextraordinariaslv")  +
										 rs.getInt("horasextraordinariassd");
							}
							totalHoras.put((long)rs.getInt("idempleado"), horas);
														
						}
						
						if (!totalHoras.isEmpty()){
						
							List<Empleado> listaEmpleados = new ArrayList<Empleado>();
							listaEmpleados = this.listaEmpeladosById(idEmpleados);
							
							for(Empleado e : listaEmpleados){
								if(totalHoras.containsKey(e.getEmplId())){
									horasExtrasByEmpleado.put(e, totalHoras.get(e.getEmplId()));					
								}					
							}
							for(Iterator<Empleado> it = horasExtrasByEmpleado.keySet().iterator(); it.hasNext();){
								Empleado clave = it.next();
								
								if (horasExtrasByEmpleado.get(clave) > limiteInferior && horasExtrasByEmpleado.get(clave) < topeHoras){ // limite de horas semanales segun el codigo del trabajo hacer este valor parametrizable
									String periodo;
									periodo = Fechas.cambiarFormato(fechaDesde, "dd/MM/yyyy") + " - " + Fechas.cambiarFormato(fechaHasta, "dd/MM/yyyy");
									//log.info("El empleado " + clave.getApellido() + " " + clave.getNombre() + "esta proximo a llegar al limite de horas extras" + " Periodo " + periodo );
									EnviarMensajeHoraExtra(clave,horasExtrasByEmpleado.get(clave), topeHoras/60, periodo);
								}
								/* El exceso de horas extras lo dejare de ultimo
									if(horasExtrasByEmpleado.get(clave) > 720){
									 EnviarMensajeHoraExtra(clave,horasExtrasByEmpleado.get(clave));
								} */
								
								log.info(clave.getApellido() + " " + clave.getNombre()  + "   "+ " Tiempo "+ horasExtrasByEmpleado.get(clave) + "Hora " + this.convertirMinutosHoras(horasExtrasByEmpleado.get(clave)));
							}
					
							listaEmpleados.clear();
						}
					}
					resultado = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				idEmpleados.clear();
				horasExtrasByEmpleado.clear();
				totalHoras.clear();		
		 }
		 
		}
	}
	
	public String convertirMinutosHoras(int minutos){
		
		String newTime = "";
		long hora2 = TimeUnit.MINUTES.toHours(minutos);
		long minutos2 = minutos - TimeUnit.HOURS.toMinutes(hora2);
		
		//log.info("Hora " + hora2 + " Minutos: " + minutos2);
		
		if (hora2<10 && minutos2 < 10){
			newTime = "0" +hora2 +":"+"0"+minutos2;
		}else if(hora2<10 && minutos2 > 10){
			newTime = "0" +hora2 +":"+minutos2;

		}else if(hora2>10 && minutos2<10){
			newTime = hora2 +":"+"0"+minutos2;
		}else if(hora2> 10 && minutos>10){
			newTime = hora2 +":"+minutos2;	
		}

		return newTime;		
	}
	

	@SuppressWarnings("unchecked")
	public List<Asistencia> SalidasAnticipadas(Date fechaDesde, Date fechaHasta) {
		
		return (List<Asistencia>) entityManager
				.createQuery(
						"select asistencia from Asistencia asistencia where asistencia.fechaHorario between :fechaDesde and :fechaHasta "
																						 + "and asistencia.entradaSalida = 'S' "
																						 + "and asistencia.estado NOT IN('FD', 'L', 'V', 'FT')"
																						 + "and lower(asistencia.empleado.detalleTipoParametroByEstado.descripcion) = 'activo' "
																						 + "and asistencia.fechaHoraTimbre < asistencia.fechaHoraHorario"
																						 + " order by asistencia.fechaHorario")
				.setParameter("fechaDesde",Fechas.cambiarFormatoFecha(fechaDesde,"yyyy-MM-dd"))
				.setParameter("fechaHasta",Fechas.cambiarFormatoFecha(fechaHasta,"yyyy-MM-dd"))
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Asistencia> RetrasoEntrada(Date fechaDesde, Date fechaHasta) {
		
		return (List<Asistencia>) entityManager
				.createQuery(
						"select asistencia from Asistencia asistencia where asistencia.fechaHorario between :fechaDesde and :fechaHasta "
																						 + "and asistencia.entradaSalida = 'E' "
																						 + "and asistencia.estado NOT IN('FD', 'L', 'V', 'FT') "
																						 + "and lower(asistencia.empleado.detalleTipoParametroByEstado.descripcion) = 'activo' "
																						 + "and asistencia.fechaHoraTimbre > asistencia.fechaHoraHorario"
																						 + " order by asistencia.fechaHorario")
				.setParameter("fechaDesde",Fechas.cambiarFormatoFecha(fechaDesde,"yyyy-MM-dd"))
				.setParameter("fechaHasta",Fechas.cambiarFormatoFecha(fechaHasta,"yyyy-MM-dd"))
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Asistencia> RetrasoEntradaAlimentacion(Date fechaHoy) {
		
		return (List<Asistencia>) entityManager
				.createQuery(
						"select asistencia from Asistencia asistencia where asistencia.fechaHorario = :fechaActual "
																						 + "and asistencia.entradaSalida = 'EA' "
																						 + "and asistencia.estado NOT IN('FD', 'L', 'V') "
																						 + "and asistencia.fechaHoraTimbre > asistencia.fechaHoraHorario")
				.setParameter("fechaActual",Fechas.cambiarFormatoFecha(fechaHoy,"yyyy-MM-dd"))
				.getResultList();
	}
	
	//notificacion cumpleaños
	
	@SuppressWarnings("unchecked")
	private List<Empleado> notificacionCumpleanos(Date fechaHoy){
		
		int dia = Integer.parseInt(Fechas.cambiarFormato(fechaHoy, "dd"));
		 int mes = Integer.parseInt(Fechas.cambiarFormato(fechaHoy, "MM"));
		
		return (List<Empleado>) entityManager
				.createQuery(
						"select empleado from Empleado empleado where MONTH(empleado.fechaNacimiento) = :mesActual and DAY(empleado.fechaNacimiento) = :diaActual "
						+ "and lower(empleado.detalleTipoParametroByEstado.descripcion) = 'activo'")									
				.setParameter("mesActual",mes)
				.setParameter("diaActual",dia)
				.getResultList();
	}
	
	//Lista de Aniversario
	
	@SuppressWarnings("unchecked")
	private List<Empleado> listaAniversario(Date fechaHoy){
			
			log.info(Fechas.cambiarFormato(fechaHoy, "MM"));
			log.info(Fechas.cambiarFormato(fechaHoy, "dd"));
			 int dia = Integer.parseInt(Fechas.cambiarFormato(fechaHoy, "dd"));
			 int mes = Integer.parseInt(Fechas.cambiarFormato(fechaHoy, "MM"));
			
			
			return (List<Empleado>) entityManager
					.createQuery(
							"select empleado from Empleado empleado, HistLabo historia "
							+ " where empleado.emplId = historia.empleado.emplId and "
							+ "lower(empleado.detalleTipoParametroByEstado.descripcion) = 'activo' and "
							+ "MONTH(historia.fechaIngreso) = :mesActual and DAY(historia.fechaIngreso) = :diaActual and historia.estado = :estado")										
					.setParameter("mesActual",mes)
					.setParameter("diaActual",dia)
					.setParameter("estado", true)
					.getResultList();
		}
		
	private ResultSet ConsultaReportes(Date Desde, Date Hasta){
		
		PreparedStatement stmt = null;
		Boolean resultado;
		java.sql.Date fechaDesde = new java.sql.Date(0);
		java.sql.Date fechaHasta = new java.sql.Date(0);
		
		String sentenciaNativa = "select apellido, empleado.nombre, empleado.codigo_empleado, cargo.descripcion, departamento.descripcion, "
								+ "detalle_grupo_contratado.descripcion, ciudad.descripcion, asis_entra.fecha_Horario " +
				"from " +
					  "(select * from asistencia where entrada_salida = 'E') asis_entra " +
					     "inner join (select * from asistencia where entrada_salida = 'S') asis_sale on asis_sale.empl_id = asis_entra.empl_id and asis_entra.fecha_horario = asis_sale.fecha_horario " +
					     "left join (select * from asistencia where entrada_salida = 'SA') sale_almu on sale_almu.empl_id = asis_entra.empl_id and asis_entra.fecha_horario = sale_almu.fecha_horario " +
					     "left join (select * from asistencia where entrada_salida = 'EA') entra_almu on entra_almu.empl_id = asis_entra.empl_id and asis_entra.fecha_horario = entra_almu.fecha_horario " +
					     "inner join empleado on empleado.empl_id = asis_entra.empl_id "+
					     "inner join cargo cargo on empleado.carg_id = cargo.carg_id " +
					     "inner join departamento departamento on empleado.depa_id = departamento.depa_id " +
					     "inner join detalle_grupo_contratado detalle_grupo_contratado on empleado.dgco_id = detalle_grupo_contratado.dgco_id " +
					     "inner join ciudad ciudad on empleado.ciud_id = ciudad.ciud_id " +
					     "inner join detalle_horario detalle_horario on detalle_horario.deho_id=asis_entra.deho_id " +
					     "inner join horario horario on horario.hora_id=detalle_horario.hora_id " +
				"where " + 
						"asis_entra.fecha_horario between ? and ? " +
						" and asis_entra.estado not in ('FD','L','V','R','P') "+
						" and asis_sale.estado not in ('FD','L','V','R','P') " +
						" and empleado.estado = '5'" +
						" order by ciudad.descripcion, departamento.descripcion,empleado.apellido,asis_entra.fecha_horario ";
		
		try {
			stmt = connection.prepareStatement(sentenciaNativa);
			fechaDesde=new java.sql.Date(Desde.getTime());
			fechaHasta=new java.sql.Date(Hasta.getTime());
			
			stmt.setDate(1, fechaDesde);
			stmt.setDate(2, fechaHasta);
							
			resultado = stmt.execute();
			
			if (resultado.equals(true)){
				return stmt.getResultSet();
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		 return null;	
	}
			
		
		@SuppressWarnings("unchecked")
		public List<Empleado> listaEmpeladosById(List<Long> listalista) {
	
			return (List<Empleado>) entityManager
					.createQuery(
							"select empleados from Empleado empleados where empleados.emplId IN (:listaEm)")
											
					.setParameter("listaEm",listalista)
					.getResultList();
		}	
		
		public Date sumarRestarMinutosFecha(Date fecha, int horas){
			 Calendar calendar = Calendar.getInstance();
		     calendar.setTime(fecha); // Configuramos la fecha que se recibe
		     calendar.add(Calendar.MINUTE, horas);  // numero de horas a añadir, o restar en caso de horas<0
		     return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
		}
		
		public List<Long> getIdEmpleados() {
			return idEmpleados;
		}

		public void setIdEmpleados(List<Long> idEmpleados) {
			this.idEmpleados = idEmpleados;
		}

		public Map<Empleado, Integer> getHorasExtrasByEmpleado() {
			return horasExtrasByEmpleado;
		}

		public void setHorasExtrasByEmpleado(Map<Empleado, Integer> horasExtrasByEmpleado) {
			this.horasExtrasByEmpleado = horasExtrasByEmpleado;
		}

		public String getDataBase() {
			return dataBase;
		}

		public void setDataBase(String dataBase) {
			this.dataBase = dataBase;
		}

		public Conexion getConexion() {
			return conexion;
		}

		public void setConexion(Conexion conexion) {
			this.conexion = conexion;
		}
	

}
