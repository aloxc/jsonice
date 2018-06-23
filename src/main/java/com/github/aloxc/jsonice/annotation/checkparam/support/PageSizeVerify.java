package com.github.aloxc.jsonice.annotation.checkparam.support;


import com.github.aloxc.jsonice.annotation.checkparam.PageSize;
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
public class PageSizeVerify implements Verify {
	private final static Log logger = LogFactory.getLog(PageSizeVerify.class);

	@Override
	public VerifyResult verify(Method method, JsonRequest request) {
		VerifyResult result = null;
		PageSize param = method.getAnnotation(PageSize.class);
		int min = param.min();
		int max = param.max();
		String key = param.key();
		int value = request.getIntParam(key, param.defaultValue());
		if (min > value || max < value || min > max){
			result = new VerifyResult();
			result.setName(key);
			result.setMessage("["+ key +"]中的值须介于["+min+"]到["+max+"]之间");
			return result;
		}
		return null;
	}

	
}
