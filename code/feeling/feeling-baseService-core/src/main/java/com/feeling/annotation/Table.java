package com.feeling.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.bind.annotation.XmlElement.DEFAULT;

/**
 * 
 * 声明一个java类为domain类，映射数据库某个表
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	/**
	 * 映射的表名
	 * @return
	 */
	String name();
	
	/**
	 * 主键名
	 * @return
	 */
	String pkName() default "id";
}
