package com.feeling.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author dutao
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface NotEmpty {
	
	public int minLength()  ;
	
	public int maxLength()  ;
	
	public String desc()  default "" ;
}
