package com.libseat.admin.task;

import com.libseat.utils.scheduler.ScheduleRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoEvaluateTask extends ScheduleRunnable {
    private static Logger logger = LoggerFactory.getLogger(AutoEvaluateTask.class);


    public AutoEvaluateTask() {

    }

    @Override
    public int loop(int i) {
        try {
            logger.info("ScheduledTask:  {} is Running", Thread.currentThread().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
