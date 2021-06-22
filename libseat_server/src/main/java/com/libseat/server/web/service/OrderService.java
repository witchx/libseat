package com.libseat.server.web.service;


import com.libseat.api.entity.OrderEntity;
import com.libseat.server.web.dto.OrderDto;
import com.libseat.server.web.dto.OrderRecordInfo;
import com.libseat.server.web.dto.SeatOrderInfo;
import com.libseat.utils.code.CommonResult;

import java.util.List;

public interface OrderService {

    OrderEntity getOrder(OrderEntity orderEntity);

    Integer updateOrder(OrderEntity orderEntity);

    Integer insertOrder(OrderEntity orderEntity);

    CommonResult<Integer> createOrder(OrderDto orderDto);;

    List<SeatOrderInfo> getAllSeatOrderList(Integer customerId);

    List<OrderRecordInfo> getAllOrderList(Integer customerId);

    void cancelOrder(OrderEntity orderEntity);

    OrderEntity getOrderSelective(OrderEntity orderEntity);
}
