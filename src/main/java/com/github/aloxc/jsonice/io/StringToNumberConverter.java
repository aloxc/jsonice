package com.github.aloxc.jsonice.io;

import org.springframework.util.NumberUtils;


/**
 * 数据类型转换器
 * @author leerohwa@gmail.com
 * @date 2016年8月10日
 */
@SuppressWarnings("rawtypes")
public class StringToNumberConverter<T extends Number> implements AbstractTypeConverter{
	Class<T> t;
	public StringToNumberConverter(Class<T> t) {
		this.t = t;
	}
	@Override
	public T convert(String a) {
		return NumberUtils.parseNumber(a, t);
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		StringToNumberConverter c = new StringToNumberConverter(Integer.class);
		Number n = c.convert("3");
		System.out.println(n.getClass());
		
	}
}
