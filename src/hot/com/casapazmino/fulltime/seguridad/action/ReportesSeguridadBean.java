package com.casapazmino.fulltime.seguridad.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

import com.casapazmino.fulltime.comun.Comunes;
import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.reportes.ReporteBean;

/**
 * @author SndVll
 */
@Stateless
@Name("reportesSeguridad")
public class ReportesSeguridadBean extends ReporteBean implements ReportesSeguridad {

	@Override
	public void listadoItemMenus() {
		ItemMenuList itemMenuList = (ItemMenuList) Component.getInstance("itemMenuList",true);
		
		String descripcion = itemMenuList.getItemMenu().getDescripcion() + "%";
		Map<String, Object> parametros = new HashMap<String, Object>();
		
		List<JRSortField> sortList = new ArrayList<JRSortField>();
		JRDesignSortField sortField = new JRDesignSortField();
		sortField.setName("grupos_descripcion");
		sortField.setOrder(SortOrderEnum.ASCENDING);
		sortField.setType(SortFieldTypeEnum.FIELD);
		sortList.add(sortField);
		
		JRDesignSortField sortField1 = new JRDesignSortField();
		sortField1.setName("item_menu_descripcion");
		sortField1.setOrder(SortOrderEnum.ASCENDING);
		sortField1.setType(SortFieldTypeEnum.FIELD);
		sortList.add(sortField1);
		
		parametros.put(JRParameter.SORT_FIELDS, sortList);

		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoItemMenus", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Lista Items Menu", itemMenuList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoItemsMenuRoles() {
		ItemsMenuRolesList itemsMenuRolesList = (ItemsMenuRolesList) Component.getInstance("itemsMenuRolesList",true);
		
		String descripcionRol = "%"; // itemsMenuRolesList.getItemsMenuRoles().getRoles().getDescripcion() + "%";
		String descripcionGrupo = "%"; // itemsMenuRolesList.getItemsMenuRoles().getGrupos().getDescripcion() + "%";
		String descripcionSubGrupo = "%"; // itemsMenuRolesList.getItemsMenuRoles().getSubgrupos().getDescripcion() + "%";
		String descripcionItemMenu = "%"; // itemsMenuRolesList.getItemsMenuRoles().getItemMenu().getDescripcion() + "%";
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcionRol", descripcionRol);
			parametros.put("descripcionGrupo", descripcionGrupo);
			parametros.put("descripcionSubGrupo", descripcionSubGrupo);
			parametros.put("descripcionItemMenu", descripcionItemMenu);
			
			this.crearArchivo("listadoItemsMenuRoles", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Lista Accesos", itemsMenuRolesList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoMenus() {
		GruposList gruposList = (GruposList) Component.getInstance("gruposList",true);
		
		String descripcion = gruposList.getGrupos().getDescripcion() + "%";
		String etiqueta = gruposList.getGrupos().getEtiqueta() + "%";
		String nombre = gruposList.getGrupos().getNombre() + "%";
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			parametros.put("etiqueta", etiqueta);
			parametros.put("nombre", nombre);
			this.crearArchivo("listadoMenus", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Lista Grupos Menu", gruposList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoRoles() {
		RolesList rolesList = (RolesList) Component.getInstance("rolesList",true);
		
		String descripcion = rolesList.getRoles().getDescripcion() + "%";
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoRoles", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Lista Roles", rolesList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoSubmenus() {
		SubgruposList subgruposList = (SubgruposList) Component.getInstance("subgruposList");
	
		String descripcion = subgruposList.getSubgrupos().getDescripcion() + "%";
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoSubmenus", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Lista SubMenus", subgruposList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void listadoUsuarios() {
		UsuariosList usuariosList = (UsuariosList) Component.getInstance("usuariosList",true);
		
		String descripcion = usuariosList.getUsuarios().getUsuario() + "%";
		Map<String, Object> parametros = new HashMap<String, Object>();
		try {
			parametros.put("descripcion", descripcion);
			this.crearArchivo("listadoUsuarios", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Lista Usuarios", usuariosList.getExtensionArchivo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// **************************************
	// REPORTES ASISTENCIA
	// **************************************
	
	@Override
	public void reporteAuditorias() {
		
		AuditoriaList auditoriaList = (AuditoriaList) Component.getInstance("auditoriaList",true);
		Map<String, Object> parametros = new HashMap<String, Object>();		
				
		Date fechaInicial = auditoriaList.getFechaDesde();
		Date fechaFinal = auditoriaList.getFechaHasta();

		// controlar estos cuando el usuario no haya selecionado el parametro
		// porque da error null pointer exception

		Long[] arregloCiudades = auditoriaList.getCiudades();
		String ciudades = Comunes.arrayToString(arregloCiudades, Constantes.SEPARADOR_LISTAS);
		 		
		try {
			parametros.put("ciudades",ciudades);
			parametros.put("fechaInicial",fechaInicial);
			parametros.put("fechaFinal",fechaFinal);
			
			this.crearArchivo("reporteAuditorias", parametros, Constantes.RUTA_LISTADOS_FUENTES, Constantes.RUTA_LISTADOS_SERVIDOR,
					"Auditoria", auditoriaList.getExtensionArchivo());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}