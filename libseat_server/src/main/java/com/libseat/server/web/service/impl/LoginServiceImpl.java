package com.libseat.server.web.service.impl;


import com.alibaba.fastjson.JSON;
import com.libseat.api.constant.Constant;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.server.web.dto.CustomerInfo;
import com.libseat.server.web.service.LoginService;
import com.libseat.server.web.service.MyService;
import com.libseat.utils.cookie.CookieUtil;
import com.libseat.utils.jwt.JwtUtil;
import com.libseat.utils.redis.RedisUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MyService myService;

    @Override
    public CustomerInfo login(CustomerEntity customerEntity) {

        CustomerInfo customer = null;
        Jedis jedis = null;
        customerEntity.setPassword(DigestUtils.md5Hex(customerEntity.getPassword()));
        try {
            jedis = redisUtil.getJedis();
            if (jedis != null) {
                String key = "student:" + customerEntity.getUsername() + customerEntity.getPassword() + ":info";
                String studentStr = jedis.get(key);
                if (StringUtils.isNotBlank(studentStr)) {
                    //用户和密码正确
                    customer = JSON.parseObject(studentStr, CustomerInfo.class);
                }
            }
            if (customer == null) {
                //缓存连接异常，去数据库中查找
                //缓存中没有，去数据库中查找
                customer = myService.getCustomerDetail(customerEntity);
                if (customer != null) {
                    //放入缓存中
                    String key = "student:" + customer.getUsername() + customerEntity.getPassword() + ":info";
                    jedis.setex(key, 60 * 60 * 24, JSON.toJSONString(customer));
                }
            }
            if (customer != null) {
                CustomerEntity customerEntity1 = new CustomerEntity();
                customerEntity1.setId(customer.getId());
                customerEntity1.setLastLoginTime(new Timestamp(System.currentTimeMillis()));
                myService.updateCustomer(customerEntity1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return customer;
    }

    @Override
    public void addCustomerToken(String token, String memberId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("customer:" + memberId + ":token", 60 * 60 * 2, token);
        jedis.close();
    }

    @Override
    public String jwtToken(CustomerInfo customer, HttpServletRequest request) {
        Map<String, Object> loginMap = new HashMap<>();
        loginMap.put( Constant.LOGIN_ID, customer.getId());
        loginMap.put( Constant.LOGIN_NICKNAME, customer.getNickname());
        loginMap.put( Constant.LOGIN_USERNAME, customer.getUsername());
        loginMap.put( Constant.LOGIN_AVATAR, customer.getIcon());
        loginMap.put( Constant.LOGIN_TEL, customer.getTel());
        loginMap.put( Constant.LOGIN_COMPANY_NAME, customer.getCompanyName());
        loginMap.put( Constant.LOGIN_COMPANY_ID, customer.getUserId());
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
        String token = JwtUtil.encode(Constant.LIBSEAT_SERVER_KEY, loginMap, ip);
        //将token存入redis
        addCustomerToken(token, customer.getUsername());
        return token;
    }
}
