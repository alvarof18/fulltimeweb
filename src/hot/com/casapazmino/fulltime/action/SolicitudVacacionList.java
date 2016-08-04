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
import com.casapazmino.fulltime.model.SolicitudVacacion;
import com.casapazmino.fulltime.seguridad.model.UsuarioCiudad;

@Name("solicitudVacacionList")
public class SolicitudVacacionList extends EntityQuery<SolicitudVacacion> {

	private static final long serialVersionUID = 223107457342789887L;

	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	@In(create = true)
	DepartamentoAutorizacionList departamentoAutorizacionList;
	
	@In
	EntityManager entityManager;
	
	@Logger
	Log log;
	
	private static final String EJBQL = "select solicitudVacacion from SolicitudVacacion solicitudVacacion";

	private static final String[] RESTRICTIONS = {
			/*"solicitudVacacion.sova = #{solicitudVacacionList.solicitudVacacion.sova}",*/
			"solicitudVacacion.empleadoByEmplId.emplId = #{solicitudVacacionList.solicitudVacacion.empleadoByEmplId.emplId}",
			"solicitudVacacion.empleadoByEmpEmplId.emplId = #{solicitudVacacionList.solicitudVacacion.empleadoByEmpEmplId.emplId}",
			"lower(solicitudVacacion.empleadoByEmplId.codigoEmpleado) like (lower(#{solicitudVacacionList.solicitudVacacion.empleadoByEmplId.codigoEmpleado}))",
			"lower(solicitudVacacion.empleadoByEmplId.cedula) like (lower(#{solicitudVacacionList.solicitudVacacion.empleadoByEmplId.cedula}))",
			"lower(solicitudVacacion.empleadoByEmplId.apellido) like concat(lower(#{solicitudVacacionList.solicitudVacacion.empleadoByEmplId.apellido}),'%')",
			"lower(solicitudVacacion.empleadoByEmplId.nombre) like concat(lower(#{solicitudVacacionList.solicitudVacacion.empleadoByEmplId.nombre}),'%')",
			"solicitudVacacion.empleadoByEmplId.ciudad.ciudId  in (#{solicitudVacacionList.listaCiudades})",
			"lower(solicitudVacacion.detalleTipoParametro.descripcion) = concat(lower(#{solicitudVacacionList.autorizado}))",
			ControlBaseDatos.modificadorConvertirFecha + "solicitudVacacion.fechaDesde) >= #{solicitudVacacionList.solicitudVacacion.fechaDesde}",	
			"solicitudVacacion.estado in (#{solicitudVacacionList.listaEstado})",
			"solicitudVacacion.nivel  >= #{solicitudVacacionList.nivel}",
			"solicitudVacacion.nivel  = #{solicitudVacacionList.nivelAux}",
			"solicitudVacacion.nivel  > #{solicitudVacacionList.nivelAux2}",	
			
	};

	private String extensionArchivo;
	private boolean gestionaVacacion;
	
	private Long[] ciudades;
	private Long[] cargos;
	private Long[] detalleGrupoContratados;
	private Long[] departamentos;
	private String tipoReporte;
	
	private Date fechaDesde;
	private Date fechaHasta;
	
	private int accesoEmpleados;
	
	private boolean tabulado;
		
	private boolean verDetalleKardexVacacion = true;
	private Long[] activoInactivo = {(long) 5};	
	private Long[] histLaboActivoInactivo = {(long) 1};
	
	private SolicitudVacacion solicitudVacacion = new SolicitudVacacion();
	List<Empleado> subOrdinados = new ArrayList<Empleado>();
	Set<SolicitudVacacion> solicitudVacaciones = null;	
	
	public String autorizado = "NO";
	
	private String estado="0";
	
	private ArrayList<Long> listaCiudades;
	
	private Integer nivel;
	private Integer nivelAux;
	private Integer nivelAux2;

	private Departamento departamento;
	
	private ArrayList<String> listaEstado=new ArrayList<String>();	
	
	private Long[] autorizadoSiNo = {(long) 0,(long) 1,(long) 2,(long) 3};
	
	private boolean verDetalle=true;

	public SolicitudVacacionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(Constantes.MAX_RESULTS);
		setExtensionArchivo(Constantes.PDF);
		
		this.getSolicitudVacacion().setFechaDesde(Fechas.SumarRestarDias(Fechas.fechaActual(), -this.buscarDiasConsultaPermiso()) );
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
	
