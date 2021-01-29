package com.libseat.api.constant;

public enum ProductStatusType {
    /** 上架*/
    PUTAWAY(1),
    /** 新品*/
    NEW(2),
    /** 推荐*/
    RECOMMEND(3);

    private Integer id;
    ProductStatusType(Integer id) {
        this.id = id;
    }
    public static ProductStatusType getById(int id) {
        for (ProductStatusType productStatusType : ProductStatusType.values()) {
            if (productStatusType.id == id) {
                return productStatusType;
            }
        }
        return null;
    }
}
