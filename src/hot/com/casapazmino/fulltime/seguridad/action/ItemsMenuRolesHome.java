package com.casapazmino.fulltime.seguridad.action;

import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.seguridad.model.Grupos;
import com.casapazmino.fulltime.seguridad.model.ItemMenu;
import com.casapazmino.fulltime.seguridad.model.ItemsMenuRoles;
import com.casapazmino.fulltime.seguridad.model.Roles;
import com.casapazmino.fulltime.seguridad.model.Subgrupos;

@Name("itemsMenuRolesHome")
public class ItemsMenuRolesHome extends EntityHome<ItemsMenuRoles> {

	private static final long serialVersionUID = 1L;
	
	@In
	private FacesMessages facesMessages;
	
	@In(create = true)
	GruposHome gruposHome;
	@In(create = true)
	ItemMenuHome itemMenuHome;
	@In(create = true)
	RolesHome rolesHome;
	@In(create = true)
	SubgruposHome subgruposHome;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;	

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setItemsMenuRolesId(Long id) {
		setId(id);
	}

	public Long getItemsMenuRolesId() {
		return (Long) getId();
	}

	@Override
	protected ItemsMenuRoles createInstance() {
		ItemsMenuRoles itemsMenuRoles = new ItemsMenuRoles();
		return itemsMenuRoles;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		Grupos grupos = gruposHome.getDefinedInstance();
		if (grupos != null) {
			getInstance().setGrupos(grupos);
		}
		ItemMenu itemMenu = itemMenuHome.getDefinedInstance();
		if (itemMenu != null) {
			getInstance().setItemMenu(itemMenu);
		}
		Roles roles = rolesHome.getDefinedInstance();
		if (roles != null) {
			getInstance().setRoles(roles);
		}
		Subgrupos subgrupos = subgruposHome.getDefinedInstance();
		if (subgrupos != null) {
			getInstance().setSubgrupos(subgrupos);
		}
	}

	public boolean isWired() {
		if (getInstance().getGrupos() == null)
			return false;
		if (getInstance().getItemMenu() == null)
			return false;
		if (getInstance().getRoles() == null)
			return false;
		if (getInstance().getSubgrupos() == null)
			return false;
		return true;
	}

	public ItemsMenuRoles getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validar(){
		String mensajeError = "Ninguno";
		
		mensajeError = this.validarEntidad();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarEntidad(){
		List<ItemsMenuRoles> itemsMenuRoles = this.buscarEntidad();
		itemsMenuRoles.remove(this.getInstance());
		if (itemsMenuRoles.size() != 0) {
			return "Seleccion ya existe";
		}
		return "Ninguno";
	}
	
	public List<ItemsMenuRoles> buscarEntidad() {
		ItemsMenuRolesList itemsMenuRolesList = (ItemsMenuRolesList) Component.getInstance("itemsMenuRolesList",true);
		
		itemsMenuRolesList.getItemsMenuRoles().getRoles().setId(this.getInstance().getRoles().getId());
		itemsMenuRolesList.getItemsMenuRoles().getGrupos().setId(this.getInstance().getGrupos().getId());
		itemsMenuRolesList.getItemsMenuRoles().getSubgrupos().setId(this.getInstance().getSubgrupos().getId());
		itemsMenuRolesList.getItemsMenuRoles().getItemMenu().setId(this.instance.getItemMenu().getId());
		
		List<ItemsMenuRoles> itemsMenuRoles = itemsMenuRolesList.getResultList();
		return itemsMenuRoles;
	}
	
	@Transactional
	@Override
	public String persist(){
		String mensajeError = "Ninguno";
		String persisted = null;
		try {
			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				persisted = super.persist();
				
				if (persisted.equals("persisted")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("ItemMenuRoles", "Insertar", cadenaActual, cadenaActual);
					auditoriaHome.persist();
					facesMessages.add("#{messages['mensaje.grabar']}");
				}
			} else {
				facesMessages.add(mensajeError);
			}

		} catch (Exception e) {
			e.printStackTrace();
			facesMessages.add("Error al crear registro");
		}
		return persisted;
	}

	@Transactional
	@Override
	public String update(){
		String mensajeError = "Ninguno";
		String updated = null;
		
		try {
			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("ItemMenuRoles", "Modificar", cadenaAnterior, cadenaActual);
					auditoriaHome.persist();
					facesMessages.add("#{messages['mensaje.actualizar']}");
				}
			} else {
				facesMessages.add(mensajeError);
			}

		} catch (Exception e) {
			facesMessages.add("#{messages['error.actualizar']}");
		}
		return updated;
	}


	@Override
	public String remove(){
		String removed = null;
		try {
			removed = super.remove();
			if (removed.equals("removed")) {
				this.setCadenaActual(this.getInstance().toString());
				auditoriaHome.asignarCampos("ItemMenuRoles", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");									
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public String getCadenaAnterior() {
		return cadenaAnterior;
	}

	public void setCadenaAnterior(String cadenaAnterior) {
		this.cadenaAnterior = cadenaAnterior;
	}

	public String getCadenaActual() {
		return cadenaActual;
	}

	public void setCadenaActual(String cadenaActual) {
		this.cadenaActual = cadenaActual;
	}


	/*public List<PermisosMenu> getPermisosMenus() {
		return getInstance() == null ? null : new ArrayList<PermisosMenu>(
				getInstance().getPermisosMenus());
	}*/
	
	public void VisualizarMenu(ValueChangeEvent event) {
		try{		
			this.instance.setGrupos(null);
			this.instance.setSubgrupos(null);
			Grupos grupos = ( (Grupos) event.getNewValue());	
			this.instance.setGrupos(grupos);
		}catch(Exception e){
			this.instance.setGrupos(null);
			this.instance.setSubgrupos(null);
		}	
	}
	
	public void VisualizarSubMenu(ValueChangeEvent event) {
		try{
			Subgrupos subgrupos = ( (Subgrupos) event.getNewValue());
			this.instance.setSubgrupos(subgrupos);			
		}catch(Exception e){
			this.instance.setSubgrupos(null);
		}	
	}
	
	public List<Subgrupos> getListaSubgrupoSelect() {		
		Grupos grupos=this.getInstance().getGrupos();
		if(grupos!=null){
			if(grupos.getNombre()!=null){
				SubgruposList subgrupos = (SubgruposList) Component.getInstance("subgruposList", true);
				subgrupos.getSubgrupos().setGrupos(grupos);
				subgrupos.setMaxResults(null);
				return subgrupos.getResultList();
			}else{
				return null;
			}					
		}else{
			return null;
		}		
	}
	
	public List<ItemMenu> getListaItemMenuSelect() {		
		Subgrupos subgrupos=this.getInstance().getSubgrupos();			
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
	
}