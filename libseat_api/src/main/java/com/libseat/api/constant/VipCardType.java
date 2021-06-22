package com.libseat.api.constant;

/**
 * 商品具体类型
 * @author witch
 */


public enum VipCardType {
    /** 储值卡*/
    VALUE_CARD(1, "czk", "储值卡"),
    /** 计次卡*/
    WOULD_CARD(2, "jck", "计次卡"),
    /** 期限卡*/
    TIME_CARD(3, "qxk", "期限卡");
    private Integer id;
    private String name;
    private String des;
    VipCardType(Integer id, String name, String des) {
        this.id = id;
        this.name = name;
        this.des = des;
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
    public String getDes(){
        return this.des;
    }
    public Integer getId(){
        return this.id;
    }
}
