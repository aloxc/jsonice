package com.github.aloxc.jsonice.io;

import com.github.aloxc.jsonice.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ICE响应类
 * @author leerohwa@gmail.com
 * @date 2016年8月5日
 */
public class IceResponse<T> implements Serializable{
	private final static Log logger = LogFactory.getLog(IceResponse.class);
	private static final long serialVersionUID = -2781531102728319063L;
	private String json = null;
	private T data = null;
	private String message = null;
	private IceCode code = null;
	private IceSuccess success = null;
	private String parameter = null;
	private static HashMap<Integer, IceSuccess> successIntMap = new HashMap<Integer, IceSuccess>();
	private static HashMap<Integer, IceCode> codeIntMap = new HashMap<Integer, IceCode>();
	
	public IceResponse(String response){
		if (!StringUtils.isEmpty(response)) {
			this.json = response;
			parseResult();
		}
	}

	public void setCode(IceCode code){
		this.code = code;
	}
	
	public void setData(T data){
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSuccess(IceSuccess success){
		this.success = success;
	}

	public IceSuccess getSuccess() {
		return success;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	
	public String toString(){
		LinkedHashMap<String, Object> ret = new LinkedHashMap<String, Object>();
		ret.put("success", String.valueOf(success.value()));//
		String msg = code.getMessage() + (parameter != null && code == IceCode.PARAMETER_ERROR ? " parameter:" + parameter : "");
		ret.put("message", StringUtils.isEmpty(message) ? msg : message);
		ret.put("data", data == null ? new HashMap<Object, Object>() : data);
		ret.put("code", String.valueOf(code.value()));
		return JsonUtil.toJson(ret);
	}
	
	private void parseResult() {
		// 判断是否为空
		if (StringUtils.isEmpty(json)) {
			return;
		}
		try {
			// 将Json字符串转成Map
			Map<String, ?> responseMap = JsonUtil.toBean(json, HashMap.class);
			// 获取success
			if (responseMap.containsKey("success")) {
				success = successIntMap.get(Integer.parseInt(String.valueOf(responseMap.get("success"))));
			}
			// 获取code
			if (responseMap.containsKey("code")) {
				code = codeIntMap.get(Integer.parseInt(String.valueOf(responseMap.get("code"))));
			}
			// 获取message
			if (responseMap.containsKey("message")) {
				message = String.valueOf(responseMap.get("message"));
			}
			// 获取data
			if (responseMap.containsKey("data")) {
				data =  (T)responseMap.get("data");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}

}
