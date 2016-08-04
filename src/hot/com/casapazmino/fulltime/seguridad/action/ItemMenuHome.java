package com.casapazmino.fulltime.seguridad.action;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.seguridad.model.Grupos;
import com.casapazmino.fulltime.seguridad.model.ItemMenu;
import com.casapazmino.fulltime.seguridad.model.ItemsMenuRoles;
import com.casapazmino.fulltime.seguridad.model.Subgrupos;

@Name("itemMenuHome")
public class ItemMenuHome extends EntityHome<ItemMenu> {

	private static final long serialVersionUID = 1L;
	@Logger
	Log log;
	
	@In(create = true)
	GruposHome gruposHome;
	@In(create = true)
	SubgruposHome subgruposHome;
	
	@In
	private FacesMessages facesMessages;
	@In
	EntityManager entityManager;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;	
	
	public Long idSubGrupo;
	public List<SelectItem>subGrupoLista;
	private SubgruposList subGruposLista;
	private Subgrupos subGrupos;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setItemMenuId(Long id) {
		setId(id);
	}

	public Long getItemMenuId() {
		return (Long) getId();
	}

	@Override
	protected ItemMenu createInstance() {
		ItemMenu itemMenu = new ItemMenu();
		itemMenu.setSubgrupos(new Subgrupos());
		itemMenu.getSubgrupos().setGrupos(new Grupos());
		return itemMenu;
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		this.llenarListaSubGrupo();
		if(isWired())
		{
			this.obtenerIdSubGrupos();
		}
		Grupos grupos = gruposHome.getDefinedInstance();
		if (grupos != null) {
			getInstance().setGrupos(grupos);
		}
		Subgrupos subgrupos = subgruposHome.getDefinedInstance();
		if (subgrupos != null) {
			getInstance().setSubgrupos(subgrupos);
		}
	}

	public boolean isWired() {
		if (getInstance().getGrupos() == null)
			return false;
		if (getInstance().getSubgrupos() == null)
			return false;
		return true;
	}

	public ItemMenu getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String validar(){
		String mensajeError = "Ninguno";
		
		mensajeError = this.validarDescripcion();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarNombre();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		mensajeError = this.validarEtiqueta();
		if (!mensajeError.equals("Ninguno")){
			return mensajeError;
		}
		
		return mensajeError;
	}
	
	public String validarDescripcion(){
		List<ItemMenu> itemMenus = this.buscarDescripcion();
		itemMenus.remove(this.getInstance());
		if (itemMenus.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	public String validarNombre(){
		List<ItemMenu> itemMenus = this.buscarNombre();
		itemMenus.remove(this.getInstance());
		if (itemMenus.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}

	public String validarEtiqueta(){
		List<ItemMenu> itemMenus = this.buscarEtiqueta();
		itemMenus.remove(this.getInstance());
		if (itemMenus.size() != 0) {
			return "Etiqueta ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<ItemMenu> buscarDescripcion() {
		
		String descripcion = this.getInstance().getDescripcion();
		
		return (List<ItemMenu>) entityManager
				.createQuery(
						"select im from ItemMenu im where im.descripcion = (:descripcion)")
				.setParameter("descripcion", descripcion)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemMenu> buscarNombre() {
		
		String nombre = this.getInstance().getNombre();
		
		return (List<ItemMenu>) entityManager
				.createQuery(
						"select im from ItemMenu im where im.nombre = (:nombre)")
				.setParameter("nombre", nombre)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<ItemMenu> buscarEtiqueta() {
		
		String etiqueta = this.getInstance().getEtiqueta();
		
		return (List<ItemMenu>) entityManager
				.createQuery(
						"select im from ItemMenu im where im.etiqueta = (:etiqueta)")
				.setParameter("etiqueta", etiqueta)
				.getResultList();
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
					auditoriaHome.asignarCampos("ItemMenu", "Insertar", cadenaActual, cadenaActual);
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
		this.subGrupos=new Subgrupos();
		try {
			this.subGrupos.setId(this.getIdSubGrupo());
			this.subgruposHome.setSubgruposId(this.subGrupos.getId());
			this.subgruposHome.find();
			log.info("ID DE SUB GRUPO DESDE SUBGRUPOHOME: "+this.subgruposHome.getId());
			log.info("ID DE GRUPO DESDE SUBGRUPOHOME: "+this.subgruposHome.getInstance().getGrupos().getId());
			this.getInstance().setSubgrupos(this.subGrupos);
			this.getInstance().setGrupos(this.subgruposHome.getInstance().getGrupos());
			mensajeError = this.validar();
			if (mensajeError.equals("Ninguno")) {
				updated = super.update();
				if (updated.equals("updated")) {
					this.setCadenaActual(this.getInstance().toString());
					auditoriaHome.asignarCampos("ItemMenu", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("ItemMenu", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");					
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public List<ItemsMenuRoles> getItemsMenuRoleses() {
		return getInstance() == null ? null : new ArrayList<ItemsMenuRoles>(
				getInstance().getItemsMenuRoleses());
	}
	
	public String guardar()
	{
		log.info("ENTRO EN GUARDAR EN ITEMS MENU");
		log.info("SUB GRUPOS ID VARIABLE: ", this.getIdSubGrupo());
		this.subGrupos=new Subgrupos();
		try
		{
			this.subGrupos.setId(this.getIdSubGrupo());
			this.subgruposHome.setSubgruposId(this.subGrupos.getId());
			this.subgruposHome.find();
			log.info("ID DE SUB GRUPO DESDE SUBGRUPOHOME: "+this.subgruposHome.getId());
			log.info("ID DE GRUPO DESDE SUBGRUPOHOME: "+this.subgruposHome.getInstance().getGrupos().getId());
			this.getInstance().setSubgrupos(this.subGrupos);
			this.getInstance().setGrupos(this.subgruposHome.getInstance().getGrupos());
			return persist();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			log.info("ERROR EN GRABAR: ", e);
		}
		return null;
	}
	
	private void llenarListaSubGrupo()
	{
			log.info("ENTRO EN LLENAR GRUPOSlISTA EN SUBGRUPOS HOME");
			this.subGrupoLista=new ArrayList<SelectItem>();
			this.subGruposLista=new SubgruposList();
			try 
			{
				if(subGrupoLista.isEmpty())
				{
					for(Subgrupos subGrupo:subGruposLista.getListaSubgrupo())
					{
						subGrupoLista.add(new SelectItem(subGrupo.getId(),subGrupo.getNombre()));
					}
				}
			} 
			catch (Exception e) 
			{
				// TODO: handle exception
				e.printStackTrace();
			}
	}
	
	private void obtenerIdSubGrupos()
	{
		this.setIdSubGrupo(this.getInstance().getSubgrupos().getId());
	}
	public Long getIdSubGrupo() {
		return idSubGrupo;
	}

	public void setIdSubGrupo(Long idSubGrupo) {
		this.idSubGrupo = idSubGrupo;
	}
	
	public List<SelectItem> getSubGrupoLista() {
		return subGrupoLista;
	}

	public void setSubGrupoLista(List<SelectItem> subGrupoLista) {
		this.subGrupoLista = subGrupoLista;
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
}
