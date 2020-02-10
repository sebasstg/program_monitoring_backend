package com.sagatechs.generics.exceptions.facesConverters;


import com.sagatechs.generics.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.primefaces.component.calendar.Calendar;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.time.LocalDate;

@FacesConverter("localDateConverter")
public class LocalDateConverter implements Converter<LocalDate> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(LocalDateConverter.class);
	@Inject
	DateUtils dateUtils;

	@Override
	public LocalDate getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isBlank(value))
			return null;
		else {
			try {
				return this.dateUtils.StringtoLocalDate(value);
			} catch ( Exception e) {
				Calendar calendarUI = (Calendar) component;
				calendarUI.resetValue();
				
				//return null;
				FacesMessage msg = new FacesMessage("Fecha inválida.", "Favor corregir según el formato.");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				
				throw new ConverterException(msg);
			}
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, LocalDate value) {
		if (value == null)
			return "";
		else {
			return this.dateUtils.localDateToString(value);
		}

	}

}
