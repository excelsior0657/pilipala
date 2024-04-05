package com.ss.pilipala.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;

@Slf4j
public class RequestUtil {
    private static String localhost = null;

    /**
     * 常用的转发 IP，一般都在这几个请求头参数配置中，遍历这些请求头参数，找到用户真实 IP
     * 如果用户 IP 都不在这几个常用参数中，那就没办法了
     */
    private static final String[] HEADER_CONFIG = {
            "x-forwarded-for",
            "proxy-client-ip",
            "wl-proxy-client-ip",
            "http_client_ip",
            "http_x_forwarded_for"
    };

    private static final String UNKNOWN = "unknown";
    private static final String IPV6_LOCALHOST = "0:0:0:0:0:0:0:1";

    /**
     * 获取本机 IP
     *
     * @return 本机 IP 地址
     */
    public static String getLocalhost() {
        // 如果有 localhost 缓存，直接返回，避免每次都去遍历获取本机地址
        if (StringUtils.isNotBlank(localhost)) {
            return localhost;
        }
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress targetHost = null;
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // loopback 回环地址 127.0.0.1 没有意义
                    // sitelocal 站点地址 -- 目标地址
                    // link
                    if (inetAddress.isLoopbackAddress()) {
                        continue;
                    }
                    // 如果实在没有 siteLocalAddress，那么 targetHost 就始终为null，所以必须先给 targetHost 赋一个值
                    if (inetAddress.isSiteLocalAddress() || Objects.isNull(targetHost)) {
                        targetHost = inetAddress;
                    }
                }
            }
            // 如果只有 loopback，targetHost依然会为空，所以返回一个 InetAddress.getLocalHost().getHostAddress()
            localhost = Objects.isNull(targetHost) ?
                    InetAddress.getLocalHost().getHostAddress() : targetHost.getHostAddress();
            return localhost;
        } catch (Exception e) {
            log.error("获取本机 IP 失败", e);
            throw new RuntimeException("获取本机 IP 失败");
        }
    }

    /**
     * 获取用户请求 IP 地址
     *
     * @return 用户 IP 地址
     */
    public static String getIpAddress(HttpServletRequest request){
        // request.getRemoteAddr() 可以直接获取用户 IP，但是如果用户用了反向代理，获取的就是反向代理的 IP
        // 这并不符合预期，所以此方法行不通
        String ip = null;
        for (String config : HEADER_CONFIG) {
            ip = request.getHeader(config);
            if (StringUtils.isNotBlank(ip) && !StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
                break;
            }
        }
        // 如果实在获取不到用户的实际 IP，那就只能返回代理的 IP 地址
        if (StringUtils.isBlank(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果获取的地址为 IPV6，则返回本机地址
        if (StringUtils.equalsIgnoreCase(IPV6_LOCALHOST, ip)) {
            ip = getLocalhost();
        }
        return ip;
    }
}
