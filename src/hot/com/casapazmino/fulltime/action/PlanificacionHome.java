package com.casapazmino.fulltime.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Horario;
import com.casapazmino.fulltime.model.Planificacion;


@Name("planificacionHome")
public class PlanificacionHome extends EntityHome<Planificacion> {

	private static final long serialVersionUID = -1145375607069970393L;
	
	@In(create = true)
	EmpleadoHome empleadoHome;
	@In(create = true)
	HorarioHome horarioHome;
	
	@In(create = true)
	PlanificacionList planificacionList;
	
	@In(create = true)
	HorarioList horarioList;
	
	@In(create = true)
	DetalleHorarioList detalleHorarioList;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	public Boolean dia1;
	public Boolean dia2;
	private Boolean dia3;
	private Boolean dia4;
	private Boolean dia5;
	private Boolean dia6;
	private Boolean dia7;
	private Boolean dia8;
	private Boolean dia9;
	private Boolean dia10;
	private Boolean dia11;
	private Boolean dia12;
	private Boolean dia13;
	private Boolean dia14;
	private Boolean dia15;
	private Boolean dia16;
	private Boolean dia17;
	private Boolean dia18;
	private Boolean dia19;
	private Boolean dia20;
	private Boolean dia21;
	private Boolean dia22;
	private Boolean dia23;
	private Boolean dia24;
	private Boolean dia25;
	private Boolean dia26;
	private Boolean dia27;
	private Boolean dia28;
	private Boolean dia29;
	private Boolean dia30;
	private Boolean dia31;
	
	
	public Date fechaSeleccionada;
	private List<Horario> horarios;

	@Logger
	Log log;
	private List<Empleado> listaEmpleados;
	public List<Planificacion> plani= new ArrayList<Planificacion>();

	public void setPlanificacionId(Integer id) {
		setId(id);
	}

	public Integer getPlanificacionId() {
		return (Integer) getId();
	}

	@Override
	protected Planificacion createInstance() {
		Planificacion planificacion = new Planificacion();
		return planificacion;
	}
	
	private Boolean access;

