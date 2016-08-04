package com.casapazmino.fulltime.comun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;

import com.casapazmino.fulltime.action.TimbreHome;
import com.casapazmino.fulltime.action.TimbreList;
import com.casapazmino.fulltime.model.Timbre;

@Name("leerArchivo")
public class LeerArchivoBean implements LeerArchivo {
	
	@In(create = true)
	TimbreHome timbreHome;
	
	@In(create = true)
	TimbreList timbreList;

	public void leer() {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File(Constantes.RUTA_ARCHIVOS_TIMBRES + Constantes.ARCHIVO_TIMBRES_NO_REGISTRADOS);
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			while ((linea = br.readLine()) != null) {
				
				StringTokenizer mistokens  = new StringTokenizer(linea, ",");
				
				String codigoEmpleado = mistokens.nextToken().trim();
				String fechaHora = mistokens.nextToken().trim();
				String accion = mistokens.nextToken().trim();
				String reloj = mistokens.nextToken().trim();
				
				this.insertarTimbre(codigoEmpleado, fechaHora, accion, reloj);

//				int existeTimbre = this.validarTimbre(codigoEmpleado, fechaHora);
//				if (existeTimbre == 0) {
//					this.insertarTimbre(codigoEmpleado, fechaHora, accion, reloj);
//				}				
			}								
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally eliminanos el archivo, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			archivo.delete();
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	private void insertarTimbre(String codigoEmpleado, String fechaHora, String accion, String reloj) {
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(fechaHora);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Timbre timbre = new Timbre();
		timbre.setCodigoEmpleado(codigoEmpleado);
		timbre.setFechaHoraTimbre(fechaDate);
		timbre.setAccion(accion);
		timbre.setCodigoReloj(reloj);
		
		timbreHome.setInstance(timbre);
		timbreHome.persist();
	}

	// TODO Implementar para revisar si ya existe el timbre que se va insertar
	// No funciona ya que al insertar en la table timbre el sistema redondea los segundos
    // cuando se intenta buscar por igualdad no encuentra los minutos que se han redobndeado
	public int validarTimbre(String codigoEmpleado, String fechaHora) {
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(fechaHora);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		timbreList.getTimbre().setCodigoEmpleado(codigoEmpleado);
		timbreList.setFechaHoraTimbre(fechaDate);

		List<Timbre> timbres = new ArrayList<Timbre>();
		timbres = timbreList.getListaTimbres();
			
		return timbres.size();
		
	}
}
