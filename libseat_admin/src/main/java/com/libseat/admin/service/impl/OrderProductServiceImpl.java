package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderProductMapper;
import com.libseat.admin.service.OrderProductService;
import com.libseat.api.entity.OrderProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProductServiceImpl implements OrderProductService {

    @Autowired
    private OrderProductMapper orderProductMapper;

    @Override
    public OrderProductEntity getOrderById(Integer id) {
        OrderProductEntity orderProductEntity = new OrderProductEntity();
        orderProductEntity.setId(id);
        return orderProductMapper.selectByPrimaryKey(orderProductEntity);
    }
}
