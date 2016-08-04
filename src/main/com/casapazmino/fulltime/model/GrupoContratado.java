package com.casapazmino.fulltime.model;

// Generated 12-Jan-2011 12:14:41 PM by Hibernate Tools 3.2.4.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.NotNull;

/**
 * GrupoContratado generated by hbm2java
 */
@Entity
@Table(name = "grupo_contratado")
public class GrupoContratado implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long grcoId;
	private String descripcion;
	private Set<DetalleGrupoContratado> detalleGrupoContratados = new HashSet<DetalleGrupoContratado>(
			0);

	public GrupoContratado() {
	}

	public GrupoContratado(String descripcion) {
		this.descripcion = descripcion;
	}

	public GrupoContratado(String descripcion,
			Set<DetalleGrupoContratado> detalleGrupoContratados) {
		this.descripcion = descripcion;
		this.detalleGrupoContratados = detalleGrupoContratados;
	}

	@Id
	@TableGenerator(name = "IDGRUPOCONTRATO", table = "contadores", pkColumnName = "ID", pkColumnValue = "16", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDGRUPOCONTRATO")
	@Column(name = "GRCO_ID", unique = true, nullable = false)
	public Long getGrcoId() {
		return this.grcoId;
	}

	public void setGrcoId(Long grcoId) {
		this.grcoId = grcoId;
	}

	@Column(name = "DESCRIPCION", nullable = false)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grupoContratado")
	public Set<DetalleGrupoContratado> getDetalleGrupoContratados() {
		return this.detalleGrupoContratados;
	}

	public void setDetalleGrupoContratados(
			Set<DetalleGrupoContratado> detalleGrupoContratados) {
		this.detalleGrupoContratados = detalleGrupoContratados;
	}

	@Override
	public String toString() {
		return "grcoId=" + grcoId + ", descripcion="
				+ descripcion;
	}

}
