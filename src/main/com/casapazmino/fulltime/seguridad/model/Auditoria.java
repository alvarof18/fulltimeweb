package com.casapazmino.fulltime.seguridad.model;
// Generated 23/11/2011 03:44:10 PM by Hibernate Tools 3.2.2.GA

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

/**
 * Auditoria generated by hbm2java
 */
@Entity
@Table(name = "auditoria")
public class Auditoria implements java.io.Serializable {

	private static final long serialVersionUID = -5845764042384355009L;
	
	private Integer audiId;
	private Usuarios usuarios;
	private String tabla;
	private String operacion;
	private Date fecha;
	private String cadenaAnterior;
	private String cadenaActual;
	private String ip;

	public Auditoria() {
	}

	public Auditoria(Usuarios usuarios, String tabla, String operacion,
			Date fecha, String cadenaAnterior) {
		this.usuarios = usuarios;
		this.tabla = tabla;
		this.operacion = operacion;
		this.fecha = fecha;
		this.cadenaAnterior = cadenaAnterior;
	}
	public Auditoria(Usuarios usuarios, String tabla, String operacion,
			Date fecha, String cadenaAnterior, String cadenaActual) {
		this.usuarios = usuarios;
		this.tabla = tabla;
		this.operacion = operacion;
		this.fecha = fecha;
		this.cadenaAnterior = cadenaAnterior;
		this.cadenaActual = cadenaActual;
	}

	@Id
	@TableGenerator(name = "IDAUDITORIA", table = "contadores", pkColumnName = "ID", pkColumnValue = "36", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDAUDITORIA")
	@Column(name = "AUDI_ID", unique = true, nullable = false)
	public Integer getAudiId() {
		return this.audiId;
	}

	public void setAudiId(Integer audiId) {
		this.audiId = audiId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID", nullable = false)
	@NotNull
	public Usuarios getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	@Column(name = "TABLA", nullable = false)
	@NotNull
	public String getTabla() {
		return this.tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	@Column(name = "OPERACION", nullable = false)
	@NotNull
	public String getOperacion() {
		return this.operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
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

	@Column(name = "CADENA_ANTERIOR", nullable = false)
	@NotNull
	public String getCadenaAnterior() {
		return this.cadenaAnterior;
	}

	public void setCadenaAnterior(String cadenaAnterior) {
		this.cadenaAnterior = cadenaAnterior;
	}

	@Column(name = "CADENA_ACTUAL")
	public String getCadenaActual() {
		return this.cadenaActual;
	}

	public void setCadenaActual(String cadenaActual) {
		this.cadenaActual = cadenaActual;
	}
	
	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	

	@Override
	public String toString() {
		return "audiId=" + audiId + ", usuarios=" + usuarios.getUsuario()
				+ ", tabla=" + tabla + ", operacion=" + operacion + ", fecha="
				+ fecha + ", cadenaAnterior=" + cadenaAnterior
				+ ", cadenaActual=" + cadenaActual;
	}
}
