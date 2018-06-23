package com.github.aloxc.jsonice.io;


import com.github.aloxc.jsonice.annotation.IceMethod;
import com.github.aloxc.jsonice.annotation.ParamBinder;
import com.github.aloxc.jsonice.annotation.checkparam.PageSize;
import com.github.aloxc.jsonice.annotation.checkparam.support.Verify;
import com.github.aloxc.jsonice.utils.ClassUtil;
import com.zeroc.Ice.Current;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * spring工程相关的资源初始化及ice接口参数验证器初始化
 */
public class ProjectUtil {
	private final static Log logger = LogFactory.getLog(ProjectUtil.class);
	private static volatile ClassPathXmlApplicationContext context;
	private static volatile ProjectUtil INSTANCE;

	public static Map<String, Method> methods = new HashMap<String, Method>();
	public static Map<String, Object> instances = new HashMap<String, Object>();
	private static HashMap<String, Class<?>> VERIFY_CLASS_MAP = new HashMap<String, Class<?>>();
	public final static HashMap<String, List<Class<?>>> PARAM_VERIFY_CLASS_MAP = new HashMap<String, List<Class<?>>>();
	public final static AtomicBoolean IS_INITIALED = new AtomicBoolean(false);
	static {
		logger.error("开始ice服务!");
		Set<Class<?>> verifyClasss = ClassUtil.getClasses(Verify.class.getPackage(),false);
		String verifyClassName = Verify.class.getSimpleName();
		String className = null;
		for (Class<?> clazz : verifyClasss) {
			className = clazz.getSimpleName();
			if (className.equals(verifyClassName)) continue;
			className = className.replaceAll("Verify", "");
			VERIFY_CLASS_MAP.put(className, clazz);
		}
		getContext();

		String[] names = context.getBeanDefinitionNames();//读取所有被spring托管的bean的名称列表
		try {
			Class.forName("javax.servlet.http.HttpServlet");//如果能找到就代表该服务器是http服务器，不做ice方法扫描
		}catch (ClassNotFoundException e){//不能找到http的类就可能是ice服务器，就做ice方法扫描
			for (String name : names) {
				try {
					String proxyObject = context.getBean(name).getClass().getName();
						String clazzName = proxyObject.split("\\$")[0];
						Class clazz = null;
						clazz = Class.forName(clazzName);
						Method[] methods = clazz.getMethods();
						for (Method method : methods) {
							if (method.isAnnotationPresent(IceMethod.class)) {
								init_service(clazz, context.getBean(name), method);
							}


						}
				} catch (ClassNotFoundException classNotFoundException) {
					logger.error("初始化ice的时候无法找到类" + classNotFoundException.getMessage());
				}
			}
			logger.error("ice方法个数" + methods.size()) ;
//			for(Map.Entry<String,Method> entry : methods.entrySet()) {
//				logger.info("ice方法:" + entry.getKey() + "," + entry.getValue().getName() + "," + instances.get(entry.getKey()));
//			}
		}

	}

	public static ClassPathXmlApplicationContext getContext() {
		if(!IS_INITIALED.getAndSet(true)) {
			if (context == null) {
				logger.error("开始初始化context");
				synchronized (ProjectUtil.class) {
					if(context == null) {
						context = new ClassPathXmlApplicationContext("./config/spring-context.xml");
						context.registerShutdownHook();
					}
				}
				logger.error("结束初始化context");
			}
		}

		return context;
	}
	private ProjectUtil(){
	}
	public static ProjectUtil getInstance(){
		if(INSTANCE == null){
			synchronized (ProjectUtil.class){
				if(INSTANCE == null){
					INSTANCE = new ProjectUtil();
				}
			}
		}
		return INSTANCE;
	}

	public boolean isDebug() {
		Boolean debug = false;
		try {
			debug = (Boolean) context.getBean("debug");
		}catch (NoSuchBeanDefinitionException e){
		}
		return debug;
	}


