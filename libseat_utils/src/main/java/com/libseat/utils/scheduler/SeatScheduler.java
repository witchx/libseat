package com.libseat.utils.scheduler;

import com.alibaba.fastjson.JSON;
import com.libseat.utils.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SeatScheduler {
    static final Logger logger = LoggerFactory.getLogger(SeatScheduler.class);
    private int poolSize;
    private String preInx;

    /**
     * 定时任务线程池
     */
    private final AtomicInteger id = new AtomicInteger(1);
    private ScheduledExecutorService sPool;

    public void start(String preInx, int poolSize) {
        logger.info(" #### start, poolSize=" + poolSize);
        this.poolSize = poolSize;
        sPool = Executors.newScheduledThreadPool(poolSize,
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName(preInx + "-Thread-" + id.getAndAdd(1));
                    return thread;
                });
    }

    /**
     * 重启计时器
     */
    public void restart() {
        stop(false);
        start(preInx, this.poolSize);
    }

    /**
     * 停止定时器
     *
     * @param now 是否立即停止
     */
    public void stop(boolean now) {
        logger.info(" #### stop, now=" + now);
        if (sPool == null) {
            return;
        }
        //停止所有
        if (now) {
            List<Runnable> waitList = sPool.shutdownNow();
            System.out.println(JSON.toJSONString(waitList));
        } else {
            sPool.shutdown();
        }
    }

    public int getPoolSize() {
        return poolSize;
    }

    public ScheduledExecutorService getsPool() {
        return sPool;
    }

    /**
     * 延迟执行
     *
     * @param runnable
     * @param delay
     * @param timeUnit
     */
    public ScheduledFuture<?> schedule(Runnable runnable, long delay, TimeUnit timeUnit) {
        return sPool.schedule(runnable, delay, timeUnit);
    }

    /**
     * 从xx时间开始，每隔y时间执行一次。不会有并发问题，线性的
     * 如果执行时长大于y秒，下次执行会在本次执行完之后再执行
     * 如果执行时长小于y秒，下次执行会在执行开始y秒后再执行
     *
     * @param callable
     * @param startTime    xx，从这个时间戳开始计时，如果没有，从当前开始算，取整秒。
     * @param periodSecond y，每隔多少秒执行一次
     */
    public ScheduledFuture<?> scheduleAtFixSecond(ScheduleRunnable callable, long startTime, int periodSecond) {
        long now = System.currentTimeMillis();
        if (startTime == 0) {
            startTime = now;
        }
        int times = (int) ((now - startTime) / (periodSecond * DateUtils.SECONDS));
        int excuteTimes = 0;
        if (times > 0) {
            excuteTimes = callable.loop(times);
        }
        int delay = (int) ((startTime - now) % (periodSecond * DateUtils.SECONDS));
        //现在比开始大，延迟就是下一个时间差
        if (delay <= 0) {
            delay += periodSecond * DateUtils.SECONDS;
        }
        logger.info(" #### per period, delay=" + delay + "," + (delay / DateUtils.DAY) + ":" + ((delay % DateUtils.DAY / DateUtils.HOUR)) + ":" + ((delay % DateUtils.HOUR / DateUtils.MINUTE)) + ", all=" + (times) + ",excute=" + excuteTimes);
        return sPool.scheduleAtFixedRate(callable, delay, periodSecond * DateUtils.SECONDS, TimeUnit.MILLISECONDS);
    }

    /**
     * 每天X点固定执行的
     *
     * @param runnable
     * @param hour     0-23，整点
     */
    public ScheduledFuture<?> schedulePerDay(Runnable runnable, int hour) {
        return schedulePerDay(runnable, hour, 0);
    }

    /**
     * 每天X点Y分固定执行的
     *
     * @param runnable
     * @param hour     0-23，整点
     * @param minute   0-59，整分
     */
    public ScheduledFuture<?> schedulePerDay(Runnable runnable, int hour, int minute) {
        return schedulePerDay(runnable, hour, minute, 0);
    }

    /**
     * 每天X点Y分Z秒固定执行的
     *
     * @param runnable
     * @param hour     0-23，整点
     * @param minute   0-59，整分
     * @param second   0-59，整秒
     */
    public ScheduledFuture<?> schedulePerDay(Runnable runnable, int hour, int minute, int second) {
        return schedulePerDay(runnable, hour * DateUtils.HOUR + minute * DateUtils.MINUTE + second * DateUtils.SECONDS);
    }

    /**
     * 每天某个时间点固定执行的
     *
     * @param runnable
     * @param dayTime  0-24*3600*1000之间，到毫秒
     */
    public ScheduledFuture<?> schedulePerDay(Runnable runnable, long dayTime) {
        //今天已经过去的时间
        long goneTime = DateUtils.getDayGoneTime(System.currentTimeMillis());
        //现在到目标点的时间差
        long delay = dayTime - goneTime;
        //说明已经过了那个点，等明天了。
        if (delay <= 0) {
            delay += DateUtils.DAY;
        }
        logger.info(" #### perday, delay=" + delay + "," + (delay / DateUtils.DAY) + ":" + ((delay % DateUtils.DAY / DateUtils.HOUR)) + ":" + ((delay % DateUtils.HOUR / DateUtils.MINUTE)));
        return sPool.scheduleAtFixedRate(runnable, delay, DateUtils.DAY, TimeUnit.MILLISECONDS);
    }

    /**
     * 每周X点Y分Z秒固定执行一次。
     *
     * @param runnable
     * @param weekDay  Calendar的week定义,Calendar.FRIDAY之类的
     * @param hour     0-23，整点
     * @param minute   0-59，整分
     * @param second   0-59, 整秒
     */
    public ScheduledFuture<?> schedulePerWeek(Runnable runnable, int weekDay, int hour, int minute, int second) {
        long targetTime = DateUtils.getWeekStartTime(weekDay) + hour * DateUtils.HOUR + minute * DateUtils.MINUTE + second * DateUtils.SECONDS;
        long now = System.currentTimeMillis();
        //时间差
        long delay = targetTime - now;
        if (delay < 0) {
            //已经过了时间，就下一周的
            delay += DateUtils.DAY * 7;
        }
        logger.info(" schedulePerWeek #### perweek, delay=" + delay + "," + (delay / DateUtils.DAY) + ":" + ((delay % DateUtils.DAY / DateUtils.HOUR)) + ":" + ((delay % DateUtils.HOUR / DateUtils.MINUTE)));
        return sPool.scheduleAtFixedRate(runnable, delay, 7 * DateUtils.DAY, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> schedulePerWeek(Runnable runnable, int weekDay, int hour, int minute) {
        return schedulePerWeek(runnable,weekDay,hour,0,0);
    }

    public ScheduledFuture<?> schedulePerWeek(Runnable runnable, int weekDay, int hour) {
        return schedulePerWeek(runnable,weekDay,0,0);
    }


}