	public List<SolicitudVacacion> getListaSolicitudVacacionNiveles() {

		List<SolicitudVacacion> solicitudesList = new ArrayList<SolicitudVacacion>();
		
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
									this.solicitudVacacion.setEmpleadoByEmpEmplId(null);						
									
//									this.nivelAux2=1;
									this.nivelAux = nivelDepartamento;
									this.listaEstado.add("1");									
									
									//***Busqueda recursiva
									String completo= this.FiltrarNivelDeartamento(i,"#{solicitudVacacionList.departamento.depaId}");
									this.AgregarRestriccion(completo);
									//*********************
									solicitudesList.addAll(this.getResultList());								
																	
									this.listaEstado.clear();	
									this.nivelAux2=null;
									this.nivelAux = null;
									this.solicitudVacacion.setEmpleadoByEmpEmplId(null);
									//*********************************
									
									this.listaEstado.add("0");	
									this.solicitudVacacion.setEmpleadoByEmpEmplId(empleado);
								}
							}							
							else{
								this.listaEstado.add("1");
							}
							
							this.nivelAux2=1;
							this.nivelAux=null;
							
							//***Busqueda recursiva
							String completo= this.FiltrarNivelDeartamento(i,"#{solicitudVacacionList.departamento.depaId}");
							this.AgregarRestriccion(completo);		
							//**********************
									
							solicitudesList.addAll(this.getResultList());
							//log.info("solicitudVacacionList estado[0]!!!!!!!!!!!!!!!!!!!!!!!!!!!! ["+i+"]:.................................:"+solicitudesList.size());
							//log.info("completo!!!!!!!!!!!!!!!!!!!!!!!!!!!! ["+i+"]:.................................:"+completo);
							
							this.listaEstado.clear();	
							this.nivelAux2=null;
							this.solicitudVacacion.setEmpleadoByEmpEmplId(null);
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
								String completo= this.FiltrarNivelDeartamento(i,"#{solicitudVacacionList.departamento.depaId}");
								this.AgregarRestriccion(completo);		
								//**********************
								
								solicitudesList.addAll(this.getResultList());
								//log.info("solicitudVacacionList estado[!0]!!!!!!!!!!!!!!!!!!!!!!!!!!!! ["+i+"]:.................................:"+solicitudesList.size());
								i--;
							}
						}
						//*************************						
					}	
					
					//***Busqueda recursiva
					String completo= this.FiltrarNivelDeartamento(nivelDepartamento,"#{solicitudVacacionList.departamento.depaId}");
					this.AgregarRestriccion(completo);				
					//**********************				
					solicitudesList.addAll(this.getResultList());
				}			
			}else{
				this.nivel=1;
				//this.nivel=empleado.getDepartamento().getNivel();
				this.listaEstado.add(this.estado);
				this.getSolicitudVacacion().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
				solicitudesList.addAll(this.getResultList());
				}
		}else{
			this.buscarUsuarioCiudad();
			if(getListaCiudades().size()>0)
				this.listaEstado.add(this.estado);
			solicitudesList.addAll(this.getResultList());
		}		
		return solicitudesList;	
	}
	
	private String FiltrarNivelDeartamento(Integer n, String cadena){
		String inicio ="solicitudVacacion.empleadoByEmplId.departamento";
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
	
	
	public List<SolicitudVacacion> getListaSolicitudVacacion(){
		
		List<SolicitudVacacion> solicitudVacaciones = new ArrayList<SolicitudVacacion>();
		Long estado;

		try {
			Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
			this.setAccesoEmpleados(gestionPermisoVacacion.buscarAccesoEmpleados());
			
			if (getAccesoEmpleados() ==  Constantes.ACCESO_INDIVIDUAL){
				this.getSolicitudVacacion().getEmpleadoByEmplId().setEmplId(empleado.getEmplId());
				//this.setMaxResults(Constantes.MAX_RESULTS);
				solicitudVacaciones.addAll(this.getResultList());
			} else if (getAccesoEmpleados() ==  Constantes.ACCESO_SUBORDINADOS) {
				
				if (this.autorizado.equals("SI")) estado = (long) 3;
				else estado = (long) 4;
				
				// Buscar permisos de la persona que ingreso al sistema
				solicitudVacaciones.addAll(this.buscarVacacionesJefeSubordinados(this.solicitudVacacion.getFechaDesde(), empleado.getEmplId(), estado));
				
				this.getSolicitudVacacion().setEmpleadoByEmpEmplId(empleado);
				solicitudVacaciones.addAll(this.getResultList());

			} else {
				this.buscarUsuarioCiudad();
				solicitudVacaciones.addAll(this.getResultList());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return solicitudVacaciones;
		
	}

	@SuppressWarnings("unchecked")
	public List<SolicitudVacacion> buscarVacacionesJefeSubordinados(Date fechaDesde, Long emplId, long estado) {
		return (List<SolicitudVacacion>) entityManager
				.createQuery(
						"select s from SolicitudVacacion s where s.fechaDesde >= (:fechaDesde) and s.detalleTipoParametro.dtpaId = (:autorizado) and s.empleadoByEmplId.emplId = (:emplId)")
				.setParameter("emplId", emplId)
				.setParameter("autorizado", estado)
				.setParameter("fechaDesde", fechaDesde)
				.getResultList();
	}	

	public int buscarDiasConsultaPermiso(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getDiasConsultaPermiso();
		
		return Integer.parseInt(detalleTipoParametro.getDescripcion());
	}
	
	// VER SI ESTE METODO PUEDE RETORNAR UNA LISTA DE EMPLEADOS
	// PARA IMPLEMENTAR UNA BUSQUEDA CON LA CLAUSULA in
//	public void buscarSubordinados(Empleado empleado) {
//		estado = true;
//		if (empleado.getEmpleados().size() != 0){
//			for (Empleado subOrdinado : empleado.getEmpleados()) {
//				this.subOrdinados.add(subOrdinado);
//				buscarSubordinados(subOrdinado);
//			}
//		}
//	}
//	
//	public List<SolicitudVacacion> buscarVacacionSubordinado(){
//		List<SolicitudVacacion> listaVacaciones = new ArrayList<SolicitudVacacion>();
//		for (Empleado empleado : subOrdinados) {
//			solicitudVacaciones =  empleado.getSolicitudVacacionsForEmplId();
//			listaVacaciones.addAll(solicitudVacaciones);			
//		}
//		return listaVacaciones;
//	}
	public void buscarUsuarioCiudad(){
		
		Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
		
		Set<UsuarioCiudad> usuarioCiudades = empleado.getUsuarios().getUsuarioCiudads();
		for (UsuarioCiudad usuarioCiudad : usuarioCiudades) {
			this.getListaCiudades().add(usuarioCiudad.getCiudad().getCiudId());
		}
	}
	
	public SolicitudVacacion getSolicitudVacacion() {
		return solicitudVacacion;
	}

	public void setSolicitudVacacion(SolicitudVacacion solicitudVacacion) {
		this.solicitudVacacion = solicitudVacacion;
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

	public boolean isGestionaVacacion() {
		return gestionaVacacion;
	}

	public void setGestionaVacacion(boolean gestionaVacacion) {
		this.gestionaVacacion = gestionaVacacion;
	}

	public Set<SolicitudVacacion> getSolicitudVacaciones() {
		return solicitudVacaciones;
	}

	public void setSolicitudVacaciones(Set<SolicitudVacacion> solicitudVacaciones) {
		this.solicitudVacaciones = solicitudVacaciones;
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

	public boolean isVerDetalleKardexVacacion() {
		return verDetalleKardexVacacion;
	}

	public void setVerDetalleKardexVacacion(boolean verDetalleKardexVacacion) {
		this.verDetalleKardexVacacion = verDetalleKardexVacacion;
	}

	public Long[] getActivoInactivo() {
		return activoInactivo;
	}

	public void setActivoInactivo(Long[] activoInactivo) {
		this.activoInactivo = activoInactivo;
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

	public Long[] getAutorizadoSiNo() {
		return autorizadoSiNo;
	}

	public void setAutorizadoSiNo(Long[] autorizadoSiNo) {
		this.autorizadoSiNo = autorizadoSiNo;
	}

	public boolean isVerDetalle() {
		return verDetalle;
	}

	public void setVerDetalle(boolean verDetalle) {
		this.verDetalle = verDetalle;
	}

	public Long[] getHistLaboActivoInactivo() {
		return histLaboActivoInactivo;
	}

	public void setHistLaboActivoInactivo(Long[] histLaboActivoInactivo) {
		this.histLaboActivoInactivo = histLaboActivoInactivo;
	}

}
