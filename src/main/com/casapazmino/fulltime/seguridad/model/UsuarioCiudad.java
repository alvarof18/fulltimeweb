package com.casapazmino.fulltime.seguridad.model;

// Generated 18/04/2012 05:38:12 PM by Hibernate Tools 3.4.0.CR1

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

import org.hibernate.validator.NotNull;

import com.casapazmino.fulltime.model.Ciudad;

/**
 * UsuarioCiudad generated by hbm2java
 */
@Entity
@Table(name = "usuario_ciudad")
public class UsuarioCiudad implements java.io.Serializable {

	private static final long serialVersionUID = 4307716333216435698L;

	private Integer usuaCiudId;
	private Usuarios usuarios;
	private Ciudad ciudad;

	public UsuarioCiudad() {
	}

	public UsuarioCiudad(Usuarios usuarios, Ciudad ciudad) {
		this.usuarios = usuarios;
		this.ciudad = ciudad;
	}

	@Id
	@TableGenerator(name = "IDUSERCIUDAD", table = "contadores", pkColumnName = "ID", pkColumnValue = "35", valueColumnName = "CONTADOR", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "IDUSERCIUDAD")
	@Column(name = "USUA_CIUD_ID", unique = true, nullable = false)
	public Integer getUsuaCiudId() {
		return this.usuaCiudId;
	}

	public void setUsuaCiudId(Integer usuaCiudId) {
		this.usuaCiudId = usuaCiudId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID", nullable = false)
	@NotNull
	public Usuarios getUsuarios() {
		if(usuarios == null){
			usuarios =  new Usuarios();
		}
		return this.usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CIUD_ID", nullable = false)
	@NotNull
	public Ciudad getCiudad() {
		if(ciudad ==  null){
			ciudad = new Ciudad();
		}
		return this.ciudad;
	}

	public void setCiudad(Ciudad ciudad) {
		this.ciudad = ciudad;
	}

	@Override
	public String toString() {
		return "usuaCiudId=" + usuaCiudId + ", usuarios="
				+ usuarios.getUsuario() + ", ciudad=" + ciudad.getDescripcion();
	}

}
