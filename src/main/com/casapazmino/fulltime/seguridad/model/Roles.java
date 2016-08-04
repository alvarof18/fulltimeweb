package com.casapazmino.fulltime.seguridad.model;
// Generated 10/01/2011 11:40:10 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.Length;

import com.sun.istack.internal.NotNull;

/**
 * Roles generated by hbm2java
 */
@Entity
@Table(name = "roles")
public class Roles implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String descripcion;
	private Integer usuarios;
	private Set<UsuariosRoles> usuariosRoleses = new HashSet<UsuariosRoles>(0);
	private Set<PermisosMenu> permisosMenus = new HashSet<PermisosMenu>(0);
	private Set<ItemsMenuRoles> itemsMenuRoleses = new HashSet<ItemsMenuRoles>(
			0);

	public Roles() {
	}

	public Roles(String descripcion, Integer usuarios,
			Set<UsuariosRoles> usuariosRoleses,
			Set<PermisosMenu> permisosMenus,
			Set<ItemsMenuRoles> itemsMenuRoleses) {
		this.descripcion = descripcion;
		this.usuarios = usuarios;
		this.usuariosRoleses = usuariosRoleses;
		this.permisosMenus = permisosMenus;
		this.itemsMenuRoleses = itemsMenuRoleses;
	}

	@Id
	@TableGenerator(name = "IDROLES", table = "contadores", pkColumnName = "ID", pkColumnValue = "33", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDROLES")
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "descripcion", length = 200)
	@Length(max = 200)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "usuarios")
	public Integer getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Integer usuarios) {
		this.usuarios = usuarios;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	public Set<UsuariosRoles> getUsuariosRoleses() {
		return this.usuariosRoleses;
	}

	public void setUsuariosRoleses(Set<UsuariosRoles> usuariosRoleses) {
		this.usuariosRoleses = usuariosRoleses;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roles", cascade=CascadeType.REMOVE)
	public Set<PermisosMenu> getPermisosMenus() {
		return this.permisosMenus;
	}

	public void setPermisosMenus(Set<PermisosMenu> permisosMenus) {
		this.permisosMenus = permisosMenus;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	public Set<ItemsMenuRoles> getItemsMenuRoleses() {
		return this.itemsMenuRoleses;
	}

	public void setItemsMenuRoleses(Set<ItemsMenuRoles> itemsMenuRoleses) {
		this.itemsMenuRoleses = itemsMenuRoleses;
	}

	@Override
	public String toString() {
		return "id=" + id + ", descripcion=" + descripcion;
	}

}
