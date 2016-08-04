package com.casapazmino.fulltime.seguridad.action;

public class Conectar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String usuario;
		ConeccionLdap coneccionLdap = new ConeccionLdap();
		usuario = coneccionLdap.authenticate("","CGE.LOCAL", "prueba_2", "Atencion01");
	}
}
