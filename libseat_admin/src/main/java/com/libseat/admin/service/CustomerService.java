package com.libseat.admin.service;

import com.libseat.api.entity.CustomerBagEntity;
import com.libseat.api.entity.CustomerEntity;
import com.libseat.utils.page.PageResult;

import java.sql.Timestamp;
import java.util.List;

public interface CustomerService {

    PageResult<CustomerEntity> getCustomerList(String username, Integer sex,  Integer userId, Timestamp createTimeStart, Timestamp createTimeEnd, Timestamp lastLoginTimeStart, Timestamp lastLoginTimeEnd, Integer page, Integer pageSize);

    Integer insertCustomerBatch(List<CustomerEntity> CustomerEntities);

    void deleteCustomerBatch(List<CustomerEntity> CustomerEntities);

    Integer updateCustomer(CustomerEntity customerEntity);

    CustomerEntity getCustomerById(Integer customerId);

    List<CustomerEntity> getYesterdayCustomerByUserId(Integer userId, Timestamp yesStartTime, Timestamp yesEndTime);

    List<String> getAllUsername();

    Integer insertCustomer(CustomerEntity customerEntity);

    CustomerBagEntity getCustomerBagByCustomerId(Integer id);
}
