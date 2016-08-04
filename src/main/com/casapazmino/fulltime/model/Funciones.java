package com.casapazmino.fulltime.model;

// Generated 29-April-2014 11:48:41 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.Length;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

/**
 * TipoCargo generated by hbm2java
 */
@Entity
@Table(name = "funciones")
public class Funciones implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long func_id;
	private PlanExtras paex_id;
	private String func_resp;
	private String actividad;
	private Integer horas_laborales;
	private Integer horas_suplementarias;
	private Integer horas_extraordinarias;
	private String justificacion;
	private Date fecha;
	private Date desde;
	private Date hasta;

	public Funciones() {

	}

	public Funciones(PlanExtras paex_id, String func_resp, String actividad,
			Integer horas_laborales, Integer horas_suplementarias,
			Integer horas_extraordinarias, String justificacion, Date fecha,
			Date desde, Date hasta) {
		this.paex_id = paex_id;
		this.func_resp = func_resp;
		this.actividad = actividad;
		this.horas_laborales = horas_laborales;
		this.horas_suplementarias = horas_suplementarias;
		this.horas_extraordinarias = horas_extraordinarias;
		this.justificacion = justificacion;
		this.fecha = fecha;
		this.setDesde(desde);
		this.setHasta(hasta);
	}

	@Id
	@TableGenerator(name = "IDFUNCIONES", table = "contadores", pkColumnName = "ID", pkColumnValue = "45", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDFUNCIONES")
	@Column(name = "FUNC_ID", unique = true, nullable = false)
	public Long getFunc_id() {
		return func_id;
	}

	public void setFunc_id(Long func_id) {
		this.func_id = func_id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAEX_ID", nullable = false)
	@NotNull
	public PlanExtras getPaex_id() {
		return paex_id;
	}

	public void setPaex_id(PlanExtras paex_id) {
		this.paex_id = paex_id;
	}

	@Column(name = "FUNC_RESP", nullable = false)
	@NotNull
	@Length(max=150)
	public String getFunc_resp(){
		return func_resp;
	}

	public void setFunc_resp(String func_resp) {
		this.func_resp = func_resp;
	}

	@Column(name = "HORAS_LABORABLES", nullable = false)
	@NotNull
	@Min(0)
	public Integer getHoras_laborales() {
		return horas_laborales;
	}

	public void setHoras_laborales(Integer horas_laborales) {
		this.horas_laborales = horas_laborales;
	}

	@Column(name = "ACTIVIDAD", nullable = false)
	@NotNull
	@Length(max=150)
	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	@Column(name = "HORAS_SUPLEMENTARIAS", nullable = false)
	@NotNull
	@Min(0)
	public Integer getHoras_suplementarias() {
		return horas_suplementarias;
	}

	public void setHoras_suplementarias(Integer horas_suplementarias) {
		this.horas_suplementarias = horas_suplementarias;
	}

	@Column(name = "HORAS_EXTRAORDINARIAS", nullable = false)
	@NotNull
	@Min(0)
	public Integer getHoras_extraordinarias() {
		return horas_extraordinarias;
	}

	public void setHoras_extraordinarias(Integer horas_extraordinarias) {
		this.horas_extraordinarias = horas_extraordinarias;
	}

	@Column(name = "JUSTIFICACION", nullable = false)
	@NotNull
	@Length(max=150)
	public String getJustificacion() {
		return justificacion;
	}

	public void setJustificacion(String justificacion) {
		this.justificacion = justificacion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA", length = 19)
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DESDE", length = 19)
	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "HASTA", length = 19)
	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

}