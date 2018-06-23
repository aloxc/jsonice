package com.github.aloxc.jsonice.io;


import com.github.aloxc.jsonice.utils.Integers;
import com.github.aloxc.jsonice.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 格式化请求json
 * @author aloxc
 * 
 */
public class JsonRequest{
	private String method = null;
	private HashMap<String, String> params = null;
	private HashMap<String, String> header = null;
	private String json = null;
	private HashMap<String, Object> req = null;

	/**
	 * 私有构造函数，json不为空的时候，就用json初始化JsonRequest。为空就用默认的
	 * @param json
	 */
	@SuppressWarnings("unchecked")
	private JsonRequest(String json) {
		if (json != null) {
			this.json = json;
			req = JsonUtil.toBean(json, HashMap.class);
			params = (HashMap<String, String>) req.get("params");
			header = (HashMap<String, String>) req.get("header");
			method = (String) req.get("method");
			if(header == null) header = new HashMap<String, String>();
			if(params == null) params = new HashMap<String, String>();
		} else {
			req = new HashMap<String, Object>();
			params = new HashMap<String, String>();
			header = new HashMap<String, String>();
		}
	}
	
	/**
	 * 实例化一个JsonRequst对象，json不为空的时候，就用json初始化JsonRequest。为空就用默认的
	 * @param json
	 * @return
	 */
	public static JsonRequest newInstance(String json) {
		return new JsonRequest(json);
	}
	/**
	 * 获取请求的方法
	 * @return
	 */
	public String getMethod( ) {
		return method;
	}
	/**
	 * 设置请求的方法名称
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取请求参数列表
	 * @return
	 */
	public Map<String, String> getParams( ) {
		return params;
	}
	/**
	 * 获取请求参数对应的值
	 * @param key 请求的参数名称
	 * @return
	 */
	public String getParam(String key) {
		return params.get(key);
	}
	/**
	 * 获取请求参数对应的值
	 * @param key 请求的参数名称
	 * @param defaultVal 如果参数中没有对应的值，就返回defaultVal
	 * @return String
	 */
	public String getParam(String key, String defaultVal) {
		String temp = params.get(key);
		if (temp == null) temp = defaultVal;
		return temp;
	}
	/**
	 * 获取请求参数对应的int值
	 * @param key 参数名称
	 * @param defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return int
	 */
	public int getIntParam(String key, int defaultNum) {
		String temp = params.get(key);
		if ((temp != null) && (!temp.equals(""))) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}

