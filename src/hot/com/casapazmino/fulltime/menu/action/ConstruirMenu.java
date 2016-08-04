package com.casapazmino.fulltime.menu.action;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.ui.component.html.HtmlLink;
import org.richfaces.component.html.HtmlPanelMenu;
import org.richfaces.component.html.HtmlPanelMenuGroup;
import org.richfaces.component.html.HtmlPanelMenuItem;

import com.casapazmino.fulltime.seguridad.model.Grupos;
import com.casapazmino.fulltime.seguridad.model.ItemMenu;
import com.casapazmino.fulltime.seguridad.model.ItemsMenuRoles;
import com.casapazmino.fulltime.seguridad.model.PermisosMenu;
import com.casapazmino.fulltime.seguridad.model.Roles;
import com.casapazmino.fulltime.seguridad.model.Subgrupos;
import com.casapazmino.fulltime.seguridad.model.Usuarios;
import com.casapazmino.fulltime.seguridad.model.UsuariosRoles;

@Name("construirMenu")
@Scope(ScopeType.SESSION)
public class ConstruirMenu implements Serializable {
	
	private static final long serialVersionUID = 5922896283171903184L;
	
	@Out(required=false, scope=ScopeType.SESSION)
	private List<HtmlPanelMenuGroup> panelMenuGrupoLista;
	private List<HtmlPanelMenuGroup> panelSubmenuGrupoLista;
	private List<HtmlPanelMenuItem> panelMenuItemLista;

	@In
	Credentials credentials;
	
	ConsultarMenu consultarMenu;
	
	private Usuarios usuarios;
	
