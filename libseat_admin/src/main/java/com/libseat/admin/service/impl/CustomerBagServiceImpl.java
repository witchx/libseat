package com.libseat.admin.service.impl;


import com.libseat.admin.mapper.CustomerBagMapper;
import com.libseat.admin.service.CustomerBagService;
import com.libseat.api.entity.CustomerBagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerBagServiceImpl implements CustomerBagService {
    @Autowired
    private CustomerBagMapper customerBagMapper;

    @Override
    public Integer insertCustomerBag(CustomerBagEntity customerBagEntity) {
        return customerBagMapper.insertSelective(customerBagEntity);
    }

    @Override
    public CustomerBagEntity getCustomerBagByCustomerId(Integer id) {
        CustomerBagEntity customerBagEntity = new CustomerBagEntity();
        customerBagEntity.setCustomerId(id);
        return customerBagMapper.selectOne(customerBagEntity);
    }
}
