package com.github.aloxc.jsonice.annotation.checkparam.support;


import com.github.aloxc.jsonice.annotation.checkparam.StringPattern;
import com.github.aloxc.jsonice.io.JsonRequest;
import com.github.aloxc.jsonice.io.VerifyResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;


/**
 * 
 * @author leerohwa@gmail.com
 * @date 2016年8月10日
 */
public class StringPatternVerify implements Verify{
	private final static Log logger = LogFactory.getLog(StringPatternVerify.class);

	@Override
	public VerifyResult verify(Method method, JsonRequest request) {
		VerifyResult result = null;
		StringPattern param = method.getAnnotation(StringPattern.class);
		String pattern = param.pattern();
		String key = param.key();
		String value = request.getParam(key, param.defaultValue());
		if(!value.matches(pattern)){
			result = new VerifyResult();
			result.setName(key);
			result.setMessage("["+ pattern +"]无法匹配["+ key +"]的取值["+value+"]");
			return result;
		}
		return null;
	}

}
