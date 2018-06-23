package com.github.aloxc.jsonice.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ice接口方法注释类，用来注释此方法是ice接口方法，被注释的方法的第一个参数应是cn.tianya.ice.JsonRequest
 *
 * @author leerohwa@gmail.com
 * @date 2016年6月12日
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IceMethod {
	
	/**
	 * ice方法名称，格式为  实现类.方法（execute方式的）例如，UserImpl.getUserBatch
	 */
	String name();
	
	
	/**
	 * 返回数据格式
	 * 1：返回简单json
	 * 2：返回带code和message和success这是的json
	 */
	int retType() default 2;
	
	String defaultReturn() default "";
	
	
	/**
	 * 方法描述
	 * @return
	 */
	String description() ;
	
	
	/**
	 * 是否是停止该ice服务
	 * @return
	 */
	boolean stop() default false;
}
