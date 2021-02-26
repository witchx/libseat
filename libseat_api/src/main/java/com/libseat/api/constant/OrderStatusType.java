package com.libseat.api.constant;

public enum OrderStatusType {
    /** 待支付*/
    UNPAID(1, "待支付"),
    /** 待消费*/
    CONSUMPTION(2, "待消费"),
    /** 待评价*/
    EVALUATE(3, "待评价"),
    /** 已取消"*/
    CANCELED(4, "已取消"),
    /** 已关闭"*/
    CLOSED(5, "已关闭"),
    /** 已完成*/
    COMPLETED(6, "已完成");

    private Integer id;
    private String des;
    OrderStatusType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static OrderStatusType getById(int id) {
        for (OrderStatusType orderStatusType : OrderStatusType.values()) {
            if (orderStatusType.id == id) {
                return orderStatusType;
            }
        }
        return null;
    }
    public String getDes() {
        return this.des;
    }
}
