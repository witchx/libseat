package com.libseat.server.web.service.impl;

import com.libseat.api.entity.OrderProductEntity;
import com.libseat.server.web.mapper.OrderProductMapper;
import com.libseat.server.web.service.OrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Override
    public OrderProductEntity getOrderByOrderId(Integer id) {
        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setOrderId(id);
        return orderProductMapper.selectOne(orderProductEntity);
    }
}
