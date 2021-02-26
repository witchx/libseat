package com.libseat.admin.service;

import com.libseat.api.entity.OrderProductEntity;

import java.util.List;

public interface OrderProductService {
    List<OrderProductEntity> getOrderByOrderId(Integer orderId);
}
