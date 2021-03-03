package com.libseat.api.constant;

/**
 * 座位：提交订单 -> 支付订单 -> 确认消费（签退） -> 完成评价 -> 删除
 * 其他：提交订单 -> 支付订单（完成） -> 删除
 */
public enum OrderProgressType {
    /** 提交*/
    SUBMIT(1, "已提交"),
    /** 支付*/
    PAY(2, "已支付"),
    /** 已确认*/
    CONFIRM(3, "已确认"),
    /** 评价*/
    ACCOMPLISH(4, "已完成");

    private Integer id;
    private String name;
    OrderProgressType(Integer id,String name) {
        this.id = id;
        this.name = name;
    }
    public static OrderProgressType getById(int id) {
        for (OrderProgressType orderProgressType : OrderProgressType.values()) {
            if (orderProgressType.id == id) {
                return orderProgressType;
            }
        }
        return null;
    }
    public String getName() {
        return this.name;
    }
    public Integer getId() {
        return this.id;
    }
}
