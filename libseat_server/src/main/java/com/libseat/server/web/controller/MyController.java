package com.libseat.server.web.controller;


import com.libseat.api.constant.Constant;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.api.entity.RankInfo;
import com.libseat.server.web.dto.CustomerInfo;
import com.libseat.server.web.service.LoginService;
import com.libseat.server.web.service.MyService;
import com.libseat.server.web.service.RankService;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.file.FileUploadUtil;
import com.libseat.utils.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author witch
 * 2020/9/14 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/my")
@Controller
public class MyController {

    @Autowired
    private MyService myService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RankService rankService;

    private Logger logger = LoggerFactory.getLogger(MyController.class);

    @RequestMapping(value = "/info" , method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CustomerEntity> getCustomerInfo (Integer id){
        if (id == null || id <= 0) {
            return CommonResult.failed();
        } else {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(id);
            customerEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
            CustomerEntity customer = myService.getCustomerInfo(customerEntity);
            if (customer != null) {
                return CommonResult.success(customer);
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/rank" , method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Map<Integer,Object>> getCustomerRank (Integer id){
        if (id == null || id <= 0) {
            return CommonResult.failed();
        }
        Map<Integer,Object> map = new HashMap<>();
        int flag = DateUtils.getWeeksBetweenToday(Constant.OPEN_DAY);
        RankInfo rank = rankService.getCustomerRankById(id);
        List<RankInfo> monthRank = rankService.getRanks(Constant.RANK_MONTH + flag);
        List<RankInfo> weekRank = rankService.getRanks(Constant.RANK_WEEK+flag);
        List<RankInfo> yearRank = rankService.getRanks(Constant.RANK_YEAR+flag);
        map.put(0,rank);
        map.put(1,weekRank);
        map.put(2,monthRank);
        map.put(3,yearRank);
        return CommonResult.success(map);
    }

    @RequestMapping(value = "/detail" , method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CustomerInfo> getCustomerDetail (Integer id){
        if (id == null || id <= 0) {
            return CommonResult.failed();
        } else {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(id);
            customerEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
            CustomerInfo customer = myService.getCustomerDetail(customerEntity);
            if (customer != null) {
                return CommonResult.success(customer);
            }
        }
        return CommonResult.failed();
    }

    @PostMapping("/upload/image/{id}")
    @ResponseBody
    public CommonResult<ResultCode> fileUpload(@PathVariable Integer id,@RequestBody MultipartFile file) {
        if (file != null) {
            String imgUrl = FileUploadUtil.uploadImage(file);
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(id);
            customerEntity.setIcon(imgUrl);
            if (myService.updateCustomer(customerEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/update_{id}" , method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> updateCustomer (@PathVariable Integer id, @RequestBody CustomerEntity customerEntity){
        if (customerEntity != null) {
            customerEntity.setId(id);
            if (myService.updateCustomer(customerEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping("/password")
    @ResponseBody
    public CommonResult<ResultCode> checkPassword(@RequestBody CustomerEntity customerEntity) {
        //调用用户服务验证用户名和密码
        CustomerInfo customer = loginService.login(customerEntity);
        if (customer != null) {
            //登录成功
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
