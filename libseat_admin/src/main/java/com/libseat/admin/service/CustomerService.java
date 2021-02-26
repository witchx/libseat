package com.libseat.admin.service;

import com.libseat.api.entity.CustomerEntity;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface CustomerService {

    PageResult<CustomerEntity> getCustomerList(String username, Integer sex, String status, Integer userId, Integer page, Integer pageSize);

    Integer insertCustomerBatch(List<CustomerEntity> CustomerEntities);

    void deleteCustomerBatch(List<CustomerEntity> CustomerEntities);

    Integer updateCustomer(CustomerEntity customerEntity);

    CustomerEntity getCustomerById(Integer customerId);
}
