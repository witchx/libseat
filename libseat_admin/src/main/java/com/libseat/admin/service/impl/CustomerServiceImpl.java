package com.libseat.admin.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.CustomerMapper;
import com.libseat.admin.service.CustomerService;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public PageResult<CustomerEntity> getCustomerList(String username, Integer sex, String status, Integer userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CustomerEntity> CustomerEntities = customerMapper.getCustomerList(username, sex, status, userId);
        PageInfo pageInfo = new PageInfo(CustomerEntities);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public Integer insertCustomerBatch(List<CustomerEntity> customerEntities) {
        return customerMapper.insertCustomerBatch(customerEntities);
    }

    @Override
    public void deleteCustomerBatch(List<CustomerEntity> customerEntities) {
        customerMapper.deleteCustomerBatch(customerEntities);
    }

    @Override
    public Integer updateCustomer(CustomerEntity customerEntity) {
        return customerMapper.updateByPrimaryKeySelective(customerEntity);
    }

    @Override
    public CustomerEntity getCustomerById(Integer customerId) {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(customerId);
        return customerMapper.selectByPrimaryKey(customerEntity);
    }

}
