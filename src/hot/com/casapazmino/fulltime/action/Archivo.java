package com.casapazmino.fulltime.action;


import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import com.casapazmino.fulltime.model.Permiso;

@Name("archivosList")
public class Archivo extends EntityHome<Permiso>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String archivo;
	private String url;
	
	
	public Archivo(String a, String b)
	{
		archivo=a;
		url=b;
	}


	public String getArchivo() {
		return archivo;
	}


	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	/*File archivo;
	
	public Archivo(String file)
	{
		this.archivo = new File(file);
	}
	
	public List<String> LeerArchivo()
	{
		List<String> listaLeida = new ArrayList<String>();
		FileReader fr = null;
		BufferedReader br = null;
		try
		{
			fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
	        while((linea=br.readLine())!=null)
	           listaLeida.add(linea);
	        
		}
		catch(Exception exp)
		{
			System.out.println(exp.getMessage());
		}finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
		return listaLeida;
	}

	public void EscribirArchivo(String file,List<String> lista)
	{
		 FileWriter fichero = null;
		 PrintWriter pw = null;
	        try
	        {
	            fichero = new FileWriter(file);
	            pw = new PrintWriter(fichero);
	 
	            for (int i = 0; i < lista.size() ; i++)
	                pw.println(lista.get(i));
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally {
	            try {
	                // Nuevamente aprovechamos el finally para 
	                // asegurarnos que se cierra el fichero.
	                if (null != fichero)
	                   fichero.close();
	                } catch (Exception e2) {
	                   e2.printStackTrace();
	                }
	             }
	}*/
	
}
