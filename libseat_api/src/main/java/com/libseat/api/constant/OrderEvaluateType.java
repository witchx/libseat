package com.libseat.api.constant;

public enum OrderEvaluateType {
    /** 一星*/
    ONE_STAR(1),
    /** 二星*/
    TWO_STAR(2),
    /** 三星*/
    THREE_STAR(3),
    /** 四星"*/
    FOUR_STAR(4),
    /** 五星*/
    FIVE_STAR(5);

    private Integer id;
    OrderEvaluateType(Integer id) {
        this.id = id;
    }
    public static OrderEvaluateType getById(int id) {
        for (OrderEvaluateType orderEvaluateType : OrderEvaluateType.values()) {
            if (orderEvaluateType.id == id) {
                return orderEvaluateType;
            }
        }
        return null;
    }
    public Integer getId() {
        return this.id;
    }
}
