package com.casapazmino.fulltime.reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.JRXmlExporterParameter;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import com.casapazmino.fulltime.comun.Constantes;

public class ExportarArchivo {

	@Logger
	Log log;

	public void exportarPdf(JasperPrint jasperPrint, String descripcionReporte) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRPdfExporter exporterPdf = new JRPdfExporter();
		exporterPdf.setParameter(JRPdfExporterParameter.JASPER_PRINT, jasperPrint);
		exporterPdf.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterPdf.exportReport();
		
		this.abrirReporte(byteArrayOutputStream.toByteArray(), descripcionReporte, "application/pdf", Constantes.EXT_PDF);
	}

	  // TAMBIEN EXPORTA A PDF DIRECTAMENTE
	  //output = JasperExportManager
	  //		.exportReportToPdf(jasperPrint);
	  //this.abrirReportePdf(output, descripcionReporte);

	public void exportarExcel(JasperPrint jasperPrint, String descripcionReporte) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRXlsExporter exporterXLS = new JRXlsExporter();
		exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterXLS.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, true);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, false);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
		exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporterXLS.exportReport();
		
		this.abrirReporte(byteArrayOutputStream.toByteArray(), descripcionReporte, "application/vnd.ms-excel", Constantes.EXT_EXCEL);
	}

	public void exportarCsv(JasperPrint jasperPrint, String descripcionReporte) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRCsvExporter exporterCsv = new JRCsvExporter();
		exporterCsv.setParameter(JRCsvExporterParameter.JASPER_PRINT, jasperPrint);
		exporterCsv.setParameter(JRCsvExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterCsv.setParameter(JRCsvExporterParameter.IGNORE_PAGE_MARGINS, true);
		exporterCsv.exportReport();
			  		
		this.abrirReporte(byteArrayOutputStream.toByteArray(), descripcionReporte, "text/csv", Constantes.EXT_CSV);
	}

	public void exportarTxt(JasperPrint jasperPrint, String descripcionReporte) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRTextExporter exporterText = new JRTextExporter();
		
		exporterText.setParameter(JRTextExporterParameter.JASPER_PRINT, jasperPrint);
		exporterText.setParameter(JRTextExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterText.setParameter(JRTextExporterParameter.IGNORE_PAGE_MARGINS, true);
		exporterText.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(6.55));
		exporterText.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(11.90));
		exporterText.exportReport();
		
		this.abrirReporte(byteArrayOutputStream.toByteArray(), descripcionReporte, "text/plain", Constantes.EXT_TXT);
	}
		
	public void exportarXml(JasperPrint jasperPrint, String descripcionReporte) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRXmlExporter exporterXml = new JRXmlExporter();
		exporterXml.setParameter(JRXmlExporterParameter.JASPER_PRINT, jasperPrint);
		exporterXml.setParameter(JRXmlExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterXml.setParameter(JRXmlExporterParameter.IGNORE_PAGE_MARGINS, true);
		this.abrirReporte(byteArrayOutputStream.toByteArray(), descripcionReporte, "text/xml", Constantes.EXT_XML);
	}
	
	public void exportarHtml(JasperPrint jasperPrint, String descripcionReporte) throws JRException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JRHtmlExporter exporterHtml = new JRHtmlExporter();
		exporterHtml.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporterHtml.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
		exporterHtml.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, true);
		exporterHtml.exportReport();
		this.abrirReporte(byteArrayOutputStream.toByteArray(), descripcionReporte, "text/html", Constantes.EXT_HTML);
	} 
	
	public void abrirReporte(byte[] reportePdf, String descripcionReporte, String tipoArchivo, String extensionArchivo) {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			if (reportePdf != null) {
				HttpServletResponse response = (HttpServletResponse) context
						.getExternalContext().getResponse();
				
				response.setContentType(tipoArchivo);
				
				try {
					response.reset();
					response.setDateHeader("Expires", 0);
					response.setContentLength(reportePdf.length);
					response.setHeader("Pragma","no-cache");
					response.setHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
					response.setHeader("Content-disposition",
							"attachment; filename=" + descripcionReporte + extensionArchivo);
					
					ServletOutputStream ouputStream = response.getOutputStream();

					// FUNCIONA BIEN PERO SE REMPLAZO POR LA LINEA DE ABAJO
					// ouputStream.write(arreglo, 0, arreglo.length);
					/*
					 * byte[] buffer = new byte[128]; int bytesRead = 0;
					 * InputStream stream = FileHandler.getInputStreamFroByteArray(arreglo);
					 * while ((bytesRead = stream.read(buffer, 0, 128)) != -1) {
					 * ouputStream.write(buffer, 0, bytesRead); }
					 */

					ouputStream.write(reportePdf, 0, reportePdf.length);
					ouputStream.flush();
					ouputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				log.info(" ========================= ERROR en ReporteBean.abrirReporte " + descripcionReporte);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			context.responseComplete();
		}
	}	
}