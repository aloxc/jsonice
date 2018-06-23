package com.github.aloxc.jsonice.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by aloxc on 2018/6/23 in project jsonice.
 */
public class IPUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static List<InetAddress> localAddressList;

    static {
        // 初始化本地网络接口列表
        localAddressList = getLocalAddresses();
    }

    /**
     * 取得本机网络地址列表
     *
     * @return
     */
    public static List<InetAddress> getLocalAddresses() {
        if (localAddressList == null) {
            localAddressList = new ArrayList<InetAddress>();
            try {
                // 获取可用的网络接口
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces != null && interfaces.hasMoreElements()) {
                    NetworkInterface interfaceN = interfaces.nextElement();
                    // 获取网络接口上的网络地址
                    Enumeration<InetAddress> ienum = interfaceN.getInetAddresses();
                    while (ienum.hasMoreElements()) {
                        InetAddress ia = ienum.nextElement();
                        // 添加网络地址到本机网络地址列表
                        localAddressList.add(ia);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("已完成本地网络接口列表初始化[{}]", localAddressList);
        }
        return localAddressList;
    }
    /**
     * 取得本机IP（多网卡主机，默认取非内网IP，如果非内网IP也有多个，取获取到的第一个）
     *
     * @return 本机IP
     */
    public static String getLocalIP() {
        return getLocalIP(false);
    }

    /**
     * 取得本机IP地址 （多网卡主机返回其中一张网卡的IP）
     *
     * @param isInter
     *            是否返回内网IP（默认网段为19.2.）
     * @return 本机ip地址
     */
    public static String getLocalIP(boolean isInter) {
        String localIP = "";
        for (InetAddress ia : localAddressList) {
            String ip = ia.getHostAddress();
            // 忽略ipv6地址和loopback地址
            if (ia instanceof Inet6Address || ip.startsWith("127")) {
                continue;
            }
            // 记录找到的第一个ipv4地址
            if (StringUtils.isEmpty(localIP)) {
                localIP = ip;
            }
            if (isInter && ip.startsWith("19.")) {
                return ip;
            }
            if (!isInter && !ip.startsWith("19.")) {
                return ip;
            }
        }
        return localIP;
    }

}