	public void wire() {
		getInstance();
		
		/*this.horarios=horarioList.getListaHorario();
		log.info("HOARIOS PLANIFICACIONHOME SIZE............................................................................:"+this.horarios.size());
		this.access=gestionPermisoVacacion.buscarModificarPlanificacion();*/
//		this.validarDia1();
//		this.validarDia2();
//		this.validarDia3();
//		this.validarDia4();
//		this.validarDia5();
//		this.validarDia6();
//		this.validarDia7();
//		this.validarDia8();
//		this.validarDia9();
//		this.validarDia10();
//		this.validarDia11();
//		this.validarDia12();
//		this.validarDia13();
//		this.validarDia14();
//		this.validarDia15();
//		this.validarDia16();
//		this.validarDia17();
//		this.validarDia18();
//		this.validarDia19();
//		this.validarDia20();
//		this.validarDia21();
//		this.validarDia22();
//		this.validarDia23();
//		this.validarDia24();
//		this.validarDia25();
//		this.validarDia26();
//		this.validarDia27();
//		this.validarDia28();
//		this.validarDia29();
//		this.validarDia30();
//		this.validarDia31();
		
		
		
//		Empleado empleado = empleadoHome.getDefinedInstance();
//		if (empleado != null) {
//			getInstance().setEmpleado(empleado);
//		}
//		Horario horarioByHora10Id = horarioHome.getDefinedInstance();
//		if (horarioByHora10Id != null) {
//			getInstance().setHorarioByHora10Id(horarioByHora10Id);
//		}
//		Horario horarioByHora11Id = horarioHome.getDefinedInstance();
//		if (horarioByHora11Id != null) {
//			getInstance().setHorarioByHora11Id(horarioByHora11Id);
//		}
//		Horario horarioByHora12Id = horarioHome.getDefinedInstance();
//		if (horarioByHora12Id != null) {
//			getInstance().setHorarioByHora12Id(horarioByHora12Id);
//		}
//		Horario horarioByHora13Id = horarioHome.getDefinedInstance();
//		if (horarioByHora13Id != null) {
//			getInstance().setHorarioByHora13Id(horarioByHora13Id);
//		}
//		Horario horarioByHora14Id = horarioHome.getDefinedInstance();
//		if (horarioByHora14Id != null) {
//			getInstance().setHorarioByHora14Id(horarioByHora14Id);
//		}
//		Horario horarioByHora15Id = horarioHome.getDefinedInstance();
//		if (horarioByHora15Id != null) {
//			getInstance().setHorarioByHora15Id(horarioByHora15Id);
//		}
//		Horario horarioByHora16Id = horarioHome.getDefinedInstance();
//		if (horarioByHora16Id != null) {
//			getInstance().setHorarioByHora16Id(horarioByHora16Id);
//		}
//		Horario horarioByHora17Id = horarioHome.getDefinedInstance();
//		if (horarioByHora17Id != null) {
//			getInstance().setHorarioByHora17Id(horarioByHora17Id);
//		}
//		Horario horarioByHora18Id = horarioHome.getDefinedInstance();
//		if (horarioByHora18Id != null) {
//			getInstance().setHorarioByHora18Id(horarioByHora18Id);
//		}
//		Horario horarioByHora19Id = horarioHome.getDefinedInstance();
//		if (horarioByHora19Id != null) {
//			getInstance().setHorarioByHora19Id(horarioByHora19Id);
//		}
//		Horario horarioByHora1Id = horarioHome.getDefinedInstance();
//		if (horarioByHora1Id != null) {
//			getInstance().setHorarioByHora1Id(horarioByHora1Id);
//		}
//		Horario horarioByHora20Id = horarioHome.getDefinedInstance();
//		if (horarioByHora20Id != null) {
//			getInstance().setHorarioByHora20Id(horarioByHora20Id);
//		}
//		Horario horarioByHora21Id = horarioHome.getDefinedInstance();
//		if (horarioByHora21Id != null) {
//			getInstance().setHorarioByHora21Id(horarioByHora21Id);
//		}
//		Horario horarioByHora22Id = horarioHome.getDefinedInstance();
//		if (horarioByHora22Id != null) {
//			getInstance().setHorarioByHora22Id(horarioByHora22Id);
//		}
//		Horario horarioByHora23Id = horarioHome.getDefinedInstance();
//		if (horarioByHora23Id != null) {
//			getInstance().setHorarioByHora23Id(horarioByHora23Id);
//		}
//		Horario horarioByHora24Id = horarioHome.getDefinedInstance();
//		if (horarioByHora24Id != null) {
//			getInstance().setHorarioByHora24Id(horarioByHora24Id);
//		}
//		Horario horarioByHora25Id = horarioHome.getDefinedInstance();
//		if (horarioByHora25Id != null) {
//			getInstance().setHorarioByHora25Id(horarioByHora25Id);
//		}
//		Horario horarioByHora26Id = horarioHome.getDefinedInstance();
//		if (horarioByHora26Id != null) {
//			getInstance().setHorarioByHora26Id(horarioByHora26Id);
//		}
//		Horario horarioByHora27Id = horarioHome.getDefinedInstance();
//		if (horarioByHora27Id != null) {
//			getInstance().setHorarioByHora27Id(horarioByHora27Id);
//		}
//		Horario horarioByHora28Id = horarioHome.getDefinedInstance();
//		if (horarioByHora28Id != null) {
//			getInstance().setHorarioByHora28Id(horarioByHora28Id);
//		}
//		Horario horarioByHora29Id = horarioHome.getDefinedInstance();
//		if (horarioByHora29Id != null) {
//			getInstance().setHorarioByHora29Id(horarioByHora29Id);
//		}
//		Horario horarioByHora2Id = horarioHome.getDefinedInstance();
//		if (horarioByHora2Id != null) {
//			getInstance().setHorarioByHora2Id(horarioByHora2Id);
//		}
//		Horario horarioByHora30Id = horarioHome.getDefinedInstance();
//		if (horarioByHora30Id != null) {
//			getInstance().setHorarioByHora30Id(horarioByHora30Id);
//		}
//		Horario horarioByHora31Id = horarioHome.getDefinedInstance();
//		if (horarioByHora31Id != null) {
//			getInstance().setHorarioByHora31Id(horarioByHora31Id);
//		}
//		Horario horarioByHora3Id = horarioHome.getDefinedInstance();
//		if (horarioByHora3Id != null) {
//			getInstance().setHorarioByHora3Id(horarioByHora3Id);
//		}
//		Horario horarioByHora4Id = horarioHome.getDefinedInstance();
//		if (horarioByHora4Id != null) {
//			getInstance().setHorarioByHora4Id(horarioByHora4Id);
//		}
//		Horario horarioByHora5Id = horarioHome.getDefinedInstance();
//		if (horarioByHora5Id != null) {
//			getInstance().setHorarioByHora5Id(horarioByHora5Id);
//		}
//		Horario horarioByHora6Id = horarioHome.getDefinedInstance();
//		if (horarioByHora6Id != null) {
//			getInstance().setHorarioByHora6Id(horarioByHora6Id);
//		}
//		Horario horarioByHora7Id = horarioHome.getDefinedInstance();
//		if (horarioByHora7Id != null) {
//			getInstance().setHorarioByHora7Id(horarioByHora7Id);
//		}
//		Horario horarioByHora8Id = horarioHome.getDefinedInstance();
//		if (horarioByHora8Id != null) {
//			getInstance().setHorarioByHora8Id(horarioByHora8Id);
//		}
//		Horario horarioByHora9Id = horarioHome.getDefinedInstance();
//		if (horarioByHora9Id != null) {
//			getInstance().setHorarioByHora9Id(horarioByHora9Id);
//		}
	}
	
