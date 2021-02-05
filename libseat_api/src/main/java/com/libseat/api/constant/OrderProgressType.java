package com.libseat.api.constant;

public enum OrderProgressType {
    /** 提交*/
    SUBMIT(0, "已提交"),
    /** 支付*/
    PAY(1, "已支付"),
    /** 签到*/
    SIGN(2, "已签到"),
    /** 关闭*/
    CLOSE(3, "已关闭"),
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
