package com.sagatechs.generics.facesConverters;

import com.sagatechs.generics.persistence.model.State;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("statusConverter")
public class StatusConverter implements Converter<State> {

	@Override
	public State getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		for (State estado : State.values()) {

			if (estado.getLabel().equals(value)) {
				return estado;
			}
		}

		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, State value) {
		if (value == null) {
			return null;
		}

		return value.getLabel();
	}

}
