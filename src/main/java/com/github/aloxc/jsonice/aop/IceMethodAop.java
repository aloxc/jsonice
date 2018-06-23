package com.github.aloxc.jsonice.aop;

import com.github.aloxc.jsonice.annotation.IceMethod;
import com.github.aloxc.jsonice.annotation.ParamBinder;
import com.github.aloxc.jsonice.annotation.checkparam.*;
import com.github.aloxc.jsonice.annotation.checkparam.support.Verify;
import com.github.aloxc.jsonice.io.*;
import com.github.aloxc.jsonice.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.List;

/**
 * ice请求的aop拦截，拦截后做下值得校验，当paramcheck包中新添加注解类的时候，
 * 需要在添加com.github.aloxc.jsonice.annotation.checkparam.support包中添加这样的类
 * public class XXXXVerify
 * XXXX就是新加注解类的类名，该类实现了com.github.aloxc.jsonice.annotation.checkparam.support.Verify接口
 * @author leerohwa@gmail.com
 * @date 2016年6月12日
 */
@Component
@Aspect
@org.springframework.core.annotation.Order(0)
public class IceMethodAop implements BeanFactoryAware{
	private final static Log logger = LogFactory.getLog(IceMethodAop.class);

	private BeanFactory beanFactory;
	@Pointcut("@annotation(com.github.aloxc.jsonice.annotation.IceMethod)")
	public void pointAround() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Around("pointAround()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		JsonRequest request = (JsonRequest) pjp.getArgs()[0];
		Signature signature = pjp.getSignature();
//		logger.error("进入了ice方法拦截器IceMethodAop["+request.getMethod()+"]" + IceMethodAop.class.getAnnotation(org.springframework.core.annotation.Order.class).value());
		MethodSignature methodSignature = (MethodSignature) signature;
		Method proxyMethod = methodSignature.getMethod();
		Method method = pjp.getTarget().getClass().getDeclaredMethod(signature.getName(),
				proxyMethod.getParameterTypes());
		String targetClass = pjp.getTarget().getClass().getSimpleName();
		String targetClassAndMethod = targetClass + "." + request.getMethod();
		// 需要做参数校验的方法列表
		List<Class<?>> checkClassList = ProjectUtil.PARAM_VERIFY_CLASS_MAP.get(targetClassAndMethod);
		// 传入校验方法的参数
		Object[] checkMethodArgs = new Object[2];
		checkMethodArgs[0] = method;
		checkMethodArgs[1] = request;
		int retType = method.getAnnotation(IceMethod.class).retType();
		if(checkClassList != null) {// 对请求参数进行校验
			for (Class<?> clazz : checkClassList) {
//				logger.error(clazz.getName());
				Verify verify = (Verify) clazz.newInstance();
				VerifyResult verifyResult = verify.verify(method, request);
				if (verifyResult != null) {// 验证不通过
					if (retType == 1) {
						if (!StringUtils.isEmpty(verifyResult.getMessage())) {
							return JsonUtil.toError(verifyResult.getMessage());
						} else {
							return JsonUtil.toError(verifyResult.getCode().getMessage());
						}
					} else {// 返回带code和message和success这是的json
						IceResponse res = new IceResponse(null);
						res.setSuccess(IceSuccess.OK);
						if (verifyResult.getName() != null || verifyResult.getCode() == IceCode.PARAMETER_ERROR) {
							res.setCode(IceCode.PARAMETER_ERROR);
							res.setParameter(verifyResult.getName());
							if (!StringUtils.isEmpty(verifyResult.getMessage())) {
								res.setMessage(verifyResult.getMessage());
							}
						} else {
							res.setCode(verifyResult.getCode());
							if (!StringUtils.isEmpty(verifyResult.getMessage())) {
								res.setMessage(verifyResult.getMessage());
							}
						}

						res.setData(null);
						return res;
					}
				}
			}
		}
		
		Annotation[][] pans = method.getParameterAnnotations();
		Object[] args = pjp.getArgs();
		Class<?>[] paramTypes = method.getParameterTypes();
//		Type[] paramTypes = method.getGenericParameterTypes();
		/**
		 * 从jsonRequest中取的对应的值绑定到请求方法的形参上
		 */
		if (pans.length > 0) {
			for (int i = 2; i < pans.length; i++) {//第一个参数固定为JsonRequest,第二个参数是Ice.Current类型，这个两个参数就不需要再处理了，只需要处理其它绑定情况
//				logger.error(i + " " + pans[i].length);	
//				logger.error("参数【"+i+"】" + paramTypes[i]);
				//处理参数绑定请，
				for (int j = 0; j < pans[i].length; j++) {
					if (pans[i][j].annotationType() == ParamBinder.class) {
						ParamBinder paramBinder = (ParamBinder) pans[i][j];
//						logger.error(pb.ref().getPackage().getName());

						if(paramBinder.ref().getPackage().getName().equals(PageNum.class.getPackage().getName())){
							// paramBinder的有ref的情况
							if (paramBinder.ref().getName().equals(PageSize.class.getName())) {
								PageSize param = method.getAnnotation(PageSize.class);
								int value = request.getIntParam(param.key(),param.defaultValue());
								args[i] = value;
							}else if (paramBinder.ref().getName().equals(PageNum.class.getName())) {
								PageNum param = method.getAnnotation(PageNum.class);
								int value = request.getIntParam(param.key(),param.defaultValue());
								args[i] = value;
							}else if (paramBinder.ref().getName().equals(Order.class.getName())) {
								Order param = method.getAnnotation(Order.class);
								int value = request.getIntParam(param.key(),param.defaultValue());
								args[i] = value;
							}else if (paramBinder.ref().getName().equals(StringOneOf.class.getName())) {
								StringOneOf param = method.getAnnotation(StringOneOf.class);
								String value = request.getParam(param.key(), param.defaultValue());
								args[i] = value;
							}else if (paramBinder.ref().getName().equals(IntegerOneOf.class.getName())) {
								IntegerOneOf param = method.getAnnotation(IntegerOneOf.class);
								Integer value = request.getIntParam(param.key(),param.defaultValue());
								args[i] = value;
							}else if (paramBinder.ref().getName().equals(Ip.class.getName())) {
								Ip param = method.getAnnotation(Ip.class);
								String userIp = null;
								if(param.fromHeader()){
									userIp = request.getHeader("userIp",null);
								}else{
									userIp = request.getParam(param.key());
								}
								args[i] = userIp;
							}else if (paramBinder.ref().getName().equals(ArrayInt.class.getName())) {
								ArrayInt param = method.getAnnotation(ArrayInt.class);
								int[] values = null;
								String temp = request.getParam(param.key());
								if(StringUtils.isEmpty(temp)){
									values = param.defaultValue(); 
								}else{
									try{
										String[] temps = temp.split(param.split());
										values = new int[temps.length];
										for (int ai = 0; ai < temps.length; ai++) {
											values[ai] = Integer.parseInt(temps[ai]);
										}
									}catch(Exception e){
										values = request.getIntArrayParams(param.key());
									}
								}
								args[i] = values;
							}else if (paramBinder.ref().getName().equals(ArrayString.class.getName())) {
								ArrayString param = method.getAnnotation(ArrayString.class);
								String[] values = null;
								String temp = request.getParam(param.key());
								if(StringUtils.isEmpty(temp)){
									values = param.defaultValue(); 
								}else{
									values = temp.split(param.split());
								}
								args[i] = values;
							}
						}else{
							//paramBinder无ref的情况
							String paramTypeName = paramTypes[i].getName();
							Class paramType = paramTypes[i];
							String strValue = request.getParam(paramBinder.key(),paramBinder.defaultValue());
							boolean required = paramBinder.required();
							String defaultValue = paramBinder.defaultValue();
							if(required && StringUtils.isEmpty(strValue)){//必须要传的字段
								if (retType == 1) {
									return JsonUtil.toError("参数[" + paramBinder.key() + "]不能为空");
								} else {// 返回带code和message和success这是的json
									IceResponse res = new IceResponse(null);
									res.setSuccess(IceSuccess.OK);
									res.setCode(IceCode.PARAMETER_ERROR);
									res.setParameter(paramBinder.key());
									res.setData(null);
									return res;
								}
							}
							Class simpleType = TypeHelper.getNumberSimpleType(paramType);
//							logger.error("boolean字段 = " + paramBinder.key() + "," + simpleType + ",值=" + strValue);
							if(simpleType != null){//数字类型
								if(!StringUtils.isEmpty(strValue)){
									args[i] = new StringToNumberConverter(simpleType).convert(strValue);
								}else{
									args[i] = new StringToNumberConverter(simpleType).convert(defaultValue);
								}
							}else if(paramTypeName.equals(String.class.getName())){//String类型参数
								args[i] = strValue;
							}else if(paramTypeName.equals(boolean.class.getName())){
								args[i] = request.getBooleanParam(paramBinder.key(), Boolean.parseBoolean(defaultValue));
							}else if(paramTypeName.equals(Boolean.class.getName()) ){
								if(StringUtils.isEmpty(strValue)){
									if(StringUtils.isEmpty(defaultValue)){
										args[i] = null;
									}else{
										args[i] = Boolean.parseBoolean(defaultValue);	
									}
								}else{
									args[i] = Boolean.parseBoolean(strValue);	
								}
							}else if(TypeHelper.isNumberWrap(paramType)){
//								logger.error("封装类" + paramType);
								if(StringUtils.isEmpty(strValue)){
									if(StringUtils.isEmpty(defaultValue)){
										args[i] = null;
									}else{
										args[i] = new StringToNumberConverter(paramType).convert(defaultValue);	
									}
								}else{
									args[i] = new StringToNumberConverter(paramType).convert(strValue);
								}
							}else if(paramTypeName.equals(char.class.getName())) {
								if (StringUtils.isEmpty(strValue)) {
									if (StringUtils.isEmpty(defaultValue)) {
										args[i] = 'A';
									} else {
										args[i] = defaultValue.charAt(0);
									}
								} else {
									args[i] = strValue.charAt(0);
								}
							}else if(paramType.getName().equals(Timestamp.class.getName())){
								if(StringUtils.isEmpty(strValue)){
									if(StringUtils.isEmpty(defaultValue)) {
										args[i] = null;
									}else{
										args[i] = Timestamp.valueOf(defaultValue);
									}
								}else{
									args[i] = Timestamp.valueOf(strValue);
								}
							}else{//绑定的是包装类型的
								logger.error(targetClass +"."+ method.getName()+"参数["+(i+1)+"]" + paramTypeName + "需要修改" + IceMethodAop.class.getName() + ",在其中增加绑定代码");

							}
						}
					}
				}
			}
		}
		
		Object ret = pjp.proceed(args);
		return ret;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory =  beanFactory;
	}
}
