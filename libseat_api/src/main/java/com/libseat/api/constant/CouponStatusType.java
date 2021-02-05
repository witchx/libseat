package com.libseat.api.constant;

public enum CouponStatusType {

    /**
     * 未使用
     */
    UNUSED(0),
    /**
     * 已过期
     */
    EXPIRED(1),
    /**
     * 已使用
     */
    USED(2);
    private Integer id;
    CouponStatusType(Integer id) {
        this.id = id;
    }
    public static CouponStatusType getById(int id) {
        for (CouponStatusType couponStatusType : CouponStatusType.values()) {
            if (couponStatusType.id == id) {
                return couponStatusType;
            }
        }
        return null;
    }
    public Integer getId() {
        return this.id;
    }
}
