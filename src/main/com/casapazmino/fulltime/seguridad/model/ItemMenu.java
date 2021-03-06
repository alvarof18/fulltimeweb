package com.casapazmino.fulltime.seguridad.model;
// Generated 10/01/2011 11:40:10 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * ItemMenu generated by hbm2java
 */
@Entity
@Table(name = "item_menu")
public class ItemMenu implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Grupos grupos;
	private Subgrupos subgrupos;
	private String nombre;
	private String descripcion;
	private String link;
	private Boolean expandible;
	private Integer orden;
	private String etiqueta;
	private Set<ItemsMenuRoles> itemsMenuRoleses = new HashSet<ItemsMenuRoles>(
			0);

	public ItemMenu() {
		this.grupos=new Grupos();
		this.subgrupos=new Subgrupos();
	}

	public ItemMenu(Grupos grupos, Subgrupos subgrupos) {
		this.grupos = grupos;
		this.subgrupos = subgrupos;
	}
	public ItemMenu(Grupos grupos, Subgrupos subgrupos, String nombre,
			String descripcion, String link, Boolean expandible, Integer orden,
			String etiqueta, Set<ItemsMenuRoles> itemsMenuRoleses) {
		this.grupos = grupos;
		this.subgrupos = subgrupos;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.link = link;
		this.expandible = expandible;
		this.orden = orden;
		this.etiqueta = etiqueta;
		this.itemsMenuRoleses = itemsMenuRoleses;
	}

	@Id
	@TableGenerator(name = "IDITEMMENU", table = "contadores", pkColumnName = "ID", pkColumnValue = "38", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDITEMMENU")
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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
	@JoinColumn(name = "subgrupos", nullable = false)
	@NotNull
	public Subgrupos getSubgrupos() {
		return this.subgrupos;
	}

	public void setSubgrupos(Subgrupos subgrupos) {
		this.subgrupos = subgrupos;
	}

	@Column(name = "nombre", length = 50,nullable = false)
	@Length(max = 50)
	@NotNull
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "descripcion", length = 100, nullable = false)
	@Length(max = 100)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "link", length = 300, nullable = false)
	@Length(max = 300)
	@NotNull
	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Column(name = "expandible")
	public Boolean getExpandible() {
		return this.expandible;
	}

	public void setExpandible(Boolean expandible) {
		this.expandible = expandible;
	}

	@Column(name = "orden")
	public Integer getOrden() {
		return this.orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	@Column(name = "etiqueta", length = 30, nullable = false)
	@Length(max = 30)
	@NotNull
	public String getEtiqueta() {
		return this.etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "itemMenu")
	public Set<ItemsMenuRoles> getItemsMenuRoleses() {
		return this.itemsMenuRoleses;
	}

	public void setItemsMenuRoleses(Set<ItemsMenuRoles> itemsMenuRoleses) {
		this.itemsMenuRoleses = itemsMenuRoleses;
	}

	@Override
	public String toString() {
		return "id=" + id + ", grupos=" + grupos.getDescripcion() + ", subgrupos="
				+ subgrupos.getDescripcion() + ", nombre=" + nombre + ", descripcion="
				+ descripcion + ", link=" + link + ", expandible=" + expandible
				+ ", orden=" + orden + ", etiqueta=" + etiqueta;
	}

	
}
