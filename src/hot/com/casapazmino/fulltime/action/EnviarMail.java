package com.casapazmino.fulltime.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jboss.seam.Component;

import com.casapazmino.fulltime.model.DetalleTipoParametro;

public class EnviarMail {
	
	Properties props;
	Session session;
	String msgBody;
	String usuario,direccion,alias,contrasenia,smptHost,port,transport,rutakey;
	boolean starTTLS,autenticacion;

	public EnviarMail()
	{
		
		//new arguments of mail
		
		DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
		DetalleTipoParametro detalleTipoParametros = new DetalleTipoParametro();
		
		detalleTipoParametros = detalleTipoParametroList.getCorreoPrincipal();
		//this.usuario=detalleTipoParametros.getDescripcion();
		EstablecerUsuario(detalleTipoParametros.getDescripcion());
		
		detalleTipoParametros = detalleTipoParametroList.getPassword_Correo();		
		this.contrasenia=detalleTipoParametros.getDescripcion();
		
		detalleTipoParametros = detalleTipoParametroList.getServidorCorreo();	
		this.smptHost=detalleTipoParametros.getDescripcion();	
		
		detalleTipoParametros = detalleTipoParametroList.getTTLSCorreo();	
		String aux=detalleTipoParametros.getDescripcion();	
		
		detalleTipoParametros = detalleTipoParametroList.getRutaCertifiacosJava();
		this.rutakey=detalleTipoParametros.getDescripcion();	
		
		if(aux.equals("true")){
			this.starTTLS = true;
		}else{
			this.starTTLS = false;
		}
		
		detalleTipoParametros = detalleTipoParametroList.getAutenticacionCorreo();	
		aux=detalleTipoParametros.getDescripcion();	
		
		if(aux.equals("ok")){
			this.autenticacion = true;
		}else{
			this.autenticacion = false;
		}
		
		detalleTipoParametros = detalleTipoParametroList.getPuertoCorreo();
		this.port =detalleTipoParametros.getDescripcion();
		
		//end new arguments
		
		System.out.println("THE FINAL VERSION.....!!!");
		System.out.println("usuario::::::::::::::::::"+this.usuario);
		System.out.println("direccion::::::::::::::::::"+this.direccion);
		System.out.println("alias::::::::::::::::::"+this.alias);
		System.out.println("contrasenia::::::::::::::::::"+this.contrasenia);
		System.out.println("smptHost::::::::::::::::::"+this.smptHost);
		System.out.println("starTTLS::::::::::::::::::"+this.starTTLS);
		System.out.println("autenticacion::::::::::::::::::"+this.autenticacion);
		System.out.println("port::::::::::::::::::"+this.port);
		System.out.println("rutakey::::::::::::::::::"+this.rutakey);
		
		this.props = new Properties();
		
		if(this.starTTLS){
			this.props.setProperty("mail.smtp.starttls.enable", "true");
			this.props.setProperty("mail.smtp.host", this.smptHost);
			this.props.setProperty("mail.smtp.port", this.port);
			this.transport="smtp";
			
				if(this.autenticacion){
					this.props.setProperty("mail.smtp.auth", "true");}
				else{
					this.props.setProperty("mail.smtp.auth", "false");}
			System.out.println("TLS TRUE.............................!!!");
			}			
		else{
			
			this.props.setProperty("mail.smtps.host", this.smptHost);
			this.props.setProperty("mail.smtps.port", this.port);						
			/*this.props.put("mail.smtps.socketFactory.port", this.port);
			this.props.put("mail.smtps.socketFactory.class","javax.net.ssl.SSLSocketFactory");
			this.props.put("mail.smtps.ssl.trust", this.smptHost);*/
		    this.transport="smtps";
		    
				if(this.autenticacion){
					this.props.setProperty("mail.smtps.auth", "true");}
				else{
					this.props.setProperty("mail.smtps.auth", "false");}	
			System.out.println("SSL TRUE.............................!!!");
		}
		
		if(this.rutakey.equals("NA")){
			System.out.println("NOT USING CERTFICATES!!!");
		}else{
			System.out.println("USING CERTFICATES FROM: "+this.rutakey);
			System.setProperty("javax.net.ssl.trustStore", this.rutakey);
			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");	
		}	
		
		this.session = Session.getInstance(this.props);		
		this.session.setDebug(true);
		this.msgBody = "";
	}
	
