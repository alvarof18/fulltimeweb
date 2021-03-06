package com.casapazmino.fulltime.model;
// Generated 23/11/2011 03:44:10 PM by Hibernate Tools 3.2.2.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import com.casapazmino.fulltime.comun.Enumeraciones;

/**
 * TipoPermiso generated by hbm2java
 */
@Entity
@Table(name = "tipo_permiso")
public class TipoPermiso implements java.io.Serializable {

	private static final long serialVersionUID = -7306992068276135239L;
	
	private Long tipeId;
	private String descripcion;
	private Enumeraciones.tipoDescuentoPermiso descuento;
	private Double factorHoras;
	private boolean afectaFechaVacacion;
	private int maximoDias;
	private boolean acumulaAnios;
	private int maximoHoras;
	private int diasJustificacion;
	private boolean correo;
	private boolean generaJust;
	private boolean validaFechas;
	private boolean timbra;
	private Integer accesoEmpleados;
	private Integer nivel;
	private Boolean crear;
	private Boolean actualizar;
	private Boolean eliminar;
	private Boolean preautorizar;
	private Boolean autorizar;
	private Boolean noautorizar;
	private Boolean legalizar;
	private Boolean incluirAlmuerzo;
	
	private Set<Permiso> permisos = new HashSet<Permiso>(0);
	
	private Set<DetalleTipoPermiso> detalleTipoPermiso = new HashSet<DetalleTipoPermiso>(0);


	public TipoPermiso() {
	}

	public TipoPermiso(String descripcion, Enumeraciones.tipoDescuentoPermiso descuento,
			Double factorHoras, boolean afectaFechaVacacion,
			int maximoDias, boolean acumulaAnios, int maximoHoras,
			int diasJustificacion, boolean correo, boolean generaJust, boolean timbra,
			Integer accesoEmpleados, boolean incluirAlmuerzo) {
		this.descripcion = descripcion;
		this.descuento = descuento;
		this.factorHoras = factorHoras;
		this.afectaFechaVacacion = afectaFechaVacacion;
		this.maximoDias = maximoDias;
		this.acumulaAnios = acumulaAnios;
		this.maximoHoras = maximoHoras;
		this.diasJustificacion = diasJustificacion;
		this.correo = correo;
		this.generaJust = generaJust;
		this.timbra = timbra;
		this.accesoEmpleados = accesoEmpleados;
		this.incluirAlmuerzo = incluirAlmuerzo;
	}

	public TipoPermiso(String descripcion, Enumeraciones.tipoDescuentoPermiso descuento,
			Double factorHoras, boolean afectaFechaVacacion,
			int maximoDias, boolean acumulaAnios, int maximoHoras,
			int diasJustificacion, boolean correo, boolean generaJust, boolean timbra, Integer accesoEmpleados,
			boolean incluirAlmuerzo, Set<Permiso> permisos) {
		this.descripcion = descripcion;
		this.descuento = descuento;
		this.factorHoras = factorHoras;
		this.afectaFechaVacacion = afectaFechaVacacion;
		this.maximoDias = maximoDias;
		this.acumulaAnios = acumulaAnios;
		this.maximoHoras = maximoHoras;
		this.diasJustificacion = diasJustificacion;
		this.correo = correo;
		this.generaJust = generaJust;
		this.timbra = timbra;
		this.permisos = permisos;
		this.accesoEmpleados = accesoEmpleados;
		this.incluirAlmuerzo = incluirAlmuerzo;
	}

