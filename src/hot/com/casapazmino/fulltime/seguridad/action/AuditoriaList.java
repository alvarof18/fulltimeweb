package com.casapazmino.fulltime.seguridad.action;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.seguridad.model.Auditoria;

@Name("auditoriaList")
public class AuditoriaList extends EntityQuery<Auditoria> {

	private static final long serialVersionUID = -2781177780583305174L;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	private static final String EJBQL = "select auditoria from Auditoria auditoria";

	private static final String[] RESTRICTIONS = {
			ControlBaseDatos.modificadorConvertirFecha + "auditoria.fecha) >= #{auditoriaList.fechaDesde}",
			ControlBaseDatos.modificadorConvertirFecha + "auditoria.fecha) <= #{auditoriaList.fechaHasta}",
			"lower(auditoria.cadenaActual) like concat(lower(#{auditoriaList.auditoria.cadenaActual}),'%')",
			"lower(auditoria.cadenaAnterior) like concat(lower(#{auditoriaList.auditoria.cadenaAnterior}),'%')",
			"lower(auditoria.operacion) like concat(lower(#{auditoriaList.auditoria.operacion}),'%')",
			"lower(auditoria.tabla) like concat(lower(#{auditoriaList.auditoria.tabla}),'%')",};
	

	private Auditoria auditoria = new Auditoria();

	private String extensionArchivo;
	
	private Long[] usuarios;
	private Long[] ciudades;
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	private String tipoReporte;
	
	private int accesoEmpleados;	
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	public AuditoriaList() {
		setExtensionArchivo("Pdf");		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Auditoria getAuditoria() {
		return auditoria;
	}
	
	public void asignarCargo(){
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
		
		if(this.getAccesoEmpleados() == 0){
			this.setTipoReporte("Empleado");
		}
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}
	}	
	public List<Auditoria> getListaAuditorias(){
		setMaxResults(null);
		return this.getResultList();
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

	public String getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
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

	public Long[] getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Long[] usuarios) {
		this.usuarios = usuarios;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
	}

}
