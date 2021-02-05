package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderVipMapper;
import com.libseat.admin.service.OrderVipService;
import com.libseat.api.entity.OrderVipEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderVipServiceImpl implements OrderVipService {

    @Autowired
    private OrderVipMapper orderVipMapper;
    @Override
    public OrderVipEntity getOrderById(Integer id) {
        OrderVipEntity orderVipEntity = new OrderVipEntity();
        return orderVipMapper.selectByPrimaryKey(orderVipEntity);
    }
}
