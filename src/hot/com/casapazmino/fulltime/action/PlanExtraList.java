package com.casapazmino.fulltime.action;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.security.Credentials;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.PlanExtras;
import com.casapazmino.fulltime.seguridad.model.UsuarioCiudad;

@Name("planExtrasList")
public class PlanExtraList extends EntityQuery<PlanExtras> {
	
	private Long[] ciudades;
	private Long[] cargos;
	private Long[]	detalleGrupoContratados;
	private String tipoReporte;
	private Long[] departamentos;
	private Date fechaDesde;
	private Date fechaHasta;
	private String extensionArchivo;
	public Integer autorizado = 4;
	
	@In(create = true)
	Credentials credentials;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	private int accesoEmpleados;	
	private ArrayList<Long> listaCiudades;	

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select planExtras from PlanExtras planExtras";

	private static final String[] RESTRICTIONS = { "planExtras.mes = #{planExtrasList.planExtras.mes}" ,
												   "planExtras.anio = #{planExtrasList.planExtras.anio}",
												   "planExtras.empleado.emplId = #{planExtrasList.planExtras.empleado.emplId}",
												   "planExtras.empleado.empleado.emplId = #{planExtrasList.planExtras.empleado.empleado.emplId}",
					"lower(planExtras.empleado.cedula) like (lower(#{planExtrasList.planExtras.empleado.cedula}))",
					"lower(planExtras.empleado.codigoEmpleado) like (lower(#{planExtrasList.planExtras.empleado.codigoEmpleado}))",
					"lower(planExtras.empleado.apellido) like concat(lower(#{planExtrasList.planExtras.empleado.apellido}),'%')",
					"lower(planExtras.empleado.nombre) like concat(lower(#{planExtrasList.planExtras.empleado.nombre}),'%')",
					"planExtras.autorizado = #{planExtrasList.autorizado}",
												   };

	//private String extensionArchivo;
	
	private PlanExtras planExtras = new PlanExtras();

	public PlanExtraList() {
		setExtensionArchivo("Pdf");
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		
	}

	public PlanExtras getPlanExtras() {		
		return planExtras;
	}

	public List<PlanExtras> getListaPlanExtra() {
		//-----------------------------------------
		
		//-----------------------------------------	
		this.setMaxResults(null);
		return this.getResultList();
	}

	public void setPlanExtras(PlanExtras planExtras) {
		this.planExtras = planExtras;
	}	

	
	public List<PlanExtras> empleados(){
		
		/*List<PlanExtras> pex = new ArrayList<PlanExtras>();			
		EmpleadoList empleadolist = (EmpleadoList) Component.getInstance("empleadoList", true);
		List<Empleado> empleado = new ArrayList<Empleado>();
		empleado=empleadolist.getEmpleadoList();
		for(Empleado em : empleado){				
			//this.planExtras.setEmpleado(em);
			//this.planExtras.setEmpleado(em);
			pex.addAll(this.getResultList());
		}		
		return pex;*/	
		
		List<PlanExtras> pex = new ArrayList<PlanExtras>();		

		try {
			Empleado empleado = gestionPermisoVacacion.buscarEmpleado();

			this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
			
//			System.out.println("ACCESS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::"+getAccesoEmpleados());
			
			if (getAccesoEmpleados() ==  Constantes.ACCESO_INDIVIDUAL){
//				System.out.println(Constantes.ACCESO_INDIVIDUAL+":::::::::::::::::::::::::::::::::::::::::::::::::::"+empleado.getEmplId());
				this.getPlanExtras().getEmpleado().setEmplId(empleado.getEmplId());				
				pex.addAll(this.getResultList());
			} else if (getAccesoEmpleados() ==  Constantes.ACCESO_SUBORDINADOS) {
//				System.out.println(Constantes.ACCESO_SUBORDINADOS+":::::::::::::::::::::::::::::::::::::::::::::::::::"+empleado.getEmplId());
				
				//this.getPlanExtras().getEmpleado().setEmplId(empleado.getEmplId());									
				this.getPlanExtras().getEmpleado().setEmpleado(empleado);				
				pex.addAll(this.getResultList());				

			} else {
				//this.buscarUsuarioCiudad();
				pex.addAll(this.getResultList());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pex;					
	}
	
	public void buscarUsuarioCiudad(){
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		
		Set<UsuarioCiudad> usuarioCiudades = empleado.getUsuarios().getUsuarioCiudads();
		for (UsuarioCiudad usuarioCiudad : usuarioCiudades) {
			this.getListaCiudades().add(usuarioCiudad.getCiudad().getCiudId());
		}
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}



	public ArrayList<Long> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(ArrayList<Long> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}	

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}
	
	public Long[] getCiudades() {
		return ciudades;
	}

	public void setCiudades(Long[] ciudades) {
		this.ciudades = ciudades;
	}
	
	public String getTipoReporte() {
		if (tipoReporte == null){
			tipoReporte = new String();	
		}
		
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	
	public Long[] getCargos() {
		return cargos;
	}

	public void setCargos(Long[] cargos) {
		this.cargos = cargos;
	}
	
	public Long[] getDetalleGrupoContratados() {
		return detalleGrupoContratados;
	}

	public void setDetalleGrupoContratados(Long[] detalleGrupoContratados) {
		this.detalleGrupoContratados = detalleGrupoContratados;
	}
	
	public Long[] getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(Long[] departamentos) {
		this.departamentos = departamentos;
	}
	
	@Transient
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	
	@Transient
	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	// Este proceso no va en el construtor se invoca desde el page.xlm
	public void asignarCargo(){
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		
		if(this.getAccesoEmpleados() == 0){
			this.setTipoReporte("Empleado");
		}
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}		
		
	}
	
	public void reset(){		
			this.planExtras = new PlanExtras();	
	}
	
	public Integer getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(Integer autorizado) {
		this.autorizado = autorizado;
	}
	
}