	public Date transformaFecha(String fecha, String formato){
		SimpleDateFormat formatoDelTexto = new SimpleDateFormat(formato);
		Date fechad = null;
		try {

		fechad = formatoDelTexto.parse(fecha);

		} catch (Exception e) {
			FacesMessages.instance().add("Error al autorizar");
			e.printStackTrace();
		}
		return fechad;
	}

	public void validarDia1(){
		String dia="01";
		this.setDia1(this.buscarAsistencia(dia));
	} 

	public void validarDia2(){
		String dia="02";
		this.setDia2(this.buscarAsistencia(dia));
	} 
	public void validarDia3(){
		String dia="03";
		this.setDia3(this.buscarAsistencia(dia));
	} 

	public void validarDia4(){
		String dia="04";
		this.setDia4(this.buscarAsistencia(dia));
	} 
	public void validarDia5(){
		String dia="05";
		this.setDia5(this.buscarAsistencia(dia));
	} 

	public void validarDia6(){
		String dia="06";
		this.setDia6(this.buscarAsistencia(dia));
	} 
	public void validarDia7(){
		String dia="07";
		this.setDia7(this.buscarAsistencia(dia));
	} 

	public void validarDia8(){
		String dia="08";
		this.setDia8(this.buscarAsistencia(dia));
	} 
	public void validarDia9(){
		String dia="09";
		this.setDia9(this.buscarAsistencia(dia));
	} 

	public void validarDia10(){
		String dia="10";
		this.setDia10(this.buscarAsistencia(dia));
	} 
	public void validarDia11(){
		String dia="11";
		this.setDia11(this.buscarAsistencia(dia));
	} 

	public void validarDia12(){
		String dia="12";
		this.setDia12(this.buscarAsistencia(dia));
	} 
	public void validarDia13(){
		String dia="13";
		this.setDia13(this.buscarAsistencia(dia));
	} 

	public void validarDia14(){
		String dia="14";
		this.setDia14(this.buscarAsistencia(dia));
	} 
	public void validarDia15(){
		String dia="15";
		this.setDia15(this.buscarAsistencia(dia));
	} 

	public void validarDia16(){
		String dia="16";
		this.setDia16(this.buscarAsistencia(dia));
	} 
	public void validarDia17(){
		String dia="17";
		this.setDia17(this.buscarAsistencia(dia));
	} 

	public void validarDia18(){
		String dia="18";
		this.setDia18(this.buscarAsistencia(dia));
	} 
	public void validarDia19(){
		String dia="19";
		this.setDia19(this.buscarAsistencia(dia));
	} 

	public void validarDia20(){
		String dia="20";
		this.setDia20(this.buscarAsistencia(dia));
	} 
	public void validarDia21(){
		String dia="21";
		this.setDia21(this.buscarAsistencia(dia));
	} 

	public void validarDia22(){
		String dia="22";
		this.setDia22(this.buscarAsistencia(dia));
	} 
	public void validarDia23(){
		String dia="23";
		this.setDia23(this.buscarAsistencia(dia));
	} 

