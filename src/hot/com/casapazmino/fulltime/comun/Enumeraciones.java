package com.casapazmino.fulltime.comun;

import java.util.Vector;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("enumeraciones")
public class Enumeraciones {
	
    public enum tipoArchivo {
		Pdf,
		Xls,
		Csv,
		Txt,
		Html;
	}
	
//	public tipoArchivo[] getTipoArchivo() {
//		return tipoArchivo.values();
//	}
	
	public Vector <String> ConvertirEnumeracion(String parametro) {
		
		Vector <String>lista= new Vector<String>();
		
		String aux="";
		
		for(int i=0;i<parametro.length();i++){
			//System.out.println(parametro.charAt(i));
			if(parametro.charAt(i)==','){
				lista.add(aux);
				aux="";
			} else {
				aux=aux+parametro.charAt(i);
			}
		}
		
		if(aux.length()>0){
			lista.add(aux);
		}		
		
		return lista;
		
	}
	
	public Vector<String> getTipoArchivo() {
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
		detalleTipoParametros = detalleTipoParametroList.getTipoReporte();		
		String parametro=detalleTipoParametros.getDescripcion();
		
		return ConvertirEnumeracion(parametro);
	}	
	
	public enum tipoReporte {
		Cargo,
		Departamento,
		Grupo,
		Empleado;
	}
	
	public tipoReporte[] getTipoReporte(){
		return tipoReporte.values();
	}
	
	public enum tipoReporte2 {
		Cargo,
		Departamento,
		Grupo,;
	}
	
	public tipoReporte2[] getTipoReporte2(){
		return tipoReporte2.values();
	}
	
	public enum entradaSalida {
		E,
		S,
		EA,
		SA;
	}
	
	public entradaSalida[] getEntradaSalida(){
		return entradaSalida.values();
	}
	
	public enum tipoAtraso {
		Minutos,
		Cantidad;
	}
	
	public tipoAtraso[] getTipoAtraso(){
		return tipoAtraso.values();
	}	
	
	public enum tipoDescuento {
		Porcentaje,
		Valor,
		Vacaciones;
	}
	
	public tipoDescuento[] getTipoDescuento(){
		return tipoDescuento.values();
	}	
	
	public enum tipoDescuentoPermiso {
		Ninguno,
		Vacaciones;
	}
	
	public tipoDescuentoPermiso[] getTipoDescuentoPermiso(){
		return tipoDescuentoPermiso.values();
	}
	
	public enum tipoFecha {
		Laborable,
		Libre,
		Feriado,
		Permiso,
		Vacaciones;
	}
	
	public tipoFecha[] getTipoFecha(){
		return tipoFecha.values();
	}

	public enum diasSemana {
		Domingo,
		Lunes,
		Martes,
		Miercoles,
		Jueves,
		Viernes,
		Sabado,
	}
	
	public diasSemana[] getDiasSemana(){
		return diasSemana.values();
	}
	
	public enum tipoDetTipPer {
		horas,
		dias
	}
	
	public tipoDetTipPer[] getTipoDetTipPer(){
		return tipoDetTipPer.values();
	}

}
