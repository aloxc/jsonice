package com.github.aloxc.jsonice.io;


import com.github.aloxc.jsonice.utils.JsonUtil;

/**
 * 参数校验结果
 * @author leerohwa@gmail.com
 * @date 2016年8月11日
 */
public class VerifyResult {

	/**
	 * ice请求中的参数字段
	 */
	private String name;
	private IceCode code;
	/**
	 * 参数验证不通过的提示语言
	 */
	private String message;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public IceCode getCode() {
		return code;
	}
	public void setCode(IceCode code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return JsonUtil.toJson(this);
	}
	
}
