package com.libseat.api.constant;

/**
 * 订单座位状态类型
 * @author witch
 */
public enum OrderSeatStatusType {
    /** 已预定*/
    ORDERED(0, "已预定"),
    /** 已取消*/
    CANCELED(1, "已取消"),
    /** 已签到*/
    CHECKED(2, "已签到"),
    /** 已关闭*/
    CLOSED(3, "已关闭");

    private Integer id;
    private String des;
    OrderSeatStatusType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static OrderSeatStatusType getById(int id) {
        for (OrderSeatStatusType orderSeatStatusType : OrderSeatStatusType.values()) {
            if (orderSeatStatusType.id == id) {
                return orderSeatStatusType;
            }
        }
        return null;
    }

    public String getDes() {
        return this.des;
    }
}
