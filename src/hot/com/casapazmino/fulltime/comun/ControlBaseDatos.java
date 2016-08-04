package com.casapazmino.fulltime.comun;

import org.jboss.seam.Component;

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

public class ControlBaseDatos {

	public static String baseDatos;
	public static String modificadorConvertirFecha;
	
	// BUSCA BASE DE DATOS ACTIVA POR LO GENERAL SE UTILIZA EN PROCESOS AUTOMATICOS
	// YA QUE AUN NO SE INGRESA AL SISTEMA Y NO SE SABE QUE BASE ESTA ACTIVA
	public static String buscarBaseDatos() {
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getBaseDatos();

		return detalleTipoParametro.getDescripcion();
		
	};

	public static void colocarBaseDatos() {

		baseDatos = buscarBaseDatos();

		if (baseDatos.equals(Constantes.MYSQL)) {
			modificadorConvertirFecha = "date(";
		} else if (baseDatos.equals(Constantes.SQLSERVER)) {
			modificadorConvertirFecha = "convert(date,";
		} else if (baseDatos.equals(Constantes.ORACLE)) {
			modificadorConvertirFecha = "TRUNC(";
		} else if (baseDatos.equals(Constantes.POSTGRES)) {
			modificadorConvertirFecha = "DATE(";
		}

	};
}
