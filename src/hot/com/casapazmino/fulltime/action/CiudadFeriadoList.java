package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.model.CiudadFeriado;

@Name("ciudadFeriadoList")
public class CiudadFeriadoList extends EntityQuery<CiudadFeriado> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select ciudadFeriado from CiudadFeriado ciudadFeriado";

	private static final String[] RESTRICTIONS = {
		ControlBaseDatos.modificadorConvertirFecha + "ciudadFeriado.feriado.fecha) >= #{ciudadFeriadoList.ciudadFeriado.feriado.fecha}",
		"lower(ciudadFeriado.ciudad.descripcion) like concat(lower(#{ciudadFeriadoList.ciudadFeriado.ciudad.descripcion}),'%')",
		"ciudadFeriado.ciudad.ciudId = #{ciudadFeriadoList.ciudadFeriado.ciudad.ciudId}",
		"ciudadFeriado.feriado.feriId = #{ciudadFeriadoList.ciudadFeriado.feriado.feriId}",
		
		ControlBaseDatos.modificadorConvertirFecha +"ciudadFeriado.feriado.fecha) >= #{ciudadFeriadoList.feriadoDesde}",
		ControlBaseDatos.modificadorConvertirFecha +"ciudadFeriado.feriado.fecha) <= #{ciudadFeriadoList.feriadoHasta}",
		"ciudadFeriado.ciudad.ciudId in (#{ciudadFeriadoList.listaCiudades})",
		
		ControlBaseDatos.modificadorConvertirFecha +"ciudadFeriado.feriado.fechaRecupera) >= #{ciudadFeriadoList.recuperableDesde}",
		ControlBaseDatos.modificadorConvertirFecha +"ciudadFeriado.feriado.fechaRecupera) <= #{ciudadFeriadoList.recuperableHasta}",
	};

	private String extensionArchivo;
	
	private Date feriadoDesde;
	private Date feriadoHasta;
	private ArrayList<Long> listaCiudades;
	
	private Date recuperableDesde;
	private Date recuperableHasta;
	
	private CiudadFeriado ciudadFeriado = new CiudadFeriado();

	public CiudadFeriadoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}
	
	public List<CiudadFeriado> getListaCiudadFeriados() {
		this.setMaxResults(null);
		return this.getResultList();
	}	

	public CiudadFeriado getCiudadFeriado() {
		return ciudadFeriado;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public Date getFeriadoDesde() {
		return feriadoDesde;
	}

	public void setFeriadoDesde(Date feriadoDesde) {
		this.feriadoDesde = feriadoDesde;
	}

	public Date getFeriadoHasta() {
		return feriadoHasta;
	}

	public void setFeriadoHasta(Date feriadoHasta) {
		this.feriadoHasta = feriadoHasta;
	}
	
	public ArrayList<Long> getListaCiudades() {
		if (listaCiudades == null){
			listaCiudades = new ArrayList<Long>();
		}
		return listaCiudades;
	}

	public void setListaCiudades(ArrayList<Long> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public Date getRecuperableDesde() {
		return recuperableDesde;
	}

	public void setRecuperableDesde(Date recuperableDesde) {
		this.recuperableDesde = recuperableDesde;
	}

	public Date getRecuperableHasta() {
		return recuperableHasta;
	}

	public void setRecuperableHasta(Date recuperableHasta) {
		this.recuperableHasta = recuperableHasta;
	}
}
