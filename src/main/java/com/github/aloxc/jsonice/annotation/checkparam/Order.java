package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 排序注解类
 * @author leerohwa@gmail.com
 * @date 2016年8月3日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Order {
	
	/**
	 * 缺省值 
	 */
	int defaultValue() default 1;
	
	
	/**
	 * 取值必须是数组元素之一
	 */
	int[] values() default {0,1,2};
	
	/**
	 * 做参数验证的时候取值的字段名称 
	 */
	String key() default "order";
	
	
	/**
	 * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
	 */
	int index() default 1;
}
