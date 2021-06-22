package com.libseat.admin.task;

import com.libseat.admin.service.OrderService;
import com.libseat.api.constant.DeleteFlagType;
import com.libseat.api.constant.OrderStatusType;
import com.libseat.api.entity.OrderEntity;
import com.libseat.utils.scheduler.ScheduleRunnable;
import com.libseat.utils.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定时任务：完成订单后，在所有订单 过了指定时间，自动删除
 */
public class OrderDeleteTask extends ScheduleRunnable {
    private static Logger logger = LoggerFactory.getLogger(OrderDeleteTask.class);

    private final OrderService orderService;

    private final long flagTime;

    public OrderDeleteTask(OrderService orderService, long flagTime) {
        this.orderService = orderService;
        this.flagTime = flagTime;
    }

    @Override
    public int loop(int i) {
        try {
            logger.info("ScheduledTask:  {} is Running", Thread.currentThread().getName());
            //取出状态为已完成的所有订单
            List<OrderEntity> orderList = orderService.getOrderList(null, OrderStatusType.COMPLETED.getId(), true);
            orderList.forEach(orderEntity -> {
                //超过时间,更改状态
                if (DateUtils.getMillisecondsBetweenNow(orderEntity.getEvaluateTime().getTime()) >= flagTime) {
                    OrderEntity order = new OrderEntity();
                    order.setId(orderEntity.getId());
                    order.setDeleteFlag(DeleteFlagType.CANCEL.getId());
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
