package com.casapazmino.fulltime.seguridad.model;
// Generated 10/01/2011 11:40:10 PM by Hibernate Tools 3.2.2.GA

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.NotNull;

import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;

/**
 * Usuarios generated by hbm2java
 */
@Entity
@Table(name = "usuarios")
public class Usuarios implements java.io.Serializable {

	private static final long serialVersionUID = 5973099710064821467L;
	
	private Long id;
	private DetalleTipoParametro estado;
	private String usuario;
	private String contrasena;
	private Date fechaCreacion;
	private Date fechaVencimiento;
	private Set<UsuarioCiudad> usuarioCiudads = new HashSet<UsuarioCiudad>(0);
//	private Set<Empleado> empleados = new HashSet<Empleado>(0);
	private Set<Auditoria> auditorias = new HashSet<Auditoria>(0);
	private Set<UsuariosRoles> usuariosRoleses = new HashSet<UsuariosRoles>(0);
	
	private Empleado empleado;

	public Usuarios() {
	}

	public Usuarios(DetalleTipoParametro estado, String usuario,
			String contrasena, Date fechaCreacion, Date fechaVencimiento) {
		this.estado = estado;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.fechaCreacion = fechaCreacion;
		this.fechaVencimiento = fechaVencimiento;
	}
	public Usuarios(DetalleTipoParametro estado, String usuario,
			String contrasena, Date fechaCreacion, Date fechaVencimiento,
			Set<UsuarioCiudad> usuarioCiudads, Empleado empleado,
			Set<Auditoria> auditorias, Set<UsuariosRoles> usuariosRoleses) {
		this.estado = estado;
		this.usuario = usuario;
		this.contrasena = contrasena;
		this.fechaCreacion = fechaCreacion;
		this.fechaVencimiento = fechaVencimiento;
		this.usuarioCiudads = usuarioCiudads;
		this.empleado = empleado;
		this.auditorias = auditorias;
		this.usuariosRoleses = usuariosRoleses;
	}

	@Id
	@TableGenerator(name = "IDUSUARIO", table = "contadores", pkColumnName = "ID", pkColumnValue = "19", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDUSUARIO")
	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "USUARIO", nullable = false)
	@NotNull
	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(name = "CONTRASENA")
	public String getContrasena() {
		return this.contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_CREACION", nullable = false, length = 10)
	@NotNull
	public Date getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_VENCIMIENTO", length = 10)
	public Date getFechaVencimiento() {
		return this.fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarios")
	public Set<UsuarioCiudad> getUsuarioCiudads() {
		return this.usuarioCiudads;
	}

	public void setUsuarioCiudads(Set<UsuarioCiudad> usuarioCiudads) {
		this.usuarioCiudads = usuarioCiudads;
	}
	
/*	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarios")
	public Set<Empleado> getEmpleados() {
		return this.empleados;
	}

	public void setEmpleados(Set<Empleado> empleados) {
		this.empleados = empleados;
	}*/
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarios")
	public Set<Auditoria> getAuditorias() {
		return this.auditorias;
	}

	public void setAuditorias(Set<Auditoria> auditorias) {
		this.auditorias = auditorias;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "usuarios", cascade=CascadeType.REMOVE)
	public Set<UsuariosRoles> getUsuariosRoleses() {
		return this.usuariosRoleses;
	}

	public void setUsuariosRoleses(Set<UsuariosRoles> usuariosRoleses) {
		this.usuariosRoleses = usuariosRoleses;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ESTADO")
	public DetalleTipoParametro getEstado() {
		if (estado == null){
			estado = new DetalleTipoParametro();
		}
		return estado;
	}

	public void setEstado(DetalleTipoParametro estado) {
		this.estado = estado;
	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "usuarios")
	public Empleado getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	@Override
	public String toString() {
		return "id=" + id + ", usuario="
				+ usuario + ", contrasena=" + contrasena + ", fechaCreacion="
				+ fechaCreacion + ", fechaVencimiento=" + fechaVencimiento;
	}

}
