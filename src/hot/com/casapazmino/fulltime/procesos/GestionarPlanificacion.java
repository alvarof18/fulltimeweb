package com.casapazmino.fulltime.procesos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.jboss.seam.Component;
import org.jfree.util.Log;

import com.casapazmino.fulltime.action.DetalleTipoParametroHome;
import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.action.PlantillaPlanificacion;
import com.casapazmino.fulltime.comun.Conexion;
import com.casapazmino.fulltime.comun.Fechas;
import com.casapazmino.fulltime.model.CiudadFeriado;
import com.casapazmino.fulltime.model.DetalleHorario;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.model.Horario;

public class GestionarPlanificacion {
	
	Conexion conexion;
	String dataBase;
	
	List<String> teclaFuncion;

	String E;
	String S;
	String SA;
	String EA;
	String SP;
	String EP;
	
	private List<PlantillaPlanificacion> asistenciaUpdate;
	private List<PlantillaPlanificacion> timbreUpdate;
	private List<PlantillaPlanificacion> permisoUpdateSalida;
	private List<PlantillaPlanificacion> permisoUpdateEntrada;
	
	public GestionarPlanificacion(Conexion conexion, String dataBase){
		this.conexion=conexion;
		this.dataBase=dataBase;
	}

	//**********************************MÉTODOS DE COMPARACIÓN******************************************
	
	//Comparar Registros a Ingresar en BD
	public List <PlantillaPlanificacion> CompararRegistros(List<Empleado> listaEmpleado, List<Date> listaFecha, 
			List<PlantillaPlanificacion> listaAsistencia,List<String> listaEstado, Horario horario, List<DetalleHorario> listaDetalleHorario, 
			List<PlantillaPlanificacion> listaPermiso,
			List<PlantillaPlanificacion> listaPermisoHoras, 
			List<CiudadFeriado> listaCiudadFeriado,
			List<CiudadFeriado> listaCiudadRecuperable){
		
			//Nuevo parámetro
			boolean incluirNuevoHorario=false;
			DetalleTipoParametroHome detalleTipoParametroHome = (DetalleTipoParametroHome) Component
					.getInstance("detalleTipoParametroHome", true);
			DetalleTipoParametro detalleTipoParametro = new DetalleTipoParametro();
	
			detalleTipoParametroHome.setId(new Long(99));
			detalleTipoParametro = detalleTipoParametroHome.find();
			String notificar = detalleTipoParametro.getDescripcion();
			
			if (notificar.toLowerCase().equals("activar")) {
				incluirNuevoHorario=true;
			}
			//*************************
			
			List <PlantillaPlanificacion> plantilla=new ArrayList<PlantillaPlanificacion>();
			
			List<PlantillaPlanificacion> listaAsistenciaEmpleado=new ArrayList<PlantillaPlanificacion>();
			
			List<Date> listaFechaEmpleado=new ArrayList<Date>();
			
			List<String> listaEstadoEmpleado=new ArrayList<String>();
			
			Long horaId=horario.getHoraId();
			
			for(Empleado e: listaEmpleado){
				
				Long emplId=e.getEmplId();
								
				listaAsistenciaEmpleado=getListaPlantillaAsistenciaEmpleado(emplId,listaAsistencia);
				
				if(listaAsistenciaEmpleado.size()==0){
					List<Empleado> listaIndividual=new ArrayList<Empleado>();
					listaIndividual.add(e);
					
					plantilla.addAll(CrearPlantillaPlanificacion(listaIndividual,listaFecha,listaEstado,listaDetalleHorario,listaPermiso,listaPermisoHoras,listaCiudadFeriado,listaCiudadRecuperable));
					
				}else{
					
					for(int i=0;i<listaFecha.size();i++){
						
						Date fecha=listaFecha.get(i);
						
						String estado=listaEstado.get(i);	
						
						boolean exist=false;
						
						for(PlantillaPlanificacion a: listaAsistenciaEmpleado){
							if( a.getFecha().compareTo(fecha)==0 ){
								
								if(incluirNuevoHorario){
									if( a.getDhId().equals(horaId)|| a.getEstado().trim().equals("V") || a.getEstado().trim().equals("P") ){
										exist=true;
										break;
									}
								}else{
									exist=true;
									break;
								}
								
							}
						}
						
						if(!exist){
							listaFechaEmpleado.add(fecha);
							listaEstadoEmpleado.add(estado);
						}
						
					}
					
					if(listaFechaEmpleado.size()>0){
						List<Empleado> listaIndividual=new ArrayList<Empleado>();
						listaIndividual.add(e);
						
						plantilla.addAll(CrearPlantillaPlanificacion(listaIndividual,listaFechaEmpleado,listaEstadoEmpleado,listaDetalleHorario,listaPermiso,listaPermisoHoras,listaCiudadFeriado,listaCiudadRecuperable));
					}		
					
				}			
				
				listaAsistenciaEmpleado=new ArrayList<PlantillaPlanificacion>();
				listaFechaEmpleado=new ArrayList<Date>();
				
				listaEstadoEmpleado=new ArrayList<String>();
				
			}
			
			return plantilla;
			
		}
		
	//Obtener lista de Asistencia por empleado
		private List<PlantillaPlanificacion> getListaPlantillaAsistenciaEmpleado(Long emplId, List<PlantillaPlanificacion> listaAsistencia){
			List<PlantillaPlanificacion> listaAsistenciaEmpleado=new ArrayList<PlantillaPlanificacion>();
			
			for(PlantillaPlanificacion a: listaAsistencia){
				
				Long emplIdAsistencia=a.getEmpId();
				
				if(emplIdAsistencia.equals(emplId))
					listaAsistenciaEmpleado.add(a);
				
			}
			
			return listaAsistenciaEmpleado;
		}
		
		//Obtener lista de Timbres por empleado
		private List<PlantillaPlanificacion> getListaPlantillaTimbreEmpleado(String codigoEmpleado, List<PlantillaPlanificacion> listaTimbre){
			
			List<PlantillaPlanificacion> listaTimbreEmpleado=new ArrayList<PlantillaPlanificacion>();
			
			for(PlantillaPlanificacion a: listaTimbre){
				
				String codigoEmpleadoAsistencia=a.getCodigoEmpleado();
				codigoEmpleadoAsistencia=codigoEmpleadoAsistencia.trim();
				
				if(codigoEmpleadoAsistencia.equals(codigoEmpleado))
					listaTimbreEmpleado.add(a);
				
			}
			
			return listaTimbreEmpleado;
		}
	
	//Creación de Registros a Ingresar en BD
//	public List<PlantillaPlanificacion> CrearPlantillaPlanificacion(List <Empleado> listaEmpleado, List<Date> listaFecha, List<String> listaEstado, List<DetalleHorario> listaDetalleHorario,List<PlantillaPlanificacion> listaPermiso){
//	
//		List<PlantillaPlanificacion> listaAsistencia=new ArrayList<PlantillaPlanificacion>();
//		
//		int j=0;
//		String estado="";
//		boolean existePermiso=false;
//		
//		for (Empleado e:listaEmpleado){
//			
//			j=0;
//			
//			List<PlantillaPlanificacion> listaPermisoEmpleado=filtrarPermisoEmpleado(listaPermiso, e.getEmplId());
//			
//			for( int i=0; i<listaFecha.size(); i++){
//				
//				estado=listaEstado.get(j);
//				existePermiso=existePermisoFecha(listaPermisoEmpleado,listaFecha.get(i));
//				
//				if(existePermiso)
//					estado="P";
//				
//				List<PlantillaPlanificacion> listaAsistenciaEmpleado= getAsistencia(e,listaDetalleHorario, listaFecha.get(i),estado);
//				listaAsistencia.addAll(listaAsistenciaEmpleado);
//				
//				j++;
//				
//				if (j>=listaEstado.size())
//					j=0;				
//				
//			}		
//			
//		}
//		
//		return listaAsistencia;		
//		
//	}
	
		//Creación de Registros a Ingresar en BD
		public List<PlantillaPlanificacion> CrearPlantillaPlanificacion(List <Empleado> listaEmpleado, List<Date> listaFecha, 
				List<String> listaEstado, List<DetalleHorario> listaDetalleHorario,List<PlantillaPlanificacion> listaPermiso,
				List<PlantillaPlanificacion> listasPermisoHoras, List<CiudadFeriado> listaCiudadFeriado,
				List<CiudadFeriado> listaCiudadRecuperable){
		
			List<PlantillaPlanificacion> listaAsistencia=new ArrayList<PlantillaPlanificacion>();
			
			int j=0;
			String estado="";
			boolean existePermiso=false;
			boolean esFeriado=false;
			boolean esRecuperable=false;
			
			for (Empleado e:listaEmpleado){
				
				j=0;
				
				List<PlantillaPlanificacion> listaPermisoEmpleado=filtrarPermisoEmpleado(listaPermiso, e.getEmplId());
				List<PlantillaPlanificacion> listaPermisoHorasEmpleado=filtrarPermisoEmpleado(listasPermisoHoras, e.getEmplId());
				
				for( int i=0; i<listaFecha.size(); i++){
									
					estado=listaEstado.get(j);	
					
					//Verificar dias recuperables
					esRecuperable=EsRecuperable(e,listaCiudadRecuperable,listaFecha.get(i));				
					if(esRecuperable)
						estado="FT";
					
					//Verificar dias feriados
					esFeriado=EsFeriado(e,listaCiudadFeriado,listaFecha.get(i));				
					if(esFeriado)
						estado="FD";
					
					//Verificar dias con permisos
					existePermiso=existePermisoFecha(listaPermisoEmpleado,listaFecha.get(i));				
					if(existePermiso)
						estado="P";
					
					//Verificar Horas Permiso
					String tipo=existePermisoHoraFecha(listaPermisoHorasEmpleado,listaFecha.get(i),listaDetalleHorario);
					
					List<PlantillaPlanificacion> listaAsistenciaEmpleado= getAsistencia(e,listaDetalleHorario, listaFecha.get(i),estado,tipo);
					listaAsistencia.addAll(listaAsistenciaEmpleado);
					
					j++;
					
					if (j>=listaEstado.size())
						j=0;				
					
				}		
				
			}
			
			return listaAsistencia;		
			
		}	
	
