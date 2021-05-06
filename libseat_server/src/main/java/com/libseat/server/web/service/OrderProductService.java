package com.libseat.server.web.service;

import com.libseat.api.entity.OrderProductEntity;

public interface OrderProductService {

    OrderProductEntity getOrderByOrderId(Integer id);
}
