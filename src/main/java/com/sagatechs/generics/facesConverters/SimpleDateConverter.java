package com.sagatechs.generics.facesConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.apache.commons.lang3.StringUtils;

@FacesConverter("simpleDateConverter")
public class SimpleDateConverter implements Converter<Date> {

	@Override
	public Date getAsObject(FacesContext context, UIComponent component, String value) {
		if (StringUtils.isBlank(value)) {
			return null;
		} else {
			 try {
				return new SimpleDateFormat("dd/MM/yyyy").parse(value);
			} catch (ParseException e) {
				throw new ConverterException("Valor inválido de fecha");
			}  
		}

	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Date value) {
		if (value == null) {
			return null;
		} else {
			return new SimpleDateFormat("dd/MM/yyyy").format(value);
		}

	}

}
