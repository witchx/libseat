package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderSettingMapper;
import com.libseat.admin.service.OrderService;
import com.libseat.admin.service.OrderSettingService;
import com.libseat.admin.task.OrderCloseTask;
import com.libseat.admin.task.OrderConfirmTask;
import com.libseat.admin.task.OrderDeleteTask;
import com.libseat.admin.task.OrderEvaluateTask;
import com.libseat.api.constant.Constant;
import com.libseat.api.constant.SchedulerTaskType;
import com.libseat.api.entity.OrderSettingEntity;
import com.libseat.utils.scheduler.ScheduleRunnable;
import com.libseat.utils.scheduler.SeatScheduler;
import com.libseat.utils.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class OrderSettingServiceImpl implements OrderSettingService {

    private static Logger logger = LoggerFactory.getLogger(OrderSettingServiceImpl.class);

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Autowired
    private OrderService orderService;

    private final Object lock = new Object();

    private Map<Integer, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    private SeatScheduler seatScheduler;

    @Override
    public Integer updateOrderSetting(OrderSettingEntity orderSettingEntity) {
        return orderSettingMapper.updateByPrimaryKeySelective(orderSettingEntity);
    }

    @Override
    public Boolean updateOrderSettingBatch(List<OrderSettingEntity> orderSettingEntities) {
        //先停掉所有,包含修改数据库启动状态
        scheduledFutureMap.keySet().forEach(this::stop);
        //再轮着启动
        orderSettingEntities.forEach(orderSettingEntity -> {
            Boolean onOff = orderSettingEntity.getOnOff();
            //先修改数据库时间
            orderSettingEntity.setOnOff(null);
            orderSettingEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            updateOrderSetting(orderSettingEntity);
            if (onOff) {
                //再启动，包含修改数据库启动状态
                start(orderSettingEntity);
            }

        });
        return true;
    }

    @Override
    public void restart(OrderSettingEntity orderSettingEntity) {
        Integer id = orderSettingEntity.getId();
        stop(id);
        start(orderSettingEntity);
    }

    @Override
    public List<OrderSettingEntity> getOrderSettingList() {
        return orderSettingMapper.selectAll();
    }

    @Override
    public OrderSettingEntity getOrderSettingById(Integer id) {
        OrderSettingEntity orderSettingEntity = new OrderSettingEntity();
        orderSettingEntity.setId(id);
        return orderSettingMapper.selectByPrimaryKey(orderSettingEntity);
    }

    @Override
    public void init(SeatScheduler seatScheduler) {
        this.seatScheduler = seatScheduler;
        logger.info("<<<<<<<<<<< Timer order setting task initialization               start");
        List<OrderSettingEntity> orderSettingList = getOrderSettingList();
        if (orderSettingList == null || orderSettingList.isEmpty()) {
            return;
        }
        orderSettingList.forEach(orderSettingEntity -> {
            if (orderSettingEntity.getOnOff()) {
                start(orderSettingEntity);
            }
        });
        logger.info("<<<<<<<<<<< Timer order setting task initialization               end");
    }

    @Override
    public Integer insertOrderSetting(OrderSettingEntity orderSettingEntity) {
        return orderSettingMapper.insert(orderSettingEntity);
    }

    @Override
    public Boolean start(OrderSettingEntity orderSettingEntity) {
        logger.info("<<<<<<<<<<< Enter the start");
        Integer id = orderSettingEntity.getId();
        int time = orderSettingEntity.getTime();
        //防止重复点击
        synchronized (lock) {
            ScheduledFuture<?> scheduledFuture = null;
            SchedulerTaskType schedulerTaskType = SchedulerTaskType.getById(id);
            if (scheduledFutureMap.keySet().contains(id)) {
                logger.error(schedulerTaskType.getDes() + "任务已启动！！！！！");
                return false;
            } else {
                switch (Objects.requireNonNull(SchedulerTaskType.getById(id))) {
                    case CLOSE:
                        //每十分钟执行一次
                        scheduledFuture = seatScheduler.scheduleAtFixSecond(new OrderCloseTask(orderService, time*DateUtils.MINUTE), 0, 10*60);
                        break;
                    case CONFIRM:
                        scheduledFuture = seatScheduler.scheduleAtFixSecond(new OrderConfirmTask(orderService, time*DateUtils.MINUTE), 0, 10*60);
                        break;
                    case EVALUATE:
                        scheduledFuture = seatScheduler.schedulePerDay(new OrderEvaluateTask(orderService, time*DateUtils.DAY), 0);
                        break;
                    case DELETE:
                        scheduledFuture = seatScheduler.schedulePerDay(new OrderDeleteTask(orderService, time*DateUtils.DAY), 0);
                        break;
                    default:
                        break;
                }
            }
            if (scheduledFuture != null) {
                scheduledFutureMap.put(id, scheduledFuture);
                OrderSettingEntity orderSetting = new OrderSettingEntity();
                orderSetting.setModifyTime(new Timestamp(System.currentTimeMillis()));
                orderSetting.setOnOff(true);
                orderSetting.setId(id);
                updateOrderSetting(orderSetting);
                logger.info("task start success");
                return true;
            }
        }
        logger.info("<<<<<<<<<<< End to start");
        return false;
    }

    @Override
    public Boolean stop(Integer id) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.get(id);
        if(scheduledFuture == null) {
            logger.error(SchedulerTaskType.getById(id).getDes() + "任务已关闭！！！！！");
            return false;
        }
        //先关闭
        if (scheduledFuture.cancel(true)) {
            //再移出来
            scheduledFutureMap.remove(id);
            //更新数据库
            OrderSettingEntity orderSettingEntity = new OrderSettingEntity();
            orderSettingEntity.setId(id);
            orderSettingEntity.setModifyTime(new Timestamp(System.currentTimeMillis()));
            orderSettingEntity.setOnOff(false);
            updateOrderSetting(orderSettingEntity);
            logger.info("task stop success");
            return true;
        }
        return false;
    }
}
