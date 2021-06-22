package com.libseat.admin.interceptors;

import com.alibaba.fastjson.JSON;
import com.libseat.admin.annotations.LoginRequired;
import com.libseat.api.constant.Constant;
import com.libseat.utils.cookie.CookieUtil;
import com.libseat.utils.http.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Witch
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断被拦截的请求的访问的方法的注解是否是需要拦截的
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);
        //是否拦截,为空表示可以不必拦截,直接返回true
        if (loginRequired == null) {
            return true;
        }
        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);
        String newToken = request.getParameter("token");
        if (StringUtils.isNotBlank(oldToken)) {
            token = oldToken;
        }
        if (StringUtils.isNotBlank(newToken)) {
            token = newToken;
        }
        //获取该请求是否必须登录成功,true为必须登录成功，false可以不必登录成功
        boolean loginSuccess = loginRequired.loginSuccess();
        //调用认证中心进行验证
        String success = "fail";

        Map<String, String> successMap = new HashMap<>();

        if (StringUtils.isNotBlank(token)) {
            String ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isBlank(ip)) {
                //没有通过反向代理
                ip = request.getRemoteAddr();
                if (StringUtils.isBlank(ip)) {
                    //可能是非法请求
                    ip = Constant.IP;
                }
            }
            String successJson = HttpclientUtil.doGet(Constant.VERIFY_IP + "?token=" + token + "&currentIp=" + ip);
            successMap = JSON.parseObject(successJson, Map.class);
            success = successMap.get("status");
        }
        if (loginSuccess) {
            //必须登录成功才能使用
            if (!success.equals("success")) {
                //重定向passport登录
                StringBuffer requestURL = request.getRequestURL();
                response.sendRedirect(Constant.LOGIN_IP + "?ReturnUrl=" + requestURL);
                return false;
            }
            request.setAttribute(Constant.LOGIN_ID, successMap.get(Constant.LOGIN_ID));
            request.setAttribute(Constant.LOGIN_USERNAME, successMap.get(Constant.LOGIN_USERNAME));
            if (StringUtils.isNotBlank(token)) {
                //验证通过，覆盖cookie中的token
                CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
            }
        } else {
            //可以不用登录成功也可以使用
            if (success.equals("success")) {
                //需要将用户携带的用户信息写入
                request.setAttribute(Constant.LOGIN_ID, successMap.get(Constant.LOGIN_ID));
                request.setAttribute(Constant.LOGIN_USERNAME, successMap.get(Constant.LOGIN_USERNAME));
                if (StringUtils.isNotBlank(token)) {
                    //验证通过，覆盖cookie中的token
                    CookieUtil.setCookie(request, response, "oldToken", token, 60 * 60 * 2, true);
                }
            }
        }
        return true;
    }
}
