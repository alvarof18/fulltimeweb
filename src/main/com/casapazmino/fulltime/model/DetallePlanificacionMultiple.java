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
@Table(name = "detalle_planificacion_multiple")
public class DetallePlanificacionMultiple implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long idDetallePlanificacionMultiple;
	private Long idPlanificacionMultiple;
	private Integer idHorario;
	private Integer	dia;
	private Integer	estado;
	private String codigoHorario;

	public DetallePlanificacionMultiple() {
	}

	public DetallePlanificacionMultiple(Long idDetallePlanificacionMultiple, Long idPlanificacionMultiple, Integer idHorario, Integer dia, Integer estado) {
		this.idDetallePlanificacionMultiple=idDetallePlanificacionMultiple;
		this.idPlanificacionMultiple=idPlanificacionMultiple;
		this.idHorario=idHorario;
		this.dia=dia;
		this.estado=estado;
	}
	
	@Id
	@TableGenerator(name = "IDDETPLAMUL", table = "contadores", pkColumnName = "ID", pkColumnValue = "50", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDDETPLAMUL")
	@Column(name = "ID_DET_PLA_MUL", unique = true, nullable = false)
	public Long getIdDetallePlanificacionMultiple() {
		return idDetallePlanificacionMultiple;
	}

	public void setIdDetallePlanificacionMultiple(
			Long idDetallePlanificacionMultiple) {
		this.idDetallePlanificacionMultiple = idDetallePlanificacionMultiple;
	}	

	@Column(name = "ID_PLA_MUL", nullable = false)
	@NotNull
	public Long getIdPlanificacionMultiple() {
		return idPlanificacionMultiple;
	}

	public void setIdPlanificacionMultiple(Long idPlanificacionMultiple) {
		this.idPlanificacionMultiple = idPlanificacionMultiple;
	}

	@Column(name = "HORA_ID", nullable = false)
	@NotNull
	public Integer getIdHorario() {
		return idHorario;
	}

	public void setIdHorario(Integer idHorario) {
		this.idHorario = idHorario;
	}

	@Column(name = "DIA", nullable = false)
	@NotNull
	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
	}

	@Column(name = "ESTADO", nullable = false)
	@NotNull
	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	@Column(name = "CODIGO_HORARIO", nullable = false)
	@NotNull
	public String getCodigoHorario() {
		return codigoHorario;
	}
	
	public void setCodigoHorario(String codigoHorario) {
		this.codigoHorario = codigoHorario;
	}	
}
