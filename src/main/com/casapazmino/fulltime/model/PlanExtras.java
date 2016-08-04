package com.casapazmino.fulltime.model;

// Generated 29-April-2014 11:48:41 PM by Hibernate Tools 3.2.4.GA

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

import org.hibernate.validator.NotNull;

/**
 * TipoCargo generated by hbm2java
 */
@Entity
@Table(name = "plan_extras")
public class PlanExtras implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long paexId;	
	private Empleado empl_id_pla_ex;
	private Empleado empl_empl_id_pla_ex;
	private Integer mes;
	private Integer anio;
	private Integer total_horas_laborales;
	private Integer total_horas_suplementarias;
	private Integer total_horas_extraordinarias;
	
	private Integer autorizado;
	
	private Set<Funciones> funciones = new HashSet<Funciones>(0);

	public PlanExtras() {
	}

	/*public PlanExtra(int mes, int anio) {
		this.mes=mes;
		this.anio=anio;
	}*/

	public PlanExtras(Empleado empl_id_pla_ex,Integer mes, Integer anio, Integer total_horas_laborales, Integer total_horas_suplementarias, 
			Integer total_horas_extraordinarias,Set<Funciones> funciones, Empleado empl_empl_id_pla_ex, Integer autorizado) {
		this.empl_id_pla_ex=empl_id_pla_ex;
		this.mes=mes;
		this.anio=anio;
		this.total_horas_laborales=total_horas_laborales;
		this.total_horas_suplementarias=total_horas_suplementarias;
		this.total_horas_extraordinarias=total_horas_extraordinarias;
		this.funciones=funciones;
		this.empl_empl_id_pla_ex=empl_empl_id_pla_ex;
		this.autorizado=autorizado;
	}

	@Id
	@TableGenerator(name = "IDPLANEXTRAS", table = "contadores", pkColumnName = "ID", pkColumnValue = "44", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDPLANEXTRAS")
	@Column(name = "PAEX_ID", unique = true, nullable = false)
	public Long getPaexId() {
		return this.paexId;
	}

	public void setPaexId(Long paexId) {
		this.paexId = paexId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPL_ID_PLA_EX", nullable = false)
	@NotNull
	public Empleado getEmpleado() {
		if (empl_id_pla_ex == null){
			empl_id_pla_ex = new Empleado();
		}
		return empl_id_pla_ex;
	}

	public void setEmpleado(Empleado empleado) {
		this.empl_id_pla_ex = empleado;
	}

	@Column(name = "MES", nullable = true)	
	@NotNull
	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}
	
	@Column(name = "ANIO", nullable = false)
	@NotNull
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	@Column(name = "TOTAL_HORAS_LABORABLES", nullable = false)
	@NotNull
	public Integer getTotal_horas_laborales() {
		return total_horas_laborales;
	}

	public void setTotal_horas_laborales(Integer total_horas_laborales) {
		this.total_horas_laborales = total_horas_laborales;
	}
	
	@Column(name = "TOTAL_HORAS_SUPLEMENTARIAS", nullable = false)
	@NotNull
	public Integer getTotal_horas_suplementarias() {
		return total_horas_suplementarias;
	}

	public void setTotal_horas_suplementarias(Integer total_horas_suplementarias) {
		this.total_horas_suplementarias = total_horas_suplementarias;
	}

	@Column(name = "TOTAL_HORAS_EXTRAORDINARIAS", nullable = false)
	@NotNull
	public Integer getTotal_horas_extraordinarias() {
		return total_horas_extraordinarias;
	}

	public void setTotal_horas_extraordinarias(Integer total_horas_extraordinarias) {
		this.total_horas_extraordinarias = total_horas_extraordinarias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "paex_id")
	public Set<Funciones> getFunciones() {
		return funciones;
	}

	public void setFunciones(Set<Funciones> funciones) {
		this.funciones = funciones;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPL_EMPL_ID_PLA_EX", nullable = false)
	@NotNull
	public Empleado getEmpl_empl_id_pla_ex() {
		return empl_empl_id_pla_ex;
	}

	public void setEmpl_empl_id_pla_ex(Empleado empl_empl_id_pla_ex) {
		this.empl_empl_id_pla_ex = empl_empl_id_pla_ex;
	}

	@Column(name = "AUTORIZADO", nullable = false)
	@NotNull
	public Integer getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Integer autorizado) {
		this.autorizado = autorizado;
	}

}