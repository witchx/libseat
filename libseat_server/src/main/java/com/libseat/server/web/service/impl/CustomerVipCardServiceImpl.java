package com.libseat.server.web.service.impl;

import com.libseat.api.entity.CustomerVipCardEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.server.web.mapper.CustomerVipCardMapper;
import com.libseat.server.web.service.CustomerVipCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerVipCardServiceImpl implements CustomerVipCardService {

    @Autowired
    private CustomerVipCardMapper customerVipCardMapper;

    @Override
    public void insertCustomerVipCard(CustomerVipCardEntity customerVipCardEntity) {
        customerVipCardMapper.insertUseGeneratedKeys(customerVipCardEntity);
    }

    @Override
    public List<VipCardEntity> getAllVipCardByCustomerId(Integer id) {
        return customerVipCardMapper.getAllVipCardByCustomerId(id);
    }
}
