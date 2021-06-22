package com.libseat.admin.service.impl;


import com.libseat.admin.mapper.CustomerBagMapper;
import com.libseat.admin.service.CustomerBagService;
import com.libseat.api.entity.CustomerBagEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

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

    @Override
    public List<CustomerBagEntity> getAllCustomerBag() {
        return customerBagMapper.selectAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void statistics() {
        //先得到所有顾客的背包
        List<CustomerBagEntity> allCustomerBag = getAllCustomerBag();
        List<CustomerBagEntity> customerBagEntities = new LinkedList<>();
        allCustomerBag.forEach(customerBagEntity -> {
            if (customerBagEntity.getTotalDays() != 0) {
                CustomerBagEntity customerBag = new CustomerBagEntity();
                customerBag.setId(customerBagEntity.getId());
                customerBag.setTotalDays(customerBagEntity.getTotalDays()-1);
                customerBagEntities.add(customerBag);
            }
        });
        customerBagMapper.updateCustomerBagBatch(customerBagEntities);
    }
}
