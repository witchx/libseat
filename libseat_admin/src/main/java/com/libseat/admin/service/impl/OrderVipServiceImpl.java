package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderVipMapper;
import com.libseat.admin.service.OrderVipService;
import com.libseat.admin.service.VipCardService;
import com.libseat.api.constant.VipCardType;
import com.libseat.api.entity.OrderVipEntity;
import com.libseat.api.entity.VipCardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderVipServiceImpl implements OrderVipService {

    @Autowired
    private OrderVipMapper orderVipMapper;

    @Autowired
    private VipCardService vipCardService;

    @Override
    public OrderVipEntity getOrderByOrderId(Integer orderId) {
        OrderVipEntity orderVipEntity = new OrderVipEntity();
        orderVipEntity.setOrderId(orderId);
        OrderVipEntity orderVip = orderVipMapper.selectOne(orderVipEntity);
        VipCardEntity vipCardById = vipCardService.getVipCardById(orderVip.getVipCardId());
        vipCardById.setVipCardType(VipCardType.getById(vipCardById.getType()).getDes());
        orderVip.setVipCard(vipCardById);
        return orderVip;
    }
}
