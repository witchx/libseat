package com.libseat.api.constant;

/**
 * 商品具体类型
 * @author witch
 */


public enum VipCardType {
    /** 储值卡*/
    VALUE_CARD(1, "czk"),
    /** 计次卡*/
    WOULD_CARD(2, "jck"),
    /** 期限卡*/
    TIME_CARD(3, "qxk");
    private Integer id;
    private String name;
    VipCardType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public static VipCardType getById(int id) {
        for (VipCardType vipCardType : VipCardType.values()) {
            if (vipCardType.id == id) {
                return vipCardType;
            }
        }
        return null;
    }
    public String getName(){
        return this.name;
    }
}
