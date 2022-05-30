package com.cellulam.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public abstract class InetAddressUtis {
    private static final Logger logger = LoggerFactory.getLogger(InetAddressUtis.class);

    public static final String DEFAULT_LOCAL_IP = "127.0.0.1";
    public static final String DEFAULT_LOCAL_HOSTNAME = "local";

    /**
     * 获取本地IP，获取不到IP时使用默认值<code>DEFAULT_LOCAL_IP</code>
     *
     * @return
     */
    public static String getLocalIPSilence() {
        try {
            return getLocalIP();
        } catch (Exception e) {
            logger.error("Failed to get local IP, use default: {}", DEFAULT_LOCAL_IP, e);
            return DEFAULT_LOCAL_IP;
        }
    }

    /**
     * 获取本地IP
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    /**
     * 获取hostname，获取失败返回<code>DEFAULT_LOCAL_HOSTNAME</code>
     *
     * @return
     */
    public static String getHostnameSilence() {
        try {
            return getHostname();
        } catch (UnknownHostException e) {
            logger.error("Failed to get hostname, use default: {}", DEFAULT_LOCAL_IP, e);
            return DEFAULT_LOCAL_HOSTNAME;
        }
    }

    /**
     * 获取hostname
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getHostname() throws UnknownHostException {
        return Inet4Address.getLocalHost().getHostName();
    }
}
