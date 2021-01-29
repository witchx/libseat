package com.libseat.api.constant;

/**
 * 商品类型
 * @author witch
 */

public enum ProductType {
    /** 优惠劵*/
    COUPON(1),
    /** VIP卡*/
    VIP_CARD(2);

    private Integer id;

    ProductType(Integer id) {
        this.id = id;
    }
    public static ProductType getById(int id) {
        for (ProductType productType : ProductType.values()) {
            if (productType.id == id) {
                return productType;
            }
        }
        return null;
    }
}
