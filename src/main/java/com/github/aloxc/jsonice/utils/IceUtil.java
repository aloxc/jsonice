package com.github.aloxc.jsonice.utils;

import com.zeroc.Ice.Current;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * 读取ice客户端的ip
 * Created by aloxc on 2018/6/23 in project jsonice.
 */
public class IceUtil {
    private final static Log logger = LogFactory.getLog(IceUtil.class);

    /**
     * 读取ice客户端的ip地址
     * @param current
     * @return
     */
    public static String getIpAddress(Current current){
        return current.ctx.get("clientIp");
    }
}
