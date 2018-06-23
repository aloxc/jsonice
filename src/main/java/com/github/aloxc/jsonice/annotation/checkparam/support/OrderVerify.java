package com.github.aloxc.jsonice.annotation.checkparam.support;


import com.github.aloxc.jsonice.annotation.checkparam.Order;
import com.github.aloxc.jsonice.io.JsonRequest;
import com.github.aloxc.jsonice.io.VerifyResult;
import com.github.aloxc.jsonice.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * 
 * @author leerohwa@gmail.com
 * @date 2016年8月10日
 */
public class OrderVerify implements Verify {
	private final static Log logger = LogFactory.getLog(OrderVerify.class);

	@Override
	public VerifyResult verify(Method method, JsonRequest request) {
		VerifyResult result = null;
		Order param = method.getAnnotation(Order.class);
		int[] values = param.values();
		String key = param.key();
		int value = request.getIntParam(key, param.defaultValue());
		Arrays.sort(values);
		if(Arrays.binarySearch(values, value) < 0){
			result = new VerifyResult();
			result.setName(key);
			result.setMessage("["+ key +"]的取值只能是"+ JsonUtil.toJson(values)+"的一个");
			return result;
		}
		return null;
	}

}
