package com.casapazmino.fulltime.model;

// Generated 12-Jan-2011 12:14:41 PM by Hibernate Tools 3.2.4.GA

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

/**
 * ProgramaVacacion generated by hbm2java
 */
@Entity
@Table(name = "programa_vacacion")
public class ProgramaVacacion implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long prvaId;
	private DetalleTipoParametro detalleTipoParametro;
	private Empleado empleado;
	private Date fechaInicio;
	private Date fechaFin;
	private Integer periodo;
	private Integer generado;
	private Date fechaInicio1;
	private Date fechaFin1;
	private Date fechaInicio2;
	private Date fechaFin2;
	private Date fechaInicio3;
	private Date fechaFin3;
	private Integer dias_vacacion;
	
	private Integer numero;

	public ProgramaVacacion() {
	}

	public ProgramaVacacion(Empleado empleado, Date fechaInicio, Date fechaFin, Integer periodo, Integer generado, Date fechaInicio1, Date fechaFin1
			, Date fechaInicio2, Date fechaFin2, Date fechaInicio3, Date fechaFin3, Integer dias_vacacion) {
		this.empleado = empleado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.periodo=periodo;
		this.generado=generado;
		this.fechaInicio1 = fechaInicio1;
		this.fechaFin1 = fechaFin1;
		this.fechaInicio2 = fechaInicio2;
		this.fechaFin2 = fechaFin2;
		this.fechaInicio3 = fechaInicio3;
		this.fechaFin3 = fechaFin3;
		this.dias_vacacion=dias_vacacion;
	}
	public ProgramaVacacion(DetalleTipoParametro detalleTipoParametro,
			Empleado empleado, Date fechaInicio, Date fechaFin, Integer periodo, Integer generado
			, Date fechaInicio1, Date fechaFin1
			, Date fechaInicio2, Date fechaFin2, Date fechaInicio3, Date fechaFin3, Integer dias_vacacion) {
		this.detalleTipoParametro = detalleTipoParametro;
		this.empleado = empleado;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.periodo=periodo;
		this.generado=generado;
		this.fechaInicio1 = fechaInicio1;
		this.fechaFin1 = fechaFin1;
		this.fechaInicio2 = fechaInicio2;
		this.fechaFin2 = fechaFin2;
		this.fechaInicio3 = fechaInicio3;
		this.fechaFin3 = fechaFin3;
		this.dias_vacacion=dias_vacacion;
	}

	@Id
	@TableGenerator(name = "IDPROVACACION", table = "contadores", pkColumnName = "ID", pkColumnValue = "30", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDPROVACACION")
	@Column(name = "PRVA_ID", unique = true, nullable = false)
	public Long getPrvaId() {
		return this.prvaId;
	}

	public void setPrvaId(Long prvaId) {
		this.prvaId = prvaId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DTPA_ID")
	public DetalleTipoParametro getDetalleTipoParametro() {
		return this.detalleTipoParametro;
	}

	public void setDetalleTipoParametro(
			DetalleTipoParametro detalleTipoParametro) {
		this.detalleTipoParametro = detalleTipoParametro;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPL_ID")
	public Empleado getEmpleado() {
		if(empleado == null) {
			empleado = new Empleado();
		}
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_INICIO", length = 19)
	public Date getFechaInicio() {
		return this.fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_FIN", length = 19)
	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	@Column(name = "PERIODO")
	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
	}

	@Column(name = "GENERADO")
	public Integer getGenerado() {
		return generado;
	}

	public void setGenerado(Integer generado) {
		this.generado = generado;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_INICIO1", length = 19)
	public Date getFechaInicio1() {
		return fechaInicio1;
	}

	public void setFechaInicio1(Date fechaInicio1) {
		this.fechaInicio1 = fechaInicio1;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_FIN1", length = 19)
	public Date getFechaFin1() {
		return fechaFin1;
	}

	public void setFechaFin1(Date fechaFin1) {
		this.fechaFin1 = fechaFin1;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_INICIO2", length = 19)
	public Date getFechaInicio2() {
		return fechaInicio2;
	}

	public void setFechaInicio2(Date fechaInicio2) {
		this.fechaInicio2 = fechaInicio2;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_FIN2", length = 19)
	public Date getFechaFin2() {
		return fechaFin2;
	}

	public void setFechaFin2(Date fechaFin2) {
		this.fechaFin2 = fechaFin2;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_INICIO3", length = 19)
	public Date getFechaInicio3() {
		return fechaInicio3;
	}

	public void setFechaInicio3(Date fechaInicio3) {
		this.fechaInicio3 = fechaInicio3;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_FIN3", length = 19)
	public Date getFechaFin3() {
		return fechaFin3;
	}

	public void setFechaFin3(Date fechaFin3) {
		this.fechaFin3 = fechaFin3;
	}

	public Integer getDias_vacacion() {
		return dias_vacacion;
	}

	@Column(name = "DIAS_VACACION")
	public void setDias_vacacion(Integer dias_vacacion) {
		this.dias_vacacion = dias_vacacion;
	}

	@Column(name = "NUMERO", nullable=true)
	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "ProgramaVacacion [prvaId=" + prvaId + ", detalleTipoParametro=" + detalleTipoParametro + ", empleado="
				+ empleado + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", periodo=" + periodo
				+ ", generado=" + generado + ", fechaInicio1=" + fechaInicio1 + ", fechaFin1=" + fechaFin1
				+ ", fechaInicio2=" + fechaInicio2 + ", fechaFin2=" + fechaFin2 + ", fechaInicio3=" + fechaInicio3
				+ ", fechaFin3=" + fechaFin3 + ", dias_vacacion=" + dias_vacacion + ", numero=" + numero + "]";
	}
	
	

}
