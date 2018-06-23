package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 每页条数注解类
 * @author leerohwa@gmail.com
 * @date 2016年6月12日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PageSize {
	
	/**
	 * 缺省值 
	 */
	int defaultValue() default 10;
	
	
	/**
	 * 最大取值
	 */
	int max() default 100;
	
	/**
	 * 最小取值 
	 */
	int min() default 1;
	
	/**
	 * 做参数验证的时候取值的字段名称 
	 */
	String key() default "pageSize";
	
	
	/**
	 * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
	 */
	int index() default 1;
}
