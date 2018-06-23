package com.github.aloxc.jsonice.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * java数据类型帮助类
 * @author leerohwa@gmail.com
 * @date 2016年8月10日
 */
public class TypeHelper {
	@SuppressWarnings("rawtypes")
	//数子类型对应的封装类型
	private final static Map<Class,Class> NUMBER_TYPE_SIMPLE_MAP = new HashMap<Class,Class>();
	@SuppressWarnings("rawtypes")
	private final static List<Class> NUMBER_TYPE_WRAP_LIST = new ArrayList<Class>();
	static{
		NUMBER_TYPE_SIMPLE_MAP.put(byte.class, Byte.class);
		NUMBER_TYPE_SIMPLE_MAP.put(double.class, Double.class);
		NUMBER_TYPE_SIMPLE_MAP.put(int.class, Integer.class);
		NUMBER_TYPE_SIMPLE_MAP.put(long.class, Long.class);
		NUMBER_TYPE_SIMPLE_MAP.put(float.class, Float.class);
		NUMBER_TYPE_SIMPLE_MAP.put(short.class, Short.class);
		
		NUMBER_TYPE_WRAP_LIST.add(Byte.class);
		NUMBER_TYPE_WRAP_LIST.add(Double.class);
		NUMBER_TYPE_WRAP_LIST.add(Integer.class);
		NUMBER_TYPE_WRAP_LIST.add(Long.class);
		NUMBER_TYPE_WRAP_LIST.add(Float.class);
		NUMBER_TYPE_WRAP_LIST.add(Short.class);
	}
	
	/**
	 * 读取java数子类型简单类型对应的封装类型
	 * @param fromType
	 * @return
	 */
	public static Class<?> getNumberSimpleType(Class<?> fromType){
		return NUMBER_TYPE_SIMPLE_MAP.get(fromType);
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isNumberWrap(Class type){
		return NUMBER_TYPE_WRAP_LIST.contains(type);
	}
}
