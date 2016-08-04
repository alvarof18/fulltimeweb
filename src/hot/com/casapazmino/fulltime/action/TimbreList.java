package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Timbre;

@Name("timbreList")
public class TimbreList extends EntityQuery<Timbre> {

	private static final long serialVersionUID = 8722391503087532955L;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	private static final String EJBQL = "select timbre from Timbre timbre";
	private static final String ORDER = "timbre.codigoEmpleado,timbre.fechaHoraTimbre";

	private static final String[] RESTRICTIONS = {
		"lower(timbre.codigoEmpleado) = (lower(#{timbreList.timbre.codigoEmpleado}))",
		ControlBaseDatos.modificadorConvertirFecha + "timbre.fechaHoraTimbre) = #{timbreList.timbre.fechaHoraTimbre}",
		"timbre.fechaHoraTimbre = #{timbreList.fechaHoraTimbre}",
	};
	
	private Date fechaHoraTimbre;
	private Date fechaDesde;
	private Date fechaHasta;
	private String extensionArchivo;
	
	private Long[] ciudades;
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	private String tipoReporte;
	
	private Empleado empleado;
	
	private int accesoEmpleados;
	
	private boolean tabulado;	
	
	private Timbre timbre = new Timbre();

	public TimbreList() {
		setEjbql(EJBQL);
		setOrder(ORDER);		
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
		
		//***************************NEW METHOD
		
		GestionPermisoVacacion gp = (GestionPermisoVacacion) Component.getInstance("gestionPermisoVacacion", true);
		
		this.setAccesoEmpleados(gp.buscarAccesoEmpleados());
		
		if(this.getAccesoEmpleados() !=2){
			Empleado em = gp.buscarEmpleado();
			timbre.setCodigoEmpleado(em.getCodigoEmpleado());
		}
		
		//***************************
	}
	
	public boolean VisualizarFieldButton(){
		boolean visualiza=true;
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		if(this.getAccesoEmpleados() !=2){
			visualiza=false;
		}
		
		return visualiza;
	}
	
	public void asignarCargo(){
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		
//		this.setGestionaPermiso(gestionPermisoVacacion.buscarPermiso());
//		if (!isGestionaPermiso()) {
//			this.setTipoReporte("Empleado");
//		}
		
		if(this.getAccesoEmpleados() == 0){
			this.setTipoReporte("Empleado");
		}
		
		//************************************NEW
		if(this.getAccesoEmpleados() == 1){
			if(this.getTipoReporte().equals("")){
				this.setTipoReporte("Departamento");
			}			
			if(this.getTipoReporte().equals("Cargo")){
				this.setTipoReporte("Departamento");
			}
			if(this.getTipoReporte().equals("Grupo")){
				this.setTipoReporte("Departamento");
			}
		}
		//***********************************
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}
	}
	
	public List<Timbre> getTimbreEmpleado(){
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		Date now=new Date();
		
		this.timbre.setFechaHoraTimbre(now);
		this.timbre.setCodigoEmpleado(empleado.getCodigoEmpleado());
		
		return getResultList();
	}	

	public List<Timbre> getListaTimbres(){
		setMaxResults(null);
		return this.getResultList();
	}
	
	public Timbre getTimbre() {
		return timbre;
	}

	public String getExtensionArchivo() {
		return extensionArchivo;
	}

	public void setExtensionArchivo(String extensionArchivo) {
		this.extensionArchivo = extensionArchivo;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Long[] getCiudades() {
		return ciudades;
	}

	public void setCiudades(Long[] ciudades) {
		this.ciudades = ciudades;
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

	public Empleado getEmpleado() {
		if (empleado == null){
			empleado = new Empleado();
		}
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
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

	public Date getFechaHoraTimbre() {
		return fechaHoraTimbre;
	}

	public void setFechaHoraTimbre(Date fechaHoraTimbre) {
		this.fechaHoraTimbre = fechaHoraTimbre;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

	public boolean isTabulado() {
		return tabulado;
	}

	public void setTabulado(boolean tabulado) {
		this.tabulado = tabulado;
	}
	
}
