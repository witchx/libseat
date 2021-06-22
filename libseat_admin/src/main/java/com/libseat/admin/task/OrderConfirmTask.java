package com.libseat.admin.task;

import com.libseat.admin.service.OrderService;
import com.libseat.admin.service.RankService;
import com.libseat.api.constant.OrderProgressType;
import com.libseat.api.constant.OrderStatusType;
import com.libseat.api.constant.OrderType;
import com.libseat.api.entity.OrderEntity;
import com.libseat.utils.scheduler.ScheduleRunnable;
import com.libseat.utils.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

/**
 * 定时任务：支付订单后，在座位订单 开始之前15分钟，如果没有取消，就自动确认，不允许退款了
 */
public class OrderConfirmTask extends ScheduleRunnable {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfirmTask.class);

    private final OrderService orderService;

    private final RankService rankService;

    private final long flagTime;

    public OrderConfirmTask(OrderService orderService, RankService rankService, long flagTime) {
        this.orderService = orderService;
        this.rankService = rankService;
        this.flagTime = flagTime;
    }

    @Override
    public int loop(int i) {
        try {
            logger.info("ScheduledTask:  {} is Running", Thread.currentThread().getName());
            //取出状态为待确认的所有座位订单
            List<OrderEntity> orderList = orderService.getOrderList(OrderType.SEAT.getId(), OrderStatusType.CONFIRM.getId(),false);
            orderList.forEach(orderEntity -> {
                //超过时间,更改状态
                long betweenNow = DateUtils.getMillisecondsBetweenNow(orderEntity.getStartTime().getTime());
                if ( betweenNow > 0 || Math.abs(betweenNow) <= flagTime) {
                    OrderEntity order = new OrderEntity();
                    order.setId(orderEntity.getId());
                    order.setStatus(OrderStatusType.EVALUATE.getId());
                    order.setProgress(OrderProgressType.CONFIRM.getId());
                    order.setConfirmTime(new Timestamp(System.currentTimeMillis()));
                    order.setType(orderEntity.getType());
                    orderService.updateOrderByTask(order);
                    //更新rank 加学习时长
                    rankService.addStudyTime(orderEntity.getId());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
