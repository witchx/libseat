package com.libseat.api.constant;

public enum OrderType {
    /** 座位*/
    SEAT(0, "座位"),
    /** vip卡*/
    VIP_CARD(1, "会员卡"),
    /** 优惠劵*/
    COUPON(2,"优惠劵"),
    /** 商品*/
    PRODUCT(3,"商品");

    private Integer id;
    private String des;
    OrderType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static OrderType getById(int id) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.id == id) {
                return orderType;
            }
        }
        return null;
    }

    public String getDes() {
        return this.des;
    }
}
