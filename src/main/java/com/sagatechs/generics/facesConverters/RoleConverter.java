package com.sagatechs.generics.facesConverters;

import com.sagatechs.generics.security.model.RoleType;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("roleConverter")
public class RoleConverter implements Converter<RoleType> {

	@Override
	public RoleType getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		}

		for (RoleType role : RoleType.values()) {

			if (role.getLabel().equals(value)) {
				return role;
			}
		}

		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, RoleType value) {
		if (value == null)
			return null;
		return value.getLabel();
	}

}
