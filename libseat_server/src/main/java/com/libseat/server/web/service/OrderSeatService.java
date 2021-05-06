package com.libseat.server.web.service;

import com.libseat.api.entity.OrderSeatEntity;

public interface OrderSeatService {
    OrderSeatEntity getOrderByOrderId(Integer orderId);

    Integer updateOrderById(OrderSeatEntity orderSeatEntity);

    void insertOrderSeat(OrderSeatEntity orderSeatEntity);
}
