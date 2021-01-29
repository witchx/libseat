package com.libseat.utils.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Witch
 */
public class CookieUtil {
    /***
     * 获得cookie中的值，默认为主ip：www.gmall.com
     * @param request
     * @param cookieName
     * @param isDecoder
     * @return
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        //如果涉及中文
                        retValue = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookies[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /***
     * 设置cookie的值
     * @param request
     * @param response
     * @param cookieName
     * @param cookieValue
     * @param cookieMaxage
     * @param isEncode
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage >= 0) {
                cookie.setMaxAge(cookieMaxage);
            }
            if (null != request) {
                // 设置域名的cookie
                cookie.setDomain(getDomainName(request));
            }
            // 在域名的根路径下保存
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 获得cookie的主域名，本系统为gmall.com，保存时使用
     * @param request
     * @return
     */
    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;
        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com or xxx.cn
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        System.out.println("domainName = " + domainName);
        return domainName;
    }

    /***
     * 将cookie中的内容按照key删除
     * @param request
     * @param response
     * @param cookieName
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        setCookie(request, response, cookieName, null, 0, false);
    }

    public static String getIp(HttpServletRequest request) {
        //代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        //如果没有代理，则获取真实ip
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}

