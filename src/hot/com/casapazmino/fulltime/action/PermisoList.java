package com.casapazmino.fulltime.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.ControlBaseDatos;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Departamento;
import com.casapazmino.fulltime.model.DepartamentoAutorizacion;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.seguridad.model.UsuarioCiudad;

@Name("permisoList")
public class PermisoList extends EntityQuery<Permiso> {

	private static final long serialVersionUID = 8338538941014213332L;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;

	@In
	EntityManager entityManager;

	@Logger
	Log log;
	
	@In(create = true)
	DepartamentoAutorizacionList departamentoAutorizacionList;
	
	private static final String EJBQL = "select permiso from Permiso permiso";

	private static String[] RESTRICTIONS = {
		"permiso.empleadoByEmplId.emplId = #{permisoList.permiso.empleadoByEmplId.emplId}",
		"permiso.empleadoByEmpEmplId.emplId = #{permisoList.permiso.empleadoByEmpEmplId.emplId}",
		"lower(permiso.empleadoByEmplId.cedula) like (lower(#{permisoList.permiso.empleadoByEmplId.cedula}))",
		"lower(permiso.empleadoByEmplId.codigoEmpleado) like (lower(#{permisoList.permiso.empleadoByEmplId.codigoEmpleado}))",
		"lower(permiso.empleadoByEmplId.apellido) like concat(lower(#{permisoList.permiso.empleadoByEmplId.apellido}),'%')",
		"lower(permiso.empleadoByEmplId.nombre) like concat(lower(#{permisoList.permiso.empleadoByEmplId.nombre}),'%')",
		"lower(permiso.detalleTipoParametro.descripcion) = concat(lower(#{permisoList.autorizado}))",		
		"permiso.empleadoByEmplId.ciudad.ciudId  in (#{permisoList.listaCiudades})",		
		ControlBaseDatos.modificadorConvertirFecha + "permiso.fechaDesde) >= #{permisoList.permiso.fechaDesde}",
		"permiso.estadoActual in (#{permisoList.listaEstado})",
		"permiso.nivel  >= #{permisoList.nivel}",
		"permiso.nivel  = #{permisoList.nivelAux}",
		"permiso.nivel  > #{permisoList.nivelAux2}",
		"permiso.permId = #{permisoList.permiso.permId}",
		//"permiso.empleadoByEmplId.departamento.depaId = #{permisoList.departamento.depaId}",	
	};

	private String extensionArchivo;
	private Boolean gestionaPermiso;
	
	private Long[] ciudades;
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	
	private Long[] tipoPermisos;
	private Long[] autorizadoSiNo = {(long) 0,(long) 1,(long) 2,(long) 3};
	
	private String[] listaLegalizado = {"true","false"};
	
	private String tipoReporte;
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	public String autorizado = "NO";
	public String estado = "0";
//	public boolean estado = false;

	private boolean tabulado;
	
	private boolean verDetalle=true;
	
	private int accesoEmpleados;
	private ArrayList<Long> listaCiudades;
	
	private ArrayList<String> listaEstado=new ArrayList<String>();	

	List<Empleado> subOrdinados = new ArrayList<Empleado>();
	Set<Permiso> permisos = null;
	
	private Permiso permiso = new Permiso();
	
	private Integer nivel;
	
	private Integer nivelAux;
	
	private Integer nivelAux2;
	
	private Departamento departamento;

	public PermisoList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
		
		this.getPermiso().setFechaDesde(Fechas.SumarRestarDias(Fechas.fechaActual(), -this.buscarDiasConsultaPermiso()));		
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
		
