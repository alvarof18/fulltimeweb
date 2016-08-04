package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.model.Horario;

@Name("horarioList")

public class HorarioList extends EntityQuery<Horario> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select horario from Horario horario";

	private static final String[] RESTRICTIONS = { "lower(horario.descripcion) like concat(lower(#{horarioList.horario.descripcion}),'%')",
													"lower(horario.estado) like lower(#{horarioList.horario.estado})"};

	private static final String ORDER = "horario.descripcion";

	private String extensionArchivo;
	
	private Horario horario = new Horario();

	public HorarioList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
	}

	public Horario getHorario() {
		return horario;
	}

	public List<Horario> getListaHorario() {
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setHorario(Horario horario) {
		this.horario = horario;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
	
	//////////////////////////////////////////////////////////////
	public List<Horario> getListaHorario2(String n) {
		this.setMaxResults(null);
		this.getHorario().setDescripcion(null);
		this.getHorario().setEstado(null);
		if(n.equals("")){
			return null;
		}else{
			return this.getResultList();
		}
		
		
	}
	//////////////////////////////////////////////////////////////
}
