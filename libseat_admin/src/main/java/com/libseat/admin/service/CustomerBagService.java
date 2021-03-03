package com.libseat.admin.service;


import com.libseat.api.entity.CustomerBagEntity;

public interface CustomerBagService {


    Integer insertCustomerBag(CustomerBagEntity customerBagEntity);

    CustomerBagEntity getCustomerBagByCustomerId(Integer id);
}