		if(this.getTipoReporte().equals("")){
			this.setTipoReporte("Cargo");
		}
	}
	
	public List<Permiso> getListaPermisoNiveles() {

		List<Permiso> permisosList = new ArrayList<Permiso>();
		
		//****************NEW FABRICIO***************************
		
		this.listaEstado.clear();
		
		this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());	
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();		
	
		if (getAccesoEmpleados() ==  Constantes.ACCESO_INDIVIDUAL || getAccesoEmpleados() ==  Constantes.ACCESO_SUBORDINADOS){
			
			departamentoAutorizacionList.getDepartamentoAutorizacion().setEmpleado(empleado);
			
			List<DepartamentoAutorizacion> departamentoAutorizacion=new ArrayList<DepartamentoAutorizacion>();
			
			departamentoAutorizacion=departamentoAutorizacionList.getResultList();
			
			//log.info("EMPL_ID!!!!!!!!!!!!!!!!!!!!!!!!!!!!:.................................:"+empleado.getEmplId());
			//log.info("VAMOS!!!!!!!!!!!!!!!!!!!!!!!!!!!!:.................................:"+departamentoAutorizacion.size());
			
			if(departamentoAutorizacion.size()>0){			
				
				this.buscarUsuarioCiudad();
				if(getListaCiudades().size()>0){		
					
					Integer nivelDepartamento=empleado.getDepartamento().getNivel();
					this.departamento=empleado.getDepartamento();
					
					if(this.estado.equals("0")){					
						//log.info("nivelDepartamento!!!!!!!!!!!!!!!!!!!!!!!!!!!!:.................................:"+nivelDepartamento);						
						//****************validar datos	
						Integer i=nivelDepartamento;
						if(i>1)
							i--;					
						while (i>0){
							if(i<=2){
								this.listaEstado.add("0");	
								if(i==2){
									//******permisos sin el mismo jefe
									this.listaEstado.clear();
									this.nivelAux=null;								
									this.permiso.setEmpleadoByEmpEmplId(null);						
									
//									this.nivelAux2=1;
									this.nivelAux =  nivelDepartamento;
									this.listaEstado.add("1");									
									
									//***Busqueda recursiva
									String completo= this.FiltrarNivelDeartamento(i,"#{permisoList.departamento.depaId}");
									this.AgregarRestriccion(completo);
									//*********************
									permisosList.addAll(this.getResultList());								
																	
									this.listaEstado.clear();	
									this.nivelAux2=null;
									this.nivelAux = null;
									this.permiso.setEmpleadoByEmpEmplId(null);
									//*********************************
									
									this.listaEstado.add("0");	
									this.permiso.setEmpleadoByEmpEmplId(empleado);
								}
							}							
							else{
								this.listaEstado.add("1");
							}
							
							this.nivelAux2=1;
							this.nivelAux=null;
							
							//***Busqueda recursiva
							String completo= this.FiltrarNivelDeartamento(i,"#{permisoList.departamento.depaId}");
							this.AgregarRestriccion(completo);		
							//**********************
									
							permisosList.addAll(this.getResultList());
							//log.info("permisosList estado[0]!!!!!!!!!!!!!!!!!!!!!!!!!!!! ["+i+"]:.................................:"+permisosList.size());
							//log.info("completo!!!!!!!!!!!!!!!!!!!!!!!!!!!! ["+i+"]:.................................:"+completo);						
							this.listaEstado.clear();	
							this.nivelAux2=null;
							this.permiso.setEmpleadoByEmpEmplId(null);
							i--;
						}					
						//*****************************		
						
						this.listaEstado.add("0");
						this.listaEstado.add("1");
						this.nivelAux=nivelDepartamento;
					}					
					else{
						if(this.estado.equals("1"))
							this.nivelAux2=nivelDepartamento;
						else
							this.nivel=nivelDepartamento;		
						
						listaEstado.add(this.estado);
						//*******************validar
						if (nivelDepartamento>1){
							Integer i=nivelDepartamento;
							i--;
							while (i>0){
								//***Busqueda recursiva
								String completo= this.FiltrarNivelDeartamento(i,"#{permisoList.departamento.depaId}");
								this.AgregarRestriccion(completo);		
								//**********************							
								permisosList.addAll(this.getResultList());
								//log.info("permisosList estado[!0]!!!!!!!!!!!!!!!!!!!!!!!!!!!! ["+i+"]:.................................:"+permisosList.size());
								i--;
							}
						}
						//*************************						
					}					
					//***Busqueda recursiva
					String completo= this.FiltrarNivelDeartamento(nivelDepartamento,"#{permisoList.departamento.depaId}");
					this.AgregarRestriccion(completo);				
					//**********************				
					permisosList.addAll(this.getResultList());					
				}
			}else{
				this.nivel=1;
				//this.nivel=empleado.getDepartamento().getNivel();
				this.listaEstado.add(this.estado);
				this.getPermiso().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
				permisosList.addAll(this.getResultList());
				}			
		}else{			
			this.buscarUsuarioCiudad();
			if(getListaCiudades().size()>0)
				this.listaEstado.add(this.estado);
			permisosList.addAll(this.getResultList());
		}		
		return permisosList;	
	}
	
	public List<Permiso> getListaPermisos() {
		
		List<Permiso> permisosList = new ArrayList<Permiso>();
		Long estado;
		
//		Busca el empleado que ingreso al sistema
		try {
			Empleado empleado = gestionPermisoVacacion.buscarEmpleado();

			this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
			
			if (getAccesoEmpleados() ==  Constantes.ACCESO_INDIVIDUAL){
				
				this.getPermiso().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
				permisosList.addAll(this.getResultList());
				
			} else if (getAccesoEmpleados() ==  Constantes.ACCESO_SUBORDINADOS) {

				if (this.autorizado.equals("SI")) estado = (long) 3;
				else estado = (long) 4;
				
				// Buscar permisos de la persona que ingreso al sistema
				permisosList.addAll(this.buscarPermisosJefeSubordinados(this.permiso.getFechaDesde(), empleado.getEmplId(), estado));
				
				// Buscar permisos de sus subordinados
				this.getPermiso().setEmpleadoByEmpEmplId(empleado);
				permisosList.addAll(this.getResultList());
				
			} else {
				
				this.buscarUsuarioCiudad();
				permisosList.addAll(this.getResultList());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return permisosList;
	}
	
	private String FiltrarNivelDeartamento(Integer n, String cadena){
		String inicio ="permiso.empleadoByEmplId.departamento";
		String fin=".depaId = ";
		
		Integer i=1;
		while(i<n){
			inicio=inicio+".departamento";
			i++;
		}
		String completo=inicio+fin+cadena;		
		return completo;
	}
	
	
	private void AgregarRestriccion(String cadena){
		String[] RESTRICTIONSAux = new String [RESTRICTIONS.length+1];
		
		for(int i=0;i<RESTRICTIONSAux.length;i++){
			
			if(i==(RESTRICTIONSAux.length-1)){
				RESTRICTIONSAux[i]=cadena;
			}else{
				RESTRICTIONSAux[i]=RESTRICTIONS[i];
			}
		}			
//		for(int i=0;i<RESTRICTIONSAux.length;i++){
//			System.out.println("RESTRICTIONSAux["+i+"]: "+RESTRICTIONSAux[i]);
//		}
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONSAux));
	}

	