	public ConstruirMenu()
	{
		try
		{
			this.inicializar();
		}
		catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	private void inicializar() 
	{
		this.panelMenuGrupoLista=new ArrayList<HtmlPanelMenuGroup>();
		panelMenuGrupoLista.clear();
		this.panelSubmenuGrupoLista=new ArrayList<HtmlPanelMenuGroup>();
		panelSubmenuGrupoLista.clear();
		this.panelMenuItemLista=new ArrayList<HtmlPanelMenuItem>();
		panelMenuItemLista.clear();
	}
	
	/**
	 * METODO PARA CREAR EL PANEL MENU GRUPO
	 * @param nombreGrupo
	 * @param panelSubMenuGrupoLista
	 * @param etiqueta
	 * @param expanded
	 * @return
	 */
	private HtmlPanelMenuGroup crearPanelMenuGrupo(String nombreGrupo, List<HtmlPanelMenuGroup> panelSubMenuGrupoLista, String etiqueta, boolean expanded)
	{
		
		HtmlPanelMenuGroup htmlPanelMenuGrupo=new HtmlPanelMenuGroup();
		htmlPanelMenuGrupo.setId("HPMG1" + nombreGrupo.trim() + System.currentTimeMillis());
		htmlPanelMenuGrupo.setName("HPMG1" + nombreGrupo.trim() + System.currentTimeMillis());
		htmlPanelMenuGrupo.setLabel(etiqueta);
		htmlPanelMenuGrupo.setIconExpanded("disc");
		htmlPanelMenuGrupo.setIconCollapsed("chevron");
		htmlPanelMenuGrupo.setExpanded(expanded);
		htmlPanelMenuGrupo.getChildren().addAll(panelSubMenuGrupoLista);
		return htmlPanelMenuGrupo;
	}
	
	private HtmlPanelMenuGroup crearPanelMenuSubGrupo(String nombreSubgrupo, String etiqueta, List<HtmlPanelMenuItem> panelMenuItemLista, boolean expanded) 
	{

		HtmlPanelMenuGroup panelMenuGroup = new HtmlPanelMenuGroup();
		panelMenuGroup.setId("HPMG2" + nombreSubgrupo.trim() + System.currentTimeMillis());
		panelMenuGroup.setName("HPMG2" + nombreSubgrupo.trim()+ System.currentTimeMillis());
		panelMenuGroup.setLabel(etiqueta);
		panelMenuGroup.setIconExpanded("triangleUp");
		panelMenuGroup.setIconCollapsed("triangleDown");
		panelMenuGroup.setExpanded(expanded);
		panelMenuGroup.getChildren().addAll(panelMenuItemLista);

		return panelMenuGroup;
	}
	
	private HtmlPanelMenuItem createPanelMenuItem(String nombreItem, String link, String labelItem) 
	{
		HtmlPanelMenuItem panelMenuItem = null;
		HtmlLink htmlLink = this.createLink(nombreItem, link, labelItem);
		if (htmlLink != null) {
			panelMenuItem = new HtmlPanelMenuItem();
			panelMenuItem.setId("HPMI" + nombreItem.trim() + System.currentTimeMillis());
			panelMenuItem.setName("HPMI" + nombreItem.trim()
					+ System.currentTimeMillis());
			panelMenuItem.getChildren().add(htmlLink);
			panelMenuItem.setAjaxSingle(true);
			panelMenuItem.setImmediate(true);
			panelMenuItem.setBypassUpdates(true);
		}
		return panelMenuItem;
	}
	
	private HtmlLink createLink(String nombreItem, String link, String label) 
	{

		HtmlLink links = new HtmlLink();
		links.setId("linkMenu" + nombreItem.trim() + System.currentTimeMillis());
		links.setIncludePageParams(false);
		links.setView(link);
		links.setValue(label);
		links.setPropagation("none");
		return links;
	}
	
	public HtmlPanelMenu cargarMenu(String nombreUsuario)
	{
		consultarMenu=new ConsultarMenu();

		List<UsuariosRoles> userRolLista=consultarMenu.getUsuarioRol(nombreUsuario);

		for (UsuariosRoles usuariosRoles : userRolLista) {

			usuarios = usuariosRoles.getUsuarios();
			
			Contexts.getSessionContext().set("users", usuarios);

			List<ItemsMenuRoles> menuRolesList = consultarMenu.getItemMenuRoles(usuariosRoles.getRoles().getId());
			this.procesarMenuRoles(menuRolesList);
			this.asignarPermisos(usuariosRoles.getRoles());
		}
		
		HtmlPanelMenu panelMenu=new HtmlPanelMenu();
		
		panelMenu.setId("menuFulltime"+System.currentTimeMillis());
		panelMenu.getChildren().addAll(this.panelMenuGrupoLista);		
		
		/**
		 * COLOCAR MENU EN ARCHIVO MENU VERTICAL
		 */
		
		Contexts.getSessionContext().set("panelMenuSesion", panelMenu);
		return panelMenu;
	}
	
	private void procesarMenuRoles(List<ItemsMenuRoles> menuRolesList) 
	{

		List<ItemMenu> itemsMenuList = new ArrayList<ItemMenu>();

		for (ItemsMenuRoles itemsMenuRoles : menuRolesList) {
			 itemsMenuList.add(consultarMenu.getItemMenu(itemsMenuRoles.getItemMenu().getId()));
		}
		this.procesarItemsMenu(itemsMenuList,"valor seleccionado, no olvidar borrar de sesion en logout");
	}
	
	private void procesarItemsMenu(List<ItemMenu> itemsMenuList, String menuItemSelect) 
	{

		Long codigoSubGrupoAnt = new Long(0);
		Long codigoGrupoAnt=new Long(0);
		
		for (ItemMenu itemsMenu : itemsMenuList) 
		{
			codigoSubGrupoAnt = itemsMenu.getSubgrupos().getId();
			codigoGrupoAnt = itemsMenu.getSubgrupos().getGrupos().getId();
			break;
		}
		ItemMenu itemsMenuLast = null;

		for (ItemMenu itemsMenu : itemsMenuList) {

			this.procesarGroupMenu(codigoGrupoAnt, codigoSubGrupoAnt, itemsMenu, menuItemSelect);

			codigoSubGrupoAnt = itemsMenu.getSubgrupos().getId();
			codigoGrupoAnt = itemsMenu.getGrupos().getId();
			
			itemsMenuLast = itemsMenu;
		}
		if (itemsMenuLast != null) {
			this.procesarCrearSubGroupMenu(itemsMenuLast.getSubgrupos().getId(), itemsMenuLast);
			this.procesarCrearGroupMenu(itemsMenuLast.getGrupos().getId(), menuItemSelect);
		}
	}
	
	private void procesarGroupMenu(Long codigoGrupoAnt, Long codigoSubGrupoAnt, ItemMenu itemsMenu, String menuItemSelect) 
	{
		if (codigoGrupoAnt == itemsMenu.getGrupos().getId()) 
		{
			this.procesarSubGroupMenu(codigoSubGrupoAnt, itemsMenu, menuItemSelect);
		} 
		else 
		{
			if ((this.panelSubmenuGrupoLista != null && !this.panelSubmenuGrupoLista.isEmpty())
					|| (this.panelMenuItemLista != null && !this.panelMenuItemLista.isEmpty())) {

				this.procesarCrearSubGroupMenu(codigoSubGrupoAnt, itemsMenu);
			}
			this.procesarCrearGroupMenu(codigoGrupoAnt, menuItemSelect);

			HtmlPanelMenuItem htmlPanelMenuItem = this.createPanelMenuItem(itemsMenu.getNombre(), itemsMenu.getLink(), itemsMenu.getEtiqueta());
			this.panelMenuItemLista = new ArrayList<HtmlPanelMenuItem>();
			if (htmlPanelMenuItem != null) 
			{
				this.panelMenuItemLista.add(htmlPanelMenuItem);
			}
		}
	}
	
	private void procesarSubGroupMenu(Long codigoSubGrupoAnt,ItemMenu itemsMenu, String menuItemSelect) {
		
		if (codigoSubGrupoAnt == itemsMenu.getSubgrupos().getId()) {
			HtmlPanelMenuItem htmlPanelMenuItem = this.createPanelMenuItem(itemsMenu.getNombre(), itemsMenu.getLink(), itemsMenu.getEtiqueta());
			if (htmlPanelMenuItem != null) {
				if (this.panelMenuItemLista != null) {
					panelMenuItemLista.add(htmlPanelMenuItem);
				}
			}
		} 
		else {
			this.procesarCrearSubGroupMenu(codigoSubGrupoAnt, itemsMenu);
		}
	}
	
	private void procesarCrearGroupMenu(Long codigoGrupoAnt, String menuItemSelect) {
		Grupos grupos = consultarMenu.getGrupo(codigoGrupoAnt);
		
		this.panelMenuGrupoLista.add(this.crearPanelMenuGrupo(grupos.getNombre(), this.panelSubmenuGrupoLista, grupos.getEtiqueta(), false));
		this.panelSubmenuGrupoLista = new ArrayList<HtmlPanelMenuGroup>();
	}
	
	private void procesarCrearSubGroupMenu(Long codigoSubGrupo, ItemMenu itemsMenu) {
		
		Subgrupos subgrupos = consultarMenu.getSubGrupo(codigoSubGrupo);

		this.panelSubmenuGrupoLista.add(this.crearPanelMenuSubGrupo(subgrupos.getNombre(), subgrupos.getEtiqueta(),this.panelMenuItemLista, false));

		this.panelMenuItemLista = new ArrayList<HtmlPanelMenuItem>();
		HtmlPanelMenuItem htmlPanelMenuItem = this.createPanelMenuItem(itemsMenu.getNombre(), itemsMenu.getLink(), itemsMenu.getEtiqueta());

		if (htmlPanelMenuItem != null) {
			if (this.panelMenuItemLista != null) {
				this.panelMenuItemLista.add(htmlPanelMenuItem);
			}
		}		
	}
	
	public HtmlPanelMenu getPanelMenu() {
		return null;
	}
	
	public void eliminarVarSessionMenu() {
		Contexts.getApplicationContext().remove("idRol");
		Contexts.getApplicationContext().remove("idItemMenu");
		Contexts.getApplicationContext().remove("idSubgrupo");
		Contexts.getApplicationContext().remove("idGrupo");
	}
	
	/**
	 * Metodo para colocar los permisos en variables de session
	 */
	public void asignarPermisos(Roles rol)
	{
		for(PermisosMenu pr:consultarMenu.getPermisosMenu(rol))
		{
			Contexts.getApplicationContext().set("permisoCrear",pr.getCrear());
			Contexts.getApplicationContext().set("permisoEditar",pr.getModificar());
			Contexts.getApplicationContext().set("permisoEliminar",pr.getEliminar());
			Contexts.getApplicationContext().set("permisoVer",pr.getVer());
			Contexts.getApplicationContext().set("permisoImprimir",pr.getImprimir());
			Contexts.getApplicationContext().set("permisoEliminarPerm",pr.getEliminarPerm());
			Contexts.getApplicationContext().set("permisoAutorizarPerm",pr.getAutorizarPerm());
			Contexts.getApplicationContext().set("permisoLegalizarPerm",pr.getLegalizarPerm());
			Contexts.getApplicationContext().set("permisoAccesoEmpleados",pr.getAccesoEmpleados());
		}
		Contexts.getApplicationContext().remove("permisoRol");
	}
	
	public List<HtmlPanelMenuItem> getPanelMenuItemList() {
		return this.panelMenuItemLista;
	}
	
	public void setPanelMenuItemList(List<HtmlPanelMenuItem> panelMenuItemList) {
		this.panelMenuItemLista = panelMenuItemList;
	}

	public List<HtmlPanelMenuGroup> getPanelSubMenuGroupList() {
		return this.panelSubmenuGrupoLista;
	}

	public void setPanelSubMenuGroupList(List<HtmlPanelMenuGroup> panelSubMenuGroupList) {
		this.panelSubmenuGrupoLista = panelSubMenuGroupList;
	}

	public List<HtmlPanelMenuGroup> getPanelMenuGroupList() {
		return this.panelMenuGrupoLista;
	}

	public void setPanelMenuGroupList(List<HtmlPanelMenuGroup> panelMenuGroupList) {
		this.panelMenuGrupoLista = panelMenuGroupList;
	}

	public Usuarios getUsuario() {
		return usuarios;
	}

	public void setUsuario(Usuarios usuario) {
		this.usuarios = usuario;
	}
}