	private boolean EsFeriado(Empleado empleado, List<CiudadFeriado> listaCiudadFeriado, Date fecha){
		boolean esFeriado=false;
		
		for(CiudadFeriado cf: listaCiudadFeriado){
			if(cf.getCiudad().equals(empleado.getCiudad())){
				//System.out.println("ciudad igual: cf.getCiudad: "+cf.getCiudad().getDescripcion()+" empleado.getCiudad:"+empleado.getCiudad().getDescripcion()+" e:"+empleado.getApellido()+" "+empleado.getNombre());
				if(fecha.equals(cf.getFeriado().getFecha())){
					//System.out.println("fecha igual: fecha: "+fecha+" cf.getFeriado.fecha:"+cf.getFeriado().getFecha());
					esFeriado=true;
					break;
				}else{
					//System.out.println("fecha no es igual: fecha: "+fecha+" cf.getFeriado.fecha:"+cf.getFeriado().getFecha());
				}
			}else{
				//System.out.println("ciudad no es igual: cf.getCiudad: "+cf.getCiudad().getDescripcion()+" empleado.getCiudad:"+empleado.getCiudad().getDescripcion()+" e:"+empleado.getApellido()+" "+empleado.getNombre());
			}
		}
		
		return esFeriado;
	}
	
	private boolean EsRecuperable(Empleado empleado, List<CiudadFeriado> listaCiudadFeriado, Date fecha){
		boolean esFeriado=false;
		
		for(CiudadFeriado cf: listaCiudadFeriado){
			if(cf.getCiudad().equals(empleado.getCiudad())){
				//System.out.println("ciudad igual: cf.getCiudad: "+cf.getCiudad().getDescripcion()+" empleado.getCiudad:"+empleado.getCiudad().getDescripcion()+" e:"+empleado.getApellido()+" "+empleado.getNombre());
				if(fecha.equals(cf.getFeriado().getFechaRecupera())){
					//System.out.println("fecha igual: fecha: "+fecha+" cf.getFeriado.fecha:"+cf.getFeriado().getFecha());
					esFeriado=true;
					break;
				}else{
					//System.out.println("fecha no es igual: fecha: "+fecha+" cf.getFeriado.fecha:"+cf.getFeriado().getFecha());
				}
			}else{
				//System.out.println("ciudad no es igual: cf.getCiudad: "+cf.getCiudad().getDescripcion()+" empleado.getCiudad:"+empleado.getCiudad().getDescripcion()+" e:"+empleado.getApellido()+" "+empleado.getNombre());
			}
		}
		
		return esFeriado;
	}
	
	//Filtrar permisos de Empleado
	private List<PlantillaPlanificacion> filtrarPermisoEmpleado(List<PlantillaPlanificacion> listaPermiso, Long emplId){
		
		List<PlantillaPlanificacion> listaPermisoEmpleado=new ArrayList<PlantillaPlanificacion>();
		
		for( PlantillaPlanificacion p : listaPermiso ){
						
			if(emplId.equals(p.getEmpId())){
				listaPermisoEmpleado.add(p);
			}
			
		}
		
		return listaPermisoEmpleado;
		
	}
	
	//Filtrar permisos de Empleado
	private boolean existePermisoFecha(List<PlantillaPlanificacion> listaPermiso, Date fecha){
		
		boolean existePermiso=false;
		
		Date fechaAsistencia=getDateWithoutTime(fecha);
		
		for( PlantillaPlanificacion p : listaPermiso ){
			
			Date fechaPermisoDesde=getDateWithoutTime(p.getFechaPermisoDesde());
			Date fechaPermisoHasta=getDateWithoutTime(p.getFechaPermisoHasta());
			
			if( (fechaAsistencia.after(fechaPermisoDesde) && fechaAsistencia.before(fechaPermisoHasta) ) || 
					fechaAsistencia.equals(fechaPermisoDesde) ||
					fechaAsistencia.equals(fechaPermisoHasta) ){
								
				existePermiso=true;
				break;
				
			}
			
		}
		
		return existePermiso;
		
	}
	
	
	//Crear Lista de Plantilla Planificacion para ingresar en BD
		private List<PlantillaPlanificacion> getAsistencia(Empleado empleado, List<DetalleHorario> listaDetalleHorario, Date fecha, String estado){
				
			List<PlantillaPlanificacion> listaPlantilla= new ArrayList<PlantillaPlanificacion>();
			
			Long spEmpleadoId=empleado.getEmplId();
			String spCodigoEmpleado=empleado.getCodigoEmpleado();
			
			for(DetalleHorario dh: listaDetalleHorario){			
				
				PlantillaPlanificacion plantilla= new PlantillaPlanificacion();				
					
				Date date=fecha;				
				Calendar calendar=getCalendar(date);		
					
				if (dh.isNocturno()) {
					calendar = Fechas.sumarRestarDias(calendar, 1);
				}	
					
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String fechaCadena = Fechas.cambiarFormato(CrearFechasAsistencia(calendar, getHora(dh.getHora()), getMinuto(dh.getHora())), "yyyy-MM-dd HH:mm:ss");

				Date fechaHoraHorario = null;
				Calendar dateAndTime=null;
					
				try {
					fechaHoraHorario =  formato.parse(fechaCadena);		
					dateAndTime=getCalendar(fechaHoraHorario);
				} catch (ParseException e) {
					e.printStackTrace();
				}
					
				Long spDhId=dh.getDehoId();
				String spEntradaSalida=dh.getEntradaSalida();
				spEntradaSalida=spEntradaSalida.trim();
				int spOrden= dh.getOrden();
				int spMaxMinuto=dh.getMaxMinuto();
					
				Date spFechaHorario=date;		
				Date spHoraHorario=dateAndTime.getTime();	
					
				String cadenaFechaHoraHorario=fechaCadena;
				String cadenaFechaHorario=""+getAño(spFechaHorario)+"-"+(getMes(spFechaHorario)+1)+"-"+getDia(spFechaHorario);
				String candenaHoraHorario=""+getHora(spHoraHorario)+":"+getMinuto(spHoraHorario)+":00";
					
				plantilla.setEmpId(spEmpleadoId);
				plantilla.setDhId(spDhId);
				plantilla.setFechaHoraHorario(cadenaFechaHoraHorario);
				plantilla.setEntradaSalida(spEntradaSalida);
				plantilla.setCodigoEmpleado(spCodigoEmpleado);
				plantilla.setOrden(spOrden);
				plantilla.setMaxMinuto(spMaxMinuto);
				plantilla.setEstado(estado);
				plantilla.setFechaHorario(cadenaFechaHorario);
				plantilla.setHoraHorario(candenaHoraHorario);
					
				listaPlantilla.add(plantilla);
			
				}
				
				return listaPlantilla;
				
			}
		
		//Obtener claves primarias de lista de empleados
		public ArrayList<Long> getListaCodigoEmpleado(List <Empleado> listaEmpleado){
			
			ArrayList<Long> listaIdEmpleado=new ArrayList<Long>();		
			
			for(Empleado e: listaEmpleado){
				listaIdEmpleado.add(e.getEmplId());
			}
			
			return listaIdEmpleado;
		}
		
		//Obtener claves primarias de lista de empleados
		public ArrayList<String> getListaCodigoRelojEmpleado(List <Empleado> listaEmpleado){
			
			ArrayList<String> listaIdEmpleado=new ArrayList<String>();		
			
			for(Empleado e: listaEmpleado){
				listaIdEmpleado.add("'"+e.getCodigoEmpleado()+"'");
			}
			
			return listaIdEmpleado;
		}
		
		//Tranformar Arreglo a Cadena
		public List<String> getCadenaArregloLong(ArrayList<Long> listaAsisId){
					
			List<String> lista=new ArrayList<String>();
					
			String part="";
			int count=0;
					
			for(int i=0;i<listaAsisId.size();i++){
				if(count==499){
					part=part.substring(0,part.length()-1);
					lista.add(part);
					part=listaAsisId.get(i)+",";
					count=0;
				}else{
					part=part+listaAsisId.get(i)+",";
				}
				count++;
			}
					
			if(part.length()>0){
				part=part.substring(0,part.length()-1);
				lista.add(part);
				part="";
			}
					
//			String AsisId=listaAsisId.toString();
//			AsisId=AsisId.substring(1);
//			AsisId=AsisId.substring(0,AsisId.length()-1);
//			
//			return AsisId;
					
			return lista;
		}
		
		//tranformar arreglo a arreglo de cadenas
		public List<String> getListaCadenaArregloLong(ArrayList<Long> listaAsisId){
			
			List<String> listaCadenaIntermendia=new ArrayList<String>();
			
			if(listaAsisId.size()<=500){
				String AsisId=listaAsisId.toString();
				AsisId=AsisId.substring(1);
				AsisId=AsisId.substring(0,AsisId.length()-1);
				listaCadenaIntermendia.add(AsisId);
			}else{
				int limite=500;
				int index=0;
				String valorCadena="";
				for(int i=0;i<listaAsisId.size();i++){
					
					valorCadena+=listaAsisId.get(i)+",";
					
					index++;
					
					if(index==limite){
						valorCadena=valorCadena.substring(0,valorCadena.length()-1);
						listaCadenaIntermendia.add(valorCadena);
						index=0;
						valorCadena="";
					}
				}
				
				if(!(valorCadena.equals(""))){
					valorCadena=valorCadena.substring(0,valorCadena.length()-1);
					listaCadenaIntermendia.add(valorCadena);
				}
				
			}
			
			return listaCadenaIntermendia;
		}
		