	public void validarDia24(){
		String dia="24";
		this.setDia24(this.buscarAsistencia(dia));
	} 
	public void validarDia25(){
		String dia="25";
		this.setDia25(this.buscarAsistencia(dia));
	} 

	public void validarDia26(){
		String dia="26";
		this.setDia26(this.buscarAsistencia(dia));
	} 
	public void validarDia27(){
		String dia="27";
		this.setDia27(this.buscarAsistencia(dia));
	} 

	public void validarDia28(){
		String dia="28";
		this.setDia28(this.buscarAsistencia(dia));
	} 
	public void validarDia29(){
		String dia="29";
		this.setDia29(this.buscarAsistencia(dia));
	} 

	public void validarDia30(){
		String dia="30";
		this.setDia30(this.buscarAsistencia(dia));
	} 
	public void validarDia31(){
		String dia="31";
		this.setDia31(this.buscarAsistencia(dia));
	} 

	

	public Boolean buscarAsistencia(String dia){
		String formato="yyyy-MM-dd";
		Calendar aux=Calendar.getInstance();
		String fecha=null;
		Integer year=this.getInstance().getAnio();
		Integer mes=this.getInstance().getMes();
		fecha=year.toString()+"-"+mes.toString()+"-"+dia;
		fechaSeleccionada=transformaFecha(fecha, formato);
		aux.setTime(fechaSeleccionada);
						
		AsistenciaList asistenciaList = (AsistenciaList) Component.getInstance("asistenciaList", true);
		List<Asistencia> asistencia=new ArrayList<Asistencia>();
		asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(this.getInstance().getEmpleadoByEmplId().getCodigoEmpleado());
		asistenciaList.setFechaAsistencia(aux.getTime());
		asistencia=asistenciaList.getListaAsistencias();
		
		if(asistencia.size()!=0){
		return true;
		}else{
			return false;
			}
		
	}
	
	public Long buscarDetallehorario(Horario horario){
		Long idDetalleHorario=null;
		List<DetalleHorario> detallehorario=new ArrayList<DetalleHorario>();
		DetalleHorarioList detalleHorarioList = (DetalleHorarioList) Component.getInstance("detalleHorarioList", true);
		detalleHorarioList.getDetalleHorario().getHorario().setHoraId(horario.getHoraId());
		detallehorario=detalleHorarioList.getListaDetalleHorario();
		for (DetalleHorario detalleHorario2 : detallehorario) {
			idDetalleHorario=detalleHorario2.getDehoId();
		}
		
		return idDetalleHorario;
		
	}
	
	

	public boolean isWired() {
		return true;
	}

	public Planificacion getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	

	
	public List<Planificacion> buscarPlanificacion(List<Empleado> empleados,Integer mes, Integer anio){
		PlanificacionList planificacionList = (PlanificacionList) Component.getInstance("planificacionList", true);
		
		
		for (Empleado em : empleados) {
			planificacionList.getPlanificacion().getEmpleadoByEmplId().setEmplId(em.getEmplId());
			planificacionList.getPlanificacion().setMes(mes);
			planificacionList.getPlanificacion().setAnio(anio);
			plani.addAll(planificacionList.getListaPlanificacion());
			for(Planificacion pla : plani){
				//log.info("plani"+pla);
			}
		}
		 
		return plani;
	}
	
	
	//create planification
	public List<Planificacion> crearListaPlanificacion(
			List<Empleado> empleados,
			Integer anio, Integer mes) {			
		// Recorrer lista de empleados
		plani.clear();
		for (Empleado em : empleados) {
			// listPersistAsistencias.clear();
			// Recorrer lista de Fechas Seleccionadas
								

						Planificacion planificacion = new Planificacion();

						planificacion.setEmpleadoByEmplId(em);
						planificacion.setAnio(anio);
						planificacion.setMes(mes);
						
						this.setInstance(planificacion);
						this.persist();
						
						plani.add(planificacion);
						
						FacesMessages.instance().clear();
						FacesMessages.instance().add(
								"Planificación creada satisfactoriamente");

					}  // TERMINO DE RECORRER DETALLE HORARIOS
				
					return plani;
	}
	
	//new verifity planification
	
