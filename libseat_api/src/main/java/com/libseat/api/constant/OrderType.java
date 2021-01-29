package com.libseat.api.constant;

public enum OrderType {
    /** 普通*/
    GENERAL(0, "普通订单"),
    /** vip*/
    VIP_CARD(1, "VIP订单");

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
