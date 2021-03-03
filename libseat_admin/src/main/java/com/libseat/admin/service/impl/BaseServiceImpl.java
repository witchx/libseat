package com.libseat.admin.service.impl;


import com.libseat.admin.service.BaseService;
import com.libseat.admin.service.OrderSettingService;
import com.libseat.admin.service.RankService;
import com.libseat.admin.service.UserDetailService;
import com.libseat.admin.task.LibseatDailyTask;
import com.libseat.admin.task.LibseatTenMinuteTask;
import com.libseat.admin.task.LibseatWeekTask;
import com.libseat.utils.scheduler.SeatScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class BaseServiceImpl implements BaseService {

    @Autowired
    private OrderSettingService orderSettingService;

    @Autowired
    private RankService rankService;

    @Autowired
    private UserDetailService userDetailService;

    SeatScheduler seatScheduler = new SeatScheduler();

    @Override
    public void startFinish() {
        //定时任务管理
        seatScheduler.start("admin - scheduler", 2);

        startScheduler();
    }

    @Override
    public void startWeek() {
        rankService.initRank();
    }

    @Override
    public void startDay() {
        userDetailService.statistics();
    }

    private void startScheduler() {
        //初始化订单的任务
        orderSettingService.init(seatScheduler);
        //每周一0点执行的任务
        // seatScheduler.schedulePerWeek(new LibseatWeekTask(this), Calendar.MONDAY, 0);
        //每10分钟执行的任务
        // seatScheduler.scheduleAtFixSecond(new LibseatTenMinuteTask(this), 0, 10*60);
        //每天零点执行
        // seatScheduler.schedulePerDay(new LibseatDailyTask(this), 0);

    }





}