	public List<Planificacion> VerificarListaPlanificacion(	
		List<Empleado> empleados,
		Integer anio, Integer mes) {
		plani.clear();
		
		for (Empleado em : empleados) {
			
			Planificacion planificacion = new Planificacion();

			planificacion.setEmpleadoByEmplId(em);
			planificacion.setAnio(anio);
			planificacion.setMes(mes);
						
			plani.add(planificacion);
			
			}				
			return plani;
	}
	
	//end new
	
	//new method
	public List<Planificacion> eliminarListaPlanificacion(
			List<Empleado> empleados,
			Integer anio, Integer mes) {
		
			List<com.casapazmino.fulltime.model.Planificacion> listPlanificacion;					
			listPlanificacion = this.buscarPlanificacion(empleados, mes, anio);
			
			for (Planificacion planificacion : listPlanificacion) {
				this.setInstance(planificacion);
				this.remove();
				FacesMessages.instance().clear();
				FacesMessages.instance().add(
						"Planificación eliminada satisfactoriamente");
			}
		return plani;			
	}
	//
		
	public void actualizarListaPlanificacion(
			List<Empleado> empleados, Horario h1,
			Integer anio, Integer mes) {

		
		// Recorrer lista de empleados
		for (Empleado em : empleados) {
			// listPersistAsistencias.clear();
			// Recorrer lista de Fechas Seleccionadas
			
		
			
						try{

						Planificacion planificacion = new Planificacion();

						planificacion.setEmpleadoByEmplId(em);
						planificacion.setHorarioByHora1Id(h1);
						planificacion.setAnio(anio);
						planificacion.setMes(mes);
						
						this.setInstance(planificacion);
						this.update();

						FacesMessages.instance().clear();
						FacesMessages.instance().add(
								"Planificacion actualizada satisfactoriamente");
					
						} catch (Exception e) {
							FacesMessages.instance().add("Error al autorizar");
							e.printStackTrace();
						}
					
			}
	}
	
