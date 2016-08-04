package com.casapazmino.fulltime.model;

// Generated 12-Jan-2011 12:14:41 PM by Hibernate Tools 3.2.4.GA

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

/**
 * Feriado generated by hbm2java
 */
@Entity
@Table(name = "feriado")
public class Feriado implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long feriId;
	private Date fecha;
	private String descripcion;
	private Set<CiudadFeriado> ciudadFeriados = new HashSet<CiudadFeriado>(0);
	
	private Date fechaRecupera;

	public Feriado() {
	}

	public Feriado(Date fecha, String descripcion) {
		this.fecha = fecha;
		this.descripcion = descripcion;
	}

	public Feriado(Date fecha,
			String descripcion, Set<CiudadFeriado> ciudadFeriados) {
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.ciudadFeriados = ciudadFeriados;
	}

	@Id
	@TableGenerator(name = "IDFERIADO", table = "contadores", pkColumnName = "ID", pkColumnValue = "28", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDFERIADO")
	@Column(name = "FERI_ID", unique = true, nullable = false)
	public Long getFeriId() {
		return this.feriId;
	}

	public void setFeriId(Long feriId) {
		this.feriId = feriId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA", nullable = false, length = 19)
	@NotNull
	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	@Column(name = "DESCRIPCION", nullable = false)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "feriado")
	public Set<CiudadFeriado> getCiudadFeriados() {
		return this.ciudadFeriados;
	}

	public void setCiudadFeriados(Set<CiudadFeriado> ciudadFeriados) {
		this.ciudadFeriados = ciudadFeriados;
	}

	@Override
	public String toString() {
		return "feriId=" + feriId + ", fecha=" + fecha
				+ ", descripcion=" + descripcion;
	}

	@Column(name = "FECHA_RECUPERA", nullable = true)
	public Date getFechaRecupera() {
		return fechaRecupera;
	}

	public void setFechaRecupera(Date fechaRecupera) {
		this.fechaRecupera = fechaRecupera;
	}
	
	
}
