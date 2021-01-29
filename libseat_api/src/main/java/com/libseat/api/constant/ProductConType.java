package com.libseat.api.constant;

import lombok.Data;

/**
 * 商品具体类型
 * @author witch
 */


public enum ProductConType {
    /** 限时*/
    TIME_LIMIT(1,"xsyhj"),
    /** 限量*/
    QUANTITY_LIMIT(2,"xlyhj"),
    /** 储值卡*/
    VALUE_CARD(3, "czk"),
    /** 计次卡*/
    WOULD_CARD(4, "jck"),
    /** 期限卡*/
    TIME_CARD(5, "qxk");
    private Integer id;
    private String name;
    ProductConType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public static ProductConType getById(int id) {
        for (ProductConType productConType : ProductConType.values()) {
            if (productConType.id == id) {
                return productConType;
            }
        }
        return null;
    }
    public String getName(){
        return this.name;
    }
}