	//找出service中对外提供服务的方法
	@SuppressWarnings({"rawtypes", "unchecked"})
	private synchronized static void init_service(Class<?> c, Object instance, Method iceMethod) {
		IceMethod an = iceMethod.getAnnotation(IceMethod.class);
		String methodName = an.name();
		if (StringUtils.isEmpty(methodName)) {
			methodName = iceMethod.getName();
		}


		if (an.retType() != 1 && an.retType() != 2) {
			logger.fatal("服务名[" + c.getName() + "." + iceMethod.getName() + "]的ServerName注解的retType只能是1或2，请检查配置！");
			System.exit(1);
		}

		if (methods.get(methodName) != null) {
			logger.fatal("服务名[" + c.getName() + "." + iceMethod.getName() + "]有冲突，请检查配置！");
			System.exit(1);
		}
//		logger.error("ice方法" + methodName + "\t" + iceMethod + "\t" + instance);
		methods.put(methodName, iceMethod);
		instances.put(methodName, instance);
		Set<Class<?>> verifyInterfaceSet = ClassUtil.getClasses(PageSize.class.getPackage(),false);
//		logger.error(JsonUtil.toJson(verifyInterfaceSet));
		List<Class<?>> verifyList = null;
		Method indexMethod = null;
		Class<?> verifyClass = null;
		Map<Integer, List<Class<?>>> verifyClassMap = new HashMap<Integer, List<Class<?>>>();
		try {
			// 获取并处理校验方法顺序，被拦截的时候按顺序去调用校验方法
			for (Class verifyInterface : verifyInterfaceSet) {
				if (iceMethod.isAnnotationPresent(verifyInterface)) {
					indexMethod = verifyInterface.getMethod("index");
					Integer idx = (Integer) indexMethod.invoke(iceMethod.getAnnotation(verifyInterface));
					verifyClass = VERIFY_CLASS_MAP.get(verifyInterface.getSimpleName());
					verifyList = verifyClassMap.get(idx);
					if (verifyList == null) {
						verifyList = new ArrayList<Class<?>>();
						verifyClassMap.put(idx, verifyList);
					}
					verifyList.add(verifyClass);
				}
			}
//			logger.error(iceMethod.getName() + "\t" + JsonUtil.toJson(verifyClassMap));
			if (verifyClassMap.size() != 0) {//需要验证
				Integer[] keys = verifyClassMap.keySet().toArray(new Integer[verifyClassMap.size()]);
				Arrays.sort(keys);
				verifyList = new ArrayList<Class<?>>();
				for (Integer key : keys) {
					verifyList.addAll(verifyClassMap.get(key));
				}
//						logger.error("需要验证的规则数量["+methodName+"]" + verifyList.size());
				PARAM_VERIFY_CLASS_MAP.put(c.getSimpleName() + "." + iceMethod.getName(), verifyList);
			}
//			logger.error(c.getSimpleName() + "." + methodName + "\t" + JsonUtil.toJson(PARAM_VERIFY_CLASS_MAP));

			//检查ice方法的参数上面的注解的规范性
			Annotation[][] pans = iceMethod.getParameterAnnotations();
			Class<?>[] paramTypes = iceMethod.getParameterTypes();
			Class<?> paramType = null;
			if(paramTypes[0].getClass().equals(JsonRequest.class)){//第一个参数必须是JsonRequest
				logger.fatal("服务名[" + c.getName() + "." + iceMethod.getName() + "]对应处理方法的第一个参数必须是JsonRequest类型，请检查！");
				System.exit(1);
			}

			if(paramTypes[1].getClass().equals(Current.class)){//第二个参数必须是Ice.Current
				logger.fatal("服务名[" + c.getName() + "." + iceMethod.getName() + "]对应处理方法的第二个参数必须是Ice.Current类型，请检查！");
				System.exit(1);
			}
			if (pans.length > 0) {
				for (int i = 2; i < pans.length; i++) {//第一个参数固定为JsonRequest,第二个参数是Ice.Current类型，这个两个参数就不需要再处理了，只需要处理其它绑定情况
					boolean hasParamBinder = false;
					for (int j = 0; j < pans[i].length; j++) {
						if (pans[i][j].annotationType() == ParamBinder.class) {
							hasParamBinder = true;
						}
					}
					if (!hasParamBinder) {
						logger.fatal("检查ice方法[" + c.getName() + "." + iceMethod.getName() + "]的第[" + (i + 1) + "]个参数,必须添加@ParamBinder注解");
						System.exit(1);
					}


					paramType = paramTypes[i];
					for (int j = 0; j < pans[i].length; j++) {
						ParamBinder paramBinder = (ParamBinder) pans[i][j];
						if(paramBinder.ref() != null && !paramBinder.ref().getName().equals("java.lang.annotation.Annotation") && !iceMethod.isAnnotationPresent(paramBinder.ref())){//检查ref引用的class是否在方法上有注解，
							logger.fatal("检查ice方法[" + c.getName() + "." + iceMethod.getName() + "]的第[" + (i + 1) + "]个参数的ParamBinder中设置了ref["+paramBinder.ref().getName()+"]，但是方法上没有该注解，需要在方法上添加该注解或者修改该参数的ParamBinder为key方式");
							System.exit(1);
						}
						if (TypeHelper.getNumberSimpleType(paramType) != null
								|| paramType.getName().equals(char.class.getName())
								|| paramType.getName().equals(boolean.class.getName())
								) {//8个简单类型必须设置默认值

							String defaultValue = paramBinder.defaultValue();
							boolean canConvert = true;
//									logger.error(paramBinder.ref().getPackage().getName());
//									logger.error(Item.class.getPackage().getName());
							if (StringUtils.isEmpty(defaultValue)) {

								if (!paramBinder.ref().getPackage().getName().equals(PageSize.class.getPackage().getName())) {
									logger.fatal("检查ice方法[" + c.getName() + "." + iceMethod.getName() + "]的第[" + (i + 1) + "]个参数的ParamBinder，必须设置defaultValue,或者参数类型设置为封装类型");
									System.exit(1);
								}
							} else {

								//简单类型的defaultValue还要检查下能否转换为该参数的数据类型
								if (TypeHelper.getNumberSimpleType(paramType) != null) {
									try {
										//做下转换看看能否转换
										new StringToNumberConverter(TypeHelper.getNumberSimpleType(paramType)).convert(defaultValue);
									} catch (Exception e) {
										canConvert = false;
									}
								} else if (paramType.getName().equals(char.class.getName())) {
									byte[] bytes = defaultValue.getBytes();
									if (bytes.length != 1) {
										canConvert = false;
									}
								} else if (paramType.getName().equals(boolean.class.getName())) {
									String low = defaultValue.toLowerCase();
									if (!(low.equals("false") || low.equals("true"))) {
										canConvert = false;
									}
								}
							}
							if (!canConvert) {
								logger.fatal("检查ice方法[" + c.getName() + "." + iceMethod.getName()
										+ "]的第[" + (i + 1) + "]参数ParamBinder的defaultValue，该defaultValue[" + defaultValue + "]不能转换为" + paramType);
								System.exit(1);
							}
						}
					}

				}
			}
		} catch (Throwable t) {
//			、、Class<?> c, Object instance, Method iceMethod
			logger.error("初始化工程配置异常[class=" + c.getClass()+",method=" + iceMethod.getName() + "]", t);
		}


	}
}
