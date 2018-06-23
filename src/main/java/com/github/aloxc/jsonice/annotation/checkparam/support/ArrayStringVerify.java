package com.github.aloxc.jsonice.annotation.checkparam.support;


import com.github.aloxc.jsonice.annotation.checkparam.ArrayString;
import com.github.aloxc.jsonice.io.JsonRequest;
import com.github.aloxc.jsonice.io.VerifyResult;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;


/**
 * int数组的验证类
 * @author leerohwa@gmail.com
 * @date 2016年8月11日
 */
public class ArrayStringVerify implements Verify{

	@Override
	public VerifyResult verify(Method method, JsonRequest request) {
		VerifyResult result = null;
		ArrayString param = method.getAnnotation(ArrayString.class);
		int min = param.elementMinLength();
		int max = param.elementMaxLength();
		int minLength = param.minLength();
		int maxLength = param.maxLength();
		String key = param.key();
		String temp = request.getParam(key);
		String[] values = null;
		if(StringUtils.isEmpty(temp)){
			values = param.defaultValue(); 
		}else{
			values = temp.split(param.split());
		}
		if(values.length > maxLength || values.length < minLength){
			result = new VerifyResult();
			result.setName(key);
			result.setMessage("数组["+ key +"]长度太长或太短，应该介于["+minLength+"]到["+maxLength+"]之间");
			return result;
		}

		if(!(min == max && min == 0)){//元素最大值，如果和min相等且都是0的话就不做大小值验证
			for (int i = 0; i < values.length; i++) {
				if(values[i].length() > max || values[i].length() < min){
					result = new VerifyResult();
					result.setName(key);
					result.setMessage("数组["+ key +"]中的元素["+ values[i] +"]长度太长或太短，应该介于["+min+"]到["+max+"]之间");
					return result;
				}	
			}
		}
		return result;
	}

}
