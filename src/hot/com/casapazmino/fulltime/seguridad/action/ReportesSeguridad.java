package com.casapazmino.fulltime.seguridad.action;

import javax.ejb.Local;

@Local
public interface ReportesSeguridad {
	void listadoRoles();
	void listadoUsuarios();
	void listadoMenus();
	void listadoSubmenus();
	void listadoItemMenus();
	void listadoItemsMenuRoles();
	
	void reporteAuditorias();

}