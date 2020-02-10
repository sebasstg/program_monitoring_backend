package com.sagatechs.generics.facesConverters;

import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("booleanSiNoConverter")
public class BooleanSiNoConverter implements Converter<Boolean> {

	@Override
	public Boolean getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		} else {
			if (value.equalsIgnoreCase("SI")) {
				return true;
			} else if (value.equalsIgnoreCase("NO")) {
				return false;
			} else {
				return null;
			}
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Boolean value) {
		if (value == null) {
			return null;
		} else {
			if (value) {
				return "SI";
			} else {
				return "NO";
			}
		}

	}

}