	//new method for planification per hour
	public String updatePlanificacion(List<Planificacion> planificacion) {
		FacesMessages.instance().clear();
		try {
			
			FacesMessages.instance().clear();
			for (Planificacion planific : planificacion) {
								
				this.clearInstance();
				FacesMessages.instance().clear();
				//plani.setHorarioByHora1Id(horarioHome.setId(id));
				log.info("==================================== for " + planific.getId());
				if(planific.getHorarioByHora1Id()!=null)
				{
					log.info("==================================== for 1 " + planific.getHorarioByHora1Id().getDescripcion());
					log.info("==================================== for 1 " + planific.getHorarioByHora1Id().getHoraId());					
				}
				if(planific.getHorarioByHora2Id()!=null)
				log.info("==================================== for 2 " + planific.getHorarioByHora2Id().getDescripcion());			
				if(planific.getHorarioByHora3Id()!=null)
				log.info("==================================== for 3 " + planific.getHorarioByHora3Id().getDescripcion());				
				if(planific.getHorarioByHora4Id()!=null)
				log.info("==================================== for 4 " + planific.getHorarioByHora4Id().getDescripcion());
				if(planific.getHorarioByHora5Id()!=null)
				log.info("=================================== for 5 " + planific.getHorarioByHora5Id().getDescripcion());
				if(planific.getHorarioByHora6Id()!=null)
				log.info("==================================== for 6 " + planific.getHorarioByHora6Id().getDescripcion());
				if(planific.getHorarioByHora7Id()!=null)
				log.info("==================================== for 7 " + planific.getHorarioByHora7Id().getDescripcion());
				if(planific.getHorarioByHora8Id()!=null)
				log.info("==================================== for 8 " + planific.getHorarioByHora8Id().getDescripcion());
				if(planific.getHorarioByHora9Id()!=null)
				log.info("==================================== for 9 " + planific.getHorarioByHora9Id().getDescripcion());
				if(planific.getHorarioByHora10Id()!=null)
				log.info("==================================== for 10 " + planific.getHorarioByHora10Id().getDescripcion());
				if(planific.getHorarioByHora11Id()!=null)
				log.info("==================================== for 11 " + planific.getHorarioByHora11Id().getDescripcion());
				if(planific.getHorarioByHora12Id()!=null)
				log.info("==================================== for 12 " + planific.getHorarioByHora12Id().getDescripcion());
				if(planific.getHorarioByHora13Id()!=null)
				log.info("==================================== for 13 " + planific.getHorarioByHora13Id().getDescripcion());
				if(planific.getHorarioByHora14Id()!=null)
				log.info("==================================== for 14 " + planific.getHorarioByHora14Id().getDescripcion());
				if(planific.getHorarioByHora15Id()!=null)
				log.info("==================================== for 15 " + planific.getHorarioByHora15Id().getDescripcion());
				if(planific.getHorarioByHora16Id()!=null)
				log.info("==================================== for 16 " + planific.getHorarioByHora16Id().getDescripcion());
				if(planific.getHorarioByHora17Id()!=null)
				log.info("==================================== for 17 " + planific.getHorarioByHora17Id().getDescripcion());
				if(planific.getHorarioByHora18Id()!=null)
				log.info("==================================== for 18 " + planific.getHorarioByHora18Id().getDescripcion());
				if(planific.getHorarioByHora19Id()!=null)
				log.info("==================================== for 19 " + planific.getHorarioByHora19Id().getDescripcion());
				if(planific.getHorarioByHora20Id()!=null)
				log.info("==================================== for 20 " + planific.getHorarioByHora20Id().getDescripcion());
				if(planific.getHorarioByHora21Id()!=null)
				log.info("==================================== for 21 " + planific.getHorarioByHora21Id().getDescripcion());
				if(planific.getHorarioByHora22Id()!=null)
				log.info("==================================== for 22" + planific.getHorarioByHora22Id().getDescripcion());
				if(planific.getHorarioByHora23Id()!=null)
				log.info("==================================== for 23" + planific.getHorarioByHora23Id().getDescripcion());
				if(planific.getHorarioByHora24Id()!=null)
				log.info("==================================== for 24" + planific.getHorarioByHora24Id().getDescripcion());
				if(planific.getHorarioByHora25Id()!=null)
				log.info("==================================== for 25" + planific.getHorarioByHora25Id().getDescripcion());
				if(planific.getHorarioByHora26Id()!=null)
				log.info("==================================== for 26" + planific.getHorarioByHora26Id().getDescripcion());
				if(planific.getHorarioByHora27Id()!=null)
				log.info("==================================== for 27" + planific.getHorarioByHora27Id().getDescripcion());
				if(planific.getHorarioByHora28Id()!=null)
				log.info("==================================== for 28" + planific.getHorarioByHora28Id().getDescripcion());
				if(planific.getHorarioByHora29Id()!=null)
				log.info("==================================== for 29" + planific.getHorarioByHora29Id().getDescripcion());
				if(planific.getHorarioByHora30Id()!=null)
				log.info("==================================== for 30" + planific.getHorarioByHora30Id().getDescripcion());
				if(planific.getHorarioByHora31Id()!=null)
				log.info("==================================== for 31" + planific.getHorarioByHora31Id().getDescripcion());
					
				this.setInstance(planific);
				this.update();
				
				FacesMessages.instance().add("Actualización Exitosa");
				
				/*if(this.instance.getHorarioByHora10Id() != null)
					log.info("lo que actualize" + this.instance.getHorarioByHora10Id().getDescripcion());*/
				
				log.info("lo que actualize" + this.instance.getAnio());
				log.info("lo que actualize" + this.instance.getMes());
				log.info("lo que actualize" + this.instance.getEmpleadoByEmplId().getNombre());	
												
			}

			return "updated";

		} catch (Exception e) {
		//	log.info(e.getMessage());
			//FacesMessages.instance().add(e.getMessage());
		//	e.printStackTrace();
		}
		FacesMessages.instance().add("Planificacion Exitosa");
		return "updated";
	}
	//end
	
		
	public List<Empleado> construirEmpleados(List<Planificacion> planificacion){
		for (Planificacion plani : planificacion) {
			listaEmpleados.add(plani.getEmpleadoByEmplId());
		}
		return listaEmpleados;
		
	}

	public List<Empleado> getListaEmpleados() {
		return listaEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listaEmpleados = listaEmpleados;
	}
	public Boolean getDia1() {
		return dia1;
	}

	public void setDia1(Boolean dia1) {
		this.dia1 = dia1;
	}

	public Boolean getDia2() {
		return dia2;
	}

	public void setDia2(Boolean dia2) {
		this.dia2 = dia2;
	}

	public Boolean getDia31() {
		return dia31;
	}

	public void setDia31(Boolean dia31) {
		this.dia31 = dia31;
	}

