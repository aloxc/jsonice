package com.github.aloxc.jsonice.annotation;

import java.lang.annotation.*;


/**
 * ice请求的时候，后端处理请求的方法中的参数值绑定注解
 * @author leerohwa@gmail.com
 * @date 2016年6月13日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ParamBinder {
	
	/**
	 * 应用参数检查的注解
	 * @return
	 */
	 Class<? extends Annotation> ref() default Annotation.class;
	 
	 /**
	  * 从json中取值的key
	  * @return
	  */
	 String key() default "";
	 
	 /**
	  * 缺省值
	  * @return
	  */
	 String defaultValue() default "";
	 
	 /**
	  * 是否必传字段
	  * @return
	  */
	 boolean required() default false;
}
