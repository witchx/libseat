package com.libseat.admin.service.impl;

import com.libseat.admin.mapper.OrderSettingMapper;
import com.libseat.admin.service.OrderSettingService;
import com.libseat.admin.task.AutoEvaluateTask;
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
import java.util.concurrent.ScheduledFuture;

@Service
public class OrderSettingServiceImpl implements OrderSettingService {

    private static Logger logger = LoggerFactory.getLogger(OrderSettingServiceImpl.class);

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    private final Object lock = new Object();

    private Map<Integer, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    private SeatScheduler seatScheduler;

    @Override
    public Integer updateOrderSetting(OrderSettingEntity orderSettingEntity, Boolean isRestart) {
        if (isRestart) {
            restart(orderSettingEntity);
        }
        return orderSettingMapper.updateByPrimaryKeySelective(orderSettingEntity);
    }

    private void restart(OrderSettingEntity orderSettingEntity) {
        if (stop(orderSettingEntity.getId())) {
            start(orderSettingEntity);
        }
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
                switch (SchedulerTaskType.getById(id)) {
                    case CANCEL:
                        scheduledFuture = buildScheduledFuture(new AutoEvaluateTask(),(int)(time*DateUtils.MINUTE/DateUtils.SECONDS));
                        break;
                    case EVALUATE:
                        scheduledFuture = buildScheduledFuture(new AutoEvaluateTask(),(int)(time*DateUtils.DAY/DateUtils.SECONDS));
                        break;
                    case CLOSE:
                        scheduledFuture = buildScheduledFuture(new AutoEvaluateTask(),(int)(time*DateUtils.DAY/DateUtils.SECONDS));
                        break;
                    case DELETE:
                        scheduledFuture = buildScheduledFuture(new AutoEvaluateTask(),(int)(time*DateUtils.DAY/DateUtils.SECONDS));
                        break;
                    default:
                        break;
                }
            }
            if (scheduledFuture != null) {
                scheduledFutureMap.put(id, scheduledFuture);
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
            updateOrderSetting(orderSettingEntity,false);
            logger.info("task stop success");
            return true;
        }
        return false;
    }

    private ScheduledFuture<?> buildScheduledFuture(ScheduleRunnable scheduleRunnable, int second) {
        long openTime = DateUtils.strToDate(Constant.OPEN_DAY, DateUtils.YYYY_MM_DD_HH_MM_SS).getTime();
        ScheduledFuture<?> scheduledFuture = seatScheduler.scheduleAtFixSecond(scheduleRunnable, openTime, second);
        return scheduledFuture;
    }
}
