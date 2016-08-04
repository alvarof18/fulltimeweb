package com.casapazmino.fulltime.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Contacto;

@Name("contactoList")
public class ContactoList extends EntityQuery<Contacto> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select contacto from Contacto contacto";

	private static final String[] RESTRICTIONS = { "lower(contacto.descripcion) like concat(lower(#{contactoList.contacto.descripcion}),'%')", };

	private String extensionArchivo;
	
	private Contacto contacto = new Contacto();

	public ContactoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Contacto getContacto() {
		return contacto;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
}
