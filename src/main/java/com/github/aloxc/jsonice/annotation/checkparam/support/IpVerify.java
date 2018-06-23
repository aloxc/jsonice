package com.github.aloxc.jsonice.annotation.checkparam.support;

import com.github.aloxc.jsonice.annotation.checkparam.Ip;
import com.github.aloxc.jsonice.io.IceCode;
import com.github.aloxc.jsonice.io.JsonRequest;
import com.github.aloxc.jsonice.io.VerifyResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ice 参数ip验证器
 * Created by aloxc on 2017/3/1.
 */
public class IpVerify implements Verify{
    private final static Log logger = LogFactory.getLog(IpVerify.class);

    @Override
    public VerifyResult verify(Method method, JsonRequest request) {
        Ip param = method.getAnnotation(Ip.class);
        VerifyResult result = null;
        boolean fromHeader = param.fromHeader();
        String key = param.key();
        boolean nullable = param.nullable();
        String userIp = null;
        if(fromHeader){
            userIp = request.getHeader("userIp",null);
        }else{
            userIp = request.getParam(key);
        }
        boolean needVerify = false;
        if(StringUtils.isEmpty(userIp)) {
            if (nullable) return null;
            else {
                result = new VerifyResult();
                result.setCode(IceCode.USER_IP_ERROR);
            }
        }else{
            //做验证
            String p = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
            Pattern pattern = Pattern.compile(p);
            Matcher matcher = pattern.matcher(userIp);
            if(!matcher.matches()){
                result = new VerifyResult();
                result.setCode(IceCode.USER_IP_ERROR);
            }
        }
        return result;
    }

}
