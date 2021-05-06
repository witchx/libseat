package com.libseat.server.web.service;


import com.libseat.api.entity.OrderEntity;
import com.libseat.server.web.dto.OrderDto;
import com.libseat.server.web.dto.OrderRecordInfo;
import com.libseat.server.web.dto.SeatOrderInfo;

import java.util.List;

public interface OrderService {

    OrderEntity getOrder(OrderEntity orderEntity);

    Integer updateOrder(OrderEntity orderEntity);

    Integer insertOrder(OrderEntity orderEntity);

    Integer createOrder(OrderDto orderDto);;

    List<SeatOrderInfo> getAllSeatOrderList(Integer customerId);

    List<OrderRecordInfo> getAllOrderList(Integer customerId);
}
