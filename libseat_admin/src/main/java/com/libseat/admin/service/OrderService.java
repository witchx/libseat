package com.libseat.admin.service;


import com.libseat.api.entity.Order;
import com.libseat.api.entity.OrderEntity;
import com.libseat.api.entity.OrderProductEntity;
import com.libseat.api.entity.OrderRecordEntity;
import com.libseat.utils.page.PageResult;

import java.sql.Timestamp;
import java.util.List;

public interface OrderService {

    PageResult<OrderEntity> getOrderList(Integer id, String no, String company, String customer, Timestamp createStartTime, Timestamp createEndTime, Integer type, Integer progress, Integer status, Integer page, Integer pageSize);

    OrderEntity getOrderById(Integer id);

    Integer updateOrder(OrderEntity orderEntity);

    Integer insertOrder(OrderEntity orderEntity);

    List<OrderEntity> getOrderList(Integer type, Integer status, Boolean isBack);

    Order getOrderSeatByOrderId(Integer id);

    Order getOrderVipByOrderId(Integer id);

    List<OrderProductEntity> getOrderProductByOrderId(Integer id);

    List<OrderRecordEntity> getOrderRecordRecord(OrderRecordEntity orderRecordEntity);

    Integer insertOrderRecordRecord(OrderRecordEntity orderRecordEntity);

    void updateOrderByTask(OrderEntity orderEntity);


    List<OrderEntity> getYesterdayOrderByUserId(Integer userId, Timestamp yesStartTime, Timestamp yesEndTime);
}

