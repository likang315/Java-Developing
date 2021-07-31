package com.atlantis.zeus.base.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络工具类
 *
 * @author likang02@corp.netease.com
 * @date 2021-07-31 14:51
 */
public class NetworkUtil {

    /**
     * 查询当前机器的IP
     *
     * @return
     */
    public static String queryIpAddress() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }
}
