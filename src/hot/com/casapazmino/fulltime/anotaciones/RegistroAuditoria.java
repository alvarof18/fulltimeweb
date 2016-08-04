package com.casapazmino.fulltime.anotaciones;

import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.jboss.seam.annotations.intercept.Interceptor;
import org.jboss.seam.async.AsynchronousInterceptor;
import org.jboss.seam.core.Expressions;

@Interceptor(stateless = true, around = AsynchronousInterceptor.class)
public class RegistroAuditoria{

/*	@AroundInvoke
	public Object logMethodEntry(InvocationContext invocationContext) throws Exception {
		

		if (invocationContext.getMethod().getName().equals("update")) {
			AuditoriaHome auditoriaHome = (AuditoriaHome) Component.getInstance("auditoriaHome",true);
			
			auditoriaHome.asignarCampos(invocationContext.getTarget().getClass().getName(), invocationContext.getMethod().getName(), "c_ant", "c_act");
			auditoriaHome.persist();
		}
		
		return invocationContext.proceed();
	}*/
	
    @AroundInvoke
    public Object aroundInvoke(InvocationContext invocationContext) throws Exception {

        Method method = invocationContext.getMethod();
 
        if (method.isAnnotationPresent(Tracked.class)) {

            Tracked trackedAnnotaion = method.getAnnotation(Tracked.class);
            for (TrackedValue trv : trackedAnnotaion.values()) {
            	Object eval = Expressions.instance().createValueExpression(trv.expr()).getValue();
                
                if (eval != null) {
                    String param = trv.parameter();
                    String val = eval.toString();
 
                    if (!"".equals(param) && !"".equals(val)) {
                    }
                }
            }
 
/*          AuditoriaHome auditoriaHome = (AuditoriaHome) Component.getInstance("auditoriaHome",true);
            auditoriaHome.asignarCampos(invocationContext.getTarget().getClass().getName(), invocationContext.getMethod().getName(), "c_ant", "c_act");
            auditoriaHome.persist();*/
 
        }

        return invocationContext.proceed();
    }
}