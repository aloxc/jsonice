package com.github.aloxc.jsonice.io;

/**
 * 
 * @author leerohwa@gmail.com
 * @date 2016年8月10日
 */
public interface AbstractTypeConverter<T> {
	T convert(String a);
}
