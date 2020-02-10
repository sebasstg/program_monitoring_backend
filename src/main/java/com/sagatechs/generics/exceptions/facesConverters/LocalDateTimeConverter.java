package com.sagatechs.generics.exceptions.facesConverters;

import com.sagatechs.generics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.time.LocalDateTime;


@FacesConverter("localDateTimeConverter")
public class LocalDateTimeConverter implements Converter<LocalDateTime> {
	

	private static final Logger LOGGER = Logger.getLogger(LocalDateTimeConverter.class);
	@Inject
	DateUtils dateUtils;
	
	@Override
	public LocalDateTime getAsObject(FacesContext context, UIComponent component, String value) {
		if(StringUtils.isBlank(value)) return null; else {
			return this.dateUtils.StringtoLocalDateTime(value);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, LocalDateTime value) {
		if (value == null)
			return "";
		else {
			return this.dateUtils.localDateTimeToString(value);
		}
		
		
	}

}
