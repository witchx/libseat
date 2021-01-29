package com.libseat.admin.controller;

import com.libseat.admin.annotations.TrimRequired;
import com.libseat.admin.service.CustomerService;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.utils.code.CommonResult;
import com.libseat.utils.code.ResultCode;
import com.libseat.utils.page.PageResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
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

    @TrimRequired
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<PageResult<CustomerEntity>> getCustomerList (@RequestParam String username,
                                                                    @RequestParam Integer sex,
                                                                    @RequestParam Integer userId,
                                                                    @RequestParam String status,
                                                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        PageResult<CustomerEntity> customerList = customerService.getCustomerList(username, sex, status, userId, page, pageSize);
        if (customerList == null || customerList.getTotal() == 0) {
            return CommonResult.failed(ResultCode.EMPTY);
        } else {
            return CommonResult.success(customerList);
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<ResultCode> createCustomer(@RequestBody List<CustomerEntity> customers) {
        for (CustomerEntity customerEntity : customers) {
            String password = DigestUtils.md5Hex(customerEntity.getPassword());
            customerEntity.setPassword(DigestUtils.md5Hex(password));
            customerEntity.setUsername(customerEntity.getUsername().trim());
            customerEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
        }
        Integer row = customerService.insertCustomerBatch(customers);
        if (row != 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult<ResultCode> updateCustomer(@PathVariable Integer id, @RequestBody CustomerEntity customerEntity) {
        if (customerEntity != null) {
            customerEntity.setId(id);
            customerEntity.setUsername(customerEntity.getUsername().trim());
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            if (customerService.updateCustomer(customerEntity) != 0) {
                return CommonResult.success();
            }
        }
        return CommonResult.failed();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult<ResultCode> deleteCustomer(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return CommonResult.failed();
        }
        List<CustomerEntity> CustomerEntities = ids.stream().map(id -> {
            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setId(id);
            customerEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            customerEntity.setDeleteFlag("1");
            return customerEntity;
        }).collect(Collectors.toList());
        customerService.deleteCustomerBatch(CustomerEntities);
        return CommonResult.success();
    }
}
