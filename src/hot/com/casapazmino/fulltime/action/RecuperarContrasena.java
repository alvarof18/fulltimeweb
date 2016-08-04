package com.casapazmino.fulltime.action;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.validator.InvalidValue;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Expressions;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;

import com.casapazmino.fulltime.comun.Cifrado;
import com.casapazmino.fulltime.model.DetalleTipoParametro;
import com.casapazmino.fulltime.model.Empleado;
import com.casapazmino.fulltime.seguridad.action.AuditoriaHome;
import com.casapazmino.fulltime.seguridad.action.UsuariosHome;
import com.casapazmino.fulltime.seguridad.action.UsuariosList;
import com.casapazmino.fulltime.seguridad.model.Usuarios;

@Name("recuperar_contraseña")
public class RecuperarContrasena extends EntityHome<Empleado> {

	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	AuditoriaHome auditoriaHome;

	@Logger
	Log log;
	
	@In
	EntityManager entityManager;	
	
	@In(create = true)
	RecuperarContrasena recuperar_contraseña;

	@In(create = true)
	RelojHome relojHome;

	@In(create = true)
	UsuariosHome usuariosHome;
	
	@In
	Credentials credentials;
	
	private String mail;
	private String cedula;
	private String contraseña_actual;
	private String contraseña_nueva;
	private String rep_contraseña_nueva;	

	@In
	private FacesMessages facesMessages;
	
	@Override
	protected void initDefaultMessages() {
		Expressions expressions = new Expressions();
		if (getCreatedMessage() == null) { setCreatedMessage(expressions.createValueExpression("#{messages['mensaje.grabar']}")); }
		if (getUpdatedMessage() == null) { setUpdatedMessage(expressions.createValueExpression("#{messages['mensaje.actualizar']}")); }
		if (getDeletedMessage() == null) { setDeletedMessage(expressions.createValueExpression("#{messages['mensaje.eliminar']}")); }
	}

	public void wire() {		
		getInstance();				
	}

	public boolean isWired() {
		return true;
	}

	public String getContraseña_actual() {
		return contraseña_actual;
	}

	public void setContraseña_actual(String contraseña_actual) {
		this.contraseña_actual = contraseña_actual;
	}

	public String getContraseña_nueva() {
		return contraseña_nueva;
	}

	public void setContraseña_nueva(String contraseña_nueva) {
		this.contraseña_nueva = contraseña_nueva;
	}

	public String getRep_contraseña_nueva() {
		return rep_contraseña_nueva;
	}

	public void setRep_contraseña_nueva(String rep_contraseña_nueva) {
		this.rep_contraseña_nueva = rep_contraseña_nueva;
	}
	
