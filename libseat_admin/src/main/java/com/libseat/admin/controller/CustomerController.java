package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.CustomerService;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.entity.CustomerBagEntity;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import com.libseat.utils.utils.DateUtils;
import com.libseat.utils.utils.UsernameGenerateUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author witch
 * 2020/11/10 : witch
 * @version 1.0
 */
@CrossOrigin
@RequestMapping("/api/customer")
@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    private final Object lock = new Object();

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<CustomerEntity>> getCustomerList (@RequestParam String username,
                                                                     @RequestParam Integer sex,
                                                                     @RequestParam Integer userId,
                                                                     @RequestParam(required = false) String createStartTime,
                                                                     @RequestParam(required = false) String createEndTime,
                                                                     @RequestParam(required = false) String lastLoginTimeStart,
                                                                     @RequestParam(required = false) String lastLoginTimeEnd,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<CustomerEntity> customerList = customerService.getCustomerList(username, sex, userId,
                DateUtils.strToTimestamp(createStartTime,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(createEndTime,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(lastLoginTimeStart,DateUtils.YYYY_MM_DD_HH_MM_SS),
                DateUtils.strToTimestamp(lastLoginTimeEnd,DateUtils.YYYY_MM_DD_HH_MM_SS),page, pageSize);
        if (customerList == null || customerList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(customerList);
        }
    }

    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CustomerBagEntity> getCustomerDetail (@PathVariable Integer id){
        CustomerBagEntity customerBagEntity = customerService.getCustomerBagByCustomerId(id);
        if (customerBagEntity != null) {
            return CommonResult.success(customerBagEntity);
        }
        return CommonResult.failed(ResultCode.EMPTY);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createCustomer(@RequestBody CustomerEntity customerEntity) {
        synchronized (lock) {
            List<String> allUsername = customerService.getAllUsername();
            String username = UsernameGenerateUtils.getUsername(4, 6, allUsername);
            String password = DigestUtils.md5Hex(customerEntity.getPassword());
            customerEntity.setUsername(username);
            customerEntity.setPassword(DigestUtils.md5Hex(password));
            customerEntity.setNickname(customerEntity.getNickname().trim());
            customerEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
        }
        if (customerService.insertCustomer(customerEntity) != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateCustomer(@PathVariable Integer id, @RequestBody CustomerEntity customerEntity) {
        if (customerEntity != null) {
            customerEntity.setId(id);
            customerEntity.setNickname(customerEntity.getNickname().trim());
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (customerService.updateCustomer(customerEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete/batch", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteCustomer(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return CommonResult.failed();
        }
        List<CustomerEntity> CustomerEntities = ids.stream().map(id -> {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(id);
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
            return customerEntity;
        }).collect(Collectors.toList());
        customerService.deleteCustomerBatch(CustomerEntities);
        return CommonResult.success();
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteCustomer(@PathVariable Integer id, @RequestBody CustomerEntity customerEntity){
        if (customerEntity != null) {
            customerEntity.setId(id);
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
            if (customerService.updateCustomer(customerEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }
}
