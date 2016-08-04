package com.casapazmino.fulltime.model;
// Generated 10/01/2011 11:40:10 PM by Hibernate Tools 3.2.2.GA

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.validator.Max;
import org.hibernate.validator.Min;
import org.hibernate.validator.NotNull;

import com.casapazmino.fulltime.comun.Enumeraciones;

/**
 * Atraso generated by hbm2java
 */
@Entity
@Table(name = "atraso")
public class Atraso implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long atraId;
	private Integer numeroDesde;
	private Integer numeroHasta;
	private Enumeraciones.tipoAtraso tipoAtraso;
	private Enumeraciones.tipoDescuento tipoDescuento;
	private BigDecimal porcentaje;

	public Atraso() {
	}

	public Atraso(int numeroDesde, int numeroHasta, BigDecimal porcentaje,
			Enumeraciones.tipoAtraso tipoAtraso, Enumeraciones.tipoDescuento tipoDescuento) {
		this.numeroDesde = numeroDesde;
		this.numeroHasta = numeroHasta;
		this.porcentaje = porcentaje;
		this.tipoAtraso = tipoAtraso;
		this.tipoDescuento = tipoDescuento;
	}

	@Id
	@TableGenerator(name = "IDATRASO", table = "contadores", pkColumnName = "ID", pkColumnValue = "5", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDATRASO")
	@Column(name = "ATRA_ID", unique = true, nullable = false)
	public Long getAtraId() {
		return this.atraId;
	}

	public void setAtraId(Long atraId) {
		this.atraId = atraId;
	}

	@Column(name = "NUMERO_DESDE")
	public Integer getNumeroDesde() {
		return this.numeroDesde;
	}

	public void setNumeroDesde(Integer numeroDesde) {
		this.numeroDesde = numeroDesde;
	}

	@Column(name = "NUMERO_HASTA")
	public Integer getNumeroHasta() {
		return this.numeroHasta;
	}

	public void setNumeroHasta(Integer numeroHasta) {
		this.numeroHasta = numeroHasta;
	}

	@Column(name = "PORCENTAJE", precision = 6, scale = 2)
	@Max(100)
	@Min(0)
	public BigDecimal getPorcentaje() {
		return this.porcentaje;
	}

	public void setPorcentaje(BigDecimal porcentaje) {
		this.porcentaje = porcentaje;
	}

	@Column(name = "TIPO_ATRASO", nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	public Enumeraciones.tipoAtraso getTipoAtraso() {
		return tipoAtraso;
	}

	public void setTipoAtraso(Enumeraciones.tipoAtraso tipoAtraso) {
		this.tipoAtraso = tipoAtraso;
	}

	@Column(name = "TIPO_DESCUENTO", nullable = false)
	@Enumerated(EnumType.STRING)
	public Enumeraciones.tipoDescuento getTipoDescuento() {
		return tipoDescuento;
	}

	public void setTipoDescuento(Enumeraciones.tipoDescuento tipoDescuento) {
		this.tipoDescuento = tipoDescuento;
	}

	@Override
	public String toString() {
		return "atraId=" + atraId + ", numeroDesde=" + numeroDesde
				+ ", numeroHasta=" + numeroHasta + ", tipoAtraso=" + tipoAtraso
				+ ", tipoDescuento=" + tipoDescuento + ", porcentaje="
				+ porcentaje;
	}
	
	

}