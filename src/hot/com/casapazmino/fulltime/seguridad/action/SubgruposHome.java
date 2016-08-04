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

@Name("subgruposHome")
public class SubgruposHome extends EntityHome<Subgrupos> {

	private static final long serialVersionUID = 1L;
	@Logger
	private Log log;
	
	@In(create = true)
	GruposHome gruposHome;
	
	@In
	private FacesMessages facesMessages;
	@In
	EntityManager entityManager;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;
	
	private String cadenaAnterior;
	private String cadenaActual;
	
	GruposList grupoList;
	
	Grupos grupo;
	private Long grupos=Long.valueOf(0);
	private List<SelectItem> gruposLista;

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void setSubgruposId(Long id) {
		setId(id);
	}

	public Long getSubgruposId() {
		return (Long) getId();
	}

	@Override
	protected Subgrupos createInstance() {
		return new Subgrupos();
	}

	public void wire() {
		getInstance();
		
		if (isManaged()) {
			setCadenaAnterior(this.getInstance().toString());
		} else setCadenaAnterior(""); 
		
		this.llenarGruposLista();
		if(isWired())
		{
			this.obtenerIdGrupo();
		}
		Grupos grupos = gruposHome.getDefinedInstance();
		if (grupos != null) {
			getInstance().setGrupos(grupos);
		}
	}

	public boolean isWired() {
		if (getInstance().getGrupos() == null)
			return false;
		return true;
	}

	public Subgrupos getDefinedInstance() {
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
		List<Subgrupos> subgrupos = this.buscarDescripcion();
		subgrupos.remove(this.getInstance());
		if (subgrupos.size() != 0) {
			return "Descripcion ya existe";
		}
		return "Ninguno";
	}
	
	public String validarNombre(){
		List<Subgrupos> subgrupos = this.buscarNombre();
		subgrupos.remove(this.getInstance());
		if (subgrupos.size() != 0) {
			return "Codigo ya existe";
		}
		return "Ninguno";
	}

	public String validarEtiqueta(){
		List<Subgrupos> subgrupos = this.buscarEtiqueta();
		subgrupos.remove(this.getInstance());
		if (subgrupos.size() != 0) {
			return "Etiqueta ya existe";
		}
		return "Ninguno";
	}

	@SuppressWarnings("unchecked")
	public List<Subgrupos> buscarDescripcion() {
		
		String descripcion = this.getInstance().getDescripcion();
		
		return (List<Subgrupos>) entityManager
				.createQuery(
						"select sg from Subgrupos sg where sg.descripcion = (:descripcion)")
				.setParameter("descripcion", descripcion)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Subgrupos> buscarNombre() {
		
		String nombre = this.getInstance().getNombre();
		
		return (List<Subgrupos>) entityManager
				.createQuery(
						"select sg from Subgrupos sg where sg.nombre = (:nombre)")
				.setParameter("nombre", nombre)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Subgrupos> buscarEtiqueta() {
		
		String etiqueta = this.getInstance().getEtiqueta();
		
		return (List<Subgrupos>) entityManager
				.createQuery(
						"select sg from Subgrupos sg where sg.etiqueta = (:etiqueta)")
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
					auditoriaHome.asignarCampos("SubGrupos", "Insertar", cadenaActual, cadenaActual);
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
					auditoriaHome.asignarCampos("SubGrupos", "Modificar", cadenaAnterior, cadenaActual);
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
				auditoriaHome.asignarCampos("SubGrupos", "Eliminar", cadenaAnterior, cadenaActual);
				auditoriaHome.persist();
				facesMessages.add("#{messages['mensaje.eliminar']}");				
			}
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return removed;
	}

	public List<ItemMenu> getItemMenus() {
		return getInstance() == null ? null : new ArrayList<ItemMenu>(
				getInstance().getItemMenus());
	}
	public List<ItemsMenuRoles> getItemsMenuRoleses() {
		return getInstance() == null ? null : new ArrayList<ItemsMenuRoles>(
				getInstance().getItemsMenuRoleses());
	}
	
	public String guardar()
	{
		this.grupo=new Grupos();
		try
		{
			grupo.setId(this.getGrupos());
			this.getInstance().setGrupos(grupo);
			return persist();
		}
		catch (Exception e) {
			// TODO: handle exception
			log.info("ERROR EN METODO GUARDAR: ", e);
		}
		return null;
	}
	
	public void llenarGruposLista()
	{
		log.info("ENTRO EN LLENAR GRUPOS LISTA EN SUBGRUPOS HOME");
		gruposLista=new ArrayList<SelectItem>();
		grupoList=new GruposList();
		try 
		{
			if(gruposLista.isEmpty())
			{
				for(Grupos grupo:grupoList.getListaGrupos())
				{
					gruposLista.add(new SelectItem(grupo.getId(),grupo.getNombre()));
				}
			}
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void obtenerIdGrupo()
	{
		this.setGrupos(this.getInstance().getGrupos().getId());
	}
	
	public Long getGrupos() {
		return grupos;
	}

	public void setGrupos(Long grupos) {
		this.grupos = grupos;
	}
	public List<SelectItem> getGruposLista() {
		return gruposLista;
	}

	public void setGruposLista(List<SelectItem> gruposLista) {
		this.gruposLista = gruposLista;
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
