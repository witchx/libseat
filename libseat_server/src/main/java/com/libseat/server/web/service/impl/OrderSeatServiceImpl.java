package com.libseat.server.web.service.impl;

import com.libseat.api.entity.OrderSeatEntity;
import com.libseat.server.web.mapper.OrderSeatMapper;
import com.libseat.server.web.service.OrderSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSeatServiceImpl implements OrderSeatService {

    @Autowired
    private OrderSeatMapper orderSeatMapper;

    @Override
    public OrderSeatEntity getOrderByOrderId(Integer orderId) {
        OrderSeatEntity orderSeatEntity = new OrderSeatEntity();
        orderSeatEntity.setOrderId(orderId);
        return orderSeatMapper.selectOne(orderSeatEntity);
    }

    @Override
    public Integer updateOrderById(OrderSeatEntity orderSeatEntity) {
        return orderSeatMapper.updateByPrimaryKeySelective(orderSeatEntity);
    }

    @Override
    public void insertOrderSeat(OrderSeatEntity orderSeatEntity) {
        orderSeatMapper.insertUseGeneratedKeys(orderSeatEntity);
    }
}
