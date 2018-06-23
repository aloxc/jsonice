package com.github.aloxc.jsonice.io;

import com.github.aloxc.jsonice.annotation.IceMethod;
import com.github.aloxc.jsonice.exception.IllegalCheckException;
import com.github.aloxc.jsonice.utils.IPUtil;
import com.github.aloxc.jsonice.utils.IceUtil;
import com.github.aloxc.jsonice.utils.JsonUtil;
import com.zeroc.Ice.Current;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * 通过反射处理ice请求
 * Created by aloxc on 2017/7/18 in project tianya_bbs_movecontent.
 */
public class IceCall {
    private final static Log logger = LogFactory.getLog(IceCall.class);
    public static String call(Class clazz,JsonRequest request, Current current){
        int retType = 1;
        try{
            long start = System.currentTimeMillis();

            String methodName = request.getMethod();
            String result = null;
            String serviceClassName = clazz.getSimpleName();
            final String serviceMethodName = serviceClassName + "." + methodName;
            Method iceMethod = ProjectUtil.methods.get(serviceMethodName);
            Object instance = ProjectUtil.instances.get(serviceMethodName);
            if(iceMethod != null) {
                int paramCount = iceMethod.getParameterTypes().length;
                Object[] paramValues = new Object[paramCount];
                paramValues[0] = request;
                paramValues[1] = current;
                Class<?>[] paramTypes = iceMethod.getParameterTypes();
                IceMethod serverAnnotation = iceMethod.getAnnotation(IceMethod.class);
                retType = serverAnnotation.retType();
                if(serverAnnotation.stop()){//方法停止服务的处理方法
                    String stopResult = null;
                    if (retType == 1) {// 返回简单json
                        stopResult = JsonUtil.toError("接口["+methodName+"]已经下线，请检查！");
                    } else{// 返回带code和message和success这是的json
                        IceResponse res = new IceResponse(null);
                        res.setSuccess(IceSuccess.OK);
                        res.setCode(IceCode.FAILURE);
                        res.setMessage("接口["+methodName+"]已经下线，请检查！");
                        res.setData(null);
                        stopResult = res.toString();
                    }
                    return stopResult;
                }

                /**
                 * 因为要让程序自动绑定实参的值到方法的形参上，调用方法的时候需要设置下形参的默认值，实际处理参数绑定是IceMethodAop中处理
                 * 此时设置的默认值不会改变实际请求的参数值，只是为了能正确的调用方法而设置
                 * 如果请求的方法有新的类型的参数也在此做下处理new一个这个类型的值放到参数列表中
                 */
                if(paramCount > 2){//第一个参数固定为JsonRequest,第二个参数是Ice.Current类型，这个两个参数就不需要再处理了，只需要处理其它绑定情况
                    for (int i = 2; i < paramTypes.length; i++) {
                        if(TypeHelper.getNumberSimpleType(paramTypes[i]) != null){
                            paramValues[i] = 0;
                        }else if(paramTypes[i].getName().equals(boolean.class.getName())){
                            paramValues[i] = true;
                        }else if(paramTypes[i].getName().equals(char.class.getName())){
                            paramValues[i] = 'A';
                        }else{
                            paramValues[i] = null;//封装类型都给null
                        }
                    }
                }

//                logger.error("请求" + methodName + ",参数个数" + paramValues.length);

                if (retType == 1) {// 返回简单json
                    result = (String)iceMethod.invoke(instance, paramValues);
                } else {// 返回带code和message和success这是的json
                    IceResponse<?> res = (IceResponse<?>)iceMethod.invoke(instance, paramValues);
                    result = res.toString();
                }
            }else{
                String trac = IceUtil.getIpAddress(current) + "," + IPUtil.getLocalIP(true);
                IceResponse res = new IceResponse(null);
                res.setSuccess(IceSuccess.FAILURE);
                res.setCode(IceCode.FAILURE);
                res.setMessage("没有此方法" + trac.replace(".", ""));
                res.setData(null);
                return res.toString();

            }
            return result;
        }catch(IllegalCheckException ce){
            return ce.getMessage();
        } catch(Exception e){
            logger.error("执行请求出错," + request,e);
        }
        if(retType == 1) {
            return JsonUtil.toError("执行请求异常");
        }else {
            IceResponse res = new IceResponse(null);
            res.setSuccess(IceSuccess.FAILURE);
            res.setCode(IceCode.FAILURE);
            res.setMessage("接口发生异常！");
            res.setData(null);
            return res.toString();
        }
    }
}