	@Id
	@TableGenerator(name = "IDTIPPERMISO", table = "contadores", pkColumnName = "ID", pkColumnValue = "7", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDTIPPERMISO")
	@Column(name = "TIPE_ID", unique = true, nullable = false)
	public Long getTipeId() {
		return this.tipeId;
	}

	public void setTipeId(Long tipeId) {
		this.tipeId = tipeId;
	}

	@Column(name = "DESCRIPCION", nullable = false)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "DESCUENTO", nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	public Enumeraciones.tipoDescuentoPermiso getDescuento() {
		return this.descuento;
	}

	public void setDescuento(Enumeraciones.tipoDescuentoPermiso descuento) {
		this.descuento = descuento;
	}

	@Column(name = "FACTOR_HORAS", nullable = false, precision = 6, scale = 4)
	@NotNull
	public Double getFactorHoras() {
		return this.factorHoras;
	}

	public void setFactorHoras(Double factorHoras) {
		this.factorHoras = factorHoras;
	}

	@Column(name = "AFECTA_FECHA_VACACION", nullable = false)
	@NotNull
	public boolean isAfectaFechaVacacion() {
		return this.afectaFechaVacacion;
	}

	public void setAfectaFechaVacacion(boolean afectaFechaVacacion) {
		this.afectaFechaVacacion = afectaFechaVacacion;
	}

	@Column(name = "MAXIMO_DIAS", nullable = false)
	@NotNull
	public int getMaximoDias() {
		return this.maximoDias;
	}

	public void setMaximoDias(int maximoDias) {
		this.maximoDias = maximoDias;
	}

	@Column(name = "ACUMULA_ANIOS", nullable = false)
	@NotNull
	public boolean isAcumulaAnios() {
		return this.acumulaAnios;
	}

	public void setAcumulaAnios(boolean acumulaAnios) {
		this.acumulaAnios = acumulaAnios;
	}

	@Column(name = "MAXIMO_HORAS", nullable = false)
	@NotNull
	public int getMaximoHoras() {
		return this.maximoHoras;
	}

	public void setMaximoHoras(int maximoHoras) {
		this.maximoHoras = maximoHoras;
	}

	@Column(name = "DIAS_JUSTIFICACION", nullable = false)
	@NotNull
	public int getDiasJustificacion() {
		return this.diasJustificacion;
	}

	public void setDiasJustificacion(int diasJustificacion) {
		this.diasJustificacion = diasJustificacion;
	}

	@Column(name = "CORREO", nullable = false)
	@NotNull
	public boolean isCorreo() {
		return this.correo;
	}

	public void setCorreo(boolean correo) {
		this.correo = correo;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoPermiso")
	public Set<Permiso> getPermisos() {
		return this.permisos;
	}

	public void setPermisos(Set<Permiso> permisos) {
		this.permisos = permisos;
	}

	@Column(name = "GENERA_JUST", nullable = false)
	@NotNull
	public boolean isGeneraJust() {
		return this.generaJust;
	}

	public void setGeneraJust(boolean generaJust) {
		this.generaJust = generaJust;
	}

	@Column(name = "VALIDA_FECHAS", nullable = false)
	@NotNull
	public boolean isValidaFechas() {
		return validaFechas;
	}

	public void setValidaFechas(boolean validaFechas) {
		this.validaFechas = validaFechas;
	}
	
	@Column(name = "TIMBRA", nullable = false)
	@NotNull
	public boolean isTimbra() {
		return timbra;
	}

	public void setTimbra(boolean timbra) {
		this.timbra = timbra;
	}

	@Column(name = "ACCESO_EMPLEADOS", nullable = false)
	@NotNull
	public Integer getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(Integer accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	@Override
	public String toString() {
		return "tipeId=" + tipeId + ", descripcion=" + descripcion
				+ ", descuento=" + descuento + ", factorHoras=" + factorHoras
				+ ", afectaFechaVacacion=" + afectaFechaVacacion
				+ ", maximoDias=" + maximoDias + ", acumulaAnios="
				+ acumulaAnios + ", maximoHoras=" + maximoHoras
				+ ", diasJustificacion=" + diasJustificacion + ", correo="
				+ correo + ", generaJust=" + generaJust + ", validaFechas="
				+ validaFechas + ", timbra=" + timbra + ", accesoEmpleados="
				+ accesoEmpleados;
	}

	@Column(name = "NIVEL", nullable = false)
	@Min(1)
	@NotNull
	public Integer getNivel() {
		return nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	@Column(name = "CREAR")
	public Boolean getCrear() {
		return crear;
	}

	public void setCrear(Boolean crear) {
		this.crear = crear;
	}

	@Column(name = "ACTUALIZAR")
	public Boolean getActualizar() {
		return actualizar;
	}

	public void setActualizar(Boolean actualizar) {
		this.actualizar = actualizar;
	}

	@Column(name = "ELIMINAR")
	public Boolean getEliminar(){
		return eliminar;
	}

	public void setEliminar(Boolean eliminar) {
		this.eliminar = eliminar;
	}

	@Column(name = "PREAUTORIZAR")
	public Boolean getPreautorizar() {
		return preautorizar;
	}

	public void setPreautorizar(Boolean preautorizar) {
		this.preautorizar = preautorizar;
	}

	@Column(name = "AUTORIZAR")
	public Boolean getAutorizar() {
		return autorizar;
	}

	public void setAutorizar(Boolean autorizar) {
		this.autorizar = autorizar;
	}

	@Column(name = "NOAUTORIZAR")
	public Boolean getNoautorizar() {
		return noautorizar;
	}

	public void setNoautorizar(Boolean noautorizar) {
		this.noautorizar = noautorizar;
	}

	@Column(name = "LEGALIZAR")
	public Boolean getLegalizar() {
		return legalizar;
	}

	public void setLegalizar(Boolean legalizar) {
		this.legalizar = legalizar;
	}

	@Column(name = "INCLUIR_ALMUERZO")
	public Boolean getIncluirAlmuerzo() {
		return incluirAlmuerzo;
	}

	public void setIncluirAlmuerzo(Boolean incluirAlmuerzo) {
		this.incluirAlmuerzo = incluirAlmuerzo;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tipoPermiso")
	public Set<DetalleTipoPermiso> getDetalleTipoPermiso() {
		return detalleTipoPermiso;
	}

	public void setDetalleTipoPermiso(Set<DetalleTipoPermiso> detalleTipoPermiso) {
		this.detalleTipoPermiso = detalleTipoPermiso;
	}	

}