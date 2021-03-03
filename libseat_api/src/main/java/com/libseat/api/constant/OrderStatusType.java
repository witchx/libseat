package com.libseat.api.constant;

/**
 * 座位：提交 -> （关闭） -> 支付 -> (取消) -> (消费/签退)确认 -> (评价)完成 -> 删除 状态为 （1、2、3、4、5、6）
 * 其他：提交 -> （关闭） -> 支付（完成） -> 删除 状态只可能为 （1、4、5）
 */
public enum OrderStatusType {
    /** 待支付*/
    UNPAID(1, "待支付"),
    /** 待确认*/
    CONFIRM(2, "待确认"),
    /** 待评价*/
    EVALUATE(3, "待评价"),
    /** 已完成*/
    COMPLETED(4, "已完成"),
    /** 已关闭"*/
    CLOSED(5, "已关闭"),
    /** 已取消"*/
    CANCEL(6, "已取消"),;

    private Integer id;
    private String des;
    OrderStatusType(Integer id, String des) {
        this.id = id;
        this.des = des;
    }
    public static OrderStatusType getById(int id) {
        for (OrderStatusType orderStatusType : OrderStatusType.values()) {
            if (orderStatusType.id == id) {
                return orderStatusType;
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
