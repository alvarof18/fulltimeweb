package com.casapazmino.fulltime.procesos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Hibernate;
import org.hibernate.validator.InvalidValue;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.action.SolicitudVacacionList;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.SolicitudVacacion;

@Name("solicitudVacacionNiveles")
@Scope(ScopeType.SESSION)
public class SolicitudVacacionNivelesBean{

	private static final long serialVersionUID = 1L;

	@In
	EntityManager entityManager;	
	
	@Logger
	Log log;

	@In(create = true)
	SolicitudVacacionList solicitudVacacionList;
	
	private List<SolicitudVacacion> listaSolicitudVacacion;
	
	private Empleado empleado;
	private String estado;
	private Date fechaDesde;	
	private Integer filas;
	private Empleado empleadoAux;
	private String estadoAux;
	private Date fechaDesdeAux;	
	private Integer filasAux;
	private Long nroSolicitud;
	
	public SolicitudVacacionNivelesBean() {	
		super();
		
		this.getEmpleado();
		this.estado="0";
//		this.filas=20;		
		this.filas=this.numeroFilas();		
		this.fechaDesde=Fechas.SumarRestarDias(Fechas.fechaActual(), - this.buscarDiasConsultaPermiso());
		
		CargarListaInicial();
		
		//variables de control de B�squeda
		getEmpleadoAux();
		this.empleadoAux.setCodigoEmpleado("");
		this.empleadoAux.setCedula("");
		this.empleadoAux.setApellido("");
		this.empleadoAux.setNombre("");
		this.estadoAux="0";
//		this.filasAux=20;		
		this.filasAux=this.numeroFilas();
		this.fechaDesdeAux=Fechas.SumarRestarDias(Fechas.fechaActual(), - this.buscarDiasConsultaPermiso());
		//
		
	}
	
	public int buscarDiasConsultaPermiso(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
				.getInstance("detalleTipoParametroList", true);
		
		DetalleTipoParametro detalleTipoParametro = detalleTipoParametroList
				.getDiasConsultaPermiso();
		
		return Integer.parseInt(detalleTipoParametro.getDescripcion());
	}
	
	public Integer numeroFilas() {

		DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
				.getInstance("detalleTipoParametroHome", true);
		
		DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();

		detalleTipoParametroHome.setId(new Long(75));
		detalleTipoParametro = detalleTipoParametroHome.find();
		return Integer.parseInt(detalleTipoParametro.getDescripcion());
		
	}
	
	
	private boolean ControlarEntrada(){
		
		String cod1=this.empleado.getCodigoEmpleado();
		String ced1=this.empleado.getCedula();
		String ape1=this.empleado.getApellido();
		String nom1=this.empleado.getNombre();
		String est1=this.estado;
		Date fec1=this.fechaDesde;
		Integer fil1=this.filas;
		
		String cod2=this.empleadoAux.getCodigoEmpleado();
		String ced2=this.empleadoAux.getCedula();
		String ape2=this.empleadoAux.getApellido();
		String nom2=this.empleadoAux.getNombre();
		String est2=this.estadoAux;
		Date fec2=this.fechaDesdeAux;
		Integer fil2=this.filasAux;
		
		if( cod1.equals(cod2) && ced1.equals(ced2) && ape1.equals(ape2) && nom1.equals(nom2) && est1.equals(est2) 
				&& ( fec1.getYear()==fec2.getYear() && fec1.getMonth()==fec2.getMonth() && fec1.getDate()==fec2.getDate() ) 
				&& fil1.equals(fil2) ){
			return false;
		}
		else{			
			this.empleadoAux.setCodigoEmpleado(cod1);
			this.empleadoAux.setCedula(ced1);
			this.empleadoAux.setApellido(ape1);
			this.empleadoAux.setNombre(nom1);
			this.estadoAux=est1;
			this.fechaDesdeAux=fec1;
			this.filasAux=fil1;
			
			return true;
		}
	}

