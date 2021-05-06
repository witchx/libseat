package com.libseat.server.web.controller;

import com.libseat.api.constant.Constant;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.server.web.dto.CompanyInfo;
import com.libseat.server.web.dto.CustomerInfo;
import com.libseat.server.web.service.LoginService;
import com.libseat.server.web.service.MyService;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.cookie.CookieUtil;
import com.libseat.utils.jwt.JwtUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RequestMapping("/api")
@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private MyService myService;

    private final Object lock = new Object();

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> login (@RequestBody CustomerEntity customerEntity, HttpServletRequest request){
        String token = "";
        //调用用户服务验证用户名和密码
        CustomerInfo customer = loginService.login(customerEntity);
        if (customer == null) {
            //登录失败
            return CommonResult.failed(ResultCode.FORBIDDEN);
        } else {
            //登录成功
            //用jwt制作token
            token = loginService.jwtToken( customer, request);
            logger.info("用户" + customer.getUsername() + "登陆成功！");
            return CommonResult.success(token);
        }
    }
    @RequestMapping("/info")
    @ResponseBody
    public CommonResult<Map<String,Object>> info(String token, HttpServletRequest request) {
        //通过jwt校验token真假
        Map<String, Object> decode = JwtUtil.decode(token, Constant.LIBSEAT_SERVER_KEY, CookieUtil.getIp(request));
        if (decode == null || decode.isEmpty()) {
            return CommonResult.failed();
        } else {
            Map<String, Object> user = new HashMap<>();
            user.put(Constant.LOGIN_USERNAME, decode.get(Constant.LOGIN_USERNAME));
            user.put(Constant.LOGIN_AVATAR, decode.get(Constant.LOGIN_AVATAR));
            user.put(Constant.LOGIN_ID, decode.get(Constant.LOGIN_ID));
            user.put(Constant.LOGIN_NICKNAME, decode.get(Constant.LOGIN_NICKNAME));
            user.put(Constant.LOGIN_TEL, decode.get(Constant.LOGIN_TEL));
            user.put(Constant.LOGIN_COMPANY_NAME, decode.get(Constant.LOGIN_COMPANY_NAME));
            user.put(Constant.LOGIN_COMPANY_ID, decode.get(Constant.LOGIN_COMPANY_ID));
            return CommonResult.success(user);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> register(@RequestBody CustomerEntity customerEntity) {
        synchronized (lock) {
            CustomerEntity customer = new CustomerEntity();
            customer.setUsername(customerEntity.getUsername());
            if (myService.getCustomer(customer) != null) {
                return CommonResult.failed("该用户已存在！");
            }
            customerEntity.setPassword(DigestUtils.md5Hex(customerEntity.getPassword()));
            customerEntity.setNickname(customerEntity.getNickname().trim());
            customerEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
            customerEntity.setIcon("https://img-blog.csdnimg.cn/20201014180756754.png?x-oss-process=image/resize,m_fixed,h_64,w_64");
        }
        if (myService.insertCustomer(customerEntity) != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @GetMapping(value = "/allCompany")
    @ResponseBody
    public CommonResult<List<CompanyInfo>> allCompany() {
        return CommonResult.success(myService.getAllCompany());
    }
}
