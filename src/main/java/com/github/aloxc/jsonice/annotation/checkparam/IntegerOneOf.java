package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 取值必须是数组元素之一注解
 * @author leerohwa@gmail.com
 * @date 2016年8月5日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IntegerOneOf {

	/**
	 * 缺省值 
	 */
	int defaultValue() default 1;
	
	
	/**
	 * 取值必须是数组元素之一
	 */
	int[] values();
	
	/**
	 * 做参数验证的时候取值的字段名称 
	 */
	String key() ;
	
	
	/**
	 * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
	 */
	int index() default 1;
}
