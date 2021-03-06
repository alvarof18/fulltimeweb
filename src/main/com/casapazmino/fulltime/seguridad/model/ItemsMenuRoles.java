package com.casapazmino.fulltime.seguridad.model;
// Generated 10/01/2011 11:40:10 PM by Hibernate Tools 3.2.2.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.NotNull;

/**
 * ItemsMenuRoles generated by hbm2java
 */
@Entity
@Table(name = "items_menu_roles")
public class ItemsMenuRoles implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Roles roles;
	private Grupos grupos;
	private ItemMenu itemMenu;
	private Subgrupos subgrupos;
	//private Set<PermisosMenu> permisosMenus = new HashSet<PermisosMenu>(0);

	public ItemsMenuRoles() {
		roles=new Roles();
		grupos=new Grupos();
		itemMenu=new ItemMenu();
		subgrupos=new Subgrupos();
	}

	public ItemsMenuRoles(Roles roles, Grupos grupos, ItemMenu itemMenu,
			Subgrupos subgrupos) {
		this.roles = roles;
		this.grupos = grupos;
		this.itemMenu = itemMenu;
		this.subgrupos = subgrupos;
	}
	/*public ItemsMenuRoles(Roles roles, Grupos grupos, ItemMenu itemMenu,
			Subgrupos subgrupos, ) {
		this.roles = roles;
		this.grupos = grupos;
		this.itemMenu = itemMenu;
		this.subgrupos = subgrupos;
		//this.permisosMenus = permisosMenus;
	}*/

	@Id
	@TableGenerator(name = "IDITEMMENUROLES", table = "contadores", pkColumnName = "ID", pkColumnValue = "39", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDITEMMENUROLES")
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roles", nullable = false)
	@NotNull
	public Roles getRoles() {
		return this.roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grupos", nullable = false)
	@NotNull
	public Grupos getGrupos() {
		return this.grupos;
	}

	public void setGrupos(Grupos grupos) {
		this.grupos = grupos;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_menu", nullable = false)
	@NotNull
	public ItemMenu getItemMenu() {
		return this.itemMenu;
	}

	public void setItemMenu(ItemMenu itemMenu) {
		this.itemMenu = itemMenu;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subgrupos", nullable = false)
	@NotNull
	public Subgrupos getSubgrupos() {
		return this.subgrupos;
	}

	public void setSubgrupos(Subgrupos subgrupos) {
		this.subgrupos = subgrupos;
	}
	/*@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "itemsMenuRoles")
	public Set<PermisosMenu> getPermisosMenus() {
		return this.permisosMenus;
	}

	public void setPermisosMenus(Set<PermisosMenu> permisosMenus) {
		this.permisosMenus = permisosMenus;
	}*/

	@Override
	public String toString() {
		return "id=" + id + ", roles=" + roles.getDescripcion() + ", grupos="
				+ grupos.getDescripcion() + ", itemMenu=" + itemMenu.getDescripcion() + ", subgrupos="
				+ subgrupos.getDescripcion();
	}

}
