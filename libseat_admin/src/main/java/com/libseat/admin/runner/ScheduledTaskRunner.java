package com.libseat.admin.runner;

import com.libseat.admin.service.OrderSettingService;
import com.libseat.utils.scheduler.SeatScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTaskRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTaskRunner.class);
    @Autowired
    private OrderSettingService orderSettingService;

    SeatScheduler seatScheduler = new SeatScheduler();
 
    /**
     * 程序启动完毕后,需要自启的任务
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        logger.info(" >>>>>> Project startup completed, open = >;Begin tasks that require self-revelation!");
        //定时任务管理
        seatScheduler.start("admin - scheduler", 2);
        //初始化开服任务
        orderSettingService.init(seatScheduler);

        logger.info(" >>>>>> Project startup completed, open = >;End of the task that requires self-revelation!");
    }
}