	private void EstablecerUsuario(String parametro){
		// TODO Auto-generated method stub
		
		//sintaxis:usuario,direciión,alias
		//String parametro="Fabricio Pilicita,sistemas@casapazmino.com.ec,FABO";
		
//		String usuario="";
//		String direccion="";
//		String alias="";
		
		String aux="";
		int j=0;
		
		for(int i=0;i<parametro.length();i++){
			
			if(parametro.charAt(i)==','){
				if(j==0){
					usuario=aux;
				}else{
					direccion=aux;
				}
				
				aux="";
				j++;
			}else{
				aux+=parametro.charAt(i);
			}
				
		}
		
		alias=aux;

	}
	
	
	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getMsgBody() {
		return msgBody;
	}

	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	
	
	
	public String getUsuario() {
		return usuario;
	}



	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}



	public String getContrasenia() {
		return contrasenia;
	}



	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}



	public String getSmptHost() {
		return smptHost;
	}



	public void setSmptHost(String smptHost) {
		this.smptHost = smptHost;
	}



	public String getPort() {
		return port;
	}



	public void setPort(String port) {
		this.port = port;
	}



	public boolean isStarTTLS() {
		return starTTLS;
	}



	public void setStarTTLS(boolean starTTLS) {
		this.starTTLS = starTTLS;
	}



	public boolean isAutenticacion() {
		return autenticacion;
	}
	
	

	public void setAutenticacion(boolean autenticacion) {
		this.autenticacion = autenticacion;
	}
	

	public void enviar(String contenido,String para,String Asunto,String cc)
	{
		try
		{
			DetalleTipoParametroList detalleTipoParametroList1 = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
			DetalleTipoParametro detalleTipoParametros1 = new DetalleTipoParametro();
			detalleTipoParametros1 = detalleTipoParametroList1.getRutaCabecera();			
			
			String cabecera=detalleTipoParametros1.getDescripcion();	
			
			detalleTipoParametros1 = detalleTipoParametroList1.getRutaPie();
			
			String pie=detalleTipoParametros1.getDescripcion();	
			
			//new
			String formulario =
					"<html>"+
						"<body>"+
							"<img src='cid:cidcabecera' width=900 height=90>"+
							"<br>"+
							"<br>"+			
							contenido+			
							"<br>"+
							"<br>"+
							"<img src='cid:cidpie' width=900 height=100>"+
							"<br>"
						+ "</body>"
					+ "</html>";
			
			
			try {
				addContent(formulario);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				//ENCABEZADO Y PIE
				try {
					addCID("cidcabecera", cabecera);
					addCID("cidpie",pie);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//
			//
			
			Message msg = new MimeMessage(session);
			try {
				msg.setFrom(new InternetAddress(direccion,alias));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			msg.addRecipient(Message.RecipientType.TO, 
					new InternetAddress(para));
			
			//enviar copia oculta CCO	
			if(cc.equals("J")){
				DetalleTipoParametroList detalleTipoParametroList = (DetalleTipoParametroList) Component.getInstance("detalleTipoParametroList", true);
				List<DetalleTipoParametro> detalleTipoParametros = new ArrayList<DetalleTipoParametro>();
				detalleTipoParametros = detalleTipoParametroList.getListaCorreos();
				
				for (DetalleTipoParametro detalleTipoParametro : detalleTipoParametros) {
					//System.out.println("=========================================================== correos j:" + detalleTipoParametro.getDescripcion());				
					msg.addRecipients(Message.RecipientType.BCC, new InternetAddress[] { new InternetAddress(""+detalleTipoParametro.getDescripcion())});	
				}
			}										
			//
			
			msg.setText("FULLTIME");
			msg.setDescription("FULLTIME");
						
			msg.setSubject(Asunto);						
			msg.setContent(multipart);
			
			Transport t = this.session.getTransport(this.transport);
			t.connect(this.usuario,this.contrasenia);
			t.sendMessage(msg, msg.getAllRecipients());
			
			multipart= new MimeMultipart("related");
		}
		catch(MessagingException error)
		{
			error.printStackTrace();
		}
	}
	
	//new method
	MimeMultipart multipart = new MimeMultipart("related");
	public void addContent(String htmlText) throws Exception
    {       
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(htmlText, "text/html");       
        this.multipart.addBodyPart(messageBodyPart);
    }
	
	public void addCID(String cidname,String pathname) throws Exception
    {
        DataSource fds = new FileDataSource(pathname);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID","<"+cidname+">");
        this.multipart.addBodyPart(messageBodyPart);
    }
}
