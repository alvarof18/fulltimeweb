package com.casapazmino.fulltime.comun;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;

import com.casapazmino.fulltime.action.DetalleTipoParametroList;
import com.casapazmino.fulltime.model.DetalleTipoParametro;

@Name("conexion")
public class Conexion{	
		
	private String tipo;
	
	private String parametro;
	
	private String dataSourceName;	
	
	private String nombreClase; 
	
	private String cadenaConexion;
	
	private String usuario;
	
	private String contraseña;
	
	private int longitudLote;
		
	
	public Conexion()
	{
		establecerTipo();
		establecerparametros();
	}
	
	public Connection getConnection(){	
		
		if(tipo.equals("driverConnection")){
			
			return getConnectionDriver();
			
		}		
		else if(tipo.equals("dataSourceConnection")){
			
			return getConnectionDataSource();
			
		}else
			return null;
		
	}
	
	public Connection getConnectionDriver(){
		try{
			Class.forName(nombreClase);
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try{			
			Class.forName(nombreClase).newInstance();
			Connection connection=DriverManager.getConnection(cadenaConexion,usuario,contraseña);
			return connection;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Connection getConnectionDataSource(){		
		try{		
			Context envCtx = new InitialContext();
			DataSource dataSource = (DataSource)envCtx.lookup(dataSourceName);
			Connection connection=dataSource.getConnection();
			return connection;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private void establecerTipo(){
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
		detalleTipoParametros = detalleTipoParametroList.getParametroConexionDriver();				
		String parametroConexion=detalleTipoParametros.getDescripcion();
		parametroConexion=parametroConexion.trim();
		
		parametro=parametroConexion;
			
		if(parametro.charAt(0)=='$')
			tipo="driverConnection";
		else
			tipo="dataSourceConnection";
		
	}
	
	private void establecerparametros(){
		
		parametro=parametro.trim();
		
		if(tipo.equals("driverConnection")){
			
			parametro=parametro.substring(1,parametro.length());
			establecerParametrosDriver(parametro);
			
		}		
		else if(tipo.equals("dataSourceConnection")){
			
			establecerParametrosDataSource(parametro);
			
		}
	}
	
	private void establecerParametrosDriver(String parametro){
		try{
			Cifrado cifrado=new Cifrado();
			
			byte[] cifradoBytes=cifrado.hexStringToByteArray(parametro);
			
			String parametroConexion=cifrado.descifrar(cifradoBytes);
			
			String nombreClase="";
			String cadenaConexion="";
			String usuario="";
			String contraseña="";
			String longitudLoteCadena="";
			
			int count=0;
			
			char letra;
			
			for(int i=0;i<parametroConexion.length();i++){
				letra=parametroConexion.charAt(i);
				if(letra!='|'){
					
					switch(count){
					
					case 0:
						nombreClase=nombreClase+letra;
						break;
						
					case 1:
						cadenaConexion=cadenaConexion+letra;
						break;
					
					case 2:
						usuario=usuario+letra;
						break;
						
					case 3:
						contraseña=contraseña+letra;
						break;
						
					case 4:
						longitudLoteCadena=longitudLoteCadena+letra;
						break;				
					
					}
					
				}else{
					count++;
				}
			}
			
			int longitudLote=Integer.parseInt(longitudLoteCadena);
			
			this.setNombreClase(nombreClase);
			this.setCadenaConexion(cadenaConexion);
			this.setUsuario(usuario);
			this.setContraseña(contraseña);
			this.setLongitudLote(longitudLote);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private void establecerParametrosDataSource(String parametroConexion){
		try{			
			
			String dataSourceName="";
			String longitudLoteCadena="";
			
			int count=0;
			
			char letra;
			
			for(int i=0;i<parametroConexion.length();i++){
				letra=parametroConexion.charAt(i);
				if(letra!='|'){
					
					switch(count){
					
					case 0:
						dataSourceName=dataSourceName+letra;
						break;
						
					case 1:
						longitudLoteCadena=longitudLoteCadena+letra;
						break;				
					
					}
					
				}else{
					count++;
				}
			}
			
			int longitudLote=Integer.parseInt(longitudLoteCadena);
						
			this.dataSourceName=dataSourceName;
			this.setLongitudLote(longitudLote);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public String getNombreClase() {
		return nombreClase;
	}

	public void setNombreClase(String nombreClase) {
		this.nombreClase = nombreClase;
	}

	public String getCadenaConexion() {
		return cadenaConexion;
	}

	public void setCadenaConexion(String cadenaConexion) {
		this.cadenaConexion = cadenaConexion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public int getLongitudLote() {
		return longitudLote;
	}

	public void setLongitudLote(int longitudLote) {
		this.longitudLote = longitudLote;
	}
	
}
