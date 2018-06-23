package com.github.aloxc.jsonice.annotation.checkparam;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * value必须是数组的注解
 * @author leerohwa@gmail.com
 * @date 2016年8月11日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ArrayInt {
	
	/**
	 * 缺省值 
	 */
	int[] defaultValue() default {};
	
	/**
	 * 元素最大值，如果和min相等且都是0的话就不做大小值验证
	 */
	int max() default Integer.MAX_VALUE;
	
	/**
	 * 元素最小值
	 */
	int min() default 0;
	
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
	String key() default "arrayInt";
	
	/**
	 * 做参数验证的时候先后顺序，值越小代表验证顺序越在前面验证
	 */
	int index() default 1;
	
	/**
	 * 是否是数组字符串，格式是用中括号括起来的，false：不是（就是需要自己split的），true：是数组字符串，可以直接使用 
	 */
	boolean isArrayString() default false;
}
