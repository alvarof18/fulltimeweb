package com.casapazmino.fulltime.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.Asistencia;
import com.casapazmino.fulltime.model.AsistenciaHoraExtra;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.SolicitudVacacion;

@Name("asistenciaHome")
public class AsistenciaHome extends EntityHome<Asistencia> {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	DetalleHorarioHome detalleHorarioHome;

	@In(create = true)
	EmpleadoHome empleadoHome;

	@In(create = true)
	AsistenciaList asistenciaList;

	@In(create = true)
	TimbreList timbreList;

	@Logger
	Log log;

	int fechasProcesadas;
	int fechasNoProcesadas;

	public List<Asistencia> listAsistencias = new ArrayList<Asistencia>();

	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) {
			setCreatedMessage(expressions
					.createValueExpression("#{messages['mensaje.grabar']}"));
		}
		if (getUpdatedMessage() == null) {
			setUpdatedMessage(expressions
					.createValueExpression("#{messages['mensaje.actualizar']}"));
		}
		if (getDeletedMessage() == null) {
			setDeletedMessage(expressions
					.createValueExpression("#{messages['mensaje.eliminar']}"));
		}
	}

	public void setAsistenciaAsisId(Long id) {
		setId(id);
	}

	public Long getAsistenciaAsisId() {
		return (Long) getId();
	}

	@Override
	protected Asistencia createInstance() {
		Asistencia asistencia = new Asistencia();
		return asistencia;
	}

	public void wire() {
		getInstance();
		DetalleHorario detalleHorario = detalleHorarioHome.getDefinedInstance();
		if (detalleHorario != null) {
			getInstance().setDetalleHorario(detalleHorario);
		}
		Empleado empleado = empleadoHome.getDefinedInstance();
		if (empleado != null) {
			getInstance().setEmpleado(empleado);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Asistencia getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public int numeroTimbres() {
		return 1;
	}

	// if (asistenciasUpdate.size() != 0) {
	// this.updateAsistencia(asistenciasUpdate);
	// }
	// }

	public void crearListaEliminarAsistencia(List<Empleado> empleados,
			List<Calendar> fechas, List<DetalleHorario> detalleHorarios,
			String estado) {

		Calendar fechaCalendar = Calendar.getInstance();

		// Recorrer lista de empleados
		for (Empleado e : empleados) {
			// Recorrer lista de Fechas Seleccionadas
			Iterator it = fechas.iterator();
			while (it.hasNext()) {
				fechaCalendar = (Calendar) it.next();

				asistenciaList.getAsistencia().getEmpleado()
						.setCodigoEmpleado(e.getCodigoEmpleado());
				asistenciaList.getAsistencia().setFechaHorario(fechaCalendar.getTime());
//				asistenciaList.getAsistencia().setEstado(estado);

				List<Asistencia> listaAsistencias = new ArrayList<Asistencia>();
				listaAsistencias = asistenciaList.getListaAsistencias();

				if (listaAsistencias.size() != 0) {
					this.removeAsistencias(listaAsistencias);
					fechasProcesadas = fechasProcesadas
							+ listaAsistencias.size();
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void crearListaInsertarAsistencia(List<Empleado> empleados,
			List<Calendar> fechas, List<DetalleHorario> detalleHorarios,
			String estado) {

		Calendar fechaCalendar = Calendar.getInstance();
		
		for (Empleado e : empleados) {
			
			for (Calendar c : fechas) {
				
				List<Asistencia> listAsistencias = new ArrayList<Asistencia>();
				listAsistencias.clear();
				try {
					asistenciaList.getAsistencia().getEmpleado()
							.setCodigoEmpleado(e.getCodigoEmpleado());
					asistenciaList.setFechaAsistencia(c.getTime());
					asistenciaList.getAsistencia().setAsisOrden(1);

					listAsistencias = asistenciaList.getListaAsistencias();

				} catch (Exception e2) {
					FacesMessages.instance().add(
							"Error Consultando - Generar planificación ");
					e2.printStackTrace();
				}

				if (listAsistencias.size() == 0) {
					for (DetalleHorario dh : detalleHorarios) {

						Asistencia asistencia = new Asistencia();

						asistencia.setDetalleHorario(dh);
						asistencia.setEmpleado(e);
						asistencia.setCodigoEmpleado(e.getCodigoEmpleado());
						asistencia.setEntradaSalida(dh.getEntradaSalida());
						
						asistencia.setFechaHorario(c.getTime());
						asistencia.setHoraHorario(c.getTime());

						if (dh.isNocturno()) {
							fechaCalendar = Fechas.sumarRestarDias(c, 1);
						} else {
							fechaCalendar = (Calendar) c.clone();
						}
						
						SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String stringFecha = Fechas.cambiarFormato(this
								.crearFechasAsistencia(fechaCalendar, dh
										.getHora().getHours(), dh.getHora()
										.getMinutes()), "yyyy-MM-dd HH:mm:ss");

						Date fechaHoraHorario = null;
						try {
							fechaHoraHorario =  formato.parse(stringFecha);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

//						asistencia.setFechaHoraHorario(this
//								.crearFechasAsistencia(fechaCalendar, dh
//										.getHora().getHours(), dh.getHora()
//										.getMinutes()));
						
						asistencia.setFechaHoraHorario(fechaHoraHorario);
						
						asistencia.setFechaHoraTimbre(null);
						asistencia.setAsisOrden((int) dh.getOrden());
						asistencia.setAsisMaxMinuto(dh.getMaxMinuto());
						asistencia.setEstado(estado);

						this.setInstance(asistencia);
						this.persist();

						fechasProcesadas++;
					} // TERMINO DE RECORRER DETALLE HORARIO
				} else
					fechasNoProcesadas++; //
			} // TERMINO DE RECORRER FECHA
			
		} // TERMINO DE RECORRER EMPLEADOS

	}

	public Date crearFechasAsistencia(Calendar fechaHorario, int hora,
			int minuto) {
		Calendar fecha = Calendar.getInstance();
		fecha.set(fechaHorario.get(Calendar.YEAR),
				fechaHorario.get(Calendar.MONTH),
				fechaHorario.get(Calendar.DAY_OF_MONTH), hora, minuto, 00);
		Date fechaAsistencia = fecha.getTime();
		return fechaAsistencia;
	}
	
	public void insertaAsistenciaMultiple(
			Empleado empleado, Calendar fecha,
			List<DetalleHorario> detalleHorarios, String estado) {

			Calendar fechaCalendar = Calendar.getInstance();
			
			List<Asistencia> listAsistenciasmul = new ArrayList<Asistencia>();
			listAsistenciasmul.clear();
			
			try {		
					//instanciar en nulo los parametros					
					asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(null);
//					asistenciaList.setFechaAsistenciaNocturno(null);
					asistenciaList.setFechaAsistencia(null);
					asistenciaList.getAsistencia().setAsisOrden(null);				
				
				asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(empleado.getCodigoEmpleado());
				asistenciaList.getAsistencia().setFechaHorario(fecha.getTime());				
					
				asistenciaList.refresh();
				listAsistenciasmul = asistenciaList.getListaAsistencias();				

			} catch (Exception e2) {
				//FacesMessages.instance().add("Error Consultando - Generar planificación ");
				e2.printStackTrace();
			}

			if (listAsistenciasmul.size() == 0) {
				
					for (DetalleHorario dh : detalleHorarios) {

						Asistencia asistencia = new Asistencia();

						asistencia.setDetalleHorario(dh);
						asistencia.setEmpleado(empleado);
						asistencia.setCodigoEmpleado(empleado.getCodigoEmpleado());
						asistencia.setEntradaSalida(dh.getEntradaSalida());
						
						asistencia.setFechaHorario(fecha.getTime());
						asistencia.setHoraHorario(fechaCalendar.getTime());						
						
						if (dh.isNocturno()) {							
							fechaCalendar = Fechas.sumarRestarDias(fecha, 1);							
						} else {
							fechaCalendar = fecha;							
						}
						
						SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String stringFecha = Fechas.cambiarFormato(this
								.crearFechasAsistencia(fechaCalendar, dh
										.getHora().getHours(), dh.getHora()
										.getMinutes()), "yyyy-MM-dd HH:mm:ss");

						Date fechaHoraHorario = null;
						try {
							fechaHoraHorario =  formato.parse(stringFecha);
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

//						asistencia.setFechaHoraHorario(this
//								.crearFechasAsistencia(fechaCalendar, dh
//										.getHora().getHours(), dh.getHora()
//										.getMinutes()));
						
						asistencia.setFechaHoraHorario(fechaHoraHorario);
						
						asistencia.setFechaHoraTimbre(null);
						asistencia.setAsisOrden((int) dh.getOrden());
						asistencia.setAsisMaxMinuto(dh.getMaxMinuto());
						asistencia.setEstado(estado);
						
					//	listAsistencias.add(asistencia);

						this.setInstance(asistencia);
						
						this.persist();

						fechasProcesadas++;
					} 
					
				} else {					
					//new method insetar nuevamente los datos modificados										
					boolean actualizar=false;
					
					Vector<Long> actual= new Vector <Long>();
					for(Asistencia li :listAsistenciasmul){
						actual.addElement(li.getDetalleHorario().getDehoId());						
					}
					
					Vector <Long> nuevo= new Vector <Long>();
					for (DetalleHorario dh : detalleHorarios) {
						nuevo.addElement(dh.getDehoId());
					}
					
					if(actual.size()==nuevo.size()){						
						
						actualizar=true;
							for(int j=0;j<actual.size();j++){								
								if(actual.elementAt(j)==nuevo.elementAt(0)&&listAsistenciasmul.get(j).getEstado().equals(estado)){
									actualizar=false;
									break;
								}
							}					
						
					}else{
						actualizar=true;
					}				
					
						
					if(actualizar){
						for(Asistencia li :listAsistenciasmul){							
							if(li.getEstado().equals("V")){
								if(estado.equals("V")){
									actualizar=false;
									break;
								}else{
									actualizar=true;
									break;
								}								
							}else{
								if(li.getEstado().equals("P")){
									if(estado.equals("P")){
										actualizar=false;
									}else{
										actualizar=true;
										break;
									}									
								}else{
									actualizar=true;
									break;
								}
							}
						}
					}
					
					
//					if(!actualizar){
//						for(Asistencia li :listAsistenciasmul){	
//							if(li.getEstado().equals(estado)){
//								actualizar=false;
//							}else{
//								actualizar=true;
//								break;
//							}
//						}
//					}
					
					
					log.info("estado...............................................................from asistencia....................:"+estado+"***********autorizar:"+actualizar);
					
					if(!actualizar){						
						fechasNoProcesadas=fechasNoProcesadas+nuevo.size();						
					}
					
					if(actualizar){
						this.removeAsistencias(listAsistenciasmul);	
						
						for (DetalleHorario dh : detalleHorarios) {						
								Asistencia asistencia = new Asistencia();

								asistencia.setDetalleHorario(dh);
								asistencia.setEmpleado(empleado);
								asistencia.setCodigoEmpleado(empleado.getCodigoEmpleado());
								asistencia.setEntradaSalida(dh.getEntradaSalida());
								
								asistencia.setFechaHorario(fecha.getTime());
								asistencia.setHoraHorario(fechaCalendar.getTime());						
								
								if (dh.isNocturno()) {							
									fechaCalendar = Fechas.sumarRestarDias(fecha, 1);							
								} else {
									fechaCalendar = fecha;							
								}
								
								SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								String stringFecha = Fechas.cambiarFormato(this
										.crearFechasAsistencia(fechaCalendar, dh
												.getHora().getHours(), dh.getHora()
												.getMinutes()), "yyyy-MM-dd HH:mm:ss");

								Date fechaHoraHorario = null;
								try {
									fechaHoraHorario =  formato.parse(stringFecha);
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								
								asistencia.setFechaHoraHorario(fechaHoraHorario);
								
								asistencia.setFechaHoraTimbre(null);
								asistencia.setAsisOrden((int) dh.getOrden());
								asistencia.setAsisMaxMinuto(dh.getMaxMinuto());
								asistencia.setEstado(estado);

								this.setInstance(asistencia);						
								this.persist();

								fechasProcesadas++;					
							}						
						}
					//}
					//new method				
				}					
				
			} // TERMINO DE RECORRER FECHA

	
	public void eliminarAsistenciaMultiple(Empleado empleado, Calendar fecha) {		
		
		List<Asistencia> listAsistenciasmul = new ArrayList<Asistencia>();
		listAsistenciasmul.clear();	
		
		try {		
				//instanciar en nulo los parametros					
				asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(null);
//				asistenciaList.setFechaAsistenciaNocturno(null);
				asistenciaList.setFechaAsistencia(null);
				asistenciaList.getAsistencia().setAsisOrden(null);					
				//
			
			asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(empleado.getCodigoEmpleado());
			asistenciaList.getAsistencia().setFechaHorario(fecha.getTime());			
			
			asistenciaList.refresh();
			listAsistenciasmul = asistenciaList.getListaAsistencias();				

		} catch (Exception e2) {
			//FacesMessages.instance().add("Error Consultando - Generar planificación ");
			e2.printStackTrace();
		}

		
		if (listAsistenciasmul.size() > 0) {
			
			List<Asistencia> eliminar = new ArrayList<Asistencia>();
			boolean elim=true;
			
			for(Asistencia a:listAsistenciasmul){
				if(a.getEstado().equals("V")){					
					//***comprobar solicitudes
					SolicitudVacacionList solicitudVacacionList= (SolicitudVacacionList) Component.getInstance("solicitudVacacionList", true);
					List <SolicitudVacacion> sol=new ArrayList <SolicitudVacacion>();
						
					solicitudVacacionList.setAutorizado("si");
					solicitudVacacionList.getSolicitudVacacion().getEmpleadoByEmplId().setCodigoEmpleado(empleado.getCodigoEmpleado());
					solicitudVacacionList.setMaxResults(null);
					
					sol=solicitudVacacionList.getResultList();
					
					
					if(sol.size()>0){							
						for(SolicitudVacacion s: sol){
							Date init=s.getFechaDesde();
							Date end=s.getFechaHasta();
							if((fecha.getTime().compareTo(init)==0||fecha.getTime().compareTo(end)==0)||(fecha.getTime().after(init)&&fecha.getTime().before(end))){
								elim=false;
								break;
							}
						}
					}
					//**					
					break;
				}
				else{
					if(a.getEstado().equals("P")){
						
						PermisoList permisoList= (PermisoList) Component.getInstance("permisoList", true);
						List <Permiso> permiso=new ArrayList <Permiso>();
							
						permisoList.setAutorizado("si");
						permisoList.getPermiso().getEmpleadoByEmplId().setCodigoEmpleado(empleado.getCodigoEmpleado());
						permisoList.getPermiso().setFechaDesde(null);
						permisoList.setMaxResults(null);
						
						permiso=permisoList.getResultList();						
						
						if(permiso.size()>0){							
							for(Permiso per: permiso){
								Date init=per.getFechaDesde();
								Date end=per.getFechaHasta();
								if((fecha.getTime().compareTo(init)==0||fecha.getTime().compareTo(end)==0)||(fecha.getTime().after(init)&&fecha.getTime().before(end))){
									elim=false;
									break;
								}
							}
						}
						if(!elim){
							break;
						}
					}else{
						elim=true;
						break;//eliminar.add(a);
					}					
				}
			}
			
			if(elim)
				this.removeAsistencias(listAsistenciasmul);
			else
				this.removeAsistencias(eliminar);			
		}
}
	

	@SuppressWarnings("deprecation")
	public List<Asistencia> crearListaInsertarAsistenciaMultiple(
			List<Empleado> empleados, List<Calendar> fechas,
			List<DetalleHorario> detalleHorarios, String estado) {

		Calendar fechaCalendar = Calendar.getInstance();

		// Recorrer lista de empleados
		for (Empleado em : empleados) {
			// listPersistAsistencias.clear();
			// Recorrer lista de Fechas Seleccionadas
			for (Calendar ca : fechas) {
				
				List<Asistencia> listAsistenciasmul = new ArrayList<Asistencia>();
				listAsistenciasmul.clear();
				try {
					asistenciaList.getAsistencia().getEmpleado()
							.setCodigoEmpleado(em.getCodigoEmpleado());
					asistenciaList.setFechaAsistencia(ca.getTime());
					asistenciaList.getAsistencia().setAsisOrden(1);

					listAsistenciasmul = asistenciaList.getListaAsistencias();

				} catch (Exception e2) {
					FacesMessages.instance().add(
							"Error Consultando - Generar planificación ");
					e2.printStackTrace();
				}

				if (listAsistenciasmul.size() == 0) {
					for (DetalleHorario dh : detalleHorarios) {

						Asistencia asistencia = new Asistencia();

						asistencia.setDetalleHorario(dh);
						asistencia.setEmpleado(em);
						asistencia.setCodigoEmpleado(em.getCodigoEmpleado());
						asistencia.setEntradaSalida(dh.getEntradaSalida());
						
						if (dh.isNocturno()) {
							fechaCalendar = Fechas.sumarRestarDias(ca, 1);
						} else {
							fechaCalendar = (Calendar) ca.clone();
						}
						
						asistencia.setFechaHoraHorario(this
								.crearFechasAsistencia(fechaCalendar, dh
										.getHora().getHours(), dh.getHora()
										.getMinutes()));
						asistencia.setFechaHoraTimbre(null);
						asistencia.setAsisOrden((int) dh.getOrden());
						asistencia.setAsisMaxMinuto(dh.getMaxMinuto());
						asistencia.setEstado(estado);

						listAsistencias.add(asistencia);

						// this.setInstance(asistencia);
						// this.persist();

						fechasProcesadas++;

					}  // TERMINO DE RECORRER DETALLE HORARIOS
				}else
						fechasNoProcesadas++;
			} // TERMINO DE RECORRER FECHA

		} // TERMINO DE RECORRER EMPLEADOS
		return listAsistencias;

	}
	
	public void removeAsistencias(List<Asistencia> listAsistencias) {
		for (Asistencia asistencia : listAsistencias) {
			if (!asistencia.getEstado().equals("V"))
			this.setInstance(asistencia);
			this.remove();
		}
	}

	public void persistAsistencia(List<Asistencia> asistencias) {

			for (Asistencia asistencia : asistencias) {
				
				this.setInstance(asistencia);
				this.persist();
			}
	}

	public void encerarContadorFechas() {
		setFechasProcesadas(0);
		setFechasNoProcesadas(0);
	}

	public void grabarAsistencia() {
		this.getInstance().setCodigoEmpleado(
				this.getInstance().getEmpleado().getCodigoEmpleado());
		this.getInstance().setEstado(Constantes.ASISTENCIA_MANUAL);
		this.persist();
	}

	@Override
	public String remove() {
		try {
			FacesMessages.instance().clear();
			super.remove();
		} catch (Exception e) {
			FacesMessages.instance().add("Error al borrar registro");
		}
		return "removed";
	}

	@Override
	// @Transactional
	// @Transactional(TransactionPropagationType.NEVER)
	// @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String persist() {
		// try {
		// Transaction.instance().setTransactionTimeout(360000);
		// } catch (SystemException e) {
		// e.printStackTrace();
		// }

		FacesMessages.instance().clear();
		return super.persist();
	}

	public String updateAsistencia(List<Asistencia> asistencias) {

		try {

			for (Asistencia asistencia : asistencias) {
				this.setInstance(asistencia);
				return this.update();
				// TODO: REVISAR ESTO PARECE QUE ACTUALIZA TODA LA LISTA DE UNA
				// SOLA NO REGISTRO POR REGISTRO
			}

			return "updated";

		} catch (Exception e) {
			FacesMessages.instance().add("Error al autorizar");
			e.printStackTrace();
		}

		return "updated";
	}

	public List<AsistenciaHoraExtra> getAsistenciaHoraExtras() {
		return getInstance() == null ? null
				: new ArrayList<AsistenciaHoraExtra>(getInstance()
						.getAsistenciaHoraExtras());
	}

	public int getFechasProcesadas() {
		return fechasProcesadas;
	}

	public void setFechasProcesadas(int fechasProcesadas) {
		this.fechasProcesadas = fechasProcesadas;
	}

	public int getFechasNoProcesadas() {
		return fechasNoProcesadas;
	}

	public void setFechasNoProcesadas(int fechasNoProcesadas) {
		this.fechasNoProcesadas = fechasNoProcesadas;
	}

	/*
	 * public void insertarAsistencia(Asistencia asistencia){
	 * entityManager.persist(asistencia); }
	 * 
	 * public void eliminarAsistencia(Asistencia asistencia) {
	 * entityManager.remove(asistencia); }
	 * 
	 * public void eliminarAsistencias(List<Asistencia> listaAsistencia) { for
	 * (Asistencia asistencia : listaAsistencia) { this.setInstance(asistencia);
	 * this.remove(); } }
	 */
	
// Insertar Planificacion multiple 
	
	public void insertaAsistencia(
			Empleado empleado, Calendar fecha,
			List<DetalleHorario> detalleHorarios, String estado) {

			Calendar fechaCalendar = Calendar.getInstance();
			
			List<Asistencia> listAsistenciasmul = new ArrayList<Asistencia>();
			listAsistenciasmul.clear();
			
			try {		
					//instanciar en nulo los parametros					
					asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(null);
					asistenciaList.setFechaAsistencia(null);
					asistenciaList.getAsistencia().setAsisOrden(null);					
					//
				
				asistenciaList.getAsistencia().getEmpleado().setCodigoEmpleado(empleado.getCodigoEmpleado());
				asistenciaList.setFechaAsistencia(fecha.getTime());
				
				asistenciaList.refresh();		
				listAsistenciasmul = asistenciaList.getListaAsistencias();				

			} catch (Exception e2) {
				//FacesMessages.instance().add("Error Consultando - Generar planificación ");
				e2.printStackTrace();
			}

			if (listAsistenciasmul.size() == 0) {
				
					for (DetalleHorario dh : detalleHorarios) {

						Asistencia asistencia = new Asistencia();

						asistencia.setDetalleHorario(dh);
						asistencia.setEmpleado(empleado);
						asistencia.setCodigoEmpleado(empleado.getCodigoEmpleado());
						asistencia.setEntradaSalida(dh.getEntradaSalida());
						
						if (dh.isNocturno()) {
							fechaCalendar = Fechas.sumarRestarDias(fecha, 1);
						} else {
							fechaCalendar = (Calendar) fecha.clone();
						}
						asistencia.setFechaHorario(fechaCalendar.getTime());
						asistencia.setHoraHorario(fechaCalendar.getTime());
						
						asistencia.setFechaHoraHorario(this
								.crearFechasAsistencia(fechaCalendar, dh
										.getHora().getHours(), dh.getHora()
										.getMinutes()));
						asistencia.setFechaHoraTimbre(null);
						asistencia.setAsisOrden((int) dh.getOrden());
						asistencia.setAsisMaxMinuto(dh.getMaxMinuto());
						asistencia.setEstado(estado);
						
					//	listAsistencias.add(asistencia);

						this.setInstance(asistencia);
						this.persist();

						fechasProcesadas++;
					}  
					
				} else {					
						//new method insetar nuevamente los datos modificados						
						this.removeAsistencias(listAsistenciasmul);						
						
						for (DetalleHorario dh : detalleHorarios) {

							Asistencia asistencia = new Asistencia();

							asistencia.setDetalleHorario(dh);
							asistencia.setEmpleado(empleado);
							asistencia.setCodigoEmpleado(empleado.getCodigoEmpleado());
							asistencia.setEntradaSalida(dh.getEntradaSalida());
							
							if (dh.isNocturno()) {
								fechaCalendar = Fechas.sumarRestarDias(fecha, 1);
							} else {
								fechaCalendar = (Calendar) fecha.clone();
							}
							asistencia.setFechaHorario(fechaCalendar.getTime());
							asistencia.setHoraHorario(fechaCalendar.getTime());
							
							asistencia.setFechaHoraHorario(this
									.crearFechasAsistencia(fechaCalendar, dh
											.getHora().getHours(), dh.getHora()
											.getMinutes()));
							asistencia.setFechaHoraTimbre(null);
							asistencia.setAsisOrden((int) dh.getOrden());
							asistencia.setAsisMaxMinuto(dh.getMaxMinuto());
							asistencia.setEstado(estado);

							//listAsistencias.add(asistencia);

							this.setInstance(asistencia);
							this.persist();

							fechasProcesadas++;
						} 						
						//new method				
					}			
				
			} // TERMINO DE RECORRER FECHA

	public boolean verificar_asistencia(Empleado empleado, Calendar fecha) {
		
		List<Asistencia> listAsistenciasmul = new ArrayList<Asistencia>();
		listAsistenciasmul.clear();
		try {
			asistenciaList.getAsistencia().getEmpleado()
					.setCodigoEmpleado(empleado.getCodigoEmpleado());
			asistenciaList.setFechaAsistencia(fecha.getTime());
			//asistenciaList.getAsistencia().setAsisOrden(1);

			listAsistenciasmul = asistenciaList.getListaAsistencias();

		} catch (Exception e2) {
			FacesMessages.instance().add(
					"Error Consultando - Generar planificación ");
			e2.printStackTrace();
		}

		if (listAsistenciasmul.size() == 0) {
			return false;					
		}else{
			return true;
		}			
			
	} 	
}
