package com.github.aloxc.jsonice.io;

import com.github.aloxc.jsonice.annotation.checkparam.PageNum;
import com.github.aloxc.jsonice.annotation.checkparam.support.Verify;
import com.github.aloxc.jsonice.utils.ClassUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;


/**
 * 检查serverAop中是否定义了所有校验注解类对应的校验方法
 * @author leerohwa@gmail.com
 * @date 2016年6月14日
 */
@Service
public class ApplicationContextCheckParamClass implements ApplicationContextAware{
	private final static Log logger = LogFactory.getLog(ApplicationContextCheckParamClass.class);

	@SuppressWarnings("rawtypes")
	@Override
	public void setApplicationContext(ApplicationContext appContext) throws BeansException {
		logger.error("开始检查查有没有对应的校验方法");
		Set<Class<?>> anInterfaces = ClassUtil.getClasses(PageNum.class.getPackage(),false);
		Set<Class<?>> verifyClasss = ClassUtil.getClasses(Verify.class.getPackage(),false);
		Set<String> verifyNames = new HashSet<String>();
		String verifyClassName = Verify.class.getName();
		for (Class<?> clazz : verifyClasss) {
			String clazzName = clazz.getName();
			if(clazzName.equals(verifyClassName) //如果是Verify类不做判断
				|| clazzName.contains("$")//某个类的内部类不做验证处理，
					){
				continue;
			}
			Class[] myinterface = clazz.getInterfaces();
			boolean isVerifyClass = false;
			if(myinterface.length > 0){
				for (Class inter : myinterface) {
					if(inter.getName().equals(verifyClassName)){
						isVerifyClass = true;
					}
				}
			}
			if(!isVerifyClass){
				logger.fatal("参数检查注解类对应的处理类[" + (clazzName) + "]必须是实现["+verifyClassName+"]接口的类,程序即将退出!");
				System.exit(1);
			}
			verifyNames.add(clazz.getName());
		}
		
		
		for (Class<?> clazz : anInterfaces) {//检查校验方法是否存在
			String checkClassName = clazz.getPackage().getName().concat(".support.") + clazz.getSimpleName() + "Verify";
			if(!clazz.getPackage().getName().equals(PageNum.class.getPackage().getName()))continue;//只解析本包中的类，不包含子包中的类
			if(!verifyNames.contains(checkClassName)){
				logger.fatal("参数检查注解类对应的处理类[" + (checkClassName) + "]不存在,程序即将退出!");
				System.exit(1);
			}
			
		}
		logger.error("结束检查查有没有对应的校验方法");
	}
	
}
