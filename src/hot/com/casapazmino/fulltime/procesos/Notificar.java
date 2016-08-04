package com.casapazmino.fulltime.procesos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Lifecycle;
import org.jboss.seam.security.Credentials;

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.action.PermisoList;
import com.casapazmino.fulltime.action.SolicitudVacacionList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Permiso;
import com.casapazmino.fulltime.model.SolicitudVacacion;

@Scope(ScopeType.APPLICATION)
@Name("Notificar")
@BypassInterceptors
public class Notificar extends HttpServlet {

	private static final long serialVersionUID = 8750596808938047779L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Notificar() {

		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		doPost(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// NEW Lifecycle.beginCall ();

//		Lifecycle.beginCall();
//		DetalleTipoParametroList detalleTipoParametroList1 = (DetalleTipoParametroList) Component
//				.getInstance("detalleTipoParametroList", true);
//		DetalleTipoParametro detalleTipoParametros1 = new DetalleTipoParametro();

//		detalleTipoParametros1 = detalleTipoParametroList1
//				.getActivarNotificacionMailFaltas();
//		String activo = detalleTipoParametros1.getDescripcion();
//		if (activo.equals("activar faltas")) {
			// ****************************************
			// try {
			// detalleTipoParametros1 =
			// detalleTipoParametroList1.getHoraFaltas();
			// String h=(""+detalleTipoParametros1.getDescripcion());
			// detalleTipoParametros1 =
			// detalleTipoParametroList1.getMinutosFaltas();
			// String m=(""+detalleTipoParametros1.getDescripcion());
			//
			// InitialContext ctx = new InitialContext();
			// StdScheduler scheduler = (StdScheduler) ctx.lookup("Quartz");
			//
			// JobDetail jd=new
			// JobDetail("myjob1",scheduler.DEFAULT_GROUP,Faltas.class);
			//
			// CronTrigger ct = new
			// CronTrigger("cronTrigger","group2","0 "+m+" "+h+" * * ?");
			// scheduler.scheduleJob(jd,ct);
			//
			// } catch (Exception exc) {
			// //exc.printStackTrace();
			//
			// }
			// ****************************************
//		}

//		detalleTipoParametros1 = detalleTipoParametroList1
//				.getActivarNotificacionMailAtrasos();
//		String activo2 = detalleTipoParametros1.getDescripcion();
//		if (activo2.equals("activar atrasos")) {
			// try {
			// detalleTipoParametros1 =
			// detalleTipoParametroList1.getMinutosRangoAtrasosSalidasAntes();
			// int
			// m=Integer.parseInt(""+detalleTipoParametros1.getDescripcion());
			// InitialContext ctx = new InitialContext();
			// StdScheduler scheduler = (StdScheduler) ctx.lookup("Quartz");
			//
			// JobDetail jd=new
			// JobDetail("myjob2",scheduler.DEFAULT_GROUP,Atrasos.class);
			//
			// CronTrigger ct = new
			// CronTrigger("cronTrigger","group1","0 0/"+m+" * * * ?");
			// scheduler.scheduleJob(jd,ct);
			//
			// } catch (Exception exc) {
			// //exc.printStackTrace();
			//
			// }
//		}

		
//		JobDetail job = JobBuilder.newJob(TareaCrearHorario.class)
//				.withIdentity("crearHorario").build();
//
//		Trigger trigger = TriggerBuilder
//				.newTrigger()
//				.withSchedule(
//						SimpleScheduleBuilder.simpleSchedule()
//								.withIntervalInSeconds(5).repeatForever())
//								.startNow()
//				.build();
		
		
//		scheduler = new StdSchedulerFactory().getScheduler();
//		scheduler.start();

//		try {
//			InitialContext ctx = new InitialContext();
//			StdScheduler scheduler = (StdScheduler) ctx.lookup("Quartz");
//			scheduler.start();
//			scheduler.scheduleJob(job, trigger);
//		} catch (SchedulerException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (NamingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
//		detalleTipoParametros1 = detalleTipoParametroList1
//				.getActivarPeriodoVacacionesAutomatica();
//		String activo3 = detalleTipoParametros1.getDescripcion();
//		if (activo3.equals("activar pv")) {
//			try {
//				detalleTipoParametros1 = detalleTipoParametroList1
//						.getHoraPeriodoVacacion();
//				String h = ("" + detalleTipoParametros1.getDescripcion());
//				detalleTipoParametros1 = detalleTipoParametroList1
//						.getMinutoPeriodoVacacion();
//				String m = ("" + detalleTipoParametros1.getDescripcion());
//
//				InitialContext ctx = new InitialContext();
//				StdScheduler scheduler = (StdScheduler) ctx.lookup("Quartz");
//
//				JobDetail jd = new JobDetail("myjob3", scheduler.DEFAULT_GROUP,
//						PeriodoVacacionAutomatico.class);
//
//				CronTrigger ct = new CronTrigger("cronTrigger", "group3", "0 "
//						+ m + " " + h + " * * ?");
//				scheduler.scheduleJob(jd, ct);

//			} catch (Exception exc) {
//				// exc.printStackTrace();
//
//			}
//		}

//		Lifecycle.endCall();

		String user = request.getParameter("user");
		String url = request.getParameter("url");

		// //////////////////
		if (url != null) {
			try {
				String nom = "";
				String nom1 = "";
				for (int i = (url.length() - 1); i >= 0; i--) {
					if (url.charAt(i) == '\\') {
						break;
					} else {
						nom1 = nom1 + url.charAt(i);
					}
				}

				for (int i = (nom1.length() - 1); i >= 0; i--) {
					nom = nom + nom1.charAt(i);
				}

				File f = new File(url);
				RandomAccessFile raf = new RandomAccessFile(url, "r");
				FileInputStream fis = new FileInputStream(f);
				FileReader fr = new FileReader(f);
				byte b[] = new byte[(int) f.length()];
				raf.read(b);

				// cabecera
				response.setHeader("Content-Type", "img/jpeg");
				response.setIntHeader("Content-Length", (int) f.length());
				response.setHeader("Content-disposition",
						"attachment; filename=" + nom);

				// lo escribimos
				OutputStream out = response.getOutputStream();
				out.write(b);
				out.close();
				raf.close();
				fis.close();
				fr.close();
			} catch (Exception e) {

			}

		} else {
			// ///////////////////////
			response.setContentType("application/x-json;charset=UTF-8");
			PrintWriter out = response.getWriter();

			// System.out.println(jsonObject);

			Lifecycle.beginCall();
			// llamada a clases Bean

			// Verificar autorizacion de notificaciones
			String jsonObject = "[]";

			DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component
					.getInstance("detalleTipoParametroList", true);
			DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
			detalleTipoParametros = detalleTipoParametroList
					.getNotificacionesSistema();

			if (detalleTipoParametros.getDescripcion().equals("activar")) {
				PermisoList permisoList = (PermisoList) Component.getInstance(
						"permisoList", true);
				SolicitudVacacionList solicitudlist = (SolicitudVacacionList) Component
						.getInstance("solicitudVacacionList", true);
				Credentials credentials = (Credentials) Component
						.getInstance(Credentials.class);

				credentials.setUsername(user);

				// //////////////Notificaciones de solictudes de permisos

				// Calendar fechaDesde = Calendar.getInstance();
				// fechaDesde.set(1, 0, 1);
				// permisoList.getPermiso().setFechaDesde(fechaDesde.getTime());
				// permisoList.setAutorizado("NO");

				permisoList.setMaxResults(20);

				permisoList.setAutorizado(null);
				permisoList.setEstado("0");
				permisoList.getPermiso().setFechaDesde(null);

				List<Permiso> permisos = new ArrayList<Permiso>();
				// permisos=permisoList.getListaPermisos();
				permisos = permisoList.getListaPermisoNiveles();

				jsonObject = "[";

				for (Permiso permi : permisos) {
					// if(permi.getDetalleTipoParametro().getDtpaId()==4){
					String all = "";
					if (permi.getDias() == 0)
						all = "all";
					String fechaDesde = RetornarFecha(permi.getFechaDesde(),
							all);
					String fechaHasta = RetornarFecha(permi.getFechaHasta(),
							all);

					if (permi.getEmpleadoByEmplId().getUsuarios().getUsuario()
							.equals(user)) {
						jsonObject += "{";
						jsonObject += "\"titulo\":\"SOLICITUD DE PERMISO\",";
						jsonObject += "\"contenido\":\"Usted ha realizado una solicitud de permiso desde el "
								+ fechaDesde + " hasta el " + fechaHasta + "\"";
						jsonObject += "},";

					} else {
						jsonObject += "{";
						jsonObject += "\"titulo\":\"SOLICITUD DE PERMISO\",";
						jsonObject += "\"contenido\":\""
								+ "El empleado "
								+ permi.getEmpleadoByEmplId().getNombre()
								+ " "
								+ permi.getEmpleadoByEmplId().getApellido()
								+ " ha realizado una solicitud de permiso desde el "
								+ fechaDesde + " hasta el " + fechaHasta + "\"";
						jsonObject += "},";
					}

				}

				// //////////////Notificaciones de solictudes vacaciones

				// solicitudlist.getSolicitudVacacion().setFechaDesde(fechaDesde.getTime());
				// solicitudlist.setAutorizado("NO");

				solicitudlist.setMaxResults(20);
				solicitudlist.setAutorizado(null);
				solicitudlist.setEstado("0");
				solicitudlist.getSolicitudVacacion().setFechaDesde(null);

				List<SolicitudVacacion> solicitud = new ArrayList<SolicitudVacacion>();
				solicitud = solicitudlist.getListaSolicitudVacacionNiveles();

				for (SolicitudVacacion permi : solicitud) {
					String fechaDesde = RetornarFecha(permi.getFechaDesde(), "");
					String fechaHasta = RetornarFecha(permi.getFechaHasta(), "");
					// if(permi.getDetalleTipoParametro().getDtpaId()==4){

					if (permi.getEmpleadoByEmplId().getUsuarios().getUsuario()
							.equals(user)) {
						jsonObject += "{";
						jsonObject += "\"titulo\":\"SOLICITUD DE VACACIONES\",";
						jsonObject += "\"contenido\":\"Usted ha realizado una solicitud de vacaciones desde el "
								+ fechaDesde + " hasta el " + fechaHasta + "\"";
						jsonObject += "},";

					} else {
						jsonObject += "{";
						jsonObject += "\"titulo\":\"SOLICITUD DE VACACIONES\",";
						jsonObject += "\"contenido\":\""
								+ "El empleado "
								+ permi.getEmpleadoByEmplId().getNombre()
								+ " "
								+ permi.getEmpleadoByEmplId().getApellido()
								+ " ha realizado una solicitud de vacaciones desde el "
								+ fechaDesde + " hasta el " + fechaHasta + "\"";
						jsonObject += "},";
					}
				}
				
				// Notificacion de Inicio de Vacaciones
				solicitud.clear();
				solicitudlist.setMaxResults(20);
				solicitudlist.setAutorizado(null);
				solicitudlist.setEstado("2");
				solicitudlist.getSolicitudVacacion().setFechaDesde(new Date());
				solicitud = solicitudlist.getListaSolicitudVacacionNiveles();
				
				for (SolicitudVacacion permi : solicitud) {
					String fechaHasta = RetornarFecha(permi.getFechaHasta(), "");
					// if(permi.getDetalleTipoParametro().getDtpaId()==4){

					if (permi.getEmpleadoByEmplId().getUsuarios().getUsuario()
							.equals(user)) {
						jsonObject += "{";
						jsonObject += "\"titulo\":\"INICIO DE VACACIONES\",";
						jsonObject += "\"contenido\":\"Usted ha comenzado su periodo de vacaciones "
									+ " hasta el " + fechaHasta + "\"";
						jsonObject += "},";

					} else {
						jsonObject += "{";
						jsonObject += "\"titulo\":\"INICIO DE VACACIONES\",";
						jsonObject += "\"contenido\":\""
								+ "El empleado "
								+ permi.getEmpleadoByEmplId().getNombre()
								+ " "
								+ permi.getEmpleadoByEmplId().getApellido()
								+ " ha comienza hoy su periodo de vacaciones"
								+ "	hasta el " + fechaHasta + "\"";
						jsonObject += "},";
					}
				}				

				jsonObject += "]";

				// System.out.println("bieennnnn!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

			} else {
				// System.out.println("no se autorizo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			}

			Lifecycle.endCall();

			out.print(jsonObject);
			out.close();
		}

	}

	private String RetornarFecha(Date date, String a) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(date);

		String year = "" + calendario.get(Calendar.YEAR);
		String month = "" + (calendario.get(Calendar.MONTH) + 1);
		String day = "" + calendario.get(Calendar.DATE);
		String nameDay = getDiaSemana(calendario.get(Calendar.DAY_OF_WEEK));

		if ((calendario.get(Calendar.MONTH)) < 10)
			month = "0" + month;

		if ((calendario.get(Calendar.DATE)) < 10)
			day = "0" + day;

		String fecha = "";

		if (a.equals("all")) {
			String hour = "" + calendario.get(Calendar.HOUR_OF_DAY);
			String minute = "" + calendario.get(Calendar.MINUTE);

			if (calendario.get(Calendar.HOUR_OF_DAY) < 10)
				hour = "0" + hour;

			if (calendario.get(Calendar.MINUTE) < 10)
				minute = "0" + minute;

			fecha = nameDay + " " + day + "/" + month + "/" + year + " " + hour
					+ ":" + minute;
		} else {
			fecha = nameDay + " " + day + "/" + month + "/" + year;
		}

		// System.out.println(fecha);
		return fecha;
	}

	private String getDiaSemana(int a) {
		String dia = "";

		switch (a) {
		case 0:
			dia = "Sábado";
			break;
		case 1:
			dia = "Domingo";
			break;
		case 2:
			dia = "Lunes";
			break;
		case 3:
			dia = "Martes";
			break;
		case 4:
			dia = "Miércoles";
			break;
		case 5:
			dia = "Jueves";
			break;
		case 6:
			dia = "Viernes";
			break;
		}

		return dia;
	}

	/*
	 * @Override public void getResource(final HttpServletRequest arg0, final
	 * HttpServletResponse arg1) throws ServletException, IOException { // TODO
	 * Auto-generated method stub new ContextualHttpServletRequest(arg0) {
	 * 
	 * @Override public void process() throws IOException { doWork(arg0, arg1);
	 * } }.run();
	 * 
	 * }
	 * 
	 * @Override public String getResourcePath() { // TODO Auto-generated method
	 * stub return "/Notificar"; }
	 * 
	 * private void doWork(HttpServletRequest request, HttpServletResponse
	 * response) { PrintWriter out=null; try { out = response.getWriter(); }
	 * catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * String jsonObject="["+ "{"+ "\"id\":\"0\","+ "\"mar\":\"Samsung\","+
	 * "\"mod\":\"UE50ES6900S 50 SMART TV\""+ "},"+ "{"+ "\"id\":\"1\","+
	 * "\"mar\":\"Sony HD\","+ "\"mod\":\"Sony Kdl-40bx455 Bravia Televisor\""+
	 * 
	 * "},"+ "{"+ "\"id\":\"2\","+ "\"mar\":\"Daewoo\","+
	 * "\"mod\":\"Lcd 42 Full Hd 1080p\""+ "}"+ "]";
	 * 
	 * out.print(jsonObject); out.close(); }
	 */

}
