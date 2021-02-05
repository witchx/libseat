package com.libseat.api.constant;

public enum CouponType {
    /** 限时*/
    TIME_LIMIT(1,"xsyhj"),
    /** 限量*/
    QUANTITY_LIMIT(2,"xlyhj");

    private Integer id;
    private String name;
    CouponType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    public static CouponType getById(int id) {
        for (CouponType couponType : CouponType.values()) {
            if (couponType.id == id) {
                return couponType;
            }
        }
        return null;
    }
    public String getName(){
        return this.name;
    }

}
