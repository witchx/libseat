package com.libseat.admin.task;

import com.libseat.admin.service.BaseService;
import com.libseat.utils.scheduler.ScheduleRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LibseatTenMinuteTask extends ScheduleRunnable {
    private static final Logger logger = LoggerFactory.getLogger(LibseatTenMinuteTask.class);

    private final BaseService baseService;

    public LibseatTenMinuteTask(BaseService baseService) {
        this.baseService = baseService;
    }
    @Override
    public int loop(int times) {
        try {
            logger.info("ScheduledTask:  {} is Running", Thread.currentThread().getName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
