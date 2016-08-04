package com.casapazmino.fulltime.model;

import java.io.Serializable;
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

import org.hibernate.validator.NotNull;

@Entity
@Table(name = "empleado_horario")
public class EmpleadoHorario implements Serializable {

	private static final long serialVersionUID = -7455180694545558827L;

	private Long emhoId;
	private Empleado empleado;
	private Horario horario;
	private Date fechaIni;
	private Date fechaFin;
	private int diasPlanificar;
	private int diasFrecuencia;
	private boolean lunes;
	private boolean martes;
	private boolean miercoles; 
	private boolean jueves;
	private boolean viernes;
	private boolean sabado;
	private boolean domingo;
	
	public EmpleadoHorario() {
	}

	public EmpleadoHorario(Long emhoId, Horario horario, Empleado empleado,
			Date fechaIni, Date fechaFin, int diasPlanificar, int diasFrecuencia) {
		this.emhoId = emhoId;
		this.horario = horario;
		this.empleado = empleado;
		this.fechaIni = fechaIni;
		this.fechaFin = fechaFin;
		this.diasPlanificar = diasPlanificar;
		this.diasFrecuencia = diasFrecuencia;
	}

	@Id
	@TableGenerator(name = "IDEMPLGRUPO", table = "contadores", pkColumnName = "ID", pkColumnValue = "54", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDEMPLGRUPO")
	@Column(name = "EMHO_ID", unique = true, nullable = false)
	public Long getEmhoId() {
		return this.emhoId;
	}

	public void setEmhoId(Long emhoId) {
		this.emhoId = emhoId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HORA_ID", nullable = false)
	@NotNull
	public Horario getHorario() {
		return this.horario;
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPL_ID", nullable = false)
	@NotNull
	public Empleado getEmpleado() {
		if(this.empleado == null){
			this.empleado = new Empleado();
		}
		return this.empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_INI", nullable = false, length = 10)
	@NotNull
	public Date getFechaIni() {
		return this.fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_FIN", nullable = false, length = 10)
	@NotNull
	public Date getFechaFin() {
		return this.fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	@Column(name = "DIAS_PLANIFICAR", nullable = false)
	@NotNull
	public int getDiasPlanificar() {
		return this.diasPlanificar;
	}

	public void setDiasPlanificar(int diasPlanificar) {
		this.diasPlanificar = diasPlanificar;
	}

	@Column(name = "DIAS_FRECUENCIA")
	public int getDiasFrecuencia() {
		return this.diasFrecuencia;
	}

	public void setDiasFrecuencia(int diasFrecuencia) {
		this.diasFrecuencia = diasFrecuencia;
	}

	@Column(name = "LUNES", nullable = false)
	@NotNull
	public boolean isLunes() {
		return lunes;
	}

	public void setLunes(boolean lunes) {
		this.lunes = lunes;
	}

	@Column(name = "MARTES", nullable = false)
	@NotNull
	public boolean isMartes() {
		return martes;
	}

	public void setMartes(boolean martes) {
		this.martes = martes;
	}

	@Column(name = "MIERCOLES", nullable = false)
	@NotNull
	public boolean isMiercoles() {
		return miercoles;
	}

	public void setMiercoles(boolean miercoles) {
		this.miercoles = miercoles;
	}

	@Column(name = "JUEVES", nullable = false)
	@NotNull
	public boolean isJueves() {
		return jueves;
	}

	public void setJueves(boolean jueves) {
		this.jueves = jueves;
	}

	@Column(name = "VIERNES", nullable = false)
	@NotNull
	public boolean isViernes() {
		return viernes;
	}

	public void setViernes(boolean viernes) {
		this.viernes = viernes;
	}

	@Column(name = "SABADO", nullable = false)
	@NotNull
	public boolean isSabado() {
		return sabado;
	}

	public void setSabado(boolean sabado) {
		this.sabado = sabado;
	}

	@Column(name = "DOMINGO", nullable = false)
	@NotNull
	public boolean isDomingo() {
		return domingo;
	}

	public void setDomingo(boolean domingo) {
		this.domingo = domingo;
	}
	
	@Override
	public String toString() {
		return "emhoId=" + emhoId + ", fechaIni=" + fechaIni
				+ ", fechaFin=" + fechaFin + ", diasPlanificar="
				+ diasPlanificar;
	}
}
