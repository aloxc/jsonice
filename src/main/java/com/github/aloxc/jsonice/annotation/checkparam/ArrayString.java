package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author leerohwa@gmail.com
 * @date 2016年8月11日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ArrayString {
	
	String[] defaultValue();
	
	/**
	 * 元素最大值，如果和min相等且都是0的话就不做大小值验证
	 */
	int elementMaxLength() default 0;
	
	/**
	 * 元素最小值
	 */
	int elementMinLength() default 0;
	
	/**
	 * 字符串分割为数字的分割符
	 */
	String split() default ",";
	
	/**
	 * 数组最大长度
	 */
	int maxLength() default 50;
	
	/**
	 * 数组最小长度
	 */
	int minLength() default 0;
	
	/**
	 * 做参数验证的时候取值的字段名称 
	 */
	String key() default "arrayString";
	
	/**
	 * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
	 */
	int index() default 1;
}
