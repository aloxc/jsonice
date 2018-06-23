package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author leerohwa@gmail.com
 * @date 2016年8月5日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StringOneOf {

	/**
	 * 缺省值 
	 */
	String defaultValue() default "";
	
	
	/**
	 * 取值必须是数组元素之一
	 */
	String[] values();
	
	/**
	 * 做参数验证的时候取值的字段名称 
	 */
	String key() ;
	
	
	/**
	 * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
	 */
	int index() default 1;

	/**
	 * 是否允许为空，默认不允许
	 * @return
     */
	boolean nullable()default false;
}
