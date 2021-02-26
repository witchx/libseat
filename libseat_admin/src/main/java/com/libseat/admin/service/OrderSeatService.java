package com.libseat.admin.service;

import com.libseat.api.entity.OrderSeatEntity;

public interface OrderSeatService {
    OrderSeatEntity getOrderByOrderId(Integer orderId);
}
