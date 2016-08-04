package com.casapazmino.fulltime.model;

import java.io.Serializable;
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

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "empleado_grupo")
public class EmpleadoGrupo implements Serializable {

	private static final long serialVersionUID = 2763171587767969337L;
	
	private Long emgrId;
	private String descripcion;
	private Set<Empleado> empleados = new HashSet<Empleado>(0);

	public EmpleadoGrupo() {
	}

	public EmpleadoGrupo(Long emgrId, String descripcion) {
		this.emgrId = emgrId;
		this.descripcion = descripcion;
	}
	public EmpleadoGrupo(Long emgrId, String descripcion, Set<Empleado> empleados) {
		this.emgrId = emgrId;
		this.descripcion = descripcion;
		this.empleados = empleados;
	}

	@Id
	@TableGenerator(name = "IDEMPLGRUPO", table = "contadores", pkColumnName = "ID", pkColumnValue = "55", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDEMPLGRUPO")
	@Column(name = "EMGR_ID", unique = true, nullable = false)
	public Long getEmgrId() {
		return this.emgrId;
	}

	public void setEmgrId(Long emgrId) {
		this.emgrId = emgrId;
	}

	@Column(name = "DESCRIPCION", nullable = false)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empleadoGrupo")
	public Set<Empleado> getEmpleados() {
		return this.empleados;
	}

	public void setEmpleados(Set<Empleado> empleados) {
		this.empleados = empleados;
	}

	@Override
	public String toString() {
		return "emgrId=" + emgrId + ", descripcion="
				+ descripcion;
	}

}
