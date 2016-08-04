package com.casapazmino.fulltime.comun;

import java.io.IOException;

import javax.ejb.Local;

@Local
public interface SubirArchivo {
	public void subirTimbres() throws IOException;
}
