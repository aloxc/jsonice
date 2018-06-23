package com.github.aloxc.jsonice.annotation.checkparam.support;

import com.github.aloxc.jsonice.io.JsonRequest;
import com.github.aloxc.jsonice.io.VerifyResult;

import java.lang.reflect.Method;


/**
 * 参数校验注解的处理器
 * @author leerohwa@gmail.com
 * @date 2016年8月10日
 */
public interface Verify {
	/**
	 * 验证方法，
	 * @param method
	 * @param request
	 * @return null：验证通过，否则不通过，不通过的结果见返回的verifyResult中相关属性
	 */
	VerifyResult verify(Method method, JsonRequest request);
}
