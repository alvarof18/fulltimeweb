package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.EmpleadoPeriodoVacacion;

@Name("empleadoPeriodoVacacionList")
public class EmpleadoPeriodoVacacionList extends EntityQuery<EmpleadoPeriodoVacacion> {

	private static final long serialVersionUID = -6430582967971312809L;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	private static final String EJBQL = "select empleadoPeriodoVacacion from EmpleadoPeriodoVacacion empleadoPeriodoVacacion";

//	Modificado 21/05/2015
	private static final String[] RESTRICTIONS = {
		"lower(empleadoPeriodoVacacion.empleado.codigoEmpleado) = #{empleadoPeriodoVacacionList.empleadoPeriodoVacacion.empleado.codigoEmpleado}",
		"empleadoPeriodoVacacion.empleado.emplId = #{empleadoPeriodoVacacionList.empleadoPeriodoVacacion.empleado.emplId}",
		"lower(empleadoPeriodoVacacion.empleado.nombre) like concat(lower(#{empleadoPeriodoVacacionList.empleadoPeriodoVacacion.empleado.nombre}),'%')",
		"lower(empleadoPeriodoVacacion.empleado.apellido) like concat(lower(#{empleadoPeriodoVacacionList.empleadoPeriodoVacacion.empleado.apellido}),'%')",
		ControlBaseDatos.modificadorConvertirFecha + "empleadoPeriodoVacacion.fechaDesde) = #{empleadoPeriodoVacacionList.empleadoPeriodoVacacion.fechaDesde}",
		"empleadoPeriodoVacacion.histLabo.hilaId = #{empleadoPeriodoVacacionList.empleadoPeriodoVacacion.histLabo.hilaId}",
		
		
	};
	private static final String ORDER = "EmpleadoPeriodoVacacion.fechaDesde";
	
	private EmpleadoPeriodoVacacion empleadoPeriodoVacacion = new EmpleadoPeriodoVacacion();
	
	public EmpleadoPeriodoVacacionList() {
		setEjbql(EJBQL);
		setOrder(ORDER);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
	}

	public EmpleadoPeriodoVacacion getEmpleadoPeriodoVacacion() {
		return empleadoPeriodoVacacion;
	}
	
	public List<EmpleadoPeriodoVacacion> getListaEmpleadoPeriodoVacacion() {
//		if (!gestionPermisoVacacion.buscarPermiso()){
//			Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
//			this.empleadoPeriodoVacacion.getEmpleado().setCodigoEmpleado(empleado.getCodigoEmpleado());
//		}
		this.setMaxResults(null);
		return this.getResultList();
	}
}