	public Boolean getDia30() {
		return dia30;
	}

	public void setDia30(Boolean dia30) {
		this.dia30 = dia30;
	}

	public Boolean getDia29() {
		return dia29;
	}

	public void setDia29(Boolean dia29) {
		this.dia29 = dia29;
	}

	public Boolean getDia28() {
		return dia28;
	}

	public void setDia28(Boolean dia28) {
		this.dia28 = dia28;
	}

	public Boolean getDia27() {
		return dia27;
	}

	public void setDia27(Boolean dia27) {
		this.dia27 = dia27;
	}

	public Boolean getDia26() {
		return dia26;
	}

	public void setDia26(Boolean dia26) {
		this.dia26 = dia26;
	}

	public Boolean getDia25() {
		return dia25;
	}

	public void setDia25(Boolean dia25) {
		this.dia25 = dia25;
	}

	public Boolean getDia24() {
		return dia24;
	}

	public void setDia24(Boolean dia24) {
		this.dia24 = dia24;
	}

	public Boolean getDia23() {
		return dia23;
	}

	public void setDia23(Boolean dia23) {
		this.dia23 = dia23;
	}

	public Boolean getDia22() {
		return dia22;
	}

	public void setDia22(Boolean dia22) {
		this.dia22 = dia22;
	}

	public Boolean getDia21() {
		return dia21;
	}

	public void setDia21(Boolean dia21) {
		this.dia21 = dia21;
	}

	public Boolean getDia20() {
		return dia20;
	}

	public void setDia20(Boolean dia20) {
		this.dia20 = dia20;
	}

	public Boolean getDia19() {
		return dia19;
	}

	public void setDia19(Boolean dia19) {
		this.dia19 = dia19;
	}

	public Boolean getDia18() {
		return dia18;
	}

	public void setDia18(Boolean dia18) {
		this.dia18 = dia18;
	}

	public Boolean getDia17() {
		return dia17;
	}

	public void setDia17(Boolean dia17) {
		this.dia17 = dia17;
	}

	public Boolean getDia16() {
		return dia16;
	}

	public void setDia16(Boolean dia16) {
		this.dia16 = dia16;
	}

	public Boolean getDia15() {
		return dia15;
	}

	public void setDia15(Boolean dia15) {
		this.dia15 = dia15;
	}

	public Boolean getDia14() {
		return dia14;
	}

	public void setDia14(Boolean dia14) {
		this.dia14 = dia14;
	}

	public Boolean getDia13() {
		return dia13;
	}

	public void setDia13(Boolean dia13) {
		this.dia13 = dia13;
	}

	public Boolean getDia12() {
		return dia12;
	}

	public void setDia12(Boolean dia12) {
		this.dia12 = dia12;
	}

	public Boolean getDia11() {
		return dia11;
	}

	public void setDia11(Boolean dia11) {
		this.dia11 = dia11;
	}

	public Boolean getDia10() {
		return dia10;
	}

	public void setDia10(Boolean dia10) {
		this.dia10 = dia10;
	}

	public Boolean getDia9() {
		return dia9;
	}

	public void setDia9(Boolean dia9) {
		this.dia9 = dia9;
	}

	public Boolean getDia8() {
		return dia8;
	}

	public void setDia8(Boolean dia8) {
		this.dia8 = dia8;
	}

	public Boolean getDia7() {
		return dia7;
	}

	public void setDia7(Boolean dia7) {
		this.dia7 = dia7;
	}

	public Boolean getDia6() {
		return dia6;
	}

	public void setDia6(Boolean dia6) {
		this.dia6 = dia6;
	}

	public Boolean getDia5() {
		return dia5;
	}

	public void setDia5(Boolean dia5) {
		this.dia5 = dia5;
	}

	public Boolean getDia4() {
		return dia4;
	}

	public void setDia4(Boolean dia4) {
		this.dia4 = dia4;
	}

	public Boolean getDia3() {
		return dia3;
	}

	public void setDia3(Boolean dia3) {
		this.dia3 = dia3;
	}

	public Boolean getAcess() {
		return this.access;
	}

	public void setAcess(Boolean access) {
		this.access = access;
	}
	
	public List<Horario> getListaHorario2(String n) {			
		if(n.equals("")){
			return null;
		}else{
			return this.horarios;
		}
	}
	
	
}