		//Tranformar Arreglo a Cadena
		public List<String> getCadenaArregloString(ArrayList<String> listaAsisId){
					
			List<String> lista=new ArrayList<String>();
					
			String part="";
			int count=0;
					
			for(int i=0;i<listaAsisId.size();i++){
				if(count==499){
					part=part.substring(0,part.length()-1);
					lista.add(part);
					part=listaAsisId.get(i)+",";
					count=0;
				}else{
					part=part+listaAsisId.get(i)+",";
				}
				count++;
			}
				
			if(part.length()>0){
				part=part.substring(0,part.length()-1);
				lista.add(part);
				part="";
			}
					
			return lista;
					
//			String AsisId=listaAsisId.toString();
//			AsisId=AsisId.substring(1);
//			AsisId=AsisId.substring(0,AsisId.length()-1);
//					
//			return AsisId;
		}
		
		//Obtener parametro de teclas de funcion
		
		private void establecerTeclasFuncion(){
			
			DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
			DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
			
			detalleTipoParametros = detalleTipoParametroList.getValorTeclasFuncion();				
			String parametro=detalleTipoParametros.getDescripcion();
			parametro=parametro.trim();			
			
			List<String> teclaFuncion = new ArrayList<String>();
			
			boolean leerValor=false;
			String valor="";
			
			for(int i=0;i<parametro.length();i++){
										
				if(leerValor){
					if(parametro.charAt(i)!=';'){
						valor+=parametro.charAt(i);
					}					
					else{
						if(valor.length()>0){
							valor=valor.trim();
							teclaFuncion.add(valor);
							valor="";
							leerValor=false;	
						}
					}
				}
				
				if(parametro.charAt(i)=='=')
					leerValor=true;			
				
				
			}
			
			this.teclaFuncion=teclaFuncion;
			
		}
		
		private void establecerTipoEntradaSalida(List<String> teclaFuncion){
		
			int tamaloTeclaFuncion=teclaFuncion.size();
			
			System.out.println("Tecla Funcion " + teclaFuncion);
			switch(tamaloTeclaFuncion){
			
			case 1:
				
				E=teclaFuncion.get(0);
				S=teclaFuncion.get(0);
				SA=teclaFuncion.get(0);
				EA=teclaFuncion.get(0);
				SP=teclaFuncion.get(0);
				EP=teclaFuncion.get(0);
				
				break;
				
			case 3:
				
				E=teclaFuncion.get(0);
				S=teclaFuncion.get(0);
				SA=teclaFuncion.get(1);
				EA=teclaFuncion.get(1);
				SP=teclaFuncion.get(2);
				EP=teclaFuncion.get(2);				
				
				break;
				
			case 6:
				
				E=teclaFuncion.get(0);
				S=teclaFuncion.get(1);
				SA=teclaFuncion.get(2);
				EA=teclaFuncion.get(3);
				SP=teclaFuncion.get(4);
				EP=teclaFuncion.get(5);		
				
				break;
				
			default:
				
				E=teclaFuncion.get(0);
				S=teclaFuncion.get(1);
				SA=teclaFuncion.get(2);
				EA=teclaFuncion.get(3);
				SP=teclaFuncion.get(4);
				EP=teclaFuncion.get(5);	
				
				break;
				
			}
			
		}
		
		//Obtener Lista de Entradas y Salidas Asistencia
		private List<String> getListaEstadoAsistencia(String teclaFuncion){
			
			List<String> listaEstado=new ArrayList<String>();
			
			boolean existe;
			
			//System.out.println("TeclaFuncion " + teclaFuncion );
			
			existe=existeTeclaFuncion(E,teclaFuncion);			
			if(existe)
				listaEstado.add("E");
			
			existe=existeTeclaFuncion(S,teclaFuncion);
			if(existe)
				listaEstado.add("S");
			
			existe=existeTeclaFuncion(SA,teclaFuncion);
			if(existe)
				listaEstado.add("SA");
			
			existe=existeTeclaFuncion(EA,teclaFuncion);
			if(existe)
				listaEstado.add("EA");
			
			return listaEstado;
		}
		
		//Obtener Lista de Entradas y Salidas Permiso
		private List<String> getListaEstadoPermiso(String teclaFuncion){
			
			List<String> listaEstado=new ArrayList<String>();
			
			boolean existe;		
			
			existe=existeTeclaFuncion(SP,teclaFuncion);
			if(existe)
				listaEstado.add("SP");
			
			existe=existeTeclaFuncion(EP,teclaFuncion);
			if(existe)
				listaEstado.add("EP");
			
			return listaEstado;
		}
		
		//Obtener Teclas de Funcion
		private boolean existeTeclaFuncion(String valoresTeclafuncion, String teclaFuncion){
			
			boolean existe=false;
			String valor="";
			
			for(int i=0;i<valoresTeclafuncion.length();i++){
				
				if(valoresTeclafuncion.charAt(i)!=','){
					valor+=valoresTeclafuncion.charAt(i);
				}else{
					if(valor.equals(teclaFuncion)){
						existe=true;
						break;
					}else{
						valor="";
					}
				}				
			}
			
			return existe;
			
		}
		
		//Gestion de Recarga de Timbres
		public void RecargarTimbres(List<PlantillaPlanificacion> resultadosAsistencia, List<PlantillaPlanificacion> resultadosTimbre, List<Empleado> listaEmpleado,
				List<PlantillaPlanificacion> resultadosPermisoDesde, List<PlantillaPlanificacion> resultadosPermisoHasta){
		
			establecerTeclasFuncion();
			establecerTipoEntradaSalida(teclaFuncion);
			
			int tamañoTeclaFuncion=teclaFuncion.size();
			
			getAsistenciaUpdate().clear();
			getTimbreUpdate().clear();
			getPermisoUpdateSalida().clear();
			getPermisoUpdateEntrada().clear();
						
			for(Empleado e: listaEmpleado){
				
				Long emplId=e.getEmplId();
				
				String codigoEmpleado=e.getCodigoEmpleado();
				
				codigoEmpleado=codigoEmpleado.trim();
				
				List<PlantillaPlanificacion> asistenciaEmpleado=getListaPlantillaAsistenciaEmpleado(emplId,resultadosAsistencia);
				List<PlantillaPlanificacion> timbreEmpleado=getListaPlantillaTimbreEmpleado(codigoEmpleado,resultadosTimbre);
				List<PlantillaPlanificacion> permisoDesdeEmpleado=getListaPlantillaAsistenciaEmpleado(emplId,resultadosPermisoDesde);
				List<PlantillaPlanificacion> permisoHastaEmpleado=getListaPlantillaAsistenciaEmpleado(emplId,resultadosPermisoHasta);
				
				if(tamañoTeclaFuncion==3||tamañoTeclaFuncion==6)
					CompararTimbreAsistencia(asistenciaEmpleado, timbreEmpleado, permisoDesdeEmpleado, permisoHastaEmpleado);
				else if(tamañoTeclaFuncion==1)
						CompararTimbreAsistenciaUnico(asistenciaEmpleado, timbreEmpleado, permisoDesdeEmpleado, permisoHastaEmpleado);
			    
			}
			
			//System.out.println("log..........................masive asistenciaUpdate:"+asistenciaUpdate.size());
			ActualizarAsistencia(asistenciaUpdate);
			//System.out.println("log..........................masive timbreUpdate:"+timbreUpdate.size());
			ActualizarTimbre(timbreUpdate);
			//System.out.println("log..........................masive permisoUpdateSalida:"+permisoUpdateSalida.size());
			ActualizarSalidaPermiso(permisoUpdateSalida);
			//System.out.println("log..........................masive permisoUpdateEntrada:"+permisoUpdateEntrada.size());
			ActualizarEntradaPermiso(permisoUpdateEntrada);
			
		}
		
		//CompararTimbres vs Asistencia
		private void CompararTimbreAsistencia(List<PlantillaPlanificacion> asistencia, List<PlantillaPlanificacion> timbre, List<PlantillaPlanificacion> permisoDesde,
				List<PlantillaPlanificacion> permisoHasta){
			
			for(PlantillaPlanificacion pt: timbre){				
								
				Date fechaHoraTimbre=pt.getFechaTimbre();
				
				
				
				String teclaFuncion=pt.getTeclaFuncion();
				
				teclaFuncion=teclaFuncion.trim();
								
				List<String> listaEstadoAsistencia = getListaEstadoAsistencia(teclaFuncion);
										
				if (listaEstadoAsistencia.size()>0){
					
						CompararAsistenciaTimbreDesglosado(pt,asistencia,fechaHoraTimbre,listaEstadoAsistencia);
					
				}else{
					
					List<String> listaEstadoPermiso = getListaEstadoPermiso(teclaFuncion);
					
					if(listaEstadoPermiso.size()>0){
						
						CompararPermisoDesglosado(pt,asistencia,permisoDesde,permisoHasta,fechaHoraTimbre,listaEstadoPermiso);
						
					}		
										
				}			
				
			}
		
		}
		
		//Comparar Asistencia desglosado 
		private void CompararAsistenciaTimbreDesglosado(PlantillaPlanificacion timbre, List<PlantillaPlanificacion> asistencia, Date fechaHoraTimbre, List<String> listaEstadoAsistencia){
			
			List<PlantillaPlanificacion> filtroAsistencia=FiltrarAsistencia(asistencia,fechaHoraTimbre,listaEstadoAsistencia);
						
			if(filtroAsistencia.size()>0){					
								
				int posicionFiltro=0;
				
				int j=0;
				
				double comparacion=0;					
				
				for(PlantillaPlanificacion pf: filtroAsistencia){
					
					posicionFiltro = 0;
					Date fechaAsistencia=pf.getFecha();
					
					double comparacionAuxiliar=DiferenciaFechas(fechaHoraTimbre,fechaAsistencia);
					
					if(j==0){
						
						posicionFiltro=0;	
						
						comparacion=comparacionAuxiliar;
						
					}else{
						
						if(comparacionAuxiliar<comparacion){
				
							comparacion=comparacionAuxiliar;
				
							posicionFiltro=j;
										
						}						
					}	
				
					j++;
					
				}	
				
			
				//System.out.println("Posicion Filtro " + posicionFiltro);
				
				PlantillaPlanificacion asistenciaCambio=filtroAsistencia.get(posicionFiltro);
				
				List<PlantillaPlanificacion> asistenciaTimbre =EditarValoresAsistenciaTimbre(timbre,asistenciaCambio);					
				
				
				
				timbre.setAccion(asistenciaTimbre.get(0).getAccion());					
				
				PlantillaPlanificacion asitenciaAddLista=new PlantillaPlanificacion();
				asitenciaAddLista.setAsisId(asistenciaTimbre.get(1).getAsisId());
				asitenciaAddLista.setFechaHoraTimbre(asistenciaTimbre.get(1).getFechaHoraTimbre());
				asitenciaAddLista.setEstado(asistenciaTimbre.get(1).getEstado());
						
							
				asistenciaUpdate.add(asitenciaAddLista);
					
				
			}else{
				timbre.setAccion("99");
			}				
			
			timbre.setCadenaFechaTimbre(CadenaFecha(fechaHoraTimbre));
			timbre.setCadenaHoraTimbre(CadenaHora(fechaHoraTimbre));
			
			timbreUpdate.add(timbre);
			
		}	
		
