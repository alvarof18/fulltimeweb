package com.casapazmino.fulltime.tareas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.annotations.async.Expiration;
import org.jboss.seam.annotations.async.IntervalCron;
import org.jboss.seam.async.QuartzTriggerHandle;

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.comun.Conexion;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.procesos.EmailAutomatico;

@Name("tareaCorreccion") 
public class TareaCorreccion {
	
	@Asynchronous
	@Transactional
	public QuartzTriggerHandle ejecutarproceso(@Expiration Date when,
			@IntervalCron String interval) {
		
		// Tarea que se ejecuta
		
		System.out.println("Proceso Ejecutado  " + new Date());
		
		Conexion conexion;	
		Connection connection;
		
		PreparedStatement stmt = null;
		int resultado;

		
		String sentenciaNativa = "update permiso set autorizado = 4 where autorizado = 74";
		
		try {
			
			conexion = new Conexion();
			connection = conexion.getConnection();
			
			stmt = connection.prepareStatement(sentenciaNativa);					
			resultado = stmt.executeUpdate();
			
			System.out.println("Proceso de correccion de Permisos" + new Date());
			System.out.println("Filas Actualizadas " + resultado);
			
			sentenciaNativa = "update solicitud_vacacion set autorizado = 4 where autorizado = 100";
			
			stmt = connection.prepareStatement(sentenciaNativa);
			resultado = stmt.executeUpdate();
			
			System.out.println("Proceso de correccion de Solicitudes de Vacaciones " + new Date());
			System.out.println("Filas Actualizadas " + resultado);
			
			connection.close();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		 return null;	
	}

}
