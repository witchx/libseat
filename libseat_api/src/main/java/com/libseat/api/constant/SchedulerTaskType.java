package com.libseat.api.constant;

public enum SchedulerTaskType {
    /** 自动取消*/
    CANCEL(1, "自动取消"),
    /** 自动好评*/
    EVALUATE(2, "自动好评"),
    /** 自动关闭*/
    CLOSE(3, "自动关闭"),
    /** 自动删除*/
    DELETE(4, "自动删除");

    private Integer id;
    private String des;
    SchedulerTaskType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static SchedulerTaskType getById(int id) {
        for (SchedulerTaskType schedulerTaskType : SchedulerTaskType.values()) {
            if (schedulerTaskType.id == id) {
                return schedulerTaskType;
            }
        }
        return null;
    }
    public String getDes() {
        return this.des;
    }
}
