package com.libseat.admin.service;


import com.libseat.api.entity.CustomerBagEntity;

import java.util.List;

public interface CustomerBagService {


    Integer insertCustomerBag(CustomerBagEntity customerBagEntity);

    CustomerBagEntity getCustomerBagByCustomerId(Integer id);

    List<CustomerBagEntity> getAllCustomerBag();

    void statistics();
}
