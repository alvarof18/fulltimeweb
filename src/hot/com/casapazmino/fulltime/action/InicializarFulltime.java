/**
 * 
 */
package com.casapazmino.fulltime.action;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Startup;
import org.jboss.seam.log.Log;

/**
 * @author Drosan
 *
 */
@Startup
public class InicializarFulltime {

	@Logger
	private Log log;
	
    @PostConstruct
    public void initByeEJB() { 
        log.info("ByeEJB se está inicializando...");
        
        
        
        
    }
 
    public String sayBye() { 
        log.info("ByeEJB está diciendo bye..."); 
        return "Bye!";
    }
 
    @PreDestroy
    public void destroyByeEJB() { 
        log.info("ByeEJB está siendo destruido..."); 
    }
}
