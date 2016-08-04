package com.casapazmino.fulltime.reportes;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.security.Identity;
import org.jboss.seam.web.ServletContexts;

import com.casapazmino.fulltime.comun.Constantes;
import com.casapazmino.fulltime.comun.GestionPermisoVacacion;
import com.casapazmino.fulltime.model.Empleado;

/**
 * @author SndVll
 */
@Stateless
@Name("reporte")
public class ReporteBean implements Reporte{

	@In
	Identity identity;
	
	@In(create = true)
	GestionPermisoVacacion gestionPermisoVacacion;
	
	@Resource(mappedName = Constantes.DATA_SOURCE)
	protected DataSource dataSource;
	
	public ReporteBean() {
	}
	
	public void crearArchivo(String nombreReporte, Map<String, Object> parametros,
			String rutaReporteFuente, String rutaReporteServidor, String descripcionReporte, String formato) {
		
		String rutaReporteJrxml = rutaReporteFuente + nombreReporte + Constantes.EXT_JRXML;
		String reporteJasper = nombreReporte + Constantes.EXT_JASPER;
		
		int accesoEmpleados = 0;
		
		Connection conn = null;
		
		try {
			
			HttpServletRequest request = ServletContexts.instance()
					.getRequest();
	
			JasperCompileManager
			.compileReportToFile(
					rutaReporteJrxml, 
					request.getSession()
							.getServletContext()
							.getRealPath(rutaReporteServidor + reporteJasper));

			File reportFile = new File(request.getSession().getServletContext()
					.getRealPath(rutaReporteServidor + nombreReporte + Constantes.EXT_JASPER));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(reportFile.getPath());
		

			// RUTA TEMPORAL REAL DE LOS ARCHIVOS EN EL SERVIDOR
			//String logo = request.getSession().getServletContext().getRealPath(Constantes.LOGO);
			String logo = Constantes.LOGO;
			String usuario = identity.getCredentials().getUsername();
			parametros.put("empresa",Constantes.NombreEmpresa);
			parametros.put("logo", logo);
			parametros.put("usuario", usuario);
			parametros.put("rutaPlantilla", Constantes.RUTA_PLANTILLA);
			
			Empleado empleado = gestionPermisoVacacion.buscarEmpleado();
			accesoEmpleados = gestionPermisoVacacion.buscarAccesoEmpleados();
			
			if(accesoEmpleados == Constantes.ACCESO_SUBORDINADOS){
				// TODO: VERIFICAR COMO SE TRANSFORMA DE NUMERO A STRING				
				parametros.put("jefeId", empleado.getEmplId().toString());
			} else
				parametros.put("jefeId", "%");

			
			
			conn = dataSource.getConnection();
			
			
			for(Iterator<String> it = parametros.keySet().iterator(); it.hasNext();){
				String claves = it.next();
				System.out.println("Clave: " + claves + "Valor: " + parametros.get(claves));
	
			}
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parametros, conn);

			// Se usa para ver el reporte en un visor de Jasper.
			//JasperViewer.viewReport(jasperPrint, false);

			ExportarArchivo ea = new ExportarArchivo();
			
			if (formato.equals(Constantes.PDF)){
				//outputStreamArchivo = ea.exportarPdf(jasperPrint, byteArrayOutputStream);
				//ea.abrirReporte(outputStreamArchivo, descripcionReporte, "application/pdf", Constantes.EXT_PDF);
				ea.exportarPdf(jasperPrint, descripcionReporte);
			} else if (formato.equals(Constantes.EXCEL)){
				ea.exportarExcel(jasperPrint, descripcionReporte);
			} else if (formato.equals(Constantes.CSV)){
				ea.exportarCsv(jasperPrint, descripcionReporte);
			} else if (formato.equals(Constantes.XML)){
				ea.exportarXml(jasperPrint, descripcionReporte);
			} else if (formato.equals(Constantes.HTML)){
				ea.exportarHtml(jasperPrint, descripcionReporte);
			} else if (formato.equals(Constantes.TXT)){
				ea.exportarTxt(jasperPrint, descripcionReporte);
			}
		} catch (JRException jre){
			jre.printStackTrace();
		} catch (SQLException sqle){
			sqle.printStackTrace();
 		} catch (Exception e) {
 			e.printStackTrace();
		} finally {
			try {
				if (conn != null) conn.close();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
		}
	}
}