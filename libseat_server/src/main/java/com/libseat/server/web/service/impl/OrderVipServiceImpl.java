package com.libseat.server.web.service.impl;


import com.libseat.api.constant.VipCardType;
import com.libseat.api.entity.OrderVipEntity;
import com.libseat.api.entity.VipCardEntity;
import com.libseat.server.web.mapper.OrderVipMapper;
import com.libseat.server.web.service.OrderVipService;
import com.libseat.server.web.service.VipCardService;
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

    @Override
    public Integer updateOrderById(OrderVipEntity orderVipEntity) {
        return orderVipMapper.updateByPrimaryKeySelective(orderVipEntity);
    }

    @Override
    public void insertOrderVip(OrderVipEntity orderVipEntity) {
        orderVipMapper.insertUseGeneratedKeys(orderVipEntity);
    }
}
