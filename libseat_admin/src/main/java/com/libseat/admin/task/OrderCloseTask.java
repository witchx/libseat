package com.libseat.admin.task;

import com.libseat.admin.service.*;
import com.libseat.api.constant.OrderStatusType;
import com.libseat.api.entity.OrderEntity;
import com.libseat.api.entity.OrderSettingEntity;
import com.libseat.utils.scheduler.ScheduleRunnable;
import com.libseat.utils.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定时任务：当提交提单后，超过指定时间，自动关闭订单
 */
public class OrderCloseTask extends ScheduleRunnable {
    private static final Logger logger = LoggerFactory.getLogger(OrderCloseTask.class);

    private final OrderService orderService;

    private final long flagTime;

    public OrderCloseTask(OrderService orderService, long flagTime) {
        this.orderService = orderService;
        this.flagTime = flagTime;
    }

    @Override
    public int loop(int i) {
        try {
            logger.info("ScheduledTask:  {} is Running", Thread.currentThread().getName());
            List<OrderEntity> orderList = orderService.getOrderList(null,OrderStatusType.UNPAID.getId(),false);
            orderList.forEach(orderEntity -> {
                //超过时间,更改状态
                if (DateUtils.getMillisecondsBetweenNow(orderEntity.getCreateTime().getTime())>=flagTime) {
                    OrderEntity order = new OrderEntity();
                    order.setId(orderEntity.getId());
                    order.setStatus(OrderStatusType.CLOSED.getId());
                    order.setProgress(orderEntity.getProgress());
                    orderService.updateOrderByTask(orderEntity);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
