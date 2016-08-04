package com.casapazmino.fulltime.model;

// Generated 12-Jan-2011 12:14:41 PM by Hibernate Tools 3.2.4.GA

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
 * Departamento generated by hbm2java
 */
@Entity
@Table(name = "departamento_autorizacion")
public class DepartamentoAutorizacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long dep_aut_id;
	private Departamento departamento;
	private Empleado empleado;

	public DepartamentoAutorizacion() {	
	}

	public DepartamentoAutorizacion(Departamento departamento,Empleado empleado) {
		this.departamento = departamento;
		this.empleado = empleado;
	}

	@Id
	@TableGenerator(name = "IDDEPAUT", table = "contadores", pkColumnName = "ID", pkColumnValue = "46", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDDEPAUT")
	@Column(name = "DEP_AUT_ID", unique = true, nullable = false)
	public Long getDepaId() {
		return this.dep_aut_id;
	}

	public void setDepaId(Long dep_aut_id) {
		this.dep_aut_id = dep_aut_id;
	}
		
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPA_ID", nullable = false)
	@NotNull
	public Departamento getDepartamento() {
		if (this.departamento == null){
			this.departamento = new Departamento();	
		}
		return this.departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPL_ID")
	public Empleado getEmpleado() {
		if(this.empleado==null){
			this.empleado=new Empleado();
		}
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}
}
