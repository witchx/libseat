package com.libseat.admin.service;


import com.libseat.api.entity.OrderEntity;
import com.libseat.utils.page.PageResult;

import java.sql.Timestamp;

public interface OrderService {

    PageResult<OrderEntity> getOrderList(Integer id, String no, String company, String customer, Timestamp createStartTime, Timestamp createEndTime, Integer status, Integer type, Integer page, Integer pageSize);

    OrderEntity getOrder(OrderEntity orderEntity);

    Integer updateOrder(OrderEntity orderEntity);

    Integer insertOrder(OrderEntity orderEntity);
}