		//Comparar Permiso desglosado 
		private void CompararPermisoDesglosado(PlantillaPlanificacion timbre,List<PlantillaPlanificacion> asistencia, List<PlantillaPlanificacion> permisoDesde, 
				List<PlantillaPlanificacion> permisoHasta, Date fechaHoraTimbre, List<String> listaEstadoPermiso){
			
			List<PlantillaPlanificacion> filtroPermiso=new ArrayList<PlantillaPlanificacion>();
						
			for(int i=0;i<listaEstadoPermiso.size();i++){
				
				String tipoEntradaSalida=listaEstadoPermiso.get(i);
				
				if(tipoEntradaSalida.equals("SP")){
					filtroPermiso=FiltrarPermisoDesde(permisoDesde, fechaHoraTimbre);
				}						
				
				if(tipoEntradaSalida.equals("EP")){
					filtroPermiso=FiltrarPermisoHasta(permisoHasta, fechaHoraTimbre);
				}
				
			}
			
			if(filtroPermiso.size()>0){					
				
				int posicionFiltro=0;
				
				int j=0;
				
				double comparacion=0;					
				
				for(PlantillaPlanificacion fp: filtroPermiso){
					
					Date fechaPermiso=fp.getFechaPermiso();
					
					double comparacionAuxiliar=DiferenciaFechas(fechaHoraTimbre,fechaPermiso);
					
					if(j==0){
						
						posicionFiltro=0;	
						
						comparacion=comparacionAuxiliar;
						
					}else{
						if(comparacionAuxiliar<comparacion){
				
							comparacion=comparacionAuxiliar;
				
							posicionFiltro=j;
				
						}						
					}	
				
					j++;
					
				}				
				
				PlantillaPlanificacion permiso=filtroPermiso.get(posicionFiltro);
				
				if(permiso.getEntradaSalida().equals("SP")){
					timbre=getTimbreSalidaPermiso(timbre,fechaHoraTimbre,asistencia,permiso);
				}
				
				if(permiso.getEntradaSalida().equals("EP")){
					timbre=getTimbreEntradaPermiso(timbre,fechaHoraTimbre,asistencia,permiso);
				}
								
				
			}else{
				timbre.setAccion("01");
			}	
			
			timbre.setCadenaFechaTimbre(CadenaFecha(fechaHoraTimbre));
			timbre.setCadenaHoraTimbre(CadenaHora(fechaHoraTimbre));
			
			timbreUpdate.add(timbre);
			
		}	
		
		//Establecer valores Asistencia-Timbre y Permiso de acuerdo a Salida de Permiso
		private PlantillaPlanificacion getTimbreSalidaPermiso(PlantillaPlanificacion timbre, Date fechaHoraTimbre, List <PlantillaPlanificacion> asistencia, PlantillaPlanificacion permiso){
			
			List<String> listaEstadoAsistencia=new ArrayList<String>();
			listaEstadoAsistencia.add("S");
			
			List<PlantillaPlanificacion> filtroAsistencia=FiltrarAsistencia(asistencia,fechaHoraTimbre,listaEstadoAsistencia);
			
			if(filtroAsistencia.size()>0){
				
				boolean existeAsistencia=false;
				
				for(PlantillaPlanificacion a: filtroAsistencia){
					
					if(a.getFecha().equals(permiso.getFechaPermiso())){
						
						permiso.setCadenaFechaDesdePermiso(CadenaFechaCompleta(fechaHoraTimbre));
						permisoUpdateSalida.add(permiso);
						
						permiso.setCadenaFechaHastaPermiso(CadenaFechaCompleta(a.getFecha()));
						permisoUpdateEntrada.add(permiso);					
						
						a.setEstado("P");
						a.setFechaHoraTimbre(CadenaFechaCompleta(fechaHoraTimbre));
						asistenciaUpdate.add(a);
												
						existeAsistencia=true;
						break;
					}					
				}
				
				if(!existeAsistencia){
					permiso.setCadenaFechaDesdePermiso(CadenaFechaCompleta(fechaHoraTimbre));
					permisoUpdateSalida.add(permiso);
				}
				
			}else{
				permiso.setCadenaFechaDesdePermiso(CadenaFechaCompleta(fechaHoraTimbre));
				permisoUpdateSalida.add(permiso);
				
			}
			
			timbre.setAccion("5");
			
			return timbre;
		}
		
		//Establecer valores Asistencia-Timbre y Permiso de acuerdo a Entrada de Permiso
		private PlantillaPlanificacion getTimbreEntradaPermiso(PlantillaPlanificacion timbre, Date fechaHoraTimbre, List <PlantillaPlanificacion> asistencia, PlantillaPlanificacion permiso){
			
			List<String> listaEstadoAsistencia=new ArrayList<String>();
			listaEstadoAsistencia.add("E");
			
			List<PlantillaPlanificacion> filtroAsistencia=FiltrarAsistencia(asistencia,fechaHoraTimbre,listaEstadoAsistencia);
			
			if(filtroAsistencia.size()>0){
				
				boolean existeAsistencia=false;
				
				for(PlantillaPlanificacion a: filtroAsistencia){
					
					if(a.getFecha().equals(permiso.getFechaPermiso())){
						
						permiso.setCadenaFechaDesdePermiso(CadenaFechaCompleta(a.getFecha()));
						permisoUpdateSalida.add(permiso);
						
						permiso.setCadenaFechaHastaPermiso(CadenaFechaCompleta(fechaHoraTimbre));
						permisoUpdateEntrada.add(permiso);
						
						a.setEstado("P");
						a.setFechaHoraTimbre(CadenaFechaCompleta(fechaHoraTimbre));
						asistenciaUpdate.add(a);
						
						existeAsistencia=true;
						break;
					}					
				}
				
				if(!existeAsistencia){
					permiso.setCadenaFechaHastaPermiso(CadenaFechaCompleta(fechaHoraTimbre));
					permisoUpdateEntrada.add(permiso);
				}
				
			}else{
				permiso.setCadenaFechaHastaPermiso(CadenaFechaCompleta(fechaHoraTimbre));
				permisoUpdateEntrada.add(permiso);
				
			}
			
			timbre.setAccion("4");
			
			return timbre;
		}
			
		
		//Comparar Asistencia de 0 teclas de función
		private void CompararTimbreAsistenciaUnico(List<PlantillaPlanificacion> asistencia, List<PlantillaPlanificacion> timbre, List<PlantillaPlanificacion> permisoDesde,
				List<PlantillaPlanificacion> permisoHasta){
					
			for(PlantillaPlanificacion pt: timbre){
				
				Date fechaHoraTimbre=pt.getFechaTimbre();
				
				String teclaFuncion=pt.getTeclaFuncion();
				
				teclaFuncion=teclaFuncion.trim();
				
				List<String> listaEstadoAsistencia = getListaEstadoAsistencia(teclaFuncion);
				
				if (listaEstadoAsistencia.size()>0){
					
					CompararAsistenciaTimbreDesglosado(pt,asistencia,fechaHoraTimbre,listaEstadoAsistencia);
					
				}			
				
			}
		
		
							
		}		
		
		//RestarFechas		
		private double DiferenciaFechas(Date fecha1, Date fecha2){
			System.out.println("!!!!!!!!!!!!!! Tiempo " + (fecha1.getTime()-fecha2.getTime()));
			return Math.abs(fecha1.getTime()-fecha2.getTime());
		}
		
		//SetearValores en Asistencia y Timbre
		private List<PlantillaPlanificacion> EditarValoresAsistenciaTimbre(PlantillaPlanificacion timbre, PlantillaPlanificacion asistencia){
			
			List<PlantillaPlanificacion> asistenciaTimbre=new ArrayList<PlantillaPlanificacion>();
			
			asistencia.setFechaHoraTimbre(CadenaFechaCompleta(timbre.getFechaTimbre()));
			
			//System.out.println("Cambioo!!!!!!ooo " + asistencia);
			//System.out.println("Cambioo!!!!!!ooo " + timbre);
			
			if(asistencia.getEstado().trim().equals("FT"))
				asistencia.setEstado("R");	
			
			String entradaSalida=asistencia.getEntradaSalida();
			
			entradaSalida=entradaSalida.trim();
			
			
			if(entradaSalida.equals("E"))
				timbre.setAccion("0");
			
			if(entradaSalida.equals("S"))
				timbre.setAccion("1");
			
			if(entradaSalida.equals("SA"))
				timbre.setAccion("3");
			
			if(entradaSalida.equals("EA"))
				timbre.setAccion("2");
			
			
			asistenciaTimbre.add(timbre);
			
			asistenciaTimbre.add(asistencia);
						
			return asistenciaTimbre;			
		}
		
		//Obtener fecha completa YYYY-MM-DD HH:mm:ss
		private String CadenaFechaCompleta(Date fecha){
			String cadenaFecha=getAño(fecha)+"-"+(getMes(fecha)+1)+"-"+getDia(fecha)+" "+getHora(fecha)+":"+getMinuto(fecha)+":"+getSegundo(fecha);
			return cadenaFecha;
		}
		
