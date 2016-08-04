package com.casapazmino.fulltime.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.NotNull;

/**
 * @author Drosan
 *
 */
@Entity
@Table(name = "contadores")
public class Contadores implements Serializable {
	
	private static final long serialVersionUID = -4868739315153327171L;
	
	Long id;
	String tabla;
	Long contador;

	public Contadores() {

	}

	@Id
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TABLA", nullable = false)
	@NotNull
	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	@Column(name = "CONTADOR", nullable = false)
	@NotNull
	public Long getContador() {
		return contador;
	}

	public void setContador(Long contador) {
		this.contador = contador;
	}
	
	@Override
	public String toString() {
		return "Id=" + id + ", Tabla="
				+ tabla + ", Contador=" + contador;
	}
}
