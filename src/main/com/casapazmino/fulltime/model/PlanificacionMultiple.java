package com.casapazmino.fulltime.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "planificacion_multiple")
public class PlanificacionMultiple implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idPlanificacionMultiple;
	private Long idEmpleado;
	private Integer mes;
	private Integer anio;
	private Integer	estado;

	public PlanificacionMultiple() {
	}

	public PlanificacionMultiple(Long idPlanificacionMultiple, Long idEmpleado, Integer mes, Integer anio, Integer	estado) {
		this.idPlanificacionMultiple=idPlanificacionMultiple;
		this.idEmpleado=idEmpleado;
		this.mes=mes;
		this.anio=anio;
		this.estado=estado;
	}
	
	@Id
	@TableGenerator(name = "IDPLAMUL", table = "contadores", pkColumnName = "ID", pkColumnValue = "49", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDPLAMUL")
	@Column(name = "ID_PLA_MUL", unique = true, nullable = false)
	public Long getIdPlanificacionMultiple() {
		return idPlanificacionMultiple;
	}

	public void setIdPlanificacionMultiple(Long idPlanificacionMultiple) {
		this.idPlanificacionMultiple = idPlanificacionMultiple;
	}

	@Column(name = "EMPL_ID", nullable = false)
	@NotNull
	public Long getIdEmpleado() {
		return idEmpleado;
	}
	
	public void setIdEmpleado(Long empl_id) {
		this.idEmpleado = empl_id;
	}

	@Column(name = "MES", nullable = false)
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

	@Column(name = "ESTADO", nullable = false)
	@NotNull
	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	
}
