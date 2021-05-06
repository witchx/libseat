package com.libseat.server.web.dto.constant;

public enum OrderSeatShowType {
    /** 所有*/
    ALL(0, "所有"),
    /** 已消费*/
    HAS_BEEN_SPENDING(1, "已消费"),
    /** 未消费*/
    NOT_SPENDING(2,"未消费"),
    /** 已取消*/
    CANCEL(3,"已取消");

    private Integer id;
    private String des;
    OrderSeatShowType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static OrderSeatShowType getById(int id) {
        for (OrderSeatShowType orderSeatShowType : OrderSeatShowType.values()) {
            if (orderSeatShowType.id == id) {
                return orderSeatShowType;
            }
        }
        return null;
    }

    public String getDes() {
        return this.des;
    }
    public Integer getId() {
        return this.id;
    }
}
