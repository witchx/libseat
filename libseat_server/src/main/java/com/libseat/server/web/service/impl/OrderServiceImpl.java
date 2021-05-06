package com.libseat.server.web.service.impl;

import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.constant.OrderProgressType;
import com.libseat.api.constant.OrderStatusType;
import com.libseat.api.constant.OrderType;
import com.libseat.api.entity.OrderEntity;
import com.libseat.api.entity.OrderSeatEntity;
import com.libseat.api.entity.OrderVipEntity;
import com.libseat.server.web.dto.OrderDto;
import com.libseat.server.web.dto.OrderRecordInfo;
import com.libseat.server.web.dto.SeatOrderInfo;
import com.libseat.server.web.mapper.OrderMapper;
import com.libseat.server.web.service.OrderSeatService;
import com.libseat.server.web.service.OrderService;
import com.libseat.server.web.service.OrderVipService;
import com.libseat.utils.utils.CodeGenerateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderSeatService orderSeatService;

    @Autowired
    private OrderVipService orderVipService;

    @Autowired
    private CodeGenerateUtils codeGenerateUtils;

    private final Object lock = new Object();



    @Override
    public OrderEntity getOrder(OrderEntity orderEntity) {
        return orderMapper.selectByPrimaryKey(orderEntity);
    }

    @Override
    public Integer updateOrder(OrderEntity orderEntity) {
        return orderMapper.updateByPrimaryKeySelective(orderEntity);
    }

    @Override
    public Integer insertOrder(OrderEntity orderEntity) {
        return orderMapper.insert(orderEntity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer createOrder(OrderDto orderDto) {
        Integer orderId = null;
        OrderType orderType = OrderType.getById(orderDto.getType());
        if (orderType != null) {
            //加锁
            synchronized (lock) {
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setUserId(orderDto.getUserId());
                orderEntity.setCustomerId(orderDto.getCustomerId());
                orderEntity.setStadiumId(orderDto.getStadiumId());
                orderEntity.setPrice(new BigDecimal(orderDto.getPrice()));
                orderEntity.setType(orderDto.getType());
                orderEntity.setDiscount(new BigDecimal(orderDto.getDiscount()));
                //自动生成订单编号
                String code = codeGenerateUtils.generateOrderCode();
                orderEntity.setNo(code);
                //已提交
                orderEntity.setProgress(OrderProgressType.SUBMIT.getId());
                //待支付
                orderEntity.setStatus(OrderStatusType.UNPAID.getId());
                orderEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
                orderEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                //插入基本订单表
                insertOrder(orderEntity);
                orderId = orderEntity.getId();
                switch (orderType) {
                    case SEAT:
                        OrderSeatEntity orderSeatEntity = new OrderSeatEntity();
                        orderSeatEntity.setSeatId(orderDto.getSeatId());
                        orderSeatEntity.setStartTime(new Timestamp(orderDto.getStartTime()));
                        orderSeatEntity.setEndTime(new Timestamp(orderDto.getEndTime()));
                        orderSeatEntity.setOrderId(orderId);
                        orderSeatEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
                        //插入座位订单表
                        orderSeatService.insertOrderSeat(orderSeatEntity);
                        break;
                    case VIP_CARD:
                        OrderVipEntity orderVipEntity = new OrderVipEntity();
                        orderVipEntity.setVipCardId(orderDto.getVipCardId());
                        orderVipEntity.setOrderId(orderId);
                        orderVipEntity.setDeleteFlag(DeleteFlagType.EXIST.getId());
                        //插入会员卡订单表
                        orderVipService.insertOrderVip(orderVipEntity);
                        break;
                    case COUPON:
                        //暂时不做，移动端没有发放优惠劵的页面 TODO
                        break;
                    case PRODUCT:
                        //暂时不做，移动端没有购买商品的页面 TODO
                        break;
                    default:
                        break;
                }
            }
        }
        return orderId;
    }

    @Override
    public List<SeatOrderInfo> getAllSeatOrderList(Integer customerId) {
        return orderMapper.getAllSeatOrderList(customerId);
    }

    @Override
    public List<OrderRecordInfo> getAllOrderList(Integer customerId) {
        return orderMapper.getAllOrderList(customerId);
    }

}