	public void ConsultarSolicitudVacacion(){
		
		String msg="";
		try{				
			//if(ControlarEntrada()){
				this.getListaSolicitudVacacion().clear();			
				this.getListaSolicitudVacacion().addAll(RetornarDatos());		
			//}			
			msg="GOOD";	
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(msg.equals("")){
			InvalidValue iv= new InvalidValue("Seleccione los datos correctamente!",null,null,null,null);
			FacesMessages.instance().clear();
			FacesMessages.instance().add(iv);
		}
	}
	
	public void Cancelar(){
		this.getEmpleado();
		this.estado="0";
//		this.filas=20;
		this.numeroFilas();
		this.fechaDesde=Fechas.SumarRestarDias(Fechas.fechaActual(), - this.buscarDiasConsultaPermiso());			
		ConsultarSolicitudVacacion();
	}
	
	private List<SolicitudVacacion> RetornarDatos(){		
		return BusquedaSolicitudVacacion(this.filas,this.empleado,this.estado,this.fechaDesde, this.nroSolicitud);
	}
	
	private List<SolicitudVacacion> BusquedaSolicitudVacacion(Integer n,Empleado e,String estado,Date fechaDesde, Long numero){

		List <SolicitudVacacion> lista=new ArrayList<SolicitudVacacion>();
		List <SolicitudVacacion> result=new ArrayList<SolicitudVacacion>();

		solicitudVacacionList.setMaxResults(n);

		solicitudVacacionList.setAutorizado(null);
		solicitudVacacionList.getSolicitudVacacion().setEmpleadoByEmplId(e);
		solicitudVacacionList.setEstado(estado);
		solicitudVacacionList.getSolicitudVacacion().setFechaDesde(fechaDesde);
		solicitudVacacionList.getSolicitudVacacion().setSova(numero);
		
		result=solicitudVacacionList.getListaSolicitudVacacionNiveles();
		
		for(SolicitudVacacion p: result){
			Hibernate.initialize(p.getEmpleadoByEmplId());
			//Hibernate.initialize(p.getTipoPermiso());		
			Hibernate.initialize(p.getDetalleTipoParametro());		
			lista.add(p);
		}
		
		return lista;
	}
	
	private void CargarListaInicial(){
		List <SolicitudVacacion> lista=new ArrayList<SolicitudVacacion>();
		List <SolicitudVacacion> result=new ArrayList<SolicitudVacacion>();

		SolicitudVacacionList modelo = (SolicitudVacacionList) Component.getInstance("solicitudVacacionList", true);
		
		modelo.setMaxResults(this.filas);

		modelo.setAutorizado(null);
		modelo.getSolicitudVacacion().setEmpleadoByEmplId(this.empleado);
		modelo.setEstado(this.estado);
		modelo.getSolicitudVacacion().setFechaDesde(this.fechaDesde);
		
		result=modelo.getListaSolicitudVacacionNiveles();
		
		for(SolicitudVacacion p: result){
			Hibernate.initialize(p.getEmpleadoByEmplId());
			//Hibernate.initialize(p.getTipoPermiso());		
			Hibernate.initialize(p.getDetalleTipoParametro());		
			lista.add(p);
		}
		
		this.getListaSolicitudVacacion().addAll(lista);
	}

	public List<SolicitudVacacion> getListaSolicitudVacacion() {
		if (listaSolicitudVacacion == null) 
			listaSolicitudVacacion = new ArrayList <SolicitudVacacion>();		
		return listaSolicitudVacacion;
	}

	public void setListaSolicitudVacacion(List<SolicitudVacacion> listaSolicitudVacacion) {
		this.listaSolicitudVacacion = listaSolicitudVacacion;
	}

	public Empleado getEmpleado() {
		if(empleado==null)
			empleado=new Empleado();
		return empleado;
	}

	public void setEmpleado(Empleado empleado) {		
		this.empleado = empleado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Integer getFilas() {
		return filas;
	}

	public void setFilas(Integer filas) {
		this.filas = filas;
	}

	public Empleado getEmpleadoAux() {
		if(empleadoAux==null)
			empleadoAux=new Empleado();
		return empleadoAux;
	}

	public void setEmpleadoAux(Empleado empleadoAux) {
		this.empleadoAux = empleadoAux;
	}

	public String getEstadoAux() {
		return estadoAux;
	}

	public void setEstadoAux(String estadoAux) {
		this.estadoAux = estadoAux;
	}

	public Date getFechaDesdeAux() {
		return fechaDesdeAux;
	}

	public void setFechaDesdeAux(Date fechaDesdeAux) {
		this.fechaDesdeAux = fechaDesdeAux;
	}

	public Integer getFilasAux() {
		return filasAux;
	}

	public void setFilasAux(Integer filasAux) {
		this.filasAux = filasAux;
	}

	public Long getNroSolicitud() {
		return nroSolicitud;
	}

	public void setNroSolicitud(Long nroSolicitud) {
		this.nroSolicitud = nroSolicitud;
	}
}