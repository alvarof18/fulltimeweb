package com.casapazmino.fulltime.comun;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.jboss.seam.annotations.Name;

@Name("Fechas")
public class Fechas {
	
	// RETORNA LA FECHA ACTUAL
	public static Date fechaActual(){
		return new Date();
	}
	
	// RETORNA CALENDARIO ACTUAL
	public static Calendar calendarioActual(){
		return Calendar.getInstance();
	}
	
	// SUMAR O RESTAR FECHAS
	// Para sumar recibe un parametro positivo (+)
	// Para restar recibe un parametro negativo (-)	
    public static Date SumarRestarDias(Date fecha, int dias) {
        Calendar calendar = calendarioActual();
        calendar.setTime(fecha);
        calendar.add(Calendar.DATE, dias);
        return calendar.getTime();
    }

	// SUMAR O RESTAR FECHAS - CALENDARIO
	// Para sumar recibe un parametro positivo (+)
	// Para restar recibe un parametro negativo (-)	
    public static Calendar sumarRestarDias(Calendar calendar, int dias) {
    	Calendar nuevoCalendario = Calendar.getInstance();
    	nuevoCalendario = (Calendar) calendar.clone();
    	nuevoCalendario.add(Calendar.DATE, dias);
    	return nuevoCalendario;
    }

	// CALCULA LA DIFERENCIA DE TIEMPO ENTRE DOS FECHAS
	public static long calcularTiempo(Date fechaDesde, Date fechaHasta){
		
		GregorianCalendar fechaInicial = new GregorianCalendar();
		GregorianCalendar fechaFinal = new GregorianCalendar();

		fechaInicial.setTime(fechaDesde);
		fechaFinal.setTime(fechaHasta);		
		
		long tiempo = fechaFinal.getTimeInMillis() - fechaInicial.getTimeInMillis();
		return tiempo;
	}
	
	// RETORNA EL NUMERO DE DIAS
	public static long numeroDias(Date fechaDesde, Date fechaHasta){
		long tiempo = calcularTiempo(fechaDesde, fechaHasta);
		return (tiempo / (1000 * 60 * 60 * 24) + 1);
	}
	
	// RETORNA EL NUMERO DE HORAS
	public Double numeroHoras(Date fechaDesde, Date fechaHasta){
		Double tiempo = (double) calcularTiempo(fechaDesde, fechaHasta);
		return (tiempo / (1000 * 60 * 60));
	}

	// RETORNA EL NUMERO DE MINUTOS	
	public long numeroMinutos(Date fechaDesde, Date fechaHasta){
		long tiempo = calcularTiempo(fechaDesde, fechaHasta);
		return (tiempo / (1000 * 60));
	}
	
	// CONVERTIR FECHA A CALENDARIO
	public Calendar fechaCalendario (Date fecha) {
		Calendar fechaCalendario = Calendar.getInstance();
		fechaCalendario.setTime(fecha);
		return fechaCalendario;
	}
	
	// CONVERTIR CALENDARIO A FECHA 
	public static Date CalendarioFecha (Calendar Calendario) {
		Calendar calendario = Calendar.getInstance();
		Date fecha=calendario.getTime();
		return fecha;
	}
	
	// CAMBIA UNA FECHA A UN FORMATO ESPECIFICO
	public static String cambiarFormato(Date fecha, String Formato){
		// Ejemplo Formato "yyyyMMdd" o "EEE MMM dd 12:00:00 z yyyy"
		SimpleDateFormat formato = new SimpleDateFormat(Formato);
		String cadenaFecha = formato.format(fecha);
		return cadenaFecha;
	}
	
	public static Date cambiarFormatoFecha(Date fecha, String Formato){
		SimpleDateFormat formato = new SimpleDateFormat(Formato);
		String stringFecha = cambiarFormato(fecha, "yyyy-MM-dd");

		Date dfecha = null;
		try {
			dfecha =  formato.parse(stringFecha);
		} catch (ParseException pe) {
			// TODO Auto-generated catch block
			pe.printStackTrace();
		}
		
		return dfecha;
	}
		
	
/*	
	public void diferenciaFechas(){ 

		int dias = 0;
		int horas = 0;
		float minutos = 0;
		
		GregorianCalendar fechaDesde = new GregorianCalendar();
		fechaDesde.setTime(this.instance.getFechaDesde());
		
		GregorianCalendar fechaHasta = new GregorianCalendar(); 
		fechaHasta.setTime(this.instance.getFechaHasta());
		
		
		dias =  fechaHasta.get(Calendar.DAY_OF_YEAR) - fechaDesde.get(Calendar.DAY_OF_YEAR);
		horas = fechaHasta.get(Calendar.HOUR_OF_DAY) - fechaDesde.get(Calendar.HOUR_OF_DAY);		
		minutos = (horas * 60) + fechaHasta.get(Calendar.MINUTE) - fechaDesde.get(Calendar.MINUTE);
		
	}

	public void recorrerFechas(){
		long dias = fechas.numeroDias(this.getInstance().getFechaDesde(), this.getInstance().getFechaHasta());

				for (int i = 0; i < dias; i++) {
					Calendar fechaSiguiente = Calendar.getInstance();
					fechaSiguiente.setTime(fechaDesde.getTime());
					listaFechas.add(fechaSiguiente);
					
					fechaDesde.add(Calendar.DATE, 1);
				}
				
	}
	// para fechas en español
	
	SimpleDateFormat formateador = new SimpleDateFormat(
   "dd 'de' MMMM 'de' yyyy", new Locale("es_ES"));  // new Locale(“es”,”CO”)
   Date fechaDate = new Date();
   String fecha = formateador.format(fechaDate);
	
*/	
}
