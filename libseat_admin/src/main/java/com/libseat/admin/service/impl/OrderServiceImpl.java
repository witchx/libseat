package com.libseat.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.libseat.admin.mapper.OrderMapper;
import com.libseat.admin.service.*;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.constant.OrderProgressType;
import com.libseat.api.constant.OrderStatusType;
import com.libseat.api.constant.OrderType;
import com.libseat.api.entity.*;
import com.libseat.utils.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderVipService orderVipService;
    @Autowired
    private OrderSeatService orderSeatService;
    @Autowired
    private OrderRecordService orderRecordService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private OrderCouponService orderCouponService;


    @Override
    public PageResult<OrderEntity> getOrderList(Integer id, String no, String company, String customer, Timestamp createStartTime, Timestamp createEndTime, Integer type, Integer progress, Integer status, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OrderEntity> orderList = orderMapper.getOrderList(id, no, company, customer, createStartTime, createEndTime, type, progress, status);
        orderList.forEach(orderEntity -> {
            orderEntity.setOrderProgress(OrderProgressType.getById(orderEntity.getProgress()).getName());
            orderEntity.setOrderType(OrderType.getById(orderEntity.getType()).getDes());
            orderEntity.setOrderStatus(OrderStatusType.getById(orderEntity.getStatus()).getDes());
        });
        PageInfo pageInfo = new PageInfo(orderList);
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public OrderEntity getOrderById(Integer id) {
        List<OrderEntity> orderList = orderMapper.getOrderList(id, null, null, null, null, null, null, null, null);
        if (orderList != null && orderList.size() == 1) {
            OrderEntity orderEntity = orderList.get(0);
            orderEntity.setOrderProgress(OrderProgressType.getById(orderEntity.getProgress()).getName());
            orderEntity.setOrderType(OrderType.getById(orderEntity.getType()).getDes());
            orderEntity.setOrderStatus(OrderStatusType.getById(orderEntity.getStatus()).getDes());
            return orderEntity;
        }
        return null;
    }

    @Override
    public Integer updateOrder(OrderEntity orderEntity) {
        return orderMapper.updateByPrimaryKeySelective(orderEntity);
    }

    @Override
    public Integer insertOrder(OrderEntity orderEntity) {
        return orderMapper.insert(orderEntity);
    }

    @Override
    public List<OrderEntity> getOrderList(Integer type, Integer status, Boolean isBack) {
        return orderMapper.getOrderListSimple(type,status,isBack);
    }

    @Override
    public Order getOrderSeatByOrderId(Integer id) {
        return orderSeatService.getOrderByOrderId(id);
    }

    @Override
    public Order getOrderVipByOrderId(Integer id) {
        return orderVipService.getOrderByOrderId(id);
    }

    @Override
    public List<OrderProductEntity> getOrderProductByOrderId(Integer id) {
        return orderProductService.getOrderByOrderId(id);
    }

    @Override
    public List<OrderRecordEntity> getOrderRecordRecord(OrderRecordEntity orderRecordEntity) {
        return orderRecordService.getOrderRecord(orderRecordEntity);
    }

    @Override
    public Integer insertOrderRecordRecord(OrderRecordEntity orderRecordEntity) {
        return orderRecordService.insertOrderRecord(orderRecordEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderByTask(OrderEntity orderEntity) {
        //先更新order数据库
        if (updateOrder(orderEntity) > 0 && orderEntity.getDeleteFlag().equals(DeleteFlagType.CANCEL.getId())) {
            //根据订单类型更新其他表
            switch (Objects.requireNonNull(OrderType.getById(orderEntity.getType()))) {
                case SEAT:
                    OrderSeatEntity orderSeatEntity = new OrderSeatEntity();
                    orderSeatEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
                    orderSeatService.updateOrderById(orderSeatEntity);
                    break;
                case VIP_CARD:
                    OrderVipEntity orderVipEntity = new OrderVipEntity();
                    orderVipEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
                    orderVipService.updateOrderById(orderVipEntity);
                    break;
                case COUPON:
                    OrderCouponEntity orderCouponEntity = new OrderCouponEntity();
                    orderCouponEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
                    orderCouponService.updateOrderById(orderCouponEntity);
                    break;
                case PRODUCT:
                    OrderProductEntity orderProductEntity = new OrderProductEntity();
                    orderProductEntity.setDeleteFlag(DeleteFlagType.CANCEL.getId());
                    orderProductService.updateOrderById(orderProductEntity);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public List<OrderEntity> getYesterdayOrderByUserId(Integer userId, Timestamp yesStartTime, Timestamp yesEndTime) {
        return orderMapper.getYesterdayOrderByUserId(userId,yesEndTime,yesEndTime);
    }
}
