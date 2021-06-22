package com.libseat.server.web.controller;


import com.libseat.server.web.dto.BasicCompanyInfo;
import com.libseat.server.web.service.HomeService;
import com.libseat.server.web.service.MyService;
import com.libseat.utils.code.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author witch
 * 2020/9/14 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/home")
@Controller
public class HomeController {

    @Autowired
    private MyService myService;

    @Autowired
    private HomeService homeService;

    private Logger logger = LoggerFactory.getLogger(HomeController.class);


    @RequestMapping("/company")
    @ResponseBody
    public CommonResult<List<BasicCompanyInfo>> getBasicCompanyInfo(Integer id) {
        if (id == null || id <= 0) {
            return CommonResult.failed();
        }
        List<BasicCompanyInfo> companyInfos = homeService.getBasicCompanyInfo(id);
        return CommonResult.success(companyInfos);
    }

}
