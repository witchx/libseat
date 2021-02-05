package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderSeatMapper;
import com.libseat.admin.service.OrderProductService;
import com.libseat.admin.service.OrderSeatService;
import com.libseat.api.entity.OrderSeatEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSeatServiceImpl implements OrderSeatService {

    @Autowired
    private OrderSeatMapper orderSeatMapper;

    @Override
    public OrderSeatEntity getOrderById(Integer id) {
        OrderSeatEntity orderSeatEntity = new OrderSeatEntity();
        orderSeatEntity.setId(id);
        return orderSeatMapper.selectByPrimaryKey(orderSeatEntity);
    }
}
