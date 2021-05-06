package com.libseat.server.web.service;


import com.libseat.api.entity.CustomerBagEntity;

public interface CustomerBagService {


    Integer insertCustomerBag(CustomerBagEntity customerBagEntity);

    CustomerBagEntity getCustomerBagByCustomerId(Integer id);

    void updateCustomerBag(CustomerBagEntity customerBag);
}
