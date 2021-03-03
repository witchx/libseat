package com.libseat.admin.service;

import com.libseat.api.entity.OrderSettingEntity;
import com.libseat.utils.scheduler.SeatScheduler;

import java.util.List;

public interface OrderSettingService {
    Integer updateOrderSetting(OrderSettingEntity orderSettingEntity);

    Boolean updateOrderSettingBatch(List<OrderSettingEntity> orderSettingEntities);

    List<OrderSettingEntity> getOrderSettingList();

    OrderSettingEntity getOrderSettingById(Integer id);

    void init(SeatScheduler seatScheduler);

    Boolean start(OrderSettingEntity orderSettingEntity);

    Boolean stop(Integer id);

    void restart(OrderSettingEntity orderSettingEntity);

    Integer insertOrderSetting(OrderSettingEntity orderSettingEntity);
}
