package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderCouponMapper;
import com.libseat.admin.service.OrderCouponService;
import com.libseat.api.entity.OrderCouponEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCouponServiceImpl implements OrderCouponService {

    @Autowired
    private OrderCouponMapper orderCouponMapper;

    @Override
    public Integer updateOrderById(OrderCouponEntity orderCouponEntity) {
        return orderCouponMapper.updateByPrimaryKeySelective(orderCouponEntity);
    }
}
