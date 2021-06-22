package com.libseat.admin.task;

import com.libseat.admin.service.OrderService;
import com.libseat.api.constant.OrderEvaluateType;
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
 * 定时任务：确认订单后，在座位订单 过了指定时间，自动五星好评
 */
public class OrderEvaluateTask extends ScheduleRunnable {
    private static Logger logger = LoggerFactory.getLogger(OrderEvaluateTask.class);

    private final OrderService orderService;

    private final long flagTime;

    public OrderEvaluateTask(OrderService orderService, long flagTime) {
        this.orderService = orderService;
        this.flagTime = flagTime;
    }

    @Override
    public int loop(int i) {
        try {
            logger.info("ScheduledTask:  {} is Running", Thread.currentThread().getName());
            //取出状态为待评价的所有座位订单
            List<OrderEntity> orderList = orderService.getOrderList(OrderType.SEAT.getId(), OrderStatusType.EVALUATE.getId(),false);
            orderList.forEach(orderEntity -> {
                //超过时间,更改状态
                if (DateUtils.getMillisecondsBetweenNow(orderEntity.getConfirmTime().getTime()) >= flagTime) {
                    OrderEntity order = new OrderEntity();
                    order.setId(orderEntity.getId());
                    order.setStatus(OrderStatusType.COMPLETED.getId());
                    order.setProgress(OrderProgressType.ACCOMPLISH.getId());
                    order.setEvaluateTime(new Timestamp(System.currentTimeMillis()));
                    order.setEvaluate(OrderEvaluateType.FIVE_STAR.getId());
                    order.setType(orderEntity.getType());
                    orderService.updateOrderByTask(order);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
