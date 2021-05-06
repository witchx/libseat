package com.libseat.api.constant;

public enum PaymentType {
    /** 直接支付*/
    MONEY(0, "直接支付"),
    /** 储值卡*/
    VALUE(1, "储值卡"),
    /** 计次卡*/
    WOULD(2,"计次卡"),
    /** 期限卡*/
    TIME(3,"期限卡");

    private Integer id;
    private String des;
    PaymentType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static PaymentType getById(int id) {
        for (PaymentType paymentType : PaymentType.values()) {
            if (paymentType.id == id) {
                return paymentType;
            }
        }
        return null;
    }

    public String getDes() {
        return this.des;
    }
    public Integer getId() {
        return this.id;
    }
}
