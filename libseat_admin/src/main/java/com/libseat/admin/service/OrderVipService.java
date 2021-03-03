package com.libseat.admin.service;

import com.libseat.api.entity.OrderVipEntity;

public interface OrderVipService {
    OrderVipEntity getOrderByOrderId(Integer orderId);

    Integer updateOrderById(OrderVipEntity orderVipEntity);
}
