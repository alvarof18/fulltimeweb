package com.casapazmino.fulltime.comun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.faces.context.FacesContext;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;


@Name("subirArchivo")
@Scope(ScopeType.EVENT)
public class SubirArchivoBean implements SubirArchivo {

	@In(value = "#{facesContext}")
	FacesContext facesContext;

	private byte[] archivo;
	private String archivoNombre; 

	public void subirTimbres() throws IOException {

//		String path = ((ServletContext) facesContext.getExternalContext()
//				.getContext()).getRealPath("/my/files/dir");
		
		String path = Constantes.RUTA_ARCHIVOS_TIMBRES;
		File f = new File(path, archivoNombre);
		
		FileOutputStream fo = new FileOutputStream(f);
		fo.write(archivo);
		fo.flush();
		fo.close();
	}

	public byte[] getArchivo() {
		return archivo;
	}

	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	public String getArchivoNombre() {
		return archivoNombre;
	}

	public void setArchivoNombre(String archivoNombre) {
		this.archivoNombre = archivoNombre;
	}
}