		//Obtener fecha completa YYYY-MM-DD
		private String CadenaFecha(Date fecha){
			String cadenaFecha=getAño(fecha)+"-"+(getMes(fecha)+1)+"-"+getDia(fecha);
			return cadenaFecha;
		}
		
		//Obtener fecha completa HH:mm:ss
		private String CadenaHora(Date fecha){
			String cadenaFecha=getHora(fecha)+":"+getMinuto(fecha)+":"+getSegundo(fecha);
			return cadenaFecha;
		}
		
		//FiltrarAsistencia por Fecha y tipo de Entrada/Salidad
		@SuppressWarnings("unchecked")
		private List<PlantillaPlanificacion> FiltrarAsistencia(List<PlantillaPlanificacion> asistencia, Date fechaHoraTimbre, List<String> listaEstado){
		
			List<PlantillaPlanificacion> filtroAsistencia=new ArrayList<PlantillaPlanificacion>();
			
			Date fechatimbre=getDateWithoutTime(fechaHoraTimbre);
		//	System.out.println("Asistencia " + asistencia);
			for(PlantillaPlanificacion p: asistencia){
				
				Date fechaHorario=getDateWithoutTime(p.getFecha());
			
				String entradaSalida=p.getEntradaSalida();				
							
				if(fechaHorario.equals(fechatimbre)){
				
					for(int i=0;i<listaEstado.size();i++){
						
						if(listaEstado.get(i).equals(entradaSalida))							
							filtroAsistencia.add(p);
					}
					
				}
				
			}
			
			return filtroAsistencia;
		}
		
		
		
		
		//Filtrar Permiso por fecha Desde
		private List<PlantillaPlanificacion> FiltrarPermisoDesde(List<PlantillaPlanificacion> permisoDesde, Date fechaDesde){
		
			List<PlantillaPlanificacion> filtroAsistencia=new ArrayList<PlantillaPlanificacion>();
			
			Date fechaPermiso=getDateWithoutTime(fechaDesde);
			
			for(PlantillaPlanificacion pd: permisoDesde){
				
				Date fechaPermisoDesde=getDateWithoutTime(pd.getFechaPermiso());		
							
				if(fechaPermisoDesde.equals(fechaPermiso)){				
					
					pd.setEntradaSalida("SP");					
					filtroAsistencia.add(pd);
					
				}
				
			}
			
			return filtroAsistencia;
		}
		
		//Filtrar Permiso por fecha Desde
		private List<PlantillaPlanificacion> FiltrarPermisoHasta(List<PlantillaPlanificacion> permisoHasta, Date fechaHasta){
		
			List<PlantillaPlanificacion> filtroAsistencia=new ArrayList<PlantillaPlanificacion>();
			
			Date fechaPermiso=getDateWithoutTime(fechaHasta);
			
			for(PlantillaPlanificacion ph: permisoHasta){
				
				Date fechaPermisoHasta=getDateWithoutTime(ph.getFechaPermiso());		
							
				if(fechaPermisoHasta.equals(fechaPermiso)){
				
					ph.setEntradaSalida("EP");
					filtroAsistencia.add(ph);
					
				}
				
			}
			
			return filtroAsistencia;
		}
					
		//Obtener fecha YYYY/MM/DD
		private Date getDateWithoutTime(Date fecha){
			
			Calendar calendar=getCalendar();

			calendar.set(Calendar.YEAR,getAño(fecha));
			calendar.set(Calendar.MONTH,(getMes(fecha)+1));
			calendar.set(Calendar.DATE,getDia(fecha));
			
			return calendar.getTime();	
		}
		
		
		//********************************************************************************************************
		
		//**********************************MÉTODOS DE MANEJO DE DATOS EN BD*************************************
		
		//Obtener Procedimiento Almacenado de Inserción de Asistencia
		private String getSpAsistenciaEmpleado(){
			
			String llamada="";
			
			if(dataBase.equals("MYSQL"))
				llamada="CALL spInsertarAsistenciaEmpleado (?, ?, ?, ?, ?, ? , ? , ?, ?, ?)";
			else
				if(dataBase.equals("SQLSERVER"))
					llamada="EXEC spInsertarAsistenciaEmpleado ?, ?, ?, ?, ?, ? , ? , ?, ?, ?";
				else
					if(dataBase.equals("ORACLE"))
						llamada="CALL spInsertarAsistenciaEmpleado (?, ?, ?, ?, ?, ? , ? , ?, ?, ?)";
					else
						if(dataBase.equals("POSTGRES"))
							llamada="SELECT spInsertarAsistenciaEmpleado (?, ?, ?, ?, ?, ? , ? , ?, ?, ?)";
			
			return llamada;
		}
		
		//Obtener Procedimiento Almacenado de Actualización de Asistencia
		private String getspActualizarAsistenciaEmpleado(){
			
			String llamada="";
			
			if(dataBase.equals("MYSQL"))
				llamada="CALL spActualizarAsistenciaEmpleado (?, ?, ?)";
			else
				if(dataBase.equals("SQLSERVER"))
					llamada="EXEC spActualizarAsistenciaEmpleado ?, ?, ?";
				else
					if(dataBase.equals("ORACLE"))
						llamada="CALL spActualizarAsistenciaEmpleado (?, ?, ?)";
					else
						if(dataBase.equals("POSTGRES"))
							llamada="SELECT spActualizarAsistenciaEmpleado (?, ?, ?)";
			
			return llamada;
		}
		
		//Obtener Procedimiento Almacenado de Actualización de Asistencia
		private String getspActualizarTimbreEmpleado(){
			
			String llamada="";
			
			if(dataBase.equals("MYSQL"))
				llamada="CALL spActualizarTimbreEmpleado (?, ?, ?, ?)";
			else
				if(dataBase.equals("SQLSERVER"))
					llamada="EXEC spActualizarTimbreEmpleado ?, ?, ?, ?";
				else
					if(dataBase.equals("ORACLE"))
						llamada="CALL spActualizarTimbreEmpleado (?, ?, ?, ?)";
					else
						if(dataBase.equals("POSTGRES"))
							llamada="SELECT spActualizarTimbreEmpleado (?, ?, ?, ?)";
			
			return llamada;
		}
		
		//Obtener Procedimiento Almacenado de Actualización de Salida Permiso
		private String getspActualizarSalidaPermisoEmpleado(){
			
			String llamada="";
			
			if(dataBase.equals("MYSQL"))
				llamada="CALL spActualizarSalidaPermiso (?, ?)";
			else
				if(dataBase.equals("SQLSERVER"))
					llamada="EXEC spActualizarSalidaPermiso ?, ?";
				else
					if(dataBase.equals("ORACLE"))
						llamada="CALL spActualizarSalidaPermiso (?, ?)";
					else
						if(dataBase.equals("POSTGRES"))
							llamada="SELECT spActualizarSalidaPermiso (?, ?)";
			
			return llamada;
		}
		
		//Obtener Procedimiento Almacenado de Actualización de Salida Permiso
		private String getspActualizarEntradaPermisoEmpleado(){
			
			String llamada="";
			
			if(dataBase.equals("MYSQL"))
				llamada="CALL spActualizarEntradaPermiso (?, ?)";
			else
				if(dataBase.equals("SQLSERVER"))
					llamada="EXEC spActualizarEntradaPermiso ?, ?";
				else
					if(dataBase.equals("ORACLE"))
						llamada="CALL spActualizarEntradaPermiso (?, ?)";
					else
						if(dataBase.equals("POSTGRES"))
							llamada="SELECT spActualizarEntradaPermiso (?, ?)";
			
			return llamada;
		}
				
		//Buscar asistencia en BD
		public List<PlantillaPlanificacion> buscarAsistenciaEmpleados(List<String> listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
					
			List<PlantillaPlanificacion> listaplantilla=new ArrayList<PlantillaPlanificacion>();
				
			for(int i=0; i<listaCodigoEmpleado.size(); i++){
				
				Conexion conexion=new Conexion();		
				Connection connection = conexion.getConnection();				
							
				String sentenciaNativa= "select distinct a.empl_id,a.fecha_horario,h.hora_id,a.estado from asistencia a inner join detalle_horario dh on dh.deho_id=a.deho_id inner join horario h on h.hora_id=dh.hora_id where a.fecha_horario >= (?) and a.fecha_horario <= (?) and a.empl_id in ("+listaCodigoEmpleado.get(i)+") order by a.empl_id, a.fecha_horario";
						
				try {
										
					PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
										
					java.sql.Date inicio=getDateSql(fechaDesde);				
					java.sql.Date fin=getDateSql(fechaHasta);
									
					preparedStatement.setDate(1, inicio);
					preparedStatement.setDate(2, fin);			
									
					ResultSet resultSet=preparedStatement.executeQuery();
							
					PlantillaPlanificacion plantilla=new PlantillaPlanificacion();
							
					while(resultSet.next()){
						plantilla.setEmpId(resultSet.getLong(1));
								
						plantilla.setFecha(resultSet.getDate(2));
							
						plantilla.setDhId(resultSet.getLong(3));
								
						plantilla.setEstado(resultSet.getString(4));
								
						listaplantilla.add(plantilla);
							
						plantilla=new PlantillaPlanificacion();
					}			
							
					preparedStatement.close();			
								
					connection.close(); 
								
				} catch (SQLException e) {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
					e1.printStackTrace();
					} 
					e.printStackTrace();
				}	
						
			}		
					
