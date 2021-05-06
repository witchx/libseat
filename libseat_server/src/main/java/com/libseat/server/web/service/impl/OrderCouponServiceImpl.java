package com.libseat.server.web.service.impl;

import com.libseat.api.entity.OrderCouponEntity;
import com.libseat.server.web.mapper.OrderCouponMapper;
import com.libseat.server.web.service.OrderCouponService;
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
