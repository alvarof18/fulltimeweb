package com.casapazmino.fulltime.comun;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.casapazmino.fulltime.action.CargoList;
import com.casapazmino.fulltime.model.Cargo;

public class Comunes {
	
	public static BigDecimal formatearNumeroBigDecimal(BigDecimal decimal, int x) {
		return decimal.setScale(x, BigDecimal.ROUND_HALF_UP);
	}

	public static String arrayToString(Long[] arregloString, String separador) {
		if (arregloString == null || separador == null) {
			return null;
		}
		StringBuffer cadena = new StringBuffer();
		if (arregloString.length > 0) {
			
			cadena.append("'");
			
			cadena.append(Long.toString(arregloString[0]));
			
			for (int i = 1; i < arregloString.length; i++) {
				cadena.append("'");
				cadena.append(separador);
				cadena.append("'");
				cadena.append(Long.toString(arregloString[i]));
			}
		}
		cadena.append("'");
		return cadena.toString();
	}
	
	public static String arrayToStringCadena(String[] arregloString, String separador) {
		if (arregloString == null || separador == null) {
			return null;
		}
		StringBuffer cadena = new StringBuffer();
		if (arregloString.length > 0) {
			
			cadena.append("'");
			
			cadena.append(arregloString[0]);
			
			for (int i = 1; i < arregloString.length; i++) {
				cadena.append("'");
				cadena.append(separador);
				cadena.append("'");
				cadena.append(arregloString[i]);
			}
		}
		cadena.append("'");
		return cadena.toString();
	}
	
	public static String arrayToString(ArrayList<Long> arregloString, String separador) {
		if (arregloString == null || separador == null) {
			return null;
		}
		StringBuffer cadena = new StringBuffer();
		if (arregloString.size() > 0) {
			
			cadena.append("'");
			
			cadena.append(Long.toString(arregloString.get(0)));
			
			for (int i = 1; i < arregloString.size(); i++) {
				cadena.append("'");
				cadena.append(separador);
				cadena.append("'");
				cadena.append(Long.toString(arregloString.get(i)));
			}
		}
		cadena.append("'");
		return cadena.toString();
	}
	
	public static String classToString(CargoList cargoList, String separador ){
		List<Cargo> listcargos = cargoList.getListaCargos();
				
		if (listcargos.size() == 0) {
			return null;
		}
		
		StringBuffer cadena = new StringBuffer();
			
		for (Cargo cargo : listcargos) {
			cadena.append("'");
			cadena.append(Long.toString(cargo.getCargId()));
			cadena.append("'");
			cadena.append(separador);
		}

		cadena.append("'");
		return cadena.toString();		
	}
		
	public String obtenerIp(){
		
		 HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		 String IpRemota =request.getRemoteAddr();
		 
		 if(IpRemota.equals("0:0:0:0:0:0:0:1")){
			 IpRemota = "127.0.0.1";
		 }
		 
		 if (IpRemota.length() > 20){
			 IpRemota = "Error IP";
			 	 
		 }

		return IpRemota;
			
	}	
}
