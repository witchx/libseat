package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.OrderMapper;
import com.libseat.admin.service.OrderService;
import com.libseat.api.constant.OrderProgressType;
import com.libseat.api.constant.OrderStatusType;
import com.libseat.api.constant.OrderType;
import com.libseat.api.entity.OrderEntity;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;


    @Override
    public PageResult<OrderEntity> getOrderList(Integer id, String no, String company, String customer, Timestamp createStartTime, Timestamp createEndTime, Integer type, Integer progress, Integer status, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OrderEntity> orderList = orderMapper.getOrderList(id, no, company, customer, createStartTime, createEndTime, type, progress, status);
        orderList.forEach(orderEntity -> {
            orderEntity.setOrderProgress(OrderProgressType.getById(orderEntity.getProgress()).getName());
            orderEntity.setOrderType(OrderType.getById(orderEntity.getType()).getDes());
            orderEntity.setOrderStatus(OrderStatusType.getById(orderEntity.getStatus()).getDes());
        });
        PageInfo pageInfo = new PageInfo(orderList);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public OrderEntity getOrderById(Integer id) {
        List<OrderEntity> orderList = orderMapper.getOrderList(id, null, null, null, null, null, null, null, null);
        if (orderList != null && orderList.size() == 1) {
            OrderEntity orderEntity = orderList.get(0);
            orderEntity.setOrderProgress(OrderProgressType.getById(orderEntity.getProgress()).getName());
            orderEntity.setOrderType(OrderType.getById(orderEntity.getType()).getDes());
            orderEntity.setOrderStatus(OrderStatusType.getById(orderEntity.getStatus()).getDes());
            return orderEntity;
        }
        return null;
    }

    @Override
    public Integer updateOrder(OrderEntity orderEntity) {
        return orderMapper.updateByPrimaryKeySelective(orderEntity);
    }

    @Override
    public Integer insertOrder(OrderEntity orderEntity) {
        return orderMapper.insert(orderEntity);
    }
}
