package com.casapazmino.fulltime.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@ValidatorClass(DuplicadoBean.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Duplicado {

//	String consulta();

	String message();
	
	String componente();
	
	String entidad();
	
	String campo();
	
	String campoId();
}
