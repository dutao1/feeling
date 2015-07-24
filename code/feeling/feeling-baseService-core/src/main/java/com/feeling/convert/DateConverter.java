package com.feeling.convert;

 

import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class DateConverter implements Converter {

	private Object defaultValue = null;
	private boolean useDefault = true; 
	
	public DateConverter(Object defaultValue) {
	    
        this.defaultValue = null;
        this.useDefault = true;
    }
 
	
	@Override
	public Object convert(Class type, Object value) {
		 if (value == null || "".equals(value)) {
	            if (useDefault) {
	                return (defaultValue);
	            } else {
	                throw new ConversionException("No value specified");
	            }
	        }
	        if (value instanceof Date) {
	            return (value);
	        }
	        return null;
	}
	
	 
}

