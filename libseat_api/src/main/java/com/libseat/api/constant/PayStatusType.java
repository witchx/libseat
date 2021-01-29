package com.libseat.api.constant;

public enum  PayStatusType {
    /** 未支付*/
    UNPAID(0),
    /** 已退款*/
    REFUNDED(1),
    /** 已支付*/
    PAID(2);

    private Integer id;
    PayStatusType(Integer id) {
        this.id = id;
    }
    public static PayStatusType getById(int id) {
        for (PayStatusType payStatusType : PayStatusType.values()) {
            if (payStatusType.id == id) {
                return payStatusType;
            }
        }
        return null;
    }
}
