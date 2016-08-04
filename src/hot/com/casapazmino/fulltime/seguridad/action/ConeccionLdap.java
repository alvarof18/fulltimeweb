/**
 * 
 */
package com.casapazmino.fulltime.seguridad.action;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class ConeccionLdap {
			
    private String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
//    private String MY_HOST = "ldap://uiosrv-dc01";
    
    public String authenticate(String host, String domain, String user, String pass) { 
            Hashtable env = new Hashtable();

            if (pass.compareTo("") == 0 || user.compareTo("") == 0) return null;
            env.put(Context.INITIAL_CONTEXT_FACTORY,INITCTX);
            env.put(Context.PROVIDER_URL, host);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, user+ "@"  + domain);
            env.put(Context.SECURITY_CREDENTIALS,pass);
            env.put(Context.REFERRAL, "follow");

            try {
            	
            	DirContext ctx = new InitialDirContext(env);
            }
            catch (NamingException e) {
                    e.printStackTrace();
                    return null;
            }
            return user;  
    }
}
