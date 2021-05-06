package com.libseat.server.web.controller;


import com.libseat.server.web.dto.PayDto;
import com.libseat.server.web.dto.PayInfo;
import com.libseat.server.web.service.PayService;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/api/pay")
@Controller
public class PayController {

    @Autowired
    private PayService payService;

    @PostMapping("/create")
    @ResponseBody
    public CommonResult<ResultCode> createPay (@RequestBody PayDto payDto){
        try {
            payService.createPay(payDto);
        } catch (Exception e) {
            return CommonResult.failed();
        }
        return CommonResult.success();
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