//    public Calendar restarFechasDias(Date fecha, int dias) {
//        Calendar calendar = Fechas.calendarioActual();
//        calendar.add(Calendar.DATE, -dias);
//        return calendar;
//    }
    
	public int buscarDiasConsultaPermiso(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getDiasConsultaPermiso();
		
		return Integer.parseInt(detalleTipoParametro.getDescripcion());
	}
	
	public void buscarUsuarioCiudad(){
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		
		Set<UsuarioCiudad> usuarioCiudades = empleado.getUsuarios().getUsuarioCiudads();
		for (UsuarioCiudad usuarioCiudad : usuarioCiudades) {
			this.getListaCiudades().add(usuarioCiudad.getCiudad().getCiudId());
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Permiso> buscarPermisosJefeSubordinados(Date fechaDesde, Long emplId, long estado) {
		return (List<Permiso>) entityManager
				.createQuery(
						"select p from Permiso p where p.fechaDesde >= (:fechaDesde) and p.detalleTipoParametro.dtpaId = (:autorizado) and p.empleadoByEmplId.emplId = (:emplId)")
				.setParameter("emplId", emplId)
				.setParameter("autorizado", estado)
				.setParameter("fechaDesde", fechaDesde)
				.getResultList();
	}	
		
//	@SuppressWarnings("unchecked")
//	public List<Permiso> buscarPermisosJefeSubordinados(Date fechaDesde, Long emplId, long estado) {
//		return (List<Permiso>) entityManager
//				.createQuery(
//						"select p from Permiso p where p.fechaDesde >= (:fechaDesde) and p.detalleTipoParametro.dtpaId = (:autorizado) and (p.empleadoByEmplId.emplId = (:emplId) or p.empleadoByEmpEmplId.emplId = (:emplId)) ")
//				.setParameter("emplId", emplId)
//				.setParameter("autorizado", estado)
//				.setParameter("fechaDesde", fechaDesde)
//				.getResultList();
//	}	

	public Permiso getPermiso() {
		return permiso;
	}

	public void setPermiso(Permiso permiso) {
		this.permiso = permiso;
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
		if (tipoReporte == null){
			tipoReporte = new String();	
		}
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

	public Boolean isGestionaPermiso() {
		return gestionaPermiso;
	}

	public void setGestionaPermiso(Boolean gestionaPermiso) {
		this.gestionaPermiso = gestionaPermiso;
	}

	public String getAutorizado() {
		return autorizado;
	}

	public void setAutorizado(String autorizado) {
		this.autorizado = autorizado;
	}

	public int getAccesoEmpleados() {
		return accesoEmpleados;
	}

	public void setAccesoEmpleados(int accesoEmpleados) {
		this.accesoEmpleados = accesoEmpleados;
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

	public boolean isTabulado() {
		return tabulado;
	}

	public void setTabulado(boolean tabulado) {
		this.tabulado = tabulado;
	}

	public Long[] getTipoPermisos() {		
		return tipoPermisos;
	}

	public void setTipoPermisos(Long[] tipoPermisos) {
		this.tipoPermisos = tipoPermisos;
	}

	public Long[] getAutorizadoSiNo() {
		return autorizadoSiNo;
	}

	public void setAutorizadoSiNo(Long[] autorizadoSiNo) {
		this.autorizadoSiNo = autorizadoSiNo;
	}

	public Integer getNivel() {
		return this.nivel;
	}

	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}

	public Departamento getDepartamento() {
		if(this.departamento==null){
			this.departamento=new Departamento();
		}
		return this.departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Integer getNivelAux() {
		return this.nivelAux;
	}

	public void setNivelAux(Integer nivelAux) {
		this.nivelAux = nivelAux;
	}

	public ArrayList<String> getListaEstado() {
		return listaEstado;
	}

	public void setListaEstado(ArrayList<String> listaEstado) {
		this.listaEstado = listaEstado;
	}

	public Integer getNivelAux2() {
		return nivelAux2;
	}

	public void setNivelAux2(Integer nivelAux2) {
		this.nivelAux2 = nivelAux2;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public boolean isVerDetalle() {
		return verDetalle;
	}

	public void setVerDetalle(boolean verDetalle) {
		this.verDetalle = verDetalle;
	}

	public String[] getListaLegalizado() {
		return listaLegalizado;
	}

	public void setListaLegalizado(String[] listaLegalizado) {
		this.listaLegalizado = listaLegalizado;
	}
	
}