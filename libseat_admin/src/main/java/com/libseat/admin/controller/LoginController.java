package com.libseat.admin.controller;

import com.alibaba.fastjson.JSON;
import com.libseat.admin.service.AdminService;
import com.libseat.admin.service.MenuService;
import com.libseat.api.constant.Constant;
import com.libseat.api.entity.AdminEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.cookie.CookieUtil;
import com.libseat.utils.jwt.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author witch
 * 2020/9/14 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/user")
@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MenuService menuService;

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> login (@RequestBody AdminEntity adminEntity, HttpServletRequest request){
        String token = "";
        //调用用户服务验证用户名和密码
        AdminEntity adminEntityFlag = adminService.login(adminEntity);
        if (adminEntityFlag == null) {
            //登录失败
            return CommonResult.failed(ResultCode.FORBIDDEN);
        } else {
            //登录成功
            //用jwt制作token
            String loginId = adminEntityFlag.getId().toString();
            try {
                token = jwtToken(adminEntityFlag, request);
            } catch (Exception e) {
                e.printStackTrace();
                return CommonResult.failed();
            }
            logger.info("用户"+loginId+"登陆成功！");
        }
        return CommonResult.success(token);
    }
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<String,Object>> info(String token, HttpServletRequest request) {
        //通过jwt校验token真假
        Map<String, Object> decode = JwtUtil.decode(token, "libseat", CookieUtil.getIp(request));
        if (decode == null || decode.isEmpty()) {
            return CommonResult.failed(ResultCode.VALIDATE_FAILED);
        } else {
            try {
                //查看token是否过期
                Object o = decode.get(Constant.LOGIN_ID);
                if (o != null) {
                    String adminToken = adminService.getAdminToken(o.toString());
                    if (StringUtils.isBlank(adminToken)) {
                        return CommonResult.failed(ResultCode.UNAUTHORIZED);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map<String,Object> user = new HashMap<>();
            user.put(Constant.LOGIN_ID, decode.get(Constant.LOGIN_ID));
            user.put(Constant.LOGIN_NAME, decode.get(Constant.LOGIN_NAME));
            user.put(Constant.LOGIN_ACCESS, decode.get(Constant.LOGIN_ACCESS));
            user.put(Constant.LOGIN_AVATAR, decode.get(Constant.LOGIN_AVATAR));
            user.put(Constant.MENU, menuService.getMenuList(false));
            return CommonResult.success(user);
        }
    }
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> logout(@RequestBody String token, HttpServletRequest request) {
        //通过jwt校验token真假
        Map<String, Object> decode = JwtUtil.decode(token, "libseat", CookieUtil.getIp(request));
        if (decode != null) {
            Object o = decode.get(Constant.LOGIN_ID);
            if (o != null) {
                try {
                    adminService.logout(o.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return CommonResult.success();
    }
    private String jwtToken(AdminEntity adminEntity, HttpServletRequest request) throws Exception {
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put( Constant.LOGIN_ID, adminEntity.getId());
        loginMap.put( Constant.LOGIN_NAME, adminEntity.getUsername());
        ArrayList<Integer> arrayList = JSON.parseObject(adminEntity.getRole(), ArrayList.class);
        String[] access = arrayList.stream().map(integer -> integer.toString()).toArray(String[]::new);
        loginMap.put( Constant.LOGIN_ACCESS, access);
        loginMap.put( Constant.LOGIN_AVATAR, adminEntity.getIcon());
        String ip = CookieUtil.getIp(request);
        if (StringUtils.isBlank(ip)) {
            //没有通过反向代理
            ip = request.getRemoteAddr();
            if (StringUtils.isBlank(ip)) {
                //可能是非法请求
                ip = Constant.IP;
            }
        }
        //需要按照设计的算法对参数进行加密后生成token
        String token = JwtUtil.encode("libseat", loginMap, ip);
        //将token存入redis
        //1.避免了单点问题
        //2.毫秒级别相应
        //3.支持设置过期时间
        adminService.addAdminToken(token, adminEntity.getId().toString());
        return token;
    }

    @RequestMapping("/password")
    @ResponseBody
    public CommonResult<ResultCode> checkPassword(@RequestBody AdminEntity adminEntity) {
        //调用用户服务验证用户名和密码
        AdminEntity adminEntityFlag = adminService.login(adminEntity);
        if (adminEntityFlag != null) {
            //登录成功
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
