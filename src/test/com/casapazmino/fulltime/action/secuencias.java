/**
 * 
 */
package com.casapazmino.fulltime.action;

/**
 * @author Drosan
 *
 */
public class secuencias {

	/**
	 * 
	 */
	public secuencias() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */

	
	public static void main(String[] args) {
		
		int rango = 1;
		
		// el final es numero de dias + 1
		
		for (int i = 1; i < 36; i++) {

			System.out.println( i + " " +   i % rango);			
			if (i % rango == 0) {
				i = i + rango;
			} 
		}
		
		
		String sCadena = "02:00";
		String sSubCadena = sCadena.substring(3,5);
		System.out.println(sSubCadena);
		sSubCadena = sCadena.substring(0,2);
		System.out.println(sSubCadena);

	}
}
