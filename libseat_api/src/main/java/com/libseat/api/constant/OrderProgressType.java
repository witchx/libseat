package com.libseat.api.constant;

public enum OrderProgressType {
    /** 提交*/
    SUBMIT(1, "已提交"),
    /** 支付*/
    PAY(2, "已支付"),
    /** 签到*/
    SIGN(3, "已消费"),
    /** 评价*/
    EVALUATE(4, "已评价");

    private Integer id;
    private String name;
    OrderProgressType(Integer id,String name) {
        this.id = id;
        this.name = name;
    }
    public static OrderProgressType getById(int id) {
        for (OrderProgressType orderProgressType : OrderProgressType.values()) {
            if (orderProgressType.id == id) {
                return orderProgressType;
            }
        }
        return null;
    }
    public String getName() {
        return this.name;
    }
}
