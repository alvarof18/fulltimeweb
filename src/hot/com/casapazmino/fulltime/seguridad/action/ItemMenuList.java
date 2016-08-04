package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.seguridad.model.ItemMenu;

@Name("itemMenuList")
public class ItemMenuList extends EntityQuery<ItemMenu> {

	private static final long serialVersionUID = 1L;
	private static final String ORDER="itemMenu.orden";

	private static final String EJBQL = "select itemMenu from ItemMenu itemMenu";

	private static final String[] RESTRICTIONS = {
			"itemMenu.id = #{idItemMenu}",
			"lower(itemMenu.descripcion) like concat(lower(#{itemMenuList.itemMenu.descripcion}),'%')",
			"lower(itemMenu.etiqueta) like concat(lower(#{itemMenuList.itemMenu.etiqueta}),'%')",
			"lower(itemMenu.link) like concat(lower(#{itemMenuList.itemMenu.link}),'%')",
			"lower(itemMenu.nombre) like concat(lower(#{itemMenuList.itemMenu.nombre}),'%')",
			"lower(itemMenu.subgrupos.nombre) like concat(lower(#{itemMenuList.itemMenu.subgrupos.nombre}),'%')",};

	private String extensionArchivo;
	
	private ItemMenu itemMenu;
	
	@In(create = true)
	ItemsMenuRolesList itemsMenuRolesList;

	public ItemMenuList() {
		this.instanciar(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public ItemMenu getItemMenu() {
		return itemMenu;
	}
	
	public void setItemMenu(ItemMenu itemMenu){
		this.itemMenu=itemMenu;
	}
	
	public void instanciar(int maxResul)
	{
		itemMenu = new ItemMenu();
		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder(ORDER);
		if(maxResul>0)
		{
			setMaxResults(maxResul);
		}
	}
	
	public ItemMenuList(int maxResul)
	{
		this.instanciar(maxResul);
	}
	
	public List<ItemMenu> getListaItemMenu()
	{
		this.setMaxResults(null);
		return this.getResultList();
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

}