	/**
	 * 获取请求参数对应的float值
	 * @param key 参数名称
	 * @param defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return float
	 */
	public float getFloatParam(String key, float defaultNum) {
		String temp = params.get(key);
		if ((temp != null) && (!temp.equals(""))) {
			float num = defaultNum;
			try {
				num = Float.parseFloat(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}
	
	/**
	 * 获取请求参数对应的byte值
	 * @param key 参数名称
	 * @param defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return byte
	 */
	public float getByteParam(String key, byte defaultNum) {
		String temp = params.get(key);
		if ((temp != null) && (!temp.equals(""))) {
			byte num = defaultNum;
			try {
				num = Byte.parseByte(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}
	
	/**
	 * 获取请求参数对应的byte值
	 * @param key 参数名称
	 * @param defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return short
	 */
	public float getShortParam(String key, short defaultNum) {
		String temp = params.get(key);
		if ((temp != null) && (!temp.equals(""))) {
			short num = defaultNum;
			try {
				num = Byte.parseByte(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}
	
	
	/**
	 * 设置请求的参数及值
	 * @param key 参数名
	 * @param value 值
	 */
	public void setParams(String key, Object value) {
		if(StringUtils.isEmpty(key)) return;
		if(value == null) return;
		params.put(key, value.toString());
	}
	
	/**
	 * 设置请求头的参数及值
	 * @param key 参数名
	 * @param value 值
	 */
	public void setHeader(String key,Object value){
		if(StringUtils.isEmpty(key)) return;
		if(value == null) return;
		header.put(key, value.toString());
	}

	/**
	 * 获取请求参数对应的long值
	 * @param key 参数名称
	 * @param defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return long
	 */
	public long getLongParam(String key, long defaultNum) {
		String temp = params.get(key);
		if (!StringUtils.isEmpty(temp)) {
			long num = defaultNum;
			try {
				num = Long.parseLong(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}
	
	/**
	 * 获取请求参数对应的布尔值
	 * @param key 参数名称
	 * @param defaultVal 如果参数中没有对应的值，就返回defaultNum
	 * @return
	 */
	public boolean getBooleanParam(String key, boolean defaultVal) {
		String temp = params.get(key);
		if (("true".equals(temp)) || ("on".equals(temp))) return true;
		if (("false".equals(temp)) || ("off".equals(temp))) {
			return false;
		}
		return defaultVal;
	}
	
	/**
	 * 获取请求参数对应的double值
	 * @param key 参数名称
	 * @param defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return
	 */
	public double getDoubleParam(String key, double defaultNum) {
		String temp = params.get(key);
		if (!StringUtils.isEmpty(temp)) {
			double num = defaultNum;
			try {
				num = Double.parseDouble(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}
	
	/**
	 * 覆盖toString方法
	 */
	public String toString( ) {
		if (json != null) return json;
		req = new HashMap<String, Object>();
		req.put("method", method);
		req.put("params", params);
		req.put("header", header);
		json = JsonUtil.toJson(req);
		return json;
	}
	/**
	 * 返回此请求的json串
	 * @return
	 */
	public String toJson( ) {
		return this.toString();
	}
	
	/**
	 * 获取请求参数中的ArrayList字段
	 * @param key 请求的参数名称
	 * @param defaultList 缺省返回的ArrayList
	 * @return
	 */
	public ArrayList<?> getArrayListParam(String key ,ArrayList<?> defaultList){
		Object temp = params.get(key);
		if(temp != null){
			ArrayList<?> list = defaultList;
			try{
				list = (ArrayList<?>) temp;
			}catch(Exception e){
			}
			return list;
		}
		return defaultList;
	}
	/**
	 * 获取请求参数列表
	 * @return
	 */
	public Map<String, String> getHeader( ) {
		return header;
	}
	/**
	 * 获取请求参数对应的值
	 * @head key 请求的参数名称
	 * @return
	 */
	public String getHeader(String key) {
		return header.get(key);
	}
	/**
	 * 获取请求参数对应的值
	 * @head key 请求的参数名称
	 * @head defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return String
	 */
	public String getHeader(String key, String defaultNum) {
		String temp =header.get(key);
		if (temp == null) temp = defaultNum;
		return temp;
	}
	/**
	 * 获取请求参数对应的int值
	 * @head key 参数名称
	 * @head defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return int
	 */
	public int getIntHeader(String key, int defaultNum) {
		String temp = header.get(key);
		if ((temp != null) && (!temp.equals(""))) {
			int num = defaultNum;
			try {
				num = Integer.parseInt(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}

	/**
	 * 获取请求参数对应的long值
	 * @head key 参数名称
	 * @head defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return long
	 */
	public long getLongHeader(String key, long defaultNum) {
		String temp = header.get(key);
		if (!StringUtils.isEmpty(temp)) {
			long num = defaultNum;
			try {
				num = Long.parseLong(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}
	
	/**
	 * 获取请求参数对应的布尔值
	 * @head key 参数名称
	 * @head defaultVal 如果参数中没有对应的值，就返回defaultNum
	 * @return
	 */
	public boolean getBooleanHeader(String key, boolean defaultVal) {
		String temp = header.get(key);
		if (("true".equals(temp)) || ("on".equals(temp))) return true;
		if (("false".equals(temp)) || ("off".equals(temp))) {
			return false;
		}
		return defaultVal;
	}
	
	/**
	 * 获取请求参数对应的double值
	 * @head key 参数名称
	 * @head defaultNum 如果参数中没有对应的值，就返回defaultNum
	 * @return
	 */
	public double getDoubleHeader(String key, double defaultNum) {
		String temp = header.get(key);
		if (!StringUtils.isEmpty(temp)) {
			double num = defaultNum;
			try {
				num = Double.parseDouble(temp);
			} catch (Exception e) {
			}
			return num;
		}
		return defaultNum;
	}
	
	/**
	 * 获取请求参数中的ArrayList字段
	 * @head key 请求的参数名称
	 * @head defaultList 缺省返回的ArrayList
	 * @return
	 */
	public ArrayList<?> getArrayListHeader(String key ,ArrayList<?> defaultList){
		Object temp = header.get(key);
		if(temp != null){
			ArrayList<?> list = defaultList;
			try{
				list = (ArrayList<?>) temp;
			}catch(Exception e){
			}
			return list;
		}
		return defaultList;
	}
	
	/**
	 * 设置请求int数组参数
	 * @param key 请求参数名
	 * @param values 请求参数值
	 */
	public void setIntArrayParams(String key, int[] values) {
		if (StringUtils.isEmpty(key)) {
			return;
		}
		if (values == null || values.length == 0) {
			return;
		}
		params.put(key, JsonUtil.toJson(values));
	}
	
	/**
	 * 获取请求参数int数组值
	 * @param key 请求参数名
	 * @return 请求参数值
	 */
	public int[] getIntArrayParams(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		String value = params.get(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		try {
			Integer[] integers = JsonUtil.toBean(value, Integer[].class);
			return Integers.intValues(integers);
		} catch (Exception e) {
		}
		return null;
	}
	
	public static void main(String[] args) {
		JsonRequest req = JsonRequest.newInstance(null);
		req.setMethod("testmethod");
		req.setParams("a1", 123);
		req.setParams("a2", 43L);
		req.setParams("a3", "aloxc");
		req.setParams("a4", true);
		req.setParams("a5", "leer\"ohwa");
		req.setIntArrayParams("a6", new int[]{1,2,3});
		req.setHeader("userId", 336604);
		System.out.println(req);
		
		System.out.println(req.getMethod());
		int[] ivalue = req.getIntArrayParams("a6");
		for (int i = 0; i < ivalue.length; i++) {
			System.out.println(ivalue[i]);
		}
//		System.out.println(req.getIntParam("a1", 567));
//		System.out.println(req.getIntParam("a9", 567));
		
//		System.out.println("header:"  + req.getIntHeader("userId",332211));
//		System.out.println(req.getParams());
//		JsonRequest req1 = JsonRequest.newInstance(req.toString());
//		System.out.println(req1.getMethod());
//		System.out.println(req1.getIntParam("a1", 567));
//		System.out.println(req1.getIntParam("a9", 567));
//		System.out.println(req1.getParam("a6","aaa"));
//		System.out.println(req.getParams());
	}
}
