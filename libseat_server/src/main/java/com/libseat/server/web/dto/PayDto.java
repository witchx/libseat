package com.libseat.server.web.dto;

import lombok.Data;

@Data
public class PayDto {
    private Integer orderType;
    private Integer userId;
    private Integer customerId;
    private Integer paymentType;
    private Integer orderId;
    /** 优惠劵*/
    private Integer couponId;
    /** 折扣金额*/
    private String discount;
}
