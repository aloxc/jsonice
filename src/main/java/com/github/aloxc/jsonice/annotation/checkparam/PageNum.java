package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 页码注解类
 * @author leerohwa@gmail.com
 * @date 2016年6月12日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PageNum {
	
	/**
	 * 缺省值 
	 */
	int defaultValue() default 1;
	
	/**
	 * 最小取值
	 */
	int min() default 1;
	
	/**
	 * 最大取值
	 */
	int max() default Integer.MAX_VALUE;
	
	/**
	 * 做参数验证的时候取值的字段名称 
	 */
	String key() default "pageNum";
	
	
	/**
	 * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
	 */
	int index() default 1;
}
