package com.libseat.server.web.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.libseat.server.framework.config.AlipayConfig;
import com.libseat.server.web.dto.PayDto;
import com.libseat.server.web.dto.PayInfo;
import com.libseat.server.web.service.PayService;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@CrossOrigin
@RequestMapping("/api/pay")
@Controller
public class PayController {

    @Autowired
    private PayService payService;

    private Logger logger = LoggerFactory.getLogger(PayController.class);

    @PostMapping("/create")
    @ResponseBody
    public CommonResult<ResultCode> createPay (@RequestBody PayDto payDto){
        if (payService.createPay(payDto)) {
            return CommonResult.success();
        } else {
            return CommonResult.failed("余额不足");
        }
    }

    @PostMapping("/alipay")
    @ResponseBody
    public Object createPay (@RequestBody PayDto payDto, HttpServletResponse response) throws Exception {
        return payService.alipay(payDto);
    }

    @PostMapping(value = "/fallback")
    public void fallback (HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            map.put(name, valueStr);
        }
        //验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(map, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, AlipayConfig.SIGN_TYPE);
        } catch (com.alipay.api.AlipayApiException e) {
            logger.error("[支付验证] 异常={}", JSON.toJSON(e));
            return;
        }
        if (signVerified) {
            if(map.get("trade_status").equals("TRADE_SUCCESS")) {
                payService.fallback(map.get("out_trade_no"));
            }
        }
    }

    @GetMapping("/info")
    @ResponseBody
    public CommonResult<PayInfo> getPay (@RequestParam Integer id){
        PayInfo payInfo = payService.getPayByOrderId(id);
        if (payInfo != null) {
            return CommonResult.success(payInfo);
        }
        return CommonResult.failed();
    }
}
