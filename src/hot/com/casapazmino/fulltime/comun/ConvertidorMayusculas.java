package com.casapazmino.fulltime.comun;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.jboss.seam.annotations.intercept.BypassInterceptors;

@org.jboss.seam.annotations.faces.Converter
@BypassInterceptors
public class ConvertidorMayusculas implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent uiCompoment, String valor) {
		return valor.toString().toUpperCase();
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent uiCompoment, Object valor) {
		return valor.toString().toUpperCase();
		
	}

/*	<h:inputText id="code" required="true" size="20" maxlength="20"
		    value="#{clientHome.instance.code}"  // solo si es necesario poner este estilo para la presentacion style="text-transform:uppercase"
		    converter="#{upperCaseConverter}"> 
*/	
}
