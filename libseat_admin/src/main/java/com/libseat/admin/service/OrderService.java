package com.libseat.admin.service;


import com.libseat.api.entity.OrderEntity;
import com.libseat.utils.page.PageResult;

import java.sql.Timestamp;

public interface OrderService {

    PageResult<OrderEntity> getOrderList(Integer id, String no, String company, String customer, Timestamp createStartTime, Timestamp createEndTime, Integer type, Integer progress, Integer status, Integer page, Integer pageSize);

    OrderEntity getOrderById(Integer id);

    Integer updateOrder(OrderEntity orderEntity);

    Integer insertOrder(OrderEntity orderEntity);
}

