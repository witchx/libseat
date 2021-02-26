package com.libseat.utils.scheduler;

/**
 * 定时器执行线程。
 */
public abstract class ScheduleRunnable implements Runnable {
    /**
     * 定时器会执行一次。
     */
    @Override
    public void run() {
        loop(1);
    }

    /**
     * 启动的时候，会循环执行这个方法，
     * @return 需要的次数
     */
    public abstract int loop(int times);
}