	public void contraseña_usuario(){
		/*log.info("contraseña_actual......................................................:"+this.contraseña_actual);
		log.info("contraseña_actual......................................................:"+this.contraseña_nueva);
		log.info("rep_contraseña_nueva......................................................:"+this.rep_contraseña_nueva);

		log.info("contraseña_actual size......................................................:"+this.contraseña_actual.length());
		log.info("contraseña_actual size......................................................:"+this.contraseña_nueva.length());
		log.info("rep_contraseña_nueva size......................................................:"+this.rep_contraseña_nueva.length());*/
		
		String user=credentials.getUsername();
		UsuariosList usuariosList= (UsuariosList)Component.getInstance("usuariosList", true);	
		Usuarios usuarios=usuariosList.obtenerUsuario(user, "","");
		
		//*************************
		String password=usuarios.getContrasena();
		String desifrado="";
		try{
			if(password.charAt(0)=='$'){
				password=password.substring(1,password.length());
				Cifrado cidfrado=new Cifrado();
				byte[] bx=cidfrado.hexStringToByteArray(password);
				desifrado=cidfrado.descifrar(bx);
			}else{
				desifrado=password;
			}
		}catch(Exception ex){
			desifrado=password;
		}		
		//*************************	
		
		if(this.rep_contraseña_nueva.length()==0||this.contraseña_nueva.length()==0||this.contraseña_actual.length()==0){
			InvalidValue iv= new InvalidValue("Ingrese todos los datos correctamente",null,null,null,null);
			facesMessages.clear();
			facesMessages.add(iv);			
		}else{
			if(this.contraseña_actual.equals(/*usuarios.getContrasena()*/desifrado)){
				if(this.contraseña_nueva.equals(this.rep_contraseña_nueva)){
					if(this.contraseña_nueva.length()>=4){
						usuariosHome.setInstance(usuarios);
						//**************
						Cifrado cifrado=new Cifrado();
						try {
							String nuevaPasswordCifrado="$"+cifrado.toHexadecimal(cifrado.cifrar(this.contraseña_nueva));
							this.contraseña_nueva=nuevaPasswordCifrado;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//**************	
						usuariosHome.getInstance().setContrasena(this.contraseña_nueva);
						usuariosHome.actualizarBean();
						facesMessages.clear();
						facesMessages.add("Se modificó la contraseña correctamente",null);
					}else{
						InvalidValue iv= new InvalidValue("La nueva contraseña debe poseer al menos 4 dígitos",null,null,null,null);
						facesMessages.clear();
						facesMessages.add(iv);	
					}
				}else{
					InvalidValue iv= new InvalidValue("Repita la nueva contraseña correctamente",null,null,null,null);
					facesMessages.clear();
					facesMessages.add(iv);	
				}
			}else{
				InvalidValue iv= new InvalidValue("La contraseña actual es incorrecta",null,null,null,null);
				facesMessages.clear();
				facesMessages.add(iv);
			}
		}			
	}
	
	public void validacion_datos(){		
		
		EmpleadoList empleadolist=(EmpleadoList)Component.getInstance("empleadoList", true);	
		String c=this.cedula,m=this.mail,u="";
		String c2="",m2="";
		
		if(c.length()!=0&&m.length()!=0){
			empleadolist.getEmpleado().setCedula(c);
			empleadolist.getEmpleado().setCorreo(m);
			
			String[] res = {					
					"lower(empleado.cedula) = concat(lower(#{empleadoList.empleado.cedula}))",
					"lower(empleado.correo) = concat(lower(#{empleadoList.empleado.correo}))",	
					};
			
			empleadolist.setMaxResults(1);		
			empleadolist.setRestrictionExpressionStrings(Arrays.asList(res));		
			
			List <Empleado> empleado=empleadolist.getResultList();
			//Empleado em;
			
			for(Empleado e:empleado){
				//em=e;
				c2=e.getCedula();
				m2=e.getCorreo();
				u=e.getUsuarios().getUsuario();
			}			
				if(c2.equals(c) && m2.equals(m)){
					//Recuperar contraseña					
					UsuariosList usuariosList= (UsuariosList)Component.getInstance("usuariosList", true);	
					Usuarios usuarios=usuariosList.obtenerUsuario(u, "","");
					
					usuariosHome.setInstance(usuarios);					
					
					String newclave=nueva_clave();
					
					boolean verificar_envio=false;
					try{
						EnviarMail mail=new EnviarMail();
						mail.enviar("Usuario "+u+" su nueva clave es:<b> "+newclave+"</b>", m, "FULLTIME", "E");
						verificar_envio=true;
					}catch(Exception e){
						verificar_envio=false;
					}
					
					if(verificar_envio){
						usuarios.setContrasena(newclave);
						usuariosHome.setInstance(usuarios);											
						usuariosHome.actualizarBean();			
						facesMessages.clear();
						facesMessages.add("El proceso se realizó correctamente! Verifique su bandeja de correo",null);
					}else{
						InvalidValue iv= new InvalidValue("No se pudo recuperar la clave!",null,null,null,null);
						facesMessages.clear();
						facesMessages.add(iv);
					}
				}else{
					InvalidValue iv= new InvalidValue("Los datos ingresados no son correctos!",null,null,null,null);
					facesMessages.clear();
					facesMessages.add(iv);
					}	
						
		}else{
			InvalidValue iv= new InvalidValue("Ingresé los datos correctamente!",null,null,null,null);
			facesMessages.clear();
			facesMessages.add(iv);	
		}				

		vaciar_datos();
	}	
		
	
	public void vaciar_datos(){
		this.cedula=null;
		this.mail=null;		
		}
	
	public boolean mostrar_enlace(){
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
		detalleTipoParametros = detalleTipoParametroList.getRecuperarContraseña();	
		String parametro=detalleTipoParametros.getDescripcion();
		if(parametro.equals("activar")){
			return true;
		}else{
			return false;
		}
	}
	
	private String nueva_clave(){
		String clave="";
		
		for(int i=0;i<=6;i++){
			
			int total=0;
			boolean desicion=true;
			do{
			int a=(int) (Math.random()*10);
			int b=(int) (Math.random()*100);
			int c=(int) (Math.random()*1000);
			
			total=a+b+c;
			
			if((total>=48 && total <=57)||(total>=65&&total<=90)||(total>=97&&total<=122)){
				desicion=false;
			}else{
				desicion=true;
			}		
			
			}while(desicion);
			
			char CHAR = (char)total;
			clave+=CHAR;
		}	
		return clave;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}	

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}	

}