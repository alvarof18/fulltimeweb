package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.Grupos;
import com.casapazmino.fulltime.seguridad.model.ItemMenu;
import com.casapazmino.fulltime.seguridad.model.ItemsMenuRoles;
import com.casapazmino.fulltime.seguridad.model.Roles;
import com.casapazmino.fulltime.seguridad.model.Subgrupos;

@Name("itemsMenuRolesList")
public class ItemsMenuRolesList extends EntityQuery<ItemsMenuRoles> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select itemsMenuRoles from ItemsMenuRoles itemsMenuRoles";
	
	private static final String ORDER="itemsMenuRoles.subgrupos.grupos.orden,"
		+"itemsMenuRoles.subgrupos.grupos.id,"
		+"itemsMenuRoles.subgrupos.orden,"
		+"itemsMenuRoles.subgrupos.id,"
		+"itemsMenuRoles.itemMenu.orden,"
		+"itemsMenuRoles.itemMenu.id";

	private static final String[] RESTRICTIONS = {
		"itemsMenuRoles.roles.id = #{idRol}",
		"itemsMenuRoles.grupos.id = #{itemsMenuRolesList.itemsMenuRoles.grupos.id}",
		"itemsMenuRoles.subgrupos.id = #{itemsMenuRolesList.itemsMenuRoles.subgrupos.id}",
		"itemsMenuRoles.itemMenu.id = #{itemsMenuRolesList.itemsMenuRoles.itemMenu.id}",
		"itemsMenuRoles.roles.id = #{itemsMenuRolesList.itemsMenuRoles.roles.id}",
	};

	private String extensionArchivo;
	
	private ItemsMenuRoles itemsMenuRoles = new ItemsMenuRoles();

	public ItemsMenuRolesList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public ItemsMenuRoles getItemsMenuRoles() {
		return itemsMenuRoles;
	}
	
	public ItemsMenuRolesList(int maxResul)
	{
		this.instaciar(maxResul);
	}
	
	private void instaciar(int maxResul)
	{
		itemsMenuRoles=new ItemsMenuRoles();
		Roles rol=new Roles();
		itemsMenuRoles.setRoles(rol);
		itemsMenuRoles.setId(new ItemsMenuRoles().getId());
		Subgrupos subGrupo=new Subgrupos();
		subGrupo.setGrupos(new Grupos());
		ItemMenu itemMenu=new ItemMenu();
		itemMenu.setSubgrupos(subGrupo);
		itemsMenuRoles.setItemMenu(itemMenu);
		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder(ORDER);
		if(maxResul>=0)
		{
			setMaxResults(maxResul);
		}
	}
	
		
	public ItemsMenuRoles getItemMenuRoles()
	{
		return itemsMenuRoles;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
	
	//
	public List<Subgrupos> getListaSubgrupoSelect() {		
		
		Grupos grupos=this.getItemsMenuRoles().getGrupos();	
		
		if(grupos!=null){
//			System.out.println("grupos.getNombre();...................................................:"+grupos.getNombre()+":");
			//if(grupos.getNombre()!=null){
				SubgruposList subgruposList = (SubgruposList) Component.getInstance("subgruposList", true);
				subgruposList.getSubgrupos().setGrupos(grupos);
				subgruposList.setMaxResults(null);
				return subgruposList.getResultList();
//			}else{
//				return null;
//			}			
		}else{
			return null;
		}
	}
	
	public void VisualizarMenu(ValueChangeEvent event) {
		
		try{				
			Grupos grupos = ( (Grupos) event.getNewValue());	
			this.getItemsMenuRoles().setGrupos(grupos);
		}catch(Exception e){
			this.getItemsMenuRoles().setGrupos(null);
			this.getItemsMenuRoles().setSubgrupos(null);
		}	
	}
	
	public List<ItemMenu> getListaItemMenuSelect() {	
		
		Subgrupos subgrupos=this.getItemsMenuRoles().getSubgrupos();	
		
		if(subgrupos!=null){
				if(subgrupos.getNombre()!=null){
					ItemMenuList itemMenuList = (ItemMenuList) Component.getInstance("itemMenuList", true);
					itemMenuList.getItemMenu().setSubgrupos(subgrupos);
					itemMenuList.setMaxResults(null);
					return itemMenuList.getResultList();	
				}else{
					return null;
				}
						
		}else{
			return null;
		}
			
		
	}
	
	public void VisualizarSubMenu(ValueChangeEvent event) {
		try{
			Subgrupos subgrupos = ( (Subgrupos) event.getNewValue());
			this.getItemsMenuRoles().setSubgrupos(subgrupos);
		}catch(Exception e){
			this.getItemsMenuRoles().setSubgrupos(null);
		}	
	}
}