			return listaplantilla;
					
		}
				
		//Buscar asistencia en BD para Recargar Timbres
		@SuppressWarnings("deprecation")
		public List<PlantillaPlanificacion> buscarAsistenciaGrupo(List<String> listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
					
			List<PlantillaPlanificacion> listaplantilla=new ArrayList<PlantillaPlanificacion>();			
					
			for(int i=0;i<listaCodigoEmpleado.size();i++){
						
				Conexion conexion=new Conexion();		
				Connection connection = conexion.getConnection();
						
				String sentenciaNativa= "select a.asis_id, a.empl_id, a.fecha_hora_horario, a.entrada_salida, a.estado from asistencia a where a.fecha_hora_horario >= (?) and a.fecha_hora_horario <= (?) and a.empl_id in ("+listaCodigoEmpleado.get(i)+") order by a.empl_id, a.fecha_hora_horario";
						
				try {
											
					PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
										
					java.sql.Date inicio=getDateSql(fechaDesde);				
					java.sql.Date fin=getDateSql(fechaHasta);
							
					fin.setDate((fin.getDate()+1));
							
					preparedStatement.setDate(1, inicio);
					preparedStatement.setDate(2, fin);			
									
					ResultSet resultSet=preparedStatement.executeQuery();
							
					PlantillaPlanificacion plantilla=new PlantillaPlanificacion();
							
					while(resultSet.next()){
								
						plantilla.setAsisId(resultSet.getLong(1));
								
						plantilla.setEmpId(resultSet.getLong(2));
								
						plantilla.setFecha(resultSet.getTimestamp(3));
								
						plantilla.setEntradaSalida(resultSet.getString(4));
								
						plantilla.setEstado(resultSet.getString(5));
						System.out.println("++++++++Eingresads asistencia: " + plantilla.getAsisId());
						System.out.println("++++++++++Eingresads asistencia: " + plantilla.getEmpId());
						System.out.println("++++++++++Eingresads asistencia: " + plantilla.getFecha());
						System.out.println("++++++++++Eingresads asistencia: " + plantilla.getEntradaSalida());
						listaplantilla.add(plantilla);
								
						plantilla=new PlantillaPlanificacion();
					}			
							
					preparedStatement.close();			
								
					connection.close(); 
						
				} catch (SQLException e) {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					e.printStackTrace();
				}	
						
			}		
					
			return listaplantilla;
					
		}
				
		//Buscar permiso de acuerdo fecha desde en BD
		@SuppressWarnings("deprecation")
		public List<PlantillaPlanificacion> buscarPermisoDesdeGrupo(List<String> listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
					
			List<PlantillaPlanificacion> listaplantilla=new ArrayList<PlantillaPlanificacion>();
					
			for(int i=0;i<listaCodigoEmpleado.size();i++){
					
				Conexion conexion=new Conexion();		
				Connection connection = conexion.getConnection();				
							
				String sentenciaNativa= "select p.perm_id, p.empl_id, p.fecha_desde from permiso p where p.fecha_desde >= (?) and p.fecha_desde <= (?) and p.empl_id in ("+listaCodigoEmpleado.get(i)+") and p.autorizado=3 order by p.empl_id, p.fecha_desde";
								
				try {
											
					PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
										
//					java.sql.Date inicio=getDateSql(fechaDesde);				
//					java.sql.Date fin=getDateSql(fechaHasta);
							
					java.sql.Timestamp inicio=getTimestampSql(fechaDesde);
					java.sql.Timestamp fin=getTimestampSql(fechaHasta);
							
					fin.setDate((fin.getDate()+1));
									
					preparedStatement.setTimestamp(1, inicio);
					preparedStatement.setTimestamp(2, fin);			
									
					ResultSet resultSet=preparedStatement.executeQuery();
							
					PlantillaPlanificacion plantilla=new PlantillaPlanificacion();
							
						while(resultSet.next()){
								
						plantilla.setPermId(resultSet.getLong(1));
								
						plantilla.setEmpId(resultSet.getLong(2));
								
						plantilla.setFechaPermiso(resultSet.getTimestamp(3));
						
						listaplantilla.add(plantilla);
								
						plantilla=new PlantillaPlanificacion();
					}			
							
					preparedStatement.close();			
								
					connection.close(); 
								
				} catch (SQLException e) {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					e.printStackTrace();
				}	
						
			}		
					
			return listaplantilla;
					
		}
				
		//Buscar permiso de acuerdo fecha desde en BD
		@SuppressWarnings("deprecation")
		public List<PlantillaPlanificacion> buscarPermisoHastaGrupo(List<String> listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
					
			List<PlantillaPlanificacion> listaplantilla=new ArrayList<PlantillaPlanificacion>();
					
			for(int i=0;i<listaCodigoEmpleado.size();i++){
					
				Conexion conexion=new Conexion();		
				Connection connection = conexion.getConnection();				
						
				String sentenciaNativa= "select p.perm_id, p.empl_id, p.fecha_hasta from permiso p where p.fecha_desde >= (?) and p.fecha_hasta <= (?) and p.empl_id in ("+listaCodigoEmpleado.get(i)+") and p.autorizado=3 order by p.empl_id, p.fecha_hasta";
								
				try {
											
					PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
										
//					java.sql.Date inicio=getDateSql(fechaDesde);				
//					java.sql.Date fin=getDateSql(fechaHasta);
							
					java.sql.Timestamp inicio=getTimestampSql(fechaDesde);
					java.sql.Timestamp fin=getTimestampSql(fechaHasta);
							
					fin.setDate((fin.getDate()+1));
									
					preparedStatement.setTimestamp(1, inicio);
					preparedStatement.setTimestamp(2, fin);			
									
					ResultSet resultSet=preparedStatement.executeQuery();
							
					PlantillaPlanificacion plantilla=new PlantillaPlanificacion();
							
					while(resultSet.next()){
								
						plantilla.setPermId(resultSet.getLong(1));
								
						plantilla.setEmpId(resultSet.getLong(2));
								
						plantilla.setFechaPermiso(resultSet.getTimestamp(3));
							
						listaplantilla.add(plantilla);
								
						plantilla=new PlantillaPlanificacion();
					}			
							
					preparedStatement.close();			
							
					connection.close(); 
							
				} catch (SQLException e) {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					e.printStackTrace();
				}	
						
			}		
					
			return listaplantilla;
					
		}
				
		//Buscar asistencia en BD para Recargar Timbres
		@SuppressWarnings("deprecation")
		public List<PlantillaPlanificacion> buscarTimbresGrupo(List<String> listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
					
			List<PlantillaPlanificacion> listaplantilla=new ArrayList<PlantillaPlanificacion>();				
					
			for(int i=0; i<listaCodigoEmpleado.size();i++){
						
				Conexion conexion=new Conexion();		
				Connection connection = conexion.getConnection();
					
				String sentenciaNativa= "select t.timb_id, t.fecha_hora_timbre, t.tecla_funcion, t.codigo_empleado from timbre t where t.fecha_hora_timbre >= (?) and t.fecha_hora_timbre <= (?) and t.codigo_empleado in ("+listaCodigoEmpleado.get(i)+") order by t.codigo_empleado, t.fecha_hora_timbre";
						
				try {
											
					PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
										
//					java.sql.Date inicio=getDateSql(fechaDesde);	
//					java.sql.Date fin=getDateSql(fechaHasta);
					
					java.sql.Timestamp inicio=getTimestampSql(fechaDesde);
					java.sql.Timestamp fin=getTimestampSql(fechaHasta);
							
					fin.setDate((fin.getDate()+1));
											
					preparedStatement.setTimestamp(1, inicio);
					preparedStatement.setTimestamp(2, fin);	
									
					ResultSet resultSet=preparedStatement.executeQuery();
							
					PlantillaPlanificacion plantilla=new PlantillaPlanificacion();
							
					while(resultSet.next()){
								
						plantilla.setTimbId(resultSet.getLong(1));
								
						plantilla.setFechaTimbre(resultSet.getTimestamp(2));
							
						plantilla.setTeclaFuncion(resultSet.getString(3));
								
						plantilla.setCodigoEmpleado(resultSet.getString(4));
								
						listaplantilla.add(plantilla);
								
						plantilla=new PlantillaPlanificacion();
					}			
							
					preparedStatement.close();			
								
					connection.close(); 
								
				} catch (SQLException e) {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					e.printStackTrace();
				}	
						
			}	
					
			return listaplantilla;
					
		}
			
		//Buscar asistencia en BD para Recargar Timbres
		//@SuppressWarnings("deprecation")
		public List<PlantillaPlanificacion> buscarPermisoEmpleados(List<String> listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
					
			List<PlantillaPlanificacion> listaplantilla=new ArrayList<PlantillaPlanificacion>();
					
			for(int i=0; i<listaCodigoEmpleado.size(); i++){
					
				Conexion conexion=new Conexion();		
				Connection connection = conexion.getConnection();			
					
				String sentenciaNativa= "select p.empl_id, p.fecha_desde, p.fecha_hasta from permiso p where p.autorizado=3 and p.numero_horas=0 and "
						+ "p.empl_id in ("+listaCodigoEmpleado.get(i)+") and ( "				 
						+ "( p.fecha_desde between (?) and (?) ) or ( p.fecha_hasta between (?) and (?) ) or "
						+ "( (?) between p.fecha_desde and p.fecha_hasta ) or ( (?) between p.fecha_desde and p.fecha_hasta ) ) "
						+ "order by p.empl_id, p.fecha_desde";
								
				try {
											
					PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
										
					java.sql.Date inicio=getDateSql(fechaDesde);				
					java.sql.Date fin=getDateSql(fechaHasta);
					
					fin.setDate((fin.getDate()+1));
									
					preparedStatement.setDate(1, inicio);
					preparedStatement.setDate(2, fin);		
					preparedStatement.setDate(3, inicio);
					preparedStatement.setDate(4, fin);
					preparedStatement.setDate(5, inicio);
					preparedStatement.setDate(6, fin);						
							
					ResultSet resultSet=preparedStatement.executeQuery();
							
					PlantillaPlanificacion plantilla=new PlantillaPlanificacion();
							
					while(resultSet.next()){
								
						plantilla.setEmpId(resultSet.getLong(1));
								
						plantilla.setFechaPermisoDesde(resultSet.getTimestamp(2));
								
						plantilla.setFechaPermisoHasta(resultSet.getTimestamp(3));
								
						listaplantilla.add(plantilla);
							
						plantilla=new PlantillaPlanificacion();
					}			
							
					preparedStatement.close();			
								
					connection.close(); 
								
				} catch (SQLException e) {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					e.printStackTrace();
				}					
						
			}	
					
			return listaplantilla;
					
		}
		
		//Ingresar asistencia en BD
		public void InsertarRegistros( List<PlantillaPlanificacion> listaAsistencia){		
			
			String sentenciaNativa = getSpAsistenciaEmpleado();
					
			Connection connection = conexion.getConnection();	
			
			final int batchSize = conexion.getLongitudLote();
			int count = 0;
			
			try {
				
				PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
				
				for (PlantillaPlanificacion p: listaAsistencia) {
					
					preparedStatement.setLong(1,p.getEmpId());
					preparedStatement.setLong(2,p.getDhId());
					preparedStatement.setString(3,p.getFechaHoraHorario());
					preparedStatement.setString(4, p.getEntradaSalida());
					preparedStatement.setString(5,p.getCodigoEmpleado());
					preparedStatement.setInt(6,p.getOrden());
					preparedStatement.setInt(7,p.getMaxMinuto());
					preparedStatement.setString(8,p.getEstado());
					preparedStatement.setString(9,p.getFechaHorario());
					preparedStatement.setString(10, p.getHoraHorario());
					preparedStatement.addBatch();
			    
				    if(++count % batchSize == 0) {
				    	try{
				    		preparedStatement.executeBatch();
				    	}catch(Exception ex){
				    		ex.printStackTrace();
				    	}				    			
				    }		  
				}	
				
				try{
		    		preparedStatement.executeBatch();
		    	}catch(Exception ex){
		    		ex.printStackTrace();
		    	}
				
				preparedStatement.close();
				
				preparedStatement = connection.prepareStatement("update contadores set contador = (select max(asis_id) + 1 from asistencia) where id = 20");
				preparedStatement.executeUpdate();	
				preparedStatement.close();
				
				connection.close(); 
				
			} catch (SQLException e) {
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				e.printStackTrace();
			}	  
		}	
		
		//Ingresar asistencia en BD
		public void ActualizarAsistencia( List<PlantillaPlanificacion> listaAsistencia){		
			
			
			for(PlantillaPlanificacion pl : listaAsistencia){
				
				System.out.println("..... lista Asistencia " + pl.getAsisId());
				System.out.println("..... lista Asistencia " + pl.getFechaHoraTimbre());
				System.out.println("..... lista Asistencia " + pl.getEstado());
				System.out.println("..... lista Asistencia " + pl.getAccion());
				
			}
	
			
			
			String sentenciaNativa = getspActualizarAsistenciaEmpleado();
										
			Connection connection = conexion.getConnection();	
			
			final int batchSize = conexion.getLongitudLote();
			int count = 0;
			
			try {
				
				PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
				
				for (PlantillaPlanificacion p: listaAsistencia) {
															
					preparedStatement.setLong(1,p.getAsisId());
					preparedStatement.setString(2,p.getFechaHoraTimbre());
					preparedStatement.setString(3,p.getEstado());
					
					preparedStatement.addBatch();					
			    
				    if(++count % batchSize == 0) {
				    	try{
				    		preparedStatement.executeBatch();
				    	}catch(Exception ex){
				    		ex.printStackTrace();
				    	}				    			
				    }		  
				}	
				
				try{
					preparedStatement.executeBatch();
		    	}catch(Exception ex){
		    		ex.printStackTrace();
		    	}
				
				preparedStatement.close();				
				connection.close(); 
				
			} catch (SQLException e) {
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}	  
		}	
		
		//Ingresar asistencia en BD
		public void ActualizarTimbre( List<PlantillaPlanificacion> listaAsistencia){		
			
			String sentenciaNativa = getspActualizarTimbreEmpleado();
					
			Connection connection = conexion.getConnection();	
			
			final int batchSize = conexion.getLongitudLote();
			int count = 0;
			
			try {
				
				PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
				
				for (PlantillaPlanificacion p: listaAsistencia) {
															
					preparedStatement.setLong(1,p.getTimbId());
					preparedStatement.setString(2,p.getAccion());
					preparedStatement.setString(3,p.getCadenaFechaTimbre());
					preparedStatement.setString(4,p.getCadenaHoraTimbre());				

					preparedStatement.addBatch();
			    
				    if(++count % batchSize == 0) {
				    	try{
				    		preparedStatement.executeBatch();
				    	}catch(Exception ex){
				    		ex.printStackTrace();
				    	}				    			
				    }		  
				}	
				
				try{
		    		preparedStatement.executeBatch();
		    	}catch(Exception ex){
		    		ex.printStackTrace();
		    	}
				
				preparedStatement.close();				
				connection.close(); 
				
			} catch (SQLException e) {
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				e.printStackTrace();
			}	  
		}
		
		//Actualizar salida permiso en BD
		public void ActualizarSalidaPermiso( List<PlantillaPlanificacion> listaAsistencia){		
			
			String sentenciaNativa = getspActualizarSalidaPermisoEmpleado();
					
			Connection connection = conexion.getConnection();	
			
			final int batchSize = conexion.getLongitudLote();
			int count = 0;
			
			try {
				
				PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
				
				for (PlantillaPlanificacion p: listaAsistencia) {
					
					System.out.println("!!!!!!!!!!!!!!!!SALIDA PERMISO " + p.getPermId());
					System.out.println("!!!!!!!!!!!!!!!!SALIDA PERMISO " + p.getCadenaFechaDesdePermiso());					
					preparedStatement.setLong(1,p.getPermId());
					preparedStatement.setString(2,p.getCadenaFechaDesdePermiso());				

					preparedStatement.addBatch();
			    
				    if(++count % batchSize == 0) {
				    	try{
				    		preparedStatement.executeBatch();
				    	}catch(Exception ex){
				    		ex.printStackTrace();
				    	}				    			
				    }		  
				}	
				
				try{
		    		preparedStatement.executeBatch();
		    	}catch(Exception ex){
		    		ex.printStackTrace();
		    	}
				
				preparedStatement.close();				
				connection.close(); 
				
			} catch (SQLException e) {
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				e.printStackTrace();
			}	  
		}	
		
		//Actualizar entrada permiso en BD
		public void ActualizarEntradaPermiso( List<PlantillaPlanificacion> listaAsistencia){		
			
			String sentenciaNativa = getspActualizarEntradaPermisoEmpleado();
					
			Connection connection = conexion.getConnection();	
			
			final int batchSize = conexion.getLongitudLote();
			int count = 0;
			
			try {
				
				PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
				
				for (PlantillaPlanificacion p: listaAsistencia) {
					
					System.out.println("!!!!!!!!!!!!!!!!ENTRADA PERMISO " + p.getPermId());
					System.out.println("!!!!!!!!!!!!!!!!ENTRADA PERMISO " + p.getCadenaFechaDesdePermiso());		
										
					preparedStatement.setLong(1,p.getPermId());
					preparedStatement.setString(2,p.getCadenaFechaHastaPermiso());				

					preparedStatement.addBatch();
			    
				    if(++count % batchSize == 0) {
				    	try{
				    		preparedStatement.executeBatch();
				    	}catch(Exception ex){
				    		ex.printStackTrace();
				    	}				    			
				    }		  
				}	
				
				try{
		    		preparedStatement.executeBatch();
		    	}catch(Exception ex){
		    		ex.printStackTrace();
		    	}
				
				preparedStatement.close();				
				connection.close(); 
				
			} catch (SQLException e) {
				try {
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				e.printStackTrace();
			}	  
		}	
		
		//Eliminar asistencia en BD
		public void EliminarRegistros( String listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
								
			Conexion conexion=new Conexion();		
			Connection connection = conexion.getConnection();				
				
			String sentenciaNativa="delete from asistencia where fecha_horario >= (?) and fecha_horario <= (?) and empl_id in ("+listaCodigoEmpleado+") and estado not like 'V%'";
				
			try {
								
				PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
				
				java.sql.Date inicio=getDateSql(fechaDesde);				
				java.sql.Date fin=getDateSql(fechaHasta);
						
				preparedStatement.setDate(1, inicio);
				preparedStatement.setDate(2, fin);
						
				preparedStatement.executeUpdate();	
				preparedStatement.close();			
					
				connection.close(); 
					
			} catch (SQLException e) {
				try {					
					connection.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
					e.printStackTrace();
			}	  
		}	

		
		//********************************************************************************************************		
		
		//**********************************MÉTODOS DE CONTROL DE FECHAS******************************************
		
		//Construir Formato de fecha
		public Date CrearFechasAsistencia(Calendar fechaHorario, int hora,
				int minuto) {
			Calendar fecha = Calendar.getInstance();
			fecha.set(fechaHorario.get(Calendar.YEAR),
					fechaHorario.get(Calendar.MONTH),
					fechaHorario.get(Calendar.DAY_OF_MONTH), hora, minuto, 00);
			Date fechaAsistencia = fecha.getTime();
			return fechaAsistencia;
		}
		
		//Obtener variable Calendar en base a una fecha
		public Calendar getCalendar(Date date){
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		}
			
		//Obtener variable Calendar vacía
		public Calendar getCalendar(){
			Calendar calendar=Calendar.getInstance();
			calendar.clear();
			return calendar;
		}
			
		//Obtener año de una fecha
		public int getAño(Date fecha){		
			int año=getCalendar(fecha).get(Calendar.YEAR);
			return año;
		}
			
		//Obtener mes de una fecha
		@SuppressWarnings("deprecation")
		public int getMes(Date fecha){				
			int mes=Integer.parseInt(""+fecha.getMonth());
			return mes;
		}
			
		//Obtener dia de una fecha
		@SuppressWarnings("deprecation")
		public int getDia(Date fecha){
			int dia=fecha.getDate();
			return dia;
		}
			
		//Obtener hora de una fecha
		@SuppressWarnings("deprecation")
		public int getHora(Date fecha){
			int dia=fecha.getHours();
			return dia;
		}
			
		//Obtener minuto de una fecha
		@SuppressWarnings("deprecation")
		public int getMinuto(Date fecha){
			int dia=fecha.getMinutes();
			return dia;
		}
		
		//Obtener minuto de una fecha
		@SuppressWarnings("deprecation")
		public int getSegundo(Date fecha){
			int dia=fecha.getSeconds();
			return dia;
		}
		
		//tranformar tipo de dato Date a DateSQL
		@SuppressWarnings("deprecation")
		public java.sql.Date getDateSql(Date date){
			
			java.sql.Date dateSql=new java.sql.Date(0);
			
			dateSql.setDate(date.getDate());
			dateSql.setMonth(date.getMonth());
			dateSql.setYear(date.getYear());
			
			return dateSql;
		}
		
		@SuppressWarnings("deprecation")
		public java.sql.Timestamp getTimestampSql(Date date){
			
			java.sql.Timestamp timestamp=new java.sql.Timestamp(0);
			
			timestamp.setDate(date.getDate());
			timestamp.setMonth(date.getMonth());
			timestamp.setYear(date.getYear());
			
			timestamp.setHours(0);
			timestamp.setMinutes(0);
			timestamp.setSeconds(0);
			timestamp.setNanos(0);
			
			return timestamp;
		}
		
		//Buscar asistencia en BD para Recargar Timbres
		//@SuppressWarnings("deprecation")
		public List<PlantillaPlanificacion> buscarPermisoHorasEmpleados(List<String> listaCodigoEmpleado, Date fechaDesde, Date fechaHasta){
							
			List<PlantillaPlanificacion> listaplantilla=new ArrayList<PlantillaPlanificacion>();
							
			for(int i=0; i<listaCodigoEmpleado.size(); i++){
							
				Conexion conexion=new Conexion();		
				Connection connection = conexion.getConnection();			
							
				String sentenciaNativa= "select p.empl_id, p.fecha_desde, p.fecha_hasta from permiso p where p.autorizado=3 and p.dias=0 and "
						+ "p.empl_id in ("+listaCodigoEmpleado.get(i)+") and ( "				 
						+ "( p.fecha_desde between (?) and (?) ) or ( p.fecha_hasta between (?) and (?) ) or "
						+ "( (?) between p.fecha_desde and p.fecha_hasta ) or ( (?) between p.fecha_desde and p.fecha_hasta ) ) "
						+ "order by p.empl_id, p.fecha_desde";
										
				try {
													
					PreparedStatement preparedStatement = connection.prepareStatement(sentenciaNativa);
												
					java.sql.Date inicio=getDateSql(fechaDesde);				
					java.sql.Date fin=getDateSql(fechaHasta);
							
					fin.setDate((fin.getDate()+1));
											
					preparedStatement.setDate(1, inicio);
					preparedStatement.setDate(2, fin);		
					preparedStatement.setDate(3, inicio);
					preparedStatement.setDate(4, fin);
					preparedStatement.setDate(5, inicio);
					preparedStatement.setDate(6, fin);						
									
					ResultSet resultSet=preparedStatement.executeQuery();
									
					PlantillaPlanificacion plantilla=new PlantillaPlanificacion();
									
					while(resultSet.next()){
										
						plantilla.setEmpId(resultSet.getLong(1));
											
						plantilla.setFechaPermisoDesde(resultSet.getTimestamp(2));
											
						plantilla.setFechaPermisoHasta(resultSet.getTimestamp(3));
											
						listaplantilla.add(plantilla);
									
						plantilla=new PlantillaPlanificacion();
		
					}			
									
					preparedStatement.close();			
								
					connection.close(); 
										
				} catch (SQLException e) {
					try {
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					e.printStackTrace();
				}					
							
			}	
							
			return listaplantilla;
							
		}
		
		//Filtrar permisos por hora de Empleado
		private String existePermisoHoraFecha(List<PlantillaPlanificacion> listaPermisoHora, Date fecha, List<DetalleHorario> detalleHorario){
				
			String tipo="";
				
			Date fechaAsistencia=getDateWithoutTime(fecha);
			
			int horaEntrada=-1;
			int horaSalida=-1;
			int minutoEntrada=-1;
			int minutoSalida=-1;
			
			for(DetalleHorario dh: detalleHorario){
				
				if(dh.getEntradaSalida().equals("E")){
					horaEntrada=dh.getHora().getHours();
					minutoEntrada=dh.getHora().getMinutes();
				}
				
				if(dh.getEntradaSalida().equals("S")){
					horaSalida=dh.getHora().getHours();
					minutoSalida=dh.getHora().getMinutes();
				}
				
			}			
				
			for( PlantillaPlanificacion p : listaPermisoHora ){
					
				Date fechaPermiso=getDateWithoutTime(p.getFechaPermisoDesde());
				
				int horaDesdePermiso=p.getFechaPermisoDesde().getHours();
				int minutoDesdePermiso=p.getFechaPermisoDesde().getMinutes();
				
				int horaHastaPermiso=p.getFechaPermisoHasta().getHours();
				int minutoHastaPermiso=p.getFechaPermisoHasta().getMinutes();
					
				if( fechaAsistencia.compareTo(fechaPermiso)==0 ){
									
					if(horaEntrada==horaDesdePermiso && minutoEntrada==minutoDesdePermiso){
						tipo="E";
						break;
					}		
					
					if(horaSalida==horaHastaPermiso && minutoSalida==minutoHastaPermiso){
						tipo="S";
						break;
					}
						
				}
					
			}
			
			System.out.println("...................................tipo:"+tipo);
				
			return tipo;
				
		}

		//Crear Lista de Plantilla Planificacion para ingresar en BD
		private List<PlantillaPlanificacion> getAsistencia(Empleado empleado, List<DetalleHorario> listaDetalleHorario, Date fecha, String estado, String tipo){
				
			List<PlantillaPlanificacion> listaPlantilla= new ArrayList<PlantillaPlanificacion>();
			
			Long spEmpleadoId=empleado.getEmplId();
			String spCodigoEmpleado=empleado.getCodigoEmpleado();
			
			for(DetalleHorario dh: listaDetalleHorario){			
				
				PlantillaPlanificacion plantilla= new PlantillaPlanificacion();				
					
				Date date=fecha;				
				Calendar calendar=getCalendar(date);		
					
				if (dh.isNocturno()) {
					calendar = Fechas.sumarRestarDias(calendar, 1);
				}	
					
				SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String fechaCadena = Fechas.cambiarFormato(CrearFechasAsistencia(calendar, getHora(dh.getHora()), getMinuto(dh.getHora())), "yyyy-MM-dd HH:mm:ss");

				Date fechaHoraHorario = null;
				Calendar dateAndTime=null;
					
				try {
					fechaHoraHorario =  formato.parse(fechaCadena);		
					dateAndTime=getCalendar(fechaHoraHorario);
				} catch (ParseException e) {
					e.printStackTrace();
				}
					
				Long spDhId=dh.getDehoId();
				String spEntradaSalida=dh.getEntradaSalida();
				spEntradaSalida=spEntradaSalida.trim();
				int spOrden= dh.getOrden();
				int spMaxMinuto=dh.getMaxMinuto();
					
				Date spFechaHorario=date;		
				Date spHoraHorario=dateAndTime.getTime();	
					
				String cadenaFechaHoraHorario=fechaCadena;
				String cadenaFechaHorario=""+getAño(spFechaHorario)+"-"+(getMes(spFechaHorario)+1)+"-"+getDia(spFechaHorario);
				String candenaHoraHorario=""+getHora(spHoraHorario)+":"+getMinuto(spHoraHorario)+":00";
					
				plantilla.setEmpId(spEmpleadoId);
				plantilla.setDhId(spDhId);
				plantilla.setFechaHoraHorario(cadenaFechaHoraHorario);
				plantilla.setEntradaSalida(spEntradaSalida);
				plantilla.setCodigoEmpleado(spCodigoEmpleado);
				plantilla.setOrden(spOrden);
				plantilla.setMaxMinuto(spMaxMinuto);
				
				if(spEntradaSalida.equals(tipo))
					plantilla.setEstado("P");
				else
					plantilla.setEstado(estado);
					
				plantilla.setFechaHorario(cadenaFechaHorario);
				plantilla.setHoraHorario(candenaHoraHorario);
					
				listaPlantilla.add(plantilla);
			
				}
				
				return listaPlantilla;
				
			}

		
		//*******************************************************************************************************

		public List<PlantillaPlanificacion> getAsistenciaUpdate() {
			if(asistenciaUpdate==null)
				asistenciaUpdate=new ArrayList<PlantillaPlanificacion>();
			return asistenciaUpdate;
		}

		public void setAsistenciaUpdate(List<PlantillaPlanificacion> asistenciaUpdate) {
			this.asistenciaUpdate = asistenciaUpdate;
		}

		public List<PlantillaPlanificacion> getTimbreUpdate() {
			if(timbreUpdate==null)
				timbreUpdate=new ArrayList<PlantillaPlanificacion>();
			return timbreUpdate;
		}

		public void setTimbreUpdate(List<PlantillaPlanificacion> timbreUpdate) {
			this.timbreUpdate = timbreUpdate;
		}

		public List<PlantillaPlanificacion> getPermisoUpdateSalida() {
			if(permisoUpdateSalida==null)
				permisoUpdateSalida=new ArrayList<PlantillaPlanificacion>();
			return permisoUpdateSalida;
		}

		public void setPermisoUpdateSalida(List<PlantillaPlanificacion> permisoUpdateSalida) {
			this.permisoUpdateSalida = permisoUpdateSalida;
		}

		public List<PlantillaPlanificacion> getPermisoUpdateEntrada() {
			if(permisoUpdateEntrada==null)
				permisoUpdateEntrada=new ArrayList<PlantillaPlanificacion>();
			return permisoUpdateEntrada;
		}

		public void setPermisoUpdateEntrada(List<PlantillaPlanificacion> permisoUpdateEntrada) {
			this.permisoUpdateEntrada = permisoUpdateEntrada;
		}

	
}
