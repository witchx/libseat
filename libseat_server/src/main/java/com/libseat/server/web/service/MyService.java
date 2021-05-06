package com.libseat.server.web.service;


import com.libseat.api.entity.CustomerEntity;
import com.libseat.server.web.dto.CompanyInfo;
import com.libseat.server.web.dto.CustomerInfo;
import com.libseat.utils.page.PageResult;

import java.util.List;

public interface MyService {


    PageResult<CustomerEntity> getCustomerList(String name, Integer page, Integer pageSize);

    CustomerEntity getCustomer(CustomerEntity customerEntity);

    CustomerInfo getCustomerDetail(CustomerEntity customerEntity);

    Integer updateCustomer(CustomerEntity customerEntity);

    Integer createCustomer(CustomerEntity customerEntity);

    CustomerEntity getCustomerInfo(CustomerEntity customerEntity);

    Integer insertCustomer(CustomerEntity customerEntity);

    List<CompanyInfo> getAllCompany();
}
