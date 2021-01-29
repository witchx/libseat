package com.libseat.api.constant;

public enum OrderProgressType {
    /** 提交*/
    SUBMIT(0),
    /** 支付*/
    PAY(1),
    /** 签到*/
    SIGN(2),
    /** 评价*/
    EVALUATE(3);

    private Integer id;
    OrderProgressType(Integer id) {
        this.id = id;
    }
    public static OrderProgressType getById(int id) {
        for (OrderProgressType orderProgressType : OrderProgressType.values()) {
            if (orderProgressType.id == id) {
                return orderProgressType;
            }
        }
        return null;
    }
}
